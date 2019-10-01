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
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Gruppo_cr_uoBulk extends Gruppo_cr_uoBase {
	Gruppo_crBulk gruppo=new Gruppo_crBulk();
	Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	
	public Gruppo_cr_uoBulk() {
		super();
	}
	public Gruppo_cr_uoBulk(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_unita_organizzativa) {
		super(esercizio, cd_gruppo_cr, cd_unita_organizzativa);
	}
	
	public void setEsercizio(Integer esercizio) {
		getGruppo().setEsercizio(esercizio);
	}
	
	public Integer getEsercizio() {
		if(getGruppo()==null)
			return null;
		else
			return getGruppo().getEsercizio();
	}
	
	public String getCd_gruppo_cr() {
		if(getGruppo()==null)
			return null;
		else
			return getGruppo().getCd_gruppo_cr();
	}
	
	public void setCd_gruppo_cr(String cd_gruppo_cr) {
		getGruppo().setCd_gruppo_cr(cd_gruppo_cr);
	}
	
	public String getCd_unita_organizzativa() {
		if(getUnita_organizzativa()==null)
			return null;
		else
			return getUnita_organizzativa().getCd_unita_organizzativa();
	}
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	public Gruppo_crBulk getGruppo() {
		return gruppo;
	}
	public void setGruppo(Gruppo_crBulk gruppo) {
		this.gruppo = gruppo;
	}
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	public void setUnita_organizzativa(Unita_organizzativaBulk unita_organizzativa) {
		this.unita_organizzativa = unita_organizzativa;
	}
	protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		return super.initialize(crudbp, actioncontext);
	}
	public void validate() throws ValidationException 
	{
		if ((getCd_gruppo_cr() == null ) || (getCd_gruppo_cr().trim().length() == 0))
			throw new ValidationException( "Il campo Gruppo deve essere valorizzato!" );

		if ((getCd_unita_organizzativa() == null ) || (getCd_unita_organizzativa().trim().length() == 0))
			throw new ValidationException( "Il campo Unità Organizzativa deve essere valorizzato" );

	}

}