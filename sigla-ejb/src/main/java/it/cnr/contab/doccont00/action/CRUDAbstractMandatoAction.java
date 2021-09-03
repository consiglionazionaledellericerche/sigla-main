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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.docamm00.actions.EconomicaAction;
import it.cnr.contab.doccont00.bp.CRUDAbstractMandatoBP;
import it.cnr.contab.doccont00.bp.ListaSospesiSpesaBP;
import it.cnr.contab.doccont00.core.bulk.CompensoOptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

/**
 * <!-- @TODO: da completare -->
 */

public abstract class CRUDAbstractMandatoAction extends EconomicaAction {
    public CRUDAbstractMandatoAction() {
        super();
    }

    /**
     * Gestisce il caricamento dei documenti passivi
     */
    //
// Gestisce la selezione del bottone "Nuova Scadenza"
//
    public Forward doAddToCRUDMain_SospesiSelezionati(ActionContext context) {

        try {
            CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP) context.getBusinessProcess();
            it.cnr.jada.util.RemoteIterator ri = bp.cercaSospesi(context);
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
/*		}
		else if (ri.countElements() == 1) {
			OggettoBulk bulk = (OggettoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			bp.setMessage("La ricerca ha fornito un solo risultato.");
			bp.edit(context,bulk);
			return context.findDefaultForward();*/
            } else {
                //		bp.setModel(context,filtro);
                BulkInfo bulkInfo = BulkInfo.getBulkInfo(SospesoBulk.class);
                ListaSospesiSpesaBP nbp = (ListaSospesiSpesaBP) context.createBusinessProcess("ListaSospesiSpesaBP");
                nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary("SospesiMandato"));
                nbp.setIterator(context, ri);
                nbp.setMultiSelection(true);
                nbp.setBulkInfo(bulkInfo);
                context.addHookForward("seleziona", this, "doRiportaSelezioneSospesi");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
	/*
	try 
	{
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)getBusinessProcess(context);
		fillModel( context );		
		bp.caricaSospesi(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
	*/
    }

    /**
     * Gestisce il caricamento dei documenti passivi
     */
    public Forward doCercaSospesi(ActionContext context) {

        try {
            fillModel(context);
            CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP) context.getBusinessProcess();
            it.cnr.jada.util.RemoteIterator ri = bp.cercaSospesi(context);
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
/*		}
		else if (ri.countElements() == 1) {
			OggettoBulk bulk = (OggettoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			bp.setMessage("La ricerca ha fornito un solo risultato.");
			bp.edit(context,bulk);
			return context.findDefaultForward();*/
            } else {
                //		bp.setModel(context,filtro);
                BulkInfo bulkInfo = BulkInfo.getBulkInfo(SospesoBulk.class);
                ListaSospesiSpesaBP nbp = (ListaSospesiSpesaBP) context.createBusinessProcess("ListaSospesiSpesaBP");
                nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary("SospesiMandato"));
                nbp.setIterator(context, ri);
                nbp.setMultiSelection(true);
//			nbp.setBulkInfo(bulkInfo);
                context.addHookForward("seleziona", this, "doRiportaSelezioneSospesi");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
	/*
	try 
	{
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)getBusinessProcess(context);
		fillModel( context );		
		bp.caricaSospesi(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
	*/
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doConfermaElimina(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            if (choice == OptionBP.YES_BUTTON) {
                CRUDBP bp = getBusinessProcess(context);
                CRUDAbstractMandatoBP bpm = (CRUDAbstractMandatoBP) context.getBusinessProcess();
                MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
                if (bpm.isAnnullabileEnte(context, mandato))
                    return openConfirm(context, "All'annullamento del mandato seguirà la riemissione?", OptionBP.CONFIRM_YES_NO, "doConfermaRiemissione");
                bp.delete(context);
                bp.setMessage("Annullamento effettuato");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaRiemissione(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            CRUDBP bp = getBusinessProcess(context);
            CRUDAbstractMandatoBP bpm = (CRUDAbstractMandatoBP) context.getBusinessProcess();
            if (choice == OptionBP.YES_BUTTON) {
                bpm.deleteRiemissione(context);
                bp.setMessage("Annullamento effettuato");
            } else {
                bp.delete(context);
                bp.setMessage("Annullamento effettuato");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDBP bp = getBusinessProcess(context);

            CRUDAbstractMandatoBP bpm = (CRUDAbstractMandatoBP) context.getBusinessProcess();

            if (!bp.isDeleteButtonEnabled()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                MandatoBulk mandato = (MandatoBulk) bp.getModel();
                if (mandato.isDipendenteDaAltroDocContabile())
                    bp.setMessage("Non è possibile annullare il mandato perchè e' stato originato da un altro doc. contabile");
                else if (bpm.isDipendenteDaConguaglio(context, mandato))
                    bp.setMessage("Non è possibile annullare il mandato poichè e' stato già effettuato il conguaglio del compenso a cui è collegato");
                else if (mandato.getDoc_contabili_collColl().size() > 0)
                    return openConfirm(context, "All'annullamento del mandato anche i documenti contabili collegati verranno annullati. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfermaElimina");
                else
                    return doConfermaElimina(context, OptionBP.YES_BUTTON);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Metodo utilizzato per gestire la conferma a seguito del controllo sul compenso
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di sfondamento
     * @return <code>Forward</code>
     * @throws <code>RemoteException</code>
     */

    public Forward doOnCompensoFailed(ActionContext context, int option) {

        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            try {
                CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP) getBusinessProcess(context);
                it.cnr.contab.doccont00.core.bulk.CompensoOptionRequestParameter param = new CompensoOptionRequestParameter();
                param.setCheckCompensoRequired(new Boolean(true));
                bp.delete(context, param);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce la selezione dei sospesi
     */
    public Forward doRiportaSelezioneSospesi(ActionContext context) {

        try {
            CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP) context.getBusinessProcess();
            bp.aggiungiSospesi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }
}
