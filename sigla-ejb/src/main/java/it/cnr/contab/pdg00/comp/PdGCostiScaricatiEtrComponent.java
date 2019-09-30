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

package it.cnr.contab.pdg00.comp;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IMultipleCRUDMgr;

import java.io.Serializable;
import java.util.Vector;

/**
  * Componente di gestione nel servito dei costi scricati verso altro cdr (Entrate)
  */

public class PdGCostiScaricatiEtrComponent extends PdGComponent implements Cloneable,Serializable,IPdGCostiScaricatiMgr {
/**
 * PdGCostiScaricatiEtrComponent constructor comment.
 */
public PdGCostiScaricatiEtrComponent() {
	super();
}

    //^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
//^^@@
	public void eliminaConBulk (it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare il dettaglio caricato!");
	}

    //^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di modificare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
//^^@@
	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
//		return bulk;
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare il dettaglio caricato!");
	}

/**	
  * Costruisce un SQL builder per l'estrazione dei dettagli scaricati verso altro cdr nel servito
  */
	protected it.cnr.jada.persistency.sql.Query select(it.cnr.jada.UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
		if (clauses == null) clauses = bulk.buildFindClauses(null);
		if (clauses == null) clauses = new it.cnr.jada.persistency.sql.CompoundFindClause();

		clauses.addClause("AND",
						  "esercizio",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  ((it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)bulk).getEsercizio()
						 );
		clauses.addClause("AND",
						  "cd_centro_responsabilita",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  ((it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)bulk).getCd_centro_responsabilita()
						 );
		clauses.addClause("AND",
						  "categoria_dettaglio",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk.CAT_SCARICO
						 );

		clauses.addClause("AND",
						  "cd_centro_responsabilita_clge",
						  it.cnr.jada.persistency.sql.SQLBuilder.ISNOTNULL,
						  null
						 );

		return super.select(userContext,clauses,bulk);
	}

}
