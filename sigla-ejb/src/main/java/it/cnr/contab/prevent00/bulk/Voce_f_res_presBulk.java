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

package it.cnr.contab.prevent00.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

public class Voce_f_res_presBulk extends Voce_f_res_presBase 
{
	Voce_fBulk voce=new Voce_fBulk();
	String cd_cds = null;
public Voce_f_res_presBulk() {
	super();
}
public Voce_f_res_presBulk(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
	setVoce(new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk(cd_voce,esercizio,ti_appartenenza,ti_gestione));
}
public java.lang.String getCd_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = this.getVoce();
	if (voce == null)
		return null;
	return voce.getCd_voce();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = this.getVoce();
	if (voce == null)
		return null;
	return voce.getEsercizio();
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = this.getVoce();
	if (voce == null)
		return null;
	return voce.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = this.getVoce();
	if (voce == null)
		return null;
	return voce.getTi_gestione();
}
/**
 * Insert the method's description here.
 * Creation date: (17/10/2002 14.44.48)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce() {
	return voce;
}
public OggettoBulk initializeForSearch(CRUDBP bp, ActionContext context) 
{
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	
	return this;
}
/**
 * Restituisce il valore della proprietà 'ROVoce'
 *
 * @return Il valore della proprietà 'ROVoce'
 */
public boolean isROVoce() 
{
	return voce == null || voce.getCrudStatus() == NORMAL;
}
public void setCd_voce(java.lang.String cd_voce) {
	this.getVoce().setCd_voce(cd_voce);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getVoce().setEsercizio(esercizio);
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getVoce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getVoce().setTi_gestione(ti_gestione);
}
/**
 * Insert the method's description here.
 * Creation date: (17/10/2002 14.44.48)
 * @param newVoce it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoce(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoce) {
	voce = newVoce;
}
public void validate() throws ValidationException 
{
	super.validate();

	if(getCd_voce()==null)
		throw new ValidationException("Selezionare un capitolo !");
			
	if(getIm_residui_presunti()==null || getIm_residui_presunti().compareTo(new java.math.BigDecimal(0))==0)
		throw new ValidationException("Inserire l'importo !");
}

}
