package it.cnr.contab.ordmag.anag00.bp;

import it.cnr.contab.ordmag.anag00.AbilitBeneServMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDAbilitBeneServMagBP extends SimpleCRUDBP{

	private final SimpleDetailCRUDController abilitBeneServMagController = new SimpleDetailCRUDController("AbilitBeneServMagController",AbilitBeneServMagBulk.class,"categoriaGruppoColl",this);	

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDAbilitBeneServMagBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDAbilitBeneServMagBP(String function) {
		super(function);
	}

	public SimpleDetailCRUDController getAbilitBeneServMagController() {
		return abilitBeneServMagController;
	}

	public boolean isEditingAbilitazione() 
	{
		AbilitBeneServMagBulk abilit = (AbilitBeneServMagBulk) getAbilitBeneServMagController().getModel();
		if(abilit != null && abilit.isToBeCreated())
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

