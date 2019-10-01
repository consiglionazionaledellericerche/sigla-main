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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.bulk.*;

public class Ass_inv_bene_fatturaBulk extends Ass_inv_bene_fatturaBase {
//	 Valore incrementale nelle associazioni per aumento di valore, proveninte da Fattura Passiva
	private java.math.BigDecimal valore_unitario;

	//Collezione contentente le righe di dettaglio della Fattura Passiva
	private BulkList dettagliFatturaColl = new BulkList();

	//Collezione contentente le righe di dettaglio del Documento
	private BulkList dettagliDocumentoColl = new BulkList();

	// Collezione di tutti i buoni selezionati
	private BulkList buoniColl = new BulkList();

	/* ID di transazione: è univoco e permette di identificare la fattura sulla
	 *	tabella INVENTARIO_BENI_APG (tabella di appoggio).
	*/ 
	private String local_transactionID;

	// Indica che l'associazione è PER AUMENTO VALORE
	private Boolean perAumentoValore = new Boolean(false);

	protected Boolean fl_bene_accessorio;

	// HashTable di rihge fattura
	private PrimaryKeyHashtable dettagliRigheHash;
	
	private PrimaryKeyHashtable dettagliRigheDocHash;

	// Riga di Fattura Attiva
	private Fattura_attiva_rigaIBulk riga_fatt_att;
	
	// Riga di Fattura Passiva
	private Fattura_passiva_rigaIBulk riga_fatt_pass;
	
	//	Buono collegato
	private it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk test_buono;
	
	protected final static Boolean TRUE = new java.lang.Boolean (true) ;
	
	protected final static Boolean FALSE = new java.lang.Boolean (false) ;
		
	private Id_inventarioBulk inventario;	
	
	private Documento_generico_rigaBulk riga_doc_gen; 
//	 Indica che l'associazione è PER AUMENTO VALORE
	private Boolean perAumentoValoreDoc = new Boolean(false);
	
public Ass_inv_bene_fatturaBulk() {
	super();
}
public Ass_inv_bene_fatturaBulk(java.lang.Long pg_riga) {
	super(pg_riga);
}
public int addToBuoniColl (it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk nuovoBuono)
{

	buoniColl.add(nuovoBuono);
	return buoniColl.size()-1;
}
public int addToDettagliFatturaColl (Fattura_passiva_rigaBulk nuovoRigo)
{

	dettagliFatturaColl.add(nuovoRigo);
	return dettagliFatturaColl.size()-1;
}

/**
 *Insert the method's description here.
 *
**/ 
public void addToDettagliRigheHash(
	Fattura_passiva_rigaBulk newriga_fattura,
	BulkList rigoInventarioColl) {

	if (dettagliRigheHash == null)
		dettagliRigheHash = new PrimaryKeyHashtable();
		
	dettagliRigheHash.put(newriga_fattura,rigoInventarioColl);
}

public void completeFrom(java.util.List righe_fattura) {	
		
	if (righe_fattura != null){
		
		/************** QUESTA PARTE DOVRA' ESSERE CANCELLATA QUANDO CI SARA' IL PG_FATTURA *************/
		BulkList aggiungi_pgFattura = new BulkList();
		if (righe_fattura.size()!=0 && righe_fattura.get(0).getClass().equals(Nota_di_credito_rigaBulk.class))
		{
			for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){
				Nota_di_credito_rigaBulk riga_fattura = (Nota_di_credito_rigaBulk)i.next();
				
				if (riga_fattura.getFattura_passiva().getPg_fattura_passiva()!=null)
					riga_fattura.setPg_fattura_passiva(riga_fattura.getFattura_passiva().getPg_fattura_passiva());
				else
					riga_fattura.setPg_fattura_passiva(new Long(1));	
					aggiungi_pgFattura.add(riga_fattura);
			}			
			setDettagliFatturaColl(aggiungi_pgFattura);
		}	
		if (righe_fattura.size()!=0 && righe_fattura.get(0).getClass().equals(Nota_di_debito_rigaBulk.class))
		{
			for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){
				Nota_di_debito_rigaBulk riga_fattura = (Nota_di_debito_rigaBulk)i.next();
				
				if (riga_fattura.getFattura_passiva().getPg_fattura_passiva()!=null)
					riga_fattura.setPg_fattura_passiva(riga_fattura.getFattura_passiva().getPg_fattura_passiva());
				else
					riga_fattura.setPg_fattura_passiva(new Long(1));	
					aggiungi_pgFattura.add(riga_fattura);
			}			
			setDettagliFatturaColl(aggiungi_pgFattura);
		}	
		else if (righe_fattura.size()!=0 && righe_fattura.get(0).getClass().equals(Fattura_passiva_rigaIBulk.class)){
			for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){
				Fattura_passiva_rigaIBulk riga_fattura = (Fattura_passiva_rigaIBulk)i.next();
				
				if (riga_fattura.getFattura_passiva().getPg_fattura_passiva()!=null)
					riga_fattura.setPg_fattura_passiva(riga_fattura.getFattura_passiva().getPg_fattura_passiva());
				else
					riga_fattura.setPg_fattura_passiva(new Long(-1));	
				aggiungi_pgFattura.add(riga_fattura);
			}			
			setDettagliFatturaColl(aggiungi_pgFattura);
		}
		else if (righe_fattura.size()!=0 && righe_fattura.get(0).getClass().equals(Fattura_attiva_rigaIBulk.class)){
			for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){
				Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)i.next();
				
				if (riga_fattura.getFattura_attiva().getPg_fattura_attiva()!=null)
					riga_fattura.setPg_fattura_attiva(riga_fattura.getFattura_attiva().getPg_fattura_attiva());
				else
					riga_fattura.setPg_fattura_attiva(new Long(-1));	
				aggiungi_pgFattura.add(riga_fattura);
			}			
			setDettagliFatturaColl(aggiungi_pgFattura);
		
	}else if (righe_fattura.size()!=0 && righe_fattura.get(0).getClass().equals(Documento_generico_rigaBulk.class)){
		for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
			
			if (riga.getDocumento_generico().getPg_documento_generico()!=null)
				riga.setPg_documento_generico(riga.getDocumento_generico().getPg_documento_generico());
			else
				riga.setPg_documento_generico(new Long(-1));	
			aggiungi_pgFattura.add(riga);
		}			
		setDettagliDocumentoColl(aggiungi_pgFattura);
	}
	}
}
public boolean isTemporaneo() {

	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 11.07.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getBuoniColl() {
	return buoniColl;
}

public java.lang.String getCd_cds_fatt_att() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fatt_att = this.getRiga_fatt_att();
	if (riga_fatt_att == null)
		return null;
	if(riga_fatt_att.getFattura_attiva()==null)
		  return null;
	return riga_fatt_att.getFattura_attiva().getCd_cds();
}
public java.lang.String getCd_cds_fatt_pass() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fatt_pass = this.getRiga_fatt_pass();
	if (riga_fatt_pass == null)
		return null;
	if(riga_fatt_pass.getFattura_passiva()==null)
		  return null;
	return riga_fatt_pass.getFattura_passiva().getCd_cds();
}
public java.lang.String getCd_uo_fatt_att() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fatt_att = this.getRiga_fatt_att();
	if (riga_fatt_att == null)
		return null;
	if(riga_fatt_att.getFattura_attiva()==null)
		  return null;
	return riga_fatt_att.getFattura_attiva().getCd_unita_organizzativa();
}
public java.lang.String getCd_uo_fatt_pass() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fatt_pass = this.getRiga_fatt_pass();
	if (riga_fatt_pass == null)
		return null;
	if(riga_fatt_pass.getFattura_passiva()==null)
		  return null;
	return riga_fatt_pass.getFattura_passiva().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 11.07.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getDettagliFatturaColl() {
	return dettagliFatturaColl;
}
/**
 * Insert the method's description here.
 * Creation date: (12/19/2001 2:48:31 PM)
 * @return java.util.Hashtable
 */
public PrimaryKeyHashtable getDettagliRigheHash() {
 	
	return dettagliRigheHash;
}
public java.lang.Integer getEsercizio_fatt_att() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fatt_att = this.getRiga_fatt_att();
	if (riga_fatt_att == null)
		return null;
	if(riga_fatt_att.getFattura_attiva()==null)
		  return null;
	return riga_fatt_att.getFattura_attiva().getEsercizio();
}
public java.lang.Integer getEsercizio_fatt_pass() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fatt_pass = this.getRiga_fatt_pass();
	if (riga_fatt_pass == null)
		return null;
	if(riga_fatt_pass.getFattura_passiva()==null)
		  return null;
	return riga_fatt_pass.getFattura_passiva().getEsercizio();
}

public java.lang.Long getPg_fattura_attiva() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fatt_att = this.getRiga_fatt_att();
	if (riga_fatt_att == null)
		return null;
	if(riga_fatt_att.getFattura_attiva()==null)
		  return null;
	return riga_fatt_att.getFattura_attiva().getPg_fattura_attiva();
}
public java.lang.Long getPg_fattura_passiva() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fatt_pass = this.getRiga_fatt_pass();
	if (riga_fatt_pass == null)
		return null;
	if(riga_fatt_pass.getFattura_passiva()==null)
		  return null;
	return riga_fatt_pass.getFattura_passiva().getPg_fattura_passiva();
}
public java.lang.Long getPg_inventario() {
		if (getTest_buono() == null)
			return null;
		if (getTest_buono().getInventario() == null)
			return null;
		return getTest_buono().getPg_inventario();
}

public java.lang.Long getProgressivo_riga_fatt_att() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fatt_att = this.getRiga_fatt_att();
	if (riga_fatt_att == null)
		return null;
	return riga_fatt_att.getProgressivo_riga();
}
public java.lang.Long getProgressivo_riga_fatt_pass() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fatt_pass = this.getRiga_fatt_pass();
	if (riga_fatt_pass == null)
		return null;
	return riga_fatt_pass.getProgressivo_riga();
}
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 11.01.03)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk
 */
public it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk getRiga_fatt_att() {
	return riga_fatt_att;
}
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 11.01.03)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk
 */
public it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk getRiga_fatt_pass() {
	return riga_fatt_pass;
}

public it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk removeFromBuoniColl( int indiceDiLinea ) {

	it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk element = (it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk)buoniColl.get(indiceDiLinea);

	return (it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk)buoniColl.remove(indiceDiLinea);
}
public Fattura_passiva_rigaBulk removeFromDettagliFatturaColl( int indiceDiLinea ) {

	Fattura_passiva_rigaBulk element = (Fattura_passiva_rigaBulk)dettagliFatturaColl.get(indiceDiLinea);

	return (Fattura_passiva_rigaBulk)dettagliFatturaColl.remove(indiceDiLinea);
}

public void removeFromDettagliRigheHash(Fattura_passiva_rigaBulk deleteRow) {
		
	dettagliRigheHash.remove(deleteRow);
}


/**
 * Insert the method's description here.
 * Creation date: (20/11/2001 14.41.50)
 * @param newFattura_attiva_dettColl it.cnr.jada.bulk.BulkList
 */
public void setBuoniColl(it.cnr.jada.bulk.BulkList newBeniColl) {
	buoniColl = newBeniColl;
}

public void setCd_cds_fatt_att(java.lang.String cd_cds_fatt_att) {
	if (getRiga_fatt_att()!=null)
	this.getRiga_fatt_att().getFattura_attiva().setCd_cds(cd_cds_fatt_att);
}
public void setCd_cds_fatt_pass(java.lang.String cd_cds_fatt_pass) {
	if (getRiga_fatt_pass()!=null)
	this.getRiga_fatt_pass().getFattura_passiva().setCd_cds(cd_cds_fatt_pass);
}
public void setCd_uo_fatt_att(java.lang.String cd_uo_fatt_att) {
	if (getRiga_fatt_att()!=null)
	this.getRiga_fatt_att().getFattura_attiva().setCd_unita_organizzativa(cd_uo_fatt_att);
}
public void setCd_uo_fatt_pass(java.lang.String cd_uo_fatt_pass) {
	if (getRiga_fatt_pass()!=null)
	this.getRiga_fatt_pass().getFattura_passiva().setCd_unita_organizzativa(cd_uo_fatt_pass);
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
 * Creation date: (12/19/2001 2:48:31 PM)
 * @param newDettagliRigheHash java.util.Hashtable
 */
public void setDettagliRigheHash(PrimaryKeyHashtable newDettagliRigheHash) {
	dettagliRigheHash = newDettagliRigheHash;
}
public void setEsercizio_fatt_att(java.lang.Integer esercizio_fatt_att) {
	if (getRiga_fatt_att()!=null)
		this.getRiga_fatt_att().getFattura_attiva().setEsercizio(esercizio_fatt_att);
}
public void setEsercizio_fatt_pass(java.lang.Integer esercizio_fatt_pass) {
	if (getRiga_fatt_pass()!=null)
	this.getRiga_fatt_pass().getFattura_passiva().setEsercizio(esercizio_fatt_pass);
}

public void setPg_fattura_attiva(java.lang.Long pg_fattura_attiva) {
	if (getRiga_fatt_att()!=null)
	this.getRiga_fatt_att().getFattura_attiva().setPg_fattura_attiva(pg_fattura_attiva);
}
public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
	if (getRiga_fatt_pass()!=null)
	this.getRiga_fatt_pass().getFattura_passiva().setPg_fattura_passiva(pg_fattura_passiva);
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getTest_buono().getInventario().setPg_inventario(pg_inventario);
}
public String getTi_documento() {	
	if (getTest_buono()==null)
		return null;
	return getTest_buono().getTi_documento();
}
public Long getPg_buono_c_s() {
	if (getTest_buono()==null)
	  return null;
	return getTest_buono().getPg_buono_c_s();
}
public Integer getEsercizio() {
	
	if (getTest_buono()==null)
	  return null;
	return getTest_buono().getEsercizio();
}
public void setEsercizio(Integer integer) {
	this.getTest_buono().setEsercizio(integer);
}
public void setPg_buono_c_s(Long long1) {
	this.getTest_buono().setPg_buono_c_s(long1);
}
public void setTi_documento(String string) {
	this.getTest_buono().setTi_documento(string);
}

public void setProgressivo_riga_fatt_att(java.lang.Long progressivo_riga_fatt_att) {
	if (getRiga_fatt_att()!=null)
		this.getRiga_fatt_att().setProgressivo_riga(progressivo_riga_fatt_att);
}
public void setProgressivo_riga_fatt_pass(java.lang.Long progressivo_riga_fatt_pass) {
	if (getRiga_fatt_pass()!=null)
	this.getRiga_fatt_pass().setProgressivo_riga(progressivo_riga_fatt_pass);
}
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 11.01.03)
 * @param newRiga_fatt_att it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk
 */
public void setRiga_fatt_att(it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk newRiga_fatt_att) {
	riga_fatt_att = newRiga_fatt_att;
}
/**
 * Insert the method's description here.
 * Creation date: (27/02/2002 11.01.03)
 * @param newRiga_fatt_pass it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk
 */
public void setRiga_fatt_pass(it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk newRiga_fatt_pass) {
	riga_fatt_pass = newRiga_fatt_pass;
}

public Id_inventarioBulk getInventario() {
	return inventario;
}

public void setInventario(Id_inventarioBulk bulk) {
	inventario = bulk;
}
public String getLocal_transactionID() {
	return local_transactionID;
}
public void setLocal_transactionID(String local_transactionID) {
	this.local_transactionID = local_transactionID;
}
public boolean isBeneAccessorio() {
	
	if (fl_bene_accessorio!=null){
		return fl_bene_accessorio.booleanValue();
	}
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2004 11.53.39)
 * @return java.lang.Boolean
 */
public boolean isPerAumentoValore() {

	if (perAumentoValore == null)
		return false;
		
	return perAumentoValore.booleanValue();
}
public void setPerAumentoValore(java.lang.Boolean newPerAumentoValore) {
	perAumentoValore = newPerAumentoValore;
}
public it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk getTest_buono() {
	return test_buono;
}
public void setTest_buono(
		it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk test_buono) {
	this.test_buono = test_buono;
}
public java.math.BigDecimal getValore_unitario() {
	return valore_unitario;
}
public void setValore_unitario(java.math.BigDecimal valore_unitario) {
	this.valore_unitario = valore_unitario;
}
public Documento_generico_rigaBulk getRiga_doc_gen() {
	return riga_doc_gen;
}
public void setRiga_doc_gen(Documento_generico_rigaBulk riga_doc_gen) {
	this.riga_doc_gen = riga_doc_gen;
}
public java.lang.String getCd_cds_doc_gen() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	if(riga_doc_gen.getDocumento_generico()==null)
		  return null;
	return riga_doc_gen.getDocumento_generico().getCd_cds();
}
public void setCd_cds_doc_gen(java.lang.String cd_cds_doc_gen) {
	if ((getRiga_doc_gen()!=null)&& (getRiga_doc_gen().getDocumento_generico()!=null) )
		this.getRiga_doc_gen().getDocumento_generico().setCd_cds(cd_cds_doc_gen);
}
public java.lang.String getCd_tipo_documento_amm() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	if(riga_doc_gen.getDocumento_generico()==null)
		  return null;
	if(riga_doc_gen.getDocumento_generico().getTipo_documento()==null)
		  return null;
	return riga_doc_gen.getDocumento_generico().getTipo_documento().getCd_tipo_documento_amm();
}
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	if ((getRiga_doc_gen()!=null)&& (getRiga_doc_gen().getDocumento_generico()!=null) &&
			(getRiga_doc_gen().getDocumento_generico().getTipo_documento()!=null))
		this.getRiga_doc_gen().getDocumento_generico().getTipo_documento().setCd_tipo_documento_amm(cd_tipo_documento_amm);
}
public java.lang.String getCd_uo_doc_gen() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	if(riga_doc_gen.getDocumento_generico()==null)
		  return null;
	return riga_doc_gen.getDocumento_generico().getCd_unita_organizzativa();
}
public void setCd_uo_doc_gen(java.lang.String cd_uo_doc_gen) {
	if ((getRiga_doc_gen()!=null)&& (getRiga_doc_gen().getDocumento_generico()!=null) )
		this.getRiga_doc_gen().getDocumento_generico().setCd_unita_organizzativa(cd_uo_doc_gen);
}
public java.lang.Integer getEsercizio_doc_gen() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	if(riga_doc_gen.getDocumento_generico()==null)
		  return null;
	return riga_doc_gen.getDocumento_generico().getEsercizio();
}
public void setEsercizio_doc_gen(java.lang.Integer esercizio_doc_gen) {
	if ((getRiga_doc_gen()!=null)&& (getRiga_doc_gen().getDocumento_generico()!=null) )
		this.getRiga_doc_gen().getDocumento_generico().setEsercizio(esercizio_doc_gen);
}
public java.lang.Long getPg_documento_generico() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	if(riga_doc_gen.getDocumento_generico()==null)
		  return null;
	return riga_doc_gen.getDocumento_generico().getPg_documento_generico();
}
public void setPg_documento_generico(java.lang.Long pg_documento_generico) {
	if ((getRiga_doc_gen()!=null)&& (getRiga_doc_gen().getDocumento_generico()!=null) )
		this.getRiga_doc_gen().getDocumento_generico().setPg_documento_generico(pg_documento_generico);
}
public Long getProgressivo_riga_doc_gen() {
	Documento_generico_rigaBulk riga_doc_gen = this.getRiga_doc_gen();
	if (riga_doc_gen == null)
		return null;
	return riga_doc_gen.getProgressivo_riga();
}
public void setProgressivo_riga_doc_gen(Long progressivo_riga_doc_gen) {
	if (getRiga_doc_gen()!=null)
		this.getRiga_doc_gen().setProgressivo_riga(progressivo_riga_doc_gen);
}
public boolean isPerAumentoValoreDoc() {
	return perAumentoValoreDoc;
}
public void setPerAumentoValoreDoc(boolean perAumentoValoreDoc) {
	this.perAumentoValoreDoc = perAumentoValoreDoc;
}
public int addToDettagliDocumentoColl (Documento_generico_rigaBulk nuovoRigo)
{
	dettagliDocumentoColl.add(nuovoRigo);
	return dettagliDocumentoColl.size()-1;
}
public BulkList getDettagliDocumentoColl() {
	return dettagliDocumentoColl;
}
public void setDettagliDocumentoColl(BulkList dettagliDocumentoColl) {
	this.dettagliDocumentoColl = dettagliDocumentoColl;
}
public Documento_generico_rigaBulk removeFromDettagliDocumentoColl( int indiceDiLinea ) {

	Documento_generico_rigaBulk element = (Documento_generico_rigaBulk)dettagliDocumentoColl.get(indiceDiLinea);

	return (Documento_generico_rigaBulk)dettagliDocumentoColl.remove(indiceDiLinea);
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
}
