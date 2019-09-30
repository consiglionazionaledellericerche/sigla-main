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

package it.cnr.contab.logregistry00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (30/09/2003 17.13.16)
 * @author: CNRADM
 */
public interface ILogRegistryBulk {

	public static final String PKG_NAME = "it.cnr.contab.logregistry00.logs.bulk";
	public static final String SUFFIX = "Bulk";
	
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.lang.String
 */
public java.lang.String getAction_();
/* 
 * Getter dell'attributo dt_transaction_
 */
public java.sql.Timestamp getDt_transaction_();
/* 
 * Getter dell'attributo pg_storico_
 */
public java.math.BigDecimal getPg_storico_();
/* 
 * Getter dell'attributo user_
 */
public java.lang.String getUser_();
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newAction_ java.lang.String
 */
public void setAction_(java.lang.String newAction_);
/* 
 * Setter dell'attributo dt_transaction_
 */
public void setDt_transaction_(java.sql.Timestamp dt_transaction_);
/* 
 * Setter dell'attributo pg_storico_
 */
public void setPg_storico_(java.math.BigDecimal pg_storico_);
/* 
 * Setter dell'attributo user_
 */
public void setUser_(java.lang.String user_);
}
