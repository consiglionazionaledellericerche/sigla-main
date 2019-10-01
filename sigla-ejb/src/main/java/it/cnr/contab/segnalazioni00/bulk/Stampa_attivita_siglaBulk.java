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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/05/2009
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.util.Collection;
import java.util.Dictionary;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stampa_attivita_siglaBulk extends OggettoBulk implements Persistent {

	private Esercizio_baseBulk esercizio_base;
	private TerzoBulk  terzoForPrint;
	

//	private String stato;
//	private String tipo_attivita;
	private java.util.Collection anni;
	private boolean terzoForPrintEnabled;
	
	private Boolean stato_analisi;
	private Boolean stato_sviluppo;
	private Boolean stato_test;
	private Boolean stato_rilasciato;
	private Boolean stato_iniziale;
	private Boolean stato_differito;
	
	private Boolean tipo_att_correttiva;
	private Boolean tipo_att_evolutiva;
	private Boolean tipo_att_manutentiva;
	private Boolean tipo_att_estrazione_dati;
	private Boolean tipo_att_supporto_gestione;
	
	private String selezionaStato;
	private String selezionaTipoAttivita;
/*		private final static java.util.Dictionary STATO;
	private final static java.util.Dictionary TIPO_ATTIVITA;
	

	public final static String INIZIALE ="I";
	public final static String ANALISI ="A";
	public final static String SVILUPPO ="S";
	public final static String TEST ="T";
	public final static String RILASCIATA ="R";
	public final static String STATO_TUTTI = "%";

	
	
	public final static String CORRETTIVA ="C";
	public final static String EVOLUTIVA ="E";
	public final static String MANUTENTIVA ="M";
	public final static String SUPPORTO_GESTIONE ="G";
	public final static String ESTRAZIONE_DATI ="D";
	public final static String TIPO_TUTTI = "%";
	 
	
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(INIZIALE, "Iniziale");
		STATO.put(ANALISI,  "Analisi");
		STATO.put(SVILUPPO, "Sviluppo");
		STATO.put(TEST,  "Test");
		STATO.put(RILASCIATA, "Rilasciata/Chiusa");
		STATO.put(STATO_TUTTI, "Tutti");
	}
	static{
		TIPO_ATTIVITA = new it.cnr.jada.util.OrderedHashtable();
		TIPO_ATTIVITA.put(CORRETTIVA, 		 "Correttiva");
		TIPO_ATTIVITA.put(EVOLUTIVA, 		 "Evolutiva");
		TIPO_ATTIVITA.put(MANUTENTIVA, 		 "Manutentiva");
		TIPO_ATTIVITA.put(SUPPORTO_GESTIONE, "Supporto alla Gestione");
		TIPO_ATTIVITA.put(ESTRAZIONE_DATI, 	 "Estrazione Dati");
		TIPO_ATTIVITA.put(TIPO_TUTTI, "Tutte");

	} 
	 
	
	public java.util.Dictionary getStatoKeys() {
		return STATO;
	}
	public java.util.Dictionary getTipo_attivitaKeys() {
		return TIPO_ATTIVITA;
	}
	*/
	
	public Stampa_attivita_siglaBulk() {
		super();
	}

	public java.util.Collection getAnni() {
		return anni;
	}

	public void setAnni(java.util.Collection anni) {
		this.anni = anni;
	}
	
	public String getCdTerzoForPrint() {
		if (getTerzoForPrint()==null)
		  return "%";
		if(getTerzoForPrint().getCd_terzo()==null)
		  return "%";	
		return getTerzoForPrint().getCd_terzo().toString();
	}

	public TerzoBulk getTerzoForPrint() {
		return terzoForPrint;
	}

	public void setTerzoForPrint(TerzoBulk terzoForPrint) {
		this.terzoForPrint = terzoForPrint;
	}

	
	
	/*public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	
	public java.lang.String getTipo_attivita() {
		return tipo_attivita;
	}
	public void setTipo_attivita(java.lang.String tipo_attivita)  {
		this.tipo_attivita=tipo_attivita;
	}
	*/
	public Esercizio_baseBulk getEsercizio_base() {
		return esercizio_base;
	}

	public void setEsercizio_base(Esercizio_baseBulk esercizio_base) {
		this.esercizio_base = esercizio_base;
	}
	
	public String getEsercizioForPrint() {

	    if (getEsercizio_base() == null)
	        return "%";
	    if (getEsercizio_base().getEsercizio() == null)
	        return "%";

	    return getEsercizio_base().getEsercizio().toString();

	}
		
	public boolean isTerzoForPrintEnabled() {
		return terzoForPrintEnabled;
	}

	public void setTerzoForPrintEnabled(boolean b) {
		terzoForPrintEnabled = b;
	}
	
	/**
	 * Inizializza gli attributi di ragruppamento
	 **
	 */
	
	public void inizializzaStati() {
		setStato_analisi(new Boolean(false));
		setStato_sviluppo(new Boolean(false));
		setStato_test(new Boolean(false));
		setStato_rilasciato(new Boolean(false));
		setStato_iniziale(new Boolean(false));
		setStato_differito(new Boolean(false));
	}
	public void selezionaStati(){
		setStato_analisi(new Boolean(!getStato_analisi().booleanValue()));
		setStato_sviluppo(new Boolean(!getStato_sviluppo().booleanValue()));
		setStato_test(new Boolean(!getStato_test().booleanValue()));
		setStato_rilasciato(new Boolean(!getStato_rilasciato().booleanValue()));
		setStato_iniziale(new Boolean(!getStato_iniziale().booleanValue()));
		setStato_differito(new Boolean(!getStato_differito().booleanValue()));
		}
	
	public Boolean getStato_analisi() {
		return stato_analisi;
	}

	public Boolean getStato_rilasciato() {
		return stato_rilasciato;
	} 
		
	public Boolean getStato_sviluppo() {
		return stato_sviluppo;
	}
	
	public Boolean getStato_test() {
		return stato_test;
	}
	
	public Boolean getStato_iniziale() {
		return stato_iniziale;
	}
	
	public Boolean getStato_differito() {
		return stato_differito;
	}
	
	
	public void setStato_analisi(Boolean boolean1) {
		stato_analisi = boolean1;
	}
	
	public void setStato_rilasciato(Boolean boolean1) {
		stato_rilasciato = boolean1;
	}
	
	public void setStato_sviluppo(Boolean boolean1) {
		stato_sviluppo = boolean1;
	}
	
	public void setStato_test(Boolean boolean1) {
		stato_test = boolean1;
	}
	
	public void setStato_iniziale(Boolean boolean1) {
		stato_iniziale = boolean1;
	}
	
	public void setStato_differito(Boolean boolean1) {
		stato_differito = boolean1;
	}
	
	public void inizializzaTipiAttivita() {
		setTipo_att_correttiva(new Boolean(false));
		setTipo_att_evolutiva(new Boolean(false));
		setTipo_att_manutentiva(new Boolean(false));
		setTipo_att_estrazione_dati(new Boolean(false));
		setTipo_att_supporto_gestione(new Boolean(false));
	}
	public void selezionaTipiAttivita(){
		setTipo_att_correttiva(new Boolean(!getTipo_att_correttiva().booleanValue()));
		setTipo_att_evolutiva(new Boolean(!getTipo_att_evolutiva().booleanValue()));
		setTipo_att_manutentiva(new Boolean(!getTipo_att_manutentiva().booleanValue()));
		setTipo_att_estrazione_dati(new Boolean(!getTipo_att_estrazione_dati().booleanValue()));
		setTipo_att_supporto_gestione(new Boolean(!getTipo_att_supporto_gestione().booleanValue()));
		}
	
	public Boolean getTipo_att_correttiva() {
		return tipo_att_correttiva;
	}

	public Boolean getTipo_att_evolutiva() {
		return tipo_att_evolutiva;
	} 
		
	public Boolean getTipo_att_manutentiva() {
		return tipo_att_manutentiva;
	}
	
	public Boolean getTipo_att_estrazione_dati() {
		return tipo_att_estrazione_dati;
	}
	
	public Boolean getTipo_att_supporto_gestione() {
		return tipo_att_supporto_gestione;
	}
	
	public void setTipo_att_correttiva(Boolean boolean1) {
		tipo_att_correttiva = boolean1;
	}
	
	public void setTipo_att_evolutiva(Boolean boolean1) {
		tipo_att_evolutiva = boolean1;
	}
	
	public void setTipo_att_manutentiva(Boolean boolean1) {
		tipo_att_manutentiva = boolean1;
	}
	
	public void setTipo_att_estrazione_dati(Boolean boolean1) {
		tipo_att_estrazione_dati = boolean1;
	}
	
	public void setTipo_att_supporto_gestione(Boolean boolean1) {
		tipo_att_supporto_gestione = boolean1;
	}

	public String getSelezionaStato() {
		return selezionaStato;
	}

	public void setSelezionaStato(String selezionaStato) {
		this.selezionaStato = selezionaStato;
	}

	public String getSelezionaTipoAttivita() {
		return selezionaTipoAttivita;
	}

	public void setSelezionaTipoAttivita(String selezionaTipoAttivita) {
		this.selezionaTipoAttivita = selezionaTipoAttivita;
	}
}