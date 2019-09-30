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
public class VSitAnaliticaGaeCompetenzaBase extends VSitAnaliticaGaeCompetenzaKey implements Keyed {
//    CDS VARCHAR(200)
	private java.lang.String cds;
 
//    UO VARCHAR(200)
	private java.lang.String uo;
 
//    DS_CDR VARCHAR(300)
	private java.lang.String dsCdr;
 
//    DENOMINAZIONE VARCHAR(300)
	private java.lang.String denominazione;
 
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
 
//    IM_STANZ_INIZIALE_A1 DECIMAL(15,2)
	private java.math.BigDecimal imStanzInizialeA1;
 
//    VARIAZIONI_PIU DECIMAL(15,2)
	private java.math.BigDecimal variazioniPiu;
 
//    VARIAZIONI_MENO DECIMAL(15,2)
	private java.math.BigDecimal variazioniMeno;
 
//    ASSESTATO_COMP DECIMAL(22,0)
	private java.math.BigDecimal assestatoComp;
 
//    IM_OBBL_ACC_COMP DECIMAL(15,2)
	private java.math.BigDecimal imObblAccComp;
 
//    DA_ASSUMERE DECIMAL(22,0)
	private java.math.BigDecimal daAssumere;
 
//    IM_ASS_DOC_AMM_SPE DECIMAL(22,0)
	private java.math.BigDecimal imAssDocAmmSpe;
 
//    IM_ASS_DOC_AMM_ETR DECIMAL(22,0)
	private java.math.BigDecimal imAssDocAmmEtr;
 
//    IM_MANDATI_REVERSALI_PRO DECIMAL(15,2)
	private java.math.BigDecimal imMandatiReversaliPro;
 
//    DA_PAGARE_INCASSARE DECIMAL(22,0)
	private java.math.BigDecimal daPagareIncassare;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SIT_ANALITICA_GAE_COMPETENZA
	 **/
	public VSitAnaliticaGaeCompetenzaBase() {
		super();
	}
	public VSitAnaliticaGaeCompetenzaBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce) {
		super(esercizio, cd_centro_responsabilita, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_voce);
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
	 * Restituisce il valore di: [denominazione]
	 **/
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [denominazione]
	 **/
	public void setDenominazione(java.lang.String denominazione)  {
		this.denominazione=denominazione;
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
	 * Restituisce il valore di: [imStanzInizialeA1]
	 **/
	public java.math.BigDecimal getImStanzInizialeA1() {
		return imStanzInizialeA1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imStanzInizialeA1]
	 **/
	public void setImStanzInizialeA1(java.math.BigDecimal imStanzInizialeA1)  {
		this.imStanzInizialeA1=imStanzInizialeA1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioniPiu]
	 **/
	public java.math.BigDecimal getVariazioniPiu() {
		return variazioniPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioniPiu]
	 **/
	public void setVariazioniPiu(java.math.BigDecimal variazioniPiu)  {
		this.variazioniPiu=variazioniPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioniMeno]
	 **/
	public java.math.BigDecimal getVariazioniMeno() {
		return variazioniMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioniMeno]
	 **/
	public void setVariazioniMeno(java.math.BigDecimal variazioniMeno)  {
		this.variazioniMeno=variazioniMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [assestatoComp]
	 **/
	public java.math.BigDecimal getAssestatoComp() {
		return assestatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [assestatoComp]
	 **/
	public void setAssestatoComp(java.math.BigDecimal assestatoComp)  {
		this.assestatoComp=assestatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imObblAccComp]
	 **/
	public java.math.BigDecimal getImObblAccComp() {
		return imObblAccComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imObblAccComp]
	 **/
	public void setImObblAccComp(java.math.BigDecimal imObblAccComp)  {
		this.imObblAccComp=imObblAccComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [daAssumere]
	 **/
	public java.math.BigDecimal getDaAssumere() {
		return daAssumere;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [daAssumere]
	 **/
	public void setDaAssumere(java.math.BigDecimal daAssumere)  {
		this.daAssumere=daAssumere;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssDocAmmSpe]
	 **/
	public java.math.BigDecimal getImAssDocAmmSpe() {
		return imAssDocAmmSpe;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssDocAmmSpe]
	 **/
	public void setImAssDocAmmSpe(java.math.BigDecimal imAssDocAmmSpe)  {
		this.imAssDocAmmSpe=imAssDocAmmSpe;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssDocAmmEtr]
	 **/
	public java.math.BigDecimal getImAssDocAmmEtr() {
		return imAssDocAmmEtr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssDocAmmEtr]
	 **/
	public void setImAssDocAmmEtr(java.math.BigDecimal imAssDocAmmEtr)  {
		this.imAssDocAmmEtr=imAssDocAmmEtr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imMandatiReversaliPro]
	 **/
	public java.math.BigDecimal getImMandatiReversaliPro() {
		return imMandatiReversaliPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imMandatiReversaliPro]
	 **/
	public void setImMandatiReversaliPro(java.math.BigDecimal imMandatiReversaliPro)  {
		this.imMandatiReversaliPro=imMandatiReversaliPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [daPagareIncassare]
	 **/
	public java.math.BigDecimal getDaPagareIncassare() {
		return daPagareIncassare;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [daPagareIncassare]
	 **/
	public void setDaPagareIncassare(java.math.BigDecimal daPagareIncassare)  {
		this.daPagareIncassare=daPagareIncassare;
	}
}