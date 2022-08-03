/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Dettaglio_contrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Dettaglio_contrattoHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class OrdineAcqRigaHome extends BulkHome {
	public OrdineAcqRigaHome(Connection conn) {
		super(OrdineAcqRigaBulk.class, conn);
	}
	public OrdineAcqRigaHome(Connection conn, PersistentCache persistentCache) {
		super(OrdineAcqRigaBulk.class, conn, persistentCache);
	}

	public Dettaglio_contrattoBulk recuperoDettaglioContratto(OrdineAcqRigaBulk riga) throws PersistencyException {
		if (riga == null || riga.getOrdineAcq() == null || riga.getOrdineAcq().getContratto() == null|| riga.getOrdineAcq().getContratto().getPg_contratto() == null
				|| riga.getBeneServizio() == null ||riga.getOrdineAcq().getContratto().getTipo_dettaglio_contratto() == null){
			return null;
		}
		ContrattoBulk contratto = riga.getOrdineAcq().getContratto();
		Dettaglio_contrattoHome home = ((Dettaglio_contrattoHome)getHomeCache().getHome(Dettaglio_contrattoBulk.class));
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.STATO", SQLBuilder.NOT_EQUALS, Dettaglio_contrattoBulk.STATO_ANNULLATO);
		sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.PG_CONTRATTO", SQLBuilder.EQUALS, riga.getOrdineAcq().getContratto().getPg_contratto());
		sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.ESERCIZIO_CONTRATTO", SQLBuilder.EQUALS, riga.getOrdineAcq().getContratto().getEsercizio());
		sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.STATO_CONTRATTO", SQLBuilder.EQUALS, riga.getOrdineAcq().getContratto().getStato());
			if (contratto.isDettaglioContrattoPerArticoli()){
				sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.CD_BENE_SERVIZIO", SQLBuilder.EQUALS, riga.getBeneServizio().getCd_bene_servizio());
			} else if (contratto.isDettaglioContrattoPerCategoriaGruppo()){
				Bene_servizioHome homeBene = ((Bene_servizioHome)getHomeCache().getHome(Bene_servizioBulk.class));
				Bene_servizioBulk bene_servizioBulk = (Bene_servizioBulk)homeBene.findByPrimaryKey(riga.getBeneServizio());
				if (bene_servizioBulk != null){
					sql.addSQLClause("AND", "DETTAGLIO_CONTRATTO.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, bene_servizioBulk.getCategoria_gruppo().getCd_categoria_gruppo());
				} else {
					return null;
				}
			}
		Collection righe = home.fetchAll(sql);
		if (righe != null && righe.size() == 1){
			return (Dettaglio_contrattoBulk) righe.iterator().next();
		}
		return null;
	}

	public SQLBuilder selectDspUopDestByClause(UserContext userContext, OrdineAcqRigaBulk riga,
													  UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk,
													  CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);

		sql.addSQLClause("AND","UNITA_OPERATIVA_ORD.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));

		return sql;
	}


}