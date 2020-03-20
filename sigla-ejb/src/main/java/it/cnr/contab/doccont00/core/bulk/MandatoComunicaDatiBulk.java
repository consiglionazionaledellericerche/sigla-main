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

import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.util.*;

public class MandatoComunicaDatiBulk extends MandatoBulk {
	private String cdLiv4;
	private String dsdLiv4;
	private String denominazioneSede;

	public String getCdLiv4() {
		return cdLiv4;
	}

	public void setCdLiv4(String cdLiv4) {
		this.cdLiv4 = cdLiv4;
	}

	public String getDsdLiv4() {
		return dsdLiv4;
	}

	public void setDsdLiv4(String dsdLiv4) {
		this.dsdLiv4 = dsdLiv4;
	}

	public String getDenominazioneSede() {
		return denominazioneSede;
	}

	public void setDenominazioneSede(String denominazioneSede) {
		this.denominazioneSede = denominazioneSede;
	}

	public BigDecimal getImportoCapitolo() {
		return importoCapitolo;
	}

	public void setImportoCapitolo(BigDecimal importoCapitolo) {
		this.importoCapitolo = importoCapitolo;
	}

	private BigDecimal importoCapitolo;

	public MandatoComunicaDatiBulk() {
	super();
}
public MandatoComunicaDatiBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
}
