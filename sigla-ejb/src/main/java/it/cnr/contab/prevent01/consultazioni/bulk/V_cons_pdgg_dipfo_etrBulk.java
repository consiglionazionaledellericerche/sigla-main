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

package it.cnr.contab.prevent01.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_pdgg_dipfo_etrBulk extends V_cons_pdgg_dipfoBulk {
	
	private java.lang.String  ds_dettaglio; 
 	
	private java.math.BigDecimal tot_ent_ist_a1;

	private java.math.BigDecimal tot_inc_ist_a1;

	private java.math.BigDecimal tot_ent_aree_a1;
	
	private java.math.BigDecimal tot_inc_aree_a1;
	
	private java.math.BigDecimal tot_ent_a1;

	private java.math.BigDecimal tot_inc_a1;

	public V_cons_pdgg_dipfo_etrBulk() {
		super();
	}

	public java.math.BigDecimal getTot_inc_ist_a1() {
		return tot_inc_ist_a1;
	}

	public void setTot_inc_ist_a1(java.math.BigDecimal tot_inc_ist_a1) {
		this.tot_inc_ist_a1 = tot_inc_ist_a1;
	}

	public java.math.BigDecimal getTot_ent_aree_a1() {
		return tot_ent_aree_a1;
	}

	public void setTot_ent_aree_a1(java.math.BigDecimal tot_ent_aree_a1) {
		this.tot_ent_aree_a1 = tot_ent_aree_a1;
	}

	public java.math.BigDecimal getTot_inc_aree_a1() {
		return tot_inc_aree_a1;
	}

	public void setTot_inc_aree_a1(java.math.BigDecimal tot_inc_aree_a1) {
		this.tot_inc_aree_a1 = tot_inc_aree_a1;
	}

	public java.lang.String getDs_dettaglio() {
		return ds_dettaglio;
	}

	public void setDs_dettaglio(java.lang.String ds_dettaglio) {
		this.ds_dettaglio = ds_dettaglio;
	}

	public java.math.BigDecimal getTot_ent_a1() {
		return tot_ent_a1;
	}

	public void setTot_ent_a1(java.math.BigDecimal tot_ent_a1) {
		this.tot_ent_a1 = tot_ent_a1;
	}

	public java.math.BigDecimal getTot_ent_ist_a1() {
		return tot_ent_ist_a1;
	}

	public void setTot_ent_ist_a1(java.math.BigDecimal tot_ent_ist_a1) {
		this.tot_ent_ist_a1 = tot_ent_ist_a1;
	}

	public java.math.BigDecimal getTot_inc_a1() {
		return tot_inc_a1;
	}

	public void setTot_inc_a1(java.math.BigDecimal tot_inc_a1) {
		this.tot_inc_a1 = tot_inc_a1;
	}
}