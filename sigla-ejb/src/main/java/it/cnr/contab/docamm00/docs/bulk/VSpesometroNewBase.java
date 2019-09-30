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

package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class VSpesometroNewBase extends VSpesometroNewKey implements Keyed {

private java.sql.Timestamp data;
private java.sql.Timestamp da_data;
private java.sql.Timestamp a_data;
private java.lang.String tipo;
private java.lang.String ti_fattura;
private java.lang.Integer esercizio;
private java.lang.String partita_iva;
private java.lang.String codice_fiscale;
private java.lang.String uo;
private java.lang.Long prog_fattura;
private java.lang.String natura ;
private java.lang.String cap ;
private java.math.BigDecimal percentuale;
private java.lang.String fl_detraibile;
private java.math.BigDecimal perc_detra	;
private java.math.BigDecimal imponibile_fa;	
private java.math.BigDecimal iva_fa;
private java.lang.String cognome;
private java.lang.String nome;
private java.lang.String provincia;
private java.lang.String cod_naz;
private java.lang.String stato_residenza;
private java.lang.String ragione_sociale;
private java.lang.String indirizzo_sede;
private java.lang.String numero_civico_sede;
private java.lang.String comune_sede;
private java.sql.Timestamp dt_fattura_fornitore;
private java.lang.String nr_fattura_fornitore;
private java.lang.String split; 
private java.lang.Long terzo_uo;

	public VSpesometroNewBase() {
		super();
	}
	public VSpesometroNewBase(java.lang.Long pg) {
		super(pg);
	}
	public java.sql.Timestamp getData() {
		return data;
	}
	public void setData(java.sql.Timestamp data) {
		this.data = data;
	}
	public java.sql.Timestamp getDa_data() {
		return da_data;
	}
	public void setDa_data(java.sql.Timestamp da_data) {
		this.da_data = da_data;
	}
	public java.sql.Timestamp getA_data() {
		return a_data;
	}
	public void setA_data(java.sql.Timestamp a_data) {
		this.a_data = a_data;
	}
	public java.lang.String getTipo() {
		return tipo;
	}
	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}
	public java.lang.String getTi_fattura() {
		return ti_fattura;
	}
	public void setTi_fattura(java.lang.String ti_fattura) {
		this.ti_fattura = ti_fattura;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.String getPartita_iva() {
		return partita_iva;
	}
	public void setPartita_iva(java.lang.String partita_iva) {
		this.partita_iva = partita_iva;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo) {
		this.uo = uo;
	}
	public java.lang.Long getProg_fattura() {
		return prog_fattura;
	}
	public void setProg_fattura(java.lang.Long prog_fattura) {
		this.prog_fattura = prog_fattura;
	}
	
	public java.lang.String getFl_detraibile() {
		return fl_detraibile;
	}
	public void setFl_detraibile(java.lang.String fl_detraibile) {
		this.fl_detraibile = fl_detraibile;
	}
	public java.math.BigDecimal getPerc_detra() {
		return perc_detra;
	}
	public void setPerc_detra(java.math.BigDecimal perc_detra) {
		this.perc_detra = perc_detra;
	}
	public java.math.BigDecimal getImponibile_fa() {
		return imponibile_fa;
	}
	public void setImponibile_fa(java.math.BigDecimal imponibile_fa) {
		this.imponibile_fa = imponibile_fa;
	}
	public java.math.BigDecimal getIva_fa() {
		return iva_fa;
	}
	public void setIva_fa(java.math.BigDecimal iva_fa) {
		this.iva_fa = iva_fa;
	}
	public java.lang.String getCognome() {
		return cognome;
	}
	public void setCognome(java.lang.String cognome) {
		this.cognome = cognome;
	}
	public java.lang.String getNome() {
		return nome;
	}
	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}
	public java.lang.String getProvincia() {
		return provincia;
	}
	public void setProvincia(java.lang.String provincia) {
		this.provincia = provincia;
	}
	public java.lang.String getCod_naz() {
		return cod_naz;
	}
	public void setCod_naz(java.lang.String cod_naz) {
		this.cod_naz = cod_naz;
	}
	public java.lang.String getStato_residenza() {
		return stato_residenza;
	}
	public void setStato_residenza(java.lang.String stato_residenza) {
		this.stato_residenza = stato_residenza;
	}
	public java.lang.String getRagione_sociale() {
		return ragione_sociale;
	}
	public void setRagione_sociale(java.lang.String ragione_sociale) {
		this.ragione_sociale = ragione_sociale;
	}
	public java.lang.String getIndirizzo_sede() {
		return indirizzo_sede;
	}
	public void setIndirizzo_sede(java.lang.String indirizzo_sede) {
		this.indirizzo_sede = indirizzo_sede;
	}
	public java.lang.String getNumero_civico_sede() {
		return numero_civico_sede;
	}
	public void setNumero_civico_sede(java.lang.String numero_civico_sede) {
		this.numero_civico_sede = numero_civico_sede;
	}
	public java.lang.String getComune_sede() {
		return comune_sede;
	}
	public void setComune_sede(java.lang.String comune_sede) {
		this.comune_sede = comune_sede;
	}
	public java.sql.Timestamp getDt_fattura_fornitore() {
		return dt_fattura_fornitore;
	}
	public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore) {
		this.dt_fattura_fornitore = dt_fattura_fornitore;
	}
	public java.lang.String getNr_fattura_fornitore() {
		return nr_fattura_fornitore;
	}
	public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore) {
		this.nr_fattura_fornitore = nr_fattura_fornitore;
	}
	public java.lang.String getNatura() {
		return natura;
	}
	public void setNatura(java.lang.String natura) {
		this.natura = natura;
	}
	public java.lang.String getCap() {
		return cap;
	}
	public void setCap(java.lang.String cap) {
		this.cap = cap;
	}
	public java.math.BigDecimal getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(java.math.BigDecimal percentuale) {
		this.percentuale = percentuale;
	}
	public java.lang.String getSplit() {
		return split;
	}
	public void setSplit(java.lang.String split) {
		this.split = split;
	}
	public java.lang.Long getTerzo_uo() {
		return terzo_uo;
	}
	public void setTerzo_uo(java.lang.Long terzo_uo) {
		this.terzo_uo = terzo_uo;
	}
	
}