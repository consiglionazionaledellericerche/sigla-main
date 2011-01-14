/*
 * Created on Nov 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import it.cnr.jada.comp.OptionRequestException;
import it.cnr.jada.util.action.OptionBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckDisponibilitaCdrGAEFailed extends OptionRequestException {
	public CheckDisponibilitaCdrGAEFailed(String s) {
		this(s, true);
	}
	public CheckDisponibilitaCdrGAEFailed(String s, boolean doDetail) {

		super("onCheckDisponibilitaCdrGAEFailed", s + ((doDetail)?"<BR>Vuoi continuare ?":""));
		if (doDetail)
			setDetail(new CheckDisponibilitaCdrGAEFailed(s, false));
	}
	
}
