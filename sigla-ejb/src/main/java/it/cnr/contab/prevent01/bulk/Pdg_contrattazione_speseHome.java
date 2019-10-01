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
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
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
public class Pdg_contrattazione_speseHome extends BulkHome {
	public Pdg_contrattazione_speseHome(java.sql.Connection conn) {
		super(Pdg_contrattazione_speseBulk.class, conn);
	}
	public Pdg_contrattazione_speseHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_contrattazione_speseBulk.class, conn, persistentCache);
	}

	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		Pdg_contrattazione_speseBulk pdg_contrattazione_spese = (Pdg_contrattazione_speseBulk)oggettobulk;
		if (pdg_contrattazione_spese.getEsercizio()==null)
			pdg_contrattazione_spese.setEsercizio(CNRUserContext.getEsercizio(usercontext));
		Pdg_contrattazione_speseBulk bulk = new Pdg_contrattazione_speseBulk(
				pdg_contrattazione_spese.getEsercizio(),
				pdg_contrattazione_spese.getCd_centro_responsabilita(),
				pdg_contrattazione_spese.getPg_progetto(),
				null);
		if (pdg_contrattazione_spese.getPg_dettaglio()==null) {
			Integer max = (Integer)findMax(bulk, "pg_dettaglio", null);
			if (max!=null)
				pdg_contrattazione_spese.setPg_dettaglio(new Integer(max.intValue()+1));
			else
				pdg_contrattazione_spese.setPg_dettaglio(new Integer(1));
		}

		super.initializePrimaryKeyForInsert(usercontext, pdg_contrattazione_spese);
	}

	public java.util.Collection findPdgContrSpeseDettagli(UserContext userContext, Pdg_approvato_dip_areaBulk pdg_dip_area) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_contrattazione_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI");
		sql.addSQLJoin("PDG_CONTRATTAZIONE_SPESE.ID_CLASSIFICAZIONE","V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE (+)");
		sql.addClause("AND","esercizio_dip",sql.EQUALS,pdg_dip_area.getEsercizio());
		sql.addClause("AND","cd_dipartimento",sql.EQUALS,pdg_dip_area.getCd_dipartimento());
		sql.addClause("AND","pg_dettaglio_dip",sql.EQUALS,pdg_dip_area.getPg_dettaglio());
		sql.addOrderBy("V_CLASSIFICAZIONE_VOCI.cd_classificazione");	
		return dettHome.fetchAll(sql);
	}

	public SQLBuilder calcolaTotalePropostoModificatoFE(it.cnr.jada.UserContext userContext,Pdg_contrattazione_speseBulk pdg, CdsBulk area) throws IntrospectionException, PersistencyException {
		Parametri_cnrBulk pcnr = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
		pcnr = (Parametri_cnrBulk) getHomeCache().getHome(Parametri_cnrBulk.class).findByPrimaryKey(pcnr);
		Integer livContrSpe = pcnr.getLivello_contratt_pdg_spe();

		Pdg_modulo_speseHome home = (Pdg_modulo_speseHome)getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(IM_SPESE_GEST_DECENTRATA_EST)");
		//sql.addColumn("SUM(IM_SPESE_GEST_DECENTRATA_INT)");
		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
		sql.addSQLJoin("PDG_MODULO_SPESE.ID_CLASSIFICAZIONE","V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());

		if (area!=null && area.getCd_unita_organizzativa()!=null)
			sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",SQLBuilder.EQUALS,area.getCd_unita_organizzativa());
		
		if (pdg.getId_classificazione()!=null) { 
			sql.addSQLClause("AND","V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+livContrSpe,SQLBuilder.EQUALS,pdg.getId_classificazione());
		}
		return sql;
	}

	public SQLBuilder calcolaTotalePropostoModificatoFE(it.cnr.jada.UserContext userContext,Pdg_contrattazione_speseBulk pdg) throws IntrospectionException, PersistencyException {
		return calcolaTotalePropostoModificatoFE(userContext, pdg, null); 
	}

	public SQLBuilder calcolaTotalePropostoModificatoFI(it.cnr.jada.UserContext userContext,Pdg_contrattazione_speseBulk pdg, CdsBulk area) throws IntrospectionException, PersistencyException {
		Parametri_cnrBulk pcnr = new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext));
		pcnr = (Parametri_cnrBulk) getHomeCache().getHome(Parametri_cnrBulk.class).findByPrimaryKey(pcnr);
		Integer livContrSpe = pcnr.getLivello_contratt_pdg_spe();

		Pdg_modulo_speseHome home = (Pdg_modulo_speseHome)getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.resetColumns();
		//sql.addColumn("SUM(IM_SPESE_GEST_DECENTRATA_EST)");
		sql.addColumn("SUM(IM_SPESE_GEST_DECENTRATA_INT)");
		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
		sql.addSQLJoin("PDG_MODULO_SPESE.ID_CLASSIFICAZIONE","V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
		sql.addSQLClause("AND","PDG_MODULO_SPESE.ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO_SPESE.PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());

		if (area!=null && area.getCd_unita_organizzativa()!=null)
			sql.addSQLClause("AND","PDG_MODULO_SPESE.CD_CDS_AREA",SQLBuilder.EQUALS,area.getCd_unita_organizzativa());
		
		if (pdg.getId_classificazione()!=null) { 
			sql.addSQLClause("AND","V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+livContrSpe,SQLBuilder.EQUALS,pdg.getId_classificazione());
		}
		return sql;
	}
	public SQLBuilder calcolaTotalePropostoModificatoFI(it.cnr.jada.UserContext userContext,Pdg_contrattazione_speseBulk pdg) throws IntrospectionException, PersistencyException {
		return calcolaTotalePropostoModificatoFI(userContext, pdg, null); 
	}
}