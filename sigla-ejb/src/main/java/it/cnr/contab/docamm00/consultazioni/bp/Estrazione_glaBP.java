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

package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.compensi00.bp.AbstractEstrazioneFiscaleBP;
import it.cnr.contab.docamm00.consultazioni.bulk.V_estrai_glaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;


public class Estrazione_glaBP extends AbstractEstrazioneFiscaleBP
{	
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.elabora");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.stampa");
		return toolbar;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
		V_estrai_glaBulk bulk = new V_estrai_glaBulk();
		bulk.setAnagrafico(new AnagraficoBulk());
		bulk.setEsercizio_pagamento(new Long(CNRUserContext.getEsercizio(context.getUserContext())));
		setModel(context,bulk);
		super.init(config,context);
	}
	   
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	public boolean isStampaButtonEnabled()
	{
		V_estrai_glaBulk bulk=null;
		if(this.getModel()!=null){
			 bulk=(V_estrai_glaBulk)getModel();
		    if(bulk.getFile()!=null)
		    	return true;
		    else
		    	return false;
		}else
				return false;
		
	}
	
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/

	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
			openForm(context,action,target,"multipart/form-data");	
	}
	public void doCarica(ActionContext context,File file) throws BusinessProcessException, ComponentException, IOException {
		throw new ApplicationException("DEPRECATO!");		
	}
	@Override
	public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
		Button[] toolbar = getToolbar();
		V_estrai_glaBulk bulk =(V_estrai_glaBulk)getModel();
	        HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
	        StringBuffer stringbuffer = new StringBuffer();
	        stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
			toolbar[2].setHref("javascript:doPrint('"+stringbuffer+ bulk.getFile() + "')");
		super.writeToolbar(pagecontext);
	}
}
