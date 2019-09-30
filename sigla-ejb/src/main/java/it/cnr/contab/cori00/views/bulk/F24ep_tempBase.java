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
 * Date 21/01/2008
 */
package it.cnr.contab.cori00.views.bulk;
import it.cnr.jada.persistency.Keyed;
public class F24ep_tempBase extends F24ep_tempKey implements Keyed {
//    CODICE_TRIBUTO VARCHAR(4) NOT NULL
	private java.lang.String codice_tributo;
 
//    CODICE_ENTE VARCHAR(4)
	private java.lang.String codice_ente;
 
//    MESE_RIF VARCHAR(4)
	private java.lang.String mese_rif;
 
//    ANNO_RIF VARCHAR(4)
	private java.lang.String anno_rif;
 
//    IMPORTO_DEBITO DECIMAL(15,2)
	private java.math.BigDecimal importo_debito;
 
//    PG_LIQUIDAZIONE DECIMAL(22,0)
	private java.lang.Long pg_liquidazione;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
	public F24ep_tempBase() {
		super();
	}
	public F24ep_tempBase(java.lang.Long prog) {
		super(prog);
	}
	public java.lang.String getCodice_tributo() {
		return codice_tributo;
	}
	public void setCodice_tributo(java.lang.String codice_tributo)  {
		this.codice_tributo=codice_tributo;
	}
	public java.lang.String getCodice_ente() {
		return codice_ente;
	}
	public void setCodice_ente(java.lang.String codice_ente)  {
		this.codice_ente=codice_ente;
	}
	public java.lang.String getMese_rif() {
		return mese_rif;
	}
	public void setMese_rif(java.lang.String mese_rif)  {
		this.mese_rif=mese_rif;
	}
	public java.lang.String getAnno_rif() {
		return anno_rif;
	}
	public void setAnno_rif(java.lang.String anno_rif)  {
		this.anno_rif=anno_rif;
	}
	public java.math.BigDecimal getImporto_debito() {
		return importo_debito;
	}
	public void setImporto_debito(java.math.BigDecimal importo_debito)  {
		this.importo_debito=importo_debito;
	}
	public java.lang.Long getPg_liquidazione() {
		return pg_liquidazione;
	}
	public void setPg_liquidazione(java.lang.Long pg_liquidazione)  {
		this.pg_liquidazione=pg_liquidazione;
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
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
}