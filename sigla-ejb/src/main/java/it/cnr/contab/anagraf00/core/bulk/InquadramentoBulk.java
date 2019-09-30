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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Inquadramento
 */

public class InquadramentoBulk extends InquadramentoBase {

	private RapportoBulk rapporto;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rif_inquadramento;
	private java.sql.Timestamp dt_fin_validita_originale;
	private java.sql.Timestamp max_dt_fin_validita_missione;
public InquadramentoBulk() {
	super();
}
public InquadramentoBulk(java.lang.Integer cd_anag,java.lang.String cd_tipo_rapporto,java.sql.Timestamp dt_ini_validita,java.sql.Timestamp dt_ini_validita_rapporto,java.lang.Long pg_rif_inquadramento) {
	super(cd_anag,cd_tipo_rapporto,dt_ini_validita,dt_ini_validita_rapporto,pg_rif_inquadramento);
	setRif_inquadramento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk(pg_rif_inquadramento));
	setRapporto(new it.cnr.contab.anagraf00.core.bulk.RapportoBulk(cd_anag,cd_tipo_rapporto,dt_ini_validita_rapporto));
}
public void fetchedFrom(Broker broker) throws it.cnr.jada.persistency.IntrospectionException,it.cnr.jada.persistency.FetchException {
	super.fetchedFrom(broker);
	setDt_fin_validita_originale(getDt_fin_validita());
}
public java.lang.Integer getCd_anag() {
	it.cnr.contab.anagraf00.core.bulk.RapportoBulk rapporto = this.getRapporto();
	if (rapporto == null)
		return null;
	return rapporto.getCd_anag();
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.core.bulk.RapportoBulk rapporto = this.getRapporto();
	if (rapporto == null)
		return null;
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = rapporto.getTipo_rapporto();
	if (tipo_rapporto == null)
		return null;
	return tipo_rapporto.getCd_tipo_rapporto();
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 10:28:15)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_fin_validita_originale() {
	return dt_fin_validita_originale;
}
public java.sql.Timestamp getDt_ini_validita_rapporto() {
	it.cnr.contab.anagraf00.core.bulk.RapportoBulk rapporto = this.getRapporto();
	if (rapporto == null)
		return null;
	return rapporto.getDt_ini_validita();
}
public java.lang.Long getPg_rif_inquadramento() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rif_inquadramento = this.getRif_inquadramento();
	if (rif_inquadramento == null)
		return null;
	return rif_inquadramento.getPg_rif_inquadramento();
}
	/**
	 * Restituisce il <code>RapportoBulk</code> contenente il rapporto associato.
	 *
	 * @return RapportoBulk
	 *
	 * @see setRapporto
	 */

	public RapportoBulk getRapporto() {
		return rapporto;
	}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2001 11:01:16)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk getRif_inquadramento() {
	return rif_inquadramento;
}
public boolean isRODt_fine_val() {

	if (getRapporto() != null && getRapporto().getAnagrafico() != null){
		return getRapporto().getAnagrafico().isDipendente();
	}
	
	return false;
}
public boolean isRORif_inquadramento() {
	return rif_inquadramento == null || rif_inquadramento.getCrudStatus() != rif_inquadramento.UNDEFINED;
}
public void setCd_anag(java.lang.Integer cd_anag) {
	this.getRapporto().setCd_anag(cd_anag);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getRapporto().getTipo_rapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 10:28:15)
 * @param newDt_fin_validita_originale java.sql.Timestamp
 */
public void setDt_fin_validita_originale(java.sql.Timestamp newDt_fin_validita_originale) {
	dt_fin_validita_originale = newDt_fin_validita_originale;
}
public void setDt_ini_validita_rapporto(java.sql.Timestamp dt_ini_validita_rapporto) {
	this.getRapporto().setDt_ini_validita(dt_ini_validita_rapporto);
}
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.getRif_inquadramento().setPg_rif_inquadramento(pg_rif_inquadramento);
}
	/**
	 * Imposta il <code>RapportoBulk</code> contenente il rapporto associato.
	 *
	 * @param RapportoBulk Il rapporto da impostare.
	 *
	 * @see getRapporto
	 */

	public void setRapporto(RapportoBulk newRapporto) {
		rapporto = newRapporto;
	}
/**
 * Insert the method's description here.
 * Creation date: (01/10/2001 11:01:16)
 * @param newRif_inquadramento it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public void setRif_inquadramento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk newRif_inquadramento) {
	rif_inquadramento = newRif_inquadramento;
}
public java.sql.Timestamp getMax_dt_fin_validita_missione() {
	return max_dt_fin_validita_missione;
}
public void setMax_dt_fin_validita_missione(
		java.sql.Timestamp max_dt_fin_validita_missione) {
	this.max_dt_fin_validita_missione = max_dt_fin_validita_missione;
}
}
