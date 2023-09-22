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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VDocammElettroniciAttiviBase extends VDocammElettroniciAttiviKey implements Keyed {
	private String cdCdsOrigine;

	private String cdUoOrigine;

	private java.sql.Timestamp dtRegistrazione;

	private String dsDocamm;

	private Integer cdTerzo;

	private String cognome;

	private String nome;

	private String ragioneSociale;

	private String codice_fiscale;

	private String partita_iva;

	private String protocollo_iva;

	private java.sql.Timestamp dtEmissione;

	private java.math.BigDecimal im_totale_docamm;

	private java.lang.String codiceUnivocoUfficioIpa;

	private java.lang.String statoInvioSdi;

	private java.lang.String ti_fattura;

	public VDocammElettroniciAttiviBase() {
		super();
	}

	public VDocammElettroniciAttiviBase(String tipoDocamm, String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_docamm) {
		super(tipoDocamm,cd_cds,cd_unita_organizzativa,esercizio,pg_docamm);
	}

	public String getCdCdsOrigine() {
		return cdCdsOrigine;
	}

	public void setCdCdsOrigine(String cdCdsOrigine) {
		this.cdCdsOrigine = cdCdsOrigine;
	}

	public String getCdUoOrigine() {
		return cdUoOrigine;
	}

	public void setCdUoOrigine(String cdUoOrigine) {
		this.cdUoOrigine = cdUoOrigine;
	}

	public Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}

	public void setDtRegistrazione(Timestamp dtRegistrazione) {
		this.dtRegistrazione = dtRegistrazione;
	}

	public String getDsDocamm() {
		return dsDocamm;
	}

	public void setDsDocamm(String dsDocamm) {
		this.dsDocamm = dsDocamm;
	}

	public Integer getCdTerzo() {
		return cdTerzo;
	}

	public void setCdTerzo(Integer cdTerzo) {
		this.cdTerzo = cdTerzo;
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

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodice_fiscale() {
		return codice_fiscale;
	}

	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}

	public String getPartita_iva() {
		return partita_iva;
	}

	public void setPartita_iva(String partita_iva) {
		this.partita_iva = partita_iva;
	}

	public String getProtocollo_iva() {
		return protocollo_iva;
	}

	public void setProtocollo_iva(String protocollo_iva) {
		this.protocollo_iva = protocollo_iva;
	}

	public Timestamp getDtEmissione() {
		return dtEmissione;
	}

	public void setDtEmissione(Timestamp dtEmissione) {
		this.dtEmissione = dtEmissione;
	}

	public BigDecimal getIm_totale_docamm() {
		return im_totale_docamm;
	}

	public void setIm_totale_docamm(BigDecimal im_totale_docamm) {
		this.im_totale_docamm = im_totale_docamm;
	}

	public String getCodiceUnivocoUfficioIpa() {
		return codiceUnivocoUfficioIpa;
	}

	public void setCodiceUnivocoUfficioIpa(String codiceUnivocoUfficioIpa) {
		this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
	}

	public String getStatoInvioSdi() {
		return statoInvioSdi;
	}

	public void setStatoInvioSdi(String statoInvioSdi) {
		this.statoInvioSdi = statoInvioSdi;
	}

	public String getTi_fattura() {
		return ti_fattura;
	}

	public void setTi_fattura(String ti_fattura) {
		this.ti_fattura = ti_fattura;
	}
}