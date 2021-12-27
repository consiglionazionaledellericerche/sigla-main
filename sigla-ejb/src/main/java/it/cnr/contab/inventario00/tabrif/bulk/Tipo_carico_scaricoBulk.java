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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;

public class Tipo_carico_scaricoBulk extends Tipo_carico_scaricoBase {

	public final static java.lang.String TIPO_CARICO = "C";
	public final static java.lang.String TIPO_SCARICO = "S";

	private final static java.util.Dictionary tipoDocumentoKeys;

	static
	{
		tipoDocumentoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoDocumentoKeys.put(TIPO_CARICO, "Carico");
		tipoDocumentoKeys.put(TIPO_SCARICO, "Scarico");
	}

public Tipo_carico_scaricoBulk() {
	super();
}
public Tipo_carico_scaricoBulk(java.lang.String cd_tipo_carico_scarico) {
	super(cd_tipo_carico_scarico);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 12.19.00)
 * @return java.util.Dictionary
 */
public final java.util.Dictionary getTipoDocumentoKeys() {
	return tipoDocumentoKeys;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>INSERT</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per l'inserimento di un OggettoBulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setTi_documento(TIPO_CARICO);
	setFl_buono_per_trasferimento(Boolean.FALSE);
	setFl_chiude_fondo(Boolean.FALSE);
	setFl_storno_fondo(Boolean.FALSE);
	setFl_elabora_buono_coge(Boolean.FALSE);
	setFl_vendita(Boolean.FALSE);
	setFl_da_ordini(Boolean.FALSE);
	return super.initializeForInsert(bp, context);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 14.05.14)
 * @return boolean
 */
public boolean isAumentoValore() {
	
	return getFl_aumento_valore().booleanValue();
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 14.05.14)
 * @return boolean
 */
public boolean isCancellabile() {
	return getDt_cancellazione()==null;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 14.05.14)
 * @return boolean
 */
public boolean isElaborabileCOGE() {
	if (getFl_elabora_buono_coge()!=null)
		return getFl_elabora_buono_coge().booleanValue();
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 14.05.14)
 * @return boolean
 */
public boolean isFatturabile() {
	if (getFl_fatturabile()!= null)
		return getFl_fatturabile().booleanValue();
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 14.05.14)
 * @return boolean
 */
public boolean isQuoteElaborabile() {
	
	return getFl_chiude_fondo().booleanValue();
}
public boolean isTrasferibile() {
	if (getFl_buono_per_trasferimento()!=null )
		return getFl_buono_per_trasferimento().booleanValue();
	else
		return false;
}
}
