package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class FatturaElettronicaAttivaComponent extends it.cnr.jada.comp.CRUDComponent 
	implements Cloneable,Serializable {
	private static final long serialVersionUID = 1L;

	public  FatturaElettronicaAttivaComponent(){
    }
	
	public Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, Calendar dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaRicevutaConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, dataConsegnaSdi);
	}
	public Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaRifiutataDestinatarioSDI(userContext, fattura, noteSdi);
	}

	public Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaScartoSDI(userContext, fattura, codiceInvioSdi, noteSdi);
	}
	
	public Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaMancataConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, noteInvioSdi);
	}
	
	public Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaDecorrenzaTerminiSDI(userContext, fattura, noteSdi);
	}
	
	public Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaTrasmissioneNonRecapitataSDI(userContext, fattura, codiceSdi, noteInvioSdi);
	}
	
	public Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaEsitoAccettatoSDI(userContext, fattura);
	}

	public Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fattura, Date dataConsegna) throws PersistencyException, ComponentException,java.rmi.RemoteException{
		return recuperoComponentFatturaAttiva().aggiornaFatturaConsegnaSDI(userContext, fattura, dataConsegna);
	}
	
	private FatturaAttivaSingolaComponentSession recuperoComponentFatturaAttiva() {
		FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession");
		return componentFatturaAttiva;
	}
}