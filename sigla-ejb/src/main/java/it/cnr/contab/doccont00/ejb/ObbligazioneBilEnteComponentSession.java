package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface ObbligazioneBilEnteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession {
void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ImpegnoBulk annullaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ImpegnoBulk creaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ImpegnoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk findElementoVoceFor(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1) throws  it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
