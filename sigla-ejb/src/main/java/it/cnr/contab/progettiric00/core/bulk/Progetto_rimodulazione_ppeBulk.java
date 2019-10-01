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

package it.cnr.contab.progettiric00.core.bulk;

import java.util.Optional;
import java.util.stream.Stream;

import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.util.OrderedHashtable;

public class Progetto_rimodulazione_ppeBulk extends Progetto_rimodulazione_ppeBase {
	private Progetto_rimodulazioneBulk progettoRimodulazione;
	private Voce_piano_economico_prgBulk vocePianoEconomico;
	
	public Progetto_rimodulazione_ppeBulk() {
		super();
	}

	public Progetto_rimodulazione_ppeBulk(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super(pg_progetto, pg_rimodulazione, cd_unita_organizzativa, cd_voce_piano, esercizio_piano);
	}
	
	public Progetto_rimodulazioneBulk getProgettoRimodulazione() {
		return progettoRimodulazione;
	}
	
	public void setProgettoRimodulazione(Progetto_rimodulazioneBulk progettoRimodulazione) {
		this.progettoRimodulazione = progettoRimodulazione;
	}
	
	@Override
	public Integer getPg_progetto() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_progetto).orElse(null);
	}
	
	@Override
	public void setPg_progetto(Integer pg_progetto) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_progetto(pg_progetto);	
		});
	}

	@Override
	public Integer getPg_rimodulazione() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_rimodulazione).orElse(null);
	}
	
	@Override
	public void setPg_rimodulazione(Integer pg_rimodulazione) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_rimodulazione(pg_rimodulazione);	
		});
	}

	public Voce_piano_economico_prgBulk getVocePianoEconomico() {
		return vocePianoEconomico;
	}
	
	public void setVocePianoEconomico(Voce_piano_economico_prgBulk vocePianoEconomico) {
		this.vocePianoEconomico = vocePianoEconomico;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return Optional.ofNullable(this.getVocePianoEconomico()).map(Voce_piano_economico_prgBulk::getCd_unita_organizzativa).orElse(null);
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		Optional.ofNullable(this.getVocePianoEconomico()).ifPresent(el->{
			el.setCd_unita_organizzativa(cd_unita_organizzativa);
		});
	}
	
	@Override
	public String getCd_voce_piano() {
		return Optional.ofNullable(this.getVocePianoEconomico()).map(Voce_piano_economico_prgBulk::getCd_voce_piano).orElse(null);
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		Optional.ofNullable(this.getVocePianoEconomico()).ifPresent(el->{
			el.setCd_voce_piano(cd_voce_piano);
		});
	}
	
	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		OrderedHashtable list = new OrderedHashtable();
		for (int i=this.getProgettoRimodulazione().getProgetto().getAnnoFineOf().intValue();i>=this.getProgettoRimodulazione().getProgetto().getAnnoInizioOf();i--)
			list.put(new Integer(i), new Integer(i));
		if (this.getEsercizio_piano()!=null && list.get(this.getEsercizio_piano())==null)
			list.put(this.getEsercizio_piano(), this.getEsercizio_piano());
		list.remove(this.getProgettoRimodulazione().getProgetto().getEsercizio());
		Optional.ofNullable(this.getProgettoRimodulazione().getProgetto())
				.flatMap(el->Optional.ofNullable(el.getPdgModuli()))
				.map(el->el.stream())
				.orElse(Stream.empty())
				.filter(el->Optional.ofNullable(el.getStato()).map(stato->!stato.equals(Pdg_moduloBulk.STATO_AC)).orElse(Boolean.FALSE))
				.forEach(el->list.remove(el));
		return list;
	}
}