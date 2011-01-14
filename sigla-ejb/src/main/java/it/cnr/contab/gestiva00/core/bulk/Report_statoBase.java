package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Report_statoBase extends Report_statoKey implements Keyed {

public Report_statoBase() {
	super();
}

public Report_statoBase(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_documento,java.lang.String tipo_report) {
	super(cd_cds,cd_tipo_sezionale,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,stato,ti_documento,tipo_report);
}
}
