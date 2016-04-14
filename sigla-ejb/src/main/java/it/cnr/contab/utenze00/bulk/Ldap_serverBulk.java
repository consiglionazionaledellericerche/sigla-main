/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 08/02/2007
 */
package it.cnr.contab.utenze00.bulk;
public class Ldap_serverBulk extends Ldap_serverBase {
	public Ldap_serverBulk() {
		super();
	}
	public Ldap_serverBulk(java.lang.String hostname, java.lang.Integer port) {
		super(hostname, port);
	}

	public boolean isAttivo() {
		return getFl_attivo() != null && getFl_attivo().booleanValue();
	}
	public boolean isMaster() {
		return getFl_master() != null && getFl_master().booleanValue();
	}
}