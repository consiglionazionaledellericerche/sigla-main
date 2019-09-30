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

package it.cnr.contab.anagraf00.tabrif.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
@JsonInclude(value=Include.NON_NULL)
public class Rif_inquadramentoBulk extends Rif_inquadramentoBase {

	
	public final static java.util.Dictionary ti_dipendente_altroKeys;
	public final static String DIPENDENTE = "D";
	public final static String ALTRO = "A";
	static {
		ti_dipendente_altroKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_dipendente_altroKeys.put(DIPENDENTE,"Dipendenti");
		ti_dipendente_altroKeys.put(ALTRO,"Altri soggetti");
	}
	
	
	public final static java.util.Dictionary cd_gruppo_inquadramentoKeys;
	public final static String GRUPPO_1 = "1";
	public final static String GRUPPO_2 = "2";
	public final static String GRUPPO_3 = "3";
	public final static String GRUPPO_4 = "4";
	public final static String GRUPPO_5 = "5";
	public final static String GRUPPO_6 = "6";
	public final static String GRUPPO_7 = "7";
	public final static String GRUPPO_8 = "8";
	static {
		cd_gruppo_inquadramentoKeys = new it.cnr.jada.util.OrderedHashtable();
		cd_gruppo_inquadramentoKeys.put(GRUPPO_1,"GRUPPO 1 gruppo estero C");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_2,"GRUPPO 2 gruppo estero B");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_3,"GRUPPO 3 gruppo estero C");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_4,"GRUPPO 4 gruppo estero D");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_5,"GRUPPO 5 gruppo estero E");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_6,"GRUPPO 6 gruppo estero E");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_7,"GRUPPO 7 gruppo estero B");
		cd_gruppo_inquadramentoKeys.put(GRUPPO_8,"GRUPPO 8 gruppo estero B");
	
	}
	
public Rif_inquadramentoBulk() {
	super();
}
public Rif_inquadramentoBulk(Long pg_rif_inquadramento) {
	super(pg_rif_inquadramento);
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 15.57.57)
 * @return java.lang.String
 */
public String getDescrRifInquadramento() {

	String str = "";

	if (getCd_profilo()!=null)
		str = str + getCd_profilo();
	if (getCd_progressione()!=null)
		str = str + getCd_progressione();
	if (getCd_livello()!=null)
		str = str + getCd_livello();
	if (getDs_inquadramento()!=null)
		str = str + " - " + getDs_inquadramento();

	return str;
	
}

public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);
	this.setTi_dipendente_altro(DIPENDENTE);
	this.setCd_gruppo_inquadramento(null);
	return this;
}

public java.util.Dictionary getTi_dipendente_altroKeys() {
	return ti_dipendente_altroKeys;
}


public java.util.Dictionary getCd_gruppo_inquadramentoKeys() {
	return cd_gruppo_inquadramentoKeys;
}

}
