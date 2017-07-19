package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Liquidazione_ivaHome extends BulkHome {
public Liquidazione_ivaHome(java.sql.Connection conn) {
	super(Liquidazione_ivaBulk.class,conn);
}
public Liquidazione_ivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquidazione_ivaBulk.class,conn,persistentCache);
}
public java.util.List findRipartizioneFinanziariaList( Liquidazione_ivaVBulk bulk ) throws IntrospectionException,PersistencyException 
{

	PersistentHome home= getHomeCache().getHome(Liquidazione_iva_ripart_finBulk.class);

    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, bulk.getCd_cds());
	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bulk.getEsercizio());
	sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"tipo_liquidazione",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sql.addClause(FindClause.AND,"dt_inizio",SQLBuilder.EQUALS, bulk.getData_da());
	sql.addClause(FindClause.AND,"dt_fine",SQLBuilder.EQUALS, bulk.getData_a());
	sql.addOrderBy("esercizio_variazione");

	return home.fetchAll(sql);
}
public java.util.List<Liquidazione_ivaBulk> findLiquidazioniProvvisorieList( Liquidazione_ivaVBulk bulk ) throws IntrospectionException,PersistencyException 
{
	PersistentHome home= getHomeCache().getHome(Liquidazione_ivaBulk.class);

    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, bulk.getCd_cds());
	sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bulk.getEsercizio());
	sql.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.PROVVISORIO);
	sql.addClause(FindClause.AND,"tipo_liquidazione",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sql.addClause(FindClause.AND,"dt_inizio",SQLBuilder.EQUALS, bulk.getData_da());
	sql.addClause(FindClause.AND,"dt_fine",SQLBuilder.EQUALS, bulk.getData_a());
	sql.addOrderBy("report_id");

	return home.fetchAll(sql);
}

public java.util.List<Liquidazione_ivaBulk> findVariazioniAssociateList( Liquidazione_ivaVBulk bulk ) throws IntrospectionException,PersistencyException 
{
	PersistentHome home= getHomeCache().getHome(Liquidazione_iva_variazioniBulk.class);

    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, bulk.getCd_cds());
	sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bulk.getEsercizio());
	sql.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.DEFINITIVO);
	sql.addClause(FindClause.AND,"tipo_liquidazione",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sql.addClause(FindClause.AND,"dt_inizio",SQLBuilder.EQUALS, bulk.getData_da());
	sql.addClause(FindClause.AND,"dt_fine",SQLBuilder.EQUALS, bulk.getData_a());

	return home.fetchAll(sql);
}
}
