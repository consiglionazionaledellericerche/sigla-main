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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RisultatoBulk extends RisultatoBase {
	private WorkpackageBulk linea_attivita;
	private Tipo_risultatoBulk tipo_risultato;
public RisultatoBulk() {
	super();
}
public RisultatoBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Long pg_risultato) {
	super(cd_centro_responsabilita,cd_linea_attivita,pg_risultato);
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = linea_attivita.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
}
public java.lang.String getCd_tipo_risultato() {
	it.cnr.contab.config00.latt.bulk.Tipo_risultatoBulk tipo_risultato = this.getTipo_risultato();
	if (tipo_risultato == null)
		return null;
	return tipo_risultato.getCd_tipo_risultato();
}
/**
 * 
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public WorkpackageBulk getLinea_attivita() {
	return linea_attivita;
}
/**
 * 
 * @return it.cnr.contab.config00.latt.bulk.Tipo_risultatoBulk
 */
public Tipo_risultatoBulk getTipo_risultato() {
	return tipo_risultato;
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getLinea_attivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
public void setCd_tipo_risultato(java.lang.String cd_tipo_risultato) {
	this.getTipo_risultato().setCd_tipo_risultato(cd_tipo_risultato);
}
/**
 * 
 * @param newLinea_attivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLinea_attivita(WorkpackageBulk newLinea_attivita) {
	linea_attivita = newLinea_attivita;
}
/**
 * 
 * @param newTipo_risultato it.cnr.contab.config00.latt.bulk.Tipo_risultatoBulk
 */
public void setTipo_risultato(Tipo_risultatoBulk newTipo_risultato) {
	tipo_risultato = newTipo_risultato;
}
public void validate(OggettoBulk parent) throws ValidationException {
	if (tipo_risultato == null)
		throw new ValidationException("E' necessario assegnare un tipo risultato.");
	if (getDs_risultato() == null)
		throw new ValidationException("E' necessario assegnare una descrizione del risultato.");
	if (getQuantita() == null)
		throw new ValidationException("E' necessario assegnare una quantità del risultato.");
}
}
