package it.cnr.contab.ordmag.anag00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.anag00.AssUnitaOperativaOrdBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDAssUnitaOperativaBP extends SimpleCRUDBP{
	private Unita_organizzativaBulk uoSrivania;

	private final SimpleDetailCRUDController assUnitaOperativaController = new SimpleDetailCRUDController("AssUnitaOperativaController",AssUnitaOperativaOrdBulk.class,"unitaOperativaColl",this);	

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDAssUnitaOperativaBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDAssUnitaOperativaBP(String function) {
		super(function);
	}

	public SimpleDetailCRUDController getAssUnitaOperativaController() {
		return assUnitaOperativaController;
	}

	public boolean isEditingAssociazione() 
	{
		AssUnitaOperativaOrdBulk ass = (AssUnitaOperativaOrdBulk) getAssUnitaOperativaController().getModel();
		if(ass != null && ass.isToBeCreated())
			return true;
			
		return false;
	}	
}
