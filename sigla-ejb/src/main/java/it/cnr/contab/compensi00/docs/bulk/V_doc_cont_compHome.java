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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;

public class V_doc_cont_compHome extends BulkHome {
public V_doc_cont_compHome(java.sql.Connection conn) {
	super(V_doc_cont_compBulk.class,conn);
}
public V_doc_cont_compHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_doc_cont_compBulk.class,conn,persistentCache);
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
public java.util.List loadAllDocCont(CompensoBulk compenso) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_cds_compenso",sql.EQUALS,compenso.getCd_cds());
	sql.addClause("AND","cd_uo_compenso",sql.EQUALS,compenso.getCd_unita_organizzativa());
	sql.addClause("AND","esercizio_compenso",sql.EQUALS,compenso.getEsercizio());
	sql.addClause("AND","pg_compenso",sql.EQUALS,compenso.getPg_compenso());

	java.util.List l = fetchAll(sql);
	for (java.util.Iterator i = l.iterator();i.hasNext();){
		V_doc_cont_compBulk docContComp = (V_doc_cont_compBulk)i.next();
		docContComp.setManRev(loadDocCont(docContComp));
	}
	return l;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadDocCont(V_doc_cont_compBulk docContComp) throws PersistencyException{

	IManRevBulk manRev = null;
	if (docContComp.TIPO_DOC_CONT_MANDATO.equals(docContComp.getTipo_doc_cont())){
		manRev = loadMandato(docContComp);
	}

	if (docContComp.TIPO_DOC_CONT_REVERSALE.equals(docContComp.getTipo_doc_cont())){
		manRev = loadReversale(docContComp);
	}

	return manRev;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadMandato(V_doc_cont_compBulk docContComp) throws PersistencyException{

	MandatoIHome manHome = (MandatoIHome)getHomeCache().getHome(MandatoIBulk.class);
	SQLBuilder sql = manHome.createSQLBuilder();
	sql.addClause("AND","cd_cds",sql.EQUALS,docContComp.getCd_cds_doc_cont());
	sql.addClause("AND","esercizio",sql.EQUALS,docContComp.getEsercizio_doc_cont());
	sql.addClause("AND","pg_mandato",sql.EQUALS,docContComp.getPg_doc_cont());

	Broker broker = createBroker(sql);
	IManRevBulk manRev = null;
	if (broker.next())
		manRev = (IManRevBulk)manHome.fetch(broker);
	broker.close();

	return manRev;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadReversale(V_doc_cont_compBulk docContComp) throws PersistencyException{

	ReversaleIHome revHome = (ReversaleIHome)getHomeCache().getHome(ReversaleIBulk.class);
	SQLBuilder sql = revHome.createSQLBuilder();
	sql.addClause("AND","cd_cds", sql.EQUALS, docContComp.getCd_cds_doc_cont());
	sql.addClause("AND","esercizio", sql.EQUALS, docContComp.getEsercizio_doc_cont());
	sql.addClause("AND","pg_reversale", sql.EQUALS, docContComp.getPg_doc_cont());

	Broker broker = createBroker(sql);
	IManRevBulk manRev = null;
	if (broker.next())
		manRev = (IManRevBulk)revHome.fetch(broker);
	broker.close();

	return manRev;
}

	public Collection<V_doc_cont_compBulk> findByCompenso(CompensoBulk compenso ) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_compenso",SQLBuilder.EQUALS, compenso.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds_compenso",SQLBuilder.EQUALS, compenso.getCd_cds() );
		sql.addClause(FindClause.AND,"cd_uo_compenso",SQLBuilder.EQUALS, compenso.getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND,"pg_compenso",SQLBuilder.EQUALS, compenso.getPg_compenso() );
		return this.fetchAll( sql);
	}

	public Collection<V_doc_cont_compBulk> findByDocumento(Integer esercizioDoc, String cdCdsDoc, Long pgDoc, String tipoDoc ) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_doc_cont",SQLBuilder.EQUALS, esercizioDoc );
		sql.addClause(FindClause.AND,"cd_cds_doc_cont",SQLBuilder.EQUALS, cdCdsDoc );
		sql.addClause(FindClause.AND,"pg_doc_cont",SQLBuilder.EQUALS, pgDoc );
		sql.addClause(FindClause.AND,"tipo_doc_cont",SQLBuilder.EQUALS, tipoDoc );
		return this.fetchAll( sql);
	}
}
