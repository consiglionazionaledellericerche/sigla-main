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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Unita_organizzativaBase extends Unita_organizzativaKey implements Keyed {
	// CD_AREA_RICERCA VARCHAR(30)
	private java.lang.String cd_area_ricerca;

	// CD_AREA_SCIENTIFICA VARCHAR(2)
	private java.lang.String cd_area_scientifica;

	// CD_PROPRIO_UNITA VARCHAR(30) NOT NULL
	private java.lang.String cd_proprio_unita;

	// CD_RESPONSABILE DECIMAL(8,0)
	private java.lang.Integer cd_responsabile;

	// CD_RESPONSABILE_AMM DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_amm;

	// CD_TIPO_UNITA VARCHAR(20) NOT NULL
	private java.lang.String cd_tipo_unita;

	// CD_UNITA_PADRE VARCHAR(30)
	private java.lang.String cd_unita_padre;

	// DS_UNITA_ORGANIZZATIVA VARCHAR(300) NOT NULL
	private java.lang.String ds_unita_organizzativa;

	// ESERCIZIO_FINE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_fine;

	// ESERCIZIO_INIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_inizio;

	// FL_CDS CHAR(1) NOT NULL
	private java.lang.Boolean fl_cds;

	// FL_PRESIDENTE_AREA CHAR(1)
	private java.lang.Boolean fl_presidente_area;

	// FL_RUBRICA CHAR(1)
	private java.lang.Boolean fl_rubrica;

	// FL_UO_CDS CHAR(1) NOT NULL
	private java.lang.Boolean fl_uo_cds;

	// LIVELLO DECIMAL(2,0) NOT NULL
	private java.lang.Integer livello;

	// PRC_COPERTURA_OBBLIG_2 DECIMAL(5,2)
	private java.math.BigDecimal prc_copertura_obblig_2;

	// PRC_COPERTURA_OBBLIG_3 DECIMAL(5,2)
	private java.math.BigDecimal prc_copertura_obblig_3;

	// ID_FUNZIONE_PUBBLICA VARCHAR(10)
	private java.lang.String id_funzione_pubblica;

	// CODICEAOOIPA VARCHAR(100)
	private java.lang.String codiceAooIpa;

	public Unita_organizzativaBase() {
	super();
}

	public Unita_organizzativaBase(java.lang.String cd_unita_organizzativa) {
	super(cd_unita_organizzativa);
}
	/*
	 * Getter dell'attributo cd_area_ricerca
	 */
	public java.lang.String getCd_area_ricerca() {
		return cd_area_ricerca;
	}
	/*
	 * Getter dell'attributo cd_area_scientifica
	 */
	public java.lang.String getCd_area_scientifica() {
		return cd_area_scientifica;
	}
	/*
	 * Getter dell'attributo cd_proprio_unita
	 */
	public java.lang.String getCd_proprio_unita() {
		return cd_proprio_unita;
	}
	/*
	 * Getter dell'attributo cd_responsabile
	 */
	public java.lang.Integer getCd_responsabile() {
		return cd_responsabile;
	}
	/*
	 * Getter dell'attributo cd_responsabile_amm
	 */
	public java.lang.Integer getCd_responsabile_amm() {
		return cd_responsabile_amm;
	}
	/*
	 * Getter dell'attributo cd_tipo_unita
	 */
	public java.lang.String getCd_tipo_unita() {
		return cd_tipo_unita;
	}
	/*
	 * Getter dell'attributo cd_unita_padre
	 */
	public java.lang.String getCd_unita_padre() {
		return cd_unita_padre;
	}
	/*
	 * Getter dell'attributo ds_unita_organizzativa
	 */
	public java.lang.String getDs_unita_organizzativa() {
		return ds_unita_organizzativa;
	}
	/*
	 * Getter dell'attributo esercizio_fine
	 */
	public java.lang.Integer getEsercizio_fine() {
		return esercizio_fine;
	}
	/*
	 * Getter dell'attributo esercizio_inizio
	 */
	public java.lang.Integer getEsercizio_inizio() {
		return esercizio_inizio;
	}
	/*
	 * Getter dell'attributo fl_cds
	 */
	public java.lang.Boolean getFl_cds() {
		return fl_cds;
	}
	/*
	 * Getter dell'attributo fl_presidente_area
	 */
	public java.lang.Boolean getFl_presidente_area() {
		return fl_presidente_area;
	}
	/*
	 * Getter dell'attributo fl_rubrica
	 */
	public java.lang.Boolean getFl_rubrica() {
		return fl_rubrica;
	}
	/*
	 * Getter dell'attributo fl_uo_cds
	 */
	public java.lang.Boolean getFl_uo_cds() {
		return fl_uo_cds;
	}
	/*
	 * Getter dell'attributo livello
	 */
	public java.lang.Integer getLivello() {
		return livello;
	}
	/*
	 * Getter dell'attributo prc_copertura_obblig_2
	 */
	public java.math.BigDecimal getPrc_copertura_obblig_2() {
		return prc_copertura_obblig_2;
	}
	/*
	 * Getter dell'attributo prc_copertura_obblig_3
	 */
	public java.math.BigDecimal getPrc_copertura_obblig_3() {
		return prc_copertura_obblig_3;
	}
	/*
	 * Setter dell'attributo cd_area_ricerca
	 */
	public void setCd_area_ricerca(java.lang.String cd_area_ricerca) {
		this.cd_area_ricerca = cd_area_ricerca;
	}
	/*
	 * Setter dell'attributo cd_area_scientifica
	 */
	public void setCd_area_scientifica(java.lang.String cd_area_scientifica) {
		this.cd_area_scientifica = cd_area_scientifica;
	}
	/*
	 * Setter dell'attributo cd_proprio_unita
	 */
	public void setCd_proprio_unita(java.lang.String cd_proprio_unita) {
		this.cd_proprio_unita = cd_proprio_unita;
	}
	/*
	 * Setter dell'attributo cd_responsabile
	 */
	public void setCd_responsabile(java.lang.Integer cd_responsabile) {
		this.cd_responsabile = cd_responsabile;
	}
	/*
	 * Setter dell'attributo cd_responsabile_amm
	 */
	public void setCd_responsabile_amm(java.lang.Integer cd_responsabile_amm) {
		this.cd_responsabile_amm = cd_responsabile_amm;
	}
	/*
	 * Setter dell'attributo cd_tipo_unita
	 */
	public void setCd_tipo_unita(java.lang.String cd_tipo_unita) {
		this.cd_tipo_unita = cd_tipo_unita;
	}
	/*
	 * Setter dell'attributo cd_unita_padre
	 */
	public void setCd_unita_padre(java.lang.String cd_unita_padre) {
		this.cd_unita_padre = cd_unita_padre;
	}
	/*
	 * Setter dell'attributo ds_unita_organizzativa
	 */
	public void setDs_unita_organizzativa(java.lang.String ds_unita_organizzativa) {
		this.ds_unita_organizzativa = ds_unita_organizzativa;
	}
	/*
	 * Setter dell'attributo esercizio_fine
	 */
	public void setEsercizio_fine(java.lang.Integer esercizio_fine) {
		this.esercizio_fine = esercizio_fine;
	}
	/*
	 * Setter dell'attributo esercizio_inizio
	 */
	public void setEsercizio_inizio(java.lang.Integer esercizio_inizio) {
		this.esercizio_inizio = esercizio_inizio;
	}
	/*
	 * Setter dell'attributo fl_cds
	 */
	public void setFl_cds(java.lang.Boolean fl_cds) {
		this.fl_cds = fl_cds;
	}
	/*
	 * Setter dell'attributo fl_presidente_area
	 */
	public void setFl_presidente_area(java.lang.Boolean fl_presidente_area) {
		this.fl_presidente_area = fl_presidente_area;
	}
	/*
	 * Setter dell'attributo fl_rubrica
	 */
	public void setFl_rubrica(java.lang.Boolean fl_rubrica) {
		this.fl_rubrica = fl_rubrica;
	}
	/*
	 * Setter dell'attributo fl_uo_cds
	 */
	public void setFl_uo_cds(java.lang.Boolean fl_uo_cds) {
		this.fl_uo_cds = fl_uo_cds;
	}
	/*
	 * Setter dell'attributo livello
	 */
	public void setLivello(java.lang.Integer livello) {
		this.livello = livello;
	}
	/*
	 * Setter dell'attributo prc_copertura_obblig_2
	 */
	public void setPrc_copertura_obblig_2(java.math.BigDecimal prc_copertura_obblig_2) {
		this.prc_copertura_obblig_2 = prc_copertura_obblig_2;
	}
	/*
	 * Setter dell'attributo prc_copertura_obblig_3
	 */
	public void setPrc_copertura_obblig_3(java.math.BigDecimal prc_copertura_obblig_3) {
		this.prc_copertura_obblig_3 = prc_copertura_obblig_3;
	}
	public java.lang.String getId_funzione_pubblica() {
		return id_funzione_pubblica;
	}
	public void setId_funzione_pubblica(java.lang.String idFunzionePubblica) {
		id_funzione_pubblica = idFunzionePubblica;
	}

	public String getCodiceAooIpa() {
		return codiceAooIpa;
	}

	public void setCodiceAooIpa(String codiceAooIpa) {
		this.codiceAooIpa = codiceAooIpa;
	}
}
