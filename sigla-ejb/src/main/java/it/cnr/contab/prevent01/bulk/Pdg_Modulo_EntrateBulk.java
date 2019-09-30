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
* Date 16/09/2005
*/
package it.cnr.contab.prevent01.bulk;

import java.sql.Timestamp;
import java.util.Iterator;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;

public class Pdg_Modulo_EntrateBulk extends Pdg_Modulo_EntrateBase {
	public Pdg_Modulo_EntrateBulk() {
		super();
	}
	public Pdg_Modulo_EntrateBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.String cd_natura, java.lang.Integer id_classificazione, java.lang.Long pg_dettaglio,java.lang.String cd_cds_area) {
		super();
		setTestata(new Pdg_moduloBulk(esercizio,cd_centro_responsabilita,pg_progetto));
//		setCdr_linea_att(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
		setNatura(new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
		setClassificazione_voci(new V_classificazione_vociBulk(id_classificazione));
		setPg_dettaglio(pg_dettaglio);
		setArea(new CdsBulk(cd_cds_area));
	}
	
	private Voce_piano_economico_prgBulk voce_piano_economico;
	
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk contraente;
	
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr_linea_att;
	private CdsBulk area;
	private WorkpackageBulk linea_attivita;
	
	private NaturaBulk natura;
	
	private String cd_ds_natura;
	
	private V_classificazione_vociBulk classificazione_voci;
	
	private java.util.Collection nature;
	 
	private Pdg_moduloBulk testata; 
	
	private SimpleBulkList dettagli_gestionali = new SimpleBulkList();
	private java.math.BigDecimal dettagli_gestionali_tot;
		
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getContraente() {
		return contraente;
	}
	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}

	public void setContraente(
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk bulk) {
		contraente = bulk;
	}

	public void setLinea_attivita(WorkpackageBulk bulk) {
		linea_attivita = bulk;
		natura = linea_attivita.getNatura();
	}

	public NaturaBulk getNatura() {
		return natura;
	}

	public void setNatura(NaturaBulk bulk) {
		natura = bulk;
	}
	
	public V_classificazione_vociBulk getClassificazione_voci() {
		return classificazione_voci;
	}
	public void setId_classificazione(Integer id_classificazione) {
			getClassificazione_voci().setId_classificazione(id_classificazione);	
	}
	public Integer getId_classificazione() {
			if (getClassificazione_voci()==null)
				return null;
			return getClassificazione_voci().getId_classificazione();
	}
	public java.lang.Integer getCd_terzo() {
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getContraente();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	public void setCd_natura(java.lang.String cd_natura) {
		this.getNatura().setCd_natura(cd_natura);
	}
	public java.lang.String getCd_natura() {
			NaturaBulk natura = this.getNatura();
			if (natura == null)
				return null;
			return natura.getCd_natura();
	}
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.getContraente().setCd_terzo(cd_terzo);
	}
	public void setCdr_linea(java.lang.String cd_cdr_linea) {
		this.getLinea_attivita().setCd_centro_responsabilita(cd_cdr_linea);
	}
	
	public java.lang.String getCdr_linea() {
			WorkpackageBulk cdr_linea = this.getLinea_attivita();
			if (cdr_linea == null)
					return null;
				return cdr_linea.getCd_centro_responsabilita();
	}
	public void setCd_linea_attivita(java.lang.String linea) {
			this.getLinea_attivita().setCd_linea_attivita(linea);
	}
	
	public java.lang.String getCd_linea_attivita() {
		WorkpackageBulk linea = this.getLinea_attivita();
			if (linea == null)
					return null;
			return linea.getCd_linea_attivita();
	}
	
	public void setClassificazione_voci(V_classificazione_vociBulk bulk) {
		classificazione_voci = bulk;
	}

	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr_linea_att() {
		return cdr_linea_att;
	}
	public void setCdr_linea_att(
		it.cnr.contab.config00.sto.bulk.CdrBulk bulk) {
		cdr_linea_att = bulk;
	} 
	public void validate(UserContext userContext) throws ValidationException 
	{
		
		for(Iterator i=this.getTestata().getDettagli_entrata().iterator();i.hasNext();)
		{
			Pdg_Modulo_EntrateBulk dett=(Pdg_Modulo_EntrateBulk)i.next();
			if (dett.getCd_cds_area() == null ){
				dett.setArea(new CdsBulk(CNRUserContext.getCd_cds(userContext)));
			}
					
			
			/*if (dett.getIm_spese_vive() == null )
				throw new ValidationException( "Il campo ''Importo Spese vive correlate all'entrata'' deve essere valorizzato.");
		
			if (dett.getDs_spese_vive() == null )
				throw new ValidationException( "Il campo ''Descrizione Spese vive correlate all'entrata'' deve essere valorizzato.");
			if (dett.getEsercizio_inizio() == null )
				throw new ValidationException( "Il campo ''Anno di Inizio'' deve essere valorizzato. " );
			
			if (dett.getEsercizio_fine() == null )
			{
				throw new ValidationException( "Il campo ''Anno di Fine'' deve essere valorizzato. " );
			}*/
			
		if (dett.getEsercizio_inizio()!=null && dett.getEsercizio_fine()!=null){
			if ( dett.getEsercizio_fine().compareTo( dett.getEsercizio_inizio()) < 0 )
				throw new ValidationException( "L' Anno di fine non può essere minore dell'anno di Inizio. " );
	
			if ( dett.getEsercizio_fine().toString().length() != 4 )
				throw new ValidationException( "Il campo Anno di Fine deve essere di quattro cifre. " );
			}
		}
	}
	public java.util.Collection getNature() {
		return nature;
	}

	public void setNature(java.util.Collection collection) {
		nature = collection;
	}
	public String getCd_ds_natura() {
		return getNatura().getCd_ds_natura();
	}
	
	public void setCd_ds_natura(String string) {
		cd_ds_natura = string;
	}
    public Integer getEsercizio(){
    	return this.getTestata().getEsercizio();
    }
	public void setEsercizio(Integer eser){
		this.getTestata().setEsercizio(eser);
	}
	public Integer getPg_progetto(){
	   return this.getTestata().getPg_progetto();
	}
	public void setPg_progetto(Integer prog){
	 this.getTestata().setPg_progetto(prog);
	}
	public void setCd_centro_responsabilita(String cdr){
	 this.getTestata().setCd_centro_responsabilita(cdr);
	}
	public String getCd_centro_responsabilita(){
	 return this.getTestata().getCd_centro_responsabilita();
	}
	
	public Pdg_moduloBulk getTestata() {
		return testata;
	}
	public void setTestata(Pdg_moduloBulk bulk) {
		testata = bulk;
	}
	
	
	public CdsBulk getArea() {
		return area;
	}

	public void setArea(CdsBulk bulk) {
		area = bulk;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
	   getArea().setCd_unita_organizzativa(cd_cds_area);
	}
	public java.lang.String getCd_cds_area () {
		if(getArea() != null)
		  return getArea().getCd_unita_organizzativa();
		return null;  
	}
	public boolean isROClassificazione_voci() {
		return getCrudStatus() != 1;
		
	}
	public boolean isRONatura() {
		return getCrudStatus() != 1;		
	}
	public boolean isROCd_cds_area() {
			return getCrudStatus() != 1;		
	}
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] 
		{getDettagli_gestionali()};
	}
	public SimpleBulkList getDettagli_gestionali() {
		return dettagli_gestionali;
	}
	public void setDettagli_gestionali(SimpleBulkList list) {
		dettagli_gestionali = list;
	}
	public Pdg_modulo_entrate_gestBulk removeFromDettagli_gestionali(int indiceDiLinea) {
		Pdg_modulo_entrate_gestBulk element = (Pdg_modulo_entrate_gestBulk)dettagli_gestionali.get(indiceDiLinea);
		return (Pdg_modulo_entrate_gestBulk)dettagli_gestionali.remove(indiceDiLinea);
	}
	public int addToDettagli_gestionali(Pdg_modulo_entrate_gestBulk nuovo)
	{	
		nuovo.setPdg_modulo_entrate(this);
		nuovo.setFl_sola_lettura(new Boolean(false));
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		nuovo.setDt_registrazione(today);
		nuovo.setIm_entrata(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_incassi(it.cnr.contab.util.Utility.ZERO);
		getDettagli_gestionali().add(nuovo);
		return getDettagli_gestionali().size()-1;
	}
	/**
	 * individua la quota di previsione già assegnata
	 */
	public java.math.BigDecimal getDettagli_gestionali_tot() {
		return dettagli_gestionali_tot;
	}
	public void setDettagli_gestionali_tot(java.math.BigDecimal decimal) {
		dettagli_gestionali_tot = decimal;
	}
	/**
	 * individua la quota di previsione ancora da assegnare
	 */
	public java.math.BigDecimal getDettagli_gestionali_res() {
		return Utility.nvl(getIm_entrata()).subtract(Utility.nvl(getDettagli_gestionali_tot()));
	}

	public Voce_piano_economico_prgBulk getVoce_piano_economico() {
		return voce_piano_economico;
	}

	public void setVoce_piano_economico(Voce_piano_economico_prgBulk voce_piano_economico) {
		this.voce_piano_economico = voce_piano_economico;
	}	

	@Override
	public String getCd_unita_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_unita_organizzativa();
	}
	
	@Override
	public void setCd_unita_piano(String cd_unita_piano) {
		this.getVoce_piano_economico().setCd_unita_organizzativa(cd_unita_piano);
	}
	
	@Override
	public String getCd_voce_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getVoce_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
}
