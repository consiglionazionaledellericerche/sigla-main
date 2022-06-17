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

package it.cnr.contab.doccont00.core;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;

import java.io.Serializable;

public class AccertamentoWizard implements Serializable {
	Accertamento_scadenzarioBulk accertamentoScadenzarioBulk;

	TerzoBulk terzoWizardBulk;

	Modalita_pagamentoBulk modalitaPagamentoWizardBulk;

	BancaBulk bancaWizardBulk;

	String descrizioneWizard;

	String descrizioneRigaDocumentoWizard;

	String descrizioneRigaReversaleWizard;

	public AccertamentoWizard() {
		super();
	}

	public AccertamentoWizard(Accertamento_scadenzarioBulk accertamentoScadenzarioBulk ) {
		this.accertamentoScadenzarioBulk = accertamentoScadenzarioBulk;
	}

	public Accertamento_scadenzarioBulk getAccertamentoScadenzarioBulk() {
		return accertamentoScadenzarioBulk;
	}

	public void setAccertamentoScadenzarioBulk(Accertamento_scadenzarioBulk accertamentoScadenzarioBulk) {
		this.accertamentoScadenzarioBulk = accertamentoScadenzarioBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public TerzoBulk getTerzoWizardBulk() {
		return terzoWizardBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public void setTerzoWizardBulk(TerzoBulk terzoWizardBulk) {
		this.terzoWizardBulk = terzoWizardBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public Modalita_pagamentoBulk getModalitaPagamentoWizardBulk() {
		return modalitaPagamentoWizardBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public void setModalitaPagamentoWizardBulk(Modalita_pagamentoBulk modalitaPagamentoWizardBulk) {
		this.modalitaPagamentoWizardBulk = modalitaPagamentoWizardBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public BancaBulk getBancaWizardBulk() {
		return bancaWizardBulk;
	}

	/**
	 * Campo utilizzato nell'ambito dell funzione di creazione automatica mandato
	 */
	public void setBancaWizardBulk(BancaBulk bancaWizardBulk) {
		this.bancaWizardBulk = bancaWizardBulk;
	}

	public String getDescrizioneWizard() {
		return descrizioneWizard;
	}

	public void setDescrizioneWizard(String descrizioneWizard) {
		this.descrizioneWizard = descrizioneWizard;
	}

	public String getDescrizioneRigaDocumentoWizard() {
		return descrizioneRigaDocumentoWizard;
	}

	public void setDescrizioneRigaDocumentoWizard(String descrizioneRigaDocumentoWizard) {
		this.descrizioneRigaDocumentoWizard = descrizioneRigaDocumentoWizard;
	}

	public String getDescrizioneRigaReversaleWizard() {
		return descrizioneRigaReversaleWizard;
	}

	public void setDescrizioneRigaReversaleWizard(String descrizioneRigaReversaleWizard) {
		this.descrizioneRigaReversaleWizard = descrizioneRigaReversaleWizard;
	}
}
