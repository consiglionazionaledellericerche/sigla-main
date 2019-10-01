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

package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ordine_dettBulk extends Ordine_dettBase {

	private OrdineBulk ordine;
public Ordine_dettBulk() {
	super();
}
public Ordine_dettBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_ordine) {
	super(cd_cds,esercizio,pg_dettaglio,pg_ordine);
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.ordine.bulk.OrdineBulk ordine = this.getOrdine();
	if (ordine == null)
		return null;
	return ordine.getCd_cds();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.ordine.bulk.OrdineBulk ordine = this.getOrdine();
	if (ordine == null)
		return null;
	return ordine.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 16.16.53)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImponibile() {
	if ((getIm_unitario()==null)||(getQuantita()==null))
		return new java.math.BigDecimal(0);
	return getIm_unitario().multiply(getQuantita());
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 16.16.53)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoTotale() {
	if (getIm_iva()==null)
		return getImponibile();
	return getImponibile().add(getIm_iva());
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 17.15.29)
 * @return it.cnr.contab.doccont00.ordine.bulk.OrdineBulk
 */
public OrdineBulk getOrdine() {
	return ordine;
}
public java.lang.Long getPg_ordine() {
	it.cnr.contab.doccont00.ordine.bulk.OrdineBulk ordine = this.getOrdine();
	if (ordine == null)
		return null;
	return ordine.getPg_ordine();
}
public void setCd_cds(java.lang.String cd_cds) {
	if (getOrdine() != null)
	  this.getOrdine().setCd_cds(cd_cds);
}
public void setEsercizio(java.lang.Integer esercizio) {
	if (getOrdine() != null)
	  this.getOrdine().setEsercizio(esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 17.15.29)
 * @param newOrdine it.cnr.contab.doccont00.ordine.bulk.OrdineBulk
 */
public void setOrdine(OrdineBulk newOrdine) {
	ordine = newOrdine;
}
public void setPg_ordine(java.lang.Long pg_ordine) {
	if (getOrdine() != null)
	  this.getOrdine().setPg_ordine(pg_ordine);
}
}
