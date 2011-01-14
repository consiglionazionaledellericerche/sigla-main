package it.cnr.contab.docamm00.tabrif.bulk;

import java.lang.String;
import java.util.Dictionary;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

public class Tipo_movimento_inventarioBulk extends Tipo_movimento_inventarioBase {

	public final static String CARICO      = "C";
	public final static String SCARICO      = "S";
	
	public final static Dictionary CARICO_SCARICO;

	static {
		
		CARICO_SCARICO = new it.cnr.jada.util.OrderedHashtable();
		CARICO_SCARICO.put(CARICO,"Carico");
		CARICO_SCARICO.put(SCARICO,"Scarico");
	};

public Tipo_movimento_inventarioBulk() {
	super();
}
public Tipo_movimento_inventarioBulk(java.lang.String cd_tipo_movimento_inventario) {
	super(cd_tipo_movimento_inventario);
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi italiano/estero.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_carico_scaricoKeys() {
		return CARICO_SCARICO;
	}
public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setFlag_carico_scarico(CARICO);
	return this;
}
}
