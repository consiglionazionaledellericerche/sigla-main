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

package it.cnr.contab.doccont00.core.bulk;

import java.sql.*;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_contHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Numerazione_doc_contHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Numerazione_doc_contHome(java.sql.Connection conn) {
	super(Numerazione_doc_contBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Numerazione_doc_contHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Numerazione_doc_contHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Numerazione_doc_contBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @param cd_cds	
 * @param tipo_doc	
 * @param user	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 * @throws OutdatedResourceException	
 * @throws BusyResourceException	
 * @throws ApplicationException	
 */
public Long getNextPg(UserContext userContext, Integer esercizio, String cd_cds, String tipo_doc, String user) throws PersistencyException, IntrospectionException, it.cnr.jada.comp.ApplicationException 
{
	Numerazione_doc_contBulk progressivo = (Numerazione_doc_contBulk)findByPrimaryKey( new Numerazione_doc_contKey(cd_cds, tipo_doc, esercizio ));
	//non esiste il record - segnalo errore
	if (progressivo == null)
		throw new it.cnr.jada.comp.ApplicationException("Non e' possibile assegnare progressivi");
	Long pgCorrente = new Long(progressivo.getCorrente().longValue()+1);
	//esauriti i progressivi - segnalo errore
	if ( !(pgCorrente.compareTo( progressivo.getUltimo()) <= 0 ))
		throw new it.cnr.jada.comp.ApplicationException("Non esistono più progressivi disponibili");
	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	try {
		lock(progressivo);
	}catch (BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	}catch (OutdatedResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	}
	update(progressivo, userContext);
	return pgCorrente;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @param cd_cds	
 * @param tipo_doc	
 * @param user	
 * @return 
 * @throws PersistencyException	
 * @throws OutdatedResourceException	
 * @throws BusyResourceException	
 */
public Long getNextTempPg(
	UserContext userContext,	
	Integer esercizio, 
	String cd_cds, 
	String tipo_doc, 
	String user)
	throws	PersistencyException,
			OutdatedResourceException,
			BusyResourceException {

	String tipoDocTemp = tipo_doc + "$";
	Numerazione_doc_contBulk progressivo =  null;
	progressivo = (Numerazione_doc_contBulk)findByPrimaryKey(
		new Numerazione_doc_contKey(cd_cds, tipoDocTemp, esercizio));
	Long pgCorrente = null;
	if (progressivo == null) {
		Numerazione_doc_contBulk numeratore = (Numerazione_doc_contBulk)findByPrimaryKey(new Numerazione_doc_contKey(cd_cds, tipo_doc, esercizio));
		if (numeratore == null)
			throw new PersistencyException("Impossibile ottenere un numeratore per il tipo documento contabile " + tipo_doc);
		progressivo = new Numerazione_doc_contBulk();
		progressivo.setUser(user);
		progressivo.setCd_cds(cd_cds);
		progressivo.setCd_tipo_documento_cont(tipoDocTemp);
		progressivo.setEsercizio(esercizio);
		progressivo.setPrimo(new Long(-numeratore.getPrimo().longValue()));
		progressivo.setUltimo(new Long(-numeratore.getUltimo().longValue()));
		pgCorrente = new Long(progressivo.getPrimo().longValue());
		progressivo.setCorrente(pgCorrente);
		insert(progressivo, userContext);
		return pgCorrente;
	}
	pgCorrente = new Long(progressivo.getCorrente().longValue()-1);
	if (pgCorrente.longValue() < progressivo.getUltimo().longValue())
		pgCorrente = progressivo.getPrimo();
	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	lock(progressivo);
	update(progressivo, userContext);
	return pgCorrente;
}
}
