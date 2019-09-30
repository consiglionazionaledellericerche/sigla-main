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

package it.cnr.contab.inventario00.docs.bulk;

import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_inv_bene_fatturaHome extends BulkHome {
public Ass_inv_bene_fatturaHome(java.sql.Connection conn) {
	super(Ass_inv_bene_fatturaBulk.class,conn);
}
public Ass_inv_bene_fatturaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_inv_bene_fatturaBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, it.cnr.jada.comp.ComponentException {
	try {
		
	Ass_inv_bene_fatturaBulk ass=new Ass_inv_bene_fatturaBulk();	
	((Ass_inv_bene_fatturaBulk)oggettobulk).setPg_riga(
	new Long(
	((Long)findAndLockMax( ass, "pg_riga", new Long(0) )).longValue()+1));
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
	 throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	} 
	super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}
public Long findmax(Ass_inv_bene_fatturaBulk ass) throws PersistencyException{	
		return new Long(((Long)findMax(ass,"pg_riga",new Long(0)))).longValue()+1;
}
/*
 * Permette, una volta fatte tutte le operazioni di Fattura e di Inventario,
 *	di rendere persistente la tabella "righeFatturaHash", la quale contiene le associazioni
 *	fra le righe di Fattura e i Beni ad esse associati. 
*/
public void makePersistentAssocia(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fattura_passiva) throws PersistencyException, IntrospectionException {
	// caso normale in cui le associazioni tra la riga di fattura ed i buono  esistono sui dati di Inventario_beni_apg
	if(associaBulk.getLocal_transactionID()!=null){
		PersistentHome apgHome = getHomeCache().getHome(Inventario_beni_apgBulk.class);
		SQLBuilder sql = apgHome.createSQLBuilder();
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
		sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_BUONO_C_S",sql.GREATER,"0");
		List beni_apg=apgHome.fetchAll(sql);
		Ass_inv_bene_fatturaBulk nuova_associazione = new Ass_inv_bene_fatturaBulk();
		for (Iterator i = beni_apg.iterator();i.hasNext();){
			Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)i.next();
				try{
				//r.p. Prendo il progressivo dalla fattura_passivaBulk perchè viene aggiornato
					nuova_associazione.setRiga_fatt_pass (new Fattura_passiva_rigaIBulk(fattura_passiva.getCd_cds(),fattura_passiva.getCd_unita_organizzativa(),
							fattura_passiva.getEsercizio(),fattura_passiva.getPg_fattura_passiva(),bene_apg.getProgressivo_riga()));
					nuova_associazione.getRiga_fatt_pass().setFattura_passivaI(new Fattura_passiva_IBulk(fattura_passiva.getCd_cds(),fattura_passiva.getCd_unita_organizzativa(),
							fattura_passiva.getEsercizio(),fattura_passiva.getPg_fattura_passiva()));
					nuova_associazione.setNr_inventario(bene_apg.getNr_inventario());
					nuova_associazione.setProgressivo(bene_apg.getProgressivo());		
					nuova_associazione.setTest_buono(new Buono_carico_scaricoBulk(bene_apg.getPg_inventario(),bene_apg.getTi_documento(),bene_apg.getEsercizio(),bene_apg.getPg_buono_c_s()));
					nuova_associazione.setUser(fattura_passiva.getUser());
					//perchè gia' loccata in modifica
					nuova_associazione.setPg_riga(new Long(
							(Long)findAndLockMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
					insert(nuova_associazione, userContext);
					apgHome.delete(bene_apg, userContext);
			}catch(it.cnr.jada.bulk.BusyResourceException e) {
				nuova_associazione.setPg_riga(new Long(
				(Long)findMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
				insert(nuova_associazione, userContext);
				apgHome.delete(bene_apg, userContext);
				//throw new PersistencyException(e);
			}
		}
	}
	else
	{
		//nel caso in cui viene sdoppiata la riga di fattura non esistono i dati su Inventario_beni_apg
		insert(associaBulk, userContext);
	}
}

/*
 * Permette, una volta fatte tutte le operazioni di Fattura e di Inventario,
 *	di rendere persistente le modifiche apportate ai beni associati per aumento di valore.
 *	La stored Procedure trichiamata si occupa di creare il buono di carico con i dettagli,
 *	di rendere persistenti sia le modifiche ai beni, sia le associazioini tra i beni e le righe di fattura
*/
public String makePersistentAssociaPerAumento(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, Fattura_passivaBulk fattura) throws PersistencyException, IntrospectionException {

	String msg = null;

	// Recupera il pg per il buono di carico
	Numeratore_buono_c_sHome numeratoreHome = (Numeratore_buono_c_sHome) getHomeCache().getHome(Numeratore_buono_c_sBulk.class);
	Long pg_buonoC = null;
	LoggableStatement  cs = null;
	try{
	    if (associaBulk.getTest_buono() !=null){
		pg_buonoC = numeratoreHome.getNextPg(userContext,
					associaBulk.getTest_buono().getEsercizio(),
					associaBulk.getTest_buono().getPg_inventario(),
					"C",
					associaBulk.getUser());

		cs = new LoggableStatement(getConnection(),
				"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB400.updCaricoBeniAumentoValFtPas(?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.setString(1, associaBulk.getLocal_transactionID()); 							// local_trans_id
		cs.setLong	(2, associaBulk.getTest_buono().getPg_inventario().longValue()); 	// pg_inventario
		cs.setInt	(3, associaBulk.getTest_buono().getEsercizio().intValue()); 		// esercizio
		cs.setLong	(4,	pg_buonoC.longValue()); 										// pg_buono_carico_scarico
		cs.setString(5, associaBulk.getTest_buono().getDs_buono_carico_scarico()); 	// ds_buono_carico_scarico
		cs.setString(6, associaBulk.getTest_buono().getCd_tipo_carico_scarico()); 	// cd_tipo_carico_scarico
		cs.setLong	(7,	fattura.getPg_fattura_passiva().longValue()); 					// pg_fattura
		cs.setString(8, associaBulk.getUser()); 										// utente
		cs.setTimestamp(9, associaBulk.getTest_buono().getData_registrazione()); 		// data_registrazione
		cs.registerOutParameter(10, java.sql.Types.VARCHAR); 							// Eventuale Messaggio di ritorno
		cs.executeQuery();
			
		msg= cs.getString(10);
		}
		
		
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw new PersistencyException(e);
	} 
	catch (Throwable e) {
	throw new PersistencyException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw new PersistencyException(e);
		}
	}
	return msg;
}
/*
 * Permette, una volta fatte tutte le operazioni di Fattura e di Inventario,
 *	di rendere persistente la tabella "righeFatturaHash", la quale contiene le associazioni
 *	fra le righe di Fattura e i Beni ad esse associati. 
*/
public void makePersistentAssocia(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fattura) throws PersistencyException, IntrospectionException {
	Ass_inv_bene_fatturaBulk nuova_associazione = new Ass_inv_bene_fatturaBulk();
	PersistentHome apgHome = getHomeCache().getHome(Inventario_beni_apgBulk.class);		
	SQLBuilder sql = apgHome.createSQLBuilder();
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_BUONO_C_S",sql.GREATER,"0");
	List beni_apg=apgHome.fetchAll(sql);
	for (Iterator i = beni_apg.iterator();i.hasNext();){
	
		Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)i.next();
		try{
		
		//r.p. Prendo il progressivo dalla fattura_passivaBulk perchè viene aggiornato
		nuova_associazione.setRiga_fatt_att (new Fattura_attiva_rigaIBulk(fattura.getCd_cds(),fattura.getCd_unita_organizzativa(),
				fattura.getEsercizio(),fattura.getPg_fattura_attiva(),bene_apg.getProgressivo_riga()));
		nuova_associazione.getRiga_fatt_att().setFattura_attivaI(new Fattura_attiva_IBulk(fattura.getCd_cds(),fattura.getCd_unita_organizzativa(),
				fattura.getEsercizio(),fattura.getPg_fattura_attiva()));
		nuova_associazione.setNr_inventario(bene_apg.getNr_inventario());
		nuova_associazione.setProgressivo(bene_apg.getProgressivo());		
		nuova_associazione.setTest_buono(new Buono_carico_scaricoBulk(bene_apg.getPg_inventario(),bene_apg.getTi_documento(),bene_apg.getEsercizio(),bene_apg.getPg_buono_c_s()));
		nuova_associazione.setUser(fattura.getUser());
		nuova_associazione.setPg_riga(new Long(
				(Long)findAndLockMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
		insert(nuova_associazione, userContext);
		apgHome.delete(bene_apg, userContext);
		}
		catch(it.cnr.jada.bulk.BusyResourceException e) {
			nuova_associazione.setPg_riga(new Long(
				(Long)findMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
				insert(nuova_associazione, userContext);
				apgHome.delete(bene_apg, userContext);
			//	throw new PersistencyException(e);
			}
		}
	}
/*
 * Permette, una volta fatte tutte le operazioni di Fattura e di Inventario,
 *	di rendere persistente la tabella "righeFatturaHash", la quale contiene le associazioni
 *	fra le righe di Fattura e i Beni ad esse associati. 
*/
public void makePersistentAssocia(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk doc) throws PersistencyException, IntrospectionException {
	PersistentHome apgHome = getHomeCache().getHome(Inventario_beni_apgBulk.class);
	SQLBuilder sql = apgHome.createSQLBuilder();
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
	sql.addSQLClause("AND","INVENTARIO_BENI_APG.PG_BUONO_C_S",sql.GREATER,"0");
	List beni_apg=apgHome.fetchAll(sql);
	Ass_inv_bene_fatturaBulk nuova_associazione = new Ass_inv_bene_fatturaBulk();
	for (Iterator i = beni_apg.iterator();i.hasNext();){
		Inventario_beni_apgBulk bene_apg=(Inventario_beni_apgBulk)i.next();
		try{
		//r.p. Prendo il progressivo dalla fattura_passivaBulk perchè viene aggiornato
		nuova_associazione.setRiga_doc_gen(new Documento_generico_rigaBulk(doc.getCd_cds(),doc.getCd_tipo_documento_amm(),doc.getCd_unita_organizzativa(),
				doc.getEsercizio(),doc.getPg_documento_generico(),bene_apg.getProgressivo_riga()));
		nuova_associazione.getRiga_doc_gen().setDocumento_generico(new Documento_genericoBulk(doc.getCd_cds(),doc.getCd_tipo_documento_amm(),doc.getCd_unita_organizzativa(),
				doc.getEsercizio(),doc.getPg_documento_generico()));
		nuova_associazione.setNr_inventario(bene_apg.getNr_inventario());
		nuova_associazione.setProgressivo(bene_apg.getProgressivo());		
		nuova_associazione.setTest_buono(new Buono_carico_scaricoBulk(bene_apg.getPg_inventario(),bene_apg.getTi_documento(),bene_apg.getEsercizio(),bene_apg.getPg_buono_c_s()));
		nuova_associazione.setUser(doc.getUser());
		//perchè gia' loccata in modifica
		nuova_associazione.setPg_riga(new Long(
				(Long)findAndLockMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
		insert(nuova_associazione, userContext);
		apgHome.delete(bene_apg, userContext);
	
	}catch(it.cnr.jada.bulk.BusyResourceException e) {
	nuova_associazione.setPg_riga(new Long(
		(Long)findMax( nuova_associazione, "pg_riga", new Long(0))).longValue()+1);
		insert(nuova_associazione, userContext);
		apgHome.delete(bene_apg, userContext);
		//throw new PersistencyException(e);
	}
	}
}
/*
 * Permette, una volta fatte tutte le operazioni di Documento e di Inventario,
 *	di rendere persistente le modifiche apportate ai beni associati per aumento di valore.
 *	La stored Procedure richiamata si occupa di creare il buono di carico con i dettagli,
 *	di rendere persistenti sia le modifiche ai beni, sia le associazioni tra i beni e le righe di Documento
*/
public String makePersistentAssociaPerAumento(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, Documento_genericoBulk doc) throws PersistencyException, IntrospectionException {

	String msg = null;

	// Recupera il pg per il buono di carico
	Numeratore_buono_c_sHome numeratoreHome = (Numeratore_buono_c_sHome) getHomeCache().getHome(Numeratore_buono_c_sBulk.class);
	Long pg_buonoC = null;
	LoggableStatement  cs = null;
	try{
	    if (associaBulk.getTest_buono() !=null){
		pg_buonoC = numeratoreHome.getNextPg(userContext,
					associaBulk.getTest_buono().getEsercizio(),
					associaBulk.getTest_buono().getPg_inventario(),
					"C",
					associaBulk.getUser());

		cs = new LoggableStatement(getConnection(),"{call " 
				+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+"CNRCTB400.updCaricoBeniAumentoValDoc(?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );	
		cs.setString(1, associaBulk.getLocal_transactionID()); 							// local_trans_id
		cs.setLong	(2, associaBulk.getTest_buono().getPg_inventario().longValue()); 	// pg_inventario
		cs.setInt	(3, associaBulk.getTest_buono().getEsercizio().intValue()); 		// esercizio
		cs.setLong	(4,	pg_buonoC.longValue()); 										// pg_buono_carico_scarico
		cs.setString(5, associaBulk.getTest_buono().getDs_buono_carico_scarico()); 	// ds_buono_carico_scarico
		cs.setString(6, associaBulk.getTest_buono().getCd_tipo_carico_scarico()); 	// cd_tipo_carico_scarico
		cs.setLong	(7,	doc.getPg_documento_generico().longValue()); 					// pg_generico
		cs.setString(8, associaBulk.getUser()); 										// utente
		cs.setTimestamp(9, associaBulk.getTest_buono().getData_registrazione()); 		// data_registrazione
		cs.registerOutParameter(10, java.sql.Types.VARCHAR); 							// Eventuale Messaggio di ritorno
		cs.executeQuery();
			
		msg= cs.getString(10);
		}
		
		
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw new PersistencyException(e);
	} 
	catch (Throwable e) {
	throw new PersistencyException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw new PersistencyException(e);
		}
	}
	return msg;
}
}
