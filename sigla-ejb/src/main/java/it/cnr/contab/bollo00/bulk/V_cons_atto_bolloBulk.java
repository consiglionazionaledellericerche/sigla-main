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

package it.cnr.contab.bollo00.bulk;
import java.math.BigDecimal;
import java.util.Dictionary;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.OrderedHashtable;

public class V_cons_atto_bolloBulk extends OggettoBulk implements Persistent {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static final Dictionary<String, String> tiDettagliKeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String TIPO_FOGLIO = "F";
	public static final String TIPO_ESEMPLARE = "E";

	static {
		tiDettagliKeys.put(TIPO_FOGLIO,"Foglio");
		tiDettagliKeys.put(TIPO_ESEMPLARE,"Esemplare");
	}
	
	public V_cons_atto_bolloBulk() {
		super();
	}

	public static final String TIPO_CONS_TIPO_ATTO = "CONSTIP";
	public static final String TIPO_CONS_UO = "CONSUO";

	public static final java.util.Dictionary<String, String> tiTipoConsultazioneKeys = new OrderedHashtable();

	static {
		tiTipoConsultazioneKeys.put(TIPO_CONS_TIPO_ATTO,"TIPO ATTO/UO");
		tiTipoConsultazioneKeys.put(TIPO_CONS_UO,"UO/TIPO ATTO");
	}
	
	private java.lang.Integer esercizio;

	private java.lang.String descrizioneAtto;
	
	private java.lang.String cdUnitaOrganizzativa;

	private java.lang.String dsUnitaOrganizzativa;

	private java.lang.String cdTipoAtto;

	private java.lang.String dsTipoAtto;

	private java.lang.String riferimento;

	private java.lang.String tiDettagli;

	private java.lang.Integer numDettagli;

	private BigDecimal imBollo;

	private BigDecimal imTotaleBollo;

	private java.lang.String tipoConsultazione;
	
	private java.lang.Integer numGeneraleFogli;

	private java.lang.Integer numGeneraleEsemplari;

	private BigDecimal imGeneraleBollo;

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getDescrizioneAtto() {
		return descrizioneAtto;
	}
	
	public void setDescrizioneAtto(java.lang.String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
	}
	
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public java.lang.String getDsUnitaOrganizzativa() {
		return dsUnitaOrganizzativa;
	}

	public void setDsUnitaOrganizzativa(java.lang.String dsUnitaOrganizzativa) {
		this.dsUnitaOrganizzativa = dsUnitaOrganizzativa;
	}

	public java.lang.String getCdTipoAtto() {
		return cdTipoAtto;
	}
	
	public void setCdTipoAtto(java.lang.String cdTipoAtto) {
		this.cdTipoAtto = cdTipoAtto;
	}
	
	public java.lang.String getDsTipoAtto() {
		return dsTipoAtto;
	}
	
	public void setDsTipoAtto(java.lang.String dsTipoAtto) {
		this.dsTipoAtto = dsTipoAtto;
	}

	public java.lang.String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(java.lang.String riferimento) {
		this.riferimento = riferimento;
	}
	
	public java.lang.String getTiDettagli() {
		return tiDettagli;
	}
	
	public void setTiDettagli(java.lang.String tiDettagli) {
		this.tiDettagli = tiDettagli;
	}

	public java.lang.Integer getNumDettagli() {
		return numDettagli;
	}

	public void setNumDettagli(java.lang.Integer numDettagli) {
		this.numDettagli = numDettagli;
	}

	public BigDecimal getImBollo() {
		return imBollo;
	}

	public void setImBollo(BigDecimal imBollo) {
		this.imBollo = imBollo;
	}

	public BigDecimal getImTotaleBollo() {
		return imTotaleBollo;
	}

	public void setImTotaleBollo(BigDecimal imTotaleBollo) {
		this.imTotaleBollo = imTotaleBollo;
	}

	public static java.util.Dictionary<String, String> getTiTipoConsultazioneKeys() {
		return tiTipoConsultazioneKeys;
	}
	
	public java.lang.String getTipoConsultazione() {
		return tipoConsultazione;
	}
	
	public void setTipoConsultazione(java.lang.String tipoConsultazione) {
		this.tipoConsultazione = tipoConsultazione;
	}

	public static Dictionary<String, String> getTiDettagliKeysKeys() {
		return tiDettagliKeys;
	}
	
	public BigDecimal getImGeneraleBollo() {
		return imGeneraleBollo;
	}
	
	public void setImGeneraleBollo(BigDecimal imGeneraleBollo) {
		this.imGeneraleBollo = imGeneraleBollo;
	}
	
	public java.lang.Integer getNumGeneraleEsemplari() {
		return numGeneraleEsemplari;
	}
	
	public void setNumGeneraleEsemplari(java.lang.Integer numGeneraleEsemplari) {
		this.numGeneraleEsemplari = numGeneraleEsemplari;
	}
	
	public java.lang.Integer getNumGeneraleFogli() {
		return numGeneraleFogli;
	}
	
	public void setNumGeneraleFogli(java.lang.Integer numGeneraleFogli) {
		this.numGeneraleFogli = numGeneraleFogli;
	}
}