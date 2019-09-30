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

package it.cnr.contab.cori00.docs.bulk;

/**
  *  Questa classe è stata creata con lo scopo di utilizzare la vista <code>V_LIQUID_GRUPPO_CORI</code>,
  *	soprattutto per mostrare i dati nei CRUDController.
  *	Oltre alle proprietà della superclasse, sono state aggiunte:
  *	<dl>
  *	<dt> ds_gruppo_cr la <code>String</code> descrizione del gruppo CORI di appartenenza
  *	<dt> terzo_versamento  <code>String</code> il Terzo del versamento
  *	<dt> capitoliColl la <code>SimpleBulkList</code> lista dei Capitoli legati al dettaglio di versamento.
  * </dl>
  *
**/  
public class Liquid_gruppo_coriIBulk extends Liquid_gruppo_coriBulk {

	private String ds_gruppo_cr;
	private String terzo_versamento;
	//private String fl_accentrato;
		
	private it.cnr.jada.bulk.SimpleBulkList capitoliColl;
/**
 * Liquid_gruppo_coriIBulk constructor comment.
 */
public Liquid_gruppo_coriIBulk() {
	super();
}
/**
 * Liquid_gruppo_coriIBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_gruppo_cr java.lang.String
 * @param cd_regione java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_comune java.lang.Long
 * @param pg_liquidazione java.lang.Integer
 */
public Liquid_gruppo_coriIBulk(String cd_cds, String cd_gruppo_cr, String cd_regione, Integer esercizio, Long pg_comune, Integer pg_liquidazione) {
	super(cd_cds, cd_gruppo_cr, cd_regione, esercizio, pg_comune, pg_liquidazione);
}

public it.cnr.jada.bulk.SimpleBulkList getCapitoliColl() {
	return capitoliColl;
}

public java.lang.String getDs_gruppo_cr() {
	return ds_gruppo_cr;
}

public java.lang.String getTerzo_versamento() {
	return terzo_versamento;
}

public void setCapitoliColl(it.cnr.jada.bulk.SimpleBulkList newCapitoliColl) {
	capitoliColl = newCapitoliColl;
}

public void setDs_gruppo_cr(java.lang.String newDs_gruppo_cr) {
	ds_gruppo_cr = newDs_gruppo_cr;
}

public void setTerzo_versamento(java.lang.String newTerzo_versamento) {
	terzo_versamento = newTerzo_versamento;
}
}
