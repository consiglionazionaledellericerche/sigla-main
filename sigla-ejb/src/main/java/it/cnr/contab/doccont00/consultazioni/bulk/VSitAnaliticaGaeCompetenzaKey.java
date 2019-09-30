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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 18/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VSitAnaliticaGaeCompetenzaKey extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SIT_ANALITICA_GAE_COMPETENZA
	 **/
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cdCentroResponsabilita;
	
	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cdLineaAttivita;

	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String tiAppartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String tiGestione;

	// CD_VOCE VARCHAR(50) NOT NULL (PK)
	private java.lang.String cdVoce;

public VSitAnaliticaGaeCompetenzaKey() {
	super();
}
public VSitAnaliticaGaeCompetenzaKey(java.lang.Integer esercizio,java.lang.String cdCentroResponsabilita,java.lang.String cdLineaAttivita,java.lang.String tiAppartenenza,java.lang.String tiGestione,java.lang.String cdVoce) {
	super();
	this.esercizio = esercizio;
	this.cdCentroResponsabilita = cdCentroResponsabilita;	
	this.cdLineaAttivita = cdLineaAttivita;
	this.tiAppartenenza = tiAppartenenza;
	this.tiGestione = tiGestione;
	this.cdVoce = cdVoce;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof VSitAnaliticaGaeCompetenzaKey)) return false;
	VSitAnaliticaGaeCompetenzaKey k = (VSitAnaliticaGaeCompetenzaKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getCdCentroResponsabilita(),k.getCdCentroResponsabilita())) return false;
	if(!compareKey(getCdLineaAttivita(),k.getCdLineaAttivita())) return false;
	if(!compareKey(getTiAppartenenza(),k.getTiAppartenenza())) return false;
	if(!compareKey(getTiGestione(),k.getTiGestione())) return false;
	if(!compareKey(getCdVoce(),k.getCdVoce())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getCdCentroResponsabilita())+
		calculateKeyHashCode(getCdLineaAttivita())+
		calculateKeyHashCode(getTiAppartenenza())+
		calculateKeyHashCode(getTiGestione()) +
	    calculateKeyHashCode(getCdVoce());
}

	/**
	 * @return
	 */
	public java.lang.String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCdLineaAttivita() {
		return cdLineaAttivita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCdVoce() {
		return cdVoce;
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
	public java.lang.String getTiAppartenenza() {
		return tiAppartenenza;
	}

	/**
	 * @return
	 */
	public java.lang.String getTiGestione() {
		return tiGestione;
	}

	/**
	 * @param string
	 */
	public void setCdCentroResponsabilita(java.lang.String string) {
		cdCentroResponsabilita = string;
	}

	/**
	 * @param string
	 */
	public void setCdLineaAttivita(java.lang.String string) {
		cdLineaAttivita = string;
	}

	/**
	 * @param string
	 */
	public void setCdVoce(java.lang.String string) {
		cdVoce = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @param string
	 */
	public void setTiAppartenenza(java.lang.String string) {
		tiAppartenenza = string;
	}

	/**
	 * @param string
	 */
	public void setTiGestione(java.lang.String string) {
		tiGestione = string;
	}

}