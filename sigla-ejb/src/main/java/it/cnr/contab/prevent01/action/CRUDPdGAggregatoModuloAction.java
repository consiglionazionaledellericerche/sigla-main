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

/*
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bp.CRUDPdGAggregatoModuloBP;
import it.cnr.contab.prevent01.bp.CRUDStatoCdrPdGPBP;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.progettiric00.bp.ProgettoAlberoLABP;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.*;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdGAggregatoModuloAction extends CRUDAction  {

	public Forward doFreeSearchSearchtool_progetto(ActionContext context) {
		try{
			Progetto_sipBulk progetto = new Progetto_sipBulk();
			progetto.setProgettopadre(new Progetto_sipBulk());
			return freeSearch(context, getFormField(context, "main.Dettagli.searchtool_progetto"), progetto);
		} catch(Throwable e){
			return handleException(context, e);
		}
	}

	public it.cnr.jada.action.Forward doSearchSearchtool_progetto(ActionContext context) {

		try{
			fillModel(context);

			CRUDPdGAggregatoModuloBP bpmod = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
			Pdg_moduloBulk dettaglio = (Pdg_moduloBulk) bpmod.getCrudDettagli().getModel();

			if (Optional.ofNullable(dettaglio)
					.filter(pdg_moduloBulk -> Optional.ofNullable(pdg_moduloBulk.getProgetto()).isPresent())
					.filter(progetto_sipBulk -> Optional.ofNullable(progetto_sipBulk.getProgetto().getCd_progetto()).isPresent() ||
							Optional.ofNullable(progetto_sipBulk.getProgetto().getDs_progetto()).isPresent()).isPresent()) {
				// L'utente ha indicato un codice da cercare: esegue una ricerca mirata.
				return search(context, getFormField(context, "main.Dettagli.searchtool_progetto"),null);
			}

			TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)context.createBusinessProcess("TestataProgettiRicercaBP");
			context.addBusinessProcess(bp);

			it.cnr.jada.util.RemoteIterator roots = roots = bp.getProgetti_sipTree(context).getChildren(context,null);
			// Non ci sono Commesse disponibili
			if (roots.countElements()==0){
				context.closeBusinessProcess();
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, roots);
				setErrorMessage(context,"Attenzione: non sono stati trovati progetti disponibili");
				return context.findDefaultForward();
			}else {
				context.closeBusinessProcess();
				// Apre un Selezionatore ad Albero per cercare i Progetti selezionando i vari livelli
				ProgettoAlberoLABP slaBP = (ProgettoAlberoLABP)context.createBusinessProcess("ProgettoAlberoLABP",
						new Object[] { bpmod.getParametriCnr().getLivelloProgetto()});
				slaBP.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Progetto_sipBulk.class));
				slaBP.setRemoteBulkTree(context,bp.getProgetti_sipTree(context),roots);
				if (bpmod.getParametriCnr().getFl_nuovo_pdg())
					slaBP.setColumns( slaBP.getBulkInfo().getColumnFieldPropertyDictionary("progetto_liv1"));
				else
					slaBP.setColumns( slaBP.getBulkInfo().getColumnFieldPropertyDictionary("progetti_sip"));
				HookForward hook = (HookForward)context.addHookForward("seleziona",this,"doBringBackSearchResult");
				hook.addParameter("field",getFormField(context,"main.Dettagli.searchtool_progetto"));
				context.addBusinessProcess(slaBP);
				return slaBP;
			}
		} catch(Throwable e){
			return handleException(context, e);
		}
	}

	public Forward doFreeSearchSearchtool_progetto_liv2(ActionContext context) {
		return doFreeSearchSearchtool_progetto(context);
	}

	public it.cnr.jada.action.Forward doSearchSearchtool_progetto_liv2(ActionContext context) {
		return 	doSearchSearchtool_progetto(context);
	}

	public it.cnr.jada.action.Forward doBringBackSearchSearchtool_progetto_liv2(ActionContext context, Pdg_moduloBulk linea, Progetto_sipBulk progetto) throws BusinessProcessException {
		return doBringBackSearchSearchtool_progetto(context, linea, progetto);
	}

	public it.cnr.jada.action.Forward doBringBackCRUDSearchtool_progetto_liv2(ActionContext context, Pdg_moduloBulk pdgModuloBulk, ProgettoBulk progetto) throws BusinessProcessException {
        CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		return doBringBackSearchSearchtool_progetto(context, pdgModuloBulk,
				Optional.ofNullable(progetto)
					.map(progettoBulk -> new Progetto_sipBulk(progettoBulk.getEsercizio(), progettoBulk.getPg_progetto(), progettoBulk.getTipo_fase()))
                        .map(progetto_sipBulk -> {
                            try {
                                return Optional.ofNullable(bp.getProgettoRicercaPadreComponentSession().findByPrimaryKey(context.getUserContext(), progetto_sipBulk))
                                        .filter(Progetto_sipBulk.class::isInstance)
                                        .map(Progetto_sipBulk.class::cast)
                                        .orElse(null);
                            } catch (ComponentException|RemoteException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        })
					.orElse(null)
		);
	}

	public it.cnr.jada.action.Forward doBringBackSearchSearchtool_progetto(ActionContext context, Pdg_moduloBulk pdgModuloBulk, Progetto_sipBulk progetto) throws BusinessProcessException {
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();

		// valore di default nel caso non fose valorizzato
		String columnDescription="Codice Modulo di Attività";

		// nome del campo nel file xml
		final String propName="cd_progetto";
		FieldProperty property = BulkInfo.getBulkInfo(pdgModuloBulk.getClass()).getFieldProperty(propName);
		if (property != null)
			columnDescription = property.getLabel();

		if (bp.getParametriCnr().getFl_nuovo_pdg())
			columnDescription="Codice Progetto";

		if (Optional.ofNullable(progetto).isPresent()) {
			if (((CdrBulk)bp.getModel()).getDettagli()!=null) {
				for (Iterator iterator = ((CdrBulk)bp.getModel()).getDettagli().iterator(); iterator.hasNext();) {
					Pdg_moduloBulk modulo = (Pdg_moduloBulk) iterator.next();
					if (modulo.getCrudStatus() != OggettoBulk.UNDEFINED && modulo.getCrudStatus() != OggettoBulk.TO_BE_CREATED && modulo.getPg_progetto()!=null && modulo.getPg_progetto().equals(progetto.getPg_progetto())) {
						setErrorMessage(context,"Attenzione: il valore immesso in "+columnDescription+" è già presente!");
						return context.findDefaultForward();
					}
				}
			}
			if (progetto.getLivello()==null || !progetto.getLivello().equals(bp.getParametriCnr().getLivelloProgetto())) {
				setErrorMessage(context,"Attenzione: il valore immesso in "+columnDescription+" non è valido!");
				return context.findDefaultForward();
			}
			if (!Optional.ofNullable(progetto.getOtherField())
					.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getStato()))
					.filter(stato -> Arrays.asList(StatoProgetto.STATO_NEGOZIAZIONE.value(), StatoProgetto.STATO_APPROVATO.value()).indexOf(stato) != -1).isPresent()) {
				if (!progetto.getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()))) {
					setErrorMessage(context,"Attenzione: il progetto non ha uno stato utile alla previsione! Deve essere completatato dalla UO responsabile!");
					return context.findDefaultForward();
				} else {
					bp.setProgettoForUpdate(progetto);
					return openConfirm(context,"Attenzione: il progetto non ha uno stato utile alla previsione! Vuoi completare le informazioni mancanti?",
							OptionBP.CONFIRM_YES_NO,"doConfermaCompletaProgetto");
				}
			}
			if (!Optional.ofNullable(progetto.getOtherField())
					.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
					.filter(tipoFinanziamentoBulk -> tipoFinanziamentoBulk.getFlPrevEntSpesa() || tipoFinanziamentoBulk.getFlRipCostiPers()).isPresent()) {
				setErrorMessage(context,"Attenzione: per il progetto non è consentita la previsione!");
				return context.findDefaultForward();
			}
		}
		pdgModuloBulk.setProgetto(progetto);
		return context.findDefaultForward();
	}

    public Forward doConfermaCompletaProgetto(ActionContext context, int opt) throws RemoteException, BusinessProcessException {
        CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP) context.getBusinessProcess();
        if (opt == OptionBP.YES_BUTTON) {
            doCRUD(context, "main.Dettagli.searchtool_progetto_liv2");
            TestataProgettiRicercaBP testataProgettiRicercaBP = (TestataProgettiRicercaBP) context.getBusinessProcess();
            ProgettoBulk progettoBulk = new ProgettoBulk(CNRUserContext.getEsercizio(context.getUserContext()), bp.getProgettoForUpdate().getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE);
            progettoBulk = Optional.ofNullable(progettoBulk)
                    .map(progetto -> {
                        try {
                            return Optional.ofNullable(bp.getProgettoRicercaPadreComponentSession().findByPrimaryKey(context.getUserContext(), progetto))
                                    .filter(ProgettoBulk.class::isInstance)
                                    .map(ProgettoBulk.class::cast)
                                    .orElse(null);
                        } catch (ComponentException | RemoteException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    })
                    .orElse(null);
            if (Optional.ofNullable(progettoBulk).isPresent()) {
                testataProgettiRicercaBP.basicEdit(context, progettoBulk, true);
            }
        }
        return context.findDefaultForward();
    }

	public it.cnr.jada.action.Forward doFilterCRUDMain_Dettagli(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			CRUDPdGAggregatoModuloBP _tmp = (CRUDPdGAggregatoModuloBP) context.getBusinessProcess();
			RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP) context.createBusinessProcess("RicercaLibera");
			ricercaliberabp.setFreeSearchSet("prg_liv2");
			ricercaliberabp.setPrototype(new Pdg_moduloBulk());
			context.addHookForward("filter", this, "doBringBackFilter");
			HookForward hookforward = (HookForward) context.findForward("filter");
			hookforward.addParameter("controller", _tmp.getCrudDettagli());
			return context.addBusinessProcess(ricercaliberabp);
		} catch (Exception exception) {
			return handleException(context, exception);
		}
	}

	public it.cnr.jada.action.Forward doCambiaStato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

		try {
			fillModel(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		String message = "Lo stato del Piano di Gestione Preliminare per il modulo di attività verrà cambiato.\n"
						+ "Vuoi continuare?";
		if (bp.getParametriCnr().getFl_nuovo_pdg())
			message = "Lo stato del Piano di Gestione Preliminare per il progetto verrà cambiato.\n"
					+ "Vuoi continuare?";
		return openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doCambiaStatoConfermato");
	}

	public Forward doCambiaStatoConfermato(ActionContext context, int opt) throws it.cnr.jada.action.BusinessProcessException {

		if (opt == OptionBP.YES_BUTTON) {
			try {
				CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
				fillModel(context);

				// controlliamo che gli stati delle righe selezionate siano tra loro congruenti
				List listaSel = bp.getCrudDettagli().getSelectedModels(context);
				String oldStato = ((Pdg_moduloBulk)(bp.getCrudDettagli().getModel())).getStato();
				String newStato = ((Pdg_moduloBulk)(bp.getCrudDettagli().getModel())).getCambia_stato();
				if (!listaSel.isEmpty()) {
					for (Iterator it=listaSel.iterator();it.hasNext();) {
						Pdg_moduloBulk mod = (Pdg_moduloBulk) it.next();
						if (!mod.getStato().equals(oldStato)) {
							setErrorMessage(context,"Attenzione: le righe selezionate devono avere lo stesso stato attuale! Deselezionare il progetto "+mod.getProgetto().getCd_progetto()+".");
							return context.findDefaultForward();
						}
					}

					Selection sel = bp.getCrudDettagli().getSelection();
					for (Iterator it=sel.iterator();it.hasNext();) {
						Integer iSel=(Integer)it.next();
						Pdg_moduloBulk mod = (Pdg_moduloBulk) bp.getCrudDettagli().getDetails().get(iSel.intValue());
						mod.setCambia_stato(newStato);
						try {
							((PdgAggregatoModuloComponentSession)bp.createComponentSession()).modificaStatoPdg_aggregato(context.getUserContext(),mod);
							sel.removeFromSelection(iSel);
							bp.getCrudDettagli().setSelection(context, sel);
						} catch(Throwable e) {
							bp.cerca(context);
							setErrorMessage(context,"Modulo di attività "+mod.getCd_progetto()+". "+e.getMessage());
							return context.findDefaultForward();
						}
					}
				}
				else {
					try {
						((PdgAggregatoModuloComponentSession)bp.createComponentSession()).modificaStatoPdg_aggregato(context.getUserContext(),(Pdg_moduloBulk)bp.getCrudDettagli().getModel());
						bp.cerca(context);
					} catch(Throwable e) {
						bp.cerca(context);
						throw e;
					}
				}
				return context.findDefaultForward();
			} catch(Throwable e) {
				return handleException(context,e);
			}
		}
		return context.findDefaultForward();
	}

	public it.cnr.jada.action.Forward doContrattazioneEntrate(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		Pdg_moduloBulk pdg_mod = (Pdg_moduloBulk)bp.getCrudDettagli().getModel();
		try {
			fillModel(context);
			if (bp.isDirty()) {
				setErrorMessage(context,"Attenzione: è necessario salvare le modifiche effettuate!");
				return context.findDefaultForward();
			}
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDPdg_Modulo_EntrateBP",
							new Object[] {
								bp.isEditable() && !bp.isROModuloEntrate() ? "M" : "V",
								pdg_mod.getEsercizio(),
								pdg_mod.getCdr(),
								pdg_mod.getProgetto()
							}
						);
			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doContrattazioneSpese(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		Pdg_moduloBulk pdg_mod = (Pdg_moduloBulk)bp.getCrudDettagli().getModel();
		try {
			fillModel(context);
			if (bp.isDirty()) {
				setErrorMessage(context,"Attenzione: è necessario salvare le modifiche effettuate!");
				return context.findDefaultForward();
			}
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDDettagliModuloCostiBP",
							new Object[] {
								bp.isEditable() && !bp.isROContrattazioni() ? "M" : "V",
								pdg_mod
							}
						);
			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doInserisciModuli(ActionContext context) {
		try {
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP) getBusinessProcess(context);
			if (bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT) {
				it.cnr.jada.util.RemoteIterator ri = bp.search(context, new CompoundFindClause(), null);
				if (ri == null || ri.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
					bp.setMessage("La ricerca non ha fornito alcun risultato.");
					return context.findDefaultForward();
				} else {
					SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
					nbp.setIterator(context,ri);
					nbp.setMultiSelection(true);
					nbp.setBulkInfo(new Progetto_sipBulk().getBulkInfo());
					nbp.setFormField(new FormField(nbp, new Pdg_moduloBulk().getBulkInfo().getFieldProperty("progetto"), new Progetto_sipBulk()));
					if (bp instanceof CRUDPdGAggregatoModuloBP && ((CRUDPdGAggregatoModuloBP)bp).getParametriCnr().getFl_nuovo_pdg()) {
						nbp.setColumns(nbp.getBulkInfo().getColumnFieldPropertyDictionary("progetto_liv2"));
					}else {
						nbp.setColumns(nbp.getBulkInfo().getColumnFieldPropertyDictionary("moduli_sip"));
					}
					context.addHookForward("seleziona",this,"doRiportaSelezioneModuli");
					return context.addBusinessProcess(nbp);
				}
			}
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doRiportaSelezioneModuli(ActionContext context)  throws java.rmi.RemoteException {

		try {
			HookForward caller = (HookForward)context.getCaller();
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			CdrBulk cdr = (CdrBulk) bp.getModel();
			SimpleDetailCRUDController controller = bp.getCrudDettagli();
			java.util.List l = (java.util.List)caller.getParameter("selectedElements");
			String errorMessage = "";
			if (l!=null && !l.isEmpty()){
				Iterator it = l.iterator();
				while(it.hasNext()) {
                    Progetto_sipBulk progetto = (Progetto_sipBulk) it.next();
                    Pdg_moduloBulk mod = new Pdg_moduloBulk();
					mod.initializeForInsert(bp,context);
					mod.setCdr(cdr);
					mod.setProgetto(progetto);
                    if (!Optional.ofNullable(progetto.getOtherField())
                            .flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getStato()))
                            .filter(stato -> Arrays.asList(StatoProgetto.STATO_NEGOZIAZIONE.value(), StatoProgetto.STATO_APPROVATO.value()).indexOf(stato) != -1).isPresent()) {
                            errorMessage +="Il progetto " + progetto.getCd_progetto() +" non ha uno stato utile alla previsione!";
                            errorMessage += bp.getParentRoot().isBootstrap() ? "<br>" : "\n";
                            continue;
                    }
                    if (!Optional.ofNullable(progetto.getOtherField())
                            .flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
                            .filter(tipoFinanziamentoBulk -> tipoFinanziamentoBulk.getFlPrevEntSpesa() || tipoFinanziamentoBulk.getFlRipCostiPers()).isPresent()) {
                        errorMessage +="Per il progetto " + progetto.getCd_progetto() +" non è consentita la previsione!<br>";
                        errorMessage += bp.getParentRoot().isBootstrap() ? "<br>" : "\n";
                        continue;
                    }
					if (!cdr.getDettagli().containsByPrimaryKey(mod))
						controller.add(context, mod);
				}
			}
			if (errorMessage.length() > 0)
			    setErrorMessage(context, errorMessage);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione della richiesta di consultazione del Piano di riparto delle spese accentrate
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doConsultaPianoRiparto(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			CdrBulk cdr = (CdrBulk)bp.getModel();

			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(context.getUserContext()));

			if (cdr!=null && cdr.getCd_centro_responsabilita()!=null) {
				clause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
			}

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsPdgPianoRipartoBP");

			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);

			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doScaricaCostiPersonale(ActionContext context) {
		try {
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			fillModel(context);
			String labelProgetto = "moduli";
			if (bp.getParametriCnr()!=null && bp.getParametriCnr().getFl_nuovo_pdg())
				labelProgetto = "progetti";
			return openConfirm(context, "Attenzione! Confermi che tutto il personale è stato ripartito sui GAE associati ai "+labelProgetto+" di carattere scientifico, evitando di utilizzare GAE associati ai "+labelProgetto+" di carattere gestionale?", OptionBP.CONFIRM_YES_NO, "doConfirmScaricaCostiPersonale");
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmScaricaCostiPersonale(ActionContext context,int option) {
		try {
			if ( option == OptionBP.YES_BUTTON)
			{
				CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
				fillModel(context);
				CdrBulk cdr = (CdrBulk) ((PdgAggregatoModuloComponentSession)bp.createComponentSession()).scaricaDipendentiSuPdGP(context.getUserContext(),(CdrBulk)bp.getModel());
				bp.setMessage("Scarico effettuato correttamente.");
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doAnnullaScaricaCostiPersonale(ActionContext context) {
		try {
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			fillModel(context);
			CdrBulk cdr = (CdrBulk) ((PdgAggregatoModuloComponentSession)bp.createComponentSession()).annullaScaricaDipendentiSuPdGP(context.getUserContext(),(CdrBulk)bp.getModel());
			bp.setMessage("Annullamento scarico effettuato correttamente.");

			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doRiportaSelezione(ActionContext context, OggettoBulk oggettobulk) {
			try {
				CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);

				Forward forward = super.doRiportaSelezione(context, oggettobulk);
				if (oggettobulk!=null) {
					bp.caricaCdrPdGP(context);
					if (!bp.isCdrPdGPUtilizzabile()) {
						bp.setStatus(bp.VIEW);
						bp.setEditable(false);
						setErrorMessage(context,"Lo stato del PdGP - CDR per il CdR "+((CdrBulk)oggettobulk).getCd_centro_responsabilita()+" risulta non impostato oppure\nè chiusa la fase previsionale per l'esercizio "+CNRUserContext.getEsercizio(context.getUserContext())+". Non consentita la modifica.");
					}
				}
				return forward;
			} catch(Exception e) {
				return handleException(context,e);
			}
	}

	public Forward doStatoCdRPdGR(ActionContext context) {
		try
		{
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			CRUDStatoCdrPdGPBP bpDett;
			String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().
					validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),
							((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ?
									((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() :
										"*","CRUDStatoCdrPdGPBP");
			if (mode == null || mode.equals("V"))
				throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa. Impossibile continuare.");

			if(bp.isEditable())
				bpDett = (CRUDStatoCdrPdGPBP)context.createBusinessProcess("CRUDStatoCdrPdGPBP", new Object[] { "M" });
			else
				bpDett = (CRUDStatoCdrPdGPBP)context.createBusinessProcess("CRUDStatoCdrPdGPBP", new Object[] { "V" });
			return context.addBusinessProcess(bpDett);
		}
		catch(Throwable e)
		{
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doGestionaleEntrate(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		Pdg_moduloBulk pdg_mod = (Pdg_moduloBulk)bp.getCrudDettagli().getModel();
		try {
			fillModel(context);
			if (bp.isDirty()) {
				setErrorMessage(context,"Attenzione: è necessario salvare le modifiche effettuate!");
				return context.findDefaultForward();
			}

			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg_mod.getEsercizio());
			clause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg_mod.getCd_centro_responsabilita());
			clause.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg_mod.getPg_progetto());

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsPdgpPdggEtrBP",
					new Object[] {
						bp.isEditable() && bp.isGestionaleOperabile() ? "M" : "V",
					}
				);

			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			ricercaLiberaBP.setSearchResultColumnSet("pdgModuloEntrateGest");
			ricercaLiberaBP.setFreeSearchSet("pdgModuloEntrateGest");

			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public it.cnr.jada.action.Forward doGestionaleSpese(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)context.getBusinessProcess();
		Pdg_moduloBulk pdg_mod = (Pdg_moduloBulk)bp.getCrudDettagli().getModel();
		try {
			fillModel(context);
			if (bp.isDirty()) {
				setErrorMessage(context,"Attenzione: è necessario salvare le modifiche effettuate!");
				return context.findDefaultForward();
			}

			if (pdg_mod==null) {
				setErrorMessage(context,"Attenzione: è necessario selezionare una riga valida!");
				return context.findDefaultForward();
			}

			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg_mod.getEsercizio());
			clause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg_mod.getCd_centro_responsabilita());
			clause.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg_mod.getPg_progetto());

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsPdgpPdggSpeBP",
					new Object[] {
						bp.isEditable() && bp.isGestionaleOperabile() ? "M" : "V",
					}
				);

			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			ricercaLiberaBP.setSearchResultColumnSet("pdgModuloSpeseGest");
			ricercaLiberaBP.setFreeSearchSet("pdgModuloSpeseGest");

			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione della richiesta di consultazione  limite spesa
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doConsultaLimitiSpesa(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)getBusinessProcess(context);
			CdrBulk cdr = (CdrBulk)bp.getModel();
			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(context.getUserContext()));
			if (cdr!=null && cdr.getCd_cds()!=null) {
				clause.addClause("AND","cdCds",SQLBuilder.EQUALS,cdr.getCd_cds());
			}
			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsLimitiSpesaPdgpBP");
			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

}
