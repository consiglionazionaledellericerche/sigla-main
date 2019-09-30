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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Detrazioni_familiariBulk extends Detrazioni_familiariBase {

    private final static java.util.Dictionary tipoPersonaKeys;
    public final static String TIPO_F       = "F";
    public final static String TIPO_C       = "C";
    public final static String TIPO_B       = "B";
    public final static String TIPO_A       = "A";
    public final static String TIPO_H       = "H";
    public final static String TIPO_L       = "L";
	public final static String TIPO_M       = "M";
	public final static String TIPO_N       = "N";
    

	static {
		tipoPersonaKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoPersonaKeys.put(TIPO_A,"Altro");
		tipoPersonaKeys.put(TIPO_B,"Figlio < 3 anni");
		tipoPersonaKeys.put(TIPO_C,"Coniuge");
		tipoPersonaKeys.put(TIPO_F,"Figlio");
		tipoPersonaKeys.put(TIPO_H,"Figlio portatore di handicap");
		tipoPersonaKeys.put(TIPO_L,"Lavoro assimilato");
		tipoPersonaKeys.put(TIPO_M,"Primo figlio se manca un genitore");
		tipoPersonaKeys.put(TIPO_N,"Non autosufficienti");		
	}
public Detrazioni_familiariBulk() {
	super();
}
public Detrazioni_familiariBulk(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Integer numero,java.lang.String ti_persona) {
	super(dt_inizio_validita,im_inferiore,numero,ti_persona);
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 12.30.41)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (05/12/2001 10.41.45)
 * @return int
 */
public java.util.Dictionary getTipoPersonaKeys() {
	return tipoPersonaKeys;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);

	setNumero(new Integer(1));
	resetImporti();

	return this;
}
private void resetImporti(){

	setIm_inferiore(new java.math.BigDecimal(0));
	setIm_superiore(new java.math.BigDecimal(0));
	setIm_detrazione(new java.math.BigDecimal(0));
	setIm_detrazione_primo_figlio(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 12.30.24)
 * @param newDate java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDate) {

	this.setDt_fine_validita(newDate);
}
public void validaImporti() throws ValidationException {

	// controllo su campo IM_DETRAZIONE
	if ( getIm_detrazione().compareTo(new java.math.BigDecimal(0)) <= 0 )
		throw new ValidationException( "Il campo Importo detrazione deve essere maggiore di zero");

	// controllo su campo IM_INFERIORE
	if ( getIm_inferiore().compareTo(new java.math.BigDecimal(0)) < 0 )
		throw new ValidationException( "Il campo Importo Inferiore non può essere negativo");

	// controllo campo IM_INFERIORE < IM_SUPERIORE
	if ( getIm_inferiore().compareTo(getIm_superiore()) >= 0 ) 
		throw new ValidationException( "Il campo Importo Superiore deve essere maggiore del campo Importo Inferiore");
}
public void validate() throws ValidationException {

	if (getIm_detrazione()==null)
		throw new ValidationException("Il campo IMPORTO DETRAZIONE non può essere vuoto");
	if (getIm_detrazione_primo_figlio()==null)
		throw new ValidationException( "Il campo IMPORTO DETRAZIONE PRIMO FIGLIO non può essere vuoto" );
	if (getIm_inferiore()==null)
		throw new ValidationException("Il campo IMPORTO INFERIORE non può essere vuoto");
	if (getIm_superiore()==null)
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto" );
}
}
