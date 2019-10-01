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
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class IpaServizioFatturaBase extends IpaServizioFatturaKey implements Keyed {
//    COD_UNI_OU VARCHAR(6)
	private java.lang.String codUniOu;
 
//    CF VARCHAR(11)
	private java.lang.String cf;
 
//    DATA_AVVIO_SFE TIMESTAMP(7)
	private java.sql.Timestamp dataAvvioSfe;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_SERVIZIO_FATTURA
	 **/
	public IpaServizioFatturaBase() {
		super();
	}
	public IpaServizioFatturaBase(java.lang.String codAmm, java.lang.String codOu) {
		super(codAmm, codOu);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codUniOu]
	 **/
	public java.lang.String getCodUniOu() {
		return codUniOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codUniOu]
	 **/
	public void setCodUniOu(java.lang.String codUniOu)  {
		this.codUniOu=codUniOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cf]
	 **/
	public java.lang.String getCf() {
		return cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cf]
	 **/
	public void setCf(java.lang.String cf)  {
		this.cf=cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataAvvioSfe]
	 **/
	public java.sql.Timestamp getDataAvvioSfe() {
		return dataAvvioSfe;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataAvvioSfe]
	 **/
	public void setDataAvvioSfe(java.sql.Timestamp dataAvvioSfe)  {
		this.dataAvvioSfe=dataAvvioSfe;
	}
}