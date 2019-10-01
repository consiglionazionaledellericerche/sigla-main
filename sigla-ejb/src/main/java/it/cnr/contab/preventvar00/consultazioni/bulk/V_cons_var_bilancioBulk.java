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

/*
* Created by Generator 1.0
* Date 23/05/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_var_bilancioBulk extends V_cons_var_bilancioBase {

	private final static java.util.Dictionary TIPO_GESTIONE;
	public final static String ENTRATA = Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_ENTRATA;
	public final static String SPESA	 = Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_SPESA;
	public final static String ENTRATA_SPESA = "X";

	static{
		TIPO_GESTIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPO_GESTIONE.put(ENTRATA, 		 "Entrata");
		TIPO_GESTIONE.put(SPESA,   		 "Spesa"  );
		TIPO_GESTIONE.put(ENTRATA_SPESA, "Entrambi");
	}

    protected final static java.util.Dictionary <String,String> TIPI_VARIAZIONE;
    public final static String STORNO_E = "STORNO_E";
    public final static String STORNO_S = "STORNO_S";
    public final static String VAR_QUADRATURA = "VAR_QUAD";
    public final static String VAR_LIBERA = "VAR_LIBERA";
    public final static String VAR_ECO = "VAR_ECO";
	public final static String VAR_MSP = "VAR_MSP";

	static {
		TIPI_VARIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_VARIAZIONE.put(STORNO_E,"Storno tra entrate");
		TIPI_VARIAZIONE.put(STORNO_S,"Storno tra spese");
		TIPI_VARIAZIONE.put(VAR_QUADRATURA,"Variazione a quadratura");
		TIPI_VARIAZIONE.put(VAR_ECO,"Economie");
		TIPI_VARIAZIONE.put(VAR_MSP,"Maggiori spese");		
	}

    protected final static java.util.Dictionary <String,String> STATO;
    public final static String STATO_P = "P";
    public final static String STATO_D = "D";

	static {
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_P,"Provvisoria");
		STATO.put(STATO_D,"Definitiva");
	}

	public V_cons_var_bilancioBulk() {
		super();
	}

public java.util.Dictionary getTipoGestioneKeys() {
	return TIPO_GESTIONE;
}

public java.util.Dictionary getTipiVariazioneKeys(){
	return TIPI_VARIAZIONE;
}

public java.util.Dictionary getStatoKeys() {
	return STATO;
}
}