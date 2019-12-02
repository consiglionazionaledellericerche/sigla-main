/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class ObbligazioneBase extends ObbligazioneKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_CDS_ORI_RIPORTO VARCHAR(30)
	private java.lang.String cd_cds_ori_riporto;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// CD_FONDO_RICERCA VARCHAR(20)
	private java.lang.String cd_fondo_ricerca;

	// CD_RIFERIMENTO_CONTRATTO VARCHAR(30)
	private java.lang.String cd_riferimento_contratto;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_TIPO_OBBLIGAZIONE VARCHAR(10)
	private java.lang.String cd_tipo_obbligazione;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DS_OBBLIGAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_obbligazione;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// DT_SCADENZA_CONTRATTO TIMESTAMP
	private java.sql.Timestamp dt_scadenza_contratto;

	// ESERCIZIO_COMPETENZA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_competenza;

	// ESERCIZIO_ORI_RIPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_riporto;

	// FL_CALCOLO_AUTOMATICO CHAR(1) NOT NULL
	private java.lang.Boolean fl_calcolo_automatico;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// FL_SPESE_COSTI_ALTRUI CHAR(1) NOT NULL
	private java.lang.Boolean fl_spese_costi_altrui;

	// IM_COSTI_ANTICIPATI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_costi_anticipati;

	// IM_OBBLIGAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbligazione;

	// NOTE_OBBLIGAZIONE VARCHAR(300)
	private java.lang.String note_obbligazione;

	// ESERCIZIO_ORI_RIPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_ori_riporto;

	// PG_OBBLIGAZIONE_ORI_RIPORTO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_ori_riporto;

	// RIPORTATO CHAR(1) NOT NULL
	private java.lang.String riportato;

	// STATO_OBBLIGAZIONE CHAR(1) NOT NULL
	private java.lang.String stato_obbligazione;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
	
	// ESERCIZIO_CONTRATTO DECIMAL(4,0) NULL
	private java.lang.Integer esercizio_contratto;

	// STATO_CONTRATTO CHAR(1) NULL
	private java.lang.String stato_contratto;

	// PG_CONTRATTO DECIMAL(10,0) NULL
	private java.lang.Long pg_contratto;

	// MOTIVAZIONE VARCHAR(300) NULL
	private java.lang.String motivazione;

	// ESERCIZIO_REP DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_rep;

	// PG_REPERTORIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_repertorio;

	// FL_NETTO_SOSPESO CHAR(1) NOT NULL
	private java.lang.Boolean fl_netto_sospeso;

	// FL_GARA_IN_CORSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gara_in_corso;
	
	// DS_GARA_IN_CORSO VARCHAR(300) NOT NULL
	private java.lang.String ds_gara_in_corso;

	// ESERCIZIO_EV_NEXT DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_ev_next;

	// TI_APPARTENENZA_EV_NEXT CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza_ev_next;

	// TI_GESTIONE_EV_NEXT CHAR(1) NOT NULL
	private java.lang.String ti_gestione_ev_next;

	// CD_ELEMENTO_VOCE_NEXT VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce_next;

public ObbligazioneBase() {
	super();
}
public ObbligazioneBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione) {
	super(cd_cds,esercizio,esercizio_originale,pg_obbligazione);
}
/* 
 * Getter dell'attributo cd_cds_ori_riporto
 */
public java.lang.String getCd_cds_ori_riporto() {
	return cd_cds_ori_riporto;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_fondo_ricerca
 */
public java.lang.String getCd_fondo_ricerca() {
	return cd_fondo_ricerca;
}
/* 
 * Getter dell'attributo cd_riferimento_contratto
 */
public java.lang.String getCd_riferimento_contratto() {
	return cd_riferimento_contratto;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo cd_tipo_obbligazione
 */
public java.lang.String getCd_tipo_obbligazione() {
	return cd_tipo_obbligazione;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo ds_obbligazione
 */
public java.lang.String getDs_obbligazione() {
	return ds_obbligazione;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo dt_scadenza_contratto
 */
public java.sql.Timestamp getDt_scadenza_contratto() {
	return dt_scadenza_contratto;
}
/* 
 * Getter dell'attributo esercizio_competenza
 */
public java.lang.Integer getEsercizio_competenza() {
	return esercizio_competenza;
}
/* 
 * Getter dell'attributo esercizio_ori_riporto
 */
public java.lang.Integer getEsercizio_ori_riporto() {
	return esercizio_ori_riporto;
}
/* 
 * Getter dell'attributo fl_calcolo_automatico
 */
public java.lang.Boolean getFl_calcolo_automatico() {
	return fl_calcolo_automatico;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo fl_spese_costi_altrui
 */
public java.lang.Boolean getFl_spese_costi_altrui() {
	return fl_spese_costi_altrui;
}
/* 
 * Getter dell'attributo im_costi_anticipati
 */
public java.math.BigDecimal getIm_costi_anticipati() {
	return im_costi_anticipati;
}
/* 
 * Getter dell'attributo im_obbligazione
 */
public java.math.BigDecimal getIm_obbligazione() {
	return im_obbligazione;
}
/* 
 * Getter dell'attributo note_obbligazione
 */
public java.lang.String getNote_obbligazione() {
	return note_obbligazione;
}
/* 
 * Getter dell'attributo esercizio_ori_ori_riporto
 */
public java.lang.Integer getEsercizio_ori_ori_riporto() {
	return esercizio_ori_ori_riporto;
}
/* 
 * Getter dell'attributo pg_obbligazione_ori_riporto
 */
public java.lang.Long getPg_obbligazione_ori_riporto() {
	return pg_obbligazione_ori_riporto;
}
/* 
 * Getter dell'attributo riportato
 */
public java.lang.String getRiportato() {
	return riportato;
}
/* 
 * Getter dell'attributo stato_obbligazione
 */
public java.lang.String getStato_obbligazione() {
	return stato_obbligazione;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Setter dell'attributo cd_cds_ori_riporto
 */
public void setCd_cds_ori_riporto(java.lang.String cd_cds_ori_riporto) {
	this.cd_cds_ori_riporto = cd_cds_ori_riporto;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_fondo_ricerca
 */
public void setCd_fondo_ricerca(java.lang.String cd_fondo_ricerca) {
	this.cd_fondo_ricerca = cd_fondo_ricerca;
}
/* 
 * Setter dell'attributo cd_riferimento_contratto
 */
public void setCd_riferimento_contratto(java.lang.String cd_riferimento_contratto) {
	this.cd_riferimento_contratto = cd_riferimento_contratto;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo cd_tipo_obbligazione
 */
public void setCd_tipo_obbligazione(java.lang.String cd_tipo_obbligazione) {
	this.cd_tipo_obbligazione = cd_tipo_obbligazione;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo ds_obbligazione
 */
public void setDs_obbligazione(java.lang.String ds_obbligazione) {
	this.ds_obbligazione = ds_obbligazione;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo dt_scadenza_contratto
 */
public void setDt_scadenza_contratto(java.sql.Timestamp dt_scadenza_contratto) {
	this.dt_scadenza_contratto = dt_scadenza_contratto;
}
/* 
 * Setter dell'attributo esercizio_competenza
 */
public void setEsercizio_competenza(java.lang.Integer esercizio_competenza) {
	this.esercizio_competenza = esercizio_competenza;
}
/* 
 * Setter dell'attributo esercizio_ori_riporto
 */
public void setEsercizio_ori_riporto(java.lang.Integer esercizio_ori_riporto) {
	this.esercizio_ori_riporto = esercizio_ori_riporto;
}
/* 
 * Setter dell'attributo fl_calcolo_automatico
 */
public void setFl_calcolo_automatico(java.lang.Boolean fl_calcolo_automatico) {
	this.fl_calcolo_automatico = fl_calcolo_automatico;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo fl_spese_costi_altrui
 */
public void setFl_spese_costi_altrui(java.lang.Boolean fl_spese_costi_altrui) {
	this.fl_spese_costi_altrui = fl_spese_costi_altrui;
}
/* 
 * Setter dell'attributo im_costi_anticipati
 */
public void setIm_costi_anticipati(java.math.BigDecimal im_costi_anticipati) {
	this.im_costi_anticipati = im_costi_anticipati;
}
/* 
 * Setter dell'attributo im_obbligazione
 */
public void setIm_obbligazione(java.math.BigDecimal im_obbligazione) {
	this.im_obbligazione = im_obbligazione;
}
/* 
 * Setter dell'attributo note_obbligazione
 */
public void setNote_obbligazione(java.lang.String note_obbligazione) {
	this.note_obbligazione = note_obbligazione;
}
/* 
 * Setter dell'attributo esercizio_ori_ori_riporto
 */
public void setEsercizio_ori_ori_riporto(java.lang.Integer esercizio_ori_ori_riporto) {
	this.esercizio_ori_ori_riporto = esercizio_ori_ori_riporto;
}
/* 
 * Setter dell'attributo pg_obbligazione_ori_riporto
 */
public void setPg_obbligazione_ori_riporto(java.lang.Long pg_obbligazione_ori_riporto) {
	this.pg_obbligazione_ori_riporto = pg_obbligazione_ori_riporto;
}
/* 
 * Setter dell'attributo riportato
 */
public void setRiportato(java.lang.String riportato) {
	this.riportato = riportato;
}
/* 
 * Setter dell'attributo stato_obbligazione
 */
public void setStato_obbligazione(java.lang.String stato_obbligazione) {
	this.stato_obbligazione = stato_obbligazione;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}

	/**
	 * @return
	 */
	public java.lang.Long getPg_contratto() {
		return pg_contratto;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio_contratto(java.lang.Integer integer) {
		esercizio_contratto = integer;
	}

	/**
	 * @param long1
	 */
	public void setPg_contratto(java.lang.Long long1) {
		pg_contratto = long1;
	}

	/**
	 * @return
	 */
	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}

	/**
	 * @param string
	 */
	public void setStato_contratto(java.lang.String string) {
		stato_contratto = string;
	}
	public java.lang.String getMotivazione() {
		return motivazione;
	}
	public void setMotivazione(java.lang.String motivazione) {
		this.motivazione = motivazione;
	}
	public java.lang.Integer getEsercizio_rep() {
		return esercizio_rep;
	}
	public void setEsercizio_rep(java.lang.Integer esercizio_rep) {
		this.esercizio_rep = esercizio_rep;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio) {
		this.pg_repertorio = pg_repertorio;
	}
	public java.lang.Boolean getFl_netto_sospeso() {
		return fl_netto_sospeso;
	}
	public void setFl_netto_sospeso(java.lang.Boolean fl_netto_sospeso) {
		this.fl_netto_sospeso = fl_netto_sospeso;
	}
	public java.lang.Boolean getFl_gara_in_corso() {
		return fl_gara_in_corso;
	}
	public void setFl_gara_in_corso(java.lang.Boolean flGaraInCorso) {
		fl_gara_in_corso = flGaraInCorso;
	}
	public java.lang.String getDs_gara_in_corso() {
		return ds_gara_in_corso;
	}
	public void setDs_gara_in_corso(java.lang.String dsGaraInCorso) {
		ds_gara_in_corso = dsGaraInCorso;
	}
	public java.lang.Integer getEsercizio_ev_next() {
		return esercizio_ev_next;
	}
	public void setEsercizio_ev_next(java.lang.Integer esercizio_ev_next) {
		this.esercizio_ev_next = esercizio_ev_next;
	}
	public java.lang.String getTi_appartenenza_ev_next() {
		return ti_appartenenza_ev_next;
	}
	public void setTi_appartenenza_ev_next(java.lang.String ti_appartenenza_ev_next) {
		this.ti_appartenenza_ev_next = ti_appartenenza_ev_next;
	}
	public java.lang.String getTi_gestione_ev_next() {
		return ti_gestione_ev_next;
	}
	public void setTi_gestione_ev_next(java.lang.String ti_gestione_ev_next) {
		this.ti_gestione_ev_next = ti_gestione_ev_next;
	}
	public java.lang.String getCd_elemento_voce_next() {
		return cd_elemento_voce_next;
	}
	public void setCd_elemento_voce_next(java.lang.String cd_elemento_voce_next) {
		this.cd_elemento_voce_next = cd_elemento_voce_next;
	}
}
