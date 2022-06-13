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

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;

import javax.persistence.Transient;

//Classe utilizzata per gestire la creazione automatica dei mandati/reversali
public class DocumentoGenericoWizardBulk extends Documento_genericoBulk {
	/**
	 * Campi utilizzati nell'ambito dell funzione di creazione automatica mandato
	 */
	@Transient
	TerzoBulk terzoWizardBulk;
	@Transient
	Modalita_pagamentoBulk modalitaPagamentoWizardBulk;
	@Transient
	BancaBulk bancaWizardBulk;

	public DocumentoGenericoWizardBulk() {
		super();
	}

	public DocumentoGenericoWizardBulk(String cd_cds, String cd_tipo_documento_amm, String cd_unita_organizzativa, Integer esercizio, Long pg_documento_generico) {
		super(cd_cds,cd_tipo_documento_amm,cd_unita_organizzativa,esercizio,pg_documento_generico);
		setTipo_documento(new Tipo_documento_ammBulk(cd_tipo_documento_amm));
	}

	public TerzoBulk getTerzoWizardBulk() {
		return terzoWizardBulk;
	}

	public void setTerzoWizardBulk(TerzoBulk terzoWizardBulk) {
		this.terzoWizardBulk = terzoWizardBulk;
	}

	public Modalita_pagamentoBulk getModalitaPagamentoWizardBulk() {
		return modalitaPagamentoWizardBulk;
	}

	public void setModalitaPagamentoWizardBulk(Modalita_pagamentoBulk modalitaPagamentoWizardBulk) {
		this.modalitaPagamentoWizardBulk = modalitaPagamentoWizardBulk;
	}

	public BancaBulk getBancaWizardBulk() {
		return bancaWizardBulk;
	}

	public void setBancaWizardBulk(BancaBulk bancaWizardBulk) {
		this.bancaWizardBulk = bancaWizardBulk;
	}
}