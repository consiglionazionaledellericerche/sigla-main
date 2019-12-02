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
* Date 30/08/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class Monito_cococoBulk extends Monito_cococoBase {
	
	private static OrderedHashtable attivitaKeys;
	final public static String ATT_ISTIT 	= "1";
	final public static String ATT_ALTRE	= "0";
	
	public Monito_cococoBulk() {
		super();
	}
	public OrderedHashtable getAttivitaKeys() {
		if (attivitaKeys == null)
		{
			attivitaKeys = new OrderedHashtable();
			attivitaKeys.put("1", "Attività Istituzionale");	
			attivitaKeys.put("0", "Altre Attività");	
		}
		return attivitaKeys;
	}
	
	/**
	 * @param hashtable
	 */
	public static void setAttivitaKeys(OrderedHashtable hashtable) {
		attivitaKeys = hashtable;
	}

}