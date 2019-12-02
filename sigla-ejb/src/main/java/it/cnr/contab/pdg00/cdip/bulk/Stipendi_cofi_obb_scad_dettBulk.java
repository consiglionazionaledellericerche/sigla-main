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
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
public class Stipendi_cofi_obb_scad_dettBulk extends Stipendi_cofi_obb_scad_dettBase {
	public Stipendi_cofi_obb_scad_dettBulk() {
		super();
	}
	public Stipendi_cofi_obb_scad_dettBulk(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.String tipo_flusso, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Long pg_obbligazione, java.lang.Integer esercizio_ori_obbligazione) {
		super(esercizio, mese, tipo_flusso, cd_cds_obbligazione, esercizio_obbligazione, pg_obbligazione, esercizio_ori_obbligazione);
	}
	private static java.util.Dictionary tipo_flussokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PRINCIPALE = "S";
	public static final String ANNULLI = "A";
	public static final String RIMBORSI = "R";
	public static final String IRAP = "I";
	public static final String FIGURATIVI = "F";

	static {
		tipo_flussokeys.put(new String(PRINCIPALE),"Principale");
		tipo_flussokeys.put(new String(ANNULLI),"Annulli");
		tipo_flussokeys.put(new String(RIMBORSI),"Rimborsi");
		tipo_flussokeys.put(new String(IRAP),"Regionalizzazione IRAP");
		tipo_flussokeys.put(new String(FIGURATIVI),"Contributi Figurativi");
	}
	public java.util.Dictionary getTipo_flussoKeys() {
		return tipo_flussokeys;
	}
}