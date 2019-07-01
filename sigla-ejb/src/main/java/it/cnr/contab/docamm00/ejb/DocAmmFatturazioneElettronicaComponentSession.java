package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;

import java.rmi.RemoteException;

import javax.ejb.Remote;
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
}
