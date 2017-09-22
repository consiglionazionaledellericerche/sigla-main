package it.cnr.contab.docamm00.service;

import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.spring.storage.bulk.StorageFile;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.spring.storage.StorageException;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.jada.DetailedException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenerazioneReportException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentiCollegatiDocAmmService extends StoreService {

	private final static String TIPO_ALLEGATO_NON_INVIATO_SDI = "allegati_non_inviati_sdi";
	private final static String TIPO_ALLEGATO_FATTURA_DOPO_PROTOCOLLO = "stampa_fattura_dopo_protocollo";
	private final static String TIPO_ALLEGATO_FATTURA_PRIMA_PROTOCOLLO = "stampa_fattura_prima_protocollo";
	private final static String FILE_FATTURA_XML_FIRMATO = "fattura_elettronica_xml_post_firma";
	public List<String> getNodeRefDocumentoAttivo(Fattura_attivaBulk fattura)throws DetailedException{
		return getNodeRefDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}

	public List<String> getNodeRefDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		List<StorageObject> results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_DOPO_PROTOCOLLO);
		if (results.size() == 0){
			results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_PRIMA_PROTOCOLLO);
			if (results.size() == 0){
				return null;
			} else{
				return results.stream()
						.map(StorageObject::getKey)
						.collect(Collectors.toList());
			}
		} else {
			return results.stream()
					.map(StorageObject::getKey)
					.collect(Collectors.toList());
		}
	}

	private List<String> getNodeRefFatturaAttivaXmlFirmato(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		List<StorageObject> results = getDocuments(folder, FILE_FATTURA_XML_FIRMATO);
		if (results.size() == 0)
			return null;
		else {
			return results.stream()
					.map(StorageObject::getKey)
					.collect(Collectors.toList());
		}
	}

	public List<String> getNodeRefAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		List<StorageObject> results = getDocuments(folder, TIPO_ALLEGATO_NON_INVIATO_SDI);
		if (results.size() == 0){
			return null;
		} else {
			return results.stream()
					.map(StorageObject::getKey)
					.collect(Collectors.toList());
		}
	}

	private List<StorageObject> getDocuments(String folder, String tipoAllegato) throws ApplicationException {
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" join sigla_fatture_attachment:"+tipoAllegato+" allegati on doc.cmis:objectId = allegati.cmis:objectId");
		query.append(" where IN_FOLDER(doc, '").append(folder).append("')");
		List<StorageObject> results = search(query.toString());
		return results;
	}

	private String getFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo,
											Long pgFattura) throws DetailedException, ApplicationException {
		return getNodeFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura).getKey();
	}

	private StorageObject getNodeFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws DetailedException, ApplicationException {
		StorageObject storageObject = recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
		if (storageObject == null){
			throw new ApplicationException("Non esistono documenti collegati alla fattura.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+" numero:"+pgFattura);
		}
		return storageObject;
	}

	public StorageObject recuperoFolderFattura(Integer esercizio, String cds, String cdUo, Long pgFattura) throws DetailedException {
		int posizionePunto = cdUo.indexOf(".");
		StringBuffer query = new StringBuffer("select fat.cmis:objectId from sigla_fatture:fatture_attive fat join strorg:uo uo on fat.cmis:objectId = uo.cmis:objectId ");
		query.append(" join strorg:cds cds on fat.cmis:objectId = cds.cmis:objectId ");
		query.append(" where fat.sigla_fatture:esercizio = ").append(esercizio);
		query.append(" and fat.sigla_fatture:pg_fattura = ").append(pgFattura);
		query.append(" and uo.strorguo:codice like '").append(cdUo.substring(0, posizionePunto)+"%").append("'");
		query.append(" and cds.strorgcds:codice = '").append(cds).append("'");
		//		query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		//		query.append(" order by doc.cmis:creationDate DESC");
		List<StorageObject> resultsFolder = search(query.toString());
		if (resultsFolder.size() == 0)
			return null;
		else if (resultsFolder.size() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' fatture.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+" numero:"+pgFattura);
		} else {
			return resultsFolder.get(0);
		}
	}

	public InputStream getStreamDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
		List<String> ids = getNodeRefDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		return getStream(ids);
	}

	public InputStream getStreamAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
		List<String> ids = getNodeRefAllegatiDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		return getStream(ids);
	}

	private InputStream getStream(List<String> ids)
			throws ApplicationException, COSVisitorException, IOException {
		if (ids != null){
			if (ids.size() == 1){
				try{
					return getResource(ids.get(0));
				}catch (StorageException _ex){
				}
			}else{
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.setDestinationStream(new ByteArrayOutputStream());
				try {
					for (String id : ids) {
						ut.addSource(getResource(id));
					}
					ut.mergeDocuments();
					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
				} catch (COSVisitorException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}catch (StorageException _ex){
				}
			}
		}
		return null;
	}

	public InputStream getStreamXmlFirmatoFatturaAttiva(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
		List<String> ids = getNodeRefFatturaAttivaXmlFirmato(esercizio, cds, cdUo, pgFattura);
		return getStream(ids);
	}

	public InputStream getStreamDocumento(Fattura_attivaBulk fattura) throws Exception{
		return getStreamDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}

	public InputStream getStreamAllegatiDocumento(Fattura_attivaBulk fattura) throws Exception{
		return getStreamAllegatiDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}

	private void archiviaFileCMIS(UserContext userContext, DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService,
								  Fattura_attivaBulk fattura, File file) throws ComponentException {
		List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
		List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
		try {
			StorageFile storageFile = new StorageFileFatturaAttiva(file, fattura,
					"application/pdf","FAPP" + fattura.constructCMISNomeFile() + ".pdf");
			String path = storageFile.getStorageParentPath();
			try{
				Optional.ofNullable(documentiCollegatiDocAmmService.restoreSimpleDocument(
						storageFile,
						storageFile.getInputStream(),
						storageFile.getContentType(),
						storageFile.getFileName(),
						path,
						true
				)).ifPresent(storageObject -> {
					List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
					aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_STAMPA_FATTURA_PRIMA_PROTOCOLLO.value());
					documentiCollegatiDocAmmService.updateProperties(
							Collections.singletonMap(
									StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
									aspects),
							storageObject);
					storageFile.setStorageObject(storageObject);
					storageFileCreate.add(storageFile);
				});
			} catch (StorageException _ex) {
				if (_ex.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
					throw new ApplicationException("CMIS - File ["+ storageFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
				throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + _ex.getMessage() + ")");
			}
		} catch (Exception e){
			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			for (StorageFile storageFile : storageFileCreate)
				documentiCollegatiDocAmmService.delete(storageFile.getStorageObject());
			for (StorageFile storageFile : storageFileAnnullati) {
				String cmisFileName = storageFile.getFileName();
				String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".")+1);
				String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
				storageFile.setFileName(cmisFileName.replace(stringToDelete, "."+cmisFileEstensione));
				documentiCollegatiDocAmmService.updateProperties(storageFile, storageFile.getStorageObject());
			}
			throw new ApplicationException(e.getMessage());
		}
	}
	public void gestioneAllegatiPerFatturazioneElettronica(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException {
		if (fattura.isDocumentoFatturazioneElettronica()){
			DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService",DocumentiCollegatiDocAmmService.class);
			File file = lanciaStampaFatturaElettronica(userContext, fattura);
			archiviaFileCMIS(userContext, documentiCollegatiDocAmmService, fattura, file);
		}
	}

	public File lanciaStampaFatturaElettronica(
			UserContext userContext,
			Fattura_attivaBulk fattura) throws ComponentException {
		try {
			String nomeProgrammaStampa = "fattura_attiva_provvisoria.jasper";
			String nomeFileStampaFattura = getOutputFileNameFatturazioneElettronica(nomeProgrammaStampa, fattura);
			File output = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", File.separator + nomeFileStampaFattura);
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setFlEmail(false);
			print.setReport("/docamm/docamm/"+ nomeProgrammaStampa);
			print.setNomeFile(nomeFileStampaFattura);
			print.setUtcr(userContext.getUser());
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.addParam("esercizio",fattura.getEsercizio(), Integer.class);
			print.addParam("cd_uo_origine",fattura.getCd_uo_origine(), String.class);
			print.addParam("pg_fattura",fattura.getPg_fattura_attiva(), Long.class);
			Report report = SpringUtil.getBean("printService",PrintService.class).executeReport(userContext,print);

			FileOutputStream f = new FileOutputStream(output);
			f.write(report.getBytes());
			return output;
		} catch (IOException e) {
			throw new GenerazioneReportException("Generazione Stampa non riuscita",e);
		}
	}

	private String getOutputFileNameFatturazioneElettronica(String reportName, Fattura_attivaBulk fattura) {
		String fileName = preparaFileNamePerStampa(reportName);
		fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + fattura.recuperoIdFatturaAsString() + '_' + fileName;
		return fileName;
	}
	private static final DateFormat PDF_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private String preparaFileNamePerStampa(String reportName) {
		String fileName = reportName;
		fileName = fileName.replace('/', '_');
		fileName = fileName.replace('\\', '_');
		if(fileName.startsWith("_"))
			fileName = fileName.substring(1);
		if(fileName.endsWith(".jasper"))
			fileName = fileName.substring(0, fileName.length() - 7);
		fileName = fileName + ".pdf";
		return fileName;
	}



}