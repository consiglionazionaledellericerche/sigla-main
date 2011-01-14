package it.cnr.contab.logregistry00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Log_registryKey extends OggettoBulk  implements KeyedPersistent {
	// NOME_TABLE_SRC VARCHAR(30) NOT NULL (PK)
	private java.lang.String nome_table_src;

public Log_registryKey() {
	super();
}
public Log_registryKey(java.lang.String nome_table_src) {
	super();
	this.nome_table_src = nome_table_src;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Log_registryKey)) return false;
	Log_registryKey k = (Log_registryKey)o;
	if(!compareKey(getNome_table_src(),k.getNome_table_src())) return false;
	return true;
}
/* 
 * Getter dell'attributo nome_table_src
 */
public java.lang.String getNome_table_src() {
	return nome_table_src;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getNome_table_src());
}
/* 
 * Setter dell'attributo nome_table_src
 */
public void setNome_table_src(java.lang.String nome_table_src) {
	this.nome_table_src = nome_table_src;
}
}
