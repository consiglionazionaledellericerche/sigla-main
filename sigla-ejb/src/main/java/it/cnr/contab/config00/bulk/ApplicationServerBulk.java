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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ApplicationServerBulk extends ApplicationServerBase {
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: APPLICATION_SERVER
	 **/
	public ApplicationServerBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: APPLICATION_SERVER
	 **/
	public ApplicationServerBulk(java.lang.String hostname, java.lang.String session_id) {
		super(hostname, session_id);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		setAttivo(new Boolean(false));
		setLogin(new Boolean(false));
		return this;
	}
}