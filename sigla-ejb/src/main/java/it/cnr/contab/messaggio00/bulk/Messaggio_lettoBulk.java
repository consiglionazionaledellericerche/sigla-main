package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_lettoBulk extends Messaggio_lettoBase {

public Messaggio_lettoBulk() {
	super();
}
public Messaggio_lettoBulk(java.lang.String cd_utente,java.lang.Long pg_messaggio) {
	super(cd_utente,pg_messaggio);
}
}
