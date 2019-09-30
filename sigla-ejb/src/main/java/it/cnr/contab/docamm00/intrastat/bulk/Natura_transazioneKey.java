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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Natura_transazioneKey extends OggettoBulk implements KeyedPersistent {

	private java.lang.Long id_natura_transazione;

public Natura_transazioneKey() {
	super();
}
public Natura_transazioneKey(java.lang.Long id_natura_transazione) {
	super();
	this.id_natura_transazione = id_natura_transazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Natura_transazioneKey)) return false;
	Natura_transazioneKey k = (Natura_transazioneKey)o;
	if(!compareKey(getId_natura_transazione(),k.getId_natura_transazione())) return false;
	return true;
}
public java.lang.Long getId_natura_transazione() {
	return id_natura_transazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId_natura_transazione());
}

public void setId_natura_transazione(java.lang.Long id_natura_transazione) {
	this.id_natura_transazione = id_natura_transazione;
}
}
