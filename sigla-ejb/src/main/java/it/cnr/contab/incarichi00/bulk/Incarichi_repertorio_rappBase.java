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

package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_repertorio_rappBase extends Incarichi_repertorio_rappKey implements Keyed {
 
	//  ANNO_COMPETENZA NUMBER(4) NOT NULL
	private java.lang.Integer anno_competenza;

	//  DT_DICHIARAZIONE TIMESTAMP(7)  NOT NULL
	private java.sql.Timestamp dt_dichiarazione;

	//  FL_ALTRI_RAPPORTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_altri_rapporti;

	public Incarichi_repertorio_rappBase() {
		super();
	}
	
	public Incarichi_repertorio_rappBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
	}
	
	public java.lang.Integer getAnno_competenza() {
		return anno_competenza;
	}
	public void setAnno_competenza(java.lang.Integer anno_competenza) {
		this.anno_competenza = anno_competenza;
	}

	public java.sql.Timestamp getDt_dichiarazione() {
		return dt_dichiarazione;
	}
	public void setDt_dichiarazione(java.sql.Timestamp dt_dichiarazione) {
		this.dt_dichiarazione = dt_dichiarazione;
	}
	public java.lang.Boolean getFl_altri_rapporti() {
		return fl_altri_rapporti;
	}
	public void setFl_altri_rapporti(java.lang.Boolean fl_altri_rapporti) {
		this.fl_altri_rapporti = fl_altri_rapporti;
	}
}