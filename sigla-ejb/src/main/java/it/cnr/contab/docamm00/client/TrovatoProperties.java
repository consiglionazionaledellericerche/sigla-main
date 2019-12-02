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

package it.cnr.contab.docamm00.client;

public class TrovatoProperties {
	private String trovatoTargetEndpoint;
	private String trovatoSiglaRestClientUser;
	private String trovatoSiglaRestClientPassword;
	public String getTrovatoTargetEndpoint() {
		return trovatoTargetEndpoint;
	}
	public void setTrovatoTargetEndpoint(String trovatoTargetEndpoint) {
		this.trovatoTargetEndpoint = trovatoTargetEndpoint;
	}
	public String getTrovatoSiglaRestClientPassword() {
		return trovatoSiglaRestClientPassword;
	}
	public void setTrovatoSiglaRestClientPassword(String trovatoSiglaRestClientPassword) {
		this.trovatoSiglaRestClientPassword = trovatoSiglaRestClientPassword;
	}
	public String getTrovatoSiglaRestClientUser() {
		return trovatoSiglaRestClientUser;
	}
	public void setTrovatoSiglaRestClientUser(String trovatoSiglaRestClientUser) {
		this.trovatoSiglaRestClientUser = trovatoSiglaRestClientUser;
	}
}
