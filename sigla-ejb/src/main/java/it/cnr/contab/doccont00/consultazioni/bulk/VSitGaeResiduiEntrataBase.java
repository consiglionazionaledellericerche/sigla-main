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
public class VSitGaeResiduiEntrataBase extends VSitGaeResiduiEntrataKey implements Keyed {
//    CDS VARCHAR(200)
	private java.lang.String cds;
 
//    UO VARCHAR(200)
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
 
//    RES_PRO_INI DECIMAL(22,0)
	private java.lang.Long resProIni;
 
//    VAR_RES_PRO_PIU DECIMAL(22,0)
	private java.lang.Long varResProPiu;
 
//    VAR_RES_PRO_MENO DECIMAL(22,0)
	private java.lang.Long varResProMeno;
 
//    TOTALE DECIMAL(22,0)
	private java.lang.Long totale;
 
//    LIQUIDATO_PRO DECIMAL(22,0)
	private java.lang.Long liquidatoPro;
 
//    INCASSATO_PRO DECIMAL(22,0)
	private java.lang.Long incassatoPro;
 
//    DA_INCASSARE DECIMAL(22,0)
	private java.lang.Long daIncassare;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SIT_GAE_RESIDUI_ENTRATA
	 **/
	public VSitGaeResiduiEntrataBase() {
		super();
	}
	public VSitGaeResiduiEntrataBase(java.lang.Integer esercizio,java.lang.String cd_centro_responsabilita,java.lang.Integer esercizioRes,java.lang.String cd_linea_attivita,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String cd_voce) {
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
	 * Restituisce il valore di: [resProIni]
	 **/
	public java.lang.Long getResProIni() {
		return resProIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resProIni]
	 **/
	public void setResProIni(java.lang.Long resProIni)  {
		this.resProIni=resProIni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varResProPiu]
	 **/
	public java.lang.Long getVarResProPiu() {
		return varResProPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varResProPiu]
	 **/
	public void setVarResProPiu(java.lang.Long varResProPiu)  {
		this.varResProPiu=varResProPiu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [varResProMeno]
	 **/
	public java.lang.Long getVarResProMeno() {
		return varResProMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [varResProMeno]
	 **/
	public void setVarResProMeno(java.lang.Long varResProMeno)  {
		this.varResProMeno=varResProMeno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totale]
	 **/
	public java.lang.Long getTotale() {
		return totale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totale]
	 **/
	public void setTotale(java.lang.Long totale)  {
		this.totale=totale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [liquidatoPro]
	 **/
	public java.lang.Long getLiquidatoPro() {
		return liquidatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [liquidatoPro]
	 **/
	public void setLiquidatoPro(java.lang.Long liquidatoPro)  {
		this.liquidatoPro=liquidatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [incassatoPro]
	 **/
	public java.lang.Long getIncassatoPro() {
		return incassatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [incassatoPro]
	 **/
	public void setIncassatoPro(java.lang.Long incassatoPro)  {
		this.incassatoPro=incassatoPro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [daIncassare]
	 **/
	public java.lang.Long getDaIncassare() {
		return daIncassare;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [daIncassare]
	 **/
	public void setDaIncassare(java.lang.Long daIncassare)  {
		this.daIncassare=daIncassare;
	}
}