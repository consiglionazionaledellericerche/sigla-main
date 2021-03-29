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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.Timestamp;

public class AnagraficoBase extends AnagraficoKey implements Keyed {
	// ALIQUOTA_FISCALE DECIMAL(6,3)
	private java.math.BigDecimal aliquota_fiscale;

	// ALTRA_ASS_PREVID_INPS VARCHAR(5)
	private java.lang.String altra_ass_previd_inps;

	// CAP_COMUNE_FISCALE VARCHAR(5)
	private java.lang.String cap_comune_fiscale;

	// CAUSALE_FINE_RAPPORTO VARCHAR(50)
	private java.lang.String causale_fine_rapporto;

	// CD_ATTIVITA_INPS VARCHAR(5)
	private java.lang.String cd_attivita_inps;

	// CD_CLASSIFIC_ANAG VARCHAR(10)
	private java.lang.String cd_classific_anag;

	// TITOLO_STUDIO VARCHAR(6)
	private java.lang.String titolo_studio;

	// CD_ENTE_APPARTENENZA DECIMAL(8,0)
	private java.lang.Integer cd_ente_appartenenza;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// CODICE_FISCALE_CAF VARCHAR(20)
	private java.lang.String codice_fiscale_caf;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// CONTO_NUMERARIO_CREDITO VARCHAR(50)
	private java.lang.String conto_numerario_credito;

	// CONTO_NUMERARIO_DEBITO VARCHAR(50)
	private java.lang.String conto_numerario_debito;

	// DENOMINAZIONE_CAF VARCHAR(100)
	private java.lang.String denominazione_caf;

	// DT_ANTIMAFIA TIMESTAMP
	private java.sql.Timestamp dt_antimafia;
	private java.sql.Timestamp dtIniValGruppoIva;

	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_canc;

	// DT_FINE_RAPPORTO TIMESTAMP
	private java.sql.Timestamp dt_fine_rapporto;

	// DT_NASCITA TIMESTAMP
	private java.sql.Timestamp dt_nascita;

	// FL_FATTURAZIONE_DIFFERITA CHAR(1)
	private java.lang.Boolean fl_fatturazione_differita;

	// FL_OCCASIONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_occasionale;

	// FL_SOGGETTO_IVA CHAR(1) NOT NULL
	private java.lang.Boolean fl_soggetto_iva;

	// FRAZIONE_FISCALE VARCHAR(100)
	private java.lang.String frazione_fiscale;

	// ID_FISCALE_ESTERO VARCHAR(20)
	private java.lang.String id_fiscale_estero;

	// MATRICOLA_INAIL VARCHAR(30)
	private java.lang.String matricola_inail;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NOTE VARCHAR(300)
	private java.lang.String note;

	// NUM_CIVICO_FISCALE VARCHAR(10)
	private java.lang.String num_civico_fiscale;

	// NUM_ISCRIZ_ALBO VARCHAR(10)
	private java.lang.String num_iscriz_albo;

	// NUM_ISCRIZ_CCIAA VARCHAR(10)
	private java.lang.String num_iscriz_cciaa;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_COMUNE_FISCALE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_comune_fiscale;

	// PG_COMUNE_NASCITA DECIMAL(10,0)
	private java.lang.Long pg_comune_nascita;

	// PG_NAZIONE_FISCALE DECIMAL(10,0)
	private java.lang.Long pg_nazione_fiscale;

	// PG_NAZIONE_NAZIONALITA DECIMAL(10,0)
	private java.lang.Long pg_nazione_nazionalita;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// SEDE_INAIL VARCHAR(50)
	private java.lang.String sede_inail;

	// TI_ENTITA CHAR(1) NOT NULL
	private java.lang.String ti_entita;

	// TI_ENTITA_FISICA CHAR(1)
	private java.lang.String ti_entita_fisica;

	// TI_ENTITA_GIURIDICA CHAR(1)
	private java.lang.String ti_entita_giuridica;

	// TI_ITALIANO_ESTERO CHAR(1) NOT NULL
	private java.lang.String ti_italiano_estero;

	// TI_SESSO CHAR(1)
	private java.lang.String ti_sesso;

	// VIA_FISCALE VARCHAR(100) NOT NULL
	private java.lang.String via_fiscale;
	
	// FL_CERVELLONI CHAR(1) NOT NULL
	private java.lang.Boolean fl_cervellone;
	
	// DT_INIZIO_RES_ITALIA TIMESTAMP
	private java.sql.Timestamp dt_inizio_res_italia;

	// DT_FINE_RES_ITALIA TIMESTAMP
	private java.sql.Timestamp dt_fine_res_italia;
	
	// ANNO_INIZIO_RES_FIS DECIMAL(4,0)
	private java.lang.Long anno_inizio_res_fis;

	// ANNO_FINE_AGEVOLAZIONI DECIMAL(4,0)
	private java.lang.Long anno_fine_agevolazioni;	
	
	// FL_NON_OBBLIG_P_IVA CHAR(1) NOT NULL
	private java.lang.Boolean fl_non_obblig_p_iva;
	
	// PG_TIPOLOGIA NUMBER(5)
	private java.lang.Number pg_tipologia;
	
	// FL_STUDIO_ASSOCIATO VARCHAR2(5) NOT NULL
	private java.lang.Boolean fl_studio_associato;

	// FL_SOSPENSIONE_IRPEF VARCHAR2(1) NOT NULL
	private java.lang.Boolean fl_sospensione_irpef;
	
	// FL_SPECIALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_speciale;
	
	// CODICE_AMMINISTRAZIONE_IPA VARCHAR(100)
	private java.lang.String codiceAmministrazioneIpa;

	// DATA_AVVIO_FATT_ELETTR TIMESTAMP
	private java.sql.Timestamp dataAvvioFattElettr;
	
	// FL_ABILITA_DIARIA_MISS_EST CHAR(1) NOT NULL
	private java.lang.Boolean fl_abilita_diaria_miss_est;
	
	// DT_INIZIO_DIARIA_MISS_EST TIMESTAMP
	private java.sql.Timestamp dt_inizio_diaria_miss_est;

	// DT_FINE_DIARIA_MISS_EST TIMESTAMP
	private java.sql.Timestamp dt_fine_diaria_miss_est;
	
	// FL_PIVA_VERIFICATA CHAR(1)
	private java.lang.String flPivaVerificata;
	
public AnagraficoBase() {
	super();
}
public AnagraficoBase(java.lang.Integer cd_anag) {
	super(cd_anag);
}
/* 
 * Getter dell'attributo aliquota_fiscale
 */
public java.math.BigDecimal getAliquota_fiscale() {
	return aliquota_fiscale;
}
/* 
 * Getter dell'attributo altra_ass_previd_inps
 */
public java.lang.String getAltra_ass_previd_inps() {
	return altra_ass_previd_inps;
}
/* 
 * Getter dell'attributo cap_comune_fiscale
 */
public java.lang.String getCap_comune_fiscale() {
	return cap_comune_fiscale;
}
/* 
 * Getter dell'attributo causale_fine_rapporto
 */
public java.lang.String getCausale_fine_rapporto() {
	return causale_fine_rapporto;
}
/* 
 * Getter dell'attributo cd_attivita_inps
 */
public java.lang.String getCd_attivita_inps() {
	return cd_attivita_inps;
}
/* 
 * Getter dell'attributo cd_classific_anag
 */
public java.lang.String getCd_classific_anag() {
	return cd_classific_anag;
}
/* 
 * Getter dell'attributo cd_ente_appartenenza
 */
public java.lang.Integer getCd_ente_appartenenza() {
	return cd_ente_appartenenza;
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo codice_fiscale_caf
 */
public java.lang.String getCodice_fiscale_caf() {
	return codice_fiscale_caf;
}
/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}
/* 
 * Getter dell'attributo conto_numerario_credito
 */
public java.lang.String getConto_numerario_credito() {
	return conto_numerario_credito;
}
/* 
 * Getter dell'attributo conto_numerario_debito
 */
public java.lang.String getConto_numerario_debito() {
	return conto_numerario_debito;
}
/* 
 * Getter dell'attributo denominazione_caf
 */
public java.lang.String getDenominazione_caf() {
	return denominazione_caf;
}
/* 
 * Getter dell'attributo dt_antimafia
 */
public java.sql.Timestamp getDt_antimafia() {
	return dt_antimafia;
}
/* 
 * Getter dell'attributo dt_canc
 */
public java.sql.Timestamp getDt_canc() {
	return dt_canc;
}
/* 
 * Getter dell'attributo dt_fine_rapporto
 */
public java.sql.Timestamp getDt_fine_rapporto() {
	return dt_fine_rapporto;
}
/* 
 * Getter dell'attributo dt_nascita
 */
public java.sql.Timestamp getDt_nascita() {
	return dt_nascita;
}
/* 
 * Getter dell'attributo fl_fatturazione_differita
 */
public java.lang.Boolean getFl_fatturazione_differita() {
	return fl_fatturazione_differita;
}
/* 
 * Getter dell'attributo fl_occasionale
 */
public java.lang.Boolean getFl_occasionale() {
	return fl_occasionale;
}
/* 
 * Getter dell'attributo fl_soggetto_iva
 */
public java.lang.Boolean getFl_soggetto_iva() {
	return fl_soggetto_iva;
}
/* 
 * Getter dell'attributo frazione_fiscale
 */
public java.lang.String getFrazione_fiscale() {
	return frazione_fiscale;
}
/* 
 * Getter dell'attributo id_fiscale_estero
 */
public java.lang.String getId_fiscale_estero() {
	return id_fiscale_estero;
}
/* 
 * Getter dell'attributo matricola_inail
 */
public java.lang.String getMatricola_inail() {
	return matricola_inail;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo note
 */
public java.lang.String getNote() {
	return note;
}
/* 
 * Getter dell'attributo num_civico_fiscale
 */
public java.lang.String getNum_civico_fiscale() {
	return num_civico_fiscale;
}
/* 
 * Getter dell'attributo num_iscriz_albo
 */
public java.lang.String getNum_iscriz_albo() {
	return num_iscriz_albo;
}
/* 
 * Getter dell'attributo num_iscriz_cciaa
 */
public java.lang.String getNum_iscriz_cciaa() {
	return num_iscriz_cciaa;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_comune_fiscale
 */
public java.lang.Long getPg_comune_fiscale() {
	return pg_comune_fiscale;
}
/* 
 * Getter dell'attributo pg_comune_nascita
 */
public java.lang.Long getPg_comune_nascita() {
	return pg_comune_nascita;
}
/* 
 * Getter dell'attributo pg_nazione_fiscale
 */
public java.lang.Long getPg_nazione_fiscale() {
	return pg_nazione_fiscale;
}
/* 
 * Getter dell'attributo pg_nazione_nazionalita
 */
public java.lang.Long getPg_nazione_nazionalita() {
	return pg_nazione_nazionalita;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Getter dell'attributo sede_inail
 */
public java.lang.String getSede_inail() {
	return sede_inail;
}
/* 
 * Getter dell'attributo ti_entita
 */
public java.lang.String getTi_entita() {
	return ti_entita;
}
/* 
 * Getter dell'attributo ti_entita_fisica
 */
public java.lang.String getTi_entita_fisica() {
	return ti_entita_fisica;
}
/* 
 * Getter dell'attributo ti_entita_giuridica
 */
public java.lang.String getTi_entita_giuridica() {
	return ti_entita_giuridica;
}
/* 
 * Getter dell'attributo ti_italiano_estero
 */
public java.lang.String getTi_italiano_estero() {
	return ti_italiano_estero;
}
/* 
 * Getter dell'attributo ti_sesso
 */
public java.lang.String getTi_sesso() {
	return ti_sesso;
}
/* 
 * Getter dell'attributo via_fiscale
 */
public java.lang.String getVia_fiscale() {
	return via_fiscale;
}
/* 
 * Setter dell'attributo aliquota_fiscale
 */
public void setAliquota_fiscale(java.math.BigDecimal aliquota_fiscale) {
	this.aliquota_fiscale = aliquota_fiscale;
}
/* 
 * Setter dell'attributo altra_ass_previd_inps
 */
public void setAltra_ass_previd_inps(java.lang.String altra_ass_previd_inps) {
	this.altra_ass_previd_inps = altra_ass_previd_inps;
}
/* 
 * Setter dell'attributo cap_comune_fiscale
 */
public void setCap_comune_fiscale(java.lang.String cap_comune_fiscale) {
	this.cap_comune_fiscale = cap_comune_fiscale;
}
/* 
 * Setter dell'attributo causale_fine_rapporto
 */
public void setCausale_fine_rapporto(java.lang.String causale_fine_rapporto) {
	this.causale_fine_rapporto = causale_fine_rapporto;
}
/* 
 * Setter dell'attributo cd_attivita_inps
 */
public void setCd_attivita_inps(java.lang.String cd_attivita_inps) {
	this.cd_attivita_inps = cd_attivita_inps;
}
/* 
 * Setter dell'attributo cd_classific_anag
 */
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.cd_classific_anag = cd_classific_anag;
}
/* 
 * Setter dell'attributo cd_ente_appartenenza
 */
public void setCd_ente_appartenenza(java.lang.Integer cd_ente_appartenenza) {
	this.cd_ente_appartenenza = cd_ente_appartenenza;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo codice_fiscale_caf
 */
public void setCodice_fiscale_caf(java.lang.String codice_fiscale_caf) {
	this.codice_fiscale_caf = codice_fiscale_caf;
}
/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}
/* 
 * Setter dell'attributo conto_numerario_credito
 */
public void setConto_numerario_credito(java.lang.String conto_numerario_credito) {
	this.conto_numerario_credito = conto_numerario_credito;
}
/* 
 * Setter dell'attributo conto_numerario_debito
 */
public void setConto_numerario_debito(java.lang.String conto_numerario_debito) {
	this.conto_numerario_debito = conto_numerario_debito;
}
/* 
 * Setter dell'attributo denominazione_caf
 */
public void setDenominazione_caf(java.lang.String denominazione_caf) {
	this.denominazione_caf = denominazione_caf;
}
/* 
 * Setter dell'attributo dt_antimafia
 */
public void setDt_antimafia(java.sql.Timestamp dt_antimafia) {
	this.dt_antimafia = dt_antimafia;
}
/* 
 * Setter dell'attributo dt_canc
 */
public void setDt_canc(java.sql.Timestamp dt_canc) {
	this.dt_canc = dt_canc;
}
/* 
 * Setter dell'attributo dt_fine_rapporto
 */
public void setDt_fine_rapporto(java.sql.Timestamp dt_fine_rapporto) {
	this.dt_fine_rapporto = dt_fine_rapporto;
}
/* 
 * Setter dell'attributo dt_nascita
 */
public void setDt_nascita(java.sql.Timestamp dt_nascita) {
	this.dt_nascita = dt_nascita;
}
/* 
 * Setter dell'attributo fl_fatturazione_differita
 */
public void setFl_fatturazione_differita(java.lang.Boolean fl_fatturazione_differita) {
	this.fl_fatturazione_differita = fl_fatturazione_differita;
}
/* 
 * Setter dell'attributo fl_occasionale
 */
public void setFl_occasionale(java.lang.Boolean fl_occasionale) {
	this.fl_occasionale = fl_occasionale;
}
/* 
 * Setter dell'attributo fl_soggetto_iva
 */
public void setFl_soggetto_iva(java.lang.Boolean fl_soggetto_iva) {
	this.fl_soggetto_iva = fl_soggetto_iva;
}
/* 
 * Setter dell'attributo frazione_fiscale
 */
public void setFrazione_fiscale(java.lang.String frazione_fiscale) {
	this.frazione_fiscale = frazione_fiscale;
}
/* 
 * Setter dell'attributo id_fiscale_estero
 */
public void setId_fiscale_estero(java.lang.String id_fiscale_estero) {
	this.id_fiscale_estero = id_fiscale_estero;
}
/* 
 * Setter dell'attributo matricola_inail
 */
public void setMatricola_inail(java.lang.String matricola_inail) {
	this.matricola_inail = matricola_inail;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo note
 */
public void setNote(java.lang.String note) {
	this.note = note;
}
/* 
 * Setter dell'attributo num_civico_fiscale
 */
public void setNum_civico_fiscale(java.lang.String num_civico_fiscale) {
	this.num_civico_fiscale = num_civico_fiscale;
}
/* 
 * Setter dell'attributo num_iscriz_albo
 */
public void setNum_iscriz_albo(java.lang.String num_iscriz_albo) {
	this.num_iscriz_albo = num_iscriz_albo;
}
/* 
 * Setter dell'attributo num_iscriz_cciaa
 */
public void setNum_iscriz_cciaa(java.lang.String num_iscriz_cciaa) {
	this.num_iscriz_cciaa = num_iscriz_cciaa;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_comune_fiscale
 */
public void setPg_comune_fiscale(java.lang.Long pg_comune_fiscale) {
	this.pg_comune_fiscale = pg_comune_fiscale;
}
/* 
 * Setter dell'attributo pg_comune_nascita
 */
public void setPg_comune_nascita(java.lang.Long pg_comune_nascita) {
	this.pg_comune_nascita = pg_comune_nascita;
}
/* 
 * Setter dell'attributo pg_nazione_fiscale
 */
public void setPg_nazione_fiscale(java.lang.Long pg_nazione_fiscale) {
	this.pg_nazione_fiscale = pg_nazione_fiscale;
}
/* 
 * Setter dell'attributo pg_nazione_nazionalita
 */
public void setPg_nazione_nazionalita(java.lang.Long pg_nazione_nazionalita) {
	this.pg_nazione_nazionalita = pg_nazione_nazionalita;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
/* 
 * Setter dell'attributo sede_inail
 */
public void setSede_inail(java.lang.String sede_inail) {
	this.sede_inail = sede_inail;
}
/* 
 * Setter dell'attributo ti_entita
 */
public void setTi_entita(java.lang.String ti_entita) {
	this.ti_entita = ti_entita;
}
/* 
 * Setter dell'attributo ti_entita_fisica
 */
public void setTi_entita_fisica(java.lang.String ti_entita_fisica) {
	this.ti_entita_fisica = ti_entita_fisica;
}
/* 
 * Setter dell'attributo ti_entita_giuridica
 */
public void setTi_entita_giuridica(java.lang.String ti_entita_giuridica) {
	this.ti_entita_giuridica = ti_entita_giuridica;
}
/* 
 * Setter dell'attributo ti_italiano_estero
 */
public void setTi_italiano_estero(java.lang.String ti_italiano_estero) {
	this.ti_italiano_estero = ti_italiano_estero;
}
/* 
 * Setter dell'attributo ti_sesso
 */
public void setTi_sesso(java.lang.String ti_sesso) {
	this.ti_sesso = ti_sesso;
}
/* 
 * Setter dell'attributo via_fiscale
 */
public void setVia_fiscale(java.lang.String via_fiscale) {
	this.via_fiscale = via_fiscale;
}
public java.lang.Long getAnno_fine_agevolazioni() {
	return anno_fine_agevolazioni;
}
public void setAnno_fine_agevolazioni(java.lang.Long anno_fine_agevolazioni) {
	this.anno_fine_agevolazioni = anno_fine_agevolazioni;
}
public java.lang.Long getAnno_inizio_res_fis() {
	return anno_inizio_res_fis;
}
public void setAnno_inizio_res_fis(java.lang.Long anno_inizio_res_fis) {
	this.anno_inizio_res_fis = anno_inizio_res_fis;
}
public java.sql.Timestamp getDt_fine_res_italia() {
	return dt_fine_res_italia;
}
public void setDt_fine_res_italia(java.sql.Timestamp dt_fine_res_italia) {
	this.dt_fine_res_italia = dt_fine_res_italia;
}
public java.sql.Timestamp getDt_inizio_res_italia() {
	return dt_inizio_res_italia;
}
public void setDt_inizio_res_italia(java.sql.Timestamp dt_inizio_res_italia) {
	this.dt_inizio_res_italia = dt_inizio_res_italia;
}
public java.lang.Boolean isFl_cervellone() {
	if (fl_cervellone!=null)
	   return fl_cervellone;
	return false;
}
public void setFl_cervellone(java.lang.Boolean fl_cervellone) {
	this.fl_cervellone = fl_cervellone;
}
public java.lang.Boolean getFl_cervellone() {
	return fl_cervellone;
}
public java.lang.Boolean getFl_non_obblig_p_iva() {
	return fl_non_obblig_p_iva;
}
public void setFl_non_obblig_p_iva(java.lang.Boolean fl_non_obblig_p_iva) {
	this.fl_non_obblig_p_iva = fl_non_obblig_p_iva;
}

public void setPg_tipologia(java.lang.Number pg_tipologia) {
	this.pg_tipologia = pg_tipologia;
}

public java.lang.Number getPg_tipologia() {
	return pg_tipologia;
}
public java.lang.Boolean getFl_studio_associato() {
	return fl_studio_associato;
}
public void setFl_studio_associato(java.lang.Boolean fl_studio_associato) {
	this.fl_studio_associato = fl_studio_associato;
}
public java.lang.Boolean getFl_speciale() {
	return fl_speciale;
}
public void setFl_speciale(java.lang.Boolean fl_speciale) {
	this.fl_speciale = fl_speciale;
}
public java.lang.Boolean getFl_sospensione_irpef() {
	return fl_sospensione_irpef;
}
public void setFl_sospensione_irpef(java.lang.Boolean fl_sospensione_irpef) {
	this.fl_sospensione_irpef = fl_sospensione_irpef;
}
public java.lang.String getTitolo_studio() {
	return titolo_studio;
}
public void setTitolo_studio(java.lang.String titolo_studio) {
	this.titolo_studio = titolo_studio;
}
public java.lang.String getCodiceAmministrazioneIpa() {
	return codiceAmministrazioneIpa;
}
public void setCodiceAmministrazioneIpa(
		java.lang.String codiceAmministrazioneIpa) {
	this.codiceAmministrazioneIpa = codiceAmministrazioneIpa;
}
public java.sql.Timestamp getDataAvvioFattElettr() {
	return dataAvvioFattElettr;
}
public void setDataAvvioFattElettr(java.sql.Timestamp dataAvvioFattElettr) {
	this.dataAvvioFattElettr = dataAvvioFattElettr;
}
public java.lang.Boolean isFl_abilita_diaria_miss_est() {
	if (fl_abilita_diaria_miss_est!=null)
	   return fl_abilita_diaria_miss_est;
	return false;
}
public void setFl_abilita_diaria_miss_est(java.lang.Boolean fl_abilita_diaria_miss_est) {
	this.fl_abilita_diaria_miss_est = fl_abilita_diaria_miss_est;
}
public java.lang.Boolean getFl_abilita_diaria_miss_est() {
	return fl_abilita_diaria_miss_est;
}
public java.sql.Timestamp getDt_inizio_diaria_miss_est() {
	return dt_inizio_diaria_miss_est;
}
public void setDt_inizio_diaria_miss_est(java.sql.Timestamp dt_inizio_diaria_miss_est) {
	this.dt_inizio_diaria_miss_est = dt_inizio_diaria_miss_est;
}
public java.sql.Timestamp getDt_fine_diaria_miss_est() {
	return dt_fine_diaria_miss_est;
}
public void setDt_fine_diaria_miss_est(java.sql.Timestamp dt_fine_diaria_miss_est) {
	this.dt_fine_diaria_miss_est = dt_fine_diaria_miss_est;
}
public java.lang.String getFlPivaVerificata() {
	return flPivaVerificata;
}
public void setFlPivaVerificata(java.lang.String flPivaVerificata) {
	this.flPivaVerificata = flPivaVerificata;
}
	public Timestamp getDtIniValGruppoIva() {
		return dtIniValGruppoIva;
	}

	public void setDtIniValGruppoIva(Timestamp dtIniValGruppoIva) {
		this.dtIniValGruppoIva = dtIniValGruppoIva;
	}

}
