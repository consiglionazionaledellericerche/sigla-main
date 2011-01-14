package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00_logsBulk extends Ext_cassiere00_logsBase {

	//private Ext_cassiere00Bulk file;
	private V_ext_cassiere00Bulk file;

public Ext_cassiere00_logsBulk() {
	super();
}
public Ext_cassiere00_logsBulk(java.lang.Integer esercizio,java.lang.String nome_file,java.math.BigDecimal pg_esecuzione) {
	super(esercizio,nome_file,pg_esecuzione);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @return it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public V_ext_cassiere00Bulk getFile() {
	return file;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @return it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public Ext_cassiere00Bulk OLDgetFile() {
	//return file;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @param newFile it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public void OLDsetFile(Ext_cassiere00Bulk newFile) {
	//file = newFile;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @param newFile it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public void setFile(V_ext_cassiere00Bulk newFile) {
	file = newFile;
}
}
