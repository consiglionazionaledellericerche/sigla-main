package it.cnr.contab.config00.comp;

import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPBulk;
import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.*;

import java.io.FileInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.StringTokenizer;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPBulk;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPHome;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FindBP;

public class Linea_attivitaComponent extends CRUDComponent implements ILinea_attivitaMgr,Cloneable,Serializable
{

/**
  *  Controlla che l'esercizio di fine validità sia corretto.
  *  Esercizio fine validità maggiore di quello del cdr
  *	   PreCondition:
  *		 L'esercizio di fine validità è maggiore di quello del cdr
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "Esercizio di terminazione deve essere minore o uguale a <esercizio cdr>"
  *  Esercizio fine validità minore dell'esercizio di qualche dettaglio di pdg
  *	   PreCondition:
  *		 Esiste qualche dettaglio di qualche pdg con questa linea di attività con esercizio maggiore della fine validità
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "L'Esercizio di terminazione deve essere superiore agli esercizi per cui sono stati definiti dei preventivi"
  *  Default
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *		 Esce senza alcuna eccezione
 */
private void aggiornaEsercizioFine(UserContext userContext, WorkpackageBulk latt) throws it.cnr.jada.comp.ComponentException {
	try {
		//rileggo la linea attivita' dal db per verificare se e' stato modificato l'esercizio fine
		WorkpackageBulk lattDB = (WorkpackageBulk) getHome( userContext, WorkpackageBulk.class).findByPrimaryKey( new WorkpackageBulk(latt.getCd_centro_responsabilita(), latt.getCd_linea_attivita()));
		if ( lattDB.getEsercizio_fine() == null && latt.getEsercizio_fine() == null )
			return;
		if ( lattDB.getEsercizio_fine() != null && lattDB.getEsercizio_fine().compareTo(latt.getEsercizio_fine()) == 0 )
			return;
		//l'esercizio fine deve essere <= dell'esercizio fine del cds da cui dipende
		if ( latt.getCentro_responsabilita().getEsercizio_fine() != null &&
			 latt.getCentro_responsabilita().getEsercizio_fine().compareTo( latt.getEsercizio_fine() ) < 0 )
				throw handleException( new ApplicationException(" Esercizio di terminazione deve essere minore o uguale a " + latt.getCentro_responsabilita().getEsercizio_fine().toString()));

		checkCessazioneLa(userContext,latt);
	} catch (Throwable e) {
		throw handleException(latt,e);
	} 

}
protected void checkCessazioneLa(UserContext userContext,WorkpackageBulk la) throws ComponentException {
	try {
		LoggableStatement cs = new LoggableStatement(getConnection(userContext), 
			"{  call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB012.checkCessazioneLa(?, ?, ?)}",false,this.getClass());
		try {
			cs.setInt( 1, la.getEsercizio_fine().intValue() );
			cs.setString( 2, la.getCd_centro_responsabilita() );		
			cs.setString( 3, la.getCd_linea_attivita());
			cs.execute();		
		} finally {
			cs.close();
		}	
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	}	
}
/** 
  *  Esercizio fine validità non impostato 
  *	   PreCondition:
  *		 L'esercizio di fine validità è nullo o minore dell'esercizio di inizio validità
  *    PostCondition:
  *		 L'esercizio di fine validità viene impostato uguale all'esercizio di fine validità del cdr.
  *  Tipo linea attività non specificato
  *    PreCondition:
  *      Non è stato specificato il tipo linea attività
  *    PostCondition:
  *		 Imposta cd_tipo_linea_attivita = 'PROP' 
  *  Normale
  *    PreCondition:
  *      Viene richiesto la creazione di una nuova linea di attivita
  *    PostCondition:
  *		 Invoca validaFunzione() e validaNaturaPerInserimento(), quindi effettua l'inserimento
 */
public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext uc, it.cnr.jada.bulk.OggettoBulk bulk) throws ComponentException {
	try {
		WorkpackageBulk latt = (WorkpackageBulk) bulk;
		
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(uc,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc),latt))
			throw new ApplicationException("Non è possibile creare nuovi GAE ad esercizio chiuso.");
		
		if (latt.getTi_gestione()==null ) throw new ApplicationException( "E' obbligatorio indicare il tipo di gestione. " );
		
		if (latt.getTi_gestione().compareTo(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE)==0 && 
				((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).isCofogObbligatorio(uc)&& (latt.getCd_cofog()==null))
				throw new ApplicationException("Non è possibile creare GAE di spesa senza indicare la classificazione Cofog.");	
			
		if ((latt.getProgetto() == null ||(latt.getProgetto() != null && latt.getProgetto().getPg_progetto() == null))&& isCommessaObbligatoria(uc,latt ))
			throw new ApplicationException( "La Commessa sul GAE non può essere nulla. " );

		if(latt.getTipo_linea_attivita() == null || latt.getTipo_linea_attivita().getCd_tipo_linea_attivita() == null) {
			latt.setTipo_linea_attivita((Tipo_linea_attivitaBulk)getHome(uc,Tipo_linea_attivitaBulk.class).findByPrimaryKey(new Tipo_linea_attivitaBulk("PROP")));
		}
		if ( latt.getEsercizio_fine() == null || latt.getEsercizio_fine().compareTo( latt.getCentro_responsabilita().getEsercizio_fine()) > 0 )
			latt.setEsercizio_fine( latt.getCentro_responsabilita().getEsercizio_fine());
		validaFunzione(uc,latt);
		validaNaturaPerInsieme(uc,latt);
		/* Angelo 18/11/2004 Aggiunta gestione PostIt*/
		for(int i = 0; ((WorkpackageBulk)bulk).getDettagliPostIt().size() > i; i++) {
			/*Valorizzazione id PostIt*/
			if (((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).getId()== null )
			{					
			 Integer idPostit = new Integer (0);
			 PostItHome PostIt_home = (PostItHome) getHome(uc,PostItBulk.class);
			  try{		
				 idPostit = PostIt_home.getMaxId();
			  }catch (it.cnr.jada.persistency.IntrospectionException ie){
			   throw handleException(ie);
			  }catch (PersistencyException pe){
			   throw handleException(pe);
			  }
			 ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).setId(idPostit);
			}
			/*Fine valorizzazione id PostIt*/
		  ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).setCd_centro_responsabilita(latt.getCd_centro_responsabilita());
		  /*Attenzione qui il codice linea attivita è ancora nullo !*/
		  ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).setCd_linea_attivita(latt.getCd_linea_attivita());
		}
		/*Fine PostIt*/		
		return super.creaConBulk( uc, bulk );
	} catch(PersistencyException e) {
		throw handleException(bulk,e);
	} catch (RemoteException e) {
		throw handleException(bulk,e);
	} 
}
//^^@@
/** 
  *  Linea di attività CSSAC già esistente
  *    PreCondition:
  *      Viene cercata E TROVATA una linea di attività con TIPO=CSSAC, cd_cdr_collegato=CDR LA origine, e cd_lacollegato=cd_linea_attivita origine.
  *    PostCondition:
  *      Quest'istanza viene restituita all'utente.
  *  Linea di attività CSSAC non esiste
  *    PreCondition:
  *      Viene cercata E NON TROVATA una linea di attività con TIPO=CSSAC, cd_cdr_collegato=CDR LA origine, e cd_lacollegato=cd_linea_attivita origine.
  *    PostCondition:
  *      Viene creata una linea di attività con codice CdR = aCdrBulk.cd_centro_responsabilita, TIPO=CSSAC, cd_cdr_collegato=CDR LA origine, cd_lacollegato=cd_linea_attivita origine, Funzione = funzione LA origine, e Natura = 4, denominazione = Denominazione LA origine, Descrizione=descrizione LA origine, e Risultati=risultati LA origine. La numerazione segue le logiche della numerazione delle linee di attività di tipo Sistema (SXX..XX) dove XX..XX progressivo numerico di numero di cifre = a quanto specificato in lunghezza chiavi per tabella LINEA_ATTIVITA - 1.
  *      
 */
//^^@@
public WorkpackageBulk creaLineaAttivitaCSSAC (UserContext aUC,CdrBulk aCdrBulk,WorkpackageBulk aLABulk) throws ComponentException
        {
	try
	{

		        WorkpackageHome aLAH = (WorkpackageHome)getHome(aUC,WorkpackageBulk.class);
				WorkpackageBulk aB = new WorkpackageBulk();
				aB.setCd_la_collegato(aLABulk.getCd_linea_attivita());
				aB.setCentro_responsabilita(aCdrBulk);
				/* Valorizzo la Commessa del nuovo Workpackage con quella ereditata*/
				if (aLABulk.getProgetto() != null)
				  aB.setProgetto(new ProgettoBulk(((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio(),aLABulk.getProgetto().getPg_progetto(),ProgettoBulk.TIPO_FASE_PREVISIONE));
				Tipo_linea_attivitaBulk aTLA = new Tipo_linea_attivitaBulk();
				aTLA.setCd_tipo_linea_attivita(Tipo_linea_attivitaBulk.CSSAC);
				aTLA = (Tipo_linea_attivitaBulk)getHome(aUC,aTLA).findByPrimaryKey(aTLA);
				aB.setTipo_linea_attivita(aTLA);
				
		        java.util.List aL = aLAH.find(aB);

                Integer aEsercizio = (((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());				

   		        if(!aL.isEmpty())
                 if(!((WorkpackageBulk)aL.get(0)).checkValiditaInEsercizio(aEsercizio))
                   throw new ApplicationException("GAE CSSAC trovata ma non valida nell'esercizio corrente!");
		        
		        if(aL.isEmpty()) {
			     aB.setTi_gestione(Tipo_linea_attivitaBulk.TI_GESTIONE_ENTRATE);
				 aB.setEsercizio_inizio(((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());
			     aB.setFunzione(aLABulk.getFunzione());
			     NaturaBulk aN = new NaturaBulk();
			     aN.setCd_natura("4");
			     aB.setNatura(aN);
			     aB.setDenominazione(Tipo_linea_attivitaBulk.RICAVIFIGURATIVI);
			     aB.setDs_linea_attivita(aLABulk.getDs_linea_attivita());
			     aB.setRisultati(aLABulk.getRisultati());
			     aB.setUser(aUC.getUser());
			     aB.setToBeCreated();
		         return (WorkpackageBulk)creaConBulk(aUC, aB);
			    }
		         
				return (WorkpackageBulk)aL.get(0);	
			}
			catch (Exception e)
			{
				throw handleException(e);
			}
        }
//^^@@
/** 
  *  Linea di attività SAUO già esistente
  *    PreCondition:
  *      Viene cercata E TROVATA una linea di attività con TIPO=SAUO, Funzione=Funzione LA origine, e Natura=Natura LA origine se diversa da 5, altrimenti 1 che corresponde per codice CdR.
  *    PostCondition:
  *      Quest'istanza viene restituita all'utente.
  *  Linea di attività SAUO non esiste
  *    PreCondition:
  *      Viene cercata MA NON TROVATA una linea di attività con TIPO=SAUO, Funzione=Funzione LA origine, e Natura=Natura LA origine se diversa da 5 altirmenti 1, che corresponde per codice CdR.
  *    PostCondition:
  *      Viene creata una Linea di attività per il CdR richiesto con TIPO=SAUO, Funzione=Funzione LA origine, Natura=Natura LA origine e denominazione = 'Spese per costi altrui'. La numerazione per il codice linea di attività segue le logiche della numerazione delle linee di attività di tipo Sistema (SXX..XX) dove XX..XX progressivo numerico di numero di cifre = a quanto specificato in lunghezza chiavi per tabella LINEA_ATTIVITA - 1.
 */
//^^@@
public WorkpackageBulk creaLineaAttivitaSAUO (UserContext aUC,CdrBulk aCdrBulk,WorkpackageBulk aLABulk) throws ComponentException
        {
	try
	{

		        WorkpackageHome aLAH = (WorkpackageHome)getHome(aUC,WorkpackageBulk.class);
				WorkpackageBulk aB = new WorkpackageBulk();
				aB.setCentro_responsabilita(aCdrBulk);
		        /* Valorizzo la Commessa del nuovo Workpackage con quella ereditata*/
		        if (aLABulk.getProgetto() != null)
		          aB.setProgetto(new ProgettoBulk(((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio(),aLABulk.getProgetto().getPg_progetto(),ProgettoBulk.TIPO_FASE_PREVISIONE));				
				Tipo_linea_attivitaBulk aTLA = new Tipo_linea_attivitaBulk();
				aTLA.setCd_tipo_linea_attivita(Tipo_linea_attivitaBulk.SAUO);
				aTLA = (Tipo_linea_attivitaBulk)getHome(aUC,aTLA).findByPrimaryKey(aTLA);
				aB.setTipo_linea_attivita(aTLA);
			    aB.setFunzione(aLABulk.getFunzione());
			    if(aLABulk.getNatura().getCd_natura().equals("5")) {
				 NaturaBulk aNU = new NaturaBulk();
				 aNU.setCd_natura("1");
			     aB.setNatura(aNU);
			    } else {
			     aB.setNatura(aLABulk.getNatura());
			    }
			    	
		        java.util.List aL = aLAH.find(aB);

                Integer aEsercizio = (((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());				

   		        if(!aL.isEmpty())
                 if(!((WorkpackageBulk)aL.get(0)).checkValiditaInEsercizio(aEsercizio))
                   throw new ApplicationException("GAE SAUO trovata ma non valida nell'esercizio corrente!");
		        
		        if(aL.isEmpty()) {
			     aB.setTi_gestione(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE);
				 aB.setEsercizio_inizio(((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());
			     aB.setDenominazione(Tipo_linea_attivitaBulk.SPESECOSTIALTRUI);
			     aB.setDs_linea_attivita(Tipo_linea_attivitaBulk.SPESECOSTIALTRUI);
			     aB.setUser(aUC.getUser());
			     aB.setToBeCreated();
		         return (WorkpackageBulk)creaConBulk(aUC, aB);
			    }
		         
				return (WorkpackageBulk)aL.get(0);	
			}
			catch (Exception e)
			{
				throw handleException(e);
			}
}
/**
 * @author mspasiano
 * Inizializza la Funzione solo con la 01  
 */
protected void initializeKeysAndOptionsInto(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	super.initializeKeysAndOptionsInto(aUC, bulk);
	if(bulk instanceof WorkpackageBulk) {
		try {
			FunzioneHome home = (FunzioneHome)getHome(aUC, FunzioneBulk.class);
			java.util.Collection coll = home.find(home.findByPrimaryKey(new FunzioneBulk("01")));
			((WorkpackageBulk)bulk).setFunzioni(coll);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}
//^^@@
/** 
  *  Linea di attività SAUOP già esistente
  *    PreCondition:
  *      Viene cercata E TROVATA una linea di attività con TIPO=SAUOP, Funzione=01 e Natura=1 che corresponde per codice CdR.
  *    PostCondition:
  *      Quest'istanza viene restituita all'utente, perché esiste per ogni CDR (servente) una e una sola LA SAUOP.
  *  Linea di attività SAUOP non esiste
  *    PreCondition:
  *      Viene cercata MA NON TROVATA una linea di attività con TIPO=SAUOP, Funzione=01 e Natura=1 che corresponde per codice CdR.
  *    PostCondition:
  *      Viene creata una Linea di attività per il CdR richiesto con TIPO=SAUOP, Funzione=01, Natura=1, e denominazione = 'Spese per costi altrui'. La numerazione per il codice linea di attività segue le logiche della numerazione delle linee di attività di tipo Sistema (SXX..XX) dove XX..XX progressivo numerico di numero di cifre = a quanto specificato in lunghezza chiavi per tabella LINEA_ATTIVITA - 1.
 */
//^^@@
public WorkpackageBulk creaLineaAttivitaSAUOP (UserContext aUC,CdrBulk aCdrBulk) throws ComponentException
        {
	try
	{

		        WorkpackageHome aLAH = (WorkpackageHome)getHome(aUC,WorkpackageBulk.class);
				WorkpackageBulk aB = new WorkpackageBulk();
				aB.setCentro_responsabilita(aCdrBulk);
				Tipo_linea_attivitaBulk aTLA = new Tipo_linea_attivitaBulk();
				aTLA.setCd_tipo_linea_attivita(Tipo_linea_attivitaBulk.SAUOP);
				aTLA = (Tipo_linea_attivitaBulk)getHome(aUC,aTLA).findByPrimaryKey(aTLA);
				aB.setTipo_linea_attivita(aTLA);
				
		        java.util.List aL = aLAH.find(aB);


                Integer aEsercizio = (((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());				

   		        if(!aL.isEmpty())
                 if(!((WorkpackageBulk)aL.get(0)).checkValiditaInEsercizio(aEsercizio))
                   throw new ApplicationException("GAE SAUOP trovata ma non valida nell'esercizio corrente!");
		        
		        if(aL.isEmpty()) {
			     aB.setTi_gestione(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE);
				 aB.setEsercizio_inizio(((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio());
			     aB.setDenominazione(Tipo_linea_attivitaBulk.SPESECOSTIALTRUI);
                 FunzioneBulk aF = new FunzioneBulk();
                 aF.setCd_funzione("01");
			     aB.setFunzione(aF);
                 NaturaBulk aN = new NaturaBulk();
                 aN.setCd_natura("1");
			     aB.setNatura(aN);
			     aB.setDs_linea_attivita(Tipo_linea_attivitaBulk.SPESECOSTIALTRUI);
			     aB.setUser(aUC.getUser());
			     aB.setToBeCreated();
		         return (WorkpackageBulk)creaConBulk(aUC, aB);
			    }
		         
				return (WorkpackageBulk)aL.get(0);	
			}
			catch (Exception e)
			{
				throw handleException(e);
			}
        }
/**
 * Esegue una operazione di eliminazione di una Linea di Attività
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di una Linea di attività non utilizzata
 * Pre:  La richiesta di cancellazione di una Linea di attività e' stata generata
 * Post: La Linea di attività e' stato cancellata
 *
 * Nome: Cancellazione di una Linea di attività utilizzata
 * Pre:  La richiesta di cancellazione di una Linea di attività utilizzata e' stata generata
 * Post: Un messaggio di errrore viene geenrato che suggerisce l'impostazione dell'Esercizio di Terminazione
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Linea di attività che deve essere cancellata 
 */

public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,(WorkpackageBulk)bulk))
			throw new ApplicationException("Non è possibile eliminare GAE con esercizio di fine validità chiuso.");
		  /*Se sto cancellando il Workpackage cancello anche tutti i dettagli */
		  if (bulk instanceof WorkpackageBulk){
			for(int i = 0; ((WorkpackageBulk)bulk).getDettagliPostIt().size() > i; i++) {
			  ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
			}            
		  }
		makeBulkPersistent( userContext,bulk );
	} catch(it.cnr.jada.persistency.sql.ReferentialIntegrityException e) {
		throw handleException(new ApplicationException( "La cancellazione non e' consentita in quanto il GAE selezionato e' utilizzato. Si consiglia l'impostazione dell'Esercizio di Terminazione. "));
	} catch (Throwable e) {
		throw handleException(bulk,e);
	} 
}
/**
 * Reimplementato per poter intercettare la DuplicateKeyException: nel
 * caso della linea di attività non viene intercettato prima (nel validaCreaConBulk)
 * perchè l'utente inserisce un codice che poi viene modificato dall'initializePrimaryKey
 * (ci aggiunge la 'P' davanti!)
 */
protected OggettoBulk eseguiCreaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException {
	try {
		return super.eseguiCreaConBulk(userContext,bulk);
	} catch(DuplicateKeyException e) {
		throw new CRUDDuplicateKeyException(e,bulk,(OggettoBulk)getHome(userContext,bulk).findByPrimaryKey(bulk));
	}
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 *  Normale
 *    PreCondition:
 *      Nessun'altra per-post condition è verificata
 *    PostCondition:
 *      Viene riletta la linea attività e caricato l'elenco dei risultati collegati.
 */	
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		WorkpackageBulk aLA = (WorkpackageBulk)super.inizializzaBulkPerModifica(userContext,bulk);

        RisultatoBulk aR = new RisultatoBulk();
        aR.setLinea_attivita(aLA);

		RisultatoHome aRH = (RisultatoHome)getHome(userContext,RisultatoBulk.class);
		
        it.cnr.jada.bulk.BulkList aBL = new it.cnr.jada.bulk.BulkList(aRH.find(aR));
		        
		aLA.setRisultati(aBL);
		
		WorkpackageHome testataHome = (WorkpackageHome)getHome(userContext, WorkpackageBulk.class);
		/* Angelo 18/11/2004 Aggiunta gestione PostIt*/
		aLA.setDettagliPostIt(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPostIt(aLA)));

		getHomeCache(userContext).fetchAll(userContext);
		
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,aLA))
			return asRO(aLA,"Non è possibile modificare GAE con esercizio di fine validità chiuso.");
		return aLA;
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 *  Normale
 *    PreCondition:
 *      E' stato modificata la natura o il cdr di una linea di attività con gestione spesa
 *    PostCondition:
 *      Viene impostato l'insieme uguale a quello della prima linea attività con la stessa natura e cdr di quella specificata.
 */
public WorkpackageBulk inizializzaNaturaPerInsieme(UserContext userContext,WorkpackageBulk linea_attivita) throws it.cnr.jada.comp.ComponentException {
	try {
		if (linea_attivita.TI_GESTIONE_SPESE.equals(linea_attivita.getTi_gestione()) &&
			linea_attivita.getInsieme_la() != null &&
			linea_attivita.getCentro_responsabilita() != null) {
			NaturaHome home = (NaturaHome)getHome(userContext,NaturaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addTableToHeader("LINEA_ATTIVITA");
			sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","LINEA_ATTIVITA.CD_INSIEME_LA",sql.EQUALS,linea_attivita.getCd_insieme_la());
			sql.addSQLClause("AND","LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA",sql.EQUALS,linea_attivita.getCd_centro_responsabilita());
			sql.addSQLClause("AND","LINEA_ATTIVITA.TI_GESTIONE",sql.EQUALS,linea_attivita.TI_GESTIONE_ENTRATE);
			Broker broker = home.createBroker(sql);
			if (broker.next()) {
				linea_attivita.setNatura((NaturaBulk)broker.fetch(NaturaBulk.class));
				linea_attivita.setNature(null);
			}
			broker.close();
			initializeKeysAndOptionsInto(userContext,linea_attivita);
		}
		return linea_attivita;
	} catch(Exception e) {
		throw handleException(e);
	}
}
public WorkpackageBulk inizializzaNature(UserContext userContext,WorkpackageBulk linea_attivita) throws it.cnr.jada.comp.ComponentException {
	try {
			NaturaHome home = (NaturaHome)getHome(userContext,NaturaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			if (linea_attivita.getTi_gestione() != null){
				if (linea_attivita.getTi_gestione().equals(linea_attivita.TI_GESTIONE_SPESE))
				  sql.addSQLClause("AND","FL_SPESA",sql.EQUALS,"Y");
				else if (linea_attivita.getTi_gestione().equals(linea_attivita.TI_GESTIONE_ENTRATE))
				  sql.addSQLClause("AND","FL_ENTRATA",sql.EQUALS,"Y"); 

			}
			Broker broker = home.createBroker(sql);
			linea_attivita.setNature(home.fetchAll(broker));
			broker.close();
	}catch(Exception e) {
		throw handleException(e);
	}
	return linea_attivita;
	
}

protected boolean isEsercizioChiuso(UserContext userContext,WorkpackageBulk latt) throws ComponentException {
	try {
		if (latt != null && latt.getCentro_responsabilita() != null) {
			EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
			return home.isEsercizioChiuso(userContext,latt.getEsercizio_fine(),latt.getCentro_responsabilita());
		}
		return false;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isCommessaObbligatoria(UserContext userContext,WorkpackageBulk latt) throws ComponentException {
	try {
		if (latt != null) {
			Parametri_cdsHome home = (Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class);
			return home.isCommessaObbligatoria(userContext,latt.getCentro_responsabilita().getUnita_padre().getUnita_padre().getCd_unita_organizzativa());
		}
		return false;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Integer esercizio,WorkpackageBulk latt) throws ComponentException {
	try {
		if (latt != null && latt.getCentro_responsabilita() != null) {
			EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
			return home.isEsercizioChiuso(userContext,esercizio,latt.getCentro_responsabilita());
		}
		return false;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
  *  Esercizio fine validità non valido
  *	   PreCondition:
  *		 Viene invocato il metodo aggiornaEsercizioFine e quest'ultimo genera una eccezione di validazione
  *    PostCondition:
  *		 Viene lasciata uscire l'eccezione senza salvare la linea di attività
  *  Default
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *		 Invoca:
  *        validaFunzione()
  *        validaModificaInsieme()
  *        validaNaturaPerInsieme()
  *        validaModificaFunzioneNatura()
  *      ed infine viene salvata la linea di attività specificata.
 */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	WorkpackageBulk linea_attivita = (WorkpackageBulk)bulk;

	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext,linea_attivita))
		throw new ApplicationException("Non è possibile modificare GAE con esercizio di fine validità chiuso.");
	
	try {
		if (linea_attivita.getTi_gestione().compareTo(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE)==0 && 
				((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).isCofogObbligatorio(userContext)
				&& (linea_attivita.getCd_cofog()==null))
				throw new ApplicationException("Non è possibile modificare GAE di spesa senza indicare la classificazione Cofog.");
	} catch (RemoteException e) {
		throw handleException(bulk,e);
	}	

	if (linea_attivita.getProgetto() == null && isCommessaObbligatoria(userContext,linea_attivita ))
		throw new ApplicationException( "La Commessa sul GAE non può essere nulla. " );

	aggiornaEsercizioFine(userContext, linea_attivita);
	validaFunzione(userContext,linea_attivita);
	validaModificaInsieme(userContext,linea_attivita);
	validaNaturaPerInsieme(userContext,linea_attivita);
	validaModificaFunzioneNatura(userContext,linea_attivita);
	/* Angelo 18/11/2004 Aggiunta gestione PostIt*/
	/*Valorizzazione id PostIt*/	
	for(int i = 0; ((WorkpackageBulk)bulk).getDettagliPostIt().size() > i; i++) {
	 /* Solo per i dettagli senza id */
	 if (((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).getId()== null )
	 {					
	  Integer idPostit = new Integer (0);
	  PostItHome PostIt_home = (PostItHome) getHome(userContext,PostItBulk.class);
	   try{		
		 idPostit = PostIt_home.getMaxId();
	   }catch (it.cnr.jada.persistency.IntrospectionException ie){
		throw handleException(ie);
	   }catch (PersistencyException pe){
		throw handleException(pe);
	   }
	  ((PostItBulk) ((WorkpackageBulk)bulk).getDettagliPostIt().get(i)).setId(idPostit);
	 }
	 /* Fine if*/
	/*Fine valorizzazione id PostIt*/
	}	
	return super.modificaConBulk( userContext, linea_attivita);
}
/**
  *  Default
  *	   PreCondition:
  *		 Viene richiesta una ricerca di linee di attività
  *    PostCondition:
  *		 Viene creata una query sulla tabella LINEA_ATTIVITA con le clausole
  *		 richieste più le seguenti clausole:
  *		 - L'esercizio di scrivania è compreso tra ESERCIZIO_INIZIO e ESERCIZIO_FINE
  *		 - Il cdr della linea di attività è gestibile dal cdr dell'utente (secondo
  *			le regole definite dalla query V_PDG_CDR_GESTIBILI)
 */
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

		//SQLBuilder sql = (SQLBuilder)super.select( userContext, clauses, bulk);
        SQLBuilder sql = getHome(userContext,bulk,"V_LINEA_ATTIVITA_PROGETTO").createSQLBuilder();
        sql.addClause(clauses);
        sql.addClause(bulk.buildFindClauses(new Boolean(true)));
		it.cnr.contab.utenze00.bulk.UtenteBulk utente = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext,it.cnr.contab.utenze00.bulk.UtenteBulk.class,null,"none").findByPrimaryKey(new it.cnr.contab.utenze00.bulk.UtenteBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
		
		sql.addTableToHeader("V_PDG_CDR_GESTIBILI");
		sql.addSQLJoin("V_LINEA_ATTIVITA_PROGETTO.ESERCIZIO","V_PDG_CDR_GESTIBILI.ESERCIZIO");
		sql.addSQLJoin("V_LINEA_ATTIVITA_PROGETTO.CD_CENTRO_RESPONSABILITA","V_PDG_CDR_GESTIBILI.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.CD_CDR_ROOT",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

		//sql.addClause( "AND", "esercizio_inizio", sql.LESS_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
		//sql.addClause( "AND", "esercizio_fine", sql.GREATER_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());	
		return sql;

}
/**
 * Pre:  Ricerca progetto
 * Post: Limitazione ai progetti diversi da quello in oggetto.
 */
        public SQLBuilder selectProgettoByClause (UserContext userContext,
                                              WorkpackageBulk linea_attivita,
                                              ProgettoBulk progetto,
                                              CompoundFindClause clause)
        throws ComponentException, PersistencyException
        {
			    ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
                SQLBuilder sql = progettohome.createSQLBuilder();
                sql.addClause( clause );
                sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
                sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", sql.EQUALS, linea_attivita.getPg_progetto());
                sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
			    sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
                // Se uo 999.000 in scrivania: visualizza tutti i progetti
	            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	            if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
					sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));
				}
                if (clause != null) 
                  sql.addClause(clause);
                return sql;
        }
/**
  *  Default
  *	   PreCondition:
  *		 Viene richiesto l'elenco dei cdr assegnabili ad una linea di attività
  *    PostCondition:
  *		 Viene creata una query sulla tabella dei CDR con le seguenti clausole
  *		 - Il cdr della linea di attività è gestibile dal cdr dell'utente (secondo
  *			le regole definite dalla query V_PDG_CDR_GESTIBILI)
 */
public SQLBuilder selectCentro_responsabilitaByClause(UserContext userContext, WorkpackageBulk linea_attivita, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{

		SQLBuilder sql = getHome(userContext, CdrBulk.class,"V_PDG_CDR_GESTIBILI").createSQLBuilder();
		sql.addClause( clauses );
		
		it.cnr.contab.utenze00.bulk.UtenteBulk utente = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext,it.cnr.contab.utenze00.bulk.UtenteBulk.class,null,"none").findByPrimaryKey(new it.cnr.contab.utenze00.bulk.UtenteBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
		
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.CD_CDR_ROOT",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

		return sql;
}
/**
  *  CDR non è specificato
  *	   PreCondition:
  *		 La linea di attività specificata ha il cdr nullo
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "Selezionare prima un CDR"
  *  Default
  *	   PreCondition:
  *		 Viene richiesto l'elenco degli insiemi assegnabili ad una linea di attività
  *    PostCondition:
  *		 Viene creata una query sulla tabella INSIEME_LA con le clausole specificate più la clausola CD_CDR = cdr della linea attività
 */
public SQLBuilder selectInsieme_laByClause(UserContext userContext, WorkpackageBulk linea_attivita, Insieme_laBulk insieme_la, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	// (11/06/2002 15.14.46) CNRADM
	// Modificato per selezionare gli insiemi compatibili col cdr
	// della linea attività.

	//return ((Insieme_laHome)getHome(userContext,insieme_la)).selectInsieme_laVisibiliByClauses(userContext,clauses);

	if (linea_attivita.getCd_centro_responsabilita() == null)
		throw new ApplicationException("Selezionare prima un CDR");
	SQLBuilder sql = getHome(userContext,insieme_la).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,linea_attivita.getCd_centro_responsabilita());
	return sql;
}
/**
  *  Default
  *	   PreCondition:
  *		 Viene richiesto l'elenco tipi linea attivitò assegnabili ad una linea di attività
  *    PostCondition:
  *		 Viene creata una query sulla tabella TIPO_LINEA_ATTIVITA con le seguenti clausole:
  *		 - ti_tipo_la != 'S' (sistema)
 */
public SQLBuilder selectTipo_linea_attivitaByClause(UserContext userContext, WorkpackageBulk linea_attivita, Tipo_linea_attivitaBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = getHome(userContext, Tipo_linea_attivitaBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	
	sql.addClause("AND","ti_tipo_la",sql.NOT_EQUALS,Tipo_linea_attivitaBulk.SISTEMA);

	return sql;
}
/** 
  *  Tipo gestione non specificato
  *	   PreCondition:
  *		 Non è stato specificato il flag ti_gestione
  *    PostCondition:
  *		 Viene forzato ti_gestione = 'E' (entrate)
  *  Funzione non specificata
  *    PreCondition:
  *      Non è stato specificato una funzione
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "E' necessario impostare la funzione per le linee di attività di spesa"
 */
private void validaFunzione(it.cnr.jada.UserContext uc,WorkpackageBulk linea_attivita) throws ComponentException {
	if (linea_attivita.TI_GESTIONE_ENTRATE.equals(linea_attivita.getTi_gestione()))
		linea_attivita.setFunzione(null);
	else if (linea_attivita.getFunzione() == null)
		throw new ApplicationException("E' necessario impostare la funzione per i GAE di spesa");
}
/**
 *  Natura e funzione non modificati
 *    PreCondition:
 *      La natura e la funzione della linea di attività non sono stati modificati 
 *    PostCondition:
 *      Esce senza eccezioni
 *  Linea di attività spese
 *    PreCondition:
 *      La linea di attività è di gestione spese ed esiste qualche dettaglio di spesa del pdg preventivo che vi fa riferimento
 *    PostCondition:
 *      Viene generata una ApplicationException con messaggio "Non è possibile cambiare funzione o natura perchè la linea di attività è utilizzata in piani di gestione"
 *  Linea di attività entrate
 *    PreCondition:
 *      La linea di attività è di gestione entrate ed esiste qualche dettaglio di entrata del pdg preventivo che vi fa riferimento
 *    PostCondition:
 *      Viene generata una ApplicationException con messaggio "Non è possibile cambiare funzione o natura perchè la linea di attività è utilizzata in piani di gestione"
 */
private void validaModificaFunzioneNatura(UserContext userContext,WorkpackageBulk linea_attivita) throws ComponentException {
	try {
		WorkpackageBulk linea_attivita_originale = (WorkpackageBulk)getHome(userContext,linea_attivita).findByPrimaryKey(linea_attivita);
		if (
			   linea_attivita_originale.getCd_funzione() != null && !linea_attivita_originale.getCd_funzione().equals(linea_attivita.getCd_funzione())
			|| linea_attivita_originale.getCd_natura() != null && !linea_attivita_originale.getCd_natura().equals(linea_attivita.getCd_natura())
		) {
			SQLBuilder sql;
			it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk pdg_preventivo_det;
			// CONTROLLO ENTRATE
			if (linea_attivita_originale.TI_GESTIONE_ENTRATE.equals(linea_attivita_originale.getTi_gestione()))
				sql = getHome(userContext,it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk.class).createSQLBuilder();
			// CONTROLLO SPESE
			else
				  sql = getHome(userContext,it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.class).createSQLBuilder();
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,linea_attivita_originale.getCd_centro_responsabilita());
			sql.addSQLClause("AND","CD_LINEA_ATTIVITA",sql.EQUALS,linea_attivita_originale.getCd_linea_attivita());
			if (sql.executeExistsQuery(getConnection(userContext))) {
			    linea_attivita.setFunzione(linea_attivita_originale.getFunzione());
			    linea_attivita.setNatura(linea_attivita_originale.getNatura());
				throw new ApplicationException("Non è possibile cambiare funzione o natura perchè il GAE è utilizzato in piani di gestione");
			}
		}
	} catch(PersistencyException e) {
		throw handleException(e);
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/**
 *  Insieme non modificato
 *    PreCondition:
 *      La linea di attività specificata ha lo stesso insieme rispetto all'ultima modifica
 *    PostCondition:
 *      Esce senza eccezioni
 *  Linea di attività già usata nel pdg
 *    PreCondition:
 *      Esiste un dettaglio del pdg che fa riferimento alla linea di attività specificata
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Non è possibile cambiare insieme perchè la linea di attività è usata nel piano di gestione."
 *
*/
private void validaModificaInsieme(UserContext userContext,WorkpackageBulk linea_attivita) throws ComponentException {
	try {
		WorkpackageBulk linea_attivita_originale = (WorkpackageBulk)getHome(userContext,linea_attivita).findByPrimaryKey(linea_attivita);
		if (linea_attivita_originale.getInsieme_la() != null &&
			!linea_attivita_originale.getInsieme_la().equalsByPrimaryKey(linea_attivita.getInsieme_la())) {
			SQLBuilder sql;
			it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk pdg_preventivo_det;
			// GESTIONE ENTRATE
			if (linea_attivita_originale.TI_GESTIONE_ENTRATE.equals(linea_attivita_originale.getTi_gestione()))
				sql = getHome(userContext,it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk.class).createSQLBuilder();
			else
				sql = getHome(userContext,it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.class).createSQLBuilder();
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,linea_attivita_originale.getCd_centro_responsabilita());
			sql.addSQLClause("AND","CD_LINEA_ATTIVITA",sql.EQUALS,linea_attivita_originale.getCd_linea_attivita());
			if (sql.executeExistsQuery(getConnection(userContext)))
				throw new ApplicationException("Non è possibile cambiare insieme perchè il GAE è usato nel piano di gestione.");
		}
	} catch(PersistencyException e) {
		throw handleException(e);
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Linea attività di tipo entrate già esistente nell'insieme
  *	   PreCondition:
  *		 La linea di attività ha gestione entrate ed esiste un'altra linea di attività nell'insieme specificato con la
  *		 stessa gestione.
  *    PostCondition:
  *		 Genera una ApplicationException con il messaggio: "Esiste già una linea di attività con gestione entrata in questo insieme"
  *  Linea di attività con natura diversa da quella delle altre linee dell'insieme - 1
  *    PreCondition:
  *      La linea di attività ha gestione entrate e le altre linee di attività dell'insieme specificato hanno natura diversa da quella specificata
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "Esistono linee di attività dello stesso insieme con natura diversa"
  *  Linea di attività con natura diversa da quella delle altre linee dell'insieme - 2
  *    PreCondition:
  *      La linea di attività ha gestione spese ed esiste una linea di attività con gestione entrate e natura diversa da quella specificata
  *    PostCondition:
  *		 Viene generata una ApplicationException con il messaggio "In questo insieme esiste una linea di attività con gestione entrate ma natura diversa"
 */
private void validaNaturaPerInsieme(UserContext userContext,WorkpackageBulk linea_attivita) throws it.cnr.jada.comp.ComponentException {
	try {
		if (linea_attivita.getCd_insieme_la() == null) return;
		WorkpackageHome home = (WorkpackageHome)getHome(userContext,linea_attivita);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","cd_insieme_la",sql.EQUALS,linea_attivita.getCd_insieme_la());
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,linea_attivita.getCd_centro_responsabilita());
		java.util.List linee = home.fetchAll(sql);
		if (linea_attivita.TI_GESTIONE_ENTRATE.equals(linea_attivita.getTi_gestione())) {
			for (java.util.Iterator i = linee.iterator();i.hasNext();) {
				WorkpackageBulk linea_attivita2 = (WorkpackageBulk)i.next();
				if (linea_attivita2.getCd_linea_attivita().equals(linea_attivita.getCd_linea_attivita()))
					continue;
				if (linea_attivita2.TI_GESTIONE_ENTRATE.equals(linea_attivita2.getTi_gestione()))
					throw new ApplicationException("Esiste già un GAE con gestione entrata in questo insieme");
				if (!linea_attivita2.getCd_natura().equals(linea_attivita.getCd_natura()))
					throw new ApplicationException("Esistono GAE dello stesso insieme con natura diversa");
			}
		} else {
			for (java.util.Iterator i = linee.iterator();i.hasNext();) {
				WorkpackageBulk linea_attivita2 = (WorkpackageBulk)i.next();
				if (linea_attivita2.getCd_linea_attivita().equals(linea_attivita.getCd_linea_attivita()))
					continue;
				if (linea_attivita2.TI_GESTIONE_ENTRATE.equals(linea_attivita2.getTi_gestione()) &&
					!linea_attivita2.getCd_natura().equals(linea_attivita.getCd_natura()))
					throw new ApplicationException("In questo insieme esiste un GAE con gestione entrate ma natura diversa");
			}
		}
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}
public java.util.List findListaGAEWS(UserContext userContext,String cdr,String tipo,String query,String dominio,String tipoRicerca)throws ComponentException{
		return findListaGAEWS(userContext,cdr,tipo,query,dominio,tipoRicerca,null);
}
public java.util.List findListaGAEWS(UserContext userContext,String cdr,String tipo,String query,String dominio,String tipoRicerca,String tipoFiltro)throws ComponentException{
	try {		
		WorkpackageHome home = (WorkpackageHome)getHome(userContext,WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("V_PDG_CDR_GESTIBILI");
		
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","V_PDG_CDR_GESTIBILI.ESERCIZIO");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","V_PDG_CDR_GESTIBILI.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.CD_CDR_ROOT",sql.EQUALS,cdr);
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,tipo);
		if (dominio.equalsIgnoreCase("codice"))
			sql.addSQLClause("AND","CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("descrizione")){
			
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_LINEA_ATTIVITA",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_LINEA_ATTIVITA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_LINEA_ATTIVITA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_LINEA_ATTIVITA)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_LINEA_ATTIVITA",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("cd_linea_attivita,DS_LINEA_ATTIVITA");
			}
	    if (tipoFiltro!=null && tipoFiltro.equalsIgnoreCase("fattura")){
	    	VFatturaPassivaSIPHome homeFat =  (VFatturaPassivaSIPHome)getHome(userContext, VFatturaPassivaSIPBulk.class,"VFATTURAPASSIVASIP_RID");
			SQLBuilder sql2 = homeFat.createSQLBuilder();
			sql2.setDistinctClause( true );
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","VFATTURAPASSIVASIP.ESERCIZIO");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","VFATTURAPASSIVASIP.CD_CENTRO_RESPONSABILITA");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA","VFATTURAPASSIVASIP.GAE");
			sql.addSQLExistsClause("AND",sql2);
		}else if(tipoFiltro!=null && tipoFiltro.equalsIgnoreCase("compenso")){
			VCompensoSIPHome homeComp = (VCompensoSIPHome)getHome(userContext, VCompensoSIPBulk.class,"VCOMPENSOSIP_RID");
			SQLBuilder sql2 = homeComp.createSQLBuilder();
			sql2.setDistinctClause( true );
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","VCOMPENSOSIP.ESERCIZIO");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","VCOMPENSOSIP.CD_CENTRO_RESPONSABILITA");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA","VCOMPENSOSIP.GAE");
			sql.addSQLExistsClause("AND",sql2);
		}else if(tipoFiltro!=null && tipoFiltro.equalsIgnoreCase("missione")){
			VMissioneSIPHome homeMis = (VMissioneSIPHome)getHome(userContext, VMissioneSIPBulk.class,"VMISSIONESIP_RID");
			SQLBuilder sql2 = homeMis.createSQLBuilder();
			sql2.setDistinctClause( true );
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","VMISSIONESIP.ESERCIZIO");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","VMISSIONESIP.CD_CENTRO_RESPONSABILITA");
			sql2.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA","VMISSIONESIP.GAE");
			sql.addSQLExistsClause("AND",sql2);
		}
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
public WorkpackageBulk completaOggetto(UserContext userContext,WorkpackageBulk linea)throws ComponentException, PersistencyException{
	linea.setProgetto((ProgettoBulk)getHome(userContext,ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),linea.getProgetto().getPg_progetto(),ProgettoBulk.TIPO_FASE_NON_DEFINITA)));
	linea.getProgetto().setProgettopadre((ProgettoBulk)getHome(userContext,ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),linea.getProgetto().getPg_progetto_padre(),ProgettoBulk.TIPO_FASE_NON_DEFINITA)));
 return linea;
}

/*Inserimento nella colonna BLOB*/
public void Inserimento_BLOB(UserContext userContext,it.cnr.jada.bulk.OggettoBulk oggetto,java.io.File file)throws ComponentException, PersistencyException{
	try {
		  /* Codice di recupero dell'ultimo id inserito nel Db */
		  PostItHome home = (PostItHome)getHome(userContext,PostItBulk.class);
		  SQLBuilder sql = home.createSQLBuilder();
		  int max = 0;
		  PostItBulk postIt_Bulk =new PostItBulk();
		  if (oggetto instanceof WorkpackageBulk) {
			  WorkpackageBulk wp=(WorkpackageBulk)oggetto;
			  postIt_Bulk.setCd_centro_responsabilita(wp.getCd_centro_responsabilita());
			  postIt_Bulk.setCd_linea_attivita(wp.getCd_linea_attivita());
		  }
		  else if(oggetto instanceof WorkpackageBulk) {
			  ProgettoBulk progetto=(ProgettoBulk)oggetto;
			  postIt_Bulk.setPg_progetto(progetto.getPg_progetto());
		  }
		  max=home.getMaxId(postIt_Bulk);
		  
		  postIt_Bulk = (PostItBulk)getHome(userContext,PostItBulk.class).findByPrimaryKey(new PostItBulk(new Integer(max)));
		  java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(file));
		  byte[] byteArr = new byte[1024];
		  oracle.sql.BLOB blob = (oracle.sql.BLOB)getHome(userContext,postIt_Bulk).getSQLBlob(postIt_Bulk,"BDATA");
		  java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
		  int len;
			
		  while ((len = in.read(byteArr))>0){
			  os.write(byteArr,0,len);
		  }
		  os.close();
		  in.close();
		  home.update(postIt_Bulk, userContext);
	} catch(Throwable e) {
		  throw handleException(e);
	} finally {					
		  file.delete();
	}	
  }
public java.util.List findListaGAEFEWS(UserContext userContext,String cdr,Integer modulo)throws ComponentException{
	try {		
		WorkpackageHome home = (WorkpackageHome)getHome(userContext,WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = home.createSQLBuilder();
		if(cdr!=null){
			sql.addTableToHeader("V_PDG_CDR_GESTIBILI");
			
			sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","V_PDG_CDR_GESTIBILI.ESERCIZIO");
			sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","V_PDG_CDR_GESTIBILI.CD_CENTRO_RESPONSABILITA");
			
			sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.CD_CDR_ROOT",sql.EQUALS,cdr);
			sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		}else{
			sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)); 
		}
		sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO",sql.EQUALS,modulo);
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}

}
