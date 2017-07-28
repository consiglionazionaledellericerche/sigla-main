/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;

import it.cnr.contab.config00.pdcep.bulk.Voce_epHome;

public class Classificazione_voci_ep_patBulk extends Classificazione_voci_epBulk {
	public Classificazione_voci_ep_patBulk() {
		super();
		setTipo(Voce_epHome.PATRIMONIALE);
	}
	public Classificazione_voci_ep_patBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
		setTipo(Voce_epHome.PATRIMONIALE);
	}
	public Classificazione_voci_ep_patBulk(Classificazione_voci_ep_patBulk liv_pre, String cd_livello) {
		super(liv_pre, cd_livello);
		setTipo(Voce_epHome.PATRIMONIALE);
	}
}