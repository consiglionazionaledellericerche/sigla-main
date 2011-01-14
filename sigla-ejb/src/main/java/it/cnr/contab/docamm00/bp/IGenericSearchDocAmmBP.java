package it.cnr.contab.docamm00.bp;

/**
 * Insert the type's description here.
 * Creation date: (3/26/2002 11:53:38 AM)
 * @author: Roberto Peli
 */
public interface IGenericSearchDocAmmBP {
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
String getColumnsetForGenericSearch();
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
String getPropertyForGenericSearch();
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException;
}
