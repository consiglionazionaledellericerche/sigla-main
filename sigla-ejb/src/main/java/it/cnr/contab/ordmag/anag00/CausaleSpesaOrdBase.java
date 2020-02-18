/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class CausaleSpesaOrdBase extends CausaleSpesaOrdKey implements Keyed {
//    DS_CAUSALE_SPESA VARCHAR(100) NOT NULL
	private String dsCausaleSpesa;
 
//    CD_VOCE_IVA VARCHAR(10)
	private String cdVoceIva;
 
//    FLAG_SPESE VARCHAR(1) NOT NULL
	private Boolean flagSpese;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CAUSALE_SPESA_ORD
	 **/
	public CausaleSpesaOrdBase() {
		super();
	}
	public CausaleSpesaOrdBase(String cdCds, Integer esercizio, String cdCausaleSpesa) {
		super(cdCds, esercizio, cdCausaleSpesa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCausaleSpesa]
	 **/
	public String getDsCausaleSpesa() {
		return dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCausaleSpesa]
	 **/
	public void setDsCausaleSpesa(String dsCausaleSpesa)  {
		this.dsCausaleSpesa=dsCausaleSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagSpese]
	 **/
	public Boolean getFlagSpese() {
		return flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagSpese]
	 **/
	public void setFlagSpese(Boolean flagSpese)  {
		this.flagSpese=flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}