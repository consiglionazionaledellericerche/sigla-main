package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_disp_cassa_cdsBulk extends V_disp_cassa_cdsBase {
	protected java.math.BigDecimal im_obbligazioni;
	protected java.math.BigDecimal im_da_trasferire;	

public V_disp_cassa_cdsBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @return 
 */
public CdsBulk asCds()
{
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	cds.setCd_unita_organizzativa( getCd_cds());
	return cds;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_da_trasferire'
 *
 * @return Il valore della proprietà 'im_da_trasferire'
 */
public java.math.BigDecimal getIm_da_trasferire() {
	return im_da_trasferire;
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_obbligazioni'
 *
 * @return Il valore della proprietà 'im_obbligazioni'
 */
public java.math.BigDecimal getIm_obbligazioni() {
	return im_obbligazioni;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_da_trasferire'
 *
 * @param newIm_da_trasferire	Il valore da assegnare a 'im_da_trasferire'
 */
public void setIm_da_trasferire(java.math.BigDecimal newIm_da_trasferire) {
	im_da_trasferire = newIm_da_trasferire;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_obbligazioni'
 *
 * @param newIm_obbligazioni	Il valore da assegnare a 'im_obbligazioni'
 */
public void setIm_obbligazioni(java.math.BigDecimal newIm_obbligazioni) {
	im_obbligazioni = newIm_obbligazioni;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
	if ( im_da_trasferire == null )
		throw new ValidationException( "Cds " + getCd_cds() + ":  e' necessario specificare un importo per il cds selezionato!" );
	if ( im_da_trasferire.compareTo( new java.math.BigDecimal(0) ) <= 0 )
		throw new ValidationException( "Cds " + getCd_cds() + ":  e' necessario specificare un importo maggiore di zero per il cds selezionato!" );	
}
}
