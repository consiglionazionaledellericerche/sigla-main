package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_trattBase extends Ass_ti_rapp_ti_trattKey implements Keyed {

public Ass_ti_rapp_ti_trattBase() {
	super();
}
public Ass_ti_rapp_ti_trattBase(java.lang.String cd_tipo_rapporto,java.lang.String cd_trattamento) {
	super(cd_tipo_rapporto,cd_trattamento);
}
}
