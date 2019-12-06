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
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.service.ContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.persistence.PersistenceException;

public class V_cons_stato_invio_reversaliHome extends BulkHome {
	private ContabiliService contabiliService;
	
	public V_cons_stato_invio_reversaliHome(java.sql.Connection conn) {
		super(V_cons_stato_invio_reversaliBulk.class, conn);
		contabiliService = SpringUtil.getBean("contabiliService",
				ContabiliService.class);			
	}
	public V_cons_stato_invio_reversaliHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_stato_invio_reversaliBulk.class, conn, persistentCache);
		contabiliService = SpringUtil.getBean("contabiliService",
				ContabiliService.class);					
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
	{
		Configurazione_cnrHome configurazione_cnrHome = Optional.ofNullable(getHomeCache().getHome(Configurazione_cnrBulk.class))
				.filter(Configurazione_cnrHome.class::isInstance)
				.map(Configurazione_cnrHome.class::cast)
				.orElseThrow(() -> new PersistenceException("Home Configurazione_cnrHome non trovata!"));

		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(usercontext));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()) &&
				!configurazione_cnrHome.isUOSpecialeDistintaTuttaSAC(CNRUserContext.getEsercizio(usercontext),CNRUserContext.getCd_unita_organizzativa(usercontext))){
			sql.addSQLClause(FindClause.AND,"CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}
		return sql;
	}	
	@Override
	public Persistent completeBulkRowByRow(UserContext userContext,
			Persistent persistent) throws PersistencyException {
		V_cons_stato_invio_reversaliBulk cons = (V_cons_stato_invio_reversaliBulk)persistent;
		List<String> nodeRefs;
		try {
			nodeRefs = contabiliService.getNodeRefContabile(cons.getEsercizio().intValue(), cons.getCd_cds(), cons.getPg_reversale(), Numerazione_doc_contBulk.TIPO_REV);
			if (nodeRefs != null && !nodeRefs.isEmpty()){
				if (((CNRUserContext) userContext).isFromBootstrap()) {
					cons.setContabile("<a class='btn btn-link' onclick='" +
							"doVisualizzaSingolaContabile("+cons.getEsercizio()+",\""+cons.getCd_cds()+"\","+cons.getPg_reversale()+"); return false' " +
							"title='Visualizza Contabile'><i class='fa fa-fw fa-2x fa-file-pdf-o text-danger' aria-hidden='true'></i></a>");
				} else {
					cons.setContabile("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
							"doVisualizzaSingolaContabile("+cons.getEsercizio()+",\""+cons.getCd_cds()+"\","+cons.getPg_reversale()+"); return false' "+
							"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
							"title='Visualizza Contabile'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
				}
			}
		} catch (ApplicationException e) {			
		}
		return super.completeBulkRowByRow(userContext, persistent);		
	}


	public SQLBuilder selectByClauseForNonAcqisiti(UserContext usercontext, V_cons_stato_invio_reversaliBulk v_cons_stato_invio_reversaliBulk,
												   CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sqlBuilder = createSQLBuilder();
		if (compoundfindclause == null) {
			if (v_cons_stato_invio_reversaliBulk != null)
				compoundfindclause = v_cons_stato_invio_reversaliBulk.buildFindClauses(null);
		} else {
			compoundfindclause = CompoundFindClause.and(compoundfindclause, v_cons_stato_invio_reversaliBulk.buildFindClauses(Boolean.FALSE));
		}
		sqlBuilder.addClause(compoundfindclause);
		Configurazione_cnrHome configurazione_cnrHome = Optional.ofNullable(getHomeCache().getHome(Configurazione_cnrBulk.class))
				.filter(Configurazione_cnrHome.class::isInstance)
				.map(Configurazione_cnrHome.class::cast)
				.orElseThrow(() -> new PersistenceException("Home Configurazione_cnrHome non trovata!"));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco e se uo speciale
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()) &&
				!configurazione_cnrHome.isUOSpecialeDistintaTuttaSAC(CNRUserContext.getEsercizio(usercontext),CNRUserContext.getCd_unita_organizzativa(usercontext))){
			sqlBuilder.addSQLClause(FindClause.AND,"CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}
		sqlBuilder.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		sqlBuilder.addClause(FindClause.AND, "esitoOperazione", SQLBuilder.EQUALS, EsitoOperazione.NON_ACQUISITO.value());
		sqlBuilder.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS, ReversaleBulk.STATO_REVERSALE_ANNULLATO);
		return sqlBuilder;
	}
}