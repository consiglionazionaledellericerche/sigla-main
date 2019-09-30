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

package it.cnr.contab.config00.bp;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDTipo_linea_attivitaBP extends it.cnr.jada.util.action.SimpleCRUDBP implements SelectionListener {
	private final RemoteDetailCRUDController cdrAssociati = new RemoteDetailCRUDController("cdrAssociati",CdrBulk.class,"cdrAssociati","CNRCONFIG00_EJB_Tipologia_linea_attivitaComponentSession",this) {
		public boolean isGrowable() {
			return super.isGrowable() && isEditing();
		}
		public boolean isShrinkable() {
			return super.isGrowable() && isEditing();
		}
	};

public CRUDTipo_linea_attivitaBP() {
	super();
}

public CRUDTipo_linea_attivitaBP(String function) {
	super(function+"Tr");
}

public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)createComponentSession()).annullaModificaCdrAssociati(
			context.getUserContext(),
			(Tipo_linea_attivitaBulk)getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}

public void deselectAll(it.cnr.jada.action.ActionContext context) {}

public final it.cnr.jada.util.action.RemoteDetailCRUDController getCdrAssociati() {
	return cdrAssociati;
}

/**
 * Ritorna la selezione corrente
 *
 * @param context	L'ActionContext della richiesta
 * @param bulks	collezione di oggetti bulk
 * @param currentSelection selezione corrente
 * @return istanza di java.util.BitSet
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	//for (int i = 0;i < bulks.length;i++) {
		//if (Boolean.TRUE.equals(((Cdr_ass_tipo_laBulk)bulks[i]).getFl_associato()))
			//currentSelection.set(i);
	//}
	return currentSelection;
}

public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)createComponentSession()).inizializzaCdrAssociatiPerModifica(
			context.getUserContext(),
			(Tipo_linea_attivitaBulk)getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}

protected void resetTabs(ActionContext context) {
	setTab("tab","tabTestata");
}

public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)createComponentSession()).associaTuttiCdr(
			context.getUserContext(),
			(Tipo_linea_attivitaBulk)getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}

/**
 * Gestisce la richiesta utente di modifica dei cdr associati al tipo di linea di attività
 *
 * @param context	L'ActionContext della richiesta
 * @param bulks	Collezione di oggetti bulk
 * @param oldSelection	Vecchia selezione
 * @param newSelection	Nuova selezione
 * @return Istanza di java.util.BitSet
 * @throws BusinessProcessException	
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)createComponentSession()).modificaCdrAssociati(
			context.getUserContext(),
			(Tipo_linea_attivitaBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
}