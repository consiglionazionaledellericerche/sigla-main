package it.cnr.contab.logregistry00.logs.bulk;

import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class L_bancaBulk extends L_bancaBase {

public L_bancaBulk() {
	super();
}
public L_bancaBulk(java.lang.Integer cd_terzo,java.lang.Long pg_banca,java.math.BigDecimal pg_storico_) {
	super(cd_terzo,pg_banca,pg_storico_);
}
}
