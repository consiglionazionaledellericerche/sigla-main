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
 * Created on Sep 30, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.servlet.jsp.JspWriter;

import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CrudDettagliSpeseBP extends SimpleDetailCRUDController{
	public CrudDettagliSpeseBP(String s, Class class1, String s1, FormController formcontroller) {
		super(s, class1, s1, formcontroller);
	}
	protected void setModel(ActionContext actioncontext,OggettoBulk oggettobulk) {
		super.setModel(actioncontext, oggettobulk);
	}
	public boolean isShrinkable() {
		if (getModel() != null){
			return super.isShrinkable() && 
			       !((Pdg_modulo_speseBulk)getModel()).isSpeseScaricateDalPersonale()&&
			       isCdrAccentratore((Pdg_modulo_speseBulk)getModel());
		}
		return super.isShrinkable();
	}
	public boolean isInputReadonly() {
		if (getModel() != null){
			return super.isInputReadonly() || ((Pdg_modulo_speseBulk)getModel()).isSpeseScaricateDalPersonale();
		}		
		return super.isInputReadonly();
	}
	public boolean isCdrAccentratore(Pdg_modulo_speseBulk pdg_modulo_spese){
		if (pdg_modulo_spese != null &&
		    pdg_modulo_spese.getCrudStatus() == OggettoBulk.NORMAL &&
		    pdg_modulo_spese.getClassificazione() != null && 
		    pdg_modulo_spese.getClassificazione().getCentro_responsabilita() != null &&
		    pdg_modulo_spese.getClassificazione().getCentro_responsabilita().getCd_centro_responsabilita() != null && 
		    pdg_modulo_spese.getPdg_modulo_costi().getPdg_modulo().getCdr().equalsByPrimaryKey(pdg_modulo_spese.getClassificazione().getCentro_responsabilita())&&
		    (!pdg_modulo_spese.getIm_spese_gest_accentrata_int().equals(Utility.ZERO)||
		     !pdg_modulo_spese.getIm_spese_gest_accentrata_est().equals(Utility.ZERO)))
		       return false;
		return true;
	}
	private CRUDDettagliModuloCostiBP geModuloCosti(){
		return (CRUDDettagliModuloCostiBP)getParentController();
	}
	public String getLabelCd_classificazione(){
		return geModuloCosti().getDescrizioneClassificazione();
	}
	public String getLabelClassificazione(){
		return geModuloCosti().getDescrizioneClassificazione();
	}
	public String getLabelClassificazione_codlast(){
		return geModuloCosti().getDescrizioneClassificazione();
	}
	public String getLabelTot_competenza_anno_in_corso(){
		return "Totale competenza "+geModuloCosti().getAnno_corrente()+" (A+B)";
	}
	public String getColumnLabelTot_competenza_anno_in_corso(){
		return geModuloCosti().getAnno_corrente()+" (A+B)";
	}
	public String getLabelIm_spese_a2(){
		return "Totale competenza "+geModuloCosti().getAnno_successivo();
	}
	public String getColumnLabelIm_spese_a2(){
		return geModuloCosti().getAnno_successivo();
	}
	public String getLabelLabel_previsione_assestata_impegno(){
		return "Previsione Assestata di Impegno "+geModuloCosti().getAnno_precedente();
	}
	public String getHeaderLabelPrev_ass_imp_int(){
		return "Prev Ass. impegni "+geModuloCosti().getAnno_precedente();
	}
	public String getHeaderLabelPrev_ass_imp_est(){
		return "Prev Ass. impegni "+geModuloCosti().getAnno_precedente();
	}
	public String getColumnLabelIm_spese_a3(){
		return geModuloCosti().getAnno_successivo_successivo();
	}
	public String getLabelIm_spese_a3(){
		return "Totale competenza " + geModuloCosti().getAnno_successivo_successivo();
	}
}
