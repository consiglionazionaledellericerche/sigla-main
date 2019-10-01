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
public class L_configurazione_cnrBase extends L_configurazione_cnrKey implements Keyed {
//    IM02 DECIMAL(20,6)
	private java.math.BigDecimal im02;
 
//    DT01 TIMESTAMP(7)
	private java.sql.Timestamp dt01;
 
//    DT02 TIMESTAMP(7)
	private java.sql.Timestamp dt02;
 
//    VAL01 VARCHAR(100)
	private java.lang.String val01;
 
//    VAL02 VARCHAR(100)
	private java.lang.String val02;
 
//    VAL03 VARCHAR(100)
	private java.lang.String val03;
 
//    VAL04 VARCHAR(100)
	private java.lang.String val04;
 
//    IM01 DECIMAL(20,6)
	private java.math.BigDecimal im01;
 
//    USER_ VARCHAR(20) NOT NULL
	private java.lang.String user_;
 
//    DT_TRANSACTION_ TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_transaction_;
 
//    ACTION_ CHAR(1) NOT NULL
	private java.lang.String action_;
 
	public L_configurazione_cnrBase() {
		super();
	}
	public L_configurazione_cnrBase(java.math.BigDecimal pg_storico_, java.lang.Integer esercizio, java.lang.String cd_unita_funzionale, java.lang.String cd_chiave_primaria, java.lang.String cd_chiave_secondaria) {
		super(pg_storico_, esercizio, cd_unita_funzionale, cd_chiave_primaria, cd_chiave_secondaria);
	}
	public java.math.BigDecimal getIm02 () {
		return im02;
	}
	public void setIm02(java.math.BigDecimal im02)  {
		this.im02=im02;
	}
	public java.sql.Timestamp getDt01 () {
		return dt01;
	}
	public void setDt01(java.sql.Timestamp dt01)  {
		this.dt01=dt01;
	}
	public java.sql.Timestamp getDt02 () {
		return dt02;
	}
	public void setDt02(java.sql.Timestamp dt02)  {
		this.dt02=dt02;
	}
	public java.lang.String getVal01 () {
		return val01;
	}
	public void setVal01(java.lang.String val01)  {
		this.val01=val01;
	}
	public java.lang.String getVal02 () {
		return val02;
	}
	public void setVal02(java.lang.String val02)  {
		this.val02=val02;
	}
	public java.lang.String getVal03 () {
		return val03;
	}
	public void setVal03(java.lang.String val03)  {
		this.val03=val03;
	}
	public java.lang.String getVal04 () {
		return val04;
	}
	public void setVal04(java.lang.String val04)  {
		this.val04=val04;
	}
	public java.math.BigDecimal getIm01 () {
		return im01;
	}
	public void setIm01(java.math.BigDecimal im01)  {
		this.im01=im01;
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