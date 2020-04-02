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
 * Created on 03/01/2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaClassBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaDetBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.util.Optional;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDLimiteSpesaClassBP extends SimpleCRUDBP {
	private SimpleDetailCRUDController dettagli = new SimpleDetailCRUDController( "Dettagli", LimiteSpesaClassBulk.class, "limitiSpesaClassColl", this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			try {
				LimiteSpesaClassBulk det=(LimiteSpesaClassBulk)model;
				completeSearchTools(context, this);
				if(det.getCd_cds()==null)
					throw new ValidationException("Inserire un valore per il campo Cds.");
				if(det.getIm_limite_assestato()==null)
					throw new ValidationException("Inserire un valore per il campo Importo limite.");
				((it.cnr.contab.config00.ejb.LimiteSpesaClassComponentSession)createComponentSession()).validaCds(
						context.getUserContext(),
						det);
			} catch(Throwable e) {
				throw new ValidationException(e.getMessage());
			}
		}

		@Override
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Optional.ofNullable(oggettobulk).filter(LimiteSpesaClassBulk.class::isInstance).map(LimiteSpesaClassBulk.class::cast)
					.filter(el->!el.isUtilizzato())
					.orElseThrow(()-> new ValidationException("I cds che hanno già stanziato fondi non possono essere eliminati."));
		}
	};

	public CRUDLimiteSpesaClassBP()
	{
		super();
	}

	public CRUDLimiteSpesaClassBP(String s)
	{
		super(s);
	}

	public SimpleDetailCRUDController getDettagli() {
		return dettagli;
	}

	@Override
	public boolean isNewButtonHidden() {
		return true;
	}

	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
		setModel( context, createEmptyModelForSearch(context) );
		setStatus(SEARCH);
	}
}
