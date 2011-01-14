package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_cori_detBulk extends Liquid_gruppo_cori_detBase {


public Liquid_gruppo_cori_detBulk() {
	super();
}
public Liquid_gruppo_cori_detBulk(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_gruppo_cr,cd_regione,cd_unita_organizzativa,esercizio,pg_compenso,pg_comune,pg_liquidazione,ti_ente_percipiente);
}
}
