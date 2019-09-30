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
* Created by Generator 1.0
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.intcass.bulk;

import java.util.Dictionary;

public class V_distinte_totBulk extends  V_distinte_totBase {
	public final static Dictionary tipoKeys;

	static 
	{
		tipoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoKeys.put("Altro","Altro");
		tipoKeys.put("Flusso","Flusso");
		tipoKeys.put("Estera","Estera");
		tipoKeys.put("Annulli","Annulli");
	};
	public V_distinte_totBulk() {
		super();
	}
}