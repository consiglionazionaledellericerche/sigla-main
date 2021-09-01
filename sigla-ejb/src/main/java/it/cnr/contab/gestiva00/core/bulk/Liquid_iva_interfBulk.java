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

package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.OrderedHashtable;

public class Liquid_iva_interfBulk extends Liquid_iva_interfBase{

	private boolean visualizzaDati = false;
	
	public static java.util.Dictionary MESI;
	public static java.util.Dictionary MESI_INT;
	public static java.util.Dictionary INT_MESI;
	
	private String mese= null;
	
	public static final String GENNAIO= "Gennaio";
	public static final String FEBBRAIO= "Febbraio";
	public static final String MARZO= "Marzo";
	public static final String APRILE= "Aprile";
	public static final String MAGGIO= "Maggio";
	public static final String GIUGNO= "Giugno";
	public static final String LUGLIO= "Luglio";
	public static final String AGOSTO= "Agosto";
	public static final String SETTEMBRE= "Settembre";
	public static final String OTTOBRE= "Ottobre";
	public static final String NOVEMBRE= "Novembre";
	public static final String DICEMBRE= "Dicembre";
	
	public static java.util.Dictionary TIPI_LIQ;
		
	public static final String SANMARINO = "S";

	
	static {
		initializeHashes();
	}

public Liquid_iva_interfBulk() {
	super();
}

public Liquid_iva_interfBulk(java.lang.Integer pg_caricamento,java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.String ti_liquidazione) {
	super(pg_caricamento,cd_cds,esercizio,cd_unita_organizzativa,dt_fine,dt_inizio,ti_liquidazione);
}

public boolean isVisualizzaDati() {
	return visualizzaDati;
}

public void setVisualizzaDati(boolean newVisualizzaDati) {
	visualizzaDati = newVisualizzaDati;
}

public void validate() throws ValidationException {

	super.validate();
	
	if (getNote() != null && getNote().length() > 1000)
		throw new ValidationException("Il campo \"note\" è troppo lungo: il massimo consentito è di 1000 caratteri!");
}

public java.util.Dictionary getInt_mesi() {

	return INT_MESI;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 16.43.24)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getMese() {
	return mese;
}

/**
 * Insert the method's description here.
 * Creation date: (21/06/2002 9.56.57)
 * @author: Alfonso Ardire
 * @return java.util.Dictionary
 */
public java.util.Dictionary getMesi() {

	return MESI;
}
public java.util.Dictionary getMesi_int() {

	return MESI_INT;
}

protected static void initializeHashes() {

	MESI= new OrderedHashtable();
	MESI.put(GENNAIO, GENNAIO);
	MESI.put(FEBBRAIO, FEBBRAIO);
	MESI.put(MARZO, MARZO);
	MESI.put(APRILE, APRILE);
	MESI.put(MAGGIO, MAGGIO);
	MESI.put(GIUGNO, GIUGNO);
	MESI.put(LUGLIO, LUGLIO);
	MESI.put(AGOSTO, AGOSTO);
	MESI.put(SETTEMBRE, SETTEMBRE);
	MESI.put(OTTOBRE, OTTOBRE);
	MESI.put(NOVEMBRE, NOVEMBRE);
	MESI.put(DICEMBRE, DICEMBRE);

	MESI_INT= new OrderedHashtable();
	MESI_INT.put(GENNAIO, new Integer(1));
	MESI_INT.put(FEBBRAIO, new Integer(2));
	MESI_INT.put(MARZO, new Integer(3));
	MESI_INT.put(APRILE, new Integer(4));
	MESI_INT.put(MAGGIO, new Integer(5));
	MESI_INT.put(GIUGNO, new Integer(6));
	MESI_INT.put(LUGLIO, new Integer(7));
	MESI_INT.put(AGOSTO, new Integer(8));
	MESI_INT.put(SETTEMBRE, new Integer(9));
	MESI_INT.put(OTTOBRE, new Integer(10));
	MESI_INT.put(NOVEMBRE, new Integer(11));
	MESI_INT.put(DICEMBRE, new Integer(12));

	INT_MESI= new OrderedHashtable();
	INT_MESI.put(new Integer(1), GENNAIO);
	INT_MESI.put(new Integer(2), FEBBRAIO);
	INT_MESI.put(new Integer(3), MARZO);
	INT_MESI.put(new Integer(4), APRILE);
	INT_MESI.put(new Integer(5), MAGGIO);
	INT_MESI.put(new Integer(6), GIUGNO);
	INT_MESI.put(new Integer(7), LUGLIO);
	INT_MESI.put(new Integer(8), AGOSTO);
	INT_MESI.put(new Integer(9), SETTEMBRE);
	INT_MESI.put(new Integer(10), OTTOBRE);
	INT_MESI.put(new Integer(11), NOVEMBRE);
	INT_MESI.put(new Integer(12), DICEMBRE);

	TIPI_LIQ= new OrderedHashtable();
	for (TipoIVA tipoIVA : TipoIVA.values()) {
		TIPI_LIQ.put(tipoIVA.value(), tipoIVA.label());
	}
	TIPI_LIQ.put(SANMARINO, "San Marino");
}
	public void setMese(java.lang.String newMese) {
		mese = newMese;
	}
	/**
	 * @return
	 */
	public static java.util.Dictionary getMESI() {
		return MESI;
	}

	/**
	 * @param dictionary
	 */
	public static void setMESI(java.util.Dictionary dictionary) {
		MESI = dictionary;
	}	
	/**
	 * @return
	 */
	public static java.util.Dictionary getTIPI_LIQ() {
		return TIPI_LIQ;
	}

	/**
	 * @param dictionary
	 */
	public static void setTIPI_LIQ(java.util.Dictionary dictionary) {
		TIPI_LIQ = dictionary;
	}

}