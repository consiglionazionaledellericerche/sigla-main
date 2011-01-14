package it.cnr.contab.chiusura00.comp;

import java.util.*;
import it.cnr.contab.chiusura00.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (30/05/2003 14.16.17)
 * @author: Simonetta Costa
 */
public interface IRicercaDocContComponent extends it.cnr.jada.comp.IRicercaMgr
{
public void	callAnnullamentoDocCont(UserContext userContext, Long pg_call) throws ComponentException ;
public void	callRiportoNextEsDocCont(UserContext userContext, Long pg_call) throws ComponentException ;
public void	callRiportoPrevEsDocCont(UserContext userContext, Long pg_call) throws ComponentException ;
public it.cnr.jada.util.RemoteIterator cercaPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException; 
public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException; 
public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException;
public it.cnr.jada.util.RemoteIterator cercaPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk docCont ) throws it.cnr.jada.comp.ComponentException; 
public void clearSelectionPerAnnullamento(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException;
public void clearSelectionPerRiportaAvanti(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException ;
public void clearSelectionPerRiportaIndietro(it.cnr.jada.UserContext userContext, V_obb_acc_xxxBulk doc) throws it.cnr.jada.comp.ComponentException ;
public V_obb_acc_xxxBulk initializeSelectionPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException;
public V_obb_acc_xxxBulk initializeSelectionPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException ;
public V_obb_acc_xxxBulk initializeSelectionPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc) throws ComponentException ;
public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,OggettoBulk bulk) throws ComponentException;
public void	selectAllPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException;
public void	selectAllPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException;
public void	selectAllPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException;

public void	callRiportoNextEsDocContVoce(UserContext userContext, Long pg_call) throws ComponentException ;

public void	selectAllPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk doc ) throws ComponentException;

public void	setSelectionPerAnnullamento(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException;

public void	setSelectionPerRiportaAvanti(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException ;

public void	setSelectionPerRiportaAvantiEvoluto(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException ;

public void	setSelectionPerRiportaIndietro(UserContext userContext, V_obb_acc_xxxBulk doc, OggettoBulk[] docContabili,BitSet oldDocContabili, BitSet newDocContabili) throws ComponentException ;
}
