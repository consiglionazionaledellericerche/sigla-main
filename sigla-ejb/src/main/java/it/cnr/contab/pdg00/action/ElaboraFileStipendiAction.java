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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.DettagliFileStipendiBP;
import it.cnr.contab.pdg00.bp.ElaboraFileStipendiBP;
import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ApplicationException;

/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 *
 * @author: Gennaro Borriello
 */
public class ElaboraFileStipendiAction extends it.cnr.jada.util.action.CRUDAction {
    /**
     * CaricaFileCassiereAction constructor comment.
     */
    public ElaboraFileStipendiAction() {
        super();
    }

    public Forward doBringBack(ActionContext context) {
        return context.findDefaultForward();
    }

    public Forward doVisualizzaDettagli(ActionContext context) {

        ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();

        try {
            V_stipendi_cofi_dettBulk v = (V_stipendi_cofi_dettBulk) bp.getModel();
            V_stipendi_cofi_dettBulk dett = (V_stipendi_cofi_dettBulk) bp.getV_stipendi_cofi_dett().getModel();
            if (v.getMese() == null) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un Mese");
            }
            if (v.getV_stipendi_cofi_dett().isEmpty()) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esistono dettagli da visualizzare");
            }
            if (dett == null) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare la riga per la quale si vogliono visualizzare i dettagli");
            }
            if (dett.getEntrata_spesa().equals("E")) {
                it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cerca(context.getUserContext(), null, new Stipendi_cofi_cori_dettBulk(), dett, "DettagliFileStipendi");
                ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                if (ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non ci sono dettagli");
                }
                DettagliFileStipendiBP nbp = (DettagliFileStipendiBP) context.createBusinessProcess("DettagliFileStipendiBP");
                nbp.setIterator(context, ri);
                nbp.setMultiSelection(false);
                nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_cori_dettBulk.class));
                return context.addBusinessProcess(nbp);
            } else {
                it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cerca(context.getUserContext(), null, new Stipendi_cofi_obb_scad_dettBulk(), dett, "DettagliFileStipendi");
                ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                if (ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non ci sono dettagli");
                }
                DettagliFileStipendiBP nbp = (DettagliFileStipendiBP) context.createBusinessProcess("DettagliFileStipendiBP");
                nbp.setIterator(context, ri);
                nbp.setMultiSelection(false);
                nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_obb_scad_dettBulk.class));
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    public Forward doVisualizzaEntrata(ActionContext context) {

        ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();

        try {
            V_stipendi_cofi_dettBulk v = (V_stipendi_cofi_dettBulk) bp.getModel();
            if (v.getMese() == null) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un Mese");
            }
            if (v.getV_stipendi_cofi_dett().isEmpty() || v.getBatch_log_riga().isEmpty()) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esistono dati da visualizzare");
            }

            it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cerca(context.getUserContext(), null, new Stipendi_cofi_coriBulk(), v, "FlussoStipendiEntrata");
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non ci sono dettagli");
            }
            DettagliFileStipendiBP nbp = (DettagliFileStipendiBP) context.createBusinessProcess("DettagliFileStipendiBP");
            nbp.setIterator(context, ri);
            nbp.setMultiSelection(false);
            nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_coriBulk.class));
            nbp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_coriBulk.class).getColumnFieldPropertyDictionary("elaborazione_flussi"));
            return context.addBusinessProcess(nbp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doVisualizzaSpesa(ActionContext context) {

        ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();

        try {
            V_stipendi_cofi_dettBulk v = (V_stipendi_cofi_dettBulk) bp.getModel();
            if (v.getMese() == null) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un Mese");
            }
            if (v.getV_stipendi_cofi_dett().isEmpty() || v.getBatch_log_riga().isEmpty()) {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esistono dati da visualizzare");
            }

            it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cerca(context.getUserContext(), null, new Stipendi_cofi_obb_scadBulk(), v, "FlussoStipendiSpesa");
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non ci sono dettagli");
            }
            DettagliFileStipendiBP nbp = (DettagliFileStipendiBP) context.createBusinessProcess("DettagliFileStipendiBP");
            nbp.setIterator(context, ri);
            nbp.setMultiSelection(false);
            nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_obb_scadBulk.class));
            nbp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofi_obb_scadBulk.class).getColumnFieldPropertyDictionary("elaborazione_flussi"));
            return context.addBusinessProcess(nbp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un Eccezione di chiave duplicata.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/
    protected Forward handleApplicationPersistencyException(ActionContext context, it.cnr.jada.persistency.sql.ApplicationPersistencyException e) {

        it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException(e.getMessage());

        return handleException(context, mess);
    }

    /**
     * Gestisce un Eccezione di chiave duplicata.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/
    protected Forward handleDuplicateKeyException(ActionContext context, it.cnr.jada.persistency.sql.DuplicateKeyException e) {

        it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException("Si sta tentando di creare un oggetto già esistente in archivio.");

        return handleException(context, mess);
    }

    public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
        it.cnr.contab.pdg00.bp.ElaboraFileStipendiBP bp = (it.cnr.contab.pdg00.bp.ElaboraFileStipendiBP) context.getBusinessProcess();
        try {
            bp.fillModel(context);
            bp.refresh(context);
            doCercaBatch(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doCercaBatch(ActionContext context) {
        try {
            fillModel(context);
            ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();
            V_stipendi_cofi_dettBulk dett = (V_stipendi_cofi_dettBulk) bp.getModel();

            if (dett.getPg_exec() != null)
                try {
                    bp.doCercaBatch(context, dett);
                } catch (Throwable e) {
                    return handleException(context, e);
                }
        } catch (FillException e1) {
            return handleException(context, e1);
        }
        return context.findDefaultForward();
    }

    public Forward doElaboraFile(ActionContext context) throws ApplicationException {

        try {
            fillModel(context);
            ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();
            V_stipendi_cofi_dettBulk dett = (V_stipendi_cofi_dettBulk) bp.getModel();

            if (dett.getMese() == null)
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
            if (dett.getV_stipendi_cofi_dett().isEmpty())
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Elaborazione non consentita: non sono stati caricati i dati per il mese specificato"));
            if (!dett.getBatch_log_riga().isEmpty())
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Elaborazione già effettuata in precedenza"));
            try {
                bp.doElaboraFile(context, dett);
                bp.refresh(context);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            bp.setMessage("Elaborazione completata.");
            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        }
    }

    public Forward doReset(ActionContext context) {
        try {
            fillModel(context);
            ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();
            V_stipendi_cofi_dettBulk dett = (V_stipendi_cofi_dettBulk) bp.getModel();

            if (dett.getMese() == null)
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
            if (dett.getV_stipendi_cofi_dett().isEmpty())
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Operazione non consentita: il mese selezionato non è stato ancora caricato"));
            if (dett.getBatch_log_riga().isEmpty())
                return handleException(context, new it.cnr.jada.bulk.ValidationException("Operazione non consentita: il mese selezionato non è stato ancora elaborato"));
            try {
                bp.doReset(context, dett);
                bp.refresh(context);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            bp.setMessage("Elaborazione Annullata.");
            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        }
    }
}