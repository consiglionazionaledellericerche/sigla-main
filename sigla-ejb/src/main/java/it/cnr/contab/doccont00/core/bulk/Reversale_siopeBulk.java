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
 * Date 14/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

public abstract class Reversale_siopeBulk extends Reversale_siopeBase {

	Codici_siopeBulk  codice_siope  = new Codici_siopeBulk();
	
	protected BulkList reversaleSiopeCupColl = new BulkList();

	public Reversale_siopeBulk() {
		super();
	}
	public Reversale_siopeBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(cd_cds, esercizio, pg_reversale, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope);
		setReversale_riga(new Reversale_rigaIBulk(cd_cds, esercizio, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, pg_reversale));
		setCodice_siope(new Codici_siopeBulk(esercizio_siope, ti_gestione, cd_siope));
	}
	
	public abstract Reversale_rigaBulk getReversale_riga();
	
	public abstract void setReversale_riga(Reversale_rigaBulk reversale_riga);

	public Codici_siopeBulk getCodice_siope() {
		return codice_siope;
	}
	
	public void setCodice_siope(Codici_siopeBulk codice_siope) {
		this.codice_siope = codice_siope;
	}

	public java.lang.String getCd_cds() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds();
	}

	public void setCd_cds(String cd_cds) {
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

	public Long getPg_reversale() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_reversale();
	}
	public void setPg_reversale(Long pg_reversale) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_reversale(pg_reversale);
	}

	public Integer getEsercizio_accertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_accertamento();
	}
	public void setEsercizio_accertamento(Integer esercizio_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_accertamento(esercizio_accertamento);
	}
	
	public Integer getEsercizio_ori_accertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_ori_accertamento();
	}
	public void setEsercizio_ori_accertamento(Integer esercizio_ori_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_ori_accertamento(esercizio_ori_accertamento);
	}

	public Long getPg_accertamento() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_accertamento();
	}
	public void setPg_accertamento(Long pg_accertamento) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_accertamento(pg_accertamento);
	}

	public Long getPg_accertamento_scadenzario() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_accertamento_scadenzario();
	}
	public void setPg_accertamento_scadenzario(Long pg_accertamento_scadenzario) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_accertamento_scadenzario(pg_accertamento_scadenzario);
	}

	public String getCd_cds_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds_doc_amm();
	}
	public void setCd_cds_doc_amm(String cd_cds_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_cds_doc_amm(cd_cds_doc_amm);
	}

	public String getCd_uo_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_uo_doc_amm();
	}
	public void setCd_uo_doc_amm(String cd_uo_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_uo_doc_amm(cd_uo_doc_amm);
	}

	public Integer getEsercizio_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_doc_amm();
	}
	public void setEsercizio_doc_amm(Integer esercizio_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setEsercizio_doc_amm(esercizio_doc_amm);
	}

	public String getCd_tipo_documento_amm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getCd_tipo_documento_amm();
	}
	public void setCd_tipo_documento_amm(String cd_tipo_documento_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setCd_tipo_documento_amm(cd_tipo_documento_amm);
	}

	public Long getPg_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga == null)
			return null;
		return riga.getPg_doc_amm();
	}
	
	public void setPg_doc_amm(Long pg_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk riga = this.getReversale_riga();
		if (riga != null) this.getReversale_riga().setPg_doc_amm(pg_doc_amm);
	}

	public Integer getEsercizio_siope() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getEsercizio();
	}

	public void setEsercizio_siope(Integer esercizio_siope) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setEsercizio(esercizio_siope);
	}

	public String getTi_gestione() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getTi_gestione();
	}

	public void setTi_gestione(String ti_gestione) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setTi_gestione(ti_gestione);
	}

	public String getCd_siope() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getCd_siope();
	}
	
	public void setCd_siope(String cd_siope) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setCd_siope(cd_siope);
	}
	public BulkList<ReversaleSiopeCupBulk> getReversaleSiopeCupColl() {
		return reversaleSiopeCupColl;
	}
	public void setReversaleSiopeCupColl(BulkList<ReversaleSiopeCupBulk> reversaleSiopeCupColl) {
		this.reversaleSiopeCupColl = reversaleSiopeCupColl;
	}
	
	public int addToReversaleSiopeCupColl( ReversaleSiopeCupBulk reversale_siope_cup ) 
	{
		reversaleSiopeCupColl.add(reversale_siope_cup);
		reversale_siope_cup.setReversaleSiope(this); 
		if (reversale_siope_cup.getImporto()==null && reversaleSiopeCupColl.size()==1) 
			reversale_siope_cup.setImporto(this.getImporto());
		else
			reversale_siope_cup.setImporto(BigDecimal.ZERO);
		return reversaleSiopeCupColl.size()-1;
	}
	public ReversaleSiopeCupBulk removeFromReversaleSiopeCupColl(int index) 
	{
		ReversaleSiopeCupBulk reversale_siope_cup = (ReversaleSiopeCupBulk)reversaleSiopeCupColl.remove(index);
		
		return reversale_siope_cup;
	}
	public BigDecimal getIm_associato_cup(){
		BigDecimal totale = Utility.ZERO;
		for (Iterator i = getReversaleSiopeCupColl().iterator(); i.hasNext();) totale = totale.add(((ReversaleSiopeCupBulk)i.next()).getImporto());
		return Utility.nvl(totale);
	}
	public BigDecimal getIm_da_associare_cup(){
		return Utility.nvl(getImporto()).subtract(Utility.nvl(getIm_associato_cup()));
	}
	/*
	 * Ritorna l'informazione circa la totale o parziale associazione della
	 * riga a codici CUP.
	 * 
	 * return
	 * 	"T" = SIOPE_TOTALMENTE_ASSOCIATO
	 *  "P" = SIOPE_PARZIALMENTE_ASSOCIATO
	 *  "N" = SIOPE_NON_ASSOCIATO
	 * 		
	 */
	public String getTipoAssociazioneCup() {
		BigDecimal totCup = getIm_associato_cup();
		if (getIm_da_associare_cup().compareTo(Utility.ZERO)==0) return Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO;
		if (totCup.compareTo(Utility.ZERO)==0) return Mandato_rigaBulk.SIOPE_NON_ASSOCIATO;
		return Mandato_rigaBulk.SIOPE_PARZIALMENTE_ASSOCIATO;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { reversaleSiopeCupColl};

	}
}