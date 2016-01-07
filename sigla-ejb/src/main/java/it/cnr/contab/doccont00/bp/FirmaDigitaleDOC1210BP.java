package it.cnr.contab.doccont00.bp;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
/**
 * 
 * @author mspasiano
 * @date 30-11-2015
 * 
 *
 */
public class FirmaDigitaleDOC1210BP extends AbstractFirmaDigitaleDocContBP {
	private static final long serialVersionUID = 1L;
	
	public FirmaDigitaleDOC1210BP() {
		super();
	}

	public FirmaDigitaleDOC1210BP(String s) {
		super(s);
	}

	@Override
	public void openIterator(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			Unita_organizzativaBulk uoScrivania = CNRUserInfo.getUnita_organizzativa(actioncontext);
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS,
					((CNRUserContext) actioncontext.getUserContext()).getEsercizio());
			if (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)!=0) {
				compoundfindclause.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS,
						((CNRUserContext) actioncontext.getUserContext()).getCd_cds());
				if (!uoScrivania.isUoCds())
					compoundfindclause.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS,
						((CNRUserContext) actioncontext.getUserContext()).getCd_unita_organizzativa());				
			}
			setBaseclause(compoundfindclause);
			setIterator(actioncontext, find(actioncontext, compoundfindclause, getModel()));
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	@Override
	protected void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione...bulks) throws ComponentException, RemoteException {
		for (StatoTrasmissione statoTrasmissione : bulks) {
			Lettera_pagam_esteroBulk lettera = new Lettera_pagam_esteroBulk(statoTrasmissione.getCd_cds(),statoTrasmissione.getCd_unita_organizzativa(),
					statoTrasmissione.getEsercizio(), statoTrasmissione.getPg_documento_cont());
			lettera = (Lettera_pagam_esteroBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), lettera);
			lettera.setStato_trasmissione(stato);
			if (stato.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
				lettera.setDt_firma(EJBCommonServices.getServerTimestamp());
			else
				lettera.setDt_firma(null);					
			lettera.setToBeUpdated();
			getComponentSession().modificaConBulk(actioncontext.getUserContext(), lettera);
		}		
	}
	
	private void valorizzaField(PDAcroForm pdAcroForm, String fieldName, String fieldValue) throws IOException {
		PDField field = pdAcroForm.getField(fieldName);
		if (field != null && fieldValue != null) {
			if (field instanceof PDCheckbox) {
				if (Boolean.valueOf(fieldValue))
					((PDCheckbox)field).check();
				else
					((PDCheckbox)field).unCheck();
			} else {
				field.setValue(fieldValue);				
			}
		}
	}
	
	@Override
	@SuppressWarnings({ "unused", "unchecked" })
	public void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException{
		try {
			List<Lettera_pagam_esteroBulk> selectedElements = getSelectedElements(actioncontext);
			if (selectedElements == null || selectedElements.isEmpty())
					throw new ApplicationException("Selezionare almeno un elemento!");
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			for (Lettera_pagam_esteroBulk lettera : selectedElements) {
				CMISPath cmisPath = lettera.getCMISPath(cmisService);
				lettera.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
				PDDocument document = PDDocument.load(this.getClass().getResourceAsStream("1210.pdf"));
				PDDocumentCatalog pdCatalog = document.getDocumentCatalog();
				PDAcroForm pdAcroForm = pdCatalog.getAcroForm();
				valorizzaField(pdAcroForm, "LUOGO", "ROMA");
				valorizzaField(pdAcroForm, "DATA", new SimpleDateFormat("dd/MM/yyyy").format(lettera.getDt_registrazione()));
				valorizzaField(pdAcroForm, "NUM_RIF", String.valueOf(lettera.getPg_lettera()));
				valorizzaField(pdAcroForm, "BONIFICO_MEZZO_"+lettera.getBonifico_mezzo(), "true");
				valorizzaField(pdAcroForm, "DIVISA", lettera.getDivisa());
				valorizzaField(pdAcroForm, "IMPORTO", new java.text.DecimalFormat("#,##0.00").format(lettera.getIm_pagamento()));
				valorizzaField(pdAcroForm, "IMPORTO_LETTERE", Utility.NumberToText(lettera.getIm_pagamento()));
				valorizzaField(pdAcroForm, "BENEFICIARIO_1", lettera.getBeneficiario());
				valorizzaField(pdAcroForm, "NUM_CONTO", lettera.getNum_conto_ben());
				valorizzaField(pdAcroForm, "IBAN", lettera.getIban());
				valorizzaField(pdAcroForm, "PRESSO_TRAMITE", lettera.getIndirizzo());
				valorizzaField(pdAcroForm, "SWIFT_BIC_ADDRESS", lettera.getIndirizzo_swift());
				valorizzaField(pdAcroForm, "MOTIVO_PAGAMENTO", lettera.getMotivo_pag());
				valorizzaField(pdAcroForm, "AMMONTARE_DEBITO_"+lettera.getAmmontare_debito(), "true");
				valorizzaField(pdAcroForm, "CONTO_PROVVISORIO_"+lettera.getAmmontare_debito(), lettera.getConto_debito());
				valorizzaField(pdAcroForm, "COMMISSIONI_SPESE_"+lettera.getCommissioni_spese(), "true");
				valorizzaField(pdAcroForm, "COMMISSIONI_SPESE_ESTERE_"+lettera.getCommissioni_spese_estere(), "true");
				for (Object obj : pdAcroForm.getFields()) {
					PDField field = (PDField)obj;
					field.setReadonly(true);
				}				
				ByteArrayOutputStream output = new ByteArrayOutputStream();				
				document.save(output);
				document.close();
				Document node = cmisService.storeSimpleDocument(lettera, new ByteArrayInputStream(output.toByteArray()),"application/pdf", 
						lettera.getCMISFolderName() + ".pdf", cmisPath);				
				aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO, lettera);
			}
			setMessage("Predisposizione effettuata correttamente.");
		} catch (ApplicationException e) {
			setMessage(e.getMessage());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		} catch (COSVisitorException e) {
			throw handleException(e);
		}
	}

	@Override
	protected AbilitatoFirma getAbilitatoFirma() {
		return AbilitatoFirma.DOC_1210;
	}
	@Override
	protected String getTitoloFirma() {
		return "firma per il controllo di ragioneria";
	}
}