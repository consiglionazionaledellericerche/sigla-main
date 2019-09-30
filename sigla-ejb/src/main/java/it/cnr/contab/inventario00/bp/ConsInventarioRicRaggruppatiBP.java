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

import it.cnr.contab.inventario00.consultazioni.bulk.VInventarioRicognizioneBulk;
import it.cnr.contab.inventario00.ejb.ConsRegistroInventarioComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsInventarioRicRaggruppatiBP extends ConsultazioniBP
{
	public ConsRegistroInventarioComponentSession createConsRegistroInventarioComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsRegistroInventarioComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_ConsRegistroInventarioComponentSession", ConsRegistroInventarioComponentSession.class);
	}
	
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			this.setFindclause(compoundfindclause);
			VInventarioRicognizioneBulk inv ;
		    if (this.getParent() instanceof ConsInventarioRicBP){ 
		    	ConsInventarioRicBP bpPar=(ConsInventarioRicBP)this.getParent();
		    	 inv = (VInventarioRicognizioneBulk)bpPar.getModel();
		    }else
		    	 inv = (VInventarioRicognizioneBulk)getModel();
			return createConsRegistroInventarioComponentSession().findConsultazioneRicognizione(context.getUserContext(),getBaseclause(),getFindclause(),inv);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}
