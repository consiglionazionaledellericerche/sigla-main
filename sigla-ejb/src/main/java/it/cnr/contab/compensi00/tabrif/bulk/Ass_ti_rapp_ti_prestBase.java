package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_prestBase extends Ass_ti_rapp_ti_prestKey implements Keyed {

public Ass_ti_rapp_ti_prestBase() {
	super();
}
public Ass_ti_rapp_ti_prestBase(java.lang.String cd_tipo_rapporto,java.lang.String cd_ti_prestazione) {
	super(cd_tipo_rapporto,cd_ti_prestazione);
}
}
