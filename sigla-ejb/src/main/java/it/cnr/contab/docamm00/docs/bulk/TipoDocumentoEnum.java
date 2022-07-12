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

import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	TipoDocumentoEnum(String value) {
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
		return TipoDocumentoEnum.NOTA_CREDITO_PASSIVA.equals(this);
	}

	public boolean isNotaDebitoPassiva() {
		return TipoDocumentoEnum.NOTA_DEBITO_PASSIVA.equals(this);
	}

	public boolean isNotaCreditoAttiva() {
		return TipoDocumentoEnum.NOTA_CREDITO_ATTIVA.equals(this);
	}

	public boolean isNotaDebitoAttiva() {
		return TipoDocumentoEnum.NOTA_DEBITO_ATTIVA.equals(this);
	}

	/**
	 * Indica se il tipo di documento rientra tra quelli amministrativi passivi (fattura, nota credito e nota debito)
	 * @return true se il tipo di documento rientra tra quelli amministrativi passivi (fattura, nota credito e nota debito)
	 */
	public boolean isDocumentoAmministrativoPassivo() {
		return this.isFatturaPassiva() || this.isNotaDebitoPassiva() || this.isNotaCreditoPassiva();
	}

	/**
	 * Indica se il tipo di documento rientra tra quelli amministrativi attivi (fattura, nota credito e nota debito)
	 * @return true se il tipo di documento rientra tra quelli amministrativi attivi (fattura, nota credito e nota debito)
	 */
	public boolean isDocumentoAmministrativoAttivo() {
		return this.isFatturaAttiva() || this.isNotaDebitoAttiva() || this.isNotaCreditoAttiva();
	}

	/**
	 * Indica se il tipo di documento rientra tra quelli generici passivi
	 * @return true se il tipo di documento rientra tra quelli generici passivi
	 */
	public boolean isDocumentoGenericoPassivo() {
		return this.isGenericoSpesa() || this.isAperturaFondo() || this.isGenericoCoriAccantonamentoSpesa() ||
				this.isGenericoCoriVersamentoSpesa() || this.isReintegroFondo() || this.isGenericoStipendiSpesa();
	}

	/**
	 * Indica se il tipo di documento rientra tra quelli generici attivi
	 * @return true se il tipo di documento rientra tra quelli generici attivi
	 */
	public boolean isDocumentoGenericoAttivo() {
		return this.isGenericoEntrata() || this.isChiusuraFondo() || this.isGenericoCoriAccantonamentoEntrata() ||
				this.isGenericoCoriVersamentoEntrata() || this.isGenericoEntrataIncassoIva() || this.isGenericoMandatoRegolarizzazione();
	}

	/**
	 * Indica se il tipo di documento è passivo (amministrativo o generico)
	 * @return true se il tipo di documento è passivo (amministrativo o generico)
	 */
	public boolean isDocumentoPassivo() {
		return this.isDocumentoAmministrativoPassivo() || this.isDocumentoGenericoPassivo();
	}

	/**
	 * Indica se il tipo di documento è attivo (amministrativo o generico)
	 * @return true se il tipo di documento è attivo (amministrativo o generico)
	 */
	public boolean isDocumentoAttivo() {
		return this.isDocumentoAmministrativoAttivo() || this.isDocumentoGenericoAttivo();
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

	public boolean isReintegroFondo() {
		return TipoDocumentoEnum.GEN_REINTEGRO_FONDO.equals(this);
	}

	public boolean isChiusuraFondo() {
		return TipoDocumentoEnum.GEN_CH_FON.equals(this);
	}

	public boolean isGenericoCoriVersamentoSpesa() {
		return TipoDocumentoEnum.GEN_CORI_VERSAMENTO_SPESA.equals(this);
	}

	public boolean isGenericoCoriVersamentoEntrata() {
		return TipoDocumentoEnum.GEN_CORI_VERSAMENTO_ENTRATA.equals(this);
	}

	public boolean isGenericoCoriAccantonamentoSpesa() {
		return TipoDocumentoEnum.GEN_CORI_ACCANTONAMENTO_SPESA.equals(this);
	}

	public boolean isGenericoEntrataIncassoIva() {
		return TipoDocumentoEnum.GEN_IVA_E.equals(this);
	}

	public boolean isGenericoSpesa() {
		return TipoDocumentoEnum.GENERICO_S.equals(this);
	}

	public boolean isGenericoEntrata() {
		return TipoDocumentoEnum.GENERICO_E.equals(this);
	}

	public boolean isGenericoStipendiSpesa() {
		return TipoDocumentoEnum.GEN_STIPENDI_SPESA.equals(this);
	}

	public boolean isGenericoCoriAccantonamentoEntrata() {
		return TipoDocumentoEnum.GEN_CORI_ACCANTONAMENTO_ENTRATA.equals(this);
	}

	public boolean isGenericoMandatoRegolarizzazione() {
		return TipoDocumentoEnum.REGOLA_E.equals(this);
	}

	/**
	 * Indica se per il tipo di documento deve essere prevista la registrazione della prima nota contabile
	 */
	public boolean isScritturaEconomicaRequired() {
		return !this.isGenericoCoriAccantonamentoSpesa() &&
				!this.isGenericoCoriAccantonamentoEntrata() &&
				!this.isGenericoStipendiSpesa() &&
				!this.isGenericoMandatoRegolarizzazione() &&
				!this.isGenericoCoriVersamentoSpesa() &&
				!this.isGenericoEntrataIncassoIva() &&
				!this.isChiusuraFondo();
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
		if (this.isDocumentoAmministrativoPassivo())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		if (this.isDocumentoAmministrativoAttivo())
			return Movimento_cogeBulk.TipoRiga.RICAVO.value();
		if (this.isCompenso())
			return Movimento_cogeBulk.TipoRiga.COSTO.value();
		if (this.isAnticipo())
			return Movimento_cogeBulk.TipoRiga.CREDITO.value();
		if (this.isAperturaFondo())
			return Movimento_cogeBulk.TipoRiga.CREDITO.value();
		if (this.isChiusuraFondo())
			return Movimento_cogeBulk.TipoRiga.CREDITO.value();
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
		if (this.isAperturaFondo())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isChiusuraFondo())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isMissione())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isGenericoCoriVersamentoSpesa())
			return Movimento_cogeBulk.TipoRiga.DEBITO.value();
		if (this.isGenericoCoriAccantonamentoSpesa())
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
		if (this.isDocumentoPassivo())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isDocumentoAttivo())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo iva viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezioneIva() {
		if (this.isFatturaPassiva() || this.isNotaDebitoPassiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isFatturaAttiva() || this.isNotaDebitoAttiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isCompenso())
			return Movimento_cogeBulk.SEZIONE_DARE;
		return null;
	}

	/**
	 * Indica quale sezione (Dare/Avere) per il conto di tipo patrimoniale viene movimentato dalla scrittura PN del tipo documento
	 */
	public String getSezionePatrimoniale() {
		if (this.isFatturaPassiva() || this.isNotaDebitoPassiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isNotaCreditoPassiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isFatturaAttiva() || this.isNotaDebitoAttiva())
			return Movimento_cogeBulk.SEZIONE_DARE;
		if (this.isNotaCreditoAttiva())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isCompenso())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isAnticipo())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isMissione())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isDocumentoPassivo())
			return Movimento_cogeBulk.SEZIONE_AVERE;
		if (this.isDocumentoAttivo())
			return Movimento_cogeBulk.SEZIONE_DARE;
		return null;
	}

	//Ritorna l'ordine di costruzione dei documenti.... ad esempio la fattura viene proma del mandato
	public int getOrdineCostruzione() {
		if (this.isAnticipo())
			return 1;
		if (this.isMissione())
			return 2;
		if (this.isCompenso())
			return 3;
		if (this.isDocumentoPassivo() || this.isDocumentoAttivo())
			return 4;
		if (this.isMandato())
			return 5;
		return 6;
	}
}
