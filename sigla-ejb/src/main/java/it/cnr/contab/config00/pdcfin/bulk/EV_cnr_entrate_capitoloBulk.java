package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.util.*;

import java.util.*;
/**
 * Elemento_voceBulk che rappresenta i capitoli di entrata del CNR
 */
public class EV_cnr_entrate_capitoloBulk extends Elemento_voceBulk {
	public final static Dictionary fl_voce_sacKeys;

	static
	{
		fl_voce_sacKeys = new OrderedHashtable();
		fl_voce_sacKeys.put(new Boolean(false), "N");
		fl_voce_sacKeys.put(new Boolean(true), "Y");
	}
public EV_cnr_entrate_capitoloBulk() {
	super();
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	setTi_elemento_voce(Elemento_voceHome.TIPO_CAPITOLO);
	elemento_padre   = new Elemento_voceBulk();
}
public EV_cnr_entrate_capitoloBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo fl_partita_giro
 */
public java.lang.Boolean getFl_partita_giro() 
{
	if ( elemento_padre == null )
		return null;
	return elemento_padre.getFl_partita_giro();
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getFl_voce_sacKeys() {
	return fl_voce_sacKeys;
}
/**
 * Metodo per inizializzare l'oggetto bulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert(bp, context);
	setFl_voce_personale( new Boolean("false") );
	return this;
}
/* 
 * Getter dell'attributo fl_partita_giro
 */
public void setFl_partita_giro( java.lang.Boolean bool) 
{
	elemento_padre.setFl_partita_giro( bool);
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException 
{
	super.validate();
	if ( elemento_padre == null || isNullOrEmpty( elemento_padre.getCd_elemento_voce() ))
		throw new ValidationException( "Inserire il codice categoria. " );
		
}
}
