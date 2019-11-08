package it.cnr.contab.ordmag.magazzino.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.magazzino.bulk.AbilitazioneMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
@Remote
public interface MovimentiMagComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	MovimentiMagBulk caricoDaOrdine(UserContext userContext, OrdineAcqConsegnaBulk consegna, EvasioneOrdineRigaBulk evasioneOrdineRiga) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    List<BollaScaricoMagBulk> generaBolleScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    ScaricoMagazzinoBulk scaricaMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    CaricoMagazzinoBulk caricaMagazzino(UserContext userContext, CaricoMagazzinoBulk caricoMagazzino) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    void annullaMovimento(UserContext userContext, MovimentiMagBulk movimentiMagBulk) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    MovimentiMagazzinoBulk initializeMovimentiMagazzino(UserContext usercontext, MovimentiMagazzinoBulk movimentiMagazzinoBulk) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
	AbilitazioneMagazzinoBulk initializeAbilitazioneMovimentiMagazzino(UserContext usercontext, AbilitazioneMagazzinoBulk abilitazioneMagazzinoBulk) throws PersistencyException, ComponentException , RemoteException, ApplicationException;
	RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle)throws ComponentException,java.rmi.RemoteException;
    public java.util.Collection<LottoMagBulk> findLottiMagazzino(UserContext userContext, MovimentiMagazzinoRigaBulk movimentiMagazzinoRigaBulk) throws ComponentException, PersistencyException, RemoteException, ApplicationException;
    public it.cnr.jada.util.RemoteIterator ricercaMovimenti(UserContext userContext, ParametriSelezioneMovimentiBulk parametri) throws ComponentException, RemoteException;
}
