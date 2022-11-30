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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Report_statoBulk extends Report_statoBase {

	private it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = null;
public Report_statoBulk() {
	super();
}
public Report_statoBulk(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_documento,java.lang.String tipo_report) {
	super(cd_cds,cd_tipo_sezionale,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,stato,ti_documento,tipo_report);
}
public void completeFrom(Stampa_registri_ivaVBulk stampa) {

	setCd_cds(stampa.getCd_cds());
	setTipo_sezionale(stampa.getTipo_sezionale());
	setCd_unita_organizzativa(stampa.getCd_unita_organizzativa());
	setEsercizio(stampa.getEsercizio());
	setTi_documento(stampa.getTipo_documento_stampato());
	setTipo_report(stampa.getTipo_report_stampato());
	setDt_inizio(stampa.getData_da());
	setDt_fine(stampa.getData_a());
}
public java.lang.String getCd_tipo_sezionale() {
	it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = this.getTipo_sezionale();
	if (tipo_sezionale == null)
		return null;
	return tipo_sezionale.getCd_tipo_sezionale();
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 12:51:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk getTipo_sezionale() {
	return tipo_sezionale;
}
public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
	this.getTipo_sezionale().setCd_tipo_sezionale(cd_tipo_sezionale);
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 12:51:48 PM)
 * @param newTipo_sezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public void setTipo_sezionale(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk newTipo_sezionale) {
	tipo_sezionale = newTipo_sezionale;
}
}
