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
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;

import it.cnr.contab.consultazioni.bp.ConsObbligazioniBP;
import it.cnr.contab.consultazioni.bp.RicercaLiberaConsObbligazioniBP;
import it.cnr.contab.consultazioni.bulk.ConsObbligazioniBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.*;

import java.util.Optional;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsObbligazioniAction extends SelezionatoreListaAction {

	@Override
	public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
		return actioncontext.findDefaultForward();
	}

	public Forward doCloseForm(ActionContext context)
		throws BusinessProcessException
	{
		try
		{
			ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
//			bp.setFindclause(null);
			return super.doCloseForm(context);
		}
		catch(Throwable throwable)
		{
			return handleException(context, throwable);
		}
	}
	public Forward doRicercaLibera(ActionContext context) {
		ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
		RicercaLiberaConsObbligazioniBP ricercaLiberaBP = null;
		try {

			Optional<CRUDBP> crudbp = Optional.ofNullable(bp.getParent())
					.filter(CRUDBP.class::isInstance)
					.map(CRUDBP.class::cast);

			if (crudbp.isPresent() ||
					Optional.ofNullable(bp)
							.filter(SearchProvider.class::isInstance)
							.isPresent() ||
					Optional.ofNullable(bp.getParent())
							.filter(SearchProvider.class::isInstance)
							.isPresent()

			) {
				SearchProvider searchProvider = Optional.ofNullable(bp.getFormField())
						.map(formField -> crudbp.get().getSearchProvider(
								formField.getModel(),
								formField.getField().getProperty()))
						.map(SearchProvider.class::cast)
						.orElseGet(() -> {
							if (crudbp.isPresent()) {
								return crudbp.get().getSearchProvider();
							} else {
								return Optional.ofNullable(bp)
										.filter(SearchProvider.class::isInstance)
										.map(SearchProvider.class::cast)
										.orElseGet(() -> Optional.ofNullable(bp.getParent())
												.filter(SearchProvider.class::isInstance)
												.map(SearchProvider.class::cast)
												.orElse(null));
							}
						});

				OggettoBulk oggettoBulk = Optional.ofNullable(bp.getFormField())
						.map(formField -> bp.getBulkInfo())
						.map(bulkInfo -> bulkInfo.getBulkClass())
						.map(aClass -> {
							try {
								return aClass.newInstance();
							} catch (InstantiationException | IllegalAccessException e) {
								throw new DetailedRuntimeException(e);
							}
						})
						.filter(OggettoBulk.class::isInstance)
						.map(OggettoBulk.class::cast)
						.orElseGet(() -> {
							if (Optional.ofNullable(bp).filter(SelezionatoreSearchBP.class::isInstance).isPresent())
								return bp.getModel();
							if (crudbp.isPresent()) {
								try {
									return crudbp.get().createEmptyModelForFreeSearch(context);
								} catch (BusinessProcessException e) {
									throw new DetailedRuntimeException(e);
								}
							} else {
								return bp.getModel();
							}
						});

				ricercaLiberaBP = (RicercaLiberaConsObbligazioniBP) context.createBusinessProcess("RicercaLiberaConsObbligazioni");
				ricercaLiberaBP.setSearchProvider(bp);
				ricercaLiberaBP.setFreeSearchSet(
						Optional.ofNullable(bp.getFormField())
								.filter(formField -> Optional.ofNullable(formField.getField()).isPresent())
								.map(formField -> formField.getField())
								.map(fieldProperty -> fieldProperty.getFreeSearchSet())
								.orElseGet(() -> {
									if (crudbp.isPresent())
										return crudbp.get().getFreeSearchSet();
									else
										return "default";
								})
				);
				ricercaLiberaBP.setShowSearchResult(true);
				ricercaLiberaBP.setCanPerformSearchWithoutClauses(true);
				ricercaLiberaBP.setPrototype(oggettoBulk);
				context.addHookForward("close",this,"doDefault");
			}
			context.addBusinessProcess(ricercaLiberaBP);
			return context.findDefaultForward();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

}
