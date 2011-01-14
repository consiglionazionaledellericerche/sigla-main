/*
 * Created on Jun 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RicosResiduiCRUDController extends SimpleDetailCRUDController {

	private CRUDRicostruzioneResiduiBP caller;
	
	/**
	 * @param s
	 * @param class1
	 * @param s1
	 * @param formcontroller
	 */
	public RicosResiduiCRUDController(
		String s,
		Class class1,
		String s1,
		FormController formcontroller) {
		super(s, class1, s1, formcontroller);
		setCaller((CRUDRicostruzioneResiduiBP)formcontroller);
	}

	/**
	 * @param s
	 * @param class1
	 * @param s1
	 * @param formcontroller
	 * @param flag
	 */
	public RicosResiduiCRUDController(
		String s,
		Class class1,
		String s1,
		FormController formcontroller,
		boolean flag) {
		super(s, class1, s1, formcontroller, flag);
		setCaller((CRUDRicostruzioneResiduiBP)formcontroller);
	}

	private CompoundFindClause filter;

	public boolean isReadonly()
	{
		return super.isReadonly() || getCaller().isDettaglioReadonly();	
	}
	public boolean isGrowable()
	{
		return super.isGrowable() && getCaller().isAbilitatoModificaDettagli();
	}
	public boolean isShrinkable()
	{
		return super.isShrinkable() && getCaller().isDettaglioShrinkable();
	}

	public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause)
	{
		filter = compoundfindclause;
		basicReset(actioncontext);
	}

	public boolean isFiltered()
	{
		return filter != null;
	}

	public CompoundFindClause getFilter()
	{
		return filter;
	}

	protected void clearFilter(ActionContext actioncontext)
	{
		filter = null;
	}

	public CRUDRicostruzioneResiduiBP getCaller() {
		return caller;
	}

	public void setCaller(CRUDRicostruzioneResiduiBP residuiBP) {
		caller = residuiBP;
	}

}
