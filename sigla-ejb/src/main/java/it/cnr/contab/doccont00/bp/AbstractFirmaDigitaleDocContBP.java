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

import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.service.ContabiliService;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.contab.util.Apparence;
import it.cnr.contab.util.PdfSignApparence;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDNotNullConstraintException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public abstract class AbstractFirmaDigitaleDocContBP extends SelezionatoreListaBP implements SearchProvider {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(AbstractFirmaDigitaleDocContBP.class);
    protected String controlloCodiceFiscale;
    private UtenteFirmaDettaglioBulk firmatario;

    public AbstractFirmaDigitaleDocContBP() {
        super();
    }

    public AbstractFirmaDigitaleDocContBP(String s) {
        super(s);
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
    }

    public void setColumnSet(ActionContext actioncontext, String statoTrasmissione) {
        String columnSetName = "firmaBase";
        if (statoTrasmissione.equalsIgnoreCase(StatoTrasmissione.ALL) || statoTrasmissione.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO))
            columnSetName = "all";
        else if (!statoTrasmissione.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO))
            columnSetName = "firmaPredisposta";
        else if (statoTrasmissione.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
            columnSetName = "firmaEseguita";
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary(columnSetName));
        Unita_organizzativaBulk uoScrivania = CNRUserInfo.getUnita_organizzativa(actioncontext);
        if (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0 ||
                uoScrivania.isUoCds()) {
            OrderedHashtable columns = (OrderedHashtable) getColumns();
            columns = (OrderedHashtable) columns.clone();
            ColumnFieldProperty cd_unita_organizzativa = getBulkInfo().getColumnFieldProperty("cds_uo", "cd_unita_organizzativa"),
                    cd_cds = getBulkInfo().getColumnFieldProperty("cds_uo", "cd_cds");
            if (cd_unita_organizzativa != null)
                columns.putFirst("cd_unita_organizzativa", getBulkInfo().getColumnFieldProperty("cds_uo", "cd_unita_organizzativa"));
            if (cd_cds != null && uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0)
                columns.putFirst("cd_cds", getBulkInfo().getColumnFieldProperty("cds_uo", "cd_cds"));
            setColumns(columns);
            table.setColumns(columns);

        }
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] baseToolbar = super.createToolbar();

        Button[] toolbar = new Button[baseToolbar.length + 9];
        int i = 0;
        for (Button button : baseToolbar) {
            toolbar[i++] = button;
        }
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.refresh");
        toolbar[i - 1].setSeparator(true);
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.deletesign");
        toolbar[i - 1].setSeparator(true);
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.predisponi");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.allegadocumenti");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.zipdocumenti");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.pdfdocumenti");
        toolbar[i - 1].setSeparator(true);
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.detail");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
                .getHandler().getProperties(getClass()), "Toolbar.sign");
        toolbar[i - 1].setSeparator(true);
        return toolbar;
    }

    @Override
    protected void init(Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(getClass().getClassLoader().loadClass(config.getInitParameter("bulkClassName"))));
            setMultiSelection(true);
            OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
            ((StatoTrasmissione) model).setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
            setModel(actioncontext, model);
            super.init(config, actioncontext);
            setColumnSet(actioncontext, MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);

            firmatario = ((UtenteComponentSession) createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession", UtenteComponentSession.class)).
                    isUtenteAbilitatoFirma(actioncontext.getUserContext(), getAbilitatoFirma());
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            controlloCodiceFiscale = sess.getVal01(actioncontext.getUserContext(), "CONTROLLO_CF_FIRMA_DOCCONT");
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        } catch (InstantiationException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (ClassNotFoundException e) {
            throw handleException(e);
        }
    }

    protected abstract AbilitatoFirma getAbilitatoFirma();

    public abstract void openIterator(ActionContext actioncontext)
            throws BusinessProcessException;

    protected CRUDComponentSession getComponentSession() {
        return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
    }

    public boolean isPredisponiButtonEnabled() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        return oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
    }

    public boolean isDetailButtonEnabled() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        return (!oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO));
    }

    public boolean isSignButtonEnabled() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        if (firmatario == null)
            return false;
        return oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
    }

    public boolean isDeleteButtonHidden() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        return !oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
    }

    public boolean isDeleteSignButtonHidden() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        if (firmatario == null)
            return true;
        return !oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
    }

    public boolean isPdfDocumentiButtonHidden() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        return !oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
    }

    public boolean isAllegaDocumentiButtonHidden() {
        return isZipDocumentiButtonHidden();
    }

    public boolean isZipDocumentiButtonHidden() {
        StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
        return oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
    }

    @SuppressWarnings("unchecked")
    public void eliminaPredisposizione(ActionContext context) throws BusinessProcessException {
        try {
            List<StatoTrasmissione> selectedElements = getSelectedElements(context);
            if (selectedElements == null || selectedElements.isEmpty())
                throw new ApplicationException("Selezionare almeno un elemento!");
            for (StatoTrasmissione v_mandato_reversaleBulk : selectedElements) {
                aggiornaStato(context, MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO, v_mandato_reversaleBulk);
            }
            setMessage("Cancellazione effettuata correttamente.");
        } catch (CRUDNotNullConstraintException e) {
            setMessage(e.getUserMessage());
        } catch (ApplicationException e) {
            setMessage(e.getMessage());
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public abstract void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException;

    protected abstract void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione... bulks) throws ComponentException, RemoteException;

    @SuppressWarnings("unchecked")
    public void scaricaDocumenti(ActionContext actioncontext) throws Exception {
        setSelection(actioncontext);
        List<StatoTrasmissione> selectelElements = getSelectedElements(actioncontext);
        final HttpServletResponse response = ((HttpActionContext) actioncontext).getResponse();
        final DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        if (selectelElements == null || selectelElements.isEmpty()) {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
            return;
        }
            PDFMergerUtility ut = new PDFMergerUtility();
            ut.setDestinationStream(new ByteArrayOutputStream());
            for (StatoTrasmissione cons : selectelElements) {
                InputStream isToAdd = documentiContabiliService.getStreamDocumento(cons);
                if (isToAdd != null)
                    ut.addSource(isToAdd);
            }
            ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream) ut.getDestinationStream()).toByteArray());
            if (is != null) {
                response.setContentType("application/pdf");
                OutputStream os = response.getOutputStream();
                response.setDateHeader("Expires", 0);
                byte[] buffer = new byte[response.getBufferSize()];
                int buflength;
                while ((buflength = is.read(buffer)) > 0) {
                    os.write(buffer, 0, buflength);
                }
                is.close();
                os.flush();
            }
    }

    private void addToZip(DocumentiContabiliService documentiContabiliService, ZipOutputStream zos, String path, StatoTrasmissione statoTrasmissione) {
        documentiContabiliService.getChildren(documentiContabiliService.getStorageObjectByPath(path).getKey())
                .stream()
                .forEach(storageObject -> {
                    try {
                        if (!Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()))
                                .map(String.class::cast)
                                .filter(s -> s.equals(StoragePropertyNames.CMIS_FOLDER.value()))
                                .isPresent()) {
                            ZipEntry zipEntryChild = new ZipEntry(statoTrasmissione.getCMISFolderName()
                                    .concat(
                                            Optional.ofNullable(storageObject.getPath())
                                            .map(s -> s.substring(statoTrasmissione.getStorePath().length()))
                                            .orElse(StorageDriver.SUFFIX)
                                    ));
                            zos.putNextEntry(zipEntryChild);
                            IOUtils.copyLarge(documentiContabiliService.getResource(storageObject), zos);
                        } else {
                            addToZip(documentiContabiliService, zos, storageObject.getPath(), statoTrasmissione);
                        }
                    } catch (IOException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
    }
    @SuppressWarnings("unchecked")
    public void scaricaZip(ActionContext actioncontext) throws Exception {
        setSelection(actioncontext);
        List<StatoTrasmissione> selectelElements = getSelectedElements(actioncontext);
        final HttpServletResponse response = ((HttpActionContext) actioncontext).getResponse();
        final DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        if (selectelElements == null || selectelElements.isEmpty()) {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
            return;
        }
            final ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            final ContabiliService contabiliService = SpringUtil.getBean("contabiliService", ContabiliService.class);
            final DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
            response.setContentType("application/zip");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-disposition", "attachment; filename=Documenti contabili.zip");
            selectelElements.stream()
                    .forEach(statoTrasmissione -> {
                        addToZip(documentiContabiliService, zos, statoTrasmissione.getStorePath(), statoTrasmissione);
                        if (statoTrasmissione.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_MAN)) {
                            try {
                                MandatoBulk mandatoBulk = (MandatoBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(),
                                        new MandatoIBulk(statoTrasmissione.getCd_cds(), statoTrasmissione.getEsercizio(), statoTrasmissione.getPg_documento_cont()));
                                Tipo_documento_ammBulk tipo_documento_ammBulk = new Tipo_documento_ammBulk();
                                tipo_documento_ammBulk.setTi_entrata_spesa("S");
                                final List<Tipo_documento_ammBulk> tipoDocAmms = Optional.ofNullable(getComponentSession().find(
                                        actioncontext.getUserContext(), tipo_documento_ammBulk.getClass(),
                                        "find", tipo_documento_ammBulk))
                                        .filter(List.class::isInstance)
                                        .map(List.class::cast)
                                        .orElse(null);
                                mandatoBulk.setTipoDocumentoKeys(
                                        Optional.ofNullable(tipoDocAmms.stream()
                                                .collect(Collectors.toMap(
                                                        Tipo_documento_ammBulk::getCd_tipo_documento_amm,
                                                        Tipo_documento_ammBulk::getDs_tipo_documento_amm,
                                                        (u, v) -> {
                                                            throw new IllegalStateException(
                                                                    String.format("Cannot have 2 values (%s, %s) for the same key", u, v)
                                                            );
                                                        }, Hashtable::new)))
                                                .orElse(null)
                                );
                                contabiliService.getNodeRefContabile(mandatoBulk)
                                        .stream()
                                        .forEach(key -> {
                                            try {
                                                final StorageObject storageObject = contabiliService.getStorageObjectBykey(key);
                                                ZipEntry zipEntryChild = new ZipEntry(statoTrasmissione.getCMISFolderName()
                                                        .concat(StorageDriver.SUFFIX)
                                                        .concat(storageObject.getPropertyValue(StoragePropertyNames.NAME.value())));
                                                zos.putNextEntry(zipEntryChild);
                                                IOUtils.copyLarge(contabiliService.getResource(key), zos);
                                            } catch (IOException e) {
                                                throw new DetailedRuntimeException(e);
                                            }
                                        });

                                getComponentSession().find(actioncontext.getUserContext(), MandatoIBulk.class,
                                        "findMandato_riga", actioncontext.getUserContext(), mandatoBulk)
                                        .stream()
                                        .filter(Mandato_rigaBulk.class::isInstance)
                                        .map(Mandato_rigaBulk.class::cast)
                                        .map(mandato_rigaBulk -> {
                                            mandato_rigaBulk.setMandato(mandatoBulk);
                                            return Optional.ofNullable(mandato_rigaBulk.getCd_tipo_documento_amm())
                                                    .filter(cdTipoDocumentoAmm -> cdTipoDocumentoAmm.equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA))
                                                    .map(s -> {
                                                        try {
                                                            return Utility.createMandatoComponentSession().inizializzaTi_fattura(actioncontext.getUserContext(), mandato_rigaBulk);
                                                        } catch (ComponentException | RemoteException e) {
                                                            throw new DetailedRuntimeException(e);
                                                        }
                                                    }).orElseGet(() -> mandato_rigaBulk);
                                        })
                                        .filter(Utility.distinctByKey(mandato_rigaBulk ->
                                                mandato_rigaBulk.getCd_tipo_documento_amm().concat(
                                                        mandato_rigaBulk.getCd_cds_doc_amm().concat(
                                                                String.valueOf(mandato_rigaBulk.getEsercizio_doc_amm()).concat(
                                                                        String.valueOf(mandato_rigaBulk.getPg_doc_amm())
                                                                )
                                                        ))
                                        ))
                                        .collect(Collectors.toList())
                                        .forEach(mandato_rigaBulk -> {
                                            documentiCollegatiDocAmmService
                                                    .getAllegatiDocumentiAmministrativi(mandato_rigaBulk).stream()
                                                    .forEach(allegatoGenericoBulk -> {
                                                        try {
                                                            ZipEntry zipEntryChild = new ZipEntry(statoTrasmissione.getCMISFolderName()
                                                                    .concat(StorageDriver.SUFFIX)
                                                                    .concat(mandato_rigaBulk.getDs_tipo_documento_amm())
                                                                    .concat(" ")
                                                                    .concat(String.valueOf(mandato_rigaBulk.getEsercizio_doc_amm()))
                                                                    .concat("-")
                                                                    .concat(String.valueOf(mandato_rigaBulk.getPg_doc_amm()))
                                                                    .concat(StorageDriver.SUFFIX)
                                                                    .concat(allegatoGenericoBulk.getNome()));
                                                            zos.putNextEntry(zipEntryChild);
                                                            IOUtils.copyLarge(documentiContabiliService.getResource(allegatoGenericoBulk.getStorageKey()), zos);
                                                        } catch (ZipException e) {
                                                            log.warn("Cannot add entry to zip file", e);
                                                        } catch (IOException e) {
                                                            throw new DetailedRuntimeException(e);
                                                        }
                                                    });
                                        });
                            } catch (ComponentException | RemoteException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        }
                    });
            zos.close();
            response.getOutputStream().flush();
    }

    public abstract StatoTrasmissione getStatoTrasmissione(ActionContext actioncontext, Integer esercizio,
                                                           String tipo, String cds, String uo, Long numero_documento) throws BusinessProcessException;

    public void scaricaDocumento(ActionContext actioncontext) throws Exception {
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String uo = ((HttpActionContext) actioncontext).getParameter("uo");
        Long numero_documento = Long.valueOf(((HttpActionContext) actioncontext).getParameter("numero_documento"));
        String tipo = ((HttpActionContext) actioncontext).getParameter("tipo");
        InputStream is = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).getStreamDocumento(
                getStatoTrasmissione(actioncontext, esercizio, tipo, cds, uo, numero_documento)
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

    @SuppressWarnings("unchecked")
    public void sign(ActionContext actioncontext, FirmaOTPBulk firmaOTPBulk) throws Exception {
        List<StatoTrasmissione> selectelElements = getSelectedElements(actioncontext);
        if (selectelElements == null || selectelElements.isEmpty()) {
            throw new ApplicationException("Selezionare almeno un documento contabile!");
        }
        addSomethingToSelectedElements(actioncontext, selectelElements);
        executeSign(actioncontext, selectelElements, firmaOTPBulk);
    }

    protected void executeSign(ActionContext actioncontext, List<StatoTrasmissione> selectelElements, FirmaOTPBulk firmaOTPBulk) throws Exception {
        Map<String, String> subjectDN = Optional.ofNullable(SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).getCertSubjectDN(firmaOTPBulk.getUserName(),
                firmaOTPBulk.getPassword()))
                .orElseThrow(() -> new ApplicationException("Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!"));
        if (Optional.ofNullable(controlloCodiceFiscale).filter(s -> s.equalsIgnoreCase("Y")).isPresent()) {
            SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).controllaCodiceFiscale(
                    subjectDN,
                    ((CNRUserInfo) actioncontext.getUserInfo()).getUtente()
            );
        }
        List<String> nodes = new ArrayList<String>();
        for (StatoTrasmissione bulk : selectelElements) {
            nodes.add(SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).getDocumentKey(bulk, true));
        }
        PdfSignApparence pdfSignApparence = new PdfSignApparence();
        pdfSignApparence.setNodes(nodes);
        pdfSignApparence.setUsername(firmaOTPBulk.getUserName());
        pdfSignApparence.setPassword(firmaOTPBulk.getPassword());
        pdfSignApparence.setOtp(firmaOTPBulk.getOtp());

        Apparence apparence = new Apparence(
                null,
                "Rome", "Firma documento contabile",
                getTitoloFirma() + "\n" + getTitolo() +
                        subjectDN.get("GIVENNAME") + " " + subjectDN.get("SURNAME"),
                0, 40, 1, 300, 80);
        pdfSignApparence.setApparence(apparence);

        try {
            SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class).signDocuments(pdfSignApparence, "service/sigla/firma/doccont");
        } catch (StorageException _ex) {
            throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
        }

        aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA,
                selectelElements.toArray(new StatoTrasmissione[selectelElements.size()]));
        setMessage("Firma effettuata correttamente.");
    }

    protected void addSomethingToSelectedElements(ActionContext actioncontext, List<StatoTrasmissione> selectelElements) throws BusinessProcessException {
    }

    protected abstract String getTitoloFirma();

    private String getTitolo() {
        if (firmatario.getTitoloFirma().equalsIgnoreCase(UtenteFirmaDettaglioBulk.TITOLO_FIRMA_DELEGATO))
            return "";
        return UtenteFirmaDettaglioBulk.titoloKeys.get(firmatario.getTitoloFirma()) + "\n";
    }

    public void invia(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {

    }
}