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

import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CambioHome extends BulkHome {

	//final static protected java.sql.Timestamp fineinfinito=new java.sql.Timestamp(new Long("7289568000000").longValue());
	final static protected String fineinfinitosql=" TO_DATE('31122200','ddmmyyyy') ";
public CambioHome(java.sql.Connection conn) {
	super(CambioBulk.class,conn);
}
public CambioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(CambioBulk.class,conn,persistentCache);
}
/**
 * 
 */

public boolean checkInserimentoSuccessivo(CambioBulk cambio) throws PersistencyException {
    try {
    	LoggableStatement ps=
            new LoggableStatement(getConnection(),
                "SELECT DT_INIZIO_VALIDITA FROM "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    + "CAMBIO WHERE CD_DIVISA = ? "
                    + "AND ?>=DT_INIZIO_VALIDITA AND "
                    + fineinfinitosql
                    + "<=DT_FINE_VALIDITA",true,this.getClass());
        try {

            ps.setString(1, cambio.getDivisa().getCd_divisa());
            ps.setObject(2, cambio.getDt_inizio_validita());

            java.sql.ResultSet rs= ps.executeQuery();

            try {
                if (rs.next()) {
                	LoggableStatement ps2=
                        new LoggableStatement(getConnection(),"UPDATE " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "CAMBIO SET DT_FINE_VALIDITA=?-1 WHERE CD_DIVISA=? AND DT_INIZIO_VALIDITA=?",true,this.getClass());
                    ps2.setObject(1, cambio.getDt_inizio_validita());
                    ps2.setString(2, cambio.getCd_divisa());
                    ps2.setObject(3, rs.getObject("DT_INIZIO_VALIDITA"));

                    java.sql.ResultSet rs2= ps2.executeQuery();

                    cambio.setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);
                    return true;
                } else
                    return false;
            } finally {
                try{rs.close();}catch( java.sql.SQLException e ){};
            }
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try{ps.close();}catch( java.sql.SQLException e ){};
        }
    } catch (java.sql.SQLException e) {
        throw new PersistencyException(e);
    }

}
/**
 * 
 */

public boolean checkPeriodi(CambioBulk cambio)  throws PersistencyException
{
	try
	{
		//if (cambio==null || cambio.getDivisa()==null)
			//return false;
		//java.sql.PreparedStatement ps = getConnection().prepareStatement(
			//"SELECT CD_DIVISA FROM " + 
			//it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			//"CAMBIO " +
			//"WHERE CD_DIVISA = ? " +
			//"AND ((?>=DT_INIZIO_VALIDITA " +
		   	//"	AND ?<=DT_FINE_VALIDITA ) "+
			//"OR (?>=DT_INIZIO_VALIDITA "+
			//"	AND ?<=DT_FINE_VALIDITA ) "+
			//"OR (?<=DT_INIZIO_VALIDITA "+
			//"	AND ?>=DT_FINE_VALIDITA ))");
		//ps.setString( 1, cambio.getDivisa().getCd_divisa());
		//ps.setObject( 2, cambio.getDt_inizio_validita());
		//ps.setObject( 3, cambio.getDt_inizio_validita());
		//ps.setObject( 4, cambio.getDt_fine_validita());
		//ps.setObject( 5, cambio.getDt_fine_validita());
		//ps.setObject( 6, cambio.getDt_inizio_validita());
		//ps.setObject( 7, cambio.getDt_fine_validita());
		//java.sql.ResultSet rs = LoggableStatement.executeQuery(ps);
		//if ( rs.next() )
			//return false;
		//else		
			//return true;
		if (cambio==null || cambio.getDivisa()==null)
			return false;
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_DIVISA FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"CAMBIO " +
			"WHERE CD_DIVISA = ? " +
			"AND ?=DT_INIZIO_VALIDITA ",true,this.getClass());
		ps.setString( 1, cambio.getDivisa().getCd_divisa());
		ps.setObject( 2, cambio.getDt_inizio_validita());
		java.sql.ResultSet rs = ps.executeQuery();
		if ( rs.next() )
			return false;
		else		
			return true;
	}

	catch ( java.sql.SQLException e )
	{
			throw new PersistencyException( e );
	}
	
}
/**
 * Ricerca il cambio valido in data <dataCambio> per la divisa selezionata
 *
 * E' possibile che possano esistere in data <dataCambio> più intervalli validi
 * In questo caso è necessario ricercare tra tutti i cambi validi, il migliore
 *
 * Metodo usato nella fattura passiva, nel documento generico, nella missione e nell'anticipo
 *
*/
public CambioBulk getCambio(DivisaBulk valuta, java.sql.Timestamp dataCambio) throws PersistencyException, it.cnr.jada.comp.ApplicationException {

	if (valuta==null || valuta.getCd_divisa()==null)
		throw new it.cnr.jada.comp.ApplicationException("Inserire una valuta per il periodo specificato!");

	return getCambio(valuta.getCd_divisa(), dataCambio);
}
//
//	Data una divisa mi ritorna il cambio valido alla data specificata
//
public CambioBulk getCambio(String divisa, java.sql.Timestamp dataCambio)
		throws PersistencyException, ApplicationException {
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND", "CD_DIVISA", sql.EQUALS, divisa);
	sql.addSQLClause("AND", "? BETWEEN DT_INIZIO_VALIDITA AND DT_FINE_VALIDITA");
	sql.addParameter(dataCambio, java.sql.Types.TIMESTAMP, 0);

	java.util.List cambiValidi = fetchAll(sql);

	if (cambiValidi == null || cambiValidi.isEmpty())
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste un cambio valido per il periodo specificato!");
	CambioBulk cambioValido = null;
	for (java.util.Iterator i = cambiValidi.iterator(); i.hasNext();)	{
		CambioBulk cambio = (CambioBulk)i.next();
		if (cambioValido == null || 
			cambio.getDt_inizio_validita().after(cambioValido.getDt_inizio_validita()) ||
			(cambio.getDt_inizio_validita().equals(cambioValido.getDt_inizio_validita()) &&
				cambio.getDuva().after(cambioValido.getDuva())))
			cambioValido = cambio;
	}

	return cambioValido;
}

public CambioBulk getCambio(it.cnr.jada.UserContext uc, DivisaBulk valuta, java.sql.Timestamp dataCambio) throws PersistencyException
{
	if((dataCambio == null) || (valuta == null) || (valuta.getCd_divisa() == null)) 
		return null;

	CambioBulk aCambio = null;

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_DIVISA", sql.EQUALS, valuta.getCd_divisa());
	sql.addSQLClause("AND","DT_INIZIO_VALIDITA", sql.LESS_EQUALS, dataCambio );
	sql.addSQLClause("AND","DT_FINE_VALIDITA", sql.GREATER_EQUALS, dataCambio );
	sql.addOrderBy("DT_INIZIO_VALIDITA DESC");


	SQLBroker broker = createBroker(sql);
	if (broker.next())
		aCambio = (CambioBulk) fetch(broker);
	
	broker.close();
		
	return aCambio;
}
/**
 * Insert the method's description here.
 * Creation date: (18/09/2001 17.11.12)
 * @return java.sql.Timestamp
 */
public final static java.sql.Timestamp getFineinfinito() {
	return it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO;
}
/**
 * Insert the method's description here.
 * Creation date: (18/09/2001 17.11.12)
 * @return java.lang.String
 */
public final static java.lang.String getFineinfinitosql() {
	return fineinfinitosql;
}
}
