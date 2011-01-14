package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_ruoloBase extends Utente_unita_ruoloKey implements Keyed {

public Utente_unita_ruoloBase() {
	super();
}
public Utente_unita_ruoloBase(java.lang.String cd_ruolo,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super(cd_ruolo,cd_unita_organizzativa,cd_utente);
}
}
