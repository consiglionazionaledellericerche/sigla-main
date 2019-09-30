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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

import java.util.*;

public class Tipo_bolloBulk extends Tipo_bolloBase {

	public final static Dictionary classe_tipo_bolloKeys;
	public final static Dictionary ti_entrata_spesaKeys;
	public final static Dictionary fl_defaultKeys;

	public final static String CLASSE_ESENTE 		= "1";
	public final static String CLASSE_ESCLUSO 		= "2";
	public final static String CLASSE_CARICO_ENTE 	= "3";
	public final static String CLASSE_CARICO_TERZO 	= "4";			

	static
	{
		classe_tipo_bolloKeys = new OrderedHashtable();
		classe_tipo_bolloKeys.put(CLASSE_ESENTE, "Esente");
		classe_tipo_bolloKeys.put(CLASSE_ESCLUSO, "Escluso");
		classe_tipo_bolloKeys.put(CLASSE_CARICO_ENTE, "Carico ente");
		classe_tipo_bolloKeys.put(CLASSE_CARICO_TERZO, "Carico terzo");
	}

	static public final String TIPO_ENTRATA 		= "E";
	static public final String TIPO_SPESA 			= "S";
	static public final String TIPO_ENTRATA_SPESA 	= "*";	
	
	static
	{
		ti_entrata_spesaKeys = new OrderedHashtable();
		ti_entrata_spesaKeys.put(TIPO_ENTRATA, "Entrata");
		ti_entrata_spesaKeys.put(TIPO_SPESA, "Spesa");
		ti_entrata_spesaKeys.put(TIPO_ENTRATA_SPESA, "Entrata/Spesa");
	}

	static
	{
		fl_defaultKeys = new OrderedHashtable();
		fl_defaultKeys.put(new Boolean(false), "N");
		fl_defaultKeys.put(new Boolean(true), "Y");
	}
/**
 * Tipo_bolloBulk constructor comment.
 */
public Tipo_bolloBulk() {
	super();
}
/**
 * Tipo_bolloBulk constructor comment.
 * @param cd_tipo_bollo Il codice del tipo di bollo
 */
public Tipo_bolloBulk(java.lang.String cd_tipo_bollo) {
	super(cd_tipo_bollo);
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getClasse_tipo_bolloKeys() {
	return classe_tipo_bolloKeys;
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getFl_defaultKeys() {
	return fl_defaultKeys;
}
public java.lang.String getPrintProperty() {
	return getCd_tipo_bollo() + " - " + getDs_tipo_bollo();
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_entrata_spesaKeys() {
	return ti_entrata_spesaKeys;
}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context L'azione del contesto in uso
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setFl_cancellato( new Boolean(false) );
	return this;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();

	// verifico che il campo ENTRATA/SPESA non sia nullo.
	if ( this.getTi_entrata_spesa() == null )
		throw new ValidationException( "Il campo ENTRATA/SPESA è obbligatorio." );

	// verifica sul campo IMPORTO
	if ( ( this.getIm_tipo_bollo() == null ) || getIm_tipo_bollo().compareTo( new java.math.BigDecimal(0) ) < 0 )
		throw new ValidationException( "L' IMPORTO deve essere maggiore o uguale a 0." );

	// verifico che il campo CODICE non sia nullo.
	if ( this.getCd_tipo_bollo() == null )
		throw new ValidationException( "Il campo CODICE è obbligatorio." );

}
}
