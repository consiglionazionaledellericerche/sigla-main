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
* Created by Generator 1.0
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class Obbligazione_modificaBulk extends Obbligazione_modificaBase {

	private ObbligazioneBulk obbligazione = new ObbligazioneBulk();	
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds = new CdsBulk();
	private BulkList obbligazione_mod_voceColl = new BulkList();

	public Obbligazione_modificaBulk() {
		super();
	}

	public Obbligazione_modificaBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica) {
		super(cd_cds, esercizio, pg_modifica);
	}

	public ObbligazioneBulk getObbligazione() {
		return obbligazione;
	}
	
	public void setObbligazione(ObbligazioneBulk obbligazione) {
		this.obbligazione = obbligazione;
	}

	public java.lang.Integer getEsercizio_originale() {
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getEsercizio_originale();
	}

	public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
		this.getObbligazione().setEsercizio_originale(esercizio_originale);
	}

	public java.lang.Long getPg_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getPg_obbligazione();
	}

	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.getObbligazione().setPg_obbligazione(pg_obbligazione);
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

	public BulkList getObbligazione_mod_voceColl() {
		return obbligazione_mod_voceColl;
	}

	public void setObbligazione_mod_voceColl(BulkList obbligazione_mod_voceColl) {
		this.obbligazione_mod_voceColl = obbligazione_mod_voceColl;
	}

	/**
	 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
	 * bulk da rendere persistenti insieme al ricevente.
	 * L'implementazione standard restituisce <code>null</code>.
	 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
	 */ 
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				obbligazione_mod_voceColl  };
	}

	public String getCd_tipo_documento_cont() {
		if (this.getObbligazione() == null)
			return null;
		if (this.getObbligazione().isObbligazioneResiduo())
			return "OBB_MOD".toString();
		else if (this.getObbligazione().isObbligazioneResiduoImproprio())
			return "OBB_MOD".toString();
		else 
			return "OBB_MOD".toString();
	}
	public int addToObbligazione_mod_voceColl(OggettoBulk dett) {
		if (dett instanceof Obbligazione_mod_voceBulk)
			((Obbligazione_mod_voceBulk)dett).setObbligazione_modifica(this);
		obbligazione_mod_voceColl.add(dett);
		return obbligazione_mod_voceColl.size()-1;
	}

	public OggettoBulk removeFromObbligazione_mod_voceColl(int index) {
		OggettoBulk dett = (OggettoBulk)obbligazione_mod_voceColl.remove(index);
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
}