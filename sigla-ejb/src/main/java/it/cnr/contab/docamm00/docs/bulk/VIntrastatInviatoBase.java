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
 * Date 17/02/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.math.BigDecimal;

import it.cnr.jada.persistency.Keyed;
public class VIntrastatInviatoBase extends VIntrastatInviatoKey implements Keyed {
 
//    MESE DECIMAL(22,0)
	private java.lang.Integer mese;
 
//    TIPO VARCHAR(15)
	private java.lang.String tipo;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    NR_FATTURA VARCHAR(40)
	private java.lang.String nr_fattura;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;
 
//    TI_BENE_SERVIZIO VARCHAR(7)
	private java.lang.String ti_bene_servizio;
 
//    CODICE VARCHAR(8)
	private java.lang.String codice;
 
//    AMMONTARE_EURO DECIMAL(22,0)
	private BigDecimal ammontare_euro;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INTRASTAT_INVIATO
	 **/
	public VIntrastatInviatoBase() {
		super();
	}
	public VIntrastatInviatoBase(java.lang.String uo,java.lang.Integer esercizio,java.lang.Long progressivo,java.lang.String protocollo_invio,java.lang.Long riga_invio) {
		super(uo,esercizio,progressivo,protocollo_invio,riga_invio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mese]
	 **/
	public java.lang.Integer getMese() {
		return mese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mese]
	 **/
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public java.lang.String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt_registrazione]
	 **/
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt_registrazione]
	 **/
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nr_fattura]
	 **/
	public java.lang.String getNr_fattura() {
		return nr_fattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nr_fattura]
	 **/
	public void setNr_fattura(java.lang.String nr_fattura)  {
		this.nr_fattura=nr_fattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partita_iva]
	 **/
	public java.lang.String getPartita_iva() {
		return partita_iva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partita_iva]
	 **/
	public void setPartita_iva(java.lang.String partita_iva)  {
		this.partita_iva=partita_iva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ti_bene_servizio]
	 **/
	public java.lang.String getTi_bene_servizio() {
		return ti_bene_servizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ti_bene_servizio]
	 **/
	public void setTi_bene_servizio(java.lang.String ti_bene_servizio)  {
		this.ti_bene_servizio=ti_bene_servizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codice]
	 **/
	public java.lang.String getCodice() {
		return codice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codice]
	 **/
	public void setCodice(java.lang.String codice)  {
		this.codice=codice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ammontare_euro]
	 **/
	public BigDecimal getAmmontare_euro() {
		return ammontare_euro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ammontare_euro]
	 **/
	public void setAmmontare_euro(BigDecimal ammontare_euro)  {
		this.ammontare_euro=ammontare_euro;
	}
	
}