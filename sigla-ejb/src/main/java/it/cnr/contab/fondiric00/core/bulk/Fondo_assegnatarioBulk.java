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

package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_assegnatarioBulk extends Fondo_assegnatarioBase {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
public Fondo_assegnatarioBulk() {
	super();
}
public Fondo_assegnatarioBulk(java.lang.String cd_fondo,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo) {
	super(cd_fondo, uo.getCd_unita_organizzativa());
	setUnita_organizzativa(uo);
}
public java.lang.Integer getCd_responsabile_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
	if (responsabile == null)
		return null;
	return responsabile.getCd_terzo();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile() {
	return responsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.45)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.getResponsabile().setCd_terzo(cd_responsabile_terzo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setResponsabile(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile) {
	responsabile = newResponsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.45)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
