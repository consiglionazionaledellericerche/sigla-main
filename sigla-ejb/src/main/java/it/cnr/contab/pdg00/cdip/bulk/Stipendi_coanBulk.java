package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_coanBulk extends Stipendi_coanBase {

public Stipendi_coanBulk() {
	super();
}
public Stipendi_coanBulk(java.lang.String cd_cds,java.lang.String cd_uo,java.lang.Integer esercizio,java.lang.Integer mese) {
	super(cd_cds,cd_uo,esercizio,mese);
}
}
