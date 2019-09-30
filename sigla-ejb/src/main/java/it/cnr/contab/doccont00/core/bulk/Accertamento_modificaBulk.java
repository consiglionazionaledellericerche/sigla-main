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
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Accertamento_modificaBulk extends Accertamento_modificaBase {

	private AccertamentoBulk accertamento = new AccertamentoBulk();	
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds = new CdsBulk();
	private Var_stanz_resBulk variazione;
	private BulkList accertamento_mod_voceColl = new BulkList();

	public Accertamento_modificaBulk() {
		super();
	}
	public Accertamento_modificaBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica) {
		super(cd_cds, esercizio, pg_modifica);
	}
	public AccertamentoBulk getAccertamento() {
		return accertamento;
	}
	
	public void setAccertamento(AccertamentoBulk accertamento) {
		this.accertamento = accertamento;
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp, context);
		if (getVariazione()==null && !((it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context)).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0))
			this.setVariazione(new Var_stanz_resBulk());
		return this;
	}
	
	public java.lang.Integer getEsercizio_originale() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getEsercizio_originale();
	}

	public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
		this.getAccertamento().setEsercizio_originale(esercizio_originale);
	}

	public java.lang.Long getPg_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getPg_accertamento();
	}

	public void setPg_accertamento(java.lang.Long pg_accertamento) {
		this.getAccertamento().setPg_accertamento(pg_accertamento);
	}

	public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
		return cds;
	}
	

	public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk cds) {
		this.cds = cds;
	}
	
	public java.lang.String getCd_cds() {
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}

	public void setCd_cds(String cd_cds) {
		this.getCds().setCd_unita_organizzativa(cd_cds);
	}

	public BulkList getAccertamento_mod_voceColl() {
		return accertamento_mod_voceColl;
	}

	public void setAccertamento_mod_voceColl(BulkList accertamento_mod_voceColl) {
		this.accertamento_mod_voceColl = accertamento_mod_voceColl;
	}

	/**
	 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
	 * bulk da rendere persistenti insieme al ricevente.
	 * L'implementazione standard restituisce <code>null</code>.
	 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
	 */ 
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				accertamento_mod_voceColl  };
	}

	public String getCd_tipo_documento_cont() {
		if (this.getAccertamento() == null)
			return null;
		return "ACR_MOD".toString();
	}
	public int addToAccertamento_mod_voceColl(OggettoBulk dett) {
		if (dett instanceof Accertamento_mod_voceBulk)
			((Accertamento_mod_voceBulk)dett).setAccertamento_modifica(this);
		accertamento_mod_voceColl.add(dett);
		return accertamento_mod_voceColl.size()-1;
	}

	public OggettoBulk removeFromAccertamento_mod_voceColl(int index) {
		OggettoBulk dett = (OggettoBulk)accertamento_mod_voceColl.remove(index);
		return dett;
	}
	public void validate() throws ValidationException {
		super.validate();

		if ( getDs_modifica() == null || getDs_modifica().equals("") )
			throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );
	}
	public boolean isTemporaneo() {

		if (getPg_modifica() == null)
			return false;
		return getPg_modifica().longValue() < 0;
	}
	public Var_stanz_resBulk getVariazione() {
		return variazione;
	}
	public void setVariazione(Var_stanz_resBulk variazione) {
		this.variazione = variazione;
	}
	public Long getPg_variazione() {
		Var_stanz_resBulk variazione = this.getVariazione();
		if (variazione == null)
			return null;
		return variazione.getPg_variazione();
	}
	public boolean isROVariazioni() {
		return (getPg_variazione()==null);
	}
}