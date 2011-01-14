/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
public class Stipendi_cofi_cori_dettBulk extends Stipendi_cofi_cori_dettBase {
	public Stipendi_cofi_cori_dettBulk() {
		super();
	}
	private static java.util.Dictionary tipo_flussokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PRINCIPALE = "S";
	public static final String ANNULLI = "A";
	public static final String RIMBORSI = "R";
	public static final String IRAP = "I";
	public static final String FIGURATIVI = "F";
	public static final String ADDIZIONALI = "C";

	static {
		tipo_flussokeys.put(new String(PRINCIPALE),"Principale");
		tipo_flussokeys.put(new String(ANNULLI),"Annulli");
		tipo_flussokeys.put(new String(RIMBORSI),"Rimborsi");
		tipo_flussokeys.put(new String(IRAP),"Regionalizzazione IRAP");
		tipo_flussokeys.put(new String(FIGURATIVI),"Contributi Figurativi");
		tipo_flussokeys.put(new String(ADDIZIONALI),"Addizionali Comunali");
	}
	public java.util.Dictionary getTipo_flussoKeys() {
		return tipo_flussokeys;
	}
	
	private static java.util.Dictionary tipokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PERCIPIENTE = "P";
	public static final String ENTE = "E";
	
	static {
		tipo_flussokeys.put(new String(PERCIPIENTE),"Percipiente");
		tipo_flussokeys.put(new String(ENTE),"Ente");
	}
	public java.util.Dictionary getTipoKeys() {
		return tipokeys;
	}
}