package it.cnr.contab.gestiva00.ejb;

import javax.ejb.Remote;

@Remote
public interface StampaRegistriIvaComponentSession extends it.cnr.jada.ejb.RicercaComponentSession {
it.cnr.jada.bulk.MTUWrapper callStampeIva(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.BulkList findRegistriStampati(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk riepilogoLiquidazioneIVA(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection selectProspetti_stampatiByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Collection selectTipi_sezionaliByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk tabCodIvaAcquisti(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk tabCodIvaVendite(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
