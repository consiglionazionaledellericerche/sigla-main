package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_accessoBase extends Utente_unita_accessoKey implements Keyed {

public Utente_unita_accessoBase() {
	super();
}
public Utente_unita_accessoBase(java.lang.String cd_accesso,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super(cd_accesso,cd_unita_organizzativa,cd_utente);
}
}
