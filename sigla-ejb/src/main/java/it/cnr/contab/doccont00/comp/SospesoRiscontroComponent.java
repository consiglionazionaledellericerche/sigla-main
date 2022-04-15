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

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.logs.ejb.BatchControlComponentSession;
import it.cnr.contab.pagopa.ejb.PendenzaPagopaComponentSession;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SospesoRiscontroComponent extends it.cnr.jada.comp.CRUDComponent implements ISospesoRiscontroMgr, ICRUDMgr, Cloneable, Serializable, IPrintMgr {
    /**
     * SospesoRiscontroComponent constructor comment.
     */
    public SospesoRiscontroComponent() {
        super();
    }

    /**
     * aggiornamento Documenti Contabili e Saldi da sospeso
     * PreCondition:
     * Nel caso un riscontro sia stato annullato ed il doc. contabile sia stato pagato oppure
     * nel caso il riscontro sia stato inserito e il doc. contabile non sia stato pagato, occorre
     * aggiornare tutti quei documenti contabili o saldi che fanno riferimento al
     * riscontro stesso.
     * PostCondition:
     * Vengono quindi aggiornati documenti contabili o saldi tramite una stored procedure
     * (aggDaSospesoRiscontro)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param v_man_rev   <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                    recuperare alcuni dati relativi al documento contabile associato al sospeso
     */
    private void aggiornaDaSospesoRiscontro(UserContext userContext, V_mandato_reversaleBulk v_man_rev) throws ComponentException {
        try {
            LoggableStatement cs = null;

            if (Numerazione_doc_contBulk.TIPO_MAN.equals(v_man_rev.getCd_tipo_documento_cont()))

                cs = new LoggableStatement(getConnection(userContext), "{  call " +
                        it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                        "CNRCTB037.riscontroMandato(?, ?, ?, ?, ?)}", false, this.getClass());
            else
                cs = new LoggableStatement(getConnection(userContext), "{  call " +
                        it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                        "CNRCTB037.riscontroReversale(?, ?, ?, ?, ?)}", false, this.getClass());

            try {
                cs.setObject(1, v_man_rev.getEsercizio());
                cs.setString(2, v_man_rev.getCd_cds());
                cs.setObject(3, v_man_rev.getPg_documento_cont());
                cs.setString(4, MandatoComponent.MODIFICA_MANDATO_ACTION);
                cs.setString(5, userContext.getUser());
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
     * aggiornamento Importo incassato di una Reversale
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo incassato della reversale
     * associata al riscontro
     * PostCondition:
     * Nel caso il dettaglio d'entrata del riscontro sia stato annullato, viene calcolato
     * l'importo del riscontro, sottraendo dallo stesso l'importo associato del dettaglio
     * d'entrata del riscontro
     * In tutti gli altri casi viene impostato l'importo del riscontro con l'importo
     * associato del dettaglio d'entrata.
     * Viene poi aggiornato l'importo della reversale associata al riscontro
     * (metodo aggiornaImReversale)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param det_etr     <code>Sospeso_det_etrBulk</code> il dettaglio d'entrata del riscontro da aggiornare
     * @param v_man_rev   <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                    recuperare alcuni dati relativi al documento contabile associato al riscontro
     */
    private void aggiornaIm_pagato_incassato(UserContext userContext, Sospeso_det_etrBulk det_etr, V_mandato_reversaleBulk v_man_rev) throws ComponentException {
        try {
            BigDecimal im_sospeso;
            if (Sospeso_det_etrBulk.STATO_ANNULLATO.equals(det_etr.getStato()))
                im_sospeso = det_etr.getIm_associato().negate();
            else
                im_sospeso = det_etr.getIm_associato();

            aggiornaImReversale(userContext, v_man_rev, im_sospeso);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Importo pagato di un Mandato
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo pagato del mandato
     * associato al riscontro
     * PostCondition:
     * Nel caso il dettaglio di spesa del riscontro sia stato annullato, viene calcolato
     * l'importo del riscontro, sottraendo dallo stesso l'importo associato del dettaglio
     * di spesa del riscontro
     * In tutti gli altri casi viene impostato l'importo del riscontro con l'importo
     * associato del dettaglio di spesa.
     * Viene poi aggiornato l'importo del mandato associato (metodo aggiornaImMandato)
     * aggiornamento Importo pagato di un Mandato di Accreditamento
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo pagato del mandato
     * di accreditamento associato al riscontro
     * PostCondition:
     * Nel caso il dettaglio di spesa del riscontro sia stato annullato, viene calcolato
     * l'importo del riscontro, sottraendo dallo stesso l'importo associato del dettaglio
     * di spesa del riscontro
     * In tutti gli altri casi viene impostato l'importo del riscontro con l'importo
     * associato del dettaglio di spesa.
     * Viene poi aggiornato l'importo del mandato di accreditamento associato
     * (metodo aggiornaImMandatoAccreditamento)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param det_usc     <code>Sospeso_det_uscBulk</code> il dettaglio di spesa del riscontro da aggiornare
     * @param v_man_rev   <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                    recuperare alcuni dati relativi al documento contabile associato al riscontro
     */
    private void aggiornaIm_pagato_incassato(UserContext userContext, Sospeso_det_uscBulk det_usc, V_mandato_reversaleBulk v_man_rev) throws ComponentException {
        try {
            BigDecimal im_sospeso;
            if (Sospeso_det_uscBulk.STATO_ANNULLATO.equals(det_usc.getStato()))
                im_sospeso = det_usc.getIm_associato().negate();
            else
                im_sospeso = det_usc.getIm_associato();

            if (v_man_rev != null && v_man_rev.getTi_documento_cont().equals(MandatoBulk.TIPO_ACCREDITAMENTO))
                aggiornaImMandatoAccreditamento(userContext, v_man_rev, im_sospeso);
            else
                aggiornaImMandato(userContext, v_man_rev, im_sospeso);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Importo di un Mandato
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo del mandato associato al riscontro
     * PostCondition:
     * Viene aggiornato l'importo pagato del mandato a cui viene aggiunto l'importo del riscontro
     * ad esso associato
     *
     * @param userContext  lo <code>UserContext</code> che ha generato la richiesta
     * @param docContabile <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                     recuperare alcuni dati relativi al documento contabile associato al riscontro
     * @param im_riscontro <code>BigDecimal</code> l'importo del riscontro
     */
    private void aggiornaImMandato(UserContext userContext, V_mandato_reversaleBulk docContabile, BigDecimal im_riscontro) throws ComponentException {
        try {
            MandatoBulk mandato = (MandatoBulk) getHome(userContext, MandatoIBulk.class).findAndLock(
                    new MandatoIBulk(
                            docContabile.getCd_cds(),
                            docContabile.getEsercizio(),
                            docContabile.getPg_documento_cont()));
            // verifico che il mandato da associare al riscontro nel frattempo non sia stato modificato
            if (!docContabile.getPg_ver_rec().equals(mandato.getPg_ver_rec()))
                throw new ApplicationException("Risorsa non più valida.");

            mandato.setIm_pagato(mandato.getIm_pagato().add(im_riscontro));

            mandato.setUser(userContext.getUser());
            updateBulk(userContext, mandato);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Importo di un Mandato di Accreditamento
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo del mandato di accreditamento
     * associato al riscontro
     * PostCondition:
     * Viene aggiornato l'importo pagato del mandato di accreditamento a cui viene aggiunto
     * l'importo del riscontro ad esso associato
     *
     * @param userContext  lo <code>UserContext</code> che ha generato la richiesta
     * @param docContabile <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                     recuperare alcuni dati relativi al documento contabile associato al riscontro
     * @param im_riscontro <code>BigDecimal</code> l'importo del riscontro
     */
    private void aggiornaImMandatoAccreditamento(UserContext userContext, V_mandato_reversaleBulk docContabile, BigDecimal im_riscontro) throws ComponentException {
        try {
            MandatoBulk mandato = (MandatoBulk) getHome(userContext, MandatoAccreditamentoBulk.class).findAndLock(
                    new MandatoAccreditamentoBulk(
                            docContabile.getCd_cds(),
                            docContabile.getEsercizio(),
                            docContabile.getPg_documento_cont()));
            // verifico che il mandato di accreditamento da associare al riscontro nel frattempo non sia stato modificato
            if (!docContabile.getPg_ver_rec().equals(mandato.getPg_ver_rec()))
                throw new ApplicationException("Risorsa non più valida.");

            mandato.setIm_pagato(mandato.getIm_pagato().add(im_riscontro));

            mandato.setUser(userContext.getUser());
            updateBulk(userContext, mandato);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Importo di una Reversale
     * PreCondition:
     * E' stata generata la richiesta di aggiornare l'importo della reversale associata al riscontro
     * PostCondition:
     * Viene aggiornato l'importo incassato della reversale a cui viene aggiunto l'importo del riscontro
     * ad esso associato
     *
     * @param userContext  lo <code>UserContext</code> che ha generato la richiesta
     * @param docContabile <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                     recuperare alcuni dati relativi al documento contabile associato al riscontro
     * @param im_riscontro <code>BigDecimal</code> l'importo del riscontro
     */
    private void aggiornaImReversale(UserContext userContext, V_mandato_reversaleBulk docContabile, BigDecimal im_riscontro) throws ComponentException {
        try {
            ReversaleBulk reversale = (ReversaleBulk) getHome(userContext, ReversaleIBulk.class).findAndLock(
                    new ReversaleIBulk(
                            docContabile.getCd_cds(),
                            docContabile.getEsercizio(),
                            docContabile.getPg_documento_cont()));
            // verifico che la reversale da associare al riscontro nel frattempo non sia stata modificata
            if (!docContabile.getPg_ver_rec().equals(reversale.getPg_ver_rec()))
                throw new ApplicationException("Risorsa non più valida.");

            reversale.setIm_incassato(reversale.getIm_incassato().add(im_riscontro));

            reversale.setUser(userContext.getUser());
            updateBulk(userContext, reversale);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Stato di una Reversale
     * PreCondition:
     * E' stata generata la richiesta di aggiornare lo stato della reversale associata al riscontro
     * PostCondition:
     * Viene calcolata la somma degli importi dei singoli riscontri associati alla stessa
     * reversale (metodo calcolaTotDettagli)
     * Vengono poi aggiornati documenti contabili o saldi in relazione al sospeso, nel caso che
     * quest'ultimo sia stato annullato e la reversale sia stata incassata oppure
     * nel caso il sospeso sia stato inserito e la reversale non sia stata incassata
     * (metodo aggiornaDaSospesoRiscontro)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param dettaglio   <code>Sospeso_det_etrBulk</code> il dettaglio del sospeso d'entrata
     * @param v_man_rev   <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                    recuperare alcuni dati relativi al documento contabile associato al riscontro
     */
    private void aggiornaStatoDocContabile(UserContext userContext, Sospeso_det_etrBulk dettaglio, V_mandato_reversaleBulk v_man_rev) throws ComponentException {
        try {
            java.math.BigDecimal totDettagli = new BigDecimal(0);
            Sospeso_det_etrHome sospeso_det_etrHome = (Sospeso_det_etrHome) getHome(userContext, Sospeso_det_etrBulk.class);
            totDettagli = sospeso_det_etrHome.calcolaTotDettagli(v_man_rev);

            // se il riscontro e' stato annullato ed il doc. contabile era incassato oppure
            // se il riscontro e' stato inserito e il doc. contabile non era incassato
            if ((v_man_rev.getIm_pagato_incassato().compareTo(v_man_rev.getIm_documento_cont()) == 0 &&
                    Sospeso_det_etrBulk.STATO_ANNULLATO.equals(dettaglio.getStato())) ||
                    (!Sospeso_det_etrBulk.STATO_ANNULLATO.equals(dettaglio.getStato()) &&
                            v_man_rev.getIm_pagato_incassato().compareTo(v_man_rev.getIm_documento_cont()) < 0 &&
                            totDettagli.compareTo(v_man_rev.getIm_documento_cont()) == 0)) {
                // chiamata alla stored procedure
                aggiornaDaSospesoRiscontro(userContext, v_man_rev);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiornamento Stato di un Mandato
     * PreCondition:
     * E' stata generata la richiesta di aggiornare lo stato del mandato associato al riscontro
     * PostCondition:
     * Viene calcolata la somma degli importi dei singoli riscontri associati allo stesso
     * mandato (metodo calcolaTotDettagli)
     * Vengono poi aggiornati documenti contabili o saldi in relazione al sospeso, nel caso che
     * quest'ultimo sia stato annullato ed il mandato sia stato pagato oppure
     * nel caso il sospeso sia stato inserito e il mandato non sia stato pagato
     * (metodo aggiornaDaSospesoRiscontro)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param dettaglio   <code>Sospeso_det_uscBulk</code> il dettaglio del sospeso di spesa
     * @param v_man_rev   <code>V_mandato_reversaleBulk</code> oggetto V_mandato_reversaleBulk per
     *                    recuperare alcuni dati relativi al documento contabile associato al riscontro
     */
    private void aggiornaStatoDocContabile(UserContext userContext, Sospeso_det_uscBulk dettaglio, V_mandato_reversaleBulk v_man_rev) throws ComponentException {
        try {
            java.math.BigDecimal totDettagli = new BigDecimal(0);
            Sospeso_det_uscHome sospeso_det_uscHome = (Sospeso_det_uscHome) getHome(userContext, Sospeso_det_uscBulk.class);
            totDettagli = sospeso_det_uscHome.calcolaTotDettagli(v_man_rev);

            // se il riscontro e' stato annullato ed il doc. contabile era pagato oppure
            // se il riscontro e' stato inserito e il doc. contabile non era pagato
            if ((v_man_rev.getIm_pagato_incassato().compareTo(v_man_rev.getIm_documento_cont()) == 0 &&
                    Sospeso_det_uscBulk.STATO_ANNULLATO.equals(dettaglio.getStato())) ||
                    (!Sospeso_det_uscBulk.STATO_ANNULLATO.equals(dettaglio.getStato()) &&
                            v_man_rev.getIm_pagato_incassato().compareTo(v_man_rev.getIm_documento_cont()) < 0 &&
                            totDettagli.compareTo(v_man_rev.getIm_documento_cont()) == 0)) {
                // chiamata alla stored procedure
                aggiornaDaSospesoRiscontro(userContext, v_man_rev);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * annullamento Dettaglio d'Entrata di un Riscontro
     * PreCondition:
     * E' stata generata la richiesta di annullare il dettaglio d'Entrata di un riscontro
     * PostCondition:
     * Viene impostato lo stato del dettaglio a ANNULLATO
     * Viene aggiornato l'importo incassato della reversale associata al riscontro che sta
     * per essere annullato (metodo aggiornaIm_pagato_incassato)
     * Viene aggiornato lo stato della reversale associata al riscontro che sta per essere
     * annullato (metodo aggiornaStatoDocContabile)
     * annullamento Dettaglio di Spesa di un Riscontro
     * PreCondition:
     * E' stata generata la richiesta di annullare il dettaglio di Spesa di un riscontro
     * PostCondition:
     * Viene impostato lo stato del dettaglio a ANNULLATO.
     * Viene aggiornato l'importo pagato del mandato associato al riscontro che sta
     * per essere annullato (metodo aggiornaIm_pagato_incassato)
     * Viene aggiornato lo stato del mandato associato al riscontro che sta per essere
     * annullato (metodo aggiornaStatoDocContabile)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>SospesoBulk</code> il riscontro da annullare
     */
    private void annullaDettaglioSospeso(UserContext userContext, SospesoBulk sospeso) throws ComponentException {
        try {
            if (sospeso.getDettaglio_etr() != null) {
                sospeso.getDettaglio_etr().setStato(Sospeso_det_etrBulk.STATO_ANNULLATO);
                sospeso.getDettaglio_etr().setUser(userContext.getUser());
                updateBulk(userContext, sospeso.getDettaglio_etr());
                V_mandato_reversaleBulk v_man_rev = (V_mandato_reversaleBulk) getHome(userContext, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(sospeso.getDettaglio_etr().getEsercizio(), Numerazione_doc_contBulk.TIPO_REV, sospeso.getDettaglio_etr().getCd_cds(), sospeso.getDettaglio_etr().getPg_reversale()));
                aggiornaIm_pagato_incassato(userContext, sospeso.getDettaglio_etr(), v_man_rev);
                aggiornaStatoDocContabile(userContext, sospeso.getDettaglio_etr(), v_man_rev);

            } else if (sospeso.getDettaglio_usc() != null) {
                sospeso.getDettaglio_usc().setStato(Sospeso_det_uscBulk.STATO_ANNULLATO);
                sospeso.getDettaglio_usc().setUser(userContext.getUser());
                updateBulk(userContext, sospeso.getDettaglio_usc());
                V_mandato_reversaleBulk v_man_rev = (V_mandato_reversaleBulk) getHome(userContext, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(sospeso.getDettaglio_usc().getEsercizio(), Numerazione_doc_contBulk.TIPO_MAN, sospeso.getDettaglio_usc().getCd_cds(), sospeso.getDettaglio_usc().getPg_mandato()));
                aggiornaIm_pagato_incassato(userContext, sospeso.getDettaglio_usc(), v_man_rev);
                aggiornaStatoDocContabile(userContext, sospeso.getDettaglio_usc(), v_man_rev);

            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * stato IN SOSPESO
     * PreCondition:
     * E' stata generata la richiesta di modificare lo stato INIZIALE di un insieme di Sospesi di Entrata CNR nello stato
     * IN SOSPESO
     * PostCondition:
     * Ad ogni Sospeso d'Entrata del CNR viene impostato lo stato a IN SOSPESO
     * stato ASSOCIATO A CDS
     * PreCondition:
     * E' stata generata la richiesta di modificare lo stato INIZIALE di un insieme di Sospesi di Entrata CNR nello stato
     * ASSOCIATO A CDS ed e' stato specificato il Cds a cui assegnare il sospeso
     * PostCondition:
     * Ad ogni Sospeso d'Entrata del CNR viene impostato lo stato a ASSOCIATO A CDS e viene impostato il codice del Cds origine
     * al valore specificato dall'utente
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param sospesi     la collezione di <code>SospesoBulk</code> per cui cambiare lo stato
     * @param nuovoStato  il nuovo stato (IN SOSPESO o ASSOCIATO A CDS) da impostare al sospeso
     * @param cd_cds      il codice del cds da impostare al cd_cds_origine del sospeso nel caso di nuovoStato = ASSOCIATO A CDS
     *                    null nel caso di nuovoStato = IN SOSPESO
     */

    public void cambiaStato(it.cnr.jada.UserContext userContext, Collection sospesi, String nuovoStato, String cd_cds) throws ComponentException {
        SospesoBulk sospeso;
        try {
            for (Iterator i = sospesi.iterator(); i.hasNext(); ) {
                sospeso = (SospesoBulk) i.next();
                sospeso.setStato_sospeso(nuovoStato);
                if (SospesoBulk.STATO_SOSP_ASS_A_CDS.equals(nuovoStato))
                    sospeso.setCd_cds_origine(cd_cds);
                sospeso.setUser(userContext.getUser());
                updateBulk(userContext, sospeso);
            }
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Esegue una operazione di ricerca di un OggettoBulk con clausole.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Clausole non specificate
     * Pre: L'albero delle clausole non è specficato (nullo)
     * Post: Viene generato un albero di clausole usando tutti i valori non nulli degli
     * attributi dell'OggettoBulk specificato come prototipo. L'elenco degli
     * attributi da utilizzare per ottenere le clausole è estratto dal
     * BulkInfo dell'OggettoBulk
     * <p>
     * Nome: Tutti i controlli superati
     * Pre: Albero delle clausole di ricerca specificato (non nullo)
     * Post: Viene effettuata una ricerca di OggettoBulk compatibili con il bulk specificato.
     * La ricerca deve essere effettuata utilizzando le clausole specificate da "clausole".
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
     * ottenuto concatenando il nome della component con la stringa ".find"
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    clausole    Una CompoundFindClause che descrive l'albero di clausole
     * da applicare nella ricerca
     * @param    bulk    l'OggettoBulk che è stato usato come prototipo per la generazione
     * delle clausole di ricerca.
     * @return Un RemoteIterator sul risultato della ricerca
     */
    public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clausole, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            if (bulk instanceof SelezionaSospesiCNRBulk)
                return iterator(userContext,
                        select(userContext, clausole, bulk),
                        SospesoBulk.class,
                        getFetchPolicyName("find"));
            else if (bulk instanceof ListaSospesiBulk)
                return iterator(userContext,
                        select(userContext, clausole, bulk),
                        SospesoBulk.class,
                        getFetchPolicyName("find"));
            return super.cerca(userContext, clausole, bulk);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * creazione Sospeso
     * PreCondition:
     * E' stata generata la richiesta di creazione di un Sospeso e il sospeso supera la validazione
     * (metodo verificaSospesoRiscontro)
     * PostCondition:
     * Per i Sospesi del CNR vengono resettati il cds di origine e l'unità organizzativa di
     * origine e viene impostato lo stato del sospeso a INIZIALE.
     * In tutti gli altri casi viene resettata l'unità organizzativa di origine, viene impostato il cds
     * di origine con quello di scrivania e lo stato del sospeso a ASSOCIATO A CDS.
     * Creazione di un Sospeso - errore
     * PreCondition:
     * la richiesta di creazione di un sospeso e' stata generata ed esiste un altro sospeso con la stessa chiave
     * PostCondition:
     * una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
     * creazione Riscontro
     * PreCondition:
     * E' stata generata la richiesta di creazione di un Riscontro e il riscontro supera la validazione
     * (metodo verificaSospesoRiscontro)
     * PostCondition:
     * Viene aggiornato l'importo del riscontro associato al documento contabile (mandato o reversale).
     * Viene poi creato un dettaglio di riscontro (metodo creaDettaglioSospeso), di tipo spesa o entrata, a seconda se
     * il riscontro è stato associato rispettivamente a un mandato o a una reversale
     * Creazione di un Riscontro - errore
     * PreCondition:
     * la richiesta di creazione di un riscontro e' stata generata ed esiste un altro riscontro con la stessa chiave
     * PostCondition:
     * una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da creare
     * @return OggettoBulk il Sospeso o Riscontro creato
     */
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            SospesoBulk sospeso = (SospesoBulk) bulk;
            validaCreaConBulk(userContext, bulk);
            verificaSospesoRiscontro(userContext, sospeso);

            if (SospesoBulk.TI_RISCONTRO.equals(sospeso.getTi_sospeso_riscontro()))
                sospeso.setIm_associato(sospeso.getIm_sospeso());

            //sospeso
            if (SospesoBulk.TI_SOSPESO.equals(sospeso.getTi_sospeso_riscontro())) {
                SQLBuilder sql = getHome(userContext, EnteBulk.class).createSQLBuilder();
                EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).fetchAll(sql).get(0);
                //crea sospeso figlio
                SospesoBulk figlio = new SospesoBulk();
                int index = sospeso.addToSospesiFigliColl(figlio);
                figlio = (SospesoBulk) sospeso.getSospesiFigliColl().get(0);
                if (figlio != null)
                    figlio.setIm_sospeso(sospeso.getIm_sospeso());

                if (!sospeso.getCd_cds().equals(ente.getCd_unita_organizzativa())) {
                    figlio.setCd_cds_origine(sospeso.getCd_cds());
                    figlio.setCd_uo_origine(null);
                    figlio.setStato_sospeso(SospesoBulk.STATO_SOSP_ASS_A_CDS);
                }

            }

            makeBulkPersistent(userContext, sospeso);

            if (sospeso.getTi_sospeso_riscontro().equals(SospesoBulk.TI_RISCONTRO))
                creaDettaglioSospeso(userContext, sospeso);

            return sospeso;
        } catch (CRUDDuplicateKeyException e) {
            throw handleException(new ApplicationException("Errore di chiave duplicata per Sospeso/Riscontro."));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * creazione Dettaglio di un riscontro (Entrata)
     * PreCondition:
     * E' stata generata la richiesta di creazione del dettaglio di un riscontro
     * PostCondition:
     * Viene creato un dettaglio d'entrata nel caso il riscontro sia stato associato a
     * una Reversale (metodo creaDettaglioSospesoEtr).
     * creazione Dettaglio di un riscontro (Spesa)
     * PreCondition:
     * E' stata generata la richiesta di creazione del dettaglio di un riscontro
     * PostCondition:
     * Viene creato un dettaglio di spesa nel caso il riscontro sia stato associato a
     * un Mandato (metodo creaDettaglioSospesoUsc).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>SospesoBulk</code> il riscontro corrente
     */
    private void creaDettaglioSospeso(UserContext userContext, SospesoBulk sospeso) throws ComponentException {
        if (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA))
            creaDettaglioSospesoEtr(userContext, sospeso);
        else if (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_SPESA))
            creaDettaglioSospesoUsc(userContext, sospeso);
    }

    /**
     * creazione Dettaglio di Entrata di un riscontro
     * PreCondition:
     * E' stata generata la richiesta di creazione del dettaglio di entrata
     * di un riscontro
     * PostCondition:
     * Viene creato un dettaglio d'entrata del riscontro che contiene i dati
     * del riscontro appena creato e viene impostato lo stato a DEFAULT.
     * Viene aggiornato l'importo incassato della reversale associata al riscontro
     * (metodo aggiornaIm_pagato_incassato).
     * Viene aggiornato lo stato della reversale associata al riscontro
     * (metodo aggiornaStatoDocContabile).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>SospesoBulk</code> il riscontro corrente
     */
    private void creaDettaglioSospesoEtr(UserContext userContext, SospesoBulk sospeso) throws ComponentException {
        try {
            Sospeso_det_etrBulk det_etr;

            Collection result = ((SospesoHome) getHome(userContext, sospeso.getClass())).findSospeso_det_etrCollEsteso(sospeso);
            for (Iterator i = result.iterator(); i.hasNext(); ) {
                det_etr = (Sospeso_det_etrBulk) i.next();
                if (det_etr.getPg_reversale().compareTo(sospeso.getV_man_rev().getPg_documento_cont()) == 0) {
                    det_etr.setStato(Sospeso_det_etrBulk.STATO_DEFAULT);
                    det_etr.setUser(userContext.getUser());
                    updateBulk(userContext, det_etr);
                    aggiornaIm_pagato_incassato(userContext, det_etr, sospeso.getV_man_rev());
                    aggiornaStatoDocContabile(userContext, det_etr, sospeso.getV_man_rev());
                    return;
                }
            }

            det_etr = new Sospeso_det_etrBulk();

            det_etr.setToBeCreated();
            det_etr.setUser(sospeso.getUser());

            det_etr.setCd_cds(sospeso.getCd_cds());
            det_etr.setEsercizio(sospeso.getEsercizio());
            det_etr.setPg_reversale(sospeso.getV_man_rev().getPg_documento_cont());
            det_etr.setTi_entrata_spesa(sospeso.getTi_entrata_spesa());
            det_etr.setTi_sospeso_riscontro(SospesoBulk.TI_RISCONTRO);
            det_etr.setCd_sospeso(sospeso.getCd_sospeso());
            det_etr.setIm_associato(sospeso.getIm_sospeso());
            det_etr.setStato(Sospeso_det_etrBulk.STATO_DEFAULT);
            det_etr.setCd_cds_reversale(sospeso.getV_man_rev().getCd_cds());
            insertBulk(userContext, det_etr);
            aggiornaIm_pagato_incassato(userContext, det_etr, sospeso.getV_man_rev());
            aggiornaStatoDocContabile(userContext, det_etr, sospeso.getV_man_rev());
        } catch (Exception e) {
            throw handleException(sospeso, e);
        }
    }

    /**
     * creazione Dettaglio di Spesa di un riscontro
     * PreCondition:
     * E' stata generata la richiesta di creazione del dettaglio di spesa
     * di un riscontro
     * PostCondition:
     * Viene creato un dettaglio di spesa del riscontro che contiene i dati
     * del riscontro appena creato e viene impostato lo stato a DEFAULT.
     * Viene aggiornato l'importo pagato del mandato associato al riscontro
     * (metodo aggiornaIm_pagato_incassato).
     * Viene aggiornato lo stato del mandato associato al riscontro
     * (metodo aggiornaStatoDocContabile).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>SospesoBulk</code> il riscontro corrente
     */
    private void creaDettaglioSospesoUsc(UserContext userContext, SospesoBulk sospeso) throws ComponentException {
        try {
            Sospeso_det_uscBulk det_usc;

            Collection result = ((SospesoHome) getHome(userContext, sospeso.getClass())).findSospeso_det_uscCollEsteso(sospeso);
            for (Iterator i = result.iterator(); i.hasNext(); ) {
                det_usc = (Sospeso_det_uscBulk) i.next();
                if (det_usc.getPg_mandato().compareTo(sospeso.getV_man_rev().getPg_documento_cont()) == 0) {
                    det_usc.setStato(Sospeso_det_uscBulk.STATO_DEFAULT);
                    det_usc.setUser(userContext.getUser());
                    updateBulk(userContext, det_usc);
                    aggiornaIm_pagato_incassato(userContext, det_usc, sospeso.getV_man_rev());
                    aggiornaStatoDocContabile(userContext, det_usc, sospeso.getV_man_rev());
                    return;
                }
            }

            det_usc = new Sospeso_det_uscBulk();
            det_usc.setToBeCreated();
            det_usc.setUser(sospeso.getUser());

            det_usc.setCd_cds(sospeso.getCd_cds());
            det_usc.setEsercizio(sospeso.getEsercizio());
            det_usc.setPg_mandato(sospeso.getV_man_rev().getPg_documento_cont());
            det_usc.setTi_entrata_spesa(SospesoBulk.TIPO_SPESA);
            det_usc.setTi_sospeso_riscontro(SospesoBulk.TI_RISCONTRO);
            det_usc.setCd_sospeso(sospeso.getCd_sospeso());
            det_usc.setIm_associato(sospeso.getIm_sospeso());
            det_usc.setStato(Sospeso_det_uscBulk.STATO_DEFAULT);
            det_usc.setCd_cds_mandato(sospeso.getV_man_rev().getCd_cds());
            insertBulk(userContext, det_usc);
            aggiornaIm_pagato_incassato(userContext, det_usc, sospeso.getV_man_rev());
            aggiornaStatoDocContabile(userContext, det_usc, sospeso.getV_man_rev());

        } catch (Exception e) {
            throw handleException(sospeso, e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative alle Reversali
     *
     * @return ReversaleComponentSession l'istanza di <code>ReversaleComponentSession</code> che serve per gestire una reversale
     */
    private ReversaleComponentSession createReversaleComponentSession() throws ComponentException {
        try {
            return (ReversaleComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ReversaleComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * eliminazione Sospeso
     * PreCondition:
     * E' stata generata la richiesta di eliminare un sospeso e il sospeso supera la validazione
     * (metodo validaEliminaConBulk)
     * PostCondition:
     * Viene impostato il flag stornato del sospeso a TRUE.
     * eliminazione Riscontro
     * PreCondition:
     * E' stata generata la richiesta di eliminare un riscontro e il riscontro supera la validazione
     * (metodo validaEliminaConBulk)
     * PostCondition:
     * Viene impostato il flag stornato del riscontro a TRUE.
     * Viene annullato l'eventuale dettaglio del riscontro che sta per essere cancellato
     * (metodo annullaDettaglioSospeso)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da cancellare
     */
    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            SospesoBulk sospeso = (SospesoBulk) bulk;

            validaEliminaConBulk(userContext, sospeso);

            sospeso.setFl_stornato(Boolean.TRUE);
            sospeso.setDt_storno(DateUtils.truncate(EJBCommonServices.getServerDate()));
            sospeso.setUser(userContext.getUser());
            updateBulk(userContext, sospeso);

            //annullo anche i figli
            SospesoBulk figlio;
            for (Iterator i = sospeso.getSospesiFigliColl().iterator(); i.hasNext(); ) {
                figlio = (SospesoBulk) i.next();
                figlio.setFl_stornato(Boolean.TRUE);
                figlio.setDt_storno(DateUtils.truncate(EJBCommonServices.getServerDate()));
                figlio.setUser(sospeso.getUser());
                updateBulk(userContext, figlio);
            }


            if (SospesoBulk.TI_RISCONTRO.equals(sospeso.getTi_sospeso_riscontro()))
                annullaDettaglioSospeso(userContext, sospeso);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

        try {
            return getHome(userContext, SospesoBulk.class).getServerDate();
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * inizializzazione di una istanza di SospesoBulk
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di SospesoBulk
     * PostCondition:
     * Vengono impostati l'esercizio con l'esercizio di scrivania, il flag
     * stornato a FALSE, il cds e il cds origine rispettivamente con il cds
     * e il cds origine di scrivania, l'importo associato a documenti contabili
     * e l'importo associato a modello 1210 a 0, la data di registrazione con la
     * data del Server
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per l'inserimento
     * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per l'inserimento
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            bulk = super.inizializzaBulkPerInserimento(userContext, bulk);
            SospesoBulk sospeso = (SospesoBulk) bulk;
            sospeso.setEsercizio(((CNRUserContext) userContext).getEsercizio());
            sospeso.setFl_stornato(new Boolean(false));
            sospeso.setCds((CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(((CNRUserContext) userContext).getCd_cds())));
            verificaStatoEsercizio(userContext, sospeso.getEsercizio(), sospeso.getCd_cds());
		/* ?????
		sospeso.setCd_cds_origine( ((CNRUserContext) userContext ).getCd_cds());
		*/
            //	sospeso.setCd_uo_origine( ((CNRUserContext) userContext).getCd_unita_organizzativa());
            sospeso.setIm_associato(new BigDecimal(0));
            sospeso.setIm_ass_mod_1210(new BigDecimal(0));
            sospeso.setDt_registrazione(DateServices.getDt_valida(userContext));
            return sospeso;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzazione di una istanza di SospesoBulk per modifica
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di SospesoBulk
     * PostCondition:
     * Viene caricata la collezione dei dettagli d'entrata o di spesa del riscontro
     * (rispettivamente Sospeso_det_etrBulk e Sospeso_det_uscBulk) e delle
     * associazioni mandato-reversale( Ass_mandato_reversaleBulk).
     * inizializzazione di una istanza di SospesoBulk per modifica - errore dettaglio d'Entrata
     * PreCondition:
     * E' stata richiesta l'inizializzazione di un riscontro d'entrata
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare che non è stata
     * associata nessuna reversale al riscontro
     * inizializzazione di una istanza di SospesoBulk per modifica - errore associazione mandato-reversale
     * PreCondition:
     * E' stata richiesta l'inizializzazione di un riscontro d'entrata
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare che non esiste
     * nessuna associazione mandato-reversale per il riscontro
     * inizializzazione di una istanza di SospesoBulk per modifica - errore dettaglio di Spesa
     * PreCondition:
     * E' stata richiesta l'inizializzazione di un riscontro di spesa
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare che non è stato
     * associato nessun mandato al riscontro
     * inizializzazione di una istanza di SospesoBulk per modifica - errore associazione mandato-reversale
     * PreCondition:
     * E' stata richiesta l'inizializzazione di un riscontro di spesa
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare che non esiste
     * nessuna associazione mandato-reversale per il riscontro
     * inizializzazione di una istanza di Sospeso  CNR per modifica
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di Sospeso d'Entrata CNR
     * PostCondition:
     * Viene caricato il sospeso CNR con i relativi accertamenti e
     * reversali ad esso associati. Sono inoltre visualizzate le unità organizzative
     * che hanno utilizzato il sospeso e la voce di bilancio CNR degli accertamenti
     * imputati.
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la modifica
     * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la modifica
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        SospesoBulk sospeso = (SospesoBulk) super.inizializzaBulkPerModifica(aUC, bulk);
        try {
            if (sospeso.getTi_sospeso_riscontro().equals(SospesoBulk.TI_RISCONTRO)) {
                SospesoHome sospesoHome = (SospesoHome) getHome(aUC, sospeso.getClass());

                if (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA)) {
                    //carico i sospesi associati alla reversale selezionata
                    List result = (List) sospesoHome.findSospeso_det_etrColl(sospeso);
                    if (result.size() != 1)
                        throw new ApplicationException("Non è stata associata nessuna reversale al riscontro.");

                    sospeso.setDettaglio_etr((Sospeso_det_etrBulk) result.get(0));
                    V_mandato_reversaleBulk man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(sospeso.getEsercizio(), Numerazione_doc_contBulk.TIPO_REV, sospeso.getCd_cds(), sospeso.getDettaglio_etr().getPg_reversale()));
                    if (man_rev == null)
                        throw new ApplicationException("Non è stata associata nessuna reversale al riscontro.");
                    sospeso.setV_man_rev(man_rev);

                    sospeso.setManRevRiportato(isAssocRiscontroEntrataDocContAnnullabile(aUC, sospeso));

                } else if (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_SPESA)) {
                    //carico i sospesi associati al mandato selezionato
                    List result = (List) sospesoHome.findSospeso_det_uscColl(sospeso);
                    if (result.size() != 1)
                        throw new ApplicationException("Non è stato associato nessun mandato al riscontro.");

                    sospeso.setDettaglio_usc((Sospeso_det_uscBulk) result.get(0));
                    V_mandato_reversaleBulk man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(sospeso.getEsercizio(), Numerazione_doc_contBulk.TIPO_MAN, sospeso.getCd_cds(), sospeso.getDettaglio_usc().getPg_mandato()));
                    if (man_rev == null)
                        throw new ApplicationException("Non è stato associato nessun mandato al riscontro.");

                    sospeso.setV_man_rev(man_rev);

                    sospeso.setManRevRiportato(isAssocRiscontroSpesaDocContAnnullabile(aUC, sospeso));


                }
            } else if (sospeso.getTi_sospeso_riscontro().equals(SospesoBulk.TI_SOSPESO)) {
                //carico la lista dei sospesi figli

                sospeso.setSospesiFigliColl(new BulkList(((SospesoHome) getHome(aUC, SospesoBulk.class)).findSospesiFigliColl(aUC, sospeso)));

                //carico la lista delle reversali e degli accertamenti associati al sospeso
                if (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA)) {
                    SQLBuilder sql = getHome(aUC, V_sospeso_rev_accertBulk.class).createSQLBuilder();
                    sql.setDistinctClause(true);
                    sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, sospeso.getEsercizio());
                    sql.addSQLClause("AND", "cd_cds", SQLBuilder.EQUALS, sospeso.getCd_cds());
                    sql.addSQLClause("AND", "ti_sospeso_riscontro", SQLBuilder.EQUALS, sospeso.getTi_sospeso_riscontro());
                    sql.addSQLClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, sospeso.getTi_entrata_spesa());
                    sql.addSQLClause("AND", "cd_sospeso_padre", SQLBuilder.EQUALS, sospeso.getCd_sospeso());

                    sospeso.setReversaliAccertamentiColl(getHome(aUC, V_sospeso_rev_accertBulk.class).fetchAll(sql));

                } else {
                    //carico la lista dei mandati e degli impegni associati al sospeso
                    SQLBuilder sql = getHome(aUC, V_sospeso_man_impBulk.class).createSQLBuilder();
                    sql.setDistinctClause(true);
                    sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, sospeso.getEsercizio());
                    sql.addSQLClause("AND", "cd_cds", SQLBuilder.EQUALS, sospeso.getCd_cds());
                    sql.addSQLClause("AND", "ti_sospeso_riscontro", SQLBuilder.EQUALS, sospeso.getTi_sospeso_riscontro());
                    sql.addSQLClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, sospeso.getTi_entrata_spesa());
                    sql.addSQLClause("AND", "cd_sospeso_padre", SQLBuilder.EQUALS, sospeso.getCd_sospeso());

                    sospeso.setMandatiImpegniColl(getHome(aUC, V_sospeso_man_impBulk.class).fetchAll(sql));

                    SQLBuilder sql1210 = getHome(aUC, Lettera_pagam_esteroBulk.class).createSQLBuilder();
                    sql1210.setDistinctClause(true);
                    sql1210.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, sospeso.getEsercizio());
                    sql1210.addSQLClause("AND", "cd_cds_sospeso", SQLBuilder.EQUALS, sospeso.getCd_cds());
                    sql1210.addSQLClause("AND", "ti_sospeso_riscontro", SQLBuilder.EQUALS, sospeso.getTi_sospeso_riscontro());
                    sql1210.addSQLClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, sospeso.getTi_entrata_spesa());
                    sql1210.addSQLClause("AND", "cd_sospeso", SQLBuilder.CONTAINS, sospeso.getCd_sospeso());
                    sospeso.setLettereColl(getHome(aUC, Lettera_pagam_esteroBulk.class).fetchAll(sql1210));
                }
            }
        } catch (Exception e) {
            throw handleException(sospeso, e);
        }
        return sospeso;

    }

    /**
     * inizializzazione di una istanza di SospesoBulk per ricerca
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di SospesoBulk per ricerca
     * PostCondition:
     * Viene inizializzata l'istanza di SospesoBulk, impostando l'esercizio e il cds
     * rispettivamente con l'esercizio e il cds di scrivania e flag stornato a FALSE.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la ricerca
     * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la ricerca
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            bulk = super.inizializzaBulkPerRicerca(userContext, bulk);
            if (bulk instanceof SospesoBulk) {
                SospesoBulk sospeso = (SospesoBulk) bulk;
                sospeso.setEsercizio(((CNRUserContext) userContext).getEsercizio());
                sospeso.setFl_stornato(new Boolean(false));
                sospeso.setCds((CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(((CNRUserContext) userContext).getCd_cds())));
                // sospeso.setCd_cds_origine( ((CNRUserContext) userContext ).getCd_cds());
                // sospeso.setCd_uo_origine( ((CNRUserContext) userContext).getCd_unita_organizzativa());
            }
		/*
		else if ( bulk instanceof ListaSospesiBulk )
		{
			Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
			if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa()))
				 throw new ApplicationException("Funzione non consentita per utente non abilitato a " + uoEnte.getCd_unita_organizzativa() );
			((ListaSospesiBulk) bulk).setSospesi_cnrColl( ((SospesoHome)getHome( userContext, SospesoBulk.class)).findSospesiCNR( ((CNRUserContext)userContext).getEsercizio()));
		}
		*/
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzazione di una istanza di SospesoBulk per ricerca libera
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di SospesoBulk per ricerca libera
     * PostCondition:
     * Viene inizializzata l'istanza di SospesoBulk, impostando l'esercizio e il cds
     * rispettivamente con l'esercizio e il cds di scrivania e flag stornato a FALSE.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da inizializzare per la ricerca libera
     * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro inizializzato per la ricerca libera
     */
    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            bulk = super.inizializzaBulkPerRicerca(userContext, bulk);
            SospesoBulk sospeso = (SospesoBulk) bulk;
            sospeso.setEsercizio(((CNRUserContext) userContext).getEsercizio());
            sospeso.setFl_stornato(new Boolean(false));
            sospeso.setCds((CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(((CNRUserContext) userContext).getCd_cds())));
            // sospeso.setCd_cds_origine( ((CNRUserContext) userContext ).getCd_cds());
            // sospeso.setCd_uo_origine( ((CNRUserContext) userContext).getCd_unita_organizzativa());
            return sospeso;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_riscontriVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setTi_entrata_spesa(Stampa_sospesi_riscontriVBulk.TIPO_ENTRATA_SPESA);

        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_sospesi_cnr_assoc_cdsVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        String cd_cds_scrivania = CNRUserContext.getCd_cds(userContext);
        stampa.setCd_cds(cd_cds_scrivania);

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setTi_entrata_spesa(Stampa_sospesi_riscontriVBulk.TIPO_ENTRATA_SPESA);

        try {
            CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
            CdsBulk cds_scrivania = (CdsBulk) cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

            if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
                stampa.setIsCdsEnte(true);
                stampa.setCdsForPrint(new CdsBulk());
            } else {
                stampa.setIsCdsEnte(false);
            }


        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_sospesi_cnr_assoc_manVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        String cd_cds_scrivania = CNRUserContext.getCd_cds(userContext);
        stampa.setCd_cds(cd_cds_scrivania);

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));

        try {
            CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
            CdsBulk cds_scrivania = (CdsBulk) cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

            if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
                stampa.setIsCdsEnte(true);
                stampa.setCdsForPrint(new CdsBulk());
            } else {
                stampa.setIsCdsEnte(false);
            }


        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_sospesi_cnr_assoc_revVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));

        String cd_uo = CNRUserContext.getCd_unita_organizzativa(userContext);

        try {
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

            if (!uo.isUoCds()) {
                stampa.setUoForPrint(uo);
                stampa.setIsUOForPrintEnabled(false);
            } else {
                stampa.setUoForPrint(new Unita_organizzativaBulk());
                stampa.setIsUOForPrintEnabled(true);
            }

        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_sospesi_da_assegnareVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));

        // Se l'esercizio di scrivania < esercizio attuale, imposta dt_fine = 31/12 dell'esercizio
        //	di scrivania; altrimenti la dt_fine = data odierna.
        try {
            if (DateServices.isAnnoMaggEsScriv(userContext)) {
                stampa.setDataFine(DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
            } else {
                stampa.setDataFine(getDataOdierna(userContext));
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(e);
        }

        stampa.setTi_entrata_spesa(Stampa_sospesi_riscontriVBulk.TIPO_ENTRATA_SPESA);
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_sospesiVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setTi_entrata_spesa(Stampa_sospesi_riscontriVBulk.TIPO_ENTRATA_SPESA);
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {


        if (bulk instanceof Stampa_riscontriVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_riscontriVBulk) bulk);
        else if (bulk instanceof Stampa_sospesiVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_sospesiVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_revVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_sospesi_cnr_assoc_revVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_cdsVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_sospesi_cnr_assoc_cdsVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_manVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_sospesi_cnr_assoc_manVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_da_assegnareVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_sospesi_da_assegnareVBulk) bulk);


        return bulk;
    }

    /**
     *
     */
    protected boolean isAssocRiscontroEntrataDocContAnnullabile(UserContext userContext, SospesoBulk riscontro) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select b.riportato " +
                            "from reversale_riga a, accertamento b " +
                            "where " +
                            "a.cd_cds = b.cd_cds and " +
                            "a.esercizio_accertamento = b.esercizio and " +
                            "a.esercizio_ori_accertamento = b.esercizio_originale and " +
                            "a.pg_accertamento = b.pg_accertamento and " +
                            "a.esercizio = ? and " +
                            "a.cd_cds = ? and " +
                            "a.pg_reversale = ? ", true, this.getClass());
            try {
                ps.setObject(1, riscontro.getV_man_rev().getEsercizio());
                ps.setString(2, riscontro.getV_man_rev().getCd_cds());
                ps.setObject(3, riscontro.getV_man_rev().getPg_documento_cont());
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        String riportato = rs.getString(1);
                        return (!"N".equals(riportato));
                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }

        return true;

    }

    /**
     *
     */
    protected boolean isAssocRiscontroSpesaDocContAnnullabile(UserContext userContext, SospesoBulk riscontro) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select b.riportato " +
                            "from mandato_riga a, obbligazione b " +
                            "where " +
                            "a.cd_cds = b.cd_cds and " +
                            "a.esercizio_obbligazione = b.esercizio and " +
                            "a.esercizio_ori_obbligazione = b.esercizio_originale and " +
                            "a.pg_obbligazione = b.pg_obbligazione and " +
                            "a.esercizio = ? and " +
                            "a.cd_cds = ? and " +
                            "a.pg_mandato = ? ", true, this.getClass());
            try {
                ps.setObject(1, riscontro.getV_man_rev().getEsercizio());
                ps.setString(2, riscontro.getV_man_rev().getCd_cds());
                ps.setObject(3, riscontro.getV_man_rev().getPg_documento_cont());
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        //int esercizio = rs.getInt(1);
                        //String cd_cds = rs.getString(2);
                        //long pg_obbligazione = rs.getLong(3);
                        //String fl_pgiro = rs.getString(4);
                        String riportato = rs.getString(1);
                        return (!"N".equals(riportato));
                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }

        return true;
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto per il Cds specificato
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta l'assegnazione di un sospeso ad un Cds
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    private boolean isEsercizioChiusoFor(UserContext userContext, String cd_cds) throws ComponentException {
        try {
            Integer esercizio = CNRUserContext.getEsercizio(userContext);
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiuso(userContext, esercizio, cd_cds);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * modifica Sospeso
     * PreCondition:
     * E' stata generata la richiesta di modifica di un Sospeso e il sospeso supera la validazione
     * (metodo verificaSospesoRiscontro)
     * PostCondition:
     * Vengono aggiornate le eventuali modifiche alla descrizione o alla causale del sospeso
     * modifica Riscontro
     * PreCondition:
     * E' stata generata la richiesta di modifica di un Riscontro e il riscontro supera la validazione
     * (metodo verificaSospesoRiscontro)
     * PostCondition:
     * Nel caso il documento contabile (mandato o reversale) inserito dall'utente sia diverso da quello
     * precedentemente associato al riscontro, viene annullato il dettaglio di spesa o d'entrata del
     * doc. contabile precedente (metodo annullaDettaglioSospeso) e viene creato un nuovo dettaglio di
     * riscontro (metodo creaDettaglioSospeso).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da modificare
     * @return sospeso <code>OggettoBulk</code> il sospeso o riscontro modificato
     */
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            SospesoBulk sospeso = (SospesoBulk) bulk;

            if (SospesoBulk.TI_SOSPESO.equals(sospeso.getTi_sospeso_riscontro()) &&
                    sospeso.getSospesiFigliColl().size() == 0)
                throw new ApplicationException("Deve essere definito almeno un dettaglio.");

            if (sospeso.isToBeUpdated()) {
                //se il sospeso è stato modificato, aggiorno anche i figli
                SospesoBulk figlio;
                for (Iterator i = sospeso.getSospesiFigliColl().iterator(); i.hasNext(); ) {
                    figlio = (SospesoBulk) i.next();
                    figlio.setCausale(sospeso.getCausale());
                    figlio.setDs_anagrafico(sospeso.getDs_anagrafico());
                    figlio.setToBeUpdated();
                }
            }

            verificaSospesoRiscontro(userContext, sospeso);
            makeBulkPersistent(userContext, sospeso);

            if (sospeso.getTi_sospeso_riscontro().equals(SospesoBulk.TI_RISCONTRO)) {
                if ((sospeso.getDettaglio_etr() != null && sospeso.getDettaglio_etr().getPg_reversale().compareTo(
                        sospeso.getV_man_rev().getPg_documento_cont()) != 0) ||
                        (sospeso.getDettaglio_usc() != null && sospeso.getDettaglio_usc().getPg_mandato().compareTo(
                                sospeso.getV_man_rev().getPg_documento_cont()) != 0)) {
                    annullaDettaglioSospeso(userContext, sospeso);
                    creaDettaglioSospeso(userContext, sospeso);
                }
            }

            return sospeso;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca eseguite su sospesi o riscontri
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca di un sospeso
     * Pre:  E' stata generata la richiesta di ricerca di un sospeso
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente
     *
     * Nome: Richiesta di ricerca di un riscontro
     * Pre:  E' stata generata la richiesta di ricerca di un riscontro
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, se l'
     *		 utente ha valorizzato il campo del codice relativo al documento contabile, viene eseguita una ricerca
     *		 in join sulle rispettive tabelle di dettaglio d'entrata o di spesa del riscontro, a seconda che l'utente
     *		 abbia inserito il codice di una reversale o di un mandato
     *
     * Nome: Richiesta di ricerca di un riscontro - errore
     * Pre:  E' stata generata la richiesta di ricerca di un riscontro e l'utente ha inserito un codice di documento
     *		 contabile non esistente nel database
     * Post: Un messaggio di errore viene visualizzato all'utente per segnalare che non esiste nessun documento
     *		 contabile (mandato o reversale) con tale codice
     *
     * Nome: Richiesta di ricerca di un riscontro - errore
     * Pre:  E' stata generata la richiesta di ricerca di un riscontro e l'utente ha inserito un codice di documento
     *		 contabile corrispondente a più documenti contabili
     * Post: Un messaggio di errore viene visualizzato all'utente per segnalare che esistono più documenti
     *		 contabili con tale codice
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @param bulk istanza di SospesoBulk che deve essere utilizzata per la ricerca
     * @return sql Query con le clausole aggiuntive
     */
    public Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (bulk instanceof SospesoBulk) {
            SospesoBulk sospeso = (SospesoBulk) bulk;

            SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, sospeso);

            sql.addSQLClause("AND", "cd_sospeso_padre", SQLBuilder.ISNULL, null);

            if (sospeso.getV_man_rev() != null && sospeso.getV_man_rev().getPg_documento_cont() != null) {
                if (sospeso.getV_man_rev().getCrudStatus() != OggettoBulk.NORMAL) {
                    CompoundFindClause clause = sospeso.getV_man_rev().buildFindClauses(null);
                    //SospesoHome sHome = (SospesoHome) getHome( userContext, sospeso.getClass());
                    V_mandato_reversaleHome vHome = (V_mandato_reversaleHome) getHome(userContext, V_mandato_reversaleBulk.class);

                    SQLBuilder sql2 = selectV_man_rev_for_searchByClause(userContext, sospeso, sospeso.getV_man_rev(), clause);
                    List result = vHome.fetchAll(sql2);
                    if (result.size() == 0) {
                        if (SospesoBulk.TIPO_ENTRATA.equals(sospeso.getTi_entrata_spesa()))
                            throw new ApplicationException(" Non esiste nessuna reversale con progressivo " + sospeso.getV_man_rev().getPg_documento_cont());
                        else if (SospesoBulk.TIPO_SPESA.equals(sospeso.getTi_entrata_spesa()))
                            throw new ApplicationException(" Non esiste nessun mandato con progressivo " + sospeso.getV_man_rev().getPg_documento_cont());
                        else
                            throw new ApplicationException(" Non esiste nessuna mandato/reversale con progressivo " + sospeso.getV_man_rev().getPg_documento_cont());
                    } else if (result.size() > 1)
                        throw new ApplicationException(" Esistono più documenti contabili con progressivo " + sospeso.getV_man_rev().getPg_documento_cont());
                    sospeso.setV_man_rev((V_mandato_reversaleBulk) result.get(0));
                }

                if (sospeso.getV_man_rev().getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_MAN)) {
                    sql.addTableToHeader("SOSPESO_DET_USC");
                    sql.addSQLJoin("SOSPESO_DET_USC.ESERCIZIO", "SOSPESO.ESERCIZIO");
                    sql.addSQLJoin("SOSPESO_DET_USC.CD_CDS", "SOSPESO.CD_CDS");
                    sql.addSQLJoin("SOSPESO_DET_USC.TI_ENTRATA_SPESA", "SOSPESO.TI_ENTRATA_SPESA");
                    sql.addSQLJoin("SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO", "SOSPESO.TI_SOSPESO_RISCONTRO");
                    sql.addSQLJoin("SOSPESO_DET_USC.CD_SOSPESO", "SOSPESO.CD_SOSPESO");
                    sql.addSQLClause("AND", "SOSPESO_DET_USC.PG_MANDATO", SQLBuilder.EQUALS, sospeso.getV_man_rev().getPg_documento_cont());
                } else if (sospeso.getV_man_rev().getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV)) {
                    sql.addTableToHeader("SOSPESO_DET_ETR");
                    sql.addSQLJoin("SOSPESO_DET_ETR.ESERCIZIO", "SOSPESO.ESERCIZIO");
                    sql.addSQLJoin("SOSPESO_DET_ETR.CD_CDS", "SOSPESO.CD_CDS");
                    sql.addSQLJoin("SOSPESO_DET_ETR.TI_ENTRATA_SPESA", "SOSPESO.TI_ENTRATA_SPESA");
                    sql.addSQLJoin("SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO", "SOSPESO.TI_SOSPESO_RISCONTRO");
                    sql.addSQLJoin("SOSPESO_DET_ETR.CD_SOSPESO", "SOSPESO.CD_SOSPESO");
                    sql.addSQLClause("AND", "SOSPESO_DET_ETR.PG_REVERSALE", SQLBuilder.EQUALS, sospeso.getV_man_rev().getPg_documento_cont());
                }
            }
            return sql;
        } else if (bulk instanceof SelezionaSospesiCNRBulk) {
            SelezionaSospesiCNRBulk seleziona = (SelezionaSospesiCNRBulk) bulk;
            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
            if (((CNRUserContext) userContext).getCd_cds().equals(ente.getCd_unita_organizzativa()))
                throw new ApplicationException("La ricerca è abilitata solo per i CDS");
            SQLBuilder sql = getHome(userContext, SospesoBulk.class).createSQLBuilder();
            sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getEsercizio());
            sql.addSQLClause("AND", "cd_cds", SQLBuilder.EQUALS, ente.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "fl_stornato", SQLBuilder.EQUALS, "N");
            sql.addSQLClause("AND", "ti_sospeso_riscontro", SQLBuilder.EQUALS, SospesoBulk.TI_SOSPESO);
            if (!seleziona.getRicercaSospesiRiaccredito()) {
                sql.addSQLClause("AND", "IM_SOSPESO - IM_ASSOCIATO", SQLBuilder.GREATER, new java.math.BigDecimal(0));
            }
            sql.addSQLClause("AND", "IM_ASS_MOD_1210", SQLBuilder.EQUALS, new java.math.BigDecimal(0));

            if (seleziona.getTi_entrata_spesa().equals(SelezionaSospesiCNRBulk.TIPO_ENTRATA))
                sql.addSQLClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, SospesoBulk.TIPO_ENTRATA);
            else if (seleziona.getTi_entrata_spesa().equals(SelezionaSospesiCNRBulk.TIPO_SPESA))
                sql.addSQLClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, SospesoBulk.TIPO_SPESA);

            sql.openParenthesis("AND");
            int nrClausoleStato = 0;

            if (seleziona.getRicercaSospesiAssegnati().booleanValue()) {
                if (nrClausoleStato == 0)
                    sql.openParenthesis("AND");
                else
                    sql.openParenthesis("OR");
                sql.addClause("AND", "stato_sospeso", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
                sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                sql.closeParenthesis();
                nrClausoleStato++;
            }
            if (seleziona.getRicercaSospesiInSospeso().booleanValue()) {
                if (nrClausoleStato == 0)
                    sql.openParenthesis("AND");
                else
                    sql.openParenthesis("OR");
                sql.addClause("AND", "stato_sospeso", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                sql.addClause("AND", "cd_cds_origine", SQLBuilder.ISNULL, null);
                sql.closeParenthesis();
            }
            if (seleziona.getRicercaSospesiInSospesoSelezionati().booleanValue()) {
                if (nrClausoleStato == 0)
                    sql.openParenthesis("AND");
                else
                    sql.openParenthesis("OR");
                sql.addClause("AND", "stato_sospeso", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                sql.closeParenthesis();
            }
            sql.closeParenthesis();

            if (seleziona.getRicercaSospesiRiaccredito()) {
                sql.addClause("AND", "stato_sospeso", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
                sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                sql.addClause("AND", "esercizio_man_riaccr", SQLBuilder.ISNOTNULL, null);
                sql.addClause("AND", "cd_cds_man_riaccr", SQLBuilder.ISNOTNULL,null);
                sql.addClause("AND", "pg_mandato_man_riaccr", SQLBuilder.ISNOTNULL,null);
            }

            return sql;

        } else if (bulk instanceof ListaSospesiBulk) {
            SQLBuilder sql = (SQLBuilder) selectForListaSospesi(userContext, clauses, bulk);
            //sql.addSQLClause( "AND", "sospeso.fl_stornato", SQLBuilder.EQUALS, "N" );
            return sql;
        }

        return null;
    }

    private Query selectForListaSospesi(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa()))
            throw new ApplicationException("Funzione non consentita per utente non abilitato a " + uoEnte.getCd_unita_organizzativa());
        verificaStatoEsercizio(userContext, ((CNRUserContext) userContext).getEsercizio(), ((CNRUserContext) userContext).getCd_cds());
//	((ListaSospesiBulk) bulk).setSospesi_cnrColl( ((SospesoHome)getHome( userContext, SospesoBulk.class)).findSospesiCNR( ((CNRUserContext)userContext).getEsercizio()));
        SQLBuilder sql = getHome(userContext, SospesoBulk.class).createSQLBuilder();
        sql.addClause(clauses);
        sql.addSQLClause("AND", "sospeso.esercizio", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getEsercizio());
        sql.addSQLClause("AND", "sospeso.cd_cds", SQLBuilder.EQUALS, uoEnte.getCd_unita_padre());
//	sql.addSQLClause( "AND", "sospeso.fl_stornato", sql.EQUALS, "N" );
//	sql.addSQLClause( "AND", "sospeso.ti_entrata_spesa", sql.EQUALS, SospesoBulk.TIPO_ENTRATA );
        sql.addSQLClause("AND", "sospeso.ti_sospeso_riscontro", SQLBuilder.EQUALS, SospesoBulk.TI_SOSPESO);
//	sql.addSQLClause( "AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_INIZIALE );
        sql.addTableToHeader("V_SOSPESO_IM_FIGLI");
        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.esercizio", "SOSPESO.ESERCIZIO");
        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.cd_cds", "SOSPESO.cd_cds");
        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.ti_sospeso_riscontro", "SOSPESO.ti_sospeso_riscontro");
        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.ti_entrata_spesa", "SOSPESO.ti_entrata_spesa");
        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.cd_sospeso", "SOSPESO.cd_sospeso");
        return sql;
    }


    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca dei Centri di Spesa
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca di un Cds da associare a Sospesi d'Entrata CNR
     * Pre:  E' stata generata la richiesta di ricerca di un Cds da associare a Sospesi d'Entrata CNR
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole specificate dall'utente.
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk istanza di ListaSospesiBulk
     * @param cds istanza di CdsBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con le clausole aggiuntive
     */
    public SQLBuilder selectCdsByClause(UserContext userContext, ListaSospesiBulk bulk, CdsBulk cds, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, cds.getClass()).createSQLBuilder();
        sql.addClause(clauses);
        return sql;
    }

    public SQLBuilder selectMandatoRiaccreditoByClause(UserContext userContext, SospesoBulk bulk, MandatoBulk mandatoBulk, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        MandatoIHome mandatoHome = Optional.ofNullable(getHome(userContext, MandatoIBulk.class))
                .filter(MandatoIHome.class::isInstance)
                .map(MandatoIHome.class::cast)
                .orElseThrow(() -> new ComponentException("Home del mandato non trovata!"));
        SQLBuilder sql = mandatoHome.createSQLBuilder();
        sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql.addClause(FindClause.AND, "cds", SQLBuilder.EQUALS, bulk.getCds_origine());
        sql.addClause(FindClause.AND, "dt_trasmissione", SQLBuilder.LESS_EQUALS, bulk.getDt_registrazione());
        sql.addClause(FindClause.AND, "esitoOperazione", SQLBuilder.EQUALS, EsitoOperazione.PAGATO.value());
        sql.addClause(clauses);
        return sql;
    }
    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Cds
     * <p>
     * Nome: Richiesta di ricerca di un Cds
     * Pre: E' stata generata la richiesta di ricerca di un Cds. Il Cds di scrivania è di tipo ENTE
     * Post: Viene restituito l'SQLBuilder
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param cds          l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     * @param                clauses L'albero logico delle clausole da applicare alla ricerca
     **/
    public SQLBuilder selectCdsForPrintByClause(UserContext userContext, Stampa_sospesi_cnr_assoc_cdsVBulk bulk, CdsBulk cds, CompoundFindClause clauses) throws ComponentException {
        SQLBuilder sql = getHome(userContext, CdsBulk.class).createSQLBuilder();
        sql.addClause(clauses);
        return sql;
    }


    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Cds
     * <p>
     * Nome: Richiesta di ricerca di un Cds
     * Pre: E' stata generata la richiesta di ricerca di un Cds. Il Cds di scrivania è di tipo ENTE
     * Post: Viene restituito l'SQLBuilder
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     * @param                clauses L'albero logico delle clausole da applicare alla ricerca
     **/
    public SQLBuilder selectCdsForPrintByClause(UserContext userContext, Stampa_sospesi_cnr_assoc_manVBulk bulk, CdsBulk cds, CompoundFindClause clauses) throws ComponentException {


        SQLBuilder sql = getHome(userContext, CdsBulk.class).createSQLBuilder();
        sql.addClause(clauses);
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
     * <p>
     * Nome: Richiesta di ricerca di una Unita Organizzativa
     * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
     * Post: Viene restituito l'SQLBuilder per filtrare le UO
     * in base al cds di scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     * @param                clauses L'albero logico delle clausole da applicare alla ricerca
     **/
    public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_sospesi_cnr_assoc_revVBulk bulk, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {


        // Recupera il Cd_Cds di scrivania
        String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);

        CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
        CdsBulk cds_scrivania;

        try {
            // Costruisce il CdsBulk di scrivania
            cds_scrivania = (CdsBulk) cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw handleException(pe);
        }

        Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
        SQLBuilder sql;

        // Verifica se il Cds di scrivania è di tipo ENTE
        if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
            sql = home.createSQLBuilderEsteso();
        } else {
            sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, bulk.getCd_cds());
        }

        sql.addClause(clauses);
        return sql;
    }

    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca dei documenti contabili (reversale o mandato)
     * che si possono associare al riscontro
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca di un Documento Contabile
     * Pre:  E' stata generata la richiesta di ricerca di un documento contabile (reversale o mandato)
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che il doc. contabile, sia esso una reversale o un mandato, abbia l'esercizio, il cds e l'unità
     *		 organizzativa rispettivamente come l'esercizio, il cds e l'unità organizzativa di scrivania,
     *		 che NON sia di tipo a regolamento sospeso o di regolarizzazione, che abbia lo stato a EMESSO e che
     *		 rispetti la condizione che la differenza tra il suo importo e l'importo incassato o pagato sia maggiore di 0.
     *
     * Nome: Richiesta di ricerca di una Reversale
     * Pre:  E' stata generata la richiesta di ricerca di una reversale
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che la reversale abbia l'esercizio, il cds e l'unità organizzativa rispettivamente come
     *		 l'esercizio, il cds e l'unità organizzativa di scrivania, che NON sia di tipo a regolamento sospeso
     *		 o di regolarizzazione, che abbia lo stato a EMESSO e che rispetti la condizione che la differenza tra
     *		 il suo importo e l'importo incassato sia maggiore di 0.
     *
     * Nome: Richiesta di ricerca di un Mandato
     * Pre:  E' stata generata la richiesta di ricerca di un mandato
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che il mandato abbia l'esercizio, il cds e l'unità organizzativa rispettivamente come
     *		 l'esercizio, il cds e l'unità organizzativa di scrivania, che NON sia di tipo a regolamento sospeso
     *		 o di regolarizzazione, che abbia lo stato a EMESSO e che rispetti la condizione che la differenza tra
     *		 il suo importo e l'importo pagato sia maggiore di 0.
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk istanza di SospesoBulk
     * @param v_man_rev istanza di V_mandato_reversaleBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con le clausole aggiuntive
     */
    public SQLBuilder selectV_man_rev_for_searchByClause(UserContext userContext, SospesoBulk bulk, V_mandato_reversaleBulk v_man_rev, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class).createSQLBuilder();
        /*
         * Se il sospeso è di tipo entrata, eseguo il caricamento delle reversali
         * Se il sospeso è di tipo spesa, eseguo il caricamento dei mandati
         * Se non definisco il tipo di sospeso, eseguo indistintamente il caricamento
         *	di reversali e di mandati
         */
        if (bulk.getTi_entrata_spesa() != null && (bulk.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA))) // sospeso di tipo entrata: ricerca reversali
            sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
        else if (bulk.getTi_entrata_spesa() != null && (bulk.getTi_entrata_spesa().equals(SospesoBulk.TIPO_SPESA))) // sospeso di tipo spesa: ricerca mandati
            sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_unita_organizzativa());
        /* SIMONA 24/9/2002 --- anche ai mandati/reversali a regolamento sospeso e' necessario associare i riscontri */
        /*	sql.addClause( "AND", "ti_documento_cont", sql.NOT_EQUALS, MandatoBulk.TIPO_REGOLAM_SOSPESO ); */

        sql.addClause("AND", "ti_documento_cont", SQLBuilder.NOT_EQUALS, MandatoBulk.TIPO_REGOLARIZZAZIONE);
        sql.addSQLClause("AND", "im_pagato_incassato > 0");
//	sql.addClause( "AND", "stato", sql.EQUALS, MandatoBulk.STATO_MANDATO_EMESSO );
//	sql.addSQLClause( "AND", "im_documento_cont - im_pagato_incassato > 0");
        /* SIMONA 10/2/2003 --- aggiunta clausola sulla dt_trasmissione per errore 469*/
//	sql.addSQLClause( "AND", "dt_trasmissione" , sql.ISNOTNULL, null );
        sql.addClause(clauses);
        return sql;

    }

    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca dei documenti contabili (reversale o mandato)
     * che si possono associare al riscontro
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca di un Documento Contabile
     * Pre:  E' stata generata la richiesta di ricerca di un documento contabile (reversale o mandato)
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che il doc. contabile, sia esso una reversale o un mandato, abbia l'esercizio, il cds e l'unità
     *		 organizzativa rispettivamente come l'esercizio, il cds e l'unità organizzativa di scrivania,
     *		 che NON sia di tipo a regolamento sospeso o di regolarizzazione, che abbia lo stato a EMESSO e che
     *		 rispetti la condizione che la differenza tra il suo importo e l'importo incassato o pagato sia maggiore di 0.
     *
     * Nome: Richiesta di ricerca di una Reversale
     * Pre:  E' stata generata la richiesta di ricerca di una reversale
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che la reversale abbia l'esercizio, il cds e l'unità organizzativa rispettivamente come
     *		 l'esercizio, il cds e l'unità organizzativa di scrivania, che NON sia di tipo a regolamento sospeso
     *		 o di regolarizzazione, che abbia lo stato a EMESSO e che rispetti la condizione che la differenza tra
     *		 il suo importo e l'importo incassato sia maggiore di 0.
     *
     * Nome: Richiesta di ricerca di un Mandato
     * Pre:  E' stata generata la richiesta di ricerca di un mandato
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     *       clausole che il mandato abbia l'esercizio, il cds e l'unità organizzativa rispettivamente come
     *		 l'esercizio, il cds e l'unità organizzativa di scrivania, che NON sia di tipo a regolamento sospeso
     *		 o di regolarizzazione, che abbia lo stato a EMESSO e che rispetti la condizione che la differenza tra
     *		 il suo importo e l'importo pagato sia maggiore di 0.
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk istanza di SospesoBulk
     * @param v_man_rev istanza di V_mandato_reversaleBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con le clausole aggiuntive
     */
    public SQLBuilder selectV_man_revByClause(UserContext userContext, SospesoBulk bulk, V_mandato_reversaleBulk v_man_rev, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class).createSQLBuilder();
        /*
         * Se il sospeso è di tipo entrata, eseguo il caricamento delle reversali
         * Se il sospeso è di tipo spesa, eseguo il caricamento dei mandati
         * Se non definisco il tipo di sospeso, eseguo indistintamente il caricamento
         *	di reversali e di mandati
         */
        if (bulk.getTi_entrata_spesa() != null && (bulk.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA))) // sospeso di tipo entrata: ricerca reversali
            sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
        else if (bulk.getTi_entrata_spesa() != null && (bulk.getTi_entrata_spesa().equals(SospesoBulk.TIPO_SPESA))) // sospeso di tipo spesa: ricerca mandati
            sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_unita_organizzativa());
        /* SIMONA 24/9/2002 --- anche ai mandati/reversali a regolamento sospeso e' necessario associare i riscontri */
        /*	sql.addClause( "AND", "ti_documento_cont", sql.NOT_EQUALS, MandatoBulk.TIPO_REGOLAM_SOSPESO ); */

        sql.addClause("AND", "ti_documento_cont", SQLBuilder.NOT_EQUALS, MandatoBulk.TIPO_REGOLARIZZAZIONE);
//	sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, bulk.getCd_uo_origine() );
        sql.addClause("AND", "stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_EMESSO);
        sql.addSQLClause("AND", "im_documento_cont - im_pagato_incassato > 0");
        /* SIMONA 10/2/2003 --- aggiunta clausola sulla dt_trasmissione per errore 469*/
        sql.addSQLClause("AND", "dt_trasmissione", SQLBuilder.ISNOTNULL, null);
        sql.addClause(clauses);
        return sql;

    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {


        if (bulk instanceof Stampa_riscontriVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_riscontriVBulk) bulk);
        else if (bulk instanceof Stampa_sospesiVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_sospesiVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_cdsVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_sospesi_cnr_assoc_cdsVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_revVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_sospesi_cnr_assoc_revVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_cnr_assoc_manVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_sospesi_cnr_assoc_manVBulk) bulk);
        else if (bulk instanceof Stampa_sospesi_da_assegnareVBulk)
            validateBulkForPrintSospesi_Riscontri(userContext, (Stampa_sospesi_da_assegnareVBulk) bulk);


        return bulk;
    }

    /**
     * Tutti controlli superati
     * PreCondition:
     * Il sospeso non è associato a nessun documento contabile
     * PostCondition:
     * Il sospeso è valido. E' consentito eseguire l'attività di eliminazione.
     * Documento contabile associato è una Reversale
     * PreCondition:
     * Il sospeso è stata associato a una reversale e si vuole procedere alla sua cancellazione
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che il sospeso è associato ad una reversale.
     * L'attività di eliminazione del sospeso non è consentita.
     * Documento contabile associato è un Mandato
     * PreCondition:
     * Il sospeso è stato associato a un mandato e si vuole procedere alla sua cancellazione
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che il sospeso è associato ad un mandato.
     * L'attività di eliminazione del sospeso non è consentita.
     * Documento contabile associato è una Lettera 1210
     * PreCondition:
     * Il sospeso è stato associato a una Lettera 1210 e si vuole procedere alla sua cancellazione
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che il sospeso è associato ad una Lettera 1210,
     * poichè l'importo associato del sospeso è maggiore di 0. L'attività di eliminazione del sospeso non è
     * consentita.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il sospeso o riscontro da validare, prima di procedere alla cancellazione
     */
    protected void validaEliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        SospesoBulk sospeso = (SospesoBulk) bulk;
        try {
            if (SospesoBulk.TI_SOSPESO.equals(sospeso.getTi_sospeso_riscontro())) {
			/*
			if ( sospeso.TIPO_ENTRATA.equals( sospeso.getTi_entrata_spesa()) )
			{
				Collection result = ((SospesoHome)getHome( userContext, SospesoBulk.class )).findSospeso_det_etrColl( sospeso );
				if ( result.size() > 0 )
					throw new ApplicationException(" Annullamento impossibile! Il sospeso e' associato alla Reversale " + ((Sospeso_det_etrBulk) result.iterator().next()).getPg_reversale());
			}
			else if ( sospeso.TIPO_SPESA.equals( sospeso.getTi_entrata_spesa()) )
			{
				Collection result = ((SospesoHome)getHome( userContext, SospesoBulk.class )).findSospeso_det_uscColl( sospeso );
				if ( result.size() > 0 )
					throw new ApplicationException(" Annullamento impossibile! Il sospeso e' associato al Mandato " + ((Sospeso_det_uscBulk) result.iterator().next()).getPg_mandato());
			}

			if( sospeso.getIm_ass_mod_1210().compareTo(new BigDecimal(0)) != 0 )
					throw new ApplicationException(" Annullamento impossibile! Il sospeso e' associato ad una Lettera di Pagamento Estero.");
*/
                /*se il sospeso o uno dei suoi figli è stato associato a mandati/reversali/lettere 1210 */
                if (sospeso.isAssociato())
                    throw new ApplicationException(" Annullamento impossibile! Il sospeso e' associato ad un documento contabile o ad una Lettera di Pagamento Estero.");

            }
            if (SospesoBulk.TI_RISCONTRO.equals(sospeso.getTi_sospeso_riscontro()))
                if (SospesoBulk.TIPO_SPESA.equals(sospeso.getTi_entrata_spesa()))
                    verificaAnnullabilitaRiscontroSpesa(userContext, sospeso);
                else
                    verificaAnnullabilitaRiscontroEntrata(userContext, sospeso);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrintSospesi_Riscontri(it.cnr.jada.UserContext userContext, Stampa_sospesi_da_assegnareVBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);


            /**** Controlli sulle Date DA A	*****/
            if (stampa.getDataInizio() == null)
                throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");


            java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());

            // La Data di Inizio Periodo è superiore alla data di Fine Periodo
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");

            // La Data di Inizio Periodo è ANTECEDENTE al 1 Gennaio dell'Esercizio di scrivania
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
            }

            try {
                if (DateServices.isAnnoMaggEsScriv(userContext)) {
                    Timestamp lastDayOfYear = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());

                    // L'esercizio di scrivania è minore dell'esercizio attuale:
                    //	la Data di Fine periodo deve essere inferiore o uguale al 31/12 dell'esercizio
                    //	di scrivania.
                    if (stampa.getDataFine().compareTo(lastDayOfYear) > 0) {
                        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
                    }
                } else {
                    // La Data di Fine periodo è SUPERIORE alla data odierna
                    if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(dataOdierna));
                    }
                }

            } catch (javax.ejb.EJBException e) {
                throw handleException(e);
            }


        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrintSospesi_Riscontri(it.cnr.jada.UserContext userContext, Stampa_sospesi_riscontriVBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);


            /**** Controlli sulle Date DA A	*****/
            if (stampa.getDataInizio() == null)
                throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");


            java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());

            // La Data di Inizio Periodo è superiore alla data di Fine Periodo
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");

            // La Data di Inizio Periodo è ANTECEDENTE al 1 Gennaio dell'Esercizio di scrivania
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
            }
            // La Data di Fine periodo è SUPERIORE alla data odierna
            if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(dataOdierna));
            }


        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * name: riscontro di entrata annullabile 1
     * pre: il riscontro di entrata è associato ad una reversale che paga accertamenti (non su pgiro) non riportati all'esercizio successivo
     * post: il riscontro supera la validazione e può essere annullato
     * <p>
     * name: riscontro di entrata annullabile 2
     * pre: il riscontro di entrata è associato ad una reversale che paga accertamenti su pgiro non riportati all'esercizio successivo
     * anche la controparte in parte spese delle partite di giro non è stata riportata all'esercizio successsivo (metodo 'verificaAnnulabilitaRiscontroEntrataPGiro')
     * post: il riscontro supera la validazione e può essere annullato
     * <p>
     * name: riscontro di entrata - errore 1
     * pre: il riscontro di entrata è associato ad una reversale che paga accertamenti (su pgiro o non) riportati all'esercizio successivo
     * post: una segnalazione di errore viene sollevata per indicare all'utente l'impossibilità di procedere con l'annullamento
     * <p>
     * name: riscontro di entrata - errore 2
     * pre: il riscontro di entrata è associato ad una reversale che paga accertamenti su pgiro non riportati all'esercizio successivo
     * però la controparte in parte spese delle partite di giro è stata riportata all'esercizio successsivo  (metodo 'verificaAnnulabilitaRiscontroEntrataPGiro')
     * post: una segnalazione di errore viene sollevata per indicare all'utente l'impossibilità di procedere con l'annullamento
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riscontro   <code>SospesoBulk</code> di tipo riscontro da validare, prima di procedere alla cancellazione
     */


    protected void verificaAnnullabilitaRiscontroEntrata(UserContext userContext, SospesoBulk riscontro) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select a.esercizio, A.cd_cds, a.esercizio_ori_accertamento, a.pg_accertamento, b.fl_pgiro, b.riportato " +
                            "from reversale_riga a, accertamento b " +
                            "where " +
//						"b.riportato = ? and " +
                            "a.cd_cds = b.cd_cds and " +
                            "a.esercizio_accertamento = b.esercizio and " +
                            "a.esercizio_ori_accertamento = b.esercizio_originale and " +
                            "a.pg_accertamento = b.pg_accertamento and " +
                            "a.esercizio = ? and " +
                            "a.cd_cds = ? and " +
                            "a.pg_reversale = ? ", true, this.getClass());
            try {
//			ps.setString(1, "Y");
                ps.setObject(1, riscontro.getV_man_rev().getEsercizio());
                ps.setString(2, riscontro.getV_man_rev().getCd_cds());
                ps.setObject(3, riscontro.getV_man_rev().getPg_documento_cont());
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        int esercizio = rs.getInt(1);
                        String cd_cds = rs.getString(2);
                        int esercizio_ori_accertamento = rs.getInt(3);
                        long pg_accertamento = rs.getLong(4);
                        String fl_pgiro = rs.getString(5);
                        String riportato = rs.getString(6);
                        if ("Y".equals(riportato))
                            throw new ApplicationException("Annullamento impossibile perchè l'accertamento " + esercizio_ori_accertamento + "/" + pg_accertamento +
                                    " del Cds " + cd_cds + " con esercizio " + esercizio +
                                    " è già stato riportato all'esercizio successivo");
                        else if ("Y".equals(fl_pgiro))
                            verificaAnnullabilitaRiscontroEntrataPGiro(userContext, esercizio, cd_cds, esercizio_ori_accertamento, pg_accertamento);

                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riscontro   <code>SospesoBulk</code> di tipo riscontro da validare, prima di procedere alla cancellazione
     */
    protected void verificaAnnullabilitaRiscontroEntrataPGiro(UserContext userContext, int esercizio, String cd_cds, int esercizio_ori_accertamento, long pg_accertamento) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select d.esercizio, d.cd_cds, d.esercizio_originale, d.pg_obbligazione, d.riportato " +
                            "from  accertamento b, ass_obb_acr_pgiro c, obbligazione d " +
                            "where " +
//						"b.riportato = ? and " +
                            "b.cd_cds = ? and " +
                            "b.esercizio = ? and " +
                            "b.esercizio_originale = ? and " +
                            "b.pg_accertamento = ? and " +
                            "b.cd_cds = c.CD_CDS and " +
                            "b.esercizio = c.esercizio and " +
                            "b.esercizio_originale = c.esercizio_ori_accertamento and " +
                            "b.pg_accertamento = c.PG_accertamento and " +
                            "d.cd_cds = c.CD_CDS and " +
                            "d.esercizio = c.esercizio and " +
                            "d.esercizio_originale = c.esercizio_ori_obbligazione and " +
                            "d.pg_obbligazione = c.PG_obbligazione", true, this.getClass());
            try {
//			ps.setString(1, "Y");
                ps.setString(1, cd_cds);
                ps.setObject(2, new Integer(esercizio));
                ps.setObject(3, new Integer(esercizio_ori_accertamento));
                ps.setObject(4, new Long(pg_accertamento));
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        int es = rs.getInt(1);
                        String cds = rs.getString(2);
                        int esOri = rs.getInt(3);
                        long pg_obbligazione = rs.getLong(4);
                        String riportato = rs.getString(5);
                        if ("Y".equals(riportato))
                            throw new ApplicationException("Annullamento impossibile perchè l'impegno " + esOri + "/" + pg_obbligazione +
                                    " del Cds " + cds + " con esercizio " + es +
                                    " è già stata riportata all'esercizio successivo");

                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * name: riscontro di spesa annullabile 1
     * pre: il riscontro di spesa è associato ad un mandato che paga obbligazioni (non su pgiro) non riportate all'esercizio successivo
     * post: il riscontro supera la validazione e può essere annullato
     * <p>
     * name: riscontro di spesa annullabile 2
     * pre: il riscontro di spesa è associato ad un mandato che paga obbligazioni su pgiro non riportate all'esercizio successivo
     * anche la controparte in parte entrate delle partite di giro non è stata riportata all'esercizio successsivo (metodo 'verificaAnnulabilitaRiscontroSpesaPGiro')
     * post: il riscontro supera la validazione e può essere annullato
     * <p>
     * name: riscontro di spesa - errore 1
     * pre: il riscontro di spesa è associato ad un mandato che paga obbligazioni (su pgiro o non) riportate all'esercizio successivo
     * post: una segnalazione di errore viene sollevata per indicare all'utente l'impossibilità di procedere con l'annullamento
     * <p>
     * name: riscontro di spesa - errore 2
     * pre: il riscontro di spesa è associato ad un mandato che paga obbligazioni su pgiro non riportate all'esercizio successivo
     * però la controparte in parte entrate delle partite di giro è stata riportata all'esercizio successsivo  (metodo 'verificaAnnulabilitaRiscontroSpesaPGiro')
     * post: una segnalazione di errore viene sollevata per indicare all'utente l'impossibilità di procedere con l'annullamento
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riscontro   <code>SospesoBulk</code> di tipo riscontro da validare, prima di procedere alla cancellazione
     */
    protected void verificaAnnullabilitaRiscontroSpesa(UserContext userContext, SospesoBulk riscontro) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select a.esercizio, A.cd_cds, a.esercizio_ori_obbligazione, a.pg_obbligazione, b.fl_pgiro, b.riportato " +
                            "from mandato_riga a, obbligazione b " +
                            "where " +
//						"b.riportato = ? and " +
                            "a.cd_cds = b.cd_cds and " +
                            "a.esercizio_obbligazione = b.esercizio and " +
                            "a.esercizio_ori_obbligazione = b.esercizio_originale and " +
                            "a.pg_obbligazione = b.pg_obbligazione and " +
                            "a.esercizio = ? and " +
                            "a.cd_cds = ? and " +
                            "a.pg_mandato = ? ", true, this.getClass());
            try {
//			ps.setString(1, "Y");
                ps.setObject(1, riscontro.getV_man_rev().getEsercizio());
                ps.setString(2, riscontro.getV_man_rev().getCd_cds());
                ps.setObject(3, riscontro.getV_man_rev().getPg_documento_cont());
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        int esercizio = rs.getInt(1);
                        String cd_cds = rs.getString(2);
                        int esercizio_originale = rs.getInt(3);
                        long pg_obbligazione = rs.getLong(4);
                        String fl_pgiro = rs.getString(5);
                        String riportato = rs.getString(6);
                        if ("Y".equals(riportato))
                            throw new ApplicationException("Annullamento impossibile perchè l'impegno " + esercizio_originale + "/" + pg_obbligazione +
                                    " del Cds " + cd_cds + " con esercizio " + esercizio +
                                    " è già stata riportata all'esercizio successivo");
                        else if ("Y".equals(fl_pgiro))
                            verificaAnnullabilitaRiscontroSpesaPGiro(userContext, esercizio, cd_cds, esercizio_originale, pg_obbligazione);
                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riscontro   <code>SospesoBulk</code> di tipo riscontro da validare, prima di procedere alla cancellazione
     */
    protected void verificaAnnullabilitaRiscontroSpesaPGiro(UserContext userContext, int esercizio, String cd_cds, int esercizio_ori_obbligazione, long pg_obbligazione) throws ComponentException {
        try {
            String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "select d.esercizio, d.cd_cds, d.esercizio_originale, d.pg_accertamento, d.riportato " +
                            "from obbligazione b, ass_obb_acr_pgiro c, accertamento d " +
                            "where " +
//						"b.riportato = ? and " +
                            "b.cd_cds = ? and " +
                            "b.esercizio = ? and " +
                            "b.esercizio_originale = ? and " +
                            "b.pg_obbligazione = ? and " +
                            "b.cd_cds = c.CD_CDS and " +
                            "b.esercizio = c.esercizio and " +
                            "b.esercizio_originale = c.esercizio_ori_obbligazione and " +
                            "b.pg_obbligazione = c.PG_OBBLIGAZIONE and " +
                            "d.cd_cds = c.CD_CDS and " +
                            "d.esercizio = c.esercizio and " +
                            "d.esercizio_originale = c.esercizio_ori_accertamento and " +
                            "d.pg_accertamento = c.PG_accertamento", true, this.getClass());
            try {
//			ps.setString(1, "Y");
                ps.setString(1, cd_cds);
                ps.setObject(2, new Integer(esercizio));
                ps.setObject(3, new Integer(esercizio_ori_obbligazione));
                ps.setObject(4, new Long(pg_obbligazione));
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        int es = rs.getInt(1);
                        String cds = rs.getString(2);
                        int esOri = rs.getInt(3);
                        long pg_accertamento = rs.getLong(3);
                        String riportato = rs.getString(4);
                        if ("Y".equals(riportato))
                            throw new ApplicationException("Annullamento impossibile perchè l'accertamento " + esOri + "/" + pg_accertamento +
                                    " del Cds " + cd_cds + " con esercizio " + esercizio +
                                    " è già stato riportato all'esercizio successivo");

                    }
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
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    private void verificaRiscontroEntrataCNR(UserContext userContext, SospesoBulk sospeso) throws ComponentException {
        try {
            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
            if (!sospeso.getCd_cds().equals(ente.getCd_unita_organizzativa()))
                return;

            SQLBuilder sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
            sql.addTableToHeader("REVERSALE_RIGA");
            sql.addSQLJoin("BANCA.CD_TERZO", "REVERSALE_RIGA.CD_TERZO_UO");
            sql.addSQLJoin("BANCA.pg_banca", "REVERSALE_RIGA.PG_BANCA");
            sql.addSQLClause("AND", "REVERSALE_RIGA.cd_cds", SQLBuilder.EQUALS, sospeso.getV_man_rev().getCd_cds());
            sql.addSQLClause("AND", "REVERSALE_RIGA.esercizio", SQLBuilder.EQUALS, sospeso.getV_man_rev().getEsercizio());
            sql.addSQLClause("AND", "REVERSALE_RIGA.pg_reversale", SQLBuilder.EQUALS, sospeso.getV_man_rev().getPg_documento_cont());
            List result = getHome(userContext, BancaBulk.class).fetchAll(sql);

            BancaBulk banca = (BancaBulk) result.get(0);
            if (banca.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA) &&
                    sospeso.getTi_cc_bi().equals(SospesoBulk.TIPO_CC))
                throw new ApplicationException("Attenzione! Il riscontro non proviene da Banca d'Italia mentre le modalità di pagamento della reversale sono di Banca d'Italia");
            else if (!banca.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA) &&
                    sospeso.getTi_cc_bi().equals(SospesoBulk.TIPO_BANCA_ITALIA))
                throw new ApplicationException("Attenzione! Il riscontro proviene da Banca d'Italia mentre le modalità di pagamento della reversale sono diverse da Banca d'Italia");


        } catch (Exception e) {
            throw handleException(e);
        }


    }

    private void verificaSospesiFigliPerEsercizio(UserContext aUC, SospesoBulk sospeso) throws ComponentException {

        SospesoBulk figlio;
        for (Iterator i = sospeso.getSospesiFigliColl().iterator(); i.hasNext(); ) {
            figlio = (SospesoBulk) i.next();
            if (SospesoBulk.STATO_SOSP_ASS_A_CDS.equals(figlio.getStato_sospeso()) &&
                    figlio.getCd_cds_origine() != null) {

                if (isEsercizioChiusoFor(aUC, figlio.getCd_cds_origine())) {

                    throw new ApplicationException("Attenzione: l'esercizio risulta chiuso per il CdS " + figlio.getCd_cds_origine());
                }

            }

        }

    }

    /**
     * Tutti controlli superati
     * PreCondition:
     * L'importo del riscontro è uguale a quello del documento contabile associato
     * Il tipo C/C-Banca d'Italia del sospeso è uguale a quello del documento contabile associato
     * PostCondition:
     * Il sospeso o riscontro sono validi. E' consentito eseguire l'attività di salvataggio.
     * Importi dei riscontri associati allo stesso doc. contabile (Entrata)
     * PreCondition:
     * Alla stessa reversale sono stati associati uno o più riscontri la cui somma degli importi
     * supera l'importo della reversale stessa
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che la somma degli importi dei singoli riscontri
     * associati alla reversale selezionata è maggiore dell'importo della reversale stessa. L'attività non è consentita.
     * Importi dei riscontri associati allo stesso doc. contabile (Spesa)
     * PreCondition:
     * Allo stesso mandato sono stati associati uno o più riscontri la cui somma degli importi
     * supera l'importo del mandato stesso
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che la somma degli importi dei singoli riscontri
     * associati al mandato selezionato è maggiore dell'importo del mandato stesso. L'attività non è consentita.
     * Tipo C/C-Banca d'Italia di un riscontro d'entrata
     * PreCondition:
     * Il tipo C/C-Banca d'Italia del riscontro è diverso da quello della reversale associata
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che non è possibile creare un riscontro per la
     * Reversale associata. L'attività non è consentita. Viene inoltre chiamato il metodo verificaRiscontroEntrataCNR()
     * per fare un'ulteriore verifica sulle modalità di pagamento delle righe della reversale rispetto a quelle del
     * riscontro.
     * Tipo C/C-Banca d'Italia di un riscontro di spesa
     * PreCondition:
     * Il tipo C/C-Banca d'Italia del riscontro è diverso da quello del mandato associato
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che non è possibile creare un riscontro per il
     * Mandato associato. L'attività non è consentita.
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param sospeso <code>SospesoBulk</code> il sospeso o riscontro da validare
     */
    private void verificaSospesoRiscontro(UserContext aUC, SospesoBulk sospeso) throws ComponentException {
        try {
            EnteBulk ente = (EnteBulk) getHome(aUC, EnteBulk.class).findAll().get(0);

            if (sospeso.isToBeCreated()) {
                Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear(sospeso.getEsercizio().intValue());
                Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

                if (today.after(lastDayOfTheYear) &&
                        sospeso.getDt_registrazione().compareTo(lastDayOfTheYear) != 0)
                    throw new ApplicationException("La data di registrazione deve essere " +
                            java.text.DateFormat.getDateInstance().format(lastDayOfTheYear));
                // Rospuc 08/05/2014
                if (SospesoBulk.TI_RISCONTRO.equals(sospeso.getTi_sospeso_riscontro())) {
                    // Prefisso di numerazione automatica dei riscontri effettuati via interfaccia cassiere
                    if (sospeso.getCd_sospeso() != null && sospeso.getCd_sospeso().toUpperCase().startsWith("XSRC"))
                        throw new ApplicationException("Il campo codice può iniziare con XSRC solo per gli inserimenti automatici.");
                }
            }

            java.math.BigDecimal totDettagli;
            if (SospesoBulk.TI_RISCONTRO.equals(sospeso.getTi_sospeso_riscontro())) {
                V_mandato_reversaleHome v_man_revHome = (V_mandato_reversaleHome) getHome(aUC, V_mandato_reversaleBulk.class);
                List result;

                if ((sospeso.getTi_entrata_spesa() != null) && (sospeso.getTi_entrata_spesa().equals(SospesoBulk.TIPO_ENTRATA))) {
                    if (sospeso.getDettaglio_etr() == null ||
                            (sospeso.getDettaglio_etr() != null &&
                                    sospeso.getDettaglio_etr().getPg_reversale().compareTo(sospeso.getV_man_rev().getPg_documento_cont()) != 0)) {
                        if (sospeso.getV_man_rev().getIm_documento_cont().subtract(sospeso.getV_man_rev().getIm_pagato_incassato()).compareTo(sospeso.getIm_sospeso()) < 0)
                            throw handleException(new ApplicationException("Attenzione! L'importo residuo da incassare della reversale è inferiore all'importo del riscontro."));

                        Sospeso_det_etrHome sospeso_det_etrHome = (Sospeso_det_etrHome) getHome(aUC, Sospeso_det_etrBulk.class);
                        totDettagli = sospeso_det_etrHome.calcolaTotDettagli(sospeso.getV_man_rev());
                        totDettagli = totDettagli.add(sospeso.getIm_sospeso());
                        if (totDettagli.compareTo(sospeso.getV_man_rev().getIm_documento_cont()) > 0)
                            throw handleException(new ApplicationException("Attenzione! La somma degli importi dei riscontri associati alla reversale selezionata è maggiore dell'importo della reversale."));

                        result = (List) v_man_revHome.findRiscontriDiEntrata(sospeso.getV_man_rev());
                        if (result.size() != 0) {
                            if (!((SospesoBulk) result.get(0)).getTi_cc_bi().equals(sospeso.getTi_cc_bi()))
                                throw handleException(new ApplicationException("Attenzione! Non è possibile creare un riscontro su " + sospeso.getTi_cc_biKeys().get(sospeso.getTi_cc_bi()) + " per la Reversale " + sospeso.getV_man_rev().getPg_documento_cont()));
                        }
                    }
                    //poichè in V_MANDATO_REVERSALE il TI_CC_BI = 'C' sempre (non si sa il motivo!!!!!!!)
                    //per le reversali di liquidazione tramite F24EP non deve essere effettuato il controllo successivo
                    if (!(sospeso.getV_man_rev().getCd_cds().equals(ente.getCd_unita_organizzativa()) &&
                            sospeso.getV_man_rev().getCd_cds().equals(sospeso.getV_man_rev().getCd_cds_origine()))) {
                        if (sospeso.getV_man_rev().getTi_cc_bi() != null &&
                                !sospeso.getV_man_rev().getTi_cc_bi().equals(sospeso.getTi_cc_bi()))
                            throw new ApplicationException("Una reversale con sospesi di tipo " + sospeso.getTi_cc_biKeys().get(sospeso.getV_man_rev().getTi_cc_bi()) +
                                    " non può essere associata ad un riscontro di tipo " + sospeso.getTi_cc_biKeys().get(sospeso.getTi_cc_bi()));
                    }
                    verificaRiscontroEntrataCNR(aUC, sospeso);
                } else if ((sospeso.getTi_entrata_spesa() != null) && SospesoBulk.TIPO_SPESA.equals(sospeso.getTi_entrata_spesa())) {
                    if (sospeso.getDettaglio_usc() == null ||
                            (sospeso.getDettaglio_usc() != null &&
                                    sospeso.getDettaglio_usc().getPg_mandato().compareTo(sospeso.getV_man_rev().getPg_documento_cont()) != 0)) {
                        if (sospeso.getV_man_rev().getIm_documento_cont().subtract(sospeso.getV_man_rev().getIm_pagato_incassato()).compareTo(sospeso.getIm_sospeso()) < 0)
                            throw handleException(new ApplicationException("Attenzione! L'importo residuo da pagare del mandato è inferiore all'importo del riscontro."));

                        Sospeso_det_uscHome sospeso_det_uscHome = (Sospeso_det_uscHome) getHome(aUC, Sospeso_det_uscBulk.class);
                        totDettagli = sospeso_det_uscHome.calcolaTotDettagli(sospeso.getV_man_rev());
                        totDettagli = totDettagli.add(sospeso.getIm_sospeso());
                        if (totDettagli.compareTo(sospeso.getV_man_rev().getIm_documento_cont()) > 0)
                            throw handleException(new ApplicationException("Attenzione! La somma degli importi dei riscontri associati al mandato selezionato è maggiore dell'importo del mandato."));

                        result = (List) v_man_revHome.findRiscontriDiSpesa(sospeso.getV_man_rev());
                        if (result.size() != 0) {
                            if (!((SospesoBulk) result.get(0)).getTi_cc_bi().equals(sospeso.getTi_cc_bi()))
                                throw handleException(new ApplicationException("Attenzione! Non è possibile creare un riscontro su " + sospeso.getTi_cc_biKeys().get(sospeso.getTi_cc_bi()) + " per il Mandato " + sospeso.getV_man_rev().getPg_documento_cont()));
                        }
                    }
                    if (sospeso.getV_man_rev().getTi_cc_bi() != null &&
                            !sospeso.getV_man_rev().getTi_cc_bi().equals(sospeso.getTi_cc_bi()))
                        throw new ApplicationException("Un mandato con sospesi di tipo " + sospeso.getTi_cc_biKeys().get(sospeso.getV_man_rev().getTi_cc_bi()) +
                                " non può essere associato ad un riscontro di tipo " + sospeso.getTi_cc_biKeys().get(sospeso.getTi_cc_bi()));
                }
            }

            // Verifica l'Esercizio dei CdS per i figli
            verificaSospesiFigliPerEsercizio(aUC, sospeso);
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Verifica dello stato dell'esercizio
     *
     * @param userContext <code>UserContext</code>
     * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
     * TRUE in tutti gli altri casi
     */

    protected void verificaStatoEsercizio(UserContext userContext, Integer es, String cd_cds) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey(
                new EsercizioBulk(cd_cds, es));
        if (esercizio == null)
            throw handleException(new ApplicationException("Inserimento impossibile: esercizio inesistente!"));
        if (!EsercizioBulk.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
            throw handleException(new ApplicationException("Inserimento impossibile: esercizio non aperto!"));
    }

    public RemoteIterator cercaSospesiPerStato(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String statoForSearch) throws ComponentException {
        try {
            SQLBuilder sql = null;
            if (oggettobulk instanceof ListaSospesiBulk) {
                sql = (SQLBuilder) selectForListaSospesi(usercontext, compoundfindclause, oggettobulk);

                if ("ANN".equals(statoForSearch))
                    sql.addSQLClause(FindClause.AND, "sospeso.fl_stornato", SQLBuilder.EQUALS, "Y");
                else if (!SospesoBulk.STATO_DOCUMENTO_TUTTI.equals(statoForSearch)) {
                    sql.addSQLClause(FindClause.AND, "sospeso.fl_stornato", SQLBuilder.EQUALS, "N");

                    SQLBuilder sqlExists = getHome(usercontext, SospesoBulk.class).createSQLBuilder();
                    sqlExists.setFromClause(sqlExists.getFromClause().append(" SOSPESO_FIGLIO"));
                    sqlExists.addSQLJoin("SOSPESO_FIGLIO.esercizio", "SOSPESO.esercizio");
                    sqlExists.addSQLJoin("SOSPESO_FIGLIO.cd_cds", "SOSPESO.cd_cds");
                    sqlExists.addSQLJoin("SOSPESO_FIGLIO.ti_entrata_spesa", "SOSPESO.ti_entrata_spesa");
                    sqlExists.addSQLJoin("SOSPESO_FIGLIO.ti_sospeso_riscontro", "SOSPESO.ti_sospeso_riscontro");
                    sqlExists.addSQLJoin("SOSPESO_FIGLIO.cd_sospeso_padre", "SOSPESO.cd_sospeso");

                    SQLBuilder sqlNotExists = getHome(usercontext, SospesoBulk.class).createSQLBuilder();
                    sqlNotExists.setFromClause(sqlNotExists.getFromClause().append(" SOSPESO_FIGLIO"));
                    sqlNotExists.addSQLJoin("SOSPESO_FIGLIO.esercizio", "SOSPESO.esercizio");
                    sqlNotExists.addSQLJoin("SOSPESO_FIGLIO.cd_cds", "SOSPESO.cd_cds");
                    sqlNotExists.addSQLJoin("SOSPESO_FIGLIO.ti_entrata_spesa", "SOSPESO.ti_entrata_spesa");
                    sqlNotExists.addSQLJoin("SOSPESO_FIGLIO.ti_sospeso_riscontro", "SOSPESO.ti_sospeso_riscontro");
                    sqlNotExists.addSQLJoin("SOSPESO_FIGLIO.cd_sospeso_padre", "SOSPESO.cd_sospeso");

                    if (SospesoBulk.STATO_SOSP_INIZIALE.equals(statoForSearch)) {
                        sqlExists.addSQLClause(FindClause.AND, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_INIZIALE);
                        sqlNotExists.addSQLClause(FindClause.AND, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                        sql.addSQLExistsClause(FindClause.AND, sqlExists);
                        sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);
                    } else if (SospesoBulk.STATO_SOSP_IN_SOSPESO.equals(statoForSearch)) {
                        sqlExists.addSQLClause(FindClause.AND, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                        sql.addSQLExistsClause(FindClause.AND, sqlExists);
                    } else if (SospesoBulk.STATO_SOSP_ASS_A_CDS.equals(statoForSearch)) {
                        sqlExists.addSQLClause(FindClause.AND, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
                        sqlNotExists.openParenthesis(FindClause.AND);
                        sqlNotExists.addSQLClause(FindClause.OR, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_INIZIALE);
                        sqlNotExists.addSQLClause(FindClause.OR, "SOSPESO_FIGLIO.STATO_SOSPESO", SQLBuilder.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                        sqlNotExists.closeParenthesis();
                        sql.addSQLExistsClause(FindClause.AND, sqlExists);
                        sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);
                    } else if ("LIBERO".equals(statoForSearch)) {
                        sql.addSQLJoin("V_SOSPESO_IM_FIGLI.IM_ASSOCIATO_FIGLI", SQLBuilder.NOT_EQUALS, "SOSPESO.IM_SOSPESO");
                        sqlExists.addSQLClause(FindClause.AND, "SOSPESO_FIGLIO.IM_ASS_MOD_1210", SQLBuilder.EQUALS, BigDecimal.ZERO);
                        sql.addSQLExistsClause(FindClause.AND, sqlExists);
                    }
                }
            } else
                sql = (SQLBuilder) select(usercontext, compoundfindclause, oggettobulk);

            return iterator(usercontext, sql, SospesoBulk.class, getFetchPolicyName("find"));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    private void aggiornaSaldiCapitoli(UserContext userContext, V_mandato_reversaleBulk mandato_reversaleBulk) throws ComponentException, PersistencyException {
        V_mandato_reversale_scad_voceHome homeManRevScadVode = (V_mandato_reversale_scad_voceHome)getHome(userContext, V_mandato_reversale_scad_voceBulk.class);
        List mandatiReversaliScadVoce = homeManRevScadVode.findMandatiReversali(mandato_reversaleBulk);
        for (Object obj : mandatiReversaliScadVoce){
            V_mandato_reversale_scad_voceBulk mandatoReversaleScadVoce = (V_mandato_reversale_scad_voceBulk) obj;
            Voce_f_saldi_cdr_lineaBulk saldo = new Voce_f_saldi_cdr_lineaBulk();
            saldo.setEsercizio(mandatoReversaleScadVoce.getEsercizio());
            saldo.setEsercizio_res(mandatoReversaleScadVoce.getEsercizio_originale());
            saldo.setCd_centro_responsabilita(mandatoReversaleScadVoce.getCd_centro_responsabilita());
            saldo.setCd_linea_attivita(mandatoReversaleScadVoce.getCd_linea_attivita());
            saldo.setTi_appartenenza(mandatoReversaleScadVoce.getTi_appartenenza());
            saldo.setTi_gestione(mandatoReversaleScadVoce.getTi_gestione());
            saldo.setCd_voce(mandatoReversaleScadVoce.getCd_voce());

            Voce_f_saldi_cdr_lineaHome home = (Voce_f_saldi_cdr_lineaHome) getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class);
            saldo = (Voce_f_saldi_cdr_lineaBulk) home.findByPrimaryKey(saldo);
            if (saldo != null){
                try {
                    home.lock(saldo);
                } catch (OutdatedResourceException | BusyResourceException e) {
                    throw new ComponentException(e);
                }
                saldo.setIm_pagamenti_incassi(saldo.getIm_pagamenti_incassi().add(mandatoReversaleScadVoce.getIm_voce().setScale(2, RoundingMode.HALF_EVEN)));
                saldo.setToBeUpdated();
                super.updateBulk(userContext, saldo);
            } else {
                throw new ComponentException("Non è stata trovata la linea di saldo");
            }
        }
    }

    private Integer aggiornaRigaProcessata(UserContext userContext, MovimentoContoEvidenzaBulk riga) throws PersistencyException, ComponentException {
        riga.setStato(MovimentoContoEvidenzaBulk.STATO_RECORD_PROCESSATO);
        riga.setToBeUpdated();
        super.updateBulk(userContext, riga);
        return 1;
    }
    public Integer caricamentoRigaGiornaleCassa(UserContext userContext, boolean tesoreriaUnica, EnteBulk cdsEnte, MovimentoContoEvidenzaBulk riga) throws ComponentException, PersistencyException {
        return 0;
    }
}
