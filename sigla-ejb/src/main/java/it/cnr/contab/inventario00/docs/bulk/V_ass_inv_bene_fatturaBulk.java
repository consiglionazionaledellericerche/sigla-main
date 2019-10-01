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
package it.cnr.contab.inventario00.docs.bulk;
import java.util.Dictionary;

public class V_ass_inv_bene_fatturaBulk extends V_ass_inv_bene_fatturaBase {
  public final static java.util.Dictionary ti_movimento;
  
    public final static String CARICO      = "Carico";
	public final static String SCARICO     = "Scarico";

	static {
			
			ti_movimento = new it.cnr.jada.util.OrderedHashtable();
			ti_movimento.put(it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk.CARICO,CARICO);
			ti_movimento.put(it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk.SCARICO,SCARICO);
		};
	public V_ass_inv_bene_fatturaBulk() {
		super();
	}
	public Dictionary getTipoMovimentoKey(){
		return ti_movimento;
	}
	
}