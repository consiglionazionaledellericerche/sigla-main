package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;


public class CRUDTipoMovimentoMagBP extends SimpleCRUDBP  {
	
	private final SimpleDetailCRUDController righeMovimentoAzioni = new SimpleDetailCRUDController("righeMovimentoAzioni",TipoMovimentoMagAzBulk.class,"righeTipoMovimentoAzioni",this);
		

	public CRUDTipoMovimentoMagBP() {
		super();
	}
	
	public CRUDTipoMovimentoMagBP(String function) {
		super(function);
	}
	
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabTipoMovimentoMag");
		super.init(config, actioncontext);
	}
	
	protected void setTab() {
		setTab("tab","tab");
		setTab("tabTipoMovAzioni","tabTipoMovAzioni");
	}

	public SimpleDetailCRUDController getRigheMovimentoAzioni() {
		return righeMovimentoAzioni;
	}

	@Override
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag)
			throws BusinessProcessException {
		// TODO Auto-generated method stub
		super.basicEdit(actioncontext, oggettobulk, flag);
		TipoMovimentoMagBulk model = (TipoMovimentoMagBulk ) this.getModel();
		righeMovimentoAzioni.setModelIndex(actioncontext, 0);		
	}
}
