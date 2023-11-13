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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.ContattoBulk;
import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession;
import it.cnr.contab.docamm00.actions.CRUDFatturaPassivaAction;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.*;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormBP;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.RegimeFiscaleType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Stream;

import javax.ejb.EJBException;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.TipoDocumentoType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;

public class CRUDFatturaPassivaElettronicaBP extends AllegatiCRUDBP<AllegatoFatturaBulk, DocumentoEleTestataBulk> implements FatturaPassivaElettronicaBP{
	private static final long serialVersionUID = 1L;
	private TipoIntegrazioneSDI tipoIntegrazioneSDI = TipoIntegrazioneSDI.PEC;
	private final SimpleDetailCRUDController crudDocEleLineaColl = 
			new SimpleDetailCRUDController("RifDocEleLineaColl",DocumentoEleLineaBulk.class,"docEleLineaColl",this);
	private final SimpleDetailCRUDController crudDocEleIVAColl = 
			new SimpleDetailCRUDController("RifDocEleIVAColl",DocumentoEleIvaBulk.class,"docEleIVAColl",this);
	private final SimpleDetailCRUDController crudDocEleAllegatiColl =
			new SimpleDetailCRUDController("RifDocEleAllegatiColl",DocumentoEleAllegatiBulk.class,"docEleAllegatiColl",this);
	private final SimpleDetailCRUDController crudDocEleTributiColl = 
			new SimpleDetailCRUDController("RifDocEleTributiColl",DocumentoEleTributiBulk.class,"docEleTributiColl",this);
	private final SimpleDetailCRUDController crudDocEleScontoMaggColl = 
			new SimpleDetailCRUDController("RifDocEleScontoMaggColl",DocumentoEleScontoMaggBulk.class,"docEleScontoMaggColl",this);
	private final SimpleDetailCRUDController crudDocEleAcquistoColl = 
			new SimpleDetailCRUDController("RifDocEleAcquistoColl",DocumentoEleAcquistoBulk.class,"docEleAcquistoColl",this);
	private final SimpleDetailCRUDController crudDocEleDdtColl = 
			new SimpleDetailCRUDController("RifDocEleDdtColl",DocumentoEleDdtBulk.class,"docEleDdtColl",this);
	private Unita_organizzativaBulk uoScrivania;
	private boolean esercizioAperto;
	private Date dataAttivazioneSplit;
	private Date dataDisattivazioneSplit;
	private Date dataAttivazioneSplitProf;
	private Date dataDisattivazioneSplitProf;
	
	public CRUDFatturaPassivaElettronicaBP() {
		super();
	}

	public CRUDFatturaPassivaElettronicaBP(String s) {
		super(s);
	}

	public boolean isPrintButtonEnabled() {
		return getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null;
	}
	
	public boolean isCompilaButtonEnabled() {
		return isEditable() && getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isCompilabile() &&
				!((DocumentoEleTestataBulk)getModel()).isIrregistrabile() &&
				((DocumentoEleTestataBulk)getModel()).getDocumentoEleTrasmissione().getUnitaOrganizzativa().equalsByPrimaryKey(uoScrivania) &&
				this.isEsercizioAperto();
		
	}

	public boolean isVisualizzaFatturaButtonEnabled() {
		return getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRegistrata();
	}
	
	public boolean isRifiutaButtonEnabled() {
		return isEditable() && getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).isRifiutabile();
	}

	public boolean isRifiutaConPECButtonEnabled() {
		DocumentoEleTestataBulk model = (DocumentoEleTestataBulk)getModel();
		return isEditable() && model != null &&
				model.getIdentificativoSdi() != null &&
				model.isRifiutabile();
	}

	public boolean isCollegaFatturaButtonHidden() {
		DocumentoEleTestataBulk model = (DocumentoEleTestataBulk)getModel();
		return !(isEditable() && model != null &&
				model.getIdentificativoSdi() != null &&
				Optional.ofNullable(model.getTipoDocumento())
						.map(s -> s.equalsIgnoreCase(TipoDocumentoType.TD_04.value())).orElse(Boolean.FALSE) &&
				(model.isRifiutabile() || model.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATA_CON_PEC)));
	}

	public boolean isMostraFatturaCollegataButtonHidden() {
		DocumentoEleTestataBulk model = (DocumentoEleTestataBulk)getModel();
		return !(model != null &&
				model.getIdentificativoSdi() != null &&
				Optional.ofNullable(model.getFatturaCollegata()).isPresent() &&
				Optional.ofNullable(model.getTipoDocumento())
						.map(s -> s.equalsIgnoreCase(TipoDocumentoType.TD_04.value())).orElse(Boolean.FALSE) &&
				model.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.STORNATO));
	}

	public boolean isMostraNotaCollegataButtonHidden() {
		DocumentoEleTestataBulk model = (DocumentoEleTestataBulk)getModel();
		return !(model != null &&
				model.getIdentificativoSdi() != null &&
				Optional.ofNullable(model.getNotaCollegata()).isPresent() &&
				Optional.ofNullable(model.getTipoDocumento())
						.map(s -> !s.equalsIgnoreCase(TipoDocumentoType.TD_04.value())).orElse(Boolean.FALSE) &&
				model.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.STORNATO));
	}

	public boolean isEsitoRifiutatoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRifiutata());
	}

	public boolean isEsitoAccettatoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRegistrata());
	}

	public boolean isReinviaEsitoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				!((DocumentoEleTestataBulk)getModel()).isRicevutaDecorrenzaTermini() &&
				((DocumentoEleTestataBulk)getModel()).getStatoNotificaEsito() != null &&
				((DocumentoEleTestataBulk)getModel()).getStatoNotificaEsito().equalsIgnoreCase(DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI));		
	}
	@Override
	protected Button[] createToolbar() {
		Button[] buttons = super.createToolbar();
		List<Button> toolbar = new ArrayList<Button>();
		toolbar.addAll(Arrays.asList(buttons));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.download"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.downloadFatturaXML"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.downloadFatturaFirmata"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.rifiuta"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.rifiutaConPEC"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.collegaNota"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.mostraFatturaCollegata"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.mostraNotaCollegata"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.accetta"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.visuallizza.fattura"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.rifiutato"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.accettato"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.reinvia"));
		return toolbar.toArray(new Button[toolbar.size()]);
	}
	
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.AGGIORNATO.name());
    	setStatus(SEARCH);
		setUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
		try {
			String integrazioneSDI = Utility.createConfigurazioneCnrComponentSession().
				getVal01(actioncontext.getUserContext(), null, null, 
						Configurazione_cnrBulk.PK_INTEGRAZIONE_SDI, Configurazione_cnrBulk.SK_INTEGRAZIONE_SDI);
			if (integrazioneSDI != null)
				tipoIntegrazioneSDI = TipoIntegrazioneSDI.valueOf(integrazioneSDI); 
			setEsercizioAperto(((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",	EsercizioComponentSession.class)).isEsercizioAperto(actioncontext.getUserContext()));
			dataAttivazioneSplit = Utility.createConfigurazioneCnrComponentSession().getDt01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA);
			dataDisattivazioneSplit = Utility.createConfigurazioneCnrComponentSession().getDt02(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA);
			dataAttivazioneSplitProf = Utility.createConfigurazioneCnrComponentSession().getDt01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA_PROF);
			dataDisattivazioneSplitProf = Utility.createConfigurazioneCnrComponentSession().getDt02(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA_PROF);
			
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (EJBException e) {
			throw handleException(e);
		}		
	}

	private void setUoScrivania(Unita_organizzativaBulk uoScrivania) {
		this.uoScrivania = uoScrivania;
	}

	public boolean isUoEnte(){
		return (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
    }

    public void writeFormFieldTrasmissione(JspWriter jspwriter, String s) throws IOException{
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormField(
    			this, jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), 
    			null, s, getInputPrefix(), 1, 1, getStatus(), 
    			isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
	}

    public void writeFormLabelTrasmissione(JspWriter jspwriter, String s)
    		throws IOException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormLabel(
    			this, jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), null, s, null, this.getParentRoot().isBootstrap());
    }
	
    public void writeFormInputTrasmissione(JspWriter jspwriter, String s)
    		throws IOException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormInput(
    			jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), null, s, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

	public SimpleDetailCRUDController getCrudDocEleLineaColl() {
		return crudDocEleLineaColl;
	}

	public SimpleDetailCRUDController getCrudDocEleIVAColl() {
		return crudDocEleIVAColl;
	}

	public SimpleDetailCRUDController getCrudDocEleAllegatiColl() {
		return crudDocEleAllegatiColl;
	}

	public SimpleDetailCRUDController getCrudDocEleTributiColl() {
		return crudDocEleTributiColl;
	}

	public SimpleDetailCRUDController getCrudDocEleScontoMaggColl() {
		return crudDocEleScontoMaggColl;
	}

	public SimpleDetailCRUDController getCrudDocEleAcquistoColl() {
		return crudDocEleAcquistoColl;
	}

	public SimpleDetailCRUDController getCrudDocEleDdtColl() {
		return crudDocEleDdtColl;
	}

	@Override
	protected void resetTabs(ActionContext actioncontext) {
		setTab( "tab", "tabEleTrasmissione");
	}
	
	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isNewButtonHidden() {
		return true;
	}

	public void scaricaEsito(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		StorageObject fatturaFolder = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
		List<StorageObject> files = SpringUtil.getBean("storeService", StoreService.class).getChildren(fatturaFolder.getKey());
		for (StorageObject storageObject : files) {
			if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(
					documentoEleTestata.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?
							StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO.value():
							StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO.value())){
				HttpServletResponse response = ((HttpActionContext)actioncontext).getResponse();
				OutputStream os = response.getOutputStream();
				response.setContentType("application/octetstream");
				response.setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
				IOUtils.copy(SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey()), os);
				os.flush();
			}
		}
	}

	public void scaricaFatturaHtml(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		StorageObject fatturaFolder = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
		List<StorageObject> files = SpringUtil.getBean("storeService", StoreService.class).getChildren(fatturaFolder.getKey());
		for (StorageObject storageObject : files) {
			if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains("P:sigla_fatture_attachment:trasmissione_fattura")){
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Source xslDoc = null;
				if (documentoEleTestata.getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("FPA12")){
					xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.2.1.xsl"));
				} else if (documentoEleTestata.getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("SDI11")){
					xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.1.xsl"));
				} else {
					throw new ApplicationException("Il formato trasmissione indicato da SDI non rientra tra i formati attesi");
				}
				Source xmlDoc = new StreamSource(SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey()));
				HttpServletResponse response = ((HttpActionContext)actioncontext).getResponse();
				OutputStream os = response.getOutputStream();
				response.setContentType("text/html");
				Transformer trasform = tFactory.newTransformer(xslDoc);
				trasform.transform(xmlDoc, new StreamResult(os));
				os.flush();
			}
		}
	}

	public void scaricaFatturaFirmata(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
        final StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
        final StorageObject fattura = Optional.ofNullable(getModel())
                .filter(DocumentoEleTestataBulk.class::isInstance)
                .map(DocumentoEleTestataBulk.class::cast)
                .map(DocumentoEleTestataBulk::getDocumentoEleTrasmissione)
                .map(DocumentoEleTrasmissioneBulk::getCmisNodeRef)
                .map(cmisNodeRef -> storeService.getStorageObjectBykey(cmisNodeRef))
                .filter(storageObject -> Optional.ofNullable(storageObject).isPresent())
                .map(fatturaFolder ->
                    storeService.getChildren(fatturaFolder.getKey()).stream()
                            .filter(storageObject -> storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
                            ).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_POST_FIRMA.value()) ||
                                    storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
                                    ).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA.value()))
                            .reduce((x, y) -> {
                                return Optional.ofNullable(x)
                                        .filter(storageObject -> storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
                                        ).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_POST_FIRMA.value()))
                                        .orElse(y);
                            }).get()).orElseThrow(() -> new RuntimeException("Fattura non trovata!"));
        final HttpServletResponse response = ((HttpActionContext) actioncontext).getResponse();
        InputStream is = storeService.getResource(fattura);
        response.setContentLength(fattura.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
        response.setContentType(fattura.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
        response.setDateHeader("Expires", 0);
        IOUtils.copyLarge(is, response.getOutputStream());
        response.getOutputStream().flush();
	}


	public void scaricaFatturaXML(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		final StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
		final StorageObject fattura = Optional.ofNullable(getModel())
				.filter(DocumentoEleTestataBulk.class::isInstance)
				.map(DocumentoEleTestataBulk.class::cast)
				.map(DocumentoEleTestataBulk::getDocumentoEleTrasmissione)
				.map(DocumentoEleTrasmissioneBulk::getCmisNodeRef)
				.map(cmisNodeRef -> storeService.getStorageObjectBykey(cmisNodeRef))
				.filter(storageObject -> Optional.ofNullable(storageObject).isPresent())
				.map(fatturaFolder ->
						storeService.getChildren(fatturaFolder.getKey()).stream()
								.filter(storageObject -> !storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
								).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_POST_FIRMA.value()) &&
										storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
										).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA.value()))
								.reduce((x, y) -> {
									return Optional.ofNullable(x)
											.filter(storageObject -> storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()
											).contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA.value()))
											.orElse(y);
								}).get()).orElseThrow(() -> new RuntimeException("Fattura non trovata!"));
		final HttpServletResponse response = ((HttpActionContext) actioncontext).getResponse();
		InputStream is = storeService.getResource(fattura);
		response.setHeader("Content-disposition", "attachment; filename=" + fattura.getPropertyValue(StoragePropertyNames.NAME.value()));
		response.setContentLength(fattura.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		response.setContentType(fattura.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
		response.setDateHeader("Expires", 0);
		IOUtils.copyLarge(is, response.getOutputStream());
		response.getOutputStream().flush();
	}

	public String getNomeFileAllegato() {
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		if (allegato != null && allegato.getCmisNodeRef() != null)
			return allegato.getNomeAttachment();
		return null;
	}

	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		StorageObject storageObject = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(allegato.getCmisNodeRef());
		InputStream is = SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey());
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer,0,buflength);
		}
		is.close();
		os.flush();
	}

	public void reinviaEsito(ActionContext actioncontext, DocumentoEleTestataBulk documentoEleTestata) throws BusinessProcessException, ValidationException {
    	try {
        	((FatturaElettronicaPassivaComponentSession)createComponentSession()).
        		notificaEsito(actioncontext.getUserContext(), tipoIntegrazioneSDI, documentoEleTestata);
        	documentoEleTestata.setStatoNotificaEsito(null);
        	documentoEleTestata.setToBeUpdated();
        	update(actioncontext);    	
    	} catch (Exception e) {
    		throw handleException(e);
		}		
	}	
	
	public void rifiutaFattura(ActionContext actioncontext, DocumentoEleTestataBulk documentoEleTestata) throws BusinessProcessException, ValidationException {
		StatoDocumentoEleEnum statoDocumentoEleEnum = documentoEleTestata.getStatoDocumentoEle();
    	try {
        	documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.RIFIUTATO.name());
        	documentoEleTestata.setToBeUpdated();
        	((FatturaElettronicaPassivaComponentSession)createComponentSession()).
    		notificaEsito(actioncontext.getUserContext(), tipoIntegrazioneSDI, documentoEleTestata);
    	} catch (Exception e) {
    		documentoEleTestata.setStatoDocumento(statoDocumentoEleEnum.name());
    		throw handleException(e);
		}
    	setModel(actioncontext, documentoEleTestata);    	
    	update(actioncontext);    	
	}

	@Override
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) oggettobulk;
    	if (!documentoEleTestata.isEditabile())
    		setStatus(VIEW);		
	}

	@Override
	public void setTab(String tabName, String pageName) {
		super.setTab(tabName, pageName);
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		if (documentoEleTestata != null) {
			if (pageName.equalsIgnoreCase("tabAllegati")){
				setStatus(EDIT);
			} else {
				if (getStatus() == EDIT && !documentoEleTestata.isEditabile())
					setStatus(VIEW);				
			}			
		}
	}
	
	public OggettoBulk completaFatturaPassiva(ActionContext context, Fattura_passivaBulk fatturaPassivaBulk, CRUDFatturaPassivaBP nbp, Fattura_passivaBulk fatturaPassivaDiRiferimento) throws BusinessProcessException {
    	try {    		
			CRUDFatturaPassivaAction action = new CRUDFatturaPassivaAction();
			FatturaPassivaComponentSession comp = (FatturaPassivaComponentSession)nbp.createComponentSession();
					DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
			fatturaPassivaBulk.setDocumentoEleTestata(documentoEleTestata);
			fatturaPassivaBulk = comp.caricaAllegatiBulk(context.getUserContext(), fatturaPassivaBulk);
			fatturaPassivaBulk.setTi_fattura(documentoEleTestata.getTipoDocumentoSIGLA());
	    	fatturaPassivaBulk.setNr_fattura_fornitore(documentoEleTestata.getNumeroDocumento());
	    	fatturaPassivaBulk.setDt_fattura_fornitore(documentoEleTestata.getDataDocumento());
	    	fatturaPassivaBulk.setEsercizio_fattura_fornitore(CNRUserContext.getEsercizio(context.getUserContext()));//TODO
	    	fatturaPassivaBulk.setData_protocollo(documentoEleTestata.getDocumentoEleTrasmissione().getDataRicezione());
	    	Calendar date = Calendar.getInstance();
	    	date.setTimeInMillis(documentoEleTestata.getDataDocumento().getTime());
	    	date.add(Calendar.MONTH, 1);
	    	
	    	fatturaPassivaBulk.setDs_fattura_passiva(documentoEleTestata.getCausale());
	    	fatturaPassivaBulk.setFl_intra_ue(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_extra_ue(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_san_marino_senza_iva(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_fattura_compenso(existsTributi(documentoEleTestata));

	    	//Il flag viene impostato a true se documento splitPayment con iva != 0
	    	fatturaPassivaBulk.setFl_split_payment(documentoEleTestata.isDocumentoSplitPayment() &&
	    			documentoEleTestata.getDocEleIVAColl()!=null && !documentoEleTestata.getDocEleIVAColl().isEmpty() && 
	    			documentoEleTestata.getDocEleIVAColl().stream().map(x->x.getImposta()).reduce((x,y)->x.add(y)).get().compareTo(BigDecimal.ZERO)!=0);

	    	//if (fatturaPassivaBulk.getFl_split_payment()) {
	    		java.util.Vector sezionali = comp.estraeSezionali(context.getUserContext(),fatturaPassivaBulk);
	    		fatturaPassivaBulk.setSezionali(sezionali);
	    		if (sezionali != null && !sezionali.isEmpty())
	    			fatturaPassivaBulk.setTipo_sezionale((Tipo_sezionaleBulk)sezionali.firstElement());
	    		else
	    			fatturaPassivaBulk.setTipo_sezionale(null);
	    	//}

	    	//TODO eliminata su richiesta di Patrizia fatturaPassivaBulk.setDt_scadenza(new java.sql.Timestamp(date.getTime().getTime())); 
	    	GregorianCalendar gcDataMinima = new GregorianCalendar(), gcDataMassima = new GregorianCalendar();
	    	gcDataMinima.setTime(calcolaDataMinimaCompetenza(documentoEleTestata));
	    	gcDataMassima.setTime(calcolaDataMassimaCompetenza(documentoEleTestata));
	    	
    		if (!fatturaPassivaBulk.getFl_fattura_compenso() && gcDataMinima.get(Calendar.YEAR)<gcDataMassima.get(Calendar.YEAR))
    			gcDataMinima.setTime(DateUtils.firstDateOfTheYear(gcDataMassima.get(Calendar.YEAR)));

        	fatturaPassivaBulk.setDt_da_competenza_coge(new Timestamp(gcDataMinima.getTime().getTime()));
	    	fatturaPassivaBulk.setDt_a_competenza_coge(new Timestamp(gcDataMassima.getTime().getTime()));
	    	if(fatturaPassivaBulk.getTi_fattura().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO)){
	    		fatturaPassivaBulk.setIm_totale_fattura(documentoEleTestata.getImportoDocumento().abs());
	    		fatturaPassivaBulk.setIm_importo_totale_fattura_fornitore_euro(documentoEleTestata.getImportoDocumento().abs());
	    	}else{
	    		fatturaPassivaBulk.setIm_totale_fattura(documentoEleTestata.getImportoDocumento());
	    		fatturaPassivaBulk.setIm_importo_totale_fattura_fornitore_euro(documentoEleTestata.getImportoDocumento());
	    	}
			fatturaPassivaBulk = comp.valorizzaInfoDocEle(context.getUserContext(), fatturaPassivaBulk);
	    	nbp.setModel(context, fatturaPassivaBulk);

	    	action.doCalcolaTotaleFatturaFornitoreInEur(context);
			fatturaPassivaBulk.setPartita_iva(documentoEleTestata.getDocumentoEleTrasmissione().getPrestatoreCodice());
	    	action.doBringBackSearchFornitore(context, fatturaPassivaBulk, documentoEleTestata.getDocumentoEleTrasmissione().getPrestatore());

	    	fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
	    	if (documentoEleTestata.getModalitaPagamento() != null) {
	    		fatturaPassivaBulk.setModalita_pagamento(documentoEleTestata.getModalitaPagamento().getRif_modalita_pagamento());
	    		action.doOnModalitaPagamentoChange(context);	    		
	    	}
			fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
			if (fatturaPassivaDiRiferimento == null) {
				FatturaPassivaRigaCRUDController dettaglioController = nbp.getDettaglio();			
				for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
					Fattura_passiva_rigaBulk rigaFattura = documentoEleTestata.getInstanceRiga();			
					int i = dettaglioController.addDetail(rigaFattura);
					dettaglioController.setDirty(true);
					dettaglioController.setModelIndex(context, i);
					rigaFattura.setBene_servizio(documentoEleLinea.getBeneServizio());
					if(documentoEleLinea.getLineaDescrizione().length()>199)
						rigaFattura.setDs_riga_fattura(documentoEleLinea.getLineaDescrizione().substring(0, 199));
					else
						rigaFattura.setDs_riga_fattura(documentoEleLinea.getLineaDescrizione());
					rigaFattura.setVoce_iva(recuperaCodiceIVA(documentoEleTestata, documentoEleLinea));
					rigaFattura.setQuantita(documentoEleLinea.getLineaQuantita());
					action.doOnQuantitaChange(context);
					rigaFattura.setPrezzo_unitario(documentoEleLinea.getLineaPrezzounitario());
					action.doCalcolaTotaliDiRiga(context);
					if (documentoEleTestata.getModalitaPagamento() != null)
						rigaFattura.setModalita_pagamento(documentoEleTestata.getModalitaPagamento().getRif_modalita_pagamento());

			    	//TODO eliminata su richiesta di Patrizia fatturaPassivaBulk.setDt_scadenza(new java.sql.Timestamp(date.getTime().getTime())); 
			    	GregorianCalendar gcDataMinimaRiga = new GregorianCalendar(), gcDataMassimaRiga = new GregorianCalendar();
			    	gcDataMinimaRiga.setTime(documentoEleLinea.getInizioDatacompetenza()==null?gcDataMinima.getTime():documentoEleLinea.getInizioDatacompetenza());
			    	gcDataMassimaRiga.setTime(documentoEleLinea.getFineDatacompetenza()==null?gcDataMassima.getTime():documentoEleLinea.getFineDatacompetenza());
			    	
			    	if (fatturaPassivaBulk.getFl_fattura_compenso()) {
						rigaFattura.setDt_da_competenza_coge(new Timestamp(gcDataMinimaRiga.getTime().getTime()));
						rigaFattura.setDt_a_competenza_coge(new Timestamp(gcDataMassimaRiga.getTime().getTime()));	
			    	} else {
			    		if (gcDataMinimaRiga.get(Calendar.YEAR)<gcDataMinima.get(Calendar.YEAR))
			    			gcDataMinimaRiga = gcDataMinima;
			    		if (gcDataMassimaRiga.get(Calendar.YEAR)<gcDataMinima.get(Calendar.YEAR))
			    			gcDataMassimaRiga = gcDataMinima;
						rigaFattura.setDt_da_competenza_coge(new Timestamp(gcDataMinimaRiga.getTime().getTime()));
						rigaFattura.setDt_a_competenza_coge(new Timestamp(gcDataMassimaRiga.getTime().getTime()));	
			    	}
				}				
			}
			nbp.initializeModelForEditAllegati(context, fatturaPassivaBulk);
			return fatturaPassivaBulk;
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);		
		}			
	}

	private Voce_ivaBulk recuperaCodiceIVA(DocumentoEleTestataBulk documentoEleTestata, DocumentoEleLineaBulk documentoEleLinea) {
		for (DocumentoEleIvaBulk documentoEleIVA : documentoEleTestata.getDocEleIVAColl()) {
			if ((documentoEleLinea.getLineaAliquotaiva() != null && 
					documentoEleIVA.getAliquotaIva() != null && 
					documentoEleLinea.getLineaAliquotaiva().equals(documentoEleIVA.getAliquotaIva())) ||
					(documentoEleLinea.getLineaNatura() != null && 
					documentoEleIVA.getNatura() != null && 
					documentoEleLinea.getLineaNatura().equals(documentoEleIVA.getNatura())))
			return documentoEleIVA.getVoceIva();
		}		
		return null;
	}

	private Timestamp calcolaDataMinimaCompetenza(
			DocumentoEleTestataBulk documentoEleTestata) {
		java.sql.Timestamp inizioDatacompetenza = EJBCommonServices.getServerDate();
		for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
			if (inizioDatacompetenza == null)
				inizioDatacompetenza = documentoEleLinea.getInizioDatacompetenza();
			else
				inizioDatacompetenza = DateUtils.min(inizioDatacompetenza, documentoEleLinea.getInizioDatacompetenza());
		}		
		return inizioDatacompetenza;
	}
	private Timestamp calcolaDataMassimaCompetenza(
			DocumentoEleTestataBulk documentoEleTestata) {
		java.sql.Timestamp fineDatacompetenza = null;
		for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
			if (fineDatacompetenza == null)
				fineDatacompetenza = documentoEleLinea.getFineDatacompetenza();
			else
				fineDatacompetenza = DateUtils.max(fineDatacompetenza, documentoEleLinea.getFineDatacompetenza());
		}		
		if (fineDatacompetenza == null)
			fineDatacompetenza = EJBCommonServices.getServerDate();
		return fineDatacompetenza;
	}

	private Boolean existsTributi(DocumentoEleTestataBulk documentoEleTestata) {
		for (DocumentoEleTributiBulk documentoEleTributi : documentoEleTestata.getDocEleTributiColl()) {
			if (documentoEleTributi.getImporto() != null && BigDecimal.ZERO.compareTo(documentoEleTributi.getImporto()) != 0)
				return Boolean.TRUE;
		}	
		if (documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale()!= null && 
			(documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_02.name()) ||
			 documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_19.name()))		
		   )
			return Boolean.TRUE;
			
	return Boolean.FALSE;
	}

	@Override
	protected String getStorePath(DocumentoEleTestataBulk documentoEleTestata, boolean create) throws BusinessProcessException {
		return Optional.ofNullable(documentoEleTestata)
				.map(DocumentoEleTestataBulk::getDocumentoEleTrasmissione)
				.map(DocumentoEleTrasmissioneBase::getCmisNodeRef)
				.map(s -> {
					return Optional.ofNullable(SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(s))
							.map(StorageObject::getPath)
							.orElse(null);
				})
				.orElse(null);
	}

	@Override
	protected Class<AllegatoFatturaBulk> getAllegatoClass() {
		return AllegatoFatturaBulk.class;
	}

	@Override
	protected boolean excludeChild(StorageObject storageObject) {
		if (Stream.of(crudDocEleAllegatiColl.getDetails().stream().toArray())
				.filter(DocumentoEleAllegatiBulk.class::isInstance)
				.map(DocumentoEleAllegatiBulk.class::cast)
				.map(DocumentoEleAllegatiBulk::getCmisNodeRef)
                .filter(cmisNodeRef -> Optional.ofNullable(cmisNodeRef).isPresent())
				.anyMatch(cmisNodeRef -> cmisNodeRef.equals(
						Optional.ofNullable(storageObject)
								.map(StorageObject::getKey)
								.orElse("")
				))){
			return true;
		}
		if (storageObject.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value())
				.equalsIgnoreCase("D:sigla_fatture_attachment:document"))
			return true;
		return super.excludeChild(storageObject);
	}

	public Boolean isPartitaIvaGruppoIva(UserContext usercontext, AnagraficoBulk anagrafico, String partitaIva, Timestamp dataDocumento) throws BusinessProcessException, ValidationException {
		try {
			return ((FatturaElettronicaPassivaComponentSession)createComponentSession()).
					isPartitaIvaGruppoIva(usercontext, anagrafico, partitaIva, dataDocumento);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	@Override
	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		return super.find(actioncontext, compoundfindclause, oggettobulk, oggettobulk1, s);
	}

	protected CRUDComponentSession getComponentSession() {
		return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}

	public String getEMailPEC(ActionContext actionContext) throws BusinessProcessException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		Optional<TerzoBulk> prestatore = Optional.ofNullable(documentoEleTestata.getDocumentoEleTrasmissione().getPrestatore());
		if (!prestatore.isPresent())
			return null;
		try {
			final List<TelefonoBulk> findTelefoni = Optional.ofNullable(getComponentSession().find(
					actionContext.getUserContext(), prestatore.get().getClass(),
					"findTelefoni", prestatore.get(), TelefonoBulk.PEC))
					.filter(List.class::isInstance)
					.map(List.class::cast)
					.orElse(Collections.emptyList());
			return findTelefoni
					.stream()
					.filter(telefonoBulk -> Optional.ofNullable(telefonoBulk.getRiferimento()).isPresent() &&
										telefonoBulk.getFattElettronica())
					.map(TelefonoBulk::getRiferimento)
					.findAny()
					.orElse(null);
		} catch (ComponentException|RemoteException e) {
			throw handleException(e);
		}
	}

	public void collegaNotaFattura(ActionContext context, DocumentoEleTestataBulk fattura, DocumentoEleTestataBulk nota) throws BusinessProcessException {
		try {
			nota.setStatoDocumento(StatoDocumentoEleEnum.STORNATO.name());
			nota.setFlIrregistrabile("S");
			nota.setFatturaCollegata(fattura);
			nota.setToBeUpdated();
			getComponentSession().modificaConBulk(context.getUserContext(), nota);
			fattura.setStatoDocumento(StatoDocumentoEleEnum.STORNATO.name());
			fattura.setToBeUpdated();
			getComponentSession().modificaConBulk(context.getUserContext(), fattura);
			setMessage(FormBP.INFO_MESSAGE, "La Fattura [" + fattura.getNumeroDocumento() + "] è stata collegata alla nota correttamente.");
			edit(context, nota);
		} catch (ComponentException | RemoteException e) {
			throw handleException(e);
		}
	}

	public void rifiutaFatturaConPEC(ActionContext context, DocumentoEleTestataBulk bulk, RifiutaFatturaBulk rifiutaFatturaBulk) throws BusinessProcessException {
		try {
			if (isDirty()) {
				save(context);
				bulk = (DocumentoEleTestataBulk) createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), bulk);
			}
			TerzoBulk prestatore =
					Optional.ofNullable(bulk.getDocumentoEleTrasmissione().getPrestatore())
						.orElseThrow(() -> new ApplicationException("Valorizzare il terzo cedente/prestatore"));
			TerzoBulk terzoPerUnitaOrganizzativa = ((it.cnr.contab.anagraf00.ejb.TerzoComponentSession) createComponentSession("CNRANAGRAF00_EJB_TerzoComponentSession"))
					.cercaTerzoPerUnitaOrganizzativa(
							context.getUserContext(), bulk.getDocumentoEleTrasmissione().getUnitaOrganizzativa());
			Numerazione_baseComponentSession numerazione = (Numerazione_baseComponentSession)
							EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession");
			boolean isNota = bulk.getTipoDocumento().equalsIgnoreCase(TipoDocumentoType.TD_04.value());
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);
			print.setReport("/docamm/docamm/rifiuto_fattura_elettronica.jasper");
			print.setNomeFile(
					"Comunicazione di non registrabilità del ".concat(
							LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
					).concat(".pdf")
			);
			print.setUtcr(context.getUserContext().getUser());
			print.addParam("title",
					Optional.ofNullable(rifiutaFatturaBulk.getMessageText())
						.filter(s -> !isNota)
						.map(s -> "Richiesta storno Documento Elettronico")
						.orElseGet(() -> "Rifiuto Documento Elettronico"),
					String.class);
			print.addParam("message", rifiutaFatturaBulk.getMessage(), String.class);
			print.addParam("note", rifiutaFatturaBulk.getNote(), String.class);
			print.addParam("is_nota", isNota, Boolean.class);

			print.addParam("codice_uo", bulk.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa(), String.class);
			print.addParam("descrizione_uo", bulk.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getDs_unita_organizzativa(), String.class);
			print.addParam("codice_cuu", terzoPerUnitaOrganizzativa.getCodiceUnivocoUfficioIpa(), String.class);

			print.addParam("denominazione_sede", prestatore.getDenominazione_sede(), String.class);
			print.addParam("pec", rifiutaFatturaBulk.getEmailPEC(), String.class);

			print.addParam("tipo_documento", DocumentoEleTestataBulk.tiTipoDocumentoKeys.get(bulk.getTipoDocumento()), String.class);
			print.addParam("identificativo_sdi", bulk.getIdentificativoSdi(), Long.class);
			print.addParam("numero_documento", bulk.getNumeroDocumento(), String.class);
			print.addParam("data_documento", bulk.getDataDocumento(), Date.class, dateFormat);
			print.addParam("data_ricezione", bulk.getDocumentoEleTrasmissione().getDataRicezione(), Date.class, dateFormat);

			Report report = SpringUtil.getBean("printService",
					PrintService.class).executeReport(context.getUserContext(),
					print);
			LocalDateTime now = LocalDateTime.now();
			int esercizio = now.getYear();
			Long numProtocollo = numerazione.creaNuovoProgressivo(
					context.getUserContext(),
					esercizio,
					"RIFIUTO_FATTURA_PEC",
					"NUM_PROTOCOLLO",
					CNRUserContext.getUser(context.getUserContext())
			);

			AllegatoNonRegistrabilitaBulk allegatoNonRegistrabilitaBulk = new AllegatoNonRegistrabilitaBulk();
			allegatoNonRegistrabilitaBulk.setNome(report.getName());
			allegatoNonRegistrabilitaBulk.setUtenteSIGLA(CNRUserContext.getUser(context.getUserContext()));
			allegatoNonRegistrabilitaBulk.setAnnoProtocollo(esercizio);
			allegatoNonRegistrabilitaBulk.setNumeroProtocollo(Utility.lpad(numProtocollo, 6, '0'));
			allegatoNonRegistrabilitaBulk.setDataProtocollo(Date.from(now.toInstant(ZoneOffset.UTC)));
			allegatoNonRegistrabilitaBulk.setTitolo("Allegato inviato al seguente indirizzo email: " + rifiutaFatturaBulk.getEmailPEC());

			final StorageObject storageObject = SpringUtil.getBean("storeService", StoreService.class)
					.restoreSimpleDocument(
						allegatoNonRegistrabilitaBulk,
						report.getInputStream(),
						report.getContentType(),
						report.getName(),
						Optional.ofNullable(getStorePath(bulk, false))
							.orElseThrow(() -> new ApplicationException("Path sul documentale non trovato, contattare il supporto Help Desk!")),
					false
					);

			FatturaPassivaElettronicaService fatturaPassivaElettronicaService = SpringUtil.getBean("fatturaPassivaElettronicaService",
					FatturaPassivaElettronicaService.class);
			fatturaPassivaElettronicaService.inviaPECFornitore(
					context.getUserContext(),
					new ByteArrayDataSource(storeService.getResource(storageObject.getKey()), MimeTypes.PDF.mimetype()),
					report.getName(),
					rifiutaFatturaBulk.getEmailPEC(),
					Optional.ofNullable(rifiutaFatturaBulk.getMessageText())
							.filter(s -> !isNota)
							.map(s -> "Richiesta Storno documento elettronico ricevuto IdentificativoSdI: ")
							.orElseGet(() -> "Rifiuto documento elettronico ricevuto IdentificativoSdI: ").concat(bulk.getIdentificativoSdi().toString()),
					Optional.ofNullable(rifiutaFatturaBulk.getMessageText())
							.filter(s -> !isNota)
							.map(s -> "Richiesta Storno documento elettronico ricevuto. ")
							.orElseGet(() -> "Rifiuto documento elettronico ricevuto. ").concat(
								"Informazioni del rifiuto e riferimenti del documento in allegato."+
								"\n\nNota: questa è un'e-mail generata automaticamente e non avremo la possibilità di " +
								"leggere eventuali e-mail di risposta. Non rispondere a questo messaggio.")
			);
			bulk.setFlIrregistrabile("S");
			if (isNota) {
				bulk.setStatoDocumento(StatoDocumentoEleEnum.RIFIUTATA_CON_PEC.name());
			} else {
				bulk.setStatoDocumento(StatoDocumentoEleEnum.DA_STORNARE.name());
			}
			bulk.setToBeUpdated();
			OggettoBulk oggettoBulk = getComponentSession().modificaConBulk(context.getUserContext(), bulk);
			if (!Optional.ofNullable(getEMailPEC(context)).isPresent()) {
				TelefonoBulk telefonoBulk = new TelefonoBulk();
				telefonoBulk.setTerzo(prestatore);
				telefonoBulk.setRiferimento(rifiutaFatturaBulk.getEmailPEC());
				telefonoBulk.setTi_riferimento(TelefonoBulk.PEC);
				telefonoBulk.setFattElettronica(Boolean.TRUE);
				telefonoBulk.setToBeCreated();
				getComponentSession().creaConBulk(context.getUserContext(), telefonoBulk);
			}
			setMessage("Comunicazione inviata correttamente.");
			edit(context, oggettoBulk);
		} catch (ComponentException | IOException | EmailException | BusyResourceException | ValidationException e) {
			throw handleException(e);
		}
	}

	private boolean isComunicazioneNonRegistabilita(AllegatoGenericoBulk allegato) {
		return Optional.ofNullable(allegato)
				.filter(AllegatoFatturaBulk.class::isInstance)
				.map(AllegatoFatturaBulk.class::cast)
				.map(AllegatoFatturaBulk::getAspect)
				.filter(strings -> strings.contains(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_COMUNICAZIONE_NON_REGISTRABILITA.value()))
				.isPresent();
	}
	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		if (isComunicazioneNonRegistabilita(allegato) &&
				Optional.ofNullable(getModel())
					.filter(DocumentoEleTestataBulk.class::isInstance)
					.map(DocumentoEleTestataBulk.class::cast)
					.map(DocumentoEleTestataBulk::getFlIrregistrabile)
					.map(s -> s.equalsIgnoreCase("S"))
					.orElse(Boolean.TRUE)
		) {
			return false;
		}
		return super.isPossibileCancellazione(allegato);
	}

	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
		if (isComunicazioneNonRegistabilita(allegato)) {
			return false;
		}
		return super.isPossibileModifica(allegato);
	}

	@Override
	protected void completeAllegato(AllegatoFatturaBulk allegato, StorageObject storageObject) throws ApplicationException {
		Optional.ofNullable(storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
				.map(strings -> strings.stream())
				.ifPresent(stringStream -> {
					stringStream
							.filter(s -> AllegatoFatturaBulk.aspectNamesDecorrenzaTerminiKeys.get(s) != null)
							.findFirst()
							.ifPresent(s -> allegato.setAspectName(s));
				});
		Optional.ofNullable(storageObject.<String>getPropertyValue("sigla_commons_aspect:utente_applicativo"))
				.ifPresent(s -> allegato.setUtenteSIGLA(s));
		super.completeAllegato(allegato, storageObject);
	}
	
	@Override
	public String getAllegatiFormName() {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		String allegatiFormName = super.getAllegatiFormName();
		if (documentoEleTestata.isRicevutaDecorrenzaTermini() && allegatiFormName.equalsIgnoreCase("default"))
			return "decorrenzaTermini";
		return allegatiFormName;
	}
	
	@Override
	public void save(ActionContext actioncontext) throws ValidationException,
			BusinessProcessException {
		boolean esisteAllegato=false;
		for (Object obj : getCrudArchivioAllegati().getDetails()) {
			AllegatoFatturaBulk allegatoFatturaBulk = (AllegatoFatturaBulk)obj;
			if (allegatoFatturaBulk != null && allegatoFatturaBulk.getAspectName() != null && 
					allegatoFatturaBulk.getAspectName().equalsIgnoreCase("P:sigla_fatture_attachment:comunicazione_non_registrabilita")) {
				DocumentoEleTestataBulk testata = ((DocumentoEleTestataBulk) getModel());
				if (testata.isRegistrata()){
					throw new ValidationException("La fattura risulta registrata, comunicazione di documento non registrabile non allegabile.");
				}
				testata.setFlIrregistrabile("S");				
				esisteAllegato=true;
			}
		}
		if (!esisteAllegato && ((DocumentoEleTestataBulk) getModel()).isAbilitato())
			((DocumentoEleTestataBulk) getModel()).setFlIrregistrabile("N");
		super.save(actioncontext);
	}

	public boolean isEsercizioAperto() {
		return esercizioAperto;
	}

	public void setEsercizioAperto(boolean esercizioAperto) {
		this.esercizioAperto = esercizioAperto;
	}
	
	public Date getDataAttivazioneSplit() {
		return dataAttivazioneSplit;
	}

	public Date getDataDisattivazioneSplit() {
		return dataDisattivazioneSplit;
	}

	public void setDataDisattivazioneSplit(Date dataDisattivazioneSplit) {
		this.dataDisattivazioneSplit = dataDisattivazioneSplit;
	}

	public Date getDataAttivazioneSplitProf() {
		return dataAttivazioneSplitProf;
	}

	public void setDataAttivazioneSplitProf(Date dataAttivazioneSplitProf) {
		this.dataAttivazioneSplitProf = dataAttivazioneSplitProf;
	}

	public Date getDataDisattivazioneSplitProf() {
		return dataDisattivazioneSplitProf;
	}

	public void setDataDisattivazioneSplitProf(Date dataDisattivazioneSplitProf) {
		this.dataDisattivazioneSplitProf = dataDisattivazioneSplitProf;
	}

}