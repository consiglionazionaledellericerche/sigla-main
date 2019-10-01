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

public class Trattamento_coriKey extends OggettoBulk implements KeyedPersistent {
	// CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_contributo_ritenuta;

	// CD_TRATTAMENTO VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_trattamento;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Trattamento_coriKey() {
	super();
}
public Trattamento_coriKey(java.lang.String cd_contributo_ritenuta,java.lang.String cd_trattamento,java.sql.Timestamp dt_inizio_validita) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	this.cd_trattamento = cd_trattamento;
	this.dt_inizio_validita = dt_inizio_validita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Trattamento_coriKey)) return false;
	Trattamento_coriKey k = (Trattamento_coriKey)o;
	if(!compareKey(getCd_contributo_ritenuta(),k.getCd_contributo_ritenuta())) return false;
	if(!compareKey(getCd_trattamento(),k.getCd_trattamento())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_contributo_ritenuta
 */
public java.lang.String getCd_contributo_ritenuta() {
	return cd_contributo_ritenuta;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_contributo_ritenuta())+
		calculateKeyHashCode(getCd_trattamento())+
		calculateKeyHashCode(getDt_inizio_validita());
}
/* 
 * Setter dell'attributo cd_contributo_ritenuta
 */
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
}
