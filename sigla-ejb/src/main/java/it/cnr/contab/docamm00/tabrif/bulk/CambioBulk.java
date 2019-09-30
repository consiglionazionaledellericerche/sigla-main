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

package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CambioBulk extends CambioBase {
	protected DivisaBulk divisa;
	protected boolean cambioDefault;
	protected String cd_divisa;
	//final static protected java.sql.Timestamp fineinfinito=new java.sql.Timestamp(new Long("7289568000000").longValue());
	
public CambioBulk() {
	super();
}

public CambioBulk(java.lang.String cd_divisa,java.sql.Timestamp dt_inizio_validita) {
	super(cd_divisa,dt_inizio_validita);
}

/**
 * Insert the method's description here.
 * Creation date: (04/04/2002 11.52.05)
 * @return java.lang.String
 */
public java.lang.String getCd_divisa() {
	return super.cd_divisa;
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.27)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public DivisaBulk getDivisa() {
	return divisa;
}

public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {


	setDivisa(new DivisaBulk());
	super.initializeForInsert(bp,context);
	
	return this;
}

public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {


	setDivisa(new DivisaBulk());
	super.initializeForSearch(bp,context);
	
	return this;
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.38)
 * @return boolean
 */
public boolean iscambioDefault() {
	return cambioDefault;
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.38)
 * @return boolean
 */
public boolean isCambioDefault() {
	return cambioDefault;
}

public boolean isInfinito() {

	if (dt_fine_validita!=null && dt_fine_validita.getTime()==it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO.getTime())
		return true;
	return false;

}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.38)
 * @return boolean
 */
public boolean isROCambioDefault() {
	return cambioDefault;
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.38)
 * @return boolean
 */
public boolean isROValuta() {
	return cambioDefault ||
	getDivisa() == null ||
			getDivisa().getCrudStatus() == OggettoBulk.NORMAL;
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.38)
 * @param newCambioDefault boolean
 */
public void setCambioDefault(boolean newCambioDefault) {
	cambioDefault = newCambioDefault;
}

/**
 * Insert the method's description here.
 * Creation date: (04/04/2002 11.52.05)
 * @param newCd_divisa java.lang.String
 */
public void setCd_divisa(java.lang.String newCd_divisa) {
	super.setCd_divisa(newCd_divisa);
}

/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.58.27)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(DivisaBulk newDivisa) {
	divisa = newDivisa;
}

public void validate() throws ValidationException {

	java.util.Calendar gc = java.util.Calendar.getInstance();
	try {
		gc.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}

	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	
	if (getDt_fine_validita()!=null && getDt_inizio_validita()!=null && getDt_fine_validita().before(getDt_inizio_validita()))
			throw new ValidationException("Attenzione! Inserire correttamente le date di inizio e fine validità");

	if (getDt_inizio_validita()==null )
			throw new ValidationException("Attenzione! Inserire correttamente la data di inizio validità");
	
	if (getDt_inizio_validita().after(new java.sql.Timestamp(gc.getTime().getTime())))
			throw new ValidationException("La data di inizio validità non può essere successiva alla data di sistema");
	if (getDt_inizio_validita().equals(new java.sql.Timestamp(gc.getTime().getTime())) && getDt_fine_validita()==null)
		setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);
	if (getDt_inizio_validita().before(new java.sql.Timestamp(gc.getTime().getTime())) && getDt_fine_validita()==null)
			throw new ValidationException("Attenzione! Inserire la data di fine validità");

	if (getCambio()!=null && getCambio().signum() <= 0)
			throw new ValidationException("Sono ammessi solo valori positivi!");
}}