package it.cnr.contab.config00.blob.bulk;        

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class PostItKey extends OggettoBulk implements KeyedPersistent {
	// ID NUMBER(10) NOT NULL (PK)
	private java.lang.Integer id;

public PostItKey() {
	super();
}
public PostItKey(java.lang.Integer id) {
	super();
	this.id = id;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof PostItKey)) return false;
	PostItKey k = (PostItKey)o;
	if(!compareKey(getId(),k.getId())) return false;
	return true;
}
/* 
 * Getter dell'attributo id
 */
public java.lang.Integer getId() {
	return id;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId());
}
/* 
 * Setter dell'attributo id
 */
public void setId(java.lang.Integer id) {
	this.id = id;
}
}