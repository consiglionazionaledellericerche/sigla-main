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

package it.cnr.contab.pdg00.cdip.bulk;
import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Costo_del_dipendenteBulk extends Costo_del_dipendenteBase {
	public final static String TI_RAPPORTO_INDETERMINATO = "IND";  
	public final static String TI_RAPPORTO_DETERMINATO = "DET";  

	public final static java.util.Dictionary ti_rapportoKeys;

	static {
		ti_rapportoKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_rapportoKeys.put(TI_RAPPORTO_INDETERMINATO,"Tempo indeterminato");
		ti_rapportoKeys.put(TI_RAPPORTO_DETERMINATO,"Tempo determinato");
	}

	public final static Dictionary tipo_naturaKeys = NaturaBulk.tipo_naturaKeys;
	
	private EV_cds_spese_capitoloBulk elemento_voce;

public Costo_del_dipendenteBulk() {
	super();
}

public Costo_del_dipendenteBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super(cd_elemento_voce,esercizio,id_matricola,mese,ti_appartenenza,ti_gestione,ti_prev_cons);
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
}

public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elemento_voce'
 *
 * @return Il valore della proprietà 'elemento_voce'
 */
public it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk getElemento_voce() {
	return elemento_voce;
}

public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}

public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_rapportoKeys'
 *
 * @return Il valore della proprietà 'ti_rapportoKeys'
 */
public java.util.Dictionary getTi_rapportoKeys() {
	return ti_rapportoKeys;
}

public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elemento_voce'
 *
 * @param newElemento_voce	Il valore da assegnare a 'elemento_voce'
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}

public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}

public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}

public void validate() throws ValidationException {
	if (getIm_a1() == null)
		throw new ValidationException("Importo Anno 1 deve essere valorizzato");
	if (getIm_a2() == null)
		throw new ValidationException("Importo Anno 2 deve essere valorizzato");
	if (getIm_a3() == null)
		throw new ValidationException("Importo Anno 3 deve essere valorizzato");
	if (getIm_oneri_cnr_a1() == null)
		throw new ValidationException("Importo Oneri CNR Anno 1 deve essere valorizzato");
	if (getIm_oneri_cnr_a2() == null)
		throw new ValidationException("Importo Oneri CNR Anno 2 deve essere valorizzato");
	if (getIm_oneri_cnr_a3() == null)
		throw new ValidationException("Importo Oneri CNR Anno 3 deve essere valorizzato");
	if (getIm_tfr_a1() == null)
		throw new ValidationException("Importo TFR Anno 1 deve essere valorizzato");
	if (getIm_tfr_a2() == null)
		throw new ValidationException("Importo TFR Anno 2 deve essere valorizzato");
	if (getIm_tfr_a3() == null)
		throw new ValidationException("Importo TFR Anno 3 deve essere valorizzato");
}
}