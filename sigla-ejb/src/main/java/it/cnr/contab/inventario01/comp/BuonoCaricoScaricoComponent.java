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
 * Created on Jan 20, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario01.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import javax.ejb.EJBException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession;
import it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioHome;
import it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chHome;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoHome;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettHome;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgHome;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BuonoCaricoScaricoComponent
extends it.cnr.jada.comp.CRUDDetailComponent
implements Cloneable,Serializable{

	private String intervallo;
	public BuonoCaricoScaricoComponent()	
	{
	}
	protected void initializeKeysAndOptionsInto(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
			try {
				Tipo_carico_scaricoHome tipoHome = (Tipo_carico_scaricoHome)getHome(usercontext, Tipo_carico_scaricoBulk.class);
				java.util.Collection tipi;
				tipi = tipoHome.findTipoMovimenti(((Buono_carico_scaricoBulk)oggettobulk).getTi_documento());
				((Buono_carico_scaricoBulk)oggettobulk).setTipoMovimenti(tipi);
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			}
		super.initializeKeysAndOptionsInto(usercontext,oggettobulk);	
	}
	protected void inizializzaTipo(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
			try {
				Tipo_carico_scaricoHome tipoHome = (Tipo_carico_scaricoHome)getHome(usercontext, Tipo_carico_scaricoBulk.class);
				java.util.Collection tipi;
				Buono_carico_scaricoBulk buono = (Buono_carico_scaricoBulk)oggettobulk;
				tipi = tipoHome.findTipoMovimenti(buono);
				buono.setTipoMovimenti(tipi);
				if (tipi != null && tipi.size() == 1){
					buono.setTipoMovimento((Tipo_carico_scaricoBulk) tipi.iterator().next());
				}
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			}	
		super.initializeKeysAndOptionsInto(usercontext,oggettobulk);
	}
	
	public void inizializzaBeniDaScaricare(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			setSavepoint(userContext,"INVENTARIO_BENI_APG");
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		}
		
	}
	/** 
	  *  Annullamento
	  *    PreCondition:
	  *      E' stata generata la richiesta di annullare tutte le operazioni Scarico Inventario.
	  *    PostCondition:
	  *      Viene effettuata un rollback fino al punto (SavePoint) impostato in precedenza, 
	  *		(metodo inizializzaBeniDaScaricare)
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	**/
	public void annullaModificaScaricoBeni(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			rollbackToSavepoint(userContext,"INVENTARIO_BENI_APG");
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	/** 
	  *  annullamento
	  *    PreCondition:
	  *      E' stata generata la richiesta di annullare tutte le operazioni effettuate 
	  *		durante la selezione di beni da associare ad una riga di Fattura.
	  *    PostCondition:
	  *      Viene effettuata un rollback fino al punto (SavePoint) impostato in precedenza, 
	  *		(metodo inizializzaBeniAssociatiPerModifica)
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  *  
	**/
	public void annullaModificaBeniAssociati(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			rollbackToSavepoint(userContext,"INVENTARIO_BENI_APG");
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk)
			throws ComponentException{
try{	
	if (bulk instanceof Buono_carico_scaricoBulk) {
		inizializzaTipo(aUC,bulk);
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) bulk;
		buonoCS.setBuono_carico_scarico_dettColl(new SimpleBulkList());		
		// INIZIALIZZA UO, CDS ED ESERCIZIO LEGGENDOLI DA SCRIVANIA
		String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC);
		String uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC);
		buonoCS.setCds_scrivania(cds);
		buonoCS.setUo_scrivania(uo);
		buonoCS.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
		buonoCS.setCondizioni(getHome(aUC,Condizione_beneBulk.class).findAll());
		intervallo = "0-0";
		buonoCS.setInventario(caricaInventario(aUC));
		buonoCS.setLocal_transactionID(getLocalTransactionID(aUC,true));		
		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);			
		buonoCS.setConsegnatario(inventarioHome.findConsegnatarioFor(buonoCS.getInventario()));
		buonoCS.setDelegato(inventarioHome.findDelegatoFor(buonoCS.getInventario()));
		buonoCS.setUo_consegnataria(inventarioHome.findUoRespFor(aUC,buonoCS.getInventario()));
		if (buonoCS instanceof Trasferimento_inventarioBulk){
			inizializzaBuonoTrasferimentoPerInserimento(aUC, buonoCS);
		}
	return buonoCS;
	} 
	else 
		{
		Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) bulk;
		ass.setInventario(caricaInventario(aUC));
		return ass;
		}
    }
	catch(it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException (pe);
	}	
	catch (it.cnr.jada.persistency.IntrospectionException ie){
		throw new ComponentException (ie);
	}
}
/** Viene richiamata la funziona che controlla se l'esercizio coep è chiuso */
	public boolean isEsercizioCOEPChiuso(UserContext userContext) throws ComponentException
{
	LoggableStatement cs = null;	
	String status = null;
	try
	{
		cs = new LoggableStatement(getConnection(userContext),
				"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());		
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));		
		cs.setObject( 3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));		
		cs.executeQuery();
		status = new String(cs.getString(1));
		if(status.compareTo("Y")==0)
			return true;
		//controlla anche se è chiuso l'inventario
		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(userContext, Id_inventarioBulk.class);
		Id_inventarioBulk inventario = inventarioHome.findInventarioFor(userContext,false);
		if (!inventarioHome.isAperto(inventario,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)))
           return true;
	
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);	
	} catch (PersistencyException e) {
		throw handleException(e);
	} catch (IntrospectionException e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	return false;		    	
}
/** Restituisce il progressivo inventario associato alla UO */
	public Id_inventarioBulk caricaInventario(UserContext aUC)
		throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException 
	{
		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
		Id_inventarioBulk inventario = inventarioHome.findInventarioFor(aUC,false);

		// NON ESISTE UN INVENTARI ASSOCIATO ALLA UO DI SCRIVANIA
		if (inventario == null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO!");		
		
	return inventario;
	}
	/** 
	  *  Buono di Carico non esistente
	  *    PreCondition:
	  *      L'OggettoBulk specificato non esiste.
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore.
	  *
	  *  Cerca l'Inventario associato alla Uo di scrivania - Inventario non disponibile.
	  *    PreCondition:
	  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
	  *
	  *  Inizializzazione di una istanza di Buono_carico_scaricoBulk per modifica
	  *    PreCondition:
	  *      E' stata richiesta l'inizializzazione di una istanza di Buono_carico_scaricoBulk per modifica
	  *    PostCondition:
	  *      Viene restituito il Buono di Carico inizializzato per la modifica.
	  *
	  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
	  *
	  * @return <code>OggettoBulk</code> il Buono di Carico inizializzato
	**/
	public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		if (bulk == null)
			throw new ComponentException("Attenzione: non esiste alcun buono corrispondente ai criteri di ricerca!");
        
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)bulk;
		if (!buonoC.isByFattura() && !buonoC.isByDocumento() && !buonoC.isByOrdini()){
			buonoC = (Buono_carico_scaricoBulk)super.inizializzaBulkPerModifica(aUC, bulk);
		}
		inizializzaTipo(aUC,buonoC);	 
		// Carica l'Inventario associato alla UO
		try{
			buonoC.setInventario(caricaInventario(aUC));
			Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);			
			buonoC.setConsegnatario(inventarioHome.findConsegnatarioFor(buonoC.getInventario()));
			buonoC.setDelegato(inventarioHome.findDelegatoFor(buonoC.getInventario()));
			buonoC.setUo_consegnataria(inventarioHome.findUoRespFor(aUC,buonoC.getInventario()));
			if (buonoC.getTi_documento().equals(buonoC.CARICO)|| (!buonoC.isByFattura() && !buonoC.isByDocumento() && !buonoC.isByOrdini())){
 				 buonoC = (Buono_carico_scaricoBulk)getHome(aUC,Buono_carico_scaricoBulk.class).findByPrimaryKey(buonoC);
				 Buono_carico_scarico_dettHome dettHome = (Buono_carico_scarico_dettHome)getHome(aUC,Buono_carico_scarico_dettBulk.class);
				 buonoC.setBuono_carico_scarico_dettColl(new BulkList(dettHome.getDetailsFor(buonoC)));
				 for (Iterator dett = buonoC.getBuono_carico_scarico_dettColl().iterator();dett.hasNext();){
					 Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)dett.next();
					 Inventario_beniBulk inv =(Inventario_beniBulk)getHome(aUC,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(dettaglio.getNr_inventario(),dettaglio.getPg_inventario(),new Long(dettaglio.getProgressivo().longValue())));
					 dettaglio.setBene(inv);
					 dettaglio.CalcolaTotaleBene();
				 }	    
				 getHomeCache(aUC).fetchAll(aUC,dettHome);
			 }
		}
		catch(it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException (pe);
		}
		catch (it.cnr.jada.persistency.IntrospectionException ie){
			throw new ComponentException (ie);
		}
		 
		return buonoC;
	}	
		
	/** 
	  *  L'oggetto non esiste
	  *    PreCondition:
	  *      L'OggettoBulk specificato non esiste.
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore.
	  * 
	  *  Cerca l'Inventario associato alla Uo di scrivania - Inventario non disponibile.
	  *    PreCondition:
	  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
	  *
	  *  Tutte le operazioni correttamente eseguite.
	  *    PreCondition:
	  *      E' stata richiesta l'inizializzazione di una istanza di Buono_caricoBulk per ricerca.
	  *    PostCondition:
	  *      Trasmette il Buono di Carico con tutti gli oggetti collegati e preparato per una operazione di ricerca.
	  *
	  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il bene che deve essere inizializzato.
	  *
	  * @return <code>OggettoBulk</code> il Buono di Carico inizializzato
	**/
	public OggettoBulk inizializzaBulkPerRicerca (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		if (bulk == null)
			throw new ApplicationException("Attenzione: non esiste alcun buono corrispondente ai criteri di ricerca!");
		if (bulk  instanceof Trasferimento_inventarioBulk) {
			throw new ApplicationException("Attenzione: maschera non disponibile in modalità di ricerca!");
		}
		bulk = super.inizializzaBulkPerRicerca(aUC,bulk);	
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bulk;

		// INIZIALIZZA UO, CDS ED ESERCIZIO LEGGENDOLI DA SCRIVANIA
		String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC);
		String uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC);
		buonoCS.setCds_scrivania(cds);
		buonoCS.setUo_scrivania(uo);
		buonoCS.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	
		try{
			buonoCS.setInventario(caricaInventario(aUC));		
			Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);			
			buonoCS.setConsegnatario(inventarioHome.findConsegnatarioFor(buonoCS .getInventario()));
			buonoCS.setDelegato(inventarioHome.findDelegatoFor(buonoCS .getInventario()));
			buonoCS.setUo_consegnataria(inventarioHome.findUoRespFor(aUC,buonoCS.getInventario()));	
		}
		catch(it.cnr.jada.persistency.PersistencyException pe){
		}
		catch (it.cnr.jada.persistency.IntrospectionException ie){
		}
		return buonoCS;
	}
	/** 
	  *  L'oggetto non esiste
	  *    PreCondition:
	  *      L'OggettoBulk specificato non esiste.
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore.
	  * 
	  *  Cerca l'Inventario associato alla Uo di scrivania - Inventario non disponibile.
	  *    PreCondition:
	  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
	  *    PostCondition:
	  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
	  *
	  *  Tutte le operazioni correttamente eseguite.
	  *    PreCondition:
	  *      E' stata richiesta l'inizializzazione di una istanza di Buono_caricoBulk per ricerca libera.
	  *    PostCondition:
	  *      Trasmette il Buono di Carico con tutti gli oggetti collegati e preparato per una operazione di ricerca libera.
	  *
	  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il bene che deve essere inizializzato.
	  *
	  * @return <code>OggettoBulk</code> il Buono di Carico inizializzato
	**/
	public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		if (bulk == null)
			throw new ComponentException("Attenzione: non esiste alcun buono corrispondente ai criteri di ricerca!");

		bulk = super.inizializzaBulkPerRicercaLibera(aUC,bulk);	
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bulk;

		// INIZIALIZZA UO, CDS ED ESERCIZIO LEGGENDOLI DA SCRIVANIA
		String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC);
		String uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC);
		buonoCS.setCds_scrivania(cds);
		buonoCS.setUo_scrivania(uo);
		buonoCS.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
		try{
			buonoCS.setInventario(caricaInventario(aUC));
			Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);			
			buonoCS.setConsegnatario(inventarioHome.findConsegnatarioFor(buonoCS.getInventario()));
			buonoCS.setDelegato(inventarioHome.findDelegatoFor(buonoCS.getInventario()));
			buonoCS.setUo_consegnataria(inventarioHome.findUoRespFor(aUC,buonoCS.getInventario()));	
		}
		catch(it.cnr.jada.persistency.PersistencyException pe){
		}
		catch (it.cnr.jada.persistency.IntrospectionException ie){
		}
		return buonoCS;
	}
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		// r.p. Effettuo la ricerca di Buono di Carico appartenenti all'esercizio Corrente
		// alla tipologia o di Carico o Scarico e alla uo associata
		// inoltre per fare la query su alcune informazioni del dettaglio
		// viene effettuata sulla vista V_buono_carico_scarico
	
		SQLBuilder sql =(SQLBuilder)getHome(userContext,V_buono_carico_scaricoBulk.class).createSQLBuilder();
	    sql.addClause(clauses); 
		sql.addSQLClause("AND", "TI_DOCUMENTO", sql.EQUALS, ((Buono_carico_scaricoBulk)bulk).getTi_documento());
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS,((Buono_carico_scaricoBulk)bulk).getPg_inventario());
		sql.addSQLClause("AND", "PG_BUONO_C_S", sql.EQUALS,((Buono_carico_scaricoBulk)bulk).getPg_buono_c_s());
		sql.addSQLClause("AND", "CD_TIPO_CARICO_SCARICO",sql.EQUALS,((Buono_carico_scaricoBulk)bulk).getCd_tipo_carico_scarico());
		sql.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","V_BUONO_CARICO_SCARICO.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","V_BUONO_CARICO_SCARICO.ESERCIZIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","V_BUONO_CARICO_SCARICO.TI_DOCUMENTO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","V_BUONO_CARICO_SCARICO.PG_BUONO_C_S");
		
		SQLBuilder sql2 =(SQLBuilder)getHome(userContext,bulk).createSQLBuilder();
		sql2.addSQLExistsClause("AND",sql);
		return sql2;
}
	
	public RemoteIterator selectDettagli(
		UserContext userContext,Buono_carico_scaricoBulk buonoC,Class bulkClass, CompoundFindClause clauses) throws ComponentException 
	{
		if (buonoC == null || buonoC.getPg_buono_c_s()==null)
			return new it.cnr.jada.util.EmptyRemoteIterator();
	
		SQLBuilder sql = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
	
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoC.getPg_inventario());
		sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoC.getTi_documento());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoC.getEsercizio());
		sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoC.getPg_buono_c_s());
			it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
	
		return ri;	
	}
	/**
	  *  Ricerca di una Categoria Gruppo Inventario
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca di una Categoria Gruppo Inventario.
	  *    PostCondition:
	  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	  *		clausole che la Categoria si soggetta a gestione inventario, (FL_GESTIONE_INVENTARIO = 'Y');
	  *		che sia valida, ossia che la data di cancellazione sia NULL; che sia un Gruppo e non una categoria (LIVELLO>0).
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param dettaglio il <code>Buono_carico_dettBulk</code> dettaglio.
	  * @param cat_gruppo la <code>Categoria_gruppo_inventBulk</code> Categoria_gruppo_inventBulk modello.
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
	public SQLBuilder selectBene_categoria_BeneByClause(UserContext userContext, Buono_carico_scarico_dettBulk dett, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk cat_gruppo, CompoundFindClause clauses) 
			throws ComponentException
	{	
			SQLBuilder sql = getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk.class).createSQLBuilder();
			sql.addClause( clauses );	
			sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",SQLBuilder.EQUALS, "Y");
			sql.addSQLClause("AND","DATA_CANCELLAZIONE",SQLBuilder.ISNULL, null);
			sql.addSQLClause("AND","LIVELLO",SQLBuilder.GREATER, "0");
			if (dett.getCat_voce()!=null){
				sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
				sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO","CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO");
				sql.addSQLClause("AND","CD_ELEMENTO_VOCE",sql.EQUALS,dett.getCat_voce().getCd_elemento_voce());
				sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,dett.getCat_voce().getEsercizio());
				sql.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS,dett.getCat_voce().getTi_appartenenza());
				sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,dett.getCat_voce().getTi_gestione());
			}
			return sql;		
	}
	/**
	  *  Ricerca di una Ubicazione
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca di una Ubicazione.
	  *    PostCondition:
	  *		 Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, 
	  *		in aggiunta, le	clausole che le Ubicazioni siano associate alla UO di scrivania.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param dettaglio il <code>Buono_carico_scarico_dettBulk</code> dettaglio di riferimento.
	  * @param ubicazione <code> Ubicazione_beneBulk</code> l'Ubicazione modello.
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
	public SQLBuilder selectBene_ubicazioneByClause(UserContext userContext, Buono_carico_scarico_dettBulk dettaglio, Ubicazione_beneBulk ubicazione, CompoundFindClause clauses) 
			throws ComponentException{
		
		String cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		SQLBuilder sql = getHome(userContext, Ubicazione_beneBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		
		// Cerca le Ubicazioni che fanno parte del CdS/UO di scrivania
		sql.openParenthesis("AND");
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS, cds_scrivania);
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, uo_scrivania);
	
		
//		public static final String CD_CDS_FITTIZIO = "999";
//		public static final String CD_UO_FITTIZIO = "999.000";
//		// Aggiunge alle Ubicazioni della UO di scrivania, quelle fittizie.
//		sql.addSQLClause("OR","CD_CDS",sql.EQUALS, Ubicazione_beneBulk.CD_CDS_FITTIZIO);
//		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, Ubicazione_beneBulk.CD_UO_FITTIZIO);
//		sql.closeParenthesis();

		try {
			Unita_organizzativa_enteBulk uoEnte=(Unita_organizzativa_enteBulk)(Utility.createUnita_organizzativaComponentSession().getUoEnte(userContext));
			sql.addSQLClause("OR","CD_CDS",sql.EQUALS, uoEnte.getCd_unita_padre());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, uoEnte.getCd_unita_organizzativa());
		} catch (Exception e) {
			throw handleException(e);
		}
		sql.closeParenthesis();
	
		sql.addOrderBy("LIVELLO");
		sql.addOrderBy("CD_UBICAZIONE");	

		return sql;		
	}	
	/**
	  *  Data una collezione di Utilizzatore_CdrVBulk, (ossia i CdR utilizztori specificati 
	  *	in una riga di dettaglio dell'Inventario), ne estrae tutti gli utilizzatori,
	  *	(Inventario_utilizzatori_laBulk), e li inserisce in una collezione,
	  * (SimpleBulkList), che verrè restituita.
	  *
	  * @param dettaglio <code>Buono_carico_dettBulk</code> la riga di dettaglio.
	  *
	  * @return list la <code>SimpleBulkList</code> lista di CdR utilizzatori rielaborata.
	**/  
	private SimpleBulkList estraiUtilizzatoriFor (Buono_carico_scarico_dettBulk dettaglio) 
		throws ComponentException
	{
		SimpleBulkList utilizzatori = new SimpleBulkList();
	
		for(Iterator i = dettaglio.getV_utilizzatoriColl().iterator(); i.hasNext();){
			Utilizzatore_CdrVBulk v_utilizzatore = (Utilizzatore_CdrVBulk)i.next();
			for (Iterator i_utilizzatore = v_utilizzatore.getBuono_cs_utilizzatoriColl().iterator(); i_utilizzatore.hasNext();){
				Inventario_utilizzatori_laBulk utilizzatore = (Inventario_utilizzatori_laBulk)i_utilizzatore.next();
				utilizzatore =(Inventario_utilizzatori_laBulk) utilizzatore.clone();
				utilizzatore.setPercentuale_utilizzo_cdr(v_utilizzatore.getPercentuale_utilizzo_cdr());
				utilizzatore.setBene(dettaglio.getBene());
				utilizzatore.setToBeCreated();
				utilizzatori.add(utilizzatore);
			}
		}
		return utilizzatori;
	}
	private String assegnaEtichetta(Buono_carico_scaricoBulk buonoc,Inventario_beniBulk bene) {

		String separatore = "-";
		String uo = buonoc.getUo_consegnataria().getCd_unita_organizzativa();
		String codice = new java.text.DecimalFormat("0000000").format(bene.getNr_inventario().longValue());
		String cod_accessorio = new java.text.DecimalFormat("000").format(bene.getProgressivo().longValue());
	
		return (uo + separatore + codice + separatore + cod_accessorio);
	}
	/**
	  * Dato il Buono di Carico, setta ad ogni riga di dettaglio la proprietà STATO_COGE
	  *	= "X". Lo stato COGE, infatti, è sempre impostato ad "X", tranne nel caso in cui si
	  *	effettui un'operazione di Scarico Totale di un Bene tramite un Buono di Scarico 
	  *	NON proveniente da Fattura Attiva: in tal caso, lo Stato COGE sarà impostato = "N".
	  *
	  * 07/06/2004 - BORRIELLO - Modifica per Rich. 693.
	  * Lo stato COGE è impostato, nei Buoni di Carico, tenendo conto del flag FL_ELABORA_BUONO_COGE,
	  * del Tipo di movimento indicato, (TI_CARICO_SCARICO);
	  *	se il buono NON è visibile da Fattura Passiva, (TI_CARICO_SCARICO.FL_FATTURABILE = 'N') e
	  *	il tipo di movimento è soggetto ad elaborazione COGE, (TI_CARICO_SCARICO.FL_ELABORA_BUONO_COGE = 'Y'),
	  *	allora lo stato COGE è impostato a 'N'.
	  *	In tutti gli altri casi è impostato ad 'X'.
	  *
	  * @param buonoC il <code>Buono_carico_scaricoBulk</code> il cui STATO_COGE deve essere impostato
	 * @throws ComponentException 
	 * @throws PersistencyException 
	 * 
	  *  
	**/
	private void assegnaStatoCOGE(UserContext userContext,Buono_carico_scaricoBulk buonoC) throws PersistencyException, ComponentException{
		for (Iterator i= buonoC.getBuono_carico_scarico_dettColl().iterator(); i.hasNext();){
			Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)i.next();
			String stato_coge;
			if (buonoC.getTipoMovimento().isFatturabile()) 
				stato_coge = Buono_carico_scarico_dettBulk.STATO_COGE_X;
			else 
				if(buonoC.getTipoMovimento().isElaborabileCOGE() && buonoC.getTi_documento().compareTo(Buono_carico_scaricoBulk.SCARICO)==0 && dettaglio.getBene().isTotalmenteScaricato())
				  stato_coge =Buono_carico_scarico_dettBulk.STATO_COGE_X;
				else 
				  stato_coge = Buono_carico_scarico_dettBulk.STATO_COGE_N;
			
			dettaglio.setStato_coge(stato_coge);
			Inventario_beniBulk bene_a=null;
			
			if ((buonoC.getTi_documento().compareTo(Buono_carico_scaricoBulk.SCARICO)==0) && buonoC.getTipoMovimento().isQuoteElaborabile()){
				if(dettaglio.getCrudStatus()!=OggettoBulk.TO_BE_CREATED ){
					 bene_a =(Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(dettaglio.getNr_inventario(),dettaglio.getPg_inventario(),new Long(dettaglio.getProgressivo().longValue())));
				if( dettaglio.getStato_coge_quote().compareTo(Buono_carico_scarico_dettBulk.STATO_COGE_C)!=0)	 
					if (bene_a.getValore_ammortizzato().compareTo(new BigDecimal(0))!=0)
						dettaglio.setStato_coge_quote(Buono_carico_scarico_dettBulk.STATO_COGE_N);
					else
						dettaglio.setStato_coge_quote(Buono_carico_scarico_dettBulk.STATO_COGE_X);
				}
			}
			else 
			        dettaglio.setStato_coge_quote(Buono_carico_scarico_dettBulk.STATO_COGE_X);	
		        }
		        
	}
	/** 
	  *  Validazione del Buono di Scarico.
	  *    PreCondition:
	  *      ValidaBuonoScarico  non superato.
	  *    PostCondition:
	  *      Non  viene consentita la registrazione del buono di scarico.
	  *   
	  *  Tutti i controlli superati.
	  *    PreCondition:
	  *      Nessun errore rilevato.
	  *    PostCondition:
	  *      Viene consentito il salvataggio del Buono di Scarico.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il Bulk da creare
	  *
	  * @return l'oggetto <code>OggettoBulk</code> creato
	**/
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

		try{

			if (bulk instanceof Trasferimento_inventarioBulk){
				return creaBuonoTrasferimentoConBulk(userContext, bulk);
			} else if (bulk instanceof Buono_carico_scaricoBulk && ((Buono_carico_scaricoBulk)bulk).getTi_documento().compareTo(Buono_carico_scaricoBulk.SCARICO)==0){
				return creaBuonoScaricoConBulk(userContext, bulk);
			}else{
				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) bulk;		
				validaBuonoCarico(userContext,buonoCS);
				intervallo = "0-0";
				buonoCS.setTi_documento(buonoCS.getTipoMovimento().getTi_documento());	

				java.util.Hashtable progressivi = new java.util.Hashtable();
				
				// Collezione di beni di cui non è stato possibile aggiornare il campo
				//	IMPONIBILE_AMMOTAMENTO; utilizzato in un Buono di Carico a Valore diretto, (non da Fattura).
				java.util.Vector notChangedBeniKey = new java.util.Vector();
			    
				if (!buonoCS.isPerAumentoValore() && !buonoCS.isByDocumentoPerAumentoValore()){
					esplodiDettagli(userContext,buonoCS, progressivi);
					assegnaStatoCOGE(userContext,buonoCS);
					//non necessario per i documenti in quanto la quantita è sempre 1
					if (buonoCS.isByFattura())
						validaValoreBeneDaFattura(buonoCS);
				
					completeUtilizzatori(buonoCS.getBuono_carico_scarico_dettColl());	
					insertBeni(userContext,buonoCS,buonoCS.getBuono_carico_scarico_dettColl());
				}
				else{
					assegnaStatoCOGE(userContext,buonoCS);
					insertBeniPerAumentoValore(userContext, buonoCS, notChangedBeniKey);		
				}

				if (buonoCS.isByOrdini()){
					try {
						Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
						Long pg = null;

						pg = numHome.getNextPg(userContext,
								buonoCS.getEsercizio(),
								buonoCS.getPg_inventario(),
								buonoCS.getTi_documento(),
								userContext.getUser());
						SimpleBulkList<OggettoBulk> dettagli = buonoCS.getBuono_carico_scarico_dettColl();
						Buono_carico_scaricoBulk definitivo = (Buono_carico_scaricoBulk) buonoCS.clone();

						Buono_carico_scarico_dettHome home = (Buono_carico_scarico_dettHome) getTempHome(userContext, Buono_carico_scarico_dettBulk.class);

						definitivo.setPg_buono_c_s(pg);
						definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);

						definitivo.setBuono_carico_scarico_dettColl(new SimpleBulkList());
						for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
							Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk) i.next();
							Buono_carico_scarico_dettBulk new_dettaglio = (Buono_carico_scarico_dettBulk) dettaglio.clone();
							new_dettaglio.setPg_buono_c_s(pg);
							new_dettaglio.setCrudStatus(OggettoBulk.TO_BE_CREATED);
							definitivo.addToBuono_carico_scarico_dettColl(new_dettaglio);

							Transito_beni_ordiniHome homeTransito = (Transito_beni_ordiniHome)getHome(userContext, Transito_beni_ordiniBulk.class);
							Transito_beni_ordiniBulk transito_beni_ordiniBulk = new Transito_beni_ordiniBulk();
							transito_beni_ordiniBulk.setId(dettaglio.getIdTransito());
							transito_beni_ordiniBulk = (Transito_beni_ordiniBulk)homeTransito.findByPrimaryKey(transito_beni_ordiniBulk);
							if (transito_beni_ordiniBulk != null){
								transito_beni_ordiniBulk.setStato(Transito_beni_ordiniBulk.STATO_TRASFERITO);
								transito_beni_ordiniBulk.setToBeUpdated();
								super.modificaConBulk(userContext, transito_beni_ordiniBulk);
							}
						}
						buonoCS = (Buono_carico_scaricoBulk) super.creaConBulk(userContext, definitivo);
					} catch (it.cnr.jada.persistency.PersistencyException e) {
						throw handleException(buonoCS, e);
					} catch (it.cnr.jada.persistency.IntrospectionException e) {
						throw handleException(buonoCS, e);
					}
					buonoCS.setByOrdini(true);

				} else {
					buonoCS = (Buono_carico_scaricoBulk)super.creaConBulk(userContext,buonoCS);
				}

				if (notChangedBeniKey != null && !notChangedBeniKey.isEmpty()){
					String msg = "Operazione riuscita con successo.\n";
					msg = msg + buildBeniNotChanged_Message(notChangedBeniKey);
					
					return asMTU(buonoCS, msg);
				}
				
				return buonoCS;
			}
			
		}catch (Throwable e){
			throw handleException(bulk, e);
		}
	}
	
	/** 
	  *  Validazione dell'operazione di Trasferimento.
	  *    PreCondition:
	  *      ValidaTrasferimento  non superato.
	  *    PostCondition:
	  *      Non  viene consentita la registrazione del buono di scarico.
	  *   
	  *  Tutti i controlli superati.
	  *    PreCondition:
	  *      Nessun errore rilevato.
	  *    PostCondition:
	  *      Viene consentito la creazione dei Buoni di Scarico e Carico per l'operazione di trasferimento dei beni.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il Bulk da creare
	  *
	  * @return l'oggetto <code>OggettoBulk</code> creato
	**/
	private OggettoBulk creaBuonoTrasferimentoConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{
		
		Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk)bulk;
		
		try{
			
			// Valida il Buono di Trasferimento
			validaTrasferimento(userContext, buonoT);

			// Richiama la Procedura che si occuperè di effettuare il trasferimento dei beni selezionati
			callTrasferisciBeni(userContext, buonoT);
			
			return asRO(buonoT, "Creazione eseguita in modo corretto.");
			
		}catch (ApplicationException ae){
			return asRO(buonoT, ae.getMessage());
		}catch (Throwable e){
			throw handleException(bulk, e);
		}
	}
	/**
	  *  Valida Buono - UO destinazione non specificata
	  *    PreCondition:
	  *      L'operazione di trasferimento che si sta facendo è di tipo extra-Inventario,
	  *		ossia, il trasferimento avverrè verso l'inventario di un'altra UO. Non è stata
	  *		specificata la UO destinazione.
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente
	  *
	  *  Valida Data Buono - data non valida
	  *    PreCondition:
	  *      La data del Buono di Scarico non supera i controlli di validazione, (metodo validaDataBuonoScarico).
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente
	  *  
	  *  Valida Buono - data di scarico inferiore al valore consentito
	  *    PreCondition:
	  *      La data di registrazione indicata per il Buono di Scarico è anteriore alla MAX(data_ultima_modifica)
	  *		registrata per i beni scaricati.
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente.
	  *  
	  *  Valida Buono - tipo movimento carico non specificato
	  *    PreCondition:
	  *      Non è stato specificato un tipo di carico.
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente.
	  *
	  *  Valida Buono - tipo movimento scarico non specificato
	  *    PreCondition:
	  *      Non è stato specificato un tipo di scarico.
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente.
	  *    
	  *  Valida Buono - descrizione mancante
	  *    PreCondition:
	  *      Non è stata specificata una descrizione per il Buono di Scarico.
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente
	  *
	  *  Valida Dettagli 
	  *      I dettagli non superano i controlli di validazione, (metodo validaDettagliTrasferiti).
	  *    PostCondition:
	  *      Un messaggio di errore viene visualizzato all'utente
	  *
	  *  Valida Buono - Tutti i controlli superati.
	  *    PreCondition:
	  *      E' stata richiesta una operazione di creazione di un Buono di Scarico. Tutti i controlli superati.
	  *    PostCondition:
	  *      Consente di portare a termine l'operazione di trasferimento
	  * 
	  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
	  * @param trasf l'oggetto <code>Trasferimento_inventarioBulk</code> che contiene le informazioni relative al trasferimento.
	**/
	private void validaTrasferimento (UserContext aUC,Trasferimento_inventarioBulk trasf) 
		throws ComponentException
	{

		try{

			// CONTROLLA I DETTAGLI TRASFERITI
			validaDettagliTrasferiti(aUC, trasf);
			
			// PER TRASFERIMENTO EXTRA INVENTARIO, CONTROLLA CHE SIA STATO INDICATA UNA UO_DESTINAZIONE
			if (trasf.isTrasferimentoExtraInv() && 
					(trasf.getUo_destinazione() == null || trasf.getUo_destinazione().getCd_unita_organizzativa() == null))			
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una UO di destinazione");

			// Controlla la validità della data indicata nel Buono di Scarico
			validaDataBuonoScarico(aUC, trasf);

			// CONTROLLA LA DATA DI TRAFSERIMENTO - DATA DI TRAFSERIMENTO < MAX(DATA_REGISTRAZIONE) per gli inventari interessati
			/*java.sql.Timestamp max_dt_registrazione = null;

			max_dt_registrazione = getMaxDataRegistrazioneFor(aUC, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC), trasf.getInventario(), trasf.getInventario_destinazione());
			
			if (max_dt_registrazione!=null && trasf.getData_registrazione().before(max_dt_registrazione)){
				StringBuffer msg = new StringBuffer("Attenzione: data di Trasferimento non valida.\n");
				msg.append("La Data di registrazione non puè essere antecedente all'ultima data registrata per i buoni di carico/scarico,");
				msg.append("(" + max_dt_registrazione + ")");
				
				throw new it.cnr.jada.comp.ApplicationException(msg.toString());
			}*/

			
			/* CONTROLLA CHE LA DATA DI SCARICO NON SIA ANTECEDENTE ALLA MAX(DATA) DELLE
			 *	MODIFICHE SUI SINGOLI BENI SCARICATI
			*/
			/* r.p. non dovrebbe servire vengono scartati prima
			 * java.sql.Timestamp maxData = getMaxDataFor(aUC, trasf);
			if (maxData != null && trasf.getData_registrazione().before(maxData)){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Scarico non valida.\n La Data di Scarico non puè essere precedente ad una modifica di uno dei beni scaricati");
			}*/
			
			// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI CARICO
			if (trasf.getTipoMovimentoCarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento Carico");

			// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI SCARICO
			if (trasf.getTipoMovimentoScarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento Scarico");


			// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE 
			if (trasf.getDs_buono_carico_scarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Descrizione per il Buono di Scarico");

		
		} catch(Throwable t){
			throw handleException(trasf, t);		
		} 
	}	
/** 
	  *  Elimina un Buono di Scarico.
	  *    PreCondition:
	  *      E' stata generata la richiesta di cancellare fisicamente un Buono di Scarico.
	  *    PostCondition:
	  *      Il Buono di Scarico con i suoi dettagli è stato eliminato.
	  *		Vengono cancellate anche tutti beni scaricati nella sessione di lavoro e presenti 
	  *		sulla tabella INVENTARIO_BENI_APG.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
	  * @param bulk <code>OggettoBulk</code> il Bulk da eliminare.
	**/ 
	public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException {
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bulk;
	  // Controlla che i beni indicati nel Buono di Carico non siano associati a Fatture
		checkBeniAssociatiPerElimina(userContext, buonoCS);
		if (buonoCS.getTi_documento().compareTo(buonoCS.SCARICO)==0){
//		Controlla che i beni legati al Buono siano eliminabili
			boolean cancella=checkBeniPerEliminaBuono(userContext, buonoCS);
			if (cancella) {
				try {
					Inventario_beni_apgHome benihome = (Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class); 
					SQLBuilder sql= benihome.createSQLBuilder();
					sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoCS.getLocal_transactionID());
					List beni =benihome.fetchAll(sql);
					for (Iterator i = beni.iterator();i.hasNext();){
						Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)i.next();
						bene_apg.setToBeDeleted();							
						super.eliminaConBulk(userContext,bene_apg);	
					}		
			
				} catch (PersistencyException e) {
					throw handleException(e);
				} 
				super.eliminaConBulk(userContext, buonoCS);
			}
		}else{		
	try{
		  // Controlla che i beni indicati nel Buono di Carico non siano associati a Fatture
  	 // checkBeniAssociatiPerElimina(userContext, buonoCS);
		  // Controlla che i beni legati al Buono di Carico siano eliminabili
  	  boolean cancella  = checkBeniPerEliminaBuono(userContext, buonoCS);
  	  if (cancella) {
					
		Buono_carico_scarico_dettHome buonoHome=(Buono_carico_scarico_dettHome)getHome(userContext,Buono_carico_scarico_dettBulk.class);
		SQLBuilder sql= buonoHome.createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,buonoCS.getPg_inventario());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,buonoCS.getEsercizio());
		sql.addSQLClause("AND","TI_DOCUMENTO",SQLBuilder.EQUALS,buonoCS.getTi_documento());
		sql.addSQLClause("AND","PG_BUONO_C_S",SQLBuilder.EQUALS,buonoCS.getPg_buono_c_s());
		sql.addOrderBy("-PROGRESSIVO");
		List beni =buonoHome.fetchAll(sql);
		for (Iterator i = beni.iterator();i.hasNext();){
			Buono_carico_scarico_dettBulk buono_dett = (Buono_carico_scarico_dettBulk)i.next();
					SQLBuilder sql_dett= buonoHome.createSQLBuilder();
					sql_dett.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,buono_dett.getPg_inventario());
					sql_dett.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS,buono_dett.getNr_inventario());
					sql_dett.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,buono_dett.getProgressivo());
					if (sql_dett.executeCountQuery(getConnection(userContext))==1){
						Inventario_beniBulk inv =(Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(buono_dett.getNr_inventario(),buono_dett.getPg_inventario(),new Long(buono_dett.getProgressivo().longValue())));
						if (!inv.isMigrato()){
//							 Elimina gli Utilizzatori eventualmente specificati per i beni del Buono di Carico
							eliminaUtilizzatoriBuono(userContext,inv);
							inv.setToBeDeleted();							
							super.eliminaConBulk(userContext,inv);
						}
					}	
		}
		super.eliminaConBulk(userContext, buonoCS);
	}
	 
	} catch (PersistencyException e) {
		throw handleException(e);
	}
	 catch (SQLException e) {
		throw handleException(e);
	}
}		
}
/** 
	  *  Elimina dei Beni associati ad una riga di Fattura Attiva.
	  *    PreCondition:
	  *      E' stata generata la richiesta di cancellare alcune associazioni, (Beni/Riga Fattura Attiva), 
	  *		fatte durante una sessione di lavoro.
	  *    PostCondition:
	  *      Vengono cancellate dalla tabella d'appoggio INVENTARIO_BENI_APG, i beni indicati nei parametri, 
	  *		associati alla riga di Fattura selezionata.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
	  * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
	  * @param beni i <code>OggettoBulk[]</code> beni selezionati dal'utente, che andrano cancellati dalle associazioni.
	  * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di fattura di cui vanno cancellate le associazioni.
**/
public void eliminaBeniAssociatiConBulk(UserContext userContext, OggettoBulk buono, OggettoBulk[] beni,OggettoBulk oggetto) throws ComponentException {
	try {
		for (int i = 0;i < beni.length;i++) {
			Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];	
			Inventario_beni_apgHome homebeni =(Inventario_beni_apgHome) getHome(userContext,Inventario_beni_apgBulk.class);
			SQLBuilder sql=homebeni.createSQLBuilder();
			if ((buono != null )&& (buono instanceof Buono_carico_scaricoBulk)){
				Buono_carico_scaricoBulk buonoS=(Buono_carico_scaricoBulk)buono;
				sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
				sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
			}
			else{
				Ass_inv_bene_fatturaBulk buonoS=(Ass_inv_bene_fatturaBulk)buono;
				sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
				sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
			}
			
			sql.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS, bene.getNr_inventario());
			sql.openParenthesis("AND");
		      sql.addSQLClause("AND","PROGRESSIVO", SQLBuilder.EQUALS, bene.getProgressivo());
		      sql.addSQLClause("OR","PROGRESSIVO", SQLBuilder.NOT_EQUALS,"0");
		      sql.addSQLClause("AND","FL_VISIBILE", SQLBuilder.EQUALS,"N");
		    sql.closeParenthesis();
			if ((oggetto != null )&& (oggetto instanceof Fattura_attiva_rigaIBulk)){
					Fattura_attiva_rigaIBulk riga_fattura=(Fattura_attiva_rigaIBulk) oggetto;
					sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
					sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
					sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
					sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_attiva());
					sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());				
			}else if (oggetto != null && oggetto instanceof Nota_di_credito_rigaBulk){
				Nota_di_credito_rigaBulk riga_fattura=(Nota_di_credito_rigaBulk) oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
			}else if (oggetto != null && oggetto instanceof Nota_di_debito_rigaBulk){
				Nota_di_debito_rigaBulk riga_fattura=(Nota_di_debito_rigaBulk) oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
			}
			else if (oggetto != null && oggetto instanceof Documento_generico_rigaBulk ){
				Documento_generico_rigaBulk riga= (Documento_generico_rigaBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_documento_generico());
				sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,riga.getCd_tipo_documento_amm());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
			}
			List listabeni=homebeni.fetchAll(sql);
			for(Iterator iteratore=listabeni.iterator();iteratore.hasNext();){
				Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iteratore.next();
				bene_apg.setToBeDeleted();							
				super.eliminaConBulk(userContext,bene_apg);	
			}	
		}
		} catch (PersistencyException e) {
			throw handleException(e);
		} 
}	
	

/** 
 *  Elimina TUTTI i Beni associati ad una riga di Fattura Attiva.
 *    PreCondition:
 *      E' stata generata la richiesta di cancellare tutte le associazioni, (Beni/Riga Fattura Attiva), 
 *		fatte durante una sessione di lavoro.
 *    PostCondition:
 *      Vengono cancellate dalla tabella d'appoggio INVENTARIO_BENI_APG, i beni associati
 *		alla riga di Fattura selezionata. Se non è stata selezionata alcuna riga di Fattura,
 *		verranno cancellate tutti i beni presenti sulla tabella.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> buono di scarico.
 * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di fattura di cui vanno cancellate le associazioni.
**/  
public void eliminaBuoniAssociatiConBulk(UserContext userContext, Ass_inv_bene_fatturaBulk buonoS, OggettoBulk oggetto) throws ComponentException {
	try{
	Inventario_beni_apgHome benihome =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);//,"V_INVENTARIO_BENI_APG");
	SQLBuilder sql= benihome.createSQLBuilder();
	sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
	
	if (oggetto != null && oggetto instanceof Fattura_passiva_rigaIBulk){	
			Fattura_passiva_rigaIBulk riga_fattura=(Fattura_passiva_rigaIBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
	}
	else if(oggetto != null && oggetto instanceof Nota_di_credito_rigaBulk){	
			Nota_di_credito_rigaBulk riga_fattura=(Nota_di_credito_rigaBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
	}
	else if(oggetto != null && oggetto instanceof Nota_di_debito_rigaBulk){	
		Nota_di_debito_rigaBulk riga_fattura=(Nota_di_debito_rigaBulk)oggetto;
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
	}
	else if(oggetto != null && oggetto instanceof Fattura_attiva_rigaIBulk){	
		Fattura_attiva_rigaIBulk riga_fattura=(Fattura_attiva_rigaIBulk)oggetto;
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_attiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
}
	
	List beni =benihome.fetchAll(sql);
	
	for(Iterator i=beni.iterator();i.hasNext();){
		Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)i.next();
		bene_apg.setToBeDeleted();							
		super.eliminaConBulk(userContext,bene_apg);
	}		
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}
public void eliminaBuoniAssociatiConBulk(UserContext userContext, Ass_inv_bene_fatturaBulk buonoS, OggettoBulk[] beni,OggettoBulk oggetto) throws ComponentException {

	try {
	for (int i = 0;i < beni.length;i++) {
		Buono_carico_scarico_dettBulk buono = (Buono_carico_scarico_dettBulk)beni[i];	
		Inventario_beni_apgHome benihome=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);//,"V_INVENTARIO_BENI_APG");
		SQLBuilder sql= benihome.createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS, buono.getNr_inventario());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS, buono.getEsercizio());
		sql.addSQLClause("AND","TI_DOCUMENTO",SQLBuilder.EQUALS, buono.getTi_documento());
		sql.addSQLClause("AND","PG_BUONO_C_S",SQLBuilder.EQUALS, buono.getPg_buono_c_s());
		
		sql.openParenthesis("AND");
	      sql.addSQLClause("AND","PROGRESSIVO", SQLBuilder.EQUALS, buono.getProgressivo());
	      sql.addSQLClause("OR","PROGRESSIVO", SQLBuilder.NOT_EQUALS,"0");
	      sql.addSQLClause("AND","FL_VISIBILE", SQLBuilder.EQUALS,"N");
	    sql.closeParenthesis();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());

		if (oggetto != null && oggetto instanceof Fattura_passiva_rigaIBulk){	
			Fattura_passiva_rigaIBulk riga_fattura=(Fattura_passiva_rigaIBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
		}
		else if(oggetto != null && oggetto instanceof Nota_di_credito_rigaBulk){	
			Nota_di_credito_rigaBulk riga_fattura=(Nota_di_credito_rigaBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
		}
		else if(oggetto != null && oggetto instanceof Nota_di_debito_rigaBulk){	
				Nota_di_debito_rigaBulk riga_fattura=(Nota_di_debito_rigaBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
		}
		else if(oggetto != null && oggetto instanceof Fattura_attiva_rigaIBulk){	
			Fattura_attiva_rigaIBulk riga_fattura=(Fattura_attiva_rigaIBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_attiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());					
		}
		List listabeni = benihome.fetchAll(sql);
		for(Iterator iteratore=listabeni.iterator();iteratore.hasNext();){
			Inventario_beni_apgBulk bene_apg =(Inventario_beni_apgBulk)iteratore.next();
			bene_apg.setToBeDeleted();							
			super.eliminaConBulk(userContext,bene_apg);	
		}	
	}
	} catch (PersistencyException e) {
		throw handleException(e);
	} 
}	

/** 
 *  Elimina TUTTI i Beni associati ad una riga di Fattura Attiva.
 *    PreCondition:
 *      E' stata generata la richiesta di cancellare tutte le associazioni, (Beni/Riga Fattura Attiva), 
 *		fatte durante una sessione di lavoro.
 *    PostCondition:
 *      Vengono cancellate dalla tabella d'appoggio INVENTARIO_BENI_APG, i beni associati
 *		alla riga di Fattura selezionata. Se non è stata selezionata alcuna riga di Fattura,
 *		verranno cancellate tutti i beni presenti sulla tabella.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> buono di scarico.
 * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di fattura di cui vanno cancellate le associazioni.
**/  
public void eliminaBeniAssociatiConBulk(UserContext userContext, OggettoBulk buonoS, OggettoBulk fattura) throws ComponentException {
	Inventario_beni_apgHome benihome=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);//,"V_INVENTARIO_BENI_APG");
	SQLBuilder sql= benihome.createSQLBuilder();
	if (buonoS instanceof Ass_inv_bene_fatturaBulk){
		Ass_inv_bene_fatturaBulk ass=(Ass_inv_bene_fatturaBulk)buonoS;
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,ass.getLocal_transactionID());
	}else if(buonoS instanceof Buono_carico_scaricoBulk){
		Buono_carico_scaricoBulk ass=(Buono_carico_scaricoBulk)buonoS;
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,ass.getLocal_transactionID());
	}
	if (fattura != null && fattura instanceof Fattura_attiva_rigaIBulk ){
		Fattura_attiva_rigaIBulk riga= (Fattura_attiva_rigaIBulk)fattura;
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_fattura_attiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
	}
	if (fattura != null && fattura instanceof Nota_di_credito_rigaBulk ){
		Nota_di_credito_rigaBulk riga= (Nota_di_credito_rigaBulk)fattura;
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
	}
	if (fattura != null && fattura instanceof Fattura_passiva_rigaIBulk ){
			Fattura_passiva_rigaIBulk riga= (Fattura_passiva_rigaIBulk)fattura;
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
	}
	if (fattura != null && fattura instanceof Nota_di_debito_rigaBulk ){
			Nota_di_debito_rigaBulk riga= (Nota_di_debito_rigaBulk)fattura;
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
	}
	if (fattura != null && fattura instanceof Documento_generico_rigaBulk ){
		Documento_generico_rigaBulk riga= (Documento_generico_rigaBulk)fattura;
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,riga.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_documento_generico());
		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,riga.getCd_tipo_documento_amm());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());					
	}
	try{
		List beni=benihome.fetchAll(sql);
		for(Iterator i=beni.iterator();i.hasNext();){
			Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)i.next();
			bene_apg.setToBeDeleted();							
			super.eliminaConBulk(userContext,bene_apg);
		}		
	} catch (PersistencyException e) {
		throw handleException(e);
	} 
}

	/**
	  * Calcola il valore dell'Intervallo di ogni dettaglio.
	  *	 Dopo aver "esploso" i dettagli, (metodo esplodiDettagli), non è piè possibile aggregare
	  *	 i dettagli cosè come li aveva creati l'utente. Per dare la possibilitè di individuare
	  *	 le righe create da un singolo dettaglio, si imposta il valore INTERVALLO, che in pratica, 
	  *	 permette di individuare le righe carete da un singolo dettaglio. 
	  *	Per es:
	  *	 l'utente crea 2 righe:
	  *		- Dettaglio 1 - quantitè 2
	  *		- Dettaglio 2 - quantitè 1
	  *
	  *	 Dopo l'esplosione avremo una situazione del genere:
	  *		- Dettaglio 1 - quantitè 1 - intervallo 1-2
	  *		- Dettaglio 1 - quantitè 1 - intervallo 1-2
	  *		- Dettaglio 2 - quantitè 1 - intervallo 3-3
	  *
	  * @param quantita la <code>BigDecimal</code> quantitè espressa nel dettaglio.
	  *  
	  * @return intervallo <code>String</code> l'intervallo.
	**/
	private String calcolaIntervallo(java.math.BigDecimal quantita) {

		if (quantita == null)
			quantita = new java.math.BigDecimal(1);
		
		java.util.StringTokenizer token = new java.util.StringTokenizer(intervallo,"-",false);	 
		int intervalloMin = Integer.parseInt(token.nextToken());
		int intervalloMax = Integer.parseInt(token.nextToken());
	
		intervalloMin = intervalloMax + 1;
		intervalloMax = intervalloMax + quantita.intValue();
		intervallo = Integer.toString(intervalloMin) + "-" + Integer.toString(intervalloMax);		
	
		return intervallo;
	}
	/**
	  *  Ricerca di un Bene
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca di un Bene padre, ossia un bene che possa 
	  *		essere di riferimento per dei beni accessori.
	  *    PostCondition:
	  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	  *		clausole che il Bene NON sia un bene accessorio, (PROGRESSIVO = 0); che appartenga allo 
	  *		stesso Inventario associato alla UO di scrivania.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param dettaglio il <code>Buono_carico_dettBulk</code> dettaglio il cui bene sarà un bene accessorio.
	  * @param bene il <code>Inventario_beniBulk</code> Inventario_beniBulk modello.
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
public SQLBuilder selectBene_bene_principaleByClause(UserContext userContext, Buono_carico_scarico_dettBulk dettaglio, Inventario_beniBulk bene, CompoundFindClause clauses) 
		throws ComponentException
{
	try {
		Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk)getHome(userContext, dettaglio.getBene().getCategoria_Bene()).findByPrimaryKey(new Categoria_gruppo_inventBulk(dettaglio.getBene().getCategoria_Bene().getCd_categoria_gruppo()));
		SQLBuilder sql = getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();
		sql.addClause( clauses );		
		sql.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,"0");
		sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,dettaglio.getPg_inventario());
		sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
		sql.addSQLClause("AND","CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,cat.getCd_categoria_gruppo());	
		// Aggiunta clausola che visualizzi solo i beni che abbiano 
		//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
		sql.addClause("AND", "esercizio_carico_bene", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addOrderBy("NR_INVENTARIO");
		return sql;		
	} catch (PersistencyException e) {
		throw handleException(e);
	} 
}
	/**
	  *  Ricerca di un Nuovo Bene Padre
	  *    PreCondition:
	  *      Si sta effettuando una operazione di trasferimento di Inventario intra-Inventario, ossia
	  *		si sta trasferendo un bene accessorio da suo bene-padre ad un altro. E' stata generata la richiesta di ricerca 
	  *		del Nuovo Bene padre.
	  *    PostCondition:
	  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	  *		clausole che il Bene NON sia un bene accessorio, (PROGRESSIVO = 0); che appartenga allo 
	  *		stesso Inventario associato alla UO di scrivania.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bene il <code>Inventario_beniBulk</code> bene che si sta trasferendo.
	  * @param modelllo il <code>Inventario_beniBulk</code> Inventario_beniBulk modello.
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
public SQLBuilder selectNuovo_bene_padreByClause(UserContext userContext, Inventario_beniBulk bene, Inventario_beniBulk modello, CompoundFindClause clauses) 
		throws ComponentException
{			
		SQLBuilder sql = getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		sql.addTableToHeader("CATEGORIA_GRUPPO_INVENT");
		sql.addSQLJoin("INVENTARIO_BENI.CD_CATEGORIA_GRUPPO","CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO");
		sql.addSQLClause("AND","CATEGORIA_GRUPPO_INVENT.DATA_CANCELLAZIONE",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,"0");
		sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,bene.getPg_inventario());
		sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
		sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,bene.getCd_categoria_gruppo());
		
		// Escludo dalla selezione l'attuale bene padre
		sql.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.NOT_EQUALS,bene.getNr_inventario());
		// Aggiunta clausola che visualizzi solo i beni che abbiano 
		sql.addClause("AND", "esercizio_carico_bene", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addOrderBy("NR_INVENTARIO");
		return sql;		
}
/**
	  *  Ricerca di un CdR Utilizzatoreper
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca di un CdR Utilizzatore.
	  *    PostCondition:
	  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	  *		clausole che il CdR sia valido per l'esercizio di scrivania.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param vutilizzatore il <code>Utilizzatore_CdrVBulk</code> CdR di riferimento
	  * @param cdr il <code>CdrBulk</code> CdR modello  
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
	public SQLBuilder selectCdrByClause(UserContext userContext, Utilizzatore_CdrVBulk vutilizzatore, CdrBulk cdr, CompoundFindClause clauses) 
			throws ComponentException
	{
			SQLBuilder sql = getHome(userContext, CdrBulk.class).createSQLBuilder();
			sql.addClause( clauses );
			sql.addSQLClause("AND", "ESERCIZIO_INIZIO", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND", "ESERCIZIO_FINE", SQLBuilder.GREATER_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));	
			return sql;		
	}
	/**
	  *  Ricerca di una Linea di Attivitè per il CdR Utilizzatore
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca di una Linea di Attivitè per il
	  *		CdR Utilizzatore
	  *    PostCondition:
	  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	  *		clausole che la Linea di Attivitè appartenga al CdR indicato.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param utilizzatori_la il <code>Inventario_utilizzatori_laBulk</code> CdR di riferimento
	  * @param l_att la <code>Linea_attivitaBulk</code> Linea di Attivitè modello
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
	public SQLBuilder selectLinea_attivitaByClause(UserContext userContext, Inventario_utilizzatori_laBulk utilizzatori_la, it.cnr.contab.config00.latt.bulk.WorkpackageBulk l_att, CompoundFindClause clauses) 
			throws ComponentException
	{
		SQLBuilder sql = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		sql.addClause( clauses );
		sql.addTableToHeader("FUNZIONE,NATURA,PARAMETRI_CNR,UNITA_ORGANIZZATIVA,CDR");

		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause(FindClause.OR,"V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_SPESE);
		sql.addSQLClause(FindClause.OR,"V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
		sql.closeParenthesis();

		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",sql.EQUALS, utilizzatori_la.getCdr().getCd_centro_responsabilita());
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
		sql.addSQLJoin("FUNZIONE.CD_FUNZIONE",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE");
		sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
		sql.addSQLJoin("FUNZIONE.CD_FUNZIONE",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE");
		sql.addSQLClause("AND","PARAMETRI_CNR.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));	
		sql.addSQLClause("AND","FUNZIONE.FL_UTILIZZABILE",sql.EQUALS,"Y");
		sql.openParenthesis("AND");
		sql.addSQLClause("AND","NATURA.FL_SPESA",sql.EQUALS,"Y");
		sql.addSQLClause("OR","PARAMETRI_CNR.FL_REGOLAMENTO_2006", sql.EQUALS, "N");
		sql.addSQLClause("OR","UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_AREA);
		sql.closeParenthesis();
		sql.setDistinctClause(true);
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		if (config != null){
			sql.addSQLClause( "AND", "CD_LINEA_ATTIVITA",  sql.NOT_EQUALS, config.getVal01());
		}
		return sql;	
	}
	/** 
	  *  Cerca i Beni disponibili
	  *    PreCondition:
	  *      E' stata generata la richiesta di cercare tutti i beni disponibili per una operazione 
	  *		di associazione ad una riga di Fattura, o per una operazione di aumento di valore.
	  *    PostCondition:
	  *      Vengono cercati e proposti tutti i beni che abbiano i requisiti adatti per essere 
	  *		utilizzati. I beni dovranno appartenere all'Inventario associato alla UO di scrivania e
	  *		non dovranno essere già stati scaricati totalmente.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il Buono di Carico.
	  * @param no_accessori <code>boolean</code> il flag che determina se escludere i beni accessori.
	  * @param beni_da_escludere la <code>SimpleBulkList</code> lista di beni da escludere, (perchè già utilizzati nella sessione di lavoro).
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  * 
	  * @param iterator la <code>RemoteIterator</code> l'iteratore sui beni trovati.
	**/  
	public RemoteIterator getListaBeni(
		UserContext userContext, 
		OggettoBulk bulk, 
		boolean no_accessori, 
		SimpleBulkList beni_da_escludere,
		CompoundFindClause clauses) throws ComponentException{
		try {
			SQLBuilder sql = new SQLBuilder();
			Inventario_beniHome invBeniHome = (Inventario_beniHome)getHome(userContext, Inventario_beniBulk.class);
			if (bulk instanceof Buono_carico_scaricoBulk){
				sql = invBeniHome.getListaBeni(
					 userContext
					,(Buono_carico_scaricoBulk)bulk
					,no_accessori
					,beni_da_escludere);
			}
			else if (bulk instanceof Ass_inv_bene_fatturaBulk){
				sql = invBeniHome.getListaBeni(
					 userContext
					,((Ass_inv_bene_fatturaBulk)bulk).getInventario()
					,no_accessori
					,beni_da_escludere);
			}
			sql.addClause(clauses);
			return iterator(userContext,sql,Inventario_beniBulk.class,null);
		}catch(PersistencyException ex){
			throw handleException(ex);
		}catch(it.cnr.jada.persistency.IntrospectionException ex){
			throw handleException(ex);
		}
	}
//	 metodo che viene richiamato dalla setselection alla richiesta di aggiunta nuovi dettagli
//   sia per lo scarico che per il trasferimento serve solo per verificare che ci siano beni scaricabili
//   in realtè quando si effettua la ricerca viene richiama getListaBenidaScaricare
	public RemoteIterator cercaBeniScaricabili(it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buono, boolean no_accessori, SimpleBulkList beni_da_escludere,CompoundFindClause clauses) throws ComponentException {
		try{
			String nr_da_escludere = "";
			SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
			if (clauses!= null )
				sql.addClause(clauses);
			sql.addSQLClause("AND", "PG_INVENTARIO", SQLBuilder.EQUALS, buono.getInventario().getPg_inventario());	
			sql.addSQLClause("AND", "FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
			sql.addSQLClause("AND", "DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buono.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
		
			// Aggiunta clausola che visualizzi solo i beni che abbiano 
			//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
			sql.addSQLClause("AND", "ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

			if (no_accessori){
				sql.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS, "0");
			}
			
			if (beni_da_escludere != null && beni_da_escludere.size()>0){
				for (java.util.Iterator i = beni_da_escludere.iterator(); i.hasNext();){
					Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
					if (!nr_da_escludere.equals("")){
						nr_da_escludere = nr_da_escludere + ",";
					}			
					nr_da_escludere = nr_da_escludere + "('" + bene.getNr_inventario() + "','" + bene.getProgressivo() + "')";
				}
				sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
			}	
				// Se si tratta di un Trasferimento di tipo Intra UO, esclude dalla ricerca
				//	i beni eventualmente utilizzati come nuovi bene padre per i beni già selezionati.
				if (buono instanceof Trasferimento_inventarioBulk && ((Trasferimento_inventarioBulk)buono).isTrasferimentoIntraInv()){
					nr_da_escludere = "";
					Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
					SQLBuilder notExistsQuery=home.createSQLBuilder();
					notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO_PRINCIPALE IS NOT NULL");
					notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_PRINCIPALE IS NOT NULL");
					notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO_PRINCIPALE IS NOT NULL");
					notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buono.getLocal_transactionID());
					List beni = home.fetchAll(notExistsQuery);
					if (beni != null && beni.size()>0){
						for (java.util.Iterator iteratore = beni.iterator(); iteratore.hasNext();){
							Inventario_beni_apgBulk bene = (Inventario_beni_apgBulk)iteratore.next();
							if (!nr_da_escludere.equals("")){
								nr_da_escludere = nr_da_escludere + ",";
							}			
							nr_da_escludere = nr_da_escludere + "('" + bene.getNr_inventario_principale() + "','" + bene.getProgressivo_principale() + "')";
						}
						sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
					}
				}
			sql.addOrderBy("NR_INVENTARIO,PROGRESSIVO");
			return iterator(userContext,sql,Inventario_beniBulk.class,null);
		}catch(PersistencyException ex){
			throw handleException(ex);
		}
		
}	
	/** 
	  *  cerca tutti i beni associabili
	  *    PreCondition:
	  *      E' stata generata la richiesta di cercare tutti i beni che rispondono alle caratteristiche 
	  *		per essere associati alle righe di Fattura Passiva.
	  *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
	  *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
	  *			- NON sono stati già associati ad altre righe di Fattura durante la stessa sessione;
	  *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N');
	  *			- sono dello stesso tipo, (COMMERCIALE/ISTITUZIONALE), della riga di Fattura.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
	  *    PostCondition:
	  *     Viene costruito e restituito l'Iteratore sui beni disponibili.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> il Bulk principale per l'associazione
	  * @param riga_fattura <code>Fattura_passiva_rigaIBulk</code> la riga di Fattura selezionate dall'utente
	  *
	  * @return l'Iteratore <code>RemoteIterator</code> sulle righe caricate
	**/
	public RemoteIterator cercaBeniAssociabili(UserContext userContext,Ass_inv_bene_fatturaBulk associa_Bulk, Fattura_passiva_rigaIBulk riga_fattura,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
     if (!associa_Bulk.isPerAumentoValore()){
		SQLBuilder sql = getHome(userContext,Buono_carico_scarico_dettBulk.class).createSQLBuilder();
		if(clauses != null)
		    sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO"); 	 
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
		//R.P. Consente di associare piu' volte lo stesso bene
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
		sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
		//sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
		//PER NON VEDERE I BUONI TEMPORANEI GIA' GENERATI
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",SQLBuilder.GREATER,0);
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
		/* condizione spostata nella vista associazioni disponibili 
		SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
		sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
		sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_FATTURABILE",sql.EQUALS,"Y");
		sql.addSQLExistsClause("AND",sql_tipo);
		*/
		SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLNotExistsClause("AND",sql_old_ass);

		SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
		sql.addSQLExistsClause("AND",sql_ass);
		sql.setDistinctClause(true);
		return iterator(userContext,sql,Buono_carico_scarico_dettBulk.class,null);
	}else{
		SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
		if(clauses != null)
		    sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
		
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
		//R.P. Consente di associare piu' volte lo stesso bene
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
		sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
		sql.setDistinctClause(true);
		return iterator(userContext,sql,Inventario_beniBulk.class,null);	
	}
}	
	/** 
	  *  cerca tutti i beni associabili
	  *    PreCondition:
	  *      E' stata generata la richiesta di cercare tutti i beni che rispondono alle caratteristiche 
	  *		per essere associati alle righe di Fattura Passiva.
	  *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
	  *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
	  *			- NON sono stati già associati ad altre righe di Fattura durante la stessa sessione;
	  *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N');
	  *			- sono dello stesso tipo, (COMMERCIALE/ISTITUZIONALE), della riga di Fattura.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
	  *    PostCondition:
	  *     Viene costruito e restituito l'Iteratore sui beni disponibili.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> il Bulk principale per l'associazione
	  * @param riga_fattura <code>Fattura_passiva_rigaIBulk</code> la riga di Fattura selezionate dall'utente
	  *
	  * @return l'Iteratore <code>RemoteIterator</code> sulle righe caricate
	**/
	public RemoteIterator cercaBeniAssociabili(UserContext userContext,Ass_inv_bene_fatturaBulk associa_Bulk, Fattura_attiva_rigaIBulk riga_fattura,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
    	SQLBuilder sql = getHome(userContext,Buono_carico_scarico_dettBulk.class).createSQLBuilder();
		if(clauses != null)
		    sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO"); 	 
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
		//R.P. Consente di associare piu' volte lo stesso bene
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, TipoIVA.COMMERCIALE.value()); // Beni dello stesso tipo della riga di Fattura
		//RP IN QUESTO CASO DEVE PRENDERE ANCHE I TOTALMENTE SCARICATI
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISTOTALMENTESCARICATO); // scaricati totalmente
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
		//sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
		//PER NON VEDERE I BUONI TEMPORANEI GIA' GENERATI
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",SQLBuilder.GREATER,0);
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
		
		/* condizione spostata nella vista associazioni disponibili 
		SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
		sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
		sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
		sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_FATTURABILE",sql.EQUALS,"Y");
		sql.addSQLExistsClause("AND",sql_tipo);
		*/

		SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_ATT",sql.EQUALS,riga_fattura.getEsercizio());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_ATT",sql.EQUALS,riga_fattura.getCd_cds());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_ATT",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_ATTIVA",sql.NOT_EQUALS,riga_fattura.getPg_fattura_attiva());
		sql.addSQLNotExistsClause("AND",sql_old_ass);

		SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
		sql.addSQLExistsClause("AND",sql_ass);
		sql.setDistinctClause(true);
		return iterator(userContext,sql,Buono_carico_scarico_dettBulk.class,null);
}		
public RemoteIterator cercaBeniAssociabili(UserContext userContext,Ass_inv_bene_fatturaBulk associa_Bulk, Nota_di_credito_rigaBulk riga_fattura,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
  
			SQLBuilder sql = getHome(userContext,Buono_carico_scarico_dettBulk.class).createSQLBuilder();
			if(clauses != null)
			    sql.addClause(clauses);
			sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");//,ASS_INV_BENE_FATTURA");
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
			sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
			
			/* r.p. eliminato selezione bene associati alla fattura di origine
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getEsercizio());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_cds());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_unita_organizzativa());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getPg_fattura_passiva());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getProgressivo_riga());
			sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO"); // Questa OUT Join
			sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO"); //  permette di escludere	
			sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO"); 	 //  quei beni che sono stati già selezioanti
		    */
			sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
			//R.P. Consente di associare piu' volte lo stesso bene
			sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
			sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
			//R.P. IN QUESTO CASO DEVE PERMETTERE L'ASSOCIAZIONE ANCHE DEI BENI TOTALMENTE SCARICATI
			//sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, "N"); // Non scaricati totalmente
			sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
			sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
			//PER NON VEDERE I BUONI TEMPORANEI GIA' GENERATI
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",SQLBuilder.GREATER,0);
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
			
			/* condizione spostata nella vista associazioni disponibili 
			SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
			sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
			sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_FATTURABILE",sql.EQUALS,"Y");
			sql.addSQLExistsClause("AND",sql_tipo);
			*/

			SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
			sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
			sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
			sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
			sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLNotExistsClause("AND",sql_old_ass);

			SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
			sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
			sql.addSQLExistsClause("AND",sql_ass);
			sql.setDistinctClause(true);
			return iterator(userContext,sql,Buono_carico_scarico_dettBulk.class,null);
	}	
public RemoteIterator cercaBeniAssociabili(UserContext userContext,Ass_inv_bene_fatturaBulk associa_Bulk, Nota_di_debito_rigaBulk riga_fattura,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {

if (!associa_Bulk.isPerAumentoValore()){
	SQLBuilder sql = getHome(userContext,Buono_carico_scarico_dettBulk.class).createSQLBuilder();
	if(clauses != null)
	    sql.addClause(clauses);
	sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");//,ASS_INV_BENE_FATTURA");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
	
	/* r.p. eliminato selezione bene associati alla fattura di origine
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getEsercizio());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_cds());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_unita_organizzativa());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getPg_fattura_passiva());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getProgressivo_riga());
	sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO"); // Questa OUT Join
	sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO"); //  permette di escludere	
	sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO"); 	 //  quei beni che sono stati già selezioanti
	*/
	
	sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
	//R.P. Consente di associare piu' volte lo stesso bene
	sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
	sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
	sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
	sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
	sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
	//PER NON VEDERE I BUONI TEMPORANEI GIA' GENERATI
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",SQLBuilder.GREATER,0);
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
	
	/* condizione spostata nella vista associazioni disponibili 
	SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
	sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
	sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
	sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
	sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
	sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
	sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
	sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_FATTURABILE",sql.EQUALS,"Y");
	sql.addSQLExistsClause("AND",sql_tipo);
	*/

	SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
	sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
	sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
	sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
	sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
	sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,riga_fattura.getPg_fattura_passiva());
	sql.addSQLNotExistsClause("AND",sql_old_ass);

	SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
	sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
	sql.addSQLExistsClause("AND",sql_ass);
	sql.setDistinctClause(true);
	return iterator(userContext,sql,Buono_carico_scarico_dettBulk.class,null);
}else{
	SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
	if(clauses != null)
	    sql.addClause(clauses);
	sql.addTableToHeader("INVENTARIO_BENI_APG");//,ASS_INV_BENE_FATTURA");
	sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
	sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
	sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
	/* r.p. eliminato selezione bene associati alla fattura di origine
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getEsercizio());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_cds());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_unita_organizzativa());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getPg_fattura_passiva());
	sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getProgressivo_riga());
	sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO"); // Questa OUT Join
	sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO"); //  permette di escludere	
	sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO"); 	 //  quei beni che sono stati già selezioanti
	*/	
	
	sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
	//R.P. Consente di associare piu' volte lo stesso bene
	sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
	sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
	sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
	sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
	sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
	sql.setDistinctClause(true);
	return iterator(userContext,sql,Inventario_beniBulk.class,null);	
}
}
	/** 
	  *  Cerca tutti i beni associabili
	  *    PreCondition:
	  *      E' stata generata la richiesta di cercare tutti i beni che rispondono alle caratteristiche 
	  *		per essere associati alle righe di Fattura Attiva.
	  *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
	  *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
	  *			- NON sono stati già associati ad altre righe di Fattura durante la stessa sessione;
	  *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N');
	  *			- sono dello stesso tipo, (COMMERCIALE/ISTITUZIONALE), della riga di Fattura.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
	  *    PostCondition:
	  *     Viene costruito e restituito l'Iteratore sui beni disponibili.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
	  * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
	  * @param riga_fattura <code>Fattura_passiva_rigaIBulk</code> la riga di Fattura selezionate dall'utente  
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
	**/
	public RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Fattura_attiva_rigaIBulk riga_fattura, CompoundFindClause clauses) throws ComponentException {
		SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
		sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già selezioanti
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, buonoS.getLocal_transactionID());  // nella transazione attuale
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, TipoIVA.COMMERCIALE.value()); // Beni dello stesso tipo della riga di Fattura
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());	
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
		sql.addSQLClause("AND", "INVENTARIO_BENI.PROGRESSIVO", SQLBuilder.EQUALS, "0"); // Solo i beni Prinicapali
		sql.addSQLClause("AND", "INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buonoS.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
		// Aggiunta clausola che visualizzi solo i beni che abbiano 
		//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		return iterator(userContext,sql,Inventario_beniBulk.class,null);		
	}
	
	/** 
	  *  Cerca tutti i beni associabili
	  *    PreCondition:
	  *      E' stata generata la richiesta di cercare tutti i beni che rispondono alle caratteristiche 
	  *		per essere associati alle righe di Fattura Attiva.
	  *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
	  *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
	  *			- NON sono stati già associati ad altre righe di Fattura durante la stessa sessione;
	  *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N');
	  *			- sono dello stesso tipo, (COMMERCIALE/ISTITUZIONALE), della riga di Fattura.
	  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
	  *    PostCondition:
	  *     Viene costruito e restituito l'Iteratore sui beni disponibili.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
	  * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
	  * @param riga_fattura <code>Fattura_passiva_rigaIBulk</code> la riga di Fattura selezionate dall'utente  
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
	  *
	  * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
	**/
	public RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Nota_di_credito_rigaBulk riga_fattura, CompoundFindClause clauses) throws ComponentException {
		SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
		sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG");//,ASS_INV_BENE_FATTURA");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già selezioanti
		
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, buonoS.getLocal_transactionID());  // nella transazione attuale
//		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS,riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
		/* r.p. eliminato selezione bene associati alla fattura di origine
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getEsercizio());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_cds());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getCd_unita_organizzativa());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getPg_fattura_passiva());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getRiga_fattura_origine().getProgressivo_riga());
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO"); // Questa OUT Join
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO"); //  permette di escludere	
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO"); 	 //  quei beni che sono stati già selezioanti
		*/
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());	
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
		//sql.addSQLClause("AND", "INVENTARIO_BENI.PROGRESSIVO", SQLBuilder.EQUALS, "0"); // Solo i beni Prinicapali
		sql.addSQLClause("AND", "INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buonoS.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
		sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,riga_fattura.getBene_servizio().getCategoria_gruppo().getCd_categoria_gruppo());
		// Aggiunta clausola che visualizzi solo i beni che abbiano 
		//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return iterator(userContext,sql,Inventario_beniBulk.class,null);		
	}
	/** 
	  * Scarica tutti i beni disponibili.
	  *    PreCondition:
	  *      E' stata generata la richiesta di scaricare tutti i beni disponibili per lo scarico.
	  *    PostCondition:
	  *		Se l'utente ha specificato delle righe di fattura attiva, i beni saranno associati ad esse.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param buonoS il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
	  * @param selectedRighe_fattura la <code>List</code> lista eventuale di righe di Fattura Attiva 
	  *		alle quali saranno associati i beni scaricati.
	  * @param clauses le <code>CompoundFindClause</code> clausole aggiunte dall'utente.
	**/ 
	public void scaricaTuttiBeni(UserContext userContext,Buono_carico_scaricoBulk buonoS, java.util.List selectedRighe_fattura, CompoundFindClause clauses) throws ComponentException {

		if (buonoS instanceof Trasferimento_inventarioBulk){
			scaricaTuttiBeniPerTrasferimento(userContext, buonoS, clauses);
			return;
		}
		else if (selectedRighe_fattura != null){
			scaricaTuttiBeniDaFattura(userContext, buonoS, selectedRighe_fattura, clauses);
			return;
		}   
	try {
		    Inventario_beniHome homebeni =(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class); 
			SQLBuilder sql = homebeni.createSQLBuilder();
			
			sql.addClause(clauses);
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.CD_CDS",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			//sql.addSQLClause("AND","INVENTARIO_BENI.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,buonoS.getData_registrazione());
			
			SQLBuilder sql_notExists = getHome(userContext,Inventario_beni_apgBulk.class).createSQLBuilder();
			sql_notExists.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)");
			sql_notExists.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)");
			sql_notExists.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)");
			sql_notExists.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
			sql.addSQLNotExistsClause("AND", sql_notExists);
			// Locka le righe
			sql.setForUpdate(true);
			List beni = homebeni.fetchAll(sql);
			
			for(Iterator i=beni.iterator();i.hasNext();){
				Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
				
				Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
				new_bene_apg.setPg_inventario(bene.getPg_inventario());
				new_bene_apg.setNr_inventario(bene.getNr_inventario());
				new_bene_apg.setProgressivo(bene.getProgressivo());
				new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
				new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
				if(buonoS.getTipoMovimento()!=null && buonoS.getTipoMovimento().getFl_vendita().booleanValue()){
					new_bene_apg.setVariazione_meno(bene.getValoreBene());
					new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
					new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
				}
				else{	
					new_bene_apg.setVariazione_meno(new java.math.BigDecimal(0));
					new_bene_apg.setFl_totalmente_scaricato(new Boolean(false));
				}
				new_bene_apg.setTi_documento(buonoS.getTi_documento());
				new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
				new_bene_apg.setToBeCreated();	
				super.creaConBulk(userContext,new_bene_apg);
			}
	} catch (PersistencyException e) {
		throw  handleException(e);
	}
}
	public void scaricaTuttiBeni(UserContext userContext,Buono_carico_scaricoBulk buonoS, java.util.List selectedRighe_fattura) throws ComponentException {
		scaricaTuttiBeni(userContext,buonoS,selectedRighe_fattura,null);
		}
	/** 
	  *  Inserisce i beni temporanei.
	  *    PreCondition:
	  *      E' stata generata la richiesta di riportare i beni selezionati dall'utente nella tabella 
	  *		temporanea INVENTARIO_BENI_APG. L'utente, in questa fase, si trova a selezionare dei 
	  *		beni già presenti sul DB, per una operazione di scarico.
	  *    PostCondition:
	  *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi ai beni selezionati dall'utente.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
	  * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
	  * @param old_ass la <code>BitSet</code> selezione precedente.
	  * @param ass la <code>BitSet</code> selezione attuale.
	**/
	public void modificaBeniScaricati(UserContext userContext,Buono_carico_scaricoBulk buonoS, OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException {
		// Si sta facendo una operazione di Trasferimento
		if (buonoS instanceof Trasferimento_inventarioBulk){
			modificaBeniScaricatiPerTrasferimento(userContext, buonoS, beni, old_ass, ass);
		} 
		else {
			/* Questo metodo viene richiamato tutte le volte che c'è un cambio di pagina del selezionatore
			 * dei beni, oppure una richiesta di Riporta.
		    */
			try {
				for (int i = 0;i < beni.length;i++) {
					Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];
					if (old_ass.get(i) != ass.get(i)) {
							
						if (ass.get(i)) {		
							// Locko il bene che è stato selezionato per essere scaricato.
							try{
								lockBulk(userContext, bene)	;
							} catch (OutdatedResourceException oe){
								throw handleException(oe);
							} catch (BusyResourceException bre){
								throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
							} catch (it.cnr.jada.persistency.PersistencyException pe){
								throw handleException(pe);
							} 
							Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
							new_bene_apg.setPg_inventario(bene.getPg_inventario());
							new_bene_apg.setNr_inventario(bene.getNr_inventario());
							new_bene_apg.setProgressivo(bene.getProgressivo());
							new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
							new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
							if(buonoS.getTipoMovimento()!=null && buonoS.getTipoMovimento().getFl_vendita().booleanValue()){
								new_bene_apg.setVariazione_meno(bene.getValoreBene());
								new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
								new_bene_apg.setFl_totalmente_scaricato(true);
							}
							else{	
								new_bene_apg.setVariazione_meno(new java.math.BigDecimal(0));
								new_bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
							}
							new_bene_apg.setTi_documento(buonoS.getTi_documento());
							new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
							new_bene_apg.setToBeCreated();							
							super.creaConBulk(userContext,new_bene_apg);
						} else {
							Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
							SQLBuilder sql= home.createSQLBuilder();
							sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
							sql.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS, bene.getNr_inventario());
							sql.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,bene.getProgressivo());
							sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
						    List beni_canc = home.fetchAll(sql); 
							for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
								Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
								new_bene_apg.setToBeDeleted();							
								super.eliminaConBulk(userContext,new_bene_apg);
							}								
						}
					}
				}
				
			} catch(Throwable e) {
				throw handleException(e);			
			}
		}
	}
public RemoteIterator getListaBeniDaScaricare(
			UserContext userContext, 
			OggettoBulk bulk, 
			boolean no_accessori, 
			SimpleBulkList beni_da_escludere,
			CompoundFindClause clauses) throws ComponentException{

			try {
				SQLBuilder sql = new SQLBuilder();
				Inventario_beniHome invBeniHome = (Inventario_beniHome)getHome(userContext, Inventario_beniBulk.class);
				if (bulk instanceof Buono_carico_scaricoBulk){
					sql = invBeniHome.getListaBeniDaScaricare(
						 userContext
						,(Buono_carico_scaricoBulk)bulk
						,no_accessori
						,beni_da_escludere);
				}
				else if (bulk instanceof Ass_inv_bene_fatturaBulk){
					sql = invBeniHome.getListaBeniDaScaricare(
						 userContext
						,((Ass_inv_bene_fatturaBulk)bulk).getInventario()
						,no_accessori
						,beni_da_escludere);
				}
					
				sql.addClause(clauses);
				return iterator(userContext,sql,Inventario_beniBulk.class,null);
			}catch(PersistencyException ex){
				throw handleException(ex);
			}catch(it.cnr.jada.persistency.IntrospectionException ex){
				throw handleException(ex);
			}
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa per una operazione di Trasferimento di inventario
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUo_destinazioneByClause(UserContext userContext, Trasferimento_inventarioBulk bulk, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {


	// Utilizza la vista V_UNITA_ORGANIZZATIVA_VALIDA per selezionare le UO valide per l'esercizio di scrivania
	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class, "V_UNITA_ORGANIZZATIVA_VALIDA"); 

	// Il metodo <code>createSQLBuilder()</code> di Unita_organizzativaHome impone delle clausole
	//	che escludono dalla select le UO CDS, e la UO di tipo ENTE
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	// Aggiungo la clausola che escluda dalla selezione l' UO di scrivania: non avrebbe senso,
	//	infatti, trasferire i beni nel proprio Inventario.
	sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.NOT_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));

	sql.addClause(clauses);
	return sql;
}
/** 
  *  Seleziona beni scaricati
  *    PreCondition:
  *      E' stata generata la richiesta di cercare i beni scaricati.
  *    PostCondition:
  *      Viene restituito un Iteratore sui beni presenti sulla tabella INVENTARIO_BENI_APG: se il Buono  
  *		di Scarico è generato da una Fattura Attiva, visualizza i beni associati alla riga di fattura indicata.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
  * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di fattura.
  * @param bulkClass la <code>Class</code> modello per il dettaglio.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
public RemoteIterator selectBeniAssociatiByClause(
	UserContext userContext,
	Buono_carico_scaricoBulk buonoS,
	OggettoBulk oggetto, 
	Class bulkClass, 
	CompoundFindClause clauses) throws ComponentException 
{
	SQLBuilder sql = null;	
	if (buonoS.getPg_buono_c_s() != null && buonoS.getPg_buono_c_s() >0){
		sql = getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();		
		sql.addTableToHeader("BUONO_CARICO_SCARICO_DETT");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO",SQLBuilder.EQUALS,"BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO",SQLBuilder.EQUALS,"BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO");
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO",SQLBuilder.EQUALS,"BUONO_CARICO_SCARICO_DETT.PROGRESSIVO");
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",SQLBuilder.EQUALS,buonoS.getPg_inventario());
		sql.addSQLClause("AND","TI_DOCUMENTO",SQLBuilder.EQUALS,buonoS.getTi_documento());
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,buonoS.getEsercizio());
		sql.addSQLClause("AND","PG_BUONO_C_S",SQLBuilder.EQUALS,buonoS.getPg_buono_c_s());
	} else {
		sql = getHome(userContext, Inventario_beniBulk.class,"V_INVENTARIO_BENI_APG").createSQLBuilder();
		if (oggetto != null && oggetto instanceof Fattura_attiva_rigaIBulk){
			Fattura_attiva_rigaIBulk riga_fattura=(Fattura_attiva_rigaIBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_attiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
		}else if(oggetto != null && oggetto instanceof Nota_di_credito_rigaBulk){
			Nota_di_credito_rigaBulk riga_fattura=(Nota_di_credito_rigaBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
		}else if(oggetto != null && oggetto instanceof Documento_generico_rigaBulk){
			Documento_generico_rigaBulk riga=(Documento_generico_rigaBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga.getCd_cds());
			sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga.getPg_documento_generico());
			sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,riga.getCd_tipo_documento_amm());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga.getProgressivo_riga());
		}
		
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
	}
	sql.addOrderBy("NR_INVENTARIO");
	sql.addOrderBy("PROGRESSIVO");	
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
	return ri;
}

/** 
 *  Seleziona beni associati
 *    PreCondition:
 *      E' stata generata la richiesta di cercare i beni associati alla riga di fattura passiva indicata.
 *    PostCondition:
 *      Viene restituito un Iteratore sui beni presenti sulla tabella INVENTARIO_BENI_APG, associati 
 *		alla riga di fattura indicata.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> l'oggetto che contiene le informazioni 
 *		relative all'Inventario di riferimento ed alle righe di Fattura Passive.
 * 
 * @param riga_fattura la <code>Fattura_passiva_rigaIBulk</code> riga di fattura passiva.
 * @param bulkClass la <code>Class</code> modello per il dettaglio.
 * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
 *
 * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
public RemoteIterator selectBeniAssociatiByClause(
	UserContext userContext,
	Ass_inv_bene_fatturaBulk associaBulk,
	OggettoBulk oggetto, 
	Class bulkClass, 
	CompoundFindClause clauses) throws ComponentException 
{

	if (oggetto == null)
		return null;
	Fattura_passiva_rigaIBulk riga_fattura=null;
	Nota_di_credito_rigaBulk nota=null;
	Nota_di_debito_rigaBulk notadeb=null;
	Fattura_attiva_rigaIBulk fattura_attiva=null;
	Documento_generico_rigaBulk riga_doc=null;
	if (associaBulk.isPerAumentoValore()||(associaBulk.isPerAumentoValoreDoc())){
		return selectBeniAssociatiPerAumento(userContext, associaBulk, oggetto, bulkClass, clauses);
	}else 
	if (oggetto instanceof Fattura_passiva_rigaIBulk)
		riga_fattura = (Fattura_passiva_rigaIBulk)oggetto;
	else if (oggetto instanceof Nota_di_credito_rigaBulk)
		nota = (Nota_di_credito_rigaBulk)oggetto;
	else if (oggetto instanceof Nota_di_debito_rigaBulk)
		notadeb = (Nota_di_debito_rigaBulk)oggetto;
	else if (oggetto instanceof Fattura_attiva_rigaIBulk)
		fattura_attiva = (Fattura_attiva_rigaIBulk)oggetto;
	else if (oggetto instanceof Documento_generico_rigaBulk)
		riga_doc = (Documento_generico_rigaBulk)oggetto;
			
	SQLBuilder sql = getHome(userContext, Buono_carico_scarico_dettBulk.class).createSQLBuilder();
	sql.addTableToHeader("INVENTARIO_BENI_APG");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","INVENTARIO_BENI_APG.ESERCIZIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","INVENTARIO_BENI_APG.TI_DOCUMENTO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","INVENTARIO_BENI_APG.PG_BUONO_C_S");
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,associaBulk.getLocal_transactionID());
	if (clauses!=null)
		sql.addClause(clauses);
	if (riga_fattura !=null){
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
	}
	else if(nota!=null){
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",SQLBuilder.EQUALS,nota.getCd_cds());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,nota.getCd_unita_organizzativa());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",SQLBuilder.EQUALS,nota.getEsercizio());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",SQLBuilder.EQUALS,nota.getPg_fattura_passiva());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",SQLBuilder.EQUALS,nota.getProgressivo_riga());
		
	}else if(notadeb!=null){
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",SQLBuilder.EQUALS,notadeb.getCd_cds());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,notadeb.getCd_unita_organizzativa());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",SQLBuilder.EQUALS,notadeb.getEsercizio());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",SQLBuilder.EQUALS,notadeb.getPg_fattura_passiva());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",SQLBuilder.EQUALS,notadeb.getProgressivo_riga());
		
	}else if(fattura_attiva!=null){
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",SQLBuilder.EQUALS,fattura_attiva.getCd_cds());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,fattura_attiva.getCd_unita_organizzativa());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",SQLBuilder.EQUALS,fattura_attiva.getEsercizio());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",SQLBuilder.EQUALS,fattura_attiva.getPg_fattura_attiva());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",SQLBuilder.EQUALS,fattura_attiva.getProgressivo_riga());
		
	}else if(riga_doc!=null){
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",SQLBuilder.EQUALS,riga_doc.getCd_cds());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riga_doc.getCd_unita_organizzativa());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",SQLBuilder.EQUALS,riga_doc.getEsercizio());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",SQLBuilder.EQUALS,riga_doc.getPg_documento_generico());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,riga_doc.getCd_tipo_documento_amm());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_doc.getProgressivo_riga());
		
	}	
	return iterator(userContext,sql,bulkClass,null);
}

/* metodo che viene richiamato su ogni riga in fase di modifica delle associazioni
 * quando viene selezionata la riga di fattura */
public void selectBeniAssociatiForModifica(
		UserContext userContext,
		Ass_inv_bene_fatturaBulk associaBulk,
		OggettoBulk oggetto) throws ComponentException
{
	try {
		Nota_di_credito_rigaBulk nota=null;
		Nota_di_debito_rigaBulk notadeb=null;
		Fattura_passiva_rigaIBulk riga_fattura =null;
		Fattura_attiva_rigaIBulk fattura_attiva =null;
		Documento_generico_rigaBulk doc_riga =null;
		Ass_inv_bene_fatturaHome home =(Ass_inv_bene_fatturaHome) getHome(userContext, Ass_inv_bene_fatturaBulk.class);
		SQLBuilder sql =home.createSQLBuilder();
		sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)");
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)");
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)");
		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
		if (oggetto!=null && oggetto instanceof Fattura_passiva_rigaIBulk){
			riga_fattura =(Fattura_passiva_rigaIBulk)oggetto;
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
		}else if (oggetto!=null && oggetto instanceof Nota_di_credito_rigaBulk){
			nota =(Nota_di_credito_rigaBulk)oggetto;
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,nota.getCd_cds());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,nota.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,nota.getEsercizio());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,nota.getPg_fattura_passiva());
			sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,nota.getProgressivo_riga());
		}
	else if (oggetto!=null && oggetto instanceof Nota_di_debito_rigaBulk){
		notadeb =(Nota_di_debito_rigaBulk)oggetto;
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,notadeb.getCd_cds());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,notadeb.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,notadeb.getEsercizio());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,notadeb.getPg_fattura_passiva());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,notadeb.getProgressivo_riga());
	}else if (oggetto!=null && oggetto instanceof Fattura_attiva_rigaIBulk){
		fattura_attiva =(Fattura_attiva_rigaIBulk)oggetto;
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_ATT",SQLBuilder.EQUALS,fattura_attiva.getCd_cds());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_ATT",SQLBuilder.EQUALS,fattura_attiva.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_ATT",SQLBuilder.EQUALS,fattura_attiva.getEsercizio());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_ATTIVA",SQLBuilder.EQUALS,fattura_attiva.getPg_fattura_attiva());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_ATT",SQLBuilder.EQUALS,fattura_attiva.getProgressivo_riga());
	}else if (oggetto!=null && oggetto instanceof Documento_generico_rigaBulk){
		doc_riga =(Documento_generico_rigaBulk)oggetto;
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_DOC_GEN",SQLBuilder.EQUALS,doc_riga.getCd_cds());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_DOC_GEN",SQLBuilder.EQUALS,doc_riga.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_DOC_GEN",SQLBuilder.EQUALS,doc_riga.getEsercizio());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,doc_riga.getCd_tipo_documento_amm());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_DOCUMENTO_GENERICO",SQLBuilder.EQUALS,doc_riga.getPg_documento_generico());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_DOC_GEN",SQLBuilder.EQUALS,doc_riga.getProgressivo_riga());
	}
		sql.setDistinctClause(true);
		List buoni_ass=home.fetchAll(sql);
		for (Iterator i=buoni_ass.iterator();i.hasNext();){
			Ass_inv_bene_fatturaBulk ass=(Ass_inv_bene_fatturaBulk)i.next();
			Inventario_beni_apgBulk new_bene = new Inventario_beni_apgBulk(); 
			BigDecimal im_riga_fattura=new java.math.BigDecimal(0);
			
			new_bene.setNr_inventario(ass.getNr_inventario());
			if (ass.getTest_buono()== null)
				new_bene.setPg_inventario(associaBulk.getInventario().getPg_inventario());
			else
				new_bene.setPg_inventario(ass.getPg_inventario());
			
			new_bene.setProgressivo(ass.getProgressivo());
			new_bene.setTi_documento(ass.getTi_documento());
			new_bene.setPg_buono_c_s(ass.getPg_buono_c_s());
		
			if (riga_fattura!=null){
				new_bene.setCd_cds(riga_fattura.getCd_cds());
				new_bene.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
				new_bene.setEsercizio(riga_fattura.getEsercizio());
				new_bene.setPg_fattura(riga_fattura.getPg_fattura_passiva());
				new_bene.setProgressivo_riga(riga_fattura.getProgressivo_riga());
				if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
					im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
				else
					im_riga_fattura = riga_fattura.getIm_imponibile();
			}else if(nota!=null){
				new_bene.setCd_cds(nota.getCd_cds());
				new_bene.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());
				new_bene.setEsercizio(nota.getEsercizio());
				new_bene.setPg_fattura(nota.getPg_fattura_passiva());
				new_bene.setProgressivo_riga(nota.getProgressivo_riga());
				if (nota.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
					im_riga_fattura = nota.getIm_imponibile().add(nota.getIm_iva());
				else
					im_riga_fattura = nota.getIm_imponibile();
			}else if(notadeb!=null){
				new_bene.setCd_cds(notadeb.getCd_cds());
				new_bene.setCd_unita_organizzativa(notadeb.getCd_unita_organizzativa());
				new_bene.setEsercizio(notadeb.getEsercizio());
				new_bene.setPg_fattura(notadeb.getPg_fattura_passiva());
				new_bene.setProgressivo_riga(notadeb.getProgressivo_riga());
				if (notadeb.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
					im_riga_fattura = notadeb.getIm_imponibile().add(notadeb.getIm_iva());
				else
					im_riga_fattura = notadeb.getIm_imponibile();
			}else if(fattura_attiva!=null){
				new_bene.setCd_cds(fattura_attiva.getCd_cds());
				new_bene.setCd_unita_organizzativa(fattura_attiva.getCd_unita_organizzativa());
				new_bene.setEsercizio(fattura_attiva.getEsercizio());
				new_bene.setPg_fattura(fattura_attiva.getPg_fattura_attiva());
				new_bene.setProgressivo_riga(fattura_attiva.getProgressivo_riga());
				im_riga_fattura = fattura_attiva.getIm_imponibile();
			}else if(doc_riga!=null){
				new_bene.setCd_cds(doc_riga.getCd_cds());
				new_bene.setCd_unita_organizzativa(doc_riga.getCd_unita_organizzativa());
				new_bene.setEsercizio(doc_riga.getEsercizio());
				new_bene.setPg_fattura(doc_riga.getPg_documento_generico());
				new_bene.setProgressivo_riga(doc_riga.getProgressivo_riga());
				new_bene.setCd_tipo_documento_amm(doc_riga.getCd_tipo_documento_amm());
				im_riga_fattura = doc_riga.getIm_riga();
			}
			new_bene.setLocal_transaction_id(associaBulk.getLocal_transactionID());
			Buono_carico_scarico_dettBulk dett =(Buono_carico_scarico_dettBulk)getHome(userContext,Buono_carico_scarico_dettBulk.class).findByPrimaryKey(new Buono_carico_scarico_dettBulk(ass.getPg_inventario(),ass.getTi_documento(),ass.getEsercizio(),ass.getPg_buono_c_s(),ass.getNr_inventario(),new Integer(ass.getProgressivo().intValue())));
			dett.setBene((Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(dett.getBene()));
			if((fattura_attiva!=null)||(doc_riga!=null && doc_riga.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.ENTRATE)){
				new_bene.setValore_alienazione(dett.getBene().getValore_alienazione());
			}else{ 
				BigDecimal tot_bene_apg=new BigDecimal(0);
				BigDecimal tot_ass_apg=new BigDecimal(0);
				Inventario_beni_apgHome home_inv=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
				SQLBuilder sql_old= home_inv.createSQLBuilder();
				sql_old.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
				sql_old.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, dett.getNr_inventario());
				sql_old.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,dett.getProgressivo());
				sql_old.addSQLClause("AND","ESERCIZIO",sql.EQUALS,dett.getEsercizio());
				sql_old.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,dett.getTi_documento());
				sql_old.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,dett.getPg_buono_c_s());
				sql_old.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
				List apg =home_inv.fetchAll(sql_old);
				for (Iterator iterator = apg.iterator();iterator.hasNext();){
					Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
					tot_bene_apg = tot_bene_apg.add(bene_apg.getValore_alienazione());							
				}		
				SQLBuilder sql_fatt= home_inv.createSQLBuilder();
				if(doc_riga!=null){
					sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,doc_riga.getEsercizio());
					sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, doc_riga.getCd_cds());
					sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,doc_riga.getCd_unita_organizzativa());
					sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,doc_riga.getPg_documento_generico());
					sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,doc_riga.getProgressivo_riga());
					sql_fatt.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,doc_riga.getCd_tipo_documento_amm());												
				}else if(riga_fattura!=null){
					sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
					sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
					sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
					sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
					sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
				}else if(nota!=null){
					sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,nota.getEsercizio());
					sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, nota.getCd_cds());
					sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
					sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
					sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());			
				}else if(notadeb!=null){
					sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
					sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, notadeb.getCd_cds());
					sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
					sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
					sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());		
				}
				sql_fatt.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
				apg =home_inv.fetchAll( sql_fatt);
				for (Iterator iterator = apg.iterator();iterator.hasNext();){
					Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
					tot_ass_apg = tot_ass_apg.add(bene_apg.getValore_alienazione());							
				}	
				if ((dett.getValore_unitario().subtract(tot_bene_apg)).compareTo(im_riga_fattura.subtract(tot_ass_apg))>0)
					new_bene.setValore_alienazione(im_riga_fattura.subtract(tot_ass_apg));
				else
					new_bene.setValore_alienazione(dett.getValore_unitario().subtract(tot_bene_apg));
			
				/*if (dett.getValore_unitario().compareTo(im_riga_fattura)>0)
					new_bene.setValore_alienazione(im_riga_fattura);
				else
					new_bene.setValore_alienazione(dett.getValore_unitario());*/
			}	
			new_bene.setImp_fattura(im_riga_fattura);
			new_bene.setToBeCreated();	
			super.creaConBulk(userContext,new_bene);
				
			ass.setToBeDeleted();							
			super.eliminaConBulk(userContext,ass);
	
		}		
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}
/** 
 *  Seleziona beni associati
 *    PreCondition:
 *      E' stata generata la richiesta di cercare i beni assciati ad una data riga di Fattura Passiva:
 *			la fattura è <code>Per Aumento di Valore</code>.
 *    PostCondition:
 *      Viene restituito un Iteratore sui beni presenti sulla tabella INVENTARIO_BENI_APG,
 *			(tramite la vista V_INVENTARIO_BENI_APG).
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_caricoBulk</code> Buono di Carico.
 * @param riga_fattura la <code>Fattura_passiva_rigaIBulk</code> riga di fattura.
 * @param bulkClass la <code>Class</code> modello per il dettaglio.
 * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
 *
 * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
private RemoteIterator selectBeniAssociatiPerAumento(
	UserContext userContext,
	Ass_inv_bene_fatturaBulk associa,
	OggettoBulk oggetto,
	Class bulkClass, 
	CompoundFindClause clauses) throws ComponentException 
{
	SQLBuilder sql = getHome(userContext, bulkClass,"V_INVENTARIO_BENI_APG").createSQLBuilder();
			
	if (oggetto != null && oggetto instanceof Fattura_passiva_rigaIBulk){
		Fattura_passiva_rigaIBulk riga_fattura =(Fattura_passiva_rigaIBulk)oggetto;
		sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
	}else if (oggetto != null && oggetto instanceof Nota_di_debito_rigaBulk){
		Nota_di_debito_rigaBulk riga_fattura =(Nota_di_debito_rigaBulk)oggetto;
		sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_fattura.getProgressivo_riga());
	}
    else if (oggetto != null && oggetto instanceof Documento_generico_rigaBulk){ 
    	Documento_generico_rigaBulk riga_doc=(Documento_generico_rigaBulk)oggetto;
		sql.addSQLClause("AND","CD_CDS_DOC_AMM",SQLBuilder.EQUALS,riga_doc.getCd_cds());
		sql.addSQLClause("AND","CD_UO_DOC_AMM",SQLBuilder.EQUALS,riga_doc.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,riga_doc.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,riga_doc.getPg_documento_generico());
		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,riga_doc.getCd_tipo_documento_amm());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",SQLBuilder.EQUALS,riga_doc.getProgressivo_riga());
    }
	sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,associa.getLocal_transactionID());
	sql.addOrderBy("NR_INVENTARIO");
	sql.addOrderBy("PROGRESSIVO");	

	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
	return ri;	
}

/** 
 *  Controlla i beni già scaricati durante la sessione di lavoro - Sono presenti beni accessori del bene indicato
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha degli accessori. 
 *		L'utente ha già indicato, in questo Buono di Scarico, dei beni che sono accessori del
 *		bene che si sta tentando di scaricare totalmente.
 *    PostCondition:
 *      Viene presentato un messaggio di errore all'utente che spiega l'impossibilitè di scaricare
 *		totalmente un bene se si sono già scaricati dei beni che sono accessori del bene indicato.
 *
 *  Controlla i beni già scaricati durante la sessione di lavoro - NON sono presenti beni accessori del bene indicato
 *    PreCondition:
 *      Tutti i controlli superati.
 *    PostCondition:
 *      Il bene indicato viene registrato come totalmente scaricato e si puè proseguire con
 *		le normali operazioni.
 *  
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoS <code>Buono_carico_scaricoBulk</code> il Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene da scaricare totalmente.
 * @throws ApplicationException
**/
public void checkBeniAccessoriAlreadyExistFor(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene_padre) throws ComponentException  {
	try{
		SQLBuilder sql=getHome(userContext, Inventario_beni_apgBulk.class).createSQLBuilder();
		if (buonoS.getPg_buono_c_s()!=null && buonoS.getPg_buono_c_s()>0){
			sql.addTableToHeader("BUONO_CARICO_SCARICO");
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_INVENTARIO", SQLBuilder.EQUALS, buonoS.getPg_inventario());
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO.ESERCIZIO", SQLBuilder.EQUALS, buonoS.getEsercizio());
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO.TI_DOCUMENTO", SQLBuilder.EQUALS, buonoS.getTi_documento());
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_BUONO_C_S", SQLBuilder.EQUALS, buonoS.getPg_buono_c_s());
		}
		if (buonoS.isByFattura()||buonoS.isByDocumento()){		
				SQLBuilder sql_acc_non=getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();
				sql_acc_non.addTableToHeader("INVENTARIO_BENI_APG");
				sql_acc_non.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO");
				sql_acc_non.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO");
				sql_acc_non.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO");
				sql_acc_non.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
				sql_acc_non.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,bene_padre.ISNOTTOTALMENTESCARICATO );
				sql_acc_non.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, bene_padre.getPg_inventario());
				sql_acc_non.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO", SQLBuilder.EQUALS, bene_padre.getNr_inventario());
				sql_acc_non.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO", SQLBuilder.NOT_EQUALS,"0");
				
				if (sql_acc_non.executeCountQuery(getConnection(userContext))>0)
					throw new it.cnr.jada.comp.ApplicationException ("Attenzione: non è possibile scaricare totalmente questo bene\n poichè, bisogna scaricare prima i suoi accessori");
				else{
					
					SQLBuilder sql_acc=getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();
					sql_acc.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, bene_padre.getPg_inventario());
					sql_acc.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO", SQLBuilder.EQUALS, bene_padre.getNr_inventario());
					sql_acc.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO", SQLBuilder.NOT_EQUALS,"0");
					sql_acc.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS,bene_padre.ISNOTTOTALMENTESCARICATO);
					
					SQLBuilder sql_non=getHome(userContext, Inventario_beni_apgBulk.class).createSQLBuilder();
					sql_non.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO");
					sql_non.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO");
					sql_non.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO");									
					sql_non.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
					sql_non.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,bene_padre.ISTOTALMENTESCARICATO );
				 	sql_acc.addSQLNotExistsClause("AND",sql_non);
						
					if (sql_acc.executeCountQuery(getConnection(userContext))>0)
						throw new it.cnr.jada.comp.ApplicationException ("Attenzione: non è possibile scaricare totalmente questo bene\n poichè, bisogna scaricare prima i suoi accessori");
				}
		}		
	sql.addTableToHeader("INVENTARIO_BENI");
	sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)");
	sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)");
	sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)");
	sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,bene_padre.ISNOTTOTALMENTESCARICATO );				
	sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
	sql.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO",SQLBuilder.EQUALS, bene_padre.getNr_inventario());
	sql.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO",SQLBuilder.NOT_EQUALS,bene_padre.getProgressivo());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO(+)",SQLBuilder.EQUALS,bene_padre.ISNOTTOTALMENTESCARICATO );
 	if (sql.executeCountQuery(getConnection(userContext))>0)
 		throw new it.cnr.jada.comp.ApplicationException ("Attenzione: non è possibile scaricare totalmente questo bene\n poichè, bisogna scaricare prima i suoi accessori");

	}
	 	catch (java.sql.SQLException e){
		   throw handleSQLException(e);				
		}
}

private boolean checkBeniPerEliminaBuono(UserContext aUC, Buono_carico_scaricoBulk buono)
throws ComponentException{
	SQLBuilder sql;	
		boolean effettuato=true;
		try{
			sql= getHome(aUC,Tipo_carico_scaricoBulk.class).createSQLBuilder();
			sql.addSQLClause("AND","FL_BUONO_PER_TRASFERIMENTO",SQLBuilder.EQUALS,"Y");
			sql.addSQLClause("AND","CD_TIPO_CARICO_SCARICO",SQLBuilder.EQUALS,buono.getCd_tipo_carico_scarico());
			if (sql.executeExistsQuery(getConnection(aUC)))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: Operazione non possibile!\nIl Buono deriva " + 
				" da un trasferimento.");
		    Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(aUC, Buono_carico_scarico_dettBulk.class);
			sql = home.createSQLBuilder();
			
			sql.addSQLClause("AND","PG_INVENTARIO", SQLBuilder.EQUALS, buono.getPg_inventario());
			sql.addSQLClause("AND","ESERCIZIO", SQLBuilder.EQUALS, buono.getEsercizio());
			sql.addSQLClause("AND","TI_DOCUMENTO", SQLBuilder.EQUALS, buono.getTi_documento());
			sql.addSQLClause("AND","PG_BUONO_C_S", SQLBuilder.EQUALS, buono.getPg_buono_c_s());
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","STATO_COGE",sql.EQUALS,"C");
			sql.openParenthesis("OR");
			sql.addSQLClause("AND","STATO_COGE_QUOTE",sql.EQUALS,"C");
			sql.closeParenthesis();
			sql.closeParenthesis();
			if (sql.executeExistsQuery(getConnection(aUC)))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: Operazione non possibile!\nIl Buono " + 
					"risulta contabilizzato.");
			
			sql = getHome(aUC, Buono_carico_scarico_dettBulk.class).createSQLBuilder();
			sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, buono.getPg_inventario());
			sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS, buono.getEsercizio());
			sql.addSQLClause("AND","TI_DOCUMENTO", sql.EQUALS, buono.getTi_documento());
			sql.addSQLClause("AND","PG_BUONO_C_S", sql.EQUALS, buono.getPg_buono_c_s());
		
			List buoni = home.fetchAll(sql);
			for(Iterator i=buoni.iterator();i.hasNext();){
				Buono_carico_scarico_dettBulk buono_dett  =(Buono_carico_scarico_dettBulk)i.next();
				Inventario_beniBulk bene = new Inventario_beniBulk(buono_dett.getNr_inventario(),buono_dett.getPg_inventario(),new Long(buono_dett.getProgressivo().longValue()));
				bene = (Inventario_beniBulk)getHome(aUC, bene).findByPrimaryKey(bene);
				if (bene.getFl_totalmente_scaricato().booleanValue()&& buono_dett.getTi_documento().compareTo(Buono_carico_scaricoBulk.CARICO)==0)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Operazione non possibile!\nIl Bene " + 
							bene.getNumeroBeneCompleto() + 
							" è stato scaricato definitivamente.");
				
				Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
				Id_inventarioBulk inventario = inventarioHome.findInventarioFor(aUC,false);
				Inventario_ap_chHome invApChHome = (Inventario_ap_chHome) getHome(aUC,Inventario_ap_chBulk.class);
				if ((bene.getValore_ammortizzato().compareTo(new BigDecimal(0))!=0 && 
					buono_dett.getTi_documento().compareTo(Buono_carico_scaricoBulk.CARICO)==0) && 
				    !invApChHome.isAperto(inventario,buono.getEsercizio()))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Operazione non possibile!\nIl Bene " + 
							bene.getNumeroBeneCompleto() + 
							" ha cominciato l'ammortamento.");
				java.util.List accessori = ((Inventario_beniHome)getHome(aUC, Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
				if (accessori.size()>0){
					for(Iterator it_accessori =accessori.iterator();it_accessori.hasNext();){
						Inventario_beniBulk bene_accessorio=(Inventario_beniBulk)it_accessori.next();
						BulkList beni_buono = new BulkList(((Inventario_beniHome)getHome(aUC, Inventario_beniBulk.class)).findDettagliBuono(buono));
						
						SQLBuilder sql_altri_mov = getHome(aUC, Buono_carico_scarico_dettBulk.class).createSQLBuilder();
						sql_altri_mov.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,bene.getPg_inventario());
						sql_altri_mov.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS,bene.getNr_inventario());
						sql_altri_mov.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,bene.getProgressivo());
						
					if ((bene.isTotalmenteScaricato()&&  buono_dett.getTi_documento().compareTo(Buono_carico_scaricoBulk.SCARICO)==0)||
							 (buono_dett.getTi_documento().compareTo(Buono_carico_scaricoBulk.CARICO)==0) && (sql_altri_mov.executeCountQuery(getConnection(aUC))==1)) 
							if (!beni_buono.containsByPrimaryKey(bene_accessorio))
							   throw new it.cnr.jada.comp.ApplicationException("Attenzione: Operazione non possibile!\nIl Bene " + 
									   bene.getNumeroBeneCompleto() + 
							   		   " è collegato a dei beni accessori.");
					}
				}
				if (bene.isCancellabile()||buono_dett.getTi_documento().compareTo(Buono_carico_scaricoBulk.SCARICO)==0){
					effettuato=Aggiornamento_bene(aUC,buono,bene);
					if (!effettuato)
					   throw new it.cnr.jada.comp.ApplicationException("Attenzione: operazione non possibile!\nIl Bene " + 
											bene.getNumeroBeneCompleto() + 
											" ha subito variazioni che ne rendono impossibile la cancellazione.");
				}
			}
			return effettuato;
		} catch (Throwable t){
			throw new ComponentException (t);
		}	
}


/** 
 *  Nuovo bene padre - Bene già selezionato per il trasferimento
 *    PreCondition:
 *		 Si sta effettuando una operazione di trasferimento intra-inventario e si sta selezionando un nuovo
 *		bene padre per il bene che si sta trasferendo. Il bene indicato come nuovo bene padre è stato a sua
 *		volta selezionato per essere trasferito.
 *    PostCondition:
 *      Viene presentato un messaggio di errore all'utente che spiega l'impossibilitè di utilizzare
 *		il bene poichè anch'esso selezionato per il trasferimento.
 *
 *  Nuovo bene padre
 *    PreCondition:
 *      Tutti i controlli superati.
 *    PostCondition:
 *      Si prosegue con le operazioni di trasferimento
 *  
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoT <code>Trasferimento_inventarioBulk</code> il Buono di Trasferimento.
 * @param nuovo_bene_padre il <code>Inventario_beniBulk</code> bene indicato come nuovo bene padre.
**/
public void checkNuovoBenePadreAlreadySelected(UserContext userContext, Trasferimento_inventarioBulk buonoT, Inventario_beniBulk nuovo_bene_padre) throws it.cnr.jada.comp.ComponentException {
	try{
		SQLBuilder  sql= getHome(userContext,Inventario_beni_apgBulk.class).createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoT.getLocal_transactionID());
		sql.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,buonoT.getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS,nuovo_bene_padre.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,nuovo_bene_padre.getProgressivo());
		
		if (sql.executeExistsQuery(getConnection(userContext)))
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: il bene selezionato è tra quelli che verranno trasferiti.\nOperazione non possibile.");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}

}
private boolean Aggiornamento_bene(UserContext userContext,Buono_carico_scaricoBulk buono, Inventario_beniBulk bene) 
throws PersistencyException, ComponentException {
	LoggableStatement  cs = null;
		int stato= 0;	
		try
		{	cs = new LoggableStatement(getConnection(userContext),
				"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB400.cancella_variazioni(?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		
			cs.setInt(1,buono.getEsercizio().intValue()); 		// esercizio
			cs.setLong(2,bene.getPg_inventario().longValue()); 	// pg_inventario
			cs.setLong(3,bene.getNr_inventario().longValue()); 	
			cs.setLong(4,bene.getProgressivo().longValue());  
			cs.setLong(5,buono.getPg_buono_c_s().longValue());
			cs.setString(6, buono.getUser()); 					// utente
			cs.setTimestamp(7,buono.getData_registrazione()); 	// data_registrazione
			if (buono.getTi_documento().compareTo(Buono_carico_scaricoBulk.CARICO)==0)
				cs.setString(8,"+");	    
			else
				cs.setString(8,"-");
			cs.setInt(9,stato);
			cs.registerOutParameter(9,java.sql.Types.INTEGER);
		
			cs.executeQuery();
			stato=cs.getInt(9);			
				
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw new PersistencyException(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (java.sql.SQLException e) {
				throw new PersistencyException(e);
			}
		}
	if (stato == 0)	
		return true;
	else
		return false;  	  
}
/** 
 *  Scarica gli accessori di un bene - Sono stati già scaricati alcuni accessori del bene indicato
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha dei beni accessori, 
 *		ma l'utente ha già scaricato dei beni che sono accessori del bene selezionato, (metodo checkBeniAccessoriAlreadyExistFor).
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare l'errore.
 *
 *  Scarica gli accessori di un bene - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha dei beni accessori.
 *    PostCondition:
 *      Vengono riportati tutti i beni accessori del bene 
 *		indicato. Se l'utente ha specificato delle righe di fattura attiva, i beni saranno 
 *		associati ad esse.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene di cui si devono scaricare gli accessori.
 * @param selectedRighe_fattura la <code>List</code> lista eventuale di righe di Fattura Attiva 
 *		alle quali saranno associati i beni scaricati.
**/
public Buono_carico_scaricoBulk scaricaBeniAccessoriFor(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene, java.util.List selectedRighe_fattura) throws ComponentException {
	
	if (!buonoS.isByFattura()){
		checkBeniAccessoriAlreadyExistFor(userContext, buonoS, bene);
	}
	try{
		if (selectedRighe_fattura == null){
		
			Inventario_beni_apgHome home_apg = (Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
			SQLBuilder sql_accessori= home_apg.createSQLBuilder();
			sql_accessori.addTableToHeader("INVENTARIO_BENI");
			sql_accessori.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO");
			sql_accessori.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO");
			sql_accessori.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO");
			sql_accessori.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql_accessori.EQUALS,bene.getPg_inventario());
			sql_accessori.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql_accessori.EQUALS,bene.getNr_inventario());
			sql_accessori.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql_accessori.NOT_EQUALS,"0");
			sql_accessori.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO",sql_accessori.EQUALS,bene.ISTOTALMENTESCARICATO);
			sql_accessori.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql_accessori.EQUALS,buonoS.getLocal_transactionID());
			List beni =home_apg.fetchAll(sql_accessori);
			for (Iterator i=beni.iterator();i.hasNext();){	
				Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)i.next();
				bene_apg.setToBeDeleted();							
				super.eliminaConBulk(userContext,bene_apg);
			}
			
			Inventario_beniHome home = (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			SQLBuilder sql= home.createSQLBuilder();
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.CD_CDS",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			//sql.addSQLClause("AND","INVENTARIO_BENI.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO",sql.NOT_EQUALS,"0");
			
			beni =home.fetchAll(sql);
			for (Iterator i=beni.iterator();i.hasNext();){	
				Inventario_beniBulk inv =(Inventario_beniBulk)i.next();
				Inventario_beni_apgBulk bene_apg= new Inventario_beni_apgBulk();
				if (buonoS.isByFattura())
					bene_apg.setVariazione_meno(bene.getVariazione_meno());
				else{
					if(buonoS.getTipoMovimento()!=null && buonoS.getTipoMovimento().getFl_vendita().booleanValue()){
						bene_apg.setValore_alienazione(bene.getValoreBene(true));
					}
					bene_apg.setVariazione_meno(inv.getValoreBene());
				}
				if((buonoS instanceof Trasferimento_inventarioBulk) && (((Trasferimento_inventarioBulk)buonoS).isFl_cambio_categoria()))
					bene_apg.setFl_visibile(new Boolean(true));
				else
					bene_apg.setFl_visibile(new Boolean(false));
				bene_apg.setFl_totalmente_scaricato(new Boolean(true));
				bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
				bene_apg.setPg_inventario(inv.getPg_inventario());
				bene_apg.setNr_inventario(inv.getNr_inventario());
				bene_apg.setProgressivo(inv.getProgressivo());
				bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
				bene_apg.setTi_documento(buonoS.getTi_documento());
				bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
				
				lockBulk(userContext,inv);
				bene_apg.setToBeCreated();							
				super.creaConBulk(userContext,bene_apg);			
				//aggiornaValoreAlienazioneFor(userContext,bene);
			}
		}
		else{
			
			for (Iterator i = selectedRighe_fattura.iterator(); i.hasNext();){
				Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)i.next();
				Inventario_beniHome home = (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
				SQLBuilder sql= home.createSQLBuilder();
				
				sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CDS",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
				//sql.addSQLClause("AND","INVENTARIO_BENI.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
				sql.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
				sql.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO",sql.NOT_EQUALS,"0");
				List beni =home.fetchAll(sql);
				for (Iterator iteratore=beni.iterator();iteratore.hasNext();){	
					Inventario_beniBulk inv =(Inventario_beniBulk)iteratore.next();
					Inventario_beni_apgBulk bene_apg= new Inventario_beni_apgBulk();
					if (buonoS.isByFattura())
						bene_apg.setVariazione_meno(bene.getVariazione_meno());
					else
						bene_apg.setVariazione_meno(inv.getValoreBene());
					bene_apg.setFl_visibile(new Boolean(false));
					bene_apg.setFl_totalmente_scaricato(new Boolean(true));
					bene_apg.setEsercizio(riga_fattura.getEsercizio());
					bene_apg.setCd_cds(riga_fattura.getCd_cds());
					bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
					bene_apg.setPg_fattura(riga_fattura.getPg_fattura_attiva());
					bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
					bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
					bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
					bene_apg.setPg_inventario(inv.getPg_inventario());
					bene_apg.setNr_inventario(inv.getNr_inventario());
					bene_apg.setProgressivo(inv.getProgressivo());
					bene_apg.setTi_documento(buonoS.getTi_documento());
					bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
					
					lockBulk(userContext,inv);
					
					bene_apg.setToBeCreated();							
					super.creaConBulk(userContext,bene_apg);			
					aggiornaValoreAlienazioneFor(userContext,bene);
				}
			}
		}
	} catch (PersistencyException e) {
		throw handleException(e);
	} catch (OutdatedResourceException e) {
		throw handleException(e);	}
	catch (BusyResourceException e) {
		throw handleException(e);	}
	return buonoS;
}
/**
  *  Aggiorna il valore_alienazione di un bene - Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di scaricare totalmente un bene che ha dei beni accessori.
  *		Questo metodo aggiorna il Valore Alienazione del bene padre, ponendolo come somma dei
  *		valori attuali dei suoi beni accessori
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene_padre il <code>Inventario_beniBulk</code> bene di cui si devono scaricare gli accessori.
  */
private void aggiornaValoreAlienazioneFor(UserContext userContext, Inventario_beniBulk bene_padre)throws ComponentException {

	bene_padre.setValore_alienazione(bene_padre.getValoreBene(true));
	bene_padre.setToBeUpdated();
	super.modificaConBulk(userContext,bene_padre);	
}
/** 
 *  Aggiorna i beni.
 *    PreCondition:
 *      E' stata generata la richiesta di aggiornare un bene che si sta scaricando.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi allo scarico 
 *		del bene selezioato.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
 * @param bene il <code>Inventario_beniBulk</code> bene da scaricare selezionato.
 * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di Fattura alla quale sarà eventualmente associato il bene.
**/ 
public void modificaBeneScaricato(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene, OggettoBulk oggetto) throws ComponentException {

	if (buonoS instanceof Trasferimento_inventarioBulk){
		modificaBeneTrasferito(userContext,buonoS, bene);
	} else {
		/* Questo metodo viene richiamato tutte le volte che c'è un cambio di pagina del selezionatore
		 *  dei beni, oppure una richiesta di Riporta. NB: per beni associati a Fattura Attiva, questo nn è necessario, in quanto il bene
		 *	sarà SEMPRE scaricato TOTALMENTE. FORSE: tenere conto del VALORE_ALIENAZIONE.
		*/ 
		Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
		SQLBuilder sql= home.createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
		if (oggetto != null && oggetto instanceof Fattura_attiva_rigaIBulk ){
			Fattura_attiva_rigaIBulk riga_fattura =(Fattura_attiva_rigaIBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_attiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());		
		}else if(oggetto != null && oggetto instanceof Nota_di_credito_rigaBulk){
				Nota_di_credito_rigaBulk riga_fattura =(Nota_di_credito_rigaBulk)oggetto;
				sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
		}
		else if (oggetto != null && oggetto instanceof Documento_generico_rigaBulk ){
			Documento_generico_rigaBulk riga_fattura =(Documento_generico_rigaBulk)oggetto;
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_documento_generico());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
			sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga_fattura.getCd_tipo_documento_amm());
		}
		
		sql.setForUpdate(true);
		try{
		List beni=home.fetchAll(sql);
		for(Iterator i=beni.iterator();i.hasNext();){
			Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)i.next();
			bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
				if (buonoS.isByFattura()||buonoS.isByDocumento()||buonoS.getTipoMovimento().getFl_vendita().booleanValue())
					bene_apg.setVariazione_meno(bene.getVariazione_meno());
				else
					bene_apg.setVariazione_meno(bene.getValore_unitario());
			bene_apg.setValore_alienazione(bene.getValore_alienazione_apg());	
			bene_apg.setToBeUpdated();							
			super.modificaConBulk(userContext,bene_apg);
		}
		
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
}
/** 
 *  Valida Buono - data di scarico non specificata
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Scarico di cui non è stata indicata una data di registrazione.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare una data.
 *
 *  Valida Buono - data di scarico superiore alla data di sistema
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Scarico è superiore alla data di sistema.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - data di scarico non valida
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Scarico è anteriore all'ultima data di scarico registrata sul DB.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *  
 *  Valida Buono - data di scarico inferiore al valore consentito
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Scarico è anteriore alla MAX(data_ultima_modifica)
 *		registrata per i beni scaricati.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *   
 *  Valida Buono - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Scarico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Scarico con tutti i dettagli ed i beni correlati.
 * 
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoScarico il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
private void validaDataBuonoScarico (UserContext aUC,Buono_carico_scaricoBulk buonoScarico) 
	throws ComponentException
{

	try{
		String ti_buono;
		if (buonoScarico instanceof Trasferimento_inventarioBulk){
			ti_buono = "Trasferimento";
		} else {
			ti_buono = "Scarico";
		}
	   	java.sql.Timestamp dataOdierna = getHome(aUC,Buono_carico_scaricoBulk.class).getServerDate();
		// CONTROLLA LA DATA DI SCARICO - DATA DI SCARICO==NULL
		if (buonoScarico.getData_registrazione()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Data di " + ti_buono);

		// CONTROLLA LA DATA DI SCARICO - DATA DI SCARICO>SYSDATE
		if (buonoScarico.getData_registrazione().after(dataOdierna))
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di " + ti_buono + " non valida. La Data di " + ti_buono + " non può essere superiore alla data odierna");
			
		// CONTROLLA LA DATA DI SCARICO - DATA DI SCARICO ALL'INTERNO DELL'ESERCIZIO DI SCRIVANIA
		java.sql.Timestamp firstDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue());
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue());		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		if (buonoScarico.getData_registrazione().before(firstDayOfYear)){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di " + ti_buono + " non valida. La data di " + ti_buono + " non può essere inferiore di " + formatter.format(firstDayOfYear));
		}
		if (buonoScarico.getData_registrazione().after(lastDayOfYear)){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di " + ti_buono + " non valida. La data di " + ti_buono + " non può essere maggiore di " + formatter.format(lastDayOfYear));
		}
	} catch(Throwable t){
		throw handleException(t);		
	} 
}
/** 
 *  Annullamento scarico beni accessori.
 *    PreCondition:
 *      E' stata generata la richiesta di annullare lo scarico di un bene che ha dei beni 
 *		accessori correlati. Il metodo provvede a cancellare i beni accessori.
 *    PostCondition:
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
 * @param bene_padre <code>Inventario_beniBulk</code> il Bene principale di riferimento.
 * @param selectedRighe_fattura <code>List</code> le righe di Fattura Attuiva a cui sono associati i beni da eliminare.
 *  
**/
public void annullaScaricaBeniAccessoriFor(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene_padre, java.util.List selectedRighe_fattura) throws ComponentException {
	Inventario_beni_apgHome home =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
	SQLBuilder sql=home.createSQLBuilder();
	try{
		if (selectedRighe_fattura == null){
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS,bene_padre.getNr_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.NOT_EQUALS,"0");
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
			
			List accessori=home.fetchAll(sql);
				for (Iterator i=accessori.iterator();i.hasNext();){
					Inventario_beni_apgBulk bene_acc=(Inventario_beni_apgBulk)i.next();
					bene_acc.setToBeDeleted();	
					super.eliminaConBulk(userContext,bene_acc);
				}
	}
		//aggiornaValoreAlienazioneFor(userContext, bene_padre); 
	}catch (PersistencyException e) {
			throw handleException(e);
	}
}
/** 
 *  Cerca gli accessori di un Bene.
 *    PreCondition:
 *      E' stata generata la richiesta di cercare tutti gli accessori di un bene.
 *    PostCondition:
 *      Viene restituita la lista dei beni che rislutano accessori del bene specificato.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bene_principale <code>Inventario_beniBulk</code> il bene di riferimento
 *
 * @return beni_accessori <code>List</code> gli eventuali beni accessori trovati
**/
public java.util.List getBeniAccessoriFor(UserContext userContext, Inventario_beniBulk principale) throws ComponentException {

	try{
		Inventario_beniHome home = (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
		return home.getBeniAccessoriFor(principale);

	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(principale, ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(principale, ex);
	}
}
/** 
 *  Richiede l'ID univoco di Transazione.
 *    PreCondition:
 *      E' stato richiesto di recuperare/generare l'identificativo di transazione.
 *    PostCondition:
 *      Viene richiesto l'ID e, se questo non esiste, verrè generato, se richiesto.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID.
 *
 * @return local_transaction_id <code>String</code> l'ID di transazione richiesto.
**/
public String getLocalTransactionID(UserContext aUC, boolean force) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	String localTransactionID = null;
	LoggableStatement cs = null;
	try
	{
		cs = new LoggableStatement(getConnection( aUC ), 
				"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+
				"IBMUTL001.getLocalTransactionID(" + 
			(force?"TRUE":"FALSE") + ")}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.executeQuery();
		localTransactionID = cs.getString(1);
	} catch (Throwable e) 
	{
		throw handleException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	return localTransactionID;
	
}
/** 
 *  Aggiorna un bene.
 *    PreCondition:
 *      E' stata generata la richiesta di aggiornare un bene che si sta scaricando.
 *    PostCondition:
 *      Viene restituito il bene con le gli aggiornamenti effettuati.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono si Scarico.
 * @param bene il <code>Inventario_beniBulk</code> bene da scaricare selezionato.
 *
 * @return <code>OggettoBulk</code> il bene aggiornato.
**/

public OggettoBulk modificaDettagliScaricoConBulk(UserContext userContext, Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene) throws ComponentException{
	return bene;
}
public OggettoBulk modificaEditDettagliScaricoConBulk (UserContext userContext, Buono_carico_scaricoBulk buonoS, Buono_carico_scarico_dettBulk buono) throws ComponentException{
	buono.getBene().setImponibile_ammortamento(buono.getBene().getImponibile_ammortamento().add(buono.getBene().getVariazione_meno().add(buono.getValore_unitario().negate())));
	if (buono.getValore_unitario().compareTo(buono.getBene().getVariazione_meno())<0)
		buono.getBene().setVariazione_meno(buono.getBene().getVariazione_meno().add(buono.getBene().getVariazione_meno().negate().add(buono.getValore_unitario())));
	else		
		buono.getBene().setVariazione_meno(buono.getBene().getVariazione_meno().add(buono.getValore_unitario().add(buono.getBene().getVariazione_meno().negate())));
 	  
	
	if (buono.getBene().getImponibile_ammortamento().compareTo(new BigDecimal(0))<0)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: il valore indicato nel campo 'Valore Scaricato' del bene nr. " + buono.getBene().getNumeroBeneCompleto() + "\n non può essere superiore al valore del bene stesso");

	try{
		validaDettaglioPerModifica(userContext,buono);
		updateBulk(userContext, buono.getBene());
		updateBulk(userContext, buono);
	} catch (PersistencyException e){
		throw new it.cnr.jada.comp.ComponentException(e);
	}
	
return buono;
}
public SimpleBulkList selezionati(it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buonoS) throws it.cnr.jada.comp.ComponentException {

	SQLBuilder sql = getHome(userContext, Inventario_beniBulk.class,"V_INVENTARIO_BENI_APG").createSQLBuilder();
	sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
	try{
		List risultato = getHome(userContext, Inventario_beniBulk.class).fetchAll(sql);		
		if (risultato.size() >0){
			return new SimpleBulkList(risultato);
		}
	} catch (PersistencyException pe){
		throw new ComponentException(pe);
	}
	return new SimpleBulkList();
 }
/** 
 *  Inizializza sessione di lavoro
 *    PreCondition:
 *      Viene richiesta una possibile operazione di associazione di Beni presenti su DB ad 
 *		una o piè righe di Fattura Passiva.
 *    PostCondition:
 *      Viene impostato un SavePoint sulla tabella INVENTARIO_BENI_APG. 
 *		In caso di chiusura della sessione da parte dell'utente, tutte le operazione fatte 
 *		sul DB saranno annullate a partire da questo punto.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta  
**/
public void inizializzaBeniAssociatiPerModifica(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	try {
		setSavepoint(userContext,"INVENTARIO_BENI_APG");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
 *  
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @parama associa_buono lo <code>OggettoBulk</code> che ha generato la richiesta
 * @param riga_fattura_ncnd il <code>OggettoBulk</code> la riga della fattura a cui è associato il bene
 * @param bene il <code>Inventario_beniBulk</code> il bene modificato.
 *
 * @return l'oggetto <code>OggettoBulk</code> modificato
**/ 
public OggettoBulk modificaBeneAssociatoConBulk (
	UserContext userContext,
	OggettoBulk associa_buono,
	OggettoBulk riga_fattura_ncnd,
	Inventario_beniBulk bene) throws ComponentException {
	try{
	String variazione=null; 	
	Inventario_beni_apgHome home = (Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
	SQLBuilder sql=home.createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	if (associa_buono instanceof Ass_inv_bene_fatturaBulk){
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,((Ass_inv_bene_fatturaBulk)associa_buono).getLocal_transactionID());
		variazione=((Ass_inv_bene_fatturaBulk)associa_buono).getTi_documento();
	}
	else if (associa_buono instanceof Buono_carico_scaricoBulk){
	     sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,((Buono_carico_scaricoBulk)associa_buono).getLocal_transactionID());
	     variazione=((Buono_carico_scaricoBulk)associa_buono).getTi_documento();
	}
	
	if (riga_fattura_ncnd != null && riga_fattura_ncnd instanceof Fattura_passiva_rigaIBulk ){
		Fattura_passiva_rigaIBulk riga_fattura=(Fattura_passiva_rigaIBulk)riga_fattura_ncnd;
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());		
	}else if (riga_fattura_ncnd != null && riga_fattura_ncnd instanceof Nota_di_credito_rigaBulk ){
		Nota_di_credito_rigaBulk riga_fattura=(Nota_di_credito_rigaBulk)riga_fattura_ncnd;
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());		
	}else if (riga_fattura_ncnd != null && riga_fattura_ncnd instanceof Nota_di_debito_rigaBulk ){
		Nota_di_debito_rigaBulk riga_fattura=(Nota_di_debito_rigaBulk)riga_fattura_ncnd;
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());		
	}else if (riga_fattura_ncnd != null && riga_fattura_ncnd instanceof Documento_generico_rigaBulk ){
		Documento_generico_rigaBulk riga_fattura=(Documento_generico_rigaBulk)riga_fattura_ncnd;
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, riga_fattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_documento_generico());
		sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());		
		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga_fattura.getCd_tipo_documento_amm());
	}
	sql.setForUpdate(true);
	List beni=home.fetchAll(sql);
	
	for(Iterator i=beni.iterator();i.hasNext();){
		Inventario_beni_apgBulk bene_apg =(Inventario_beni_apgBulk)i.next();
		if (variazione.compareTo(Buono_carico_scaricoBulk.CARICO)==0)
			bene_apg.setVariazione_piu(bene.getVariazione_piu());
		else{
			bene_apg.setVariazione_meno(bene.getVariazione_meno());
			bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
		}
		bene_apg.setToBeUpdated();							
		super.modificaConBulk(userContext,bene_apg);
	}
	
	} catch (PersistencyException e) {
		throw handleException(e);
	} 
	
	return bene;
}

/** 
 *  Inserisce i beni temporanei.
 *    PreCondition:
 *      E' stata generata la richiesta di riportare i beni selezionati dall'utente nella tabella 
 *		temporanea INVENTARIO_BENI_APG. L'utente, in questa fase, si trova a selezionare dei 
 *		beni già presenti sul DB, per una operazione di associazione con Fattura Passiva.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi ai beni selezionati dall'utente.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> l'oggetto che contiene le informazioni 
 *		relative all'Inventario di riferimento ed alle righe di Fattura Passive.
 * 
 * @param righe_fattura la <code>List</code> lista di righe di fattura a cui associare i beni.
 * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
 * @param old_ass la <code>BitSet</code> selezione precedente.
 * @param ass la <code>BitSet</code> selezione attuale.
**/ 
public void modificaBeniAssociati(UserContext userContext,Ass_inv_bene_fatturaBulk associaBulk, java.util.List righe_fattura,OggettoBulk[] buoni,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException {

	/*		Questo metodo viene richiamato tutte le volte che c'è un cambio di pagina del selezionatore
	 * 		dei beni, oppure una richiesta di Riporta - Seleziona.
	*/
	Fattura_passiva_rigaIBulk riga_fattura=null;
	Fattura_attiva_rigaIBulk fattura_attiva=null;
	Nota_di_credito_rigaBulk nota=null;
	Nota_di_debito_rigaBulk notadeb=null;
	Documento_generico_rigaBulk documento=null;
	BigDecimal im_riga_fattura=new java.math.BigDecimal(0);
	BigDecimal tot_bene_apg=new BigDecimal(0);
	BigDecimal tot_ass_apg=new BigDecimal(0);
	try {
		for (Iterator selected = righe_fattura.iterator(); selected.hasNext();){
			OggettoBulk oggettoBulk = (OggettoBulk)selected.next();
			if (oggettoBulk instanceof Fattura_passiva_rigaIBulk) 
  		        riga_fattura = (Fattura_passiva_rigaIBulk)oggettoBulk;
  		     else if (oggettoBulk instanceof Fattura_attiva_rigaIBulk) 
  		    	fattura_attiva = (Fattura_attiva_rigaIBulk)oggettoBulk;
  		     else if (oggettoBulk instanceof Nota_di_debito_rigaBulk) 
 		        notadeb = (Nota_di_debito_rigaBulk)oggettoBulk;
  		     else if (oggettoBulk instanceof Nota_di_credito_rigaBulk)
  		        nota =(Nota_di_credito_rigaBulk)oggettoBulk;
  		     else if (oggettoBulk instanceof Documento_generico_rigaBulk)
  		    	 documento = (Documento_generico_rigaBulk)oggettoBulk;
		   for (int i = 0;i < buoni.length;i++) {
		     if (!associaBulk.isPerAumentoValore() && !associaBulk.isPerAumentoValoreDoc()){
				Buono_carico_scarico_dettBulk buono = (Buono_carico_scarico_dettBulk)buoni[i];
				if (old_ass.get(i) != ass.get(i)) {
					if (ass.get(i)) {								
							Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
							new_bene_apg.setPg_inventario(buono.getPg_inventario());
							new_bene_apg.setNr_inventario(buono.getNr_inventario());
							new_bene_apg.setProgressivo(new Long(buono.getProgressivo()));
							new_bene_apg.setDt_validita_variazione(buono.getBene().getDt_validita_variazione());
							new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
							new_bene_apg.setTi_documento(buono.getTi_documento());
							new_bene_apg.setPg_buono_c_s(buono.getPg_buono_c_s());
							if (riga_fattura!=null){
								new_bene_apg.setCd_cds(riga_fattura.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(riga_fattura.getEsercizio());   		
								new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_passiva());  
								new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
								if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
									im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
								else
									im_riga_fattura = riga_fattura.getIm_imponibile();
							}
							else if(nota!=null)
							{
								new_bene_apg.setCd_cds(nota.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(nota.getEsercizio());   		
								new_bene_apg.setPg_fattura(nota.getPg_fattura_passiva());  
								new_bene_apg.setProgressivo_riga(nota.getProgressivo_riga());
								if (nota.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
									im_riga_fattura = nota.getIm_imponibile().add(nota.getIm_iva());
								else
									im_riga_fattura = nota.getIm_imponibile();
							}
							else if(fattura_attiva!=null)
							{
								new_bene_apg.setCd_cds(fattura_attiva.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(fattura_attiva.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(fattura_attiva.getEsercizio());   		
								new_bene_apg.setPg_fattura(fattura_attiva.getPg_fattura_attiva());  
								new_bene_apg.setProgressivo_riga(fattura_attiva.getProgressivo_riga());
								im_riga_fattura = fattura_attiva.getIm_imponibile();
							}
							else if(notadeb!=null)
							{
								new_bene_apg.setCd_cds(notadeb.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(notadeb.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(notadeb.getEsercizio());   		
								new_bene_apg.setPg_fattura(notadeb.getPg_fattura_passiva());  
								new_bene_apg.setProgressivo_riga(notadeb.getProgressivo_riga());
								if (notadeb.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
									im_riga_fattura = notadeb.getIm_imponibile().add(notadeb.getIm_iva());
								else
									im_riga_fattura = notadeb.getIm_imponibile();
							}else if(documento!=null)
							{
								new_bene_apg.setCd_cds(documento.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(documento.getEsercizio());   		
								new_bene_apg.setPg_fattura(documento.getPg_documento_generico());  
								new_bene_apg.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
								new_bene_apg.setProgressivo_riga(documento.getProgressivo_riga());
								//IVA NON IMPUTATA SEPARATAMENTE 
								im_riga_fattura = documento.getIm_riga();
							}
						if((fattura_attiva!=null)||(documento!=null && documento.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.ENTRATE)){
							new_bene_apg.setValore_alienazione(buono.getBene().getValore_alienazione());
						}else{ 
							tot_bene_apg=new BigDecimal(0);
							tot_ass_apg=new BigDecimal(0);
							Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
							SQLBuilder sql= home.createSQLBuilder();
							sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
							sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, buono.getNr_inventario());
							sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,buono.getProgressivo());
							sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buono.getEsercizio());
							sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
							sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
							sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
							List apg =home.fetchAll(sql);
							for (Iterator iterator = apg.iterator();iterator.hasNext();){
								Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
								tot_bene_apg = tot_bene_apg.add(bene_apg.getValore_alienazione());							
							}		
							SQLBuilder sql_fatt= home.createSQLBuilder();
							if(documento!=null){
								sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,documento.getEsercizio());
								sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, documento.getCd_cds());
								sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
								sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
								sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
								sql_fatt.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
							}else if(riga_fattura!=null){
								sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
								sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
								sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
								sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
								sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());											
							}else if(nota!=null){
								sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,nota.getEsercizio());
								sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, nota.getCd_cds());
								sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
								sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
								sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());						
							}else if(notadeb!=null){
								sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
								sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, notadeb.getCd_cds());
								sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
								sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
								sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());											
							}
							sql_fatt.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
							apg =home.fetchAll( sql_fatt);
							for (Iterator iterator = apg.iterator();iterator.hasNext();){
								Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
								tot_ass_apg = tot_ass_apg.add(bene_apg.getValore_alienazione());							
							}
							//new_bene_apg.setValore_alienazione(buono.getValore_unitario());						
							if ((buono.getValore_unitario().subtract(tot_bene_apg)).compareTo(im_riga_fattura.subtract(tot_ass_apg))>0)
								new_bene_apg.setValore_alienazione(im_riga_fattura.subtract(tot_ass_apg));
							else
								new_bene_apg.setValore_alienazione(buono.getValore_unitario().subtract(tot_bene_apg));
						}
							new_bene_apg.setImp_fattura(im_riga_fattura);
							if (new_bene_apg.getValore_alienazione().compareTo(new BigDecimal(0))>0){
								Inventario_beniBulk inv =(Inventario_beniBulk)getHome( userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(new_bene_apg.getNr_inventario(),new_bene_apg.getPg_inventario(),new Long(new_bene_apg.getProgressivo().longValue())));
								lockBulk(userContext,inv);
								new_bene_apg.setToBeCreated();
								super.creaConBulk(userContext,new_bene_apg);
							}
				 	}
				}
				else {
						Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql= home.createSQLBuilder();
						sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
						sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, buono.getNr_inventario());
						sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,buono.getProgressivo());
						sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buono.getEsercizio());
						sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
						sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
						sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						if (riga_fattura!=null){	
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
						}
						else if(nota!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, nota.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
						}
						else if(fattura_attiva!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, fattura_attiva.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,fattura_attiva.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,fattura_attiva.getPg_fattura_attiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,fattura_attiva.getProgressivo_riga());
						}
						else if(notadeb!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, notadeb.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());
						}else if(documento!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, documento.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
							sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
						}
						//??			
						List beni_canc = home.fetchAll(sql); 
						for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
							new_bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,new_bene_apg);
						}			
	
					}	
		}else
		{
				Inventario_beniBulk bene = (Inventario_beniBulk)buoni[i];
				if (old_ass.get(i) != ass.get(i)) {
					if (ass.get(i)) {	
							Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
							new_bene_apg.setPg_inventario(bene.getPg_inventario());
							new_bene_apg.setNr_inventario(bene.getNr_inventario());
							new_bene_apg.setProgressivo(bene.getProgressivo());
							new_bene_apg.setDt_validita_variazione(associaBulk.getTest_buono().getData_registrazione());
							new_bene_apg.setEsercizio(associaBulk.getEsercizio());
							if (riga_fattura!=null){
								new_bene_apg.setCd_cds(riga_fattura.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());  							   		
								new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_passiva());  
								new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());    
							}else if(notadeb!=null){
								new_bene_apg.setCd_cds(notadeb.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(notadeb.getCd_unita_organizzativa());  							   		
								new_bene_apg.setPg_fattura(notadeb.getPg_fattura_passiva());  
								new_bene_apg.setProgressivo_riga(notadeb.getProgressivo_riga());
							}else if(documento!=null)
							{
								new_bene_apg.setCd_cds(documento.getCd_cds());   				
								new_bene_apg.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());  	
								new_bene_apg.setEsercizio(documento.getEsercizio());   		
								new_bene_apg.setPg_fattura(documento.getPg_documento_generico());  
								new_bene_apg.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
								new_bene_apg.setProgressivo_riga(documento.getProgressivo_riga());
							}
							new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
							new_bene_apg.setValore_alienazione(bene.getValoreBene());
							new_bene_apg.setTi_documento(associaBulk.getTi_documento());
							new_bene_apg.setPg_buono_c_s(associaBulk.getPg_buono_c_s());
							Inventario_beniBulk inv =(Inventario_beniBulk)getHome( userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(new_bene_apg.getNr_inventario(),new_bene_apg.getPg_inventario(),new Long(new_bene_apg.getProgressivo().longValue())));
							lockBulk(userContext,inv);
							
							new_bene_apg.setToBeCreated();							
							super.creaConBulk(userContext,new_bene_apg);
						}
					} else {
						Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql= home.createSQLBuilder();
						sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
						sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
						sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
						sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						if (riga_fattura!=null){	
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
							sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, riga_fattura.getEsercizio());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
						}
						else if(nota!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, nota.getCd_cds());
							sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, nota.getEsercizio());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
						}
						else if(notadeb!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, notadeb.getCd_cds());
							sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, notadeb.getEsercizio());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());
						}else if(documento!=null){
							sql.addSQLClause("AND","CD_CDS",sql.EQUALS, documento.getCd_cds());
							sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
							sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
							sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
							sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
						}
						
						List beni_canc = home.fetchAll(sql); 
						for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
							new_bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,new_bene_apg);
						}			
	
					}	
				}
		}
	   }
	} catch (PersistencyException e) {
		handleException(e);
	} catch (OutdatedResourceException e) {
		handleException(e);
	} catch (BusyResourceException e) {
		throw new ApplicationException("Risorsa occupata");
	}	
}
/** 
 *  associa tutti i beni
 *    PreCondition:
 *      E' stata generata la richiesta di associare tutti i beni disponibili, alle righe di Fattura
 *		selezionate.
 *		I Beni sono stati caricati con il metodo cercaBeniAssociabili.  
 *    PostCondition:
 *     Per ogni riga di Fattura Passiva selezionata dall'utente, vengono inserite nella 
 *		tabella INVENTARIO_BENI_APG le associazioni fra i TUTTI i beni disponibili e la 
 *		riga di fattura stessa.
 *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
 *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
 *			- NON sono stati già associati ad altre righe di Fattura durante la stessa sessione;
 *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N');
 *			- sono dello stesso tipo, (COMMERCIALE/ISTITUZIONALE), della riga di Fattura.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> il Bulk principale per l'associazione
 * @param selectedRighe_fattura <code>java.util.List</code> le righe di Fattura selezionate dall'utente
 *  
**/
public void associaTuttiBeni(UserContext userContext,Ass_inv_bene_fatturaBulk associaBulk, java.util.List selectedRighe_fattura, CompoundFindClause clause) throws ComponentException {
	Fattura_passiva_rigaIBulk riga_fattura= null;
	Fattura_attiva_rigaIBulk fattura_attiva= null;
	Nota_di_credito_rigaBulk nota = null;
	Nota_di_debito_rigaBulk notadeb = null;
	Documento_generico_rigaBulk documento = null;
	for (Iterator i = selectedRighe_fattura.iterator(); i.hasNext();){
		if(selectedRighe_fattura.get(0) instanceof Fattura_passiva_rigaIBulk)
			riga_fattura = (Fattura_passiva_rigaIBulk)i.next();
		else if (selectedRighe_fattura.get(0) instanceof Nota_di_credito_rigaBulk)
			nota=(Nota_di_credito_rigaBulk)i.next();
		else if (selectedRighe_fattura.get(0) instanceof Nota_di_debito_rigaBulk)
			notadeb=(Nota_di_debito_rigaBulk)i.next();	
		else if (selectedRighe_fattura.get(0) instanceof Fattura_attiva_rigaIBulk)
			fattura_attiva=(Fattura_attiva_rigaIBulk)i.next();	
		else if (selectedRighe_fattura.get(0) instanceof Documento_generico_rigaBulk)
			documento=(Documento_generico_rigaBulk)i.next();
	try {
		if (documento != null && associaBulk.isPerAumentoValoreDoc()){
			Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(clause);
			sql.addTableToHeader("INVENTARIO_BENI_APG");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,associaBulk.getPg_inventario());
			sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO (+)");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,associaBulk.getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)",sql.EQUALS,associaBulk.getLocal_transactionID());
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associaBulk.getTest_buono().getData_registrazione());
			if (documento !=null)
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE",sql.EQUALS,documento.getDocumento_generico().getTi_istituz_commerc());
			if((documento.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
				documento.getObbligazione_scadenziario()!=null &&
				documento.getObbligazione_scadenziario().getObbligazione()!=null &&
				documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
					sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
					sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",sql.EQUALS,"INVENTARIO_BENI.CD_CATEGORIA_GRUPPO");
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.ESERCIZIO",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getEsercizio());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_appartenenza());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_GESTIONE",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_gestione());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getCd_elemento_voce());		
			}
			// Locka le righe
			sql.setForUpdate(true);
			List beni =home.fetchAll(sql);
			for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beniBulk bene = (Inventario_beniBulk)iteratore.next();
				Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
				new_bene_apg.setCd_cds(documento.getCd_cds());
				new_bene_apg.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());
				new_bene_apg.setEsercizio(documento.getEsercizio());
				new_bene_apg.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
				new_bene_apg.setPg_fattura(documento.getPg_documento_generico());
				new_bene_apg.setProgressivo_riga(documento.getProgressivo_riga());
				new_bene_apg.setPg_inventario(bene.getPg_inventario());
				new_bene_apg.setNr_inventario(bene.getNr_inventario());
				new_bene_apg.setProgressivo(bene.getProgressivo());
				new_bene_apg.setDt_validita_variazione(associaBulk.getTest_buono().getData_registrazione());
				new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
				new_bene_apg.setValore_alienazione(bene.getValoreBene());
				new_bene_apg.setFl_totalmente_scaricato(new Boolean(false));
				new_bene_apg.setTi_documento(associaBulk.getTi_documento());
				new_bene_apg.setPg_buono_c_s(associaBulk.getPg_buono_c_s());
				new_bene_apg.setToBeCreated();	
				super.creaConBulk(userContext,new_bene_apg);
			}
		}else if (documento!=null){
				Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(userContext,Buono_carico_scarico_dettBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(clause);
				sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO"); 	 
				sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associaBulk.getLocal_transactionID());	// della stessa Fattura.
				//R.P. Consente di associare piu' volte lo stesso bene
				sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associaBulk.getInventario().getPg_inventario());
		//		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
				sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, documento.getDocumento_generico().getTi_istituz_commerc()); // Beni dello stesso tipo della riga 				
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.ESERCIZIO", SQLBuilder.EQUALS,documento.getEsercizio());
				if (documento.getDocumento_generico().getTi_entrate_spese()== Documento_genericoBulk.SPESE)
					sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
				else{
					sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
					sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISTOTALMENTESCARICATO); // scaricati totalmente
					//	condizione spostata nella vista associazioni disponibili per il fatturabile
					SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
					sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
					sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
					sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
					sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
					sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
					sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
					sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_VENDITA",sql.EQUALS,"Y");
					sql.addSQLExistsClause("AND",sql_tipo);
				}
				// Aggiunta clausola che visualizzi solo i beni che abbiano 
				//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
				sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S", SQLBuilder.GREATER,"0");
				SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
				
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_DOC_GEN",sql.EQUALS,documento.getEsercizio());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_DOC_GEN",sql.EQUALS,documento.getCd_cds());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_DOC_GEN",sql.EQUALS,documento.getCd_unita_organizzativa());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_DOCUMENTO_GENERICO",sql.NOT_EQUALS,documento.getPg_documento_generico());						
				sql.addSQLNotExistsClause("AND",sql_old_ass);
				
				SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
				sql.addSQLExistsClause("AND",sql_ass); 
				if((documento.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
						documento.getObbligazione_scadenziario()!=null &&
						documento.getObbligazione_scadenziario().getObbligazione()!=null &&
						documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
						sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
						sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",sql.EQUALS,"INVENTARIO_BENI.CD_CATEGORIA_GRUPPO");
						sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.ESERCIZIO",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getEsercizio());
						sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_appartenenza());
						sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_GESTIONE",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_gestione());
						sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS,documento.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getCd_elemento_voce());		
					}
				// Locka le righe
				sql.setForUpdate(true);
				List buoni =home.fetchAll(sql);
				for(Iterator iteratore=buoni.iterator();iteratore.hasNext();){
					Buono_carico_scarico_dettBulk buono = (Buono_carico_scarico_dettBulk)iteratore.next();
					Buono_carico_scaricoBulk buonoT = (Buono_carico_scaricoBulk)getHome(userContext,Buono_carico_scaricoBulk.class).findByPrimaryKey(buono.getBuono_cs());
					buono.setBene((Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(buono.getBene()));
					Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
					BigDecimal im_riga_fattura=documento.getIm_riga();					
					new_bene_apg.setCd_cds(documento.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(documento.getEsercizio());
					new_bene_apg.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
					new_bene_apg.setPg_fattura(documento.getPg_documento_generico());
					new_bene_apg.setProgressivo_riga(documento.getProgressivo_riga());
					new_bene_apg.setPg_inventario(buono.getPg_inventario());
					new_bene_apg.setNr_inventario(buono.getBene().getNr_inventario());
					new_bene_apg.setProgressivo(buono.getBene().getProgressivo());
					new_bene_apg.setDt_validita_variazione(buonoT.getData_registrazione());
					new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
					new_bene_apg.setTi_documento(buono.getTi_documento());
					new_bene_apg.setPg_buono_c_s(buono.getPg_buono_c_s());
				// associo o l'importo della riga di fattura o l'importo del buono 
			    // in quanto puo' essere un'associazione n-n
				// SE DOCUMENTO GENERICO DI ENTRATA RECUPERO IL VALORE DI ALIENAZIONE SUL BENE	
					if((documento!=null && documento.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.ENTRATE)){
						new_bene_apg.setValore_alienazione(buono.getBene().getValore_alienazione());
					}else{
						// per consentire l'associazione n-n devo calcolare quanto ho gia' associato
						// per dettaglio buono e per riga documento
						BigDecimal tot_bene_apg=new BigDecimal(0);
						BigDecimal tot_ass_apg=new BigDecimal(0);
						Inventario_beni_apgHome home_inv=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql_old= home_inv.createSQLBuilder();
						sql_old.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
						sql_old.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, buono.getNr_inventario());
						sql_old.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,buono.getProgressivo());
						sql_old.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buono.getEsercizio());
						sql_old.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
						sql_old.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
						sql_old.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						List apg =home_inv.fetchAll(sql_old);
						for (Iterator iterator = apg.iterator();iterator.hasNext();){
							Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
							tot_bene_apg = tot_bene_apg.add(bene_apg.getValore_alienazione());							
						}		
						SQLBuilder sql_fatt= home_inv.createSQLBuilder();
						if(documento!=null){
							sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,documento.getEsercizio());
							sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, documento.getCd_cds());
							sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
							sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
							sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
							sql_fatt.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
							sql_fatt.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						}				
						apg =home_inv.fetchAll( sql_fatt);
						for (Iterator iterator = apg.iterator();iterator.hasNext();){
							Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
							tot_ass_apg = tot_ass_apg.add(bene_apg.getValore_alienazione());							
						}	
						if ((buono.getValore_unitario().subtract(tot_bene_apg)).compareTo(im_riga_fattura.subtract(tot_ass_apg))>0)
							new_bene_apg.setValore_alienazione(im_riga_fattura.subtract(tot_ass_apg));
						else
							new_bene_apg.setValore_alienazione(buono.getValore_unitario().subtract(tot_bene_apg));
					/*	if (buono.getValore_unitario().compareTo(im_riga_fattura)>0)
							new_bene_apg.setValore_alienazione(im_riga_fattura);
						else
						new_bene_apg.setValore_alienazione(buono.getValore_unitario());*/
					}
					new_bene_apg.setImp_fattura(im_riga_fattura);
					new_bene_apg.setFl_totalmente_scaricato(buono.getBene().getFl_totalmente_scaricato());
					if (new_bene_apg.getValore_alienazione().compareTo(new BigDecimal(0))>0){
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
					}
			}
		}	
		else if (associaBulk.isPerAumentoValore()){
			Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(clause);
			sql.addTableToHeader("INVENTARIO_BENI_APG");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,associaBulk.getPg_inventario());
			sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO (+)");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,associaBulk.getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)",sql.EQUALS,associaBulk.getLocal_transactionID());
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associaBulk.getTest_buono().getData_registrazione());
			if (riga_fattura !=null){
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE",sql.EQUALS,riga_fattura.getTi_istituz_commerc());
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",sql.EQUALS,riga_fattura.getBene_servizio().getCategoria_gruppo().getCd_categoria_gruppo());
			/*	
			 * sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,riga_fattura.getProgressivo_riga());
				*/
			}else if(nota!=null){
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE",sql.EQUALS,nota.getTi_istituz_commerc());
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",sql.EQUALS,nota.getBene_servizio().getCategoria_gruppo().getCd_categoria_gruppo());
			/*	sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,nota.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,nota.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,nota.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,nota.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,nota.getProgressivo_riga());
				*/
			}else if(notadeb!=null){
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE",sql.EQUALS,notadeb.getTi_istituz_commerc());
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",sql.EQUALS,notadeb.getBene_servizio().getCategoria_gruppo().getCd_categoria_gruppo());
			/*	sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,notadeb.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,notadeb.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,notadeb.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,notadeb.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,notadeb.getProgressivo_riga());
				SQLBuilder sql_nota = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();				
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getCd_cds());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getCd_unita_organizzativa());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getEsercizio());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.EQUALS,notadeb.getRiga_fattura_associata().getPg_fattura_passiva());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getProgressivo_riga());
				sql.addSQLExistsClause("AND",sql_nota);
				*/
			}
			// Locka le righe
			sql.setForUpdate(true);
			List beni =home.fetchAll(sql);
			for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beniBulk bene = (Inventario_beniBulk)iteratore.next();
				Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
				if(riga_fattura!=null){
					new_bene_apg.setCd_cds(riga_fattura.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(riga_fattura.getEsercizio());
					new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_passiva());
					new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
				}else if(nota!=null){
					new_bene_apg.setCd_cds(nota.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(nota.getEsercizio());
					new_bene_apg.setPg_fattura(nota.getPg_fattura_passiva());
					new_bene_apg.setProgressivo_riga(nota.getProgressivo_riga());
				}else if(notadeb!=null){
					new_bene_apg.setCd_cds(notadeb.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(notadeb.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(notadeb.getEsercizio());
					new_bene_apg.setPg_fattura(notadeb.getPg_fattura_passiva());
					new_bene_apg.setProgressivo_riga(notadeb.getProgressivo_riga());
				}
				new_bene_apg.setPg_inventario(bene.getPg_inventario());
				new_bene_apg.setNr_inventario(bene.getNr_inventario());
				new_bene_apg.setProgressivo(bene.getProgressivo());
				new_bene_apg.setDt_validita_variazione(associaBulk.getTest_buono().getData_registrazione());
				new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
				new_bene_apg.setValore_alienazione(bene.getValoreBene());
				new_bene_apg.setFl_totalmente_scaricato(new Boolean(false));
				new_bene_apg.setTi_documento(associaBulk.getTi_documento());
				new_bene_apg.setPg_buono_c_s(associaBulk.getPg_buono_c_s());
				new_bene_apg.setToBeCreated();	
				super.creaConBulk(userContext,new_bene_apg);
			}
		}
		else if (documento==null){
				Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(userContext,Buono_carico_scarico_dettBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(clause);
				sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
				sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO"); 	 
				sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associaBulk.getLocal_transactionID());	// della stessa Fattura.
				//R.P. Consente di associare piu' volte lo stesso bene
				sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associaBulk.getInventario().getPg_inventario());
				
			if(riga_fattura!=null){
				sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
				sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga_fattura.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura				
				sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, riga_fattura.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.ESERCIZIO", SQLBuilder.EQUALS,riga_fattura.getEsercizio());
            //  r.p. per il momento in sospeso modificare la gestione
			/*	sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,riga_fattura.getProgressivo_riga());
				*/
				sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
			}
			else if(nota!=null){
				//RP IN QUESTO CASO DEVE PRENDERE ANCHE I TOTALMENTE SCARICATI
				//sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
				sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, nota.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
				sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, nota.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.ESERCIZIO", SQLBuilder.EQUALS,nota.getEsercizio());
				sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
            //  r.p. per il momento in sospeso modificare la gestione
			/*	sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,nota.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,nota.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,nota.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,nota.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,nota.getProgressivo_riga());
				*/
			}
			else if(notadeb!=null){
				sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO); // Non scaricati totalmente
				sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, notadeb.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
				sql.addSQLClause("AND", "INVENTARIO_BENI.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, notadeb.getBene_servizio().getCd_categoria_gruppo()); // Che sia della stessa Categoria Gruppo
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.ESERCIZIO", SQLBuilder.EQUALS,notadeb.getEsercizio());
				sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
            //  r.p. per il momento in sospeso modificare la gestione
				/*sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,notadeb.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,notadeb.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,notadeb.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,notadeb.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,notadeb.getProgressivo_riga());
				*/				
			}else if(fattura_attiva!=null){
				//RP IN QUESTO CASO DEVE PRENDERE SOLO I TOTALMENTE SCARICATI
				sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, Inventario_beniBulk.ISTOTALMENTESCARICATO); // Scaricati totalmente
				sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS,TipoIVA.COMMERCIALE.value()); // Beni dello stesso tipo della riga di Fattura
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.ESERCIZIO", SQLBuilder.EQUALS,fattura_attiva.getEsercizio());
				sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
			}
				// Aggiunta clausola che visualizzi solo i beni che abbiano 
				//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
				sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
				sql.addSQLClause("AND", "BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S", SQLBuilder.GREATER,"0");
					
				/* condizione spostata nella vista associazioni disponibili 
				SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
				sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
				sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
				sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
				sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
				sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
				sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
				sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_FATTURABILE",sql.EQUALS,"Y");
				sql.addSQLExistsClause("AND",sql_tipo);
				*/
					
				SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
				sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
			if (riga_fattura!=null){	
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,riga_fattura.getPg_fattura_passiva());
			}else if(nota!=null){
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,nota.getEsercizio());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,nota.getCd_cds());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,nota.getCd_unita_organizzativa());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,nota.getPg_fattura_passiva());
				
				/*SQLBuilder sql_nota = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
				sql_nota.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,nota.getRiga_fattura_associata().getCd_cds());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,nota.getRiga_fattura_associata().getCd_unita_organizzativa());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,nota.getRiga_fattura_associata().getEsercizio());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.EQUALS,nota.getRiga_fattura_associata().getPg_fattura_passiva());
				sql_nota.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,nota.getRiga_fattura_associata().getProgressivo_riga());
				sql.addSQLExistsClause("AND",sql_nota);*/
				
			}else if(notadeb!=null){
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,notadeb.getEsercizio());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,notadeb.getCd_cds());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,notadeb.getCd_unita_organizzativa());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.NOT_EQUALS,notadeb.getPg_fattura_passiva());
				/*SQLBuilder sql_deb = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();				
				sql_deb.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				sql_deb.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
				sql_deb.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
				sql_deb.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getCd_cds());
				sql_deb.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getCd_unita_organizzativa());
				sql_deb.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getEsercizio());
				sql_deb.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.EQUALS,notadeb.getRiga_fattura_associata().getPg_fattura_passiva());
				sql_deb.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,notadeb.getRiga_fattura_associata().getProgressivo_riga());		
				sql.addSQLExistsClause("AND",sql_deb);*/
			}else if(fattura_attiva!=null){
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_ATT",sql.EQUALS,fattura_attiva.getEsercizio());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_ATT",sql.EQUALS,fattura_attiva.getCd_cds());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_ATT",sql.EQUALS,fattura_attiva.getCd_unita_organizzativa());
				sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_ATTIVA",sql.NOT_EQUALS,fattura_attiva.getPg_fattura_attiva());
			}
				sql.addSQLNotExistsClause("AND",sql_old_ass);
				SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
				sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
				sql.addSQLExistsClause("AND",sql_ass); 
				// Locka le righe
				sql.setForUpdate(true);
				List buoni =home.fetchAll(sql);
				for(Iterator iteratore=buoni.iterator();iteratore.hasNext();){
					Buono_carico_scarico_dettBulk buono = (Buono_carico_scarico_dettBulk)iteratore.next();
					Buono_carico_scaricoBulk buonoT = (Buono_carico_scaricoBulk)getHome(userContext,Buono_carico_scaricoBulk.class).findByPrimaryKey(buono.getBuono_cs());
					Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
					buono.setBene((Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(buono.getBene()));
					
					BigDecimal im_riga_fattura=new java.math.BigDecimal(0);
					if(riga_fattura!=null){
						new_bene_apg.setCd_cds(riga_fattura.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(riga_fattura.getEsercizio());
						new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_passiva());
						new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
						if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
						else
							im_riga_fattura = riga_fattura.getIm_imponibile();
					}else if(nota!=null){
						new_bene_apg.setCd_cds(nota.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(nota.getEsercizio());
						new_bene_apg.setPg_fattura(nota.getPg_fattura_passiva());
						new_bene_apg.setProgressivo_riga(nota.getProgressivo_riga());
						if (nota.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = nota.getIm_imponibile().add(nota.getIm_iva());
						else
							im_riga_fattura = nota.getIm_imponibile();
					}else if(notadeb!=null){
						new_bene_apg.setCd_cds(notadeb.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(notadeb.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(notadeb.getEsercizio());
						new_bene_apg.setPg_fattura(notadeb.getPg_fattura_passiva());
						new_bene_apg.setProgressivo_riga(notadeb.getProgressivo_riga());
						if (notadeb.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = notadeb.getIm_imponibile().add(notadeb.getIm_iva());
						else
							im_riga_fattura = notadeb.getIm_imponibile();
					}else if(fattura_attiva!=null){
						new_bene_apg.setCd_cds(fattura_attiva.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(fattura_attiva.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(fattura_attiva.getEsercizio());
						new_bene_apg.setPg_fattura(fattura_attiva.getPg_fattura_attiva());
						new_bene_apg.setProgressivo_riga(fattura_attiva.getProgressivo_riga());
						im_riga_fattura = fattura_attiva.getIm_imponibile();
					}
					new_bene_apg.setPg_inventario(buono.getPg_inventario());
					new_bene_apg.setNr_inventario(buono.getBene().getNr_inventario());
					new_bene_apg.setProgressivo(buono.getBene().getProgressivo());
					new_bene_apg.setDt_validita_variazione(buonoT.getData_registrazione());
					new_bene_apg.setLocal_transaction_id(associaBulk.getLocal_transactionID());
					new_bene_apg.setTi_documento(buono.getTi_documento());
					new_bene_apg.setPg_buono_c_s(buono.getPg_buono_c_s());
				// associo o l'importo della riga di fattura o l'importo del buono 
			    // in quanto puo' essere un'associazione n-n,
				// SE PROVENIENTE DA FATTURA ATTIVA RECUPERO IL VALORE DI ALIENAZIONE
					if(fattura_attiva!=null){
						new_bene_apg.setValore_alienazione(buono.getBene().getValore_alienazione());
					}else{	
						// per consentire l'associazione n-n devo calcolare quanto ho gia' associato
						// per dettaglio buono e per riga fattura-nota
						BigDecimal tot_bene_apg=new BigDecimal(0);
						BigDecimal tot_ass_apg=new BigDecimal(0);
						Inventario_beni_apgHome home_inv=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql_old= home_inv.createSQLBuilder();
						sql_old.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, associaBulk.getInventario().getPg_inventario());
						sql_old.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, buono.getNr_inventario());
						sql_old.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,buono.getProgressivo());
						sql_old.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buono.getEsercizio());
						sql_old.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
						sql_old.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
						sql_old.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						List apg =home_inv.fetchAll(sql_old);
						for (Iterator iterator = apg.iterator();iterator.hasNext();){
							Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
							tot_bene_apg = tot_bene_apg.add(bene_apg.getValore_alienazione());							
						}		
						SQLBuilder sql_fatt= home_inv.createSQLBuilder();
						if (nota!=null){
							sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,nota.getEsercizio());
							sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, nota.getCd_cds());
							sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
							sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
							sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());						
						}else if(notadeb!=null){
							sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
							sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, notadeb.getCd_cds());
							sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
							sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
							sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());						
						}else if(riga_fattura!=null){
							sql_fatt.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
							sql_fatt.addSQLClause("AND","CD_CDS",sql.EQUALS, riga_fattura.getCd_cds());
							sql_fatt.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
							sql_fatt.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
							sql_fatt.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());						
						}
						sql_fatt.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						apg =home_inv.fetchAll( sql_fatt);
						for (Iterator iterator = apg.iterator();iterator.hasNext();){
							Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iterator.next();
							tot_ass_apg = tot_ass_apg.add(bene_apg.getValore_alienazione());							
						}	
						if ((buono.getValore_unitario().subtract(tot_bene_apg)).compareTo(im_riga_fattura.subtract(tot_ass_apg))>0)
							new_bene_apg.setValore_alienazione(im_riga_fattura.subtract(tot_ass_apg));
						else
							new_bene_apg.setValore_alienazione(buono.getValore_unitario().subtract(tot_bene_apg));
					/*	if (buono.getValore_unitario().compareTo(im_riga_fattura)>0)
							new_bene_apg.setValore_alienazione(im_riga_fattura);
						else
							new_bene_apg.setValore_alienazione(buono.getValore_unitario());*/
					}	
					new_bene_apg.setImp_fattura(im_riga_fattura);
					new_bene_apg.setFl_totalmente_scaricato(buono.getBene().getFl_totalmente_scaricato());
					if (new_bene_apg.getValore_alienazione().compareTo(new BigDecimal(0))>0){
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
					}
			}
		
		}
	} catch (PersistencyException e) {
			throw  handleException(e);
	}
} 
}
	public void associaTuttiBeni(UserContext userContext,Ass_inv_bene_fatturaBulk associaBulk, java.util.List selectedRighe_fattura) throws ComponentException {
	associaTuttiBeni(userContext, associaBulk, selectedRighe_fattura, null);
}
	/** 
	  *  Inserisce i beni temporanei - Trasferimento
	  *    PreCondition:
	  *      E' stata generata la richiesta di riportare i beni selezionati dall'utente nella tabella 
	  *		temporanea INVENTARIO_BENI_APG. L'utente, in questa fase, si trova a selezionare dei 
	  *		beni già presenti sul DB, per una operazione di TRASFERIMENTO.
	  *    PostCondition:
	  *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi ai beni selezionati dall'utente.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
	  * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
	  * @param old_ass la <code>BitSet</code> selezione precedente.
	  * @param ass la <code>BitSet</code> selezione attuale.
	**/
	private void modificaBeniScaricatiPerTrasferimento(UserContext userContext,Buono_carico_scaricoBulk buonoS, OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException {
		
		/* Questo metodo viene richiamato tutte le volte che c'è un cambio di pagina del selezionatore
		 * dei beni, oppure una richiesta di Riporta.
	    */
		
		try {
			for (int i = 0;i < beni.length;i++) {
				Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];
				if (old_ass.get(i) != ass.get(i)) {
						
					if (ass.get(i)) {
						if (bene.isBeneAccessorio() && checkBeneAlreadyExist(userContext, buonoS, bene)){
							continue;
						}
						// Locko il bene che è stato selezionato per essere scaricato.
						try{
							lockBulk(userContext, bene)	;
						} catch (OutdatedResourceException oe){
							throw handleException(oe);
						} catch (BusyResourceException bre){
							throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
						} catch (it.cnr.jada.persistency.PersistencyException pe){
							throw handleException(pe);
						} 
						Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
						new_bene_apg.setPg_inventario(bene.getPg_inventario());
						new_bene_apg.setNr_inventario(bene.getNr_inventario());
						new_bene_apg.setProgressivo(bene.getProgressivo());
						new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
						new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
						new_bene_apg.setValore_alienazione(bene.getValoreBene());
						new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
						new_bene_apg.setFl_visibile(new Boolean(true));
						new_bene_apg.setFl_trasf_come_principale(new Boolean(false));
						new_bene_apg.setTi_documento(buonoS.getTi_documento());
						new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
						// Se il bene è un bene principale, (progressivo == 0),
						//	scarica TUTTI i suoi eventuali Beni Accessori.
						if (!bene.isBeneAccessorio()){
							java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
							if (accessori.size() > 0){
								// Cancello i beni accessori eventualmente già presenti, prima di scaricare tutti gli accessori del bene padre
								deleteBeneAccessoriAlreadyExistsFor(userContext, buonoS, bene);							
								scaricaBeniAccessoriFor(userContext, buonoS, bene, null);
							}
						}
					} else {
						Inventario_beni_apgHome benihome =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);//,"V_INVENTARIO_BENI_APG");
						SQLBuilder sql= benihome.createSQLBuilder();
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());	
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
						List beni_acc =benihome.fetchAll(sql);
						
						for(Iterator iteratore=beni_acc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iteratore.next();
							bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,bene_apg);		
						}
					}
				}
			}
		}catch (it.cnr.jada.persistency.PersistencyException pe){
			throw handleException(pe);
		} catch (IntrospectionException e) {
			throw handleException(e);
		} 
}
	/** 
	  *  Scarico di un bene
	  *    PreCondition:
	  *      E' stata generata la richiesta di scaricare un bene. Il metodo controlla che
	  *		tale bene non sia stato già scaricato; viene utilizzato nei Buoni di Scarico per Trasferimento,
	  *		per evitare che l'utente possa scaricare contemporaneamente un bene padre, 
	  *		(che in automatico fa sè che vengano scaricati anche i suoi accessori), ed un bene suo accessorio.
	  *		Questo provocherebbe una visualizzazione errata dei beni nel tab dei dettagli ed un comportamento anomalo,
	  *		in caso di cancellazione di uno dei beni in questione.
	  *    PostCondition:
	  *      Restituisce <code>true</code> se il bene già esiste
	  *  
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
	  * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
	  * @param bene_padre il <code>Inventario_beniBulk</code> bene da scaricare totalmente.
	  *
	  *	@return boolean
	**/
private boolean checkBeneAlreadyExist(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene) throws ComponentException {
	Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);	
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());	
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
	try {
		if (sql.executeCountQuery(getConnection(userContext))>0)
			return true;
	}  catch (SQLException e) {
		throw handleException(e);
	}
	return false;
 }
/** 
 *  Scarico di beni
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha degli accessori. 
 *		Prima di scaricare tutti i beni accessori, si provvede a cancellare dalla tabella d'appoggio,
 *		INVENTARIO_BENI_APG, quei beni accessori eventualmente già scaricati in precedenza.
 *	   PostCondition:
 *      Gli eventuali beni accessori vengono cancellati. Si puè proseguire con le normali operazioni.
 *  
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene di cui si controllano gli accessori.
**/
private void deleteBeneAccessoriAlreadyExistsFor(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene_padre) throws ComponentException {
	try {
	Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);	
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS,bene_padre.getPg_inventario());	
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS,bene_padre.getNr_inventario());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.NOT_EQUALS,bene_padre.getProgressivo());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
	List accessori=home.fetchAll(sql);
		for (Iterator i=accessori.iterator();i.hasNext();){
			Inventario_beni_apgBulk bene_acc=(Inventario_beni_apgBulk)i.next();
			bene_acc.setToBeDeleted();	
			super.eliminaConBulk(userContext,bene_acc);
		}
	} catch (PersistencyException e) {
		handleException(e);
	}
 }
/** 
 *  Inserisce i beni temporanei.
 *    PreCondition:
 *      E' stata generata la richiesta di riportare i beni selezionati dall'utente nella tabella 
 *		temporanea INVENTARIO_BENI_APG. L'utente, in questa fase, si trova a selezionare dei 
 *		beni già presenti sul DB, per una operazione di scarico associato ad una Fattura Atiiva.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi ai beni selezionati dall'utente.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
 * @param righe_fattura la <code>List</code> lista di righe di fattura alle quali saranno associate i beni.
 * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
 * @param old_ass la <code>BitSet</code> selezione precedente.
 * @param ass la <code>BitSet</code> selezione attuale.
**/
public void modificaBeniScaricatiPerAssocia(UserContext userContext,Buono_carico_scaricoBulk buonoS, java.util.List righe_fattura,OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass) 
	throws ComponentException {

	/* Questo metodo viene richiamato tutte le volte che c'è un cambio di pagina del selezionatore
	 * dei beni, oppure una richiesta di Riporta SELEZIONE.
   */	
	String tipo = null;
	try {
	 if (!righe_fattura.isEmpty() && (righe_fattura.get(0) instanceof Fattura_attiva_rigaIBulk)){
		for (Iterator selected = righe_fattura.iterator(); selected.hasNext();){
			Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)selected.next();
			for (int i = 0;i < beni.length;i++) {
				Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];				
				
				if (old_ass.get(i) != ass.get(i)) {
						
					if (ass.get(i)) {		
						// Locko il bene che è stato selezionato per essere scaricato.
						try{
							lockBulk(userContext, bene)	;
						} catch (OutdatedResourceException oe){
							throw handleException(oe);
						} catch (BusyResourceException bre){
							throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
						} catch (it.cnr.jada.persistency.PersistencyException pe){
							throw handleException(pe);
						} 
						// Controlla che non siano stati associati ad una Riga di Fattura, Beni di tipo diverso 
						if (tipo == null){
							tipo = bene.getTi_commerciale_istituzionale();
						}
						else if (!tipo.equalsIgnoreCase(bene.getTi_commerciale_istituzionale())){
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile associare beni di tipo diverso.");
						}
						java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
						if (accessori.size() > 0)
							bene.setAccessori(new SimpleBulkList(accessori));
						Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
						new_bene_apg.setPg_inventario(bene.getPg_inventario());
						new_bene_apg.setNr_inventario(bene.getNr_inventario());
						new_bene_apg.setProgressivo(bene.getProgressivo());
						new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
						new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
						new_bene_apg.setCd_cds(riga_fattura.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(riga_fattura.getEsercizio());
						new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_attiva());
						new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
						new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
						new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						new_bene_apg.setFl_visibile(new Boolean(true));
						new_bene_apg.setTi_documento(buonoS.getTi_documento());
						new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
						// Scarica TUTTI i Beni Accessori del Bene appena scaricato
						if (accessori.size() > 0)
							scaricaBeniAccessori(userContext, buonoS, bene, riga_fattura);
						
					} else {
						Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql= home.createSQLBuilder();
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga_fattura.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_attiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
					    List beni_canc = home.fetchAll(sql); 
						for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
							new_bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,new_bene_apg);
						}								
					}					
				}
			}
	   }
	}else if (!righe_fattura.isEmpty() && (righe_fattura.get(0) instanceof Nota_di_credito_rigaBulk))
	{// nota credito
		for (Iterator selected = righe_fattura.iterator(); selected.hasNext();){
			Nota_di_credito_rigaBulk nota = (Nota_di_credito_rigaBulk)selected.next();
			for (int i = 0;i < beni.length;i++) {
				Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];				
				
				if (old_ass.get(i) != ass.get(i)) {
						
					if (ass.get(i)) {		
						// Locko il bene che è stato selezionato per essere scaricato.
						try{
							lockBulk(userContext, bene)	;
						} catch (OutdatedResourceException oe){
							throw handleException(oe);
						} catch (BusyResourceException bre){
							throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
						} catch (it.cnr.jada.persistency.PersistencyException pe){
							throw handleException(pe);
						} 
						// Controlla che non siano stati associati ad una Riga di Fattura, Beni di tipo diverso 
						if (tipo == null){
							tipo = bene.getTi_commerciale_istituzionale();
						}
						else if (!tipo.equalsIgnoreCase(bene.getTi_commerciale_istituzionale())){
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile associare beni di tipo diverso.");
						}
						Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
						new_bene_apg.setPg_inventario(bene.getPg_inventario());
						new_bene_apg.setNr_inventario(bene.getNr_inventario());
						new_bene_apg.setProgressivo(bene.getProgressivo());
						new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
						new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
						new_bene_apg.setCd_cds(nota.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(nota.getEsercizio());
						new_bene_apg.setPg_fattura(nota.getPg_fattura_passiva());
						new_bene_apg.setProgressivo_riga(nota.getProgressivo_riga());
						new_bene_apg.setVariazione_meno(new java.math.BigDecimal(0));
						new_bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
						new_bene_apg.setFl_visibile(new Boolean(true));
						new_bene_apg.setTi_documento(buonoS.getTi_documento());
						new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
						// Scarica TUTTI i Beni Accessori del Bene appena scaricato
						java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
						if (accessori.size() > 0 && bene.isTotalmenteScaricato())
							scaricaBeniAccessori(userContext, buonoS, bene);
					} else {
						Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql= home.createSQLBuilder();
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,nota.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,nota.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
					    List beni_canc = home.fetchAll(sql); 
						for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
							new_bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,new_bene_apg);
						}								
					}					
				}
			}
		}
	}else if (!righe_fattura.isEmpty() && (righe_fattura.get(0) instanceof Documento_generico_rigaBulk)){
		for (Iterator selected = righe_fattura.iterator(); selected.hasNext();){
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)selected.next();
			for (int i = 0;i < beni.length;i++) {
				Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];				
				
				if (old_ass.get(i) != ass.get(i)) {
						
					if (ass.get(i)) {		
						// Locko il bene che è stato selezionato per essere scaricato.
						try{
							lockBulk(userContext, bene)	;
						} catch (OutdatedResourceException oe){
							throw handleException(oe);
						} catch (BusyResourceException bre){
							throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
						} catch (it.cnr.jada.persistency.PersistencyException pe){
							throw handleException(pe);
						} 
						// Controlla che non siano stati associati ad una Riga di Fattura, Beni di tipo diverso 
						if (tipo == null){
							tipo = bene.getTi_commerciale_istituzionale();
						}
						else if (!tipo.equalsIgnoreCase(bene.getTi_commerciale_istituzionale())){
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile associare beni di tipo diverso.");
						}
						Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
						new_bene_apg.setPg_inventario(bene.getPg_inventario());
						new_bene_apg.setNr_inventario(bene.getNr_inventario());
						new_bene_apg.setProgressivo(bene.getProgressivo());
						new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
						new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
						new_bene_apg.setCd_cds(riga.getCd_cds());
						new_bene_apg.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
						new_bene_apg.setEsercizio(riga.getEsercizio());
						new_bene_apg.setPg_fattura(riga.getPg_documento_generico());
						new_bene_apg.setCd_tipo_documento_amm(riga.getCd_tipo_documento_amm());
						new_bene_apg.setProgressivo_riga(riga.getProgressivo_riga());
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
						new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
						new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						new_bene_apg.setFl_visibile(new Boolean(true));					
						/*new_bene_apg.setVariazione_meno(new java.math.BigDecimal(0));
						new_bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
						new_bene_apg.setFl_visibile(new Boolean(true));*/
						new_bene_apg.setTi_documento(buonoS.getTi_documento());
						new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
						new_bene_apg.setToBeCreated();	
						super.creaConBulk(userContext,new_bene_apg);
						// Scarica TUTTI i Beni Accessori del Bene appena scaricato
						java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
						if (accessori.size() > 0 && bene.isTotalmenteScaricato())
							scaricaBeniAccessori(userContext, buonoS, bene);
					} else {
						Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
						SQLBuilder sql= home.createSQLBuilder();
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga.getPg_documento_generico());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga.getProgressivo_riga());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga.getCd_tipo_documento_amm());
					    List beni_canc = home.fetchAll(sql); 
						for(Iterator iteratore= beni_canc.iterator();iteratore.hasNext();){
							Inventario_beni_apgBulk new_bene_apg =(Inventario_beni_apgBulk)iteratore.next();
							new_bene_apg.setToBeDeleted();							
							super.eliminaConBulk(userContext,new_bene_apg);
						}								
					}					
				}
			}
		}
	}
	}catch (PersistencyException e) {
		handleException(e);
	} catch (IntrospectionException e) {
		handleException(e);
	}
}
/** 
 *  Scarica gli accessori di un bene - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha dei beni accessori.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG tutti i beni accessori del bene 
 *		indicato. I beni saranno associati alla riga di fattura attiva specificata.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene di cui si devono scaricare gli accessori.
 * @param riga_fattura la <code>Fattura_attiva_rigaIBulk</code> riga di Fattura Attiva 
**/
private void scaricaBeniAccessori(
		UserContext userContext,
		Buono_carico_scaricoBulk buonoS, 
		Inventario_beniBulk bene_padre, 
		Fattura_attiva_rigaIBulk riga_fattura) throws ComponentException {
	try{
			Inventario_beniHome home = (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			SQLBuilder sql= home.createSQLBuilder();
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO",sql.EQUALS, bene_padre.getNr_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO",sql.NOT_EQUALS,"0");
			List beni =home.fetchAll(sql);
		
				for (Iterator i=beni.iterator();i.hasNext();){	
					Inventario_beniBulk inv =(Inventario_beniBulk)i.next();
					Inventario_beni_apgBulk bene_apg= new Inventario_beni_apgBulk();
					bene_apg.setVariazione_meno(inv.getValoreBene());
					bene_apg.setEsercizio(riga_fattura.getEsercizio());
					bene_apg.setCd_cds(riga_fattura.getCd_cds());
					bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
					bene_apg.setPg_fattura(riga_fattura.getPg_fattura_attiva());
					bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
					bene_apg.setFl_visibile(new Boolean(false));
					bene_apg.setFl_totalmente_scaricato(new Boolean(true));
					bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
					bene_apg.setPg_inventario(inv.getPg_inventario());
					bene_apg.setNr_inventario(inv.getNr_inventario());
					bene_apg.setProgressivo(inv.getProgressivo());
					bene_apg.setDt_validita_variazione(inv.getDt_validita_variazione());
					bene_apg.setTi_documento(buonoS.getTi_documento());
					bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
					bene_apg.setValore_alienazione(new BigDecimal(0));
					bene_apg.setToBeCreated();			
					lockBulk(userContext,inv);
					super.creaConBulk(userContext,bene_apg);			
					aggiornaValoreAlienazioneFor(userContext,bene_padre);
				}
	} catch(PersistencyException e) {
		handleException(e);
	} catch (OutdatedResourceException e) {
		handleException(e);
	} catch (BusyResourceException e) {
		handleException(e);
	}
 }
/** 
 *  Scarica gli accessori di un bene - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare totalmente un bene che ha dei beni accessori.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG tutti i beni accessori del bene 
 *		indicato. I beni saranno associati alla riga di fattura attiva specificata.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene di cui si devono scaricare gli accessori.
 *  
**/
public void scaricaBeniAccessori(
		UserContext userContext,
		Buono_carico_scaricoBulk buonoS, 
		Inventario_beniBulk bene_padre) throws ComponentException {
	try{
	java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene_padre);
	if (accessori.size() > 0){
		   
		    checkBeniAccessoriAlreadyExistFor(userContext, buonoS, bene_padre);
	}
	} catch(PersistencyException e) {
		handleException(e);
	}
	catch (IntrospectionException e) {
		handleException(e);
	}
	
 }

/** 
 * Scarica tutti i beni disponibili.
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare tutti i beni disponibili per lo scarico.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG tutti i beni disponibili. 
 *		Se l'utente ha specificato delle righe di fattura attiva, i beni saranno associati ad esse.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
 * @param selectedRighe_fattura la <code>List</code> lista eventuale di righe di Fattura Attiva 
 *		alle quali saranno associati i beni scaricati.
 * @param clauses le <code>CompoundFindClause</code> clausole aggiunte dall'utente.
**/ 
private void scaricaTuttiBeniDaFattura(UserContext userContext,Buono_carico_scaricoBulk buonoS, java.util.List selectedRighe_fattura, CompoundFindClause clauses) throws ComponentException {
try {	
  if (!selectedRighe_fattura.isEmpty() && (selectedRighe_fattura.get(0) instanceof Fattura_attiva_rigaIBulk)){	
	for (Iterator i = selectedRighe_fattura.iterator(); i.hasNext();){
		Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)i.next();
			Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(clauses);
			sql.addTableToHeader("INVENTARIO_BENI_APG");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
			sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO (+)");
			sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO (+)");
			sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
			sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, TipoIVA.COMMERCIALE.value()); // Beni dello stesso tipo della riga di Fattura
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)",sql.EQUALS,buonoS.getLocal_transactionID());
			sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,riga_fattura.getPg_fattura_attiva());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,riga_fattura.getProgressivo_riga());
			Inventario_beni_apgHome home_apg=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
			SQLBuilder notExistsQuery=home_apg.createSQLBuilder();
			notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
			notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
			notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
			notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
            sql.addSQLNotExistsClause("AND",notExistsQuery);
			// Locka le righe
			sql.setForUpdate(true);
			List beni =home.fetchAll(sql);
			for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beniBulk bene = (Inventario_beniBulk)iteratore.next();
				
				java.util.List accessori = ((Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class)).getBeniAccessoriFor(bene);
				if (accessori.size() > 0)
					bene.setAccessori(new SimpleBulkList(accessori));
				Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
				new_bene_apg.setCd_cds(riga_fattura.getCd_cds());
				new_bene_apg.setCd_unita_organizzativa(riga_fattura.getCd_unita_organizzativa());
				new_bene_apg.setEsercizio(riga_fattura.getEsercizio());
				new_bene_apg.setPg_fattura(riga_fattura.getPg_fattura_attiva());
				new_bene_apg.setProgressivo_riga(riga_fattura.getProgressivo_riga());
				new_bene_apg.setPg_inventario(bene.getPg_inventario());
				new_bene_apg.setNr_inventario(bene.getNr_inventario());
				new_bene_apg.setProgressivo(bene.getProgressivo());
				new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
				new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
				new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
				new_bene_apg.setTi_documento(buonoS.getTi_documento());
				new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
				if (bene.getProgressivo()!=0){
					new_bene_apg.setFl_visibile(false);
					new_bene_apg.setValore_alienazione(new BigDecimal(0));
					new_bene_apg.setVariazione_meno(bene.getValoreBene());
				}
				else{
					new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
					new_bene_apg.setFl_visibile(true);
					new_bene_apg.setVariazione_meno(bene.getValoreBene());
				}
				new_bene_apg.setToBeCreated();	
				super.creaConBulk(userContext,new_bene_apg);
			}
	}
  }
	//nota credito
	else if (!selectedRighe_fattura.isEmpty() && (selectedRighe_fattura.get(0) instanceof Nota_di_credito_rigaBulk)){
		for (Iterator i = selectedRighe_fattura.iterator(); i.hasNext();){
			Nota_di_credito_rigaBulk nota = (Nota_di_credito_rigaBulk)i.next();
				Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(clauses);
				sql.addTableToHeader("INVENTARIO_BENI_APG");//,ASS_INV_BENE_FATTURA");
				sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
				sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO (+)");
				sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO (+)");
				sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO (+)");
				/* r.p. eliminata selezione beni gia' associati a fattura origine
				sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO"); // Questa OUT Join
				sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO"); //  permette di escludere	
				sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO"); 	 //  quei beni che sono stati già selezioanti
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,nota.getRiga_fattura_origine().getEsercizio());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",SQLBuilder.EQUALS,nota.getRiga_fattura_origine().getCd_cds());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",SQLBuilder.EQUALS,nota.getRiga_fattura_origine().getCd_unita_organizzativa());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,nota.getRiga_fattura_origine().getPg_fattura_passiva());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",SQLBuilder.EQUALS,nota.getRiga_fattura_origine().getProgressivo_riga());
				*/
				sql.addSQLClause("AND", "INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.ISNULL,null);
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)",sql.EQUALS,buonoS.getLocal_transactionID());
				sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, nota.getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,nota.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,nota.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,nota.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,nota.getPg_fattura_passiva());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,nota.getProgressivo_riga());
				sql.addSQLClause("AND", "INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buonoS.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,nota.getBene_servizio().getCategoria_gruppo().getCd_categoria_gruppo());
				// Aggiunta clausola che visualizzi solo i beni che abbiano 
				//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
				sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				Inventario_beni_apgHome home_apg=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
				SQLBuilder notExistsQuery=home_apg.createSQLBuilder();
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
				notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
	            sql.addSQLNotExistsClause("AND",notExistsQuery);
				// Locka le righe
				sql.setForUpdate(true);
				List beni =home.fetchAll(sql);
				for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
					Inventario_beniBulk bene = (Inventario_beniBulk)iteratore.next();
					Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
					new_bene_apg.setCd_cds(nota.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(nota.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(nota.getEsercizio());
					new_bene_apg.setPg_fattura(nota.getPg_fattura_passiva());
					new_bene_apg.setProgressivo_riga(nota.getProgressivo_riga());
					new_bene_apg.setPg_inventario(bene.getPg_inventario());
					new_bene_apg.setNr_inventario(bene.getNr_inventario());
					new_bene_apg.setProgressivo(bene.getProgressivo());
					new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
					new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
					new_bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
					new_bene_apg.setTi_documento(buonoS.getTi_documento());
					new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
					if (bene.getProgressivo()!=0 && bene.isTotalmenteScaricato()){
						new_bene_apg.setFl_visibile(false);
						new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
					}
					else{
						new_bene_apg.setFl_visibile(true);
						new_bene_apg.setVariazione_meno(new BigDecimal(0));
					}
					new_bene_apg.setToBeCreated();	
					super.creaConBulk(userContext,new_bene_apg);
				}
		}	
	}else if (!selectedRighe_fattura.isEmpty() && (selectedRighe_fattura.get(0) instanceof Documento_generico_rigaBulk)){
		for (Iterator i = selectedRighe_fattura.iterator(); i.hasNext();){
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
				Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(clauses);
				sql.addTableToHeader("INVENTARIO_BENI_APG");
				sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
				sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO (+)");
				sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO (+)");
				sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO (+)");
				sql.addSQLClause("AND", "INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.ISNULL,null);
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)",sql.EQUALS,buonoS.getLocal_transactionID());
				sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS(+)",sql.EQUALS,riga.getCd_cds());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA(+)",sql.EQUALS,riga.getCd_unita_organizzativa());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO(+)",sql.EQUALS,riga.getEsercizio());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA(+)",sql.EQUALS,riga.getPg_documento_generico());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM(+)",sql.EQUALS,riga.getCd_tipo_documento_amm());
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA(+)",sql.EQUALS,riga.getProgressivo_riga());
				sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buonoS.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
				sql.addSQLClause("AND","INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga.getDocumento_generico().getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
				// Aggiunta clausola che visualizzi solo i beni che abbiano 
				//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
				sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				Inventario_beni_apgHome home_apg=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
				SQLBuilder notExistsQuery=home_apg.createSQLBuilder();
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
				notExistsQuery.addSQLJoin("INVENTARIO_BENI_APG.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
				notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
	            sql.addSQLNotExistsClause("AND",notExistsQuery);
				// Locka le righe
				sql.setForUpdate(true);
				List beni =home.fetchAll(sql);
				for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
					Inventario_beniBulk bene = (Inventario_beniBulk)iteratore.next();
					Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
					new_bene_apg.setCd_cds(riga.getCd_cds());
					new_bene_apg.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
					new_bene_apg.setEsercizio(riga.getEsercizio());
					new_bene_apg.setPg_fattura(riga.getPg_documento_generico());
					new_bene_apg.setProgressivo_riga(riga.getProgressivo_riga());
					new_bene_apg.setCd_tipo_documento_amm(riga.getCd_tipo_documento_amm());
					new_bene_apg.setPg_inventario(bene.getPg_inventario());
					new_bene_apg.setNr_inventario(bene.getNr_inventario());
					new_bene_apg.setProgressivo(bene.getProgressivo());
					new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
					new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
					new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
					new_bene_apg.setTi_documento(buonoS.getTi_documento());
					new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
					if (bene.getProgressivo()!=0 && bene.isTotalmenteScaricato()){
						new_bene_apg.setFl_visibile(false);
						new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
					}
					else{
						/*new_bene_apg.setVariazione_meno(new java.math.BigDecimal(0));
						new_bene_apg.setFl_totalmente_scaricato(bene.getFl_totalmente_scaricato());
						new_bene_apg.setFl_visibile(new Boolean(true))
						*/
						new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
						new_bene_apg.setFl_visibile(true);
						new_bene_apg.setVariazione_meno(bene.getValoreBene());
					}
					new_bene_apg.setToBeCreated();	
					super.creaConBulk(userContext,new_bene_apg);
				}
		}	
	}
	} catch (PersistencyException e) {					
		 handleException(e);
	} catch (IntrospectionException e) {
		handleException(e);
	} 
	
}
/** 
 * Scarica tutti i beni disponibili.
 *    PreCondition:
 *      E' stata generata la richiesta di scaricare tutti i beni disponibili per lo scarico per trasferimento.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG tutti i beni disponibili. 
 *		Se l'utente ha specificato delle righe di fattura attiva, i beni saranno associati ad esse.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
 * @param clauses le <code>CompoundFindClause</code> clausole aggiunte dall'utente.
**/ 
private void scaricaTuttiBeniPerTrasferimento(UserContext userContext,Buono_carico_scaricoBulk buonoS, CompoundFindClause clauses) throws ComponentException {
	Inventario_beniHome homebeni= (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
	SQLBuilder sql = homebeni.createSQLBuilder();
	try{
		sql.addClause(clauses);
		sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql.EQUALS, buonoS.getInventario().getPg_inventario());
		sql.addSQLClause("AND","INVENTARIO_BENI.CD_CDS",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
		//sql.addSQLClause("AND","INVENTARIO_BENI.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
		sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",sql.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
		sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",sql.LESS_EQUALS,buonoS.getData_registrazione());
		
		SQLBuilder sql_notExists = getHome(userContext,Inventario_beni_apgBulk.class).createSQLBuilder();
		sql_notExists.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)");
		sql_notExists.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)");
		sql_notExists.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)");
		sql_notExists.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
		sql.addSQLNotExistsClause("AND", sql_notExists);
		
		if (((Trasferimento_inventarioBulk)buonoS).isTrasferimentoIntraInv()){
			SQLBuilder notExistsQuery = getHome(userContext,Inventario_beni_apgBulk.class).createSQLBuilder();
			notExistsQuery.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO_PRINCIPALE");
			notExistsQuery.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO_PRINCIPALE");
			notExistsQuery.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO_PRINCIPALE");
			notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
			notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO_PRINCIPALE IS NOT NULL");
			notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO_PRINCIPALE IS NOT NULL");
			notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_PRINCIPALE IS NOT NULL");
			sql.addSQLNotExistsClause("AND", notExistsQuery);
		}
		// 	Locka le righe
		sql.setForUpdate(true);
		List beni = homebeni.fetchAll(sql);
		
		for(Iterator i=beni.iterator();i.hasNext();){
			Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
			Inventario_beni_apgBulk new_bene_apg = new Inventario_beni_apgBulk();
			new_bene_apg.setPg_inventario(bene.getPg_inventario());
			new_bene_apg.setNr_inventario(bene.getNr_inventario());
			new_bene_apg.setProgressivo(bene.getProgressivo());
			new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());
			new_bene_apg.setLocal_transaction_id(buonoS.getLocal_transactionID());
			new_bene_apg.setVariazione_meno(bene.getValoreBene());
			new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
			new_bene_apg.setFl_trasf_come_principale(new Boolean(false));
			if (new_bene_apg.getProgressivo().compareTo(new Long("0"))==0)
				new_bene_apg.setFl_visibile(new Boolean(true));
			else
				new_bene_apg.setFl_visibile(new Boolean(false));
			new_bene_apg.setTi_documento(buonoS.getTi_documento());
			new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
			new_bene_apg.setToBeCreated();	
			super.creaConBulk(userContext,new_bene_apg);
		}
	} catch(PersistencyException e) {
		throw handleException(e);
	}
 }

/** 
 *  Annullamento
 *    PreCondition:
 *      E' stata generata la richiesta di annullare tutte le operazioni effettuate durante
 *		la fase di associazione delle righe di una Fattura a dei Beni presenti in Inventario.
 *    PostCondition:
 *      Vengono cancellate tutte le associazioni fatte e presenti sulla tabella temporanea INVENTARIO_BENI_APG
 *		( metodo Buono_scaricoHome.deleteTempFor(Buono_scaricoBulk, java.util.List) )
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
 * @param righe_fattura <code>java.util.List</code> tutte le righe di Fattura Attiva.
 *  
**/
public void annullaRiportaAssFattura_Bene(UserContext aUC,OggettoBulk buonoS, java.util.List righe_fattura) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	Fattura_passiva_rigaIBulk fattura_passiva=null;
	Nota_di_credito_rigaBulk nota=null;
	Nota_di_debito_rigaBulk notadeb = null;
	Fattura_attiva_rigaIBulk fattura_attiva=null;
	Documento_generico_rigaBulk riga=null;
	for (Iterator i = righe_fattura.iterator(); i.hasNext();){
		OggettoBulk oggetto = (OggettoBulk)i.next();
		if (oggetto instanceof Fattura_passiva_rigaIBulk)
			fattura_passiva = (Fattura_passiva_rigaIBulk) oggetto; 
		else if (oggetto instanceof Nota_di_debito_rigaBulk)
			notadeb = (Nota_di_debito_rigaBulk) oggetto; 
		else if (oggetto instanceof Fattura_attiva_rigaIBulk)
			fattura_attiva = (Fattura_attiva_rigaIBulk) oggetto;
		else if(oggetto instanceof Nota_di_credito_rigaBulk)
			nota = (Nota_di_credito_rigaBulk) oggetto;
		else if (oggetto instanceof Documento_generico_rigaBulk)
			riga =(Documento_generico_rigaBulk)oggetto;
		Inventario_beni_apgHome benihome =(Inventario_beni_apgHome)getHome(aUC,Inventario_beni_apgBulk.class);//,"V_INVENTARIO_BENI_APG");
		SQLBuilder sql= benihome.createSQLBuilder();
		if (buonoS != null && buonoS instanceof Buono_carico_scaricoBulk)
			sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,((Buono_carico_scaricoBulk)buonoS).getLocal_transactionID());
		else if (buonoS != null && buonoS instanceof Ass_inv_bene_fatturaBulk)
			sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,((Ass_inv_bene_fatturaBulk)buonoS).getLocal_transactionID());
		if (fattura_passiva != null){		
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,fattura_passiva.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,fattura_passiva.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,fattura_passiva.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,fattura_passiva.getProgressivo_riga());					
		}
		else if(nota!=null){
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,nota.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,nota.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
		}else if(notadeb!=null){
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,notadeb.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());
		}else if(fattura_attiva!=null){
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,fattura_attiva.getCd_cds());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,fattura_attiva.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,fattura_attiva.getEsercizio());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,fattura_attiva.getPg_fattura_attiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,fattura_attiva.getProgressivo_riga());
	   }else if (riga!=null){
		    sql.addSQLClause("AND","CD_CDS",sql.EQUALS,riga.getCd_cds());
		    sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga.getCd_unita_organizzativa());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,riga.getEsercizio());
			sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga.getCd_tipo_documento_amm());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,riga.getPg_documento_generico());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,riga.getProgressivo_riga());
	   }
		   
			List beni =benihome.fetchAll(sql);
			for(Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beni_apgBulk bene_apg = (Inventario_beni_apgBulk)iteratore.next();
				bene_apg.setToBeDeleted();							
				super.eliminaConBulk(aUC,bene_apg);
			}
			}		
	}
/**
 * Si sta tentando confermare le operazioni compiute per associare delle righe di Fattura Passiva a dei beni già presenti sul DB.
 *
 *  Valida riporta - Righe di Fattura senza beni associati
 *    PreCondition:
 *      Non tutte le righe di fattura sono state associate a dei beni.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di
 *		associare almeno un bene ad ogni riga di Fattura.
 *
 *  Valida riporta - Tutti i controlli superati
 *    PreCondition:
 *      E' stata generata una richiesta di ritornare alla Fattura Passiva. Tutti i controlli superati.
 *    PostCondition:
 *      Viene consentito il ritorno nella Fattura Passiva per effettuare il salvataggio di 
 *		tutti i dati relativi a Fattura ed Inventario.
 *  
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param associaBulk <code>Ass_inv_bene_fatturaBulk</code> l'oggetto che contiene le informazioni 
 *		relative all'Inventario di riferimento ed alle righe di Fattura Passive.
**/
public void validaRiportaAssFattura_Bene(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk) throws ComponentException {

	java.math.BigDecimal tot_variazioni_riga;
	java.math.BigDecimal tot_bene;
	java.math.BigDecimal im_riga_fattura= new java.math.BigDecimal(0);
	java.math.BigDecimal tot_beni = new java.math.BigDecimal(0);
	java.math.BigDecimal tot_fattura = new java.math.BigDecimal(0);
	final java.math.BigDecimal zero = new java.math.BigDecimal(0);
	Nota_di_credito_rigaBulk nota =null;
	Nota_di_debito_rigaBulk notadeb=null;
	Fattura_passiva_rigaIBulk riga_fattura = null;
	Fattura_attiva_rigaIBulk fattura_attiva = null;
	Documento_generico_rigaBulk documento=null;
	Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
	//SQLBuilder sql=home.createSQLBuilder();
	// Se l'associazione è par aumento di valore, valida la testata del Buono di Carico
	try{
		if (associaBulk != null){
			for (Iterator i = associaBulk.getDettagliFatturaColl().iterator(); i.hasNext();){
				SQLBuilder sql=home.createSQLBuilder();
				if (associaBulk.getDettagliFatturaColl().size()!=0 && associaBulk.getDettagliFatturaColl().get(0) instanceof Fattura_passiva_rigaIBulk)
					riga_fattura = (Fattura_passiva_rigaIBulk)i.next();
				else if (associaBulk.getDettagliFatturaColl().size()!=0 && associaBulk.getDettagliFatturaColl().get(0) instanceof Nota_di_debito_rigaBulk)
					notadeb = (Nota_di_debito_rigaBulk)i.next();
				else if (associaBulk.getDettagliFatturaColl().size()!=0 && associaBulk.getDettagliFatturaColl().get(0) instanceof Fattura_attiva_rigaIBulk)
					fattura_attiva = (Fattura_attiva_rigaIBulk)i.next();
				else
					nota = (Nota_di_credito_rigaBulk)i.next();
				
				if (riga_fattura!=null){
						if (associaBulk.isPerAumentoValore()){	
							validaBuonoCarico_Testata(userContext, associaBulk.getTest_buono());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga_fattura.getCd_cds());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						}else{
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga_fattura.getCd_cds());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
							Ass_inv_bene_fatturaHome home_ass=(Ass_inv_bene_fatturaHome)getHome(userContext, Ass_inv_bene_fatturaBulk.class);
							SQLBuilder sql_ass=home_ass.createSQLBuilder();
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_BUONO_C_S");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.ESERCIZIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.NR_INVENTARIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_INVENTARIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PROGRESSIVO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.TI_DOCUMENTO");
							sql.addSQLNotExistsClause("AND",sql_ass);
						} 
					if (sql.executeCountQuery(getConnection(userContext))==0)
						throw new ApplicationException("Attenzione: è necessario indicare per ogni riga di Fattura almeno un bene.\n La riga " + riga_fattura.getDs_riga_fattura() +  " non ha beni associati.");
					
				}
				else if (nota!=null){
					if (associaBulk.isPerAumentoValore()){	
						validaBuonoCarico_Testata(userContext, associaBulk.getTest_buono());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,nota.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,nota.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
					}else{
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,nota.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,nota.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,nota.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,nota.getPg_fattura_passiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,nota.getProgressivo_riga());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						Ass_inv_bene_fatturaHome home_ass=(Ass_inv_bene_fatturaHome)getHome(userContext, Ass_inv_bene_fatturaBulk.class);
						SQLBuilder sql_ass=home_ass.createSQLBuilder();
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_BUONO_C_S");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.ESERCIZIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.NR_INVENTARIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_INVENTARIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PROGRESSIVO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.TI_DOCUMENTO");
						sql.addSQLNotExistsClause("AND",sql_ass);
						
					}
					if (sql.executeCountQuery(getConnection(userContext))==0)
						throw new ApplicationException("Attenzione: è necessario indicare per ogni riga di NC almeno un bene.\n La riga " + nota.getDs_riga_fattura() +  " non ha beni associati.");
					
				}else if (notadeb!=null){
					if (associaBulk.isPerAumentoValore()){	
						validaBuonoCarico_Testata(userContext, associaBulk.getTest_buono());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,notadeb.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
					}else{
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,notadeb.getCd_cds());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,notadeb.getCd_unita_organizzativa());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,notadeb.getEsercizio());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,notadeb.getPg_fattura_passiva());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,notadeb.getProgressivo_riga());
						sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						Ass_inv_bene_fatturaHome home_ass=(Ass_inv_bene_fatturaHome)getHome(userContext, Ass_inv_bene_fatturaBulk.class);
						SQLBuilder sql_ass=home_ass.createSQLBuilder();
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_BUONO_C_S");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.ESERCIZIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.NR_INVENTARIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_INVENTARIO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PROGRESSIVO");
						sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.TI_DOCUMENTO");
						sql.addSQLNotExistsClause("AND",sql_ass);
					}
				if (sql.executeCountQuery(getConnection(userContext))==0)
					throw new ApplicationException("Attenzione: è necessario indicare per ogni riga di ND almeno un bene.\n La riga " + notadeb.getDs_riga_fattura() +  " non ha beni associati.");
			}else if (fattura_attiva!=null){			
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,fattura_attiva.getCd_cds());
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,fattura_attiva.getCd_unita_organizzativa());
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,fattura_attiva.getEsercizio());
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,fattura_attiva.getPg_fattura_attiva());
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,fattura_attiva.getProgressivo_riga());
					sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
					Ass_inv_bene_fatturaHome home_ass=(Ass_inv_bene_fatturaHome)getHome(userContext, Ass_inv_bene_fatturaBulk.class);
					SQLBuilder sql_ass=home_ass.createSQLBuilder();
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_BUONO_C_S");
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.ESERCIZIO");
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.NR_INVENTARIO");
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_INVENTARIO");
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PROGRESSIVO");
					sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.TI_DOCUMENTO");
					sql.addSQLNotExistsClause("AND",sql_ass);
				if (sql.executeCountQuery(getConnection(userContext))==0)
					throw new ApplicationException("Attenzione: è necessario indicare per ogni riga di Fattura Attiva almeno un bene.\n La riga " + fattura_attiva.getDs_riga_fattura() +  " non ha beni associati.");
			}
				if (associaBulk.isPerAumentoValore()){
					tot_variazioni_riga = new java.math.BigDecimal(0);
					List beni=home.fetchAll(sql);
					for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
						Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();
						java.math.BigDecimal variazione_piu = bene_apg.getVariazione_piu();
						if ((variazione_piu == null || (variazione_piu.compareTo(zero))==0) && riga_fattura!=null){
							throw new ApplicationException("Attenzione: indicare un valore per il bene " + bene_apg.getNr_inventario()+ 
							" associato alla riga di Fattura " + riga_fattura.getDs_riga_fattura());
						}
						if ((variazione_piu == null || (variazione_piu.compareTo(zero))==0) && nota!=null){
							throw new ApplicationException("Attenzione: indicare un valore per il bene " + bene_apg.getNr_inventario()+ 
							" associato alla riga di Nota Credito " + nota.getDs_riga_fattura());
						}
						if ((variazione_piu == null || (variazione_piu.compareTo(zero))==0) && notadeb!=null){
							throw new ApplicationException("Attenzione: indicare un valore per il bene " + bene_apg.getNr_inventario()+ 
							" associato alla riga di Nota Debito " + notadeb.getDs_riga_fattura());
						}
						tot_variazioni_riga = tot_variazioni_riga.add(variazione_piu);
					}
					if (riga_fattura!=null)
						if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
						else
							im_riga_fattura = riga_fattura.getIm_imponibile();
					else if (nota!=null)
						if (nota.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = nota.getIm_imponibile().add(nota.getIm_iva());
						else
							im_riga_fattura = nota.getIm_imponibile();
					 if (notadeb!=null)
							if (notadeb.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
								im_riga_fattura = notadeb.getIm_imponibile().add(notadeb.getIm_iva());
							else
								im_riga_fattura = notadeb.getIm_imponibile();
					// Controlla che i valori siano congruenti
					if (tot_variazioni_riga.compareTo(im_riga_fattura) != 0 && riga_fattura!=null)							
						throw new ApplicationException(
								"Attenzione: il totale delle variazioni, (" + tot_variazioni_riga + "), indicate per la riga di Fattura " + riga_fattura.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					if (tot_variazioni_riga.compareTo(im_riga_fattura) != 0 && nota!=null)							
						throw new ApplicationException(
								"Attenzione: il totale delle variazioni, (" + tot_variazioni_riga + "), indicate per la riga di Nota Credito " + nota.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					if (tot_variazioni_riga.compareTo(im_riga_fattura) != 0 && notadeb!=null)							
						throw new ApplicationException(
								"Attenzione: il totale delle variazioni, (" + tot_variazioni_riga + "), indicate per la riga di Nota Debito " + notadeb.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					tot_beni = tot_beni.add(tot_variazioni_riga);
					tot_fattura = tot_fattura.add(im_riga_fattura);
				}
				else{
					tot_bene = new java.math.BigDecimal(0);		
					List beni=home.fetchAll(sql);
					for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
						Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();		
						java.math.BigDecimal valore = bene_apg.getValore_alienazione();
						tot_bene = tot_bene.add(valore);
					}
					if (riga_fattura!=null)
						if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
						else
							im_riga_fattura = riga_fattura.getIm_imponibile();
					else if (nota!=null)
						if (nota.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = nota.getIm_imponibile().add(nota.getIm_iva());
						else
							im_riga_fattura = nota.getIm_imponibile();
					else if (notadeb!=null)
						if (notadeb.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
							im_riga_fattura = notadeb.getIm_imponibile().add(notadeb.getIm_iva());
						else
							im_riga_fattura = notadeb.getIm_imponibile();
					else if(fattura_attiva!=null)
						im_riga_fattura = fattura_attiva.getIm_imponibile();
					//	Controlla che i valori siano congruenti
					 if (tot_bene.compareTo(im_riga_fattura) != 0 && riga_fattura!=null)							
						throw new ApplicationException(
							"Attenzione: il totale dei beni, (" + tot_bene + "), indicati per la riga di Fattura " + riga_fattura.getDs_riga_fattura() +  
							" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					 if (tot_bene.compareTo(im_riga_fattura) != 0 && nota!=null)							
							throw new ApplicationException(
								"Attenzione: il totale dei beni, (" + tot_bene + "), indicati per la riga di Nota Credito " + nota.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					 if (tot_bene.compareTo(im_riga_fattura) != 0 && notadeb!=null)							
							throw new ApplicationException(
								"Attenzione: il totale dei beni, (" + tot_bene + "), indicati per la riga di Nota Debito " + notadeb.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					 if (tot_bene.compareTo(im_riga_fattura) != 0 && fattura_attiva!=null)							
							throw new ApplicationException(
								"Attenzione: il totale dei beni, (" + tot_bene + "), indicati per la riga di Nota Debito " + fattura_attiva.getDs_riga_fattura() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					tot_beni = tot_beni.add(tot_bene);
					tot_fattura = tot_fattura.add(im_riga_fattura);
				}
				// Controlla che il totale della Fattura quadri con il totale degli incrementi
				if (associaBulk.isPerAumentoValore() &&	(tot_fattura.compareTo(tot_beni)) != 0)
					throw new ApplicationException(
						"Attenzione: il totale della Fattura, (" + tot_fattura + "), non corrisponde al totale degli incrementi, (" + tot_beni + ")");
			
			}
			for (Iterator i = associaBulk.getDettagliDocumentoColl().iterator(); i.hasNext();){
				SQLBuilder sql=home.createSQLBuilder();
				if (associaBulk.getDettagliDocumentoColl().size()!=0 && associaBulk.getDettagliDocumentoColl().get(0) instanceof Documento_generico_rigaBulk)
					documento = (Documento_generico_rigaBulk)i.next();
				
				if (documento!=null){
						if (associaBulk.isPerAumentoValoreDoc()){	
							validaBuonoCarico_Testata(userContext, associaBulk.getTest_buono());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,documento.getCd_cds());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,documento.getEsercizio());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
						}else{
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,documento.getCd_cds());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,documento.getCd_unita_organizzativa());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,documento.getEsercizio());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,documento.getPg_documento_generico());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,documento.getProgressivo_riga());
							sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
							Ass_inv_bene_fatturaHome home_ass=(Ass_inv_bene_fatturaHome)getHome(userContext, Ass_inv_bene_fatturaBulk.class);
							SQLBuilder sql_ass=home_ass.createSQLBuilder();
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_BUONO_C_S");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.ESERCIZIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.NR_INVENTARIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PG_INVENTARIO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.PROGRESSIVO");
							sql_ass.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO",sql_ass.EQUALS,"INVENTARIO_BENI_APG.TI_DOCUMENTO");
							sql.addSQLNotExistsClause("AND",sql_ass);
						}
					if (sql.executeCountQuery(getConnection(userContext))==0)
						throw new ApplicationException("Attenzione: è necessario indicare per ogni riga di Documento almeno un bene.\n La riga " + documento.getDs_riga() +  " non ha beni associati.");
					
				}
				if (associaBulk.isPerAumentoValoreDoc()){
					tot_variazioni_riga = new java.math.BigDecimal(0);
					List beni=home.fetchAll(sql);
					for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
						Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();
						java.math.BigDecimal variazione_piu = bene_apg.getVariazione_piu();
						if ((variazione_piu == null || (variazione_piu.compareTo(zero))==0) && documento!=null){
							throw new ApplicationException("Attenzione: indicare un valore per il bene " + bene_apg.getNr_inventario()+ 
							" associato alla riga di Documento " + documento.getDs_riga());
						}
						tot_variazioni_riga = tot_variazioni_riga.add(variazione_piu);
					}
					if (documento!=null)
						im_riga_fattura = documento.getIm_riga();
					// Controlla che i valori siano congruenti
					if (tot_variazioni_riga.compareTo(im_riga_fattura) != 0 && documento!=null)							
						throw new ApplicationException(
								"Attenzione: il totale delle variazioni, (" + tot_variazioni_riga + "), indicate per la riga di Documento " + documento.getDs_riga() +  
								" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					tot_beni = tot_beni.add(tot_variazioni_riga);
					tot_fattura = tot_fattura.add(im_riga_fattura);
				}
				else{
					tot_bene = new java.math.BigDecimal(0);	
					if (documento!=null)
						im_riga_fattura = documento.getIm_riga();
					List beni=home.fetchAll(sql);
					for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
						Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();		
						java.math.BigDecimal valore = bene_apg.getValore_alienazione();
					
						if (valore.compareTo(im_riga_fattura)>0)
							tot_bene = tot_bene.add(im_riga_fattura);		
						else
							tot_bene = tot_bene.add(valore);	
					}
					
					//	Controlla che i valori siano congruenti
					 if (tot_bene.compareTo(im_riga_fattura) != 0 && documento!=null)							
						throw new ApplicationException(
							"Attenzione: il totale dei beni, (" + tot_bene + "), indicati per la riga di Documento " + documento.getDs_riga() +  
							" non corrisponde all'importo della riga stessa, (" + im_riga_fattura + ")");
					tot_beni = tot_beni.add(tot_bene);
					tot_fattura = tot_fattura.add(im_riga_fattura);
				}
				// Controlla che il totale della Fattura quadri con il totale degli incrementi
				if (associaBulk.isPerAumentoValore() &&	(tot_fattura.compareTo(tot_beni)) != 0)
					throw new ApplicationException(
						"Attenzione: il totale della Fattura, (" + tot_fattura + "), non corrisponde al totale degli incrementi, (" + tot_beni + ")");
				else if (associaBulk.isPerAumentoValoreDoc() &&	(tot_fattura.compareTo(tot_beni)) != 0)
					throw new ApplicationException(
							"Attenzione: il totale del Documento, (" + tot_fattura + "), non corrisponde al totale degli incrementi, (" + tot_beni + ")");
			}
	}
   } catch (it.cnr.jada.comp.ApplicationException e){
		throw handleException(e);
   } catch (SQLException e) {
   		throw handleException(e);
   } catch (PersistencyException e) {
   		throw handleException(e);
   }
}
/** 
 *  Valida Buono - data di carico non specificata
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stata indicata una data di registrazione.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare una data..
 *
 *  Valida Buono - data di carico superiore alla data di sistema
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Carico è superiore alla data di sistema.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - data di carico non valida
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Carico è anteriore all'ultima data di carico registrata sul DB.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - tipo movimento non specificato
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stato specificato il tipo di movimento.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente indicando la necessitè di scegliere il tipo movimento.
 *
 *  Valida Buono - descrizione non specificata
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stata specificata la descrizione.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Carico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Carico con tutti i dettagli ed i beni correlati.
 * 
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoCarico il <code>Buono_caricoBulk</code> Buono di Carico.
**/
private void validaBuonoCarico_Testata (UserContext aUC,Buono_carico_scaricoBulk buonoCarico) 
	throws ComponentException
{
	java.sql.Timestamp dataOdierna = null;
	try{
		dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		if (buonoCarico.getData_registrazione()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: bisogna valorizzare la data di Carico");
		
		// CONTROLLA LA DATA DI CARICO - DATA DI CARICO > DATA ODIERNA
		if (buonoCarico.getData_registrazione().after(dataOdierna))
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Carico non valida. La Data di Carico non può essere superiore alla data odierna");

		// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI CARICO
		if (buonoCarico.getTipoMovimento()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento per il Buono di Carico");
		// CONTROLLA LA DATA DI CARICO - DATA DI CARICO ALL'INTERNO DELL'ESERCIZIO DI SCRIVANIA
		java.sql.Timestamp firstDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(aUC).intValue());
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(aUC).intValue());		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		if (buonoCarico.getData_registrazione().before(firstDayOfYear)){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Carico non valida. La data di Carico non può essere inferiore di " + formatter.format(firstDayOfYear));
		}
		if (buonoCarico.getData_registrazione().after(lastDayOfYear)){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Carico non valida. La data di Carico non può essere maggiore di " + formatter.format(lastDayOfYear));
		}
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE PER IL BUONO DI CARICO
		if (buonoCarico.getDs_buono_carico_scarico()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare la Descrizione del Buono di Carico in Testata");
	}catch(Throwable t){
		throw handleException(t);		
	}
}

/** 
 *  Aggiorna i beni temporanei per trasferimento.
 *    PreCondition:
 *      E' stata generata la richiesta di aggiornare un bene che si sta scaricando.
 *    PostCondition:
 *      Vengono riportati sulla tabella INVENTARIO_BENI_APG i dati relativi al trasferimento
 *		del bene selezionato.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
 * @param bene il <code>Inventario_beniBulk</code> bene da scaricare selezionato.
**/ 
// metodo che viene richiamato alla validazione dell'inserimento oppure all'inserimento di un nuovo dettaglio
// per aggiornare i campi per il trasferimento
private void modificaBeneTrasferito(UserContext userContext,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene) throws ComponentException {
	try {
	Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
	SQLBuilder sql=home.createSQLBuilder();
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario()); 
	//	 Se il bene che si sta trasferendo è un bene principale, (PROGRESSIVO = 0),
	//	allora il nuovo bene padre viene copiato anche negli EVENTUALI beni accessori
	//	scaricati insieme al padre.
	if ((bene.getNuovo_bene_padre() != null && bene.getNuovo_bene_padre().getPg_inventario() != null)){
		if (bene.isBeneAccessorio())
		  sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	}	
	else
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
	List beni = home.fetchAll(sql);
	for (Iterator i=beni.iterator();i.hasNext();){
		Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)i.next();
		if (bene.getFl_trasf_come_principale() != null)
			 bene_apg.setFl_trasf_come_principale(bene.getFl_trasf_come_principale());
		if (bene.getNuovo_bene_padre() != null && bene.getNuovo_bene_padre().getPg_inventario() != null){
			bene_apg.setPg_inventario_principale(bene.getNuovo_bene_padre().getPg_inventario());
			bene_apg.setNr_inventario_principale(bene.getNuovo_bene_padre().getNr_inventario());
			bene_apg.setProgressivo_principale(bene.getNuovo_bene_padre().getProgressivo());
		}else{
			bene_apg.setPg_inventario_principale(null);
			bene_apg.setNr_inventario_principale(null);
			bene_apg.setProgressivo_principale(null);
		}
		bene_apg.setCd_categoria_gruppo_new(null);
		// viene già controllato che è di tipo (Trasferimento_inventarioBulk)
		if (((Trasferimento_inventarioBulk)buonoS).isFl_cambio_categoria()){
			if (bene.getNuova_categoria() != null && bene.getNuova_categoria().getCd_categoria_gruppo() != null){
				bene_apg.setCd_categoria_gruppo_new(bene.getNuova_categoria().getCd_categoria_gruppo());
			}
			bene_apg.setPg_inventario_principale(null);
			bene_apg.setNr_inventario_principale(null);
			bene_apg.setProgressivo_principale(null);
		}
		bene_apg.setToBeUpdated();
		super.modificaConBulk(userContext,bene_apg);	
	}
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}

/** 
 *  Valida Dettagli - Non ci sono dettagli
 *    PreCondition:
 *      Non sono stati selezionati beni per l'operazione di trasferimento.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Dettagli - Beni senza "nuovo bene padre"
 *    PreCondition:
 *  	 E' stata richiesto il salvataggio di una operazione di Trasferimento Intra-Inventario.
 *		Il trasferimento "Intra-inventario" serve a eseguire una delle seguenti operazioni:
 *		 - rendere un bene principale, (PROGRESSIVO == 0), accessorio di un altro bene;
 *		 - sganciare un bene accessorio ed assegnarlo ad un altro bene, oppure trasformarlo in bene principale.
 *		Nel caso di un bene principale che deve essere trasformato in accessorio di un altro bene,
 *		oppure nel caso di un bene accessorio da assegnare ad un altro bene padre, è necessario indicare il nuovo
 *		bene padre.
 *		Uno o piè beni selezionati, non hanno indicato il "Nuovo Bene Padre".  *
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *  
 *  Valida Dettagli - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di Trasferimento di Inventario. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di proseguire con le operazioni di salvataggio.
 * 
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoScarico il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
private void validaDettagliTrasferiti (UserContext userContext, Trasferimento_inventarioBulk buonoT) throws ComponentException {
	try {
		Inventario_beni_apgHome home =(Inventario_beni_apgHome) getHome(userContext,Inventario_beni_apgBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoT.getLocal_transactionID());
		if (sql.executeCountQuery(home.getConnection())==0){ 
					if (buonoT.isTrasferimentoIntraInv() ||
				(buonoT.isTrasferimentoExtraInv() && !buonoT.getFl_scarica_tutti().booleanValue()))
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare almeno un bene da scaricare!");
		}
		if (buonoT.isTrasferimentoIntraInv()){
			SQLBuilder sql_princ = home.createSQLBuilder();
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoT.getLocal_transactionID());
			sql_princ.openParenthesis("AND");
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",SQLBuilder.EQUALS,0);
			sql_princ.openParenthesis("OR");
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",SQLBuilder.NOT_EQUALS,0);
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TRASF_COME_PRINCIPALE",SQLBuilder.EQUALS,"N");
			sql_princ.closeParenthesis();
			sql_princ.closeParenthesis();
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO_PRINCIPALE",SQLBuilder.ISNULL,"null");
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO_PRINCIPALE",SQLBuilder.ISNULL,"null");
			sql_princ.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_PRINCIPALE",SQLBuilder.ISNULL,"null");

			SQLBuilder sql_cat = home.createSQLBuilder();
			sql_cat.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoT.getLocal_transactionID());
			sql_cat.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CATEGORIA_GRUPPO_NEW",SQLBuilder.ISNOTNULL,"null");
			
			SQLBuilder sql_count = home.createSQLBuilder();
			sql_count.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoT.getLocal_transactionID());
			if (((Trasferimento_inventarioBulk)buonoT).isFl_cambio_categoria() &&
				    sql_cat.executeCountQuery(home.getConnection())!=sql_count.executeCountQuery(home.getConnection()) ){
					StringBuffer msg = new StringBuffer("Attenzione indicare la nuova categoria su tutti i beni selezionati.");
					List Inv_canc=home.fetchAll(sql); 
				    for (Iterator i=Inv_canc.iterator();i.hasNext();){
				    	Inventario_beni_apgBulk canc=(Inventario_beni_apgBulk)i.next();
				    	canc.setToBeDeleted();							
						super.eliminaConBulk(userContext,canc);
				    }
					throw new it.cnr.jada.comp.ApplicationException (msg.toString());					
			}	    
			else if (sql_princ.executeCountQuery(home.getConnection())!=0 && !buonoT.isFl_cambio_categoria()){
				StringBuffer msg = new StringBuffer("Attenzione: ");
				msg.append("un bene principale può essere trasferito solo come accessorio di un altro bene.\n");
				msg.append("Oppure un bene accessorio può essere trasferito o come nuovo bene principale, ");
				msg.append("o come accessorio di un bene padre diverso.\n");
				List Inv_canc=home.fetchAll(sql);
			    for (Iterator i=Inv_canc.iterator();i.hasNext();){
			    	Inventario_beni_apgBulk canc=(Inventario_beni_apgBulk)i.next();
			    	canc.setToBeDeleted();							
					super.eliminaConBulk(userContext,canc);
			    }
				throw new it.cnr.jada.comp.ApplicationException (msg.toString());
			}
		}
	} 
	catch (SQLException e) 
	{
		throw handleException(e);	
	} catch (PersistencyException e) {
		throw handleException(e);
	}
		
}
/**  
 *  Richiama la procedura che provvede ad effettuare il trasferimento dei beni.
 *    PreCondition:
 *      E' stata generata la richiesta di Trasferire dei beni.
 *    PostCondition:
 *      Viene richiamata la procedura di Trafserimento, (CNRCTB400.trasferisciBeni).
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param file il <code>V_ext_cassiere00Bulk</code> file da processare.
**/ 
private void callTrasferisciBeni(
	UserContext userContext, 
	Trasferimento_inventarioBulk buonoT)
	throws  it.cnr.jada.comp.ComponentException {

	String fl_trasf_tutti = null;

	// Parametri relativi all'eventuale Uo di destinazione, (x trasferimento Extra-Inventario)
	long pg_invent_dest = 0;
	String cd_cds_dest = null;
	String cd_uo_dest = null;
	
	int esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue();
	
	if (buonoT.isTrasferimentoExtraInv() && buonoT.getFl_scarica_tutti() != null)
		fl_trasf_tutti = buonoT.getFl_scarica_tutti().booleanValue()?"Y":"N";

	if (buonoT.isTrasferimentoExtraInv() && buonoT.getInventario_destinazione() != null){
		pg_invent_dest = buonoT.getInventario_destinazione().getPg_inventario().longValue();
		if (buonoT.getUo_destinazione() != null){
			cd_cds_dest = buonoT.getUo_destinazione().getCd_unita_padre();
			cd_uo_dest = buonoT.getUo_destinazione().getCd_unita_organizzativa();
		}
	}

	LoggableStatement cs = null;
	try	{
		cs =new LoggableStatement(getConnection(userContext), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB400.trasferisciBeni(?,?,?,?,?,?,?,?,?,?,?,?) }",false,this.getClass());

		cs.setString(1, buonoT.getLocal_transactionID()); 		// LOCAL_TRANSACTION_ID
		cs.setLong(2, buonoT.getPg_inventario().longValue()); 	// PG_INVENTARIO_ORIGINE
		cs.setLong(3, pg_invent_dest); 							// PG_INVENTARIO_ORIGINE
		cs.setInt(4, esercizio); 								// ESERCIZIO
		cs.setString(5, buonoT.getTipoMovimentoCarico().getCd_tipo_carico_scarico()); 		// TIPO_MOVIMENTO_CARICO
		cs.setString(6, buonoT.getTipoMovimentoScarico().getCd_tipo_carico_scarico()); 		// TIPO_MOVIMENTO_SCARICO
		cs.setString(7, buonoT.getDs_buono_carico_scarico().replace("'"," ")); 	// DS_TRASFERIMENTO
		cs.setTimestamp(8, buonoT.getData_registrazione()); 	// DT_REGISRAZIONE
		cs.setString(9, fl_trasf_tutti); 						// FL_TRASFERISCI_TUTTI
		cs.setString(10, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));	// USER
		cs.setString(11, cd_cds_dest);							// CD_CDS_DEST
		cs.setString(12, cd_uo_dest);							// CD_UO_DEST
		
		cs.executeQuery();
	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
}
/**
 *  Aggiorna i dati sul DB, relativi ai Beni.
 *    PreCondition:
 *      E' stata generata la richiesta di creare un Buono di Carico per aumento di valore.
 *    PostCondition:
 *		 Data la collezione dei dettagli del Buono di Carico, aggiorna fisicamente i beni sul DB.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.  
 * @param buonoC <code>Buono_caricoBulk</code> il Buono di Carico.
**/  
private void insertBeniPerAumentoValore (UserContext aUC,Buono_carico_scaricoBulk buonoC, java.util.Vector notChangedBeniKey) 
	throws ComponentException
{

	SimpleBulkList dettagliColl = buonoC.getBuono_carico_scarico_dettColl();
	Buono_carico_scarico_dettBulk dett = new Buono_carico_scarico_dettBulk();
	Inventario_beniBulk bene = new Inventario_beniBulk();
	Iterator i = dettagliColl.iterator();	
	
	while (i.hasNext()){
		dett = (Buono_carico_scarico_dettBulk) i.next();
		bene = dett.getBene();
		java.math.BigDecimal var_piu = (bene.getVariazione_piu() != null?bene.getVariazione_piu():new java.math.BigDecimal(0));

		// Gennaro Borriello - (23/09/2004 16.12.51)
		//	Rich. 840 - Ricalcolo in automatico dell'Imponibile Ammortamento.		
		calcolaVariazione_ImponibileAmmortamento(dett, bene, notChangedBeniKey);
		
		bene.setVariazione_piu(var_piu.add(dett.getValore_unitario()));	
		bene.setDt_validita_variazione(dett.getBuono_cs().getData_registrazione());		
		bene.setToBeUpdated();		

		try{
			updateBulk(aUC,bene);
		}catch (Exception pe){
			throw handleException(bene,pe);
		}		
		dett.setIntervallo(calcolaIntervallo(new BigDecimal(dett.getQuantita().longValue())));
	}
}
/**
 * Calcola la variazione dell'Imponibile Ammortamento.
 *	 E' stato richiesto di salvare un Buono di carico per aumento di valore.
 *	 Il sistema si occupa di allineare automaticamente il campo Imponibile_Ammortamento del Bene,
 *	 tenendo presente il valore assestato:
 *
 *	- il valore del bene, (assestato), è uguale a Imponibile_ammortamento: il campo imponibile ammortamento
 *		viene aggiornato con il valore unitario del dettaglio del Buono;
 *	
 *	- il valore assestato del bene differisce dall'Imponibile_Ammortamento:
 *		il campo Imponibile_ammortamento NON viene aggiornato. Viene salvato l'oggetto Key
 *		del bene, che sarà poi utilizzato per costruire un messaggio di alert per l'utente.
 *	
 * @param bene il <code>Inventario_beniBulk</code> bene da rivalutare.
**/
private void calcolaVariazione_ImponibileAmmortamento (Buono_carico_scarico_dettBulk dett, Inventario_beniBulk bene, java.util.Vector notChangedBeniKey) {
	
	if ( (bene.getImponibile_ammortamento().compareTo(bene.getValoreBene())) == 0 || bene.isMigrato())
		bene.setImponibile_ammortamento(bene.getImponibile_ammortamento().add(dett.getValore_unitario()));
	 else {
		if (notChangedBeniKey == null){
			notChangedBeniKey = new java.util.Vector();
		}
		notChangedBeniKey.add((Inventario_beniKey)bene.getKey());
	}	
}
/**
 *  E' stato richiesto di salvare un Buono di Carico per Aumento di Valore diretto, (NON proveniente da Fattura Passiva).
 *	Durante il salvataggio dei beni, non è stato possibile aggiornare in automatico il campo
 *	IMPONIBILE_AMMORTAMENTO, poichè il valore assestato del bene non è uguale all'imponibile_ammortamento,
 *	(metodo calcolaVariazione_ImponibileAmmortamento()); viene costruito un messaggio per l'utente
 *	che lo avvisa del mancato aggiornamento dei beni.
 *
 *	@return msg <code>String</code> il messaggio all'utente.
**/ 
private String buildBeniNotChanged_Message(java.util.Vector notChangedBeniKey) {

	if (notChangedBeniKey == null)
		return null;

	StringBuffer msg = new StringBuffer();

	msg.append("Attenzione.\n");
	msg.append("Per i seguenti beni non è stato possibile aggiornare il campo IMPONIBILE_AMMORTAMENTO:\n");

	for (Iterator i = notChangedBeniKey.iterator(); i.hasNext();){
		Inventario_beniKey beneKey = (Inventario_beniKey)i.next();
		
		msg.append(beneKey.getNr_inventario().toString());
		msg.append("-");
		msg.append(beneKey.getProgressivo().toString());
		msg.append("\n");
	}

	return msg.toString();
	
}
/**
 *  Inserisce i dati sul DB, relativi ai Beni.
 *    PreCondition:
 *      E' stata generata la richiesta di creare un Buono di Carico.
 *    PostCondition:
 *		 Data la collezione dei dettagli del Buono di Carico, inserisce fisicamente i beni sul DB.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.  
 * @param buonoC <code>Buono_caricoBulk</code> il Buono di Carico.
 * @param dettagliColl la <code>SimpleBulkList</code> collezione dei dettagli del Buono di Carico.
**/  
private void insertBeni (UserContext aUC,Buono_carico_scaricoBulk buonoC, SimpleBulkList dettagliColl) 
	throws ComponentException
{

	Inventario_beniComponentSession inventario_beniComponent = ((it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession.class));
	Buono_carico_scarico_dettBulk dett = new Buono_carico_scarico_dettBulk();
	Inventario_beniBulk bene = new Inventario_beniBulk();
	Iterator i = dettagliColl.iterator();	
	
	while (i.hasNext()){
		dett = (Buono_carico_scarico_dettBulk) i.next();
		bene = dett.getBene();
		bene.setUser(dett.getUser());
		bene.setCd_condizione_bene(dett.getCondizioneBene().getCd_condizione_bene());
		bene.setValore_iniziale(dett.getValore_unitario());
		bene.setValore_ammortizzato(new java.math.BigDecimal(0));
		bene.setCd_categoria_bene(bene.getCategoria_Bene().getCd_categoria_gruppo());
		bene.setCd_ubicazione(bene.getUbicazione().getCd_ubicazione());
		bene.setDt_validita_variazione(buonoC.getData_registrazione());
		bene.setInventario(dett.getBuono_cs().getInventario());
		bene.setFl_totalmente_scaricato(false);
		if (bene.getAssegnatario() != null){
			bene.setCd_assegnatario(bene.getAssegnatario().getCd_terzo());
		}
		try {
			if (!Utility.createConfigurazioneCnrComponentSession().isGestioneEtichettaInventarioBeneAttivo(aUC))
			{
				bene.setEtichetta(assegnaEtichetta(buonoC,bene));
			}
		} catch (RemoteException e) {
			throw new ComponentException(e);
		}



		// 04.12.2003 - Gestione del Esercizio di Creazione del Bene
		//	Assegna al campo ESERCIZIO_CARICO_BENE, l'esercizio di scrivania
		bene.setEsercizio_carico_bene(CNRUserContext.getEsercizio(aUC));
		bene.setDt_acquisizione(buonoC.getData_registrazione());
		// Gennaro Borriello - (16/09/2004 16.46.02)
		bene.setFl_migrato(Boolean.FALSE);
		if (bene.getTipo_ammortamento() != null){
			bene.setTi_ammortamento(bene.getTipo_ammortamento().getTi_ammortamento());
		}
		Transito_beni_ordiniBulk transito_beni_ordiniBulk = null;
		if (buonoC.isByOrdini()){
			Transito_beni_ordiniHome homeTransito = (Transito_beni_ordiniHome)getHome(aUC, Transito_beni_ordiniBulk.class);
			transito_beni_ordiniBulk = new Transito_beni_ordiniBulk();
			transito_beni_ordiniBulk.setId(dett.getIdTransito());
			try {
				transito_beni_ordiniBulk = (Transito_beni_ordiniBulk)homeTransito.findByPrimaryKey(transito_beni_ordiniBulk);
				bene.setTransito_beni_ordini(transito_beni_ordiniBulk);
				if (transito_beni_ordiniBulk != null){
					getHomeCache(aUC).fetchAll(aUC);
				}
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			}
		}


		try{
			makeBulkPersistent(aUC,bene,false);
		}catch (Exception pe){
			throw handleException(pe);			
		}
		if (buonoC.isByOrdini() && transito_beni_ordiniBulk != null){
			try {
				Obbligazione_scadenzarioBulk os = transito_beni_ordiniBulk.getMovimentiMag().getLottoMag().getOrdineAcqConsegna().getObbligazioneScadenzario();
				Obbligazione_scadenzarioHome obblHome = (Obbligazione_scadenzarioHome) getHome(aUC, Obbligazione_scadenzarioBulk.class);
				os = (Obbligazione_scadenzarioBulk)obblHome.findByPrimaryKey(os);
				inventario_beniComponent.creaUtilizzatori(aUC, os, dett);
			} catch (PersistencyException | RemoteException e) {
				throw new ComponentException(e);
			}
		}
	}
}
/**
 *  Elaborazione dei dettagli di un Buono di Carico.
 *    PreCondition:
 *      E' stata generata la richiesta di creare un Buono di Carico.
 *    PostCondition:
 *		 Data la collezione dei dettagli del Buono di Carico, crea una nuova collezione di dettagli:
 *		per ogni dettaglio creato dall'utente vengono creati tanti nuovi dettagli, (che hanno caratteristiche 
 *		del dettaglio originale), ma con quantitè unitaria. La nuova collezione di dettagli sostituirè
 *		la collezione originale in fase di salvataggio.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.  
 * @param buono_cs <code>Buono_caricoBulk</code> il Buono di Carico che contiene i dettagli da elaborare.
 *
 * @return buonoC il <code>Buono_carico_scaricoBulk</code> buono di carico rielaborato.
**/  
private Buono_carico_scaricoBulk esplodiDettagli (UserContext aUC, Buono_carico_scaricoBulk buono_cs, java.util.Hashtable progressivi) 
	throws ComponentException
{
	
	Long nr_inventario = new Long(0);
	Buono_carico_scarico_dettBulk tmpDettaglio;
	Buono_carico_scarico_dettBulk newDettaglio;
	Inventario_beniBulk tmpBene;
	Inventario_beniBulk newBene;
	SimpleBulkList newDettaglioColl = new SimpleBulkList();
	BulkList dettagliPerRigaDiFattura;
	Iterator righe = buono_cs.getBuono_carico_scarico_dettColl().iterator();	
	Inventario_beniHome inventario_bene_home = (Inventario_beniHome) getHome(aUC,Inventario_beniBulk.class);

	// Recupera NR_INVENTARIO per l'Inventario corrente
	try{		
		nr_inventario = inventario_bene_home.getMaxNr_Inventario(buono_cs.getPg_inventario());
		if (nr_inventario.compareTo(new Long(0))==0|| nr_inventario.compareTo(buono_cs.getInventario().getNr_inventario_iniziale().longValue()-1)<0)
			nr_inventario = new Long(buono_cs.getInventario().getNr_inventario_iniziale().longValue()-1);
	}catch (PersistencyException pe){
			throw handleException(pe);
	}
	
	// Comincia ad elaborare ("esplode") la collezione di dettagli
	while (righe.hasNext()){

		tmpDettaglio = (Buono_carico_scarico_dettBulk)righe.next();
		tmpBene = tmpDettaglio.getBene();
		if (!tmpDettaglio.isAccessorioContestuale()){ 
			
			int quantita = tmpDettaglio.getQuantita().intValue();
			intervallo = calcolaIntervallo(new BigDecimal(tmpDettaglio.getQuantita().longValue()));
			dettagliPerRigaDiFattura = new BulkList();
			for (int r = 0; r<quantita; r++){
				newDettaglio = (Buono_carico_scarico_dettBulk)tmpDettaglio.clone();
				newBene = (Inventario_beniBulk)tmpBene.clone();
				
				newDettaglio.setToBeCreated();
				newBene.setToBeCreated();
				
				newDettaglio.setBene(newBene);
				newDettaglio.setIntervallo(intervallo);		
				newDettaglio.setQuantita(new Long("1"));
				newDettaglio.setTotale(newDettaglio.getValore_unitario());
				
				/* Assegna NR_INVENTARIO e PROGRESSIVO al Dettaglio, in base alla 
				* 	condizione del bene: il PROGRESSIVO, difatti viene calcolato in modo
				* 	diverso a seconda che il bene sia oppure no un bene accessorio.
				*/ 
				if (newDettaglio.isBeneAccessorio() && (!newDettaglio.isAccessorioContestuale())){
					// Bene Accessorio di un bene già registrato su DB
					newDettaglio.setNr_inventario(newDettaglio.getBene().getBene_principale().getNr_inventario());
					newDettaglio.setProgressivo(new Integer(getProgressivoDaBenePrincipale(aUC,newDettaglio.getBene().getBene_principale(), progressivi).intValue()));				
				}
				else if (!newDettaglio.isBeneAccessorio()){
					// Bene SENZA Accessori
					newDettaglio.setProgressivo(new Integer(0));					
					nr_inventario = new Long(nr_inventario.longValue()+1);
					newDettaglio.setNr_inventario(nr_inventario);
				}				
				newDettaglio.getBene().setUtilizzatori(estraiUtilizzatoriFor(newDettaglio));
				if (newDettaglio.getBene().getCategoria_Bene()!=null &&  
					newDettaglio.getBene().getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					!newDettaglio.getBene().getCategoria_Bene().getFl_gestione_targa() && newDettaglio.getBene().getTarga()!=null)
					newDettaglio.getBene().setTarga(null);

				if (newDettaglio.getBene().getCategoria_Bene()!=null &&  
						newDettaglio.getBene().getCategoria_Bene().getCd_categoria_gruppo()!=null && 
						!newDettaglio.getBene().getCategoria_Bene().getFl_gestione_seriale() && newDettaglio.getBene().getSeriale()!=null)
						newDettaglio.getBene().setSeriale(null);
					 
				if (!newDettaglio.isAccessorioContestuale()){
					newDettaglioColl.add(newDettaglio);
				}
				if (tmpDettaglio.isAssociatoConAccessorioContestuale()){
					// Bene associato ad un Bene Contestuale
					esplodiDettagliAssociatiContestualmente(buono_cs,newDettaglioColl,tmpDettaglio,newDettaglio);
				}
				dettagliPerRigaDiFattura.add(newDettaglio);
			}
			if (buono_cs.isByFattura())
				buono_cs.sostituisciDettagli_Inventario_Per_Righe_Fattura(tmpDettaglio, dettagliPerRigaDiFattura);
			else if (buono_cs.isByDocumento()) 
				buono_cs.sostituisciDettagli_Inventario_Per_Righe_Documento(tmpDettaglio, dettagliPerRigaDiFattura);
		}
	}
	buono_cs.setBuono_carico_scarico_dettColl(newDettaglioColl);
	return buono_cs;
}
/** 
 *  Richiede il progressivo per i beni accessori.
 *    PreCondition:
 *      E' stato richiesto di recuperare/generare progressivo per i beni accessori relativi 
 *		al bene principale indicato nei parametri.
 *    PostCondition:
 *      Controlla se sono stati già chiesti progressivi per il bene principale di riferimento:
 *		in caso affermativo, restituisce il progressivo incrementato di 1; 
 *		in caso negativo, viene richiesto di resuperare dal DB il progressivo per il bene padre indicato.
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param benePrincipale il <code>Inventario_beniBulk</code> bene padre di riferimento.
 *
 * @return progressivo il <code>Long</code> progressivo recuperato.
**/
private java.lang.Long getProgressivoDaBenePrincipale (UserContext aUC,Inventario_beniBulk benePrincipale, java.util.Hashtable progressivi)
	throws ComponentException
{

	java.lang.Long progressivo = new Long("0");

	Inventario_beniHome beneHome =(Inventario_beniHome) getHome(aUC,Inventario_beniBulk.class);

	if (progressivi.containsKey(benePrincipale.getNr_inventario())){
		progressivo =(Long) progressivi.remove(benePrincipale.getNr_inventario());
		long prog = progressivo.longValue();
		prog++;
		progressivo = new Long(prog);
		progressivi.put(benePrincipale.getNr_inventario(),progressivo);
		
	}
	else{
		try{
			progressivo = beneHome.getMaxProgressivo_Accessorio(benePrincipale);
		} catch(PersistencyException pe){
		} 
		
		long prog = progressivo.longValue();
		prog++;
		progressivo = new Long(prog);
		progressivi.put(benePrincipale.getNr_inventario(),progressivo);		
	}

	return progressivo;

}
/**
 *  Funziona come il metodo <code>esplodiDettagli</code>, ma viene invocato in presenza di 
 *	una riga di dettaglio che determina la creazione di beni accessori di un bene che è stato
 *	creato nello stesso Buono di Carico.
 *
**/ 
private void esplodiDettagliAssociatiContestualmente (
	Buono_carico_scaricoBulk buono_carico,
	SimpleBulkList dettaglioColl,
	Buono_carico_scarico_dettBulk old_padre,
	Buono_carico_scarico_dettBulk new_padre)
	throws ComponentException
{
	
	Buono_carico_scarico_dettBulk newDettaglio;	
	Inventario_beniBulk newBene;
	PrimaryKeyHashtable pkht = buono_carico.getAccessoriContestualiHash();
	BulkList dettagliPerRigaDiFattura;
	Long progressivo = new Long(0);

	BulkList accessori = (BulkList)pkht.get(old_padre.getChiaveHash());
	for (Iterator i = accessori.iterator(); i.hasNext();){		
		dettagliPerRigaDiFattura = new BulkList();
		Buono_carico_scarico_dettBulk dettaglio_associato = (Buono_carico_scarico_dettBulk)i.next();
		Inventario_beniBulk bene_associato = dettaglio_associato.getBene();
		
		intervallo = calcolaIntervallo(new BigDecimal(dettaglio_associato.getQuantita().longValue()));		
		for (int r = 0; r<dettaglio_associato.getQuantita().intValue(); r++){			

			newDettaglio = new Buono_carico_scarico_dettBulk();
			newBene = new Inventario_beniBulk();
			newDettaglio = (Buono_carico_scarico_dettBulk) dettaglio_associato.clone();
			newBene = (Inventario_beniBulk)bene_associato.clone();

			newDettaglio.setToBeCreated();
			newBene.setToBeCreated();
			
			newDettaglio.setIntervallo(intervallo);		
			newDettaglio.setQuantita(new Long("1"));
			newDettaglio.setTotale(newDettaglio.getValore_unitario());
			
			newBene.setNr_inventario(new_padre.getNr_inventario());
			progressivo = new Long(progressivo.intValue()+1);
			newBene.setProgressivo(new Long(progressivo.longValue()));
			newBene.setTipo_ammortamento(new_padre.getBene().getTipo_ammortamento());
			newBene.setFl_ammortamento(new_padre.getBene().getFl_ammortamento());
			newDettaglio.setBene(newBene);			

			dettaglioColl.add(newDettaglio);
			if (buono_carico.isByFattura()||buono_carico.isByDocumento())
				dettagliPerRigaDiFattura.add(newDettaglio);
			
		}
		if (buono_carico.isByFattura())
			buono_carico.sostituisciDettagli_Inventario_Per_Righe_Fattura(dettaglio_associato, dettagliPerRigaDiFattura);
		if (buono_carico.isByDocumento())
			buono_carico.sostituisciDettagli_Inventario_Per_Righe_Documento(dettaglio_associato, dettagliPerRigaDiFattura);
	}		
}
/**
 * Per ogni dettaglio del buono di Carico, ne identifica la collezione di Utilizzaotori e,
 *	 per ogni Utilizzatore, imposta il Bene di riferimento.  
 *
 * @param dettagli la <code>SimpleBulkList</code> lista dei dettagli.
**/
private void completeUtilizzatori (SimpleBulkList dettagli) 
	throws ComponentException
{
	
	SimpleBulkList linee_attivita = new SimpleBulkList();
	SimpleBulkList utilizzatori = new SimpleBulkList();
	
	// Iteratore sui dettagli
	Iterator i_dettagli = dettagli.iterator();

	// Iteratore sugli utilizzatori
	Iterator i_utilizzatori;

	// Iteratore sulle Linee di Attivitè
	Iterator i_linee_attivita;
	
	while (i_dettagli.hasNext()){
		Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)i_dettagli.next();

		// Preleva la collezione di VUtilizzatori per ogni dettaglio
		utilizzatori = dettaglio.getV_utilizzatoriColl();
		i_utilizzatori = utilizzatori.iterator();
		while (i_utilizzatori.hasNext()){
			Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)i_utilizzatori.next();
			linee_attivita = utilizzatore.getBuono_cs_utilizzatoriColl();
			i_linee_attivita = linee_attivita.iterator();

			while (i_linee_attivita.hasNext()){
				Inventario_utilizzatori_laBulk linea_attivita = (Inventario_utilizzatori_laBulk)i_linee_attivita.next();

				// Imposta il bene, prendendo come riferiemento il bene del dettaglio
				linea_attivita.setBene(dettaglio.getBene());
			}
		}
	}
	
}
/**
 * Il metodo serve ad impostare il valore unitario dei Beni, per Buoni di Carico generati da
 *	 Fatture Passive. Il valore del bene viene impostato tenendo presente il valore unitario
 *	 indicato nella riga di Fattura e dell'IVA, nel caso di righe di Fattura di tipo Istituzionale.
 *
 * @param buonoC <code>Buono_caricoBulk</code> il Buono di Carico.
**/
private void validaValoreBeneDaFattura(Buono_carico_scaricoBulk buonoC) {

	// Carica i dettagli del Buono di Carico
	SimpleBulkList dettColl = buonoC.getBuono_carico_scarico_dettColl();
	
	// Carica le righe di Fattura Passiva
	PrimaryKeyHashtable righe_fatturaHash = buonoC.getDettagliRigheHash();
	
	it.cnr.jada.bulk.BulkList dettagli_associati = null;
	java.math.BigDecimal valore_unitario = new java.math.BigDecimal(0);
	java.math.BigDecimal valore_residuo = new java.math.BigDecimal(0);
	java.math.BigDecimal imponibile_totale = new java.math.BigDecimal(0);
	boolean first = true;
	
	for (java.util.Enumeration e = righe_fatturaHash.keys(); e.hasMoreElements();){
		Fattura_passiva_rigaBulk riga_fattura = (Fattura_passiva_rigaBulk)e.nextElement();
		// Controlla se la riga di fattura è ISTITUZIONALE
		if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
			imponibile_totale =riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
		else
			imponibile_totale =riga_fattura.getIm_imponibile();
		valore_unitario = imponibile_totale.divide(riga_fattura.getQuantita(), 2, java.math.BigDecimal.ROUND_HALF_UP);
		valore_residuo = imponibile_totale.subtract(valore_unitario.multiply(riga_fattura.getQuantita()));
		dettagli_associati =(BulkList) righe_fatturaHash.get(riga_fattura);
		for (java.util.Iterator i = dettagli_associati.iterator(); i.hasNext();){
			Buono_carico_scarico_dettBulk dettaglio_inventario = (Buono_carico_scarico_dettBulk)i.next();
			Buono_carico_scarico_dettBulk dettaglio_da_variare = (Buono_carico_scarico_dettBulk)dettColl.get(dettColl.indexOfByPrimaryKey(dettaglio_inventario));
			dettColl.remove(dettaglio_da_variare);
			if (first){
				dettaglio_da_variare.setValore_unitario(valore_unitario.add(valore_residuo));
				dettaglio_da_variare.getBene().setImponibile_ammortamento(dettaglio_da_variare.getBene().getImponibile_ammortamento().add(valore_residuo));
				first = false;
			} else {
				dettaglio_da_variare.setValore_unitario(valore_unitario);
			}
			dettColl.add(dettaglio_da_variare);				
		}
		first = true;			
	
	}
	
}
/** 
 *  Valida Buono - data di carico non specificata
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stata indicata una data di registrazione.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare una data..
 *
 *  Valida Buono - data di carico superiore alla data di sistema
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Carico è superiore alla data di sistema.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - data di carico non valida
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Carico è anteriore all'ultima data di carico registrata sul DB.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - tipo movimento non specificato
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stato specificato il tipo di movimento.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente indicando la necessitè di scegliere il tipo movimento.
 *
 *  Valida Buono - descrizione non specificata
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico di cui non è stata specificata la descrizione.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - dettagli mancanti
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Carico che non ha dettagli.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - categoria bene non specificata
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificata la Categoria Gruppo Inventario del bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - descrizione bene non specificata
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificata la descrizione del bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - condizione bene non specificata
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificata la descrizione del bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - bene accessorio senza bene padre
 *    PreCondition:
 *      Non è stato specificato il bene di riferimento per un dettaglio del Buono di Carico
 *		definito come accessorio di un altro bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente ondocando la necessitè di specificare il bene padre.
 *
 *  Valida Buono - ubicazione bene non specificata
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificata l'ubicazione del bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente indicando la necessitè di specificare l'ubicazione.
 *
 *  Valida Buono - quantitè non specificata
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificata la quantitè.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - valore unitario non specificato
 *    PreCondition:
 *      Per uno dei dettagli del Buono di Carico non è stata specificato il valore unitario dei beni.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - valore da ammortizzare non valido
 *    PreCondition:
 *      E' stato indicato un valore da ammortizzare superiore al valore del bene stesso.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *
 *  Valida Buono - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Carico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Carico con tutti i dettagli ed i beni correlati.
 * 
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoCarico il <code>Buono_caricoBulk</code> Buono di Carico.
**/
private void validaBuonoCarico (UserContext aUC,Buono_carico_scaricoBulk buonoCarico) 
	throws ComponentException
{
	Buono_carico_scarico_dettBulk dett = new Buono_carico_scarico_dettBulk();
	Inventario_beniBulk bene = new Inventario_beniBulk();	
	Iterator i = buonoCarico.getBuono_carico_scarico_dettColl().iterator();

	try{
		// Controlla la testata del Buono di Carico
		validaBuonoCarico_Testata(aUC, buonoCarico);

		// CONTROLLA CHE CI SIA ALMENO UNA RIGA DI DETTAGLIO
		if (buonoCarico.getBuono_carico_scarico_dettColl().size()==0)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il Buono di Carico deve contenere almeno una riga di dettaglio.");

		ArrayList<String> etichettaList=new ArrayList<>();
		/****** INIZIO CONTROLLO SU TUTTE LE RIGHE DI DETTAGLIO ******/
		while (i.hasNext()){


			dett = (Buono_carico_scarico_dettBulk) i.next();
			bene = dett.getBene();
			
			// CONTROLLA CHE SIA STATA SPECIFICATA UNA CATEGORIA PER IL BENE
			if (bene.getCategoria_Bene()==null || bene.getCategoria_Bene().getCd_categoria_gruppo()==null) {
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare la Categoria di appartenenza del Bene '" + (bene.getDs_bene() != null ? "'" + bene.getDs_bene() + "'" : ""));

			}else

			{
				java.util.Collection ti_ammortamenti = ((it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession.class)).findTipiAmmortamento(aUC,bene.getCategoria_Bene());
				dett.getBene().setTi_ammortamenti(ti_ammortamenti);
				dett.getBene().setFl_ammortamento(dett.getBene().getCategoria_Bene().getFl_ammortamento());
				if (ti_ammortamenti.size()==1) 
					dett.getBene().setTipo_ammortamento((it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk)ti_ammortamenti.iterator().next());
			}
			if (!buonoCarico.isPerAumentoValore() && !dett.isBeneAccessorio())
			// CONTROLLA CHE SIA STATA SPECIFICATA UNA targa PER IL BENE
			if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					bene.getCategoria_Bene().getFl_gestione_targa() && bene.getTarga()==null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: è obbligatorio indicare la targa per la Categoria del Bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));
			if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					bene.getCategoria_Bene().getFl_gestione_seriale() && bene.getSeriale()==null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: è obbligatorio indicare il seriale per la Categoria del Bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));

			// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE PER IL BENE
			if (bene.getDs_bene()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare la Descrizione del Bene '" + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));					
			
			// CONTROLLA CHE SIA STATA SPECIFICATA UNA CONDIZIONE PER IL BENE
			if (dett.getCondizioneBene()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Condizione per il Bene '" + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));			
		
			// CONTROLLA, NEL CASO DI UN BENE ACCESSORIO, CHE SIA STATO SPECIFICATO IL BENE PRINCIPALE A CUI FAR RIFERIMENTO
			if (dett.isBeneAccessorio()){
				if (dett.isAccessorioContestuale() && bene.getBene_principale() == null){
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare un Bene Principale per il Bene Accessorio " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));
				} 
				else if (!dett.isAccessorioContestuale() && (bene.getBene_principale()==null || bene.getBene_principale().getNr_inventario() == null || bene.getBene_principale().getDs_bene() == null)){
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare un Bene Principale per il Bene Accessorio " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));
				}				
			}		
			//if (dett.isBeneAccessorio() && (bene.getBene_principale()==null || bene.getBene_principale().getNr_inventario() == null || bene.getBene_principale().getDs_bene() == null))
				//throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare un Bene Principale per il Bene Accessorio " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));

			// CONTROLLA CHE SIA STATA SPECIFICATA UNA UBICAZIONE PER IL BENE
			if (bene.getUbicazione()==null || bene.getUbicazione().getCd_ubicazione()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare l'Ubicazione del Bene" + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));
					
			// CONTROLLA CHE SIA STATO INSERITO LA QUANTITA' PER IL BENE
			if (dett.getQuantita()==null || dett.getQuantita().compareTo(new Long(1))<0)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: la Quantita' del Bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valida.\n La Quantità deve essere maggiore di 0");

			// CONTROLLA CHE SIA STATO INSERITO IL PREZZO UNITARIO PER IL BENE
			if (dett.getValore_unitario()==null || dett.getValore_unitario().compareTo(new java.math.BigDecimal(0))<=0)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare il Prezzo Unitario del Bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));

			// CONTROLLA, NEL CASO DI GESTIONE ATTIVA, CHE SIA STATA IMPOSTATA L'ETICHETTA DEL BENE
			try {
				if (Utility.createConfigurazioneCnrComponentSession().isGestioneEtichettaInventarioBeneAttivo(aUC))
				{
					// il controllo avviene solo per beni "nuovi"
					if(bene.getKey()==null) {
						if (bene.getEtichetta() == null) {
							throw new ApplicationException("E' necessario indicare l'etichetta del bene");
						} else {
							// VERIFICA PRESENZA ETICHETTA SU DB
							if (checkEtichettaBeneAlreadyExist(aUC, dett)) {
								throw new ApplicationException("Attenzione, l'etichetta: " + dett.getEtichetta() + " è già associata ad un altro bene");
							}
							//VERIIFICA ETICHETTA IN LISTA
							else {
								if (etichettaList.size() > 0) {
									if (etichettaList.contains(dett.getEtichetta())) {
										throw new ApplicationException("Attenzione, l'etichetta: " + dett.getEtichetta() + " è già inserita in lista");
									}
								}
								etichettaList.add(bene.getEtichetta());

							}

						}
					}
				}
			} catch (RemoteException e) {
				throw new ComponentException(e);
			}


		}
		if (buonoCarico.isPerAumentoValore() && (dett.getValore_unitario() == null || (dett.getValore_unitario().compareTo(new java.math.BigDecimal(0))==0))){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare il Valore Caricato per il bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":""));
		}

		if ( ((Buono_carico_scaricoBulk)dett.getBuono_cs()).isPerAumentoValore() ){
			// Buono di Carico per aumento di valore
			// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE DEL BENE
			java.math.BigDecimal valore_bene = dett.getBene().getValoreBene().add(dett.getValore_unitario());
			if (dett.getBene().getImponibile_ammortamento() != null && dett.getBene().getImponibile_ammortamento().compareTo(valore_bene)>0){
				throw new ValidationException("Attenzione: il valore da ammortizzare di un bene deve essere inferiore  o uguale al valore del bene.\n" +
						"Il valore da ammortizzare del bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valido");
			}
		} else {
			// Buono di Carico normale
			// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE UNITARIO
			if (dett.getBene().getImponibile_ammortamento() != null && dett.getBene().getImponibile_ammortamento().compareTo(dett.getValore_unitario())>0){
				throw new ValidationException("Attenzione: il valore da ammortizzare di un bene deve essere inferiore  o uguale al valore del bene.\n" +
						"Il valore da ammortizzare del bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valido");
			}
			// V.T. Imposta valore imponibile ammortamento
			if(dett.getBene().getImponibile_ammortamento() == null){
				dett.getBene().setImponibile_ammortamento(dett.getBene().getValoreBene());
			}
		}

	}catch(Throwable t){
		throw handleException(dett, t);		
	}
 }
/** 
 *  Validazione del Buono di Scarico.
 *    PreCondition:
 *      ValidaBuonoScarico  non superato.
 *    PostCondition:
 *      Non  viene consentita la registrazione del buono di scarico.
 *   
 *  Tutti i controlli superati.
 *    PreCondition:
 *      Nessun errore rilevato.
 *    PostCondition:
 *      Viene consentito il salvataggio del Buono di Scarico.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bulk <code>OggettoBulk</code> il Bulk da creare
 *
 * @return l'oggetto <code>OggettoBulk</code> creato
**/
private OggettoBulk creaBuonoScaricoConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)bulk;
		
		// Valida il Buono di Scarico
		validaBuonoScarico(userContext, buonoS);
		if (!buonoS.isByFattura() && !buonoS.isByDocumento() && !buonoS.isByOrdini()){
		// Setto il progressivo del Buono Scarico
			Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHome(userContext,Numeratore_buono_c_sBulk.class);
		
			try{ 
					buonoS.setPg_buono_c_s(numHome.getNextPg(
							userContext,
							buonoS.getEsercizio(),
							buonoS.getPg_inventario(),
							buonoS.getTi_documento(),
							userContext.getUser()));
				
			}catch (Throwable e){
				throw new ComponentException (e);
			}
		}
		if (!buonoS.isByFattura()&& !buonoS.isByDocumento()&& !buonoS.isByOrdini()){
			String msg = null;
			msg = makePersistentScarico(userContext, buonoS);

			// La procedura per lo scarico dei beni ha restituito un messaggio di Alert
			if (msg != null){
				return asMTU(buonoS, msg);
			}
		}
		return buonoS;
		
	}catch (Throwable e){
		throw handleException(bulk, e);
	}
}
/** 
 *  Valida Data Buono - data non valida
 *    PreCondition:
 *      La data del Buono di Scarico non supera i controlli di validazione, (metodo validaDataBuonoScarico).
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare una data.
 *  
 *  Valida dettagli - dettagli non validi
 *    PreCondition:
 *      I dettagli del Buono di Scarico non passano la validazione, (metodo validaDettagliAssociati).
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente con la spiegazione dell'errore.
 *  
 *  Valida Buono - data di scarico inferiore al valore consentito
 *    PreCondition:
 *      La data di registrazione indicata per il Buono di Scarico è anteriore alla MAX(data_ultima_modifica)
 *		registrata per i beni scaricati.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *  
 *  Valida Buono - tipo movimento non specificato
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Scarico di cui non si è specificato il tipo di movimento.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *  
 *  Valida Buono - descrizione mancante
 *    PreCondition:
 *      Non è stata specificata una descrizione per il Buono di Scarico.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente indicando la necessitè di specificare la descrizione.
 *  
 *  Buono di Scarico generato da Fattura Attiva - Valida quadratura non superato
 *    PreCondition:
 *		 Si sta tentando di salvare un Buono di Scarico generato da una Fattura Attiva. 
 *		I beni scaricati non passano i controlli sulla quadratura dei valori alienazione, (metodo validaQuadratura).
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente con la spiegazione dell'errore.
 *  
 *  Valida Buono - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Scarico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Scarico con tutti i dettagli ed i beni correlati.
 * 
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoScarico il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
private void validaBuonoScarico (UserContext aUC,Buono_carico_scaricoBulk buonoScarico) 
	throws ComponentException
{

	Buono_carico_scarico_dettBulk dett = new Buono_carico_scarico_dettBulk();
	
	try{
		// Controlla la validità della data indicata nel Buono di Scarico
		validaDataBuonoScarico(aUC, buonoScarico);

		// CONTROLLA LA DATA DI SCARICO - DATA DI SCARICO < ULTIMA DATA DI SCARICO REGISTRATA
		//java.sql.Timestamp data_ultimo_scarico = ((Buono_carico_scaricoHome)getHome(aUC,Buono_carico_scaricoBulk.class)).getData_di_Scarico(buonoScarico);
		//if (data_ultimo_scarico!=null && buonoScarico.getData_registrazione().before(data_ultimo_scarico))
			//throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Scarico non valida. La Data di Scarico non puè essere minore dell'ultima data registrata");

		// Controlla che ci siano beni associati alla Fattura e che abbiano un VALORE_ALIENAZIONE valido
		validaDettagliAssociati(aUC,buonoScarico);
	
		/* CONTROLLA CHE LA DATA DI SCARICO NON SIA ANTECEDENTE ALLA MAX(DATA) DELLE
		 *	MODIFICHE SUI SINGOLI BENI SCARICATI
		*/
		if (buonoScarico.getData_registrazione().before(getMaxDataFor(aUC, buonoScarico))){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: data di Scarico non valida.\n La Data di Scarico non può essere precedente ad una modifica di uno dei beni scaricati");
		}
		
		// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI SCARICO
		if (buonoScarico.getTipoMovimento()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento per il Buono di Scarico");

		// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE 
		if (buonoScarico.getDs_buono_carico_scarico()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Descrizione per il Buono di Scarico");
		if (buonoScarico.isByFattura()||buonoScarico.isByDocumento()){
			// CONTROLLO DI QUADRATURA	
			validaQuadratura(aUC, buonoScarico);
		}
		
	} catch(Throwable t){
		throw handleException(dett, t);		
	}
}
/** 
 *  Modifica Buono di Carico
 *    PreCondition:
 *      E' stata generata la richiesta di modificare un Buono di Carico.
 *    PostCondition:
 *      Viene consentito il salvataggio.
 *  
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param bulk <code>OggettoBulk</code> il Bulk da modificare
 *
 * @return l'oggetto <code>OggettoBulk</code> modificato
**/ 
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) 
	throws ComponentException
{
  try{
  Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bulk;
  if (buono_cs.getTi_documento().equals(Buono_carico_scaricoBulk.CARICO)){
	 SimpleBulkList dettagli = buono_cs.getBuono_carico_scarico_dettColl(); 
	 for (Iterator i=dettagli.iterator();i.hasNext();){
		Buono_carico_scarico_dettBulk dettaglio =(Buono_carico_scarico_dettBulk)i.next();
		dettaglio.getBene().setValore_unitario(dettaglio.getValore_unitario());
			if (!dettaglio.getBuono_cs().getTipoMovimento().getFl_aumento_valore())
				dettaglio.getBene().setValore_iniziale(dettaglio.getValore_unitario());
			else
				dettaglio.getBene().setVariazione_piu(dettaglio.getBene().getVariazione_piu().add(dettaglio.getTotale().negate()).add(dettaglio.getValore_unitario()));
			assegnaStatoCOGE(aUC,buono_cs);
			/***************** BORRIELLO - Rich. 693 **********************/
			// 	Se lo stato del dettaglio è 'C', (Contabilizzato), viene riportato ad 'R', (da riprocessare)
			if (dettaglio.getStato_coge().equals(Buono_carico_scarico_dettBulk.STATO_COGE_C)){
				dettaglio.setStato_coge(Buono_carico_scarico_dettBulk.STATO_COGE_R);
			}
			dettaglio.getBene().setImponibile_ammortamento(dettaglio.getBene().getValoreBene());
			validaDettaglioPerModifica(aUC,dettaglio);
			updateBulk(aUC, dettaglio.getBene());
			updateBulk(aUC, dettaglio);
	   }
	}else{
			Buono_carico_scarico_dettHome dettHome = (Buono_carico_scarico_dettHome)getHome(aUC,Buono_carico_scarico_dettBulk.class);
			buono_cs.setBuono_carico_scarico_dettColl(new BulkList(dettHome.getDetailsFor(buono_cs)));
			SimpleBulkList dettagli = buono_cs.getBuono_carico_scarico_dettColl(); 
			for (Iterator i=dettagli.iterator();i.hasNext();){
				Buono_carico_scarico_dettBulk dettaglio =(Buono_carico_scarico_dettBulk)i.next();
				Inventario_beniBulk inv =(Inventario_beniBulk)getHome(aUC,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(dettaglio.getNr_inventario(),dettaglio.getPg_inventario(),new Long(dettaglio.getProgressivo().longValue())));
				dettaglio.setBene(inv);
				dettaglio.CalcolaTotaleBene();
				assegnaStatoCOGE(aUC,buono_cs);
				updateBulk(aUC, dettaglio);
			}
		}
	}
 catch (PersistencyException e){
		throw new it.cnr.jada.comp.ComponentException(e);
	} 
	return super.modificaConBulk(aUC,bulk);
}

/**    
 *  Rende persistente il Buono di Scarico
 *    PreCondition:
 *      E' stata generata la richiesta di rendere persistente su DB il Buono di Scarico ed
 *		i dettagli ad esso correlati.
 *    PostCondition:
 *      Viene richiamata la procedura che scriverè sulle tabelle dell'Inventario i dati inseriti dall'utente.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param buonoS il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
public String makePersistentScarico(UserContext aUC, Buono_carico_scaricoBulk buonoS) 
	throws ComponentException, it.cnr.jada.comp.ApplicationException 
{
	String msg = null;	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement  cs = null;
	try
	{
		cs = new LoggableStatement(getConnection( aUC ),"{call " + schema +
				"CNRCTB400.updScaricoInventarioBeni(?,?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.setString(1, buonoS.getLocal_transactionID()); 		// local_trans_id
		cs.setLong(2, buonoS.getPg_inventario().longValue()); 	// pg_inventario
		cs.setInt(3, buonoS.getEsercizio().intValue()); 		// esercizio
		cs.setLong(4, buonoS.getPg_buono_c_s().longValue()); 		// pg_buono_carico_scarico
		cs.setString(5, buonoS.getDs_buono_carico_scarico()); 				// ds_buono_carico_scarico
		cs.setString(6, buonoS.getCd_tipo_carico_scarico()); 			// cd_tipo_carico_scarico
		cs.setString(7, buonoS.getUser()); 						// utente
		cs.setString(8, (buonoS.isByFattura())?"Y":"N"); 				// da_fattura
		cs.setString(9,"N");									//tipo_fattura
		cs.setTimestamp(10, buonoS.getData_registrazione()); 	// data_registrazione
		cs.registerOutParameter(11, java.sql.Types.VARCHAR); // Eventuale Messaggio di ritorno
	
		cs.executeQuery();
		msg= cs.getString(11);
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw handleSQLException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	return msg;
}
/** 
 *  Valida Dettagli - Non ci sono dettagli
 *    PreCondition:
 *      Si sta tentando di salvare un Buono di Scarico che non ha dettagli.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare almeno un dettaglio.
 *
 *  Valida Dettagli - Valore da scaricare non specificato
 *    PreCondition:
 *      Si sta tentando di salvare un dettaglio di cui non è stato specificato il valore da scaricare del bene.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare il valore.
 *
 *  Valida Dettagli da Fattura - Beni eterogenei
 *    PreCondition:
 *      Il Buono che si sta salvando è stato generato a partire da una Fattura Attiva.
 *		Si sono specificati, per una stessa riga di Fattura, beni di tipo diverso (ISTITUZIONALE/COMMERCIALE).
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilitè
 *		di associare ad una riga di Fattura beni di tipo diverso.
 *  
 *  Valida Dettagli - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Scarico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Scarico con tutti i dettagli ed i beni correlati.
 * 
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoScarico il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
private void validaDettagliAssociati (UserContext userContext, Buono_carico_scaricoBulk buonoS) throws ComponentException {

	boolean hasNoDetails = true;
	/* Query per il controllo dell'omogeneitè dei tipi (ISTITUZIONALE/COMMERCIALE) dei beni
	 * 	associati ad ogni riga di Fattura: la query permette di stabilire se, per una
	 *	riga di Fattura, sono stati associati beni di tipo diverso.
	*/	
	try {	
		Inventario_beni_apgHome apgHome =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
		SQLBuilder sql=apgHome.createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
		List beniApg=apgHome.fetchAll(sql);
		for (Iterator i=beniApg.iterator();i.hasNext();){ 
			Inventario_beni_apgBulk beneApg =(Inventario_beni_apgBulk)i.next();
			/*Inventario_beniBulk bene =(Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(beneApg.getNr_inventario(),beneApg.getPg_inventario(),beneApg.getProgressivo()));
			Categoria_gruppo_inventBulk cat =(Categoria_gruppo_inventBulk)getHome(userContext,Categoria_gruppo_inventBulk.class).findByPrimaryKey(new Categoria_gruppo_inventBulk(bene.getCd_categoria_bene()));
			
			if(cat!=null && cat.getData_cancellazione()!= null && buonoS.getData_registrazione()!=null &&
					cat.getData_cancellazione().before(buonoS.getData_registrazione()))
				throw new ApplicationException("Il Bene "+bene.getNr_inventario()+" ha un categoria non più valida");
			*/
			hasNoDetails = false;
			if (beneApg.getFl_totalmente_scaricato()!=null && !beneApg.getFl_totalmente_scaricato()){
				if (beneApg.getVariazione_meno() == null || beneApg.getVariazione_meno().compareTo(new java.math.BigDecimal(0))==0)
					throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare un valore per ogni bene scaricato.\n" + 
								" Indicare un valore per il bene " + beneApg.getNr_inventario() + "-" + beneApg.getProgressivo());
				}
		}
		if (hasNoDetails){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare almeno un bene da scaricare!");
		}
		//r.p. eliminato controllo fattura a si per il tipo istituzionale commerciale???
	}catch (PersistencyException e) {
		throw handleException(e);
	}
}
/**
 * Restituisce, tra tutti i beni movimentati nel Buono di Scarico, la MAX(dt_validita_variazione), 
 *	 ossia, la data corrispondente alla modifica piè recente.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param Buono_carico_scaricoBulk il <code>Buono_carico_scaricoBulk</code> Buono di Scarico.
 *
 * @return max_data <code>Timestamp</code> il valore cercato.

**/
private java.sql.Timestamp getMaxDataFor(UserContext userContext, Buono_carico_scaricoBulk buonoS) throws ComponentException {	
	Timestamp max_data=null;
	try{
		if ((buonoS instanceof Trasferimento_inventarioBulk)
				&& (((Trasferimento_inventarioBulk)buonoS).isTrasferimentoExtraInv() && ((Trasferimento_inventarioBulk)buonoS).getFl_scarica_tutti().booleanValue())){
			Inventario_beniHome home=(Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
			max_data = home.getMaxDataFor(userContext,buonoS.getInventario());
		} else {
			Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
			max_data = home.getMaxDataFor(buonoS.getLocal_transactionID());
		}
	}catch (PersistencyException pe){
		throw handleException(pe);
	}
	return max_data;	
}
/** 
 *  Valida Quadratura - Qaudratura non valida
 *    PreCondition:
 *      Il Buono di Scarico che si sta creando, è stato generato a partire da una Fattura
 *		Attiva. Il totale dei Valore Alienazione specificati per i beni associati ad una riga 
 *		di Fattura non corrispondono col valore atteso.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.
 *  
 *  Valida Quadratura - Tutti i controlli superati.
 *    PreCondition:
 *      E' stata richiesta una operazione di creazione di un Buono di Scarico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di creare il Buono di Scarico con tutti i dettagli ed i beni correlati.
 * 
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param buonoScarico il <code>Buono_scaricoBulk</code> Buono di Scarico.
**/
private void validaQuadratura (it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buonoS) 
	throws ComponentException
{
try{
	int quadratura;
	java.math.BigDecimal totale_alienazione = new java.math.BigDecimal(0);
	java.math.BigDecimal im_fattura = new java.math.BigDecimal(0);
	
	Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
	if (buonoS.getDettagliFatturaColl().size()!=0 && buonoS.getDettagliFatturaColl().get(0) instanceof Fattura_attiva_rigaIBulk){
		for (Iterator i = buonoS.getDettagliFatturaColl().iterator(); i.hasNext();){
			SQLBuilder sql=home.createSQLBuilder();
			Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)i.next(); 
				
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_attiva());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,"0");
			List beni=home.fetchAll(sql);
			for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();
				totale_alienazione=totale_alienazione.add(bene_apg.getValore_alienazione());
			}
			im_fattura = im_fattura.add(riga_fattura.getIm_imponibile());	
		
			quadratura = totale_alienazione.compareTo(im_fattura);
			if (quadratura!=0)				
					throw new ApplicationException(
						"Attenzione: il totale dei Valori Alienazione dei beni associati ad una riga di Fattura " + 
						"deve corrispondere al totale della riga stessa.\n " + "Il totale della riga di Fattura '" + 
						riga_fattura.getDs_riga_fattura() + "' non corrisponde con il totale dei beni ad essa associati.\n " +
						"Il valore previsto è " + im_fattura);
			
		}
	}else if (buonoS.getDettagliFatturaColl().size()!=0 && buonoS.getDettagliFatturaColl().get(0) instanceof Nota_di_credito_rigaBulk){
		Set<Inventario_beniBulk> beniScelti = new HashSet<>();
		for (Iterator i = buonoS.getDettagliFatturaColl().iterator(); i.hasNext();){
			SQLBuilder sql=home.createSQLBuilder();
			Nota_di_credito_rigaBulk riga_fattura = (Nota_di_credito_rigaBulk)i.next(); 
				
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga_fattura.getCd_cds());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga_fattura.getEsercizio());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga_fattura.getProgressivo_riga());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
			//sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,"0");
			List beni=home.fetchAll(sql);
			for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();
				totale_alienazione=totale_alienazione.add(bene_apg.getVariazione_meno());
				Inventario_beniBulk inventario_beniBulk = new Inventario_beniBulk(bene_apg.getNr_inventario(), bene_apg.getPg_inventario(), bene_apg.getProgressivo());
				if (beniScelti.isEmpty() || !beniScelti.contains(inventario_beniBulk)){
					inventario_beniBulk.setValore_iniziale(bene_apg.getVariazione_meno());
					beniScelti.add(inventario_beniBulk);
				} else {
					Inventario_beniBulk beneScelto = beniScelti.stream()
	 						.filter(bene -> inventario_beniBulk.equals(bene))
							.findAny()
							.orElse(null);
					beneScelto.setValore_iniziale(beneScelto.getValoreBene().add(bene_apg.getVariazione_meno()));
					beneScelto.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
				}
			}
			if (riga_fattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value()))
				im_fattura = im_fattura.add(riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva()));
			else
				im_fattura = im_fattura.add(riga_fattura.getIm_imponibile());
				quadratura = totale_alienazione.compareTo(im_fattura);
			if (quadratura!=0)				
					throw new ApplicationException(
						"Attenzione: il totale delle Variazioni dei beni associati ad una riga di NC " + 
						"deve corrispondere al totale della riga stessa.\n " + "Il totale della NC '" + 
						riga_fattura.getDs_riga_fattura() + "' non corrisponde con il totale dei beni ad essa associati.\n " +
						"Il valore previsto è " + im_fattura);
			
		}
		for (Inventario_beniBulk inv : beniScelti) {
			Inventario_beniHome homeInv=(Inventario_beniHome)getHome(userContext, Inventario_beniBulk.class);
			if (inv.isToBeUpdated()){
				Inventario_beniBulk beneDB = (Inventario_beniBulk)homeInv.findByPrimaryKey(userContext, new Inventario_beniBulk(inv.getNr_inventario(), inv.getPg_inventario(), inv.getProgressivo()));
				if (beneDB.getValoreBene().compareTo(inv.getValoreBene()) < 0){
					throw new ApplicationException(
							"Attenzione: il bene " + beneDB.getNr_inventario()+"-"+beneDB.getPg_inventario()+
									" è stato scaricato per un importo maggiore del suo valore.");
				}
			}
		}
	}
	else if (buonoS.getDettagliDocumentoColl().size()!=0 && buonoS.getDettagliDocumentoColl().get(0) instanceof Documento_generico_rigaBulk){
		for (Iterator i = buonoS.getDettagliDocumentoColl().iterator(); i.hasNext();){
			SQLBuilder sql=home.createSQLBuilder();
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next(); 
				
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_CDS",sql.EQUALS,riga.getCd_cds());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,riga.getCd_unita_organizzativa());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.ESERCIZIO",sql.EQUALS,riga.getEsercizio());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_FATTURA",sql.EQUALS,riga.getPg_documento_generico());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga.getCd_tipo_documento_amm());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_RIGA",sql.EQUALS,riga.getProgressivo_riga());
			sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
			//sql.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",sql.EQUALS,"0");
			List beni=home.fetchAll(sql);
			for (Iterator iteratore=beni.iterator();iteratore.hasNext();){
				Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)iteratore.next();
				totale_alienazione=totale_alienazione.add(bene_apg.getValore_alienazione());
			}
			
				im_fattura = im_fattura.add(riga.getIm_riga());
				quadratura = totale_alienazione.compareTo(im_fattura);
			if (quadratura!=0)				
					throw new ApplicationException(
						"Attenzione: il totale delle Variazioni dei beni associati ad una riga di documento generico attivo " + 
						"deve corrispondere al totale della riga stessa.\n " + "Il totale della documento generico attivo '" + 
						riga.getDs_riga() + "' non corrisponde con il totale dei beni ad essa associati.\n " +
						"Il valore previsto è " + im_fattura);
			
		}
	}
} catch (PersistencyException e) {
		throw handleException(e);
}

}
/**  
 *  Inizializzazione di una istanza di Trasferimento_inventarioBulk
 *    PreCondition:
 *      Il Buono di Scarico che si sta inizializzando è un buono di Trasferimento di Inventario
 *    PostCondition:
 *		 Vengono impostate le informazioni relative a Consegnatario, Delegato e UO Resp.
 *		 dell'Inventario a cui è associata la UO di scrivania.
 *  
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param bulk <code>OggettoBulk</code> il buono che deve essere istanziato
**/
private void inizializzaBuonoTrasferimentoPerInserimento (UserContext userContext, OggettoBulk bulk) throws ComponentException {

	if (bulk instanceof Trasferimento_inventarioBulk){

		Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk)bulk;
		try {

			// Inizializzo le informazioni relative a Consegnatario, Delegato e UO Resp.
			Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(userContext, Id_inventarioBulk.class);			
			buonoT.setConsegnatario(inventarioHome.findConsegnatarioFor(buonoT.getInventario()));
			buonoT.setDelegato(inventarioHome.findDelegatoFor(buonoT.getInventario()));
			buonoT.setUo_consegnataria(inventarioHome.findUoRespFor(userContext,buonoT.getInventario()));

			// Inizializzo la Uo di Destinazione
			buonoT.setUo_destinazione(new Unita_organizzativaBulk());
			
			//return buonoS;
		}catch(PersistencyException ex){
			throw handleException(bulk,ex);
		}catch(it.cnr.jada.persistency.IntrospectionException ex){
			throw handleException(bulk,ex);
		}
	}
}
/**
 *  Controlla i beni del Buono da eliminare - beni associati a Fatture
 *    PreCondition:
 *      E' stata generata la richiesta di cancellare fisicamente un Buono di Carico.
 *		I beni del Buono sono associati a Fattura Passiva/Attiva.
 *    PostCondition:
 *      Viene visualizzato un messaggio che avvisa l'utente dell'impossibilitè di cancellare
 *		il Buono.
 *
 *  Controlla i beni del Buono da eliminare - tutti i controlli superati.
 *    PreCondition:
 *      Tutti i controlli superati.
 *    PostCondition:
 *      Consente di proseguire con le operazioni di cancellazione del Buono.
 * 
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
 * @param buono <code>Buono_caricoBulk</code> il Buono da eliminare.
**/ 
private void checkBeniAssociatiPerElimina(UserContext userContext, Buono_carico_scaricoBulk buono) throws ComponentException{

	try{
		SQLBuilder sql = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_INVENTARIO", sql.EQUALS, buono.getPg_inventario());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO", sql.EQUALS, buono.getEsercizio());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.TI_DOCUMENTO", sql.EQUALS, buono.getTi_documento());
		sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_BUONO_C_S", sql.EQUALS, buono.getPg_buono_c_s());
		
	/*	vecchio controllo 
	 * SQLBuilder sql_dett = getHome(userContext, Buono_carico_scarico_dettBulk.class).createSQLBuilder();
		sql_dett.addTableToHeader("ASS_INV_BENE_FATTURA");
		sql_dett.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO", sql.EQUALS, buono.getPg_inventario());
		sql_dett.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO", sql.EQUALS, buono.getEsercizio());
		sql_dett.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO", sql.EQUALS, buono.getTi_documento());
		sql_dett.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S", sql.EQUALS, buono.getPg_buono_c_s());
		sql_dett.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_BUONO_C_S", sql.ISNULL,"NULL");
		sql_dett.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql_dett.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql_dett.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
		
		sql.addSQLExistsClause("OR",sql_dett); */
		if (sql.executeCountQuery(getConnection(userContext))>0)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: operazione non possibile!\nUno o più beni del Buono risultano associati con righe di Documenti amministrativi.");
			
	} catch (Throwable t){
		throw new ComponentException (t);
	} 
}
/** 
 *  Elimina Utilizzatori - tutti i controlli superati
 *    PreCondition:
 *      E' stata generata la richiesta di cancellare fisicamente un Buono di Carico 
 *		e tutti i beni ad esso associati e tutti i controlli sono andati a buon fine.
 *    PostCondition:
 *      Cancella gli utilizzatori dei beni associati al Buono di Carico specificato.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
 * @param bulk <code>OggettoBulk</code> il Buono da eliminare.
**/ 
private void eliminaUtilizzatoriBuono (UserContext userContext,Inventario_beniBulk bene) throws ComponentException {
	try{
		Inventario_utilizzatori_laHome inv_lahome =(Inventario_utilizzatori_laHome)getHome(userContext,Inventario_utilizzatori_laBulk.class);
		SQLBuilder sql_la= inv_lahome.createSQLBuilder();
		sql_la.addSQLClause("AND","PG_INVENTARIO",SQLBuilder.EQUALS,bene.getPg_inventario());
		sql_la.addSQLClause("AND","NR_INVENTARIO",SQLBuilder.EQUALS,bene.getNr_inventario());
		sql_la.addSQLClause("AND","PROGRESSIVO",SQLBuilder.EQUALS,bene.getProgressivo());
		List LA =inv_lahome.fetchAll(sql_la);
		for(Iterator i=LA.iterator();i.hasNext();){
				Inventario_utilizzatori_laBulk util_la =(Inventario_utilizzatori_laBulk)i.next();
				util_la.setToBeDeleted();							
				super.eliminaConBulk(userContext,util_la);
		}
		
	}catch(PersistencyException ex){
		throw handleException(ex);
	}
}
/** 
 *  Valida dettaglio per modifica - valore unitario non specificato
 *    PreCondition:
 *      Si sta tentando di salvare un dettaglio di cui non è stato indicato il valore unitario.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessitè di specificare un valore.

 *  Valida dettaglio per modifica - valore da ammortizzare inferiore al valore consentito
 *    PreCondition:
 *      Il valore da ammortizzare indicato per il bene, è inferiore al valore già ammortizzato.
 *    PostCondition:
 *      Un messaggio di errore viene visualizzato all'utente.  
 *
 *  Valida dettaglio per modifica - Tutti i controlli superati
 *    PreCondition:
 *      E' stata richiesta una operazione di modifica di un Buono di Carico. Tutti i controlli superati.
 *    PostCondition:
 *      Consente di modificare il Buono di Carico con tutti i dettagli ed i beni correlati.
 * 
 
 * @param dettaglio il <code>Buono_carico_scaricodettBulk</code> dettaglio da controllare.
**/
private void validaDettaglioPerModifica(UserContext userContext,Buono_carico_scarico_dettBulk dettaglio) throws ComponentException{
	try{		
		Inventario_beniBulk bene = dettaglio.getBene();
		// VALORE NON SPECIFICATO
		if (dettaglio.getValore_unitario()== null || (dettaglio.getValore_unitario().compareTo(new java.math.BigDecimal(0)))==0){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un valore Unitario");
		}

		if (bene.getValore_ammortizzato() != null){		
			// CONTROLLA CHE IL VALORE DA AMMORTIZZARE NON SIA INFERIORE AL VALORE AMMORTIZZATO
			if (bene.getImponibile_ammortamento() == null || bene.getImponibile_ammortamento().compareTo(bene.getValore_ammortizzato())<0){				
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: il valore da ammortizzare di un bene non può essere inferiore al valore già ammortizzato del bene.\n" + 
						"Il valore da ammortizzare del bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valido");
			}
		}
		
	} catch (Throwable t){
		throw new ComponentException (t);
	}
}
public RemoteIterator selectEditDettagliScarico(
		UserContext userContext,
		Buono_carico_scaricoBulk buonoS,
		Class bulkClass, 
		CompoundFindClause clauses) throws ComponentException {	

		if (buonoS == null || buonoS.getPg_buono_c_s()==null)
			return new it.cnr.jada.util.EmptyRemoteIterator();
		
		SQLBuilder sql = getHome(userContext, Buono_carico_scarico_dettBulk.class).createSQLBuilder();
		
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
		sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoS.getTi_documento());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoS.getEsercizio());
		sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoS.getPg_buono_c_s());
		

		it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
		
		return ri;	
	}
 public boolean verifica_associazioni(UserContext userContext,Buono_carico_scarico_dettBulk dettaglio)throws ComponentException
 {
	 try{
	Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(userContext,Buono_carico_scarico_dettBulk.class);
	return home.isAssociato_documento(dettaglio);
	 }catch (SQLException ex) {
			throw handleException(ex);	
	}
 }	
 public boolean verifica_associazioni(UserContext userContext,Buono_carico_scaricoBulk buono)throws ComponentException
 {
	 try{
	Buono_carico_scaricoHome home=(Buono_carico_scaricoHome)getHome(userContext,Buono_carico_scaricoBulk.class);
	return home.isAssociato_documento_testata(buono);
	 }catch (SQLException ex) {
			throw handleException(ex);	
	}
 }	
 public boolean isNonUltimo(UserContext userContext,Buono_carico_scarico_dettBulk dettaglio)throws ComponentException
 {
	 try{
	Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(userContext,Buono_carico_scarico_dettBulk.class);
	return home.isNonUltimo(dettaglio);
	 }catch (SQLException ex) {
			throw handleException(ex);	
	}
 }	
 public Long findMaxAssociazione(UserContext userContext,Ass_inv_bene_fatturaBulk ass) throws ComponentException {
try{
			Ass_inv_bene_fatturaHome home=(Ass_inv_bene_fatturaHome)getHome(userContext,Ass_inv_bene_fatturaBulk.class);
			return home.findmax(ass);
			 } catch (PersistencyException e) {
				throw handleException(e);	
		}

 }
 public OggettoBulk modificaBeniAssociatiConBulk (UserContext userContext, Fattura_attiva_rigaIBulk riga_fattura, Inventario_beniBulk bene) throws ComponentException{
		return bene;
 }
 public OggettoBulk modificaBeniAssociatiConBulk (UserContext userContext, Nota_di_credito_rigaBulk riga_fattura, Inventario_beniBulk bene) throws ComponentException{
		return bene;
}
public OggettoBulk modificaBeniAssociatiConBulk (UserContext userContext, Documento_generico_rigaBulk riga_fattura, Inventario_beniBulk bene) throws ComponentException{
		return bene;
}
public RemoteIterator cercaBeniAssociabili(UserContext userContext,Ass_inv_bene_fatturaBulk associa_Bulk, Documento_generico_rigaBulk riga,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
	  if (!associa_Bulk.isPerAumentoValoreDoc()){
    	SQLBuilder sql = getHome(userContext,Buono_carico_scarico_dettBulk.class).createSQLBuilder();
		if(clauses != null)
		    sql.addClause(clauses);
		sql.addTableToHeader("INVENTARIO_BENI_APG,INVENTARIO_BENI");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");	
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO"); 	 
		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
		//R.P. Consente di associare piu' volte lo stesso bene
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga.getDocumento_generico().getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
		//sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
		//PER NON VEDERE I BUONI TEMPORANEI GIA' GENERATI
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",SQLBuilder.GREATER,0);
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",SQLBuilder.EQUALS,riga.getEsercizio());
		if(riga.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE)
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.CARICO);
		else{
			sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, "Y"); // scaricati totalmente
			sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",SQLBuilder.EQUALS,Buono_carico_scaricoBulk.SCARICO);
		// condizione spostata nella vista associazioni disponibili per il fatturabile
			SQLBuilder sql_tipo = getHome(userContext, Buono_carico_scaricoBulk.class).createSQLBuilder();
			sql_tipo.addTableToHeader("TIPO_CARICO_SCARICO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO","TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
			sql_tipo.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
			sql_tipo.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_VENDITA",sql.EQUALS,"Y");
			sql.addSQLExistsClause("AND",sql_tipo);
		}

		SQLBuilder sql_old_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class).createSQLBuilder();
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","ASS_INV_BENE_FATTURA.PROGRESSIVO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","ASS_INV_BENE_FATTURA.ESERCIZIO");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
		sql_old_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_DOC_GEN",sql.EQUALS,riga.getEsercizio());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_DOC_GEN",sql.EQUALS,riga.getCd_cds());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_DOC_GEN",sql.EQUALS,riga.getCd_unita_organizzativa());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,riga.getCd_tipo_documento_amm());
		sql_old_ass.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_DOCUMENTO_GENERICO",sql.NOT_EQUALS,riga.getPg_documento_generico());
		sql.addSQLNotExistsClause("AND",sql_old_ass);

		SQLBuilder sql_ass = getHome(userContext, Ass_inv_bene_fatturaBulk.class,"V_ASSOCIAZIONI_DISPONIBILI").createSQLBuilder();
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.PG_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","V_ASSOCIAZIONI_DISPONIBILI.NR_INVENTARIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","V_ASSOCIAZIONI_DISPONIBILI.PROGRESSIVO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","V_ASSOCIAZIONI_DISPONIBILI.ESERCIZIO");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","V_ASSOCIAZIONI_DISPONIBILI.PG_BUONO_C_S");
		sql_ass.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","V_ASSOCIAZIONI_DISPONIBILI.TI_DOCUMENTO");
		sql.addSQLExistsClause("AND",sql_ass);
		
		if((riga.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
			riga.getObbligazione_scadenziario()!=null &&
			riga.getObbligazione_scadenziario().getObbligazione()!=null &&
			riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
			sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
			sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",sql.EQUALS,"INVENTARIO_BENI.CD_CATEGORIA_GRUPPO");
			sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.ESERCIZIO",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getEsercizio());
			sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_appartenenza());
			sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_GESTIONE",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_gestione());
			sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getCd_elemento_voce());		
		}
	
		sql.setDistinctClause(true);
		return iterator(userContext,sql,Buono_carico_scarico_dettBulk.class,null);
	   }
     else{
    	SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
 		if(clauses != null)
 		    sql.addClause(clauses);
 		sql.addTableToHeader("INVENTARIO_BENI_APG");
 		sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
 		sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
 		sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già associati con righe
 		
 		sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, associa_Bulk.getLocal_transactionID());	// della stessa Fattura.
 		//R.P. Consente di associare piu' volte lo stesso bene
 		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, associa_Bulk.getInventario().getPg_inventario());
 		sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, riga.getDocumento_generico().getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
 		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, "N"); // Non scaricati totalmente
 		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
 		sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,associa_Bulk.getTest_buono().getData_registrazione());
 		if((riga.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
 				riga.getObbligazione_scadenziario()!=null &&
 				riga.getObbligazione_scadenziario().getObbligazione()!=null &&
 				riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
 				sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
 				sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",sql.EQUALS,"INVENTARIO_BENI.CD_CATEGORIA_GRUPPO");
 				sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.ESERCIZIO",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getEsercizio());
 				sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_appartenenza());
 				sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_GESTIONE",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_gestione());
 				sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS,riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getCd_elemento_voce());		
 			}
 		sql.setDistinctClause(true);
 		return iterator(userContext,sql,Inventario_beniBulk.class,null);
     }
    }
	 public RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Documento_generico_rigaBulk doc,CompoundFindClause clauses) throws ComponentException {
			SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
			sql.addClause(clauses);
			sql.addTableToHeader("INVENTARIO_BENI_APG");//,ASS_INV_BENE_FATTURA");
			sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO(+)"); // Questa OUT Join
			sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO(+)"); //  permette di escludere	
			sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO(+)"); 	 //  quei beni che sono stati già selezioanti
			sql.addSQLClause("AND", "INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID(+)", SQLBuilder.EQUALS, buonoS.getLocal_transactionID());  // nella transazione attuale
			sql.addSQLClause("AND", "INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.ISNULL,null);
			sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());	
			sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", SQLBuilder.EQUALS, "N"); // Non scaricati totalmente
			sql.addSQLClause("AND", "INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE", SQLBuilder.LESS_EQUALS, buonoS.getData_registrazione()); // Con data di validità inferiore all'attuale data di Registrazione
			sql.addSQLClause("AND", "INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE", SQLBuilder.EQUALS, doc.getDocumento_generico().getTi_istituz_commerc()); // Beni dello stesso tipo della riga di Fattura
			// Aggiunta clausola che visualizzi solo i beni che abbiano 
			//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
			sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			if((doc.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
					doc.getObbligazione_scadenziario()!=null &&
					doc.getObbligazione_scadenziario().getObbligazione()!=null &&
					doc.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
					sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
					sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",sql.EQUALS,"INVENTARIO_BENI.CD_CATEGORIA_GRUPPO");
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.ESERCIZIO",sql.EQUALS,doc.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getEsercizio());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA",sql.EQUALS,doc.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_appartenenza());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.TI_GESTIONE",sql.EQUALS,doc.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getTi_gestione());
					sql.addSQLClause("AND","CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS,doc.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getCd_elemento_voce());		
				}
			
			return iterator(userContext,sql,Inventario_beniBulk.class,null);
		}
	 public boolean isContabilizzato(UserContext userContext,Buono_carico_scaricoBulk buono)throws ComponentException
	 {
		 try{
		Buono_carico_scaricoHome home=(Buono_carico_scaricoHome)getHome(userContext,Buono_carico_scaricoBulk.class);
		return home.IsContabilizzato(buono);
		 }catch (SQLException ex) {
				throw handleException(ex);	
		}
	 }	
	 public Categoria_gruppo_voceBulk findCategoria_gruppo_voceforvoce(UserContext userContext,Elemento_voceBulk elem) throws ComponentException{
		 try{
		Categoria_gruppo_voceHome home=(Categoria_gruppo_voceHome)getHome(userContext,Categoria_gruppo_voceBulk.class);
		return home.findCategoria_gruppo_voceforvoce(elem);
		 }catch (PersistencyException ex) {
				throw handleException(ex);	
		}
	 }
	 public void scaricaTuttiBeniDef(UserContext userContext,Buono_carico_scaricoBulk buonoS ) throws ComponentException {
		try {
				Boolean accesorio_presente;
			    Inventario_beni_apgHome homebeni =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class); 
				SQLBuilder sql = homebeni.createSQLBuilder();
				sql.addTableToHeader("INVENTARIO_BENI");
				sql.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",SQLBuilder.EQUALS, buonoS.getInventario().getPg_inventario());
				sql.addSQLClause("AND","INVENTARIO_BENI.CD_CDS",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
				sql.addSQLClause("AND","INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
				sql.addSQLClause("AND","INVENTARIO_BENI.DT_VALIDITA_VARIAZIONE",SQLBuilder.LESS_EQUALS,buonoS.getData_registrazione());
				sql.addSQLJoin("INVENTARIO_BENI.PG_INVENTARIO","INVENTARIO_BENI_APG.PG_INVENTARIO");
				sql.addSQLJoin("INVENTARIO_BENI.NR_INVENTARIO","INVENTARIO_BENI_APG.NR_INVENTARIO");
				sql.addSQLJoin("INVENTARIO_BENI.PROGRESSIVO","INVENTARIO_BENI_APG.PROGRESSIVO");
				sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
				// Locka le righe 
				sql.setForUpdate(true);
				List beni = homebeni.fetchAll(sql);
				Inventario_beniBulk bene;
				for(Iterator i=beni.iterator();i.hasNext();){
					accesorio_presente=new Boolean(false);
					Inventario_beni_apgBulk new_bene_apg = (Inventario_beni_apgBulk)i.next();
					bene =(Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(new Inventario_beniBulk(new_bene_apg.getNr_inventario(),new_bene_apg.getPg_inventario(),new_bene_apg.getProgressivo()));
					
					if(bene.getProgressivo()==0 ){ 
						deleteBeneAccessoriAlreadyExistsFor(userContext, buonoS, bene);							
						scaricaBeniAccessoriFor(userContext, buonoS, bene, null);
					}else{
						SQLBuilder notExistsQuery=homebeni.createSQLBuilder();
						notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO",SQLBuilder.EQUALS,bene.getNr_inventario());
						notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO",SQLBuilder.EQUALS,bene.getProgressivo());
						notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO",SQLBuilder.EQUALS,bene.getPg_inventario());
						notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.FL_TOTALMENTE_SCARICATO",SQLBuilder.EQUALS,bene.ISTOTALMENTESCARICATO );
						notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",SQLBuilder.EQUALS,buonoS.getLocal_transactionID());
						List acc = homebeni.fetchAll(notExistsQuery);
						if (acc != null && acc.size()>0){
							accesorio_presente=new Boolean(true);
						}
					}
					if(!accesorio_presente){
						new_bene_apg.setDt_validita_variazione(bene.getDt_validita_variazione());		
						if(buonoS.getTipoMovimento()!=null && buonoS.getTipoMovimento().getFl_vendita().booleanValue()){
							new_bene_apg.setVariazione_meno(bene.getValoreBene());
							new_bene_apg.setValore_alienazione(bene.getValoreBene(true));
							new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						}
						else{	
							new_bene_apg.setVariazione_meno(bene.getValoreBene());
							new_bene_apg.setFl_totalmente_scaricato(new Boolean(true));
						}
						new_bene_apg.setTi_documento(buonoS.getTi_documento());
						new_bene_apg.setPg_buono_c_s(buonoS.getPg_buono_c_s());
						new_bene_apg.setToBeUpdated();	
						super.modificaConBulk(userContext,new_bene_apg);
					}
				}
		} catch (PersistencyException e) {
			throw  handleException(e);
		} 
	}
	public Ass_inv_bene_fatturaBulk sdoppiaAssociazioneFor (UserContext userContext,Fattura_passiva_rigaBulk riga_fattura,Fattura_passiva_rigaBulk riga_fattura_new) 
	throws ComponentException
	{
		Ass_inv_bene_fatturaBulk ass=new Ass_inv_bene_fatturaBulk();
		try{
			if(riga_fattura instanceof Fattura_passiva_rigaIBulk){
				Ass_inv_bene_fatturaHome home = (Ass_inv_bene_fatturaHome)getHome(userContext,Ass_inv_bene_fatturaBulk.class);
				SQLBuilder sql= home.createSQLBuilder();
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,riga_fattura.getProgressivo_riga());
				if (home.fetchAll(sql).size() ==1){ 
					ass=(Ass_inv_bene_fatturaBulk)home.fetchAll(sql).get(0); 
					Ass_inv_bene_fatturaBulk new_ass=new Ass_inv_bene_fatturaBulk();
					new_ass.setPg_riga(findMaxAssociazione(userContext,new_ass));
					new_ass.setRiga_fatt_pass(ass.getRiga_fatt_pass());
					new_ass.setTest_buono(ass.getTest_buono());
					new_ass.setNr_inventario(ass.getNr_inventario());
					new_ass.setProgressivo(ass.getProgressivo());
					new_ass.setProgressivo_riga_fatt_pass(riga_fattura_new.getProgressivo_riga());
					new_ass.setUser(userContext.getUser());
					new_ass.setToBeCreated();
					return new_ass;
				}
			}else if(riga_fattura instanceof Nota_di_credito_rigaBulk){
				Ass_inv_bene_fatturaHome home = (Ass_inv_bene_fatturaHome)getHome(userContext,Ass_inv_bene_fatturaBulk.class);
				SQLBuilder sql= home.createSQLBuilder();
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.ESERCIZIO_FATT_PASS",sql.EQUALS,riga_fattura.getEsercizio());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_CDS_FATT_PASS",sql.EQUALS,riga_fattura.getCd_cds());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.CD_UO_FATT_PASS",sql.EQUALS,riga_fattura.getCd_unita_organizzativa());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PG_FATTURA_PASSIVA",sql.EQUALS,riga_fattura.getPg_fattura_passiva());
				sql.addSQLClause("AND","ASS_INV_BENE_FATTURA.PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,riga_fattura.getProgressivo_riga());
				if (home.fetchAll(sql).size() ==1){ 
					ass=(Ass_inv_bene_fatturaBulk)home.fetchAll(sql).get(0);
					Ass_inv_bene_fatturaBulk new_ass=new Ass_inv_bene_fatturaBulk();
					new_ass.setPg_riga(findMaxAssociazione(userContext,new_ass));
					new_ass.setRiga_fatt_pass(ass.getRiga_fatt_pass());
					new_ass.setTest_buono(ass.getTest_buono());
					new_ass.setNr_inventario(ass.getNr_inventario());
					new_ass.setProgressivo(ass.getProgressivo());
					new_ass.setProgressivo_riga_fatt_pass(riga_fattura_new.getProgressivo_riga());
					new_ass.setUser(userContext.getUser());
					new_ass.setToBeCreated();
					return new_ass;
				}
			}
			}catch(it.cnr.jada.persistency.PersistencyException ex){
				throw handleException( ex);
			}
		return null;			
		}	
	public SQLBuilder selectNuova_categoriaByClause(UserContext userContext, Inventario_beniBulk modello,Categoria_gruppo_inventBulk cat, CompoundFindClause clauses) 
			throws ComponentException
	{			
			SQLBuilder sql = getHome(userContext, Categoria_gruppo_inventBulk.class).createSQLBuilder();
			sql.addClause( clauses );
			sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",sql.EQUALS, "Y");
			sql.addSQLClause("AND","DATA_CANCELLAZIONE",sql.ISNULL, null);
			sql.addSQLClause("AND","LIVELLO",sql.GREATER, "0");
			return sql;		
	}
	public boolean checkEtichettaBeneAlreadyExist(UserContext userContext, Buono_carico_scarico_dettBulk dett)  throws ComponentException, RemoteException{
		try{
			Inventario_beniHome invBeniHome = (Inventario_beniHome)getHome(userContext, Inventario_beniBulk.class);
			return invBeniHome.IsEtichettaBeneAlreadyExist(dett);
		}catch (SQLException ex) {
			throw handleException(ex);
		}
	}
}


