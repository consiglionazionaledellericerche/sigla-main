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