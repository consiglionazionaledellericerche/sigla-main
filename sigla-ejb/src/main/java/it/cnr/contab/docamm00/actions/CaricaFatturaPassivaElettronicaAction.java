package it.cnr.contab.docamm00.actions;


import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.bp.CaricaFatturaElettronicaBP;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.RicezioneFatturePA;
import it.cnr.contab.docamm00.fatturapa.bulk.FileSdIConMetadatiTypeBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.FormAction;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.upload.UploadedFile;
import it.gov.fatturapa.sdi.messaggi.v1.MetadatiInvioFileType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;
import it.gov.fatturapa.sdi.monitoraggio.v1.FattureRicevuteType;
import it.gov.fatturapa.sdi.monitoraggio.v1.MonitoraggioFlussiType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.FileSdIConMetadatiType;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;

public class CaricaFatturaPassivaElettronicaAction extends FormAction {
	private static final long serialVersionUID = 1L;
	private transient final static Logger logger = LoggerFactory.getLogger(CaricaFatturaPassivaElettronicaAction.class);

	public Forward doNotificaDecorrenzaTermini(ActionContext actioncontext) throws java.rmi.RemoteException {
		HttpServletRequest request = ((HttpActionContext)actioncontext).getRequest();
		try {
			FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
	    			FatturazioneElettronicaClient.class);
	    	JAXBElement<NotificaDecorrenzaTerminiType>  notificaDecorrenzaTermini = (JAXBElement<NotificaDecorrenzaTerminiType>) client.getUnmarshaller().unmarshal(new StreamSource(request.getInputStream()));
	    	SpringUtil.getBean("ricezioneFattureService", RicezioneFatturePA.class).
	    		notificaDecorrenzaTermini(notificaDecorrenzaTermini.getValue().getIdentificativoSdI(), 
	    				notificaDecorrenzaTermini.getValue().getNomeFile(), 
	    				null);//TODO
		} catch (Exception e) {
			throw new RemoteException("Internal Server Error", e);
		}		
		return actioncontext.findDefaultForward();
	}
	public Forward doRiceviFatturaSDI(ActionContext actioncontext) throws java.rmi.RemoteException {
		HttpServletRequest request = ((HttpActionContext)actioncontext).getRequest();
		try {
	    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
	    			FatturazioneElettronicaClient.class);
	    	JAXBElement<FileSdIConMetadatiType>  fileSdIConMetadati = (JAXBElement<FileSdIConMetadatiType>) client.getUnmarshaller().unmarshal(new StreamSource(request.getInputStream()));
	    	SpringUtil.getBean("ricezioneFattureService", RicezioneFatturePA.class).
			riceviFatturaSIGLA(fileSdIConMetadati.getValue().getIdentificativoSdI(), 
					fileSdIConMetadati.getValue().getNomeFile(), null, 
					fileSdIConMetadati.getValue().getFile(), 
					fileSdIConMetadati.getValue().getNomeFileMetadati(),
					fileSdIConMetadati.getValue().getMetadati());	    	
		} catch (Exception e) {
			throw new RemoteException("Internal Server Error", e);
		}		
		return actioncontext.findDefaultForward();
	}	
	
	@SuppressWarnings("unchecked")
	public Forward doCaricaFattura(ActionContext actioncontext) throws java.rmi.RemoteException {
		CaricaFatturaElettronicaBP caricaPassivaElettronicaBP = (CaricaFatturaElettronicaBP) actioncontext.getBusinessProcess();
		try {
			caricaPassivaElettronicaBP.fillModel(actioncontext);
			FileSdIConMetadatiTypeBulk fileSdIConMetadatiTypeBulk = (FileSdIConMetadatiTypeBulk) caricaPassivaElettronicaBP.getModel();
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.file");
			UploadedFile fileMetadata = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.metadati");
			if (file.getFile() == null || fileMetadata.getFile() == null){
				caricaPassivaElettronicaBP.setMessage("Valorizzare i campi obbligatori!");
				return actioncontext.findDefaultForward();
			}	
	    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
	    			FatturazioneElettronicaClient.class);
	    	JAXBElement<MetadatiInvioFileType> metadatiInvioFileType = (JAXBElement<MetadatiInvioFileType>) client.getUnmarshaller().unmarshal(new StreamSource(fileMetadata.getFile()));
	    	fileSdIConMetadatiTypeBulk.setIdentificativoSdI(metadatiInvioFileType.getValue().getIdentificativoSdI());
	    	String cuu = metadatiInvioFileType.getValue().getCodiceDestinatario();
	    	//TODO
	    	
	    	DataSource byteArrayDataSource = new UploadedFileDataSource(file);
			DataSource byteArrayDataSource2 = new UploadedFileDataSource(fileMetadata);
			
			SpringUtil.getBean("ricezioneFattureService", RicezioneFatturePA.class).
				riceviFatturaSIGLA(fileSdIConMetadatiTypeBulk.getIdentificativoSdI(), file.getName(), null, 
						new DataHandler(byteArrayDataSource), 
								fileMetadata.getName(), 
								new DataHandler(byteArrayDataSource2));
			caricaPassivaElettronicaBP.setMessage("File caricato correttamente");
			fileSdIConMetadatiTypeBulk.setIdentificativoSdI(null);
		} catch (FillException e) {
			return handleException(actioncontext, e);
		} catch (FileNotFoundException e) {
			return handleException(actioncontext, e);
		} catch (IOException e) {
			return handleException(actioncontext, e);
		} catch (ComponentException e) {
			return handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	};

	@SuppressWarnings("unchecked")
	public Forward doControllaFatture(ActionContext actioncontext) throws java.rmi.RemoteException {
		CaricaFatturaElettronicaBP caricaPassivaElettronicaBP = (CaricaFatturaElettronicaBP) actioncontext.getBusinessProcess();
    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
    			FatturazioneElettronicaClient.class);
		UploadedFile fileFattureRicevute = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.fileFattureRicevute");
		if (fileFattureRicevute.getFile() == null){
			caricaPassivaElettronicaBP.setMessage("Valorizzare il file!");
			return actioncontext.findDefaultForward();
		}	

		try {
    		FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession = 
    				(FatturaElettronicaPassivaComponentSession) caricaPassivaElettronicaBP.createComponentSession("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession");
    		List<FattureRicevuteType.Flusso> results = new ArrayList<FattureRicevuteType.Flusso>();
			JAXBElement<MonitoraggioFlussiType> fattureRicevuteType = ((JAXBElement<MonitoraggioFlussiType>) 
					client.getUnmarshaller().unmarshal(new StreamSource(fileFattureRicevute.getFile())));
			for (FattureRicevuteType.Flusso flusso : fattureRicevuteType.getValue().getFattureRicevute().getFlusso()) {
				if (!flusso.getStato().equalsIgnoreCase("SF00")) {
					if (!fatturaElettronicaPassivaComponentSession.existsIdentificativo(actioncontext.getUserContext(), Long.valueOf(flusso.getIdSdI()))) {
						results.add(flusso);
					}					
				}
			}
			if (results.isEmpty()){
				caricaPassivaElettronicaBP.setMessage("Non ci sono anomalie.");
			} else {
				caricaPassivaElettronicaBP.setAnomalie(results);
				caricaPassivaElettronicaBP.setMessage("Non sono presenti "+ results.size() + " fatture, controllare il dettaglio!");				
			}
		} catch (XmlMappingException e) {
			return handleException(actioncontext, e);
		} catch (IOException e) {
			return handleException(actioncontext, e);
		} catch (BusinessProcessException e) {
			return handleException(actioncontext, e);
		} catch (ComponentException e) {
			return handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}	
	@SuppressWarnings("unchecked")
	public Forward doAllineaNotifiche(ActionContext actioncontext) {
		TipoIntegrazioneSDI tipoIntegrazioneSDI = TipoIntegrazioneSDI.PEC;
		
		CaricaFatturaElettronicaBP caricaPassivaElettronicaBP = (CaricaFatturaElettronicaBP) actioncontext.getBusinessProcess();
    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
    			FatturazioneElettronicaClient.class);
		UploadedFile fileFattureRicevute = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.fileFattureRicevute");
		if (fileFattureRicevute.getFile() == null){
			caricaPassivaElettronicaBP.setMessage("Valorizzare il file!");
			return actioncontext.findDefaultForward();
		}	
		try {
			String integrazioneSDI = Utility.createConfigurazioneCnrComponentSession().
					getVal01(actioncontext.getUserContext(), null, null, 
							Configurazione_cnrBulk.PK_INTEGRAZIONE_SDI, Configurazione_cnrBulk.SK_INTEGRAZIONE_SDI);
				if (integrazioneSDI != null)
					tipoIntegrazioneSDI = TipoIntegrazioneSDI.valueOf(integrazioneSDI); 

			FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession = 
    				(FatturaElettronicaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession");
			JAXBElement<MonitoraggioFlussiType> fattureRicevuteType = ((JAXBElement<MonitoraggioFlussiType>) 
					client.getUnmarshaller().unmarshal(new StreamSource(fileFattureRicevute.getFile())));
			List<Long> identificativi = new ArrayList<Long>();
			for (FattureRicevuteType.Flusso flusso : fattureRicevuteType.getValue().getFattureRicevute().getFlusso()) {
				if (!flusso.getStato().equalsIgnoreCase("SF00")) {
					logger.info("Inizio controllo identificativo:" + flusso.getIdSdI());
					identificativi.add(Long.valueOf(flusso.getIdSdI()));
					fatturaElettronicaPassivaComponentSession.allineaEsitoCommitente(actioncontext.getUserContext(), 
							Long.valueOf(flusso.getIdSdI()), flusso.getStato(), tipoIntegrazioneSDI);
					logger.info("Fine controllo identificativo:" + flusso.getIdSdI());					
				}
			}
			fatturaElettronicaPassivaComponentSession.allineaEsitoCommitente(actioncontext.getUserContext(), 
					identificativi, tipoIntegrazioneSDI);
			caricaPassivaElettronicaBP.setMessage("Notifiche allineate correttamente.");
		} catch (XmlMappingException e) {
			return handleException(actioncontext, e);
		} catch (IOException e) {
			return handleException(actioncontext, e);
		} catch (ComponentException e) {
			return handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}	
	
	public Forward doScaricaFatture(ActionContext actioncontext) throws FillException {
		FatturaPassivaElettronicaService fatturaPassivaElettronicaService = SpringUtil.getBean("fatturaPassivaElettronicaService", 
				FatturaPassivaElettronicaService.class);
		CaricaFatturaElettronicaBP caricaFatturaElettronicaBP = (CaricaFatturaElettronicaBP)actioncontext.getBusinessProcess();
		caricaFatturaElettronicaBP.fillModel(actioncontext);
		FileSdIConMetadatiTypeBulk model = (FileSdIConMetadatiTypeBulk) caricaFatturaElettronicaBP.getModel();
		fatturaPassivaElettronicaService.caricaFatture(model.getDaysBefore(), model.getIdentificativoSDI());
		return actioncontext.findDefaultForward();
	}	
	
	
	class UploadedFileDataSource implements DataSource {
		
		private UploadedFile file;
		
		public UploadedFileDataSource(UploadedFile file) {
			this.file = file;
		}
		

		@Override
		public OutputStream getOutputStream() throws IOException {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
 			IOUtils.copy(getInputStream(), output);
			return output;
		}
		
		@Override
		public String getName() {
			return file.getName();
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(file.getFile());
		}
		
		@Override
		public String getContentType() {
			return file.getContentType();
		}
		
		
	}
	
	
}