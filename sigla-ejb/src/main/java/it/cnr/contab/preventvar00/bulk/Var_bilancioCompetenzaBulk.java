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

package it.cnr.contab.preventvar00.bulk;

public class Var_bilancioCompetenzaBulk extends Var_bilancioBulk {
    protected final static java.util.Dictionary <String,String> TIPI_VARIAZIONE;
	static {
		TIPI_VARIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_VARIAZIONE.put(STORNO_E,"Storno tra entrate");
		TIPI_VARIAZIONE.put(STORNO_S,"Storno tra spese");
		TIPI_VARIAZIONE.put(VAR_QUADRATURA,"Variazione a quadratura");
	}

	public Var_bilancioCompetenzaBulk() {
		super();
	}

	public Var_bilancioCompetenzaBulk(String cd_cds, Integer esercizio,
			Long pg_variazione, String ti_appartenenza) {
		super(cd_cds, esercizio, pg_variazione, ti_appartenenza);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'tipiVariazioneKeys'
	 *
	 * @return Il valore della proprietà 'tipiVariazioneKeys'
	 */
	public java.util.Dictionary getTipiVariazioneKeys(){
		return TIPI_VARIAZIONE;
	}	
	protected String getCompetenzaResiduo() {
		return "C";
	}

}
