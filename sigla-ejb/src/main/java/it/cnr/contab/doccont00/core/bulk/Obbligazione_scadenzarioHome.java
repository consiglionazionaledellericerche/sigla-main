package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.sql.*;

public class Obbligazione_scadenzarioHome extends BulkHome implements IScadenzaDocumentoContabileHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Obbligazione_scadenzarioHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Obbligazione_scadenzarioHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Obbligazione_scadenzarioHome(java.sql.Connection conn) {
	super(Obbligazione_scadenzarioBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scadenzarioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Obbligazione_scadenzarioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Obbligazione_scadenzarioBulk.class,conn,persistentCache);
}

public void aggiornaImportoAssociatoADocAmm(
	UserContext userContext, 	
	IScadenzaDocumentoContabileBulk scadenzaDocCont)
	throws PersistencyException, BusyResourceException, OutdatedResourceException {

	if (scadenzaDocCont == null) return;

	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)scadenzaDocCont;
	try {
		((BulkHome)getHomeCache().getHome(scadenza.getFather().getClass())).lock((OggettoBulk)scadenza.getFather());
		//getHomeCache().getHome(scadenza.getFather().getClass()).update((Persistent)scadenza.getFather());
	} catch (OutdatedResourceException e) {
	}
		
	lock(scadenza);
	
	getHomeCache().getHome(Obbligazione_scadenzarioBulk.class, "IMPORTO_ASSOCIATO").update(scadenza, userContext);

	//StringBuffer stm = new StringBuffer("UPDATE ");
	//stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
	//stm.append(getColumnMap().getTableName());
	//stm.append(" SET IM_ASSOCIATO_DOC_AMM = ?,  WHERE (");
	//stm.append("CD_CDS = ? AND ESERCIZIO = ? AND ESERCIZIO_ORIGINALE = ? AND PG_OBBLIGAZIONE = ? AND PG_OBBLIGAZIONE_SCADENZARIO = ? )");

	//try {
		//PreparedStatement ps = getConnection().prepareStatement(stm.toString());
		//try {	
			//ps.setBigDecimal(1, scadenza.getIm_associato_doc_amm());
			//ps.setString(2, scadenza.getCd_cds());
			//ps.setInt(3, scadenza.getEsercizio().intValue());
			//ps.setInt(4, scadenza.getEsercizio_originale().intValue());
			//ps.setLong(5, scadenza.getPg_obbligazione().longValue());
			//ps.setLong(6, scadenza.getPg_obbligazione_scadenzario().longValue());

			//LoggableStatement.executeUpdate(ps);
		//} finally {
			//try{ps.close();}catch( java.sql.SQLException e ){};
		//}
	//} catch(java.sql.SQLException e) {
		//throw SQLExceptionHandler.getInstance().handleSQLException(e,scadenza);
	//}
}
/**
 * Metodo per cercare i documenti passivi associati all'obbligazione.
 *
 * @param os <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione
 *
 * @return <code>V_doc_passivo_obbligazioneBulk</code> i documenti passivi associati all'obbligazione
 * 		   null non è stato trovato nessun documento passivo associato all'obbligazione
 *
 */
public V_doc_passivo_obbligazioneBulk findDoc_passivo( Obbligazione_scadenzarioBulk os ) throws IntrospectionException,PersistencyException 
{
	PersistentHome docHome = getHomeCache().getHome(V_doc_passivo_obbligazioneBulk.class );
	SQLBuilder sql = docHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS_OBBLIGAZIONE",sql.EQUALS, os.getCd_cds());	
	sql.addSQLClause("AND","ESERCIZIO_OBBLIGAZIONE",sql.EQUALS, os.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORI_OBBLIGAZIONE",sql.EQUALS, os.getEsercizio_originale());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE",sql.EQUALS, os.getPg_obbligazione());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE_SCADENZARIO",sql.EQUALS, os.getPg_obbligazione_scadenzario());
	sql.addOrderBy("FL_SELEZIONE DESC");
	List<V_doc_passivo_obbligazioneBulk> l = docHome.fetchAll(sql);
	return Optional.ofNullable(l.stream()
						.filter(e->e.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_ORDINE))
						.findFirst().orElse(null))
				.orElse(l.stream().findFirst().orElse(null));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param os	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public Mandato_rigaBulk findMandato( Obbligazione_scadenzarioBulk os ) throws IntrospectionException,PersistencyException 
{
	PersistentHome mrHome = getHomeCache().getHome(Mandato_rigaIBulk.class );
	SQLBuilder sql = mrHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, os.getCd_cds());		
	sql.addSQLClause("AND","ESERCIZIO_OBBLIGAZIONE",sql.EQUALS, os.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORI_OBBLIGAZIONE",sql.EQUALS, os.getEsercizio_originale());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE",sql.EQUALS, os.getPg_obbligazione());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE_SCADENZARIO",sql.EQUALS, os.getPg_obbligazione_scadenzario());
	sql.addSQLClause("AND","STATO",sql.NOT_EQUALS, Mandato_rigaBulk.STATO_ANNULLATO);		
	List l =  mrHome.fetchAll(sql);
	if ( l.size() > 0 )
		return (Mandato_rigaBulk) l.get(0);
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param os	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findObbligazione_scad_voceList( it.cnr.jada.UserContext userContext,Obbligazione_scadenzarioBulk os ) throws IntrospectionException,PersistencyException 
{
//	PersistentHome osvHome = getHomeCache().getHome(Obbligazione_scad_voceBulk.class, "default", "it.cnr.contab.doccont00.comp.ObbligazioneComponent.edit" );
	PersistentHome osvHome = getHomeCache().getHome(Obbligazione_scad_voceBulk.class );
	SQLBuilder sql = osvHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, os.getObbligazione().getCds().getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, os.getObbligazione().getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORIGINALE",sql.EQUALS, os.getObbligazione().getEsercizio_originale());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE",sql.EQUALS, os.getObbligazione().getPg_obbligazione());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE_SCADENZARIO",sql.EQUALS, os.getPg_obbligazione_scadenzario());
	sql.addOrderBy("CD_LINEA_ATTIVITA");
	List l =  osvHome.fetchAll(sql);
	getHomeCache().fetchAll(userContext);
	return l;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoKeys( Obbligazione_scadenzarioBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "S" );
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
}
