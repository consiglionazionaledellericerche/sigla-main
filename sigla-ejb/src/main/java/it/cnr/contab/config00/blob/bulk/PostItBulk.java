package it.cnr.contab.config00.blob.bulk;        

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class PostItBulk extends PostItBase {
private String blob;
	public PostItBulk() {
		super();
	}
	public PostItBulk(java.lang.Integer id) {
		super(id);
	}


/**
 * @return
 */
public String getBlob() {
	return blob;
}

/**
 * @param string
 */
public void setBlob(String string) {
	blob = string;
}

/**
 * Restituisce il valore della proprietà 'rOprogetto'
 *
 * @return Il valore della proprietà 'rOprogetto'
 */
public boolean isROpostit() {
	
	return getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
}

/**
 * Metodo che elimina il path dal nome del file
 *
 * @return Il nome del file senza path
 * 
 * @param Il nome completo di path
 */

public static String parseFilename(String file) {

	StringTokenizer fileName = new StringTokenizer(file,"\\",false);
	String newFileName = null;
	
	while (fileName.hasMoreTokens()){
		newFileName = fileName.nextToken();   	
	}

	if (newFileName != null)
		return newFileName;
	
	return file;
}

}