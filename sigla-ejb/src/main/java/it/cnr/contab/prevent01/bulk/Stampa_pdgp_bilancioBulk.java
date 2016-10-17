/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bulk;

import java.util.Enumeration;
import java.util.Iterator;

import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdgp_bilancioBulk extends OggettoBulk {

	private Integer esercizio;
	
	//	Tipo gestione
	private String ti_gestione;

	//	Tipo di stampa
	private String ti_fonte;
	
	//	Tipo livello
	private String ti_livello;

	private it.cnr.jada.util.OrderedHashtable livelliOptions = new it.cnr.jada.util.OrderedHashtable();
	
	public final static String TIPO_DECISIONALE_SCIENTIFICO = "DECSCI";
	public final static String TIPO_GESTIONALE_SCIENTIFICO = "GESTSCI";
	public final static String TIPO_STANZIAMENTO_SCIENTIFICO = "STASCI";
	public final static String TIPO_DECISIONALE_FINANZIARIO = "DECFIN";
	public final static String TIPO_GESTIONALE_FINANZIARIO = "GESTFIN";
	public final static String TIPO_STANZIAMENTO_FINANZIARIO = "STAFIN";

	public final static java.util.Dictionary ti_fonteKeys;
	
	static {
		ti_fonteKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_fonteKeys.put(TIPO_DECISIONALE_SCIENTIFICO,"Decisionale Scientifico");
		ti_fonteKeys.put(TIPO_GESTIONALE_SCIENTIFICO,"Gestionale Scientifico");
		ti_fonteKeys.put(TIPO_DECISIONALE_FINANZIARIO,"Decisionale Finanziario");
		ti_fonteKeys.put(TIPO_GESTIONALE_FINANZIARIO,"Gestionale Finanziario");
	};	

	public final static String TIPO_GESTIONE_ENTRATA = "E";
	public final static String TIPO_GESTIONE_SPESA = "S";

	public final static java.util.Dictionary ti_gestioneKeys;
	
	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TIPO_GESTIONE_ENTRATA,"Entrata");
		ti_gestioneKeys.put(TIPO_GESTIONE_SPESA,"Spesa");
	};	

	public Stampa_pdgp_bilancioBulk() {
		super();
	}
	
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}

	public String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	
	public String getTi_fonte() {
		return ti_fonte;
	}
	public void setTi_fonte(String ti_fonte) {
		this.ti_fonte = ti_fonte;
	}	
	
	public String getTi_livello() {
		return ti_livello;
	}
	public void setTi_livello(String ti_livello) {
		this.ti_livello = ti_livello;
	}

	public it.cnr.jada.util.OrderedHashtable getLivelliOptions() {
		return livelliOptions;
	}
	public void setLivelliOptions(it.cnr.jada.util.OrderedHashtable livelliOptions) {
		this.livelliOptions = livelliOptions;
	}
	
	public Integer getNr_livello() {
		Enumeration a = livelliOptions.keys();
		while (a.hasMoreElements()) {
			Integer key = (Integer) a.nextElement();
			if (livelliOptions.get(key).equals(ti_livello))
				return key;
		}
		return 1;
	}
}
