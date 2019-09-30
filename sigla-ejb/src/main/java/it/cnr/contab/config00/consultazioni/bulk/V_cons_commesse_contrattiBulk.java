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
* Creted by Generator 1.0
* Date 18/04/2005
*/
package it.cnr.contab.config00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_cons_commesse_contrattiBulk extends OggettoBulk implements Persistent {
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(400)
	private java.lang.String ds_commessa;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(400)
	private java.lang.String ds_progetto;
 
//    ESERCIZIO_CONTRATTO_PADRE DECIMAL(4,0)
	private java.lang.Integer esercizio_contratto_padre;

//	STATO_CONTRATTO_PADRE VARCHAR(1)
	private java.lang.String stato_contratto_padre;
 
//    PG_CONTRATTO_PADRE DECIMAL(9,0)
	private java.lang.Long pg_contratto_padre;
 
//    ESERCIZIO_CONTRATTO DECIMAL(4,0)
	private java.lang.Integer esercizio_contratto;

//	STATO_CONTRATTO VARCHAR(1)
	private java.lang.String stato_contratto;
 
//    PG_CONTRATTO DECIMAL(9,0)
	private java.lang.Long pg_contratto;
 
//    OGGETTO_CONTRATTO VARCHAR(500)
	private java.lang.String oggetto_contratto;
 
//    TOTALE_ENTRATE DECIMAL(15,2)
	private java.math.BigDecimal totale_entrate;
 
//    TOTALE_SPESE DECIMAL(15,2)
	private java.math.BigDecimal totale_spese;
 
	public V_cons_commesse_contrattiBulk() {
		super();
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto () {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.Integer getEsercizio_contratto_padre () {
		return esercizio_contratto_padre;
	}
	public void setEsercizio_contratto_padre(java.lang.Integer esercizio_contratto_padre)  {
		this.esercizio_contratto_padre=esercizio_contratto_padre;
	}
	public java.lang.Long getPg_contratto_padre () {
		return pg_contratto_padre;
	}
	public void setPg_contratto_padre(java.lang.Long pg_contratto_padre)  {
		this.pg_contratto_padre=pg_contratto_padre;
	}
	public java.lang.Integer getEsercizio_contratto () {
		return esercizio_contratto;
	}
	public void setEsercizio_contratto(java.lang.Integer esercizio_contratto)  {
		this.esercizio_contratto=esercizio_contratto;
	}
	public java.lang.Long getPg_contratto () {
		return pg_contratto;
	}
	public void setPg_contratto(java.lang.Long pg_contratto)  {
		this.pg_contratto=pg_contratto;
	}
	public java.lang.String getOggetto_contratto () {
		return oggetto_contratto;
	}
	public void setOggetto_contratto(java.lang.String oggetto_contratto)  {
		this.oggetto_contratto=oggetto_contratto;
	}
	public java.math.BigDecimal getTotale_entrate () {
		return totale_entrate;
	}
	public void setTotale_entrate(java.math.BigDecimal totale_entrate)  {
		this.totale_entrate=totale_entrate;
	}
	public java.math.BigDecimal getTotale_spese () {
		return totale_spese;
	}
	public void setTotale_spese(java.math.BigDecimal totale_spese)  {
		this.totale_spese=totale_spese;
	}
	/**
	 * @return
	 */
	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}
	
	/**
	 * @return
	 */
	public java.lang.String getStato_contratto_padre() {
		return stato_contratto_padre;
	}
	
	/**
	 * @param string
	 */
	public void setStato_contratto(java.lang.String string) {
		stato_contratto = string;
	}
	
	/**
	 * @param string
	 */
	public void setStato_contratto_padre(java.lang.String string) {
		stato_contratto_padre = string;
	}

}