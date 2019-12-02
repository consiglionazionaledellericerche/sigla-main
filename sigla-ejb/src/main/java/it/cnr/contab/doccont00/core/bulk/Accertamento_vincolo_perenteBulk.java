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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Accertamento_vincolo_perenteBulk extends Accertamento_vincolo_perenteBase {
	private static final long serialVersionUID = 1L;

	private Var_stanz_resBulk variazioneResidua;
    private it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento;

	public Accertamento_vincolo_perenteBulk() {
		super();
	}

	public Accertamento_vincolo_perenteBulk(java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String cd_cds_accertamento,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento) {
		super(esercizio, pg_variazione, cd_cds_accertamento, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento);
		setVariazioneResidua(new Var_stanz_resBulk(esercizio, pg_variazione));
	}
	
	public Var_stanz_resBulk getVariazioneResidua() {
		return variazioneResidua;
	}
	
	public void setVariazioneResidua(Var_stanz_resBulk variazioneResidua) {
		this.variazioneResidua = variazioneResidua;
	}
	
	@Override
	public Integer getEsercizio() {
		Var_stanz_resBulk variazioneResidua = this.getVariazioneResidua();
		if (variazioneResidua == null)
			return null;
		return variazioneResidua.getEsercizio();
	}
	
	@Override
	public void setEsercizio(Integer esercizio) {
		this.getVariazioneResidua().setEsercizio(esercizio);
	}

	@Override
	public Long getPg_variazione() {
		Var_stanz_resBulk variazioneResidua = this.getVariazioneResidua();
		if (variazioneResidua == null)
			return null;
		return variazioneResidua.getPg_variazione();
	}
	
	@Override
	public void setPg_variazione(Long pg_variazione) {
		this.getVariazioneResidua().setPg_variazione(pg_variazione);
	}
	
	public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk getAccertamento() {
		return accertamento;
	}
	
	public void setAccertamento(it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento) {
		this.accertamento = accertamento;
	}
	
	@Override
	public String getCd_cds_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getCd_cds();
	}
	
	@Override
	public void setCd_cds_accertamento(String cd_cds_accertamento) {
		this.getAccertamento().setCd_cds(cd_cds_accertamento);
	}
	
	@Override
	public Integer getEsercizio_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getEsercizio();
	}
	
	@Override
	public void setEsercizio_accertamento(Integer esercizio_accertamento) {
		this.getAccertamento().setEsercizio(esercizio_accertamento);
	}
	
	@Override
	public Integer getEsercizio_ori_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getEsercizio_originale();
	}
	
	@Override
	public void setEsercizio_ori_accertamento(Integer esercizio_ori_accertamento) {
		this.getAccertamento().setEsercizio_originale(esercizio_ori_accertamento);
	}
	
	@Override
	public Long getPg_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getPg_accertamento();
	}
	
	@Override
	public void setPg_accertamento(Long pg_accertamento) {
		this.getAccertamento().setPg_accertamento(pg_accertamento);
	}
}
