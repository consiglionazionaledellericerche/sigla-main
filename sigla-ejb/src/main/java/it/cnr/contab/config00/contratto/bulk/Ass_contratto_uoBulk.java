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
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_contratto_uoBulk extends Ass_contratto_uoBase {
	private ContrattoBulk contratto;
	private Unita_organizzativaBulk unita_organizzativa; 
	
	public Ass_contratto_uoBulk() {
		super();
	}
	public Ass_contratto_uoBulk(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.String cd_unita_organizzativa) {
		super(esercizio, stato_contratto, pg_contratto, cd_unita_organizzativa);
		setContratto(new ContrattoBulk(esercizio,stato_contratto,pg_contratto));
		setUnita_organizzativa(new Unita_organizzativaBulk(cd_unita_organizzativa));
	}
	/**
	 * @return
	 */
	public ContrattoBulk getContratto() {
		return contratto;
	}

	/**
	 * @return
	 */
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	/**
	 * @param bulk
	 */
	public void setContratto(ContrattoBulk bulk) {
		contratto = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#setEsercizio(java.lang.Integer)
	 */	
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getContratto().setEsercizio(esercizio);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#getEsercizio()
	 */
	public java.lang.Integer getEsercizio () {
		return getContratto().getEsercizio();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#setPg_contratto(java.lang.Long)
	 */
	public void setPg_contratto(java.lang.Long pg_contratto)  {
		this.getContratto().setPg_contratto(pg_contratto);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#getPg_contratto()
	 */
	public java.lang.Long getPg_contratto () {
		return getContratto().getPg_contratto();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#setCd_unita_organizzativa(java.lang.String)
	 */
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#getCd_unita_organizzativa()
	 */
	public java.lang.String getCd_unita_organizzativa () {
		return getUnita_organizzativa().getCd_unita_organizzativa();
	}

}