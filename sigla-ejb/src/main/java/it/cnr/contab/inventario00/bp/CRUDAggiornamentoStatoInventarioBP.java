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

package it.cnr.contab.inventario00.bp;

import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.upload.UploadedFile;

import java.rmi.RemoteException;
import java.util.TreeMap;

public class CRUDAggiornamentoStatoInventarioBP extends CRUDAggiornamentoInventarioBP{
	private static final long LUNGHEZZA_MAX=0x1000000;

	public CRUDAggiornamentoStatoInventarioBP() {
		super();
	}
	/**
	 * CRUDTrasferimentoInventarioBP constructor comment.
	 * @param function java.lang.String
	 */
	public CRUDAggiornamentoStatoInventarioBP(String function) {
		super(function);
		setTab("tab","tabAggiornamentoInventarioTestata");
	}

	private static final String[] TAB_STATO = new String[]{ "tabAggiornamentoInventarioStato","Aggiornamento Stato","/inventario00/tab_aggiornamento_stato.jsp" };

	public String[][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i = 0;
		pages.put(i++, TAB_TESTATA);
		pages.put(i++, TAB_STATO);
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
		return tabs;
	}

	@Override
	public void validate(ActionContext actioncontext) throws ValidationException {
		// TODO Auto-generated method stub
		Aggiornamento_inventarioBulk allegato = (Aggiornamento_inventarioBulk)this.getModel();
		UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter(getInputPrefix()+".blob");

		if (allegato.isBeneSmarrito() && (file == null || file.getName().equals("")))
			throw new ValidationException("Attenzione: selezionare un File da caricare.");

		if (!(file == null || file.getName().equals(""))) {
			if (file.length() > LUNGHEZZA_MAX)
				throw new ValidationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");

			if (!file.getContentType().equals("application/pdf"))
				throw new ValidationException("File non valido! Il formato del file consentito è il pdf.");

			allegato.setFile(file.getFile());
		}

		super.validate(actioncontext);
	}

	public String getColumnSetName() {
		return "byStato";
	}

	public void aggiornamento_beni (ActionContext context) throws BusinessProcessException, DetailedRuntimeException, ComponentException, RemoteException, PersistencyException, OutdatedResourceException, BusyResourceException {
		it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiorno = (it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)getModel();
		it.cnr.contab.inventario00.ejb.Aggiornamento_inventarioComponentSession h = (it.cnr.contab.inventario00.ejb.Aggiornamento_inventarioComponentSession)createComponentSession();
		setModel(context,h.aggiornaStatoBeni(context.getUserContext(),aggiorno));
		setMessage("Salvataggio eseguito in modo corretto.");
	}

}

