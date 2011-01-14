/*
* Date 27/10/2005
*/
package it.cnr.contab.incarichi00.bulk;

import java.util.Dictionary;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.OrderedHashtable;
public class V_terzi_da_completareBulk extends OggettoBulk implements Persistent {

//	CD_CDS VARCHAR(30) NOT NULL
  	private java.lang.String cd_cds;

//	CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
  	private java.lang.String cd_unita_organizzativa;

//	CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

//	DENOMINAZIONE VARCHAR(100)
	private java.lang.String denominazione;

	public V_terzi_da_completareBulk() {
		super();
	}

	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer int1) {
		cd_terzo = int1;
	}
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(java.lang.String string) {
		denominazione = string;
	}
}