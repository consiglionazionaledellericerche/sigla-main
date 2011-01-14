package it.cnr.contab.compensi00.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDBonusNucleoFamBP extends SimpleDetailCRUDController{
	public CRUDBonusNucleoFamBP(String s, Class class1, String s1, FormController formcontroller) {
		super(s, class1, s1, formcontroller);
	}	
	protected void setModel(ActionContext actioncontext,OggettoBulk oggettobulk) {
		super.setModel(actioncontext, oggettobulk);
	}
}
