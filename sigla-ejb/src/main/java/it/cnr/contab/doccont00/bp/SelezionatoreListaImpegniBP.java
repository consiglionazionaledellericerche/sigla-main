/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
