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

package it.cnr.contab.missioni00.comp;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.CambioHome;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaHome;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (23/11/2001 15.02.34)
 *
 * @author: Paola sala
 */

public class AnticipoComponent extends it.cnr.jada.comp.CRUDComponent implements IAnticipoMgr, Cloneable, Serializable {
    /**
     * MissioneComponent constructor comment.
     */
    public AnticipoComponent() {
        super();
    }

    private void aggiornaCogeCoan(
            UserContext userContext,
            AnticipoBulk anticipo,
            IDocumentoContabileBulk docCont)
            throws ComponentException {

        try {
            if (docCont != null && anticipo != null && anticipo.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = anticipo.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) anticipo.getDefferredSaldi().get(key);

                    //caso di creazione o di nessuna modifica sui doc cont
                    if (values == null) return;

                    //QUI chiamare component del documento contabile interessato
                    String jndiName = null;
                    Class clazz = null;
                    DocumentoContabileComponentSession session = null;
                    if (docCont instanceof ObbligazioneBulk) {
                        jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
                        clazz = ObbligazioneAbstractComponentSession.class;
                        session =
                                (ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                        jndiName, clazz);
                    } else if (docCont instanceof AccertamentoBulk) {
                        jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
                        clazz = AccertamentoAbstractComponentSession.class;
                        session =
                                (AccertamentoAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                        jndiName, clazz);
                    }
                    if (session != null) {
                        session.aggiornaCogeCoanInDifferita(userContext, key, values);
                        anticipo.getDefferredSaldi().remove(key);
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(anticipo, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(anticipo, e);
        }
    }

    private void aggiornaCogeCoanDocAmm(
            UserContext userContext,
            AnticipoBulk anticipo)
            throws ComponentException {

        if (anticipo == null) return;

        aggiornaCogeCoanObbligazioniDaCancellare(userContext, anticipo);
        aggiornaCogeCoanObbligazioni(userContext, anticipo);
    }

    private void aggiornaCogeCoanObbligazioni(
            UserContext userContext,
            AnticipoBulk anticipo)
            throws ComponentException {

        if (anticipo != null) {
            ObbligazioniTable obbligazioniHash = anticipo.getObbligazioniHash();
            if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {

                //Aggiorna coge coan per le obbligazioni NON temporanee
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable) obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaCogeCoan(
                            userContext,
                            anticipo,
                            (IDocumentoContabileBulk) e.nextElement());

            }
        }
    }

    private void aggiornaCogeCoanObbligazioniDaCancellare(
            UserContext userContext,
            AnticipoBulk anticipo)
            throws ComponentException {

        if (anticipo != null) {
            if (anticipo.getDocumentiContabiliCancellati() != null &&
                    !anticipo.getDocumentiContabiliCancellati().isEmpty() &&
                    anticipo.getObbligazioniHash() != null) {

                for (java.util.Enumeration e = anticipo.getDocumentiContabiliCancellati().elements(); e.hasMoreElements(); ) {
                    OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                    if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
                        Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) oggettoBulk;
                        if (!scadenza.getObbligazione().isTemporaneo()) {
                            PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, anticipo.getObbligazioniHash().keys());
                            if (!obbligs.containsKey(scadenza.getObbligazione()))
                                aggiornaCogeCoan(
                                        userContext,
                                        anticipo,
                                        scadenza.getObbligazione());
                        }
                    }
                }
            }
        }
    }

    /**
     * Aggiorna obbligazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna obbligazione temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di un anticipo
     * L'utente ha creato una nuova obbligazione nel contesto transazionale dell'anticipo
     * Post: Il sistema assegna un progressivo definitivo all'obbligazione (metodo 'aggiornaObbligazioneTemporanea'),
     * aggiorna l'importo associato a doc. amministrativi della scadenza di obbligazione, aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     * <p>
     * Nome: Aggiorna obbligazione non temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di un anticipo
     * L'utente ha selezionato una scadenza di obbligazione già esistente
     * Post: Il sistema aggiorna l'importo associato a doc. amministrativi della scadenza di obbligazione e aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     * <p>
     * Nome: Elimina associazione con obbligazione
     * Pre:  L'utente ha richiesto la modifica di un anticipo
     * L'utente ha eliminato un'associazione con una scadenza di obbligazione
     * Post: Il sistema azzera l'importo associato a doc. amministrativi della scadenza di obbligazione e aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    anticipo        l'AnticipoBulk da creare/modificare
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */
    private void aggiornaObbligazione(UserContext userContext, AnticipoBulk anticipo, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws ComponentException {
        Obbligazione_scadenzarioBulk scadenza = anticipo.getScadenza_obbligazione();

        if ((scadenza == null) || (scadenza.getPg_obbligazione_scadenzario() == null) ||
                (scadenza.getPg_obbligazione() == null) || (scadenza.getEsercizio_originale() == null))
            return;            // L'anticipo avra' sempre una obbligazione

        // Ciclo sulle scadenze precedentementi associate all'anticipo.
        // Se nuove le inserisco comunque in tabella con "im_associato_doc_amm" nullo e ne aggiorno i saldi
        // Se non nuove le aggiorno azzerando il loro "im_associato_doc_amm"
        Obbligazione_scadenzarioBulk aScadenzaNonAssociata;
        if (anticipo.getDocumentiContabiliCancellati() != null) {
            for (Iterator i = anticipo.getDocumentiContabiliCancellati().iterator(); i.hasNext(); ) {
                aScadenzaNonAssociata = (Obbligazione_scadenzarioBulk) i.next();
                if (!aScadenzaNonAssociata.getObbligazione().equalsByPrimaryKey(scadenza.getObbligazione())) {
                    // se la scadenza appartiene alla stessa obbligazione della scadenza associata all'anticipo
                    // non aggiornare i saldi altrimenti lo faresti due volte (l'aggiornamento dei saldi va per
                    // obbligazione e non per scadenze)
                    aggiornaSaldi(userContext, anticipo, aScadenzaNonAssociata.getObbligazione(), status);

                    if (aScadenzaNonAssociata.getObbligazione().isTemporaneo())
                        aggiornaObbligazioneTemporanea(userContext, aScadenzaNonAssociata.getObbligazione());
                }

                aScadenzaNonAssociata.setIm_associato_doc_amm((new BigDecimal(0)));
                updateImportoAssociatoDocAmm(userContext, aScadenzaNonAssociata);
            }
        }
        // Se la scadenza era gia' stata creata e non e' stata modificata
        // il metodo non aggiornera' alcun saldo
        aggiornaSaldi(userContext, anticipo, scadenza.getObbligazione(), status);

        if (scadenza.getObbligazione().isTemporaneo())
            aggiornaObbligazioneTemporanea(userContext, scadenza.getObbligazione());

        scadenza.setIm_associato_doc_amm(anticipo.getIm_anticipo_divisa());
        updateImportoAssociatoDocAmm(userContext, scadenza);

        if ((anticipo.getDocumentiContabiliCancellati() == null) ||
                (anticipo.getDocumentiContabiliCancellati().isEmpty()))
            return;
    }

    /**
     * Aggiorna obbligazione temporanea
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna obbligazione temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di un anticipo
     * L'utente ha creato una nuova obbligazione nel contesto transazionale dell'anticipo e pertanto
     * questa obbligazione ha un progressivo temporaneo
     * Post: Il sistema assegna la numerazione definitiva all'obbligazione creata nel contesto dell'anticipo
     *
     * @param    userContext                lo UserContext che ha generato la richiesta
     * @param    obbligazioneTemporanea    l'ObbligazioneBulk con numerazione temporanea da rendere definitiva
     */
    private void aggiornaObbligazioneTemporanea(UserContext userContext, ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {
        try {
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
            Long pg = null;
            pg = numHome.getNextPg(userContext,
                    obbligazioneTemporanea.getEsercizio(),
                    obbligazioneTemporanea.getCd_cds(),
                    obbligazioneTemporanea.getCd_tipo_documento_cont(),
                    obbligazioneTemporanea.getUser());

            ObbligazioneHome home = (ObbligazioneHome) getHome(userContext, obbligazioneTemporanea);
            home.confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(obbligazioneTemporanea, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(obbligazioneTemporanea, e);
        }
    }

    /**
     * Aggiorna saldi obbligazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna saldi obbligazione
     * Pre:  L'utente ha richiesto l'inserimento/modifica di un anticipo
     * L'utente ha associato una obbligazione all'anticipo
     * Post: Il sistema richiede alla Component che gestisce l'Obbligazione l'aggiornamento in differita dei saldi
     *
     * @param docCont il documento contabile di tipo ObbligazioneBulk per cui chiedere l'aggiornamento dei saldi
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    anticipo        l'AnticipoBulk a cui e' stata associata una scadenza di obbligazione
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    private void aggiornaSaldi(it.cnr.jada.UserContext userContext, AnticipoBulk anticipo, IDocumentoContabileBulk docCont, OptionRequestParameter status) throws ComponentException {
        try {
            if (docCont != null && anticipo != null && anticipo.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = anticipo.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) anticipo.getDefferredSaldi().get(key);

                    if (values != null) {
                        //QUI chiamare component del documento contabile interessato
                        String jndiName = null;
                        Class clazz = null;
                        it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession session = null;
                        if (docCont instanceof ObbligazioneBulk) {
                            jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
                            clazz = it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class;
                            session = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(jndiName, clazz);
                        }
                        if (session != null) {
                            session.aggiornaSaldiInDifferita(userContext, key, values, status);
                            //NON Differibile: si rischia di riprocessare i saldi impropriamente
                            anticipo.getDefferredSaldi().remove(key);
                        }
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(anticipo, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(anticipo, e);
        }
    }

    /**
     * Carica i dati relativi al terzo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica terzo
     * Pre:  Il sistema carica in modifica un anticipo selezionato
     * Post: Il sistema carica i dati relativi al terzo dell'anticipo, comprensivi dei termini
     * e delle modalità di pagamento
     * <p>
     * Nome: Carica terzo - errore
     * Pre:  Il sistema non trova il terzo da caricare per l'anticipo che si sta aprendo in modifica
     * Post: Una segnalazione di errore viene restituita all'utente
     *
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    anticipo    l'AnticipoBulk da caricare
     */
    private void caricaTerzoInModificaAnticipo(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            anticipo.setV_terzo(new V_terzo_per_compensoBulk());
            SQLBuilder sql = selectV_terzoByClause(userContext, anticipo, anticipo.getV_terzo(), null);
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, anticipo.getCd_terzo());

            V_terzo_per_compensoHome terzoHome = (V_terzo_per_compensoHome) getHome(userContext, V_terzo_per_compensoBulk.class);

            List terzi = terzoHome.fetchAll(sql);
            if (terzi == null || terzi.isEmpty())
                throw new ApplicationException("Attenzione, il terzo associato all'anticipo non esiste !");

            anticipo.setV_terzo((V_terzo_per_compensoBulk) terzi.get(0));
            anticipo.setTermini(findTermini(userContext, anticipo));
            anticipo.setModalita(findModalita(userContext, anticipo));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle scadenze di obbligazioni eleggibili all'anticipo
     * PostCondition:
     * Il sistema fornisce le scadenze di obbligazioni ove:
     * - l'esercizio dell'obbligazione e' uguale a quello di scrivania
     * - l'obbligazione e' definitiva
     * - l'obbligazione non e' stata cancellata logicamente
     * - l'obbligazione non e' una partita di giro
     * - la scadenza non ha importo nullo
     * - l'importo associato a documento amministrativo della scadenza e' nullo (oppure 0)
     * - l'importo associato a documento contabile della scadenza e' nullo (oppure 0)
     * - l'unita' organizzativa dell'obbligazione e' uguale a quella dell'anticipo
     * - l'obbligazione non e' riportata
     * - il terzo dell'obbligazione e' uguale a quello dell'anticipo
     * Possono essere aggiunte altre condizioni di ricerca se l'utente le ha specificate
     * nella finestra di ricerca delle obbligazioni. Tali condizioni sono relative al terzo, alla
     * data di scadenza, all'importo della scadenza, al progressivo dell'obbligazione e all'elemento
     * voce dell'obbligazione
     */
    public RemoteIterator cercaObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro)
            throws ComponentException {

        Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(context, Obbligazione_scadenzarioBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.setDistinctClause(true);
        sql.addTableToHeader("OBBLIGAZIONE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");

        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", SQLBuilder.EQUALS, "D");
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", SQLBuilder.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE.FL_PGIRO", SQLBuilder.NOT_EQUALS, "Y");
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", SQLBuilder.NOT_EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, filtro.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", SQLBuilder.EQUALS, "N");

        if (filtro.getElemento_voce() != null) {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_ELEMENTO_VOCE", SQLBuilder.STARTSWITH, filtro.getElemento_voce().getCd_elemento_voce());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_APPARTENENZA", SQLBuilder.EQUALS, filtro.getElemento_voce().getTi_appartenenza());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_GESTIONE", SQLBuilder.EQUALS, filtro.getElemento_voce().getTi_gestione());
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", SQLBuilder.EQUALS, filtro.getElemento_voce().getEsercizio());
        }

        if (!filtro.getFl_fornitore().booleanValue()) {
            sql.addTableToHeader("TERZO");
            sql.addTableToHeader("ANAGRAFICO");
            sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
            sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "(OBBLIGAZIONE.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
            sql.addParameter(filtro.getFornitore().getCd_terzo(), java.sql.Types.INTEGER, 0);
            sql.addParameter(AnagraficoBulk.DIVERSI, java.sql.Types.VARCHAR, 0);
        } else {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", SQLBuilder.EQUALS, filtro.getFornitore().getCd_terzo());
        }

        if (filtro.getFl_data_scadenziario().booleanValue() && filtro.getData_scadenziario() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA", SQLBuilder.EQUALS, filtro.getData_scadenziario());
        if (filtro.getFl_importo().booleanValue() && filtro.getIm_importo() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", SQLBuilder.GREATER_EQUALS, filtro.getIm_importo());

        //filtro su Tipo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getTipo_obbligazione() != null) {
            if (ObbligazioneBulk.TIPO_COMPETENZA.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
            else if (ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
            else if (ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
        }

        //filtro su Anno Residuo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getEsercizio_ori_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE", SQLBuilder.EQUALS, filtro.getEsercizio_ori_obbligazione());

        //filtro su Numero obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getNr_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.PG_OBBLIGAZIONE", SQLBuilder.EQUALS, filtro.getNr_obbligazione());

        return iterator(
                context,
                sql,
                Obbligazione_scadenzarioBulk.class,
                "default");
    }

    /**
     * Completa i dati relativi al terzo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Completa terzo
     * Pre:  L'utente ha selezionato un nuovo terzo per l'anticipo
     * Post: Il sistema valorizza tutti i dati relativi all'anagrafico associato al terzo selezionato, in particolare
     * nome, cognome, ragione sociale, codice fiscale, partita iva, modalita e termini di pagamento
     *
     * @return l'AnticipoBulk con i dati relativi al terzo inizializzati
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    anticipo    l'AnticipoBulk per cui e' stato selezionato un nuovo terzo
     * @param    aTerzo        il terzo di tipo V_terzo_per_compensoBulk selezionato dall'utente
     */
    public AnticipoBulk completaTerzo(UserContext uc, AnticipoBulk anticipo, V_terzo_per_compensoBulk aTerzo) throws ComponentException {
        if (anticipo != null) {
            anticipo.setV_terzo(aTerzo);
            anticipo.setCd_terzo(aTerzo.getCd_terzo());
            anticipo.setNome(aTerzo.getNome());
            anticipo.setCognome(aTerzo.getCognome());
            anticipo.setRagione_sociale(aTerzo.getRagione_sociale());
            anticipo.setCodice_fiscale(aTerzo.getCodice_fiscale());
            anticipo.setPartita_iva(aTerzo.getPartita_iva());
            anticipo.setTermini(findTermini(uc, anticipo));
            anticipo.setModalita(findModalita(uc, anticipo));
        }

        return anticipo;
    }

    /**
     * Creazione anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Crea anticipo
     * Pre:  L'utente ha completato l'inserimento dei dati relativi ad un nuovo anticipo e ne chiede il salvataggio
     * Post: Il sistema rende persistente l'anticipo creato
     *
     * @return l'AnticipoBulk creato e salvato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'AnticipoBulk per cui e' stato richiesta la creazione
     */

    public OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return creaConBulk(userContext, bulk, null);
    }

    /**
     * Crea l'anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Validazione della scadenza di obbligazione associata all'anticipo
     * Pre:  La scadenza legata all'anticipo passa la validazione
     * Post: Il sistema prosegue con la creazione dell'anticipo
     * <p>
     * Nome: Aggiornamento dell'obbligazione
     * Pre:  L'utente richiede il salvataggio di un anticipo con obbligazione
     * Post: Il sistema aggiorna l' "importo associato a documento amministrativo" della scadenza associata
     * all'anticipo ed aggiorna anche i saldi.
     * Se l'utente ha creato delle scadenze senza associarle all'anticipo il sistema provvede comunque
     * all'aggiornamento dei saldi.
     *
     * @return l'AnticipoBulk salvato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'AnticipoBulk per cui e' stato richiesta la creazione
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    public OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk, OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;

        // Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        if (anticipo.getDefferredSaldi() != null)
            aTempDiffSaldi = (PrimaryKeyHashMap) anticipo.getDefferredSaldi().clone();

        validaObbligazione(userContext, anticipo.getScadenza_obbligazione(), anticipo);

        aggiornaObbligazione(userContext, anticipo, status);
        anticipo.setStato_cofi(AnticipoBulk.STATO_CONTABILIZZATO);

        anticipo = (AnticipoBulk) super.creaConBulk(userContext, anticipo);

        // Restore dell'hash map dei saldi
        if (anticipo.getDefferredSaldi() != null)
            anticipo.getDefferredSaldi().putAll(aTempDiffSaldi);
        aggiornaCogeCoanDocAmm(userContext, anticipo);

        if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(anticipo.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");

        return anticipo;
    }

    /**
     * Creazione Rimborso Anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Crea rimborso
     * Pre:  Una richiesta di creazione di un rimborso completo di un anticipo è stata generata
     * L'anticipo e' in stato pagato
     * L'utente ha selezionato la linea di attività per cui creare l'accertamento su cui contabilizzare il rimborso
     * Post: Viene chiamata la stored procedure che
     * - crea un accertamento nel bilancio del CNR con voce del piano uguale
     * a quella specificata in Configurazione CNR e linea di attività uguale a quella specificata dall'utente
     * - crea un rimborso contabilizzato sull'accertamento precedente
     * <p>
     * Nome: Crea rimborso - errore
     * Pre:  Una richiesta di creazione di un rimborso completo di un anticipo è stata generata
     * L'utente non ha selezionato una linea di attività per cui creare l'accertamento su cui contabilizzare il rimborso
     * Post: Una segnalazione di errore viene restituita all'utente
     *
     * @return l'AnticipoBulk con il rimborso associato
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    anticipo l' AnticipoBulk per cui creare il rimborso
     */

    public AnticipoBulk creaRimborsoCompleto(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            if (anticipo.getCd_linea_attivita() == null)
                throw new ApplicationException("E' necessario specificare un GAE per il rimborso");

            LoggableStatement cs = new LoggableStatement(getConnection(userContext),
                    "{  call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB580.rimborsoCompletoAnticipo(?, ?, ?, ?, ?, ?)}", false, this.getClass());
            try {
                cs.setString(1, anticipo.getCd_cds());
                cs.setObject(2, anticipo.getEsercizio());
                cs.setString(3, anticipo.getCd_unita_organizzativa());
                cs.setObject(4, anticipo.getPg_anticipo());
                cs.setObject(5, CNRUserContext.getEsercizio(userContext));
                cs.setString(6, userContext.getUser());
                cs.executeQuery();
                return anticipo;
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                cs.close();
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
     *
     * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code>
     * che serve per leggere i parametri di configurazione del CNR
     */
    private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException {
        try {
            return (Configurazione_cnrComponentSession) EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Cancellazione logica anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cancellazione logica anticipo
     * Pre:  L'utente ha selezionato elimina anticipo
     * Post: Il sistema cancella logicamente l' anticipo e se l'anticipo risulta
     * contabilizzato in Coge/Coan riporto i relativi stati ad "R"
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l' AnticipoBulk da cancellare
     */
    private void deleteLogically(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            anticipo.setToBeUpdated();

            // ************
            //	Se l'esercizio di scrivania e' diverso da quello solare
            //	inizializzo la data di cancellazione al 31/12/esercizio anticipo
            java.sql.Timestamp tsOdierno = getHome(userContext, anticipo).getServerDate();
            GregorianCalendar tsOdiernoGregorian = anticipo.getGregorianCalendar(tsOdierno);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

            if (tsOdiernoGregorian.get(GregorianCalendar.YEAR) != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
                anticipo.setDt_cancellazione(new java.sql.Timestamp(sdf.parse("31/12/" + anticipo.getEsercizio().intValue()).getTime()));
            else {
                String currentDate = tsOdiernoGregorian.get(GregorianCalendar.DAY_OF_MONTH) + "/" +
						tsOdiernoGregorian.get(GregorianCalendar.MONTH) + "/" +
						tsOdiernoGregorian.get(GregorianCalendar.YEAR);
                anticipo.setDt_cancellazione(new java.sql.Timestamp(sdf.parse(currentDate).getTime()));
            }
            // ************

            anticipo.setStato_cofi(AnticipoBulk.STATO_ANNULLATO);

            if (AnticipoBulk.STATO_CONTABILIZZATO_COAN.equals(anticipo.getStato_coan()))
                anticipo.setStato_coan(AnticipoBulk.STATO_RICONTABILIZZARE_COAN);
            if (AnticipoBulk.STATO_CONTABILIZZATO_COGE.equals(anticipo.getStato_coge()))
                anticipo.setStato_coge(AnticipoBulk.STATO_RICONTABILIZZARE_COGE);

            updateBulk(userContext, anticipo);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Cancellazione anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: 	cancellazione anticipo
     * Pre:  	L'utente vuole eliminare un anticipo
     * Post: 	Il sistema scollega la scadenza di obbligazione dell'anticipo azzerandone
     * l'importo associato a documento amministrativo
     * <p>
     * Nome: 	cancellazione fisica anticipo
     * Pre:  	L'utente vuole eliminare un anticipo non associato a mandato, non contabilizzato in Coge/Coan e
     * la cui obbligazione non e' stata riportata
     * Post: 	Il sistema consente l'eliminazione fisica dell'anticipo
     * <p>
     * Nome: 	cancellazione logica anticipo
     * Pre:  	L'utente vuole eliminare un anticipo o associato a mandato o contabilizzato in Coge/Coan o la cui
     * obbligazione e' stata riportata
     * Post: 	Il sistema consente l'eliminazione logica dell'anticipo
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l' AnticipoBulk da cancellare
     */

    public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;

        try {

            // Controllo dello stato dell'es COEP prec. per compensi riportati Da ese. prec. (isRiportataInScrivania())
            validateEsercizioCOEP(aUC, anticipo);

            // Salvo temporaneamente l'hash map dei saldi
            PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
            if (anticipo.getDefferredSaldi() != null)
                aTempDiffSaldi = (PrimaryKeyHashMap) anticipo.getDefferredSaldi().clone();

            //	Scollego la scadenza di obbligazione dell'anticipo
            Obbligazione_scadenzarioBulk scadenza = anticipo.getScadenza_obbligazione();
            if (scadenza != null) {
                scadenza.setIm_associato_doc_amm(new BigDecimal(0));
                updateImportoAssociatoDocAmm(aUC, scadenza);
            }

            //	Cancello logicamente/fisicamente l'anticipo
            if (AnticipoBulk.STATO_INIZIALE_MANREV.equals(anticipo.getTi_associato_manrev()) &&
                    AnticipoBulk.STATO_INIZIALE_COGE.equals(anticipo.getStato_coge()) &&
                    AnticipoBulk.STATO_INIZIALE_COAN.equals(anticipo.getStato_coan()) &&
                    !anticipo.isRiportata())
                super.eliminaConBulk(aUC, anticipo);
            else
                deleteLogically(aUC, anticipo);

            // Restore dell'hash map dei saldi
            if (anticipo.getDefferredSaldi() != null)
                anticipo.getDefferredSaldi().putAll(aTempDiffSaldi);

            aggiornaCogeCoanDocAmm(aUC, anticipo);

            if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(anticipo.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Carica i dati relativi al cambio
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica cambio
     * Pre:  E' stata generata una richiesta di caricamento di un cambio associato ad una certa divisa e valido
     * in una certa data
     * Post: Il sistema restituisce il cambio valido per la divisa data
     *
     * @return il CambioBulk trovato oppure null se non esiste nessun cambio valido per quella divisa in quella data
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    divisa    la DivisaBulk per cui ricercare il cambio
     * @param    dataCambio    la data per cui il cambio deve essere valido
     */

    private CambioBulk findCambio(UserContext uc, DivisaBulk divisa, java.sql.Timestamp dataCambio) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        CambioHome cambioHome = (CambioHome) getHome(uc, CambioBulk.class);
        CambioBulk aCambio = cambioHome.getCambio(divisa, dataCambio);

		return aCambio;
    }

    /**
     * Carica i dati relativi alla divisa di default
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica divisa
     * Pre:  E' stata generata una richiesta di caricamento della divisa di default (EURO)
     * Post: Il sistema restituisce la divisa di default
     *
     * @return la DivisaBulk di default oppure null se non esiste nessuna divisa di default
     * @param    uc    lo UserContext che ha generato la richiesta
     */

    private DivisaBulk findDivisaDefault(UserContext aUC) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, RemoteException {
        DivisaHome divisaHome = (DivisaHome) getHome(aUC, DivisaBulk.class);
        DivisaBulk divisaDefault = divisaHome.getDivisaDefault(aUC);

        return divisaDefault;
    }

    /**
     * Carica i dati relativi alle coordinate bancarie
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica banche
     * Pre:  E' stata generata una richiesta di caricamento delle coordinate bancarie relative al terzo dell'anticipo
     * Post: Il sistema restituisce la lista delle coordinate bancarie relative al terzo dell'anticipo
     *
     * @return la collezione di istanze di tipo BancaBulk
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    anticipo l' AnticipoBulk da cui ricavare il terzo per cui selezionare le coordinate bancarie
     */

    public java.util.Collection findListabanche(UserContext aUC, AnticipoBulk anticipo) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (anticipo.getV_terzo() == null)
            return null;

        return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, anticipo, null, null));
    }

    /**
     * Carica i dati relativi alle modalità di pagamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica modalita
     * Pre:  E' stata generata una richiesta di caricamento delle modalità di pagamento relative al terzo dell'anticipo
     * Post: Il sistema restituisce la lista delle modalità di pagamento relative al terzo dell'anticipo
     *
     * @return la collezione di istanze di tipo Rif_modalita_pagamentoBulk
     * @param    uc        lo UserContext che ha generato la richiesta
     * @param    bulk l' AnticipoBulk da cui ricavare il terzo per cui selezionare le modalità di pagamento
     */

    public java.util.Collection findModalita(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            AnticipoBulk anticipo = (AnticipoBulk) bulk;
            if ((anticipo.getTerzo() == null))
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            return terzoHome.findRif_modalita_pagamento(anticipo.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Carica i dati relativi ai termini di pagamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica termini
     * Pre:  E' stata generata una richiesta di caricamento dei termini di pagamento relativi al terzo dell'anticipo
     * Post: Il sistema restituisce la lista dei termini di pagamento relativi al terzo dell'anticipo
     *
     * @return la collezione di istanze di tipo Rif_termini_pagamentoBulk
     * @param    uc        lo UserContext che ha generato la richiesta
     * @param    bulk l' AnticipoBulk da cui ricavare il terzo per cui selezionare i termini di pagamento
     */

    public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            AnticipoBulk anticipo = (AnticipoBulk) bulk;
            if ((anticipo.getTerzo() == null))
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            return terzoHome.findRif_termini_pagamento(anticipo.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }

    }

    /**
     * Cambio data data registrazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cambio data - ok
     * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
     * La nuova data supera la validazione ( metodo 'validaDataRegistrazione')
     * Post: Il sistema inizializza la divisa di default e il cambio relativo a
     * tale divisa e valido alla data di registrazione
     * <p>
     * Nome: cambio data - errore
     * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
     * La nuova data non supera la validazione ( metodo 'validaDataRegistrazione')
     * Post: Il sistema segnala all'utente che la nuova data non ha superato la validazione
     *
     * @return l'AnticipoBulk con caricato il cambio relativo alla divisa per la nuova data di registrazione
     * @param    aUC            lo UserContext che ha generato la richiesta
     * @param    anticipo l' AnticipoBulk per cui gestire la nuova data di registrazioen
     */
    public AnticipoBulk gestisciCambioDataRegistrazione(UserContext aUC, AnticipoBulk anticipo) throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.sql.SQLException {
        // Verifico l'eleggibilita della data di registrazione
        validaDataRegistrazione(aUC, anticipo);

        try {
            if (anticipo.getCd_divisa() == null)
                anticipo.setDivisa(findDivisaDefault(aUC));
        } catch (RemoteException e) {
            throw handleException(anticipo, e);
        } catch (javax.ejb.EJBException e) {
            throw handleException(anticipo, e);
        }

        // Ricerco il cambio alla data
        CambioBulk cambio = findCambio(aUC, anticipo.getDivisa(), anticipo.getDt_registrazione());
        if (cambio != null)
            anticipo.setCambio(cambio.getCambio());

        return anticipo;
    }

    /**
     * Pre:  L'esercizio di scrivania è antecedente a quello corrente
     * Post: La data restituita viene inizializzata al 31/12/esercizio scrivania
     * <p>
     * Pre:  L'esercizio di scrivania NON è antecedente a quello corrente
     * Post: La data restituita viene inizializzata alla data odierna
     *
     * @return La data correttamente inizializzata
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk L' AnticipoBulk la cui data deve essere inizializzata.
     */

    private Timestamp getDataPerInizializzazioni(UserContext userContext, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.text.ParseException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;

        java.sql.Timestamp tsOdierno = getHome(userContext, anticipo).getServerDate();
        java.util.GregorianCalendar tsOdiernoGregorian = anticipo.getGregorianCalendar(tsOdierno);

        if (tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) > anticipo.getEsercizio().intValue()) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return (new java.sql.Timestamp(sdf.parse("31/12/" + anticipo.getEsercizio().intValue()).getTime()));
        }
        return tsOdierno;
    }

    private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(
            UserContext userContext,
            java.util.Enumeration scadenze) throws ComponentException {

        it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
        if (scadenze != null)
            while (scadenze.hasMoreElements()) {
                IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk) scadenze.nextElement();
                if (!scadenza.getFather().isTemporaneo()) {
                    if (!documentiContabiliNonTemporanei.containsKey(scadenza.getFather())) {
                        Vector allInstances = new java.util.Vector();
                        allInstances.addElement(scadenza.getFather());
                        documentiContabiliNonTemporanei.put(scadenza.getFather(), allInstances);
                    } else {
                        ((Vector) documentiContabiliNonTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
                    }
                }
            }
        return documentiContabiliNonTemporanei;
    }

    /**
     * Pre:	 Il sistema carica in modifica un anticipo con obbligazione riportata all'esercizio successivo
     * Post: Viene restituita la stringa 'R'
     * <p>
     * Pre:	 Il sistema carica in modifica un anticipo con obbligazione non riportata all'esercizio successivo
     * Post: Viene restituita la stringa 'N'
     *
     * @return Lo stato del documento contabile associato all'anticipo
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk L' AnticipoBulk caricato in modifica.
     */
    private String getStatoRiporto(UserContext context, AnticipoBulk anticipo) throws ComponentException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);
            return session.getStatoRiporto(context, anticipo);
        } catch (Throwable t) {
            throw handleException(anticipo, t);
        }
    }

    /**
     * Pre:	 Il sistema carica in modifica un anticipo con una obbligazione riportata all'esercizio di scrivania
     * Post: Viene restituita la stringa 'R'
     * <p>
     * Pre:	 Il sistema carica in modifica un anticipo con nessuna obbligazione riportata all'esercizio di scrivania
     * Post: Viene restituita la stringa 'N'
     *
     * @return Lo stato del documento contabile associato all'anticipo
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk L' AnticipoBulk caricato in modifica.
     */
    private String getStatoRiportoInScrivania(UserContext context, AnticipoBulk anticipo) throws ComponentException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);
            return session.getStatoRiportoInScrivania(context, anticipo);
        } catch (Throwable t) {
            throw handleException(anticipo, t);
        }
    }

    /**
     * Pre:	 Il sistema carica in modifica unrimborso con accertamento riportato all'esercizio successivo
     * Post: La procedura restitusce la stringa 'R'
     * <p>
     * Pre:	 Il sistema carica in modifica un rimborso con accertamento non riportato all'esercizio successivo
     * Post: La procedura restitusce la stringa 'N'
     *
     * @return Lo stato del documento contabile associato al rimborso
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk il RimborsoBulk caricato in modifica.
     */

    private String getStatoRiportoRimborso(UserContext context, RimborsoBulk rimborso) throws ComponentException {
        LoggableStatement cs = null;
        String status = null;

        try {
            try {
                cs = new LoggableStatement(getConnection(context),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB105.getStatoRiportato(?, ?, ?, ?, ?, ?) }", false, this.getClass());

                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.setString(2, rimborso.getCd_cds());
                cs.setString(3, rimborso.getCd_unita_organizzativa());
                cs.setInt(4, rimborso.getEsercizio().intValue());
                cs.setLong(5, rimborso.getPg_rimborso().longValue());
                cs.setString(6, Numerazione_doc_ammBulk.TIPO_RIMBORSO);
                cs.setInt(7, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).intValue());

                cs.executeQuery();
                status = cs.getString(1);
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        return status;
    }

    /**
     * Pre:	 Il sistema carica in modifica unrimborso con accertamento riportato all'esercizio successivo
     * Post: La procedura restitusce la stringa 'R'
     * <p>
     * Pre:	 Il sistema carica in modifica un rimborso con accertamento non riportato all'esercizio successivo
     * Post: La procedura restitusce la stringa 'N'
     *
     * @return Lo stato del documento contabile associato al rimborso
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk il RimborsoBulk caricato in modifica.
     */

    private String getStatoRiportoRimborsoInScrivania(UserContext context, RimborsoBulk rimborso) throws ComponentException {
        LoggableStatement cs = null;
        String status = null;

        try {
            try {
                cs = new LoggableStatement(getConnection(context),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB105.getStatoRiportatoInScrivania(?, ?, ?, ?, ?, ?) }", false, this.getClass());

                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.setString(2, rimborso.getCd_cds());
                cs.setString(3, rimborso.getCd_unita_organizzativa());
                cs.setInt(4, rimborso.getEsercizio().intValue());
                cs.setLong(5, rimborso.getPg_rimborso().longValue());
                cs.setString(6, Numerazione_doc_ammBulk.TIPO_RIMBORSO);
                cs.setInt(7, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).intValue());

                cs.executeQuery();
                status = cs.getString(1);
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        return status;
    }

    /**
     * Tipo di Cancellazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Tipo di Cancellazione
     * Pre:  L'utente ha richiesto la cancellazione dell'anticipo
     * Post: Il sistema richiama la stored procedure che stabilisce se la cancellazione
     * dell'anticipo deve essere logica o fisica
     *
     * @return il tipo di cancellazione (NULL = non cancellabile; F = cancellazione
     * fisica; L = cancellazione logica)
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    l' AnticipoBulk da cancellare
     */

    private Character getTipoCancellazione(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;
        Character rc = null;
        LoggableStatement cs = null;

        try {
            try {
                cs = new LoggableStatement(getConnection(aUC), "{call 	" + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "CNRCTB505.eseguiDelMissione(?,?,?,?,?)}", false, this.getClass());

                cs.setObject(1, anticipo.getCd_cds());
                cs.setObject(2, anticipo.getCd_unita_organizzativa());
                cs.setObject(3, anticipo.getEsercizio());
                cs.setObject(4, anticipo.getPg_anticipo());
                cs.setNull(5, java.sql.Types.CHAR);

                cs.registerOutParameter(5, java.sql.Types.CHAR);

                cs.executeQuery();
                rc = (Character) cs.getObject(5);
            } finally {
                cs.close();
            }
            return rc;
        } catch (java.sql.SQLException e) {
            throw handleException(anticipo, e);
        }
    }

    /**
     * Inizializzazione dell'anticipo per la creazione
     * <p>
     * Esercizio non aperto
     * PreCondition: L'esercizio di scrivania e' in uno stato diverso da APERTO
     * PostCondition: Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare missioni.
     * <p>
     * Esercizio aperto
     * PreCondition: L'esercizio di scrivania e' in stato APERTO
     * PostCondition: e' possibile procedere con la creazione dell' anticipo
     * <p>
     * Inizializzazione data registrazione e data competenza coge da/a
     * PreCondition: Inizializzazione della data registrazione e della data competenza coge da/a
     * PostCondition: Il sistema inizializza la data registrazione e la data competenza coge da/a
     *
     * @param aUC  lo user context
     * @param bulk l'istanza di  <code>AnticipoBulk</code> che si sta creando
     * @return l'istanza di  <code>AnticipoBulk</code>
     **/
    public OggettoBulk inizializzaBulkPerInserimento(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;
        try {
            //	Verifico che l'esercizio dell'anticipo (scrivania) sia aperto
            if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(anticipo.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire un anticipo per un esercizio non aperto!");

            //	Inizializzazione e controlli della data di registrazione
            anticipo.setDt_registrazione(getDataPerInizializzazioni(aUC, bulk));
            anticipo.setDt_a_competenza_coge(anticipo.getDt_registrazione());
            anticipo.setDt_da_competenza_coge(anticipo.getDt_registrazione());
            gestisciCambioDataRegistrazione(aUC, anticipo);

            return super.inizializzaBulkPerInserimento(aUC, anticipo);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializzazione dell'anticipo per la modifica
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: esercizio non valorizzato
     * Pre:  L'anticipo da caricare non ha valorizzato l'esercizio
     * Post: Il metodo utilizza un Throw Exception per comunicare che non e' possibile procedere.
     * <p>
     * Nome: esercizio anticipo maggiore di quello di scrivania
     * Pre:  L'utente ha selezionato un anticipo con esercizio maggiore a quello di scrivania
     * Post: Il metodo utilizza un Throw Exception per comunicare che non e' possibile procedere.
     * <p>
     * Nome: inizializzazione anticipo
     * Pre:  inizializzazione anticipo per modifica
     * Post: Il sistema inizializza l'anticipo con i dati relativi al terzo, all'eventuale rimborso e missione collegata
     * e verifica se il documento contabile associato all'anticipo e' stato riportato
     *
     * @return l'AnticipoBulk inizializzato
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk l' AnticipoBulk da inizializzare
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        if (bulk == null)
            return null;

        AnticipoBulk anticipo = (AnticipoBulk) bulk;
        try {
            if (anticipo.getEsercizio() == null)
                throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non è valorizzato! Impossibile proseguire.");

            if (anticipo.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
                throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

            anticipo = (AnticipoBulk) super.inizializzaBulkPerModifica(userContext, anticipo);

            caricaTerzoInModificaAnticipo(userContext, anticipo);

            // 	Serve nella lettura delle scadenze eleggibili (per poter riselezionare la scadenza
            //	associata all' anticipo che si sta modificando)
            if (anticipo.getScadenza_obbligazione() != null) {
                anticipo.setScadenza_obbligazioneClone(new Obbligazione_scadenzarioBulk());
                anticipo.getScadenza_obbligazioneClone().setEsercizio_originale(anticipo.getEsercizio_ori_obbligazione());
                anticipo.getScadenza_obbligazioneClone().setPg_obbligazione(anticipo.getPg_obbligazione());
                anticipo.getScadenza_obbligazioneClone().setPg_obbligazione_scadenzario(anticipo.getPg_obbligazione_scadenzario());
                anticipo.getScadenza_obbligazioneClone().setEsercizio(anticipo.getScadenza_obbligazione().getEsercizio());
                anticipo.getScadenza_obbligazioneClone().setCd_cds(anticipo.getScadenza_obbligazione().getCd_cds());
            }

            //	In base allo stato di riporto dell'obbligazione dell'anticipo
            //	inizializzo la variabile 'riportata' dell'anticipo
            anticipo.setRiportata(getStatoRiporto(userContext, anticipo));

            /**
             * Gennaro Borriello - (02/11/2004 15.04.39)
             *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
             */
            anticipo.setRiportataInScrivania(getStatoRiportoInScrivania(userContext, anticipo));

            anticipo = inizializzaRimborso(userContext, anticipo);

            if (anticipo.isAnticipoConMissione())
                loadMissione(userContext, anticipo);

			Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
					.filter(Scrittura_partita_doppiaHome.class::isInstance)
					.map(Scrittura_partita_doppiaHome.class::cast)
					.orElseThrow(() -> handleException(new ApplicationException("Partita doppia Home not found")));
			try {
				final Optional<Scrittura_partita_doppiaBulk> scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(anticipo);
				if (scritturaOpt.isPresent()) {
					Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
					scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
							.findMovimentiDareColl(userContext, scrittura)));
					scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
							.findMovimentiAvereColl(userContext, scrittura)));
					anticipo.setScrittura_partita_doppia(scrittura);
				}
			} catch (PersistencyException e) {
				throw handleException(anticipo, e);
			}
			try {
				Utility.createScritturaPartitaDoppiaComponentSession().proposeScritturaPartitaDoppia(userContext, anticipo);
			} catch (Exception e) {
				throw handleException(anticipo, e);
			}
		} catch (Throwable e) {
            throw handleException(e);
        }

        return anticipo;
    }

    /**
     * Inizializzazione rimborso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione rimborso
     * Pre:  L'utente ha selezionato un anticipo per modificarlo.  L'anticipo e' stato rimborsato
     * Post: Il sistema verifica se il documento contabile associato al rimborso e' stato riportato o meno e
     * restituisce l'anticipo con impostati tutti i dati relativi al rimborso
     *
     * @return l'AnticipoBulk inizializzato
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk l' AnticipoBulk da inizializzare
     */
    private AnticipoBulk inizializzaRimborso(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, RimborsoBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio_anticipo", SQLBuilder.EQUALS, anticipo.getEsercizio());
            sql.addClause("AND", "cd_cds_anticipo", SQLBuilder.EQUALS, anticipo.getCd_cds());
            sql.addClause("AND", "cd_uo_anticipo", SQLBuilder.EQUALS, anticipo.getCd_unita_organizzativa());
            sql.addClause("AND", "pg_anticipo", SQLBuilder.EQUALS, anticipo.getPg_anticipo());
            List result = getHome(userContext, RimborsoBulk.class).fetchAll(sql);
            if (result.size() > 1)
                throw new ApplicationException("Attenzione esiste piu' di un rimborso associato all'anticipo");
            if (result.size() == 1)
                anticipo.setRimborso((RimborsoBulk) result.get(0));

            //	In base allo stato di riporto dell'obbligazione dell'rimborso
            //	inizializzo la variabile 'riportata' del rimborso
            if (anticipo.hasRimborso()) {
                anticipo.getRimborso().setRiportata(getStatoRiportoRimborso(userContext, anticipo.getRimborso()));

                /**
                 * Gennaro Borriello - (02/11/2004 15.04.39)
                 *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
                 */
                anticipo.getRimborso().setRiportataInScrivania(getStatoRiportoRimborsoInScrivania(userContext, anticipo.getRimborso()));
            }

            return anticipo;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Verifica annullamento anticipo
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Verifica stato_cofi anticipo
     * Pre:  Viene richiesto se l'anticipo e' annullato o meno
     * Post: Il sistema esegue una query per verificare lo stato cofi dell'anticipo.
     * stato_cofi = A --> il metodo ritorna <true> (anticipo annullato)
     * stato_cofi <> A --> il metodo ritorna <false> (anticipo non annullato)
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param anticipo    L'anticipo da controllare
     * @return vero se l'anticipo è anullato falso altrimenti
     **/
    public boolean isAnticipoAnnullato(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            java.sql.ResultSet rs;
            String stato = null;
            String str = "SELECT STATO_COFI FROM " +
                    it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                    "ANTICIPO " +
                    "WHERE " +
                    "CD_CDS = ? AND " +
                    "CD_UNITA_ORGANIZZATIVA = ? AND " +
                    "ESERCIZIO = ? AND " +
                    "PG_ANTICIPO = ?";

            LoggableStatement ps = new LoggableStatement(getConnection(userContext), str, true, this.getClass());
            try {
                ps.setObject(1, anticipo.getCd_cds());
                ps.setObject(2, anticipo.getCd_unita_organizzativa());
                ps.setObject(3, anticipo.getEsercizio());
                ps.setObject(4, anticipo.getPg_anticipo());

                rs = ps.executeQuery();
                try {
                    if (rs.next())
                        stato = rs.getString(1);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
				}
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
			}

            return (AnticipoBulk.STATO_ANNULLATO.equals(stato));

        } catch (java.sql.SQLException ex) {
            throw handleException(anticipo, ex);
        }
    }

    /**
     * Carica missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica missione
     * Pre:  Il sistema inizializza un anticipo legato a missione per la modifica
     * Post: Il sistema esegue una query per caricare la missione
     * <p>
     * Nome: Carica missione
     * Pre:  L'anticipo risulta legato a missione ma il sistema non trova la missione associata
     * Post: Il metodo utilizza un Throw Exception per comunicare che non e' possibile procedere.
     *
     * @return l' AnticipoBulk con la missione caricata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    anticipo    l' AnticipoBulk per cui caricare la missione
     */

    private AnticipoBulk loadMissione(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            MissioneHome missioneHome = (MissioneHome) getHome(userContext, MissioneBulk.class);
            MissioneBulk missione = missioneHome.loadMissione(userContext, anticipo);
            anticipo.setMissione(missione);

            if (missione == null)
                throw new ApplicationException("Impossibile trovare la missione associata all' anticipo !");
        } catch (Throwable e) {
            throw handleException(e);
        }
        return anticipo;
    }

    /**
     * Modifica l'anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Modifica anticipo
     * Pre:  L'utente ha completato le modifiche dei dati dell'anticipo e ne chiede il salvataggio
     * Post: Il sistema rende persistente le modifiche dell'anticipo
     *
     * @return l'AnticipoBulk salvato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'AnticipoBulk per cui e' stato richiesta la modifica
     */

    public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return modificaConBulk(userContext, bulk, null);
    }

    /**
     * Modifica l'anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Validazione della scadenza di obbligazione associata all'anticipo
     * Pre:  La scadenza legata all'anticipo passa la validazione
     * Post: Il sistema prosegue con la modifica dell'anticipo
     * <p>
     * Nome: Modifica anticipo con obbligazione appena creata
     * Pre:  L'obbligazione associata all'anticipo modificato e' appena stata creata
     * Post: Il sistema aggiorna l'obbligazione associata all'anticipo e i suoi saldi
     * <p>
     * Nome: Modifica anticipo con obbligazione gia' esistente
     * Pre:  L'obbligazione associata all'anticipo modificato gia' esisteva
     * Post: Il sistema aggiorna l'importo associato a documento amministrativo della relativa scadenza
     * <p>
     * Nome: Modifica anticipo
     * Pre:  E' stata tolta l'associazione dell'anticipo con l'obbligazione originaria
     * Post: Il sistema aggiorna tale obbligazione azzerando l'importo associato a documento amministrativo
     * della relativa scadenza
     * <p>
     * Nome: Modifica anticipo
     * Pre:  In fase di modifica dell'anticipo l'utente ha creato una obbligazione che poi non ha associato all'anticipo
     * Post: Il sistema rende persistente tale obbligazione ed aggiorna i saldi
     * <p>
     * Nome: Modifica anticipo
     * Pre:  Gli aggiornamenti delle obbligazioni e scadenze sono andati a buon fine
     * Post: Il sistema rende persistente le modifiche dell'anticipo
     *
     * @return l'AnticipoBulk salvato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'AnticipoBulk per cui e' stato richiesta la modifica
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk, OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;

        // Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        aTempDiffSaldi = (PrimaryKeyHashMap) anticipo.getDefferredSaldi().clone();

        validaObbligazione(userContext, anticipo.getScadenza_obbligazione(), anticipo);
        aggiornaObbligazione(userContext, anticipo, status);

        anticipo = (AnticipoBulk) super.modificaConBulk(userContext, anticipo);

        // Restore dell'hash map dei saldi
        if (anticipo.getDefferredSaldi() != null)
            anticipo.getDefferredSaldi().putAll(aTempDiffSaldi);

        //Aggiornamenti degli stati COGE e COAN
        boolean aggiornaStatoCoge = false;
        try {
            AnticipoBulk anticipoDB = (AnticipoBulk) getTempHome(userContext, AnticipoBulk.class).findByPrimaryKey(anticipo);
            if (!Utility.equalsNull(anticipo.getStato_pagamento_fondo_eco(), anticipoDB.getStato_pagamento_fondo_eco()) ||
                    !Utility.equalsNull(anticipo.getCd_terzo(), anticipoDB.getCd_terzo()) ||
                    !Utility.equalsNull(anticipo.getDt_da_competenza_coge(), anticipoDB.getDt_da_competenza_coge()) ||
                    !Utility.equalsNull(anticipo.getDt_a_competenza_coge(), anticipoDB.getDt_a_competenza_coge()) ||
                    !Utility.equalsNull(anticipo.getIm_anticipo_divisa(), anticipoDB.getIm_anticipo_divisa().setScale(2)) ||
                    !Utility.equalsNull(anticipo.getIm_anticipo(), anticipoDB.getIm_anticipo().setScale(2)) ||
                    !Utility.equalsBulkNull(anticipo.getScadenza_obbligazione(), anticipoDB.getScadenza_obbligazione())
            )
                aggiornaStatoCoge = true;
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        }
        if (aggiornaStatoCoge) {
            if (AnticipoBulk.STATO_CONTABILIZZATO_COAN.equalsIgnoreCase(anticipo.getStato_coan())) {
                anticipo.setStato_coan(AnticipoBulk.STATO_RICONTABILIZZARE_COAN);
                anticipo.setToBeUpdated();
            }
            if (AnticipoBulk.STATO_CONTABILIZZATO_COGE.equalsIgnoreCase(anticipo.getStato_coge())) {
                anticipo.setStato_coge(AnticipoBulk.STATO_RICONTABILIZZARE_COGE);
                anticipo.setToBeUpdated();
            }
        }
        //	Aggiornamenti degli stati COGE e COAN sui rimborsi
        boolean aggiornaStatoCogeRimborsi = false;
        if (anticipo.getRimborso() != null) {
            RimborsoBulk rimborso = anticipo.getRimborso();
            try {
                RimborsoBulk rimborsoDB = (RimborsoBulk) getTempHome(userContext, RimborsoBulk.class).findByPrimaryKey(rimborso);
                if (!Utility.equalsNull(rimborso.getIm_rimborso(), rimborso.getIm_rimborso()) ||
                        !Utility.equalsNull(rimborso.getCd_terzo(), rimborsoDB.getCd_terzo()) ||
                        !Utility.equalsNull(rimborso.getDt_da_competenza_coge(), rimborsoDB.getDt_da_competenza_coge()) ||
                        !Utility.equalsNull(rimborso.getDt_a_competenza_coge(), rimborsoDB.getDt_a_competenza_coge()) ||
                        !Utility.equalsNull(rimborso.getEsercizio_accertamento(), rimborsoDB.getEsercizio_accertamento()) ||
                        !Utility.equalsNull(rimborso.getCd_cds_accertamento(), rimborsoDB.getCd_cds_accertamento()) ||
                        !Utility.equalsNull(rimborso.getPg_accertamento(), rimborsoDB.getPg_accertamento()) ||
                        !Utility.equalsNull(rimborso.getPg_accertamento_scadenzario(), rimborsoDB.getPg_accertamento_scadenzario())
                )
                    aggiornaStatoCogeRimborsi = true;
            } catch (PersistencyException e) {
                throw new ComponentException(e);
            }
            if (aggiornaStatoCogeRimborsi) {
                if (AnticipoBulk.STATO_CONTABILIZZATO_COAN.equalsIgnoreCase(rimborso.getStato_coan())) {
                    rimborso.setStato_coan(AnticipoBulk.STATO_RICONTABILIZZARE_COAN);
                    anticipo.setToBeUpdated();
                }
                if (AnticipoBulk.STATO_CONTABILIZZATO_COGE.equalsIgnoreCase(rimborso.getStato_coge())) {
                    rimborso.setStato_coge(AnticipoBulk.STATO_RICONTABILIZZARE_COGE);
                    anticipo.setToBeUpdated();
                }
            }
        }
        if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(anticipo.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");

        return anticipo;
    }

    /**
     * Riporto ad esercizio successivo dell'accertamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Riporto ad esercizio successivo dell'accertamento del rimborso
     * Pre:  L'utente ha richiesto il riporto in avanti del documento contabile associato al rimborso
     * Post: Il sistema chiama la procedura che gestisce il riporto in avanti
     *
     * @return l'AnticipoBulk rimborsato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'AnticipoBulk rimborsato
     */

    public AnticipoBulk riportaRimborsoAvanti(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB046.riportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }", false, this.getClass());

            cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
            cs.setString(2, Numerazione_doc_ammBulk.TIPO_RIMBORSO);
            cs.setString(3, anticipo.getRimborso().getCd_cds());
            cs.setInt(4, anticipo.getRimborso().getEsercizio().intValue());
            cs.setString(5, anticipo.getRimborso().getCd_unita_organizzativa());
            cs.setLong(6, anticipo.getRimborso().getPg_rimborso().longValue());
            cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

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
        return anticipo;
    }

    public AnticipoBulk riportaRimborsoIndietro(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call "
                            + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB046.deriportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }", false, this.getClass());

            cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
            cs.setString(2, Numerazione_doc_ammBulk.TIPO_RIMBORSO);
            cs.setString(3, anticipo.getRimborso().getCd_cds());
            cs.setInt(4, anticipo.getRimborso().getEsercizio().intValue());
            cs.setString(5, anticipo.getRimborso().getCd_unita_organizzativa());
            cs.setLong(6, anticipo.getRimborso().getPg_rimborso().longValue());
            cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

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
        return anticipo;
    }

    /**
     * Annulla le modifiche apportate all'anticipo e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: 	Rollback to savePoint
     * Pre:  	Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: 	Tutte le modifiche effettuate sull'anticipo da quando si e' impostato il savepoint vengono annullate
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     */
    public void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException {
        try {
            rollbackToSavepoint(userContext, savePointName);
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() != 1086)
                throw handleException(e);
        }
    }

    /**
     * Ricerca anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca di anticipi
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta con le clausole che
     * l'anticipo abbia cds, unità organizzativa uguali a quello di scrivania
     *
     * @param clauses le clausole speicificate dall'utene
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    l'AnticipoBulk da ricercare
     */

    public Query select(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        AnticipoBulk anticipo = (AnticipoBulk) bulk;

        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, anticipo.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, anticipo.getCd_unita_organizzativa());

        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("ANTICIPO.CD_TERZO", "TERZO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", SQLBuilder.EQUALS, anticipo.getV_terzo().getCd_terzo_precedente());

        return sql;
    }

    /**
     * Ricerca banca
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca delle coordinate bancarie del terzo associato all'anticipo
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta con le clausole che
     * le coordinate bancarie siano valide (non cancellate), siano associate al terzo dell'anticipo a abbiano un tipo uguale
     * a quello selezionato dall'utente (bancario, postale, etc.)
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    anticipo    l'AnticipoBulk da cui ricavare il terzo e il tipo di pagamento
     * @param    banca    la BancaBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectBancaByClause(UserContext aUC, AnticipoBulk anticipo, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {
        BancaHome bancaHome = (BancaHome) getHome(aUC, BancaBulk.class);
        return bancaHome.selectBancaFor(
                anticipo.getModalita_pagamento(),
                anticipo.getCd_terzo());
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Seleziona linee attività
     * Pre:  Una richiesta di listare le linee di attività da cui selezionare quella da utilizzare
     * per la creazione del rimborso e' stata generata
     * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente e le clausole aggiuntive che
     * la linea di attività sia di Entrata e il Cdr della Linea di attività appartenga all'Uo di scrivania
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    anticipo l' AnticipoBulk per cui selezionare la linea di attività
     * @param    latt la Linea_attivitaBulk da ricercare
     * @param    clauses le clausole specificate dall'utente
     */

    public SQLBuilder selectLattPerRimborsoByClause(UserContext aUC, AnticipoBulk anticipo, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt, CompoundFindClause clauses) throws ComponentException, RemoteException {
        Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione(aUC, ((CNRUserContext) aUC).getEsercizio(), null, "ELEMENTO_VOCE_SPECIALE", "RIMBORSO_ANTICIPO");
        if (config == null)
            throw new ApplicationException("Configurazione CNR: manca la definizione dell'ELEMENTO VOCE per RIMBORSO ANTICIPO");

        if (config.getVal01() == null)
            throw new ApplicationException("Configurazione CNR: manca il CODICE  dell'ELEMENTO VOCE per RIMBORSO ANTICIPO");


        SQLBuilder sql = getHome(aUC, latt.getClass(), "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
        sql.addClause(clauses);
        sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, anticipo.getEsercizio());
        sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.LIKE, anticipo.getCd_unita_organizzativa() + ".%");
        sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_ENTRATE);
        sql.addSQLClause(" AND ", "CD_NATURA IN ( SELECT CD_NATURA FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ASS_EV_EV " +
                "WHERE ASS_EV_EV.esercizio = ? and" +
                "  ASS_EV_EV.cd_elemento_voce = ? " +
                " and ASS_EV_EV.ti_appartenenza = ? " +
                " and ASS_EV_EV.ti_gestione = ?)");
        sql.addParameter(((CNRUserContext) aUC).getEsercizio(), Types.INTEGER, 0);
        sql.addParameter(config.getVal01(), Types.CHAR, 0);
        sql.addParameter(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CNR, Types.CHAR, 0);
        sql.addParameter(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE, Types.CHAR, 0);
        return sql;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Seleziona scadenza di obbligazione
     * Pre:  Una richiesta di listare le scadenze di obbligazione da cui selezionare quella da utilizzare
     * per la creazione dell'anticipo e' stata generata
     * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente e le seguenti clausole aggiuntive:
     * - obbligazione definitiva non annullata
     * - obbligazione con cds e esercizio uguali a quelli di scrivania
     * - obbligazione con terzo uguale a quello specificato per l'anticipo
     * - scadenza con data maggiore o uguale a quella di registrazione dell'anticipo
     * - scadenza con importo associato a doc. amministrativo uguale a 0
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    aUC    lo UserContext che ha generato la richiesta
     * @param    anticipo l'AnticipoBulk per cui selezionare la scadenza di obbligazione
     * @param    scadenza l'Obbligazione_scadenzarioBulk da ricercare
     * @param    clauses le clausole specificate dall'utente
     */

    public SQLBuilder selectScadenza_obbligazioneByClause(UserContext aUC, AnticipoBulk anticipo, Obbligazione_scadenzarioBulk scadenza, CompoundFindClause clauses) throws ComponentException {
        Obbligazione_scadenzarioHome scadenzaHome = (Obbligazione_scadenzarioHome) getHome(aUC, Obbligazione_scadenzarioBulk.class);
        SQLBuilder sql = scadenzaHome.createSQLBuilder();

        GregorianCalendar ggRegistrazione = (GregorianCalendar) anticipo.getGregorianCalendar(anticipo.getDt_registrazione()).clone();
        ggRegistrazione.set(GregorianCalendar.HOUR_OF_DAY, 0);
        ggRegistrazione.set(GregorianCalendar.MINUTE, 0);
        ggRegistrazione.set(GregorianCalendar.SECOND, 0);
        java.sql.Timestamp dataRegistrazione = new java.sql.Timestamp(ggRegistrazione.getTime().getTime());

        sql.setDistinctClause(true);
        sql.addTableToHeader("OBBLIGAZIONE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");

        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", SQLBuilder.EQUALS, anticipo.getEsercizio());
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_CDS", SQLBuilder.EQUALS, anticipo.getCd_cds());
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", SQLBuilder.EQUALS, "D");
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", SQLBuilder.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", SQLBuilder.EQUALS, anticipo.getCd_terzo());
        sql.addSQLClause("AND", "OBBLIGAZIONE.FL_PGIRO", SQLBuilder.NOT_EQUALS, "Y");
        sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", SQLBuilder.EQUALS, "N");

        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA", SQLBuilder.GREATER_EQUALS, dataRegistrazione);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);

        if (clauses != null)
            sql.addClause(clauses);

        return sql;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Seleziona terzo
     * Pre:  Una richiesta di listare i terzi da cui selezionare quello da utilizzare
     * per la creazione dell'anticipo e' stata generata
     * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente e la clausola aggiuntiva che il
     * tipo anagrafico (dipendente/altro) sia uguale a quello specificato per l'anticipo
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    aUC    lo UserContext che ha generato la richiesta
     * @param    anticipo l'AnticipoBulk per cui selezionare il terzo
     * @param    aTerzo il V_terzo_per_compensoBulk da ricercare
     * @param    clauses le clausole specificate dall'utente
     */

    public SQLBuilder selectV_terzoByClause(UserContext aUC, AnticipoBulk anticipo, V_terzo_per_compensoBulk aTerzo, CompoundFindClause clauses) throws ComponentException {
        //	Faccio una select DISTINCT perche' altrimenti seleziono tanti terzi
        //	quanti sono i tipi rapporto

        SQLBuilder sql = getHome(aUC, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO").createSQLBuilder();
        sql.addClause(clauses);
        sql.setDistinctClause(true);
        sql.addSQLClause("AND", "TI_DIPENDENTE_ALTRO", SQLBuilder.EQUALS, anticipo.getTi_anagrafico());
        sql.addSQLClause("AND", "CD_TERZO", SQLBuilder.EQUALS, anticipo.getCd_terzo());

        // Validita del terzo
        CompoundFindClause clause = CompoundFindClause.or(
                new SimpleFindClause("dt_fine_validita_terzo", SQLBuilder.GREATER_EQUALS, anticipo.getDt_registrazione()),
                new SimpleFindClause("dt_fine_validita_terzo", SQLBuilder.ISNULL, null));
        sql.addClause(clause);

        return sql;
    }

    /**
     * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo
     * fino a quel momento
     * Pre-post-conditions:
     * <p>
     * Nome: Imposta savePoint
     * Pre:  Una richiesta di impostare un savepoint e' stata generata
     * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     */
    public void setSavePoint(UserContext userContext, String savePointName) throws ComponentException {
        try {
            setSavepoint(userContext, savePointName);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }

    /**
     * Aggiorna importo associato a doc.amm. della scadenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: aggiorna
     * Pre:  L'utente ha selezionato una scadenza di obbligazione per contabilizzare l'anticipo
     * Post: Il sistema aggiorna l'importo associato a documenti amministrativi della scadenza dell'obbligazione su cui+
     * l'anticipo e' stato contabilizzato
     *
     * @return l'Obbligazione_scadenzarioBulk con l'importo aggiornato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    scadenza    l'Obbligazione_scadenzarioBulk per cui modificare l'importo associato a doc.amm.
     */

//
//	Aggiorno l'importo "IM_ASSOCIATO_DOC_AMM" della scadenza di obbligazione legata all'anticipo
//
    private IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(UserContext userContext, IScadenzaDocumentoContabileBulk scadenza) throws ComponentException {
        try {
            ((IScadenzaDocumentoContabileHome) getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext, scadenza);
        } catch (it.cnr.jada.persistency.PersistencyException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        } catch (it.cnr.jada.bulk.BusyResourceException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        } catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        }

        return scadenza;
    }

    /**
     * Verifica data registrazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: valida data - ok
     * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
     * La nuova data e' uguale o successiva alla data dell'ultimo anticipo inserito
     * Post: Il sistema valida la nuova data
     * <p>
     * Nome: valida data - errore
     * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
     * La nuova data e' antecedente alla data dell'ultimo anticipo inserito
     * Post: Il sistema segnala con un errore all'utente la non validazione della data inserita
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    anticipo    l'AnticipoBulk per cui validare la data di registrazione
     */


    private void validaDataRegistrazione(UserContext userContext, AnticipoBulk anticipo) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.sql.SQLException {
        java.sql.Timestamp dtUltimoAnticipo = ((AnticipoHome) getHome(userContext, AnticipoBulk.class)).findDataRegistrazioneUltimoAnticipo(anticipo);
        if ((dtUltimoAnticipo != null) && (dtUltimoAnticipo.after(anticipo.getDt_registrazione())))
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire un'Anticipo con data anteriore a " + java.text.DateFormat.getDateInstance().format(dtUltimoAnticipo));
    }

    /**
     * Viene richiesta la validazione dell'obbligazione associata all'anticipo
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: 	Scadenza non selezionata
     * Pre: 	Non e' stata selezionata la scadenza da associare all'anticipo
     * Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
     * Generata una ApplicationException con il messaggio:	"Nessuna obbligazione associata!"
     * <p>
     * Nome: 	Importi obbligazione/scadenza NULLI
     * Pre: 	L'importo della obbligazione e/o della scadenza è nullo
     * Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
     * Generata una ApplicationException con il messaggio:
     * "L'importo dell'obbligazione/scadenza è un dato obbligatorio"
     * <p>
     * Nome: 	Importo scadenza diverso da quello dell'anticipo
     * Pre: 	L'importo della scadenza è diverso da quello dell' anticipo
     * Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
     * Generata una ApplicationException con il messaggio:
     * "La scadenza di obbligazione associata ha un importo diverso da quello dell'anticipo!"
     * <p>
     * Nome: 	Data scadenza NON valida
     * Pre: 	La scadenza selezionata ha una data minore della data di registrazione dell'anticipo
     * Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
     * Generata una ApplicationException con il messaggio:
     * "La data della scadenza dell'obbligazione deve essere successiva alla data di registrazione dell' anticipo!"
     * <p>
     * Nome: 	Terzo selezionato NON valido
     * Pre: 	Il terzo selezionato è diverso dal terzo dell'anticipo oppure il tipo entità NON è DIVERSI
     * Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
     * Generata una ApplicationException con il messaggio:
     * "L'obbligazione deve avere un creditore valido!"
     * <p>
     * Nome: 	Tutte le validazioni precedenti superate
     * Pre: 	L'obbligazione supera tutte le validazioni precedenti
     * Post: 	Viene validata l'associazione dell'anticipo con la scadenza di obbligazione
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    scadenza        la scadenza da validare
     **/
    public void validaObbligazione(UserContext userContext, Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk) throws ComponentException {
        AnticipoBulk anticipo = (AnticipoBulk) bulk;
        ObbligazioneBulk obbligazione = scadenza.getObbligazione();

        if (scadenza == null)
            throw new it.cnr.jada.comp.ApplicationException("Nessun impegno associato!");

        if (obbligazione.getIm_obbligazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'importo dell'impegno è un dato obbligatorio");

        if (scadenza.getIm_scadenza() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'importo della scadenza è un dato obbligatorio");

        if (scadenza.getIm_scadenza().compareTo(anticipo.getIm_anticipo_divisa()) != 0)
            throw new it.cnr.jada.comp.ApplicationException("La scadenza dell'impegno associato ha un importo diverso da quello dell'anticipo!");

        GregorianCalendar gcRegistrazione = (GregorianCalendar) anticipo.getGregorianCalendar(anticipo.getDt_registrazione()).clone();
        gcRegistrazione.set(java.util.GregorianCalendar.HOUR_OF_DAY, 0);
        gcRegistrazione.set(java.util.GregorianCalendar.MINUTE, 0);
        gcRegistrazione.set(java.util.GregorianCalendar.SECOND, 0);
        if (gcRegistrazione.getTime().compareTo(scadenza.getDt_scadenza()) >= 0)
            throw new it.cnr.jada.comp.ApplicationException("La data della scadenza dell'impegno deve essere successiva alla data di registrazione dell' anticipo!");

        validaTerzoObbligazione(userContext, anticipo, obbligazione);
    }

    /**
     * Validazione del Anticipo per Esercizio COEP precedente
     * <p>
     * Gennaro Borriello/Luisa Farinella - (05/11/2004 18.11.25)
     * Aggiunto controllo per i documenti RIPORTATI, in base all'esercizio COEP precedente
     * all'es. di scrivania.
     */
    private void validateEsercizioCOEP(it.cnr.jada.UserContext userContext, AnticipoBulk anticipo) throws ComponentException {

        LoggableStatement cs = null;
        String status = null;

        try {
            if (anticipo.isRiportataInScrivania()) {
                Integer es_prec = new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue() - 1);

                cs = new LoggableStatement(getConnection(userContext), "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "CNRCTB200.isChiusuraCoepDef(?,?)}", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.setObject(2, es_prec);
                cs.setObject(3, anticipo.getCd_cds());

                cs.executeQuery();

                status = cs.getString(1);

                if (status.compareTo("Y") != 0) {
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile eliminare il documento, poichè l'esercizio economico precedente non è chiuso.");
                }
            }

        } catch (java.sql.SQLException ex) {
            throw new it.cnr.jada.comp.ComponentException(ex);
        } finally {
            try {
                if (cs != null)
                    cs.close();
            } catch (Throwable t) {
                throw new it.cnr.jada.comp.ComponentException(t);
            }
        }
    }

    /**
     * Viene controllato che il terzo selezionato nell'anticipo corrisponda con al terzo selezionato
     * nell'obbligazione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome:	Terzo obbligazione non valido
     * Pre: 	Non è stato selezionato il terzo nell'obbligazione
     * Post: 	Ritorna un ApplicationException con la descrizione dell'errore
     * <p>
     * Nome: 	Terzo obbligazione uguale al terzo dell'anticipo
     * Pre: 	Il terzo selezionato nell'obbligazione corrisponde al terzo dell'anticipo
     * Post: 	Viene validato il terzo
     * <p>
     * Nome: 	L'anagrafica dell'obbligazione ha tipo entita DIVERSI
     * Pre: 	L'anagafica associata al terzo dell'obbligazione ha come tipo entita DIVERSI
     * Post: 	Viene validato il terzo
     * <p>
     * Nome: 	Nessuna delle due condizioni precedenti è verificata
     * Pre: 	Il terzo selezionato NON corrisponde al terzo dell'anticipo e
     * l'anagrafica associata NON ha tipo entita DIVERSI
     * Post: 	Ritorna un ApplicationException con la descrizione dell'errore
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    compenso        l'anticipo di cui validare il terzo
     * @param    obblig            l'obbligazione di cui validare il terzo
     **/
    private void validaTerzoObbligazione(UserContext userContext, AnticipoBulk anticipo, ObbligazioneBulk obbligazione) throws ComponentException {
        try {
            TerzoBulk creditore = obbligazione.getCreditore();
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            creditore = (TerzoBulk) terzoHome.findByPrimaryKey(obbligazione.getCreditore());
            if (creditore == null || creditore.getCd_terzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("L'impegno deve avere un creditore valido!");
            getHomeCache(userContext).fetchAll(userContext);

            AnagraficoHome anaHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
            AnagraficoBulk anagrafico = (AnagraficoBulk) anaHome.findByPrimaryKey(creditore.getAnagrafico());
            if (!anticipo.getTerzo().equalsByPrimaryKey(creditore) && !AnagraficoBulk.DIVERSI.equalsIgnoreCase(anagrafico.getTi_entita()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore dell'anticipo!");
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public boolean verificaStatoEsercizio(UserContext userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws ComponentException {
        try {
            it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome) getHome(userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);

            return !eHome.isEsercizioChiuso(userContext, anEsercizio.getEsercizio(), anEsercizio.getCd_cds());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }
}
