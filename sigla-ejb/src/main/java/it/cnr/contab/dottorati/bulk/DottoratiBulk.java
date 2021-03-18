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

package it.cnr.contab.dottorati.bulk;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

import java.util.Optional;

public class DottoratiBulk extends DottoratiBase {
	private static final long serialVersionUID = 1L;

	/**private Unita_organizzativaBulk unitaOrganizzativa;
	private Tipo_atto_bolloBulk tipoAttoBollo;
	private ContrattoBulk contratto = new ContrattoBulk();	
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	
	private boolean flContrattoRegistrato = Boolean.FALSE;*/
	
	public DottoratiBulk() {
		super();
	}

	public DottoratiBulk(Integer id) {
		super(id);
	}

/**	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}

	@Override
	public String getCdUnitaOrganizzativa() {
		return Optional.ofNullable(getUnitaOrganizzativa())
				.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
				.orElse(null);
	}

	@Override
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		Optional.ofNullable(getUnitaOrganizzativa()).ifPresent(el->el.setCd_unita_organizzativa(cdUnitaOrganizzativa));
	}

	public Tipo_atto_bolloBulk getTipoAttoBollo() {
		return tipoAttoBollo;
	}

	public void setTipoAttoBollo(Tipo_atto_bolloBulk tipoAttoBollo) {
		this.tipoAttoBollo = tipoAttoBollo;
	}

	@Override
	public Integer getIdTipoAttoBollo() {
		return Optional.ofNullable(getTipoAttoBollo())
					.map(Tipo_atto_bolloBulk::getId)
					.orElse(null);
	}

	@Override
	public void setIdTipoAttoBollo(Integer idTipoAttoBollo) {
		Optional.ofNullable(getTipoAttoBollo()).ifPresent(el->el.setId(idTipoAttoBollo));
	}

	public boolean isRONumFogli() {
		return Optional.ofNullable(getNumRighe()).filter(el->el>0).isPresent();
	}

	public BulkCollection[] getBulkLists() {
		 return new BulkCollection[] {
				 archivioAllegati };
	}

	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}

	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;
	}

	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}

	public void setArchivioAllegati(
			BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}

	public String getCdTipoAttoBollo(){
		return Optional.ofNullable(getTipoAttoBollo())
				.map(Tipo_atto_bolloBulk::getCodice)
				.orElse(null);
	}

	public void setCdTipoAttoBollo(String cdTipoAttoBollo) {
		Optional.ofNullable(getTipoAttoBollo()).ifPresent(el->el.setCodice(cdTipoAttoBollo));
	}

	public ContrattoBulk getContratto() {
		return contratto;
	}

	public void setContratto(ContrattoBulk contratto) {
		this.contratto = contratto;
	}

 	public Integer getEsercizio_contratto() {
		return Optional.ofNullable(this.getContratto()).map(ContrattoBulk::getEsercizio).orElse(null);
	}

	public void setEsercizio_contratto(Integer esercizio) {
		Optional.ofNullable(getContratto()).ifPresent(el->el.setEsercizio(esercizio));
	}

	public String getStato_contratto() {
		return Optional.ofNullable(this.getContratto()).map(ContrattoBulk::getStato).orElse(null);
	}

	public void setStato_contratto(String stato) {
		Optional.ofNullable(getContratto()).ifPresent(el->el.setStato(stato));
	}

	public Long getPg_contratto() {
		return Optional.ofNullable(this.getContratto()).map(ContrattoBulk::getPg_contratto).orElse(null);
	}

	public void setPg_contratto(Long pg_contratto) {
		Optional.ofNullable(getContratto()).ifPresent(el->el.setPg_contratto(pg_contratto));
	}
	
	public boolean isFlContrattoRegistrato() {
		return flContrattoRegistrato;
	}
	
	public void setFlContrattoRegistrato(boolean flContrattoRegistrato) {
		this.flContrattoRegistrato = flContrattoRegistrato;
	}*/
}