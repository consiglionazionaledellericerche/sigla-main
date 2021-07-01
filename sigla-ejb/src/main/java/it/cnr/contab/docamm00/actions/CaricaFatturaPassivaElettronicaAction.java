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
import it.cnr.jada.DetailedRuntimeException;
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

import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	    	
	    	getRicezioneFattureService().
	    		notificaDecorrenzaTermini(new BigInteger(notificaDecorrenzaTermini.getValue().getIdentificativoSdI()), 
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
	    	getRicezioneFattureService().
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
			if (file.getFile() == null || (!Optional.ofNullable(fileMetadata).map(UploadedFile::getFile).isPresent() && fileSdIConMetadatiTypeBulk.getIdentificativoSdI() == null)){
				caricaPassivaElettronicaBP.setMessage("Valorizzare i campi obbligatori!");
				return actioncontext.findDefaultForward();
			}	
	    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
	    			FatturazioneElettronicaClient.class);

			fileSdIConMetadatiTypeBulk.setIdentificativoSdI(
					Optional.ofNullable(fileMetadata)
							.map(UploadedFile::getFile)
							.map(file1 -> new StreamSource(file1))
							.map(streamSource -> {
								try {
									return client.getUnmarshaller().unmarshal(streamSource);
								} catch (IOException e) {
									throw new RuntimeException("Cannot marshal file");
								}
							})
							.filter(JAXBElement.class::isInstance)
							.map(JAXBElement.class::cast)
							.map(JAXBElement::getValue)
							.filter(MetadatiInvioFileType.class::isInstance)
							.map(MetadatiInvioFileType.class::cast)
							.map(MetadatiInvioFileType::getIdentificativoSdI)
							.map(s -> new BigInteger(s))
							.orElse(fileSdIConMetadatiTypeBulk.getIdentificativoSdI())
			);

	    	DataSource byteArrayDataSource = new UploadedFileDataSource(file);
			getRicezioneFattureService().
				riceviFatturaSIGLA(
						fileSdIConMetadatiTypeBulk.getIdentificativoSdI(),
						file.getName(),
						null,
						new DataHandler(byteArrayDataSource), 
						Optional.ofNullable(fileMetadata).filter(uploadedFile -> Optional.ofNullable(uploadedFile.getFile()).isPresent()).map(UploadedFile::getName).orElse("empty.xml"),
						Optional.ofNullable(fileMetadata).filter(uploadedFile -> Optional.ofNullable(uploadedFile.getFile()).isPresent()).map(
								uploadedFile -> {
									return new DataHandler(new UploadedFileDataSource(uploadedFile));
								}
						).orElse(new DataHandler(new EmptyDataSource("empty.xml", "text/xml")))
				);
			caricaPassivaElettronicaBP.setMessage("File caricato correttamente");
			fileSdIConMetadatiTypeBulk.setIdentificativoSdI(null);
		} catch (FillException e) {
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

	class EmptyDataSource implements DataSource {
		private final String name;
		private final String contentType;

		public EmptyDataSource(String name, String contentType) {
			this.name = name;
			this.contentType = contentType;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return new ByteArrayOutputStream();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream("".getBytes());
		}

		@Override
		public String getContentType() {
			return contentType;
		}

	}

	private RicezioneFatturePA getRicezioneFattureService() {
		return Optional.ofNullable(EJBCommonServices.createEJB("RicezioneFatture!it.cnr.contab.docamm00.ejb.RicezioneFatturePA"))
				.filter(RicezioneFatturePA.class::isInstance)
				.map(RicezioneFatturePA.class::cast)
				.orElseThrow(() -> new DetailedRuntimeException("cannot find ejb RicezioneFatture!it.cnr.contab.docamm00.ejb.RicezioneFatturePA"));
	}
	
}