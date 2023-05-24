/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.utenze00.bulk;
//import java.sql.SQLException;
//import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

/**
 * Bean che contiene informazioni di buse su sessione utente/password utente/UO di scrivania/CDR utente/esercizi
 */

public class CNRUserInfo extends it.cnr.jada.bulk.UserInfo implements Cloneable {
    private UtenteBulk utente;
    private Integer esercizio;
    private java.lang.String password;
    private java.lang.String nuovaPassword;
    private java.lang.String confermaPassword;
    private java.lang.String descrizioneSessione;
    private Albero_mainBulk alberoRoot;
    private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
    private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
    private DipartimentoBulk dipartimento;
    private java.lang.Integer[] esercizi;
    private String cd_tipo_rapporto;
    //private Parametri_cnrBulk parametri_cnr;
    //private Parametri_cdsBulk parametri_cds;
    private java.lang.String logo;
    private java.lang.String tipo_db;
    private java.lang.String ldap_userid;
    private java.lang.String ldap_password;
    private java.lang.String utente_multiplo;
    private java.util.Dictionary utentiMultipliKeys = new it.cnr.jada.util.OrderedHashtable();

    public CNRUserInfo() {
        super();
    }

    /**
     * Ritorna l'esercizio
     *
     * @param context L'ActionContext della richiesta
     * @return
     */
    public static Integer getEsercizio(it.cnr.jada.action.ActionContext context) {
        return ((CNRUserInfo) context.getUserInfo()).getEsercizio();
    }

    /**
     * Ritorna i Parametri CNR
     *
     * @param context L'ActionContext della richiesta
     * @return
     */
    public static String getCd_tipo_rapporto(it.cnr.jada.action.ActionContext context) {
        return ((CNRUserInfo) context.getUserInfo()).getCd_tipo_rapporto();
    }

    /**
     * Ritorna l'UO
     *
     * @param context L'ActionContext della richiesta
     * @return
     */
    public static it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa(it.cnr.jada.action.ActionContext context) {
        return (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk) ((CNRUserInfo) context.getUserInfo()).getUnita_organizzativa().clone();
    }

    /**
     * Ritorna l'esercizio
     *
     * @param context L'ActionContext della richiesta
     * @return
     */
    public static DipartimentoBulk getDipartimento(it.cnr.jada.action.ActionContext context) {
        if (((CNRUserInfo) context.getUserInfo()).getDipartimento() == null)
            return null;
        return (DipartimentoBulk) ((CNRUserInfo) context.getUserInfo()).getDipartimento().clone();
    }

    /**
     * Operazione di creazione di un business process per l'utente
     *
     * @param context L'ActionContext della richiesta
     * @param name    nome del BP da creare
     * @param params  parametri
     * @return il BP creato
     * @throws BusinessProcessException
     */
    public it.cnr.jada.action.BusinessProcess createBusinessProcess(it.cnr.jada.action.ActionContext context, String name, Object[] params, String tiFunzione) throws it.cnr.jada.action.BusinessProcessException {
        try {
            if (params.length > 0 &&
                    params[0] instanceof String) {
                if (getUtente().isUtenteComune() && getUnita_organizzativa() == null)
                    throw new it.cnr.jada.action.MessageToUser("Unità organizzativa su cui l'utente ha l'accesso richiesto non specificata!");
                String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(), getUtente(), getUtente().isUtenteComune() ? getUnita_organizzativa().getCd_unita_organizzativa() : "*", name, tiFunzione);
                if (mode == null)
                    throw new it.cnr.jada.action.MessageToUser("Accesso non consentito");
                String rmode = params[0].toString();
                if (mode.equals("V") &&
                        rmode.indexOf('M') >= 0)
                    rmode = rmode.replace('M', 'V');
                params[0] = rmode;
            }
            return super.createBusinessProcess(context, name, params);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    public it.cnr.jada.action.BusinessProcess createBusinessProcess(it.cnr.jada.action.ActionContext context, String name, Object[] params) throws it.cnr.jada.action.BusinessProcessException {
        return createBusinessProcess(context, name, params, null);
    }

    public boolean fillFromActionContext(it.cnr.jada.action.ActionContext context, String prefix, int status, FieldValidationMap fillExceptionMap) throws FillException {
        boolean modified = super.fillFromActionContext(context, prefix, status, fillExceptionMap);
        context.setTracingSessionDescription(descrizioneSessione);
        return modified;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'alberoRoot'
     *
     * @return Il valore della proprietà 'alberoRoot'
     */
    public Albero_mainBulk getAlberoRoot() {
        return alberoRoot;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'alberoRoot'
     *
     * @param newAlberoRoot Il valore da assegnare a 'alberoRoot'
     */
    public void setAlberoRoot(Albero_mainBulk newAlberoRoot) {
        alberoRoot = newAlberoRoot;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'cdr'
     *
     * @return Il valore della proprietà 'cdr'
     */
    public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
        if (utente == null || !(utente instanceof UtenteComuneBulk)) {
            cdr = null;
            return null;
        }
        if (cdr == null)
            cdr = ((UtenteComuneBulk) utente).getCdr();
        return cdr;
    }

    public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
        this.cdr = cdr;
    }
/**
 * Ritorna i Parametri CNR
 *
 * @param context    L'ActionContext della richiesta
 * @return
 */
/*public static Parametri_cnrBulk getParametri_cnr(it.cnr.jada.action.ActionContext context) {
	return ((CNRUserInfo)context.getUserInfo()).getParametri_cnr();
}*/
/**
 * Ritorna i Parametri CNR
 *
 * @param context    L'ActionContext della richiesta
 * @return
 */
/*public static Parametri_cdsBulk getParametri_cds(it.cnr.jada.action.ActionContext context) {
	return ((CNRUserInfo)context.getUserInfo()).getParametri_cds();
}*/

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'confermaPassword'
     *
     * @return Il valore della proprietà 'confermaPassword'
     */
    public java.lang.String getConfermaPassword() {
        return confermaPassword;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'confermaPassword'
     *
     * @param newConfermaPassword Il valore da assegnare a 'confermaPassword'
     */
    public void setConfermaPassword(java.lang.String newConfermaPassword) {
        confermaPassword = newConfermaPassword;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'descrizioneSessione'
     *
     * @return Il valore della proprietà 'descrizioneSessione'
     */
    public java.lang.String getDescrizioneSessione() {
        return descrizioneSessione;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'descrizioneSessione'
     *
     * @param newDescrizioneSessione Il valore da assegnare a 'descrizioneSessione'
     */
    public void setDescrizioneSessione(java.lang.String newDescrizioneSessione) {
        descrizioneSessione = newDescrizioneSessione;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'esercizi'
     *
     * @return Il valore della proprietà 'esercizi'
     */
    public java.lang.Integer[] getEsercizi() {
        return esercizi;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'esercizi'
     *
     * @param newEsercizi Il valore da assegnare a 'esercizi'
     */
    public void setEsercizi(java.lang.Integer[] newEsercizi) {
        esercizi = newEsercizi;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'esercizio'
     *
     * @return Il valore della proprietà 'esercizio'
     */
    public Integer getEsercizio() {
        return esercizio;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'esercizio'
     *
     * @param newEsercizio Il valore da assegnare a 'esercizio'
     */
    public void setEsercizio(Integer newEsercizio) {
        esercizio = newEsercizio;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'nuovaPassword'
     *
     * @return Il valore della proprietà 'nuovaPassword'
     */
    public java.lang.String getNuovaPassword() {
        return nuovaPassword;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'nuovaPassword'
     *
     * @param newNuovaPassword Il valore da assegnare a 'nuovaPassword'
     */
    public void setNuovaPassword(java.lang.String newNuovaPassword) {
        nuovaPassword = newNuovaPassword;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'password'
     *
     * @return Il valore della proprietà 'password'
     */
    public java.lang.String getPassword() {
        return password;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'password'
     *
     * @param newPassword Il valore da assegnare a 'password'
     */
    public void setPassword(java.lang.String newPassword) {
        password = newPassword;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'unita_organizzativa'
     *
     * @return Il valore della proprietà 'unita_organizzativa'
     */
    public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
        return unita_organizzativa;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'unita_organizzativa'
     *
     * @param newUnita_organizzativa Il valore da assegnare a 'unita_organizzativa'
     */
    public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
        unita_organizzativa = newUnita_organizzativa;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'utente'
     *
     * @return Il valore della proprietà 'utente'
     */
    public UtenteBulk getUtente() {
        return utente;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'utente'
     *
     * @param newUtente Il valore da assegnare a 'utente'
     */
    public void setUtente(UtenteBulk newUtente) {
        utente = newUtente;
    }

    /**
     * Controlli di validazione della password in modifica della password
     *
     * @throws ValidationException
     */
    public void validaNuovaPassword() throws ValidationException {
        if (nuovaPassword == null || confermaPassword == null)
            throw new ValidationException("E' necessario riempire entrambi i campi Nuova password e Conferma");
        if (!nuovaPassword.equals(confermaPassword))
            throw new ValidationException("La password digitata non corrisponde con quella di conferma.");
        if (nuovaPassword.length() < 8)
            throw new ValidationException("Password troppo corta (min. 8 caratteri)");
    }

    /**
     * @return
     */
	/*public Parametri_cdsBulk getParametri_cds() {
		return parametri_cds;
	}*/

    /**
     * @return
     */
	/*public Parametri_cnrBulk getParametri_cnr() {
		return parametri_cnr;
	}*/

    /**
     * @param bulk
     */
	/*public void setParametri_cds(Parametri_cdsBulk bulk) {
		parametri_cds = bulk;
	}*/

    /**
     * @param bulk
     */
	/*public void setParametri_cnr(Parametri_cnrBulk bulk) {
		parametri_cnr = bulk;
	}*/

    /**
     * @return
     */
    public java.lang.String getLogo() {
        return logo;
    }

    /**
     * @param string
     */
    public void setLogo(java.lang.String val) {
        logo = val;
    }

    /**
     * @return
     */
    public String getCd_tipo_rapporto() {
        return cd_tipo_rapporto;
    }

    /**
     * @param string
     */
    public void setCd_tipo_rapporto(String string) {
        cd_tipo_rapporto = string;
    }

    /**
     * @return
     */
    public java.lang.String getTipo_db() {
        return tipo_db;
    }

    /**
     * @param string
     */
    public void setTipo_db(java.lang.String string) {
        tipo_db = string;
    }

    public DipartimentoBulk getDipartimento() {
        if (utente == null || !(utente instanceof UtenteComuneBulk)) {
            dipartimento = null;
            return null;
        }
        dipartimento = utente.getDipartimento();
        return dipartimento;
    }

    public java.lang.String getLdap_userid() {
        return ldap_userid;
    }

    public void setLdap_userid(java.lang.String ldap_userid) {
        this.ldap_userid = ldap_userid;
    }

    public java.lang.String getLdap_password() {
        return ldap_password;
    }

    public void setLdap_password(java.lang.String ldap_password) {
        this.ldap_password = ldap_password;
    }

    public java.util.Dictionary getUtentiMultipliKeys() {
        return utentiMultipliKeys;
    }

    public void setUtentiMultipliKeys(java.util.Dictionary utentiMultipliKeys) {
        this.utentiMultipliKeys = utentiMultipliKeys;
    }

    public java.lang.String getUtente_multiplo() {
        return utente_multiplo;
    }

    public void setUtente_multiplo(java.lang.String utente_multiplo) {
        this.utente_multiplo = utente_multiplo;
    }
}
