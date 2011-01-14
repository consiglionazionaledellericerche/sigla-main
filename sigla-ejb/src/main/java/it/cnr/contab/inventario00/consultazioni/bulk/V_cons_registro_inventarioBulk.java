/*
* Creted by Generator 1.0
* Date 21/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import java.util.Dictionary;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_registro_inventarioBulk extends V_cons_registro_inventarioBase {
	public V_cons_registro_inventarioBulk() {
		super();
	}
	 public final static java.util.Dictionary ti_movimento;
	  
	    public final static String CARICO      = "Carico";
		public final static String SCARICO     = "Scarico";

		static {
				
				ti_movimento = new it.cnr.jada.util.OrderedHashtable();
				ti_movimento.put(it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk.CARICO,CARICO);
				ti_movimento.put(it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk.SCARICO,SCARICO);
			};
		public Dictionary getTipoMovimentoKey(){
			return ti_movimento;
		}	
}