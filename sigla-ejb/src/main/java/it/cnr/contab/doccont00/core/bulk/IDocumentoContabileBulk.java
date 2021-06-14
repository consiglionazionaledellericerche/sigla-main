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

package it.cnr.contab.doccont00.core.bulk;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;

/**
 * Insert the type's description here.
 * Creation date: (2/7/2002 3:24:48 PM)
 * @author: Simonetta Costa
 */
@JsonInclude(value=Include.NON_NULL)
public interface IDocumentoContabileBulk {
	public static final String TI_ENTRATA 	= "E";
	public static final String TI_SPESA 	= "S";	
	String getCd_cds();
	Integer getEsercizio();
	/**
	 * Insert the method's description here.
	 * Creation date: (2/7/2002 3:58:54 PM)
	 * @return java.lang.String
	 */
	String getManagerName();
	Integer getEsercizio_originale();
	Long getPg_doc_contabile();
	/**
	 * Insert the method's description here.
	 * Creation date: (27/02/2002 14.34.10)
	 * @return boolean
	 */
	Long getPg_ver_rec();
	public String getRiportato(); 
	public Map getSaldiInfo(); 
	String getTi_entrata_spesa();
	/**
	 * Insert the method's description here.
	 * Creation date: (27/02/2002 14.34.10)
	 * @return boolean
	 */
	boolean isInitialized();
	/**
	 * Insert the method's description here.
	 * Creation date: (27/02/2002 14.34.10)
	 * @return boolean
	 */
	boolean isTemporaneo();

	java.lang.String getTi_gestione();

	java.lang.String getTi_appartenenza();

	java.lang.String getCd_elemento_voce();
}
