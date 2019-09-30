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

package it.cnr.contab.docamm00.ejb;

import java.io.InputStream;
import java.util.Date;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.activation.DataHandler;
import javax.ejb.Local;
@Local
public interface TrasmissioneFatturePA {
	public void notificaFatturaAttivaRicevutaConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaMancataConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaScarto(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaEsito(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaConsegnaPec(UserContext userContext, String nomeFile, Date dataConsegna) throws ComponentException;
	public Boolean notificaFatturaAttivaDecorrenzaTermini(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public InputStream mancataConsegnaPecInvioFatturaAttiva(UserContext userContext, String nomeFile) throws ComponentException;
}
