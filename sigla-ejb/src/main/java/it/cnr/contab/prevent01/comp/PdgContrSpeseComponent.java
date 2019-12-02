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
 * Created on Oct 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoHome;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.prevent01.bulk.Ass_dipartimento_areaBulk;
import it.cnr.contab.prevent01.bulk.Ass_dipartimento_areaHome;
import it.cnr.contab.prevent01.bulk.Contrattazione_speseVirtualBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateHome;
import it.cnr.contab.prevent01.bulk.Pdg_approvato_dip_areaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_approvato_dip_areaHome;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseHome;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioHome;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class PdgContrSpeseComponent extends CRUDComponent {

	public PdgContrSpeseComponent() {
		super();
	}
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		return inizializzaBulkPerModifica(usercontext,oggettobulk);		
	}
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		return inizializzaBulkPerModifica(usercontext,oggettobulk);		
	}
	public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		return inizializzaBulkPerModifica(usercontext,oggettobulk);		
	}
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		try {
			Contrattazione_speseVirtualBulk testata = (Contrattazione_speseVirtualBulk)oggettobulk;
			Pdg_approvato_dip_areaHome pdgDipAreaHome = (Pdg_approvato_dip_areaHome)getHome(usercontext, Pdg_approvato_dip_areaBulk.class);
			
			UtenteBulk utente = (UtenteBulk)getHome(usercontext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(usercontext)));
			if (utente != null && utente.getCd_dipartimento() != null)
				testata.setDettagliDipArea(new it.cnr.jada.bulk.BulkList(pdgDipAreaHome.findPdgDipAreaDettagli(usercontext, testata, utente.getCd_dipartimento())));
			else
				testata.setDettagliDipArea(new it.cnr.jada.bulk.BulkList(pdgDipAreaHome.findPdgDipAreaDettagli(usercontext, testata)));

			getHomeCache(usercontext).fetchAll(usercontext);

			for (java.util.Iterator i = testata.getDettagliDipArea().iterator(); i.hasNext();) 
		   	{
				Pdg_approvato_dip_areaBulk appDipArea = (Pdg_approvato_dip_areaBulk) i.next();
				appDipArea.setImporto_ripartito(pdgDipAreaHome.calcolaTotaleApprovatoSpeseInterne(usercontext, appDipArea));
				appDipArea.setUtenteDipartimento(utente != null && utente.getCd_dipartimento() != null);
		   	}
			
			return testata;
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}		
	}
	public OggettoBulk inizializzaDettagliBulkPerModifica(UserContext usercontext,Contrattazione_speseVirtualBulk contr_spese, Pdg_approvato_dip_areaBulk pdg_dip_area)throws ComponentException {
		try {
			Contrattazione_speseVirtualBulk testata = contr_spese;
			Pdg_contrattazione_speseHome pdgContrHome = (Pdg_contrattazione_speseHome)getHome(usercontext, Pdg_contrattazione_speseBulk.class);
			testata.setDettagliContrSpese(new it.cnr.jada.bulk.BulkList(pdgContrHome.findPdgContrSpeseDettagli(usercontext, pdg_dip_area)));
			getHomeCache(usercontext).fetchAll(usercontext);

			return testata;
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}		
	}
	public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
//		Contrattazione_speseVirtualBulk contr_spese = (Contrattazione_speseVirtualBulk)super.creaConBulk(userContext,bulk);
		validaCreaModificaTestata(userContext, (Contrattazione_speseVirtualBulk)bulk);
		validaCreaModificaDettagli(userContext, (Contrattazione_speseVirtualBulk)bulk);
		return super.creaConBulk(userContext,bulk);
	}
	public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		//Contrattazione_speseVirtualBulk contr_spese = (Contrattazione_speseVirtualBulk)super.modificaConBulk(userContext,bulk);
		validaCreaModificaTestata(userContext, (Contrattazione_speseVirtualBulk)bulk);
		validaCreaModificaDettagli(userContext, (Contrattazione_speseVirtualBulk)bulk);
		return super.modificaConBulk(userContext,bulk);
	}
	private void validaCreaModificaTestata(UserContext userContext, Contrattazione_speseVirtualBulk contr_spese) throws ComponentException {
		try{
			for(Iterator dettagli = contr_spese.getDettagliDipArea().iterator(); dettagli.hasNext();){
				Pdg_approvato_dip_areaBulk pdg_dip_area = (Pdg_approvato_dip_areaBulk)dettagli.next();
				// controlliamo se presente in ASS_DIPARTIMENTO_AREA
				if ((pdg_dip_area.isToBeCreated()||pdg_dip_area.isToBeUpdated())) {
					if (pdg_dip_area.getCd_dipartimento()!=null && pdg_dip_area.getCd_cds_area()!=null) {
						Ass_dipartimento_areaBulk ass_dip_area = new Ass_dipartimento_areaBulk(pdg_dip_area.getEsercizio(),
								pdg_dip_area.getCd_dipartimento(),
								pdg_dip_area.getCd_cds_area());
						Ass_dipartimento_areaHome home = (Ass_dipartimento_areaHome)getHome(userContext, ass_dip_area);
						
						ass_dip_area = (Ass_dipartimento_areaBulk)home.findByPrimaryKey(ass_dip_area);
						if (ass_dip_area==null)
							throw new ApplicationException("Attenzione: non esiste l'associazione Dipartimento/Area "+
									pdg_dip_area.getCd_dipartimento()+"/"+pdg_dip_area.getCd_cds_area()+
									". Non è possibile inserire la riga.");
					}
				}
				// controlliamo se la "chiave logica" è già presente sul db
				if ((pdg_dip_area.isToBeCreated())) {
					if (pdg_dip_area.getCd_dipartimento()!=null) {

						BulkHome home = getHome(userContext,Pdg_approvato_dip_areaBulk.class);
						SQLBuilder sql = home.createSQLBuilder();
						sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,pdg_dip_area.getEsercizio());
						sql.addSQLClause("AND","CD_DIPARTIMENTO",sql.EQUALS,pdg_dip_area.getCd_dipartimento());
						if (pdg_dip_area.getCd_cds_area()!=null)
							sql.addSQLClause("AND","CD_CDS_AREA",sql.EQUALS,pdg_dip_area.getCd_cds_area());
						else
							sql.addSQLClause("AND","CD_CDS_AREA",sql.ISNULL,null);
						java.util.List lista = getHome(userContext,Pdg_approvato_dip_areaBulk.class).fetchAll(sql);

						if (!lista.isEmpty()) {
							if (pdg_dip_area.getCd_cds_area()!=null)
								throw new ApplicationException("Attenzione: è già presente negli archivi una riga con Dipartimento/Area "+
										pdg_dip_area.getCd_dipartimento()+"/"+pdg_dip_area.getCd_cds_area()+". Cancellare il dato duplicato prima di effettuare il salvataggio.");
							else
								throw new ApplicationException("Attenzione: è già presente negli archivi una riga con Dipartimento "+
										pdg_dip_area.getCd_dipartimento()+". Cancellare il dato duplicato prima di effettuare il salvataggio.");
						}
					}
				}
			}

		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	
	private void validaCreaModificaDettagli(UserContext userContext, Contrattazione_speseVirtualBulk contr_spese) throws ComponentException {
		Integer livelloContr = livelloContrattazioneSpese(userContext);
		for(Iterator dettagli = contr_spese.getDettagliContrSpese().iterator(); dettagli.hasNext();){
			Pdg_contrattazione_speseBulk pdg_contr_spese = (Pdg_contrattazione_speseBulk)dettagli.next();
			if (pdg_contr_spese.getArea()==null || pdg_contr_spese.getArea().getCd_unita_organizzativa()==null) {
				if (pdg_contr_spese.getPdg_dip_area()!=null && pdg_contr_spese.getPdg_dip_area().getArea()!=null &&
					pdg_contr_spese.getPdg_dip_area().getArea().getCd_unita_organizzativa()!=null)
					pdg_contr_spese.setArea(pdg_contr_spese.getPdg_dip_area().getArea());
				else
					pdg_contr_spese.setArea(new CdsBulk(pdg_contr_spese.getProgetto().getUnita_organizzativa().getCd_cds()));
			}
					
			if ((pdg_contr_spese.isToBeCreated()||pdg_contr_spese.isToBeUpdated())) {
		        if (livelloContr.intValue()>0 && pdg_contr_spese.getId_classificazione() == null)
		           throw new it.cnr.jada.comp.ApplicationException("Attenzione: Il livello contrattazione è obbligatorio!");
			}
		}
	}
	public boolean isApprovatoDefinitivo(UserContext userContext) throws ComponentException {
		try{
			Parametri_cnrHome home = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk par = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
			par = (Parametri_cnrBulk)home.findByPrimaryKey(par);
			return par.getFl_approvato_definitivo();
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public SQLBuilder selectAreaByClause (UserContext userContext,Pdg_approvato_dip_areaBulk dip_area, CdsBulk area, CompoundFindClause clause) throws ComponentException, PersistencyException
	{
			SQLBuilder sql = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
			
			if (clause != null) 
			  sql.addClause(clause);
			sql.addClause("AND", "cd_tipo_unita", SQLBuilder.EQUALS, it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA);
			if (dip_area.getDipartimento()!=null && dip_area.getDipartimento().getCd_dipartimento()!=null) {
				sql.addTableToHeader("ASS_DIPARTIMENTO_AREA");
				sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "ASS_DIPARTIMENTO_AREA.CD_CDS_AREA");
				sql.addSQLClause("AND", "ASS_DIPARTIMENTO_AREA.CD_DIPARTIMENTO", SQLBuilder.EQUALS, dip_area.getDipartimento().getCd_dipartimento());
			}
			sql.addOrderBy("unita_organizzativa.cd_unita_organizzativa");
			return sql;
	}

	public SQLBuilder selectDipartimentoByClause (UserContext userContext,Pdg_approvato_dip_areaBulk dip_area, DipartimentoBulk area, CompoundFindClause clause) throws ComponentException, PersistencyException
	{
			SQLBuilder sql = ((DipartimentoHome)getHome(userContext, DipartimentoBulk.class)).createSQLBuilder();
			
			if (clause != null) 
			  sql.addClause(clause);			
			 
			java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext));
		 	sql.addClause("AND", "dt_istituzione", sql.LESS, lastDayOfYear);
			sql.openParenthesis("AND");
			sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS,  lastDayOfYear);
			sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
			sql.closeParenthesis();
			UtenteBulk utente = (UtenteBulk)getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext)));
			sql.addClause("AND", "cd_dipartimento", SQLBuilder.EQUALS, utente.getCd_dipartimento());
			sql.addOrderBy("cd_dipartimento");
			return sql;
	}

	public SQLBuilder selectClassificazioneByClause (UserContext userContext,Pdg_contrattazione_speseBulk pdg_contr_spese,V_classificazione_vociBulk classificazione,CompoundFindClause clause) throws ComponentException, PersistencyException
	{
			SQLBuilder sql = ((V_classificazione_vociHome)getHome(userContext, V_classificazione_vociBulk.class)).createSQLBuilder();
			sql.addTableToHeader("PARAMETRI_CNR");
			//sql.addTableToHeader("CDR");
			//sql.addTableToHeader("UNITA_ORGANIZZATIVA");
			sql.addSQLClause("AND", "PARAMETRI_CNR.ESERCIZIO", sql.EQUALS, pdg_contr_spese.getEsercizio());
			sql.addSQLJoin("PARAMETRI_CNR.LIVELLO_CONTRATT_PDG_SPE","V_CLASSIFICAZIONE_VOCI.NR_LIVELLO");
			//sql.addSQLClause("AND", "CDR.CD_CENTRO_RESPONSABILITA", sql.EQUALS, pdg_contr_spese.getCd_centro_responsabilita());
			//sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
			/*
		    sql.openParenthesis("AND");
		      sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_ACCENTRATO", sql.EQUALS, "Y");
		      sql.addSQLClause("OR", "V_CLASSIFICAZIONE_VOCI.FL_DECENTRATO", sql.EQUALS, "Y");
		    sql.closeParenthesis();
		    */
		    /*
			sql.openParenthesis("AND");
				sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
				//sql.addClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, Boolean.TRUE);
				sql.openParenthesis("OR");		        
				  sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
				  sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, "N");
				sql.closeParenthesis();  		      
			sql.closeParenthesis();
			*/
		    sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.ESERCIZIO", sql.EQUALS, pdg_contr_spese.getEsercizio());
		    sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		    sql.addOrderBy("V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE");
			if (clause != null) 
			  sql.addClause(clause);
			return sql;
	}
	public SQLBuilder selectProgettoByClause (UserContext userContext,
			  Pdg_contrattazione_speseBulk pdg_contr_spese,
			  Progetto_sipBulk progetto,
			  CompoundFindClause clause) throws ComponentException, PersistencyException
	{
		try {
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));

			Progetto_sipHome progettohome = (Progetto_sipHome)getHome(userContext, Progetto_sipBulk.class);
	        SQLBuilder sql = progettohome.createSQLBuilder();

	        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, pdg_contr_spese.getEsercizio());
			sql.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_PREVISIONE);
		    sql.addClause("AND", "livello", SQLBuilder.EQUALS, parCnr.getLivelloProgetto());
			sql.addClause("AND", "fl_utilizzabile", SQLBuilder.EQUALS, Boolean.TRUE);
	
		    if (!parCnr.getFl_nuovo_pdg()) {
				//Aggiungo in Join le commesse
				sql.addTableToHeader("PROGETTO A");
				sql.addSQLJoin("PROGETTO_SIP.ESERCIZIO_PROGETTO_PADRE","A.ESERCIZIO");
				sql.addSQLJoin("PROGETTO_SIP.PG_PROGETTO_PADRE","A.PG_PROGETTO");
			    sql.addSQLJoin("PROGETTO_SIP.TIPO_FASE_PROGETTO_PADRE","A.TIPO_FASE");
		
			    //Aggiungo in Join i progetti
				sql.addTableToHeader("PROGETTO B");
			    sql.addSQLJoin("A.ESERCIZIO_PROGETTO_PADRE","B.ESERCIZIO");
			    sql.addSQLJoin("A.PG_PROGETTO_PADRE","B.PG_PROGETTO");
			    sql.addSQLJoin("A.TIPO_FASE_PROGETTO_PADRE","B.TIPO_FASE");
				sql.addSQLClause("AND", "B.cd_dipartimento", SQLBuilder.EQUALS, pdg_contr_spese.getPdg_dip_area().getCd_dipartimento());
			} else {
			    //Aggiungo in Join le aree progettuali
				sql.addTableToHeader("PROGETTO A");
				sql.addSQLJoin("PROGETTO_SIP.ESERCIZIO_PROGETTO_PADRE","A.ESERCIZIO");
				sql.addSQLJoin("PROGETTO_SIP.PG_PROGETTO_PADRE","A.PG_PROGETTO");
			    sql.addSQLJoin("PROGETTO_SIP.TIPO_FASE_PROGETTO_PADRE","A.TIPO_FASE");
				sql.addSQLClause("AND", "A.cd_dipartimento", SQLBuilder.EQUALS, pdg_contr_spese.getPdg_dip_area().getCd_dipartimento());
			}
			// Se uo 999.000 in scrivania: visualizza tutti i progetti
	        Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	        if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			    if (!parCnr.getFl_nuovo_pdg())
			    	sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));
			    else
			    	sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
			}
	        if (clause != null) 
	          sql.addClause(clause);
	        return sql;
		}catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
	}
	public SQLBuilder selectCdrByClause (UserContext userContext,
			  Pdg_contrattazione_speseBulk pdg_contr_spese,
			  CdrBulk cdr,
			  CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		CdrHome cdrhome = (CdrHome)getHome(userContext, CdrBulk.class);
		SQLBuilder sql = cdrhome.createSQLBuilder();
		sql.addClause( clause );
		sql.addClause("AND", "livello", sql.EQUALS, CdrHome.CDR_PRIMO_LIVELLO);
		if (pdg_contr_spese.getPg_progetto()!=null) {
			CdrBulk cdrPrimoLivello = caricaCdrAfferenzaDaUo(userContext,pdg_contr_spese.getProgetto().getUnita_organizzativa());
			if (cdrPrimoLivello==null)
				throw new ApplicationException("Errore nella ricerca del CdR di primo livello per la UO "+pdg_contr_spese.getProgetto().getUnita_organizzativa());
			sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, cdrPrimoLivello);
		}
		
		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
	public CdrBulk caricaCdrAfferenzaDaUo(UserContext userContext,
			  Unita_organizzativaBulk uo)
	throws ComponentException, PersistencyException
	{
		String keyAff;
		CdrHome cdrhome = (CdrHome)getHome(userContext, CdrBulk.class);
		if ( uo.getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ))
			keyAff = cdrhome.findCdCdrAfferenzaForSAC( uo.getCd_unita_organizzativa());							
		else
			keyAff = cdrhome.findCdCdrAfferenzaForMacro( uo.getCd_cds());
		
		if (keyAff==null)
			throw new ApplicationException("Errore nella ricerca del CdR di primo livello per la UO " + uo.getCd_unita_organizzativa());
		
		CdrBulk ncdr = new CdrBulk(keyAff);
		ncdr =  (CdrBulk) cdrhome.findByPrimaryKey(ncdr);
		return ncdr;
	}
	public Integer livelloContrattazioneSpese(UserContext userContext)
	throws ComponentException
	{
		Parametri_cnrHome parhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
		Parametri_cnrBulk par = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
		try {
			par = (Parametri_cnrBulk) parhome.findByPrimaryKey(par);
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
		return par.getLivello_contratt_pdg_spe();
	}
	private Pdg_esercizioBulk caricaPdgEsercizio(UserContext userContext, Integer esercizio, String cd_cdr)
	throws ComponentException, PersistencyException
	{
		Pdg_esercizioHome parhome = (Pdg_esercizioHome)getHome(userContext, Pdg_esercizioBulk.class);
		Pdg_esercizioBulk pdg = new Pdg_esercizioBulk(esercizio,cd_cdr);
		pdg = (Pdg_esercizioBulk) parhome.findByPrimaryKey(pdg);
		return pdg;
	}
	public void approvaDefinitivamente(UserContext userContext)	throws ComponentException {
		try {
			List listaModuli = findPdgEsercizio(userContext, Pdg_esercizioBulk.STATO_IN_ESAME_CDR );
			if (!listaModuli.isEmpty())
				throw new ApplicationException( "Lo stato non può essere aggiornato poichè non tutti i CDS hanno il PDGP in stato " + Pdg_esercizioBulk.STATO_IN_ESAME_CDR);

			controllaImportiFontiInterneApprovate(userContext);

			Parametri_cnrHome parhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk par = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
			par = (Parametri_cnrBulk) parhome.findByPrimaryKey(par);
			par.setFl_approvato_definitivo(Boolean.TRUE);
			par.setToBeUpdated();
			updateBulk(userContext, par);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	public void undoApprovazioneDefinitiva(UserContext userContext)	throws ComponentException {
		try {
			Parametri_cnrHome parhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk par = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
			par = (Parametri_cnrBulk) parhome.findByPrimaryKey(par);
			par.setFl_approvato_definitivo(Boolean.FALSE);
			par.setToBeUpdated();
			updateBulk(userContext, par);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	private void controllaImportiFontiInterneApprovate(UserContext userContext) throws it.cnr.jada.comp.ComponentException {

		BigDecimal impTotale = Utility.ZERO;

		try {
			Pdg_approvato_dip_areaHome appDipAreaHome = (Pdg_approvato_dip_areaHome)getHome(userContext,Pdg_approvato_dip_areaBulk.class);
			Pdg_contrattazione_speseHome contrSpeseHome = (Pdg_contrattazione_speseHome)getHome(userContext,Pdg_contrattazione_speseBulk.class);

			SQLBuilder appDipAreaSql = appDipAreaHome.createSQLBuilder();
			appDipAreaSql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));

			SQLBroker appDipAreaBroker = appDipAreaHome.createBroker(appDipAreaSql);
			
			while(appDipAreaBroker.next()) {
				impTotale = Utility.ZERO;
				Pdg_approvato_dip_areaBulk appDipArea = (Pdg_approvato_dip_areaBulk)appDipAreaBroker.fetch(Pdg_approvato_dip_areaBulk.class);
				getHomeCache(userContext).fetchAll(userContext);

				impTotale = appDipAreaHome.calcolaTotaleApprovatoSpeseInterne(userContext,appDipArea);
				
				if (Utility.nvl(appDipArea.getImporto_approvato()).compareTo(impTotale)!=0){
					if ( appDipArea.getArea()!=null && appDipArea.getArea().getCd_unita_organizzativa()!=null)
						throw new ApplicationException("Non esiste quadratura tra l'importo totale approvato ed i dettagli ripartiti per il Dipartimento " + appDipArea.getCd_dipartimento() + " - " + appDipArea.getDipartimento().getDs_dipartimento() + " e l'area " + appDipArea.getCd_cds_area() + " - " + appDipArea.getArea().getCd_ds_cds() + ". Impossibile procedere.");
					else
						throw new ApplicationException("Non esiste quadratura tra l'importo totale approvato ed i dettagli ripartiti per il Dipartimento " + appDipArea.getCd_dipartimento() + " - " + appDipArea.getDipartimento().getDs_dipartimento() + ". Impossibile procedere.");
				}
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IntrospectionException e) {
			throw handleException(e);
		}
	}
	private java.util.List findPdgEsercizio(UserContext userContext, String notStato ) throws ComponentException, PersistencyException, BusyResourceException, OutdatedResourceException {
		BulkHome home = getHome(userContext,Pdg_esercizioBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND","stato",sql.NOT_EQUALS,notStato);
		return home.fetchAll(sql);
	}

}
