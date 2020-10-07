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

package it.cnr.contab.config00.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;
import java.sql.*;

/**
 * Creation date: (12/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cdsHome extends BulkHome {
	public Parametri_cdsHome(java.sql.Connection conn) {
		super(Parametri_cdsBulk.class, conn);
	}
	public Parametri_cdsHome(
		java.sql.Connection conn,
		PersistentCache persistentCache) {
		super(Parametri_cdsBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
	{
		Parametri_cdsBulk parametri = (Parametri_cdsBulk) bulk;
		parametri.setEsercizio( CNRUserContext.getEsercizio(userContext));
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		  sql.addSQLClause("AND","CD_CDS",sql.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}		
		return sql;
	}	
	/**
	 * Metodo che restituisce il valore della Commessa Obbligatoria
	 * @return Boolean contiene il valore del Flag Commessa Obbligatoria
	 */
	public boolean isCommessaObbligatoria(it.cnr.jada.UserContext userContext, String cd_cds) throws PersistencyException
	{
		Parametri_cdsBulk parametri = (Parametri_cdsBulk)findByPrimaryKey( new Parametri_cdsBulk(cd_cds,CNRUserContext.getEsercizio(userContext)));
		if (parametri == null)
		  return true;
		else
		  return parametri.getFl_commessa_obbligatoria().booleanValue();  
	
	}
	public boolean isRibaltato(it.cnr.jada.UserContext userContext) throws PersistencyException
	{
		  return isRibaltato(userContext,CNRUserContext.getCd_cds(userContext));  	
	}	
		
	public boolean isRibaltato(it.cnr.jada.UserContext userContext, String cd_cds) throws PersistencyException
	{
		Parametri_cdsBulk parametri = (Parametri_cdsBulk)findByPrimaryKey( new Parametri_cdsBulk(cd_cds,CNRUserContext.getEsercizio(userContext)));
		return isRibaltato(parametri);  
	
	}
	private boolean isRibaltato(Parametri_cdsBulk parametri) {
		if (parametri == null)
		  return true;
		else
		  return parametri.getFl_ribaltato().booleanValue();
	}	

	public boolean isRibaltato(it.cnr.jada.UserContext userContext, String cd_cds, Integer esercizio) throws PersistencyException
	{
		Parametri_cdsBulk parametri = (Parametri_cdsBulk)findByPrimaryKey( new Parametri_cdsBulk(cd_cds,esercizio));
		return isRibaltato(parametri);  
	
	}	

	public boolean isProgettoNumeratore(it.cnr.jada.UserContext userContext, String cd_cds) throws PersistencyException
	{
		Parametri_cdsBulk parametri = (Parametri_cdsBulk)findByPrimaryKey( new Parametri_cdsBulk(cd_cds,CNRUserContext.getEsercizio(userContext)));
		if (parametri == null)
		  return true;
		else
		  return parametri.getFl_progetto_numeratore().booleanValue();  
	
	}

	public Optional<String> getAbilProgetti(it.cnr.jada.UserContext userContext, String cd_cds) throws PersistencyException {
		Parametri_cdsBulk parametri = (Parametri_cdsBulk)findByPrimaryKey( new Parametri_cdsBulk(cd_cds,CNRUserContext.getEsercizio(userContext)));
		return Optional.ofNullable(parametri)
					.map(Parametri_cdsBase::getAbil_progetto_strorg);
	}
}
