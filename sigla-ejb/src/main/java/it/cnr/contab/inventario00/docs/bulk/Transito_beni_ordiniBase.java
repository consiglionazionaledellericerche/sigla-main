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

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transito_beni_ordiniBase extends Transito_beni_ordiniKey implements Keyed {
	// CD_ASSEGNATARIO DECIMAL(8,0)
	private Integer cd_assegnatario;

	private Long pg_inventario;

	public Long getPg_inventario() {
		return pg_inventario;
	}

	public void setPg_inventario(Long pg_inventario) {
		this.pg_inventario = pg_inventario;
	}

	// CD_CDS VARCHAR(30) NOT NULL
	private String cd_cds;

	// CD_CONDIZIONE_BENE VARCHAR(10) NOT NULL
	private String cd_condizione_bene;

	// CD_UBICAZIONE VARCHAR(30) NOT NULL
	private String cd_ubicazione;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private String cd_unita_organizzativa;

	// COLLOCAZIONE VARCHAR(50)
	private String collocazione;

	// DS_BENE VARCHAR(100) NOT NULL
	private String ds_bene;

	private java.sql.Timestamp dt_acquisizione;

	// ESERCIZIO_CARICO_BENE DECIMAL(4,0) NOT NULL
	private Integer esercizio_carico_bene;

	private Integer cd_barre;

	private Long id_movimenti_mag;

	// ETICHETTA VARCHAR(50) NOT NULL
	private String etichetta;

	// FL_AMMORTAMENTO CHAR(1)
	private Boolean fl_ammortamento;

	// TI_AMMORTAMENTO CHAR(1)
	private String ti_ammortamento;

	// TI_COMMERCIALE_ISTITUZIONALE CHAR(1) NOT NULL
	private String ti_commerciale_istituzionale;

	// VALORE_INIZIALE DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal valore_iniziale;

	private String targa;

	private String seriale;

public Transito_beni_ordiniBase() {
	super();
}
public Transito_beni_ordiniBase(Long id) {
	super(id);
}

	public Integer getCd_assegnatario() {
		return cd_assegnatario;
	}

	public void setCd_assegnatario(Integer cd_assegnatario) {
		this.cd_assegnatario = cd_assegnatario;
	}

	public String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}

	public String getCd_condizione_bene() {
		return cd_condizione_bene;
	}

	public void setCd_condizione_bene(String cd_condizione_bene) {
		this.cd_condizione_bene = cd_condizione_bene;
	}

	public String getCd_ubicazione() {
		return cd_ubicazione;
	}

	public void setCd_ubicazione(String cd_ubicazione) {
		this.cd_ubicazione = cd_ubicazione;
	}

	public String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getDs_bene() {
		return ds_bene;
	}

	public void setDs_bene(String ds_bene) {
		this.ds_bene = ds_bene;
	}

	public Timestamp getDt_acquisizione() {
		return dt_acquisizione;
	}

	public void setDt_acquisizione(Timestamp dt_acquisizione) {
		this.dt_acquisizione = dt_acquisizione;
	}

	public Integer getEsercizio_carico_bene() {
		return esercizio_carico_bene;
	}

	public void setEsercizio_carico_bene(Integer esercizio_carico_bene) {
		this.esercizio_carico_bene = esercizio_carico_bene;
	}

	public Integer getCd_barre() {
		return cd_barre;
	}

	public void setCd_barre(Integer cd_barre) {
		this.cd_barre = cd_barre;
	}

	public Long getId_movimenti_mag() {
		return id_movimenti_mag;
	}

	public void setId_movimenti_mag(Long id_movimenti_mag) {
		this.id_movimenti_mag = id_movimenti_mag;
	}

	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	public Boolean getFl_ammortamento() {
		return fl_ammortamento;
	}

	public void setFl_ammortamento(Boolean fl_ammortamento) {
		this.fl_ammortamento = fl_ammortamento;
	}

	public String getTi_ammortamento() {
		return ti_ammortamento;
	}

	public void setTi_ammortamento(String ti_ammortamento) {
		this.ti_ammortamento = ti_ammortamento;
	}

	public String getTi_commerciale_istituzionale() {
		return ti_commerciale_istituzionale;
	}

	public void setTi_commerciale_istituzionale(String ti_commerciale_istituzionale) {
		this.ti_commerciale_istituzionale = ti_commerciale_istituzionale;
	}

	public BigDecimal getValore_iniziale() {
		return valore_iniziale;
	}

	public void setValore_iniziale(BigDecimal valore_iniziale) {
		this.valore_iniziale = valore_iniziale;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getSeriale() {
		return seriale;
	}

	public void setSeriale(String seriale) {
		this.seriale = seriale;
	}
}
