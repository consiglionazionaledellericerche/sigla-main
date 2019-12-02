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
 * Date 07/10/2013
 */
package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.persistency.Keyed;
public class CofogBase extends CofogKey implements Keyed {
//    DS_COFOG VARCHAR(1000) NOT NULL
	private java.lang.String dsCofog;
 
//    CD_LIVELLO1 VARCHAR(3) NOT NULL
	private java.lang.String cdLivello1;
 
//    CD_LIVELLO2 VARCHAR(6)
	private java.lang.String cdLivello2;
 
//    CD_LIVELLO3 VARCHAR(10)
	private java.lang.String cdLivello3;
 
//    NR_LIVELLO DECIMAL(1,0) NOT NULL
	private java.lang.Integer nrLivello;
 
//    FL_ACCENTRATO CHAR(1) NOT NULL
	private Boolean flAccentrato;
 
//    FL_DECENTRATO CHAR(1) NOT NULL
	private Boolean flDecentrato;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: COFOG
	 **/
	public CofogBase() {
		super();
	}
	public CofogBase(java.lang.String cdCofog) {
		super(cdCofog);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCofog]
	 **/
	public java.lang.String getDsCofog() {
		return dsCofog;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCofog]
	 **/
	public void setDsCofog(java.lang.String dsCofog)  {
		this.dsCofog=dsCofog;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLivello1]
	 **/
	public java.lang.String getCdLivello1() {
		return cdLivello1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLivello1]
	 **/
	public void setCdLivello1(java.lang.String cdLivello1)  {
		this.cdLivello1=cdLivello1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLivello2]
	 **/
	public java.lang.String getCdLivello2() {
		return cdLivello2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLivello2]
	 **/
	public void setCdLivello2(java.lang.String cdLivello2)  {
		this.cdLivello2=cdLivello2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLivello3]
	 **/
	public java.lang.String getCdLivello3() {
		return cdLivello3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLivello3]
	 **/
	public void setCdLivello3(java.lang.String cdLivello3)  {
		this.cdLivello3=cdLivello3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrLivello]
	 **/
	public java.lang.Integer getNrLivello() {
		return nrLivello;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrLivello]
	 **/
	public void setNrLivello(java.lang.Integer nrLivello)  {
		this.nrLivello=nrLivello;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flAccentrato]
	 **/
	public Boolean getFlAccentrato() {
		return flAccentrato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flAccentrato]
	 **/
	public void setFlAccentrato(Boolean flAccentrato)  {
		this.flAccentrato=flAccentrato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flDecentrato]
	 **/
	public  Boolean getFlDecentrato() {
		return flDecentrato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flDecentrato]
	 **/
	public void setFlDecentrato(Boolean flDecentrato)  {
		this.flDecentrato=flDecentrato;
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