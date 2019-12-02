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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class DivisaBulk extends DivisaBase {
	protected DivisaBulk divisa;
	public DivisaBulk() {
		super();
	}
	public DivisaBulk(java.lang.String cd_divisa) {
		super(cd_divisa);
	}
	/*
	 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
	 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
	 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
	 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
	 */
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		divisa=new DivisaBulk();

		return this;
	}
	public OggettoBulk initializeForEdit(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {


		super.initializeForEdit(bp,context);

		return this;
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);

		if (getFl_calcola_con_diviso()==null)
			setFl_calcola_con_diviso(Boolean.FALSE);

		return this;
	}
	public void validate() throws ValidationException {
		super.validate();
		if (getFl_calcola_con_diviso()==null)
			setFl_calcola_con_diviso(new Boolean(false));

		try{
			if (getPrecisione()!=null && (getPrecisione().intValue()<0 || getPrecisione().intValue()>=10))
				throw new ValidationException("Valore per la precisione non ammesso");	
		}
		catch (java.lang.NumberFormatException nfe){
			throw new ValidationException("Attenzione! Inserire numeri interi");
		}
	}
}
