package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Liquidazione_iva_ripart_finBase extends Liquidazione_iva_ripart_finKey implements Keyed {
	private static final long serialVersionUID = 1L;

	//  ESERCIZIO_VARIAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_variazione;

	//  CD_CDR VARCHAR(30)
	private java.lang.String cd_cdr;

	//  CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;

	//  IM_VARIAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_variazione;

	public Liquidazione_iva_ripart_finBase() {
		super();
	}

	public Liquidazione_iva_ripart_finBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine,Long pg_dettaglio) {
		super(cd_cds,esercizio,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine,pg_dettaglio);
	}
	
	public java.lang.Integer getEsercizio_variazione() {
		return esercizio_variazione;
	}

	public java.lang.String getCd_cdr() {
		return cd_cdr;
	}
	
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	
	public java.math.BigDecimal getIm_variazione() {
		return im_variazione;
	}
	
	public void setEsercizio_variazione(java.lang.Integer esercizio_variazione) {
		this.esercizio_variazione = esercizio_variazione;
	}
	
	public void setCd_cdr(java.lang.String cd_cdr) {
		this.cd_cdr = cd_cdr;
	}
	
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}
	
	public void setIm_variazione(java.math.BigDecimal im_variazione) {
		this.im_variazione = im_variazione;
	}
}
