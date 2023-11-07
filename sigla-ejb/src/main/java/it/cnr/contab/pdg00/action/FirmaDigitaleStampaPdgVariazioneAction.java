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

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.pdg00.bp.FirmaDigitalePdgVariazioniBP;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.upload.UploadedFile;

public class FirmaDigitaleStampaPdgVariazioneAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
	private static final long serialVersionUID = 1L;

	/**
	 * SpoolerStatusAction constructor comment.
	 */
	public FirmaDigitaleStampaPdgVariazioneAction() {
		super();
	}
	
	public Forward doCambiaVisibilita(ActionContext context) {
		FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk)bp.getModel();
		String tiSigned = bulk.getTiSigned();
		try {
			fillModel(context);
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			bulk.setTiSigned(tiSigned);
			return handleException(context,e);
		}
	}
	
	public Forward doRefresh(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doSelection(ActionContext context,String name) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.setFocus(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doSign(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.sign(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doSignOTP(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			Parametri_cdsBulk  parametriCds = Utility.createParametriCdsComponentSession().
				getParametriCds(context.getUserContext(), 
								CNRUserContext.getCd_cds(context.getUserContext()), 
								CNRUserContext.getEsercizio(context.getUserContext()));
			if (!parametriCds.getFl_kit_firma_digitale()){
				bp.sign(context);
				return context.findDefaultForward();
			}
			else
			{
			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(context, new FirmaOTPBulk());
			context.addHookForward("firmaOTP",this,"doBackFirmaOTP");			
			return context.addBusinessProcess(firmaOTPBP);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doBackFirmaOTP(ActionContext context) {
		FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
		HookForward caller = (HookForward)context.getCaller();
		FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
		try {
			firmaOTPBulk.validate();
			bp.firmaOTP(context, firmaOTPBulk);			
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doPersist(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
	        String pathFirmaTempDir = ((HttpActionContext)context).getServlet().getServletContext().getRealPath("/tmp/"+((HttpActionContext)context).getSession().getId());
			String signFileRicevuto = ((HttpActionContext)context).getParameter("sign_file_ricevuto");
        	String fileNameCompleto = pathFirmaTempDir+"/"+signFileRicevuto;
			bp.persist(context, fileNameCompleto);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doInvia(ActionContext context) {
		FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
		try {
			it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
			UploadedFile file=httpContext.getMultipartParameter("main.file_to_upload");
			if (file == null || file.getName().equals(""))
				throw new it.cnr.jada.comp.ApplicationException("Selezionare la variazione firmata da inviare");
			bp.caricaDatiPEC(context);
			bp.persistUploadedFile(context, file);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
}
