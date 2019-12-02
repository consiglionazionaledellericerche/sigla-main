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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class MissioneQuotaRimborsoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_area_estera;
	private java.lang.String cd_gruppo_inquadramento;
	private java.sql.Timestamp dt_inizio_validita;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MISSIONE_QUOTA_RIMBORSO
	 **/
	public MissioneQuotaRimborsoKey() {
		super();
	}
	public MissioneQuotaRimborsoKey(java.lang.String cd_area_estera, java.lang.String cd_gruppo_inquadramento, java.sql.Timestamp dt_inizio_validita) {
		super();
		this.cd_area_estera=cd_area_estera;
		this.cd_gruppo_inquadramento=cd_gruppo_inquadramento;
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof MissioneQuotaRimborsoKey)) return false;
		MissioneQuotaRimborsoKey k = (MissioneQuotaRimborsoKey) o;
		if (!compareKey(getCd_area_estera(), k.getCd_area_estera())) return false;
		if (!compareKey(getCd_gruppo_inquadramento(), k.getCd_gruppo_inquadramento())) return false;
		if (!compareKey(getDt_inizio_validita(), k.getDt_inizio_validita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_area_estera());
		i = i + calculateKeyHashCode(getCd_gruppo_inquadramento());
		i = i + calculateKeyHashCode(getDt_inizio_validita());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_area_estera]
	 **/
	public void setCd_area_estera(java.lang.String cd_area_estera)  {
		this.cd_area_estera=cd_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_area_estera]
	 **/
	public java.lang.String getCd_area_estera() {
		return cd_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_gruppo_inquadramento]
	 **/
	public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento)  {
		this.cd_gruppo_inquadramento=cd_gruppo_inquadramento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_gruppo_inquadramento]
	 **/
	public java.lang.String getCd_gruppo_inquadramento() {
		return cd_gruppo_inquadramento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt_inizio_validita]
	 **/
	public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita)  {
		this.dt_inizio_validita=dt_inizio_validita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt_inizio_validita]
	 **/
	public java.sql.Timestamp getDt_inizio_validita() {
		return dt_inizio_validita;
	}
}