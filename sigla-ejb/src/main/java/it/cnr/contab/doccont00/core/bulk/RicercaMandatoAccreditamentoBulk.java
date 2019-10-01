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

public class RicercaMandatoAccreditamentoBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.sql.Timestamp   dt_scadenza_obbligazioni;
	private java.math.BigDecimal im_disp_cassa_CNR;
	private java.util.HashMap 	 centriDiSpesaMap = new java.util.HashMap();
	private java.util.Collection centriDiSpesaColl;
	private java.util.Collection centriDiSpesaSelezionatiColl;
	private boolean flTuttiCdsCaricati = true;
public RicercaMandatoAccreditamentoBulk() {
	super();
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCentriDiSpesaColl() {
	return centriDiSpesaColl;
}
/**
 * @return java.util.HashMap
 */
public java.util.HashMap getCentriDiSpesaMap() {
	return centriDiSpesaMap;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCentriDiSpesaSelezionatiColl() {
	return centriDiSpesaSelezionatiColl;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dt_scadenza_obbligazioni'
 *
 * @return Il valore della proprietà 'dt_scadenza_obbligazioni'
 */
public java.sql.Timestamp getDt_scadenza_obbligazioni() {
	return dt_scadenza_obbligazioni;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_disp_cassa_CNR'
 *
 * @return Il valore della proprietà 'im_disp_cassa_CNR'
 */
public java.math.BigDecimal getIm_disp_cassa_CNR() {
	return im_disp_cassa_CNR;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'flTuttiCdsCaricati'
 *
 * @return Il valore della proprietà 'flTuttiCdsCaricati'
 */
public boolean isFlTuttiCdsCaricati() {
	return flTuttiCdsCaricati;
}
/**
 * @param newCentriDiSpesaColl java.util.Collection
 */
public void setCentriDiSpesaColl(java.util.Collection newCentriDiSpesaColl) {
	centriDiSpesaColl = newCentriDiSpesaColl;
	V_disp_cassa_cdsBulk cds;
	for (java.util.Iterator i = centriDiSpesaColl.iterator(); i.hasNext(); )
	{
		cds = (V_disp_cassa_cdsBulk) i.next();
		centriDiSpesaMap.put( cds.getCd_cds(), cds );
	}	
	
}
/**
 * @param newCentriDiSpesaMap java.util.HashMap
 */
public void setCentriDiSpesaMap(java.util.HashMap newCentriDiSpesaMap) {
	centriDiSpesaMap = newCentriDiSpesaMap;
}
/**
 * @param newCentriDiSpesaSelezionatiColl java.util.Collection
 */
public void setCentriDiSpesaSelezionatiColl(java.util.Collection newCentriDiSpesaSelezionatiColl) {
	centriDiSpesaSelezionatiColl = newCentriDiSpesaSelezionatiColl;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'dt_scadenza_obbligazioni'
 *
 * @param newDt_scadenza_obbligazioni	Il valore da assegnare a 'dt_scadenza_obbligazioni'
 */
public void setDt_scadenza_obbligazioni(java.sql.Timestamp newDt_scadenza_obbligazioni) {
	dt_scadenza_obbligazioni = newDt_scadenza_obbligazioni;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'flTuttiCdsCaricati'
 *
 * @param newFlTuttiCdsCaricati	Il valore da assegnare a 'flTuttiCdsCaricati'
 */
public void setFlTuttiCdsCaricati(boolean newFlTuttiCdsCaricati) {
	flTuttiCdsCaricati = newFlTuttiCdsCaricati;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_disp_cassa_CNR'
 *
 * @param newIm_disp_cassa_CNR	Il valore da assegnare a 'im_disp_cassa_CNR'
 */
public void setIm_disp_cassa_CNR(java.math.BigDecimal newIm_disp_cassa_CNR) {
	im_disp_cassa_CNR = newIm_disp_cassa_CNR;
}
}
