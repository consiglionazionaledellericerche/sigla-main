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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_ap_chBulk extends Inventario_ap_chBase {

	private Id_inventarioBulk inventario;
	private Inventario_consegnatarioBulk inventarioConsegnatario;
	public static final java.lang.String OPEN = "A";
	public final static java.lang.String CLOSE = "C";
	public final static java.lang.String PENDING = "P";
	private boolean inventarioRO = false;
	private java.util.List storiaApertura;

	// Flag che indica sei il Bulk corrente corrisponde allo stato attuale
	private Boolean isAttuale;
public Inventario_ap_chBulk() {
	super();
}
public Inventario_ap_chBulk(java.sql.Timestamp dt_apertura,java.lang.Integer esercizio,java.lang.Long pg_inventario) {
	super(dt_apertura,esercizio,pg_inventario);
}
/**
 * Insert the method's description here.
 * Creation date: (14/12/2001 14.31.49)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataChiusura() {
	
	if ((getDt_chiusura()!=null) && (getDt_chiusura().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)) )
		return null;
	return getDt_chiusura();
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2001 14.03.15)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public Id_inventarioBulk getInventario() {
	return inventario;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.13.20)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Inventario_consegnatarioBulk
 */
public Inventario_consegnatarioBulk getInventarioConsegnatario() {
	return inventarioConsegnatario;
}
/**
 * Insert the method's description here.
 * Creation date: (09/05/2002 14.37.41)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getIsAttuale() {
	return isAttuale;
}
public java.lang.Long getPg_inventario() {
	it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = this.getInventario();
	if (inventario == null)
		return null;
	return inventario.getPg_inventario();
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/2001 12.23.34)
 * @return java.util.List
 */
public java.util.List getStoriaApertura() {
	return storiaApertura;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isClose() {
	
	if (getStato()!=null)
		return  getStato().equalsIgnoreCase(Inventario_ap_chBulk.CLOSE);

	return true;
			
}
public boolean isInventarioConsegnatarioRO() {
	if (getInventarioConsegnatario()==null)
		return true;
	return (getInventarioConsegnatario().getCrudStatus()!=it.cnr.jada.bulk.OggettoBulk.UNDEFINED);
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2001 16.40.48)
 * @return boolean
 */
public boolean isInventarioRO() {
	return inventarioRO;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isOpen() {
	
	if (getStato()!=null)
		return  getStato().equalsIgnoreCase(Inventario_ap_chBulk.OPEN);

	return true;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROdt_apertura() {
		
	if (isClose() && getIsAttuale()!= null && getIsAttuale().booleanValue())
		return false;
		
	return true;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROdt_chiusura() {
	
	if (isOpen() && getStato()!=null)
		return false;
		
	return true;	
}
/**
 * Insert the method's description here.
 * Creation date: (14/12/2001 14.31.49)
 * @param newDataChiusura java.sql.Timestamp
 */
public void setDataChiusura(java.sql.Timestamp newDataChiusura) {
	setDt_chiusura(newDataChiusura);
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2001 14.03.15)
 * @param newInventario it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public void setInventario(Id_inventarioBulk newInventario) {
	inventario = newInventario;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.13.20)
 * @param newInventarioConsegnatario it.cnr.contab.inventario00.tabrif.bulk.Inventario_consegnatarioBulk
 */
public void setInventarioConsegnatario(Inventario_consegnatarioBulk newInventarioConsegnatario) {
	inventarioConsegnatario = newInventarioConsegnatario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2001 16.40.48)
 * @param newInventarioRO boolean
 */
public void setInventarioRO(boolean newInventarioRO) {
	inventarioRO = newInventarioRO;
}
/**
 * Insert the method's description here.
 * Creation date: (09/05/2002 14.37.41)
 * @param newIsAttuale java.lang.Boolean
 */
public void setIsAttuale(java.lang.Boolean newIsAttuale) {
	isAttuale = newIsAttuale;
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getInventario().setPg_inventario(pg_inventario);
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/2001 12.23.34)
 * @param newStoriaApertura java.util.List
 */
public void setStoriaApertura(java.util.List newStoriaApertura) {
	storiaApertura = newStoriaApertura;
}
}
