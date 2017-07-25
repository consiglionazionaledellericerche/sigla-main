package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Liquidazione_iva_variazioniBase extends Liquidazione_iva_variazioniKey implements Keyed {
	private static final long serialVersionUID = 1L;

	private java.lang.Integer esercizio_variazione_comp;

	private java.lang.Long pg_variazione_comp;

	private java.lang.Integer esercizio_variazione_res;

	private java.lang.Long pg_variazione_res;

	public Liquidazione_iva_variazioniBase() {
		super();
	}

	public Liquidazione_iva_variazioniBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine,Long pg_dettaglio) {
		super(cd_cds,esercizio,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine,pg_dettaglio);
	}
	
	public java.lang.Integer getEsercizio_variazione_comp() {
		return esercizio_variazione_comp;
	}
	
	public java.lang.Long getPg_variazione_comp() {
		return pg_variazione_comp;
	}
	
	public java.lang.Integer getEsercizio_variazione_res() {
		return esercizio_variazione_res;
	}
	
	public java.lang.Long getPg_variazione_res() {
		return pg_variazione_res;
	}
	
	public void setEsercizio_variazione_comp(java.lang.Integer esercizio_variazione_comp) {
		this.esercizio_variazione_comp = esercizio_variazione_comp;
	}
	
	public void setPg_variazione_comp(java.lang.Long pg_variazione_comp) {
		this.pg_variazione_comp = pg_variazione_comp;
	}
	
	public void setEsercizio_variazione_res(java.lang.Integer esercizio_variazione_res) {
		this.esercizio_variazione_res = esercizio_variazione_res;
	}
	
	public void setPg_variazione_res(java.lang.Long pg_variazione_res) {
		this.pg_variazione_res = pg_variazione_res;
	}
}
