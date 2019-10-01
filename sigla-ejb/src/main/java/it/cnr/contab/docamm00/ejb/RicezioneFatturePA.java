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

import it.cnr.jada.comp.ComponentException;

import java.math.BigInteger;
import java.util.Date;

import javax.activation.DataHandler;
import javax.ejb.Local;
import javax.mail.Message;
@Local
public interface RicezioneFatturePA {
	public void riceviFatturaSIGLA(BigInteger identificativoSdI, String nomeFile, String replyTo, DataHandler file, String nomeFileMetadati,DataHandler metadati) throws ComponentException;
	public void notificaDecorrenzaTermini(BigInteger identificativoSdI, String nomeFile, DataHandler file);
	public void notificaDecorrenzaTermini(String nomeFile, DataHandler data) throws ComponentException;
	public void notificaScartoEsito(String nomeFile, DataHandler data, Date dataRicevimento) throws ComponentException;
	public void notificaFatturaPassivaConsegnaEsitoPec(String idSdI, Date dataRicevimento) throws ComponentException;
	public void notificaScartoMailNotificaNonRicevibile(Message message, String idSdi, Date dataRicevimentoMail) throws ComponentException;
}
