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

package it.cnr.contab.incarichi00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_cdsBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_da_assegnareBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_terzoBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_uoBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_limiteBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDConfigRepertorioLimitiBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private Unita_organizzativaBulk uoSrivania;

	private final SimpleDetailCRUDController repertorioLimiti = new SimpleDetailCRUDController("RepertorioLimiti",Repertorio_limitiBulk.class,"repertorio_limitiColl",this){
	public boolean isShrinkable() {
			return super.isShrinkable() &&
				  (getModel()==null ||
			       !((Repertorio_limitiBulk)getModel()).isUtilizzato());
		}
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			if (detail instanceof Repertorio_limitiBulk)
				if (((Repertorio_limitiBulk)detail).isUtilizzato())
					throw new ValidationException("I dettagli già utilizzati non possono essere eliminati.");
		}
	};

	private final SimpleDetailCRUDController incarichiXCds = new SimpleDetailCRUDController("IncarichiXCds",V_incarichi_cdsBulk.class,"incarichi_x_cdsColl",repertorioLimiti);

	private final SimpleDetailCRUDController incarichiXUo = new SimpleDetailCRUDController("IncarichiXUo",V_incarichi_uoBulk.class,"incarichi_x_uoColl",incarichiXCds);

	private final SimpleDetailCRUDController incarichiXTerzo = new SimpleDetailCRUDController("IncarichiXTerzo",V_incarichi_terzoBulk.class,"incarichi_x_terzoColl",incarichiXUo);

	private final SimpleDetailCRUDController incarichiScaduti = new SimpleDetailCRUDController("IncarichiScaduti",V_incarichi_da_assegnareBulk.class,"incarichi_scadutiColl",incarichiXUo);

	private final SimpleDetailCRUDController incarichiValidi = new SimpleDetailCRUDController("IncarichiValidi",V_incarichi_da_assegnareBulk.class,"incarichi_validiColl",incarichiXUo);
	
	private final SimpleDetailCRUDController incarichiXTerzoAll = new SimpleDetailCRUDController("IncarichiXTerzoAll",V_incarichi_terzoBulk.class,"incarichi_x_terzoColl",repertorioLimiti);

	private final SimpleDetailCRUDController incarichiScadutiAll = new SimpleDetailCRUDController("IncarichiScadutiAll",V_incarichi_da_assegnareBulk.class,"incarichi_scadutiColl",repertorioLimiti);

	private final SimpleDetailCRUDController incarichiValidiAll = new SimpleDetailCRUDController("IncarichiValidiAll",V_incarichi_da_assegnareBulk.class,"incarichi_validiColl",repertorioLimiti);

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDConfigRepertorioLimitiBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDConfigRepertorioLimitiBP(String function) {
		super(function);
	}

	public final SimpleDetailCRUDController getRepertorioLimiti() {
		return repertorioLimiti;
	}
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
		if (((Tipo_limiteBulk)oggettobulk).getFl_cancellato().booleanValue()){
			setStatus(CRUDBP.VIEW);
			setMessage("Tipo Limite eliminato. Non consentita la modifica.");
		}
	}
	public final SimpleDetailCRUDController getIncarichiXCds() {
		return incarichiXCds;
	}
	public final SimpleDetailCRUDController getIncarichiXUo() {
		return incarichiXUo;
	}
	public final SimpleDetailCRUDController getIncarichiXTerzo() {
		return incarichiXTerzo;
	}
	public final SimpleDetailCRUDController getIncarichiScaduti() {
		return incarichiScaduti;
	}
	public final SimpleDetailCRUDController getIncarichiValidi() {
		return incarichiValidi;
	}
	public final SimpleDetailCRUDController getIncarichiXTerzoAll() {
		return incarichiXTerzoAll;
	}
	public final SimpleDetailCRUDController getIncarichiScadutiAll() {
		return incarichiScadutiAll;
	}
	public final SimpleDetailCRUDController getIncarichiValidiAll() {
		return incarichiValidiAll;
	}
	protected void resetTabs(ActionContext context) {
		setTab( "tab", "tabRepertorio");
		setTab( "tabDettaglio", "tabIncarichiTerzo");
	}
	public String[][] getTabs() {
		if (this.getModel()==null || this.getRepertorioLimiti()==null ||
			this.getRepertorioLimiti().getModel()==null)
		    return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" }
               };   
		if (((Tipo_limiteBulk)this.getModel()).getFl_raggruppa()==Boolean.FALSE)
			return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" },
				{ "tabIncarichiTerzo","Assegnati","/incarichi00/tab_incarichi_terzo.jsp" },
				{ "tabIncarichiDaAssegnare","In Corso","/incarichi00/tab_incarichi_validi.jsp" },
				{ "tabIncarichiScaduti","Scaduti","/incarichi00/tab_incarichi_scaduti.jsp" }
            };   
		if (this.getIncarichiXCds()==null ||
			this.getIncarichiXCds().getDetails().isEmpty() )
		   return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" }
               };   
		else if (this.getIncarichiXCds().getModel()==null ||
				 this.getIncarichiXUo()==null ||
				 this.getIncarichiXUo().getDetails().isEmpty() )
		   return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" },
				{ "tabIncarichiCds","Ripartizione x CDS","/incarichi00/tab_incarichi_cds.jsp" }
               };   
		else if (this.getIncarichiXUo().getModel()==null ||
				 ((this.getIncarichiXTerzo()==null ||
				   this.getIncarichiXTerzo().getDetails().isEmpty()) &&
				   (this.getIncarichiScaduti()==null ||
				    this.getIncarichiScaduti().getDetails().isEmpty()) &&
				   (this.getIncarichiValidi()==null ||
				    this.getIncarichiValidi().getDetails().isEmpty())))
		   return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" },
				{ "tabIncarichiCds","Ripartizione x CDS","/incarichi00/tab_incarichi_cds.jsp" },
				{ "tabIncarichiUo","Ripartizione x UO","/incarichi00/tab_incarichi_uo.jsp" }
               };   
		else
		   return new String[][] {
				{ "tabRepertorio","Esercizio","/incarichi00/tab_repertorio.jsp" },
				{ "tabIncarichiCds","Ripartizione x CDS","/incarichi00/tab_incarichi_cds.jsp" },
				{ "tabIncarichiUo","Ripartizione x UO","/incarichi00/tab_incarichi_uo.jsp" },
				{ "tabIncarichiDettaglio","Dettaglio Incarichi","/incarichi00/tab_incarichi_dettaglio.jsp" }
               };   
	}
	public String[][] getTabsDettaglioIncarichi() {
		return new String[][] {
				{ "tabIncarichiTerzo","Assegnati","/incarichi00/tab_incarichi_terzo.jsp" },
				{ "tabIncarichiDaAssegnare","In Corso","/incarichi00/tab_incarichi_validi.jsp" },
				{ "tabIncarichiScaduti","Scaduti","/incarichi00/tab_incarichi_scaduti.jsp" }
               };   
	}
	private Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	private void setUoSrivania(Unita_organizzativaBulk uoSrivania) {
		this.uoSrivania = uoSrivania;
	}

	public boolean isUoEnte(){
    	return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
    }

	protected void initialize(ActionContext context) throws BusinessProcessException {
		super.initialize(context);
		setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
	}
}
