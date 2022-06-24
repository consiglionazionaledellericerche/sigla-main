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
 * Date 30/11/2009
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.persistency.Persistent;
public class V_cons_sospesiBulk extends OggettoBulk implements Persistent{
	
//  ESERCIZIO DECIMAL(4,0)
	private Integer esercizio;
 
//    TI_ENTRATA_SPESA CHAR(1)
	private String ti_entrata_spesa;
 
//    CD_CDS VARCHAR(30)
	private String cd_cds;
 
//    CD_CDS_ORIGINE VARCHAR(30)
	private String cd_cds_origine;
 
//    CD_SOSPESO VARCHAR(24)
	private String cd_sospeso;
 
//    LIVELLO DECIMAL(22,0)
	private Long livello;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    DS_ANAGRAFICO VARCHAR(200)
	private String ds_anagrafico;
 
//    CAUSALE VARCHAR(300)
	private String causale;
 
//    TI_CC_BI CHAR(1)
	private String ti_cc_bi;
 
//    DES_TI_CC_BI VARCHAR(14)
	private String des_ti_cc_bi;
 
//    STATO_VALIDITA VARCHAR(9)
	private String stato_validita;
 
//    DT_STORNO TIMESTAMP(7)
	private java.sql.Timestamp dt_storno;
 
//    IM_SOSPESO DECIMAL(22,0)
	private java.math.BigDecimal im_sospeso;
 
//    IM_ASSOCIATO DECIMAL(22,0)
	private java.math.BigDecimal im_associato;
 
//    IM_DA_ASSOCIARE DECIMAL(22,0)
	private java.math.BigDecimal im_da_associare;
 
//    DS_STATO_SOSPESO VARCHAR(35)
	private String ds_stato_sospeso;
	private String cd_avviso_pagopa;

	public String getCd_avviso_pagopa() {
		return cd_avviso_pagopa;
	}

	public void setCd_avviso_pagopa(String cd_avviso_pagopa) {
		this.cd_avviso_pagopa = cd_avviso_pagopa;
	}

	//    IM_ASS_MOD_1210 DECIMAL(22,0)
	private java.math.BigDecimal im_ass_mod_1210;
 
//    CD_SOSPESO_PADRE VARCHAR(24)
	private String cd_sospeso_padre;
 
//    PG_MAN_REV DECIMAL(22,0)
	private Long pg_man_rev;
 
	public V_cons_sospesiBulk() {
		super();
	}
	
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public String getTi_entrata_spesa() {
		return ti_entrata_spesa;
	}
	public void setTi_entrata_spesa(String ti_entrata_spesa)  {
		this.ti_entrata_spesa=ti_entrata_spesa;
	}
	public String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public String getCd_cds_origine() {
		return cd_cds_origine;
	}
	public void setCd_cds_origine(String cd_cds_origine)  {
		this.cd_cds_origine=cd_cds_origine;
	}
	public String getCd_sospeso() {
		return cd_sospeso;
	}
	public void setCd_sospeso(String cd_sospeso)  {
		this.cd_sospeso=cd_sospeso;
	}
	public Long getLivello() {
		return livello;
	}
	public void setLivello(Long livello)  {
		this.livello=livello;
	}
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public String getDs_anagrafico() {
		return ds_anagrafico;
	}
	public void setDs_anagrafico(String ds_anagrafico)  {
		this.ds_anagrafico=ds_anagrafico;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale)  {
		this.causale=causale;
	}
	public String getTi_cc_bi() {
		return ti_cc_bi;
	}
	public void setTi_cc_bi(String ti_cc_bi)  {
		this.ti_cc_bi=ti_cc_bi;
	}
	public String getDes_ti_cc_bi() {
		return des_ti_cc_bi;
	}
	public void setDes_ti_cc_bi(String des_ti_cc_bi)  {
		this.des_ti_cc_bi=des_ti_cc_bi;
	}
	public String getStato_validita() {
		return stato_validita;
	}
	public void setStato_validita(String stato_validita)  {
		this.stato_validita=stato_validita;
	}
	public java.sql.Timestamp getDt_storno() {
		return dt_storno;
	}
	public void setDt_storno(java.sql.Timestamp dt_storno)  {
		this.dt_storno=dt_storno;
	}
	public java.math.BigDecimal getIm_sospeso() {
		return im_sospeso;
	}
	public void setIm_sospeso(java.math.BigDecimal im_sospeso)  {
		this.im_sospeso=im_sospeso;
	}
	public java.math.BigDecimal getIm_associato() {
		return im_associato;
	}
	public void setIm_associato(java.math.BigDecimal im_associato)  {
		this.im_associato=im_associato;
	}
	public java.math.BigDecimal getIm_da_associare() {
		return im_da_associare;
	}
	public void setIm_da_associare(java.math.BigDecimal im_da_associare)  {
		this.im_da_associare=im_da_associare;
	}
	public String getDs_stato_sospeso() {
		return ds_stato_sospeso;
	}
	public void setDs_stato_sospeso(String ds_stato_sospeso)  {
		this.ds_stato_sospeso=ds_stato_sospeso;
	}
	public java.math.BigDecimal getIm_ass_mod_1210() {
		return im_ass_mod_1210;
	}
	public void setIm_ass_mod_1210(java.math.BigDecimal im_ass_mod_1210)  {
		this.im_ass_mod_1210=im_ass_mod_1210;
	}
	public String getCd_sospeso_padre() {
		return cd_sospeso_padre;
	}
	public void setCd_sospeso_padre(String cd_sospeso_padre)  {
		this.cd_sospeso_padre=cd_sospeso_padre;
	}
	public Long getPg_man_rev() {
		return pg_man_rev;
	}
	public void setPg_man_rev(Long pg_man_rev)  {
		this.pg_man_rev=pg_man_rev;
	}
}