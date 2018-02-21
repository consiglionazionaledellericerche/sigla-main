/*
 * Created on Apr 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.util;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ICancellatoLogicamente {
	/**
	 * il metodo ritorna TRUE se l'oggetto che lo implementa è stato cancellato logicamente
	 */
	public boolean isCancellatoLogicamente();
	/**
	 * il metodo viene richiamato quando si vuole effettuare la cancellazione logica dell'oggetto 
	 */
	public void cancellaLogicamente();	
}
