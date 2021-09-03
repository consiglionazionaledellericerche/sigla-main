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

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.compensi00.comp.IMinicarrieraMgr;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.NumerazioneTempDocAmmComponentSession;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.52)
 * @author: Roberto Fantino
 */
public class ScadenzarioDottoratiComponent extends it.cnr.jada.comp.CRUDComponent {
	/**
	 * CompensoComponent constructor comment.
	 */
	public ScadenzarioDottoratiComponent() {
		super();
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectTerzoByClause(UserContext userContext, ScadenzarioDottoratiBulk scadenzarioDottorati, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
		sql.addClause(FindClause.AND,"ti_terzo",SQLBuilder.NOT_EQUALS,"C");
		sql.addClause( clauses );
		return sql;
	}
}

