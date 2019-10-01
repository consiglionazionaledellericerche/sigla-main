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
 * Date 21/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Blt_regole_diariaBase extends Blt_regole_diariaKey implements Keyed {
//    GIORNO_MIN DECIMAL(3,0) NOT NULL
	private java.lang.Integer giornoMin;
 
//    GIORNO_MAX DECIMAL(3,0) NOT NULL
	private java.lang.Integer giornoMax;
 
//    GIORNI_DEF DECIMAL(3,0)
	private java.lang.Integer giorniDef;
 
//    FL_DIARIA CHAR(1) NOT NULL
	private java.lang.Boolean flDiaria;
 
//    FL_MENSILE CHAR(1) NOT NULL
	private java.lang.Boolean flMensile;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaBase() {
		super();
	}
	public Blt_regole_diariaBase(java.lang.String cdAccordo, java.lang.Integer pgRegola) {
		super(cdAccordo, pgRegola);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giornoMin]
	 **/
	public java.lang.Integer getGiornoMin() {
		return giornoMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giornoMin]
	 **/
	public void setGiornoMin(java.lang.Integer giornoMin)  {
		this.giornoMin=giornoMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giornoMax]
	 **/
	public java.lang.Integer getGiornoMax() {
		return giornoMax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giornoMax]
	 **/
	public void setGiornoMax(java.lang.Integer giornoMax)  {
		this.giornoMax=giornoMax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giorniDef]
	 **/
	public java.lang.Integer getGiorniDef() {
		return giorniDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giorniDef]
	 **/
	public void setGiorniDef(java.lang.Integer giorniDef)  {
		this.giorniDef=giorniDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flDiaria]
	 **/
	public java.lang.Boolean getFlDiaria() {
		return flDiaria;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flDiaria]
	 **/
	public void setFlDiaria(java.lang.Boolean flDiaria)  {
		this.flDiaria=flDiaria;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flMensile]
	 **/
	public java.lang.Boolean getFlMensile() {
		return flMensile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flMensile]
	 **/
	public void setFlMensile(java.lang.Boolean flMensile)  {
		this.flMensile=flMensile;
	}
}