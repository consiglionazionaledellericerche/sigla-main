package it.cnr.contab.doccont00.ejb;

import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.ParseException;

import javax.ejb.Remote;

@Remote
public interface ReversaleComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.doccont00.core.bulk.ReversaleBulk aggiungiDocAttivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk annullaReversale(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk annullaReversale(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaReversaleDiTrasferimento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiTrasferimento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk listaDocAttivi(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isChiudibileReversaleProvvisoria(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException;
boolean isRevProvvLiquidCoriCentroAperta(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException;
java.lang.Boolean isCollegamentoSiopeCompleto (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ReversaleBulk setCodiciSIOPECollegabili (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
byte[] lanciaStampa( it.cnr.jada.UserContext userContext, String cds, Integer esercizio , Long pgReversale) throws PersistencyException, ComponentException, RemoteException, ParseException;
}
