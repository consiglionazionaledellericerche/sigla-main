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

package it.cnr.contab.config00.pdcfin.bulk;

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_funz_tipocdsBulk extends Ass_ev_funz_tipocdsBase {
	it.cnr.jada.util.OrderedHashtable funzioni;
	it.cnr.jada.util.OrderedHashtable tipiCds;	
	
	
public Ass_ev_funz_tipocdsBulk() {}
public Ass_ev_funz_tipocdsBulk(java.lang.String cd_conto,java.lang.String cd_funzione,java.lang.String cd_tipo_unita,java.lang.Integer esercizio) {
	super(cd_conto,cd_funzione,cd_tipo_unita,esercizio);
	this.setEsercizio(esercizio);
	this.setCd_tipo_unita(cd_tipo_unita);
	this.setCd_conto(cd_conto);
	this.setCd_funzione(cd_funzione);
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable getFunzioni() {
	return funzioni;
}
/**
 * Creation date: (16/05/2001 17:11:32)
 * @return String
 */
public String getMapKey() 
{
	if ( getCd_funzione() != null && getCd_tipo_unita() != null )
		return getCd_funzione() + "-" + getCd_tipo_unita() ;
	return null;
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable getTipiCds() {
	return tipiCds;
}
/**
 * @param newFunzioni it.cnr.jada.util.OrderedHashtable
 */
public void setFunzioni(it.cnr.jada.util.OrderedHashtable newFunzioni) {
	funzioni = newFunzioni;
}
/**
 * @param newTipiCds it.cnr.jada.util.OrderedHashtable
 */
public void setTipiCds(it.cnr.jada.util.OrderedHashtable newTipiCds) {
	tipiCds = newTipiCds;
}
}
