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
import it.cnr.contab.docamm00.fatturapa.bulk.StatoDocumentoEleEnum;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.xmlbnl.Mandato;

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
		return TipoDocumentoEnum.TIPO_NOTA_CREDITO_PASSIVA.equals(this.value);
	}

	public boolean isNotaDebitoPassiva() {
		return TipoDocumentoEnum.TIPO_NOTA_DEBITO_PASSIVA.equals(this.value);
	}

	public boolean isNotaCreditoAttiva() {
		return TipoDocumentoEnum.TIPO_NOTA_CREDITO_ATTIVA.equals(this.value);
	}

	public boolean isNotaDebitoAttiva() {
		return TipoDocumentoEnum.TIPO_NOTA_DEBITO_ATTIVA.equals(this.value);
	}

	public boolean isDocumentoAmministrativoPassivo() {
		return this.isFatturaPassiva() || this.isNotaDebitoPassiva() || this.isNotaCreditoPassiva();
	}

	public boolean isDocumentoAmministrativoAttivo() {
		return this.isFatturaAttiva() || this.isNotaDebitoAttiva() || this.isNotaCreditoAttiva();
	}

	public boolean isMandato() {
		return TipoDocumentoEnum.MANDATO.equals(this.value);
	}

	public boolean isReversale() {
		return TipoDocumentoEnum.REVERSALE.equals(this.value);
	}

	public static TipoDocumentoEnum fromValue(String v) {
		for (TipoDocumentoEnum c : TipoDocumentoEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

	public String getSezioneCostoRicavo() {
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
		return null;
	}
}