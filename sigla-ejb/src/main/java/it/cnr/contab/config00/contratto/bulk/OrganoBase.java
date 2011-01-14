/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class OrganoBase extends OrganoKey implements Keyed {
//    DS_TIPO_ORGANO VARCHAR(200) NOT NULL
	private java.lang.String ds_organo;

//	DT_INIZIO_VALIDITA TIMESTAMP(7)
    private java.sql.Timestamp dt_inizio_validita;
 
//	DT_FINE_VALIDITA TIMESTAMP(7)
    private java.sql.Timestamp dt_fine_validita;
   
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

//	FL_NON_DEFINITO CHAR(1) NOT NULL
    private java.lang.Boolean fl_non_definito;
 
	public OrganoBase() {
		super();
	}
	public OrganoBase(java.lang.String cd_tipo_organo) {
		super(cd_tipo_organo);
	}
	public java.lang.String getDs_organo () {
		return ds_organo;
	}
	public void setDs_organo(java.lang.String ds_tipo_organo)  {
		this.ds_organo=ds_tipo_organo;
	}
	public java.lang.Boolean getFl_cancellato () {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
/**
 * @return
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}

/**
 * @return
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}

/**
 * @return
 */
public java.lang.Boolean getFl_non_definito() {
	return fl_non_definito;
}

/**
 * @param timestamp
 */
public void setDt_fine_validita(java.sql.Timestamp timestamp) {
	dt_fine_validita = timestamp;
}

/**
 * @param timestamp
 */
public void setDt_inizio_validita(java.sql.Timestamp timestamp) {
	dt_inizio_validita = timestamp;
}

/**
 * @param boolean1
 */
public void setFl_non_definito(java.lang.Boolean boolean1) {
	fl_non_definito = boolean1;
}

}