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

import it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP;
import it.cnr.contab.doccont00.bp.RicercaMandatoReversaleBP;
import it.cnr.contab.doccont00.bp.ViewDettaglioTotaliBP;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

public class CRUDDistintaCassiereAction extends it.cnr.jada.util.action.CRUDAction {
    public CRUDDistintaCassiereAction() {
        super();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "distintaCassDet"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doAddToCRUDMain_DistintaCassDet(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
            RicercaMandatoReversaleBP ricercaBP = (RicercaMandatoReversaleBP) context.createBusinessProcess("RicercaMandatoReversaleBP", new Object[]{"MTh"});
            if (Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context)).getFl_tesoreria_unica().booleanValue())
                ricercaBP.setSearchResultColumnSet("elencoConUoFirmati");
            else if (bp.isElencoConUo())
                ricercaBP.setSearchResultColumnSet("elencoConUo");
            else
                ricercaBP.setSearchResultColumnSet("default");

            context.addHookForward("bringback", this, "doBringBackDettaglioDistinta");
            return context.addBusinessProcess(ricercaBP);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackDettaglioDistinta(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) context.getBusinessProcess();
            bp.getDistintaCassDet().reset(context);
            bp.setDirty(true);
            bp.calcolaTotali(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione di tutti gli elementi dal controller "distintaCassDet"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveAllFromCRUDMain_DistintaCassDet(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
            bp.getDistintaCassDet().removeAll(context);
            bp.calcolaTotali(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "distintaCassDet"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_DistintaCassDet(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
            bp.controllaEliminaMandati(context);
            bp.getDistintaCassDet().remove(context);
            bp.calcolaTotali(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doVisualizzaDettagliTotali(ActionContext context) {
        try {
            fillModel(context);
            Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getBusinessProcess(context).getModel();
            ViewDettaglioTotaliBP view = (ViewDettaglioTotaliBP) context.createBusinessProcess("ViewDettaglioTotaliBP");
            view.setModel(context, distinta);
            return context.addBusinessProcess(view);
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doVisualizzaDettagliTotaliTrasmessi(ActionContext context) {
        try {
            fillModel(context);
            Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getBusinessProcess(context).getModel();
            ViewDettaglioTotaliBP view = (ViewDettaglioTotaliBP) context.createBusinessProcess("ViewDettaglioTotaliTrasmessiBP");
            view.setModel(context, distinta);
            return context.addBusinessProcess(view);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnCheckIbanFailed(ActionContext context, int option) {
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            try {
                CRUDBP bp = getBusinessProcess(context);
                ((Distinta_cassiereBulk) bp.getModel()).setCheckIbanEseguito(true);
                doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    public Forward doEstrai(ActionContext context) {
        try {
            CRUDBP bp = getBusinessProcess(context);
            fillModel(context);
            if (bp.isDirty())
                return openContinuePrompt(context, "doConfermaEstrai");
            return doConfermaEstrai(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaEstrai(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
                bp.generaXML(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSign(ActionContext context) {
        try {
            CRUDBP bp = getBusinessProcess(context);
            fillModel(context);
            if (bp.isDirty())
                return openContinuePrompt(context, "doConfermaSign");
            return doConfermaSign(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaSign(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                try {
                    BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
                    firmaOTPBP.setModel(context, new FirmaOTPBulk());
                    context.addHookForward("firmaOTP", this, "doBackSign");
                    return context.addBusinessProcess(firmaOTPBP);
                } catch (Exception e) {
                    return handleException(context, e);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBackSign(ActionContext context) {
        CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) context.getBusinessProcess();

        HookForward caller = (HookForward) context.getCaller();
        FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
        try {
            fillModel(context);
            firmaOTPBulk.validate();
            bp.invia(context, firmaOTPBulk);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    public Forward doInvia(ActionContext context) {
        try {
            CRUDBP bp = getBusinessProcess(context);
            fillModel(context);
            if (bp.isDirty())
                return openContinuePrompt(context, "doConfermaInvio");
            return doConfermaInvio(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaInvio(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
                bp.inviaDistinta(context, (Distinta_cassiereBulk) bp.getModel());
                bp.setMessage("Salvataggio effettuato correttamente.");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doInviaPEC(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
            final Boolean inviata = bp.reinviaPEC(context, (Distinta_cassiereBulk) bp.getModel());
            bp.setMessage(inviata ? "PEC inviata correttamente." : "PEC non inviata, in quanto non erano presenti disposizioni con documenti esterni.");
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doInviaSiopeplus(ActionContext context) {
        try {
            CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess(context);
            fillModel(context);
            bp.generaFlussoSeRifiutato(context);
            if (bp.isDirty())
                return openContinuePrompt(context, "doConfermaSign");
            return doConfermaInviaSiopeplus(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaInviaSiopeplus(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                try {
                    BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
                    firmaOTPBP.setModel(context, new FirmaOTPBulk());
                    context.addHookForward("firmaOTP", this, "doBackInviaSiopeplus");
                    return context.addBusinessProcess(firmaOTPBP);
                } catch (Exception e) {
                    return handleException(context, e);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBackInviaSiopeplus(ActionContext context) {
        CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) context.getBusinessProcess();

        HookForward caller = (HookForward) context.getCaller();
        FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
        try {
            fillModel(context);
            firmaOTPBulk.validate();
            bp.inviaSiopeplus(context, firmaOTPBulk);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }



}
