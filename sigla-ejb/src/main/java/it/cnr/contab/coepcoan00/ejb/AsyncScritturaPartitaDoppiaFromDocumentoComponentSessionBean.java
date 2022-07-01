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

package it.cnr.contab.coepcoan00.ejb;

import it.cnr.contab.coepcoan00.comp.ScritturaPartitaDoppiaFromDocumentoComponent;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless(name = "CNRCOEPCOAN00_EJB_AsyncScritturaPartitaDoppiaFromDocumentoComponentSession")
public class AsyncScritturaPartitaDoppiaFromDocumentoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AsyncScritturaPartitaDoppiaFromDocumentoComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new AsyncScritturaPartitaDoppiaFromDocumentoComponentSessionBean();
    }

	@Asynchronous
	public void asyncLoadScritturePatrimoniali(UserContext param0, Integer param1, String param2) throws ComponentException, PersistencyException, RemoteException {
		ScritturaPartitaDoppiaFromDocumentoComponentSession session = Utility.createScritturaPartitaDoppiaFromDocumentoComponentSession();
		List<IDocumentoCogeBulk> allDocuments = session.getAllDocumentiCoge(param0, param1, param2);
		Collection<List<IDocumentoCogeBulk>> splitList = (Collection<List<IDocumentoCogeBulk>>) Utility.splitListBySize(
				allDocuments.stream().filter(el->Optional.ofNullable(el.getDt_contabilizzazione()).isPresent())
						.sorted(Comparator.comparing(IDocumentoCogeBulk::getDt_contabilizzazione))
						.collect(Collectors.toList()), 100);

		splitList.stream().forEach(el->{
			try {
				session.loadScritturePatrimoniali(param0, el);
			} catch (Exception e) {
				throw new DetailedRuntimeException(e);
			}
		});
	}
}
