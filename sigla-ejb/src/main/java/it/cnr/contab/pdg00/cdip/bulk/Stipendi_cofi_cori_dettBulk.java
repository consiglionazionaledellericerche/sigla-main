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
 * Date 27/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
public class Stipendi_cofi_cori_dettBulk extends Stipendi_cofi_cori_dettBase {
	public Stipendi_cofi_cori_dettBulk() {
		super();
	}
	private static java.util.Dictionary tipo_flussokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PRINCIPALE = "S";
	public static final String ANNULLI = "A";
	public static final String RIMBORSI = "R";
	public static final String IRAP = "I";
	public static final String FIGURATIVI = "F";
	public static final String ADDIZIONALI = "C";

	static {
		tipo_flussokeys.put(new String(PRINCIPALE),"Principale");
		tipo_flussokeys.put(new String(ANNULLI),"Annulli");
		tipo_flussokeys.put(new String(RIMBORSI),"Rimborsi");
		tipo_flussokeys.put(new String(IRAP),"Regionalizzazione IRAP");
		tipo_flussokeys.put(new String(FIGURATIVI),"Contributi Figurativi");
		tipo_flussokeys.put(new String(ADDIZIONALI),"Addizionali Comunali");
	}
	public java.util.Dictionary getTipo_flussoKeys() {
		return tipo_flussokeys;
	}
	
	private static java.util.Dictionary tipokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PERCIPIENTE = "P";
	public static final String ENTE = "E";
	
	static {
		tipo_flussokeys.put(new String(PERCIPIENTE),"Percipiente");
		tipo_flussokeys.put(new String(ENTE),"Ente");
	}
	public java.util.Dictionary getTipoKeys() {
		return tipokeys;
	}
}