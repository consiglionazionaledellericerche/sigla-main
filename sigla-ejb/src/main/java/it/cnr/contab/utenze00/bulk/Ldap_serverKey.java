/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 08/02/2007
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ldap_serverKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String hostname;
	private java.lang.Integer port;
	public Ldap_serverKey() {
		super();
	}
	public Ldap_serverKey(java.lang.String hostname, java.lang.Integer port) {
		super();
		this.hostname=hostname;
		this.port=port;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ldap_serverKey)) return false;
		Ldap_serverKey k = (Ldap_serverKey) o;
		if (!compareKey(getHostname(), k.getHostname())) return false;
		if (!compareKey(getPort(), k.getPort())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getHostname());
		i = i + calculateKeyHashCode(getPort());
		return i;
	}
	public void setHostname(java.lang.String hostname)  {
		this.hostname=hostname;
	}
	public java.lang.String getHostname() {
		return hostname;
	}
	public void setPort(java.lang.Integer port)  {
		this.port=port;
	}
	public java.lang.Integer getPort() {
		return port;
	}
}