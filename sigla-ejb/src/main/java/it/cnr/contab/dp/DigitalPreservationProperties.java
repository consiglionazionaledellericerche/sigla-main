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

package it.cnr.contab.dp;

public class DigitalPreservationProperties {
	private String digitalPreservationCodAmm;
	private String digitalPreservationCodRegFA;
	private String digitalPreservationCodRegFP;
	public String getDigitalPreservationCodAmm() {
		return digitalPreservationCodAmm;
	}
	public void setDigitalPreservationCodAmm(String digitalPreservationCodAmm) {
		this.digitalPreservationCodAmm = digitalPreservationCodAmm;
	}
	public String getDigitalPreservationCodRegFA() {
		return digitalPreservationCodRegFA;
	}
	public void setDigitalPreservationCodRegFA(String digitalPreservationCodRegFA) {
		this.digitalPreservationCodRegFA = digitalPreservationCodRegFA;
	}
	public String getDigitalPreservationCodRegFP() {
		return digitalPreservationCodRegFP;
	}
	public void setDigitalPreservationCodRegFP(String digitalPreservationCodRegFP) {
		this.digitalPreservationCodRegFP = digitalPreservationCodRegFP;
	}
}
