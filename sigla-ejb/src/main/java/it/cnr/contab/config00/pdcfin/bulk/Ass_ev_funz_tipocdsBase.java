package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_funz_tipocdsBase extends Ass_ev_funz_tipocdsKey implements Keyed {

public Ass_ev_funz_tipocdsBase() {
	super();
}
public Ass_ev_funz_tipocdsBase(java.lang.String cd_conto,java.lang.String cd_funzione,java.lang.String cd_tipo_unita,java.lang.Integer esercizio) {
	super(cd_conto,cd_funzione,cd_tipo_unita,esercizio);
}
}
