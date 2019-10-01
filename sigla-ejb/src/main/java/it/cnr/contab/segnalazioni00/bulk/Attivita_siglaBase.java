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
 * Date 20/05/2009
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Attivita_siglaBase extends Attivita_siglaKey implements Keyed {
//    PROTOCOLLO VARCHAR(20)
	private java.lang.String protocollo;
 
//    ID_HELP DECIMAL(10,0)
	private java.lang.Long id_help;
 
//    DT_ATTIVITA TIMESTAMP(7)
	private java.sql.Timestamp dt_attivita;
 
//    RICHIEDENTE VARCHAR(100)
	private java.lang.String richiedente;
 
//    DS_ATTIVITA VARCHAR(500)
	private java.lang.String ds_attivita;
 
//    NOTE VARCHAR(200)
	private java.lang.String note;
 
//    PRIORITA VARCHAR(1)
	private java.lang.String priorita;
 
//    NOTE_PRIORITA VARCHAR(200)
	private java.lang.String note_priorita;
 
//    DT_CONSEGNA TIMESTAMP(7)
	private java.sql.Timestamp dt_consegna;
 
//    STATO VARCHAR(2)
	private java.lang.String stato;
 
//    TIPO_ATTIVITA VARCHAR(1)
	private java.lang.String tipo_attivita;
 
//    CD_REDATTORE DECIMAL(8,0)
	private java.lang.Integer cd_redattore;
 
//    REFERENTI VARCHAR(100)
	private java.lang.String referenti;
 
//    DT_INIZIO_ATTIVITA TIMESTAMP(7)
	private java.sql.Timestamp dt_inizio_attivita;
 
//    DT_FINE_ATTIVITA TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_attivita;
 
//    TEMPO_ANALISI VARCHAR(100)
	private java.lang.String tempo_analisi;
 
//    TEMPO_SVILUPPO VARCHAR(100)
	private java.lang.String tempo_sviluppo;
 
//    CD_RESPONSABILE_1 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_1;
 
//    CD_RESPONSABILE_2 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_2;
 
//    CD_RESPONSABILE_3 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_3;
 
//    CD_RESPONSABILE_4 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_4;
 
//    CD_RESPONSABILE_5 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_5;
 
//    CD_RESPONSABILE_6 DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_6;
 
//    FL_BUG_APERTO CHAR(1)
	private java.lang.Boolean fl_bug_aperto;
 
//    ID_BUGZILLA DECIMAL(10,0)
	private java.lang.Long id_bugzilla;
 
	public Attivita_siglaBase() {
		super();
	}
	public Attivita_siglaBase(java.lang.Long pg_attivita, java.lang.Integer esercizio) {
		super(pg_attivita, esercizio);
	}
	public java.lang.String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(java.lang.String protocollo)  {
		this.protocollo=protocollo;
	}
	public java.lang.Long getId_help() {
		return id_help;
	}
	public void setId_help(java.lang.Long id_help)  {
		this.id_help=id_help;
	}
	public java.sql.Timestamp getDt_attivita() {
		return dt_attivita;
	}
	public void setDt_attivita(java.sql.Timestamp dt_attivita)  {
		this.dt_attivita=dt_attivita;
	}
	public java.lang.String getRichiedente() {
		return richiedente;
	}
	public void setRichiedente(java.lang.String richiedente)  {
		this.richiedente=richiedente;
	}
	public java.lang.String getDs_attivita() {
		return ds_attivita;
	}
	public void setDs_attivita(java.lang.String ds_attivita)  {
		this.ds_attivita=ds_attivita;
	}
	public java.lang.String getNote() {
		return note;
	}
	public void setNote(java.lang.String note)  {
		this.note=note;
	}
	public java.lang.String getPriorita() {
		return priorita;
	}
	public void setPriorita(java.lang.String priorita)  {
		this.priorita=priorita;
	}
	public java.lang.String getNote_priorita() {
		return note_priorita;
	}
	public void setNote_priorita(java.lang.String note_priorita)  {
		this.note_priorita=note_priorita;
	}
	public java.sql.Timestamp getDt_consegna() {
		return dt_consegna;
	}
	public void setDt_consegna(java.sql.Timestamp dt_consegna)  {
		this.dt_consegna=dt_consegna;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getTipo_attivita() {
		return tipo_attivita;
	}
	public void setTipo_attivita(java.lang.String tipo_attivita)  {
		this.tipo_attivita=tipo_attivita;
	}
	public java.lang.Integer getCd_redattore() {
		return cd_redattore;
	}
	public void setCd_redattore(java.lang.Integer cd_redattore)  {
		this.cd_redattore=cd_redattore;
	}
	public java.lang.String getReferenti() {
		return referenti;
	}
	public void setReferenti(java.lang.String referenti)  {
		this.referenti=referenti;
	}
	public java.sql.Timestamp getDt_inizio_attivita() {
		return dt_inizio_attivita;
	}
	public void setDt_inizio_attivita(java.sql.Timestamp dt_inizio_attivita)  {
		this.dt_inizio_attivita=dt_inizio_attivita;
	}
	public java.sql.Timestamp getDt_fine_attivita() {
		return dt_fine_attivita;
	}
	public void setDt_fine_attivita(java.sql.Timestamp dt_fine_attivita)  {
		this.dt_fine_attivita=dt_fine_attivita;
	}
	public java.lang.String getTempo_analisi() {
		return tempo_analisi;
	}
	public void setTempo_analisi(java.lang.String tempo_analisi)  {
		this.tempo_analisi=tempo_analisi;
	}
	public java.lang.String getTempo_sviluppo() {
		return tempo_sviluppo;
	}
	public void setTempo_sviluppo(java.lang.String tempo_sviluppo)  {
		this.tempo_sviluppo=tempo_sviluppo;
	}
	public java.lang.Integer getCd_responsabile_1() {
		return cd_responsabile_1;
	}
	public void setCd_responsabile_1(java.lang.Integer cd_responsabile_1)  {
		this.cd_responsabile_1=cd_responsabile_1;
	}
	public java.lang.Integer getCd_responsabile_2() {
		return cd_responsabile_2;
	}
	public void setCd_responsabile_2(java.lang.Integer cd_responsabile_2)  {
		this.cd_responsabile_2=cd_responsabile_2;
	}
	public java.lang.Integer getCd_responsabile_3() {
		return cd_responsabile_3;
	}
	public void setCd_responsabile_3(java.lang.Integer cd_responsabile_3)  {
		this.cd_responsabile_3=cd_responsabile_3;
	}
	public java.lang.Integer getCd_responsabile_4() {
		return cd_responsabile_4;
	}
	public void setCd_responsabile_4(java.lang.Integer cd_responsabile_4)  {
		this.cd_responsabile_4=cd_responsabile_4;
	}
	public java.lang.Integer getCd_responsabile_5() {
		return cd_responsabile_5;
	}
	public void setCd_responsabile_5(java.lang.Integer cd_responsabile_5)  {
		this.cd_responsabile_5=cd_responsabile_5;
	}
	public java.lang.Integer getCd_responsabile_6() {
		return cd_responsabile_6;
	}
	public void setCd_responsabile_6(java.lang.Integer cd_responsabile_6)  {
		this.cd_responsabile_6=cd_responsabile_6;
	}
	public java.lang.Boolean getFl_bug_aperto() {
		return fl_bug_aperto;
	}
	public void setFl_bug_aperto(java.lang.Boolean fl_bug_aperto)  {
		this.fl_bug_aperto=fl_bug_aperto;
	}
	public java.lang.Long getId_bugzilla() {
		return id_bugzilla;
	}
	public void setId_bugzilla(java.lang.Long id_bugzilla)  {
		this.id_bugzilla=id_bugzilla;
	}
}