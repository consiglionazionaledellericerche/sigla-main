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

package it.cnr.contab.config00.comp;

import java.util.List;

import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class CRUDConfigAssEvoldEvnewComponent extends it.cnr.jada.comp.CRUDComponent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			super.validaCreaConBulk(usercontext, oggettobulk);
			
			Ass_evold_evnewBulk ass = (Ass_evold_evnewBulk)oggettobulk;
			
			if (Elemento_voceHome.GESTIONE_SPESE.equals(ass.getTi_gestione_search())) {
				SQLBuilder sqlObb = ((ObbligazioneHome)getHome(usercontext, ObbligazioneBulk.class)).createSQLBuilder();
				sqlObb.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, ass.getElemento_voce_old().getEsercizio());
				sqlObb.addSQLClause(FindClause.AND, "TI_APPARTENENZA", SQLBuilder.EQUALS, ass.getElemento_voce_old().getTi_appartenenza());
				sqlObb.addSQLClause(FindClause.AND, "TI_GESTIONE", SQLBuilder.EQUALS, ass.getElemento_voce_old().getTi_gestione());
				sqlObb.addSQLClause(FindClause.AND, "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, ass.getElemento_voce_old().getCd_elemento_voce());
				sqlObb.addSQLClause(FindClause.AND, "ESERCIZIO_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlObb.addSQLClause(FindClause.AND, "TI_APPARTENENZA_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlObb.addSQLClause(FindClause.AND, "TI_GESTIONE_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlObb.addSQLClause(FindClause.AND, "CD_ELEMENTO_VOCE_NEXT", SQLBuilder.ISNOTNULL, null);

				if (sqlObb.executeCountQuery(getConnection(usercontext)) > 0)
					throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare associazioni per la voce "+
						ass.getEsercizio_old()+"/"+ass.getTi_gestione_old()+"/"+ass.getCd_elemento_voce_old()+
						" in quanto esistono impegni sulla voce in oggetto in cui è stata indicata la voce di ribaltamento!");
			} else {
				SQLBuilder sqlAcc = ((AccertamentoHome)getHome(usercontext, AccertamentoBulk.class)).createSQLBuilder();
				sqlAcc.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, ass.getElemento_voce_old().getEsercizio());
				sqlAcc.addSQLClause(FindClause.AND, "TI_APPARTENENZA", SQLBuilder.EQUALS, ass.getElemento_voce_old().getTi_appartenenza());
				sqlAcc.addSQLClause(FindClause.AND, "TI_GESTIONE", SQLBuilder.EQUALS, ass.getElemento_voce_old().getTi_gestione());
				sqlAcc.addSQLClause(FindClause.AND, "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, ass.getElemento_voce_old().getCd_elemento_voce());
				sqlAcc.addSQLClause(FindClause.AND, "ESERCIZIO_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlAcc.addSQLClause(FindClause.AND, "TI_APPARTENENZA_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlAcc.addSQLClause(FindClause.AND, "TI_GESTIONE_EV_NEXT", SQLBuilder.ISNOTNULL, null);
				sqlAcc.addSQLClause(FindClause.AND, "CD_ELEMENTO_VOCE_NEXT", SQLBuilder.ISNOTNULL, null);

				if (sqlAcc.executeCountQuery(getConnection(usercontext)) > 0)
					throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare associazioni per la voce "+
						ass.getEsercizio_old()+"/"+ass.getTi_gestione_old()+"/"+ass.getCd_elemento_voce_old()+
						" in quanto esistono accertamenti sulla voce in oggetto in cui è stata indicata la voce di ribaltamento!");
			}
		}catch(Exception ex){
			throw handleException(ex);
		}
	}
	
	public Elemento_voceBulk getCurrentElementoVoce(UserContext usercontext, Elemento_voceBulk voceOld, int annoVoceNew) throws ComponentException {
		try {
			Ass_evold_evnewHome ass_evold_evnewHome = (Ass_evold_evnewHome) getHome( usercontext, Ass_evold_evnewBulk.class);
			Elemento_voceBulk currentVoce = voceOld;
			while (currentVoce!=null && currentVoce.getEsercizio().compareTo(annoVoceNew)<0) {
				//cerco la voce del nuovo anno
				List listVociNew = ass_evold_evnewHome.findAssElementoVoceNewList(currentVoce);
				if (!listVociNew.isEmpty()) {
					if (listVociNew.size()>1)
						throw new ApplicationException("Trovate nella tabella di associazione Vecchie/Nuove Voci più elementi voce nel nuovo anno per la Voce: "+ currentVoce.getCd_voce() +" nell'esercizio: "+currentVoce.getCd_elemento_voce());
					currentVoce = (Elemento_voceBulk)listVociNew.get(0);
				} else {
					currentVoce = (Elemento_voceBulk)getHome(usercontext,Elemento_voceBulk.class).findByPrimaryKey(
							  new Elemento_voceBulk(currentVoce.getCd_elemento_voce(),currentVoce.getEsercizio()+1,currentVoce.getTi_appartenenza(),currentVoce.getTi_gestione())
							  );
				}
			}
			return currentVoce;
		}catch(Exception ex){
			throw handleException(ex);
		}		
	}
}
