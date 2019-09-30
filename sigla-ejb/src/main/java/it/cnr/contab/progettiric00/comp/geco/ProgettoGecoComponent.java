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

package it.cnr.contab.progettiric00.comp.geco;

import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiIBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_attivitaBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progetto_operativoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ProgettoGecoComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<Geco_progettoIBulk> cercaProgettiGeco(UserContext userContext, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws ComponentException{
		try {
			ProgettoBulk progetto = (ProgettoBulk)oggettoBulk;
			BulkHome gecoProgettoHome = getHome(userContext, bulkClass);
			Geco_progettoBulk gecoProgettoDummy = new Geco_progettoBulk();
			SQLBuilder sql = gecoProgettoHome.createSQLBuilder();
			if (progetto!=null){
				if (progetto.getEsercizio() != null)
					gecoProgettoDummy.setEsercizio(new Long(progetto.getEsercizio()));
				if (progetto.getPg_progetto() != null)
					gecoProgettoDummy.setId_prog(new Long(progetto.getPg_progetto()));
				if (progetto.getTipo_fase() != null)
					gecoProgettoDummy.setFase(progetto.getTipo_fase());
			}
			sql.addClause(gecoProgettoDummy.buildFindClauses(new Boolean(true)));
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			return (List<Geco_progettoIBulk>)gecoProgettoHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Geco_commessaIBulk> cercaCommesseGeco(UserContext userContext, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws ComponentException{
		try {
			ProgettoBulk progetto = (ProgettoBulk)oggettoBulk;
			BulkHome gecoCommessaHome = getHome(userContext, bulkClass);
			Geco_commessaBulk gecoCommessaDummy = new Geco_commessaBulk();
			SQLBuilder sql = gecoCommessaHome.createSQLBuilder();
			if (progetto!=null){
				if (progetto.getEsercizio() != null)
					gecoCommessaDummy.setEsercizio(new Long(progetto.getEsercizio()));
				if (progetto.getPg_progetto() != null)
					gecoCommessaDummy.setId_comm(new Long(progetto.getPg_progetto()));
				if (progetto.getTipo_fase() != null)
					gecoCommessaDummy.setFase(progetto.getTipo_fase());
			}
			sql.addClause(gecoCommessaDummy.buildFindClauses(new Boolean(true)));
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			if (!bulkClass.equals(Geco_progetto_operativoBulk.class))
				sql.addClause("AND","cds",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
			else {
				BulkHome home = getHome(userContext, Geco_attivitaBulk.class);
				SQLBuilder sqlExists = getSqlModuliGeco(userContext,oggettoBulk,home);
				sqlExists.addSQLJoin("attivita.id_prog","progetto_operativo.id_prog");
				sql.addSQLExistsClause(FindClause.AND, sqlExists);
			}
			return (List<Geco_commessaIBulk>)gecoCommessaHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Geco_moduloIBulk> cercaModuliGeco(UserContext userContext, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws ComponentException{
		try {
			BulkHome gecoModuloHome = getHome(userContext, bulkClass);
			SQLBuilder sql = getSqlModuliGeco(userContext,oggettoBulk,gecoModuloHome);
			return (List<Geco_moduloIBulk>)gecoModuloHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private SQLBuilder getSqlModuliGeco(UserContext userContext, OggettoBulk oggettoBulk, BulkHome home) throws ComponentException{
		ProgettoBulk progetto = (ProgettoBulk)oggettoBulk;
		Geco_moduloBulk gecoModuloDummy = new Geco_moduloBulk();
		SQLBuilder sql = home.createSQLBuilder();
		if (progetto!=null){
			if (progetto.getEsercizio() != null)
				gecoModuloDummy.setEsercizio(new Long(progetto.getEsercizio()));
			if (progetto.getPg_progetto() != null)
				gecoModuloDummy.setId_mod(new Long(progetto.getPg_progetto()));
			if (progetto.getTipo_fase() != null)
				gecoModuloDummy.setFase(progetto.getTipo_fase());
		}
		sql.addClause(gecoModuloDummy.buildFindClauses(new Boolean(true)));
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND","cds_esec",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	public List<Geco_dipartimentiIBulk> cercaDipartimentiGeco(UserContext userContext, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws ComponentException{
		try {
			DipartimentoBulk dipartimento = (DipartimentoBulk)oggettoBulk;
			BulkHome gecoDipartimentoHome = getHome(userContext, bulkClass);
			Geco_dipartimentiBulk gecoDipartimentoDummy = new Geco_dipartimentiBulk();
			SQLBuilder sql = gecoDipartimentoHome.createSQLBuilder();
			if (dipartimento!=null){
				if (dipartimento.getCd_dipartimento() != null)
					gecoDipartimentoDummy.setCod_dip(dipartimento.getCd_dipartimento());
			}
			sql.addClause(gecoDipartimentoDummy.buildFindClauses(new Boolean(true)));
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			return (List<Geco_dipartimentiIBulk>)gecoDipartimentoHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	@Override
	public Connection getConnection(UserContext usercontext) throws ComponentException {
        try{
        	if (connection == null)
        		connection = EJBCommonServices.getDatasource("java:/jdbc/GECO").getConnection();
        	return connection;
        }catch(Exception exception){
            throw handleException(exception);
        }
	}
}
