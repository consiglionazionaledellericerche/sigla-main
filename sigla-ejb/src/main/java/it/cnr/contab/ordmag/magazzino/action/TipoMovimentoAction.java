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

package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;


/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * funzionalità supplementari necessarie al crud dell'anagrafina.
 */
public class TipoMovimentoAction extends it.cnr.jada.util.action.CRUDAction {

	// public final String UTENTE_MIGRAZIONE = "$$$$$MIGRAZIONE$$$$$";

	/**
	 * Costruttore standard di CRUDAnagraficaAction.
	 *
	 */

	public TipoMovimentoAction() {
		super();
	}

	public Forward doOnChangeTipo(ActionContext context) {
		try {
			fillModel(context);
			TipoMovimentoMagBulk tipoMovimento = (TipoMovimentoMagBulk)getBusinessProcess(context).getModel();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

		return context.findDefaultForward();
	}



}
