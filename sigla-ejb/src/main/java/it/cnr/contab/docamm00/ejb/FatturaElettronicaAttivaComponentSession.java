package it.cnr.contab.docamm00.ejb;
import java.util.Date;

import javax.ejb.Remote;
import javax.xml.datatype.XMLGregorianCalendar;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface FatturaElettronicaAttivaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, XMLGregorianCalendar dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fattura, Date dataConsegna) throws PersistencyException, ComponentException,java.rmi.RemoteException;
	void gestioneInvioMailNonRecapitabilita(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ApplicationException, ComponentException,java.rmi.RemoteException;
}