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
* Date 16/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_Modulo_EntrateKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_centro_responsabilita;
	private java.lang.Integer pg_progetto;
	private java.lang.String cd_natura;
	private java.lang.Integer id_classificazione;
	private java.lang.Long pg_dettaglio;
	private java.lang.String cd_cds_area;
  	
	public Pdg_Modulo_EntrateKey() {
		super();
	}
	public Pdg_Modulo_EntrateKey(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.String cd_natura, java.lang.Integer id_classificazione, java.lang.Long pg_dettaglio,java.lang.String cd_cds_area) {
		super();
		this.esercizio=esercizio;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
		this.pg_progetto=pg_progetto;
		this.cd_natura=cd_natura;
		this.id_classificazione=id_classificazione;
		this.pg_dettaglio=pg_dettaglio;
		this.cd_cds_area=cd_cds_area;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_Modulo_EntrateKey)) return false;
		Pdg_Modulo_EntrateKey k = (Pdg_Modulo_EntrateKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		if (!compareKey(getPg_progetto(), k.getPg_progetto())) return false;
		if (!compareKey(getCd_natura(), k.getCd_natura())) return false;
		if (!compareKey(getId_classificazione(), k.getId_classificazione())) return false;
		if (!compareKey(getPg_dettaglio(), k.getPg_dettaglio())) return false;
		if (!compareKey(getCd_cds_area(), k.getCd_cds_area())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
		i = i + calculateKeyHashCode(getPg_progetto());
		i = i + calculateKeyHashCode(getCd_natura());
		i = i + calculateKeyHashCode(getId_classificazione());
		i = i + calculateKeyHashCode(getPg_dettaglio());
		i = i + calculateKeyHashCode(getCd_cds_area());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setPg_progetto(java.lang.Integer pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.Integer getPg_progetto () {
		return pg_progetto;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
	public void setPg_dettaglio(java.lang.Long pg_dettaglio)  {
		this.pg_dettaglio=pg_dettaglio;
	}
	public java.lang.Long getPg_dettaglio () {
		return pg_dettaglio;
	}
	public java.lang.String getCd_cds_area() {
		return cd_cds_area;
		}
	public void setCd_cds_area(java.lang.String string) {
		cd_cds_area = string;
	}
}