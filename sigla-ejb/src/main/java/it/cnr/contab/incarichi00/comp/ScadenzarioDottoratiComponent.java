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

package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.52)
 *
 * @author: Roberto Fantino
 */
public class ScadenzarioDottoratiComponent extends it.cnr.jada.comp.CRUDComponent {
    /**
     * CompensoComponent constructor comment.
     */
    public ScadenzarioDottoratiComponent() {
        super();
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectTerzoByClause(
            UserContext userContext, ScadenzarioDottoratiBulk scadenzarioDottorati,
            TerzoBulk terzo, CompoundFindClause clauses
    ) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("anagrafico", "ANAGRAFICO");
        sql.addClause(FindClause.AND, "ti_terzo", SQLBuilder.NOT_EQUALS, "C");
        sql.addSQLClause(FindClause.AND, "cognome", SQLBuilder.CONTAINS, scadenzarioDottorati.getCognome());
        sql.addSQLClause(FindClause.AND, "nome", SQLBuilder.CONTAINS, scadenzarioDottorati.getNome());
        sql.addSQLClause(FindClause.AND, "ragione_sociale", SQLBuilder.CONTAINS, scadenzarioDottorati.getRagioneSociale());
        sql.addSQLClause(FindClause.AND, "codice_fiscale", SQLBuilder.CONTAINS, scadenzarioDottorati.getCodiceFiscale());
        sql.addSQLClause(FindClause.AND, "partita_iva", SQLBuilder.CONTAINS, scadenzarioDottorati.getPartitaIva());
        sql.addClause(clauses);
        return sql;
    }

    public ScadenzarioDottoratiBulk completaTerzo(UserContext userContext, ScadenzarioDottoratiBulk scadenzarioDottoratiBulk, TerzoBulk terzoBulk) throws ComponentException {
        TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
        try {
            scadenzarioDottoratiBulk.setModalita(terzoHome.findRif_modalita_pagamento(terzoBulk));
            scadenzarioDottoratiBulk.setTermini(terzoHome.findRif_termini_pagamento(terzoBulk));
        } catch (PersistencyException | IntrospectionException e) {
            throw handleException(e);
        }
        return scadenzarioDottoratiBulk;
    }

    /**
     * Percipiente selezionato
     * PreCondition:
     * Viene richiesta la lista delle Modalità di pagamento
     * associate al percipiente
     * PostCondition:
     * Viene restituita la lista dei Modalità di pagamento
     * associate al percipiente
     * <p>
     * Percipiente NON selezionato
     * PreCondition:
     * Non è stato selezionato il percipiente
     * PostCondition:
     * Non vengono caricate le modalità di pagamento
     **/

    public java.util.Collection findModalita(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {
        try {
            ScadenzarioDottoratiBulk scadenzarioDottoratiBulk = (ScadenzarioDottoratiBulk) bulk;
            if (scadenzarioDottoratiBulk.getTerzo() == null ||
                    scadenzarioDottoratiBulk.getTerzo().getCd_terzo() == null)
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
            return terzoHome.findRif_modalita_pagamento(scadenzarioDottoratiBulk.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }
    }
    /**
     * Percipiente selezionato
     *    PreCondition:
     *		 Viene richiesta la lista dei Termini di pagamento
     * 	 associati al terzo
     *	   PostCondition:
     *		 Viene restituita la lista dei Termini di pagamento
     * 	 associati al terzo
     *
     * Percipiente NON selezionato
     *    PreCondition:
     *		 Non è stato selezionato il terzo
     *	   PostCondition:
     *		 Non vengono caricati i termini di pagamento
     **/
    public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException{
        try {
            ScadenzarioDottoratiBulk scadenzarioDottoratiBulk = (ScadenzarioDottoratiBulk)bulk;
            if(scadenzarioDottoratiBulk.getTerzo() == null ||
                    scadenzarioDottoratiBulk.getTerzo().getCd_terzo() == null)
                return null;
            TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
            return terzoHome.findRif_termini_pagamento(scadenzarioDottoratiBulk.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex){
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex){
            throw handleException(bulk, ex);
        }
    }
}
