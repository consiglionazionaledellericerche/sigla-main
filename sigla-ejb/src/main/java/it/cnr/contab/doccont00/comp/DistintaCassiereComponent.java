package it.cnr.contab.doccont00.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIHome;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_mandato_reversaleBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_mandato_reversaleHome;
import it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIHome;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIHome;
import it.cnr.contab.doccont00.intcass.bulk.DistintaCassiere1210Bulk;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereHome;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiere_detBulk;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiere_detHome;
import it.cnr.contab.doccont00.intcass.bulk.ExtCassiereCdsBulk;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_scartiBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_distinta_cass_im_man_revBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDNotDeletableException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLQuery;
import it.cnr.jada.persistency.sql.SQLUnion;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.commons.io.IOUtils;

public class DistintaCassiereComponent extends
		it.cnr.jada.comp.CRUDDetailComponent implements IDistintaCassiereMgr,
		Cloneable, Serializable {

	final private static String SEMAFORO_DISTINTA = "DISTINTA_CASSIERE00";

	public DistintaCassiereComponent() {
	}

	/**
	 * Crea un semaforo per consentire l'immissione della prima distinta
	 * 
	 * Nome: crea semaforo - ok Pre: E' necessario inserire la prima distinta (
	 * per esercizio e uo) e nessun altro utente sta eseguendo questa operazione
	 * Post: Il semaforo viene acquisito in modo da impedire ad altri utenti
	 * l'inserimento di un'altra distinta
	 * 
	 * Nome: crea semaforo - errore Pre: E' necessario inserire la prima
	 * distinta ( per esercizio e uo) e un altro utente sta eseguendo la stessa
	 * operazione Post: Una segnalazione di errore comunica all'utente
	 * l'impossibilità a procedere
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            l'istanza di Distinta_cassiereBulk da inserire
	 */

	protected void acquisisciSemaforo(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{  call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB800.acquisisciSemaforo(?, ?, ?, ?)}",
					false, this.getClass());
			try {
				cs.setObject(1, distinta.getEsercizio());
				cs.setString(2, distinta.getCd_unita_organizzativa());
				cs.setString(3, SEMAFORO_DISTINTA);
				cs.setString(4, userContext.getUser());
				cs.executeQuery();
			} catch (SQLException e) {
				throw handleException(e);
			} finally {
				cs.close();
			}
		} catch (SQLException e) {
			throw handleException(e);
		}

	}

	/**
	 * Assegna lo stato trasmissione di mandato/reversale
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Aggiorna stato mandato accreditamento Pre: Una richiesta di
	 * aggiornare lo stato trasmissione di un documento contabile di tipo
	 * mandato di accreditamento e' stata generata Post: E' stato aggiornato lo
	 * stato del mandato di accreditamento (metodo
	 * 'aggiornaStatoMandatoAccreditamento')
	 * 
	 * Nome: Aggiorna stato mandato non di accreditamento Pre: Una richiesta di
	 * aggiornare lo stato trasmissione di un mandato con tipologia diversa da
	 * accreditamento e' stata generata Post: E' stato aggiornato lo stato del
	 * mandato (metodo 'aggiornaStatoMandato')
	 * 
	 * Nome: Aggiorna stato reversale Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di una reversale e' stata generata Post: E' stato aggiornato
	 * lo stato della reversale (metodo 'aggiornaStatoReversale')
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param docContabile
	 *            il documento contabile V_mandato_reversaleBulk per cui
	 *            aggiornare lo stato
	 * @param stato_trasmissione
	 *            il nuovo stato che il doc. contabile dovrà assumere
	 */

	protected void aggiornaStatoDocContabile(UserContext userContext,
			V_mandato_reversaleBulk docContabile, String stato_trasmissione)
			throws OutdatedResourceException,
			it.cnr.jada.persistency.PersistencyException, ComponentException,
			BusyResourceException, javax.ejb.EJBException {
		if (docContabile.isMandatoAccreditamento())
			aggiornaStatoMandatoAccreditamento(userContext, docContabile,
					stato_trasmissione);
		else if (docContabile.isMandato())
			aggiornaStatoMandato(userContext, docContabile, stato_trasmissione);
		else if (docContabile.isReversale())
			aggiornaStatoReversale(userContext, docContabile,
					stato_trasmissione);
	}

	/**
	 * Assegna lo stato trasmissione di tutti i mandati/reversali inseriti in
	 * distinta
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Aggiorna stato doc.contabili Pre: Una richiesta di aggiornare lo
	 * stato trasmissione di tutti doc. contabili inseriti in una distinta e'
	 * stata generata Post: Per ogni doc. contabile inserito in distinta e'
	 * stato richiamato il metodo 'aggiornaStatoDocContabile' che ne ha
	 * aggiornato lo stato trasmissione
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la distinta i cui doc.contabili devono essere aggiornati
	 * @param stato_trasmissione
	 *            il nuovo stato che il doc. contabile dovrà assumere
	 */

	protected void aggiornaStatoDocContabili(UserContext userContext,
			Distinta_cassiereBulk distinta, String stato_trasmissione)
			throws ComponentException {
		try {
			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			SQLBroker broker = getHome(userContext,
					V_mandato_reversaleBulk.class).createBroker(sql);
			V_mandato_reversaleBulk docContabile;
			while (broker.next()) {
				docContabile = (V_mandato_reversaleBulk) broker
						.fetch(V_mandato_reversaleBulk.class);
				aggiornaStatoDocContabile(userContext, docContabile,
						stato_trasmissione);
			}
			broker.close();

			/*
			 * 
			 * String schema =
			 * it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
			 * Timestamp ts = getHome( userContext,
			 * Distinta_cassiereBulk.class).getServerTimestamp();
			 * java.sql.PreparedStatement ps = null; ps =
			 * getConnection(userContext).prepareStatement( "UPDATE "+ schema+
			 * "MANDATO A " + "SET STATO_TRASMISSIONE = ?, " +
			 * "PG_VER_REC = PG_VER_REC + 1, " + "DUVA = ?, " + "UTUV = ? " +
			 * "WHERE EXISTS " + "(SELECT 1 FROM  " + schema +
			 * "DISTINTA_CASSIERE_DET B " + "WHERE " + "CD_CDS = ? AND " +
			 * "CD_UNITA_ORGANIZZATIVA = ? AND " + "ESERCIZIO = ? AND " +
			 * "PG_DISTINTA = ? AND " + "PG_MANDATO IS NOT NULL AND " +
			 * "A.CD_CDS = B.CD_CDS AND " + "A.ESERCIZIO = B.ESERCIZIO AND " +
			 * "A.PG_MANDATO = B.PG_MANDATO) " );
			 * 
			 * ps.setString(1, stato_trasmissione); ps.setTimestamp( 2, ts );
			 * ps.setString(3, distinta.getUser()); ps.setString(4,
			 * distinta.getCd_cds()); ps.setString(5,
			 * distinta.getCd_unita_organizzativa()); ps.setObject(6,
			 * distinta.getEsercizio()); ps.setObject(7,
			 * distinta.getPg_distinta()); LoggableStatement.execute(ps);
			 * try{ps.close();}catch( java.sql.SQLException e ){};
			 * 
			 * ps = getConnection(userContext).prepareStatement( "UPDATE "+
			 * schema+ "REVERSALE A " + "SET STATO_TRASMISSIONE = ?, " +
			 * "PG_VER_REC = PG_VER_REC + 1, " + "DUVA = ?, " + "UTUV = ? " +
			 * "WHERE EXISTS " + "(SELECT 1 FROM  " + schema +
			 * "DISTINTA_CASSIERE_DET B " + "WHERE " + "CD_CDS = ? AND " +
			 * "CD_UNITA_ORGANIZZATIVA = ? AND " + "ESERCIZIO = ? AND " +
			 * "PG_DISTINTA = ? AND " + "PG_REVERSALE IS NOT NULL AND " +
			 * "A.CD_CDS = B.CD_CDS AND " + "A.ESERCIZIO = B.ESERCIZIO AND " +
			 * "A.PG_REVERSALE = B.PG_REVERSALE) " );
			 * 
			 * ps.setString(1, stato_trasmissione); ps.setTimestamp( 2, ts );
			 * ps.setString(3, distinta.getUser()); ps.setString(4,
			 * distinta.getCd_cds()); ps.setString(5,
			 * distinta.getCd_unita_organizzativa()); ps.setObject(6,
			 * distinta.getEsercizio()); ps.setObject(7,
			 * distinta.getPg_distinta()); LoggableStatement.execute(ps);
			 * try{ps.close();}catch( java.sql.SQLException e ){};
			 */

		} catch (Exception e) {
			throw handleException(e);
		}

	}

	/**
	 * Assegna lo stato trasmissione di un mandato con tipologia diversa da
	 * accreditamento
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Aggiorna stato mandato a INSERITO_IN_DISTINTA/NON INSERITO IN
	 * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di un
	 * mandato non di accreditamento e' stata generata e il nuovo stato e'
	 * INSERITO_IN_DISTINTA o NON INSERITO IN DISTINTA Post: E' stato aggiornato
	 * lo stato del mandato
	 * 
	 * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di un mandato non di accreditamento e' stata generata e il
	 * nuovo stato e' TRASMESSO e il mandato non e' ancora stato trasmesso Post:
	 * E' stato aggiornato lo stato del mandato e la sua data di trasmissione
	 * 
	 * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di un mandato non di accreditamento e' stata generata e il
	 * nuovo stato e' TRASMESSO e il mandato era già stato trasmesso Post: E'
	 * stato aggiornato lo stato del mandato e la sua data di ritrasmissione
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param docContabile
	 *            il mandato non di accredit. per cui aggiornare lo stato
	 * @param stato_trasmissione
	 *            il nuovo stato che il mandato dovrà assumere
	 */

	protected void aggiornaStatoMandato(UserContext userContext,
			V_mandato_reversaleBulk docContabile, String stato_trasmissione)
			throws OutdatedResourceException,
			it.cnr.jada.persistency.PersistencyException, ComponentException,
			BusyResourceException, javax.ejb.EJBException {
		MandatoBulk mandato = (MandatoBulk) getHome(userContext,
				MandatoIBulk.class).findAndLock(
				new MandatoIBulk(docContabile.getCd_cds(), docContabile
						.getEsercizio(), docContabile.getPg_documento_cont()));

		 if ( mandato.getPg_ver_rec().compareTo( docContabile.getPg_ver_rec())
		 != 0 )
		 throw new ApplicationException("Attenzione! Il mandato " +
		 mandato.getPg_mandato() +
		 " non e' più valido perche' stato modificato." );
		if(mandato.isAnnullato())
			mandato.setStato_trasmissione_annullo(stato_trasmissione);
		else
			mandato.setStato_trasmissione(stato_trasmissione);

		// se si tratta di una prima trasmissione aggiorna la dt_trasmissione
		if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
				.getStato_trasmissione())){
			if( mandato.getDt_trasmissione() == null) {
			// Se la data di annullamento NON E' NULLA, e siamo in esercizio
			// successivo, metto
			// la data di trasmissione = ad istante successivo a quella di
			// annullamento
			if (DateServices.isAnnoMaggEsScriv(userContext)) {
				if (mandato.getDt_annullamento() != null) {
					mandato.setDt_trasmissione(DateServices.getNextMinTs(
							userContext, mandato.getDt_annullamento()));
				} else {
					mandato
							.setDt_trasmissione(DateServices
									.getMidDayTs(DateServices
											.getTs_valido(userContext)));
				}
			} else {
				mandato.setDt_trasmissione(DateServices
						.getTs_valido(userContext));
			}
			// se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
		} else if (mandato.getDt_trasmissione() != null) {
			if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
					.getStato_trasmissione_annullo()) || mandato
					.getStato_trasmissione_annullo() ==null){
				if (DateServices.isAnnoMaggEsScriv(userContext)) {
					if (mandato.getDt_trasmissione().after(
							mandato.getDt_annullamento()))
						mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
								userContext, mandato.getDt_trasmissione()));
					else
						mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
								userContext, mandato.getDt_annullamento()));
				} else  {
							mandato.setDt_ritrasmissione(DateServices
							.getTs_valido(userContext));
				}
			}else mandato.setDt_ritrasmissione( null);
		}
	}
	mandato.setUser(userContext.getUser());
	updateBulk(userContext, mandato);

}

	/**
	 * Assegna lo stato trasmissione di un mandato con tipologia accreditamento
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Aggiorna stato mandato a INSERITO_IN_DISTINTA/NON INSERITO IN
	 * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di un
	 * mandato di accreditamento e' stata generata e il nuovo stato e' INSERITO
	 * IN DISTINTA o NON INSERITO IN DISTINTA Post: E' stato aggiornato lo stato
	 * del mandato
	 * 
	 * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di un mandato di accreditamento e' stata generata e il nuovo
	 * stato e' TRASMESSO e il mandato non e' ancora stato trasmesso Post: E'
	 * stato aggiornato lo stato del mandato e la sua data di trasmissione
	 * 
	 * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di un mandato di accreditamento e' stata generata e il nuovo
	 * stato e' TRASMESSO e il mandato era già stato trasmesso Post: E' stato
	 * aggiornato lo stato del mandato e la sua data di ritrasmissione
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param docContabile
	 *            il mandato di accredit. per cui aggiornare lo stato
	 * @param stato_trasmissione
	 *            il nuovo stato che il mandato dovrà assumere
	 */

	protected void aggiornaStatoMandatoAccreditamento(UserContext userContext,
			V_mandato_reversaleBulk docContabile, String stato_trasmissione)
			throws OutdatedResourceException,
			it.cnr.jada.persistency.PersistencyException, ComponentException,
			BusyResourceException, javax.ejb.EJBException {
		MandatoBulk mandato = (MandatoBulk) getHome(userContext,
				MandatoAccreditamentoBulk.class).findAndLock(
				new MandatoAccreditamentoBulk(docContabile.getCd_cds(),
						docContabile.getEsercizio(), docContabile
								.getPg_documento_cont()));
		 if ( mandato.getPg_ver_rec().compareTo( docContabile.getPg_ver_rec())
		 != 0 )
		 throw new ApplicationException("Attenzione! Il mandato " +
		 mandato.getPg_mandato() +
		 " non e' più valido perche' stato modificato." );

		mandato.setStato_trasmissione(stato_trasmissione);

		// se si tratta di una prima trasmissione aggiorna la dt_trasmissione
		if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
				.getStato_trasmissione())){
			if( mandato.getDt_trasmissione() == null) {
			// Se la data di annullamento NON E' NULLA, e siamo in esercizio
			// successivo, metto
			// la data di trasmissione = ad istante successivo a quella di
			// annullamento
			if (DateServices.isAnnoMaggEsScriv(userContext)) {
				if (mandato.getDt_annullamento() != null) {
					mandato.setDt_trasmissione(DateServices.getNextMinTs(
							userContext, mandato.getDt_annullamento()));
				} else {
					mandato
							.setDt_trasmissione(DateServices
									.getMidDayTs(DateServices
											.getTs_valido(userContext)));
				}
			} else {
				mandato.setDt_trasmissione(DateServices
						.getTs_valido(userContext));
			}
			// se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
		} else if ( mandato.getDt_trasmissione() != null) {
			if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
					.getStato_trasmissione_annullo()) || mandato
					.getStato_trasmissione_annullo() ==null){
			
				if (DateServices.isAnnoMaggEsScriv(userContext)) {
					if (mandato.getDt_annullamento() == null
							|| mandato.getDt_trasmissione().after(
									mandato.getDt_annullamento()))
						mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
								userContext, mandato.getDt_trasmissione()));
					else
						mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
								userContext, mandato.getDt_annullamento()));
				} else {
					mandato.setDt_ritrasmissione(DateServices
							.getTs_valido(userContext));
				}
					
			 }else mandato.setDt_ritrasmissione( null);
		}
	}
	mandato.setUser(userContext.getUser());
	updateBulk(userContext, mandato);

}

	/**
	 * Assegna lo stato trasmissione di una reversale
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Aggiorna stato reversale a INSERITO IN DISTINTA/NON INSERITO IN
	 * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di una
	 * reversale e' stata generata e il nuovo stato e' INSERITO IN DISTINTA o
	 * NON INSERITO IN DISTINTA Post: E' stato aggiornato lo stato della
	 * reversale
	 * 
	 * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di una reversale e' stata generata e il nuovo stato e'
	 * TRASMESSO e la reversale non e' ancora stata trasmessa Post: E' stato
	 * aggiornato lo stato della reversale e la sua data di trasmissione
	 * 
	 * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
	 * trasmissione di una reversale e' stata generata e il nuovo stato e'
	 * TRASMESSO e la reversale era già stata trasmessa Post: E' stato
	 * aggiornato lo stato della reversale e la sua data di ritrasmissione
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param docContabile
	 *            la reversale per cui aggiornare lo stato
	 * @param stato_trasmissione
	 *            il nuovo stato che la reversale dovrà assumere
	 */

	protected void aggiornaStatoReversale(UserContext userContext,
			V_mandato_reversaleBulk docContabile, String stato_trasmissione)
			throws OutdatedResourceException,
			it.cnr.jada.persistency.PersistencyException, ComponentException,
			BusyResourceException, javax.ejb.EJBException {
		ReversaleBulk reversale = (ReversaleBulk) getHome(userContext,
				ReversaleIBulk.class).findAndLock(
				new ReversaleIBulk(docContabile.getCd_cds(), docContabile
						.getEsercizio(), docContabile.getPg_documento_cont()));
		if ( reversale.getPg_ver_rec().compareTo(
		 docContabile.getPg_ver_rec()) != 0 )
		 throw new ApplicationException("Attenzione! La reversale " +
		 reversale.getPg_reversale() +
		 " non e' più valida perche' stata modificata." );
		if(reversale.isAnnullato())
			reversale.setStato_trasmissione_annullo(stato_trasmissione);
		else
			reversale.setStato_trasmissione(stato_trasmissione);

		// se si tratta di una prima trasmissione aggiorna la dt_trasmissione
		if (ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO.equals(reversale
				.getStato_trasmissione())){

			if(reversale.getDt_trasmissione() == null) {
			// Se la data di annullamento NON E' NULLA, e siamo in esercizio
			// successivo, metto
			// la data di trasmissione = ad istante successivo a quella di
			// annullamento
			if (DateServices.isAnnoMaggEsScriv(userContext)) {
				if (reversale.getDt_annullamento() != null) {
					reversale.setDt_trasmissione(DateServices.getNextMinTs(
							userContext, reversale.getDt_annullamento()));
				} else {
					reversale
							.setDt_trasmissione(DateServices
									.getMidDayTs(DateServices
											.getTs_valido(userContext)));
				}
			} else {
					reversale.setDt_trasmissione(DateServices
						.getTs_valido(userContext));
			}
			// se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
		} else if (reversale.getDt_trasmissione() != null) {
			if (ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO.equals(reversale
					.getStato_trasmissione_annullo()) || reversale
					.getStato_trasmissione_annullo() ==null){
			if (DateServices.isAnnoMaggEsScriv(userContext)) {

				if (reversale.getDt_annullamento() == null
						|| reversale.getDt_trasmissione().after(
								reversale.getDt_annullamento()))
					reversale.setDt_ritrasmissione(DateServices.getNextMinTs(
							userContext, reversale.getDt_trasmissione()));
				else
					reversale.setDt_ritrasmissione(DateServices.getNextMinTs(
							userContext, reversale.getDt_annullamento()));
			} else {
				reversale.setDt_ritrasmissione(DateServices
						.getTs_valido(userContext));
				}
			}else reversale.setDt_ritrasmissione( null);		
		}
		}
		reversale.setUser(userContext.getUser());
		updateBulk(userContext, reversale);

	}

	/**
	 * Calcola i totali storici dei doc.contabili trasmessi a cassiere
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Calcola totali storici Pre: Una richiesta di invio di una distinta
	 * al cassiere e' stata generata ed e pertanto necessario aggiornare lo
	 * storico dei totali Post: Nella distinta sono stati calcolati i totali
	 * storici (suddivisi per tipologia) sommando gli importi dei doc. contabili
	 * non annullati presenti in distinta
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la distinta da inviare al cassiere
	 * @return distinta la distinta coi totali storici aggiornati
	 */

	protected Distinta_cassiereBulk aggiornaStoricoTrasmessi(
			UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		try {
			calcolaTotali(userContext, distinta);
			calcolaTotaliStorici(userContext, distinta);
			/* aggiorno lo storico dei trasmessi */
			distinta
					.setIm_man_ini_pag(distinta
							.getTotStoricoMandatiPagamentoTrasmessi()
							.add(distinta.getTotMandatiPagamento())
							.subtract(
									distinta
											.getTotMandatiPagamentoAnnullatiRitrasmessi()));
			distinta
					.setIm_man_ini_sos(distinta
							.getTotStoricoMandatiRegSospesoTrasmessi()
							.add(distinta.getTotMandatiRegSospeso())
							.subtract(
									distinta
											.getTotMandatiRegSospesoAnnullatiRitrasmessi()));
			distinta
					.setIm_man_ini_acc(distinta
							.getTotStoricoMandatiAccreditamentoTrasmessi()
							.add(distinta.getTotMandatiAccreditamento())
							.subtract(
									distinta
											.getTotMandatiAccreditamentoAnnullatiRitrasmessi()));
			distinta
					.setIm_rev_ini_sos(distinta
							.getTotStoricoReversaliRegSospesoTrasmesse()
							.add(distinta.getTotReversaliRegSospesoBI())
							.subtract(
									distinta
											.getTotReversaliRegSospesoBIAnnullateRitrasmesse())
							.add(distinta.getTotReversaliRegSospesoCC())
							.subtract(
									distinta
											.getTotReversaliRegSospesoCCAnnullateRitrasmesse()));
			distinta
					.setIm_rev_ini_tra(distinta
							.getTotStoricoReversaliTrasferimentoTrasmesse()
							.add(distinta.getTotReversaliTrasferimento())
							.subtract(
									distinta
											.getTotReversaliTrasferimentoAnnullateRitrasmesse()));
			distinta
					.setIm_rev_ini_rit(distinta
							.getTotStoricoReversaliRitenuteTrasmesse()
							.add(distinta.getTotReversaliRitenute())
							.subtract(
									distinta
											.getTotReversaliRitenuteAnnullateRitrasmesse()));

			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * 
	 * Nome: Aggiungi mandati reversali collegati Pre: E' stata generata la
	 * richiesta di inserire in distinta un mandato/reversale Post: Per ogni
	 * mandato/reversale dipendente da quello da inserire in distinta viene
	 * creato automaticamente un altro dettaglio distinta e lo stato di tale
	 * doc.contabile viene aggiornato a INSERITO IN DISTINTA ( metodo
	 * 'inserisciDettaglioDistinta')
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk in cui inserire i doc. contabili
	 *            collegati
	 * @param docConatbile
	 *            il V_mandato_reversaleBulk per cui ricercare i doc. contabili
	 *            dièpendenti
	 * @param last_pg_dettaglio
	 *            , il progressivo dell'ultimo dettaglio inserito in distinta
	 * @return il progressivo aggiornato dell'ultimo dettaglio inserito in
	 *         distinta
	 */

	protected Long aggiungiMandatiEReversaliCollegati(UserContext userContext,
			Distinta_cassiereBulk distinta,
			V_mandato_reversaleBulk docContabilePadre, Long last_pg_dettaglio)
			throws PersistencyException, ComponentException {
		/*
		 * aggiungo in automatico i mandati già trasmessi e successivamente
		 * annullati
		 */
		Collection docContabili = ((V_mandato_reversaleHome) getHome(
				userContext, V_mandato_reversaleBulk.class))
				.findDocContabiliCollegati(docContabilePadre);
		V_mandato_reversaleBulk docContabile;
		for (Iterator i = docContabili.iterator(); i.hasNext();) {
			docContabile = (V_mandato_reversaleBulk) i.next();
			last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
					distinta, docContabile, last_pg_dettaglio);
			inserisciDettaglioDistinteCollegate(userContext, distinta,
					docContabile);
		}
		return last_pg_dettaglio;
	}

	/**
	 * 
	 * Nome: Aggiungi mandati reversali da ritrasmettere Pre: E' stata generata
	 * la richiesta di inviare una distinta al cassiere ed e' pertanto
	 * necessario inserire automaticamente in distinta tutti i mandati/reversali
	 * che sono stati annullati dopo che erano già stati inviati a cassiere
	 * Post: E' stato creato un nuovo dettaglio per ogni doc. contabile con data
	 * di annullamento successiva alla data di trasmissione e data di
	 * ritrasmissione non valorizzata.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk in cui inserire i doc. contabili da
	 *            ritrasmettere
	 * @throws RemoteException
	 * @throws EJBException
	 */

	protected void aggiungiMandatiEReversaliDaRitrasmettere(
			UserContext userContext, Distinta_cassiereBulk distinta)
			throws SQLException, ComponentException, PersistencyException,
			EJBException, RemoteException {
		Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
				userContext, Distinta_cassiere_detBulk.class))
				.getUltimoPg_Dettaglio(userContext, distinta);
		Collection docContabili = ((V_mandato_reversaleHome) getHome(
				userContext, V_mandato_reversaleBulk.class))
				.findDocContabiliAnnullatiDaRitrasmettere(distinta,
						annulliTuttaSac(userContext, distinta),tesoreriaUnica(userContext, distinta));
		V_mandato_reversaleBulk docContabile;
		for (Iterator i = docContabili.iterator(); i.hasNext();) {
			docContabile = (V_mandato_reversaleBulk) i.next();

			last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
					distinta, docContabile, last_pg_dettaglio);
			inserisciDettaglioDistinteCollegate(userContext, distinta,
					docContabile);

			/*
			 * non e' necessario aggiungere i collegati perch' se e' stato
			 * annullato il padre, automaticamente risultano annullati tutti i
			 * collegati last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
			 * userContext, distinta, docContabile, last_pg_dettaglio ) ;
			 */
		}
	}

	/**
	 * 
	 * Nome: Annulla modifiche dettagli Pre: E' stata generata la richiesta di
	 * annullare tutte le modifiche fatte dall'utente ai dettagli della distinta
	 * Post: La transazione viene riportata all'ultimo savepoint impostato
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk le cui modifiche ai dettagli devono
	 *            essere annullate
	 */

	public void annullaModificaDettagliDistinta(
			it.cnr.jada.UserContext userContext, Distinta_cassiereBulk dsitinta)
			throws it.cnr.jada.comp.ComponentException {
		try {
			rollbackToSavepoint(userContext, "DISTINTA_CASSIERE_DET");
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}

	/**
	 * Assegna il progressivo
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Assegna progressivo Pre: Una richiesta di creazione di una distinta
	 * e' stata generata ed e' pertanto necessario assegnarle il progressivo
	 * Post: Il progressivo viene calcolato incrementando di 1 l'ultimo
	 * progressivo presente nel db per l'esercizio e l'uo di scrivania;
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui generare il progressivo
	 * @param distinta
	 *            la distinta con il progressivo assegnato
	 */

	protected Distinta_cassiereBulk assegnaProgressivo(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		try {
			((Distinta_cassiereHome) getHome(userContext, distinta.getClass()))
					.inizializzaProgressivo(userContext, distinta);
			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Assegna il progressivo cassiere
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Assegna progressivo cassiere Pre: Una richiesta di inviare una
	 * distinta a cassiere e' stata generata ed e' pertanto necessario
	 * assegnarle il progressivo cassiere Post: Il progressivo cassiere viene
	 * calcolato incrementando di 1 l'ultimo progressivo cassiere presente per
	 * l'esercizio e l'uo di scrivania;
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui generare il progressivo
	 *            cassiere
	 * @param distinta
	 *            la distinta con il progressivo cassiere assegnato
	 */

	protected Distinta_cassiereBulk assegnaProgressivoCassiere(
			UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		try {
			((Distinta_cassiereHome) getHome(userContext, distinta.getClass()))
					.inizializzaProgressivoCassiere(userContext, distinta);
			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Inserisce in distinta tutti i mandati e tutte le reversali
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Inserisci tutti i documenti contabili Pre: Una richiesta di
	 * inserire in distinta tutti i mandati e le reversali visualizzati
	 * all'utente e' stata generata Post: Per ogni mandato/reversale
	 * visualizzato all'utente viene generato un dettaglio della distinta e lo
	 * stato trasmissione del mandato/reversale viene aggiornato a 'inserito in
	 * distinta' (metodo 'inserisciDettaglioDistinta'); se tale
	 * mandato/reversale ha associati altre reversali/mandati, vengono creati
	 * automaticamente dei dettagli di distinta anche per questi ed il loro
	 * stato trasmissione viene aggiornato (metodo
	 * 'aggiungiMandatiEReversaliCollegati').
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui generare i dettagkli
	 * @param docPassivo
	 *            un istanza di V_mandato_reversaleBulk contente i criteri di
	 *            ricerca specificati dall'utente nella selezione del
	 *            mandato/reversale
	 */

	public void associaTuttiDocContabili(UserContext userContext,
			Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docPassivo)
			throws ComponentException {

		try {
			SQLQuery sql = cercaMandatiEReversaliSQL(userContext, null,
					docPassivo, distinta);
			V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
					userContext, V_mandato_reversaleBulk.class);
			SQLBroker broker = home.createBroker(sql);

			Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
					userContext, Distinta_cassiere_detBulk.class))
					.getUltimoPg_Dettaglio(userContext, distinta);
			V_mandato_reversaleBulk docContabile;
			while (broker.next()) {
				docContabile = (V_mandato_reversaleBulk) broker
						.fetch(V_mandato_reversaleBulk.class);
				last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
						distinta, docContabile, last_pg_dettaglio);
				last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
						userContext, distinta, docContabile, last_pg_dettaglio);
				inserisciDettaglioDistinteCollegate(userContext, distinta,
						docContabile);
			}
			broker.close();

			/*
			 * String schema =
			 * it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema(); String
			 * user =
			 * it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
			 * java.sql.Timestamp dacr = new
			 * java.sql.Timestamp(System.currentTimeMillis());
			 * 
			 * java.sql.PreparedStatement ps =
			 * getConnection(userContext).prepareStatement(
			 * "INSERT INTO "+schema+
			 * "ASS_TIPO_LA_CDR ( CD_CENTRO_RESPONSABILITA, CD_TIPO_LINEA_ATTIVITA, UTCR, DUVA, UTUV, DACR, PG_VER_REC ) SELECT CD_CENTRO_RESPONSABILITA, ?, ?, ?, ?, ?, 1 FROM "
			 * +schema+
			 * "V_CDR_VALIDO WHERE ESERCIZIO = ? AND NOT EXISTS ( SELECT 1 FROM "
			 * +schema+
			 * "ASS_TIPO_LA_CDR WHERE V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA = ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA AND ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA = ? ) "
			 * );
			 * 
			 * ps.setString(1,dsitinta.getCd_tipo_linea_attivita()); //
			 * CD_TIPO_LINEA_ATTIVITA ps.setString(2,user); // UTCR
			 * ps.setTimestamp(3,dacr); // DUVA ps.setString(4,user); // UTUV
			 * ps.setTimestamp(5,dacr); // DACR
			 * ps.setInt(6,it.cnr.contab.utenze00
			 * .bp.CNRUserContext.getEsercizio(userContext).intValue()); //
			 * ESERCIZIO ps.setString(7,tipo_la.getCd_tipo_linea_attivita()); //
			 * CD_TIPO_LINEA_ATTIVITA LoggableStatement.execute(ps);
			 * try{ps.close();}catch( java.sql.SQLException e ){};
			 */
		} catch (Exception e) {
			throw handleException(e);
		}

	}

	/**
	 * Calcola i totali dei mandati/reversali inseriti in distinta
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Calcola totali Pre: Una richiesta di calcolare i totali dei mandati
	 * e delle reversali inseriti in distinta e' stata generata Post: I totali,
	 * distinti secondo le varie tipologie dei mandati e delle reversali, sono
	 * stati calcolati
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui calcolare i totali
	 * @return la Distinta_cassiereBulk con tutti i totali impostati
	 */

	public Distinta_cassiereBulk calcolaTotali(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			Distinta_cassiereHome distHome = (Distinta_cassiereHome) getHome(
					userContext, distinta.getClass());
			distinta.resetTotali();
			distinta.setTotMandatiAccreditamento(distHome
					.calcolaTotMandatiAccreditamento(distinta));
			distinta.setTotMandatiAccreditamentoAnnullati(distHome
					.calcolaTotMandatiAccreditamentoAnnullati(distinta));
			distinta
					.setTotMandatiAccreditamentoAnnullatiRitrasmessi(distHome
							.calcolaTotMandatiAccreditamentoAnnullatiRitrasmessi(distinta));
			distinta.setTotMandatiPagamento(distHome
					.calcolaTotMandatiPagamento(distinta));
			distinta.setTotMandatiPagamentoAnnullati(distHome
					.calcolaTotMandatiPagamentoAnnullati(distinta));
			distinta.setTotMandatiPagamentoAnnullatiRitrasmessi(distHome
					.calcolaTotMandatiPagamentoAnnullatiRitrasmessi(distinta));
			distinta.setTotMandatiRegSospeso(distHome
					.calcolaTotMandatiRegSospeso(distinta));
			distinta.setTotMandatiRegSospesoAnnullati(distHome
					.calcolaTotMandatiRegSospesoAnnullati(distinta));
			distinta.setTotMandatiRegSospesoAnnullatiRitrasmessi(distHome
					.calcolaTotMandatiRegSospesoAnnullatiRitrasmessi(distinta));

			distinta = distHome.calcolaTotReversaliRegSospeso(distinta);
			/*
			 * distinta.setTotReversaliRegSospesoBI(
			 * distHome.calcolaTotReversaliRegSospesoBI( distinta ));
			 * distinta.setTotReversaliRegSospesoBIAnnullate(
			 * distHome.calcolaTotReversaliRegSospesoBIAnnullate( distinta ));
			 * distinta.setTotReversaliRegSospesoBIAnnullateRitrasmesse(
			 * distHome.calcolaTotReversaliRegSospesoBIAnnullateRitrasmesse(
			 * distinta )); distinta.setTotReversaliRegSospesoCC(
			 * distHome.calcolaTotReversaliRegSospesoCC( distinta ));
			 * distinta.setTotReversaliRegSospesoCCAnnullate(
			 * distHome.calcolaTotReversaliRegSospesoCCAnnullate( distinta ));
			 * distinta.setTotReversaliRegSospesoCCAnnullateRitrasmesse(
			 * distHome.calcolaTotReversaliRegSospesoCCAnnullateRitrasmesse(
			 * distinta ));
			 */

			distinta.setTotReversaliTrasferimento(distHome
					.calcolaTotReversaliTrasferimento(distinta));
			distinta.setTotReversaliTrasferimentoAnnullate(distHome
					.calcolaTotReversaliTrasferimentoAnnullate(distinta));
			distinta
					.setTotReversaliTrasferimentoAnnullateRitrasmesse(distHome
							.calcolaTotReversaliTrasferimentoAnnullateRitrasmesse(distinta));
			distinta.setTotReversaliRitenute(distHome
					.calcolaTotReversaliRitenute(distinta));
			distinta.setTotReversaliRitenuteAnnullate(distHome
					.calcolaTotReversaliRitenuteAnnullate(distinta));
			distinta.setTotReversaliRitenuteAnnullateRitrasmesse(distHome
					.calcolaTotReversaliRitenuteAnnullateRitrasmesse(distinta));

			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Calcola i totali storici degli importi dei mandati/reversali trasmesse al
	 * cassiere
	 * 
	 * Nome: storico senza distinte trasmesse Pre: E' stata generata la
	 * richiesta di calcolare i totali storici degli importi dei
	 * mandati/reversali trasmessi al cassiere e nessuna distinta e' ancora
	 * stata trasmessa al cassiere Post: Tutti i totali vengono inizializzati al
	 * valore 0.
	 * 
	 * Nome: storico con distinte trasmesse Pre: E' stata generata la richiesta
	 * di calcolare i totali storici degli importi dei mandati/reversali
	 * trasmessi al cassiere e esistono distinte già trasmessa al cassiere Post:
	 * Tutti i totali di ogni singola tipologia di doc. contabile vengono
	 * inizializzati col valore impostato nell'ultima distinta trasmessa. I
	 * totali complessivi dei mandati trasmessi vengono calcolati come somma dei
	 * totali dei mandati di tipo 'Accreditamento', 'Regolamento Sospeso',
	 * 'Pagamento'. I totali complessivi delle reversali trasmesse vengono
	 * calcolati come somma dei totali delle reversali di tipo 'Accreditamento',
	 * 'Regolamento Sospeso'. Inoltre vengono ricercati se esistono
	 * mandati/reversali annullati dopo il trasferimento a cassiere della
	 * distinta in cui erano stati inseriti: gli importi di tali doc. contabili
	 * vengono sommati nei tot. mandati/reversali da ritrasmettere.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            l'istanza di Distinta_cassiereBulk per cui calolare lo storico
	 * @return la distinta coi totali storici valorizzati;
	 */

	protected Distinta_cassiereBulk calcolaTotaliStorici(
			UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		try {
			Distinta_cassiereBulk ultimaDistinta = ((Distinta_cassiereHome) getHome(
					userContext, distinta.getClass()))
					.findUltimaDistintaTrasmessa(distinta);
			if (ultimaDistinta == null) {
				distinta.setTotStoricoMandatiPagamentoTrasmessi(new BigDecimal(
						0));
				distinta
						.setTotStoricoMandatiRegSospesoTrasmessi(new BigDecimal(
								0));
				distinta
						.setTotStoricoMandatiAccreditamentoTrasmessi(new BigDecimal(
								0));
				distinta.setTotStoricoMandatiTrasmessi(new BigDecimal(0));
				distinta
						.setTotStoricoReversaliRegSospesoTrasmesse(new BigDecimal(
								0));
				distinta
						.setTotStoricoReversaliTrasferimentoTrasmesse(new BigDecimal(
								0));
				distinta
						.setTotStoricoReversaliRitenuteTrasmesse(new BigDecimal(
								0));
				distinta.setTotStoricoReversaliTrasmesse(new BigDecimal(0));
			} else {
				distinta.setTotStoricoMandatiPagamentoTrasmessi(ultimaDistinta
						.getIm_man_ini_pag());
				distinta.setTotStoricoMandatiRegSospesoTrasmessi(ultimaDistinta
						.getIm_man_ini_sos());
				distinta
						.setTotStoricoMandatiAccreditamentoTrasmessi(ultimaDistinta
								.getIm_man_ini_acc());
				distinta
						.setTotStoricoMandatiTrasmessi(distinta
								.getTotStoricoMandatiAccreditamentoTrasmessi()
								.add(
										distinta
												.getTotStoricoMandatiRegSospesoTrasmessi())
								.add(
										distinta
												.getTotStoricoMandatiPagamentoTrasmessi()));
				distinta
						.setTotStoricoReversaliRegSospesoTrasmesse(ultimaDistinta
								.getIm_rev_ini_sos());
				distinta
						.setTotStoricoReversaliTrasferimentoTrasmesse(ultimaDistinta
								.getIm_rev_ini_tra());
				distinta.setTotStoricoReversaliRitenuteTrasmesse(ultimaDistinta
						.getIm_rev_ini_rit());
				distinta
						.setTotStoricoReversaliTrasmesse(distinta
								.getTotStoricoReversaliRegSospesoTrasmesse()
								.add(
										distinta
												.getTotStoricoReversaliTrasferimentoTrasmesse())
								.add(
										distinta
												.getTotStoricoReversaliRitenuteTrasmesse()));
			}

			// calcolo i totali dei mandati/reversali da ritrasmettere
			V_mandato_reversaleBulk docContabile;
			distinta
					.setTotStoricoMandatiDaRitrasmettere(new java.math.BigDecimal(
							0));
			distinta
					.setTotStoricoReversaliDaRitrasmettere(new java.math.BigDecimal(
							0));
			for (Iterator i = ((V_mandato_reversaleHome) getHome(userContext,
					V_mandato_reversaleBulk.class))
					.findDocContabiliAnnullatiDaRitrasmettere(distinta,
							annulliTuttaSac(userContext, distinta),tesoreriaUnica(userContext, distinta)).iterator(); i
					.hasNext();) {
				docContabile = (V_mandato_reversaleBulk) i.next();
				if (docContabile.isMandato())
					distinta.setTotStoricoMandatiDaRitrasmettere(distinta
							.getTotStoricoMandatiDaRitrasmettere().add(
									docContabile.getIm_documento_cont()));
				else if (docContabile.isReversale())
					distinta.setTotStoricoReversaliDaRitrasmettere(distinta
							.getTotStoricoReversaliDaRitrasmettere().add(
									docContabile.getIm_documento_cont()));

			}

			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}

	}

	/**
	 * Richiama la procedura che processa il file selezionato dall'utente
	 * PreCondition: E' stata generata la richiesta di processare un file
	 * selezionato dall'utente. PostCondition: Viene richiamata la procedura di
	 * Processo dei File
	 * 
	 * @param userContext
	 *            lo <code>UserContext</code> che ha generato la richiesta.
	 * @param file
	 *            il <code>V_ext_cassiere00Bulk</code> file da processare.
	 **/
	private void callCheckDocContForDistinta(UserContext userContext,
			Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{ call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB750.checkDocContForDistCas(?,?,?,?) }", false,
					this.getClass());

			cs.setString(1, distinta.getCd_cds());
			cs.setInt(2, distinta.getEsercizio().intValue());
			cs.setString(3, distinta.getCd_unita_organizzativa());
			cs.setLong(4, distinta.getPg_distinta().longValue());

			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}

	/**
	 * Richiama la procedura che processa il file selezionato dall'utente
	 * PreCondition: E' stata generata la richiesta di processare un file
	 * selezionato dall'utente. PostCondition: Viene richiamata la procedura di
	 * Processo dei File
	 * 
	 * @param userContext
	 *            lo <code>UserContext</code> che ha generato la richiesta.
	 * @param file
	 *            il <code>V_ext_cassiere00Bulk</code> file da processare.
	 **/
	private void callProcessaFile(UserContext userContext,
			V_ext_cassiere00Bulk file)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{ call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB750.processaInterfaccia(?,?,?) }", false, this
					.getClass());
			cs.setInt(1, file.getEsercizio().intValue());
			cs.setString(2, file.getNome_file());
			cs.setString(3, it.cnr.contab.utenze00.bp.CNRUserContext
					.getUser(userContext));
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}

	/**
	 * 
	 * Nome: Carica i logs relativi al file selezionato. Pre: E' stata richiesto
	 * di inizializzare un oggetto <code>V_ext_cassiere00Bulk</code> per la
	 * visualizzazione dei Log relativi ai processi effettuati sul File
	 * corrispondente. Post: Viene caricata la lista di processi relativi al
	 * File specificato.
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param file
	 *            <code>V_ext_cassiere00Bulk</code> l'oggetto da inizializzare
	 * 
	 * @return <code>V_ext_cassiere00Bulk<code> l'oggetto inizializzato
	 * 
	 * 
	 */
	public it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk caricaLogs(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk file)
			throws it.cnr.jada.comp.ComponentException {

		SQLBuilder sql = getHome(userContext, Ext_cassiere00_logsBulk.class)
				.createSQLBuilder();

		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, file.getEsercizio());
		sql.addSQLClause("AND", "NOME_FILE", sql.EQUALS, file.getNome_file());

		try {
			List risultato = getHome(userContext, Ext_cassiere00_logsBulk.class)
					.fetchAll(sql);
			if (risultato.size() > 0) {
				file.setLogs(new SimpleBulkList(risultato));
			}
		} catch (PersistencyException pe) {
			throw new ComponentException(pe);
		}

		return file;
	}

	/**
	 * 
	 * Nome: Cerca File Cassiere. Pre: E' stata richiesto di visualizzare tutti
	 * i File Ccassiere caricati.
	 * 
	 * Post: Viene restituito un RemoteIterator con il risultato della ricerca.
	 * 
	 * @param userContext
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * 
	 * @return il <code>RemoteIterator</code> della lista dei File Cassiere
	 * 
	 * 
	 */
	public it.cnr.jada.util.RemoteIterator cercaFile_Cassiere(
			it.cnr.jada.UserContext userContext, CompoundFindClause user_clauses)
			throws it.cnr.jada.comp.ComponentException {

		SQLBuilder sql = getHome(userContext, V_ext_cassiere00Bulk.class)
				.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,((CNRUserContext) userContext).getEsercizio()); 
		if (user_clauses != null)
			sql.addClause(user_clauses);

		return iterator(userContext, sql, V_ext_cassiere00Bulk.class, "default");

	}

	/**
	 * Esegue una operazione di ricerca di Mandati/Reversali
	 * 
	 * Nome: Cerca mandati e reversali Pre: E' necessario ricercare i mandati e
	 * le reversali da cui selezionare quelli da includere in distinta Post:
	 * Viene creato un RemoteIterator passandogli le clausole presenti nel
	 * SQLBuilder creato dal metodo 'cercaMandatiEReversaliSQL'
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param clausole
	 *            le clausole specificate dall'utente
	 * @param docPassivo
	 *            l'istanza di V_mandato_reversaleBulk con le impostazioni
	 *            specificate dall'utente
	 * @param distinta
	 *            l'istanza di Distinta_cassiereBulk per cui ricercare i
	 *            mandati/reversali
	 * @return RemoteIterator con le istanze di V_mandato_reversaleBulk
	 */
	public it.cnr.jada.util.RemoteIterator cercaMandatiEReversali(
			UserContext userContext,
			it.cnr.jada.persistency.sql.CompoundFindClause clausole,
			V_mandato_reversaleBulk docPassivo, Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {
		try {

			SQLQuery sql = cercaMandatiEReversaliSQL(userContext, clausole,
					(V_mandato_reversaleBulk) docPassivo, distinta);
			return iterator(userContext, sql, V_mandato_reversaleBulk.class,
					null);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Esegue una operazione di creazione del SQLBuilder per ricercare
	 * Mandati/Reversali
	 * 
	 * Nome: Cerca mandati e reversali Pre: E' necessario creare il SQLBuilder
	 * per ricercare i mandati e le reversali da cui selezionare quelli da
	 * includere in distinta Post: Viene generato il SQLBuilder con le clausole
	 * specificate dall'utente ed inoltre le clausole che il cds di appartenza
	 * sia uguale al cds di scrivania, lo stato di trasmissione sia NON INSERITO
	 * IN DISTINTA, il tipo doc. contabile sia diverso da REGOLARIZZAZIONE, e il
	 * doc. contabile non dipenda da altri doc. contabili.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param clausole
	 *            le clausole specificate dall'utente
	 * @param docPassivo
	 *            l'istanza di V_mandato_reversaleBulk con le impostazioni
	 *            specificate dall'utente
	 * @param distinta
	 *            l'istanza di Distinta_cassiereBulk per cui ricercare i
	 *            mandati/reversali
	 * @return SQLBuilder con tutte le clausole
	 */
	private SQLQuery cercaMandatiEReversaliSQL(UserContext userContext,
			it.cnr.jada.persistency.sql.CompoundFindClause clausole,
			V_mandato_reversaleBulk docPassivo, Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {
		try {
			if (distinta.getFl_flusso()){

				SQLBuilder sql = getHome(userContext,
						V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
				sql.addClause(clausole);
				sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				// Da condizionare 02/12/2015
				if(!tesoreriaUnica(userContext, distinta)){
					sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
					sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
							MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
				}
				else{
					sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.dt_firma", SQLBuilder.ISNOTNULL,null);
					sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
					MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
				}
				sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont", SQLBuilder.NOT_EQUALS,
						MandatoBulk.TIPO_REGOLARIZZAZIONE);
				sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato", SQLBuilder.NOT_EQUALS,
						MandatoBulk.STATO_MANDATO_ANNULLATO);
				if (isInserisciMandatiVersamentoCori(userContext)) {
					sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.versamento_cori = 'N'");
				}
				sql.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
				sql.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT_PADRE","V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");
								if (Utility.createParametriCnrComponentSession().getParametriCnr(
						userContext, docPassivo.getEsercizio()).getFl_siope()
						.booleanValue()) {
					Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
							userContext, Unita_organizzativa_enteBulk.class)
							.findAll().get(0);
					if (!((CNRUserContext) userContext).getCd_cds().equals(
							ente.getUnita_padre().getCd_unita_organizzativa()))
						sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont",
								SQLBuilder.NOT_EQUALS,
								MandatoBulk.TIPO_ACCREDITAMENTO);
				}

				if (docPassivo != null) // (1) clausole sull'esercizio,
										// cd_unita_organizzativa + clausole
										// dell'utente
					sql.addClause(docPassivo.buildFindClauses(null));
				// sql.addOrderBy(
				// "cd_tipo_documento_cont, ti_documento_cont, pg_documento_cont" );

				SQLUnion union;

				// MARIO - condizione che aggiunge in ogni caso, a prescindere dalla
				// selezione effettuata
				// i mandati di versamento CORI/IVA, ma solo se i parametri sono
				// impostati in tal senso
				if (isInserisciMandatiVersamentoCori(userContext)) {
					SQLBuilder sql2 = getHome(userContext,
							V_mandato_reversaleBulk.class,
							"V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
					sql2.addClause(clausole);
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.esercizio", SQLBuilder.EQUALS,
							((CNRUserContext) userContext).getEsercizio());
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_unita_organizzativa",
							SQLBuilder.EQUALS, docPassivo
									.getCd_unita_organizzativa());
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_tipo_documento_cont",
							SQLBuilder.EQUALS, "MAN");

					// Da condizionare 02/12/2015
					if(!tesoreriaUnica(userContext, distinta)){
						sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
						sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
								MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
					}
					else{
						sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.dt_firma", SQLBuilder.ISNOTNULL,null);
						sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
					}				
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont",
							SQLBuilder.NOT_EQUALS,
							MandatoBulk.TIPO_REGOLARIZZAZIONE);
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato", SQLBuilder.NOT_EQUALS,
							MandatoBulk.STATO_MANDATO_ANNULLATO);
					sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.versamento_cori = 'S'");
					sql2.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
					sql2.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT_PADRE","V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");
					
					union = sql2.union(sql, true);
					return union;
				} else {
					return sql;
				}
			}else if (distinta.getFl_sepa()){  //no flusso
				SQLBuilder sql = getHome(userContext,
						V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DIST_SEPA").createSQLBuilder();
				sql.addClause(clausole);
				sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				// Da condizionare 02/12/2015
				if(!tesoreriaUnica(userContext, distinta)){
					sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
					sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato_trasmissione", SQLBuilder.EQUALS,
							MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
				}
				else{
					sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.dt_firma", SQLBuilder.ISNOTNULL,null);
					sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato_trasmissione", SQLBuilder.EQUALS,
					MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
				}
				sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.ti_documento_cont", SQLBuilder.NOT_EQUALS,
						MandatoBulk.TIPO_REGOLARIZZAZIONE);
				sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato", SQLBuilder.NOT_EQUALS,
						MandatoBulk.STATO_MANDATO_ANNULLATO);
				
				sql.addSQLJoin("v_mandato_reversale_dist_sepa.CD_TIPO_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_sepa.CD_TIPO_DOCUMENTO_CONT");
				sql.addSQLJoin("v_mandato_reversale_dist_sepa.PG_DOCUMENTO_CONT_PADRE","v_mandato_reversale_dist_sepa.PG_DOCUMENTO_CONT");
								if (Utility.createParametriCnrComponentSession().getParametriCnr(
						userContext, docPassivo.getEsercizio()).getFl_siope()
						.booleanValue()) {
					Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
							userContext, Unita_organizzativa_enteBulk.class)
							.findAll().get(0);
					if (!((CNRUserContext) userContext).getCd_cds().equals(
							ente.getUnita_padre().getCd_unita_organizzativa()))
						sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.ti_documento_cont",
								SQLBuilder.NOT_EQUALS,
								MandatoBulk.TIPO_ACCREDITAMENTO);
				}
				if (docPassivo != null) // (1) clausole sull'esercizio,
										// cd_unita_organizzativa + clausole
										// dell'utente
					sql.addClause(docPassivo.buildFindClauses(null));
					return sql;
				}
		 else if (distinta.getFl_annulli()){  //annulli
			SQLBuilder sql = getHome(userContext,
					V_mandato_reversaleBulk.class,
					"V_MANDATO_REVERSALE_DIST_ANN").createSQLBuilder();
			sql.addClause(clausole);
			sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.esercizio", SQLBuilder.EQUALS,
					((CNRUserContext) userContext).getEsercizio());
			// Da condizionare 02/12/2015
			if(!tesoreriaUnica(userContext, distinta)){
				sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
				sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.stato_trasmissione", SQLBuilder.EQUALS,
						MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
			}
			else{
				sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.dt_firma", SQLBuilder.ISNOTNULL,null);
				sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.stato_trasmissione", SQLBuilder.EQUALS,
				MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
			}
			sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.ti_documento_cont", SQLBuilder.NOT_EQUALS,
					MandatoBulk.TIPO_REGOLARIZZAZIONE);
//			sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato", SQLBuilder.NOT_EQUALS,
//					MandatoBulk.STATO_MANDATO_ANNULLATO);
//			
			sql.addSQLJoin("v_mandato_reversale_dist_ann.CD_TIPO_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_ann.CD_TIPO_DOCUMENTO_CONT");
			sql.addSQLJoin("v_mandato_reversale_dist_ann.PG_DOCUMENTO_CONT_PADRE","v_mandato_reversale_dist_ann.PG_DOCUMENTO_CONT");
							if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext, docPassivo.getEsercizio()).getFl_siope()
					.booleanValue()) {
				Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
						userContext, Unita_organizzativa_enteBulk.class)
						.findAll().get(0);
				if (!((CNRUserContext) userContext).getCd_cds().equals(
						ente.getUnita_padre().getCd_unita_organizzativa()))
					sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.ti_documento_cont",
							SQLBuilder.NOT_EQUALS,
							MandatoBulk.TIPO_ACCREDITAMENTO);
			}
			if (docPassivo != null) // (1) clausole sull'esercizio,
									// cd_unita_organizzativa + clausole
									// dell'utente
				sql.addClause(docPassivo.buildFindClauses(null));
				return sql;
			}
			else  //no flusso e no sepa e no annulli
			{
			SQLBuilder sql = getHome(userContext,
					V_mandato_reversaleBulk.class,
					"V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
			sql.addClause(clausole);
			sql.addSQLClause("AND", "v_mandato_reversale_distinta.esercizio", SQLBuilder.EQUALS,
					((CNRUserContext) userContext).getEsercizio());
			// Da condizionare 02/12/2015
			if(!tesoreriaUnica(userContext, distinta)){
				sql.addSQLClause("AND", "v_mandato_reversale_distinta.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
				sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
						MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
			}
			else{
				sql.addSQLClause("AND", "v_mandato_reversale_distinta.dt_firma", SQLBuilder.ISNOTNULL,null);
				sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
						MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
				
				SQLBuilder sql2 = getHome(userContext,V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DIST_SEPA").createSQLBuilder();
				sql2.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_SEPA.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
						"V_MANDATO_REVERSALE_DIST_SEPA.ESERCIZIO");
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
						"V_MANDATO_REVERSALE_DIST_SEPA.CD_CDS");
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_SEPA.CD_TIPO_DOCUMENTO_CONT");
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_SEPA.PG_DOCUMENTO_CONT");
				sql.addSQLNotExistsClause("AND", sql2);
				
				SQLBuilder sql3 = getHome(userContext,V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
				sql3.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_XML.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
						"V_MANDATO_REVERSALE_DIST_XML.ESERCIZIO");
				sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
						"V_MANDATO_REVERSALE_DIST_XML.CD_CDS");
				sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
				sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");
				sql.addSQLNotExistsClause("AND", sql3);
				
				SQLBuilder sql4 = getHome(userContext,V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DIST_ANN").createSQLBuilder();
				sql4.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_ANN.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
						"V_MANDATO_REVERSALE_DIST_ANN.ESERCIZIO");
				sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
						"V_MANDATO_REVERSALE_DIST_ANN.CD_CDS");
				sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_ANN.CD_TIPO_DOCUMENTO_CONT");
				sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
						"V_MANDATO_REVERSALE_DIST_ANN.PG_DOCUMENTO_CONT");
				sql.addSQLNotExistsClause("AND", sql4);
			}
			sql.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont", SQLBuilder.NOT_EQUALS,
					MandatoBulk.TIPO_REGOLARIZZAZIONE);
			sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato", SQLBuilder.NOT_EQUALS,
					MandatoBulk.STATO_MANDATO_ANNULLATO);
			if (isInserisciMandatiVersamentoCori(userContext)) {
				sql.addSQLClause("AND", "v_mandato_reversale_distinta.versamento_cori = 'N'");
			}
			sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT");
			sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE","V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT");
			
			if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext, docPassivo.getEsercizio()).getFl_siope()
					.booleanValue()) {
				Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
						userContext, Unita_organizzativa_enteBulk.class)
						.findAll().get(0);
				if (!((CNRUserContext) userContext).getCd_cds().equals(
						ente.getUnita_padre().getCd_unita_organizzativa()))
					sql.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont",
							SQLBuilder.NOT_EQUALS,
							MandatoBulk.TIPO_ACCREDITAMENTO);
			}

			if (docPassivo != null) // (1) clausole sull'esercizio,
									// cd_unita_organizzativa + clausole
									// dell'utente
				sql.addClause(docPassivo.buildFindClauses(null));
			// sql.addOrderBy(
			// "cd_tipo_documento_cont, ti_documento_cont, pg_documento_cont" );

			SQLUnion union;

			// MARIO - condizione che aggiunge in ogni caso, a prescindere dalla
			// selezione effettuata
			// i mandati di versamento CORI/IVA, ma solo se i parametri sono
			// impostati in tal senso
			if (isInserisciMandatiVersamentoCori(userContext)) {
				SQLBuilder sql2 = getHome(userContext,
						V_mandato_reversaleBulk.class,
						"V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
				sql2.addClause(clausole);
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.esercizio", SQLBuilder.EQUALS,
						((CNRUserContext) userContext).getEsercizio());
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_unita_organizzativa",
						SQLBuilder.EQUALS, docPassivo
								.getCd_unita_organizzativa());
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_tipo_documento_cont",
						SQLBuilder.EQUALS, "MAN");

				// Da condizionare 02/12/2015
				if(!tesoreriaUnica(userContext, distinta)){
					sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_cds", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_cds());
					sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
							MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
				}
				else{
					sql2.addSQLClause("AND", "v_mandato_reversale_distinta.dt_firma", SQLBuilder.ISNOTNULL,null);
					sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
							MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
				}				
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont",
						SQLBuilder.NOT_EQUALS,
						MandatoBulk.TIPO_REGOLARIZZAZIONE);
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato", SQLBuilder.NOT_EQUALS,
						MandatoBulk.STATO_MANDATO_ANNULLATO);
				sql2.addSQLClause("AND", "v_mandato_reversale_distinta.versamento_cori = 'S'");
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT");
				sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE","V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT");
				union = sql2.union(sql, true);
				return union;
			} else {
				return sql;
			}
			}
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Elimina un dettaglio di distinta
	 * 
	 * Nome: elimina dettaglio Pre: E' stata generata la richiesta di eliminare
	 * un dettaglio di distinta relativo ad un documento contabile (mandato o
	 * reversale) Post: Il dettaglio e' stato cancellato e lo stato_trasmissione
	 * del doc. contabile associato a tale dettaglio viene aggiornato a NON
	 * INSERITO IN DISTINTA
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui cancellare il dettaglio
	 * @param docContabile
	 *            il mandato/reversale da cancellare dalla distinta
	 */
	public void eliminaDettaglioDistinta(it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
			throws ComponentException {
		try {
			if(tesoreriaUnica(userContext, distinta))
				aggiornaStatoDocContabile(userContext, docContabile,
						MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
			else
				aggiornaStatoDocContabile(userContext, docContabile,
					MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
			String schema = it.cnr.jada.util.ejb.EJBCommonServices
					.getDefaultSchema();
			LoggableStatement ps = null;
			if (docContabile.isMandato()) {
				ps = new LoggableStatement(getConnection(userContext),
						"DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
								+ "WHERE ESERCIZIO = ? AND "
								+ "CD_CDS = ? AND "
								+ "CD_UNITA_ORGANIZZATIVA = ? AND "
								+ "PG_DISTINTA = ? AND " + "PG_MANDATO = ? ",
						true, this.getClass());
			} else if (docContabile.isReversale()) {
				ps = new LoggableStatement(getConnection(userContext),
						"DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
								+ "WHERE ESERCIZIO = ? AND "
								+ "CD_CDS = ? AND "
								+ "CD_UNITA_ORGANIZZATIVA = ? AND "
								+ "PG_DISTINTA = ? AND " + "PG_REVERSALE = ? ",
						true, this.getClass());
			}
			try {
				ps.setObject(1, distinta.getEsercizio());
				ps.setString(2, distinta.getCd_cds());
				ps.setString(3, distinta.getCd_unita_organizzativa());
				ps.setObject(4, distinta.getPg_distinta());
				ps.setObject(5, docContabile.getPg_documento_cont());
				ps.execute();
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}

		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Elimina un insieme di dettagli dalla distinta
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Elimina dettagli Pre: Una richiesta di eliminare alcuni dettagli da
	 * una distinta non ancora trasmessa al cassiere e' stata generata Post:
	 * Ogni dettaglio per cui e' stata richiesta la cancellazione viene
	 * eliminato dalla distinta e lo stato trasmissione del documento contabile
	 * ad esso associato viene aggiornato a NON INSERITO IN DISTINTA; se tale
	 * documento aveva altri documenti contabili collegati anche questi
	 * documenti vengono eliminati dalla distinta ed il loro stato trasmissione
	 * aggiornato.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param docContabili
	 *            l'array di V_mandato_reversaleBulk da cancellare dalla
	 *            distinta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui eliminare i dettagli
	 */

	public void eliminaDistinta_cassiere_detCollConBulk(
			it.cnr.jada.UserContext userContext,
			it.cnr.jada.bulk.OggettoBulk[] docContabili,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			for (int i = 0; i < docContabili.length; i++) {
				V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) docContabili[i];
				// aggiornaStatoDocContabile( userContext, docContabile,
				// MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO );
				eliminaDettaglioDistinta(userContext, distinta, docContabile);
				eliminaMandatiEReversaliCollegati(userContext, distinta,
						docContabile);
				eliminaDettaglioDistinteCollegate(userContext, distinta,
						docContabile);
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Elimina tutti i dettagli di una distinta
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Elimina tutti dettagli Pre: Una richiesta di eliminare una distinta
	 * non ancora trasmessa al cassiere e' stata generata ed e' pertanto
	 * necessario eliminare tutti i suoi dettagli Post: Ogni dettaglio della
	 * distinta viene eliminato e lo stato trasmissione del documento contabile
	 * ad esso associato viene aggiornato a NON INSERITO IN DISTINTA
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui eliminare tutti i dettagli
	 */
	public void eliminaDistinta_cassiere_detCollConBulk(
			it.cnr.jada.UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		try {
			eliminaTuttiDettagliDistinteCollegate(userContext, distinta);
			eliminaTuttiDettagliDistinta(userContext, distinta);
			distinta.resetTotali();
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Nome: Elimina Doc.Contabile Padre Pre: E' stata richiesta la
	 * cancellazione di un dettaglio di una distinta relativo ad un doc.
	 * contabile da cui dipendono altri doc. contabili; Post: Per tutti i doc.
	 * contabili figli di quello da cancellare viene richiamato il metodo
	 * 'eliminaDettaglioDistinta' che procede all'eliminazione del dettaglio
	 * dalla distinta e all'aggiornamento dello stato_trasmissione del doc.
	 * contabile collegato
	 * 
	 * Nome: Elimina Doc.Contabile Figlio Pre: E' stata richiesta la
	 * cancellazione di un dettaglio di una distinta relativo ad un doc.
	 * contabile che dipende da un altro doc. contabile; Post: Viene recuperato
	 * il doc. conatabile padre rispetto a quello da cancellare e tutti i suoi
	 * figli e per ognuno di questi documenti viene richiamato il metodo
	 * 'eliminaDettaglioDistinta' che procede all'eliminazione del dettaglio
	 * dalla distinta e all'aggiornamento dello stato_trasmissione del doc.
	 * contabile collegato
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta i cui dettagli sono da
	 *            cancellare
	 * @param docContabile
	 *            <code>V_mandato_reversaleBulk</code> il doc.contabile per cui
	 *            ricercare i doc. contabili collegati
	 * 
	 * 
	 */

	protected void eliminaMandatiEReversaliCollegati(UserContext userContext,
			Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
			throws PersistencyException, ComponentException {
		Collection docContabili;
		V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
				userContext, V_mandato_reversaleBulk.class);
		V_mandato_reversaleBulk docContabilePadre;

		if (docContabile.getPg_documento_cont_padre().compareTo(
				docContabile.getPg_documento_cont()) == 0)
			// si tratta di un padre, seleziona tutti i figli
			docContabili = home.findDocContabiliCollegati(docContabile);
		else // si tratta di un figlio, seleziona il padre + tutti gli altri
				// figli
		{
			docContabilePadre = home.findDocContabilePadre(docContabile);
			docContabili = home.findDocContabiliCollegatiEccetto(docContabile);
			docContabili.add(docContabilePadre);
		}
		for (Iterator i = docContabili.iterator(); i.hasNext();) {
			V_mandato_reversaleBulk vManRev = (V_mandato_reversaleBulk) i
					.next();
			eliminaDettaglioDistinta(userContext, distinta, vManRev);
			eliminaDettaglioDistinteCollegate(userContext, distinta, vManRev);
		}
	}

	/**
	 * cancellazione dettagli distinta PreCondition: E' stata richiesta la
	 * cancellazione di tutti i dettagli di una istanza di Distinta_cassiereBulk
	 * PostCondition: Lo stato_trasmissione dei doc.contabili associati ai
	 * dettagli della distinta viene aggiornato a NON INSERITO IN DISTINTA
	 * (metodo 'aggiornaStatoDocContabili'). Tutti i dettagli della distinta
	 * vengono fisicamente rimossi dal database
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta i cui dettagli sono da
	 *            cancellare
	 * 
	 * 
	 * 
	 */
	public void eliminaTuttiDettagliDistinta(
			it.cnr.jada.UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		try {
			if(tesoreriaUnica(userContext, distinta))
				aggiornaStatoDocContabili(userContext, distinta,
						MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
			else
				aggiornaStatoDocContabili(userContext, distinta,
						MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
			String schema = it.cnr.jada.util.ejb.EJBCommonServices
					.getDefaultSchema();
			LoggableStatement ps = null;
			ps = new LoggableStatement(getConnection(userContext),
					"DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
							+ "WHERE ESERCIZIO = ? AND " + "CD_CDS = ? AND "
							+ "CD_UNITA_ORGANIZZATIVA = ? AND "
							+ "PG_DISTINTA = ? ", true, this.getClass());
			try {
				ps.setObject(1, distinta.getEsercizio());
				ps.setString(2, distinta.getCd_cds());
				ps.setString(3, distinta.getCd_unita_organizzativa());
				ps.setObject(4, distinta.getPg_distinta());
				ps.executeUpdate();
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Nome: Creazione di una Distinta_cassiereBulk Pre: E' stata richiesta la
	 * creazione di una istanza di Distinta_cassiereBulk Post: La distinta, che
	 * era già stata in precedenza inserita nel database ( metodo
	 * 'inizializzaBulkPerInserimento'), viene aggiornata.
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da cancellare
	 * 
	 * 
	 * 
	 */

	protected OggettoBulk eseguiCreaConBulk(UserContext userContext,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		bulk.setCrudStatus(bulk.TO_BE_UPDATED);
		return modificaConBulk(userContext, bulk);
	}

	/**
	 * Cancellazione di una istanza di Distinta_cassiereBulk
	 * 
	 * Nome: Esegui cancellazione Pre: E' stata richiesta la cancellazione di
	 * una istanza di Distinta_cassiereBulk che non e' ancora stata inviata al
	 * cassiere Post: Tutti i dettagli della distinta sono stati eliminati e lo
	 * stato dei doc. contabili inseriti nella distinta viene riportato a NON
	 * INSERITO IN DISTINTA (metodo 'eliminaTuttiDettagliDistinta'). La distinta
	 * viene cancellata
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da cancellare
	 * 
	 * 
	 * 
	 */

	protected void eseguiEliminaConBulk(UserContext userContext,
			OggettoBulk bulk) throws ComponentException, PersistencyException {
		try {
			Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
			eliminaTuttiDettagliDistinteCollegate(userContext, distinta);
			eliminaTuttiDettagliDistinta(userContext, distinta);
			deleteBulk(userContext, distinta);
		} catch (it.cnr.jada.persistency.sql.NotDeletableException e) {
			if (e.getPersistent() != bulk)
				throw handleException(e);
			throw new CRUDNotDeletableException("Oggetto non eliminabile", e);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Modifica di una istanza di Distinta_cassiereBulk Nome: Esegui modifica
	 * distinta Pre: E' stata richiesta la modifica di una istanza di
	 * Distinta_cassiereBulk che ha superato la validazione Post: La distinta e'
	 * stata modificata
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da modificare
	 * 
	 * @return la distinta modificata
	 * 
	 * 
	 */
	protected OggettoBulk eseguiModificaConBulk(UserContext userContext,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
		try {
			makeBulkPersistent(userContext, distinta);
			return distinta;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Inizializzazione di una istanza di Distinta_cassiereBulk per inserimento
	 * 
	 * Nome: Inizializzazione per inserimento Pre: E' stata richiesta
	 * l'inizializzazione di una istanza di Distinta_cassiereBulk per
	 * inserimento Post: Viene inizializzata la distinta, impostando come Cds
	 * quello di scrivania e come data di emissione la data odierna; vengono
	 * impostati a 0 tutti i totali dei mandati/reversali presenti in distinta;
	 * viene assegnato il progressivo distinta ( metodo 'assegnaProgressivo');
	 * vengono calcolati gli storici degli importi di mandati/reversali già
	 * trasmessi al cassiere (metodo 'calcolaTotaliStorici'); viene recuperato
	 * il codice del Cds Ente (999). La distinta viene inserita nel database.
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da inizializzare per
	 *            inserimento
	 * 
	 * @return la distinta inizializzata per l'inserimento
	 * 
	 * 
	 */

	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {
		try {
			verificaStatoEsercizio(userContext);

			// questa inizializzazione è necessaria per motivi prestazionali
			// serve per preimpostare il terzo per i versamenti CORI accentrati
			// in base all'anno di esercizio di scrivania
			callCercaTerzoVersCORI(userContext,
					it.cnr.contab.utenze00.bp.CNRUserContext
							.getEsercizio(userContext));

			bulk = super.inizializzaBulkPerInserimento(userContext, bulk);
			if (bulk instanceof Distinta_cassiereBulk) {
				Distinta_cassiereBulk distinta = inizializzaDistintaPerInserimento(
						userContext, (Distinta_cassiereBulk) bulk,
						(CdsBulk) getHome(userContext, CdsBulk.class)
								.findByPrimaryKey(
										new CdsBulk(
												((CNRUserContext) userContext)
														.getCd_cds())));
				/*
				 * Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)
				 * bulk; distinta.setCds( (CdsBulk) getHome( userContext,
				 * CdsBulk.class ).findByPrimaryKey( new CdsBulk(
				 * ((CNRUserContext) userContext).getCd_cds())));
				 * lockUltimaDistinta( userContext, distinta ); //
				 * distinta.setDt_emissione( getHome( userContext,
				 * distinta.getClass()).getServerTimestamp()); // imposto la
				 * data di emissione in modo da averla nel seguente formato:
				 * gg/mm/aaaa distinta.setDt_emissione(
				 * DateServices.getDt_valida( userContext) ); // inizializzo i
				 * totali dei trasmessi distinta = calcolaTotaliStorici(
				 * userContext, distinta ); distinta.setIm_man_ini_pag( new
				 * BigDecimal(0) ); distinta.setIm_man_ini_sos( new
				 * BigDecimal(0) ); distinta.setIm_man_ini_acc( new
				 * BigDecimal(0) ); distinta.setIm_rev_ini_sos( new
				 * BigDecimal(0) ); distinta.setIm_rev_ini_tra( new
				 * BigDecimal(0) ); distinta.setIm_rev_ini_rit( new
				 * BigDecimal(0) );
				 * 
				 * assegnaProgressivo( userContext, distinta );
				 * 
				 * EnteBulk ente = (EnteBulk) getHome( userContext,
				 * EnteBulk.class).findAll().get(0); distinta.setCd_cds_ente(
				 * ente.getCd_unita_organizzativa());
				 */
				insertBulk(userContext, distinta);
			}
			return bulk;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Inizializzazione di una istanza di Distinta_cassiereBulk per modifica
	 * 
	 * Nome: Inizializzazione per modifica Pre: E' stata richiesta
	 * l'inizializzazione di una istanza di Distinta_cassiereBulk per modifica
	 * Post: Viene inizializzato la distinta, calcolati i totali dei
	 * mandati/reversali presenti in distinta (suddivivisi per tipologia)
	 * (metodo 'calcolaTotali'), vengono calcolati gli storici degli importi di
	 * mandati/reversali già trasmessi al cassiere (metodo
	 * 'calcolaTotaliStorici') e viene recuperato il codice del Cds Ente (999)
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da inizializzare per la
	 *            modifica
	 * 
	 * @return la distinta inizializzata per la modifica
	 * 
	 * 
	 */

	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {
		try {
			// questa inizializzazione è necessaria per motivi prestazionali
			// serve per preimpostare il terzo per i versamenti CORI accentrati
			// in base all'anno di esercizio di scrivania
			callCercaTerzoVersCORI(userContext,
					it.cnr.contab.utenze00.bp.CNRUserContext
							.getEsercizio(userContext));

			bulk = super.inizializzaBulkPerModifica(userContext, bulk);
			if (bulk instanceof Distinta_cassiereBulk) {
				Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
				lockUltimaDistinta(userContext, distinta);
				distinta = calcolaTotali(userContext, distinta);
				distinta = calcolaTotaliStorici(userContext, distinta);

				EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
						.findAll().get(0);
				distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
				distinta.setCreateByOtherUo(this.isCreateByOtherUo(userContext,
						distinta));
			}
			return bulk;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
	 * 
	 * Nome: Inizializzazione per ricerca Pre: E' stata richiesta
	 * l'inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
	 * Post: Viene inizializzato il Cds della distinta, viene recuperato il
	 * codice del Cds ente (999) e vengono calcolati gli storici degli importi
	 * di mandati/reversali già trasmessi al cassiere (metodo
	 * 'calcolaTotaliStorici')
	 * 
	 * @param aUC
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk
	 *            <code>OggettoBulk</code> la distinta da inizializzare per la
	 *            ricerca
	 * 
	 * @return la distinta inizializzata per la ricerca
	 * 
	 * 
	 */
	public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {
		try {
			// questa inizializzazione è necessaria per motivi prestazionali
			// serve per preimpostare il terzo per i versamenti CORI accentrati
			// in base all'anno di esercizio di scrivania
			callCercaTerzoVersCORI(userContext,
					it.cnr.contab.utenze00.bp.CNRUserContext
							.getEsercizio(userContext));

			bulk = super.inizializzaBulkPerRicerca(userContext, bulk);
			if (bulk instanceof Distinta_cassiereBulk) {
				Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
				distinta.setCds((CdsBulk) getHome(userContext, CdsBulk.class)
						.findByPrimaryKey(
								new CdsBulk(((CNRUserContext) userContext)
										.getCd_cds())));
				/* inizializzo i totali dei trasmessi */
				distinta = calcolaTotaliStorici(userContext, distinta);

				EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
						.findAll().get(0);
				distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
			}
			return bulk;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * 
	 * Nome: inizializza dettagli per modfica Pre: E' stata generata la
	 * richiesta di modificare i dettagli di una distinta Post: E' stato
	 * impostato un savepoint in modo da consentire all'utente l'annullamento
	 * delle modifiche dei dettagli
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk i cui dettagli devono essere
	 *            modificati
	 */

	public void inizializzaDettagliDistintaPerModifica(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			setSavepoint(userContext, "DISTINTA_CASSIERE_DET");
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea un dettaglio di distinta
	 * 
	 * Nome: creazione dettaglio Pre: E' stata generata la richiesta di
	 * inserimento di un nuovo dettaglio di distinta relativo al documento
	 * contabile (mandato o reversale) a partire dal progerssivo dettaglio
	 * last_pg_dettaglio Post: Un nuovo dettaglio distinta e' stato creato e lo
	 * stato_trasmissione del doc. contabile associato a tale dettaglio viene
	 * aggiornato a INSERITO IN DISTINTA; il progressivo dettaglio viene
	 * incrementato di 1.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui creare il dettaglio
	 * @param docContabile
	 *            il mandato/reversale da inserire in distinta
	 * @param last_pg_dettaglio
	 *            il numero che indica l'ultimo progressivo dettaglio utilizzato
	 *            per la distinta
	 * @return last_pg_dettaglio + 1
	 */
	public Long inserisciDettaglioDistinta(it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta,
			V_mandato_reversaleBulk docContabile, Long last_pg_dettaglio)
			throws ComponentException {
		try {
			Distinta_cassiere_detBulk dettaglio = new Distinta_cassiere_detBulk(
					distinta.getCd_cds(), distinta.getCd_unita_organizzativa(),
					distinta.getEsercizio(), last_pg_dettaglio, distinta
							.getPg_distinta());
			if (docContabile.isMandato())
				dettaglio.setPg_mandato(docContabile.getPg_documento_cont());
			else if (docContabile.isReversale())
				dettaglio.setPg_reversale(docContabile.getPg_documento_cont());
			dettaglio.setCd_cds_origine(docContabile.getCd_cds());
			dettaglio.setUser(it.cnr.contab.utenze00.bp.CNRUserContext
					.getUser(userContext));
			insertBulk(userContext, dettaglio);

			aggiornaStatoDocContabile(userContext, docContabile,
					MandatoBulk.STATO_TRASMISSIONE_INSERITO);

			last_pg_dettaglio = new Long(last_pg_dettaglio.longValue() + 1);

			return last_pg_dettaglio;

		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Gestisce l'invio a cassiere di un insieme di distinte selezionate
	 * dall'utente
	 * 
	 * Nome: gestione dell'invio di distinte al cassiere Pre: L'utente ha
	 * selezionato le distinte da inviare al cassiere Post: Tutte le distinte
	 * selezionate sono state inviate al cassiere ( metodo
	 * 'inviaSingolaDistinta')
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinte
	 *            la collezione di oggetti V_distinta_cass_im_man_revBulk da
	 *            inviare
	 */

	public void inviaDistinte(UserContext userContext, Collection distinte)
			throws it.cnr.jada.comp.ComponentException {
		try {
			V_distinta_cass_im_man_revBulk v_distinta;
			Distinta_cassiereBulk distinta;

			for (Iterator i = distinte.iterator(); i.hasNext();) {
				v_distinta = (V_distinta_cass_im_man_revBulk) i.next();
				distinta = (Distinta_cassiereBulk) getHome(userContext,
						Distinta_cassiereBulk.class).findByPrimaryKey(
						new Distinta_cassiereBulk(v_distinta.getCd_cds(),
								v_distinta.getCd_unita_organizzativa(),
								v_distinta.getEsercizio(), v_distinta
										.getPg_distinta()));
				if (v_distinta.getPg_ver_rec().compareTo(
						distinta.getPg_ver_rec()) != 0)
					throw new ApplicationException("Risorsa non più valida");
				inviaSingolaDistinta(userContext, distinta);
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * 
	 * Nome: gestione dell'invio di una distinta al cassiere Pre: Una richiesta
	 * di invio a cassiere di una distinta e' stata generata Post: Tutti i
	 * mandati/reversali che erano già stati trasmessi al cassiere e che
	 * successivamente sono stati annullati vengono aggiunti automaticamente in
	 * distinta per essere ritrasmessi (metodo
	 * 'aggiungiMandatiEReversaliDaRitrasmettere'); tutti i doc.contabili
	 * inclusi in distinta vengono aggiornati per modificarne lo
	 * stato_trasmissione a TRASMESSO e la data di trasmissione ( metodo
	 * 'aggiornaStatoDocContabili'); alla distinta viene assegnato il
	 * progressivo cassiere (metodo 'assegnaProgressivoCassiere'); lo storico
	 * dei trasmessi viene aggiornato aggiungendo gli importi relativi ai
	 * mandati/reversali non annullati presenti nella distinta inviata (metodo
	 * 'aggiornaStoricoTrasmessi'); la data di invio della distinta viene
	 * aggiornata con la data odierna.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk da inviare al cassiere
	 */

	public void inviaSingolaDistinta(UserContext userContext,
			Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {
		try {
			if (distinta == null)
				throw new ApplicationException(
						"Attenzione! La distinta e' stata cancellata");

			boolean isDistintaCollegata = false;
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
					userContext, Unita_organizzativa_enteBulk.class).findAll()
					.get(0);
			if (distinta.getCd_cds() != ente.getCd_unita_organizzativa()
					&& this.isCreateByOtherUo(userContext, distinta))
				isDistintaCollegata = true;

			if (isDistintaCollegata
					&& !CNRUserContext.getCd_unita_organizzativa(userContext)
							.equals(ente.getCd_unita_organizzativa()))
				throw new ApplicationException(
						"Attenzione! La distinta "
								+ distinta.getPg_distinta()
								+ " e' stata creata dalla Uo Ente. Non è possibile modificare lo stato.");

			if (!(isDistintaCollegata && CNRUserContext
					.getCd_unita_organizzativa(userContext).equals(
							ente.getCd_unita_organizzativa())))
				if(!tesoreriaUnica(userContext,distinta )) {
				// aggiungo i mandati da ritrasmettere
					aggiungiMandatiEReversaliDaRitrasmettere(userContext, distinta);
				}
			verificaDuplicazioniProgDocCont(userContext, distinta);

			// aggiorno lo stato trasmissione di mandati/reversali
			aggiornaStatoDocContabili(userContext, distinta,
					MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);

			assegnaProgressivoCassiere(userContext, distinta);

			aggiornaStoricoTrasmessi(userContext, distinta);

			// imposto la data di invio della distinta
			distinta.setDt_invio(DateServices.getDt_valida(userContext));
			distinta.setUser(((CNRUserContext) userContext).getUser());
			distinta.setToBeUpdated();
			makeBulkPersistent(userContext, distinta);
			aggiornaDataDiffDocamm(userContext, distinta,
					MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
			SQLBuilder sql = selectDistinte_cassiere_detCollegate(userContext,
					distinta);
			List list = getHome(userContext, Distinta_cassiere_detBulk.class)
					.fetchAll(sql);

			for (Iterator i = list.iterator(); i.hasNext();)
				inviaSingolaDistinta(userContext,
						(Distinta_cassiereBulk) getHome(userContext,
								Distinta_cassiereBulk.class).findByPrimaryKey(
								((Distinta_cassiere_detBulk) i.next())
										.getDistinta()));
			
		} catch (Exception e) {
		throw handleException(e);
		}
	}

	private void aggiornaDataDiffDocamm(UserContext userContext,
			Distinta_cassiereBulk distinta, String statoTrasmissioneTrasmesso)
			throws ComponentException {
		try {
			V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
					userContext, V_mandato_reversaleBulk.class);
			Fattura_passiva_IHome home_fat = (Fattura_passiva_IHome) getHome(
					userContext, Fattura_passiva_IBulk.class);
			Fattura_passiva_rigaIHome home_righe = (Fattura_passiva_rigaIHome) getHome(
					userContext, Fattura_passiva_rigaIBulk.class);
			Mandato_rigaIHome home_mandato = (Mandato_rigaIHome) getHome(
					userContext, Mandato_rigaIBulk.class);
			Mandato_rigaIBulk mandato_riga = new Mandato_rigaIBulk();
			Reversale_rigaIHome home_reversale = (Reversale_rigaIHome) getHome(
					userContext, Reversale_rigaIBulk.class);
			Reversale_rigaIBulk reversale_riga = new Reversale_rigaIBulk();
			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			List list = home.fetchAll(sql);

			for (Iterator i = list.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
						.next();
				if (bulk.isMandato()) {
					mandato_riga = new Mandato_rigaIBulk();
					mandato_riga.setMandatoI(new MandatoIBulk(bulk.getCd_cds(),
							bulk.getEsercizio(), bulk.getPg_documento_cont()));
					List l = home_mandato.find(mandato_riga);
					for (Iterator iter = l.iterator(); iter.hasNext();) {
						mandato_riga = (Mandato_rigaIBulk) iter.next();
						if (mandato_riga.getCd_tipo_documento_amm() != null
								&& mandato_riga
										.getCd_tipo_documento_amm()
										.compareTo(
												Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0
								|| mandato_riga
										.getCd_tipo_documento_amm()
										.compareTo(
												Numerazione_doc_ammBulk.TIPO_COMPENSO) == 0) {
							// Fattura_passiva_IBulk
							// fattura=(Fattura_passiva_IBulk)home_fat.findByPrimaryKey(
							// new
							// Fattura_passiva_IBulk(mandato_riga.getCd_cds_doc_amm(),mandato_riga.getCd_uo_doc_amm(),mandato_riga.getEsercizio_doc_amm(),mandato_riga.getPg_doc_amm()));
							// if(fattura!=null){
							SQLBuilder sql_fat = home_righe.createSQLBuilder();
							if (mandato_riga
									.getCd_tipo_documento_amm()
									.compareTo(
											Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0) {
								sql_fat.addTableToHeader("FATTURA_PASSIVA");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA_RIGA.ESERCIZIO",
										"FATTURA_PASSIVA.ESERCIZIO");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA_RIGA.CD_CDS",
										"FATTURA_PASSIVA.CD_CDS");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
												"FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
												"FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE",
												sql.EQUALS, mandato_riga
														.getCd_cds());
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE",
												sql.EQUALS,
												mandato_riga
														.getEsercizio_obbligazione());
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE",
												sql.EQUALS,
												mandato_riga
														.getEsercizio_ori_obbligazione());
								sql_fat.addSQLClause("AND",
										"FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE",
										sql.EQUALS, mandato_riga
												.getPg_obbligazione());
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO",
												sql.EQUALS,
												mandato_riga
														.getPg_obbligazione_scadenzario());
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA_RIGA.STATO_COFI",
												sql.NOT_EQUALS,
												Fattura_passiva_rigaBulk.STATO_ANNULLATO);
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
												sql.EQUALS, "Y");
							} else if (mandato_riga
									.getCd_tipo_documento_amm()
									.compareTo(
											Numerazione_doc_ammBulk.TIPO_COMPENSO) == 0) {
								sql_fat
										.addTableToHeader("FATTURA_PASSIVA,COMPENSO");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA_RIGA.ESERCIZIO",
										"FATTURA_PASSIVA.ESERCIZIO");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA_RIGA.CD_CDS",
										"FATTURA_PASSIVA.CD_CDS");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
												"FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
												"FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA.ESERCIZIO_FATTURA_FORNITORE",
												sql.EQUALS,
												"COMPENSO.ESERCIZIO_FATTURA_FORNITORE");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA.DT_FATTURA_FORNITORE",
										sql.EQUALS,
										"COMPENSO.DT_FATTURA_FORNITORE");
								sql_fat.addSQLJoin(
										"FATTURA_PASSIVA.NR_FATTURA_FORNITORE",
										sql.EQUALS,
										"COMPENSO.NR_FATTURA_FORNITORE");
								sql_fat
										.addSQLJoin(
												"FATTURA_PASSIVA.DT_REGISTRAZIONE",
												sql.EQUALS,
												"COMPENSO.DT_REGISTRAZIONE");
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA.STATO_COFI",
												sql.NOT_EQUALS,
												Fattura_passiva_rigaBulk.STATO_ANNULLATO);
								sql_fat
										.addSQLClause(
												"AND",
												"FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
												sql.EQUALS, "Y");

								sql_fat.addSQLClause("AND",
										"COMPENSO.CD_CDS_OBBLIGAZIONE",
										sql.EQUALS, mandato_riga.getCd_cds());
								sql_fat.addSQLClause("AND",
										"COMPENSO.ESERCIZIO_OBBLIGAZIONE",
										sql.EQUALS, mandato_riga
												.getEsercizio_obbligazione());
								sql_fat
										.addSQLClause(
												"AND",
												"COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE",
												sql.EQUALS,
												mandato_riga
														.getEsercizio_ori_obbligazione());
								sql_fat.addSQLClause("AND",
										"COMPENSO.PG_OBBLIGAZIONE", sql.EQUALS,
										mandato_riga.getPg_obbligazione());
								sql_fat
										.addSQLClause(
												"AND",
												"COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO",
												sql.EQUALS,
												mandato_riga
														.getPg_obbligazione_scadenzario());
								sql_fat
										.addSQLClause(
												"AND",
												"COMPENSO.STATO_COFI",
												sql.NOT_EQUALS,
												Fattura_passiva_rigaBulk.STATO_ANNULLATO);
								sql_fat.addSQLClause("AND",
										"COMPENSO.FL_LIQUIDAZIONE_DIFFERITA",
										sql.EQUALS, "Y");
							}
							List list_righe = home_righe.fetchAll(sql_fat);
							for (Iterator it = list_righe.iterator(); it
									.hasNext();) {
								Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) it
										.next();
								riga = (Fattura_passiva_rigaBulk) home_righe
										.findByPrimaryKey(riga);
								if (riga.getData_esigibilita_iva() == null) {

									LoggableStatement cs = new LoggableStatement(
											getConnection(userContext),
											"{  call "
													+ EJBCommonServices
															.getDefaultSchema()
													+ "CNRCTB100.aggiorna_data_differita(?, ?, ?, ?, ?)}",
											false, this.getClass());
									try {
										cs.setInt(1, riga.getEsercizio());
										cs.setString(2, riga.getCd_cds());
										cs.setString(3, riga
												.getCd_unita_organizzativa());
										cs.setLong(4, riga
												.getPg_fattura_passiva()
												.longValue());
										cs.setLong(5, riga
												.getProgressivo_riga()
												.longValue());
										cs.executeQuery();
									} catch (SQLException e) {
										throw handleException(e);
									} finally {
										cs.close();
									}
								}

							}
						}
					}

				}
				// Per le NC che generano una reversale
				else {

					reversale_riga.setReversaleI(new ReversaleIBulk(bulk
							.getCd_cds(), bulk.getEsercizio(), bulk
							.getPg_documento_cont()));
					List l = home_reversale.find(reversale_riga);
					for (Iterator iter = l.iterator(); iter.hasNext();) {
						reversale_riga = (Reversale_rigaIBulk) iter.next();
						if (reversale_riga.getCd_tipo_documento_amm() != null
								&& reversale_riga
										.getCd_tipo_documento_amm()
										.compareTo(
												Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0) {
							SQLBuilder sql_fat = home_righe.createSQLBuilder();
							sql_fat.addTableToHeader("FATTURA_PASSIVA");
							sql_fat.addSQLJoin(
									"FATTURA_PASSIVA_RIGA.ESERCIZIO",
									"FATTURA_PASSIVA.ESERCIZIO");
							sql_fat.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS",
									"FATTURA_PASSIVA.CD_CDS");
							sql_fat
									.addSQLJoin(
											"FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
											"FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
							sql_fat.addSQLJoin(
									"FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
									"FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
							sql_fat.addSQLClause("AND",
									"FATTURA_PASSIVA_RIGA.CD_CDS_ACCERTAMENTO",
									sql.EQUALS, reversale_riga.getCd_cds());
							sql_fat
									.addSQLClause(
											"AND",
											"FATTURA_PASSIVA_RIGA.ESERCIZIO_ACCERTAMENTO",
											sql.EQUALS,
											reversale_riga
													.getEsercizio_accertamento());
							sql_fat
									.addSQLClause(
											"AND",
											"FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_ACCERTAMENTO",
											sql.EQUALS,
											reversale_riga
													.getEsercizio_ori_accertamento());
							sql_fat.addSQLClause("AND",
									"FATTURA_PASSIVA_RIGA.PG_ACCERTAMENTO",
									sql.EQUALS, reversale_riga
											.getPg_accertamento());
							sql_fat
									.addSQLClause(
											"AND",
											"FATTURA_PASSIVA_RIGA.PG_ACCERTAMENTO_SCADENZARIO",
											sql.EQUALS,
											reversale_riga
													.getPg_accertamento_scadenzario());
							sql_fat.addSQLClause("AND",
									"FATTURA_PASSIVA_RIGA.STATO_COFI",
									sql.NOT_EQUALS,
									Fattura_passiva_rigaBulk.STATO_ANNULLATO);
							sql_fat
									.addSQLClause(
											"AND",
											"FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
											sql.EQUALS, "Y");
							List list_righe = home_righe.fetchAll(sql_fat);
							for (Iterator it = list_righe.iterator(); it
									.hasNext();) {
								Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) it
										.next();
								riga = (Fattura_passiva_rigaBulk) home_righe
										.findByPrimaryKey(riga);
								if (riga.getData_esigibilita_iva() == null) {

									LoggableStatement cs = new LoggableStatement(
											getConnection(userContext),
											"{  call "
													+ EJBCommonServices
															.getDefaultSchema()
													+ "CNRCTB100.aggiorna_data_differita(?, ?, ?, ?, ?)}",
											false, this.getClass());
									try {
										cs.setInt(1, riga.getEsercizio());
										cs.setString(2, riga.getCd_cds());
										cs.setString(3, riga
												.getCd_unita_organizzativa());
										cs.setLong(4, riga
												.getPg_fattura_passiva()
												.longValue());
										cs.setLong(5, riga
												.getProgressivo_riga()
												.longValue());
										cs.executeQuery();
									} catch (SQLException e) {
										throw handleException(e);
									} finally {
										cs.close();
									}
								}

							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw handleException(e);
		}

	}

	protected void lockUltimaDistinta(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			Distinta_cassiereBulk ultimaDistinta = ((Distinta_cassiereHome) getHome(
					userContext, distinta.getClass()))
					.findUltimaDistinta(distinta);
			if (ultimaDistinta == null)
				acquisisciSemaforo(userContext, distinta);

		} catch (Exception e) {
			throw handleException(e);
		}

	}

	/**
	 * 
	 * Nome: modifica dettagli distinta Pre: L'utente ha richiesto la modifica
	 * dei dettagli di una distinta, aggiungendo nuovi mandati/reversali e/o
	 * richiedendo la cancellazione dalla distinta di mandati/reversali
	 * precedentemente inseriti Post: Per ogni mandato/reversale per cui
	 * l'utente ha richiesto l'inserimento in distinta viene generato un
	 * dettaglio della distinta e lo stato trasmissione del mandato/reversale
	 * viene aggiornato a 'inserito in distinta' (metodo
	 * 'inserisciDettaglioDistinta'); se tale mandato/reversale ha associati
	 * altre reversali/mandati, vengono creati automaticamente dei dettagli di
	 * distinta anche per questi ed il loro stato trasmissione viene aggiornato
	 * (metodo 'aggiungiMandatiEReversaliCollegati'); Per ogni mandato/reversale
	 * per cui l'utente ha richiesto la cancellazione dalla distinta viene
	 * eliminato il dettaglio distinta ad esso riferito e lo stato trasmissione
	 * del mandato/reversale viene aggiornato a 'non inserito in distinta'
	 * (metodo 'eliminaDettaglioDistinta'); se tale mandato/reversale ha
	 * associati reversali/mandati, vengono eliminati automaticamente i loro
	 * dettagli di distinta ed il loro stato trasmissione viene aggiornato
	 * (metodo 'eliminaMandatiEReversaliCollegati');
	 * 
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk i cui dettagli sono stati modificati
	 * @param docContabili
	 *            l'array di documenti contabili (V_mandato_reversaleBulk)
	 *            potenzialmente interessati da questa modifica
	 * @param oldDocContabili
	 *            il BitSet che specifica la precedente selezione nell'array
	 *            docContabili
	 * @param newDocContabili
	 *            il BitSet che specifica l'attuale selezione nell'array
	 *            docContabili
	 */

	public void modificaDettagliDistinta(UserContext userContext,
			Distinta_cassiereBulk distinta, OggettoBulk[] docContabili,
			BitSet oldDocContabili, BitSet newDocContabili)
			throws ComponentException {
		try {
			Long last_pg_dettaglio = null;
			for (int i = 0; i < docContabili.length; i++) {
				V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) docContabili[i];
				if (oldDocContabili.get(i) != newDocContabili.get(i)) {
					if (last_pg_dettaglio == null)
						last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
								userContext, Distinta_cassiere_detBulk.class))
								.getUltimoPg_Dettaglio(userContext, distinta);
					if (newDocContabili.get(i)) {
						last_pg_dettaglio = inserisciDettaglioDistinta(
								userContext, distinta, docContabile,
								last_pg_dettaglio);
						last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
								userContext, distinta, docContabile,
								last_pg_dettaglio);
						inserisciDettaglioDistinteCollegate(userContext,
								distinta, docContabile);
					} else {
						eliminaDettaglioDistinta(userContext, distinta,
								docContabile);
						eliminaMandatiEReversaliCollegati(userContext,
								distinta, docContabile);
						eliminaDettaglioDistinteCollegate(userContext,
								distinta, docContabile);
					}
				}
			}
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Processa File PreCondition: E' stata generata la richiesta di processare
	 * un file. Nessun errore rilevato. PostCondition: Viene richiamata la
	 * procedura che processerà il file selezionato dall'utente, (metodo
	 * callProcessaFile). Restituisce l'oggetto V_ext_cassiere00Bulk aggiornato.
	 * 
	 * @param userContext
	 *            lo <code>UserContext</code> che ha generato la richiesta.
	 * @param file
	 *            <code>V_ext_cassiere00Bulk</code> l'oggetto che contiene le
	 *            informazioni relative al file da processare.
	 * 
	 * @return <code>V_ext_cassiere00Bulk</code> l'oggetto aggiornato.
	 **/
	public V_ext_cassiere00Bulk processaFile(
			it.cnr.jada.UserContext userContext, V_ext_cassiere00Bulk file)
			throws it.cnr.jada.comp.ComponentException {

		callProcessaFile(userContext, file);

		return file;// aggiornaLiquidCori(userContext, liquidazione_cori);
	}

	/**
	 * Crea il SQLBuilder per ricercare le distinte
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: cerca distinte Pre: Una richiesta di ricerca di una o più distinte
	 * e' stata generata Post: Viene creato il SQLBuilder con impostati
	 * l'esercizio, il cd_cds e il cd-unita_organizzativa di scrivania
	 * 
	 * Nome: cerca distinte da inviare Pre: Una richiesta di ricerca di una o
	 * più distinte da inviare al cassiere e' stata generata Post: Viene creato
	 * il SQLBuilder con impostati l'esercizio, il cd_cds e il
	 * cd_unita_organizzativa di scrivania e con la data di invio non
	 * valorizzata
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param clauses
	 *            le clausole specificate dall'utente
	 * @param bulk
	 *            la Distinta_cassiereBulk oppure la
	 *            V_distinta_cass_im_man_revBulk da ricercare
	 * @return sql Query con le clausole aggiuntive
	 */

	protected Query select(UserContext userContext, CompoundFindClause clauses,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS,
				((CNRUserContext) userContext).getCd_unita_organizzativa());
		sql.addClause("AND", "esercizio", sql.EQUALS,
				((CNRUserContext) userContext).getEsercizio());
		sql.addClause("AND", "cd_cds", sql.EQUALS,
				((CNRUserContext) userContext).getCd_cds());
		// visualizza solo le distinte che non sono state ancora inviate
		if (bulk instanceof V_distinta_cass_im_man_revBulk) {
			verificaStatoEsercizio(userContext);
			sql.addClause("AND", "dt_invio", sql.ISNULL, null);
		}
		if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk)bulk).getFl_flusso()!=null)
			sql.addClause("AND","fl_flusso", sql.EQUALS, ((Distinta_cassiereBulk)bulk).getFl_flusso().booleanValue());
		if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk)bulk).getFl_sepa()!=null)
			sql.addClause("AND","fl_sepa", sql.EQUALS, ((Distinta_cassiereBulk)bulk).getFl_sepa().booleanValue());
		if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk)bulk).getFl_annulli()!=null)
			sql.addClause("AND","fl_annulli", sql.EQUALS, ((Distinta_cassiereBulk)bulk).getFl_annulli().booleanValue());
	
		sql.addOrderBy("pg_distinta");
		return sql;
	}

	/**
	 * Crea il SQLBuilder per recuperare tutti dettagli del File Cassiere
	 * selezionato
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: cerca i dettagli di un File Cassiere Pre: Una richiesta di ricerca
	 * dei dettagli del File Cassiere e' stata generata Post: Viene creato il
	 * SQLBuilder che consente di recuperare le istanze di Ext_cassiere00Bulk
	 * che contengono tutte le informazioni richieste.
	 * 
	 * @param userContext
	 *            lo <code>UserContext</code> che ha generato la richiesta
	 * @param file
	 *            il <code>V_ext_cassiere00Bulk</code> di cui ricercare i
	 *            dettagli
	 * @param bulkClass
	 *            l'<code>OggettoBulk</code> da usare come prototipo della
	 *            ricerca; sul prototipo vengono costruite delle clausole
	 *            aggiuntive che vengono aggiunte in AND alle clausole
	 *            specificate.
	 * @param clauses
	 *            <code>CompoundFindClause</code> L'albero logico delle clausole
	 *            da applicare alla ricerca
	 * 
	 * @return <code>SQLBuilder</code> Un'istanza di SQLBuilder contenente
	 *         l'istruzione SQL da eseguire e tutti i parametri della query.
	 */
	public SQLBuilder selectDettagliFileCassiereByClause(
			UserContext userContext, V_ext_cassiere00Bulk file,
			Ext_cassiere00Bulk bulkClass, CompoundFindClause clauses)
			throws ComponentException {

		if (file == null)
			return null;

		SQLBuilder sql = getHome(userContext, Ext_cassiere00Bulk.class)
				.createSQLBuilder();

		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, file.getEsercizio());
		sql.addSQLClause("AND", "NOME_FILE", sql.EQUALS, file.getNome_file());

		return sql;
	}
	/**
	 * Crea il SQLBuilder per recuperare i documenti 1210 collegabili alla distinta
	 * @param userContext
	 * @param distinta
	 * @param bulkClass
	 * @param clauses
	 * @return
	 * @throws ComponentException
	 */
	public SQLBuilder selectDistintaCassiere1210LettereDaCollegareByClause(UserContext userContext, DistintaCassiere1210Bulk distinta, Class bulkClass, CompoundFindClause clauses) throws ComponentException {
		SQLBuilder sql = getHome(userContext, Lettera_pagam_esteroBulk.class).createSQLBuilder();
		if (clauses != null)
			sql.addClause(clauses);
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND, "stato_trasmissione", SQLBuilder.EQUALS, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
		sql.addClause(FindClause.AND, "distintaCassiere", SQLBuilder.ISNULL, null);		
		return sql;		
	}
	
	/**
	 * Crea il SQLBuilder per recuperare i documenti 1210 collegati alla distinta
	 * @param userContext
	 * @param distinta
	 * @param bulkClass
	 * @param clauses
	 * @return
	 * @throws ComponentException
	 */
	public SQLBuilder selectDistintaCassiere1210LettereCollegateByClause(UserContext userContext, DistintaCassiere1210Bulk distinta, Class bulkClass, CompoundFindClause clauses) throws ComponentException {
		SQLBuilder sql = getHome(userContext, Lettera_pagam_esteroBulk.class).createSQLBuilder();
		if (clauses != null)
			sql.addClause(clauses);		
		if (distinta.getEsercizio() == null)
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, 0);			
		sql.addClause(FindClause.AND, "distintaCassiere", SQLBuilder.EQUALS, distinta);			
		return sql;		
	}
	
	/**
	 * Crea il SQLBuilder per recuperare tutti dettagli della distinta
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: cerca i dettagli di una distinta Pre: Una richiesta di ricerca dei
	 * dettagli della distinta e' stata generata Post: Viene creato il
	 * SQLBuilder che consente di recuperare le istanze di
	 * V_mandato_reversaleBulk che contengono tutte le informazioni dei mandati
	 * e delle reversali inseriti in distinta.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk di cui ricercare i dettagli
	 * @param bulkClass
	 * @param clauses
	 * @return SQLBuilder
	 */
	
		public SQLBuilder selectDistinta_cassiere_detCollByClause(
				UserContext userContext, Distinta_cassiereBulk distinta,
				Class bulkClass, CompoundFindClause clauses)
				throws ComponentException {	
			try{
				Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);	
			SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class,
					"V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
	 
			if (distinta.getPg_distinta() == null)
				return null;
			long nrDettagli = ((Distinta_cassiere_detHome) getHome(userContext,
					Distinta_cassiere_detBulk.class)).getNrDettagli(
					userContext, distinta);
			if (nrDettagli == 0)
				return null;
			
			if(tesoreriaUnica(userContext,distinta )) {	
				SQLBuilder sqlIN =  getHome(userContext, Distinta_cassiere_detBulk.class).createSQLBuilder();
				sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.ESERCIZIO", sql.EQUALS,
						distinta.getEsercizio());
				sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_CDS", sql.EQUALS,
						distinta.getCd_cds());
				sqlIN.addSQLClause("AND",
						"DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
						distinta.getCd_unita_organizzativa());
				sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.PG_DISTINTA",
						sql.EQUALS, distinta.getPg_distinta());
				//sqlIN.addOrderBy("CD_CDS_ORIGINE,PG_DETTAGLIO");
				List list = getHome(userContext, Distinta_cassiere_detBulk.class).fetchAll(sqlIN);
				String lista_mandati=null;
				String lista_reversali=null;
				//String cds_old=null;
				//String cds_old_rev=null;
				sql.openParenthesis("AND");
				for (Iterator i = list.iterator(); i.hasNext();) {
					Distinta_cassiere_detBulk det=(Distinta_cassiere_detBulk)i.next();
					//if(det.getCd_cds_origine()!=cds_old && det.getPg_mandato()!=null){
					if(det.getPg_mandato()!=null){
						if(lista_mandati!=null){
							sql.openParenthesis("OR");
						
							sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS","V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
							sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", sql.EQUALS, distinta
									.getEsercizio());
							sql.addSQLClause("AND",
									"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
									sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
							
							  sql.addSQLClause("AND","(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN ("+lista_mandati+ ")");
							  
//							  sql.openParenthesis("AND");
//							  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old);
//							  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//							  sql.closeParenthesis();
//							  
							  sql.closeParenthesis();
						}
					    //cds_old=det.getCd_cds_origine();
					    lista_mandati=null;
					}
					if(det.getPg_mandato()!=null){
						if (lista_mandati!=null) 
						   lista_mandati=lista_mandati+","+det.getPg_mandato().toString();
						else
							lista_mandati=det.getPg_mandato().toString();	
					}
					//if(det.getCd_cds_origine()!=cds_old_rev && det.getPg_reversale()!=null){
					if(det.getPg_reversale()!=null){
						if(lista_reversali!=null ){
							sql.openParenthesis("OR");
						
							sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS","V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
							sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", sql.EQUALS, distinta
									.getEsercizio());
							sql.addSQLClause("AND",
									"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
									sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
							
							  sql.addSQLClause("AND","(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN ("+lista_reversali+ ")");
							  
							  
//							  sql.openParenthesis("AND");
//							  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old_rev);
//							  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//							  sql.closeParenthesis();
//							 
							  sql.closeParenthesis();
						}
					    //cds_old_rev=det.getCd_cds_origine();
					    lista_reversali=null;
					}
					if(det.getPg_reversale()!=null){
						if (lista_reversali!=null) 
							lista_reversali=lista_reversali+","+det.getPg_reversale().toString();
							else
								lista_reversali=det.getPg_reversale().toString();
					}
				}
			 
				if(lista_mandati!=null){
					sql.openParenthesis("OR");
					sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS","V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
					sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", sql.EQUALS, distinta
							.getEsercizio());
					sql.addSQLClause("AND",
							"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
							sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
					
					  sql.addSQLClause("AND","(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN ("+lista_mandati+ ")");
					  
//					  sql.openParenthesis("AND");
//					  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old);
//					  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//					  sql.closeParenthesis();
					  sql.closeParenthesis();
				}
				if(lista_reversali!=null){
					sql.openParenthesis("OR");
					sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS","V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
					sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", sql.EQUALS, distinta
							.getEsercizio());
					sql.addSQLClause("AND",
							"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
							sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
					
					  sql.addSQLClause("AND","(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN ("+lista_reversali+ ")");
					  
//					  sql.openParenthesis("AND");
//					  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old_rev);
//					  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//					  sql.closeParenthesis();
					  sql.closeParenthesis();
				}
				
				sql.closeParenthesis();
			  //  sql.addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.TI_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE ");
			    return sql;	 
		 }
		else{
			SQLBuilder sql2 = getHome(userContext, Distinta_cassiere_detBulk.class)
					.createSQLBuilder();
			sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
					"DISTINTA_CASSIERE_DET.ESERCIZIO");
			sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
					"DISTINTA_CASSIERE_DET.CD_CDS");
			// da committare
			sql2.openParenthesis("AND");
			sql2.openParenthesis("AND");
			sql2.addSQLClause("AND",
					"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
					sql2.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
			sql2.addSQLJoin("	V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
					" DISTINTA_CASSIERE_DET.PG_MANDATO");
			sql2.addSQLClause("OR",
					"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
					sql2.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
			sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
					"DISTINTA_CASSIERE_DET.PG_REVERSALE");
			sql2.closeParenthesis();
			sql2.closeParenthesis();
			// da committare
			sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.ESERCIZIO", sql.EQUALS,
					distinta.getEsercizio());
			sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_CDS", sql.EQUALS,
					distinta.getCd_cds());
			sql2.addSQLClause("AND",
					"DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
					distinta.getCd_unita_organizzativa());
			sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.PG_DISTINTA",
					sql.EQUALS, distinta.getPg_distinta());

			sql.addSQLExistsClause("AND", sql2);
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, distinta
					.getEsercizio());
			sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, distinta.getCd_cds());
			// sql.addSQLClause( "AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
			// distinta.getCd_unita_organizzativa() );

			sql	.addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.TI_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE ");
			return sql;
		}
		
		/*
		 * SQLBuilder sql =
		 * getHome(userContext,V_mandato_reversaleBulk.class).createSQLBuilder
		 * (); sql.addTableToHeader("DISTINTA_CASSIERE_DET");
		 * sql.addSQLJoin("V_MANDATO_REVERSALE.ESERCIZIO"
		 * ,"DISTINTA_CASSIERE_DET.ESERCIZIO");
		 * sql.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS"
		 * ,"DISTINTA_CASSIERE_DET.CD_CDS"); sql.openParenthesis( "AND" );
		 * sql.addSQLClause("AND",
		 * "(DISTINTA_CASSIERE_DET.PG_MANDATO is not null AND V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT = 'MAN' AND DISTINTA_CASSIERE_DET.PG_MANDATO = V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT)"
		 * ); sql.addSQLClause("OR",
		 * "(DISTINTA_CASSIERE_DET.PG_REVERSALE is not null AND V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT = 'REV' AND DISTINTA_CASSIERE_DET.PG_REVERSALE = V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT)"
		 * ); sql.closeParenthesis();
		 * sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.ESERCIZIO",sql.EQUALS,
		 * distinta.getEsercizio());
		 * sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.CD_CDS",sql.EQUALS,
		 * distinta.getCd_cds());
		 * sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA"
		 * ,sql.EQUALS, distinta.getCd_unita_organizzativa()); if (
		 * distinta.getPg_distinta() != null )
		 * sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.PG_DISTINTA"
		 * ,sql.EQUALS, distinta.getPg_distinta()); else
		 * sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.PG_DISTINTA"
		 * ,sql.ISNULL, null); sql.addOrderBy(
		 * " V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE.TI_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT_PADRE "
		 * ); return sql;
		 */
		 } catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Validazione di una DistintaBulk in caso di inserimento/maodifica
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Errore nr. dettagli Pre: E' stata richiesta la creazione/modifica
	 * di una distinta senza dettagli Post: Viene restituita all'utente una
	 * segnalazione di errore
	 * 
	 * Nome: validazione ok Pre: E' stata richiesta la creazione/modifica di una
	 * distinta con almeno un dettaglio Post: La distinta ha superato la
	 * validazione
	 * 
	 * @param userContext
	 *            <code>UserContext</code>
	 * @param bulk
	 *            <code>Distinta_cassiereBulk</code> distinta da validare
	 * 
	 */

	protected void validaCreaModificaConBulk(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {
		super.validaCreaModificaConBulk(userContext, bulk);
		
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
		
		try {
			long nrDettagli = ((Distinta_cassiere_detHome) getHome(userContext,
					Distinta_cassiere_detBulk.class)).getNrDettagli(
					userContext, distinta);
			if (nrDettagli == 0)
				throw new ApplicationException(
						" La distinta deve avere almeno un dettaglio!");
			// Controlla i documenti inseriti nella distinta
			validaDocumentiContabiliAssociati(userContext, distinta);
			if(distinta.getFl_annulli().booleanValue())
				callCheckDocContForDistintaAnn(userContext, distinta);
			callCheckDocContForDistinta(userContext, distinta);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Verifica dello stato dell'esercizio
	 * 
	 * @param userContext
	 *            <code>UserContext</code>
	 * 
	 * @return FALSE se per il cds interessato non è stato inserito nessun
	 *         esercizio o se l'esercizio non è in stato di "aperto" TRUE in
	 *         tutti gli altri casi
	 * 
	 */
	protected void verificaStatoEsercizio(UserContext userContext)
			throws ComponentException {
		try {
			EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext,
					EsercizioBulk.class).findByPrimaryKey(
					new EsercizioBulk(((CNRUserContext) userContext)
							.getCd_cds(), ((CNRUserContext) userContext)
							.getEsercizio()));
			if (esercizio == null)
				throw new ApplicationException("L'esercizio "
						+ ((CNRUserContext) userContext).getEsercizio()
						+ " non è ancora stato definito per il Cds "
						+ ((CNRUserContext) userContext).getCd_cds());
			if (!esercizio.STATO_APERTO.equals(esercizio
					.getSt_apertura_chiusura()))
				throw new ApplicationException("L'esercizio "
						+ ((CNRUserContext) userContext).getEsercizio()
						+ " non è ancora stato aperto per il Cds "
						+ ((CNRUserContext) userContext).getCd_cds());
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Ricerca i parametri ente per l'anno di esercizio in scrivania
	 * 
	 * @param aUC
	 *            UserContext
	 * @return Parametri_cnrBulk contenente i parametri ente
	 * @throws it.cnr.jada.comp.ComponentException
	 */
	public Parametri_cnrBulk parametriCnr(UserContext aUC)
			throws it.cnr.jada.comp.ComponentException {
		Parametri_cnrBulk param;
		try {
			param = (Parametri_cnrBulk) getHome(aUC, Parametri_cnrBulk.class)
					.findByPrimaryKey(
							new Parametri_cnrBulk(((CNRUserContext) aUC)
									.getEsercizio()));
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (ComponentException ex) {
			throw handleException(ex);
		}
		if (param == null) {
			throw new ApplicationException("Parametri CNR non trovati.");
			// se si vuole gestire un default
			// param = new Parametri_cnrBulk();
			// param.setFl_versamenti_cori(Boolean.FALSE);
		}
		return param;
	}

	/**
	 * è vero se è stato impostato il flag nei parametri generali
	 * FL_VERSAMENTO_CORI che indica se inserire i mandati di versamento
	 * CORI/IVA in modo obbligatorio e automatico
	 */
	private boolean isInserisciMandatiVersamentoCori(UserContext context)
			throws it.cnr.jada.comp.ComponentException {

		Parametri_cnrBulk parametriCnr = parametriCnr(context);

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);

		if (parametriCnr.getFl_versamenti_cori().booleanValue()
				&& day >= parametriCnr.getVersamenti_cori_giorno().intValue())
			return (true);
		else
			return (false);
	}

	private void callCercaTerzoVersCORI(UserContext userContext,
			Integer esercizio) throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{ call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRUTIL.loadTerzoVersCori(?) }", false, this.getClass());
			cs.setInt(1, esercizio.intValue());
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}

	protected boolean annulliTuttaSac(UserContext userContext,
			Distinta_cassiereBulk distinta) throws SQLException,
			ComponentException, PersistencyException, EJBException,
			RemoteException {
		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
				.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		if (sess.getVal01(userContext, new Integer(0), null, "UO_SPECIALE",
				"UO_DISTINTA_TUTTA_SAC") == null)
			return false;
		if (sess.getVal01(userContext, new Integer(0), null, "UO_SPECIALE",
				"UO_DISTINTA_TUTTA_SAC").equals(
				distinta.getCd_unita_organizzativa()))
			return true;
		return false;
	}

	/**
	 * Verifica che i Documenti Associati alla distinta siano associabili
	 * 
	 * @param userContext
	 *            <code>UserContext</code>
	 * @param distinta
	 *            <code>Distinta_cassiereBulk</code>
	 * 
	 * 
	 */
	private void validaDocumentiContabiliAssociati(UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			// if
			// (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,
			// distinta.getEsercizio()).getFl_siope().booleanValue()) {
			V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
					userContext, V_mandato_reversaleBulk.class);
			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			List list = home.fetchAll(sql);
			for (Iterator i = list.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
						.next();
				
				if (Utility.createParametriCnrComponentSession()
						.getParametriCnr(userContext, distinta.getEsercizio())
						.getFl_siope().booleanValue()) {
					if (bulk.isMandato()
							&& !Utility
									.createMandatoComponentSession()
									.isCollegamentoSiopeCompleto(
											userContext,
											(MandatoBulk) getHome(userContext,
													MandatoIBulk.class)
													.findByPrimaryKey(
															new MandatoIBulk(
																	bulk
																			.getCd_cds(),
																	bulk
																			.getEsercizio(),
																	bulk
																			.getPg_documento_cont()))))
						throw new ApplicationException(
								"Il mandato "
								+bulk.getCd_cds()+"/"
										+ bulk.getPg_documento_cont()
										+ " non risulta associato completamente a codici Siope. Scollegarlo dalla distinta e ripetere l'operazione.");
					else if (bulk.isReversale()
							&& !Utility
									.createReversaleComponentSession()
									.isCollegamentoSiopeCompleto(
											userContext,
											(ReversaleBulk) getHome(
													userContext,
													ReversaleIBulk.class)
													.findByPrimaryKey(
															new ReversaleIBulk(
																	bulk
																			.getCd_cds(),
																	bulk
																			.getEsercizio(),
																	bulk
																			.getPg_documento_cont()))))
						throw new ApplicationException(
								"La reversale "+bulk.getCd_cds()+"/"
										+ bulk.getPg_documento_cont()
										+ " non risulta associata completamente a codici Siope. Scollegarla dalla distinta e ripetere l'operazione.");
				}
				// Controllo valorizzazione Iban
				if (!distinta.isCheckIbanEseguito()) {
					if (bulk.isMandato()) {
						Mandato_rigaIHome home_mandato = (Mandato_rigaIHome) getHome(
								userContext, Mandato_rigaIBulk.class);
						Mandato_rigaIBulk mandato_riga = new Mandato_rigaIBulk();
						mandato_riga.setMandatoI(new MandatoIBulk(bulk
								.getCd_cds(), bulk.getEsercizio(), bulk
								.getPg_documento_cont()));
						List l = home_mandato.find(mandato_riga);
						for (Iterator iter = l.iterator(); iter.hasNext();) {
							mandato_riga = (Mandato_rigaIBulk) iter.next();
							mandato_riga
									.setBanca((BancaBulk) getHome(userContext,
											BancaBulk.class)
											.findByPrimaryKey(
													new BancaBulk(
															mandato_riga
																	.getCd_terzo(),
															mandato_riga
																	.getPg_banca())));
							mandato_riga.getBanca().setTerzo(
									(TerzoBulk) getHome(userContext,
											TerzoBulk.class).findByPrimaryKey(
											new TerzoBulk(mandato_riga
													.getCd_terzo())));
							mandato_riga
									.getBanca()
									.getTerzo()
									.setAnagrafico(
											(AnagraficoBulk) getHome(
													userContext,
													AnagraficoBulk.class)
													.findByPrimaryKey(
															new AnagraficoBulk(
																	mandato_riga
																			.getBanca()
																			.getTerzo()
																			.getCd_anag())));

							if (mandato_riga
									.getBanca()
									.getTi_pagamento()
									.compareTo(
											Rif_modalita_pagamentoBulk.BANCARIO) == 0
									&& mandato_riga.getBanca().getTerzo()
											.getAnagrafico()
											.getTi_italiano_estero().compareTo(
													NazioneBulk.EXTRA_CEE) != 0
									&& mandato_riga.getBanca().getCodice_iban() == null)
								if (!(Utility
										.createParametriCdsComponentSession()
										.getParametriCds(userContext,
												distinta.getCd_cds(),
												distinta.getEsercizio())
										.getFl_blocco_iban().booleanValue()))
									throw new CheckIbanFailed(
											"Poiché a partire dal 1/7/2008 i pagamenti privi di codice IBAN verranno eseguiti dalla banca in ritardo e con una penale a carico dell'istituto, prima di continuare con il salvataggio della distinta (pulsante OK) si consiglia di verificare le coordinate bancarie del terzo "
													+ bulk.getCd_terzo()
													+ " - "
													+ mandato_riga
															.getBanca()
															.getTerzo()
															.getAnagrafico()
															.getDescrizioneAnagrafica()
													+ "!"
													+ "<BR><BR>"
													+ " Selezionando il pulsante 'Annulla' e uscendo dalla funzione di 'Distinta' è possibile eseguire l'aggiornamento dei dati IBAN in anagrafica, dalla funzione 'Terzo persona fisica/giuridica', senza dover ricorrere all'annullo del mandato emesso. Al salvataggio dell'aggiornamento del codice terzo, il codice IBAN verrà riportato automaticamente sul mandato, il quale dovrà essere solo nuovamente stampato ed inserito in distinta per l'invio in banca.<BR><BR>"
													+ "Si ricorda che dal 1/07/2008 non sarà più possibile emettere distinte contenenti mandati con beneficiari privi di IBAN.");

								else
									throw new ApplicationException(
											"Non è possibile procedere al salvataggio della distinta emessa in quanto sono presenti pagamenti privi di codice IBAN. E' necessario verificare le coordinate bancarie del terzo "
													+ bulk.getCd_terzo()
													+ " - "
													+ mandato_riga
															.getBanca()
															.getTerzo()
															.getAnagrafico()
															.getDescrizioneAnagrafica()
													+ "!\n\n"
													+ "Uscendo dalla funzione di 'Distinta' è obbligatorio eseguire l'aggionamento dei dati IBAN in anagrafica, dalla funzione 'Terzo persona fisica/giuridica', senza dover ricorrere all'annullo del mandato emesso. Al salvataggio dell'aggiornamento del codice terzo, il codice IBAN verrà riportato automaticamente sul mandato,"
													+ " il quale dovrà essere solo nuovamente stampato ed inserito in distinta per l'invio in banca.");

						}
					}
				}
			}

			// }
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private Distinta_cassiereBulk inizializzaDistintaPerInserimento(
			UserContext userContext, Distinta_cassiereBulk distinta, CdsBulk cds)
			throws ComponentException {
		try {
			distinta.setCds(cds);
			lockUltimaDistinta(userContext, distinta);
			// distinta.setDt_emissione( getHome( userContext,
			// distinta.getClass()).getServerTimestamp());
			// imposto la data di emissione in modo da averla nel seguente
			// formato: gg/mm/aaaa
			distinta.setDt_emissione(DateServices.getDt_valida(userContext));
			/* inizializzo i totali dei trasmessi */
			distinta = calcolaTotaliStorici(userContext, distinta);
			distinta.setIm_man_ini_pag(new BigDecimal(0));
			distinta.setIm_man_ini_sos(new BigDecimal(0));
			distinta.setIm_man_ini_acc(new BigDecimal(0));
			distinta.setIm_rev_ini_sos(new BigDecimal(0));
			distinta.setIm_rev_ini_tra(new BigDecimal(0));
			distinta.setIm_rev_ini_rit(new BigDecimal(0));

			assegnaProgressivo(userContext, distinta);

			EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
					.findAll().get(0);
			distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
			return distinta;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Elimina dalle distinte dei CDS le reversali associate al mandato di
	 * trasferimento che si sta scollegando
	 * 
	 * Nome: elimina dalle distinte le reversali associate al mandato di
	 * trasferimento che si sta scollegando Pre: E' stata generata la richiesta
	 * di eliminare un dettaglio di distinta relativo ad un documento contabile
	 * (mandato o reversale) Post: Il dettaglio e' stato cancellato e lo
	 * stato_trasmissione del doc. contabile associato a tale dettaglio viene
	 * aggiornato a NON INSERITO IN DISTINTA
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui cancellare il dettaglio
	 * @param docContabile
	 *            il mandato/reversale da cancellare dalla distinta
	 */
	public void eliminaDettaglioDistinteCollegate(
			it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
			throws ComponentException {
		try {
			if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext, distinta.getEsercizio()).getFl_siope()
					.booleanValue()
					&& docContabile.isMandatoAccreditamento()) {
				Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
						userContext, Ass_mandato_reversaleBulk.class);
				Collection listReversali = assHome.findReversali(userContext,
						new MandatoBulk(docContabile.getCd_cds(), docContabile
								.getEsercizio(), docContabile
								.getPg_documento_cont()));

				for (Iterator j = listReversali.iterator(); j.hasNext();) {
					Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
							.next();
					ReversaleBulk reversale = (ReversaleBulk) getHome(
							userContext, ReversaleIBulk.class)
							.findByPrimaryKey(
									new ReversaleBulk(assBulk
											.getCd_cds_reversale(), assBulk
											.getEsercizio_reversale(), assBulk
											.getPg_reversale()));
					if (!reversale.getCd_tipo_documento_cont().equals(
							Numerazione_doc_contBulk.TIPO_REV_PROVV)) {
						V_mandato_reversaleBulk docContabileAssociato = (V_mandato_reversaleBulk) getHome(
								userContext, V_mandato_reversaleBulk.class)
								.findByPrimaryKey(
										new V_mandato_reversaleBulk(reversale
												.getEsercizio(), reversale
												.getCd_tipo_documento_cont(),
												reversale.getCd_cds(),
												reversale.getPg_reversale()));

						Distinta_cassiere_detHome distHome = (Distinta_cassiere_detHome) getHome(
								userContext, Distinta_cassiere_detBulk.class);
						Collection listDistinteReversali = distHome
								.getDettaglioDistinta(userContext, reversale);

						for (Iterator x = listDistinteReversali.iterator(); x
								.hasNext();) {
							Distinta_cassiereBulk distintaReversale = ((Distinta_cassiere_detBulk) x
									.next()).getDistinta();

							eliminaDettaglioDistinta(userContext,
									distintaReversale, docContabileAssociato);
							eliminaMandatiEReversaliCollegati(userContext,
									distintaReversale, docContabileAssociato);

							SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
									userContext, distintaReversale,
									V_mandato_reversaleBulk.class, null);
							List list = getHome(userContext,
									V_mandato_reversaleBulk.class)
									.fetchAll(sql);
							if (list.isEmpty()) {
								distintaReversale.setToBeDeleted();
								deleteBulk(userContext, distintaReversale);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Elimina dalle distinte dei CDS le reversali associate al mandato di
	 * trasferimento che si sta scollegando
	 * 
	 * Nome: elimina dalle distinte le reversali associate al mandato di
	 * trasferimento che si sta scollegando Pre: E' stata generata la richiesta
	 * di eliminare un dettaglio di distinta relativo ad un documento contabile
	 * (mandato o reversale) Post: Il dettaglio e' stato cancellato e lo
	 * stato_trasmissione del doc. contabile associato a tale dettaglio viene
	 * aggiornato a NON INSERITO IN DISTINTA
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui cancellare il dettaglio
	 * @param docContabile
	 *            il mandato/reversale da cancellare dalla distinta
	 */
	public void eliminaTuttiDettagliDistinteCollegate(
			it.cnr.jada.UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		try {
			if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext, distinta.getEsercizio()).getFl_siope()
					.booleanValue()) {
				V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
						userContext, V_mandato_reversaleBulk.class);
				SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
						userContext, distinta, V_mandato_reversaleBulk.class,
						null);
				if(sql!=null){
					List list = home.fetchAll(sql);
	
					for (Iterator i = list.iterator(); i.hasNext();)
						eliminaDettaglioDistinteCollegate(userContext, distinta,
								(V_mandato_reversaleBulk) i.next());
				}
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea un dettaglio di distinta
	 * 
	 * Nome: creazione dettaglio Pre: E' stata generata la richiesta di
	 * inserimento di un nuovo dettaglio di distinta relativo al documento
	 * contabile (mandato o reversale) a partire dal progerssivo dettaglio
	 * last_pg_dettaglio Post: Un nuovo dettaglio distinta e' stato creato e lo
	 * stato_trasmissione del doc. contabile associato a tale dettaglio viene
	 * aggiornato a INSERITO IN DISTINTA; il progressivo dettaglio viene
	 * incrementato di 1.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk per cui creare il dettaglio
	 * @param docContabile
	 *            il mandato/reversale da inserire in distinta
	 * @param last_pg_dettaglio
	 *            il numero che indica l'ultimo progressivo dettaglio utilizzato
	 *            per la distinta
	 * @return last_pg_dettaglio + 1
	 */
	private void inserisciDettaglioDistinteCollegate(
			it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
			throws ComponentException {
		try {
			if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext, distinta.getEsercizio()).getFl_siope()
					.booleanValue()
					&& docContabile.isMandatoAccreditamento()) {
				Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
						userContext, Ass_mandato_reversaleBulk.class);
				Collection listReversali = assHome.findReversali(userContext,
						new MandatoBulk(docContabile.getCd_cds(), docContabile
								.getEsercizio(), docContabile
								.getPg_documento_cont()));

				for (Iterator j = listReversali.iterator(); j.hasNext();) {
					Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
							.next();
					ReversaleBulk reversale = (ReversaleBulk) getHome(
							userContext, ReversaleIBulk.class)
							.findByPrimaryKey(
									new ReversaleBulk(assBulk
											.getCd_cds_reversale(), assBulk
											.getEsercizio_reversale(), assBulk
											.getPg_reversale()));
					if (!reversale.getCd_tipo_documento_cont().equals(
							Numerazione_doc_contBulk.TIPO_REV_PROVV)) {
						V_mandato_reversaleBulk docContabileAssociato = (V_mandato_reversaleBulk) getHome(
								userContext, V_mandato_reversaleBulk.class)
								.findByPrimaryKey(
										new V_mandato_reversaleBulk(reversale
												.getEsercizio(), reversale
												.getCd_tipo_documento_cont(),
												reversale.getCd_cds(),
												reversale.getPg_reversale()));

						Distinta_cassiere_detHome distHome = (Distinta_cassiere_detHome) getHome(
								userContext, Distinta_cassiere_detBulk.class);
						Collection listDistinteReversali = distHome
								.getDettaglioDistinta(userContext, reversale);

						if (listDistinteReversali.isEmpty()) {
							Distinta_cassiereBulk distintaRev = findDistintaCollegataCreata(
									userContext, distinta, reversale
											.getUnita_organizzativa());

							if (distintaRev == null) {
								try {
									distintaRev = new Distinta_cassiereBulk();
									distintaRev.setEsercizio(reversale
											.getEsercizio());
									distintaRev
											.setUnita_organizzativa(reversale
													.getUnita_organizzativa());
									distintaRev.setToBeCreated();
									distintaRev = inizializzaDistintaPerInserimento(
											userContext,
											distintaRev,
											(CdsBulk) getHome(userContext,
													CdsBulk.class)
													.findByPrimaryKey(
															new CdsBulk(
																	reversale
																			.getCd_cds())));
								    distintaRev.setFl_flusso(Boolean.FALSE);
								    distintaRev.setFl_sepa(Boolean.FALSE);
									insertBulk(userContext, distintaRev);
								} catch (ApplicationException e) {
									if (e.getDetail() instanceof it.cnr.jada.persistency.sql.BusyRecordException)
										throw new ApplicationException(
												"Attenzione! L'unità organizzativa "
														+ reversale
																.getUnita_organizzativa()
																.getCd_unita_organizzativa()
														+ " sta effettuando operazioni sulle distinte. Impossibile creare la distinta per le reversali di regolarizzazione associate. Ripetere successivamente l'operazione o scollegare il mandato dalla distinta.");
									throw e;
								}
							}

							Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
									userContext,
									Distinta_cassiere_detBulk.class))
									.getUltimoPg_Dettaglio(userContext,
											distintaRev);
							Distinta_cassiere_detBulk dettaglio = new Distinta_cassiere_detBulk(
									distintaRev.getCd_cds(), distintaRev
											.getCd_unita_organizzativa(),
									distintaRev.getEsercizio(),
									last_pg_dettaglio, distintaRev
											.getPg_distinta());
							dettaglio.setPg_reversale(reversale
									.getPg_reversale());
							dettaglio
									.setUser(it.cnr.contab.utenze00.bp.CNRUserContext
											.getUser(userContext));
							insertBulk(userContext, dettaglio);
							aggiornaStatoDocContabile(userContext,
									docContabileAssociato,
									ReversaleBulk.STATO_TRASMISSIONE_INSERITO);
						}
					}
				}
			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private Distinta_cassiereBulk findDistintaCollegataCreata(
			it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta,
			Unita_organizzativaBulk unitaOrganizzativa)
			throws ComponentException {
		try {
			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			List list = getHome(userContext, V_mandato_reversaleBulk.class)
					.fetchAll(sql);

			for (Iterator i = list.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) i
						.next();
				if (docContabile.isMandatoAccreditamento()) {
					Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
							userContext, Ass_mandato_reversaleBulk.class);
					Collection listReversali = assHome.findReversali(
							userContext, new MandatoBulk(docContabile
									.getCd_cds(), docContabile.getEsercizio(),
									docContabile.getPg_documento_cont()));

					for (Iterator j = listReversali.iterator(); j.hasNext();) {
						Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
								.next();
						ReversaleBulk reversale = (ReversaleBulk) getHome(
								userContext, ReversaleIBulk.class)
								.findByPrimaryKey(
										new ReversaleBulk(assBulk
												.getCd_cds_reversale(), assBulk
												.getEsercizio_reversale(),
												assBulk.getPg_reversale()));
						if (reversale.getUnita_organizzativa()
								.equalsByPrimaryKey(unitaOrganizzativa)
								&& !reversale
										.getCd_tipo_documento_cont()
										.equals(
												Numerazione_doc_contBulk.TIPO_REV_PROVV)
								&& reversale
										.getStato_trasmissione()
										.equals(
												ReversaleBulk.STATO_TRASMISSIONE_INSERITO)) {

							Collection listDistinteReversali = ((Distinta_cassiere_detHome) getHome(
									userContext,
									Distinta_cassiere_detBulk.class))
									.getDettaglioDistinta(userContext,
											reversale);

							for (Iterator x = listDistinteReversali.iterator(); x
									.hasNext();) {
								Distinta_cassiereBulk distintaReversale = ((Distinta_cassiere_detBulk) x
										.next()).getDistinta();
								if (distintaReversale.getDt_invio() == null)
									return distintaReversale;
							}
						}
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private Collection<Distinta_cassiereBulk> findDistinteCollegateCreate(
			it.cnr.jada.UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		try {
			Collection<Distinta_cassiereBulk> collection = new BulkList();

			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			List list = getHome(userContext, V_mandato_reversaleBulk.class)
					.fetchAll(sql);

			for (Iterator i = list.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) i
						.next();
				if (docContabile.isMandatoAccreditamento()) {
					Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
							userContext, Ass_mandato_reversaleBulk.class);
					Collection listReversali = assHome.findReversali(
							userContext, new MandatoBulk(docContabile
									.getCd_cds(), docContabile.getEsercizio(),
									docContabile.getPg_documento_cont()));

					for (Iterator j = listReversali.iterator(); j.hasNext();) {
						Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
								.next();
						ReversaleBulk reversale = (ReversaleBulk) getHome(
								userContext, ReversaleIBulk.class)
								.findByPrimaryKey(
										new ReversaleBulk(assBulk
												.getCd_cds_reversale(), assBulk
												.getEsercizio_reversale(),
												assBulk.getPg_reversale()));
						if (!reversale.getCd_tipo_documento_cont().equals(
								Numerazione_doc_contBulk.TIPO_REV_PROVV)
								&& reversale
										.getStato_trasmissione()
										.equals(
												ReversaleBulk.STATO_TRASMISSIONE_INSERITO)) {

							Collection listDistinteReversali = ((Distinta_cassiere_detHome) getHome(
									userContext,
									Distinta_cassiere_detBulk.class))
									.getDettaglioDistinta(userContext,
											reversale);

							for (Iterator x = listDistinteReversali.iterator(); x
									.hasNext();)
								collection.add(((Distinta_cassiere_detBulk) x
										.next()).getDistinta());
						}
					}
				}
			}
			return collection;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private boolean isCreateByOtherUo(it.cnr.jada.UserContext userContext,
			Distinta_cassiereBulk distinta) throws ComponentException {
		try {
			SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
					userContext, distinta, V_mandato_reversaleBulk.class, null);
			List list = getHome(userContext, V_mandato_reversaleBulk.class)
					.fetchAll(sql);

			if (Utility.createParametriCnrComponentSession().getParametriCnr(
					userContext,
					it.cnr.contab.utenze00.bp.CNRUserContext
							.getEsercizio(userContext)).getFl_siope()
					.booleanValue()) {
				for (Iterator i = list.iterator(); i.hasNext();)
					if (((V_mandato_reversaleBulk) i.next())
							.isReversaleTrasferimento())
						return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea il SQLBuilder per recuperare tutti dettagli delle distinte collegate
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: cerca i dettagli delle distinte collegate a quella indicata Pre:
	 * Una richiesta di ricerca dei dettagli delle distinte collegate a quella
	 * indicata e' stata generata Post: Viene creato il SQLBuilder che consente
	 * di recuperare le istanze di V_mandato_reversaleBulk che contengono tutte
	 * le informazioni dei mandati e delle reversali inseriti nelle distinte
	 * collegate a quella indicata.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk di cui ricercare i dettagli
	 * @param bulkClass
	 * @param clauses
	 * @return SQLBuilder
	 */
	public SQLBuilder selectDistinte_cassiere_detCollegateCollByClause(
			UserContext userContext, Distinta_cassiereBulk distinta,
			Class bulkClass, CompoundFindClause clauses)
			throws ComponentException {
		try {
			if (distinta.getPg_distinta() == null)
				return null;

			SQLBuilder sql2 = selectDistinte_cassiere_detCollegate(userContext,
					distinta);
			List list = getHome(userContext, Distinta_cassiere_detBulk.class)
					.fetchAll(sql2);

			SQLBuilder sql = getHome(userContext,
					V_mandato_reversaleBulk.class,
					"V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
			if (!list.isEmpty()) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					Distinta_cassiere_detBulk det = (Distinta_cassiere_detBulk) i
							.next();
					if (det.getPg_reversale() != null) {
						sql.openParenthesis("OR");
						sql.addSQLClause("AND",
								"V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
								SQLBuilder.EQUALS, det.getEsercizio());
						sql.addSQLClause("AND",
								"V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
								SQLBuilder.EQUALS, det.getCd_cds());
						sql
								.addSQLClause(
										"AND",
										"V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
										SQLBuilder.EQUALS,
										Numerazione_doc_contBulk.TIPO_REV);
						sql
								.addSQLClause(
										"AND",
										"V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
										SQLBuilder.EQUALS, det
												.getPg_reversale());
						sql.closeParenthesis();
					}
				}
				sql
						.addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_CDS, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT ");
			} else
				sql.addSQLClause("AND",
						"V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO IS NULL");
			return sql;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea il SQLBuilder per recuperare tutti dettagli delle distinte collegate
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: cerca i dettagli delle distinte collegate a quella indicata Pre:
	 * Una richiesta di ricerca dei dettagli delle distinte collegate a quella
	 * indicata e' stata generata Post: Viene creato il SQLBuilder che consente
	 * di recuperare le istanze di V_mandato_reversaleBulk che contengono tutte
	 * le informazioni dei mandati e delle reversali inseriti nelle distinte
	 * collegate a quella indicata.
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param distinta
	 *            la Distinta_cassiereBulk di cui ricercare i dettagli
	 * @param bulkClass
	 * @param clauses
	 * @return SQLBuilder
	 */
	private SQLBuilder selectDistinte_cassiere_detCollegate(
			UserContext userContext, Distinta_cassiereBulk distinta)
			throws ComponentException {
		if (distinta.getPg_distinta() == null)
			return null;

		SQLBuilder sql = getHome(userContext, Distinta_cassiere_detBulk.class)
				.createSQLBuilder();
		sql.addTableToHeader("DISTINTA_CASSIERE_DET", "DCMAN");
		sql.addTableToHeader("ASS_MANDATO_REVERSALE", "ASS");
		sql.addTableToHeader("MANDATO", "MAN");

		sql.addSQLClause("AND", "DCMAN.ESERCIZIO", SQLBuilder.EQUALS, distinta
				.getEsercizio());
		sql.addSQLClause("AND", "DCMAN.CD_CDS", SQLBuilder.EQUALS, distinta
				.getCd_cds());
		sql.addSQLClause("AND", "DCMAN.CD_UNITA_ORGANIZZATIVA",
				SQLBuilder.EQUALS, distinta.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "DCMAN.PG_DISTINTA", SQLBuilder.EQUALS,
				distinta.getPg_distinta());

		sql.addSQLJoin("DCMAN.ESERCIZIO", "MAN.ESERCIZIO");
		sql.addSQLJoin("DCMAN.CD_CDS", "MAN.CD_CDS");
		sql.addSQLJoin("DCMAN.PG_MANDATO", "MAN.PG_MANDATO");

		sql.addSQLClause("AND", "MAN.TI_MANDATO", SQLBuilder.EQUALS,
				MandatoBulk.TIPO_ACCREDITAMENTO);

		sql.addSQLJoin("MAN.ESERCIZIO", "ASS.ESERCIZIO_MANDATO");
		sql.addSQLJoin("MAN.CD_CDS", "ASS.CD_CDS_MANDATO");
		sql.addSQLJoin("MAN.PG_MANDATO", "ASS.PG_MANDATO");

		sql.addSQLJoin("ASS.ESERCIZIO_REVERSALE",
				"DISTINTA_CASSIERE_DET.ESERCIZIO");
		sql.addSQLJoin("ASS.CD_CDS_REVERSALE", "DISTINTA_CASSIERE_DET.CD_CDS");
		sql
				.addSQLJoin("ASS.PG_REVERSALE",
						"DISTINTA_CASSIERE_DET.PG_REVERSALE");

		return sql;
	}

	public RemoteIterator selectFileScarti(UserContext userContext,
			Ext_cassiere00_logsBulk logs) throws ComponentException,
			RemoteException {
		SQLBuilder sql = getHome(userContext, Ext_cassiere00_scartiBulk.class)
				.createSQLBuilder();
		// sql.setDistinctClause(true);
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, logs.getEsercizio());
		sql.addSQLClause("AND", "NOME_FILE", sql.EQUALS, logs.getNome_file());
		sql.addSQLClause("AND", "PG_ESECUZIONE", sql.EQUALS, logs
				.getPg_esecuzione());
		it.cnr.jada.util.RemoteIterator ri = iterator(userContext, sql,
				Ext_cassiere00_scartiBulk.class, null);
		return ri;
	}

	public void caricaFile(UserContext userContext, File file)
			throws ComponentException {
		try {

			if (((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",
							EsercizioComponentSession.class))
					.isEsercizioChiuso(userContext))
				throw new it.cnr.jada.comp.ApplicationException(
						"Funzione non disponibile ad esercizio chiuso.");

			String eserc_scirvania = it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(userContext).toString();
			Bframe_blobBulk bframe_blob = new Bframe_blobBulk("INT_CASS00",
					new java.io.File(parseFilename(file)).getName(), "ritorno/"
							+ eserc_scirvania + "/");
			BulkHome home = (BulkHome) getHome(userContext, Bframe_blobBulk.class);
			bframe_blob.setUser(userContext.getUser());
			home.insert(bframe_blob, userContext);
			home.setSQLLob(bframe_blob, "CDATA", IOUtils.toString(new FileInputStream(file), "UTF-8"));
			home.update(bframe_blob, userContext);
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			file.delete();
		}
	}

	/**
	 * Richiamato per la procedura di upload del nuovo File Cassiere.
	 * 
	 * 
	 * @param context
	 *            il <code>ActionContext</code> che contiene le informazioni
	 *            relative alla richiesta
	 * 
	 * @return forward <code>Forward</code>
	 **/
	private String parseFilename(File file) {

		StringTokenizer fileName = new StringTokenizer(file.getName(), "\\",
				false);
		String newFileName = null;

		while (fileName.hasMoreTokens()) {
			if (newFileName == null) {
				newFileName = fileName.nextToken();
			} else
				newFileName = newFileName + "/" + fileName.nextToken();
		}

		if (newFileName != null)
			return newFileName;

		return file.getName();
	}
	public ExtCassiereCdsBulk recuperaCodiciCdsCassiere(UserContext userContext,Distinta_cassiereBulk distinta) throws ComponentException, PersistencyException, EJBException{
		try{
		ExtCassiereCdsBulk bulk = new ExtCassiereCdsBulk();
		bulk.setEsercizio(distinta.getEsercizio());
		if (!tesoreriaUnica(userContext, distinta)) 
			bulk.setCdCds(distinta.getCd_cds());
		else	
			if (distinta.getCd_cds_ente()!=null)
			bulk.setCdCds(distinta.getCd_cds_ente());
		List oggetti =null;
		oggetti=getHome(userContext, ExtCassiereCdsBulk.class).find(bulk);
		if (oggetti.size()==0)
			throw new ApplicationException("Configurazione mancante dati cassiere");
		else if (oggetti.size()>1)
			throw new ApplicationException("Configurazione errata dati cassiere");
		else	
			return (ExtCassiereCdsBulk)oggetti.get(0);
		
		}catch (Exception e) {
			throw handleException(e);
			
		}
	}
	
	
	public BancaBulk recuperaIbanUo (UserContext userContext,Unita_organizzativaBulk uo) throws ComponentException,
	PersistencyException {
		SQLBuilder sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
		sql.addTableToHeader("TERZO");	
		sql.addSQLClause("AND","TERZO.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,uo.getCd_unita_organizzativa());
		sql.addSQLJoin("BANCA.CD_TERZO","TERZO.CD_TERZO");
		sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",sql.ISNULL,null);
		try {
			if (!Utility.createParametriCnrComponentSession().getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext)).getFl_tesoreria_unica().booleanValue())	
				sql.addSQLClause("AND", "BANCA.FL_CC_CDS",sql.EQUALS, "Y");
			else{
				Configurazione_cnrBulk config = new Configurazione_cnrBulk(
						"CONTO_CORRENTE_SPECIALE",
						"ENTE", 
						"*", 
						new Integer(0));
				it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome)getHome(userContext, config);
				List configurazioni = home.find(config);
				if ((configurazioni != null) && (configurazioni.size() == 1)) {
					Configurazione_cnrBulk configBanca = (Configurazione_cnrBulk)configurazioni.get(0);
					sql.addSQLClause("AND", "BANCA.ABI",sql.EQUALS,configBanca.getVal01());
					sql.addSQLClause("AND", "BANCA.CAB",sql.EQUALS,configBanca.getVal02());
					sql.addSQLClause("AND", "BANCA.NUMERO_CONTO",sql.CONTAINS, configBanca.getVal03());			       
				}
			}
		} catch (Exception e) {
			throw handleException(e);
		}	
		sql.addSQLClause("AND", "BANCA.FL_CANCELLATO",sql.EQUALS, "N");
		sql.addSQLClause("AND","CODICE_IBAN",sql.ISNOTNULL,null);
		if (getHome( userContext, BancaBulk.class ).fetchAll( sql ).size()==0)
			throw new ApplicationException("Configurazione iban uo mancante");
		else if (getHome( userContext, BancaBulk.class ).fetchAll( sql ).size()>1)
			throw new ApplicationException("Configurazione iban uo errata");
		else
		return 
				(BancaBulk)getHome( userContext, BancaBulk.class ).fetchAll( sql ).get(0);
	}
	public List dettagliDistinta(UserContext usercontext, Distinta_cassiereBulk distinta,String tipo) throws PersistencyException, ComponentException{
		try{
		SQLBuilder sql = getHome(usercontext, V_mandato_reversaleBulk.class).createSQLBuilder();
		sql.addTableToHeader("DISTINTA_CASSIERE_DET");
		sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.ESERCIZIO",SQLBuilder.EQUALS, distinta.getEsercizio());
		sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.CD_CDS",SQLBuilder.EQUALS, distinta.getCd_cds());
		sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, distinta.getCd_unita_organizzativa());
		sql.addSQLClause("AND","DISTINTA_CASSIERE_DET.PG_DISTINTA",SQLBuilder.EQUALS, distinta.getPg_distinta());
		
		sql.addSQLJoin("V_MANDATO_REVERSALE.ESERCIZIO","DISTINTA_CASSIERE_DET.ESERCIZIO");
		
		if (!Utility.createParametriCnrComponentSession().getParametriCnr(usercontext,CNRUserContext.getEsercizio(usercontext)).getFl_tesoreria_unica().booleanValue())	
			sql.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS","DISTINTA_CASSIERE_DET.CD_CDS");
		else
			sql.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS","DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE");
		
		// da committare
		//sql.addSQLJoin("V_MANDATO_REVERSALE.CD_UNITA_ORGANIZZATIVA","DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA");
		
		if (tipo.compareTo(Numerazione_doc_contBulk.TIPO_MAN)==0){
			sql.addSQLClause("AND","V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT",sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
			sql.addSQLJoin("DISTINTA_CASSIERE_DET.PG_MANDATO",SQLBuilder.EQUALS,"V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT");
		}else{
			sql.addSQLClause("AND","V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT",sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
			sql.addSQLJoin("DISTINTA_CASSIERE_DET.PG_REVERSALE",SQLBuilder.EQUALS,"V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT");
		}
		return getHome(usercontext, V_mandato_reversaleBulk.class).fetchAll(sql);
	} catch (Exception e){
		   throw handleException(e);
	 }
	}
	protected boolean tesoreriaUnica(UserContext userContext,
			Distinta_cassiereBulk distinta) throws SQLException,
			ComponentException, PersistencyException, EJBException,
			RemoteException {
		return (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,distinta.getEsercizio()).getFl_tesoreria_unica().booleanValue());
	}
	public Distinta_cassiereBulk inviaDistinta(UserContext userContext,
			Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {
		try {
			if (distinta == null)
				throw new ApplicationException(
						"Attenzione! La distinta e' stata cancellata");
			validaCreaModificaConBulk(userContext, distinta);
			verificaDuplicazioniProgDocCont(userContext, distinta);
			// aggiungo i mandati da ritrasmettere
				if(!tesoreriaUnica(userContext,distinta )) {	
					aggiungiMandatiEReversaliDaRitrasmettere(userContext, distinta);
				}
			// aggiorno lo stato trasmissione di mandati/reversali
			aggiornaStatoDocContabili(userContext, distinta,
					MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);

			assegnaProgressivoCassiere(userContext, distinta);

			aggiornaStoricoTrasmessi(userContext, distinta);

			// imposto la data di invio della distinta
			distinta.setDt_invio(DateServices.getDt_valida(userContext));
			distinta.setUser(((CNRUserContext) userContext).getUser());
			distinta.setToBeUpdated();
			makeBulkPersistent(userContext, distinta);
			aggiornaDataDiffDocamm(userContext, distinta,
					MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
			return distinta;
			
		} catch (Exception e) {
		throw handleException(e);
		}
	}
	public List<V_mandato_reversaleBulk> findMandatiCollegati(UserContext usercontext, V_mandato_reversaleBulk v_mandato_reversaleBulk) throws ComponentException{
		try {
			return ((V_mandato_reversaleHome)getHome(usercontext, V_mandato_reversaleBulk.class)).findMandatiCollegati(v_mandato_reversaleBulk);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}	
	public List<V_mandato_reversaleBulk> findReversaliCollegate(UserContext usercontext, V_mandato_reversaleBulk v_mandato_reversaleBulk) throws ComponentException{
		try {
			return ((V_mandato_reversaleHome)getHome(usercontext, V_mandato_reversaleBulk.class)).findReversaliCollegate(v_mandato_reversaleBulk);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	
	public List findDocumentiFlusso(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException{
		it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome home=(it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome)getHome(usercontext, it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.setDistinctClause(true);
		sql.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio());
		sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "PG_DOCUMENTO",SQLBuilder.EQUALS,bulk.getPg_documento_cont());
		sql.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT",SQLBuilder.EQUALS,bulk.getCd_tipo_documento_cont());
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	
	public List findDocumentiFlussoClass(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException{
		it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome homeClass=(it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome)getHome(usercontext, it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk.class,"CLASSIFICAZIONE");
		SQLBuilder sqlClass = homeClass.createSQLBuilder();
		sqlClass.resetColumns();
		sqlClass.addColumn("ESERCIZIO");
		sqlClass.addColumn("CD_UNITA_ORGANIZZATIVA");
		sqlClass.addColumn("PG_DOCUMENTO");
		sqlClass.addColumn("CD_SIOPE");
		sqlClass.addColumn("CD_CUP");
		sqlClass.addColumn("IM_DOCUMENTO"); 
		sqlClass.addColumn("CD_TIPO_DOCUMENTO_AMM"); 
		sqlClass.addColumn("PG_DOC_AMM");
		sqlClass.addColumn("IMPORTO_CGE","IMPORTO_CGE");
		sqlClass.addColumn("IMPORTO_CUP","IMPORTO_CUP"); 
		sqlClass.setDistinctClause(true);
		sqlClass.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio());
		sqlClass.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
		sqlClass.addSQLClause("AND", "PG_DOCUMENTO",SQLBuilder.EQUALS,bulk.getPg_documento_cont());
		sqlClass.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT",SQLBuilder.EQUALS,bulk.getCd_tipo_documento_cont());
		sqlClass.setOrderBy("cdSiope",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		sqlClass.setOrderBy("cdCup",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		try {
			return homeClass.fetchAll(sqlClass);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	public List findDocumentiFlussoSospeso(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException{
		it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome homeSosp=(it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome)getHome(usercontext, it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk.class,"SOSPESO");
		SQLBuilder sqlSosp = homeSosp.createSQLBuilder();
		sqlSosp.resetColumns();
		sqlSosp.addColumn("ESERCIZIO");
		sqlSosp.addColumn("CD_UNITA_ORGANIZZATIVA");
		sqlSosp.addColumn("PG_DOCUMENTO");
		sqlSosp.addColumn("DT_REGISTRAZIONE_SOSP");
		sqlSosp.addColumn("TI_ENTRATA_SPESA"); 
		sqlSosp.addColumn("CD_SOSPESO");
		sqlSosp.addColumn("IM_SOSPESO");
		sqlSosp.addColumn("IM_ASSOCIATO");
		sqlSosp.setDistinctClause(true);
		sqlSosp.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio());
		sqlSosp.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
		sqlSosp.addSQLClause("AND", "PG_DOCUMENTO",SQLBuilder.EQUALS,bulk.getPg_documento_cont());
		sqlSosp.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT",SQLBuilder.EQUALS,bulk.getCd_tipo_documento_cont());
		sqlSosp.setOrderBy("cdSospeso",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		try {
			return homeSosp.fetchAll(sqlSosp);
		} catch (PersistencyException e) {
			throw handleException(e);
		}

	}
	public List findReversali(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException{
		SQLBuilder sql2 = getHome(usercontext, V_mandato_reversaleBulk.class).createSQLBuilder();
		sql2.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio());
		sql2.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
		sql2.addSQLClause("AND", "PG_DOCUMENTO_CONT_PADRE",SQLBuilder.EQUALS,bulk.getPg_documento_cont());
		sql2.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT",SQLBuilder.EQUALS,it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV);
		sql2.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT_PADRE",SQLBuilder.EQUALS,it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);
		try {
			return getHome(usercontext, V_mandato_reversaleBulk.class).fetchAll(sql2);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	public List findDocumentiFlussoClassReversali(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException{
		it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome homeClass=(it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoHome)getHome(usercontext, it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk.class,"CLASSIFICAZIONE");
		SQLBuilder sqlClass = homeClass.createSQLBuilder();
		sqlClass.resetColumns();
		sqlClass.addColumn("ESERCIZIO");
		sqlClass.addColumn("CD_UNITA_ORGANIZZATIVA");
		sqlClass.addColumn("PG_DOCUMENTO");
		sqlClass.addColumn("CD_SIOPE");
		sqlClass.addColumn("CD_CUP");// sempre null per le Revesali, non gestito dal flusso
		sqlClass.addColumn("CD_TIPO_DOCUMENTO_AMM"); 
		sqlClass.addColumn("PG_DOC_AMM");
		sqlClass.addColumn("IM_DOCUMENTO"); 
		sqlClass.addColumn("IMPORTO_CGE");
		sqlClass.addColumn("IMPORTO_CUP"); //sempre 0  per le Revesali, non gestito dal flusso
		sqlClass.setDistinctClause(true);
		sqlClass.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio());
		sqlClass.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
		sqlClass.addSQLClause("AND", "PG_DOCUMENTO",SQLBuilder.EQUALS,bulk.getPg_documento_cont());
		sqlClass.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT",SQLBuilder.EQUALS,bulk.getCd_tipo_documento_cont());
		sqlClass.setOrderBy("cdSiope",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		try {
			return homeClass.fetchAll(sqlClass);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	private void callCheckDocContForDistintaAnn(UserContext userContext,
			Distinta_cassiereBulk distinta)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{ call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB750.checkDocContForDistCasAnn(?,?,?,?) }", false,
					this.getClass());

			cs.setString(1, distinta.getCd_cds());
			cs.setInt(2, distinta.getEsercizio().intValue());
			cs.setString(3, distinta.getCd_unita_organizzativa());
			cs.setLong(4, distinta.getPg_distinta().longValue());

			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}

private void verificaDuplicazioniProgDocCont(UserContext userContext,
		Distinta_cassiereBulk distinta) throws ComponentException {
try {
	V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
			userContext, V_mandato_reversaleBulk.class);
	SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
			userContext, distinta, V_mandato_reversaleBulk.class, null);
	List list = home.fetchAll(sql);
	List lista= home.fetchAll(sql);
	String duplicati=null;
		for (Iterator i = list.iterator(); i.hasNext();) {
			V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i.next();
			if(tesoreriaUnica(userContext, distinta)){
				Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
				if (sess.getVal01(userContext, new Integer(0), null, "COSTANTI", "BLOCCO_UNICITA_PG_MANREV") != null && 
					sess.getVal01(userContext, new Integer(0), null, "COSTANTI","BLOCCO_UNICITA_PG_MANREV").compareTo("S")==0){
					if(distinta.getFl_flusso().booleanValue()){
						for (Iterator iter = lista.iterator(); iter.hasNext();) {
							V_mandato_reversaleBulk copia = (V_mandato_reversaleBulk) iter.next();
							if (copia.getPg_documento_cont().compareTo(bulk.getPg_documento_cont())==0 && copia.getCd_tipo_documento_cont().compareTo(bulk.getCd_tipo_documento_cont())!=0)
								if (duplicati != null) {
									if (!duplicati.contains(" "+bulk.getPg_documento_cont()))
										duplicati=duplicati+" "+bulk.getPg_documento_cont();
								} else
									duplicati="Risultano presenti in distinta sia il mandato che la reversale n. " +bulk.getPg_documento_cont();
						}
					}
				}
			}
		}
		if (duplicati!=null)
			throw new ApplicationException(duplicati);
	} catch (Exception e) {
		throw handleException(e);
	}
}
}