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