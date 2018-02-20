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
