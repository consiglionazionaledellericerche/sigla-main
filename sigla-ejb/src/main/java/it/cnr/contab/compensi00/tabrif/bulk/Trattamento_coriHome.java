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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Trattamento_coriHome extends BulkHome {
public Trattamento_coriHome(java.sql.Connection conn) {
	super(Trattamento_coriBulk.class,conn);
}
public Trattamento_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Trattamento_coriBulk.class,conn,persistentCache);
}
public void completaTrattamentoCori(Trattamento_coriBulk obj) throws PersistencyException {

	Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHomeCache().getHome(Tipo_trattamentoBulk.class);
	Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
	filtro.setCdTipoTrattamento(obj.getCd_trattamento());
	filtro.setDataValidita(getServerDate());
	obj.setTipoTrattamento(trattHome.findTipoTrattamentoValido(filtro));

	Tipo_contributo_ritenutaHome coriHome = (Tipo_contributo_ritenutaHome )getHomeCache().getHome(Tipo_contributo_ritenutaBulk.class);
	obj.setTipoContributoRitenuta(coriHome.findTipoCORIValido(obj.getCd_contributo_ritenuta(), getServerDate()));

}
public java.util.List findAllRowsForTrattamento(Trattamento_coriBulk trattCORI) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_TRATTAMENTO",SQLBuilder.EQUALS,trattCORI.getCd_trattamento());
	sql.addOrderBy("id_riga");

	java.util.List list = fetchAll(sql);
	for (java.util.Iterator i = list.iterator();i.hasNext();)
		completaTrattamentoCori((Trattamento_coriBulk)i.next());
	
	return list;
}
public java.util.List findAllRowsForTrattamento(Tipo_trattamentoBulk tipoTrattamento) throws PersistencyException{
	return findAllRowsForTrattamento(tipoTrattamento.getCd_trattamento());
}
private java.util.List findAllRowsForTrattamento(String cd_trattamento) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_TRATTAMENTO",SQLBuilder.EQUALS,cd_trattamento);
	sql.addOrderBy("id_riga");

	java.util.List list = fetchAll(sql);
	for (java.util.Iterator i = list.iterator();i.hasNext();)
		completaTrattamentoCori((Trattamento_coriBulk)i.next());
	
	return list;
}
public int getNextPgRiga(Trattamento_coriBulk trattCori) throws PersistencyException{

	Trattamento_coriBulk cori = new Trattamento_coriBulk(null, trattCori.getCd_trattamento(), null);
	String maxPg = (String)findMax(cori, "id_riga", null);
	int currentPg = 0;

	if (maxPg!=null)
		currentPg = (Long.valueOf(maxPg)).intValue();

	return currentPg + 1;
}
public void validaTrattamento(Trattamento_coriBulk trattCori) throws it.cnr.jada.comp.ApplicationException, PersistencyException{

	trattCori.validaTrattamento();
	if (trattCori.getDt_fine_validita().compareTo(getServerDate())<0)
		throw new it.cnr.jada.comp.ApplicationException("La Data Fine Validita non può essere inferiore alla Data Odierna");
}
}
