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
* Date 16/06/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.math.BigDecimal;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_obbl_riportabiliBulk extends OggettoBulk implements Persistent {
//	  CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//	  CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//	  CD_CDS VARCHAR(30) NOT NULL
    private java.lang.String cd_cds;

//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
    private java.lang.String cd_unita_organizzativa;

//	  CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//	  ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_originale;

//	  PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;
 
//	  PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione_scadenzario;
 
//	  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//	  STATO_OBBLIGAZIONE CHAR(1) NOT NULL
	private java.lang.String stato_obbligazione;
 
//	  RIPORTATO CHAR(1) NOT NULL
	private java.lang.String riportato;
 
//	  DS_OBBLIGAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_obbligazione;
 
//	  IM_SCADVOCE DECIMAL(22,0)
	private BigDecimal im_scadvoce;
 
//	  CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
	public V_cons_obbl_riportabiliBulk() {
		super();
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.Integer getEsercizio_originale () {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
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
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getStato_obbligazione () {
		return stato_obbligazione;
	}
	public void setStato_obbligazione(java.lang.String stato_obbligazione)  {
		this.stato_obbligazione=stato_obbligazione;
	}
	public java.lang.String getRiportato () {
		return riportato;
	}
	public void setRiportato(java.lang.String riportato)  {
		this.riportato=riportato;
	}
	public java.lang.String getDs_obbligazione () {
		return ds_obbligazione;
	}
	public void setDs_obbligazione(java.lang.String ds_obbligazione)  {
		this.ds_obbligazione=ds_obbligazione;
	}
	public BigDecimal getIm_scadvoce () {
		return im_scadvoce;
	}
	public void setIm_scadvoce(BigDecimal im_scadvoce)  {
		this.im_scadvoce=im_scadvoce;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
}