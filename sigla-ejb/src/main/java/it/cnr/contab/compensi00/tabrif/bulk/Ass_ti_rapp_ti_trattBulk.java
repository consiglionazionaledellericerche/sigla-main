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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_trattBulk extends Ass_ti_rapp_ti_trattBase {

	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk();
	private Tipo_trattamentoBulk tipoTrattamento = new Tipo_trattamentoBulk();
public Ass_ti_rapp_ti_trattBulk() {
	super();
}
public Ass_ti_rapp_ti_trattBulk(java.lang.String cd_tipo_rapporto,java.lang.String cd_trattamento) {
	super(cd_tipo_rapporto,cd_trattamento);
	setTipoRapporto(new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk(cd_tipo_rapporto));
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
	if (tipoRapporto == null)
		return null;
	return tipoRapporto.getCd_tipo_rapporto();
}
public java.lang.String getCd_trattamento() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento = this.getTipoTrattamento();
	if (tipoTrattamento == null)
		return null;
	return tipoTrattamento.getCd_trattamento();
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipoRapporto() {
	return tipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.58)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public Tipo_trattamentoBulk getTipoTrattamento() {
	return tipoTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public boolean isROTipoRapporto(){
	return (getTipoRapporto() == null || getTipoRapporto().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public boolean isROTipoTrattamento(){
	return (getTipoTrattamento() == null || getTipoTrattamento().getCrudStatus() == OggettoBulk.NORMAL);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.getTipoTrattamento().setCd_trattamento(cd_trattamento);
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @param newTipoRapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public void setTipoRapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipoRapporto) {
	tipoRapporto = newTipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.58)
 * @param newTipoTrattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public void setTipoTrattamento(Tipo_trattamentoBulk newTipoTrattamento) {
	tipoTrattamento = newTipoTrattamento;
}
}
