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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.EJBException;
import javax.mail.AuthenticationFailedException;
import javax.mail.PasswordAuthentication;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.gov.fatturapa.sdi.messaggi.v1.EsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.RiferimentoFatturaType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.FileSdIType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.ObjectFactory;
public class DocumentoEleTestataHome extends BulkHome {
	private transient final static Logger logger = LoggerFactory.getLogger(DocumentoEleTestataHome.class);

	private static final long serialVersionUID = 1L;
	public DocumentoEleTestataHome(Connection conn) {
		super(DocumentoEleTestataBulk.class, conn);
	}
	public DocumentoEleTestataHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleTestataBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectOptionsByClause(String s, OggettoBulk oggettobulk, BulkHome bulkhome, OggettoBulk oggettobulk1, CompoundFindClause compoundfindclause)
			throws InvocationTargetException, IllegalAccessException,
			PersistencyException {
		DocumentoEleTestataBulk testata = (DocumentoEleTestataBulk)oggettobulk;
		java.sql.Timestamp dataRic = testata.getDocumentoEleTrasmissione().getDataRicezione();
		java.util.GregorianCalendar dataRicGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar
				.getInstance();
		dataRicGregorian.setTime(dataRic== null ? new Date() : dataRic);		
		if (s.equalsIgnoreCase("documentoEleTrasmissione.prestatore"))
			return selectTerzoForCFIVA(testata, (TerzoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getPrestatoreCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getPrestatoreCodice());
		else if (s.equalsIgnoreCase("documentoEleTrasmissione.prestatoreAnag"))
			return selectAnagraficoForCFIVA(testata, (AnagraficoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getPrestatoreCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getPrestatoreCodice());
		if (s.equalsIgnoreCase("documentoEleTrasmissione.intermediario"))
			return selectTerzoForCFIVA(testata, (TerzoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getIntermediarioCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getIntermediarioCodice());
		else if (s.equalsIgnoreCase("documentoEleTrasmissione.intermediarioAnag"))
			return selectAnagraficoForCFIVA(testata, (AnagraficoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getIntermediarioCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getIntermediarioCodice());
		if (s.equalsIgnoreCase("documentoEleTrasmissione.rappresentante"))
			return selectTerzoForCFIVA(testata, (TerzoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getRappresentanteCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getRappresentanteCodice());
		else if (s.equalsIgnoreCase("documentoEleTrasmissione.rappresentanteAnag"))
			return selectAnagraficoForCFIVA(testata, (AnagraficoBulk)oggettobulk1, compoundfindclause, 
					testata.getDocumentoEleTrasmissione().getRappresentanteCodicefiscale(), 
					testata.getDocumentoEleTrasmissione().getRappresentanteCodice());
		else if (s.equalsIgnoreCase("documentoEleTrasmissione.unitaCompetenza")){
			SQLBuilder sql = getHomeCache().getHome(Unita_organizzativaBulk.class, "V_UNITA_ORGANIZZATIVA_VALIDA").selectByClause(compoundfindclause);
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, dataRicGregorian.get(java.util.GregorianCalendar.YEAR));
			return sql;
		}else
			return bulkhome.selectByClause(compoundfindclause);
	}

	public SQLBuilder selectAnagraficoForCFIVA(DocumentoEleTestataBulk testata, 
			AnagraficoBulk anagrafico, CompoundFindClause clauses, String codiceFiscale, String partitaIVA) throws PersistencyException {
		AnagraficoHome anagraficoHome = (AnagraficoHome)getHomeCache().getHome(AnagraficoBulk.class);
    	SQLBuilder sql = anagraficoHome.selectByClause(clauses);
        
    	sql.openParenthesis(FindClause.AND);
    	sql.addClause(FindClause.OR, "dt_fine_rapporto", SQLBuilder.ISNULL, null);
    	sql.addClause(FindClause.OR, "dt_fine_rapporto", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause(FindClause.AND, "partita_iva", SQLBuilder.EQUALS, partitaIVA);
		if (codiceFiscale != null){
			sql.openParenthesis(FindClause.OR);
			sql.addSQLClause(FindClause.OR, "codice_fiscale", SQLBuilder.EQUALS, codiceFiscale);
			sql.addClause("AND", "ti_entita_giuridica", SQLBuilder.NOT_EQUALS, AnagraficoBulk.GRUPPO_IVA);

			AssGruppoIvaAnagHome assGruppoIvaAnagHome = (AssGruppoIvaAnagHome)getHomeCache().getHome(AssGruppoIvaAnagBulk.class);
			SQLBuilder sqlExists = assGruppoIvaAnagHome.createSQLBuilder();
			sqlExists.addSQLJoin("ASS_GRUPPO_IVA_ANAG.CD_ANAG", "ANAGRAFICO.CD_ANAG");
			sql.addSQLNotExistsClause("AND",sqlExists);
			sql.closeParenthesis();
		}
		sql.closeParenthesis();
        return sql;
    }	

	public SQLBuilder selectTerzoForCFIVA(DocumentoEleTestataBulk testata, 
    		TerzoBulk terzo, CompoundFindClause clauses, String codiceFiscale, String partitaIVA) throws PersistencyException {
    	TerzoHome terzoHome = (TerzoHome)getHomeCache().getHome(TerzoBulk.class);
    	SQLBuilder sql = terzoHome.selectByClause(clauses);

    	sql.openParenthesis(FindClause.AND);
    	sql.addClause(FindClause.OR, "dt_fine_rapporto", SQLBuilder.ISNULL, null);
    	sql.addClause(FindClause.OR, "dt_fine_rapporto", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.AND);
		if (codiceFiscale != null){
			sql.addSQLClause(FindClause.AND, "CODICE_FISCALE_ANAGRAFICO", SQLBuilder.EQUALS, codiceFiscale);
			AnagraficoHome anagraficoHome = (AnagraficoHome)getHomeCache().getHome(AnagraficoBulk.class);
			SQLBuilder sqlExists = anagraficoHome.createSQLBuilder();
			sqlExists.addSQLJoin("V_TERZO_CF_PI.CD_ANAG", "ANAGRAFICO.CD_ANAG");
			sqlExists.addSQLClause(FindClause.AND, "PARTITA_IVA", SQLBuilder.EQUALS, partitaIVA);
			sqlExists.addClause("AND", "ti_entita_giuridica", SQLBuilder.NOT_EQUALS, AnagraficoBulk.GRUPPO_IVA);
			sql.addSQLExistsClause("OR",sqlExists);
		} else {
				sql.addSQLClause(FindClause.OR, "PARTITA_IVA_ANAGRAFICO", SQLBuilder.EQUALS, partitaIVA);
		}
		sql.closeParenthesis();
        return sql;
    }	
	
	public PasswordAuthentication getAuthenticatorPecSdi(UserContext userContext) throws ComponentException {
		Configurazione_cnrBulk email;
		try {
			email = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, new Integer(0),null,Configurazione_cnrBulk.PK_EMAIL_PEC, Configurazione_cnrBulk.SK_SDI);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (EJBException e) {
			throw new ApplicationException(e);
		}

		if (email != null)
			try {
				String password = StringEncrypter.decrypt(email.getVal01(), email.getVal02());
				return new PasswordAuthentication(email.getVal01(), password);	
			} catch (EncryptionException e1) {
				new AuthenticationFailedException("Cannot decrypt password");
			}
		throw new ApplicationException("Confiurazione PEC non trovata, contattare il servizio di HelpDesk!");
	}
	public NotificaEsitoCommittenteType createNotificaEsitoCommittente(DocumentoEleTestataBulk documentoEleTestataBulk) {
    	it.gov.fatturapa.sdi.messaggi.v1.ObjectFactory objMessaggi = new it.gov.fatturapa.sdi.messaggi.v1.ObjectFactory();        	
    	NotificaEsitoCommittenteType notificaEsitoCommittenteType = objMessaggi.createNotificaEsitoCommittenteType();
    	notificaEsitoCommittenteType.setIdentificativoSdI(documentoEleTestataBulk.getIdentificativoSdi().toString());
    	notificaEsitoCommittenteType.setVersione("1.0");
    	RiferimentoFatturaType riferimentoFatturaType = objMessaggi.createRiferimentoFatturaType();
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(documentoEleTestataBulk.getDataDocumento().getTime());
    	riferimentoFatturaType.setAnnoFattura(BigInteger.valueOf(cal.get(Calendar.YEAR)));
    	riferimentoFatturaType.setNumeroFattura(documentoEleTestataBulk.getNumeroDocumento());     
    	riferimentoFatturaType.setPosizioneFattura(BigInteger.valueOf(documentoEleTestataBulk.getProgressivo() + 1));   	
    	notificaEsitoCommittenteType.setRiferimentoFattura(riferimentoFatturaType);
    	notificaEsitoCommittenteType.setEsito(documentoEleTestataBulk.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?EsitoCommittenteType.EC_02: EsitoCommittenteType.EC_01);
    	notificaEsitoCommittenteType.setMessageIdCommittente(documentoEleTestataBulk.getStatoDocumentoEle().name());        	
    	notificaEsitoCommittenteType.setDescrizione(documentoEleTestataBulk.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?
    			documentoEleTestataBulk.getMotivoRifiuto():documentoEleTestataBulk.getStatoDocumentoEle().name());
    	return notificaEsitoCommittenteType;
	}

	public void storeEsitoDocument(DocumentoEleTestataBulk documentoEleTestata,  ByteArrayInputStream byteArrayInputStream, String aspect) throws ApplicationException {
		StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "D:sigla_fatture_attachment:document");
		metadataProperties.put(StoragePropertyNames.NAME.value(), documentoEleTestata.getNomeFile("EC"));
		metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
				Arrays.asList("P:sigla_commons_aspect:utente_applicativo_sigla", aspect));
		metadataProperties.put("sigla_commons_aspect:utente_applicativo", "SDI");
		try {
			storeService.storeSimpleDocument(byteArrayInputStream, "text/xml",
					storeService.getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef()).getPath(),
					metadataProperties);
		} catch(StorageException _ex) {
			logger.error("storeEsitoDocument", _ex);
		}
	}

	public void notificaEsito(UserContext userContext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk) throws ApplicationException, IOException {
		if (documentoEleTestataBulk.isRicevutaDecorrenzaTermini()) {
			logger.info("Notifica esito per identificativo SDI:"+documentoEleTestataBulk.getIdentificativoSdi()+" non inviata per avvenuta decorrenza termini!");
			return;
		}
    	FatturazioneElettronicaClient client = SpringUtil.getBean("fatturazioneElettronicaClient", 
    			FatturazioneElettronicaClient.class);
    	FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean("fatturaPassivaElettronicaService", 
    			FatturaPassivaElettronicaService.class);

    	NotificaEsitoCommittenteType notificaEsitoCommittenteType = 
    			this.createNotificaEsitoCommittente(documentoEleTestataBulk);
    	if (tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC)) {
    		try {
            	it.gov.fatturapa.sdi.messaggi.v1.ObjectFactory objMessaggi = new it.gov.fatturapa.sdi.messaggi.v1.ObjectFactory();        	
            	JAXBElement<NotificaEsitoCommittenteType> notificaEsitoCommittente = objMessaggi.
            			createNotificaEsitoCommittente(notificaEsitoCommittenteType);
            	ByteArrayOutputStream outputStreamNotificaEsito = new ByteArrayOutputStream();
            	client.getMarshaller().marshal(notificaEsitoCommittente, new StreamResult(outputStreamNotificaEsito));
            	PasswordAuthentication authentication = getAuthenticatorPecSdi(userContext);
            	if (authentication == null) {
        			logger.error("Errore applicativo durante la Notifica di Esito Committente, contattare il servizio di HelpDesk!");
            		throw new ApplicationException("Errore applicativo durante la Notifica di Esito Committente, contattare il servizio di HelpDesk!");
            	}
				/*
            	storeEsitoDocument(documentoEleTestataBulk, new ByteArrayInputStream(outputStreamNotificaEsito.toByteArray()), 
            			documentoEleTestataBulk.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?
								StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO.value():
								StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO.value());

				 */
            	fatturaService.notificaEsito(authentication.getUserName(), authentication.getPassword(), 
            			documentoEleTestataBulk, notificaEsitoCommittente);
    		} catch(ApplicationException _ex) {
        		throw _ex;
    		} catch(Exception _ex) {
    			logger.error("Errore applicativo durante la Notifica di Esito Committente, contattare il servizio di HelpDesk!" + _ex.getMessage(), _ex.getCause());
    			throw new ApplicationException("Errore applicativo durante la Notifica di Esito Committente, contattare il servizio di HelpDesk!\n" + _ex.getMessage(), _ex.getCause());
    		}
    	} else if (!tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC)) {
        	ObjectFactory obj = new ObjectFactory();
        	FileSdIType fileSdIType = obj.createFileSdIType();
        	fileSdIType.setIdentificativoSdI(BigInteger.valueOf(documentoEleTestataBulk.getIdentificativoSdi()));
        	fileSdIType.setNomeFile(documentoEleTestataBulk.getNomeFile("EC"));
        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();        	
        	client.getMarshaller().marshal(notificaEsitoCommittenteType, new StreamResult(outputStream));
        	ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        	DataSource ds = new UploadedFileDataSource(inputStream);
        	fileSdIType.setFile(new DataHandler(ds));
        	JAXBElement<FileSdIType> notificaEsito = obj.createNotificaEsito(fileSdIType);
        	ByteArrayOutputStream outputStreamNotificaEsito = new ByteArrayOutputStream();
        	client.getMarshaller().marshal(notificaEsito, new StreamResult(outputStreamNotificaEsito));
        	storeEsitoDocument(documentoEleTestataBulk, new ByteArrayInputStream(outputStreamNotificaEsito.toByteArray()), 
        			documentoEleTestataBulk.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?
							StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO.value():
							StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO.value());
    	}
	}
	public SQLBuilder selectFattureAggiornateComplete(UserContext userContext, DocumentoEleTestataBulk documentoEleTestataBulk, CompoundFindClause clauses) throws PersistencyException {
		if(clauses == null){
			if(documentoEleTestataBulk != null)
				clauses = documentoEleTestataBulk.buildFindClauses(null);
		}else{
			clauses = CompoundFindClause.and(clauses, documentoEleTestataBulk.buildFindClauses(Boolean.FALSE));
		}
		SQLBuilder sql = getHomeCache().getHome(DocumentoEleTestataBulk.class, "V_DOCUMENTO_ELE").selectByClause(userContext, clauses);
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "statoDocumento", SQLBuilder.EQUALS, StatoDocumentoEleEnum.AGGIORNATO.name());
		sql.addClause(FindClause.OR, "statoDocumento", SQLBuilder.EQUALS, StatoDocumentoEleEnum.COMPLETO.name());
		sql.closeParenthesis();
		if (CNRUserContext.getCd_unita_organizzativa(userContext) != null) {
			Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class)
					.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
			boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
			if (!isUoEnte) {
				sql.openParenthesis(FindClause.AND);
				sql.addSQLClause(FindClause.AND, "V_DOCUMENTO_ELE.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
				sql.addSQLClause(FindClause.OR, "V_DOCUMENTO_ELE.CD_UNITA_COMPETENZA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
				sql.closeParenthesis();
			}
		}
		return sql;
	}
	class UploadedFileDataSource implements DataSource {
		
		private InputStream inputStream;
		
		public UploadedFileDataSource(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		@Override
		public OutputStream getOutputStream() throws IOException {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
 			IOUtils.copy(inputStream, output);
			return output;
		}
		
		@Override
		public String getName() {
			return "DATASOURCE";
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return inputStream;
		}
		
		@Override
		public String getContentType() {
			return "www/unknow";
		}
	}
}