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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;

public class Numeratore_buono_c_sHome extends BulkHome {
public Numeratore_buono_c_sHome(java.sql.Connection conn) {
	super(Numeratore_buono_c_sBulk.class,conn);
}
public Numeratore_buono_c_sHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Numeratore_buono_c_sBulk.class,conn,persistentCache);
}
public Long getNextPg(
	UserContext userContext,	
	Integer esercizio,
	Long progressivoInventario,
	String tipoDocumento,
	String user) 
	throws PersistencyException, 
			IntrospectionException, 
			it.cnr.jada.comp.ApplicationException 
{
	try{
	Numeratore_buono_c_sBulk numeratore = (Numeratore_buono_c_sBulk)findByPrimaryKey(new Numeratore_buono_c_sKey(esercizio,progressivoInventario,tipoDocumento));
	// non esiste il record - creo un nuovo record
	if (numeratore == null){
		numeratore = new Numeratore_buono_c_sBulk(esercizio, progressivoInventario, tipoDocumento);
		numeratore.setCorrente(new Long("1"));
		numeratore.setIniziale(new Long("0"));
		numeratore.setUser(user);
		insert(numeratore, userContext);		
	}
	else{
		numeratore.setCorrente(new Long(numeratore.getCorrente().longValue()+1));		
		lock(numeratore);
		update(numeratore, userContext);
	}
	return numeratore.getCorrente();
	}catch (BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");	
	}catch (OutdatedResourceException e) {
	    throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	}	
}
public Long getNextTempPg(
	UserContext userContext,
	Integer esercizio,
	Long progressivoInventario,
	String tipoDocumento,
	String user) 
	throws	PersistencyException, ApplicationException {
	try{
	String tipoDocTemp = null;
	Long pgCorrente = null;
	if (tipoDocumento.equalsIgnoreCase("C")){
		tipoDocTemp = "T";
	}
	else if (tipoDocumento.equalsIgnoreCase("S")){
		tipoDocTemp = "P";
	}
	
	Numeratore_buono_c_sBulk progressivo  = (Numeratore_buono_c_sBulk)findByPrimaryKey(
			new Numeratore_buono_c_sKey(esercizio,progressivoInventario,tipoDocTemp));
	Numeratore_buono_c_sBulk progressivo_def  = (Numeratore_buono_c_sBulk)findByPrimaryKey(
			new Numeratore_buono_c_sKey(esercizio,progressivoInventario,tipoDocumento));
	
	if (progressivo == null) {
		progressivo = new Numeratore_buono_c_sBulk();
		progressivo.setEsercizio(esercizio);
		progressivo.setPg_inventario(progressivoInventario);
		progressivo.setTi_carico_scarico(tipoDocTemp);
		progressivo.setIniziale(new Long(-1));
		pgCorrente = new Long(progressivo.getIniziale().longValue());
		progressivo.setCorrente(pgCorrente);
		progressivo.setUser(user);
		insert(progressivo, userContext);
		if (progressivo_def ==null){
				progressivo_def = new Numeratore_buono_c_sBulk();
				progressivo_def.setEsercizio(esercizio);
				progressivo_def.setPg_inventario(progressivoInventario);
				progressivo_def.setTi_carico_scarico(tipoDocumento);
				progressivo_def.setIniziale(new Long(1));
				progressivo_def.setCorrente(progressivo_def.getIniziale().longValue());
				progressivo_def.setUser(user);
				insert(progressivo_def, userContext);
			}
		return pgCorrente;
	}
	
	pgCorrente = new Long(progressivo.getCorrente().longValue()-1);
	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	lock(progressivo);
	update(progressivo, userContext);
	return pgCorrente;
	}catch (BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");	
	}catch (OutdatedResourceException e) {
	    throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	}	
}
}
