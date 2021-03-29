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

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;

import java.rmi.RemoteException;

import javax.ejb.Remote;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

@Remote
public interface DocAmmFatturazioneElettronicaComponentSession extends
		it.cnr.jada.ejb.CRUDComponentSession {
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(
			UserContext userContext, Fattura_attivaBulk fattura)
			throws RemoteException, it.cnr.jada.comp.ComponentException;

	public String recuperoNomeFileXml(UserContext userContext,
			Fattura_attivaBulk fattura) throws RemoteException,
			ComponentException;

	public Configurazione_cnrBulk getAuthenticatorPecSdi(
			UserContext userContext) throws RemoteException,
			ComponentException;

	public String recuperoInizioNomeFile(UserContext userContext)
			throws RemoteException, ComponentException;
	public FatturaElettronicaType preparaFattura(UserContext userContext, Fattura_attivaBulk fattura)throws RemoteException, ComponentException;
	public void aggiornaMetadati(UserContext userContext, Integer esercizio, String cdCds, Long pgFatturaAttiva)throws RemoteException, ComponentException;
}
