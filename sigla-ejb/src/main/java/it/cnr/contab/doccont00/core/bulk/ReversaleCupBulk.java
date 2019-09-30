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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;


import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public abstract class ReversaleCupBulk extends ReversaleCupBase {

	CupBulk  cup  = new CupBulk();
	
	public ReversaleCupBulk() {
		super();
	}

	public ReversaleCupBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.String cd_cup) {
		super(cd_cds, esercizio, pg_reversale, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, cd_cup);
		setReversale_riga(new Reversale_rigaIBulk(cd_cds, esercizio,esercizio_accertamento,esercizio_ori_accertamento,pg_accertamento ,pg_accertamento_scadenzario,pg_reversale));
		setCup(new CupBulk(cd_cup));
	}

	public abstract Reversale_rigaBulk getReversale_riga();
	
	public abstract void setReversale_riga(Reversale_rigaBulk reversale_riga);

	
	public java.lang.String getCdCds() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds();
	}

	public void setCdCds(String cd_cds) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_cds(cd_cds);
	}

	public java.lang.Integer getEsercizio() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio(esercizio);
	}

	public Long getPgReversale() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_reversale();
	}
	public void setPgReversale(Long pg_reversale) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_reversale(pg_reversale);
	}

	public Integer getEsercizioAccertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_accertamento();
	}
	public void setEsercizioAccertamento(Integer esercizio_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_accertamento(esercizio_accertamento);
	}
	public Integer getEsercizioOriAccertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_ori_accertamento();
	}
	public void setEsercizioOriAccertamento(Integer esercizio_ori_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_ori_accertamento(esercizio_ori_accertamento);
	}

	public Long getPgAccertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_accertamento();
	}
	public void setPgAccertamento(Long pg_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_accertamento(pg_accertamento);
	}

	public Long getPgAccertamentoScadenzario() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_accertamento_scadenzario();
	}
	public void setPgAccertamentoScadenzario(Long pg_accertamento_scadenzario) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_accertamento_scadenzario(pg_accertamento_scadenzario);
	}

	public String getCdCdsDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds_doc_amm();
	}
	public void setCdCdsDocAmm(String cd_cds_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_cds_doc_amm(cd_cds_doc_amm);
	}

	public String getCdUoDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_uo_doc_amm();
	}
	public void setCdUoDocAmm(String cd_uo_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_uo_doc_amm(cd_uo_doc_amm);
	}

	public Integer getEsercizioDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_doc_amm();
	}
	public void setEsercizioDocAmm(Integer esercizio_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_doc_amm(esercizio_doc_amm);
	}

	public String getCdTipoDocumentoAmm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_tipo_documento_amm();
	}
	public void setCdTipoDocumentoAmm(String cd_tipo_documento_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_tipo_documento_amm(cd_tipo_documento_amm);
	}

	public Long getPgDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_doc_amm();
	}
	
	public void setPgDocAmm(Long pg_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_doc_amm(pg_doc_amm);
	}
	public String getCdCup() {
		CupBulk cup = this.getCup();
		if (cup == null)
			return null;
		return cup.getCdCup();
	}
	
	public void setCdCup(String cd_cup) {
		CupBulk cup = this.getCup();
		if (cup != null) this.getCup().setCdCup(cd_cup);
	}

	public CupBulk getCup() {
		return cup;
	}

	public void setCup(CupBulk cup) {
		this.cup = cup;
	}
}