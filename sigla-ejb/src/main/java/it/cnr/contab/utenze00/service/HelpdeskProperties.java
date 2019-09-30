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

package it.cnr.contab.utenze00.service;

public class HelpdeskProperties {
	private String helpdeskTargetEndpoint;
	private String helpdeskSiglaRestClientUser;
	private String helpdeskSiglaRestClientPassword;
	public String getHelpdeskTargetEndpoint() {
		return helpdeskTargetEndpoint;
	}
	public void setHelpdeskTargetEndpoint(String helpdeskTargetEndpoint) {
		this.helpdeskTargetEndpoint = helpdeskTargetEndpoint;
	}
	public String getHelpdeskSiglaRestClientPassword() {
		return helpdeskSiglaRestClientPassword;
	}
	public void setHelpdeskSiglaRestClientPassword(String helpdeskSiglaRestClientPassword) {
		this.helpdeskSiglaRestClientPassword = helpdeskSiglaRestClientPassword;
	}
	public String getHelpdeskSiglaRestClientUser() {
		return helpdeskSiglaRestClientUser;
	}
	public void setHelpdeskSiglaRestClientUser(String helpdeskSiglaRestClientUser) {
		this.helpdeskSiglaRestClientUser = helpdeskSiglaRestClientUser;
	}
}
