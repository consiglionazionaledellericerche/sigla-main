/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 28/02/2020
 */
package it.cnr.contab.util00.bulk;

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

public class HelpBulk extends HelpBase {

	public final static Dictionary bpKeys = new OrderedHashtable();
    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: HELP_LKT
     **/
    public HelpBulk() {
        super();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: HELP_LKT
     **/
    public HelpBulk(java.lang.Integer id) {
        super(id);
    }


    public HelpBulk bpName(java.lang.String bpName) {
        setBpName(bpName);
        return this;
    }

    public HelpBulk page(java.lang.String page) {
        setPage(page);
        return this;
    }

	public static Dictionary getBpKeys() {
		return bpKeys;
	}
}