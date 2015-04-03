package it.cnr.contab.docamm00.comp;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.docamm00.cmis.CMISFolderFatturaPassiva;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAcquistoBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleDdtBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleIvaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleLineaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleScontoMaggBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTributiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.StatoDocumentoEleEnum;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoAcquistoEnum;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.firma.Verifica;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.gov.fatturapa.EsitoRicezioneType;
import it.gov.fatturapa.FileSdIConMetadatiType;
import it.gov.fatturapa.FileSdIType;
import it.gov.fatturapa.RispostaRiceviFattureType;
import it.gov.fatturapa.sdi.fatturapa.v1.AllegatiType;
import it.gov.fatturapa.sdi.fatturapa.v1.CedentePrestatoreType;
import it.gov.fatturapa.sdi.fatturapa.v1.CessionarioCommittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiBolloType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiCassaPrevidenzialeType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDocumentiCorrelatiType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiGeneraliDocumentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiRiepilogoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasportoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DettaglioLineeType;
import it.gov.fatturapa.sdi.fatturapa.v1.DettaglioPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;
import it.gov.fatturapa.sdi.fatturapa.v1.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.IdFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.RappresentanteFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.ScontoMaggiorazioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.TerzoIntermediarioSoggettoEmittenteType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.jboss.wsf.spi.annotation.WebContext;
@Stateless
@WebService(endpointInterface="it.gov.fatturapa.RicezioneFatture", 
			name="RicezioneFatture",targetNamespace="http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebContext(contextRoot="/fatturesdi")
public class RicezioneFatture implements it.gov.fatturapa.RicezioneFatture, it.cnr.contab.docamm00.ejb.RicezioneFatturePA {
	private static final Logger LOGGER = Logger.getLogger(RicezioneFatture.class);
	
	@SuppressWarnings("unchecked")	
	public RispostaRiceviFattureType riceviFatture(FileSdIConMetadatiType parametersIn) {
		RispostaRiceviFattureType risposta = new RispostaRiceviFattureType();
		try {
			JAXBContext jc = JAXBContext.newInstance("it.gov.fatturapa.sdi.fatturapa.v1");
			byte[] bytesMetadata = IOUtils.toByteArray(parametersIn.getMetadati().getInputStream());
			if (Base64.isArrayByteBase64(bytesMetadata))
				bytesMetadata = Base64.decodeBase64(bytesMetadata);

			boolean isp7m = parametersIn.getFile().getContentType().toLowerCase().endsWith("p7m");
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			if (isp7m)
				bStream = estraiFirma(parametersIn.getFile().getInputStream(), jc);
			else
				IOUtils.copy(parametersIn.getFile().getInputStream(), bStream);
			
			CMISPath cmisPath = saveFattura(
					isp7m,
					parametersIn.getNomeFile(), 
					parametersIn.getFile().getInputStream(), 
					parametersIn.getFile().getContentType(),

					isp7m?parametersIn.getNomeFile().substring(0, parametersIn.getNomeFile().lastIndexOf(".")):parametersIn.getNomeFile(), 
					new ByteArrayInputStream(bStream.toByteArray()), 
					"application/xml",
					
					parametersIn.getNomeFileMetadati(), 
					new ByteArrayInputStream(bytesMetadata), 
					parametersIn.getMetadati().getContentType(),
					parametersIn.getIdentificativoSdI());
			
			JAXBElement<FatturaElettronicaType> fatturaElettronicaType = (JAXBElement<FatturaElettronicaType>) 
					jc.createUnmarshaller().unmarshal(new ByteArrayInputStream(bStream.toByteArray()));
			elaboraFattura(fatturaElettronicaType.getValue(), parametersIn.getIdentificativoSdI(), parametersIn.getNomeFile(), cmisPath);		
			risposta.setEsito(EsitoRicezioneType.ER_01);
		} catch (Exception e) {
			LOGGER.error("Errore nel WS della ricezione delle fatture!", e);
			java.io.StringWriter sw = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(sw));
			SendMail.sendErrorMail("Errore nel WS della ricezione delle fatture!Identificativo:"+parametersIn.getIdentificativoSdI(), sw.toString());
			throw new SOAPFaultException(generaFault(e.getMessage(), e));
		}
		return risposta;
	}

	private ByteArrayOutputStream estraiFirma(InputStream is, JAXBContext jc) throws CMSException, IOException {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		byte[] inputBytes = IOUtils.toByteArray(is);
		try {			
			Verifica.verificaBustaFirmata(new ByteArrayInputStream(inputBytes), bStream);
			jc.createUnmarshaller().unmarshal(new ByteArrayInputStream(bStream.toByteArray()));
			return bStream;
		} catch(Exception _ex) {
			try {
				if (Base64.isArrayByteBase64(inputBytes))
					inputBytes = Base64.decodeBase64(inputBytes);					
			} catch(ArrayIndexOutOfBoundsException e) {			
			}
			CMSSignedData sdp = new CMSSignedData(inputBytes);
			CMSProcessable cmsp = sdp.getSignedContent();
			cmsp.write(bStream);
			return bStream;			
		}
	}
		
	private CMISPath saveFattura(boolean isp7m, String name, InputStream stream, String contentTypeFile,
			String nameMinusP7m, InputStream streamMinusP7m, String contentTypeFileMinusP7m,			
			String nomeFileMedatati, InputStream streamMetadati,  String contentTypeMetadata, 
			BigInteger identificativoSdI) throws ApplicationException {
		SiglaCMISService cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);		
		CMISPath cmisPath = SpringUtil.getBean("cmisPathFatturePassive",CMISPath.class);
		Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR)), 
				month = String.valueOf(now.get(Calendar.MONTH) + 1),
				day = String.valueOf(now.get(Calendar.DAY_OF_MONTH)),
				folderName = identificativoSdI + " - " + name.substring(0, name.indexOf("."));
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, year, year, year);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, month, month, month);		
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, day, day, day);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, folderName, null, null, 
				new CMISFolderFatturaPassiva(null, identificativoSdI));
		try {
			Map<String, Object> metadataPropertiesMinusP7M = new HashMap<String, Object>();
			metadataPropertiesMinusP7M.put(PropertyIds.OBJECT_TYPE_ID, "D:sigla_fatture_attachment:document");
			metadataPropertiesMinusP7M.put(PropertyIds.NAME, nameMinusP7m);
			metadataPropertiesMinusP7M.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, 
					Arrays.asList("P:sigla_commons_aspect:utente_applicativo_sigla", "P:sigla_fatture_attachment:trasmissione_fattura"));
			metadataPropertiesMinusP7M.put("sigla_commons_aspect:utente_applicativo", "SDI");
			cmisService.storeSimpleDocument(streamMinusP7m, contentTypeFileMinusP7m, cmisPath, metadataPropertiesMinusP7M);			
		} catch(CmisContentAlreadyExistsException _ex){
			LOGGER.warn("PEC File "+nameMinusP7m+" alredy store!");
		}
		try {
			Map<String, Object> metadataProperties = new HashMap<String, Object>();
			metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, "D:sigla_fatture_attachment:document");
			metadataProperties.put(PropertyIds.NAME, nomeFileMedatati);
			metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, 
					Arrays.asList("P:sigla_commons_aspect:utente_applicativo_sigla"));
			metadataProperties.put("sigla_commons_aspect:utente_applicativo", "SDI");
			cmisService.storeSimpleDocument(streamMetadati, contentTypeMetadata, cmisPath, metadataProperties);
		} catch(CmisContentAlreadyExistsException _ex){
			LOGGER.warn("PEC File "+nomeFileMedatati+" alredy store!");
		}
		
		if (isp7m) {
			try {
				Map<String, Object> fileProperties = new HashMap<String, Object>();
				fileProperties.put(PropertyIds.OBJECT_TYPE_ID, "D:sigla_fatture_attachment:document");
				fileProperties.put(PropertyIds.NAME, name);
				fileProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, 
						Arrays.asList("P:sigla_commons_aspect:utente_applicativo_sigla", "P:sigla_fatture_attachment:fattura_elettronica_xml_post_firma", CMISAspect.CNR_SIGNEDDOCUMENT.value()));
				fileProperties.put("sigla_commons_aspect:utente_applicativo", "SDI");
				cmisService.storeSimpleDocument(stream, contentTypeFile, cmisPath, fileProperties);
			} catch(CmisContentAlreadyExistsException _ex){
				LOGGER.warn("PEC File "+name+" alredy store!");
			}				
		}
		return cmisPath;
	}
	
	private void elaboraFattura(FatturaElettronicaType fatturaElettronicaType, BigInteger identificativoSdI, String nomeFile, CMISPath cmisPath) throws ApplicationException {
		FatturaElettronicaPassivaComponentSession component = 
				(FatturaElettronicaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession");
    	UserContext userContext = new WSUserContext("SDI",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
		
		DatiTrasmissioneType datiTrasmissione = fatturaElettronicaType.getFatturaElettronicaHeader().getDatiTrasmissione();
		IdFiscaleType idTrasmittente = datiTrasmissione.getIdTrasmittente();
		CessionarioCommittenteType committente = fatturaElettronicaType.getFatturaElettronicaHeader().getCessionarioCommittente();
		CedentePrestatoreType cedentePrestatore = fatturaElettronicaType.getFatturaElettronicaHeader().getCedentePrestatore();
		RappresentanteFiscaleType rappresentante = fatturaElettronicaType.getFatturaElettronicaHeader().getRappresentanteFiscale();
		TerzoIntermediarioSoggettoEmittenteType intermediario = fatturaElettronicaType.getFatturaElettronicaHeader().
				getTerzoIntermediarioOSoggettoEmittente();
		DocumentoEleTrasmissioneBulk docTrasmissione = new DocumentoEleTrasmissioneBulk(
				idTrasmittente.getIdPaese(), idTrasmittente.getIdCodice(),identificativoSdI.longValue());
		docTrasmissione.setProgressivoInvio(datiTrasmissione.getProgressivoInvio());
		if (datiTrasmissione.getFormatoTrasmissione() != null)
			docTrasmissione.setFormatoTrasmissione(datiTrasmissione.getFormatoTrasmissione().value());
		else
			docTrasmissione.setFormatoTrasmissione(FormatoTrasmissioneType.SDI_11.value());
		docTrasmissione.setCodiceDestinatario(datiTrasmissione.getCodiceDestinatario());
		docTrasmissione.setCodiceUnivocoSdi(identificativoSdI.longValue());
		docTrasmissione.setDataRicezione(EJBCommonServices.getServerTimestamp());
		docTrasmissione.setNomeFile(nomeFile);
		SiglaCMISService cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);
		docTrasmissione.setCmisNodeRef(cmisService.getNodeByPath(cmisPath).getId());
		if (fatturaElettronicaType.getFatturaElettronicaHeader().getSoggettoEmittente() != null)
			docTrasmissione.setSoggettoEmittente(fatturaElettronicaType.getFatturaElettronicaHeader().getSoggettoEmittente().name());
		if (committente != null) {
			if (committente.getDatiAnagrafici() != null) {
				docTrasmissione.setCommittenteCodicefiscale(committente.getDatiAnagrafici().getCodiceFiscale());
				if (committente.getDatiAnagrafici().getIdFiscaleIVA() != null) {
					docTrasmissione.setCommittenteCodice(committente.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
					docTrasmissione.setCommittentePaese(committente.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese());
				}
				if (committente.getDatiAnagrafici().getAnagrafica() != null) {					
					docTrasmissione.setCommittenteCognome(committente.getDatiAnagrafici().getAnagrafica().getCognome());
					docTrasmissione.setCommittenteNome(committente.getDatiAnagrafici().getAnagrafica().getNome());
					docTrasmissione.setCommittenteDenominazione(committente.getDatiAnagrafici().getAnagrafica().getDenominazione());
					docTrasmissione.setCommittenteTitolo(committente.getDatiAnagrafici().getAnagrafica().getTitolo());
					docTrasmissione.setCommittenteCodeori(committente.getDatiAnagrafici().getAnagrafica().getCodEORI());
				}
			}
			if (committente.getSede() != null) {
				docTrasmissione.setCommittenteCap(committente.getSede().getCAP());
				docTrasmissione.setCommittenteComune(committente.getSede().getComune());
				docTrasmissione.setCommittenteIndirizzo(committente.getSede().getIndirizzo());
				docTrasmissione.setCommittenteNazione(committente.getSede().getNazione());
				docTrasmissione.setCommittenteNumerocivico(committente.getSede().getNumeroCivico());
				docTrasmissione.setCommittenteProvincia(committente.getSede().getProvincia());				
			}			
		}
		if (intermediario != null) {
			if (intermediario.getDatiAnagrafici() != null) {
				docTrasmissione.setIntermediarioCodicefiscale(intermediario.getDatiAnagrafici().getCodiceFiscale());
				if (intermediario.getDatiAnagrafici().getIdFiscaleIVA() != null) {
					docTrasmissione.setIntermediarioCodice(intermediario.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
					docTrasmissione.setIntermediarioPaese(intermediario.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese());
				}
				if (intermediario.getDatiAnagrafici().getAnagrafica() != null) {
					docTrasmissione.setIntermediarioCodeori(intermediario.getDatiAnagrafici().getAnagrafica().getCodEORI());					
					docTrasmissione.setIntermediarioCognome(intermediario.getDatiAnagrafici().getAnagrafica().getCognome());
					docTrasmissione.setIntermediarioDenominazione(intermediario.getDatiAnagrafici().getAnagrafica().getDenominazione());
					docTrasmissione.setIntermediarioNome(intermediario.getDatiAnagrafici().getAnagrafica().getNome());
					docTrasmissione.setIntermediarioTitolo(intermediario.getDatiAnagrafici().getAnagrafica().getTitolo());								
				}				
			}
		}
		if (cedentePrestatore != null) {
			docTrasmissione.setRiferimentoAmministrazione(cedentePrestatore.getRiferimentoAmministrazione());
			if (cedentePrestatore.getDatiAnagrafici() != null) {
				if (cedentePrestatore.getDatiAnagrafici().getDataIscrizioneAlbo() != null)
					docTrasmissione.setDataiscrizionealbo(convert(cedentePrestatore.getDatiAnagrafici().
						getDataIscrizioneAlbo()));
				docTrasmissione.setAlboprofessionale(cedentePrestatore.getDatiAnagrafici().getAlboProfessionale());
				docTrasmissione.setProvinciaalbo(cedentePrestatore.getDatiAnagrafici().getProvinciaAlbo());
				docTrasmissione.setNumeroalbo(cedentePrestatore.getDatiAnagrafici().getNumeroIscrizioneAlbo());
				docTrasmissione.setPrestatoreCodicefiscale(cedentePrestatore.getDatiAnagrafici().getCodiceFiscale());				
				if (cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA() != null) {
					docTrasmissione.setPrestatoreCodice(cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
					docTrasmissione.setPrestatorePaese(cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese());
				}
				if (cedentePrestatore.getDatiAnagrafici().getAnagrafica() != null) {					
					docTrasmissione.setPrestatoreCognome(cedentePrestatore.getDatiAnagrafici().getAnagrafica().getCognome());
					docTrasmissione.setPrestatoreNome(cedentePrestatore.getDatiAnagrafici().getAnagrafica().getNome());
					docTrasmissione.setPrestatoreDenominazione(cedentePrestatore.getDatiAnagrafici().getAnagrafica().getDenominazione());
					docTrasmissione.setPrestatoreTitolo(cedentePrestatore.getDatiAnagrafici().getAnagrafica().getTitolo());
					docTrasmissione.setPrestatoreCodeori(cedentePrestatore.getDatiAnagrafici().getAnagrafica().getCodEORI());
				}
				if (cedentePrestatore.getDatiAnagrafici().getRegimeFiscale() != null) {
					docTrasmissione.setRegimefiscale(cedentePrestatore.getDatiAnagrafici().getRegimeFiscale().name());
				}
			}
			if (cedentePrestatore.getSede() != null) {
				docTrasmissione.setPrestatoreCap(cedentePrestatore.getSede().getCAP());
				docTrasmissione.setPrestatoreComune(cedentePrestatore.getSede().getComune());
				docTrasmissione.setPrestatoreIndirizzo(cedentePrestatore.getSede().getIndirizzo());
				docTrasmissione.setPrestatoreNazione(cedentePrestatore.getSede().getNazione());
				docTrasmissione.setPrestatoreNumerocivico(cedentePrestatore.getSede().getNumeroCivico());
				docTrasmissione.setPrestatoreProvincia(cedentePrestatore.getSede().getProvincia());				
			}

			if (cedentePrestatore.getStabileOrganizzazione() != null) {
				docTrasmissione.setStabileorgCap(cedentePrestatore.getStabileOrganizzazione().getCAP());
				docTrasmissione.setStabileorgComune(cedentePrestatore.getStabileOrganizzazione().getComune());
				docTrasmissione.setStabileorgIndirizzo(cedentePrestatore.getStabileOrganizzazione().getIndirizzo());
				docTrasmissione.setStabileorgNazione(cedentePrestatore.getStabileOrganizzazione().getNazione());
				docTrasmissione.setStabileorgNumerocivico(cedentePrestatore.getStabileOrganizzazione().getNumeroCivico());
				docTrasmissione.setStabileorgProvincia(cedentePrestatore.getStabileOrganizzazione().getProvincia());
			}
			
			if (cedentePrestatore.getContatti() != null) {
				docTrasmissione.setPrestatoreEmail(cedentePrestatore.getContatti().getEmail());
				docTrasmissione.setPrestatoreFax(cedentePrestatore.getContatti().getFax());
				docTrasmissione.setPrestatoreTelefono(cedentePrestatore.getContatti().getTelefono());
			}
			if (cedentePrestatore.getIscrizioneREA() != null) {
				docTrasmissione.setReaCapitalesociale(cedentePrestatore.getIscrizioneREA().getCapitaleSociale());
				docTrasmissione.setReaNumerorea(cedentePrestatore.getIscrizioneREA().getNumeroREA());
				if (cedentePrestatore.getIscrizioneREA().getSocioUnico() != null)
					docTrasmissione.setReaSociounico(cedentePrestatore.getIscrizioneREA().getSocioUnico().name());
				if (cedentePrestatore.getIscrizioneREA().getStatoLiquidazione() != null)
					docTrasmissione.setReaStatoliquidazione(cedentePrestatore.getIscrizioneREA().getStatoLiquidazione().name());
				docTrasmissione.setReaUfficio(cedentePrestatore.getIscrizioneREA().getUfficio());
			}
		}
		if (rappresentante != null) {
			if (rappresentante.getDatiAnagrafici() != null) {
				docTrasmissione.setRappresentanteCodicefiscale(rappresentante.getDatiAnagrafici().getCodiceFiscale());
				if (rappresentante.getDatiAnagrafici().getAnagrafica() != null) {
					docTrasmissione.setRappresentanteCodeori(rappresentante.getDatiAnagrafici().getAnagrafica().getCodEORI());
					docTrasmissione.setRappresentanteCognome(rappresentante.getDatiAnagrafici().getAnagrafica().getCognome());
					docTrasmissione.setRappresentanteDenominazione(rappresentante.getDatiAnagrafici().getAnagrafica().getDenominazione());
					docTrasmissione.setRappresentanteNome(rappresentante.getDatiAnagrafici().getAnagrafica().getNome());					
					docTrasmissione.setRappresentanteTitolo(rappresentante.getDatiAnagrafici().getAnagrafica().getTitolo());
				}
				if (rappresentante.getDatiAnagrafici().getIdFiscaleIVA() != null) {
					docTrasmissione.setRappresentanteCodice(rappresentante.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
					docTrasmissione.setRappresentantePaese(rappresentante.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese());
				}				
			}
		}
		docTrasmissione.setTrasmittenteEmail(docTrasmissione.getTrasmittenteEmail());
		docTrasmissione.setTrasmittenteTelefono(docTrasmissione.getTrasmittenteTelefono());		
		docTrasmissione.setToBeCreated();

		for (int i = 0; i < fatturaElettronicaType.getFatturaElettronicaBody().size(); i++) {
			FatturaElettronicaBodyType fatturaElettronicaBody = fatturaElettronicaType.getFatturaElettronicaBody().get(i);
			DocumentoEleTestataBulk docTestata = new DocumentoEleTestataBulk(
					idTrasmittente.getIdPaese(), idTrasmittente.getIdCodice(),identificativoSdI.longValue(), (long)i);	
			if (fatturaElettronicaBody.getDatiGenerali() != null) {
				if (fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento() != null) {
					docTestata.setTipoDocumento(
							fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getTipoDocumento().value());
					docTestata.setDivisa(fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getDivisa());
					docTestata.setDataDocumento(
							convert(fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getData()));
					docTestata.setNumeroDocumento(fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getNumero());
					if (fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo() != null) {
						DatiBolloType datiBollo = fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo();
						docTestata.setBolloVirtuale(datiBollo.getBolloVirtuale().value());
						docTestata.setImportoBollo(truncBigDecimal(datiBollo.getImportoBollo()));
					}
					docTestata.setImportoDocumento(truncBigDecimal(fatturaElettronicaBody.getDatiGenerali().
							getDatiGeneraliDocumento().getImportoTotaleDocumento()));
					docTestata.setArrotondamento(truncBigDecimal(fatturaElettronicaBody.getDatiGenerali().
							getDatiGeneraliDocumento().getArrotondamento()));
					docTestata.setCausale(StringUtils.join(fatturaElettronicaBody.getDatiGenerali().
							getDatiGeneraliDocumento().getCausale().toArray(),","));
					if (fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getArt73() != null)
						docTestata.setArt73(fatturaElettronicaBody.getDatiGenerali().
							getDatiGeneraliDocumento().getArt73().value());					
				}
				if (fatturaElettronicaBody.getDatiGenerali().getDatiTrasporto() != null) {
					DatiTrasportoType datiTrasporto = fatturaElettronicaBody.getDatiGenerali().getDatiTrasporto();
					if (datiTrasporto.getDatiAnagraficiVettore() != null){
						docTestata.setVettoreCodicefiscale(datiTrasporto.getDatiAnagraficiVettore().getCodiceFiscale());
						docTestata.setVettoreNumerolicenza(datiTrasporto.getDatiAnagraficiVettore().getNumeroLicenzaGuida());
						if (datiTrasporto.getDatiAnagraficiVettore().getAnagrafica() != null) {
							docTestata.setVettoreDenominazione(datiTrasporto.getDatiAnagraficiVettore().getAnagrafica().getDenominazione());
							docTestata.setVettoreNome(datiTrasporto.getDatiAnagraficiVettore().getAnagrafica().getNome());
							docTestata.setVettoreCognome(datiTrasporto.getDatiAnagraficiVettore().getAnagrafica().getCognome());
							docTestata.setVettoreTitolo(datiTrasporto.getDatiAnagraficiVettore().getAnagrafica().getTitolo());
							docTestata.setVettoreCodeori(datiTrasporto.getDatiAnagraficiVettore().getAnagrafica().getCodEORI());
						}
						if (datiTrasporto.getDatiAnagraficiVettore().getIdFiscaleIVA() != null) {
							docTestata.setVettorePaese(datiTrasporto.getDatiAnagraficiVettore().getIdFiscaleIVA().getIdPaese());
							docTestata.setVettoreCodice(datiTrasporto.getDatiAnagraficiVettore().getIdFiscaleIVA().getIdCodice());
						}
					}
					docTestata.setMezzoTrasporto(datiTrasporto.getMezzoTrasporto());
					docTestata.setCausaleTrasporto(datiTrasporto.getCausaleTrasporto());
					docTestata.setNumeroColli(datiTrasporto.getNumeroColli());
					docTestata.setDescrizioneTrasporto(datiTrasporto.getDescrizione());
					docTestata.setUnitaMisurapeso(datiTrasporto.getUnitaMisuraPeso());
					docTestata.setPesoLordo(truncBigDecimal(datiTrasporto.getPesoLordo()));
					docTestata.setPesoNetto(truncBigDecimal(datiTrasporto.getPesoNetto()));
					docTestata.setDataoraRitiro(convert(datiTrasporto.getDataOraRitiro()));
					docTestata.setDatainizioTrasporto(convert(datiTrasporto.getDataInizioTrasporto()));
					docTestata.setTipoResa(datiTrasporto.getTipoResa());
					if (datiTrasporto.getIndirizzoResa() != null) {
						docTestata.setResaIndirizzo(datiTrasporto.getIndirizzoResa().getIndirizzo());
						docTestata.setResaNumerocivico(datiTrasporto.getIndirizzoResa().getNumeroCivico());
						docTestata.setResaCap(datiTrasporto.getIndirizzoResa().getCAP());
						docTestata.setResaComune(datiTrasporto.getIndirizzoResa().getComune());
						docTestata.setResaProvincia(datiTrasporto.getIndirizzoResa().getProvincia());
						docTestata.setResaNazione(datiTrasporto.getIndirizzoResa().getNazione());						
					}
					docTestata.setDataoraConsegna(convert(datiTrasporto.getDataOraConsegna()));
				}
				if (fatturaElettronicaBody.getDatiGenerali().getFatturaPrincipale() != null) {
					docTestata.setNumeroFatturaprincipale(fatturaElettronicaBody.getDatiGenerali().
							getFatturaPrincipale().getNumeroFatturaPrincipale());
					docTestata.setDataFatturaprincipale(convert(fatturaElettronicaBody.getDatiGenerali().
							getFatturaPrincipale().getDataFatturaPrincipale()));
				}
				if (fatturaElettronicaBody.getDatiVeicoli() != null) {
					docTestata.setTotalePercorsoveicolo(fatturaElettronicaBody.getDatiVeicoli().getTotalePercorso());
					docTestata.setDataImmatricolazioneveicolo(convert(fatturaElettronicaBody.getDatiVeicoli().getData()));
				}
				if (fatturaElettronicaBody.getDatiPagamento() != null && !fatturaElettronicaBody.getDatiPagamento().isEmpty()) {
					if (fatturaElettronicaBody.getDatiPagamento().size() > 1) {
						docTestata.setAnomalie("Sono presenti piu di un dettaglio di Pagamento!");
					} else {
						DatiPagamentoType datiPagamento = fatturaElettronicaBody.getDatiPagamento().get(0);
						docTestata.setCodicePagamento(datiPagamento.getCondizioniPagamento().value());
						if (datiPagamento.getDettaglioPagamento() != null && !datiPagamento.getDettaglioPagamento().isEmpty()) {
							if (datiPagamento.getDettaglioPagamento().size() > 1) {
								docTestata.setAnomalie("Sono presenti piu di un dettaglio di Pagamento!");
							} else {
								DettaglioPagamentoType dettaglioPagamento = datiPagamento.getDettaglioPagamento().get(0);
								docTestata.setBeneficiarioPagamento(dettaglioPagamento.getBeneficiario());
								docTestata.setBeneficiarioModPag(dettaglioPagamento.getModalitaPagamento().value());
								docTestata.setDataterminiPagamento(convert(dettaglioPagamento.getDataRiferimentoTerminiPagamento()));
								docTestata.setGiorniterminiPagamento(dettaglioPagamento.getGiorniTerminiPagamento());
								docTestata.setDatascadenzaPagamento(convert(dettaglioPagamento.getDataScadenzaPagamento()));
								docTestata.setImportoPagamento(truncBigDecimal(dettaglioPagamento.getImportoPagamento()));
								docTestata.setCodufficiopostale(dettaglioPagamento.getCodUfficioPostale());
								docTestata.setCognomeQuietanzante(dettaglioPagamento.getCognomeQuietanzante());
								docTestata.setNomeQuietanzante(dettaglioPagamento.getNomeQuietanzante());
								docTestata.setIstitutoFinanziario(dettaglioPagamento.getIstitutoFinanziario());
								docTestata.setIban(dettaglioPagamento.getIBAN());
								docTestata.setAbi(dettaglioPagamento.getABI());
								docTestata.setCab(dettaglioPagamento.getCAB());
								docTestata.setBic(dettaglioPagamento.getBIC());
								docTestata.setScontoPagamentoAnt(truncBigDecimal(dettaglioPagamento.getScontoPagamentoAnticipato()));
								docTestata.setDatalimitePagamentoAnt(convert(dettaglioPagamento.getDataLimitePagamentoAnticipato()));
								docTestata.setPenalitaPagRitardati(truncBigDecimal(dettaglioPagamento.getPenalitaPagamentiRitardati()));
								docTestata.setDataRicorrenzapenale(convert(dettaglioPagamento.getDataDecorrenzaPenale()));
							}
						}						
					}
				}
			}
			docTestata.setStatoDocumento(StatoDocumentoEleEnum.INIZIALE.name());
			docTestata.setToBeCreated();
			docTrasmissione.addToDocEleTestataColl(docTestata);
			if (fatturaElettronicaBody.getDatiBeniServizi() != null) {
				if (fatturaElettronicaBody.getDatiBeniServizi().getDettaglioLinee() != null) {
					for (DettaglioLineeType dettaglioLinea : fatturaElettronicaBody.getDatiBeniServizi().getDettaglioLinee()) {
						DocumentoEleLineaBulk docEleLinea = new DocumentoEleLineaBulk(idTrasmittente.getIdPaese(), 
								idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, dettaglioLinea.getNumeroLinea());
						if (dettaglioLinea.getTipoCessionePrestazione() != null)
							docEleLinea.setTipoCessione(dettaglioLinea.getTipoCessionePrestazione().value());
						List<String> anomalie = new ArrayList<String>();
						if (dettaglioLinea.getCodiceArticolo() != null && !dettaglioLinea.getCodiceArticolo().isEmpty()){
							if (dettaglioLinea.getCodiceArticolo().size() > 1) {
								anomalie.add("Sono presenti piu di un dettaglio di Articolo!");
							} else {
								docEleLinea.setArticoloTipo(dettaglioLinea.getCodiceArticolo().get(0).getCodiceTipo());
								docEleLinea.setArticoloValore(dettaglioLinea.getCodiceArticolo().get(0).getCodiceValore());
							}
						}
						docEleLinea.setLineaDescrizione(dettaglioLinea.getDescrizione());
						docEleLinea.setLineaQuantita(truncBigDecimal(dettaglioLinea.getQuantita()));
						docEleLinea.setLineaUnitamisura(dettaglioLinea.getUnitaMisura());
						docEleLinea.setInizioDatacompetenza(convert(dettaglioLinea.getDataInizioPeriodo()));
						docEleLinea.setFineDatacompetenza(convert(dettaglioLinea.getDataFinePeriodo()));
						docEleLinea.setLineaPrezzounitario(truncBigDecimal(dettaglioLinea.getPrezzoUnitario()));
						if (dettaglioLinea.getScontoMaggiorazione() != null && !dettaglioLinea.getScontoMaggiorazione().isEmpty()) {
							if (dettaglioLinea.getScontoMaggiorazione().size() == 1) {
								docEleLinea.setTipoScontomag(dettaglioLinea.getScontoMaggiorazione().get(0).getTipo().value());
								docEleLinea.setPercentualeScontomag(truncBigDecimal(dettaglioLinea.getScontoMaggiorazione().get(0).getPercentuale()));
								docEleLinea.setImportoScontomag(truncBigDecimal(dettaglioLinea.getScontoMaggiorazione().get(0).getImporto()));								
							} else {
								BigDecimal scontoMaggiorazioneImporto = BigDecimal.ZERO;
								for (ScontoMaggiorazioneType scontoMaggiorazione : dettaglioLinea.getScontoMaggiorazione()) {
									scontoMaggiorazioneImporto = scontoMaggiorazioneImporto.add(scontoMaggiorazione.getImporto());
								}
								docEleLinea.setImportoScontomag(truncBigDecimal(scontoMaggiorazioneImporto));								
							}
						}
						docEleLinea.setLineaPrezzototale(truncBigDecimal(dettaglioLinea.getPrezzoTotale()));
						docEleLinea.setLineaAliquotaiva(truncBigDecimal(dettaglioLinea.getAliquotaIVA()));
						if (dettaglioLinea.getRitenuta() != null)
							docEleLinea.setLineaRitenuta(dettaglioLinea.getRitenuta().value());
						if (dettaglioLinea.getNatura() != null)
							docEleLinea.setLineaNatura(dettaglioLinea.getNatura().value());
						docEleLinea.setLineaRiferimentoamm(dettaglioLinea.getRiferimentoAmministrazione());
						if (dettaglioLinea.getAltriDatiGestionali() != null && !dettaglioLinea.getAltriDatiGestionali().isEmpty()) {
							if (dettaglioLinea.getAltriDatiGestionali().size() > 1) {
								anomalie.add("Sono presenti piu di un dettaglio di Dati Gestionali!");
							} else {
								docEleLinea.setTipoDato(dettaglioLinea.getAltriDatiGestionali().get(0).getTipoDato());
								docEleLinea.setRiferimentoTesto(dettaglioLinea.getAltriDatiGestionali().get(0).getRiferimentoTesto());
								docEleLinea.setRiferimentoNumero(dettaglioLinea.getAltriDatiGestionali().get(0).getRiferimentoNumero());
								docEleLinea.setRiferimentodata(convert(dettaglioLinea.getAltriDatiGestionali().get(0).getRiferimentoData()));								
							}							
						}
						if (!anomalie.isEmpty())
							docEleLinea.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
						docEleLinea.setToBeCreated();
						docTestata.addToDocEleLineaColl(docEleLinea);
					}					
				}
				if (fatturaElettronicaBody.getDatiBeniServizi().getDatiRiepilogo() != null && 
						!fatturaElettronicaBody.getDatiBeniServizi().getDatiRiepilogo().isEmpty()) {
					List<String> anomalie = new ArrayList<String>();
					int indexIva = 0;
					for (DatiRiepilogoType datiRiepilogo : fatturaElettronicaBody.getDatiBeniServizi().getDatiRiepilogo()) {
						DocumentoEleIvaBulk docEleIVA = new DocumentoEleIvaBulk(idTrasmittente.getIdPaese(), 
								idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, (long)++indexIva);
						docEleIVA.setAliquotaIva(datiRiepilogo.getAliquotaIVA());
						if (datiRiepilogo.getNatura() != null)
							docEleIVA.setNatura(datiRiepilogo.getNatura().value());
						docEleIVA.setSpeseAccessorie(truncBigDecimal(datiRiepilogo.getSpeseAccessorie()));
						docEleIVA.setArrotondamento(truncBigDecimal(datiRiepilogo.getArrotondamento()));
						docEleIVA.setImponibileImporto(truncBigDecimal(datiRiepilogo.getImponibileImporto()));
						docEleIVA.setImposta(datiRiepilogo.getImposta());
						if (datiRiepilogo.getEsigibilitaIVA() != null)
							docEleIVA.setEsigibilitaIva(datiRiepilogo.getEsigibilitaIVA().value());
						docEleIVA.setRiferimentoNormativo(datiRiepilogo.getRiferimentoNormativo());
						if (!anomalie.isEmpty())
							docEleIVA.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
						docEleIVA.setToBeCreated();
						docTestata.addToDocEleIVAColl(docEleIVA);												
					}
				}
			}
			/**
			 * Carico gli Allegati
			 */
			if (fatturaElettronicaBody.getAllegati() != null && 
					!fatturaElettronicaBody.getAllegati().isEmpty()) {
				for (AllegatiType allegato : fatturaElettronicaBody.getAllegati()) {
					List<String> anomalie = new ArrayList<String>();
					DocumentoEleAllegatiBulk docAllegato = new DocumentoEleAllegatiBulk(idTrasmittente.getIdPaese(), 
							idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, allegato.getNomeAttachment());
					docAllegato.setAlgoritmoCompressione(allegato.getAlgoritmoCompressione());
					docAllegato.setFormatoAttachment(allegato.getFormatoAttachment());
					docAllegato.setDescrizioneAttachment(allegato.getDescrizioneAttachment());
					try {
						Map<String, Object> fileProperties = new HashMap<String, Object>();
						fileProperties.put(PropertyIds.OBJECT_TYPE_ID, "D:sigla_fatture_attachment:document");
						fileProperties.put(PropertyIds.NAME, allegato.getNomeAttachment());
						fileProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, 
								Arrays.asList("P:sigla_commons_aspect:utente_applicativo_sigla"));
						fileProperties.put("sigla_commons_aspect:utente_applicativo", "SDI");
						byte[] bytes = allegato.getAttachment();
						try {
							if (Base64.isArrayByteBase64(bytes))
								bytes = Base64.decodeBase64(bytes);					
						} catch(ArrayIndexOutOfBoundsException _ex) {			
						}
						Document document = SpringUtil.getBean("cmisService", SiglaCMISService.class).
								storeSimpleDocument(
										new ByteArrayInputStream(bytes), 
										"application/" + allegato.getFormatoAttachment(), cmisPath, fileProperties);
						docAllegato.setCmisNodeRef(document.getId());												
					} catch(Exception  _ex) {
						anomalie.add("Errore nel salvataggio dell'allegato sul documentale! Identificativo:"+identificativoSdI + " " + _ex.getMessage());
						LOGGER.error("Errore nel salvataggio dell'allegato sul documentale! Identificativo:"+identificativoSdI, _ex);
						java.io.StringWriter sw = new java.io.StringWriter();
						_ex.printStackTrace(new java.io.PrintWriter(sw));
						SendMail.sendErrorMail("Errore nel salvataggio dell'allegato sul documentale!Identificativo:"+identificativoSdI, sw.toString());
					}
					if (!anomalie.isEmpty())
						docAllegato.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
					docAllegato.setToBeCreated();
					docTestata.addToDocEleAllegatiColl(docAllegato);												
				}
			}
			/**
			 * Carico le Ritenute/Contributi
			 */
			if (fatturaElettronicaBody.getDatiGenerali() != null && 
					fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento() != null) {
				DatiGeneraliDocumentoType datiGeneraliDocumento = fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento();
				int indexTributo = 1;
				if (datiGeneraliDocumento.getDatiRitenuta() != null) {
					List<String> anomalie = new ArrayList<String>();
					DocumentoEleTributiBulk docTributo = new DocumentoEleTributiBulk(idTrasmittente.getIdPaese(), 
							idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, (long)indexTributo);
					docTributo.setTipoRiga("RIT");
					if (datiGeneraliDocumento.getDatiRitenuta().getTipoRitenuta() != null)
						docTributo.setTipoTributo(datiGeneraliDocumento.getDatiRitenuta().getTipoRitenuta().value());
					docTributo.setImporto(truncBigDecimal(datiGeneraliDocumento.getDatiRitenuta().getImportoRitenuta()));
					docTributo.setAliquota(truncBigDecimal(datiGeneraliDocumento.getDatiRitenuta().getAliquotaRitenuta()));
					if (datiGeneraliDocumento.getDatiRitenuta().getCausalePagamento() != null)
						docTributo.setCausalePagamento(datiGeneraliDocumento.getDatiRitenuta().getCausalePagamento().value());
					if (!anomalie.isEmpty())
						docTributo.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
					docTributo.setToBeCreated();
					docTestata.addToDocEleTributiColl(docTributo);												
				}
				if (datiGeneraliDocumento.getDatiCassaPrevidenziale() != null && 
						!datiGeneraliDocumento.getDatiCassaPrevidenziale().isEmpty()) {
					for (DatiCassaPrevidenzialeType datiCassaPrevidenziale : datiGeneraliDocumento.getDatiCassaPrevidenziale()) {
						List<String> anomalie = new ArrayList<String>();
						indexTributo++;
						DocumentoEleTributiBulk docTributo = new DocumentoEleTributiBulk(idTrasmittente.getIdPaese(), 
								idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, (long)indexTributo);							
						docTributo.setTipoRiga("CAS");
						if (datiCassaPrevidenziale.getTipoCassa() != null)
							docTributo.setTipoTributo(datiCassaPrevidenziale.getTipoCassa().value());
						docTributo.setImporto(truncBigDecimal(datiCassaPrevidenziale.getImportoContributoCassa()));
						docTributo.setAliquota(truncBigDecimal(datiCassaPrevidenziale.getAlCassa()));
						docTributo.setImponibileCassa(truncBigDecimal(datiCassaPrevidenziale.getImponibileCassa()));
						docTributo.setAliquotaIva(truncBigDecimal(datiCassaPrevidenziale.getAliquotaIVA()));
						if (datiCassaPrevidenziale.getRitenuta() != null)
							docTributo.setRitenutaCassa(datiCassaPrevidenziale.getRitenuta().value());
						if (datiCassaPrevidenziale.getNatura() != null)
							docTributo.setNatura(datiCassaPrevidenziale.getNatura().value());
						docTributo.setRiferimentoAmministrazione(datiCassaPrevidenziale.getRiferimentoAmministrazione());
						if (!anomalie.isEmpty())
							docTributo.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
						docTributo.setToBeCreated();
						docTestata.addToDocEleTributiColl(docTributo);																			
					}
				}
			}
			/**
			 * Carico lo sconto
			 */				
			if (fatturaElettronicaBody.getDatiGenerali() != null && 
					fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento() != null) {
				if (fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getScontoMaggiorazione() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getScontoMaggiorazione().isEmpty()){
					int indexSconto = 0;
					for (ScontoMaggiorazioneType scontoMaggiorazione : fatturaElettronicaBody.getDatiGenerali().getDatiGeneraliDocumento().getScontoMaggiorazione()) {
						indexSconto++;
						List<String> anomalie = new ArrayList<String>();
						DocumentoEleScontoMaggBulk docSconto = new DocumentoEleScontoMaggBulk(idTrasmittente.getIdPaese(), 
								idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, (long)indexSconto);
						if (scontoMaggiorazione.getTipo() != null)
							docSconto.setTipoScontomagg(scontoMaggiorazione.getTipo().value());
						docSconto.setPercentualeScontomagg(truncBigDecimal(scontoMaggiorazione.getPercentuale()));
						docSconto.setImportoScontomagg(truncBigDecimal(scontoMaggiorazione.getImporto()));
						if (!anomalie.isEmpty())
							docSconto.setAnomalie(StringUtils.join(anomalie.toArray()," - "));
						docSconto.setToBeCreated();
						docTestata.addToDocEleScontoMaggColl(docSconto);																			
					}
				}
			}
			/**
			 * Carico l'ordine di acquisto
			 */
			List<DocumentoEleAcquistoBulk> acquisti = new ArrayList<DocumentoEleAcquistoBulk>();
			if (fatturaElettronicaBody.getDatiGenerali() != null) {
				if (fatturaElettronicaBody.getDatiGenerali().getDatiOrdineAcquisto() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiOrdineAcquisto().isEmpty()){
					for (DatiDocumentiCorrelatiType datiOrdineAcquisto : fatturaElettronicaBody.getDatiGenerali().getDatiOrdineAcquisto()) {
						acquisti.addAll(caricaAcquisti(datiOrdineAcquisto, TipoAcquistoEnum.Ordine.name(), idTrasmittente, datiTrasmissione, (long)i, identificativoSdI.longValue()));
					}
				}
				if (fatturaElettronicaBody.getDatiGenerali().getDatiContratto() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiContratto().isEmpty()){
					for (DatiDocumentiCorrelatiType datiOrdineAcquisto : fatturaElettronicaBody.getDatiGenerali().getDatiContratto()) {
						acquisti.addAll(caricaAcquisti(datiOrdineAcquisto, TipoAcquistoEnum.Contratto.name(), idTrasmittente, datiTrasmissione, (long)i, identificativoSdI.longValue()));
					}
				}
				if (fatturaElettronicaBody.getDatiGenerali().getDatiConvenzione() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiConvenzione().isEmpty()){
					for (DatiDocumentiCorrelatiType datiOrdineAcquisto : fatturaElettronicaBody.getDatiGenerali().getDatiConvenzione()) {
						acquisti.addAll(caricaAcquisti(datiOrdineAcquisto, TipoAcquistoEnum.Convenzione.name(), idTrasmittente, datiTrasmissione, (long)i, identificativoSdI.longValue()));
					}
				}
				if (fatturaElettronicaBody.getDatiGenerali().getDatiRicezione() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiRicezione().isEmpty()){
					for (DatiDocumentiCorrelatiType datiOrdineAcquisto : fatturaElettronicaBody.getDatiGenerali().getDatiRicezione()) {
						acquisti.addAll(caricaAcquisti(datiOrdineAcquisto, TipoAcquistoEnum.Ricezione.name(), idTrasmittente, datiTrasmissione, (long)i, identificativoSdI.longValue()));
					}
				}
				if (fatturaElettronicaBody.getDatiGenerali().getDatiFattureCollegate() != null &&
						!fatturaElettronicaBody.getDatiGenerali().getDatiFattureCollegate().isEmpty()){
					for (DatiDocumentiCorrelatiType datiOrdineAcquisto : fatturaElettronicaBody.getDatiGenerali().getDatiFattureCollegate()) {
						acquisti.addAll(caricaAcquisti(datiOrdineAcquisto, TipoAcquistoEnum.Fatture_Collegate.name(), idTrasmittente, datiTrasmissione, (long)i, identificativoSdI.longValue()));
					}
				}
				int progressivoAcquisto = 0;
				for (DocumentoEleAcquistoBulk docAcquisto : acquisti) {
					docAcquisto.setProgressivoAcquisto(new Long (progressivoAcquisto++));
					docAcquisto.setToBeCreated();
					docTestata.addToDocEleAcquistoColl(docAcquisto);
				}
			}
			/**
			 * Carico il documento di trasporto
			 */
			if (fatturaElettronicaBody.getDatiGenerali() != null) {
				if (fatturaElettronicaBody.getDatiGenerali().getDatiDDT() != null && 
						!fatturaElettronicaBody.getDatiGenerali().getDatiDDT().isEmpty() ) {
					int progressivoDdt = 0;
					for (DatiDDTType datiDDTT : fatturaElettronicaBody.getDatiGenerali().getDatiDDT()) {
						progressivoDdt++;
						for (Integer numeroLinea : datiDDTT.getRiferimentoNumeroLinea()) {
							DocumentoEleDdtBulk docDDT = new DocumentoEleDdtBulk(idTrasmittente.getIdPaese(), 
									idTrasmittente.getIdCodice(), identificativoSdI.longValue(), (long)i, (long)progressivoDdt);
							docDDT.setNumeroLinea(numeroLinea);
							docDDT.setDdtNumero(datiDDTT.getNumeroDDT());
							docDDT.setDdtData(convert(datiDDTT.getDataDDT()));
						}					
					}
				}
			}
		} //END FOR LOOP Fattura Elettronica Body
		try {
			docTrasmissione = (DocumentoEleTrasmissioneBulk) component.creaDocumento(userContext, docTrasmissione);
			component.completaDocumento(userContext, docTrasmissione);
		} catch (Exception e ) {
			LOGGER.error("Errore nel WS della ricezione delle fatture!Identificativo:"+identificativoSdI, e);
			java.io.StringWriter sw = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(sw));
			SendMail.sendErrorMail("Errore nel WS della ricezione delle fatture!Identificativo:"+identificativoSdI, sw.toString());
		}	
	}
	
	private List<DocumentoEleAcquistoBulk> caricaAcquisti(DatiDocumentiCorrelatiType datiOrdineAcquisto, 
			String tipo, IdFiscaleType idTrasmittente, DatiTrasmissioneType datiTrasmissione, Long progressivo, Long indentificativoSdi){
		List<DocumentoEleAcquistoBulk> results = new ArrayList<DocumentoEleAcquistoBulk>();
		if (datiOrdineAcquisto.getRiferimentoNumeroLinea() == null || datiOrdineAcquisto.getRiferimentoNumeroLinea().isEmpty()){
			results.add(caricaAcquisto(datiOrdineAcquisto, tipo, idTrasmittente, datiTrasmissione, progressivo, 
					indentificativoSdi, null));
		}
		for (Integer numeroLinea : datiOrdineAcquisto.getRiferimentoNumeroLinea()) {
			results.add(caricaAcquisto(datiOrdineAcquisto, tipo, idTrasmittente, datiTrasmissione, progressivo, 
					indentificativoSdi, numeroLinea.longValue()));
		}
		return results;
	}
	private DocumentoEleAcquistoBulk caricaAcquisto(DatiDocumentiCorrelatiType datiOrdineAcquisto, 
			String tipo, IdFiscaleType idTrasmittente, DatiTrasmissioneType datiTrasmissione, Long progressivo, Long indentificativoSdi, Long numeroLinea){
		DocumentoEleAcquistoBulk docAcquisto = new DocumentoEleAcquistoBulk(idTrasmittente.getIdPaese(), 
				idTrasmittente.getIdCodice(),indentificativoSdi, progressivo, (long)0);
		docAcquisto.setTipoRifacquisto(tipo);
		if (numeroLinea != null)
			docAcquisto.setNumeroLinea(numeroLinea.longValue());
		docAcquisto.setAcquistoDocumento(datiOrdineAcquisto.getIdDocumento());
		docAcquisto.setAcquistoData(convert(datiOrdineAcquisto.getData()));
		docAcquisto.setAcquistoNumitem(datiOrdineAcquisto.getNumItem());
		docAcquisto.setAcquistoCommessa(datiOrdineAcquisto.getCodiceCommessaConvenzione());
		docAcquisto.setAcquistoCup(datiOrdineAcquisto.getCodiceCUP());
		docAcquisto.setAcquistoCig(datiOrdineAcquisto.getCodiceCIG());
		return docAcquisto;
	}
	
	private BigDecimal truncBigDecimal(BigDecimal bigDecimal) {
		if (bigDecimal == null)
			return null;
		if (bigDecimal.scale() > 2)
			return bigDecimal.setScale(2, RoundingMode.DOWN);
		return bigDecimal;
	}
	
	private java.sql.Timestamp convert(XMLGregorianCalendar calendar) {
		if (calendar == null)
			return null;
		return new java.sql.Timestamp(calendar.toGregorianCalendar().getTime().getTime());
	}

	private java.sql.Timestamp convert(Calendar calendar) {
		if (calendar == null)
			return null;
		return new java.sql.Timestamp(calendar.getTimeInMillis());
	}

	public void notificaDecorrenzaTermini(BigInteger identificativoSdI, String nomeFile, DataHandler file) {
		FileSdIType parametersNotifica = new FileSdIType();
		parametersNotifica.setIdentificativoSdI(identificativoSdI);
		parametersNotifica.setNomeFile(nomeFile);
		parametersNotifica.setFile(file);
		notificaDecorrenzaTermini(parametersNotifica);
	}
	
	public void notificaDecorrenzaTermini(FileSdIType parametersNotifica) {
		
	}
	
	private SOAPFault generaFault(String stringFault, Exception _ex) {
		try{
			MessageFactory factory = MessageFactory.newInstance();
			SOAPMessage message = factory.createMessage(); 
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			SOAPBody body = message.getSOAPBody(); 
			SOAPFault fault = body.addFault();
			Name faultName = soapFactory.createName("","", SOAPConstants.URI_NS_SOAP_ENVELOPE);
			fault.setFaultCode(faultName);
			fault.setFaultString(stringFault);
			fault.setAttribute("cause", _ex.getClass().getName());
			return fault;
		} catch(SOAPException e) {
			return null;
		}
	}

	public void riceviFatturaSIGLA(BigInteger identificativoSdI,
			String nomeFile, DataHandler file, String nomeFileMetadati,
			DataHandler metadati) throws ComponentException {
		FileSdIConMetadatiType parametersIn = new FileSdIConMetadatiType();
		parametersIn.setIdentificativoSdI(identificativoSdI);
		parametersIn.setNomeFile(nomeFile);
		parametersIn.setFile(file);
		parametersIn.setNomeFileMetadati(nomeFileMetadati);
		parametersIn.setMetadati(metadati);
		try {
			riceviFatture(parametersIn);
		} catch(SOAPFaultException _ex) {
			if (_ex.getFault().getAttribute("cause").equalsIgnoreCase(CmisContentAlreadyExistsException.class.getName()))
				throw new ApplicationException("Fattura già presente!");
			throw _ex;
		}
	}
}