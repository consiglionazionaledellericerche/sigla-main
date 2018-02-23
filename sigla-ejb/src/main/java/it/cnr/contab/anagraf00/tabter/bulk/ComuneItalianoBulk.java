package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 16.50.28)
 * @author: CNRADM
 */
public class ComuneItalianoBulk extends ComuneBulk {
/**
 * ComuneItalianoBulk constructor comment.
 */
public ComuneItalianoBulk() {
	super();
}
/**
 * ComuneItalianoBulk constructor comment.
 * @param pg_comune java.lang.Long
 */
public ComuneItalianoBulk(Long pg_comune) {
	super(pg_comune);
}
public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {

	super.initializeForInsert(bp, context);
	setTi_italiano_estero(COMUNE_ITALIANO);
	return this;
}
public boolean isItaliano(){
	return true;
}
/**
 * Oltre alla normale validate da un avviso di errore se la nazione è nulla.
 *
 * @exeption it.cnr.jada.bulk.ValidationException
*/
public void validate() throws ValidationException {

	super.validate();
	if (getDs_comune()==null)
		throw new ValidationException("Il campo DESCRIZIONE non può essere vuoto");
	if (getCd_catastale()==null)
		throw new ValidationException("Il campo CODICE CATASTALE non può essere vuoto");
	if (getCd_cap()==null)
		throw new ValidationException("Il campo CAP non può essere vuoto");
	if(getCd_provincia() == null)
		throw new ValidationException("Il campo PROVINCIA non può essere vuoto");
}
}
