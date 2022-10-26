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

package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.jada.bulk.ValidationException;

import java.sql.Timestamp;
import java.util.Optional;

public class Liquidazione_ivaBulk extends Liquidazione_ivaBase implements IDocumentoCogeBulk {
	private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

	public Liquidazione_ivaBulk() {
		super();
	}

	public Liquidazione_ivaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id) {
		super(cd_cds,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,report_id);
	}

	public void validate() throws ValidationException {

		super.validate();

		if (getAnnotazioni() != null && getAnnotazioni().length() > 1000)
			throw new ValidationException("Il campo \"annotazioni\" è troppo lungo: il massimo consentito è di 1000 caratteri!");
	}
	public boolean equalsByImporti(Liquidazione_ivaBulk bulk) {
		if(!compareKey(getCd_cds(),bulk.getCd_cds())) return false;
		if(!compareKey(getCd_unita_organizzativa(),bulk.getCd_unita_organizzativa())) return false;
		if(!compareKey(getEsercizio(),bulk.getEsercizio())) return false;
		if(!compareKey(getDt_inizio(),bulk.getDt_inizio())) return false;
		if(!compareKey(getDt_fine(),bulk.getDt_fine())) return false;
		if(!compareKey(getStato(),bulk.getStato())) return false;
		if(!compareKey(getIva_vendite(),bulk.getIva_vendite())) return false;
		if(!compareKey(getIva_vendite_diff(),bulk.getIva_vendite_diff())) return false;
		if(!compareKey(getIva_vend_diff_esig(),bulk.getIva_vend_diff_esig())) return false;
		if(!compareKey(getIva_autofatt(),bulk.getIva_autofatt())) return false;
		if(!compareKey(getIva_intraue(),bulk.getIva_intraue())) return false;
		if(!compareKey(getIva_debito(),bulk.getIva_debito())) return false;
		if(!compareKey(getIva_acquisti(),bulk.getIva_acquisti())) return false;
		if(!compareKey(getIva_acq_non_detr(),bulk.getIva_acq_non_detr())) return false;
		if(!compareKey(getIva_acquisti_diff(),bulk.getIva_acquisti_diff())) return false;
		if(!compareKey(getIva_acq_diff_esig(),bulk.getIva_acq_diff_esig())) return false;
		if(!compareKey(getIva_credito(),bulk.getIva_credito())) return false;
		if(!compareKey(getVar_imp_per_prec(),bulk.getVar_imp_per_prec())) return false;
		if(!compareKey(getIva_non_vers_per_prec(),bulk.getIva_non_vers_per_prec())) return false;
		if(!compareKey(getIva_deb_cred_per_prec(),bulk.getIva_deb_cred_per_prec())) return false;
		if(!compareKey(getCred_iva_comp_detr(),bulk.getCred_iva_comp_detr())) return false;
		if(!compareKey(getIva_deb_cred(),bulk.getIva_deb_cred())) return false;
		if(!compareKey(getInt_deb_liq_trim(),bulk.getInt_deb_liq_trim())) return false;
		if(!compareKey(getCred_iva_spec_detr(),bulk.getCred_iva_spec_detr())) return false;
		if(!compareKey(getAcconto_iva_vers(),bulk.getAcconto_iva_vers())) return false;
		if(!compareKey(getIva_da_versare(),bulk.getIva_da_versare())) return false;
		if(!compareKey(getIva_versata(),bulk.getIva_versata())) return false;
		if(!compareKey(getCred_iva_infrann_rimb(),bulk.getCred_iva_infrann_rimb())) return false;
		if(!compareKey(getIva_credito_no_prorata(),bulk.getIva_credito_no_prorata())) return false;
		if(!compareKey(getPerc_prorata_detraibile(),bulk.getPerc_prorata_detraibile())) return false;
		if(!compareKey(getCred_iva_infrann_comp(),bulk.getCred_iva_infrann_comp())) return false;
		if(!compareKey(getIva_liq_esterna(),bulk.getIva_liq_esterna())) return false;
		if(!compareKey(getIva_vend_diff_es_prec_esig(),bulk.getIva_vend_diff_es_prec_esig())) return false;
		if(!compareKey(getIva_acq_diff_es_prec_esig(),bulk.getIva_acq_diff_es_prec_esig())) return false;
		if(!compareKey(getIva_ven_split_payment(),bulk.getIva_ven_split_payment())) return false;
		if(!compareKey(getIva_acq_split_payment(),bulk.getIva_acq_split_payment())) return false;
		return true;
	}

	@Override
	public String getCd_tipo_doc() {
		return TipoDocumentoEnum.LIQUIDAZIONE_IVA.getValue();
	}

	@Override
	public String getCd_uo() {
		return this.getCd_unita_organizzativa();
	}

	/**
	 * Ritorna sempre valore null in quanto campo valido solo per documeti amministrativi e contabili
	 */
	@Override
	public Long getPg_doc() {
		return null;
	}

	@Override
	public Timestamp getDtInizioLiquid() {
		return this.getDt_inizio();
	}

	@Override
	public Timestamp getDtFineLiquid() {
		return this.getDt_fine();
	}

	@Override
	public String getTipoLiquid() {
		return this.getTipo_liquidazione();
	}

	@Override
	public Long getReportIdLiquid() {
		return this.getReport_id();
	}

	@Override
	public TipoDocumentoEnum getTipoDocumentoEnum() {
		return TipoDocumentoEnum.LIQUIDAZIONE_IVA;
	}

	@Override
	public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
		return scrittura_partita_doppia;
	}

	@Override
	public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
		this.scrittura_partita_doppia = scrittura_partita_doppia;
	}

	@Override
	public Timestamp getDt_contabilizzazione() {
		return this.getDt_fine();
	}
}