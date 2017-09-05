package it.cnr.contab.gestiva00.core.bulk;
import java.math.BigDecimal;

import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
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

public java.util.List<Liquidazione_ivaBulk> findLiquidazioniMassiveProvvisorieList( Liquidazione_ivaVBulk bulk ) throws IntrospectionException,PersistencyException 
{
	PersistentHome home= getHomeCache().getHome(Liquidazione_ivaBulk.class);
    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bulk.getEsercizio());
	sql.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.PROVVISORIO);
	sql.addClause(FindClause.AND,"tipo_liquidazione",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sql.addClause(FindClause.AND,"dt_inizio",SQLBuilder.EQUALS, bulk.getData_da());
	sql.addClause(FindClause.AND,"dt_fine",SQLBuilder.EQUALS, bulk.getData_a());
	sql.addClause(FindClause.AND,"report_id",SQLBuilder.EQUALS, Liquidazione_massa_provvisoria_ivaVBulk.REPORTID);
	sql.addClause(FindClause.AND,"iva_da_versare",SQLBuilder.LESS, BigDecimal.ZERO);
	
	SQLBuilder sqlNotExists = home.createSQLBuilder();
	sqlNotExists.resetColumns();
	sqlNotExists.addColumn("1");
	sqlNotExists.setFromClause(null);
	sqlNotExists.addTableToHeader("LIQUIDAZIONE_IVA", "LIQPRD");

	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.ESERCIZIO",SQLBuilder.EQUALS, bulk.getEsercizio());
	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.STATO",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.DEFINITIVO);
	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.TIPO_LIQUIDAZIONE",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.DT_INIZIO",SQLBuilder.EQUALS, bulk.getData_da());
	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.DT_FINE",SQLBuilder.EQUALS, bulk.getData_a());
	sqlNotExists.addSQLClause(FindClause.AND,"LIQPRD.REPORT_ID",SQLBuilder.EQUALS, BigDecimal.ZERO);
	sqlNotExists.addSQLJoin("LIQUIDAZIONE_IVA.CD_UNITA_ORGANIZZATIVA", "LIQPRD.CD_UNITA_ORGANIZZATIVA");

	sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);

	sql.addOrderBy("cd_unita_organizzativa");

	return home.fetchAll(sql);
}

public java.util.List<Liquidazione_ivaBulk> findLiquidazioniMassiveDefinitiveList( Liquidazione_ivaVBulk bulk ) throws IntrospectionException,PersistencyException 
{
	PersistentHome home= getHomeCache().getHome(Liquidazione_ivaBulk.class);

    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bulk.getEsercizio());
	sql.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.DEFINITIVO);
	sql.addClause(FindClause.AND,"tipo_liquidazione",SQLBuilder.EQUALS, bulk.getTipoSezionaleFlag());
	sql.addClause(FindClause.AND,"dt_inizio",SQLBuilder.EQUALS, bulk.getData_da());
	sql.addClause(FindClause.AND,"dt_fine",SQLBuilder.EQUALS, bulk.getData_a());
	sql.addClause(FindClause.AND,"report_id",SQLBuilder.EQUALS, BigDecimal.ZERO);
	
	sql.addOrderBy("cd_unita_organizzativa");

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
public java.util.List findMandatoRigheAssociateList( Liquidazione_ivaBulk bulk ) throws IntrospectionException,PersistencyException 
{

	PersistentHome home= getHomeCache().getHome(Mandato_rigaIBulk.class);

    SQLBuilder sql= home.createSQLBuilder();

	sql.addClause(FindClause.AND,"cd_tipo_documento_amm",SQLBuilder.EQUALS, bulk.getCd_tipo_documento());
	sql.addClause(FindClause.AND,"cd_cds_doc_amm",SQLBuilder.EQUALS, bulk.getCd_cds_doc_amm());
	sql.addClause(FindClause.AND,"cd_uo_doc_amm",SQLBuilder.EQUALS, bulk.getCd_uo_doc_amm());
	sql.addClause(FindClause.AND,"esercizio_doc_amm",SQLBuilder.EQUALS, bulk.getEsercizio_doc_amm());
	sql.addClause(FindClause.AND,"pg_doc_amm",SQLBuilder.EQUALS, bulk.getPg_doc_amm());
	sql.addOrderBy("esercizio_ori_obbligazione");

	return home.fetchAll(sql);
}
}
