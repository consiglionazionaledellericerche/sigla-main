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
 * Created on Apr 6, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;

public class V_doc_attivo_accertamento_wizardBulk extends V_doc_attivo_accertamentoBulk {
	Reversale_rigaBulk reversaleRiga;

	String descrizioneRigaReversaleWizard;

	public V_doc_attivo_accertamento_wizardBulk() {
		super();
	}

	public Reversale_rigaBulk getReversaleRiga() {
		return reversaleRiga;
	}

	public void setReversaleRiga(Reversale_rigaBulk reversaleRiga) {
		this.reversaleRiga = reversaleRiga;
	}

	public String getDescrizioneRigaReversaleWizard() {
		return descrizioneRigaReversaleWizard;
	}

	public void setDescrizioneRigaReversaleWizard(String descrizioneRigaReversaleWizard) {
		this.descrizioneRigaReversaleWizard = descrizioneRigaReversaleWizard;
	}
}
