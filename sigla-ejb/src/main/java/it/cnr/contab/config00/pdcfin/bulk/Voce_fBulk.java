package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_fBulk extends Voce_fBase {

public Voce_fBulk() {
	super();
}
public Voce_fBulk(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
}
}
