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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.jada.bulk.*;

import java.math.BigDecimal;

public class Contributo_ritenutaBulk extends Contributo_ritenutaBase {

	private java.util.Collection dettagli;
	private it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk tipoContributoRitenuta;
	public final static java.lang.String TIPO_ENTE = "E";
	public final static java.lang.String TIPO_PERCEPIENTE = "P";
	private CompensoBulk compenso;
	private it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario;
	public Contributo_ritenutaBulk() {
		super();
	}
	public Contributo_ritenutaBulk(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.String ti_ente_percipiente) {
		super(cd_cds,cd_contributo_ritenuta,cd_unita_organizzativa,esercizio,pg_compenso,ti_ente_percipiente);
		setCompenso(new it.cnr.contab.compensi00.docs.bulk.CompensoBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_compenso));
	}

	public java.math.BigDecimal getAliquotaEnte() {

		if (getTi_ente_percipiente().equals(TIPO_ENTE))
			return getAliquota();

		return new java.math.BigDecimal(0);
	}

	public java.math.BigDecimal getAliquotaPercepiente() {

		if (getTi_ente_percipiente().equals(TIPO_PERCEPIENTE))
			return getAliquota();

		return new java.math.BigDecimal(0);
	}

	public java.math.BigDecimal getAmmontareEnte() {

		if (getTi_ente_percipiente().equals(TIPO_ENTE))
			return getAmmontare();

		return new java.math.BigDecimal(0);
	}

	public java.math.BigDecimal getAmmontareEnteLordo() {

		if (getTi_ente_percipiente().equals(TIPO_ENTE))
			return getAmmontare_lordo();

		return new java.math.BigDecimal(0);
	}

	public java.math.BigDecimal getAmmontarePercepiente() {

		if (getTi_ente_percipiente().equals(TIPO_PERCEPIENTE))
			return getAmmontare();

		return new java.math.BigDecimal(0);
	}

	public java.math.BigDecimal getAmmontarePercepienteLordo() {

		if (getTi_ente_percipiente().equals(TIPO_PERCEPIENTE))
			return getAmmontare_lordo();

		return new java.math.BigDecimal(0);
	}

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { new BulkList(dettagli) };
	}

	public java.lang.String getCd_cds() {
		it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
		if (compenso == null)
			return null;
		return compenso.getCd_cds();
	}

	public java.lang.String getCd_cds_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}

	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
		if (compenso == null)
			return null;
		return compenso.getCd_unita_organizzativa();
	}

	public CompensoBulk getCompenso() {
		return compenso;
	}

	public java.util.Collection getDettagli() {
		return dettagli;
	}

	public java.lang.Integer getEsercizio() {
		it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
		if (compenso == null)
			return null;
		return compenso.getEsercizio();
	}

	public java.lang.Integer getEsercizio_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getEsercizio();
	}

	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
		return obbligazioneScadenzario;
	}

	public java.lang.Long getPg_compenso() {
		it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
		if (compenso == null)
			return null;
		return compenso.getPg_compenso();
	}

	public Integer getEsercizio_ori_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getEsercizio_originale();
	}

	public java.lang.Long getPg_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getPg_obbligazione();
	}

	public java.lang.Long getPg_obbligazione_scadenzario() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return obbligazioneScadenzario.getPg_obbligazione_scadenzario();
	}

	public it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk getTipoContributoRitenuta() {
		return tipoContributoRitenuta;
	}

	public void setCd_cds(java.lang.String cd_cds) {
		this.getCompenso().setCd_cds(cd_cds);
	}

	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getCompenso().setCd_unita_organizzativa(cd_unita_organizzativa);
	}

	public void setCompenso(CompensoBulk newCompenso) {
		compenso = newCompenso;
	}

	public void setDettagli(java.util.Collection newDettagli) {
		dettagli = newDettagli;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.getCompenso().setEsercizio(esercizio);
	}

	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setEsercizio(esercizio_obbligazione);
	}

	public void setObbligazioneScadenzario(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newObbligazioneScadenzario) {
		obbligazioneScadenzario = newObbligazioneScadenzario;
	}

	public void setPg_compenso(java.lang.Long pg_compenso) {
		this.getCompenso().setPg_compenso(pg_compenso);
	}

	public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
	}

	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setPg_obbligazione(pg_obbligazione);
	}

	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
		this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
	}

	public void setTipoContributoRitenuta(it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk newTipoContributoRitenuta) {
		tipoContributoRitenuta = newTipoContributoRitenuta;
	}

	public boolean isContributoEnte() {
		return Contributo_ritenutaBulk.TIPO_ENTE.equals(this.getTi_ente_percipiente());
	}

	public boolean isContributoPercipiente() {
		return Contributo_ritenutaBulk.TIPO_PERCEPIENTE.equals(this.getTi_ente_percipiente());
	}

	public String getSezioneCostoRicavo() {
		String mySezione = this.getCompenso().getTipoDocumentoEnum().getSezioneEconomica();
		return this.getAmmontare().compareTo(BigDecimal.ZERO)<0?Movimento_cogeBulk.getControSezione(mySezione):mySezione;
	}
}