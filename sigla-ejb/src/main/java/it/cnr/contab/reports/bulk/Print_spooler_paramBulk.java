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

package it.cnr.contab.reports.bulk;

public class Print_spooler_paramBulk extends Print_spooler_paramBase {

	private Print_spooler_paramKey key;

	public Print_spooler_paramBulk() {
		super();
		key = new Print_spooler_paramKey();
	}

	public Print_spooler_paramBulk(java.lang.String nome_param,
			java.lang.Long pg_stampa) {
		super(nome_param, pg_stampa);
		key = new Print_spooler_paramKey(nome_param, pg_stampa);
	}
	
	@Override
	public void setNomeParam(String nomeParam) {
		super.setNomeParam(nomeParam);
		key.setNomeParam(nomeParam);
	}
	
	@Override
	public void setPgStampa(Long pgStampa) {
		super.setPgStampa(pgStampa);
		key.setPgStampa(pgStampa);
	}

	@Override
	public Print_spooler_paramKey getKey() {
		return key;
	}
}