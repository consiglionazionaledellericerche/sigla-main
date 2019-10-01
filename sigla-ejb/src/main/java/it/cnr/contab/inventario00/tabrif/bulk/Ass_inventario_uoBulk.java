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

public class Ass_inventario_uoBulk extends Ass_inventario_uoBase {

	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
	private Id_inventarioBulk inventario;
public Ass_inventario_uoBulk() {
	super();
}
public Ass_inventario_uoBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Long pg_inventario) {
	super(cd_cds,cd_unita_organizzativa,pg_inventario);
	setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_unita_organizzativa));
	setInventario(new it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk(pg_inventario));
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2001 11.23.52)
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
 * Creation date: (16/11/2001 13.21.54)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2001 11.23.52)
 * @param newInventario it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public void setInventario(Id_inventarioBulk newInventario) {
	inventario = newInventario;
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getInventario().setPg_inventario(pg_inventario);
}
/**
 * Insert the method's description here.
 * Creation date: (16/11/2001 13.21.54)
 * @param newUnitaOrganizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnitaOrganizzativa) {
	unita_organizzativa = newUnitaOrganizzativa;
}
}
