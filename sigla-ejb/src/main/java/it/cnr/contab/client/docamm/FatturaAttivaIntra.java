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

package it.cnr.contab.client.docamm;

import javax.xml.bind.annotation.XmlElement;

public class FatturaAttivaIntra {
 private java.math.BigDecimal ammontare_euro;
 private String cod_incasso;
 private String cod_erogazione;
 private Long id_cpa;
 private Long pg_nazione;
 // variabili relative ai beni
 private Long id_nomenclatura_combinata;
 private Long id_natura_transazione;
 private String cd_provincia;
 private String cd_consegna;
 private String cd_trasporto;
 private java.math.BigDecimal Massa;
 private java.math.BigDecimal valore_statistico;
 private Long unita_supplementari;
 //variabili beni non obbligatorie
 
	public FatturaAttivaIntra() {
		super();
	}

public java.math.BigDecimal getAmmontare_euro() {
	return ammontare_euro;
}
@XmlElement(required=true)
public void setAmmontare_euro(java.math.BigDecimal ammontare_euro) {
	this.ammontare_euro = ammontare_euro;
}
public String getCod_incasso() {
	return cod_incasso;
}

public void setCod_incasso(String cod_incasso) {
	this.cod_incasso = cod_incasso;
}
public String getCod_erogazione() {
	return cod_erogazione;
}

public void setCod_erogazione(String cod_erogazione) {
	this.cod_erogazione = cod_erogazione;
}
public Long getId_cpa() {
	return id_cpa;
}

public void setId_cpa(Long id_cpa) {
	this.id_cpa = id_cpa;
}

public Long getPg_nazione() {
	return pg_nazione;
}
@XmlElement(required=true)
public void setPg_nazione(Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}

public Long getId_nomenclatura_combinata() {
	return id_nomenclatura_combinata;
}

public void setId_nomenclatura_combinata(Long id_nomenclatura_combinata) {
	this.id_nomenclatura_combinata = id_nomenclatura_combinata;
}

public Long getId_natura_transazione() {
	return id_natura_transazione;
}

public void setId_natura_transazione(Long id_natura_transazione) {
	this.id_natura_transazione = id_natura_transazione;
}

public String getCd_provincia() {
	return cd_provincia;
}

public void setCd_provincia(String cd_provincia) {
	this.cd_provincia = cd_provincia;
}

public String getCd_consegna() {
	return cd_consegna;
}

public void setCd_consegna(String cd_consegna) {
	this.cd_consegna = cd_consegna;
}

public String getCd_trasporto() {
	return cd_trasporto;
}

public void setCd_trasporto(String cd_trasporto) {
	this.cd_trasporto = cd_trasporto;
}

public java.math.BigDecimal getMassa() {
	return Massa;
}

public void setMassa(java.math.BigDecimal massa) {
	Massa = massa;
}

public java.math.BigDecimal getValore_statistico() {
	return valore_statistico;
}

public void setValore_statistico(java.math.BigDecimal valore_statistico) {
	this.valore_statistico = valore_statistico;
}

public Long getUnita_supplementari() {
	return unita_supplementari;
}

public void setUnita_supplementari(Long unita_supplementari) {
	this.unita_supplementari = unita_supplementari;
}

}
