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

package it.cnr.contab.preventvar00.bulk;

import javax.ejb.EJBException;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancio_detHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Var_bilancio_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Var_bilancio_detHome(java.sql.Connection conn) {
	super(Var_bilancio_detBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Var_bilancio_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Var_bilancio_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Var_bilancio_detBulk.class,conn,persistentCache);
}
/**
 * Carica i dettagli di un variazione di bilancio
 *
 * @param varBilancio	
 * @return la lista dei dettagli caricati
 * @throws PersistencyException	
 * @throws EJBException 
 * @throws ComponentException 
 */
public java.util.List caricaDettagli(UserContext userContext,Var_bilancioBulk varBilancio) throws PersistencyException, ComponentException, EJBException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,varBilancio.getCd_cds());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,varBilancio.getEsercizio());
	sql.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS,varBilancio.getTi_appartenenza());
	sql.addSQLClause("AND","PG_VARIAZIONE",sql.EQUALS,varBilancio.getPg_variazione());
	
	java.util.List l = fetchAll(sql);
	for (java.util.Iterator i=l.iterator();i.hasNext();){
		Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)i.next();
		V_assestato_voceBulk ass_voce = new V_assestato_voceBulk();
		ass_voce = caricaVoceFSaldi(userContext,varBilDett, varBilancio);
		if (ass_voce!=null)
			varBilDett.setVoceFSaldi(ass_voce);
	}

	getHomeCache().fetchAll(userContext);
	return l;

}
/**
 * Carica il saldo del capitolo/articolo impattato dal dettaglio di variazione in processo
 *
 * @param varBilDett dettaglio di variazione in processo
 * @return istanza di V_assestato_voceBulk
 * @throws PersistencyException	
 * @throws EJBException 
 * @throws ComponentException 
 */
public V_assestato_voceBulk caricaVoceFSaldi(UserContext userContext,Var_bilancio_detBulk varBilDett,Var_bilancioBulk varBilancio) throws PersistencyException, ComponentException, EJBException{

	V_assestato_voceHome home = (V_assestato_voceHome)getHomeCache().getHome(V_assestato_voceBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,varBilDett.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_RES",SQLBuilder.EQUALS,varBilancio.getEsercizio_importi());
	sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, Utility.createCdrComponentSession().getCdrEnte(userContext).getCd_centro_responsabilita());
	sql.addSQLClause("AND","TI_APPARTENENZA",SQLBuilder.EQUALS,varBilDett.getTi_appartenenza());
	sql.addSQLClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,varBilDett.getTi_gestione());
	sql.addSQLClause("AND","CD_VOCE",SQLBuilder.EQUALS,varBilDett.getCd_voce());

	Broker broker = createBroker(sql);
	V_assestato_voceBulk voce = null;
	if (broker.next())
		voce = (V_assestato_voceBulk )home.fetch(broker);
	broker.close();
	return voce;
}
}
