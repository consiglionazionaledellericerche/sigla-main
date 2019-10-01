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
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_procedura_noteBase extends Incarichi_procedura_noteKey implements Keyed {
	//  NOTA VARCHAR(2000)
	private java.lang.String nota;

	public Incarichi_procedura_noteBase() {
		super();
	}
	public Incarichi_procedura_noteBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long pg_nota) {
		super(esercizio, pg_repertorio, pg_nota);
	}
	public java.lang.String getNota() {
		return nota;
	}
	public void setNota(java.lang.String nota) {
		this.nota = nota;
	}
}