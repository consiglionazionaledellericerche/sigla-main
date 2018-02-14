package it.cnr.contab.bollo00.bulk;

import java.util.Optional;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

public class Atto_bolloBulk extends Atto_bolloBase implements AllegatoParentBulk {
	private static final long serialVersionUID = 1L;

	private Unita_organizzativaBulk unitaOrganizzativa;
	private Tipo_atto_bolloBulk tipoAttoBollo;
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	
	public Atto_bolloBulk() {
		super();
	}

	public Atto_bolloBulk(java.lang.Integer id) {
		super(id);
	}

	public Unita_organizzativaBulk getUnitaOrganizzativa() {
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
		 return new it.cnr.jada.bulk.BulkCollection[] {
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
}