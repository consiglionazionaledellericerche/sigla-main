package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (25/08/2004 14.17.32)
 * @author: Gennaro Borriello
 */
public class Stampa_fat_pas_per_vpVBulk extends Stampa_docamm_per_voce_del_pianoVBulk {
/**
 * Stampa_fat_pas_per_vpVBulk constructor comment.
 */

	private String stato;
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	
	final public static String STATO_I = "I";
	final public static String STATO_C = "C";
	final public static String STATO_P = "P";
	final public static String STATO_A = "A";
	final public static String STATO_TUTTI = "*";
	
			
	static {
			ti_statoKeys.put(STATO_I,"Iniziale");
			ti_statoKeys.put(STATO_C,"Contabilizzato");
			ti_statoKeys.put(STATO_P,"Pagata");
			ti_statoKeys.put(STATO_A,"Annullata");
			ti_statoKeys.put(STATO_TUTTI,"Tutti");
			}
				
public Stampa_fat_pas_per_vpVBulk() {
		super();
	}		
			
	public final java.util.Dictionary getti_statoKeys() {
				return ti_statoKeys;
		}
		
	public String getstato() {
				return stato;
		}
	
	public void setstato(String string) {
				stato = string;
		}
}
