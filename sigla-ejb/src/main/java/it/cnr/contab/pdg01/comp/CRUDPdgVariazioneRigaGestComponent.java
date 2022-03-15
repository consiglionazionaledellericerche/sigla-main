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

package it.cnr.contab.pdg01.comp;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.bulk.storage.PdgVariazioneDocument;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.pdg01.ejb.CRUDPdgVariazioneGestionaleComponentSession;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;

public class CRUDPdgVariazioneRigaGestComponent extends it.cnr.jada.comp.CRUDComponent {
	/**
	  * CRUDPdgVariazioneRigaGestComponent constructor comment.
	  */
	public CRUDPdgVariazioneRigaGestComponent() {
		super();
	}

	/**
	 * Carica un modulo del Pdg con tutti i dettagli gestionali correlati.
	 *
	 * Nome: Inizializzazione;
	 * Pre:  Preparare l'oggetto alle modifiche;
	 * Post: Si procede, oltre che alla normale procedura di inizializzazione di un oggetto bulk,
	 *       anche al caricamento dei dettagli gestionali e al calcolo delle somme già ripartite.
	 *
	 * @param bulk dovrà essere sempre <code>Pdg_variazioneBulk</code>.
	 *
	 * @return un <code>OggettoBulk</code> che sarà sempre un <code>Pdg_variazioneBulk</code>.
	 */
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Ass_pdg_variazione_cdrBulk testata = (Ass_pdg_variazione_cdrBulk)super.inizializzaBulkPerModifica(userContext,bulk);
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome)getHome(userContext, Ass_pdg_variazione_cdrBulk.class);
			testata.setRigheVariazioneEtrGest(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliEntrataVariazioneGestionale(testata)));
			testata.setRigheVariazioneSpeGest(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliSpesaVariazioneGestionale(testata)));
			inizializzaVistosuDettagli(userContext,testata);
			testata.setTotale_quota_spesa(Utility.ZERO);
			PersistentHome laHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
			for (Iterator righeVar=testata.getRigheVariazioneSpeGest().iterator();righeVar.hasNext();){
				Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk)righeVar.next();
				testata.setTotale_quota_spesa(Utility.nvl(testata.getTotale_quota_spesa()).add(Utility.nvl(varRiga.getIm_variazione())));

				SQLBuilder sql = laHome.createSQLBuilder();
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,varRiga.getCd_cdr_assegnatario());
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,varRiga.getCd_linea_attivita());
				List list = laHome.fetchAll(sql);
				if (list.size()==1)
					varRiga.setProgetto(((WorkpackageBulk)list.get(0)).getProgetto());
			}					
			for (Iterator righeVar=testata.getRigheVariazioneEtrGest().iterator();righeVar.hasNext();){
				Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk)righeVar.next();

				SQLBuilder sql = laHome.createSQLBuilder();
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,varRiga.getCd_cdr_assegnatario());
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,varRiga.getCd_linea_attivita());
				List list = laHome.fetchAll(sql);
				if (list.size()==1)
					varRiga.setProgetto(((WorkpackageBulk)list.get(0)).getProgetto());
			}					
			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}	 

	/**
	 * Esegue una operazione di modifica di un Pdg_variazioneBulk. 
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Modifica di una variazione
	 * Pre:  La richiesta di modifica di una variazione è stata generata
	 * Post: Viene loccato il record PDG_VARIAZIONE con l'istruzione findAndLock per evitare che salvataggi 
	 *       simultanei da parte di più utenti possano alterare i controlli.
	 * 	 Vengono verificati i dettagli della variazione gestionale associati al modulo per controllare che 
	 *       l'assestato di bilancio per la voce modificata non sia negativa.
	 * 	 In caso affermativo viene generata una ApplicationException per segnalare all'utente 
	 *       l'impossibilità di effettuare la variazione di Bilancio.
	 *
	 * @param	userContext	lo UserContext che ha generato la richiesta
	 * @param	oggettobulk il Pdg_variazioneBulk che deve essere modificato
	 * @return	il Pdg_variazioneBulk risultante dopo l'operazione di modifica.
	 */	
	public OggettoBulk modificaConBulk(UserContext userContext,	OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk.setCrudStatus(OggettoBulk.NORMAL);
			Pdg_variazioneHome pdgHome = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
			Pdg_variazioneBulk pdgClone;
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);
			Ass_pdg_variazione_cdrBulk testataClone;
			try{
				pdgClone = (Pdg_variazioneBulk)pdgHome.findAndLock(((Ass_pdg_variazione_cdrBulk)oggettobulk).getPdg_variazione());
			} catch (BusyResourceException e) {
				throw new ApplicationException("Operazione effettuata al momento da un'altro utente sulla stessa variazione. Riprovare a salvare successivamente.");
			} catch (ObjectNotFoundException e) {
				throw new ApplicationException("Operazione non effettuata. La variazione risulta essere stata eliminata da un altro utente.");
			} 

			try{
				testataClone = (Ass_pdg_variazione_cdrBulk)testataHome.findAndLock(oggettobulk);
			} catch (BusyResourceException e) {
				throw new ApplicationException("Operazione effettuata al momento da un'altro utente sullo stesso CDR della variazione. Riprovare a salvare successivamente.");
			} catch (ObjectNotFoundException e) {
				throw new ApplicationException("Operazione non effettuata. L'associazione del CDR alla variazione risulta essere stata eliminata da un altro utente.");
			} 

			CdsHome cdsHome = (CdsHome)getHome(userContext,CdsBulk.class);

			boolean rigaInsModEtr = false;
			boolean rigaInsModSpe = false;
			BigDecimal totaleRigheEtr = Utility.ZERO;  
			BigDecimal totaleRigheSpe = Utility.ZERO;  
			
			for (java.util.Iterator i=((Ass_pdg_variazione_cdrBulk)oggettobulk).getRigheVariazioneEtrGest().iterator();i.hasNext();){			
				Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk)i.next();
				totaleRigheEtr = totaleRigheEtr.add(Utility.nvl(rigaVar.getIm_variazione()));
				if (rigaVar.getCd_cds_area() == null )
					rigaVar.setArea((CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(rigaVar.getCdr_assegnatario().getCd_cds())));
				else if (!rigaVar.getCd_cds_area().equals(rigaVar.getCdr_assegnatario().getCd_cds())) {
					/*
					 * Se è stata valorizzata l'area occorre controllare:
					 * 1) Non è possibile, tramite storni di entrata, aumentare o ridurre gli importi dell'Area
					 * 
					 * n.b. si suppone che per l'area non si entri mai in questo ramo in quanto il campo Area 
					 *      è impostato automaticamente uguale al CDS del Cdr_accentratore   
					 */
					if (pdgClone.isStorno())
						throw new ApplicationException("Non è possibile effettuare storni di entrata su Area da parte di Istituti.");
				}
					
				if (rigaVar.getCrudStatus()==OggettoBulk.TO_BE_CREATED || rigaVar.getCrudStatus()==OggettoBulk.TO_BE_UPDATED)
					rigaInsModEtr = true;				
			}
			for (java.util.Iterator i=((Ass_pdg_variazione_cdrBulk)oggettobulk).getRigheVariazioneSpeGest().iterator();i.hasNext();){			
				Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk)i.next();
				totaleRigheSpe = totaleRigheSpe.add(Utility.nvl(rigaVar.getIm_variazione()));
				if (rigaVar.getCd_cds_area() == null )
					rigaVar.setArea((CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(rigaVar.getCdr_assegnatario().getCd_cds())));
				else if (!rigaVar.getCd_cds_area().equals(rigaVar.getCdr_assegnatario().getCd_cds())) {
					/*
					 * Se è stata valorizzata l'area occorre controllare:
					 * 1) Non è possibile, tramite lo storno di spesa, sottrarre importi all'Area
					 * 
					 * n.b. si suppone che per l'area non si entri mai in questo ramo in quanto il campo Area 
					 *      è impostato automaticamente uguale al CDS del Cdr_accentratore   
					 */
					if (pdgClone.isStorno() && rigaVar.getIm_variazione().compareTo(Utility.ZERO)<0)
						throw new ApplicationException("Non è possibile effettuare storni negativi di spesa su Area da parte di Istituti.");
				}
				if (rigaVar.getCrudStatus()==OggettoBulk.TO_BE_CREATED || rigaVar.getCrudStatus()==OggettoBulk.TO_BE_UPDATED)
					rigaInsModSpe = true;				
			}
			
			if ((rigaInsModEtr && testataClone.getIm_entrata().compareTo(totaleRigheEtr) == 0) ||
			    (rigaInsModSpe && testataClone.getIm_spesa().compareTo(totaleRigheSpe) == 0)){
				UtenteHome utenteHome = (UtenteHome)getHome(userContext,UtenteBulk.class);
				MessaggioBulk messaggio = null;
				for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(pdgClone.getCd_centro_responsabilita()).iterator();i.hasNext();){
					UtenteBulk utente = (UtenteBulk)i.next();
					if (rigaInsModEtr)
						messaggio = generaMessaggioCopertura(userContext,utente,testataClone,Elemento_voceHome.GESTIONE_ENTRATE);
					else
						messaggio = generaMessaggioCopertura(userContext,utente,testataClone,Elemento_voceHome.GESTIONE_SPESE);
					
					super.creaConBulk(userContext, messaggio);
				}											
			}

			BigDecimal totaleImportoRiga = BigDecimal.ZERO;
			Ass_pdg_variazione_cdrBulk ass = (Ass_pdg_variazione_cdrBulk)oggettobulk;
			if (ass.getPdg_variazione().isApprovata()){
				for (java.util.Iterator i =  ass.getRigheVariazioneSpeGest().iterator();i.hasNext();) {
					Pdg_variazione_riga_gestBulk riga = (Pdg_variazione_riga_gestBulk)i.next();
					PdGVariazioniComponentSession comp = Utility.createPdGVariazioniComponentSession();
					try {
						if (comp.isRigaLiquidazioneIva(userContext, riga)){
							throw new ApplicationException ("Attenzione: Non è possibile salvare la variazione contenente la GAE di default della liquidazione IVA!");
						} else {
							totaleImportoRiga = totaleImportoRiga.add(Utility.nvl(riga.getIm_variazione()));
						}
					} catch (ComponentException e) {
						throw new ApplicationException (e.getMessage());
					}
				}
				if (ass.getPdg_variazione().isApprovata() && Utility.nvl(ass.getIm_spesa()).compareTo(totaleImportoRiga) != 0){
					throw new ApplicationException ("Attenzione: la somma degli importi "+totaleImportoRiga+" non corrisponde al totale indicato "+Utility.nvl(ass.getIm_spesa())+" sul centro di responsabilità!");
				}
				CRUDPdgVariazioneGestionaleComponentSession varSession = (CRUDPdgVariazioneGestionaleComponentSession)createComponentSessionVariazioneGestionale();
				try {
					varSession.allineaSaldiVariazioneApprovata(userContext, ass);
				} catch (ComponentException e) {
					throw handleException(e);
				} catch (RemoteException e) {
					throw handleException(e);
				}
			}

			OggettoBulk assPdg = super.modificaConBulk(userContext, oggettobulk);	
			if (ass.getPdg_variazione().isApprovata()){
				createDocumentForVariazioneLiquidazioneIVA(userContext, ass.getPdg_variazione());
			}
			return assPdg;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}

	private void createDocumentForVariazioneLiquidazioneIVA(UserContext userContext, Pdg_variazioneBulk variazione)
			throws ComponentException {
		try {
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = new ArchiviaStampaPdgVariazioneBulk();
			archiviaStampaPdgVariazioneBulk.setPdg_variazioneForPrint(variazione);
			archiviaStampaPdgVariazioneBulk.setCentro_responsabilita(variazione.getCentro_responsabilita());
			archiviaStampaPdgVariazioneBulk.setDs_variazione(variazione.getDs_variazione());
			archiviaStampaPdgVariazioneBulk.setPg_variazione_pdg(variazione.getPg_variazione_pdg());
			
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);
			print.setReport("/cnrpreventivo/pdg/stampa_variazioni_pdg.jasper");
			print.setNomeFile("Variazione al PdG n. "
					+ archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
					+ " CdR proponente "
					+ archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf");
			print.setUtcr(userContext.getUser());
			print.addParam("Esercizio", archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint().getEsercizio(), Integer.class);
			print.addParam("Variazione", archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg().intValue(), Integer.class);
			Report report = SpringUtil.getBean("printService",
					PrintService.class).executeReport(userContext,
					print);
			String cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
			StorageObject node = SpringUtil.getBean("storeService", StoreService.class).storeSimpleDocument(
			        archiviaStampaPdgVariazioneBulk,
                    report.getInputStream(),
                    report.getContentType(),
                    report.getName(),
                    cmisPath);
			archiviaStampaPdgVariazioneBulk.setPdgVariazioneDocument(PdgVariazioneDocument.construct(node));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
		
	}

	private String getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) throws ApplicationException{
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(),
                Optional.ofNullable(archiviaStampaPdgVariazioneBulk.getEsercizio())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
				SpringUtil.getBean("storeService", StoreService.class).
						sanitizeFolderName(archiviaStampaPdgVariazioneBulk.getCd_cds()),
                "CdR "+archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita()+
                        " Variazione "+ Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(),5,'0')
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
	}
	
	public CRUDComponentSession createComponentSessionVariazioneGestionale() throws javax.ejb.EJBException {
		return (CRUDPdgVariazioneGestionaleComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG01_EJB_CRUDPdgVariazioneGestionaleComponentSession",CRUDPdgVariazioneGestionaleComponentSession.class);
}

	/**
	 * Aggiunge delle clausole a tutte le operazioni di ricerca eseguite su WorkpackageBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di una Linea di Attività
	 * Post: Vengono restituite tutte le Linee di Attività che:
	 * 		 - sono associate al CDR Assegnatario del dettaglio gestionale Pdg_modulo_spese_gestBulk;
	 * 		 - sono associate al Modulo di Attività del dettaglio gestionale Pdg_modulo_speseBulk;
	 * 		 - siano Linee utilizzabile nella Gestione spese (TI_GESTIONE='S')
	 * 		 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clause clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 * @throws RemoteException 
	 */
	public SQLBuilder selectLinea_attivitaByClause (UserContext userContext, 
													Pdg_variazione_riga_spesa_gestBulk dett,
													WorkpackageBulk latt, 
													CompoundFindClause clause) throws ComponentException, PersistencyException, RemoteException {	
		SQLBuilder sql = getHome(userContext, latt, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();

		sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,dett.getCd_cdr_assegnatario());
		
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_SPESE);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	    sql.closeParenthesis();

	    if (dett.getProgetto()!=null && dett.getProgetto().getPg_progetto()!=null)
	    	sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,dett.getProgetto().getPg_progetto());
	    
	 // Obbligatorio cofog sulle GAE
	 	if(((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).isCofogObbligatorio(userContext))
	 		sql.addSQLClause(FindClause.AND,"CD_COFOG",SQLBuilder.ISNOTNULL,null);
		sql.addTableToHeader("FUNZIONE");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE","FUNZIONE.CD_FUNZIONE");
		sql.addSQLClause(FindClause.AND, "FUNZIONE.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
		sql.addSQLClause(FindClause.AND, "NATURA.FL_SPESA",SQLBuilder.EQUALS,"Y");

		sql.addTableToHeader("PROGETTO_GEST");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","PROGETTO_GEST.ESERCIZIO");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO","PROGETTO_GEST.PG_PROGETTO");
		sql.addSQLClause(FindClause.AND,"PROGETTO_GEST.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

		if (dett.getPdg_variazione().getTipologia_fin() != null) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"NATURA.TIPO",SQLBuilder.EQUALS,dett.getPdg_variazione().getTipologia_fin());

			Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext))).ifPresent(cdrPersonale->{
				sql.addSQLClause( FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrPersonale);
			});

			sql.closeParenthesis();
		}

		/**
		 * Escludo la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		if (config != null){
			sql.addSQLClause( FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  SQLBuilder.NOT_EQUALS, config.getVal01());
		}

		/*
		 * RP 11/01/2007
		 * Inizialmente inserita per garantire che le aree utilizzassero solo GAE movimentate dagli Istituti.
		 * Eliminata successivamente per non limitare le aree nell'utilizzo delle GAE
		 */
		/*
		if (dett.getPdg_variazione().getCentro_responsabilita().getUnita_padre().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0) {
			SQLBuilder sqlAssestato = getHome(userContext, V_assestatoBulk.class).createSQLBuilder();
			sqlAssestato.addSQLJoin("V_ASSESTATO.ESERCIZIO", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
			sqlAssestato.addSQLJoin("V_ASSESTATO.ESERCIZIO_RES", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
			sqlAssestato.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
			sqlAssestato.addSQLJoin("V_ASSESTATO.CD_LINEA_ATTIVITA", "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA");
			sqlAssestato.addClause("AND", "importo_disponibile", sqlAssestato.GREATER, Utility.ZERO);
			sql.addSQLExistsClause("AND", sqlAssestato);
		}
		*/
		if (clause != null) sql.addClause(clause);
		
		return sql; 
	}	

	public SQLBuilder selectLinea_attivitaByClause (UserContext userContext, 
													Pdg_variazione_riga_entrata_gestBulk dett,
													WorkpackageBulk latt, 
													CompoundFindClause clause) throws ComponentException, PersistencyException {	
		SQLBuilder sql = getHome(userContext, latt, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();

		sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,dett.getCd_cdr_assegnatario());

		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRATE);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	    sql.closeParenthesis();

	    if (dett.getProgetto()!=null && dett.getProgetto().getPg_progetto()!=null)
	    	sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,dett.getProgetto().getPg_progetto());

		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
		sql.addSQLClause(FindClause.AND, "NATURA.FL_ENTRATA",SQLBuilder.EQUALS,"Y");

		sql.addTableToHeader("PROGETTO_GEST");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","PROGETTO_GEST.ESERCIZIO");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO","PROGETTO_GEST.PG_PROGETTO");
		sql.addSQLClause(FindClause.AND,"PROGETTO_GEST.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");
		/*
		 * L'origine delle fonti pilota l'utilizzo delle GAE sui dettagli della variazione.
		 * In particolare:
 		 *    - Variazione con Fonti Interne: solo GAE di tipo FIN;
		 *    - Variazione con Fonti Esterne: solo GAE di tipo FES.
		 * L'origine delle fonti filtra le GAE utilizzabili sulle righe di variazione ad eccezione
		 * delle GAE del CdR speciale del Personale in CONFIGURAZIONE_CNR, che risultano utilizzabili 
		 * sia su variazioni di Tipo Interno che Esterno.
		 */
		if (dett.getPdg_variazione().getTipologia_fin() != null) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"NATURA.TIPO",SQLBuilder.EQUALS,dett.getPdg_variazione().getTipologia_fin());

			Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext))).ifPresent(cdrPersonale->{
				sql.addSQLClause( FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrPersonale);
			});

			sql.closeParenthesis();
		}

		/*
		 * RP 11/01/2007
		 * Inizialmente inserita per garantire che le aree utilizzassero solo GAE movimentate dagli Istituti.
		 * Eliminata successivamente per non limitare le aree nell'utilizzo delle GAE
		 */
		/*
		if (dett.getPdg_variazione().getCentro_responsabilita().getUnita_padre().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0) {
			SQLBuilder sqlAssestato = getHome(userContext, V_assestatoBulk.class).createSQLBuilder();
			sqlAssestato.addSQLJoin("V_ASSESTATO.ESERCIZIO", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
			sqlAssestato.addSQLJoin("V_ASSESTATO.ESERCIZIO_RES", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
			sqlAssestato.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
			sqlAssestato.addSQLJoin("V_ASSESTATO.CD_LINEA_ATTIVITA", "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA");
			sqlAssestato.addClause("AND", "importo_disponibile", sqlAssestato.GREATER, Utility.ZERO);
			sql.addSQLExistsClause("AND", sqlAssestato);
		}
		*/

		sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			
		if (clause != null) sql.addClause(clause);
		
		return sql; 
	}	

	public SQLBuilder selectLinea_attivita_progettoByClause (UserContext userContext, 
															 Pdg_variazione_riga_gestBulk dett,
															 ProgettoBulk prg, 
															 CompoundFindClause clause) throws ComponentException, PersistencyException {
		ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, prg,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettoHome.createSQLBuilder();
		sql.addSQLClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","tipo_fase",sql.EQUALS,ProgettoBulk.TIPO_FASE_GESTIONE);

		Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
		Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
		if (parCnrBulk.getFl_nuovo_pdg())
			sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		else
			sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);

		// Se uo 999.000 in scrivania: visualizza tutti i progetti
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			if (parCnrBulk.getFl_nuovo_pdg())
				sql.addSQLExistsClause("AND",progettoHome.abilitazioniCommesse(userContext));
			else
				sql.addSQLExistsClause("AND",progettoHome.abilitazioniModuli(userContext));
		}	  
		if (clause != null) sql.addClause(clause);
		return sql; 
	}	
	
	public SQLBuilder selectProgettoByClause (UserContext userContext, 
											  Pdg_variazione_riga_gestBulk dett,
											  ProgettoBulk prg, 
											  CompoundFindClause clause) throws ComponentException, PersistencyException {
		return selectLinea_attivita_progettoByClause (userContext, dett, prg, clause); 
	}	

	/**
	 * Aggiunge delle clausole a tutte le operazioni di ricerca eseguite su Elemento_voceBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di Elemento Voce
	 * Post: Vengono restituiti tutti gli Elementi Voce che:
	 * 		 - sono associate a classificazioni di un livello pari a quello definito in Parametri_Ente;
	 * 		 - le classificazioni associate sono figlie della classificazione del dettaglio gestionale Pdg_modulo_spese_gestBulk
	 * 		 - non sia una partita di giro
	 * 		 - abbia la FUNZIONE uguale a quella della Linea di Attività del dettaglio gestionale Pdg_modulo_spese_gestBulk
	 * 		 - il CD_TIPO_UNITA è uguale a quello della UO associata al CDR
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clause clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */
	public SQLBuilder selectElemento_voceByClause (UserContext userContext, 
												   Pdg_variazione_riga_spesa_gestBulk dett,
												   Elemento_voceBulk elementoVoce, 
												   CompoundFindClause clause) throws ComponentException, PersistencyException {
		try {
			Optional<ProgettoBulk> optProgetto = Optional.empty();
			if (Optional.ofNullable(dett.getProgetto()).flatMap(el -> Optional.ofNullable(el.getPg_progetto())).isPresent()) {
				ProgettoHome home = (ProgettoHome) getHome(userContext, ProgettoBulk.class);
				home.setFetchPolicy("it.cnr.contab.progettiric00.comp.ProgettoRicercaComponent.find");
				optProgetto = Optional.ofNullable((ProgettoBulk) home.findByPrimaryKey(dett.getProgetto()));
				getHomeCache(userContext).fetchAll(userContext);
			}

			boolean isProgettoWithPianoEconomico = optProgetto.map(ProgettoBulk::isPianoEconomicoRequired).orElse(Boolean.FALSE);

			/**
			 * Gestione disattivata su richiesta di Claudia Rosati che richiedeva che dovevano essere visualizzate le voci di piano economico
			 * tenendo in considerazione anche della rimodulazione alla quale si appoggia la variazione
			 */
			//String columnMapName = isProgettoWithPianoEconomico?"V_ELEMENTO_VOCE_PDG_SPE_PIAECO":"V_ELEMENTO_VOCE_PDG_SPE";
			String columnMapName = "V_ELEMENTO_VOCE_PDG_SPE";

			if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

			SQLBuilder sql = getHome(userContext, elementoVoce, columnMapName).createSQLBuilder();
			
			if(clause != null) sql.addClause(clause);
	
			sql.addSQLClause(FindClause.AND, columnMapName+".ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
	
			sql.addTableToHeader("PARAMETRI_LIVELLI");
			sql.addSQLJoin(columnMapName+".ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");
	
			sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
			sql.addSQLJoin(columnMapName+".ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
			sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");
	
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR, columnMapName+".FL_PARTITA_GIRO", SQLBuilder.ISNULL, null);
			sql.addSQLClause(FindClause.OR, columnMapName+".FL_PARTITA_GIRO", SQLBuilder.EQUALS, "N");
			sql.closeParenthesis();
			sql.addSQLClause( FindClause.AND, columnMapName+".FL_SOLO_RESIDUO", SQLBuilder.EQUALS, "N");
			if (dett.getLinea_attivita() != null)
				sql.addSQLClause(FindClause.AND,columnMapName+".CD_FUNZIONE",SQLBuilder.EQUALS,dett.getLinea_attivita().getCd_funzione());
	
			if(!Utility.createParametriCnrComponentSession().getParametriCnr(userContext, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )).getFl_nuovo_pdg())
				if (dett.getCdr_assegnatario()!=null && dett.getCdr_assegnatario().getUnita_padre().getCd_tipo_unita() != null)
					sql.addSQLClause(FindClause.AND,columnMapName+".CD_TIPO_UNITA",SQLBuilder.EQUALS,dett.getCdr_assegnatario().getUnita_padre().getCd_tipo_unita());

			/**
			 * Gestione disattivata su richiesta di Claudia Rosati che richiedeva che dovevano essere visualizzate le voci di piano economico
			 * tenendo in considerazione anche della rimodulazione alla quale si appoggia la variazione
			 */
			/*
			if (isProgettoWithPianoEconomico) {
				/*Limito la ricerca alle sole voci associate al progetto per l'anno del residuo
				sql.addSQLClause(FindClause.AND, columnMapName + ".PG_PROGETTO_ASSOCIATO", SQLBuilder.EQUALS, optProgetto.get().getPg_progetto());
				sql.addSQLClause(FindClause.AND, columnMapName + ".ESERCIZIO_PIANO_ASSOCIATO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ));
			}
			*/

			/*
			 * controllo aggiunto solo per variazioni su anni successivi a quello di attivazione piano economico e per 
			 * progetti con Piano Economico con data fine/proroga successiva all'anno di attivazione
			 */
			BigDecimal annoFrom = Utility.createConfigurazioneCnrComponentSession().getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
			if (Optional.ofNullable(annoFrom).map(BigDecimal::intValue).map(el->el.compareTo(CNRUserContext.getEsercizio( userContext ))<=0).orElse(Boolean.FALSE)) {
				optProgetto.orElseThrow(() -> new ApplicationException("Errore: Progetto non valorizzato sulla riga della variazione!"));

				/*
					Il filtro sulle voci di bilancio presenti nel piano economico del progetto va fatto se:
					1. il progetto prevede il piano economico
					2. il progetto rientra nel periodo di attivazione dei progetti
				 */
				if (isProgettoWithPianoEconomico &&
						Optional.ofNullable(optProgetto.get().getOtherField().getAnnoFine())
								.filter(annoFine->annoFine.compareTo(annoFrom.intValue())>=0)
								.isPresent()) {
					/*
						se il progetto è attivo (anno fine del progetto maggiore o uguale all'anno di esercizio) occorre sempre controllare le voci associate al piano economico del progetto
					 */
					if (Optional.ofNullable(optProgetto.get().getOtherField().getAnnoFine())
								.filter(annoFine->annoFine.compareTo(CNRUserContext.getEsercizio(userContext))>=0)
								.isPresent()) {
						Ass_progetto_piaeco_voceHome assHome = (Ass_progetto_piaeco_voceHome) getHome(userContext, Ass_progetto_piaeco_voceBulk.class);
						SQLBuilder assSql = assHome.createSQLBuilder();
						assSql.addSQLClause(FindClause.AND, "ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO", SQLBuilder.EQUALS, dett.getProgetto().getPg_progetto());
						assSql.addSQLClause(FindClause.AND, "ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_PIANO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

						List<Ass_progetto_piaeco_voceBulk> list = assHome.fetchAll(assSql);
						List<Progetto_rimodulazione_voceBulk> listRim = new BulkList<Progetto_rimodulazione_voceBulk>();
						if (Optional.ofNullable(dett.getPdg_variazione().getProgettoRimodulazione())
								.filter(rim -> rim.getPg_progetto().equals(dett.getProgetto().getPg_progetto()))
								.isPresent()) {
							Progetto_rimodulazioneHome rimHome = (Progetto_rimodulazioneHome) getHome(userContext, Progetto_rimodulazioneBulk.class);
							listRim = new BulkList<>(rimHome.findDettagliVoceRimodulazione(dett.getPdg_variazione().getProgettoRimodulazione()));
						}

						if (list.isEmpty() && listRim.isEmpty())
							sql.addSQLClause(FindClause.AND, "V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", SQLBuilder.EQUALS, -100);
						else {
							//Recupero la lista delle voci movimentate perchè se tra quelle da eliminare occorre comunque selezionarle per consentire
							//all'utente di effettuare una variazione negativa
							List<V_saldi_voce_progettoBulk> vociMovimentate = ((V_saldi_voce_progettoHome) getHome(userContext, V_saldi_voce_progettoBulk.class))
									.cercaSaldoVoce(optProgetto.get().getPg_progetto()).stream()
									.filter(el -> el.getAssestato().compareTo(BigDecimal.ZERO) > 0 ||
											el.getUtilizzatoAssestatoFinanziamento().compareTo(BigDecimal.ZERO) > 0)
									.collect(Collectors.toList());

							sql.openParenthesis(FindClause.AND);
							for (Ass_progetto_piaeco_voceBulk assVoce : list) {
								//Se la voce è stata eliminata nella rimodulazione la stessa non viene proposta
								if (listRim.stream().filter(voceRim -> voceRim.getElementoVoce().equalsByPrimaryKey(assVoce.getElemento_voce()))
										.filter(Progetto_rimodulazione_voceBulk::isTiOperazioneEliminato)
										.filter(voceRim -> !vociMovimentate.stream()
												.filter(voceMov -> voceMov.getEsercizio_voce().equals(voceRim.getElementoVoce().getEsercizio()))
												.filter(voceMov -> voceMov.getTi_appartenenza().equals(voceRim.getElementoVoce().getTi_appartenenza()))
												.filter(voceMov -> voceMov.getTi_gestione().equals(voceRim.getElementoVoce().getTi_gestione()))
												.filter(voceMov -> voceMov.getCd_elemento_voce().equals(voceRim.getElementoVoce().getCd_elemento_voce()))
												.findFirst().isPresent())
										.findFirst().isPresent())
									continue;
								Elemento_voceBulk voceNew = Utility.createCRUDConfigAssEvoldEvnewComponentSession().getCurrentElementoVoce(userContext, assVoce.getElemento_voce(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
								if (Optional.ofNullable(voceNew).flatMap(el -> Optional.ofNullable(el.getCd_elemento_voce())).isPresent()) {
									sql.openParenthesis(FindClause.OR);
									sql.addSQLClause(FindClause.AND, columnMapName+".ESERCIZIO", SQLBuilder.EQUALS, voceNew.getEsercizio());
									sql.addSQLClause(FindClause.AND, columnMapName+".TI_APPARTENENZA", SQLBuilder.EQUALS, voceNew.getTi_appartenenza());
									sql.addSQLClause(FindClause.AND, columnMapName+".TI_GESTIONE", SQLBuilder.EQUALS, voceNew.getTi_gestione());
									sql.addSQLClause(FindClause.AND, columnMapName+".CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, voceNew.getCd_elemento_voce());
									sql.closeParenthesis();
								}
							}
							//Aggiungo le voci di bilancio inserite nella rimodulazione
							for (Progetto_rimodulazione_voceBulk assRimVoce : listRim) {
								if (assRimVoce.isTiOperazioneAggiunto()) {
									Elemento_voceBulk voceNew = Utility.createCRUDConfigAssEvoldEvnewComponentSession().getCurrentElementoVoce(userContext, assRimVoce.getElementoVoce(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
									if (Optional.ofNullable(voceNew).flatMap(el -> Optional.ofNullable(el.getCd_elemento_voce())).isPresent()) {
										sql.openParenthesis(FindClause.OR);
										sql.addSQLClause(FindClause.AND, columnMapName+".ESERCIZIO", SQLBuilder.EQUALS, voceNew.getEsercizio());
										sql.addSQLClause(FindClause.AND, columnMapName+".TI_APPARTENENZA", SQLBuilder.EQUALS, voceNew.getTi_appartenenza());
										sql.addSQLClause(FindClause.AND, columnMapName+".TI_GESTIONE", SQLBuilder.EQUALS, voceNew.getTi_gestione());
										sql.addSQLClause(FindClause.AND, columnMapName+".CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, voceNew.getCd_elemento_voce());
										sql.closeParenthesis();
									}
								}
							}
							sql.closeParenthesis();
						}
					} else {
						/*
							Progetto scaduto.
							1) Se variazione di maggiore entrata/spesa su stesso istituto e/o istituti diversi devo poter associare qualsiasi voce di bilancio
							1) Se variazione di tipo prelievo dai fondi devo poter associare qualsiasi voce di bilancio
							2) Per altre variazioni devo poter movimentare solo le voci di bilancio movimentate e non associate che hanno un assestato da spostare
						 */
						if (!Tipo_variazioneBulk.VARIAZIONE_POSITIVA_STESSO_ISTITUTO.equals(dett.getPdg_variazione().getTipologia()) &&
							!Tipo_variazioneBulk.VARIAZIONE_POSITIVA_ISTITUTI_DIVERSI.equals(dett.getPdg_variazione().getTipologia()) &&
						    !Tipo_variazioneBulk.PRELIEVO_FONDI.equals(dett.getPdg_variazione().getTipologia())) {
							//Recupero la lista delle voci movimentate perchè se tra quelle da eliminare occorre comunque selezionarle per consentire
							//all'utente di effettuare una variazione negativa
							List<V_saldi_voce_progettoBulk> vociConDisponibilita = ((V_saldi_voce_progettoHome) getHome(userContext, V_saldi_voce_progettoBulk.class))
									.cercaSaldoVoce(optProgetto.get().getPg_progetto()).stream()
									.filter(el -> el.getDispAssestatoFinanziamento().compareTo(BigDecimal.ZERO) > 0 ||
											el.getDispAssestatoCofinanziamento().compareTo(BigDecimal.ZERO) > 0)
									.collect(Collectors.toList());

							if (vociConDisponibilita.isEmpty())
								sql.addSQLClause(FindClause.AND, columnMapName+".ESERCIZIO", SQLBuilder.EQUALS, -100);
							else {
								sql.openParenthesis(FindClause.AND);
								vociConDisponibilita.stream().forEach(voceNew->{
									sql.openParenthesis(FindClause.OR);
									sql.addSQLClause(FindClause.AND, columnMapName+".ESERCIZIO", SQLBuilder.EQUALS, voceNew.getEsercizio());
									sql.addSQLClause(FindClause.AND, columnMapName+".TI_APPARTENENZA", SQLBuilder.EQUALS, voceNew.getTi_appartenenza());
									sql.addSQLClause(FindClause.AND, columnMapName+".TI_GESTIONE", SQLBuilder.EQUALS, voceNew.getTi_gestione());
									sql.addSQLClause(FindClause.AND, columnMapName+".CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, voceNew.getCd_elemento_voce());
									sql.closeParenthesis();
								});
								sql.closeParenthesis();
							}
						}
					}
				}
			}
			if (clause != null) sql.addClause(clause);
			return sql;
		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	}
	
	public SQLBuilder selectElemento_voceByClause (UserContext userContext, 
												   Pdg_variazione_riga_entrata_gestBulk dett,
												   Elemento_voceBulk elementoVoce, 
												   CompoundFindClause clause) throws ComponentException, PersistencyException {
		if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_ETR").createSQLBuilder();

		if(clause != null) sql.addClause(clause);

		sql.addSQLClause("AND", "V_ELEMENTO_VOCE_PDG_ETR.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

		sql.addTableToHeader("PARAMETRI_LIVELLI");
		sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_ETR.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");

		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
		sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_ETR.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
		sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_ENTRATA");

		sql.openParenthesis("AND");
		sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_ETR.FL_PARTITA_GIRO", sql.ISNULL, null);	
		sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_ETR.FL_PARTITA_GIRO", sql.EQUALS, "N");	
		sql.closeParenthesis();
		sql.addSQLClause( "AND", "V_ELEMENTO_VOCE_PDG_ETR.FL_SOLO_RESIDUO", sql.EQUALS, "N");
		if (dett.getLinea_attivita() != null && dett.getCd_linea_attivita() != null)
			sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_ETR.CD_NATURA",sql.EQUALS,dett.getLinea_attivita().getCd_natura());
		if (dett.getCdr_assegnatario()!=null && dett.getCdr_assegnatario().getUnita_padre().getCd_tipo_unita() != null && !dett.getCdr_assegnatario().getUnita_padre().getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC))
			sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_ETR.FL_VOCE_SAC",sql.EQUALS,"N");	      
	
		if (clause != null) sql.addClause(clause);

		return sql;
	}

	public SQLBuilder selectAreaByClause (UserContext userContext,Pdg_variazione_riga_gestBulk dett,CdsBulk ass_uo,CompoundFindClause clause) throws ComponentException, PersistencyException
	{
		SQLBuilder sql = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
		if (clause != null) 
		  sql.addClause(clause);		
		if(dett.isNonAccentrata()|| (dett.getElemento_voce()!=null && dett.getElemento_voce().getFl_prelievo())){ 
			sql.addTableToHeader("ASS_UO_AREA");
			sql.addTableToHeader("UNITA_ORGANIZZATIVA UO");
			sql.addTableToHeader("UNITA_ORGANIZZATIVA CDS");
			sql.addSQLClause("AND", "ASS_UO_AREA.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLJoin("CDS.CD_UNITA_PADRE","UO.CD_UNITA_PADRE");
			sql.addSQLJoin("CDS.CD_UNITA_ORGANIZZATIVA","ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA");
			sql.addSQLJoin("ASS_UO_AREA.CD_AREA_RICERCA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		}
		else{
			sql.addTableToHeader("UNITA_ORGANIZZATIVA UO");
			sql.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","UO.CD_UNITA_PADRE");
		}	    
		SQLBuilder sql2 = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
		sql2.addTableToHeader("UNITA_ORGANIZZATIVA UO");
		sql2.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		sql2.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","UO.CD_UNITA_PADRE");
		return sql.union(sql2,false);
	}

	private BigDecimal calcolaImporto(UserContext userContext, SQLBuilder sql) throws ComponentException{
		BigDecimal totale = Utility.ZERO;
		try {
			java.sql.ResultSet rs = null;
			LoggableStatement ps = null;
			try {
				ps = sql.prepareStatement(getConnection(userContext));
				try {
					rs = ps.executeQuery();
					if (rs.next() && rs.getBigDecimal(1)!= null)
					  totale = totale.add(rs.getBigDecimal(1));
				} catch (java.sql.SQLException e) {
					throw handleSQLException(e);
				} finally {
					if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}		
		return totale;
	}

	private void valorizzaImportoTotali(UserContext userContext, Pdg_modulo_speseBulk pdg) throws ComponentException{
    	try {
    		Pdg_modulo_speseHome testataHome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
			pdg.setTotale_spese_accentrate_esterne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliAccEst(userContext,pdg)));						
			pdg.setTotale_spese_accentrate_interne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliAccInt(userContext,pdg)));						
			pdg.setTotale_spese_decentrate_esterne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliDecEst(userContext,pdg)));						
			pdg.setTotale_spese_decentrate_interne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliDecInt(userContext,pdg)));						
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}	
	}		

	private MessaggioBulk generaMessaggioCopertura(UserContext userContext, UtenteBulk utente, Ass_pdg_variazione_cdrBulk ass_var, String tipo) throws ComponentException, PersistencyException{
		MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
		MessaggioBulk messaggio = new MessaggioBulk();
		messaggio.setPg_messaggio(new Long(messHome.fetchNextSequenceValue(userContext,"CNRSEQ00_PG_MESSAGGIO").longValue()));
		messaggio.setCd_utente(utente.getCd_utente());
		messaggio.setPriorita(new Integer(1));
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - È stata raggiunta la quota di " + (tipo.equals(Elemento_voceHome.GESTIONE_SPESE)?"Spesa":"Entrata") + " assegnata alla Variazione di competenza");
		messaggio.setCorpo("Numero variazione:"+ass_var.getPg_variazione_pdg());
		messaggio.setCorpo(messaggio.getCorpo() + "\n" + "Il CdR :"+ass_var.getCentro_responsabilita().getCd_ds_cdr()+" ha coperto la quota assegnata.");
		messaggio.setSoggetto(messaggio.getDs_messaggio());
		messaggio.setToBeCreated(); 
		return messaggio;	 	
	}

	private void inizializzaVistosuDettagli(UserContext userContext, Ass_pdg_variazione_cdrBulk ass) throws ComponentException{
		try {
			if (!ass.getPdg_variazione().isApprovata())	return;

			WorkpackageHome lineaHome = (WorkpackageHome)getHome(userContext, WorkpackageBulk.class);
			UtenteBulk utente = (UtenteBulk) getHome( userContext, UtenteBulk.class).findByPrimaryKey(new UtenteKey(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));

			Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

			if (uoScrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
				if (utente.getDipartimento()!=null && utente.getDipartimento().getCd_dipartimento()!=null){
					for (java.util.Iterator rigaIt=ass.getRigheVariazioneEtrGest().iterator();rigaIt.hasNext();){
						Pdg_variazione_riga_gestBulk riga = (Pdg_variazione_riga_gestBulk)rigaIt.next();
						if (lineaHome.findDipartimento(userContext, riga.getLinea_attivita()).equalsByPrimaryKey(utente.getDipartimento()))
							riga.setFl_riga_vistabile(Boolean.TRUE);
					}
					for (java.util.Iterator rigaIt=ass.getRigheVariazioneSpeGest().iterator();rigaIt.hasNext();){
						Pdg_variazione_riga_gestBulk riga = (Pdg_variazione_riga_gestBulk)rigaIt.next();
						if (lineaHome.findDipartimento(userContext, riga.getLinea_attivita()).equalsByPrimaryKey(utente.getDipartimento()))
							riga.setFl_riga_vistabile(Boolean.TRUE);
					}
				}
				else
				{
					/**
					 * Il flag viene impostato a "TRUE" quando accede un utente di tipo ENTE senza dipartimento per
					 * consentire a chi deve dare "APPROVAZIONE FORMALE" di conoscere le righe della variazione
					 * ancora in attesa di apposizione di visto da parte dei dipartimenti.
					 */
					for (java.util.Iterator rigaIt=ass.getRigheVariazioneEtrGest().iterator();rigaIt.hasNext();)
						((Pdg_variazione_riga_gestBulk)rigaIt.next()).setFl_riga_vistabile(Boolean.TRUE);
					for (java.util.Iterator rigaIt=ass.getRigheVariazioneSpeGest().iterator();rigaIt.hasNext();)
						((Pdg_variazione_riga_gestBulk)rigaIt.next()).setFl_riga_vistabile(Boolean.TRUE);
				}
			}
					
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}
	}

	public ProgettoBulk getProgettoLineaAttivita(UserContext usercontext, Pdg_variazione_riga_gestBulk dett) throws ComponentException{
		try {
			PersistentHome laHome = getHome(usercontext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
			SQLBuilder sql = laHome.createSQLBuilder();
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,dett.getEsercizio());
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,dett.getCd_cdr_assegnatario());
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,dett.getCd_linea_attivita());
			List list = laHome.fetchAll(sql);
			if (list.size()==1)
				return (ProgettoBulk)getHome(usercontext, ProgettoBulk.class).findByPrimaryKey(usercontext, ((WorkpackageBulk)list.get(0)).getProgetto());
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return null;
	}
}
