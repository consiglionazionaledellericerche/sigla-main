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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_doc_contabiliBulk extends V_ass_doc_contabiliBase 
{
	protected IManRevBulk man_rev;

public V_ass_doc_contabiliBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_cdsPrintable'
 *
 * @return Il valore della proprietà 'cd_cdsPrintable'
 */
public java.lang.String getCd_cdsPrintable() 
{
	if ( man_rev == null )
		return null;
	if ( man_rev.getCd_cds().equals( getCd_cds()))
		return getCd_cds_coll();
	return getCd_cds();	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_tipo_documento_contPrintable'
 *
 * @return Il valore della proprietà 'cd_tipo_documento_contPrintable'
 */
public String getCd_tipo_documento_contPrintable() 
{
	if ( man_rev == null )
		return null;
	if ( man_rev.getCd_tipo_documento_cont().equals( getCd_tipo_documento_cont()))
		return getCd_tipo_documento_cont_coll();
	return getCd_tipo_documento_cont();	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizioPrintable'
 *
 * @return Il valore della proprietà 'esercizioPrintable'
 */
public Integer getEsercizioPrintable() 
{
	if ( man_rev == null )
		return null;
	if ( man_rev.getEsercizio().equals( getEsercizio()))
		return getEsercizio_coll();
	return getEsercizio();	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'man_rev'
 *
 * @return Il valore della proprietà 'man_rev'
 */
public IManRevBulk getMan_rev() {
	return man_rev;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_documento_contPrintable'
 *
 * @return Il valore della proprietà 'pg_documento_contPrintable'
 */
public Long getPg_documento_contPrintable() 
{
	if ( man_rev == null )
		return null;
	if ( man_rev.getPg_documento_cont().equals( getPg_documento_cont()))
		return getPg_documento_cont_coll();
	return getPg_documento_cont();	
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'man_rev'
 *
 * @param newMan_rev	Il valore da assegnare a 'man_rev'
 */
public void setMan_rev(IManRevBulk newMan_rev) {
	man_rev = newMan_rev;
}
}
