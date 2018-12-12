package it.cnr.contab.chiusura00.comp;

import it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.List;

import javax.ejb.EJBException;
	   

public class RicercaDocContComponent extends it.cnr.jada.comp.RicercaComponent implements IRicercaDocContComponent
{
	final static String PROC_NAME_ANNULLAMENTO 				= "CNRCTB047.annullaDocCont";
	final static String PROC_NAME_RIPORTA_AVANTI 			= "CNRCTB047.riportoNextEsDocCOnt";
	final static String PROC_NAME_RIPORTA_INDIETRO 			= "CNRCTB047.riportoPrevEsDocCOnt";
	final static String PROC_NAME_RIPORTA_AVANTI_EVOLUTO 	= "CNRCTB047.riportoNextEsDocContVoce";	
	
/**
 * RicercaDocContComponent constructor comment.
 */
public RicercaDocContComponent() {
	super();
}
/*
 *	Pre: nella VSX_CHIUSURA sono stati inseriti i record con le chiavi dei doc.contabili da annullare
 *	Post: viene chiamata la stored procedure di annullamento dei doc.contabili inseriti in VSX_CHIUSURA
 *
 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code>	 da utilizzare come chiave nella tabella VSX_CHIUSURA
*/
public void	callAnnullamentoDocCont(UserContext userContext, Long pg_call) throws ComponentException 
{
	LoggableStatement cs = null;
	try 
	{
		try	
		{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB047.annullamentoDocCont( ?, ? ) }",false,this.getClass());
			cs.setObject( 1, pg_call);
			cs.setObject( 2, ((CNRUserContext)userContext).getUser());			
//			cs.setObject( 2, ((CNRUserContext)userContext).getEsercizio());
//			cs.setObject( 4, it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());			
			cs.executeQuery();
		} catch (Throwable e) 
		{
			throw handleException(e);
		} finally 
		{
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
}
/*
 *	Pre: nella VSX_CHIUSURA sono stati inseriti i record con le chiavi dei doc.contabili da riportare
 *	     all'esercizio successivo
 *	Post: viene chiamata la stored procedure di riporto dei doc.contabili inseriti in VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code>	 da utilizzare come chiave nella tabella VSX_CHIUSURA
*/

public void	callRiportoNextEsDocCont(UserContext userContext, Long pg_call) throws ComponentException 
{
	LoggableStatement cs = null;	
	try 
	{
		try	
		{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB047.riportoNextEsDocCOnt( ?, ? , ?) }",false,this.getClass());
			cs.setObject( 1, pg_call);
			cs.setObject( 2, ((CNRUserContext)userContext).getUser());
			cs.setObject( 3, ((CNRUserContext)userContext).getCd_cds());															
//			cs.setObject( 2, ((CNRUserContext)userContext).getEsercizio());
//			cs.setObject( 3, ((CNRUserContext)userContext).getUser());
//			cs.setObject( 4, it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());			
			cs.executeQuery();
		} catch (Throwable e) 
		{
			throw handleException(e);
		} finally 
		{
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
}
/*
 * 	Name: riporto es successivo per CDS diverso da Ente
 * 	Pre: in scrivania è stato selezinato un Cds diverso dall'Ente
 * 		  l'utente ha specificato se vuole riportare all'es. successivo doc. contabili di entrata o di spesa
 * 		  l'utente ha specificato se vuole riportare all'es. successivo doc. contabili creati dall'uo di scrivania per
 * 		     se stesso o per l'ente
 * 		  l'utente ha selezionato un opportuno elemento voce   
 * 		  nella VSX_CHIUSURA sono stati inseriti i record con le chiavi dei doc.contabili da riportare
 * 	     all'esercizio successivo e il nuovo elemento voce
 * 	Post: viene chiamata la stored procedure di riporto dei doc.contabili inseriti in VSX_CHIUSURA con cambio di
 * 	      elemento voce

 * 	Name: riporto es successivo per CDS ugaule a Ente
 * 	Pre: in scrivania è stato selezinato il Cds dell'Ente
 * 		  l'utente ha selezionato una nuova voce_f
 * 		  nella VSX_CHIUSURA sono stati inseriti i record con le chiavi dei degli impegni o degli impegni residui
 * 		  da riportare all'esercizio successivo e la nuova voce_f
 * 	Post: viene chiamata la stored procedure di riporto dei doc.contabili inseriti in VSX_CHIUSURA con cambio di
 * 	      voce_f

 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code>	 da utilizzare come chiave nella tabella VSX_CHIUSURA
      
*/

public void	callRiportoNextEsDocContVoce(UserContext userContext, Long pg_call) throws ComponentException 
{
	LoggableStatement cs = null;	
	try 
	{
		try	
		{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB047.riportoNextEsDocContVoce( ?, ? ) }",false,this.getClass());
			cs.setObject( 1, pg_call);
			cs.setObject( 2, ((CNRUserContext)userContext).getUser());						
//			cs.setObject( 2, ((CNRUserContext)userContext).getEsercizio());
//			cs.setObject( 3, ((CNRUserContext)userContext).getUser());
//			cs.setObject( 4, it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());			
			cs.executeQuery();
		} catch (Throwable e) 
		{
			throw handleException(e);
		} finally 
		{
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
}
/*
 * 	Name: riporto da esercizio successivo 
 * 	Pre: nella VSX_CHIUSURA sono stati inseriti i record con le chiavi dei doc.contabili selezionati dall'utente
 * 	     che erano già stati riportati all'esercizio successivo
 * 	     all'esercizio successivo e il nuovo elemento voce
 * 	Post: viene chiamata la stored procedure di riporto indietro dei doc.contabili inseriti in VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code>	 da utilizzare come chiave nella tabella VSX_CHIUSURA
*/

public void	callRiportoPrevEsDocCont(UserContext userContext, Long pg_call) throws ComponentException 
{
	LoggableStatement cs = null;	
	try 
	{
		try	
		{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB047.riportoPrevEsDocCOnt( ?, ? ) }",false,this.getClass());
			cs.setObject( 1, pg_call);
			cs.setObject( 2, ((CNRUserContext)userContext).getUser());						
//			cs.setObject( 2, ((CNRUserContext)userContext).getEsercizio());
//			cs.setObject( 3, ((CNRUserContext)userContext).getUser());
//			cs.setObject( 4, it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());			
			cs.executeQuery();
		} catch (Throwable e) 
		{
			throw handleException(e);
		} finally 
		{
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
}
/*
 * 	Name: ricerca per annullamento
 * 	Pre: l'utente ha richiesto la lista dei documenti contabili che possono essere annullati
 * 	Post: il sistema restituisce il Remote Iterator con la lista dei doc. contabili che possono essere annullati

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clasuole di ricerca indicate dall'utente
 * @return <code>RemoteIterator</code> con la lista di <code>V_obb_acc_xxxBulk</code> che soddisfano le clausole
           di ricerca
	
*/

public it.cnr.jada.util.RemoteIterator cercaPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	try {
		return iterator(
			userContext,
			selectPerAnnullamento(userContext,docCont),
			docCont.getClass(),
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/*
 * 	Name: ricerca per riporta esercizio successivo
 * 	Pre: l'utente ha richiesto la lista dei documenti contabili che possono essere portati all'esercizio successivo
 * 	Post: il sistema restituisce il Remote Iterator con la lista dei doc. contabili che possono essere portati all'esercizio successivo

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clasuole di ricerca indicate dall'utente
 * @return <code>RemoteIterator</code> con la lista di <code>V_obb_acc_xxxBulk</code> che soddisfano le clausole
           di ricerca
*/

public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	try {
		return iterator(
			userContext,
			selectPerRiportaAvanti(userContext,docCont),
			docCont.getClass(),
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}

}
/*
 * 	Name: ricerca per riporta esercizio successivo con cambio di elemento voce/voce_f
 * 	Pre: l'utente ha richiesto la lista dei documenti contabili che possono essere portati all'esercizio successivo con cambio di elemento_voce o di voce_f
 * 	Post: il sistema restituisce il Remote Iterator con la lista dei doc. contabili che possono essere portati all'esercizio successivo

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clasuole di ricerca indicate dall'utente
 * @return <code>RemoteIterator</code> con la lista di <code>V_obb_acc_xxxBulk</code> che soddisfano le clausole
           di ricerca
	
*/
public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	return cercaPerRiportaAvanti( userContext, docCont );

}
/*
 * 	Name: ricerca per riporta esercizio indietro
 * 	Pre: l'utente ha richiesto la lista dei documenti contabili che possono essere portati indietro dall'esercizio successivo
 * 	Post: il sistema restituisce il Remote Iterator con la lista dei doc. contabili che possono essere portati indietro dall'esercizio successivo

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clasuole di ricerca indicate dall'utente
 * @return <code>RemoteIterator</code> con la lista di <code>V_obb_acc_xxxBulk</code> che soddisfano le clausole
           di ricerca
	
*/

public it.cnr.jada.util.RemoteIterator cercaPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
		try {
		return iterator(
			userContext,
			selectPerRiportaIndietro(userContext,docCont),
			docCont.getClass(),
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}

}
/*
 * 	Name: annullamento selezione
 * 	Pre: l'utente ha richiesto l'annullamento della selezione della lista dei documenti contabili 
 * 	     che aveva precedentemente selezioanto
 * 	Post: il sistema cancella dalla tabella VSX_CHIUSURA gli elementi inseriti fino a quel momento a fronte del pg_call corrente

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con il pg_call corrente della VSX_CHIUSURA
*/

public void clearSelection(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException 
{
	try
	{
		if ( doc.getPg_call() == null )
			return;
			
		LoggableStatement ps = new LoggableStatement(getConnection( userContext),
		"DELETE FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"VSX_CHIUSURA " +
		"WHERE PG_CALL = ? " ,true,this.getClass());

		try
		{
			ps.setObject( 1, doc.getPg_call());
			ps.executeUpdate();
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
		
	}	
	catch (SQLException e )
	{
		throw handleException( e );
	}	
}
/*
 *	Name: annullamento selezione per annullamento
 *	Pre: l'utente ha richiesto l'annullamento della selezione della lista dei documenti contabili 
 *	     che aveva precedentemente selezioanto per annullamento
 *	Post: il sistema cancella dalla tabella VSX_CHIUSURA gli elementi inseriti fino a quel momento a fronte del pg_call corrente

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con il pg_call corrente della VSX_CHIUSURA
*/

public void clearSelectionPerAnnullamento(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException 
{
	clearSelection( userContext, doc );

}
/*
 *	Name: annullamento selezione per riporta avanti
 *	Pre: l'utente ha richiesto l'annullamento della selezione della lista dei documenti contabili 
 *	     che aveva precedentemente selezioanto per riportare all'esercizio successivo
 *	Post: il sistema cancella dalla tabella VSX_CHIUSURA gli elementi inseriti fino a quel momento a fronte del pg_call corrente

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con il pg_call corrente della VSX_CHIUSURA
*/

public void clearSelectionPerRiportaAvanti(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException 
{
	clearSelection( userContext, doc );
}
/*
 *	Name: annullamento selezione per riporta indietro
 *	Pre: l'utente ha richiesto l'annullamento della selezione della lista dei documenti contabili 
 *	     che aveva precedentemente selezionato per riportare indietro dall'esercizio successivo
 *	Post: il sistema cancella dalla tabella VSX_CHIUSURA gli elementi inseriti fino a quel momento a fronte del pg_call corrente

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con il pg_call corrente della VSX_CHIUSURA
*/

public void clearSelectionPerRiportaIndietro(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException 
{
	clearSelection( userContext, doc );
}
/*
 * 	Name: eliminazione di un doc.contabile della VSC_CHIUSURA
 * 	Pre: l'utente ha deselezionato un doc. contabile precedentemente selezionato e che era stato inserito nella VSX_CHIUSRA
 * 	Post: il sistema cancella un record dalla tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code> il pg_call corrente della VSX_CHIUSURA
 * @param doc <code>V_obb_acc_xxxBulk</code> con la chiave del doc. contabile da eliminare
	
*/

protected void	eliminaVsx(UserContext userContext, Long pg_call, V_obb_acc_xxxBulk doc ) throws ComponentException 
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection( userContext),
		"DELETE FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"VSX_CHIUSURA " +
		"WHERE PG_CALL = ? AND " +
		"CD_CDS = ? AND " +
		"ESERCIZIO = ? AND " +
		"ESERCIZIO_ORI_ACC_OBB = ? AND " +
		"PG_ACC_OBB = ? AND " +
		"TI_GESTIONE = ?  ",true ,this.getClass());

		try
		{
			ps.setObject( 1, pg_call);
			ps.setString( 2, doc.getCd_cds());
			ps.setObject( 3, doc.getEsercizio());
			ps.setObject( 4, doc.getEsercizio_ori_acc_obb());
			ps.setObject( 5, doc.getPg_acc_obb());
			ps.setString( 6, doc.getTi_gestione());
//			ps.setObject( 6, doc.getPg_ver_rec());			
			ps.executeUpdate();
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
		
	}	
	catch (SQLException e )
	{
		throw handleException( e );
	}	
}
/*
 *	Name: identificazione dell'ultimo PAR_NUM
 *	Pre: il sistema deve inserire dei reocord nella tabella VSX_CHIUSURA
 *	Post: il sistema identifica l'ultimo PAR_NUM inserito per un certo PG_CALL

 * @param userContext <code>UserContext</code>
 * @param pg_call <code>Long</code> il pg_call corrente della VSX_CHIUSURA
 * @return <code>Integer</code> l'ultimo par_num inserito in VSX_CHIUSURA per il pg_call specificato
*/

protected Integer	getLast_par_num(UserContext userContext, Long pg_call) throws ComponentException 
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection( userContext),
		"SELECT PAR_NUM FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"VSX_CHIUSURA " +
		"WHERE PG_CALL = ? AND " +
		"PAR_NUM = ( SELECT MAX(PAR_NUM) " +			
		"FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
		"VSX_CHIUSURA " +
		"WHERE PG_CALL = ? ) " +
		"FOR UPDATE NOWAIT",true,this.getClass());

		try
		{
			ps.setObject( 1, pg_call);
			ps.setObject( 2, pg_call);
			ResultSet rs = ps.executeQuery();
			try
			{	
				if ( rs.next() )
					return  new Integer( rs.getInt(1) + 1) ;
				else
					return  new Integer( 1 ) ;
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
		
	}	
	catch (SQLException e )
	{
		throw handleException( e );
	}	
}
/*
 *	Name: identificazione di un nuovo PG_CALL
 *	Pre: il sistema deve inserire dei reocord nella tabella VSX_CHIUSURA
 *	Post: il sistema identifica un nuovo PG_CALL da usare nell'inserimento dei record nella tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @return <code>Long</code> il pg_call da usare come chiave della VSX_CHIUSURA
	
*/

protected Long	getPg_call(UserContext userContext) throws ComponentException 
{
	LoggableStatement cs = null;
	Long pg = null;
	try 
	{
		try	{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ ? = call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"IBMUTL020.vsx_get_pg_call() }",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.NUMERIC);
			cs.executeQuery();
			pg = new Long(cs.getLong(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	if (pg == null)
		throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la vista VSX_CHIUSURA!");
	return pg;		
}
/*
 *	Name: inzializzazione della selezione
 *	Pre: l'utente ha richiesto la visualizzazione di documenti contabili che soddisfano certi criteri di ricerca
 *	Post: il sistema inzializza un nuovo PG_CALL da usare successivamente nell'inserimento dei record nella tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> da inizializzare con il pg_call
 * @return doc <code>V_obb_acc_xxxBulk</code> inizializzato con il pg_call	
*/

protected V_obb_acc_xxxBulk initializeSelection(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException 
{
	try 
	{
	//	setSavepoint(userContext,"VSX_CHIUSURA");
		doc.setPg_call( getPg_call(userContext));
		return doc;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/*
 *	Name: inzializzazione della selezione per annullamento
 *	Pre: l'utente ha richiesto la visualizzazione di documenti contabili che soddisfano certi criteri di ricerca per effettuare un annullamento
 *	Post: il sistema inzializza un nuovo PG_CALL da usare successivamente nell'inserimento dei record nella tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> da inizializzare con il pg_call
 * @return doc <code>V_obb_acc_xxxBulk</code> inizializzato con il pg_call	
	
*/

public V_obb_acc_xxxBulk initializeSelectionPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException 
{
	try 
	{
		return initializeSelection(userContext,doc);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/*
 *	Name: inzializzazione della selezione per riporta avanti
 *	Pre: l'utente ha richiesto la visualizzazione di documenti contabili che soddisfano certi criteri di ricerca per effettuare il loro riporto all'esercizio
 *	     successivo
 *	Post: il sistema inzializza un nuovo PG_CALL da usare successivamente nell'inserimento dei record nella tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> da inizializzare con il pg_call
 * @return doc <code>V_obb_acc_xxxBulk</code> inizializzato con il pg_call	
	
*/

public V_obb_acc_xxxBulk initializeSelectionPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException 
{
	try 
	{
		return initializeSelection(userContext,doc);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/*
 *	Name: inzializzazione della selezione per riporta indietro
 *	Pre: l'utente ha richiesto la visualizzazione di documenti contabili che soddisfano certi criteri di ricerca per effettuare il loro riporto 
 *	     indietro dall'esercizio successivo
 *	Post: il sistema inzializza un nuovo PG_CALL da usare successivamente nell'inserimento dei record nella tabella VSX_CHIUSURA

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> da inizializzare con il pg_call
 * @return doc <code>V_obb_acc_xxxBulk</code> inizializzato con il pg_call	
	
*/
public V_obb_acc_xxxBulk initializeSelectionPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException 
{
	try 
	{
		return initializeSelection(userContext,doc);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/*
	Name: esercizio non aperto
	Pre: l'esercizio di scrivani per il cds di scrivania non è in stato aperto
	Post: il sistema segnala all'utente l'impossibilità di richiamare le funzioni di annullamento, riporto ad esercizio 
	      successivo e riporto da esercizio successivo

	Name: esercizio aperto
	Pre: l'esercizio di scrivani per il cds di scrivania è in stato aperto
	Post: il sistema inizializza il bulk V_obb_acc_xxxBulk con alcuni dati utilizzati successivamente dalle funzionalità
			di chiususra
*/

public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	try 
	{
//		initializeKeysAndOptionsInto(userContext,bulk);
		EnteBulk ente = (EnteBulk) getHome( userContext, EnteBulk.class).findAll().get(0);
		((V_obb_acc_xxxBulk)bulk).setCd_cds_ente( ente.getCd_unita_organizzativa());
		((V_obb_acc_xxxBulk)bulk).setCd_cds_scrivania( ((CNRUserContext)userContext).getCd_cds());
		verificaStatoEsercizio( userContext, ((CNRUserContext)userContext).getEsercizio(), ((CNRUserContext)userContext).getCd_cds()  );
		verificaAbilitazioneRibaltamento( userContext);
		return bulk;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/*
 *	Name: inserimento di un doc.contabile della VSX_CHIUSURA
 *	Pre: l'utente ha selezionato un doc. contabile 
 *	Post: il sistema inserisce un record dalla tabella VSX_CHIUSURA relativo al doc. contabile da elaborare

 * @param userContext <code>UserContext</code>
 * @param ricerca <code>V_obb_acc_xxxBulk</code> con il pg_call ed eventualmente la nuova voce_f o il nuovo elemento_voce
 * @param doc <code>V_obb_acc_xxxBulk</code> con la chiave del documento contabile da inserire nella VSX_CHIUSURA
 * @param last_par_num <code>Integer</code> con l'ultimo valore di par_num della VSX_CHIUSURA
 * @param proc_name <code>String</code> con il nome della stored procedure da inserire nella VSX_CHIUSURA
 * @param esercizio_ribaltamento <code>Integer</code> esercizio del ribaltamento
  
 * @return  <code>Integer</code> l'ultimo par_num inserito per il pg_call corrente
	
*/

protected Integer	inserisciVsx(UserContext userContext, V_obb_acc_xxxBulk ricerca, V_obb_acc_xxxBulk doc,Integer last_par_num, String proc_name, Integer esercizio_ribaltamento ) throws ComponentException 
{
	try
	{
		LoggableStatement ps =new LoggableStatement( getConnection( userContext),
		"INSERT  INTO " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"VSX_CHIUSURA ( " +
		"PG_CALL, " +
		"PAR_NUM, " +
		"TI_GESTIONE, " +
		"ESERCIZIO, " +
		"CD_CDS, " +
		"CD_CDS_ORIGINE, " +
		"ESERCIZIO_ORI_ACC_OBB, " + 
		"PG_ACC_OBB, " +
		"TI_APPARTENENZA, " +
		"CD_ELEMENTO_VOCE, " +
		"CD_VOCE, " +
		"PG_VER_REC_DOC, " + 							
		"PROC_NAME, " +
		"PG_VER_REC, " +
		"UTUV, " +
		"UTCR, " +
		"DACR, " +
		"DUVA, " +
		"MESSAGETOUSER, " +
		"ESERCIZIO_RIBALTAMENTO ) " +
		"VALUES ( ?, ?, ?, ?, ?, ?, ?,	?,	?,	?,	?,	?,	?,	?,	?,	?,	?,	?, ?, ? )" ,
		true,this.getClass());

		try
		{
			ps.setObject( 1, ricerca.getPg_call());
			ps.setObject( 2, last_par_num);			
			ps.setString( 3, doc.getTi_gestione());
			ps.setObject( 4, doc.getEsercizio());
			ps.setString( 5, doc.getCd_cds());			
			ps.setString( 6, doc.getCd_cds_origine());			
			ps.setObject( 7, doc.getEsercizio_ori_acc_obb());
			ps.setObject( 8, doc.getPg_acc_obb());
			if ( ricerca.getNuova_voce().getCd_voce() != null )
			{
				ps.setString( 9, ricerca.getNuova_voce().getTi_appartenenza());
				ps.setString( 10, null);
				ps.setString( 11, ricerca.getNuova_voce().getCd_voce());
			}
			else if ( ricerca.getNuovo_ev().getCd_elemento_voce() != null )
			{
				ps.setString( 9, ricerca.getNuovo_ev().getTi_appartenenza());
				ps.setString( 10, ricerca.getNuovo_ev().getCd_elemento_voce());
				ps.setString( 11, null);
			}
			else
			{
				ps.setString( 9, null);
				ps.setString( 10, null);
				ps.setString( 11, null);
			}
			ps.setObject( 12, doc.getPg_ver_rec());			
			ps.setString( 13, proc_name);
			ps.setObject( 14, new Integer(1));
			ps.setString( 15, userContext.getUser());
			ps.setString( 16, userContext.getUser());
			Timestamp now = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			ps.setTimestamp(17, now );
			ps.setTimestamp(18, now );
			ps.setString(19, null );
			ps.setObject(20, esercizio_ribaltamento);			
			ps.executeUpdate();
			
			last_par_num = new Integer(last_par_num.intValue() + 1);
			return last_par_num;
			
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
		
	}	
	catch (Exception e )
	{
		throw handleException( e );
	}	
}
/*
 *	Name: inserimento di tutti i doc.contabile della VSX_CHIUSURA
 *	Pre: l'utente ha effettuato una ricerca che ha dato come risultato una lista di documenti contabili
 *		  l'utente ha selezionato di voler agire su tutti i documenti contabili proposti dal sistema
 *	Post: il sistema inserisce tutti i record relativi alla lista dei doc.contabili tramite
 *	      una INSERT/SELECT

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con l'eventuale nuova voce_f o il nuovo elemento_voce
 * @param proc_name <code>String</code> con il nome della stored procedure da inserire nella VSX_CHIUSURA
 * @param esercizio_ribaltamento <code>Integer</code> esercizio del ribaltamento
 * @param sqlB <code>SQLBuilder</code> clausole SQL che permettono di identificare i doc.contabili da inserire in 
      
*/

public void	selectAll(UserContext userContext, V_obb_acc_xxxBulk doc, String proc_name, Integer es_ribaltamento, SQLBuilder sqlB ) throws ComponentException
{

	try 
	{

		String insertStmt = 		"INSERT  INTO " + 
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"VSX_CHIUSURA ( " +
					"PG_CALL, " +
					"PAR_NUM, " +
					"TI_GESTIONE, " +
					"ESERCIZIO, " +
					"CD_CDS, " +
					"CD_CDS_ORIGINE, " +
					"ESERCIZIO_ORI_ACC_OBB, " +
					"PG_ACC_OBB, " +
					"PROC_NAME, " +
					"PG_VER_REC_DOC, " + 					
					"PG_VER_REC, " +
					"UTUV, " +
					"UTCR, " +
					"DACR, " +
					"DUVA, " +
					"MESSAGETOUSER, " +
					"ESERCIZIO_RIBALTAMENTO, " +
					"TI_APPARTENENZA, " +
					"CD_ELEMENTO_VOCE, " +
					"CD_VOCE " +
					") ";

		SQLBuilder sql = sqlB;
//		select 999, rownum, ti_gestione, esercizio, cd_cds, pg_acc_obb, 'XXXX', pg_ver_rec, 'TEST', 'TEST', sysdate, sysdate, null
		String header = "SELECT " + doc.getPg_call().toString() + 
								", rownum, ti_gestione, esercizio, cd_cds, cd_cds_origine, esercizio_ori_acc_obb, pg_acc_obb, '" +
		                	proc_name  +
		                	"', pg_ver_rec, " + 		                	
		                	"1, '" + 
		                	((CNRUserContext)userContext).getUser() +
		                	"', '" + 
		                	((CNRUserContext)userContext).getUser() + 
		                	"', sysdate, sysdate, null, " + 
		                	es_ribaltamento + ", ";

		if ( doc.getNuova_voce().getCd_voce() != null )
		 	header = header + " '" + 
		 						doc.getNuova_voce().getTi_appartenenza() + "', null, '" +
		                	doc.getNuova_voce().getCd_voce() + "'";
		else if ( doc.getNuovo_ev().getCd_elemento_voce() != null )
		 	header = header + " '" + 
		 						doc.getNuovo_ev().getTi_appartenenza() + "', '" +
		                	doc.getNuovo_ev().getCd_elemento_voce() + "', null"; 
		else
		 	header = header + " null, null, null "; 
		                	
		sql.setHeader( header );
		String result = insertStmt.concat( sql.getStatement());
		sql.setStatement( result);

		LoggableStatement ps = sql.prepareStatement(getConnection(userContext));
		try
		{
			ps.executeUpdate();
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
		
		
	}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
		
}
/*
 *	Name: inserimento di tutti i doc.contabile della VSX_CHIUSURA per annullamento
 *	Pre: l'utente ha effettuato una ricerca che ha dato come risultato una lista di documenti contabili potenzialmente annullabili
 *		  l'utente ha selezionato di voler agire su tutti i documenti contabili proposti dal sistema
 *	Post: il sistema inserisce tutti i record relativi alla lista dei doc.contabili tramite
 *	      una INSERT/SELECT

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
      
*/
	      
public void	selectAllPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException
{

	
	try 
	{
		selectAll( userContext, doc, PROC_NAME_ANNULLAMENTO, ((CNRUserContext)userContext).getEsercizio(), selectPerAnnullamento( userContext, doc));
	}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
/*
	try 
	{
		
		Query sql = selectPerAnnullamento( userContext, doc );
		V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome( userContext, V_mandato_reversaleBulk.class );
		SQLBroker broker = home.createBroker( sql );
		
		Long last_pg_dettaglio = ((Distinta_cassiere_detHome)getHome( userContext, Distinta_cassiere_detBulk.class)).getUltimoPg_Dettaglio( userContext, distinta );
		V_mandato_reversaleBulk docContabile;
		while ( broker.next() )
		{
			docContabile = (V_mandato_reversaleBulk) broker.fetch(V_mandato_reversaleBulk.class );
			last_pg_dettaglio = inserisciDettaglioDistinta( userContext, distinta, docContabile, last_pg_dettaglio );
 			last_pg_dettaglio =	aggiungiMandatiEReversaliCollegati( userContext, distinta, docContabile, last_pg_dettaglio )	;					
		}
		broker.close();
	*/	
			
/*
		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
		String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
		java.sql.Timestamp dacr = new java.sql.Timestamp(System.currentTimeMillis());

		java.sql.PreparedStatement ps = getConnection(userContext).prepareStatement(
			"INSERT INTO "+schema+"ASS_TIPO_LA_CDR ( CD_CENTRO_RESPONSABILITA, CD_TIPO_LINEA_ATTIVITA, UTCR, DUVA, UTUV, DACR, PG_VER_REC ) SELECT CD_CENTRO_RESPONSABILITA, ?, ?, ?, ?, ?, 1 FROM "+schema+"V_CDR_VALIDO WHERE ESERCIZIO = ? AND NOT EXISTS ( SELECT 1 FROM "+schema+"ASS_TIPO_LA_CDR WHERE V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA = ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA AND ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA = ? ) ");

		ps.setString(1,dsitinta.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.setString(2,user);  // UTCR
		ps.setTimestamp(3,dacr);   // DUVA
		ps.setString(4,user);   // UTUV
		ps.setTimestamp(5,dacr);    // DACR
		ps.setInt(6,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());   // ESERCIZIO
		ps.setString(7,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		LoggableStatement.execute(ps);
		try{ps.close();}catch( java.sql.SQLException e ){};
			}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
*/		
}
/*
 *	Name: inserimento di tutti i doc.contabile della VSX_CHIUSURA per riporta avanti
 *	Pre: l'utente ha effettuato una ricerca che ha dato come risultato una lista di documenti contabili potenzialmente 
 *	     riportabili all'esercizio successivo
 *		  l'utente ha selezionato di voler agire su tutti i documenti contabili proposti dal sistema
 *	Post: il sistema inserisce tutti i record relativi alla lista dei doc.contabili tramite
 *	      una INSERT/SELECT

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 
*/

public void	selectAllPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException
{

	try 
	{
		selectAll( userContext, doc, PROC_NAME_RIPORTA_AVANTI, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1), selectPerRiportaAvanti( userContext, doc));
	}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
		
}
/*
 *	Name: inserimento di tutti i doc.contabile della VSX_CHIUSURA per riporta avanti evoluto
 *	Pre: l'utente ha effettuato una ricerca che ha dato come risultato una lista di documenti contabili potenzialmente 
 *	     riportabili all'esercizio successivo con cambio di elemento_voce o di voce_f
 *	     l'utente ha selezionato un elemento voce o una voce_f 
 *		  l'utente ha selezionato di voler agire su tutti i documenti contabili proposti dal sistema
 *	Post: il sistema inserisce tutti i record relativi alla lista dei doc.contabili tramite
 *	      una INSERT/SELECT

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
	      
*/

public void	selectAllPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException
{

	try 
	{
		selectAll( userContext, doc, PROC_NAME_RIPORTA_AVANTI_EVOLUTO, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1), selectPerRiportaAvanti( userContext, doc));
	}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
		
}
/*
 *	Name: inserimento di tutti i doc.contabile della VSX_CHIUSURA per riporta indietro
 *	Pre: l'utente ha effettuato una ricerca che ha dato come risultato una lista di documenti contabili potenzialmente 
 *	     riportabili indietro dall'esercizio successivo
 *		  l'utente ha selezionato di voler agire su tutti i documenti contabili proposti dal sistema
 *	Post: il sistema inserisce tutti i record relativi alla lista dei doc.contabili tramite
 *	      una INSERT/SELECT

 * @param userContext <code>UserContext</code>
 * @param doc <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
	      
*/

public void	selectAllPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException
{

	try 
	{
		selectAll( userContext, doc, PROC_NAME_RIPORTA_INDIETRO, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() - 1),selectPerRiportaIndietro(userContext, doc));
	}	
	 catch(Exception e) 
	{
		throw handleException(e);
	}	
		
}
/*
 *	Name: selezione elemento voce per Ente
 *	Pre: l'utente ha in scrivania l'ente
 *		  l'utente ha richiesto la selezione di un elemento_voce
 *	Post: il sistema propone tutti i CAPITOLI o le CATEGORIE di SPESA del CNR per l'esercizio di scrivania

 *	Name: selezione elemento voce per Cds - caso 1
 *	Pre: l'utente ha in scrivania un cds diverso dall'Ente
 *		  l'utente ha richiesto la selezione di docuemnti contabili emessi nel bilancio dell'ente
 *		  l'utente ha richiesto la selezione di un elemento_voce		  
 *	Post: il sistema propone tutti i CAPITOLI di ENTRATA o SPESA del CNR per l'esercizio di scrivania

 *	Name: selezione elemento voce per Cds - caso 2
 *	Pre: l'utente ha in scrivania un cds diverso dall'Ente
 *		  l'utente ha richiesto la selezione di docuemnti contabili emessi nel bilancio del Cds
 *		  l'utente ha richiesto la selezione di un elemento_voce		  
 *	Post: il sistema propone tutti i CAPITOLI di ENTRATA o SPESA del CDS per l'esercizio di scrivania
	
 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @param ev <code>Elemento_voceBulk</code> elemento voce da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sull'elemento voce

*/


public SQLBuilder selectElemento_voceByClause(UserContext userContext, V_obb_acc_xxxBulk docCont, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk ev, CompoundFindClause clauses ) throws it.cnr.jada.comp.ComponentException
{
	SQLBuilder sql = getHome( userContext, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk.class ).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());

	if ( docCont.getFl_ente().booleanValue() || docCont.isEnteInScrivania())
		sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CNR);
	else
		sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CDS);	

	if ( docCont.isEnteInScrivania() )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE);	
	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());

	//spese CNR --> CAPITOLI/CATEGORIE
	//altri	 --> CAPITOLI
	if (docCont.isEnteInScrivania() )
	{
		sql.openParenthesis( "AND");	
		sql.addSQLClause( "AND", "ti_elemento_voce", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.TIPO_CAPITOLO);
		sql.addSQLClause( "OR", "ti_elemento_voce", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.TIPO_CATEGORIA);
		sql.closeParenthesis();
	}
	else
		sql.addSQLClause( "AND", "ti_elemento_voce", sql.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.TIPO_CAPITOLO);	
		
	return sql;
}
/* funzione di riporta con cambio di capitolo */
/* selezionata solo nel caso di impegni CNR non su partita di giro */

/*
 *	Name: selezione voce_f per Ente
 *	Pre: l'utente ha in scrivania l'ente
 *		  l'utente ha richiesto la funzione di riporta con cambio di capitolo
 *		  l'utente ha richiesto la visualizzazione dei nuovi capitoli validi
 *	Post: il sistema propone tutti i CAPITOLI di spesa del CNR PARTE 1 definiti per l'esercizio+1

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @param nuova_voce <code>Voce_fBulk</code> voce da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sulla nuova voce
	

*/

public SQLBuilder selectNuova_voceByClause(UserContext userContext, V_obb_acc_xxxBulk docCont, Voce_fBulk nuova_voce, CompoundFindClause clauses ) throws it.cnr.jada.comp.ComponentException
{
	SQLBuilder sql = getHome( userContext, Voce_fBulk.class ).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1));
	sql.addSQLClause( "AND", "fl_mastrino", sql.EQUALS, "Y");	
	sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
	sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
	sql.addSQLClause( "AND", "cd_parte", sql.EQUALS, Elemento_voceHome.PARTE_1);		
	return sql;
}
/* funzione di riporta con cambio di elemento */
/* selezionata per tutti i doc.cont. esclusi gli impegni CNR non su partita di giro */

/*
	Name: errore gestione
	Pre: l'utente ha in scrivania un cds diverso dall'Ente
		  l'utente ha richiesto la visualizzazione dei nuovi capitoli validi
		  l'utente non ha specificato la gestione (ENTRATA/SPESA)
	Post: il sistema emette un messahhio di errore per indicare che la gestione è un campo mandatorio

	Name: selezione elemento voce  - caso 1
	Pre: l'utente ha in scrivania un cds diverso dall'Ente
		  l'utente ha richiesto la selezione di docuemnti contabili emessi nel bilancio dell'ente (accertamenti o partite di giro)
		  l'utente ha richiesto la visualizzazione dei nuovi capitoli validi
	Post: il sistema propone tutti i CAPITOLI di ENTRATA o SPESA del CNR per l'esercizio successivo a quello di scrivania

	Name: selezione elemento voce  - caso 2
	Pre: l'utente ha in scrivania un cds diverso dall'Ente
		  l'utente ha richiesto la selezione di docuemnti contabili del bilancio del Cds (obbligazioni o partite di giro)
		  l'utente ha richiesto la visualizzazione dei nuovi capitoli validi
	Post: il sistema propone tutti i CAPITOLI di ENTRATA o SPESA del CDS per l'esercizio successivo a quello di scrivania
	
 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @param nuovo_ev <code>Elemento_voceBulk</code> elemento voce da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sull'elemento voce

*/


public SQLBuilder selectNuovo_evByClause(UserContext userContext, V_obb_acc_xxxBulk docCont, Elemento_voceBulk nuovo_ev, CompoundFindClause clauses ) throws it.cnr.jada.comp.ComponentException
{
	SQLBuilder sql = getHome( userContext, Elemento_voceBulk.class ).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1));
	sql.addSQLClause( "AND", "ti_elemento_voce", sql.EQUALS, Elemento_voceHome.TIPO_CAPITOLO);	
	if ( docCont.getFl_ente().booleanValue() )
		sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
	else
		sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CDS);	

	if ( docCont.getTi_gestione() == null  )
		//se non ho il 999 in scrivania l'utente deve selezionare un tipo gestione
		throw new ApplicationException( "E' necessario selezionare il tipo di gestione:Entrata o Spesa"); 
	else
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	return sql;
}
/*
	Name: ricerca per annullamento Ente
	Pre: 	l'utente ha in scrivania il Cds dell'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere annullati
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_ANNULLA
	      per l'esercizio di scrivania emessi dall'UO dell'Ente e di tipo IMPEGNO RESIDUO

	Name: ricerca per annullamento Cds
	Pre: 	l'utente ha in scrivania il Cds diverso dall'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere annullati
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_ANNULLA
	      per l'esercizio di scrivania emessi dall'UO di scrivania sia nel proprio bilancio che nel bilancio dell'Ente

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sulla V_OBB_ACC_ANNULLA
      
*/

protected SQLBuilder selectPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_ANNULLA" ).createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	if ( docCont.isEnteInScrivania() )
		//solo gli impegni residui possono essere annullati
		sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
	if ( docCont.getFl_ente().booleanValue() )
	{//se ho il cds in scrivania e cerco doc x l'ente 
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
		if ( docCont.getTi_competenza_residuo() != null  )
		{
			if ( docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				// solo ACR_RES (gli IMP_RES vengono gestiti dal CNR)					
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
			else
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
		}		
	}	
	else
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		
	return sql;
	
	/*
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	sql.addSQLClause( "AND", "im_acc_obb", sql.GREATER, new BigDecimal(0));	
	sql.addSQLClause( "AND", "im_associato_doc_amm", sql.EQUALS, new BigDecimal(0));
	sql.addSQLClause( "AND", "riportato", sql.EQUALS, "N");	
//	sql.addSQLClause( "AND", "dt_cancellazione", sql.ISNULL, null); la vista già filtra i cancellati
	if ( docCont.isEnteInScrivania() )
		//solo gli impegni residui possono essere annullati
		sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
	if ( docCont.getFl_ente().booleanValue() )
	{//se ho il cds in scrivania e cerco doc x l'ente 
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
		if ( docCont.getTi_competenza_residuo() != null  )
		{
			if ( docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				// solo ACR_RES (gli IMP_RES vengono gestiti dal CNR)					
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
			else
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
		}		
	}	
	else
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		

	// per le partite di giro viene proposta solo quella di origine 		
	sql.addSQLClause( "AND", "(TI_ORIGINE IS NULL OR TI_ORIGINE = TI_GESTIONE) ");

	return sql;
	*/
}
/*
	Name: ricerca per riporta avanti Ente
	Pre: 	l'utente ha in scrivania il Cds dell'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere riportati all'esercizio successivo
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_RIPORTA
	      per l'esercizio di scrivania emessi dall'UO dell'Ente (tipo IMPEGNO  e/o IMPEGNO RESIDUO)

	Name: ricerca per riporta avanti Cds
	Pre: 	l'utente ha in scrivania un Cds diverso dall'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere riportati all'esercizio successivo
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_RIPORTA
	      per l'esercizio di scrivania emessi dall'UO di scrivania sia per l'Ente (tipo ACCERTAMENTO  e/o ACCERTAMENTO RESIDUO)
	      che per se stesso (tipo OBBLIGAZIONE, OBBLIGAZIONE PGIRO  e/o ACCERTAMENTO PGIRO)
	      
 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sulla V_OBB_ACC_RIPORTA
	      
	      
*/

protected SQLBuilder selectPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	//sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	sql.addSQLClause( "AND", "cd_cds_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());
	if ( docCont.isEnteInScrivania() )
	{
		//se ho l'ente in scrivania e cerco dei residui solo --> IMP_RES (ACR_RES sono gestiti dai CDS)
			if ( docCont.getTi_competenza_residuo() != null && docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);			
			else if ( docCont.getTi_competenza_residuo() != null && docCont.getTi_competenza_residuo().equals( docCont.TIPO_COMPETENZA  ))
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);			
	}	
	else if ( docCont.getFl_ente().booleanValue() )
	{	
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
		if ( docCont.getTi_competenza_residuo() != null  )
		{ //se ho il cds in scrivania e cerco doc x l'ente 
			if ( docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				// solo ACR_RES (gli IMP_RES vengono gestiti dal CNR)					
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
			else
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
		}		
	}	
	//else
	//	sql.addSQLClause( "AND", "cd_cds_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if (docCont.getCd_voce() != null && !docCont.isROCd_voce())
		sql.addSQLClause( "AND", "cd_voce", sql.LIKE, docCont.getCd_voce() );
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		
	return sql;
	
	/*
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	sql.addSQLClause( "AND", "im_acc_obb", sql.GREATER, new BigDecimal(0));
	sql.addSQLClause( "AND", "riportato", sql.EQUALS, "N");		
	//non vi siano mandati con stato EMESSO sulle scadenze
	sql.addSQLClause( "AND", "im_associato_rev_man = im_riscontrato");
	//non sia stata completamente riscontrata
	sql.addSQLClause( "AND", "im_acc_obb > im_riscontrato"); 
	//	sql.addSQLClause( "AND", "dt_cancellazione", sql.ISNULL, null); la vista già filtra i cancellati
	if ( docCont.isEnteInScrivania() )
	{
		//se ho l'ente in scrivania e cerco dei residui solo --> IMP_RES (ACR_RES sono gestiti dai CDS)
			if ( docCont.getTi_competenza_residuo() != null && docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);			
			else if ( docCont.getTi_competenza_residuo() != null && docCont.getTi_competenza_residuo().equals( docCont.TIPO_COMPETENZA  ))
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);			
	}	
	else if ( docCont.getFl_ente().booleanValue() )
	{	
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
		if ( docCont.getTi_competenza_residuo() != null  )
		{ //se ho il cds in scrivania e cerco doc x l'ente 
			if ( docCont.getTi_competenza_residuo().equals( docCont.TIPO_RESIDUO  ))
				// solo ACR_RES (gli IMP_RES vengono gestiti dal CNR)					
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
			else
				sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
		}		
	}	
	else
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		

	// solo per il CDS (non per l'ente) per le partite di giro viene proposta solo quella di origine
	sql.addSQLClause( "AND", "((cd_tipo_documento_cont <> '" + Numerazione_doc_contBulk.TIPO_ACR_PGIRO + "' AND " +
									 " cd_tipo_documento_cont <> '" + Numerazione_doc_contBulk.TIPO_OBB_PGIRO + "') OR " +
									 "(cd_tipo_documento_cont = '" + Numerazione_doc_contBulk.TIPO_ACR_PGIRO + "' AND " +
									 " ti_origine = 'E' ) OR " +
									 "(cd_tipo_documento_cont = '" + Numerazione_doc_contBulk.TIPO_OBB_PGIRO + "' AND " +
									 " ti_origine = 'S' ))" );
									 
	return sql;
	*/
}
/*
	Name: ricerca per riporta indietro Ente
	Pre: 	l'utente ha in scrivania il Cds dell'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere riportati indietro dall'esercizio successivo
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_DERIPORTA
	      per l'esercizio di scrivania emessi dall'UO dell'Ente (tipo IMPEGNO RESIDUO)

	Name: ricerca per riporta indietro Cds
	Pre: 	l'utente ha in scrivania un Cds diverso dall'Ente
			l'utente ha richiesto la lista dei documenti contabili che possono essere riportati indietro dall'esercizio successivo
	Post: il sistema restituisce le clausole per identificare i documenti contabili presenti nella vista V_OBB_ACC_DERIPORTA
	      per l'esercizio di scrivania emessi dall'UO di scrivania sia per l'Ente (tipo ACCERTAMENTO RESIDUO)
	      che per se stesso (tipo OBBLIGAZIONE, OBBLIGAZIONE PGIRO  e/o ACCERTAMENTO PGIRO)

 * @param userContext <code>UserContext</code>
 * @param docCont <code>V_obb_acc_xxxBulk</code> con le clausole specificate dall'utente
 * @return <code>SQLBuilder</code> con le clausole per eseguire la ricerca sulla V_OBB_ACC_DERIPORTA
	      
	      
*/

protected SQLBuilder selectPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException 
{
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_DERIPORTA" ).createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	if ( docCont.getFl_ente().booleanValue() )
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
	else
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if ( docCont.getCd_voce() != null && !docCont.isROCd_voce())
		sql.addSQLClause( "AND", "cd_voce", sql.LIKE, docCont.getCd_voce() );
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		
	return sql;
	
	/*
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
	sql.addSQLClause( "AND", "im_acc_obb", sql.GREATER, new BigDecimal(0));
	sql.addSQLClause( "AND", "pg_acc_obb_ori_riporto", sql.ISNOTNULL, null);		
	//non vi siano mandati con stato EMESSO sulle scadenze
	sql.addSQLClause( "AND", "im_associato_rev_man = im_riscontrato");
	//non sia stata completamente riscontrata
	sql.addSQLClause( "AND", "im_acc_obb > im_riscontrato"); 
	//	sql.addSQLClause( "AND", "dt_cancellazione", sql.ISNULL, null); la vista già filtra i cancellati
	if ( docCont.getFl_ente().booleanValue() )
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, docCont.getCd_cds_ente());
	else
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	if ( docCont.getTi_gestione() != null  )
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, docCont.getTi_gestione());
	if ( docCont.getIm_acc_obb() != null  )
		sql.addSQLClause( "AND", "im_acc_obb", sql.EQUALS, docCont.getIm_acc_obb());
	if ( docCont.getCd_terzo() != null  )
		sql.addSQLClause( "AND", "cd_terzo", sql.EQUALS, docCont.getCd_terzo());
	if ( docCont.getCd_elemento_voce() != null  )
		sql.addSQLClause( "AND", "cd_elemento_voce", sql.EQUALS, docCont.getCd_elemento_voce());
	if ( docCont.getPg_doc_da() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.GREATER_EQUALS, docCont.getPg_doc_da());
	if ( docCont.getPg_doc_a() != null  )
		sql.addSQLClause( "AND", "pg_acc_obb", sql.LESS_EQUALS, docCont.getPg_doc_a());		
		

	// solo per il CDS (non per l'ente) per le partite di giro viene proposta solo quella di origine
	sql.addSQLClause( "AND", "((cd_tipo_documento_cont <> '" + Numerazione_doc_contBulk.TIPO_ACR_PGIRO + "' AND " +
									 " cd_tipo_documento_cont <> '" + Numerazione_doc_contBulk.TIPO_OBB_PGIRO + "') OR " +
									 "(cd_tipo_documento_cont = '" + Numerazione_doc_contBulk.TIPO_ACR_PGIRO + "' AND " +
									 " ti_origine = 'E' ) OR " +
									 "(cd_tipo_documento_cont = '" + Numerazione_doc_contBulk.TIPO_OBB_PGIRO + "' AND " +
									 " ti_origine = 'S' ))" );
									 
	return sql;
	*/
}
/**
 *
 *	Name: selezione/deselezione di documenti contabili
 *	Pre: 	l'utente ha richiesto la visualizzazzione di una lista di documenti contabili
 *			l'utente ha selezionato alcuni documenti conatbili
 *			l'utente ha deselezionato alcuni documenti conatbili precedentemente selezionati
 *	Post: il sistema inserisce nella VSX_CHIUSURA le chiavi dei documenti contabili selezionati dall'utente (metodo 'inserisciVsx')
 *	      il sistema elimina dalla VSX_CHIUSURA le chiavi dei documenti contabili deselezionati dall'utente (metodo 'eliminaVsx')
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ricerca	il V_obb_acc_xxxBulk con le clausole di ricerca specificate dall'utente
 * @param	docContabili l'array di documenti contabili (V_obb_acc_xxxBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */	

protected void	setSelection(UserContext userContext, V_obb_acc_xxxBulk ricerca, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili, String procName, Integer es_ribaltamento) throws ComponentException 
{
	try
	{
		Integer last_par_num = null;
		for (int i = 0;i < docContabili.length;i++) 
		{
			V_obb_acc_xxxBulk docContabile = (V_obb_acc_xxxBulk)docContabili[i];
			if (oldDocContabili.get(i) != newDocContabili.get(i)) 
			{
				if ( last_par_num == null )
					last_par_num = getLast_par_num( userContext, ricerca.getPg_call() );
				if (newDocContabili.get(i)) 
					last_par_num = inserisciVsx( userContext, ricerca, docContabile, last_par_num, procName, es_ribaltamento);
				else 
					eliminaVsx(userContext,ricerca.getPg_call(), docContabile);

			}
		} 
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}
}
/**
 *
 *	Name: selezione/deselezione di documenti contabili
 *	Pre: 	l'utente ha richiesto la visualizzazzione di una lista di documenti contabili per annullamento
 *			l'utente ha selezionato alcuni documenti conatabili
 *			l'utente ha deselezionato alcuni documenti contabili precedentemente selezionati
 *	Post: il sistema inserisce nella VSX_CHIUSURA le chiavi dei documenti contabili selezionati dall'utente (metodo 'inserisciVsx')
 *	      il sistema elimina dalla VSX_CHIUSURA le chiavi dei documenti contabili deselezionati dall'utente (metodo 'eliminaVsx')
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ricerca	il V_obb_acc_xxxBulk con le clausole di ricerca specificate dall'utente
 * @param	docContabili l'array di documenti contabili (V_obb_acc_xxxBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */	

public void	setSelectionPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException 
{
	try
	{
		setSelection( userContext, doc, docContabili, oldDocContabili, newDocContabili, PROC_NAME_ANNULLAMENTO, ((CNRUserContext)userContext).getEsercizio());
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}
}
/**
 *
 *	Name: selezione/deselezione di documenti contabili
 *	Pre: 	l'utente ha richiesto la visualizzazzione di una lista di documenti contabili per riporto all'es. successivo
 *			l'utente ha selezionato alcuni documenti conatabili
 *			l'utente ha deselezionato alcuni documenti contabili precedentemente selezionati
 *	Post: il sistema inserisce nella VSX_CHIUSURA le chiavi dei documenti contabili selezionati dall'utente (metodo 'inserisciVsx')
 *	      il sistema elimina dalla VSX_CHIUSURA le chiavi dei documenti contabili deselezionati dall'utente (metodo 'eliminaVsx')
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ricerca	il V_obb_acc_xxxBulk con le clausole di ricerca specificate dall'utente
 * @param	docContabili l'array di documenti contabili (V_obb_acc_xxxBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */	

public void	setSelectionPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException 
{
	try
	{
		setSelection( userContext, doc, docContabili, oldDocContabili, newDocContabili, PROC_NAME_RIPORTA_AVANTI, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1));
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}
}
/**
 *
 *	Name: selezione/deselezione di documenti contabili
 *	Pre: 	l'utente ha richiesto la visualizzazzione di una lista di documenti contabili per riporto all'es. successivo
         con cambio di elemento_voce o voce_f
 *			l'utente ha selezionato alcuni documenti conatabili
 *			l'utente ha deselezionato alcuni documenti contabili precedentemente selezionati
 *	Post: il sistema inserisce nella VSX_CHIUSURA le chiavi dei documenti contabili selezionati dall'utente (metodo 'inserisciVsx')
 *	      il sistema elimina dalla VSX_CHIUSURA le chiavi dei documenti contabili deselezionati dall'utente (metodo 'eliminaVsx')
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ricerca	il V_obb_acc_xxxBulk con le clausole di ricerca specificate dall'utente
 * @param	docContabili l'array di documenti contabili (V_obb_acc_xxxBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */	
public void	setSelectionPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException 
{
	try
	{
		setSelection( userContext, doc, docContabili, oldDocContabili, newDocContabili, PROC_NAME_RIPORTA_AVANTI_EVOLUTO, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() + 1));
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}
}
/**
 *
 *	Name: selezione/deselezione di documenti contabili
 *	Pre: 	l'utente ha richiesto la visualizzazzione di una lista di documenti contabili per riporto indietro dall'es. successivo
 *			l'utente ha selezionato alcuni documenti conatabili
 *			l'utente ha deselezionato alcuni documenti contabili precedentemente selezionati
 *	Post: il sistema inserisce nella VSX_CHIUSURA le chiavi dei documenti contabili selezionati dall'utente (metodo 'inserisciVsx')
 *	      il sistema elimina dalla VSX_CHIUSURA le chiavi dei documenti contabili deselezionati dall'utente (metodo 'eliminaVsx')
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ricerca	il V_obb_acc_xxxBulk con le clausole di ricerca specificate dall'utente
 * @param	docContabili l'array di documenti contabili (V_obb_acc_xxxBulk) potenzialmente interessati da questa modifica
 * @param	oldDocContabili il BitSet che specifica la precedente selezione nell'array docContabili
 * @param	newDocContabili il BitSet che specifica l'attuale selezione nell'array docContabili 
 */	

public void	setSelectionPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException 
{
	try
	{
		setSelection( userContext, doc, docContabili, oldDocContabili, newDocContabili, PROC_NAME_RIPORTA_INDIETRO, new Integer(((CNRUserContext)userContext).getEsercizio().intValue() - 1));
	} 
	catch(Throwable e) 
	{
		throw handleException(e);
	}
}
/**
 * Verifica dello stato dell'esercizio 
 *
 * @param userContext <code>UserContext</code>
 * @param es <code>Integer</code>  l'esercizio da verificare
 * @param cd_cds <code>String</code>  il cds da verificare 
 *
 * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
 *		     TRUE in tutti gli altri casi
 *
 */

void verificaStatoEsercizio( UserContext userContext, Integer es, String cd_cds ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey( 
																									new EsercizioBulk( cd_cds, es ));
	if (esercizio == null )
			throw handleException( new ApplicationException( "Funzione non abilitata: esercizio inesistente!") );
	if ( !esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Funzione non abilitata: esercizio non aperto!") );
}			
			
public boolean isRibaltato(it.cnr.jada.UserContext userContext) throws ComponentException
{
	try	{
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext);
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
public boolean isRibaltato(it.cnr.jada.UserContext userContext, CdrBulk cdr) throws ComponentException
{
	try	{
		cdr = (CdrBulk)getHome(userContext, CdrBulk.class, null, getFetchPolicyName("find")).findByPrimaryKey(cdr);
		cdr.setUnita_padre((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, cdr.getCd_cds());
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
public boolean isRibaltato(it.cnr.jada.UserContext userContext, String cds, Integer esercizio) throws ComponentException
{
	try	{
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, cds, esercizio);
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
public boolean isRibaltato(it.cnr.jada.UserContext userContext, Unita_organizzativaBulk uo, Integer esercizio) throws ComponentException
{
	try	{
		uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class, null, getFetchPolicyName("find")).findByPrimaryKey(uo);
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, uo.getCd_proprio_unita(), esercizio);
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
public boolean isRibaltato(it.cnr.jada.UserContext userContext, CdsBulk cds) throws ComponentException
{
	try	{
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, cds.getCd_unita_organizzativa());
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
public boolean isRibaltato(it.cnr.jada.UserContext userContext, CdsBulk cds, Integer esercizio) throws ComponentException
{
	try	{
		return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, cds.getCd_unita_organizzativa(), esercizio);
	} 
	catch(Throwable e) {
		throw handleException(e);
	}
}	
/**
 * Ritorna true se esistono righe con disponibilità sfondata su V_SITUAZIONE_LINEE_COMP_RES
 * 
 * @param userContext
 * @return
 * @throws ComponentException
 */
public boolean isSfondataDispCdS(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {
			String selCount = "SELECT COUNT(*)" +
							" FROM " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "V_SITUAZIONE_LINEE_COMP_RES" +
							" WHERE CD_CDS = '" + cd_cds + "'" +
							" AND ESERCIZIO = " + ((CNRUserContext)userContext).getEsercizio() +
							" AND TI_GESTIONE = '" + Elemento_voceHome.GESTIONE_SPESE + "' " +
							" AND FL_PARTITA_GIRO = 'N' " +
							" AND (DISP_COMP<0 OR DISP_RES_IMP<0) " ;

			// per la 999 esclude i residui
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
			if (cd_cds.equals( ente.getCd_unita_padre())){
				selCount = selCount + 
					" AND ESERCIZIO_RES = ESERCIZIO " ;
			}
			
			java.sql.ResultSet rsCount = getHomeCache(userContext).getConnection().createStatement().executeQuery(selCount);
			rsCount.next();
			if(rsCount.getInt(1) > 0)
				return true;
			else
				return false;
		} else {
			return true;
		}
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}
/**
 * Ritorna true se è stata chiusa la ricostruzione dei residui per i CdR del CDS di scrivania
 * 
 * @param userContext
 * @return
 * @throws ComponentException
 */
public boolean isRicosResiduiChiusa(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {

			// uso createSQLBuilderEsteso perchè il metodo createSQLBuilder filtra alcune cose
			SQLBuilder sql = ((Pdg_residuoHome)getHome(userContext, Pdg_residuoBulk.class)).createSQLBuilder();
			sql.addTableToHeader("V_CDR_VALIDO_CDS");
			sql.addSQLJoin("PDG_RESIDUO.ESERCIZIO", "V_CDR_VALIDO_CDS.ESERCIZIO");
			sql.addSQLJoin("PDG_RESIDUO.CD_CENTRO_RESPONSABILITA", "V_CDR_VALIDO_CDS.CD_CENTRO_RESPONSABILITA");
			sql.addSQLClause("AND", "STATO", sql.NOT_EQUALS, Pdg_residuoBulk.STATO_CHIUSO);
			sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, cd_cds);
	
			List result = getHome( userContext, Pdg_residuoBulk.class ).fetchAll( sql );
			if ( result.size() > 0 )
				return false;
			else
				return true;
		} else {
			return false;
		}
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}

public boolean isRiaccertamentoChiuso(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk)getHome(userContext, Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(cd_cds,((CNRUserContext)userContext).getEsercizio()));
			if (param_cds.getFl_riaccertamento()) {
				SQLBuilder sql = selectResiduiForRiaccertamento(userContext);
				if (sql.executeCountQuery(getConnection(userContext))>0)
					return false;
			}
		}
		return true;
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}

public boolean isRiobbligazioneChiusa(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk)getHome(userContext, Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(cd_cds,((CNRUserContext)userContext).getEsercizio()));
			if (param_cds.getFl_riobbligazione()) {
				SQLBuilder sql = selectResiduiForRiobbligazione(userContext);
				if (sql.executeCountQuery(getConnection(userContext))>0)
					return false;
			}
		}
		return true;
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}

public boolean isGaeCollegateProgetti(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {
			SQLBuilder sql = selectGaeSenzaProgettiForRibaltamento(userContext);
			if (sql.executeCountQuery(getConnection(userContext))>0)
				return false;
		}
		return true;
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}

public boolean getCdsRibaltato(UserContext context)throws ComponentException
{
	LoggableStatement cs = null;
		try	
		{
			cs = new LoggableStatement(getConnection(context), 
					"{ ? = call " +
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB048.getcdsribaltato( ?, ? ) }",false,this.getClass());
			
			cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
			cs.setInt(2, ((CNRUserContext)context).getEsercizio());
			cs.setString(3, ((CNRUserContext)context).getCd_cds());	
			
			cs.executeQuery();
			return "Y".equals(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null)
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	} 


public void	callRibaltaDispImproprie(UserContext userContext) throws ComponentException 
{
	LoggableStatement cs = null;	
	try 
	{
		try	
		{
			cs = new LoggableStatement(getConnection(userContext), 
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB047.ribalta_disp_improprie( ?, ? , ?) }",false,this.getClass());
			cs.setObject( 1, ((CNRUserContext)userContext).getEsercizio());
			cs.setObject( 2, ((CNRUserContext)userContext).getCd_cds());
			cs.setObject( 3, ((CNRUserContext)userContext).getUser());
			cs.executeQuery();
		} catch (Throwable e) 
		{
			throw handleException(e);
		} finally 
		{
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
}

public OggettoBulk updateParametriCds (UserContext context) throws EJBException,ComponentException, PersistencyException, RemoteException {
	Parametri_cdsBulk parametri_cds =(Parametri_cdsBulk)getHome(context,Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(((CNRUserContext)context).getCd_cds(),((CNRUserContext)context).getEsercizio()-1));
	if(parametri_cds==null || parametri_cds.getFl_ribaltato()){ 
		parametri_cds =(Parametri_cdsBulk)getHome(context,Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(((CNRUserContext)context).getCd_cds(),((CNRUserContext)context).getEsercizio()));
		try {
			
			parametri_cds.setFl_ribaltato( new Boolean(true));
			parametri_cds.setToBeUpdated();
			updateBulk(context, parametri_cds);
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	else{
		throw new ApplicationException("Attenzione! Il ribaltamento per l'esercizio precedente non è stato effettuato.");
	}
		
	return parametri_cds;
}

public void verificaAbilitazioneRibaltamento( UserContext userContext ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	Parametri_cdsBulk parametri_cds =(Parametri_cdsBulk)getHome(userContext,Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(((CNRUserContext)userContext).getCd_cds(),((CNRUserContext)userContext).getEsercizio()));
	if(parametri_cds==null || !parametri_cds.getFl_riporta_avanti()) 
		throw new ApplicationException("Il ribaltamento all'esercizio successivo non è abilitato per questo cds.");	
}

public SQLBuilder selectResiduiForRiaccertamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).createSQLBuilder();
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.CD_CDS_ORIGINE", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getCd_cds());
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.FL_PGIRO",SQLBuilder.EQUALS, "N");

	sql.addTableToHeader("ACCERTAMENTO");
	sql.addSQLJoin("ACCERTAMENTO.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sql.addSQLJoin("ACCERTAMENTO.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sql.addSQLJoin("ACCERTAMENTO.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sql.addSQLJoin("ACCERTAMENTO.PG_ACCERTAMENTO", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");
	
	sql.addSQLClause(FindClause.AND, "ACCERTAMENTO.STATO_RESIDUO", SQLBuilder.ISNULL, null);
	return sql;
}

public SQLBuilder selectResiduiForRiobbligazione(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).createSQLBuilder();
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.CD_CDS_ORIGINE", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getCd_cds());
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
	sql.addSQLClause(FindClause.AND, "V_OBB_ACC_RIPORTA.FL_PGIRO",SQLBuilder.EQUALS, "N");

	sql.addTableToHeader("OBBLIGAZIONE");
	sql.addSQLJoin("OBBLIGAZIONE.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sql.addSQLJoin("OBBLIGAZIONE.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sql.addSQLJoin("OBBLIGAZIONE.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sql.addSQLJoin("OBBLIGAZIONE.PG_OBBLIGAZIONE", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");
	
	sql.addSQLClause(FindClause.AND, "OBBLIGAZIONE.STATO_RESIDUO", SQLBuilder.ISNULL, null);
	return sql;
}
public SQLBuilder selectGaeSenzaProgettiForRibaltamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).createSQLBuilder();
	sql.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause(FindClause.AND, "cd_cds_origine", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	PersistentHome gaeHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");

	PersistentHome accHome = getHome(userContext, Accertamento_scad_voceBulk.class);
	SQLBuilder sqlAccExist = accHome.createSQLBuilder();
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");

	SQLBuilder sqlGaeAccExist = gaeHome.createSQLBuilder();
	sqlGaeAccExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "ACCERTAMENTO_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
	sqlGaeAccExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "ACCERTAMENTO_SCAD_VOCE.CD_LINEA_ATTIVITA");
	sqlGaeAccExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio()+1);
	sqlGaeAccExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.ISNOTNULL, null);

	sqlAccExist.addSQLNotExistsClause(FindClause.AND, sqlGaeAccExist);

	PersistentHome obbHome = getHome(userContext, Obbligazione_scad_voceBulk.class);
	SQLBuilder sqlObbExist = obbHome.createSQLBuilder();
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");

	SQLBuilder sqlGaeObbExist = gaeHome.createSQLBuilder();
	sqlGaeObbExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "OBBLIGAZIONE_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
	sqlGaeObbExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA");
	sqlGaeObbExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio()+1);
	sqlGaeObbExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.ISNOTNULL, null);

	sqlObbExist.addSQLNotExistsClause(FindClause.AND, sqlGaeObbExist);

	sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
			sql.addSQLClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
			sql.addSQLExistsClause(FindClause.AND, sqlAccExist);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
			sql.addSQLClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			sql.addSQLExistsClause(FindClause.AND, sqlObbExist);
		sql.closeParenthesis();
	sql.closeParenthesis();
	
	return sql;
}

public it.cnr.jada.util.RemoteIterator cercaResiduiForRiaccertamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException 
{
	try {
		return iterator(
			userContext,
			selectResiduiForRiaccertamento(userContext),
			V_obb_acc_xxxBulk.class,
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}
}

public it.cnr.jada.util.RemoteIterator cercaGaeSenzaProgettiForRibaltamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException 
{
	try {
		return iterator(
			userContext,
			selectGaeSenzaProgettiForRibaltamento(userContext),
			V_obb_acc_xxxBulk.class,
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}
}

public boolean isProgettiCollegatiGaeApprovati(it.cnr.jada.UserContext userContext) throws ComponentException {
	try {
		String cd_cds = CNRUserContext.getCd_cds(userContext);
		if(cd_cds != null) {
			SQLBuilder sql = selectProgettiCollegatiGaeNonApprovatiForRibaltamento(userContext);
			if (sql.executeCountQuery(getConnection(userContext))>0)
				return false;
		}
		return true;
	}
	catch(Throwable e) {
		throw handleException(e);
	}
}
public SQLBuilder selectProgettiCollegatiGaeNonApprovatiForRibaltamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).createSQLBuilder();
	sql.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause(FindClause.AND, "cd_cds_origine", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getCd_cds());

	PersistentHome gaeHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");

	PersistentHome accHome = getHome(userContext, Accertamento_scad_voceBulk.class);
	SQLBuilder sqlAccExist = accHome.createSQLBuilder();
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sqlAccExist.addSQLJoin("ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");

	SQLBuilder sqlGaeAccExist = gaeHome.createSQLBuilder();
	sqlGaeAccExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "ACCERTAMENTO_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
	sqlGaeAccExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "ACCERTAMENTO_SCAD_VOCE.CD_LINEA_ATTIVITA");
	sqlGaeAccExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio()+1);
	sqlGaeAccExist.addTableToHeader("PROGETTO_OTHER_FIELD");
	sqlGaeAccExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", "PROGETTO_OTHER_FIELD.PG_PROGETTO");
	sqlGaeAccExist.addSQLClause(FindClause.AND, "PROGETTO_OTHER_FIELD.STATO", SQLBuilder.EQUALS, Progetto_other_fieldBulk.STATO_APPROVATO);
	
	sqlAccExist.addSQLNotExistsClause(FindClause.AND, sqlGaeAccExist);

	PersistentHome obbHome = getHome(userContext, Obbligazione_scad_voceBulk.class);
	SQLBuilder sqlObbExist = obbHome.createSQLBuilder();
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.CD_CDS", "V_OBB_ACC_RIPORTA.CD_CDS");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO", "V_OBB_ACC_RIPORTA.ESERCIZIO");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE", "V_OBB_ACC_RIPORTA.ESERCIZIO_ORI_ACC_OBB");
	sqlObbExist.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE", "V_OBB_ACC_RIPORTA.PG_ACC_OBB");

	SQLBuilder sqlGaeObbExist = gaeHome.createSQLBuilder();
	sqlGaeObbExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "OBBLIGAZIONE_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
	sqlGaeObbExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA");
	sqlGaeObbExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext)userContext).getEsercizio()+1);
	sqlGaeObbExist.addTableToHeader("PROGETTO_OTHER_FIELD");
	sqlGaeObbExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", "PROGETTO_OTHER_FIELD.PG_PROGETTO");
	sqlGaeObbExist.addSQLClause(FindClause.AND, "PROGETTO_OTHER_FIELD.STATO", SQLBuilder.EQUALS, Progetto_other_fieldBulk.STATO_APPROVATO);

	sqlObbExist.addSQLNotExistsClause(FindClause.AND, sqlGaeObbExist);

	sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
			sql.addSQLClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
			sql.addSQLExistsClause(FindClause.AND, sqlAccExist);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
			sql.addSQLClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			sql.addSQLExistsClause(FindClause.AND, sqlObbExist);
		sql.closeParenthesis();
	sql.closeParenthesis();
	
	return sql;
}

public it.cnr.jada.util.RemoteIterator cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(UserContext userContext) throws it.cnr.jada.comp.ComponentException 
{
	try {
		return iterator(
			userContext,
			selectProgettiCollegatiGaeNonApprovatiForRibaltamento(userContext),
			V_obb_acc_xxxBulk.class,
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
