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
* Date 07/02/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import java.util.Hashtable;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_sospesi_riscontriBulk extends OggettoBulk implements Persistent {
	public V_cons_sospesi_riscontriBulk() {
		super();
	}
	
	
//	  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//	  CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//	  TI_ENTRATA_SPESA CHAR(1)
	private java.lang.String ti_entrata_spesa;
 
//	  TI_SOSPESO_RISCONTRO CHAR(1)
	private java.lang.String ti_sospeso_riscontro;
 
//	  CD_SOSPESO VARCHAR(24)
	private java.lang.String cd_sospeso;
 
//	  IM_SOSPESO DECIMAL(15,2)
	private java.math.BigDecimal im_sospeso;
 
//	  IM_ASSOCIATO DECIMAL(15,2)
	private java.math.BigDecimal im_associato;
 
//	  DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//	  TI_CC_BI CHAR(1)
	//private java.lang.String ti_cc_bi;
	
	private static final java.util.Dictionary ti_cc_biKeys = new it.cnr.jada.util.OrderedHashtable();
	
	private String ti_cc_bi;
	
	public static final String TIPO_CC = "C";
	public static final String TIPO_BANCA_ITALIA = "B";
	
			
		static {
			ti_cc_biKeys.put(TIPO_CC,"C/C");
			ti_cc_biKeys.put(TIPO_BANCA_ITALIA,"Banca d'Italia");
		}
		
 
//	  CAUSALE VARCHAR(200)
	private java.lang.String causale;
 
//	  DS_ANAGRAFICO VARCHAR(200)
	private java.lang.String ds_anagrafico;
 
//	  PG_MANDATO_REVERSALE DECIMAL(10,0)
	private java.lang.Long pg_mandato_reversale;
 
	
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getTi_entrata_spesa () {
		return ti_entrata_spesa;
	}
	public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa)  {
		this.ti_entrata_spesa=ti_entrata_spesa;
	}
	public java.lang.String getTi_sospeso_riscontro () {
		return ti_sospeso_riscontro;
	}
	public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro)  {
		this.ti_sospeso_riscontro=ti_sospeso_riscontro;
	}
	public java.lang.String getCd_sospeso () {
		return cd_sospeso;
	}
	public void setCd_sospeso(java.lang.String cd_sospeso)  {
		this.cd_sospeso=cd_sospeso;
	}
	public java.math.BigDecimal getIm_sospeso () {
		return im_sospeso;
	}
	public void setIm_sospeso(java.math.BigDecimal im_sospeso)  {
		this.im_sospeso=im_sospeso;
	}
	public java.math.BigDecimal getIm_associato () {
		return im_associato;
	}
	public void setIm_associato(java.math.BigDecimal im_associato)  {
		this.im_associato=im_associato;
	}
	public java.sql.Timestamp getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
    public String getTi_cc_bi () {
		return ti_cc_bi;
	}
	public void setTi_cc_bi(String string)  {
		ti_cc_bi=string;
	}
	
	public final java.util.Dictionary getTi_cc_biKeys() {
				return ti_cc_biKeys;
		}
		
	public java.lang.String getCausale () {
		return causale;
	}
	public void setCausale(java.lang.String causale)  {
		this.causale=causale;
	}
	public java.lang.String getDs_anagrafico () {
		return ds_anagrafico;
	}
	public void setDs_anagrafico(java.lang.String ds_anagrafico)  {
		this.ds_anagrafico=ds_anagrafico;
	}
	public java.lang.Long getPg_mandato_reversale () {
		return pg_mandato_reversale;
	}
	public void setPg_mandato_reversale(java.lang.Long pg_mandato_reversale)  {
		this.pg_mandato_reversale=pg_mandato_reversale;
	}
}