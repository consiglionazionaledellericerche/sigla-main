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
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.util.List;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk;

public class Gruppo_cr_detBulk extends Gruppo_cr_detBase {
	public Gruppo_cr_detBulk() {
		super();
	}
	public Gruppo_cr_detBulk(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_regione, java.lang.Long pg_comune) {
		super(esercizio, cd_gruppo_cr, cd_regione, pg_comune);
		setGruppo(new Gruppo_crBulk(esercizio,cd_gruppo_cr));
	}
	private Rif_modalita_pagamentoBulk modalitaPagamento;
	private java.util.Collection modalitaOptions;
	private RegioneBulk regione = new RegioneBulk();
	private ComuneBulk comune = new ComuneBulk();
	private TerzoBulk terzo = new TerzoBulk();
	private Gruppo_crBulk gruppo= new Gruppo_crBulk();
	private BancaBulk banca = new BancaBulk();
	protected List bancaOptions;
public boolean isROModalitaPagamento() 
{
	 return  modalitaPagamento==null||modalitaPagamento.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 17.01.17)
 * @return boolean
 */
public boolean isAbledToInsertBank() {

	return !(getTerzo()!= null &&
		getModalitaPagamento() != null &&
		getBancaOptions()!= null &&
		!getBancaOptions().isEmpty());
}
public boolean isROBanca() 
{
	return banca == null || banca.getCrudStatus() == NORMAL;
}
public java.util.Collection getModalitaOptions() {
	return modalitaOptions;
}
public void setModalitaOptions(java.util.Collection modalita) {
	this.modalitaOptions = modalita;
}
public Rif_modalita_pagamentoBulk getModalitaPagamento() {
	return modalitaPagamento;
}
public void setModalitaPagamento(Rif_modalita_pagamentoBulk modalitaPagamento) {
	this.modalitaPagamento = modalitaPagamento;
}
public ComuneBulk getComune() {
	return comune;
}
public void setComune(ComuneBulk comune) {
	this.comune = comune;
}
public RegioneBulk getRegione() {
	return regione;
}
public void setRegione(RegioneBulk regione) {
	this.regione = regione;
}
public Gruppo_crBulk getGruppo() {
	return gruppo;
}
public void setGruppo(Gruppo_crBulk gruppo) {
	this.gruppo = gruppo;
}
public TerzoBulk getTerzo() {
	return terzo;
}
public void setTerzo(TerzoBulk terzo) {
	this.terzo = terzo;
}
public void setCd_terzo_versamento(Integer cd_terzo_versamento) {
	
	this.getTerzo().setCd_terzo(cd_terzo_versamento);
}

public Integer getCd_terzo_versamento() {
	if (terzo==null)
		return null;
	else
		return getTerzo().getCd_terzo();
}

public void setPg_comune(Long pg_comune) {
	getComune().setPg_comune(pg_comune);
}

public Long getPg_comune() {
	if (comune==null)
		return null;
	else
		return getComune().getPg_comune();
}

public void setCd_regione(String cd_regione) {
	getRegione().setCd_regione(cd_regione);
}

public String getCd_regione() {
	if(regione==null)
		return null;
	else		
		return getRegione().getCd_regione();
}

public String getCd_gruppo_cr() {
	if(gruppo==null)
		return null;
	else		
		return getGruppo().getCd_gruppo_cr();
}

public void setCd_gruppo_cr(String cd_gruppo_cr) {
	getGruppo().setCd_gruppo_cr(cd_gruppo_cr);
}

public void setEsercizio(Integer esercizio) {
	getGruppo().setEsercizio(esercizio);
}
public Integer getEsercizio() {
	if(gruppo==null)
		return null;
	else		
		return getGruppo().getEsercizio();
}

public void setCd_modalita_pagamento(String cd_modalita_pagamento) {

	getModalitaPagamento().setCd_modalita_pag(cd_modalita_pagamento);
}

public String getCd_modalita_pagamento() {
	if (modalitaPagamento==null)
		return null;
	else
		return getModalitaPagamento().getCd_modalita_pag();
}
public BancaBulk getBanca() {
	return banca;
}
public void setBanca(BancaBulk banca) {
	this.banca = banca;
}

public Long getPg_banca() {
	if (banca==null)
		return null;
	else
		return getBanca().getPg_banca();
}

public void setPg_banca(Long pg_banca) {
	getBanca().setPg_banca(pg_banca);
}
public List getBancaOptions() {
	return bancaOptions;
}
public void setBancaOptions(List bancaOptions) {
	this.bancaOptions = bancaOptions;
}
}