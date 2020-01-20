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

import it.cnr.contab.anagraf00.bp.CRUDTerzoBP;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * funzionalità supplementari necessarie al crud dell'anagrafina.
 */
public class LuogoConsegnaAction extends it.cnr.jada.util.action.CRUDAction {

	// public final String UTENTE_MIGRAZIONE = "$$$$$MIGRAZIONE$$$$$";

	/**
	 * Costruttore standard di CRUDAnagraficaAction.
	 *
	 */

	public LuogoConsegnaAction() {
		super();
	}



	public Forward doBringBackSearchFindComune(ActionContext context,
											   LuogoConsegnaMagBulk luogoBulk,
											   ComuneBulk comuneBulkBulk) {
		if ( comuneBulkBulk!=null) {
			luogoBulk.setCap(comuneBulkBulk.getCd_cap());
			luogoBulk.setComuneItaliano(comuneBulkBulk);
		}
		return context.findDefaultForward();
	}

	public Forward doBlankSearchFindComune(ActionContext context,
										   LuogoConsegnaMagBulk luogoBulk) {
		try {
			luogoBulk.setComuneItaliano(null);
			luogoBulk.setCap("");

		} catch (Exception var5) {
			throw new ActionPerformingError(var5);
		}
		return context.findDefaultForward();
	}

}
