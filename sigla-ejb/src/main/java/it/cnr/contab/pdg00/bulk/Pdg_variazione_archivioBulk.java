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
* Date 21/07/2005
*/
package it.cnr.contab.pdg00.bulk;
import java.sql.Timestamp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_variazione_archivioBulk extends Pdg_variazione_archivioBase {
	public static final java.util.Dictionary tipo_archivioKeys = new it.cnr.jada.util.OrderedHashtable();
	protected Pdg_variazioneBulk pdg_variazione;

	final public static String SITUAZIONE_ASSESTATO_ENTRATA = "E";
	final public static String SITUAZIONE_ASSESTATO_RICAVI = "R";
	final public static String SITUAZIONE_ASSESTATO_SPESA = "S";
	final public static String SITUAZIONE_ASSESTATO_COSTI = "C";
	final public static String DEFAULT_NOME_FILE = "Consultazione";	
	final public static String DEFAULT_ESTENSIONE = "xls";	
	
	static {
		tipo_archivioKeys.put(SITUAZIONE_ASSESTATO_ENTRATA,"Situazione Assestato Entrate");
		tipo_archivioKeys.put(SITUAZIONE_ASSESTATO_RICAVI,"Situazione Assestato Ricavi");
		tipo_archivioKeys.put(SITUAZIONE_ASSESTATO_SPESA,"Situazione Assestato Spese");
		tipo_archivioKeys.put(SITUAZIONE_ASSESTATO_COSTI,"Situazione Assestato Costi");
	}

	public Pdg_variazione_archivioBulk() {
		super();
	}

	public Pdg_variazione_archivioBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Long progressivo_riga) {
		super(esercizio, pg_variazione_pdg, progressivo_riga);
		pdg_variazione = new Pdg_variazioneBulk(esercizio, pg_variazione_pdg);
	}
	/**
	 * @return
	 */
	public Pdg_variazioneBulk getPdg_variazione() {
		return pdg_variazione;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_variazione(Pdg_variazioneBulk bulk) {
		pdg_variazione = bulk;
	}
	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return getPdg_variazione().getEsercizio();
	}
	/**
	 * @return
	 */
	public java.lang.Long getPg_variazione_pdg() {
		return getPdg_variazione().getPg_variazione_pdg();
	}
	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		getPdg_variazione().setEsercizio(integer);
	}

	/**
	 * @param long1
	 */
	public void setPg_variazione_pdg(java.lang.Long long1) {
		getPdg_variazione().setPg_variazione_pdg(long1);
	}
	/**
	 * @return
	 */
	public static java.util.Dictionary getTipo_archivioKeys() {
		return tipo_archivioKeys;
	}

	/**
	 * @return
	 */
	public String getNomeFile() {
		if (getDacr() != null || getTipo_archivio() != null) {
		  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");
		  return getTipo_archivioKeys().get(getTipo_archivio()) + " " + sdf.format(getDacr()) + "." + DEFAULT_ESTENSIONE; 
		}
		else
		  return DEFAULT_NOME_FILE + "." + DEFAULT_ESTENSIONE; 
	}

	public Timestamp getData_creazione()
	{
		return getDacr();
	}

}