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

public class Id_inventarioBulk extends Id_inventarioBase {

	private it.cnr.jada.bulk.BulkList ass_inventario_uo;
	private java.util.List uo;
	
	private it.cnr.jada.bulk.BulkList ass_inventario_uoDisponibili;
	private java.util.List uoDisponibili;
	
	private Ass_inventario_uoBulk assInvUoResp;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoResp;
public Id_inventarioBulk() {
	super();
}
public Id_inventarioBulk(Long pg_inventario) {
	super(pg_inventario);
}
public void distribuisciProgressivo(){

	for (java.util.Iterator i = getAss_inventario_uo().iterator();i.hasNext();){
		((Ass_inventario_uoBulk)i.next()).setPg_inventario(this.getPg_inventario());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (22/11/2001 9.36.55)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getAss_inventario_uo() {
	return ass_inventario_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 17.58.53)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getAss_inventario_uoDisponibili() {
	return ass_inventario_uoDisponibili;
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 14.56.48)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ass_inventario_uoBulk
 */
public Ass_inventario_uoBulk getAssInvUoResp() {
	return assInvUoResp;
}
public BulkCollection[] getBulkLists() {
	return new BulkCollection[] { this.getAss_inventario_uo() };
}
/**
 * Insert the method's description here.
 * Creation date: (22/11/2001 9.37.54)
 * @return java.util.List
 */
public java.util.List getUo() {
	return uo;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 18.00.14)
 * @return java.util.List
 */
public java.util.List getUoDisponibili() {
	return uoDisponibili;
}
/**
 * Insert the method's description here.
 * Creation date: (22/11/2001 9.49.39)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoResp() {
	if (getAssInvUoResp() == null)
		return null;
	return getAssInvUoResp().getUnita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 13.34.33)
 * @return it.cnr.jada.bulk.OggettoBulk
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

	this.setNr_inventario_iniziale(new Long(0));

	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (22/11/2001 9.40.19)
 * @param AssInvUo it.cnr.jada.bulk.BulkList
 */
public void setAss_inventario_uo(BulkList newAss_inventario_uo) {

	ass_inventario_uo = newAss_inventario_uo;
	uo = new it.cnr.jada.util.Collect(ass_inventario_uo,"unita_organizzativa");
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 17.58.53)
 * @param newAss_inventario_uoDisponibili it.cnr.jada.bulk.BulkList
 */
public void setAss_inventario_uoDisponibili(it.cnr.jada.bulk.BulkList newAss_inventario_uoDisponibili) {
	ass_inventario_uoDisponibili = newAss_inventario_uoDisponibili;
	uoDisponibili = new it.cnr.jada.util.Collect(ass_inventario_uoDisponibili,"unita_organizzativa");
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 14.56.48)
 * @param newAssInvUoResp it.cnr.contab.inventario00.tabrif.bulk.Ass_inventario_uoBulk
 */
public void setAssInvUoResp(Ass_inventario_uoBulk newAssInvUoResp) {
	assInvUoResp = newAssInvUoResp;
}
}
