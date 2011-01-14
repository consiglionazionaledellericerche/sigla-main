package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Classificazione_vociComponentSession extends it.cnr.jada.ejb.CRUDComponentSession 
{
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk caricaClassVociAssociate(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1, it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk caricaPdgPianoRipartoSpese(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1, it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2, java.lang.Integer param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
