package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione dei Costi Scaricati verso altra UO
 */

public class CRUDCostiScaricatiBP extends it.cnr.jada.util.action.CRUDListaBP {

	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;

public CRUDCostiScaricatiBP() {
	super();
}

public CRUDCostiScaricatiBP(String function) {
	super(function);
}

/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param function	La funzione con cui è stato creato il BusinessProcess
 * @param cdr	
 */
public CRUDCostiScaricatiBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	super(function);
	setCentro_responsabilita(cdr);
}

public it.cnr.jada.bulk.OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk o = (it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)super.createEmptyModelForSearch(context);
		o.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		o.setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk( centro_responsabilita.getCd_centro_responsabilita() ));
		return o;
	} catch(Exception e) {
		throw handleException(e);
	}
}

public it.cnr.jada.bulk.OggettoBulk createEmptyModelForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk o = (it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)super.createEmptyModelForSearch(context);
		o.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		o.setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk( centro_responsabilita.getCd_centro_responsabilita() ));
		return o;
	} catch(Exception e) {
		throw handleException(e);
	}
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'centro_responsabilita'
 *
 * @return Il valore della proprietà 'centro_responsabilita'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'centro_responsabilita'
 *
 * @param newCentro_responsabilita	Il valore da assegnare a 'centro_responsabilita'
 */
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
		centro_responsabilita = newCentro_responsabilita;
	}
}