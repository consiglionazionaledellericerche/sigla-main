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
* Date 01/03/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Inventario_beni_apgBase extends Inventario_beni_apgKey implements Keyed {
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    PG_FATTURA DECIMAL(10,0)
	private java.lang.Long pg_fattura;
 
//    PROGRESSIVO_RIGA DECIMAL(10,0)
	private java.lang.Long progressivo_riga;
 
//    PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_inventario;
 
//    NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario;
 
//    PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;
 

 
//    VARIAZIONE_MENO DECIMAL(15,2)
	private java.math.BigDecimal variazione_meno;
 
//    FL_TOTALMENTE_SCARICATO CHAR(1)
	private java.lang.Boolean fl_totalmente_scaricato;
 
//    FL_VISIBILE CHAR(1)
	private java.lang.Boolean fl_visibile;
 
//    VALORE_ALIENAZIONE DECIMAL(15,2)
	private java.math.BigDecimal valore_alienazione;
 
//    VARIAZIONE_PIU DECIMAL(15,2)
	private java.math.BigDecimal variazione_piu;

	private java.math.BigDecimal imp_fattura;
 
//    ID_BENE_ORIGINE VARCHAR(30)
	private java.lang.String id_bene_origine;
 
//    FL_MIGRATO CHAR(1)
	private java.lang.Boolean fl_migrato;
 
//    FL_TRASF_COME_PRINCIPALE CHAR(1)
	private java.lang.Boolean fl_trasf_come_principale;
 
//    PG_INVENTARIO_PRINCIPALE DECIMAL(10,0)
	private java.lang.Long pg_inventario_principale;
 
//    NR_INVENTARIO_PRINCIPALE DECIMAL(10,0)
	private java.lang.Long nr_inventario_principale;
 
//    PROGRESSIVO_PRINCIPALE DECIMAL(10,0)
	private java.lang.Long progressivo_principale;
 
	private java.lang.Long pg_buono_c_s;
	
	private java.sql.Timestamp dt_validita_variazione;
	
	private String cd_tipo_documento_amm;
	
	private java.lang.String cd_categoria_gruppo_new;
	
	public java.lang.Long getPg_buono_c_s() {
		return pg_buono_c_s;
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s) {
		this.pg_buono_c_s = pg_buono_c_s;
	}
	public String getTi_documento() {
		return ti_documento;
	}
	public void setTi_documento(String ti_documento) {
		this.ti_documento = ti_documento;
	}
	private String ti_documento;
	
	public Inventario_beni_apgBase() {
		super();
	}
	public Inventario_beni_apgBase(Long riga,String id) {
		super(riga,id);
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
	public java.lang.Long getPg_fattura () {
		return pg_fattura;
	}
	public void setPg_fattura(java.lang.Long pg_fattura)  {
		this.pg_fattura=pg_fattura;
	}
	public java.lang.Long getProgressivo_riga () {
		return progressivo_riga;
	}
	public void setProgressivo_riga(java.lang.Long progressivo_riga)  {
		this.progressivo_riga=progressivo_riga;
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
	public java.lang.Long getProgressivo () {
		return progressivo;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	
	public java.math.BigDecimal getVariazione_meno () {
		return variazione_meno;
	}
	public void setVariazione_meno(java.math.BigDecimal variazione_meno)  {
		this.variazione_meno=variazione_meno;
	}
	public java.lang.Boolean getFl_totalmente_scaricato () {
		return fl_totalmente_scaricato;
	}
	public void setFl_totalmente_scaricato(java.lang.Boolean fl_totalmente_scaricato)  {
		this.fl_totalmente_scaricato=fl_totalmente_scaricato;
	}
	public java.lang.Boolean getFl_visibile () {
		return fl_visibile;
	}
	public void setFl_visibile(java.lang.Boolean fl_visibile)  {
		this.fl_visibile=fl_visibile;
	}
	public java.math.BigDecimal getValore_alienazione () {
		return valore_alienazione;
	}
	public void setValore_alienazione(java.math.BigDecimal valore_alienazione)  {
		this.valore_alienazione=valore_alienazione;
	}
	public java.math.BigDecimal getVariazione_piu () {
		return variazione_piu;
	}
	public void setVariazione_piu(java.math.BigDecimal variazione_piu)  {
		this.variazione_piu=variazione_piu;
	}
	public java.lang.String getId_bene_origine () {
		return id_bene_origine;
	}
	public void setId_bene_origine(java.lang.String id_bene_origine)  {
		this.id_bene_origine=id_bene_origine;
	}
	public java.lang.Boolean getFl_migrato () {
		return fl_migrato;
	}
	public void setFl_migrato(java.lang.Boolean fl_migrato)  {
		this.fl_migrato=fl_migrato;
	}
	public java.lang.Boolean getFl_trasf_come_principale () {
		return fl_trasf_come_principale;
	}
	public void setFl_trasf_come_principale(java.lang.Boolean fl_trasf_come_principale)  {
		this.fl_trasf_come_principale=fl_trasf_come_principale;
	}
	public java.lang.Long getPg_inventario_principale () {
		return pg_inventario_principale;
	}
	public void setPg_inventario_principale(java.lang.Long pg_inventario_principale)  {
		this.pg_inventario_principale=pg_inventario_principale;
	}
	public java.lang.Long getNr_inventario_principale () {
		return nr_inventario_principale;
	}
	public void setNr_inventario_principale(java.lang.Long nr_inventario_principale)  {
		this.nr_inventario_principale=nr_inventario_principale;
	}
	public java.lang.Long getProgressivo_principale () {
		return progressivo_principale;
	}
	public void setProgressivo_principale(java.lang.Long progressivo_principale)  {
		this.progressivo_principale=progressivo_principale;
	}
	public java.sql.Timestamp getDt_validita_variazione() {
		return dt_validita_variazione;
	}
	public void setDt_validita_variazione(java.sql.Timestamp dt_validita_variazione) {
		this.dt_validita_variazione = dt_validita_variazione;
	}
	public java.math.BigDecimal getImp_fattura() {
		return imp_fattura;
	}
	public void setImp_fattura(java.math.BigDecimal imp_fattura) {
		this.imp_fattura = imp_fattura;
	}
	public String getCd_tipo_documento_amm() {
		return cd_tipo_documento_amm;
	}
	public void setCd_tipo_documento_amm(String cd_tipo_documento_amm) {
		this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	}
	public java.lang.String getCd_categoria_gruppo_new() {
		return cd_categoria_gruppo_new;
	}
	public void setCd_categoria_gruppo_new(java.lang.String cd_categoria_gruppo_new) {
		this.cd_categoria_gruppo_new = cd_categoria_gruppo_new;
	}
	
}