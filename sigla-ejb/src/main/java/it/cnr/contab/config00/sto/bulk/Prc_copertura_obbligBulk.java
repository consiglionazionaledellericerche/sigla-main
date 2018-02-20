package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Prc_copertura_obbligBulk extends Prc_copertura_obbligBase 
{
	protected CdsBulk cds;

public Prc_copertura_obbligBulk() {
	super();
}
public Prc_copertura_obbligBulk(java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_unita_organizzativa,esercizio);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cds'
 *
 * @return Il valore della proprietà 'cds'
 */
public CdsBulk getCds() {
	return cds;
}
/**
 * Metodo per la gestione del campo <code>esercizio</code>.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cds'
 *
 * @param newCds	Il valore da assegnare a 'cds'
 */
public void setCds(CdsBulk newCds) {
	cds = newCds;
}
}
