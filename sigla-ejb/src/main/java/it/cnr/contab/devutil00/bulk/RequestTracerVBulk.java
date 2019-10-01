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

package it.cnr.contab.devutil00.bulk;

public class RequestTracerVBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.util.List tracingUsers;
/**
 * 
 * @return java.util.Set
 */
public java.util.List getTracingUsers() {
	return tracingUsers;
}
/**
 * 
 * @param newTracingUsers java.util.Set
 */
public void setTracingUsers(java.util.Enumeration tracingUsers) {
	this.tracingUsers = new java.util.ArrayList();
	if (tracingUsers == null) return;
	for (;tracingUsers.hasMoreElements();)
		this.tracingUsers.add(tracingUsers.nextElement());
}
/**
 * 
 * @param newTracingUsers java.util.Set
 */
public void setTracingUsers(java.util.List newTracingUsers) {
	tracingUsers = newTracingUsers;
}
}
