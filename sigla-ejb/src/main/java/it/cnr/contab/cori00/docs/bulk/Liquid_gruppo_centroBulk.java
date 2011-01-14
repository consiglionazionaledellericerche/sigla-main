package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.persistency.Persistent;

public class Liquid_gruppo_centroBulk extends Liquid_gruppo_centroBase implements Persistent {
	
	public final static String STATO_INIZIALE = "I";
	public final static String STATO_SOSPESO = "S";
	public final static String STATO_RIBALTATO = "R";
	public final static String STATO_CHIUSO = "C";
	public final static String STATO_ANNULLATO = "A";

	public Liquid_gruppo_centroBulk() {
		super();
	}
}