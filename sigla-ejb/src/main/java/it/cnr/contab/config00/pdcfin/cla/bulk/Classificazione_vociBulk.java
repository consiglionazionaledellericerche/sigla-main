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
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaClassBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Classificazione_vociBulk extends Classificazione_vociBase {
  	private java.lang.String cd_classificazione;
	private Classificazione_vociBulk classificazione_voci;
	private CdrBulk centro_responsabilita;
	protected BulkList classVociAssociate = new BulkList();
	protected BulkList pdgPianoRipartoSpese = new BulkList();
	private BigDecimal tot_imp_piano_riparto_spese;
	private BulkList<LimiteSpesaClassBulk> limitiSpesaClassColl = new BulkList();

	public static final String TIPO_CLASSIFICAZIONE_01 ="T01";
	public static final String TIPO_CLASSIFICAZIONE_02 ="T02";
	public static final String TIPO_CLASSIFICAZIONE_03 ="T03";
	public static final String TIPO_CLASSIFICAZIONE_04 ="T04";
	public static final String TIPO_CLASSIFICAZIONE_05 ="T05";
	public static final String TIPO_CLASSIFICAZIONE_06 ="T06";
	public static final String TIPO_CLASSIFICAZIONE_07 ="T07";
	
	public final static Dictionary ti_classificazioneKeys;
	static {
		ti_classificazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_01,"Tipologia 01");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_02,"Tipologia 02");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_03,"Tipologia 03");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_04,"Tipologia 04");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_05,"Tipologia 05");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_06,"Tipologia 06");
		ti_classificazioneKeys.put(TIPO_CLASSIFICAZIONE_07,"Tipologia 07");
    };

	public Classificazione_vociBulk() {
		super();
	}
	
	public Classificazione_vociBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	
	public int addToClassVociAssociate(Classificazione_vociBulk dett) {
		dett.setClassificazione_voci(this);
		dett.setTi_gestione(this.getTi_gestione());
		dett.setFl_mastrino(Boolean.FALSE);
		dett.setFl_class_sac(Boolean.FALSE);
		dett.setFl_solo_gestione(Boolean.FALSE);
		dett.setFl_piano_riparto(Boolean.FALSE);
		dett.setFl_accentrato(Boolean.FALSE);
		dett.setFl_decentrato(Boolean.FALSE);
		dett.setFl_esterna_da_quadrare_sac(Boolean.FALSE);
		dett.setCd_livello1(this.getCd_livello1());
		dett.setCd_livello2(this.getCd_livello2());
		dett.setCd_livello3(this.getCd_livello3());
		dett.setCd_livello4(this.getCd_livello4());
		dett.setCd_livello5(this.getCd_livello5());
		dett.setCd_livello6(this.getCd_livello6());		
		dett.setCd_livello7(this.getCd_livello7());
		dett.setFl_prev_obb_anno_suc(Boolean.FALSE);
		getClassVociAssociate().add(dett);
		return getClassVociAssociate().size()-1;
	}	

	public int addToPdgPianoRipartoSpese(Pdg_piano_ripartoBulk dett) {
		dett.setV_classificazione_voci((V_classificazione_vociBulk)this);
		dett.setEsercizio(this.getEsercizio());
		getPdgPianoRipartoSpese().add(dett);
		return getPdgPianoRipartoSpese().size()-1;
	}

	public int addToLimitiSpesaClassColl(LimiteSpesaClassBulk dett) {
		dett.setV_classificazione_voci((V_classificazione_vociBulk)this);
		getLimitiSpesaClassColl().add(dett);
		return getLimitiSpesaClassColl().size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getClassVociAssociate(), getPdgPianoRipartoSpese(), getLimitiSpesaClassColl()};
	}
	
	public Classificazione_vociBulk removeFromClassVociAssociate(int index) {
		Classificazione_vociBulk dett = (Classificazione_vociBulk)getClassVociAssociate().remove(index);
		return dett;
	}
	
	public Pdg_piano_ripartoBulk removeFromPdgPianoRipartoSpese(int index) {
		Pdg_piano_ripartoBulk dett = (Pdg_piano_ripartoBulk)getPdgPianoRipartoSpese().remove(index);
		return dett;
	}

	public LimiteSpesaClassBulk removeFromLimitiSpesaClassColl(int index) {
		LimiteSpesaClassBulk dett = (LimiteSpesaClassBulk)getLimitiSpesaClassColl().remove(index);
		return dett;
	}

 	protected Classificazione_vociBulk(String ti_gestione, String cd_livello1, String cd_livello2, String cd_livello3, String cd_livello4, String cd_livello5, String cd_livello6, String cd_livello7) {
		super();
		setTi_gestione(ti_gestione);
		setCd_livello1(cd_livello1);
		setCd_livello2(cd_livello2);
		setCd_livello3(cd_livello3);
		setCd_livello4(cd_livello4);
		setCd_livello5(cd_livello5);
		setCd_livello6(cd_livello6);
		setCd_livello7(cd_livello7);
	}
	
	protected Classificazione_vociBulk(Classificazione_vociBulk liv_pre, String cd_livello) {
		this(liv_pre.getTi_gestione(), liv_pre.getCd_livello1(), liv_pre.getCd_livello2(), liv_pre.getCd_livello3(), liv_pre.getCd_livello4(), liv_pre.getCd_livello5(), liv_pre.getCd_livello6(), liv_pre.getCd_livello7());

		if (liv_pre instanceof Classificazione_voci_etr_liv1Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv1Bulk)
			setCd_livello2(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_etr_liv2Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv2Bulk)
			setCd_livello3(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_etr_liv3Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv3Bulk)
			setCd_livello4(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_etr_liv4Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv4Bulk)
			setCd_livello5(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_etr_liv5Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv5Bulk)
			setCd_livello6(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_etr_liv6Bulk||
		    liv_pre instanceof Classificazione_voci_spe_liv6Bulk)
			setCd_livello7(cd_livello);						
		setClassificazione_voci(liv_pre);
	}

	/*
	 * Ritorna il Classificazione_vociBulk che gestisce il campo id_class_padre
	 */
	public Classificazione_vociBulk getClassificazione_voci() {
		return classificazione_voci;
	}

	/*
	 * Imposta il Classificazione_vociBulk che gestisce il campo id_class_padre
	 */
	public void setClassificazione_voci(Classificazione_vociBulk bulk) {
		classificazione_voci = bulk;
	}

	public java.lang.Integer getId_class_padre () {
		if (getClassificazione_voci()==null) return null;
		return getClassificazione_voci().getId_classificazione();
	}
	public void setId_class_padre(java.lang.Integer id_class_padre)  {
		getClassificazione_voci().setId_classificazione(id_class_padre);
	}

	public java.lang.String getCd_classificazione() {
		if (cd_classificazione == null) {
			String cod = null;
			for (int i=1; i<=7; i++) {
				if (getCd_livello(i) != null) {
					if (i == 1)
						cod = getCd_livello(i);
					else
						cod = cod.concat("." + getCd_livello(i));
				}
			}
			return cod;
		}
		return cd_classificazione;
	}
	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	public BulkList getClassVociAssociate() {
		return classVociAssociate;
	}

	public BulkList getPdgPianoRipartoSpese() {
		return pdgPianoRipartoSpese;
	}

	public void setClassVociAssociate(BulkList list) {
		classVociAssociate = list;
	}

	public void setPdgPianoRipartoSpese(BulkList list) {
		pdgPianoRipartoSpese = list;
	}

	/*
	 * Restituisce il numero dell'ultimo livello caricato del Bulk di riferimento 
	 */
	public Integer getLivelloMax() {
		if (getCd_livello7() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_SETTIMO);
		else if (getCd_livello6() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_SESTO);
		else if (getCd_livello5() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_QUINTO);
		else if (getCd_livello4() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_QUARTO);
		else if (getCd_livello3() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_TERZO);
		else if (getCd_livello2() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_SECONDO);
		else if (getCd_livello1() != null)
			return new Integer(Classificazione_vociHome.LIVELLO_PRIMO);
		return new Integer(Classificazione_vociHome.LIVELLO_MIN);
	}

	/*
	 * Restituisce il codice del livello <liv> richiesto 
	 */
	public String getCd_livello(int liv) {
		if (liv==1)
			return getCd_livello1();
		else if (liv==2)
			return getCd_livello2();
		else if (liv==3)
			return getCd_livello3();
		else if (liv==4)
			return getCd_livello4();
		else if (liv==5)
			return getCd_livello5();
		else if (liv==6)
			return getCd_livello6();
		else if (liv==7)
			return getCd_livello7();
		return getCd_livello1();
	}

	/*
	 * Attribuisce al livello <liv> indicato il valore <valore> fornito 
	 */
	public void setCd_livello(String valore, int liv) {
		if (liv==1)
			setCd_livello1(valore);
		else if (liv==2)
			setCd_livello2(valore);
		else if (liv==3)
			setCd_livello3(valore);
		else if (liv==4)
			setCd_livello4(valore);
		else if (liv==5)
			setCd_livello5(valore);
		else if (liv==6)
			setCd_livello6(valore);
		else if (liv==7)
			setCd_livello7(valore);
		else
			setCd_livello1(valore);
	}

	public OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		if (crudbp==null || !crudbp.isSearching()) {
			setFl_mastrino(Boolean.FALSE);
			setFl_class_sac(Boolean.FALSE);
			setFl_solo_gestione(Boolean.FALSE);
			setFl_piano_riparto(Boolean.FALSE);
			setFl_accentrato(Boolean.FALSE);
			setFl_decentrato(Boolean.FALSE);
			setFl_esterna_da_quadrare_sac(Boolean.FALSE);
			setFl_prev_obb_anno_suc(Boolean.FALSE);
		}
		return super.initialize(crudbp, actioncontext);
	}

	/*
	 * Restituisce la somma degli importi del Piano Riparto Spese associato alla classificazione  
	 */
	public BigDecimal getTot_imp_piano_riparto_spese() {
		return tot_imp_piano_riparto_spese;
	}

	/*
	 * Inizializza la somma degli importi del Piano Riparto Spesem, associato alla classificazione,
	 * con il valore <decimal> indicato  
	 */
	public void setTot_imp_piano_riparto_spese(BigDecimal decimal) {
		tot_imp_piano_riparto_spese = decimal;
	}
	/*
	 * Ritorna il CdrBulk che gestisce il campo cdr_accentratore
	 */
	public CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}

	/*
	 * Imposta il CdrBulk che gestisce il campo cdr_accentratore
	 */
	public void setCentro_responsabilita(CdrBulk bulk) {
		centro_responsabilita = bulk;
	}

	public String getCdr_accentratore() {
		if (getCentro_responsabilita()==null) return null;
		return getCentro_responsabilita().getCd_centro_responsabilita();
	}

	public void setCdr_accentratore(String cdr_accentratore) {
		getCentro_responsabilita().setCd_centro_responsabilita(cdr_accentratore);
	}

	public void validate() throws ValidationException {
		super.validate();
		if (getFl_accentrato().equals(Boolean.TRUE) &&  getCdr_accentratore()==null)
			throw new ValidationException("E' obbligatorio indicare il CDR accentratore.");
		else if (getFl_accentrato().equals(Boolean.FALSE) &&  getCdr_accentratore()!=null)
			setCentro_responsabilita(null);
	}
	
	public static Dictionary getTi_classificazioneKeys() {
		return ti_classificazioneKeys;
	}

	public BulkList<LimiteSpesaClassBulk> getLimitiSpesaClassColl() {
		return limitiSpesaClassColl;
	}

	public void setLimitiSpesaClassColl(BulkList<LimiteSpesaClassBulk> limitiSpesaClassColl) {
		this.limitiSpesaClassColl = limitiSpesaClassColl;
	}

	public BigDecimal getImLimiteAssestatoRipartito() {
		return Optional.ofNullable(getLimitiSpesaClassColl())
				.map(List::stream)
				.orElse(Stream.empty())
				.map(LimiteSpesaClassBulk::getIm_limite_assestato)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}