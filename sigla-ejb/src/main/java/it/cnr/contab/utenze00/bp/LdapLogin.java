package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.UtenteBulk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

public class LdapLogin {

    public static final boolean ABILITA_UTENTE_IN_LDAP_TRUE = true;
    public static final boolean ABILITA_UTENTE_IN_LDAP_FALSE = false;

    private static final String USER_NOT_ENABLED = "no";
    private static final String USER_ENABLED = "si"; // questo può essere un
    // valore qualunque
    // (diverso da "no")
    private static final String UID_ATTRIBUTE = "uid";
    private static final String MATRICOLA_ATTRIBUTE = "matricola";
    private static final String MAIL_ATTRIBUTE = "mail";
    private static final String CODICEFISCALE_ATTRIBUTE = "codicefiscale";
    private static final Logger logger = LoggerFactory.getLogger(LdapLogin.class);
    private String appName; // = "cnrapp1";
    private String baseDN; // = "o=cnr,c=it";
    private String attrDataPw = "dataultimocambiopw";
    private String ldapHost; // = "siglaas4.cedrc.cnr.it";
    private int ldapPort; // = "389";
    private String loginDN; // = "cn=Manager,o=cnr,c=it";
    private String password; // = "xxxxx";
    private boolean ssl = false;
    private boolean master = false;

    public LdapLogin() {
    }

    public LdapLogin(String ldapHost, int ldapPort, String loginDN,
                     String password, boolean ssl, boolean master) {
        setLdapHost(ldapHost);
        setLdapPort(ldapPort);
        setLoginDN(loginDN);
        setPassword(password);
        setSsl(ssl);
        setMaster(master);
    }

    /**
     * Valida un utente sul server LDAP indicato in questa istanza
     *
     * @param userID       utente da validare
     * @param userPassword password dell'utente da validare
     * @param calNow       data di accesso del tentativo di validazione
     * @throws Exception
     */
    public void validaUtente(UtenteBulk utenteReale,
                             java.util.Calendar calNow) throws Exception {
        validaUtente(utenteReale, calNow, false);
    }

    private LdapContext getLdapContext(String principal, String credentials) throws NamingException {
        String protocol = "ldap://";
        int iLdapPort = getLdapPort();
        if (getLdapPort() == 0) {
            if (isSsl()) {
                iLdapPort = 636;
            } else {
                iLdapPort = 389;
            }
        }
        if (isSsl()) {
            protocol = "ldaps://";
        }

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, protocol + ldapHost + ":" + iLdapPort);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);
        env.put("com.sun.jndi.ldap.connect.timeout", "5000");
        env.put("com.sun.jndi.ldap.read.timeout", "30000");

        return new InitialLdapContext(env, null);
    }

    private Map<String, Object> getLDAPAttributes(LdapContext ctxGC, String search) throws NamingException {
        String[] returnAttrs = {appName, attrDataPw, CODICEFISCALE_ATTRIBUTE, MAIL_ATTRIBUTE};
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnAttrs);

        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> answer = ctxGC.search(baseDN, search, searchCtls);
        Map<String, Object> amap = new HashMap<String, Object>();
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            Attributes attrs = sr.getAttributes();
            if (attrs != null) {
                amap.put("DN", sr.getNameInNamespace());
                NamingEnumeration<?> ne = attrs.getAll();
                while (ne.hasMore()) {
                    Attribute attr = (Attribute) ne.next();
                    if (attr.size() == 1) {
                        amap.put(attr.getID(), attr.get());
                    } else {
                        HashSet<String> s = new HashSet<String>();
                        NamingEnumeration<?> n = attr.getAll();
                        while (n.hasMoreElements()) {
                            s.add((String) n.nextElement());
                        }
                        amap.put(attr.getID(), s);
                    }
                }
                ne.close();
            }
        }
        return amap;

    }

    /**
     * Valida un utente sul server LDAP indicato in questa istanza
     *
     * @param userID       utente da validare
     * @param userPassword password dell'utente da validare
     * @param calNow       data di accesso del tentativo di validazione
     * @param abilita      se a true deve affettuare anche l'autorizzazione dell'utente a
     *                     SIGLA oltre ad autenticare
     * @throws Exception
     */
    public void validaUtente(UtenteBulk utenteReale,
                             java.util.Calendar calNow, boolean abilita) throws Exception {
        String userID = utenteReale.getCd_utente_uid();
        String userPassword = utenteReale.getLdap_password();

        if (userID == null)
            throw new Exception("Valorizzare il codice utente.");
        if (userPassword == null)
            throw new Exception("Valorizzare la password utente.");

        String userDN = null;
        LdapContext ctxGC = getLdapContext(getLoginDN(), getPassword());
        try {
            Map<String, Object> amap = getLDAPAttributes(ctxGC, UID_ATTRIBUTE + "=" + userID);

            if (amap.get(CODICEFISCALE_ATTRIBUTE) != null)
                utenteReale.setCodiceFiscaleLDAP(String.valueOf(amap.get(CODICEFISCALE_ATTRIBUTE)));

            if (amap.isEmpty())
                throw new Exception("L'utente con uid=" + userID
                        + " non è stato trovato.");
            userDN = String.valueOf(amap.get("DN"));
            String sigla = String.valueOf(amap.get(appName));
            LdapContext ctxGCUser = null;
            try {
                ctxGCUser = getLdapContext(userDN, userPassword);

            } catch (javax.naming.AuthenticationException _ex) {
                throw new PasswordNonValidaException(
                        "La password dell'utente " + userDN + " non e' corretta.\n");
            } finally {
                if (ctxGCUser != null)
                    ctxGCUser.close();
            }
            /**
             * se non esiste l'attributo e se il server non è di tipo MASTER
             * lanciamo una eccezione in modo che si arrivi al server master
             * per aggiungere l'attributo (il master è l'unico su cui si può
             * scrivere
             */
            if (sigla == null) {
                if ((abilita == ABILITA_UTENTE_IN_LDAP_TRUE) && isMaster())
                    cambiaAbilitazioneUtente(userID, true);
                else
                    throw new AttributoNonPresenteException(
                            "L'utente " + userDN + " non e' abilitato all'utilizzo dell'applicazione SIGLA.");
            } else {
                if (sigla.equalsIgnoreCase(USER_NOT_ENABLED)) {
                    if ((abilita == ABILITA_UTENTE_IN_LDAP_TRUE) && isMaster())
                        cambiaAbilitazioneUtente(userID, true);
                    else
                        throw new UtenteNonAbilitatoException(
                                "L'utente "
                                        + userDN
                                        + " non e' abilitato all'utilizzo dell'applicazione SIGLA.");
                }
            }
        } catch (NamingException nex) {
            logger.error("", nex);
            throw nex;
        } finally {
            ctxGC.close();
        }
    }

    /**
     * Abilita l'utente "userID" se "abilitazione" è true, altrimenti disabilita
     * con le seguenti regole: se l'attributo su ldap "cnrapp1" non esiste lo
     * aggiunge a "si" se "abilita" = true, a "no" se "abilita" = false se
     * l'attributo su ldap "cnrapp1" = "no" e "abilita" = true, cambia "cnrapp1"
     * a "si" se l'attributo su ldap "cnrapp1" != "no" e "abilita" = true,
     * cambia "cnrapp1" a "si" se l'attributo su ldap "cnrapp1" = "no" e
     * "abilita" = false non fa nulla se l'attributo su ldap "cnrapp1" = "si" e
     * "abilita" = false cambia "cnrapp1" a "si"
     *
     * @param userID  utente di cui modificare l'abilitazione
     * @param abilita valore dell'abilitazione da impostare
     * @throws Exception
     */
    public void cambiaAbilitazioneUtente(String userID, boolean abilita)
            throws Exception {
        if (userID == null)
            throw new Exception("Valorizzare lo User ID.");

        LdapContext ctxGC = null;
        try {
            ctxGC = getLdapContext(getLoginDN(), getPassword());
            Map<String, Object> amap = getLDAPAttributes(ctxGC, UID_ATTRIBUTE + "=" + userID);
            if (amap.isEmpty())
                throw new Exception("L'utente con uid=" + userID
                        + " non è stato trovato.");
            String userDN = String.valueOf(amap.get("DN"));
            String sigla = String.valueOf(amap.get(appName));
            ModificationItem[] mods = new ModificationItem[1];
            Attribute mod0;
            if (abilita == ABILITA_UTENTE_IN_LDAP_TRUE) {
                mod0 = new BasicAttribute(appName, USER_ENABLED);
            } else {
                mod0 = new BasicAttribute(appName, USER_NOT_ENABLED);

            }
            if (sigla == null) {
                mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, mod0);
                ctxGC.modifyAttributes(userDN, mods);
            } else {
                /*
                 * se l'utente va disabilitato lo facciamo solo se non lo è già
                 * se l'utente va abilitato lo facciamo solo se il valore
                 * dell'attributo è "no" questo evita di modificare il valore di
                 * "cnrapp1" se questo ha un valore diverso (ma equivalente
                 * nell'effetto di abilitazione) di "no"
                 */
                if (!(abilita == ABILITA_UTENTE_IN_LDAP_TRUE)
                        && !sigla.equalsIgnoreCase(
                        USER_NOT_ENABLED)
                        || (abilita == ABILITA_UTENTE_IN_LDAP_TRUE)
                        && sigla.equalsIgnoreCase(
                        USER_NOT_ENABLED)) {
                    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod0);
                    ctxGC.modifyAttributes(userDN, mods);
                }
            }

        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
            throw e;
        } finally {
            if (ctxGC != null)
                ctxGC.close();
        } // fine try esterna
    }

    public String[] getLdapUser(Integer matricola) throws Exception {
        if (matricola == null)
            throw new Exception("Valorizzare la matricola.");
        LdapContext ctxGC = null;
        try {
            ctxGC = getLdapContext(getLoginDN(), getPassword());
            Map<String, Object> amap = getLDAPAttributes(ctxGC, MATRICOLA_ATTRIBUTE + "=" + matricola);
            if (amap.isEmpty())
                throw new Exception("L'utente con matricola=" + matricola
                        + " non è stato trovato.");
            return new String[]{
                    String.valueOf(amap.get(UID_ATTRIBUTE)),
                    String.valueOf(amap.get(MAIL_ATTRIBUTE))};
        } finally {
            if (ctxGC != null)
                ctxGC.close();
        }
    }

    public boolean isUtenteAbilitato(String userID) throws Exception {
        if (userID == null)
            throw new Exception("Valorizzare lo User ID.");
        LdapContext ctxGC = null;
        try {
            ctxGC = getLdapContext(getLoginDN(), getPassword());
            Map<String, Object> amap = getLDAPAttributes(ctxGC, UID_ATTRIBUTE + "=" + userID);
            if (amap.isEmpty())
                throw new Exception("L'utente con uid=" + userID
                        + " non è stato trovato.");
            String sigla = String.valueOf(amap.get(appName));

            if (sigla == null) {
                return false;
            } else {
                return !sigla.equalsIgnoreCase(USER_NOT_ENABLED);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
            throw e;
        } finally {
            if (ctxGC != null)
                ctxGC.close();
        }
    }

    public String getLdapHost() {
        return ldapHost;
    }

    public void setLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
    }

    public int getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(int ldapPort) {
        this.ldapPort = ldapPort;
    }

    public String getLoginDN() {
        return loginDN;
    }

    public void setLoginDN(String loginDN) {
        this.loginDN = loginDN;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
