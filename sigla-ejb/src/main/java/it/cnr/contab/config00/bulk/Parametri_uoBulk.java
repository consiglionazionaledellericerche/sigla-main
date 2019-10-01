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
* Date 17/11/2005
*/
package it.cnr.contab.config00.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Parametri_uoBulk extends Parametri_uoBase {
	private Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	private ProgettoBulk modulo_default = new ProgettoBulk();

	public Parametri_uoBulk() {
		super();
	}
	public Parametri_uoBulk(java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio) {
		super(cd_unita_organizzativa, esercizio);
		setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	/**
	 * @param CRUDBP
	 * @param ActionContext
	 */
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context){
		super.initializeForInsert(bp, context);
		setEsercizio(CNRUserInfo.getEsercizio(context) );
		setUnita_organizzativa(CNRUserInfo.getUnita_organizzativa(context));
		setFl_gestione_moduli( new Boolean( false ) );
		return this;
	}
	/**
	 * @return Unita_organizzativaBulk
	 */
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	/**
	 * @param bulk
	 */
	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}
	/**
	 * @return String
	 */
	public String getCd_unita_organizzativa() {
		return getUnita_organizzativa().getCd_unita_organizzativa();
	}
	/**
	 * @param String	 
	 */
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	/**
	 * @return ProgettoBulk
	 */
	public ProgettoBulk getModulo_default() {
		return modulo_default;
	}

	/**
	 * @param bulk
	 */
	public void setModulo_default(ProgettoBulk bulk) {
		modulo_default = bulk;
	}
	/**
	 * @return Integer
	 */
	public Integer getPg_modulo_default() {
		return getModulo_default().getPg_progetto();
	}
	/**
	 * @param Integer	 
	 */
	public void setPg_modulo_default(Integer pg_modulo_default) {
		// TODO Auto-generated method stub
		getModulo_default().setPg_progetto(pg_modulo_default);
	}
}