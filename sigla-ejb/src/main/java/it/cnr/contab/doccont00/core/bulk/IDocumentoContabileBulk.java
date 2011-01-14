package it.cnr.contab.doccont00.core.bulk;

import java.util.Map;
/**
 * Insert the type's description here.
 * Creation date: (2/7/2002 3:24:48 PM)
 * @author: Simonetta Costa
 */
public interface IDocumentoContabileBulk {
	public static final String TI_ENTRATA 	= "E";
	public static final String TI_SPESA 	= "S";	
String getCd_cds();
Integer getEsercizio();
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 3:58:54 PM)
 * @return java.lang.String
 */
String getManagerName();
Integer getEsercizio_originale();
Long getPg_doc_contabile();
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 14.34.10)
 * @return boolean
 */
Long getPg_ver_rec();
public String getRiportato(); 
public Map getSaldiInfo(); 
String getTi_entrata_spesa();
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 14.34.10)
 * @return boolean
 */
boolean isInitialized();
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 14.34.10)
 * @return boolean
 */
boolean isTemporaneo();
}
