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
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;

public class Classificazione_voci_etrBulk extends Classificazione_vociBulk {
	public Classificazione_voci_etrBulk() {
		super();
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
	public Classificazione_voci_etrBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
	public Classificazione_voci_etrBulk(Classificazione_voci_etrBulk liv_pre, String cd_livello) {
		super(liv_pre, cd_livello);
		setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	}
}