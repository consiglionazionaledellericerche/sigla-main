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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;

public enum TipoDocumentoEnum {
	ANTICIPO(Numerazione_doc_ammBulk.TIPO_ANTICIPO),
	MISSIONE(Numerazione_doc_ammBulk.TIPO_MISSIONE),
	COMPENSO(Numerazione_doc_ammBulk.TIPO_COMPENSO),
	FATTURA_PASSIVA(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA),
	NOTA_CREDITO_PASSIVA(TipoDocumentoEnum.TIPO_NOTA_CREDITO_PASSIVA),
	NOTA_DEBITO_PASSIVA(TipoDocumentoEnum.TIPO_NOTA_DEBITO_PASSIVA),
	FATTURA_ATTIVA(Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA),
	NOTA_CREDITO_ATTIVA(TipoDocumentoEnum.TIPO_NOTA_CREDITO_ATTIVA),
	NOTA_DEBITO_ATTIVA(TipoDocumentoEnum.TIPO_NOTA_DEBITO_ATTIVA),
	GEN_CORA_E(IDocumentoAmministrativoRigaBulk.tipo.GEN_CORA_E.name()),
	GEN_CORV_E(IDocumentoAmministrativoRigaBulk.tipo.GEN_CORV_E.name()),
	GEN_CORI_VER_SPESA("GEN_CORV_S"),
	GEN_IVA_E(Numerazione_doc_ammBulk.TIPO_GEN_IVA_E),
	GEN_CH_FON(Numerazione_doc_ammBulk.TIPO_GEN_CH_FON),
	GEN_AP_FON(Numerazione_doc_ammBulk.TIPO_GEN_AP_FON),
	GENERICO_S(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S),
	GENERICO_E(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E),
	MANDATO(Numerazione_doc_contBulk.TIPO_MAN),
	REVERSALE(Numerazione_doc_contBulk.TIPO_REV);

	private final String value;

	public final static String TIPO_NOTA_CREDITO_PASSIVA = "NOTA_CREDITO_P";
	public final static String TIPO_NOTA_DEBITO_PASSIVA = "NOTA_DEBITO_P";
	public final static String TIPO_NOTA_CREDITO_ATTIVA = "NOTA_CREDITO_A";
	public final static String TIPO_NOTA_DEBITO_ATTIVA = "NOTA_DEBITO_A";

	TipoDocumentoEnum(String value) {
		this.value = value;
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
		return TipoDocumentoEnum.TIPO_NOTA_CREDITO_PASSIVA.equals(this);
	}

	public boolean isNotaDebitoPassiva() {
		return TipoDocumentoEnum.TIPO_NOTA_DEBITO_PASSIVA.equals(this);
	}

	public boolean isNotaCreditoAttiva() {
		return TipoDocumentoEnum.TIPO_NOTA_CREDITO_ATTIVA.equals(this);
	}

	public boolean isNotaDebitoAttiva() {
		return TipoDocumentoEnum.TIPO_NOTA_DEBITO_ATTIVA.equals(this);
	}

	public boolean isDocumentoAmministrativoPassivo() {
		return this.isFatturaPassiva() || this.isNotaDebitoPassiva() || this.isNotaCreditoPassiva() || this.isGenericoSpesa();
	}

	public boolean isDocumentoAmministrativoAttivo() {
		return this.isFatturaAttiva() || this.isNotaDebitoAttiva() || this.isNotaCreditoAttiva() || this.isGenericoEntrata();
	}

	public boolean isMandato() {
		return TipoDocumentoEnum.MANDATO.equals(this);
	}

	public boolean isReversale() {
		return TipoDocumentoEnum.REVERSALE.equals(this);
	}

	public boolean isAperturaFondo() {
		return TipoDocumentoEnum.GEN_AP_FON.equals(this);
	}

	public boolean isChiusuraFondo() {
		return TipoDocumentoEnum.GEN_CH_FON.equals(this);
	}

	public boolean isGenericoCoriVersamentoSpesa() {
		return TipoDocumentoEnum.GEN_CORI_VER_SPESA.equals(this);
	}

	public boolean isGenericoSpesa() {
		return TipoDocumentoEnum.GENERICO_S.equals(this);
	}

	public boolean isGenericoEntrata() {
		return TipoDocumentoEnum.GENERICO_E.equals(this);
	}

	public static TipoDocumentoEnum fromValue(String v) {
		for (TipoDocumentoEnum c : TipoDocumentoEnum.values()) {
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
		if (this.isFatturaPassiva())
			return Movimento_cogeBulk.TIPO_COSTO;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.TIPO_COSTO;
		if (this.isFatturaAttiva())
			return Movimento_cogeBulk.TIPO_RICAVO;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.TIPO_RICAVO;
		if (this.isCompenso())
			return Movimento_cogeBulk.TIPO_COSTO;
		if (this.isAnticipo())
			return Movimento_cogeBulk.TIPO_COSTO;
		if (this.isMissione())
			return Movimento_cogeBulk.TIPO_COSTO;
		return null;
	}

	/**
	 * Indica quale tipo di conto patrimoniale (debito/credito) viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getTipoPatrimoniale() {
		if (this.isFatturaPassiva())
			return Movimento_cogeBulk.TIPO_DEBITO;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.TIPO_DEBITO;
		if (this.isFatturaAttiva())
			return Movimento_cogeBulk.TIPO_CREDITO;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.TIPO_CREDITO;
		if (this.isCompenso())
			return Movimento_cogeBulk.TIPO_DEBITO;
		if (this.isAnticipo())
			return Movimento_cogeBulk.TIPO_DEBITO;
		if (this.isMissione())
			return Movimento_cogeBulk.TIPO_DEBITO;
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo economica viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezioneEconomica() {
		if (this.isFatturaPassiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isFatturaAttiva())
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
		if (this.isGenericoSpesa())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isGenericoEntrata())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isGenericoCoriVersamentoSpesa())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		return null;
	}
}