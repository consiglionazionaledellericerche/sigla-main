package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_gruppo_voceBase extends Categoria_gruppo_voceKey implements Keyed {

public Categoria_gruppo_voceBase() {
	super();
}

public Categoria_gruppo_voceBase(java.lang.String cd_categoria_gruppo,java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_categoria_gruppo,cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
}
