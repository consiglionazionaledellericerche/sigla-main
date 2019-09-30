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
public class IpaServFattBase extends IpaServFattKey implements Keyed {
//    COD_UNI_OU VARCHAR(6)
	private java.lang.String codUniOu;
 
//    DES_OU VARCHAR(1000)
	private java.lang.String desOu;
 
//    REGIONE VARCHAR(50)
	private java.lang.String regione;
 
//    PROVINCIA VARCHAR(4)
	private java.lang.String provincia;
 
//    COMUNE VARCHAR(100)
	private java.lang.String comune;
 
//    INDIRIZZO VARCHAR(200)
	private java.lang.String indirizzo;
 
//    CAP VARCHAR(5)
	private java.lang.String cap;
 
//    CF VARCHAR(11)
	private java.lang.String cf;
 
//    DT_VERIFICA_CF TIMESTAMP(7)
	private java.sql.Timestamp dtVerificaCf;
 
//    DATA_AVVIO_SFE TIMESTAMP(7)
	private java.sql.Timestamp dataAvvioSfe;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_SERV_FATT
	 **/
	public IpaServFattBase() {
		super();
	}
	public IpaServFattBase(java.lang.String codAmm, java.lang.String codOu) {
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
	 * Restituisce il valore di: [desOu]
	 **/
	public java.lang.String getDesOu() {
		return desOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [desOu]
	 **/
	public void setDesOu(java.lang.String desOu)  {
		this.desOu=desOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [regione]
	 **/
	public java.lang.String getRegione() {
		return regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [regione]
	 **/
	public void setRegione(java.lang.String regione)  {
		this.regione=regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provincia]
	 **/
	public java.lang.String getProvincia() {
		return provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provincia]
	 **/
	public void setProvincia(java.lang.String provincia)  {
		this.provincia=provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [comune]
	 **/
	public java.lang.String getComune() {
		return comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [comune]
	 **/
	public void setComune(java.lang.String comune)  {
		this.comune=comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzo]
	 **/
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzo]
	 **/
	public void setIndirizzo(java.lang.String indirizzo)  {
		this.indirizzo=indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cap]
	 **/
	public java.lang.String getCap() {
		return cap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cap]
	 **/
	public void setCap(java.lang.String cap)  {
		this.cap=cap;
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
	 * Restituisce il valore di: [dtVerificaCf]
	 **/
	public java.sql.Timestamp getDtVerificaCf() {
		return dtVerificaCf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtVerificaCf]
	 **/
	public void setDtVerificaCf(java.sql.Timestamp dtVerificaCf)  {
		this.dtVerificaCf=dtVerificaCf;
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