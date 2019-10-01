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
public abstract class MandatoSiopeCupBulk extends MandatoSiopeCupBase {
	
	/**
	 * [MANDATO_SIOPE Conserva, per ogni riga di Mandato, il o i codici SIOPE associati ed il relativo importo.]
	 **/
	public abstract Mandato_siopeBulk getMandatoSiope();
	
	public abstract void setMandatoSiope(Mandato_siopeBulk mandato_siope);
	/**
	 * [CUP null]
	 **/
	private CupBulk cup =  new CupBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MANDATO_SIOPE_CUP
	 **/
	public MandatoSiopeCupBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MANDATO_SIOPE_CUP
	 **/
	public MandatoSiopeCupBulk(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato, java.lang.Integer esercizioObbligazione, java.lang.Integer esercizioOriObbligazione, java.lang.Long pgObbligazione, java.lang.Long pgObbligazioneScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.Integer esercizioSiope, java.lang.String tiGestione, java.lang.String cdSiope, java.lang.String cdCup) {
		super(cdCds, esercizio, pgMandato, esercizioObbligazione, esercizioOriObbligazione, pgObbligazione, pgObbligazioneScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, esercizioSiope, tiGestione, cdSiope, cdCup);
		setMandatoSiope( new Mandato_siopeIBulk(cdCds,esercizio,pgMandato,esercizioObbligazione,esercizioOriObbligazione,pgObbligazione,pgObbligazioneScadenzario,cdCdsDocAmm,cdUoDocAmm,esercizioDocAmm,cdTipoDocumentoAmm,pgDocAmm,esercizioSiope,tiGestione,cdSiope) );
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
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getMandatoSiope().setCd_cds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getMandatoSiope().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMandato]
	 **/
	public java.lang.Long getPgMandato() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getPg_mandato();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMandato]
	 **/
	public void setPgMandato(java.lang.Long pgMandato)  {
		this.getMandatoSiope().setPg_mandato(pgMandato);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbligazione]
	 **/
	public java.lang.Integer getEsercizioObbligazione() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getEsercizio_obbligazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbligazione]
	 **/
	public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione)  {
		this.getMandatoSiope().setEsercizio_obbligazione(esercizioObbligazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriObbligazione]
	 **/
	public java.lang.Integer getEsercizioOriObbligazione() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getEsercizio_ori_obbligazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriObbligazione]
	 **/
	public void setEsercizioOriObbligazione(java.lang.Integer esercizioOriObbligazione)  {
		this.getMandatoSiope().setEsercizio_ori_obbligazione(esercizioOriObbligazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getPg_obbligazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.getMandatoSiope().setPg_obbligazione(pgObbligazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScadenzario]
	 **/
	public java.lang.Long getPgObbligazioneScadenzario() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getPg_obbligazione_scadenzario();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScadenzario]
	 **/
	public void setPgObbligazioneScadenzario(java.lang.Long pgObbligazioneScadenzario)  {
		this.getMandatoSiope().setPg_obbligazione_scadenzario(pgObbligazioneScadenzario);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsDocAmm]
	 **/
	public java.lang.String getCdCdsDocAmm() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getCd_cds_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsDocAmm]
	 **/
	public void setCdCdsDocAmm(java.lang.String cdCdsDocAmm)  {
		this.getMandatoSiope().setCd_cds_doc_amm(cdCdsDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoDocAmm]
	 **/
	public java.lang.String getCdUoDocAmm() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getCd_uo_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoDocAmm]
	 **/
	public void setCdUoDocAmm(java.lang.String cdUoDocAmm)  {
		this.getMandatoSiope().setCd_uo_doc_amm(cdUoDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioDocAmm]
	 **/
	public java.lang.Integer getEsercizioDocAmm() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getEsercizio_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioDocAmm]
	 **/
	public void setEsercizioDocAmm(java.lang.Integer esercizioDocAmm)  {
		this.getMandatoSiope().setEsercizio_doc_amm(esercizioDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoAmm]
	 **/
	public java.lang.String getCdTipoDocumentoAmm() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getCd_tipo_documento_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoAmm]
	 **/
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm)  {
		this.getMandatoSiope().setCd_tipo_documento_amm(cdTipoDocumentoAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocAmm]
	 **/
	public java.lang.Long getPgDocAmm() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getPg_doc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocAmm]
	 **/
	public void setPgDocAmm(java.lang.Long pgDocAmm)  {
		this.getMandatoSiope().setPg_doc_amm(pgDocAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioSiope]
	 **/
	public java.lang.Integer getEsercizioSiope() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getEsercizio_siope();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioSiope]
	 **/
	public void setEsercizioSiope(java.lang.Integer esercizioSiope)  {
		this.getMandatoSiope().setEsercizio_siope(esercizioSiope);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTiGestione() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getTi_gestione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTiGestione(java.lang.String tiGestione)  {
		this.getMandatoSiope().setTi_gestione(tiGestione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdSiope]
	 **/
	public java.lang.String getCdSiope() {
		Mandato_siopeBulk mandatoSiope = this.getMandatoSiope();
		if (mandatoSiope == null)
			return null;
		return getMandatoSiope().getCd_siope();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdSiope]
	 **/
	public void setCdSiope(java.lang.String cdSiope)  {
		this.getMandatoSiope().setCd_siope(cdSiope);
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