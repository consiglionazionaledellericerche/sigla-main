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

package it.cnr.contab.config00.pdcfin.bulk;

public interface IVoceBilancioBulk {
	public java.lang.Integer getEsercizio();
	
	public java.lang.String getTi_appartenenza();

	public java.lang.String getTi_gestione();

	public java.lang.String getCd_elemento_voce();

	public java.lang.String getCd_voce();
	
	public java.lang.String getCd_titolo_capitolo();
	
	public java.lang.String getCd_funzione();
}
