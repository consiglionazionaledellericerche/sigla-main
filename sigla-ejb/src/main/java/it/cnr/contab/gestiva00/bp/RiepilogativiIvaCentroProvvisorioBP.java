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

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class RiepilogativiIvaCentroProvvisorioBP extends StampaRegistriIvaBP {

	private int status = INSERT;
public RiepilogativiIvaCentroProvvisorioBP() {
	this("");
}
public RiepilogativiIvaCentroProvvisorioBP(String function) {
	super(function+"Tr");
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Riepilogativi_iva_centro_provvisorioVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Riepilogativi_iva_centro_provvisorioVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Riepilogativi_iva_centro_provvisorioVBulk bulk = new Riepilogativi_iva_centro_provvisorioVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Riepilogativi_iva_centro_provvisorioVBulk)bulk.initializeForSearch(this,context);
		
		bulk.setTipi_sezionali(
			createComponentSession().selectTipi_sezionaliByClause(
														context.getUserContext(),
														bulk,
														new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
														null));
		return bulk;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetForSearch(context);
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		setDirty(false);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}
