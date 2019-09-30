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
* Creted by Generator 1.0
* Date 04/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.Persistent;
public class Param_cons_costi_personaleBulk extends OggettoBulk implements Persistent{

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
	private java.lang.Integer mese;
	
	private ProgettoBulk v_commessa;
	private ProgettoBulk v_modulo;
	private V_dipendenteBulk v_dipendente;
	private Unita_organizzativaBulk v_uo;
	
	private java.lang.Integer esercizio;
	
	private java.lang.String cds;
	private java.lang.String uo;
	private boolean roFindV_uo;
	

	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo) {
		this.uo = uo;
	}

	static 
	{
		meseKeys = new it.cnr.jada.util.OrderedHashtable();							
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
		
	};	
	public Param_cons_costi_personaleBulk() {
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
	
	public ProgettoBulk getV_commessa() {
		return v_commessa;
	}
	public void setV_commessa(ProgettoBulk v_commessa) {
		this.v_commessa = v_commessa;
	}
	public ProgettoBulk getV_modulo() {
		return v_modulo;
	}
	public void setV_modulo(ProgettoBulk v_modulo) {
		this.v_modulo = v_modulo;
	}
	public V_dipendenteBulk getV_dipendente() {
		return v_dipendente;
	}
	public void setV_dipendente(V_dipendenteBulk v_dipendente) {
		this.v_dipendente = v_dipendente;
	}
	public Unita_organizzativaBulk getV_uo() {
		return v_uo;
	}
	public void setV_uo(Unita_organizzativaBulk v_uo) {
		this.v_uo = v_uo;
	}	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds) {
		this.cds = cds;
	}
	public java.lang.Integer getMese () {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	
	public String getId_matricola(){
		if(getV_dipendente()==null)
			return null;
		return getV_dipendente().getId_matricola();
	}
	
	public void setId_matricola(String matricola){
		if(getV_dipendente()!=null)
			getV_dipendente().setId_matricola(matricola);
	}
	
	public String getNominativo() {
		if(getV_dipendente()==null) 
			return null;
		return getV_dipendente().getNominativo();
	}
	
	public void setNominativo(String nominativo){
		if(getV_dipendente()!=null)
			getV_dipendente().setNominativo(nominativo);
	}
	

	public String getCd_commessa() {

		if (getV_commessa()==null)
			return null;

		return getV_commessa().getCd_progetto();
	}
	
	public String getCd_modulo() {

		if (getV_modulo()==null)
			return null;
		
		return getV_modulo().getCd_progetto();
	}
	
	public String getCd_uo(){
		if(getV_uo()==null)
			return null;
		return getV_uo().getCd_unita_organizzativa();
	}
	
	public void setCd_uo(String uo){
		if(getV_uo()!=null)
			getV_uo().setCd_unita_organizzativa(uo);
	}
	
	public String getDs_uo() {
		if(getV_uo()==null) 
			return null;
		return getV_uo().getDs_unita_organizzativa();
	}
	
	public void setDs_uo(String ds_uo){
		if(getV_uo()!=null)
			getV_uo().setDs_unita_organizzativa(ds_uo);
	}
	
	public void validaMese() throws ValidationException 
	{
		if(getMese() == null)
			throw new ValidationException("Selezionare il Mese");
	}
	
	public void validaUo() throws ValidationException{
		if (getCd_uo()== null)
			throw new ValidationException("Selezionare l'Unità Organizzativa");
	}
	
	public boolean isROFindV_uo() {
//		return  getV_uo() == null && getV_uo().getCrudStatus() == OggettoBulk.NORMAL; 
		return roFindV_uo;
	}
	

	public void setROFindV_uo(boolean b) {
		roFindV_uo = b;
	}
}