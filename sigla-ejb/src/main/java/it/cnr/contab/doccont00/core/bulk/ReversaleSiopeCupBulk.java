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
 * Date 20/06/2013
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public abstract class ReversaleSiopeCupBulk extends ReversaleSiopeCupBase {
	
	public abstract Reversale_siopeBulk getReversaleSiope();
	
	public abstract void setReversaleSiope(Reversale_siopeBulk reversale_siope);
	
	/**
	 * [REVERSALE_SIOPE Conserva, per ogni riga di Reversale, il o i codici SIOPE associati ed il relativo importo.]
	 **/
	private Reversale_siopeBulk reversaleSiope =  new Reversale_siopeIBulk();
	/**
	 * [CUP null]
	 **/
	private CupBulk cup =  new CupBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: REVERSALE_SIOPE_CUP
	 **/
	public ReversaleSiopeCupBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: REVERSALE_SIOPE_CUP
	 **/
	public ReversaleSiopeCupBulk(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgReversale, java.lang.Integer esercizioAccertamento, java.lang.Integer esercizioOriAccertamento, java.lang.Long pgAccertamento, java.lang.Long pgAccertamentoScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.Integer esercizioSiope, java.lang.String tiGestione, java.lang.String cdSiope, java.lang.String cdCup) {
		super(cdCds, esercizio, pgReversale, esercizioAccertamento, esercizioOriAccertamento, pgAccertamento, pgAccertamentoScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, esercizioSiope, tiGestione, cdSiope, cdCup);
		setReversaleSiope( new Reversale_siopeIBulk(cdCds,esercizio,pgReversale,esercizioAccertamento,esercizioOriAccertamento,pgAccertamento,pgAccertamentoScadenzario,cdCdsDocAmm,cdUoDocAmm,esercizioDocAmm,cdTipoDocumentoAmm,pgDocAmm,esercizioSiope,tiGestione,cdSiope) );
		setCup( new CupBulk(cdCup) );
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public CupBulk getCup() {
		return cup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setCup(CupBulk cup)  {
		this.cup=cup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getReversaleSiope().setCd_cds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getReversaleSiope().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgReversale]
	 **/
	public java.lang.Long getPgReversale() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getPg_reversale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgReversale]
	 **/
	public void setPgReversale(java.lang.Long pgReversale)  {
		this.getReversaleSiope().setPg_reversale(pgReversale);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioAccertamento]
	 **/
	public java.lang.Integer getEsercizioAccertamento() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getEsercizio_accertamento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioAccertamento]
	 **/
	public void setEsercizioAccertamento(java.lang.Integer esercizioAccertamento)  {
		this.getReversaleSiope().setEsercizio_accertamento(esercizioAccertamento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriAccertamento]
	 **/
	public java.lang.Integer getEsercizioOriAccertamento() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getEsercizio_ori_accertamento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriAccertamento]
	 **/
	public void setEsercizioOriAccertamento(java.lang.Integer esercizioOriAccertamento)  {
		this.getReversaleSiope().setEsercizio_ori_accertamento(esercizioOriAccertamento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgAccertamento]
	 **/
	public java.lang.Long getPgAccertamento() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getPg_accertamento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgAccertamento]
	 **/
	public void setPgAccertamento(java.lang.Long pgAccertamento)  {
		this.getReversaleSiope().setPg_accertamento(pgAccertamento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgAccertamentoScadenzario]
	 **/
	public java.lang.Long getPgAccertamentoScadenzario() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getPg_accertamento_scadenzario();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgAccertamentoScadenzario]
	 **/
	public void setPgAccertamentoScadenzario(java.lang.Long pgAccertamentoScadenzario)  {
		this.getReversaleSiope().setPg_accertamento_scadenzario(pgAccertamentoScadenzario);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsDocAmm]
	 **/
	public java.lang.String getCdCdsDocAmm() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getCd_cds_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsDocAmm]
	 **/
	public void setCdCdsDocAmm(java.lang.String cdCdsDocAmm)  {
		this.getReversaleSiope().setCd_cds_doc_amm(cdCdsDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoDocAmm]
	 **/
	public java.lang.String getCdUoDocAmm() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getCd_uo_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoDocAmm]
	 **/
	public void setCdUoDocAmm(java.lang.String cdUoDocAmm)  {
		this.getReversaleSiope().setCd_uo_doc_amm(cdUoDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioDocAmm]
	 **/
	public java.lang.Integer getEsercizioDocAmm() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getEsercizio_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioDocAmm]
	 **/
	public void setEsercizioDocAmm(java.lang.Integer esercizioDocAmm)  {
		this.getReversaleSiope().setEsercizio_doc_amm(esercizioDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoAmm]
	 **/
	public java.lang.String getCdTipoDocumentoAmm() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getCd_tipo_documento_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoAmm]
	 **/
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm)  {
		this.getReversaleSiope().setCd_tipo_documento_amm(cdTipoDocumentoAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocAmm]
	 **/
	public java.lang.Long getPgDocAmm() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getPg_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocAmm]
	 **/
	public void setPgDocAmm(java.lang.Long pgDocAmm)  {
		this.getReversaleSiope().setPg_doc_amm(pgDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioSiope]
	 **/
	public java.lang.Integer getEsercizioSiope() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getEsercizio_siope();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioSiope]
	 **/
	public void setEsercizioSiope(java.lang.Integer esercizioSiope)  {
		this.getReversaleSiope().setEsercizio_siope(esercizioSiope);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTiGestione() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getTi_gestione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTiGestione(java.lang.String tiGestione)  {
		this.getReversaleSiope().setTi_gestione(tiGestione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdSiope]
	 **/
	public java.lang.String getCdSiope() {
		Reversale_siopeBulk reversaleSiope = this.getReversaleSiope();
		if (reversaleSiope == null)
			return null;
		return getReversaleSiope().getCd_siope();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdSiope]
	 **/
	public void setCdSiope(java.lang.String cdSiope)  {
		this.getReversaleSiope().setCd_siope(cdSiope);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		CupBulk cup = this.getCup();
		if (cup == null)
			return null;
		return getCup().getCdCup();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.getCup().setCdCup(cdCup);
	}
}