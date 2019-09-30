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

package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDConfigAssEvoldEvnewBP extends SimpleCRUDBP{
	private static final long serialVersionUID = 1L;
	
	public CRUDConfigAssEvoldEvnewBP() {
		super();
	}

	public CRUDConfigAssEvoldEvnewBP(String s) {
		super(s);
	}
	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config, actioncontext);
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		return initializeBulk(actioncontext, oggettobulk);
	}
	
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		return initializeBulk(actioncontext, oggettobulk);
	}
	
	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForSearch(actioncontext, oggettobulk);
		oggettobulk = initializeBulk(actioncontext, oggettobulk);
		((Ass_evold_evnewBulk)oggettobulk).setTi_gestione_old(((Ass_evold_evnewBulk)oggettobulk).getTi_gestione_search());
		((Ass_evold_evnewBulk)oggettobulk).setTi_gestione_new(((Ass_evold_evnewBulk)oggettobulk).getTi_gestione_search());
		((Ass_evold_evnewBulk)oggettobulk).setEsercizio_old(((Ass_evold_evnewBulk)oggettobulk).getEsercizio_old_search());
		((Ass_evold_evnewBulk)oggettobulk).setEsercizio_new(((Ass_evold_evnewBulk)oggettobulk).getEsercizio_new_search());
		return oggettobulk;
	}
	
	@Override
	public OggettoBulk initializeModelForFreeSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForFreeSearch(actioncontext, oggettobulk);
		oggettobulk = initializeBulk(actioncontext, oggettobulk);
		((Ass_evold_evnewBulk)oggettobulk).setTi_gestione_old(((Ass_evold_evnewBulk)oggettobulk).getTi_gestione_search());
		((Ass_evold_evnewBulk)oggettobulk).setTi_gestione_new(((Ass_evold_evnewBulk)oggettobulk).getTi_gestione_search());
		((Ass_evold_evnewBulk)oggettobulk).setEsercizio_old(((Ass_evold_evnewBulk)oggettobulk).getEsercizio_old_search());
		((Ass_evold_evnewBulk)oggettobulk).setEsercizio_new(((Ass_evold_evnewBulk)oggettobulk).getEsercizio_new_search());
		return oggettobulk;
	}
	
	private OggettoBulk initializeBulk(ActionContext actioncontext, OggettoBulk oggettobulk) {
		if (oggettobulk instanceof Ass_evold_evnewBulk) {
			((Ass_evold_evnewBulk)oggettobulk).setTi_gestione_search(((Ass_evold_evnewBulk)oggettobulk).getTi_gestione_old());
			((Ass_evold_evnewBulk)oggettobulk).setEsercizio_old_search(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			((Ass_evold_evnewBulk)oggettobulk).setEsercizio_new_search(CNRUserContext.getEsercizio(actioncontext.getUserContext())+1);
		}
		return oggettobulk;
	}
	
	public String getColumnLabelCd_elemento_voce_old(){
		return "Elemento Voce ".concat(((Ass_evold_evnewBulk)getModel()).getEsercizio_old().toString());
	}

	public String getColumnLabelCd_elemento_voce_new(){
		return "Elemento Voce ".concat(((Ass_evold_evnewBulk)getModel()).getEsercizio_new().toString());
	}
}
