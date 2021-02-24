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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class AssCatgrpInventVoceEpKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio;

	// CD_VOCE_EP VARCHAR(45) NOT NULL (PK)
	private String cd_voce_ep;
	private java.lang.String cd_categoria_gruppo;

	public AssCatgrpInventVoceEpKey() {
		super();
	}
	public AssCatgrpInventVoceEpKey(String cd_voce_ep, Integer esercizio, java.lang.String cd_categoria_gruppo) {
		super();
		this.cd_voce_ep = cd_voce_ep;
		this.esercizio = esercizio;
		this.cd_categoria_gruppo = cd_categoria_gruppo;
	}

	public String getCd_categoria_gruppo() {
		return cd_categoria_gruppo;
	}

	public void setCd_categoria_gruppo(String cd_categoria_gruppo) {
		this.cd_categoria_gruppo = cd_categoria_gruppo;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof AssCatgrpInventVoceEpKey)) return false;
		AssCatgrpInventVoceEpKey k = (AssCatgrpInventVoceEpKey)o;
		if(!compareKey(getCd_voce_ep(),k.getCd_voce_ep())) return false;
		if(!compareKey(getCd_categoria_gruppo(),k.getCd_categoria_gruppo())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		return true;
	}
	/*
	 * Getter dell'attributo cd_voce_ep
	 */
	public String getCd_voce_ep() {
		return cd_voce_ep;
	}
	/*
	 * Getter dell'attributo esercizio
	 */
	public Integer getEsercizio() {
		return esercizio;
	}
	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCd_voce_ep())+
						calculateKeyHashCode(getEsercizio())+
						calculateKeyHashCode(getCd_categoria_gruppo());
	}
	/*
	 * Setter dell'attributo cd_voce_ep
	 */
	public void setCd_voce_ep(String cd_voce_ep) {
		this.cd_voce_ep = cd_voce_ep;
	}
	/*
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
		return super.initialize(bp,context);
	}
}

