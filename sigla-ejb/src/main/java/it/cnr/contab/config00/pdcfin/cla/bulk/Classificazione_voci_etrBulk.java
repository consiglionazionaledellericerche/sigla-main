/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;

public class Classificazione_voci_etrBulk extends Classificazione_vociBulk {
	public Classificazione_voci_etrBulk() {
		super();
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
	public Classificazione_voci_etrBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
	public Classificazione_voci_etrBulk(Classificazione_voci_etrBulk liv_pre, String cd_livello) {
		super(liv_pre, cd_livello);
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
}