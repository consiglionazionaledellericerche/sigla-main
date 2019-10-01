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
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Tipo_cr_baseBulk extends Tipo_cr_baseBase {
	private Tipo_contributo_ritenutaBulk contributo_ritenuta = new Tipo_contributo_ritenutaBulk();
	Gruppo_crBulk gruppo=new Gruppo_crBulk();
	public Tipo_cr_baseBulk() {
		super();
	}
	public Tipo_cr_baseBulk(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta) {
		super(esercizio, cd_contributo_ritenuta);
	}
	public Tipo_contributo_ritenutaBulk getContributo_ritenuta() {
		return contributo_ritenuta;
	}
	public void setContributo_ritenuta(
			Tipo_contributo_ritenutaBulk contributo_ritenuta) {
		this.contributo_ritenuta = contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk contributo_ritenuta = this.getContributo_ritenuta();
		if (contributo_ritenuta == null)
			return null;
		return contributo_ritenuta.getCd_contributo_ritenuta();
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
		this.getContributo_ritenuta().setCd_contributo_ritenuta(cd_contributo_ritenuta);
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
	public Gruppo_crBulk getGruppo() {
		return gruppo;
	}
	public void setGruppo(Gruppo_crBulk gruppo) {
		this.gruppo = gruppo;
	}
	protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
	
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		return super.initialize(crudbp, actioncontext);
	}
	public void validate() throws ValidationException 
	{
		
		// controllo su campo CONTRIBUTO RITENUTA
		if ((getCd_contributo_ritenuta() == null ) || (getCd_contributo_ritenuta().trim().length() == 0))
			throw new ValidationException( "Il campo Contributo Ritenuta deve essere valorizzato!" );
		if ((getCd_gruppo_cr() == null ) || (getCd_gruppo_cr().trim().length() == 0))
			throw new ValidationException( "Il campo Gruppo deve essere valorizzato!" );


	}

}