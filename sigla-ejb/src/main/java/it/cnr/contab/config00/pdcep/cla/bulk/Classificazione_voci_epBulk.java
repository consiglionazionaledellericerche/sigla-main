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
package it.cnr.contab.config00.pdcep.cla.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Classificazione_voci_epBulk extends Classificazione_voci_epBase {
  	private java.lang.String cd_classificazione;
	private Classificazione_voci_epBulk classificazione_voci_ep;
	protected BulkList classVociAssociate = new BulkList();
	
	public Classificazione_voci_epBulk() {
		super();
	}
	
	public Classificazione_voci_epBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	
	public int addToClassVociAssociate(Classificazione_voci_epBulk dett) {
		dett.setClassificazione_voci_ep(this);
		dett.setTipo(this.getTipo());
		dett.setFl_mastrino(Boolean.FALSE);
		dett.setCd_livello1(this.getCd_livello1());
		dett.setCd_livello2(this.getCd_livello2());
		dett.setCd_livello3(this.getCd_livello3());
		dett.setCd_livello4(this.getCd_livello4());
		dett.setCd_livello5(this.getCd_livello5());
		dett.setCd_livello6(this.getCd_livello6());		
		dett.setCd_livello7(this.getCd_livello7());
		dett.setCd_livello8(this.getCd_livello8());
		getClassVociAssociate().add(dett);
		return getClassVociAssociate().size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getClassVociAssociate()};
	}
	
	public Classificazione_voci_epBulk removeFromClassVociAssociate(int index) {
		Classificazione_voci_epBulk dett = (Classificazione_voci_epBulk)getClassVociAssociate().remove(index);
		return dett;
	}
	protected Classificazione_voci_epBulk(String tipo, String cd_livello1, String cd_livello2, String cd_livello3, 
	String cd_livello4, String cd_livello5, String cd_livello6, String cd_livello7, String cd_livello8) {
		super();
		setTipo(tipo);
		setCd_livello1(cd_livello1);
		setCd_livello2(cd_livello2);
		setCd_livello3(cd_livello3);
		setCd_livello4(cd_livello4);
		setCd_livello5(cd_livello5);
		setCd_livello6(cd_livello6);
		setCd_livello7(cd_livello7);
		setCd_livello8(cd_livello8);
	}
	
	protected Classificazione_voci_epBulk(Classificazione_voci_epBulk liv_pre, String cd_livello) {
		this(liv_pre.getTipo(), liv_pre.getCd_livello1(), liv_pre.getCd_livello2(), liv_pre.getCd_livello3(), liv_pre.getCd_livello4(), liv_pre.getCd_livello5(), liv_pre.getCd_livello6(), liv_pre.getCd_livello7(), liv_pre.getCd_livello8());

		if (liv_pre instanceof Classificazione_voci_ep_eco_liv1Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv1Bulk)
			setCd_livello2(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv2Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv2Bulk)
			setCd_livello3(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv3Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv3Bulk)
			setCd_livello4(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv4Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv4Bulk)
			setCd_livello5(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv5Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv5Bulk)
			setCd_livello6(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv6Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv6Bulk)
			setCd_livello7(cd_livello);						
		if (liv_pre instanceof Classificazione_voci_ep_eco_liv7Bulk||
		    liv_pre instanceof Classificazione_voci_ep_pat_liv7Bulk)
		  setCd_livello8(cd_livello);						  
			
		setClassificazione_voci_ep(liv_pre);
	}

	/*
	 * Ritorna il Classificazione_voci_epBulk che gestisce il campo id_class_padre
	 */
	public Classificazione_voci_epBulk getClassificazione_voci_ep() {
		return classificazione_voci_ep;
	}

	/*
	 * Imposta il Classificazione_voci_epBulk che gestisce il campo id_class_padre
	 */
	public void setClassificazione_voci_ep(Classificazione_voci_epBulk bulk) {
		classificazione_voci_ep = bulk;
	}

	public java.lang.Integer getId_class_padre () {
		if (getClassificazione_voci_ep()==null) return null;
		return getClassificazione_voci_ep().getId_classificazione();
	}
	public void setId_class_padre(java.lang.Integer id_class_padre)  {
		getClassificazione_voci_ep().setId_classificazione(id_class_padre);
	}

	public java.lang.String getCd_classificazione() {
		if (cd_classificazione == null) {
			String cod = null;
			for (int i=1; i<=8; i++) {
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

	public void setClassVociAssociate(BulkList list) {
		classVociAssociate = list;
	}

	/*
	 * Restituisce il numero dell'ultimo livello caricato del Bulk di riferimento 
	 */
	public Integer getLivelloMax() {
		if (getCd_livello8() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_OTTAVO);
		else if (getCd_livello7() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_SETTIMO);
		else if (getCd_livello6() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_SESTO);
		else if (getCd_livello5() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_QUINTO);
		else if (getCd_livello4() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_QUARTO);
		else if (getCd_livello3() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_TERZO);
		else if (getCd_livello2() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_SECONDO);
		else if (getCd_livello1() != null)
			return new Integer(Classificazione_voci_epHome.LIVELLO_PRIMO);
		return new Integer(Classificazione_voci_epHome.LIVELLO_MIN);
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
		else if (liv==8)
			return getCd_livello8();
			
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
		else if (liv==8)
			setCd_livello8(valore);
		else
			setCd_livello1(valore);
	}

	public OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		if (crudbp==null || !crudbp.isSearching()) {
			setFl_mastrino(Boolean.FALSE);
			}
		return super.initialize(crudbp, actioncontext);
	}
	
	public void validate() throws ValidationException {
		super.validate();
	}
}