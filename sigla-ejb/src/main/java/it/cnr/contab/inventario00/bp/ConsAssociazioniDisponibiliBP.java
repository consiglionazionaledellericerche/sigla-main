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

package it.cnr.contab.inventario00.bp;
/**
  *  Questa classe gestisce le operazioni di business relative all'associazione di 
  *	una Fattura Passiva a dei beni esistenti nel DB.
**/

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
 
public class ConsAssociazioniDisponibiliBP extends ConsultazioniBP{	
	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
			CompoundFindClause clauses = new CompoundFindClause();
			if(!isUoEnte(context)){
				Id_inventarioBulk pg_inv=((BuonoCaricoScaricoComponentSession)EJBCommonServices.createEJB(
					"CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
					BuonoCaricoScaricoComponentSession.class)).caricaInventario(context.getUserContext());
				clauses.addClause("AND", "pg_inventario", SQLBuilder.EQUALS, pg_inv.getPg_inventario());			
			}
			clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
			setBaseclause(clauses);

			super.init(config,context);
					
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}	
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
}
