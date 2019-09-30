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

import java.util.Date;
public class RigaperNC {

	public RigaperNC() {
		// TODO Auto-generated constructor stub
	}
	private Integer esercizio;
	private String cds;
	private String uo;
	private Long pg_fattura;
	private Long progressivoriga;
	private String descrizione;
	private Date dataregistrazione;
	private String cognome;
	private String nome;
	private String ragionesociale;
	private java.math.BigDecimal imp_disponibile_nc; 
	private String voce_iva;
	private String causale;
	private String tariffario;
	private String tiposezionale;
	private Boolean fl_intra;
	private Boolean fl_extra;
	private Boolean fl_san_marino;
	private Boolean fl_liquidazione_differita;
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}
	public String getCds() {
		return cds;
	}
	public void setCds(String cds) {
		this.cds = cds;
	}
	public String getUo() {
		return uo;
	}
	public void setUo(String uo) {
		this.uo = uo;
	}
	public Long getPg_fattura() {
		return pg_fattura;
	}
	public void setPg_fattura(Long pg_fattura) {
		this.pg_fattura = pg_fattura;
	}
	public Long getProgressivoriga() {
		return progressivoriga;
	}
	public void setProgressivoriga(Long progressivoriga) {
		this.progressivoriga = progressivoriga;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Date getDataregistrazione() {
		return dataregistrazione;
	}
	public void setDataregistrazione(Date dataregistrazione) {
		this.dataregistrazione = dataregistrazione;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRagionesociale() {
		return ragionesociale;
	}
	public void setRagionesociale(String ragionesociale) {
		this.ragionesociale = ragionesociale;
	}
	public java.math.BigDecimal getImp_disponibile_nc() {
		return imp_disponibile_nc;
	}
	public void setImp_disponibile_nc(java.math.BigDecimal imp_disponibile_nc) {
		this.imp_disponibile_nc = imp_disponibile_nc;
	}
	public String getVoce_iva() {
		return voce_iva;
	}
	public void setVoce_iva(String voce_iva) {
		this.voce_iva = voce_iva;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getTariffario() {
		return tariffario;
	}
	public void setTariffario(String tariffario) {
		this.tariffario = tariffario;
	}
	public String getTiposezionale() {
		return tiposezionale;
	}
	public void setTiposezionale(String tiposezionale) {
		this.tiposezionale = tiposezionale;
	}
	public Boolean getFl_intra() {
		return fl_intra;
	}
	public void setFl_intra(Boolean fl_intra) {
		this.fl_intra = fl_intra;
	}
	public Boolean getFl_extra() {
		return fl_extra;
	}
	public void setFl_extra(Boolean fl_extra) {
		this.fl_extra = fl_extra;
	}
	public Boolean getFl_san_marino() {
		return fl_san_marino;
	}
	public void setFl_san_marino(Boolean fl_san_marino) {
		this.fl_san_marino = fl_san_marino;
	}
	public Boolean getFl_liquidazione_differita() {
		return fl_liquidazione_differita;
	}
	public void setFl_liquidazione_differita(Boolean fl_liquidazione_differita) {
		this.fl_liquidazione_differita = fl_liquidazione_differita;
	}
	
}
