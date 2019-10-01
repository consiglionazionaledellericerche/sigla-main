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

package it.cnr.contab.inventario00.docs.bulk;

import java.util.*;

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;



public class Inventario_utilizzatori_laBulk extends Inventario_utilizzatori_laBase {

	private WorkpackageBulk linea_attivita;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
	//private Buono_carico_scaricoBulk buono_cs;
	private it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk dettaglio;
	private Inventario_beniBulk bene;
	private Utilizzatore_CdrVBulk vUtilizzatore;
public Inventario_utilizzatori_laBulk() {
	super();
}
public Inventario_utilizzatori_laBulk(java.lang.String cd_linea_attivita,java.lang.String cd_utilizzatore_cdr,java.lang.Long nr_inventario,java.lang.Long pg_inventario,java.lang.Long progressivo) {
	super(cd_linea_attivita,cd_utilizzatore_cdr,nr_inventario,pg_inventario,progressivo);
	setBene(new it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk(nr_inventario,pg_inventario,progressivo));
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_utilizzatore_cdr,cd_linea_attivita));
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2001 2:42:18 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public Inventario_beniBulk getBene() {
	return bene;
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
}
public java.lang.String getCd_utilizzatore_cdr() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = linea_attivita.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
/**
 * Insert the method's description here.
 * Creation date: (11/27/2001 5:21:23 PM)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * Insert the method's description here.
 * Creation date: (1/10/2002 3:49:38 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Buono_carico_dettBulk
 */
public Buono_carico_scarico_dettBulk getDettaglio() {
	return dettaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (11/27/2001 5:21:23 PM)
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita() {
	return linea_attivita;
}
public java.lang.Long getNr_inventario() {
	it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk bene = this.getBene();
	if (bene == null)
		return null;
	return bene.getNr_inventario();
}
public java.lang.Long getPg_inventario() {
	it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk bene = this.getBene();
	if (bene == null)
		return null;
	it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = bene.getInventario();
	if (inventario == null)
		return null;
	return inventario.getPg_inventario();
}
public java.lang.Long getProgressivo() {
	it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk bene = this.getBene();
	if (bene == null)
		return null;
	return bene.getProgressivo();
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/2001 6:15:15 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk
 */
public Utilizzatore_CdrVBulk getVUtilizzatore() {
	return vUtilizzatore;
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/2001 6:15:15 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk
 */
public boolean isROlinea_attivita() {
	
	return getLinea_attivita() == null ||
			getLinea_attivita().getCrudStatus() ==  OggettoBulk.NORMAL;
			
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2001 2:42:18 PM)
 * @param newBene it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public void setBene(Inventario_beniBulk newBene) {
	bene = newBene;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 9:46:13 AM)
 * @param newBuono_cs it.cnr.contab.inventario00.docs.bulk.Buono_carico_scaricoBulk
 */
public void setBuono_cs_bene(Inventario_beniBulk newBene) {
	bene = newBene;
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
public void setCd_utilizzatore_cdr(java.lang.String cd_utilizzatore_cdr) {
	if (getLinea_attivita()!=null)
		this.getLinea_attivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_utilizzatore_cdr);
}
/**
 * Insert the method's description here.
 * Creation date: (11/27/2001 5:21:23 PM)
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
/**
 * Insert the method's description here.
 * Creation date: (1/10/2002 3:49:38 PM)
 * @param newDettaglio it.cnr.contab.inventario00.docs.bulk.Buono_carico_dettBulk
 */
public void setDettaglio(Buono_carico_scarico_dettBulk newDettaglio) {
	dettaglio = newDettaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (11/27/2001 5:21:23 PM)
 * @param newLinea_attivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLinea_attivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita) {
	linea_attivita = newLinea_attivita;
}
public void setNr_inventario(java.lang.Long nr_inventario) {
	this.getBene().setNr_inventario(nr_inventario);
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getBene().getInventario().setPg_inventario(pg_inventario);
}
public void setProgressivo(java.lang.Long progressivo) {
	this.getBene().setProgressivo(progressivo);
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/2001 6:15:15 PM)
 * @param newVUtilizzatore it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk
 */
public void setVUtilizzatore(Utilizzatore_CdrVBulk newVUtilizzatore) {
	vUtilizzatore = newVUtilizzatore;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 9:46:13 AM)
 * @param newBuono_cs it.cnr.contab.inventario00.docs.bulk.Buono_carico_scaricoBulk
 */
public void setVUtilizzatore_cdr(Utilizzatore_CdrVBulk newVUtilizzatore) {
	vUtilizzatore = newVUtilizzatore;
}
}
