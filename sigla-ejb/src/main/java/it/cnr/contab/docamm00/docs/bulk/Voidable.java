package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (3/28/2002 3:46:20 PM)
 * @author: Roberto Peli
 */
public interface Voidable {
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:48:10 PM)
 * @return java.sql.Timestamp
 */
java.sql.Timestamp getDt_cancellazione();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:47:20 PM)
 * @return boolean
 */
boolean isAnnullato();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:56:31 PM)
 */
boolean isVoidable();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:47:20 PM)
 * @return boolean
 */
void setAnnullato(java.sql.Timestamp date);
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:48:10 PM)
 * @return java.sql.Timestamp
 */
void setDt_cancellazione(java.sql.Timestamp date);
}
