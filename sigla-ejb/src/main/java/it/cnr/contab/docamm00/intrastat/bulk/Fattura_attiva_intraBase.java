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
 * Date 23/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.persistency.Keyed;
public class Fattura_attiva_intraBase extends Fattura_attiva_intraKey implements Keyed {
//    AMMONTARE_EURO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal ammontare_euro;
 
//    ID_NATURA_TRANSAZIONE DECIMAL(22,0)
	private java.lang.Long id_natura_transazione;
 
//    ID_NOMENCLATURA_COMBINATA DECIMAL(22,0)
	private java.lang.Long id_nomenclatura_combinata;
 
//    MASSA_NETTA DECIMAL(15,2)
	private java.math.BigDecimal massa_netta;
 
//    UNITA_SUPPLEMENTARI VARCHAR(30)
	private java.lang.Long unita_supplementari;
 
//    VALORE_STATISTICO DECIMAL(15,2)
	private java.math.BigDecimal valore_statistico;
 
//    ESERCIZIO_COND_CONSEGNA DECIMAL(4,0)
	private java.lang.Integer esercizio_cond_consegna;
 
//    CD_INCOTERM VARCHAR(10)
	private java.lang.String cd_incoterm;
 
//    ESERCIZIO_MOD_TRASPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_mod_trasporto;
 
//    CD_MODALITA_TRASPORTO VARCHAR(5)
	private java.lang.String cd_modalita_trasporto;
 
//    PG_NAZIONE_DESTINAZIONE DECIMAL(10,0)
	private java.lang.Long pg_nazione_destinazione;
 
//    CD_PROVINCIA_ORIGINE VARCHAR(10)
	private java.lang.String cd_provincia_origine;
 
//    DS_BENE VARCHAR(300)
	private java.lang.String ds_bene;
 
//    ID_SERVIZIO DECIMAL(22,0)
	private java.lang.Long id_cpa;
 
//    ESERCIZIO_MOD_INCASSO DECIMAL(4,0)
	private java.lang.Integer esercizio_mod_incasso;
 
//    CD_MODALITA_INCASSO CHAR(1)
	private java.lang.String cd_modalita_incasso;
 
//    ESERCIZIO_MOD_EROGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_mod_erogazione;
 
//    CD_MODALITA_EROGAZIONE CHAR(1)
	private java.lang.String cd_modalita_erogazione;
 
	private java.lang.String nr_protocollo;
	private java.lang.Integer nr_progressivo;
	
	private java.lang.Boolean fl_inviato;
	
	public Fattura_attiva_intraBase() {
		super();
	}
	public Fattura_attiva_intraBase(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_fattura_attiva, java.lang.Long pg_riga_intra) {
		super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_attiva, pg_riga_intra);
	}
	public java.math.BigDecimal getAmmontare_euro() {
		return ammontare_euro;
	}
	public void setAmmontare_euro(java.math.BigDecimal ammontare_euro)  {
		this.ammontare_euro=ammontare_euro;
	}
	public java.lang.Long getId_natura_transazione() {
		return id_natura_transazione;
	}
	public void setId_natura_transazione(java.lang.Long id_natura_transazione)  {
		this.id_natura_transazione=id_natura_transazione;
	}
	public java.lang.Long getId_nomenclatura_combinata() {
		return id_nomenclatura_combinata;
	}
	public void setId_nomenclatura_combinata(java.lang.Long id_nomenclatura_combinata)  {
		this.id_nomenclatura_combinata=id_nomenclatura_combinata;
	}
	public java.math.BigDecimal getMassa_netta() {
		return massa_netta;
	}
	public void setMassa_netta(java.math.BigDecimal massa_netta)  {
		this.massa_netta=massa_netta;
	}
	public java.lang.Long getUnita_supplementari() {
		return unita_supplementari;
	}
	public void setUnita_supplementari(java.lang.Long unita_supplementari)  {
		this.unita_supplementari=unita_supplementari;
	}
	public java.math.BigDecimal getValore_statistico() {
		return valore_statistico;
	}
	public void setValore_statistico(java.math.BigDecimal valore_statistico)  {
		this.valore_statistico=valore_statistico;
	}
	public java.lang.Integer getEsercizio_cond_consegna() {
		return esercizio_cond_consegna;
	}
	public void setEsercizio_cond_consegna(java.lang.Integer esercizio_cond_consegna)  {
		this.esercizio_cond_consegna=esercizio_cond_consegna;
	}
	public java.lang.String getCd_incoterm() {
		return cd_incoterm;
	}
	public void setCd_incoterm(java.lang.String cd_incoterm)  {
		this.cd_incoterm=cd_incoterm;
	}
	public java.lang.Integer getEsercizio_mod_trasporto() {
		return esercizio_mod_trasporto;
	}
	public void setEsercizio_mod_trasporto(java.lang.Integer esercizio_mod_trasporto)  {
		this.esercizio_mod_trasporto=esercizio_mod_trasporto;
	}
	public java.lang.String getCd_modalita_trasporto() {
		return cd_modalita_trasporto;
	}
	public void setCd_modalita_trasporto(java.lang.String cd_modalita_trasporto)  {
		this.cd_modalita_trasporto=cd_modalita_trasporto;
	}
	public java.lang.Long getPg_nazione_destinazione() {
		return pg_nazione_destinazione;
	}
	public void setPg_nazione_destinazione(java.lang.Long pg_nazione_destinazione)  {
		this.pg_nazione_destinazione=pg_nazione_destinazione;
	}
	public java.lang.String getCd_provincia_origine() {
		return cd_provincia_origine;
	}
	public void setCd_provincia_origine(java.lang.String cd_provincia_origine)  {
		this.cd_provincia_origine=cd_provincia_origine;
	}
	public java.lang.String getDs_bene() {
		return ds_bene;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	public java.lang.Long getId_cpa() {
		return id_cpa;
	}
	public void setId_cpa(java.lang.Long id_cpa)  {
		this.id_cpa=id_cpa;
	}
	public java.lang.Integer getEsercizio_mod_incasso() {
		return esercizio_mod_incasso;
	}
	public void setEsercizio_mod_incasso(java.lang.Integer esercizio_mod_incasso)  {
		this.esercizio_mod_incasso=esercizio_mod_incasso;
	}
	public java.lang.String getCd_modalita_incasso() {
		return cd_modalita_incasso;
	}
	public void setCd_modalita_incasso(java.lang.String cd_modalita_incasso)  {
		this.cd_modalita_incasso=cd_modalita_incasso;
	}
	public java.lang.Integer getEsercizio_mod_erogazione() {
		return esercizio_mod_erogazione;
	}
	public void setEsercizio_mod_erogazione(java.lang.Integer esercizio_mod_erogazione)  {
		this.esercizio_mod_erogazione=esercizio_mod_erogazione;
	}
	public java.lang.String getCd_modalita_erogazione() {
		return cd_modalita_erogazione;
	}
	public void setCd_modalita_erogazione(java.lang.String cd_modalita_erogazione)  {
		this.cd_modalita_erogazione=cd_modalita_erogazione;
	}
	public java.lang.Boolean getFl_inviato() {
		return fl_inviato;
	}
	public void setFl_inviato(java.lang.Boolean fl_inviato) {
		this.fl_inviato = fl_inviato;
	}
	public java.lang.String getNr_protocollo() {
		return nr_protocollo;
	}
	public void setNr_protocollo(java.lang.String nr_protocollo) {
		this.nr_protocollo = nr_protocollo;
	}
	public java.lang.Integer getNr_progressivo() {
		return nr_progressivo;
	}
	public void setNr_progressivo(java.lang.Integer nr_progressivo) {
		this.nr_progressivo = nr_progressivo;
	}
}