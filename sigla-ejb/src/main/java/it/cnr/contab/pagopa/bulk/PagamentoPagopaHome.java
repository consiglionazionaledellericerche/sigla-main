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

package it.cnr.contab.pagopa.bulk;

import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoRigaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.Orderable;

import java.math.BigDecimal;
import java.util.List;

public class PagamentoPagopaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public PagamentoPagopaHome(java.sql.Connection conn) {
		super(PagamentoPagopaBulk.class,conn);
	}
	public PagamentoPagopaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(PagamentoPagopaBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			PagamentoPagopaBulk PagamentoPagopaBulk = (PagamentoPagopaBulk)oggettobulk;
			PagamentoPagopaBulk.setId(
					new Long(((Long)findAndLockMax( oggettobulk, "id", new Long(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, PagamentoPagopaBulk);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

	@Override
	public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
//		((PagamentoPagopaBulk)persistent).setStato(PagamentoPagopaBulk.STATO_ANNULLATO);
		super.update(persistent, userContext);
	}
	public SQLBuilder createSQLBuilder() {

		SQLBuilder sql = super.createSQLBuilder();
//		sql.addSQLClause("AND", "STATO", sql.NOT_EQUALS, PagamentoPagopaBulk.STATO_ANNULLATO);
		return sql;
	}

	public PagamentoPagopaBulk findPagamentoPagopa(UserContext userContext, String iur) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();

		sql.addClause(FindClause.AND, "iur", SQLBuilder.EQUALS, iur);
		List lista =  this.fetchAll(sql);
		if (lista != null){
			if (lista.size() == 1){
				return (PagamentoPagopaBulk) lista.get(0);
			} else {
				throw new PersistencyException("Esistono sull'archivio pagamenti PagoPA diverse righe con IUR "+iur);
			}
		}
		return null;
	}
	public PagamentoPagopaBulk findPagamentoPagopa(UserContext userContext, Long idPendenza) throws PersistencyException
	{
		SQLBuilder sql = searchPagamenti(idPendenza);
		List lista =  this.fetchAll(sql);
		if (lista != null){
			if (lista.size() == 1){
				return (PagamentoPagopaBulk) lista.get(0);
			} else {
				throw new PersistencyException("Esistono sull'archivio pagamenti PagoPA diverse righe con id Perndenza "+idPendenza);
			}
		}
		return null;
	}

	public SQLBuilder searchPagamenti(Long idPendenza) {
		SQLBuilder sql = this.createSQLBuilder();

		sql.addSQLClause(FindClause.AND, "id_pendenza_pagopa", SQLBuilder.EQUALS, idPendenza);
		return sql;
	}

}
