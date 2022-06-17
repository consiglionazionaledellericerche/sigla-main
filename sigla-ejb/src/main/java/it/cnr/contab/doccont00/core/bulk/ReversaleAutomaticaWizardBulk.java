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
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;


import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.docamm00.docs.bulk.DocumentoGenericoWizardBulk;
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.doccont00.bp.MandatoAutomaticoWizardBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.util.*;

public class ReversaleAutomaticaWizardBulk extends ReversaleIBulk {
	private java.util.Collection reversaliColl = new java.util.LinkedList();

	private Collection accertamentiColl = Collections.EMPTY_LIST;
	protected Collection docAttiviColl = new java.util.LinkedList();

	protected Collection accertamentiSelezionatiColl = new java.util.LinkedList();
	protected Collection<V_doc_attivo_accertamento_wizardBulk> docAttiviSelezionatiColl = new java.util.LinkedList();

	private DocumentoGenericoWizardBulk modelloDocumento;

	private String ti_accertamenti;
	private String ti_automatismo;

	protected BancaBulk banca = new BancaBulk();
	protected Modalita_pagamentoBulk modalita_pagamento = new Modalita_pagamentoBulk();
	protected List modalita_pagamentoOptions;
	protected List bancaOptions;

	//Indica se la reversale da generare deve essere divisa tra competenza/residuo o unico
	private boolean flGeneraReversaleUnica;

	public final static String AUTOMATISMO_DA_ACCERTAMENTI	= "A";
	public final static String AUTOMATISMO_DA_DOCATTIVI	= "D";

	public final static String ACCERTAMENTI_TIPO_COMPETENZA	= "C";
	public final static String ACCERTAMENTI_TIPO_RESIDUO		= "R";
	public final static String ACCERTAMENTI_TIPO_ALL			= "A";
	public final static Dictionary tipoImpegniKeys;

	static
	{
		tipoImpegniKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoImpegniKeys.put(ACCERTAMENTI_TIPO_COMPETENZA,	"Competenza");
		tipoImpegniKeys.put(ACCERTAMENTI_TIPO_RESIDUO,		"Residuo");
		tipoImpegniKeys.put(ACCERTAMENTI_TIPO_ALL,			"Tutti");
	};

	public Collection<ReversaleBulk> getReversaliColl() {
		return reversaliColl;
	}

	public void setReversaliColl(Collection reversaliColl) {
		this.reversaliColl = reversaliColl;
	}

	public Collection getAccertamentiColl() {
		return accertamentiColl;
	}

	public void setAccertamentiColl(Collection accertamentiColl) {
		this.accertamentiColl = accertamentiColl;
	}

	@Override
	public Collection getDocAttiviColl() {
		return docAttiviColl;
	}

	@Override
	public void setDocAttiviColl(Collection docAttiviColl) {
		this.docAttiviColl = docAttiviColl;
	}

	public Collection getAccertamentiSelezionatiColl() {
		return accertamentiSelezionatiColl;
	}

	public void setAccertamentiSelezionatiColl(Collection accertamentiSelezionatiColl) {
		this.accertamentiSelezionatiColl = accertamentiSelezionatiColl;
	}

	public Collection<V_doc_attivo_accertamento_wizardBulk> getDocAttiviSelezionatiColl() {
		return docAttiviSelezionatiColl;
	}

	public void setDocAttiviSelezionatiColl(Collection<V_doc_attivo_accertamento_wizardBulk> docAttiviSelezionatiColl) {
		this.docAttiviSelezionatiColl = docAttiviSelezionatiColl;
	}

	public DocumentoGenericoWizardBulk getModelloDocumento() {
		return modelloDocumento;
	}

	public void setModelloDocumento(DocumentoGenericoWizardBulk modelloDocumento) {
		this.modelloDocumento = modelloDocumento;
	}

	public String getTi_automatismo() {
		return ti_automatismo;
	}

	public void setTi_automatismo(String ti_automatismo) {
		this.ti_automatismo = ti_automatismo;
	}

	public String getTi_accertamenti() {
		return ti_accertamenti;
	}

	public void setTi_accertamenti(String ti_accertamenti) {
		this.ti_accertamenti = ti_accertamenti;
	}

	public boolean isAutomatismoDaAccertamenti() {
		return getTi_automatismo().equals(AUTOMATISMO_DA_ACCERTAMENTI);
	}

	public boolean isAutomatismoDaDocumentiAttivi() {
		return getTi_automatismo().equals(AUTOMATISMO_DA_DOCATTIVI);
	}

	public boolean isFlGeneraReversaleUnica() {
		return flGeneraReversaleUnica;
	}

	public void setFlGeneraReversaleUnica(boolean flGeneraReversaleUnica) {
		this.flGeneraReversaleUnica = flGeneraReversaleUnica;
	}

	public BancaBulk getBanca() {
		return banca;
	}

	public void setBanca(BancaBulk banca) {
		this.banca = banca;
	}

	public Modalita_pagamentoBulk getModalita_pagamento() {
		return modalita_pagamento;
	}

	public void setModalita_pagamento(Modalita_pagamentoBulk modalita_pagamento) {
		this.modalita_pagamento = modalita_pagamento;
	}

	public List getModalita_pagamentoOptions() {
		return modalita_pagamentoOptions;
	}

	public void setModalita_pagamentoOptions(List modalita_pagamentoOptions) {
		this.modalita_pagamentoOptions = modalita_pagamentoOptions;
	}

	public static ReversaleAutomaticaWizardBulk createBy(MandatoAutomaticoWizardBulk mandatoWizard) {
		ReversaleAutomaticaWizardBulk reversaleWizard = new ReversaleAutomaticaWizardBulk();
		reversaleWizard.setTi_automatismo(ReversaleAutomaticaWizardBulk.AUTOMATISMO_DA_ACCERTAMENTI);
		reversaleWizard.setEsercizio(mandatoWizard.getEsercizio());
		reversaleWizard.setCds(mandatoWizard.getCds());
		reversaleWizard.setUnita_organizzativa(mandatoWizard.getUnita_organizzativa());
		reversaleWizard.setCd_cds_origine(mandatoWizard.getCd_cds_origine());
		reversaleWizard.setCd_uo_origine(mandatoWizard.getCd_uo_origine());
		reversaleWizard.setDt_emissione(mandatoWizard.getDt_emissione());
		reversaleWizard.setUser(mandatoWizard.getUser());

		Reversale_terzoBulk reversaleTerzo = new Reversale_terzoBulk();
		reversaleTerzo.setTipoBollo(mandatoWizard.getMandato_terzo().getTipoBollo());
		reversaleTerzo.setTerzo(mandatoWizard.getMandato_terzo().getTerzo());
		reversaleWizard.setReversale_terzo(reversaleTerzo);

		DocumentoGenericoWizardBulk modelloDocumento = new DocumentoGenericoWizardBulk();

		modelloDocumento.setEsercizio(mandatoWizard.getModelloDocumento().getEsercizio());
		modelloDocumento.setCd_cds(mandatoWizard.getModelloDocumento().getCd_cds());
		modelloDocumento.setCd_unita_organizzativa(mandatoWizard.getModelloDocumento().getCd_unita_organizzativa());
		modelloDocumento.setCd_cds_origine(mandatoWizard.getModelloDocumento().getCd_cds_origine());
		modelloDocumento.setCd_uo_origine(mandatoWizard.getModelloDocumento().getCd_uo_origine());
		modelloDocumento.setTipo_documento(mandatoWizard.getModelloDocumento().getTipo_documento());
		modelloDocumento.setData_registrazione(mandatoWizard.getModelloDocumento().getData_registrazione());
		modelloDocumento.setDt_da_competenza_coge(mandatoWizard.getModelloDocumento().getDt_da_competenza_coge());
		modelloDocumento.setDt_a_competenza_coge(mandatoWizard.getModelloDocumento().getDt_a_competenza_coge());
		modelloDocumento.setDs_documento_generico(mandatoWizard.getModelloDocumento().getDs_documento_generico());
		modelloDocumento.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
		modelloDocumento.setUser(mandatoWizard.getModelloDocumento().getUser());

		reversaleWizard.setModelloDocumento(modelloDocumento);

		return reversaleWizard;
	}
}
