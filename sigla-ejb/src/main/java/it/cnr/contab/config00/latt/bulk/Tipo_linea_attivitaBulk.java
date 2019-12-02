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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_linea_attivitaBulk extends Tipo_linea_attivitaBase implements CostantiTi_gestione {
	protected FunzioneBulk funzione;
	protected NaturaBulk natura;
	public static final java.util.Dictionary TI_TIPO_LA;
	public final static String COMUNE = "C";
	public final static String PROPRIA = "P";
	public final static String SISTEMA = "S";
	public final static String SAUO = "SAUO";
	public final static String SAUOP = "SAUOP";
	public final static String CSSAC = "CSSAC";
	public final static String SPESECOSTIALTRUI = "Spese costi altrui";
	public final static String RICAVIFIGURATIVI = "Ricavi figurativi";
	
	protected java.util.Collection funzioni;
	protected java.util.Collection nature;

	static {
		TI_TIPO_LA = new it.cnr.jada.util.OrderedHashtable();
		TI_TIPO_LA.put(COMUNE,"Comune");
		TI_TIPO_LA.put(PROPRIA,"Propria");
	}

	private final static java.util.Dictionary ti_gestioneKeys;

	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
public Tipo_linea_attivitaBulk() {
	super();
}
public Tipo_linea_attivitaBulk(java.lang.String cd_tipo_linea_attivita) {
	super(cd_tipo_linea_attivita);
}
/**
 * Restituisce il valore della proprietà 'funzione'
 *
 * @return Il valore della proprietà 'funzione'
 */
public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
	return funzione;
}
/**
 * Restituisce il valore della proprietà 'funzioni'
 *
 * @return Il valore della proprietà 'funzioni'
 */
public java.util.Collection getFunzioni() {
	return funzioni;
}
/**
 * Restituisce il valore della proprietà 'natura'
 *
 * @return Il valore della proprietà 'natura'
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * Restituisce il valore della proprietà 'nature'
 *
 * @return Il valore della proprietà 'nature'
 */
public java.util.Collection getNature() {
	return nature;
}
/**
 * Restituisce il valore della proprietà 'ti_gestioneKeys'
 *
 * @return Il valore della proprietà 'ti_gestioneKeys'
 */
public java.util.Dictionary getTi_gestioneKeys() {
	return ti_gestioneKeys;
}
	/**
	 * Restituisce il <code>Dictionary</code> per definire se la linea di attività e comune o meno.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_tipo_laKeys() {
		return TI_TIPO_LA;
	}
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setTi_tipo_la(COMUNE);
	return this;
}
/**
 * Imposta il valore della proprietà 'funzione'
 *
 * @param newFunzione	Il valore da assegnare a 'funzione'
 */
public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
	funzione = newFunzione;
}
/**
 * Imposta il valore della proprietà 'funzioni'
 *
 * @param newFunzioni	Il valore da assegnare a 'funzioni'
 */
public void setFunzioni(java.util.Collection newFunzioni) {
	funzioni = newFunzioni;
}
/**
 * Imposta il valore della proprietà 'natura'
 *
 * @param newNatura	Il valore da assegnare a 'natura'
 */
public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
	natura = newNatura;
}
/**
 * Imposta il valore della proprietà 'nature'
 *
 * @param newNature	Il valore da assegnare a 'nature'
 */
public void setNature(java.util.Collection newNature) {
	nature = newNature;
}
public void validate() throws ValidationException {
	
	if (getTi_tipo_la().equals(COMUNE)) {
		if (getNatura() == null || getNatura().getCd_natura() == null)
			throw new ValidationException("Se il tipo di GAE è comune la natura deve essere specificata!");
		if (TI_GESTIONE_SPESE.equals(getTi_gestione()) && getFunzione() == null)
			throw new ValidationException("Se il tipo di GAE è comune la funzione deve essere specificata!");
	}
}
}
