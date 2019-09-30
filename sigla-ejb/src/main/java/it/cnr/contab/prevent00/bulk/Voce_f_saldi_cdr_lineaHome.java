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

/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Voce_f_saldi_cdr_lineaHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cdr_lineaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_f_saldi_cdr_lineaHome(java.sql.Connection conn) {
	super(Voce_f_saldi_cdr_lineaBulk.class,conn);
}

public Voce_f_saldi_cdr_lineaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_saldi_cdr_lineaBulk.class,conn,persistentCache);
}
/**
 * Cerca il CDS ENTE
 *
 * @param dettaglio	saldo dettaglio
 * @return Lista contenente il CDS ENTE
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public java.util.List cercaCdsEnte(Voce_f_saldi_cdr_lineaBulk dettaglio) throws PersistencyException, IntrospectionException
{
	PersistentHome enteHome = getHomeCache().getHome(it.cnr.contab.config00.sto.bulk.EnteBulk.class, "V_CDS_VALIDO");
	SQLBuilder sql = enteHome.createSQLBuilder();
	
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, dettaglio.getEsercizio());
	
	return enteHome.fetchAll(sql);
}
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	//sql.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.ESERCIZIO",SQLBuilder.EQUALS,"VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES");
	return sql;
}
public Persistent findByElementoVoce(Voce_f_saldi_cdr_lineaBulk saldo) throws PersistencyException {
	PersistentHome home = getHomeCache().getHome(Voce_f_saldi_cdr_lineaBulk.class);	
	SQLBuilder sql = createSQLBuilder();	
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,saldo.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_RES",SQLBuilder.EQUALS,saldo.getEsercizio_res());
    sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,saldo.getCd_centro_responsabilita());
    sql.addSQLClause("AND","CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,saldo.getCd_linea_attivita());
	sql.addSQLClause("AND","TI_APPARTENENZA",SQLBuilder.EQUALS,saldo.getTi_appartenenza());	
	sql.addSQLClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,saldo.getTi_gestione());
	sql.addSQLClause("AND","CD_ELEMENTO_VOCE",SQLBuilder.EQUALS,saldo.getCd_elemento_voce());
	SQLBroker broker = home.createBroker(sql);
	if (broker.next())
	  saldo = (Voce_f_saldi_cdr_lineaBulk)broker.fetch(Voce_f_saldi_cdr_lineaBulk.class);
	else
	  saldo=null;	    
	return saldo;  
}
public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
	Voce_f_saldi_cdr_lineaBulk saldo = (Voce_f_saldi_cdr_lineaBulk)oggettobulk;
	saldo.inizializzaSommeAZero();
	super.initializePrimaryKeyForInsert(usercontext, saldo);
}
/**
 * Effettua la ricerca di tutti i dettagli residui partendo da un dettaglio di competenza
 *
 * @param V_saldi_piano_econom_progettoBulk istanza del bulk del cdr/voce/linea di competenza
 * @return una lista contenente i <<Voce_f_saldi_cdr_lineaBulk>> cdr/voce/linea residui
 */
public java.util.List cercaDettagliResidui(Voce_f_saldi_cdr_lineaBulk saldo) throws PersistencyException, IntrospectionException
{
	PersistentHome home = getHomeCache().getHome(Voce_f_saldi_cdr_linea_resBulk.class);	
	SQLBuilder sql = home.createSQLBuilder();	
	
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,saldo.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_RES",SQLBuilder.LESS,saldo.getEsercizio());
    sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,saldo.getCd_centro_responsabilita());
    sql.addSQLClause("AND","CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,saldo.getCd_linea_attivita());
	sql.addSQLClause("AND","TI_APPARTENENZA",SQLBuilder.EQUALS,saldo.getTi_appartenenza());	
	sql.addSQLClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,saldo.getTi_gestione());
	sql.addSQLClause("AND","CD_VOCE",SQLBuilder.EQUALS,saldo.getCd_voce());

	return home.fetchAll(sql);
}
}
