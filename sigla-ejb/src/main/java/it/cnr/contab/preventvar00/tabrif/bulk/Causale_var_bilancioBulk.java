package it.cnr.contab.preventvar00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Causale_var_bilancioBulk extends Causale_var_bilancioBase {

	// TI_CAUSALE
	public final static java.lang.String UTENTE = "U";
	public final static java.lang.String SISTEMA = "S";

	public final static java.util.Dictionary TI_CAUSALE;

	static{
		TI_CAUSALE = new it.cnr.jada.util.OrderedHashtable();
		TI_CAUSALE.put(SISTEMA,"Sistema");
		TI_CAUSALE.put(UTENTE,"Utente");
	}



public Causale_var_bilancioBulk() {
	super();
}
public Causale_var_bilancioBulk(java.lang.String cd_causale_var_bilancio) {
	super(cd_causale_var_bilancio);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_causaleKeys'
 *
 * @return Il valore della proprietà 'ti_causaleKeys'
 */
public java.util.Dictionary getTi_causaleKeys() {

	return TI_CAUSALE;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);
	setTi_causale(UTENTE);

	return this;
}
}
