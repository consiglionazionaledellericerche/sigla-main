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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;

import java.sql.Timestamp;
import java.util.*;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.*;

public class Documento_genericoBulk extends Documento_genericoBase implements IDocumentoAmministrativoSpesaBulk, Voidable, IDefferUpdateSaldi {
	protected BulkList documento_generico_dettColl= new BulkList();
	private java.util.Vector dettagliCancellati= new Vector();
	private int num_dettColl= 0;
	protected DivisaBulk valuta;
	private java.util.Collection valute;
	private java.sql.Timestamp fine_validita_valuta;
	private java.sql.Timestamp inizio_validita_valuta;
	private char changeOperation= MOLTIPLICA;
	public static final char DIVISIONE= '/';
	public static final char MOLTIPLICA= '*';
	private java.util.Vector documentiContabiliCancellati= new Vector();
	public final static String STATO_PAGATO= "P";
	public final static String STATO_INIZIALE= "I";
	public final static String STATO_PARZIALE= "Q";
	public final static String STATO_ANNULLATO= "A";
	public final static String STATO_CONTABILIZZATO= "C";
	public final static String GENERICO_E= Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E;
	public final static String GENERICO_S= Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S;
	public final static Dictionary STATO;
	public final static String ASSOCIATO_A_MANDATO= "T";
	public final static String NON_ASSOCIATO_A_MANDATO= "N";
	public final static String PARZIALMENTE_ASSOCIATO_A_MANDATO= "P";
	public final static java.util.Dictionary STATO_MANDATO;
	public final static String NO_FONDO_ECO = "N";
	public final static String FONDO_ECO = "A";
	public final static String REGISTRATO_IN_FONDO_ECO = "R";
	private Tipo_documento_genericoBulk tipoDocumentoGenerico;

	private ObbligazioniTable documento_generico_obbligazioniHash= null;
	private AccertamentiTable documento_generico_accertamentiHash= null;
	private java.math.BigDecimal importoTotalePerObbligazione= new java.math.BigDecimal(0);
	private java.math.BigDecimal importoTotalePerAccertamento= new java.math.BigDecimal(0);
	private boolean flagEnte;
	private String uo_CNR;
	private String cds_CNR;
	private char ti_entrate_spese;
	private boolean passivo_ente= false;
	public final static char ENTRATE= 'E';
	public final static char SPESE= 'S';
	public final static Dictionary flagEnteKeys;
	public final static Dictionary entrate_speseKeys;	
	protected TerzoBulk terzo_uo_cds;
	protected TerzoBulk terzo_spesa;
	private PrimaryKeyHashMap deferredSaldi= new PrimaryKeyHashMap();
	
	public final static String NON_REGISTRATO_IN_COGE= "N";
	public final static String REGISTRATO_IN_COGE= "C";
	public final static String DA_RIREGISTRARE_IN_COGE= "R";
	public final static String DA_NON_REGISTRARE_IN_COGE= "X";

	public final static String NON_CONTABILIZZATO_IN_COAN = "N";
	public final static String CONTABILIZZATO_IN_COAN = "C";
	public final static String DA_RICONTABILIZZARE_IN_COAN = "R";
	public final static String DA_NON_REGISTRARE_IN_COAN= "X";

	public final static Dictionary TIPO;
	public final static Dictionary STATO_COGE;
	public final static Dictionary STATO_FONDO_ECO;
	public final static Dictionary STATO_COAN;
	public final static Dictionary STATI_RIPORTO;

	private java.util.Collection tipi_doc;
	private java.util.Collection tipi_doc_for_search;
	private Tipo_documento_ammBulk tipo_documento;

	protected boolean defaultValuta = false;
	private java.lang.String riportata = NON_RIPORTATO;

	private java.lang.String riportataInScrivania = NON_RIPORTATO;
	private Integer esercizioInScrivania;
	private java.sql.Timestamp dataInizioObbligoRegistroUnico;

	/*
	 * Le variabili isDetailDoubled e isDocumentoModificabile servono per gestire il caso in cui l'utente
	 * non potendo modificare il documento procede solo a sdoppiare la riga di dettaglio. In tal caso la 
	 * procedura provvede a non rieffettuare la ricontabilizzazione in COAN e COGE.
	 *    
	 */
	private boolean isDetailDoubled = false; //serve per sapere se è stata sdoppiata una riga di dettaglio 
	private boolean isDocumentoModificabile = true; //serve per sapere se il documento è modificabile o meno

	private Lettera_pagam_esteroBulk lettera_pagamento_estero = null;
	public final static Dictionary STATO_LIQUIDAZIONE;
	public final static Dictionary CAUSALE;


	static {
		TIPO = new it.cnr.jada.util.OrderedHashtable();
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			TIPO.put(tipoIVA.value(), tipoIVA.label());
		}

		STATO= new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE, "Iniziale");
		STATO.put(STATO_CONTABILIZZATO, "Contabilizzato");
		STATO.put(STATO_PARZIALE, "Parziale");
		STATO.put(STATO_PAGATO, "Pagato");
		STATO.put(STATO_ANNULLATO, "Annullato");

		flagEnteKeys= new it.cnr.jada.util.OrderedHashtable();
		flagEnteKeys.put(new Boolean(false), "N");
		flagEnteKeys.put(new Boolean(true), "Y");

		entrate_speseKeys= new it.cnr.jada.util.OrderedHashtable();
		entrate_speseKeys.put(String.valueOf(ENTRATE), "ENTRATE");
		entrate_speseKeys.put(String.valueOf(SPESE), "SPESE");

		STATO_MANDATO= new it.cnr.jada.util.OrderedHashtable();
		STATO_MANDATO.put(NON_ASSOCIATO_A_MANDATO, "Man/rev non associato");
		STATO_MANDATO.put(PARZIALMENTE_ASSOCIATO_A_MANDATO, "Parzialmente associato a man/rev");
		STATO_MANDATO.put(ASSOCIATO_A_MANDATO, "Man/rev associato");

		STATO_COGE = new it.cnr.jada.util.OrderedHashtable();
		STATO_COGE.put(NON_REGISTRATO_IN_COGE,"Non registrato");
		STATO_COGE.put(REGISTRATO_IN_COGE,"Registrato");
		STATO_COGE.put(DA_RIREGISTRARE_IN_COGE,"Da registrare nuovamente");
		STATO_COGE.put(DA_NON_REGISTRARE_IN_COGE,"Da non registrare");

		STATO_COAN = new it.cnr.jada.util.OrderedHashtable();
		STATO_COAN.put(NON_CONTABILIZZATO_IN_COAN,"Non contabilizzato");
		STATO_COAN.put(CONTABILIZZATO_IN_COAN,"Contabilizzato");
		STATO_COAN.put(DA_RICONTABILIZZARE_IN_COAN,"Da contabilizzare nuovamente");

		STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_RIPORTO.put(NON_RIPORTATO,"Non riportata");
		STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO,"Parzialmente riportata");
		STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO,"Completamente riportata");

		STATO_FONDO_ECO = new it.cnr.jada.util.OrderedHashtable();
		STATO_FONDO_ECO.put(NO_FONDO_ECO,"Non usare fondo economale");
		STATO_FONDO_ECO.put(FONDO_ECO,"Usa fondo economale");
		STATO_FONDO_ECO.put(REGISTRATO_IN_FONDO_ECO,"Registrato in fondo economale");

		STATO_LIQUIDAZIONE = new it.cnr.jada.util.OrderedHashtable();
		STATO_LIQUIDAZIONE.put(LIQ, "Liquidabile");
		STATO_LIQUIDAZIONE.put(NOLIQ, "Non Liquidabile");
		STATO_LIQUIDAZIONE.put(SOSP, "Liquidazione sospesa");

		CAUSALE= new it.cnr.jada.util.OrderedHashtable();
		CAUSALE.put(ATTLIQ,"In attesa di liquidazione");
		CAUSALE.put(CONT,"Contenzioso");
	}
	public final static java.util.Dictionary ti_bonifico_mezzoKeys = Lettera_pagam_esteroBulk.ti_bonifico_mezzoKeys, 
			ti_ammontare_debitoKeys = Lettera_pagam_esteroBulk.ti_ammontare_debitoKeys,
			ti_commissione_speseKeys = Lettera_pagam_esteroBulk.ti_commissione_speseKeys,
			ti_divisaKeys = Lettera_pagam_esteroBulk.ti_divisaKeys;
	
	private java.sql.Timestamp dt_termine_creazione_docamm = null;
	private CarichiInventarioTable carichiInventarioHash = null;
	private AssociazioniInventarioTable associazioniInventarioHash = null;
	private Boolean ha_beniColl;
	public Documento_genericoBulk() {
		super();
	}
	public Documento_genericoBulk(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_documento_generico) {
		super(cd_cds,cd_tipo_documento_amm,cd_unita_organizzativa,esercizio,pg_documento_generico);
		setTipo_documento(new it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk(cd_tipo_documento_amm));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
	public void addToDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont, java.util.Map values) {

		if (docCont != null) {
			if (deferredSaldi == null)
				deferredSaldi= new PrimaryKeyHashMap();
			if (!deferredSaldi.containsKey(docCont))
				deferredSaldi.put(docCont, values);
			else {
				Map firstValues= (Map) deferredSaldi.get(docCont);
				deferredSaldi.remove(docCont);
				deferredSaldi.put(docCont, firstValues);
			}
		}
	}
	public void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) {

		if (dettaglio != null && ((OggettoBulk)dettaglio).getCrudStatus() == OggettoBulk.NORMAL) {
			getDettagliCancellati().addElement(dettaglio);
			addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
		}
	}
	public void addToDocumentiContabiliCancellati(it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk dettaglio) {

		if (dettaglio != null && ((OggettoBulk)dettaglio).getCrudStatus() == OggettoBulk.NORMAL &&
				!BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio))
			getDocumentiContabiliCancellati().addElement(dettaglio);
	}

	public void addToDocumento_generico_accertamentiHash(
			it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento,
			Documento_generico_rigaBulk rigaDoc) {

		if (documento_generico_accertamentiHash == null)
			documento_generico_accertamentiHash = new AccertamentiTable();
		Vector righeAssociate = (Vector)documento_generico_accertamentiHash.get(accertamento);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			//documento_generico_accertamentiHash.put(accertamento, righeAssociate);
		}
		if (rigaDoc != null && !righeAssociate.contains(rigaDoc)) {
			righeAssociate.add(rigaDoc);
			//Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
			//e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
			if (documento_generico_accertamentiHash.containsKey(accertamento))
				documento_generico_accertamentiHash.remove(accertamento);
			//documento_generico_accertamentiHash.put(accertamento, righeAssociate);
		}
		documento_generico_accertamentiHash.put(accertamento, righeAssociate);

		if (getDocumentiContabiliCancellati() != null && 
				it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), accertamento))
			removeFromDocumentiContabiliCancellati(accertamento);
	}

	public int addToDocumento_generico_dettColl( Documento_generico_rigaBulk nuovoRigo ) 
	{


		nuovoRigo.setTi_associato_manrev(nuovoRigo.NON_ASSOCIATO_A_MANDATO);
		nuovoRigo.setTerzo(new TerzoBulk());
		if (getTi_entrate_spese()==ENTRATE){
			nuovoRigo.setTerzo_uo_cds(getTerzo_uo_cds());		
		}
		nuovoRigo.setDocumento_generico(this);

		try {
			java.sql.Timestamp ts = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			nuovoRigo.setDt_da_competenza_coge((getDt_da_competenza_coge() == null)?ts : getDt_da_competenza_coge());
			nuovoRigo.setDt_a_competenza_coge((getDt_a_competenza_coge() == null)?ts : getDt_a_competenza_coge());
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}	
		nuovoRigo.setStato_cofi(STATO_INIZIALE);
		long max = 0;
		for (Iterator i = documento_generico_dettColl.iterator(); i.hasNext();) {
			long prog = ((Documento_generico_rigaBulk)i.next()).getProgressivo_riga().longValue();
			if (prog > max) max = prog;
		}
		nuovoRigo.setProgressivo_riga(new Long(max+1));
		documento_generico_dettColl.add(nuovoRigo);
		return documento_generico_dettColl.size()-1;
	}
	public void addToDocumento_generico_obbligazioniHash(
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione,
			Documento_generico_rigaBulk rigaDoc) {

		if (documento_generico_obbligazioniHash == null)
			documento_generico_obbligazioniHash = new ObbligazioniTable();
		Vector righeAssociate = (Vector)documento_generico_obbligazioniHash.get(obbligazione);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			//documento_generico_obbligazioniHash.put(obbligazione, righeAssociate);
		}
		if (rigaDoc != null && !righeAssociate.contains(rigaDoc)) {
			righeAssociate.add(rigaDoc);
			//Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
			//e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
			if (documento_generico_obbligazioniHash.containsKey(obbligazione))
				documento_generico_obbligazioniHash.remove(obbligazione);
			//documento_generico_obbligazioniHash.put(obbligazione, righeAssociate);
		}
		documento_generico_obbligazioniHash.put(obbligazione, righeAssociate);

		if (getDocumentiContabiliCancellati() != null && 
				it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), obbligazione))
			removeFromDocumentiContabiliCancellati(obbligazione);
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean controllaCompatibilitaPer1210() {

		//controlla compatibilità dei clienti/fornitori x accertamenti/obbligazioni
		if (getDocumento_generico_dettColl() == null ||
				getDocumento_generico_dettColl().isEmpty() )
			return false;

		TerzoBulk terzo = null;
		Rif_modalita_pagamentoBulk modalita = null;
		int count = 0;
		for (java.util.Iterator i = getDocumento_generico_dettColl().iterator(); i.hasNext(); count++) {
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk) i.next();
			if (count == 0) {
				terzo = riga.getTerzo();
				modalita = riga.getModalita_pagamento();
			} else if (modalita == null || terzo == null || 
					riga.getTerzo() == null || riga.getModalita_pagamento() == null ||
					!terzo.equalsByPrimaryKey(riga.getTerzo()) ||
					!modalita.getTi_pagamento().equals(riga.getModalita_pagamento().getTi_pagamento()))
				return false;
		}
		return true;
	}
	public AccertamentiTable getAccertamentiHash() {

		return getDocumento_generico_accertamentiHash();
	}
	public BulkCollection[] getBulkLists() {

		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] { 
				documento_generico_dettColl				
		};
	}
	public java.lang.String getCd_divisa() {
		it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = this.getValuta();
		if (valuta == null)
			return null;
		return valuta.getCd_divisa();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public java.lang.String getCd_tipo_doc_amm() {
		return getCd_tipo_documento_amm();
	}
	public java.lang.String getCd_tipo_documento_amm() {
		it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk tipo_documento = this.getTipo_documento();
		if (tipo_documento == null)
			return null;
		return tipo_documento.getCd_tipo_documento_amm();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public java.lang.String getCd_uo() {
		return getCd_unita_organizzativa();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 16.09.10)
	 * @return java.lang.String
	 */
	public java.lang.String getCds_CNR() {
		return cds_CNR;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 13.24.17)
	 * @return char
	 */
	public char getChangeOperation() {
		return changeOperation;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/13/2002 9:59:40 AM)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public java.lang.Class getChildClass() {
		return null;
	}
	public List getChildren() {

		return getDocumento_generico_dettColl();
	}
	public static Calendar getDateCalendar(java.sql.Timestamp date) {

		if (date == null)
			try {
				date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}

		java.util.Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date(date.getTime()));
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		return calendar;
	}

	public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() {
		return deferredSaldi;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
	public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {

		if (docCont != null && deferredSaldi != null)
			for (Iterator i= deferredSaldi.keySet().iterator(); i.hasNext();) {
				IDocumentoContabileBulk key= (IDocumentoContabileBulk) i.next();
				if (((OggettoBulk) docCont).equalsByPrimaryKey((OggettoBulk) key))
					return key;
			}
		return null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getDescrizione_spesa() {
		return getDs_documento_generico();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/02/2002 16.41.41)
	 * @return java.util.Vector
	 */
	public java.util.Vector getDettagliCancellati() {
		return dettagliCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public java.util.Vector getDettagliNonContabilizzati() {

		Vector dettagliNonContabilizzati = new Vector();
		if (getDocumento_generico_dettColl() != null) {
			for (Iterator i = getDocumento_generico_dettColl().iterator(); i.hasNext();) {
				Documento_generico_rigaBulk dettaglio = (Documento_generico_rigaBulk)i.next();
				if (dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi()))
					dettagliNonContabilizzati.add(dettaglio);
			}
		}
		return dettagliNonContabilizzati;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public java.util.Vector getDettagliPagati() {

		Vector dettagliPagati = new Vector();
		if (getDocumento_generico_dettColl() != null) {
			for (Iterator i = getDocumento_generico_dettColl().iterator(); i.hasNext();) {
				Documento_generico_rigaBulk dettaglio = (Documento_generico_rigaBulk)i.next();
				if (dettaglio.STATO_PAGATO.equals(dettaglio.getStato_cofi()))
					dettagliPagati.add(dettaglio);
			}
		}
		return dettagliPagati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/02/2002 16.39.28)
	 * @return java.util.Vector
	 */
	public java.util.Vector getDocumentiContabiliCancellati() {
		return documentiContabiliCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 14.10.38)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public AccertamentiTable getDocumento_generico_accertamentiHash() {
		return documento_generico_accertamentiHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/09/2001 10.54.15)
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList<Documento_generico_rigaBulk> getDocumento_generico_dettColl() {
		return documento_generico_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 14.10.38)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public ObbligazioniTable getDocumento_generico_obbligazioniHash() {
		return documento_generico_obbligazioniHash;
	}
	/**
	 * getDocumentoAmministrativoClassForDelete method comment.
	 */
	public java.lang.Class getDocumentoAmministrativoClassForDelete() {
		return null;
	}
	/**
	 * getDocumentoContabileClassForDelete method comment.
	 */
	public java.lang.Class getDocumentoContabileClassForDelete() {
		return null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/07/2003 11.04.01)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getDt_termine_creazione_docamm() {
		return dt_termine_creazione_docamm;
	}
	public java.lang.Integer getEsercizio_lettera() {
		it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera_pagamento_estero = this.getLettera_pagamento_estero();
		if (lettera_pagamento_estero == null)
			return null;
		return lettera_pagamento_estero.getEsercizio();
	}
	/**
	 * Creation date: (08/11/2004 13.34.04)
	 * @param newEsercizioInScrivania java.lang.Integer
	 */
	public void setEsercizioInScrivania(java.lang.Integer newEsercizioInScrivania) {
		esercizioInScrivania = newEsercizioInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Insert the method's description here.
	 * Creation date: (08/11/2004 13.34.04)
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEsercizioInScrivania() {
		return esercizioInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getFine_validita_valuta() {
		return fine_validita_valuta; 
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImporto_netto_spesa() {
		return getImporto_spesa();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImporto_spesa() {
		return getIm_totale();
	}
	public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

		if (importo == null) return null;
		return importo.negate();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 16.04.00)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImportoTotalePerAccertamento() {
		return importoTotalePerAccertamento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 16.04.00)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImportoTotalePerObbligazione() {
		return importoTotalePerObbligazione;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getInizio_validita_valuta() {
		return inizio_validita_valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (09/07/2002 16.29.09)
	 * @author: CNRADM
	 * @return it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk
	 */
	public Lettera_pagam_esteroBulk getLettera_pagamento_estero() {
		return lettera_pagamento_estero;
	}
	public String getManagerName() {

		if (Documento_genericoBulk.GENERICO_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				Numerazione_doc_ammBulk.TIPO_TRASF_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				Numerazione_doc_ammBulk.TIPO_REGOLA_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				Numerazione_doc_ammBulk.TIPO_GEN_IVA_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				"GEN_CORA_E".equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				"GEN_CORV_E".equalsIgnoreCase(getCd_tipo_documento_amm()) ||
				"GEN_RC_DAT".equalsIgnoreCase(getCd_tipo_documento_amm()))
			return "CRUDGenericoAttivoBP";
		return "CRUDGenericoPassivoBP";
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/22/2002 2:36:40 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public java.lang.String getManagerOptions() {
		return "VTh";
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/10/2001 14.27.01)
	 * @return int
	 */
	public int getNum_dettColl() {
		return num_dettColl;
	}
	public ObbligazioniTable getObbligazioniHash() {

		return getDocumento_generico_obbligazioniHash();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public java.lang.Long getPg_doc_amm() {
		return getPg_documento_generico();
	}
	public java.lang.Long getPg_lettera() {
		it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera_pagamento_estero = this.getLettera_pagamento_estero();
		if (lettera_pagamento_estero == null)
			return null;
		return lettera_pagamento_estero.getPg_lettera();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/05/2003 15.55.11)
	 * @return java.lang.String
	 */
	public java.lang.String getRiportata() {
		return riportata;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (02/11/2004 14.12.35)
	 * @return java.lang.String
	 */
	public java.lang.String getRiportataInScrivania() {
		return riportataInScrivania;
	}
	/* 
	 * Getter dell'attributo riportata
	 */
	public Dictionary getRiportataKeys() {
		return STATI_RIPORTO;
	}
	public Dictionary getStato_cofiKeys() {
		return STATO;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di documento.
	 *
	 * @return java.util.Dictionary
	 */

	public Dictionary getStato_cofiKeysForSearch() {

		it.cnr.jada.util.OrderedHashtable d = (it.cnr.jada.util.OrderedHashtable)getStato_cofiKeys();
		if (d == null) return null;

		it.cnr.jada.util.OrderedHashtable clone = (it.cnr.jada.util.OrderedHashtable)d.clone();
		clone.remove(STATO_INIZIALE);
		return clone;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di documento.
	 *
	 * @return java.util.Dictionary
	 */

	public Dictionary getStato_pagamento_fondo_ecoKeys() {

		if (getStato_pagamento_fondo_eco() != null &&
				REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco())) {
			return STATO_FONDO_ECO;
		}

		OrderedHashtable oh = (OrderedHashtable)((OrderedHashtable)STATO_FONDO_ECO).clone();
		oh.remove(REGISTRATO_IN_FONDO_ECO);
		return oh;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di documento.
	 *
	 * @return java.util.Dictionary
	 */

	public Dictionary getStato_pagamento_fondo_ecoKeysForSearch() {

		//OrderedHashtable d = (OrderedHashtable)getStato_pagamento_fondo_ecoKeys();
		//if (d == null) return null;

		//OrderedHashtable clone = (OrderedHashtable)d.clone();
		//clone.remove(REGISTRATO_IN_FONDO_ECO);
		//return clone;
		return STATO_FONDO_ECO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 10.45.09)
	 * @author: Alfonso Ardire
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa() {
		return terzo_spesa;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/03/2002 12.26.47)
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_uo_cds() {
		return terzo_uo_cds;
	}
	/* 
	 * Getter dell'attributo ti_associato_manrev
	 */
	public Dictionary getTi_associato_manrevKeys() {
		return STATO_MANDATO;
	}
	/* 
	 * Getter dell'attributo ti_associato_manrev
	 */
	public Dictionary getTi_associato_manrevKeysForSearch() {
		return getTi_associato_manrevKeys();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/02/2002 17.26.16)
	 * @return char
	 */
	public char getTi_entrate_spese() {
		return ti_entrate_spese;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public Dictionary getTi_istituz_commercKeys() {
		return TIPO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 11.35.49)
	 * @author: Alfonso Ardire
	 * @return java.util.Collection
	 */
	public java.util.Collection getTipi_doc() {
		return tipi_doc;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (04/06/2002 10.26.49)
	 * @author: Alfonso Ardire
	 * @return java.util.Collection
	 */
	public java.util.Collection getTipi_doc_for_search() {
		return tipi_doc_for_search;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 15.34.43)
	 * @author: Alfonso Ardire
	 * @return it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk
	 */
	public Tipo_documento_ammBulk getTipo_documento() {
		return tipo_documento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/7/2002 3:17:11 PM)
	 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
	 */
	public java.util.Dictionary getTipo_sospesoKeys() {

		java.util.Dictionary tipi = new java.util.Hashtable();
		tipi.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_ENTRATA, "Entrata");
		tipi.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_SPESA, "Spesa");
		return tipi;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 16.09.10)
	 * @return java.lang.String
	 */
	public java.lang.String getUo_CNR() {
		return uo_CNR;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getValuta() {
		return valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @return java.util.Collection
	 */
	public java.util.Collection getValute() {
		return valute;
	}
	/**
	 * Indica se la competenza COGE è stata indicata nell'anno precedente. Regola valida SOLO nel caso di 
	 * ESERCIZIO == ESERCIZIO_INIZIO
	 */
	public boolean hasCompetenzaCOGEInAnnoPrecedente() {

		return getDateCalendar(getDt_a_competenza_coge()).get(Calendar.YEAR) == getEsercizio().intValue()-1;
	}
	public boolean hasDettagliNonContabilizzati() {

		return !getDettagliNonContabilizzati().isEmpty();
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 * @return boolean
	 */
	public boolean hasDettagliPagati() {

		return !getDettagliPagati().isEmpty();
	}
	public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
		unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		setCd_cds_origine(getCd_cds());
		setCd_uo_origine(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		return super.initialize(bp,context);
	}
	public OggettoBulk initializeForFreeSearch(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		//initializeForInsert NON è errore --> la chiamata è voluta
		super.initializeForInsert(bp,context);

		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setCd_cds(null); // ho aggiunto CD_CDS nelle findFieldProperties -> imposto a NULL per escluderlo dai filtri di ricerca
		if (bp instanceof CRUDDocumentoGenericoPassivoBP && ((CRUDDocumentoGenericoPassivoBP)bp).isSpesaBP()){
			setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
			setStato_cofi(this.STATO_CONTABILIZZATO);
			setCd_tipo_documento_amm(this.GENERICO_S);
			setStato_pagamento_fondo_eco(FONDO_ECO);

			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
			setCd_cds(uo.getCd_unita_padre());
			setCd_cds_origine(uo.getCd_unita_padre());
			if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(uo.getCd_tipo_unita())){
				setCd_unita_organizzativa(null);
				setCd_uo_origine(null);
			}else{
				setCd_unita_organizzativa(uo.getCd_unita_organizzativa());
				setCd_uo_origine(uo.getCd_unita_organizzativa());
			}
		} else if (bp instanceof CRUDDocumentoGenericoAttivoBP){
			setCd_uo_origine(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		}

		return this;
	}
	public OggettoBulk initializeForInsert(CRUDBP bp,it.cnr.jada.action.ActionContext context) {


		if (getStato_cofi()==null)
			setStato_cofi(STATO_INIZIALE);
		setTi_associato_manrev(this.NON_ASSOCIATO_A_MANDATO);
		setStato_coan("N");
		setStato_pagamento_fondo_eco("N");
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		if (bp instanceof CRUDDocumentoGenericoPassivoBP ){
			if(this.getCd_tipo_documento_amm()!=null && this.getCd_tipo_documento_amm().compareTo(GENERICO_S)==0){
				setStato_liquidazione(SOSP);
				setCausale(ATTLIQ);
			}else{
				setStato_liquidazione(null);
				setCausale(null);
			}
		}
		return super.initializeForInsert(bp,context);
	}
	public OggettoBulk initializeForSearch(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		//initializeForInsert NON è errore --> la chiamata è voluta
		super.initializeForInsert(bp,context);

		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setCd_cds(null); // ho aggiunto CD_CDS nelle findFieldProperties -> imposto a NULL per escluderlo dai filtri di ricerca

		if (bp instanceof CRUDDocumentoGenericoPassivoBP && ((CRUDDocumentoGenericoPassivoBP)bp).isSpesaBP()){
			setStato_cofi(this.STATO_CONTABILIZZATO);
			setTipo_documento(new Tipo_documento_ammBulk());
			setCd_tipo_documento_amm(this.GENERICO_S);
			setStato_pagamento_fondo_eco(FONDO_ECO);
			setStato_liquidazione(LIQ);
			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
			setCd_cds(uo.getCd_unita_padre());
			setCd_cds_origine(uo.getCd_unita_padre());
			if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(uo.getCd_tipo_unita())){
				setCd_unita_organizzativa(null);
				setCd_uo_origine(null);
			}else{
				setCd_unita_organizzativa(uo.getCd_unita_organizzativa());
				setCd_uo_origine(uo.getCd_unita_organizzativa());
			}
		}
		return this;
	}
	public boolean isAbledToDeleteLettera() {

		return	isGenericoAttivo() ||
				getLettera_pagamento_estero() == null ||
				isPagata() ||
				isPagataParzialmente()||
				isROStatoTrasmissioneLettera();
	}
	public boolean isAbledToDisassociaLettera() {

		return	isGenericoAttivo() ||
				getLettera_pagamento_estero() == null ||
				isPagata() ||
				isPagataParzialmente()||
				!isROStatoTrasmissioneLettera();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isAbledToInsertLettera() {

		return (getLettera_pagamento_estero() != null) ||
				isPagata() ||
				isPagataParzialmente() || //richiesta 02449A
				isByFondoEconomale() ||
				!controllaCompatibilitaPer1210();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isAbledToModifyTipoFattura() {

		return false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 10.43.32)
	 * @return boolean
	 */
	public boolean isAnnullato() {
		return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isByFondoEconomale() {

		return	!NO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/06/2002 10.24.57)
	 * @author: Alfonso Ardire
	 * @return boolean
	 */
	public boolean isDefaultValuta() {
		return defaultValuta;
	}
	/**
	 * isDeleting method comment.
	 */
	public boolean isDeleting() {
		return false;
	}
	public boolean isDoc1210Associato() {

		return	!isGenericoAttivo() &&
				getLettera_pagamento_estero() != null &&
				getLettera_pagamento_estero().getIm_pagamento() != null &&
				getLettera_pagamento_estero().getIm_pagamento().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0;
	}
	public boolean isEditable() {
		try{
			return !(isPagata() ||
					isAnnullato() ||
					(!((getEsercizio().intValue() == getEsercizioInScrivania().intValue())&& !isRiportata()) && 
							!isDeleting()));
		}catch(NullPointerException e){
			return false;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 12.50.26)
	 * @return boolean
	 */
	public boolean isFlagEnte() {
		return flagEnte;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/03/2002 13.29.19)
	 * @return boolean
	 */
	public boolean isGenericoAttivo() {
		return getTi_entrate_spese() == ENTRATE;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public boolean isPagata() {

		return STATO_PAGATO.equalsIgnoreCase(getStato_cofi()) || 
				REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public boolean isPagataParzialmente() {

		return STATO_PARZIALE.equalsIgnoreCase(getStato_cofi());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/03/2002 13.29.19)
	 * @return boolean
	 */
	public boolean isPassivo_ente() {
		return passivo_ente;
	}
	public boolean isRiportata() {

		return !NON_RIPORTATO.equals(riportata);
	}
	public boolean isRiportataInScrivania() {

		return !NON_RIPORTATO.equals(riportataInScrivania);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/03/2002 13.29.19)
	 * @return boolean
	 */
	public boolean isROCambio() {
		return isDefaultValuta() 
				|| isGenericoAttivo() ;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isRODateCompetenzaCOGE() {

		return getDocumento_generico_dettColl() != null &&
				!getDocumento_generico_dettColl().isEmpty();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 12.50.26)
	 * @return boolean
	 */
	public boolean isROFlagEnte() {

		return	//Questo controllo evita problemi del tipo "cambiamenti di chiave" quando il doc è già salvato
				isToBeUpdated() || 
				getCrudStatus() == OggettoBulk.NORMAL ||
				//*****************************************************************************************
				((this.isPassivo_ente() && 
						(this.getUo_CNR()!=null && this.getUo_CNR().equals(getCd_uo_origine()))) ||
						this.isGenericoAttivo() && this.getAccertamentiHash() != null && !this.getAccertamentiHash().isEmpty()) ||
						(!this.isGenericoAttivo() && this.getObbligazioniHash() != null && !this.getObbligazioniHash().isEmpty()||
							(!this.isGenericoAttivo() && this.getLettera_pagamento_estero()!=null));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 12.50.26)
	 * @return boolean
	 */
	public boolean isROProgressivo() {

		return true;	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/11/2002 5:10:59 PM)
	 */
	public boolean isROSospeso() {

		return	getLettera_pagamento_estero() == null ||
				getLettera_pagamento_estero().getSospeso() == null ||
				getLettera_pagamento_estero().getSospeso().getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/11/2002 5:10:59 PM)
	 */
	public boolean isROSospesoSearchTool() {

		return false;	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isROStato_pagamento_fondo_eco() {

		return	isPagata() || 
				isPagataParzialmente() || 
				getLettera_pagamento_estero() != null || 
				(tipo_documento!=null && 
				tipo_documento.getFl_solo_partita_giro()!=null &&
				tipo_documento.getFl_solo_partita_giro().booleanValue()) ||
				(getCd_unita_organizzativa()!=null && !getCd_uo_origine().equals(getCd_unita_organizzativa()));

	}
	
	public boolean isROStatoTrasmissioneLettera() {
		if (lettera_pagamento_estero == null)
			return true;
		if (!lettera_pagamento_estero.getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO))
			return true;
		return false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/03/2002 13.29.19)
	 * @return boolean
	 */
	public boolean isROValuta() {
		return isGenericoAttivo() || (tipo_documento!=null && tipo_documento.getFl_solo_partita_giro()!=null && tipo_documento.getFl_solo_partita_giro().booleanValue());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 10.43.32)
	 */
	public boolean isVoidable() {

		return ((STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
				ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
				(STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
						PARZIALMENTE_ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
						!NON_REGISTRATO_IN_COGE.equalsIgnoreCase(getStato_coge()) ||
						!NON_CONTABILIZZATO_IN_COAN.equalsIgnoreCase(getStato_coan()) ||
						// Gennaro Borriello - (02/11/2004 16.48.21)
						// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
						//	DA UN ES. PRECEDENTE a quello di scrivania.			
						(isRiportataInScrivania() && !isRiportata()) ) &&
						!"GEN_RC_DAT".equalsIgnoreCase(getCd_tipo_documento_amm());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
	public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

		if (docCont != null && deferredSaldi != null &&
				deferredSaldi.containsKey(docCont))
			deferredSaldi.remove(docCont);
	}
	public int removeFromDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) {

		if (BulkCollections.containsByPrimaryKey(getDettagliCancellati(), (OggettoBulk)dettaglio))
			getDettagliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDettagliCancellati(), (OggettoBulk)dettaglio));
		return dettagliCancellati.size()-1;
	}
	public int removeFromDocumentiContabiliCancellati(it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk dettaglio) {

		if (BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio))
			getDocumentiContabiliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio));
		return documentiContabiliCancellati.size()-1;
	}
	public void removeFromDocumento_generico_accertamentiHash(
			Documento_generico_rigaBulk riga) {

		Vector righeAssociate = (Vector)documento_generico_accertamentiHash.get(riga.getAccertamento_scadenziario());
		//if (righeAssociate != null) {
		//righeAssociate.remove(riga);
		//if (righeAssociate.isEmpty())
		//documento_generico_accertamentiHash.remove(riga.getAccertamento_scadenziario());
		//}
		if (righeAssociate != null) {
			righeAssociate.remove(riga);
			if (righeAssociate.isEmpty()) {
				documento_generico_accertamentiHash.remove(riga.getAccertamento_scadenziario());
				riga.getAccertamento_scadenziario().setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
				addToDocumentiContabiliCancellati(riga.getAccertamento_scadenziario());
			}
		} else
			addToDocumentiContabiliCancellati(riga.getAccertamento_scadenziario());	
	}
	public Documento_generico_rigaBulk removeFromDocumento_generico_dettColl( int indiceDiLinea ) 
	{
		Documento_generico_rigaBulk element = (Documento_generico_rigaBulk)documento_generico_dettColl.get(indiceDiLinea);
		addToDettagliCancellati(element);
		if (element != null && getTi_entrate_spese()==ENTRATE && element.getAccertamento_scadenziario() != null)
			removeFromDocumento_generico_accertamentiHash(element);
		if (element != null && getTi_entrate_spese()==SPESE && element.getObbligazione_scadenziario() != null)
			removeFromDocumento_generico_obbligazioniHash(element);




		//int righe_pagate= 0;
		//int righe_contabilizzate= 0;
		//int righe_ass_man_rev= 0;
		//int righe= 0;
		//Documento_generico_rigaBulk riga= null;
		//for (java.util.Iterator i= this.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
		//riga= (Documento_generico_rigaBulk) i.next();
		//if (!riga.equals(element)) {
		//if (Documento_generico_rigaBulk.STATO_PAGATO.equals(riga.getStato_cofi()))
		//righe_pagate++;
		//if (Documento_generico_rigaBulk.STATO_CONTABILIZZATO.equals(riga.getStato_cofi()))
		//righe_contabilizzate++;
		//if (Documento_generico_rigaBulk.ASSOCIATO_A_MANDATO.equals(riga.getTi_associato_manrev()));
		//righe_ass_man_rev++;
		//righe++;
		//}
		//}
		//if (righe==0 || (righe_pagate == 0 && righe_contabilizzate == 0))
		//this.setStato_cofi(this.STATO_INIZIALE);
		//else if (righe_pagate == righe)
		//this.setStato_cofi(this.STATO_PAGATO);
		//else if (righe_contabilizzate == righe)
		//this.setStato_cofi(this.STATO_CONTABILIZZATO);
		//else if (righe_pagate != 0)
		//this.setStato_cofi(this.STATO_PARZIALE);

		//if (righe==0 || righe_ass_man_rev==0)
		//this.setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);
		//else if (righe_ass_man_rev!=0 && righe_ass_man_rev<righe)
		//this.setTi_associato_manrev(PARZIALMENTE_ASSOCIATO_A_MANDATO);
		//else if (righe_ass_man_rev!=0 && righe_ass_man_rev==righe)
		//this.setTi_associato_manrev(ASSOCIATO_A_MANDATO);

		return (Documento_generico_rigaBulk) documento_generico_dettColl.remove(indiceDiLinea);


		//Documento_generico_rigaBulk removedElement = (Documento_generico_rigaBulk)documento_generico_dettColl.remove(indiceDiLinea);

		//int numeroDiRigheNonContabilizzate= 0;
		//Documento_generico_rigaBulk riga= null;
		//for (java.util.Iterator i= this.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
		//riga= (Documento_generico_rigaBulk) i.next();
		//if (Documento_generico_rigaBulk.STATO_INIZIALE.equals(riga.getStato_cofi()))
		//numeroDiRigheNonContabilizzate++;
		//}
		//if (numeroDiRigheNonContabilizzate == 0)
		//this.setStato_cofi(this.STATO_INIZIALE);

		//return removedElement;
	}
	public void removeFromDocumento_generico_obbligazioniHash(
			Documento_generico_rigaBulk riga) {

		Vector righeAssociate = (Vector)documento_generico_obbligazioniHash.get(riga.getObbligazione_scadenziario());
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza=riga.getObbligazione_scadenziario();	
		//if (righeAssociate != null) {
		//scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0));
		//scadenza.setToBeUpdated();
		//righeAssociate.remove(riga);
		//if (righeAssociate.isEmpty())
		//documento_generico_obbligazioniHash.remove(riga.getObbligazione_scadenziario());
		//}
		if (righeAssociate != null) {
			righeAssociate.remove(riga);
			if (righeAssociate.isEmpty()) {
				documento_generico_obbligazioniHash.remove(riga.getObbligazione_scadenziario());
				if (!isPassivo_ente())
					riga.getObbligazione_scadenziario().setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
				else
					riga.getObbligazione_scadenziario().setIm_associato_doc_amm(riga.getObbligazione_scadenziario().getIm_associato_doc_amm().subtract((riga.getIm_riga_iniziale()==null?riga.getIm_imponibile():riga.getIm_riga_iniziale())).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
				addToDocumentiContabiliCancellati(riga.getObbligazione_scadenziario());
			}
		} else
			addToDocumentiContabiliCancellati(riga.getObbligazione_scadenziario());
	}

	public void resetDefferredSaldi() {

		deferredSaldi = null;	
	}
	public void setAndVerifyStatus() {

		if (getStato_cofi() != STATO_PAGATO) {
			if (hasDettagliPagati())
				setStato_cofi(STATO_PARZIALE);
			else
				setStato_cofi(hasDettagliNonContabilizzati() ?
						STATO_INIZIALE : 
							STATO_CONTABILIZZATO);
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 10.43.32)
	 * @return boolean
	 */
	public void setAnnullato(java.sql.Timestamp date) {
		setStato_cofi(STATO_ANNULLATO);
		setDt_cancellazione(date);
	}
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.getValuta().setCd_divisa(cd_divisa);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public void setCd_tipo_doc_amm(java.lang.String newCd_tipo_doc_amm) {

		if (getTipo_documento()==null)
			setTipo_documento(new Tipo_documento_ammBulk());
		setCd_tipo_documento_amm(newCd_tipo_doc_amm);
	}
	public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
		this.getTipo_documento().setCd_tipo_documento_amm(cd_tipo_documento_amm);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public void setCd_uo(java.lang.String newCd_uo) {

		setCd_unita_organizzativa(newCd_uo);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 16.09.10)
	 * @param newCds_CNR java.lang.String
	 */
	public void setCds_CNR(java.lang.String newCds_CNR) {
		cds_CNR = newCds_CNR;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 13.24.17)
	 * @param newChangeOperation char
	 */
	public void setChangeOperation(char newChangeOperation) {
		changeOperation = newChangeOperation;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/06/2002 10.24.57)
	 * @author: Alfonso Ardire
	 * @param newDefaultValuta boolean
	 */
	public void setDefaultValuta(boolean newDefaultValuta) {
		defaultValuta = newDefaultValuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/02/2002 16.41.41)
	 * @param newDettagliCancellati java.util.Vector
	 */
	public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
		dettagliCancellati = newDettagliCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/02/2002 16.39.28)
	 * @param newDocumentiContabiliCancellati java.util.Vector
	 */
	public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
		documentiContabiliCancellati = newDocumentiContabiliCancellati;
	}

	public void setDocumento_generico_accertamentiHash(AccertamentiTable newDocumento_generico_accertamentiHash) {
		documento_generico_accertamentiHash = newDocumento_generico_accertamentiHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/09/2001 10.54.15)
	 * @param newDocumento_generico_dettColl it.cnr.jada.bulk.BulkList
	 */
	public void setDocumento_generico_dettColl(it.cnr.jada.bulk.BulkList newDocumento_generico_dettColl) {
		documento_generico_dettColl = newDocumento_generico_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 14.10.38)
	 * @param newDocumento_generico_obbligazioniHash it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public void setDocumento_generico_obbligazioniHash(ObbligazioniTable newDocumento_generico_obbligazioniHash) {
		documento_generico_obbligazioniHash = newDocumento_generico_obbligazioniHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/07/2003 11.04.01)
	 * @param newDt_termine_creazione_docamm java.sql.Timestamp
	 */
	public void setDt_termine_creazione_docamm(java.sql.Timestamp newDt_termine_creazione_docamm) {
		dt_termine_creazione_docamm = newDt_termine_creazione_docamm;
	}
	public void setEsercizio_lettera(java.lang.Integer esercizio_lettera) {
		this.getLettera_pagamento_estero().setEsercizio(esercizio_lettera);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @param newFine_validita_valuta java.sql.Timestamp
	 */
	public void setFine_validita_valuta(java.sql.Timestamp newFine_validita_valuta) {
		fine_validita_valuta = newFine_validita_valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 12.50.26)
	 * @param newFlagEnte boolean
	 */
	public void setFlagEnte(boolean newFlagEnte) {
		flagEnte = newFlagEnte;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 16.04.00)
	 * @param newImportoTotalePerAccertamento java.math.BigDecimal
	 */
	public void setImportoTotalePerAccertamento(java.math.BigDecimal newImportoTotalePerAccertamento) {
		importoTotalePerAccertamento = newImportoTotalePerAccertamento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/12/2001 16.04.00)
	 * @param newImportoTotalePerObbligazione java.math.BigDecimal
	 */
	public void setImportoTotalePerObbligazione(java.math.BigDecimal newImportoTotalePerObbligazione) {
		importoTotalePerObbligazione = newImportoTotalePerObbligazione;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @param newInizio_validita_valuta java.sql.Timestamp
	 */
	public void setInizio_validita_valuta(java.sql.Timestamp newInizio_validita_valuta) {
		inizio_validita_valuta = newInizio_validita_valuta;
	}
	/**
	 * setIsDeleting method comment.
	 */
	public void setIsDeleting(boolean deletingStatus) {}

	private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

	@Override
	public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
		return scrittura_partita_doppia;
	}
	@Override
	public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
		this.scrittura_partita_doppia = scrittura_partita_doppia;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (09/07/2002 16.29.09)
	 * @author: CNRADM
	 * @param newLettera_pagamento_estero it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk
	 */
	public void setLettera_pagamento_estero(Lettera_pagam_esteroBulk newLettera_pagamento_estero) {
		lettera_pagamento_estero = newLettera_pagamento_estero;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/10/2001 14.27.01)
	 * @param newNum_dettColl int
	 */
	public void setNum_dettColl(int newNum_dettColl) {
		num_dettColl = newNum_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/03/2002 13.29.19)
	 * @param newPassivo_ente boolean
	 */
	public void setPassivo_ente(boolean newPassivo_ente) {
		passivo_ente = newPassivo_ente;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/28/2002 1:14:55 PM)
	 * @return java.lang.Integer
	 */
	public void setPg_doc_amm(java.lang.Long newPg) {

		setPg_documento_generico(newPg);
	}
	public void setPg_lettera(java.lang.Long pg_lettera) {
		this.getLettera_pagamento_estero().setPg_lettera(pg_lettera);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/05/2003 15.55.11)
	 * @param newRiportata java.lang.String
	 */
	public void setRiportata(java.lang.String newRiportata) {
		riportata = newRiportata;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (02/11/2004 14.12.35)
	 * @param newRiportataInScrivania java.lang.String
	 */
	public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
		riportataInScrivania = newRiportataInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 10.45.09)
	 * @author: Alfonso Ardire
	 * @param newTerzo_spesa it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setTerzo_spesa(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo_spesa) {
		terzo_spesa = newTerzo_spesa;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/03/2002 12.26.47)
	 * @param newTerzo_uo_cds it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setTerzo_uo_cds(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo_uo_cds) {
		terzo_uo_cds = newTerzo_uo_cds;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/02/2002 17.26.16)
	 * @param newTi_entrate_spese char
	 */
	public void setTi_entrate_spese(char newTi_entrate_spese) {
		ti_entrate_spese = newTi_entrate_spese;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 11.35.49)
	 * @author: Alfonso Ardire
	 * @param newTipi_doc java.util.Collection
	 */
	public void setTipi_doc(java.util.Collection newTipi_doc) {
		tipi_doc = newTipi_doc;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (04/06/2002 10.26.49)
	 * @author: Alfonso Ardire
	 * @param newTipi_doc_for_search java.util.Collection
	 */
	public void setTipi_doc_for_search(java.util.Collection newTipi_doc_for_search) {
		tipi_doc_for_search = newTipi_doc_for_search;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/05/2002 15.34.43)
	 * @author: Alfonso Ardire
	 * @param newTipo_documento it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk
	 */
	public void setTipo_documento(Tipo_documento_ammBulk newTipo_documento) {
		tipo_documento = newTipo_documento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (22/02/2002 16.09.10)
	 * @param newUo_CNR java.lang.String
	 */
	public void setUo_CNR(java.lang.String newUo_CNR) {
		uo_CNR = newUo_CNR;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @param newValuta it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public void setValuta(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newValuta) {
		valuta = newValuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (15/10/2001 11.47.47)
	 * @param newValute java.util.Collection
	 */
	public void setValute(java.util.Collection newValute) {
		valute = newValute;
	}
	public void validaDateCompetenza()
			throws ValidationException {

		if (getDt_da_competenza_coge() == null) 
			throw new ValidationException("Inserire la data di \"competenza da\" per la testata documento.");
		if (getDt_a_competenza_coge() == null)
			throw new ValidationException("Inserire la data di \"competenza a\" per la testata documento.");

		Calendar competenzaDa = getDateCalendar(getDt_da_competenza_coge());
		Calendar competenzaA = getDateCalendar(getDt_a_competenza_coge());

		if (competenzaA.before(competenzaDa))
			throw new ValidationException("Inserire correttamente le date di competenza in testata documento");

		if (isFlagEnte())
			validaDateCompetenzaEnte(competenzaDa, competenzaA);
		else
			validaDateCompetenzaNonEnte(competenzaDa, competenzaA);
	}
	private void validaDateCompetenzaEnte(
			Calendar competenzaDa,
			Calendar competenzaA)
					throws ValidationException {

		int annoCompetenzaDa = competenzaDa.get(Calendar.YEAR);
		int annoCompetenzaA = competenzaA.get(Calendar.YEAR);
		try {
			if (annoCompetenzaDa != getEsercizio().intValue())
				throw new ValidationException("La data di inizio competenza deve appartenere all'esercizio di scrivania!");
			if (annoCompetenzaA > getEsercizio().intValue()+1)
				throw new ValidationException("La data di fine competenza deve appartenere all'esercizio di scrivania o al successivo!");
		} catch (ValidationException e) {
			int annoPrecedente = getEsercizio().intValue()-1;
			if ((annoCompetenzaA < annoPrecedente) ||
					(annoCompetenzaDa < annoPrecedente))
				throw e;
			else if (annoCompetenzaDa == annoPrecedente) {
				if (annoCompetenzaA > annoPrecedente)	
					throw new ValidationException("La data di \"competenza a\" deve appartenere all'esercizio dell'anno " + annoPrecedente + ".");
				if (isGenericoAttivo() && getData_registrazione().after(getDt_termine_creazione_docamm())) {
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
					throw new ValidationException("Non è possibile inserire documenti con competenza nell'anno precedente con data di registrazione successiva al " + sdf.format(getDt_termine_creazione_docamm()) + "!");
				}
			} else
				throw e;
		}	
	}
	private void validaDateCompetenzaNonEnte(
			Calendar competenzaDa,
			Calendar competenzaA)
					throws ValidationException {

		int annoCompetenzaDa = competenzaDa.get(Calendar.YEAR);
		int annoCompetenzaA = competenzaA.get(Calendar.YEAR);
		try {
			if (annoCompetenzaDa != getEsercizio().intValue())
				throw new ValidationException("La data di inizio competenza deve appartenere all'esercizio di scrivania!");
			if (isGenericoAttivo()) {
				if (annoCompetenzaA > getEsercizio().intValue())
					throw new ValidationException("La data di fine competenza deve appartenere all'esercizio di scrivania!");
			} else {
				if (annoCompetenzaA > getEsercizio().intValue()+1)
					throw new ValidationException("La data di fine competenza deve appartenere all'esercizio di scrivania o al successivo!");
			}
		} catch (ValidationException e) {
			int annoPrecedente = getEsercizio().intValue()-1;
			if (isGenericoAttivo())
				throw e;
			if ((annoCompetenzaA < annoPrecedente) ||
					(annoCompetenzaDa < annoPrecedente))
				throw e;
			else if (annoCompetenzaDa == annoPrecedente) {
				if (annoCompetenzaA > annoPrecedente)	
					throw new ValidationException("La data di \"competenza a\" deve appartenere all'esercizio dell'anno " + annoPrecedente + ".");
				if (isGenericoAttivo() && getData_registrazione().after(getDt_termine_creazione_docamm())) {
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
					throw new ValidationException("Non è possibile inserire documenti con competenza nell'anno precedente con data di registrazione successiva al " + sdf.format(getDt_termine_creazione_docamm()) + "!");
				}
			} else
				throw e;
		}
	}
	public void validate() throws ValidationException {

		if (getTipo_documento()==null)
			throw new ValidationException("Selezionare un tipo di documento");
		if(getTipo_documento().getCd_tipo_documento_amm().compareTo(GENERICO_S)==0)
			if (dataInizioObbligoRegistroUnico!=null && getData_registrazione().after(dataInizioObbligoRegistroUnico))
			{
				if(getStato_liquidazione()==null)
					throw new ValidationException("Inserire lo stato della liquidazione!");
				if(getStato_liquidazione()!=null && getStato_liquidazione().compareTo(this.LIQ)!=0 && getCausale()==null)
					throw new ValidationException("Inserire la causale.");
			}

		if (getLettera_pagamento_estero() != null)
			getLettera_pagamento_estero().validate();

		if (getStato_cofi()==null)
			setStato_cofi(STATO_INIZIALE);

		if (getStato_coge()==null)
			setStato_coge(NON_REGISTRATO_IN_COGE);

		if (getTi_entrate_spese() == Documento_genericoBulk.SPESE)
		{
			for (Iterator i = documento_generico_dettColl.iterator(); i.hasNext();) {
				Documento_generico_rigaBulk riga = ((Documento_generico_rigaBulk)i.next());
				if (riga.getObbligazione_scadenziario() != null &&
						riga.getObbligazione_scadenziario().getObbligazione() != null &&
						riga.getObbligazione_scadenziario().getObbligazione().getFl_netto_sospeso())
					setStato_coge(DA_NON_REGISTRARE_IN_COGE);
			}
		}

		if (getTi_istituz_commerc()==null)
			setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());

		if (getIm_totale()==null)
			setIm_totale(new java.math.BigDecimal(0));

		if (getData_registrazione()!=null && getDt_scadenza()!=null && getData_registrazione().after(getDt_scadenza()))
			throw new ValidationException("La data di registrazione deve essere precedente a quella di scadenza!");

		if (getDs_documento_generico()==null)
			throw new ValidationException("Inserire una descrizione del documento.");
		//if (getFl_modifica_coge()==null)
		//setFl_modifica_coge(Boolean.FALSE);


		java.util.Calendar limInf = null;
		java.util.Calendar limSup = null;
		java.util.Calendar today = getDateCalendar(null);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

		java.util.Calendar dataRegistrazione = getDateCalendar(getData_registrazione());

		try {
			int compare = getEsercizio().compareTo(new Integer(dataRegistrazione.get(Calendar.YEAR)));
			if (compare == 0) {
				limSup = today;
				limInf = getDateCalendar(new java.sql.Timestamp(sdf.parse("01/01/"+getEsercizio().intValue()).getTime()));
			} else if (compare > 0) {
				limSup = getDateCalendar(new java.sql.Timestamp(sdf.parse("31/12/"+getEsercizio().intValue()).getTime()));
				limInf = getDateCalendar(new java.sql.Timestamp(sdf.parse("01/01/"+getEsercizio().intValue()).getTime()));
			} else
				throw new ValidationException("La data di registrazione deve appartenere all'esercizio " + getEsercizio().intValue() + "!");

			if (!((dataRegistrazione.after(limInf) || (dataRegistrazione.equals(limInf))) && 
					(dataRegistrazione.before(limSup) || (dataRegistrazione.equals(limSup)))))
				throw new ValidationException("La data di registrazione deve essere compresa tra il " + sdf.format(limInf.getTime()) + " e il " + sdf.format(limSup.getTime()) + "!");
		} catch (java.text.ParseException e) {
			throw new ValidationException("Data registrazione NON valida!");
		}

		validaDateCompetenza();

		if (isGenericoAttivo() && (getTipoDocumentoGenerico()== null|| getTipoDocumentoGenerico().getId() == null)){
			throw new ValidationException("Inserire il tipo documento generico.");
		}
		super.validate();
	}
	public boolean isDetailDoubled() {
		return isDetailDoubled;
	}
	public void setDetailDoubled(boolean isDetailDoubled) {
		this.isDetailDoubled = isDetailDoubled;
	}
	public boolean isDocumentoModificabile() {
		return isDocumentoModificabile;
	}
	public void setDocumentoModificabile(boolean isDocumentoModificabile) {
		this.isDocumentoModificabile = isDocumentoModificabile;
	}
	public void addToAssociazioniInventarioHash(
			Ass_inv_bene_fatturaBulk ass,
			Documento_generico_rigaBulk riga) {

		if (associazioniInventarioHash == null)
			associazioniInventarioHash = new AssociazioniInventarioTable();
		Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			associazioniInventarioHash.put(ass, righeAssociate);
		}
		if (riga != null && !righeAssociate.contains(riga))
			righeAssociate.add(riga);
	}

	public void addToCarichiInventarioHash(
			Buono_carico_scaricoBulk buonoCS,
			Documento_generico_rigaBulk riga) {

		if (carichiInventarioHash == null)
			carichiInventarioHash = new CarichiInventarioTable();
		Vector righeAssociate = (Vector)carichiInventarioHash.get(buonoCS);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			carichiInventarioHash.put(buonoCS, righeAssociate);
		}
		if (riga != null && !righeAssociate.contains(riga))
			righeAssociate.add(riga);
	}
	public AssociazioniInventarioTable getAssociazioniInventarioHash() {
		return associazioniInventarioHash;
	}
	public void setAssociazioniInventarioHash(
			AssociazioniInventarioTable associazioniInventarioHash) {
		this.associazioniInventarioHash = associazioniInventarioHash;
	}
	public CarichiInventarioTable getCarichiInventarioHash() {
		return carichiInventarioHash;
	}
	public void setCarichiInventarioHash(
			CarichiInventarioTable carichiInventarioHash) {
		this.carichiInventarioHash = carichiInventarioHash;
	}
	public void removeFromAssociazioniInventarioHash(
			Ass_inv_bene_fatturaBulk ass,
			Documento_generico_rigaBulk riga) {

		if (associazioniInventarioHash == null) return;
		Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
		if (righeAssociate != null) {
			if (riga != null && righeAssociate.contains(riga))
				righeAssociate.remove(riga);
			if (righeAssociate.isEmpty())
				associazioniInventarioHash.remove(ass);
		}
	}

	public void removeFromCarichiInventarioHash(
			Buono_carico_scaricoBulk buonoCS,
			Documento_generico_rigaBulk riga) {

		if (carichiInventarioHash == null) return;
		Vector righeAssociate = (Vector)carichiInventarioHash.get(buonoCS);
		if (righeAssociate != null) {
			if (riga != null && righeAssociate.contains(riga))
				righeAssociate.remove(riga);
			if (righeAssociate.isEmpty())
				carichiInventarioHash.remove(buonoCS);
		}
	}
	/**
	 * @return
	 */
	public Boolean getHa_beniColl() {
		if (ha_beniColl!=null )
			return ha_beniColl;
		else
			return Boolean.FALSE;	
	}

	/**
	 * @param boolean1
	 */
	public void setHa_beniColl(Boolean boolean1) {
		ha_beniColl = boolean1;
	}
	public Ass_inv_bene_fatturaBulk getAssociationWithInventarioFor(Documento_generico_rigaBulk riga) {

		if (associazioniInventarioHash == null || riga == null) return null;

		for (Enumeration e = associazioniInventarioHash.keys(); e.hasMoreElements();) {
			Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
			Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
			if (righeAssociate != null && !righeAssociate.isEmpty() &&
					BulkCollections.containsByPrimaryKey(righeAssociate, riga))
				return ass;
		}
		return null;
	}
	public Dictionary getStato_liquidazioneKeys() {
		return STATO_LIQUIDAZIONE;
	}
	public Dictionary getCausaleKeys(){
		return CAUSALE;
	}
	public java.sql.Timestamp getDataInizioObbligoRegistroUnico() {
		return dataInizioObbligoRegistroUnico;
	}
	public void setDataInizioObbligoRegistroUnico(
			java.sql.Timestamp dataInizioObbligoRegistroUnico) {
		this.dataInizioObbligoRegistroUnico = dataInizioObbligoRegistroUnico;
	}
	public Tipo_documento_genericoBulk getTipoDocumentoGenerico() {
		return tipoDocumentoGenerico;
	}
	public void setTipoDocumentoGenerico(Tipo_documento_genericoBulk tipoDocumentoGenerico) {
		this.tipoDocumentoGenerico = tipoDocumentoGenerico;
	}	
	@Override
	public Integer getIdTipoDocumentoGenerico() {
		return Optional.ofNullable(getTipoDocumentoGenerico())
					.map(Tipo_documento_genericoBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdTipoDocumentoGenerico(Integer idTipoDocumentoGenerico) {
		Optional.ofNullable(getTipoDocumentoGenerico()).ifPresent(el->el.setId(idTipoDocumentoGenerico));
	}

	public TipoDocumentoEnum getTipoDocumentoEnum() {
		return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc_amm());
	}

	@Override
	public String getCd_tipo_doc() {
		return this.getCd_tipo_doc_amm();
	}

	@Override
	public Long getPg_doc() {
		return this.getPg_doc_amm();
	}

	@Override
	public Timestamp getDt_contabilizzazione() {
		return this.getData_registrazione();
	}
}