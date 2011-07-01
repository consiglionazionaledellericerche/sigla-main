package it.cnr.contab.doccont00.ejb;

import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;

@Remote
public interface MandatoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.doccont00.core.bulk.MandatoBulk aggiungiDocPassivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk aggiungiImpegni(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoBulk annullaMandato(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoBulk annullaMandato(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1,it.cnr.contab.doccont00.core.bulk.CompensoOptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoBulk annullaMandato(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1,it.cnr.contab.doccont00.core.bulk.CompensoOptionRequestParameter param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaImpegni(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.core.bulk.MandatoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.core.bulk.MandatoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findBancaOptions(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findDisponibilitaDiCassaPerCapitolo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoIBulk listaDocAttiviPerRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoIBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoBulk listaDocPassivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk listaImpegniCNR(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk listaSituazioneCassaCds(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void esistonoPiuModalitaPagamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoIBulk esitaVariazioneBilancioDiRegolarizzazione (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoIBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Boolean isCollegamentoSiopeCompleto (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoIBulk listaScadenzeAccertamentoPerRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoIBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void avvisoDiPagamentoMandatiRiscontrati() throws ComponentException, java.rmi.RemoteException;
void avvisoDiPagamentoMandatoRiscontrato(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws ComponentException, java.rmi.RemoteException;
}
