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

package it.cnr.contab.prevent00.comp;

import it.cnr.contab.config00.esercizio.bulk.*;
import java.io.Serializable;
import java.io.PrintWriter;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.RemoteIterator;

public class BilancioPreventivoComponent  extends it.cnr.jada.comp.CRUDComponent implements IBilancioPreventivoMgr,Cloneable,Serializable{

    public  BilancioPreventivoComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  Aggiornamento impegno sul capitolo di parte 1
  *    PreCondition:
  *      Viene richiesto di aggiornare l'impegno sul capitolo di parte 1 della parte spese bilancio CNR
  *      
  *      
  *      
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB030.aggiornaImpegnoCapitolo
  *      
 */
//^^@@
	private void aggiornaImpegnoCapitolo(UserContext userContext, Voce_f_saldi_cmpBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB030.aggiornaImpegnoCapitolo(?,?)}",false,this.getClass());
			cs.setObject( 1, aBulk.getEsercizio() );
			cs.setString( 2, aBulk.getCd_voce());
			try {
				cs.executeQuery();
			} catch (Throwable e) {
				throw handleException(aBulk,e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

//^^@@
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Viene richiesta l'approvazione del bilancio preventivo.
  *      Lo stato del bilancio preventivo è (B) predisposto o prodotto.
  *    PostCondition:
  *      Viene invocato il controllo di pareggio di bilancio mandatorio (richiesta CINECA/CNR del 04/11/2002)
  *      Lo stato del bilancio preventivo si cambia da 'B' a 'C' approvato.
  *  Stato bilancio preventivo NON è B
  *    PreCondition:
  *      Lo stato del bilancio preventivo NON è B.
  *    PostCondition:
  *      Operazione non consentita.
  *      Throw exception: Lo stato del bilancio preventivo non consente l'approvazione.
 */
//^^@@
public Bilancio_preventivoBulk approvaBilancioPreventivo (UserContext aUC,Bilancio_preventivoBulk bilancio) throws ComponentException
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(aUC))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile approvare il bilancio preventivo ad esercizio chiuso.");

    // FIX 18/01/2002
    // Rilettura loockante del piano per verificare che nessuno l'abbia modificato dall'apertura della finestra del bilancio testata
    try {
     lockBulk(aUC,bilancio);
    } catch(Throwable e) {
		throw handleException(bilancio, e);
	}
    	
	try 
	{
        if(!bilancio.getStato().equals(bilancio.STATO_B))
         throw new it.cnr.jada.comp.ApplicationException("Lo stato del bilancio preventivo non consente l'approvazione.");

        checkSpareggioBilancioApprovazione(aUC, bilancio);
 	
		bilancio.setUser(aUC.getUser());
		bilancio.setStato(bilancio.STATO_C);
		bilancio.setToBeUpdated();
		bilancio.setDt_approvazione(getHome(aUC, bilancio.getClass()).getServerTimestamp());
		
		makeBulkPersistent(aUC,bilancio);

		return(caricaBilancioPreventivo(aUC,bilancio));
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}	
}
/** 
  *  normale
  *    PreCondition:
  *      Ricerca del Bilancio di previsione 
  *		 (esercizio di scrivania, cds di scrivania, appartenenza C/D)
  *    PostCondition:
  *      Se il bilancio non esiste Exception
 */

public Bilancio_preventivoBulk caricaBilancioPreventivo(UserContext aUC,Bilancio_preventivoBulk bulk) throws ComponentException
{
	Bilancio_preventivoBulk bilancioPrev;

	try
	{
		bilancioPrev = (Bilancio_preventivoBulk)getHome(aUC, bulk).findByPrimaryKey(bulk);
	}
	catch (it.cnr.jada.persistency.PersistencyException e) 
	{
		throw new it.cnr.jada.comp.ComponentException(e);
	}				
	return (bilancioPrev);
}
//^^@@
/** 
  *  
  *    PreCondition:
  *      
  *    PostCondition:
  *      
 */
//^^@@

private void cbSalvDett(UserContext userContext, Voce_f_saldi_cmpBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
 
		try {			
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"CNRCTB055.postsalvdett(?,?,?,?,?,?,?)}",false,this.getClass());
			cs.setObject( 1, aBulk.getEsercizio());
			cs.setString( 2, aBulk.getCd_cds());
			cs.setString( 3, aBulk.getTi_appartenenza());
			cs.setString( 4, aBulk.getTi_gestione());
			cs.setString( 5, aBulk.getCd_voce());
			cs.setString( 6, aBulk.getTi_competenza_residuo());
			cs.setString( 7, aBulk.getUser());
			try {
				cs.executeQuery();
			} catch (Throwable e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {			
			throw handleSQLException(e);
		}
	}

/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesta la ricerca del cd_cds ENTE che servira' per 
  *		 inizializzare il cd_cds del bilancio di previsione CNR
  *    PostCondition:
  *      ritorno il risultato della ricerca nella tabella unita_organizzativa 
  *		 dell'unita' con cd_tipo_unita=ENTE
 */

public OggettoBulk cercaCdsEnte(UserContext aUC,Bilancio_preventivoBulk bilancio) throws ComponentException
{
	try
	{
		Bilancio_preventivoHome bilancioHome = (Bilancio_preventivoHome) getHome( aUC, bilancio.getClass());
		java.util.List list =  bilancioHome.cercaCdsEnte( bilancio );
		EnteBulk ente=null;
		for (java.util.Iterator i = list.iterator(); i.hasNext();) 
	   	{
        	ente = (EnteBulk) i.next();
    	}		
		return(ente);
	}
	catch ( Exception e )
	{
		throw handleException(e);
	} 
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesta la ricerca del cd_cds ENTE che servira' per 
  *		 inizializzare il cd_cds dei dettagli di spesa/entrata
  *		 del bilancio di previsione CNR
  *    PostCondition:
  *      ritorno il risultato della ricerca nella tabella unita_organizzativa 
  *		 dell'unita' con cd_tipo_unita=ENTE
 */

public OggettoBulk cercaCdsEnte(UserContext aUC, Voce_f_saldi_cmpBulk dettaglio) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpHome dettaglioHome = (Voce_f_saldi_cmpHome) getHome( aUC, dettaglio.getClass());
		java.util.List list =  dettaglioHome.cercaCdsEnte( dettaglio );
		EnteBulk ente=null;
		for (java.util.Iterator i = list.iterator(); i.hasNext();) 
	   	{
        	ente = (EnteBulk) i.next();
    	}		
		return(ente);
	}
	catch ( Exception e )
	{
		throw handleException(e);
	} 
}
//^^@@
/** 
  *  Controllo di non sfondamento previsioni CNR in modifica previsioni di entrata CDS
  *    PreCondition:
  *      Viene richiesto il controllo di non sfondamento previsioni CNR in modifica previsioni di entrata CDS
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB055.checkEntrataBilFinCdsCnr
 */
//^^@@

private java.math.BigDecimal checkEntrataBilFinCdsCnr(UserContext userContext, Voce_f_saldi_cmpBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
        java.math.BigDecimal aDelta = new java.math.BigDecimal(0);
		try {			
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB055.checkEntrataBilFinCdsCnr(?,?,?,?,?)}",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.DECIMAL );
			cs.setObject( 2, aBulk.getEsercizio());
			cs.setString( 3, aBulk.getCd_cds());
			cs.setString( 4, aBulk.getTi_appartenenza());
			cs.setString( 5, aBulk.getTi_gestione());
			cs.setString( 6, aBulk.getCd_voce());
			try {
				cs.executeQuery();
			    aDelta = cs.getBigDecimal(1);		
			} catch (Throwable e) {
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
		return aDelta;
	}

//^^@@
/** 
  *  Controllo di non sfondamento del 3% del fondo di riserva CDS
  *    PreCondition:
  *      Viene richiesto il controllo di non sfondamento del 35 del fondo di riserva CDS
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB055.checkFondoRiserva
 */
//^^@@

private java.math.BigDecimal checkFondoRiserva(UserContext userContext, Integer aEs, String aCdCds, String aTiAppartenenza) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
        java.math.BigDecimal aDelta = new java.math.BigDecimal(0);
		try {			
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB055.checkFondoRiserva(?,?,?)}",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.DECIMAL );
			cs.setObject( 2, aEs);
			cs.setString( 3, aCdCds);
			cs.setString( 4, aTiAppartenenza);
			try {
				cs.executeQuery();
			    aDelta = cs.getBigDecimal(1);		
			} catch (Throwable e) {
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
		return aDelta;
	}

//^^@@
/** 
  *  Controllo pareggio di bilancio
  *    PreCondition:
  *      Viene richiesto il controllo di pareggio di bilancio
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB055.checkSpareggioBilancio
 */
//^^@@
	private java.math.BigDecimal checkSpareggioBilancio(UserContext userContext, Voce_f_saldi_cmpBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
        java.math.BigDecimal aDelta = new java.math.BigDecimal(0);
		try {			
			LoggableStatement cs = new LoggableStatement(getConnection(userContext),
					"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"CNRCTB055.checkSpareggioBilancio(?,?,?)}",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.DECIMAL );
			cs.setObject( 2, aBulk.getEsercizio());
			cs.setString( 3, aBulk.getCd_cds());
			cs.setString( 4, aBulk.getTi_appartenenza());
			try {
				cs.executeQuery();
			    aDelta = cs.getBigDecimal(1);		
			} catch (Throwable e) {
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
		return aDelta;
	}

//^^@@
/** 
  *  Controllo pareggio di bilancio all'approvazione
  *    PreCondition:
  *      Viene richiesto il controllo di pareggio di bilancio
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB055.checkSpareggioBilancio
 */
//^^@@
	private java.math.BigDecimal checkSpareggioBilancioApprovazione(UserContext userContext, Bilancio_preventivoBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
        java.math.BigDecimal aDelta = new java.math.BigDecimal(0);
		try {			
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"CNRCTB055.checkSpareggioBilancio(?,?,?,?)}",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.DECIMAL );
			cs.setObject( 2, aBulk.getEsercizio());
			cs.setString( 3, aBulk.getCd_cds());
			cs.setString( 4, aBulk.getTi_appartenenza());
			cs.setString( 5, "Y"); // forza il controllo di pareggio del bilancio
			try {
				cs.executeQuery();
			    aDelta = cs.getBigDecimal(1);		
			} catch (Throwable e) {
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
		return aDelta;
	}

//^^@@
/** 
  *  Controllo di non sfondamento previsioni CNR in modifica previsioni di spesa CDS
  *    PreCondition:
  *      Viene richiesto il controllo di non sfondamento previsioni CNR in modifica previsioni di spesa CDS
  *    PostCondition:
  *      Viene lanciata la stored procedure CNRCTB055.checkSpeseBilFinCdsCnr
 */
//^^@@

private java.math.BigDecimal checkSpeseBilFinCdsCnr(UserContext userContext, Voce_f_saldi_cmpBulk aBulk) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
        java.math.BigDecimal aDelta = new java.math.BigDecimal(0);
		try {			
			LoggableStatement cs =new LoggableStatement(getConnection( userContext ),
					"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"CNRCTB055.checkSpeseBilFinCdsCnr(?,?,?)}",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.DECIMAL );
			cs.setObject( 2, aBulk.getEsercizio());
			cs.setString( 3, aBulk.getCd_cds());
			cs.setString( 4, aBulk.getTi_appartenenza());
			try {
				cs.executeQuery();
			    aDelta = cs.getBigDecimal(1);		
			} catch (Throwable e) {
				throw handleException(e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
		return aDelta;
	}

/**
  *  Richiesta creazione di dettaglio in bilancio finaniziario CNR ed esiste già il dettaglio per la parte residui
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CNR.
  *      Esiste per tale dettaglio la specifica di dettaglio della parte residui
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Creazione di dettaglio in bilancio finaniziario CNR
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CNR.
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene creato un nuovo dettaglio
  *      Viene creato un record relativo alla parte residui
  *      Se il dettaglio è di spesa viene aggiornato l'impegno automatico colegato al capitolo
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio mantiene entrate > spese per il preventivo CDS
  *    PostCondition:
  *      Viene creato un nuovo dettaglio e segnalato lo spareggio all'utente
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio spese > entrate
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese > entrate per il preventivo CDS
  *    PostCondition:
  *      Viene Sollevata un'eccezione
  *
  *  Creazione di dettaglio di spesa in bilancio finaniziario CDS con sfondamento del limite del 3% del fondo di riserva
  *    PreCondition:
  *      Viene richiesta la creazione di un dettaglio di spesa del bilancio preventivo CDS.
  *      Dopo la modifica il totale del funzionamento supera il 3% del fondo di riserva
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con entrata CDS > delle spese corrispondenti in bilancio CNR
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese CNR corrispondenti entrata CDS < entrata CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  */
	public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare dettagli ad esercizio chiuso.");

		OggettoBulk aBulk = super.creaConBulk(userContext,bulk);
        String aMessage="Operazione completata";
		
		
    	try {

	    	if( bulk instanceof Voce_f_saldi_cmpBulk
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cds)
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) 
			) {
             if(((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE)) {
  			  checkFondoRiserva(
	  			       userContext, 
	  			       ((Voce_f_saldi_cmpBulk)bulk).getEsercizio(),
	  			       ((Voce_f_saldi_cmpBulk)bulk).getCd_cds(), 
	  			       ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza());
	          checkSpeseBilFinCdsCnr(userContext, (Voce_f_saldi_cmpBulk)bulk);   
	         }
             if(((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE)) {
	          java.math.BigDecimal aDeltaE = checkEntrataBilFinCdsCnr(userContext, (Voce_f_saldi_cmpBulk)bulk);   
              if(aDeltaE.compareTo(new java.math.BigDecimal(0)) == -1) {
	           it.cnr.contab.util.EuroFormat aEFE = new it.cnr.contab.util.EuroFormat();
	           String aValE = aEFE.format(aDeltaE);
 			   aMessage += "\r\nEsiste una differenza tra entrate CDS e spese CNR corrispondenti pari a: "+aValE;
              }
	         }
			 java.math.BigDecimal aDelta = checkSpareggioBilancio(userContext, (Voce_f_saldi_cmpBulk)bulk);
             if(aDelta.compareTo(new java.math.BigDecimal(0)) == 1) {
	          it.cnr.contab.util.EuroFormat aEF = new it.cnr.contab.util.EuroFormat();
	          String aVal = aEF.format(aDelta);
 			  aMessage += "\r\nEsiste una differenza tra entrate e spese pari a: "+aVal;
             }
			}

			if( bulk instanceof Voce_f_saldi_cmpBulk
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cnr)
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) 
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE)
              )
			{
		      aggiornaImpegnoCapitolo(userContext, (Voce_f_saldi_cmpBulk)bulk);
		    }
		

			if( bulk instanceof Voce_f_saldi_cmpBulk
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cnr)
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) )
			{
				Voce_f_saldi_cmp_resBulk residui = new Voce_f_saldi_cmp_resBulk(
														((Voce_f_saldi_cmpBulk)bulk).getCd_cds(),
														((Voce_f_saldi_cmpBulk)bulk).getCd_voce(),
														((Voce_f_saldi_cmpBulk)bulk).getEsercizio(),
														((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza(),
														Voce_f_saldi_cmp_resBulk.TIPO_RESIDUO,
														((Voce_f_saldi_cmpBulk)bulk).getTi_gestione()
													);
				residui.initializzaAttributiPerInsert((Voce_f_saldi_cmpBulk)bulk, userContext);
				residui.setUser( bulk.getUser() );
				try {
					insertBulk(userContext,residui);
				} catch(it.cnr.jada.persistency.sql.DuplicateKeyException dke) {
					throw new it.cnr.jada.comp.ApplicationException("Impossibile creare un record di tipo \"residuo\", record già esistente!");
				}

				((Voce_f_saldi_cmpBulk)bulk).setResidui( residui );
			}

			// Rich: 661 - Borriello
			// Richiama la Procedura per l'aggiornamento eventuale di avanzo/disavanzo
			if (bulk instanceof Voce_f_saldi_cmpBulk){
				cbSalvDett(userContext, (Voce_f_saldi_cmpBulk)bulk);
			}

		} catch (Throwable e) {
			throw handleException(bulk,e);
		}

	    return asMTU(aBulk,aMessage);
	}

//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta la creazione degli stanziamenti iniziali del bilancio preventivo di un singolo CdS.
  *    PostCondition:
  *      La creazione degli stanziamenti iniziali del bilancio preventivo di un singolo CdS richiede l'aggregazione per 'Rubrica' dei PdG che appartengono al CdS. Il risultato di questa aggregazione (prodotta/specificata nel metodo aggregaPdGPerRubrica) viene utilizzato per la scrittura degli saldi corrispondenti nella tabella VOCE_F_SALDI_CMP. La procedura ORACLE predisponeBilFinCDS(esercizio, cds, utente) esegue quest'attività.
 */
//^^@@
public void creaStanziamentiInizialiCdS (UserContext aUC,CdsBulk cds) throws ComponentException
        {
            return ;
        }
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta la creazione degli stanziamenti iniziali del bilancio preventivo del CNR.
  *    PostCondition:
  *      La creazione degli stanziamenti iniziali del bilancio preventivo del CNR richiede la lettura delle righe delle tabelle PDG_AGGREGATO* e la scrittura degli saldi corrispondenti nella tabella VOCE_F_SALDI_CMP. La procedura ORACLE predisponeBilFinCNR(esercizio, utente) esegue quest'attività.
 */
//^^@@
public void creaStanziamentiInizialiCNR (UserContext aUC,short esercizio) throws ComponentException
        {
            return ;
        }
/**
     Controparte residui non trovata per dettaglio di competenza CNR in modifica
  *    PreCondition:
  *      Dettaglio di parte residui non trovato in corrispondenza del dettaglio CNR di competenza in modifica
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Richiesta di inizializzazione del dettaglio CNR per modifica
  *    PreCondition:
  *      Viene richiesta l'inizializzazione di un dettaglio del bilancio finanziario CNR
  *    PostCondition:
  *      Viene inizializzato il dettaglio parte competenza per la modifica
  *      Viene caricato automaticamente il dettaglio residui collegato
  *
  *  Richiesta di inizializzazione del dettaglio CDS per modifica
  *    PreCondition:
  *      Viene richiesta l'inizializzazione di un dettaglio del bilancio finanziario CDS
  *    PostCondition:
  *      Viene inizializzato il dettaglio per la modifica
 */
 public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		Voce_f_saldi_cmpBulk competenze = (Voce_f_saldi_cmpBulk)super.inizializzaBulkPerModifica(userContext, bulk);

		if( bulk instanceof Voce_f_saldi_cmpBulk
			&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cnr)
			&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) )
		{
			it.cnr.jada.bulk.BulkHome home = getHome(userContext,Voce_f_saldi_cmp_resBulk.class,null,getFetchPolicyName("edit"));
			Voce_f_saldi_cmp_resBulk residui =
				(Voce_f_saldi_cmp_resBulk)home.findByPrimaryKey(
					new Voce_f_saldi_cmp_resBulk(
						competenze.getCd_cds(),
						competenze.getCd_voce(),
						competenze.getEsercizio(),
						competenze.getTi_appartenenza(),
						competenze.TIPO_RESIDUO,
						competenze.getTi_gestione()
					)
				);

			if (bulk == null)
				throw new it.cnr.jada.persistency.ObjectNotFoundException("Object not found",(it.cnr.jada.persistency.Persistent)residui);
			initializeKeysAndOptionsInto(userContext, residui);

			inizializzaSaldo1210(userContext, residui);

			if (residui.getIm_1210() == null)
			 residui.setIm_1210(new java.math.BigDecimal(0));

			competenze.setResidui(residui);

			try {
				getHomeCache(userContext).fetchAll(userContext,home);
			} catch(it.cnr.jada.persistency.ObjectNotFoundException e) {
			}

		}

		inizializzaSaldo1210(userContext, competenze);

		if (competenze.getIm_1210() == null)
			competenze.setIm_1210(new java.math.BigDecimal(0));

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext))
			return asRO(competenze,"Dettaglio non modificabile ad esercizio chiuso.");

		return competenze;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/** 
  *  Caricamento del saldo documenti 1210 non pagati parte residui
 */
private void inizializzaSaldo1210(UserContext userContext, Voce_f_saldi_cmp_resBulk dettaglio) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
  if(dettaglio.getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE))
   return;
  try {	
	  LoggableStatement aPS=null;
    try {
	 aPS=new LoggableStatement(getConnection(userContext),
	  "select im_voce from "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"V_LETTERA_PAGAM_ESTERO_DOC_OS where"
	  +"        ESERCIZIO=? " 
      +"    and CD_CDS=? "
      +"    and TI_APPARTENENZA=? "
      +"    and TI_GESTIONE=? "
      +"    and CD_VOCE=? " 
      +"    and COMPETENZE_RESIDUI=? ",true,this.getClass());

	 aPS.setInt(1,dettaglio.getEsercizio().intValue());
	 aPS.setString(2,dettaglio.getCd_cds());
	 aPS.setString(3,dettaglio.getTi_appartenenza());
	 aPS.setString(4,dettaglio.getTi_gestione());
	 aPS.setString(5,dettaglio.getCd_voce());
	 aPS.setString(6,dettaglio.getTi_competenza_residuo());
     java.sql.ResultSet aRS=aPS.executeQuery();
     while(aRS.next()) {
	  dettaglio.setIm_1210(aRS.getBigDecimal(1));    
	 }
     try{aRS.close();}catch( java.sql.SQLException e ){};
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	} finally {
	 if(aPS != null) 
         try{aPS.close();}catch( java.sql.SQLException e ){};
    }	
  } catch (Throwable e) {
	 throw handleException(e);
  }
}
/** 
  *  Caricamento del saldo documenti 1210 non pagati parte competenza
 */
private void inizializzaSaldo1210(UserContext userContext, Voce_f_saldi_cmpBulk dettaglio) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
  if(dettaglio.getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE))
   return;
  try {	
	  LoggableStatement aPS=null;
    try {
	 aPS=new LoggableStatement(getConnection(userContext),
	  "select im_voce from "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"V_LETTERA_PAGAM_ESTERO_DOC_OS where"
	  +"        ESERCIZIO=? " 
      +"    and CD_CDS=? "
      +"    and TI_APPARTENENZA=? "
      +"    and TI_GESTIONE=? "
      +"    and CD_VOCE=? " 
      +"    and COMPETENZE_RESIDUI=? ",true,this.getClass());

	 aPS.setInt(1,dettaglio.getEsercizio().intValue());
	 aPS.setString(2,dettaglio.getCd_cds());
	 aPS.setString(3,dettaglio.getTi_appartenenza());
	 aPS.setString(4,dettaglio.getTi_gestione());
	 aPS.setString(5,dettaglio.getCd_voce());
	 aPS.setString(6,dettaglio.getTi_competenza_residuo());
     java.sql.ResultSet aRS=aPS.executeQuery();
     while(aRS.next()) {
	  dettaglio.setIm_1210(aRS.getBigDecimal(1));    
	 }
     try{aRS.close();}catch( java.sql.SQLException e ){};
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	} finally {
	 if(aPS != null) 
       try{aPS.close();}catch( java.sql.SQLException e ){};
    }	
  } catch (Throwable e) {
	 throw handleException(e);
  }
}
protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
  *  Modifica di dettaglio in bilancio finaniziario CNR di spesa
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CNR.
  *      Il dettaglio è di spesa
  *    PostCondition:
  *      Viene aggiornato l'impegno automatico collegato al capitolo
  *      Viene aggiornato il saldo parte competenza
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio spese > entrate
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese > entrate per il preventivo CDS
  *    PostCondition:
  *      Viene Sollevata un'eccezione
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio de spesa del bilancio preventivo CDS.
  *      Il nuovo dettaglio modificato determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio di spesa in bilancio finaniziario CDS con sfondamento del limite del 3% del fondo di riserva
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CDS.
  *      Dopo la modifica il totale del funzionamento supera il 3% del fondo di riserva
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la modifica di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con entrata CDS > delle spese corrispondenti in bilancio CNR
  *    PreCondition:
  *      Viene richiesta la modifica di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese CNR corrispondenti entrata CDS < entrata CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio caso generale
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene aggiornato il saldo parte competenza
  */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare dettagli ad esercizio chiuso.");

		OggettoBulk aBulk = super.modificaConBulk(userContext,bulk);
		String aMessage = "Operazione completata";
    	try {
			if( bulk instanceof Voce_f_saldi_cmpBulk
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cds)
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) 
			) {
             if(((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE)) {
  			  checkFondoRiserva(
	  			       userContext, 
	  			       ((Voce_f_saldi_cmpBulk)bulk).getEsercizio(),
	  			       ((Voce_f_saldi_cmpBulk)bulk).getCd_cds(), 
	  			       ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza());
	          checkSpeseBilFinCdsCnr(userContext, (Voce_f_saldi_cmpBulk)bulk);   
	         }
             if(((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE)) {
	          java.math.BigDecimal aDeltaE = checkEntrataBilFinCdsCnr(userContext, (Voce_f_saldi_cmpBulk)bulk);   
              if(aDeltaE.compareTo(new java.math.BigDecimal(0)) == -1) {
	           it.cnr.contab.util.EuroFormat aEFE = new it.cnr.contab.util.EuroFormat();
	           String aValE = aEFE.format(aDeltaE);
 			   aMessage += "\r\nEsiste una differenza tra entrate CDS e spese CNR corrispondenti pari a: "+aValE;
              }
	         }
              
			 java.math.BigDecimal aDelta = checkSpareggioBilancio(userContext, (Voce_f_saldi_cmpBulk)bulk);
             if(aDelta.compareTo(new java.math.BigDecimal(0)) == 1) {
	          it.cnr.contab.util.EuroFormat aEF = new it.cnr.contab.util.EuroFormat();
	          String aVal = aEF.format(aDelta);
 			  aMessage += "\r\nEsiste una differenza tra entrate e spese pari a: "+aVal;
             }

             
			}
			if( bulk instanceof Voce_f_saldi_cmpBulk
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_appartenenza().equals(Voce_f_saldi_cmpBulk.tipo_appartenenza_cnr)
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_competenza_residuo().equals(Voce_f_saldi_cmpBulk.TIPO_COMPETENZA) 
				&& ((Voce_f_saldi_cmpBulk)bulk).getTi_gestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE)
              )
			{
		      aggiornaImpegnoCapitolo(userContext, (Voce_f_saldi_cmpBulk)bulk);
         	}
			
			// Rich: 661 - Borriello
			// Richiama la Procedura per l'aggiornamento eventuale di avanzo/disavanzo
			if (bulk instanceof Voce_f_saldi_cmpBulk){
				cbSalvDett(userContext, (Voce_f_saldi_cmpBulk)bulk);
			}			
	    } catch (Throwable e) {
			throw handleException(bulk,e);
		}
        
	    return asMTU(aBulk,aMessage);
}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la predisposizione del bilancio finanziario CDS
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB055.predisponeBilFinCDS
  *      Viene effettuato il controllo di non sfondamento del tetto del 3% del fondo di riserva
 */
//^^@@
public Bilancio_preventivoBulk predisponeBilancioPreventivoCdS (UserContext aUC, Bilancio_preventivoBulk bilancioPrev) throws ComponentException
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(aUC))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile predisporre il bilancio preventivo ad esercizio chiuso.");

	try
	{
		lockBulk(aUC, bilancioPrev);
		LoggableStatement cs = new LoggableStatement(getConnection( aUC ), 
				"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+
				"CNRCTB055.predisponeBilFinCDS(?,?,?)}",false,this.getClass());
		cs.setObject( 1, bilancioPrev.getEsercizio() );
		cs.setObject( 2, bilancioPrev.getCd_cds() );		
		cs.setObject( 3, aUC.getUser());
		cs.executeQuery();
		cs.close();

		checkFondoRiserva(aUC, bilancioPrev.getEsercizio(), bilancioPrev.getCd_cds(), bilancioPrev.getTi_appartenenza());
		
		return caricaBilancioPreventivo(aUC, bilancioPrev);
	} 
	catch (Throwable e) 
	{
		throw handleException(bilancioPrev, e);
	}
}
//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la produzione del bilancio finanziario CNR
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB055.predisponeBilFinCNR
 */
//^^@@
public Bilancio_preventivoBulk predisponeBilancioPreventivoCNR (UserContext aUC, Bilancio_preventivoBulk bilancioPrev) throws ComponentException
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(aUC))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile predisporre il bilancio preventivo ad esercizio chiuso.");

	try
	{
		lockBulk(aUC, bilancioPrev);		
		LoggableStatement cs =new LoggableStatement(getConnection( aUC ),
				"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+
				"CNRCTB055.predisponeBilFinCNR(?,?)}",false,this.getClass());
		cs.setObject( 1, bilancioPrev.getEsercizio() );
		cs.setObject( 2, aUC.getUser());
		cs.executeQuery();
		cs.close();
		return caricaBilancioPreventivo(aUC, bilancioPrev);		
	} 
	catch (Throwable e) 
	{
		throw handleException(bilancioPrev, e);
	}
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoceByClause(UserContext userContext, Voce_f_saldi_cmpBulk dettaglio, it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	it.cnr.contab.config00.pdcfin.bulk.Voce_fHome voceHome = (it.cnr.contab.config00.pdcfin.bulk.Voce_fHome) getHome(userContext, it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = voceHome.createSQLBuilder();
	sql.addClause(clauses);

	sql.addClause("AND", "ti_appartenenza", sql.EQUALS, dettaglio.getTi_appartenenza());
	sql.addClause("AND", "ti_gestione", sql.EQUALS, dettaglio.getTi_gestione());	
	sql.addClause("AND", "esercizio", sql.EQUALS, dettaglio.getEsercizio());
	sql.addClause("AND", "fl_mastrino", sql.EQUALS, new Boolean(true));


	if((dettaglio.getTi_appartenenza().equals(dettaglio.tipo_appartenenza_cds)) &&
	   (dettaglio.getTi_gestione().equals(dettaglio.tipo_gestione_spesa)))
	{
		sql.addSQLClause("AND","cd_parte = ? OR cd_cds = ?");
		sql.addParameter(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.PARTE_2, java.sql.Types.VARCHAR, 1);		
		sql.addParameter(dettaglio.getCd_cds(), java.sql.Types.VARCHAR, 1);
	}	
 	
	return sql;
}
}
