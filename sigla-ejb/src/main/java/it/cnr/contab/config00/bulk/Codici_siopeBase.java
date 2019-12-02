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
 * Date 23/04/2007
 */
package  it.cnr.contab.config00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Codici_siopeBase extends Codici_siopeKey implements Keyed {
//    DESCRIZIONE VARCHAR(200) NOT NULL
	private java.lang.String descrizione;
 

	public Codici_siopeBase() {
		super();
	}
	public Codici_siopeBase(java.lang.Integer esercizio, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(esercizio, ti_gestione, cd_siope);
	}
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	
	
}