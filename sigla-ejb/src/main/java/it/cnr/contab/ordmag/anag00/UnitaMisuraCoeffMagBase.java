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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class UnitaMisuraCoeffMagBase extends UnitaMisuraCoeffMagKey implements Keyed {
//    CD_UNITA_MISURA_CAR VARCHAR(10)
	private java.lang.String cdUnitaMisuraCar;
 
//    COEF_CONV_CAR DECIMAL(12,5)
	private java.math.BigDecimal coefConvCar;
 
//    CD_UNITA_MISURA_SCA VARCHAR(10)
	private java.lang.String cdUnitaMisuraSca;
 
//    COEF_CONV_SCA DECIMAL(12,5)
	private java.math.BigDecimal coefConvSca;
 
//    DT_FIN_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dtFinValidita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_MISURA_COEFF_MAG
	 **/
	public UnitaMisuraCoeffMagBase() {
		super();
	}
	public UnitaMisuraCoeffMagBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdBeneServizio, java.sql.Timestamp dtIniValidita) {
		super(cdCds, cdMagazzino, cdBeneServizio, dtIniValidita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisuraCar]
	 **/
	public java.lang.String getCdUnitaMisuraCar() {
		return cdUnitaMisuraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisuraCar]
	 **/
	public void setCdUnitaMisuraCar(java.lang.String cdUnitaMisuraCar)  {
		this.cdUnitaMisuraCar=cdUnitaMisuraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coefConvCar]
	 **/
	public java.math.BigDecimal getCoefConvCar() {
		return coefConvCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coefConvCar]
	 **/
	public void setCoefConvCar(java.math.BigDecimal coefConvCar)  {
		this.coefConvCar=coefConvCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisuraSca]
	 **/
	public java.lang.String getCdUnitaMisuraSca() {
		return cdUnitaMisuraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisuraSca]
	 **/
	public void setCdUnitaMisuraSca(java.lang.String cdUnitaMisuraSca)  {
		this.cdUnitaMisuraSca=cdUnitaMisuraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coefConvSca]
	 **/
	public java.math.BigDecimal getCoefConvSca() {
		return coefConvSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coefConvSca]
	 **/
	public void setCoefConvSca(java.math.BigDecimal coefConvSca)  {
		this.coefConvSca=coefConvSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFinValidita]
	 **/
	public java.sql.Timestamp getDtFinValidita() {
		return dtFinValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFinValidita]
	 **/
	public void setDtFinValidita(java.sql.Timestamp dtFinValidita)  {
		this.dtFinValidita=dtFinValidita;
	}
}