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

package it.cnr.contab.inventario00.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.doccont00.comp.DateServices;
/**
 * Insert the type's description here.
 * Creation date: (07/05/2002 17.44.39)
 * @author: Gennaro Borriello
 */
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioBulk;
import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioHome;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniHome;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laHome;
import it.cnr.contab.inventario00.docs.bulk.Stampa_beni_senza_utilizVBulk;
import it.cnr.contab.inventario00.docs.bulk.Stampa_registro_inventarioVBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.docs.bulk.V_ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioHome;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoHome;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoHome;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
 
public class Inventario_beniComponent 
		extends it.cnr.jada.comp.CRUDComponent 
			implements IInventario_beniMgr, ICRUDMgr, Serializable, Cloneable, it.cnr.jada.comp.IPrintMgr {
/**
 * Inventario_beniComponent constructor comment.
 */
public Inventario_beniComponent() {
	super();
}
/** 
  *   Aggiorna Beni Accessori di un Bene Padre
  *    PreCondition:
  *      In seguito ad una richiesta di modifica di un Bene, (metodo modificaConBulk), 
  *		il sistema provvede ad aggiornare i valori relativi ad Ubicazione ed Asseganatrio di
  *		tutti gli eventuali beni accessori del bene modificato.
  *    PostCondition:
  *      Per ogni bene accessorio aggiorno Ubicazione ed Assegntario.
  *  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene principale
  *
**/
private void aggiornaBeniAccessoriFor(UserContext userContext,Inventario_beniBulk bene) throws ComponentException {
	
	for (Iterator i = bene.getAccessori().iterator(); i.hasNext();){
		Inventario_beniBulk bene_accessorio = (Inventario_beniBulk)i.next();
		bene_accessorio.setUbicazione(bene.getUbicazione());
		bene_accessorio.setAssegnatario(bene.getAssegnatario());
		try{
			updateBulk(userContext, bene_accessorio);
		} catch (it.cnr.jada.persistency.PersistencyException e){
			throw new ComponentException(e);
		} 
		
	}
}
//^^@@
/** 
  *  L'inventario non esiste
  *    PreCondition:
  *      Non è stato trovato un Inventario associato alla UO di scrivania.
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che la UO non ha un
  *		Inventario
  *
  *  L'inventario non è aperto.
  *    PreCondition:
  *      L'Inventario non è in stato "A", ossia aperto.
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che l'Inventario 
  *		associato alla UO non è in stato "Aperto"
  *
  *  Carica l'Inventario associato alla UO di scrivania
  *    PreCondition:
  *      E' stata generata la richiesta di caricare l'Inventario associato alla UO di scrivania 
  *		e tutti i controlli sono stati superati
  *    PostCondition:
  *      vengono trasmessi i dati relativi all'Inventario associato alla UO di scrivania.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  *
  * @return Id_inventarioBulk <code>SQLBuilder</code> l'Inventario associato alla UO di scrivania
**/
public Id_inventarioBulk caricaInventario(UserContext aUC)
		throws ComponentException,
		it.cnr.jada.persistency.PersistencyException,
		it.cnr.jada.persistency.IntrospectionException 
{
	
	Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
	Id_inventarioBulk inventario = inventarioHome.findInventarioFor(aUC,false);

	// NON ESISTE UN INVENTARIO ASSOCIATO ALLA UO DI SCRIVANIA
	if (inventario == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO");
		

	return inventario;
}
/** 
  *   Carico i CdR Utilizzatori, (e le relative Linee di Attività), di un dato Bene
  *    PreCondition:
  *      In seguito ad una richiesta di modifica di un Bene, (metodo modificaConBulk),
  *		il sistema cerca gli eventuali CdR Utilizzatori del Bene
  *    PostCondition:
  *      Se ci sono CdR Utilizzatori, questi vengono caricati e, per ognuno di essi, vengono
  *		caricate le relative Linee di Attività di utilizzo
  *  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene <code>Inventario_beniBulk</code> il bene di riferimento
  *
**/
private void caricaUtilizzatoriFor(UserContext userContext,Inventario_beniBulk bene) throws ComponentException,
	it.cnr.jada.persistency.PersistencyException 
{
	BigDecimal perc_cdr=new BigDecimal(0); 
	CdrHome home=(CdrHome)getHome(userContext,CdrBulk.class);
	SQLBuilder sql=home.createSQLBuilder();
	sql.addTableToHeader("INVENTARIO_UTILIZZATORI_LA");
	sql.setDistinctClause(true);
	sql.addSQLJoin("INVENTARIO_UTILIZZATORI_LA.CD_UTILIZZATORE_CDR",sql.EQUALS,"CDR.CD_CENTRO_RESPONSABILITA");
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	List utilizzatori = home.fetchAll(sql);
    for(Iterator i = utilizzatori.iterator();i.hasNext();){
    	CdrBulk cdr_utilizzatore=(CdrBulk)i.next();
    	Inventario_utilizzatori_laHome home_perc=(Inventario_utilizzatori_laHome)getHome(userContext,Inventario_utilizzatori_laBulk.class);
    	SQLBuilder sql_perc=home_perc.createSQLBuilder();
    	sql_perc.setDistinctClause(true);
    	sql_perc.addSQLClause("AND","CD_UTILIZZATORE_CDR",sql.EQUALS,cdr_utilizzatore.getCd_centro_responsabilita());
    	sql_perc.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());
    	sql_perc.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
    	sql_perc.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
    	it.cnr.jada.persistency.Broker broker = home_perc.createBroker(sql_perc);
    	if (broker.next()){	
    		perc_cdr =(((Inventario_utilizzatori_laBulk) broker.fetch(Inventario_utilizzatori_laBulk.class)).getPercentuale_utilizzo_cdr());
    		broker.close();	
    	}	    
     	Utilizzatore_CdrVBulk v_utilizzatore = new Utilizzatore_CdrVBulk();	
			v_utilizzatore.setCdr(cdr_utilizzatore);
			v_utilizzatore.setPercentuale_utilizzo_cdr(perc_cdr);
		SQLBuilder sql_la=home_perc.createSQLBuilder();
		sql_la.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
		sql_la.addSQLClause("AND", "NR_INVENTARIO", sql.EQUALS, bene.getNr_inventario());
		sql_la.addSQLClause("AND", "PROGRESSIVO", sql.EQUALS, bene.getProgressivo());
		sql_la.addSQLClause("AND", "CD_UTILIZZATORE_CDR", sql.EQUALS, cdr_utilizzatore.getCd_centro_responsabilita());
		List utilizzatori_la=home_perc.fetchAll(sql_la);
		for(Iterator iteratore=utilizzatori_la.iterator();iteratore.hasNext();){
			Inventario_utilizzatori_laBulk utilizzatore_la=(Inventario_utilizzatori_laBulk)iteratore.next();
				v_utilizzatore.addToUtilizzatoriColl(utilizzatore_la);
	        }
	        bene.addToV_utilizzatoriColl(v_utilizzatore);
		}
		getHomeCache(userContext).fetchAll(userContext);
}
/** 
  *  Cancella i CdR Utilizzatori, (e le relative Linee di Attività), di un dato Bene
  *    PreCondition:
  *      In seguito ad una richiesta di modifica di un Bene, (metodo modificaConBulk),
  *		il sistema elimina dalla tabella INVENTARIO_UTILIZZATORI tutti i record relativi al bene
  *		selezionato, per poi riscrivere i nuovi collegamenti
  *    PostCondition:
  *      elimina dalla tabella INVENTARIO_UTILIZZATORI tutti i record relativi al bene
  *		selezionato.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene <code>Inventario_beniBulk</code> il bene di riferimento
  *
**/
private void deleteUtilizzatori(UserContext aUC,Inventario_beniBulk bene) 
	throws ComponentException 
{
	if (bene != null){	
		Inventario_utilizzatori_laHome home = (Inventario_utilizzatori_laHome)getHome(aUC,Inventario_utilizzatori_laBulk.class);
		Inventario_beniHome bene_home = (Inventario_beniHome)getHome(aUC,Inventario_beniBulk.class);
		try {
			bene.setV_utilizzatoriColl(new SimpleBulkList(bene_home.findUtilizzatori(aUC,bene)));
				for(java.util.Iterator i = bene.getV_utilizzatoriColl().iterator(); i.hasNext();) {
					Inventario_utilizzatori_laBulk utilizzatore_la = (Inventario_utilizzatori_laBulk)i.next();	
					utilizzatore_la.setToBeDeleted();
					super.eliminaConBulk(aUC,utilizzatore_la);
				}		
			bene.setV_utilizzatoriColl(new SimpleBulkList(bene_home.findUtilizzatori(aUC,bene)));
		} catch (IntrospectionException e) {
			throw new ComponentException (e);
		} catch (PersistencyException e) {
			throw new ComponentException (e);
		}
	}
}
/**
  * Data una collezione di Utilizzatore_CdrVBulk, (ossia gli utilizztori specificati 
  *	in un bene), ne estrae tutti gli utilizzatori,
  *	(Inventario_utilizzatori_laBulk), e li inserisce in una collezione,
  *	(SimpleBulkList), che verrà restituita.
**/
private SimpleBulkList estraiUtilizzatoriFor (Inventario_beniBulk bene) 
	throws ComponentException
{
	SimpleBulkList utilizzatori = new SimpleBulkList();
	for(Iterator i = bene.getV_utilizzatoriColl().iterator(); i.hasNext();){		
		Utilizzatore_CdrVBulk v_utilizzatore = (Utilizzatore_CdrVBulk)i.next();
		for (Iterator i_utilizzatore = v_utilizzatore.getBuono_cs_utilizzatoriColl().iterator(); i_utilizzatore.hasNext();){
			Inventario_utilizzatori_laBulk utilizzatore = (Inventario_utilizzatori_laBulk)i_utilizzatore.next();
			utilizzatore.setPercentuale_utilizzo_cdr(utilizzatore.getVUtilizzatore().getPercentuale_utilizzo_cdr());
			utilizzatore.setBene(bene);
			utilizzatore.setCrudStatus(1);
			utilizzatore.setUser(bene.getUser());
			utilizzatore.setToBeCreated();
			utilizzatori.add(utilizzatore);
		}
	}
	return utilizzatori;
}
/**
  * Dato un bene accessorio, cerca nella tabella INVENTARIO_BENI, il bene padre.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene <code>Inventario_beniBulk</code> il bene accessorio di riferimento
  *
  * @return bene <code>Inventario_beniBulk</code> il bene padre
**/
private Inventario_beniBulk findBenePrincipale(UserContext aUC,Inventario_beniBulk bene) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	if (bene == null) 
		return null;
	
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Inventario_beniBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, bene.getInventario().getPg_inventario());
	sql.addSQLClause("AND", "NR_INVENTARIO", sql.EQUALS, bene.getNr_inventario());
	sql.addSQLClause("AND", "PROGRESSIVO", sql.EQUALS, "0");		

	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;
 
	Inventario_beniBulk bene_principale=(Inventario_beniBulk) broker.fetch(Inventario_beniBulk.class);

	broker.close();
	return bene_principale;
	
}
/** 
  *   Cerca i Tipi Ammortamento legati alla Categoria indicata
  *    PreCondition:
  *      E' stato richiesto di cercare i Tipi Ammortamento associati ad una Categoria Gruppo Inventario
  *    PostCondition:
  *      Viene restituita la lista dei Tipi Ammortamento legati alla Categoria Gruppo
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param categoria_gruppo <code>Categoria_gruppo_inventBulk</code> la Categoria Gruppo Inventario di riferimento
  *
  * @return <code>Collection</code> i Tipi Ammortamento eventualmente trovati
**/

public java.util.Collection findTipiAmmortamento(UserContext aUC,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk categoria_gruppo) 
	throws ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.persistency.IntrospectionException {
	
	Tipo_ammortamentoHome ti_ammHome = (Tipo_ammortamentoHome)getHome(aUC, Tipo_ammortamentoBulk.class);
	return ti_ammHome.findTipiAmmortamentoFor(aUC,categoria_gruppo);
}
/** 
  *   Cerca il Tipo Ammortamento
  *    PreCondition:
  *      In seguito ad una richiesta di inizializzazione per modifica di un Bene, 
  *		il sistema cerca il Tipo Ammortamento associato al Bene indicato.
  *    PostCondition:
  *      Vengono trasmessi i dati relativi al Tipo Ammortamento associato al bene
  *  
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene <code>Inventario_beniBulk</code> il bene di riferimento
  *
  * @return tipo_ammortamento<code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento trovato
**/
private Tipo_ammortamentoBulk findTipoAmmortamento(UserContext aUC,Inventario_beniBulk bene) 
	throws ComponentException 
{
try {
	Tipo_ammortamentoBulk tipo_ammortamento=null;
	if (bene == null) 
		return null;
	 Tipo_ammortamentoHome home=(Tipo_ammortamentoHome)getHome(aUC,Tipo_ammortamentoBulk.class);
	 SQLBuilder sql=home.createSQLBuilder();
	 sql.addTableToHeader("V_AMMORTAMENTO_BENI");
	 sql.addSQLJoin("TIPO_AMMORTAMENTO.TI_AMMORTAMENTO",sql.EQUALS,"V_AMMORTAMENTO_BENI.TI_AMMORTAMENTO");
	 sql.addSQLJoin("TIPO_AMMORTAMENTO.CD_TIPO_AMMORTAMENTO",sql.EQUALS,"V_AMMORTAMENTO_BENI.CD_TIPO_AMMORTAMENTO");
	 sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());
	 sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	 sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	 sql.addSQLClause("AND","ESERCIZIO_COMPETENZA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	 List tipi= home.fetchAll(sql);
	
	 for (Iterator i=tipi.iterator();i.hasNext();){
	 	tipo_ammortamento=(Tipo_ammortamentoBulk)i.next();
	 }
	return tipo_ammortamento;
	} catch (PersistencyException e) {
		throw new ComponentException (e);
	}
}
/** 
  *   Cerca il numero Buono di carico
  *    PreCondition:
  *      In seguito ad una richiesta di inizializzazione per modifica di un Bene, 
  *		il sistema cerca il Numero del Buono di Carico associato al Bene indicato.
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene <code>Inventario_beniBulk</code> il bene di riferimento
  *
  * @return Integer<code>Integer</code> 
**/
private Integer findBuonoCarico(UserContext aUC,Inventario_beniBulk bene) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{	
	Integer Nr_Buono=0;
	if (bene == null) 
		return null;
	V_cons_registro_inventarioHome home =(V_cons_registro_inventarioHome)getHome(aUC,V_cons_registro_inventarioBulk.class);
	SQLBuilder sql=home.createSQLBuilder();
	sql.addTableToHeader("TIPO_CARICO_SCARICO");
	sql.addSQLJoin("TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO",SQLBuilder.EQUALS,"V_CONS_REGISTRO_INVENTARIO.CD_TIPO_CARICO_SCARICO");
	sql.addSQLClause("AND","TIPO_CARICO_SCARICO.TI_DOCUMENTO",sql.EQUALS,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk.CARICO);
	sql.addSQLClause("AND","TIPO_CARICO_SCARICO.FL_AUMENTO_VALORE",sql.EQUALS,"N");
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,bene.getProgressivo());
	
	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
    while(broker.next())
    	Nr_Buono=new Integer(((V_cons_registro_inventarioBulk) broker.fetch(V_cons_registro_inventarioBulk.class)).getPg_buono_c_s().intValue());
    broker.close();
	  return Nr_Buono; 
}
/**
  * Dato un bene, cerca nella tabella INVENTARIO_BENI, i beni accessori.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene_principale <code>Inventario_beniBulk</code> il bene di riferimento
  *
  * @return beni_accessori <code>List</code> gli eventuali beni accessori trovati
**/
private java.util.List getBeniAccessoriFor(UserContext userContext, Inventario_beniBulk bene_principale) throws ComponentException {

	try{
		Inventario_beniHome home = (Inventario_beniHome)getHome(userContext,Inventario_beniBulk.class);
		return home.getBeniAccessoriFor(bene_principale);

	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bene_principale, ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bene_principale, ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

	try
	{
		return getHome(userContext, Inventario_beniBulk.class).getServerDate();
	}
	catch(it.cnr.jada.persistency.PersistencyException ex)
	{
		throw handleException(ex);
	}
}
//^^@@
/** 
  *  Richiede ID univoco di Transazione
  *    PreCondition:
  *      E' stato richiesto di recuperare/generare l'identificativo di transazione.
  *    PostCondition:
  *      Viene richiesto l'ID e, se questo non esiste, verrà generato, se richiesto
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID
  *
  * @return local_transaction_id <code>String</code> l'ID di transazione richiesto
**/
public String getLocalTransactionID(UserContext aUC, boolean force) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	String localTransactionID = null;
	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection( aUC ),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"IBMUTL001.getLocalTransactionID(" + 
			(force?"TRUE":"FALSE") + ")}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.executeQuery();
		localTransactionID = cs.getString(1);
	} catch (Throwable e) {
		throw handleException(e);
	} finally{
		try{
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e){
			throw handleException(e);
		}
	}
	return localTransactionID;
	
}
/** 
  *  Cerca l'Inventario associato alla Uo di scrivania
  *    PreCondition:
  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_beniBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_beniBulk
  *    PostCondition:
  *      Vengono caricate le Condizioni_bene valide.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il bene inizializzato
**/
public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC, OggettoBulk bulk) 
		throws ComponentException
{

	super.inizializzaBulkPerInserimento(aUC,bulk);	
		
	Inventario_beniBulk bene = (Inventario_beniBulk) bulk;

	try{
		bene.setInventario(caricaInventario(aUC));		
	} catch(it.cnr.jada.persistency.PersistencyException pe){
		throw new it.cnr.jada.comp.ComponentException(pe);
	} catch (it.cnr.jada.persistency.IntrospectionException ie){
		throw new it.cnr.jada.comp.ComponentException(ie);
	}		
	
	// INIZIALIZZA LE CONDIZIONI DEL BENE
	try{
		bene.setCondizioni(getHome(aUC,Condizione_beneBulk.class).findAll());
	} catch ( PersistencyException pe){
		throw new it.cnr.jada.comp.ComponentException(pe);
	}		
				
	if (isEsercizioCOEPChiuso(aUC))
    	return asRO(bene, "Funzione disponibile solo in lettura. L'esercizio COEP per il CdS " + it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC) + " risulta in stato 'C' o 'P'.");
		
	return bene;
}

/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una ComponentException con la descrizione dell'errore.
  *   
  *  Inizializzazione di una istanza di Inventario_beniBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_beniBulk per modifica
  *    PostCondition:
  *      Vengono caricati gli eventuali Utilizzatori del Bene, gli accessori o il bene padre,
  *		a seconda se il Bene di riferimento è rispettivamente un bene accessorio oppure no;
  *		carica, inoltre, i Tipi Ammortamento legati alla Categoria Gruppo Inventario a cui appartiene il Bene
  *		ed il Tipo Ammortamento associato al Bene stesso.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere inizializzato
  *
  * @return <code>OggettoBulk</code> il bene inizializzato
**/
public OggettoBulk inizializzaBulkPerModifica(
    UserContext aUC,
    OggettoBulk bulk)
    throws ComponentException {
    if (bulk == null)
        throw new ComponentException("Attenzione: non esiste alcun Bene corrispondente ai criteri di ricerca!");

    bulk = super.inizializzaBulkPerModifica(aUC, bulk);
    Inventario_beniBulk bene = (Inventario_beniBulk) bulk;

    bene.setValore_unitario(bene.getValore_iniziale());
    try {
        caricaUtilizzatoriFor(aUC, bene);

        // Carica il Bene Principale se il bene selezionato è un Bene Accessorio
        if (bene.isBeneAccessorio()) {
            bene.setBene_principale(findBenePrincipale(aUC, bene));
        } else {
            // Carica i Beni Accessori del Bene
            java.util.List accessori = getBeniAccessoriFor(aUC, bene);
            bene.setAccessori(new SimpleBulkList(accessori));
        }

        // Carica i Tipi Ammortamento legati alla Categoria Gruppo
        bene.setTi_ammortamenti(findTipiAmmortamento(aUC, bene.getCategoria_Bene()));

        // Carica il Tipo Ammortamento del Bene
        bene.setTipo_ammortamento(findTipoAmmortamento(aUC, bene));
		
		// Carica il numero del Buono di Carico iniziale per l'esercizio
		Integer appo = findBuonoCarico(aUC,bene);
		if (appo.intValue()!=0){
		  	bene.setnum_buono(appo);
		  	Buono_carico_scarico_dettHome home=(Buono_carico_scarico_dettHome)getHome(aUC,Buono_carico_scarico_dettBulk.class);
		  	bene.setBuoni_dettColl(new BulkList(home.findBuoniFor(bene)));
		}
        /* Setta il flag Ammortamento, in base al flag Soggetto 
        *	ad Ammortamento della Categoria Gruppo*/
		//r.p. 23/11/2007 commentato perchè alterava l'eventuale scelta del non ammortamento 
        //bene.setFl_ammortamento(bene.getCategoria_Bene().getFl_ammortamento());
		bene.setDa_fattura(da_fattura(aUC,bene));
		bene.setHa_dettagli(ha_dettagli(aUC,bene));
		bene.setContab(isContab(aUC,bene));
    } catch (it.cnr.jada.persistency.PersistencyException e) {
        throw handleException(bene, e);
    } catch (it.cnr.jada.persistency.IntrospectionException e) {
        throw handleException(bene, e);
    }

    if (bene.isTotalmenteScaricato())
    	return asRO(bene, "ATTENZIONE: questo bene è stato totalmente scaricato.\nNon sarà possibile fare alcuna operazione su di esso.");

    if (isEsercizioCOEPChiuso(aUC))
    	return asRO(bene, null);
    	
    return bene;
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
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_beniBulk per ricerca.
  *    PostCondition:
  *      Trasmette il Bene con tutti gli oggetti collegati e preparato per una operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere inizializzato.
  *
  * @return <code>OggettoBulk</code> il Bene inizializzato
**/
public OggettoBulk inizializzaBulkPerRicerca (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	if (bulk == null)
		throw new ComponentException("Attenzione: non esiste alcun bene corrispondente ai criteri di ricerca!");

	bulk = super.inizializzaBulkPerRicerca(aUC,bulk);	
	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;

	// INIZIALIZZA UO E CDS LEGGENDOLI DA SCRIVANIA
	//bene.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC));
	//bene.setCd_unita_organizzativa(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC));
	
	try{
		bene.setInventario(caricaInventario(aUC));		
	}
	catch(it.cnr.jada.persistency.PersistencyException pe){
	}
	catch (it.cnr.jada.persistency.IntrospectionException ie){
	}
	return bene;
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_registro_inventarioVBulk stampa) throws ComponentException{
	try{
		String cd_cds_scrivania = CNRUserContext.getCd_cds(userContext);
		
	    /* 22/12/2004
	     * Se in scrivania ho il 999 non valorizzo il CDS
	     * in modo da stampare tutti i CDS
	     * e non solo il 999.000
	     * 
	     * 27/12/2004
	     * Ripristinata situazione precedente. Sulla mappa in
	     * questo modo verrà visualizzato il CDS di appartnnenza.
	     * Nel Bulk è stato effettuato il test se CDS = 999
	     * 
	     * */
		//if (!cd_cds_scrivania.equals("999"))	
		EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
		stampa.setCdsEnte(ente);
		stampa.setCd_cds(cd_cds_scrivania);
	
		stampa.setDataInizio(it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
		stampa.setDataFine(getDataOdierna(userContext));
	
		stampa.setCategoriaForPrint(new Categoria_gruppo_inventBulk());
		stampa.setGruppoForPrint(new Categoria_gruppo_inventBulk());
	
		stampa.setNrInventarioFrom(new Long(0));
		stampa.setNrInventarioTo(new Long("9999999999"));
	

		Tipo_carico_scaricoHome tipo_carico_scaricoHome = (Tipo_carico_scaricoHome)getHome(userContext, it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk.class);
		java.util.Collection tipo_carico_scarico = tipo_carico_scaricoHome.findAll();
		stampa.setTipoMovimento(new Tipo_carico_scaricoBulk());
		stampa.setTipoMovimenti(tipo_carico_scarico);
		
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
	

	Timestamp dataOdierna = getDataOdierna(userContext);
	    
    stampa.setDataInizio(new Timestamp(0));
	stampa.setDataFine(dataOdierna);	
	
	try{
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
		

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUOForPrintEnabled(true);
		}

			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_beni_senza_utilizVBulk stampa) throws ComponentException{

	String cd_cds_scrivania = CNRUserContext.getCd_cds(userContext);

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));	
	stampa.setCd_cds(cd_cds_scrivania);

	stampa.setDataInizio(it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));

	try {		
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
	
		if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
			stampa.setUoEnte(true);
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUOForPrintEnabled(true);
		} else{	
			Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
			stampa.setUoEnte(false);
			if (uoScrivania.isUoCds()){
				stampa.setUoForPrint(new Unita_organizzativaBulk());
				stampa.setUOForPrintEnabled(true);
			} else {		
				stampa.setUoForPrint(uoScrivania);
				stampa.setUOForPrintEnabled(false);
			}
		}
		
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_beni_senza_utilizVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_beni_senza_utilizVBulk)bulk);
	else if (bulk instanceof Stampa_registro_inventarioVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_registro_inventarioVBulk)bulk);
		
	return bulk;
}
/**
  *	Controllo se l'esercizio COEP di scrivania e' aperto
  *
  * Nome: Controllo chiusura esercizio COEP
  * Pre:  E' stata richiesta la modifica di un bene in inventario
  * Post: Viene chiamata una stored procedure che restituisce 
  *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C o P
  *		  -		'N' altrimenti
  *		  Se l'esercizio e' chiuso e' impossibile proseguire
  *
  * @param  userContext <code>UserContext</code>
  
  * @return boolean : TRUE se stato = C
  *					  FALSE altrimenti
  */
protected boolean isEsercizioCOEPChiuso(UserContext userContext) throws ComponentException {
	
	LoggableStatement cs = null;	
	String status = null;

	
	try
	{
		// Controlla lo Stato 'C'
		cs = new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());		

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)	);		
		cs.setObject( 3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext)		);		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

	    if(status.compareTo("Y")==0){
	    	return true;
	    }

	    // Resetta i parametri
	    cs.clearParameters();

	    // Controlla lo Stato 'P'
		cs = new LoggableStatement(getConnection(userContext), "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+	"CNRCTB200.isChiusuraCoepProva(?,?)}",false,this.getClass());		

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)	);		
		cs.setObject( 3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext)		);		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

	    if(status.compareTo("Y")==0){
	    	return true;
	    }
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
	}
	
    return false;		    	
}
public Boolean isContab(it.cnr.jada.UserContext userContext, Inventario_beniBulk bene) throws it.cnr.jada.comp.ComponentException {
			try {
				SQLBuilder sql_exists = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
				sql_exists.addTableToHeader("BUONO_CARICO_SCARICO_DETT");
				sql_exists.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO", "INVENTARIO_BENI.PG_INVENTARIO");
				sql_exists.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO", "INVENTARIO_BENI.NR_INVENTARIO");
				sql_exists.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO", "INVENTARIO_BENI.PROGRESSIVO");
				sql_exists.addSQLClause("AND","INVENTARIO_BENI.PG_INVENTARIO",sql_exists.EQUALS,bene.getPg_inventario());
				sql_exists.addSQLClause("AND","INVENTARIO_BENI.NR_INVENTARIO",sql_exists.EQUALS,bene.getNr_inventario());
				sql_exists.addSQLClause("AND","INVENTARIO_BENI.PROGRESSIVO",sql_exists.EQUALS,bene.getProgressivo());
				sql_exists.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.STATO_COGE",sql_exists.NOT_EQUALS,"N");
				sql_exists.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.STATO_COGE",sql_exists.NOT_EQUALS,"X");
				if (sql_exists.executeExistsQuery(getConnection(userContext)))
					return Boolean.TRUE;
				return Boolean.FALSE;
			} catch(java.sql.SQLException e) {
				throw handleException(e);
			}
		}
/** 
  *  Modifica non valida - Valida Bene.
  *    PreCondition:
  *      I controlli sulle modifiche apportate al bene non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *
  *  Modifica non valida - Valida Utilizzatori.
  *    PreCondition:
  *      I controlli sulle modifiche apportate agli utilizzatori del bene non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Bene
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Bene. 
  *		Le modifiche passano le validazioni.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/ 
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) 
	throws ComponentException
{
	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;	

	try{
		// valida il bene
		validaBene(aUC, bene);
		if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
				!bene.getCategoria_Bene().getFl_gestione_targa() && bene.getTarga()!=null)
			bene.setTarga(null);
		
		if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
			!bene.getCategoria_Bene().getFl_gestione_seriale() && bene.getSeriale()!=null)
			bene.setSeriale(null);
		
		
		validaUtilizzatori(aUC, bene);
		/* Cancella le relazioni esistenti tra il bene e gli utilizzatori ù
		 *	per poi ricrearle con quelle attuali
		*/
		bene.setUtilizzatori(estraiUtilizzatoriFor(bene));
		
		deleteUtilizzatori(aUC, bene);

				

		if (bene.getAccessori().size()>0)
			aggiornaBeniAccessoriFor(aUC, bene);
		
	} catch (Throwable t1){
		throw new ComponentException (t1);
	}
	
	return super.modificaConBulk(aUC,bene);
}
/**
  *  Cerca l'Inventario associato alla Uo di scrivania
  *    PreCondition:
  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Ricerca di un Bene
  *    PreCondition:
  *      E' stato richiesto di cercare un Bene
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Inventario_beniBulk):
  *		i beni devono altresì appartenere all'Inventario associato alla Uo di scrivania e
  *		non devono essere totalmente scaricati.
  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  * @param bulk <code>OggettoBulk</code> il Bene modello
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) 
	throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	// Effettuo la ricerca dei beni	
	Id_inventarioBulk inventario = null;
	
	try{
		inventario = caricaInventario(userContext);
	} catch (it.cnr.jada.persistency.IntrospectionException e){
		throw new it.cnr.jada.comp.ComponentException(e);
	}
	
	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses, bulk);	
	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;
	
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, inventario.getPg_inventario());
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

	
	return sql;
	
}
public RemoteIterator selectBuonoFor(
	UserContext userContext,
	Inventario_beniBulk bene) throws ComponentException 
{
	SQLBuilder sql = getHome(userContext, V_cons_registro_inventarioBulk.class,"BASECONS").createSQLBuilder();
	sql.setDistinctClause(true);
	sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO", sql.EQUALS,bene.getProgressivo());
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,V_cons_registro_inventarioBulk.class,null);
	return ri;
}

public RemoteIterator selectFatturaFor(
	UserContext userContext,
	Inventario_beniBulk bene) throws ComponentException 
{
	SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO", sql.EQUALS,bene.getProgressivo());
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,V_ass_inv_bene_fatturaBulk.class,null);
	return ri;
}
/** 
  *  Cerca beni accessori
  *    PreCondition:
  *      E' stata generata la richiesta di cercare i beni accessori di un bene specificato.
  *    PostCondition:
  *      Viene restituito un Iteratore sui beni presenti sulla tabella INVENTARIO_BENI
  *		che rispondono ai requisiti richiesti, ossia che siano accessori del bene indicato.
  *		Sono visualizzati anche quei beni che sono stati scaricati totalmente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene principale.
  *
  * @return ri <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
public RemoteIterator selectBeniAccessoriFor(
	UserContext userContext,
	Inventario_beniBulk bene) throws ComponentException 
{
	SQLBuilder sql = getHome(userContext, Inventario_beniBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO", sql.NOT_EQUALS, new Long(0));
	sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO", sql.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,Inventario_beniBulk.class,null);
	return ri;
}
/**
  *  Ricerca di una Categoria Gruppo Inventario per il Bene
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di una Categoria Gruppo Inventario.
  *    PostCondition:
  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che la Categoria abbia una data di cancellazione valida, (ossia NULL); 
  *		che sia di livello maggiore di 0, (cioè che sia un Gruppo); 
  *		che sia valida per la gestione dell'Inventario.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene di riferimento
  * @param cat_gruppo la <code>Categoria_gruppo_inventBulk</code> categoria di riferimento  
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
 * @throws PersistencyException 
**/
public SQLBuilder selectCategoria_BeneByClause(UserContext userContext, Inventario_beniBulk bene, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk cat_gruppo, CompoundFindClause clauses) 
		throws ComponentException, PersistencyException
{		
	Inventario_beniBulk ori =((Inventario_beniBulk)getHome(userContext,Inventario_beniBulk.class).findByPrimaryKey(bene));
	SQLBuilder sql = getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",sql.EQUALS, "Y");
	sql.addSQLClause("AND","DATA_CANCELLAZIONE",sql.ISNULL, null);
	sql.addSQLClause("AND","LIVELLO",sql.GREATER, "0");
	if ((bene.getDa_fattura()!=null && bene.getDa_fattura().booleanValue())||(bene.isContab()!=null && bene.isContab().booleanValue())
			||bene.isMigrato()||bene.isBeneAccessorio()
		||(bene.getEsercizio_carico_bene()!= null && !bene.getEsercizio_carico_bene().equals(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)))){
		sql.addSQLClause("AND","CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,ori.getCd_categoria_gruppo());
    }
	return sql;		
}
public Boolean da_fattura(UserContext userContext, Inventario_beniBulk bene) throws ComponentException
{	
		SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO", sql.EQUALS,bene.getProgressivo());
		try 
		{
		  if (sql.executeCountQuery(getConnection(userContext))>0)							
			return Boolean.TRUE;						 
		  else
			return Boolean.FALSE;	
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
	}
public Boolean ha_dettagli(UserContext userContext, Inventario_beniBulk bene) throws ComponentException
{	
		SQLBuilder sql = getHome(userContext, V_cons_registro_inventarioBulk.class).createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO", sql.EQUALS, bene.getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,bene.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO", sql.EQUALS,bene.getProgressivo());
		try 
		{
		  if (sql.executeCountQuery(getConnection(userContext))>0)							
			return Boolean.TRUE;						 
		  else
			return Boolean.FALSE;	
			} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
	}	
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
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
public SQLBuilder selectCategoriaForPrintByClause(UserContext userContext, Stampa_registro_inventarioVBulk stampa, Categoria_gruppo_inventBulk uo, CompoundFindClause clauses) throws ComponentException {
		SQLBuilder sql = getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",sql.EQUALS, "Y");
		sql.addSQLClause("AND","DATA_CANCELLAZIONE",sql.ISNULL, null);
		sql.addSQLClause("AND","LIVELLO",sql.EQUALS, "0");
		return sql;
}
/**
  *  Ricerca di un CdR per il Bene
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di un CdR Utilizzatore per il Bene
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
		sql.addSQLClause("AND", "ESERCIZIO_INIZIO", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "ESERCIZIO_FINE", sql.GREATER_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return sql;		
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
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
public SQLBuilder selectGruppoForPrintByClause(UserContext userContext, Stampa_registro_inventarioVBulk stampa, Categoria_gruppo_inventBulk uo, CompoundFindClause clauses) throws ComponentException {

	if (stampa.getCategoriaForPrint() == null || stampa.getCategoriaForPrint().getCd_categoria_gruppo() == null)
		throw new ApplicationException(new ValidationException ("Attenzione: selezionare prima una Categoria.") );
	SQLBuilder sql = getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",sql.EQUALS, "Y");
	//sql.addSQLClause("AND","DATA_CANCELLAZIONE",sql.ISNULL, null);
	sql.addSQLClause("AND","LIVELLO",sql.GREATER, "0");
	sql.addSQLClause("AND","CD_CATEGORIA_PADRE",sql.EQUALS, stampa.getCategoriaForPrint().getCd_categoria_gruppo());
	return sql;
}
/**
  *  Ricerca di una Linea di Attività per il CdR Utilizzatore
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di una Linea di Attività per il
  *		CdR Utilizzatore
  *    PostCondition:
  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che la Linea di Attività appartenga al CdR indicato.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param utilizzatori_la il <code>Inventario_utilizzatori_laBulk</code> CdR di riferimento
  * @param l_att la <code>Linea_attivitaBulk</code> Linea di Attività modello  
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
  *  Ricerca di una Ubicazione
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare una Ubicazione.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Ubicazione_beneBulk),
  *		ed è stata aggiunta la clausola che l'Ubicazione sia associata alla UO di scrivania.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene di riferimento
  * @param ubicazione la <code>Ubicazione_beneBulk</code> ubicazione di riferimento  
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectUbicazioneByClause(UserContext userContext, Inventario_beniBulk bene, Ubicazione_beneBulk ubicazione, CompoundFindClause clauses) 
		throws ComponentException
{
		String cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		SQLBuilder sql = getHome(userContext, Ubicazione_beneBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		sql.openParenthesis("AND");
		// Cerca le ubicazioni relative al CdS/UO discrivania	
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, cds_scrivania);
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, uo_scrivania);
		
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
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_beni_senza_utilizVBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	// Recupera il Cd_Cds di scrivania
	String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
	CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
	CdsBulk	cds_scrivania;
	try{
		// Costruisce il CdsBulk di scrivania
		cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw handleException(pe);
	}
	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql;
	// Verifica se il Cds di scrivania è di tipo ENTE
	if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
		sql = home.createSQLBuilderEsteso();
	} else{
		sql = home.createSQLBuilder();
		sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	}	
	sql.addClause(clauses);
	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_registro_inventarioVBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	// Recupera il Cd_Cds di scrivania
	String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
	CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
	CdsBulk	cds_scrivania;
	try{
		// Costruisce il CdsBulk di scrivania
		cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw handleException(pe);
	}
	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql;
	// Verifica se il Cds di scrivania è di tipo ENTE
	if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
		sql = home.createSQLBuilderEsteso();
	} else{
		sql = home.createSQLBuilder();
		sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	}	
	sql.addClause(clauses);
	return sql;
}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_beni_senza_utilizVBulk)
		validateBulkForPrint(aUC, (Stampa_beni_senza_utilizVBulk)bulk);
	else if (bulk instanceof Stampa_registro_inventarioVBulk)
		validateBulkForPrint(aUC, (Stampa_registro_inventarioVBulk)bulk);
	return bulk;
}
/** 
  *  validaBene - Categoria Gruppo Inventario non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stata indicata nessuna Categoria 
  *		Gruppo Inventario per il bene
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene
  *
  *  validaBene - Descrizione del Bene non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stata indicata una descrizione
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene
  *
  *  validaBene - Condizione del Bene non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stata indicata una condizione
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene  
  *
  *  validaBene - Ubicazione del Bene non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stata indicata una ubicazione
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene
  *
  *  validaBene - Prezzo unitario del Bene non valido
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stato indicato il Valore Iniziale,
  *		oppure il valore indicato è inferiore al Valore del Bene
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene
  *
  *  validaBene - Valore da ammortizzare del Bene non valido
  *    PreCondition:
  *      Si sta tentando di salvare un bene di cui non è stato indicato un valore da ammortizzare,
  *		oppure il valore indicato è inferiore al valore del bene, 
  *		oppure è inferiore al valore già ammortizzato.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare il Bene
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene da controllare
**/
private void validaBene (UserContext aUC, Inventario_beniBulk bene) 
	throws ComponentException
{
	java.math.BigDecimal zero = new java.math.BigDecimal(0);	
	try{	
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA CATEGORIA PER IL BENE
		if (bene.getCategoria_Bene()==null || bene.getCategoria_Bene().getCd_categoria_gruppo()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare la Categoria di appartenenza");
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA targa PER IL BENE
		if (!bene.isBeneAccessorio() && bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
				bene.getCategoria_Bene().getFl_gestione_targa() && bene.getTarga()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: è obbligatorio indicare la targa per questa Categoria");
//		if (!bene.isBeneAccessorio() && bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
//				bene.getCategoria_Bene().getFl_gestione_seriale() && bene.getSeriale()==null)
//			throw new it.cnr.jada.comp.ApplicationException("Attenzione: è obbligatorio indicare il seriale per questa Categoria");
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE PER IL BENE
		if (bene.getDs_bene()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare la Descrizione del Bene ");					
		
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA CONDIZIONE PER IL BENE
		if (bene.getCondizioneBene()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Condizione");			
	
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA UBICAZIONE PER IL BENE
		if (bene.getUbicazione()==null || bene.getUbicazione().getCd_ubicazione()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare l'Ubicazione del Bene");
		if (bene.getV_utilizzatoriColl().size()==0 && !bene.isBeneAccessorio())
			throw new ApplicationException("Attenzione: bisogna indicare gli Utilizzatori");				
		// CONTROLLA CHE SIA STATO INSERITO IL PREZZO UNITARIO PER IL BENE
		if (bene.getValore_unitario()==null || bene.getValore_unitario().compareTo(zero)<=0)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare il Valore Iniziale");

		java.math.BigDecimal valore_iniziale = bene.getValore_iniziale();
		bene.setValore_iniziale(bene.getValore_unitario());
		/*if (bene.getValore_unitario().compareTo(bene.getValoreBene())<0){*/
		if (bene.getValoreBene().compareTo(zero)<0){
			bene.setValore_iniziale(valore_iniziale);	
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: valore iniziale non valido.\n Le variazioni effettuate sul valore del bene,\n non pemettono di inserire  un valore iniziale inferiore a " + bene.getValore_iniziale().subtract(bene.getValoreBene()));
		}
		// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE UNITARIO
		/*if (bene.getImponibile_ammortamento() != null && bene.getImponibile_ammortamento().compareTo(bene.getValore_unitario())>0){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il valore da ammortizzare non è valido\n Il valore da ammortizzare del bene deve essere inferiore  o uguale al valore iniziale del bene.");
		}*/

		// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE DEL BENE
		if (bene.getImponibile_ammortamento() != null && bene.getImponibile_ammortamento().compareTo(bene.getValoreBene())>0){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il valore da ammortizzare non è valido\n Il valore da ammortizzare del bene non può essere superiore al valore del bene.");
		}
		
		// CONTROLLA CHE IL VALORE DA AMMORTIZZARE NON SIA INFERIORE AL VALORE AMMORTIZZATO
		if (bene.getImponibile_ammortamento() == null && bene.getValore_ammortizzato()!=null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il bene è già stato ammortizzato.\n Il valore da ammortizzare non può essere 0");
		}
		if (bene.getImponibile_ammortamento() != null  && bene.getValore_ammortizzato()!=null && bene.getImponibile_ammortamento().compareTo(bene.getValore_ammortizzato())<0){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il valore da ammortizzare non è valido\n Il valore da ammortizzare di un bene non può essere inferiore al valore già ammortizzato.");
		}
	
	}catch(Throwable t){
		throw handleException(bene, t);		
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_beni_senza_utilizVBulk stampa) throws ComponentException{
	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());
		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");
		java.sql.Timestamp firstDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
		if (stampa.getDataInizio().compareTo(firstDayOfYear)<0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
		}
		if (stampa.getDataFine().compareTo(lastDayOfYear)>0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
		}
	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_registro_inventarioVBulk stampa) throws ComponentException{
	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		// Controlli sui campi Data		
		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
		// Controlli sui Campi nr_Inventario_da	nr_Inventario_a
		if (stampa.getNrInventarioFrom()== null){
			throw new ValidationException("Il campo DA CODICE BENE è obbligatorio");
		}
		if (stampa.getNrInventarioTo()== null){
			throw new ValidationException("Il campo A CODICE BENE è obbligatorio");
		}			
	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/** 
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificato il codice.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare il codice del CdR.
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Il CdR Utilizzatore è stato indicato già in precedenza.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		utilizzare più volte lo stesso CdR
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificata la percentuale di utilizzo.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare la percentuale di utilizzo.
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificata alcuna Linea di Attività
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare per ogni CdR almeno una Linea di Attività.
  *  
  *  validaUtilizzatori - Percentuale utilizzo dei CdR non valida
  *    PreCondition:
  *      Il totale delle percentuali di utilizzo indicate per ogni CdR, non è 100.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare che il totale deve essere 100.
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene di cui controllare gli Utilizzatori
**/
private void validaUtilizzatori (UserContext aUC,Inventario_beniBulk bene) 
	throws ComponentException
{
	SimpleBulkList cdr_utilizzatori = bene.getV_utilizzatoriColl();
	java.math.BigDecimal zero = new java.math.BigDecimal(0);
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	java.math.BigDecimal percentuale_utilizzo_CdR = new java.math.BigDecimal("0");	
	java.util.Vector cdr = new java.util.Vector();
	int riga = 0;
	if (cdr_utilizzatori.size()>0){
		Iterator i = cdr_utilizzatori.iterator();		
		while (i.hasNext()){
			riga++;
			
			Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)i.next();
			// Controlla che sia stato specificato il CdR
			if (utilizzatore.getCdr()==null || utilizzatore.getCdCdr()==null){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario indicare il codice del CdR Utilizzatore.\n " +
					"Il CdR alla riga " + riga + " non è valido");
			}
			// Controlla che non vi siano CdR DUPLICATI
			if (it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(cdr,utilizzatore.getCdr())){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: non è possibile indicare più volte uno stesso CdR come Utilizzatore\n " +
					"Il CdR " + utilizzatore.getCdCdr() +" è duplicato.");
			}
			else {
				cdr.add(utilizzatore.getCdr());
			}
			// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per il CdR
			if (utilizzatore.getPercentuale_utilizzo_cdr()!=null && ((utilizzatore.getPercentuale_utilizzo_cdr().compareTo(zero))>0)){
				percentuale_utilizzo_CdR = percentuale_utilizzo_CdR.add(utilizzatore.getPercentuale_utilizzo_cdr());				 
			}
			else if (utilizzatore.getPercentuale_utilizzo_cdr()==null){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: la percentuale di utilizzo per i CdR Utilizzatori non può essere nulla\n " +
					"Specificare la perc. di utilizzo per il CdR " + utilizzatore.getCdCdr());
			}
			// Controlla che per ogni CdR specificato siano state indicate anche delle Linee di Attività
			if (utilizzatore.getBuono_cs_utilizzatoriColl()==null || (utilizzatore.getBuono_cs_utilizzatoriColl().size()==0)){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare i GAE per ogni Utilizzatore\n "+
					"Indicare le Linee di Attività per il CdR " + utilizzatore.getCdCdr());
			}
		}
		// Controlla che il totale delle percentuali di utilizzo dei CdR sia 100
		if (percentuale_utilizzo_CdR.compareTo(cento)!=0)
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: la percentuale totale di utilizzo dei CdR deve essere 100.\n "+
				"Verificare le percentuali di utilizzo degli Utilizzatori ");
			
	}
}
	public HashMap<Obbligazione_scadenzarioBulk, Boolean> creaUtilizzatori(UserContext userContext, Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk, Buono_carico_scarico_dettBulk buono) throws ComponentException {
		HashMap<Obbligazione_scadenzarioBulk, Boolean> resUtilizzatori = new HashMap<Obbligazione_scadenzarioBulk, Boolean>();
		Boolean exit = false;
		BigDecimal tot_perc_cdr = new BigDecimal(0);
		try {
			Obbligazione_scadenzarioHome obblHome = ((Obbligazione_scadenzarioHome) getHome(userContext, Obbligazione_scadenzarioBulk.class, null, getFetchPolicyName("findForInventario")));
			ObbligazioneBulk obblig = obbligazione_scadenzarioBulk.getObbligazione();
			ObbligazioneHome obbligHome = (ObbligazioneHome) getHome(userContext, obblig.getClass());
			obblig.setObbligazione_scadenzarioColl(new BulkList(obbligHome.findObbligazione_scadenzarioList(obblig)));
			for (Iterator i = obblig.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); ) {
				Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) i.next();
				if (os.getIm_associato_doc_amm().doubleValue() != 0 && os.equalsByPrimaryKey(obbligazione_scadenzarioBulk)) {
					os.setObbligazione(obblig);
					os.setObbligazione_scad_voceColl(new BulkList(obblHome.findObbligazione_scad_voceList(userContext, os)));
					obblig.refreshDettagliScadenzarioPerCdrECapitoli();
					for (Iterator lista_cdraggr = obblig.getCdrAggregatoColl().iterator(); lista_cdraggr.hasNext(); ) {
						Obbligazione_scad_voce_aggregatoBulk cdraggr = (Obbligazione_scad_voce_aggregatoBulk) lista_cdraggr.next();
						if (cdraggr != null && cdraggr.getImporto() != null && cdraggr.getImporto().doubleValue() != 0) {
							BigDecimal tot_perc_la = new BigDecimal(0);
							Iterator listaScad_voce = obblHome.findObbligazione_scad_voceList(userContext, os).iterator();
							for (Iterator x = listaScad_voce; x.hasNext(); ) {
								Obbligazione_scad_voceBulk dett = (Obbligazione_scad_voceBulk) x.next();
								getHomeCache(userContext).fetchAll(userContext);
								WorkpackageBulk linea_att = dett.getLinea_attivita();
								if (linea_att.getCd_centro_responsabilita() == cdraggr.getCodice()) {
									if (dett.getObbligazione_scadenzario().getIm_scadenza().doubleValue() != 0)
										dett.setPrc((dett.getIm_voce().multiply(new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
									if (dett.getPrc() != null && dett.getPrc().compareTo(new BigDecimal(0)) != 0 && dett.getObbligazione_scadenzario().getIm_associato_doc_amm().doubleValue() != 0) {
										it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk new_utilizzatore_la
												= new it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(), linea_att.getCd_centro_responsabilita(),
												buono.getNr_inventario(),
												buono.getPg_inventario(), new Long(buono.getProgressivo().longValue()));
										BigDecimal perc_cdr = (cdraggr.getImporto().multiply(new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(), 2, 6));
										tot_perc_cdr = tot_perc_cdr.add(perc_cdr);
										tot_perc_la = tot_perc_la.add(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr, 2, 6));
										if ((tot_perc_cdr.doubleValue() >= 100 && tot_perc_la.doubleValue() >= 100)) {
											exit = true;
										}
										new_utilizzatore_la.setPercentuale_utilizzo_cdr(perc_cdr);
										new_utilizzatore_la.setPercentuale_utilizzo_la(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr, 2, 6));
										new_utilizzatore_la.setToBeCreated();
										if ((perc_cdr.compareTo(new BigDecimal(100)) <= 0) || (((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr, 2, 6).compareTo(new BigDecimal(100)) <= 0)) {
											Inventario_utilizzatori_laHome Inventario_utilizzatore_laHome = (Inventario_utilizzatori_laHome) getHome(userContext, Inventario_utilizzatori_laBulk.class);
											Inventario_utilizzatori_laBulk utilizzatore = (Inventario_utilizzatori_laBulk) Inventario_utilizzatore_laHome.findByPrimaryKey(new Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(), linea_att.getCd_centro_responsabilita(),
													buono.getNr_inventario(),
													buono.getPg_inventario(), new Long(buono.getProgressivo().longValue())));
											if (!new_utilizzatore_la.equalsByPrimaryKey(utilizzatore))
												super.insertBulk(userContext, new_utilizzatore_la, true);
										}
									}
								}
							}
						}
					}
					Iterator listaScad_voce = obblHome.findObbligazione_scad_voceList(userContext, os).iterator();
					getHomeCache(userContext).fetchAll(userContext);
					resUtilizzatori.put(os,exit);
					return resUtilizzatori;
				}
			}
		} catch (PersistencyException e1) {
			throw new ComponentException(e1);
		}
		resUtilizzatori.put(obbligazione_scadenzarioBulk,exit);
		return resUtilizzatori;
	}

}
