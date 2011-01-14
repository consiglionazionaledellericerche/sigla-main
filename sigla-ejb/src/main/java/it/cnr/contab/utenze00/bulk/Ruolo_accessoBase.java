package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ruolo_accessoBase extends Ruolo_accessoKey implements Keyed {

public Ruolo_accessoBase() {
	super();
}
public Ruolo_accessoBase(java.lang.String cd_accesso,java.lang.String cd_ruolo) {
	super(cd_accesso,cd_ruolo);
}
}
