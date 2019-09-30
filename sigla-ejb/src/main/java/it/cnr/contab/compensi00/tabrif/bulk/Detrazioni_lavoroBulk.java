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

public class Detrazioni_lavoroBulk extends Detrazioni_lavoroBase {

    private final static java.util.Dictionary tipoLavoroKeys;
    public final static String TIPO_A       = "A";
    public final static String TIPO_D       = "D";

	static {
		tipoLavoroKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoLavoroKeys.put(TIPO_A,"Autonomo");
		tipoLavoroKeys.put(TIPO_D,"Dipendente");
	}
public Detrazioni_lavoroBulk() {
	super();
}
public Detrazioni_lavoroBulk(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.String ti_lavoro) {
	super(dt_inizio_validita,im_inferiore,ti_lavoro);
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
 * Creation date: (03/12/2001 13.53.55)
 * @return int
 */
public java.util.Dictionary getTipoLavoroKeys() {
	return tipoLavoroKeys;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);
	
	setTi_lavoro(TIPO_A);
	resetImporti();

	return this;
}
private void resetImporti(){

	setIm_inferiore(new java.math.BigDecimal(0));
	setIm_superiore(new java.math.BigDecimal(0));
	setIm_detrazione(new java.math.BigDecimal(0));
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

	// controllo su campo IM_DETRAZIONE
	if ( getIm_detrazione() == null )
		throw new ValidationException("Il campo IMPORTO DETRAZIONE non può essere vuoto");
	// controllo su campo IM_INFERIORE
	if ( getIm_inferiore() == null )
		throw new ValidationException("Il campo IMPORTO INFERIORE non può essere vuoto");
	// controllo su campo IM_SUPERIORE
	if ( getIm_superiore() == null )
		throw new ValidationException("Il campo IMPORTO SUPERIORE non può essere vuoto");

	// controllo su campo DT_INIZIO_VALIDITA
	if ( getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto" );
}
}
