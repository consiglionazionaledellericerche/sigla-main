/*
* Created by Generator 1.0
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.service.ContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
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
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
	
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}	
		//sql.addOrderBy("CD_CDS, PG_REVERSALE");
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
}