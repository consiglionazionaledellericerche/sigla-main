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
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.si.service.dto.anagrafica.enums.TipoContratto;

import java.util.HashMap;
import java.util.Map;

public class Tipo_incaricoBulk extends Tipo_incaricoBase {
    public final static java.util.Dictionary <String,String> TIPI_ASSOCIAZIONE;
    public final static String ASS_INCARICHI = "INC";
    public final static String ASS_BORSE_STUDIO = "BST";
    public final static String ASS_ASSEGNI_RICERCA = "ASR";

	static {
		TIPI_ASSOCIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_ASSOCIAZIONE.put(ASS_INCARICHI,"Incarichi");
		TIPI_ASSOCIAZIONE.put(ASS_BORSE_STUDIO,"Borse di Studio");
		TIPI_ASSOCIAZIONE.put(ASS_ASSEGNI_RICERCA,"Assegni di Ricerca");
	}

	public static Map<String, TipoContratto> TIPOCONTRATTO_ACE = new HashMap<String, TipoContratto>(){
		{
			put(ASS_BORSE_STUDIO, TipoContratto.BORSISTA);
			put(ASS_ASSEGNI_RICERCA, TipoContratto.ASSEGNISTA);
			put(ASS_INCARICHI, TipoContratto.COLLABORATORE_PROFESSIONALE);
		}
	};

	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk();
	
	public Tipo_incaricoBulk() {
		super();
	}
	public Tipo_incaricoBulk(java.lang.String cd_tipo_incarico) {
		super(cd_tipo_incarico);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}
	public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipoRapporto() {
		return tipoRapporto;
	}
	public void setTipoRapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipoRapporto) {
		tipoRapporto = newTipoRapporto;
	}
	public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
		this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
	}
	public java.lang.String getCd_tipo_rapporto() {
		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
		if (tipoRapporto == null)
			return null;
		return tipoRapporto.getCd_tipo_rapporto();
	}
	public java.util.Dictionary getTipiAssociazioneKeys(){
		return TIPI_ASSOCIAZIONE;
	}
}