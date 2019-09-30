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
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.comp;

import java.util.List;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class PdgMissioneComponent extends it.cnr.jada.comp.CRUDComponent {
	private static final long serialVersionUID = 1L;

	public PdgMissioneComponent() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<Tipo_unita_organizzativaBulk> findTipiUoAssociabili(UserContext userContext, Pdg_missioneBulk missione) throws it.cnr.jada.comp.ComponentException {
		try
		{
			Tipo_unita_organizzativaHome home = ((Tipo_unita_organizzativaHome)getHome(userContext, Tipo_unita_organizzativaBulk.class));
			SQLBuilder sql = home.createSQLBuilder();

			SQLBuilder sqlExist = getHome(userContext, Ass_pdg_missione_tipo_uoBulk.class).createSQLBuilder();		   
			sqlExist.addSQLJoin("TIPO_UNITA_ORGANIZZATIVA.CD_TIPO_UNITA","ASS_PDG_MISSIONE_TIPO_UO.CD_TIPO_UNITA");
			sqlExist.addSQLClause(FindClause.AND,"ASS_PDG_MISSIONE_TIPO_UO.CD_MISSIONE",SQLBuilder.EQUALS,missione.getCd_missione());
			sql.addSQLNotExistsClause(FindClause.AND,sqlExist);
			
			return home.fetchAll(sql);
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}
	
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			
			if (oggettobulk instanceof Pdg_missioneBulk) {
				Pdg_missioneBulk missione = (Pdg_missioneBulk)oggettobulk;
				Pdg_missioneHome pdgMissioneHome = (Pdg_missioneHome) getHome( usercontext, Pdg_missioneBulk.class);
				missione.setAssPdgMissioneTipoUoColl(new BulkList<OggettoBulk>( pdgMissioneHome.findAss_pdg_missione_tipo_uoList(missione)));
				missione.setTipiUoAssociabili(new BulkList<Tipo_unita_organizzativaBulk>(findTipiUoAssociabili(usercontext, missione)));
				getHomeCache(usercontext).fetchAll(usercontext);
			}
			return oggettobulk;
		} catch (Exception e) {
			throw handleException( e );
		}
	}
	
	@Override
	protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
			throws ComponentException, PersistencyException {
//		Pdg_missioneBulk missione = (Pdg_missioneBulk)oggettobulk;
//		it.cnr.jada.bulk.BulkList tipiUoCollegati = missione.getTipi_uo_collegati();
//		it.cnr.jada.bulk.BulkList tipiUoDisponibili = missione.getTipi_uo_disponibili();
//
//		missione = (Pdg_missioneBulk)super.eseguiCreaConBulk(usercontext, missione);
//		
//		for (java.util.Iterator i = tipiUoCollegati.iterator();i.hasNext();) {
//			OggettoBulk obj = (OggettoBulk) i.next();
//			if (obj.isToBeCreated() || obj.isToBeDeleted())
//				super.modificaConBulk(usercontext, obj);
//		}
//
//		// TODO Auto-generated method stub
		return super.eseguiCreaConBulk(usercontext, oggettobulk);

	
	}
}