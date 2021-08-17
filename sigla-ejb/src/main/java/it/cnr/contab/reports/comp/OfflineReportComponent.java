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

package it.cnr.contab.reports.comp;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrKey;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.reports.bulk.*;
import it.cnr.contab.reports.service.dataSource.PrintDataSourceOffline;
import it.cnr.contab.reports.util.UtilReports;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenericComponent;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.SendMail;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.rmi.RemoteException;
import java.util.*;

public class OfflineReportComponent extends GenericComponent implements
        IOfflineReportMgr {
    /**
     * OfflineReportComponent constructor comment.
     */
    public OfflineReportComponent() {
        super();
    }

    /**
     * Stampa non configurata PreCondition: La stampa specificata non è stata
     * configurata (non esiste un record corrispondente nella tabella
     * PRINT_PRIORITY) PostCondition: Genera una ApplicationException con il
     * messaggio
     * "La stampa non è stata configurata correttamente. Avvisare il supporto tecnico."
     * Normale PreCondition: Nessun'altra precondizione è verificata
     * PostCondition: Aggiunge la richiesta di stampa alla tabella PRINT_SPOOLER
     * impostando la priorità e la descrizione configurate in PRINT_PRIORITY
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

            Print_priorityBulk print_priority=findPrintPriority(userContext,print_spooler.getReport());
            print_spooler.setPriorita(new Integer(0));

            if ( print_priority==null)
                throw new ApplicationException(
                        "La stampa non è stata configurata correttamente. Avvisare il supporto tecnico.");
            print_spooler.setReport(UtilReports.getReportNameToRun(print_priority,print_spooler));
            if (print_spooler.getIntervallo() != null && print_spooler.getTiIntervallo() != null) {
                /**
                 * Per le stampe schedulate viene impostata un priorità fissa a 4
                 */
                print_spooler.setPrioritaServer(4);
            } else {
                print_spooler.setPrioritaServer(print_priority.getPriority());
            }
            print_spooler.setUser(userContext.getUser());
            print_spooler.setDsStampa(print_priority.getDsReport());
            if ( !Optional.ofNullable(print_spooler.getStato()).isPresent())
                print_spooler.setStato(print_spooler.STATO_IN_CODA);
            print_spooler.setDtProssimaEsecuzione(print_spooler
                    .getDtPartenza());

            if (print_spooler.getDtPartenza() != null
                    && print_spooler.getEmailCc() != null)
                throw new ApplicationException(
                        "Non è possibile inserire il campo E-Mail Cc per le stampe programmate in batch.");
            if (print_spooler.getDtPartenza() != null
                    && print_spooler.getEmailCcn() != null)
                throw new ApplicationException(
                        "Non è possibile inserire il campo E-Mail Ccn per le stampe programmate in batch.");

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
                            "L'utente non è stato assegnato a nessun CDR quindi non è possibile impostare questo livello di visibilità.");
                print_spooler
                        .setVisibilita(it.cnr.contab.utenze00.bp.CNRUserContext
                                .getCd_cdr(userContext));
            } else if (Print_spoolerBulk.TI_VISIBILITA_UNITA_ORGANIZZATIVA
                    .equals(print_spooler.getTiVisibilita())) {
                if (CNRUserContext
                        .getCd_unita_organizzativa(userContext) == null)
                    throw new ApplicationException(
                            "L'utente non ha selezionato una unità organizzativa, quindi non può impostare questo livello di visibilità.");
                print_spooler.setVisibilita(CNRUserContext
                        .getCd_unita_organizzativa(userContext));
            } else if (Print_spoolerBulk.TI_VISIBILITA_CDS
                    .equals(print_spooler.getTiVisibilita())) {
                if (CNRUserContext.getCd_cds(userContext) == null)
                    throw new ApplicationException(
                            "L'utente non ha selezionato una unità organizzativa, quindi non può impostare questo livello di visibilità.");
                print_spooler.setVisibilita(CNRUserContext
                        .getCd_cds(userContext));
            }

            checkSQLConstraints(userContext, print_spooler, false, true);
            insertBulk(userContext, print_spooler);
            for (Iterator i = reportProperties.iterator(); i.hasNext(); ) {
                Print_spooler_paramBulk param = (Print_spooler_paramBulk) i
                        .next();
                param.setPgStampa(print_spooler.getPgStampa());
                param.setUser(userContext.getUser());
                insertBulk(userContext, param);
            }

        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Una o più stampe già cancellate PreCondition: L'utente ha richiesto la
     * cancellazione di una o più stampe dalla coda di stampa e almeno una di
     * esse risulta già cancellata. PostCondition: Viene generata una
     * ApplicationException con il messaggio
     * "Una o più stampe sono state cancellate da altri utenti." Una o più
     * stampe in esecuzione PreCondition: L'utente ha richiesto la cancellazione
     * di una o più stampe dalla coda di stampa e almeno una di esse risulta in
     * esecuzione. PostCondition: Viene generata una ApplicationException con il
     * messaggio"Una o più stampe sono attualmente in esecuzione e non possono essere cancellate."
     * Normale PreCondition: Nessun'altra precondizione è verificata
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
                            "Una o più stampe sono state cancellate da altri utenti.");
                if (Print_spoolerBulk.STATO_IN_ESECUZIONE
                        .equals(print_spooler[i].getStato()))
                    throw new ApplicationException(
                            "Una o più stampe sono attualmente in esecuzione e non possono essere cancellate.");
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
                        .iterator(); iteratorIndirizzi.hasNext(); ) {
                    String ind = iteratorIndirizzi.next();
                    bufferIndirizzi.append(ind);
                    bufferIndirizzi.append(",");
                }
                bufferIndirizzi.deleteCharAt(bufferIndirizzi.length() - 1);
                printSpooler.setEmailA(bufferIndirizzi.toString());
                printSpooler.setToBeUpdated();
                updateBulk(userContext, printSpooler);
            }
            SendMail.sendMail(
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
    public Print_priorityBulk findPrintPriority(
            it.cnr.jada.UserContext userContext, String reportName)
            throws ComponentException {
        try {
            return ( Print_priorityBulk) getHome(userContext,
                    Print_priorityBulk.class).findByPrimaryKey(
                    new Print_priorityBulk(reportName));

        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale PreCondition: L'utente ha richiesto la composizione della coda di
     * stampa PostCondition: Viene restituito l'elenco delle stampe presenti
     * nella coda di stampa compatibili con i criteri di visibilità specificati
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
                            "L'utente non è stato assegnato a nessun CDR quindi non è possibile impostare questo livello di visibilità.");
                sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
                        it.cnr.contab.utenze00.bp.CNRUserContext
                                .getCd_cdr(userContext));
            } else if (Print_spoolerBulk.TI_VISIBILITA_UNITA_ORGANIZZATIVA
                    .equals(ti_visibilita)) {
                if (CNRUserContext.getCd_unita_organizzativa(userContext) == null)
                    throw new ApplicationException(
                            "L'utente non ha selezionato una unità organizzativa, quindi non può impostare questo livello di visibilità.");
                sql.addClause("and", "visibilita", SQLBuilder.EQUALS,
                        CNRUserContext.getCd_unita_organizzativa(userContext));
            } else if (Print_spoolerBulk.TI_VISIBILITA_CDS
                    .equals(ti_visibilita)) {
                if (CNRUserContext.getCd_cds(userContext) == null)
                    throw new ApplicationException(
                            "L'utente non ha selezionato una unità organizzativa, quindi non può impostare questo livello di visibilità.");
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
                            "Utente non abilitato per il livello di visibilità Ente.");
                CdrBulk cdr = (CdrBulk) getHome(userContext, CdrBulk.class)
                        .findByPrimaryKey(
                                new CdrKey(
                                        it.cnr.contab.utenze00.bp.CNRUserContext
                                                .getCd_cdr(userContext)));
                if (utente == null
                        || it.cnr.contab.utenze00.bp.CNRUserContext
                        .getCd_cdr(userContext) == null)
                    throw new ApplicationException(
                            "Utente non abilitato per il livello di visibilità Ente.");
                getHomeCache(userContext).fetchAll(userContext);
                if (!Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equals(cdr
                        .getUnita_padre().getUnita_padre().getCd_tipo_unita()))
                    throw new ApplicationException(
                            "Utente non abilitato per il livello di visibilità Ente.");
            }
            sql.addClause("and", "tiVisibilita", sql.EQUALS, ti_visibilita);

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

    public Boolean controllaStampeInCoda(
            it.cnr.jada.UserContext userContext, it.cnr.contab.reports.bulk.Print_spoolerBulk stampa)
            throws it.cnr.jada.comp.ComponentException, RemoteException {
        try {
            LoggableStatement stm = new LoggableStatement(
                    getConnection(userContext),
                    "SELECT REPORT,STATO,UTCR FROM "
                            + it.cnr.jada.util.ejb.EJBCommonServices
                            .getDefaultSchema()
                            + "PRINT_SPOOLER WHERE REPORT = ? AND"
                            + " (STATO = ? OR"
                            + " STATO = ? ) AND"
                            + " UTCR = ?", true, this.getClass());
            try {
                stm.setString(1, stampa.getReport());
                stm.setString(2, Print_spoolerBulk.STATO_IN_CODA);
                stm.setString(3, Print_spoolerBulk.STATO_IN_ESECUZIONE);
                stm.setString(4, stampa.getUser());
                java.sql.ResultSet rs = stm.executeQuery();
                try {
                    if (rs.next())
                        return true;
                } catch (Throwable e) {
                    throw handleException(e);
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
        }
        return false;
    }

    public Print_spoolerBulk getJobWaitToJsoDS(UserContext userContext) throws ComponentException {
        Print_spoolerHome printHome = (Print_spoolerHome) getHome(userContext, Print_spoolerBulk.class);
        try {
            final List<Print_spoolerBulk> list = printHome.fetchAll(printHome.getJobWaitToJsoDS());

            Print_spoolerBulk p= Optional.ofNullable(list).filter(l2-> !l2.isEmpty())
                    .map(l -> l.get(0))
                    .orElse(null);
            if ( p!=null){
                //recupera i params chiedere a Marco se vogliamo modificare Print_spoolerBulk per recuperarli sempre
                Print_spooler_paramHome paramsHome = (Print_spooler_paramHome) getHome(userContext,Print_spooler_paramBulk.class);
                BulkList b = paramsHome.getParamsFromPgStampa( p.getPgStampa());
                if ( b!=null)
                    p.setParams(b);

            }
            return p;
        } catch (PersistencyException | BusyResourceException e) {
            throw handleException(e);
        }
    }
    public Print_spoolerBulk getPrintSpoolerDsOffLine(UserContext userContext, Print_spoolerBulk printSpoller, PrintDataSourceOffline printDsOffLine) throws ComponentException, RemoteException {
        return printDsOffLine.getPrintSpooler(userContext,printSpoller,
                getHome( userContext,printDsOffLine.getBulkClass()));
    }
}
