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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviBulk;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaAttivaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.sdi.IErroreType;
import it.cnr.contab.sdi.IListaErrori;
import it.cnr.contab.sdi.IMancataConsegna;
import it.cnr.contab.sdi.INonRecapitabile;
import it.cnr.contab.sdi.IRicevutaConsegna;
import it.cnr.contab.sdi.IScarto;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.gov.fatturapa.FileSdIType;
import it.gov.fatturapa.sdi.messaggi.v1.EsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoType;

@Stateless
@WebService(endpointInterface = "it.gov.fatturapa.TrasmissioneFatture",
        name = "TrasmissioneFatture", targetNamespace = "http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class TrasmissioneFatture implements it.cnr.contab.docamm00.ejb.TrasmissioneFatturePA, it.gov.fatturapa.TrasmissioneFatture {
    private static final Logger logger = LoggerFactory.getLogger(TrasmissioneFatture.class);

    public void ricevutaConsegna(FileSdIType ricevuta) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaRicevutaConsegna(userContext, ricevuta.getNomeFile(), ricevuta.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Ricevuta Consegna"));
        }
    }

    public void notificaMancataConsegna(FileSdIType mancataConsegna) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaMancataConsegna(userContext, mancataConsegna.getNomeFile(), mancataConsegna.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Mancata Consegna"));
        }
    }

    public void notificaScarto(FileSdIType scarto) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaScarto(userContext, scarto.getNomeFile(), scarto.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Notifica Scarto"));
        }
    }

    public void notificaEsito(FileSdIType esito) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaEsito(userContext, esito.getNomeFile(), esito.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Notifica Esito"));
        }
    }

    public void notificaDecorrenzaTermini(FileSdIType decorrenzaTermini) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaDecorrenzaTermini(userContext, decorrenzaTermini.getNomeFile(), decorrenzaTermini.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Decorrenza Termini"));
        }
    }

    public void attestazioneTrasmissioneFattura(
            FileSdIType attestazioneTrasmissioneFattura) {
        UserContext userContext = createUserContext();
        try {
            notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(userContext, attestazioneTrasmissioneFattura.getNomeFile(), attestazioneTrasmissioneFattura.getFile());
        } catch (Exception e) {
            throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura per Attestazione Trasmissione Fattura"));
        }
    }

    public void notificaFatturaAttivaRicevutaConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            JAXBElement<IRicevutaConsegna> file = (JAXBElement<IRicevutaConsegna>) getJAXBElement(data);
            IRicevutaConsegna ricevuta = file.getValue();
            logger.info("Fatture Elettroniche: Attive: Ricevuta Consegna. MessageId:" + ricevuta.getMessageId());

            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, ricevuta.getIdentificativoSdI());
            if (fattura != null && (fattura.getStatoInvioSdi() == null || !fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_MANCATA_CONSEGNA))) {
                logger.info("Fatture Elettroniche: Attive: Fattura già elaborata " + ricevuta.getIdentificativoSdI());
            } else {
                if (fattura == null) {
                    String nomeFileP7m = ricevuta.getNomeFile();
                    fattura = recuperoFatturaDaNomeFile(userContext, nomeFileP7m);
                }
                if (fattura != null) {
                    salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_RICEVUTA_CONSEGNA);
                    try {
                        fattura = component.aggiornaFatturaRicevutaConsegnaInvioSDI(userContext, fattura, String.valueOf(ricevuta.getIdentificativoSdI()), ricevuta.getDataOraConsegna());
                        logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura consegnata  " + fattura.recuperoIdFatturaAsString());
                    } catch (Exception ex) {
                        logger.error("Fatture Elettroniche: Attive: MessageId:" + ricevuta.getMessageId() + ". Errore nell'elaborazione della Fattura consegnata " + fattura.recuperoIdFatturaAsString() + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                        java.io.StringWriter sw = new java.io.StringWriter();
                        ex.printStackTrace(new java.io.PrintWriter(sw));
                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: Ricevuta consegna.  " + fattura.recuperoIdFatturaAsString(), sw.toString());
                    }
                } else {
                    logger.warn("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura." + ricevuta.getNomeFile());
                    SendMail.sendErrorMail("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura", "Ricevuta Consegna. " + ricevuta.getNomeFile());
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private void salvaFileSuDocumentale(DataHandler data, String nomeFile,
                                        Fattura_attivaBulk fattura, StorageDocAmmAspect aspect) throws IOException,
            ApplicationException {
        logger.info("Inizio Salvataggio sul Documentale");
        DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
        StorageFile storageFile = new StorageFileFatturaAttiva(data.getInputStream(), data.getContentType(), nomeFile, fattura);
        if (storageFile != null) {
            String path = storageFile.getStorageParentPath();
            try {
                StorageObject storageObject = documentiCollegatiDocAmmService.restoreSimpleDocument(
                        storageFile,
                        storageFile.getInputStream(),
                        storageFile.getContentType(),
                        storageFile.getFileName(),
                        path,
                        true);
                documentiCollegatiDocAmmService.addAspect(storageObject, aspect.value());
                storageFile.setStorageObject(storageObject);
                logger.info("Salvato file sul Documentale");
            } catch (StorageException e) {
                if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                    throw new ApplicationException("CMIS - File Ricevuta Consegna [" + storageFile.getFileName() + "] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
                throw new ApplicationException("CMIS - Errore nella registrazione del file Ricevuta Consegna  sul Documentale (" + e.getMessage() + ")");
            }
        }
    }

    public void notificaFatturaAttivaMancataConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            JAXBElement<IMancataConsegna> file = (JAXBElement<IMancataConsegna>) getJAXBElement(data);
            IMancataConsegna mancataConsegna = file.getValue();
            logger.info("Fatture Elettroniche: Attive: Mancata Consegna. MessageId:" + mancataConsegna.getMessageId());
            String codiceSDI = String.valueOf(mancataConsegna.getIdentificativoSdI());
            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, codiceSDI);
            if (fattura != null) {
                logger.info("Fatture Elettroniche: Attive: Fattura già elaborata " + codiceSDI);
            } else {
                String nomeFileP7m = mancataConsegna.getNomeFile();
                fattura = recuperoFatturaDaNomeFile(userContext, nomeFileP7m);
                if (fattura != null) {
                    salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_MANCATA_CONSEGNA);
                    try {
                        fattura = component.aggiornaFatturaMancataConsegnaInvioSDI(userContext, fattura, codiceSDI, mancataConsegna.getDescrizione());
                        logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura con mancata consegna con nome file " + nomeFileP7m);
                    } catch (Exception ex) {
                        logger.error("Fatture Elettroniche: Attive: MessageId:" + mancataConsegna.getMessageId() + ". Errore nell'elaborazione della mancata consegna della fattura con nome file " + nomeFileP7m + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                        java.io.StringWriter sw = new java.io.StringWriter();
                        ex.printStackTrace(new java.io.PrintWriter(sw));
                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: Mancata consegna. Nome file " + nomeFileP7m, sw.toString());
                    }
                } else {
                    logger.warn("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura." + nomeFileP7m);
                    SendMail.sendErrorMail("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura", "Mancata Consegna. Nome File " + nomeFileP7m);
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    public void notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        RicercaDocContComponentSession docComponent = recuperoComponentRicercaDocCont();
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            JAXBElement<INonRecapitabile> file = (JAXBElement<INonRecapitabile>) getJAXBElement(data);
            INonRecapitabile notifica = file.getValue();
            logger.info("Fatture Elettroniche: Attive: Trasmissione non recapitata. MessageId:" + notifica.getMessageId());
            String codiceSDI = String.valueOf(notifica.getIdentificativoSdI());
            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, codiceSDI);
            if (fattura != null && fattura.getStatoInvioSdi() != null && fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_NON_RECAPITABILE)) {
                logger.info("Fatture Elettroniche: Attive: Fattura già elaborata " + codiceSDI);
            } else {
                String nomeFileP7m = notifica.getNomeFile();
                fattura = recuperoFatturaDaNomeFile(userContext, nomeFileP7m);
                if (fattura != null) {
                    if ((CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) == 0) || (docComponent.isRibaltato(userContext, fattura.getCd_cds_origine(), fattura.getEsercizio()) && CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) > 0)) {
                        salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA);
                        try {
                            component.aggiornaFatturaTrasmissioneNonRecapitataSDI(userContext, fattura, codiceSDI, notifica.getNote());
                            logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura con trasmissione non recapitata con nome file " + nomeFileP7m);
                            if (fattura instanceof Fattura_attiva_IBulk) {
                                Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fattura;
                                if (fatturaAttiva.getNotaCreditoAutomaticaGenerata() != null) {
                                    component.gestioneInvioMailNonRecapitabilita(userContext, fattura);
                                    try {
                                        SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaAttiva.getNotaCreditoAutomaticaGenerata());
                                    } catch (Exception ex) {
                                        logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione della stampa della mancata consegna della fattura con nome file " + nomeFileP7m + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                                        java.io.StringWriter sw = new java.io.StringWriter();
                                        ex.printStackTrace(new java.io.PrintWriter(sw));
                                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: Mancata Consegna. Nome file " + nomeFileP7m, sw.toString());
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione della mancata consegna della fattura con nome file " + nomeFileP7m + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                            java.io.StringWriter sw = new java.io.StringWriter();
                            ex.printStackTrace(new java.io.PrintWriter(sw));
                            SendMail.sendErrorMail("Fatture Elettroniche: Attive: Trasmissione non recapitata. Nome file " + nomeFileP7m, sw.toString());
                        }
                    }
                } else {
                    logger.warn("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura. Trasmissione non recapitata. Nome File " + nomeFileP7m);
                    SendMail.sendErrorMail("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura", "Trasmissione non recapitata. Nome File " + nomeFileP7m);
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }


    public void notificaFatturaAttivaConsegnaPec(UserContext userContext, String nomeFile, Date dataConsegna) throws ComponentException {
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            logger.info("Fatture Elettroniche: Attive: Pec: Notifica Consegna Nome File: " + nomeFile);
            Fattura_attivaBulk fattura = recuperoFatturaDaNomeFile(userContext, nomeFile);
            if (fattura != null) {
                if (fattura.getStatoInvioSdi() != null &&
                        (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_CONSEGNATA_SDI))) {
                    logger.info("Fatture Elettroniche: Attive: PEC. Fattura già elaborata. " + nomeFile);
                } else if (fattura.getStatoInvioSdi() != null &&
                        (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI))) {
                    try {
                        component.aggiornaFatturaConsegnaSDI(userContext, fattura, dataConsegna);
                        logger.info("Fatture Elettroniche: Attive: PEC. Aggiornamento Fattura consegnata a SDI " + nomeFile);
                    } catch (Exception ex) {
                        logger.error("Fatture Elettroniche: Attive: PEC. Errore nell'aggiornamento della consegna a SDI. Nome File: " + nomeFile + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                        java.io.StringWriter sw = new java.io.StringWriter();
                        ex.printStackTrace(new java.io.PrintWriter(sw));
                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Nome file " + nomeFile, sw.toString());
                    }
                } else {
                    logger.info("Fatture Elettroniche: Attive: PEC. Fattura con uno stato di lavorazione avanzato. " + nomeFile + ". Stato: " + fattura.getStatoInvioSdi());
                }
            } else {
                logger.warn("Fatture Elettroniche: Attive: PEC. Per il nome del file indicato nell'e-mail di Consegna della PEC non corrisponde nessuna fattura." + nomeFile);
                SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Per il nome del file indicato nell'e-mail di Consegna della PEC non corrisponde nessuna fattura", "Consegna PEC. Nome File: " + nomeFile);
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    public InputStream mancataConsegnaPecInvioFatturaAttiva(UserContext userContext, String nomeFile) throws ComponentException {
        try {
            logger.info("Fatture Elettroniche: Attive: Pec: Mancata Consegna Nome File: " + nomeFile);
            Fattura_attivaBulk fattura = recuperoFatturaDaNomeFile(userContext, nomeFile);
            if (fattura != null) {
                logger.info("Fatture Elettroniche: Attive: Pec: Mancata Consegna Fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva());
                if (fattura.getStatoInvioSdi() != null && fattura.getStatoInvioSdi().equals("FATT_ELETT_INVIATA_SDI")) {
                    try {
                        DocumentiCollegatiDocAmmService docCollService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
                        InputStream streamSigned = docCollService.getStreamXmlFirmatoFatturaAttiva(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
                        if (streamSigned != null) {
                            SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Mancata Consegna Fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva(), "Verrà riprovato l'invio.");
                            return streamSigned;
                        }
                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Stream file firmato non trovato.", "Mancata Consegna Fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva());
                    } catch (Exception ex) {
                        logger.error("Fatture Elettroniche: Attive: PEC. Mancata Consegna. Errore nella ricerca dello stream del file firmato della fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva() + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                        java.io.StringWriter sw = new java.io.StringWriter();
                        ex.printStackTrace(new java.io.PrintWriter(sw));
                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Mancata Consegna. Errore nella ricerca dello stream del file firmato della fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva(), sw.toString());
                    }
                } else {
                    logger.info("Fatture Elettroniche: Attive: Pec: Mancata Consegna Fattura: " + fattura.getCd_uo() + "-" + fattura.getEsercizio() + "-" + fattura.getPg_fattura_attiva() + ". Mancata Consegna Elaborata");
                }
            } else {
                logger.warn("Fatture Elettroniche: Attive: PEC. Mancata Consegna. Per il nome del file indicato nell'e-mail di Consegna della PEC non corrisponde nessuna fattura." + nomeFile);
                SendMail.sendErrorMail("Fatture Elettroniche: Attive: PEC. Fatture Elettroniche: Attive: PEC. Mancata Consegna. Per il nome del file indicato nell'e-mail di Consegna della PEC non corrisponde nessuna fattura", "Consegna PEC. Nome File: " + nomeFile);
            }
            return null;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    public void notificaFatturaAttivaScarto(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        RicercaDocContComponentSession docComponent = recuperoComponentRicercaDocCont();
        try {
            JAXBElement<IScarto> file = (JAXBElement<IScarto>) getJAXBElement(data);
            IScarto notifica = file.getValue();
            logger.info("Fatture Elettroniche: Attive: Notifica Scarto. MessageId:" + notifica.getMessageId());
            String codiceSDI = notifica.getIdentificativoSdI();
            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, codiceSDI);
            if (fattura != null) {
                logger.info("Fatture Elettroniche: Attive: Fattura già elaborata " + codiceSDI);
            } else {
                String nomeFileP7m = recuperoNomeFileP7m(notifica);
                fattura = recuperoFatturaDaNomeFile(userContext, nomeFileP7m);
                if (fattura != null) {
                    if ((CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) == 0) || (docComponent.isRibaltato(userContext, fattura.getCd_cds_origine(), fattura.getEsercizio()) && CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) > 0)) {
                        salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_SCARTO);
                        StringBuffer errore = estraiErrore(notifica);
                        try {
                            fattura = component.aggiornaFatturaScartoSDI(userContext, fattura, codiceSDI, errore.toString());
                            logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura scartata con nomeFile " + nomeFileP7m);
                            if (fattura instanceof Fattura_attiva_IBulk) {
                                Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) fattura;
                                if (fatturaAttiva.getNotaCreditoAutomaticaGenerata() != null) {
                                    try {
                                        SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaAttiva.getNotaCreditoAutomaticaGenerata());
                                    } catch (Exception ex) {
                                        logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione della stampa dello scarto della fattura con nome file " + nomeFileP7m + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                                        java.io.StringWriter sw = new java.io.StringWriter();
                                        ex.printStackTrace(new java.io.PrintWriter(sw));
                                        SendMail.sendErrorMail("Fatture Elettroniche: Attive: Notifica Scarto. Nome file " + nomeFileP7m, sw.toString());
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione dello scarto della fattura con nome file " + nomeFileP7m + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                            java.io.StringWriter sw = new java.io.StringWriter();
                            ex.printStackTrace(new java.io.PrintWriter(sw));
                            SendMail.sendErrorMail("Fatture Elettroniche: Attive: Notifica Scarto. Nome file " + nomeFileP7m, sw.toString());
                        }
                    }
                } else {
                    logger.warn("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura." + nomeFileP7m);
                    SendMail.sendErrorMail("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura", "Scarto. Nome File " + nomeFileP7m);
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    public Boolean notificaFatturaAttivaDecorrenzaTermini(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            JAXBElement<NotificaDecorrenzaTerminiType> file = (JAXBElement<NotificaDecorrenzaTerminiType>) getJAXBElement(data);
            NotificaDecorrenzaTerminiType notifica = file.getValue();
            logger.info("Fatture Elettroniche: Attive: Decorrenza Termini. MessageId:" + notifica.getMessageId());
            String identificativoSdi = String.valueOf(notifica.getIdentificativoSdI());
            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, identificativoSdi);
            if (fattura != null) {
                if (!StringUtils.isEmpty(fattura.getStatoInvioSdi())) {
                    if (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_DECORRENZA_TERMINI)) {
                        logger.info("Fatture Elettroniche: Attive: Fattura già elaborata ");
                    } else if (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_CONSEGNATA_SDI) ||
                            fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_CONSEGNATA_DESTINATARIO)) {
                        salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_DECORRENZA_TERMINI);
                        try {
                            component.aggiornaFatturaDecorrenzaTerminiSDI(userContext, fattura, notifica.getDescrizione());
                            logger.info("Fatture Elettroniche: Attive: aggiornamento decorrenza termini Fattura con id SDI " + identificativoSdi);
                        } catch (Exception ex) {
                            logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione della decorrenza termini della fattura con id SDI " + identificativoSdi + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                            java.io.StringWriter sw = new java.io.StringWriter();
                            ex.printStackTrace(new java.io.PrintWriter(sw));
                            SendMail.sendErrorMail("Fatture Elettroniche: Attive: Decorrenza Termini. Id SDI " + identificativoSdi, sw.toString());
                        }
                    }
                }
            } else {
                logger.info("Fatture Elettroniche: Attive: Decorrenza Termini Non trovata. Id SDI " + identificativoSdi);
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    public void notificaFatturaAttivaEsito(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException {
        RicercaDocContComponentSession docComponent = recuperoComponentRicercaDocCont();
        FatturaElettronicaAttivaComponentSession component = recuperoComponentFatturaElettronicaAttiva();
        try {
            JAXBElement<NotificaEsitoType> file = (JAXBElement<NotificaEsitoType>) getJAXBElement(data);
            NotificaEsitoType notifica = file.getValue();
            String identificativoSdi = String.valueOf(notifica.getIdentificativoSdI());
            logger.info("Fatture Elettroniche: Attive: Esito. MessageId:" + notifica.getMessageId() + ". Identificativo SDI: " + identificativoSdi);
            Fattura_attivaBulk fattura = recuperoFatturaDaCodiceInvioSDI(userContext, identificativoSdi);
            if (fattura != null) {
                if (!StringUtils.isEmpty(fattura.getStatoInvioSdi())) {
                    if (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_ACCETTATA_DESTINATARIO) || fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_RIFIUTATA_DESTINATARIO)) {
                        logger.info("Fatture Elettroniche: Attive: Fattura già elaborata ");
                    } else if (fattura.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_CONSEGNATA_DESTINATARIO)) {
                        if (esitoAccettato(notifica)) {
                            salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO);
                            try {
                                component.aggiornaFatturaEsitoAccettatoSDI(userContext, fattura);
                                logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura accettata con id SDI " + identificativoSdi);
                            } catch (Exception ex) {
                                logger.error("Fatture Elettroniche: Attive: MessageId:" + notifica.getMessageId() + ". Errore nell'elaborazione della Fattura accettata con id SDI " + identificativoSdi + ". Errore:" + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());
                                java.io.StringWriter sw = new java.io.StringWriter();
                                ex.printStackTrace(new java.io.PrintWriter(sw));
                                SendMail.sendErrorMail("Fatture Elettroniche: Attive: Esito Accettato. Id SDI " + identificativoSdi, sw.toString());
                            }
						} else {
							if ((CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) == 0) || (docComponent.isRibaltato(userContext, fattura.getCd_cds_origine(), fattura.getEsercizio()) && CNRUserContext.getEsercizio(userContext).compareTo(fattura.getEsercizio()) > 0)){
								salvaFileSuDocumentale(data, nomeFile, fattura, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO);
								String rifiuto = recuperoMotivoRifiuto(notifica);
								try{
									fattura = component.aggiornaFatturaRifiutataDestinatarioSDI(userContext, fattura, rifiuto);
									logger.info("Fatture Elettroniche: Attive: aggiornamento Fattura rifiutata con id SDI "+identificativoSdi);
									if (fattura instanceof Fattura_attiva_IBulk){
										Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk)fattura;
										if (fatturaAttiva.getNotaCreditoAutomaticaGenerata() != null){
											try{
                                                SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaAttiva.getNotaCreditoAutomaticaGenerata());
											} catch (Exception ex) {
												logger.error("Fatture Elettroniche: Attive: MessageId:"+notifica.getMessageId()+". Errore nell'elaborazione della stampa della Fattura rifiutata con id SDI "+identificativoSdi + ". Errore:" +ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()):ex.getMessage());
												java.io.StringWriter sw = new java.io.StringWriter();
												ex.printStackTrace(new java.io.PrintWriter(sw));
												SendMail.sendErrorMail("Fatture Elettroniche: Attive: Esito Rifiutato. Id SDI "+identificativoSdi, sw.toString());
											}
										}
									}
								} catch (Exception ex) {
									logger.error("Fatture Elettroniche: Attive: MessageId:"+notifica.getMessageId()+". Errore nell'elaborazione della Fattura rifiutata con id SDI "+identificativoSdi + ". Errore:" +ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()):ex.getMessage());
									java.io.StringWriter sw = new java.io.StringWriter();
									ex.printStackTrace(new java.io.PrintWriter(sw));
									SendMail.sendErrorMail("Fatture Elettroniche: Attive: Esito Rifiutato. Id SDI "+identificativoSdi, sw.toString());
								}
							}
                        }
                        //					} else {
                        //						logger.warn("Fatture Elettroniche: Attive: Stato fattura vuoto non previsto per la notifica esito per la fattura " + identificativoSdi);
                    }
                    //				} else {
                    //					logger.warn("Fatture Elettroniche: Attive: Stato fattura " + fattura.getStatoInvioSdi() + " non previsto per la notifica esito per la fattura " + identificativoSdi);
                }
            } else {
                logger.warn("Fatture Elettroniche: Attive: Per il nome dell'identificativo SDI indicato nel file dell'e-mail non corrisponde nessuna fattura." + identificativoSdi);
                SendMail.sendErrorMail("Fatture Elettroniche: Attive: Per il nome del file inviato indicato nel file dell'e-mail non corrisponde nessuna fattura", "Notifica Esito. Id SDI " + identificativoSdi);
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private String recuperoNomeFileP7m(IScarto notifica) {
        String nomeFileP7m = notifica.getNomeFile();
        return nomeFileP7m;
    }


    private RicercaDocContComponentSession recuperoComponentRicercaDocCont() {
        RicercaDocContComponentSession component = (RicercaDocContComponentSession) EJBCommonServices.createEJB("CNRCHIUSURA00_EJB_RicercaDocContComponentSession");
        return component;
    }

    private FatturaElettronicaAttivaComponentSession recuperoComponentFatturaElettronicaAttiva() {
        FatturaElettronicaAttivaComponentSession component = (FatturaElettronicaAttivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaAttivaComponentSession");
        return component;
    }

    private Fattura_attivaBulk recuperoFatturaDaCodiceInvioSDI(UserContext userContext, String codiceInvioSDI) throws Exception {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = recuperoComponentFatturaAttiva();
        Fattura_attivaBulk fattura = componentFatturaAttiva.ricercaFatturaDaCodiceSDI(userContext, codiceInvioSDI);
        return fattura;
    }

    private Fattura_attivaBulk recuperoFatturaDaNomeFile(UserContext userContext, String nomeFileP7m) throws Exception {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = recuperoComponentFatturaAttiva();
        Fattura_attivaBulk fattura = componentFatturaAttiva.recuperoFatturaElettronicaDaNomeFile(userContext, nomeFileP7m);
        return fattura;
    }

    private FatturaAttivaSingolaComponentSession recuperoComponentFatturaAttiva() {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession");
        return componentFatturaAttiva;
    }

    private Boolean esitoAccettato(NotificaEsitoType notifica) {
		if (notifica.getEsitoCommittente() != null && notifica.getEsitoCommittente().getEsito().compareTo(EsitoCommittenteType.EC_01) == 0){
        return true;
		}
		return false;
    }

    private String recuperoMotivoRifiuto(NotificaEsitoType notifica) {
        String rifiuto = null;
        if (notifica.getEsitoCommittente() != null && notifica.getEsitoCommittente().getEsito().compareTo(EsitoCommittenteType.EC_02) == 0) {
            rifiuto = notifica.getEsitoCommittente().getDescrizione();
        } else {
            rifiuto = "Esito Committente non conforme su notifica con messageId " + notifica.getMessageId();
        }
        return rifiuto;
    }

    private StringBuffer estraiErrore(IScarto notifica) {
        StringBuffer errori = new StringBuffer();
        IListaErrori listaErrori = notifica.getListaErrori();
        if (listaErrori != null && listaErrori.getErrore() != null && !listaErrori.getErrore().isEmpty()) {
            for (Object errore : listaErrori.getErrore()) {
            	if (errore instanceof IErroreType){
            		IErroreType err = (IErroreType)errore; 
            		if (errori.length() != 0) {
                        errori.append(" - ");
                    }
                    errori.append(err.getDescrizione());
            	}
            }
        }
        return errori;
    }

    private UserContext createUserContext() {
        UserContext userContext = new WSUserContext("SDI", null, new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)), null, null, null);
        return userContext;
    }

    private JAXBElement<?> getJAXBElement(DataHandler data) throws ComponentException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(data.getInputStream(), bStream);
            JAXBContext jc = JAXBContext.newInstance(
                    it.gov.agenziaentrate.ivaservizi.docs.xsd.fattura.messaggi.v1.ObjectFactory.class,
                    it.gov.fatturapa.sdi.messaggi.v1.ObjectFactory.class);
            return (JAXBElement<?>) jc.createUnmarshaller().unmarshal(new ByteArrayInputStream(bStream.toByteArray()));
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
//
//	private JAXBElement<?> getJAXBElement(FileSdIType fileSdiType) {
//		JAXBContext jc = null;
//		try {
//			jc = JAXBContext.newInstance("it.gov.fatturapa.sdi.messaggi.v1");
//		} catch (JAXBException e) {
//			logger.error("Errore in fase di inizializzazione di un oggetto JAXB. ", e);
//		}
//		
//		File file = null;
//		try {
//			file = File.createTempFile(fileSdiType.getNomeFile(), ".tmp");
//		} catch (IOException e) {
//			logger.error("Errore in fase di creazione file temporaneo. ", e);
//		}
//		
//		JAXBElement<?> element = null; 
//		if (jc != null && file != null){
//			try{
//				element = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(file);
//			} catch (ClassCastException e) {
//				logger.error("Errore in fase di creazione file temporaneo. ", e);
//			} catch (JAXBException e) {
//				logger.error("Errore generico in fase di caricamento del file. ", e);
//			}
//		}
//		return element;
//	}

    private SOAPFault generaFault(String stringFault) {
        try {
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage();
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPBody body = message.getSOAPBody();
            SOAPFault fault = body.addFault();
            Name faultName = soapFactory.createName("", "", SOAPConstants.URI_NS_SOAP_ENVELOPE);
            fault.setFaultCode(faultName);
            fault.setFaultString(stringFault);
            return fault;
        } catch (SOAPException e) {
            return null;
        }
    }

}
