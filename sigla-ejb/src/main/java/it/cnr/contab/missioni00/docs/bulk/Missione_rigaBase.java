package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.persistency.Keyed;

public class Missione_rigaBase extends Missione_rigaKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// IM_TOTALE_RIGA_MISSIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_riga_missione;
	
	public Missione_rigaBase() {
		super();
	}

	public Missione_rigaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_missione,java.lang.Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_missione,progressivo_riga);
	}

	public java.lang.String getCd_cds_obbligazione() {
		return cd_cds_obbligazione;
	}

	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
		this.cd_cds_obbligazione = cd_cds_obbligazione;
	}

	public java.lang.Integer getEsercizio_obbligazione() {
		return esercizio_obbligazione;
	}

	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
		this.esercizio_obbligazione = esercizio_obbligazione;
	}

	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return esercizio_ori_obbligazione;
	}

	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
		this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
	}

	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}

	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.pg_obbligazione = pg_obbligazione;
	}

	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}

	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
		this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
	}

	public java.math.BigDecimal getIm_totale_riga_missione() {
		return im_totale_riga_missione;
	}

	public void setIm_totale_riga_missione(java.math.BigDecimal im_totale_riga_missione) {
		this.im_totale_riga_missione = im_totale_riga_missione;
	}
}
