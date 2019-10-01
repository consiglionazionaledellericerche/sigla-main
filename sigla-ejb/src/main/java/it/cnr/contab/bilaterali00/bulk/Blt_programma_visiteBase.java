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
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Blt_programma_visiteBase extends Blt_programma_visiteKey implements Keyed {
//    ANNO_VISITA DECIMAL(4,0) NOT NULL
	private java.lang.Integer annoVisita;
	
//	  FL_STRANIERO          CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean flStraniero;

//    NUM_VISITE_AUTORIZZATE DECIMAL(3,0) NOT NULL
	private java.lang.Integer numVisiteAutorizzate;
 
//    NUM_VISITE_UTILIZZATE DECIMAL(3,0) NOT NULL
	private java.lang.Integer numVisiteUtilizzate;

//    NUM_MAX_GG_VISITA DECIMAL(3,0) NOT NULL
	private java.lang.Integer numMaxGgVisita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_programma_visiteBase() {
		super();
	}
	public Blt_programma_visiteBase(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Long pgRecord) {
		super(cdAccordo, cdProgetto, pgRecord);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [annoVisita]
	 **/
	public java.lang.Integer getAnnoVisita() {
		return annoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [annoVisita]
	 **/
	public void setAnnoVisita(java.lang.Integer annoVisita)  {
		this.annoVisita=annoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numVisiteAutorizzate]
	 **/
	public java.lang.Integer getNumVisiteAutorizzate() {
		return numVisiteAutorizzate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numVisiteAutorizzate]
	 **/
	public void setNumVisiteAutorizzate(java.lang.Integer numVisiteAutorizzate)  {
		this.numVisiteAutorizzate=numVisiteAutorizzate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numVisiteUtilizzate]
	 **/
	public java.lang.Integer getNumVisiteUtilizzate() {
		return numVisiteUtilizzate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numVisiteUtilizzate]
	 **/
	public void setNumVisiteUtilizzate(java.lang.Integer numVisiteUtilizzate)  {
		this.numVisiteUtilizzate=numVisiteUtilizzate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flStraniero]
	 **/
	public java.lang.Boolean getFlStraniero() {
		return flStraniero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flStraniero]
	 **/
	public void setFlStraniero(java.lang.Boolean flStraniero) {
		this.flStraniero = flStraniero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numMaxGgVisita]
	 **/
	public java.lang.Integer getNumMaxGgVisita() {
		return numMaxGgVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numMaxGgVisita]
	 **/
	public void setNumMaxGgVisita(java.lang.Integer numMaxGgVisita)  {
		this.numMaxGgVisita=numMaxGgVisita;
	}
}