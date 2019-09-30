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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;

/**
 * Insert the type's description here.
 * Creation date: (25/08/2004 13.27.53)
 * @author: Gennaro Borriello
 */
public class Stampa_docamm_per_voce_del_pianoVBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	// Esercizio di scrivania
	private Integer esercizio;

	// Il Cds di scrivania
	private String cd_cds;
	
	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	private boolean uOForPrintEnabled;

	// Voce del Piano 
	private Elemento_voceBulk vocedpForPrint;
	private boolean VocedPForPrintEnabled;
	
	private TerzoBulk terzo;
	private boolean cdTerzoForPrintEnabled;
	
	/*private String stato;
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	
	
	final public static String STATO_I = "I";
	final public static String STATO_C = "C";
	final public static String STATO_P = "P";
	final public static String STATO_A = "A";
	final public static String STATO_TUTTI = "*";
	
			
		static {
				ti_statoKeys.put(STATO_I,"Iniziale");
				ti_statoKeys.put(STATO_C,"Contabilizzato");
				ti_statoKeys.put(STATO_P,"Pagata");
				ti_statoKeys.put(STATO_A,"Annullata");
				ti_statoKeys.put(STATO_TUTTI,"Tutti");
		}*/
/**
 * Stampa_docamm_per_voce_del_pianoVBulk constructor comment.
 */
public Stampa_docamm_per_voce_del_pianoVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}

/*public final java.util.Dictionary getti_statoKeys() {
			return ti_statoKeys;
	}*/

	public Integer getCdTerzoForPrint() {

		if (getTerzo()==null)
			return new Integer(0);
		if (getTerzo().getCd_terzo()==null)
			return new Integer(0);

		return getTerzo().getCd_terzo();
	}

	/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.lang.String
 */
public String getCdUOCRForPrint() {

	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.lang.String
 */

public String getVoceDPForPrint() {

	if (getVocedpForPrint()==null)
		return "*";
	if (getVocedpForPrint().getCd_elemento_voce()==null)
		return "*";

	return getVocedpForPrint().getCd_elemento_voce();
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @return java.lang.String
 */
public TerzoBulk getTerzo() {
	return terzo;
}

public Elemento_voceBulk getVocedpForPrint(){
	return vocedpForPrint;
}
	
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */

public boolean isVocedPForPrintEnabled() {
	return VocedPForPrintEnabled;
}

public boolean isCdTerzoForPrintEnabled() {
	return cdTerzoForPrintEnabled;
}

public boolean isROTerzo() {
	return getTerzo()==null || getTerzo().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return uOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}

public void setVocedpForPrint(Elemento_voceBulk newVocedpForPrint) {
	vocedpForPrint = newVocedpForPrint;
}

public void setTerzo(TerzoBulk newTerzo) {
	terzo = newTerzo;
}	
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @param newUOForPrintEnabled boolean
 */
public void setUOForPrintEnabled(boolean newUOForPrintEnabled) {
	uOForPrintEnabled = newUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (25/08/2004 13.29.56)
 * @param newVoce_del_piano java.lang.String
 */

public void setVocedPForPrintEnabled(boolean newVocedPForPrintEnabled) {
	VocedPForPrintEnabled = newVocedPForPrintEnabled;
}

public void setCdTerzoForPrintEnabled(boolean newCdterzoForPrintEnabled) {
	cdTerzoForPrintEnabled = newCdterzoForPrintEnabled;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
/*public String getstato() {
			return stato;
	}
	
	
	
	public void setstato(String string) {
			stato = string;
	}
	*/
}
