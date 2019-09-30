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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 30/05/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ExtCassiereCdsBulk extends ExtCassiereCdsBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
		
	public ExtCassiereCdsBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
	public ExtCassiereCdsBulk(java.lang.Integer esercizio, java.lang.String codiceProto) {
		super(esercizio, codiceProto);
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForInsert(crudbp, actioncontext);
	}
	@Override
		public OggettoBulk initializeForSearch(CRUDBP crudbp,
				ActionContext actioncontext) {
			setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			return super.initializeForSearch(crudbp, actioncontext);
		}
	
}