/*
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_pdg_variazione_cdrHome extends BulkHome {
	public Ass_pdg_variazione_cdrHome(java.sql.Connection conn) {
		super(Ass_pdg_variazione_cdrBulk.class, conn);
	}
	public Ass_pdg_variazione_cdrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_pdg_variazione_cdrBulk.class, conn, persistentCache);
	}
	public java.util.Collection findDettagliSpesa(Ass_pdg_variazione_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_preventivo_spe_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","PDG_PREVENTIVO_SPE_DET.ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliEntrata(Ass_pdg_variazione_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_preventivo_etr_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findDettagliSpesaGestionale(Ass_pdg_variazione_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO","CDR.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","PDG_MODULO_SPESE_GEST.ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE_GEST.PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliEntrataGestionale(Ass_pdg_variazione_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_entrate_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("PDG_MODULO_ENTRATE_GEST.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","PDG_MODULO_ENTRATE_GEST.ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_ENTRATE_GEST.PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati di entrata nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_entrata_gestBulk</code>
	 */
	public java.util.Collection findDettagliEntrataVariazioneGestionale(Ass_pdg_variazione_cdrBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_entrata_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","cd_cdr_assegnatario",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);

		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati di spese nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_gestBulk</code>
	 */
	public java.util.Collection findDettagliSpesaVariazioneGestionale(Ass_pdg_variazione_cdrBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_spesa_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","cd_cdr_assegnatario",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);

		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i Dipartimenti associati ai moduli delle GAE utilizzate sulla PDG_VARIAZIONE_RIGA_GEST 
	 * relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>DipartimentoBulk</code>
	 */
	public java.util.Collection findDipartimentiAssociati(Ass_pdg_variazione_cdrBulk testata) throws PersistencyException {
		PersistentHome prgHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = prgHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("DISTINCT PROGETTO.CD_DIPARTIMENTO");

		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO = "+testata.getEsercizio());
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG = "+testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO = '"+testata.getCd_centro_responsabilita()+"'");

		sql.addTableToHeader("LINEA_ATTIVITA");
		sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA","PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
		sql.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA","PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");

		sql.addTableToHeader("PROGETTO_GEST MODULO");
		sql.addSQLJoin("MODULO.ESERCIZIO","PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sql.addSQLJoin("MODULO.PG_PROGETTO","LINEA_ATTIVITA.PG_PROGETTO");
			
		sql.addTableToHeader("PROGETTO_GEST COMMESSA");
		sql.addSQLJoin("COMMESSA.ESERCIZIO","MODULO.ESERCIZIO_PROGETTO_PADRE");
		sql.addSQLJoin("COMMESSA.PG_PROGETTO","MODULO.PG_PROGETTO_PADRE");

		sql.addTableToHeader("PROGETTO_GEST PROGETTO");
		sql.addSQLJoin("PROGETTO.ESERCIZIO","COMMESSA.ESERCIZIO_PROGETTO_PADRE");
		sql.addSQLJoin("PROGETTO.PG_PROGETTO","COMMESSA.PG_PROGETTO_PADRE");
		
		PersistentHome dettHome = getHomeCache().getHome(DipartimentoBulk.class);
		SQLBuilder sqlDip = dettHome.createSQLBuilder();
		sqlDip.addSQLClause("AND", "DIPARTIMENTO.CD_DIPARTIMENTO IN ("+sql.getStatement()+")");

		return dettHome.fetchAll(sqlDip);
	}	
	/**
	 * Recupera tutti i Dipartimenti associati ai moduli delle GAE utilizzate sulla PDG_VARIAZIONE_RIGA_GEST 
	 * relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>DipartimentoBulk</code>
	 */
	public java.util.Collection findDettagliDipartimento(Ass_pdg_variazione_cdrBulk testata, DipartimentoBulk dip) throws PersistencyException {
		PersistentHome home = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = home.createSQLBuilder();

		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","cd_cdr_assegnatario",SQLBuilder.EQUALS,testata.getCd_centro_responsabilita());

		sql.addTableToHeader("LINEA_ATTIVITA");
		sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA","PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
		sql.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA","PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");

		sql.addTableToHeader("PROGETTO_GEST MODULO");
		sql.addSQLJoin("MODULO.ESERCIZIO","PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sql.addSQLJoin("MODULO.PG_PROGETTO","LINEA_ATTIVITA.PG_PROGETTO");
			
		sql.addTableToHeader("PROGETTO_GEST COMMESSA");
		sql.addSQLJoin("COMMESSA.ESERCIZIO","MODULO.ESERCIZIO_PROGETTO_PADRE");
		sql.addSQLJoin("COMMESSA.PG_PROGETTO","MODULO.PG_PROGETTO_PADRE");

		sql.addTableToHeader("PROGETTO_GEST PROGETTO");
		sql.addSQLJoin("PROGETTO.ESERCIZIO","COMMESSA.ESERCIZIO_PROGETTO_PADRE");
		sql.addSQLJoin("PROGETTO.PG_PROGETTO","COMMESSA.PG_PROGETTO_PADRE");
		sql.addSQLClause("AND", "PROGETTO.CD_DIPARTIMENTO",SQLBuilder.EQUALS,dip.getCd_dipartimento());

		return home.fetchAll(sql);
	}
	public java.util.Collection<Pdg_variazione_riga_gestBulk> findDettagliSpesa(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addSQLClause(FindClause.AND,"PG_VARIAZIONE_PDG",SQLBuilder.EQUALS,testata.getPg_variazione_pdg());
		return dettHome.fetchAll(sql);
	}	
}