package it.cnr.contab.pdg00.cdip.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_dipendenteBulk extends V_dipendenteBase {
	private it.cnr.jada.bulk.BulkList costi_per_elemento_voce;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
	public final static Dictionary tipo_naturaKeys = NaturaBulk.tipo_naturaKeys;
	
public V_dipendenteBulk() {
	super();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costi_per_elemento_voce'
 *
 * @return Il valore della proprietà 'costi_per_elemento_voce'
 */
public it.cnr.jada.bulk.BulkList getCosti_per_elemento_voce() {
	return costi_per_elemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'unita_organizzativa'
 *
 * @return Il valore della proprietà 'unita_organizzativa'
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'costi_per_elemento_voce'
 *
 * @param newCosti_per_elemento_voce	Il valore da assegnare a 'costi_per_elemento_voce'
 */
public void setCosti_per_elemento_voce(it.cnr.jada.bulk.BulkList newCosti_per_elemento_voce) {
	costi_per_elemento_voce = newCosti_per_elemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'unita_organizzativa'
 *
 * @param newUnita_organizzativa	Il valore da assegnare a 'unita_organizzativa'
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
