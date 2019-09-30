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
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

public class Classificazione_voci_etr_liv6Bulk extends Classificazione_voci_etr_liv5Bulk {

	public Classificazione_voci_etr_liv6Bulk() {
		super();
	}
	public Classificazione_voci_etr_liv6Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_etr_liv6Bulk(String cd_livello1, String cd_livello2, String cd_livello3, String cd_livello4, String cd_livello5, String cd_livello6) {
		super(cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5);
		setCd_livello6(cd_livello6);
	}
}
