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

package it.cnr.contab.config00.comp;

import java.io.FileInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.expression.spel.ast.BooleanLiteral;

import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPBulk;
import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.latt.bulk.Ass_linea_attivita_esercizioBulk;
import it.cnr.contab.config00.latt.bulk.Ass_linea_attivita_esercizioHome;
import it.cnr.contab.config00.latt.bulk.Insieme_laBulk;
import it.cnr.contab.config00.latt.bulk.RisultatoBulk;
import it.cnr.contab.config00.latt.bulk.RisultatoHome;
import it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk;
import it.cnr.contab.config00.pdcfin.bulk.FunzioneHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPHome;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPBulk;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_laBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_laHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoBulk;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoHome;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneHome;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoHome;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.CRUDDuplicateKeyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.DuplicateKeyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

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
		
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(uc, CNRUserContext.getEsercizio(uc)); 

		if (latt.getTi_gestione().compareTo(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE)==0 && parCnr.isCofogObbligatorio() && latt.getCd_cofog()==null)
			throw new ApplicationException("Non è possibile creare GAE di spesa senza indicare la classificazione Cofog.");	

		if ((latt.getPdgMissione()==null || latt.getPdgMissione().getCd_missione()==null) &&
			(parCnr.getFl_nuovo_pdg() || (latt.getProgetto2016()!=null && latt.getProgetto2016().getPg_progetto()!=null))) 
			throw new ApplicationException("Non è possibile creare GAE senza indicare il codice missione.");	

		if (isCommessaObbligatoria(uc,latt )) {
			String cdProgramma = null;
			if (!parCnr.getFl_nuovo_pdg() || latt.getEsercizio_inizio().compareTo(Integer.valueOf(2016))==-1) { 
				if ((latt.getModulo2015() == null ||(latt.getModulo2015() != null && latt.getModulo2015().getPg_progetto() == null)))
					throw new ApplicationException( "La Commessa sul GAE non può essere nulla.");
				if (latt!=null && latt.getModulo2015()!=null && 
					latt.getModulo2015().getProgettopadre()!=null && latt.getModulo2015().getProgettopadre().getProgettopadre()!=null)
					cdProgramma = latt.getModulo2015().getProgettopadre().getProgettopadre().getCd_programma();
			}
			if (parCnr.getFl_nuovo_pdg()) {
				if (latt.getProgetto2016() == null ||(latt.getProgetto2016() != null && latt.getProgetto2016().getPg_progetto() == null))
					throw new ApplicationException( "Il Progetto sul GAE non può essere nullo. " );
				if (latt!=null && latt.getProgetto2016()!=null && latt.getProgetto2016().getProgettopadre()!=null)
					if (cdProgramma != null && !cdProgramma.equals(latt.getProgetto2016().getProgettopadre().getCd_programma()))
						throw new ApplicationException( "Il Codice Dipartimento del Modulo di attività ("+cdProgramma+") non può essere differente da quello del Progetto ("+latt.getProgetto2016().getProgettopadre().getCd_programma()+")." );
					cdProgramma = latt.getProgetto2016().getProgettopadre().getCd_programma();
			}
			if (cdProgramma!=null) {
				if (latt.getPdgProgramma()!=null && !cdProgramma.equals(latt.getPdgProgramma().getCd_programma()))
					throw new ApplicationException( "Il Codice Dipartimento del Modulo di attività e del Progetto ("+cdProgramma+") risulta differente dal Codice Programma indicato sulla "+
							" Linea di attività ("+latt.getPdgProgramma().getCd_programma()+")." );
				latt.setPdgProgramma((Pdg_programmaBulk)findByPrimaryKey(uc, new Pdg_programmaBulk(cdProgramma)));
			}
		}				

		if(latt.getTipo_linea_attivita() == null || latt.getTipo_linea_attivita().getCd_tipo_linea_attivita() == null) {
			latt.setTipo_linea_attivita((Tipo_linea_attivitaBulk)getHome(uc,Tipo_linea_attivitaBulk.class).findByPrimaryKey(new Tipo_linea_attivitaBulk("PROP")));
		}
		if ( latt.getEsercizio_fine() == null || latt.getEsercizio_fine().compareTo( latt.getCentro_responsabilita().getEsercizio_fine()) > 0 )
			latt.setEsercizio_fine( latt.getCentro_responsabilita().getEsercizio_fine());
		validaFunzione(uc,latt);
		validaNaturaPerInsieme(uc,latt);

		ProgettoBulk modulo2015=null,progetto2016=null;
		if (latt.getModulo2015()!=null && latt.getModulo2015().getPg_progetto()!=null)
			modulo2015=latt.getModulo2015();
		if (latt.getProgetto2016()!=null && latt.getProgetto2016().getPg_progetto()!=null) {
			progetto2016 = latt.getProgetto2016();
		}
		latt = (WorkpackageBulk)super.creaConBulk( uc, bulk );
		if (modulo2015!=null) {
			Ass_linea_attivita_esercizioBulk assGaeEsercizio2015 = new Ass_linea_attivita_esercizioBulk(latt.getEsercizio_inizio(),latt.getCd_centro_responsabilita(), latt.getCd_linea_attivita());
			assGaeEsercizio2015.setProgetto(modulo2015);
			assGaeEsercizio2015.setEsercizio_fine(Integer.valueOf(2015));
			assGaeEsercizio2015.setToBeCreated();
			makeBulkPersistent(uc, assGaeEsercizio2015);
			latt.setModulo2015(assGaeEsercizio2015.getProgetto());
		} 
		if (progetto2016!=null) {
			Ass_linea_attivita_esercizioBulk assGaeEsercizio2016 = new Ass_linea_attivita_esercizioBulk(CNRUserContext.getEsercizio(uc),latt.getCd_centro_responsabilita(), latt.getCd_linea_attivita());
			assGaeEsercizio2016.setProgetto(progetto2016);
			assGaeEsercizio2016.setEsercizio_fine(latt.getEsercizio_fine());
			assGaeEsercizio2016.setToBeCreated();
			makeBulkPersistent(uc, assGaeEsercizio2016);
			latt.setProgetto2016(assGaeEsercizio2016.getProgetto());
		}
		return latt;
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
public void initializeKeysAndOptionsInto(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
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

		WorkpackageHome testataHome = (WorkpackageHome)getHome(userContext, WorkpackageBulk.class);
		it.cnr.jada.bulk.BulkList<Ass_linea_attivita_esercizioBulk> assGaeEsercizioList = new it.cnr.jada.bulk.BulkList(testataHome.findDettagliEsercizio((WorkpackageBulk)bulk));
		for (Iterator i = assGaeEsercizioList.iterator(); i.hasNext();) {
			Ass_linea_attivita_esercizioBulk assGaeEsercizio = (Ass_linea_attivita_esercizioBulk) i.next();
			assGaeEsercizio.setToBeDeleted();
			makeBulkPersistent( userContext,assGaeEsercizio );
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
		ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
		Progetto_other_fieldHome progetto_other_fieldHome = (Progetto_other_fieldHome)getHome(userContext, Progetto_other_fieldBulk.class);

		it.cnr.jada.bulk.BulkList<Ass_linea_attivita_esercizioBulk> assGaeEsercizioList = new it.cnr.jada.bulk.BulkList(testataHome.findDettagliEsercizio(aLA));
		for (Iterator i = assGaeEsercizioList.iterator(); i.hasNext();) {
			Ass_linea_attivita_esercizioBulk assGaeEsercizio = (Ass_linea_attivita_esercizioBulk) i.next();
			if (assGaeEsercizio.getEsercizio().compareTo(new Integer(2016))==-1) {
				int annoProgetto = CNRUserContext.getEsercizio(userContext).compareTo(new Integer(2016))!=-1?new Integer(2015):CNRUserContext.getEsercizio(userContext);
				aLA.setModulo2015((ProgettoBulk)progettoHome.findByPrimaryKey(new ProgettoBulk(annoProgetto, assGaeEsercizio.getPg_progetto(), ProgettoBulk.TIPO_FASE_NON_DEFINITA)));
				if (aLA.getModulo2015()==null)
					throw new ApplicationException("Attenzione! E'' stato indicato sulla linea di attivita'' un progetto (" + assGaeEsercizio.getPg_progetto() + ") inesistente.");					
				//Aggiorno l'anno anche sul progetto padre
				if (CNRUserContext.getEsercizio(userContext).compareTo(new Integer(2016))!=-1 && aLA.getModulo2015().getEsercizio_progetto_padre()==null )
					aLA.getModulo2015().setEsercizio_progetto_padre(annoProgetto);
			} else { 
				int annoProgetto = CNRUserContext.getEsercizio(userContext).compareTo(new Integer(2016))==-1?new Integer(2016):CNRUserContext.getEsercizio(userContext);
				aLA.setProgetto2016((ProgettoBulk)progettoHome.findByPrimaryKey(new ProgettoBulk(annoProgetto, assGaeEsercizio.getPg_progetto(), ProgettoBulk.TIPO_FASE_NON_DEFINITA)));
				if (aLA.getProgetto2016()!=null && aLA.getProgetto2016().getPg_progetto()!=null)
					aLA.getProgetto2016().setOtherField((Progetto_other_fieldBulk)progetto_other_fieldHome.findByPrimaryKey(new Progetto_other_fieldBulk(aLA.getProgetto2016().getPg_progetto())));
				if (aLA.getProgetto2016()==null)
					throw new ApplicationException("Attenzione! E'' stato indicato sulla linea di attivita'' un progetto (" + assGaeEsercizio.getPg_progetto() + ") inesistente.");					
				//Aggiorno l'anno anche sul progetto padre
				if (CNRUserContext.getEsercizio(userContext).compareTo(new Integer(2016))==-1 && aLA.getProgetto2016().getEsercizio_progetto_padre()==null )
					aLA.getProgetto2016().setEsercizio_progetto_padre(annoProgetto);
			}
		}
		
		//Verifico se è stata utilizzata nel 2015 e/o nel 2016
		//Rospuc i controlli effettuati in isGaeUtilizzata sono insufficienti, in accordo con l'ufficio competente non sono più modificabili 
		if (aLA.getModulo2015()!=null)
			aLA.setUtilizzata2015(true);
		if (aLA.getProgetto2016()!=null)
			aLA.setUtilizzata2016(true);
		
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
		String cdNaturaReimpiego = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
		if (WorkpackageBulk.TI_GESTIONE_SPESE.equals(linea_attivita.getTi_gestione()) &&
			linea_attivita.getInsieme_la() != null &&
			linea_attivita.getCentro_responsabilita() != null &&
			Optional.ofNullable(linea_attivita.getCd_natura()).map(el->!el.equals(cdNaturaReimpiego)).orElse(Boolean.TRUE)) {
			NaturaHome home = (NaturaHome)getHome(userContext,NaturaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addTableToHeader("LINEA_ATTIVITA");
			sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","LINEA_ATTIVITA.CD_INSIEME_LA",SQLBuilder.EQUALS,linea_attivita.getCd_insieme_la());
			sql.addSQLClause("AND","LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,linea_attivita.getCd_centro_responsabilita());
			sql.addSQLClause("AND","LINEA_ATTIVITA.TI_GESTIONE",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRATE);
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
			if (linea_attivita.TI_GESTIONE_SPESE.equals(linea_attivita.getTi_gestione()))
			  sql.addSQLClause("AND","FL_SPESA",sql.EQUALS,"Y");
			else if (linea_attivita.TI_GESTIONE_ENTRATE.equals(linea_attivita.getTi_gestione()))
			  sql.addSQLClause("AND","FL_ENTRATA",sql.EQUALS,"Y"); 
			else if (linea_attivita.getTi_gestione().equals(linea_attivita.TI_GESTIONE_ENTRAMBE)) {
			  sql.addSQLClause("AND","FL_SPESA",sql.EQUALS,"Y");
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
	try{
		WorkpackageBulk linea_attivita = (WorkpackageBulk)bulk;
	
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,linea_attivita))
			throw new ApplicationException("Non è possibile modificare GAE con esercizio di fine validità chiuso.");
		
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)); 

		if (linea_attivita.getTi_gestione().compareTo(Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE)==0 && parCnr.isCofogObbligatorio() && linea_attivita.getCd_cofog()==null)
			throw new ApplicationException("Non è possibile modificare GAE di spesa senza indicare la classificazione Cofog.");

		if ((linea_attivita.getPdgMissione()==null || linea_attivita.getPdgMissione().getCd_missione()==null) &&
			(parCnr.getFl_nuovo_pdg() || (linea_attivita.getProgetto2016()!=null && linea_attivita.getProgetto2016().getPg_progetto()!=null))) 
			throw new ApplicationException("Non è possibile modificare GAE senza indicare il codice missione.");	

		if (isCommessaObbligatoria(userContext,linea_attivita )) {
			if (!parCnr.getFl_nuovo_pdg() || linea_attivita.getEsercizio_inizio().compareTo(Integer.valueOf(2016))==-1) { 
				if ((linea_attivita.getModulo2015() == null ||(linea_attivita.getModulo2015() != null && linea_attivita.getModulo2015().getPg_progetto() == null)))
					throw new ApplicationException( "La Commessa sul GAE non può essere nulla. " );
			}
			if (parCnr.getFl_nuovo_pdg() && (linea_attivita.getProgetto2016() == null ||(linea_attivita.getProgetto2016() != null && linea_attivita.getProgetto2016().getPg_progetto() == null)))
				throw new ApplicationException( "Il Progetto sul GAE non può essere nullo. " );
		}				

		aggiornaEsercizioFine(userContext, linea_attivita);
		validaFunzione(userContext,linea_attivita);
		validaModificaInsieme(userContext,linea_attivita);
		validaNaturaPerInsieme(userContext,linea_attivita);
		validaModificaFunzioneNatura(userContext,linea_attivita);

		WorkpackageHome testataHome = (WorkpackageHome)getHome(userContext, WorkpackageBulk.class);
		it.cnr.jada.bulk.BulkList<Ass_linea_attivita_esercizioBulk> assGaeEsercizioList = new it.cnr.jada.bulk.BulkList(testataHome.findDettagliEsercizio(linea_attivita));
		Ass_linea_attivita_esercizioBulk assGaeEsercizio2015 = null, assGaeEsercizio2016 = null;
		for (Iterator i = assGaeEsercizioList.iterator(); i.hasNext();) {
			Ass_linea_attivita_esercizioBulk assGaeEsercizio = (Ass_linea_attivita_esercizioBulk) i.next();
			if (assGaeEsercizio.getEsercizio().compareTo(new Integer(2016))==-1) {
				assGaeEsercizio2015 = assGaeEsercizio;
				if (linea_attivita.getModulo2015()==null || linea_attivita.getModulo2015().getPg_progetto()==null)
					assGaeEsercizio2015.setToBeDeleted();
				else if (!linea_attivita.getModulo2015().getPg_progetto().equals(assGaeEsercizio.getProgetto().getPg_progetto())) {
					if (isGaeUtilizzata(userContext,linea_attivita,true)) 
						throw new ApplicationException( "Il Modulo di attività non può essere modificato in quanto la GAE risulta già utilizzata." );
					assGaeEsercizio2015.setProgetto(linea_attivita.getModulo2015());
					assGaeEsercizio2015.setToBeUpdated();
				}
			} else {
				assGaeEsercizio2016 = assGaeEsercizio;
				if (linea_attivita.getProgetto2016()==null || linea_attivita.getProgetto2016().getPg_progetto()==null ||
					linea_attivita.getEsercizio_fine().compareTo(assGaeEsercizio2016.getEsercizio())<0) {
					if (isGaeUtilizzata(userContext,linea_attivita,false)) 
						throw new ApplicationException( "Il Progetto non può essere eliminato in quanto la GAE risulta già utilizzata." );
					assGaeEsercizio2016.setToBeDeleted();
				} else if (linea_attivita.getProgetto2016().getPg_progetto().equals(assGaeEsercizio.getProgetto().getPg_progetto()) &&
						   linea_attivita.getEsercizio_fine().compareTo(assGaeEsercizio.getEsercizio_fine())!=0) {
					assGaeEsercizio2016.setEsercizio_fine(linea_attivita.getEsercizio_fine());
					assGaeEsercizio2016.setToBeUpdated();
				} else if (!linea_attivita.getProgetto2016().getPg_progetto().equals(assGaeEsercizio.getProgetto().getPg_progetto()) ||
						   !linea_attivita.getEsercizio_fine().equals(assGaeEsercizio.getEsercizio_fine())) {
					if (isGaeUtilizzata(userContext,linea_attivita,false)) 
						throw new ApplicationException( "Il Progetto non può essere modificato in quanto la GAE risulta già utilizzata." );
					assGaeEsercizio2016.setProgetto(linea_attivita.getProgetto2016());
					assGaeEsercizio2016.setEsercizio_fine(linea_attivita.getEsercizio_fine());
					assGaeEsercizio2016.setToBeUpdated();
				}
			}
		}
		if (assGaeEsercizio2015==null && linea_attivita.getModulo2015()!=null && linea_attivita.getModulo2015().getPg_progetto()!=null){
			assGaeEsercizio2015 = new Ass_linea_attivita_esercizioBulk(linea_attivita.getEsercizio_inizio(),linea_attivita.getCd_centro_responsabilita(), linea_attivita.getCd_linea_attivita());
			assGaeEsercizio2015.setProgetto(linea_attivita.getModulo2015());
			assGaeEsercizio2015.setEsercizio_fine(Integer.valueOf(2015));
			assGaeEsercizio2015.setToBeCreated();
		}
		if (assGaeEsercizio2016==null && linea_attivita.getProgetto2016()!=null && linea_attivita.getProgetto2016().getPg_progetto()!=null){
			assGaeEsercizio2016 = new Ass_linea_attivita_esercizioBulk(Integer.valueOf(2016),linea_attivita.getCd_centro_responsabilita(), linea_attivita.getCd_linea_attivita());
			assGaeEsercizio2016.setProgetto(linea_attivita.getProgetto2016());
			assGaeEsercizio2016.setEsercizio_fine(linea_attivita.getEsercizio_fine());
			assGaeEsercizio2016.setToBeCreated();
		}
		linea_attivita = (WorkpackageBulk)super.modificaConBulk( userContext, linea_attivita);
		if (assGaeEsercizio2015!=null &&
				(assGaeEsercizio2015.isToBeCreated() || assGaeEsercizio2015.isToBeUpdated() || assGaeEsercizio2015.isToBeDeleted())) {
			if (!assGaeEsercizio2015.isToBeDeleted())
				linea_attivita.setModulo2015(assGaeEsercizio2015.getProgetto());
			makeBulkPersistent(userContext, assGaeEsercizio2015);
		}
		if (assGaeEsercizio2016!=null && 
				(assGaeEsercizio2016.isToBeCreated() || assGaeEsercizio2016.isToBeUpdated() || assGaeEsercizio2016.isToBeDeleted())) {
			if (!assGaeEsercizio2016.isToBeDeleted())
				linea_attivita.setModulo2015(assGaeEsercizio2016.getProgetto());
			makeBulkPersistent(userContext, assGaeEsercizio2016);
		}
		return linea_attivita;
	} catch(PersistencyException e) {
		throw handleException(bulk,e);
	} catch (IntrospectionException e) {
		throw handleException(bulk,e);
	} catch (RemoteException e) {
		throw handleException(bulk,e);
	} catch (SQLException e) {
		throw handleException(bulk,e);
	}
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

		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);

		WorkpackageBulk linea = (WorkpackageBulk)bulk; 
		if (esercizio.compareTo(Integer.valueOf(2016))==-1) {
			if (linea.getModulo2015()!=null && linea.getModulo2015().getPg_progetto()!=null)
				sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_PROGETTO.PG_PROGETTO",SQLBuilder.EQUALS,linea.getModulo2015().getPg_progetto());
			if (linea.getProgetto2016()!=null && linea.getProgetto2016().getPg_progetto()!=null) {
				Ass_linea_attivita_esercizioHome homeAss = (Ass_linea_attivita_esercizioHome)getHome(userContext, Ass_linea_attivita_esercizioBulk.class);	
				SQLBuilder sqlExists = homeAss.createSQLBuilder();
				sqlExists.addSQLJoin("ASS_LINEA_ATTIVITA_ESERCIZIO.CD_LINEA_ATTIVITA", "V_LINEA_ATTIVITA_PROGETTO.CD_LINEA_ATTIVITA");
				sqlExists.addSQLJoin("ASS_LINEA_ATTIVITA_ESERCIZIO.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_PROGETTO.CD_CENTRO_RESPONSABILITA");
				sqlExists.addSQLClause("AND", "ASS_LINEA_ATTIVITA_ESERCIZIO.ESERCIZIO", SQLBuilder.EQUALS, Integer.valueOf(2016));
				sqlExists.addSQLClause("AND", "ASS_LINEA_ATTIVITA_ESERCIZIO.PG_PROGETTO", SQLBuilder.EQUALS, linea.getProgetto2016().getPg_progetto());
			}
		} else {
			if (linea.getProgetto2016()!=null && linea.getProgetto2016().getPg_progetto()!=null)
				sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_PROGETTO.PG_PROGETTO",SQLBuilder.EQUALS,linea.getProgetto2016().getPg_progetto());
			if (linea.getModulo2015()!=null && linea.getModulo2015().getPg_progetto()!=null) {
				Ass_linea_attivita_esercizioHome homeAss = (Ass_linea_attivita_esercizioHome)getHome(userContext, Ass_linea_attivita_esercizioBulk.class);	
				SQLBuilder sqlExists = homeAss.createSQLBuilder();
				sqlExists.addSQLJoin("ASS_LINEA_ATTIVITA_ESERCIZIO.ESERCIZIO", "V_LINEA_ATTIVITA_PROGETTO.ESERCIZIO_INIZIO");
				sqlExists.addSQLJoin("ASS_LINEA_ATTIVITA_ESERCIZIO.CD_LINEA_ATTIVITA", "V_LINEA_ATTIVITA_PROGETTO.CD_LINEA_ATTIVITA");
				sqlExists.addSQLJoin("ASS_LINEA_ATTIVITA_ESERCIZIO.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_PROGETTO.CD_CENTRO_RESPONSABILITA");
				sqlExists.addSQLClause("AND", "ASS_LINEA_ATTIVITA_ESERCIZIO.PG_PROGETTO", SQLBuilder.EQUALS, linea.getModulo2015().getPg_progetto());
			}
		}
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
                                          CompoundFindClause clause) throws ComponentException, PersistencyException {
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
    sql.addClause( clause );
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", sql.EQUALS, linea_attivita.getPg_progetto());
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
    // Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
	  	sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));
    if (clause != null) 
        sql.addClause(clause);
    return sql;
}

public SQLBuilder selectModulo2015ByClause (UserContext userContext,
									        WorkpackageBulk linea_attivita,
									        ProgettoBulk progetto,
									        CompoundFindClause clause) throws ComponentException, PersistencyException {
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
    sql.addClause( clause );
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
    if (linea_attivita.getModulo2015()!=null)
    	sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", sql.EQUALS, linea_attivita.getModulo2015().getPg_progetto());
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
    sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
    // Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
	  	sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));
    if (linea_attivita!=null && linea_attivita.getPdgProgramma()!=null && linea_attivita.getPdgProgramma().getCd_programma()!=null) {
		sql.addTableToHeader("V_PROGETTO_PADRE","PROGETTO_DIP");
		sql.addSQLJoin("V_PROGETTO_PADRE.ESERCIZIO_PROGETTO_PADRE","PROGETTO_DIP.ESERCIZIO");
		sql.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO_PADRE","PROGETTO_DIP.PG_PROGETTO");
		sql.addSQLJoin("V_PROGETTO_PADRE.TIPO_FASE_PROGETTO_PADRE","PROGETTO_DIP.TIPO_FASE");
		sql.addSQLClause(FindClause.AND, "PROGETTO_DIP.P_CD_DIPARTIMENTO", SQLBuilder.EQUALS, linea_attivita.getPdgProgramma().getCd_programma());
	}
    if (clause != null) 
        sql.addClause(clause);
	return sql;
}

public SQLBuilder selectProgetto2016ByClause (UserContext userContext,
										      WorkpackageBulk linea_attivita,
										      ProgettoBulk progetto,
										      CompoundFindClause clause) throws ComponentException, PersistencyException {
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	sql.addClause( clause );

	Parametri_cnrBulk parCnr = null;
	try {
		parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
	} catch (RemoteException e) {
		throw handleException(linea_attivita,e);
	} 
	
	if (parCnr.getFl_nuovo_pdg())
		sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	else
		sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, Integer.valueOf(2016));
	
    if (linea_attivita.getProgetto2016()!=null)
    	sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", sql.EQUALS, linea_attivita.getProgetto2016().getPg_progetto());
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
		sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
	if (clause != null) 
		sql.addClause(clause);

	if (linea_attivita!=null && linea_attivita.getPdgProgramma()!=null && linea_attivita.getPdgProgramma().getCd_programma()!=null) {
		sql.addTableToHeader("PROGETTO","PROGETTO_DIP");
		sql.addSQLJoin("V_PROGETTO_PADRE.ESERCIZIO_PROGETTO_PADRE","PROGETTO_DIP.ESERCIZIO");
		sql.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO_PADRE","PROGETTO_DIP.PG_PROGETTO");
		sql.addSQLJoin("V_PROGETTO_PADRE.TIPO_FASE_PROGETTO_PADRE","PROGETTO_DIP.TIPO_FASE");
		sql.addSQLClause(FindClause.AND, "PROGETTO_DIP.CD_PROGRAMMA", SQLBuilder.EQUALS, linea_attivita.getPdgProgramma().getCd_programma());
	}
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

	public SQLBuilder selectPdgMissioneByClause (UserContext userContext, WorkpackageBulk linea_attivita, Pdg_missioneBulk pdgMissione, CompoundFindClause clause) throws ComponentException, PersistencyException {
		Pdg_missioneHome pdgMissionehome = (Pdg_missioneHome)getHome(userContext, Pdg_missioneBulk.class);
		SQLBuilder sql = pdgMissionehome.createSQLBuilder();

		Ass_pdg_missione_tipo_uoHome asshome = (Ass_pdg_missione_tipo_uoHome)getHome(userContext, Ass_pdg_missione_tipo_uoBulk.class);
		SQLBuilder sqlExists = asshome.createSQLBuilder();    	
		sqlExists.addSQLJoin("ASS_PDG_MISSIONE_TIPO_UO.CD_MISSIONE","PDG_MISSIONE.CD_MISSIONE");
		if (linea_attivita!=null && linea_attivita.getCentro_responsabilita()!=null &&
				linea_attivita.getCentro_responsabilita().getUnita_padre()!=null && 
						linea_attivita.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita()!=null)
			sqlExists.addSQLClause(FindClause.AND, "ASS_PDG_MISSIONE_TIPO_UO.CD_TIPO_UNITA",SQLBuilder.EQUALS,linea_attivita.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita());
		else
			sqlExists.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
			

		sql.addSQLExistsClause(FindClause.AND, sqlExists);

		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
	
	private boolean isGaeUtilizzata(UserContext userContext, WorkpackageBulk gae, boolean ante2015) throws ComponentException, PersistencyException, SQLException {
		if (WorkpackageBulk.TI_GESTIONE_SPESE.equals(gae.getTi_gestione())) {
			//Controllo Ass_cdp_la 
			Ass_cdp_laHome homeAssCdpLa =  (Ass_cdp_laHome)getHome(userContext, Ass_cdp_laBulk.class);
			SQLBuilder sqlAssCdpLa = homeAssCdpLa.createSQLBuilder();
			if (ante2015) 
				sqlAssCdpLa.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlAssCdpLa.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlAssCdpLa.addClause(FindClause.AND, "linea_attivita", SQLBuilder.EQUALS, gae);
			
			if (sqlAssCdpLa.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo Pdg_modulo_spese_gest
			Pdg_modulo_spese_gestHome homePDG =  (Pdg_modulo_spese_gestHome)getHome(userContext, Pdg_modulo_spese_gestBulk.class);
			SQLBuilder sqlPDG = homePDG.createSQLBuilder();
			if (ante2015)
				sqlPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlPDG.addClause(FindClause.AND, "linea_attivita", SQLBuilder.EQUALS, gae);

			if (sqlPDG.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo variazione
			Var_stanz_res_rigaHome homeVarRes =  (Var_stanz_res_rigaHome)getHome(userContext, Var_stanz_res_rigaBulk.class);
			SQLBuilder sqlVarRes = homeVarRes.createSQLBuilder();
			if (ante2015)
				sqlVarRes.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlVarRes.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlVarRes.addClause(FindClause.AND, "cd_linea_attivita", SQLBuilder.EQUALS, gae.getCd_linea_attivita());
			sqlVarRes.addClause(FindClause.AND, "cd_cdr", SQLBuilder.EQUALS, gae.getCd_centro_responsabilita());

			if (sqlVarRes.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo Pdg_variazione_riga_gest
			Pdg_variazione_riga_gestHome homeVarPDG =  (Pdg_variazione_riga_gestHome)getHome(userContext, Pdg_variazione_riga_gestBulk.class);
			SQLBuilder sqlVarPDG = homeVarPDG.createSQLBuilder();
			if (ante2015)
				sqlVarPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlVarPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlVarPDG.addClause(FindClause.AND, "linea_attivita", SQLBuilder.EQUALS, gae);

			if (sqlVarPDG.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo Var_stanz_res_riga
			Var_stanz_res_rigaHome homeVarResPDG =  (Var_stanz_res_rigaHome)getHome(userContext, Var_stanz_res_rigaBulk.class);
			SQLBuilder sqlVarResPDG = homeVarResPDG.createSQLBuilder();
			if (ante2015)
				sqlVarResPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlVarResPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlVarResPDG.addClause(FindClause.AND, "linea_di_attivita", SQLBuilder.EQUALS, gae);

			return sqlVarResPDG.executeExistsQuery(getConnection(userContext));
		} else {
			Pdg_modulo_entrate_gestHome homePDG =  (Pdg_modulo_entrate_gestHome)getHome(userContext, Pdg_modulo_entrate_gestBulk.class);
			SQLBuilder sqlPDG = homePDG.createSQLBuilder();
			if (ante2015)
				sqlPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlPDG.addClause(FindClause.AND, "linea_attivita", SQLBuilder.EQUALS, gae);

			if (sqlPDG.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo Pdg_variazione_riga_gest
			Pdg_variazione_riga_gestHome homeVarPDG =  (Pdg_variazione_riga_gestHome)getHome(userContext, Pdg_variazione_riga_gestBulk.class);
			SQLBuilder sqlVarPDG = homeVarPDG.createSQLBuilder();
			if (ante2015)
				sqlVarPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlVarPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlVarPDG.addClause(FindClause.AND, "linea_attivita", SQLBuilder.EQUALS, gae);

			if (sqlVarPDG.executeExistsQuery(getConnection(userContext)))
				return true;

			//Controllo Var_stanz_res_riga
			Var_stanz_res_rigaHome homeVarResPDG =  (Var_stanz_res_rigaHome)getHome(userContext, Var_stanz_res_rigaBulk.class);
			SQLBuilder sqlVarResPDG = homeVarResPDG.createSQLBuilder();
			if (ante2015)
				sqlVarResPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS_EQUALS, Integer.valueOf(2015));
			else
				sqlVarResPDG.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, Integer.valueOf(2016));
			sqlVarResPDG.addClause(FindClause.AND, "linea_di_attivita", SQLBuilder.EQUALS, gae);

			return sqlVarResPDG.executeExistsQuery(getConnection(userContext));
		}
	}
	
	public SQLBuilder selectVocePianoEconomico2016ByClause (UserContext userContext, WorkpackageBulk linea_attivita, Voce_piano_economico_prgBulk vocePianoEconomico, CompoundFindClause clause) throws ComponentException, PersistencyException {
		Voce_piano_economico_prgHome vocePianoHome = (Voce_piano_economico_prgHome)getHome(userContext, Voce_piano_economico_prgBulk.class);
		Integer pgProgetto=null;
 		if (linea_attivita!=null && linea_attivita.getProgetto2016()!=null && linea_attivita.getProgetto2016().getPg_progetto()!=null)
 			pgProgetto = linea_attivita.getProgetto2016().getPg_progetto();

		SQLBuilder sql = vocePianoHome.findVocePianoEconomicoPrgList(pgProgetto);

		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
}
