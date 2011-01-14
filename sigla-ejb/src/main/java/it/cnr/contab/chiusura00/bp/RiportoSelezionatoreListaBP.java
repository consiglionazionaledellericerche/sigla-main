/*
 * Created on Jan 11, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.chiusura00.bp;

import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RiportoSelezionatoreListaBP extends SelezionatoreListaBP {

	private boolean selectAllOnly;

	public RiportoSelezionatoreListaBP()
	{
		super();
	}

	public RiportoSelezionatoreListaBP(String s)
	{
		super(s);
	}

	public boolean isSelezionaButtonHidden()
	{
		return !table.isMultiSelection() || isSelectAllOnly();
	}
	
	public boolean isSelectAllOnly() {
		return selectAllOnly;
	}

	public void setSelectAllOnly(boolean b) {
		selectAllOnly = b;
	}

}
