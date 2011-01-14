package it.cnr.contab.doccont00.bp;

import it.cnr.jada.util.jsp.*;
/**
 * Insert the type's description here.
 * Creation date: (02/07/2003 12.15.58)
 * @author: Simonetta Costa
 */
public class SelezionatoreListaImpegniBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
/**
 * SelezionatoreListaImpegniBP constructor comment.
 */
public SelezionatoreListaImpegniBP() {
	super();
}
/**
 * SelezionatoreListaImpegniBP constructor comment.
 * @param function java.lang.String
 */
public SelezionatoreListaImpegniBP(String function) {
	super(function);
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.multiSelection");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.selectAll");
	return toolbar;
}
}
