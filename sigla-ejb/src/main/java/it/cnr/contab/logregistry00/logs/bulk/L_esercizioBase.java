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
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.persistency.Keyed;
public class L_esercizioBase extends L_esercizioKey implements Keyed {
//    DS_ESERCIZIO VARCHAR(100) NOT NULL
	private java.lang.String ds_esercizio;
 
//    ST_APERTURA_CHIUSURA CHAR(1)
	private java.lang.String st_apertura_chiusura;
 
//    IM_CASSA_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cassa_iniziale;
 
//    USER_ VARCHAR(20) NOT NULL
	private java.lang.String user_;
 
//    DT_TRANSACTION_ TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_transaction_;
 
//    ACTION_ CHAR(1) NOT NULL
	private java.lang.String action_;
 
	public L_esercizioBase() {
		super();
	}
	public L_esercizioBase(java.math.BigDecimal pg_storico_, java.lang.String cd_cds, java.lang.Integer esercizio) {
		super(pg_storico_, cd_cds, esercizio);
	}
	public java.lang.String getDs_esercizio () {
		return ds_esercizio;
	}
	public void setDs_esercizio(java.lang.String ds_esercizio)  {
		this.ds_esercizio=ds_esercizio;
	}
	public java.lang.String getSt_apertura_chiusura () {
		return st_apertura_chiusura;
	}
	public void setSt_apertura_chiusura(java.lang.String st_apertura_chiusura)  {
		this.st_apertura_chiusura=st_apertura_chiusura;
	}
	public java.math.BigDecimal getIm_cassa_iniziale () {
		return im_cassa_iniziale;
	}
	public void setIm_cassa_iniziale(java.math.BigDecimal im_cassa_iniziale)  {
		this.im_cassa_iniziale=im_cassa_iniziale;
	}
	public java.lang.String getUser_ () {
		return user_;
	}
	public void setUser_(java.lang.String user_)  {
		this.user_=user_;
	}
	public java.sql.Timestamp getDt_transaction_ () {
		return dt_transaction_;
	}
	public void setDt_transaction_(java.sql.Timestamp dt_transaction_)  {
		this.dt_transaction_=dt_transaction_;
	}
	public java.lang.String getAction_ () {
		return action_;
	}
	public void setAction_(java.lang.String action_)  {
		this.action_=action_;
	}
}