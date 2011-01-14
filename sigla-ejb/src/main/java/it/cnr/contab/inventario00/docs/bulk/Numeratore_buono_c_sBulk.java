package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numeratore_buono_c_sBulk extends Numeratore_buono_c_sBase {

public Numeratore_buono_c_sBulk() {
	super();
}
public Numeratore_buono_c_sBulk(java.lang.Integer esercizio,java.lang.Long pg_inventario,java.lang.String ti_carico_scarico) {
	super(esercizio,pg_inventario,ti_carico_scarico);
}
}
