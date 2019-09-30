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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

public interface StatoTrasmissione extends AllegatoParentBulk{
	public static String ALL = "ALL";
	public java.lang.String getStato_trasmissione();
	public void setStato_trasmissione(java.lang.String stato_trasmissione);
	public String getStorePath();
	public Integer getEsercizio();
	public String getCd_cds();
	public String getCd_unita_organizzativa();
	public Long getPg_documento_cont();
	public String getCd_tipo_documento_cont();
	public String getCMISName();
	public String getCMISFolderName();
}