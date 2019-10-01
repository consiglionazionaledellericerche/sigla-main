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
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class BonusBase extends BonusKey implements Keyed {
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;
 
//    ESERCIZIO_IMPOSTA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_imposta;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_registrazione;
 
//    CODICE_FISCALE VARCHAR(20) NOT NULL
	private java.lang.String codice_fiscale;
 
//    COGNOME VARCHAR(50) NOT NULL
	private java.lang.String cognome;
 
//    NOME VARCHAR(50) NOT NULL
	private java.lang.String nome;
 
//    TI_SESSO CHAR(1) NOT NULL
	private java.lang.String ti_sesso;
 
//    DT_NASCITA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_nascita;
 
//    PG_COMUNE_NASCITA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_comune_nascita;
 
//    CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;
 
//    CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;
 
//    IM_REDDITO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito;
 
//    IM_REDDITO_NUCLEO_F DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_nucleo_f;
 
//    IM_BONUS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_bonus;
 
//    CD_CONDIZIONE VARCHAR(5) NOT NULL
	private java.lang.String cd_condizione;
 
//    DT_RICHIESTA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_richiesta;
 
//    FL_TRASMESSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_trasmesso;
	
	
	public BonusBase() {
		super();
	}
	public BonusBase(java.lang.Integer esercizio, java.lang.Long pg_bonus) {
		super(esercizio, pg_bonus);
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.Integer getEsercizio_imposta() {
		return esercizio_imposta;
	}
	public void setEsercizio_imposta(java.lang.Integer esercizio_imposta)  {
		this.esercizio_imposta=esercizio_imposta;
	}
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale)  {
		this.codice_fiscale=codice_fiscale;
	}
	public java.lang.String getCognome() {
		return cognome;
	}
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	public java.lang.String getNome() {
		return nome;
	}
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	public java.lang.String getTi_sesso() {
		return ti_sesso;
	}
	public void setTi_sesso(java.lang.String ti_sesso)  {
		this.ti_sesso=ti_sesso;
	}
	public java.sql.Timestamp getDt_nascita() {
		return dt_nascita;
	}
	public void setDt_nascita(java.sql.Timestamp dt_nascita)  {
		this.dt_nascita=dt_nascita;
	}
	public java.lang.Long getPg_comune_nascita() {
		return pg_comune_nascita;
	}
	public void setPg_comune_nascita(java.lang.Long pg_comune_nascita)  {
		this.pg_comune_nascita=pg_comune_nascita;
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
	public java.math.BigDecimal getIm_reddito() {
		return im_reddito;
	}
	public void setIm_reddito(java.math.BigDecimal im_reddito)  {
		this.im_reddito=im_reddito;
	}
	public java.math.BigDecimal getIm_reddito_nucleo_f() {
		return im_reddito_nucleo_f;
	}
	public void setIm_reddito_nucleo_f(java.math.BigDecimal im_reddito_nucleo_f)  {
		this.im_reddito_nucleo_f=im_reddito_nucleo_f;
	}
	public java.math.BigDecimal getIm_bonus() {
		return im_bonus;
	}
	public void setIm_bonus(java.math.BigDecimal im_bonus)  {
		this.im_bonus=im_bonus;
	}
	public java.lang.String getCd_condizione() {
		return cd_condizione;
	}
	public void setCd_condizione(java.lang.String cd_condizione)  {
		this.cd_condizione=cd_condizione;
	}
	public java.sql.Timestamp getDt_richiesta() {
		return dt_richiesta;
	}
	public void setDt_richiesta(java.sql.Timestamp dt_richiesta)  {
		this.dt_richiesta=dt_richiesta;
	}
	public java.lang.Boolean getFl_trasmesso() {
		return fl_trasmesso;
	}
	public void setFl_trasmesso(java.lang.Boolean fl_trasmesso)  {
		this.fl_trasmesso=fl_trasmesso;
	}
}