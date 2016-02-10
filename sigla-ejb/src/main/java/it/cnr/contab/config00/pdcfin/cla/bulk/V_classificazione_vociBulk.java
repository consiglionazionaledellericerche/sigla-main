/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.jada.bulk.BulkList;

public class V_classificazione_vociBulk extends Classificazione_vociBulk {
  	private java.lang.String cd_livello_last;
  	private java.lang.String cd_classificazione;
	private java.lang.Integer nr_livello;
	
	public V_classificazione_vociBulk() {
		super();
	}
	
	public java.lang.String getCd_livello_last() {
		return cd_livello_last;
	}
	
	public void setCd_livello_last(java.lang.String cd_livello_last) {
		this.cd_livello_last = cd_livello_last;
	}
	
	public V_classificazione_vociBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}

	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	public java.lang.Integer getNr_livello() {
		return nr_livello;
	}

	public void setNr_livello(java.lang.Integer integer) {
		nr_livello = integer;
	}
}