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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_comunicati_fp_detBase extends Incarichi_comunicati_fp_detKey implements Keyed {

	//    TIPO_RECORD_DET VARCHAR(3)
	private java.lang.String tipo_record_det;

	//    IMPORTO_PAG DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_pag;

	//    ESITO_INCARICO_PAGAMENTO VARCHAR(5)
	private java.lang.String esito_incarico_pagamento;

	//    ERR_ANNO_PAG VARCHAR(2000)
	private java.lang.String err_anno_pag;

	//    ERR_SEMESTRE_PAG VARCHAR(2000)
	private java.lang.String err_semestre_pag;

	//    ERR_IMPORTO_PAG VARCHAR(2000)
	private java.lang.String err_importo_pag;

	
	public Incarichi_comunicati_fp_detBase() {
		super();
	}

	public Incarichi_comunicati_fp_detBase(java.lang.Integer esercizio_repertorio, java.lang.Long pg_repertorio, java.lang.String tipo_record, java.lang.Long pg_record, java.lang.Integer anno_pag, java.lang.Integer semestre_pag) {
		super(esercizio_repertorio, pg_repertorio, tipo_record, pg_record, anno_pag, semestre_pag);
	}

	public java.lang.String getTipo_record_det() {
		return tipo_record_det;
	}

	public void setTipo_record_det(java.lang.String tipoRecordDet) {
		tipo_record_det = tipoRecordDet;
	}

	public java.math.BigDecimal getImporto_pag() {
		return importo_pag;
	}

	public void setImporto_pag(java.math.BigDecimal importoPag) {
		importo_pag = importoPag;
	}

	public java.lang.String getEsito_incarico_pagamento() {
		return esito_incarico_pagamento;
	}

	public void setEsito_incarico_pagamento(java.lang.String esitoIncaricoPagamento) {
		esito_incarico_pagamento = esitoIncaricoPagamento;
	}

	public java.lang.String getErr_anno_pag() {
		return err_anno_pag;
	}

	public void setErr_anno_pag(java.lang.String errAnnoPag) {
		err_anno_pag = errAnnoPag;
	}

	public java.lang.String getErr_semestre_pag() {
		return err_semestre_pag;
	}

	public void setErr_semestre_pag(java.lang.String errSemestrePag) {
		err_semestre_pag = errSemestrePag;
	}

	public java.lang.String getErr_importo_pag() {
		return err_importo_pag;
	}

	public void setErr_importo_pag(java.lang.String errImportoPag) {
		err_importo_pag = errImportoPag;
	}
}
