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
 * Date 02/10/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Stipendi_cofi_obbBulk extends Stipendi_cofi_obbBase {
	public Stipendi_cofi_obbBulk() {
		super();
	}
	public Stipendi_cofi_obbBulk(java.lang.Integer esercizio, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione) {
		super(esercizio, cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione);
		setObbligazioni(new ObbligazioneBulk(cd_cds_obbligazione,esercizio_obbligazione,esercizio_ori_obbligazione,pg_obbligazione));
	}
	
	public ObbligazioneBulk obbligazioni = new ObbligazioneBulk();
	
	public ObbligazioneBulk getObbligazioni() {
		return obbligazioni;
	}
	public void setObbligazioni(ObbligazioneBulk obbligazioni) {
		this.obbligazioni = obbligazioni;
	}
	public Integer getEsercizio_obbligazione() {
		if (getObbligazioni()==null) 
			return null;
		return getObbligazioni().getEsercizio();
	}
	public void setEsercizio_obbligazione(Integer es)	{		
		this.getObbligazioni().setEsercizio(es);
	}

	public String getCd_cds_obbligazione() {
		if (getObbligazioni()==null) 
			return null;
		return getObbligazioni().getCds().getCd_unita_organizzativa();
	}

	public void setCd_cds_obbligazione(String cds)
	{		
		this.getObbligazioni().getCds().setCd_unita_organizzativa(cds);
	}
	public Integer getEsercizio_ori_obbligazione() {
		if (getObbligazioni()==null) 
			return null;
		return getObbligazioni().getEsercizio_originale();
	}

	public void setEsercizio_ori_obbligazione(Integer es_ori)	{		
		this.getObbligazioni().setEsercizio_originale(es_ori);
	}
	public Long getPg_obbligazione() {
		if (getObbligazioni()==null) 
			return null;
		return getObbligazioni().getPg_obbligazione();
	}
	public void setPg_obbligazione(Long pg)
	{		
		this.getObbligazioni().setPg_obbligazione(pg);
	}
	public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext) );
		return super.initializeForInsert(crudbp, actioncontext);
	}
}