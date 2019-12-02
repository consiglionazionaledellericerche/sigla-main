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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_partner_esternoBulk extends Progetto_partner_esternoBase {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk partner_esterno;
	
public Progetto_partner_esternoBulk() {
	super();
}
public Progetto_partner_esternoBulk(java.lang.Integer pg_progetto,it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo) {
	super(pg_progetto, terzo.getCd_terzo());
	setPartner_esterno(terzo);
}
public java.lang.Integer getCd_partner_esterno() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk partner_esterno = this.getPartner_esterno();
	if (partner_esterno == null)
		return null;
	return partner_esterno.getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getPartner_esterno() {
	return partner_esterno;
}
public void setCd_partner_esterno(java.lang.Integer cd_partner_esterno) {
	this.getPartner_esterno().setCd_terzo(cd_partner_esterno);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setPartner_esterno(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newPartner_esterno) {
	partner_esterno = newPartner_esterno;
}

}