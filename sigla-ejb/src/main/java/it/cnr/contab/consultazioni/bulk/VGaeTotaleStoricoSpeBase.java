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
 * Date 29/08/2013
 */
package it.cnr.contab.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class VGaeTotaleStoricoSpeBase extends   OggettoBulk implements Persistent {
//    CDR VARCHAR(30)
	private java.lang.String cdr;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cdLineaAttivita;
 
//    DENOMINAZIONE VARCHAR(300)
	private java.lang.String denominazione;
 
//    CD_RESPONSABILE_TERZO DECIMAL(8,0)
	private java.lang.Integer cdResponsabileTerzo;
 
//    DS_TERZO VARCHAR(200)
	private java.lang.String dsTerzo;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cdModulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String dsModulo;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pgProgetto;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cdElementoVoce;
 
//    STANZIAMENTO_ASSESTATO_TOT DECIMAL(22,0)
	private java.math.BigDecimal stanziamentoAssestatoTot;
 
//    IMPEGNATO DECIMAL(22,0)
	private java.math.BigDecimal  impegnato;
 
//    DISPONIBILITA_IMPEGNARE DECIMAL(22,0)
	private java.math.BigDecimal  disponibilitaImpegnare;
 
//    PAGATO DECIMAL(22,0)
	private java.math.BigDecimal  pagato;
 
//    DA_PAGARE DECIMAL(22,0)
	private java.math.BigDecimal  daPagare;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_GAE_TOTALE_STORICO_SPE
	 **/
	public VGaeTotaleStoricoSpeBase() {
		super();
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdr]
	 **/
	public java.lang.String getCdr() {
		return cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdr]
	 **/
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLineaAttivita]
	 **/
	public java.lang.String getCdLineaAttivita() {
		return cdLineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLineaAttivita]
	 **/
	public void setCdLineaAttivita(java.lang.String cdLineaAttivita)  {
		this.cdLineaAttivita=cdLineaAttivita;
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
	 * Restituisce il valore di: [cdResponsabileTerzo]
	 **/
	public java.lang.Integer getCdResponsabileTerzo() {
		return cdResponsabileTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdResponsabileTerzo]
	 **/
	public void setCdResponsabileTerzo(java.lang.Integer cdResponsabileTerzo)  {
		this.cdResponsabileTerzo=cdResponsabileTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTerzo]
	 **/
	public java.lang.String getDsTerzo() {
		return dsTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTerzo]
	 **/
	public void setDsTerzo(java.lang.String dsTerzo)  {
		this.dsTerzo=dsTerzo;
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
	 * Restituisce il valore di: [pgProgetto]
	 **/
	public java.lang.Long getPgProgetto() {
		return pgProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgProgetto]
	 **/
	public void setPgProgetto(java.lang.Long pgProgetto)  {
		this.pgProgetto=pgProgetto;
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

	public java.math.BigDecimal getStanziamentoAssestatoTot() {
		return stanziamentoAssestatoTot;
	}

	public void setStanziamentoAssestatoTot(
			java.math.BigDecimal stanziamentoAssestatoTot) {
		this.stanziamentoAssestatoTot = stanziamentoAssestatoTot;
	}

	public java.math.BigDecimal getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(java.math.BigDecimal impegnato) {
		this.impegnato = impegnato;
	}

	public java.math.BigDecimal getDisponibilitaImpegnare() {
		return disponibilitaImpegnare;
	}

	public void setDisponibilitaImpegnare(
			java.math.BigDecimal disponibilitaImpegnare) {
		this.disponibilitaImpegnare = disponibilitaImpegnare;
	}

	public java.math.BigDecimal getPagato() {
		return pagato;
	}

	public void setPagato(java.math.BigDecimal pagato) {
		this.pagato = pagato;
	}

	public java.math.BigDecimal getDaPagare() {
		return daPagare;
	}

	public void setDaPagare(java.math.BigDecimal daPagare) {
		this.daPagare = daPagare;
	}
	
}