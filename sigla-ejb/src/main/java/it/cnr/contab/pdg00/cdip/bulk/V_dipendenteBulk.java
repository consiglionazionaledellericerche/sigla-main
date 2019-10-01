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

package it.cnr.contab.pdg00.cdip.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_dipendenteBulk extends V_dipendenteBase {
	private it.cnr.jada.bulk.BulkList costi_per_elemento_voce;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
	public final static Dictionary tipo_naturaKeys = NaturaBulk.tipo_naturaKeys;
	
public V_dipendenteBulk() {
	super();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costi_per_elemento_voce'
 *
 * @return Il valore della proprietà 'costi_per_elemento_voce'
 */
public it.cnr.jada.bulk.BulkList getCosti_per_elemento_voce() {
	return costi_per_elemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'unita_organizzativa'
 *
 * @return Il valore della proprietà 'unita_organizzativa'
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'costi_per_elemento_voce'
 *
 * @param newCosti_per_elemento_voce	Il valore da assegnare a 'costi_per_elemento_voce'
 */
public void setCosti_per_elemento_voce(it.cnr.jada.bulk.BulkList newCosti_per_elemento_voce) {
	costi_per_elemento_voce = newCosti_per_elemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'unita_organizzativa'
 *
 * @param newUnita_organizzativa	Il valore da assegnare a 'unita_organizzativa'
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
