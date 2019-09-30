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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.cnr.contab.config00.pdcep.bulk.Ass_cap_entrata_Cnr_conto_econom_ricavoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.core.bulk.Sospeso_det_uscBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_approvato_dip_areaHome extends BulkHome {
	public Pdg_approvato_dip_areaHome(Connection conn) {
		super(Pdg_approvato_dip_areaBulk.class, conn);
	}
	public Pdg_approvato_dip_areaHome(Connection conn, PersistentCache persistentCache) {
		super(Pdg_approvato_dip_areaBulk.class, conn, persistentCache);
	}

	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		Pdg_approvato_dip_areaBulk pdg_dip_area = (Pdg_approvato_dip_areaBulk)oggettobulk;
		Pdg_approvato_dip_areaBulk bulk = new Pdg_approvato_dip_areaBulk(
				pdg_dip_area.getEsercizio(),
				pdg_dip_area.getCd_dipartimento(),
				pdg_dip_area.getPg_dettaglio());
		pdg_dip_area.setEsercizio(CNRUserContext.getEsercizio(usercontext));
		if (pdg_dip_area.getPg_dettaglio()==null) {
			Integer max = (Integer)findMax(bulk, "pg_dettaglio", null);
			if (max!=null)
				pdg_dip_area.setPg_dettaglio(new Integer(max.intValue()+1));
			else
				pdg_dip_area.setPg_dettaglio(new Integer(1));
		}
		super.initializePrimaryKeyForInsert(usercontext, pdg_dip_area);
	}
	
	public java.util.Collection findPdgDipAreaDettagli(UserContext userContext, Contrattazione_speseVirtualBulk testata) throws IntrospectionException, PersistencyException {
		return findPdgDipAreaDettagli(userContext, testata, null);
	}

	public java.util.Collection findPdgDipAreaDettagli(UserContext userContext, Contrattazione_speseVirtualBulk testata, String cdDip) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_approvato_dip_areaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));

		if (cdDip != null)
			sql.addClause("AND","cd_dipartimento",SQLBuilder.EQUALS,cdDip);

		sql.addOrderBy("esercizio, cd_dipartimento, pg_dettaglio");	
		return dettHome.fetchAll(sql);
	}

	public java.util.Collection findPdgContrattazioneSpeseDettagli(UserContext userContext, Pdg_approvato_dip_areaBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_contrattazione_speseBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio_dip",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addClause("AND","cd_dipartimento",SQLBuilder.EQUALS,testata.getCd_dipartimento());
		sql.addClause("AND","pg_dettaglio_dip",SQLBuilder.EQUALS,testata.getPg_dettaglio());
		return dettHome.fetchAll(sql);
	}

	public BigDecimal calcolaTotaleApprovatoSpeseInterne( UserContext userContext, Pdg_approvato_dip_areaBulk appDipArea ) throws IntrospectionException,  PersistencyException 
	{
		BigDecimal impTotale = Utility.ZERO;
		
		for (java.util.Iterator i = findPdgContrattazioneSpeseDettagli(userContext,appDipArea).iterator(); i.hasNext();) 
	   	{
			Pdg_contrattazione_speseBulk contrSpese = (Pdg_contrattazione_speseBulk) i.next();
			impTotale = impTotale.add(Utility.nvl(contrSpese.getAppr_tot_spese_decentr_int()));
	   	}
		return impTotale;
	}
}