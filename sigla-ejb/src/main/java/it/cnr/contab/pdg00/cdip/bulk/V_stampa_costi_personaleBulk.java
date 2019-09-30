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
* Date 04/04/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_stampa_costi_personaleBulk extends V_stampa_costi_personaleBase {

	public static final Integer MESE_0 = new Integer(0);
	public static final Integer MESE_1 = new Integer(1);
	public static final Integer MESE_2 = new Integer(2);
	public static final Integer MESE_3 = new Integer(3);
	public static final Integer MESE_4 = new Integer(4);
	public static final Integer MESE_5 = new Integer(5);
	public static final Integer MESE_6 = new Integer(6);
	public static final Integer MESE_7 = new Integer(7);
	public static final Integer MESE_8 = new Integer(8);
	public static final Integer MESE_9 = new Integer(9);
	public static final Integer MESE_10= new Integer(10);
	public static final Integer MESE_11= new Integer(11);
	public static final Integer MESE_12= new Integer(12);
	
	public static final String SOMME_RIPARTITE = "Somme ripartite";
	public static final String SOMME_NON_RIPARTITE = "Somme non ripartite";
	public static final String SOMME_ASSEGNATE_DA_ALTRA_UO = "Somme ricevute da altra UO";
	public static final String SOMME_ASSEGNATE_AD_ALTRA_UO = "Somme assegnate ad altra UO";
	
	public final static java.util.Dictionary meseKeys;
	public final static java.util.Dictionary sommeKeys;
	
	static 
	{
		meseKeys = new it.cnr.jada.util.OrderedHashtable();							
		meseKeys.put(MESE_0,  "Preventivo");
		meseKeys.put(MESE_1,  "Gennaio");	
		meseKeys.put(MESE_2,  "Febbraio");
		meseKeys.put(MESE_3,  "Marzo");
		meseKeys.put(MESE_4,  "Aprile");
		meseKeys.put(MESE_5,  "Maggio");
		meseKeys.put(MESE_6,  "Giugno");
		meseKeys.put(MESE_7,  "Luglio");
		meseKeys.put(MESE_8,  "Agosto");
		meseKeys.put(MESE_9,  "Settembre");
		meseKeys.put(MESE_10, "Ottobre");
		meseKeys.put(MESE_11, "Novembre");
		meseKeys.put(MESE_12, "Dicembre");
		
		sommeKeys = new it.cnr.jada.util.OrderedHashtable();
		sommeKeys.put(SOMME_RIPARTITE,  SOMME_RIPARTITE);
		sommeKeys.put(SOMME_NON_RIPARTITE,  SOMME_NON_RIPARTITE);
		sommeKeys.put(SOMME_ASSEGNATE_DA_ALTRA_UO,  SOMME_ASSEGNATE_DA_ALTRA_UO);
		sommeKeys.put(SOMME_ASSEGNATE_AD_ALTRA_UO,  SOMME_ASSEGNATE_AD_ALTRA_UO);
	};	
	public V_stampa_costi_personaleBulk() {
		super();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/06/2004 15.23.28)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}	
	/**
	 * Insert the method's description here.
	 * Creation date: (08/06/2004 15.23.28)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getSommeKeys() {
		return sommeKeys;
	}	
}