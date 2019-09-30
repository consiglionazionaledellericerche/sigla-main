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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.consultazioni.bp;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.pdg00.consultazioni.bulk.V_cons_pdg_assestato_aggregatoBulk;
import it.cnr.contab.pdg00.consultazioni.ejb.ConsPDGAssestatoAggregatoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGAssestatoAggregatoBP extends ConsultazioniBP {
	public static final String LIVELLO_DIP = "DIP";
	public static final String LIVELLO_CDS = "CDS";
	public static final String LIVELLO_UO = "UO";
	public static final String LIVELLO_LIV1 = "LIV1";
	public static final String LIVELLO_LIV2 = "LIV2";
	public static final String LIVELLO_DET = "DET";

    private String livelloConsultazione;
	private String pathConsultazione;
	private String ds_livello1;
	private String ds_livello2;

	public ConsPDGAssestatoAggregatoComponentSession createPdgAssestatoAggregatoComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsPDGAssestatoAggregatoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_ConsPDGAssestatoAggregatoComponentSession",ConsPDGAssestatoAggregatoComponentSession.class);
	}

	public Classificazione_vociComponentSession createClassificazioneVociComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (Classificazione_vociComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

		CompoundFindClause clauses = new CompoundFindClause();
		clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
		setBaseclause(clauses);

		if (getPathConsultazione()==null) {
			setPathConsultazione(this.LIVELLO_DIP);					
			setLivelloConsultazione(this.LIVELLO_DIP);					
		}
		super.init(config,context);
		initVariabili(context, null, this.LIVELLO_DIP);
	}		

	public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (pathProvenienza == null && livello_destinazione.equals(this.LIVELLO_DIP)) {
				setPathConsultazione(this.LIVELLO_DIP);					
				setLivelloConsultazione(this.LIVELLO_DIP);					
			}
			else
			{
				setPathConsultazione(pathProvenienza.concat(livello_destinazione));
				setLivelloConsultazione(livello_destinazione);
			}
	
			setSearchResultColumnSet(getPathConsultazione());
			setFreeSearchSet(getPathConsultazione());
			setTitle();
			setDs_livello1(getDs_livello1(context.getUserContext()));
			setDs_livello2(getDs_livello2(context.getUserContext()));
			setField("cd_livello1", getDs_livello1(context.getUserContext()));
			setField("cd_livello2", getDs_livello2(context.getUserContext()));
			if (livello_destinazione.equals(this.LIVELLO_DET))
				setMultiSelection(false);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		if (getLivelloConsultazione().equals(this.LIVELLO_DIP)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cds");
			button.setSeparator(true);
			listButton.addElement(button);

			Button buttonLiv1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello1");
			buttonLiv1.setLabel(getDs_livello1());
			listButton.addElement(buttonLiv1);
		}
		if (getLivelloConsultazione().equals(this.LIVELLO_LIV1)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cds");
			button.setSeparator(true);
			listButton.addElement(button);

			Button buttonLiv2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello2");
			buttonLiv2.setLabel(getDs_livello2());
			listButton.addElement(buttonLiv2);
		}
		if (getLivelloConsultazione().equals(this.LIVELLO_LIV2)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cds");
			button.setSeparator(true);

			listButton.addElement(button);
		}
		if (getLivelloConsultazione().equals(this.LIVELLO_CDS)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.uo");
			button.setSeparator(true);

			listButton.addElement(button);
		}
		if (getLivelloConsultazione().equals(this.LIVELLO_UO)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
			button.setSeparator(true);

			listButton.addElement(button);
		}

		return listButton;
	}	

	public String getLivelloConsultazione() {
		return livelloConsultazione;
	}
	public void setLivelloConsultazione(String string) {
		livelloConsultazione = string;
	}
	public String getPathConsultazione() {
		return pathConsultazione;
	}
	public void setPathConsultazione(String string) {
		pathConsultazione = string;
	}
    public String getPathDestinazione(String destinazione) {
		return getPathConsultazione().concat(destinazione);
    }
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi al Dipartimento 
     *
	 * @return boolean 
	 */
	public boolean isPresenteDIP() {
		return getPathConsultazione().indexOf(LIVELLO_DIP)>=0;
	}
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi al primo livello della
	 * Classificazione Ufficiale 
     *
	 * @return boolean 
	 */
	public boolean isPresenteLIV1() {
		return getPathConsultazione().indexOf(LIVELLO_LIV1)>=0;
	}
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi al secondo livello della
	 * Classificazione Ufficiale 
     *
	 * @return boolean 
	 */
	public boolean isPresenteLIV2() {
		return getPathConsultazione().indexOf(LIVELLO_LIV2)>=0;
	}
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi al CDS
	 * @return boolean 
     *
	 */
	public boolean isPresenteCDS() {
		return getPathConsultazione().indexOf(LIVELLO_CDS)>=0;
	}
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi all'Unità Organizzativa
     *
	 * @return boolean 
	 */
	public boolean isPresenteUO() {
		return getPathConsultazione().indexOf(LIVELLO_UO)>=0;
	}
	/**
	 * Indica se la consultazione attuale contiene anche i campi relativi al dettaglio finale
     *
	 * @return boolean 
	 */
	public boolean isPresenteDET() {
		return getPathConsultazione().indexOf(LIVELLO_DET)>=0;
	}
	/**
	 * Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
	 * sulla base del path della consultazione
	 */
	public void setTitle() {
		String title=null;
		if (this instanceof ConsPDGAssestatoAggregatoEtrBP)
			title = "Entrate";
		else
			title = "Spese";
		
		if (isPresenteDIP()) title = title.concat(" - Dipartimento");
		if (isPresenteLIV1()) title = title.concat("\\Titolo");
		if (isPresenteLIV2()) title = title.concat("\\Categoria");
		if (isPresenteCDS()) title = title.concat("\\Cds");
		if (isPresenteUO()) title = title.concat("\\Unità Organizzativa");
		if (isPresenteDET()) title = title.concat("\\Dettagli");
		getBulkInfo().setShortDescription(title);
		if (this instanceof ConsPDGAssestatoAggregatoEtrBP)
			getBulkInfo().setLongDescription("Consultazione Assestato Entrate PDG");
		else
			getBulkInfo().setLongDescription("Consultazione Assestato Spese PDG");
	}
	public String getDs_livello1() {
		return ds_livello1;
	}
	public String getDs_livello2() {
		return ds_livello2;
	}
	public void setDs_livello1(String string) {
		ds_livello1 = string;
	}
	public void setDs_livello2(String string) {
		ds_livello2 = string;
	}
	/**
	 * Ritorna la descrizione del primo livello della classificazione ufficiale
	 *
	 * @param userContext il context di riferimento 
	 * @return String la descrizione del livello richiesto 
	 */
	public String getDs_livello1(UserContext userContext) throws BusinessProcessException {
		try {
			if (getDs_livello1()==null) {
				if (this instanceof ConsPDGAssestatoAggregatoSpeBP)
					setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																										   CNRUserContext.getEsercizio(userContext),
																										   Elemento_voceHome.GESTIONE_SPESE,
																										   new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
				else
					setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																										   CNRUserContext.getEsercizio(userContext),
																										   Elemento_voceHome.GESTIONE_ENTRATE,
																										   new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
			}
			return getDs_livello1();
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	/**
	 * Ritorna la descrizione del secondo livello della classificazione ufficiale
	 *
	 * @param userContext il context di riferimento 
	 * @return String la descrizione del livello richiesto 
	 */
	public String getDs_livello2(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (getDs_livello2()==null) {
				if (this instanceof ConsPDGAssestatoAggregatoSpeBP)
					setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																										   CNRUserContext.getEsercizio(userContext),
																										   Elemento_voceHome.GESTIONE_SPESE,
																										   new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
				else
					setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																										   CNRUserContext.getEsercizio(userContext),
																										   Elemento_voceHome.GESTIONE_ENTRATE,
																										   new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
			}
			return getDs_livello2();
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	/**
	 * Modifica la label presente nel BulkInfo di riferimento per il campo <field> indicato
	 *
	 * @param field il campo da aggiornare 
	 * @param label il nuovo valore da sostituire al vecchio
	 */
	public void setField(String field, String label) {
		FieldProperty fieldproperty = null;
		try {
			fieldproperty = (FieldProperty)getBulkInfo().getFieldProperty(field);
			fieldproperty.setLabel(label);
		} catch (NullPointerException e){}

		try {
			fieldproperty = (FieldProperty)getBulkInfo().getFormFieldProperty(field);
			fieldproperty.setLabel(label);
		} catch (NullPointerException e){}

		try {
			fieldproperty = getBulkInfo().getColumnFieldProperty(field);
			fieldproperty.setLabel(label);
		} catch (NullPointerException e){}

		try {
			fieldproperty = getBulkInfo().getFindFieldProperty(field);
			fieldproperty.setLabel(label);
		} catch (NullPointerException e){}
		
		for(Enumeration enumeration = getBulkInfo().getFreeSearchProperties(); enumeration.hasMoreElements();)
		{
			try {
				fieldproperty = (FieldProperty)enumeration.nextElement();
				if (fieldproperty.getName().equals(field)) {
					fieldproperty.setLabel(label);
				}
			} catch (NullPointerException e){}
		}
		for(Enumeration enumeration = getBulkInfo().getColumnFieldProperties(this.pathConsultazione); enumeration.hasMoreElements();)
		{
			try {
				fieldproperty = (FieldProperty)enumeration.nextElement();
				if (fieldproperty.getName().equals(field)) {
					fieldproperty.setLabel(label);
				}
			} catch (NullPointerException e){}
		}
	}
	/**
	 * Ritorna la CompoundFindClause ottenuta in base alla selezione effettuata
	 *
	 * @param field il campo da aggiornare 
	 * @param label il nuovo valore da sostituire al vecchio
	 */
	public CompoundFindClause getSelezione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{
			CompoundFindClause clauses = null;
			for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			{
				V_cons_pdg_assestato_aggregatoBulk wpb = (V_cons_pdg_assestato_aggregatoBulk)i.next();
				CompoundFindClause parzclause = new CompoundFindClause();
	
				if (isPresenteDIP()) 
					parzclause.addClause("AND","dip",SQLBuilder.EQUALS,wpb.getDip());
				if (isPresenteLIV1()) 
					parzclause.addClause("AND","cd_livello1",SQLBuilder.EQUALS,wpb.getCd_livello1());
				if (isPresenteLIV2()) 
					parzclause.addClause("AND","cd_livello2",SQLBuilder.EQUALS,wpb.getCd_livello2());
				if (isPresenteCDS()) 
					parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
				if (isPresenteUO()) 
					parzclause.addClause("AND","uo",SQLBuilder.EQUALS,wpb.getUo());
				if (isPresenteDET()) 
					parzclause.addClause("AND","cd_classificazione",SQLBuilder.EQUALS,wpb.getCd_classificazione());
	
				clauses = clauses.or(clauses, parzclause);
			}
			return clauses;
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}
