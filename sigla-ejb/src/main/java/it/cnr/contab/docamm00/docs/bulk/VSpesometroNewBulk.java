package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.util.OrderedHashtable;

public class VSpesometroNewBulk extends VSpesometroNewBase {
	private static OrderedHashtable tipoKeys;
	final public static String ATTIVA 	= "ATTIVA";
	final public static String PASSIVA	= "PASSIVA";
	String nome_file;
	public String getNome_file() {
		return nome_file;
	}

	public void setNome_file(String nome_file) {
		this.nome_file = nome_file;
	}

	public OrderedHashtable getTipoKeys() {
		if (tipoKeys == null)
		{
			tipoKeys = new OrderedHashtable();
			tipoKeys.put("ATTIVA", "ATTIVE");	
			tipoKeys.put("PASSIVA", "PASSIVE");	
		}
		return tipoKeys;
	}
	
	/**
	 * @param hashtable
	 */
	public static void setTipoKeys(OrderedHashtable hashtable) {
		tipoKeys = hashtable;
	}
	public VSpesometroNewBulk() {
		super();
	}
	public VSpesometroNewBulk(java.lang.Long pg) {
		super(pg);
	}
	
}