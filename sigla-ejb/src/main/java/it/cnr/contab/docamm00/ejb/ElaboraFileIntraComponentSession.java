package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2.DatiFatturaType;

import java.util.List;

import javax.ejb.Remote;
import javax.xml.bind.JAXBElement;

@Remote
public interface ElaboraFileIntraComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {

	List EstraiLista(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneUnoAcquisti(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneDueAcquisti(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneTreAcquisti(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneQuattroAcquisti(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneUnoVendite (UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneDueVendite(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneTreVendite(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List SezioneQuattroVendite(UserContext uc, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List EstraiListaIntra12(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	void confermaElaborazione(UserContext userContext, VIntrastatBulk bulk)throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	java.util.Date recuperoMaxDtPagamentoLiq(UserContext uc, OggettoBulk bulk) throws ComponentException ,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	List EstraiBlacklist(UserContext userContext, OggettoBulk model, OggettoBulk terzo)throws ComponentException ,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	JAXBElement<DatiFatturaType> creaDatiFatturaType(UserContext context,VSpesometroNewBulk dett)throws ComponentException ,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	
	
}
