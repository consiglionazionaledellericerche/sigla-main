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
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_tipo_ruolo_privilegioBulk extends Ass_tipo_ruolo_privilegioBase {
	private Tipo_ruoloBulk tipo_ruolo;
	private PrivilegioBulk privilegio;

	public Ass_tipo_ruolo_privilegioBulk() {
		super();
	}
	public Ass_tipo_ruolo_privilegioBulk(java.lang.String tipo, java.lang.String cd_privilegio) {
		super(tipo, cd_privilegio);
	}

	public PrivilegioBulk getPrivilegio() {
		return privilegio;
	}
	public void setPrivilegio(PrivilegioBulk privilegio) {
		this.privilegio = privilegio;
	}
	public Tipo_ruoloBulk getTipo_ruolo() {
		return tipo_ruolo;
	}
	public void setTipo_ruolo(Tipo_ruoloBulk tipo_ruolo) {
		this.tipo_ruolo = tipo_ruolo;
	}
	public String getCd_privilegio() {
		if (getPrivilegio()==null) return null;
		return getPrivilegio().getCd_privilegio();
	}
	public void setCd_privilegio(String cd_privilegio) {
		if (getPrivilegio()!=null) 
			getPrivilegio().setCd_privilegio(cd_privilegio);
	}
	public String getTipo() {
		if (getTipo_ruolo()==null) return null;
		return getTipo_ruolo().getTipo();
	}
	public void setTipo(String tipo) {
		if (getTipo_ruolo()!=null) 
			getTipo_ruolo().setTipo(tipo);
	}
}