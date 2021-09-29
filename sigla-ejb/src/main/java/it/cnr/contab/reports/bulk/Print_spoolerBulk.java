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

import java.io.Serializable;
import java.text.Format;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import it.cnr.contab.logs.bulk.Batch_proceduraBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class Print_spoolerBulk extends Print_spoolerBase {
	public static final String STATO_IN_CODA = "C";
	public static final String STATO_IN_ESECUZIONE = "X";
	public static final String STATO_ERRORE = "E";
	public static final String STATO_ESEGUITA = "S";
	public static final String STATO_IN_CODA_WAITDS = "P";

	public static final String TI_VISIBILITA_PUBLICO = "P";
	public static final String TI_VISIBILITA_UTENTE = "U";
	public static final String TI_VISIBILITA_CDR = "C";
	public static final String TI_VISIBILITA_UNITA_ORGANIZZATIVA = "O";
	public static final String TI_VISIBILITA_CDS = "S";
	public static final String TI_VISIBILITA_CNR = "N";

	private static final java.util.Dictionary statoKeys;
	private static final java.util.Dictionary ti_visibilitaKeys;

	private BulkList params = new BulkList();
	
	static {
		statoKeys = new OrderedHashtable();
		statoKeys.put(STATO_IN_CODA, "In coda");
		statoKeys.put(STATO_IN_ESECUZIONE, "In esecuzione");
		statoKeys.put(STATO_ERRORE, "Errore");
		statoKeys.put(STATO_ESEGUITA, "Eseguita");
		statoKeys.put(STATO_IN_CODA_WAITDS, "In Coda");
		ti_visibilitaKeys = new OrderedHashtable();
		ti_visibilitaKeys.put(TI_VISIBILITA_UTENTE, "Utente");
		ti_visibilitaKeys.put(TI_VISIBILITA_CDR, "Cdr");
		ti_visibilitaKeys.put(TI_VISIBILITA_UNITA_ORGANIZZATIVA, "Unità org.");
		ti_visibilitaKeys.put(TI_VISIBILITA_CDS, "Cds");
		ti_visibilitaKeys.put(TI_VISIBILITA_CNR, "Ente");
		ti_visibilitaKeys.put(TI_VISIBILITA_PUBLICO, "Pubblico");
	}
	public static final String TIPO_INTERVALLO_GIORNI = "G";
	public static final String TIPO_INTERVALLO_SETTIMANE = "S";
	public static final String TIPO_INTERVALLO_MESI = "M";
	public static final Hashtable tipo_intervalloKeys;

	static {
		tipo_intervalloKeys = new Hashtable();
		tipo_intervalloKeys.put(TIPO_INTERVALLO_GIORNI, "giorni");
		tipo_intervalloKeys.put(TIPO_INTERVALLO_SETTIMANE, "settimane");
		tipo_intervalloKeys.put(TIPO_INTERVALLO_MESI, "mesi");
	}

	public Print_spoolerBulk() {
		super();
	}

	public Print_spoolerBulk(java.lang.Long pg_stampa) {
		super(pg_stampa);
	}

	public BulkList getParams() {
		return params;
	}

	public void setParams(BulkList params) {
		this.params = params;
	}

	/**
	 * Insert the method's description here. Creation date: (12/04/2002
	 * 11:10:23)
	 * 
	 * @return java.util.Dictionary
	 */
	public java.util.Dictionary getStatoKeys() {
		return statoKeys;
	}

	/**
	 * Insert the method's description here. Creation date: (24/05/2002
	 * 14:49:33)
	 * 
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_visibilitaKeys() {
		return ti_visibilitaKeys;
	}

	public boolean isEseguita() {
		return STATO_ESEGUITA.equalsIgnoreCase(getStato());
	}

	public java.util.Dictionary getTipo_intervalloKeys() {
		return tipo_intervalloKeys;
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,
			it.cnr.jada.action.ActionContext context) {
		setTiIntervallo(TIPO_INTERVALLO_GIORNI);
		return super.initializeForInsert(bp, context);
	}

	public void validate() throws ValidationException {
		if (getIntervallo() != null && getTiIntervallo() == null)
			throw new ValidationException(
					"Se si specifica l'intervallo \350 necessario specificarne l'unit\340.");
		if (getIntervallo() == null && getTiIntervallo() != null)
			throw new ValidationException(
					"Se si specifica l'unit\340 di intervallo \350 necessario specificare l'intervallo.");
		if (getIntervallo() != null && getDtPartenza() == null)
			throw new ValidationException(
					"Se si specifica l'intervallo \350 necessario specificare l'ora di partenza.");
		if (getIntervallo() != null && getIntervallo().longValue() == 0L)
			throw new ValidationException("Intervallo non valido");
		if (getIntervallo() == null && getDtPartenza() != null)
			throw new ValidationException(
					"Se si specifica l'ora di partenza \350 necessario specificare l'intervallo.");
		if (getDtPartenza() != null
				&& getDtPartenza().before(
						EJBCommonServices.getServerTimestamp()))
			throw new ValidationException(
					"Se si specifica l'ora di partenza \350 necessario specificare una data e/o un'ora futura.");
		else
			return;
	}
	
	public void addParam(Print_spooler_paramBulk param){
		params.add(param);
	}

	public void addParam(String paramName, Date valoreParam, Class<Date> paramType, Format format){
		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam(paramName);
		param.setValoreParam(format.format(valoreParam));
		param.setParamType(paramType.getName());
		params.add(param);
	}
	
	public <T extends Serializable> void addParam(String paramName, T valoreParam, Class<T> paramType){
		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam(paramName);
		param.setValoreParam(String.valueOf(valoreParam));
		param.setParamType(paramType.getName());
		params.add(param);
	}
	
}
