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

package it.cnr.contab.doccont00.core.bulk;

import 	it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Dictionary;

/**
 * Insert the method's description here.
 * Creation date: (12/10/2005 16.00.00)
 * @author: Raffaele Pagano
 */
public class ObbligazioneRes_impropriaBulk extends ObbligazioneBulk {
	Voce_fBulk voce = new Voce_fBulk();
	private String statoResiduo;
/**
 * ObbligazioneRes_impropriaBulk constructor comment.
 */
public ObbligazioneRes_impropriaBulk() {
	super();
	initialize();	
}
/**
 * ObbligazioneRes_impropriaBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_obbligazione java.lang.Long
 */
public ObbligazioneRes_impropriaBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
	initialize();	
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2005 16.00.00)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce() {
	return voce;
}
// metodo per inizializzare l'oggetto bulk
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA );
	setFl_pgiro( new Boolean( false ));
}
/* (non-Javadoc)
 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk#initializeForInsert(it.cnr.jada.util.action.CRUDBP, it.cnr.jada.action.ActionContext)
 */
public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
	// TODO Auto-generated method stub
	super.initializeForInsert(bp, context);
	setEsercizio_originale(null);
	return this; 
}
protected OggettoBulk initialize(CRUDBP crudbp,	ActionContext actioncontext) {
	super.initialize(crudbp, actioncontext);
	caricaAnniResidui(actioncontext);
	return this; 
}
public boolean isROVoce() {
	return voce == null || voce.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2003 13.43.47)
 * @param newVoce it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoce(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoce) {
	voce = newVoce;
}
public void validate() throws ValidationException {
	// controllo su campo ESERCIZIO_ORIGINALE
	if ( getEsercizio_originale() == null || getEsercizio_originale().equals("") )
		throw new ValidationException( "Il campo ESERCIZIO IMPEGNO è obbligatorio." );

	if (getEsercizio_originale().compareTo(getEsercizio())!=-1)
		throw new ValidationException("L'esercizio dell'impegno residuo deve essere inferiore al " + getEsercizio());

	super.validate();
}
	public String getStatoResiduo() {
		return statoResiduo;
	}
	public void setStatoResiduo(String statoResiduo) {
		this.statoResiduo = statoResiduo;
	}
	@SuppressWarnings("rawtypes")
	public Dictionary getStato_ObbligazioneResiduaKeys() {
		java.util.Dictionary tiStatoObbligazioneResiduaKeys = new it.cnr.jada.util.OrderedHashtable();

		tiStatoObbligazioneResiduaKeys.put(ObbligazioneResBulk.STATORES_PAGATO, "Pagato");
		tiStatoObbligazioneResiduaKeys.put(ObbligazioneResBulk.STATORES_PAGABILE, "Pagabile");
		tiStatoObbligazioneResiduaKeys.put(ObbligazioneResBulk.STATORES_NON_PAGABILE, "Non Pagabile");
		return tiStatoObbligazioneResiduaKeys;
	}

	public boolean isLiquidabile() {
		return ObbligazioneResBulk.STATORES_PAGABILE.equals(getStatoResiduo());
	}

	public boolean isNonLiquidabile() {
		return ObbligazioneResBulk.STATORES_NON_PAGABILE.equals(getStatoResiduo());
	}
}