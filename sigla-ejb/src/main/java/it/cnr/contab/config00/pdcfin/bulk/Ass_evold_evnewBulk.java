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

package it.cnr.contab.config00.pdcfin.bulk;

import java.util.Dictionary;

@SuppressWarnings("unchecked")
public class Ass_evold_evnewBulk extends Ass_evold_evnewBase {
	private static final long serialVersionUID = 1L;

	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	@SuppressWarnings("rawtypes")
	private final static java.util.Dictionary ti_gestioneKeys;
	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	

	protected Elemento_voceBulk elemento_voce_old = new Elemento_voceBulk();
	protected Elemento_voceBulk elemento_voce_new = new Elemento_voceBulk();	

	public Ass_evold_evnewBulk() {
		super();
	}

	public Elemento_voceBulk getElemento_voce_old() {
		return this.elemento_voce_old;
	}

	public void setElemento_voce_old(Elemento_voceBulk elemento_voce_old) {
		this.elemento_voce_old = elemento_voce_old;
	}

	public Elemento_voceBulk getElemento_voce_new() {
		return this.elemento_voce_new;
	}

	public void setElemento_voce_new(Elemento_voceBulk elemento_voce_new) {
		this.elemento_voce_new = elemento_voce_new;
	}

	public java.lang.Integer getEsercizio_old() {
		Elemento_voceBulk elemento_voce_old = this.getElemento_voce_old();
		if (elemento_voce_old == null)
			return null;
		return elemento_voce_old.getEsercizio();
	}

	public void setEsercizio_old(java.lang.Integer esercizio_old) {
		this.getElemento_voce_old().setEsercizio(esercizio_old);
	}

	public java.lang.String getTi_appartenenza_old() {
		Elemento_voceBulk elemento_voce_old = this.getElemento_voce_old();
		if (elemento_voce_old == null)
			return null;
		return elemento_voce_old.getTi_appartenenza();
	}

	public void setTi_appartenenza_old(java.lang.String ti_appartenenza_old) {
		this.getElemento_voce_old().setTi_appartenenza(ti_appartenenza_old);
	}

	public java.lang.String getTi_gestione_old() {
		Elemento_voceBulk elemento_voce_old = this.getElemento_voce_old();
		if (elemento_voce_old == null)
			return null;
		return elemento_voce_old.getTi_gestione();
	}

	public void setTi_gestione_old(java.lang.String ti_gestione_old) {
		this.getElemento_voce_old().setTi_gestione(ti_gestione_old);
	}

	public java.lang.String getCd_elemento_voce_old() {
		Elemento_voceBulk elemento_voce_old = this.getElemento_voce_old();
		if (elemento_voce_old == null)
			return null;
		return elemento_voce_old.getCd_elemento_voce();
	}

	public void setCd_elemento_voce_old(java.lang.String cd_elemento_voce_old) {
		this.getElemento_voce_old().setCd_elemento_voce(cd_elemento_voce_old);
	}

	public java.lang.Integer getEsercizio_new() {
		Elemento_voceBulk elemento_voce_new = this.getElemento_voce_new();
		if (elemento_voce_new == null)
			return null;
		return elemento_voce_new.getEsercizio();
	}

	public void setEsercizio_new(java.lang.Integer esercizio_new) {
		this.getElemento_voce_new().setEsercizio(esercizio_new);
	}

	public java.lang.String getTi_appartenenza_new() {
		Elemento_voceBulk elemento_voce_new = this.getElemento_voce_new();
		if (elemento_voce_new == null)
			return null;
		return elemento_voce_new.getTi_appartenenza();
	}

	public void setTi_appartenenza_new(java.lang.String ti_appartenenza_new) {
		this.getElemento_voce_new().setTi_appartenenza(ti_appartenenza_new);
	}

	public java.lang.String getTi_gestione_new() {
		Elemento_voceBulk elemento_voce_new = this.getElemento_voce_new();
		if (elemento_voce_new == null)
			return null;
		return elemento_voce_new.getTi_gestione();
	}

	public void setTi_gestione_new(java.lang.String ti_gestione_new) {
		this.getElemento_voce_new().setTi_gestione(ti_gestione_new);
	}

	public java.lang.String getCd_elemento_voce_new() {
		Elemento_voceBulk elemento_voce_new = this.getElemento_voce_new();
		if (elemento_voce_new == null)
			return null;
		return elemento_voce_new.getCd_elemento_voce();
	}

	public void setCd_elemento_voce_new(java.lang.String cd_elemento_voce_new) {
		this.getElemento_voce_new().setCd_elemento_voce(cd_elemento_voce_new);
	}

	public boolean isROElementoVoce() {
		return this.getTi_gestione_search()==null;
	}

	@SuppressWarnings("rawtypes")
	public Dictionary getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}
	
	private java.lang.String ti_gestione_search;

	public java.lang.String getTi_gestione_search() {
		return ti_gestione_search;
	}
	
	public void setTi_gestione_search(java.lang.String ti_gestione_search) {
		this.ti_gestione_search = ti_gestione_search;
	}
	
	private java.lang.Integer esercizio_old_search;
	
	public java.lang.Integer getEsercizio_old_search() {
		return esercizio_old_search;
	}
	
	public void setEsercizio_old_search(java.lang.Integer esercizio_old_search) {
		this.esercizio_old_search = esercizio_old_search;
	}
	
	private java.lang.Integer esercizio_new_search;
	
	public java.lang.Integer getEsercizio_new_search() {
		return esercizio_new_search;
	}
	
	public void setEsercizio_new_search(java.lang.Integer esercizio_new_search) {
		this.esercizio_new_search = esercizio_new_search;
	}
}