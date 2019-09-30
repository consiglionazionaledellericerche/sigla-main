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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
public class Tipo_norma_perlaBulk extends Tipo_norma_perlaBase {
    public final static java.util.Dictionary <String,String> TIPI_ASSOCIAZIONE;
    public final static String ASS_INCARICHI = "INC";
    public final static String ASS_CONTRATTI = "CTR";
	
	static {
		TIPI_ASSOCIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_ASSOCIAZIONE.put(ASS_INCARICHI,"Incarichi");
		TIPI_ASSOCIAZIONE.put(ASS_CONTRATTI,"Borse di Studio");
	}

	public Tipo_norma_perlaBulk() {
		super();
	}
	public Tipo_norma_perlaBulk(java.lang.String cd_tipo_norma_perla) {
		super(cd_tipo_norma_perla);
	}
	public java.util.Dictionary getTipiAssociazioneKeys(){
		return TIPI_ASSOCIAZIONE;
	}
}