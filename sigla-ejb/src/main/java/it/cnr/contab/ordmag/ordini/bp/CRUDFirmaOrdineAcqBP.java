package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class CRUDFirmaOrdineAcqBP extends CRUDOrdineAcqBP{
	public CRUDFirmaOrdineAcqBP(String function) throws BusinessProcessException{
		super(function);
	}
	public CRUDFirmaOrdineAcqBP() throws BusinessProcessException{
		super();
	}

	@Override
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[9];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.sblocca");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.firmaOrdine");

		return toolbar;
	}
	public boolean isFirmaOrdineButtonHidden() {
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
	}

	public boolean isSbloccaButtonHidden() {
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
	}
	@Override
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		OrdineAcqBulk ordine = (OrdineAcqBulk)oggettobulk;
		ordine.setIsForFirma(true);
		return super.find(actioncontext, compoundfindclause, ordine);
	}
	@Override
	public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
		completeSearchTools(actioncontext, this);
		validate(actioncontext);
		saveChildren(actioncontext);
		update(actioncontext);
		if(getMessage() == null)
			setMessage("Salvataggio eseguito in modo corretto.");
		commitUserTransaction();
		try
		{
			basicEdit(actioncontext, getModel(), true);
		}
		catch(BusinessProcessException businessprocessexception)
		{
			setModel(actioncontext, null);
			setDirty(false);
			throw businessprocessexception;
		}
	}
	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		// TODO Auto-generated method stub
		OrdineAcqBulk ordine = (OrdineAcqBulk)oggettobulk;
		ordine.setIsForFirma(true);
		return super.initializeModelForSearch(actioncontext, ordine);
	}
}
