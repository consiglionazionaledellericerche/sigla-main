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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.persistency.Keyed;

public class Incarichi_parametriBase extends Incarichi_parametriKey implements Keyed {
//  PUBBLICA_CONTRATTO CHAR(1)
	@FieldPropertyAnnotation(name="fl_pubblica_contratto",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Pubblica contratti stipulati")
	private java.lang.String pubblica_contratto;
 
//  ALLEGA_CONTRATTO CHAR(1)
	@FieldPropertyAnnotation(name="allega_contratto",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega contratti stipulati")
	private java.lang.String allega_contratto;

//  ALLEGA_DECISIONE_CTR CHAR(1)
	@FieldPropertyAnnotation(name="allega_decisione_ctr",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega decisione a contrattare")
	private java.lang.String allega_decisione_ctr;

//  ALLEGA_DECRETO_NOMINA CHAR(1)
	@FieldPropertyAnnotation(name="fl_allega_decreto_nomina",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega decreto di nomina")
	private java.lang.String allega_decreto_nomina;

//  RICERCA_INTERNA CHAR(1)
	@FieldPropertyAnnotation(name="ricerca_interna",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Ricerca Professionalità Interne Obbligatorio")
	private java.lang.String ricerca_interna;
	
//  MERAMENTE_OCCASIONALE CHAR(1)
	@FieldPropertyAnnotation(name="meramente_occasionale",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Gestione dei Collaboratori Meramente Occasionali")
	private java.lang.String meramente_occasionale;

//  LIMITE_DT_STIPULA CHAR(1)
	@FieldPropertyAnnotation(name="limite_dt_stipula",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Limite Data Stipula")
	private java.lang.String limite_dt_stipula;

//  GIORNI_LIMITE_DT_STIPULA  NUMBER(3)
	@FieldPropertyAnnotation(name="giorni_limite_dt_stipula",
			inputType=InputType.TEXT,
			formatName = FormatName.EuroFormat,
			maxLength=16,
			inputSize=16,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Giorni limite data stipula")
	private Integer giorni_limite_dt_stipula;
	
//  IMPORTO_LIMITE_MEROCC  NUMBER(15,2)
	@FieldPropertyAnnotation(name="importo_limite_merocc",
			inputType=InputType.TEXT,
			formatName = FormatName.EuroFormat,
			maxLength=16,
			inputSize=16,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Importo Limite per Meramente Occasionali")
	private java.math.BigDecimal importo_limite_merocc;

//  INCARICO_RIC_GIORNI_PUBBL   NUMBER(3)
	@FieldPropertyAnnotation(name="incarico_ric_giorni_pubbl",
			inputType=InputType.TEXT,
			maxLength=3,
			inputSize=3,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			label="Giorni di pubblicazione di richiesta incarico per collaborazione")
	private Integer incarico_ric_giorni_pubbl;

//  INCARICO_RIC_GIORNI_SCAD   NUMBER(3)
	@FieldPropertyAnnotation(name="incarico_ric_giorni_scad",
			inputType=InputType.TEXT,
			maxLength=3,
			inputSize=3,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			label="Giorni di validità di richiesta incarico per collaborazione dopo la pubblicazione")
	private Integer incarico_ric_giorni_scad;

//  CD_GRUPPO_FILE VARCHAR(10)
	@FieldPropertyAnnotation(name="cd_gruppo_file",
			inputType=InputType.TEXT,
			inputSize=10,
			maxLength=10,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Codice Gruppo File")
	private java.lang.String cd_gruppo_file;
	
//  ALLEGA_CURRICULUM_VITAE CHAR(1)
	@FieldPropertyAnnotation(name="allega_curriculum_vitae",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega Curriculum Vitae")
	private java.lang.String allega_curriculum_vitae;
	
//  ALLEGA_DICH_CONTRAENTE CHAR(1)
	@FieldPropertyAnnotation(name="allega_dich_contraente",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega Dichiarazione Contraente")
	private java.lang.String allega_dich_contraente;

//  ALLEGA_PROGETTO CHAR(1)
	@FieldPropertyAnnotation(name="allega_progetto",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega Progetto")
	private java.lang.String allega_progetto;

	//  ALLEGA_INSUSSISTENZA_CONFLITTO_INTERESSE CHAR(1)
	@FieldPropertyAnnotation(name="allega_conflitto_interesse",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Allega Insussistenza Conflitto Interesse")
	private java.lang.String allega_conflitto_interesse;

//  INDICA_URL_PROGETTO CHAR(1)
	@FieldPropertyAnnotation(name="indica_url_progetto",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Indica Url Progetto")
	private java.lang.String indica_url_progetto;

//  FL_INVIO_FP CHAR(1)
	@FieldPropertyAnnotation(name="fl_invio_fp",
			inputType=InputType.TEXT,
			enabledOnSearch=true,
			enabledOnInsert=false,
			enabledOnEdit=false,
			nullable=false,
			label="Invio alla funzione pubblica")
	private java.lang.String fl_invio_fp;

	public Incarichi_parametriBase() {
		super();
	}
	public Incarichi_parametriBase(java.lang.String cd_parametri) {
		super(cd_parametri);
	}

	public java.lang.String getPubblica_contratto() {
		return pubblica_contratto;
	}
	public void setPubblica_contratto(java.lang.String pubblica_contratto) {
		this.pubblica_contratto = pubblica_contratto;
	}

	public java.lang.String getAllega_contratto() {
		return allega_contratto;
	}
	public void setAllega_contratto(java.lang.String allega_contratto) {
		this.allega_contratto = allega_contratto;
	}

	public java.lang.String getAllega_decisione_ctr() {
		return allega_decisione_ctr;
	}
	public void setAllega_decisione_ctr(java.lang.String allega_decisione_ctr) {
		this.allega_decisione_ctr = allega_decisione_ctr;
	}

	public java.lang.String getAllega_decreto_nomina() {
		return allega_decreto_nomina;
	}
	public void setAllega_decreto_nomina(java.lang.String allega_decreto_nomina) {
		this.allega_decreto_nomina = allega_decreto_nomina;
	}

	public java.lang.String getRicerca_interna() {
		return ricerca_interna;
	}
	public void setRicerca_interna(java.lang.String ricerca_interna) {
		this.ricerca_interna = ricerca_interna;
	}

	public java.lang.String getMeramente_occasionale() {
		return meramente_occasionale;
	}
	public void setMeramente_occasionale(java.lang.String meramente_occasionale) {
		this.meramente_occasionale = meramente_occasionale;
	}

	public java.lang.String getLimite_dt_stipula() {
		return limite_dt_stipula;
	}
	public void setLimite_dt_stipula(java.lang.String limite_dt_stipula) {
		this.limite_dt_stipula = limite_dt_stipula;
	}

	public Integer getGiorni_limite_dt_stipula() {
		return giorni_limite_dt_stipula;
	}
	public void setGiorni_limite_dt_stipula(Integer giorni_limite_dt_stipula) {
		this.giorni_limite_dt_stipula = giorni_limite_dt_stipula;
	}
	
	public java.math.BigDecimal getImporto_limite_merocc() {
		return importo_limite_merocc;
	}
	public void setImporto_limite_merocc(java.math.BigDecimal importo_limite_merocc) {
		this.importo_limite_merocc = importo_limite_merocc;
	}

	public java.lang.Integer getIncarico_ric_giorni_pubbl() {
		return incarico_ric_giorni_pubbl;
	}
	public void setIncarico_ric_giorni_pubbl(java.lang.Integer incarico_ric_giorni_pubbl) {
		this.incarico_ric_giorni_pubbl = incarico_ric_giorni_pubbl;
	}

	public java.lang.Integer getIncarico_ric_giorni_scad() {
		return incarico_ric_giorni_scad;
	}
	public void setIncarico_ric_giorni_scad(java.lang.Integer incarico_ric_giorni_scad) {
		this.incarico_ric_giorni_scad = incarico_ric_giorni_scad;
	}

	public java.lang.String getCd_gruppo_file() {
		return cd_gruppo_file;
	}
	public void setCd_gruppo_file(java.lang.String cd_gruppo_file) {
		this.cd_gruppo_file = cd_gruppo_file;
	}

	public java.lang.String getAllega_curriculum_vitae() {
		return allega_curriculum_vitae;
	}
	public void setAllega_curriculum_vitae(java.lang.String allega_curriculum_vitae) {
		this.allega_curriculum_vitae = allega_curriculum_vitae;
	}

	public java.lang.String getAllega_dich_contraente() {
		return allega_dich_contraente;
	}
	public void setAllega_dich_contraente(java.lang.String allega_dich_contraente) {
		this.allega_dich_contraente = allega_dich_contraente;
	}

	public java.lang.String getAllega_progetto() {
		return allega_progetto;
	}
	public void setAllega_progetto(java.lang.String allega_progetto) {
		this.allega_progetto = allega_progetto;
	}

	public java.lang.String getIndica_url_progetto() {
		return indica_url_progetto;
	}
	public void setIndica_url_progetto(java.lang.String indica_url_progetto) {
		this.indica_url_progetto = indica_url_progetto;
	}

	public java.lang.String getFl_invio_fp() {
		return fl_invio_fp;
	}
	public void setFl_invio_fp(java.lang.String fl_invio_fp) {
		this.fl_invio_fp = fl_invio_fp;
	}

	public String getAllega_conflitto_interesse() {
		return allega_conflitto_interesse;
	}
	public void setAllega_conflitto_interesse(String allega_conflitto_interesse) {
		this.allega_conflitto_interesse = allega_conflitto_interesse;
	}
}