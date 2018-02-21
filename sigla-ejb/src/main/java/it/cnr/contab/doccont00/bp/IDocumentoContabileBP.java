package it.cnr.contab.doccont00.bp;

public interface IDocumentoContabileBP {
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param setSavePoint	
 * @return 
 * @throws BusinessProcessException	
 */
public abstract it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession getVirtualSession (	it.cnr.jada.action.ActionContext context,	boolean setSavePoint) throws it.cnr.jada.action.BusinessProcessException;
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'deleting'
 *
 * @return Il valore della proprietà 'deleting'
 */
public boolean isDeleting();
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'deleting'
 *
 * @param newDeleting	Il valore da assegnare a 'deleting'
 */
public void setDeleting(boolean newDeleting);
}
