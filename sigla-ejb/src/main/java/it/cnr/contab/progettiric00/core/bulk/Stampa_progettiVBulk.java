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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

/**
 * @author marco spasiano
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Stampa_progettiVBulk extends ProgettoBulk {
    private Integer esercizio;
    private String cd_cds;
    private ProgettoBulk progettoForPrint;
	/**
	 * Constructor for Stampa_progettiVBulk.
	 */
	public Stampa_progettiVBulk() {
		super();
	}

	/**
	 * Returns the cd_cds.
	 * @return String
	 */
	public String getCd_cds() {
		return cd_cds;
	}

	/**
	 * Returns the esercizio.
	 * @return Integer
	 */
	public Integer getEsercizio() {
		return esercizio;
	}

	/**
	 * Returns the progettoForPrint.
	 * @return ProgettoBulk
	 */
	public ProgettoBulk getProgettoForPrint() {
		return progettoForPrint;
	}

	/**
	 * Sets the cd_cds.
	 * @param cd_cds The cd_cds to set
	 */
	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}

	/**
	 * Sets the esercizio.
	 * @param esercizio The esercizio to set
	 */
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	/**
	 * Sets the progettoForPrint.
	 * @param progettoForPrint The progettoForPrint to set
	 */
	public void setProgettoForPrint(ProgettoBulk progettoForPrint) {
		this.progettoForPrint = progettoForPrint;
	}
    public boolean isROProgettoForPrint(){
	   return progettoForPrint == null || progettoForPrint.getCrudStatus() == NORMAL;
    }
    public String getCdProgettoCRForPrint() {

      if (getProgettoForPrint() == null)
        return "*";
      if (getProgettoForPrint().getCd_progetto()== null)
          return "*";
      return getProgettoForPrint().getCd_progetto().toString();
    }	
    /**
     * Insert the method's description here.
     * Creation date: (13/02/2003 14.11.01)
     * @return java.lang.String
     */
    public Integer getTc() {
	    return new Integer(0);
    }

}
