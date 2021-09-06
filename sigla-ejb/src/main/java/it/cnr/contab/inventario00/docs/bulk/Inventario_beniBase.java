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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_beniBase extends Inventario_beniKey implements Keyed {
	// CD_ASSEGNATARIO DECIMAL(8,0)
	private java.lang.Integer cd_assegnatario;

	// CD_CATEGORIA_GRUPPO VARCHAR(10) NOT NULL
	private java.lang.String cd_categoria_gruppo;

	// CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;

	// CD_CONDIZIONE_BENE VARCHAR(10) NOT NULL
	private java.lang.String cd_condizione_bene;

	// CD_UBICAZIONE VARCHAR(30) NOT NULL
	private java.lang.String cd_ubicazione;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// COLLOCAZIONE VARCHAR(50)
	private java.lang.String collocazione;

	// DS_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_bene;

	// DT_VALIDITA_VARIAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_validita_variazione;
	
	private java.sql.Timestamp dt_acquisizione;

	// ESERCIZIO_CARICO_BENE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_carico_bene;

	private java.lang.Integer cd_barre;

	// ETICHETTA VARCHAR(50) NOT NULL
	private java.lang.String etichetta;

	// FL_AMMORTAMENTO CHAR(1)
	private java.lang.Boolean fl_ammortamento;

	// FL_MIGRATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_migrato;

	// FL_TOTALMENTE_SCARICATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_totalmente_scaricato;

	// ID_BENE_ORIGINE VARCHAR(30)
	private java.lang.String id_bene_origine;

	// IMPONIBILE_AMMORTAMENTO DECIMAL(20,6)
	private java.math.BigDecimal imponibile_ammortamento;

	// TI_AMMORTAMENTO CHAR(1)
	private java.lang.String ti_ammortamento;

	// TI_COMMERCIALE_ISTITUZIONALE CHAR(1) NOT NULL
	private java.lang.String ti_commerciale_istituzionale;

	// VALORE_ALIENAZIONE DECIMAL(20,6)
	private java.math.BigDecimal valore_alienazione;

	// VALORE_AMMORTIZZATO DECIMAL(20,6)
	private java.math.BigDecimal valore_ammortizzato;

	// VALORE_INIZIALE DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal valore_iniziale;

	public Long getId_transito_beni_ordini() {
		return id_transito_beni_ordini;
	}

	public void setId_transito_beni_ordini(Long id_transito_beni_ordini) {
		this.id_transito_beni_ordini = id_transito_beni_ordini;
	}

	// VARIAZIONE_MENO DECIMAL(20,6)
	private java.math.BigDecimal variazione_meno;

	// VARIAZIONE_PIU DECIMAL(20,6)
	private java.math.BigDecimal variazione_piu;
	
	private java.lang.String targa;
	
	private java.lang.String seriale;

	private java.lang.Long id_transito_beni_ordini;

	public Inventario_beniBase() {
	super();
}
public Inventario_beniBase(java.lang.Long nr_inventario,java.lang.Long pg_inventario,java.lang.Long progressivo) {
	super(nr_inventario,pg_inventario,progressivo);
}
/* 
 * Getter dell'attributo cd_assegnatario
 */
public java.lang.Integer getCd_assegnatario() {
	return cd_assegnatario;
}
/* 
 * Getter dell'attributo cd_categoria_gruppo
 */
public java.lang.String getCd_categoria_gruppo() {
	return cd_categoria_gruppo;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_condizione_bene
 */
public java.lang.String getCd_condizione_bene() {
	return cd_condizione_bene;
}
/* 
 * Getter dell'attributo cd_ubicazione
 */
public java.lang.String getCd_ubicazione() {
	return cd_ubicazione;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo collocazione
 */
public java.lang.String getCollocazione() {
	return collocazione;
}
/* 
 * Getter dell'attributo ds_bene
 */
public java.lang.String getDs_bene() {
	return ds_bene;
}
/* 
 * Getter dell'attributo dt_validita_variazione
 */
public java.sql.Timestamp getDt_validita_variazione() {
	return dt_validita_variazione;
}
/* 
 * Getter dell'attributo esercizio_carico_bene
 */
public java.lang.Integer getEsercizio_carico_bene() {
	return esercizio_carico_bene;
}
/* 
 * Getter dell'attributo etichetta
 */
public java.lang.String getEtichetta() {
	return etichetta;
}
/* 
 * Getter dell'attributo fl_ammortamento
 */
public java.lang.Boolean getFl_ammortamento() {
	return fl_ammortamento;
}
/* 
 * Getter dell'attributo fl_migrato
 */
public java.lang.Boolean getFl_migrato() {
	return fl_migrato;
}
/* 
 * Getter dell'attributo fl_totalmente_scaricato
 */
public java.lang.Boolean getFl_totalmente_scaricato() {
	return fl_totalmente_scaricato;
}
/* 
 * Getter dell'attributo id_bene_origine
 */
public java.lang.String getId_bene_origine() {
	return id_bene_origine;
}
/* 
 * Getter dell'attributo imponibile_ammortamento
 */
public java.math.BigDecimal getImponibile_ammortamento() {
	return imponibile_ammortamento;
}
/* 
 * Getter dell'attributo ti_ammortamento
 */
public java.lang.String getTi_ammortamento() {
	return ti_ammortamento;
}
/* 
 * Getter dell'attributo ti_commerciale_istituzionale
 */
public java.lang.String getTi_commerciale_istituzionale() {
	return ti_commerciale_istituzionale;
}
/* 
 * Getter dell'attributo valore_alienazione
 */
public java.math.BigDecimal getValore_alienazione() {
	return valore_alienazione;
}
/* 
 * Getter dell'attributo valore_ammortizzato
 */
public java.math.BigDecimal getValore_ammortizzato() {
	return valore_ammortizzato;
}
/* 
 * Getter dell'attributo valore_iniziale
 */
public java.math.BigDecimal getValore_iniziale() {
	return valore_iniziale;
}
/* 
 * Getter dell'attributo variazione_meno
 */
public java.math.BigDecimal getVariazione_meno() {
	return variazione_meno;
}
/* 
 * Getter dell'attributo variazione_piu
 */
public java.math.BigDecimal getVariazione_piu() {
	return variazione_piu;
}
/* 
 * Setter dell'attributo cd_assegnatario
 */
public void setCd_assegnatario(java.lang.Integer cd_assegnatario) {
	this.cd_assegnatario = cd_assegnatario;
}
/* 
 * Setter dell'attributo cd_categoria_gruppo
 */
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_condizione_bene
 */
public void setCd_condizione_bene(java.lang.String cd_condizione_bene) {
	this.cd_condizione_bene = cd_condizione_bene;
}
/* 
 * Setter dell'attributo cd_ubicazione
 */
public void setCd_ubicazione(java.lang.String cd_ubicazione) {
	this.cd_ubicazione = cd_ubicazione;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo collocazione
 */
public void setCollocazione(java.lang.String collocazione) {
	this.collocazione = collocazione;
}
/* 
 * Setter dell'attributo ds_bene
 */
public void setDs_bene(java.lang.String ds_bene) {
	this.ds_bene = ds_bene;
}
/* 
 * Setter dell'attributo dt_validita_variazione
 */
public void setDt_validita_variazione(java.sql.Timestamp dt_validita_variazione) {
	this.dt_validita_variazione = dt_validita_variazione;
}
/* 
 * Setter dell'attributo esercizio_carico_bene
 */
public void setEsercizio_carico_bene(java.lang.Integer esercizio_carico_bene) {
	this.esercizio_carico_bene = esercizio_carico_bene;
}
/* 
 * Setter dell'attributo etichetta
 */
public void setEtichetta(java.lang.String etichetta) {
	this.etichetta = etichetta;
}
/* 
 * Setter dell'attributo fl_ammortamento
 */
public void setFl_ammortamento(java.lang.Boolean fl_ammortamento) {
	this.fl_ammortamento = fl_ammortamento;
}
/* 
 * Setter dell'attributo fl_migrato
 */
public void setFl_migrato(java.lang.Boolean fl_migrato) {
	this.fl_migrato = fl_migrato;
}
/* 
 * Setter dell'attributo fl_totalmente_scaricato
 */
public void setFl_totalmente_scaricato(java.lang.Boolean fl_totalmente_scaricato) {
	this.fl_totalmente_scaricato = fl_totalmente_scaricato;
}
/* 
 * Setter dell'attributo id_bene_origine
 */
public void setId_bene_origine(java.lang.String id_bene_origine) {
	this.id_bene_origine = id_bene_origine;
}
/* 
 * Setter dell'attributo imponibile_ammortamento
 */
public void setImponibile_ammortamento(java.math.BigDecimal imponibile_ammortamento) {
	this.imponibile_ammortamento = imponibile_ammortamento;
}
/* 
 * Setter dell'attributo ti_ammortamento
 */
public void setTi_ammortamento(java.lang.String ti_ammortamento) {
	this.ti_ammortamento = ti_ammortamento;
}
/* 
 * Setter dell'attributo ti_commerciale_istituzionale
 */
public void setTi_commerciale_istituzionale(java.lang.String ti_commerciale_istituzionale) {
	this.ti_commerciale_istituzionale = ti_commerciale_istituzionale;
}
/* 
 * Setter dell'attributo valore_alienazione
 */
public void setValore_alienazione(java.math.BigDecimal valore_alienazione) {
	this.valore_alienazione = valore_alienazione;
}
/* 
 * Setter dell'attributo valore_ammortizzato
 */
public void setValore_ammortizzato(java.math.BigDecimal valore_ammortizzato) {
	this.valore_ammortizzato = valore_ammortizzato;
}
/* 
 * Setter dell'attributo valore_iniziale
 */
public void setValore_iniziale(java.math.BigDecimal valore_iniziale) {
	this.valore_iniziale = valore_iniziale;
}
/* 
 * Setter dell'attributo variazione_meno
 */
public void setVariazione_meno(java.math.BigDecimal variazione_meno) {
	this.variazione_meno = variazione_meno;
}
/* 
 * Setter dell'attributo variazione_piu
 */
public void setVariazione_piu(java.math.BigDecimal variazione_piu) {
	this.variazione_piu = variazione_piu;
}
public java.sql.Timestamp getDt_acquisizione() {
	return dt_acquisizione;
}
public void setDt_acquisizione(java.sql.Timestamp dt_acquisizione) {
	this.dt_acquisizione = dt_acquisizione;
}
public java.lang.Integer getCd_barre() {
	return cd_barre;
}
public void setCd_barre(java.lang.Integer cd_barre) {
	this.cd_barre = cd_barre;
}
public java.lang.String getTarga() {
	return targa;
}
public void setTarga(java.lang.String targa) {
	this.targa = targa;
}
public java.lang.String getSeriale() {
	return seriale;
}
public void setSeriale(java.lang.String seriale) {
	this.seriale = seriale;
}
}
