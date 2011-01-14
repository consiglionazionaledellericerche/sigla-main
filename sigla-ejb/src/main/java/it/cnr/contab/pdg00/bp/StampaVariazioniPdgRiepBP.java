package it.cnr.contab.pdg00.bp;


import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Insert the type's description here.
 * Creation date: (18/10/2005 16.21.29)
 * @author: 
 */
public class StampaVariazioniPdgRiepBP extends it.cnr.contab.reports.bp.ParametricPrintBP{

	private SimpleDetailCRUDController crudRiepilogoVariazioni = new SimpleDetailCRUDController( "RiepilogoVariazioni", Pdg_variazioneBulk.class, "riepilogovariazioni", this) ;
		
		
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudRiepilogoVariazioni() {
		return crudRiepilogoVariazioni;
	}

	/**
	 * @param controller
	 */
	public void setCrudRiepilogoVariazioni(SimpleDetailCRUDController controller) {
		crudRiepilogoVariazioni = controller;
	}
}