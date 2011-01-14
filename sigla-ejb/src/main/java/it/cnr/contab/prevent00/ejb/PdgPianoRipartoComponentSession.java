package it.cnr.contab.prevent00.ejb;

import javax.ejb.Remote;

@Remote
public interface PdgPianoRipartoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk aggiornaTotaleGeneraleImpdaRipartire(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isPdgPianoRipartoDefinitivo(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk rendiPdgPianoRipartoDefinitivo(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk rendiPdgPianoRipartoProvvisorio(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk caricaStruttura(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
