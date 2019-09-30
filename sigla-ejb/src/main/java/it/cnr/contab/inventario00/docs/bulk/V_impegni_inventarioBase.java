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
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_impegni_inventarioBase extends OggettoBulk implements Persistent {
 
//  ESERCIZIO DECIMAL(22,0)
	private java.lang.Long esercizio;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

//  CD_unita_organizzativa VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
	
//    ESERCIZIO_ORIGINALE DECIMAL(22,0)
	private java.lang.Long esercizio_originale;
 
//    PG_OBBLIGAZIONE DECIMAL(22,0)
	private java.lang.Long pg_obbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(22,0)
	private java.lang.Long pg_obbligazione_scadenzario;
 
//    DS_OBBLIGAZIONE VARCHAR(300)
	private java.lang.String ds_obbligazione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    IMPEGNATO DECIMAL(22,0)
	private  java.math.BigDecimal impegnato;
 
//    ASSOCIATO DECIMAL(22,0)
	private  java.math.BigDecimal associato;
 
	public V_impegni_inventarioBase() {
		super();
	}
	public java.lang.Long getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Long esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Long getEsercizio_originale () {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Long esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
	}
	public java.lang.Long getPg_obbligazione () {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione_scadenzario () {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario)  {
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
	}
	public java.lang.String getDs_obbligazione () {
		return ds_obbligazione;
	}
	public void setDs_obbligazione(java.lang.String ds_obbligazione)  {
		this.ds_obbligazione=ds_obbligazione;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce () {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	public java.math.BigDecimal getAssociato() {
		return associato;
	}
	public void setAssociato(java.math.BigDecimal associato) {
		this.associato = associato;
	}
	public java.math.BigDecimal getImpegnato() {
		return impegnato;
	}
	public void setImpegnato(java.math.BigDecimal impegnato) {
		this.impegnato = impegnato;
	}
	
}