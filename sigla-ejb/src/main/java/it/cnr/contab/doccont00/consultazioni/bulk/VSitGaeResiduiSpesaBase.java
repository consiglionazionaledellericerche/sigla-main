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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 18/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class VSitGaeResiduiSpesaBase extends VSitGaeResiduiSpesaKey implements Keyed {
//    CDS VARCHAR(4000)
	private java.lang.String cds;
 
//    UO VARCHAR(4000)
	private java.lang.String uo;
 
//    DS_CDR VARCHAR(300)
	private java.lang.String dsCdr;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String dsLineaAttivita;
 
//    CD_NATURA VARCHAR(1)
	private java.lang.String cdNatura;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cdProgetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String dsProgetto;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cdCommessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String dsCommessa;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cdModulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String dsModulo;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cdElementoVoce;
 
//    DS_ELEMENTO_VOCE VARCHAR(200)
	private java.lang.String dsElementoVoce;
 
//    STANZ_RES_INI DECIMAL(22,0)
	private BigDecimal stanzResIni;
 
//    VAR_STANZ_RES_PIU DECIMAL(22,0)
	private BigDecimal varStanzResPiu;
 
//    VAR_STANZ_RES_MENO_STO DECIMAL(22,0)
	private BigDecimal varStanzResMenoSto;
 
//    VAR_STANZ_RES_MENO_ECO DECIMAL(22,0)
	private BigDecimal varStanzResMenoEco;
 
//    RES_PRO_INI DECIMAL(22,0)
	private BigDecimal resProIni;
 
//    VAR_RES_PRO_PIU DECIMAL(22,0)
	private BigDecimal varResProPiu;
 
//    VAR_RES_PRO_MENO DECIMAL(22,0)
	private BigDecimal varResProMeno;
 
//    LIQUIDATO_PRO DECIMAL(22,0)
	private BigDecimal liquidatoPro;
 
//    PAGATO_PRO DECIMAL(22,0)
	private BigDecimal pagatoPro;
 
//    RES_IMP_RIBALTATI DECIMAL(22,0)
	private BigDecimal resImpRibaltati;
 
//    RES_IMP_RIB_LIQ DECIMAL(22,0)
	private BigDecimal resImpRibLiq;
 
//    RES_IMP_RIB_PAG DECIMAL(22,0)
	private BigDecimal resImpRibPag;
 
//    RES_IMP_EM_ESE DECIMAL(22,0)
	private BigDecimal resImpEmEse;
 
//    RES_IMP_EM_ESE_LIQ DECIMAL(22,0)
	private BigDecimal resImpEmEseLiq;
 
//    RES_IMP_EM_ESE_PAG DECIMAL(22,0)
	private BigDecimal resImpEmEsePag;
 
//    RES_IMP_ATTUALI DECIMAL(22,0)
	private BigDecimal resImpAttuali;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SIT_GAE_RESIDUI_SPESA
	 **/
	public VSitGaeResiduiSpesaBase() {
		super();
	}
	public VSitGaeResiduiSpesaBase(java.lang.Integer esercizio,java.lang.String cd_centro_responsabilita,java.lang.Integer esercizioRes,java.lang.String cd_linea_attivita,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String cd_voce) {
		super(esercizio, cd_centro_responsabilita, esercizioRes, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_voce);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cds]
	 **/
	public java.lang.String getCds() {
		return cds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cds]
	 **/
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [uo]
	 **/
	public java.lang.String getUo() {
		return uo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [uo]
	 **/
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCdr]
	 **/
	public java.lang.String getDsCdr() {
		return dsCdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCdr]
	 **/
	public void setDsCdr(java.lang.String dsCdr)  {
		this.dsCdr=dsCdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsLineaAttivita]
	 **/
	public java.lang.String getDsLineaAttivita() {
		return dsLineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsLineaAttivita]
	 **/
	public void setDsLineaAttivita(java.lang.String dsLineaAttivita)  {
		this.dsLineaAttivita=dsLineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNatura]
	 **/
	public java.lang.String getCdNatura() {
		return cdNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNatura]
	 **/
	public void setCdNatura(java.lang.String cdNatura)  {
		this.cdNatura=cdNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		return cdProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.cdProgetto=cdProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsProgetto]
	 **/
	public java.lang.String getDsProgetto() {
		return dsProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsProgetto]
	 **/
	public void setDsProgetto(java.lang.String dsProgetto)  {
		this.dsProgetto=dsProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCommessa]
	 **/
	public java.lang.String getCdCommessa() {
		return cdCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCommessa]
	 **/
	public void setCdCommessa(java.lang.String cdCommessa)  {
		this.cdCommessa=cdCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCommessa]
	 **/
	public java.lang.String getDsCommessa() {
		return dsCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCommessa]
	 **/
	public void setDsCommessa(java.lang.String dsCommessa)  {
		this.dsCommessa=dsCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModulo]
	 **/
	public java.lang.String getCdModulo() {
		return cdModulo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModulo]
	 **/
	public void setCdModulo(java.lang.String cdModulo)  {
		this.cdModulo=cdModulo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsModulo]
	 **/
	public java.lang.String getDsModulo() {
		return dsModulo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsModulo]
	 **/
	public void setDsModulo(java.lang.String dsModulo)  {
		this.dsModulo=dsModulo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCdElementoVoce() {
		return cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCdElementoVoce(java.lang.String cdElementoVoce)  {
		this.cdElementoVoce=cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsElementoVoce]
	 **/
	public java.lang.String getDsElementoVoce() {
		return dsElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsElementoVoce]
	 **/
	public void setDsElementoVoce(java.lang.String dsElementoVoce)  {
		this.dsElementoVoce=dsElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stanzResIni]
	 **/
	public BigDecimal getStanzResIni() {
		return stanzResIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stanzResIni]
	 **/
	public void setStanzResIni(BigDecimal stanzResIni)  {
		this.stanzResIni=stanzResIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varStanzResPiu]
	 **/
	public BigDecimal getVarStanzResPiu() {
		return varStanzResPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varStanzResPiu]
	 **/
	public void setVarStanzResPiu(BigDecimal varStanzResPiu)  {
		this.varStanzResPiu=varStanzResPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varStanzResMenoSto]
	 **/
	public BigDecimal getVarStanzResMenoSto() {
		return varStanzResMenoSto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varStanzResMenoSto]
	 **/
	public void setVarStanzResMenoSto(BigDecimal varStanzResMenoSto)  {
		this.varStanzResMenoSto=varStanzResMenoSto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varStanzResMenoEco]
	 **/
	public BigDecimal getVarStanzResMenoEco() {
		return varStanzResMenoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varStanzResMenoEco]
	 **/
	public void setVarStanzResMenoEco(BigDecimal varStanzResMenoEco)  {
		this.varStanzResMenoEco=varStanzResMenoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resProIni]
	 **/
	public BigDecimal getResProIni() {
		return resProIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resProIni]
	 **/
	public void setResProIni(BigDecimal resProIni)  {
		this.resProIni=resProIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varResProPiu]
	 **/
	public BigDecimal getVarResProPiu() {
		return varResProPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varResProPiu]
	 **/
	public void setVarResProPiu(BigDecimal varResProPiu)  {
		this.varResProPiu=varResProPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varResProMeno]
	 **/
	public BigDecimal getVarResProMeno() {
		return varResProMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varResProMeno]
	 **/
	public void setVarResProMeno(BigDecimal varResProMeno)  {
		this.varResProMeno=varResProMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [liquidatoPro]
	 **/
	public BigDecimal getLiquidatoPro() {
		return liquidatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [liquidatoPro]
	 **/
	public void setLiquidatoPro(BigDecimal liquidatoPro)  {
		this.liquidatoPro=liquidatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pagatoPro]
	 **/
	public BigDecimal getPagatoPro() {
		return pagatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pagatoPro]
	 **/
	public void setPagatoPro(BigDecimal pagatoPro)  {
		this.pagatoPro=pagatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpRibaltati]
	 **/
	public BigDecimal getResImpRibaltati() {
		return resImpRibaltati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpRibaltati]
	 **/
	public void setResImpRibaltati(BigDecimal resImpRibaltati)  {
		this.resImpRibaltati=resImpRibaltati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpRibLiq]
	 **/
	public BigDecimal getResImpRibLiq() {
		return resImpRibLiq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpRibLiq]
	 **/
	public void setResImpRibLiq(BigDecimal resImpRibLiq)  {
		this.resImpRibLiq=resImpRibLiq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpRibPag]
	 **/
	public BigDecimal getResImpRibPag() {
		return resImpRibPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpRibPag]
	 **/
	public void setResImpRibPag(BigDecimal resImpRibPag)  {
		this.resImpRibPag=resImpRibPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpEmEse]
	 **/
	public BigDecimal getResImpEmEse() {
		return resImpEmEse;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpEmEse]
	 **/
	public void setResImpEmEse(BigDecimal resImpEmEse)  {
		this.resImpEmEse=resImpEmEse;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpEmEseLiq]
	 **/
	public BigDecimal getResImpEmEseLiq() {
		return resImpEmEseLiq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpEmEseLiq]
	 **/
	public void setResImpEmEseLiq(BigDecimal resImpEmEseLiq)  {
		this.resImpEmEseLiq=resImpEmEseLiq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpEmEsePag]
	 **/
	public BigDecimal getResImpEmEsePag() {
		return resImpEmEsePag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpEmEsePag]
	 **/
	public void setResImpEmEsePag(BigDecimal resImpEmEsePag)  {
		this.resImpEmEsePag=resImpEmEsePag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resImpAttuali]
	 **/
	public BigDecimal getResImpAttuali() {
		return resImpAttuali;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resImpAttuali]
	 **/
	public void setResImpAttuali(BigDecimal resImpAttuali)  {
		this.resImpAttuali=resImpAttuali;
	}
}