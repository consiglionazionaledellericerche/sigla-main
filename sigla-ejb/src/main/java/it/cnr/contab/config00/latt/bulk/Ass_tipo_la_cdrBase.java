package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_tipo_la_cdrBase extends Ass_tipo_la_cdrKey implements Keyed {

public Ass_tipo_la_cdrBase() {
	super();
}
public Ass_tipo_la_cdrBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_tipo_linea_attivita) {
	super(cd_centro_responsabilita,cd_tipo_linea_attivita);
}
}
