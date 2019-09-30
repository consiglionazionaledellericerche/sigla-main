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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.consultazioni.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class ConsPDGGIstEtrBP extends ConsPDGGIstBP {
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			return createPdggAreaComponentSession().findConsultazioneEtr(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createPdggAreaComponentSession().findConsultazioneEtr(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}
