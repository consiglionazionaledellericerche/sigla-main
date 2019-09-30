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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_approvato_dip_areaBulk extends Pdg_approvato_dip_areaBase {
	
	private BulkList dettagliContrSpese = new BulkList();
	private DipartimentoBulk dipartimento;
	private CdsBulk area;
	private BigDecimal importo_ripartito;
	private boolean utenteDipartimento = false;

	public Pdg_approvato_dip_areaBulk() {
		super();
	}
	public Pdg_approvato_dip_areaBulk(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_dipartimento, pg_dettaglio);
		setDipartimento(new DipartimentoBulk(cd_dipartimento));
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
	{
		inizializzaImporti();		
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		if (it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context)!=null) {
			setDipartimento(it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context));
			setUtenteDipartimento(true);
		}
		return super.initializeForInsert(bp, context);
	}
	public BulkList getDettagliContrSpese() {
		return dettagliContrSpese;
	}
	public void setDettagliContrSpese(BulkList dettagliContrSpese) {
		this.dettagliContrSpese = dettagliContrSpese;
	}
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}
	public void setDipartimento(DipartimentoBulk dipartimento) {
		this.dipartimento = dipartimento;
	}
	public String getCd_dipartimento() {
		DipartimentoBulk dipartimento=this.getDipartimento();
		if(dipartimento==null)
			return null;
		return dipartimento.getCd_dipartimento();
	}
	public void setCd_dipartimento(String cd_dipartimento) {
		dipartimento.setCd_dipartimento(cd_dipartimento);
	}

	public java.lang.String getCd_cds_area () {
		if(getArea() != null)
		  return getArea().getCd_unita_organizzativa();
		return null;  
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		if(getArea() != null)
		   getArea().setCd_unita_organizzativa(cd_cds_area);
		super.setCd_cds_area(cd_cds_area);   
	}
	public CdsBulk getArea() {
		return area;
	}
	public void setArea(CdsBulk bulk) {
		area = bulk;
	}
	private void inizializzaImporti(){
		setImporto_approvato(Utility.ZERO);
		setImporto_ripartito(Utility.ZERO);
	}
	public boolean isROrigates() {
		if (!isToBeCreated())
			return true;
		else
			return false;
	}
	public boolean isRODipartimento() {
		return isROrigates()||isUtenteDipartimento();
	}
	public boolean isROImporto_approvato() {
		return isUtenteDipartimento();
	}
	public BigDecimal getImporto_ripartito() {
		return importo_ripartito;
	}
	public void setImporto_ripartito(BigDecimal importo_ripartito) {
		this.importo_ripartito = importo_ripartito;
	}
	public void setUtenteDipartimento(boolean newUtenteDipartimento) {
		utenteDipartimento = newUtenteDipartimento;
	}
	private boolean isUtenteDipartimento() {
		return utenteDipartimento;
	}
}