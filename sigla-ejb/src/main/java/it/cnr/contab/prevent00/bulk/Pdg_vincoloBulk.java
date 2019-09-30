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
package it.cnr.contab.prevent00.bulk;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Pdg_vincoloBulk extends Pdg_vincoloBase {
	private static final long serialVersionUID = 1L;

	private WorkpackageBulk lineaAttivita;
    private Elemento_voceBulk elementoVoce;
    
    private it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento;

	public Pdg_vincoloBulk() {
		super();
	}

	public Pdg_vincoloBulk(java.lang.Integer esercizio, java.lang.Integer esercizio_res, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.Long pg_vincolo) {
		super(esercizio, esercizio_res, cd_centro_responsabilita, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_elemento_voce, pg_vincolo);
		
	}
	
	public WorkpackageBulk getLineaAttivita() {
		return lineaAttivita;
	}
	
	public void setLineaAttivita(WorkpackageBulk lineaAttivita) {
		this.lineaAttivita = lineaAttivita;
	}

	public Elemento_voceBulk getElementoVoce() {
		return elementoVoce;
	}
	
	public void setElementoVoce(Elemento_voceBulk elementoVoce) {
		this.elementoVoce = elementoVoce;
	}
	
	@Override
	public String getCd_centro_responsabilita() {
		WorkpackageBulk linea_attivita = this.getLineaAttivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_centro_responsabilita();
	}
	
	@Override
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.getLineaAttivita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	@Override
	public String getCd_linea_attivita() {
		WorkpackageBulk linea_attivita = this.getLineaAttivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_linea_attivita();
	}
	
	@Override
	public void setCd_linea_attivita(String cd_linea_attivita) {
		this.getLineaAttivita().setCd_linea_attivita(cd_linea_attivita);
	}
	
	@Override
	public Integer getEsercizio() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return elementoVoce.getEsercizio();
	}
	
	@Override
	public void setEsercizio(Integer esercizio) {
		this.getElementoVoce().setEsercizio(esercizio);
	}

	@Override
	public String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	
	@Override
	public void setTi_appartenenza(String ti_appartenenza) {
		this.getElementoVoce().setTi_appartenenza(ti_appartenenza);
	}

	@Override
	public String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	
	@Override
	public void setTi_gestione(String ti_gestione) {
		this.getElementoVoce().setTi_gestione(ti_gestione);
	}
	
	@Override
	public String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	
	@Override
	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.getElementoVoce().setCd_elemento_voce(cd_elemento_voce);
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

	V_assestatoBulk	assestatoRisorseCopertura;
	
	public V_assestatoBulk getAssestatoRisorseCopertura() {
		return assestatoRisorseCopertura;
	}
	
	public void setAssestatoRisorseCopertura(V_assestatoBulk assestatoRisorseCopertura) {
		this.assestatoRisorseCopertura = assestatoRisorseCopertura;
	}
}
