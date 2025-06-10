/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.inventario00.bp;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import it.cnr.contab.coepcoan00.bp.CRUDScritturaPDoppiaBP;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk;
import it.cnr.contab.inventario00.ejb.*;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDAggiornamentoInventarioBP extends it.cnr.jada.util.action.SimpleCRUDBP implements SelectionListener{
	
private final SimpleDetailCRUDController vUtilizzatori = new SimpleDetailCRUDController("VUtilizzatori",Utilizzatore_CdrVBulk.class,"v_utilizzatoriColl",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			validate_Percentuali_LA(context,model);
		}
};
	
private final SimpleDetailCRUDController utilizzatori = new SimpleDetailCRUDController("Utilizzatori",Inventario_utilizzatori_laBulk.class,"buono_cs_utilizzatoriColl",vUtilizzatori);	
	
private it.cnr.jada.persistency.sql.CompoundFindClause clauses;
	
public CRUDAggiornamentoInventarioBP() {
	super();
}
/**
 * CRUDTrasferimentoInventarioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAggiornamentoInventarioBP(String function) {
	super(function);
	setTab("tab","tabAggiornamentoInventarioTestata");
}

/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	Aggiornamento_inventarioBulk agg = (Aggiornamento_inventarioBulk)getModel();
	super.init(config,context);
	dettagliCRUDController.setMultiSelection(false);
	dettagliCRUDController.setPaged(true);
	resetTabs();
}
/**
 *	Disabilito il bottone di ricerca libera.
 */
public boolean isNewButtonHidden() {
	
	return true;
}
public boolean isFreeSearchButtonHidden() {
	
	return true;
}
/**
 *	Disabilito il bottone di ricerca.
 */
public boolean isSearchButtonHidden() {
	return true;
}
public boolean isDeleteButtonHidden() {
	return true;
}

/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

public void resetTabs() {
	setTab("tab","tabAggiornamentoInventarioTestata");
}

public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {

	return super.initializeModelForInsert(context, bulk);
}
/**
 * Restituisce il valore della proprietà 'utilizzatori'
 *
 * @return utilizzatori <code>SimpleDetailCRUDController</code> il valore
 */
public final SimpleDetailCRUDController getUtilizzatori() {
	return utilizzatori;
}
/**
 * Restituisce il valore della proprietà 'vutilizzatori'
 *
 * @return Il valore della proprietà 'vutilizzatori'
 */
public final SimpleDetailCRUDController getVutilizzatori() {
	return vUtilizzatori;
}


/**
 * Insert the method's description here.
 * Creation date: (12/09/2002 11.06.12)
 * @param newClauses it.cnr.jada.persistency.sql.CompoundFindClause
 */
public void setClauses(it.cnr.jada.persistency.sql.CompoundFindClause newClauses) {
	clauses = newClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (12/09/2002 11.06.12)
 * @return it.cnr.jada.persistency.sql.CompoundFindClause
 */
public it.cnr.jada.persistency.sql.CompoundFindClause getClauses() {
	return clauses;
}
/**
 * Restituisce il valore della proprietà 'dettagliCRUDController'
 *
 * @return <code>SimpleDetailCRUDController</code> il valore della proprietà 'dettagliCRUDController'
 */
public final SimpleDetailCRUDController getDettagliCRUDController() {
	 return dettagliCRUDController;
}	
 
 
private final SimpleDetailCRUDController dettagliCRUDController = new SimpleDetailCRUDController("dettaglioCRUDController",Inventario_beniBulk.class,"dettagli",this){
	 public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
	 }
};

public void aggiornamento_beni (ActionContext context) throws BusinessProcessException, DetailedRuntimeException, ComponentException, RemoteException, PersistencyException, OutdatedResourceException, BusyResourceException{
	it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiorno = (it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)getModel();		
	it.cnr.contab.inventario00.ejb.Aggiornamento_inventarioComponentSession h = (it.cnr.contab.inventario00.ejb.Aggiornamento_inventarioComponentSession)createComponentSession();
	setModel(context,h.aggiornaBeni(context.getUserContext(),aggiorno));
	setMessage("Salvataggio eseguito in modo corretto.");
}
public void validate_Percentuali_LA(ActionContext context,OggettoBulk model) throws ValidationException {

	Utilizzatore_CdrVBulk cdrUtilizzatore = (Utilizzatore_CdrVBulk)model;

	SimpleBulkList utilizzatori_LA = cdrUtilizzatore.getBuono_cs_utilizzatoriColl();
	java.math.BigDecimal percentuale_utilizzo_LA = new java.math.BigDecimal("0");
	Vector cdr = new Vector();
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	
	if (utilizzatori_LA.size()>0){
		Iterator i = utilizzatori_LA.iterator();
		while (i.hasNext()){
			Inventario_utilizzatori_laBulk utilizzatore_LA = (Inventario_utilizzatori_laBulk)i.next();
			
			// Controlla che sia stata specificata la line di Attività
			if (utilizzatore_LA.getLinea_attivita()==null || utilizzatore_LA.getLinea_attivita().getCd_linea_attivita() == null){
				throw new ValidationException ("GAE non valido. Indicare sia il codice del GAE, sia la sua percentuale di utilizzo");
			}
			
			// Controlla che non vi siano Linee di Attività DUPLICATE
			if (BulkCollections.containsByPrimaryKey(cdr,utilizzatore_LA.getLinea_attivita())){
				throw new ValidationException ("GAE duplicato. Non è possibile indicare più volte uno stesso GAE");
			}
			else {
				cdr.add(utilizzatore_LA.getLinea_attivita());
			}
			
			// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per Linea di Attività
			if (utilizzatore_LA.getPercentuale_utilizzo_la()!=null){
				percentuale_utilizzo_LA = percentuale_utilizzo_LA.add(utilizzatore_LA.getPercentuale_utilizzo_la());
			}
			else{
				throw new ValidationException ("La percentuale di utilizzo per i GAE non può essere nulla");
			}
		}

		// Controlla che il totale delle percentuali di utilizzo delle Linee di Attività sia 100
		if (percentuale_utilizzo_LA.compareTo(cento)!=0)
			throw new ValidationException ("La percentuale di utilizzo per i GAE non è valida");
	}
}
 
 /**
  * Restituisce la Selezione fatta sul Controller dei Beni Associati.
  *
  * @param context la <code>ActionContext</code> che ha generato la richiesta
  * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
  * @param currentSelection la <code>BitSet</code> selezione attuale.
  *
  * @return currentSelection la <code>BitSet</code> selezione attuale.
 **/
 public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	
	 return currentSelection;
 }
  
 /**
  * initializeSelection method comment.
  *
  * @param context la <code>ActionContext</code> che ha generato la richiesta
 **/ 
 public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	
 }
 /**
  * E' stata generata la richiesta di annullare tutte le operazioni effettuate sul Buono di 
  *	Scarico Inventario.
  *	Viene invocato il metodo <code>Aggiornamento_inventarioComponent.annullaModificaScaricoBeni</code>
  *
  *	@param context la <code>ActionContext</code> che ha generato la richiesta.
  */
 public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	  
 }
 /**
  * Imposta la Selezione fatta sul Controller dei Beni che si stanno aggiornando.
  *
  * @param context la <code>ActionContext</code> che ha generato la richiesta
  * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
  * @param oldSelection la <code>BitSet</code> selezione precedente.
  * @param newSelection la <code>BitSet</code> selezione attuale.
  *
  * @return currentSelection la <code>BitSet</code> selezione attuale.
 **/
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
		for (int i = 0;i < bulks.length;i++) {
			Inventario_beniBulk bene = (Inventario_beniBulk)bulks[i];
			if (oldSelection.get(i) != newSelection.get(i)) {	 	
	 	      getDettagliCRUDController().add(context,bene);
			}
		}		
		return newSelection;
 }

 /**
   * Metodo richiesto dall'interfaccia <code>SelectionListener</code>.
   *
   * @param context la <code>ActionContext</code> che ha generato la richiesta  
 **/
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
 

 /**
  *	Seleziona tutti i beni da una finestra di Selezione.
  *	L'utente sta selezionando i beni da aggiornare, e decide di aggiornare tutti i beni risultati 
  *	dalla ricerca di beni disponibili.
  *	Invoca il metodo <code>Aggiornamento_inventarioComponentSession.aggiornaTuttiBeni</code>
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 **/
   public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			setModel(context,((Aggiornamento_inventarioComponentSession)createComponentSession()).aggiornaTuttiBeni(
				context.getUserContext(),
				(Aggiornamento_inventarioBulk)getModel(),
				getClauses()));
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
	  }
   }

 /**
   * E' stata generata la richiesta di cercare i beni disponibili per una operazione d Scarico.
   *	Vengono invocati due diversi metodi della Component <i>Aggiornamento_inventarioComponent</i>, a seconda
   *	che il Buono di Scarico sia creato da una Fattura Attiva oppure no:
   *		- invoca il metodo <code>Aggiornamento_inventarioComponentt.cercaBeniAssociabili</code>, se 
   *			proviene da Fattura Attiva;
   *		- invoca il metodo <code>Aggiornamento_inventarioComponent.cercaBeniScaricabili</code>, altrimenti.
   *
   * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
   * @param no_accessori <code>boolean</code> indica se escludere i beni accessori dalla ricerca
   * @param beni_da_escludere <code>SimpleBulkList</code> i beni da escludere dalla ricerca, magari perchè già selezionati
   * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
   *
   * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
  **/
  public RemoteIterator getListaBeniDaAggiornare(
	  it.cnr.jada.UserContext userContext, 
	  SimpleBulkList beni_da_escludere,
	  it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws BusinessProcessException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException {

		  return ((Aggiornamento_inventarioComponentSession)createComponentSession()).cercaBeniAggiornabili(userContext, (Aggiornamento_inventarioBulk)this.getModel(),  clauses);	 
  }

	protected static final String[] TAB_TESTATA = new String[]{ "tabAggiornamentoInventarioTestata","Beni","/inventario00/tab_aggiornamento_testata.jsp" };
	private static final String[] TAB_DETTAGLIO = new String[]{ "tabAggiornamentoInventarioDettaglio","Aggiornamenti","/inventario00/tab_aggiornamento_dettaglio.jsp" };

	public String[][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i = 0;
		pages.put(i++, TAB_TESTATA);
		pages.put(i++, TAB_DETTAGLIO);
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
		return tabs;
	}

	public String getColumnSetName() {
		return "default";
	}
}

