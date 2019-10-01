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

public class Ass_ti_rapp_ti_prestKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_rapporto;

	// CD_TI_PRESTAZIONE VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_ti_prestazione;

public Ass_ti_rapp_ti_prestKey() {
	super();
}
public Ass_ti_rapp_ti_prestKey(java.lang.String cd_tipo_rapporto,java.lang.String cd_ti_prestazione) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
	this.cd_ti_prestazione = cd_ti_prestazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_ti_rapp_ti_prestKey)) return false;
	Ass_ti_rapp_ti_prestKey k = (Ass_ti_rapp_ti_prestKey)o;
	if(!compareKey(getCd_tipo_rapporto(),k.getCd_tipo_rapporto())) return false;
	if(!compareKey(getCd_ti_prestazione(),k.getCd_ti_prestazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo cd_ti_prestazione
 */
public java.lang.String getCd_ti_prestazione() {
	return cd_ti_prestazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_rapporto())+
		calculateKeyHashCode(getCd_ti_prestazione());
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo cd_ti_prestazione
 */
public void setCd_ti_prestazione(java.lang.String cd_ti_prestazione) {
	this.cd_ti_prestazione = cd_ti_prestazione;
}
}
