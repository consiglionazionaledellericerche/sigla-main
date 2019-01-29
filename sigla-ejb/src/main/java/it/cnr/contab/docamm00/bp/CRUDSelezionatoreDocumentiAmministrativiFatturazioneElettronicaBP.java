package it.cnr.contab.docamm00.bp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.SignP7M;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;

public class CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP extends SelezionatoreListaBP  implements SearchProvider {
    private transient final static Logger logger = LoggerFactory.getLogger(CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP.class);
    private static final long serialVersionUID = 1L;
    private DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService;
    private boolean utenteNonAbilitatoFirma;

    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP() {
        this("");
    }


    /**
     * DocumentiAmministrativiProtocollabiliBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP(String function) {
        super(function + "Tr");
    }

    public boolean isUtenteNonAbilitatoFirma() {
        return utenteNonAbilitatoFirma;
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
    }

    public void setUtenteNonAbilitatoFirma(boolean utenteNonAbilitatoFirma) {
        this.utenteNonAbilitatoFirma = utenteNonAbilitatoFirma;
    }

    @Override
    protected void init(Config config, ActionContext context)
    		throws BusinessProcessException {
    	try {
    		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(getClass().getClassLoader().loadClass(config.getInitParameter("bulkClassName"))));
    		setMultiSelection(true);
    		OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
    		UserContext userContext = context.getUserContext();
    		try {
    			setUtenteNonAbilitatoFirma(isUtenteNonAbilitatoFirma(userContext));
    		} catch (ComponentException e) {
    			logger.error("Utente Non Abilitato Firma", e);
    		}
    		if (isUtenteNonAbilitatoFirma()){
    			((Fattura_attivaBulk) model).setStatoFattElett(Fattura_attivaBulk.DA_PREDISPORRE_ALLA_FIRMA);
    		} else {
    			((Fattura_attivaBulk) model).setStatoFattElett(Fattura_attivaBulk.DA_PREDISPORRE_E_FIRMARE);
    		}
    		setModel(context, model);
    		super.init(config, context);
    		setColumns(getBulkInfo().getColumnFieldPropertyDictionary("fatturazioneElettronicaSet"));
    		openIterator(context);
    		documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
    	} catch (InstantiationException e) {
    		throw handleException(e);
    	} catch (IllegalAccessException e) {
    		throw handleException(e);
    	} catch (ClassNotFoundException e) {
    		throw handleException(e);
    	}
    }

	public void openIterator(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			setIterator(actioncontext, search(
			        actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}

	public RemoteIterator search(
			ActionContext actioncontext,
			CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk)
			throws BusinessProcessException {
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    fattura,
                    "selectByClauseForFatturazioneElettronica");
        } catch (ComponentException|RemoteException e) {
            throw handleException(e);
        }
	}

    protected CRUDComponentSession getComponentSession() {
        return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
    }

	public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String cdUo = ((HttpActionContext) actioncontext).getParameter("cdUo");
        Long pgFattura = Long.valueOf(((HttpActionContext) actioncontext).getParameter("pgFattura"));
        StorageObject storageObject = documentiCollegatiDocAmmService.recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
        InputStream is = null;
        if (storageObject == null) {
            is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
        } else {
            is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
            if (is == null) {
                is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
            }
        }
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

    private InputStream getStreamNewDocument(ActionContext actioncontext,
                                             Integer esercizio, String cds, String cdUo, Long pgFattura)
            throws Exception {
        InputStream is;
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);
        UserContext userContext = actioncontext.getUserContext();
        Fattura_attivaBulk fattura = componentFatturaAttiva.ricercaFatturaByKey(userContext, esercizio.longValue(), cds, cdUo, pgFattura);
        SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fattura);
        is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
        return is;
    }

    public boolean isUtenteNonAbilitatoFirma(UserContext userContext) throws ApplicationException {
        try {
            return !UtenteBulk.isAbilitatoFirmaFatturazioneElettronica(userContext);
        } catch (ComponentException | RemoteException e) {
            throw new ApplicationException(e);
        }
    }

    public boolean isPredisponiButtonVisible() {
        return !isUtenteNonAbilitatoFirma();
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] baseToolbar = super.createToolbar();
        Button[] toolbar = new Button[baseToolbar.length + 2];
        int i = 0;
        for (Button button : baseToolbar) {
            toolbar[i++] = button;
        }
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.predisponi");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.FirmaInvia");
        return toolbar;
    }

    public DocAmmFatturazioneElettronicaComponentSession createComponentSession()
            throws BusinessProcessException {
        return (DocAmmFatturazioneElettronicaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession",
                DocAmmFatturazioneElettronicaComponentSession.class);
    }

	public void predisponiPerLaFirma(ActionContext context) throws BusinessProcessException{
		try {
            logger.info("Inizio Predisposizione per la firma");
	        UserContext userContext = context.getUserContext();
	        List<OggettoBulk> lista = getSelectedElements(context);

	        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
	                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
	                FatturaAttivaSingolaComponentSession.class);

	        for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
	            OggettoBulk docAmm = i.next();
	            if (docAmm instanceof Fattura_attivaBulk) {
	            	protocollaECreaFileXml(userContext, componentFatturaAttiva, (Fattura_attivaBulk)docAmm);
	            }
	        }
            commitUserTransaction();
            setFocusedElement(context, null);
            refresh(context);
			
		} catch (ApplicationException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}


	public Fattura_attivaBulk protocollaECreaFileXml(UserContext userContext,
			FatturaAttivaSingolaComponentSession componentFatturaAttiva, Fattura_attivaBulk fatturaAttiva)
			throws BusinessProcessException, ComponentException, RemoteException, PersistencyException,
			ApplicationException {
		logger.info("Processo la fattura");
		File file = creaFileXml(userContext, fatturaAttiva);

		logger.info("Creato file XML");
		if (fatturaAttiva.getProtocollo_iva() == null){
		    Fattura_attivaBulk fatturaAttivaProtocollata = protocollazione(userContext, fatturaAttiva);
		    fatturaAttiva = fatturaAttivaProtocollata;
		    logger.info("Creato protocollazione");
		}
		List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
		List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
		try {
		    StorageFile storageFile = new StorageFileFatturaAttiva(file, fatturaAttiva,
		            "application/xml", "FAXA" + fatturaAttiva.constructCMISNomeFile() + ".xml");

		    if (storageFile != null) {
		        //E' previsto solo l'inserimento ma non l'aggiornamento
		        String path = storageFile.getStorageParentPath();
		        try {
		            Optional.ofNullable(documentiCollegatiDocAmmService.restoreSimpleDocument(
		                    storageFile,
		                    storageFile.getInputStream(),
		                    storageFile.getContentType(),
		                    storageFile.getFileName(),
		                    path,
		                    false
		            )).ifPresent(storageObject -> {
		                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
		                aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA.value());
		                documentiCollegatiDocAmmService.updateProperties(
		                        Collections.singletonMap(
		                                StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
		                                aspects),
		                        storageObject);
		                storageFile.setStorageObject(storageObject);
		                storageFileCreate.add(storageFile);
		            });
		            logger.info("Salvato file XML sul Documentale");
		        } catch (StorageException _ex) {
		            if (_ex.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
		                throw new ApplicationException("CMIS - File [" + storageFile.getFileName() + "] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
		            throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + _ex.getMessage() + ")");
		        }
		        fatturaAttiva = componentFatturaAttiva.aggiornaFatturaPredispostaAllaFirma(userContext, fatturaAttiva);
		    }
		} catch (Exception e) {
			/*
			    Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			 */
		    for (StorageFile storageFile : storageFileAnnullati) {
		        String cmisFileName = storageFile.getFileName();
		        String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".") + 1);
		        String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
		        storageFile.setFileName(cmisFileName.replace(stringToDelete, "." + cmisFileEstensione));
		        documentiCollegatiDocAmmService.updateProperties(storageFile, storageFile.getStorageObject());
		    }
		    throw new BusinessProcessException(e);
		}
		documentiCollegatiDocAmmService.gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaAttiva);
		return fatturaAttiva;
	}
    public void firmaOTP(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        UserContext userContext = context.getUserContext();
        List<Fattura_attivaBulk> lista = getSelectedElements(context);
        String errorMessage = firmaFatture(firmaOTPBulk, lista); 
        if (errorMessage != null){
            setMessage(errorMessage);
        }
        aggiornaFatturaEInviaMail(userContext, lista);
        commitUserTransaction();
        setFocusedElement(context, null);
        refresh(context);
    }


	public String firmaFatture(FirmaOTPBulk firmaOTPBulk, List<Fattura_attivaBulk> listFattura)
			throws ApplicationException {
		String errorMessage = null; 
		for (Fattura_attivaBulk fatturaProtocollata : listFattura) { 
        	StorageObject so = fatturaProtocollata.getStorageObject();
        	if (so != null) {
        		if (so.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0) {
        			String nomeFile = so.getPropertyValue(StoragePropertyNames.NAME.value());
        			String nomeFileP7m = nomeFile + ".p7m";
        			SignP7M signP7M = new SignP7M(
        					so.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()),
        					firmaOTPBulk.getUserName(),
        					firmaOTPBulk.getPassword(),
        					firmaOTPBulk.getOtp(),
        					nomeFileP7m
        					);
        			try {
        				Optional.ofNullable(documentiCollegatiDocAmmService.signDocuments(signP7M, "service/sigla/firma/fatture"))
        				.map(key -> documentiCollegatiDocAmmService.getStorageObjectBykey(key))
        				.ifPresent(storageObject -> {
        					InputStream streamSigned = documentiCollegatiDocAmmService.getResource(storageObject);
        					File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", nomeFileP7m);
        					try {
        						OutputStream outputStream = new FileOutputStream(fileSigned);
        						IOUtils.copy(streamSigned, outputStream);
        						outputStream.close();
        						logger.info("Salvato file firmato temporaneo");
        						fatturaProtocollata.setNomeFileInvioSdi(nomeFileP7m);
        						fatturaProtocollata.setFile(fileSigned);
        					} catch (Exception ex) {
        						logger.error("Errore nella firma del file");
        						List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
        						aspects.remove(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
        						documentiCollegatiDocAmmService.updateProperties(
        								Collections.singletonMap(
        										StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
        										aspects),
        								storageObject);
        						documentiCollegatiDocAmmService.delete(storageObject);
        					}
        				});
        			} catch (StorageException _ex) {
        				logger.error("Errore in fase di firma della fattura "+_ex.getMessage());
        				errorMessage = FirmaOTPBulk.errorMessage(_ex.getMessage());
            			logger.error(errorMessage);
        				break;
        			}
        		} else {
    				errorMessage = "Errore. Il file XML salvato era vuoto.";
        			logger.error(errorMessage);
    				break;
        		}
        	}
        }
		return errorMessage;
	}


	public void recuperoFilePerFirma(ActionContext context, List<Fattura_attivaBulk> lista)
			throws ApplicationException, IOException {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva;
		try {
			componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
			        "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
			        FatturaAttivaSingolaComponentSession.class);
		} catch (BusinessProcessException e1) {
			throw new ApplicationException(e1);
		}
		for (Iterator<Fattura_attivaBulk> i = lista.iterator(); i.hasNext(); ) {
			Fattura_attivaBulk docAmm = i.next();
			if (Fattura_attivaBulk.FATT_ELETT_ALLA_FIRMA.equals(docAmm.getStatoInvioSdi())){
				try {
					protocollaECreaFileXml(context.getUserContext(), componentFatturaAttiva, (Fattura_attivaBulk)docAmm);
					commitUserTransaction();
				} catch (BusinessProcessException | ComponentException | PersistencyException e) {
					throw new ApplicationException(e);
				}
			}
			StorageObject so = documentiCollegatiDocAmmService.getFileXmlFatturaAttiva(docAmm);
			if (so != null) {
				logger.info("Recuperato File XML");
				docAmm.setStorageObject(so);
			}
		}
	}


	public void aggiornaFatturaEInviaMail(UserContext userContext, List<Fattura_attivaBulk> listaFattura)
			throws BusinessProcessException, RemoteException, ComponentException {
		DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();
        Configurazione_cnrBulk config = component.getAuthenticatorPecSdi(userContext);
        logger.info("Recuperata Autenticazione PEC");
    	FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean(FatturaPassivaElettronicaService.class);
    	String pwd = null;
    	try {
    		pwd = StringEncrypter.decrypt(config.getVal01(), config.getVal02());
    	} catch (EncryptionException e1) {
    		new ApplicationException("Cannot decrypt password");
    	}
    	final String password = pwd;
        logger.info("Decrypt password");
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);
		for (Fattura_attivaBulk fatturaProtocollata : listaFattura) { 
        	if (fatturaProtocollata.getFile() != null){
            	try {
            		Fattura_attivaBulk fattura = (Fattura_attivaBulk)componentFatturaAttiva.findByPrimaryKey(userContext, fatturaProtocollata); 
            		componentFatturaAttiva.aggiornaFatturaInvioSDI(userContext, fattura);
            		logger.info("Fattura con progressivo univoco " + fatturaProtocollata.getEsercizio() + "/" + fatturaProtocollata.getProgrUnivocoAnno() + " aggiornata.");
            		if (!fatturaProtocollata.isNotaCreditoDaNonInviareASdi()) {
            			fatturaService.inviaFatturaElettronica(config.getVal01(), password, fatturaProtocollata.getFile(), fatturaProtocollata.getNomeFileInvioSdi());
            			logger.info("File firmato inviato");
            		}
            	} catch (Exception ex) {
            		logger.error("Errore nell'invio del file " + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());

            		throw new DetailedRuntimeException("Errore nell'invio della mail PEC per la fatturazione elettronica. Ripetere l'operazione di firma!");
            	}
        	}
        };
	}

    public Fattura_attivaBulk protocollazione(UserContext userContext,
                                              Fattura_attivaBulk fattura) throws BusinessProcessException,
            ComponentException, RemoteException, PersistencyException {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);

        Long pgProtocollazione = componentFatturaAttiva.callGetPgPerProtocolloIVA(userContext);
        Long pgStampa = componentFatturaAttiva.callGetPgPerStampa(userContext);
        Timestamp dataStampa = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
        Integer offSet = 0;
        componentFatturaAttiva.preparaProtocollazioneEProtocolla(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
        return componentFatturaAttiva.ricercaFatturaByKey(userContext, new Long(fattura.getEsercizio()), fattura.getCd_cds(), fattura.getCd_unita_organizzativa(), fattura.getPg_fattura_attiva());
    }

    private void extracted(Exception e) throws ApplicationException {
        throw new ApplicationException(e.getMessage());
    }

    private File creaFileXml(UserContext userContext, Fattura_attivaBulk fattura) throws BusinessProcessException {
        try {
            DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();

            JAXBElement<FatturaElettronicaType> fatturaType = component.creaFatturaElettronicaType(userContext, fattura);
            String nomeFile = component.recuperoNomeFileXml(userContext, fattura);
            File file = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", nomeFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            JAXBContext jaxbContext = JAXBContext.newInstance("it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1");
            jaxbContext.createMarshaller().marshal(fatturaType, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            return file;
        } catch (Exception e) {
            throw new BusinessProcessException(e);
        }
    }
}
