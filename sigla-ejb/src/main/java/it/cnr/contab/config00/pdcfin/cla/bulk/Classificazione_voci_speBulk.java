/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;

public class Classificazione_voci_speBulk extends Classificazione_vociBulk {
	public Classificazione_voci_speBulk() {
		super();
		setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	}
	public Classificazione_voci_speBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
		setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	}
	public Classificazione_voci_speBulk(Classificazione_voci_speBulk liv_pre, String cd_livello) {
		super(liv_pre, cd_livello);
		setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	}
}