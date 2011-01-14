package it.cnr.contab.missioni00.tabrif.bulk;

/**
 * Insert the type's description here.
 * Creation date: (12/02/2002 11.50.10)
 * @author: Roberto Fantino
 */
public abstract class TipoAreaGeografica {

	public final static java.util.Dictionary TIPI_AREA_GEOGRAFICA = new it.cnr.jada.util.OrderedHashtable();
	public final static String ITALIA = "I";
	public final static String ESTERO = "E";
	public final static String INDIFFERENTE  = "*";

	static {
		TIPI_AREA_GEOGRAFICA.put(ITALIA,"Italia");
		TIPI_AREA_GEOGRAFICA.put(ESTERO,"Estero");
		TIPI_AREA_GEOGRAFICA.put(INDIFFERENTE,"Indifferente");
	}

}
