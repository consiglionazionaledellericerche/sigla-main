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
 * Date 16/04/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Acconto_classific_coriBulk extends Acconto_classific_coriBase {
	public final static String TIPO_ACCONTO_ADD_COM = "C1";
	public Acconto_classific_coriBulk() {
		super();
	}
	public Acconto_classific_coriBulk(java.lang.Integer esercizio, java.lang.Integer cd_anag, java.lang.String cd_classificazione_cori) {
		super(esercizio, cd_anag, cd_classificazione_cori);
	}
	//  PERCENTUALE DECIMAL(10,6) NULL
	private java.math.BigDecimal percentuale;
	private ComuneBulk comune = new ComuneBulk();
	private java.lang.String cd_catastale;
	private java.lang.String ds_comune;
	private AnagraficoBulk anagrafico = new AnagraficoBulk();
	private java.lang.String descrizioneAnagrafica;
	
	public java.math.BigDecimal getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(java.math.BigDecimal percentuale) {
		this.percentuale = percentuale;
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
	public String getDs_comune(){
		if (comune==null)
			return null;
		else
			return getComune().getDs_comune();
	}
	
	public void setDs_comune(String ds_comune) {
		getComune().setDs_comune(ds_comune);
	}
	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
	public void setAnagrafico(AnagraficoBulk anagrafico) {
		this.anagrafico = anagrafico;
	}
	public void setCd_anag(Integer cd_anag) {
		getAnagrafico().setCd_anag(cd_anag);
	}

	public Integer getCd_anag() {
		if (anagrafico==null)
			return null;
		else
			return getAnagrafico().getCd_anag();
	}
	public String getDescrizioneAnagrafica(){
		if (anagrafico==null)
			return null;
		else
			return getAnagrafico().getDescrizioneAnagrafica();
	}
}