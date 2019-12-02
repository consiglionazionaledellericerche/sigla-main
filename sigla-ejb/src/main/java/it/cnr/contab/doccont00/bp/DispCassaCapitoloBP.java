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

import it.cnr.contab.doccont00.ejb.*;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmp_resBulk;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class DispCassaCapitoloBP extends it.cnr.jada.util.action.SimpleCRUDBP 
{
	private final SimpleDetailCRUDController dispCassa = new SimpleDetailCRUDController("dispCassaCapitolo", Voce_f_saldi_cmpBulk.class,"dispCassaColl",this);
	private final SimpleDetailCRUDController dispCassaRes = new SimpleDetailCRUDController("dispCassaCapitoloRes", Voce_f_saldi_cmp_resBulk.class,"dispCassaColl",this);	
/**
 * DispCassaCapitoloBP constructor comment.
 */
public DispCassaCapitoloBP() {
	super();
}
/**
 * DispCassaCapitoloBP constructor comment.
 * @param function java.lang.String
 */
public DispCassaCapitoloBP(String function) {
	super(function);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2002 18.02.02)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDispCassa() {
	return dispCassa;
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2002 18.02.02)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDispCassaRes() {
	return dispCassaRes;
}
public String getFormTitle() 
{
	return "Disponibilità sul Capitolo";
}
/**
 * Serve per aggiornare le spese del Cdr
 * @param context Il contesto dell'azione
 */
public void  refreshDispCassa( it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException
{
	try
	{
		DispCassaCapitoloBulk model = (DispCassaCapitoloBulk)getModel();
		List result = ((MandatoComponentSession)createComponentSession()).findDisponibilitaDiCassaPerCapitolo(context.getUserContext(), model.getMandato() );
		model.setDispCassaColl( result );

	}
	catch (Exception e )
	{
		throw handleException(e)	;
	}	
	
}
}
