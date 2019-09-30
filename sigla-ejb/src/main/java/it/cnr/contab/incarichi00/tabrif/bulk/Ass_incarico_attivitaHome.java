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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_incarico_attivitaHome extends BulkHome {
	public Ass_incarico_attivitaHome(Connection conn) {
		super(Ass_incarico_attivitaBulk.class, conn);
	}
	public Ass_incarico_attivitaHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_incarico_attivitaBulk.class, conn, persistentCache);
	}
	public java.util.Collection findProceduraIncarichi(Ass_incarico_attivitaBulk bulk) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Incarichi_proceduraBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,bulk.getEsercizio());
		sql.addClause("AND","cd_tipo_incarico",SQLBuilder.EQUALS,bulk.getCd_tipo_incarico());
		sql.addClause("AND","cd_tipo_attivita",SQLBuilder.EQUALS,bulk.getCd_tipo_attivita());
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return dettHome.fetchAll(sql);
	}
	/**
	 * Metodo per inizializzare il campo Elemento_voce di una riga di un mandato
	 *
	 * @param riga <code>Mandato_rigaBulk</code> la riga del mandato
	 *
	 */
	public void initializeElemento_voce(UserContext userContext, Mandato_rigaBulk riga ) throws PersistencyException {
		if (riga.getElemento_voce()!=null) return;
		ObbligazioneBulk obbl = (ObbligazioneBulk)getHomeCache().getHome(ObbligazioneBulk.class).findByPrimaryKey(new ObbligazioneBulk(riga.getCd_cds(),riga.getEsercizio_obbligazione(),riga.getEsercizio_ori_obbligazione(),riga.getPg_obbligazione()));
		Elemento_voceBulk elemento_voce = (Elemento_voceBulk)getHomeCache().getHome(Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(obbl.getCd_elemento_voce(), obbl.getEsercizio(), obbl.getTi_appartenenza(), obbl.getTi_gestione()));
		riga.setElemento_voce(elemento_voce);
	}
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		if (persistent instanceof Ass_incarico_attivitaBulk) {
			if (((Ass_incarico_attivitaBulk)persistent).getTipo_attivita()!=null && 
				((Ass_incarico_attivitaBulk)persistent).getTipo_attivita().getCd_tipo_attivita()!=null) { 
				Tipo_attivitaBulk tipo_attivita = (Tipo_attivitaBulk)getHomeCache().getHome(Tipo_attivitaBulk.class).findByPrimaryKey(((Ass_incarico_attivitaBulk)persistent).getTipo_attivita());
				((Ass_incarico_attivitaBulk)persistent).setTipo_attivita(tipo_attivita);
			}
			if (((Ass_incarico_attivitaBulk)persistent).getTipo_incarico()!=null && 
				((Ass_incarico_attivitaBulk)persistent).getTipo_incarico().getCd_tipo_incarico()!=null) { 
				Tipo_incaricoBulk tipo_incarico = (Tipo_incaricoBulk)getHomeCache().getHome(Tipo_incaricoBulk.class).findByPrimaryKey(((Ass_incarico_attivitaBulk)persistent).getTipo_incarico());
				((Ass_incarico_attivitaBulk)persistent).setTipo_incarico(tipo_incarico);
			}
		}
		return super.completeBulkRowByRow(userContext, persistent);
	}
	
}