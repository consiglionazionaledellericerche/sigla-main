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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CrudDettagliEntrataBP extends SimpleDetailCRUDController{
	public CrudDettagliEntrataBP(String s, Class class1, String s1, FormController formcontroller) {
		super(s, class1, s1, formcontroller);
		
	}
	private CRUDPdg_Modulo_EntrateBP getEntrata(){
			return (CRUDPdg_Modulo_EntrateBP)getParentController();
	}
	protected void setModel(ActionContext actioncontext,OggettoBulk oggettobulk) {
		super.setModel(actioncontext, oggettobulk);
	}
	public String getLabelCd_classificazione(){
		return getEntrata().getDescrizioneClassificazione();
	}
	public String getLabelFind_classificazione_voci(){
		return getEntrata().getDescrizioneClassificazione();
	}
	public String getLabelFind_classificazione_voci_codlast(){
		return getEntrata().getDescrizioneClassificazione();
	}
	public String getColumnLabelCd_classificazione(){
			return getEntrata().getDescrizioneClassificazione();
	}
	public String getColumnLabelFind_classificazione_voci(){
		return getEntrata().getDescrizioneClassificazione();
	}
	public String getLabelIm_entrata(){
		return "Previsione "+ getEntrata().getAnno_corrente();
	}
	public String getLabelIm_entrata_app(){
		return "Approvato "+ getEntrata().getAnno_corrente();
	}
	public String getLabelIm_entrata_a2(){
		return "Approvato "+ getEntrata().getAnno_successivo();
	}
	public String getLabelIm_entrata_a3(){
		return "Approvato "+ getEntrata().getAnno_successivo_successivo();
	}
	public String getLabelIm_spese_vive(){
		return "Imp.Spese<BR>vive correlate<BR>all'entrata "+ getEntrata().getAnno_corrente();
	}
	public String getLabelDs_spese_vive(){
		return "Desc.Spese<BR>vive correlate<BR>all'entrata "+ getEntrata().getAnno_corrente();
	}
	public String getColumnLabelIm_entrata(){
		return "Previsione "+ getEntrata().getAnno_corrente();
	}
	public String getColumnLabelIm_entrata_app(){
		return "Approvato "+ getEntrata().getAnno_corrente();
	}
	public String getColumnLabelIm_entrata_a2(){
		return "Approvato "+ getEntrata().getAnno_successivo();
	}
	public String getColumnLabelIm_entrata_a3(){
		return "Approvato "+ getEntrata().getAnno_successivo_successivo();
	}
	public String getColumnLabelIm_spese_vive(){
		return "Imp.Spese<BR>vive correlate<BR>all'entrata "+getEntrata().getAnno_corrente();
	}
	public String getColumnLabelDs_spese_vive(){
		return "Desc.Spese<BR>vive correlate<BR>all'entrata "+getEntrata().getAnno_corrente();
	}  
	public String getHeaderLabelIm_entrata(){
		return "Previsione "+ getEntrata().getAnno_corrente();
	} 
	public String getHeaderLabelIm_entrata_app(){
		return "Previsione "+ getEntrata().getAnno_corrente();
	}
	public boolean isGrowable() {
		return super.isGrowable()&&!getEntrata().isUtente_Ente()&&!getEntrata().isPDGPEsaminatoDalCentroUP();
	}
	public boolean isShrinkable() {
		return super.isShrinkable()&&!getEntrata().isUtente_Ente()&&!getEntrata().isPDGPEsaminatoDalCentroUP();
	}
}
