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

import java.sql.Timestamp;

public class GestionePagopaBase extends GestionePagopaKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// ID_TIPO_ATTO_BOLLO NUMBER NOT NULL
	private Integer idTipoPendenzaPagopa;

	private Timestamp dtIniValidita;
	private Timestamp dtFinValidita;
	private Boolean posizioneDebitoria;
	private Boolean posizioneCreditoria;

	public GestionePagopaBase() {
		super();
	}

	public GestionePagopaBase(Integer id) {
		super(id);
	}

	public Integer getIdTipoPendenzaPagopa() {
		return idTipoPendenzaPagopa;
	}

	public void setIdTipoPendenzaPagopa(Integer idTipoPendenzaPagopaBulk) {
		this.idTipoPendenzaPagopa = idTipoPendenzaPagopaBulk;
	}

	public Timestamp getDtIniValidita() {
		return dtIniValidita;
	}

	public void setDtIniValidita(Timestamp dtIniValidita) {
		this.dtIniValidita = dtIniValidita;
	}

	public Timestamp getDtFinValidita() {
		return dtFinValidita;
	}

	public void setDtFinValidita(Timestamp dtFinValidita) {
		this.dtFinValidita = dtFinValidita;
	}

	public Boolean getPosizioneDebitoria() {
		return posizioneDebitoria;
	}

	public void setPosizioneDebitoria(Boolean posizioneDebitoria) {
		this.posizioneDebitoria = posizioneDebitoria;
	}

	public Boolean getPosizioneCreditoria() {
		return posizioneCreditoria;
	}

	public void setPosizioneCreditoria(Boolean posizioneCreditoria) {
		this.posizioneCreditoria = posizioneCreditoria;
	}
}
