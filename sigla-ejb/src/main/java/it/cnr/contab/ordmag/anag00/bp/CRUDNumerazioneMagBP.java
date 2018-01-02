package it.cnr.contab.ordmag.anag00.bp;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDNumerazioneMagBP extends SimpleCRUDBP{

	private final SimpleDetailCRUDController numerazioneMagController = new SimpleDetailCRUDController("NumerazioneMagController",NumerazioneMagBulk.class,"numeratoreColl",this);	

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDNumerazioneMagBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDNumerazioneMagBP(String function) {
		super(function);
	}

	public SimpleDetailCRUDController getNumerazioneMagController() {
		return numerazioneMagController;
	}

	public boolean isEditingAbilitazione() 
	{
		NumerazioneMagBulk num = (NumerazioneMagBulk) getNumerazioneMagController().getModel();
		if(num != null && num.isToBeCreated())
			return true;
			
		return false;
	}

	@Override
	public OggettoBulk initializeModelForFreeSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		MagazzinoBulk mag = (MagazzinoBulk)oggettobulk;
		mag.setInQuery(true);
		return super.initializeModelForFreeSearch(actioncontext, mag);
	}

	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		MagazzinoBulk mag = (MagazzinoBulk)oggettobulk;
		mag.setInQuery(true);
		return super.initializeModelForSearch(actioncontext, mag);
	}	
}

