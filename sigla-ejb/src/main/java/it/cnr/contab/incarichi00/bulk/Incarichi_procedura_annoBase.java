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
public class Incarichi_procedura_annoBase extends Incarichi_procedura_annoKey implements Keyed {
//    IMPORTO_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_iniziale;
 
//    IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_complessivo;

	public Incarichi_procedura_annoBase() {
		super();
	}
	public Incarichi_procedura_annoBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Integer esercizio_limite) {
		super(esercizio, pg_repertorio, esercizio_limite);
	}
	public java.math.BigDecimal getImporto_iniziale() {
		return importo_iniziale;
	}
	public void setImporto_iniziale(java.math.BigDecimal importo_iniziale)  {
		this.importo_iniziale=importo_iniziale;
	}
	public java.math.BigDecimal getImporto_complessivo() {
		return importo_complessivo;
	}
	public void setImporto_complessivo(java.math.BigDecimal importo_complessivo)  {
		this.importo_complessivo=importo_complessivo;
	}
}