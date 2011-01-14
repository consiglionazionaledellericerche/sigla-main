package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CapBase extends CapKey implements Keyed {

public CapBase() {
	super();
}
public CapBase(java.lang.String cd_cap,java.lang.Long pg_comune) {
	super(cd_cap,pg_comune);
}
}
