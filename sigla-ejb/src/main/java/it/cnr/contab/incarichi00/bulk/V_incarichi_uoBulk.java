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
 * Date 09/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_uoBulk extends OggettoBulk implements Persistent {

	private BulkList incarichi_x_terzoColl = new BulkList();
	private BulkList incarichi_scadutiColl = new BulkList();
	private BulkList incarichi_validiColl = new BulkList();

//    ESERCIZIO_LIMITE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_limite;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    DS_UNITA_ORGANIZZATIVA VARCHAR(300) NOT NULL
	private java.lang.String ds_unita_organizzativa;
 
//    CD_TIPO_LIMITE VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_limite;
 
//    IM_INCARICHI DECIMAL(22,0)
	private java.math.BigDecimal im_incarichi;
 
	private java.math.BigDecimal prc_utilizzato;

	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getDs_unita_organizzativa() {
		return ds_unita_organizzativa;
	}
	public void setDs_unita_organizzativa(java.lang.String ds_unita_organizzativa)  {
		this.ds_unita_organizzativa=ds_unita_organizzativa;
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.math.BigDecimal getIm_incarichi() {
		return im_incarichi;
	}
	public void setIm_incarichi(java.math.BigDecimal im_incarichi)  {
		this.im_incarichi=im_incarichi;
	}
	public java.math.BigDecimal getPrc_utilizzato() {
		return prc_utilizzato;
	}
	public void setPrc_utilizzato(java.math.BigDecimal prc_utilizzato) {
		this.prc_utilizzato = prc_utilizzato;
	}
	public BulkList getIncarichi_x_terzoColl() {
		return incarichi_x_terzoColl;
	}
	public void setIncarichi_x_terzoColl(BulkList incarichi_x_terzoColl) {
		this.incarichi_x_terzoColl = incarichi_x_terzoColl;
	}
	public BulkList getIncarichi_scadutiColl() {
		return incarichi_scadutiColl;
	}
	public void setIncarichi_scadutiColl(BulkList incarichi_scadutiColl) {
		this.incarichi_scadutiColl = incarichi_scadutiColl;
	}
	public BulkList getIncarichi_validiColl() {
		return incarichi_validiColl;
	}
	public void setIncarichi_validiColl(BulkList incarichi_validiColl) {
		this.incarichi_validiColl = incarichi_validiColl;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 incarichi_x_terzoColl,
				 incarichi_scadutiColl,
				 incarichi_validiColl};
	}
	public java.math.BigDecimal getTot_incarichi_assegnati(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_x_terzoColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_terzoBulk)i.next()).getIm_incarichi());
		return totale;
	}
	public java.math.BigDecimal getTot_incarichi_validi(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_validiColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_da_assegnareBulk)i.next()).getIm_incarichi());
		return totale;
	}
	public java.math.BigDecimal getTot_incarichi_scaduti(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_scadutiColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_da_assegnareBulk)i.next()).getIm_incarichi());
		return totale;
	}
}