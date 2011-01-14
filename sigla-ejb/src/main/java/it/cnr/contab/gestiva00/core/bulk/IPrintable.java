package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (12/6/2002 11:15:55 AM)
 * @author: CNRADM
 */
public interface IPrintable {
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
java.math.BigDecimal getId_report();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
String getReportName();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
java.util.Vector getReportParameters();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
boolean isRistampa();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
boolean isRistampabile();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
void setId_report(java.math.BigDecimal new_id_report);

BulkList getPrintSpoolerParam();
}
