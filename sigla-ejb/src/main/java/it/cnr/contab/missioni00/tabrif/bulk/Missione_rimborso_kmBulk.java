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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_rimborso_kmBulk extends Missione_rimborso_kmBase {
    private final static java.util.Dictionary tipoAutoKeys;
    public final static String TIPO_P       = "P";
    public final static String TIPO_A       = "A";

	static {
		tipoAutoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoAutoKeys.put(TIPO_P,"Propria");
		tipoAutoKeys.put(TIPO_A,"Amministrativa");
	}
	private NazioneBulk nazione = new NazioneBulk();
public Missione_rimborso_kmBulk() {
	super();
}
public Missione_rimborso_kmBulk(java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.String ti_area_geografica, java.lang.String ti_auto) {
	super(dt_inizio_validita,pg_nazione,ti_area_geografica,ti_auto);
	setNazione(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk(pg_nazione));
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.29.30)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {
	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2001 14.14.23)
 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public NazioneBulk getNazione() {
	return nazione;
}
public java.lang.Long getPg_nazione() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = this.getNazione();
	if (nazione == null)
		return null;
	return nazione.getPg_nazione();
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.53.47)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_area_geograficaKeys() {
	return TipoAreaGeografica.TIPI_AREA_GEOGRAFICA;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2001 13.58.29)
 */
/**
 * Restituisce il dizionario {@link java.util.Dictionary } dei tipi di auto.
 * 
 * @return java.util.Dictionary tipoAutoKeys
 */
public java.util.Dictionary getTipoAutoKeys() {
	return tipoAutoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	setTi_auto(TIPO_P);
	resetImporti();

	return this;
}
public boolean isROFindNazione(){

	if (getTi_area_geografica()==null)
		return false;

	return 
		(getTi_area_geografica().compareTo(TipoAreaGeografica.ITALIA)==0) ||
		(getTi_area_geografica().compareTo(TipoAreaGeografica.INDIFFERENTE)==0);
}
public boolean isRONazione(){

	return getNazione()==null || getNazione().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.25.56)
 */
private void resetImporti() {
	
	setIndennita_chilometrica(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.30.01)
 * @param newTime java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDataFineValidita) {

	this.setDt_fine_validita(newDataFineValidita);
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2001 14.14.23)
 * @param newNazione it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione(NazioneBulk newNazione) {
	nazione = newNazione;
}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
public void validate() throws ValidationException {

	// controllo su campo TIPO AUTO
	if (getTi_auto() == null ) 
		throw new ValidationException( "Selezionare il TIPO AUTO!" );

	// controllo su campo TI_AREA_GEOGRAFICA
	if (getTi_area_geografica() == null ) 
		throw new ValidationException( "Selezionare l'AREA GEOGRAFICA!" );

	// controllo su campo NAZIONE
	if (getPg_nazione() == null ) 
		throw new ValidationException( "Il campo NAZIONE non può essere vuoto!" );

	// controllo su campo DATA INIZIO VALIDITA
	if (getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto!" );

	// controllo su campo INDENNITA CHILOMETRICA
	if ( getIndennita_chilometrica() == null )
		throw new ValidationException( "Il campo INDENNITA CHILOMETRICA non può essere vuoto!" );
	if (getIndennita_chilometrica().compareTo(new java.math.BigDecimal(0))<=0)
		throw new ValidationException( "Il campo INDENNITA CHILOMETRICA deve essere maggiore di 0!" );

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");
	
}		
}
