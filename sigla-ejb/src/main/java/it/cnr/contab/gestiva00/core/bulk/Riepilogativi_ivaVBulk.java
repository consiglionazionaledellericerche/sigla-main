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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public abstract class Riepilogativi_ivaVBulk
    extends Stampa_registri_ivaVBulk
    implements IPrintable {

    public final static String SEZIONALI_FLAGS_VEN = "V";
    public final static String SEZIONALI_FLAGS_ACQ = "A";
    public final static String SEZIONALI_FLAGS_SEZ = "S";

    public final static java.util.Dictionary SEZIONALI_FLAG_KEYS;
	
    static {

        SEZIONALI_FLAG_KEYS = new it.cnr.jada.util.OrderedHashtable();
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_VEN, "Tutti i sezionali di vendita");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_ACQ, "Tutti i sezionali di acquisto");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_SEZ, "Seleziona sezionale");
    }
    
    private String sezionaliFlag = SEZIONALI_FLAGS_SEZ;
	private java.math.BigDecimal id_report = null;

/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Riepilogativi_ivaVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:21:10 PM)
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:21:10 PM)
 */
public abstract java.lang.String getReportName();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:21:10 PM)
 */
public abstract java.util.Vector getReportParameters();
/**
 * Insert the method's description here.
 * Creation date: (30/08/2002 13.48.56)
 * @return java.lang.String
 */
public java.lang.String getSezionaliFlag() {
	return sezionaliFlag;
}
/**
 * Insert the method's description here.
 * Creation date: (30/08/2002 13.48.56)
 * @param newSezionaliFlag java.lang.String
 */
public java.util.Dictionary getSezionaliFlags() {

	OrderedHashtable dic = (OrderedHashtable)((OrderedHashtable)SEZIONALI_FLAG_KEYS).clone();

	if (SEZIONALI_SAN_MARINO_SENZA_IVA.equalsIgnoreCase(getTipoSezionaleFlag()))
		dic.remove(SEZIONALI_FLAGS_VEN);
		
	return dic;
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_documento_stampato() {
	return "*";
}
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogativi_ivaVBulk bulk = (Riepilogativi_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setTipoSezionaleFlag(SEZIONALI_COMMERCIALI);
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:21:10 PM)
 */
public boolean isRistampabile() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:21:10 PM)
 */
public void setId_report(java.math.BigDecimal new_id_report) {

	id_report = new_id_report;	
}
/**
 * Insert the method's description here.
 * Creation date: (30/08/2002 13.48.56)
 * @param newSezionaliFlag java.lang.String
 */
public void setSezionaliFlag(java.lang.String newSezionaliFlag) {
	sezionaliFlag = newSezionaliFlag;
}
}
