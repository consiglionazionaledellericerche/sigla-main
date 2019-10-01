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

package it.cnr.contab.progettiric00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Voce_piano_economico_prgKey extends OggettoBulk implements KeyedPersistent {
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_voce_piano;

	public Voce_piano_economico_prgKey() {
		super();
	}
	
	public Voce_piano_economico_prgKey(java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano) {
		super();
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.cd_voce_piano = cd_voce_piano;
	}
	
	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}
	
	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Voce_piano_economico_prgKey)) return false;
		Voce_piano_economico_prgKey k = (Voce_piano_economico_prgKey)o;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getCd_voce_piano(),k.getCd_voce_piano())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getCd_voce_piano());
	}
}
