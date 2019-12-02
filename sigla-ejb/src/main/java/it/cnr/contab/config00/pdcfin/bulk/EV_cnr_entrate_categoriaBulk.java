/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.util.*;
/**
 * Elemento_voceBulk che rappresenta le categorie di entrata del CNR
 */
public class EV_cnr_entrate_categoriaBulk extends Elemento_voceBulk {
public EV_cnr_entrate_categoriaBulk() {
	super();
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	setTi_elemento_voce(Elemento_voceHome.TIPO_CATEGORIA);
	elemento_padre   = new Elemento_voceBulk();	
}
public EV_cnr_entrate_categoriaBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
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
		throw new ValidationException( "Inserire il codice titolo. " );
		
}
}
