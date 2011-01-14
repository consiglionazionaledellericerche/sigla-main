package it.cnr.contab.incarichi00.ejb;

import javax.ejb.Remote;

@Remote
public interface RepertorioLimitiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk getRepertorioLimiti(it.cnr.jada.UserContext param0,int param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk aggiornaRepertorioLimiti(it.cnr.jada.UserContext param0,int param1,java.lang.String param2,java.lang.String param3,java.lang.String param4,java.math.BigDecimal param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
