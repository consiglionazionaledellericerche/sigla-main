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

package it.cnr.contab.pagopa.bulk;

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PendenzaPagopaBase extends PendenzaPagopaKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO DECIMAL(4,0) NOT NULL 
	private Integer esercizio;
	private Integer esercizioVoce;
	private java.lang.String cd_elemento_voce;

	public Integer getEsercizioVoce() {
		return esercizioVoce;
	}

	public void setEsercizioVoce(Integer esercizioVoce) {
		this.esercizioVoce = esercizioVoce;
	}

	public String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}

	public String getTi_appartenenza() {
		return ti_appartenenza;
	}

	public void setTi_appartenenza(String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}

	public String getTi_gestione() {
		return ti_gestione;
	}

	public void setTi_gestione(String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private String cdUnitaOrganizzativa;
	
	private String tipoPosizione;

	private Integer idTipoPendenzaPagopa;

	public String getCdIuv() {
		return cdIuv;
	}

	public void setCdIuv(String cdIuv) {
		this.cdIuv = cdIuv;
	}

	public String getCdTipoDocAmm() {
		return cdTipoDocAmm;
	}

	public void setCdTipoDocAmm(String cdTipoDocAmm) {
		this.cdTipoDocAmm = cdTipoDocAmm;
	}

	// CD_PROVV VARCHAR(20)
	private String cdAvviso;
	private String cdIuv;
	private String cdCdsDocAmm;
	private String cdUoDocAmm;
	private String cdTipoDocAmm;
	private BigDecimal importoPendenza;
	private Integer esercizioDocAmm;
	private Long pgDocAmm;

	// NUMERO_PROVV DECIMAL(10,0)
	private String stato;

	private java.sql.Timestamp dtScadenza;

	private String note;

	private java.lang.Integer cd_terzo;

	public String getDescrizione() {
		return descrizione;
	}

	public Integer getCd_terzo() {
		return cd_terzo;
	}

	public void setCd_terzo(Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	private String descrizione;
	public PendenzaPagopaBase() {
		super();
	}

	public PendenzaPagopaBase(Long id) {
		super(id);
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public String getCdCdsDocAmm() {
		return cdCdsDocAmm;
	}

	public void setCdCdsDocAmm(String cdCdsDocAmm) {
		this.cdCdsDocAmm = cdCdsDocAmm;
	}

	public String getCdUoDocAmm() {
		return cdUoDocAmm;
	}

	public void setCdUoDocAmm(String cdUoDocAmm) {
		this.cdUoDocAmm = cdUoDocAmm;
	}

	public BigDecimal getImportoPendenza() {
		return importoPendenza;
	}

	public void setImportoPendenza(BigDecimal importoPendenza) {
		this.importoPendenza = importoPendenza;
	}

	public Integer getEsercizioDocAmm() {
		return esercizioDocAmm;
	}

	public void setEsercizioDocAmm(Integer esercizioDocAmm) {
		this.esercizioDocAmm = esercizioDocAmm;
	}

	public Long getPgDocAmm() {
		return pgDocAmm;
	}

	public void setPgDocAmm(Long pgDocAmm) {
		this.pgDocAmm = pgDocAmm;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public String getTipoPosizione() {
		return tipoPosizione;
	}

	public void setTipoPosizione(String tipoPosizione) {
		this.tipoPosizione = tipoPosizione;
	}

	public Integer getIdTipoPendenzaPagopa() {
		return idTipoPendenzaPagopa;
	}

	public void setIdTipoPendenzaPagopa(Integer idTipoPendenzaPagopa) {
		this.idTipoPendenzaPagopa = idTipoPendenzaPagopa;
	}

	public String getCdAvviso() {
		return cdAvviso;
	}

	public void setCdAvviso(String cdAvviso) {
		this.cdAvviso = cdAvviso;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Timestamp getDtScadenza() {
		return dtScadenza;
	}

	public void setDtScadenza(Timestamp dtScadenza) {
		this.dtScadenza = dtScadenza;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
