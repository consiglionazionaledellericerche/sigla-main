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

package it.cnr.contab.prevent00.bp;
/**
 * Classe che imposta la stampa dei documenti dei piani di gestione.
 * 
 */
public class DocumentoPdGBP  extends it.cnr.jada.util.action.FormBP {
/**
 * Costruttore standard di CRUDTotaliPdGAggregatoBP.
 */
public DocumentoPdGBP() {
	super();
}

/**
 * Costruttore di CRUDTotaliPdGAggregatoBP cui viene passata la funzione in ingresso.
 *
 * @param funzione java.lang.String
 */
public DocumentoPdGBP(String function) {
	super(function);
}

public String getPrintbp() {
 return "OfflineReportPrintBP";
}

/**
 * Inizializza la schermata presentando in primo piano il Tab dell'esercizio dell'anno corrente
 *
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @exception it.cnr.jada.action.BusinessProcessException
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
	}
}