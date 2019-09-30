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

import java.util.Dictionary;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

public class Voce_piano_economico_prgBulk extends Voce_piano_economico_prgBase {
	private Unita_organizzativaBulk unita_organizzativa;

	public final static Dictionary tipoVoceEconomicaKeys;

	public final static String PERSONALE_DETER = "PTD";
	public final static String PERSONALE_INDET = "PTI";
	public final static String PERSONALE_OTHER = "POT";
	
	static {
		tipoVoceEconomicaKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoVoceEconomicaKeys.put(PERSONALE_DETER,"Personale a tempo determinato");
		tipoVoceEconomicaKeys.put(PERSONALE_INDET,"Personale a tempo indeterminato");
		tipoVoceEconomicaKeys.put(PERSONALE_OTHER,"Altro tipo di personale");
	}

	public Voce_piano_economico_prgBulk() {
		super();
	}

	public Voce_piano_economico_prgBulk(java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano) {
		super(cd_unita_organizzativa, cd_voce_piano);
	}
	
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	
	public void setUnita_organizzativa(Unita_organizzativaBulk unita_organizzativa) {
		this.unita_organizzativa = unita_organizzativa;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
		if (unita_organizzativa == null)
			return null;
		return unita_organizzativa.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	public static Dictionary getTipovoceeconomicakeys() {
		return tipoVoceEconomicaKeys;
	}
	

	public boolean isVocePersonaleTempoDeterminato() {
		return PERSONALE_DETER.equals(this.getTipologia());
	}
	
	public boolean isVocePersonaleTempoIndeterminato() {
		return PERSONALE_INDET.equals(this.getTipologia());
	}

	public boolean isVocePersonaleAltraTipologia() {
		return PERSONALE_OTHER.equals(this.getTipologia());
	}

	public boolean isVocePersonale() {
		return this.isVocePersonaleTempoDeterminato()||
				this.isVocePersonaleTempoIndeterminato()||
				this.isVocePersonaleAltraTipologia();
	}
}
