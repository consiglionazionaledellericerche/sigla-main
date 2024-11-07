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

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleHome;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (20/11/2002 12.26.17)
 *
 * @author: Roberto Fantino
 */
public class AssManualeMandatoReversaleComponent extends it.cnr.jada.comp.CRUDComponent implements IAssManualeMandatoReversaleMgr {
    /**
     * MandatoReversaliComponent constructor comment.
     */
    public AssManualeMandatoReversaleComponent() {
        super();
    }

    /**
     * Aggiorno la tabella ASS_MANDATO_REVERSALE cancellando le reversali
     * che sono state scollegate e inserendo le reversali associate
     * <p>
     * Nel caso in cui la procedura CNRCTB037.checkReversaliAssociate fornisca
     * un errore di validazione, verrà eseguito un rollback
     * <p>
     * Nel caso in cui la procedura CNRCTB037.checkRevDisassocMan fornisca
     * un errore di validazione, verrà eseguito un rollback
     **/
    private void aggiornaReversaliAssociate(UserContext userContext, MandatoBulk mandato) throws ComponentException {

        try {
            for (java.util.Iterator i = mandato.getReversaliColl().iterator(); i.hasNext(); ) {
                Ass_mandato_reversaleBulk assManRev = (Ass_mandato_reversaleBulk) i.next();
                if (assManRev.isToBeCreated())
                    insertBulk(userContext, assManRev);
            }
            for (java.util.Iterator i = mandato.getReversaliColl().deleteIterator(); i.hasNext(); ) {
                Ass_mandato_reversaleBulk assManRev = (Ass_mandato_reversaleBulk) i.next();
                checkRevDisassocMan(userContext, assManRev);
                if (assManRev.isToBeDeleted())
                    deleteBulk(userContext, assManRev);
            }
        } catch (PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Richiama la procedura Oracle CNRCTB037.checkRevDisassocMan
     * che controlla l'eventuale disassociabilità di una reversale da un mandato
     **/
    private void checkRevDisassocMan(UserContext userContext, Ass_mandato_reversaleBulk assManRev) throws ComponentException {

        try {
            LoggableStatement cs = null;
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                                + "CNRCTB037.checkRevDisassocMan(?,?,?,?,?,?)}", false, this.getClass());

                cs.setObject(1, assManRev.getCd_cds_mandato());
                cs.setObject(2, assManRev.getEsercizio_mandato());
                cs.setObject(3, assManRev.getPg_mandato());
                cs.setObject(4, assManRev.getCd_cds_reversale());
                cs.setObject(5, assManRev.getEsercizio_reversale());
                cs.setObject(6, assManRev.getPg_reversale());
                cs.executeQuery();
            } finally {
                cs.close();
            }

        } catch (java.sql.SQLException ex) {
            throw handleException(assManRev, ex);
        }

        return;
    }

    /**
     * Richiama la procedura Oracle CNRCTB037.checkReversaliAssociate
     * che controlla le reversali associate al mandato e ritorna l'importo
     * ritenuta da inserire nel mandato
     **/
    private MandatoBulk checkReversaliAssociate(UserContext userContext, MandatoBulk mandato) throws ComponentException {

        try {
            mandato.setIm_ritenute(new java.math.BigDecimal(0));

            LoggableStatement cs = null;
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                                + "CNRCTB037.checkReversaliAssociate(?,?,?,?)}", false, this.getClass());

                cs.setObject(1, mandato.getCd_cds());
                cs.setObject(2, mandato.getEsercizio());
                cs.setObject(3, mandato.getPg_mandato());
                cs.setObject(4, mandato.getIm_ritenute());

                cs.registerOutParameter(4, java.sql.Types.DECIMAL);

                cs.executeQuery();
                mandato.setIm_ritenute(cs.getBigDecimal(4));
            } finally {
                cs.close();
            }

        } catch (java.sql.SQLException ex) {
            throw handleException(mandato, ex);
        }

        return mandato;
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile
     * operazione di modifica.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Oggetto non esistente
     * Pre: L'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * <p>
     * Nome: Tutti i controlli superati
     * Pre: L'OggettoBulk specificato esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
     * per l'operazione di presentazione e modifica nell'interfaccia visuale.
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
     * ottenuto concatenando il nome della component con la stringa ".edit"
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk    l'OggettoBulk da preparare
     * @return l'OggettoBulk preparato
     **/
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        MandatoBulk mandato = (MandatoBulk) super.inizializzaBulkPerModifica(userContext, bulk);
        mandato.setReversaliColl(new BulkList(loadReversaliAssociate(userContext, mandato)));
        mandato.setReversaliDisponibili(loadReversaliDisponibili(userContext, mandato));

        return mandato;
    }

    /**
     * Caricamento Reversali Associate al Mandato
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Caricamento Reversali associate
     * Pre: Viene richiesto il caricamento delle reversali associate al mandato
     * Post: Viene restituita la collezione delle reversali associate al mandato
     */
    public java.util.Collection loadReversaliAssociate(UserContext userContext, MandatoBulk mandato) throws ComponentException {

        try {
            Ass_mandato_reversaleHome home = (Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class);
            java.util.Collection coll = home.findReversali(userContext, mandato);

            for (java.util.Iterator i = coll.iterator(); i.hasNext(); ) {
                Ass_mandato_reversaleBulk assManRev = (Ass_mandato_reversaleBulk) i.next();
                ReversaleIHome revHome = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
                assManRev.setReversale(revHome.loadReversale(userContext, assManRev.getCd_cds_reversale(), assManRev.getEsercizio_reversale(), assManRev.getPg_reversale()));
                assManRev.setMandato(mandato);
            }
            return coll;

        } catch (PersistencyException ex) {
            throw handleException(ex);
        } catch (IntrospectionException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Caricamento Reversali Da Associare al Mandato
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Caricamento Reversali da associare al mandato (reversali non ancora associate a mandato)
     * Pre: Viene richiesto il caricamento delle reversali da associare al mandato (non ancora associate a mandato)
     * Post: Viene restituita la collezione delle reversali da associare al mandato
     */
    public java.util.List loadReversaliDisponibili(UserContext userContext, MandatoBulk mandato) throws ComponentException {

        try {
            V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(userContext, V_mandato_reversaleBulk.class);
            java.util.List l = home.findReversaliAssociabili(userContext, mandato);

            java.util.List list = new BulkList();
            for (java.util.Iterator i = l.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk vManRev = (V_mandato_reversaleBulk) i.next();
                if (
                        vManRev.getCd_tipo_documento_cont_padre().equals(Numerazione_doc_contBulk.TIPO_REV)  ||
                                (Optional.ofNullable(vManRev.getEsitoOperazione()).filter(s -> s.equals(EsitoOperazione.ACQUISITO.value())).isPresent() &&
                                        vManRev.getCd_tipo_documento_cont_padre().equals(Numerazione_doc_contBulk.TIPO_MAN))
                ) {
                    ReversaleIHome revHome = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
                    ReversaleBulk rev = revHome.loadReversale(userContext, vManRev.getCd_cds(), vManRev.getEsercizio(), vManRev.getPg_documento_cont());
                    if (!reversaleIsAssociata(userContext, mandato, rev) && Optional.ofNullable(rev).isPresent())
                        list.add(rev);
                }
            }
            return list;

        } catch (PersistencyException ex) {
            throw handleException(ex);
        } catch (IntrospectionException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Esegue una operazione di creazione di un OggettoBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Non è stato selezionato il mandato
     * Pre: Viene richiesto un salvataggio senza aver selezionato il mandato
     * Post: Viene generata una eccezione con la descrizione dell'errore
     * <p>
     * Nome: Non ho associato nessuna reversale al mandato
     * Pre: Viene richiesto il salvataggio del mandato senza aver collegato reversali
     * Post: Viene generata una eccezione con la descrizione dell'errore
     * <p>
     * Nome: Validazione NON superata
     * Pre:  Le reversali associate al mandato non superano la validazione del package 037
     * Post: Viene generata una eccezione con la descrizione dell'errore
     * <p>
     * Nome: Validazione superata
     * Pre: Le reversali associate al mandato superano la validazione
     * Post: Vengono salvate le nuove associazioni e aggiornato il mandato selezionato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param bulk        OggettoBulk il compenso che deve essere creato
     * @return l'OggettoBulk risultante dopo l'operazione di creazione.
     * <p>
     * Metodo di validzione del mandato:
     * validaMandato(UserContext, MandatoBulk)
     **/
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {
            MandatoBulk mandato = (MandatoBulk) bulk;
            validaMandato(userContext, mandato);

            aggiornaReversaliAssociate(userContext, mandato);
            checkReversaliAssociate(userContext, mandato);

            updateBulk(userContext, mandato);
            return mandato;
        } catch (PersistencyException ex) {
            throw handleException(ex);
        }
    }

    private boolean reversaleIsAssociata(UserContext userContext, MandatoBulk mandato, ReversaleBulk reversale) {

        for (java.util.Iterator i = mandato.getReversaliAssociate().iterator(); i.hasNext(); ) {
            ReversaleBulk revAss = (ReversaleBulk) i.next();
            if (revAss.equalsByPrimaryKey(reversale))
                return true;
        }
        return false;
    }

    /**
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Mandato
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Richiesta di ricerca di un Mandato
     * Pre:  E' stata generata la richiesta di ricerca di un Mandato
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e,
     * in aggiunta, le seguenti clausole statiche:
     * <p>
     * --> Clausole impostate dall'initializeForSearch dell'oggettoBulk
     * - ESERCIZIO = esercizio di scrivania
     * - CD_UNITA_ORGANIZZATIVA = codice unita organizzativa di scrivania
     * - CD_UO_ORIGINE = codice unita organizzativa di scrivania
     * <p>
     * --> Aggiunte
     * - CD_CDS = cds di scrivania
     * - STATO = "E" --> includo solo i mandati Emessi
     * - STATO_TRASMISSIONE != "T" --> escludo i mandati Trasmessi
     * - TI_MANDATO != "A" --> escludo i mandati di Accreditamento
     * - TI_MANDATO != "R" --> escludo i mandati di Regolarizzazione
     * - escludo i mandati ASSOCIATI A COMPENSO
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param clauses     clausole di ricerca gia' specificate dall'utente
     * @param bulk        istanza di MandatoIBulk che deve essere utilizzata per la ricerca
     * @return L'SQLBuilder con le clausole aggiuntive
     **/
    public Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, PersistencyException {

        MandatoIBulk mandato = (MandatoIBulk) bulk;
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        sql.setDistinctClause(true);
        sql.addClause(clauses);

        sql.addSQLClause(FindClause.AND, "MANDATO.CD_CDS", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
        sql.addSQLClause(FindClause.AND, "MANDATO.STATO", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_EMESSO);
        sql.addSQLClause(FindClause.AND, "MANDATO.STATO_TRASMISSIONE", SQLBuilder.EQUALS, MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
        sql.addSQLClause(FindClause.AND, "MANDATO.TI_MANDATO", SQLBuilder.NOT_EQUALS, MandatoBulk.TIPO_ACCREDITAMENTO);
        sql.addSQLClause(FindClause.AND, "MANDATO.TI_MANDATO", SQLBuilder.NOT_EQUALS, MandatoBulk.TIPO_REGOLARIZZAZIONE);

        SQLBuilder sqlSostituzione = getHome(userContext, bulk.getClass()).createSQLBuilder();
        sqlSostituzione.setFromClause(sqlSostituzione.getFromClause().append(" SOSTITUZIONE"));
        sqlSostituzione.addSQLJoin("SOSTITUZIONE.CD_CDS", "MANDATO.CD_CDS");
        sqlSostituzione.addSQLJoin("SOSTITUZIONE.ESERCIZIO", "MANDATO.ESERCIZIO");
        sqlSostituzione.addSQLJoin("SOSTITUZIONE.PG_MANDATO_RIEMISSIONE", "MANDATO.PG_MANDATO");
        sqlSostituzione.addSQLClause(FindClause.AND, "SOSTITUZIONE.STATO_VAR_SOS", SQLBuilder.EQUALS, StatoVariazioneSostituzione.SOSTITUZIONE_DEFINITIVA.value());

        SQLBuilder sqlNonAssociabile = getHome(userContext, bulk.getClass(), "V_MANDATO_NON_ASSOCIABILE_REV").createSQLBuilder();
        sqlNonAssociabile.addSQLJoin("V_MANDATO_NON_ASSOCIABILE_REV.PG_MANDATO", "MANDATO.PG_MANDATO");
        sqlNonAssociabile.addSQLJoin("V_MANDATO_NON_ASSOCIABILE_REV.CD_CDS", "MANDATO.CD_CDS");
        sqlNonAssociabile.addSQLJoin("V_MANDATO_NON_ASSOCIABILE_REV.ESERCIZIO", "MANDATO.ESERCIZIO");

        sql.openParenthesis(FindClause.AND);
            sql.addSQLExistsClause(FindClause.AND, sqlSostituzione);
            sql.addSQLNotExistsClause(FindClause.OR, sqlNonAssociabile);
        sql.closeParenthesis();

        return sql;
    }

    /**
     * Viene richiesta una validazione del mandato
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Non è stato selezionato il mandato
     * Pre: Viene richiesto un salvataggio senza aver selezionato il mandato
     * Post: Viene generata una eccezione con la descrizione dell'errore
     * <p>
     * Nome: Non ho associato nessuna reversale al mandato
     * Pre: Viene richiesto il salvataggio del mandato senza aver collegato reversali
     * Post: Viene generata una eccezione con la descrizione dell'errore
     */
    private void validaMandato(UserContext userContext, MandatoBulk mandato) throws ComponentException {

        if (mandato.getPg_mandato() == null)
            throw new ApplicationException("Selezionare il mandato");

        if (
                mandato.getReversaliColl() == null ||
                        mandato.getReversaliColl().isEmpty() && (
                                mandato.getReversaliColl().deleteIterator() == null
                                        || !mandato.getReversaliColl().deleteIterator().hasNext()
                        )
        )
            throw new ApplicationException("E' necessario associare delle reversali al mandato");

        // Locko ogni reversale associata, prima di continuare con le operazioni di creazione:
        //	in tal modo, disabilito le modifiche su tali oggetti, da parte di utenti concorrenti e,
        //	allo stesso tempo, controllo che, nel frattempo, non sia cambiato il PG_VER_REC degli oggetti associati.
        try {
            java.util.Iterator i = mandato.getReversaliAssociate().iterator();

			while (i.hasNext()) {
                lockBulk(userContext, (ReversaleIBulk) i.next());
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        } catch (BusyResourceException e) {
            throw handleException(e);
        } catch (OutdatedResourceException e) {
            throw handleException(e);
        }
    }
}
