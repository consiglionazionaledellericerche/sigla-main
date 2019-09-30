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
package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_situazione_sintetica_x_progettoBulk extends OggettoBulk {

	private Integer esercizio;

	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;

	private Unita_organizzativaBulk uoForPrint;
	
	protected it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettoForPrint;

	private WorkpackageBulk gaeForPrint;

	private TerzoBulk responsabileGaeForPrint;

	private boolean cdsForPrintEnabled;

	private boolean uoForPrintEnabled;
	
	private boolean progettoForPrintEnabled;

	private boolean gaeForPrintEnabled;

	private Boolean printGae;
	
	private Boolean printVoce;

	private Boolean printAnno;

	private Boolean printMovimentazione;

	private Boolean printSoloGaeAttive;

	private Boolean printPianoEconomico;

	private String ti_ordine_stampa;
	
	public final static String TI_ORDINE_GAE_VOCE_ANNO = "V";
	public final static String TI_ORDINE_GAE_ANNO_VOCE = "A";

	public final static java.util.Dictionary ti_ordine_stampaKeys;
	
	static {		
		ti_ordine_stampaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_ordine_stampaKeys.put(TI_ORDINE_GAE_VOCE_ANNO,"Voce/Anno");
		ti_ordine_stampaKeys.put(TI_ORDINE_GAE_ANNO_VOCE,"Anno/Voce");
	};
	
	public Stampa_situazione_sintetica_x_progettoBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}

	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}
	
	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint) {
		this.cdsForPrint = cdsForPrint;
	}
	
	public Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}
	
	public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
		this.uoForPrint = uoForPrint;
	}
	
	public it.cnr.contab.progettiric00.core.bulk.ProgettoBulk getProgettoForPrint() {
		return progettoForPrint;
	}
	
	public void setProgettoForPrint(it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettoForPrint) {
		this.progettoForPrint = progettoForPrint;
	}
	
	public WorkpackageBulk getGaeForPrint() {
		return gaeForPrint;
	}
	
	public void setGaeForPrint(WorkpackageBulk gaeForPrint) {
		this.gaeForPrint = gaeForPrint;
	}

	public TerzoBulk getResponsabileGaeForPrint() {
		return responsabileGaeForPrint;
	}
	
	public void setResponsabileGaeForPrint(TerzoBulk responsabileGaeForPrint) {
		this.responsabileGaeForPrint = responsabileGaeForPrint;
	}
	
	public void setCdsForPrintEnabled(boolean cdsForPrintEnabled) {
		this.cdsForPrintEnabled = cdsForPrintEnabled;
	}
	
	public void setUoForPrintEnabled(boolean uoForPrintEnabled) {
		this.uoForPrintEnabled = uoForPrintEnabled;
	}
	
	public void setProgettoForPrintEnabled(boolean progettoForPrintEnabled) {
		this.progettoForPrintEnabled = progettoForPrintEnabled;
	}
	
	public void setGaeForPrintEnabled(boolean gaeForPrintEnabled) {
		this.gaeForPrintEnabled = gaeForPrintEnabled;
	}
	
	public String getTi_ordine_stampa() {
		return ti_ordine_stampa;
	}
	
	public void setTi_ordine_stampa(String ti_ordine_stampa) {
		this.ti_ordine_stampa = ti_ordine_stampa;
	}
	
	public boolean isROFindCdsForPrint() {
		return !cdsForPrintEnabled || getCdUoForPrint()!=null;
	}

	public boolean isROCdsForPrint() {
		return isROFindCdsForPrint() || getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
	}
	
	public boolean isROFindUoForPrint() {
		return !uoForPrintEnabled || getCdCdsForPrint()==null || getCdGaeForPrint()!=null || getPgProgettoForPrint()!=null;
	}

	public boolean isROUoForPrint() {
		return isROFindUoForPrint() || getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}

	public boolean isROFindProgettoForPrint() {
		return !progettoForPrintEnabled || getCdUoForPrint()==null;
	}

	public boolean isROProgettoForPrint() {
		return isROFindProgettoForPrint() || getProgettoForPrint()==null || getProgettoForPrint().getCrudStatus()==NORMAL;
	}

	public boolean isROFindGaeForPrint() {
		return !gaeForPrintEnabled || getCdUoForPrint()==null;
	}

	public boolean isROGaeForPrint() {
		return isROFindGaeForPrint() || getGaeForPrint()==null || getGaeForPrint().getCrudStatus()==NORMAL;
	}

	public boolean isROFindResponsabileGaeForPrint() {
		return isROFindGaeForPrint() || getPgProgettoForPrint()==null;
	}

	public boolean isROResponsabileGaeForPrint() {
		return isROFindResponsabileGaeForPrint() || getResponsabileGaeForPrint()==null || getResponsabileGaeForPrint().getCrudStatus()==NORMAL;
	}

	public String getCdCdsForPrint() {
		return getCdsForPrint()!=null?getCdsForPrint().getCd_unita_organizzativa():null;
	}

	public Integer getCdResponsabileGaeForPrint() {
		return getResponsabileGaeForPrint()!=null?getResponsabileGaeForPrint().getCd_terzo():null;
	}

	public String getDsResponsabileGaeForPrint() {
		return getResponsabileGaeForPrint()!=null?getResponsabileGaeForPrint().getDenominazione_sede():null;
	}

	public String getCdUoForPrint() {
		return getUoForPrint()!=null?getUoForPrint().getCd_unita_organizzativa():null;
	}

	public Integer getPgProgettoForPrint() {
		return getProgettoForPrint()!=null?this.getProgettoForPrint().getPg_progetto():null;
	}

	public String getCdGaeForPrint() {
		return getGaeForPrint()!=null?getGaeForPrint().getCd_linea_attivita():null;
	}

	public String getCdsUoForPrint() {
		return getCdUoForPrint()!=null?getCdUoForPrint():getCdCdsForPrint();
	}

	public Boolean getPrintAnno() {
		return printAnno;
	}
	
	public void setPrintAnno(Boolean printAnno) {
		this.printAnno = printAnno;
	}
	
	public Boolean getPrintGae() {
		return printGae;
	}
	
	public void setPrintGae(Boolean printGae) {
		this.printGae = printGae;
	}

	public Boolean getPrintMovimentazione() {
		return printMovimentazione;
	}
	
	public void setPrintMovimentazione(Boolean printMovimentazione) {
		this.printMovimentazione = printMovimentazione;
	}
	
	public Boolean getPrintVoce() {
		return printVoce;
	}
	
	public void setPrintVoce(Boolean printVoce) {
		this.printVoce = printVoce;
	}

	public Boolean getPrintSoloGaeAttive() {
		return printSoloGaeAttive;
	}
	
	public void setPrintSoloGaeAttive(Boolean printSoloGaeAttive) {
		this.printSoloGaeAttive = printSoloGaeAttive;
	}
	
	public Boolean getPrintPianoEconomico() {
		return printPianoEconomico;
	}
	
	public void setPrintPianoEconomico(Boolean printPianoEconomico) {
		this.printPianoEconomico = printPianoEconomico;
	}

	@Override
	public void validate() throws ValidationException {
		if (getCdUoForPrint()==null)
			throw new ValidationException("E' obbligatorio indicare l'unità organizzativa.");
		if (getPgProgettoForPrint()==null)
			throw new ValidationException("E' obbligatorio indicare il progetto.");
		super.validate();
	}
}