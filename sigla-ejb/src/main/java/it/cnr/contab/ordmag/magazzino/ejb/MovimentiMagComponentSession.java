package it.cnr.contab.ordmag.magazzino.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface MovimentiMagComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	List<MovimentiMagBulk> caricoDaOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine,
			OrdineAcqConsegnaBulk consegna, OrdineAcqBulk ordine, EvasioneOrdineRigaBulk evasioneOrdineRiga, 
			List<MovimentiMagBulk> listaMovimentiScarico)throws ComponentException, PersistencyException, RemoteException, ApplicationException;
	}
