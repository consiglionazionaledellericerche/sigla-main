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
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class Monito_cococoBase extends Monito_cococoKey implements Persistent {
	
	private java.lang.String descrizione;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.String cd_terzo;
 
//    DENOMINAZIONE VARCHAR(100)
	private java.lang.String denominazione;
 
//    DATA_DA TIMESTAMP(7)
	private java.sql.Timestamp dt_da_competenza_coge;
 
//    DATA_A TIMESTAMP(7)
	private java.sql.Timestamp dt_a_competenza_coge;

//	ATTIVITA VARCHAR(1)
  	private java.lang.String attivita;	
 
//    NUMERO_GIORNI DECIMAL(10,0)
	private java.lang.Long numero_giorni;
 
//    IM_TOTALE_COMPENSO DECIMAL(15,2)
	private java.math.BigDecimal im_totale_compenso;
 
//    IM_LORDO_PERCIPIENTE DECIMAL(15,2)
	private java.math.BigDecimal im_lordo_percipiente;
 
//    IM_NETTO_PERCIPIENTE DECIMAL(15,2)
	private java.math.BigDecimal im_netto_percipiente;
 
//    IM_CR_PERCIPIENTE DECIMAL(15,2)
	private java.math.BigDecimal im_cr_percipiente;
 
//    IM_CR_ENTE DECIMAL(15,2)
	private java.math.BigDecimal im_cr_ente;
 
//    IM_TOTALE_COMPENSO_PERIODO DECIMAL(15,2)
	private java.math.BigDecimal im_totale_compenso_periodo;
 
	public Monito_cococoBase() {
		super();
	}
	public java.lang.String getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.String cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDenominazione () {
		return denominazione;
	}
	public void setDenominazione(java.lang.String denominazione)  {
		this.denominazione=denominazione;
	}
	public java.lang.Long getNumero_giorni () {
		return numero_giorni;
	}
	public void setNumero_giorni(java.lang.Long numero_giorni)  {
		this.numero_giorni=numero_giorni;
	}
	public java.math.BigDecimal getIm_totale_compenso () {
		return im_totale_compenso;
	}
	public void setIm_totale_compenso(java.math.BigDecimal im_totale_compenso)  {
		this.im_totale_compenso=im_totale_compenso;
	}
	public java.math.BigDecimal getIm_lordo_percipiente () {
		return im_lordo_percipiente;
	}
	public void setIm_lordo_percipiente(java.math.BigDecimal im_lordo_percipiente)  {
		this.im_lordo_percipiente=im_lordo_percipiente;
	}
	public java.math.BigDecimal getIm_netto_percipiente () {
		return im_netto_percipiente;
	}
	public void setIm_netto_percipiente(java.math.BigDecimal im_netto_percipiente)  {
		this.im_netto_percipiente=im_netto_percipiente;
	}
	public java.math.BigDecimal getIm_cr_percipiente () {
		return im_cr_percipiente;
	}
	public void setIm_cr_percipiente(java.math.BigDecimal im_cr_percipiente)  {
		this.im_cr_percipiente=im_cr_percipiente;
	}
	public java.math.BigDecimal getIm_cr_ente () {
		return im_cr_ente;
	}
	public void setIm_cr_ente(java.math.BigDecimal im_cr_ente)  {
		this.im_cr_ente=im_cr_ente;
	}
	public java.math.BigDecimal getIm_totale_compenso_periodo () {
		return im_totale_compenso_periodo;
	}
	public void setIm_totale_compenso_periodo(java.math.BigDecimal im_totale_compenso_periodo)  {
		this.im_totale_compenso_periodo=im_totale_compenso_periodo;
	}
	/**
	 * @return
	 */
	public java.lang.String getDescrizione() {
		return descrizione;
	}


	/**
	 * @param string
	 */
	public void setDescrizione(java.lang.String string) {
		descrizione = string;
	}

/**
 * @return
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}

/**
 * @return
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}

/**
 * @param timestamp
 */
public void setDt_a_competenza_coge(java.sql.Timestamp timestamp) {
	dt_a_competenza_coge = timestamp;
}

/**
 * @param timestamp
 */
public void setDt_da_competenza_coge(java.sql.Timestamp timestamp) {
	dt_da_competenza_coge = timestamp;
}

/**
 * @return
 */
public java.lang.String getAttivita() {
	return attivita;
}

/**
 * @param string
 */
public void setAttivita(java.lang.String string) {
	this.attivita = string;
}
}