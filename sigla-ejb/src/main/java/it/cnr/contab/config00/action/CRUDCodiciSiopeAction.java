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

/*
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.action;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bp.CRUDCodiciSiopeBP;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_tipologia_istat_siopeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_tipologia_istat_siopeHome;
import it.cnr.contab.doccont00.bp.ConsConfrontoEntSpeBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDCodiciSiopeAction extends CRUDAction {

	
	public CRUDCodiciSiopeAction() {
		super();
	}
	
	
	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			if (context.getBusinessProcess() instanceof CRUDCodiciSiopeBP) {
			CRUDCodiciSiopeBP bp = (CRUDCodiciSiopeBP)getBusinessProcess(context);
			Codici_siopeBulk codiciSiope = (Codici_siopeBulk)bp.getModel();
			fillModel( context );
			bp.validainserimento(context, codiciSiope);	
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
		
		return super.doSalva(context);
	}
	
	public Forward doElimina(ActionContext actioncontext) throws RemoteException{
		try{
			if (actioncontext.getBusinessProcess() instanceof CRUDCodiciSiopeBP)
				return super.doElimina(actioncontext);
			/* Gestione sospesa 
			 * SimpleCRUDBP simplebp = (SimpleCRUDBP) getBusinessProcess(actioncontext);
	        Ass_tipologia_istat_siopeBulk ass = (Ass_tipologia_istat_siopeBulk)simplebp.getModel();
	        fillModel(actioncontext);
				java.sql.Connection conn = null;
	        try{
				conn=it.cnr.jada.util.ejb.EJBCommonServices.getConnection(actioncontext);
				it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
				Ass_tipologia_istat_siopeHome AssHome = (Ass_tipologia_istat_siopeHome)homeCache.getHome(Ass_tipologia_istat_siopeBulk.class);
				if (AssHome.IsUtilizzato(ass)){
					throw new ApplicationException("Non è possibile eliminare l'associazione perchè già utilizzata.");
				}
			}finally{
				   if (conn!=null)
   						  try{conn.close();}catch( java.sql.SQLException e ){};
			}*/
	    }
	    catch(Throwable throwable){
	        return handleException(actioncontext, throwable);
	    }
	    return super.doElimina(actioncontext);
}
	
}


