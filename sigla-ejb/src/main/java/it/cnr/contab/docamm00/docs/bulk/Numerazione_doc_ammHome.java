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

package it.cnr.contab.docamm00.docs.bulk;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_ammHome extends BulkHome {
public Numerazione_doc_ammHome(java.sql.Connection conn) {
	super(Numerazione_doc_ammBulk.class,conn);
}
public Numerazione_doc_ammHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Numerazione_doc_ammBulk.class,conn,persistentCache);
}
public Long getNextPg(UserContext userContext, Integer esercizio, String cd_cds, String uo, String tipo_doc, String user) throws PersistencyException, IntrospectionException, OutdatedResourceException, ComponentException 
{
	Numerazione_doc_ammBulk progressivo;
	if(tipo_doc.compareTo(Numerazione_doc_ammBulk.TIPO_LETTERA_ESTERO)==0){
		try {
			if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,esercizio).getFl_tesoreria_unica().booleanValue()){
				Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)(getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
				progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey( new Numerazione_doc_ammKey(uoEnte.getCd_cds(), tipo_doc, uoEnte.getCd_unita_organizzativa(), esercizio ));
			}
			else{
				progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey( new Numerazione_doc_ammKey(cd_cds, tipo_doc, uo, esercizio ));
			}
		} catch (ComponentException e) {
			throw new ComponentException( e );
		} catch (RemoteException e) {
			throw new ComponentException( e );
		} catch (EJBException e) {
			throw new ComponentException( e );
		}
	}
	else
	  progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey( new Numerazione_doc_ammKey(cd_cds, tipo_doc, uo, esercizio ));

	//non esiste il record - segnalo errore
	if (progressivo == null)
		throw new it.cnr.jada.comp.ApplicationException("Non e' possibile assegnare progressivi");
	Long pgCorrente = new Long(progressivo.getCorrente().longValue()+1);

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
public Long getNextTempPg(UserContext userContext,Integer esercizio, String cd_cds, String uo, String tipo_doc, String user) throws	PersistencyException, OutdatedResourceException, BusyResourceException 
{
	Numerazione_doc_ammBulk progressivo =  null;
	Long pgCorrente = null;
	
	String tipoDocTemp = tipo_doc + "$";	
	progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey(new Numerazione_doc_ammKey(cd_cds, tipoDocTemp, uo, esercizio));

	if (progressivo == null) 
	{
		progressivo = new Numerazione_doc_ammBulk();
		progressivo.setUser(user);
		progressivo.setCd_cds(cd_cds);
		progressivo.setCd_unita_organizzativa(uo);
		progressivo.setCd_tipo_documento_amm(tipoDocTemp);
		progressivo.setEsercizio(esercizio);
		
		pgCorrente = new Long(-1);
		progressivo.setCorrente(pgCorrente);
		insert(progressivo, userContext);
		
		return pgCorrente;
	}
	
	pgCorrente = new Long(progressivo.getCorrente().longValue()-1);
	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	lock(progressivo);
	update(progressivo, userContext);
	
	return pgCorrente;
}
}
