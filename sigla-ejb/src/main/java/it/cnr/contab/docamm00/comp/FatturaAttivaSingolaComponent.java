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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaHome;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.client.RicercaTrovato;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.ejb.VoceIvaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.views.bulk.V_stm_paramin_ft_attivaBulk;
import it.cnr.contab.docamm00.views.bulk.V_stm_paramin_ft_attivaHome;
import it.cnr.contab.docamm00.views.bulk.Vsx_rif_protocollo_ivaBulk;
import it.cnr.contab.docamm00.views.bulk.Vsx_rif_protocollo_ivaHome;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.contab.inventario01.bulk.*;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailHome;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FatturaAttivaSingolaComponent
        extends it.cnr.jada.comp.CRUDComponent
        implements ICRUDMgr, IFatturaAttivaSingolaMgr, Cloneable, Serializable {
    private transient final static Logger logger = LoggerFactory.getLogger(FatturaAttivaSingolaComponent.class);
    private static final DateFormat PDF_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
//^^@@

    public FatturaAttivaSingolaComponent() {

        /*Default constructor*/


    }

    /**
     * Addebita i dettagli selezionati.
     * PreCondition:
     * In nota di debito viene richiesta la contabilizzazione dei dettagli selezionati.
     * PostCondition:
     * Vegono addebitati nella nota di debito passata i "dettagliDaInventariare" sull'obbligazione/accertamento selezionato/creato.
     */
//^^@@
    public Nota_di_debito_attivaBulk addebitaDettagli(
            UserContext context,
            Nota_di_debito_attivaBulk ndD,
            java.util.List dettagliDaAddebitare,
            java.util.Hashtable relationsHash)
            throws ComponentException {

        if (ndD == null) return ndD;

        if (dettagliDaAddebitare == null || dettagliDaAddebitare.isEmpty()) {
            if (relationsHash != null) {
                Accertamento_scadenzarioBulk accertamentoSelezionato = (Accertamento_scadenzarioBulk) relationsHash.get(ndD);
                if (accertamentoSelezionato != null)
                    ndD.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, null);
            }
        } else {
            for (Iterator i = dettagliDaAddebitare.iterator(); i.hasNext(); ) {
                Nota_di_debito_attiva_rigaBulk rigaNdD = (Nota_di_debito_attiva_rigaBulk) i.next();
                Fattura_attiva_rigaIBulk rigaAssociata = null;
                if (relationsHash != null) {
                    Object obj = relationsHash.get(rigaNdD);
                    if (obj instanceof Fattura_attiva_rigaIBulk) {
                        Fattura_attiva_rigaIBulk rigaInRelazione = (Fattura_attiva_rigaIBulk) obj;
                        rigaAssociata = (rigaInRelazione == null) ?
                                rigaNdD.getRiga_fattura_associata() :
                                rigaInRelazione;
                    }
                } else
                    rigaAssociata = rigaNdD.getRiga_fattura_associata();

                if (rigaAssociata != null) {
                    ndD = basicAddebitaDettaglio(
                            context,
                            ndD,
                            rigaNdD,
                            rigaAssociata);
                }
            }
        }
        return ndD;
    }

    private void aggiornaAccertamenti(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva,
            OptionRequestParameter status) throws ComponentException {

        if (fattura_attiva != null) {
            AccertamentiTable accertamentiHash = fattura_attiva.getFattura_attiva_accertamentiHash();
            if (accertamentiHash != null && !accertamentiHash.isEmpty()) {
                Accertamento_scadenzarioHome home = (Accertamento_scadenzarioHome) getHome(userContext, Accertamento_scadenzarioBulk.class);

                //Aggiorna i saldi per gli accertamenti NON temporanei
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((AccertamentiTable) accertamentiHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaSaldi(
                            userContext,
                            fattura_attiva,
                            (IDocumentoContabileBulk) e.nextElement(),
                            status);

                it.cnr.jada.bulk.PrimaryKeyHashtable accTemporanei = getDocumentiContabiliTemporanei(userContext, ((AccertamentiTable) accertamentiHash.clone()).keys());
                for (java.util.Enumeration e = accTemporanei.keys(); e.hasMoreElements(); ) {
                    AccertamentoBulk accT = (AccertamentoBulk) e.nextElement();

                    //Aggiorna i saldi per gli accertamenti temporanei
                    //DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
                    aggiornaSaldi(
                            userContext,
                            fattura_attiva,
                            accT,
                            status);

                    aggiornaAccertamentiTemporanei(userContext, accT);
                    accTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable(accTemporanei);
                    for (Iterator i = ((Vector) accTemporanei.get(accT)).iterator(); i.hasNext(); )
                        ((AccertamentoBulk) i.next()).setPg_accertamento(accT.getPg_accertamento());
                }

                AccertamentiTable newAccertamentiHash = new AccertamentiTable(accertamentiHash);
                fattura_attiva.setFattura_attiva_accertamentiHash(newAccertamentiHash);
                for (java.util.Enumeration e = ((AccertamentiTable) newAccertamentiHash.clone()).keys(); e.hasMoreElements(); ) {
                    Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) e.nextElement();
/*
                    if (scadenza.getCrudStatus() == 5){
                        ScadenzaPagopaComponentSession pagopaComponent = (ScadenzaPagopaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPAGOPA_EJB_ScadenzaPagopaComponentSession", ScadenzaPagopaComponentSession.class);
                        try {
                            ScadenzaPagopaBulk scadenzaPagopa = pagopaComponent.generaPosizioneDebitoria(userContext, fattura_attiva, scadenza.getDt_scadenza_incasso(), scadenza.getIm_scadenza());
                            scadenzaPagopa = (ScadenzaPagopaBulk) super.creaConBulk(userContext, scadenzaPagopa);
                            for (java.util.Enumeration e1 = ((AccertamentiTable) fattura_attiva.getFattura_attiva_accertamentiHash()).keys(); e1.hasMoreElements(); ) {
                                Accertamento_scadenzarioBulk scadenzaFattura = (Accertamento_scadenzarioBulk) e1.nextElement();
                                scadenzaFattura.setScadenzaPagopa(scadenzaPagopa);
                                scadenzaFattura.setToBeUpdated();
                            }
                        } catch (RemoteException remoteException) {
                            throw  new ComponentException(remoteException);
                        }
                    }
*/
                    scadenza.setIm_associato_doc_amm(calcolaTotaleAccertamentoPer(userContext, scadenza, fattura_attiva));
                    updateImportoAssociatoDocAmm(userContext, scadenza);
                }
            }
        }

    }

    private void aggiornaAccertamentiSuCancellazione(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            java.util.Enumeration scadenzeDaCancellare,
            java.util.Collection scadenzeConfermate,
            OptionRequestParameter status)
            throws ComponentException {

        if (scadenzeDaCancellare != null) {

            it.cnr.jada.bulk.PrimaryKeyHashtable accTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
            for (java.util.Enumeration e = scadenzeDaCancellare; e.hasMoreElements(); ) {
                OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                if (oggettoBulk instanceof Accertamento_scadenzarioBulk) {
                    Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) oggettoBulk;
                    if (scadenza.getAccertamento().isTemporaneo()) {
                        if (!accTemporanei.containsKey(scadenza.getAccertamento())) {
                            Vector allInstances = new java.util.Vector();
                            allInstances.addElement(scadenza);
                            accTemporanei.put(scadenza.getAccertamento(), allInstances);
                        } else {
                            ((Vector) accTemporanei.get(scadenza.getAccertamento())).add(scadenza);
                        }
                    } else if (!fatturaAttiva.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
                        PrimaryKeyHashtable accerts = getDocumentiContabiliNonTemporanei(userContext, fatturaAttiva.getAccertamentiHash().keys());
                        if (!accerts.containsKey(scadenza.getAccertamento()))
                            aggiornaSaldi(
                                    userContext,
                                    fatturaAttiva,
                                    scadenza.getAccertamento(),
                                    status);
                        scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                        updateImportoAssociatoDocAmm(userContext, scadenza);
                    }
                }
            }
            for (java.util.Enumeration e = accTemporanei.keys(); e.hasMoreElements(); ) {
                AccertamentoBulk accT = (AccertamentoBulk) e.nextElement();

                //Aggiorna i saldi per le obbligazioni temporanee
                //DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
                PrimaryKeyHashtable accerts = getDocumentiContabiliTemporanei(userContext, fatturaAttiva.getAccertamentiHash().keys());
                if (!accerts.containsKey(accT))
                    aggiornaSaldi(userContext, fatturaAttiva, accT, status);

                if (scadenzeConfermate == null || !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(scadenzeConfermate, accT))
                    aggiornaAccertamentiTemporanei(userContext, accT);
            }
        }
    }

    private void aggiornaAccertamentiTemporanei(UserContext userContext, AccertamentoBulk accertamentoTemporaneo) throws ComponentException {

        try {
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
            Long pg = null;
            pg = numHome.getNextPg(userContext,
                    accertamentoTemporaneo.getEsercizio(),
                    accertamentoTemporaneo.getCd_cds(),
                    accertamentoTemporaneo.getCd_tipo_documento_cont(),
                    accertamentoTemporaneo.getUser());
            AccertamentoHome home = (AccertamentoHome) getHome(userContext, accertamentoTemporaneo);
            home.confirmAccertamentoTemporaneo(userContext, accertamentoTemporaneo, pg);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(accertamentoTemporaneo, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(accertamentoTemporaneo, e);
        }
    }

    private void aggiornaBuoniScaricoTemporanei(
            UserContext userContext,
            Fattura_attivaBulk fa,
            Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {

        try {
            Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
            Long pg = numHome.getNextPg(userContext,
                    buonoTemporaneo.getEsercizio(),
                    buonoTemporaneo.getPg_inventario(),
                    buonoTemporaneo.getTi_documento(),
                    userContext.getUser());

            Buono_carico_scaricoBulk definitivo = (Buono_carico_scaricoBulk) buonoTemporaneo.clone();
            definitivo.setPg_buono_c_s(pg);
            definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
            Buono_carico_scaricoHome home = (Buono_carico_scaricoHome) getHome(userContext, definitivo);
            home.makePersistentScaricoDaFattura(definitivo, fa);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(buonoTemporaneo, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(buonoTemporaneo, e);
        }
    }

    /*private void aggiornaBuoniScaricoTemporanei(UserContext userContext, Fattura_attivaBulk fa, Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {

	try {
		//Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
		//Long pg = null;
		//try {
			//pg = numHome.getNextPg(
						//buonoTemporaneo.getEsercizio(),
						//buonoTemporaneo.getPg_inventario(),
						//buonoTemporaneo.getTi_documento(),
						//userContext.getUser());
		//} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
			//throw handleException(e);
		//} catch (it.cnr.jada.bulk.BusyResourceException e) {
			//throw handleException(e);
		//}
		Buono_scaricoHome home = (Buono_scaricoHome)getHome(userContext, buonoTemporaneo);

		//home.confirmBuonoCaricoScaricoTemporaneo(buonoTemporaneo, pg);

		if (home instanceof it.cnr.contab.inventario00.docs.bulk.Buono_scaricoHome)
			((it.cnr.contab.inventario00.docs.bulk.Buono_scaricoHome)home).makePersistentScaricoDaFattura((Buono_scaricoBulk)buonoTemporaneo, fa);
		//if (buonoTemporaneo instanceof Buono_scaricoBulk) {
			//Buono_scaricoBulk bs = (Buono_scaricoBulk)buonoTemporaneo;
			//if (bs.getDettagliRigheHash() != null && !bs.getDettagliRigheHash().isEmpty())
				//home.makePersistentRigheFatturaAttiva(bs.getDettagliRigheHash());
		//}

	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(buonoTemporaneo, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(buonoTemporaneo, e);
	}
}*/
    private void aggiornaCogeCoan(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            IDocumentoContabileBulk docCont)
            throws ComponentException {

        try {
            if (docCont != null && fatturaAttiva != null && fatturaAttiva.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = fatturaAttiva.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) fatturaAttiva.getDefferredSaldi().get(key);

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
                        fatturaAttiva.getDefferredSaldi().remove(key);
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(fatturaAttiva, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(fatturaAttiva, e);
        }
    }

    private void aggiornaCogeCoanAccertamenti(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva != null) {
            AccertamentiTable accertamentiHash = fatturaAttiva.getFattura_attiva_accertamentiHash();
            if (accertamentiHash != null && !accertamentiHash.isEmpty()) {

                //Aggiorna coge coan per le obbligazioni NON temporanee
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((AccertamentiTable) accertamentiHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaCogeCoan(
                            userContext,
                            fatturaAttiva,
                            (IDocumentoContabileBulk) e.nextElement());

            }
        }
    }

    private void aggiornaCogeCoanAccertamentiDaCancellare(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva != null) {
            if (fatturaAttiva.getDocumentiContabiliCancellati() != null &&
                    !fatturaAttiva.getDocumentiContabiliCancellati().isEmpty() &&
                    fatturaAttiva.getAccertamentiHash() != null) {

                for (java.util.Enumeration e = fatturaAttiva.getDocumentiContabiliCancellati().elements(); e.hasMoreElements(); ) {
                    OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                    if (oggettoBulk instanceof Accertamento_scadenzarioBulk) {
                        Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) oggettoBulk;
                        if (!scadenza.getAccertamento().isTemporaneo()) {
                            PrimaryKeyHashtable accerts = getDocumentiContabiliNonTemporanei(userContext, fatturaAttiva.getAccertamentiHash().keys());
                            if (!accerts.containsKey(scadenza.getAccertamento()))
                                aggiornaCogeCoan(
                                        userContext,
                                        fatturaAttiva,
                                        scadenza.getAccertamento());
                        }
                    }
                }
            }
        }
    }

    private void aggiornaCogeCoanDocAmm(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva == null) return;

        aggiornaCogeCoanAccertamentiDaCancellare(userContext, fatturaAttiva);
        aggiornaCogeCoanAccertamenti(userContext, fatturaAttiva);

        if (fatturaAttiva instanceof Nota_di_credito_attivaBulk) {
            aggiornaCogeCoanObbligazioniDaCancellare(userContext, (Nota_di_credito_attivaBulk) fatturaAttiva);
            aggiornaCogeCoanObbligazioni(userContext, (Nota_di_credito_attivaBulk) fatturaAttiva);
        }
    }

    private void aggiornaCogeCoanObbligazioni(
            UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws ComponentException {

        if (notaDiCredito != null) {
            ObbligazioniTable obbligazioniHash = notaDiCredito.getObbligazioni_scadenzarioHash();
            if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {
                //Aggiorna coge coan per gli accertamenti NON temporanei
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable) obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaCogeCoan(userContext,
                            notaDiCredito,
                            (IDocumentoContabileBulk) e.nextElement());
            }
        }
    }
//^^@@

    private void aggiornaCogeCoanObbligazioniDaCancellare(
            UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws ComponentException {

        if (notaDiCredito != null) {
            if (notaDiCredito.getDocumentiContabiliCancellati() != null &&
                    !notaDiCredito.getDocumentiContabiliCancellati().isEmpty() &&
                    notaDiCredito.getObbligazioniHash() != null) {

                for (java.util.Enumeration e = notaDiCredito.getDocumentiContabiliCancellati().elements(); e.hasMoreElements(); ) {
                    OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                    if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
                        Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) oggettoBulk;
                        if (!scadenza.getObbligazione().isTemporaneo()) {
                            PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, notaDiCredito.getObbligazioniHash().keys());
                            if (!obbligs.containsKey(scadenza.getObbligazione()))
                                aggiornaCogeCoan(
                                        userContext,
                                        notaDiCredito,
                                        scadenza.getObbligazione());
                        }
                    }
                }
            }
        }
    }

    /**
     * Calcolo totali di fattura.
     * PreCondition:
     * Viene richiesti il calcolo dei totali di fattura
     * PostCondition:
     * Vegono calcolati i totali per la fattura attiva.
     * Si verifica errore.
     * PreCondition:
     * Condizione di errore.
     * PostCondition:
     * Viene gestito l'errore
     */
//^^@@
    public Fattura_attivaBulk aggiornaImportiTotali(it.cnr.jada.UserContext uc, Fattura_attivaBulk fattura) throws ComponentException {

        BulkList dettaglio = fattura.getFattura_attiva_dettColl();
        if (dettaglio == null)
            return fattura;

        java.math.BigDecimal imp = new java.math.BigDecimal(0);
        imp = imp.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal iva = new java.math.BigDecimal(0);
        iva = iva.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal totale = new java.math.BigDecimal(0);
        totale = totale.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

        try {
            for (Iterator i = dettaglio.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                if (!riga.isAnnullato()) {
                    riga.calcolaCampiDiRiga();
                    imp = imp.add(riga.getIm_imponibile());
                    iva = iva.add(riga.getIm_iva());
                    totale = totale.add(riga.getIm_imponibile().add(riga.getIm_iva()));
                }
            }
        } catch (Throwable t) {
            throw handleException(fattura, t);
        }
        fattura.setIm_totale_imponibile(imp);
        fattura.setIm_totale_iva(iva);
        fattura.setIm_totale_fattura(totale);

        return fattura;
    }

    private void aggiornaObbligazioni(
            UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito,
            OptionRequestParameter status)
            throws ComponentException {

        if (notaDiCredito != null) {
            ObbligazioniTable obbligazioniHash = notaDiCredito.getObbligazioni_scadenzarioHash();
            if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {
                Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(userContext, Obbligazione_scadenzarioBulk.class);

                //Aggiorna i saldi per gli obbligazioni NON temporanei
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable) obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaSaldi(
                            userContext,
                            notaDiCredito,
                            (IDocumentoContabileBulk) e.nextElement(),
                            status);

                it.cnr.jada.bulk.PrimaryKeyHashtable obblTemporanee = getDocumentiContabiliTemporanei(userContext, ((ObbligazioniTable) obbligazioniHash.clone()).keys());
                for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements(); ) {
                    ObbligazioneBulk obblT = (ObbligazioneBulk) e.nextElement();

                    //Aggiorna i saldi per le obbligazioni temporanee
                    //DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
                    aggiornaSaldi(userContext, notaDiCredito, obblT, status);

                    aggiornaObbligazioniTemporanee(userContext, obblT);
                    obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable(obblTemporanee);
                    for (Iterator i = ((Vector) obblTemporanee.get(obblT)).iterator(); i.hasNext(); )
                        ((ObbligazioneBulk) i.next()).setPg_obbligazione(obblT.getPg_obbligazione());
                }
                ObbligazioniTable newObbligazioniHash = new ObbligazioniTable(obbligazioniHash);
                notaDiCredito.setObbligazioni_scadenzarioHash(newObbligazioniHash);
                for (java.util.Enumeration e = ((ObbligazioniTable) newObbligazioniHash.clone()).keys(); e.hasMoreElements(); ) {
                    Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) e.nextElement();
                    scadenza.setIm_associato_doc_amm(calcolaTotalePer((java.util.List) newObbligazioniHash.get(scadenza), notaDiCredito.quadraturaInDeroga()));
                    updateImportoAssociatoDocAmm(userContext, scadenza);
                }
            }
        }
    }

    private void aggiornaObbligazioniSuCancellazione(
            UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito,
            java.util.Enumeration scadenzeDaCancellare,
            java.util.Collection scadenzeConfermate,
            OptionRequestParameter status)
            throws ComponentException {

        if (scadenzeDaCancellare != null) {

            it.cnr.jada.bulk.PrimaryKeyHashtable obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable();
            for (java.util.Enumeration e = scadenzeDaCancellare; e.hasMoreElements(); ) {
                OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
                    Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) oggettoBulk;
                    if (scadenza.getObbligazione().isTemporaneo()) {
                        if (!obblTemporanee.containsKey(scadenza.getObbligazione())) {
                            Vector allInstances = new java.util.Vector();
                            allInstances.addElement(scadenza);
                            obblTemporanee.put(scadenza.getObbligazione(), allInstances);
                        } else {
                            ((Vector) obblTemporanee.get(scadenza.getObbligazione())).add(scadenza);
                        }
                    } else if (!notaDiCredito.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
                        PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, notaDiCredito.getObbligazioniHash().keys());
                        if (!obbligs.containsKey(scadenza.getObbligazione()))
                            aggiornaSaldi(
                                    userContext,
                                    notaDiCredito,
                                    scadenza.getObbligazione(),
                                    status);
                        scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                        updateImportoAssociatoDocAmm(userContext, scadenza);
                    }
                    /**
                     * Devo aggiornare i Saldi per quelle scadenze modificate e riportate
                     * ma poi scollegate dal documento
                     * Marco Spasiano 05/05/2006
                     */
                    aggiornaSaldi(userContext, notaDiCredito, scadenza.getObbligazione(), status);
                }
            }
            for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements(); ) {
                ObbligazioneBulk obblT = (ObbligazioneBulk) e.nextElement();
                if (scadenzeConfermate == null || !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(scadenzeConfermate, obblT)) {

                    //Aggiorna i saldi per le obbligazioni temporanee
                    //DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
                    PrimaryKeyHashtable obbligs = getDocumentiContabiliTemporanei(userContext, notaDiCredito.getObbligazioniHash().keys());
                    if (!obbligs.containsKey(obblT))
                        aggiornaSaldi(userContext, notaDiCredito, obblT, status);

                    for (Iterator i = ((Vector) obblTemporanee.get(obblT)).iterator(); i.hasNext(); ) {
                        Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) i.next();
                        scad.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                        updateImportoAssociatoDocAmm(userContext, scad);
                    }
                    aggiornaObbligazioniTemporanee(userContext, obblT);
                }
            }
        }
    }

    private void aggiornaObbligazioniTemporanee(UserContext userContext, ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {

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

    private void aggiornaRigheFatturaAttivaDiOrigine(UserContext aUC, Nota_di_credito_attivaBulk ndc) throws ComponentException {

        for (Iterator i = ndc.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaIBulk rigaFA = ((Nota_di_credito_attiva_rigaBulk) i.next()).getRiga_fattura_associata();
            basicAggiornaRigheFatturaAttivaDiOrigine(aUC, rigaFA);
        }
    }

    private void aggiornaRigheFatturaAttivaDiOrigine(UserContext aUC, Nota_di_debito_attivaBulk ndd) throws ComponentException {

        for (Iterator i = ndd.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaIBulk rigaFA = ((Nota_di_debito_attiva_rigaBulk) i.next()).getRiga_fattura_associata();
            basicAggiornaRigheFatturaAttivaDiOrigine(aUC, rigaFA);
        }
    }

    private void aggiornaSaldi(
            it.cnr.jada.UserContext uc,
            Fattura_attivaBulk fa,
            IDocumentoContabileBulk docCont,
            OptionRequestParameter status)
            throws ComponentException {

        try {
            if (docCont != null && fa != null && fa.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = fa.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) fa.getDefferredSaldi().get(key);
                    //QUI chiamare component del documento contabile interessato
                    String jndiName = null;
                    Class clazz = null;
                    it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession session = null;
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
                        session.aggiornaSaldiInDifferita(uc, key, values, status);
                        //NON Differibile: si rischia di riprocessare i saldi impropriamente
                        fa.getDefferredSaldi().remove(key);
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(fa, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(fa, e);
        }
    }

    private void aggiornaScarichiInventario(UserContext userContext, Fattura_attivaBulk fattura_attiva) throws ComponentException {

        if (fattura_attiva != null) {
            CarichiInventarioTable carichiInventarioHash = fattura_attiva.getCarichiInventarioHash();
            if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
                Vector carichiTemporanei = new Vector();
                for (java.util.Enumeration e = ((CarichiInventarioTable) carichiInventarioHash.clone()).keys(); e.hasMoreElements(); ) {
                    Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) e.nextElement();
                    Buono_carico_scaricoHome home = (Buono_carico_scaricoHome) getHome(userContext, buonoCS);
                    if (buonoCS.isByFattura() &&
                            !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(carichiTemporanei, buonoCS))
                        carichiTemporanei.add(buonoCS);
                }

                for (Iterator i = carichiTemporanei.iterator(); i.hasNext(); ) {
                    Buono_carico_scaricoBulk buono_temporaneo = (Buono_carico_scaricoBulk) i.next();
                    aggiornaEstremiFattura(userContext, fattura_attiva, buono_temporaneo);
                    aggiornaBuoniScaricoTemporanei(userContext, fattura_attiva, buono_temporaneo);

                }
            }
        }
    }
//^^@@

    private void aggiornaEstremiFattura(UserContext userContext, Fattura_attivaBulk fattura_attiva, Buono_carico_scaricoBulk buono_temporaneo) throws ComponentException {
        try {
            Inventario_beni_apgHome apgHome = (Inventario_beni_apgHome) getHome(userContext, Inventario_beni_apgBulk.class);
            SQLBuilder sql = apgHome.createSQLBuilder();
            sql.addSQLClause("AND", "LOCAL_TRANSACTION_ID", sql.EQUALS, buono_temporaneo.getLocal_transactionID());
            List beniApg = apgHome.fetchAll(sql);
            for (Iterator i = beniApg.iterator(); i.hasNext(); ) {
                Inventario_beni_apgBulk beneApg = (Inventario_beni_apgBulk) i.next();
                beneApg.setPg_fattura(fattura_attiva.getPg_fattura_attiva());
                updateBulk(userContext, beneApg);
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * non utilizzato
     */
//^^@@
    public void aggiornaStatoDocumentiAmministrativi(
            it.cnr.jada.UserContext userContext,
            String cd_cds,
            String cd_unita_organizzativa,
            String tipo_documento,
            Integer esercizio,
            Long progressivo,
            String action)
            throws it.cnr.jada.comp.ComponentException {
    }

    /**
     * Normale.
     * PreCondition:
     * Viene annullata richiesta la protocollazione o la ristampa dei documenti attivi
     * PostCondition:
     * Viene ripristinata la situazione iniziale
     */

    public void annullaSelezionePerStampa(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        try {
            rollbackToSavepoint(userContext, "STAMPA_IVA_FA");
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }

    private void assegnaProgressivo(UserContext userContext, Fattura_attivaBulk fattura_attiva) throws ComponentException {

        try {
            // Assegno un nuovo progressivo alla fattura
            ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
            Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(fattura_attiva);
            fattura_attiva.setPg_fattura_attiva(progressiviSession.getNextPG(userContext, numerazione));

            Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
            Numerazione_doc_ammBulk numerazioneProgressivoUnivoco = new Numerazione_doc_ammBulk(fattura_attiva, uoEnte);
            numerazioneProgressivoUnivoco.setCd_tipo_documento_amm(Numerazione_doc_ammBulk.TIPO_UNIVOCO_FATTURA_ATTIVA);
            fattura_attiva.setProgrUnivocoAnno(progressiviSession.getNextPG(userContext, numerazioneProgressivoUnivoco));
        } catch (Throwable t) {
            throw handleException(fattura_attiva, t);
        }
    }

    private Nota_di_debito_attivaBulk basicAddebitaDettaglio(
            UserContext context,
            Nota_di_debito_attivaBulk ndD,
            Nota_di_debito_attiva_rigaBulk rigaNdD,
            Fattura_attiva_rigaIBulk rigaAssociata)
            throws ComponentException {

        Accertamento_scadenzarioBulk accertamentoSelezionato = rigaAssociata.getAccertamento_scadenzario();
        if (accertamentoSelezionato != null) {

            if (ndD.getFattura_attiva_accertamentiHash() != null) {
                Accertamento_scadenzarioBulk key = ndD.getFattura_attiva_accertamentiHash().getKey(accertamentoSelezionato);
                if (key != null) accertamentoSelezionato = key;
                else accertamentoSelezionato = caricaScadenzaAccertamentoPer(context, accertamentoSelezionato);
            } else accertamentoSelezionato = caricaScadenzaAccertamentoPer(context, accertamentoSelezionato);


            rigaNdD.setAccertamento_scadenzario(accertamentoSelezionato);
            rigaNdD.setRiga_fattura_associata(rigaAssociata);
            rigaNdD.setStato_cofi(rigaNdD.STATO_CONTABILIZZATO);
            rigaNdD.setToBeUpdated();
            ndD.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, rigaNdD);
            //ndD.setStato_coan(ndD.NON_PROCESSARE_IN_COAN);
            if (ndD.getStato_cofi() != ndD.STATO_PAGATO)
                ndD.setStato_cofi((ndD.getFattura_attiva_accertamentiHash().isEmpty()) ?
                        ndD.STATO_INIZIALE :
                        ndD.STATO_CONTABILIZZATO);
            try {
                AccertamentoAbstractComponentSession session = (AccertamentoAbstractComponentSession) EJBCommonServices.createEJB(
                        "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
                        AccertamentoAbstractComponentSession.class);
                session.lockScadenza(context, accertamentoSelezionato);
            } catch (Throwable t) {
                throw handleException(ndD, t);
            }
        }
        return ndD;
    }

    private void basicAggiornaRigheFatturaAttivaDiOrigine(UserContext aUC, Fattura_attiva_rigaIBulk rigaFA) throws ComponentException {

        try {
            updateBulk(aUC, rigaFA);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(rigaFA, e);
        }
    }

    private Nota_di_credito_attivaBulk basicStornaDettaglio(
            UserContext context,
            Nota_di_credito_attivaBulk ndC,
            Nota_di_credito_attiva_rigaBulk rigaNdC,
            Fattura_attiva_rigaIBulk rigaAssociata)
            throws ComponentException {

        Accertamento_scadenzarioBulk accertamentoSelezionato = rigaAssociata.getAccertamento_scadenzario();
        if (accertamentoSelezionato != null) {

            if (ndC.getFattura_attiva_accertamentiHash() != null) {
                Accertamento_scadenzarioBulk key = ndC.getFattura_attiva_accertamentiHash().getKey(accertamentoSelezionato);
                if (key != null) accertamentoSelezionato = key;
                else accertamentoSelezionato = caricaScadenzaAccertamentoPer(context, accertamentoSelezionato);
                //Questo controllo NON è + necessario. Viene fatto dall'obbligazione
                //java.util.List dettagliGiaCollegati = (java.util.List)ndC.getFattura_attiva_accertamentiHash().get(accertamentoSelezionato);
                //if (dettagliGiaCollegati != null && !dettagliGiaCollegati.isEmpty()) {
                //java.math.BigDecimal importo = calcolaTotalePer(dettagliGiaCollegati);
                //if (importo.add(rigaNdC.getIm_imponibile().add(rigaNdC.getIm_iva())).compareTo(accertamentoSelezionato.getIm_scadenza()) > 0)
                //throw new it.cnr.jada.comp.ApplicationException("I dettagli che si sta cercando di collegare superano la disponibiltà della scadenza!");
                //}
            } else accertamentoSelezionato = caricaScadenzaAccertamentoPer(context, accertamentoSelezionato);

            if (accertamentoSelezionato.getEsercizio() != null && accertamentoSelezionato.getEsercizio().compareTo(ndC.getEsercizio()) != 0) {
                throw new it.cnr.jada.comp.ApplicationException("Per la riga di fattura " + rigaAssociata.getCd_cds() + "/" + rigaAssociata.getEsercizio() + "/" + rigaAssociata.getPg_fattura_attiva() + "/" + rigaAssociata.getProgressivo_riga() +
                        " non è possibile creare la nota di credito in quanto l'esercizio dell'accertamento " + accertamentoSelezionato.getCd_cds() + "/" + accertamentoSelezionato.getEsercizio() + "/" + accertamentoSelezionato.getPg_accertamento() + " non corrisponde all'esercizio della nota di credito");
            }

            rigaNdC.setAccertamento_scadenzario(accertamentoSelezionato);
            rigaNdC.setRiga_fattura_associata(rigaAssociata);
            rigaNdC.setStato_cofi(rigaNdC.STATO_CONTABILIZZATO);
            rigaNdC.setToBeUpdated();
            ndC.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, rigaNdC);
            //ndC.setStato_coan(ndC.NON_PROCESSARE_IN_COAN);
            if (ndC.getStato_cofi() != ndC.STATO_PAGATO)
                ndC.setStato_cofi((ndC.getFattura_attiva_accertamentiHash().isEmpty()) ?
                        ndC.STATO_INIZIALE :
                        ndC.STATO_CONTABILIZZATO);
            try {
                AccertamentoAbstractComponentSession session = (AccertamentoAbstractComponentSession) EJBCommonServices.createEJB(
                        "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
                        AccertamentoAbstractComponentSession.class);
                session.lockScadenza(context, accertamentoSelezionato);
            } catch (Throwable t) {
                throw handleException(ndC, t);
            }
        }
        return ndC;
    }

    private Nota_di_credito_attivaBulk basicStornaDettaglio(
            UserContext context,
            Nota_di_credito_attivaBulk ndC,
            Nota_di_credito_attiva_rigaBulk rigaNdC,
            Obbligazione_scadenzarioBulk scadenza)
            throws ComponentException {

        if (scadenza != null) {

            if (ndC.getObbligazioni_scadenzarioHash() != null) {
                Obbligazione_scadenzarioBulk key = ndC.getObbligazioni_scadenzarioHash().getKey(scadenza);
                if (key != null) scadenza = key;
                else scadenza = caricaScadenzaObbligazionePer(context, scadenza);
            } else scadenza = caricaScadenzaObbligazionePer(context, scadenza);

            if (scadenza.getEsercizio() != null && scadenza.getEsercizio().compareTo(ndC.getEsercizio()) != 0) {
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare la nota di credito in quanto l'esercizio dell'obbligazione " + scadenza.getCd_cds() + "/" + scadenza.getEsercizio() + "/" + scadenza.getPg_obbligazione() + " non corrisponde all'esercizio della nota di credito");
            }

            rigaNdC.setObbligazione_scadenzario(scadenza);
            rigaNdC.setRiga_fattura_associata(rigaNdC.getRiga_fattura_associata());
            rigaNdC.setStato_cofi(rigaNdC.STATO_CONTABILIZZATO);
            impostaCollegamentoCapitoloPerTrovato(context, rigaNdC);
            rigaNdC.setToBeUpdated();
            ndC.addToObbligazioni_scadenzarioHash(scadenza, rigaNdC);
            if (ndC.getStato_cofi() != ndC.STATO_PAGATO)
                ndC.setStato_cofi((ndC.getObbligazioni_scadenzarioHash().isEmpty()) ?
                        ndC.STATO_INIZIALE :
                        ndC.STATO_CONTABILIZZATO);
            try {
                ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession) EJBCommonServices.createEJB(
                        "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
                        ObbligazioneAbstractComponentSession.class);
                session.lockScadenza(context, scadenza);
            } catch (Throwable t) {
                throw handleException(ndC, t);
            }
        }
        return ndC;
    }

    private java.math.BigDecimal calcolaTotaleAccertamentoPer(
            it.cnr.jada.UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Fattura_attivaBulk fatturaAttiva)
            throws it.cnr.jada.comp.ComponentException {

        java.math.BigDecimal imp = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        if (fatturaAttiva instanceof Nota_di_credito_attivaBulk) {
            imp = calcolaTotaleAccertamentoPerNdC(
                    userContext,
                    scadenza,
                    (Nota_di_credito_attivaBulk) fatturaAttiva);
        } else if (fatturaAttiva instanceof Nota_di_debito_attivaBulk) {
            imp = calcolaTotaleAccertamentoPerNdD(
                    userContext,
                    scadenza,
                    (Nota_di_debito_attivaBulk) fatturaAttiva);
        } else {
            imp = calcolaTotaleAccertamentoPerFA(
                    userContext,
                    scadenza,
                    (Fattura_attivaBulk) fatturaAttiva);
        }
        return imp;
    }

    private java.math.BigDecimal calcolaTotaleAccertamentoPerFA(
            it.cnr.jada.UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Fattura_attivaBulk fatturaAttiva)
            throws it.cnr.jada.comp.ComponentException {

        AccertamentiTable accertamentiHash = fatturaAttiva.getFattura_attiva_accertamentiHash();
        Vector dettagli = (Vector) accertamentiHash.get(scadenza);
        java.math.BigDecimal impTotaleDettagli = calcolaTotalePer(dettagli, fatturaAttiva.quadraturaInDeroga());
        java.math.BigDecimal impTotaleStornati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal impTotaleAddebitati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.util.HashMap dettagliDaStornare = caricaStorniPer(userContext, dettagli);
        java.util.HashMap dettagliDaAddebitare = caricaAddebitiPer(userContext, dettagli);
        boolean quadraturaInDeroga = fatturaAttiva.quadraturaInDeroga();
        if (dettagliDaStornare != null) {
            Vector dettagliStornati = null;
            for (Iterator dett = dettagliDaStornare.keySet().iterator(); dett.hasNext(); ) {
                Fattura_attiva_rigaIBulk key = (Fattura_attiva_rigaIBulk) dett.next();
                for (Iterator i = ((Vector) dettagliDaStornare.get(key)).iterator(); i.hasNext(); ) {
                    Nota_di_credito_attiva_rigaBulk riga = (Nota_di_credito_attiva_rigaBulk) i.next();
                    AccertamentiTable hash = riga.getFattura_attiva().getFattura_attiva_accertamentiHash();
                    if (hash == null)
                        rebuildAccertamenti(userContext, riga.getFattura_attiva());
                    hash = riga.getFattura_attiva().getFattura_attiva_accertamentiHash();
                    if (hash != null) {
                        if (hash != null) {
                            Vector dettagliStornatiScadenza = (Vector) hash.get(scadenza);
                            if (dettagliStornatiScadenza != null) {
                                if (dettagliStornati == null) {
                                    dettagliStornati = new Vector();
                                    dettagliStornati.addAll(dettagliStornatiScadenza);
                                } else {
                                    for (Iterator d = dettagliStornatiScadenza.iterator(); d.hasNext(); ) {
                                        Nota_di_credito_attiva_rigaBulk rigaNdC = (Nota_di_credito_attiva_rigaBulk) d.next();
                                        if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(dettagliStornati, rigaNdC))
                                            dettagliStornati.add(rigaNdC);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (dettagliStornati != null)
                impTotaleStornati = impTotaleStornati.add(
                        calcolaTotalePer(dettagliStornati, quadraturaInDeroga));
        }
        if (dettagliDaAddebitare != null) {
            Vector dettagliAddebitati = null;

            for (Iterator dett = dettagliDaAddebitare.keySet().iterator(); dett.hasNext(); ) {
                Fattura_attiva_rigaIBulk key = (Fattura_attiva_rigaIBulk) dett.next();
                for (Iterator i = ((Vector) dettagliDaAddebitare.get(key)).iterator(); i.hasNext(); ) {
                    Nota_di_debito_attiva_rigaBulk riga = (Nota_di_debito_attiva_rigaBulk) i.next();
                    AccertamentiTable hash = riga.getFattura_attiva().getFattura_attiva_accertamentiHash();
                    if (hash == null)
                        rebuildAccertamenti(userContext, riga.getFattura_attiva());
                    hash = riga.getFattura_attiva().getFattura_attiva_accertamentiHash();
                    Vector dettagliAddebitatiScadenza = (Vector) hash.get(scadenza);
                    if (dettagliAddebitatiScadenza != null) {
                        if (hash != null) {
                            if (dettagliAddebitati == null) {
                                dettagliAddebitati = new Vector();
                                dettagliAddebitati.addAll(dettagliAddebitatiScadenza);
                            } else {
                                for (Iterator d = dettagliAddebitatiScadenza.iterator(); d.hasNext(); ) {
                                    Nota_di_debito_attiva_rigaBulk rigaNdD = (Nota_di_debito_attiva_rigaBulk) d.next();
                                    if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(dettagliAddebitati, rigaNdD))
                                        dettagliAddebitati.add(rigaNdD);
                                }
                            }
                        }
                    }
                }
            }
            if (dettagliAddebitati != null)
                impTotaleAddebitati = impTotaleAddebitati.add(calcolaTotalePer(dettagliAddebitati, quadraturaInDeroga));
        }
        return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
    }

    private java.math.BigDecimal calcolaTotaleAccertamentoPerNdC(
            it.cnr.jada.UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws it.cnr.jada.comp.ComponentException {

        AccertamentiTable accertamentiHash = notaDiCredito.getFattura_attiva_accertamentiHash();
        Vector dettagli = (Vector) accertamentiHash.get(scadenza);
        java.math.BigDecimal impTotaleDettagli = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal impTotaleStornati = calcolaTotalePer(dettagli, notaDiCredito.quadraturaInDeroga()).add(calcolaTotalePer(caricaStorniExceptFor(userContext, scadenza, notaDiCredito), notaDiCredito.quadraturaInDeroga()));
        java.math.BigDecimal impTotaleAddebitati = calcolaTotalePer(caricaAddebitiExceptFor(userContext, scadenza, null), notaDiCredito.quadraturaInDeroga());
        Vector fattureContenute = new Vector();
        for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
            Nota_di_credito_attiva_rigaBulk riga = (Nota_di_credito_attiva_rigaBulk) i.next();
            Fattura_attiva_IBulk fatturaOrigine = riga.getRiga_fattura_associata().getFattura_attivaI();
            if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(fattureContenute, fatturaOrigine))
                fattureContenute.add(fatturaOrigine);
        }
        for (Iterator i = fattureContenute.iterator(); i.hasNext(); ) {
            Fattura_attiva_IBulk fatturaAttivaI = (Fattura_attiva_IBulk) i.next();
            if (fatturaAttivaI.getFattura_attiva_dettColl() == null ||
                    fatturaAttivaI.getFattura_attiva_dettColl().isEmpty()) {
                try {
                    fatturaAttivaI.setFattura_attiva_dettColl(new BulkList(findDettagli(userContext, fatturaAttivaI)));
                    rebuildAccertamenti(userContext, fatturaAttivaI);
                } catch (Throwable t) {
                    throw handleException(t);
                }
            }

            Vector dettagliContabilizzati = (Vector) fatturaAttivaI.getFattura_attiva_accertamentiHash().get(scadenza);
            if (dettagliContabilizzati != null)
                impTotaleDettagli = impTotaleDettagli.add(calcolaTotalePer(dettagliContabilizzati, notaDiCredito.quadraturaInDeroga()));
        }

        return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
    }

    private java.math.BigDecimal calcolaTotaleAccertamentoPerNdD(
            it.cnr.jada.UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Nota_di_debito_attivaBulk notaDiDebito)
            throws it.cnr.jada.comp.ComponentException {

        AccertamentiTable accertamentiHash = notaDiDebito.getFattura_attiva_accertamentiHash();
        Vector dettagli = (Vector) accertamentiHash.get(scadenza);
        java.math.BigDecimal impTotaleDettagli = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal impTotaleStornati = calcolaTotalePer(caricaStorniExceptFor(userContext, scadenza, null), notaDiDebito.quadraturaInDeroga());
        java.math.BigDecimal impTotaleAddebitati = calcolaTotalePer(dettagli, notaDiDebito.quadraturaInDeroga()).add(calcolaTotalePer(caricaAddebitiExceptFor(userContext, scadenza, notaDiDebito), notaDiDebito.quadraturaInDeroga()));
        Vector fattureContenute = new Vector();
        for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
            Nota_di_debito_attiva_rigaBulk riga = (Nota_di_debito_attiva_rigaBulk) i.next();
            Fattura_attiva_IBulk fatturaOrigine = riga.getRiga_fattura_associata().getFattura_attivaI();
            if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(fattureContenute, fatturaOrigine))
                fattureContenute.add(fatturaOrigine);
        }
        for (Iterator i = fattureContenute.iterator(); i.hasNext(); ) {
            Fattura_attiva_IBulk fatturaAttivaI = (Fattura_attiva_IBulk) i.next();
            if (fatturaAttivaI.getFattura_attiva_dettColl() == null ||
                    fatturaAttivaI.getFattura_attiva_dettColl().isEmpty()) {
                try {
                    fatturaAttivaI.setFattura_attiva_dettColl(new BulkList(findDettagli(userContext, fatturaAttivaI)));
                    rebuildAccertamenti(userContext, fatturaAttivaI);
                } catch (Throwable t) {
                    throw handleException(t);
                }
            }

            Vector dettagliContabilizzati = (Vector) fatturaAttivaI.getFattura_attiva_accertamentiHash().get(scadenza);
            if (dettagliContabilizzati != null)
                impTotaleDettagli = impTotaleDettagli.add(calcolaTotalePer(dettagliContabilizzati, notaDiDebito.quadraturaInDeroga()));
        }

        return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
    }

    private java.math.BigDecimal calcolaTotalePer(java.util.List selectedModels,
                                                  boolean escludiIVA)
            throws it.cnr.jada.comp.ApplicationException {

        java.math.BigDecimal importo = new java.math.BigDecimal(0);

        if (selectedModels != null) {
            for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk rigaSelected = (Fattura_attiva_rigaBulk) i.next();
                if (escludiIVA)
                    importo = importo.add(rigaSelected.getIm_imponibile());
                else
                    importo = importo.add(rigaSelected.getIm_imponibile().add(rigaSelected.getIm_iva()));
            }
        }
        importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        return importo;
    }
//^^@@

    private java.math.BigDecimal calcolaTotaleScadenzaObbligazionePer(
            it.cnr.jada.UserContext userContext,
            Obbligazione_scadenzarioBulk scadenza,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws it.cnr.jada.comp.ComponentException {

        Vector dettagli = (Vector) notaDiCredito.getObbligazioniHash().get(scadenza);
        return calcolaTotalePer(dettagli, notaDiCredito.quadraturaInDeroga());
    }

    /**
     * Calcola i consuntivi del documento amministrativo
     * PreCondition:
     * viene richiesto il consuntivo
     * PostCondition:
     * Vegono restituiti i valori calcolati.
     */
//^^@@
    public IDocumentoAmministrativoBulk calcoloConsuntivi(UserContext aUC, IDocumentoAmministrativoBulk documentoAmministrativo) throws ComponentException {

        if (documentoAmministrativo == null)
            return documentoAmministrativo;

        Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) documentoAmministrativo;
        fatturaAttiva.setFattura_attiva_consuntivoColl(new Vector());

        BulkList righeFattura = fatturaAttiva.getFattura_attiva_dettColl();
        if (righeFattura == null)
            return fatturaAttiva;

        for (Iterator i = righeFattura.iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
            if (!riga.isAnnullato()) {
                Consuntivo_rigaVBulk rigaConsuntivo = getRigaConsuntivoFor(riga);
                if (rigaConsuntivo != null) {
                    java.math.BigDecimal totaleImponibile = rigaConsuntivo.getTotale_imponibile().add((riga.getIm_imponibile() == null) ? new java.math.BigDecimal(0) : riga.getIm_imponibile());
                    rigaConsuntivo.setTotale_imponibile(totaleImponibile);
                    java.math.BigDecimal totaleIva = rigaConsuntivo.getTotale_iva().add((riga.getIm_iva() == null) ? new java.math.BigDecimal(0) : riga.getIm_iva());
                    rigaConsuntivo.setTotale_iva(totaleIva);
                    java.math.BigDecimal totalePrezzo = rigaConsuntivo.getTotale_prezzo().add(riga.getIm_imponibile().add(riga.getIm_iva()));
                    rigaConsuntivo.setTotale_prezzo(totalePrezzo);
                }
            }
        }

        return aggiornaImportiTotali(aUC, fatturaAttiva);
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesto un progressivo per la protocollazione
     * PostCondition:
     * Tale valore viene restituito
     */

    public Long callGetPgPerProtocolloIVA(
            UserContext userContext)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        Long pg = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "IBMUTL020.vsx_get_pg_call() }", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                //cs.setString(1, cd_cds);
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
            throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la vista di assegnazione del protocollo IVA!");
        return pg;
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesto un progressivo per la ristampa
     * PostCondition:
     * Tale valore viene restituito
     */

    public Long callGetPgPerStampa(
            UserContext userContext)
            throws it.cnr.jada.comp.ComponentException {

        //ricavo il progressivo unico pg_stampa
        Long pg_Stampa = new Long(0);
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "select IBMSEQ00_STAMPA.nextval from dual", true, this.getClass());
            try {
                java.sql.ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next())
                        pg_Stampa = new Long(rs.getLong(1));
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
        return pg_Stampa;
    }

    /**
     * Reintegra le spese in elenco sul fondo economale
     */

    private void callRifProtocolloIVA(
            UserContext userContext,
            Long progressivo)
            throws it.cnr.jada.comp.ComponentException, PersistencyException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB150.vsx_protocollazione_doc(?) }", false, this.getClass());
            cs.setLong(1, progressivo.longValue());
            cs.executeQuery();
        } catch (java.sql.SQLException e) {
            throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e);
            //throw new PersistencyException(e);
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

    private void callRiportaAvanti(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB046.riportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }", false, this.getClass());

            cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
            cs.setString(2, fatturaAttiva.getCd_tipo_doc_amm());
            cs.setString(3, fatturaAttiva.getCd_cds());
            cs.setInt(4, fatturaAttiva.getEsercizio().intValue());
            cs.setString(5, fatturaAttiva.getCd_uo());
            cs.setLong(6, fatturaAttiva.getPg_doc_amm().longValue());
            cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

            cs.executeQuery();

        } catch (Throwable e) {
            throw handleException(fatturaAttiva, e);
        } finally {
            try {
                if (cs != null) cs.close();
            } catch (java.sql.SQLException e) {
                throw handleException(fatturaAttiva, e);
            }
        }
    }

    private void callRiportaIndietro(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB046.deriportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }", false, this.getClass());

            cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
            cs.setString(2, fatturaAttiva.getCd_tipo_doc_amm());
            cs.setString(3, fatturaAttiva.getCd_cds());
            cs.setInt(4, fatturaAttiva.getEsercizio().intValue());
            cs.setString(5, fatturaAttiva.getCd_uo());
            cs.setLong(6, fatturaAttiva.getPg_doc_amm().longValue());
            cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

            cs.executeQuery();

        } catch (Throwable e) {
            throw handleException(fatturaAttiva, e);
        } finally {
            try {
                if (cs != null) cs.close();
            } catch (java.sql.SQLException e) {
                throw handleException(fatturaAttiva, e);
            }
        }
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta l'eliminazione di parametri per il processo di protocollazione iva
     * 'pg'
     * PostCondition:
     * I paramteri del processo 'pg' vengono eliminati
     * Fattura attiva specifica.
     * PreCondition:
     * Viene richiesta l'eliminazione di parametri per il processo di protocollazione iva
     * 'pg' in relazione al documento attivo specificato in 'fatturaAttiva'
     * PostCondition:
     * I paramteri del processo 'pg' relazionati a 'fatturaAttiva' vengono eliminati
     */

    public void cancellaDatiPerProtocollazioneIva(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            Long pg)
            throws ComponentException {

        try {
            //Non posso usare la cancellazione normale perchè in questo punto non ho
            //mai la chiave completa dell'oggetto --> devo costruire a mano l'SQL
            Vsx_rif_protocollo_ivaHome home = (Vsx_rif_protocollo_ivaHome) getHome(userContext, Vsx_rif_protocollo_ivaBulk.class);

            LoggableStatement ps = null;
            try {
                StringBuffer stm = new StringBuffer("DELETE FROM ");
                stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
                stm.append(home.getColumnMap().getTableName());
                stm.append(" WHERE (PG_CALL = ?");

                stm.append((fatturaAttiva != null) ?
                        " AND PG_FATTURA = ? AND ESERCIZIO = ? AND CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ?)" :
                        ")");
                ps = new LoggableStatement(getConnection(userContext), stm.toString(), true, this.getClass());

                if (fatturaAttiva != null) {
                    ps.setLong(1, pg.longValue());
                    ps.setLong(2, fatturaAttiva.getPg_fattura_attiva().longValue());
                    ps.setInt(3, fatturaAttiva.getEsercizio().intValue());
                    ps.setString(4, fatturaAttiva.getCd_cds());
                    ps.setString(5, fatturaAttiva.getCd_unita_organizzativa());
                } else
                    ps.setLong(1, pg.longValue());

                ps.execute();

            } catch (SQLException e) {
                throw new PersistencyException(e.getMessage());
            } finally {
                if (ps != null)
                    try {
                        ps.close();
                    } catch (java.sql.SQLException e) {
                    }
                ;
            }

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta l'eliminazione di parametri per il processo di stampa iva
     * 'pg'
     * PostCondition:
     * I paramteri del processo 'pg' vengono eliminati
     * Fattura attiva specifica.
     * PreCondition:
     * Viene richiesta l'eliminazione di parametri per il processo di stampa iva
     * 'pg' in relazione al documento attivo specificato in 'fatturaAttiva'
     * PostCondition:
     * I paramteri del processo 'pg' relazionati a 'fatturaAttiva' vengono eliminati
     */

    public void cancellaDatiPerStampaIva(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            Long pg)
            throws ComponentException {

        try {
            //Non posso usare la cancellazione normale perchè in questo punto non ho
            //mai la chiave completa dell'oggetto --> devo costruire a mano l'SQL
            V_stm_paramin_ft_attivaHome home = (V_stm_paramin_ft_attivaHome) getHome(userContext, V_stm_paramin_ft_attivaBulk.class);

            LoggableStatement ps = null;
            try {
                StringBuffer stm = new StringBuffer("DELETE FROM ");
                stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
                stm.append(home.getColumnMap().getTableName());
                stm.append(" WHERE (ID_REPORT = ?");

                stm.append((fatturaAttiva != null) ?
                        " AND PG_FATTURA_ATTIVA = ? AND ESERCIZIO = ? AND CD_CDS = ? AND CD_UO = ?)" :
                        ")");
                ps = new LoggableStatement(getConnection(userContext), stm.toString(), true, this.getClass());

                if (fatturaAttiva != null) {
                    ps.setLong(1, pg.longValue());
                    ps.setLong(2, fatturaAttiva.getPg_fattura_attiva().longValue());
                    ps.setInt(3, fatturaAttiva.getEsercizio().intValue());
                    ps.setString(4, fatturaAttiva.getCd_cds());
                    ps.setString(5, fatturaAttiva.getCd_unita_organizzativa());
                } else
                    ps.setLong(1, pg.longValue());

                ps.execute();

            } catch (SQLException e) {
                throw new PersistencyException(e.getMessage());
            } finally {
                if (ps != null)
                    try {
                        ps.close();
                    } catch (java.sql.SQLException e) {
                    }
                ;
            }

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }

    }

    private Accertamento_scadenzarioBulk caricaAccertamentoPer(
            UserContext context,
            Accertamento_scadenzarioBulk scadenza)
            throws ComponentException {

        if (scadenza != null) {
            try {
                it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession)
                        it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
                                it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession.class);
                AccertamentoBulk accertamento = (AccertamentoBulk) h.inizializzaBulkPerModifica(context, scadenza.getAccertamento());
                BulkList scadenze = accertamento.getAccertamento_scadenzarioColl();
                scadenza = (Accertamento_scadenzarioBulk) scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
            } catch (java.rmi.RemoteException e) {
                throw handleException(scadenza, e);
            } catch (javax.ejb.EJBException e) {
                throw handleException(scadenza, e);
            }
            return scadenza;
        }
        return null;
    }

    private java.util.List caricaAddebitiExceptFor(
            UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Nota_di_debito_attivaBulk notaDiDebito)
            throws it.cnr.jada.comp.ComponentException {

        Nota_di_debito_attiva_rigaHome home = (Nota_di_debito_attiva_rigaHome) getHomeCache(userContext).getHome(Nota_di_debito_attiva_rigaBulk.class);
        try {
            return home.findAddebitiForAccertamentoExceptFor(scadenza, notaDiDebito);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(notaDiDebito, e);
        }
    }

    private java.util.HashMap caricaAddebitiPer(
            UserContext userContext,
            java.util.List dettagliFattura)
            throws it.cnr.jada.comp.ComponentException {

        java.util.HashMap dettagliCaricati = new java.util.HashMap();
        if (dettagliFattura != null) {
            Nota_di_debito_attiva_rigaHome home = (Nota_di_debito_attiva_rigaHome) getHomeCache(userContext).getHome(Nota_di_debito_attiva_rigaBulk.class);
            for (Iterator i = dettagliFattura.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk rigaFattura = (Fattura_attiva_rigaIBulk) i.next();
                java.util.List righeNdD = home.findRigaFor(rigaFattura);
                if (righeNdD != null && !righeNdD.isEmpty()) {
                    for (Iterator it = righeNdD.iterator(); it.hasNext(); ) {
                        Nota_di_debito_attiva_rigaBulk rigaNdD = (Nota_di_debito_attiva_rigaBulk) it.next();
                        if (rigaNdD.getFattura_attiva().getFattura_attiva_dettColl() == null ||
                                rigaNdD.getFattura_attiva().getFattura_attiva_dettColl().isEmpty()) {
                            try {
                                rigaNdD.getFattura_attiva().setFattura_attiva_dettColl(new BulkList(findDettagli(userContext, rigaNdD.getFattura_attiva())));
                            } catch (Throwable t) {
                                throw handleException(t);
                            }
                        }
                    }
                    dettagliCaricati.put(rigaFattura, new Vector(righeNdD));
                }
            }
        }
        return dettagliCaricati;
    }

    private Accertamento_scadenzarioBulk caricaScadenzaAccertamentoPer(
            UserContext context,
            Accertamento_scadenzarioBulk scadenza)
            throws ComponentException {

        if (scadenza != null) {
            try {
                it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession)
                        it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
                                it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession.class);
                AccertamentoBulk accertamento = (AccertamentoBulk) h.inizializzaBulkPerModifica(context, scadenza.getAccertamento());
                BulkList scadenze = accertamento.getAccertamento_scadenzarioColl();
                scadenza = (Accertamento_scadenzarioBulk) scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
            } catch (java.rmi.RemoteException e) {
                throw handleException(scadenza, e);
            } catch (javax.ejb.EJBException e) {
                throw handleException(scadenza, e);
            }
            return scadenza;
        }
        return null;
    }

    private Obbligazione_scadenzarioBulk caricaScadenzaObbligazionePer(
            UserContext context,
            Obbligazione_scadenzarioBulk scadenza)
            throws ComponentException {

        if (scadenza != null) {
            try {
                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)
                        it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
                                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class);
                ObbligazioneBulk obbligazione = (ObbligazioneBulk) h.inizializzaBulkPerModifica(context, scadenza.getObbligazione());
                BulkList scadenze = obbligazione.getObbligazione_scadenzarioColl();
                scadenza = (Obbligazione_scadenzarioBulk) scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
            } catch (java.rmi.RemoteException e) {
                throw handleException(scadenza, e);
            } catch (javax.ejb.EJBException e) {
                throw handleException(scadenza, e);
            }
            return scadenza;
        }
        return null;
    }

    private java.util.List caricaStorniExceptFor(
            UserContext userContext,
            Accertamento_scadenzarioBulk scadenza,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws it.cnr.jada.comp.ComponentException {

        Nota_di_credito_attiva_rigaHome home = (Nota_di_credito_attiva_rigaHome) getHomeCache(userContext).getHome(Nota_di_credito_attiva_rigaBulk.class);
        try {
            return home.findStorniForAccertamentoExceptFor(scadenza, notaDiCredito);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(notaDiCredito, e);
        }
    }
//^^@@

    private java.util.HashMap caricaStorniPer(
            UserContext userContext,
            java.util.List dettagliFattura)
            throws it.cnr.jada.comp.ComponentException {

        java.util.HashMap dettagliCaricati = new java.util.HashMap();
        if (dettagliFattura != null) {
            Nota_di_credito_attiva_rigaHome home = (Nota_di_credito_attiva_rigaHome) getHomeCache(userContext).getHome(Nota_di_credito_attiva_rigaBulk.class);
            for (Iterator i = dettagliFattura.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk rigaFattura = (Fattura_attiva_rigaIBulk) i.next();
                java.util.List righeNdC = home.findRigaFor(rigaFattura);
                if (righeNdC != null && !righeNdC.isEmpty()) {
                    for (Iterator it = righeNdC.iterator(); it.hasNext(); ) {
                        Nota_di_credito_attiva_rigaBulk rigaNdC = (Nota_di_credito_attiva_rigaBulk) it.next();
                        if (rigaNdC.getFattura_attiva().getFattura_attiva_dettColl() == null ||
                                rigaNdC.getFattura_attiva().getFattura_attiva_dettColl().isEmpty()) {
                            try {
                                rigaNdC.getFattura_attiva().setFattura_attiva_dettColl(new BulkList(findDettagli(userContext, rigaNdC.getFattura_attiva())));
                            } catch (Throwable t) {
                                throw handleException(t);
                            }
                        }
                    }
                    dettagliCaricati.put(rigaFattura, new Vector(righeNdC));
                }
            }
        }
        return dettagliCaricati;
    }
//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle scadenze di Accertamenti congruenti con la fattura che si sta creando/modificando.
     * PostCondition:
     * Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
     * Validazione lista delle Accertamenti per le fatture
     * PreCondition:
     * Si è verificato un errore nel caricamento delle scadenze delle Accertamenti.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Accertamento definitiva
     * PreCondition:
     * La scadenza non appartiene ad un'Accertamento definitiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Accertamenti non cancellate
     * PreCondition:
     * La scadenza appartiene ad un'Accertamento cancellata
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Accertamenti associate ad altri documenti amministrativi
     * PreCondition:
     * La scadenza appartiene ad un'Accertamento associata ad altri documenti amministrativi
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Accertamenti della stessa UO
     * PreCondition:
     * La scadenza dell'Accertamento non appartiene alla stessa UO di generazione fattura
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitatazione filtro di selezione sul debitore dell'Accertamento
     * PreCondition:
     * La scadenza dell'Accertamento ha un debitore diverso da quello della fattura
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Disabilitazione filtro di selezione sul debitore dell'Accertamento
     * PreCondition:
     * La scadenza dell'Accertamento ha un debitore diverso da quello della fattura  e non è di tipo "diversi"
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro di selezione sulla data di scadenza
     * PreCondition:
     * La scadenza dell'Accertamento ha una data scadenza precedente alla data di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro importo scadenza
     * PreCondition:
     * La scadenza dell'Accertamento ha un importo di scadenza inferiore a quella di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro sul progressivo dell'Accertamento
     * PreCondition:
     * La scadenza dell'Accertamento non ha progressivo specificato
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Associazione di una scadenza a titolo capitolo dei beni servizio inventariabili da contabilizzare
     * PreCondition:
     * L'Accertamento non ha titolo capitolo dei beni servizio inventariabili da contabilizzare
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     */
//^^@@
    public RemoteIterator cercaAccertamenti(UserContext context, Filtro_ricerca_accertamentiVBulk filtro)
            throws ComponentException {

        Accertamento_scadenzarioHome home = (Accertamento_scadenzarioHome) getHome(context, Accertamento_scadenzarioBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.setDistinctClause(true);
        sql.addTableToHeader("ACCERTAMENTO");
        sql.addSQLJoin("ACCERTAMENTO_SCADENZARIO.CD_CDS", "ACCERTAMENTO.CD_CDS");
        sql.addSQLJoin("ACCERTAMENTO_SCADENZARIO.ESERCIZIO", "ACCERTAMENTO.ESERCIZIO");
        sql.addSQLJoin("ACCERTAMENTO_SCADENZARIO.ESERCIZIO_ORIGINALE", "ACCERTAMENTO.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("ACCERTAMENTO_SCADENZARIO.PG_ACCERTAMENTO", "ACCERTAMENTO.PG_ACCERTAMENTO");

        sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.IM_SCADENZA", sql.NOT_EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = 0.00 OR ACCERTAMENTO_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = 0.00 OR ACCERTAMENTO_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");

        sql.addSQLClause("AND", "ACCERTAMENTO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, filtro.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "ACCERTAMENTO.CD_UO_ORIGINE", sql.EQUALS, filtro.getCd_uo_origine());
        sql.addSQLClause("AND", "ACCERTAMENTO.RIPORTATO", sql.EQUALS, "N");
        sql.addSQLClause("AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNULL, null);

        sql.addSQLClause("AND", "ACCERTAMENTO.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
        sql.addSQLClause("AND", "ACCERTAMENTO.ESERCIZIO_COMPETENZA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));

        if (filtro.hasDocumentoCompetenzaCOGEInAnnoPrecedente()) {
            sql.addSQLClause("AND", "ACCERTAMENTO.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
            sql.addSQLClause("AND", "ACCERTAMENTO.FL_PGIRO", sql.EQUALS, "N");
        } else {
            sql.addSQLClause("AND", "ACCERTAMENTO.CD_TIPO_DOCUMENTO_CONT", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_SIST);
            sql.addSQLClause("AND", "ACCERTAMENTO.CD_TIPO_DOCUMENTO_CONT", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_PLUR);
//		sql.addSQLClause("AND","ACCERTAMENTO.CD_TIPO_DOCUMENTO_CONT", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
            if (!filtro.hasDocumentoCompetenzaCOGESoloInAnnoCorrente())
                sql.addSQLClause("AND", "ACCERTAMENTO.FL_PGIRO", sql.EQUALS, "N");
        }

        //sql.addTableToHeader("TERZO");
        if (filtro.getFl_data_scadenziario().booleanValue() && filtro.getData_scadenziario() != null)
            sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.DT_SCADENZA_INCASSO", sql.EQUALS, filtro.getData_scadenziario());
        if (filtro.getFl_importo().booleanValue() && filtro.getIm_importo() != null)
            sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.IM_SCADENZA", sql.GREATER_EQUALS, filtro.getIm_importo());
        else
            sql.addSQLClause("AND", "ACCERTAMENTO_SCADENZARIO.IM_SCADENZA > 0.00 ");
        if (filtro.getFl_nr_accertamento().booleanValue() && filtro.getEsercizio_ori_accertamento() != null)
            sql.addSQLClause("AND", "ACCERTAMENTO.ESERCIZIO_ORIGINALE", sql.EQUALS, filtro.getEsercizio_ori_accertamento());
        if (filtro.getFl_nr_accertamento().booleanValue() && filtro.getNr_accertamento() != null)
            sql.addSQLClause("AND", "ACCERTAMENTO.PG_ACCERTAMENTO", sql.EQUALS, filtro.getNr_accertamento());

        //sql.addTableToHeader("ANAGRAFICO");
        //sql.addSQLJoin("ACCERTAMENTO.CD_TERZO", "TERZO.CD_TERZO");
        //sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");

        if (!filtro.getFl_cliente().booleanValue()) {
            sql.addTableToHeader("TERZO");
            sql.addTableToHeader("ANAGRAFICO");
            sql.addSQLJoin("ACCERTAMENTO.CD_TERZO", "TERZO.CD_TERZO");
            sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "(ACCERTAMENTO.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
            sql.addParameter(filtro.getCliente().getCd_terzo(), java.sql.Types.INTEGER, 0);
            sql.addParameter(AnagraficoBulk.DIVERSI, java.sql.Types.VARCHAR, 0);
        } else {
            sql.addSQLClause("AND", "ACCERTAMENTO.CD_TERZO", sql.EQUALS, filtro.getCliente().getCd_terzo());
        }

        //if (filtro.getFl_cliente().booleanValue()) {
        //sql.openParenthesis("AND");
        //sql.addSQLClause("AND","ACCERTAMENTO.CD_TERZO",sql.EQUALS, filtro.getCliente().getCd_terzo());
        //sql.openParenthesis("OR");
        //sql.addSQLClause("OR","ANAGRAFICO.TI_ENTITA",sql.EQUALS, AnagraficoBulk.DIVERSI);
        //sql.addSQLClause("AND","ACCERTAMENTO.CD_CDS_ORIGINE",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context));
        //sql.closeParenthesis();
        //sql.closeParenthesis();
        //}
        //else{
        //sql.addSQLClause("AND","ANAGRAFICO.TI_ENTITA",sql.EQUALS, AnagraficoBulk.DIVERSI);
        //sql.addSQLClause("AND","ACCERTAMENTO.CD_CDS_ORIGINE",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context));
        //}
        return iterator(
                context,
                sql,
                Accertamento_scadenzarioBulk.class,
                "default");
    }
//^^@@

    /**
     * Non esiste la valuta o il periodo di cambio di riferimento.
     * PreCondition:
     * La valuta di riferimento o il relativo cambio non sono presenti.
     * PostCondition:
     * Annullata la scelta della valuta.
     */
//^^@@
    public Fattura_attivaBulk cercaCambio(it.cnr.jada.UserContext uc, Fattura_attivaBulk fattura)
            throws ComponentException {

        it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = fattura.getValuta();
        if (valuta == null) {
            return resetChangeData(uc, fattura);
        }

        return fattura;
    }
//^^@@

    /**
     * dettagli della fattura selezionata per l'inserimento di dettagli nelle note di credito.
     * PreCondition:
     * Viene richiesta la lista dei dettagli della fattura per l'inserimento di dettagli nelle note di credito.
     * PostCondition:
     * Viene restituita la lista dei dettagli della fattura selezionata per l'inserimento di dettagli nelle note di credito.
     */
//^^@@
    public RemoteIterator cercaDettagliFatturaPerNdC(UserContext context, Fattura_attiva_IBulk fatturaAttiva)
            throws ComponentException {

        String statoR = getStatoRiporto(context, fatturaAttiva);
        String statoRipInScrivania = getStatoRiportoInScrivania(context, fatturaAttiva);

        if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).equals(fatturaAttiva.getEsercizio())) {
            if (!fatturaAttiva.NON_RIPORTATO.equals(statoR))
                throw new it.cnr.jada.comp.ApplicationException("La fattura selezionata è stata riportata in altro esercizio! Operazione annullata.");
        }
        // Rospuc 11/05/2017
//	else {
//		if (!fatturaAttiva.COMPLETAMENTE_RIPORTATO.equals(statoRipInScrivania))
//			throw new it.cnr.jada.comp.ApplicationException("La fattura selezionata o è stata riportata parzialmente o non è stata riportata nell'esercizio corrente! Operazione annullata.");
//	}

        Fattura_attiva_rigaIHome home = (Fattura_attiva_rigaIHome) getHome(context, Fattura_attiva_rigaIBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "pg_fattura_attiva", sql.EQUALS, fatturaAttiva.getPg_fattura_attiva());
        sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaAttiva.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, fatturaAttiva.getEsercizio());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fatturaAttiva.getCd_unita_organizzativa());
        sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_ANNULLATO);
        //Escludo le riportate
        //sql.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.EQUALS, fatturaPassiva.getEsercizio());

        try {
            return iterator(
                    context,
                    sql,
                    Fattura_attiva_rigaIBulk.class,
                    "default");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
//^^@@

    /**
     * dettagli della fattura selezionata per l'inserimento di dettagli nelle note di debito.
     * PreCondition:
     * Viene richiesta la lista dei dettagli della fattura per l'inserimento di dettagli nelle note di debito.
     * PostCondition:
     * Viene restituita la lista dei dettagli della fattura selezionata per l'inserimento di dettagli nelle note di debito.
     */
//^^@@
    public RemoteIterator cercaDettagliFatturaPerNdD(UserContext context, Fattura_attiva_IBulk fatturaAttiva)
            throws ComponentException {

        return cercaDettagliFatturaPerNdC(context, fatturaAttiva);
    }
//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle fatture congruenti con la nota di credito che si sta creando/modificando.
     * PostCondition:
     * La fattura viene aggiunta alla lista delle fatture congruenti.
     * Validazione lista delle fatture per le note di credito
     * PreCondition:
     * Si è verificato un errore nel caricamento delle fatture.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Fornitore nota di credito = fornitore fattura
     * PreCondition:
     * Il fornitore della fattura non è lo stesso di quello della nota di credito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * CDS di appartenenza
     * PreCondition:
     * La fattura non appartiene al CDS di creazione della nota di credito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * Esercizio di appartenenza
     * PreCondition:
     * L'esercizio della fattura non è lo stesso di quello della nota di credito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * Unità organizzativa di appartenenza
     * PreCondition:
     * La UO della fattura non è la stessa di quella della nota di credito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     */
//^^@@
    public RemoteIterator cercaFatturaPerNdC(UserContext context, Nota_di_credito_attivaBulk notaDiCredito)
            throws ComponentException {

        Fattura_attiva_IHome home = (Fattura_attiva_IHome) getHome(context, Fattura_attiva_IBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        //sql.addClause("AND","stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_PAGATO);
        sql.addClause("AND", "cd_terzo", sql.EQUALS, notaDiCredito.getCliente().getCd_terzo());
        sql.addClause("AND", "cd_cds_origine", sql.EQUALS, notaDiCredito.getCd_cds_origine());
        sql.addClause("AND", "esercizio", sql.LESS_EQUALS, notaDiCredito.getEsercizio());
        sql.addClause("AND", "cd_uo_origine", sql.EQUALS, notaDiCredito.getCd_uo_origine());
        sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_ANNULLATO);
        sql.addClause("AND", "fl_congelata", sql.EQUALS, Boolean.FALSE);
        sql.addClause("AND", "ti_causale_emissione", sql.EQUALS, notaDiCredito.getTi_causale_emissione());
        sql.addOrderBy("ESERCIZIO DESC");

        try {
            return iterator(
                    context,
                    sql,
                    Fattura_attiva_IBulk.class,
                    "default");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle fatture congruenti con la nota di debito che si sta creando/modificando.
     * PostCondition:
     * La fattura viene aggiunta alla lista delle fatture congruenti.
     * Validazione lista delle fatture per le note di debito
     * PreCondition:
     * Si è verificato un errore nel caricamento delle fatture.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Fornitore nota di debito = fornitore fattura
     * PreCondition:
     * Il fornitore della fattura non è lo stesso di quello della nota di debito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * CDS di appartenenza
     * PreCondition:
     * La fattura non appartiene al CDS di creazione della nota di debito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * Esercizio di appartenenza
     * PreCondition:
     * L'esercizio della fattura non è lo stesso di quello della nota di debito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     * Unità organizzativa di appartenenza
     * PreCondition:
     * La UO della fattura non è la stessa di quella della nota di debito
     * PostCondition:
     * La fattura non viene aggiunta alla lista delle fatture congruenti.
     */
//^^@@
    public RemoteIterator cercaFatturaPerNdD(UserContext context, Nota_di_debito_attivaBulk notaDiDebito)
            throws ComponentException {

        Fattura_attiva_IHome home = (Fattura_attiva_IHome) getHome(context, Fattura_attiva_IBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        //sql.addClause("AND","stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_PAGATO);
        sql.addClause("AND", "cd_terzo", sql.EQUALS, notaDiDebito.getCliente().getCd_terzo());
        sql.addClause("AND", "cd_cds_origine", sql.EQUALS, notaDiDebito.getCd_cds_origine());
        sql.addClause("AND", "esercizio", sql.LESS_EQUALS, notaDiDebito.getEsercizio());
        sql.addClause("AND", "cd_uo_origine", sql.EQUALS, notaDiDebito.getCd_uo_origine());
        sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_ANNULLATO);
        sql.addClause("AND", "fl_congelata", sql.EQUALS, Boolean.FALSE);
        sql.addClause("AND", "ti_causale_emissione", sql.EQUALS, notaDiDebito.getTi_causale_emissione());
        sql.addOrderBy("ESERCIZIO DESC");

        try {
            return iterator(
                    context,
                    sql,
                    Fattura_attiva_IBulk.class,
                    "default");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
//^^@@

    /**
     * Fornisce i dati aggiuntivi del cliente selezionato
     * PreCondition:
     * Viene selezionato o cambiato il cliente
     * PostCondition:
     * Fornisce i dati aggiuntivi del cliente selezionato
     */
//^^@@
    public Nota_di_credito_attivaBulk completaCliente(
            it.cnr.jada.UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws it.cnr.jada.comp.ComponentException {

        TerzoBulk cliente = notaDiCredito.getCliente();
        if (cliente != null) {
            it.cnr.contab.anagraf00.core.bulk.TerzoHome home = (it.cnr.contab.anagraf00.core.bulk.TerzoHome) getHome(userContext, cliente);
            try {
                notaDiCredito.setTermini(findTermini(userContext, notaDiCredito));
                notaDiCredito.setModalita(findModalita(userContext, notaDiCredito));
                notaDiCredito.setTermini_pagamento(null);
                notaDiCredito.setModalita_pagamento(null);
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(e);
            } catch (it.cnr.jada.persistency.IntrospectionException e) {
                throw handleException(e);
            }
        }

        return notaDiCredito;
    }
//^^@@

    /**
     * tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * vengono trasmessi i dati relativi al terzo.
     * Validazione terzo.
     * PreCondition:
     * Una condizione di errore è stata rilevata.
     * PostCondition:
     * Viene trasmesso un messaggio di errore :"Attenzione, la selezione del Terzo non è corretta".
     */
//^^@@
    public Fattura_attivaBulk completaProtocolloSuRighe(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {


        BulkList dettaglio = fatturaAttiva.getFattura_attiva_dettColl();
        if (dettaglio == null) return fatturaAttiva;

        try {
            for (Iterator i = dettaglio.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                riga.setPg_fattura_attiva(fatturaAttiva.getPg_fattura_attiva());
            }
        } catch (Throwable t) {
            throw handleException(fatturaAttiva, t);
        }

        return fatturaAttiva;
    }

    /**
     * Fornisce i dati aggiuntivi del cliente selezionato
     * PreCondition:
     * Viene selezionato o cambiato il cliente
     * PostCondition:
     * Fornisce i dati aggiuntivi del cliente selezionato
     */
//^^@@
    public Fattura_attivaBulk completaTerzo(UserContext aUC, Fattura_attivaBulk fatturaAttiva, TerzoBulk terzo) throws ComponentException {
        try {
            if (fatturaAttiva != null) {
                TerzoBulk oldTerzo = fatturaAttiva.getCliente();
                fatturaAttiva.setCliente(terzo);
                fatturaAttiva.setNome(terzo.getAnagrafico().getNome());
                fatturaAttiva.setCognome(terzo.getAnagrafico().getCognome());
                fatturaAttiva.setRagione_sociale(terzo.getAnagrafico().getRagione_sociale());
                fatturaAttiva.setCodice_fiscale(terzo.getAnagrafico().getCodice_fiscale());
                AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(aUC, AnagraficoBulk.class);
                if (!terzo.getAnagrafico().isGruppoIVA()){
                    AnagraficoBulk ana = anagraficoHome.findGruppoIva(terzo.getAnagrafico(), fatturaAttiva.getDt_registrazione());
                    if (ana != null){
                        fatturaAttiva.setPartita_iva(ana.getPartita_iva());
                    } else {
                        fatturaAttiva.setPartita_iva(terzo.getAnagrafico().getPartita_iva());
                    }
                } else {
                    fatturaAttiva.setPartita_iva(terzo.getAnagrafico().getPartita_iva());
                }
                fatturaAttiva.getCliente().getAnagrafico().setDichiarazioni_intento(new BulkList(anagraficoHome.findDichiarazioni_intentoValide(terzo.getAnagrafico())));

                impostaDatiPerFatturazioneElettronica(aUC, fatturaAttiva, terzo);
                //ricontabilizzazione COGE
                if (oldTerzo != null && terzo != null && !oldTerzo.equalsByPrimaryKey(terzo) && fatturaAttiva.REGISTRATO_IN_COGE.equalsIgnoreCase(fatturaAttiva.getStato_coge()))
                    fatturaAttiva.setStato_coge(fatturaAttiva.DA_RIREGISTRARE_IN_COGE);
                //if (oldTerzo != null && terzo != null && !oldTerzo.equalsByPrimaryKey(terzo) && fatturaAttiva.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(fatturaAttiva.getStato_coan()))
                //fatturaAttiva.setStato_coan(fatturaAttiva.DA_RICONTABILIZZARE_IN_COAN);
                AnagraficoComponentSession sess = (AnagraficoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
                if (fatturaAttiva.getFl_intra_ue() != null && fatturaAttiva.getFl_intra_ue().booleanValue() &&
                        terzo.getAnagrafico().getPartita_iva() != null &&
                        (terzo.getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.CEE) == 0) &&
                        !sess.verificaStrutturaPiva(aUC, terzo.getAnagrafico()))
                    throw new it.cnr.jada.comp.ApplicationException("Verificare la partita Iva del cliente non corrisponde al modello della sua nazionalità.");
            }
        } catch (ApplicationException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        } catch (Throwable t) {

            throw new it.cnr.jada.comp.ApplicationException("Attenzione, la selezione del Terzo non è corretta");
        }
        return fatturaAttiva;
    }

    private void impostaDatiPerFatturazioneElettronica(UserContext aUC,
                                                       Fattura_attivaBulk fatturaAttiva, TerzoBulk terzo)
            throws ComponentException {
        if (isAttivaFatturazioneElettronica(aUC, terzo.getAnagrafico(), fatturaAttiva.getDt_registrazione())) {
        	try {
				terzo.setPec(new BulkList(((TerzoHome) getHome(aUC, TerzoBulk.class)).findTelefoni(terzo, TelefonoBulk.PEC)));
	        	terzo.setEmail(new BulkList(((TerzoHome) getHome(aUC, TerzoBulk.class)).findTelefoni(terzo, TelefonoBulk.EMAIL)));
			} catch (IntrospectionException | PersistencyException e) {
				throw new ComponentException(e);
			}
            controlloCodiceIpaValorizzato(terzo);
            fatturaAttiva.setCodiceUnivocoUfficioIpa(terzo.getCodiceUnivocoUfficioIpa());
            fatturaAttiva.setCodiceDestinatarioFatt(terzo.getCodiceDestinatarioFatt());
            fatturaAttiva.setPecFatturaElettronica(terzo.getPecFatturazioneElettronica() == null ? null : terzo.getPecFatturazioneElettronica().getRiferimento());
            fatturaAttiva.setMailFatturaElettronica(terzo.getEmailFatturazioneElettronica() == null ? null : terzo.getEmailFatturazioneElettronica().getRiferimento());
            fatturaAttiva.setFlFatturaElettronica(true);
        } else {
            fatturaAttiva.setCodiceDestinatarioFatt(null);
            fatturaAttiva.setPecFatturaElettronica(null);
            fatturaAttiva.setMailFatturaElettronica(null);
            fatturaAttiva.setCodiceUnivocoUfficioIpa(null);
            fatturaAttiva.setFlFatturaElettronica(false);
        }
    }

    public Boolean isAttivaFatturazioneElettronica(UserContext aUC, AnagraficoBulk anagraficoBulk, Date dataFattura) throws ComponentException {
        Date dataInizio;
        try {
            dataInizio = Utility.createConfigurazioneCnrComponentSession().getDt01(aUC, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC), null, Configurazione_cnrBulk.PK_FATTURAZIONE_ELETTRONICA, Configurazione_cnrBulk.SK_ATTIVA);
        } catch (ComponentException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        } catch (RemoteException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        } catch (EJBException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        }
        if (dataFattura == null || dataInizio == null || dataFattura.before(dataInizio) || anagraficoBulk.getDataAvvioFattElettr() == null || dataFattura.before(anagraficoBulk.getDataAvvioFattElettr())) {
            return false;
        }
        return true;
    }

    public BigDecimal getImportoBolloVirtuale(UserContext aUC, Fattura_attivaBulk fatt) throws ComponentException {
        Tipo_atto_bolloBulk tipoAtto = getTipoAttoBollo(aUC, fatt);
        if (tipoAtto != null) {
            return tipoAtto.getImBollo();
        }
        return null;
    }

    public Tipo_atto_bolloBulk getTipoAttoBollo(UserContext aUC, Fattura_attivaBulk fatt) throws ComponentException {
        try {
            Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(aUC, fatt.getEsercizio(), null, Configurazione_cnrBulk.PK_BOLLO_VIRTUALE, Configurazione_cnrBulk.SK_BOLLO_VIRTUALE_CODICE_FATTURA_ATTIVA);
             
            return Optional.ofNullable(config)
                    .map(el -> fatt.isDocumentoFatturazioneElettronica() ? el.getVal02() : el.getVal01())
                    .map(v -> {
                        Tipo_atto_bolloBulk tipoAtto;
                        try {
                            tipoAtto = Utility.createTipoAttoBolloComponentSession().getTipoAttoBollo(aUC, fatt.getDt_registrazione(), v);
                            return Optional.ofNullable(tipoAtto).orElse(null);
                        } catch (Exception e) {
                            throw new DetailedRuntimeException(e);
                        }
                    })
                    .orElse(null);
        } catch (ComponentException | RemoteException | EJBException | DetailedRuntimeException e) {
            throw handleException(e);
        }
    }


    /**
     * Validazione riga.
     * PreCondition:
     * E' stata richiesta la contabilizzazione dei dettagli di fattura selezionati ma almeno un dettaglio
     * non supera i controlli del metodo 'validaRiga'.
     * PostCondition:
     * Obbligo di modifica o annullamento riga.
     * Tutti i controlli superati.
     * PreCondition:
     * E' stata richiesta la contabilizzazione dei dettagli di fattura selezionati. Ogni dettaglio
     * supera i controlli del metodo 'validaRiga'.
     * PostCondition:
     * Consente il passaggio alla riga seguente.
     */
//^^@@
    public Fattura_attivaBulk contabilizzaDettagliSelezionati(
            UserContext context,
            Fattura_attivaBulk fatturaAttiva,
            java.util.Collection dettagliSelezionati,
            it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamentoSelezionato)
            throws ComponentException {

        if (accertamentoSelezionato != null && dettagliSelezionati != null) {
            if (!dettagliSelezionati.isEmpty()) {
                for (java.util.Iterator i = dettagliSelezionati.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk rigaSelected = (Fattura_attiva_rigaBulk) i.next();
                    validaScadenze(fatturaAttiva, accertamentoSelezionato);
                    rigaSelected.setAccertamento_scadenzario(accertamentoSelezionato);
                    impostaCollegamentoCapitoloPerTrovato(context, rigaSelected);
                    rigaSelected.setStato_cofi(rigaSelected.STATO_CONTABILIZZATO);
                    rigaSelected.setToBeUpdated();
                    fatturaAttiva.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, rigaSelected);
                }
                fatturaAttiva.addToFattura_attiva_ass_totaliMap(accertamentoSelezionato, calcolaTotalePer((Vector) fatturaAttiva.getFattura_attiva_accertamentiHash().get(accertamentoSelezionato), false));

                if (fatturaAttiva.getStato_cofi() != fatturaAttiva.STATO_PAGATO)
                    fatturaAttiva.setStato_cofi((fatturaAttiva.getFattura_attiva_accertamentiHash().isEmpty()) ?
                            fatturaAttiva.STATO_INIZIALE :
                            fatturaAttiva.STATO_CONTABILIZZATO);
            } else {
                fatturaAttiva.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, null);
                fatturaAttiva.setAndVerifyStatus();
            }
            try {
                AccertamentoAbstractComponentSession session = (AccertamentoAbstractComponentSession) EJBCommonServices.createEJB(
                        "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
                        AccertamentoAbstractComponentSession.class);
                session.lockScadenza(context, accertamentoSelezionato);
            } catch (Throwable t) {
                throw handleException(fatturaAttiva, t);
            }
        }
        return fatturaAttiva;
    }

    private void validaScadenze(Fattura_attivaBulk doc, Accertamento_scadenzarioBulk newScad) throws ComponentException {
        Iterator it;

        Vector scadCanc = doc.getDocumentiContabiliCancellati();
        if (scadCanc != null) {
            it = scadCanc.iterator();

            while (it.hasNext()) {
                Accertamento_scadenzarioBulk scad = (Accertamento_scadenzarioBulk) it.next();
                if (scad.getAccertamento() instanceof AccertamentoResiduoBulk) {
                    if (scad.getAccertamento().equalsByPrimaryKey(newScad.getAccertamento()) && ((AccertamentoResiduoBulk) scad.getAccertamento()).getAccertamento_modifica() != null
                            && ((AccertamentoResiduoBulk) scad.getAccertamento()).getAccertamento_modifica().getPg_modifica() != null) {
                        throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'accertamento residuo " + scad.getPg_accertamento() + " poichè è stata effettuata una modifica in questo documento amministrativo!");
                    }
                }
            }
        }

        AccertamentiTable accertamentiHash = doc.getAccertamentiHash();
        if (accertamentiHash != null && !accertamentiHash.isEmpty()) {

            for (java.util.Enumeration e = accertamentiHash.keys(); e.hasMoreElements(); ) {
                Accertamento_scadenzarioBulk scad = (Accertamento_scadenzarioBulk) e.nextElement();
                if (scad.getAccertamento() instanceof AccertamentoResiduoBulk) {
                    if (scad.getAccertamento().equalsByPrimaryKey(newScad.getAccertamento()) && ((AccertamentoResiduoBulk) scad.getAccertamento()).getAccertamento_modifica() != null
                            && ((AccertamentoResiduoBulk) scad.getAccertamento()).getAccertamento_modifica().getPg_modifica() != null) {
                        throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'accertamento residuo " + scad.getPg_accertamento() + " poichè è stata effettuata una modifica in questo documento amministrativo!");
                    }
                }
            }
        }
    }

    public void controllaCompetenzaCOGEDettagli(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva != null && !fatturaAttiva.isAnnullato()) {
            AccertamentiTable accertamentiHash = fatturaAttiva.getFattura_attiva_accertamentiHash();
            if (accertamentiHash != null) {
                for (java.util.Enumeration e = accertamentiHash.keys(); e.hasMoreElements(); ) {
                    Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) e.nextElement();
                    Iterator righeCollegate = ((java.util.List) accertamentiHash.get(scadenza)).iterator();
                    if (righeCollegate != null && righeCollegate.hasNext()) {
                        Fattura_attiva_rigaBulk primaRiga = (Fattura_attiva_rigaBulk) righeCollegate.next();
                        java.util.Calendar dtCompetenzaDa = fatturaAttiva.getDateCalendar(primaRiga.getDt_da_competenza_coge());
                        java.util.Calendar dtCompetenzaA = fatturaAttiva.getDateCalendar(primaRiga.getDt_a_competenza_coge());

                        while (righeCollegate.hasNext()) {
                            Fattura_attiva_rigaBulk rigaSuccessiva = (Fattura_attiva_rigaBulk) righeCollegate.next();
                            java.util.Calendar dtCompetenzaDaSuccessiva = fatturaAttiva.getDateCalendar(rigaSuccessiva.getDt_da_competenza_coge());
                            java.util.Calendar dtCompetenzaASuccessiva = fatturaAttiva.getDateCalendar(rigaSuccessiva.getDt_a_competenza_coge());
                            if (!dtCompetenzaDa.equals(dtCompetenzaDaSuccessiva) ||
                                    !dtCompetenzaA.equals(dtCompetenzaASuccessiva))
                                throw new it.cnr.jada.comp.ApplicationException("I dettagli del documento collegati alla scadenza \"" + scadenza.getDs_scadenza() + "\"\nnon hanno lo stesso periodo di competenza! Impossibile salvare.");
                        }
                    }
                }
            }
        }
    }
//^^@@

    private void controllaContabilizzazioneDiTutteLeRighe(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        int numeroDiRigheNonContabilizzate = 0;
        Fattura_attiva_rigaBulk riga = null;
        for (java.util.Iterator i = fattura_attiva.getFattura_attiva_dettColl().iterator();
             i.hasNext();
                ) {
            riga = (Fattura_attiva_rigaBulk) i.next();
            if (Fattura_attiva_rigaBulk.STATO_INIZIALE.equals(riga.getStato_cofi()))
                numeroDiRigheNonContabilizzate++;
        }

        if (numeroDiRigheNonContabilizzate == 1)
            if (riga.getDs_riga_fattura() != null)
                throw new it.cnr.jada.comp.ApplicationException(
                        "Il dettaglio \"" + riga.getDs_riga_fattura() + "\" NON è stato contabilizzato!");
            else
                throw new it.cnr.jada.comp.ApplicationException(
                        "Un dettaglio NON è stato contabilizzato!");
        if (numeroDiRigheNonContabilizzate > 1)
            if (riga.getDs_riga_fattura() != null)
                throw new it.cnr.jada.comp.ApplicationException(
                        numeroDiRigheNonContabilizzate
                                + "-dettagli non contabilizzati. Il primo e' : Dettaglio "
                                + riga.getDs_riga_fattura()
                                + ","
                                + riga.getIm_imponibile()
                                + " non contabilizzato");
            else
                throw new it.cnr.jada.comp.ApplicationException(
                        numeroDiRigheNonContabilizzate + "-dettagli non contabilizzati.");

    }
//^^@@

    /**
     * Quadratura delle scadenze Accertamenti di fattura non estera o estera senza lettera di pagamento.
     * PreCondition:
     * La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleAccertamentoPer') insistenti sull'elenco di dettagli associati
     * alla scadenza Accertamento è uguale all'importo della scadenza Accertamento stessa
     * PostCondition:
     * Permette la continuazione.
     * Controlli non superati.
     * PreCondition:
     * Non vengono superate tutte le validazioni
     * PostCondition:
     * messaggio di errore.
     */
//^^@@
    public void controllaQuadraturaAccertamenti(UserContext aUC, Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva != null && !fatturaAttiva.isAnnullato() && !fatturaAttiva.isCongelata()) {
            AccertamentiTable accertamentiHash = fatturaAttiva.getFattura_attiva_accertamentiHash();
            if (accertamentiHash != null) {
                for (java.util.Enumeration e = accertamentiHash.keys(); e.hasMoreElements(); ) {
                    Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) e.nextElement();
                    java.math.BigDecimal totale = calcolaTotaleAccertamentoPer(aUC, scadenza, fatturaAttiva).abs();
                    java.math.BigDecimal delta = scadenza.getIm_scadenza().subtract(totale);
                    if (delta.compareTo(new java.math.BigDecimal(0)) > 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Attenzione: La scadenza ");
                        sb.append(scadenza.getDs_scadenza());
                        sb.


                                append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
                        sb.append(" è stata coperta solo per ");
                        sb.append(totale.doubleValue() + " EUR!");
                        throw new it.cnr.jada.comp.ApplicationException(sb.toString());
                    } else if (delta.compareTo(new java.math.BigDecimal(0)) < 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Attenzione: La scadenza ");
                        sb.append(scadenza.getDs_scadenza());
                        sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
                        sb.append(" è scoperta per ");
                        sb.append(delta.abs().doubleValue() + " EUR!");
                        throw new it.cnr.jada.comp.ApplicationException(sb.toString());
                    }
                }
            }
        }
    }
//^^@@

    /**
     * Quadratura delle scadenze obbligazioni di fattura.
     * PreCondition:
     * La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleObbligazionePer') insistenti sull'elenco di dettagli associati
     * alla scadenza obbligazione è uguale all'importo della scadenza obbligazione stessa
     * PostCondition:
     * Permette la continuazione.
     * PreCondition:
     * Non vengono superate tutte le validazioni
     * PostCondition:
     * messaggio di errore.
     */
//^^@@
    public void controllaQuadraturaObbligazioni(UserContext aUC, Nota_di_credito_attivaBulk notaDiCredito)
            throws ComponentException {

        if (notaDiCredito != null && !notaDiCredito.isAnnullato() && !notaDiCredito.isCongelata()) {
            ObbligazioniTable obbligazioniHash = notaDiCredito.getObbligazioniHash();
            if (obbligazioniHash != null) {
                for (java.util.Enumeration e = obbligazioniHash.keys(); e.hasMoreElements(); ) {
                    Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) e.nextElement();
                    java.math.BigDecimal totale = calcolaTotaleScadenzaObbligazionePer(aUC, scadenza, notaDiCredito).abs();
                    java.math.BigDecimal delta = scadenza.getIm_scadenza().subtract(totale);
                    if (delta.compareTo(new java.math.BigDecimal(0)) > 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Attenzione: La scadenza ");
                        sb.append(scadenza.getDs_scadenza());
                        sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
                        sb.append(" è stata coperta solo per ");
                        sb.append(totale.doubleValue() + " EUR!");
                        throw new it.cnr.jada.comp.ApplicationException(sb.toString());
                    } else if (delta.compareTo(new java.math.BigDecimal(0)) < 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Attenzione: La scadenza ");
                        sb.append(scadenza.getDs_scadenza());
                        sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
                        sb.append(" è scoperta per ");
                        sb.append(delta.abs().doubleValue() + " EUR!");
                        throw new it.cnr.jada.comp.ApplicationException(sb.toString());
                    }
                }
            }
        }
    }
//^^@@

    /**
     * Validazione documento.
     * PreCondition:
     * Viene richiesta la creazione di un documento e lo stesso non ha superato il metodo 'validaFattura'.
     * PostCondition:
     * Non  viene consentita la registrazione della fattura.
     * Tutti i controlli superati.
     * PreCondition:
     * Viene richiesta la creazione di un documento e lo stesso ha superato il metodo 'validaFattura'.
     * PostCondition:
     * Viene consentita la registrazione del documento.
     */
//^^@@
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {

        return creaConBulk(userContext, bulk, null);
    }

    /**
     * Validazione documento.
     * PreCondition:
     * Viene richiesta la creazione di un documento e lo stesso non ha superato il metodo 'validaFattura'.
     * PostCondition:
     * Non  viene consentita la registrazione della fattura.
     * Tutti i controlli superati.
     * PreCondition:
     * Viene richiesta la creazione di un documento e lo stesso ha superato il metodo 'validaFattura'.
     * PostCondition:
     * Viene consentita la registrazione del documento.
     */
//^^@@
    public OggettoBulk creaConBulk(
            UserContext userContext,
            OggettoBulk bulk,
            OptionRequestParameter status)
            throws ComponentException {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bulk;


        //effettua il controllo di validazione
        fattura = (Fattura_attivaBulk) calcoloConsuntivi(userContext, fattura);

        assegnaProgressivo(userContext, fattura);

        try {
            java.util.Calendar gc = java.util.Calendar.getInstance();
            gc.setTime(getHome(userContext, fattura).getServerTimestamp());
            //controlla che la data di registrazione non sia successiva alla data di sistema
            if (fattura.getDt_registrazione() != null
                    && fattura.getDt_registrazione().after(
                    new java.sql.Timestamp(gc.getTime().getTime())))
                throw new it.cnr.jada.comp.ApplicationException(
                        "Attenzione: la data di registrazione non puo' essere successiva alla data attuale");
            else {
                // controlla che la data di registrazione sia successiava all'ultima data di registrazione inserita
                java.sql.Timestamp ultimaRegistrazione =
                        (
                                (Fattura_attivaHome) getHome(
                                        userContext,
                                        Fattura_attivaBulk.class)).findForMaxDataRegistrazione(
                                userContext,
                                fattura);
                if (ultimaRegistrazione != null
                        && fattura.getDt_registrazione().before(ultimaRegistrazione))
                    throw new it.cnr.jada.comp.ApplicationException(
                            "La data di registrazione non e' valida: deve essere successiva all'ultima data di registrazione inserita (relativa al tipo sezionale)!");
            }
        } catch (PersistencyException ex) {
            throw handleException(fattura, ex);
        }

        completaDatiPerFatturazioneElettronica(userContext, fattura);

        if (fattura instanceof Fattura_attiva_IBulk) {
            if (hasFatturaAttivaARowToBeInventoried(userContext, fattura)) {
                verificaEsistenzaEdAperturaInventario(userContext, fattura);
                if (fattura.hasCompetenzaCOGEInAnnoPrecedente())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: per le date competenza indicate non è possibile inventariare i beni.");
                if (hasFatturaAttivaARowNotInventoried(userContext, fattura))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: è necessario inventariare tutti i dettagli.");
            }
        }

        validaFattura(userContext, fattura);

        // Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        if (fattura.getDefferredSaldi() != null)
            aTempDiffSaldi = (PrimaryKeyHashMap) fattura.getDefferredSaldi().clone();

        manageDocumentiContabiliCancellati(userContext, fattura, status);

        aggiornaAccertamenti(userContext, fattura, status);
        if (fattura instanceof Nota_di_credito_attivaBulk) {
            Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk) fattura;
            aggiornaRigheFatturaAttivaDiOrigine(userContext, ndc);
            aggiornaObbligazioni(userContext, ndc, status);
        }
        if (fattura instanceof Nota_di_debito_attivaBulk) {
            Nota_di_debito_attivaBulk ndd = (Nota_di_debito_attivaBulk) fattura;
            aggiornaRigheFatturaAttivaDiOrigine(userContext, ndd);

            for (Iterator i = ndd.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk rigaFA = ((Nota_di_debito_attiva_rigaBulk) i.next()).getRiga_fattura_associata();
                Fattura_attiva_IBulk fatturaOriginaria = rigaFA.getFattura_attivaI();
                RemoteIterator ri = findNotaDiCreditoFor(userContext, fatturaOriginaria);
                try {
                    if (ri != null  && ri.countElements() > 0) {
                        while (ri.hasMoreElements()) {
                            Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk) ri.nextElement();
                            notaDiCredito =(Nota_di_credito_attivaBulk) findByPrimaryKey(userContext, notaDiCredito);
                            if (notaDiCredito != null && (notaDiCredito.isFatturaElettronicaScartata()||notaDiCredito.isFatturaElettronicaRifiutata()) && notaDiCredito.getIm_totale_fattura().compareTo(ndd.getIm_totale_fattura()) == 0){
                                impostaDocumentoDaNonInviare(ndd);
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    throw new it.cnr.jada.comp.ApplicationException("Errore nel recupero della nota di credito");
                }
            break;
            }
        }

        prepareScarichiInventario(userContext, fattura);

        fattura = (Fattura_attivaBulk) super.creaConBulk(userContext, fattura);

        aggiornaScarichiInventario(userContext, fattura);
        String messaggio = aggiornaAssociazioniInventario(userContext, fattura);
        // Restore dell'hash map dei saldi
        if (fattura.getDefferredSaldi() != null)
            fattura.getDefferredSaldi().putAll(aTempDiffSaldi);
        aggiornaCogeCoanDocAmm(userContext, fattura);

        aggiornaDataEsigibilitaIVA(userContext, fattura, "C");
        try {
            if (!verificaStatoEsercizio(
                    userContext,
                    new EsercizioBulk(
                            fattura.getCd_cds(),
                            ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            throw handleException(bulk, e);
        }
        controllaQuadraturaInventario(userContext, fattura);
        if (messaggio != null)
            return asMTU(fattura, messaggio);

        return fattura;
    }

    private void completaDatiPerFatturazioneElettronica(UserContext userContext,
                                                        Fattura_attivaBulk fattura) throws ComponentException {
        if (fattura.isPossibleFatturazioneElettronica()) {
            if (fattura.getCliente() != null) {
                impostaDatiPerFatturazioneElettronica(userContext, fattura, fattura.getCliente());
                if (fattura.isDocumentoFatturazioneElettronica()) {
                    fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_ALLA_FIRMA);
                }
            }
        }
    }

    private void controlloCodiceIpaValorizzato(TerzoBulk terzo)
            throws ApplicationException {
        if (terzo.getAnagrafico().isEntePubblico() && terzo.getCodiceUnivocoUfficioIpa() == null && terzo.getAnagrafico().isItaliano()) {
            throw new it.cnr.jada.comp.ApplicationException(
                    "Il codice terzo utilizzato si riferisce ad un'anagrafica censita nell'indice delle " +
                            "pubbliche amministrazioni. Richiedere tramite helpdesk l'inserimento del codice Univoco Ufficio IPA " +
                            "relativo al terzo per il quale si sta tentando di emettere fattura.");
        } else if (!terzo.getAnagrafico().isEntePubblico() && terzo.getAnagrafico().isPersonaGiuridica() && terzo.getAnagrafico().isItaliano() &&  
        			terzo.getCodiceDestinatarioFatt() == null && !terzo.esistePecFatturazioneElettronica() && !terzo.getFlSbloccoFatturaElettronica()) {
            throw new it.cnr.jada.comp.ApplicationException(
                    "Il codice terzo utilizzato si riferisce ad un'anagrafica che ha attiva la fatturazione elettronica." +
                            "E' necessario indicare sul terzo il codice destinatario fattura o la pec per la fatturazione elettronica.");
        }
    }
/*
private void deleteAssociazioniInventarioWith(UserContext userContext,Fattura_attiva_rigaIBulk dettaglio)
	throws ComponentException {

	CarichiInventarioTable scarichi = (CarichiInventarioTable)dettaglio.getFattura_attiva().getCarichiInventarioHash();
	if (scarichi != null && !scarichi.isEmpty()) {
		Buono_scaricoBulk bs = (Buono_scaricoBulk)scarichi.get(dettaglio);
		Buono_scaricoHome home = (Buono_scaricoHome)getHomeCache(userContext).getHome(bs);
		try {
			home.deleteTempFor(bs, dettaglio);
		} catch (PersistencyException e) {
			throw handleException(dettaglio, e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(dettaglio, e);
		}
	}
	//if (dettaglio != null && dettaglio.isToBeDeleted()) {
		//java.io.StringWriter sql = new java.io.StringWriter();
		//java.io.PrintWriter pw = new java.io.PrintWriter(sql);
		//pw.write("DELETE FROM " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ASS_INV_BENE_FATTURA ");
		//pw.write("WHERE (CD_CDS_FATT_ATT = ? AND ESERCIZIO_FATT_ATT = ? AND CD_UO_FATT_Att = ? AND PG_FATTURA_ATTIVA = ? AND PROGRESSIVO_RIGA_FATT_ATT = ?)");
		//pw.flush();
		//java.sql.PreparedStatement ps = null;
		//try {
			//try {
				//ps = getConnection(userContext).prepareStatement(sql.toString());
				//pw.close();

				//ps.setString(1, dettaglio.getCd_cds());
				//ps.setInt(2, dettaglio.getEsercizio().intValue());
				//ps.setString(3, dettaglio.getCd_unita_organizzativa());
				//ps.setLong(4, dettaglio.getFattura_attiva().getPg_fattura_attiva().longValue());
				//ps.setLong(5, dettaglio.getProgressivo_riga().longValue());
				//LoggableStatement.execute(ps);
			//} finally {
				//if (ps != null)
					//try{ps.close();}catch( java.sql.SQLException e ){};
			//}
		//} catch (java.sql.SQLException e) {
			//throw handleException(dettaglio, e);
		//}
	//}
}*/
//^^@@

    private void deleteAssociazioniInventarioWith(UserContext userContext, Fattura_attiva_rigaBulk dettaglio)
            throws ComponentException {
        try {
            if (dettaglio != null && dettaglio.isToBeDeleted()) {
                Ass_inv_bene_fatturaHome asshome = (Ass_inv_bene_fatturaHome) getHome(userContext, Ass_inv_bene_fatturaBulk.class);
                SQLBuilder sql = asshome.createSQLBuilder();
                sql.addSQLClause("AND", "CD_CDS_FATT_ATT", sql.EQUALS, dettaglio.getCd_cds());
                sql.addSQLClause("AND", "ESERCIZIO_FATT_ATT", sql.EQUALS, dettaglio.getEsercizio().intValue());
                sql.addSQLClause("AND", "CD_UO_FATT_ATT", sql.EQUALS, dettaglio.getCd_unita_organizzativa());
                sql.addSQLClause("AND", "PG_FATTURA_ATTIVA", sql.EQUALS, dettaglio.getPg_fattura_attiva().longValue());
                sql.addSQLClause("AND", "PROGRESSIVO_RIGA_FATT_ATT", sql.EQUALS, dettaglio.getProgressivo_riga().longValue());
                List associazioni = asshome.fetchAll(sql);
                for (Iterator i = associazioni.iterator(); i.hasNext(); ) {
                    Ass_inv_bene_fatturaBulk associazione = (Ass_inv_bene_fatturaBulk) i.next();
                    associazione.setCrudStatus(OggettoBulk.TO_BE_DELETED);
                    super.eliminaConBulk(userContext, associazione);
                }
            }
        } catch (PersistencyException e) {
            throw handleException(dettaglio, e);
        }
    }
//^^@@

    /**
     * tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * Permette l'annullamento della fattura.
     * validazione eliminazione fattura.
     * PreCondition:
     * E' stata eliminata una fattura in stato B or C
     * PostCondition:
     * Viene inviato un messaggio.
     */
//^^@@
    private void deleteLogically(UserContext userContext, Fattura_attivaBulk fattura_attiva) throws ComponentException {

        if (fattura_attiva instanceof Voidable && ((Voidable) fattura_attiva).isVoidable()) {
            try {
                java.sql.Timestamp dataAnnullamento = getHome(userContext, fattura_attiva).getServerTimestamp();
                java.util.Calendar dtAnnullamentoCal = java.util.GregorianCalendar.getInstance();
                dtAnnullamentoCal.setTime(dataAnnullamento);
                int annoSolare = dtAnnullamentoCal.get(java.util.Calendar.YEAR);
                if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue()) {
                    //Sono costretto ad utilizzare il Calendar per mantenere ora, minuti e secondi.
                    dtAnnullamentoCal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
                    dtAnnullamentoCal.set(java.util.Calendar.DAY_OF_MONTH, 31);
                    dtAnnullamentoCal.set(java.util.Calendar.YEAR, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
                    dataAnnullamento = new java.sql.Timestamp(dtAnnullamentoCal.getTime().getTime());
                }

                for (int c = 0; c < fattura_attiva.getBulkLists().length; c++) {
                    BulkList bl = (it.cnr.jada.bulk.BulkList) fattura_attiva.getBulkLists()[c];
                    for (Iterator i = bl.iterator(); i.hasNext(); ) {
                        OggettoBulk obj = (OggettoBulk) i.next();
                        if (obj instanceof Fattura_attiva_rigaBulk) {
                            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) obj;
                            if (riga instanceof Voidable && ((Voidable) riga).isVoidable()) {
                                ((Voidable) riga).setAnnullato(dataAnnullamento);
                                riga.setToBeUpdated();
                                if (riga.isInventariato()) {
                                    Fattura_attiva_rigaBulk cloneDettaglio = (Fattura_attiva_rigaBulk) riga.clone();
                                    cloneDettaglio.setToBeDeleted();
                                    deleteAssociazioniInventarioWith(userContext, cloneDettaglio);
                                }

                                update(userContext, riga);
                            } else
                                throw new it.cnr.jada.comp.ApplicationException("Questa fattura NON è annullabile perché almeno uno dei sui dettagli non è stato associato a mandato o reversale!");
                        }
                    }
                }

                fattura_attiva.setAnnullato(dataAnnullamento);
                if (fattura_attiva.REGISTRATO_IN_COGE.equalsIgnoreCase(fattura_attiva.getStato_coge()))
                    fattura_attiva.setStato_coge(fattura_attiva.DA_RIREGISTRARE_IN_COGE);
                if (fattura_attiva.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(fattura_attiva.getStato_coan()))
                    fattura_attiva.setStato_coan(fattura_attiva.DA_RICONTABILIZZARE_IN_COAN);
                fattura_attiva.setToBeUpdated();
                updateBulk(userContext, fattura_attiva);

                if (fattura_attiva instanceof Fattura_attiva_IBulk &&
                        fattura_attiva.getAccertamentiHash() != null)
                    aggiornaAccertamentiSuCancellazione(
                            userContext,
                            fattura_attiva,
                            fattura_attiva.getAccertamentiHash().keys(),
                            null,
                            null);
                if (fattura_attiva instanceof Nota_di_credito_attivaBulk &&
                        ((Nota_di_credito_attivaBulk) fattura_attiva).getObbligazioniHash() != null)
                    aggiornaObbligazioniSuCancellazione(
                            userContext,
                            (Nota_di_credito_attivaBulk) fattura_attiva,
                            ((Nota_di_credito_attivaBulk) fattura_attiva).getObbligazioniHash().keys(),
                            null,
                            null);
                return;
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(fattura_attiva, e);
            }
        }
        throw new it.cnr.jada.comp.ApplicationException("Questa fattura NON è annullabile!");
    }
//^^@@

    /**
     * tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * Permette la cancellazione della fattura.
     * validazione eliminazione fattura.
     * PreCondition:
     * E' stata eliminata una fattura in stato B or C
     * PostCondition:
     * Viene inviato un messaggio.
     */
//^^@@
    private void deletePhisically(UserContext aUC, Fattura_attivaBulk fattura_attiva) throws ComponentException {

        for (int c = 0; c < fattura_attiva.getBulkLists().length; c++) {
            BulkList bl = (it.cnr.jada.bulk.BulkList) fattura_attiva.getBulkLists()[c];
            for (Iterator i = bl.iterator(); i.hasNext(); ) {
                OggettoBulk obj = (OggettoBulk) i.next();
                obj.setToBeDeleted();
                if (obj instanceof Fattura_attiva_rigaIBulk) {
                    Fattura_attiva_rigaIBulk fpr = (Fattura_attiva_rigaIBulk) obj;
                    if (fpr.isInventariato())
                        deleteAssociazioniInventarioWith(aUC, fpr);
                }
            }
        }

        super.eliminaConBulk(aUC, fattura_attiva);

        try {
            if (fattura_attiva instanceof Fattura_attiva_IBulk)
                aggiornaAccertamentiSuCancellazione(
                        aUC,
                        fattura_attiva,
                        fattura_attiva.getAccertamentiHash().keys(),
                        null,
                        null);
            if (fattura_attiva instanceof Nota_di_credito_attivaBulk && ((Nota_di_credito_attivaBulk) fattura_attiva).getObbligazioniHash() != null)
                aggiornaObbligazioniSuCancellazione(
                        aUC,
                        (Nota_di_credito_attivaBulk) fattura_attiva,
                        ((Nota_di_credito_attivaBulk) fattura_attiva).getObbligazioniHash().keys(),
                        null,
                        null);
        } catch (Throwable e) {
            throw handleException(fattura_attiva, e);
        }
    }

    /**
     * tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * Permette la cancellazione della fattura.
     * validazione eliminazione fattura.
     * PreCondition:
     * E' stata eliminata una fattura in stato B or C
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare una fattura in stato IVA B o C"
     */
//^^@@
    public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        Fattura_attivaBulk fattura_attiva = (Fattura_attivaBulk) bulk;

        // Gennaro Borriello - (08/11/2004 19.06.55)
        //	Controllo gli stati degli esercizi Finanziari/Economici.
        verificaStatoEsercizi(aUC, fattura_attiva);
        Fattura_attiva_rigaBulk riga;
        Integer contaRighe = 0;
        try {
            eliminaFattura(aUC, fattura_attiva);
            for (Iterator i = fattura_attiva.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                riga = (Fattura_attiva_rigaBulk) i.next();
                eliminaRiga(aUC, riga);
                if (!riga.isToBeCreated()) {
                    try {
                        List result = findManRevRigaCollegati(aUC, riga);
                        if (result != null && !result.isEmpty()) {
                            riga.setToBeUpdated();
                            riga.setTi_associato_manrev(Fattura_passivaBulk.ASSOCIATO_A_MANDATO);
                            contaRighe++;
                        }
                    } catch (PersistencyException e) {
                        throw new ComponentException(e);
                    } catch (IntrospectionException e) {
                        throw new ComponentException(e);
                    }
                }
            }
            if (contaRighe == fattura_attiva.getFattura_attiva_dettColl().size()) {
                fattura_attiva.setToBeUpdated();
                fattura_attiva.setTi_associato_manrev(Fattura_attivaBulk.ASSOCIATO_A_MANDATO);
            }
        } catch (it.cnr.jada.comp.ApplicationException e) {
            throw handleException(new it.cnr.jada.comp.ApplicationException(e.getMessage()));
        }

        // Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        if (fattura_attiva.getDefferredSaldi() != null)
            aTempDiffSaldi = (PrimaryKeyHashMap) fattura_attiva.getDefferredSaldi().clone();

        if (fattura_attiva instanceof Voidable && fattura_attiva.isVoidable())
            deleteLogically(aUC, fattura_attiva);
        else
            deletePhisically(aUC, fattura_attiva);

        // Restore dell'hash map dei saldi
        if (fattura_attiva.getDefferredSaldi() != null)
            fattura_attiva.getDefferredSaldi().putAll(aTempDiffSaldi);
        aggiornaCogeCoanDocAmm(aUC, fattura_attiva);

        //try {
        //if (!verificaStatoEsercizio(
        //aUC,
        //new EsercizioBulk(
        //fattura_attiva.getCd_cds(),
        //((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio())))
        //throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");
        //} catch (it.cnr.jada.comp.ApplicationException e) {
        //throw handleException(bulk, e);
        //}
    }
//^^@@

    private void eliminaFattura(UserContext aUC, Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        String entitaSingolare = (fatturaAttiva instanceof Fattura_attiva_IBulk) ?
                "fattura" : "nota di credito";

        //non e' possibile eliminare una fattura con dettagli pagati
        if (fatturaAttiva.STATO_PARZIALE.equalsIgnoreCase(fatturaAttiva.getStato_cofi()))
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare una " + entitaSingolare + " in stato parziale.");
        if (fatturaAttiva.isPagata())
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare una " + entitaSingolare + " in stato pagato.");
        if (fatturaAttiva.isStampataSuRegistroIVA())
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare un  documento attivo già stampato sui registri IVA!");
        if (fatturaAttiva.hasIntrastatInviati())
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare una " + entitaSingolare + " per cui esistono dettagli intrastat inviati!");
        //validazioneComune(aUC, fatturaAttiva);

        if (fatturaAttiva instanceof Fattura_attiva_IBulk) {
            Fattura_attiva_IBulk fatturaAttivaI = (Fattura_attiva_IBulk) fatturaAttiva;
            if (fatturaAttivaI.hasStorni())
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: per eliminare una fattura è necessario eliminare tutte le note di credito/debito collegate!");
            //Controllo la presenza di eventuali ndc o ndd annullate per non permettere la
            //cancellazione della fattura. Infatti hasStorni e hasAddebiti non caricano
            //i documenti annullati (Necessario solo se la fattura è cancellabile fisicamente e non logicamente)
            if (!fatturaAttivaI.isVoidable()) {
                try {
                    Nota_di_credito_attivaHome homeNdC = (Nota_di_credito_attivaHome) getHome(aUC, Nota_di_credito_attivaBulk.class);
                    if (homeNdC.selectFor(fatturaAttivaI).executeExistsQuery(getConnection(aUC)))
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare fisicamente questa fattura perché ad essa sono associate note di credito annullate!");
                    Nota_di_debito_attivaHome homeNdD = (Nota_di_debito_attivaHome) getHome(aUC, Nota_di_debito_attivaBulk.class);
                    if (homeNdD.selectFor(fatturaAttivaI).executeExistsQuery(getConnection(aUC)))
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare fisicamente questa fattura perché ad essa sono associate note di debito annullate!");
                } catch (SQLException e) {
                    throw handleException(fatturaAttivaI, e);
                }

            }
        }
    }

    /**
     * valida eliminazione dettaglio
     * PreCondition:
     * E' stato eliminato un dettaglio in  in una fattura in stato IVA B o C.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio in una fattura in stato IVA B o C".
     * tutti i controlli superati
     * PreCondition:
     * Nessun errore è stato rilevato.
     * PostCondition:
     * Viene dato il consenso per l'eliminazione della riga.
     * <p>
     * valida eliminazione dettaglio pagato.
     * PreCondition:
     * E' stato eliminato un dettaglio in stato P.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio pagato".
     * eliminazione dettaglio in fattura pagata.
     * PreCondition:
     * E' stato eliminato un dettaglio in una fattura con testata in stato P
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio in una fattura pagata".
     */
//^^@@
    public void eliminaRiga(UserContext aUC, Fattura_attiva_rigaBulk riga)
            throws ComponentException {

        if (riga.getFattura_attiva().isPagata())
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si può eliminare un dettaglio di una fattura già pagata.");

        if (riga instanceof Fattura_attiva_rigaIBulk) {
            Fattura_attiva_rigaIBulk faRiga = (Fattura_attiva_rigaIBulk) riga;
            if (faRiga.hasAddebiti() || faRiga.hasStorni())
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si può eliminare il dettaglio " + ((faRiga.getDs_riga_fattura() == null) ? "" : faRiga.getDs_riga_fattura()) + " perché ad esso sono associati addebiti o storni!");
        }

        String statoIVA = getStatoIVA(aUC, riga.getFattura_attiva());

        //Tolto come da richiesta 423.
        //if (riga.getFattura_attiva().isStampataSuRegistroIVA())
        //throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è permessa la cancellazione di un dettaglio quando lo stato IVA è B o C.");


        if (riga.STATO_PAGATO.equals(riga.getStato_cofi()))
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si può eliminare il dettaglio " + ((riga.getDs_riga_fattura() == null) ? "" : riga.getDs_riga_fattura()) + " perché già pagato.");

    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la verifica dell'esistenza di parametri per la protocollazione iva
     * in relazione al processo di protocollazion pgProtocollazioneIva
     * PostCondition:
     * Viene restituito 'true' o 'false' a seconda che i parametri vengano
     * rispettivamente trovati o meno
     */

    public boolean esistonoDatiPerProtocollazioneIva(
            UserContext userContext,
            Long pgProtocollazioneIva)
            throws ComponentException, PersistencyException {

        if (pgProtocollazioneIva == null) return false;

        Vsx_rif_protocollo_ivaHome home = (Vsx_rif_protocollo_ivaHome) getHome(userContext, Vsx_rif_protocollo_ivaBulk.class);

        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "pg_call", sql.EQUALS, pgProtocollazioneIva);

        try {
            return sql.executeExistsQuery(getConnection(userContext));
        } catch (SQLException e) {
            throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e);
        }
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la verifica dell'esistenza di parametri per la stampa iva
     * in relazione al processo di stampa pgStampa
     * PostCondition:
     * Viene restituito 'true' o 'false' a seconda che i parametri vengano
     * rispettivamente trovati o meno
     */

    public boolean esistonoDatiPerStampaIva(
            UserContext userContext,
            Long pgStampa)
            throws ComponentException, PersistencyException {

        if (pgStampa == null) return false;

        V_stm_paramin_ft_attivaHome home = (V_stm_paramin_ft_attivaHome) getHome(userContext, V_stm_paramin_ft_attivaBulk.class);

        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "id_report", sql.EQUALS, pgStampa);

        try {
            return sql.executeExistsQuery(getConnection(userContext));
        } catch (SQLException e) {
            throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e);
        }
    }

    /**
     * sezionale non valido.
     * PreCondition:
     * E' stata selezionato sezionale non valido.
     * PostCondition:
     * Viene inviato il messaggio: "Il tipo di sezionale non è valido".
     * tutti i controlli superati.
     * PreCondition:
     * E' stata selezionato sezionale valido.
     * PostCondition:
     * Viene ritornato il vettore dei sezionali corrispondenti.
     */
//^^@@
    public Vector estraeSezionali(UserContext aUC, Fattura_attivaBulk fattura) throws ComponentException {

        try {
            if (fattura == null)
                return null;
            return new Vector(findSezionali(aUC, fattura));
        } catch (Throwable t) {
            throw new it.cnr.jada.comp.ApplicationException("Il tipo di sezionale non è valido");
        }
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Viene richiesto l'elenco dei sezionali validi per una ristampa di
     * documenti attivi
     * PostCondition:
     * Viene restituito l'elenco dei sezionali compatibili
     */

    public Vector estraeSezionaliPerRistampa(
            UserContext aUC,
            Fattura_attivaBulk fattura,
            Vector clauses) throws ComponentException {

        try {
            if (fattura == null)
                return null;
            return new Vector(findSezionaliPerRistampa(aUC, fattura, clauses));
        } catch (Throwable t) {
            throw new it.cnr.jada.comp.ApplicationException("Il tipo di sezionale non è valido");
        }
    }
//^^@@

    /**
     * ricerca degli accertamenti per le fatture attive
     * PreCondition:
     * Richiesto un accertamento
     * PostCondition:
     * Restituisce la collezione di accertamenti
     */
//^^@@
    public RemoteIterator findAccertamentiFor(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            java.math.BigDecimal minIm_Scadenza)
            throws ComponentException {

        Fattura_attiva_rigaIHome home = (Fattura_attiva_rigaIHome) getHome(userContext, Fattura_attiva_rigaIBulk.class);
        try {
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.selectAccertamentiPer(
                    userContext,
                    fatturaAttiva,
                    minIm_Scadenza);
            return iterator(
                    userContext,
                    sql,
                    Fattura_attiva_rigaIBulk.class,
                    "default");
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fatturaAttiva, e);
        }
    }
//^^@@

    /**
     * lista dei dettagli.
     * PreCondition:
     * Richiesta di caricamento dettagli di una fattura, nota di credito, nota di debito
     * PostCondition:
     * Restituisce la lista dei dettagli
     */
//^^@@
    public java.util.List findDettagli(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (fatturaAttiva == null) return null;

        it.cnr.jada.bulk.BulkHome home = null;
        if (fatturaAttiva instanceof Nota_di_credito_attivaBulk)
            home = getHome(aUC, Nota_di_credito_attiva_rigaBulk.class);
        else if (fatturaAttiva instanceof Nota_di_debito_attivaBulk)
            home = getHome(aUC, Nota_di_debito_attiva_rigaBulk.class);
        else home = getHome(aUC, Fattura_attiva_rigaIBulk.class);

        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

        sql.addClause("AND", "pg_fattura_attiva", sql.EQUALS, fatturaAttiva.getPg_fattura_attiva());
        sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaAttiva.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, fatturaAttiva.getEsercizio());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fatturaAttiva.getCd_unita_organizzativa());
        sql.addOrderBy("progressivo_riga");
        return home.fetchAll(sql);

    }
//^^@@

    /**
     * ricerca delle banche.
     * PreCondition:
     * Richiesta ricerca delle banche
     * PostCondition:
     * Restituisce la collezione di banche
     */
//^^@@
    public java.util.Collection findListabanche(UserContext aUC, Nota_di_credito_attivaBulk notaDiCredito) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

        if (notaDiCredito.getCliente() == null ||
                notaDiCredito.getCliente().getCd_terzo() == null)
            return null;

        return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, notaDiCredito, null, null));
    }
//^^@@

    /**
     * ricerca delle banche per la uo
     * PreCondition:
     * Richiesta ricerca delle banche
     * PostCondition:
     * Restituisce la collezione di banche
     */
//^^@@
    public Vector findListabancheuo(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException, PersistencyException {

        if (fatturaAttiva.getTerzo_uo() == null ||
                fatturaAttiva.getTerzo_uo().getCd_terzo() == null)
            return null;

        Vector listaBanche = new Vector(getHome(aUC, BancaBulk.class).fetchAll(
                selectBanca_uoByClause(aUC, fatturaAttiva, null, null)));

        return listaBanche;
    }
//^^@@

    /**
     * ricerca delle modalità di pagamento.
     * PreCondition:
     * Richiesta ricerca delle modalità di pagamento
     * PostCondition:
     * Restituisce la collezione di modalità di pagamento
     */
//^^@@
    public java.util.Collection findModalita(UserContext aUC, Nota_di_credito_attivaBulk ndc) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (ndc.getCliente() == null || ndc.getCliente().getCd_terzo() == null) return null;
        return ((TerzoHome) getHome(aUC, TerzoBulk.class)).findRif_modalita_pagamento(ndc.getCliente());
    }
//^^@@

    /**
     * ricerca delle modalità di pagamento della uo.
     * PreCondition:
     * Richiesta ricerca delle modalità di pagamento
     * PostCondition:
     * Restituisce la collezione di modalità di pagamento
     */
//^^@@
    public java.util.Collection findModalita_uo(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (fatturaAttiva.getTerzo_uo() == null || fatturaAttiva.getTerzo_uo().getCd_terzo() == null) return null;
        return ((TerzoHome) getHome(aUC, TerzoBulk.class)).findRif_modalita_pagamento(fatturaAttiva.getTerzo_uo());
    }
//^^@@

    /**
     * ricerca delle note di credito.
     * PreCondition:
     * Richiesta ricerca delle note di credito generate dalla fattura in argomento
     * PostCondition:
     * Restituisce la lista delle ricorrenze
     */
//^^@@
    public RemoteIterator findNotaDiCreditoFor(UserContext aUC, Fattura_attiva_IBulk fatturaAttiva) throws ComponentException {

        if (fatturaAttiva == null) return null;
        Nota_di_credito_attivaHome home = (Nota_di_credito_attivaHome) getHome(aUC, Nota_di_credito_attivaBulk.class);
        return iterator(aUC,
                home.selectFor(fatturaAttiva),
                Nota_di_credito_attivaBulk.class,
                "dafault");
    }
//^^@@

    /**
     * ricerca delle note di debito.
     * PreCondition:
     * Richiesta ricerca delle note di debito generate dalla fattura in argomento
     * PostCondition:
     * Restituisce la lista delle ricorrenze
     */
//^^@@
    public RemoteIterator findNotaDiDebitoFor(UserContext aUC, Fattura_attiva_IBulk fatturaAttiva) throws ComponentException {

        if (fatturaAttiva == null) return null;
        Nota_di_debito_attivaHome home = (Nota_di_debito_attivaHome) getHome(aUC, Nota_di_debito_attivaBulk.class);
        return iterator(aUC,
                home.selectFor(fatturaAttiva),
                Nota_di_debito_attivaBulk.class,
                "dafault");
    }
//^^@@

    /**
     * Ricerca dei sezionali
     * PreCondition:
     * Ricerca dei sezionali.
     * PostCondition:
     * Restituisce la collezione dei sezionali validi per la fattura
     * Si verifica errore.
     * PreCondition:
     * Condizione di errore.
     * PostCondition:
     * Viene rilanciata un messaggio dettagliato.
     */
//^^@@
    public java.util.Collection findSezionali(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva)
            throws
            ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        Vector options = new Vector();
        if (fatturaAttiva.getFl_extra_ue() != null && fatturaAttiva.getFl_extra_ue().booleanValue()) {
            options.add(new String[][]{{"TIPO_SEZIONALE.FL_EXTRA_UE", "Y", "AND"}});
        } else if (fatturaAttiva.getFl_san_marino() != null && fatturaAttiva.getFl_san_marino().booleanValue()) {
            options.add(new String[][]{{"TIPO_SEZIONALE.FL_SAN_MARINO_CON_IVA", "Y", "AND"}});
        } else if (fatturaAttiva.getFl_intra_ue() != null && fatturaAttiva.getFl_intra_ue().booleanValue()) {
            options.add(new String[][]{{"TIPO_SEZIONALE.FL_INTRA_UE", "Y", "AND"}});
        } else
            options.add(new String[][]{{"TIPO_SEZIONALE.FL_ORDINARIO", "Y", "AND"}});

        options.add(new String[][]{{"TIPO_SEZIONALE.TI_BENE_SERVIZIO", "*", "AND"}});

        //Richiesta Mingarelli e Paolo del 25/02/2002.
        options.add(new String[][]{{"TIPO_SEZIONALE.FL_AUTOFATTURA", "N", "AND"}});
        //

        return ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome) getHome(aUC, it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class))
                .findTipiSezionali(
                        fatturaAttiva.getEsercizio(),
                        fatturaAttiva.getCd_uo_origine(),
                        it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.COMMERCIALE,
                        it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.VENDITE,
                        fatturaAttiva.getTi_fattura(),
                        options);
    }
//^^@@

    /**
     * Ricerca dei sezionali
     * PreCondition:
     * Ricerca dei sezionali.
     * PostCondition:
     * Restituisce la collezione dei sezionali validi per la fattura
     * Si verifica errore.
     * PreCondition:
     * Condizione di errore.
     * PostCondition:
     * Viene rilanciata un messaggio dettagliato.
     */
//^^@@
    public java.util.Collection findSezionaliPerRistampa(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva,
            Vector options)
            throws
            ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        return ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome) getHome(aUC, it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class))
                .findTipiSezionaliPerRistampa(
                        fatturaAttiva.getEsercizio(),
                        fatturaAttiva.getCd_uo_origine(),
                        it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.COMMERCIALE,
                        it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.VENDITE,
                        fatturaAttiva.getTi_fattura(),
                        options);
    }
//^^@@

    /**
     * Ricerca dei tariffari
     * PreCondition:
     * Un nuovo tariffario è stato selezionato dall'utente.
     * PostCondition:
     * Restituisce la collezione dei tariffari validi per la fattura
     */
//^^@@
    public it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk findTariffario(UserContext aUC, Fattura_attiva_rigaBulk riga) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {


        if (riga == null) return null;

        it.cnr.jada.bulk.BulkHome home = getHome(aUC, it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

        sql.addClause("AND", "cd_tariffario", sql.EQUALS, riga.getCd_tariffario());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, riga.getFattura_attiva().getCd_uo_origine());
        sql.addClause("AND", "dt_ini_validita", sql.LESS_EQUALS, riga.getFattura_attiva().getDt_registrazione());
        sql.addClause("AND", "dt_fine_validita", sql.GREATER_EQUALS, riga.getFattura_attiva().getDt_registrazione());


        it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;

        it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk tariffario = (it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk) broker.fetch(it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk.class);
        broker.close();
        return tariffario;
    }
//^^@@

    /**
     * ricerca dei termini di pagamento.
     * PreCondition:
     * Richiesta ricerca dei termini di pagamento
     * PostCondition:
     * Restituisce la collezione di termini di pagamento
     */
//^^@@
    public java.util.Collection findTermini(UserContext aUC, Nota_di_credito_attivaBulk ndc) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (ndc.getCliente() == null) return null;
        return ((TerzoHome) getHome(aUC, TerzoBulk.class)).findRif_termini_pagamento(ndc.getCliente());
    }
//^^@@

    /**
     * ricerca dei termini di pagamento per la uo.
     * PreCondition:
     * Richiesta ricerca dei termini di pagamento
     * PostCondition:
     * Restituisce la collezione di termini di pagamento
     */
//^^@@
    public java.util.Collection findTermini_uo(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (fatturaAttiva.getTerzo_uo() == null) return null;
        return ((TerzoHome) getHome(aUC, TerzoBulk.class)).findRif_termini_pagamento(fatturaAttiva.getTerzo_uo());
    }

    /**
     * ricerca del terzo della UO.
     * PreCondition:
     * Richiesta ricerca del terzo della UO
     * PostCondition:
     * Restituisce il terzo UO
     */
//^^@@
    public TerzoBulk findTerzoUO(UserContext aUC, Fattura_attivaBulk fattura) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {


        if (fattura == null) return null;

        it.cnr.jada.bulk.BulkHome home = getHome(aUC, TerzoBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fattura.getCd_uo_origine());

        it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;

        TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);
        broker.close();
        return terzo;

    }

    private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {

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

    private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {

        it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
        if (scadenze != null)
            while (scadenze.hasMoreElements()) {
                IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk) scadenze.nextElement();
                if (scadenza.getFather().isTemporaneo()) {
                    if (!documentiContabiliTemporanei.containsKey(scadenza.getFather())) {
                        Vector allInstances = new java.util.Vector();
                        allInstances.addElement(scadenza.getFather());
                        documentiContabiliTemporanei.put(scadenza.getFather(), allInstances);
                    } else {
                        ((Vector) documentiContabiliTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
                    }
                }
            }
        return documentiContabiliTemporanei;
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
     */
    private DivisaBulk getEuro(UserContext userContext) throws ComponentException {

        String cd_euro = null;
        try {
            cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
            if (cd_euro == null)
                throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire una fattura, immettere tale valore.");
        } catch (javax.ejb.EJBException e) {
            handleException(e);
        } catch (java.rmi.RemoteException e) {
            handleException(e);
        }

        DivisaBulk valuta = null;

        try {
            valuta = (DivisaBulk) getHome(userContext, DivisaBulk.class).find(new it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk(cd_euro)).get(0);
            if (valuta == null)
                throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire una fattura, immettere tale valore.");
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            handleException(e);
        }
        return valuta;
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
     */
    private Consuntivo_rigaVBulk getRigaConsuntivoFor(Fattura_attiva_rigaBulk rigaFatturaAttiva) {

        if (rigaFatturaAttiva == null || rigaFatturaAttiva.getVoce_iva() == null)
            return null;

        java.util.Collection consuntivo = rigaFatturaAttiva.getFattura_attiva().getFattura_attiva_consuntivoColl();

        String codiceIva = rigaFatturaAttiva.getVoce_iva().getCd_voce_iva();
        Consuntivo_rigaVBulk rigaConsuntivo = null;
        // Cerco nel consuntivo corrente una riga per il codice IVA richiesto
        for (Iterator i = consuntivo.iterator(); i.hasNext(); ) {
            Consuntivo_rigaVBulk rigaC = (Consuntivo_rigaVBulk) i.next();
            if (codiceIva != null && codiceIva.equalsIgnoreCase(rigaC.getVoce_iva().getCd_voce_iva())) {
                rigaConsuntivo = rigaC;
                break;
            }
        }

        if (rigaConsuntivo == null) {
            // Non ho trovato una riga per il codice IVA richiesto
            rigaConsuntivo = new Consuntivo_rigaVBulk(rigaFatturaAttiva);
            rigaFatturaAttiva.getFattura_attiva().addToFattura_attiva_consuntivoColl(rigaConsuntivo);
        }

        return rigaConsuntivo;
    }

    private RiportoDocAmmComponentSession getRiportoComponentSession(
            UserContext context)
            throws ComponentException {

        try {
            return (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                    "CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
                    RiportoDocAmmComponentSession.class);
        } catch (Throwable t) {
            throw handleException(t);
        }
    }

    private String getStatoIVA(UserContext userContext, Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        return (fattura_attiva.getProtocollo_iva() == null ||
                fattura_attiva.getProtocollo_iva_generale() == null) ?
                "A" : "B";
    }

    private String getStatoRiporto(
            UserContext context,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        try {
            RiportoDocAmmComponentSession session =
                    (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                            "CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
                            RiportoDocAmmComponentSession.class);
            return session.getStatoRiporto(context, fatturaAttiva);
        } catch (Throwable t) {
            throw handleException(fatturaAttiva, t);
        }
    }
//^^@@

    private String getStatoRiportoInScrivania(
            UserContext context,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        try {
            RiportoDocAmmComponentSession session =
                    (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                            "CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
                            RiportoDocAmmComponentSession.class);
            return session.getStatoRiportoInScrivania(context, fatturaAttiva);
        } catch (Throwable t) {
            throw handleException(fatturaAttiva, t);
        }
    }

    /**
     * Dettagli di fattura non inventariati
     * PreCondition:
     * Richiesta dell'esistenza di dettagli non inventariati
     * PostCondition:
     * Restituisce la conferma
     */
//^^@@
    public boolean hasFatturaAttivaARowNotInventoried(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        java.util.Vector coll = new java.util.Vector();
        Iterator dettagli = fattura_attiva.getFattura_attiva_dettColl().iterator();
        if (dettagli != null) {
            while (dettagli.hasNext()) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) dettagli.next();
                if (Fattura_attiva_IBulk.BENEDUREVOLE.equalsIgnoreCase(riga.getFattura_attiva().getTi_causale_emissione()) &&
                        !riga.isInventariato())
                    return true;
            }
        }
        return false;
    }
//^^@@

    private boolean hasFatturaAttivaARowToBeInventoried(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        java.util.Vector coll = new java.util.Vector();
        if (fattura_attiva.getFattura_attiva_dettColl() != null) {
            Iterator dettagli = fattura_attiva.getFattura_attiva_dettColl().iterator();
            while (dettagli.hasNext()) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) dettagli.next();
                if (Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(riga.getFattura_attiva().getTi_causale_emissione()))
                    return true;
            }
        }
        return false;
    }
//^^@@

    /**
     * normale
     * PreCondition:
     * Viene richiesta una possibile operazione di creazione.
     * PostCondition:
     * L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
     */
//^^@@
    public OggettoBulk inizializzaBulkPerInserimento(
            UserContext aUC,
            OggettoBulk bulk)
            throws ComponentException {
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bulk;

        try {
            //imposto CDS e UO per l'ENTE
            it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome) getHome(aUC, it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk.class);

            fattura.setCd_unita_organizzativa(((it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk) (getHome(aUC, it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk.class).fetchAll(uoHome.createSQLBuilder())).get(0)).getCd_unita_organizzativa());

            it.cnr.contab.config00.sto.bulk.CdsHome cdsHome = (it.cnr.contab.config00.sto.bulk.CdsHome) getHome(aUC, it.cnr.contab.config00.sto.bulk.CdsBulk.class);

            fattura.setCd_cds(((it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk) (getHome(aUC, it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk.class).fetchAll(uoHome.createSQLBuilder())).get(0)).getCd_unita_padre());

            Fattura_attivaHome dHome = (Fattura_attivaHome) getHome(aUC, fattura);
            if (!verificaStatoEsercizio(
                    aUC,
                    new EsercizioBulk(
                            fattura.getCd_cds(),
                            fattura.getEsercizio())))  //equivalente a scrivania!!
                throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire una fattura attiva per un esercizio non aperto!");
            java.sql.Timestamp date = dHome.getServerDate();
            int annoSolare = fattura.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire documenti attivi in esercizi non corrispondenti all'anno solare!");
            fattura.setDt_registrazione(date);
            setDt_termine_creazione_docamm(aUC, fattura);
            fattura.setAttivoSplitPayment(isAttivoSplitPayment(aUC, date));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(bulk, e);
        }


        fattura.setStato_cofi(fattura.STATO_INIZIALE);
        fattura.setStato_coge(fattura.NON_REGISTRATO_IN_COGE);
        fattura.setIm_totale_fattura(new java.math.BigDecimal(0));
        fattura.setIm_totale_imponibile(new java.math.BigDecimal(0));
        fattura.setIm_totale_iva(new java.math.BigDecimal(0));

        fattura.setDt_da_competenza_coge(fattura.getDt_registrazione());
        fattura.setDt_a_competenza_coge(fattura.getDt_registrazione());
        //r.p.????
        fattura.setFl_congelata(new Boolean(false));
        fattura.setTi_associato_manrev(fattura.NON_ASSOCIATO_A_MANDATO);
        fattura.setFl_stampa(new Boolean(false));
        fattura.setStato_coan(fattura.NON_CONTABILIZZATO_IN_COAN);
        // fine r.p.
        fattura = setChangeDataToEur(aUC, fattura);

        /**
         * Gennaro Borriello - (08/11/2004 13.35.27)
         *	Aggiunta proprietà <code>esercizioInScrivania</code>, che verrà utilizzata
         *	per la gestione di isRiportataInScrivania(), in alcuni casi.
         */
        fattura.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));

        try {
            try {
                fattura.setTerzo_uo(findTerzoUO(aUC, fattura));
                fattura.setModalita_uo(findModalita_uo(aUC, fattura));
            } catch (PersistencyException e) {
                throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
            }
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        }

        completeWithCondizioneConsegna(aUC, fattura);
        completeWithModalitaTrasporto(aUC, fattura);
        completeWithModalitaIncasso(aUC, fattura);
        completeWithModalitaErogazione(aUC, fattura);
        fattura =
                (Fattura_attivaBulk) super.inizializzaBulkPerInserimento(aUC, fattura);

        TerzoBulk tb = new TerzoBulk();
        tb.setAnagrafico(new AnagraficoBulk());
        fattura.setCliente(tb);

        Vector sezionaliCaricati = estraeSezionali(aUC, fattura);
        fattura.setSezionali(sezionaliCaricati);
        fattura.setTipo_sezionale(
                (sezionaliCaricati != null && !sezionaliCaricati.isEmpty())
                        ? (it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk) sezionaliCaricati.iterator().next()
                        : null);
        return fattura;
    }

    /**
     * normale
     * PreCondition:
     * Viene richiesta una possibile operazione di modifica.
     * PostCondition:
     * L'OggettoBulk viene caricato con tutti gli oggetti collegati.
     */
//^^@@
    public OggettoBulk inizializzaBulkPerModifica(
            UserContext userContext,
            OggettoBulk bulk)
            throws ComponentException {

        if (bulk == null)
            throw new ComponentException("Attenzione: non esiste alcun documento corrispondente ai criteri di ricerca!");

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bulk;
        if (fattura.getEsercizio() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non è valorizzato! Impossibile proseguire.");

        if (fattura.getEsercizio().intValue() >
                it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
            throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

        fattura = (Fattura_attivaBulk) super.inizializzaBulkPerModifica(userContext, fattura);

        try {
            lockBulk(userContext, fattura);
            setDt_termine_creazione_docamm(userContext, fattura);
        } catch (Throwable t) {
            throw handleException(t);
        }
        fattura.setHa_beniColl(ha_beniColl(userContext, fattura));
        try {

            BulkList dettagli = new BulkList(findDettagli(userContext, fattura));
            fattura.setFattura_attiva_dettColl(dettagli);
            // RP INTRASTAT
            completeWithCondizioneConsegna(userContext, fattura);
            completeWithModalitaTrasporto(userContext, fattura);
            completeWithModalitaIncasso(userContext, fattura);
            completeWithModalitaErogazione(userContext, fattura);
            BulkList dettagliIntrastat = new BulkList(findDettagliIntrastat(userContext, fattura));
            if (dettagliIntrastat != null && !dettagliIntrastat.isEmpty())
                for (Iterator i = dettagliIntrastat.iterator(); i.hasNext(); ) {
                    Fattura_attiva_intraBulk dettaglio = (Fattura_attiva_intraBulk) i.next();
                    dettaglio.setFattura_attiva(fattura);
                    dettaglio.setCondizione_consegnaColl(fattura.getCondizione_consegnaColl());
                    dettaglio.setModalita_trasportoColl(fattura.getModalita_trasportoColl());
                    dettaglio.setModalita_incassoColl(fattura.getModalita_incassoColl());
                    dettaglio.setModalita_erogazioneColl(fattura.getModalita_erogazioneColl());
                }
            fattura.setFattura_attiva_intrastatColl(dettagliIntrastat);

            //fattura.setModalita_uo(findModalita_uo(userContext,fattura));

            Fattura_attiva_rigaBulk riga = null;
            for (java.util.Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                riga = (Fattura_attiva_rigaBulk) i.next();
                //ricavo per ogni riga il tariffario...
                if (fattura.getTi_causale_emissione().equals(fattura.TARIFFARIO)) {
                    riga.setTariffario(findTariffario(userContext, riga));
                }
                impostaCollegamentoCapitoloPerTrovato(userContext, riga);
                TrovatoBulk trovatoBulk = new TrovatoBulk();
                trovatoBulk.setPg_trovato(riga.getPg_trovato());
                trovatoBulk.setInventore("1");
                trovatoBulk.setTitolo("f");

                riga.setTrovato(trovatoBulk);
            }

            getHomeCache(userContext).fetchAll(userContext);

            int dettagliRiportati = 0;
            for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                if (Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(fattura.getTi_causale_emissione()))
                    dettaglio.setInventariato(true);
                if (dettaglio.checkIfRiportata()) {
                    dettaglio.setRiportata(dettaglio.RIPORTATO);
                    fattura.setRiportata(fattura.PARZIALMENTE_RIPORTATO);
                    dettagliRiportati++;
                }
            }
            fattura.setRiportata(getStatoRiporto(userContext, fattura));

            /**
             * Gennaro Borriello - (02/11/2004 15.04.39)
             *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
             */
            fattura.setRiportataInScrivania(getStatoRiportoInScrivania(userContext, fattura));

            /**
             * Gennaro Borriello - (08/11/2004 13.35.27)
             *	Aggiunta proprietà <code>esercizioInScrivania</code>, che verrà utilizzata
             *	per la gestione di isRiportataInScrivania(), in alcuni casi.
             */
            fattura.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            fattura.setAttivoSplitPayment(isAttivoSplitPayment(userContext, fattura.getDt_registrazione()));
            calcoloConsuntivi(userContext, fattura);
            rebuildAccertamenti(userContext, fattura);
            if (fattura instanceof Nota_di_credito_attivaBulk)
                rebuildObbligazioni(userContext, (Nota_di_credito_attivaBulk) fattura);

            //java.util.Collection coll = findListabancheuo(userContext,fattura);
            //fattura.setBanca_uo((coll == null || coll.isEmpty()) ? null : (BancaBulk)new java.util.Vector(coll).firstElement());

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fattura, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(fattura, e);
        }
        Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
                .filter(Scrittura_partita_doppiaHome.class::isInstance)
                .map(Scrittura_partita_doppiaHome.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Partita doppia Home not found"));
        try {
            final Optional<Scrittura_partita_doppiaBulk> scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(fattura);
            if (scritturaOpt.isPresent()) {
                Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
                scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                        .findMovimentiDareColl(userContext, scrittura)));
                scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                        .findMovimentiAvereColl(userContext, scrittura)));
                fattura.setScrittura_partita_doppia(scrittura);
            }
        } catch (PersistencyException e) {
            throw handleException(fattura, e);
        }
        try {
            Utility.createScritturaPartitaDoppiaComponentSession().proposeScritturaPartitaDoppia(userContext, fattura);
        } catch (Exception e) {
            throw handleException(fattura, e);
        }
        return fattura;
    }
//^^@@

    private void impostaCollegamentoCapitoloPerTrovato(UserContext aUC,
                                                       Fattura_attiva_rigaBulk riga) throws ComponentException {
        if (riga.getAccertamento_scadenzario() != null && riga.getAccertamento_scadenzario().getPg_accertamento() != null) {
            riga.setCollegatoCapitoloPerTrovato(isVocePerTrovati(aUC, riga.getAccertamento_scadenzario()));
        } else {
            if (riga instanceof Nota_di_credito_attiva_rigaBulk) {
                Nota_di_credito_attiva_rigaBulk rigaNc = (Nota_di_credito_attiva_rigaBulk) riga;
                if (rigaNc.getObbligazione_scadenzario() != null && rigaNc.getObbligazione_scadenzario().getPg_obbligazione() != null) {
                    riga.setCollegatoCapitoloPerTrovato(rigaNc.getObbligazione_scadenzario().getObbligazione().getElemento_voce().isVocePerTrovati());
                }
            }
        }
    }
//^^@@

    /**
     * normale
     * PreCondition:
     * Viene richiesta una possibile operazione di ricerca.
     * PostCondition:
     * L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per l'operazione inserimento criteri di ricerca.
     */
//^^@@
    public OggettoBulk inizializzaBulkPerRicerca(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bulk;
        TerzoBulk nt = new TerzoBulk();
        nt.setAnagrafico(new AnagraficoBulk());
        fattura.setCliente(nt);
        fattura = setChangeDataToEur(aUC, fattura);
        return (super.inizializzaBulkPerRicerca(aUC, fattura));
    }

    /**
     * normale
     * PreCondition:
     * Viene richiesta una possibile operazione di ricerca libera.
     * PostCondition:
     * L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per l'utilizzo come prototipo in in una operazione di ricerca libera
     */
//^^@@
    public OggettoBulk inizializzaBulkPerRicercaLibera(
            UserContext userContext,
            OggettoBulk bulk)
            throws ComponentException {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) super.inizializzaBulkPerRicercaLibera(userContext, bulk);

        TerzoBulk cliente = new TerzoBulk();
        cliente.setAnagrafico(new AnagraficoBulk());
        fa.setCliente(cliente);

        try {
            fa.setSezionali(findSezionali(userContext, fa));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fa, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(fa, e);
        }
        fa.setSezionaliFlag(null);

        return fa;
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la protocollazione o la ristampa dei documenti attivi
     * PostCondition:
     * Viene impostato un punto di ripristino
     */

    public void inizializzaSelezionePerStampa(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        try {
            setSavepoint(userContext, "STAMPA_IVA_FA");
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }

    public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        if (trovato != null) {
            RicercaTrovato ricercaTrovato;
            try {
                ricercaTrovato = new RicercaTrovato();
                return ricercaTrovato.ricercaDatiTrovato(userContext, trovato);
            } catch (FileNotFoundException e) {
                throw new ApplicationException("File in configurazione non trovato " + e.getMessage());
            } catch (IOException e) {
                throw new ApplicationException("Eccezione di IO " + e.getMessage());
            } catch (ComponentException e) {
                throw e;
            } catch (Exception e) {
                throw new ApplicationException("Eccezione generica " + e.getMessage());
            }
        }
        return new TrovatoBulk();
    }

    private void inserisciDatiPerProtocollazioneIva(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            Long pg,
            Integer count,
            java.sql.Timestamp dataStampa)
            throws PersistencyException, ComponentException {

        try {
            lockBulk(userContext, fatturaAttiva);
        } catch (OutdatedResourceException e) {
            throw new ApplicationException("La fattura è stata modificata, rieffettuare la ricerca.");
        } catch (BusyResourceException e) {
            throw new ApplicationException("Fattura al momento utilizzata da un altro utente.");
        }

        Vsx_rif_protocollo_ivaHome home = (Vsx_rif_protocollo_ivaHome) getHome(userContext, Vsx_rif_protocollo_ivaBulk.class);
        Vsx_rif_protocollo_ivaBulk vista = new Vsx_rif_protocollo_ivaBulk();
        vista.setPg_call(pg);
        vista.completeFrom(fatturaAttiva);
        vista.setDt_stampa(dataStampa);
        vista.setPar_num(count);
        //N.B. Questo setUtente ha scopi diversi da setUser
        vista.setUtente(userContext.getUser());
        vista.setUser(userContext.getUser());
        vista.setToBeCreated();
        home.insert(vista, userContext);
        //ATTIVARE -- SOLO -- IN CASO DI DEBUG
        //try {
        //java.sql.PreparedStatement ps = getConnection(userContext).prepareStatement("commit");
        //LoggableStatement.execute(ps);
        //try{ps.close();}catch( java.sql.SQLException e ){};
        //} catch (SQLException e) {
        //throw handleException(e);
        //}
    }


//^^@@

    private void inserisciDatiPerStampaIva(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            Long pg,
            Integer count)
            throws PersistencyException, ComponentException {

        try {
            lockBulk(userContext, fatturaAttiva);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw new ApplicationException("Fattura al momento utilizzata da un'altro utente, riprovare in un secondo momento.");
        } catch (Throwable e) {
            throw new PersistencyException(e, fatturaAttiva);
        }

        V_stm_paramin_ft_attivaHome home = (V_stm_paramin_ft_attivaHome) getHome(userContext, V_stm_paramin_ft_attivaBulk.class);
        V_stm_paramin_ft_attivaBulk vista = new V_stm_paramin_ft_attivaBulk();
        vista.completeFrom(fatturaAttiva);
        vista.setId_report(java.math.BigDecimal.valueOf(pg.longValue()));
        vista.setSequenza(java.math.BigDecimal.valueOf(count.longValue()));
        vista.setGruppo("A");
        vista.setTipologia_riga("D");
        vista.setDescrizione("Stampa fattura attiva, nota credito-debito su FA");
        vista.setUser(userContext.getUser());
        vista.setToBeCreated();
        home.insert(vista, userContext);
        //ATTIVARE -- SOLO -- IN CASO DI DEBUG
        //try {
        //java.sql.PreparedStatement ps = getConnection(userContext).prepareStatement("commit");
        //LoggableStatement.execute(ps);
        //try{ps.close();}catch( java.sql.SQLException e ){};
        //} catch (SQLException e) {
        //throw handleException(e);
        //}
    }

    /**
     * Validazione riga.
     * PreCondition:
     * validaRiga non superata.
     * PostCondition:
     * Obbligo di modifica o annullamento riga.
     * Tutti i controlli superati.
     * PreCondition:
     * validaRiga superato
     * PostCondition:
     * Consente il passaggio alla riga seguente.
     */
//^^@@
    public void inserisciRiga(UserContext aUC, Fattura_attiva_rigaBulk rigaFattura) throws ComponentException {

        validaRiga(aUC, rigaFattura);
    }

    private void inserisciTuttiDatiPerProtocollazioneIva(UserContext userContext, SQLBuilder selectFrom, Filtro_ricerca_doc_amm_protocollabileVBulk filtro, Long pg) throws PersistencyException, ComponentException {
        Fattura_attiva_IHome fattura_home = (Fattura_attiva_IHome) getHome(userContext, Fattura_attiva_IBulk.class);
        int count = 0;
        for (Iterator fatture = fattura_home.fetchAll(selectFrom).iterator(); fatture.hasNext(); ) {
            Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fatture.next();
            try {
                lockBulk(userContext, fatturaAttiva);
            } catch (it.cnr.jada.bulk.BusyResourceException e) {
                throw new ApplicationException("Fattura al momento utilizzata da un'altro utente, riprovare in un secondo momento.");
            } catch (OutdatedResourceException e) {
                throw new ApplicationException("Fattura al momento utilizzata da un'altro utente, riprovare in un secondo momento.");
            }
            Vsx_rif_protocollo_ivaHome home = (Vsx_rif_protocollo_ivaHome) getHome(userContext, Vsx_rif_protocollo_ivaBulk.class);
            Vsx_rif_protocollo_ivaBulk vista = new Vsx_rif_protocollo_ivaBulk();
            vista.setPg_call(pg);
            vista.completeFrom(fatturaAttiva);
            vista.setDt_stampa(filtro.getDt_stampa());
            vista.setPar_num(count++);
            //N.B. Questo setUtente ha scopi diversi da setUser
            vista.setUtente(userContext.getUser());
            vista.setUser(userContext.getUser());
            vista.setToBeCreated();
            home.insert(vista, userContext);
        }
    }

    private void inserisciTuttiDatiPerRistampaIva(UserContext userContext, SQLBuilder selectFrom, Filtro_ricerca_doc_amm_ristampabileVBulk filtro, Long pg) throws PersistencyException, ComponentException {
        Fattura_attiva_IHome fattura_home = (Fattura_attiva_IHome) getHome(userContext, Fattura_attiva_IBulk.class);
        int count = 0;
        for (Iterator fatture = fattura_home.fetchAll(selectFrom).iterator(); fatture.hasNext(); ) {
            Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fatture.next();
            try {
                lockBulk(userContext, fatturaAttiva);
            } catch (it.cnr.jada.bulk.BusyResourceException e) {
                throw new ApplicationException("Fattura al momento utilizzata da un'altro utente, riprovare in un secondo momento.");
            } catch (OutdatedResourceException e) {
                throw new ApplicationException("Fattura al momento utilizzata da un'altro utente, riprovare in un secondo momento.");
            }
            V_stm_paramin_ft_attivaHome home = (V_stm_paramin_ft_attivaHome) getHome(userContext, V_stm_paramin_ft_attivaBulk.class);
            V_stm_paramin_ft_attivaBulk vista = new V_stm_paramin_ft_attivaBulk();
            vista.completeFrom(fatturaAttiva);
            vista.setId_report(java.math.BigDecimal.valueOf(pg.longValue()));
            vista.setSequenza(java.math.BigDecimal.valueOf(count++));
            vista.setGruppo("A");
            vista.setTipologia_riga("D");
            vista.setDescrizione("Stampa fattura attiva, nota credito-debito su FA");
            vista.setUser(userContext.getUser());
            vista.setToBeCreated();
            home.insert(vista, userContext);
        }
    }

    private void inserisciTuttiDatiPerStampaIva(
            UserContext userContext,
            SQLBuilder selectFrom,
            Filtro_ricerca_doc_amm_protocollabileVBulk filtro,
            Long pg)
            throws PersistencyException, ComponentException {

        inserisciTuttiDatiPerRistampaIva(
                userContext,
                selectFrom,
                null,
                pg);
    }

    /**
     * Controllo l'esercizio coep
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    private boolean isEsercizioCoepChiusoFor(UserContext userContext, Fattura_attivaBulk documento, Integer esercizio) throws ComponentException {
        LoggableStatement cs = null;
        String status = null;

        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB200.isChiusuraCoepDef(?,?)}", false, this.getClass());

            cs.registerOutParameter(1, java.sql.Types.VARCHAR);
            cs.setObject(2, esercizio);
            cs.setObject(3, documento.getCd_cds());

            cs.executeQuery();

            status = new String(cs.getString(1));

            if (status.compareTo("Y") == 0)
                return true;

        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }

        return false;
    }

    private Fattura_attivaBulk manageDeletedElements(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            OptionRequestParameter status)
            throws ComponentException {

        if (fatturaAttiva != null) {
            manageDocumentiContabiliCancellati(
                    userContext,
                    fatturaAttiva,
                    status);
            if (fatturaAttiva.getDettagliCancellati() != null &&
                    !fatturaAttiva.getDettagliCancellati().isEmpty()) {
                boolean eliminaBuoniScaricoPrecedenti = false;
                for (Iterator i = fatturaAttiva.getDettagliCancellati().iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                    if (dettaglio instanceof Fattura_attiva_rigaIBulk &&
                            dettaglio.isInventariato()) {
                        if (dettaglio.getCrudStatus() == OggettoBulk.UNDEFINED)
                            deleteAssociazioniInventarioWith(userContext, (Fattura_attiva_rigaIBulk) dettaglio);
                        else if (dettaglio.isToBeDeleted())
                            eliminaBuoniScaricoPrecedenti = true;
                    }
                }
                if (eliminaBuoniScaricoPrecedenti) {
                    //chiamare gestione cancellazione
                }
            }
        }
        return fatturaAttiva;
    }
//^^@@

    private void manageDocumentiContabiliCancellati(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            OptionRequestParameter status)
            throws ComponentException {

        if (fatturaAttiva != null) {
            if (fatturaAttiva.getDocumentiContabiliCancellati() != null &&
                    !fatturaAttiva.getDocumentiContabiliCancellati().isEmpty()) {

                if (fatturaAttiva instanceof Fattura_attiva_IBulk) {
                    PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
                            userContext,
                            fatturaAttiva.getFattura_attiva_accertamentiHash().keys());
                    Vector scadenzeConfermate = new Vector();
                    java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
                    while (e.hasMoreElements()) {
                        OggettoBulk obj = (OggettoBulk) e.nextElement();
                        if (obj instanceof AccertamentoBulk)
                            scadenzeConfermate.add(obj);
                    }
                    aggiornaAccertamentiSuCancellazione(
                            userContext,
                            fatturaAttiva,
                            fatturaAttiva.getDocumentiContabiliCancellati().elements(),
                            scadenzeConfermate,
                            status);
                } else if (fatturaAttiva instanceof Nota_di_credito_attivaBulk && ((Nota_di_credito_attivaBulk) fatturaAttiva).getObbligazioniHash() != null) {
                    PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
                            userContext,
                            ((Nota_di_credito_attivaBulk) fatturaAttiva).getObbligazioniHash().keys());
                    Vector scadenzeConfermate = new Vector();
                    java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
                    while (e.hasMoreElements()) {
                        OggettoBulk obj = (OggettoBulk) e.nextElement();
                        if (obj instanceof ObbligazioneBulk)
                            scadenzeConfermate.add(obj);
                    }

                    aggiornaObbligazioniSuCancellazione(
                            userContext,
                            (Nota_di_credito_attivaBulk) fatturaAttiva,
                            fatturaAttiva.getDocumentiContabiliCancellati().elements(),
                            scadenzeConfermate,
                            status);
                }
            }
        }
    }
//^^@@

    /**
     * Validazione documento.
     * PreCondition:
     * validaFattura non superata.
     * PostCondition:
     * Non  viene consentita la registrazione della fattura.
     * Tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato
     * PostCondition:
     * Viene consentito il salvataggio del documento.
     */
//^^@@
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {

        return modificaConBulk(userContext, bulk, null);
    }

    /**
     * Validazione documento.
     * PreCondition:
     * validaFattura non superata.
     * PostCondition:
     * Non  viene consentita la registrazione della fattura.
     * Tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato
     * PostCondition:
     * Viene consentito il salvataggio del documento.
     */
//^^@@
    public OggettoBulk modificaConBulk(
            UserContext userContext,
            OggettoBulk bulk,
            OptionRequestParameter status)
            throws ComponentException {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bulk;
        fattura = (Fattura_attivaBulk) calcoloConsuntivi(userContext, fattura);

        try {
            if (fattura instanceof Fattura_attiva_IBulk) {
                if (hasFatturaAttivaARowToBeInventoried(userContext, fattura)) {
                    verificaEsistenzaEdAperturaInventario(userContext, fattura);
                    if (fattura.hasCompetenzaCOGEInAnnoPrecedente())
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: per le date competenza indicate non è possibile inventariare i beni.");

                    if (hasFatturaAttivaARowNotInventoried(userContext, fattura))
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: è necessario inventariare tutti i dettagli.");
                }
            }
            validaFattura(userContext, fattura);
        } catch (it.cnr.jada.comp.ApplicationException e) {
            throw handleException(bulk, e);
        }

// Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        if (fattura.getDefferredSaldi() != null)
            aTempDiffSaldi = (PrimaryKeyHashMap) fattura.getDefferredSaldi().clone();

        manageDeletedElements(userContext, fattura, status);

        aggiornaAccertamenti(userContext, fattura, status);

        if (bulk instanceof Nota_di_credito_attivaBulk) {
            Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk) fattura;
            aggiornaRigheFatturaAttivaDiOrigine(userContext, ndc);
            aggiornaObbligazioni(userContext, ndc, status);
        }

        if (bulk instanceof Nota_di_debito_attivaBulk) {
            Nota_di_debito_attivaBulk ndd = (Nota_di_debito_attivaBulk) fattura;
            aggiornaRigheFatturaAttivaDiOrigine(userContext, ndd);
        }

        prepareScarichiInventario(userContext, fattura);

        /*
         * Se il documento non era modificabile nei suoi elementi principali, ma si è solo proceduto a
         * sdoppiare una riga di dettaglio allora la ricontabilizzazione in COAN e COGE non è necessaria
         */
        boolean aggiornaStatoCoge = false;
        try {
            Fattura_attivaBulk fatturaAttivaDB = (Fattura_attivaBulk) getTempHome(userContext, Fattura_attivaBulk.class).findByPrimaryKey(
                    new Documento_amministrativo_attivoBulk(
                            fattura.getCd_cds(),
                            fattura.getCd_unita_organizzativa(),
                            fattura.getEsercizio(),
                            fattura.getPg_fattura_attiva()
                    ));
            if (!Utility.equalsNull(fattura.getTi_fattura(), fatturaAttivaDB.getTi_fattura()) ||
                    !Utility.equalsNull(fattura.getFl_congelata(), fatturaAttivaDB.getFl_congelata()) ||
                    !Utility.equalsNull(fattura.getTi_causale_emissione(), fatturaAttivaDB.getTi_causale_emissione()) ||
                    !Utility.equalsNull(fattura.getCd_terzo(), fatturaAttivaDB.getCd_terzo())
                    )
                aggiornaStatoCoge = true;
            if (!aggiornaStatoCoge && fattura.getFattura_attiva_dettColl().size() > 0) {
                for (Iterator iter = fattura.getFattura_attiva_dettColl().iterator(); iter.hasNext() && !aggiornaStatoCoge; ) {
                    Fattura_attiva_rigaBulk fattura_riga = (Fattura_attiva_rigaBulk) iter.next();
                    Fattura_attiva_rigaBulk fatturaDB_riga = caricaRigaDB(userContext, fattura_riga);
                    if (fatturaDB_riga != null) {
                        if (!Utility.equalsNull(fattura_riga.getIm_totale_divisa(), fatturaDB_riga.getIm_totale_divisa().setScale(2)) ||
                                !Utility.equalsNull(fattura_riga.getIm_imponibile(), fatturaDB_riga.getIm_imponibile().setScale(2)) ||
                                !Utility.equalsNull(fattura_riga.getIm_iva(), fatturaDB_riga.getIm_iva().setScale(2)) ||
                                (fatturaDB_riga.getIm_diponibile_nc() != null &&
                                        !Utility.equalsNull(fattura_riga.getIm_diponibile_nc(), fatturaDB_riga.getIm_diponibile_nc().setScale(2))) ||
                                !Utility.equalsBulkNull(fattura_riga.getAccertamento_scadenzario(), fatturaDB_riga.getAccertamento_scadenzario()) ||
                                !Utility.equalsNull(fattura_riga.getDt_da_competenza_coge(), fatturaDB_riga.getDt_da_competenza_coge()) ||
                                !Utility.equalsNull(fattura_riga.getDt_a_competenza_coge(), fatturaDB_riga.getDt_a_competenza_coge())
                                )
                            aggiornaStatoCoge = true;
                    }
                }
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        }
        if (aggiornaStatoCoge) {
            if (!(!fattura.isDocumentoModificabile() && fattura.isDetailDoubled())) {
                //Aggiornamenti degli stati COGE e COAN
                if (fattura.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(fattura.getStato_coan())) {
                    fattura.setStato_coan(fattura.DA_RICONTABILIZZARE_IN_COAN);
                    fattura.setToBeUpdated();
                }
                if (fattura.REGISTRATO_IN_COGE.equalsIgnoreCase(fattura.getStato_coge())) {
                    fattura.setStato_coge(fattura.DA_RIREGISTRARE_IN_COGE);
                    fattura.setToBeUpdated();
                }
            }
        }
        fattura = (Fattura_attivaBulk) super.modificaConBulk(userContext, fattura);

        aggiornaScarichiInventario(userContext, fattura);
        String messaggio = aggiornaAssociazioniInventario(userContext, fattura);

        // Restore dell'hash map dei saldi
        if (fattura.getDefferredSaldi() != null)
            fattura.getDefferredSaldi().putAll(aTempDiffSaldi);

        /*
         * Se il documento non era modificabile nei suoi elementi principali, ma si è solo proceduto a
         * sdoppiare una riga di dettaglio allora il controllo sulla chiusura dell'esercizio del documento
         * non è necessario
         */
        if (!fattura.isDocumentoModificabile() && fattura.isDetailDoubled())
            return fattura;

        aggiornaCogeCoanDocAmm(userContext, fattura);

        aggiornaDataEsigibilitaIVA(userContext, fattura, "N");
        try {
            if (!verificaStatoEsercizio(
                    userContext,
                    new EsercizioBulk(
                            fattura.getCd_cds(),
                            ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            throw handleException(bulk, e);
        }
        controllaQuadraturaInventario(userContext, fattura);
        if (messaggio != null)
            return asMTU(fattura, messaggio);

        return fattura;
    }

    private Fattura_attiva_rigaBulk caricaRigaDB(UserContext userContext, Fattura_attiva_rigaBulk fatturaAttivaRiga) throws ComponentException, PersistencyException {
        Fattura_attiva_rigaIHome home = (Fattura_attiva_rigaIHome) getTempHome(userContext, Fattura_attiva_rigaIBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_unita_organizzativa());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, fatturaAttivaRiga.getEsercizio());
        sql.addClause("AND", "pg_fattura_attiva", SQLBuilder.EQUALS, fatturaAttivaRiga.getPg_fattura_attiva());
        sql.addClause("AND", "progressivo_riga", SQLBuilder.EQUALS, fatturaAttivaRiga.getProgressivo_riga());
        SQLBroker broker = home.createBroker(sql);
        if (broker.next())
            return (Fattura_attiva_rigaBulk) broker.fetch(Fattura_attiva_rigaIBulk.class);
        return null;
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la protocollazione o la ristampa dei documenti attivi
     * e vengono selezionate solo alcuni documenti
     * PostCondition:
     * Vegono inseriti nell'apposita tabella i parametri per la ristampa o la protocollazione
     * che verranno riusati dalla procedura
     * Deselezione di elementi.
     * PreCondition:
     * Viene richiesta la protocollazione o la ristampa dei documenti attivi
     * e vengono deselezionate solo alcuni documenti già precedentemente selezionati
     * PostCondition:
     * Vegono eliminati dall'apposita tabella i parametri per la ristampa o la protocollazione
     * in modo che non verranno usati dalle relative procedure
     */

    public Integer modificaSelezionePerStampa(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            OggettoBulk[] fatture,
            BitSet old_ass,
            BitSet ass,
            Long pgProtocollazione,
            Integer offSet,
            Long pgStampa,
            java.sql.Timestamp dataStampa)
            throws ComponentException {

        try {
            for (int i = 0; i < fatture.length; i++) {
                if (old_ass.get(i) != ass.get(i)) {
                    Fattura_attivaBulk fattura = (Fattura_attivaBulk) fatture[i];
                    if (ass.get(i)) {
                        offSet = preparaProtocollazione(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
                    } else {
                        if (pgProtocollazione != null)
                            cancellaDatiPerProtocollazioneIva(userContext, fattura, pgProtocollazione);
                        cancellaDatiPerStampaIva(userContext, fattura, pgStampa);
                    }
                }
            }
            return offSet;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    public void preparaProtocollazioneEProtocolla(UserContext userContext, Long pgProtocollazione,
                                                  Integer offSet, Long pgStampa, java.sql.Timestamp dataStampa,
                                                  Fattura_attivaBulk fattura) throws PersistencyException,
            ComponentException {
        preparaProtocollazione(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
        protocolla(userContext, dataStampa, pgProtocollazione);
    }

    private Integer preparaProtocollazione(UserContext userContext, Long pgProtocollazione,
                                           Integer offSet, Long pgStampa, java.sql.Timestamp dataStampa,
                                           Fattura_attivaBulk fattura) throws PersistencyException,
            ComponentException {
        if (pgProtocollazione != null)
            inserisciDatiPerProtocollazioneIva(userContext, fattura, pgProtocollazione, offSet, dataStampa);
        inserisciDatiPerStampaIva(userContext, fattura, pgStampa, offSet);
        offSet = new Integer(offSet.intValue() + 1);
        return offSet;
    }

    /*private void prepareScarichiInventario(UserContext userContext,Fattura_attivaBulk fattura_attiva) throws ComponentException {

	if (fattura_attiva != null) {
		CarichiInventarioTable carichiInventarioHash = fattura_attiva.getCarichiInventarioHash();
		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
				if (buonoCS instanceof Buono_scaricoBulk && buonoCS.isTemporaneo()) {
					Buono_scaricoBulk bc = (Buono_scaricoBulk)buonoCS;
					it.cnr.jada.bulk.PrimaryKeyHashtable ht = bc.getDettagliRigheHash();
					if (ht != null && !ht.isEmpty()) {
						it.cnr.jada.bulk.PrimaryKeyHashtable newHt = new it.cnr.jada.bulk.PrimaryKeyHashtable();
						for (java.util.Enumeration k = ht.keys(); k.hasMoreElements();)
							((Fattura_attiva_rigaBulk)k.nextElement()).setFattura_attiva(fattura_attiva);
						ht = new it.cnr.jada.bulk.PrimaryKeyHashtable(ht);
						for (Iterator i = fattura_attiva.getFattura_attiva_dettColl().iterator(); i.hasNext();) {
							Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk)i.next();
							BulkList bl = (BulkList)ht.get(riga);
							if (bl != null)
								newHt.put(riga, bl);
						}
						bc.setDettagliRigheHash(newHt);
					}
				}
			}
		}
	}
}*/
    private void prepareScarichiInventario(UserContext userContext, Fattura_attivaBulk fattura_attiva) throws ComponentException {

        if (fattura_attiva != null) {
            CarichiInventarioTable carichiInventarioHash = fattura_attiva.getCarichiInventarioHash();
            if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
                for (java.util.Enumeration e = ((CarichiInventarioTable) carichiInventarioHash.clone()).keys(); e.hasMoreElements(); ) {
                    Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) e.nextElement();
                    if (buonoCS instanceof Buono_carico_scaricoBulk) {// && buonoCS.isByFattura()) {
                        it.cnr.jada.bulk.PrimaryKeyHashtable ht = buonoCS.getDettagliRigheHash();
                        if (ht != null && !ht.isEmpty()) {
                            it.cnr.jada.bulk.PrimaryKeyHashtable newHt = new it.cnr.jada.bulk.PrimaryKeyHashtable();
                            for (java.util.Enumeration k = ht.keys(); k.hasMoreElements(); )
                                ((Fattura_attiva_rigaBulk) k.nextElement()).setFattura_attiva(fattura_attiva);
                            ht = new it.cnr.jada.bulk.PrimaryKeyHashtable(ht);
                            for (Iterator i = fattura_attiva.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                                BulkList bl = (BulkList) ht.get(riga);
                                if (bl != null)
                                    newHt.put(riga, bl);
                            }
                            buonoCS.setDettagliRigheHash(newHt);
                        }
                    }
                }
            }
        }
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la prtocollazione iva per tutti i parametri inseriti
     * relativi al processo di protocollazione 'pg'.
     * PostCondition:
     * Viene richiamata la procedura di protocollazione.
     */

    public void protocolla(
            UserContext context,
            java.sql.Timestamp dataStampa,
            Long pg)
            throws it.cnr.jada.comp.ComponentException {

        validaDataStampaProtocollazioneIVA(context, dataStampa);
        try {
            callRifProtocolloIVA(context, pg);

            cancellaDatiPerProtocollazioneIva(context, null, pg);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    private void rebuildAccertamenti(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException {

        BulkList righeFattura = fatturaAttiva.getFattura_attiva_dettColl();
        if (righeFattura != null) {

            java.util.HashMap storniHashMap = null;
            java.util.HashMap addebitiHashMap = null;
            if (fatturaAttiva instanceof Fattura_attiva_IBulk) {
                storniHashMap = caricaStorniPer(aUC, righeFattura);
                ((Fattura_attiva_IBulk) fatturaAttiva).setStorniHashMap(storniHashMap);
                addebitiHashMap = caricaAddebitiPer(aUC, righeFattura);
                ((Fattura_attiva_IBulk) fatturaAttiva).setAddebitiHashMap(addebitiHashMap);
            }
            for (Iterator i = righeFattura.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                Accertamento_scadenzarioBulk scadenza = riga.getAccertamento_scadenzario();
                if (scadenza != null) {
                    if (fatturaAttiva.getFattura_attiva_accertamentiHash() == null ||
                            fatturaAttiva.getFattura_attiva_accertamentiHash().getKey(scadenza) == null) {
                        scadenza = caricaScadenzaAccertamentoPer(aUC, scadenza);
                    }
                    fatturaAttiva.addToFattura_attiva_accertamentiHash(scadenza, riga);
                    if (riga instanceof Fattura_attiva_rigaIBulk) {
                        Fattura_attiva_rigaIBulk rigaFP = (Fattura_attiva_rigaIBulk) riga;
                        java.math.BigDecimal impStorni = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                        java.math.BigDecimal impAddebiti = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                        if (storniHashMap != null) {
                            impStorni = calcolaTotalePer((Vector) storniHashMap.get(riga), false);
                            rigaFP.setIm_totale_storni(impStorni);
                        }
                        if (addebitiHashMap != null) {
                            impAddebiti = calcolaTotalePer((Vector) addebitiHashMap.get(riga), false);
                            rigaFP.setIm_totale_addebiti(impAddebiti);
                        }
                        rigaFP.setSaldo(riga.getIm_imponibile().add(riga.getIm_iva()).subtract(impStorni).add(impAddebiti));
                    }
                }
            }
            if (!(fatturaAttiva instanceof Fattura_attiva_IBulk) && fatturaAttiva.getFattura_attiva_accertamentiHash() != null) {
                for (java.util.Enumeration e = fatturaAttiva.getFattura_attiva_accertamentiHash().keys(); e.hasMoreElements(); ) {
                    Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) e.nextElement();
                    fatturaAttiva.addToFattura_attiva_ass_totaliMap(
                            scadenza,
                            calcolaTotalePer((Vector) fatturaAttiva.getFattura_attiva_accertamentiHash().get(scadenza), fatturaAttiva.quadraturaInDeroga()));
                }
            }
        }

        try {
            getHomeCache(aUC).fetchAll(aUC);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fatturaAttiva, e);
        }
    }

    private void rebuildObbligazioni(
            UserContext userContext,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws ComponentException {

        if (notaDiCredito == null) return;

        BulkList righeNdC = notaDiCredito.getFattura_attiva_dettColl();
        if (righeNdC != null) {
            for (Iterator i = righeNdC.iterator(); i.hasNext(); ) {
                Nota_di_credito_attiva_rigaBulk riga = (Nota_di_credito_attiva_rigaBulk) i.next();
                Obbligazione_scadenzarioBulk scadenza = riga.getObbligazione_scadenzario();
                if (scadenza != null) {
                    if (notaDiCredito.getObbligazioniHash() == null ||
                            notaDiCredito.getObbligazioniHash().getKey(scadenza) == null) {
                        scadenza = caricaScadenzaObbligazionePer(userContext, scadenza);
                    }
                    notaDiCredito.addToObbligazioni_scadenzarioHash(scadenza, riga);
                }
            }
        }
        if (notaDiCredito.getObbligazioni_scadenzarioHash() != null) {
            for (java.util.Enumeration e = notaDiCredito.getObbligazioni_scadenzarioHash().keys(); e.hasMoreElements(); ) {
                Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) e.nextElement();
                notaDiCredito.addToFattura_attiva_ass_totaliMap(scadenza, calcolaTotalePer((Vector) notaDiCredito.getObbligazioni_scadenzarioHash().get(scadenza), notaDiCredito.quadraturaInDeroga()));
            }
        }

        try {
            getHomeCache(userContext).fetchAll(userContext);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(notaDiCredito, e);
        }
    }
//^^@@

    private Fattura_attivaBulk resetChangeData(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        // Viene chiamato solo quando la valuta della fattura è null
        fattura_attiva.setInizio_validita_valuta(null);
        fattura_attiva.setFine_validita_valuta(null);
        fattura_attiva.setCambio(new java.math.BigDecimal(0));
        return fattura_attiva;
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Viene richiesto il riporto del documento amministrativo all'esercizio successivo.
     * PostCondition:
     * Viene richiamata la procedura DB corretta
     */
//^^@@
    public IDocumentoAmministrativoBulk riportaAvanti(
            UserContext userContext,
            IDocumentoAmministrativoBulk docAmm,
            OptionRequestParameter status)
            throws ComponentException {

//	fattura = (Fattura_attivaBulk)modificaConBulk(userContext, fattura, status);

        try {
            return getRiportoComponentSession(userContext).riportaAvanti(userContext, docAmm, status);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesto il riporto del documento amministrativo all'esercizio precedente.
     * PostCondition:
     * Viene richiamata la procedura DB corretta
     */
//^^@@
    public IDocumentoAmministrativoBulk riportaIndietro(
            UserContext userContext,
            IDocumentoAmministrativoBulk docAmm)
            throws ComponentException {

        try {
            return getRiportoComponentSession(userContext).riportaIndietro(userContext, docAmm);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Rollback to savePoint
     * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
     * modifiche apportate al doc. amministrativo che ha aperto il compenso
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

    private ObbligazioneBulk salvaObbligazione(
            UserContext context,
            ObbligazioneBulk obbligazione)
            throws ComponentException {

        if (obbligazione != null) {
            try {
                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)
                        it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
                                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class);
                try {
                    return (ObbligazioneBulk) h.modificaConBulk(context, obbligazione);
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    throw new it.cnr.jada.comp.ApplicationException("Impegno \"" + obbligazione.getDs_obbligazione() + "\": " + e.getMessage(), e);
                }
            } catch (java.rmi.RemoteException e) {
                throw handleException(obbligazione, e);
            } catch (javax.ejb.EJBException e) {
                throw handleException(obbligazione, e);
            }
        }
        return null;
    }
//^^@@

    protected it.cnr.jada.persistency.sql.Query select(
            UserContext userContext,
            CompoundFindClause clauses,
            OggettoBulk bulk)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {

        it.cnr.jada.persistency.sql.SQLBuilder sql =
                (it.cnr.jada.persistency.sql.SQLBuilder) super.select(userContext, clauses, bulk);
        TerzoBulk cliente = ((Fattura_attivaBulk) bulk).getCliente();
        sql.addSQLClause("AND", "FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, ((Fattura_attivaBulk) bulk).getEsercizio());
        if (cliente != null) {
            sql.addTableToHeader("TERZO");
            sql.addSQLJoin("TERZO.CD_TERZO", "FATTURA_ATTIVA.CD_TERZO");
            sql.addSQLClause("AND", "FATTURA_ATTIVA.CD_TERZO", sql.EQUALS, cliente.getCd_terzo());
            sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", sql.EQUALS, cliente.getCd_precedente());
        }
        return sql;
    }
//^^@@

    /**
     * Vengono richiesti i dati relativi ad una banca dell'uo
     * PreCondition:
     * Vengono richiesti i dati relativi ad una banca dell'uo
     * PostCondition:
     * Aggiunge delle condizioni supplementari codice dell'uo, del tipo di pagamento scelto adll'uo
     * e filtra le banche annullate
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectBanca_uoByClause(UserContext aUC, Fattura_attivaBulk fatturaAttiva, it.cnr.contab.anagraf00.core.bulk.BancaBulk banca, CompoundFindClause clauses)
            throws ComponentException {

        try {
            BancaHome bancaHome = (BancaHome) getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
            return bancaHome.selectBancaFor(
                    fatturaAttiva.getModalita_pagamento_uo(),
                    fatturaAttiva.getTerzo_uo().getCd_terzo());
        } catch (Exception e) {
            throw new it.cnr.jada.comp.ApplicationException("Impossibile continuare. Informazioni bancarie relative all'UO non caricate");
        }
    }
//^^@@

    /**
     * Vengono richiesti i dati relativi ad una banca del cliente
     * PreCondition:
     * Vengono richiesti i dati relativi ad una banca del cliente
     * PostCondition:
     * Aggiunge delle condizioni supplementari codice del cliente, del tipo di pagamento scelto
     * e filtra le banche annullate
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectBancaByClause(UserContext aUC, Nota_di_credito_attivaBulk notaDiCredito, it.cnr.contab.anagraf00.core.bulk.BancaBulk banca, CompoundFindClause clauses)
            throws ComponentException {

        BancaHome bancaHome = (BancaHome) getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
        return bancaHome.selectBancaFor(
                notaDiCredito.getModalita_pagamento(),
                notaDiCredito.getCliente().getCd_terzo());
    }
//^^@@

    /**
     * Vengono richiesti i dati relativi ad cliente
     * PreCondition:
     * Vengono richiesti i dati relativi ad cliente
     * PostCondition:
     * Aggiunge delle condizioni supplementari impostate dall'utente
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectClienteByClause(
            UserContext aUC,
            Fattura_attivaBulk fatturaAttiva,
            TerzoBulk cliente,
            CompoundFindClause clauses)
            throws ComponentException {
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, cliente, "V_TERZO_CF_PI").createSQLBuilder();
        sql.addTableToHeader("ANAGRAFICO");
        sql.addSQLJoin("ANAGRAFICO.CD_ANAG", "V_TERZO_CF_PI.CD_ANAG");
        sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, cliente.getCd_terzo());

	/*sql.openParenthesis( "AND");
	sql.addSQLClause("AND","(ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS,"I");
	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA", sql.ISNOTNULL, true);
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE", sql.ISNOTNULL, true);
	sql.closeParenthesis();
	sql.openParenthesis("OR");
	sql.addSQLClause("OR","ANAGRAFICO.FL_NON_OBBLIG_P_IVA", sql.EQUALS,"Y");
	sql.addSQLClause("AND","ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS,"I");
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE", sql.ISNOTNULL, true);
	sql.closeParenthesis();
	sql.addSQLClause("OR","ANAGRAFICO.TI_ITALIANO_ESTERO", sql.NOT_EQUALS,"I");
	sql.closeParenthesis();
    */
        //modifica aggiunta per Art.35 DL n.223/2006
	/*
	sql.openParenthesis( "AND");
	sql.addSQLClause("AND","(ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS,"I");
	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA", sql.ISNOTNULL, true);
	sql.closeParenthesis();
	sql.addSQLClause("OR","ANAGRAFICO.TI_ITALIANO_ESTERO", sql.NOT_EQUALS,"I");
	sql.closeParenthesis();*/
        sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.STARTSWITH, fatturaAttiva.getCodice_fiscale());
        sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.STARTSWITH, fatturaAttiva.getPartita_iva());
        sql.addSQLClause("AND", "ANAGRAFICO.NOME", sql.STARTSWITH, fatturaAttiva.getNome());
        sql.addSQLClause("AND", "ANAGRAFICO.COGNOME", sql.STARTSWITH, fatturaAttiva.getCognome());
        sql.addSQLClause("AND", "ANAGRAFICO.RAGIONE_SOCIALE", sql.STARTSWITH, fatturaAttiva.getRagione_sociale());
        sql.addSQLClause("AND", "V_TERZO_CF_PI.DENOMINAZIONE_SEDE", sql.STARTSWITH, cliente.getDenominazione_sede());
        sql.addSQLClause("AND", "ANAGRAFICO.TI_ENTITA", sql.NOT_EQUALS, AnagraficoBulk.DIVERSI);
        sql.addSQLClause("AND", "V_TERZO_CF_PI.TI_TERZO", sql.NOT_EQUALS, TerzoBulk.CREDITORE);
        sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_PRECEDENTE", sql.EQUALS, cliente.getCd_precedente());
        sql.addSQLClause("AND", "((V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL) OR (V_TERZO_CF_PI.DT_FINE_RAPPORTO >= ?))");
        sql.addParameter(fatturaAttiva.getDt_registrazione(), java.sql.Types.TIMESTAMP, 0);
        sql.addSQLClause("AND", "ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS, fatturaAttiva.getSupplierNationType());
        if (fatturaAttiva.getFl_liquidazione_differita() != null &&
                fatturaAttiva.getFl_liquidazione_differita().booleanValue())
            sql.addSQLClause("AND", "ANAGRAFICO.FL_FATTURAZIONE_DIFFERITA", sql.EQUALS, "Y");

        sql.addClause(clauses);
        return sql;
    }
//^^@@

    /**
     * Vengono richiesti i dati relativi ad soggetto
     * PreCondition:
     * Vengono richiesti i dati relativi ad soggetto
     * PostCondition:
     * Aggiunge delle condizioni supplementari impostate dall'utente
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectClienteByClause(
            UserContext aUC,
            Filtro_ricerca_doc_ammVBulk filtro,
            TerzoBulk soggetto,
            CompoundFindClause clauses)
            throws ComponentException {

        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, soggetto, "V_TERZO_CF_PI").createSQLBuilder();
        sql.addTableToHeader("ANAGRAFICO");
        sql.addSQLJoin("ANAGRAFICO.CD_ANAG", "V_TERZO_CF_PI.CD_ANAG");
        sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, soggetto.getCd_terzo());
        sql.addSQLClause("AND", "ANAGRAFICO.TI_ENTITA", sql.NOT_EQUALS, AnagraficoBulk.DIVERSI);
        sql.addSQLClause("AND", "V_TERZO_CF_PI.TI_TERZO", sql.NOT_EQUALS, TerzoBulk.CREDITORE);
        sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_PRECEDENTE", sql.EQUALS, soggetto.getCd_precedente());

        if (soggetto.getAnagrafico() != null) {
            sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.STARTSWITH, soggetto.getAnagrafico().getCodice_fiscale());
            sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.STARTSWITH, soggetto.getAnagrafico().getPartita_iva());
        }

        sql.addClause(clauses);
        return sql;
    }
//^^@@

    /**
     * Vengono richiesti i dati relativi ad un tariffario
     * PreCondition:
     * Vengono richiesti i dati relativi ad un tariffario
     * PostCondition:
     * Aggiunge delle condizioni supplementari del codice impostato dall'utente, della data inizio e fine
     * coerenti e unità organizzativa appropriata
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectTariffarioByClause(
            UserContext aUC,
            Fattura_attiva_rigaBulk riga,
            it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk tariffario,
            CompoundFindClause clauses)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {

        if (riga.getFattura_attiva().getDt_registrazione() == null)
            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione: inserire una data valida di registrazione per la fattura");

        it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome tariffarioHome =
                (it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome) getHome(aUC,
                        it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk.class);

        it.cnr.jada.persistency.sql.SQLBuilder sql =
                tariffarioHome.createSQLBuilder();

        sql.addClause(clauses);

        sql.addSQLClause(
                "AND",
                "TARIFFARIO.CD_TARIFFARIO",
                sql.EQUALS,
                tariffario.getCd_tariffario());
        sql.addSQLClause(
                "AND",
                "TARIFFARIO.CD_UNITA_ORGANIZZATIVA",
                sql.EQUALS,
                riga.getFattura_attiva().getCd_uo_origine());
        sql.addSQLClause(
                "AND",
                "TARIFFARIO.DT_INI_VALIDITA",
                sql.LESS_EQUALS,
                riga.getFattura_attiva().getDt_registrazione());
        sql.addSQLClause(
                "AND",
                "TARIFFARIO.DT_FINE_VALIDITA",
                sql.GREATER_EQUALS,
                riga.getFattura_attiva().getDt_registrazione());
        return sql;
    }

    /**
     * Vengono richiesti i dati relativi ad una voce iva
     * PreCondition:
     * Vengono richiesti i dati relativi ad una voce iva
     * PostCondition:
     * Aggiunge delle condizioni supplementari del tipo applicazione o entrambe o vendite
     */
//^^@@
    public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_ivaByClause(UserContext aUC, Fattura_attiva_rigaBulk riga, it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva, CompoundFindClause clauses)
            throws ComponentException {
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, voce_iva).createSQLBuilder();

        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "TI_APPLICAZIONE", sql.EQUALS, voce_iva.ENTRAMBE);
        sql.addSQLClause("OR", "TI_APPLICAZIONE", sql.EQUALS, voce_iva.VENDITE);
        sql.closeParenthesis();
        // Se il cliente è un soggetto iva ed è Italiano
        if (riga.getFattura_attiva() != null && riga.getFattura_attiva().getCliente() != null && riga.getFattura_attiva().getCliente().getAnagrafico() != null &&
                riga.getFattura_attiva().getCliente().getAnagrafico().getTi_italiano_estero() != null &&
                riga.getFattura_attiva().getCliente().getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.ITALIA) == 0 &&
                riga.getFattura_attiva().getCliente().getAnagrafico().getPartita_iva() != null) {
            // tutti
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "FL_SOLO_ITALIA", sql.EQUALS, "N");
            sql.addSQLClause("OR", "FL_SOLO_ITALIA", sql.EQUALS, "Y");
            sql.closeParenthesis();

        } else
            sql.addSQLClause("AND", "FL_SOLO_ITALIA", sql.EQUALS, "N");

        if (riga.getBene_servizio() != null) {
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "TI_BENE_SERVIZIO", sql.EQUALS, voce_iva.BENE_SERVIZIO);
            sql.addSQLClause("OR", "TI_BENE_SERVIZIO", sql.EQUALS, riga.getBene_servizio().getTi_bene_servizio());
            sql.closeParenthesis();
        }

// da valutare
//	if (riga!=null && riga.getFattura_attiva()!=null && ((Fattura_attiva_IBulk)riga.getFattura_attiva()).getFl_intra_ue())
//		sql.addSQLClause("AND", "FL_INTRA", sql.EQUALS, "Y");
        if (riga instanceof Nota_di_credito_attiva_rigaBulk && !riga.getFattura_attiva().isIvaRecuperabile())
            sql.addSQLClause("AND", "FL_IVA_NON_RECUPERABILE", sql.EQUALS, "Y");
        sql.addClause(clauses);
        return sql;
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la protocollazione dei documenti attivi e vengono selezionate
     * tutte le estrazioni del "filtro".
     * PostCondition:
     * Vegono inseriti nelle apposite tabelle i parametri per la protocollazione
     * e per la ristampa che verranno riusate dalle procedure protocolla e ristampa
     */

    public Filtro_ricerca_doc_amm_protocollabileVBulk selezionaTuttiPerStampa(
            UserContext userContext,
            Filtro_ricerca_doc_amm_protocollabileVBulk filtro)
            throws ComponentException {

        try {
            Long pgProtocollazioneIVA = filtro.getPgProtocollazioneIVA();
            if (pgProtocollazioneIVA == null) {
                pgProtocollazioneIVA = callGetPgPerProtocolloIVA(userContext);
                filtro.setPgProtocollazioneIVA(pgProtocollazioneIVA);
            } else
                //Mi assicuro che non siano stati inseriti manualmente delle fatture e poi sia stato premuto
                //seleziona tutti
                cancellaDatiPerProtocollazioneIva(userContext, null, pgProtocollazioneIVA);

            Long pgStampa = filtro.getPgStampa();
            if (pgStampa == null) {
                pgStampa = callGetPgPerStampa(userContext);
                filtro.setPgStampa(pgStampa);
            } else
                //Mi assicuro che non siano stati inseriti manualmente delle fatture e poi sia stato premuto
                //seleziona tutti
                cancellaDatiPerStampaIva(userContext, null, pgStampa);

            SQLBuilder sql = (SQLBuilder) select(userContext, filtro.getSQLClauses(), (OggettoBulk) filtro.getInstance());
            inserisciTuttiDatiPerProtocollazioneIva(userContext, sql, filtro, pgProtocollazioneIVA);
            sql = (SQLBuilder) select(userContext, filtro.getSQLClauses(), (OggettoBulk) filtro.getInstance());
            inserisciTuttiDatiPerStampaIva(userContext, sql, filtro, pgStampa);

            return filtro;

        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la ristampa dei documenti attivi e vengono selezionate
     * tutte le estrazioni del "filtro".
     * PostCondition:
     * Vegono inseriti nell'apposita tabella i parametri per la ristampa
     * che verranno riusati dalla procedura
     */

    public Filtro_ricerca_doc_amm_ristampabileVBulk selezionaTuttiPerStampa(
            UserContext userContext,
            Filtro_ricerca_doc_amm_ristampabileVBulk filtro)
            throws ComponentException {

        try {
            Long pgStampa = filtro.getPgStampa();
            if (pgStampa == null) {
                pgStampa = callGetPgPerStampa(userContext);
                filtro.setPgStampa(pgStampa);
            } else
                //Mi assicuro che non siano stati inseriti manualmente delle fatture e poi sia stato premuto
                //seleziona tutti
                cancellaDatiPerStampaIva(userContext, null, pgStampa);

            SQLBuilder sql = (SQLBuilder) select(userContext, filtro.getSQLClauses(), (OggettoBulk) filtro.getInstance());
            inserisciTuttiDatiPerRistampaIva(userContext, sql, filtro, pgStampa);

            return filtro;

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    private Fattura_attivaBulk setChangeDataToEur(
            UserContext userContext,
            Fattura_attivaBulk fattura_attiva)
            throws ComponentException {

        fattura_attiva.setValuta(getEuro(userContext));
        //cercaCambio(userContext, fattura_attiva);
        return fattura_attiva;
    }

    public Fattura_attivaBulk setContoEnteIn(
            it.cnr.jada.UserContext userContext,
            Fattura_attivaBulk fatturaAttiva,
            java.util.List banche)
            throws ComponentException {

        if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(fatturaAttiva.getModalita_pagamento_uo().getTi_pagamento())) {
            fatturaAttiva.setBanca_uo((BancaBulk) banche.get(0));
            return fatturaAttiva;
        }

        try {
            Configurazione_cnrBulk config = new Configurazione_cnrBulk(
                    "CONTO_CORRENTE_SPECIALE",
                    "ENTE",
                    "*",
                    new Integer(0));
            it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome) getHome(userContext, config);
            java.util.List configurazioni = home.find(config);
            if (configurazioni != null) {
                if (configurazioni.isEmpty())
                    fatturaAttiva.setBanca_uo((BancaBulk) banche.get(0));
                else if (configurazioni.size() == 1) {
                    Configurazione_cnrBulk configBanca = (Configurazione_cnrBulk) configurazioni.get(0);
                    BancaBulk bancaEnte = null;
                    for (Iterator i = banche.iterator(); i.hasNext(); ) {
                        BancaBulk banca = (BancaBulk) i.next();
                        if (bancaEnte == null) {
                            if (banca.getAbi().equalsIgnoreCase(configBanca.getVal01()) &&
                                    banca.getCab().equalsIgnoreCase(configBanca.getVal02()) &&
                                    banca.getNumero_conto().contains(configBanca.getVal03()))
                                bancaEnte = banca;
                        }
                    }
                    if (bancaEnte == null)
                        bancaEnte = (BancaBulk) banche.get(0);
                    fatturaAttiva.setBanca_uo(bancaEnte);
                } else
                    fatturaAttiva.setBanca_uo((BancaBulk) banche.get(0));
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }

        return fatturaAttiva;
    }

    private void setDt_termine_creazione_docamm(
            it.cnr.jada.UserContext userContext,
            Fattura_attivaBulk fattura)
            throws ComponentException {

        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession session = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)
                    it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                            "CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
                            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            java.sql.Timestamp t = session.getDt01(
                    userContext,
                    fattura.getEsercizio(),
                    "*",
                    "CHIUSURA_COSTANTI",
                    "TERMINE_CREAZIONE_DOCAMM_ES_PREC");
            if (t == null)
                throw new it.cnr.jada.comp.ApplicationException("La costante di chiusura \"termine creazione docamm es prec\" NON è stata definita! Impossibile proseguire.");

            fattura.setDt_termine_creazione_docamm(t);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
//^^@@

    /**
     * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
     * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
     * anche quelli del documento amministrativo.
     * <p>
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
//^^@@

    /**
     * Storna i dettagli selezionati.
     * PreCondition:
     * In nota di credito viene richiesta la contabilizzazione dei dettagli selezionati.
     * PostCondition:
     * Vengono stornati nella nota di credito passata i "dettagliDaStornare" sull'obbligazione/accertamento selezionato/creato.
     */
//^^@@
    public Nota_di_credito_attivaBulk stornaDettagli(
            UserContext context,
            Nota_di_credito_attivaBulk ndC,
            java.util.List dettagliDaStornare,
            java.util.Hashtable relationsHash)
            throws ComponentException {

        if (ndC == null) return ndC;

        if (dettagliDaStornare == null || dettagliDaStornare.isEmpty()) {
            if (relationsHash != null) {
                Accertamento_scadenzarioBulk accertamentoSelezionato = (Accertamento_scadenzarioBulk) relationsHash.get(ndC);
                if (accertamentoSelezionato != null)
                    ndC.addToFattura_attiva_accertamentiHash(accertamentoSelezionato, null);
            }
        } else {
            for (Iterator i = dettagliDaStornare.iterator(); i.hasNext(); ) {
                Nota_di_credito_attiva_rigaBulk rigaNdC = (Nota_di_credito_attiva_rigaBulk) i.next();
                Fattura_attiva_rigaIBulk rigaAssociata = null;
                Obbligazione_scadenzarioBulk obbligazione_scadenzarioAssociata = null;
                if (relationsHash != null) {
                    Object obj = relationsHash.get(rigaNdC);
                    if (obj instanceof Fattura_attiva_rigaIBulk) {
                        Fattura_attiva_rigaIBulk rigaInRelazione = (Fattura_attiva_rigaIBulk) obj;
                        rigaAssociata = (rigaInRelazione == null) ?
                                rigaNdC.getRiga_fattura_associata() :
                                rigaInRelazione;
                    } else {
                        obbligazione_scadenzarioAssociata = (Obbligazione_scadenzarioBulk) obj;
                    }
                } else
                    rigaAssociata = rigaNdC.getRiga_fattura_associata();

                if (rigaAssociata != null) {
                    ndC = basicStornaDettaglio(
                            context,
                            ndC,
                            rigaNdC,
                            rigaAssociata);
                } else {
                    ndC = basicStornaDettaglio(
                            context,
                            ndC,
                            rigaNdC,
                            obbligazione_scadenzarioAssociata);
                }
            }
        }
        return ndC;
    }
//^^@@

    /**
     * Aggiornamento di un dettaglio di documento amministrativo
     * PreCondition:
     * Richiesto l'aggiornamento di un dettaglio di documento amministrativo
     * PostCondition:
     * Il dettaglio viene aggiornato
     */
//^^@@
    public IDocumentoAmministrativoRigaBulk update(
            it.cnr.jada.UserContext userContext,
            IDocumentoAmministrativoRigaBulk rigaDocAmm)
            throws it.cnr.jada.comp.ComponentException {

        try {
            updateBulk(userContext, (OggettoBulk) rigaDocAmm);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException((OggettoBulk) rigaDocAmm, e);
        }
        return rigaDocAmm;
    }

    /**
     * Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
     * PreCondition:
     * Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
     * PostCondition:
     * Il dettaglio viene aggiornato
     */
//^^@@
    public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
            it.cnr.jada.UserContext userContext,
            IScadenzaDocumentoContabileBulk scadenza)
            throws it.cnr.jada.comp.ComponentException {

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
     * Normale.
     * PreCondition:
     * Viene richiesta la visualizzazione del consuntivo fattura.
     * PostCondition:
     * Vegono restituiti i dettagli fattura raggruppati per codice IVA.
     */

    private void validaClausolePerRistampa(
            UserContext userContext,
            Filtro_ricerca_doc_amm_ristampabileVBulk filtro)
            throws it.cnr.jada.comp.ComponentException {

        if (filtro == null)
            throw new it.cnr.jada.comp.ApplicationException("Specificare un filtro per la ricerca!");

        try {
            filtro.validateClauses();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            throw handleException(filtro, e);
        }
    }

    private void validaConConsuntivi(
            UserContext userContext,
            Fattura_attivaBulk original,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (original == null || fatturaAttiva == null)
            return;

        if (fatturaAttiva.isStampataSuRegistroIVA()) {
            Fattura_attivaBulk FAConsuntivata = (Fattura_attivaBulk) calcoloConsuntivi(userContext, fatturaAttiva);
            Fattura_attivaBulk originaleConsuntivato = (Fattura_attivaBulk) calcoloConsuntivi(userContext, original);
            Vector consOriginale = (Vector) originaleConsuntivato.getFattura_attiva_consuntivoColl();
            Vector consFA = (Vector) FAConsuntivata.getFattura_attiva_consuntivoColl();
            if (consFA.size() != consOriginale.size())
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile aggiungere, togliere o cambiare codici IVA su fatture già stampate!");
            for (Iterator i = consFA.iterator(); i.hasNext(); ) {
                Consuntivo_rigaVBulk rigaConsuntivoFA = (Consuntivo_rigaVBulk) i.next();
                try {
                    int idx = it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(consOriginale, rigaConsuntivoFA);
                    Consuntivo_rigaVBulk rigaConsuntivoOriginale = (Consuntivo_rigaVBulk) consOriginale.get(idx);
                    if (!rigaConsuntivoOriginale.equalsByPrimaryKey(rigaConsuntivoFA))
                        throw new IndexOutOfBoundsException();
                    if ((rigaConsuntivoOriginale.getTotale_iva().compareTo(rigaConsuntivoFA.getTotale_iva()) != 0) ||
                            (rigaConsuntivoOriginale.getTotale_imponibile().compareTo(rigaConsuntivoFA.getTotale_imponibile()) != 0))
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: i totali IVA o imponibile per il codice IVA \"" +
                                rigaConsuntivoFA.getVoce_iva().getCd_voce_iva() +
                                "\" non sono modificabili perchè la fattura risulta già stampata su registro definitivo!");
                } catch (IndexOutOfBoundsException e) {
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile aggiungere il codice IVA \"" +
                            rigaConsuntivoFA.getVoce_iva().getCd_voce_iva() +
                            "\" perchè il documento risulta già stampato su registro definitivo!");
                }
            }
        }
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Viene richiesta la visualizzazione del consuntivo fattura.
     * PostCondition:
     * Vegono restituiti i dettagli fattura raggruppati per codice IVA.
     */

    private void validaDataStampaProtocollazioneIVA(
            UserContext userContext,
            java.sql.Timestamp dataStampa)
            throws it.cnr.jada.comp.ApplicationException {

        if (dataStampa == null)
            throw new it.cnr.jada.comp.ApplicationException("Specificare la data di protocollazione IVA!");

        //Nel caso di richiesta dovranno essere aggiunti qui i controlli su dataStampa
        //non posteriore ad oggi (se data stampa resa modificabile) e dataStampa
        //non precedente al massimo valore delle date emissione delle fatture attive
        //già protocollate.
    }

    /**
     * validazione modifica  esercizio e tipo fattura (G1)
     * PreCondition:
     * Sono stati modificati campi relativi all' esercizio, stato iva  e tipo fattura.
     * <p>
     * PostCondition:
     * Viene inviato un messaggio "Attenzione: Sono stati modificati campi relativi all' esercizio, stato iva  e tipo fattura. Non è possibile validare le modifiche apportate".
     * tutti i controlli superati
     * PreCondition:
     * Nessuna situazione di errore di validazione è stata rilevata.
     * PostCondition:
     * Consentita la registrazione.
     * validazione numero di dettagli maggiore di zero.
     * PreCondition:
     * Il numero di dettagli nella fattura è zero
     * PostCondition:
     * Viene inviato un messaggio: "Attenzione non possono esistere fatture senza almeno un dettaglio".
     * validazione aggiunta dettagli n fatture con stato iva B o C.
     * PreCondition:
     * E' stato aggiunto un dettaglio in  fatture con stato iva B o C .
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si possono aggiungere dettagli in fatture con stato iva B o C ."
     * validazione modifica sezionale
     * PreCondition:
     * E' stato modificato un sezionale in fatture con dettagli in stato not I e stato iva B o C.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si possono eliminare  dettagli in  fatture parzialmente contabilizzate e stato iva B o C"
     * validazione modifica testata (G3)
     * PreCondition:
     * Sono stati modificati i campi  data fattura di emissione , importo, flag IntraUE, flag San Marino, sezionale in fatture con stato iva B o C .
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
     * validazione modifica testata campo terzo.(G5)
     * PreCondition:
     * E stato modificato il campo terzo nella testata in stato (B or C) or (A and testata=P).
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione lo stato della fattura non consente di modificare il cliente".
     * validazione modifica fattura pagata.
     * PreCondition:
     * E' satata eseguita una modifica in fattura con testata in stato P.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può modificare nulla in una fattura pagata".
     * validazione associazione scadenze
     * PreCondition:
     * Esistono dettagli non collegati ad accertamento.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione esistono dettagli non collegati ad accertamento."
     */
//^^@@
    public void validaFattura(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException {
    	
        //controlla che la data di scadenza sia successiva alla data di registrazione
        if (fatturaAttiva.getDt_scadenza() != null && fatturaAttiva.getDt_scadenza().before(fatturaAttiva.getDt_registrazione()))
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: la data di scadenza e' precedente a quella di registrazione");
        //controlla che ci siano righe
        if (fatturaAttiva.getFattura_attiva_dettColl().isEmpty())
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non possono esistere fatture senza almeno un dettaglio");
        else {
            BulkList dettaglio = fatturaAttiva.getFattura_attiva_dettColl();
            for (Iterator i = dettaglio.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                if (fatturaAttiva.getTi_causale_emissione() != null && fatturaAttiva.getTi_causale_emissione().equals(fatturaAttiva.TARIFFARIO)) {
                    if (riga.getTariffario() == null)
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: inserire un tariffario valido per ogni riga");
                }
                validaRiga(aUC, riga);
            }
            controlliQuadraturaTotaleFattura(aUC, fatturaAttiva, dettaglio, true);
        }

        if (fatturaAttiva.getFl_intra_ue() && fatturaAttiva.getPartita_iva() == null && fatturaAttiva.getCliente() != null && fatturaAttiva.getCliente().getAnagrafico() != null && fatturaAttiva.getCliente().getAnagrafico().isPersonaGiuridica() ){
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile emettere una fattura attiva estera ad un cliente non persona fisica senza partita IVA");
        }
        
        try {
            fatturaAttiva.validaDateCompetenza();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        }
        controllaCompetenzaCOGEDettagli(aUC, fatturaAttiva);

        if (fatturaAttiva instanceof Nota_di_credito_attivaBulk) {
            Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk) fatturaAttiva;
            if (ndc.getObbligazioniHash() != null && !ndc.getObbligazioniHash().isEmpty()) {
                if (ndc.getModalita_pagamento() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Specificare le modalità di pagamento per le obbligazioni inserite.");
                if (ndc.getBanca() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Specificare il conto d'appoggio per le obbligazioni inserite.");
                controllaQuadraturaObbligazioni(aUC, ndc);
            }
        }

        //if (fatturaAttiva.getIm_totale_fattura().compareTo(new java.math.BigDecimal(0)) == 0)
        //  throw new it.cnr.jada.comp.ApplicationException("L'importo totale della fattura non può essere zero!");
        if (fatturaAttiva.getDt_registrazione() != null
                && fatturaAttiva.getCliente().getDt_fine_rapporto() != null
                && fatturaAttiva.getDt_registrazione().after(fatturaAttiva.getCliente().getDt_fine_rapporto()))
            throw new it.cnr.jada.comp.ApplicationException("Il cliente selezionato ha una data fine rapporto precedente alla data di registrazione della fattura.");

        validazioneComune(aUC, fatturaAttiva);

        controllaContabilizzazioneDiTutteLeRighe(aUC, fatturaAttiva);
        controllaQuadraturaAccertamenti(aUC, fatturaAttiva);
        controllaQuadraturaIntrastat(aUC, fatturaAttiva);
    }
//^^@@

    public void controlliQuadraturaTotaleFattura(UserContext aUC, Fattura_attivaBulk fatturaAttiva, BulkList dettaglio, Boolean totaleAliquotaIva)
            throws ApplicationException, ComponentException {
    	HashMap<BigDecimal, BigDecimal> mappaAliquote = new HashMap<>();
    	Tipo_atto_bolloBulk tipoAtto = getTipoAttoBollo(aUC, fatturaAttiva);
        if (tipoAtto != null || fatturaAttiva.isDocumentoFatturazioneElettronica()) {
            Boolean esisteRigaConBollo = false;
            BigDecimal totaleImportoRigheSoggetteBollo = BigDecimal.ZERO;
            for (Iterator i = dettaglio.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
            	Bene_servizioBulk bene = null;
                if (tipoAtto != null){
                	try {
                		bene = (Bene_servizioBulk) completaOggetto(aUC, riga.getBene_servizio());
                	} catch (PersistencyException e) {
                		throw new ComponentException(e);
                	}
                	if (bene.getFl_bollo()) {
                		if (esisteRigaConBollo) {
                			throw new it.cnr.jada.comp.ApplicationException("Attenzione: esiste più di una riga con un bene/servizio di tipo Bollo");
                		}
                		BigDecimal importoBollo = tipoAtto.getImBollo();
                		if (importoBollo != null && importoBollo.compareTo(riga.getIm_imponibile()) != 0) {
                			throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'importo indicato per il bollo " + riga.getIm_imponibile().doubleValue() +
                					" è diverso dall'importo Bollo Virtuale stabilito sui parametri " + importoBollo.doubleValue());
                		}
                		esisteRigaConBollo = true;
                	}
                }
                Voce_ivaBulk voceIva;
                try {
                    voceIva = (Voce_ivaBulk) completaOggetto(aUC, riga.getVoce_iva());
                } catch (PersistencyException e) {
                    throw new ComponentException(e);
                }
                if (tipoAtto != null){
                    if (voceIva.isCodiceIvaSoggettoBollo() && !bene.getFl_bollo()) {
                        totaleImportoRigheSoggetteBollo = totaleImportoRigheSoggetteBollo.add(riga.getIm_imponibile()).add(riga.getIm_iva());
                    }
                }
                
                if (fatturaAttiva.isDocumentoFatturazioneElettronica()) {
                    if (totaleAliquotaIva){
                    	if (mappaAliquote.containsKey(voceIva.getPercentuale())){
                    		mappaAliquote.put(voceIva.getPercentuale(), mappaAliquote.get(voceIva.getPercentuale()).add(riga.getIm_imponibile()));
                    	} else {
                    		mappaAliquote.put(voceIva.getPercentuale(), riga.getIm_imponibile());
                    	}
                    }
                }
            }
            if (fatturaAttiva.isDocumentoFatturazioneElettronica()) {
                if (totaleAliquotaIva){
                	BigDecimal totaleIva = BigDecimal.ZERO;
                	Iterator it = mappaAliquote.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry riga = (Map.Entry)it.next();
                        BigDecimal imponibile = (BigDecimal)riga.getValue();
                        BigDecimal aliquota = (BigDecimal)riga.getKey();
                        if (aliquota.compareTo(BigDecimal.ZERO) > 0){
                        	imponibile.multiply(aliquota).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP);
                        	totaleIva = totaleIva.add(imponibile.multiply(aliquota).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    BigDecimal differenzaIva = totaleIva.subtract(fatturaAttiva.getIm_totale_iva());
                    BigDecimal tolleranza = BigDecimal.ONE.divide((BigDecimal.TEN.multiply(BigDecimal.TEN)));
                    if (differenzaIva.compareTo(BigDecimal.ZERO) > 0 && differenzaIva.compareTo(tolleranza) > 0){
                        throw new it.cnr.jada.comp.ApplicationException("Il totale documento per aliquota iva è maggiore di "+differenzaIva+" rispetto alla somma dei dettagli del documento. Aggiungere " + differenzaIva+" all'importo IVA(forzandolo) di una delle righe del documento.");
                    }
                    if (differenzaIva.compareTo(BigDecimal.ZERO) < 0 && differenzaIva.abs().compareTo(tolleranza) > 0){
                        throw new it.cnr.jada.comp.ApplicationException("Il totale documento per aliquota iva è minore di "+differenzaIva+" rispetto alla somma dei dettagli del documento. Sottrarre " + differenzaIva.abs()+" dall'importo IVA(forzandolo) di una delle righe del documento.");
                    }
                }
            }
            if (tipoAtto != null){
                BigDecimal importoLimiteBolloVirtuale = tipoAtto.getLimiteCalcolo();
                if (importoLimiteBolloVirtuale != null) {
                    if (totaleImportoRigheSoggetteBollo.compareTo(importoLimiteBolloVirtuale) > 0 && !esisteRigaConBollo) {
                        throw new it.cnr.jada.comp.ApplicationException("La somma delle righe soggette a bollo: " + totaleImportoRigheSoggetteBollo.doubleValue() + " supera "
                                + "l'importo limite previsto per il bollo: " + importoLimiteBolloVirtuale.doubleValue() + ". E' necessario indicare la riga con il bollo.");
                    } else if (totaleImportoRigheSoggetteBollo.compareTo(importoLimiteBolloVirtuale) <= 0 && esisteRigaConBollo) {
                        throw new it.cnr.jada.comp.ApplicationException("La somma delle righe soggette a bollo: " + totaleImportoRigheSoggetteBollo.doubleValue() + " non supera "
                                + "l'importo limite previsto per il bollo: " + importoLimiteBolloVirtuale.doubleValue() + ". E' necessario eliminare la riga con il bollo.");
                    }
                }
            }
        }
    	
    }
    public void controlliGestioneBolloVirtuale(UserContext aUC, Fattura_attivaBulk fatturaAttiva, BulkList dettaglio)
            throws ApplicationException, ComponentException {
    	controlliQuadraturaTotaleFattura(aUC, fatturaAttiva, dettaglio, false);
    }

    /**
     * validazione bene.
     * PreCondition:
     * Il bene  relativo alla riga fattura in via di variazione risulta di tipo soggetto ad inventario.
     * PostCondition:
     * Viene inviato un messaggio all'utente "Questo bene è soggetto ad inventario".
     * NOTA: Vanno dettagliate le condizioni di inventario non appena disponibile il relativo use case
     * tutti i controli superati
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * Viene consentita la registrazione riga.
     * validazione modifica imponibile, iva, totale, aliquota (G2)
     * PreCondition:
     * Sono stati modificati i campi  imponibile, iva, totale, aliquiota (G2) in fattura in stato B or C
     * PostCondition:
     * Viene inviato un messaggio "Attenzione:  questa modifica non è permessa"
     * validazione modifica dettaglio pagato.
     * PreCondition:
     * E' stato modificato un dettaglio in stato P.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio già pagato".
     * validazione modifica dettaglio di fattura già pagata.
     * PreCondition:
     * E' stato modificato un dettaglio di fattura in stato P.
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può modificare un dettaglio in una fattura già pagata".
     */
//^^@@
    public void validaRiga(UserContext aUC, Fattura_attiva_rigaBulk fatturaRiga)
            throws ComponentException {
        if (fatturaRiga.getBene_servizio() == null || fatturaRiga.getBene_servizio().getCrudStatus() == OggettoBulk.UNDEFINED)
            throw new it.cnr.jada.comp.ApplicationException("Inserire un bene/servizio per la riga.");

        if (fatturaRiga.getDs_riga_fattura() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire una descrizione per la riga.");
        if (fatturaRiga.getQuantita() == null || fatturaRiga.getQuantita().intValue() <= 0)
            throw new it.cnr.jada.comp.ApplicationException("La quantità specificata nel dettaglio NON è valida.");
        try {
            VoceIvaComponentSession h = (VoceIvaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_VoceIvaComponentSession", VoceIvaComponentSession.class);
            Voce_ivaBulk def = h.caricaVoceIvaDefault(aUC);
            if (def != null && def.getCd_voce_iva() != null)
                if (fatturaRiga.getVoce_iva().getCd_voce_iva().compareTo(def.getCd_voce_iva()) == 0)
                    throw new it.cnr.jada.comp.ApplicationException("Codice iva non valido");

        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
        try {
            fatturaRiga.validaDateCompetenza();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        }

        if (fatturaRiga.getFattura_attiva().getTi_causale_emissione().equals(fatturaRiga.getFattura_attiva().TARIFFARIO) &&
                (fatturaRiga.getTariffario() == null || fatturaRiga.getTariffario().getCd_tariffario() == null || 
                 fatturaRiga.getTariffario().getCrudStatus() == OggettoBulk.UNDEFINED))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Selezionare un tariffario!");

        if (fatturaRiga.getAccertamento_scadenzario() != null && !fatturaRiga.getAccertamento_scadenzario().getAccertamento().getCd_terzo().equals(fatturaRiga.getFattura_attiva().getCliente().getCd_terzo())
                && !fatturaRiga.getAccertamento_scadenzario().getAccertamento().getDebitore().getAnagrafico().getTi_entita().equals(AnagraficoBulk.DIVERSI))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione! Il cliente selezionato non corrisponde al cliente dell'accertamento " + ((fatturaRiga.getDs_riga_fattura() != null) ? "sul dettaglio " + fatturaRiga.getDs_riga_fattura() : "su un dettaglio"));


        Elemento_voceBulk voce = recuperoVoce(aUC, fatturaRiga.getAccertamento_scadenzario());
        if (fatturaRiga.getPg_trovato() != null && ((fatturaRiga.getAccertamento_scadenzario() == null) || (fatturaRiga.getAccertamento_scadenzario() != null && isInibitaIndicazioneTrovato(voce))))
            fatturaRiga.setPg_trovato(null);
        if (fatturaRiga.getAccertamento_scadenzario() != null && isObbligatoriaIndicazioneTrovato(voce) && fatturaRiga.getPg_trovato() == null)
            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione! Non è stato inserito il Brevetto/Trovato mentre la voce di bilancio utilizzata per la contabilizzazione del dettaglio collegato ne prevede l'indicazione obbligatoria");

        //se ho una fattura di tipo a TARIFFARIO controllo che la data di registrazione sia compresa
        //nei periodi del sezionale impostato sulla riga
        if (fatturaRiga.getFattura_attiva().getTi_causale_emissione().equals(fatturaRiga.getFattura_attiva().TARIFFARIO) &&
                fatturaRiga.getFattura_attiva().getDt_registrazione() != null &&
                fatturaRiga.getTariffario() != null &&
                (fatturaRiga.getTariffario().getDt_ini_validita().after(fatturaRiga.getFattura_attiva().getDt_registrazione()) ||
                        fatturaRiga.getTariffario().getDt_fine_validita().before(fatturaRiga.getFattura_attiva().getDt_registrazione())))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione! Per la data di registrazione impostata il tariffario selezionato " + ((fatturaRiga.getDs_riga_fattura() != null) ? "sul dettaglio " + fatturaRiga.getDs_riga_fattura() : "su un dettaglio") + "non è valido");
        //Il controllo è stato eliminato a seguito della richiesta 423. Validazione in component
        //if (fatturaRiga.getFattura_attiva().isStampataSuRegistroIVA() &&
        //fatturaRiga.getCrudStatus() != OggettoBulk.NORMAL &&
        //!(fatturaRiga instanceof Fattura_attiva_rigaIBulk)) { /* aggiunto per richiesta 423 */

        //Fattura_attiva_rigaBulk original = null;
        //try {
        //original = (Fattura_attiva_rigaBulk)getHome(aUC, fatturaRiga).findByPrimaryKey(fatturaRiga);
        //} catch (it.cnr.jada.persistency.PersistencyException e) {
        //throw handleException(fatturaRiga, e);
        //}
        //if (original != null) {
        //if (original.getIm_imponibile().compareTo(fatturaRiga.getIm_imponibile()) != 0 ||
        //original.getIm_iva().compareTo(fatturaRiga.getIm_iva()) != 0 ||
        //(original.getIm_diponibile_nc() != null && original.getIm_diponibile_nc().compareTo(fatturaRiga.getIm_totale_divisa()) != 0) ||
        //original.getCd_voce_iva().compareTo(fatturaRiga.getCd_voce_iva()) != 0)

        //throw new it.cnr.jada.comp.ApplicationException("Attenzione: questa modifica non è permessa quando il documento è stampato sui registri IVA.");
        //}
        //}
        if (fatturaRiga instanceof Nota_di_credito_attiva_rigaBulk && fatturaRiga.getFattura_attiva() != null && !fatturaRiga.getFattura_attiva().isIvaRecuperabile() && fatturaRiga.getVoce_iva() != null && !fatturaRiga.getVoce_iva().getFl_iva_non_recuperabile())
            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione! Il codice iva " + ((fatturaRiga.getDs_riga_fattura() != null) ? "sul dettaglio " + fatturaRiga.getDs_riga_fattura() : "su un dettaglio") + " non è valido");

        if (Optional.ofNullable(fatturaRiga)
                .flatMap(fattura_attiva_rigaBulk -> Optional.ofNullable(fattura_attiva_rigaBulk.getVoce_iva()))
                .flatMap(voce_ivaBulk -> Optional.ofNullable(voce_ivaBulk.getFl_obb_dichiarazione_intento()))
                .orElse(Boolean.FALSE))
            verificaEsistenzaDichiarazioneIntento(aUC, fatturaRiga);
		if (!RemoveAccent.isOk(fatturaRiga.getDs_riga_fattura())){
			throw new ApplicationException("La descrizione contienere caratteri speciali non supportati.");
		}
    }

    private boolean isObbligatoriaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
        if (voce == null)
            return false;
        return voce.isObbligatoriaIndicazioneTrovato();
    }

    private boolean isFacoltativaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
        if (voce == null)
            return false;
        return voce.isFacoltativaIndicazioneTrovato();
    }

    private boolean isInibitaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
        if (voce == null)
            return false;
        return voce.isInibitaIndicazioneTrovato();
    }

    private Elemento_voceBulk recuperoVoce(UserContext aUC, Accertamento_scadenzarioBulk scadenza) throws ComponentException {

        if (scadenza == null || scadenza.getAccertamento() == null)
            return null;

        Elemento_voceHome evHome = (Elemento_voceHome) getHome(aUC, Elemento_voceBulk.class);
        SQLBuilder sql = evHome.createSQLBuilder();

        sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, scadenza.getAccertamento().getEsercizio());
        sql.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, scadenza.getAccertamento().getTi_appartenenza());
        sql.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, scadenza.getAccertamento().getTi_gestione());
        sql.addSQLClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, scadenza.getAccertamento().getCd_elemento_voce());

        try {
            List voce = evHome.fetchAll(sql);
            if (voce.isEmpty())
                return null;
            return (Elemento_voceBulk) voce.get(0);

        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    private boolean isVocePerTrovati(UserContext aUC, Accertamento_scadenzarioBulk scadenza) throws ComponentException {

        if (scadenza.getAccertamento() == null)
            return false;

        Elemento_voceHome evHome = (Elemento_voceHome) getHome(aUC, Elemento_voceBulk.class);
        SQLBuilder sql = evHome.createSQLBuilder();

        sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, scadenza.getAccertamento().getEsercizio());
        sql.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, scadenza.getAccertamento().getTi_appartenenza());
        sql.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, scadenza.getAccertamento().getTi_gestione());
        sql.addSQLClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, scadenza.getAccertamento().getCd_elemento_voce());
        sql.addSQLClause("AND", "fl_trovato", SQLBuilder.NOT_EQUALS, "N");

        try {
            List voce = evHome.fetchAll(sql);
            if (voce.isEmpty())
                return false;
        } catch (PersistencyException e) {
            throw handleException(e);
        }
        return true;
    }
//^^@@

    private void validazioneComune(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException {

        String entitaSingolare = (fatturaAttiva instanceof Fattura_attiva_IBulk) ?
                "fattura" : (fatturaAttiva instanceof Nota_di_credito_attivaBulk) ? "nota di credito" : "nota di debito";
        String entitaPlurale = (fatturaAttiva instanceof Fattura_attiva_IBulk) ?
                "fatture" : (fatturaAttiva instanceof Nota_di_credito_attivaBulk) ? "note di credito" : "note di debito";

        if (!verificaEsistenzaSezionalePer(aUC, fatturaAttiva))
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è stato definito un sezionale per le " + entitaPlurale + " e il tipo sezionale \"" + ((fatturaAttiva.getTipo_sezionale() != null) ? fatturaAttiva.getTipo_sezionale().getDs_tipo_sezionale() : "") + "\"!");

        //Modificato a seguito richiesta 423
        Fattura_attivaBulk original = null;
        try {
            original = (Fattura_attivaBulk) getHome(aUC, fatturaAttiva, null, "none").findByPrimaryKey(fatturaAttiva);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fatturaAttiva, e);
        }

        if (original != null && fatturaAttiva.isStampataSuRegistroIVA()) {
            if (!original.getDt_registrazione().equals(fatturaAttiva.getDt_registrazione()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile modificare la data registrazione del documento quando è in stato IVA B o C");
            if (!original.getCd_tipo_sezionale().equalsIgnoreCase(fatturaAttiva.getCd_tipo_sezionale()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile modificare il sezionale di " + entitaPlurale + " con stato IVA B o C.");

            if (original.getIm_totale_fattura().compareTo(fatturaAttiva.getIm_totale_fattura()) != 0 ||
                    !original.getFl_intra_ue().equals(fatturaAttiva.getFl_intra_ue()) ||
                    !original.getFl_extra_ue().equals(fatturaAttiva.getFl_extra_ue()) ||
                    !original.getFl_san_marino().equals(fatturaAttiva.getFl_san_marino()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare campi relativi agli importi totali o ai sezionali della quando la " + entitaSingolare + " è in stato B o C");

            if (!original.getCd_terzo().equals(fatturaAttiva.getCd_terzo()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare campi relativi all'anagrafico quando la " + entitaSingolare + " è in stato B o C");

            java.util.List originalRows = null;
            try {
                Fattura_attiva_rigaBulk clause = null;
                if (fatturaAttiva instanceof Fattura_attiva_IBulk)
                    clause = new Fattura_attiva_rigaIBulk();
                else if (fatturaAttiva instanceof Nota_di_credito_attivaBulk)
                    clause = new Nota_di_credito_attiva_rigaBulk();
                else clause = new Nota_di_debito_attiva_rigaBulk();

                clause.setFattura_attiva(fatturaAttiva);
                originalRows = getHome(aUC, clause, null, "solo_voce_iva").find(clause);
                getHomeCache(aUC).fetchAll(aUC);
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(fatturaAttiva, e);
            }
            if (originalRows != null) {
                //if (!(fatturaAttiva instanceof Fattura_attiva_IBulk) &&
                //originalRows.size() != fatturaAttiva.getFattura_attiva_dettColl().size())
                //throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile aggiungere, eliminare o modificare i dettagli quando lo stato IVA del documento è B o C.");
                //else {
                original.setFattura_attiva_dettColl(new BulkList(originalRows));
                for (Iterator i = ((Fattura_attivaBulk) original).getFattura_attiva_dettColl().iterator(); i.hasNext(); )
                    ((Fattura_attiva_rigaBulk) i.next()).calcolaCampiDiRiga();
                validaConConsuntivi(
                        aUC,
                        original,
                        fatturaAttiva);
                //}
            }
        }
    }

    /**
     * Verifica l'esistenza e apertura dell'inventario
     * PreCondition:
     * Nessuna condizione di errore rilevata.
     * PostCondition:
     * Viene consentita l'attività richiesta
     * L'inventario non esiste
     * PreCondition:
     * L'inventario per CDS e UO correnti non esiste
     * PostCondition:
     * Viene visualizzato messaggio "non esiste un inventario per questo CDS"
     * L'inventario non è aperto
     * PreCondition:
     * L'inventario per CDS e UO correnti esiste ma non è aperto
     * PostCondition:
     * Viene visualizzato messaggio "l'inventario per questo CDS non è aperto"
     */
//^^@@
    public void verificaEsistenzaEdAperturaInventario(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        try {
            it.cnr.contab.inventario00.ejb.IdInventarioComponentSession h = (it.cnr.contab.inventario00.ejb.IdInventarioComponentSession)
                    it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                            "CNRINVENTARIO00_EJB_IdInventarioComponentSession",
                            it.cnr.contab.inventario00.ejb.IdInventarioComponentSession.class);
            it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = h.findInventarioFor(
                    userContext,
                    fatturaAttiva.getCd_cds_origine(),
                    fatturaAttiva.getCd_uo_origine(),
                    false);
            if (inventario == null)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che non esiste un inventario per questo CDS.\nIn caso di inserimento di dettagli con beni soggetti ad inventario, non sarà permesso il salvataggio della fattura,\nfino alla creazione ed apertura di un nuovo inventario!");
            else if (!h.isAperto(userContext, inventario, fatturaAttiva.getEsercizio())) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che l'inventario per questo CDS non è aperto.\nNel caso di inserimento di dettagli con beni soggetti ad inventario, non sarà permesso il salvataggio della fattura\nfino ad apertura di quest'ultimo!");
            }
        } catch (Exception e) {
            throw handleException(fatturaAttiva, e);
        }
    }
//^^@@

    private boolean verificaEsistenzaSezionalePer(
            UserContext userContext,
            Fattura_attivaBulk fatturaAttiva)
            throws ComponentException {

        if (fatturaAttiva != null && fatturaAttiva.getTipo_sezionale() != null) {

            it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk key = new it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk(
                    fatturaAttiva.getCd_cds_origine(),
                    fatturaAttiva.getTipo_sezionale().getCd_tipo_sezionale(),
                    fatturaAttiva.getCd_uo_origine(),
                    fatturaAttiva.getEsercizio(),
                    fatturaAttiva.getTi_fattura());
            try {
                return getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk.class).findByPrimaryKey(key) != null;
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(fatturaAttiva, e);
            }
        }
        return false;
    }
//^^@@

    /**
     *
     */
//^^@@
    private void verificaStatoEsercizi(UserContext aUC, Fattura_attivaBulk documento) throws ComponentException {

        if (!verificaStatoEsercizio(aUC, new EsercizioBulk(documento.getCd_cds(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC))))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");

        if (documento.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()) {
            throw new it.cnr.jada.comp.ApplicationException("Operazione non permessa!");
        }

        if (documento.getEsercizio().intValue() < it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()) {
            // Gennaro Borriello - (11/10/2004 17.29.46)
            //	Aggiunto controllo per i documenti RIPORTATI, in base all'esercizio COEP precedente
            //	all'es. di scrivania.
            //
            // Gennaro Borriello - (02/11/2004 16.48.21)
            // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
            if (documento.isRiportataInScrivania()) {
                Integer es_prec = new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue() - 1);
                if (!isEsercizioCoepChiusoFor(aUC, documento, es_prec)) {
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile eliminare il documento, poichè l'esercizio economico precedente a quello in scrivania non è chiuso.");
                }
            } else
                throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare il documento perchè non risulta riportato nell'esercizio di scrivania!");
        }
    }

    /**
     * Verifica l'esistenza e apertura dell'esercizio
     * PreCondition:
     * Nessuna condizione di errore rilevata.
     * PostCondition:
     * Viene consentita l'attività richiesta
     * L'esercizio non è aperto
     * PreCondition:
     * L'esercizio su cui insiste il controllo non è aperto
     * PostCondition:
     * Viene notificato l'errore
     */
//^^@@
    public boolean verificaStatoEsercizio(
            UserContext userContext,
            EsercizioBulk anEsercizio)
            throws ComponentException {

        try {
            it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return !eHome.isEsercizioChiuso(
                    userContext,
                    anEsercizio.getEsercizio(),
                    anEsercizio.getCd_cds());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    //aggiunto per testare l'esercizio della data competenza da/a
    public boolean isEsercizioChiusoPerDataCompetenza(UserContext userContext, Integer esercizio, String cd_cds) throws ComponentException, PersistencyException {
        try {

            LoggableStatement cs = new LoggableStatement(getConnection(userContext),
                    "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB008.isEsercizioChiusoYesNo(?,?)}", false, this.getClass());

            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setObject(2, esercizio);
                cs.setObject(3, cd_cds);

                cs.execute();

                return "Y".equals(cs.getString(1));
            } finally {
                cs.close();
            }
        } catch (java.sql.SQLException e) {
            throw handleSQLException(e);
        }
    }

    public Boolean ha_beniColl(UserContext userContext, OggettoBulk fattura) throws ComponentException {
        SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
        if (fattura instanceof Nota_di_credito_attivaBulk) {
            Nota_di_credito_attivaBulk fattura_passiva = (Nota_di_credito_attivaBulk) fattura;
            // nella view vengono valorizzati gli stessi campi
            sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
            sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
            sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
            sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
        } else if (fattura instanceof Nota_di_credito_attiva_rigaBulk) {
            Nota_di_credito_attiva_rigaBulk fattura_passiva = (Nota_di_credito_attiva_rigaBulk) fattura;
            sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
            sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
            sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
            sql.addSQLClause("AND", "progressivo_riga_fatt_pass", sql.EQUALS, fattura_passiva.getProgressivo_riga());
            sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
        } else if (fattura instanceof Nota_di_debito_attivaBulk) {
            Nota_di_debito_attivaBulk fattura_passiva = (Nota_di_debito_attivaBulk) fattura;
            // nella view vengono valorizzati gli stessi campi
            sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
            sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
            sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
            sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
        } else if (fattura instanceof Nota_di_debito_attiva_rigaBulk) {
            Nota_di_debito_attiva_rigaBulk fattura_passiva = (Nota_di_debito_attiva_rigaBulk) fattura;
            sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
            sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
            sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
            sql.addSQLClause("AND", "progressivo_riga_fatt_pass", sql.EQUALS, fattura_passiva.getProgressivo_riga());
            sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
        } else {
            if (fattura instanceof Fattura_attivaBulk) {
                Fattura_attivaBulk fattura_passiva = (Fattura_attivaBulk) fattura;
                sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
                sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
                sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
                sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
                sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
            } else {
                Fattura_attiva_rigaBulk fattura_passiva = (Fattura_attiva_rigaBulk) fattura;
                sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura_passiva.getCd_cds());
                sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());
                sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura_passiva.getEsercizio());
                sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura_passiva.getPg_fattura_attiva());
                sql.addSQLClause("AND", "progressivo_riga_fatt_pass", sql.EQUALS, fattura_passiva.getProgressivo_riga());
                sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
            }
        }
        try {
            if (sql.executeCountQuery(getConnection(userContext)) > 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (java.sql.SQLException e) {
            throw handleSQLException(e);
        }
    }

    private void controllaQuadraturaInventario(UserContext auc, Fattura_attivaBulk fattura) throws ComponentException {
        BigDecimal totale_inv = Utility.ZERO;
        BigDecimal totale_fat = Utility.ZERO;
        BigDecimal im_riga_fattura = Utility.ZERO;

        for (Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaBulk riga_fattura = (Fattura_attiva_rigaBulk) i.next();
            if (riga_fattura.isInventariato()) {
                im_riga_fattura = riga_fattura.getIm_imponibile();
                totale_fat = totale_fat.add(im_riga_fattura);
            }
        }
        Buono_carico_scarico_dettHome assHome = (Buono_carico_scarico_dettHome) getHome(auc, Buono_carico_scarico_dettBulk.class);
        SQLBuilder sql = assHome.createSQLBuilder();

        sql.setDistinctClause(true);
        sql.addTableToHeader("ASS_INV_BENE_FATTURA");
        sql.addSQLClause("AND", "CD_CDS_FATT_ATT", SQLBuilder.EQUALS, fattura.getCd_cds());
        sql.addSQLClause("AND", "CD_UO_FATT_ATT", SQLBuilder.EQUALS, fattura.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "ESERCIZIO_FATT_ATT", SQLBuilder.EQUALS, fattura.getEsercizio());
        sql.addSQLClause("AND", "PG_FATTURA_ATTIVA", SQLBuilder.EQUALS, fattura.getPg_fattura_attiva());
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO", sql.EQUALS, "ASS_INV_BENE_FATTURA.PG_INVENTARIO");
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO", sql.EQUALS, "ASS_INV_BENE_FATTURA.ESERCIZIO");
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO", sql.EQUALS, "ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO", sql.EQUALS, "ASS_INV_BENE_FATTURA.NR_INVENTARIO");
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S", sql.EQUALS, "ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
        sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO", sql.EQUALS, "ASS_INV_BENE_FATTURA.PROGRESSIVO");

        try {
            List associazioni = assHome.fetchAll(sql);
            for (Iterator i = associazioni.iterator(); i.hasNext(); ) {
                Buono_carico_scarico_dettBulk ass = (Buono_carico_scarico_dettBulk) i.next();
                ass.setBene((Inventario_beniBulk) getHome(auc, Inventario_beniBulk.class).findByPrimaryKey(ass.getBene()));
                if (!ass.getBene().isTotalmenteScaricato())
                    totale_inv = totale_inv.add(ass.getValore_unitario());
                else
                    totale_inv = totale_inv.add(ass.getBene().getValore_alienazione());
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        }
        /* r.p. verificare la condizione fatturaPassiva.getCrudStatus()==1
         *  Escludo le fattura già inserite in quanto la quadratura potrebbe non essere verificata
         * a causa della vecchia gestione dell'associazione con il valore del bene completo,
         * che non è stato possibile ricostruire in automatico
         */
        if (totale_fat.compareTo(totale_inv) != 0)//  && fatturaPassiva.getCrudStatus()==1)
            throw new ApplicationException("Attenzione il totale delle righe collegate ad Inventario non corrisponde all'importo da Inventariare.");

    }

    private String aggiornaAssociazioniInventario(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {

        StringBuffer msgBuf = new StringBuffer();

        if (fattura != null) {
            AssociazioniInventarioTable associazioniInventarioHash = fattura.getAssociazioniInventarioHash();
            if (associazioniInventarioHash != null && !associazioniInventarioHash.isEmpty()) {
                Vector associazioniTemporanee = new Vector();
                for (java.util.Enumeration e = ((AssociazioniInventarioTable) associazioniInventarioHash.clone()).keys(); e.hasMoreElements(); ) {
                    Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) e.nextElement();
                    if (ass.isTemporaneo() &&
                            !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(associazioniTemporanee, ass))
                        associazioniTemporanee.add(ass);
                }
                for (Iterator i = associazioniTemporanee.iterator(); i.hasNext(); ) {
                    String msg = aggiornaAssociazioniInventarioTemporanee(userContext, fattura, (Ass_inv_bene_fatturaBulk) i.next());
                    if (msg != null)
                        msgBuf.append(msg);
                }
            }
        }

        if (msgBuf.toString().equals(""))
            return null;

        return msgBuf.toString();
    }

    private String aggiornaAssociazioniInventarioTemporanee(
            UserContext userContext,
            Fattura_attivaBulk fp,
            Ass_inv_bene_fatturaBulk assTemporanea) throws ComponentException {
        String msg = null;
        try {
            Ass_inv_bene_fatturaHome home = (Ass_inv_bene_fatturaHome) getHome(userContext, assTemporanea);
            home.makePersistentAssocia(userContext, assTemporanea, fp);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(assTemporanea, e);
        }

        return msg;
    }

    public void rimuoviDaAssociazioniInventario(
            UserContext userContext,
            Fattura_attiva_rigaIBulk dettaglio,
            Ass_inv_bene_fatturaBulk associazione) throws ComponentException {

        if (dettaglio != null && associazione != null) {
            Inventario_beni_apgHome home = (Inventario_beni_apgHome) getHome(userContext, Inventario_beni_apgBulk.class);
            try {
                home.deleteTempFor(userContext, associazione);
            } catch (it.cnr.jada.persistency.IntrospectionException e) {
                throw handleException(dettaglio, e);
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(dettaglio, e);
            }
        }
    }

    public RemoteIterator selectBeniFor(
            UserContext userContext,
            Fattura_attivaBulk fattura) throws ComponentException {
        SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
        sql.addSQLClause("AND", "esercizio_fatt_pass", sql.EQUALS, fattura.getEsercizio());
        sql.addSQLClause("AND", "cd_cds_fatt_pass", sql.EQUALS, fattura.getCd_cds());
        sql.addSQLClause("AND", "cd_uo_fatt_pass", sql.EQUALS, fattura.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "pg_fattura_passiva", sql.EQUALS, fattura.getPg_fattura_attiva());
        sql.addSQLClause("AND", "cd_tipo_documento_amm", sql.ISNULL, null);
        return iterator(userContext, sql, V_ass_inv_bene_fatturaBulk.class, null);
    }
//^^@@

    /**
     * Metodo che serve per ricostruire sul documento alcune HashTable necessarie per il funzionamento
     * del Business Process
     * <p>
     * Pre:  E' stata modificato il documento o alcuni suoi dettagli
     * Post: Viene ricaricata l'HashTable delle Obbligazioni associate alle Nota Credito
     * o degli Accertamenti associati alla fattura attiva
     *
     * @param userContext
     * @param bulk        fattura o nota credito da aggiornare
     * @return fattura o nota credito aggiornato con le modifiche
     * @throws it.cnr.jada.comp.ComponentException
     */
    public OggettoBulk rebuildDocumento(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        if (bulk instanceof Nota_di_credito_attivaBulk) {
            rebuildObbligazioni(userContext, (Nota_di_credito_attivaBulk) bulk);
        } else if (bulk instanceof Fattura_attivaBulk) {
            ((Fattura_attivaBulk) bulk).setFattura_attiva_accertamentiHash(null);
            rebuildAccertamenti(userContext, (Fattura_attivaBulk) bulk);
        }
        return bulk;
    }

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle scadenze di obbligazioni congruenti con la fattura passiva che si sta creando/modificando.
     * PostCondition:
     * Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
     * Validazione lista delle obbligazioni per le fatture passive
     * PreCondition:
     * Si è verificato un errore nel caricamento delle scadenze delle obbligazioni.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Obbligazione definitiva
     * PreCondition:
     * La scadenza non appartiene ad un'obbligazione definitiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni non cancellate
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione cancellata
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni associate ad altri documenti amministrativi
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni della stessa UO
     * PreCondition:
     * La scadenza dell'obbligazione non appartiene alla stessa UO di generazione fattura passiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitatazione filtro di selezione sul debitore dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Disabilitazione filtro di selezione sul debitore dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva e non è di tipo "diversi"
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro di selezione sulla data di scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro importo scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro sul progressivo dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione non ha progressivo specificato
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Associazione di una scadenza a titolo capitolo dei beni servizio inventariabili da contabilizzare
     * PreCondition:
     * L'obbligazione non ha titolo capitolo dei beni servizio inventariabili da contabilizzare
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     */
//^^@@
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

        sql.addTableToHeader("ELEMENTO_VOCE");
        sql.addSQLJoin("OBBLIGAZIONE.CD_ELEMENTO_VOCE", "ELEMENTO_VOCE.CD_ELEMENTO_VOCE");
        sql.addSQLJoin("OBBLIGAZIONE.TI_APPARTENENZA", "ELEMENTO_VOCE.TI_APPARTENENZA");
        sql.addSQLJoin("OBBLIGAZIONE.TI_GESTIONE", "ELEMENTO_VOCE.TI_GESTIONE");
        sql.addSQLJoin("OBBLIGAZIONE.ESERCIZIO", "ELEMENTO_VOCE.ESERCIZIO");

        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS, "D");
        sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", sql.EQUALS, "N");
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.NOT_EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, filtro.getCd_unita_organizzativa());

        if (filtro.getElemento_voce() != null) {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_ELEMENTO_VOCE", sql.STARTSWITH, filtro.getElemento_voce().getCd_elemento_voce());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_APPARTENENZA", sql.EQUALS, filtro.getElemento_voce().getTi_appartenenza());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_GESTIONE", sql.EQUALS, filtro.getElemento_voce().getTi_gestione());
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, filtro.getElemento_voce().getEsercizio());
        }

        if (filtro.hasDocumentoCompetenzaCOGEInAnnoPrecedente() ||
                !filtro.hasDocumentoCompetenzaCOGESoloInAnnoCorrente())
            sql.addSQLClause("AND", "OBBLIGAZIONE.FL_PGIRO", sql.EQUALS, "N");

        if (!filtro.getFl_fornitore().booleanValue()) {
            sql.addTableToHeader("TERZO");
            sql.addTableToHeader("ANAGRAFICO");
            sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
            sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "(OBBLIGAZIONE.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
            sql.addParameter(filtro.getFornitore().getCd_terzo(), java.sql.Types.INTEGER, 0);
            sql.addParameter(AnagraficoBulk.DIVERSI, java.sql.Types.VARCHAR, 0);
        } else {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", sql.EQUALS, filtro.getFornitore().getCd_terzo());
        }

        if (filtro.getFl_data_scadenziario().booleanValue() && filtro.getData_scadenziario() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA", sql.EQUALS, filtro.getData_scadenziario());
        if (filtro.getFl_importo().booleanValue() && filtro.getIm_importo() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.GREATER_EQUALS, filtro.getIm_importo());

        //filtro su Tipo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getTipo_obbligazione() != null) {
            if (ObbligazioneBulk.TIPO_COMPETENZA.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
            else if (ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
            else if (ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
        }

        //filtro su Anno Residuo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getEsercizio_ori_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE", sql.EQUALS, filtro.getEsercizio_ori_obbligazione());

        //filtro su Numero obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getNr_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.PG_OBBLIGAZIONE", sql.EQUALS, filtro.getNr_obbligazione());

        return iterator(
                context,
                sql,
                Obbligazione_scadenzarioBulk.class,
                "default");
    }

    public Long inserisciDatiPerStampaIva(
            UserContext userContext,
            Long esercizio,
            String cd_cds,
            String cd_unita_organizzativa,
            Long pg_fattura) throws PersistencyException, ComponentException {

        Long pg_stampa = null;

        Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
        fatturaAttiva.setEsercizio(esercizio.intValue());
        fatturaAttiva.setCd_cds_origine(cd_cds);
        fatturaAttiva.setCd_uo_origine(cd_unita_organizzativa);
        fatturaAttiva.setPg_fattura_attiva(pg_fattura);

        List fatture = (getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva));
        if (fatture.size() == 0)
            fatturaAttiva = null;
        else if (fatture.size() == 1)
            fatturaAttiva = (Fattura_attiva_IBulk) fatture.get(0);
        else  //non dovrebbe capitare mai!
            throw new FatturaNonTrovataException("Fattura non trovata!");
        Nota_di_credito_attivaBulk ncAttiva = null;
        if (fatturaAttiva == null) {
            ncAttiva = new Nota_di_credito_attivaBulk();
            ncAttiva.setEsercizio(esercizio.intValue());
            ncAttiva.setCd_cds_origine(cd_cds);
            ncAttiva.setCd_uo_origine(cd_unita_organizzativa);
            ncAttiva.setPg_fattura_attiva(pg_fattura);
            List notec = (getHome(userContext, Nota_di_credito_attivaBulk.class).find(ncAttiva));
            if (notec.size() == 0)
                ncAttiva = null;
            else if (notec.size() == 1)
                ncAttiva = (Nota_di_credito_attivaBulk) notec.get(0);
            else  //non dovrebbe capitare mai!
                throw new FatturaNonTrovataException("Fattura non trovata!");
        }
        if (fatturaAttiva != null || ncAttiva != null) {
            if ((fatturaAttiva != null && fatturaAttiva.getProtocollo_iva() != null) || (ncAttiva != null && ncAttiva.getProtocollo_iva() != null)) {
                V_stm_paramin_ft_attivaHome home = (V_stm_paramin_ft_attivaHome) getHome(userContext, V_stm_paramin_ft_attivaBulk.class);
                V_stm_paramin_ft_attivaBulk vista = new V_stm_paramin_ft_attivaBulk();
                if (fatturaAttiva != null)
                    vista.completeFrom(fatturaAttiva);
                else
                    vista.completeFrom(ncAttiva);
                pg_stampa = callGetPgPerStampa(userContext).longValue();
                vista.setId_report(java.math.BigDecimal.valueOf(pg_stampa));
                vista.setSequenza(new BigDecimal(0));
                vista.setGruppo("A");
                vista.setTipologia_riga("D");
                vista.setDescrizione("Stampa fattura attiva, nota credito-debito su FA");
                vista.setUser(userContext.getUser());
                vista.setToBeCreated();
                home.insert(vista, userContext);
            } else
                throw new FatturaNonProtocollataException("Fattura non protocollata!");
        } else
            throw new FatturaNonTrovataException("Fattura non trovata!");

        return pg_stampa;
    }

    public byte[] lanciaStampa(
            UserContext userContext,
            Long pg_stampa) throws PersistencyException, ComponentException {
        try {

            File output = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", File.separator + getOutputFileName("fatturaattiva_ncd.jasper", pg_stampa));
            Print_spoolerBulk print = new Print_spoolerBulk();
            print.setPgStampa(pg_stampa);
            print.setFlEmail(false);
            print.setReport("/docamm/docamm/fatturaattiva_ncd.jasper");
            print.setNomeFile(getOutputFileName("fatturaattiva_ncd.jasper", pg_stampa));
            print.setUtcr(userContext.getUser());
            print.addParam("id_report", pg_stampa, Long.class);
            print.addParam("Ti_stampa", "R", String.class);
            Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);

            FileOutputStream f = new FileOutputStream(output);
            f.write(report.getBytes());
            return report.getBytes();
        } catch (IOException e) {
            throw new GenerazioneReportException("Generazione Stampa non riuscita", e);
        }
    }

    public File lanciaStampaFatturaElettronica(
            UserContext userContext,
            Fattura_attivaBulk fattura) throws ComponentException {
        try {
            String nomeProgrammaStampa = "fattura_attiva_provvisoria.jasper";
            String nomeFileStampaFattura = getOutputFileNameFatturazioneElettronica(nomeProgrammaStampa, fattura);
            File output = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", File.separator + nomeFileStampaFattura);
            Print_spoolerBulk print = new Print_spoolerBulk();
            print.setFlEmail(false);
            print.setReport("/docamm/docamm/" + nomeProgrammaStampa);
            print.setNomeFile(nomeFileStampaFattura);
            print.setUtcr(userContext.getUser());
            print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
            print.addParam("esercizio", fattura.getEsercizio(), Integer.class);
            print.addParam("cd_uo_origine", fattura.getCd_uo_origine(), String.class);
            print.addParam("pg_fattura", fattura.getPg_fattura_attiva(), Long.class);
            Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);

            FileOutputStream f = new FileOutputStream(output);
            f.write(report.getBytes());
            return output;
        } catch (IOException e) {
            throw new GenerazioneReportException("Generazione Stampa non riuscita", e);
        }
    }

    private String getOutputFileName(String reportName, long pg_stampa)

    {
        String fileName = preparaFileNamePerStampa(reportName);
        fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + pg_stampa + '_' + fileName;
        return fileName;
    }

    private String getOutputFileNameFatturazioneElettronica(String reportName, Fattura_attivaBulk fattura)

    {
        String fileName = preparaFileNamePerStampa(reportName);
        fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + fattura.recuperoIdFatturaAsString() + '_' + fileName;
        return fileName;
    }

    private String preparaFileNamePerStampa(String reportName) {
        String fileName = reportName;
        fileName = fileName.replace('/', '_');
        fileName = fileName.replace('\\', '_');
        if (fileName.startsWith("_"))
            fileName = fileName.substring(1);
        if (fileName.endsWith(".jasper"))
            fileName = fileName.substring(0, fileName.length() - 7);
        fileName = fileName + ".pdf";
        return fileName;
    }

    public OggettoBulk completaOggetto(UserContext aUC, OggettoBulk oggetto) throws PersistencyException, ComponentException {
        if (oggetto instanceof it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk) {
            it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce = (it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk) oggetto;
            return ((it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk) getHome(aUC, it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk.class).findByPrimaryKey(voce));
        }
        if (oggetto instanceof it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk) {
            it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = (it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk) oggetto;
            voce = ((it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk) getHome(aUC, it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk.class).findByPrimaryKey(voce));
            if (voce == null) {
                voce = (it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk) oggetto;
                voce.setCd_titolo_capitolo(voce.getCd_voce());
                voce.setCd_voce(null);
                List voci = (getHome(aUC, it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk.class).find(voce));
                if (voci.size() == 0)
                    voce = null;
                else if (voci.size() == 1)
                    voce = (it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk) voci.get(0);
                else  //non dovrebbe capitare mai!
                    voce = null;
            }
            return voce;
        }
        if (oggetto instanceof TerzoBulk) {
            TerzoBulk terzo = (TerzoBulk) oggetto;
            terzo = ((TerzoBulk) getHome(aUC, TerzoBulk.class).findByPrimaryKey(terzo));
            if (terzo != null)
                terzo.setAnagrafico((AnagraficoBulk) getHome(aUC, AnagraficoBulk.class).findByPrimaryKey(terzo.getAnagrafico()));
            return terzo;
        }
        if (oggetto instanceof ContrattoBulk) {
            ContrattoBulk contratto = (ContrattoBulk) oggetto;
            contratto = ((ContrattoBulk) getHome(aUC, ContrattoBulk.class).findByPrimaryKey(contratto));
            return contratto;
        }
        if (oggetto instanceof WorkpackageBulk) {
            WorkpackageBulk gae = (WorkpackageBulk) oggetto;
            gae = ((WorkpackageBulk) getHome(aUC, WorkpackageBulk.class).findByPrimaryKey(gae));
            if (gae != null)
                gae.setCentro_responsabilita((CdrBulk) getHome(aUC, CdrBulk.class).findByPrimaryKey(gae.getCentro_responsabilita()));
            return gae;
        }
        if (oggetto instanceof CdsBulk) {
            CdsBulk cds = (CdsBulk) oggetto;
            cds = ((CdsBulk) getHome(aUC, CdsBulk.class).findByPrimaryKey(cds));
            return cds;
        }
        if (oggetto instanceof Tipo_sezionaleBulk) {
            Tipo_sezionaleBulk tipo_sez = (Tipo_sezionaleBulk) oggetto;
            tipo_sez = ((Tipo_sezionaleBulk) getHome(aUC, Tipo_sezionaleBulk.class).findByPrimaryKey(tipo_sez));
            return tipo_sez;
        }
        if (oggetto instanceof Elemento_voceBulk) {
            Elemento_voceBulk elemento_v = (Elemento_voceBulk) oggetto;
            elemento_v = ((Elemento_voceBulk) getHome(aUC, Elemento_voceBulk.class).findByPrimaryKey(elemento_v));
            return elemento_v;
        }
        if (oggetto instanceof Fattura_attiva_rigaIBulk) {
            Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) oggetto;
            riga = ((Fattura_attiva_rigaIBulk) getHome(aUC, Fattura_attiva_rigaIBulk.class).findByPrimaryKey(riga));
            return riga;
        }
        if (oggetto instanceof Fattura_attiva_IBulk) {
            Fattura_attiva_IBulk fattura = (Fattura_attiva_IBulk) oggetto;
            fattura = ((Fattura_attiva_IBulk) getHome(aUC, Fattura_attiva_IBulk.class).findByPrimaryKey(fattura));
            return fattura;
        }
        if (oggetto instanceof Rif_modalita_pagamentoBulk) {
            Rif_modalita_pagamentoBulk rif_mod = (Rif_modalita_pagamentoBulk) oggetto;
            rif_mod = ((Rif_modalita_pagamentoBulk) getHome(aUC, Rif_modalita_pagamentoBulk.class).findByPrimaryKey(rif_mod));
            return rif_mod;
        }
        if (oggetto instanceof BancaBulk) {
            BancaBulk banca = (BancaBulk) oggetto;
            banca = ((BancaBulk) getHome(aUC, BancaBulk.class).findByPrimaryKey(banca));
            return banca;
        }
        if (oggetto instanceof Unita_organizzativaBulk) {
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) oggetto;
            uo = ((Unita_organizzativaBulk) getHome(aUC, Unita_organizzativaBulk.class).findByPrimaryKey(uo));
            return uo;
        }
        if (oggetto instanceof CdrBulk) {
            CdrBulk cdr = (CdrBulk) oggetto;
            cdr = ((CdrBulk) getHome(aUC, CdrBulk.class).findByPrimaryKey(cdr));
            return cdr;
        }
        if (oggetto instanceof AccertamentoBulk) {
            AccertamentoBulk acc = (AccertamentoBulk) oggetto;
            acc = ((AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey(acc));
            return acc;
        }
        if (oggetto instanceof Accertamento_scadenzarioBulk) {
            Accertamento_scadenzarioBulk acc = (Accertamento_scadenzarioBulk) oggetto;
            acc = ((Accertamento_scadenzarioBulk) getHome(aUC, Accertamento_scadenzarioBulk.class).findByPrimaryKey(acc));
            return acc;
        }
        if (oggetto instanceof Accertamento_scad_voceBulk) {
            Accertamento_scad_voceBulk acc = (Accertamento_scad_voceBulk) oggetto;
            acc = ((Accertamento_scad_voceBulk) getHome(aUC, Accertamento_scad_voceBulk.class).findByPrimaryKey(acc));
            return acc;
        }
        if (oggetto instanceof ObbligazioneBulk) {
            ObbligazioneBulk obb = (ObbligazioneBulk) oggetto;
            obb = ((ObbligazioneBulk) getHome(aUC, ObbligazioneBulk.class).findByPrimaryKey(obb));
            return obb;
        }
        if (oggetto instanceof Obbligazione_scadenzarioBulk) {
            Obbligazione_scadenzarioBulk obb = (Obbligazione_scadenzarioBulk) oggetto;
            obb = ((Obbligazione_scadenzarioBulk) getHome(aUC, Obbligazione_scadenzarioBulk.class).findByPrimaryKey(obb));
            return obb;
        }
        if (oggetto instanceof Obbligazione_scad_voceBulk) {
            Obbligazione_scad_voceBulk obb = (Obbligazione_scad_voceBulk) oggetto;
            obb = ((Obbligazione_scad_voceBulk) getHome(aUC, Obbligazione_scad_voceBulk.class).findByPrimaryKey(obb));
            return obb;
        }
        if (oggetto instanceof Bene_servizioBulk) {
            Bene_servizioBulk obj = (Bene_servizioBulk) oggetto;
            obj = ((Bene_servizioBulk) getHome(aUC, Bene_servizioBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Modalita_erogazioneBulk) {
            Modalita_erogazioneBulk obj = (Modalita_erogazioneBulk) oggetto;
            obj = ((Modalita_erogazioneBulk) getHome(aUC, Modalita_erogazioneBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Modalita_incassoBulk) {
            Modalita_incassoBulk obj = (Modalita_incassoBulk) oggetto;
            obj = ((Modalita_incassoBulk) getHome(aUC, Modalita_incassoBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Codici_cpaBulk) {
            Codici_cpaBulk obj = (Codici_cpaBulk) oggetto;
            obj = ((Codici_cpaBulk) getHome(aUC, Codici_cpaBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof NazioneBulk) {
            NazioneBulk obj = (NazioneBulk) oggetto;
            obj = ((NazioneBulk) getHome(aUC, NazioneBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof ProvinciaBulk) {
            ProvinciaBulk obj = (ProvinciaBulk) oggetto;
            obj = ((ProvinciaBulk) getHome(aUC, ProvinciaBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Nomenclatura_combinataBulk) {
            Nomenclatura_combinataBulk obj = (Nomenclatura_combinataBulk) oggetto;
            obj = ((Nomenclatura_combinataBulk) getHome(aUC, Nomenclatura_combinataBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Natura_transazioneBulk) {
            Natura_transazioneBulk obj = (Natura_transazioneBulk) oggetto;
            obj = ((Natura_transazioneBulk) getHome(aUC, Natura_transazioneBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Condizione_consegnaBulk) {
            Condizione_consegnaBulk obj = (Condizione_consegnaBulk) oggetto;
            obj = ((Condizione_consegnaBulk) getHome(aUC, Condizione_consegnaBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof Modalita_trasportoBulk) {
            Modalita_trasportoBulk obj = (Modalita_trasportoBulk) oggetto;
            obj = ((Modalita_trasportoBulk) getHome(aUC, Modalita_trasportoBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        if (oggetto instanceof NaturaBulk) {
            NaturaBulk obj = (NaturaBulk) oggetto;
            obj = ((NaturaBulk) getHome(aUC, NaturaBulk.class).findByPrimaryKey(obj));
            return obj;
        }
        return null;
    }

    public boolean VerificaDuplicati(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException, PersistencyException {
        List fatture = null;
        if (fatturaAttiva instanceof Fattura_attiva_IBulk)
            fatture = (getHome(aUC, Fattura_attiva_IBulk.class).find(fatturaAttiva));
        else
            fatture = (getHome(aUC, Nota_di_credito_attivaBulk.class).find(fatturaAttiva));
        if (fatture.size() == 0)
            return false;
        else
            return true;

    }

    public java.util.List findListaModalitaPagamentoWS(UserContext userContext, String terzo, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Modalita_pagamentoHome home = (Modalita_pagamentoHome) getHome(userContext, Modalita_pagamentoBulk.class);
            //SQLBuilder sql=home.createSQLBuilder();
            SQLBuilder sql = (SQLBuilder) super.select(userContext, null, new Modalita_pagamentoBulk());
            sql.addTableToHeader("RIF_MODALITA_PAGAMENTO");

            sql.addSQLJoin("RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG", "MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
            sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terzo);
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_MODALITA_PAG", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_MODALITA_PAG");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaBancheWS(UserContext userContext, String terzo, String modalita, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Rif_modalita_pagamentoBulk rif_mod = (Rif_modalita_pagamentoBulk) getHome(userContext, Rif_modalita_pagamentoBulk.class).findByPrimaryKey(new Rif_modalita_pagamentoBulk(modalita));
            BancaHome home = (BancaHome) getHome(userContext, BancaBulk.class);
            SQLBuilder sql = home.selectBancaFor(rif_mod, new Integer(terzo));
            sql.addTableToHeader("MODALITA_PAGAMENTO");
            sql.addSQLJoin("MODALITA_PAGAMENTO.CD_TERZO", sql.EQUALS, "BANCA.CD_TERZO");
            sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_MODALITA_PAG", sql.EQUALS, rif_mod.getCd_modalita_pag());
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "PG_BANCA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "INTESTAZIONE");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaRigheperNCWS(UserContext userContext, String uo, String terzo, String ti_causale, String esercizio,
                                                String query, String dominio, String tipoRicerca) throws ComponentException {
        try {

            Unita_organizzativaHome uo_home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk u_org = (Unita_organizzativaBulk) uo_home.findByPrimaryKey(new Unita_organizzativaBulk(uo));

            Fattura_attiva_rigaIHome home = (Fattura_attiva_rigaIHome) getHome(userContext, Fattura_attiva_rigaIBulk.class);
            SQLBuilder sql = (SQLBuilder) super.select(userContext, null, new Fattura_attiva_rigaIBulk());
            SQLBuilder sql_exists = home.createSQLBuilder();
            SQLBuilder sql_exists2 = home.createSQLBuilder();
            sql.addTableToHeader("FATTURA_ATTIVA");
            sql.addSQLJoin("FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, "FATTURA_ATTIVA_RIGA.ESERCIZIO");
            sql.addSQLJoin("FATTURA_ATTIVA.CD_CDS", sql.EQUALS, "FATTURA_ATTIVA_RIGA.CD_CDS");
            sql.addSQLJoin("FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, "FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA");
            sql.addSQLJoin("FATTURA_ATTIVA.PG_FATTURA_ATTIVA", sql.EQUALS, "FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA");
            sql.addSQLClause("AND", "cd_terzo", sql.EQUALS, terzo);
            sql.addSQLClause("AND", "cd_cds_origine", sql.EQUALS, u_org.getCd_cds());
            sql.addSQLClause("AND", "cd_uo_origine", sql.EQUALS, u_org.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "FATTURA_ATTIVA.stato_cofi", sql.NOT_EQUALS, Fattura_attiva_IBulk.STATO_ANNULLATO);
            sql.addClause("AND", "fl_congelata", sql.EQUALS, Boolean.FALSE);
            sql.addSQLClause("AND", "ti_causale_emissione", sql.EQUALS, ti_causale);
            sql.addSQLClause("AND", "FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, esercizio);
            sql.addSQLClause("AND", "IM_DIPONIBILE_NC", sql.GREATER, new BigDecimal("0"));
            sql.addSQLClause("AND", "TI_FATTURA", sql.EQUALS, Fattura_attiva_IBulk.TIPO_FATTURA_ATTIVA);
            if (esercizio.compareTo(CNRUserContext.getEsercizio(userContext).toString()) == 0) {
                // Bisogna selezionare solo quelle non riportate (ne parzialmente, ne totalmente)
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO", sql.EQUALS, "FATTURA_ATTIVA.ESERCIZIO");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS", sql.EQUALS, "FATTURA_ATTIVA.CD_CDS");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA", sql.EQUALS, "FATTURA_ATTIVA.PG_FATTURA_ATTIVA");
                sql_exists.openParenthesis("AND");
                sql_exists.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.ISNOTNULL, null);
                sql_exists.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.GREATER, esercizio);
                sql_exists.openParenthesis("OR");
                sql_exists.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.ISNOTNULL, null);
                sql_exists.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.GREATER, esercizio);
                sql_exists.closeParenthesis();
                sql_exists.closeParenthesis();
                sql.addSQLNotExistsClause("AND", sql_exists);
            } else {
                // Bisogna selezionare solo quelle totalmente riportate
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO", sql.EQUALS, "FATTURA_ATTIVA.ESERCIZIO");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS", sql.EQUALS, "FATTURA_ATTIVA.CD_CDS");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA");
                sql_exists.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA", sql.EQUALS, "FATTURA_ATTIVA.PG_FATTURA_ATTIVA");
                sql_exists.openParenthesis("AND");
                sql_exists.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.ISNOTNULL, null);
                sql_exists.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.EQUALS, CNRUserContext.getEsercizio(userContext).toString());
                sql_exists.openParenthesis("OR");
                sql_exists.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.ISNOTNULL, null);
                sql_exists.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.EQUALS, CNRUserContext.getEsercizio(userContext).toString());
                sql_exists.closeParenthesis();
                sql_exists.closeParenthesis();
                sql.addSQLExistsClause("AND", sql_exists);
                sql_exists2.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO", sql.EQUALS, "FATTURA_ATTIVA.ESERCIZIO");
                sql_exists2.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS", sql.EQUALS, "FATTURA_ATTIVA.CD_CDS");
                sql_exists2.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA");
                sql_exists2.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA", sql.EQUALS, "FATTURA_ATTIVA.PG_FATTURA_ATTIVA");
                sql_exists2.openParenthesis("AND");
                sql_exists2.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.ISNOTNULL, null);
                sql_exists2.addSQLClause("AND", "ESERCIZIO_ACCERTAMENTO", sql.LESS, CNRUserContext.getEsercizio(userContext).toString());
                sql_exists2.openParenthesis("OR");
                sql_exists2.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.ISNOTNULL, null);
                sql_exists2.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.LESS, CNRUserContext.getEsercizio(userContext).toString());
                sql_exists2.closeParenthesis();
                sql_exists2.closeParenthesis();
                sql.addSQLNotExistsClause("AND", sql_exists2);
            }
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "PG_FATTURA_ATTIVA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_FATTURA_ATTIVA");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public Fattura_attivaBulk ricercaFattura(
            UserContext userContext,
            Long esercizio,
            String cd_cds,
            String cd_unita_organizzativa,
            Long pg_fattura) throws PersistencyException, ComponentException {
        try {
            Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
            fatturaAttiva.setEsercizio(esercizio.intValue());
            fatturaAttiva.setCd_cds_origine(cd_cds);
            fatturaAttiva.setCd_uo_origine(cd_unita_organizzativa);
            fatturaAttiva.setPg_fattura_attiva(pg_fattura);

            List fatture = (getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva));
            if (fatture.size() == 0)
                fatturaAttiva = null;
            else if (fatture.size() == 1) {
                fatturaAttiva = (Fattura_attiva_IBulk) fatture.get(0);
                BulkList dettagli = new BulkList(findDettagli(userContext, fatturaAttiva));
                BulkList dettagli_intra = new BulkList(findDettagliIntrastat(userContext, fatturaAttiva));
                fatturaAttiva.setFattura_attiva_dettColl(dettagli);
                fatturaAttiva.setFattura_attiva_intrastatColl(dettagli_intra);
            } else  //non dovrebbe capitare mai!
                throw new FatturaNonTrovataException("Fattura non trovata!");

            Nota_di_credito_attivaBulk ncAttiva = null;
            if (fatturaAttiva == null) {
                ncAttiva = new Nota_di_credito_attivaBulk();
                ncAttiva.setEsercizio(esercizio.intValue());
                ncAttiva.setCd_cds_origine(cd_cds);
                ncAttiva.setCd_uo_origine(cd_unita_organizzativa);
                ncAttiva.setPg_fattura_attiva(pg_fattura);
                List notec = (getHome(userContext, Nota_di_credito_attivaBulk.class).find(ncAttiva));
                if (notec.size() == 0)
                    ncAttiva = null;
                else if (notec.size() == 1) {
                    ncAttiva = (Nota_di_credito_attivaBulk) notec.get(0);
                    BulkList dettagli = new BulkList(findDettagli(userContext, ncAttiva));
                    ncAttiva.setFattura_attiva_dettColl(dettagli);
                } else  //non dovrebbe capitare mai!
                    throw new FatturaNonTrovataException("Fattura non trovata!");
            }

            if (fatturaAttiva != null)
                return fatturaAttiva;
            else if (ncAttiva != null)
                return ncAttiva;
            else
                throw new FatturaNonTrovataException("Fattura non trovata!");
        } catch (IntrospectionException e) {
            throw handleException(e);
        }

    }

    private Fattura_attivaBulk ricercaFatturaTrovato(
            UserContext userContext,
            Long esercizio,
            String cd_cds,
            String cd_unita_organizzativa,
            Long pg_fattura,
            boolean byKey) throws PersistencyException, ComponentException {
        try {
            Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
            fatturaAttiva.setEsercizio(esercizio.intValue());
            if (byKey) {
                fatturaAttiva.setCd_cds(cd_cds);
                fatturaAttiva.setCd_unita_organizzativa(cd_unita_organizzativa);
            } else {
                fatturaAttiva.setCd_cds_origine(cd_cds);
                fatturaAttiva.setCd_uo_origine(cd_unita_organizzativa);
            }

            fatturaAttiva.setPg_fattura_attiva(pg_fattura);

            List fatture = (getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva));
            if (fatture.size() == 0)
                fatturaAttiva = null;
            else if (fatture.size() == 1) {
                fatturaAttiva = (Fattura_attiva_IBulk) fatture.get(0);
                BulkList dettagli = new BulkList(findDettagli(userContext, fatturaAttiva));
                BulkList dettagli_intra = new BulkList(findDettagliIntrastat(userContext, fatturaAttiva));
                fatturaAttiva.setFattura_attiva_dettColl(dettagli);
                fatturaAttiva.setFattura_attiva_intrastatColl(dettagli_intra);
            } else  //non dovrebbe capitare mai!
                throw new FatturaNonTrovataException("Fattura non trovata!");

            Nota_di_credito_attivaBulk ncAttiva = null;
            if (fatturaAttiva == null) {
                ncAttiva = new Nota_di_credito_attivaBulk();
                ncAttiva.setEsercizio(esercizio.intValue());
                if (byKey) {
                    ncAttiva.setCd_cds(cd_cds);
                    ncAttiva.setCd_unita_organizzativa(cd_unita_organizzativa);
                } else {
                    ncAttiva.setCd_cds_origine(cd_cds);
                    ncAttiva.setCd_uo_origine(cd_unita_organizzativa);
                }
                ncAttiva.setPg_fattura_attiva(pg_fattura);
                List notec = (getHome(userContext, Nota_di_credito_attivaBulk.class).find(ncAttiva));
                if (notec.size() == 0)
                    ncAttiva = null;
                else if (notec.size() == 1) {
                    ncAttiva = (Nota_di_credito_attivaBulk) notec.get(0);
                    BulkList dettagli = new BulkList(findDettagli(userContext, ncAttiva));
                    ncAttiva.setFattura_attiva_dettColl(dettagli);
                } else  //non dovrebbe capitare mai!
                    throw new FatturaNonTrovataException("Fattura non trovata!");
            }

            caricaDettagliFatturaTrovato(userContext, fatturaAttiva);
            getHomeCache(userContext).fetchAll(userContext);
            if (fatturaAttiva != null)
                return fatturaAttiva;
            else if (ncAttiva != null)
                return ncAttiva;
            else
                throw new FatturaNonTrovataException("Fattura non trovata!");
        } catch (IntrospectionException e) {
            throw handleException(e);
        }

    }

    private void caricaDettagliFatturaTrovato(UserContext userContext, BulkList<Fattura_attivaBulk> fatture) throws ComponentException, PersistencyException {
        for (Iterator<Fattura_attivaBulk> i = fatture.iterator(); i.hasNext(); ) {
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) i.next();
            caricaDettagliFatturaTrovato(userContext, fattura);
        }
    }

    private void caricaDettagliFatturaTrovato(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException, PersistencyException {
        if (fattura != null) {
            BulkList<Fattura_attiva_rigaBulk> dett = fattura.getFattura_attiva_dettColl();
            for (Iterator<Fattura_attiva_rigaBulk> j = dett.iterator(); j.hasNext(); ) {
                Fattura_attiva_rigaIBulk det = (Fattura_attiva_rigaIBulk) j.next();
                det.setVoce_iva((Voce_ivaBulk) getHome(userContext, Voce_ivaBulk.class).findByPrimaryKey(det.getVoce_iva()));

                if (det.getTi_associato_manrev() != null && det.ASSOCIATO_A_MANDATO.equalsIgnoreCase(det.getTi_associato_manrev())) {
                    SQLBuilder sql = getHome(userContext, Reversale_rigaIBulk.class).createSQLBuilder();
                    sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, det.getCd_cds());
                    sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, det.getCd_unita_organizzativa());
                    sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, det.getEsercizio());
                    sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA);
                    sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, det.getPg_fattura_attiva());
                    sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS, Reversale_rigaBulk.STATO_ANNULLATO);
                    List result = getHome(userContext, Reversale_rigaIBulk.class).fetchAll(sql);
                    BulkList bl = det.getReversaliRighe();
                    for (Iterator k = result.iterator(); k.hasNext(); ) {
                        Reversale_rigaIBulk revr = (Reversale_rigaIBulk) k.next();
                        revr.setReversale((ReversaleIBulk) getHome(userContext, ReversaleIBulk.class).findByPrimaryKey(revr.getReversale()));
                        bl.add(revr);
                    }
                }
            }
        }
    }

    public Fattura_attivaBulk ricercaFatturaTrovato(
            UserContext userContext,
            Long esercizio,
            String cd_cds,
            String cd_unita_organizzativa,
            Long pg_fattura) throws PersistencyException, ComponentException {
        return ricercaFatturaTrovato(
                userContext,
                esercizio,
                cd_cds,
                cd_unita_organizzativa,
                pg_fattura,
                false);
    }

    public Fattura_attivaBulk ricercaFatturaByKey(
            UserContext userContext,
            Long esercizio,
            String cd_cds,
            String cd_unita_organizzativa,
            Long pg_fattura) throws PersistencyException, ComponentException {
        return ricercaFatturaTrovato(
                userContext,
                esercizio,
                cd_cds,
                cd_unita_organizzativa,
                pg_fattura,
                true);
    }

    public List<Fattura_attivaBulk> ricercaFattureTrovato(
            UserContext userContext,
            Long trovato) throws PersistencyException, ComponentException {
        try {
            Fattura_attiva_rigaIBulk fatturaAttivaRiga = new Fattura_attiva_rigaIBulk();
            fatturaAttivaRiga.setPg_trovato(trovato);
            List fattureRighe = (getHome(userContext, Fattura_attiva_rigaIBulk.class).find(fatturaAttivaRiga));
            BulkList fatture = new BulkList();
            for (Iterator<Fattura_attiva_rigaIBulk> i = fattureRighe.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk fatr = (Fattura_attiva_rigaBulk) i.next();

                Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
                fatturaAttiva.setEsercizio(fatr.getEsercizio().intValue());
                fatturaAttiva.setCd_cds(fatr.getCd_cds());
                fatturaAttiva.setCd_unita_organizzativa(fatr.getCd_unita_organizzativa());
                fatturaAttiva.setPg_fattura_attiva(fatr.getPg_fattura_attiva());

                fatturaAttiva = (Fattura_attiva_IBulk) (getHome(userContext, Fattura_attiva_IBulk.class).findByPrimaryKey(fatturaAttiva));
                if (fatturaAttiva == null)
                    throw new FatturaNonTrovataException("Fattura non trovata!");
                else {
                    BulkList dettagli = new BulkList(findDettagli(userContext, fatturaAttiva));
                    BulkList dettagli_intra = new BulkList(findDettagliIntrastat(userContext, fatturaAttiva));
                    fatturaAttiva.setFattura_attiva_dettColl(dettagli);
                    fatturaAttiva.setFattura_attiva_intrastatColl(dettagli_intra);
                }
                if (!fatture.containsByPrimaryKey(fatturaAttiva))
                    fatture.add(fatturaAttiva);
            }

            caricaDettagliFatturaTrovato(userContext, fatture);

            return fatture;
        } catch (IntrospectionException e) {
            throw handleException(e);
        }
    }

    public List recuperoScadVoce(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof Accertamento_scadenzarioBulk) {
                Accertamento_scadenzarioBulk accScad = (Accertamento_scadenzarioBulk) bulk;
                Accertamento_scad_voceBulk accScadVoce = new Accertamento_scad_voceBulk();
                accScadVoce.setAccertamento_scadenzario(accScad);
                return (getHome(userContext, Accertamento_scad_voceBulk.class).find(accScadVoce));
            } else if (bulk instanceof Obbligazione_scadenzarioBulk) {
                Obbligazione_scadenzarioBulk obbScad = (Obbligazione_scadenzarioBulk) bulk;
                Obbligazione_scad_voceBulk obbScadVoce = new Obbligazione_scad_voceBulk();
                obbScadVoce.setObbligazione_scadenzario(obbScad);
                return (getHome(userContext, Obbligazione_scad_voceBulk.class).find(obbScadVoce));
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        }

        return null;
    }

    public List findManRevRigaCollegati(UserContext userContext, Fattura_attiva_rigaBulk fatturaAttivaRiga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException {
        List result = null;
        if (fatturaAttivaRiga.getAccertamento_scadenzario() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getCd_cds() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getEsercizio() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento() != null &&
                fatturaAttivaRiga.getAccertamento_scadenzario().getPg_accertamento_scadenzario() != null) {

            SQLBuilder sql = getHome(userContext, Reversale_rigaIBulk.class).createSQLBuilder();
            sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_cds());
            sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_unita_organizzativa());
            sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getEsercizio());
            sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA);
            sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getPg_fattura_attiva());
            sql.addClause(FindClause.AND, "cd_cds_accertamento", SQLBuilder.EQUALS, fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getCd_cds());
            sql.addClause(FindClause.AND, "esercizio_accertamento", SQLBuilder.EQUALS, fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getEsercizio());
            sql.addClause(FindClause.AND, "esercizio_ori_accertamento", SQLBuilder.EQUALS, fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale());
            sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, fatturaAttivaRiga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento());
            sql.addClause(FindClause.AND, "pg_accertamento_scadenzario", SQLBuilder.EQUALS, fatturaAttivaRiga.getAccertamento_scadenzario().getPg_accertamento_scadenzario());

            result = getHome(userContext, Reversale_rigaIBulk.class).fetchAll(sql);
        } else if (fatturaAttivaRiga.getCd_cds_obbligazione() != null &&
                fatturaAttivaRiga.getEsercizio_obbligazione() != null &&
                fatturaAttivaRiga.getEsercizio_ori_obbligazione() != null &&
                fatturaAttivaRiga.getPg_obbligazione() != null &&
                fatturaAttivaRiga.getPg_obbligazione_scadenzario() != null) {
            SQLBuilder sql = getHome(userContext, Mandato_rigaIBulk.class).createSQLBuilder();
            sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_cds());
            sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_unita_organizzativa());
            sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getEsercizio());
            sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA);
            sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, fatturaAttivaRiga.getPg_fattura_attiva());
            sql.addClause(FindClause.AND, "cd_cds_obbligazione", SQLBuilder.EQUALS, fatturaAttivaRiga.getCd_cds_obbligazione());
            sql.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, fatturaAttivaRiga.getEsercizio_obbligazione());
            sql.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, fatturaAttivaRiga.getEsercizio_ori_obbligazione());
            sql.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, fatturaAttivaRiga.getPg_obbligazione());
            sql.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, fatturaAttivaRiga.getPg_obbligazione_scadenzario());

            result = getHome(userContext, Mandato_rigaIBulk.class).fetchAll(sql);
        }

        return result;
    }

    private void aggiornaDataEsigibilitaIVA(
            UserContext userContext,
            Fattura_attivaBulk fattura,
            String creaModifica)
            throws ComponentException {

        if (creaModifica.compareTo("C") != 0 && (fattura == null || fattura.isStampataSuRegistroIVA()))
            return;
        try {
            for (Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();

                if (Fattura_attivaBulk.NON_ASSOCIATO_A_MANDATO.equalsIgnoreCase(dettaglio.getTi_associato_manrev())) {
                    if (fattura.getFl_liquidazione_differita().booleanValue()) {
                        dettaglio.setData_esigibilita_iva(null);
                    } else
                        dettaglio.setData_esigibilita_iva(fattura.getDt_registrazione());

                    updateBulk(userContext, dettaglio);
                }
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public void controllaQuadraturaIntrastat(UserContext aUC, Fattura_attivaBulk fattura)
            throws ComponentException {
        try {

            if (fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue() &&
                    fattura.getCliente() != null && fattura.getCliente().getAnagrafico() != null && fattura.getCliente().getAnagrafico().getPartita_iva() != null) {
                Boolean trovato = false;
                Boolean obbligatorio = false;

                Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(aUC, fattura.getEsercizio());
                if (par != null && par.getFl_obb_intrastat() != null && par.getFl_obb_intrastat().booleanValue()) {
                    for (Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                        Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                        if (dettaglio.getBene_servizio() != null && dettaglio.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
                            obbligatorio = true;
                    }
                    if (obbligatorio && fattura.getFattura_attiva_intrastatColl() != null && fattura.getFattura_attiva_intrastatColl().isEmpty())
                        trovato = true;
                    else if (!obbligatorio && fattura.getFattura_attiva_intrastatColl() != null && !fattura.getFattura_attiva_intrastatColl().isEmpty())
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: non indicare i dati Intrastat");
                    if (trovato && (fattura instanceof Fattura_attiva_IBulk))
                        throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare i dati Intrastat");

                }
            }
        } catch (RemoteException t) {
            throw handleException(fattura, t);
        } catch (ComponentException t) {
            throw handleException(fattura, t);
        }
    }

    private java.util.List findDettagliIntrastat(UserContext aUC, Fattura_attivaBulk fattura) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

        if (fattura == null &&
                !fattura.getFl_intra_ue().booleanValue()) return null;

        it.cnr.jada.bulk.BulkHome home = getHome(aUC, Fattura_attiva_intraBulk.class, null, "NoFatturaAttivaBulk");

        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "pg_fattura_attiva", sql.EQUALS, fattura.getPg_fattura_attiva());
        sql.addClause("AND", "cd_cds", sql.EQUALS, fattura.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, fattura.getEsercizio());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fattura.getCd_unita_organizzativa());
        return home.fetchAll(sql);
    }

    private Fattura_attivaBulk completeWithCondizioneConsegna(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {
        Condizione_consegnaHome home = (Condizione_consegnaHome) getHome(userContext, Condizione_consegnaBulk.class);
        try {
            fattura.setCondizione_consegnaColl(home.fetchAll(home.selectByClause(userContext, null)));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fattura, e);
        }
        return fattura;
    }

    private Fattura_attivaBulk completeWithModalitaTrasporto(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {
        Modalita_trasportoHome home = (Modalita_trasportoHome) getHome(userContext, Modalita_trasportoBulk.class);
        try {
            fattura.setModalita_trasportoColl(home.fetchAll(home.selectByClause(userContext, null)));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fattura, e);
        }
        return fattura;
    }

    private Fattura_attivaBulk completeWithModalitaIncasso(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {
        Modalita_incassoHome home = (Modalita_incassoHome) getHome(userContext, Modalita_incassoBulk.class);
        try {
            fattura.setModalita_incassoColl(home.fetchAll(home.selectByClause(userContext, null)));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fattura, e);
        }
        return fattura;
    }

    private Fattura_attivaBulk completeWithModalitaErogazione(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {
        Modalita_erogazioneHome home = (Modalita_erogazioneHome) getHome(userContext, Modalita_erogazioneBulk.class);
        try {
            fattura.setModalita_erogazioneColl(home.fetchAll(home.selectByClause(userContext, null)));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fattura, e);
        }
        return fattura;
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectBene_servizioByClause(
            UserContext userContext,
            Fattura_attiva_rigaBulk dettaglio,
            Bene_servizioBulk beneServizio,
            CompoundFindClause clauses)
            throws ComponentException {
        Bene_servizioHome beneServizioHome = (Bene_servizioHome) getHome(userContext, Bene_servizioBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = beneServizioHome.createSQLBuilder();
        sql.addClause("AND", "ti_bene_servizio", sql.EQUALS, dettaglio.getFattura_attiva().getTi_bene_servizio());
        sql.addClause(clauses);
        return sql;
    }

    public java.util.List findListaBeneServizioWS(UserContext userContext, String query, String tipo, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Bene_servizioHome home = (Bene_servizioHome) getHome(userContext, Bene_servizioBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            if (tipo.compareTo(Bene_servizioBulk.BENE_SERVIZIO) != 0)
                sql.addSQLClause("AND", "TI_BENE_SERVIZIO", SQLBuilder.EQUALS, tipo.toUpperCase());
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_BENE_SERVIZIO", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_BENE_SERVIZIO");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaModalitaIncassoWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Modalita_incassoHome home = (Modalita_incassoHome) getHome(userContext, Modalita_incassoBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_MODALITA_INCASSO", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_MODALITA_INCASSO");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaModalitaErogazioneWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Modalita_erogazioneHome home = (Modalita_erogazioneHome) getHome(userContext, Modalita_erogazioneBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_MODALITA_EROGAZIONE", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_MODALITA_EROGAZIONE");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaCodiciCpaWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Codici_cpaHome home = (Codici_cpaHome) getHome(userContext, Codici_cpaBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "TI_BENE_SERVIZIO", sql.EQUALS, "S");
            sql.addSQLClause("AND", "FL_UTILIZZABILE", sql.EQUALS, "Y");
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_CPA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_CPA");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaNazioneWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            NazioneHome home = (NazioneHome) getHome(userContext, NazioneBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_NAZIONE", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_NAZIONE");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    private SQLBuilder completaRicercaDominioDescrizione(SQLBuilder sql, String tipoRicerca, String query, String campoDs) {
        sql.openParenthesis("AND");
        for (StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements(); ) {
            String queryDetail = stringtokenizer.nextToken();
            if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva")) || tipoRicerca == null) {
                if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
                    sql.addSQLClause("AND", campoDs, SQLBuilder.CONTAINS, queryDetail);
                else {
                    sql.openParenthesis("AND");
                    sql.addSQLClause("OR", campoDs, SQLBuilder.CONTAINS, queryDetail);
                    sql.addSQLClause("OR", campoDs, SQLBuilder.CONTAINS, RemoveAccent.convert(queryDetail));
                    sql.closeParenthesis();
                }
            } else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
                if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))) {
                    sql.openParenthesis("AND");
                    sql.addSQLClause("AND", "UPPER(" + campoDs + ")", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                    sql.addSQLClause("OR", campoDs, SQLBuilder.STARTSWITH, queryDetail + " ");
                    sql.addSQLClause("OR", campoDs, SQLBuilder.ENDSWITH, " " + queryDetail);
                    sql.closeParenthesis();
                } else {
                    sql.openParenthesis("AND");
                    sql.openParenthesis("AND");
                    sql.addSQLClause("OR", "UPPER(" + campoDs + ")", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                    sql.addSQLClause("OR", "UPPER(" + campoDs + ")", SQLBuilder.EQUALS, RemoveAccent.convert(queryDetail).toUpperCase());
                    sql.closeParenthesis();
                    sql.openParenthesis("OR");
                    sql.addSQLClause("OR", campoDs, SQLBuilder.STARTSWITH, queryDetail + " ");
                    sql.addSQLClause("OR", campoDs, SQLBuilder.STARTSWITH, RemoveAccent.convert(queryDetail) + " ");
                    sql.closeParenthesis();
                    sql.openParenthesis("OR");
                    sql.addSQLClause("OR", campoDs, SQLBuilder.ENDSWITH, " " + queryDetail);
                    sql.addSQLClause("OR", campoDs, SQLBuilder.ENDSWITH, " " + RemoveAccent.convert(queryDetail));
                    sql.closeParenthesis();
                    sql.closeParenthesis();
                }
            }
        }
        sql.closeParenthesis();
        sql.addOrderBy(campoDs);
        return sql;
    }

    public java.util.List findListaNomenclaturaCombinataWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Nomenclatura_combinataHome home = (Nomenclatura_combinataHome) getHome(userContext, Nomenclatura_combinataBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "ESERCIZIO_INIZIO", sql.GREATER_EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "ESERCIZIO_FINE", sql.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_NOMENCLATURA_COMBINATA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_NOMENCLATURA_COMBINATA");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaNaturaTransazioneWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Natura_transazioneHome home = (Natura_transazioneHome) getHome(userContext, Natura_transazioneBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_NATURA_TRANSAZIONE", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_NATURA_TRANSAZIONE");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaCondizioneConsegnaWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Condizione_consegnaHome home = (Condizione_consegnaHome) getHome(userContext, Condizione_consegnaBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_INCOTERM", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_CONDIZIONE_CONSEGNA");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaModalitaTrasportoWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            Modalita_trasportoHome home = (Modalita_trasportoHome) getHome(userContext, Modalita_trasportoBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_MODALITA_TRASPORTO", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_MODALITA_TRASPORTO");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List findListaProvinciaWS(UserContext userContext, String query, String dominio, String tipoRicerca) throws ComponentException {
        try {
            ProvinciaHome home = (ProvinciaHome) getHome(userContext, ProvinciaBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_PROVINCIA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                sql = completaRicercaDominioDescrizione(sql, tipoRicerca, query, "DS_PROVINCIA");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    @Deprecated
    public Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(UserContext userContext, Fattura_attiva_IBulk fa, Integer esercizio) throws ComponentException, RemoteException, PersistencyException {
        return generaNotaCreditoAutomatica(userContext, fa, esercizio, true);
    }

    private Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(UserContext userContext, Fattura_attiva_IBulk fa, Integer esercizio, Boolean isNotaEsterna) throws ComponentException, RemoteException, PersistencyException {
        fa = (Fattura_attiva_IBulk) inizializzaBulkPerModifica(userContext, fa);
        if (fa.isPagata()){
            throw new it.cnr.jada.comp.ApplicationException("La fattura "+fa.getEsercizio()+"-"+fa.getPg_fattura_attiva()+" è già incassata. Non è possibile emettere la nota di credito.");
        }

        Nota_di_credito_attivaBulk notaDiCredito = new Nota_di_credito_attivaBulk(fa, esercizio);
        notaDiCredito.setCaricaDatiPerFatturazioneElettronica(!isNotaEsterna);
        if (isNotaEsterna) {
            notaDiCredito.setCodiceUnivocoUfficioIpa(fa.getCodiceUnivocoUfficioIpa());
            notaDiCredito.setFlFatturaElettronica(fa.getFlFatturaElettronica());
            notaDiCredito.setPecFatturaElettronica(fa.getPecFatturaElettronica());
            notaDiCredito.setMailFatturaElettronica(fa.getMailFatturaElettronica());
            notaDiCredito.setCodiceDestinatarioFatt(fa.getCodiceDestinatarioFatt());
        }
        notaDiCredito.setDt_termine_creazione_docamm(notaDiCredito.getDt_termine_creazione_docamm());

        /**
         * Aggiunge sul modello corrente del target i dettagli selzionati sul documento amministrativo di origine
         * Ogni dettaglio deve essere in stato iniziale.
         */
        it.cnr.jada.bulk.BulkList dettagliNdC = notaDiCredito.getFattura_attiva_dettColl();
        if (dettagliNdC == null) {
            dettagliNdC = new it.cnr.jada.bulk.BulkList();
            notaDiCredito.setFattura_attiva_dettColl(dettagliNdC);
        }

        for (Iterator iterator = fa.getFattura_attiva_dettColl().iterator(); iterator.hasNext(); ) {
            Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk) iterator.next();
            //controlla se uno o più dettagli non sono stati aggiunti per mancanza di disponibiltà
            if (!Fattura_attiva_rigaIBulk.STATO_INIZIALE.equals(dettaglio.getStato_cofi())) {
                Nota_di_credito_attiva_rigaBulk dettaglioNdC = new Nota_di_credito_attiva_rigaBulk();
                dettaglioNdC.setNotaDiCredito(notaDiCredito);
                dettaglioNdC.setToBeCreated();
                try {
                    dettaglioNdC.copyFrom(dettaglio);
                    if (dettaglioNdC.getIm_imponibile().compareTo(BigDecimal.ZERO) == 0 && dettaglio.getIm_diponibile_nc().compareTo(BigDecimal.ZERO) == 1) {
                        dettaglioNdC.setQuantita(BigDecimal.ONE);
                        dettaglioNdC.setPrezzo_unitario(dettaglio.getIm_diponibile_nc().divide(dettaglio.getIm_iva().divide(dettaglio.getIm_imponibile()).add(BigDecimal.ONE)));
                        dettaglioNdC.setFl_iva_forzata(Boolean.FALSE);
                        dettaglioNdC.calcolaCampiDiRiga();
                        java.math.BigDecimal vecchioTotale = new java.math.BigDecimal(0).setScale(0, java.math.BigDecimal.ROUND_HALF_UP);
                        java.math.BigDecimal totaleDiRiga = dettaglioNdC.getIm_imponibile().add(dettaglioNdC.getIm_iva());
                        java.math.BigDecimal nuovoImportoDisponibile = dettaglio.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
                        dettaglio.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    }
                    dettagliNdC.add(dettaglioNdC);
                } catch (FillException e) {
                }
            }
        }

        notaDiCredito = stornaDettagli(userContext, notaDiCredito, dettagliNdC, null);

        AccertamentoAbstractComponentSession h = (AccertamentoAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession", AccertamentoAbstractComponentSession.class);

        AccertamentiTable accertamentiHash = notaDiCredito.getFattura_attiva_accertamentiHash();
        if (accertamentiHash != null) {
            for (java.util.Enumeration key = accertamentiHash.keys(); key.hasMoreElements(); ) {
                Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) key.nextElement();
                BigDecimal totaleDaSottrarre = BigDecimal.ZERO;
                for (Iterator<Fattura_attiva_rigaBulk> iterator2 = ((java.util.List) accertamentiHash.get(scadenza)).iterator(); iterator2.hasNext(); ) {
                    Fattura_attiva_rigaBulk rigaFattura = iterator2.next();
                    if (notaDiCredito.quadraturaInDeroga())
                        totaleDaSottrarre = totaleDaSottrarre.add(rigaFattura.getIm_imponibile());
                    else
                        totaleDaSottrarre = totaleDaSottrarre.add(rigaFattura.getIm_totale_divisa());
                }
                if (totaleDaSottrarre.compareTo(BigDecimal.ZERO) != 0) {
                    try {
                        Accertamento_scadenzarioBulk sca = (Accertamento_scadenzarioBulk) h.modificaScadenzaInAutomatico(userContext,
                                scadenza,
                                scadenza.getIm_scadenza().subtract(totaleDaSottrarre),
                                false, true);
                        for (Iterator<Fattura_attiva_rigaBulk> iterator2 = ((java.util.List) accertamentiHash.get(scadenza)).iterator(); iterator2.hasNext(); ) {
                            Fattura_attiva_rigaBulk rigaFattura = iterator2.next();
                            rigaFattura.setAccertamento_scadenzario(sca);
                        }
                    } catch (it.cnr.jada.comp.ComponentException e) {
                        if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                            throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                        if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                            throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                        throw e;
                    }
                }
            }
        }
        notaDiCredito.setFattura_attiva_accertamentiHash(null);
        rebuildAccertamenti(userContext, notaDiCredito);
        if (!isNotaEsterna) {
            impostaDocumentoDaNonInviare(notaDiCredito);
        }
        notaDiCredito.setToBeCreated();

        Nota_di_credito_attivaBulk notaCredito = (Nota_di_credito_attivaBulk) creaConBulk(userContext, notaDiCredito);
        return notaCredito;
    }

    private void impostaDocumentoDaNonInviare(Fattura_attivaBulk documento) {
        documento.setNcAnnulloSdi("S");
    }

    private void protocollazione(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException,
            PersistencyException {
        Long pgProtocollazione = callGetPgPerProtocolloIVA(userContext);
        Long pgStampa = callGetPgPerStampa(userContext);
        Timestamp dataStampa = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
        Integer offSet = 0;
        preparaProtocollazioneEProtocolla(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
    }

    public Fattura_attiva_IBulk ricercaFatturaSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
        fatturaAttiva.setCodiceInvioSdi(codiceInvioSdi);

        List<?> fatture = getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva);
        if (fatture.size() == 1)
            fatturaAttiva = (Fattura_attiva_IBulk) fatture.get(0);
        else  //non dovrebbe capitare mai!
            throw new FatturaNonTrovataException("Fattura non trovata!");
        return fatturaAttiva;
    }

    public Fattura_attivaBulk ricercaFatturaDaCodiceSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
        fatturaAttiva.setCodiceInvioSdi(codiceInvioSdi);
        List lista = (getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva));
        if (lista == null || lista.isEmpty()) {
            Nota_di_credito_attivaBulk nota = new Nota_di_credito_attivaBulk();
            nota.setCodiceInvioSdi(codiceInvioSdi);
            List listaNota = (getHome(userContext, Nota_di_credito_attivaBulk.class).find(nota));

            if (listaNota == null || listaNota.isEmpty()) {
                return null;
            } else if (listaNota.size() == 1) {
                return (Nota_di_credito_attivaBulk) listaNota.get(0);
            } else {
                throw new ComponentException("Sono state trovate più note di credito con lo stesso codice invio SDI: " + codiceInvioSdi);
            }
        } else if (lista.size() == 1) {
            return (Fattura_attiva_IBulk) lista.get(0);
        } else {
            throw new ComponentException("Sono state trovate più fatture con lo stesso codice invio SDI: " + codiceInvioSdi);
        }
    }

    private void sendMailForNotificationOk(UserContext userContext, Fattura_attivaBulk fattura) {
        /**
         * Invio mail di notifica Ricezione
         */
        try {
            String subject = "";
            String text = "";
            String estremoFattura = fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva();
            subject = "[SIGLA] Notifica esito positivo invio fattura attiva " + estremoFattura;
            subject += " UO: " + fattura.getCd_unita_organizzativa();
            text = "La fattura attiva elettronica: <b>" + estremoFattura + "</b>" +
                    " è stata accettata dal cliente.";
            Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome) getHome(userContext, Utente_indirizzi_mailBulk.class);
            Collection utenti = utente_indirizzi_mailHome.findUtenteNotificaOkInvioFatturaElettronicaAttiva(fattura.getCd_uo_origine());
            sendMailForNotificationFatturaElettronica(subject, text, utenti);
        } catch (Exception e) {
            logger.info("Errore durante l'invio della mail di notifica ok. Errore: " + e.getMessage() == null ? (e.getCause() == null ? "Errore Generico" : e.getCause().toString()) : e.getMessage());
        }
    }

    private void sendMailForNotificationKo(UserContext userContext, Fattura_attivaBulk fattura) {
        try {
            String subject = "";
            String text = "";
            String estremoFattura = fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva();
            subject = "[SIGLA] Notifica errore invio fattura attiva " + estremoFattura;
            subject += " UO: " + fattura.getCd_unita_organizzativa();
            text = "Errore durante l'invio della fattura attiva elettronica: <b>" + estremoFattura + "</b>" +
                    ". Motivo: " + fattura.getNoteInvioSdi();
            Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome) getHome(userContext, Utente_indirizzi_mailBulk.class);
            Collection utenti = utente_indirizzi_mailHome.findUtenteNotificaOkInvioFatturaElettronicaAttiva(fattura.getCd_uo_origine());
            sendMailForNotificationFatturaElettronica(subject, text, utenti);
        } catch (Exception e) {
            logger.info("Errore durante l'invio della mail di notifica ko. Errore: " + e.getMessage() == null ? (e.getCause() == null ? "Errore Generico" : e.getCause().toString()) : e.getMessage());
        }
    }

    private void sendMailForNotificationFatturaElettronica(String subject,
                                                           String text, Collection utenti) throws AddressException {
        String addressTO = null;
        for (java.util.Iterator<Utente_indirizzi_mailBulk> i = utenti.iterator(); i.hasNext(); ) {
            Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk) i.next();
            if (addressTO == null)
                addressTO = new String();
            else
                addressTO = addressTO + ",";
            addressTO = addressTO + utente_indirizzi.getIndirizzo_mail();
        }
        if (addressTO != null) {
            SendMail.sendMail(subject, text, InternetAddress.parse(addressTO));
        }
    }

    public Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_RIFIUTATA_DESTINATARIO);
        fattura.setNoteInvioSdi(impostaNoteSdi(noteSdi));
        fattura.setToBeUpdated();
        updateBulk(userContext, fattura);
        if (fattura instanceof Fattura_attiva_IBulk) {
            Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fattura;
            Nota_di_credito_attivaBulk nota = generaNotaCreditoAutomatica(userContext, fatturaAttiva, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext), false);
            fatturaAttiva.setNotaCreditoAutomaticaGenerata(nota);
            sendMailForNotificationKo(userContext, fatturaAttiva);
        }
        return fattura;
    }

    public Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_DECORRENZA_TERMINI);
        fattura.setNoteInvioSdi(impostaNoteSdi(noteSdi));
        fattura.setToBeUpdated();
        updateBulk(userContext, fattura);
        return fattura;
    }

    public Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_ACCETTATA_DESTINATARIO);
        fattura.setToBeUpdated();
        updateBulk(userContext, fattura);
        return fattura;
    }

    public Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
    	if (!noteSdi.contains("Fattura duplicata")){
            fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_SCARTATA_DA_SDI);
            fattura.setCodiceInvioSdi(codiceInvioSdi);
            fattura.setNoteInvioSdi(impostaNoteSdi(noteSdi));
            fattura.setToBeUpdated();
            updateBulk(userContext, fattura);
            if (fattura instanceof Fattura_attiva_IBulk) {
                Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fattura;
                Nota_di_credito_attivaBulk nota = generaNotaCreditoAutomatica(userContext, fatturaAttiva, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext), false);
                fatturaAttiva.setNotaCreditoAutomaticaGenerata(nota);
                sendMailForNotificationKo(userContext, fatturaAttiva);
            }
    	}
        return fattura;
    }


    @Deprecated
    public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, String codiceInvioSdi, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        Fattura_attiva_IBulk fatturaAttiva = ricercaFatturaSDI(userContext, codiceInvioSdi);
        return aggiornaDatiFatturaSDI(userContext, fatturaAttiva, statoInvioSdi, noteInvioSdi, dataConsegnaSdi, stornaFattura);
    }

    @Deprecated
    public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, Fattura_attiva_IBulk fatturaAttiva, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fatturaAttiva.setStatoInvioSdi(statoInvioSdi);
        fatturaAttiva.setNoteInvioSdi(impostaNoteSdi(noteInvioSdi));
        fatturaAttiva.setDtConsegnaSdi(dataConsegnaSdi != null ? new Timestamp(dataConsegnaSdi.toGregorianCalendar().getTime().getTime()) : null);
        fatturaAttiva.setToBeUpdated();
        updateBulk(userContext, fatturaAttiva);

        if (stornaFattura)
            generaNotaCreditoAutomatica(userContext, fatturaAttiva, fatturaAttiva.getEsercizio());
        return fatturaAttiva;
    }

    public Boolean isAttivoSplitPayment(UserContext aUC, Date dataFattura) throws ComponentException {
        Date dataInizio;
        try {
            dataInizio = Utility.createConfigurazioneCnrComponentSession().getDt01(aUC, new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_ATTIVA);
        } catch (ComponentException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        } catch (RemoteException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        } catch (EJBException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
        }
        if (dataFattura == null || dataInizio == null || dataFattura.before(dataInizio)) {
            return false;
        }
        return true;
    }

    public Fattura_attivaBulk aggiornaFatturaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        if (fatturaAttiva.isNotaCreditoDaNonInviareASdi()) {
            fatturaAttiva.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_FIRMATA_NC);
        } else {
            fatturaAttiva.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI);
        }
        fatturaAttiva.setToBeUpdated();
        updateBulk(userContext, fatturaAttiva);
        return fatturaAttiva;
    }

    public Fattura_attivaBulk aggiornaFatturaPredispostaAllaFirma(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException, java.rmi.RemoteException {
           
    	fatturaAttiva.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_PREDISPOSTA_FIRMA);
        fatturaAttiva.setToBeUpdated();
        updateBulk(userContext, fatturaAttiva);
        return fatturaAttiva;
    }

    public Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, Date dataConsegnaSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fatturaAttiva.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_CONSEGNATA_SDI);
        fatturaAttiva.setDtRicezioneSdi(dataConsegnaSdi!=null?new Timestamp(dataConsegnaSdi.getTime()):null);
        fatturaAttiva.setToBeUpdated();
        updateBulk(userContext, fatturaAttiva);
        return fatturaAttiva;
    }

    public Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_MANCATA_CONSEGNA);
        fattura.setCodiceInvioSdi(codiceSdi);
        fattura.setNoteInvioSdi(impostaNoteSdi(noteInvioSdi));
        fattura.setToBeUpdated();
        updateBulk(userContext, fattura);
        if (fattura.getCodiceUnivocoUfficioIpa() != null){
            gestioneEmailUtenteAvvisoFatturaElettronica(userContext, fattura);
        }

    	return fattura;
    }

	public void gestioneEmailUtenteAvvisoFatturaElettronica(UserContext userContext, Fattura_attivaBulk fattura)
			throws ComponentException {
		String msg = "Si comunica che occorre inviare al cliente la fattura "+fattura.getCd_uo_origine()+"-"+fattura.getEsercizio()+"-"+fattura.getPg_fattura_attiva()+".";
    	String subject = "IMPORTANTE: Necessario inviare al cliente la fattura";

    	if (fattura.isFatturaEstera() || (fattura.getCodiceDestinatarioFatt() == null && 
    			fattura.getPecFatturaElettronica() == null)){
    		String eMail = recuperoEmailUtente(userContext, fattura);
    		if (eMail != null){
    			List<String> list = new ArrayList<>();
    			list.add(eMail);
    			if (fattura.isFatturaEstera()){
    				SendMail.sendMail(subject, msg, list);
        		} else {
        			if (fattura.getPartita_iva() == null){
            			String msgPersonaFisica = msg+" Bisogna inoltre avvisarlo che può visualizzare la fattura elettronica nella sua area riservata del sito WEB dell'agenzia delle entrate.";
        				SendMail.sendMail(subject, msgPersonaFisica, list);
        			} else {
        				msg = "Comunicare al cliente che la fattura elettronica "+fattura.getCd_uo_origine()+"-"+fattura.getEsercizio()+"-"+fattura.getPg_fattura_attiva()+" si trova nella sua area riservata del sito WEB dell'agenzia delle entrate.";
        				subject = "Necessario comunicare al cliente trasmissione fattura elettronica";
        				SendMail.sendMail(subject, msg, list);
        			}
    			}
    		}
    	}
	}

    public Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, XMLGregorianCalendar dataConsegnaSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fatturaAttiva.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_CONSEGNATA_DESTINATARIO);
        fatturaAttiva.setCodiceInvioSdi(codiceSdi);
        fatturaAttiva.setNoteInvioSdi(null);
        fatturaAttiva.setDtConsegnaSdi(dataConsegnaSdi != null ? new Timestamp(dataConsegnaSdi.toGregorianCalendar().getTimeInMillis()) : null);
        fatturaAttiva.setToBeUpdated();
        updateBulk(userContext, fatturaAttiva);
        sendMailForNotificationOk(userContext, fatturaAttiva);

        return fatturaAttiva;
    }

    public java.util.List recuperoFattureElettronicheSenzaNotificaConsegna(UserContext userContext, Unita_organizzativaBulk unita_organizzativaBulk) throws PersistencyException, ComponentException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {
        return recuperoFattureElettroniche(userContext, unita_organizzativaBulk, Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI);
    }

    private java.util.List recuperoFattureElettroniche(UserContext userContext, Unita_organizzativaBulk unita_organizzativaBulk, String statoInvioSdi) throws PersistencyException, ComponentException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {
        Fattura_attivaHome home = (Fattura_attivaHome) getHome(userContext, Fattura_attivaBulk.class);

        SQLBuilder sql = home.createSQLBuilder();

        sql.addSQLClause("AND", "FL_FATTURA_ELETTRONICA", sql.EQUALS, "Y");
        sql.addSQLClause("AND", "CD_UO_ORIGINE", sql.EQUALS, unita_organizzativaBulk.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "STATO_INVIO_SDI", sql.EQUALS, statoInvioSdi);

        return home.fetchAll(sql);
    }

    public Fattura_attivaBulk recuperoFatturaElettronicaDaNomeFile(UserContext userContext, String nomeFileInvioSdi) throws PersistencyException, ComponentException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {

        Fattura_attiva_IBulk fatturaAttiva = new Fattura_attiva_IBulk();
        fatturaAttiva.setNomeFileInvioSdi(nomeFileInvioSdi);
        List lista = (getHome(userContext, Fattura_attiva_IBulk.class).find(fatturaAttiva));

        if (lista == null || lista.isEmpty()) {
            Nota_di_credito_attivaBulk nota = new Nota_di_credito_attivaBulk();
            nota.setNomeFileInvioSdi(nomeFileInvioSdi);
            List listaNota = (getHome(userContext, Nota_di_credito_attivaBulk.class).find(nota));

            if (listaNota == null || listaNota.isEmpty()) {
                Nota_di_debito_attivaBulk notaDebito = new Nota_di_debito_attivaBulk();
                notaDebito.setNomeFileInvioSdi(nomeFileInvioSdi);
                List listaNotaDebito = (getHome(userContext, Nota_di_debito_attivaBulk.class).find(notaDebito));

                if (listaNotaDebito == null || listaNotaDebito.isEmpty()) {
                    return null;
                } else if (listaNotaDebito.size() == 1) {
                    return (Nota_di_debito_attivaBulk) listaNota.get(0);
                } else {
                    throw new ComponentException("Esistono più note di debito aventi lo stesso nome file di invio a SDI! " + nomeFileInvioSdi);
                }
            } else if (listaNota.size() == 1) {
                return (Nota_di_credito_attivaBulk) listaNota.get(0);
            } else {
                throw new ComponentException("Esistono più note di credito aventi lo stesso nome file di invio a SDI! " + nomeFileInvioSdi);
            }
        } else if (lista.size() == 1) {
            return (Fattura_attiva_IBulk) lista.get(0);
        } else {
            throw new ComponentException("Esistono più fatture aventi lo stesso nome file di invio a SDI! " + nomeFileInvioSdi);
        }
    }

    public Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        fattura.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_NON_RECAPITABILE);
        fattura.setCodiceInvioSdi(codiceInvioSdi);
        fattura.setNoteInvioSdi(impostaNoteSdi(noteSdi));
        fattura.setToBeUpdated();
        updateBulk(userContext, fattura);
        if (fattura instanceof Fattura_attiva_IBulk) {
            Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fattura;
            sendMailForNotificationKo(userContext, fatturaAttiva);
        }
        return fattura;
    }

    private String impostaNoteSdi(String noteSdi) {
        return noteSdi == null || noteSdi.length() <= 500 ? noteSdi : noteSdi.substring(0, 499);
    }

    private void verificaEsistenzaDichiarazioneIntento(UserContext userContext,
                                                       Fattura_attiva_rigaBulk fatturaRiga) throws ComponentException, ApplicationException {
        try {
            List dichiarazioni = null;
            Dichiarazione_intentoHome home = (Dichiarazione_intentoHome) getHome(userContext, Dichiarazione_intentoBulk.class);
            SQLBuilder sql = (SQLBuilder) home.createSQLBuilder();
            sql.addSQLClause("AND", "ANNO_RIF", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "cd_anag", sql.EQUALS, fatturaRiga.getFattura_attiva().getCliente().getCd_anag());
            sql.addSQLClause("AND", "dt_ini_validita", sql.LESS_EQUALS, fatturaRiga.getFattura_attiva().getDt_registrazione());
            sql.addSQLClause("AND", "dt_fin_validita", sql.GREATER_EQUALS, fatturaRiga.getFattura_attiva().getDt_registrazione());
            //sql.addSQLClause("AND","fl_acquisti", sql.EQUALS,"Y");
            sql.openParenthesis("AND");
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "dt_inizio_val_dich", sql.LESS_EQUALS, fatturaRiga.getFattura_attiva().getDt_registrazione());
            sql.addSQLClause("AND", "dt_fine_val_dich", sql.GREATER_EQUALS, fatturaRiga.getFattura_attiva().getDt_registrazione());
            sql.closeParenthesis();
            sql.openParenthesis("OR");
            sql.addSQLClause("AND", "dt_inizio_val_dich", sql.ISNULL, null);
            sql.addSQLClause("AND", "dt_fine_val_dich", sql.ISNULL, null);
            sql.closeParenthesis();
            sql.closeParenthesis();
            dichiarazioni = home.fetchAll(sql);
            if (dichiarazioni.size() == 0)
                throw new ApplicationException("Dichiarazione intento non trovata o non valida!");
            for (Iterator i = dichiarazioni.iterator(); i.hasNext(); ) {
                Dichiarazione_intentoBulk bulk = (Dichiarazione_intentoBulk) i.next();
                if (bulk.getIm_limite_op() != null) {
                    BigDecimal tot = BigDecimal.ZERO;
                    Fattura_attiva_rigaHome homeFat = (Fattura_attiva_rigaHome) getHome(userContext, Fattura_attiva_rigaBulk.class);
                    SQLBuilder sql_fatt = (SQLBuilder) homeFat.createSQLBuilder();
                    sql_fatt.addTableToHeader("TERZO");
                    sql_fatt.addTableToHeader("ANAGRAFICO");
                    sql_fatt.addTableToHeader("FATTURA_ATTIVA");
                    sql_fatt.addTableToHeader("VOCE_IVA");
                    sql_fatt.resetColumns();
                    sql_fatt.addColumn("NVL(SUM(DECODE(FATTURA_ATTIVA.TI_FATTURA,'C',-IM_IMPONIBILE,IM_IMPONIBILE)),0)", "IM_IMPONIBILE");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA.CD_TERZO", "TERZO.CD_TERZO");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_VOCE_IVA", "VOCE_IVA.CD_VOCE_IVA");
                    sql_fatt.addSQLJoin("ANAGRAFICO.CD_ANAG", "TERZO.CD_ANAG");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, "FATTURA_ATTIVA_RIGA.ESERCIZIO");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA.CD_CDS", sql.EQUALS, "FATTURA_ATTIVA_RIGA.CD_CDS");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, "FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA");
                    sql_fatt.addSQLJoin("FATTURA_ATTIVA.PG_FATTURA_ATTIVA", sql.EQUALS, "FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA");
                    if (fatturaRiga.getPg_fattura_attiva() != null) {
                        sql_fatt.addSQLClause("AND", "FATTURA_ATTIVA.PG_FATTURA_ATTIVA", sql.NOT_EQUALS, fatturaRiga.getPg_fattura_attiva());
                    }


                    sql_fatt.addSQLClause("AND", "FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
                    sql_fatt.addSQLClause("AND", "ANAGRAFICO.CD_ANAG", sql.EQUALS, fatturaRiga.getFattura_attiva().getCliente().getCd_anag());
                    sql_fatt.addSQLClause("AND", "VOCE_IVA.FL_OBB_DICHIARAZIONE_INTENTO", sql.EQUALS, "Y");

                    java.sql.ResultSet rs = null;
                    LoggableStatement ps = null;
                    try {
                        ps = sql_fatt.prepareStatement(getConnection(userContext));
                        try {
                            rs = ps.executeQuery();
                            if (rs.next() && rs.getBigDecimal(1) != null)
                                tot = tot.add(rs.getBigDecimal(1));
                        } catch (java.sql.SQLException e) {
                            throw handleSQLException(e);
                        } finally {
                            if (rs != null) try {
                                rs.close();
                            } catch (java.sql.SQLException e) {
                            }
                            ;
                        }
                    } catch (SQLException e) {
                        throw handleException(e);
                    } finally {
                        if (ps != null) try {
                            ps.close();
                        } catch (java.sql.SQLException e) {
                        }
                        ;
                    }

                    for (Iterator fatCorrente = fatturaRiga.getFattura_attiva().getFattura_attiva_dettColl().iterator(); fatCorrente.hasNext(); ) {
                        if (fatturaRiga.getFattura_attiva().getTi_fattura().compareTo("C") == 0) {
                            Nota_di_credito_attiva_rigaBulk fatbulk = (Nota_di_credito_attiva_rigaBulk) fatCorrente.next();
                            if (fatbulk.getVoce_iva().getFl_obb_dichiarazione_intento())
                                tot = tot.subtract(fatbulk.getIm_imponibile());
                        } else {
                            Fattura_attiva_rigaIBulk fatbulk = (Fattura_attiva_rigaIBulk) fatCorrente.next();
                            if (fatbulk.getVoce_iva().getFl_obb_dichiarazione_intento())
                                tot = tot.add(fatbulk.getIm_imponibile());
                        }
                    }
                    if (tot.compareTo(bulk.getIm_limite_op()) > 0)
                        throw new ApplicationException("L'importo dei dettagli supera il limite indicato nella dichiarazione di intento come totale operazione!");
                } else if (bulk.getIm_limite_sing_op() != null) {
                    if (fatturaRiga.getIm_imponibile().compareTo(bulk.getIm_limite_sing_op()) > 0)
                        throw new ApplicationException("L'importo del dettaglio supera il limite indicato nella dichiarazione di intento per singola operazione!");
                }
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        } catch (ComponentException e1) {
            throw handleException(e1);
        }
    }


	public String recuperoEmailUtente(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ComponentException {
		UtenteBulk utente = new UtenteBulk(fatturaAttiva.getUtcr());
		try {
		    utente = (UtenteBulk) getHome(aUC, UtenteBulk.class).findByPrimaryKey(utente);
		    if (utente != null){
		    	if (utente.getCd_utente_uid() != null){
		    		return utente.getCd_utente_uid()+"@cnr.it";
		    	}

				String msg = "Si comunica che occorre inviare al cliente la fattura "+fatturaAttiva.getCd_uo_origine()+"-"+fatturaAttiva.getEsercizio()+"-"+fatturaAttiva.getPg_fattura_attiva()+".";
		    	String subject = "Email utente non trovata IMPORTANTE: Necessario inviare al cliente la fattura";
		        SendMail.sendErrorMail(subject, msg);
		    }
		} catch (PersistencyException e) {
		    throw new ComponentException(e);
		}
		return null;
	}
	
	public void gestioneAvvisoInvioMailFattureAttive(UserContext aUC) throws ComponentException {
        Fattura_attiva_IHome home = (Fattura_attiva_IHome) getHome(aUC, Fattura_attiva_IBulk.class);

        SQLBuilder sql = home.createSQLBuilder();
        
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DATE, -2);
    	Timestamp dateOfSearch = new Timestamp(cal.getTimeInMillis());


        sql.addSQLClause("AND", "FL_FATTURA_ELETTRONICA", sql.EQUALS, "Y");
        sql.addSQLClause("AND", "STATO_INVIO_SDI", sql.EQUALS, Fattura_attivaBulk.FATT_ELETT_CONSEGNATA_SDI);
        sql.addSQLClause("AND", "DT_RICEZIONE_SDI", sql.LESS_EQUALS, dateOfSearch);
        sql.addSQLClause("AND", "CODICE_UNIVOCO_UFFICIO_IPA", sql.ISNULL,null);
        sql.addSQLClause("AND", "PEC_FATTURA_ELETTRONICA", sql.ISNULL,null);
        sql.addSQLClause("AND", "CODICE_DESTINATARIO_FATT", sql.ISNULL,null);
        
        List fatture;
		try {
			fatture = home.fetchAll(sql);
	        for (Iterator i = fatture.iterator(); i.hasNext(); ) {
	            Fattura_attiva_IBulk fatturaProtocollata = (Fattura_attiva_IBulk) i.next();
	            invioMail(aUC, fatturaProtocollata);
	        }
        } catch (PersistencyException e) {
            throw handleException(e);
		}
        
        Nota_di_credito_attivaHome homeNc = (Nota_di_credito_attivaHome) getHome(aUC, Nota_di_credito_attivaBulk.class);

        sql = homeNc.createSQLBuilder();
        
        sql.addSQLClause("AND", "FL_FATTURA_ELETTRONICA", sql.EQUALS, "Y");
        sql.addSQLClause("AND", "STATO_INVIO_SDI", sql.EQUALS, Fattura_attivaBulk.FATT_ELETT_CONSEGNATA_SDI);
        sql.addSQLClause("AND", "DT_RICEZIONE_SDI", sql.LESS_EQUALS, dateOfSearch);
        sql.addSQLClause("AND", "CODICE_UNIVOCO_UFFICIO_IPA", sql.ISNULL,null);
        sql.addSQLClause("AND", "PEC_FATTURA_ELETTRONICA", sql.ISNULL,null);
        sql.addSQLClause("AND", "CODICE_DESTINATARIO_FATT", sql.ISNULL,null);
        
        List nc;
		try {
			nc = home.fetchAll(sql);
	        for (Iterator i = nc.iterator(); i.hasNext(); ) {
	            Nota_di_credito_attivaBulk fatturaProtocollata = (Nota_di_credito_attivaBulk) i.next();
	            invioMail(aUC, fatturaProtocollata);
	        }
        } catch (PersistencyException e) {
            throw handleException(e);
		}
        
	}

	public void invioMail(UserContext aUC, Fattura_attivaBulk fatturaProtocollata)
			throws ComponentException, PersistencyException {
		String msg = "Si comunica che occorre inviare al cliente la fattura "+fatturaProtocollata.getCd_uo_origine()+"-"+fatturaProtocollata.getEsercizio()+"-"+fatturaProtocollata.getPg_fattura_attiva()+".";
		String subject = "IMPORTANTE: Necessario inviare al cliente la fattura";

		if (fatturaProtocollata.isFatturaEstera() || (fatturaProtocollata.getCodiceDestinatarioFatt() == null && 
				fatturaProtocollata.getPecFatturaElettronica() == null)){
			String eMail = recuperoEmailUtente(aUC, fatturaProtocollata);
			if (eMail != null){
				List<String> list = new ArrayList<>();
				list.add(eMail);
				if (fatturaProtocollata.isFatturaEstera()){
					SendMail.sendMail(subject, msg, list);
				} else {
					if (fatturaProtocollata.getPartita_iva() == null){
		    			String msgPersonaFisica = msg+" Bisogna inoltre avvisarlo che può visualizzare la fattura elettronica nella sua area riservata del sito WEB dell'agenzia delle entrate.";
						SendMail.sendMail(subject, msgPersonaFisica, list);
					} else {
						msg = "Comunicare al cliente che la fattura elettronica "+fatturaProtocollata.getCd_uo_origine()+"-"+fatturaProtocollata.getEsercizio()+"-"+fatturaProtocollata.getPg_fattura_attiva()+" si trova nella sua area riservata del sito WEB dell'agenzia delle entrate.";
						subject = "Necessario comunicare al cliente trasmissione fattura elettronica";
						SendMail.sendMail(subject, msg, list);
					}
				}
			} else {
				SendMail.sendErrorMail(subject, msg);
			}
		}
		
		fatturaProtocollata.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_AVVISO_NOTIFICA_INVIO_MAIL);
		updateBulk(aUC, fatturaProtocollata);
	}
}
