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
* Date 21/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_cons_registro_inventarioBase extends OggettoBulk implements Persistent {
	private java.lang.Long pg_inventario;
	private java.lang.String ti_documento;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_buono_c_s;
	private java.lang.Long nr_inventario;
	private java.lang.Integer progressivo;
	private java.lang.Integer esercizio_amm;
	private java.lang.String cd_utilizzatore_cdr;
	private java.lang.String cd_linea_attivita;
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    ESERCIZIO_CARICO_BENE DECIMAL(4,0)
	private java.lang.Integer esercizio_carico_bene;
 
//    ETICHETTA VARCHAR(50)
	private java.lang.String etichetta;
 
//    DS_BENE VARCHAR(100)
	private java.lang.String ds_bene;
 
//    VALORE_INIZIALE DECIMAL(22,0)
	private java.math.BigDecimal valore_iniziale;
 
//    CATEGORIA VARCHAR(1)
	private java.lang.String categoria;
 
//    CD_CATEGORIA_GRUPPO VARCHAR(10)
	private java.lang.String cd_categoria_gruppo;
 
//    DS_CATEGORIA_GRUPPO VARCHAR(100)
	private java.lang.String ds_categoria_gruppo;
 
//    DS_UBICAZIONE_BENE VARCHAR(300)
	private java.lang.String ds_ubicazione_bene;
 
//    CD_ASSEGNATARIO DECIMAL(8,0)
	private java.lang.Integer cd_assegnatario;
 
//    DENOMINAZIONE_SEDE VARCHAR(200)
	private java.lang.String denominazione_sede;
 
//    DATA_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp data_registrazione;
 
//    CD_TIPO_CARICO_SCARICO VARCHAR(10)
	private java.lang.String cd_tipo_carico_scarico;
 
//    DS_TIPO_CARICO_SCARICO VARCHAR(100)
	private java.lang.String ds_tipo_carico_scarico;
 
//    VARIAZIONE_PIU DECIMAL(22,0)
	private java.math.BigDecimal variazione_piu;
 
//    VARIAZIONE_MENO DECIMAL(22,0)
	private java.math.BigDecimal variazione_meno;
 
//    QUANTITA DECIMAL(22,0)
	private java.lang.Long quantita;
 
//    VALORE_UNITARIO DECIMAL(20,6)
	private java.math.BigDecimal valore_unitario;
 
//    IMPONIBILE_AMMORTAMENTO DECIMAL(15,2)
	private java.math.BigDecimal imponibile_ammortamento;
 
//    IM_MOVIMENTO_AMMORT DECIMAL(22,0)
	private java.math.BigDecimal im_movimento_ammort;
	
	private java.math.BigDecimal valore_ammortizzato;
 
//    NUMERO_ANNI DECIMAL(4,0)
	private java.lang.Integer numero_anni;
 
//    PERC_AMMORTAMENTO DECIMAL(5,2)
	private java.math.BigDecimal perc_ammortamento;
 
//    PERC_PRIMO_ANNO DECIMAL(5,2)
	private java.math.BigDecimal perc_primo_anno;
 
//    PERC_SUCCESSIVI DECIMAL(5,2)
	private java.math.BigDecimal perc_successivi;
 
	// PERCENTUALE_UTILIZZO_CDR DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_utilizzo_cdr;

	// PERCENTUALE_UTILIZZO_LA DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_utilizzo_la;

	public V_cons_registro_inventarioBase() {
		super();
	}
	
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Integer getEsercizio_carico_bene () {
		return esercizio_carico_bene;
	}
	public void setEsercizio_carico_bene(java.lang.Integer esercizio_carico_bene)  {
		this.esercizio_carico_bene=esercizio_carico_bene;
	}
	public java.lang.String getEtichetta () {
		return etichetta;
	}
	public void setEtichetta(java.lang.String etichetta)  {
		this.etichetta=etichetta;
	}
	public java.lang.String getDs_bene () {
		return ds_bene;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	public java.math.BigDecimal getValore_iniziale () {
		return valore_iniziale;
	}
	public void setValore_iniziale(java.math.BigDecimal valore_iniziale)  {
		this.valore_iniziale=valore_iniziale;
	}
	public java.lang.String getCategoria () {
		return categoria;
	}
	public void setCategoria(java.lang.String categoria)  {
		this.categoria=categoria;
	}
	public java.lang.String getCd_categoria_gruppo () {
		return cd_categoria_gruppo;
	}
	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo)  {
		this.cd_categoria_gruppo=cd_categoria_gruppo;
	}
	public java.lang.String getDs_categoria_gruppo () {
		return ds_categoria_gruppo;
	}
	public void setDs_categoria_gruppo(java.lang.String ds_categoria_gruppo)  {
		this.ds_categoria_gruppo=ds_categoria_gruppo;
	}
	public java.lang.String getDs_ubicazione_bene () {
		return ds_ubicazione_bene;
	}
	public void setDs_ubicazione_bene(java.lang.String ds_ubicazione_bene)  {
		this.ds_ubicazione_bene=ds_ubicazione_bene;
	}
	public java.lang.Integer getCd_assegnatario () {
		return cd_assegnatario;
	}
	public void setCd_assegnatario(java.lang.Integer cd_assegnatario)  {
		this.cd_assegnatario=cd_assegnatario;
	}
	public java.lang.String getDenominazione_sede () {
		return denominazione_sede;
	}
	public void setDenominazione_sede(java.lang.String denominazione_sede)  {
		this.denominazione_sede=denominazione_sede;
	}
	public java.sql.Timestamp getData_registrazione () {
		return data_registrazione;
	}
	public void setData_registrazione(java.sql.Timestamp data_registrazione)  {
		this.data_registrazione=data_registrazione;
	}
	public java.lang.String getCd_tipo_carico_scarico () {
		return cd_tipo_carico_scarico;
	}
	public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico)  {
		this.cd_tipo_carico_scarico=cd_tipo_carico_scarico;
	}
	public java.lang.String getDs_tipo_carico_scarico () {
		return ds_tipo_carico_scarico;
	}
	public void setDs_tipo_carico_scarico(java.lang.String ds_tipo_carico_scarico)  {
		this.ds_tipo_carico_scarico=ds_tipo_carico_scarico;
	}
	public java.math.BigDecimal getVariazione_piu () {
		return variazione_piu;
	}
	public void setVariazione_piu(java.math.BigDecimal variazione_piu)  {
		this.variazione_piu=variazione_piu;
	}
	public java.math.BigDecimal getVariazione_meno () {
		return variazione_meno;
	}
	public void setVariazione_meno(java.math.BigDecimal variazione_meno)  {
		this.variazione_meno=variazione_meno;
	}
	public java.lang.Long getQuantita () {
		return quantita;
	}
	public void setQuantita(java.lang.Long quantita)  {
		this.quantita=quantita;
	}
	public java.math.BigDecimal getValore_unitario () {
		return valore_unitario;
	}
	public void setValore_unitario(java.math.BigDecimal valore_unitario)  {
		this.valore_unitario=valore_unitario;
	}
	public java.math.BigDecimal getImponibile_ammortamento () {
		return imponibile_ammortamento;
	}
	public void setImponibile_ammortamento(java.math.BigDecimal imponibile_ammortamento)  {
		this.imponibile_ammortamento=imponibile_ammortamento;
	}
	public java.math.BigDecimal getIm_movimento_ammort () {
		return im_movimento_ammort;
	}
	public void setIm_movimento_ammort(java.math.BigDecimal im_movimento_ammort)  {
		this.im_movimento_ammort=im_movimento_ammort;
	}
	public java.lang.Integer getNumero_anni () {
		return numero_anni;
	}
	public void setNumero_anni(java.lang.Integer numero_anni)  {
		this.numero_anni=numero_anni;
	}
	public java.math.BigDecimal getPerc_ammortamento () {
		return perc_ammortamento;
	}
	public void setPerc_ammortamento(java.math.BigDecimal perc_ammortamento)  {
		this.perc_ammortamento=perc_ammortamento;
	}
	public java.math.BigDecimal getPerc_primo_anno () {
		return perc_primo_anno;
	}
	public void setPerc_primo_anno(java.math.BigDecimal perc_primo_anno)  {
		this.perc_primo_anno=perc_primo_anno;
	}
	public java.math.BigDecimal getPerc_successivi () {
		return perc_successivi;
	}
	public void setPerc_successivi(java.math.BigDecimal perc_successivi)  {
		this.perc_successivi=perc_successivi;
	}

	public java.math.BigDecimal getPercentuale_utilizzo_cdr() {
		return percentuale_utilizzo_cdr;
	}

	public java.math.BigDecimal getPercentuale_utilizzo_la() {
		return percentuale_utilizzo_la;
	}

	public void setPercentuale_utilizzo_cdr(java.math.BigDecimal decimal) {
		percentuale_utilizzo_cdr = decimal;
	}

	public void setPercentuale_utilizzo_la(java.math.BigDecimal decimal) {
		percentuale_utilizzo_la = decimal;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public java.lang.String getCd_utilizzatore_cdr() {
		return cd_utilizzatore_cdr;
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public java.lang.Long getNr_inventario() {
		return nr_inventario;
	}

	public java.lang.Long getPg_buono_c_s() {
		return pg_buono_c_s;
	}

	public java.lang.Long getPg_inventario() {
		return pg_inventario;
	}

	public java.lang.Integer getProgressivo() {
		return progressivo;
	}

	public java.lang.String getTi_documento() {
		return ti_documento;
	}

	public void setCd_linea_attivita(java.lang.String string) {
		cd_linea_attivita = string;
	}

	public void setCd_utilizzatore_cdr(java.lang.String string) {
		cd_utilizzatore_cdr = string;
	}
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	public void setNr_inventario(java.lang.Long long1) {
		nr_inventario = long1;
	}

	public void setPg_buono_c_s(java.lang.Long long1) {
		pg_buono_c_s = long1;
	}

	public void setPg_inventario(java.lang.Long long1) {
		pg_inventario = long1;
	}

	public void setProgressivo(java.lang.Integer integer) {
		progressivo = integer;
	}

	public void setTi_documento(java.lang.String string) {
		ti_documento = string;
	}
	public java.lang.Integer getEsercizio_amm() {
		return esercizio_amm;
	}
	public void setEsercizio_amm(java.lang.Integer integer) {
		esercizio_amm = integer;
	}
	public java.math.BigDecimal getValore_ammortizzato() {
		return valore_ammortizzato;
	}
	public void setValore_ammortizzato(java.math.BigDecimal decimal) {
		valore_ammortizzato = decimal;
	}

}