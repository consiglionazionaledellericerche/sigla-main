/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 17/02/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class VIntrastatInviatoBulk extends VIntrastatInviatoBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INTRASTAT_INVIATO
	 **/
	public VIntrastatInviatoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INTRASTAT_INVIATO
	 **/
	public VIntrastatInviatoBulk(java.lang.String uo,java.lang.Integer esercizio,java.lang.Long progressivo,java.lang.String protocollo_invio,java.lang.Long riga_invio) {
		super(uo,esercizio,progressivo,protocollo_invio,riga_invio);
	}
	
	
	private static OrderedHashtable tipoKeys;
	final public static String ATTIVA 	= "Fattura Attiva";
	final public static String PASSIVA	= "Fattura Passiva";
	
	public OrderedHashtable getTipoKeys() {
		if (tipoKeys == null)
		{
			tipoKeys = new OrderedHashtable();
			tipoKeys.put("Fattura Attiva", "Fattura Attiva");	
			tipoKeys.put("Fattura Passiva", "Fattura Passiva");	
		}
		return tipoKeys;
	}
	
	/**
	 * @param hashtable
	 */
	public static void setTipoKeys(OrderedHashtable hashtable) {
		tipoKeys = hashtable;
	}
	private static OrderedHashtable tiBeneServizioKeys;
	final public static String BENI 	= "Beni";
	final public static String SERVIZI	= "Servizi";
	
	public OrderedHashtable getTiBeneServizioKeys() {
		if (tiBeneServizioKeys == null)
		{
			tiBeneServizioKeys = new OrderedHashtable();
			tiBeneServizioKeys.put("Beni", "Beni");	
			tiBeneServizioKeys.put("Servizi", "Servizi");	
		}
		return tiBeneServizioKeys;
	}
	
	/**
	 * @param hashtable
	 */
	public static void setTiBeneServizioKeys(OrderedHashtable hashtable) {
		tiBeneServizioKeys = hashtable;
	}
		
	private static java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final int GENNAIO = 1;
	public static final int FEBBRAIO = 2;
	public static final int MARZO = 3;
	public static final int APRILE = 4;
	public static final int MAGGIO = 5;
	public static final int GIUGNO = 6;
	public static final int LUGLIO = 7;
	public static final int AGOSTO = 8;
	public static final int SETTEMBRE = 9;
	public static final int OTTOBRE = 10;
	public static final int NOVEMBRE = 11;
	public static final int DICEMBRE = 12;

	static {
		meseKeys.put(new Integer(GENNAIO),"Gennaio");
		meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
		meseKeys.put(new Integer(MARZO),"Marzo");
		meseKeys.put(new Integer(APRILE),"Aprile");
		meseKeys.put(new Integer(MAGGIO),"Maggio");
		meseKeys.put(new Integer(GIUGNO),"Giugno");
		meseKeys.put(new Integer(LUGLIO),"Luglio");
		meseKeys.put(new Integer(AGOSTO),"Agosto");
		meseKeys.put(new Integer(SETTEMBRE),"Settembre");
		meseKeys.put(new Integer(OTTOBRE),"Ottobre");
		meseKeys.put(new Integer(NOVEMBRE),"Novembre");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
	}
	/**
	 * @param dictionary
	 */
	public final java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}
	/**
	 * @param dictionary
	 */
	public static void setMeseKeys(java.util.Dictionary dictionary) {
		meseKeys = dictionary;
	}
}