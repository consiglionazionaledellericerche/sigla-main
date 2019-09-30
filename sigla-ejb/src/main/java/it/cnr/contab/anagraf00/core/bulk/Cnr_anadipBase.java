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
* Date 22/11/2005
*/
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Cnr_anadipBase extends Cnr_anadipKey implements Keyed {
//    NOMINATIVO VARCHAR(60) NOT NULL
	private java.lang.String nominativo;
 
//    SESSO VARCHAR(1)
	private java.lang.String sesso;
 
//    DIP_COD_FIS VARCHAR(16) NOT NULL
	private java.lang.String dip_cod_fis;
 
//    DATA_NASCITA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data_nascita;
 
//    COMUNE_NASC VARCHAR(40) NOT NULL
	private java.lang.String comune_nasc;
 
//    PROV_NASC VARCHAR(2)
	private java.lang.String prov_nasc;
 
//    IND_RESID VARCHAR(80) NOT NULL
	private java.lang.String ind_resid;
 
//    COM_RESID VARCHAR(40) NOT NULL
	private java.lang.String com_resid;
 
//    CAP_RESID VARCHAR(5) NOT NULL
	private java.lang.String cap_resid;
 
//    PRO_RESID VARCHAR(2) NOT NULL
	private java.lang.String pro_resid;
 
//    COD_COMUNE VARCHAR(4)
	private java.lang.String cod_comune;
 
//    STATO_CIVILE VARCHAR(1)
	private java.lang.String stato_civile;
 
//    DEC_STAT_CIV TIMESTAMP(7)
	private java.sql.Timestamp dec_stat_civ;
 
//    TIT_ONORIF VARCHAR(10)
	private java.lang.String tit_onorif;
 
//    DESC_TIT_ONORIF VARCHAR(35)
	private java.lang.String desc_tit_onorif;
 
//    TIT_STUDIO VARCHAR(2)
	private java.lang.String tit_studio;
 
//    DESC_TIT_STUDIO VARCHAR(35)
	private java.lang.String desc_tit_studio;
 
//    COD_SPEC VARCHAR(4)
	private java.lang.String cod_spec;
 
//    DESC_SPEC VARCHAR(35)
	private java.lang.String desc_spec;
 
//    ANNO_SPEC DECIMAL(4,0)
	private java.lang.Integer anno_spec;
 
//    LUOGO_SPEC VARCHAR(50)
	private java.lang.String luogo_spec;
 
//    ABILITAZIONE VARCHAR(1)
	private java.lang.String abilitazione;
 
//    ANNO_ABILITAZIONE DECIMAL(4,0)
	private java.lang.Integer anno_abilitazione;
 
//    FASCICOLO VARCHAR(10) NOT NULL
	private java.lang.String fascicolo;
 
//    DATA_ASSUNZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data_assunzione;
 
//    DATA_CESSAZIONE TIMESTAMP(7)
	private java.sql.Timestamp data_cessazione;
 
//    CAUSA_CESS VARCHAR(1)
	private java.lang.String causa_cess;
 
//    DESC_CAUSA_CESS VARCHAR(35)
	private java.lang.String desc_causa_cess;
 
//    RAPP_IMPIEGO VARCHAR(2) NOT NULL
	private java.lang.String rapp_impiego;
 
//    LIVELLO_1 VARCHAR(3) NOT NULL
	private java.lang.String livello_1;
 
//    LIVELLO_2 VARCHAR(3)
	private java.lang.String livello_2;
 
//    LIVELLO_3 VARCHAR(3)
	private java.lang.String livello_3;
 
//    PROFILO DECIMAL(4,0) NOT NULL
	private java.lang.Integer profilo;
 
//    CONTRATTO VARCHAR(7) NOT NULL
	private java.lang.String contratto;
 
//    DESC_PROFILO VARCHAR(100)
	private java.lang.String desc_profilo;
 
//    LIVPREC_1 VARCHAR(3)
	private java.lang.String livprec_1;
 
//    LIVPREC_2 VARCHAR(3)
	private java.lang.String livprec_2;
 
//    LIVPREC_3 VARCHAR(3)
	private java.lang.String livprec_3;
 
//    PROFILO_PREC DECIMAL(4,0)
	private java.lang.Integer profilo_prec;
 
//    DESC_PROF_PREC VARCHAR(100)
	private java.lang.String desc_prof_prec;
 
//    CONTRATTO_PREC VARCHAR(7)
	private java.lang.String contratto_prec;
 
//    PROFILO_PREC_FINE TIMESTAMP(7)
	private java.sql.Timestamp profilo_prec_fine;
 
//    TIPO_CONTRATTO VARCHAR(2)
	private java.lang.String tipo_contratto;
 
//    DESC_TIPO_CONTRATTO VARCHAR(35)
	private java.lang.String desc_tipo_contratto;
 
//    ENTE_PREV VARCHAR(2)
	private java.lang.String ente_prev;
 
//    UNITA_OPER VARCHAR(6) NOT NULL
	private java.lang.String unita_oper;
 
//    SEDE_LAVORO VARCHAR(6) NOT NULL
	private java.lang.String sede_lavoro;
 
//    DEC_INIZIO TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dec_inizio;
 
//    DEC_FINE TIMESTAMP(7)
	private java.sql.Timestamp dec_fine;
 
//    TIPO_PRES VARCHAR(7)
	private java.lang.String tipo_pres;
 
//    DESC_TIPO_PRES VARCHAR(35)
	private java.lang.String desc_tipo_pres;
 
//    TIT_AFFEREN VARCHAR(6)
	private java.lang.String tit_afferen;
 
//    MOD_PAG VARCHAR(1) NOT NULL
	private java.lang.String mod_pag;
 
//    ABI_PAG VARCHAR(5)
	private java.lang.String abi_pag;
 
//    CAB_PAG VARCHAR(5)
	private java.lang.String cab_pag;
 
//    NRC_PAG VARCHAR(12)
	private java.lang.String nrc_pag;
 
//    IMP_FISC DECIMAL(16,3) NOT NULL
	private java.math.BigDecimal imp_fisc;
 
//    IMP_PREV DECIMAL(16,3) NOT NULL
	private java.math.BigDecimal imp_prev;
 
//    IMP_IMPO1 DECIMAL(16,3) NOT NULL
	private java.math.BigDecimal imp_impo1;
 
//    IMP_IMPO2 DECIMAL(16,3) NOT NULL
	private java.math.BigDecimal imp_impo2;
 
//    SINDACATO VARCHAR(3)
	private java.lang.String sindacato;
 
//    ANZ_RAPP_PREC DECIMAL(5,3) NOT NULL
	private java.math.BigDecimal anz_rapp_prec;
 
//    ANZ_RISCATTI DECIMAL(5,3) NOT NULL
	private java.math.BigDecimal anz_riscatti;
 
//    TOT_ANNI DECIMAL(5,3) NOT NULL
	private java.math.BigDecimal tot_anni;
 
//    DATA_RIF TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data_rif;
 
//    ANNO_RIF DECIMAL(4,0) NOT NULL
	private java.lang.Integer anno_rif;
 
//    MESE_RIF DECIMAL(2,0) NOT NULL
	private java.lang.Integer mese_rif;
 
//    TIPO_ASS VARCHAR(2)
	private java.lang.String tipo_ass;
 
//    DES_TIPO_ASS VARCHAR(50)
	private java.lang.String des_tipo_ass;
 
//    RIF_CONC VARCHAR(15)
	private java.lang.String rif_conc;
 
//    TAB_COD_COMNAS VARCHAR(4)
	private java.lang.String tab_cod_comnas;
 
//    TAB_CAP_COMNAS VARCHAR(5)
	private java.lang.String tab_cap_comnas;
 
//    PARTIME VARCHAR(1)
	private java.lang.String partime;
 
//    ATT_RID_STIP DECIMAL(5,2)
	private java.math.BigDecimal att_rid_stip;
 
//    GIU_DEC_INIZIO TIMESTAMP(7)
	private java.sql.Timestamp giu_dec_inizio;
 
//    NAZ_NAS VARCHAR(3)
	private java.lang.String naz_nas;
 
//    NCAR_COGN DECIMAL(4,0)
	private java.lang.Integer ncar_cogn;
 
//    UO_TIT VARCHAR(6)
	private java.lang.String uo_tit;
 
//    FL_GESTIONE_MANUALE CHAR(1)
	private java.lang.Boolean fl_gestione_manuale;
 
	public Cnr_anadipBase() {
		super();
	}
	public Cnr_anadipBase(java.lang.Integer matricola) {
		super(matricola);
	}
	public java.lang.String getNominativo () {
		return nominativo;
	}
	public void setNominativo(java.lang.String nominativo)  {
		this.nominativo=nominativo;
	}
	public java.lang.String getSesso () {
		return sesso;
	}
	public void setSesso(java.lang.String sesso)  {
		this.sesso=sesso;
	}
	public java.lang.String getDip_cod_fis () {
		return dip_cod_fis;
	}
	public void setDip_cod_fis(java.lang.String dip_cod_fis)  {
		this.dip_cod_fis=dip_cod_fis;
	}
	public java.sql.Timestamp getData_nascita () {
		return data_nascita;
	}
	public void setData_nascita(java.sql.Timestamp data_nascita)  {
		this.data_nascita=data_nascita;
	}
	public java.lang.String getComune_nasc () {
		return comune_nasc;
	}
	public void setComune_nasc(java.lang.String comune_nasc)  {
		this.comune_nasc=comune_nasc;
	}
	public java.lang.String getProv_nasc () {
		return prov_nasc;
	}
	public void setProv_nasc(java.lang.String prov_nasc)  {
		this.prov_nasc=prov_nasc;
	}
	public java.lang.String getInd_resid () {
		return ind_resid;
	}
	public void setInd_resid(java.lang.String ind_resid)  {
		this.ind_resid=ind_resid;
	}
	public java.lang.String getCom_resid () {
		return com_resid;
	}
	public void setCom_resid(java.lang.String com_resid)  {
		this.com_resid=com_resid;
	}
	public java.lang.String getCap_resid () {
		return cap_resid;
	}
	public void setCap_resid(java.lang.String cap_resid)  {
		this.cap_resid=cap_resid;
	}
	public java.lang.String getPro_resid () {
		return pro_resid;
	}
	public void setPro_resid(java.lang.String pro_resid)  {
		this.pro_resid=pro_resid;
	}
	public java.lang.String getCod_comune () {
		return cod_comune;
	}
	public void setCod_comune(java.lang.String cod_comune)  {
		this.cod_comune=cod_comune;
	}
	public java.lang.String getStato_civile () {
		return stato_civile;
	}
	public void setStato_civile(java.lang.String stato_civile)  {
		this.stato_civile=stato_civile;
	}
	public java.sql.Timestamp getDec_stat_civ () {
		return dec_stat_civ;
	}
	public void setDec_stat_civ(java.sql.Timestamp dec_stat_civ)  {
		this.dec_stat_civ=dec_stat_civ;
	}
	public java.lang.String getTit_onorif () {
		return tit_onorif;
	}
	public void setTit_onorif(java.lang.String tit_onorif)  {
		this.tit_onorif=tit_onorif;
	}
	public java.lang.String getDesc_tit_onorif () {
		return desc_tit_onorif;
	}
	public void setDesc_tit_onorif(java.lang.String desc_tit_onorif)  {
		this.desc_tit_onorif=desc_tit_onorif;
	}
	public java.lang.String getTit_studio () {
		return tit_studio;
	}
	public void setTit_studio(java.lang.String tit_studio)  {
		this.tit_studio=tit_studio;
	}
	public java.lang.String getDesc_tit_studio () {
		return desc_tit_studio;
	}
	public void setDesc_tit_studio(java.lang.String desc_tit_studio)  {
		this.desc_tit_studio=desc_tit_studio;
	}
	public java.lang.String getCod_spec () {
		return cod_spec;
	}
	public void setCod_spec(java.lang.String cod_spec)  {
		this.cod_spec=cod_spec;
	}
	public java.lang.String getDesc_spec () {
		return desc_spec;
	}
	public void setDesc_spec(java.lang.String desc_spec)  {
		this.desc_spec=desc_spec;
	}
	public java.lang.Integer getAnno_spec () {
		return anno_spec;
	}
	public void setAnno_spec(java.lang.Integer anno_spec)  {
		this.anno_spec=anno_spec;
	}
	public java.lang.String getLuogo_spec () {
		return luogo_spec;
	}
	public void setLuogo_spec(java.lang.String luogo_spec)  {
		this.luogo_spec=luogo_spec;
	}
	public java.lang.String getAbilitazione () {
		return abilitazione;
	}
	public void setAbilitazione(java.lang.String abilitazione)  {
		this.abilitazione=abilitazione;
	}
	public java.lang.Integer getAnno_abilitazione () {
		return anno_abilitazione;
	}
	public void setAnno_abilitazione(java.lang.Integer anno_abilitazione)  {
		this.anno_abilitazione=anno_abilitazione;
	}
	public java.lang.String getFascicolo () {
		return fascicolo;
	}
	public void setFascicolo(java.lang.String fascicolo)  {
		this.fascicolo=fascicolo;
	}
	public java.sql.Timestamp getData_assunzione () {
		return data_assunzione;
	}
	public void setData_assunzione(java.sql.Timestamp data_assunzione)  {
		this.data_assunzione=data_assunzione;
	}
	public java.sql.Timestamp getData_cessazione () {
		return data_cessazione;
	}
	public void setData_cessazione(java.sql.Timestamp data_cessazione)  {
		this.data_cessazione=data_cessazione;
	}
	public java.lang.String getCausa_cess () {
		return causa_cess;
	}
	public void setCausa_cess(java.lang.String causa_cess)  {
		this.causa_cess=causa_cess;
	}
	public java.lang.String getDesc_causa_cess () {
		return desc_causa_cess;
	}
	public void setDesc_causa_cess(java.lang.String desc_causa_cess)  {
		this.desc_causa_cess=desc_causa_cess;
	}
	public java.lang.String getRapp_impiego () {
		return rapp_impiego;
	}
	public void setRapp_impiego(java.lang.String rapp_impiego)  {
		this.rapp_impiego=rapp_impiego;
	}
	public java.lang.String getLivello_1 () {
		return livello_1;
	}
	public void setLivello_1(java.lang.String livello_1)  {
		this.livello_1=livello_1;
	}
	public java.lang.String getLivello_2 () {
		return livello_2;
	}
	public void setLivello_2(java.lang.String livello_2)  {
		this.livello_2=livello_2;
	}
	public java.lang.String getLivello_3 () {
		return livello_3;
	}
	public void setLivello_3(java.lang.String livello_3)  {
		this.livello_3=livello_3;
	}
	public java.lang.Integer getProfilo () {
		return profilo;
	}
	public void setProfilo(java.lang.Integer profilo)  {
		this.profilo=profilo;
	}
	public java.lang.String getContratto () {
		return contratto;
	}
	public void setContratto(java.lang.String contratto)  {
		this.contratto=contratto;
	}
	public java.lang.String getDesc_profilo () {
		return desc_profilo;
	}
	public void setDesc_profilo(java.lang.String desc_profilo)  {
		this.desc_profilo=desc_profilo;
	}
	public java.lang.String getLivprec_1 () {
		return livprec_1;
	}
	public void setLivprec_1(java.lang.String livprec_1)  {
		this.livprec_1=livprec_1;
	}
	public java.lang.String getLivprec_2 () {
		return livprec_2;
	}
	public void setLivprec_2(java.lang.String livprec_2)  {
		this.livprec_2=livprec_2;
	}
	public java.lang.String getLivprec_3 () {
		return livprec_3;
	}
	public void setLivprec_3(java.lang.String livprec_3)  {
		this.livprec_3=livprec_3;
	}
	public java.lang.Integer getProfilo_prec () {
		return profilo_prec;
	}
	public void setProfilo_prec(java.lang.Integer profilo_prec)  {
		this.profilo_prec=profilo_prec;
	}
	public java.lang.String getDesc_prof_prec () {
		return desc_prof_prec;
	}
	public void setDesc_prof_prec(java.lang.String desc_prof_prec)  {
		this.desc_prof_prec=desc_prof_prec;
	}
	public java.lang.String getContratto_prec () {
		return contratto_prec;
	}
	public void setContratto_prec(java.lang.String contratto_prec)  {
		this.contratto_prec=contratto_prec;
	}
	public java.sql.Timestamp getProfilo_prec_fine () {
		return profilo_prec_fine;
	}
	public void setProfilo_prec_fine(java.sql.Timestamp profilo_prec_fine)  {
		this.profilo_prec_fine=profilo_prec_fine;
	}
	public java.lang.String getTipo_contratto () {
		return tipo_contratto;
	}
	public void setTipo_contratto(java.lang.String tipo_contratto)  {
		this.tipo_contratto=tipo_contratto;
	}
	public java.lang.String getDesc_tipo_contratto () {
		return desc_tipo_contratto;
	}
	public void setDesc_tipo_contratto(java.lang.String desc_tipo_contratto)  {
		this.desc_tipo_contratto=desc_tipo_contratto;
	}
	public java.lang.String getEnte_prev () {
		return ente_prev;
	}
	public void setEnte_prev(java.lang.String ente_prev)  {
		this.ente_prev=ente_prev;
	}
	public java.lang.String getUnita_oper () {
		return unita_oper;
	}
	public void setUnita_oper(java.lang.String unita_oper)  {
		this.unita_oper=unita_oper;
	}
	public java.lang.String getSede_lavoro () {
		return sede_lavoro;
	}
	public void setSede_lavoro(java.lang.String sede_lavoro)  {
		this.sede_lavoro=sede_lavoro;
	}
	public java.sql.Timestamp getDec_inizio () {
		return dec_inizio;
	}
	public void setDec_inizio(java.sql.Timestamp dec_inizio)  {
		this.dec_inizio=dec_inizio;
	}
	public java.sql.Timestamp getDec_fine () {
		return dec_fine;
	}
	public void setDec_fine(java.sql.Timestamp dec_fine)  {
		this.dec_fine=dec_fine;
	}
	public java.lang.String getTipo_pres () {
		return tipo_pres;
	}
	public void setTipo_pres(java.lang.String tipo_pres)  {
		this.tipo_pres=tipo_pres;
	}
	public java.lang.String getDesc_tipo_pres () {
		return desc_tipo_pres;
	}
	public void setDesc_tipo_pres(java.lang.String desc_tipo_pres)  {
		this.desc_tipo_pres=desc_tipo_pres;
	}
	public java.lang.String getTit_afferen () {
		return tit_afferen;
	}
	public void setTit_afferen(java.lang.String tit_afferen)  {
		this.tit_afferen=tit_afferen;
	}
	public java.lang.String getMod_pag () {
		return mod_pag;
	}
	public void setMod_pag(java.lang.String mod_pag)  {
		this.mod_pag=mod_pag;
	}
	public java.lang.String getAbi_pag () {
		return abi_pag;
	}
	public void setAbi_pag(java.lang.String abi_pag)  {
		this.abi_pag=abi_pag;
	}
	public java.lang.String getCab_pag () {
		return cab_pag;
	}
	public void setCab_pag(java.lang.String cab_pag)  {
		this.cab_pag=cab_pag;
	}
	public java.lang.String getNrc_pag () {
		return nrc_pag;
	}
	public void setNrc_pag(java.lang.String nrc_pag)  {
		this.nrc_pag=nrc_pag;
	}
	public java.math.BigDecimal getImp_fisc () {
		return imp_fisc;
	}
	public void setImp_fisc(java.math.BigDecimal imp_fisc)  {
		this.imp_fisc=imp_fisc;
	}
	public java.math.BigDecimal getImp_prev () {
		return imp_prev;
	}
	public void setImp_prev(java.math.BigDecimal imp_prev)  {
		this.imp_prev=imp_prev;
	}
	public java.math.BigDecimal getImp_impo1 () {
		return imp_impo1;
	}
	public void setImp_impo1(java.math.BigDecimal imp_impo1)  {
		this.imp_impo1=imp_impo1;
	}
	public java.math.BigDecimal getImp_impo2 () {
		return imp_impo2;
	}
	public void setImp_impo2(java.math.BigDecimal imp_impo2)  {
		this.imp_impo2=imp_impo2;
	}
	public java.lang.String getSindacato () {
		return sindacato;
	}
	public void setSindacato(java.lang.String sindacato)  {
		this.sindacato=sindacato;
	}
	public java.math.BigDecimal getAnz_rapp_prec () {
		return anz_rapp_prec;
	}
	public void setAnz_rapp_prec(java.math.BigDecimal anz_rapp_prec)  {
		this.anz_rapp_prec=anz_rapp_prec;
	}
	public java.math.BigDecimal getAnz_riscatti () {
		return anz_riscatti;
	}
	public void setAnz_riscatti(java.math.BigDecimal anz_riscatti)  {
		this.anz_riscatti=anz_riscatti;
	}
	public java.math.BigDecimal getTot_anni () {
		return tot_anni;
	}
	public void setTot_anni(java.math.BigDecimal tot_anni)  {
		this.tot_anni=tot_anni;
	}
	public java.sql.Timestamp getData_rif () {
		return data_rif;
	}
	public void setData_rif(java.sql.Timestamp data_rif)  {
		this.data_rif=data_rif;
	}
	public java.lang.Integer getAnno_rif () {
		return anno_rif;
	}
	public void setAnno_rif(java.lang.Integer anno_rif)  {
		this.anno_rif=anno_rif;
	}
	public java.lang.Integer getMese_rif () {
		return mese_rif;
	}
	public void setMese_rif(java.lang.Integer mese_rif)  {
		this.mese_rif=mese_rif;
	}
	public java.lang.String getTipo_ass () {
		return tipo_ass;
	}
	public void setTipo_ass(java.lang.String tipo_ass)  {
		this.tipo_ass=tipo_ass;
	}
	public java.lang.String getDes_tipo_ass () {
		return des_tipo_ass;
	}
	public void setDes_tipo_ass(java.lang.String des_tipo_ass)  {
		this.des_tipo_ass=des_tipo_ass;
	}
	public java.lang.String getRif_conc () {
		return rif_conc;
	}
	public void setRif_conc(java.lang.String rif_conc)  {
		this.rif_conc=rif_conc;
	}
	public java.lang.String getTab_cod_comnas () {
		return tab_cod_comnas;
	}
	public void setTab_cod_comnas(java.lang.String tab_cod_comnas)  {
		this.tab_cod_comnas=tab_cod_comnas;
	}
	public java.lang.String getTab_cap_comnas () {
		return tab_cap_comnas;
	}
	public void setTab_cap_comnas(java.lang.String tab_cap_comnas)  {
		this.tab_cap_comnas=tab_cap_comnas;
	}
	public java.lang.String getPartime () {
		return partime;
	}
	public void setPartime(java.lang.String partime)  {
		this.partime=partime;
	}
	public java.math.BigDecimal getAtt_rid_stip () {
		return att_rid_stip;
	}
	public void setAtt_rid_stip(java.math.BigDecimal att_rid_stip)  {
		this.att_rid_stip=att_rid_stip;
	}
	public java.sql.Timestamp getGiu_dec_inizio () {
		return giu_dec_inizio;
	}
	public void setGiu_dec_inizio(java.sql.Timestamp giu_dec_inizio)  {
		this.giu_dec_inizio=giu_dec_inizio;
	}
	public java.lang.String getNaz_nas () {
		return naz_nas;
	}
	public void setNaz_nas(java.lang.String naz_nas)  {
		this.naz_nas=naz_nas;
	}
	public java.lang.Integer getNcar_cogn () {
		return ncar_cogn;
	}
	public void setNcar_cogn(java.lang.Integer ncar_cogn)  {
		this.ncar_cogn=ncar_cogn;
	}
	public java.lang.String getUo_tit () {
		return uo_tit;
	}
	public void setUo_tit(java.lang.String uo_tit)  {
		this.uo_tit=uo_tit;
	}
	public java.lang.Boolean getFl_gestione_manuale () {
		return fl_gestione_manuale;
	}
	public void setFl_gestione_manuale(java.lang.Boolean fl_gestione_manuale)  {
		this.fl_gestione_manuale=fl_gestione_manuale;
	}
}