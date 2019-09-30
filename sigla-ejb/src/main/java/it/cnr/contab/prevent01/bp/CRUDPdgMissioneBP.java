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
 * Created on Oct 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDPdgMissioneBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private final SimpleDetailCRUDController crudTipiUoAssociabili = new SimpleDetailCRUDController("tipiUoAssociabili",Tipo_unita_organizzativaBulk.class,"tipiUoAssociabili",this);
	private final SimpleDetailCRUDController crudAssPdgMissioneTipiUo = new SimpleDetailCRUDController("assPdgMissioneTipoUoColl",Ass_pdg_missione_tipo_uoBulk.class,"assPdgMissioneTipoUoColl",this);
	
	public CRUDPdgMissioneBP() throws BusinessProcessException {
		super();
		initTipiUoTable();
	}
	
	public CRUDPdgMissioneBP( String function ) throws BusinessProcessException {
		super( function );
		initTipiUoTable();
	}
	
//	@SuppressWarnings("rawtypes")
	public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
//		Pdg_missioneBulk missione = (Pdg_missioneBulk)this.getModel();
//		it.cnr.jada.bulk.BulkList tipiUoCollegati = missione.getTipi_uo_collegati();
//		it.cnr.jada.bulk.BulkList tipiUoDisponibili = missione.getTipi_uo_disponibili();

		super.save(actioncontext);
		
//		updateTipiUo(actioncontext.getUserContext(), tipiUoCollegati);
//		updateTipiUo(actioncontext.getUserContext(), tipiUoDisponibili);
	}

	@SuppressWarnings("rawtypes")
	public void updateTipiUo(it.cnr.jada.UserContext userContext, BulkList tipiUo) throws it.cnr.jada.action.BusinessProcessException {
		try{
			CRUDComponentSession sess = (CRUDComponentSession)createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
			for (java.util.Iterator i = tipiUo.iterator();i.hasNext();) 
			{
				OggettoBulk obj = (OggettoBulk) i.next();
				if (obj.isToBeCreated() || obj.isToBeDeleted())
					sess.modificaConBulk(userContext, obj);
			}
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public SimpleDetailCRUDController getCrudTipiUoAssociabili() {
		return crudTipiUoAssociabili;
	}
	
	public SimpleDetailCRUDController getCrudAssPdgMissioneTipiUo() {
		return crudAssPdgMissioneTipiUo;
	}
	
	private void initTipiUoTable() {
		crudTipiUoAssociabili.setPaged(true);
		crudTipiUoAssociabili.setPageSize(10);
		crudAssPdgMissioneTipiUo.setPaged(true);
		crudAssPdgMissioneTipiUo.setPageSize(10);
	}
	
	public void addToAssPdgMissioneTipiUo(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		int[] indexes = this.getCrudTipiUoAssociabili().getSelectedRows(actioncontext);
	
		java.util.Arrays.sort( indexes );
		for (int index = indexes.length - 1 ;index >= 0 ;index--) {
			Tipo_unita_organizzativaBulk tipoUo = (Tipo_unita_organizzativaBulk)this.getCrudTipiUoAssociabili().removeDetail(indexes[index]);
			tipoUo.setCrudStatus(OggettoBulk.NORMAL);

			Ass_pdg_missione_tipo_uoBulk assTipoUo = new Ass_pdg_missione_tipo_uoBulk();
			assTipoUo.setTipoUnitaOrganizzativa(tipoUo);
			this.getCrudAssPdgMissioneTipiUo().addDetail(assTipoUo);
		}
		this.getCrudTipiUoAssociabili().getSelection().clear();
		this.getCrudAssPdgMissioneTipiUo().getSelection().clear();
	}

	public void removeFromAssPdgMissioneTipiUo(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		int[] indexes = this.getCrudAssPdgMissioneTipiUo().getSelectedRows(actioncontext);
	
		java.util.Arrays.sort( indexes );
		for (int index = indexes.length - 1 ;index >= 0 ;index--) {	
			Ass_pdg_missione_tipo_uoBulk assTipoUo = (Ass_pdg_missione_tipo_uoBulk)this.getCrudAssPdgMissioneTipiUo().removeDetail(indexes[index]);
			this.getCrudTipiUoAssociabili().addDetail(assTipoUo.getTipoUnitaOrganizzativa());
			assTipoUo.getTipoUnitaOrganizzativa().setCrudStatus(OggettoBulk.NORMAL);
		}
		this.getCrudTipiUoAssociabili().getSelection().clear();
		this.getCrudAssPdgMissioneTipiUo().getSelection().clear();
	}

}
