/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_situazione_sintetica_x_progettoBulk extends OggettoBulk {

	private Integer esercizio;

	private String cd_unita_organizzativa;

	protected it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;

	protected it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progetto;

	public Stampa_situazione_sintetica_x_progettoBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}

	public it.cnr.contab.progettiric00.core.bulk.ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}
	
	public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita) {
		this.centro_responsabilita = centro_responsabilita;
	}
	
	public String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	
	public Integer getPg_progetto() {
		return this.getProgetto()!=null?this.getProgetto().getPg_progetto():null;
	}
}