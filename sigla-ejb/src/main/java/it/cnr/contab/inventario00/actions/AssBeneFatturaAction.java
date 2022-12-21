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

package it.cnr.contab.inventario00.actions;

/**
 * Questa classe gestisce le richieste provenienti dalle jsp relative all'associazione di
 * una Fattura Passiva a dei beni esistenti nel DB.
 *
 * <p>Implementa i seguenti comandi:
 * <dl>
 * <dt> doRiporta
 * <dt> checkRigheFattura_perAssociazione
 * <dt> doSelectDettagliFattura
 * <dt> doSelezionaBeni_associati
 * <dt> doAddToCRUDMain_DettagliFattura_RigheInventarioDaFattura
 * <dt> doAnnullaRiporta
 * </dl>
 **/

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario00.bp.ListaBeniBP;
import it.cnr.contab.inventario00.bp.ListaBuoniBP;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.ObjectReplacer;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.rmi.RemoteException;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AssBeneFatturaAction extends CRUDAction {


    public AssBeneFatturaAction() {
        super();
    }

    /**
     * Controlla che le righe selezionate, a cui verranno associate i beni, siano omogenee
     *	per tipo (ISTITUZIONALE/COMMERCIALE).
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    private void checkRigheFattura_perAssociazione(ActionContext context) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.bulk.ValidationException {

        String tipo_riga = null;
        String bene_servizio = null;
        Nota_di_credito_rigaBulk nota = null;
        Nota_di_debito_rigaBulk notadeb = null;
        Fattura_passiva_rigaIBulk riga_fattura = null;
        Fattura_attiva_rigaIBulk riga_attiva = null;
        AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
        Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) bp.getModel();
        if (ass.isPerAumentoValore() && ass.getTest_buono().getData_registrazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: bisogna specificare prima la data registrazione.");
        List dettagliFattura;
        try {
            dettagliFattura = bp.getDettagliFattura().getSelectedModels(context);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage(), e);
        }
        for (Iterator i = dettagliFattura.iterator(); i.hasNext(); ) {
            if (dettagliFattura.get(0).getClass().equals(Nota_di_credito_rigaBulk.class))
                nota = (Nota_di_credito_rigaBulk) i.next();
            else if (dettagliFattura.get(0).getClass().equals(Nota_di_debito_rigaBulk.class))
                notadeb = (Nota_di_debito_rigaBulk) i.next();
            else if (dettagliFattura.get(0).getClass().equals(Fattura_attiva_rigaIBulk.class))
                riga_attiva = (Fattura_attiva_rigaIBulk) i.next();
            else
                riga_fattura = (Fattura_passiva_rigaIBulk) i.next();
            // Controlla il tipo, (IST/COM), di riga.
            if (tipo_riga == null && riga_fattura != null)
                tipo_riga = riga_fattura.getTi_istituz_commerc();
            else if (tipo_riga == null && nota != null)
                tipo_riga = nota.getTi_istituz_commerc();
            else if (tipo_riga == null && notadeb != null)
                tipo_riga = notadeb.getTi_istituz_commerc();
            else if (riga_fattura != null && !tipo_riga.equalsIgnoreCase(riga_fattura.getTi_istituz_commerc()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Fattura di Tipo diverso.");
            else if (nota != null && !tipo_riga.equalsIgnoreCase(nota.getTi_istituz_commerc()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Note Credito di Tipo diverso.");
            else if (notadeb != null && !tipo_riga.equalsIgnoreCase(notadeb.getTi_istituz_commerc()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Note Debito di Tipo diverso.");

            // Controlla il Bene/Servizio
            if (riga_fattura != null) {
                if (bene_servizio == null)
                    bene_servizio = riga_fattura.getBene_servizio().getCd_bene_servizio();
                else if (!bene_servizio.equalsIgnoreCase(riga_fattura.getBene_servizio().getCd_bene_servizio()))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Fattura il cui Bene/Servizio sia diverso.");
            } else if (nota != null) {
                if (bene_servizio == null)
                    bene_servizio = nota.getBene_servizio().getCd_bene_servizio();
                else if (!bene_servizio.equalsIgnoreCase(nota.getBene_servizio().getCd_bene_servizio()))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Nota Credito il cui Bene/Servizio sia diverso.");
            } else if (notadeb != null) {
                if (bene_servizio == null)
                    bene_servizio = notadeb.getBene_servizio().getCd_bene_servizio();
                else if (!bene_servizio.equalsIgnoreCase(notadeb.getBene_servizio().getCd_bene_servizio()))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Nota Debito il cui Bene/Servizio sia diverso.");
            }
        }
    }

    /**
     *  Associa dei Beni ad una o più righe di Fattura.
     *	Il metodo dapprima controlla che l'utente abbia selezionato almeno una riga di fattura
     *	a cui associare i beni: in caso negativo, provvede a mandare un messagio di errore dove
     *	richiede di selezionare una riga di Fattura.
     *	In seguito, il sistema controlla che le righe selezionate dall'utente siano omogenee:
     *	controlla, cioè, che siano o tutte ISTITUZIONALI o tutte COMMERCIALI, (metodo checkRigheFattura_perAssociazione).
     *	Infine, verifica l'esistenza di beni che abbiano le caratteristiche per essere associate
     *	alle righe selezionate, (metodo cercaBeniAssociabili): se ci sono beni disponibili, questi
     *	vengono visualizzati all'utente che selezionerà quelli necessari.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doAddToCRUDMain_DettagliFattura_RigheInventarioDaFattura(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            java.util.List selectedModels = bp.getDettagliFattura().getSelectedModels(context);
            Selection selection = bp.getDettagliFattura().getSelection(context);

            // Controlla che l'utente abia selezionato almeno una riga di Fattura
            if (selection == null || selection.size() == 0) {
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una o più righe di Fattura");
            }

            // Controlla che le righe di Fattura selezionate siano omogenee.
            checkRigheFattura_perAssociazione(context);

            it.cnr.jada.util.RemoteIterator ri;
//		 Crea un Iteratore sui beni disponibili ad essere associati ad una riga di Fattura

            if (bp.getDettagliFattura().getSelectedModels(context).get(0).getClass().equals(Fattura_passiva_rigaIBulk.class)) {
                Fattura_passiva_rigaIBulk modello_fattura = (Fattura_passiva_rigaIBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_fattura, null);
            }
            // questo caso non si verifica per le nota_credito
            else {
                Nota_di_debito_rigaBulk modello_nota = (Nota_di_debito_rigaBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_nota, null);
            }


            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            int count = ri.countElements();

            // Controlla che ci siano beni disponibili.
            if (count == 0) {
                bp.setMessage("Nessun Bene associabile");
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
            } else {
                SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class), null, "doSelezionaBeni_associati", null, bp);
                slbp.setMultiSelection(true);
            }

            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

	public Forward doFiltraPrezzoUnitario(ActionContext actioncontext) {
		try {
			fillModel(actioncontext);
			AssBeneFatturaBP bp = (AssBeneFatturaBP) actioncontext.getBusinessProcess();
			Selection selection = bp.getDettagliFattura().getSelection(actioncontext);
			// Controlla che l'utente abia selezionato almeno una riga di Fattura
			if (selection == null || selection.size() == 0)
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura");
			else if (selection.size() > 1)
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura per volta");
            final Optional<Fattura_passiva_rigaBulk> fatturaPassivaRigaBulk = Optional.ofNullable(bp.getDettagliFattura().getModel())
                    .filter(Fattura_passiva_rigaBulk.class::isInstance)
                    .map(Fattura_passiva_rigaBulk.class::cast);
            if (fatturaPassivaRigaBulk.isPresent()) {
                CompoundFindClause compoundFindClause = new CompoundFindClause();
                compoundFindClause.addClause(FindClause.AND, "valore_unitario", SQLBuilder.EQUALS, fatturaPassivaRigaBulk.get().getPrezzo_unitario());
                return doAddToCRUDMain_DettagliFattura_RigheDaFattura(actioncontext, compoundFindClause);
            }
            return doAddToCRUDMain_DettagliFattura_RigheDaFattura(actioncontext);
		} catch (Throwable e) {
			return handleException(actioncontext, e);
		}
	}

	public Forward doAddToCRUDMain_DettagliFattura_RigheDaFattura(ActionContext context) {
		return doAddToCRUDMain_DettagliFattura_RigheDaFattura(context, null);
	}
	/**
	 *  Associa dei Beni ad una o più righe di Fattura.
	 *	Il metodo dapprima controlla che l'utente abbia selezionato almeno una riga di fattura
	 *	a cui associare i beni: in caso negativo, provvede a mandare un messagio di errore dove
	 *	richiede di selezionare una riga di Fattura.
	 *	In seguito, il sistema controlla che le righe selezionate dall'utente siano omogenee:
	 *	controlla, cioè, che siano o tutte ISTITUZIONALI o tutte COMMERCIALI, (metodo checkRigheFattura_perAssociazione).
	 *	Infine, verifica l'esistenza di beni che abbiano le caratteristiche per essere associate
	 *	alle righe selezionate, (metodo cercaBeniAssociabili): se ci sono beni disponibili, questi
	 *	vengono visualizzati all'utente che selezionerà quelli necessari.
	 *
	 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
	 *
	 * @return forward <code>Forward</code>
	 **/
    public Forward doAddToCRUDMain_DettagliFattura_RigheDaFattura(ActionContext context, CompoundFindClause compoundFindClause) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            java.util.List selectedModels = bp.getDettagliFattura().getSelectedModels(context);
            Selection selection = bp.getDettagliFattura().getSelection(context);

            // Controlla che l'utente abia selezionato almeno una riga di Fattura
            if (selection == null || selection.size() == 0)
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura");
            else if (selection.size() > 1)
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura per volta");


            // Controlla che le righe di Fattura selezionate siano omogenee.
            checkRigheFattura_perAssociazione(context);
            it.cnr.jada.util.RemoteIterator ri;
//		 Crea un Iteratore sui beni disponibili ad essere associati ad una riga di Fattura

            if (bp.getDettagliFattura().getSelectedModels(context).get(0).getClass().equals(Fattura_passiva_rigaIBulk.class)) {
                Fattura_passiva_rigaIBulk modello_fattura = (Fattura_passiva_rigaIBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_fattura, compoundFindClause);
            } else if (bp.getDettagliFattura().getSelectedModels(context).get(0).getClass().equals(Nota_di_credito_rigaBulk.class)) {
                Nota_di_credito_rigaBulk modello_nota = (Nota_di_credito_rigaBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_nota, compoundFindClause);
            } else if (bp.getDettagliFattura().getSelectedModels(context).get(0).getClass().equals(Fattura_attiva_rigaIBulk.class)) {
                Fattura_attiva_rigaIBulk modello_nota = (Fattura_attiva_rigaIBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_nota, compoundFindClause);
            } else {
                Nota_di_debito_rigaBulk modello_nota = (Nota_di_debito_rigaBulk) bp.getDettagliFattura().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello_nota, compoundFindClause);
            }


            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            int count = ri.countElements();

            // Controlla che ci siano beni disponibili.
            if (count == 0) {
                bp.setMessage("Nessun Buono associabile");
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
            } else if (count == 1) {
                final BitSet bitSet = new BitSet(0);
                bitSet.set(0, true);
                bp.setSelection(context, new OggettoBulk[]{(OggettoBulk) ri.nextElement()}, new BitSet(), bitSet);
                doSelezionaBuoni_associati(context);
            } else {
                SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class), null, "doSelezionaBuoni_associati", null, bp);
                slbp.setMultiSelection(true);
            }

            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    protected SelezionatoreListaBP select(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1, ObjectReplacer objectreplacer, SelectionListener selectionlistener)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp;
            if (bulkinfo.getBulkClass().getName().endsWith("Buono_carico_scarico_dettBulk"))
                selezionatorelistabp = (ListaBuoniBP) actioncontext.createBusinessProcess("ListaBuoniBP");
            else
                selezionatorelistabp = (ListaBeniBP) actioncontext.createBusinessProcess("ListaBeniBP");
            selezionatorelistabp.setObjectReplacer(objectreplacer);
            selezionatorelistabp.setSelectionListener(actioncontext, selectionlistener);
            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
            selezionatorelistabp.setBulkInfo(bulkinfo);
            selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
            actioncontext.addHookForward("seleziona", this, s1);
            HookForward _tmp = (HookForward) actioncontext.findForward("seleziona");
            actioncontext.addBusinessProcess(selezionatorelistabp);
            return selezionatorelistabp;
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        }
    }

    /**
     * Richiamato dalla pressione del tasto "Annulla", provvede a cancellare tutte le associazioni
     * Richiama il metodo AssBeneFatturaBP.annullaRiporta(ActionContext).
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doAnnullaRiporta(ActionContext context) throws BusinessProcessException {

        try {
            List righe_fattura = null;
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            if (bp.isDaDocumento())
                righe_fattura = bp.getDettagliDocumento().getDetails();
            else
                righe_fattura = bp.getDettagliFattura().getDetails();
            bp.createComponentSession().annullaRiportaAssFattura_Bene(context.getUserContext(), bp.getModel(), righe_fattura);
            context.closeBusinessProcess();
            HookForward bringBackForward = (HookForward) context.findForward("bringback");
            if (bringBackForward != null)
                return bringBackForward;
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * @param context    L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRiporta(ActionContext context) {
        try {
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            fillModel(context);
            it.cnr.jada.bulk.OggettoBulk model = bp.getBringBackModel();
            bp.createComponentSession().validaRiportaAssFattura_Bene(context.getUserContext(), (Ass_inv_bene_fatturaBulk) model);
            context.closeBusinessProcess();
            HookForward bringBackForward = (HookForward) context.findForward("bringback");
            if (bringBackForward == null)
                return context.findDefaultForward();
            bringBackForward.addParameter("bringback", model);
            return bringBackForward;
        } catch (Throwable e) {
            return handleException(context, e);
        }


    }

    /**
     *  Salva il valore indicato nel campo variazione_piu.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSalvaVariazionePiu(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            if (!bp.isDaDocumento())
                bp.modificaBeneAssociatoConBulk(context, bp.getRigheInventarioDaFattura().getModel());
            else
                bp.modificaBeneAssociatoConBulk(context, bp.getRigheInventarioDaDocumento().getModel());

            //bp.getRigheInventarioDaFattura().getSelection().clearSelection();
            //bp.getRigheInventarioDaFattura().reset(context);
            //bp.getRigheInventarioDaFattura().getSelection().setSelected(bp.getRigheInventarioDaFattura().getSelection(context).getFocus());


        } catch (Exception e) {
            return handleException(context, e);
        }

        return context.findDefaultForward();
    }

    /**
     *  Richiamtao dal FrameWork quando seleziono una riga dalla finestra dei Dettagli Fattura,
     *	permette di cancellare le selezioni fatte precedentemente dal'utente, e pone il
     *	checkBox relativo alla riga selezionata nello stato di CHECKED;
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelectDettagliFattura(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getDettagliFattura().setSelection(context);
            bp.getDettagliFattura().getSelection().clearSelection();
            bp.getDettagliFattura().getSelection().setSelected(bp.getDettagliFattura().getSelection(context).getFocus());

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     *
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelectRigheInventarioDaFattura(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getRigheInventarioDaFattura().setSelection(context);
            bp.getRigheInventarioDaFattura().getSelection().clearSelection();
            bp.getRigheInventarioDaFattura().getSelection().setSelected(bp.getRigheInventarioDaFattura().getSelection(context).getFocus());
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     *
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelectRigheDaFattura(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getRigheDaFattura().setSelection(context);
            bp.getRigheDaFattura().getSelection().clearSelection();
            bp.getRigheDaFattura().getSelection().setSelected(bp.getRigheDaFattura().getSelection(context).getFocus());
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doSelectRigheDaDocumento(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getRigheDaDocumento().setSelection(context);
            bp.getRigheDaDocumento().getSelection().clearSelection();
            bp.getRigheDaDocumento().getSelection().setSelected(bp.getRigheDaDocumento().getSelection(context).getFocus());
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     *  Effettua un refresh della finestra dei beni associati ad una riga di Fattura Passiva
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelezionaBuoni_associati(ActionContext context) {
        try {
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            if (!bp.isDaDocumento())
                bp.getRigheDaFattura().reset(context);
            else
                bp.getRigheDaDocumento().reset(context);

            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doOnData_registrazioneChange(ActionContext context) {
        try {
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            if (bp.isPerAumentoValore())
                bp.getRigheInventarioDaFattura().removeAll(context);
            else if (bp.isPerAumentoValoreDoc())
                bp.getRigheInventarioDaDocumento().removeAll(context);
            fillModel(context);

            Ass_inv_bene_fatturaBulk model = (Ass_inv_bene_fatturaBulk) bp.getModel();
            bp.setModel(context, model);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);

        }
    }

    /**
     *  Richiamtao dal FrameWork quando seleziono una riga dalla finestra dei Dettagli Documento,
     *	permette di cancellare le selezioni fatte precedentemente dal'utente, e pone il
     *	checkBox relativo alla riga selezionata nello stato di CHECKED;
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelectDettagliDocumento(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getDettagliDocumento().setSelection(context);
            bp.getDettagliDocumento().getSelection().clearSelection();
            bp.getDettagliDocumento().getSelection().setSelected(bp.getDettagliDocumento().getSelection(context).getFocus());

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doAddToCRUDMain_DettagliDocumento_RigheDaDocumento(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            java.util.List selectedModels = bp.getDettagliDocumento().getSelectedModels(context);
            Selection selection = bp.getDettagliDocumento().getSelection(context);

            // Controlla che l'utente abia selezionato almeno una riga di Fattura
            if (selection == null || selection.size() == 0)
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Documento");
            else if (selection.size() > 1)
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Documento per volta");


            // Controlla che le righe di Documento selezionate siano omogenee.
            checkRigheDocumento_perAssociazione(context);
            it.cnr.jada.util.RemoteIterator ri = null;
//		 Crea un Iteratore sui beni disponibili ad essere associati ad una riga di Documento

            if (bp.getDettagliDocumento().getSelectedModels(context).get(0).getClass().equals(Documento_generico_rigaBulk.class)) {
                Documento_generico_rigaBulk modello = (Documento_generico_rigaBulk) bp.getDettagliDocumento().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello, null);
            }

            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            int count = ri.countElements();

            // Controlla che ci siano beni disponibili.
            if (count == 0) {
                bp.setMessage("Nessun Buono associabile");
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
            } else {
                SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class), null, "doSelezionaBuoni_associati", null, bp);
                slbp.setMultiSelection(true);
            }

            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Controlla che le righe selezionate, a cui verranno associate i beni, siano omogenee
     *	per tipo (ISTITUZIONALE/COMMERCIALE).
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    private void checkRigheDocumento_perAssociazione(ActionContext context) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.bulk.ValidationException {

        String tipo_riga = null;
        String bene_servizio = null;
        Documento_generico_rigaBulk doc_riga = null;
        AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
        Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) bp.getModel();
        if (ass.isPerAumentoValoreDoc() && ass.getTest_buono().getData_registrazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: bisogna specificare prima la data registrazione.");
        List dettagliDocumento;
        try {
            dettagliDocumento = bp.getDettagliDocumento().getSelectedModels(context);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ApplicationException(e.getMessage(), e);
        }
        for (Iterator i = dettagliDocumento.iterator(); i.hasNext(); ) {
            if (dettagliDocumento.get(0).getClass().equals(Documento_generico_rigaBulk.class))
                doc_riga = (Documento_generico_rigaBulk) i.next();
            // Controlla il tipo, (IST/COM), di riga.
            if (tipo_riga == null && doc_riga != null)
                tipo_riga = doc_riga.getDocumento_generico().getTi_istituz_commerc();
            else if (doc_riga != null && !tipo_riga.equalsIgnoreCase(doc_riga.getDocumento_generico().getTi_istituz_commerc()))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile selezionare righe di Documento di Tipo diverso.");

        }
    }

    public Forward doAddToCRUDMain_DettagliDocumento_RigheInventarioDaDocumento(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            java.util.List selectedModels = bp.getDettagliDocumento().getSelectedModels(context);
            Selection selection = bp.getDettagliDocumento().getSelection(context);

            // Controlla che l'utente abia selezionato almeno una riga di Documento
            if (selection == null || selection.size() == 0) {
                throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una o più righe di Documento");
            }

            // Controlla che le righe di Documento selezionate siano omogenee.
            checkRigheDocumento_perAssociazione(context);

            it.cnr.jada.util.RemoteIterator ri = null;
//		 Crea un Iteratore sui beni disponibili ad essere associati ad una riga di Documento

            if (bp.getDettagliDocumento().getSelectedModels(context).get(0).getClass().equals(Documento_generico_rigaBulk.class)) {
                Documento_generico_rigaBulk modello = (Documento_generico_rigaBulk) bp.getDettagliDocumento().getSelectedModels(context).get(0);
                ri = bp.createComponentSession().cercaBeniAssociabili(context.getUserContext(), (Ass_inv_bene_fatturaBulk) bp.getModel(), modello, null);
            }
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            int count = ri.countElements();

            // Controlla che ci siano beni disponibili.
            if (count == 0) {
                bp.setMessage("Nessun Bene associabile");
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
            } else {
                SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class), null, "doSelezionaBeni_associati", null, bp);
                slbp.setMultiSelection(true);
            }

            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSelectRigheInventarioDaDocumento(ActionContext context) {

        try {
            fillModel(context);
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            bp.getRigheInventarioDaDocumento().setSelection(context);
            bp.getRigheInventarioDaDocumento().getSelection().clearSelection();
            bp.getRigheInventarioDaDocumento().getSelection().setSelected(bp.getRigheInventarioDaDocumento().getSelection(context).getFocus());
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     *  Effettua un refresh della finestra dei beni associati ad una riga di Fattura Passiva
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     *
     * @return forward <code>Forward</code>
     **/
    public Forward doSelezionaBeni_associati(ActionContext context) {
        try {
            AssBeneFatturaBP bp = (AssBeneFatturaBP) context.getBusinessProcess();
            if (!bp.isDaDocumento())
                bp.getRigheInventarioDaFattura().reset(context);
            else
                bp.getRigheInventarioDaDocumento().reset(context);

            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
}
