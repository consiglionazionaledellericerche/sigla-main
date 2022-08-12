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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.bp.IDocAmmEconomicaBP;
import it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.V_impegnoBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.util.List;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Mandato di Accreditamento
 */

public class CRUDMandatoAccreditamentoBP extends CRUDAbstractMandatoBP {
    //	private final CRUDMandatoRigaController mandatoRighe = new CRUDMandatoRigaController("MandatoRighe",MandatoAccreditamento_rigaBulk.class,"mandato_rigaColl",this);
    private final SimpleDetailCRUDController impegni = new SimpleDetailCRUDController("Impegni", V_impegnoBulk.class, "impegniSelezionatiColl", this);

    public CRUDMandatoAccreditamentoBP() {
        super();
        setTab("tab", "tabMandato");
    }

    public CRUDMandatoAccreditamentoBP(String function) {
        super(function);
        setTab("tab", "tabMandato");
    }

    public void aggiungiImpegni(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
            HookForward caller = (HookForward) context.getCaller();
            List impegni = (List) caller.getParameter("selectedElements");

            if (impegni == null)
                return;

            mandato.addToImpegniSelezionatiColl(impegni);

            setModel(context, mandato);
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per gestire l'aggiunta di nuove righe al mandato.
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */

    public void aggiungiRighe(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
	/*	if ( getImpegni().getSelectedModels(context).size() != 0 )
		{
			mandato = (MandatoAccreditamentoBulk) ((MandatoComponentSession) createComponentSession()).aggiungiDocPassivi( context.getUserContext(), mandato, getImpegni().getSelectedModels(context));
			setModel( mandato );
			getImpegni().getSelection().clear();
			resyncChildren();
		}*/
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Metodo utilizzato per gestire la modifica delle coordinate bancarie (BancaBulk) a seguito della
     * modifica delle modalita Di Pagamento
     *
     * @param context <code>ActionContext</code> in uso.
     */

    public void cambiaModalitaPagamento(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
            List result = ((MandatoComponentSession) createComponentSession()).findBancaOptions(context.getUserContext(), mandato);
            mandato.setBancaOptions(result);
            setModel(context, mandato);
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    public it.cnr.jada.util.RemoteIterator cercaImpegni(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
            return ((MandatoComponentSession) createComponentSession()).cercaImpegni(context.getUserContext(), null, mandato);
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return toolbar La nuova toolbar creata
     */
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[8];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.printpdf");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.contabile");
        toolbar = IDocAmmEconomicaBP.addPartitario(toolbar, attivaEconomicaParallela, isEditing(), getModel());
        return toolbar;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 10.05.20)
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getImpegni() {
        return impegni;
    }

    /**
     * Verifica se il bottone di Aggiunta delle righe di Mandato è abilitato.
     *
     * @return FALSE    Il bottone di Aggiunta delle righe di Mandato non è abilitato
     */
    public boolean isAddImpegniButtonEnabled() {
        return isEditable() && isEditing() && !((MandatoBulk) getModel()).isAnnullato();
    }

    public boolean isDeleteImpegniButtonEnabled() {
        return isEditable() && isEditing() && !((MandatoBulk) getModel()).isAnnullato() &&
                ((MandatoAccreditamentoBulk) getModel()).getImpegniSelezionatiColl() != null && ((MandatoAccreditamentoBulk) getModel()).getImpegniSelezionatiColl().size() > 0;
    }

    /**
     * Inzializza il ricevente nello stato di SEARCH.
     *
     * @param context Il contesto dell'azione
     */
    public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            rollbackUserTransaction();
            setModel(context, createEmptyModelForSearch(context));
            setStatus(SEARCH);
            setDirty(false);
            resetChildren(context);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }
}
