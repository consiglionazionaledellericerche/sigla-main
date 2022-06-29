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

public class Classificazione_coriBulk extends Classificazione_coriBase {
	public final static String TIPO_ADDIZIONALE_COMUNALE_ACCONTO = "C1";
	public final static String TIPO_FISCALE = "FI";
	public final static String TIPO_PREVIDENZIALE = "PR";
	public final static String TIPO_IRAP = "IRAP";
	public final static String TIPO_ADDIZIONALE_REGIONALE = "R0";
	public final static String TIPO_ADDIZIONALE_PROVINCIALE = "P0";
	public final static String TIPO_ADDIZIONALE_COMUNALE = "C0";
	public final static String TIPO_INAIL = "IL";
	public final static String TIPO_IVA = "IV";
	public final static String TIPO_RIVALSA = "RV";
	public final static String TIPO_SPECIALE_STIPENDI = "ST";
	public final static String TIPO_ADDIZIONALE_REGIONALE_ADDEBITO_RATEIZZAZIONE = "R9";
	public final static java.lang.String TIPO_ADDIZIONALE_PROVINCIALE_ADDEBITO_RATEIZZAZIONE = "P9";
	public final static java.lang.String TIPO_ADDIZIONALE_COMUNALE_ADDEBITO_RATEIZZAZIONE = "C9";

	public Classificazione_coriBulk() {
		super();
	}
	public Classificazione_coriBulk(java.lang.String cd_classificazione_cori) {
		super(cd_classificazione_cori);
	}

	public boolean isTipoIva() {
		return Classificazione_coriBulk.TIPO_IVA.equals(this.getCd_classificazione_cori());
	}

	public boolean isTipoRivalsa() {
		return Classificazione_coriBulk.TIPO_RIVALSA.equals(this.getCd_classificazione_cori());
	}
}
