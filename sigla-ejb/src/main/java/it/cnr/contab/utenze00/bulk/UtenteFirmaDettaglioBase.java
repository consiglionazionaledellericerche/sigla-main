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
 * Date 11/12/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;

import java.util.List;
public class UtenteFirmaDettaglioBase extends UtenteFirmaDettaglioKey implements Keyed {
//    DA_DATA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp daData;
 
//    A_DATA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp a_data;
 
//    FUNZIONI_ABILITATE VARCHAR(2000)
	private List<java.lang.String> funzioniAbilitate;
 
//    TITOLO_FIRMA VARCHAR(250)
	private java.lang.String titoloFirma;
 
//    DELEGATO_DA VARCHAR(20)
	private java.lang.String delegatoDa;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UTENTE_FIRMA_DETTAGLIO
	 **/
	public UtenteFirmaDettaglioBase() {
		super();
	}
	public UtenteFirmaDettaglioBase(java.lang.String cdUtente, java.lang.String cdUnitaOrganizzativa) {
		super(cdUtente, cdUnitaOrganizzativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.sql.Timestamp getDaData() {
		return daData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setDaData(java.sql.Timestamp daData)  {
		this.daData=daData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.sql.Timestamp getA_data() {
		return a_data;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setA_data(java.sql.Timestamp a_data)  {
		this.a_data=a_data;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public List<java.lang.String> getFunzioniAbilitate() {
		return funzioniAbilitate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setFunzioniAbilitate(List<java.lang.String> funzioniAbilitate)  {
		this.funzioniAbilitate=funzioniAbilitate;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.lang.String getTitoloFirma() {
		return titoloFirma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setTitoloFirma(java.lang.String titoloFirma)  {
		this.titoloFirma=titoloFirma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.lang.String getDelegatoDa() {
		return delegatoDa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setDelegatoDa(java.lang.String delegatoDa)  {
		this.delegatoDa=delegatoDa;
	}
}