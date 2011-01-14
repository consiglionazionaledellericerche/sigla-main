package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class InquadramentoBase extends InquadramentoKey implements Keyed {
	// DT_FIN_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_validita;

public InquadramentoBase() {
	super();
}
public InquadramentoBase(java.lang.Integer cd_anag,java.lang.String cd_tipo_rapporto,java.sql.Timestamp dt_ini_validita,java.sql.Timestamp dt_ini_validita_rapporto,java.lang.Long pg_rif_inquadramento) {
	super(cd_anag,cd_tipo_rapporto,dt_ini_validita,dt_ini_validita_rapporto,pg_rif_inquadramento);
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
}
