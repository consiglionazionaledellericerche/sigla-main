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

package it.cnr.contab.bilaterali00.bp;

import it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzati_dettBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_programma_visiteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.util.TreeMap;

public class CRUDBltAccordiBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController crudBltProgetti = new SimpleDetailCRUDController("BltProgetti",Blt_progettiBulk.class,"blt_progettiColl",this) {
		protected void validate(ActionContext actioncontext, it.cnr.jada.bulk.OggettoBulk oggettobulk) throws it.cnr.jada.bulk.ValidationException {
			super.validate(actioncontext, oggettobulk);
			Blt_progettiBulk progetto = (Blt_progettiBulk)oggettobulk;
			
			if (progetto.getFl_associato_respons_ita()) {
				if (progetto.getComuneEnteResponsIta()==null||progetto.getComuneEnteResponsIta().getPg_comune()==null)
				    throw new ValidationException( "E' obbligatorio indicare il comune dell'ente di appartenenza del responsabile italiano!");
				if (progetto.getCap_ente_respons_ita()==null)
				    throw new ValidationException( "E' obbligatorio indicare il cap dell'ente di appartenenza del responsabile italiano!");
				if (progetto.getIndirizzo_ente_respons_ita()==null)
				    throw new ValidationException( "E' obbligatorio indicare l'indirizzo dell'ente di appartenenza del responsabile italiano!");
				if (progetto.getEnte_respons_ita()==null)
				    throw new ValidationException( "E' obbligatorio indicare la denominazione dell'ente di appartenenza del responsabile italiano!");
			} else {
				progetto.setEnte_respons_ita(null);
				progetto.setCap_ente_respons_ita(null);
				progetto.setComuneEnteResponsIta(null);
				progetto.setIndirizzo_ente_respons_ita(null);
				if (!progetto.getFl_associato_respons_ita())
					progetto.setFl_associato_respons_ita(Boolean.FALSE);
			}
		};
	};
	private final SimpleDetailCRUDController crudBltProgrammaVisiteIta = new SimpleDetailCRUDController("BltProgrammaVisiteIta",Blt_programma_visiteBulk.class,"bltProgrammaVisiteItaColl",crudBltProgetti);
	private final SimpleDetailCRUDController crudBltProgrammaVisiteStr = new SimpleDetailCRUDController("BltProgrammaVisiteStr",Blt_programma_visiteBulk.class,"bltProgrammaVisiteStrColl",crudBltProgetti);
	
	private final SimpleDetailCRUDController crudBltAutorizzatiIta = new SimpleDetailCRUDController("BltAutorizzatiIta",Blt_autorizzatiBulk.class,"bltAutorizzatiItaColl",crudBltProgetti) {
		protected void validate(ActionContext actioncontext, it.cnr.jada.bulk.OggettoBulk oggettobulk) throws it.cnr.jada.bulk.ValidationException {
			super.validate(actioncontext, oggettobulk);
			Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk)oggettobulk;
			
			if (autorizzato.getTerzo()!=null && autorizzato.getTerzo().isDipendente()) {
				if (autorizzato.getFlAssimilatoDip()==null || !autorizzato.getFlAssimilatoDip())
					throw new ValidationException( "Il terzo selezionato risulta essere un dipendente. Indicare il terzo come Dipendente/Assimilato !");
				if (((Blt_autorizzatiBulk)oggettobulk).getCdCdrTerzo()==null)
				    throw new ValidationException( "E' obbligatorio indicare il centro di responsabilità di appartenenza!");
			}
			if (!autorizzato.getFlAssimilatoDip() || autorizzato.getFlAssociato()) {
				if (autorizzato.getComuneEnteDiAppartenenza()==null||autorizzato.getComuneEnteDiAppartenenza().getPg_comune()==null)
				    throw new ValidationException( "E' obbligatorio indicare il comune dell'ente di appartenenza!");
				if (autorizzato.getCapEnteDiAppartenenza()==null)
				    throw new ValidationException( "E' obbligatorio indicare il cap dell'ente di appartenenza!");
				if (autorizzato.getIndirizzoEnteDiAppartenenza()==null)
				    throw new ValidationException( "E' obbligatorio indicare l'indirizzo dell'ente di appartenenza!");
				if (autorizzato.getEnteDiAppartenenza()==null)
				    throw new ValidationException( "E' obbligatorio indicare la denominazione dell'ente di appartenenza!");
			} else {
				autorizzato.setEnteDiAppartenenza(null);
				autorizzato.setCapEnteDiAppartenenza(null);
				autorizzato.setComuneEnteDiAppartenenza(null);
				autorizzato.setIndirizzoEnteDiAppartenenza(null);
				if (!autorizzato.getFlAssimilatoDip())
					autorizzato.setFlAssociato(Boolean.FALSE);
			}
		};
	};

	private final SimpleDetailCRUDController crudBltAutorizzatiDettIta = new SimpleDetailCRUDController("BltAutorizzatiDettIta",Blt_autorizzati_dettBulk.class,"bltAutorizzatiDettColl",crudBltAutorizzatiIta){
		public boolean isShrinkable() {
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();
			if (autorizzatoDett!=null && autorizzatoDett.getBltVisiteColl()!=null && !autorizzatoDett.getBltVisiteColl().isEmpty())
				return false;
			return super.isShrinkable();
		};
		public boolean isReadonly() {
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();
			if (autorizzatoDett!=null && autorizzatoDett.getBltVisitaValida()!=null)
				return true;
			return super.isReadonly();
		};
		@Override
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {
			super.writeHTMLToolbar(context, reset, find, delete, false);

			Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk)getParentModel();
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();

			if (autorizzato != null && autorizzatoDett!=null && autorizzatoDett.getFlVisitaEffettuata()) {
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
						context,
						"img/import16.gif",
						"javascript:submitForm('doApriBltVisitaIta')",
						true,
						"Apri Visita",
						HttpActionContext.isFromBootstrap(context));
			}
			super.closeButtonGROUPToolbar(context);
		}	
	};

	private final SimpleDetailCRUDController crudBltAutorizzatiStr = new SimpleDetailCRUDController("BltAutorizzatiStr",Blt_autorizzatiBulk.class,"bltAutorizzatiStrColl",crudBltProgetti);

	private final SimpleDetailCRUDController crudBltAutorizzatiDettStr = new SimpleDetailCRUDController("BltAutorizzatiDettStr",Blt_autorizzati_dettBulk.class,"bltAutorizzatiDettColl",crudBltAutorizzatiStr){
		public boolean isShrinkable() {
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();
			if (autorizzatoDett!=null && autorizzatoDett.getBltVisiteColl()!=null && !autorizzatoDett.getBltVisiteColl().isEmpty())
				return false;
			return super.isShrinkable();
		};
		public boolean isReadonly() {
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();
			if (autorizzatoDett!=null && autorizzatoDett.getBltVisitaValida()!=null)
				return true;
			return super.isReadonly();
		};
		@Override
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {
			super.writeHTMLToolbar(context, reset, find, delete, false);

			Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk)getParentModel();
			Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk)getModel();

			if (autorizzato != null && autorizzatoDett!=null && autorizzatoDett.getFlVisitaEffettuata()) {
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
						context,
						"img/import16.gif",
						"javascript:submitForm('doApriBltVisitaStr')",
						true,
						"Apri Visita",
						HttpActionContext.isFromBootstrap(context));
			}
			super.closeButtonGROUPToolbar(context);
		}	
	};

	/**
	 * Primo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 */
	public CRUDBltAccordiBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 * @param String function
	 */
	public CRUDBltAccordiBP(String function) {
		super(function);
	}
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabTestata");
		super.init(config, actioncontext);
	}
	public final SimpleDetailCRUDController getCrudBltProgetti() {
		return crudBltProgetti;
	}
	public SimpleDetailCRUDController getCrudBltProgrammaVisiteIta() {
		return crudBltProgrammaVisiteIta;
	}
	public SimpleDetailCRUDController getCrudBltProgrammaVisiteStr() {
		return crudBltProgrammaVisiteStr;
	}
	public SimpleDetailCRUDController getCrudBltAutorizzatiIta() {
		return crudBltAutorizzatiIta;
	}
	public SimpleDetailCRUDController getCrudBltAutorizzatiStr() {
		return crudBltAutorizzatiStr;
	}
	public String[][] getTabs() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTestata", "Accordo", "/bilaterali00/tab_accordo.jsp" });
		if (!isSearching() && getModel()!=null && !getModel().isToBeCreated() && ((Blt_accordiBulk)getModel()).getCd_accordo()!=null){
			hash.put(i++, new String[]{"tabBltProgetti", "Progetti", "/bilaterali00/tab_progetti.jsp" });
			if (getCrudBltProgetti()!=null && getCrudBltProgetti().getModel()!=null){
				hash.put(i++, new String[]{"tabBltAutorizzati", "Terzi Autorizzati", "/bilaterali00/tab_autorizzati.jsp" });
				hash.put(i++, new String[]{"tabBltProgrammaVisite", "Programma Visite", "/bilaterali00/tab_programma_visite.jsp" });
			}
		}
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;		
	}
	
	protected void resetTabs(ActionContext context) {
		setTab("tab","tabTestata");
		setTab("tabBltProgrammaVisite","tabBltProgrammaVisiteIta");
		setTab("tabBltAutorizzati","tabBltAutorizzatiIta");
		setTab("tabBltAutorizzatiIta","tabBltAutorizzatiItaGen");
		setTab("tabBltAutorizzatiStr","tabBltAutorizzatiStrGen");
	}
	public SimpleDetailCRUDController getCrudBltAutorizzatiDettIta() {
		return crudBltAutorizzatiDettIta;
	}
	public SimpleDetailCRUDController getCrudBltAutorizzatiDettStr() {
		return crudBltAutorizzatiDettStr;
	}
}
