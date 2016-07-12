package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.UtenteBulk;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPJSSESecureSocketFactory;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;

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
	 * @param userID
	 *            utente da validare
	 * @param userPassword
	 *            password dell'utente da validare
	 * @param calNow
	 *            data di accesso del tentativo di validazione
	 * @throws Exception
	 * @throws LDAPException
	 */
	public void validaUtente(UtenteBulk utenteReale,
			java.util.Calendar calNow) throws Exception, LDAPException {
		validaUtente(utenteReale, calNow, false);
	}

	/**
	 * Valida un utente sul server LDAP indicato in questa istanza
	 * 
	 * @param userID
	 *            utente da validare
	 * @param userPassword
	 *            password dell'utente da validare
	 * @param calNow
	 *            data di accesso del tentativo di validazione
	 * @param abilita
	 *            se a true deve affettuare anche l'autorizzazione dell'utente a
	 *            SIGLA oltre ad autenticare
	 * @throws Exception
	 * @throws LDAPException
	 */
	public void validaUtente(UtenteBulk utenteReale,
			java.util.Calendar calNow, boolean abilita) throws Exception,
			LDAPException {
		String userID = utenteReale.getCd_utente_uid();
		String userPassword = utenteReale.getLdap_password();
		
		if (userID == null)
			throw new Exception("Valorizzare il codice utente.");
		if (userPassword == null)
			throw new Exception("Valorizzare la password utente.");

		int iLdapPort = getLdapPort();
		if (getLdapPort() == 0)
			if (isSsl())
				iLdapPort = LDAPConnection.DEFAULT_SSL_PORT;
			else
				iLdapPort = LDAPConnection.DEFAULT_PORT;

		int ldapVersion = LDAPConnection.LDAP_V3;

		java.security.Security
				.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		LDAPJSSESecureSocketFactory ssf = new LDAPJSSESecureSocketFactory();
		LDAPConnection lc = null;

		if (isSsl())
			lc = new LDAPConnection(ssf);
		else
			lc = new LDAPConnection();

		String userDN;

		try {
			// connetti al server
			lc.connect(getLdapHost(), iLdapPort);

			// autenticati al server con l'utente reader
			lc.bind(ldapVersion, getLoginDN(), getPassword().getBytes("UTF8"));

			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB, UID_ATTRIBUTE + "=" + userID,
					null, // return all attributes
					false); // return attrs and values

			LDAPEntry nextEntry = null;
			if (searchResults.hasMore()) {
				nextEntry = searchResults.next();
			} else {
				throw new Exception("L'utente con uid=" + userID
						+ " non è stato trovato.");
			}
			userDN = nextEntry.getDN();

			String returnAttrs[] = { appName, attrDataPw, CODICEFISCALE_ATTRIBUTE };

			LDAPEntry apps = lc.read(userDN, returnAttrs);
			LDAPAttribute sigla = apps.getAttribute(appName);
			LDAPAttribute dataPw = apps.getAttribute(attrDataPw);
			
			if (apps.getAttribute(CODICEFISCALE_ATTRIBUTE) != null)
				utenteReale.setCodiceFiscaleLDAP(apps.getAttribute(CODICEFISCALE_ATTRIBUTE).getStringValue());
			
			// vecchio metodo per semplice confronto
			// non tiene conto delle password cifrate
			/*
			 * LDAPAttribute attr = new LDAPAttribute( "userPassword",
			 * userPassword ); boolean correct = lc.compare( userDN, attr );
			 * 
			 * if (!correct) { throw new
			 * PasswordNonValidaException("La password dell'utente "
			 * +userDN+" non e' corretta.\n"); }
			 */

			// autenticati al server con l'utente finale
			try {
				lc.bind(ldapVersion, userDN, userPassword.getBytes("UTF8"));
			} catch (LDAPException e) {
				if (e.getResultCode() == LDAPException.INVALID_CREDENTIALS)
					throw new PasswordNonValidaException(
							"La password dell'utente " + userDN
									+ " non e' corretta.\n");
			}

			// se non esiste l'attributo e se il server non è di tipo MASTER
			// lanciamo una eccezione in modo che si arrivi al server master
			// per aggiungere l'attributo (il master è l'unico su cui si può
			// scrivere)
			if (sigla == null) {
				if ((abilita == ABILITA_UTENTE_IN_LDAP_TRUE) && isMaster())
					cambiaAbilitazioneUtente(userID, true);
				else
					throw new AttributoNonPresenteException(
							"L'utente "
									+ userDN
									+ " non e' abilitato all'utilizzo dell'applicazione SIGLA.");
			} else {
				String siglaVal = sigla.getStringValue();
				if (siglaVal.equalsIgnoreCase(USER_NOT_ENABLED)) {
					if ((abilita == ABILITA_UTENTE_IN_LDAP_TRUE) && isMaster())
						cambiaAbilitazioneUtente(userID, true);
					else
						throw new UtenteNonAbilitatoException(
								"L'utente "
										+ userDN
										+ " non e' abilitato all'utilizzo dell'applicazione SIGLA.");
				}
				// else
				// throw new
				// Exception("L'utente "+userDN+" non e' abilitato all'utilizzo dell'applicazione SIGLA.");
			}
			/*
			 * if (dataPw!=null) { String dataPwVal = dataPw.getStringValue();
			 * java.text.SimpleDateFormat sdf = new
			 * java.text.SimpleDateFormat("yyyyMMddHHmmss'N'"); java.util.Date
			 * dataUltimoCambioPw = sdf.parse(dataPwVal); java.util.Calendar
			 * calScad = java.util.Calendar.getInstance();
			 * calScad.setTime(calNow.getTime()); calScad.add(calNow.MONTH,-6);
			 * 
			 * if (calScad.getTime().after(dataUltimoCambioPw)) throw new
			 * PasswordLdapScadutaException(); }
			 */
		} catch (UnsupportedEncodingException e) {
			System.out.println("Errore: " + e.toString());
		} finally {
			try {
				// disconnetti
				lc.disconnect();
			} catch (LDAPException ex) {
			}
		} // fine try esterna
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
	 * @param userID
	 *            utente di cui modificare l'abilitazione
	 * @param abilita
	 *            valore dell'abilitazione da impostare
	 * @throws Exception
	 * @throws LDAPException
	 */
	public void cambiaAbilitazioneUtente(String userID, boolean abilita)
			throws Exception, LDAPException {
		if (userID == null)
			throw new Exception("Valorizzare lo User ID.");

		int iLdapPort = getLdapPort();
		if (getLdapPort() == 0)
			if (isSsl())
				iLdapPort = LDAPConnection.DEFAULT_SSL_PORT;
			else
				iLdapPort = LDAPConnection.DEFAULT_PORT;

		int ldapVersion = LDAPConnection.LDAP_V3;

		java.security.Security
				.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		LDAPJSSESecureSocketFactory ssf = new LDAPJSSESecureSocketFactory();
		LDAPConnection lc = null;

		if (isSsl())
			lc = new LDAPConnection(ssf);
		else
			lc = new LDAPConnection();

		String userDN;

		try {
			// connetti al server
			lc.connect(getLdapHost(), iLdapPort);

			// autenticati al server con l'utente reader
			lc.bind(ldapVersion, getLoginDN(), getPassword().getBytes("UTF8"));

			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB, UID_ATTRIBUTE + "=" + userID,
					null, // return all attributes
					false); // return attrs and values

			LDAPEntry nextEntry = null;
			if (searchResults.hasMore()) {
				nextEntry = searchResults.next();
			} else {
				throw new Exception("L'utente con uid=" + userID
						+ " non è stato trovato.");
			}
			userDN = nextEntry.getDN();

			String returnAttrs[] = { appName };

			LDAPEntry apps = lc.read(userDN, returnAttrs);
			LDAPAttribute sigla = apps.getAttribute(appName);

			LDAPModification[] modAppAbil = new LDAPModification[1];

			LDAPAttribute modSigla = null;
			if (abilita == ABILITA_UTENTE_IN_LDAP_TRUE)
				modSigla = new LDAPAttribute(appName, USER_ENABLED);
			else
				modSigla = new LDAPAttribute(appName, USER_NOT_ENABLED);

			if (sigla == null) {
				modAppAbil[0] = new LDAPModification(LDAPModification.ADD,
						modSigla);
				lc.modify(userDN, modAppAbil);
			} else {
				/*
				 * se l'utente va disabilitato lo facciamo solo se non lo è già
				 * se l'utente va abilitato lo facciamo solo se il valore
				 * dell'attributo è "no" questo evita di modificare il valore di
				 * "cnrapp1" se questo ha un valore diverso (ma equivalente
				 * nell'effetto di abilitazione) di "no"
				 */
				if (!(abilita == ABILITA_UTENTE_IN_LDAP_TRUE)
						&& !sigla.getStringValue().equalsIgnoreCase(
								USER_NOT_ENABLED)
						|| (abilita == ABILITA_UTENTE_IN_LDAP_TRUE)
						&& sigla.getStringValue().equalsIgnoreCase(
								USER_NOT_ENABLED)) {

					modAppAbil[0] = new LDAPModification(
							LDAPModification.REPLACE, modSigla);
					lc.modify(userDN, modAppAbil);
				}

			}

		} catch (UnsupportedEncodingException e) {
			System.out.println("Errore: " + e.toString());
		} finally {
			try {
				// disconnetti
				lc.disconnect();
			} catch (LDAPException ex) {
			}
		} // fine try esterna
	}
	
	public  String[] getLdapUser(Integer matricola) throws Exception,
	LDAPException {
		if (matricola == null)
			throw new Exception("Valorizzare la matricola.");
		int iLdapPort = getLdapPort();
		if (getLdapPort() == 0)
			if (isSsl())
				iLdapPort = LDAPConnection.DEFAULT_SSL_PORT;
			else
				iLdapPort = LDAPConnection.DEFAULT_PORT;

		int ldapVersion = LDAPConnection.LDAP_V3;
		java.security.Security
				.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		LDAPJSSESecureSocketFactory ssf = new LDAPJSSESecureSocketFactory();
		LDAPConnection lc = null;
		if (isSsl())
			lc = new LDAPConnection(ssf);
		else
			lc = new LDAPConnection();
		String userDN;
		try {
			lc.connect(getLdapHost(), iLdapPort);
			lc.bind(ldapVersion, getLoginDN(), getPassword().getBytes("UTF8"));
			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB, MATRICOLA_ATTRIBUTE + "=" + matricola,
					null, // return all attributes
					false); // return attrs and values

			LDAPEntry nextEntry = null;
			if (searchResults.hasMore()) {
				nextEntry = searchResults.next();
			} else {
				throw new Exception("L'utente con matricola=" + matricola
						+ " non è stato trovato.");
			}
			userDN = nextEntry.getDN();

			String returnAttrs[] = { UID_ATTRIBUTE, MAIL_ATTRIBUTE };

			LDAPEntry apps = lc.read(userDN, returnAttrs);
			return new String[]{apps.getAttribute(UID_ATTRIBUTE).getStringValue(), apps.getAttribute(MAIL_ATTRIBUTE).getStringValue()};
		} finally {
			lc.disconnect();
		}
	}
	
	public boolean isUtenteAbilitato(String userID) throws Exception,
			LDAPException {
		if (userID == null)
			throw new Exception("Valorizzare lo User ID.");

		int iLdapPort = getLdapPort();
		if (getLdapPort() == 0)
			if (isSsl())
				iLdapPort = LDAPConnection.DEFAULT_SSL_PORT;
			else
				iLdapPort = LDAPConnection.DEFAULT_PORT;

		int ldapVersion = LDAPConnection.LDAP_V3;

		java.security.Security
				.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		LDAPJSSESecureSocketFactory ssf = new LDAPJSSESecureSocketFactory();
		LDAPConnection lc = null;

		if (isSsl())
			lc = new LDAPConnection(ssf);
		else
			lc = new LDAPConnection();

		String userDN;

		try {
			// connetti al server
			lc.connect(getLdapHost(), iLdapPort);

			// autenticati al server con l'utente reader
			lc.bind(ldapVersion, getLoginDN(), getPassword().getBytes("UTF8"));

			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB, UID_ATTRIBUTE + "=" + userID,
					null, // return all attributes
					false); // return attrs and values

			LDAPEntry nextEntry = null;
			if (searchResults.hasMore()) {
				nextEntry = searchResults.next();
			} else {
				throw new Exception("L'utente con uid=" + userID
						+ " non è stato trovato.");
			}
			userDN = nextEntry.getDN();

			String returnAttrs[] = { appName };

			LDAPEntry apps = lc.read(userDN, returnAttrs);
			LDAPAttribute sigla = apps.getAttribute(appName);

			if (sigla == null) {
				return false;
			} else {
				if (sigla.getStringValue().equalsIgnoreCase(USER_NOT_ENABLED))
					return false;
				else
					return true;
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("Errore: " + e.toString());
		} finally {
			try {
				// disconnetti
				lc.disconnect();
			} catch (LDAPException ex) {
			}
		} // fine try esterna
		return false;
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
