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

package it.cnr.contab.bilaterali00.action;

import it.cnr.contab.anagraf00.bp.CRUDAbiCabBP;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP;
import it.cnr.contab.bilaterali00.bulk.BltIstitutiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzati_dettBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;
import it.cnr.contab.bilaterali00.ejb.BltAccordiComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class BltAccordiAction extends it.cnr.jada.util.action.CRUDAction{
	public BltAccordiAction() {
		super();
	}

    public Forward doApriBltVisitaIta(ActionContext context) {
    	return doApriBltVisita(context, true);
    }
    public Forward doApriBltVisitaStr(ActionContext context) {
    	return doApriBltVisita(context, false);
    }
    private Forward doApriBltVisita(ActionContext context, boolean isItaliano) {
    	try {
    		CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
    		fillModel(context);

	        Blt_autorizzati_dettBulk autorizzatoDett;
	        
	        if (isItaliano)
	        	autorizzatoDett = (Blt_autorizzati_dettBulk)((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiDettIta()).getModel();
	        else
	        	autorizzatoDett = (Blt_autorizzati_dettBulk)((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiDettStr()).getModel();

	        Blt_visiteBulk visita = autorizzatoDett.getBltVisitaValida();
	        
	        if (visita==null) {
    			bp.setMessage("Non risulta essere stata effettuata ancora nessuna visita.");
    			return context.findDefaultForward();
    		}

	        CRUDBP newBP = (CRUDBP)context.getUserInfo().createBusinessProcess(
    				context,
    				"CRUDBltVisiteBP",
    				new Object[] {
    					"MRSW",
    					visita
    				}
    			);

    		newBP.edit(context, visita);
    		newBP.setBringBack(true);
	        context.addHookForward("bringback",this,"doBringBackApriBltVisita");

    		return context.addBusinessProcess(newBP);
    	} catch(Throwable e) {
    		return handleException(context,e);
    	}
    }
	public Forward doBringBackApriBltVisita(ActionContext context)	throws java.rmi.RemoteException {
    	HookForward caller = (HookForward)context.getCaller();
    	Blt_visiteBulk visitaNew = (Blt_visiteBulk)caller.getParameter("bringback");

    	if (visitaNew!=null) {
        	CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
	    	if (visitaNew.isVisitaItaliano()) {
		        Blt_autorizzati_dettBulk autorizzatoDettIta = (Blt_autorizzati_dettBulk)((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiDettIta()).getModel();
		        if (autorizzatoDettIta!=null){
					int index = bp.getCrudBltAutorizzatiDettIta().getDetails().indexOf(autorizzatoDettIta);
		        	int indexNew = autorizzatoDettIta.getBltVisiteColl().indexOf(autorizzatoDettIta.getBltVisitaValida());
			        autorizzatoDettIta.getBltVisiteColl().set(indexNew, visitaNew);
					bp.getCrudBltAutorizzatiDettIta().getDetails().set(index, autorizzatoDettIta);
		        }
			} else {
		        Blt_autorizzati_dettBulk autorizzatoDettStr = (Blt_autorizzati_dettBulk)((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiDettStr()).getModel();
		        if (autorizzatoDettStr!=null){
					int index = bp.getCrudBltAutorizzatiDettStr().getDetails().indexOf(autorizzatoDettStr);
			        int indexNew = autorizzatoDettStr.getBltVisiteColl().indexOf(autorizzatoDettStr.getBltVisitaValida());
			        autorizzatoDettStr.getBltVisiteColl().set(indexNew, visitaNew);
					bp.getCrudBltAutorizzatiDettStr().getDetails().set(index, autorizzatoDettStr);
		        }
			}
		}
		return context.findDefaultForward();	
	}
	public Forward doBringBackSearchFindComuneEnteDiAppartenenza(ActionContext context, Blt_autorizzatiBulk autorizzato, ComuneBulk comune)	throws java.rmi.RemoteException {
		CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);

		if (comune != null) {
			if( comune.getPg_comune() != null && !("".equals(comune.getPg_comune())) ) {
				try {
					int index = bp.getCrudBltAutorizzatiIta().getDetails().indexOf(autorizzato);
					bp.getCrudBltAutorizzatiIta().getDetails().set(index, ((BltAccordiComponentSession)bp.createComponentSession()).setComuneEnteDiAppartenenza(context.getUserContext(), autorizzato, comune));
					bp.getCrudBltAutorizzatiIta().setModelIndex(context, index);
				} catch(Throwable e) {
					return handleException(context,e);
				}
			}
		}
		return context.findDefaultForward();	
	}
	public Forward doRiportaSelezioneComuneEnteDiAppartenenza(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = (it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk)caller.getParameter("selezione");
		if (comune != null) {
			CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
			Blt_autorizzatiBulk autorizzato = ((Blt_autorizzatiBulk)((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiIta()).getModel());
			autorizzato.setComuneEnteDiAppartenenza(comune);

			if( comune.getPg_comune() != null && !(comune.getPg_comune()).equals("") ) {
				try {
					bp.getCrudBltAutorizzatiIta().getDetails().set(((SimpleDetailCRUDController)bp.getCrudBltAutorizzatiIta()).getModelIndex(), ((BltAccordiComponentSession)bp.createComponentSession()).setComuneEnteDiAppartenenza(context.getUserContext(), autorizzato, comune));
				} catch(BusinessProcessException bpe) {
					return handleException(context, bpe);
				} catch(it.cnr.jada.comp.ComponentException ce) {
					return handleException(context,ce);
				}
			}
			
		}

		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindComuneEnteResponsIta(ActionContext context, Blt_progettiBulk progetto, ComuneBulk comune)	throws java.rmi.RemoteException {
		CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);

		if (comune != null) {
			if( comune.getPg_comune() != null && !("".equals(comune.getPg_comune())) ) {
				try {
					int index = bp.getCrudBltProgetti().getDetails().indexOf(progetto);
					bp.getCrudBltProgetti().getDetails().set(index, ((BltAccordiComponentSession)bp.createComponentSession()).setComuneEnteResponsIta(context.getUserContext(), progetto, comune));
					bp.getCrudBltProgetti().setModelIndex(context, index);
				} catch(Throwable e) {
					return handleException(context,e);
				}
			}
		}
		return context.findDefaultForward();	
	}
	public Forward doRiportaSelezioneComuneEnteResponsIta(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = (it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk)caller.getParameter("selezione");
		if (comune != null) {
			CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
			Blt_progettiBulk progetto = ((Blt_progettiBulk)((SimpleDetailCRUDController)bp.getCrudBltProgetti()).getModel());
			progetto.setComuneEnteResponsIta(comune);

			if( comune.getPg_comune() != null && !(comune.getPg_comune()).equals("") ) {
				try {
					bp.getCrudBltProgetti().getDetails().set(((SimpleDetailCRUDController)bp.getCrudBltProgetti()).getModelIndex(), ((BltAccordiComponentSession)bp.createComponentSession()).setComuneEnteResponsIta(context.getUserContext(), progetto, comune));
				} catch(BusinessProcessException bpe) {
					return handleException(context, bpe);
				} catch(it.cnr.jada.comp.ComponentException ce) {
					return handleException(context,ce);
				}
			}
			
		}

		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindTerzo(ActionContext context, Blt_autorizzatiBulk autorizzato, TerzoBulk terzo) {
		try {
			fillModel(context);
			if (terzo!=null) {
				CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
				autorizzato.setTerzo(Utility.createTerzoComponentSession().completaTerzo(context.getUserContext(), terzo));
			}
			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlankSearchFind_cdr_respons_ita(ActionContext context, Blt_progettiBulk progetto) {
		progetto.setCentro_responsabilitaIta(new CdrBulk());
		progetto.setBltIstituto(null);
		return context.findDefaultForward();
	}
	
	public Forward doBringBackSearchFind_cdr_respons_ita(ActionContext context, Blt_progettiBulk progetto, CdrBulk cdr) {
		try {
			fillModel(context);
			if (cdr!=null) {
				CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
				progetto.setCentro_responsabilitaIta(cdr);
				BltIstitutiBulk istituto = (BltIstitutiBulk) ((BltAccordiComponentSession)bp.createComponentSession()).findByPrimaryKey(context.getUserContext(), 
							new BltIstitutiBulk(progetto.getCentro_responsabilitaIta().getCd_centro_responsabilita()));
				if (istituto != null)
					progetto.setBltIstituto(istituto);
			}
			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlankSearchFindCdrTerzo(ActionContext context, Blt_autorizzatiBulk autorizzato) {
		autorizzato.setCdrTerzo(new CdrBulk());
		autorizzato.setBltIstituto(null);
		return context.findDefaultForward();
	}
	
	public Forward doBringBackSearchFindCdrTerzo(ActionContext context, Blt_autorizzatiBulk autorizzato, CdrBulk cdr) {
		try {
			fillModel(context);
			if (cdr!=null) {
				CRUDBltAccordiBP bp = (CRUDBltAccordiBP)getBusinessProcess(context);
				autorizzato.setCdrTerzo(cdr);
				BltIstitutiBulk istituto = (BltIstitutiBulk) ((BltAccordiComponentSession)bp.createComponentSession()).findByPrimaryKey(context.getUserContext(), 
							new BltIstitutiBulk(autorizzato.getCdCdrTerzo()));
				if (istituto != null)
					autorizzato.setBltIstituto(istituto);
			}
			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	
}