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

/*
 * Created on Jan 19, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario01.bp;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Vector;

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.AbstractDetailCRUDController;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDCaricoInventarioBP extends CRUDCaricoScaricoInventarioBP{
	private Long progressivo_beni = new Long("0");
	/* Flag che viene impostato a true nel caso che l'utente immetta un valore non valido 
	 * nel campo Num. Gruppi. Se settato a true, nella Jsp, il campo verrÓ visualizzato in 
	 * arancio, seguendo lo standard.
	*/ 
	private boolean isNumGruppiErrato = false; 
	private boolean isQuantitaEnabled = true;
	private boolean by_ordini = false;


	public CRUDCaricoInventarioBP() {
	super();
	setTab("tab","tabCaricoInventarioTestata");
	
}

	public CRUDCaricoInventarioBP(String function) {
		super(function);
	}
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	
		super.init(config,context);
		resetTabs();
		utilizzatori.setReadonly(false);
	}
	@Override
	public boolean isPrintButtonHidden() {
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) getModel();
		return 	((by_ordini && isTemporaneo())|| super.isPrintButtonHidden());
	}

	public boolean isTemporaneo() {
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) getModel();
		return 	buonoCS.isTemporaneo();
	}


	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
				Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
				testata.setTi_documento(Buono_carico_scaricoBulk.CARICO);
				resetTabs();
					if (by_fattura||by_documento){
						bulk = super.initializeModelForEdit(context, testata);
						return testata;
						
					}else{
						bulk = super.initializeModelForEdit(context, bulk);
						return bulk;
					}
				
	}
	public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
				Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
				testata.setTi_documento(Buono_carico_scaricoBulk.CARICO);
				bulk = super.initializeModelForInsert(context, testata);
				return bulk;
	}
	
	public OggettoBulk initializeModelForFreeSearch(
		ActionContext actioncontext,
		OggettoBulk oggettobulk)
		throws BusinessProcessException {
			Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)oggettobulk;
			testata.setTi_documento(Buono_carico_scaricoBulk.CARICO);
			oggettobulk = super.initializeModelForFreeSearch(actioncontext, testata);
			return oggettobulk;
	}
	
	public OggettoBulk initializeModelForSearch(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
				Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
				testata.setTi_documento(Buono_carico_scaricoBulk.CARICO);
				bulk = super.initializeModelForSearch(context, testata);
				return bulk;
	}
	
	public void resetTabs() {
		setTab("tab","tabCaricoInventarioTestata");
	}
	public boolean isTabUtilizzatoriEnabled() {
			
		return (isInserting() && !isBy_fattura() && !isBy_documento() && !isBy_ordini());
	}
	public boolean isBy_ordini() {
		return by_ordini;
	}

	public void setBy_ordini(boolean b) {
		by_ordini = b;
		setFirst(true);
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) getModel();
		buonoCS.setByOrdini(new Boolean(b));

	}


	public void resetForSearch(ActionContext context) throws BusinessProcessException {
		super.resetForSearch(context);
		resetTabs();
	}
	protected final AbstractDetailCRUDController dettaglio = createDettaglio();
	
	private final SimpleDetailCRUDController vUtilizzatori = new SimpleDetailCRUDController("VUtilizzatori",Utilizzatore_CdrVBulk.class,"v_utilizzatoriColl",dettaglio){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			validate_Percentuali_LA(context,model);
		}
		
	};

	private final SimpleDetailCRUDController utilizzatori = new SimpleDetailCRUDController("Utilizzatori",Inventario_utilizzatori_laBulk.class,"buono_cs_utilizzatoriColl",vUtilizzatori);	

	protected it.cnr.jada.util.action.AbstractDetailCRUDController createDettaglio() {
		
	return new SimpleDetailCRUDController("Dettaglio",Buono_carico_scarico_dettBulk.class,"buono_carico_scarico_dettColl",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {			
			validate_Dettagli(context,model);
		}

		@Override
		public boolean isGrowable() {
			if (isBy_ordini()){
				return false;
			}
			return super.isGrowable();
		}
	};
	}
	
	private final SimpleDetailCRUDController dettagliFattura = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliFattura",Fattura_passiva_rigaBulk.class,"dettagliFatturaColl",this){
			public void validate(ActionContext context,OggettoBulk model) throws ValidationException {			
				validate_Dettagli_da_Fattura(context,model);
			}
		};

		private final SimpleDetailCRUDController righeInventarioDaFattura = new SimpleDetailCRUDController("RigheInventarioDaFattura",Buono_carico_scarico_dettBulk.class,null,dettagliFattura){
		
			public java.util.List getDetails() {			
				Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)CRUDCaricoInventarioBP.this.getModel();
				Fattura_passiva_rigaBulk riga_selezionata = (Fattura_passiva_rigaBulk)getParentModel();			
				return getRigheInventarioDaFatturaDetails (buono_cs, riga_selezionata);
			}
		};
	public final AbstractDetailCRUDController getRigheInventarioDaFattura() {
		return righeInventarioDaFattura;
	}

	public java.util.List getRigheInventarioDaFatturaDetails(
		Buono_carico_scaricoBulk buonoCS,
		Fattura_passiva_rigaBulk riga_selezionata) {
	
		if (buonoCS != null && riga_selezionata != null){
			BulkList list = (BulkList)buonoCS.getDettagliRigheHash().get(riga_selezionata);
			return list;
		}	
		else{
			return null;
		}
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
			
				// Controlla che sia stata specificata la line di AttivitÓ
				if (utilizzatore_LA.getLinea_attivita()==null || utilizzatore_LA.getLinea_attivita().getCd_linea_attivita() == null){
					throw new ValidationException ("GAE non valido. Indicare sia il codice del GAE, sia la sua percentuale di utilizzo");
				}
			
				// Controlla che non vi siano Linee di AttivitÓ DUPLICATE
				if (BulkCollections.containsByPrimaryKey(cdr,utilizzatore_LA.getLinea_attivita())){
					throw new ValidationException ("GAE duplicato. Non è possibile indicare più volte uno stesso GAE");
				}
				else {
					cdr.add(utilizzatore_LA.getLinea_attivita());
				}
			
				// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per Linea di AttivitÓ
				if (utilizzatore_LA.getPercentuale_utilizzo_la()!=null){
					percentuale_utilizzo_LA = percentuale_utilizzo_LA.add(utilizzatore_LA.getPercentuale_utilizzo_la());
				}
				else{
					throw new ValidationException ("La percentuale di utilizzo per i GAE non può essere nulla");
				}
			}

			// Controlla che il totale delle percentuali di utilizzo delle Linee di AttivitÓ sia 100
			if (percentuale_utilizzo_LA.compareTo(cento)!=0)
				throw new ValidationException ("La percentuale di utilizzo per i GAE non è valida");
		}
	}
	/**
	 * Metodo richiamato dal SimpleDetailCRUDController vUtilizzatori quando si passa
	 * da un CdR utilizzatore all'altro.
	 * Controlla se la percentuale di utilizzo delle linee di attivitÓ, per 
	 * CdR, Þ valida.
	 * Controlla, inoltre, se sono state fatte modifiche non valide su beni associati
	 * ad altri bene contestualmente al buono di Carico.
	 * 
	 **/
 
	private void validate_property_details(Buono_carico_scarico_dettBulk dett) throws ValidationException {
	
		Inventario_beniBulk bene = dett.getBene();
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)dett.getBuono_cs();
		
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA CATEGORIA PER IL BENE
		if (bene.getCategoria_Bene()==null)
			throw new ValidationException("Attenzione: indicare la Categoria di appartenenza del Bene");
		
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE PER IL BENE
		if (bene.getDs_bene()==null)
			throw new ValidationException("Attenzione: indicare la Descrizione del Bene");
				
		// CONTROLLA CHE SIA STATA SPECIFICATA UNA CONDIZIONE PER IL BENE
		if (bene.getCondizioneBene()==null)
			throw new ValidationException("Attenzione: indicare una Condizione per il Bene");

		// CONTROLLA, NEL CASO DI UN BENE ACCESSORIO, CHE SIA STATO SPECIFICATO IL BENE PRINCIPALE A CUI FAR RIFERIMENTO
		if (dett.isBeneAccessorio() && bene.getBene_principale()==null)
			throw new ValidationException("Attenzione: indicare un Bene Principale per il Bene Accessorio");

		// CONTROLLA CHE SIA STATA SPECIFICATA UNA UBICAZIONE PER IL BENE
		if (bene.getUbicazione()==null)
			throw new ValidationException("Attenzione: indicare l'Ubicazione del Bene");	
		
		// CONTROLLA CHE SIA STATO INSERITO LA QUANTITA' PER IL BENE
		if (dett.getQuantita()==null)
			throw new ValidationException("Attenzione: indicare la Quantita");

		// CONTROLLA CHE SIA STATO INSERITO IL PREZZO UNITARIO PER IL BENE
		if (dett.getValore_unitario()==null)
			throw new ValidationException("Attenzione: indicare il Prezzo Unitario");

		if (buonoC.isPerAumentoValore() && (dett.getValore_unitario() == null || (dett.getValore_unitario().compareTo(new java.math.BigDecimal(0))==0))){
			throw new ValidationException("Attenzione: indicare il Valore Caricato per il bene");
		}
		if (!buonoC.isPerAumentoValore()&&(dett.getV_utilizzatoriColl().size()==0)&&!buonoC.isByFattura()   &&!buonoC.isByOrdini() &&!buonoC.isByDocumento()&& !dett.isBeneAccessorio()&& !dett.getFl_bene_accessorio() )
			throw new ValidationException("Attenzione: bisogna indicare gli Utilizzatori");
	
		if ( ((Buono_carico_scaricoBulk)dett.getBuono_cs()).isPerAumentoValore() ){
		// Buono di Carico per aumento di valore
		// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE DEL BENE
		java.math.BigDecimal valore_bene = dett.getBene().getValoreBene().add(dett.getValore_unitario());
		
		if (dett.getBene().getImponibile_ammortamento() != null && dett.getBene().getImponibile_ammortamento().compareTo(valore_bene)>0){
			throw new ValidationException("Attenzione: il valore da ammortizzare di un bene deve essere inferiore  o uguale al valore del bene.\n" + 
					"Il valore da ammortizzare del bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valido");
			}
		} else {
		// Buono di Carico normale
		// CONTROLLA CHE IL VALORE DA AMMORTIZZARE SIA INFERIORE AL VALORE UNITARIO
			if (dett.getBene().getImponibile_ammortamento() != null && dett.getBene().getImponibile_ammortamento().compareTo(dett.getValore_unitario())>0){
			throw new ValidationException("Attenzione: il valore da ammortizzare di un bene deve essere inferiore  o uguale al valore del bene.\n" + 
					"Il valore da ammortizzare del bene " + (bene.getDs_bene()!=null?"'"+bene.getDs_bene()+"'":"") + " non è valido");
			}
		}
	}
	private void validate_Dettagli(ActionContext context,OggettoBulk model) throws ValidationException{
		if (isInserting()){	
		try {
			completeSearchTools(context, this);
			
			Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)model;
			Buono_carico_scaricoBulk buono_carico = (Buono_carico_scaricoBulk) dettaglio.getBuono_cs();
			PrimaryKeyHashtable accessori_contestuali = null;
			BulkList newBeni_associati = new BulkList();
		
			// Controlla che l'Utente abbia inserito tutti i campi Obbligatori
			validate_property_details(dettaglio);
		
			/*  Se il Dettaglio appena lasciato Þ un dettaglio a cui sono associati dei beni
			*  dichiarati contestaulmente, va a modificare le descrizioni dei beni associati.
			* Altresý, sostituisce la coppia Dettaglio Principale -  dettagli Associati 
			* attualmente presente nella HashTable.
			*/
			if (dettaglio.isAssociatoConAccessorioContestuale()){
				if ( dettaglio.getQuantita()!=null && dettaglio.getQuantita().compareTo(new Long("1"))!=0){
					dettaglio.setQuantita(new Long("1"));
					throw new ValidationException("Attenzione: la quantità di questa riga deve essere 1, poichè alcuni beni sono suoi accessori");
				}
				accessori_contestuali = buono_carico.getAccessoriContestualiHash();
				BulkList beni_associati = (BulkList)accessori_contestuali.get(dettaglio.getChiaveHash());
				for (Iterator i = beni_associati.iterator(); i.hasNext();){
					Buono_carico_scarico_dettBulk dettaglio_associato = (Buono_carico_scarico_dettBulk) i.next();
					dettaglio_associato.getBene().getBene_principale().setDs_bene(dettaglio.getBene().getDs_bene());
					dettaglio_associato.getBene().setUbicazione(dettaglio.getBene().getUbicazione());
					dettaglio_associato.getBene().setCategoria_Bene(dettaglio.getBene().getCategoria_Bene());
					dettaglio_associato.getBene().setAssegnatario(dettaglio.getBene().getAssegnatario());
					newBeni_associati.add(dettaglio_associato);
				}		
				accessori_contestuali.put(dettaglio.getChiaveHash(),newBeni_associati);
			}
			
			if (dettaglio.isAccessorioContestuale()){
				accessori_contestuali = buono_carico.getAccessoriContestualiHash();
				for (java.util.Enumeration e = accessori_contestuali.keys(); e.hasMoreElements();) {
					String chiave_bene_padre = (String)e.nextElement();
					newBeni_associati = (BulkList)accessori_contestuali.get(chiave_bene_padre);
					if (newBeni_associati.containsByPrimaryKey(dettaglio)){			
						newBeni_associati.remove(dettaglio);
						newBeni_associati.add(dettaglio);
						break;
					}	
				}
			}
			} catch (BusinessProcessException e1) {
				handleException(e1);
			}
		}			
		
		validate_Percentuali_CdR(context, model);
	
	}
	/**
	 * Metodo richiamato dal SimpleDetailCRUDController vUtilizzatori quando si passa
	 * da un CdR utilizzatore all'altro.
	 * Controlla se la percentuale di utilizzo delle linee di attivitÓ, per 
	 * CdR, Þ valida.
	 **/
 
	public void validate_Percentuali_CdR(ActionContext context,OggettoBulk model) throws ValidationException {

		Buono_carico_scarico_dettBulk buono_dettaglio = (Buono_carico_scarico_dettBulk)model;
		SimpleBulkList cdr_utilizzatori = buono_dettaglio.getV_utilizzatoriColl();	
		java.math.BigDecimal percentuale_utilizzo_CdR = new java.math.BigDecimal("0");	
		Vector cdr = new Vector();
		
		if (cdr_utilizzatori.size()>0){
	
			Iterator i = cdr_utilizzatori.iterator();		
			while (i.hasNext()){
				Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)i.next();

				// Richiama la procedura di validazione delle singole Linee di AttivitÓ
				validate_Percentuali_LA(context,utilizzatore);
			
				// Controlla che sia stata specificata il CdR
				if (utilizzatore.getCdr()==null){
					throw new ValidationException ("Utilizzatore non valido. Indicare sia il codice del CdR Utilizzatore, sia la sua percentuale di utilizzo");
				}
			
				// Controlla che non vi siano Linee di AttivitÓ DUPLICATE
				if (BulkCollections.containsByPrimaryKey(cdr,utilizzatore.getCdr())){
					throw new ValidationException ("CdR Utilizzatore duplicato. Non è possibile indicare più volte uno stesso CdR come Utilizzatore");
				}
				else {
					cdr.add(utilizzatore.getCdr());
				}
			
				// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per il CdR
				if (utilizzatore.getPercentuale_utilizzo_cdr()!=null && ((utilizzatore.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal("0")))>0)){
					percentuale_utilizzo_CdR = percentuale_utilizzo_CdR.add(utilizzatore.getPercentuale_utilizzo_cdr());				 
				}
				else{
					throw new ValidationException ("La percentuale di utilizzo per gli Utilizzatori non può essere nulla");
				}
			
				// Controlla che per ogni CdR specificato siano state indicate anche delle Linee di AttivitÓ
				if (utilizzatore.getBuono_cs_utilizzatoriColl()==null || (utilizzatore.getBuono_cs_utilizzatoriColl().size()==0)){
					throw new ValidationException ("Attenzione! Specificare per ogni Utilizzatore i GAE corrispondenti");
				}
			}

			// Controlla che il totale delle percentuali di utilizzo delle Linee di AttivitÓ sia 100
			if ((percentuale_utilizzo_CdR.compareTo(new java.math.BigDecimal("100")))!=0)
				throw new ValidationException ("La percentuale di utilizzo per gli Utilizzatori non è valida");
		}
	
	}
	public SimpleDetailCRUDController getDettagliFattura() {
		return dettagliFattura;
	}
	public AbstractDetailCRUDController getDettaglio() {
		return dettaglio;
	}
	public boolean isBringbackButtonEnabled() {
		return isInserting() || isEditing();
	}
	public boolean isModValore_unitario(){
		return isNonIniziatoAmmortamento();
		//rp 15/12/2006 abilitati tutti alla modifica
		//&& isAmministratore();	   
	}

	/**
	  * Abilita il pulsante di "Nuovo", nella finestra dei dettagli del Buono di Carico.
	  *	Se il Buono Þ stato creato da una Fattura Passiva, o Þ in stato di EDIT, disabilita 
	  *	il tasto di aggiunta di nuovi dettagli al Buono.
	  *
	  * @return <code>boolean</code>
	**/   
	public boolean isCRUDAddButtonEnabled() {
	
		return (isInserting() && !isBy_fattura() && !isBy_documento() && !isBy_ordini());
	}
	/**
	  * Abilita il pulsante di "Elimina", nella finestra dei dettagli del Buono di Carico.
	  *	Se il Buono Þ stato creato da una Fattura Passiva, o Þ in stato di EDIT, disabilita 
	  *	il tasto di cancellazione dei dettagli dal Buono.
	  *
	  * @return <code>boolean</code>
	**/ 
	public boolean isCRUDDeleteButtonEnabled() {
	
		return (isInserting() && !isBy_fattura() && !isBy_documento() && !isBy_ordini());
	}
	/**
	  * Disabilita il pulsante di "Elimina", nel form del Buono di Carico.
	  *	Non Þ possibile cancellare un Buono presente sul DB.
	  *
	  * @return <code>boolean</code> FALSE
	**/ 
	public boolean isDeleteButtonEnabled() {
	
		return isEditing();
	}

	@Override
	public boolean isBringbackButtonHidden() {
		if (isBy_ordini()){
			return true;
		}
		return super.isBringbackButtonHidden();
	}

	/**
	  * Nascondo il pulsante di "Elimina", nel form del Buono di Carico.
	  *	Non Þ possibile cancellare un Buono presente sul DB.
	  *
	  * @return <code>boolean</code> TRUE
	**/ 
	public boolean isDeleteButtonHidden() {
		if (isBy_fattura()||isBy_documento()||isBy_ordini())
			return true;
		return false;
	}

	public boolean isNonAmmortizzato() {

		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)getDettaglio().getModel();
		if (dett!=null && dett.getBene()!=null && dett.getBene().getFl_ammortamento() != null){
			return !dett.getBene().getFl_ammortamento().booleanValue();
		}
	
		return false;
	}
	public boolean isNonIniziatoAmmortamento() {

		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)getDettaglio().getModel();
		if (dett!=null && dett.getBene()!=null && dett.getBene().getValore_ammortizzato() != null){
			return dett.getBene().getValore_ammortizzato().compareTo(new BigDecimal(0))==0 ;//&& dett.getBene().isCancellabile();
		}
	
		return false;
	}

	public boolean isNonScaricato() {

		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)getDettaglio().getModel();
		if (dett!=null && dett.getBene()!=null && dett.getBene().getFl_totalmente_scaricato() != null){
			return !dett.getBene().getFl_totalmente_scaricato().booleanValue();
		}
	
		return false;
	}

	/**
	 * Restituisce true se la Categoria Gruppo alla quale appartiene il Bene, NON Þ soggetta
	 *	ad ammortamento (CATEGORIA_GRUPPPO_INVENT.FL_AMMORTAMENTO==FALSE)
	 *
	 * @return <code>boolean</code> 
	 */ 
	public boolean isNotAmmortizzabile() {

		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)getDettaglio().getModel();
		if (dett!=null && 
				dett.getBene()!=null && 
				dett.getBene().getCategoria_Bene() != null && 
				dett.getBene().getCategoria_Bene().getFl_ammortamento() != null){
				
			return !dett.getBene().getCategoria_Bene().getFl_ammortamento().booleanValue();
		}
	
		return true;
	}
	public void aggiungiDettagliCarico(it.cnr.jada.UserContext userContext, java.util.List beni) throws it.cnr.jada.comp.ComponentException{

		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)getModel();
		Buono_carico_scarico_dettBulk dettCarico = null;
		Inventario_beniBulk bene = null;
	
		for (Iterator i = beni.iterator(); i.hasNext();){	
			dettCarico = new Buono_carico_scarico_dettBulk();
			bene = (Inventario_beniBulk)i.next();
			if(bene.getCategoria_Bene()!=null && buonoC.getData_registrazione()!=null && bene.getCategoria_Bene().getData_cancellazione()!=null &&
					bene.getCategoria_Bene().getData_cancellazione().before(buonoC.getData_registrazione()))
				throw new ApplicationException("Il Bene "+bene.getNr_inventario()+" ha un categoria non più valida");
			dettCarico.setBuono_cs(buonoC);
			dettCarico.setBene(bene);
			dettCarico.setQuantita(new Long(1));
			dettCarico.setValore_unitario(new java.math.BigDecimal(0));
			buonoC.getBuono_carico_scarico_dettColl().add(dettCarico);		
		}

	}	
	
	public Long getProgressivo_beni() {
		return progressivo_beni;
	}

	public void setProgressivo_beni(Long long1) {
		progressivo_beni = long1;
	}
	/**
	 * Metodo richiamato dal SimpleDetailCRUDController dettagliFattura quando si passa
	 * da un dettaglio dell'inventario all'altro.
	 * Controlla, tra le altre cose, che la somma delle quantitÓ specificate per ogni 
	 * singolo dettaglio di Inventario, sia uguale alla quantitÓ indicata per la riga
	 * di Fattura.
	 * 
	 **/
 
	private void validate_Dettagli_da_Fattura(ActionContext context,OggettoBulk model) throws ValidationException {

		java.math.BigDecimal totale = new java.math.BigDecimal(0);	
		Fattura_passiva_rigaIBulk riga_fattura = (Fattura_passiva_rigaIBulk)model;
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)getModel();	
		PrimaryKeyHashtable righeInventarioHash = (PrimaryKeyHashtable)buonoCS.getDettagliRigheHash();	
		if (righeInventarioHash != null){
		BulkList dettagliInventario = (BulkList)righeInventarioHash.get(riga_fattura);
			if (dettagliInventario.size()>0){
				for (Iterator i = dettagliInventario.iterator(); i.hasNext();){
					Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)i.next();
					if (dettaglio.getQuantita()!=null){
						totale = totale.add(new BigDecimal(dettaglio.getQuantita().longValue()));
					}
					if (dettaglio.isAssociatoConAccessorioContestuale()){
						BulkList newBeni_associati = new BulkList();
						if ( dettaglio.getQuantita()!=null && dettaglio.getQuantita().compareTo(new Long("1"))!=0){
						dettaglio.setQuantita(new Long("1"));
						throw new ValidationException("Attenzione. La Quantità di questa riga deve essere 1, poichè alcuni beni sono suoi accessori");
					}
					PrimaryKeyHashtable accessori_contestuali = buonoCS.getAccessoriContestualiHash();
					BulkList beni_associati = (BulkList)accessori_contestuali.get(dettaglio.getChiaveHash());
					for (Iterator i_beni_associati = beni_associati.iterator(); i_beni_associati.hasNext();){
						Buono_carico_scarico_dettBulk dettaglio_associato = (Buono_carico_scarico_dettBulk) i_beni_associati.next();
						dettaglio_associato.getBene().getBene_principale().setDs_bene(dettaglio.getBene().getDs_bene());				
						newBeni_associati.add(dettaglio_associato);
					}		
					accessori_contestuali.put(dettaglio.getChiaveHash(),newBeni_associati);
			
					}
				}

				if (!(totale.compareTo(riga_fattura.getQuantita())==0)){
					throw new ValidationException("Attenzione: " +
							"il totale delle quantità indicate per la riga di Fattura '" + 
							riga_fattura.getDs_riga_fattura() + 
							"' non corrisponde al totale della riga stessa");
				}
			
			}
		}
	}
	
	
	public final SimpleDetailCRUDController getVUtilizzatori() {
		return vUtilizzatori;
	}
	public final SimpleDetailCRUDController getUtilizzatori() {
		return utilizzatori;
	}
	public Forward findDefaultForward() {
	
		if (isBy_fattura()&& isFirst()){
			return findForward("daFattura");
		}else if (isBy_documento()&& isFirst()){
			return findForward("daDocumento");
		}else{
			return findForward("default");
		}
	}
	
	public SearchProvider getBeneSearchProvider(ActionContext context){
		return new SearchProvider() {
				public it.cnr.jada.util.RemoteIterator search(it.cnr.jada.action.ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk prototype) throws it.cnr.jada.action.BusinessProcessException {				
					Buono_carico_scaricoBulk buonoC =(Buono_carico_scaricoBulk)getModel();
					boolean no_accessori = buonoC.isByFattura()||buonoC.isByDocumento()||buonoC.isByOrdini();
					try{
						return getListaBeni(context.getUserContext(), no_accessori, buonoC.getBuono_carico_scarico_dettColl(), clauses);
					} catch (Throwable t){
						return null;
					}			
				}
			};
	}	
	/**
	  *	 Cerca i beni disponibili.
	  * Cerca i beni disponibili per una operazione di Carico per aumento di valore o per una 
	  *	associazione ad un riga di Fattura Passiva. Invoca il metodo 
	  *	<code>BuonoCaricoComponent.getListaBeni</code>.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
	  * @param no_accessori <code>boolean</code> indica se escludere i beni accessori.
	  * @param beni_da_escludere la <code>SimpleBulkList</code> lista di beni da escludere, perchÞ giÓ utilizzati.
	  * @param clauses le <code>CompoundFindClause</code> clausole della ricerca.
	  *
	  * @return iterator <code>RemoteIterator</code> l'iterator sui beni trovati
	**/ 
	public it.cnr.jada.util.RemoteIterator getListaBeni(
		it.cnr.jada.UserContext userContext, 
		boolean no_accessori, 
		SimpleBulkList beni_da_escludere,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws BusinessProcessException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException {

		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).getListaBeni(userContext, (Buono_carico_scaricoBulk)this.getModel(), no_accessori,beni_da_escludere, clauses);
	}
	public void setIsNumGruppiErrato(boolean newIsNumGruppiErrato) {
		isNumGruppiErrato = newIsNumGruppiErrato;
	}

	public boolean isNumGruppiErrato() {
		return isNumGruppiErrato;
	}
	public void setIsQuantitaEnabled(boolean enabled) {
		
		 isQuantitaEnabled = enabled;
	}
	public boolean isQuantitaEnabled() {
		
		return isQuantitaEnabled;
	}
	public Long incrementaProgressivo_beni() {

		Long prog = progressivo_beni;
		progressivo_beni = new Long(progressivo_beni.longValue() + 1);
		
		return prog;
	}
	public String getLabelData_registrazione(){
		return "Data Carico";
	}
	private final SimpleDetailCRUDController dettagliDocumento = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliDocumento",Documento_generico_rigaBulk.class,"dettagliDocumentoColl",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {			
			validate_Dettagli_da_Documento(context,model);
		}
	};

	public SimpleDetailCRUDController getDettagliDocumento() {
		return dettagliDocumento;
	}
	private final SimpleDetailCRUDController righeInventarioDaDocumento = new SimpleDetailCRUDController("RigheInventarioDaDocumento",Buono_carico_scarico_dettBulk.class,null,dettagliDocumento){
		
		public java.util.List getDetails() {			
			Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)CRUDCaricoInventarioBP.this.getModel();
			Documento_generico_rigaBulk riga_selezionata = (Documento_generico_rigaBulk)getParentModel();			
			return getRigheInventarioDaDocumentoDetails (buono_cs, riga_selezionata);
		}
	};
	public java.util.List getRigheInventarioDaDocumentoDetails(
			Buono_carico_scaricoBulk buonoCS,
			Documento_generico_rigaBulk riga_selezionata) {
		
			if (buonoCS != null && riga_selezionata != null){
				BulkList list = (BulkList)buonoCS.getDettagliRigheDocHash().get(riga_selezionata);
				return list;
			}	
			else{
				return null;
			}
		}

	public SimpleDetailCRUDController getRigheInventarioDaDocumento() {
		return righeInventarioDaDocumento;
	}	
	/**
	 * Metodo richiamato dal SimpleDetailCRUDController dettagliFattura quando si passa
	 * da un dettaglio dell'inventario all'altro.
	 * Controlla, tra le altre cose, che la somma delle quantitÓ specificate per ogni 
	 * singolo dettaglio di Inventario, sia uguale alla quantitÓ indicata per la riga
	 * di Fattura.
	 * 
	 **/
 
	private void validate_Dettagli_da_Documento(ActionContext context,OggettoBulk model) throws ValidationException {

		java.math.BigDecimal totale = new java.math.BigDecimal(0);	
		Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)model;
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)getModel();	
		PrimaryKeyHashtable righeInventarioHash = (PrimaryKeyHashtable)buonoCS.getDettagliRigheDocHash();	
		if (righeInventarioHash != null){
		BulkList dettagliInventario = (BulkList)righeInventarioHash.get(riga);
			if (dettagliInventario.size()>0){
				for (Iterator i = dettagliInventario.iterator(); i.hasNext();){
					Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)i.next();
					if (dettaglio.getQuantita()!=null){
						totale = totale.add(new BigDecimal(dettaglio.getQuantita().longValue()));
					}
					if (dettaglio.isAssociatoConAccessorioContestuale()){
						BulkList newBeni_associati = new BulkList();
						if ( dettaglio.getQuantita()!=null && dettaglio.getQuantita().compareTo(new Long("1"))!=0){
						dettaglio.setQuantita(new Long("1"));
						throw new ValidationException("Attenzione. La Quantità di questa riga deve essere 1, poichè alcuni beni sono suoi accessori");
					}
					PrimaryKeyHashtable accessori_contestuali = buonoCS.getAccessoriContestualiHash();
					BulkList beni_associati = (BulkList)accessori_contestuali.get(dettaglio.getChiaveHash());
					for (Iterator i_beni_associati = beni_associati.iterator(); i_beni_associati.hasNext();){
						Buono_carico_scarico_dettBulk dettaglio_associato = (Buono_carico_scarico_dettBulk) i_beni_associati.next();
						dettaglio_associato.getBene().getBene_principale().setDs_bene(dettaglio.getBene().getDs_bene());				
						newBeni_associati.add(dettaglio_associato);
					}		
					accessori_contestuali.put(dettaglio.getChiaveHash(),newBeni_associati);
			
					}
				}
			}
		}
	}
}

