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
* Date 12/09/2005
*/
package it.cnr.contab.config00.sto.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_uo_areaBulk extends Ass_uo_areaBase {
	private Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	private CdsBulk cds_area_ricerca = new CdsBulk();

	public Ass_uo_areaBulk() {
		super();
	}
	public Ass_uo_areaBulk(java.lang.Integer esercizio, java.lang.String cd_unita_organizzativa, java.lang.String cd_area_ricerca) {
		super(esercizio, cd_unita_organizzativa, cd_area_ricerca);
		setUnita_organizzativa(new Unita_organizzativaBulk(cd_unita_organizzativa));
		setCds_area_ricerca(new CdsBulk(cd_area_ricerca));		
	}

	public CdsBulk getCds_area_ricerca() {
		return cds_area_ricerca;
	}

	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	public void setCds_area_ricerca(CdsBulk bulk) {
		cds_area_ricerca = bulk;
	}

	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}

	public String getCd_unita_organizzativa() {
		if (getUnita_organizzativa() == null)
			return null;
		return getUnita_organizzativa().getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}

	public String getCd_area_ricerca() {
		if (getCds_area_ricerca() == null)
			return null;
		return getCds_area_ricerca().getCd_unita_organizzativa();
	}
	
	public void setCd_area_ricerca(String cd_area_ricerca) {
		getCds_area_ricerca().setCd_unita_organizzativa(cd_area_ricerca);
	}

	public boolean isROArea_ricerca() {
//	   Check se l'unità padre è di tipo area
//	   Se l' UO padre è un'area, non posso collegare a questa UO un'area
		if (getUnita_organizzativa().getUnita_padre().getCd_tipo_unita() == null ||
		    getUnita_organizzativa().getUnita_padre().getCrudStatus() == UNDEFINED ||
			((getUnita_organizzativa().getUnita_padre().getCrudStatus() == NORMAL) && 
			  getUnita_organizzativa().getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA))) {
		   this.setCds_area_ricerca(null);
		   return true;
		} 
		else
		   return false;
	}

	public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForInsert(crudbp, actioncontext);
	}
}