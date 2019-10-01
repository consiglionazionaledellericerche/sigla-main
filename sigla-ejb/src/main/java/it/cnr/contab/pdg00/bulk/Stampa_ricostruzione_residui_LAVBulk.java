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

package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (23/06/2004 11.08.47)
 * @author: Gennaro Borriello
 */
public class Stampa_ricostruzione_residui_LAVBulk extends it.cnr.jada.bulk.OggettoBulk {

	//private it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttForPrint;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrUtente;

	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrForPrint;
	
	//	Il Progetto
	 private ProgettoBulk progettoForPrint = new ProgettoBulk();
	 //	Il Dipartimento
	 private DipartimentoBulk dipartimentoForPrint = new DipartimentoBulk();
	//.......private String cdsForPrint;
	
	//	Il Modulo
	 private ProgettoBulk moduloForPrint = new ProgettoBulk();
	 	
	private boolean cdsForPrintEnabled;

	private int livello_Responsabilita;
	
	// Livelli relativi al CdR utente
	public static final int LV_AC   = 0; // Amministazione centrale
	public static final int LV_CDRI = 1; // CDR I 00
	public static final int LV_RUO  = 2; // CDR II responsabile
	public static final int LV_NRUO = 3; // CDR II non responsabile

/**
 * Stampa_obbligazioni_LAVBulk constructor comment.
 */
public Stampa_ricostruzione_residui_LAVBulk() {
	super();
}

/**
 * @return
 */
public ProgettoBulk getProgettoForPrint() {
	return progettoForPrint;
}

/**
 * @param bulk
 */
public void setProgettoForPrint(ProgettoBulk bulk) {
	progettoForPrint = bulk;
}
/**
 * 
 * @return
 */
public String getCdProgettoForPrint() {
	if (getProgettoForPrint()==null)
		return "*";
	if (getProgettoForPrint().getCd_progetto()==null)
		return "*";
	return getProgettoForPrint().getCd_progetto().toString();
}
/**
 * 
 * @return
 */
public String getCdModuloForPrint() {
	if (getModuloForPrint()==null)
		return "*";
	if (getModuloForPrint().getCd_progetto()==null)
		return "*";
	return getModuloForPrint().getCd_progetto().toString();
}
/**
	 * @return
	 */
	public DipartimentoBulk getDipartimentoForPrint() {
		return dipartimentoForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setDipartimentoForPrint(DipartimentoBulk bulk) {
		dipartimentoForPrint = bulk;
	}
	/**
	 * 
	 * @return
	 */
	public String getCdDipartimentoForPrint() {
		if (getDipartimentoForPrint()==null)
			return "*";
		if (getDipartimentoForPrint().getCd_dipartimento()==null)
			return "*";
		return getDipartimentoForPrint().getCd_dipartimento().toString();
	}

	/**
	 * @return
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
		cdsForPrint = bulk;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.lang.String
	 */
	public String getCdCdsForPrint() {

		if (getCdsForPrint()==null)
			return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getCdsForPrint().getCd_unita_organizzativa().toString();
	}
	/**
	 * @param b
	 */
	public void setCdsForPrintEnabled(boolean b) {
		cdsForPrintEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isCdsForPrintEnabled() {
		return !cdsForPrintEnabled;
	}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdrNullableForPrint() {

	// Se non è stata specificato un  CdR
	if ( (getCdrForPrint()==null) ||
			(getCdrForPrint().getCd_centro_responsabilita()==null) ){


		if (LV_NRUO==getLivello_Responsabilita()){
			// Restituisco il CdR dell'Utente loggato
			return getCdrUtente().getCd_centro_responsabilita().toString();
		} else{
			return "*";
		}
				
	}

	return getCdrForPrint().getCd_centro_responsabilita().toString();

}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
--
public String getCdLANullableForPrint() {


	if (getLineaAttForPrint()==null)
		return "*";
	if (getLineaAttForPrint().getCd_linea_attivita()==null)
		return "*";

	return getLineaAttForPrint().getCd_linea_attivita().toString();
}
 */
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.43.40)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrForPrint() {
	return cdrForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 11.38.06)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrUtente() {
	return cdrUtente;
}
/**
 * Insert the method's description here.
 * Creation date: (30/06/2004 15.04.01)
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk

public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLineaAttForPrint() {
	return lineaAttForPrint;
}
 */
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 9.57.19)
 * @return int
 */
public int getLivello_Responsabilita() {
	return livello_Responsabilita;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdrForPrint() {
	return getCdrForPrint()==null || getCdrForPrint().getCrudStatus()==NORMAL;
}



/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */
public boolean isROCdsForPrint() {
	return getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
}

/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String

public boolean isROLineaAttForPrint() {

	return isROLineaAttForPrintSearchTool() || (getLineaAttForPrint()==null || getLineaAttForPrint().getCrudStatus()==NORMAL);
}
 */
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String

public boolean isROLineaAttForPrintSearchTool() {
	if (getCdrForPrint() == null || getCdrForPrint().getCd_centro_responsabilita() == null)
		return true;
	return false;
}
 */
/**
 * Insert the method's description here.
 * Creation date: (14/05/2003 9.43.40)
 * @param newCdrForPrint it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdrForPrint(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrForPrint) {
	cdrForPrint = newCdrForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 11.38.06)
 * @param newCdrUtente it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdrUtente(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrUtente) {
	cdrUtente = newCdrUtente;
}
/**
 * Insert the method's description here.
 * Creation date: (30/06/2004 15.04.01)
 * @param newLineaAttForPrint it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk

public void setLineaAttForPrint(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLineaAttForPrint) {
	lineaAttForPrint = newLineaAttForPrint;
}
 */
/**
 * Insert the method's description here.
 * Creation date: (19/05/2003 9.57.19)
 * @param newLivello_Responsabilita int
 */
public void setLivello_Responsabilita(int newLivello_Responsabilita) {
	livello_Responsabilita = newLivello_Responsabilita;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {

}


	/**
	 * @return
	 */
	public ProgettoBulk getModuloForPrint() {
		return moduloForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setModuloForPrint(ProgettoBulk bulk) {
		moduloForPrint = bulk;
	}

}
