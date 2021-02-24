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

/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.StrServ;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.si.spring.storage.StorageObject;

import java.io.File;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public abstract class Fattura_attivaBulk extends Fattura_attivaBase implements IDocumentoAmministrativoBulk, Voidable, it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi,
		AllegatoParentBulk {
	public final static String BENEDUREVOLE        = "B";
	public final static String CONTRATTO           = "C";
	public final static String LIBERA              = "L";
	public final static String TARIFFARIO          = "T";
	@JsonIgnore
	private int num_dettColl=0;

	private StorageObject storageObject;
	private File file;
	private String nomeFile;
	private String typeNameForCMIS;

	public final static String STATO_INIZIALE = "I";
	public final static String STATO_CONTABILIZZATO = "C";
	public final static String STATO_PARZIALE = "Q";
	public final static String STATO_PAGATO = "P";
	public final static String STATO_ANNULLATO = "A";

	public final static Dictionary intra_ueKeys;
	public final static Dictionary san_marinoKeys;
	public final static Dictionary extra_ueKeys;
	public final static Dictionary STATO;
	public final static Dictionary STATO_MANDATO;
	public final static Dictionary STATO_COGE;
	public final static Dictionary STATO_COAN;
	public final static Dictionary SEZIONALI_FLAG_KEYS;
	public final static Dictionary STATI_RIPORTO;

	public final static String NON_ASSOCIATO_A_MANDATO = "N";
	public final static String PARZIALMENTE_ASSOCIATO_A_MANDATO = "P";
	public final static String ASSOCIATO_A_MANDATO = "T";

	public final static String NON_CONTABILIZZATO_IN_COAN = "N";
	public final static String CONTABILIZZATO_IN_COAN = "C";
	public final static String DA_RICONTABILIZZARE_IN_COAN = "R";
	public final static String NON_PROCESSARE_IN_COAN = "X";

	public final static String NON_REGISTRATO_IN_COGE = "N";
	public final static String REGISTRATO_IN_COGE = "C";
	public final static String DA_RIREGISTRARE_IN_COGE = "R";
	public final static String NON_PROCESSARE_IN_COGE = "X";

	public final static String SEZIONALI_FLAGS_ALL = "0";
	public final static String SEZIONALI_FLAGS_ORD = "1";
	public final static String SEZIONALI_FLAGS_IUE = "2";
	public final static String SEZIONALI_FLAGS_EUE = "3";
	public final static String SEZIONALI_FLAGS_SM  = "4";

	public final static String FATTURA_DI_SERVIZI = Bene_servizioBulk.SERVIZIO;
	public final static String FATTURA_DI_BENI = Bene_servizioBulk.BENE;
	public final static Dictionary FATTURA_BENI_SERVIZI;

    protected final static java.util.Dictionary statoFattureElettronicheKeys;

	public final static String DA_PREDISPORRE_ALLA_FIRMA = "P";
	public final static String DA_FIRMARE = "F";
	public final static String DA_PREDISPORRE_E_FIRMARE = "S";

	static {
    	statoFattureElettronicheKeys = new it.cnr.jada.util.OrderedHashtable();
    	statoFattureElettronicheKeys.put(DA_FIRMARE, "Da Firmare");
    	statoFattureElettronicheKeys.put(DA_PREDISPORRE_E_FIRMARE, "Da Predisporre e firmare");
    }

	public final static String FATT_ELETT_PREDISPOSTA_FIRMA = "PRE";
	public final static String FATT_ELETT_INVIATA_SDI = "INV";
	public final static String FATT_ELETT_ALLA_FIRMA = "FIR";
	public final static String FATT_ELETT_SCARTATA_DA_SDI = "SCA";
	public final static String FATT_ELETT_CONSEGNATA_SDI = "COS";
	public final static String FATT_ELETT_AVVISO_NOTIFICA_INVIO_MAIL = "AVV";
	public final static String FATT_ELETT_MANCATA_CONSEGNA = "MAC";
	public final static String FATT_ELETT_NON_RECAPITABILE = "NRE";
	public final static String FATT_ELETT_CONSEGNATA_DESTINATARIO = "CON";
	public final static String FATT_ELETT_ACCETTATA_DESTINATARIO = "ACC";
	public final static String FATT_ELETT_RIFIUTATA_DESTINATARIO =  "RIF";
	public final static String FATT_ELETT_DECORRENZA_TERMINI = "DEC";
	public final static String FATT_ELETT_FIRMATA_NC = "FIN";


	public final static String TIPO_FATTURA_ATTIVA = "F";
	public final static String TIPO_NOTA_DI_CREDITO = "C";
	public final static String TIPO_NOTA_DI_DEBITO = "D";
	@JsonIgnore
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	private String statoFattElett;
	public final static Dictionary statoInvioSdiKeys;

	public final static Dictionary tipoFatturaKeys;

	/*
	Definizione del motivo di emissione.

	Dominio:
	B = Bene durevole
	C = Contratto
	L = Libera
	T = Tariffario
	 */
	public final static Dictionary TMOTIVOEMISSIONE;
	static {
		TMOTIVOEMISSIONE = new it.cnr.jada.util.OrderedHashtable();
		TMOTIVOEMISSIONE.put(BENEDUREVOLE,"Bene durevole");
		TMOTIVOEMISSIONE.put(CONTRATTO,"Contratto");
		TMOTIVOEMISSIONE.put(LIBERA,"Libera");
		TMOTIVOEMISSIONE.put(TARIFFARIO,"Tariffario");		

		intra_ueKeys = new OrderedHashtable();
		intra_ueKeys.put(new Boolean(false), "N");
		intra_ueKeys.put(new Boolean(true), "Y");

		san_marinoKeys = new OrderedHashtable();
		san_marinoKeys.put(new Boolean(false), "N");
		san_marinoKeys.put(new Boolean(true), "Y");

		extra_ueKeys = new OrderedHashtable();
		extra_ueKeys.put(new Boolean(false), "N");
		extra_ueKeys.put(new Boolean(true), "Y");

		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE,"Iniziale");
		STATO.put(STATO_CONTABILIZZATO,"Contabilizzato");
		STATO.put(STATO_PARZIALE,"Parziale");
		STATO.put(STATO_PAGATO,"Incassato");
		STATO.put(STATO_ANNULLATO,"Annullato");

		STATO_MANDATO = new it.cnr.jada.util.OrderedHashtable();
		STATO_MANDATO.put(NON_ASSOCIATO_A_MANDATO,"Man/rev non associato");
		STATO_MANDATO.put(PARZIALMENTE_ASSOCIATO_A_MANDATO,"Parzialmente associato a man/rev");
		STATO_MANDATO.put(ASSOCIATO_A_MANDATO,"Man/rev associato");

		STATO_COGE = new it.cnr.jada.util.OrderedHashtable();
		STATO_COGE.put(NON_REGISTRATO_IN_COGE,"Non registrato");
		STATO_COGE.put(REGISTRATO_IN_COGE,"Registrato");
		STATO_COGE.put(DA_RIREGISTRARE_IN_COGE,"Da registrare nuovamente");
		STATO_COGE.put(NON_PROCESSARE_IN_COGE,"Non processare");

		STATO_COAN = new it.cnr.jada.util.OrderedHashtable();
		STATO_COAN.put(NON_CONTABILIZZATO_IN_COAN,"Non contabilizzato");
		STATO_COAN.put(CONTABILIZZATO_IN_COAN,"Contabilizzato");
		STATO_COAN.put(DA_RICONTABILIZZARE_IN_COAN,"Da contabilizzare nuovamente");
		STATO_COAN.put(NON_PROCESSARE_IN_COAN,"Non processare");

		SEZIONALI_FLAG_KEYS = new it.cnr.jada.util.OrderedHashtable();
		SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_ALL, "Tutte");
		SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_ORD, "Ordinarie");
		SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_IUE, "Intra UE");
		SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_EUE, "Extra UE");
		SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_SM, "S. Marino");

		STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_RIPORTO.put(NON_RIPORTATO,"Non riportata");
		STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO,"Parzialmente riportata");
		STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO,"Completamente riportata");

		FATTURA_BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
		FATTURA_BENI_SERVIZI.put(FATTURA_DI_BENI,"Fattura di beni");
		FATTURA_BENI_SERVIZI.put(FATTURA_DI_SERVIZI,"Fattura di servizi");

		statoInvioSdiKeys = new it.cnr.jada.util.OrderedHashtable();
		statoInvioSdiKeys.put(FATT_ELETT_INVIATA_SDI,"Inviata a SDI");
		statoInvioSdiKeys.put(FATT_ELETT_ALLA_FIRMA,"Alla Firma");
		statoInvioSdiKeys.put(FATT_ELETT_PREDISPOSTA_FIRMA,"Predisposta alla Firma");
		statoInvioSdiKeys.put(FATT_ELETT_SCARTATA_DA_SDI,"Scartata da SDI");
		statoInvioSdiKeys.put(FATT_ELETT_CONSEGNATA_SDI,"Consegnata SDI");
		statoInvioSdiKeys.put(FATT_ELETT_AVVISO_NOTIFICA_INVIO_MAIL,"Consegnata a SDI e Inviato Avviso notifica e-mail");
		statoInvioSdiKeys.put(FATT_ELETT_MANCATA_CONSEGNA,"Mancata consegna");	
		statoInvioSdiKeys.put(FATT_ELETT_NON_RECAPITABILE,"Non recapitabile");	
		statoInvioSdiKeys.put(FATT_ELETT_CONSEGNATA_DESTINATARIO,"Consegnata al destinatario");	
		statoInvioSdiKeys.put(FATT_ELETT_ACCETTATA_DESTINATARIO,"Accettata dal destinatario");	
		statoInvioSdiKeys.put(FATT_ELETT_RIFIUTATA_DESTINATARIO,"Rifiutata dal destinatario");	
		statoInvioSdiKeys.put(FATT_ELETT_DECORRENZA_TERMINI,"Decorrenza termini accettazione/rifiuto da parte del destinatario");	
		statoInvioSdiKeys.put(FATT_ELETT_FIRMATA_NC,"Firmata(solo per le note di credito)");	

		tipoFatturaKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoFatturaKeys.put(TIPO_FATTURA_ATTIVA,"Fattura");
		tipoFatturaKeys.put(TIPO_NOTA_DI_CREDITO,"Nota Credito");
		tipoFatturaKeys.put(TIPO_NOTA_DI_DEBITO,"Nota Debito");	

	}
	@JsonIgnore
	protected SezionaleBulk sezionale;
	@JsonIgnore
	protected Tipo_sezionaleBulk tipo_sezionale;
	@JsonIgnore
	protected DivisaBulk valuta;
	@JsonIgnore
	private Fattura_attivaBulk fattura_attiva;
	@JsonIgnore
	protected AnagraficoBulk anagrafico;
	@JsonIgnore
	protected TerzoBulk cliente;
	@JsonIgnore
	protected TerzoBulk terzo_uo;
	@JsonIgnore
	protected BancaBulk banca_uo;
	protected Rif_modalita_pagamentoBulk modalita_pagamento_uo;
	protected Rif_termini_pagamentoBulk termini_pagamento_uo;
	protected Consuntivo_rigaVBulk consuntivo_riga;

	@JsonIgnore
	private BulkList fattura_attiva_dettColl = new BulkList();
	@JsonIgnore
	private BulkList riferimenti_bancari = new BulkList();
	@JsonIgnore
	private Collection fattura_attiva_consuntivoColl = new Vector();
	@JsonIgnore
	private AccertamentiTable fattura_attiva_accertamentiHash = null;
	@JsonIgnore
	private Map fattura_attiva_ass_totaliMap = null;
	@JsonIgnore
	private CarichiInventarioTable carichiInventarioHash = null;
	@JsonIgnore
	private AssociazioniInventarioTable associazioniInventarioHash = null;
	@JsonIgnore
	private Boolean ha_beniColl;
	@JsonIgnore
	private java.math.BigDecimal importoTotalePerAccertamento = new java.math.BigDecimal(0);

	public static final char DIVISIONE = '/';
	public static final char MOLTIPLICA = '*';
	@JsonIgnore
	private char changeOperation = MOLTIPLICA;
	private java.sql.Timestamp inizio_validita_valuta;
	private java.sql.Timestamp fine_validita_valuta;

	@JsonIgnore
	private java.util.Collection sezionali;
	@JsonIgnore
	private java.util.Collection tipo_prestazioni;
	@JsonIgnore
	private java.util.Collection valute;
	@JsonIgnore
	private java.util.Collection banche_uo;
	@JsonIgnore
	private java.util.Collection modalita_uo;
	@JsonIgnore
	private java.util.Collection termini_uo;

	public final static String STATO_IVA_A = "A";
	public final static String STATO_IVA_B = "B";
	public final static String STATO_IVA_C = "C";

	@JsonIgnore
	private java.util.Vector dettagliCancellati = new Vector();
	@JsonIgnore
	private java.util.Vector documentiContabiliCancellati = new Vector();
	@JsonIgnore
	private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
	@JsonIgnore
	private boolean isDeleting = false;
	@JsonIgnore
	private String sezionaliFlag = SEZIONALI_FLAGS_ALL;

	private java.lang.String riportata = NON_RIPORTATO;
	private java.lang.String riportataInScrivania = NON_RIPORTATO;
	private Integer esercizioInScrivania;
	@JsonIgnore
	private String collegamentoDocumentale;
	@JsonIgnore
	private Boolean caricaDatiPerFatturazioneElettronica = true;

	/*
	 * Le variabili isDetailDoubled e isDocumentoModificabile servono per gestire il caso in cui l'utente
	 * non potendo modificare il documento procede solo a sdoppiare la riga di dettaglio. In tal caso la 
	 * procedura provvede a non rieffettuare la ricontabilizzazione in COAN e COGE.
	 *    
	 */
	@JsonIgnore
	private boolean isDetailDoubled = false; //serve per sapere se è stata sdoppiata una riga di dettaglio
	@JsonIgnore
	private boolean isDocumentoModificabile = true; //serve per sapere se il documento è modificabile o meno
	@JsonIgnore
	private java.sql.Timestamp dt_termine_creazione_docamm = null;
	@JsonIgnore
	private boolean isGestoreBancaFatturaAttiva;
	@JsonIgnore
	private boolean isIvaRecuperabile=true;
	@JsonIgnore
	private BulkList fattura_attiva_intrastatColl = new BulkList();
	@JsonIgnore
	private java.util.Collection modalita_trasportoColl = null;
	@JsonIgnore
	private java.util.Collection condizione_consegnaColl = null;
	@JsonIgnore
	private java.util.Collection modalita_erogazioneColl = null;
	@JsonIgnore
	private java.util.Collection modalita_incassoColl = null;
	@JsonIgnore
	private boolean isAttivoSplitPayment=false;

	public Fattura_attivaBulk() {
		super();
	}
	public Fattura_attivaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva);
	}
	public void addToCarichiInventarioHash(
			Buono_carico_scaricoBulk buonoCS,
			Fattura_attiva_rigaBulk rigaFattura) {

		if (carichiInventarioHash == null)
			carichiInventarioHash = new CarichiInventarioTable();
		Vector righeAssociate = (Vector)carichiInventarioHash.get(buonoCS);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			carichiInventarioHash.put(buonoCS, righeAssociate);
		}
		if (rigaFattura != null && !righeAssociate.contains(rigaFattura))
			righeAssociate.add(rigaFattura);
	}

	public static java.util.Dictionary getTipoFatturaKeys() {
		return tipoFatturaKeys;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
	public void addToDefferredSaldi(
			it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont, 
			java.util.Map values) {

		if (docCont != null) {
			if (deferredSaldi == null)
				deferredSaldi = new PrimaryKeyHashMap();
			if (!deferredSaldi.containsKey(docCont))
				deferredSaldi.put(docCont, values);
			else {
				Map firstValues = (Map)deferredSaldi.get(docCont);
				deferredSaldi.remove(docCont);
				deferredSaldi.put(docCont, firstValues);
			}
		}
	}

	public static java.util.Dictionary getStatoInvioSdiKeys() {
		return statoInvioSdiKeys;
	}

	public Object recuperoStatoInvioSdiKeys() {
		if (getStatoInvioSdi() != null){
			return getStatoInvioSdiKeys().get(getStatoInvioSdi());
		}
		return null;
	}

	public Boolean isFatturaElettronicaAllaFirma() {
		if (FATT_ELETT_ALLA_FIRMA.equals(getStatoInvioSdi())){
			return true;
		}
		return false;
	}

	public Boolean isFatturaElettronicaPredispostaAllaFirma() {
		if (FATT_ELETT_PREDISPOSTA_FIRMA.equals(getStatoInvioSdi())){
			return true;
		}
		return false;
	}

	public Boolean isFatturaElettronicaScartata() {
		if (FATT_ELETT_SCARTATA_DA_SDI.equals(getStatoInvioSdi())){
			return true;
		}
		return false;
	}

	public Boolean isFatturaElettronicaRifiutata() {
		if (FATT_ELETT_RIFIUTATA_DESTINATARIO.equals(getStatoInvioSdi())){
			return true;
		}
		return false;
	}

	public void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) {

		if (dettaglio != null && ((OggettoBulk)dettaglio).getCrudStatus() == OggettoBulk.NORMAL) {
			getDettagliCancellati().addElement(dettaglio);
			addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
		}
	}

	public void addToDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio) {

		if (dettaglio != null && ((OggettoBulk)dettaglio).getCrudStatus() == OggettoBulk.NORMAL &&
				!BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio))
			getDocumentiContabiliCancellati().addElement(dettaglio);
	}

	public void addToFattura_attiva_accertamentiHash(
			Accertamento_scadenzarioBulk scadenza,
			Fattura_attiva_rigaBulk rigaFattura) {

		if (fattura_attiva_accertamentiHash == null)
			fattura_attiva_accertamentiHash = new AccertamentiTable();
		Vector righeAssociate = (Vector)fattura_attiva_accertamentiHash.get(scadenza);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			//fattura_attiva_accertamentiHash.put(scadenza, righeAssociate);
			addToFattura_attiva_ass_totaliMap(scadenza, new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		}
		if (rigaFattura != null && !righeAssociate.contains(rigaFattura)) {
			righeAssociate.add(rigaFattura);
			//Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
			//e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
			if (fattura_attiva_accertamentiHash.containsKey(scadenza))
				fattura_attiva_accertamentiHash.remove(scadenza);
			//fattura_attiva_accertamentiHash.put(scadenza, righeAssociate);
		}
		fattura_attiva_accertamentiHash.put(scadenza, righeAssociate);

		if (getDocumentiContabiliCancellati() != null && 
				BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), scadenza))
			removeFromDocumentiContabiliCancellati(scadenza);
	}

	public void addToFattura_attiva_ass_totaliMap(
			Accertamento_scadenzarioBulk accertamento, java.math.BigDecimal totale) {

		if (fattura_attiva_ass_totaliMap == null)
			fattura_attiva_ass_totaliMap = new PrimaryKeyHashMap();
		fattura_attiva_ass_totaliMap.put(accertamento, totale);
	}
	public void addToFattura_attiva_ass_totaliMap(
			Obbligazione_scadenzarioBulk obbligazione, java.math.BigDecimal totale) {

		if (fattura_attiva_ass_totaliMap == null)
			fattura_attiva_ass_totaliMap = new PrimaryKeyHashMap();
		fattura_attiva_ass_totaliMap.put(obbligazione, totale);
	}

	public int addToFattura_attiva_consuntivoColl( Consuntivo_rigaVBulk os ) 
	{
		fattura_attiva_consuntivoColl.add(os);
		os.setDocumentoAmministrativo(this);

		return fattura_attiva_consuntivoColl.size()-1;
	}

	public int addToFattura_attiva_dettColl( Fattura_attiva_rigaBulk nuovoRigo ) 
	{
		nuovoRigo.setFattura_attiva(this);

		nuovoRigo.setStato_cofi(nuovoRigo.STATO_INIZIALE);
		nuovoRigo.setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

		long max = 0;
		for (Iterator i = fattura_attiva_dettColl.iterator(); i.hasNext();) {
			long prog = ((Fattura_attiva_rigaBulk)i.next()).getProgressivo_riga().longValue();
			if (prog > max) max = prog;
		}
		nuovoRigo.setProgressivo_riga(new Long(max+1));

		try {
			java.sql.Timestamp ts = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			nuovoRigo.setDt_da_competenza_coge((getDt_da_competenza_coge() == null)?ts : getDt_da_competenza_coge());
			nuovoRigo.setDt_a_competenza_coge((getDt_a_competenza_coge() == null)?ts : getDt_a_competenza_coge());
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}
		nuovoRigo.setFl_iva_forzata(Boolean.FALSE);
		nuovoRigo.setQuantita(new java.math.BigDecimal(1));
		nuovoRigo.setPrezzo_unitario(new java.math.BigDecimal(0));
		nuovoRigo.setIm_iva(new java.math.BigDecimal(0));
		nuovoRigo.setIm_totale_divisa(new java.math.BigDecimal(0));
		nuovoRigo.setInventariato(false);
		nuovoRigo.calcolaCampiDiRiga();
		if (!getTi_causale_emissione().equals(TARIFFARIO))
			nuovoRigo.setVoce_iva(new Voce_ivaBulk());
		nuovoRigo.setTariffario(new TariffarioBulk());

		fattura_attiva_dettColl.add(nuovoRigo);
		return fattura_attiva_dettColl.size()-1;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 12:27:15 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public AccertamentiTable getAccertamentiHash() {

		return getFattura_attiva_accertamentiHash();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca_uo() {
		return banca_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return java.util.Collection
	 */
	public java.util.Collection getBanche_uo() {
		return banche_uo;
	}
	public BulkCollection[] getBulkLists() {

		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] { 
				fattura_attiva_dettColl,
				fattura_attiva_intrastatColl
		};
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/3/2001 11:14:38 AM)
	 * @return it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable
	 */
	public CarichiInventarioTable getCarichiInventarioHash() {
		return carichiInventarioHash;
	}
	public java.lang.String getCd_divisa() {
		it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = this.getValuta();
		if (valuta == null)
			return null;
		return valuta.getCd_divisa();
	}
	public java.lang.Integer getCd_terzo() {
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk cliente = this.getCliente();
		if (cliente == null)
			return null;
		return cliente.getCd_terzo();
	}
	/* 
	 * Getter dell'attributo cd_terzo_uo_cds
	 */
	public java.lang.Integer getCd_terzo_uo_cds() {
		if (getTerzo_uo()==null)
			return null;
		return getTerzo_uo().getCd_terzo();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	public String getCd_tipo_doc_amm() {

		return Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA;
	}
	public java.lang.String getCd_tipo_sezionale() {
		it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = this.getTipo_sezionale();
		if (tipo_sezionale == null)
			return null;
		return tipo_sezionale.getCd_tipo_sezionale();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	public String getCd_uo() {

		return getCd_unita_organizzativa();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return char
	 */
	public char getChangeOperation() {
		return changeOperation;
	}
	@JsonIgnore
	public List getChildren() {
		return getFattura_attiva_dettColl();
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getCliente() {
		return cliente;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.docamm00.docs.bulk.Consuntivo_rigaVBulk
	 */
	public Consuntivo_rigaVBulk getConsuntivo_riga() {
		return consuntivo_riga;
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
	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
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
			for (Iterator i = deferredSaldi.keySet().iterator(); i.hasNext();) {
				IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
				if (((OggettoBulk)docCont).equalsByPrimaryKey((OggettoBulk)key))
					return key;
			}
		return null;	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 3:54:42 PM)
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
	@JsonIgnore
	public java.util.Vector getDettagliNonContabilizzati() {

		Vector dettagliNonContabilizzati = new Vector();
		if (getFattura_attiva_dettColl() != null) {
			for (Iterator i = getFattura_attiva_dettColl().iterator(); i.hasNext();) {
				Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk)i.next();
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
	@JsonIgnore
	public java.util.Vector getDettagliPagati() {
		Vector dettagliPagati = new Vector();
		if (getFattura_attiva_dettColl() != null) {
			for (Iterator i = getFattura_attiva_dettColl().iterator(); i.hasNext();) {
				Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk)i.next();
				if (dettaglio.STATO_PAGATO.equals(dettaglio.getStato_cofi()))
					dettagliPagati.add(dettaglio);
			}
		}
		return dettagliPagati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:31:04 PM)
	 * @return java.util.Vector
	 */
	@JsonIgnore
	public java.util.Vector getDocumentiContabiliCancellati() {
		return documentiContabiliCancellati;
	}
	@JsonIgnore
	public Class getDocumentoAmministrativoClassForDelete() {
		return Fattura_attiva_IBulk.class;
	}
	@JsonIgnore
	public Class getDocumentoContabileClassForDelete() {
		return it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk.class;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/07/2003 17.30.44)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getDt_termine_creazione_docamm() {
		return dt_termine_creazione_docamm;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/11/2004 13.39.36)
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEsercizioInScrivania() {
		return esercizioInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getExtra_ueKeys() {
		return extra_ueKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/10/2001 13.26.51)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk
	 */
	public Fattura_attivaBulk getFattura_attiva() {
		return fattura_attiva;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 11:20:07 AM)
	 * @return it.cnr.contab.docamm00.docs.bulk.AccertamentiTable
	 */
	public AccertamentiTable getFattura_attiva_accertamentiHash() {
		return fattura_attiva_accertamentiHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/23/2002 2:34:25 PM)
	 * @return java.util.Map
	 */
	public java.util.Map getFattura_attiva_ass_totaliMap() {
		return fattura_attiva_ass_totaliMap;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/14/2001 3:19:38 PM)
	 * @return java.util.Collection
	 */
	public java.util.Collection getFattura_attiva_consuntivoColl() {
		return fattura_attiva_consuntivoColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/07/2001 11.07.07)
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList<Fattura_attiva_rigaBulk> getFattura_attiva_dettColl() {
		return fattura_attiva_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getFine_validita_valuta() {
		return fine_validita_valuta;
	}
	public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

		if (importo == null) return null;
		return importo.negate();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 11:25:40 AM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImportoTotalePerAccertamento() {
		return importoTotalePerAccertamento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getInizio_validita_valuta() {
		return inizio_validita_valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getIntra_ueKeys() {
		return intra_ueKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento_uo() {
		return modalita_pagamento_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return java.util.Collection
	 */
	public java.util.Collection getModalita_uo() {
		return modalita_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/10/2001 14.27.01)
	 * @return int
	 */
	public int getNum_dettColl() {
		return num_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/14/2001 3:19:38 PM)
	 * @return java.util.Collection
	 */
	public ObbligazioniTable getObbligazioniHash() {
		return null;
	}
	public java.lang.Long getPg_banca_uo() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca_uo();
		if (banca == null)
			return null;
		return banca.getPg_banca();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	public Long getPg_doc_amm() {

		return getPg_fattura_attiva();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getRiferimenti_bancari() {
		return riferimenti_bancari;
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
	 * Creation date: (02/11/2004 14.34.09)
	 * @return java.lang.String
	 */
	public java.lang.String getRiportataInScrivania() {
		return riportataInScrivania;
	}
	/* 
	 * Getter dell'attributo riportata
	 */
	@JsonIgnore
	public Dictionary getRiportataKeys() {
		return STATI_RIPORTO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 17.21.44)
	 * @return java.util.Dictionary
	 */
	@JsonIgnore
	public final static java.util.Dictionary getSan_marinoKeys() {
		return san_marinoKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk
	 */
	@JsonIgnore
	public it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk getSezionale() {
		return sezionale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return java.util.Collection
	 */
	@JsonIgnore
	public java.util.Collection<Tipo_sezionaleBulk> getSezionali() {
		return sezionali;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/13/2002 10:36:39 AM)
	 * @return int
	 */
	@JsonIgnore
	public String getSezionaliFlag() {
		if (getFl_intra_ue() != null && getFl_intra_ue().booleanValue()) {
			setFl_extra_ue(Boolean.FALSE);
			setFl_san_marino(Boolean.FALSE);
			sezionaliFlag = SEZIONALI_FLAGS_IUE;
		} else if (getFl_extra_ue() != null && getFl_extra_ue().booleanValue()) {
			setFl_intra_ue(Boolean.FALSE);
			setFl_san_marino(Boolean.FALSE);
			sezionaliFlag = SEZIONALI_FLAGS_EUE;
		} else if (getFl_san_marino() != null && getFl_san_marino().booleanValue()) {
			setFl_intra_ue(Boolean.FALSE);
			setFl_extra_ue(Boolean.FALSE);
			sezionaliFlag = SEZIONALI_FLAGS_SM;
		} else {
			if (getFl_intra_ue() == null && 
					getFl_extra_ue() == null && 
					getFl_san_marino() == null) {
				sezionaliFlag = SEZIONALI_FLAGS_ALL;
			} else {
				sezionaliFlag = SEZIONALI_FLAGS_ORD;
			}
		}

		return sezionaliFlag;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/13/2002 10:36:39 AM)
	 * @return int
	 */
	@JsonIgnore
	public Dictionary getSezionaliFlags() {
		return SEZIONALI_FLAG_KEYS;
	}
	@JsonIgnore
	public Dictionary getStato_cofiKeys() {
		return STATO;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */
	@JsonIgnore
	public Dictionary getStato_cofiKeysForSearch() {

		it.cnr.jada.util.OrderedHashtable d = (it.cnr.jada.util.OrderedHashtable)getStato_cofiKeys();
		if (d == null) return null;

		it.cnr.jada.util.OrderedHashtable clone = (it.cnr.jada.util.OrderedHashtable)d.clone();
		clone.remove(STATO_INIZIALE);
		return clone;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	@JsonIgnore
	public String getStatoIVA() {
		return (getProtocollo_iva() == null ||
				getProtocollo_iva_generale() == null) ?
						"A" : "B";
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento_uo() {
		return termini_pagamento_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @return java.util.Collection
	 */
	public java.util.Collection getTermini_uo() {
		return termini_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/11/2001 16.11.00)
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_uo() {
		return terzo_uo;
	}
	/* 
	 * Getter dell'attributo ti_associato_manrev
	 */
	@JsonIgnore
	public Dictionary getTi_associato_manrevKeys() {
		return STATO_MANDATO;
	}
	/* 
	 * Getter dell'attributo ti_associato_manrev
	 */
	@JsonIgnore
	public Dictionary getTi_associato_manrevKeysForSearch() {
		return getTi_associato_manrevKeys();
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei motivi emissione.
	 *
	 * @return java.util.Dictionary
	 */
	@JsonIgnore
	public Dictionary getTi_causale_emissioneKeys() {
		return TMOTIVOEMISSIONE;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (06/11/2001 13.34.24)
	 * @return java.util.Collection
	 */
	public java.util.Collection getTipo_prestazioni() {
		return tipo_prestazioni;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk getTipo_sezionale() {
		return tipo_sezionale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getValuta() {
		return valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
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


		super.initialize(bp,context);

		if (getCd_uo_origine()==null)
			setCd_uo_origine(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

		return this;
	}
	public OggettoBulk initializeForEdit(CRUDBP bp,it.cnr.jada.action.ActionContext context) {


		super.initializeForEdit(bp,context);

		return this;
	}
	public OggettoBulk initializeForInsert(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);

		setStato_coan("N");
		setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
		unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_cds_origine(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());

		if (getEsercizio()==null)
			setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

		if (getFl_extra_ue()==null) setFl_extra_ue(Boolean.FALSE);
		if (getFl_san_marino()==null) setFl_san_marino(Boolean.FALSE);
		if (getFl_intra_ue()==null) setFl_intra_ue(Boolean.FALSE);
		if (getFl_liquidazione_differita()==null) setFl_liquidazione_differita(Boolean.FALSE);
		if (getFl_stampa()==null) setFl_stampa(Boolean.FALSE);
		setFl_congelata(Boolean.FALSE);

		if (getCambio()==null) setCambio(new java.math.BigDecimal("1"));


		setTi_causale_emissione(TARIFFARIO);

		if (getStato_cofi() == null)
			setStato_cofi("I");
		if(getFl_pagamento_anticipato()==null) setFl_pagamento_anticipato("N");
		return this;
	}
	public OggettoBulk initializeForSearch(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForSearch(bp,context);

		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_cds_origine(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());

		if (getEsercizio()==null)
			setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

		setFl_intra_ue(null);
		setFl_extra_ue(null);
		setFl_san_marino(null);
		//if (getFl_extra_ue()==null) setFl_extra_ue(Boolean.FALSE);
		//if (getFl_san_marino()==null) setFl_san_marino(Boolean.FALSE);
		//if (getFl_intra_ue()==null) setFl_intra_ue(Boolean.FALSE);
		//if (getFl_liquidazione_differita()==null) setFl_liquidazione_differita(Boolean.FALSE);
		if (getFl_stampa()==null) setFl_stampa(Boolean.FALSE);

		if (getCambio()==null) setCambio(new java.math.BigDecimal("1"));


		return this;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isAbledToInsertUOBank() {
		return getTerzo_uo() == null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isAbledToModifyTipoFattura() {
		return ! (getCrudStatus() == OggettoBulk.UNDEFINED ||
				(fattura_attiva_dettColl != null &&
				fattura_attiva_dettColl.isEmpty() &&
				fattura_attiva_intrastatColl != null &&
				fattura_attiva_intrastatColl.isEmpty()));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 * @return boolean
	 */
	public boolean isAnnullato() {
		return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
	}
	public boolean isCongelata() {

		return getFl_congelata() != null && getFl_congelata().booleanValue();
	}
	public boolean isContabilizzato() {
		return STATO_CONTABILIZZATO.equalsIgnoreCase(getStato_cofi());
	}
	/**
	 * Restituisce <code>true</code> se il sezionale è di tipo Istituzionale
	 *
	 * @return boolean
	 */

	public boolean isDeleting() {

		return isDeleting;
	}
	public boolean isEditable() {

		return !(isPagata() || 
				//isStampataSuRegistroIVA()) || //Modificato a seguito richiesta 423
				isAnnullato() ||
				isCongelata() ||
				(!((getEsercizio() != null && getEsercizioInScrivania() != null && 
				getEsercizio().intValue() == getEsercizioInScrivania().intValue())&& !isRiportata()) && 
				!isDeleting()));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public boolean isPagata() {

		return STATO_PAGATO.equalsIgnoreCase(getStato_cofi());
	}
	public boolean isRiportata() {

		return !NON_RIPORTATO.equals(riportata);
	}
	public boolean isRiportataInScrivania() {

		return !NON_RIPORTATO.equals(riportataInScrivania);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROcliente() {
		return isROClienteSearchTool() || getCliente() == null ||
				getCliente().getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROClienteCrudTool() {
		return isROClienteSearchTool();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROclienteForSearch() {
		return isROcliente();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROClienteSearchTool() {
		return 	(fattura_attiva_accertamentiHash != null && 
				!fattura_attiva_accertamentiHash.isEmpty()) ||
				(fattura_attiva_intrastatColl != null && 
				!fattura_attiva_intrastatColl.isEmpty())||
				isStampataSuRegistroIVA();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isRODateCompetenzaCOGE() {
		return getFattura_attiva_dettColl() != null &&
				!getFattura_attiva_dettColl().isEmpty();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROFl_liquidazione_differita() {
		return isStampataSuRegistroIVA();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROimporto() {
		return true;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	@JsonIgnore
	public boolean isROImportoTotalePerAccertamento() {
		return true;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:28:51 PM)
	 * @return java.util.Vector
	 */
	public boolean isStampataSuRegistroIVA() {
		return STATO_IVA_B.equalsIgnoreCase(getStatoIVA()) ||
				STATO_IVA_C.equalsIgnoreCase(getStatoIVA());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 */
	@JsonIgnore
	public boolean isVoidable() {
		// Gennaro Borriello - (02/11/2004 16.48.21)
		// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
		//	DA UN ES. PRECEDENTE a quello di scrivania.			
		return	( (isRiportataInScrivania() && !isRiportata()) ) ||
				(STATO_CONTABILIZZATO.equals(getStato_cofi()) && ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
				(STATO_CONTABILIZZATO.equals(getStato_cofi()) && PARZIALMENTE_ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
				(!NON_REGISTRATO_IN_COGE.equalsIgnoreCase(getStato_coge()) &&
						!NON_PROCESSARE_IN_COGE.equalsIgnoreCase(getStato_coge())) ||
						(!NON_CONTABILIZZATO_IN_COAN.equalsIgnoreCase(getStato_coan()) &&
								!NON_PROCESSARE_IN_COAN.equalsIgnoreCase(getStato_coan()));
	}
	public void removeFromCarichiInventarioHash(
			Buono_carico_scaricoBulk buonoCS,
			Fattura_attiva_rigaBulk rigaFattura) {

		if (carichiInventarioHash == null) return;
		Vector righeAssociate = (Vector)carichiInventarioHash.get(buonoCS);
		if (righeAssociate != null) {
			if (rigaFattura != null && righeAssociate.contains(rigaFattura))
				righeAssociate.remove(rigaFattura);
			if (righeAssociate.isEmpty())
				carichiInventarioHash.remove(buonoCS);
		}
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

	public int removeFromDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio) {

		if (BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio))
			getDocumentiContabiliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(),(OggettoBulk)dettaglio));
		return documentiContabiliCancellati.size()-1;
	}

	public void removeFromFattura_attiva_accertamentiHash(
			Fattura_attiva_rigaBulk rigaFattura) {

		Vector righeAssociate = (Vector)fattura_attiva_accertamentiHash.get(rigaFattura.getAccertamento_scadenzario());
		if (righeAssociate != null) {
			righeAssociate.remove(rigaFattura);
			if (righeAssociate.isEmpty()) {
				fattura_attiva_accertamentiHash.remove(rigaFattura.getAccertamento_scadenzario());
				addToDocumentiContabiliCancellati(rigaFattura.getAccertamento_scadenzario());
			}
		} else
			addToDocumentiContabiliCancellati(rigaFattura.getAccertamento_scadenzario());
	}

	public void removeFromFattura_attiva_ass_totaliMap(
			Accertamento_scadenzarioBulk scadenza) {

		fattura_attiva_ass_totaliMap.remove(scadenza);
	}

	public void removeFromFattura_attiva_ass_totaliMap(
			Obbligazione_scadenzarioBulk scadenza) {

		fattura_attiva_ass_totaliMap.remove(scadenza);
	}

	public boolean removeFromFattura_attiva_consuntivoColl(Consuntivo_rigaVBulk riga) 
	{

		return fattura_attiva_consuntivoColl.remove(riga);
	}

	public Fattura_attiva_rigaBulk removeFromFattura_attiva_dettColl(int indiceDiLinea) {

		Fattura_attiva_rigaBulk element= (Fattura_attiva_rigaBulk) fattura_attiva_dettColl.get(indiceDiLinea);
		addToDettagliCancellati(element);
		if (element != null && element.getAccertamento_scadenzario() != null)
			removeFromFattura_attiva_accertamentiHash(element);

		int righe_pagate= 0;
		int righe_contabilizzate= 0;
		int righe_ass_man_rev= 0;
		int righe= 0;
		Fattura_attiva_rigaBulk riga= null;
		for (java.util.Iterator i= this.getFattura_attiva_dettColl().iterator(); i.hasNext();) {
			riga= (Fattura_attiva_rigaBulk) i.next();
			if (!riga.equals(element)) {
				if (Fattura_attiva_rigaBulk.STATO_PAGATO.equals(riga.getStato_cofi()))
					righe_pagate++;
				if (Fattura_attiva_rigaBulk.STATO_CONTABILIZZATO.equals(riga.getStato_cofi()))
					righe_contabilizzate++;
				if (Fattura_attiva_rigaBulk.ASSOCIATO_A_MANDATO.equals(riga.getTi_associato_manrev()))
					righe_ass_man_rev++;
				righe++;
			}
		}
		if (righe==0 || (righe_pagate == 0 && righe_contabilizzate == 0))
			this.setStato_cofi(this.STATO_INIZIALE);
		else if (righe_pagate == righe)
			this.setStato_cofi(this.STATO_PAGATO);
		else if (righe_contabilizzate == righe)
			this.setStato_cofi(this.STATO_CONTABILIZZATO);
		else if (righe_pagate != 0)
			this.setStato_cofi(this.STATO_PARZIALE);

		if (righe==0 || righe_ass_man_rev==0)
			this.setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);
		else if (righe_ass_man_rev!=0 && righe_ass_man_rev<righe)
			this.setTi_associato_manrev(PARZIALMENTE_ASSOCIATO_A_MANDATO);
		else if (righe_ass_man_rev!=0 && righe_ass_man_rev==righe)
			this.setTi_associato_manrev(ASSOCIATO_A_MANDATO);

		return (Fattura_attiva_rigaBulk) fattura_attiva_dettColl.remove(indiceDiLinea);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (5/15/2002 10:50:29 AM)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 */
	public void resetDefferredSaldi() {

		deferredSaldi = null;	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newAnagrafico it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public void setAnagrafico(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
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
	 * Creation date: (13/05/2002 11.33.25)
	 * @return boolean
	 */
	public void setAnnullato(java.sql.Timestamp date) {

		setStato_cofi(STATO_ANNULLATO);
		setDt_cancellazione(date);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newBanca_uo it.cnr.contab.anagraf00.core.bulk.BancaBulk
	 */
	public void setBanca_uo(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca_uo) {
		banca_uo = newBanca_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newBanche_uo java.util.Collection
	 */
	public void setBanche_uo(java.util.Collection newBanche_uo) {
		banche_uo = newBanche_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/3/2001 11:14:38 AM)
	 * @param newCarichiInventarioHash it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable
	 */
	public void setCarichiInventarioHash(CarichiInventarioTable newCarichiInventarioHash) {
		carichiInventarioHash = newCarichiInventarioHash;
	}
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.getValuta().setCd_divisa(cd_divisa);
	}
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.getCliente().setCd_terzo(cd_terzo);
	}
	/* 
	 * Setter dell'attributo cd_terzo_uo_cds
	 */
	public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {

		getTerzo_uo().setCd_terzo(cd_terzo_uo_cds);

	}
	public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
		this.getTipo_sezionale().setCd_tipo_sezionale(cd_tipo_sezionale);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newChangeOperation char
	 */
	public void setChangeOperation(char newChangeOperation) {
		changeOperation = newChangeOperation;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newCliente it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setCliente(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newCliente) {
		cliente = newCliente;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newConsuntivo_riga it.cnr.contab.docamm00.docs.bulk.Consuntivo_rigaVBulk
	 */
	public void setConsuntivo_riga(Consuntivo_rigaVBulk newConsuntivo_riga) {
		consuntivo_riga = newConsuntivo_riga;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 3:54:42 PM)
	 * @param newDettagliCancellati java.util.Vector
	 */
	public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
		dettagliCancellati = newDettagliCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/15/2002 2:31:04 PM)
	 * @param newDocumentiContabiliCancellati java.util.Vector
	 */
	public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
		documentiContabiliCancellati = newDocumentiContabiliCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/07/2003 17.30.44)
	 * @param newDt_termine_creazione_docamm java.sql.Timestamp
	 */
	public void setDt_termine_creazione_docamm(java.sql.Timestamp newDt_termine_creazione_docamm) {
		dt_termine_creazione_docamm = newDt_termine_creazione_docamm;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/11/2004 13.39.36)
	 * @param newEsercizioInScrivania java.lang.Integer
	 */
	public void setEsercizioInScrivania(java.lang.Integer newEsercizioInScrivania) {
		esercizioInScrivania = newEsercizioInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/10/2001 13.26.51)
	 * @param newFattura_attiva it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk
	 */
	public void setFattura_attiva(Fattura_attivaBulk newFattura_attiva) {
		fattura_attiva = newFattura_attiva;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 11:20:07 AM)
	 * @param newFattura_attiva_accertamentiHash it.cnr.contab.docamm00.docs.bulk.AccertamentiTable
	 */
	public void setFattura_attiva_accertamentiHash(AccertamentiTable newFattura_attiva_accertamentiHash) {
		fattura_attiva_accertamentiHash = newFattura_attiva_accertamentiHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/23/2002 2:34:25 PM)
	 * @param newFattura_attiva_ass_totaliMap java.util.Map
	 */
	public void setFattura_attiva_ass_totaliMap(java.util.Map newFattura_attiva_ass_totaliMap) {
		fattura_attiva_ass_totaliMap = newFattura_attiva_ass_totaliMap;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/14/2001 3:19:38 PM)
	 * @param newFattura_attiva_consuntivoColl java.util.Collection
	 */
	public void setFattura_attiva_consuntivoColl(java.util.Collection newFattura_attiva_consuntivoColl) {
		fattura_attiva_consuntivoColl = newFattura_attiva_consuntivoColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newFattura_attiva_dettColl it.cnr.jada.bulk.BulkList
	 */
	public void setFattura_attiva_dettColl(it.cnr.jada.bulk.BulkList newFattura_attiva_dettColl) {
		fattura_attiva_dettColl = newFattura_attiva_dettColl;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newFine_validita_valuta java.sql.Timestamp
	 */
	public void setFine_validita_valuta(java.sql.Timestamp newFine_validita_valuta) {
		fine_validita_valuta = newFine_validita_valuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 11:25:40 AM)
	 * @param newImportoTotalePerAccertamento java.math.BigDecimal
	 */
	public void setImportoTotalePerAccertamento(java.math.BigDecimal newImportoTotalePerAccertamento) {
		importoTotalePerAccertamento = newImportoTotalePerAccertamento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newInizio_validita_valuta java.sql.Timestamp
	 */
	public void setInizio_validita_valuta(java.sql.Timestamp newInizio_validita_valuta) {
		inizio_validita_valuta = newInizio_validita_valuta;
	}
	/**
	 * Restituisce <code>true</code> se il sezionale è di tipo Istituzionale
	 *
	 * @return boolean
	 */

	public void setIsDeleting(boolean deleting) {

		isDeleting = deleting;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newModalita_pagamento_uo it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
	 */
	public void setModalita_pagamento_uo(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento_uo) {
		modalita_pagamento_uo = newModalita_pagamento_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newModalita_uo java.util.Collection
	 */
	public void setModalita_uo(java.util.Collection newModalita_uo) {
		modalita_uo = newModalita_uo;
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
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newRiferimenti_bancari it.cnr.jada.bulk.BulkList
	 */
	public void setRiferimenti_bancari(it.cnr.jada.bulk.BulkList newRiferimenti_bancari) {
		riferimenti_bancari = newRiferimenti_bancari;
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
	 * Creation date: (02/11/2004 14.34.09)
	 * @param newRiportataInScrivania java.lang.String
	 */
	public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
		riportataInScrivania = newRiportataInScrivania;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newSezionale it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk
	 */
	public void setSezionale(it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk newSezionale) {
		sezionale = newSezionale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newSezionali java.util.Collection
	 */
	public void setSezionali(java.util.Collection newSezionali) {
		sezionali = newSezionali;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/13/2002 10:36:39 AM)
	 * @param newSezionaliFlag int
	 */
	public void setSezionaliFlag(String newSezionaliFlag) {
		sezionaliFlag = newSezionaliFlag;

		switch (sezionaliFlag == null ? 99 : Integer.valueOf(sezionaliFlag).intValue()) {
		case 0	:	{
			setFl_intra_ue(null);
			setFl_extra_ue(null);
			setFl_san_marino(null);
			break;
		} 
		case 1	:	{
			setFl_intra_ue(Boolean.FALSE);
			setFl_extra_ue(Boolean.FALSE);
			setFl_san_marino(Boolean.FALSE);
			break;
		} 
		case 2	:	{
			setFl_intra_ue(Boolean.TRUE);
			setFl_extra_ue(Boolean.FALSE);
			setFl_san_marino(Boolean.FALSE);
			break;
		} 
		case 3	:	{
			setFl_intra_ue(Boolean.FALSE);
			setFl_extra_ue(Boolean.TRUE);
			setFl_san_marino(Boolean.FALSE);
			break;
		} 
		case 4	:	{
			setFl_intra_ue(Boolean.FALSE);
			setFl_extra_ue(Boolean.FALSE);
			setFl_san_marino(Boolean.TRUE);
			break;
		} 
		default: 	{
			setFl_intra_ue(null);
			setFl_extra_ue(null);
			setFl_san_marino(null);
		}
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newTermini_pagamento_uo it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
	 */
	public void setTermini_pagamento_uo(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento_uo) {
		termini_pagamento_uo = newTermini_pagamento_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/6/2002 10:45:04 AM)
	 * @param newTermini_uo java.util.Collection
	 */
	public void setTermini_uo(java.util.Collection newTermini_uo) {
		termini_uo = newTermini_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/11/2001 16.11.00)
	 * @param newTerzo_uo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setTerzo_uo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo_uo) {
		terzo_uo = newTerzo_uo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (06/11/2001 13.34.25)
	 * @param newTipo_prestazioni java.util.Collection
	 */
	public void setTipo_prestazioni(java.util.Collection newTipo_prestazioni) {
		tipo_prestazioni = newTipo_prestazioni;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newTipo_sezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
	 */
	public void setTipo_sezionale(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk newTipo_sezionale) {
		tipo_sezionale = newTipo_sezionale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
	 * @param newValuta it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public void setValuta(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newValuta) {
		valuta = newValuta;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/10/2001 14.41.50)
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
				if (this.getStato_cofi()!=null && this.getStato_cofi().equals(STATO_INIZIALE))
					if (getDt_registrazione().after(getDt_termine_creazione_docamm())) {
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
						throw new ValidationException("Non è possibile inserire documenti con competenza nell'anno precedente con data di registrazione successiva al " + sdf.format(getDt_termine_creazione_docamm()) + "!");
					}
			} else
				throw e;
		}

	}
	public void validate() throws ValidationException {

		if (getStato_cofi() == null)
			setStato_cofi("I");
		if (getTi_associato_manrev() == null)
			setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

		if (getDt_registrazione() == null)
			throw new ValidationException("Inserire la data di registrazione del documento!");

		java.util.Calendar limInf = null;
		java.util.Calendar limSup = null;
		java.util.Calendar today = getDateCalendar(null);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

		java.util.Calendar dataRegistrazione = getDateCalendar(getDt_registrazione());

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

		if (!RemoveAccent.isOk(getDs_fattura_attiva())){
			throw new ValidationException("La descrizione contienere caratteri speciali non supportati.");
		}

        super.validate();
	}
	public void addToAssociazioniInventarioHash(
			Ass_inv_bene_fatturaBulk ass,
			Fattura_attiva_rigaBulk rigaFattura) {

		if (associazioniInventarioHash == null)
			associazioniInventarioHash = new AssociazioniInventarioTable();
		Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
		if (righeAssociate == null) {
			righeAssociate = new Vector();
			associazioniInventarioHash.put(ass, righeAssociate);
		}
		if (rigaFattura != null && !righeAssociate.contains(rigaFattura))
			righeAssociate.add(rigaFattura);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/27/2002 10:36:59 AM)
	 * @return it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable
	 */
	public AssociazioniInventarioTable getAssociazioniInventarioHash() {
		return associazioniInventarioHash;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/27/2002 10:36:59 AM)
	 * @param newAssociazioniInventarioHash it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable
	 */
	public void setAssociazioniInventarioHash(AssociazioniInventarioTable newAssociazioniInventarioHash) {
		associazioniInventarioHash = newAssociazioniInventarioHash;
	}
	public Ass_inv_bene_fatturaBulk getAssociationWithInventarioFor(Fattura_attiva_rigaBulk rigaFattura) {

		if (associazioniInventarioHash == null || rigaFattura == null) return null;

		for (Enumeration e = associazioniInventarioHash.keys(); e.hasMoreElements();) {
			Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
			Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
			if (righeAssociate != null && !righeAssociate.isEmpty() &&
					BulkCollections.containsByPrimaryKey(righeAssociate, rigaFattura))
				return ass;
		}
		return null;
	}
	public void removeFromAssociazioniInventarioHash(
			Ass_inv_bene_fatturaBulk ass,
			Fattura_attiva_rigaBulk rigaFattura) {

		if (associazioniInventarioHash == null) return;
		Vector righeAssociate = (Vector)associazioniInventarioHash.get(ass);
		if (righeAssociate != null) {
			if (rigaFattura != null && righeAssociate.contains(rigaFattura))
				righeAssociate.remove(rigaFattura);
			if (righeAssociate.isEmpty())
				associazioniInventarioHash.remove(ass);
		}
	}
	public Boolean getHa_beniColl() {
		if (ha_beniColl!=null )
			return ha_beniColl;
		else
			return Boolean.FALSE;	
	}
	public void setHa_beniColl(Boolean ha_beniColl) {
		this.ha_beniColl = ha_beniColl;
	}
	@JsonIgnore
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
	@JsonIgnore
	public boolean isROModalita_pagamento() {
		//	if (isRiportata()&& isContabilizzato()) 
		return false;
		//	return true;
	}

	public void setIvaRecuperabile(boolean isIvaRecuperabile) {
		this.isIvaRecuperabile = isIvaRecuperabile;
	}
	@JsonIgnore
	public boolean isIvaRecuperabile() {
		return isIvaRecuperabile;
	}
	@JsonIgnore
	public Dictionary getTi_bene_servizioKeys() {
		return FATTURA_BENI_SERVIZI;
	}
	@JsonIgnore
	public Dictionary getStatoFattureElettronicheKeys() {
		return statoFattureElettronicheKeys;
	}
	@JsonIgnore
	public boolean isROTi_bene_servizio() {
		return	//isCommerciale() &&
				((getFl_extra_ue() != null && getFl_extra_ue().booleanValue()) ||
						(getFl_intra_ue() != null && getFl_intra_ue().booleanValue())) &&
						getFattura_attiva_dettColl() != null &&
						!getFattura_attiva_dettColl().isEmpty();
	}
	public int addToFattura_attiva_intrastatColl(Fattura_attiva_intraBulk dettaglio) {

		dettaglio.initialize();
		dettaglio.setFattura_attiva(this);

		long max = 0;
		for (Iterator i = fattura_attiva_dettColl.iterator(); i.hasNext();) {
			long prog = ((Fattura_attiva_rigaBulk)i.next()).getProgressivo_riga().longValue();
			if (prog > max) max = prog;
		}
		dettaglio.setPg_riga_intra(new Long(max+1));
		if (getCliente() != null && getCliente().getAnagrafico() != null &&
				getCliente().getAnagrafico().getComune_fiscale() != null) {
			NazioneBulk nazione = getCliente().getAnagrafico().getComune_fiscale().getNazione();
			dettaglio.setNazione_destinazione(nazione);
		}
		for (Iterator i = fattura_attiva_dettColl.iterator(); i.hasNext();) {
			Fattura_attiva_rigaBulk riga = ((Fattura_attiva_rigaBulk)i.next());
			if (riga.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
				dettaglio.setAmmontare_euro(dettaglio.getAmmontare_euro().add(riga.getIm_imponibile()));
		}
		dettaglio.setModalita_trasportoColl(getModalita_trasportoColl());
		dettaglio.setCondizione_consegnaColl(getCondizione_consegnaColl());
		dettaglio.setModalita_incassoColl(getModalita_incassoColl());
		dettaglio.setModalita_erogazioneColl(getModalita_erogazioneColl());

		fattura_attiva_intrastatColl.add(dettaglio);
		return fattura_attiva_intrastatColl.size()-1;
	}
	public Fattura_attiva_intraBulk removeFromFattura_attiva_intrastatColl(int index) {

		return (Fattura_attiva_intraBulk)fattura_attiva_intrastatColl.remove(index);
	}
	public java.util.Collection getModalita_trasportoColl() {
		return modalita_trasportoColl;
	}
	public void setModalita_trasportoColl(
			java.util.Collection modalita_trasportoColl) {
		this.modalita_trasportoColl = modalita_trasportoColl;
	}
	public java.util.Collection getCondizione_consegnaColl() {
		return condizione_consegnaColl;
	}
	public void setCondizione_consegnaColl(
			java.util.Collection condizione_consegnaColl) {
		this.condizione_consegnaColl = condizione_consegnaColl;
	}
	public java.util.Collection getModalita_erogazioneColl() {
		return modalita_erogazioneColl;
	}
	public void setModalita_erogazioneColl(
			java.util.Collection modalita_erogazioneColl) {
		this.modalita_erogazioneColl = modalita_erogazioneColl;
	}
	public java.util.Collection getModalita_incassoColl() {
		return modalita_incassoColl;
	}
	public void setModalita_incassoColl(java.util.Collection modalita_incassoColl) {
		this.modalita_incassoColl = modalita_incassoColl;
	}
	public BulkList getFattura_attiva_intrastatColl() {
		return fattura_attiva_intrastatColl;
	}
	public void setFattura_attiva_intrastatColl(
			BulkList fattura_attiva_intrastatColl) {
		this.fattura_attiva_intrastatColl = fattura_attiva_intrastatColl;
	}
	@JsonIgnore
	public String getSupplierNationType() {
		String cond = null;
		if (getFl_intra_ue() != null && getFl_intra_ue().booleanValue())
			cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.CEE;
		else if (getFl_extra_ue() != null && getFl_extra_ue().booleanValue())
			cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.EXTRA_CEE;
		else if (getFl_san_marino() != null && getFl_san_marino().booleanValue())
			cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.SAN_MARINO;
		else
			cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.ITALIA;
		return cond;
	}
	@JsonIgnore
	public boolean isAbledToModifyFlagsTipoFattura() {
		return isAbledToModifyTipoFattura() ||
				(getCliente() != null &&
				getCliente().getCrudStatus() == OggettoBulk.NORMAL);
	}
	public boolean hasIntrastatInviati() {
		if (getFattura_attiva_intrastatColl() != null && !getFattura_attiva_intrastatColl().isEmpty()) {
			for (java.util.Iterator i = getFattura_attiva_intrastatColl().iterator(); i.hasNext();) {
				Fattura_attiva_intraBulk fpr = (Fattura_attiva_intraBulk)i.next();
				if (fpr.getFl_inviato() != null && fpr.getFl_inviato().booleanValue()) {
					return true;
				}
			}
		}	
		return false;
	}
	public boolean isDocumentoFatturazioneElettronica() {
		return Optional.ofNullable(getFlFatturaElettronica()).orElse(Boolean.FALSE);
	}
	public String getCollegamentoDocumentale() {
		return collegamentoDocumentale;
	}
	public void setCollegamentoDocumentale(String collegamentoDocumentale) {
		this.collegamentoDocumentale = collegamentoDocumentale;
	}
	//public CMISFileFatturaAttiva getCMISFile(String typeName) throws IOException{
	//	CMISFileFatturaAttiva cmisFile = null;
	//	this.setTypeNameForCMIS(typeName);
	//	if (this.getFile()==null)
	//		cmisFile = new CMISFileFatturaAttiva(this);
	//	else 
	//		cmisFile = new CMISFileFatturaAttiva(this.getFile(), this.getNomeFile(), this);
	//	return cmisFile;
	//}
	//public CMISFile getCMISFile(Node node) {
	//	return new CMISFileFatturaAttiva(node, this);
	//}

	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(getTi_fattura());
		nomeFile = nomeFile.append("-"+this.getCd_unita_organizzativa());
		nomeFile = nomeFile.append("-"+this.getEsercizio().toString()+Utility.lpad(this.getPg_fattura_attiva().toString(),9,'0'));
		return nomeFile.toString();
	}

	public String recuperoIdFatturaAsString(){
		if (getProtocollo_iva() != null){
			return StrServ.replace(getCd_uo_origine(), ".", "")+getEsercizio()+StrServ.lpad(getCd_tipo_sezionale().substring(2), 4)+getTi_fattura()+StrServ.lpad(getProtocollo_iva().toString(),5);
		} 
		return StrServ.replace(getCd_unita_organizzativa(), ".", "")+getEsercizio()+StrServ.lpad(getPg_fattura_attiva().toString(), 5);
	}
	public boolean quadraturaInDeroga() {
		return isAttivoSplitPayment &&	(getFl_liquidazione_differita() != null && getFl_liquidazione_differita().booleanValue());
	}
	public boolean isAttivoSplitPayment() {
		return isAttivoSplitPayment;
	}
	public void setAttivoSplitPayment(boolean isAttivoSplitPayment) {
		this.isAttivoSplitPayment = isAttivoSplitPayment;
	}
	public void setCaricaDatiPerFatturazioneElettronica(
			Boolean caricaDatiPerFatturazioneElettronica) {
		this.caricaDatiPerFatturazioneElettronica = caricaDatiPerFatturazioneElettronica;
	}
	public Boolean isPossibleFatturazioneElettronica() {
		if (caricaDatiPerFatturazioneElettronica){
			return true;
		}
		return false;
	}
	public Boolean isNotaCreditoDaNonInviareASdi(){
		if (getNcAnnulloSdi() == null || getNcAnnulloSdi().equals("N")){
			return false;
		}
		return true;
	}
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;
	}
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(
			BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public Boolean isFatturaEstera(){
		return getFl_extra_ue() || getFl_intra_ue() || getFl_san_marino();
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public StorageObject getStorageObject() {
		return storageObject;
	}
	public void setStorageObject(StorageObject storageObject) {
		this.storageObject = storageObject;
	}
	public String getStatoFattElett() {
		return statoFattElett;
	}
	public void setStatoFattElett(String statoFattElett) {
		this.statoFattElett = statoFattElett;
	}
}
