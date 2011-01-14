package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_fondo_eco_mandatoBase extends Ass_fondo_eco_mandatoKey implements Keyed {


public Ass_fondo_eco_mandatoBase() {
	super();
}
public Ass_fondo_eco_mandatoBase(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio);
}
}
