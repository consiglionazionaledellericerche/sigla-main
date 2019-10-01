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

public class Tipologia_rischioKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPOLOGIA_RISCHIO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipologia_rischio;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Tipologia_rischioKey() {
	super();
}
public Tipologia_rischioKey(java.lang.String cd_tipologia_rischio,java.sql.Timestamp dt_inizio_validita) {
	super();
	this.cd_tipologia_rischio = cd_tipologia_rischio;
	this.dt_inizio_validita = dt_inizio_validita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipologia_rischioKey)) return false;
	Tipologia_rischioKey k = (Tipologia_rischioKey)o;
	if(!compareKey(getCd_tipologia_rischio(),k.getCd_tipologia_rischio())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipologia_rischio
 */
public java.lang.String getCd_tipologia_rischio() {
	return cd_tipologia_rischio;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipologia_rischio())+
		calculateKeyHashCode(getDt_inizio_validita());
}
/* 
 * Setter dell'attributo cd_tipologia_rischio
 */
public void setCd_tipologia_rischio(java.lang.String cd_tipologia_rischio) {
	this.cd_tipologia_rischio = cd_tipologia_rischio;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
}
