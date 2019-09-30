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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_saldi_cdr_lineaKey extends OggettoBulk implements KeyedPersistent {

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_RES DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_res;
	
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;
	
	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

	// CD_VOCE VARCHAR(50) NOT NULL (PK)
	private java.lang.String cd_voce;

public Voce_f_saldi_cdr_lineaKey() {
	super();
}
public Voce_f_saldi_cdr_lineaKey(java.lang.Integer esercizio,java.lang.Integer esercizio_res,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String cd_voce) {
	super();
	this.esercizio = esercizio;
	this.esercizio_res = esercizio_res;
	this.cd_centro_responsabilita = cd_centro_responsabilita;	
	this.cd_linea_attivita = cd_linea_attivita;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
	this.cd_voce = cd_voce;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Voce_f_saldi_cdr_lineaKey)) return false;
	Voce_f_saldi_cdr_lineaKey k = (Voce_f_saldi_cdr_lineaKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_res(),k.getEsercizio_res())) return false;			
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	if(!compareKey(getCd_voce(),k.getCd_voce())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_res())+
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_linea_attivita())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione()) +
	    calculateKeyHashCode(getCd_voce());
}

	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_voce() {
		return cd_voce;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}

	/**
	 * @return
	 */
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}

	/**
	 * @return
	 */
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String string) {
		cd_centro_responsabilita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_linea_attivita(java.lang.String string) {
		cd_linea_attivita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_voce(java.lang.String string) {
		cd_voce = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio_res(java.lang.Integer integer) {
		esercizio_res = integer;
	}

	/**
	 * @param string
	 */
	public void setTi_appartenenza(java.lang.String string) {
		ti_appartenenza = string;
	}

	/**
	 * @param string
	 */
	public void setTi_gestione(java.lang.String string) {
		ti_gestione = string;
	}

}
