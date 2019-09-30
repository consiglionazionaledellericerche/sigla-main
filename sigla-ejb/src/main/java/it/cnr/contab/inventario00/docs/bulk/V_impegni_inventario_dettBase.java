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
* Date 14/12/2006
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_impegni_inventario_dettBase extends OggettoBulk implements Persistent {
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    PG_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pg_documento;
 
//    PROGRESSIVO_RIGA DECIMAL(10,0)
	private java.lang.Long progressivo_riga;
 
//    TIPO VARCHAR(100)
	private java.lang.String tipo;
 
//    IM_IMPONIBILE DECIMAL(15,2)
	private java.math.BigDecimal im_imponibile;
 
//    IM_IVA DECIMAL(22,0)
	private java.math.BigDecimal im_iva;
 
//    PG_INVENTARIO DECIMAL(10,0)
	private java.lang.Long pg_inventario;
 
//    NR_INVENTARIO DECIMAL(10,0)
	private java.lang.Long nr_inventario;
 
//    PROGRESSIVO DECIMAL(3,0)
	private java.lang.Integer progressivo;
 
//    VALORE_UNITARIO DECIMAL(20,6)
	private java.math.BigDecimal valore_unitario;
 
//    TI_DOCUMENTO VARCHAR(10)
	private java.lang.String ti_documento;
 
//    PG_BUONO_C_S DECIMAL(10,0)
	private java.lang.Long pg_buono_c_s;
 
//    ESERCIZIO_IMP DECIMAL(4,0)
	private java.lang.Integer esercizio_imp;
 
//    CDS_IMP VARCHAR(30)
	private java.lang.String cds_imp;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0)
	private java.lang.Integer esercizio_originale;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;
 
//    IM_SCADENZA DECIMAL(15,2)
	private java.math.BigDecimal im_scadenza;
 
	public V_impegni_inventario_dettBase() {
		super();
	}
	
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_documento () {
		return pg_documento;
	}
	public void setPg_documento(java.lang.Long pg_documento)  {
		this.pg_documento=pg_documento;
	}
	public java.lang.Long getProgressivo_riga () {
		return progressivo_riga;
	}
	public void setProgressivo_riga(java.lang.Long progressivo_riga)  {
		this.progressivo_riga=progressivo_riga;
	}
	public java.lang.String getTipo () {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.math.BigDecimal getIm_imponibile () {
		return im_imponibile;
	}
	public void setIm_imponibile(java.math.BigDecimal im_imponibile)  {
		this.im_imponibile=im_imponibile;
	}
	public java.lang.Long getPg_inventario () {
		return pg_inventario;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getNr_inventario () {
		return nr_inventario;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
	public java.lang.Integer getProgressivo () {
		return progressivo;
	}
	public void setProgressivo(java.lang.Integer progressivo)  {
		this.progressivo=progressivo;
	}
	public java.math.BigDecimal getValore_unitario () {
		return valore_unitario;
	}
	public void setValore_unitario(java.math.BigDecimal valore_unitario)  {
		this.valore_unitario=valore_unitario;
	}
	public java.lang.String getTi_documento () {
		return ti_documento;
	}
	public void setTi_documento(java.lang.String ti_documento)  {
		this.ti_documento=ti_documento;
	}
	public java.lang.Long getPg_buono_c_s () {
		return pg_buono_c_s;
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s)  {
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public java.lang.Integer getEsercizio_imp () {
		return esercizio_imp;
	}
	public void setEsercizio_imp(java.lang.Integer esercizio_imp)  {
		this.esercizio_imp=esercizio_imp;
	}
	public java.lang.String getCds_imp () {
		return cds_imp;
	}
	public void setCds_imp(java.lang.String cds_imp)  {
		this.cds_imp=cds_imp;
	}
	public java.lang.Integer getEsercizio_originale () {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
	}
	public java.lang.Long getPg_obbligazione () {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione_scadenzario () {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario)  {
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
	}
	public java.math.BigDecimal getIm_scadenza () {
		return im_scadenza;
	}
	public void setIm_scadenza(java.math.BigDecimal im_scadenza)  {
		this.im_scadenza=im_scadenza;
	}

	public java.math.BigDecimal getIm_iva() {
		return im_iva;
	}

	public void setIm_iva(java.math.BigDecimal im_iva) {
		this.im_iva = im_iva;
	}
}