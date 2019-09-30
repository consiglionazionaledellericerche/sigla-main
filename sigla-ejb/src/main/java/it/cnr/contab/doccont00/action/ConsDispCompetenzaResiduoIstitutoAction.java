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




import it.cnr.contab.doccont00.bp.ConsDispCompResDipIstBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResEntIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResEntIstVoceBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstVoceBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResVoceNatBP;
import it.cnr.contab.doccont00.bp.ConsDispCompetenzaResiduoIstitutoBP;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGPTitBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsDispCompetenzaResiduoIstitutoAction extends ConsultazioniAction{

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			ConsDispCompetenzaResiduoIstitutoBP consultazioneBP = null;
			if (bp instanceof ConsDispCompResIstCdrGaeBP) 
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResIstCdrGaeBP");
			if (bp instanceof ConsDispCompResIstVoceBP) 
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResIstVoceBP");
			if (bp instanceof ConsDispCompResDipIstBP)
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResDipIstBP");
			if (bp instanceof ConsDispCompResVoceNatBP)
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResVoceNatBP");
			if (bp instanceof ConsDispCompResEntIstCdrGaeBP) 
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResEntIstCdrGaeBP");
			if (bp instanceof ConsDispCompResEntIstVoceBP) 
				consultazioneBP = (ConsDispCompetenzaResiduoIstitutoBP)context.createBusinessProcess("ConsDispCompResEntIstVoceBP");
			
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			
			if (bp instanceof ConsDispCompResIstCdrGaeBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneCdrGae(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			if (bp instanceof ConsDispCompResIstVoceBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneVoce(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			if (bp instanceof ConsDispCompResDipIstBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneDip(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			if (bp instanceof ConsDispCompResVoceNatBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneVoceNat(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneEntrateCdrGae(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			if (bp instanceof ConsDispCompResEntIstVoceBP)
				consultazioneBP.setIterator(context,consultazioneBP.createConsDispCompetenzaResiduoIstitutoComponentSession().findConsultazioneEntrateVoce(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	
// le action per Istituto Cdr Gae	
	public Forward doConsultaProgetto(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest = ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROG;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest = ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROG;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaCommessa(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest = ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMM;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest = ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMM;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaModulo(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest=ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMOD;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest=ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMOD;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaCdr(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest = ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDR;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest = ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDR;
		return doConsulta(context, livDest);
		}
	public Forward doConsultaGae(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest=ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDRGAE;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest=ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDRGAE;
		return doConsulta(context,livDest);
	}
	public Forward doConsultaVoce(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest=ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDRGAEDET;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest=ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDRGAEDET;
		return doConsulta(context, livDest);
	}
	
	public Forward doConsultaCdsCdr(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest =  ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDR;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest =  ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDR;
		return doConsulta(context,livDest);
	}
	
	public Forward doConsultaCdsCdrGae(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest = ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDRGAE;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest = ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDRGAE;
		return doConsulta(context,livDest);
	}
	
	public Forward doConsultaCdsCdrGaeVoce(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstCdrGaeBP)
			livDest = ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDRGAEDET;
		if (bp instanceof ConsDispCompResEntIstCdrGaeBP)
			livDest = ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDRGAEDET;
		return doConsulta(context, livDest);
	}
//	le action per Istituto Voce del Piano
	public Forward doConsultaVoceVoce(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCE;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCE;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaVoceProgetto(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROG;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROG;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaVoceCommessa(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMM;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMM;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaVoceModulo(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMOD;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMOD;
		return doConsulta(context, livDest);
	}
	public Forward doConsultaVoceCdr(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMODCDR;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMODCDR;
		return doConsulta(context, livDest);
		}
	public Forward doConsultaVoceGae(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMODCDRGAE;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMODCDRGAE;
		return doConsulta(context, livDest);
	}
	
	public Forward doConsultaVoceCdr2(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCECDR;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCECDR;
		return doConsulta(context, livDest);
	}
	
	public Forward doConsultaVoceCdrGae(ActionContext context) {
		ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
		bp.setSelection(context);
		String livDest=null;
		if (bp instanceof ConsDispCompResIstVoceBP)
			livDest = ConsDispCompResIstVoceBP.LIV_BASECDSVOCECDRGAE;
		if (bp instanceof ConsDispCompResEntIstVoceBP)
			livDest = ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCECDRGAE;
		return doConsulta(context, livDest);
	}
	
	
//	 le action per Dipartimento
	public Forward doConsultaDipCds(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDS);
	}
	public Forward doConsultaDipProgetto(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROG);
	}
	public Forward doConsultaDipCommessa(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMM);
	}
	public Forward doConsultaDipModulo(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMOD);
	}
	public Forward doConsultaDipCdr(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDR);
		}
	public Forward doConsultaDipGae(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDRGAE);
	}
	public Forward doConsultaDipVoce(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDRGAEDET);
	}
	
	public Forward doConsultaDipCdsCdr(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDR);
	}
	
	public Forward doConsultaDipCdsCdrGae(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDRGAE);
	}
	
	public Forward doConsultaDipCdsCdrGaeVoce(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDRGAEDET);
	}
	
//	 le action per Voce - Natura
	public Forward doConsultaVoceVoceNat(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_VOCENAT);
	}
	public Forward doConsultaVoceVoceMod(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_VOCENATMOD);
	}
	public Forward doConsultaVoceVoceCdr(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_VOCENATMODCDR);
	}
	public Forward doConsultaVoceVoceGae(ActionContext context) {
		return doConsulta(context, ConsDispCompResIstCdrGaeBP.LIV_VOCENATMODCDRGAE);
	}
	
	
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsDispCompetenzaResiduoIstitutoBP) {
			ConsDispCompetenzaResiduoIstitutoBP bp = (ConsDispCompetenzaResiduoIstitutoBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}
