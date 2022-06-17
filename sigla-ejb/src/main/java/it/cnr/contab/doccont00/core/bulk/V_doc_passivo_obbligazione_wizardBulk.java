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

public class V_doc_passivo_obbligazione_wizardBulk extends V_doc_passivo_obbligazioneBulk {
	Mandato_rigaBulk mandatoRiga;

	String descrizioneRigaMandatoWizard;

	public V_doc_passivo_obbligazione_wizardBulk() {
		super();
	}

	public Mandato_rigaBulk getMandatoRiga() {
		return mandatoRiga;
	}

	public void setMandatoRiga(Mandato_rigaBulk mandatoRiga) {
		this.mandatoRiga = mandatoRiga;
	}

	public String getDescrizioneRigaMandatoWizard() {
		return descrizioneRigaMandatoWizard;
	}

	public void setDescrizioneRigaMandatoWizard(String descrizioneRigaMandatoWizard) {
		this.descrizioneRigaMandatoWizard = descrizioneRigaMandatoWizard;
	}
}
