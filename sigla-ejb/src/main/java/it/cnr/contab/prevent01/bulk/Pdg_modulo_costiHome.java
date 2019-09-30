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
* Created by Generator 1.0
* Date 28/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_modulo_costiHome extends BulkHome {
	public Pdg_modulo_costiHome(java.sql.Connection conn) {
		super(Pdg_modulo_costiBulk.class, conn);
	}
	public Pdg_modulo_costiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_modulo_costiBulk.class, conn, persistentCache);
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.bulk.BulkHome#initializePrimaryKeyForInsert(it.cnr.jada.UserContext, it.cnr.jada.bulk.OggettoBulk)
	 */
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		((Pdg_modulo_costiBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(usercontext));
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}
	public java.util.Collection findPdgModuloSpeseDettagli(UserContext userContext, Pdg_modulo_costiBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class,null,"it.cnr.contab.prevent01.comp.PdgModuloCostiComponent.edit");
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findPdgModuloContrSpeseDettagli(UserContext userContext, Pdg_modulo_costiBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_contrattazione_speseBulk.class,null,"it.cnr.contab.prevent01.comp.PdgModuloCostiComponent.edit");
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI");
		sql.addSQLJoin("PDG_CONTRATTAZIONE_SPESE.ID_CLASSIFICAZIONE","V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE (+)");
		sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		sql.addOrderBy("V_CLASSIFICAZIONE_VOCI.cd_classificazione");	
		//sql.addOrderBy("cd_cds_area");	
		return dettHome.fetchAll(sql);
	}
	public SQLBuilder calcolaTotMassaSpendibileAnnoPrecedente(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi, Integer anno_precedente) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Voce_f_saldi_cdr_lineaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(VOCE_F_SALDI_CDR_LINEA.IM_STANZ_INIZIALE_A1 + VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_PIU - VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_MENO) TOTALE");
		sql.addTableToHeader("LINEA_ATTIVITA");
		sql.addTableToHeader("CDR");
		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLClause("AND","LINEA_ATTIVITA.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		sql.addSQLClause("AND","VOCE_F_SALDI_CDR_LINEA.ESERCIZIO",sql.EQUALS,anno_precedente);
		sql.addSQLClause("AND","VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES",sql.EQUALS,anno_precedente);
		sql.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA");
		sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");		
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",sql.EQUALS,pdg_modulo_costi.getPdg_modulo().getCdr().getCd_cds());
		sql.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA","VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
		sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA","VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND","VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA",sql.EQUALS,Voce_f_saldi_cdr_lineaBulk.TIPO_APPARTENENZA_CDS);
		sql.addSQLClause("AND","VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);		
		return sql;
	}
	public SQLBuilder calcolaTotMassaSpendibileAnnoInCorsoSpese(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_INT + PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_EST) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	public SQLBuilder calcolaTotMassaSpendibileAnnoInCorsoCosti(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_costiBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_COSTI.RIS_PRES_ES_PREC_TIT_I+PDG_MODULO_COSTI.RIS_PRES_ES_PREC_TIT_II) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_COSTI.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_COSTI.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_COSTI.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	public SQLBuilder calcolaValorePresuntoAttivitaAnnoInCorsoSpese(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_SPESE.IM_SPESE_GEST_ACCENTRATA_INT + PDG_MODULO_SPESE.IM_SPESE_GEST_ACCENTRATA_EST + PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_INT + PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_EST) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	public SQLBuilder calcolaValorePresuntoAttivitaAnnoInCorsoCosti(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_costiBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_COSTI.IM_COSTI_GENERALI+PDG_MODULO_COSTI.IM_CF_TFR+PDG_MODULO_COSTI.IM_CF_TFR_DET+PDG_MODULO_COSTI.IM_CF_AMM_IMMOBILI+PDG_MODULO_COSTI.IM_CF_AMM_ATTREZZ+PDG_MODULO_COSTI.IM_CF_AMM_ALTRO+PDG_MODULO_COSTI.RIS_PRES_ES_PREC_TIT_I+PDG_MODULO_COSTI.RIS_PRES_ES_PREC_TIT_II) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_COSTI.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_COSTI.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_COSTI.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	public SQLBuilder calcolaTotEntratePrevisteAnnoInCorso(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_Modulo_EntrateBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_ENTRATE.IM_ENTRATA) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_ENTRATE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_ENTRATE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_ENTRATE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());

		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("PDG_MODULO_ENTRATE.CD_NATURA","NATURA.CD_NATURA");
		sql.addSQLClause("AND","NATURA.TIPO",SQLBuilder.EQUALS,NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);
		
		CdrBulk cdr = (CdrBulk)getHomeCache().getHome(CdrBulk.class).findByPrimaryKey(pdg_modulo_costi.getPdg_modulo().getCdr());
		cdr.setUnita_padre((Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));
		if (pdg_modulo_costi.getPdg_modulo().getCdr().isCdrSAC()) {
			sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
			sql.addSQLJoin("PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE","V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
			sql.addSQLClause("AND","V_CLASSIFICAZIONE_VOCI_ALL.FL_ESTERNA_DA_QUADRARE_SAC",SQLBuilder.EQUALS,"Y");
		}
		return sql;
	}
	public SQLBuilder calcolaTotaleSpeseCoperteFontiEsterneAnnoInCorso(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_EST + PDG_MODULO_SPESE.IM_SPESE_GEST_ACCENTRATA_EST) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	public SQLBuilder calcolaSpeseDecentrateFontiInterneIstituto(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = calcolaSpeseDecentrateFontiInterne(userContext,pdg_modulo_costi);
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",sql.EQUALS,pdg_modulo_costi.getPdg_modulo().getCdr().getCd_cds());		
		return sql;
	}
	public SQLBuilder calcolaSpeseDecentrateFontiInterneArea(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = calcolaSpeseDecentrateFontiInterne(userContext,pdg_modulo_costi);
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",sql.NOT_EQUALS,pdg_modulo_costi.getPdg_modulo().getCdr().getCd_cds());		
		return sql;
	}

	public SQLBuilder calcolaSpeseAccentrateFontiInterneIstituto(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = calcolaSpeseAccentrateFontiInterne(userContext,pdg_modulo_costi);
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",sql.EQUALS,pdg_modulo_costi.getPdg_modulo().getCdr().getCd_cds());		
		return sql;
	}
	public SQLBuilder calcolaSpeseAccentrateFontiInterneArea(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = calcolaSpeseAccentrateFontiInterne(userContext,pdg_modulo_costi);
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",sql.NOT_EQUALS,pdg_modulo_costi.getPdg_modulo().getCdr().getCd_cds());		
		return sql;
	}

	private SQLBuilder calcolaSpeseDecentrateFontiInterne(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_SPESE.IM_SPESE_GEST_DECENTRATA_INT) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
	private SQLBuilder calcolaSpeseAccentrateFontiInterne(it.cnr.jada.UserContext userContext,Pdg_modulo_costiBulk pdg_modulo_costi) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_SPESE.IM_SPESE_GEST_ACCENTRATA_INT) TOTALE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",sql.EQUALS,pdg_modulo_costi.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_costi.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",sql.EQUALS,pdg_modulo_costi.getPg_progetto());		
		return sql;
	}
		
}