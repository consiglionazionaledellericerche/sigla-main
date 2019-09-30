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

public abstract class MandatoCupBulk extends MandatoCupBase {

	CupBulk  cup  = new CupBulk();
	
	public MandatoCupBulk() {
		super();
	}

	public MandatoCupBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.String cd_cup) {
		super(cd_cds, esercizio, pg_mandato, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, cd_cup);
		setMandato_riga(new Mandato_rigaIBulk(cd_cds, cd_cds_doc_amm, cd_tipo_documento_amm, cd_uo_doc_amm, esercizio, esercizio_doc_amm, esercizio_obbligazione, pg_doc_amm, pg_mandato, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario));
		setCup(new CupBulk(cd_cup));
	}

	public abstract Mandato_rigaBulk getMandato_riga();
	
	public abstract void setMandato_riga(Mandato_rigaBulk mandato_riga);

	
	public java.lang.String getCdCds() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds();
	}

	public void setCdCds(String cd_cds) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_cds(cd_cds);
	}

	public java.lang.Integer getEsercizio() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio(esercizio);
	}

	public Long getPgMandato() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_mandato();
	}
	public void setPgMandato(Long pg_mandato) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_mandato(pg_mandato);
	}

	public Integer getEsercizioObbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_obbligazione();
	}
	public void setEsercizioObbligazione(Integer esercizio_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_obbligazione(esercizio_obbligazione);
	}
	public Integer getEsercizioOriObbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_ori_obbligazione();
	}
	public void setEsercizioOriObbligazione(Integer esercizio_ori_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_ori_obbligazione(esercizio_ori_obbligazione);
	}

	public Long getPgObbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_obbligazione();
	}
	public void setPgObbligazione(Long pg_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_obbligazione(pg_obbligazione);
	}

	public Long getPgObbligazioneScadenzario() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_obbligazione_scadenzario();
	}
	public void setPgObbligazioneScadenzario(Long pg_obbligazione_scadenzario) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
	}

	public String getCdCdsDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds_doc_amm();
	}
	public void setCdCdsDocAmm(String cd_cds_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_cds_doc_amm(cd_cds_doc_amm);
	}

	public String getCdUoDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_uo_doc_amm();
	}
	public void setCdUoDocAmm(String cd_uo_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_uo_doc_amm(cd_uo_doc_amm);
	}

	public Integer getEsercizioDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_doc_amm();
	}
	public void setEsercizioDocAmm(Integer esercizio_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_doc_amm(esercizio_doc_amm);
	}

	public String getCdTipoDocumentoAmm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_tipo_documento_amm();
	}
	public void setCdTipoDocumentoAmm(String cd_tipo_documento_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_tipo_documento_amm(cd_tipo_documento_amm);
	}

	public Long getPgDocAmm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_doc_amm();
	}
	
	public void setPgDocAmm(Long pg_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_doc_amm(pg_doc_amm);
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