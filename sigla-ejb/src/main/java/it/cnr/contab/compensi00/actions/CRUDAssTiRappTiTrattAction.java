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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.tabrif.bulk.Ass_ti_rapp_ti_trattBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;

/**
 * Insert the type's description here.
 * Creation date: (11/03/2002 17.11.58)
 * @author: Roberto Fantino
 */
public class CRUDAssTiRappTiTrattAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDAssTiRappTiTrattAction constructor comment.
 */
public CRUDAssTiRappTiTrattAction() {
	super();
}
public Forward handleDuplicateKeyException(ActionContext context, it.cnr.jada.comp.CRUDDuplicateKeyException e) {

	setErrorMessage(context, "Errore! Si sta tentando di creare un oggetto già esistente in archivio");

	return context.findDefaultForward();
}
}
