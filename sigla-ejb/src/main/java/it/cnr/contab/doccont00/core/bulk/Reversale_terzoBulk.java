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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Reversale_terzoBulk extends Reversale_terzoBase {
	protected it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk tipoBollo;
	protected it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo;
	protected ReversaleIBulk reversaleI = new ReversaleIBulk();

public Reversale_terzoBulk() {
	super();
}
public Reversale_terzoBulk(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.Integer esercizio,java.lang.Long pg_reversale) {
	super(cd_cds,cd_terzo,esercizio,pg_reversale);
	setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
	setReversaleI(new it.cnr.contab.doccont00.core.bulk.ReversaleIBulk(cd_cds,esercizio,pg_reversale));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversaleI();
	if (reversale == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = reversale.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
public java.lang.String getCd_tipo_bollo() {
	it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk tipoBollo = this.getTipoBollo();
	if (tipoBollo == null)
		return null;
	return tipoBollo.getCd_tipo_bollo();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversaleI();
	if (reversale == null)
		return null;
	return reversale.getEsercizio();
}
public java.lang.Long getPg_reversale() {
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversaleI();
	if (reversale == null)
		return null;
	return reversale.getPg_reversale();
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleIBulk
 */
public ReversaleIBulk getReversaleI() {
	return reversaleI;
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
/**
 * @return it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk
 */
public it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk getTipoBollo() {
	return tipoBollo;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getReversaleI().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
}
public void setCd_tipo_bollo(java.lang.String cd_tipo_bollo) {
	this.getTipoBollo().setCd_tipo_bollo(cd_tipo_bollo);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getReversaleI().setEsercizio(esercizio);
}
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.getReversaleI().setPg_reversale(pg_reversale);
}
/**
 * @param newReversaleI it.cnr.contab.doccont00.core.bulk.ReversaleIBulk
 */
public void setReversaleI(ReversaleIBulk newReversaleI) {
	reversaleI = newReversaleI;
}
/**
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
/**
 * @param newTipoBollo it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk
 */
public void setTipoBollo(it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk newTipoBollo) {
	tipoBollo = newTipoBollo;
}
}
