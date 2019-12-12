/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bulk;

import java.util.Enumeration;

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
	private String ti_stampa;

	//	Tipo di aggregazione
	private String ti_aggregazione;

	//	Tipo di orgine
	private String ti_origine;

	//	Tipo livello
	private String ti_livello;

	private Integer percCassa;

	//	Stampa Riepilogo Titoli
	private boolean ti_riepilogo;

	//	Parte Bilancio Gestionale da stampare - I - II parte
	private String ti_parte;

	private it.cnr.jada.util.OrderedHashtable livelliOptions = new it.cnr.jada.util.OrderedHashtable();
	
	public final static String TIPO_DECISIONALE = "DEC";
	public final static String TIPO_GESTIONALE = "GEST";
	public final static String TIPO_PLURIENNALE = "PLUR";

	public final static java.util.Dictionary ti_stampaKeys;
	
	static {
		ti_stampaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_stampaKeys.put(TIPO_DECISIONALE,"Decisionale");
		ti_stampaKeys.put(TIPO_GESTIONALE,"Gestionale");
		ti_stampaKeys.put(TIPO_PLURIENNALE,"Pluriennale");
	}

	public final static String TIPO_SCIENTIFICO = "SCI";
	public final static String TIPO_FINANZIARIO = "FIN";

	public final static java.util.Dictionary ti_aggregazioneKeys;
	
	static {
		ti_aggregazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_aggregazioneKeys.put(TIPO_SCIENTIFICO,"Scientifico");
		ti_aggregazioneKeys.put(TIPO_FINANZIARIO,"Finanziario");
	}

	public final static String TIPO_PROVVISORIO = "EXT";
	public final static String TIPO_REALE = "REA";

	public final static java.util.Dictionary ti_origineKeys;
	
	static {
		ti_origineKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_origineKeys.put(TIPO_PROVVISORIO,"Provvisoria");
		ti_origineKeys.put(TIPO_REALE,"Reale");
	}

	public final static String TIPO_PARTE_PRIMA = "I";
	public final static String TIPO_PARTE_SECONDA = "II";
	public final static String TIPO_PARTE_ENTRAMBE = "III";

	public final static java.util.Dictionary ti_parteKeys;
	
	static {
		ti_parteKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_parteKeys.put(TIPO_PARTE_PRIMA,"Prima");
		ti_parteKeys.put(TIPO_PARTE_SECONDA,"Seconda");
		ti_parteKeys.put(TIPO_PARTE_ENTRAMBE,"Entrambe");
	}

	public final static String TIPO_DECISIONALE_SCIENTIFICO = "DECSCI";
	public final static String TIPO_GESTIONALE_SCIENTIFICO = "GESTSCI";
	public final static String TIPO_STANZIAMENTO_SCIENTIFICO = "STASCI";
	public final static String TIPO_PROVVISORIO_SCIENTIFICO = "EXTSCI";

	public final static String TIPO_DECISIONALE_FINANZIARIO = "DECFIN";
	public final static String TIPO_GESTIONALE_FINANZIARIO = "GESTFIN";
	public final static String TIPO_STANZIAMENTO_FINANZIARIO = "STAFIN";
	public final static String TIPO_PROVVISORIO_FINANZIARIO = "EXTFIN";

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
	
	public String getTi_origine() {
		return ti_origine;
	}
	public void setTi_origine(String ti_origine) {
		this.ti_origine = ti_origine;
	}

	public String getTi_aggregazione() {
		return ti_aggregazione;
	}
	public void setTi_aggregazione(String ti_aggregazione) {
		this.ti_aggregazione = ti_aggregazione;
	}
	
	public String getTi_stampa() {
		return ti_stampa;
	}
	public void setTi_stampa(String ti_stampa) {
		this.ti_stampa = ti_stampa;
	}

	public String getTi_fonte() {
		if (this.isTipoPluriennale())
			return TIPO_GESTIONALE.concat(getTi_aggregazione());
		else if (TIPO_REALE.equals(getTi_origine()))
			return getTi_stampa().concat(getTi_aggregazione());
		return TIPO_PROVVISORIO.concat(getTi_aggregazione());
	}
	
	public String getTi_livello() {
		return ti_livello;
	}
	public void setTi_livello(String ti_livello) {
		this.ti_livello = ti_livello;
	}

	public Integer getPercCassa() {
		return percCassa;
	}
	public void setPercCassa(Integer percCassa) {
		this.percCassa = percCassa;
	}
	
	public boolean getTi_riepilogo() {
		return ti_riepilogo;
	}
	public void setTi_riepilogo(boolean ti_riepilogo) {
		this.ti_riepilogo = ti_riepilogo;
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

	public String getRiepilogo() {
		return ti_riepilogo?"S":"N";
	}
	
	public String getTi_parte() {
		return ti_parte;
	}
	
	public void setTi_parte(String ti_parte) {
		this.ti_parte = ti_parte;
	}

	public boolean isTipoDecisionale() {
		return Stampa_pdgp_bilancioBulk.TIPO_DECISIONALE.equals(this.getTi_stampa());
	}

	public boolean isTipoGestionale() {
		return Stampa_pdgp_bilancioBulk.TIPO_GESTIONALE.equals(this.getTi_stampa());
	}

	public boolean isTipoPluriennale() {
		return Stampa_pdgp_bilancioBulk.TIPO_PLURIENNALE.equals(this.getTi_stampa());
	}

	public boolean isPartePrima() {
		return Stampa_pdgp_bilancioBulk.TIPO_PARTE_PRIMA.equals(this.getTi_parte());
	}

	public boolean isParteSeconda() {
		return Stampa_pdgp_bilancioBulk.TIPO_PARTE_SECONDA.equals(this.getTi_parte());
	}

	public boolean isTipoGestioneEntrata() {
		return Stampa_pdgp_bilancioBulk.TIPO_GESTIONE_ENTRATA.equals(this.getTi_gestione());
	}

	public boolean isTipoGestioneSpesa() {
		return Stampa_pdgp_bilancioBulk.TIPO_GESTIONE_SPESA.equals(this.getTi_gestione());
	}

	public boolean isTipoAggregazioneScientifica() {
		return Stampa_pdgp_bilancioBulk.TIPO_SCIENTIFICO.equals(this.getTi_aggregazione());
	}

	public boolean isTipoAggregazioneFinanziaria() {
		return Stampa_pdgp_bilancioBulk.TIPO_FINANZIARIO.equals(this.getTi_aggregazione());
	}
}
