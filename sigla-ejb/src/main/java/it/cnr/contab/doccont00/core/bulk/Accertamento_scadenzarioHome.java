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

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;
import java.sql.*;

public class Accertamento_scadenzarioHome extends BulkHome implements IScadenzaDocumentoContabileHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Accertamento_scadenzarioHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Accertamento_scadenzarioHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Accertamento_scadenzarioHome(java.sql.Connection conn) {
	super(Accertamento_scadenzarioBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Accertamento_scadenzarioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Accertamento_scadenzarioBulk.class,conn,persistentCache);
}

public void aggiornaImportoAssociatoADocAmm(
	UserContext userContext, 		
	IScadenzaDocumentoContabileBulk scadenzaDocCont) 
	throws PersistencyException, BusyResourceException, OutdatedResourceException {

	if (scadenzaDocCont == null) return;

	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)scadenzaDocCont;

	try {
		((BulkHome)getHomeCache().getHome(scadenza.getFather().getClass())).lock((OggettoBulk)scadenza.getFather());
		//getHomeCache().getHome(scadenza.getFather().getClass()).update((Persistent)scadenza.getFather());
	} catch (OutdatedResourceException e) {
	}

	lock(scadenza);
	
	getHomeCache().getHome(Accertamento_scadenzarioBulk.class, "IMPORTO_ASSOCIATO").update(scadenza, userContext);

//	StringBuffer stm = new StringBuffer("UPDATE ");
//	stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
//	stm.append(getColumnMap().getTableName());
//	stm.append(" SET IM_ASSOCIATO_DOC_AMM = ? WHERE (");
//	stm.append("CD_CDS = ? AND ESERCIZIO = ? AND ESERCIZIO_ORIGINALE = ? AND PG_ACCERTAMENTO = ? AND PG_ACCERTAMENTO_SCADENZARIO = ? )");
//
//	try {
//		PreparedStatement ps = getConnection().prepareStatement(stm.toString());
//		try {	
//			ps.setBigDecimal(1, scadenza.getIm_associato_doc_amm());
//			ps.setString(2, scadenza.getCd_cds());
//			ps.setInt(3, scadenza.getEsercizio().intValue());
//			ps.setInt(4, scadenza.getEsercizio_originale().intValue());
//		    ps.setLong(5, scadenza.getPg_accertamento().longValue());
//			ps.setLong(6, scadenza.getPg_accertamento_scadenzario().longValue());
//
//			LoggableStatement.executeUpdate(ps);
//		} finally {
//			try{ps.close();}catch( java.sql.SQLException e ){};
//		}
//	} catch(java.sql.SQLException e) {
//		throw SQLExceptionHandler.getInstance().handleSQLException(e,scadenza);
//	}
}
/**
 * Ricerco nella vista V_DOC_ATTIVO_ACCERTAMENTO gli estremi dei documetni 
 * attivi (fatture attive, generici di entrata) collegati alle scadenze del 
 * mio accertamento
 *
 * @param scadenza	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public V_doc_attivo_accertamentoBulk findDoc_attivo( Accertamento_scadenzarioBulk scadenza ) throws IntrospectionException,PersistencyException 
{
	PersistentHome docHome = getHomeCache().getHome(V_doc_attivo_accertamentoBulk.class );
	SQLBuilder sql = docHome.createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS_ACCERTAMENTO",sql.EQUALS, scadenza.getCd_cds());
	sql.addSQLClause("AND","ESERCIZIO_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORI_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio_originale());
	sql.addSQLClause("AND","PG_ACCERTAMENTO",sql.EQUALS, scadenza.getPg_accertamento());
	sql.addSQLClause("AND","PG_ACCERTAMENTO_SCADENZARIO",sql.EQUALS, scadenza.getPg_accertamento_scadenzario());
	sql.addOrderBy("FL_SELEZIONE DESC");	
		
	java.util.List lista =  docHome.fetchAll(sql);
	if ( lista.size() > 0 )
		return (V_doc_attivo_accertamentoBulk) lista.get(0);
	else
		return null;	
}
public List findDocAmm( Accertamento_scadenzarioBulk scadenza, Class class1 ) throws IntrospectionException,PersistencyException 
{
	PersistentHome docHome = getHomeCache().getHome(class1);
	SQLBuilder sql = docHome.createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS_ACCERTAMENTO",sql.EQUALS, scadenza.getCd_cds());
	sql.addSQLClause("AND","ESERCIZIO_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORI_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio_originale());
	sql.addSQLClause("AND","PG_ACCERTAMENTO",sql.EQUALS, scadenza.getPg_accertamento());
	sql.addSQLClause("AND","PG_ACCERTAMENTO_SCADENZARIO",sql.EQUALS, scadenza.getPg_accertamento_scadenzario());
		
	java.util.List lista =  docHome.fetchAll(sql);
	return lista;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param as	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public Reversale_rigaBulk findReversale( Accertamento_scadenzarioBulk as ) throws IntrospectionException,PersistencyException 
{
	PersistentHome rrHome = getHomeCache().getHome(Reversale_rigaIBulk.class );
	SQLBuilder sql = rrHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, as.getCd_cds());		
	sql.addSQLClause("AND","ESERCIZIO_ACCERTAMENTO",sql.EQUALS, as.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORI_ACCERTAMENTO",sql.EQUALS, as.getEsercizio_originale());
	sql.addSQLClause("AND","PG_ACCERTAMENTO",sql.EQUALS, as.getPg_accertamento());
	sql.addSQLClause("AND","PG_ACCERTAMENTO_SCADENZARIO",sql.EQUALS, as.getPg_accertamento_scadenzario());
	sql.addSQLClause("AND","STATO",sql.NOT_EQUALS, Reversale_rigaBulk.STATO_ANNULLATO);		
	List l =  rrHome.fetchAll(sql);
	if ( l.size() > 0 )
		return (Reversale_rigaBulk) l.get(0);
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoKeys( Accertamento_scadenzarioBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
	List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
	Hashtable ht = new Hashtable();
	Tipo_documento_ammBulk tipo;
	for (Iterator i = result.iterator(); i.hasNext(); )
	{
		tipo = (Tipo_documento_ammBulk) i.next();
		ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
	}	
	return ht;
}

public java.util.List findAccertamento_scad_voceList( it.cnr.jada.UserContext userContext, Accertamento_scadenzarioBulk os ) throws IntrospectionException,PersistencyException 
{
//	PersistentHome osvHome = getHomeCache().getHome(Accertamento_scad_voceBulk.class, "default", "it.cnr.contab.doccont00.comp.AccertamentoComponent.edit" );
	PersistentHome osvHome = getHomeCache().getHome(Accertamento_scad_voceBulk.class );
	SQLBuilder sql = osvHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, os.getAccertamento().getCds().getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, os.getAccertamento().getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORIGINALE",sql.EQUALS, os.getAccertamento().getEsercizio_originale());
	sql.addSQLClause("AND","PG_ACCERTAMENTO",sql.EQUALS, os.getAccertamento().getPg_accertamento());
	sql.addSQLClause("AND","PG_ACCERTAMENTO_SCADENZARIO",sql.EQUALS, os.getPg_accertamento_scadenzario());
	List l =  osvHome.fetchAll(sql);
	getHomeCache().fetchAll(userContext);
	return l;
}
}
