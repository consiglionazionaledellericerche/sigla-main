package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AbicabKey extends OggettoBulk implements KeyedPersistent {
	// ABI CHAR(5) NOT NULL (PK)
	private java.lang.String abi;

	// CAB CHAR(5) NOT NULL (PK)
	private java.lang.String cab;

public AbicabKey() {
	super();
}
public AbicabKey(java.lang.String abi,java.lang.String cab) {
	super();
	this.abi = abi;
	this.cab = cab;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof AbicabKey)) return false;
	AbicabKey k = (AbicabKey)o;
	if(!compareKey(getAbi(),k.getAbi())) return false;
	if(!compareKey(getCab(),k.getCab())) return false;
	return true;
}
/* 
 * Getter dell'attributo abi
 */
public java.lang.String getAbi() {
	return abi;
}
/* 
 * Getter dell'attributo cab
 */
public java.lang.String getCab() {
	return cab;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getAbi())+
		calculateKeyHashCode(getCab());
}
/* 
 * Setter dell'attributo abi
 */
public void setAbi(java.lang.String abi) {
	this.abi = abi;
}
/* 
 * Setter dell'attributo cab
 */
public void setCab(java.lang.String cab) {
	this.cab = cab;
}
}
