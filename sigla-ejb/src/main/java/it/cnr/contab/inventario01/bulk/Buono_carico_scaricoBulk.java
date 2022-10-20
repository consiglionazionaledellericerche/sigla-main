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
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;

import java.math.BigDecimal;

public class Buono_carico_scaricoBulk extends Buono_carico_scaricoBase {
	
	public final static String CARICO = "C";
	public final static String SCARICO = "S";
	private String local_transactionID;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo_consegnataria;
	private TerzoBulk consegnatario;
	private TerzoBulk delegato;
	protected Tipo_carico_scaricoBulk tipoMovimento;
	private java.util.Collection tipoMovimenti;
	private Boolean byFattura = new Boolean(false);
	private Boolean byFatturaPerAumentoValore= new Boolean(false);
	private Id_inventarioBulk inventario;	
	private java.util.Collection condizioni;
	private String cds_scrivania;
	private String uo_scrivania;
	private Integer nr_inventario;
	private Integer cd_barre;
//	HashTable di Beni che sono accessori di Beni Inseriti Contestualmente
	private PrimaryKeyHashtable accessoriContestualiHash;
	private it.cnr.jada.bulk.SimpleBulkList buono_carico_scarico_dettColl;
//	HashTable di rihge fattura
	private PrimaryKeyHashtable dettagliRigheHash;
//	Collezione contentente le righe di dettaglio della Fattura Passiva
	private BulkList dettagliFatturaColl = new BulkList();
	 
//	Collezione contentente le righe di dettaglio della Documento generico Passivo
	private BulkList dettagliDocumentoColl = new BulkList();
	private Boolean byDocumento = new Boolean(false);
	private Boolean byOrdini = new Boolean(false);
	private Boolean byDocumentoPerAumentoValore= new Boolean(false);
	
	private PrimaryKeyHashtable dettagliRigheDocHash;
	private Boolean perVendita= new Boolean(false);
	public Buono_carico_scaricoBulk() {
		super();
	}
	public Buono_carico_scaricoBulk(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s) {
		super(pg_inventario, ti_documento, esercizio, pg_buono_c_s);
		setInventario(new it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk(pg_inventario));
	}
	public TerzoBulk getConsegnatario() {
		return consegnatario;
	}

	public TerzoBulk getDelegato() {
		return delegato;
	}

	public Id_inventarioBulk getInventario() {
		return inventario;
	}
	public java.util.Collection getTipoMovimenti() {
		return tipoMovimenti;
	}
	public Tipo_carico_scaricoBulk getTipoMovimento() {
		return tipoMovimento;
	}
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo_consegnataria() {
		return uo_consegnataria;
	}

	public void setConsegnatario(TerzoBulk bulk) {
		consegnatario = bulk;
	}

	public void setDelegato(TerzoBulk bulk) {
		delegato = bulk;
	}

	public void setInventario(Id_inventarioBulk bulk) {
		inventario = bulk;
	}
	public void setTipoMovimenti(java.util.Collection collection) {
		tipoMovimenti = collection;
	}

	public void setTipoMovimento(Tipo_carico_scaricoBulk bulk) {
		tipoMovimento = bulk;
	}

	public void setUo_consegnataria(
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk bulk) {
		uo_consegnataria = bulk;
	}
	public void setPg_inventario(java.lang.Long pg_inventario) {
		this.getInventario().setPg_inventario(pg_inventario);
	}
	public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico) {
		this.getTipoMovimento().setCd_tipo_carico_scarico(cd_tipo_carico_scarico);
	}
	public java.lang.Long getPg_inventario() {
		it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = this.getInventario();
		if (inventario == null)
			return null;
		return inventario.getPg_inventario();
	}
	public java.lang.String getCd_tipo_carico_scarico() {
		it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk tipoMovimento = this.getTipoMovimento();
		if (tipoMovimento == null)
			return null;
		return tipoMovimento.getCd_tipo_carico_scarico();
	}

	public void setByOrdini(Boolean boolean1) {
		byOrdini = boolean1;
	}
	public boolean isByOrdini() {

		if (byOrdini == null)
			return false;
		return byOrdini.booleanValue();
	}
	public void setByFattura(Boolean boolean1) {
		byFattura = boolean1;
	}
	public boolean isByFattura() {

		if (byFattura == null)
			return false;
		return byFattura.booleanValue();
	}
	public boolean isByFatturaPerAumentoValore() {

		if (byFatturaPerAumentoValore == null)
			return false;
		
		return byFatturaPerAumentoValore.booleanValue();
	}
	public boolean isPerAumentoValore() {
	
		if (getTipoMovimento() == null || getTipoMovimento().getFl_aumento_valore() == null)
			return false;
		
		return getTipoMovimento().getFl_aumento_valore().booleanValue();
	}

	public void setByFatturaPerAumentoValore(Boolean boolean1) {
		setByFattura(boolean1);
		byFatturaPerAumentoValore = boolean1;
	}

	public String getCds_scrivania() {
		return cds_scrivania;
	}

	public java.util.Collection getCondizioni() {
		return condizioni;
	}
	public String getUo_scrivania() {
		return uo_scrivania;
	}
	public void setCds_scrivania(String string) {
		cds_scrivania = string;
	}
	public void setCondizioni(java.util.Collection collection) {
		condizioni = collection;
	}
	public void setUo_scrivania(String string) {
		uo_scrivania = string;
	}
	public Integer getNr_inventario() {
		return nr_inventario;
	}
	public void setNr_inventario(Integer integer) {
		nr_inventario = integer;
	}


	public PrimaryKeyHashtable getAccessoriContestualiHash() {
		return accessoriContestualiHash;
	}
	
	public void setAccessoriContestualiHash(PrimaryKeyHashtable hashtable) {
		accessoriContestualiHash = hashtable;
	}
	public Long addToAccessoriContestualiHash(
		Buono_carico_scarico_dettBulk bene_padre, 
		Buono_carico_scarico_dettBulk bene_figlio,
		Long progressivo) {


		if (accessoriContestualiHash == null)
			accessoriContestualiHash = new PrimaryKeyHashtable();

		BulkList beni_associati = null;
		if (bene_padre.getChiaveHash()!=null){
			beni_associati = (BulkList)accessoriContestualiHash.get(bene_padre.getChiaveHash());
		}
	
		if (beni_associati == null){
			/* Se beni_associati == null, significa che non ci sono beni associati a 
			* questo bene, quindi crea una nuova BulkList che verrà associata al bene 
			* principale. In seguito vengono settati la proprieta Nr_Inventario e Progressivo
			* in modo da creare una chiave primaria per il bene principale, così da essere
			* univocamente rintracciato nella HashTable.
			*/
			beni_associati = new BulkList();
			bene_padre.setNr_inventario(progressivo);
			bene_padre.setProgressivo(new Integer("0"));
			bene_padre.setPg_inventario(getPg_inventario());
			
			progressivo = new Long(progressivo.longValue()+1);
		}

		/* Viene assegnato al bene figlio il codice (Nr_inventario) fittizio del padre
		*	ed un progreessivo (Progressivo) che lo identifichi in modo.
		*/
		bene_figlio.setNr_inventario(bene_padre.getNr_inventario());
		bene_figlio.setProgressivo(new Integer(Integer.toString(beni_associati.size()+1)));
		bene_figlio.setPg_inventario(getPg_inventario());
		beni_associati.add(bene_figlio);	
		accessoriContestualiHash.put(bene_padre.getChiaveHash(), beni_associati);
	
		return progressivo;
	
	}
	public int removeFromAccessoriContestualiHash(
		Buono_carico_scarico_dettBulk bene_figlio) {
		
		if (accessoriContestualiHash != null){
			for (java.util.Enumeration e = accessoriContestualiHash.keys(); e.hasMoreElements();) {
				String chiave_bene_padre = (String)e.nextElement();
				BulkList beni_accessori = (BulkList)accessoriContestualiHash.get(chiave_bene_padre);
				if (beni_accessori.containsByPrimaryKey(bene_figlio)){
					beni_accessori.removeByPrimaryKey(bene_figlio);
					if (beni_accessori.isEmpty()){				
						accessoriContestualiHash.remove(chiave_bene_padre);
						if (accessoriContestualiHash.isEmpty()){
							setAccessoriContestualiHash(null);
						}
					}
					break;
				}
			}
		}
		return (accessoriContestualiHash != null?accessoriContestualiHash.size():0);
	
	}
	public it.cnr.jada.bulk.SimpleBulkList getBuono_carico_scarico_dettColl() {
		return buono_carico_scarico_dettColl;
	}

	public void setBuono_carico_scarico_dettColl(it.cnr.jada.bulk.SimpleBulkList list) {
		buono_carico_scarico_dettColl = list;
	}
	
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		 return new it.cnr.jada.bulk.BulkCollection[] { this.getBuono_carico_scarico_dettColl() };
	}
	public Buono_carico_scarico_dettBulk  removeFromBuono_carico_scarico_dettColl( int indiceDiLinea ) {
		Buono_carico_scarico_dettBulk  element = (Buono_carico_scarico_dettBulk )buono_carico_scarico_dettColl.get(indiceDiLinea);
		return (Buono_carico_scarico_dettBulk )buono_carico_scarico_dettColl.remove(indiceDiLinea);
	}

	public int addToBuono_carico_scarico_dettColl (Buono_carico_scarico_dettBulk nuovo)
	{	
		nuovo.setBuono_cs(this);
		getBuono_carico_scarico_dettColl().add(nuovo);
		if (!this.isByOrdini()){
			nuovo.setBene(new Inventario_beniBulk());
			nuovo.getBene().setInventario(this.getInventario());
			nuovo.getBene().setPg_inventario(this.getPg_inventario());
		nuovo.getBene().setTi_commerciale_istituzionale(TipoIVA.ISTITUZIONALE.value());
			nuovo.getBene().setFl_totalmente_scaricato(java.lang.Boolean.FALSE);
			nuovo.getBene().setCategoria_Bene(new it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk());
			nuovo.getBene().setUbicazione(new Ubicazione_beneBulk());
			nuovo.getBene().setAssegnatario(new TerzoBulk());
			if(nuovo.getBene().getCondizioneBene() == null) {
				nuovo.getBene().setCondizioneBene(new Condizione_beneBulk());
				nuovo.getBene().setCd_condizione_bene("4");
			}
		}
		return getBuono_carico_scarico_dettColl().size()-1;
	}
	public PrimaryKeyHashtable getDettagliRigheHash() {
		return dettagliRigheHash;
	}

	public void setDettagliRigheHash(PrimaryKeyHashtable hashtable) {
		dettagliRigheHash = hashtable;
	}
	public void addToDettagliRigheHash(
		Fattura_passiva_rigaBulk newriga_fattura,
		BulkList rigoInventarioColl) {

		if (dettagliRigheHash == null)
			dettagliRigheHash = new PrimaryKeyHashtable();
	
		dettagliRigheHash.put(newriga_fattura,rigoInventarioColl);
	}
	public void removeFromDettagliRigheHash(
		Fattura_passiva_rigaBulk deleteRow) {
		
		dettagliRigheHash.remove(deleteRow);
	}
	public int addToDettagliFatturaColl (Fattura_passiva_rigaBulk nuovoRigo)
	{

		dettagliFatturaColl.add(nuovoRigo);
		return dettagliFatturaColl.size()-1;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/21/2001 5:15:54 PM)
	 * @return java.lang.String
	 */
	public void caricaDettagliFromTransito(java.util.List<Transito_beni_ordiniBulk> dettagliDaInventariare){
		if (dettagliDaInventariare != null){
			for (Transito_beni_ordiniBulk transito : dettagliDaInventariare){
				Buono_carico_scarico_dettBulk rigoInventario = new Buono_carico_scarico_dettBulk();
				Inventario_beniBulk bene = new Inventario_beniBulk();
				rigoInventario.setToBeCreated();
				bene.setToBeCreated();
				rigoInventario.setBuono_cs(this);
				bene.setDs_bene(transito.getDs_bene());
				rigoInventario.setQuantita(new Long("1"));
				rigoInventario.setBene(bene);
				bene.setCollocazione(transito.getCollocazione());
				bene.setValore_iniziale(transito.getValore_iniziale());
				bene.setAssegnatario(transito.getAssegnatario());
				bene.setCategoria_Bene(transito.getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo());
				bene.setCondizioneBene(transito.getCondizioneBene());
				bene.setInventario(transito.getInventario());
				bene.setTipo_ammortamento(transito.getTipo_ammortamento());
				bene.setFl_ammortamento(transito.getFl_ammortamento());
				bene.setUbicazione(transito.getUbicazione());
				bene.setCd_barre(transito.getCd_barre());
				bene.setTarga(transito.getTarga());
				bene.setSeriale(transito.getSeriale());
				bene.setDt_acquisizione(transito.getDt_acquisizione());
				bene.setTi_commerciale_istituzionale(transito.getTi_commerciale_istituzionale());
				rigoInventario.setIdTransito(transito.getId());
				rigoInventario.setValore_unitario(transito.getValore_iniziale());
				rigoInventario.CalcolaTotaleBene();
				rigoInventario.setTi_documento(CARICO);
				addToBuono_carico_scarico_dettColl(rigoInventario);
			}
		}
	}
	public void completeFrom(java.util.List dettagliDaInventariare) throws it.cnr.jada.comp.ApplicationException {
		java.math.BigDecimal valore_unitario = new java.math.BigDecimal(0);
		if (dettagliDaInventariare != null){
			dettagliRigheHash = new PrimaryKeyHashtable();
			dettagliRigheDocHash = new PrimaryKeyHashtable();
			Fattura_passiva_rigaBulk rigoFattura;
			Fattura_attiva_rigaBulk rigoFattura_at=null;
			Nota_di_credito_rigaBulk rigonc=null;
			Buono_carico_scarico_dettBulk rigoInventario;
			Inventario_beniBulk bene;
			BulkList dettagliInventarioPerRigaFatturaColl;
			java.util.Iterator i = dettagliDaInventariare.iterator();
			if (this.getTi_documento().equals(Buono_carico_scaricoBulk.CARICO)){
				while (i.hasNext()){
					if (dettagliDaInventariare.get(0) instanceof Fattura_passiva_rigaBulk){
					
						setDettagliFatturaColl(new BulkList(dettagliDaInventariare));
						rigoFattura = (Fattura_passiva_rigaBulk)i.next();
						/* Controlla che il Bene Proveniente Dalla Fattura abbia un Bene/Servizio
						 *	valido, ossia che contenga la Categoria Gruppo.			 
						*/					
						if (rigoFattura.getBene_servizio().getCategoria_gruppo() == null)
							throw new it.cnr.jada.comp.ApplicationException("Il Bene/Servizio del dettaglio \"" + rigoFattura.getDs_riga_fattura() + "\" non ha definito alcuna categoria di appartenenza! Operazione interrotta.");
					
						dettagliInventarioPerRigaFatturaColl = new BulkList();
						rigoInventario = new Buono_carico_scarico_dettBulk();
						bene = new Inventario_beniBulk();
						rigoInventario.setCrudStatus(TO_BE_CREATED);
						bene.setCrudStatus(TO_BE_CREATED);
						rigoInventario.setBuono_cs(this);
						bene.setDs_bene(rigoFattura.getDs_riga_fattura());
						rigoInventario.setQuantita(new Long(rigoFattura.getQuantita().longValue()));			
						rigoInventario.setBene(bene);
						// Assegna il Prezzo unitario: il prezzo Þ diverso a seconda che il dettaglio della Fattura sia ISTITUZIONALE o COMMERCIALE
						if (rigoFattura.getTi_istituz_commerc().equals(TipoIVA.ISTITUZIONALE.value())){
							valore_unitario = rigoFattura.getIm_imponibile().add(rigoFattura.getIm_iva());
							valore_unitario = valore_unitario.divide(rigoFattura.getQuantita(), 2 ,java.math.BigDecimal.ROUND_HALF_UP);
							rigoInventario.setValore_unitario(valore_unitario);				
						} else {
							valore_unitario = rigoFattura.getIm_imponibile().divide(rigoFattura.getQuantita(), 2 ,java.math.BigDecimal.ROUND_HALF_UP);
							rigoInventario.setValore_unitario(valore_unitario);
						}			
						
						rigoInventario.getBene().setValore_iniziale(rigoInventario.getValore_unitario());			
						rigoInventario.CalcolaTotaleBene();
						rigoInventario.setTi_documento(CARICO);
						rigoInventario.getBene().setTi_commerciale_istituzionale(rigoFattura.getTi_istituz_commerc());
						dettagliRigheHash.put(rigoFattura,dettagliInventarioPerRigaFatturaColl);
						setDettagliRigheHash(dettagliRigheHash);
					} else if (dettagliDaInventariare.get(0) instanceof Documento_generico_rigaBulk){
						setDettagliDocumentoColl(new BulkList(dettagliDaInventariare));
						

						Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
						/* Controlla che il Bene Proveniente Dalla Fattura abbia un Bene/Servizio
						 *	valido, ossia che contenga la Categoria Gruppo.			 
						*/			
						//if (rigoFattura.getBene_servizio().getCategoria_gruppo() == null)
							//throw new it.cnr.jada.comp.ApplicationException("Il Bene/Servizio del dettaglio \"" + rigoFattura.getDs_riga_fattura() + "\" non ha definito alcuna categoria di appartenenza! Operazione interrotta.");
					
						dettagliInventarioPerRigaFatturaColl = new BulkList();
						rigoInventario = new Buono_carico_scarico_dettBulk();
						bene = new Inventario_beniBulk();
						rigoInventario.setCrudStatus(TO_BE_CREATED);
						bene.setCrudStatus(TO_BE_CREATED);
						rigoInventario.setBuono_cs(this);
						bene.setDs_bene(riga.getDs_riga());
						rigoInventario.setQuantita(new Long(1));			
						rigoInventario.setBene(bene);
						rigoInventario.setValore_unitario(riga.getIm_riga());			
						rigoInventario.getBene().setValore_iniziale(riga.getIm_riga());			
						rigoInventario.CalcolaTotaleBene();
						rigoInventario.setTi_documento(CARICO);
						rigoInventario.getBene().setTi_commerciale_istituzionale(riga.getDocumento_generico().getTi_istituz_commerc());
						dettagliRigheDocHash.put(riga,dettagliInventarioPerRigaFatturaColl);
						setDettagliRigheDocHash(dettagliRigheDocHash);
					}						
				  }
				}
				else{
					//dettagliRigheHash = new PrimaryKeyHashtable();
					
					while (i.hasNext()){
						if (dettagliDaInventariare.get(0).getClass()==Documento_generico_rigaBulk.class){					
							setDettagliDocumentoColl(new BulkList(dettagliDaInventariare));
							Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
							dettagliInventarioPerRigaFatturaColl = new BulkList();
							rigoInventario = new Buono_carico_scarico_dettBulk();
							bene = new Inventario_beniBulk();
							rigoInventario.setCrudStatus(TO_BE_CREATED);
							bene.setCrudStatus(TO_BE_CREATED);
							rigoInventario.setBuono_cs(this);
							bene.setDs_bene(riga.getDs_riga());
							rigoInventario.setQuantita(new Long(1));			
							rigoInventario.setBene(bene);
							rigoInventario.setValore_unitario(riga.getIm_riga());			
							rigoInventario.getBene().setValore_iniziale(riga.getIm_riga());			
							rigoInventario.CalcolaTotaleBene();
							rigoInventario.setTi_documento(SCARICO);
							rigoInventario.getBene().setTi_commerciale_istituzionale(riga.getDocumento_generico().getTi_istituz_commerc());
							dettagliRigheDocHash.put(riga,dettagliInventarioPerRigaFatturaColl);
							setDettagliRigheDocHash(dettagliRigheDocHash);
						}	
						else{
							setDettagliFatturaColl(new BulkList(dettagliDaInventariare));
							if (dettagliDaInventariare.get(0).getClass()==Fattura_attiva_rigaIBulk.class) 
									rigoFattura_at =(Fattura_attiva_rigaBulk)i.next();
							else if (dettagliDaInventariare.get(0).getClass()==Nota_di_credito_rigaBulk.class)
								    rigonc =(Nota_di_credito_rigaBulk)i.next();
							
							dettagliInventarioPerRigaFatturaColl = new BulkList();
							rigoInventario = new Buono_carico_scarico_dettBulk();
							bene = new Inventario_beniBulk();
							rigoInventario.setCrudStatus(TO_BE_CREATED);
							bene.setCrudStatus(TO_BE_CREATED);
							rigoInventario.setBuono_cs(this);
							if (rigoFattura_at!=null)
								rigoInventario.setQuantita(new Long(rigoFattura_at.getQuantita().longValue()));
							else
								rigoInventario.setQuantita(new Long(rigonc.getQuantita().longValue()));
							rigoInventario.setBene(bene);
							if (rigoFattura_at!=null)
								rigoInventario.setValore_unitario(rigoFattura_at.getIm_imponibile());
							else
								rigoInventario.setValore_unitario(rigonc.getIm_imponibile());
											
							rigoInventario.setTi_documento(SCARICO);
							//rigoInventario.getBene().setTi_commerciale_istituzionale(Fattura_passiva_rigaBulk.COMMERCIALE);
							
							if (rigoFattura_at!=null)
								dettagliRigheHash.put(rigoFattura_at,dettagliInventarioPerRigaFatturaColl);
							else
								dettagliRigheHash.put(rigonc,dettagliInventarioPerRigaFatturaColl);
							
						
							setDettagliRigheHash(dettagliRigheHash);
						}
					}
				}
			}
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (11/20/2001 11.07.07)
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getDettagliFatturaColl() {
		return dettagliFatturaColl;
	}
	public Fattura_passiva_rigaBulk removeFromDettagliFatturaColl( int indiceDiLinea ) {

		Fattura_passiva_rigaBulk element = (Fattura_passiva_rigaBulk)dettagliFatturaColl.get(indiceDiLinea);

		return (Fattura_passiva_rigaBulk)dettagliFatturaColl.remove(indiceDiLinea);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/11/2001 14.41.50)
	 * @param newFattura_attiva_dettColl it.cnr.jada.bulk.BulkList
	 */
	public void setDettagliFatturaColl(it.cnr.jada.bulk.BulkList newDettagliFatturaColl) {
		dettagliFatturaColl = newDettagliFatturaColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/22/2001 3:56:54 PM)
	 * @return java.lang.String
	 */
	public boolean hasDettagli() {
		return getBuono_carico_scarico_dettColl().size()>0;
	}
	public boolean includesBene(Inventario_beniBulk bene) {

	return (getDettaglioScaricoPerBene(bene)!=null);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/01/2002 16.52.40)
	 * @return boolean
	 */
	public Buono_carico_scarico_dettBulk getDettaglioScaricoPerBene(Inventario_beniBulk bene) {

		java.util.List beni = new it.cnr.jada.util.Collect(getBuono_carico_scarico_dettColl(),"bene");
		for (int i = 0;i<beni.size();i++){
			Inventario_beniBulk unBene = (Inventario_beniBulk)beni.get(i);
			if (unBene.getNumeroBeneCompleto().equals(bene.getNumeroBeneCompleto()))
				return (Buono_carico_scarico_dettBulk)getBuono_carico_scarico_dettColl().get(i);
		}
		return null;
	}
	public String getLocal_transactionID() {
		return local_transactionID;
	}
	public void setLocal_transactionID(String local_transactionID) {
		this.local_transactionID = local_transactionID;
	}
	public void sostituisciDettagli_Inventario_Per_Righe_Fattura(Buono_carico_scarico_dettBulk dettaglio1, BulkList dettagliSostituti){

		PrimaryKeyHashtable righe_Fattura = getDettagliRigheHash();
		for (java.util.Enumeration e = righe_Fattura.keys(); e.hasMoreElements();){
				
			Fattura_passiva_rigaBulk riga_fattura = (Fattura_passiva_rigaBulk)e.nextElement();
			BulkList dettagli_associati = (BulkList)righe_Fattura.get(riga_fattura);
			if (dettagli_associati.containsByPrimaryKey(dettaglio1)){
				for (java.util.Iterator i = dettagliSostituti.iterator(); i.hasNext();){
					
					dettagli_associati.add(i.next());
				}
				dettagli_associati.remove(dettaglio1);
				righe_Fattura.put(riga_fattura,dettagli_associati);
				break;
			}
		}

	}
	public Integer getCd_barre() {
		return cd_barre;
	}
	public void setCd_barre(Integer cd_barre) {
		this.cd_barre = cd_barre;
	}
	public boolean isByDocumento() {

		if (byDocumento == null)
			return false;
		return byDocumento.booleanValue();
	}
	public void setByDocumento(Boolean byDocumento) {
		this.byDocumento = byDocumento;
	}
	public int addToDettagliDocumentoColl (Documento_generico_rigaBulk nuovoRigo)
	{
		dettagliDocumentoColl.add(nuovoRigo);
		return dettagliDocumentoColl.size()-1;
	}
	public Documento_generico_rigaBulk removeFromDettagliDocumentoColl( int indiceDiLinea ) {

		Documento_generico_rigaBulk element = (Documento_generico_rigaBulk)dettagliDocumentoColl.get(indiceDiLinea);

		return (Documento_generico_rigaBulk)dettagliDocumentoColl.remove(indiceDiLinea);
	}
	public BulkList getDettagliDocumentoColl() {
		return dettagliDocumentoColl;
	}
	public void setDettagliDocumentoColl(BulkList dettagliDocumentoColl) {
		this.dettagliDocumentoColl = dettagliDocumentoColl;
	}
	public PrimaryKeyHashtable getDettagliRigheDocHash() {
		return dettagliRigheDocHash;
	}
	public void setDettagliRigheDocHash(PrimaryKeyHashtable dettagliRigheDocHash) {
		this.dettagliRigheDocHash = dettagliRigheDocHash;
	}
	public void addToDettagliRigheDocHash(
			Documento_generico_rigaBulk newriga,
			BulkList rigoInventarioColl) {

			if (dettagliRigheDocHash == null)
				dettagliRigheDocHash = new PrimaryKeyHashtable();
		
			dettagliRigheDocHash.put(newriga,rigoInventarioColl);
		}
	public void removeFromDettagliRigheDocHash(
			Documento_generico_rigaBulk deleteRow) {
			dettagliRigheDocHash.remove(deleteRow);
		}
	public void setByDocumentoPerAumentoValore(Boolean boolean1) {
		setByDocumento(boolean1);
		byDocumentoPerAumentoValore = boolean1;
	}	
	public boolean isByDocumentoPerAumentoValore() {

		if (byDocumentoPerAumentoValore == null)
			return false;
		
		return byDocumentoPerAumentoValore.booleanValue();
	}
	public void sostituisciDettagli_Inventario_Per_Righe_Documento(Buono_carico_scarico_dettBulk tmpDettaglio, BulkList dettagliPerRigaDiDoc) {
		
		PrimaryKeyHashtable righe_Doc = getDettagliRigheDocHash();
		for (java.util.Enumeration e = righe_Doc.keys(); e.hasMoreElements();){
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)e.nextElement();
			BulkList dettagli_associati = (BulkList)righe_Doc.get(riga);
			if (dettagli_associati.containsByPrimaryKey(tmpDettaglio)){
				for (java.util.Iterator i = dettagliPerRigaDiDoc.iterator(); i.hasNext();){
					dettagli_associati.add(i.next());
				}
				dettagli_associati.remove(tmpDettaglio);
				righe_Doc.put(riga,dettagli_associati);
				break;
			}
		}	
	}
	
	public boolean isPerVendita() {

		if (perVendita == null)
			return false;
		return perVendita.booleanValue();
	}
	public void setPerVendita(Boolean perVendita) {
		this.perVendita = perVendita;
	}
	public boolean isTemporaneo() {
		return 	getPg_buono_c_s() == null || getPg_buono_c_s().compareTo(new Long("0")) <= 0;
	}

}