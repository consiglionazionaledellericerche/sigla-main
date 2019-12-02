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

import java.sql.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Detrazioni_lavoroHome extends BulkHome {
public Detrazioni_lavoroHome(java.sql.Connection conn) {
	super(Detrazioni_lavoroBulk.class,conn);
}
public Detrazioni_lavoroHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Detrazioni_lavoroBulk.class,conn,persistentCache);
}
/**
 * Metodo che verifica la validità delle detrazioni per il lavoro
 * Creation date: (27/11/2001 13.12.51)
 *
 * @param detrazioni_lavoro    Detrazioni_lavoroBulk
 * @param uc  it.cnr.jada.UserContext
 *
 * @return boolean
 *
 * @exception PersistencyException
 */

public boolean checkIntervallo(Detrazioni_lavoroBulk detraz)  throws PersistencyException{

	try{

		boolean accepted = false;

		SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","ti_lavoro",sql.EQUALS,detraz.getTi_lavoro());
		
		CompoundFindClause clause01 = CompoundFindClause.and(
			new SimpleFindClause("im_inferiore", sql.LESS_EQUALS, detraz.getIm_inferiore()),
			new SimpleFindClause("im_superiore", sql.GREATER_EQUALS, detraz.getIm_superiore()));
		CompoundFindClause clause02 = CompoundFindClause.and(
			new SimpleFindClause("im_inferiore", sql.GREATER_EQUALS, detraz.getIm_inferiore()),
			new SimpleFindClause("im_inferiore", sql.LESS_EQUALS, detraz.getIm_superiore()));
		CompoundFindClause clause = CompoundFindClause.or(clause01, clause02);

		sql.addClause(clause);
		
		java.util.List l = fetchAll(sql);

		if (l.isEmpty())
			accepted = true;

	   return accepted;
	}catch(Exception e){
		throw new PersistencyException(e);
	}
}
/**
 * Metodo che verifica la validità delle detrazioni per il lavoro
 * Creation date: (27/11/2001 13.12.51)
 *
 * @param detrazioni_lavoro    Detrazioni_lavoroBulk
 * @param uc  it.cnr.jada.UserContext
 *
 * @return boolean
 *
 * @exception PersistencyException
 */

public boolean checkValidita(UserContext userContext, Detrazioni_lavoroBulk detraz)  throws PersistencyException{

	try{

		boolean accepted = false;
		/* Ricava tutte le righe della tabella con quel tipo d'auto e quella nazione */                                                    
			       
		Timestamp dataMax = (Timestamp)findMax(detraz,"dt_inizio_validita",null);
		
		if(dataMax == null)
			accepted = true;
		else{ 
			if(detraz.getDt_inizio_validita().after(dataMax)){
				Detrazioni_lavoroBulk oldDetraz = (Detrazioni_lavoroBulk)findAndLock(new Detrazioni_lavoroBulk(dataMax,detraz.getIm_inferiore(),detraz.getTi_lavoro()));
				oldDetraz.setDt_fine_validita(CompensoBulk.decrementaData(detraz.getDt_inizio_validita()));
				update(oldDetraz, userContext);
				accepted = true;
			} else
	   			accepted = false;	
		}
		detraz.setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);

	   return accepted;
	}catch(Exception e){
		throw new PersistencyException(e);
	}
}
}
