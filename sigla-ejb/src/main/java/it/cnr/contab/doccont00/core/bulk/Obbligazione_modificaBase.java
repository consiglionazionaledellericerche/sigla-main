/*
* Created by Generator 1.0
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Obbligazione_modificaBase extends Obbligazione_modificaKey implements Keyed {
//    DS_MODIFICA VARCHAR(300)
	private java.lang.String ds_modifica;
 
//    DT_MODIFICA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_modifica;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_originale;

//    PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;
 
	// MOTIVAZIONE VARCHAR(300) NULL
	private java.lang.String motivazione;

	public Obbligazione_modificaBase() {
		super();
	}
	public Obbligazione_modificaBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica) {
		super(cd_cds, esercizio, pg_modifica);
	}
	public java.lang.String getDs_modifica () {
		return ds_modifica;
	}
	public void setDs_modifica(java.lang.String ds_modifica)  {
		this.ds_modifica=ds_modifica;
	}
	public java.sql.Timestamp getDt_modifica () {
		return dt_modifica;
	}
	public void setDt_modifica(java.sql.Timestamp dt_modifica)  {
		this.dt_modifica=dt_modifica;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
		this.esercizio_originale = esercizio_originale;
	}
	public java.lang.Long getPg_obbligazione () {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.String getMotivazione() {
		return motivazione;
	}
	public void setMotivazione(java.lang.String motivazione) {
		this.motivazione = motivazione;
	}
}