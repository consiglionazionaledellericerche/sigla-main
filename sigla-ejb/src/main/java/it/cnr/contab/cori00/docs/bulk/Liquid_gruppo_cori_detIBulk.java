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
  *  Sottoclasse di <code>Liquid_gruppo_cori_detBulk</code>.
  *	E' stata creata una sottoclasse per poter utilizzare la vista <code>V_LIQUID_GRUPPO_CORI_DET</code>,
  *	nella visualizzazione dei dettagli di un gruppo CORI.
  *	Oltre alle proprietà della superclasse, sono state aggiunte:
  *	<dl>
  *	<dt> ds_terzo <code>String</code> il Terzo del Versamento
  *	<dt> dt_emissione_mandato  <code>Timestamp</code> la data di emissione del mandato
  *	<dt> im_cori <code>BigDecimal</code> l'importo del CORI
  * </dl>
  *
**/  
public class Liquid_gruppo_cori_detIBulk extends Liquid_gruppo_cori_detBulk {
	
	private String ds_terzo;
	private java.sql.Timestamp dt_emissione_mandato;
	private java.math.BigDecimal im_cori;
/**
 * Liquid_gruppo_cori_detIBulk constructor comment.
 */
public Liquid_gruppo_cori_detIBulk() {
	super();
}

public Liquid_gruppo_cori_detIBulk(String cd_cds, String cd_contributo_ritenuta, String cd_gruppo_cr, String cd_regione, String cd_unita_organizzativa, Integer esercizio, Long pg_compenso, Long pg_comune, Integer pg_liquidazione, String ti_ente_percipiente) {
	super(cd_cds, cd_contributo_ritenuta, cd_gruppo_cr, cd_regione, cd_unita_organizzativa, esercizio, pg_compenso, pg_comune, pg_liquidazione, ti_ente_percipiente);
}

public java.lang.String getDs_terzo() {
	return ds_terzo;
}

public java.sql.Timestamp getDt_emissione_mandato() {
	return dt_emissione_mandato;
}

public java.math.BigDecimal getIm_cori() {
	return im_cori;
}

public void setDs_terzo(java.lang.String newDs_terzo) {
	ds_terzo = newDs_terzo;
}

public void setDt_emissione_mandato(java.sql.Timestamp newDt_emissione_mandato) {
	dt_emissione_mandato = newDt_emissione_mandato;
}

public void setIm_cori(java.math.BigDecimal newIm_cori) {
	im_cori = newIm_cori;
}
}
