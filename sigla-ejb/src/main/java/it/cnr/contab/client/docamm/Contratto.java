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

public class Contratto {

	public Contratto() {
		// TODO Auto-generated constructor stub
	}
private Integer esercizio;
private String stato;
private Long pg_contratto;
private Integer codiceterzo;
private String descrizione;
private String natura;
private java.math.BigDecimal im_contratto_attivo;
private java.math.BigDecimal im_contratto_passivo;
public Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
public String getStato() {
	return stato;
}
public void setStato(String stato) {
	this.stato = stato;
}
public Long getPg_contratto() {
	return pg_contratto;
}
public void setPg_contratto(Long pg_contratto) {
	this.pg_contratto = pg_contratto;
}
public Integer getCodiceterzo() {
	return codiceterzo;
}
public void setCodiceterzo(Integer codiceterzo) {
	this.codiceterzo = codiceterzo;
}
public String getDescrizione() {
	return descrizione;
}
public void setDescrizione(String descrizione) {
	this.descrizione = descrizione;
}
public String getNatura() {
	return natura;
}
public void setNatura(String natura) {
	this.natura = natura;
}
public java.math.BigDecimal getIm_contratto_attivo() {
	return im_contratto_attivo;
}
public void setIm_contratto_attivo(java.math.BigDecimal im_contratto_attivo) {
	this.im_contratto_attivo = im_contratto_attivo;
}
public java.math.BigDecimal getIm_contratto_passivo() {
	return im_contratto_passivo;
}
public void setIm_contratto_passivo(java.math.BigDecimal im_contratto_passivo) {
	this.im_contratto_passivo = im_contratto_passivo;
}
}
