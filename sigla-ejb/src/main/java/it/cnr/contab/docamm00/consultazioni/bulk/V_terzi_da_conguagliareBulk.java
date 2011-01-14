/*
* Date 27/10/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;

import java.util.Dictionary;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class V_terzi_da_conguagliareBulk extends OggettoBulk implements Persistent {
//	ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

//	CD_CDS VARCHAR(30) NOT NULL
  	private java.lang.String cd_cds;

//	CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
  	private java.lang.String cd_unita_organizzativa;

//	CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

//	DENOMINAZIONE VARCHAR(100)
	private java.lang.String denominazione;

//	CD_TIPO_RAPPORTO VARCHAR(10)
	private java.lang.String cd_tipo_rapporto;
	
//	TIPOLOGIA VARCHAR(1)
	private java.lang.String tipologia;
		
	private static OrderedHashtable tipologiaKeys;
		final public static String TIPO_A 	= "A";
		final public static String TIPO_B	= "B";
		final public static String TIPO_C	= "C";
	
	public V_terzi_da_conguagliareBulk() {
		super();
	}

	public OrderedHashtable getTipologiaKeys() {
		if (tipologiaKeys == null)
		{
			tipologiaKeys = new OrderedHashtable();
			tipologiaKeys.put("A", "Con compensi senza conguaglio");	
			tipologiaKeys.put("B", "Con addizionale rateizzata non trattenuta");	
			tipologiaKeys.put("C", "Con conguagli non contabilizzati");
		}
		return tipologiaKeys;
	}
	public static void setTipologiaKeys(OrderedHashtable hashtable) {
		tipologiaKeys = hashtable;
	}	
	public Dictionary getTipologiaKeysForSearch() {
		return getTipologiaKeys();
	}	
	public java.lang.Integer getEsercizio () {
			return esercizio;
	}
	public void setEsercizio(java.lang.Integer int1) {
		esercizio = int1;
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
	public java.lang.String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}
	public void setCd_tipo_rapporto(java.lang.String string) {
		cd_tipo_rapporto = string;
	}
	public java.lang.String getTipologia() {
		return tipologia;
	}
	public void setTipologia(java.lang.String string) {
		tipologia = string;
	}
}