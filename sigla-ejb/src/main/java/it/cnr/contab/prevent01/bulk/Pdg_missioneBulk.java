/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkCollection;

@SuppressWarnings("rawtypes") 
public class Pdg_missioneBulk extends Pdg_missioneBase {
	private static final long serialVersionUID = 1L;

	private it.cnr.jada.bulk.BulkList assPdgMissioneTipoUoColl = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList tipiUoAssociabili  = new it.cnr.jada.bulk.BulkList();

	public Pdg_missioneBulk() {
		super();
	}
	
	public Pdg_missioneBulk(java.lang.String cd_missione) {
		super(cd_missione);
	}

	public it.cnr.jada.bulk.BulkList getTipiUoAssociabili() {
		return tipiUoAssociabili;
	}
	
	public void setTipiUoAssociabili(it.cnr.jada.bulk.BulkList tipiUoAssociabili) {
		this.tipiUoAssociabili = tipiUoAssociabili;
	}
	
	public it.cnr.jada.bulk.BulkList getAssPdgMissioneTipoUoColl() {
		return assPdgMissioneTipoUoColl;
	}
	
	public void setAssPdgMissioneTipoUoColl(it.cnr.jada.bulk.BulkList assPdgMissioneTipoUoColl) {
		this.assPdgMissioneTipoUoColl = assPdgMissioneTipoUoColl;
	}
	
	@SuppressWarnings("unchecked")
	public int addToAssPdgMissioneTipoUoColl( Ass_pdg_missione_tipo_uoBulk ass ) {
		assPdgMissioneTipoUoColl.add(ass);
		ass.setPdgMissione(this);
		return assPdgMissioneTipoUoColl.size()-1;
	}
	
	public Ass_pdg_missione_tipo_uoBulk removeFromAssPdgMissioneTipoUoColl(int index) {
		return (Ass_pdg_missione_tipo_uoBulk)assPdgMissioneTipoUoColl.remove(index);
	}

	@SuppressWarnings("unchecked")
	public int addToTipiUoAssociabili( Tipo_unita_organizzativaBulk tipoUo ) {
		tipiUoAssociabili.add(tipoUo);
		return tipiUoAssociabili.size()-1;
	}
	
	public Tipo_unita_organizzativaBulk removeFromTipiUoAssociabili(int index) {
		return (Tipo_unita_organizzativaBulk)tipiUoAssociabili.remove(index);
	}

	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 assPdgMissioneTipoUoColl};
	}
}