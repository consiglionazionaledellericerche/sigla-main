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
 * Creation date: (01/10/2003 12.19.20)
 * @author: CNRADM
 */
public abstract class OggettoLogBulk 
	extends it.cnr.jada.bulk.OggettoBulk 
	implements ILogRegistryBulk {
		
	// PG_STORICO_ DECIMAL(22,0) NOT NULL (PK)
	protected java.math.BigDecimal pg_storico_;

	// USER_ VARCHAR(20) NOT NULL
	protected java.lang.String user_;

	// DT_TRANSACTION_ TIMESTAMP NOT NULL
	protected java.sql.Timestamp dt_transaction_;

	// ACTION_ CHAR(1) NOT NULL
	protected java.lang.String action_;
/**
 * LogRegistryBulk constructor comment.
 */
public OggettoLogBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.lang.String
 */
public java.lang.String getAction_() {
	return action_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_transaction_() {
	return dt_transaction_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPg_storico_() {
	return pg_storico_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.lang.String
 */
public java.lang.String getUser_() {
	return user_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newAction_ java.lang.String
 */
public void setAction_(java.lang.String newAction_) {
	action_ = newAction_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newDt_transaction_ java.sql.Timestamp
 */
public void setDt_transaction_(java.sql.Timestamp newDt_transaction_) {
	dt_transaction_ = newDt_transaction_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newPg_storico_ java.math.BigDecimal
 */
public void setPg_storico_(java.math.BigDecimal newPg_storico_) {
	pg_storico_ = newPg_storico_;
}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newUser_ java.lang.String
 */
public void setUser_(java.lang.String newUser_) {
	user_ = newUser_;
}
}
