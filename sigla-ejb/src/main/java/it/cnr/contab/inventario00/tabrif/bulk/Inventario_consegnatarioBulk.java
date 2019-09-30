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


import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_consegnatarioBulk extends Inventario_consegnatarioBase {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk consegnatario;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk delegato;
	private Id_inventarioBulk inventario;
	private java.lang.String ds_consegnatario;
	private java.lang.String ds_delegato;
	
public Inventario_consegnatarioBulk() {
	super();
}
public Inventario_consegnatarioBulk(java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_inventario) {
	super(dt_inizio_validita,pg_inventario);
}
public java.lang.Integer getCd_consegnatario() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk consegnatario = this.getConsegnatario();
	if (consegnatario == null)
		return null;
	return consegnatario.getCd_terzo();
}
public java.lang.Integer getCd_delegato() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk delegato = this.getDelegato();
	if (delegato == null)
		return null;
	return delegato.getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.12.15)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getConsegnatario() {
	return consegnatario;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDataFineValidita() {
	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.21.22)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getDelegato() {
	return delegato;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.39.59)
 * @return int
 */
public String getDs_consegnatario() {
	if( (consegnatario == null) || (consegnatario.getAnagrafico() == null) )
		return null;
	return consegnatario.getAnagrafico().getDescrizioneAnagrafica();
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.41.56)
 * @return java.lang.String
 */
public java.lang.String getDs_delegato() {
	if( (delegato == null) || (delegato.getAnagrafico() == null) )
		return null;
	
	return delegato.getAnagrafico().getDescrizioneAnagrafica();

}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.22.28)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public Id_inventarioBulk getInventario() {
	return inventario;
}
public java.lang.Long getPg_inventario() {
	it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = this.getInventario();
	if (inventario == null)
		return null;
	return inventario.getPg_inventario();
}
/**
 * Insert the method's description here.
 * Creation date: (13/09/2002 10.18.59)
 * @return boolean
 */
public boolean isLast() {

	if (getDt_fine_validita() == null)
		return true;
		
	return false;
}
public void setCd_consegnatario(java.lang.Integer cd_consegnatario) {
	this.getConsegnatario().setCd_terzo(cd_consegnatario);
}
public void setCd_delegato(java.lang.Integer cd_delegato) {
	this.getDelegato().setCd_terzo(cd_delegato);
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.12.15)
 * @param newConsegnatario it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setConsegnatario(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newConsegnatario) {
	consegnatario = newConsegnatario;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDataFineValidita(java.sql.Timestamp dt_fine_validita) {
	
	this.setDt_fine_validita(dt_fine_validita);
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.21.22)
 * @param newDelegato it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setDelegato(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newDelegato) {
	delegato = newDelegato;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.39.59)
 * @param newDs_consegnatario java.lang.String
 */
public void setDs_consegnatario(java.lang.String newDs_consegnatario) {
	ds_consegnatario = newDs_consegnatario;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 17.41.56)
 * @param newDs_delegato java.lang.String
 */
public void setDs_delegato(java.lang.String newDs_delegato) {
	ds_delegato = newDs_delegato;
}
/**
 * Insert the method's description here.
 * Creation date: (04/12/2001 12.22.28)
 * @param newInventario it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public void setInventario(Id_inventarioBulk newInventario) {
	inventario = newInventario;
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getInventario().setPg_inventario(pg_inventario);
}
}
