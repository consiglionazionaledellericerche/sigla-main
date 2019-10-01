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

package it.cnr.contab.bollo00.comp;

import java.sql.Timestamp;
import java.util.Optional;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class TipoAttoBolloComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Tipo_atto_bolloBulk) {
			try {
				Tipo_atto_bolloBulk bulk = (Tipo_atto_bolloBulk)oggettobulk;
				//verifico che per per le date di validità del bollo da salvare non esistano altri bolli validi 
				Tipo_atto_bolloHome home = (Tipo_atto_bolloHome)getHome(usercontext, Tipo_atto_bolloBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(FindClause.AND,"codice",SQLBuilder.EQUALS, bulk.getCodice());
				Optional.ofNullable(bulk.getId()).ifPresent(el->sql.addClause(FindClause.AND,"id",SQLBuilder.NOT_EQUALS, bulk.getId()));
				sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.ISNULL, null);
				sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.GREATER_EQUALS, bulk.getDtIniValidita());
				sql.closeParenthesis();
	
				Optional.ofNullable(bulk.getDtFinValidita()).ifPresent(dtFine->{
					sql.addClause(FindClause.AND,"dtIniValidita",SQLBuilder.LESS_EQUALS, dtFine );
				});
				
				if (sql.executeExistsQuery(getConnection(usercontext)))
					throw new ApplicationException("Record non aggiornabile in quanto esiste per lo stesso codice atto un record con date di validità che si sovrappongono.");
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}
	public Tipo_atto_bolloBulk getTipoAttoBollo(it.cnr.jada.UserContext param0, Timestamp data, java.lang.String codiceTipoAttoBollo) throws it.cnr.jada.comp.ComponentException{
		try {
			return ((Tipo_atto_bolloHome)getHome(param0, Tipo_atto_bolloBulk.class)).findByCodiceAndData(codiceTipoAttoBollo, data);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
}
