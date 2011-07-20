package it.cnr.contab.reports.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrKey;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.reports.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.SendMail;

public class OfflineReportComponent extends GenericComponent implements
		IOfflineReportMgr {
	/**
	 * OfflineReportComponent constructor comment.
	 */
	public OfflineReportComponent() {
		super();
	}

	/**
	 * Stampa non configurata PreCondition: La stampa specificata non � stata
	 * configurata (non esiste un record corrispondente nella tabella
	 * PRINT_PRIORITY) PostCondition: Genera una ApplicationException con il
	 * messaggio
	 * "La stampa non � stata configurata correttamente. Avvisare il supporto tecnico."
	 * Normale PreCondition: Nessun'altra precondizione � verificata
	 * PostCondition: Aggiunge la richiesta di stampa alla tabella PRINT_SPOOLER
	 * impostando la priorit� e la descrizione configurate in PRINT_PRIORITY
	 */

	public void addJob(it.cnr.jada.UserContext userContext,
			it.cnr.contab.reports.bulk.Print_spoolerBulk print_spooler,
			it.cnr.jada.bulk.BulkList reportProperties)
			throws it.cnr.jada.comp.ComponentException {
		try {
			try {
				print_spooler.validate();
			} catch (ValidationException e) {
				throw new ApplicationException(e.getMessage());
			}
			LoggableStatement stm = new LoggableStatement(
					getConnection(userContext),
					"SELECT PRIORITY,DS_REPORT FROM "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "PRINT_PRIORITY WHERE REPORT_NAME = ?", true,
					this.getClass());
			try {
				stm.setString(1, print_spooler.getReport());
				java.sql.ResultSet rs = stm.executeQuery();
				try {
					if (!rs.next())
						throw new ApplicationException(
								"La stampa non � stata configurata correttamente. Avvisare il supporto tecnico.");
					print_spooler.setPrioritaServer(new Integer(rs.getInt(1)));
					print_spooler.setUser(userContext.getUser());
					print_spooler.setPriorita(new Integer(0));
					print_spooler.setDsStampa(rs.getString(2));
					print_spooler.setStato(print_spooler.STATO_IN_CODA);
					print_spooler.setDtProssimaEsecuzione(print_spooler
							.getDtPartenza());

					if (print_spooler.getDtPartenza() != null
							&& print_spooler.getEmailCc() != null)
						throw new ApplicationException(
								"Non � possibile inserire il campo E-Mail Cc per le stampe programmate in batch.");
					if (print_spooler.getDtPartenza() != null
							&& print_spooler.getEmailCcn() != null)
						throw new ApplicationException(
								"Non � possibile inserire il campo E-Mail Ccn per le stampe programmate in batch.");

					if (Print_spoolerBulk.TI_VISIBILITA_UTENTE
							.equals(print_spooler.getTiVisibilita()))
						print_spooler.setVisibilita(userContext.getUser());
					else if (Print_spoolerBulk.TI_VISIBILITA_CDR
							.equals(print_spooler.getTiVisibilita())) {
						UtenteBulk utente = (UtenteBulk) getHome(userContext,
								UtenteBulk.class).findByPrimaryKey(
								new UtenteKey(CNRUserContext
										.getUser(userContext)));
						if (utente == null
								|| it.cnr.contab.utenze00.bp.CNRUserContext
										.getCd_cdr(userContext) == null)
							throw new ApplicationException(
									"L'utente non � stato assegnato a nessun CDR quindi non � possibile impostare questo livello di visibilit�.");
						print_spooler
								.setVisibilita(it.cnr.contab.utenze00.bp.CNRUserContext
										.getCd_cdr(userContext));
					} else if (Print_spoolerBulk.TI_VISIBILITA_UNITA_ORGANIZZATIVA
							.equals(print_spooler.getTiVisibilita())) {
						if (CNRUserContext
								.getCd_unita_organizzativa(userContext) == null)
							throw new ApplicationException(
									"L'utente non ha selezionato una unit� organizzativa, quindi non pu� impostare questo livello di visibilit�.");
						print_spooler.setVisibilita(CNRUserContext
								.getCd_unita_organizzativa(userContext));
					} else if (Print_spoolerBulk.TI_VISIBILITA_CDS
							.equals(print_spooler.getTiVisibilita())) {
						if (CNRUserContext.getCd_cds(userContext) == null)
							throw new ApplicationException(
									"L'utente non ha selezionato una unit� organizzativa, quindi non pu� impostare questo livello di visibilit�.");
						print_spooler.setVisibilita(CNRUserContext
								.getCd_cds(userContext));
					}

					checkSQLConstraints(userContext, print_spooler, false, true);
					insertBulk(userContext, print_spooler);
					for (Iterator i = reportProperties.iterator(); i.hasNext();) {
						Print_spooler_paramBulk param = (Print_spooler_paramBulk) i
								.next();
						param.setPgStampa(print_spooler.getPgStampa());
						param.setUser(userContext.getUser());
						insertBulk(userContext, param);
					}
				} finally {
					try {
						rs.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} finally {
				try {
					stm.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	/**
	 * Una o pi� stampe gi� cancellate PreCondition: L'utente ha richiesto la
	 * cancellazione di una o pi� stampe dalla coda di stampa e almeno una di
	 * esse risulta gi� cancellata. PostCondition: Viene generata una
	 * ApplicationException con il messaggio
	 * "Una o pi� stampe sono state cancellate da altri utenti." Una o pi�
	 * stampe in esecuzione PreCondition: L'utente ha richiesto la cancellazione
	 * di una o pi� stampe dalla coda di stampa e almeno una di esse risulta in
	 * esecuzione. PostCondition: Viene generata una ApplicationException con il
	 * messaggio"Una o pi� stampe sono attualmente in esecuzione e non possono essere cancellate."
	 * Normale PreCondition: Nessun'altra precondizione � verificata
	 * PostCondition: Le stampe specificate vengono cancellate dalla coda di
	 * stampa.
	 */

	public void deleteJobs(it.cnr.jada.UserContext userContext,
			Print_spoolerBulk[] print_spooler)
			throws it.cnr.jada.comp.ComponentException {
		try {
			for (int i = 0; i < print_spooler.length; i++) {
				print_spooler[i] = (Print_spoolerBulk) getHome(userContext,
						print_spooler[i]).findAndLock(print_spooler[i]);
				if (print_spooler[i] == null)
					throw new ApplicationException(
							"Una o pi� stampe sono state cancellate da altri utenti.");
				if (Print_spoolerBulk.STATO_IN_ESECUZIONE
						.equals(print_spooler[i].getStato()))
					throw new ApplicationException(
							"Una o pi� stampe sono attualmente in esecuzione e non possono essere cancellate.");
				deleteBulk(userContext, print_spooler[i]);
			}
		} catch (PersistencyException e) {
		} catch (BusyResourceException e) {
		} catch (OutdatedResourceException e) {
			throw handleException(e);
		}
	}

	public void cancellaSchedulazione(it.cnr.jada.UserContext userContext,
			Long pgStampa, String indirizzoEMail)
			throws it.cnr.jada.comp.ComponentException {
		try {
			ArrayList<String> nuoviIndirizzi = new ArrayList<String>();
			StringBuffer bufferIndirizzi = new StringBuffer();
			Print_spoolerBulk printSpooler = (Print_spoolerBulk) getHome(
					userContext, Print_spoolerBulk.class).findByPrimaryKey(
					new Print_spoolerBulk(pgStampa));
			if (printSpooler == null)
				return;
			StringTokenizer indirizzi = new StringTokenizer(printSpooler
					.getEmailA(), ",");
			if (indirizzi.countTokens() == 1
					&& printSpooler.getEmailA()
							.equalsIgnoreCase(indirizzoEMail)) {
				deleteBulk(userContext, printSpooler);
			} else {
				while (indirizzi.hasMoreElements()) {
					String indirizzo = (String) indirizzi.nextElement();
					if (!indirizzo.equalsIgnoreCase(indirizzoEMail))
						nuoviIndirizzi.add(indirizzo);
				}
				for (Iterator<String> iteratorIndirizzi = nuoviIndirizzi
						.iterator(); iteratorIndirizzi.hasNext();) {
					String ind = iteratorIndirizzi.next();
					bufferIndirizzi.append(ind);
					bufferIndirizzi.append(",");
				}
				bufferIndirizzi.deleteCharAt(bufferIndirizzi.length() - 1);
				printSpooler.setEmailA(bufferIndirizzi.toString());
				printSpooler.setToBeUpdated();
				updateBulk(userContext, printSpooler);
			}
			SendMail.getInstance().sendMail(
					"Rimozione dalla lista di distribuzione di SIGLA",
					"Le confermiamo la rimozione dalla lista di distribuzione della \""
							+ printSpooler.getDsStampa() + "\".",
					InternetAddress.parse(indirizzoEMail));
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (AddressException e) {
			throw handleException(e);
		}
	}

	public Print_spoolerBulk findPrintSpooler(
			it.cnr.jada.UserContext userContext, Long pgStampa)
			throws ComponentException {
		try {
			return (Print_spoolerBulk) getHome(userContext,
					Print_spoolerBulk.class).findByPrimaryKey(
					new Print_spoolerBulk(pgStampa));
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	/**
	 * Normale PreCondition: L'utente ha richiesto la composizione della coda di
	 * stampa PostCondition: Viene restituito l'elenco delle stampe presenti
	 * nella coda di stampa compatibili con i criteri di visibilit� specificati
	 * (secondo quanto sepcificato dalla vista "V_PRINT_SPOOLER_VISIBILITA")
	 */

	public it.cnr.jada.util.RemoteIterator queryJobs(
			it.cnr.jada.UserContext userContext, String ti_visibilita)
			throws it.cnr.jada.comp.ComponentException {
		try {
			SQLBuilder sql = getHome(userContext, Print_spoolerBulk.class)
					.createSQLBuilder();

			if (Print_spoolerBulk.TI_VISIBILITA_UTENTE.equals(ti_visibilita))
				sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
						userContext.getUser());
			else if (Print_spoolerBulk.TI_VISIBILITA_CDR.equals(ti_visibilita)) {
				UtenteBulk utente = (UtenteBulk) getHome(userContext,
						UtenteBulk.class).findByPrimaryKey(
						new UtenteKey(CNRUserContext.getUser(userContext)));
				if (utente == null
						|| it.cnr.contab.utenze00.bp.CNRUserContext
								.getCd_cdr(userContext) == null)
					throw new ApplicationException(
							"L'utente non � stato assegnato a nessun CDR quindi non � possibile impostare questo livello di visibilit�.");
				sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
						it.cnr.contab.utenze00.bp.CNRUserContext
								.getCd_cdr(userContext));
			} else if (Print_spoolerBulk.TI_VISIBILITA_UNITA_ORGANIZZATIVA
					.equals(ti_visibilita)) {
				if (CNRUserContext.getCd_unita_organizzativa(userContext) == null)
					throw new ApplicationException(
							"L'utente non ha selezionato una unit� organizzativa, quindi non pu� impostare questo livello di visibilit�.");
				sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
						CNRUserContext.getCd_unita_organizzativa(userContext));
			} else if (Print_spoolerBulk.TI_VISIBILITA_CDS
					.equals(ti_visibilita)) {
				if (CNRUserContext.getCd_cds(userContext) == null)
					throw new ApplicationException(
							"L'utente non ha selezionato una unit� organizzativa, quindi non pu� impostare questo livello di visibilit�.");
				sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
						CNRUserContext.getCd_cds(userContext));
			} else if (Print_spoolerBulk.TI_VISIBILITA_CNR
					.equals(ti_visibilita)) {
				UtenteBulk utente = (UtenteBulk) getHome(userContext,
						UtenteBulk.class).findByPrimaryKey(
						new UtenteKey(CNRUserContext.getUser(userContext)));
				if (utente == null
						|| it.cnr.contab.utenze00.bp.CNRUserContext
								.getCd_cdr(userContext) == null)
					throw new ApplicationException(
							"Utente non abilitato per il livello di visibilit� CNR.");
				CdrBulk cdr = (CdrBulk) getHome(userContext, CdrBulk.class)
						.findByPrimaryKey(
								new CdrKey(
										it.cnr.contab.utenze00.bp.CNRUserContext
												.getCd_cdr(userContext)));
				if (utente == null
						|| it.cnr.contab.utenze00.bp.CNRUserContext
								.getCd_cdr(userContext) == null)
					throw new ApplicationException(
							"Utente non abilitato per il livello di visibilit� CNR.");
				getHomeCache(userContext).fetchAll(userContext);
				if (!Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equals(cdr
						.getUnita_padre().getUnita_padre().getCd_tipo_unita()))
					throw new ApplicationException(
							"Utente non abilitato per il livello di visibilit� CNR.");
			}

			sql.addClause("and", "ti_visibilita", sql.EQUALS, ti_visibilita);

			sql.addOrderBy("DACR DESC");

			return iterator(userContext, sql, Print_spoolerBulk.class, null);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public String getLastServerActive(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		Print_spoolerHome printHome = (Print_spoolerHome) getHome(userContext, Print_spoolerBulk.class);
		try {
			return printHome.getLastServerActive();
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (BusyResourceException e) {
			throw handleException(e);
		}
	}
}
