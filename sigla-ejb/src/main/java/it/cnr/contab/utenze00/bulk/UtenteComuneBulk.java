package it.cnr.contab.utenze00.bulk;

/**
 * Definisce un Utente Comune (utente finale) del sistema Gestione Contabilita' CNR (tipo utente = COMUNE e flag
 * utente template = FALSE) 
 *	
 */



import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

import java.util.*;

public class UtenteComuneBulk extends UtenteTemplateBulk implements it.cnr.jada.persistency.PersistencyListener
{
	private CdrBulk cdr = new CdrBulk();
	private UtenteTemplateBulk template = new UtenteTemplateBulk();

public UtenteComuneBulk() {
	super();
	inizializza();	
}
public UtenteComuneBulk(java.lang.String cd_utente) {
	super(cd_utente);
	inizializza();	
}
public java.lang.String getCd_cdr() {
	it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
	if (cdr == null)
		return null;
	return cdr.getCd_centro_responsabilita();
}
public java.lang.String getCd_utente_templ() {
	it.cnr.contab.utenze00.bulk.UtenteTemplateBulk template = this.getTemplate();
	if (template == null)
		return null;
	return template.getCd_utente();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cdr'
 *
 * @return Il valore della proprietà 'cdr'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'template'
 *
 * @return Il valore della proprietà 'template'
 */
public UtenteTemplateBulk getTemplate() {
	return template;
}
/**
 * Inizializza gli attributi specifici dell'Utente Comune
 */

private void inizializza() 
{
	setTi_utente(UtenteHome.TIPO_COMUNE);
	setFl_utente_templ( new Boolean( false ) );
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo cdr
 * @return boolean true se il campo deve essere disabilitato
 */

public boolean isROCdr() {
	return cdr == null || cdr.getCrudStatus() == NORMAL;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo template
 * @return boolean true quando il campo deve essere disabilitato
 */

public boolean isROTemplate() {
	return template == null || template.getCrudStatus() == NORMAL;
}
public void setCd_cdr(java.lang.String cd_cdr) {
	this.getCdr().setCd_centro_responsabilita(cd_cdr);
}
public void setCd_utente_templ(java.lang.String cd_utente_templ) {
	this.getTemplate().setCd_utente(cd_utente_templ);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cdr'
 *
 * @param newCdr	Il valore da assegnare a 'cdr'
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'template'
 *
 * @param newTemplate	Il valore da assegnare a 'template'
 */
public void setTemplate(UtenteTemplateBulk newTemplate) {
	template = newTemplate;
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException 
{
	super.validate();

	if ( cdr.getCd_centro_responsabilita() == null  )
		throw new ValidationException( "Il campo CODICE CDR è obbligatorio." );

}
}
