package it.cnr.contab.config00.pdcfin.bulk;

import java.rmi.RemoteException;
import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

public class Elemento_voceBulk extends Elemento_voceBase {
	private java.util.Hashtable ti_appartenenzaKeys;
	private java.util.Hashtable ti_gestioneKeys; 
	private java.util.Hashtable ti_elemento_voceKeys;
	private java.util.Hashtable associazioni = new java.util.Hashtable();

	protected Elemento_voceBulk elemento_padre;
	protected Capoconto_finBulk capoconto_fin = new Capoconto_finBulk();
	private Classificazione_entrateBulk classificazione_entrate;
	private Classificazione_speseBulk classificazione_spese;
	private V_classificazione_vociBulk v_classificazione_voci;
	private boolean isGestoreIstat;
public Elemento_voceBulk( ) 
{
	super();
}
public Elemento_voceBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
/**
 * Dal codice del capitolo ricevente di entrata CDS estra il codice del titolo
 * @return il codice del titolo
 */

public String estraiCodiceTitoloPerCapitoloEntrataCDS() {
	return getCd_elemento_voce().substring(0,2);
}
/**
 * @return it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk
 */
public Capoconto_finBulk getCapoconto_fin() {
	return capoconto_fin;
}
/**
 * Restituisce il valore della proprietà 'cd_ds_elemento_voce'
 *
 * @return Il valore della proprietà 'cd_ds_elemento_voce'
 */
public String getCd_ds_elemento_voce() {
	return getCd_elemento_voce() + " - " + getDs_elemento_voce();
}
public java.lang.String getCd_elemento_padre() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_padre = this.getElemento_padre();
	if (elemento_padre == null)
		return null;
	return elemento_padre.getCd_elemento_voce();
}
/**
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public Elemento_voceBulk getElemento_padre() {
	return elemento_padre;
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTi_appartenenzaKeys() {
	return ti_appartenenzaKeys;
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTi_elemento_voceKeys() {
	return ti_elemento_voceKeys;
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTi_gestioneKeys() {
	return ti_gestioneKeys;
}
/**
 * Inizializza l'attributo <code>esercizio</code>.
 * @param bp business process corrente
 * @param context contesto dell'Action che e' stata generata
 * @return OggettoBulk elemento voce con esercizio inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	setFl_voce_sac( new Boolean( false ) );
	setFl_voce_non_sogg_imp_aut( new Boolean( false ) );
	setFl_recon(new Boolean( false ) );
	setFl_inv_beni_patr(new Boolean( false ));
	setFl_voce_fondo(new Boolean( false ) );
	setFl_check_terzo_siope(new Boolean( false ));
	setFl_inv_beni_comp(new Boolean(false));
	setFl_limite_spesa(new Boolean(false));
	setFl_soggetto_prelievo(new Boolean(false));
	setPerc_prelievo_pdgp_entrate(Utility.ZERO);
	setFl_prelievo(new Boolean(false));
	setFl_solo_competenza(new Boolean(false));
	setFl_solo_residuo(new Boolean(false));
	return this; 
}
/**
 * Inizializza l'attributo <code>esercizio</code>.
 * @param bp business process corrente
 * @param context contesto dell'Action che e' stata generata
 * @return OggettoBulk elemento voce con esercizio inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}


/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo capoconto_fin
 * @return boolean true quando il campo deve essere disabilitato
 */

public boolean isROCapocontoFin() {
	return capoconto_fin == null || capoconto_fin.getCrudStatus() == NORMAL;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo elemento_padre
 * @return boolean true quando il campo deve essere disabilitato
 */

public boolean isROElemento_padre() {
	return elemento_padre == null || elemento_padre.getCrudStatus() == NORMAL;
}
/**
 * @param newCapoconto_fin it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk
 */
public void setCapoconto_fin(Capoconto_finBulk newCapoconto_fin) {
	capoconto_fin = newCapoconto_fin;
}
public void setCd_elemento_padre(java.lang.String cd_elemento_padre) {
	this.getElemento_padre().setCd_elemento_voce(cd_elemento_padre);
}
/**
 * @param newElemento_padre it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_padre(Elemento_voceBulk newElemento_padre) {
	elemento_padre = newElemento_padre;
}
/**
 * @param newTi_appartenenzaKeys java.util.Hashtable
 */
public void setTi_appartenenzaKeys(java.util.Hashtable newTi_appartenenzaKeys) {
	ti_appartenenzaKeys = newTi_appartenenzaKeys;
}
/**
 * @param newTi_elemento_voceKeys java.util.Hashtable
 */
public void setTi_elemento_voceKeys(java.util.Hashtable newTi_elemento_voceKeys) {
	ti_elemento_voceKeys = newTi_elemento_voceKeys;
}
/**
 * @param newTi_gestioneKeys java.util.Hashtable
 */
public void setTi_gestioneKeys(java.util.Hashtable newTi_gestioneKeys) {
	ti_gestioneKeys = newTi_gestioneKeys;
}
/**
 * Esegue la validazione formale dei campi di input
 */



public void validate() throws ValidationException 
{
	if ( getEsercizio() == null  )
		throw new ValidationException( "Il campo ESERCIZIO è obbligatorio." );
	if ( getEsercizio().toString().length() != 4 )
		throw new ValidationException( "Il campo ESERCIZIO deve essere di quattro cifre. " );
	if ( getDs_elemento_voce() == null  )
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );
	if ( !isNullOrEmpty( getCd_proprio_elemento() ) )
	{
		long cdLong;
		try
		{
			cdLong = Long.parseLong( getCd_proprio_elemento() );
		}
		catch (java.lang.NumberFormatException e)
		{
			throw new ValidationException( "Il campo CODICE deve essere numerico. " );			
		}
		if ( cdLong < 0 )
			throw new ValidationException( "Il campo CODICE deve essere maggiore di 0. " );			
	}		
		
}

/**
 * @return
 */
public Classificazione_entrateBulk getClassificazione_entrate() {
	return classificazione_entrate;
}

/**
 * @param bulk
 */
public void setClassificazione_entrate(Classificazione_entrateBulk v_classificazione_entrate) {
	classificazione_entrate = v_classificazione_entrate;
}

public java.lang.Integer getEsercizio_cla_e() {
	Classificazione_entrateBulk classificazione_entrate = this.getClassificazione_entrate();
	if (classificazione_entrate == null)
		return null;
	return classificazione_entrate.getEsercizio();
}

public java.lang.String getCod_cla_e() {
	Classificazione_entrateBulk classificazione_entrate = this.getClassificazione_entrate();
	if (classificazione_entrate == null)
		return null;
	return classificazione_entrate.getCodice_cla_e();
}

public void setEsercizio_cla_e(java.lang.Integer v_esercizio_cla_e) {
	this.getClassificazione_entrate().setEsercizio(v_esercizio_cla_e);
}

public void setCod_cla_e(java.lang.String v_cod_cla_e) {
	this.getClassificazione_entrate().setCodice_cla_e(v_cod_cla_e);
}

	/**
	 * @return
	 */
	public Classificazione_speseBulk getClassificazione_spese() {
		return classificazione_spese;
	}

	/**
	 * @param bulk
	 */
	public void setClassificazione_spese(Classificazione_speseBulk newClassificazione_spese) {
		classificazione_spese = newClassificazione_spese;
	}

	public java.lang.Integer getEsercizio_cla_s() {
		Classificazione_speseBulk classificazione_spese = this.getClassificazione_spese();
		if (classificazione_spese == null)
			return null;
		return classificazione_spese.getEsercizio();
	}

	public java.lang.String getCod_cla_s() {
		Classificazione_speseBulk classificazione_spese = this.getClassificazione_spese();
		if (classificazione_spese == null)
			return null;
		return classificazione_spese.getCodice_cla_s();
	}

	public void setEsercizio_cla_s(java.lang.Integer v_esercizio_cla_s) {
		this.getClassificazione_spese().setEsercizio(v_esercizio_cla_s);
	}

	public void setCod_cla_s(java.lang.String v_cod_cla_s) {		
		this.getClassificazione_spese().setCodice_cla_s(v_cod_cla_s);
	}	

	public V_classificazione_vociBulk getV_classificazione_voci() {
		return v_classificazione_voci;
	}

	public void setV_classificazione_voci(V_classificazione_vociBulk v_class) {
		v_classificazione_voci = v_class;
	}

	public java.lang.Integer getId_classificazione() {
		if (getV_classificazione_voci() == null)
			return null;
		return getV_classificazione_voci().getId_classificazione();
	}

	public void setId_classificazione(java.lang.Integer v_id_classificazione) {
		getV_classificazione_voci().setId_classificazione(v_id_classificazione);
	}
	
	public void setGestoreIstat(boolean b) {
		isGestoreIstat = b;
	}
	
	public boolean isGestoreIstat() {
		return isGestoreIstat;
	}
	
	public boolean isGestoreOk(UserContext context) throws ComponentException, RemoteException {
    		setGestoreIstat(UtenteBulk.isGestoreIstatSiope(context));
			return isGestoreIstat;		
		
	}
}
