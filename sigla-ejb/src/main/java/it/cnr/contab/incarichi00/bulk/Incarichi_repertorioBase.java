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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_repertorioBase extends Incarichi_repertorioKey implements Keyed {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;
 
//    ESERCIZIO_PROCEDURA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_procedura;
 
//    PG_PROCEDURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_procedura;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_cancellazione;

//    DT_STIPULA TIMESTAMP(7)
	private java.sql.Timestamp dt_stipula;
 
//    DT_INIZIO_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_inizio_validita;
 
//    DT_FINE_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_validita;
 
//    DT_PROROGA TIMESTAMP(7)
	private java.sql.Timestamp dt_proroga;
 
//    DT_PROROGA_PAGAM TIMESTAMP(7)
	private java.sql.Timestamp dt_proroga_pagam;

//   TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;

//    CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

//    CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;
 
//    FL_PUBBLICA_CONTRATTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pubblica_contratto;

//    IMPORTO_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_lordo;

//    IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_complessivo;

//    FL_INVIATO_CORTE_CONTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_inviato_corte_conti;

//    DT_INVIO_CORTE_CONTI TIMESTAMP(7)
	private java.sql.Timestamp dt_invio_corte_conti;

//    ESITO_CORTE_CONTI CHAR(3)
	private java.lang.String esito_corte_conti;

//    CD_PROVV VARCHAR(20)
	private java.lang.String cd_provv;

//    NUMERO_PROVV DECIMAL(10,0)
	private java.lang.Integer nr_provv;

//    DT_PROVV TIMESTAMP(7)
	private java.sql.Timestamp dt_provv;

//    DT_EFFICACIA TIMESTAMP(7)
	private java.sql.Timestamp dt_efficacia;

	private java.lang.Integer idPerla;

	private java.lang.String anomalia_perla;

	public Incarichi_repertorioBase() {
		super();
	}
	public Incarichi_repertorioBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio) {
		super(esercizio, pg_repertorio);
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.Integer getEsercizio_procedura() {
		return esercizio_procedura;
	}
	public void setEsercizio_procedura(java.lang.Integer esercizio_procedura)  {
		this.esercizio_procedura=esercizio_procedura;
	}
	public java.lang.Long getPg_procedura() {
		return pg_procedura;
	}
	public void setPg_procedura(java.lang.Long pg_procedura)  {
		this.pg_procedura=pg_procedura;
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.sql.Timestamp getDt_cancellazione() {
		return dt_cancellazione;
	}
	public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione)  {
		this.dt_cancellazione=dt_cancellazione;
	}
	public java.sql.Timestamp getDt_stipula() {
		return dt_stipula;
	}
	public void setDt_stipula(java.sql.Timestamp dt_stipula)  {
		this.dt_stipula=dt_stipula;
	}
	public java.sql.Timestamp getDt_inizio_validita() {
		return dt_inizio_validita;
	}
	public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita)  {
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita)  {
		this.dt_fine_validita=dt_fine_validita;
	}
	public java.sql.Timestamp getDt_proroga() {
		return dt_proroga;
	}
	public void setDt_proroga(java.sql.Timestamp dt_proroga)  {
		this.dt_proroga=dt_proroga;
	}
	public java.lang.String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}
	public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto)  {
		this.cd_tipo_rapporto=cd_tipo_rapporto;
	}
	public java.lang.String getCd_trattamento() {
		return cd_trattamento;
	}
	public void setCd_trattamento(java.lang.String cd_trattamento)  {
		this.cd_trattamento=cd_trattamento;
	}
	public java.lang.String getTi_istituz_commerc() {
		return ti_istituz_commerc;
	}
	public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
		this.ti_istituz_commerc = ti_istituz_commerc;
	}
	public java.lang.Boolean getFl_pubblica_contratto() {
		return fl_pubblica_contratto;
	}
	public void setFl_pubblica_contratto(java.lang.Boolean fl_pubblica_contratto) {
		this.fl_pubblica_contratto = fl_pubblica_contratto;
	}
	public java.math.BigDecimal getImporto_complessivo() {
		return importo_complessivo;
	}
	public void setImporto_complessivo(java.math.BigDecimal importo_complessivo) {
		this.importo_complessivo = importo_complessivo;
	}
	public java.math.BigDecimal getImporto_lordo() {
		return importo_lordo;
	}
	public void setImporto_lordo(java.math.BigDecimal importo_lordo) {
		this.importo_lordo = importo_lordo;
	}
	public java.sql.Timestamp getDt_proroga_pagam() {
		return dt_proroga_pagam;
	}
	public void setDt_proroga_pagam(java.sql.Timestamp dt_proroga_pagam) {
		this.dt_proroga_pagam = dt_proroga_pagam;
	}
	
	public java.lang.Boolean getFl_inviato_corte_conti() {
		return fl_inviato_corte_conti;
	}
	public void setFl_inviato_corte_conti(java.lang.Boolean fl_inviato_corte_conti) {
		this.fl_inviato_corte_conti = fl_inviato_corte_conti;
	}
	
	public java.sql.Timestamp getDt_invio_corte_conti() {
		return dt_invio_corte_conti;
	}
	public void setDt_invio_corte_conti(java.sql.Timestamp dt_invio_corte_conti) {
		this.dt_invio_corte_conti = dt_invio_corte_conti;
	}

	public java.lang.String getEsito_corte_conti() {
		return esito_corte_conti;
	}
	public void setEsito_corte_conti(java.lang.String esito_corte_conti) {
		this.esito_corte_conti = esito_corte_conti;
	}

	public java.lang.String getCd_provv() {
		return cd_provv;
	}
	public void setCd_provv(java.lang.String cdProvv) {
		cd_provv = cdProvv;
	}
	
	public java.lang.Integer getNr_provv() {
		return nr_provv;
	}
	public void setNr_provv(java.lang.Integer nrProvv) {
		nr_provv = nrProvv;
	}
	
	public java.sql.Timestamp getDt_provv() {
		return dt_provv;
	}
	public void setDt_provv(java.sql.Timestamp dtProvv) {
		dt_provv = dtProvv;
	}

    public java.sql.Timestamp getDt_efficacia() {
		return dt_efficacia;
	}
    public void setDt_efficacia(java.sql.Timestamp dt_efficacia) {
		this.dt_efficacia = dt_efficacia;
	}

	public Integer getIdPerla() {
		return idPerla;
	}

	public void setIdPerla(Integer idPerla) {
		this.idPerla = idPerla;
	}

	public String getAnomalia_perla() {
		return anomalia_perla;
	}

	public void setAnomalia_perla(String anomalia_perla) {
		this.anomalia_perla = anomalia_perla;
	}
}
