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
* Date 20/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_pdg_etrBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(400)
	private java.lang.String ds_progetto;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(400)
	private java.lang.String ds_commessa;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(400)
	private java.lang.String ds_modulo;
 
//    TIPO_PROGETTO VARCHAR(10)
	private java.lang.String tipo_progetto;
 
//    CDS VARCHAR(30)
	private java.lang.String cds;
 
//    DS_CDS VARCHAR(300)
	private java.lang.String ds_cds;
 
//    UO VARCHAR(30)
	private java.lang.String uo;
 
//    TITOLO VARCHAR(20)
	private java.lang.String titolo;
 
//    DS_TITOLO VARCHAR(100)
	private java.lang.String ds_titolo;
 
//    CODICE_CLAS_ENTRATA VARCHAR(20)
	private java.lang.String codice_clas_entrata;
 
//    DS_CLASSIFICAZIONE_ENTRATA VARCHAR(400)
	private java.lang.String ds_classificazione_entrata;
 
//    ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String elemento_voce;
 
//    NATURA VARCHAR(1)
	private java.lang.String natura;
 
//    IM_ENTRATE_RICAVI DECIMAL(15,2)
	private java.math.BigDecimal im_entrate_ricavi;
 
//    IM_ENTRATE_SENZA_RICAVI DECIMAL(15,2)
	private java.math.BigDecimal im_entrate_senza_ricavi;
 
	public V_pdg_etrBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto () {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.String getCd_modulo () {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo)  {
		this.cd_modulo=cd_modulo;
	}
	public java.lang.String getDs_modulo () {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getTipo_progetto () {
		return tipo_progetto;
	}
	public void setTipo_progetto(java.lang.String tipo_progetto)  {
		this.tipo_progetto=tipo_progetto;
	}
	public java.lang.String getCds () {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getDs_cds () {
		return ds_cds;
	}
	public void setDs_cds(java.lang.String ds_cds)  {
		this.ds_cds=ds_cds;
	}
	public java.lang.String getUo () {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getTitolo () {
		return titolo;
	}
	public void setTitolo(java.lang.String titolo)  {
		this.titolo=titolo;
	}
	public java.lang.String getDs_titolo () {
		return ds_titolo;
	}
	public void setDs_titolo(java.lang.String ds_titolo)  {
		this.ds_titolo=ds_titolo;
	}
	public java.lang.String getCodice_clas_entrata () {
		return codice_clas_entrata;
	}
	public void setCodice_clas_entrata(java.lang.String codice_clas_entrata)  {
		this.codice_clas_entrata=codice_clas_entrata;
	}
	public java.lang.String getDs_classificazione_entrata () {
		return ds_classificazione_entrata;
	}
	public void setDs_classificazione_entrata(java.lang.String ds_classificazione_entrata)  {
		this.ds_classificazione_entrata=ds_classificazione_entrata;
	}
	public java.lang.String getElemento_voce () {
		return elemento_voce;
	}
	public void setElemento_voce(java.lang.String elemento_voce)  {
		this.elemento_voce=elemento_voce;
	}
	public java.lang.String getNatura () {
		return natura;
	}
	public void setNatura(java.lang.String natura)  {
		this.natura=natura;
	}
	public java.math.BigDecimal getIm_entrate_ricavi () {
		return im_entrate_ricavi;
	}
	public void setIm_entrate_ricavi(java.math.BigDecimal im_entrate_ricavi)  {
		this.im_entrate_ricavi=im_entrate_ricavi;
	}
	public java.math.BigDecimal getIm_entrate_senza_ricavi () {
		return im_entrate_senza_ricavi;
	}
	public void setIm_entrate_senza_ricavi(java.math.BigDecimal im_entrate_senza_ricavi)  {
		this.im_entrate_senza_ricavi=im_entrate_senza_ricavi;
	}
}