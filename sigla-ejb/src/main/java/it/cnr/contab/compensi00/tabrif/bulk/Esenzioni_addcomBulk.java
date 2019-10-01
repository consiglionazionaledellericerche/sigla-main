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
 * Date 14/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Esenzioni_addcomBulk extends Esenzioni_addcomBase {
	private ComuneBulk comune = new ComuneBulk();
	public Esenzioni_addcomBulk() {
		super();
	}
	public Esenzioni_addcomBulk(java.lang.String cd_catastale, java.sql.Timestamp dt_inizio_validita) {
		super(cd_catastale, dt_inizio_validita);
	}
	public ComuneBulk getComune() {
		return comune;
	}
	public void setComune(ComuneBulk comune) {
		this.comune = comune;
	}

	public void setPg_comune(Long pg_comune) {
		getComune().setPg_comune(pg_comune);
	}

	public Long getPg_comune() {
		if (comune==null)
			return null;
		else
			return getComune().getPg_comune();
	}

	public String getCd_catastale(){
		if (comune==null)
			return null;
		else
			return getComune().getCd_catastale();
	}
	
	public void setCd_catastale(String cd_catastale) {
		getComune().setCd_catastale(cd_catastale);
	}
	public void setDataFineValidita(java.sql.Timestamp newData) {
		
		this.setDt_fine_validita(newData);
	}
	public java.sql.Timestamp getDataFineValidita() {

		if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
			return null;
		return getDt_fine_validita();
	}
	public void validate() throws ValidationException {

		if ( getPg_comune() == null )
			throw new ValidationException("Il campo Comune non può essere vuoto");
		if ( getDt_inizio_validita() == null )
			throw new ValidationException( "Il campo Data Inizio Validita non può essere vuoto" );
		if ( getImporto() == null )
			throw new ValidationException("Il campo Importo Esenzione non può essere vuoto");
		
	}
}