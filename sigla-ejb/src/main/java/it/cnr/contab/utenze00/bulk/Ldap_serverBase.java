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
 * Date 08/02/2007
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ldap_serverBase extends Ldap_serverKey implements Keyed {
//    FL_ATTIVO VARCHAR(1) NOT NULL
	private java.lang.Boolean fl_attivo;
 
//    FL_MASTER VARCHAR(1) NOT NULL
	private java.lang.Boolean fl_master;
 
//    LIVELLO_PRIORITA DECIMAL(4,0) NOT NULL
	private java.lang.Integer livello_priorita;
 
//  FL_MASTER VARCHAR(1) NOT NULL
	private java.lang.Boolean fl_ssl;

	public Ldap_serverBase() {
		super();
	}
	public Ldap_serverBase(java.lang.String hostname, java.lang.Integer port) {
		super(hostname, port);
	}
	public java.lang.Boolean getFl_attivo() {
		return fl_attivo;
	}
	public void setFl_attivo(java.lang.Boolean fl_attivo)  {
		this.fl_attivo=fl_attivo;
	}
	public java.lang.Boolean getFl_master() {
		return fl_master;
	}
	public void setFl_master(java.lang.Boolean fl_master)  {
		this.fl_master=fl_master;
	}
	public java.lang.Integer getLivello_priorita() {
		return livello_priorita;
	}
	public void setLivello_priorita(java.lang.Integer livello_priorita)  {
		this.livello_priorita=livello_priorita;
	}
	public java.lang.Boolean getFl_ssl() {
		return fl_ssl;
	}
	public void setFl_ssl(java.lang.Boolean fl_ssl) {
		this.fl_ssl = fl_ssl;
	}
}