package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00_logsBase extends Ext_cassiere00_logsKey implements Keyed {

public Ext_cassiere00_logsBase() {
	super();
}
public Ext_cassiere00_logsBase(java.lang.Integer esercizio,java.lang.String nome_file,java.math.BigDecimal pg_esecuzione) {
	super(esercizio,nome_file,pg_esecuzione);
}
}
