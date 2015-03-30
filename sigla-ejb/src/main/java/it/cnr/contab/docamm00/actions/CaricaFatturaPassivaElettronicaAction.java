package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.CaricaFatturaElettronicaBP;
import it.cnr.contab.docamm00.ejb.RicezioneFatturePA;
import it.cnr.contab.docamm00.fatturapa.bulk.FileSdIConMetadatiTypeBulk;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.FormAction;
import it.cnr.jada.util.upload.UploadedFile;
import it.gov.fatturapa.sdi.messaggi.v1.MetadatiInvioFileType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.FileSdIConMetadatiType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;

public class CaricaFatturaPassivaElettronicaAction extends FormAction {
	private static final long serialVersionUID = 1L;

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
					fileSdIConMetadati.getValue().getNomeFile(), 
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
	    	SpringUtil.getBean("ricezioneFattureService", RicezioneFatturePA.class).
				riceviFatturaSIGLA(fileSdIConMetadatiTypeBulk.getIdentificativoSdI(), file.getName(), 
						new DataHandler(new ByteArrayDataSource(
								IOUtils.toByteArray(new FileInputStream(file.getFile())),
								file.getContentType())), 
								fileMetadata.getName(), 
								new DataHandler(new ByteArrayDataSource(
								IOUtils.toByteArray(new FileInputStream(fileMetadata.getFile())), 
								fileMetadata.getContentType())));
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
}
