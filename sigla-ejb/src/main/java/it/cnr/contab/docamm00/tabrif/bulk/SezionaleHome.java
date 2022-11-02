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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.List;

public class SezionaleHome extends BulkHome {
	public SezionaleHome(java.sql.Connection conn) {
		super(SezionaleBulk.class,conn);
	}
	public SezionaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(SezionaleBulk.class,conn,persistentCache);
	}
	public boolean verificaStatoEsercizio(SezionaleBulk sezionale) throws PersistencyException, IntrospectionException {

		it.cnr.contab.config00.esercizio.bulk.EsercizioBulk esercizio = (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk) getHomeCache().getHome(it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class).findByPrimaryKey(
			new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( sezionale.getCd_cds(), sezionale.getEsercizio()));
		if (esercizio == null || esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()))
			return false;
		return true;
	}

	public SezionaleBulk getSezionaleByTipoDocumento(IDocumentoAmministrativoBulk docamm) throws PersistencyException, ComponentException {
		if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoAttivo() || docamm.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo()) {
			SQLBuilder sql = this.createSQLBuilder();
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, docamm.getEsercizio());
			sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, docamm.getCd_cds());
			sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, docamm.getCd_uo());

			String cdTipoSezionale;
			if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoAttivo())
				cdTipoSezionale = ((Fattura_attivaBulk) docamm).getCd_tipo_sezionale();
			else
				cdTipoSezionale = ((Fattura_passivaBulk) docamm).getCd_tipo_sezionale();

			String tiFattura = null;
			if (docamm.getTipoDocumentoEnum().isFatturaAttiva() || docamm.getTipoDocumentoEnum().isFatturaPassiva())
				tiFattura = SezionaleBulk.FATTURA;
			else if (docamm.getTipoDocumentoEnum().isNotaCreditoAttiva() || docamm.getTipoDocumentoEnum().isNotaCreditoPassiva())
				tiFattura = SezionaleBulk.NOTACREDITO;
			else if (docamm.getTipoDocumentoEnum().isNotaDebitoAttiva() || docamm.getTipoDocumentoEnum().isNotaDebitoPassiva())
				tiFattura = SezionaleBulk.NOTADEBITO;

			if (cdTipoSezionale!=null && tiFattura!=null)
				return this.getSezionaleByTipoDocumento(docamm.getEsercizio(), docamm.getCd_cds(), docamm.getCd_uo(), cdTipoSezionale, tiFattura);
		}
		return null;
	}

	public SezionaleBulk getSezionaleByTipoDocumento(AutofatturaBulk autofatturaBulk) throws PersistencyException, ComponentException {
		return this.getSezionaleByTipoDocumento(autofatturaBulk.getEsercizio(), autofatturaBulk.getCd_cds(), autofatturaBulk.getCd_unita_organizzativa(), autofatturaBulk.getCd_tipo_sezionale(), autofatturaBulk.getTi_fattura());
	}

	private SezionaleBulk getSezionaleByTipoDocumento(Integer aEsercizio, String aCdCds, String aCdUo, String aCdTipoSezionale, String aTiFattura) throws PersistencyException, ComponentException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, aEsercizio);
		sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, aCdCds);
		sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, aCdUo);
		sql.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, aCdTipoSezionale);
		sql.addClause(FindClause.AND, "ti_fattura", SQLBuilder.EQUALS, aTiFattura);

		List<SezionaleBulk> result = this.fetchAll(sql);
		if (result.size()>1)
			throw new ApplicationException("Errore nei dati: nella tabella SEZIONALE esistono per l'esercizio "+aEsercizio+", per il cds "+ aCdCds +
					". per la UO "+aCdUo+", per il tipo sezionale "+aCdTipoSezionale+" e per la tipologia "+aTiFattura+" più righe valide!");
		return result.stream().findFirst().orElse(null);
	}
}
