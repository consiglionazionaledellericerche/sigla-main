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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

/**
 * Gestione dei dati relativi alla tabella Dichiarazione_intento
 */

public class Dichiarazione_intentoBulk extends Dichiarazione_intentoBase {

	private AnagraficoBulk anagrafico;
public Dichiarazione_intentoBulk() { 
	super();
}
public Dichiarazione_intentoBulk(java.lang.Integer cd_anag,java.lang.Integer esercizio,java.lang.Integer progr) {
	super(cd_anag,esercizio,progr);
}
	/**
	 * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 *
	 * @see setAnagrafico
	 */

	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
public java.lang.Integer getCd_anag() {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
	if (anagrafico == null)
		return null;
	return anagrafico.getCd_anag();
}
	/**
	 * Imposta l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @param newAnagrafico Anagrafica di riferimento.
	 *
	 * @see getAnagrafico
	 */

	public void setAnagrafico(AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}
public void setCd_anag(java.lang.Integer cd_anag) {
	this.getAnagrafico().setCd_anag(cd_anag);
}

public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setFl_acquisti(Boolean.FALSE);
	setFl_importazioni(Boolean.FALSE);
	setDt_fin_validita(null);
	setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
	return super.initializeForInsert(bp,context);
}
public void validate(OggettoBulk parent) throws ValidationException {
	super.validate(parent);
	if (getFl_acquisti() && getFl_importazioni()) 
		throw new ValidationException("L'opzione acquisti e importazione non possono essere entrambi indicati.");
	 else  if(!getFl_acquisti() && !getFl_importazioni()) 
		throw new ValidationException("Scegliere un opzione tra acquisti e importazione.");
	if((getDt_inizio_val_dich()!=null && getDt_fine_val_dich()==null)||
		(getDt_inizio_val_dich()==null && getDt_fine_val_dich()!=null))
		throw new ValidationException("Indicare sia la data di inizio e fine validità.");
	if(getDt_inizio_val_dich()!=null &&  getDt_fine_val_dich()!=null && getDt_inizio_val_dich().after(getDt_fine_val_dich()))
		throw new ValidationException("La data di inizio del periodo di riferimento deve essere antecedente alla data di fine.");
	if(getDt_inizio_val_dich()!=null &&  getDt_fine_val_dich()!=null &&
			(getIm_limite_op()!=null ||getIm_limite_sing_op()!=null))
		throw new ValidationException("La dichiarazione è valida o per periodo di riferimento o per importo.");
	if(getDt_inizio_val_dich()==null &&  getDt_fine_val_dich()==null &&
			((getIm_limite_op()!=null && getIm_limite_sing_op()!=null)))
		throw new ValidationException("Indicare solo uno degli importi limite.");
	if(getDt_inizio_val_dich()==null &&  getDt_fine_val_dich()==null &&
			((getIm_limite_op()==null && getIm_limite_sing_op()==null)))
		throw new ValidationException("Indicare il periodo di riferimento o uno degli importi limite.");
	if(getDt_ini_validita()!=null &&  getDt_fin_validita()!=null && getDt_ini_validita().after(getDt_fin_validita()))
		throw new ValidationException("La data di inizio validità deve essere antecedente alla data di fine.");
	if(getId_dichiarazione()!=null && getId_dichiarazione().length()!=17)
		throw new ValidationException("Il codice telematico deve essere di 17 caratteri.");
}

public boolean isRODichiarazione() {
 return (!getAnagrafico().isUo_ente()); 
}
}
