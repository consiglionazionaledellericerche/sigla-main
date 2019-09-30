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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2014
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Collections;

public class VIncarichiAssRicBorseStBulk extends VIncarichiAssRicBorseStBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INCARICHI_ELENCO_CONS
	 **/
	private java.lang.Integer esercizioValidita;
	private String fonti;
    public final static java.util.Dictionary <String,String> STATO_KEYS;
    public final static java.util.Dictionary <String,String> TIPOLOGIA_COLLABORAZIONE_KEYS;
    public final static java.util.Dictionary <String,String> TIPOLOGIA_FONTI_KEYS;
    public final static String TUTTI = "T";
    public final static String TUTTE = "T";
    public final static String ASSEGNI_DI_RICERCA = "ASS";
    public final static String BORSA_DI_STUDIO = "BORS";
    public final static String COLLABORAZIONE_OCCASIONALE = "OCCA";
    public final static String COLLABORAZIONE_PROFESSIONALE = "PROF";
    public final static String COLLABORAZIONE_COORDINATA_CONTINUATIVA = "COLL";
	private Unita_organizzativaBulk uoForPrint;
	private TerzoBulk filtroSoggetto = null;
	private Boolean statoProvvisorio;
	private Boolean statoDefinitivo;
	private Boolean statoAnnullatoEliminato;
	private Boolean statoInviato;
	private Boolean statoChiuso;
	private Boolean tipologiaAssegni;
	private Boolean tipologiaBorsaDiStudio;
	private Boolean tipologiaCollOcc;
	private Boolean tipologiaCollProf;
	private Boolean tipologiaCococo;
	private boolean isUOForPrintEnabled;
	
	static {
		TIPOLOGIA_FONTI_KEYS = new it.cnr.jada.util.OrderedHashtable();
		java.util.Dictionary<String,String> tipiFonti = NaturaBulk.tipo_naturaKeys;
		for(String key : Collections.list(tipiFonti.keys())){
			TIPOLOGIA_FONTI_KEYS.put(key,tipiFonti.get(key));	
		}
		TIPOLOGIA_FONTI_KEYS.put(TUTTE,"Tutte");
		
		STATO_KEYS = new it.cnr.jada.util.OrderedHashtable();
		java.util.Dictionary<String,String> statiIncarichi = Incarichi_repertorioBulk.ti_statoKeys;
		for(String key : Collections.list(statiIncarichi.keys())){
			STATO_KEYS.put(key,statiIncarichi.get(key));	
		}
		STATO_KEYS.put(TUTTI,"Tutti");

		
//		for (Enumeration<String> i = tipiIncarico.keys(); tipiIncarico.keys().hasMoreElements();) {
//			String key = i.nextElement();
//			TIPOLOGIA_COLLABORAZIONE_KEYS.put(key,tipiIncarico.get(key));	
//		}
		TIPOLOGIA_COLLABORAZIONE_KEYS = new it.cnr.jada.util.OrderedHashtable();
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(ASSEGNI_DI_RICERCA,"Assegno di Ricerca");
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(BORSA_DI_STUDIO,"Borsa di Studio");
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(COLLABORAZIONE_OCCASIONALE,"Collaborazione Occasionale");
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(COLLABORAZIONE_PROFESSIONALE,"Collaborazione Professionale");
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(COLLABORAZIONE_COORDINATA_CONTINUATIVA,"Collaborazione Coordinata e Continuativa");
		TIPOLOGIA_COLLABORAZIONE_KEYS.put(TUTTE,"Tutte");
	}

	public VIncarichiAssRicBorseStBulk() {
		super();
		setFonti(TUTTE);
		inizializzaRagruppamenti();
	}

	public static java.util.Dictionary getTipologiaFontiKeys() {
		return TIPOLOGIA_FONTI_KEYS;
	}

	private java.sql.Date dt_validita;
	public java.sql.Date getDt_validita() {
		return dt_validita;
	}
	public void setDt_validita(java.sql.Date dt_validita) {
		this.dt_validita = dt_validita;
	}

	@Override
	public OggettoBulk initializeForSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		super.initializeForSearch(crudbp, actioncontext);
		return this;
	}

	public void inizializzaRagruppamenti() {
		setTipologiaAssegni(new Boolean(true));
		setTipologiaBorsaDiStudio(new Boolean(true));
		setTipologiaCococo(new Boolean(true));
		setTipologiaCollOcc(new Boolean(true));
		setTipologiaCollProf(new Boolean(true));
		
		setStatoAnnullatoEliminato(new Boolean(false));
		setStatoChiuso(new Boolean(false));
		setStatoDefinitivo(new Boolean(true)); 
		setStatoInviato(new Boolean(false));    
		setStatoProvvisorio(new Boolean(false));
		
	}

	public Boolean isSceltaFontiInterne(){
		if (getFonti() != null && getFonti().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE)){
			return true;
		}
		return false;
	}

	public Boolean isSceltaFontiEsterne(){
		if (getFonti() != null && getFonti().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE)){
			return true;
		}
		return false;
	}

	public Boolean isSceltaFontiTutte(){
		if (getFonti() != null && getFonti().equals(TUTTE)){
			return true;
		}
		return false;
	}

	public java.lang.Integer getEsercizioValidita() {
		return esercizioValidita;
	}

	public void setEsercizioValidita(java.lang.Integer esercizioValidita) {
		this.esercizioValidita = esercizioValidita;
	}

	public String getFonti() {
		return fonti;
	}

	public void setFonti(String fonti) {
		this.fonti = fonti;
	}

	public boolean isROsoggetto() {
		
		return getFiltroSoggetto() == null ||
				getFiltroSoggetto().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}

	public TerzoBulk getFiltroSoggetto() {
		return filtroSoggetto;
	}

	public void setFiltroSoggetto(TerzoBulk filtroSoggetto) {
		this.filtroSoggetto = filtroSoggetto;
	}

	public Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}

	public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
		this.uoForPrint = uoForPrint;
	}
	public boolean isROUoForPrint() {
		return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}

	public Boolean getStatoProvvisorio() {
		return statoProvvisorio;
	}

	public void setStatoProvvisorio(Boolean statoProvvisorio) {
		this.statoProvvisorio = statoProvvisorio;
	}

	public Boolean getStatoDefinitivo() {
		return statoDefinitivo;
	}

	public void setStatoDefinitivo(Boolean statoDefinitivo) {
		this.statoDefinitivo = statoDefinitivo;
	}

	public Boolean getStatoAnnullatoEliminato() {
		return statoAnnullatoEliminato;
	}

	public void setStatoAnnullatoEliminato(Boolean statoAnnullatoEliminato) {
		this.statoAnnullatoEliminato = statoAnnullatoEliminato;
	}

	public Boolean getStatoInviato() {
		return statoInviato;
	}

	public void setStatoInviato(Boolean statoInviato) {
		this.statoInviato = statoInviato;
	}

	public Boolean getStatoChiuso() {
		return statoChiuso;
	}

	public void setStatoChiuso(Boolean statoChiuso) {
		this.statoChiuso = statoChiuso;
	}

	public Boolean getTipologiaAssegni() {
		return tipologiaAssegni;
	}

	public void setTipologiaAssegni(Boolean tipologiaAssegni) {
		this.tipologiaAssegni = tipologiaAssegni;
	}

	public Boolean getTipologiaBorsaDiStudio() {
		return tipologiaBorsaDiStudio;
	}

	public void setTipologiaBorsaDiStudio(Boolean tipologiaBorsaDiStudio) {
		this.tipologiaBorsaDiStudio = tipologiaBorsaDiStudio;
	}

	public Boolean getTipologiaCollOcc() {
		return tipologiaCollOcc;
	}

	public void setTipologiaCollOcc(Boolean tipologiaCollOcc) {
		this.tipologiaCollOcc = tipologiaCollOcc;
	}

	public Boolean getTipologiaCollProf() {
		return tipologiaCollProf;
	}

	public void setTipologiaCollProf(Boolean tipologiaCollProf) {
		this.tipologiaCollProf = tipologiaCollProf;
	}

	public Boolean getTipologiaCococo() {
		return tipologiaCococo;
	}

	public void setTipologiaCococo(Boolean tipologiaCococo) {
		this.tipologiaCococo = tipologiaCococo;
	}
	
	public Boolean almenoUnoStatoSelezionato(){
		return 	getStatoAnnullatoEliminato() || getStatoChiuso() || getStatoDefinitivo() || getStatoInviato() || getStatoProvvisorio();
	}
	public Boolean almenoUnaTipologiaSelezionata(){
		return 	getTipologiaAssegni() || getTipologiaBorsaDiStudio() || getTipologiaCococo() || getTipologiaCollOcc() || getTipologiaCollProf();
	}

	public boolean isUOForPrintEnabled() {
		return isUOForPrintEnabled;
	}

	public void setUOForPrintEnabled(boolean isUOForPrintEnabled) {
		this.isUOForPrintEnabled = isUOForPrintEnabled;
	}
	
}