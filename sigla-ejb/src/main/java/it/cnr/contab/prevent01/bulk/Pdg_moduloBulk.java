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
* Created by Generator 1.0
* Date 19/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
public class Pdg_moduloBulk extends Pdg_moduloBase {

	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
	private final static java.util.Dictionary statiKeys;
	protected it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto;

	private final static java.util.Dictionary statiKeys_AC;
	private final static java.util.Dictionary statiKeys_CC;
	private final static java.util.Dictionary statiKeys_AD;
	private final static java.util.Dictionary statiKeys_AP;
	private final static java.util.Dictionary statiKeys_AG;
	private final static java.util.Dictionary statiKeys_CG;

	public static String STATO_AC       = "AC";
	public static String STATO_CC       = "CC";
	public static String STATO_AD       = "AD";
	public static String STATO_AP       = "AP";
	public static String STATO_AG       = "AG";
	public static String STATO_CG       = "CG";

	static {
		statiKeys = new it.cnr.jada.util.OrderedHashtable();
		statiKeys.put(STATO_AC,"AC - Apertura per la compilazione");
		statiKeys.put(STATO_CC,"CC - Chiusura della compilazione");
		statiKeys.put(STATO_AD,"AD - In fase di adueguamento");
		statiKeys.put(STATO_AP,"AP - Approvato");
		statiKeys.put(STATO_AG,"AG - Apertura Gestionale del CDR");
		statiKeys.put(STATO_CG,"CG - Chiusura Gestionale del CDR");

		statiKeys_AC = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_AC.put(STATO_AC,statiKeys.get(STATO_AC));
		statiKeys_AC.put(STATO_CC,statiKeys.get(STATO_CC));

		statiKeys_CC = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_CC.put(STATO_CC,statiKeys.get(STATO_CC));
		statiKeys_CC.put(STATO_AC,statiKeys.get(STATO_AC));
		//statiKeys_CC.put(STATO_AD,statiKeys.get(STATO_AD));

		statiKeys_AD = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_AD.put(STATO_AD,statiKeys.get(STATO_AD));
		statiKeys_AD.put(STATO_AP,statiKeys.get(STATO_AP));

		statiKeys_AP = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_AP.put(STATO_AP,statiKeys.get(STATO_AP));
		statiKeys_AP.put(STATO_AD,statiKeys.get(STATO_AD));

		statiKeys_AG = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_AG.put(STATO_AG,statiKeys.get(STATO_AG));
		statiKeys_AG.put(STATO_CG,statiKeys.get(STATO_CG));

		statiKeys_CG = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_CG.put(STATO_CG,statiKeys.get(STATO_CG));
		statiKeys_CG.put(STATO_AG,statiKeys.get(STATO_AG));
	}

	private java.lang.String cambia_stato;
	
	private Boolean existDecisionaleE = Boolean.FALSE;	
	private Boolean existDecisionaleS = Boolean.FALSE;	
	private Boolean existDecisionaleC = Boolean.FALSE;
	private Boolean existDecisionaleR = Boolean.FALSE;
	private Boolean existGestionaleE = Boolean.FALSE;	
	private Boolean existGestionaleS = Boolean.FALSE;
	
	private SimpleBulkList dettagli_entrata= new SimpleBulkList();
	private BigDecimal importo_progetto;
	public Pdg_moduloBulk() {
		super();
	}
	public Pdg_moduloBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto) {
		super(esercizio, cd_centro_responsabilita, pg_progetto);
		setCdr(new CdrBulk(cd_centro_responsabilita));
		setProgetto(new Progetto_sipBulk(esercizio,pg_progetto,ProgettoBulk.TIPO_FASE_PREVISIONE));
	}

	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
		return cdr;
	}
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] 
		{getDettagli_entrata()};
	}
	public SimpleBulkList getDettagli_entrata() {
		return dettagli_entrata;
	}

	public void setDettagli_entrata(SimpleBulkList list) {
		dettagli_entrata = list;
	}
	public Pdg_Modulo_EntrateBulk  removeFromDettagli_entrata( int indiceDiLinea ) {
		Pdg_Modulo_EntrateBulk  element = (Pdg_Modulo_EntrateBulk )dettagli_entrata.get(indiceDiLinea);
		return (Pdg_Modulo_EntrateBulk )dettagli_entrata.remove(indiceDiLinea);
	}
	public int addToDettagli_entrata (Pdg_Modulo_EntrateBulk nuovo)
	{	
		nuovo.setTestata(this);
		nuovo.setIm_entrata(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_entrata_a2(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_entrata_a3(it.cnr.contab.util.Utility.ZERO);
		getDettagli_entrata().add(nuovo);
		return getDettagli_entrata().size()-1;
	}
	/**
	 * Restituisce il dizionario {@link java.util.Dictionary } degli stati.
	 * 
	 * @return java.util.Dictionary statiKeys
	 */
	public java.util.Dictionary getStatiKeys() {
		if (STATO_AC.equals(getStato()))
			return statiKeys_AC;
		else if (STATO_CC.equals(getStato()))
			return statiKeys_CC;
		else if (STATO_AD.equals(getStato()))
			return statiKeys_AD;
		else if (STATO_AP.equals(getStato()))
			return statiKeys_AP;
		else if (STATO_AG.equals(getStato()))
			return statiKeys_AG;
		else if (STATO_CG.equals(getStato()))
			return statiKeys_CG;
		else
			return statiKeys;
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
	{
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setStato(STATO_AC);
		return super.initializeForInsert(bp, context);
	}
	
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	public String getCd_centro_responsabilita() {
		it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
		if (cdr == null)
			return null;
		return getCdr().getCd_centro_responsabilita();
	}

	public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
		cdr = newCdr;
	}

	public it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk getProgetto() {
		return progetto;
	}

	public void setProgetto(
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto) {
		this.progetto = progetto;
	}
	
	public java.lang.Integer getPg_progetto() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = this.getProgetto();
	if (progetto == null)
		return null;
	return progetto.getPg_progetto();
	}

	public void setPg_progetto(java.lang.Integer progetto) {
		  this.getProgetto().setPg_progetto(progetto);
	}
	
	public java.lang.String getCd_progetto() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = this.getProgetto();
	if (progetto == null)
		return null;
	return progetto.getCd_progetto();
	}	

	public Progetto_sipBulk getProgettopadre() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = getProgetto();
	if (progetto == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = progetto.getProgettopadre();
	if (progettopadre == null)
		return null;
	return progettopadre;
	}

	public Progetto_sipBulk getProgettononno() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = getProgetto();
	if (progetto == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = progetto.getProgettopadre();
	if (progettopadre == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettononno = progettopadre.getProgettopadre();
	if (progettononno == null)
		return null;
	return progettononno;
	}
	
	public boolean isROprogetto() {
		if (!isToBeCreated())
			return true;
		else
			return false;
	}

	public java.lang.String getCambia_stato() {
		return cambia_stato;
	}

	public void setCambia_stato(java.lang.String string) {
		cambia_stato = string;
	}

	public void setStato(java.lang.String stato)  {
		super.setStato(stato);
		setCambia_stato(stato);
	}

	public BigDecimal getImporto_progetto() {
		return importo_progetto;
	}

	public void setImporto_progetto(BigDecimal decimal) {
		importo_progetto = decimal;
	}
	
	public Boolean getExistDecisionaleE() {
		return existDecisionaleE;
	}
	
	public void setExistDecisionaleE(Boolean existDecisionaleE) {
		this.existDecisionaleE = existDecisionaleE;
	}
	
	public Boolean getExistDecisionaleS() {
		return existDecisionaleS;
	}
	
	public void setExistDecisionaleS(Boolean existDecisionaleS) {
		this.existDecisionaleS = existDecisionaleS;
	}
	
	public Boolean getExistDecisionaleC() {
		return existDecisionaleC;
	}
	
	public void setExistDecisionaleC(Boolean existDecisionaleC) {
		this.existDecisionaleC = existDecisionaleC;
	}
	
	public Boolean getExistDecisionaleR() {
		return existDecisionaleR;
	}
	
	public void setExistDecisionaleR(Boolean existDecisionaleR) {
		this.existDecisionaleR = existDecisionaleR;
	}
	
	public Boolean getExistGestionaleE() {
		return existGestionaleE;
	}
	
	public void setExistGestionaleE(Boolean existGestionaleE) {
		this.existGestionaleE = existGestionaleE;
	}
	
	public Boolean getExistGestionaleS() {
		return existGestionaleS;
	}
	
	public void setExistGestionaleS(Boolean existGestionaleS) {
		this.existGestionaleS = existGestionaleS;
	}

}