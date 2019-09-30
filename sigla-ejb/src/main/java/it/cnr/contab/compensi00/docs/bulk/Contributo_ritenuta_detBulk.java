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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Contributo_ritenuta_detBulk extends Contributo_ritenuta_detBase {

	private Contributo_ritenutaBulk contributoRitenuta = new Contributo_ritenutaBulk();
public Contributo_ritenuta_detBulk() {
	super();
}
public Contributo_ritenuta_detBulk(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_riga,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_unita_organizzativa,esercizio,pg_compenso,pg_riga,ti_ente_percipiente);
	setContributoRitenuta(new it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk(cd_cds,cd_contributo_ritenuta,cd_unita_organizzativa,esercizio,pg_compenso,ti_ente_percipiente));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;

	return contributoRitenuta.getCd_cds();
}
public java.lang.String getCd_contributo_ritenuta() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;

	return contributoRitenuta.getCd_contributo_ritenuta();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;

	return contributoRitenuta.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 11.20.42)
 * @return it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk
 */
public Contributo_ritenutaBulk getContributoRitenuta() {
	return contributoRitenuta;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;

	return contributoRitenuta.getEsercizio();
}
public java.lang.Long getPg_compenso() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;

	return contributoRitenuta.getPg_compenso();
}
public java.lang.String getTi_ente_percipiente() {
	it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk contributoRitenuta = this.getContributoRitenuta();
	if (contributoRitenuta == null)
		return null;
	return contributoRitenuta.getTi_ente_percipiente();
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 11.20.42)
 * @param newContributoRitenuta it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk
 */
public void setContributoRitenuta(Contributo_ritenutaBulk newContributoRitenuta) {
	contributoRitenuta = newContributoRitenuta;
}
}
