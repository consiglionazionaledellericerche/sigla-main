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