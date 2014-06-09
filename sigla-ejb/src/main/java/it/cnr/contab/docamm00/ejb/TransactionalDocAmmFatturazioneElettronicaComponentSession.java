package it.cnr.contab.docamm00.ejb;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

import java.rmi.RemoteException;

import javax.xml.bind.JAXBElement;

public class TransactionalDocAmmFatturazioneElettronicaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements DocAmmFatturazioneElettronicaComponentSession {

	public String recuperoNomeFileXml(it.cnr.jada.UserContext param0,Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (String)invoke("recuperoNomeFileXml",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
	}
	
	
	@SuppressWarnings("unchecked")
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(it.cnr.jada.UserContext param0,Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (JAXBElement<FatturaElettronicaType>)invoke("creaFatturaElettronicaType",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
