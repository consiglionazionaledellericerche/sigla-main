package it.cnr.contab.pdg00.action;

/**
 * Gestione delle Action del crud dei dettagli del PdG
 */

import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.jada.util.action.BulkBP;

public class CRUDPdGAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDPdGAction() {
	super();
}
	/**
	 * Cancellazione concatenata dell'Elemento_voce selezionato alla cancellazione della Linea_attivita selezionata
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */

	public it.cnr.jada.action.Forward doBlankSearchLinea_attivita(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) {

		Pdg_preventivo_detBulk model = (Pdg_preventivo_detBulk)((BulkBP)context.getBusinessProcess()).getModel();
		model.setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk());
		model.setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk());
		return context.findDefaultForward();
	}

}
