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

package it.cnr.contab.gestiva00.bp;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.action.CompoundPropertyController;

import java.sql.Timestamp;


public class LiquidazioneIvaBP extends StampaRegistriIvaBP {

	private int status = INSERT;
	private boolean liquidato = false;
	private final CompoundPropertyController dettaglio = new CompoundPropertyController("liquidazione_iva", Liquidazione_ivaBulk.class,"liquidazione_iva",this);

public LiquidazioneIvaBP() {
	super();
}

public LiquidazioneIvaBP(String function) {
	super(function);
}

/**
 * Restituisce il valore della proprietà 'dettaglio'
 *
 * @return Il valore della proprietà 'dettaglio'
 */
public final CompoundPropertyController getDettaglio() {
	return dettaglio;
}

/**
 * Restituisce il valore della proprietà 'liquidato'
 *
 * @return Il valore della proprietà 'liquidato'
 */
public boolean isLiquidato() {
	return liquidato;
}

/**
 * Imposta il valore della proprietà 'liquidato'
 *
 * @param newLiquidato	Il valore da assegnare a 'liquidato'
 */
public void setLiquidato(boolean newLiquidato) {
	liquidato = newLiquidato;
}
public void inizializzaMese(ActionContext context) throws BusinessProcessException {
}
	@Override
	public void doOnMeseChange(ActionContext context) throws BusinessProcessException {
		super.doOnMeseChange(context);
		try {
			inizializzaMese(context);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	@Override
	protected void setDataDaA(ActionContext context, Stampa_registri_ivaVBulk stampaBulk) {
		int esercizio = stampaBulk.getEsercizio().intValue();
		int meseIndex = ((Integer)stampaBulk.getMesi_int().get(stampaBulk.getMese())).intValue();

		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		gc.set(java.util.Calendar.DAY_OF_MONTH, 1);
		gc.set(java.util.Calendar.YEAR, esercizio);

		gc.set(java.util.Calendar.MONTH, ((meseIndex > 0) ? meseIndex-1 : meseIndex));
		stampaBulk.setData_da(new Timestamp(gc.getTime().getTime()));
		if (meseIndex < 0)
			gc.set(java.util.Calendar.YEAR, esercizio);
		gc.set(java.util.Calendar.MONTH, ((meseIndex > 0) ? meseIndex : meseIndex + 1));
		gc.add(java.util.Calendar.DAY_OF_MONTH, -1);
		stampaBulk.setData_a(new Timestamp(gc.getTime().getTime()));
	}
}