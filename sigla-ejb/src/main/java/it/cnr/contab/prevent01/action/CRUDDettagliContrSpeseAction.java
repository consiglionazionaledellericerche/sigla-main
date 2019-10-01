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

package it.cnr.contab.prevent01.action;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bp.CRUDDettagliContrSpeseBP;
import it.cnr.contab.prevent01.bulk.Contrattazione_speseVirtualBulk;
import it.cnr.contab.prevent01.bulk.Pdg_approvato_dip_areaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgContrSpeseComponentSession;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;

public class CRUDDettagliContrSpeseAction extends CRUDAction {
 
	public Forward doTab(ActionContext context, String tabName, String pageName) 
	{
		try
		{
			fillModel( context );
			CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)getBusinessProcess(context);
			Contrattazione_speseVirtualBulk contr_spese = (Contrattazione_speseVirtualBulk) bp.getModel();
			//bp.getModel().validate();
			if (bp.isDirty())
			{
				getBusinessProcess(context).setErrorMessage("Salvare le modifiche prima di cambiare pagina.");
				return context.findDefaultForward();
			}
			if (pageName.equals("tabDettagli")) {
				Pdg_approvato_dip_areaBulk pdg_dip_area = (Pdg_approvato_dip_areaBulk) bp.getCrudDettagliDipArea().getModel();
				if (pdg_dip_area==null) {
					getBusinessProcess(context).setErrorMessage("Selezionare una riga per visualizzare i dettagli.");
					return context.findDefaultForward();
				}
				if (!contr_spese.getDettagliContrSpese().isEmpty()) {
					if (!pdg_dip_area.equalsByPrimaryKey(((Pdg_contrattazione_speseBulk)contr_spese.getDettagliContrSpese().get(0)).getPdg_dip_area()))
						bp.caricaDettagli(context, contr_spese, pdg_dip_area);
				}
				else
					bp.caricaDettagli(context, contr_spese, pdg_dip_area);			
			}
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
		return super.doTab(context, tabName, pageName);
	}

	public Forward doSearchSearchtool_progetto(ActionContext context) {
		CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)context.getBusinessProcess();
		return search(context, getFormField(context, "main.Dettagli.searchtool_progetto"),bp.isFlNuovoPdg()?"progetto_liv2":null);
	}

	public Forward doSearchSearchtool_progetto_liv2(ActionContext context) {
		return doSearchSearchtool_progetto(context);
	}

	public Forward doBlankSearchSearchtool_progetto(ActionContext actioncontext, String s) {
		CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)actioncontext.getBusinessProcess();
		Pdg_contrattazione_speseBulk pdg_contr_spese = (Pdg_contrattazione_speseBulk)bp.getModel();
		pdg_contr_spese.setCdr(null);
		return super.doBlankSearch(actioncontext, s);
	}

	public Forward doBlankSearchSearchtool_progetto_liv2(ActionContext actioncontext, String s) {
		return doBlankSearchSearchtool_progetto(actioncontext, s);
	}

	public it.cnr.jada.action.Forward doBringBackSearchSearchtool_progetto(ActionContext context,Pdg_contrattazione_speseBulk pdg_contr_spese, Progetto_sipBulk progetto) {
		try {
			CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)context.getBusinessProcess();
		
			if (progetto != null) {
				if (progetto.getUnita_organizzativa().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC )) 
				{
					bp.setErrorMessage("Non è possibile inserire o modificare "+(bp.isFlNuovoPdg()?"progetti":"moduli di attività")+" afferenti a CDS appartenenti alla SAC.");
					return context.findDefaultForward();
				}
				
				CdrBulk cdr = ((PdgContrSpeseComponentSession)bp.createComponentSession()).caricaCdrAfferenzaDaUo(context.getUserContext(), progetto.getUnita_organizzativa());
				pdg_contr_spese.setProgetto(progetto);
				pdg_contr_spese.setCdr(cdr);
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public it.cnr.jada.action.Forward doBringBackSearchSearchtool_progetto_liv2(ActionContext context,Pdg_contrattazione_speseBulk pdg_contr_spese, Progetto_sipBulk progetto) {
		return doBringBackSearchSearchtool_progetto(context, pdg_contr_spese, progetto);
	}

	public it.cnr.jada.action.Forward doApprova(ActionContext context) {
		try {
			fillModel( context );
			CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)context.getBusinessProcess();
			if (bp.isDirty())
			{
				bp.setErrorMessage("Salvare le modifiche prima eseguire il comando richiesto.");
				return context.findDefaultForward();
			}
			bp.approvaDefinitivamente(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doUndoApprova(ActionContext context) {
		try {
			CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)context.getBusinessProcess();
			bp.undoApprovazioneDefinitiva(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
