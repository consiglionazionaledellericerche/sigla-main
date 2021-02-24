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

package it.cnr.contab.config00.comp;


import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LimiteSpesaClassComponent extends it.cnr.jada.comp.CRUDComponent implements  java.io.Serializable, Cloneable {
	public LimiteSpesaClassComponent()
	{
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
		try
		{
			bulk = super.inizializzaBulkPerModifica(userContext,bulk);
			V_classificazione_vociBulk classificazione=(V_classificazione_vociBulk)bulk;
			LimiteSpesaClassHome dettHome = (LimiteSpesaClassHome)getHome(userContext,LimiteSpesaClassBulk.class);
			classificazione.setLimitiSpesaClassColl(new BulkList(dettHome.getDetailsFor(classificazione)));
			getHomeCache(userContext).fetchAll(userContext);
			return classificazione;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	@Override
	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			V_classificazione_vociBulk model = (V_classificazione_vociBulk)oggettobulk;

			//Recupero il valore del campo sulla View e lo aggiorno sul record Classififcazione_voci.
			Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(usercontext, Classificazione_vociBulk.class)
					.findByPrimaryKey(new Classificazione_vociBulk(model.getId_classificazione()));
			if (Optional.ofNullable(classif.getIm_limite_assestato()).orElse(BigDecimal.ZERO)
					.compareTo(Optional.ofNullable(model.getIm_limite_assestato()).orElse(BigDecimal.ZERO))!=0) {
				classif.setIm_limite_assestato(model.getIm_limite_assestato());
				classif.setToBeUpdated();
				getHome(usercontext, Classificazione_vociBulk.class).update(classif, usercontext);
			}

			if (Optional.ofNullable(model.getLimitiSpesaClassColl()).isPresent())
				this.makeBulkListPersistent(usercontext, model.getLimitiSpesaClassColl(), Boolean.TRUE);

			return inizializzaBulkPerModifica(usercontext,oggettobulk);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	@Override
	protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		SQLBuilder sql =  (SQLBuilder)super.select(usercontext, compoundfindclause, oggettobulk);
		sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		return sql;
	}

	public void validaCds(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		try{
			LimiteSpesaClassBulk limite=(LimiteSpesaClassBulk)bulk;
			if (limite.isToBeCreated()){
				SQLBuilder sql = getHome(userContext, LimiteSpesaClassBulk.class).createSQLBuilder();
				sql.addClause(FindClause.AND, "id_classificazione", SQLBuilder.EQUALS,limite.getId_classificazione());
				sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, limite.getCd_cds());
				if (sql.executeCountQuery(getConnection(userContext))>0)
					throw new ApplicationException("Esiste già un limite definito per questa classificazione e per il Cds: "+limite.getCd_cds());
			}
		}catch (Throwable e) {
			throw handleException(bulk,e);
		}
	}
}
