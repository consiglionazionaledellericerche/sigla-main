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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
public class MovimentiMagHome extends BulkHome {
	public MovimentiMagHome(Connection conn) {
		super(MovimentiMagBulk.class, conn);
	}
	public MovimentiMagHome(Connection conn, PersistentCache persistentCache) {
		super(MovimentiMagBulk.class, conn, persistentCache);
	}
	public Long recuperoProgressivoMovimento(it.cnr.jada.UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_MOVIMENTI_MAG").longValue());
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		MovimentiMagBulk movimento = (MovimentiMagBulk)bulk;
		if (movimento.getPgMovimento() == null)
			movimento.setPgMovimento(recuperoProgressivoMovimento(userContext));
	}
	@Override
	public void insert(Persistent persistent, UserContext userContext) throws PersistencyException {
		MovimentiMagBulk movimentiMag = (MovimentiMagBulk)persistent;
    	LottoMagHome lottoHome = (LottoMagHome)getHomeCache().getHome(LottoMagBulk.class);
    	LottoMagBulk lotto;
		try {
			lotto = (LottoMagBulk)lottoHome.findAndLock(movimentiMag.getLottoMag());
		} catch (OutdatedResourceException | BusyResourceException e) {
			throw new PersistencyException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
		lotto = lottoHome.aggiornaValori(userContext, lotto, movimentiMag);
		lotto.setToBeUpdated();
		lottoHome.update(lotto, userContext);
		super.insert(persistent, userContext);
	}
}