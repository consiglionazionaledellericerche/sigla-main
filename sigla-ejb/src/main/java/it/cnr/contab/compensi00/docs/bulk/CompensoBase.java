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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.*;

import java.math.BigDecimal;

public class CompensoBase extends CompensoKey implements Keyed {
	// ALIQUOTA_FISCALE DECIMAL(6,3)
	private java.math.BigDecimal aliquota_fiscale;

	// ALIQUOTA_IRPEF_DA_MISSIONE DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota_irpef_da_missione;

	// ALIQUOTA_IRPEF_TASSEP DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota_irpef_tassep;

	// CD_CDR_GENRC VARCHAR(30)
	private java.lang.String cd_cdr_genrc;

	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_DOC_GENRC VARCHAR(30)
	private java.lang.String cd_cds_doc_genrc;

	// CD_CDS_MISSIONE VARCHAR(30)
	private java.lang.String cd_cds_missione;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_LINEA_ATTIVITA_GENRC VARCHAR(10)
	private java.lang.String cd_linea_attivita_genrc;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_MODALITA_PAG_UO_CDS VARCHAR(10)
	private java.lang.String cd_modalita_pag_uo_cds;

	// CD_PROVINCIA_ADD VARCHAR(10)
	private java.lang.String cd_provincia_add;

	// CD_REGIONE_ADD VARCHAR(10)
	private java.lang.String cd_regione_add;

	// CD_REGIONE_IRAP VARCHAR(10)
	private java.lang.String cd_regione_irap;

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERMINI_PAG_UO_CDS VARCHAR(10)
	private java.lang.String cd_termini_pag_uo_cds;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_UO_CDS DECIMAL(8,0)
	private java.lang.Integer cd_terzo_uo_cds;

	// CD_TIPOLOGIA_RISCHIO VARCHAR(10)
	private java.lang.String cd_tipologia_rischio;

	// CD_TIPO_DOC_GENRC VARCHAR(10)
	private java.lang.String cd_tipo_doc_genrc;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;

	// CD_UO_DOC_GENRC VARCHAR(30)
	private java.lang.String cd_uo_doc_genrc;

	// CD_UO_MISSIONE VARCHAR(30)
	private java.lang.String cd_uo_missione;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// CD_VOCE_IVA VARCHAR(10)
	private java.lang.String cd_voce_iva;

	// CODICE_FISCALE VARCHAR(20)
	@StoragePolicy(name="P:emp:cf", property=@StorageProperty(name="emp:codice"))
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DETRAZIONE_ALTRI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_altri;

	private java.math.BigDecimal detrazioneRiduzioneCuneo;

	private java.math.BigDecimal detrazioneRidCuneoNetto;

	// DETRAZIONE_ALTRI_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_altri_netto;

	// DETRAZIONE_CONIUGE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_coniuge;

	// DETRAZIONE_CONIUGE_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_coniuge_netto;

	// DETRAZIONE_FIGLI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_figli;

	// DETRAZIONE_FIGLI_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazione_figli_netto;

	// DETRAZIONI_LA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_la;

	// DETRAZIONI_LA_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_la_netto;

	// DETRAZIONI_PERSONALI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_personali;

	// DETRAZIONI_PERSONALI_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_personali_netto;

	// DS_COMPENSO VARCHAR(300) NOT NULL
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:description"))
	@StorageProperty(name="emppay:descDoc")
	private java.lang.String ds_compenso;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_EMISSIONE_MANDATO TIMESTAMP
	private java.sql.Timestamp dt_emissione_mandato;

	// DT_FATTURA_FORNITORE TIMESTAMP
	private java.sql.Timestamp dt_fattura_fornitore;

	// DT_PAGAMENTO_FONDO_ECO TIMESTAMP
	private java.sql.Timestamp dt_pagamento_fondo_eco;

	// DT_PAGAMENTO_MANDATO TIMESTAMP
	private java.sql.Timestamp dt_pagamento_mandato;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	@StorageProperty(name="emppay:datDoc",converterBeanName="cmis.converter.timestampToCalendarConverter")
	private java.sql.Timestamp dt_registrazione;

	// DT_TRASMISSIONE_MANDATO TIMESTAMP
	private java.sql.Timestamp dt_trasmissione_mandato;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_DOC_GENRC DECIMAL(4,0)
	private java.lang.Integer esercizio_doc_genrc;

	// ESERCIZIO_FATTURA_FORNITORE DECIMAL(4,0)
	private java.lang.Integer esercizio_fattura_fornitore;

	// ESERCIZIO_MISSIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_missione;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// FL_ACCANTONA_ADD_TERR CHAR(1) NOT NULL
	private java.lang.Boolean fl_accantona_add_terr;

	// FL_COMPENSO_CONGUAGLIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_compenso_conguaglio;

	// FL_COMPENSO_MCARRIERA_TASSEP CHAR(1) NOT NULL
	private java.lang.Boolean fl_compenso_mcarriera_tassep;

	// FL_COMPENSO_MINICARRIERA CHAR(1) NOT NULL
	private java.lang.Boolean fl_compenso_minicarriera;

	// FL_COMPENSO_STIPENDI CHAR(1) NOT NULL
	private java.lang.Boolean fl_compenso_stipendi;

	// FL_DIARIA CHAR(1) NOT NULL
	private java.lang.Boolean fl_diaria;

	// FL_ESCLUDI_QVARIA_DEDUZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_escludi_qvaria_deduzione;

	// FL_GENERATA_FATTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_generata_fattura;

	// FL_INTERA_QFISSA_DEDUZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intera_qfissa_deduzione;

	// FL_RECUPERO_RATE CHAR(1) NOT NULL
	private java.lang.Boolean fl_recupero_rate;

	// FL_SENZA_CALCOLI CHAR(1) NOT NULL
	private java.lang.Boolean fl_senza_calcoli;

	// IMPONIBILE_FISCALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_fiscale;

	// IMPONIBILE_FISCALE_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_fiscale_netto;

	// IMPONIBILE_INAIL DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_inail;

	// IMPONIBILE_IVA DECIMAL(15,2)
	private java.math.BigDecimal imponibile_iva;

	// IM_CR_ENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cr_ente;

	// IM_CR_PERCIPIENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cr_percipiente;

	// IM_DEDUZIONE_IRPEF DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_irpef;

	// IM_DETRAZIONE_PERSONALE_ANAG DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione_personale_anag;

	// IM_LORDO_PERCIPIENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_lordo_percipiente;

	// IM_NETTO_PERCIPIENTE DECIMAL(15,2) NOT NULL
	@StorageProperty(name="emppay:impNetto")
	private java.math.BigDecimal im_netto_percipiente;

	// IM_NO_FISCALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_no_fiscale;

	// IM_TOTALE_COMPENSO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_compenso;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NR_FATTURA_FORNITORE VARCHAR(20)
	private java.lang.String nr_fattura_fornitore;

	// NUMERO_GIORNI DECIMAL(8,0) NOT NULL
	private java.lang.Integer numero_giorni;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// PG_BANCA_UO_CDS DECIMAL(10,0)
	private java.lang.Long pg_banca_uo_cds;

	// PG_COMUNE_ADD DECIMAL(10,0)
	private java.lang.Long pg_comune_add;

	// PG_DOC_GENRC DECIMAL(10,0)
	private java.lang.Long pg_doc_genrc;

	// PG_MISSIONE DECIMAL(10,0)
	private java.lang.Long pg_missione;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// QUOTA_ESENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal quota_esente;

	// QUOTA_ESENTE_NO_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal quota_esente_no_iva;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// STATO_COAN CHAR(1) NOT NULL
	private java.lang.String stato_coan;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// STATO_PAGAMENTO_FONDO_ECO CHAR(1) NOT NULL
	private java.lang.String stato_pagamento_fondo_eco;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;

	// QUOTA_ESENTE_INPS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal quota_esente_inps;	

	// CD_RAPPORTO_INPS VARCHAR(5) NULL
	private java.lang.String cd_rapporto_inps;
	
	// CD_ATTIVITA_INPS VARCHAR(5) NULL
	private java.lang.String cd_attivita_inps;
	
	// CD_ALTRA_ASS_INPS VARCHAR(5) NULL
	private java.lang.String cd_altra_ass_inps;		
	
	// PG_COMUNE_INPS DECIMAL(10,0)
	private java.lang.Long pg_comune_inps;
	
	// IM_REDDITO_COMPLESSIVO(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_complessivo;

	// IM_REDDITO_ABITAZ_PRINC(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_abitaz_princ;

	// ESERCIZIO_REP DECIMAL(4,0)
	public java.lang.Integer esercizio_rep;

	// PG_REPERTORIO DECIMAL(10,0)
	public java.lang.Long pg_repertorio;	

	// ESERCIZIO_LIMITE_REP DECIMAL(4,0)
	public java.lang.Integer esercizio_limite_rep;

	// IM_NETTO_DA_TRATTENERE(15,2) NULL
	private java.math.BigDecimal im_netto_da_trattenere;

	// TI_PRESTAZIONE CHAR(5) NULL
	private java.lang.String ti_prestazione;		

	private java.lang.Integer esercizio_bonus;
	private java.lang.Long pg_bonus;
	// 
	private java.lang.Boolean fl_liquidazione_differita;
	
	// CD_TERZO_PIGNORATO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_pignorato;
	
	// ESERCIZIO_CONTRATTO DECIMAL(4,0)
	public java.lang.Integer esercizio_contratto;
	
	// STATO_CONTRATTO CHAR(1) NULL
		private java.lang.String stato_contratto;		
	
	// PG_CONTRATTO DECIMAL(10,0)
		public java.lang.Long pg_contratto;	
		
	// IM_TOT_REDDITO_COMPLESSIVO(15,2) NOT NULL
	private java.math.BigDecimal im_tot_reddito_complessivo;		
	
		// PG_TROVATO DECIMAL(10,0)
	private java.lang.Long pg_trovato;
	
	private java.sql.Timestamp dt_scadenza;
	
	private java.sql.Timestamp data_protocollo;
	
	private java.lang.String numero_protocollo;		
	
	private java.lang.String stato_liquidazione;
	
	private java.lang.String causale;
	
	private java.lang.Boolean fl_documento_ele;
	
	private java.lang.Boolean fl_split_payment;
	
    private String motivo_assenza_cig;

public CompensoBase() {
	super();
}
public CompensoBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_compenso);
}
/* 
 * Getter dell'attributo aliquota_fiscale
 */
public java.math.BigDecimal getAliquota_fiscale() {
	return aliquota_fiscale;
}
/* 
 * Getter dell'attributo aliquota_irpef_da_missione
 */
public java.math.BigDecimal getAliquota_irpef_da_missione() {
	return aliquota_irpef_da_missione;
}
/* 
 * Getter dell'attributo aliquota_irpef_tassep
 */
public java.math.BigDecimal getAliquota_irpef_tassep() {
	return aliquota_irpef_tassep;
}
/* 
 * Getter dell'attributo cd_cdr_genrc
 */
public java.lang.String getCd_cdr_genrc() {
	return cd_cdr_genrc;
}
/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
}
/* 
 * Getter dell'attributo cd_cds_doc_genrc
 */
public java.lang.String getCd_cds_doc_genrc() {
	return cd_cds_doc_genrc;
}
/* 
 * Getter dell'attributo cd_cds_missione
 */
public java.lang.String getCd_cds_missione() {
	return cd_cds_missione;
}
/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_linea_attivita_genrc
 */
public java.lang.String getCd_linea_attivita_genrc() {
	return cd_linea_attivita_genrc;
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_modalita_pag_uo_cds
 */
public java.lang.String getCd_modalita_pag_uo_cds() {
	return cd_modalita_pag_uo_cds;
}
/* 
 * Getter dell'attributo cd_provincia_add
 */
public java.lang.String getCd_provincia_add() {
	return cd_provincia_add;
}
/* 
 * Getter dell'attributo cd_regione_add
 */
public java.lang.String getCd_regione_add() {
	return cd_regione_add;
}
/* 
 * Getter dell'attributo cd_regione_irap
 */
public java.lang.String getCd_regione_irap() {
	return cd_regione_irap;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
/* 
 * Getter dell'attributo cd_termini_pag_uo_cds
 */
public java.lang.String getCd_termini_pag_uo_cds() {
	return cd_termini_pag_uo_cds;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_terzo_uo_cds
 */
public java.lang.Integer getCd_terzo_uo_cds() {
	return cd_terzo_uo_cds;
}
/* 
 * Getter dell'attributo cd_tipo_doc_genrc
 */
public java.lang.String getCd_tipo_doc_genrc() {
	return cd_tipo_doc_genrc;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo cd_tipologia_rischio
 */
public java.lang.String getCd_tipologia_rischio() {
	return cd_tipologia_rischio;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
/* 
 * Getter dell'attributo cd_uo_doc_genrc
 */
public java.lang.String getCd_uo_doc_genrc() {
	return cd_uo_doc_genrc;
}
/* 
 * Getter dell'attributo cd_uo_missione
 */
public java.lang.String getCd_uo_missione() {
	return cd_uo_missione;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}
/* 
 * Getter dell'attributo detrazione_altri
 */
public java.math.BigDecimal getDetrazione_altri() {
	return detrazione_altri;
}
/* 
 * Getter dell'attributo detrazione_altri_netto
 */
public java.math.BigDecimal getDetrazione_altri_netto() {
	return detrazione_altri_netto;
}
/* 
 * Getter dell'attributo detrazione_coniuge
 */
public java.math.BigDecimal getDetrazione_coniuge() {
	return detrazione_coniuge;
}
/* 
 * Getter dell'attributo detrazione_coniuge_netto
 */
public java.math.BigDecimal getDetrazione_coniuge_netto() {
	return detrazione_coniuge_netto;
}
/* 
 * Getter dell'attributo detrazione_figli
 */
public java.math.BigDecimal getDetrazione_figli() {
	return detrazione_figli;
}
/* 
 * Getter dell'attributo detrazione_figli_netto
 */
public java.math.BigDecimal getDetrazione_figli_netto() {
	return detrazione_figli_netto;
}
/* 
 * Getter dell'attributo detrazioni_la
 */
public java.math.BigDecimal getDetrazioni_la() {
	return detrazioni_la;
}
/* 
 * Getter dell'attributo detrazioni_la_netto
 */
public java.math.BigDecimal getDetrazioni_la_netto() {
	return detrazioni_la_netto;
}
/* 
 * Getter dell'attributo detrazioni_personali
 */
public java.math.BigDecimal getDetrazioni_personali() {
	return detrazioni_personali;
}
/* 
 * Getter dell'attributo detrazioni_personali_netto
 */
public java.math.BigDecimal getDetrazioni_personali_netto() {
	return detrazioni_personali_netto;
}
/* 
 * Getter dell'attributo ds_compenso
 */
public java.lang.String getDs_compenso() {
	return ds_compenso;
}
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}
/* 
 * Getter dell'attributo dt_emissione_mandato
 */
public java.sql.Timestamp getDt_emissione_mandato() {
	return dt_emissione_mandato;
}
/* 
 * Getter dell'attributo dt_fattura_fornitore
 */
public java.sql.Timestamp getDt_fattura_fornitore() {
	return dt_fattura_fornitore;
}
/* 
 * Getter dell'attributo dt_pagamento_fondo_eco
 */
public java.sql.Timestamp getDt_pagamento_fondo_eco() {
	return dt_pagamento_fondo_eco;
}
/* 
 * Getter dell'attributo dt_pagamento_mandato
 */
public java.sql.Timestamp getDt_pagamento_mandato() {
	return dt_pagamento_mandato;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo dt_trasmissione_mandato
 */
public java.sql.Timestamp getDt_trasmissione_mandato() {
	return dt_trasmissione_mandato;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}
/* 
 * Getter dell'attributo esercizio_doc_genrc
 */
public java.lang.Integer getEsercizio_doc_genrc() {
	return esercizio_doc_genrc;
}
/* 
 * Getter dell'attributo esercizio_fattura_fornitore
 */
public java.lang.Integer getEsercizio_fattura_fornitore() {
	return esercizio_fattura_fornitore;
}
/* 
 * Getter dell'attributo esercizio_missione
 */
public java.lang.Integer getEsercizio_missione() {
	return esercizio_missione;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo fl_accantona_add_terr
 */
public java.lang.Boolean getFl_accantona_add_terr() {
	return fl_accantona_add_terr;
}
/* 
 * Getter dell'attributo fl_compenso_conguaglio
 */
public java.lang.Boolean getFl_compenso_conguaglio() {
	return fl_compenso_conguaglio;
}
/* 
 * Getter dell'attributo fl_compenso_mcarriera_tassep
 */
public java.lang.Boolean getFl_compenso_mcarriera_tassep() {
	return fl_compenso_mcarriera_tassep;
}
/* 
 * Getter dell'attributo fl_compenso_minicarriera
 */
public java.lang.Boolean getFl_compenso_minicarriera() {
	return fl_compenso_minicarriera;
}
/* 
 * Getter dell'attributo fl_compenso_stipendi
 */
public java.lang.Boolean getFl_compenso_stipendi() {
	return fl_compenso_stipendi;
}
/* 
 * Getter dell'attributo fl_diaria
 */
public java.lang.Boolean getFl_diaria() {
	return fl_diaria;
}
/* 
 * Getter dell'attributo fl_escludi_qvaria_deduzione
 */
public java.lang.Boolean getFl_escludi_qvaria_deduzione() {
	return fl_escludi_qvaria_deduzione;
}
/* 
 * Getter dell'attributo fl_generata_fattura
 */
public java.lang.Boolean getFl_generata_fattura() {
	return fl_generata_fattura;
}
/* 
 * Getter dell'attributo fl_intera_qfissa_deduzione
 */
public java.lang.Boolean getFl_intera_qfissa_deduzione() {
	return fl_intera_qfissa_deduzione;
}
/* 
 * Getter dell'attributo fl_recupero_rate
 */
public java.lang.Boolean getFl_recupero_rate() {
	return fl_recupero_rate;
}
/* 
 * Getter dell'attributo fl_senza_calcoli
 */
public java.lang.Boolean getFl_senza_calcoli() {
	return fl_senza_calcoli;
}
/* 
 * Getter dell'attributo im_cr_ente
 */
public java.math.BigDecimal getIm_cr_ente() {
	return im_cr_ente;
}
/* 
 * Getter dell'attributo im_cr_percipiente
 */
public java.math.BigDecimal getIm_cr_percipiente() {
	return im_cr_percipiente;
}
/* 
 * Getter dell'attributo im_deduzione_irpef
 */
public java.math.BigDecimal getIm_deduzione_irpef() {
	return im_deduzione_irpef;
}
/* 
 * Getter dell'attributo im_detrazione_personale_anag
 */
public java.math.BigDecimal getIm_detrazione_personale_anag() {
	return im_detrazione_personale_anag;
}
/* 
 * Getter dell'attributo im_lordo_percipiente
 */
public java.math.BigDecimal getIm_lordo_percipiente() {
	return im_lordo_percipiente;
}
/* 
 * Getter dell'attributo im_netto_percipiente
 */
public java.math.BigDecimal getIm_netto_percipiente() {
	return im_netto_percipiente;
}
/* 
 * Getter dell'attributo im_no_fiscale
 */
public java.math.BigDecimal getIm_no_fiscale() {
	return im_no_fiscale;
}
/* 
 * Getter dell'attributo im_totale_compenso
 */
public java.math.BigDecimal getIm_totale_compenso() {
	return im_totale_compenso;
}
/* 
 * Getter dell'attributo imponibile_fiscale
 */
public java.math.BigDecimal getImponibile_fiscale() {
	return imponibile_fiscale;
}
/* 
 * Getter dell'attributo imponibile_fiscale_netto
 */
public java.math.BigDecimal getImponibile_fiscale_netto() {
	return imponibile_fiscale_netto;
}
/* 
 * Getter dell'attributo imponibile_inail
 */
public java.math.BigDecimal getImponibile_inail() {
	return imponibile_inail;
}
/* 
 * Getter dell'attributo imponibile_iva
 */
public java.math.BigDecimal getImponibile_iva() {
	return imponibile_iva;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo nr_fattura_fornitore
 */
public java.lang.String getNr_fattura_fornitore() {
	return nr_fattura_fornitore;
}
/* 
 * Getter dell'attributo numero_giorni
 */
public java.lang.Integer getNumero_giorni() {
	return numero_giorni;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento_scadenzario
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/* 
 * Getter dell'attributo pg_banca_uo_cds
 */
public java.lang.Long getPg_banca_uo_cds() {
	return pg_banca_uo_cds;
}
/* 
 * Getter dell'attributo pg_comune_add
 */
public java.lang.Long getPg_comune_add() {
	return pg_comune_add;
}
/* 
 * Getter dell'attributo pg_doc_genrc
 */
public java.lang.Long getPg_doc_genrc() {
	return pg_doc_genrc;
}
/* 
 * Getter dell'attributo pg_missione
 */
public java.lang.Long getPg_missione() {
	return pg_missione;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/* 
 * Getter dell'attributo quota_esente
 */
public java.math.BigDecimal getQuota_esente() {
	return quota_esente;
}
/* 
 * Getter dell'attributo quota_esente_no_iva
 */
public java.math.BigDecimal getQuota_esente_no_iva() {
	return quota_esente_no_iva;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Getter dell'attributo stato_coan
 */
public java.lang.String getStato_coan() {
	return stato_coan;
}
/* 
 * Getter dell'attributo stato_cofi
 */
public java.lang.String getStato_cofi() {
	return stato_cofi;
}
/* 
 * Getter dell'attributo stato_coge
 */
public java.lang.String getStato_coge() {
	return stato_coge;
}
/* 
 * Getter dell'attributo stato_pagamento_fondo_eco
 */
public java.lang.String getStato_pagamento_fondo_eco() {
	return stato_pagamento_fondo_eco;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Getter dell'attributo ti_associato_manrev
 */
public java.lang.String getTi_associato_manrev() {
	return ti_associato_manrev;
}
/* 
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Setter dell'attributo aliquota_fiscale
 */
public void setAliquota_fiscale(java.math.BigDecimal aliquota_fiscale) {
	this.aliquota_fiscale = aliquota_fiscale;
}
/* 
 * Setter dell'attributo aliquota_irpef_da_missione
 */
public void setAliquota_irpef_da_missione(java.math.BigDecimal aliquota_irpef_da_missione) {
	this.aliquota_irpef_da_missione = aliquota_irpef_da_missione;
}
/* 
 * Setter dell'attributo aliquota_irpef_tassep
 */
public void setAliquota_irpef_tassep(java.math.BigDecimal aliquota_irpef_tassep) {
	this.aliquota_irpef_tassep = aliquota_irpef_tassep;
}
/* 
 * Setter dell'attributo cd_cdr_genrc
 */
public void setCd_cdr_genrc(java.lang.String cd_cdr_genrc) {
	this.cd_cdr_genrc = cd_cdr_genrc;
}
/* 
 * Setter dell'attributo cd_cds_accertamento
 */
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.cd_cds_accertamento = cd_cds_accertamento;
}
/* 
 * Setter dell'attributo cd_cds_doc_genrc
 */
public void setCd_cds_doc_genrc(java.lang.String cd_cds_doc_genrc) {
	this.cd_cds_doc_genrc = cd_cds_doc_genrc;
}
/* 
 * Setter dell'attributo cd_cds_missione
 */
public void setCd_cds_missione(java.lang.String cd_cds_missione) {
	this.cd_cds_missione = cd_cds_missione;
}
/* 
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_linea_attivita_genrc
 */
public void setCd_linea_attivita_genrc(java.lang.String cd_linea_attivita_genrc) {
	this.cd_linea_attivita_genrc = cd_linea_attivita_genrc;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_modalita_pag_uo_cds
 */
public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
	this.cd_modalita_pag_uo_cds = cd_modalita_pag_uo_cds;
}
/* 
 * Setter dell'attributo cd_provincia_add
 */
public void setCd_provincia_add(java.lang.String cd_provincia_add) {
	this.cd_provincia_add = cd_provincia_add;
}
/* 
 * Setter dell'attributo cd_regione_add
 */
public void setCd_regione_add(java.lang.String cd_regione_add) {
	this.cd_regione_add = cd_regione_add;
}
/* 
 * Setter dell'attributo cd_regione_irap
 */
public void setCd_regione_irap(java.lang.String cd_regione_irap) {
	this.cd_regione_irap = cd_regione_irap;
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
/* 
 * Setter dell'attributo cd_termini_pag_uo_cds
 */
public void setCd_termini_pag_uo_cds(java.lang.String cd_termini_pag_uo_cds) {
	this.cd_termini_pag_uo_cds = cd_termini_pag_uo_cds;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_terzo_uo_cds
 */
public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
	this.cd_terzo_uo_cds = cd_terzo_uo_cds;
}
/* 
 * Setter dell'attributo cd_tipo_doc_genrc
 */
public void setCd_tipo_doc_genrc(java.lang.String cd_tipo_doc_genrc) {
	this.cd_tipo_doc_genrc = cd_tipo_doc_genrc;
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo cd_tipologia_rischio
 */
public void setCd_tipologia_rischio(java.lang.String cd_tipologia_rischio) {
	this.cd_tipologia_rischio = cd_tipologia_rischio;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
/* 
 * Setter dell'attributo cd_uo_doc_genrc
 */
public void setCd_uo_doc_genrc(java.lang.String cd_uo_doc_genrc) {
	this.cd_uo_doc_genrc = cd_uo_doc_genrc;
}
/* 
 * Setter dell'attributo cd_uo_missione
 */
public void setCd_uo_missione(java.lang.String cd_uo_missione) {
	this.cd_uo_missione = cd_uo_missione;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}
/* 
 * Setter dell'attributo detrazione_altri
 */
public void setDetrazione_altri(java.math.BigDecimal detrazione_altri) {
	this.detrazione_altri = detrazione_altri;
}
/* 
 * Setter dell'attributo detrazione_altri_netto
 */
public void setDetrazione_altri_netto(java.math.BigDecimal detrazione_altri_netto) {
	this.detrazione_altri_netto = detrazione_altri_netto;
}
/* 
 * Setter dell'attributo detrazione_coniuge
 */
public void setDetrazione_coniuge(java.math.BigDecimal detrazione_coniuge) {
	this.detrazione_coniuge = detrazione_coniuge;
}
/* 
 * Setter dell'attributo detrazione_coniuge_netto
 */
public void setDetrazione_coniuge_netto(java.math.BigDecimal detrazione_coniuge_netto) {
	this.detrazione_coniuge_netto = detrazione_coniuge_netto;
}
/* 
 * Setter dell'attributo detrazione_figli
 */
public void setDetrazione_figli(java.math.BigDecimal detrazione_figli) {
	this.detrazione_figli = detrazione_figli;
}
/* 
 * Setter dell'attributo detrazione_figli_netto
 */
public void setDetrazione_figli_netto(java.math.BigDecimal detrazione_figli_netto) {
	this.detrazione_figli_netto = detrazione_figli_netto;
}
/* 
 * Setter dell'attributo detrazioni_la
 */
public void setDetrazioni_la(java.math.BigDecimal detrazioni_la) {
	this.detrazioni_la = detrazioni_la;
}
/* 
 * Setter dell'attributo detrazioni_la_netto
 */
public void setDetrazioni_la_netto(java.math.BigDecimal detrazioni_la_netto) {
	this.detrazioni_la_netto = detrazioni_la_netto;
}
/* 
 * Setter dell'attributo detrazioni_personali
 */
public void setDetrazioni_personali(java.math.BigDecimal detrazioni_personali) {
	this.detrazioni_personali = detrazioni_personali;
}
/* 
 * Setter dell'attributo detrazioni_personali_netto
 */
public void setDetrazioni_personali_netto(java.math.BigDecimal detrazioni_personali_netto) {
	this.detrazioni_personali_netto = detrazioni_personali_netto;
}
/* 
 * Setter dell'attributo ds_compenso
 */
public void setDs_compenso(java.lang.String ds_compenso) {
	this.ds_compenso = ds_compenso;
}
/* 
 * Setter dell'attributo dt_a_competenza_coge
 */
public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
	this.dt_a_competenza_coge = dt_a_competenza_coge;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_da_competenza_coge
 */
public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
	this.dt_da_competenza_coge = dt_da_competenza_coge;
}
/* 
 * Setter dell'attributo dt_emissione_mandato
 */
public void setDt_emissione_mandato(java.sql.Timestamp dt_emissione_mandato) {
	this.dt_emissione_mandato = dt_emissione_mandato;
}
/* 
 * Setter dell'attributo dt_fattura_fornitore
 */
public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore) {
	this.dt_fattura_fornitore = dt_fattura_fornitore;
}
/* 
 * Setter dell'attributo dt_pagamento_fondo_eco
 */
public void setDt_pagamento_fondo_eco(java.sql.Timestamp dt_pagamento_fondo_eco) {
	this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
}
/* 
 * Setter dell'attributo dt_pagamento_mandato
 */
public void setDt_pagamento_mandato(java.sql.Timestamp dt_pagamento_mandato) {
	this.dt_pagamento_mandato = dt_pagamento_mandato;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo dt_trasmissione_mandato
 */
public void setDt_trasmissione_mandato(java.sql.Timestamp dt_trasmissione_mandato) {
	this.dt_trasmissione_mandato = dt_trasmissione_mandato;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}
/* 
 * Setter dell'attributo esercizio_doc_genrc
 */
public void setEsercizio_doc_genrc(java.lang.Integer esercizio_doc_genrc) {
	this.esercizio_doc_genrc = esercizio_doc_genrc;
}
/* 
 * Setter dell'attributo esercizio_fattura_fornitore
 */
public void setEsercizio_fattura_fornitore(java.lang.Integer esercizio_fattura_fornitore) {
	this.esercizio_fattura_fornitore = esercizio_fattura_fornitore;
}
/* 
 * Setter dell'attributo esercizio_missione
 */
public void setEsercizio_missione(java.lang.Integer esercizio_missione) {
	this.esercizio_missione = esercizio_missione;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo fl_accantona_add_terr
 */
public void setFl_accantona_add_terr(java.lang.Boolean fl_accantona_add_terr) {
	this.fl_accantona_add_terr = fl_accantona_add_terr;
}
/* 
 * Setter dell'attributo fl_compenso_conguaglio
 */
public void setFl_compenso_conguaglio(java.lang.Boolean fl_compenso_conguaglio) {
	this.fl_compenso_conguaglio = fl_compenso_conguaglio;
}
/* 
 * Setter dell'attributo fl_compenso_mcarriera_tassep
 */
public void setFl_compenso_mcarriera_tassep(java.lang.Boolean fl_compenso_mcarriera_tassep) {
	this.fl_compenso_mcarriera_tassep = fl_compenso_mcarriera_tassep;
}
/* 
 * Setter dell'attributo fl_compenso_minicarriera
 */
public void setFl_compenso_minicarriera(java.lang.Boolean fl_compenso_minicarriera) {
	this.fl_compenso_minicarriera = fl_compenso_minicarriera;
}
/* 
 * Setter dell'attributo fl_compenso_stipendi
 */
public void setFl_compenso_stipendi(java.lang.Boolean fl_compenso_stipendi) {
	this.fl_compenso_stipendi = fl_compenso_stipendi;
}
/* 
 * Setter dell'attributo fl_diaria
 */
public void setFl_diaria(java.lang.Boolean fl_diaria) {
	this.fl_diaria = fl_diaria;
}
/* 
 * Setter dell'attributo fl_escludi_qvaria_deduzione
 */
public void setFl_escludi_qvaria_deduzione(java.lang.Boolean fl_escludi_qvaria_deduzione) {
	this.fl_escludi_qvaria_deduzione = fl_escludi_qvaria_deduzione;
}
/* 
 * Setter dell'attributo fl_generata_fattura
 */
public void setFl_generata_fattura(java.lang.Boolean fl_generata_fattura) {
	this.fl_generata_fattura = fl_generata_fattura;
}
/* 
 * Setter dell'attributo fl_intera_qfissa_deduzione
 */
public void setFl_intera_qfissa_deduzione(java.lang.Boolean fl_intera_qfissa_deduzione) {
	this.fl_intera_qfissa_deduzione = fl_intera_qfissa_deduzione;
}
/* 
 * Setter dell'attributo fl_recupero_rate
 */
public void setFl_recupero_rate(java.lang.Boolean fl_recupero_rate) {
	this.fl_recupero_rate = fl_recupero_rate;
}
/* 
 * Setter dell'attributo fl_senza_calcoli
 */
public void setFl_senza_calcoli(java.lang.Boolean fl_senza_calcoli) {
	this.fl_senza_calcoli = fl_senza_calcoli;
}
/* 
 * Setter dell'attributo im_cr_ente
 */
public void setIm_cr_ente(java.math.BigDecimal im_cr_ente) {
	this.im_cr_ente = im_cr_ente;
}
/* 
 * Setter dell'attributo im_cr_percipiente
 */
public void setIm_cr_percipiente(java.math.BigDecimal im_cr_percipiente) {
	this.im_cr_percipiente = im_cr_percipiente;
}
/* 
 * Setter dell'attributo im_deduzione_irpef
 */
public void setIm_deduzione_irpef(java.math.BigDecimal im_deduzione_irpef) {
	this.im_deduzione_irpef = im_deduzione_irpef;
}
/* 
 * Setter dell'attributo im_detrazione_personale_anag
 */
public void setIm_detrazione_personale_anag(java.math.BigDecimal im_detrazione_personale_anag) {
	this.im_detrazione_personale_anag = im_detrazione_personale_anag;
}
/* 
 * Setter dell'attributo im_lordo_percipiente
 */
public void setIm_lordo_percipiente(java.math.BigDecimal im_lordo_percipiente) {
	this.im_lordo_percipiente = im_lordo_percipiente;
}
/* 
 * Setter dell'attributo im_netto_percipiente
 */
public void setIm_netto_percipiente(java.math.BigDecimal im_netto_percipiente) {
	this.im_netto_percipiente = im_netto_percipiente;
}
/* 
 * Setter dell'attributo im_no_fiscale
 */
public void setIm_no_fiscale(java.math.BigDecimal im_no_fiscale) {
	this.im_no_fiscale = im_no_fiscale;
}
/* 
 * Setter dell'attributo im_totale_compenso
 */
public void setIm_totale_compenso(java.math.BigDecimal im_totale_compenso) {
	this.im_totale_compenso = im_totale_compenso;
}
/* 
 * Setter dell'attributo imponibile_fiscale
 */
public void setImponibile_fiscale(java.math.BigDecimal imponibile_fiscale) {
	this.imponibile_fiscale = imponibile_fiscale;
}
/* 
 * Setter dell'attributo imponibile_fiscale_netto
 */
public void setImponibile_fiscale_netto(java.math.BigDecimal imponibile_fiscale_netto) {
	this.imponibile_fiscale_netto = imponibile_fiscale_netto;
}
/* 
 * Setter dell'attributo imponibile_inail
 */
public void setImponibile_inail(java.math.BigDecimal imponibile_inail) {
	this.imponibile_inail = imponibile_inail;
}
/* 
 * Setter dell'attributo imponibile_iva
 */
public void setImponibile_iva(java.math.BigDecimal imponibile_iva) {
	this.imponibile_iva = imponibile_iva;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo nr_fattura_fornitore
 */
public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore) {
	this.nr_fattura_fornitore = nr_fattura_fornitore;
}
/* 
 * Setter dell'attributo numero_giorni
 */
public void setNumero_giorni(java.lang.Integer numero_giorni) {
	this.numero_giorni = numero_giorni;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento_scadenzario
 */
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
/* 
 * Setter dell'attributo pg_banca_uo_cds
 */
public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
	this.pg_banca_uo_cds = pg_banca_uo_cds;
}
/* 
 * Setter dell'attributo pg_comune_add
 */
public void setPg_comune_add(java.lang.Long pg_comune_add) {
	this.pg_comune_add = pg_comune_add;
}
/* 
 * Setter dell'attributo pg_doc_genrc
 */
public void setPg_doc_genrc(java.lang.Long pg_doc_genrc) {
	this.pg_doc_genrc = pg_doc_genrc;
}
/* 
 * Setter dell'attributo pg_missione
 */
public void setPg_missione(java.lang.Long pg_missione) {
	this.pg_missione = pg_missione;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
/* 
 * Setter dell'attributo quota_esente
 */
public void setQuota_esente(java.math.BigDecimal quota_esente) {
	this.quota_esente = quota_esente;
}
/* 
 * Setter dell'attributo quota_esente_no_iva
 */
public void setQuota_esente_no_iva(java.math.BigDecimal quota_esente_no_iva) {
	this.quota_esente_no_iva = quota_esente_no_iva;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
/* 
 * Setter dell'attributo stato_coan
 */
public void setStato_coan(java.lang.String stato_coan) {
	this.stato_coan = stato_coan;
}
/* 
 * Setter dell'attributo stato_cofi
 */
public void setStato_cofi(java.lang.String stato_cofi) {
	this.stato_cofi = stato_cofi;
}
/* 
 * Setter dell'attributo stato_coge
 */
public void setStato_coge(java.lang.String stato_coge) {
	this.stato_coge = stato_coge;
}
/* 
 * Setter dell'attributo stato_pagamento_fondo_eco
 */
public void setStato_pagamento_fondo_eco(java.lang.String stato_pagamento_fondo_eco) {
	this.stato_pagamento_fondo_eco = stato_pagamento_fondo_eco;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
/* 
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
	/**
	 * Returns the quota_esente_inps.
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getQuota_esente_inps() {
		return quota_esente_inps;
	}

	/**
	 * Sets the quota_esente_inps.
	 * @param quota_esente_inps The quota_esente_inps to set
	 */
	public void setQuota_esente_inps(java.math.BigDecimal quota_esente_inps) {
		this.quota_esente_inps = quota_esente_inps;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_altra_ass_inps() {
		return cd_altra_ass_inps;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_attivita_inps() {
		return cd_attivita_inps;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_rapporto_inps() {
		return cd_rapporto_inps;
	}

	/**
	 * @param string
	 */
	public void setCd_altra_ass_inps(java.lang.String string) {
		cd_altra_ass_inps = string;
	}

	/**
	 * @param string
	 */
	public void setCd_attivita_inps(java.lang.String string) {
		cd_attivita_inps = string;
	}

	/**
	 * @param string
	 */
	public void setCd_rapporto_inps(java.lang.String string) {
		this.cd_rapporto_inps = string;
	}
	public java.lang.Long getPg_comune_inps() {
		return pg_comune_inps;
	}
	public void setPg_comune_inps(java.lang.Long pg_comune_inps) {
		this.pg_comune_inps = pg_comune_inps;
	}
	public java.math.BigDecimal getIm_reddito_abitaz_princ() {
		return im_reddito_abitaz_princ;
	}
	public void setIm_reddito_abitaz_princ(
			java.math.BigDecimal im_reddito_abitaz_princ) {
		this.im_reddito_abitaz_princ = im_reddito_abitaz_princ;
	}
	public java.math.BigDecimal getIm_reddito_complessivo() {
		return im_reddito_complessivo;
	}
	public void setIm_reddito_complessivo(
			java.math.BigDecimal im_reddito_complessivo) {
		this.im_reddito_complessivo = im_reddito_complessivo;
	}
	public java.lang.Integer getEsercizio_limite_rep() {
		return esercizio_limite_rep;
	}
	public void setEsercizio_limite_rep(java.lang.Integer esercizio_limite_rep) {
		this.esercizio_limite_rep = esercizio_limite_rep;
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
	public java.math.BigDecimal getIm_netto_da_trattenere() {
		return im_netto_da_trattenere;
	}
	public void setIm_netto_da_trattenere(
			java.math.BigDecimal im_netto_da_trattenere) {
		this.im_netto_da_trattenere = im_netto_da_trattenere;
	}
	public java.lang.String getTi_prestazione() {
		return ti_prestazione;
	}
	public void setTi_prestazione(java.lang.String ti_prestazione) {
		this.ti_prestazione = ti_prestazione;
	}
	public java.lang.Integer getEsercizio_bonus() {
		return esercizio_bonus;
	}
	public void setEsercizio_bonus(java.lang.Integer esercizio_bonus) {
		this.esercizio_bonus = esercizio_bonus;
	}
	public java.lang.Long getPg_bonus() {
		return pg_bonus;
	}
	public void setPg_bonus(java.lang.Long pg_bonus) {
		this.pg_bonus = pg_bonus;
	}
	public java.lang.Boolean getFl_liquidazione_differita() {
		return fl_liquidazione_differita;
	}
	public void setFl_liquidazione_differita(
			java.lang.Boolean fl_liquidazione_differita) {
		this.fl_liquidazione_differita = fl_liquidazione_differita;
	}
	public java.lang.Integer getCd_terzo_pignorato() {
		return cd_terzo_pignorato;
	}
	public void setCd_terzo_pignorato(java.lang.Integer cd_terzo_pignorato) {
		this.cd_terzo_pignorato = cd_terzo_pignorato;
	}
	public java.lang.Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}
	public void setEsercizio_contratto(java.lang.Integer esercizio_contratto) {
		this.esercizio_contratto = esercizio_contratto;
	}
	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}
	public void setStato_contratto(java.lang.String stato_contratto) {
		this.stato_contratto = stato_contratto;
	}
	public java.lang.Long getPg_contratto() {
		return pg_contratto;
	}
	public void setPg_contratto(java.lang.Long pg_contratto) {
		this.pg_contratto = pg_contratto;
	}
	public java.lang.Long getPg_trovato() {
		return pg_trovato;
	}
	public void setPg_trovato(java.lang.Long pg_trovato) {
		this.pg_trovato = pg_trovato;
	}
	
	public java.math.BigDecimal getIm_tot_reddito_complessivo() {
		return im_tot_reddito_complessivo;
	}
	public void setIm_tot_reddito_complessivo(
			java.math.BigDecimal im_tot_reddito_complessivo) {
		this.im_tot_reddito_complessivo = im_tot_reddito_complessivo;
	}
	public java.sql.Timestamp getDt_scadenza() {
		return dt_scadenza;
	}
	public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
		this.dt_scadenza = dt_scadenza;
	}
	public java.sql.Timestamp getData_protocollo() {
		return data_protocollo;
	}
	public void setData_protocollo(java.sql.Timestamp data_protocollo) {
		this.data_protocollo = data_protocollo;
	}
	public java.lang.String getNumero_protocollo() {
		return numero_protocollo;
	}
	public void setNumero_protocollo(java.lang.String numero_protocollo) {
		this.numero_protocollo = numero_protocollo;
	}
	public java.lang.String getStato_liquidazione() {
		return stato_liquidazione;
	}
	public void setStato_liquidazione(java.lang.String stato_liquidazione) {
		this.stato_liquidazione = stato_liquidazione;
	}
	public java.lang.String getCausale() {
		return causale;
	}
	public void setCausale(java.lang.String causale) {
		this.causale = causale;
	}
	public java.lang.Boolean getFl_documento_ele() {
		return fl_documento_ele;
	}
	public void setFl_documento_ele(java.lang.Boolean fl_documento_ele) {
		this.fl_documento_ele = fl_documento_ele;
	}
	public java.lang.Boolean getFl_split_payment() {
		return fl_split_payment;
	}
	public void setFl_split_payment(java.lang.Boolean fl_split_payment) {
		this.fl_split_payment = fl_split_payment;
	}
	public String getMotivo_assenza_cig() {
		return motivo_assenza_cig;
	}
	public void setMotivo_assenza_cig(String motivo_assenza_cig) {
		this.motivo_assenza_cig = motivo_assenza_cig;
	}
	public BigDecimal getDetrazioneRiduzioneCuneo() {
		return detrazioneRiduzioneCuneo;
	}

	public void setDetrazioneRiduzioneCuneo(BigDecimal detrazioneRiduzioneCuneo) {
		this.detrazioneRiduzioneCuneo = detrazioneRiduzioneCuneo;
	}

	public BigDecimal getDetrazioneRidCuneoNetto() {
		return detrazioneRidCuneoNetto;
	}

	public void setDetrazioneRidCuneoNetto(BigDecimal detrazioneRidCuneoNetto) {
		this.detrazioneRidCuneoNetto = detrazioneRidCuneoNetto;
	}

}
