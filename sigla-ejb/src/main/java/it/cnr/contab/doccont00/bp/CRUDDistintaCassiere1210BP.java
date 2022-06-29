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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.DistintaCassiere1210Bulk;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.contab.util.Apparence;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.PdfSignApparence;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class CRUDDistintaCassiere1210BP extends SimpleCRUDBP {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CRUDDistintaCassiere1210BP.class);
    private final LetteraRemoteDetailCRUDController distintaCassiere1210LettereDaCollegare = new LetteraRemoteDetailCRUDController("DistintaCassiere1210LettereDaCollegare", Lettera_pagam_esteroBulk.class,
            "distintaCassiere1210LettereDaCollegare", "CNRDOCCONT00_EJB_DistintaCassiereComponentSession", this);
    private final LetteraRemoteDetailCRUDController distintaCassiere1210LettereCollegate = new LetteraRemoteDetailCRUDController("DistintaCassiere1210LettereCollegate", Lettera_pagam_esteroBulk.class,
            "distintaCassiere1210LettereCollegate", "CNRDOCCONT00_EJB_DistintaCassiereComponentSession", this);
    protected DocumentiContabiliService documentiContabiliService;
    protected String controlloCodiceFiscale;
    private UtenteFirmaDettaglioBulk firmatario;

    public CRUDDistintaCassiere1210BP() {
        super("Tn");
    }

    public CRUDDistintaCassiere1210BP(String function) {
        super(function + "Tn");
    }

    public RemoteDetailCRUDController getDistintaCassiere1210LettereDaCollegare() {
        return distintaCassiere1210LettereDaCollegare;
    }

    public RemoteDetailCRUDController getDistintaCassiere1210LettereCollegate() {
        return distintaCassiere1210LettereCollegate;
    }

    @Override
    public boolean isDeleteButtonEnabled() {
        return super.isDeleteButtonEnabled() && getModel() != null && ((DistintaCassiere1210Bulk) getModel()).getDtInvio() == null;
    }

    public boolean isRimuoviButtonEnabled() {
        return isEditable() && isEditing();
    }

    public boolean isAssociaButtonEnabled() {
        return getModel() != null && ((DistintaCassiere1210Bulk) getModel()).getDtInvio() == null;
    }

    @Override
    public boolean isPrintButtonHidden() {
        if (isSearching())
            return true;
        if (isDirty())
            return true;
        if (getModel() != null && ((DistintaCassiere1210Bulk) getModel()).getDtInvio() != null)
            return true;
        return super.isPrintButtonHidden();
    }

    public boolean isInviaButtonHidden() {
        if (isSearching() || isViewing() || isDirty())
            return true;
        if (firmatario == null)
            return true;
        return !(getModel() != null && ((DistintaCassiere1210Bulk) getModel()).getDtInvio() == null);
    }

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    protected void initializePrintBP(AbstractPrintBP bp) {
        OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
        DistintaCassiere1210Bulk distinta = (DistintaCassiere1210Bulk) getModel();

        printbp.setReportName("/doccont/doccont/distinta_cassiere_1210.jasper");
        Print_spooler_paramBulk param = new Print_spooler_paramBulk();
        param.setNomeParam("esercizio");
        param.setValoreParam(distinta.getEsercizio().toString());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);
        param = new Print_spooler_paramBulk();
        param.setNomeParam("pg_distinta");
        param.setValoreParam(distinta.getPgDistinta().toString());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        param = new Print_spooler_paramBulk();
        param.setNomeParam("DT_EMISSIONE");
        param.setValoreParam(format.format(distinta.getDtEmissione()));
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("DT_INVIO");
        param.setValoreParam(distinta.getDtInvio() != null ? format.format(distinta.getDtInvio()) : "");
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

    }

    @Override
    protected Button[] createToolbar() {
        Button[] baseToolbar = super.createToolbar();
        Button[] toolbar = new Button[baseToolbar.length + 1];
        int i = 0;
        for (Button button : baseToolbar) {
            toolbar[i++] = button;
        }
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.invia");
        toolbar[i - 1].setSeparator(true);
        return toolbar;
    }

    @Override
    public void save(ActionContext actioncontext) throws ValidationException,
            BusinessProcessException {
        if (getMessage() == null)
            setMessage("Salvataggio eseguito in modo corretto.");
        try {
            setModel(actioncontext, createComponentSession().inizializzaBulkPerModifica(actioncontext.getUserContext(), getModel()));
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
        setDirty(false);
        commitUserTransaction();
    }

    @Override
    public void basicEdit(ActionContext actioncontext,
                          OggettoBulk oggettobulk, boolean flag)
            throws BusinessProcessException {
        super.basicEdit(actioncontext, oggettobulk, flag);
        if (((DistintaCassiere1210Bulk) oggettobulk).getDtInvio() != null)
            setStatus(VIEW);

    }

    @Override
    protected void init(Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
            firmatario = ((UtenteComponentSession) createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession", UtenteComponentSession.class)).
                    isUtenteAbilitatoFirma(actioncontext.getUserContext(), AbilitatoFirma.DIST);
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            controlloCodiceFiscale = sess.getVal01(actioncontext.getUserContext(), "CONTROLLO_CF_FIRMA_DOCCONT");
            if (isEditing())
                setDirty(true);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public void scaricaDocumento(ActionContext actioncontext) throws Exception {
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String uo = ((HttpActionContext) actioncontext).getParameter("uo");
        Long numero_documento = Long.valueOf(((HttpActionContext) actioncontext).getParameter("numero_documento"));
        String tipo = ((HttpActionContext) actioncontext).getParameter("tipo");
        InputStream is = documentiContabiliService.getStreamDocumento(
                new Lettera_pagam_esteroBulk(cds, uo, esercizio, numero_documento)
        );
        if (is == null) {
            log.error("CMIS Object not found: " + esercizio + cds + numero_documento + tipo);
            is = this.getClass().getResourceAsStream("/cmis/404.pdf");
        }
        ((HttpActionContext) actioncontext).getResponse().setContentType("application/pdf");
        OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
        ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
        byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
        int buflength;
        while ((buflength = is.read(buffer)) > 0) {
            os.write(buffer, 0, buflength);
        }
        is.close();
        os.flush();
    }

    public boolean isDistintaInviata() {
        DistintaCassiere1210Bulk distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) getModel();
        return distintaCassiere1210Bulk != null && distintaCassiere1210Bulk.getDtInvio() != null;
    }

    public void scaricaDistinta(ActionContext actioncontext) throws Exception {
        DistintaCassiere1210Bulk distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) getModel();
        InputStream is = documentiContabiliService.getResource(
                documentiContabiliService.getStorageObjectByPath(
                        distintaCassiere1210Bulk.getStorePath()
                                .concat(StorageDriver.SUFFIX)
                                .concat("Distinta 1210 n. "
                                        + distintaCassiere1210Bulk.getPgDistinta() + ".pdf")
                )
        );
        if (is != null) {
            ((HttpActionContext) actioncontext).getResponse().setContentType("application/pdf");
            OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
            ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
            byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
            int buflength;
            while ((buflength = is.read(buffer)) > 0) {
                os.write(buffer, 0, buflength);
            }
            is.close();
            os.flush();
        }
    }

    @Override
    public OggettoBulk initializeModelForInsert(ActionContext actioncontext,
                                                OggettoBulk oggettobulk) throws BusinessProcessException {
        oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
        DistintaCassiere1210Bulk distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) oggettobulk;
        distintaCassiere1210Bulk.setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
        distintaCassiere1210Bulk.setDtEmissione(EJBCommonServices.getServerTimestamp());
        Numerazione_baseComponentSession numerazione =
                (Numerazione_baseComponentSession) createComponentSession("CNRCONFIG00_TABNUM_EJB_TREQUIRED_Numerazione_baseComponentSession");
        try {
            distintaCassiere1210Bulk.setPgDistinta(
                    numerazione.creaNuovoProgressivo(
                            actioncontext.getUserContext(),
                            CNRUserContext.getEsercizio(actioncontext.getUserContext()),
                            "DISTINTA_CASSIERE_1210",
                            "PG_DISTINTA",
                            actioncontext.getUserContext().getUser()
                    )
            );
            setModel(actioncontext, oggettobulk);
            create(actioncontext);
            setStatus(EDIT);
            setDirty(true);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            if (e.getCause() instanceof BusyResourceException)
                throw new BusinessProcessException(new ApplicationException(e.getCause().getMessage()));
            throw handleException(e);
        } catch (BusyResourceException e) {
            throw handleException(e);
        }
        return oggettobulk;
    }

    @SuppressWarnings("unchecked")
    public void rimuoviDocumento(ActionContext context) throws BusinessProcessException {
        try {
            Selection selection = distintaCassiere1210LettereCollegate.getSelection(context);
            if (selection.isEmpty())
                throw new ValidationException("Selezionare almeno un elemento!");
            for (Iterator<Integer> iterator = selection.iterator(); iterator.hasNext(); ) {
                Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(iterator.next());
                lettera_pagam_esteroBulk.setDistintaCassiere(null);
                lettera_pagam_esteroBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                lettera_pagam_esteroBulk.setToBeUpdated();
                createComponentSession().modificaConBulk(context.getUserContext(), lettera_pagam_esteroBulk);
            }
            distintaCassiere1210LettereDaCollegare.reset(context);
            distintaCassiere1210LettereCollegate.reset(context);
        } catch (ValidationException e) {
            throw handleException(e);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void associaDocumento(ActionContext context) throws BusinessProcessException {
        try {
            Selection selection = distintaCassiere1210LettereDaCollegare.getSelection(context);
            if (selection.isEmpty())
                throw new ValidationException("Selezionare almeno un elemento!");
            for (Iterator<Integer> iterator = selection.iterator(); iterator.hasNext(); ) {
                Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereDaCollegare.getLettera(iterator.next());
                lettera_pagam_esteroBulk.setDistintaCassiere((DistintaCassiere1210Bulk) getModel());
                lettera_pagam_esteroBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_INSERITO);
                lettera_pagam_esteroBulk.setToBeUpdated();
                createComponentSession().modificaConBulk(context.getUserContext(), lettera_pagam_esteroBulk);
            }
            distintaCassiere1210LettereDaCollegare.reset(context);
            distintaCassiere1210LettereCollegate.reset(context);
        } catch (ValidationException e) {
            throw handleException(e);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public void signDocuments(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        List<String> nodes = new ArrayList<String>();
        for (int i = 0; i < distintaCassiere1210LettereCollegate.countDetails(); i++) {
            Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(i);
            if (lettera_pagam_esteroBulk.getFl_seconda_firma_apposta() == null || !lettera_pagam_esteroBulk.getFl_seconda_firma_apposta())
                nodes.add(documentiContabiliService.getDocumentKey(lettera_pagam_esteroBulk, true));
        }
        Apparence apparence = new Apparence(
                null,
                "Rome", "Firma documento contabile",
                "per invio all'Istituto cassiere\nFirmato da\n",
                300, 40, 1, 550, 80);
        signDocuments(context, firmaOTPBulk, nodes, apparence);
    }

    public void signDocuments(ActionContext context, FirmaOTPBulk firmaOTPBulk, List<String> nodes, Apparence apparence) throws Exception {
        Map<String, String> subjectDN = Optional.ofNullable(SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).getCertSubjectDN(firmaOTPBulk.getUserName(),
                firmaOTPBulk.getPassword()))
                .orElseThrow(() -> new ApplicationException("Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!"));
        if (Optional.ofNullable(controlloCodiceFiscale).filter(s -> s.equalsIgnoreCase("Y")).isPresent()) {
            SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).controllaCodiceFiscale(
                    subjectDN,
                    ((CNRUserInfo) context.getUserInfo()).getUtente()
            );
        }
        PdfSignApparence pdfSignApparence = new PdfSignApparence();
        pdfSignApparence.setNodes(nodes);
        pdfSignApparence.setUsername(firmaOTPBulk.getUserName());
        pdfSignApparence.setPassword(firmaOTPBulk.getPassword());
        pdfSignApparence.setOtp(firmaOTPBulk.getOtp());

        apparence.setTesto(apparence.getTesto() + subjectDN.get("GIVENNAME") + " " + subjectDN.get("SURNAME"));
        pdfSignApparence.setApparence(apparence);
        try {
            documentiContabiliService.signDocuments(pdfSignApparence, "service/sigla/firma/doccont");
        } catch (StorageException _ex) {
            throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
        }
        for (int i = 0; i < distintaCassiere1210LettereCollegate.countDetails(); i++) {
            Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(i);
            lettera_pagam_esteroBulk.setFl_seconda_firma_apposta(true);
            lettera_pagam_esteroBulk.setToBeUpdated();
            lettera_pagam_esteroBulk = (Lettera_pagam_esteroBulk) createComponentSession().modificaConBulk(context.getUserContext(), lettera_pagam_esteroBulk);
        }
    }

    public void generaDistinta(ActionContext actionContext) throws IOException, ComponentException {
        DistintaCassiere1210Bulk distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) getModel();
        Timestamp currentTimestamp = EJBCommonServices.getServerTimestamp();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Print_spoolerBulk print = new Print_spoolerBulk();
        print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
        print.setFlEmail(false);
        print.setReport("/doccont/doccont/distinta_cassiere_1210.jasper");
        print.setNomeFile("Distinta 1210 n. "
                + distintaCassiere1210Bulk.getPgDistinta() + ".pdf");
        print.setUtcr(actionContext.getUserContext().getUser());
        print.addParam("esercizio", String.valueOf(distintaCassiere1210Bulk.getEsercizio()), String.class);
        print.addParam("pg_distinta", String.valueOf(distintaCassiere1210Bulk.getPgDistinta()), String.class);
        print.addParam("DT_EMISSIONE", format.format(distintaCassiere1210Bulk.getDtEmissione()), String.class);
        print.addParam("DT_INVIO", format.format(currentTimestamp), String.class);

        Report report = SpringUtil.getBean("printService",
                PrintService.class).executeReport(actionContext.getUserContext(),
                print);
        StorageObject node = documentiContabiliService.restoreSimpleDocument(
                distintaCassiere1210Bulk,
                report.getInputStream(),
                report.getContentType(),
                report.getName(),
                distintaCassiere1210Bulk.getStorePath(),
                false);
        PDFMergerUtility ut = new PDFMergerUtility();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ut.setDestinationStream(out);
        ut.addSource(documentiContabiliService.getResource(node));
        for (int i = 0; i < distintaCassiere1210LettereCollegate.countDetails(); i++) {
            Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(i);
            ut.addSource(documentiContabiliService.getStreamDocumento(lettera_pagam_esteroBulk));
        }
        try {
            ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException _ex) {
            throw new ApplicationException("\nAlla distinta risulta allegato un documento non in formato PDF" +
                    ", pertanto è stato escluso dalla selezione.");
        }
        final Configurazione_cnrBulk configurazione = Utility.createConfigurazioneCnrComponentSession()
                .getConfigurazione(
                        actionContext.getUserContext(),
                        0,
                        "*",
                        "DISTINTA_ESTERO",
                        "DIMENSIONE_MASSIMA_MB"
                );

        final BigInteger dimension = BigInteger.valueOf(out.size()).divide(BigInteger.valueOf(1024)).divide(BigInteger.valueOf(1024));
        if (dimension.compareTo(configurazione.getIm01().toBigInteger()) > 0) {
            throw new ApplicationMessageFormatException("La dimensione della distinta {0}Mbytes supera la dimensione massima consentita {1}Mbytes!",
                    dimension,
                    configurazione.getIm01().toBigInteger());
        }

        documentiContabiliService.restoreSimpleDocument(
            distintaCassiere1210Bulk,
            new ByteArrayInputStream(out.toByteArray()),
            MimeTypes.PDF.mimetype(),
            DocumentiContabiliService.DISTINTA_PEC_PDF,
            distintaCassiere1210Bulk.getStorePath(),
            false
        );
    }

    public void invia(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        DistintaCassiere1210Bulk distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) getModel();
        Timestamp currentTimestamp = EJBCommonServices.getServerTimestamp();
        List<String> nodes = new ArrayList<String>();
        for (int i = 0; i < distintaCassiere1210LettereCollegate.countDetails(); i++) {
            Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(i);
            nodes.add(documentiContabiliService.getDocumentKey(lettera_pagam_esteroBulk, true));
        }
        nodes.add(documentiContabiliService.getStorageObjectByPath(distintaCassiere1210Bulk.getStorePath().concat(StorageDriver.SUFFIX).concat("Distinta 1210 n. "
                + distintaCassiere1210Bulk.getPgDistinta() + ".pdf")).getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()));
        StorageObject distintaPEC = documentiContabiliService.getStorageObjectByPath(distintaCassiere1210Bulk.getStorePath().concat(StorageDriver.SUFFIX).concat(DocumentiContabiliService.DISTINTA_PEC_PDF));
        nodes.add(distintaPEC.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()));

        Apparence apparence = new Apparence(
                null,
                "Rome", "Firma documento contabile",
                "per invio all'Istituto cassiere\nFirmato da\n",
                300, 40, 1, 550, 80);
        signDocuments(context, firmaOTPBulk, nodes, apparence);
        for (int i = 0; i < distintaCassiere1210LettereCollegate.countDetails(); i++) {
            Lettera_pagam_esteroBulk lettera_pagam_esteroBulk = distintaCassiere1210LettereCollegate.getLettera(i);
            lettera_pagam_esteroBulk = ((Lettera_pagam_esteroBulk) createComponentSession().findByPrimaryKey(context.getUserContext(), lettera_pagam_esteroBulk));
            lettera_pagam_esteroBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
            lettera_pagam_esteroBulk.setToBeUpdated();
            lettera_pagam_esteroBulk = (Lettera_pagam_esteroBulk) createComponentSession().modificaConBulk(context.getUserContext(), lettera_pagam_esteroBulk);
        }
        distintaCassiere1210Bulk.setDtInvio(currentTimestamp);
        distintaCassiere1210Bulk.setToBeUpdated();
        distintaCassiere1210Bulk = (DistintaCassiere1210Bulk) createComponentSession().modificaConBulk(context.getUserContext(), distintaCassiere1210Bulk);
        //nodes.add(nodo);
        if (distintaCassiere1210Bulk.getEsercizio() != null && distintaCassiere1210Bulk.getPgDistinta() != null) {
            documentiContabiliService.inviaDistintaPEC1210(
                    Collections.singletonList(distintaPEC.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value())),
                    true,
                    distintaCassiere1210Bulk.getEsercizio() + "/" + distintaCassiere1210Bulk.getPgDistinta()
            );
        } else {
            documentiContabiliService.inviaDistintaPEC1210(Collections.singletonList(distintaPEC.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value())));
        }
        commitUserTransaction();
        setMessage("Invio effettuato correttamente.");
        setStatus(VIEW);
    }

    private Integer getLastPagePDF(InputStream stream) throws IOException {
        PDDocument document = PDDocument.load(stream);
        int lastPage = document.getNumberOfPages();
        document.close();
        return lastPage;
    }

    @Override
    protected void closed(ActionContext context)
            throws BusinessProcessException {
        try {
            distintaCassiere1210LettereCollegate.closed(context);
            distintaCassiere1210LettereDaCollegare.closed(context);
        } catch (RemoteException e) {
            throw handleException(e);
        }
        super.closed(context);
    }

    class LetteraRemoteDetailCRUDController extends RemoteDetailCRUDController {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        public LetteraRemoteDetailCRUDController(String s, Class class1,
                                                 String s1, String s2, FormController formcontroller) {
            super(s, class1, s1, s2, formcontroller);
            setPageSize(1000);
        }

        public Lettera_pagam_esteroBulk getLettera(int index) {
            return (Lettera_pagam_esteroBulk) getDetailsPage().get(index);
        }

        @Override
        public void basicReset(ActionContext actioncontext) {
            if (!isSearching())
                super.basicReset(actioncontext);
        }
    }

}