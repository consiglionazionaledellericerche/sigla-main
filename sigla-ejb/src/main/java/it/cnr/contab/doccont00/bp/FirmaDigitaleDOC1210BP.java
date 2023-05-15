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

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mspasiano
 * @date 30-11-2015
 *
 *
 */
public class FirmaDigitaleDOC1210BP extends AbstractFirmaDigitaleDocContBP {
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(FirmaDigitaleDOC1210BP.class);

	public FirmaDigitaleDOC1210BP() {
		super();
	}

	public FirmaDigitaleDOC1210BP(String s) {
		super(s);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		super.init(config, actioncontext);
		openIterator(actioncontext);
	}

	public RemoteIterator search(
			ActionContext actioncontext,
			CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk)
			throws BusinessProcessException {
		Lettera_pagam_esteroBulk lettera = (Lettera_pagam_esteroBulk) oggettobulk;
		Lettera_pagam_esteroBulk letteraAll = new Lettera_pagam_esteroBulk();
		letteraAll.setStato_trasmissione(null);
		try {
			return getComponentSession().cerca(actioncontext.getUserContext(),
					compoundfindclause,
					lettera.getStato_trasmissione().equalsIgnoreCase(StatoTrasmissione.ALL) ? letteraAll : lettera,
                    "selectByClauseForFirma1210");
		} catch (ComponentException|RemoteException e) {
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
	@Override
	protected void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione...bulks) throws ComponentException, RemoteException {
		for (StatoTrasmissione statoTrasmissione : bulks) {
			Lettera_pagam_esteroBulk lettera = new Lettera_pagam_esteroBulk(statoTrasmissione.getCd_cds(),statoTrasmissione.getCd_unita_organizzativa(),
					statoTrasmissione.getEsercizio(), statoTrasmissione.getPg_documento_cont());
			lettera = (Lettera_pagam_esteroBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), lettera);
			if (!statoTrasmissione.getStato_trasmissione().equals(lettera.getStato_trasmissione()))
				throw new ApplicationException("Risorsa non più valida, eseguire nuovamente la ricerca!");
			lettera.setStato_trasmissione(stato);
			if (stato.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
				lettera.setDt_firma(EJBCommonServices.getServerTimestamp());
			else
				lettera.setDt_firma(null);
			lettera.setToBeUpdated();
			getComponentSession().modificaConBulk(actioncontext.getUserContext(), lettera);
		}
	}

	@Override
	public StatoTrasmissione getStatoTrasmissione(ActionContext actioncontext, Integer esercizio, String tipo, String cds, String uo, Long numero_documento) {
		return new Lettera_pagam_esteroBulk(cds,uo, esercizio,numero_documento);
	}

	private PDField valorizzaField(PDAcroForm pdAcroForm, String fieldName, String fieldValue, boolean autosize) throws IOException, ApplicationException {
        PDField field = pdAcroForm.getField(fieldName);
        if (field != null) {
            if (field instanceof PDCheckBox) {
                if (Boolean.valueOf(fieldValue))
                    ((PDCheckBox)field).check();
                else
                    ((PDCheckBox)field).unCheck();
            } else {
				field.getAcroForm().setNeedAppearances(Boolean.TRUE);
                field.setValue(
                        Optional.ofNullable(fieldValue)
							.map(s -> s.replace("\r", " "))
							.map(s -> s.replace("\n", " "))
							.orElseThrow(() -> new ApplicationMessageFormatException(
								"Predisposizione non possibile. Il valore del campo [{0}] non può essere nullo!", fieldName
							)));
				if (autosize) {
					Optional.ofNullable(field)
						.filter(PDTextField.class::isInstance)
						.map(PDTextField.class::cast)
						.ifPresent(pdTextField -> {
							pdTextField.setDefaultAppearance("/Helv 0 Tf 0 0 0 rg");
						});
				}
            }
		}
        return field;
	}

	public PDDocument createDocument(Lettera_pagam_esteroBulk lettera, Configurazione_cnrBulk configurazione) throws IOException, ApplicationException {
		PDDocument document = PDDocument.load(this.getClass().getResourceAsStream("1210.pdf"));
		PDDocumentCatalog pdCatalog = document.getDocumentCatalog();
		PDAcroForm pdAcroForm = pdCatalog.getAcroForm();

		List<PDField> fields = new ArrayList<PDField>();
		fields.add(valorizzaField(pdAcroForm, "DATA ESECUZIONE", new SimpleDateFormat("dd/MM/yyyy").format(lettera.getDt_registrazione()), false));
		fields.add(valorizzaField(pdAcroForm, "IDENTIFICATIVO_INTERNO", String.valueOf(lettera.getPg_lettera()) + " - " + lettera.getCd_unita_organizzativa(), false));
		fields.add(valorizzaField(pdAcroForm, "DATA VALUTA BENEFICIARIO", "", false));
		fields.add(valorizzaField(pdAcroForm, "CONTO TRANSITORIO NOMINATIVO NOP", "", false));
		fields.add(valorizzaField(pdAcroForm, "PAESE BENEFICIARIO", lettera.getPaese_beneficiario(), false));
		fields.add(valorizzaField(pdAcroForm, "DIVISA", lettera.getDivisa(), false));
		fields.add(valorizzaField(pdAcroForm, "IMPORTO", new java.text.DecimalFormat("#,##0.00").format(lettera.getIm_pagamento()), false));
		fields.add(valorizzaField(pdAcroForm, "IMPORTO IN LETTERE", Utility.NumberToText(lettera.getIm_pagamento()), false));
		fields.add(valorizzaField(pdAcroForm, "NOMINATIVO BENEFICIARIO", lettera.getBeneficiario(), true));
		fields.add(valorizzaField(pdAcroForm, "INDIRIZZO BENEFICIARIO", lettera.getIndirizzo_beneficiario(), true));
		fields.add(valorizzaField(pdAcroForm, "INDIRIZZO BENEFICIARIO_2", "", true));
		fields.add(valorizzaField(pdAcroForm, "ANAGRAFICA BANCA BENEFICIARIA Opzionale", lettera.getIndirizzo(), true));
		fields.add(valorizzaField(pdAcroForm, "IBAN o CONTO BENEFICIARIO", lettera.getIban(), true));
		fields.add(valorizzaField(pdAcroForm, "BIC BANCA BENEFICIARIA o CODICI ROUTING", lettera.getIndirizzo_swift(), false));
		fields.add(valorizzaField(pdAcroForm, "BIC BANCA INTERMEDIARIA Opzionale", Optional.ofNullable(lettera.getBic_banca_intermediaria()).orElse(""), false));
		fields.add(valorizzaField(pdAcroForm, "CAUSALE DEL PAGAMENTO", lettera.getMotivo_pag(), true));
		fields.add(valorizzaField(pdAcroForm, "S", lettera.getCommissioni_spese(), false));
		fields.add(valorizzaField(pdAcroForm, "A", "Scelta4", false));
		fields.add(valorizzaField(pdAcroForm, "IBAN", configurazione.getVal01(), true));
		fields.add(valorizzaField(pdAcroForm, "SPORTELLO", configurazione.getVal02(), false));
		fields.add(valorizzaField(pdAcroForm, "CONTO", configurazione.getVal03(), false));
		fields.add(valorizzaField(pdAcroForm, "LUOGO E DATA", "ROMA " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()), false));

		fields.add(valorizzaField(pdAcroForm,
				"ULTERIORI INFORMAZIONI Opzionale",
				Optional.ofNullable(lettera.getIstruzioni_speciali_1()).orElse("") +
						Optional.ofNullable(lettera.getIstruzioni_speciali_2()).orElse("") +
						Optional.ofNullable(lettera.getIstruzioni_speciali_3()).orElse(""),
				true)
		);
		try {
			pdAcroForm.flatten(fields.stream()
					.filter(pdField -> Optional.ofNullable(pdField).isPresent())
					.collect(Collectors.toList()), true);
		} catch (IllegalArgumentException _ex) {
			throw new ApplicationMessageFormatException("Errore durante la predisposizione, controllare i caratteri inseriti: {0}", _ex.getMessage());
		}
		return document;
	}

	@Override
	@SuppressWarnings({ "unused", "unchecked" })
	public void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException{
		try {
			List<Lettera_pagam_esteroBulk> selectedElements = getSelectedElements(actioncontext);
			if (selectedElements == null || selectedElements.isEmpty())
				throw new ApplicationException("Selezionare almeno un elemento!");
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			final Configurazione_cnrBulk configurazione = Utility.createConfigurazioneCnrComponentSession()
					.getConfigurazione(
							actioncontext.getUserContext(),
							0,
							"*",
							"PAGAMENTO_ESTERO",
							"IBAN_SPORTELLO_CONTO"
					);

			for (Lettera_pagam_esteroBulk lettera : selectedElements) {
				PDDocument document = createDocument(lettera, configurazione);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
				document.save(output);
				document.close();
				SpringUtil.getBean("storeService", StoreService.class).restoreSimpleDocument(
						lettera,
						new ByteArrayInputStream(output.toByteArray()),
						"application/pdf",
						lettera.getCMISFolderName() + ".pdf",
						lettera.getStorePath(),
						true);
				aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO, lettera);
			}
			setMessage("Predisposizione effettuata correttamente.");
		} catch (ApplicationException e) {
			setMessage(e.getMessage());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
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
