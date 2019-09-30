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

package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AssIncaricoAttivitaComponent extends CRUDComponent {
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		return sql;
	}

	public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Ass_incarico_attivitaBulk) {
				Ass_incarico_attivitaBulk incAttBulk = (Ass_incarico_attivitaBulk)oggettobulk;
				Ass_incarico_attivitaHome incAttHome = (Ass_incarico_attivitaHome)getHome( usercontext, Ass_incarico_attivitaBulk.class);

				if (!incAttHome.findProceduraIncarichi(incAttBulk).isEmpty())
					throw new ComponentException("Eliminazione non possibile! Risulta essere già stata effettuata almeno una procedura per il conferimento di incarichi per il tipo incarico/attività che si desidera eliminare.");
			}
			super.eliminaConBulk(usercontext, oggettobulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}