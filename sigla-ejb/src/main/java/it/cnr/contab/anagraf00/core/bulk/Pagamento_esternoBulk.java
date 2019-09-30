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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.math.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * Gestione dei dati relativi alla tabella Pagamento_esterno
 */

public class Pagamento_esternoBulk extends Pagamento_esternoBase {

	private AnagraficoBulk anagrafico;
	private Tipo_rapportoBulk tipo_rapporto;
public Pagamento_esternoBulk() {	
	super();
}
public Pagamento_esternoBulk(java.lang.Integer cd_anag,java.lang.Integer pg_pagamento) {
	super(cd_anag,pg_pagamento);
	setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk(cd_anag));
}

	/**
	 * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 *
	 * @see setAnagrafico
	 */

	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
public java.lang.Integer getCd_anag() {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
	if (anagrafico == null)
		return null;
	return anagrafico.getCd_anag();
}
	/**
	 * Imposta l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @param newAnagrafico Anagrafica di riferimento.
	 *
	 * @see getAnagrafico
	 */

	public void setAnagrafico(AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}
public void setCd_anag(java.lang.Integer cd_anag) {
	this.getAnagrafico().setCd_anag(cd_anag);
}
	/**
	 * Returns the im_totale.
	 * @return BigDecimal
	 */
	public BigDecimal getIm_totale() {
		if (getIm_spese() != null)
		  return getIm_pagamento().add(getIm_spese());
		else
		  return getIm_pagamento();  
	}

	/**
	 * @return
	 */
	public Tipo_rapportoBulk getTipo_rapporto() {
		return tipo_rapporto;
	}

	/**
	 * @param bulk
	 */
	public void setTipo_rapporto(Tipo_rapportoBulk bulk) {
		tipo_rapporto = bulk;
	}
	public String getCd_tipo_rapporto() {
		if(getTipo_rapporto() == null)
		  return null;
		return getTipo_rapporto().getCd_tipo_rapporto();
	}

	public void setCd_tipo_rapporto(String string) {
		getTipo_rapporto().setCd_tipo_rapporto(string);
	}
	public void validateDate() throws ValidationException {

		if (getDt_pagamento() == null)
			throw new ValidationException("Inserire la data di pagamento!");

		java.util.Calendar limInf = null;
		java.util.Calendar limSup = null;
		java.util.Calendar today = getDateCalendar(null);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

		java.util.Calendar dataPagamento = getDateCalendar(getDt_pagamento());
        if(dataPagamento.after(getDateCalendar(null)))
		  throw new ValidationException("La data di pagamento non può essere superiore alla data odierna!");
	}
	public static Calendar getDateCalendar(java.sql.Timestamp date) {

		if (date == null)
			try {
				date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}
		
		java.util.Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date(date.getTime()));
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		return calendar;
	}			
}
