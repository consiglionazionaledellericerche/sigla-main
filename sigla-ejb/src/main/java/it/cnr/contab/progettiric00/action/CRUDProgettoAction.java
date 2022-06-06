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

package it.cnr.contab.progettiric00.action;

import it.cnr.contab.config00.bp.CRUDConfigAnagContrattoBP;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.progettiric00.bp.AmministraTestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.ProgettoAlberoBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.action.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Azione che gestisce le richieste relative alla Gestione Progetto Risorse
 * (Progetto)
 */
public class CRUDProgettoAction extends CRUDAbstractProgettoAction {
    public CRUDProgettoAction() {
        super();
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDBP bp = getBusinessProcess(context);
            if (bp instanceof TestataProgettiRicercaBP && ((TestataProgettiRicercaBP) bp).isFlNuovoPdg())
                return doConfirmElimina(context, OptionBP.YES_BUTTON);
            return openConfirm(context, "Attenzione i Finanziatori del progetto, le UO partecipanti ed i Post-It saranno persi, vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmElimina");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfirmElimina(ActionContext context, int option) throws java.rmi.RemoteException {
        try {
            if (option == OptionBP.YES_BUTTON) {
                return super.doElimina(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * E' stata generata la richiesta di cercare un Progetto che sia padre del Progetto
     * che si sta creando.
     * Il metodo antepone alla descrizione specificata dall'utente, quella del Progetto selezionato
     * come padre.
     * In caso di modifica di un Progetto esistente sul DB, il sistema controlla che il Progetto
     * selezionato dall'utente non sia la stesso che sta modificando.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/

    public Forward doBringBackSearchFind_nodo_padre(ActionContext context, ProgettoBulk progetto, ProgettoBulk progetto_padre) throws java.rmi.RemoteException {

        if (progetto_padre != null) {
            // L'utente ha selezionato come Progetto padre il Progetto che sta modificando
            if (progetto_padre.getCd_progetto().equals(progetto.getCd_progetto())) {
                setErrorMessage(context, "Attenzione: non è possibile selezionare come padre il Progetto stesso");
                return context.findDefaultForward();
            }
            /* riporto le informazioni ereditate dal progetto padre */
            CRUDBP bp = getBusinessProcess(context);
            if (((TestataProgettiRicercaBP) bp).getLivelloProgetto() != ((progetto_padre.getLivello()).intValue() + 1)) {
                setErrorMessage(context, "Attenzione: il codice inserito non è del tipo richiesto");
                return context.findDefaultForward();
            }
            if (bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT) {
                if (progetto.getDs_progetto() != null) {
                    if (progetto.getDs_progetto().indexOf(progetto_padre.getDs_progetto()) == -1)
                        progetto.setDs_progetto(progetto_padre.getDs_progetto() + " - " + progetto.getDs_progetto());
                } else
                    progetto.setDs_progetto(progetto_padre.getDs_progetto());

                progetto.setTipo(progetto_padre.getTipo());
                progetto.setStato(progetto_padre.getStato());
                progetto.setDt_inizio(progetto_padre.getDt_inizio());
                progetto.setLivello(new Integer(progetto_padre.getLivello().intValue() + 1));
                // se il padre è una commessa proponiamo anche i seguenti:
                if (progetto_padre.getLivello().equals(new Integer(2))) {
                    progetto.setDt_fine(progetto_padre.getDt_fine());
                    progetto.setDurata_progetto(progetto_padre.getDurata_progetto());
                }
            }
            progetto.setProgettopadre(progetto_padre);
        }

        return context.findDefaultForward();
    }

    public Forward doFreeSearchFind_nodo_padre(ActionContext context) {
        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
        ProgettoBulk progetto = (ProgettoBulk) bp.getModel();
        progetto.setProgettopadre(new ProgettoBulk());
        return freeSearch(context, getFormField(context, "main.find_nodo_padre"), progetto.getProgettopadre());
    }

    /**
     * E' stata generata la richiesta di cercare un Progetto che sia padre della Progetto
     * che si sta creando.
     * Il metodo controlla se l'utente ha indicato nel campo codice del Progetto padre un
     * valore: in caso affermativo, esegue una ricerca mirata per trovare esattamente il codice
     * indicato; altrimenti, apre un <code>SelezionatoreListaAlberoBP</code> che permette all'utente
     * di cercare il nodo padre scorrendo i Progetti secondo i vari livelli.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/
    public it.cnr.jada.action.Forward doSearchFind_nodo_padre(ActionContext context) {

        try {

            TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
            if ("TestataProgettiRicercaBP".equals(bp.getName()))
                return search(context, getFormField(context, "main.find_nodo_padre"), "filtro_ricerca_aree_short");

            ProgettoBulk progetto = (ProgettoBulk) bp.getModel();

            String cd = null;

            if (progetto.getProgettopadre() != null)
                cd = progetto.getProgettopadre().getCd_progetto();

            if (cd != null) {
                if (cd.equals(progetto.getCd_progetto())) {
                    return handleException(context, new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare come nodo padre il progetto corrente"));
                } else {
                    // L'utente ha indicato un codice da cercare: esegue una ricerca mirata.
                    return search(context, getFormField(context, "main.find_nodo_padre"), null);
                }
            }

            it.cnr.jada.util.RemoteIterator roots = bp.getProgettiTree(context).getChildren(context, null);
            // Non ci sono Progetti disponibili ad essere utiilzzati come nodo padre
            if (roots.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, roots);
                setErrorMessage(context, "Attenzione: non sono stati trovati Progetti disponibili");
                return context.findDefaultForward();
            } else {
                // Apre un Selezionatore ad Albero per cercare i Progetti selezionando i vari livelli
                ProgettoAlberoBP slaBP = (ProgettoAlberoBP) context.createBusinessProcess("ProgettoAlberoBP");
                slaBP.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(ProgettoBulk.class));
                if (bp.isFlNuovoPdg())
                    slaBP.setColumns(slaBP.getBulkInfo().getColumnFieldPropertyDictionary("nuovoPdgLiv1"));
                slaBP.setRemoteBulkTree(context, bp.getProgettiTree(context), roots);
                HookForward hook = (HookForward) context.addHookForward("seleziona", this, "doBringBackSearchResult");
                hook.addParameter("field", getFormField(context, "main.find_nodo_padre"));
                context.addBusinessProcess(slaBP);
                return slaBP;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * E' stata generata la richiesta di cercare un Progetto che sia padre del Progetto
     * che si sta creando.
     * Il metodo antepone alla descrizione specificata dall'utente, quella del Progetto selezionato
     * come padre.
     * In caso di modifica di una Progetto esistente sul DB, il sistema controlla che il Progetto
     * selezionato dall'utente non sia la stesso che sta modificando.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/

    public it.cnr.jada.action.Forward OLDdoBringBackSearchResult(ActionContext context) throws java.rmi.RemoteException {

        HookForward caller = (HookForward) context.getCaller();
        ProgettoBulk ubi = (ProgettoBulk) ((TestataProgettiRicercaBP) getBusinessProcess(context)).getModel();
        ProgettoBulk ubiPadre = (ProgettoBulk) caller.getParameter("focusedElement");

        if (ubiPadre != null) {
            // L'utente ha selezionato come Progetto padre il Progetto che sta modificando
            if (ubiPadre.getCd_progetto().equals(ubi.getCd_progetto())) {
                setErrorMessage(context, "Attenzione: non è possibile selezionare come padre il Progetto stesso");
                return context.findDefaultForward();
            }
            if (ubi.isToBeCreated())
                ubi.setDs_progetto(ubiPadre.getDs_progetto() + " - " + ubi.getDs_progetto());
        }

        return super.doBringBackSearchResult(
                context,
                (FormField) caller.getParameter("field"),
                ubiPadre);
    }

    public it.cnr.jada.action.Forward doBringBackSearchTipoFinanziamentoOf(ActionContext context, ProgettoBulk progetto, TipoFinanziamentoBulk tipoFinanziamento) throws java.rmi.RemoteException {
        if (tipoFinanziamento != null) {
			if (!tipoFinanziamento.getFlPianoEcoFin() && progetto.isDettagliPianoEconomicoPresenti()) {
				setErrorMessage(context, "Attenzione: non è possibile selezionare un tipo finanziamento che non prevede il piano economico essendo presente un piano economico sul progetto.");
				return context.findDefaultForward();
			}
			if (!tipoFinanziamento.getFlAttivo()) {
				setErrorMessage(context, "Attenzione: non è possibile selezionare un tipo finanziamento non abilitato all'associazione ai progetti.");
				return context.findDefaultForward();
			}
        }
    	progetto.getOtherField().setTipoFinanziamento(tipoFinanziamento);
    	if (!progetto.isDatePianoEconomicoRequired()) {
    		progetto.getOtherField().setDtInizio(null);
    		progetto.getOtherField().setDtFine(null);
    		progetto.getOtherField().setDtProroga(null);
    	}
    	if (progetto.isPianoEconomicoRequired() && progetto.getOtherField().isStatoIniziale() && progetto.getDettagliPianoEconomicoTotale().isEmpty()) {
    		progetto.getOtherField().setImFinanziato(BigDecimal.ZERO);
    		progetto.getOtherField().setImCofinanziato(BigDecimal.ZERO);
    	}
        return context.findDefaultForward();
    }
    
    public it.cnr.jada.action.Forward doBringBackSearchVoce_piano(ActionContext context, Progetto_piano_economicoBulk progettoPiaeco, Voce_piano_economico_prgBulk vocePiaeco) throws java.rmi.RemoteException {
    	try {
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
	        progettoPiaeco.setVoce_piano_economico(vocePiaeco);
	        bp.caricaVociPianoEconomicoAssociate(context,progettoPiaeco);
	        return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }	        
    }

    public it.cnr.jada.action.Forward doBringBackSearchVoce_piano_amm(ActionContext context, Progetto_piano_economicoBulk progettoPiaeco, Voce_piano_economico_prgBulk vocePiaeco) throws java.rmi.RemoteException {
    	return doBringBackSearchVoce_piano(context, progettoPiaeco, vocePiaeco);
    }
    
    public Forward doNegoziazioneOf(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Il progetto sarà messo in stato \"NEGOZIAZIONE\". Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmNegoziazioneOf");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmNegoziazioneOf(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
				bp.changeStato(context,StatoProgetto.STATO_NEGOZIAZIONE.value());
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doApprovaOf(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Il progetto sarà messo in stato \"APPROVATO\". Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmApprovaOf");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmApprovaOf(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
				bp.changeStato(context,StatoProgetto.STATO_APPROVATO.value());
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doAnnullaOf(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Il progetto sarà messo in stato \"ANNULLATO\". Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaOf");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmAnnullaOf(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
				bp.changeStato(context,StatoProgetto.STATO_ANNULLATO.value());
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doChiusuraOf(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Il progetto sarà messo in stato \"CHIUSO\" e verrà impostata la data di fine uguale alla data odierna. "
        			+ "Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmChiusuraOf");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmChiusuraOf(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
				bp.changeStato(context,StatoProgetto.STATO_CHIUSURA.value());
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doOnDtInizioOfChange(ActionContext context) {
		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
				.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

		Optional<Progetto_other_fieldBulk> optOtherField = 
				optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField()));

		Optional<Timestamp> optData = optOtherField.flatMap(el->Optional.ofNullable(el.getDtInizio()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optProgetto.isPresent())
				optProgetto.get().validaDateProgetto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optOtherField.get().setDtInizio(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}

	public Forward doOnDtFineOfChange(ActionContext context) {
		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
				.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

		Optional<Progetto_other_fieldBulk> optOtherField = 
				optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField()));

		Optional<Timestamp> optData = optOtherField.flatMap(el->Optional.ofNullable(el.getDtFine()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optProgetto.isPresent())
				optProgetto.get().validaDateProgetto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optOtherField.get().setDtFine(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}

	public Forward doOnDtProrogaOfChange(ActionContext context) {
		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
				.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

		Optional<Progetto_other_fieldBulk> optOtherField = 
				optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField()));

		Optional<Timestamp> optData = optOtherField.flatMap(el->Optional.ofNullable(el.getDtProroga()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optProgetto.isPresent())
				optProgetto.get().validaDateProgetto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optOtherField.get().setDtProroga(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}

	public Forward doOnDtInizioFideiussioneOfChange(ActionContext context) {
		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
				.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

		Optional<Progetto_other_fieldBulk> optOtherField = 
				optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField()));

		Optional<Timestamp> optData = optOtherField.flatMap(el->Optional.ofNullable(el.getDtInizioFideiussione()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optOtherField.isPresent())
				optOtherField.get().validaDateFideiussioneProgetto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optOtherField.get().setDtInizioFideiussione(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}

	public Forward doOnDtFineFideiussioneOfChange(ActionContext context) {
		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
				.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

		Optional<Progetto_other_fieldBulk> optOtherField = 
				optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField()));

		Optional<Timestamp> optData = optOtherField.flatMap(el->Optional.ofNullable(el.getDtFineFideiussione()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optOtherField.isPresent())
				optOtherField.get().validaDateFideiussioneProgetto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optOtherField.get().setDtFineFideiussione(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}
	
	public Forward doRiapriOf(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Il progetto sarà riaperto. "
        			+ "Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmRiapriOf");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmRiapriOf(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(context);
				bp.changeStato(context,ProgettoBulk.STATO_RIAPERTURA);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	
	public Forward doRimodula(ActionContext context){
		try 
		{
			fillModel( context );
	        TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
			
			Optional<ProgettoBulk> optProgetto = Optional.ofNullable(bp.getModel())
					.filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);

	        Optional<Progetto_rimodulazioneBulk> lastRim = optProgetto.get().getRimodulazioni().stream()
					.filter(el->!el.isStatoRespinto())
					.sorted(Comparator.comparing(Progetto_rimodulazioneBulk::getPg_rimodulazione).reversed())
					.findFirst();

			if (lastRim.filter(el->el.isStatoProvvisorio()||el.isStatoDefinitivo()||el.isStatoValidato()).isPresent())
				return openConfirm(context, "Attenzione! Si vuole accedere alla rimodulazione in corso del progetto?", 
	        			OptionBP.CONFIRM_YES_NO, "doConfirmRimodula");
			else
				return openConfirm(context, "Attenzione! Si vuole procedere alla rimodulazione del progetto?", 
        			OptionBP.CONFIRM_YES_NO, "doConfirmRimodula");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmRimodula(ActionContext context,int option) {
		try 
		{
			if (option == OptionBP.YES_BUTTON) {
				TestataProgettiRicercaBP bp= (TestataProgettiRicercaBP) getBusinessProcess(context);
				String function = bp.isEditable() ? "M" : "V";
				function += "R";

				ProgettoBulk progetto = (ProgettoBulk)bp.getModel();

				RimodulaProgettiRicercaBP newbp = null;
				// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","RimodulaProgettiRicercaBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di rimodulazione progetti. Impossibile continuare.");

				newbp = (RimodulaProgettiRicercaBP) context.getUserInfo().createBusinessProcess(context,"RimodulaProgettiRicercaBP",new Object[] { function,  progetto});
				newbp.setBringBack(true);
				context.addHookForward("bringback", this, "doBringBackRimodula");
				return context.addBusinessProcess(newbp);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	
    public Forward doBringBackRimodula(ActionContext context) {
        try {
        	if (Optional.ofNullable(getBusinessProcess(context)).map(TestataProgettiRicercaBP.class::isInstance).orElse(Boolean.FALSE)) {
	        	HookForward caller = (HookForward)context.getCaller();
	        	Progetto_rimodulazioneBulk rim = (Progetto_rimodulazioneBulk)caller.getParameter("bringback");
		    	TestataProgettiRicercaBP bp= (TestataProgettiRicercaBP) getBusinessProcess(context);
	            ProgettoBulk progetto = (ProgettoBulk) bp.getModel();
				if (Optional.ofNullable(rim).map(Progetto_rimodulazioneBulk::isStatoApprovato).orElse(Boolean.TRUE)) {
					bp.basicEdit(context, progetto,Boolean.TRUE);
				} else {
		            List<Progetto_rimodulazioneBulk> listRimodulazioni = bp.createComponentSession().find(context.getUserContext(), ProgettoBulk.class, "findRimodulazioni", progetto.getPg_progetto());
		            progetto.setRimodulazioni(new BulkList<Progetto_rimodulazioneBulk>(listRimodulazioni));
				}
        	}
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
	
	public Forward doOpenContratto(ActionContext context, String s)
	{
		try 
		{
			fillModel( context );
			CRUDController crudController = getController(context, s);

			TestataProgettiRicercaBP bp= (TestataProgettiRicercaBP) getBusinessProcess(context);

			// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
			String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDConfigAnagContrattoBP");
			if (mode == null) 
				throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa del contratto. Impossibile continuare.");

			if (!Optional.ofNullable(crudController.getModel()).isPresent())
				throw new it.cnr.jada.action.MessageToUser("Selezionare il contratto al quale si vuole accesso.");

			CRUDConfigAnagContrattoBP newbp = (CRUDConfigAnagContrattoBP) context.getUserInfo().createBusinessProcess(context,"CRUDConfigAnagContrattoBP",new Object[] { "V", (ContrattoBulk)crudController.getModel(), "V"});
			return context.addBusinessProcess(newbp);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
    public Forward doPrintSintetica(ActionContext actioncontext)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmPrintSintetica");
            else
                return doConfirmPrintSintetica(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
    
    public Forward doConfirmPrintSintetica(ActionContext actioncontext, int i)
    {
        try
        {
            if(i == 4)
            {
            	TestataProgettiRicercaBP bulkbp = (TestataProgettiRicercaBP)actioncontext.getBusinessProcess();
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess(bulkbp.getPrintbp());
                bulkbp.initializePrintSinteticaBP((AbstractPrintBP)businessprocess);
                if (bulkbp.getTransactionPolicy()!= BusinessProcess.IGNORE_TRANSACTION)
                	actioncontext.closeBusinessProcess(bulkbp);
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

	public Forward doRemovePianoEconomico(ActionContext actioncontext)
	{
		try
		{
			BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
			fillModel(actioncontext);
			if (bulkbp instanceof AmministraTestataProgettiRicercaBP) {
				if (bulkbp.isDirty()) {
					openMessage(actioncontext, "Operazione non consentita! Confermare o annullare le modifiche effettuate prima di procedere ad una nuova operazione!");
					return actioncontext.findDefaultForward();
				} else
					return openConfirm(actioncontext, "L'operazione richiesta comporta l'annullamento del piano economico anche sui bilanci preventivi dove eventualmente inserito. Vuoi continuare?",
							OptionBP.CONFIRM_YES_NO, "doConfirmRemovePianoEconomico");
			} else {
				openMessage(actioncontext, "Operazione non consentita!");
				return actioncontext.findDefaultForward();
			}
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}

	public Forward doConfirmRemovePianoEconomico(ActionContext actioncontext, int i)
	{
		try
		{
			if(i == OptionBP.YES_BUTTON)
			{
				TestataProgettiRicercaBP bulkbp = (TestataProgettiRicercaBP)actioncontext.getBusinessProcess();
				bulkbp.removePianoEconomico(actioncontext);
				openMessage(actioncontext, "Operazione effettuata! E' stato inserito un tipo di finanziamento che non prevede piano economico. Verificare la correttezza dello stesso ed eventualmente procedere al suo aggiornamento!");
			}
			return actioncontext.findDefaultForward();
		}
		catch(BusinessProcessException businessprocessexception)
		{
			return handleException(actioncontext, businessprocessexception);
		}
	}

	public Forward doOnEsercizioPianoChange(ActionContext actioncontext) {
		try {
			fillModel(actioncontext);
			TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)getBusinessProcess(actioncontext);
			Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAltriAnni())
					.flatMap(el->Optional.ofNullable(el.getModel()))
					.filter(Progetto_piano_economicoBulk.class::isInstance)
					.map(Progetto_piano_economicoBulk.class::cast);

			if (optPpe.isPresent() && optPpe.map(Progetto_piano_economicoBulk::isAnnoPianoEconomicoMinoreAnnoInizio).orElse(Boolean.FALSE))
				optPpe.get().setIm_spesa_finanziato(BigDecimal.ZERO);
			return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}
}

