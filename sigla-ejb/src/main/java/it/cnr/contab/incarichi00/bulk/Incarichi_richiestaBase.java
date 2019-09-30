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
 * Date 23/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_richiestaBase extends Incarichi_richiestaKey implements Keyed {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    CD_TEMATICA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_tematica_attivita;

//    ATTIVITA VARCHAR(800) NOT NULL
	private java.lang.String attivita;
 
//    ATTIVITA_BREVE VARCHAR(100) NOT NULL
	private java.lang.String attivita_breve;
 
//    COMPETENZE VARCHAR(800)
	private java.lang.String competenze;
 
//    DURATA VARCHAR(200) NOT NULL
	private java.lang.String durata;
 
//    SEDE_LAVORO VARCHAR(200) NOT NULL
	private java.lang.String sede_lavoro;
 
//    NOTE VARCHAR(500)
	private java.lang.String note;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;
 
//    DATA_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp data_pubblicazione;
 
//    DATA_FINE_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp data_fine_pubblicazione;
 
//    DATA_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp data_scadenza;
 
//    PERSONALE_INTERNO VARCHAR(2)
	private java.lang.String personale_interno;
 
//    EMAIL_RISPOSTE VARCHAR(100) NOT NULL
	private java.lang.String email_risposte;
 
//    NR_RISORSE_DA_TROVARE DECIMAL(4,0)
	private java.lang.Integer nr_risorse_da_trovare;

//    NR_RISORSE_TROVATE_SI DECIMAL(4,0)
	private java.lang.Integer nr_risorse_trovate_si;

//    NR_RISORSE_TROVATE_NO DECIMAL(4,0)
	private java.lang.Integer nr_risorse_trovate_no;

//    NR_RISORSE_TROVATE_NA DECIMAL(4,0)
	private java.lang.Integer nr_risorse_trovate_na;

	public Incarichi_richiestaBase() {
		super();
	}
	public Incarichi_richiestaBase(java.lang.Integer esercizio, java.lang.Long pg_richiesta) {
		super(esercizio, pg_richiesta);
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
	public java.lang.String getAttivita() {
		return attivita;
	}
	public void setAttivita(java.lang.String attivita)  {
		this.attivita=attivita;
	}
	public java.lang.String getAttivita_breve() {
		return attivita_breve;
	}
	public void setAttivita_breve(java.lang.String attivita_breve)  {
		this.attivita_breve=attivita_breve;
	}
	public java.lang.String getCompetenze() {
		return competenze;
	}
	public void setCompetenze(java.lang.String competenze)  {
		this.competenze=competenze;
	}
	public java.lang.String getDurata() {
		return durata;
	}
	public void setDurata(java.lang.String durata)  {
		this.durata=durata;
	}
	public java.lang.String getSede_lavoro() {
		return sede_lavoro;
	}
	public void setSede_lavoro(java.lang.String sede_lavoro)  {
		this.sede_lavoro=sede_lavoro;
	}
	public java.lang.String getNote() {
		return note;
	}
	public void setNote(java.lang.String note)  {
		this.note=note;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.sql.Timestamp getData_pubblicazione() {
		return data_pubblicazione;
	}
	public void setData_pubblicazione(java.sql.Timestamp data_pubblicazione)  {
		this.data_pubblicazione=data_pubblicazione;
	}
	public java.sql.Timestamp getData_fine_pubblicazione() {
		return data_fine_pubblicazione;
	}
	public void setData_fine_pubblicazione(java.sql.Timestamp data_fine_pubblicazione)  {
		this.data_fine_pubblicazione=data_fine_pubblicazione;
	}
	public java.sql.Timestamp getData_scadenza() {
		return data_scadenza;
	}
	public void setData_scadenza(java.sql.Timestamp data_scadenza)  {
		this.data_scadenza=data_scadenza;
	}
	public java.lang.String getPersonale_interno() {
		return personale_interno;
	}
	public void setPersonale_interno(java.lang.String personale_interno)  {
		this.personale_interno=personale_interno;
	}
	public java.lang.String getEmail_risposte() {
		return email_risposte;
	}
	public void setEmail_risposte(java.lang.String email_risposte)  {
		this.email_risposte=email_risposte;
	}
	public java.lang.String getCd_tematica_attivita() {
		return cd_tematica_attivita;
	}
	public void setCd_tematica_attivita(java.lang.String cd_tematica_attivita) {
		this.cd_tematica_attivita = cd_tematica_attivita;
	}
	public java.lang.Integer getNr_risorse_da_trovare() {
		return nr_risorse_da_trovare;
	}
	public void setNr_risorse_da_trovare(java.lang.Integer nr_risorse_da_trovare) {
		this.nr_risorse_da_trovare = nr_risorse_da_trovare;
	}
	public java.lang.Integer getNr_risorse_trovate_si() {
		return nr_risorse_trovate_si;
	}
	public void setNr_risorse_trovate_si(java.lang.Integer nr_risorse_trovate_si) {
		this.nr_risorse_trovate_si = nr_risorse_trovate_si;
	}
	public java.lang.Integer getNr_risorse_trovate_no() {
		return nr_risorse_trovate_no;
	}
	public void setNr_risorse_trovate_no(java.lang.Integer nr_risorse_trovate_no) {
		this.nr_risorse_trovate_no = nr_risorse_trovate_no;
	}
	public java.lang.Integer getNr_risorse_trovate_na() {
		return nr_risorse_trovate_na;
	}
	public void setNr_risorse_trovate_na(java.lang.Integer nr_risorse_trovate_na) {
		this.nr_risorse_trovate_na = nr_risorse_trovate_na;
	}
}