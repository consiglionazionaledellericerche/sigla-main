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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.contab.pagopa.model.Riscossione;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagamentoPagopaBase extends PagamentoPagopaKey implements Keyed {
	private static final long serialVersionUID = 1L;

	private Long idPendenzaPagopa;

	private Timestamp dtPagamento;

	private BigDecimal importo;

	private String iur;

	private String causale;

	private String rpp;

	private String ccp;

	private Riscossione.StatoEnum stato = null;

	public PagamentoPagopaBase() {
		super();
	}

	public PagamentoPagopaBase(Long id) {
		super(id);
	}

	public Long getIdPendenzaPagopa() {
		return idPendenzaPagopa;
	}

	public void setIdPendenzaPagopa(Long idPendenzaPagopa) {
		this.idPendenzaPagopa = idPendenzaPagopa;
	}

	public Timestamp getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(Timestamp dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getRpp() {
		return rpp;
	}

	public void setRpp(String rpp) {
		this.rpp = rpp;
	}

	public Riscossione.StatoEnum getStato() {
		return stato;
	}

	public void setStato(Riscossione.StatoEnum stato) {
		this.stato = stato;
	}

	public String getCcp() {
		return ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
}
