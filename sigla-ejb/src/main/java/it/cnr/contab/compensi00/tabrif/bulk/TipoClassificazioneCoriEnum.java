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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;

public enum TipoClassificazioneCoriEnum {
	ANTICIPO(Numerazione_doc_ammBulk.TIPO_ANTICIPO),
	MISSIONE(Numerazione_doc_ammBulk.TIPO_MISSIONE),
	COMPENSO(Numerazione_doc_ammBulk.TIPO_COMPENSO),
	FATTURA_PASSIVA(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA),
	NOTA_CREDITO_PASSIVA(TipoClassificazioneCoriEnum.TIPO_NOTA_CREDITO_PASSIVA),
	NOTA_DEBITO_PASSIVA(TipoClassificazioneCoriEnum.TIPO_NOTA_DEBITO_PASSIVA),
	FATTURA_ATTIVA(Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA),
	NOTA_CREDITO_ATTIVA(TipoClassificazioneCoriEnum.TIPO_NOTA_CREDITO_ATTIVA),
	NOTA_DEBITO_ATTIVA(TipoClassificazioneCoriEnum.TIPO_NOTA_DEBITO_ATTIVA),
	GEN_CORI_ACCANTONAMENTO_ENTRATA(IDocumentoAmministrativoRigaBulk.tipo.GEN_CORA_E.name()),
	GEN_CORI_ACCANTONAMENTO_SPESA("GEN_CORA_S"),
	GEN_CORI_VERSAMENTO_ENTRATA(IDocumentoAmministrativoRigaBulk.tipo.GEN_CORV_E.name()),
	GEN_CORI_VERSAMENTO_SPESA("GEN_CORV_S"),
	GEN_IVA_E(Numerazione_doc_ammBulk.TIPO_GEN_IVA_E),
	GEN_CH_FON(Numerazione_doc_ammBulk.TIPO_GEN_CH_FON),
	GEN_AP_FON(Numerazione_doc_ammBulk.TIPO_GEN_AP_FON),
	GEN_REINTEGRO_FONDO("GEN_RE_FON"),
	GENERICO_S(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S),
	GENERICO_E(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E),
	MANDATO(Numerazione_doc_contBulk.TIPO_MAN),
	REVERSALE(Numerazione_doc_contBulk.TIPO_REV),
	GEN_STIPENDI_SPESA("GEN_STIP_S"),
	REGOLA_E("REGOLA_E");

	private final String value;

	public final static String TIPO_NOTA_CREDITO_PASSIVA = "NOTA_CREDITO_P";
	public final static String TIPO_NOTA_DEBITO_PASSIVA = "NOTA_DEBITO_P";
	public final static String TIPO_NOTA_CREDITO_ATTIVA = "NOTA_CREDITO_A";
	public final static String TIPO_NOTA_DEBITO_ATTIVA = "NOTA_DEBITO_A";

	TipoClassificazioneCoriEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public boolean isAnticipo() {
		return Numerazione_doc_ammBulk.TIPO_ANTICIPO.equals(this.value);
	}

	public boolean isCompenso() {
		return Numerazione_doc_ammBulk.TIPO_COMPENSO.equals(this.value);
	}

	public boolean isMissione() {
		return Numerazione_doc_ammBulk.TIPO_MISSIONE.equals(this.value);
	}

	public boolean isRimborso() {
		return Numerazione_doc_ammBulk.TIPO_RIMBORSO.equals(this.value);
	}

	public boolean isFatturaAttiva() {
		return Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals(this.value);
	}

	public boolean isFatturaPassiva() {
		return Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals(this.value);
	}

	public boolean isNotaCreditoPassiva() {
		return TipoClassificazioneCoriEnum.NOTA_CREDITO_PASSIVA.equals(this);
	}

	public boolean isNotaDebitoPassiva() {
		return TipoClassificazioneCoriEnum.NOTA_DEBITO_PASSIVA.equals(this);
	}

	public boolean isNotaCreditoAttiva() {
		return TipoClassificazioneCoriEnum.NOTA_CREDITO_ATTIVA.equals(this);
	}

	public boolean isNotaDebitoAttiva() {
		return TipoClassificazioneCoriEnum.NOTA_DEBITO_ATTIVA.equals(this);
	}

	public boolean isDocumentoAmministrativoPassivo() {
		return this.isFatturaPassiva() || this.isNotaDebitoPassiva() || this.isNotaCreditoPassiva() || this.isGenericoSpesa() || this.isGenericoStipendiSpesa();
	}

	public boolean isDocumentoAmministrativoAttivo() {
		return this.isFatturaAttiva() || this.isNotaDebitoAttiva() || this.isNotaCreditoAttiva() || this.isGenericoEntrata() || this.isGenericoCoriAccantonamentoEntrata();
	}

	public boolean isMandato() {
		return TipoClassificazioneCoriEnum.MANDATO.equals(this);
	}

	public boolean isReversale() {
		return TipoClassificazioneCoriEnum.REVERSALE.equals(this);
	}

	public boolean isAperturaFondo() {
		return TipoClassificazioneCoriEnum.GEN_AP_FON.equals(this);
	}

	public boolean isChiusuraFondo() {
		return TipoClassificazioneCoriEnum.GEN_CH_FON.equals(this);
	}

	public boolean isGenericoCoriVersamentoSpesa() {
		return TipoClassificazioneCoriEnum.GEN_CORI_VERSAMENTO_SPESA.equals(this);
	}

	public boolean isGenericoCoriAccantonamentoSpesa() {
		return TipoClassificazioneCoriEnum.GEN_CORI_ACCANTONAMENTO_SPESA.equals(this);
	}

	public boolean isGenericoSpesa() {
		return TipoClassificazioneCoriEnum.GENERICO_S.equals(this);
	}

	public boolean isGenericoEntrata() {
		return TipoClassificazioneCoriEnum.GENERICO_E.equals(this);
	}

	public boolean isGenericoStipendiSpesa() {
		return TipoClassificazioneCoriEnum.GEN_STIPENDI_SPESA.equals(this);
	}

	public boolean isGenericoCoriAccantonamentoEntrata() {
		return TipoClassificazioneCoriEnum.GEN_CORI_ACCANTONAMENTO_ENTRATA.equals(this);
	}

	public boolean isGenericoMandatoRegolarizzazione() {
		return TipoClassificazioneCoriEnum.REGOLA_E.equals(this);
	}

	/**
	 * Indica se per il tipo di documento deve essere prevista la registrazione della prima nota contabile
	 */
	public boolean isScritturaEconomicaRequired() {
		return !this.isGenericoCoriAccantonamentoSpesa() &&
				!this.isGenericoCoriAccantonamentoEntrata() &&
				!this.isGenericoStipendiSpesa() &&
				!this.isGenericoMandatoRegolarizzazione();
	}

	public static TipoClassificazioneCoriEnum fromValue(String v) {
		for (TipoClassificazioneCoriEnum c : TipoClassificazioneCoriEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

	/**
	 * Indica quale tipo di conto di economica (costo/ricavo) viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getTipoEconomica() {
		if (this.isDocumentoAmministrativoPassivo())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		if (this.isDocumentoAmministrativoAttivo())
			return Movimento_cogeBulk.TipoRiga.RICAVO.value();
		if (this.isCompenso())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		if (this.isAnticipo())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		if (this.isMissione())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		return null;
	}

	/**
	 * Indica quale tipo di conto patrimoniale (debito/credito) viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getTipoPatrimoniale() {
		if (this.isFatturaPassiva()||this.isGenericoSpesa()||this.isGenericoStipendiSpesa())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isFatturaAttiva()||this.isGenericoEntrata()||this.isGenericoCoriAccantonamentoEntrata())
			return Movimento_cogeBulk.TipoRiga.CREDITO.value();
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.TipoRiga.CREDITO.value();
		if (this.isCompenso())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isAnticipo())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isMissione())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isGenericoCoriVersamentoSpesa())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo economica viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezioneEconomica() {
		if (this.isFatturaPassiva()||this.isGenericoSpesa()||this.isGenericoStipendiSpesa())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isFatturaAttiva()||this.isGenericoEntrata()||this.isGenericoCoriAccantonamentoEntrata())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isCompenso())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isAnticipo())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isMissione())
			return Movimento_cogeBulk.SEZIONE_DARE;
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo iva viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezioneIva() {
		if (this.isFatturaPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isFatturaAttiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo patrimoniale viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezionePatrimoniale() {
		if (this.isFatturaPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isFatturaAttiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isCompenso())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isAnticipo())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isMissione())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isGenericoSpesa()||this.isGenericoStipendiSpesa())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isGenericoEntrata()||this.isGenericoCoriAccantonamentoEntrata())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isGenericoCoriVersamentoSpesa())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		return null;
	}
}
