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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Attivita_siglaBulk extends Attivita_siglaBase {
	private TerzoBulk terzo;
	private TerzoBulk terzo1;
	private TerzoBulk terzo2;
	private TerzoBulk terzo3;
	private TerzoBulk terzo4;
	private TerzoBulk terzo5;
	private TerzoBulk terzo6;
	private final static java.util.Dictionary PRIORITA;
	private final static java.util.Dictionary STATO;
	private final static java.util.Dictionary TIPO_ATTIVITA;
	public final static String INIZIALE ="I";
	public final static String ANALISI ="A";
	public final static String SVILUPPO ="S";
	public final static String TEST ="T";
	public final static String RILASCIATA ="R";
	public final static String DIFFERITO ="D";

	public final static String UNO ="1";
	public final static String DUE ="2";
	public final static String TRE ="3";
	public final static String QUATTRO ="4";
	public final static String CINQUE ="5";
	
	public final static String CORRETTIVA ="C";
	public final static String EVOLUTIVA ="E";
	public final static String MANUTENTIVA ="M";
	public final static String SUPPORTO_GESTIONE ="G";
	public final static String ESTRAZIONE_DATI ="D";
	 
	static{
		PRIORITA = new it.cnr.jada.util.OrderedHashtable();
		PRIORITA.put(UNO, 		 "Alta");
		PRIORITA.put(DUE, 		 "Medio Alta");
		PRIORITA.put(TRE, 		 "Media");
		PRIORITA.put(QUATTRO, 		 "Medio Bassa");
		PRIORITA.put(CINQUE, 		 "Bassa");
	}
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(INIZIALE, 		 "Iniziale");
		STATO.put(ANALISI, 		 "Analisi");
		STATO.put(SVILUPPO, 		 "Sviluppo");
		STATO.put(TEST, 		 "Test");
		STATO.put(RILASCIATA, 		 "Rilasciata/Chiusa");
		STATO.put(DIFFERITO, 		 "Sospeso/Differito");
	}
	static{
		TIPO_ATTIVITA = new it.cnr.jada.util.OrderedHashtable();
		TIPO_ATTIVITA.put(CORRETTIVA, 		 "Correttiva");
		TIPO_ATTIVITA.put(EVOLUTIVA, 		 "Evolutiva");
		TIPO_ATTIVITA.put(MANUTENTIVA, 		 "Manutentiva");
		TIPO_ATTIVITA.put(SUPPORTO_GESTIONE, "Supporto alla Gestione");
		TIPO_ATTIVITA.put(ESTRAZIONE_DATI, 	 "Estrazione Dati");
	} 
	 
	public Attivita_siglaBulk() {
		super();
	}
	public Attivita_siglaBulk(java.lang.Long pg_attivita, java.lang.Integer esercizio) {
		super(pg_attivita, esercizio);
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}
	public TerzoBulk getTerzo1() {
		return terzo1;
	}
	public void setTerzo1(TerzoBulk terzo1) {
		this.terzo1 = terzo1;
	}
	public TerzoBulk getTerzo2() {
		return terzo2;
	}
	public void setTerzo2(TerzoBulk terzo2) {
		this.terzo2 = terzo2;
	}
	public TerzoBulk getTerzo3() {
		return terzo3;
	}
	public void setTerzo3(TerzoBulk terzo3) {
		this.terzo3 = terzo3;
	}
	public TerzoBulk getTerzo4() {
		return terzo4;
	}
	public void setTerzo4(TerzoBulk terzo4) {
		this.terzo4 = terzo4;
	}
	public TerzoBulk getTerzo5() {
		return terzo5;
	}
	public void setTerzo5(TerzoBulk terzo5) {
		this.terzo5 = terzo5;
	}
	public TerzoBulk getTerzo6() {
		return terzo6;
	}
	public void setTerzo6(TerzoBulk terzo6) {
		this.terzo6 = terzo6;
	}
	
	public java.lang.Integer getCd_redattore() {
		if (getTerzo()!=null)
			return getTerzo().getCd_terzo();
		else
			return null;
	}
	public void setCd_redattore(java.lang.Integer cd_redattore)  {
		getTerzo().setCd_terzo(cd_redattore);
	}
	public java.lang.Integer getCd_responsabile_1() {
		if (getTerzo1()!=null)
			return getTerzo1().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_1(java.lang.Integer cd_responsabile_1)  {
		getTerzo1().setCd_terzo(cd_responsabile_1);
	}
	public java.lang.Integer getCd_responsabile_2() {
		if (getTerzo2()!=null)
			return getTerzo2().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_2(java.lang.Integer cd_responsabile_2)  {
		getTerzo2().setCd_terzo(cd_responsabile_2);
	}
	public java.lang.Integer getCd_responsabile_3() {
		if (getTerzo3()!=null)
			return getTerzo3().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_3(java.lang.Integer cd_responsabile_3)  {
		getTerzo3().setCd_terzo(cd_responsabile_3);
	}
	public java.lang.Integer getCd_responsabile_4() {
		if (getTerzo4()!=null)
			return getTerzo4().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_4(java.lang.Integer cd_responsabile_4)  {
		getTerzo4().setCd_terzo(cd_responsabile_4);
	}
	public java.lang.Integer getCd_responsabile_5() {
		if (getTerzo5()!=null)
			return getTerzo5().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_5(java.lang.Integer cd_responsabile_5)  {
		getTerzo5().setCd_terzo(cd_responsabile_5);
	}
	public java.lang.Integer getCd_responsabile_6() {
		if (getTerzo6()!=null)
			return getTerzo6().getCd_terzo();
		else
			return null;
	}
	public void setCd_responsabile_6(java.lang.Integer cd_responsabile_6)  {
		getTerzo6().setCd_terzo(cd_responsabile_6);
	}
	
	public java.util.Dictionary getPrioritaKeys() {
		return PRIORITA;
	}
	public java.util.Dictionary getStatoKeys() {
		return STATO;
	}
	public java.util.Dictionary getTipo_attivitaKeys() {
		return TIPO_ATTIVITA;
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		this.setFl_bug_aperto(false);
		this.setEsercizio((it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext())));
		return super.initializeForInsert(crudbp, actioncontext);
	}
}