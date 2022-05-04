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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAcquistoBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBase;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoStorePath;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import java.sql.Timestamp;
import java.util.*;

public abstract class Fattura_passivaBulk
        extends Fattura_passivaBase
        implements IDocumentoAmministrativoBulk,
        Voidable,
        IDefferUpdateSaldi,
        AllegatoParentBulk,
        AllegatoStorePath {

    public static final char DIVISIONE = '/';
    public static final char MOLTIPLICA = '*';
    public final static String STATO_IVA_A = "A";
    public final static String STATO_IVA_B = "B";
    public final static String STATO_IVA_C = "C";
    public final static String STATO_INIZIALE = "I";
    public final static String STATO_CONTABILIZZATO = "C";
    public final static String STATO_PARZIALE = "Q";
    public final static String STATO_PAGATO = "P";
    public final static String STATO_ANNULLATO = "A";
    public final static String NON_REGISTRATO_IN_COGE = "N";
    public final static String REGISTRATO_IN_COGE = "C";
    public final static String DA_RICONTABILIZZARE_IN_COGE = "R";
    public final static String NON_PROCESSARE_IN_COGE = "X";
    public final static String NON_CONTABILIZZATO_IN_COAN = "N";
    public final static String CONTABILIZZATO_IN_COAN = "C";
    public final static String DA_RICONTABILIZZARE_IN_COAN = "R";
    public final static String NON_PROCESSARE_IN_COAN = "X";
    public final static String NO_FONDO_ECO = "N";
    public final static String FONDO_ECO = "A";
    public final static String REGISTRATO_IN_FONDO_ECO = "R";
    public final static String NON_ASSOCIATO_A_MANDATO = "N";
    public final static String PARZIALMENTE_ASSOCIATO_A_MANDATO = "P";
    public final static String ASSOCIATO_A_MANDATO = "T";
    public final static String PROMISCUA = "P";
    public final static String TIPO_FATTURA_PASSIVA = "F";
    public final static String TIPO_NOTA_DI_CREDITO = "C";
    public final static String TIPO_NOTA_DI_DEBITO = "D";
    public final static String SEZIONALI_FLAGS_ALL = "0";
    public final static String SEZIONALI_FLAGS_ORD = "1";
    public final static String SEZIONALI_FLAGS_IUE = "2";
    public final static String SEZIONALI_FLAGS_EUE = "3";
    public final static String SEZIONALI_FLAGS_SMC = "4";
    public final static String SEZIONALI_FLAGS_SMS = "5";
    public final static String FATTURA_DI_SERVIZI = Bene_servizioBulk.SERVIZIO;
    public final static String FATTURA_DI_BENI = Bene_servizioBulk.BENE;
    public final static Dictionary TIPO;
    public final static Dictionary STATO;
    public final static Dictionary STATO_COGE;
    public final static Dictionary STATO_COAN;
    public final static Dictionary STATO_FONDO_ECO;
    public final static Dictionary STATO_MANDATO;
    public final static Dictionary SEZIONALI_FLAG_KEYS;
    public final static Dictionary FATTURA_BENI_SERVIZI;
    public final static Dictionary STATI_RIPORTO;
    public final static Dictionary STATO_LIQUIDAZIONE;
    public final static Dictionary CAUSALE;
    public final static java.util.Dictionary ti_bonifico_mezzoKeys = Lettera_pagam_esteroBulk.ti_bonifico_mezzoKeys,
            ti_ammontare_debitoKeys = Lettera_pagam_esteroBulk.ti_ammontare_debitoKeys,
            ti_commissione_speseKeys = Lettera_pagam_esteroBulk.ti_commissione_speseKeys,
            ti_divisaKeys = Lettera_pagam_esteroBulk.ti_divisaKeys;

    static {
        TIPO = new it.cnr.jada.util.OrderedHashtable();
        for (TipoIVA tipoIVA : TipoIVA.values()) {
            TIPO.put(tipoIVA.value(), tipoIVA.label());
        }
        TIPO.put(PROMISCUA, "Promiscua");

        STATO = new it.cnr.jada.util.OrderedHashtable();
        STATO.put(STATO_INIZIALE, "Iniziale");
        STATO.put(STATO_CONTABILIZZATO, "Contabilizzato");
        STATO.put(STATO_PARZIALE, "Parziale");
        STATO.put(STATO_PAGATO, "Pagato");
        STATO.put(STATO_ANNULLATO, "Annullato");

        STATO_MANDATO = new it.cnr.jada.util.OrderedHashtable();
        STATO_MANDATO.put(NON_ASSOCIATO_A_MANDATO, "Man/rev non associato");
        STATO_MANDATO.put(PARZIALMENTE_ASSOCIATO_A_MANDATO, "Parzialmente associato a man/rev");
        STATO_MANDATO.put(ASSOCIATO_A_MANDATO, "Man/rev associato");

        STATO_COGE = new it.cnr.jada.util.OrderedHashtable();
        STATO_COGE.put(NON_REGISTRATO_IN_COGE, "Non registrato");
        STATO_COGE.put(REGISTRATO_IN_COGE, "Registrato");
        STATO_COGE.put(NON_PROCESSARE_IN_COGE, "Non processare");

        STATO_COAN = new it.cnr.jada.util.OrderedHashtable();
        STATO_COAN.put(NON_CONTABILIZZATO_IN_COAN, "Non contabilizzato");
        STATO_COAN.put(CONTABILIZZATO_IN_COAN, "Contabilizzato");
        STATO_COAN.put(DA_RICONTABILIZZARE_IN_COAN, "Da contabilizzare nuovamente");
        STATO_COAN.put(NON_PROCESSARE_IN_COAN, "Non processare");

        STATO_FONDO_ECO = new it.cnr.jada.util.OrderedHashtable();
        STATO_FONDO_ECO.put(NO_FONDO_ECO, "Non usare fondo economale");
        STATO_FONDO_ECO.put(FONDO_ECO, "Usa fondo economale");
        STATO_FONDO_ECO.put(REGISTRATO_IN_FONDO_ECO, "Registrato in fondo economale");

        STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
        STATI_RIPORTO.put(NON_RIPORTATO, "Non riportata");
        STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO, "Parzialmente riportata");
        STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO, "Completamente riportata");

        SEZIONALI_FLAG_KEYS = new it.cnr.jada.util.OrderedHashtable();
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_ALL, "Tutte");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_ORD, "Ordinarie");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_IUE, "Intra UE");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_EUE, "Extra UE");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_SMC, "S. Marino con IVA");
        SEZIONALI_FLAG_KEYS.put(SEZIONALI_FLAGS_SMS, "S. Marino senza IVA");

        FATTURA_BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
        FATTURA_BENI_SERVIZI.put(FATTURA_DI_BENI, "Fattura di beni");
        FATTURA_BENI_SERVIZI.put(FATTURA_DI_SERVIZI, "Fattura di servizi");

        STATO_LIQUIDAZIONE = new it.cnr.jada.util.OrderedHashtable();
        STATO_LIQUIDAZIONE.put(LIQ, "Liquidabile");
        STATO_LIQUIDAZIONE.put(NOLIQ, "Non Liquidabile");
        STATO_LIQUIDAZIONE.put(SOSP, "Liquidazione sospesa");

        CAUSALE = new it.cnr.jada.util.OrderedHashtable();
        CAUSALE.put(ATTLIQ, "In attesa di liquidazione");
        CAUSALE.put(CONT, "Contenzioso");
        CAUSALE.put(ATTNC, "In attesa di nota credito");
    }
    protected Tipo_sezionaleBulk tipo_sezionale;
    protected DivisaBulk valuta;
    protected boolean defaultValuta = false;
    protected AnagraficoBulk anagrafico;
    protected TerzoBulk fornitore;
    protected TerzoBulk cessionario;
    protected BancaBulk banca;
    protected Rif_modalita_pagamentoBulk modalita_pagamento;
    protected Rif_termini_pagamentoBulk termini_pagamento;
    protected DocumentoEleTestataBulk documentoEleTestata;
    private BulkList fattura_passiva_dettColl = new BulkList();
    private BulkList fattura_passiva_intrastatColl = new BulkList();
    private BulkList riferimenti_bancari = new BulkList();
    private BulkList<DocumentoEleAcquistoBulk> docEleAcquistoColl = new BulkList<DocumentoEleAcquistoBulk>();
    private Collection fattura_passiva_consuntivoColl = new Vector();
    private ObbligazioniTable fattura_passiva_obbligazioniHash = null;
    private FatturaRigaOrdiniTable fatturaRigaOrdiniHash = null;
    private Map fattura_passiva_ass_totaliMap = null;
    private CarichiInventarioTable carichiInventarioHash = null;
    private AssociazioniInventarioTable associazioniInventarioHash = null;
    private char changeOperation = MOLTIPLICA;
    private java.sql.Timestamp inizio_validita_valuta;
    private java.sql.Timestamp fine_validita_valuta;
    /*
     * Le variabili isDetailDoubled e isDocumentoModificabile servono per gestire il caso in cui l'utente
	 * non potendo modificare il documento procede solo a sdoppiare la riga di dettaglio. In tal caso la
	 * procedura provvede a non rieffettuare la ricontabilizzazione in COAN e COGE.
	 *
	 */
    private boolean isDetailDoubled = false; //serve per sapere se ? stata sdoppiata una riga di dettaglio
    private boolean isDocumentoModificabile = true; //serve per sapere se il documento ? modificabile o meno
    private java.math.BigDecimal im_totale_fattura_calcolato = new java.math.BigDecimal(0);
    /* Le seguenti due collection servono per caricare i Tipi di Sezionale in modo selettivo:
	 * in sezionaliIstituzionali andr? il Sezionale, (o i Sezionali), che sar? visualizzato
	 * in caso di fattura di tipo Istituzionale;
	 * in sezionaliCommerciali andranno tutti i Sezionali che saranno presentati in
	 * caso di fattura di tipo Istituzionale.
	*/
    private java.util.Collection sezionali;
    private java.util.Collection valute;
    private java.util.Collection banche;
    private java.util.Collection modalita;
    private java.util.Collection termini;
    private Boolean ha_beniColl;
    private Boolean eseguito = new Boolean(false);
    private java.math.BigDecimal importoTotalePerObbligazione = new java.math.BigDecimal(0);
    private boolean autoFatturaNeeded = false;
    private boolean isDeleting = false;
    private AutofatturaBulk autofattura = null;
    ;
    private java.util.Vector dettagliCancellati = new Vector();
    private java.util.Vector documentiContabiliCancellati = new Vector();
    private Lettera_pagam_esteroBulk lettera_pagamento_estero = null;
    private String sezionaliFlag = SEZIONALI_FLAGS_ALL;
    private java.util.Collection modalita_trasportoColl = null;
    private java.util.Collection condizione_consegnaColl = null;
    private java.util.Collection modalita_erogazioneColl = null;
    private java.util.Collection modalita_incassoColl = null;
    private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
    private java.lang.String riportata = NON_RIPORTATO;
    private java.lang.String riportataInScrivania = NON_RIPORTATO;
    private Integer esercizioInScrivania;
    private boolean isIvaRecuperabile = true;
    private java.sql.Timestamp dataInizioObbligoRegistroUnico;
    private java.sql.Timestamp dataInizioFatturaElettronica;
    private java.sql.Timestamp dataInizioSplitPayment;
    private CompensoBulk compenso = null;
    private BulkList<DocumentoEleAllegatiBulk> docEleAllegatiColl = new BulkList<DocumentoEleAllegatiBulk>();
    private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
    private java.sql.Timestamp dt_termine_creazione_docamm = null;

    private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

    public Fattura_passivaBulk() {
        super();
    }

    public Fattura_passivaBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_fattura_passiva) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);
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

    public void addToAssociazioniInventarioHash(
            Ass_inv_bene_fatturaBulk ass,
            Fattura_passiva_rigaBulk rigaFattura) {

        if (associazioniInventarioHash == null)
            associazioniInventarioHash = new AssociazioniInventarioTable();
        Vector righeAssociate = (Vector) associazioniInventarioHash.get(ass);
        if (righeAssociate == null) {
            righeAssociate = new Vector();
            associazioniInventarioHash.put(ass, righeAssociate);
        }
        if (rigaFattura != null && !righeAssociate.contains(rigaFattura))
            righeAssociate.add(rigaFattura);
    }

    public void addToCarichiInventarioHash(
            Buono_carico_scaricoBulk buonoCS,
            Fattura_passiva_rigaBulk rigaFattura) {

        if (carichiInventarioHash == null)
            carichiInventarioHash = new CarichiInventarioTable();
        Vector righeAssociate = (Vector) carichiInventarioHash.get(buonoCS);
        if (righeAssociate == null) {
            righeAssociate = new Vector();
            carichiInventarioHash.put(buonoCS, righeAssociate);
        }
        if (rigaFattura != null && !righeAssociate.contains(rigaFattura))
            righeAssociate.add(rigaFattura);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/15/2002 10:50:29 AM)
     *
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
                Map firstValues = (Map) deferredSaldi.get(docCont);
                deferredSaldi.remove(docCont);
                deferredSaldi.put(docCont, firstValues);
            }
        }
    }

    public void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) {

        if (dettaglio != null && ((OggettoBulk) dettaglio).getCrudStatus() == OggettoBulk.NORMAL) {
            getDettagliCancellati().addElement(dettaglio);
            addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
        }
    }

    public void addToDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio) {

        if (dettaglio != null && ((OggettoBulk) dettaglio).getCrudStatus() == OggettoBulk.NORMAL &&
                !BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) dettaglio))
            getDocumentiContabiliCancellati().addElement(dettaglio);
    }

    public void addToFattura_passiva_ass_totaliMap(
            Accertamento_scadenzarioBulk accertamento, java.math.BigDecimal totale) {

        if (fattura_passiva_ass_totaliMap == null)
            fattura_passiva_ass_totaliMap = new PrimaryKeyHashMap();
        fattura_passiva_ass_totaliMap.put(accertamento, totale);
    }

    public void addToFattura_passiva_ass_totaliMap(
            Obbligazione_scadenzarioBulk obbligazione, java.math.BigDecimal totale) {

        if (fattura_passiva_ass_totaliMap == null)
            fattura_passiva_ass_totaliMap = new PrimaryKeyHashMap();
        fattura_passiva_ass_totaliMap.put(obbligazione, totale);
    }

    public int addToFattura_passiva_consuntivoColl(Consuntivo_rigaVBulk os) {
        fattura_passiva_consuntivoColl.add(os);
        os.setDocumentoAmministrativo(this);

        return fattura_passiva_consuntivoColl.size() - 1;
    }

    public int addToFattura_passiva_dettColl(Fattura_passiva_rigaBulk nuovoRigo) {

        nuovoRigo.setFattura_passiva(this);
        nuovoRigo.setStato_cofi(nuovoRigo.STATO_INIZIALE);
        nuovoRigo.setTi_associato_manrev(nuovoRigo.NON_ASSOCIATO_A_MANDATO);

        try {
            java.sql.Timestamp ts = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
            nuovoRigo.setDt_da_competenza_coge((getDt_da_competenza_coge() == null) ? ts : getDt_da_competenza_coge());
            nuovoRigo.setDt_a_competenza_coge((getDt_a_competenza_coge() == null) ? ts : getDt_a_competenza_coge());
        } catch (javax.ejb.EJBException e) {
            throw new it.cnr.jada.DetailedRuntimeException(e);
        }
        nuovoRigo.setTi_istituz_commerc(
                PROMISCUA.equalsIgnoreCase(getTi_istituz_commerc()) ?
                        TipoIVA.COMMERCIALE.value() : getTi_istituz_commerc());

        long max = 0;
        for (Iterator i = fattura_passiva_dettColl.iterator(); i.hasNext(); ) {
            long prog = ((Fattura_passiva_rigaBulk) i.next()).getProgressivo_riga().longValue();
            if (prog > max) max = prog;
        }
        nuovoRigo.setProgressivo_riga(new Long(max + 1));

        nuovoRigo.setFl_iva_forzata(Boolean.FALSE);
        nuovoRigo.setQuantita(new java.math.BigDecimal(1));
        nuovoRigo.setPrezzo_unitario(new java.math.BigDecimal(0));
        nuovoRigo.setInventariato(false);

        nuovoRigo.calcolaCampiDiRiga();

        nuovoRigo.setFornitore(this.getFornitore());
        nuovoRigo.setTermini(this.getTermini());
        nuovoRigo.setTermini_pagamento(this.getTermini_pagamento());
        nuovoRigo.setModalita(this.getModalita());
        nuovoRigo.setModalita_pagamento(this.getModalita_pagamento());
        nuovoRigo.setBanche(this.getBanche());
        nuovoRigo.setBanca(this.getBanca());
        nuovoRigo.setCessionario(this.getCessionario());

        fattura_passiva_dettColl.add(nuovoRigo);
        setToBeCreated();

        return fattura_passiva_dettColl.size() - 1;
    }

    public int addToFattura_passiva_intrastatColl(Fattura_passiva_intraBulk dettaglio) {

        dettaglio.initialize();
        dettaglio.setFattura_passiva(this);

        long max = 0;
        for (Iterator i = fattura_passiva_dettColl.iterator(); i.hasNext(); ) {
            long prog = ((Fattura_passiva_rigaBulk) i.next()).getProgressivo_riga().longValue();
            if (prog > max) max = prog;
        }
        dettaglio.setPg_riga_intra(new Long(max + 1));
        if (getFornitore() != null && getFornitore().getAnagrafico() != null &&
                getFornitore().getAnagrafico().getComune_fiscale() != null) {
            NazioneBulk nazione = getFornitore().getAnagrafico().getComune_fiscale().getNazione();
            if (getTi_bene_servizio().compareTo(Bene_servizioBulk.BENE) == 0) {
                dettaglio.setNazione_origine(nazione);
                dettaglio.setNazione_provenienza(nazione);
            } else
                dettaglio.setNazione_provenienza(nazione);
        }
        //if (fatturaPassiva != null && fatturaPassiva.getFl_intra_ue() != null &&
        //fatturaPassiva.getFl_intra_ue().booleanValue())	{
        for (Iterator i = fattura_passiva_dettColl.iterator(); i.hasNext(); ) {
            Fattura_passiva_rigaBulk riga = ((Fattura_passiva_rigaBulk) i.next());
            if (riga.getBene_servizio().getFl_obb_intrastat_acq().booleanValue())
                dettaglio.setAmmontare_euro(dettaglio.getAmmontare_euro().add(riga.getIm_imponibile()));
            if (!this.isDefaultValuta() &&
                    ((this.getFornitore() != null &&
                            this.getFornitore().getAnagrafico() != null &&
                            this.getFornitore().getAnagrafico().getNazionalita() != null &&
                            this.getFornitore().getAnagrafico().getNazionalita().getDivisa() != null) ||
                            this.getFornitore().getAnagrafico().getComune_fiscale() != null &&
                                    this.getFornitore().getAnagrafico().getComune_fiscale().getNazione() != null &&
                                    this.getFornitore().getAnagrafico().getComune_fiscale().getNazione().getDivisa() != null)) {
                dettaglio.setAmmontare_divisa(dettaglio.getAmmontare_divisa().add(riga.getIm_totale_divisa()));
            }
        }
        dettaglio.setModalita_trasportoColl(getModalita_trasportoColl());
        dettaglio.setCondizione_consegnaColl(getCondizione_consegnaColl());
        dettaglio.setModalita_incassoColl(getModalita_incassoColl());
        dettaglio.setModalita_erogazioneColl(getModalita_erogazioneColl());

        fattura_passiva_intrastatColl.add(dettaglio);
        return fattura_passiva_intrastatColl.size() - 1;
    }

    public void addToFattura_passiva_obbligazioniHash(
            Obbligazione_scadenzarioBulk obbligazione,
            Fattura_passiva_rigaBulk rigaFattura) throws ApplicationException {
        if (!Optional.ofNullable(rigaFattura).isPresent()) {
            throw new ApplicationException("La riga di fattura non è stata selezionata, oppure è già stata contabilizzata!");
        }
    	obbligazione.setCig(rigaFattura.getCig());
    	obbligazione.setMotivo_assenza_cig(rigaFattura.getMotivo_assenza_cig());
        if (fattura_passiva_obbligazioniHash == null)
            fattura_passiva_obbligazioniHash = new ObbligazioniTable();
        Vector righeAssociate = (Vector) fattura_passiva_obbligazioniHash.get(obbligazione);
        if (righeAssociate == null) {
            righeAssociate = new Vector();
            //fattura_passiva_obbligazioniHash.put(obbligazione, righeAssociate);
            addToFattura_passiva_ass_totaliMap(obbligazione, new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        if (rigaFattura != null && !righeAssociate.contains(rigaFattura)) {
            righeAssociate.add(rigaFattura);
            //Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
            //e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
            if (fattura_passiva_obbligazioniHash.containsKey(obbligazione))
                fattura_passiva_obbligazioniHash.remove(obbligazione);
            //fattura_passiva_obbligazioniHash.put(obbligazione, righeAssociate);
        }
        fattura_passiva_obbligazioniHash.put(obbligazione, righeAssociate);

        if (getDocumentiContabiliCancellati() != null &&
                BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), obbligazione))
            removeFromDocumentiContabiliCancellati(obbligazione);
    }

    public void addToFatturaRigaOrdiniHash(
            Fattura_passiva_rigaBulk rigaFattura,
            FatturaOrdineBulk fatturaOrdineBulk) {
        fatturaRigaOrdiniHash = Optional.ofNullable(fatturaRigaOrdiniHash)
                                    .orElseGet(() -> new FatturaRigaOrdiniTable());
        rigaFattura.addToFatturaOrdineColl(fatturaOrdineBulk);
        fatturaRigaOrdiniHash.put(rigaFattura, rigaFattura.getFatturaOrdineColl());
    }

    public int addToRiferimenti_bancari(Fattura_passiva_rigaBulk os) {
        riferimenti_bancari.add(os);
        os.setFattura_passiva(this);

        return riferimenti_bancari.size() - 1;
    }

    public void aggiornaImportiTotali() {

        java.math.BigDecimal imp = new java.math.BigDecimal(0);
        imp = imp.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal iva = new java.math.BigDecimal(0);
        iva = iva.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal totale = new java.math.BigDecimal(0);
        totale = totale.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal totaleImponibileDivisa = new java.math.BigDecimal(0);
        totaleImponibileDivisa = totaleImponibileDivisa.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

        if (getFattura_passiva_dettColl() != null)
            for (Iterator i = getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) i.next();
                if (!riga.isAnnullato()) {
                    riga.calcolaCampiDiRiga();
                    totaleImponibileDivisa = totaleImponibileDivisa.add(riga.getIm_totale_divisa());
                    imp = imp.add(riga.getIm_imponibile());
                    iva = iva.add(riga.getIm_iva());
                    totale = totale.add(riga.getIm_imponibile().add(riga.getIm_iva()));
                }
            }

        if (getIm_importo_totale_fattura_fornitore_euro() == null)
            setIm_importo_totale_fattura_fornitore_euro(new java.math.BigDecimal(0));
        setIm_totale_imponibile(imp);
        setIm_totale_iva(iva);
        setIm_totale_fattura_calcolato(totale);
        setIm_totale_imponibile_divisa(totaleImponibileDivisa);
    }

    public void calcolaTotaleFatturaFornitoreInEur() {

        if (getValuta() == null)
            return;
        java.math.BigDecimal importoTotale = getIm_totale_fattura();
        if (importoTotale == null)
            importoTotale = new java.math.BigDecimal(0);
        else {
            java.math.BigDecimal cambioImpostato = getCambio();
            if (cambioImpostato == null) {
                cambioImpostato = new java.math.BigDecimal(0).setScale(0, java.math.BigDecimal.ROUND_HALF_UP);
                setCambio(cambioImpostato);
            }
            importoTotale = (getChangeOperation() == MOLTIPLICA) ?
                    importoTotale.multiply(cambioImpostato) :
                    importoTotale.divide(cambioImpostato, java.math.BigDecimal.ROUND_HALF_UP);
        }
        importoTotale = importoTotale.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        setIm_importo_totale_fattura_fornitore_euro(importoTotale);
    }

    public boolean existARowToBeInventoried() {

        if (getFattura_passiva_dettColl() != null) {
            Iterator dettagli = getFattura_passiva_dettColl().iterator();
            while (dettagli.hasNext()) {
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) dettagli.next();
                if (riga.getBene_servizio() != null &&
                        riga.getBene_servizio().getFl_gestione_inventario() != null &&
                        riga.getBene_servizio().getFl_gestione_inventario().booleanValue())
                    return true;
            }
        }
        return false;
    }

    public boolean existARowInventoried() {
        if (getFattura_passiva_dettColl() != null) {
            Iterator dettagli = getFattura_passiva_dettColl().iterator();
            while (dettagli.hasNext()) {
                if (((Fattura_passiva_rigaBulk) dettagli.next()).isInventariato())
                    return true;
            }
        }
        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/07/2001 11.07.07)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public AccertamentiTable getAccertamentiHash() {

        return null;
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
     */
    public AnagraficoBulk getAnagrafico() {
        return anagrafico;
    }

    /**
     * @return void
     */
    public void setAnagrafico(AnagraficoBulk newAnagrafico) {
        anagrafico = newAnagrafico;
    }

    public Ass_inv_bene_fatturaBulk getAssociationWithInventarioFor(Fattura_passiva_rigaBulk rigaFattura) {

        if (associazioniInventarioHash == null || rigaFattura == null) return null;

        for (Enumeration e = associazioniInventarioHash.keys(); e.hasMoreElements(); ) {
            Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) e.nextElement();
            Vector righeAssociate = (Vector) associazioniInventarioHash.get(ass);
            if (righeAssociate != null && !righeAssociate.isEmpty() &&
                    BulkCollections.containsByPrimaryKey(righeAssociate, rigaFattura))
                return ass;
        }
        return null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/27/2002 10:36:59 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable
     */
    public AssociazioniInventarioTable getAssociazioniInventarioHash() {
        return associazioniInventarioHash;
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/27/2002 10:36:59 AM)
     *
     * @param newAssociazioniInventarioHash it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable
     */
    public void setAssociazioniInventarioHash(AssociazioniInventarioTable newAssociazioniInventarioHash) {
        associazioniInventarioHash = newAssociazioniInventarioHash;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/13/2002 10:53:38 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk
     */
    public AutofatturaBulk getAutofattura() {
        return autofattura;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/13/2002 10:53:38 AM)
     *
     * @param newAutofattura it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk
     */
    public void setAutofattura(AutofatturaBulk newAutofattura) {
        autofattura = newAutofattura;
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public BancaBulk getBanca() {
        return banca;
    }

    /**
     * @return void
     */
    public void setBanca(BancaBulk newBanca) {
        banca = newBanca;
    }

    /**
     * Restituisce la <code>Collection</code> contenente l'elenco di banche
     * relativi al terzo selezionato
     *
     * @return java.util.Collection
     */

    public java.util.Collection getBanche() {
        return banche;
    }

    /**
     * Imposta la <code>Collection</code> contenente l'elenco di banche
     * relativi al terzo selezionato
     *
     * @param newBanche <code>java.util.Collection</code>
     */

    public void setBanche(java.util.Collection newBanche) {
        banche = newBanche;
    }


    public BulkCollection[] getBulkLists() {

        // Metti solo le liste di oggetti che devono essere resi persistenti

        return new it.cnr.jada.bulk.BulkCollection[]{
                fattura_passiva_dettColl,
                fattura_passiva_intrastatColl,
                riferimenti_bancari,
                docEleAllegatiColl
        };
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/3/2001 11:14:38 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable
     */
    public CarichiInventarioTable getCarichiInventarioHash() {
        return carichiInventarioHash;
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/3/2001 11:14:38 AM)
     *
     * @param newCarichiInventarioHash it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable
     */
    public void setCarichiInventarioHash(CarichiInventarioTable newCarichiInventarioHash) {
        carichiInventarioHash = newCarichiInventarioHash;
    }

    public java.lang.String getCd_divisa() {
        it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = this.getValuta();
        if (valuta == null)
            return null;
        return valuta.getCd_divisa();
    }

    public void setCd_divisa(java.lang.String cd_divisa) {
        this.getValuta().setCd_divisa(cd_divisa);
    }

    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
        if (modalita_pagamento == null)
            return null;
        return modalita_pagamento.getCd_modalita_pag();
    }

    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    public java.lang.String getCd_termini_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
        if (termini_pagamento == null)
            return null;
        return termini_pagamento.getCd_termini_pag();
    }

    public void setCd_termini_pag(java.lang.String cd_termini_pag) {
        this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
    }

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = this.getFornitore();
        if (fornitore == null)
            return null;
        return fornitore.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.getFornitore().setCd_terzo(cd_terzo);
    }

    public java.lang.Integer getCd_terzo_cessionario() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk cessionario = this.getCessionario();
        if (cessionario == null)
            return null;
        return cessionario.getCd_terzo();
    }

    public void setCd_terzo_cessionario(java.lang.Integer cd_terzo_cessionario) {
        this.getCessionario().setCd_terzo(cd_terzo_cessionario);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public java.lang.String getCd_tipo_doc_amm() {

        return Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA;
    }

    public java.lang.String getCd_tipo_sezionale() {
        it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = this.getTipo_sezionale();
        if (tipo_sezionale == null)
            return null;
        return tipo_sezionale.getCd_tipo_sezionale();
    }

    public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
        this.getTipo_sezionale().setCd_tipo_sezionale(cd_tipo_sezionale);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public java.lang.String getCd_uo() {

        return getCd_unita_organizzativa();
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/11/2002 1:51:27 PM)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getCessionario() {
        return cessionario;
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/11/2002 1:51:27 PM)
     *
     * @param newCessionario it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public void setCessionario(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newCessionario) {
        cessionario = newCessionario;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/2/2001 4:24:22 PM)
     *
     * @return char
     */
    public char getChangeOperation() {
        return changeOperation;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/2/2001 4:24:22 PM)
     *
     * @param newChangeOperation char
     */
    public void setChangeOperation(char newChangeOperation) {
        changeOperation = newChangeOperation;
    }

    public List getChildren() {

        return getFattura_passiva_dettColl();
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/5/2002 12:00:26 PM)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getCondizione_consegnaColl() {
        return condizione_consegnaColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/5/2002 12:00:26 PM)
     *
     * @param newCondizione_consegnaColl java.util.Collection
     */
    public void setCondizione_consegnaColl(java.util.Collection newCondizione_consegnaColl) {
        condizione_consegnaColl = newCondizione_consegnaColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/15/2002 10:50:29 AM)
     */
    public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() {
        return deferredSaldi;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/15/2002 10:50:29 AM)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {

        if (docCont != null && deferredSaldi != null)
            for (Iterator i = deferredSaldi.keySet().iterator(); i.hasNext(); ) {
                IDocumentoContabileBulk key = (IDocumentoContabileBulk) i.next();
                if (((OggettoBulk) docCont).equalsByPrimaryKey((OggettoBulk) key))
                    return key;
            }
        return null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:02:34 PM)
     *
     * @return java.lang.String
     */
    public abstract String getDescrizioneEntita();

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:02:34 PM)
     *
     * @return java.lang.String
     */
    public abstract String getDescrizioneEntitaPlurale();

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDettagliCancellati() {
        return dettagliCancellati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @param newDettagliCancellati java.util.Vector
     */
    public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
        dettagliCancellati = newDettagliCancellati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDettagliNonContabilizzati() {

        Vector dettagliNonContabilizzati = new Vector();
        if (getFattura_passiva_dettColl() != null) {
            for (Iterator i = getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                if (dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi()))
                    dettagliNonContabilizzati.add(dettaglio);
            }
        }
        return dettagliNonContabilizzati;
    }

    public java.util.Vector getDettagliContabilizzati() {

        Vector dettagliContabilizzati = new Vector();
        if (getFattura_passiva_dettColl() != null) {
            for (Iterator i = getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                if (dettaglio.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
                    dettagliContabilizzati.add(dettaglio);
            }
        }
        return dettagliContabilizzati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDettagliPagati() {

        Vector dettagliPagati = new Vector();
        if (getFattura_passiva_dettColl() != null) {
            for (Iterator i = getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                if (dettaglio.STATO_PAGATO.equals(dettaglio.getStato_cofi()))
                    dettagliPagati.add(dettaglio);
            }
        }
        return dettagliPagati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:31:04 PM)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDocumentiContabiliCancellati() {
        return documentiContabiliCancellati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:31:04 PM)
     *
     * @param newDocumentiContabiliCancellati java.util.Vector
     */
    public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
        documentiContabiliCancellati = newDocumentiContabiliCancellati;
    }

    public Class getDocumentoAmministrativoClassForDelete() {

        return Fattura_passiva_IBulk.class;
    }

    public Class getDocumentoContabileClassForDelete() {

        return AccertamentoOrdBulk.class;
    }

    public java.lang.Integer getEsercizio_lettera() {
        it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera_pagamento_estero = this.getLettera_pagamento_estero();
        if (lettera_pagamento_estero == null)
            return null;
        return lettera_pagamento_estero.getEsercizio();
    }

    public void setEsercizio_lettera(java.lang.Integer esercizio_lettera) {
        this.getLettera_pagamento_estero().setEsercizio(esercizio_lettera);
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/11/2004 13.40.35)
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getEsercizioInScrivania() {
        return esercizioInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/11/2004 13.40.35)
     *
     * @param newEsercizioInScrivania java.lang.Integer
     */
    public void setEsercizioInScrivania(java.lang.Integer newEsercizioInScrivania) {
        esercizioInScrivania = newEsercizioInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/7/2002 2:15:19 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public Map getFattura_passiva_ass_totaliMap() {
        return fattura_passiva_ass_totaliMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/7/2002 2:15:19 PM)
     *
     * @param newFattura_passiva_ass_totaliMap it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public void setFattura_passiva_ass_totaliMap(Map newFattura_passiva_ass_totaliMap) {
        fattura_passiva_ass_totaliMap = newFattura_passiva_ass_totaliMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/07/2001 11.07.07)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public Collection getFattura_passiva_consuntivoColl() {

        return fattura_passiva_consuntivoColl;
    }

    /**
     * Imposta la <code>Collection</code> contenente l'elenco di modalita di pagamento
     * relativi al terzo selezionato
     *
     * @param newConsuntivo <code>java.util.Collection</code>
     */

    public void setFattura_passiva_consuntivoColl(Collection newConsuntivo) {
        fattura_passiva_consuntivoColl = newConsuntivo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/07/2001 11.07.07)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Fattura_passiva_rigaBulk> getFattura_passiva_dettColl() {
        return fattura_passiva_dettColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/28/2001 12:02:07 PM)
     *
     * @param newFattura_passiva_dettColl it.cnr.jada.bulk.BulkList
     */
    public void setFattura_passiva_dettColl(it.cnr.jada.bulk.BulkList newFattura_passiva_dettColl) {
        fattura_passiva_dettColl = newFattura_passiva_dettColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/2/2002 3:48:07 PM)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getFattura_passiva_intrastatColl() {
        return fattura_passiva_intrastatColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/2/2002 3:48:07 PM)
     *
     * @param newFattura_passiva_intrastatColl it.cnr.jada.bulk.BulkList
     */
    public void setFattura_passiva_intrastatColl(it.cnr.jada.bulk.BulkList newFattura_passiva_intrastatColl) {
        fattura_passiva_intrastatColl = newFattura_passiva_intrastatColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/18/2001 12:32:22 PM)
     *
     * @return java.util.Hashtable
     */
    public ObbligazioniTable getFattura_passiva_obbligazioniHash() {
        return fattura_passiva_obbligazioniHash;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/18/2001 12:32:22 PM)
     *
     * @param newFattura_passiva_obbligazioniHash java.util.Hashtable
     */
    public void setFattura_passiva_obbligazioniHash(ObbligazioniTable newFattura_passiva_obbligazioniHash) {
        fattura_passiva_obbligazioniHash = newFattura_passiva_obbligazioniHash;
    }

    public FatturaRigaOrdiniTable getFatturaRigaOrdiniHash() {
        return fatturaRigaOrdiniHash;
    }

    public void setFatturaRigaOrdiniHash(FatturaRigaOrdiniTable fatturaRigaOrdiniHash) {
        this.fatturaRigaOrdiniHash = fatturaRigaOrdiniHash;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/19/2001 11:53:57 AM)
     *
     * @return java.sql.Timestamp
     */
    public java.sql.Timestamp getFine_validita_valuta() {
        return fine_validita_valuta;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/19/2001 11:53:57 AM)
     *
     * @param newFine_validita_valuta java.sql.Timestamp
     */
    public void setFine_validita_valuta(java.sql.Timestamp newFine_validita_valuta) {
        fine_validita_valuta = newFine_validita_valuta;
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public TerzoBulk getFornitore() {
        return fornitore;
    }

    /**
     * @return void
     */
    public void setFornitore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newFornitore) {
        fornitore = newFornitore;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 3:21:17 PM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_importo_totale_fattura_fornitore_euro() {

        //Questo get ? rimasto per compatibilit?
        return getIm_totale_quadratura();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 3:21:17 PM)
     *
     * @param newIm_importo_totale_fattura_fornitore_euro java.math.BigDecimal
     */
    public void setIm_importo_totale_fattura_fornitore_euro(java.math.BigDecimal newIm_importo_totale_fattura_fornitore_euro) {

        //Questo get ? rimasto per compatibilit?
        setIm_totale_quadratura(newIm_importo_totale_fattura_fornitore_euro);
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:36:06 PM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_totale_fattura_calcolato() {
        return im_totale_fattura_calcolato;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:36:06 PM)
     *
     * @param newIm_totale_fattura_calcolato java.math.BigDecimal
     */
    public void setIm_totale_fattura_calcolato(java.math.BigDecimal newIm_totale_fattura_calcolato) {
        im_totale_fattura_calcolato = newIm_totale_fattura_calcolato;
    }

    public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

        if (importo == null) return null;
        return importo.negate();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/26/2001 4:49:29 PM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getImportoTotalePerObbligazione() {
        return importoTotalePerObbligazione;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/26/2001 4:49:29 PM)
     *
     * @param newImportoTotalePerObbligazione java.math.BigDecimal
     */
    public void setImportoTotalePerObbligazione(java.math.BigDecimal newImportoTotalePerObbligazione) {
        importoTotalePerObbligazione = newImportoTotalePerObbligazione;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/19/2001 11:53:57 AM)
     *
     * @return java.sql.Timestamp
     */
    public java.sql.Timestamp getInizio_validita_valuta() {
        return inizio_validita_valuta;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/19/2001 11:53:57 AM)
     *
     * @param newInizio_validita_valuta java.sql.Timestamp
     */
    public void setInizio_validita_valuta(java.sql.Timestamp newInizio_validita_valuta) {
        inizio_validita_valuta = newInizio_validita_valuta;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/22/2002 11:12:29 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk
     */
    public Lettera_pagam_esteroBulk getLettera_pagamento_estero() {
        return lettera_pagamento_estero;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/22/2002 11:12:29 AM)
     *
     * @param newLetteraPagamentoEstero it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk
     */
    public void setLettera_pagamento_estero(Lettera_pagam_esteroBulk newLetteraPagamentoEstero) {
        lettera_pagamento_estero = newLetteraPagamentoEstero;
    }

    /**
     * Restituisce la <code>Collection</code> contenente l'elenco di modalita di pagamento
     * relativi al terzo selezionato
     *
     * @return java.util.Collection
     */

    public java.util.Collection getModalita() {
        return modalita;
    }

    /**
     * Imposta la <code>Collection</code> contenente l'elenco di modalita di pagamento
     * relativi al terzo selezionato
     *
     * @param newModalita <code>java.util.Collection</code>
     */

    public void setModalita(java.util.Collection newModalita) {
        modalita = newModalita;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/27/2001 12:15:34 PM)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
     */
    public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
        return modalita_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/27/2001 12:15:34 PM)
     *
     * @param newModalita_pagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
     */
    public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
        modalita_pagamento = newModalita_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/5/2002 11:58:39 AM)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getModalita_trasportoColl() {
        return modalita_trasportoColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/5/2002 11:58:39 AM)
     *
     * @param newModalita_trasportoColl java.util.Collection
     */
    public void setModalita_trasportoColl(java.util.Collection newModalita_trasportoColl) {
        modalita_trasportoColl = newModalita_trasportoColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/07/2001 11.07.07)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public ObbligazioniTable getObbligazioniHash() {

        return getFattura_passiva_obbligazioniHash();
    }

    public java.lang.Long getPg_banca() {
        it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
        if (banca == null)
            return null;
        return banca.getPg_banca();
    }

    public void setPg_banca(java.lang.Long pg_banca) {
        this.getBanca().setPg_banca(pg_banca);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public java.lang.Long getPg_doc_amm() {

        return getPg_fattura_passiva();
    }

    public java.lang.Long getPg_lettera() {
        it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera_pagamento_estero = this.getLettera_pagamento_estero();
        if (lettera_pagamento_estero == null)
            return null;
        return lettera_pagamento_estero.getPg_lettera();
    }

    public void setPg_lettera(java.lang.Long pg_lettera) {
        this.getLettera_pagamento_estero().setPg_lettera(pg_lettera);
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/07/2001 11.07.07)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getRiferimenti_bancari() {
        return riferimenti_bancari;
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/05/2003 15.55.11)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportata() {
        return riportata;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.24.40)
     *
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

    /**
     * Insert the method's description here.
     * Creation date: (10/8/2001 2:29:32 PM)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getSezionali() {
        return sezionali;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/8/2001 2:29:32 PM)
     *
     * @param newSezionali java.util.Collection
     */
    public void setSezionali(java.util.Collection newSezionali) {
        sezionali = newSezionali;
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/13/2002 10:36:39 AM)
     *
     * @return int
     */
    public String getSezionaliFlag() {

        if (getFl_intra_ue() != null && getFl_intra_ue().booleanValue()) {
            setFl_extra_ue(Boolean.FALSE);
            setFl_san_marino_con_iva(Boolean.FALSE);
            setFl_san_marino_senza_iva(Boolean.FALSE);
            setFl_bolla_doganale(Boolean.FALSE);
            setFl_spedizioniere(Boolean.FALSE);
            if (getTi_istituz_commerc() != null && TipoIVA.COMMERCIALE.value().equalsIgnoreCase(getTi_istituz_commerc()) &&
                    getTi_bene_servizio() != null && FATTURA_DI_BENI.equalsIgnoreCase(getTi_bene_servizio()) &&
                    getFl_merce_extra_ue() != null && getFl_merce_extra_ue()) {
                setFl_merce_extra_ue(Boolean.TRUE);
                setFl_merce_intra_ue(Boolean.FALSE);
                setFl_autofattura(Boolean.FALSE);
                setAutoFatturaNeeded(true);
            } else if (getTi_istituz_commerc() != null && TipoIVA.COMMERCIALE.value().equalsIgnoreCase(getTi_istituz_commerc()) &&
                    getTi_bene_servizio() != null && FATTURA_DI_SERVIZI.equalsIgnoreCase(getTi_bene_servizio())) {
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                //setFl_autofattura(Boolean.TRUE);
                setAutoFatturaNeeded(false);
            } else {
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                boolean autoFatt = (getTi_istituz_commerc() != null && !TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()));
                setAutoFatturaNeeded(autoFatt);
                if (autoFatt)
                    setFl_autofattura(Boolean.TRUE);
            }
            if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                ((Fattura_passiva_IBulk) this).setFattura_estera(null);

            sezionaliFlag = SEZIONALI_FLAGS_IUE;
        } else if (getFl_extra_ue() != null && getFl_extra_ue().booleanValue()) {
            setFl_intra_ue(Boolean.FALSE);
            setFl_san_marino_con_iva(Boolean.FALSE);
            setFl_san_marino_senza_iva(Boolean.FALSE);
            setFl_bolla_doganale(Boolean.FALSE);
            setFl_spedizioniere(Boolean.FALSE);
            setFl_merce_extra_ue(Boolean.FALSE);
            //??? solo commerciale
            if (getTi_istituz_commerc() != null && TipoIVA.COMMERCIALE.value().equalsIgnoreCase(getTi_istituz_commerc()) &&
                    getTi_bene_servizio() != null && FATTURA_DI_BENI.equalsIgnoreCase(getTi_bene_servizio()) &&
                    getFl_merce_intra_ue() != null && getFl_merce_intra_ue()) {
                boolean autoFatt = (getTi_istituz_commerc() != null && !TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()));
                setAutoFatturaNeeded(autoFatt);
                if (autoFatt)
                    setFl_autofattura(Boolean.TRUE);

            } else {
                if (isAutoFatturaNeeded())
                    setFl_autofattura(isFatturaDiServizi() ? Boolean.TRUE : Boolean.FALSE);
            }
            if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                ((Fattura_passiva_IBulk) this).setFattura_estera(null);

            //setAutoFatturaNeeded(isFatturaDiServizi());
            sezionaliFlag = SEZIONALI_FLAGS_EUE;
        } else if (getFl_san_marino_con_iva() != null && getFl_san_marino_con_iva().booleanValue()) {
            setFl_intra_ue(Boolean.FALSE);
            setFl_extra_ue(Boolean.FALSE);
            setFl_san_marino_senza_iva(Boolean.FALSE);
            setFl_bolla_doganale(Boolean.FALSE);
            setFl_spedizioniere(Boolean.FALSE);
            setFl_merce_extra_ue(Boolean.FALSE);
            setFl_merce_intra_ue(Boolean.FALSE);
            if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                ((Fattura_passiva_IBulk) this).setFattura_estera(null);
            if (isAutoFatturaNeeded())
                setFl_autofattura(Boolean.FALSE);
            setAutoFatturaNeeded(false);
            sezionaliFlag = SEZIONALI_FLAGS_SMC;
        } else if (getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue()) {
            setFl_intra_ue(Boolean.FALSE);
            setFl_extra_ue(Boolean.FALSE);
            setFl_san_marino_con_iva(Boolean.FALSE);
            setFl_bolla_doganale(Boolean.FALSE);
            setFl_spedizioniere(Boolean.FALSE);
            setFl_merce_extra_ue(Boolean.FALSE);
            setFl_merce_intra_ue(Boolean.FALSE);
            if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                ((Fattura_passiva_IBulk) this).setFattura_estera(null);
            boolean autoFatt = (getTi_istituz_commerc() != null && !TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()));
            //setAutoFatturaNeeded(autoFatt);
            if (autoFatt && (getTi_bene_servizio() != null && Bene_servizioBulk.BENE.equalsIgnoreCase(getTi_bene_servizio()))) {
                setFl_autofattura(Boolean.TRUE);
                setAutoFatturaNeeded(Boolean.TRUE);
            } else
                setAutoFatturaNeeded(Boolean.FALSE);

            sezionaliFlag = SEZIONALI_FLAGS_SMS;
        } else {
            if (isAutoFatturaNeeded())
                setFl_autofattura(Boolean.FALSE);
            setAutoFatturaNeeded(false);
            if (getFl_intra_ue() == null &&
                    getFl_extra_ue() == null &&
                    getFl_san_marino_con_iva() == null &&
                    getFl_san_marino_senza_iva() == null) {
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
     *
     * @param newSezionaliFlag int
     */
    public void setSezionaliFlag(String newSezionaliFlag) {
        sezionaliFlag = newSezionaliFlag;

        switch (sezionaliFlag == null ? 99 : Integer.valueOf(sezionaliFlag).intValue()) {
            case 0: {
                setFl_intra_ue(null);
                setFl_extra_ue(null);
                setFl_san_marino_con_iva(null);
                setFl_san_marino_senza_iva(null);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                if (isAutoFatturaNeeded())
                    setFl_autofattura(Boolean.FALSE);
                setAutoFatturaNeeded(false);
                break;
            }
            case 1: {
                setFl_intra_ue(Boolean.FALSE);
                setFl_extra_ue(Boolean.FALSE);
                setFl_san_marino_con_iva(Boolean.FALSE);
                setFl_san_marino_senza_iva(Boolean.FALSE);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                if (isAutoFatturaNeeded())
                    setFl_autofattura(Boolean.FALSE);
                setAutoFatturaNeeded(false);
                break;
            }
            case 2: {
                setFl_intra_ue(Boolean.TRUE);
                setFl_extra_ue(Boolean.FALSE);
                setFl_san_marino_con_iva(Boolean.FALSE);
                setFl_san_marino_senza_iva(Boolean.FALSE);
                setFl_autofattura(Boolean.TRUE);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                boolean autoFatt = (getTi_istituz_commerc() != null && !TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()));
                setAutoFatturaNeeded(autoFatt);
                break;
            }
            case 3: {
                setFl_intra_ue(Boolean.FALSE);
                setFl_extra_ue(Boolean.TRUE);
                setFl_san_marino_con_iva(Boolean.FALSE);
                setFl_san_marino_senza_iva(Boolean.FALSE);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                if (isAutoFatturaNeeded())
                    setFl_autofattura(isFatturaDiServizi() ? Boolean.TRUE : Boolean.FALSE);
                setAutoFatturaNeeded(isFatturaDiServizi());
                break;
            }
            case 4: {
                setFl_intra_ue(Boolean.FALSE);
                setFl_extra_ue(Boolean.FALSE);
                setFl_san_marino_con_iva(Boolean.TRUE);
                setFl_san_marino_senza_iva(Boolean.FALSE);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                if (isAutoFatturaNeeded())
                    setFl_autofattura(Boolean.FALSE);
                setAutoFatturaNeeded(false);
                break;
            }
            case 5: {
                setFl_intra_ue(Boolean.FALSE);
                setFl_extra_ue(Boolean.FALSE);
                setFl_san_marino_con_iva(Boolean.FALSE);
                setFl_san_marino_senza_iva(Boolean.TRUE);
                setFl_autofattura(Boolean.TRUE);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                boolean autoFatt = (getTi_istituz_commerc() != null && !TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()));
                setAutoFatturaNeeded(autoFatt);
                break;
            }
            default: {
                setFl_intra_ue(null);
                setFl_extra_ue(null);
                setFl_san_marino_con_iva(null);
                setFl_san_marino_senza_iva(null);
                setFl_bolla_doganale(Boolean.FALSE);
                setFl_spedizioniere(Boolean.FALSE);
                setFl_merce_extra_ue(Boolean.FALSE);
                setFl_merce_intra_ue(Boolean.FALSE);
                if (getClass().isAssignableFrom(Fattura_passiva_IBulk.class))
                    ((Fattura_passiva_IBulk) this).setFattura_estera(null);
                if (isAutoFatturaNeeded())
                    setFl_autofattura(Boolean.FALSE);
                setAutoFatturaNeeded(false);
            }
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/13/2002 10:36:39 AM)
     *
     * @return int
     */
    public Dictionary getSezionaliFlags() {
        return SEZIONALI_FLAG_KEYS;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getStato_cofiKeys() {
        return STATO;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getStato_cofiKeysForSearch() {

        OrderedHashtable d = (OrderedHashtable) getStato_cofiKeys();
        if (d == null) return null;

        OrderedHashtable clone = (OrderedHashtable) d.clone();
        clone.remove(STATO_INIZIALE);
        return clone;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getStato_pagamento_fondo_ecoKeys() {

        if (getStato_pagamento_fondo_eco() != null &&
                REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco())) {
            return STATO_FONDO_ECO;
        }

        OrderedHashtable oh = (OrderedHashtable) ((OrderedHashtable) STATO_FONDO_ECO).clone();
        oh.remove(REGISTRATO_IN_FONDO_ECO);
        return oh;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
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
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public String getStatoIVA() {

        return (getProtocollo_iva() == null ||
                getProtocollo_iva_generale() == null) ?
                "A" : "B";
    }

    public String getSupplierNationType() {

        String cond = null;
        if (getFl_intra_ue() != null && getFl_intra_ue().booleanValue())
            cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.CEE;
        else if (getFl_extra_ue() != null && getFl_extra_ue().booleanValue())
            cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.EXTRA_CEE;
        else if (getFl_san_marino_con_iva() != null && getFl_san_marino_con_iva().booleanValue() ||
                getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue())
            cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.SAN_MARINO;
        else
            cond = it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.ITALIA;
        return cond;
    }

    /**
     * Restituisce la <code>Collection</code> contenente l'elenco di termini di pagamento
     * relativi al terzo selezionato
     *
     * @return java.util.Collection
     */

    public java.util.Collection getTermini() {
        return termini;
    }

    /**
     * Imposta la <code>Collection</code> contenente l'elenco dei termini di pagamento
     * relativi al terzo selezionato
     *
     * @param newTermini <code>java.util.Collection</code>
     */

    public void setTermini(java.util.Collection newTermini) {
        termini = newTermini;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/27/2001 12:16:23 PM)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
     */
    public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
        return termini_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/27/2001 12:16:23 PM)
     *
     * @param newTermini_pagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
     */
    public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
        termini_pagamento = newTermini_pagamento;
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
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura Extra UE.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getTi_bene_servizioKeys() {
        return FATTURA_BENI_SERVIZI;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getTi_istituz_commercKeys() {
        OrderedHashtable d = (OrderedHashtable) getTi_istituz_commercKeysForSearch();
        if (d == null) return null;
        OrderedHashtable clone = (OrderedHashtable) d.clone();
        if ((getStato_cofi() != null && getStato_cofi().compareTo(STATO_INIZIALE) == 0) || !isNotAbledToModifyTipoIstCom())
            clone.remove(PROMISCUA);
        return clone;

    }

    public Dictionary getTi_istituz_commercKeysForSearch() {
        return TIPO;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/7/2001 11:29:16 AM)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
     */
    public Tipo_sezionaleBulk getTipo_sezionale() {
        return tipo_sezionale;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/7/2001 11:29:16 AM)
     *
     * @param newTipo_sezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
     */
    public void setTipo_sezionale(Tipo_sezionaleBulk newTipo_sezionale) {
        tipo_sezionale = newTipo_sezionale;
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/7/2002 3:17:11 PM)
     *
     * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
     */
    public java.util.Dictionary getTipo_sospesoKeys() {

        java.util.Dictionary tipi = new java.util.Hashtable();
        tipi.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_ENTRATA, "Entrata");
        tipi.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_SPESA, "Spesa");
        return tipi;
    }

    /**
     * @return DivisaBulk
     */
    public DivisaBulk getValuta() {
        return valuta;
    }

    /**
     * @return void
     */
    public void setValuta(DivisaBulk newValuta) {
        valuta = newValuta;
        setChangeOperationOn(valuta);
    }

    /**
     * Restituisce la <code>Collection</code> contenente l'elenco delle valute
     *
     * @return java.util.Collection
     */

    public java.util.Collection getValute() {
        return valute;
    }

    /**
     * Imposta la <code>Collection</code> contenente l'elenco di valute
     *
     * @param newValute <code>java.util.Collection</code>
     */

    public void setValute(java.util.Collection newValute) {
        valute = newValute;
    }

    /**
     * Indica se la competenza COGE ? stata indicata nell'anno precedente. Regola valida SOLO nel caso di
     * ESERCIZIO == ESERCIZIO_INIZIO
     */
    public boolean hasCompetenzaCOGEInAnnoPrecedente() {

        //Modificato a seguito della richiesta n? 737 del 21/01/2004
        //Originale:
        //if (ISTITUZIONALE.equals(getTi_istituz_commerc()))
        //return getDateCalendar(getDt_a_competenza_coge()).get(Calendar.YEAR) == getEsercizio().intValue()-1;
        //return false;
        return getDateCalendar(getDt_a_competenza_coge()).get(Calendar.YEAR) == getEsercizio().intValue() - 1;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean hasDettagliNonContabilizzati() {

        return !getDettagliNonContabilizzati().isEmpty();
    }

    public boolean hasDettagliContabilizzati() {

        return !getDettagliContabilizzati().isEmpty();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean hasDettagliPagati() {

        return !getDettagliPagati().isEmpty();
    }

    public OggettoBulk initialize(CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
        setCd_cds_origine(getCd_cds());
        setCd_uo_origine(getCd_unita_organizzativa());
        setFl_fattura_compenso(Boolean.FALSE);
        setFl_bolla_doganale(Boolean.FALSE);
        setFl_spedizioniere(Boolean.FALSE);
        setFl_autofattura(Boolean.FALSE);
        setFl_merce_extra_ue(Boolean.FALSE);
        setFl_merce_intra_ue(Boolean.FALSE);
        setFl_liquidazione_differita(Boolean.FALSE);
        setFl_split_payment(Boolean.FALSE);
        return this;
    }

    /**
     * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
     * in stato <code>INSERT</code>.
     * Questo metodo viene invocato automaticamente da un
     * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
     * per l'inserimento di un OggettoBulk.
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initializeForInsert(bp, context);

        setFl_intra_ue(Boolean.FALSE);
        setFl_extra_ue(Boolean.FALSE);
        setFl_san_marino_con_iva(Boolean.FALSE);
        setFl_san_marino_senza_iva(Boolean.FALSE);
        setFl_congelata(Boolean.FALSE);
        setFl_liquidazione_differita(Boolean.FALSE);
        setFl_split_payment(Boolean.FALSE);

        return this;
    }

    /**
     * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
     * in stato <code>SEARCH</code>.
     * Questo metodo viene invocato automaticamente da un
     * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
     * per la ricerca di un OggettoBulk.
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initializeForSearch(bp, context);

        setFl_intra_ue(null);
        setFl_extra_ue(null);
        setFl_san_marino_con_iva(null);
        setFl_san_marino_senza_iva(null);
        setFl_fattura_compenso(null);
        setFl_liquidazione_differita(null);
        setFl_split_payment(null);
        setFl_bolla_doganale(null);
        setFl_spedizioniere(null);
        setFl_autofattura(null);
        setFl_merce_extra_ue(null);
        setFl_merce_intra_ue(null);

        return this;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToDeleteLettera() {

        return getLettera_pagamento_estero() == null ||
                isPagata() ||
                isPagataParzialmente() ||
                isROStatoTrasmissioneLettera();
    }

    public boolean isAbledToDisassociaLettera() {

        return getLettera_pagamento_estero() == null ||
                isPagata() ||
                isPagataParzialmente() ||
                !isROStatoTrasmissioneLettera();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToInsertBank() {

        return !(getFornitore() != null &&
                getFornitore().getCrudStatus() == OggettoBulk.NORMAL &&
                getModalita_pagamento() != null);
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToInsertLettera() {

        return !isEstera() ||
                getLettera_pagamento_estero() != null ||
                isPagata() ||
                isPagataParzialmente() ||
                (getObbligazioniHash() == null || getObbligazioniHash().isEmpty());
        //Come da richiesta 108 gestione errori CNR elimino il controllo sulla valuta (09/09/2002 RP)
        //|| isDefaultValuta();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToModifyFlagsTipoFattura() {

        return isAbledToModifyTipoFattura() ||
                (getFornitore() != null &&
                        getFornitore().getCrudStatus() == OggettoBulk.NORMAL);
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToModifyTipoFattura() {

        return !(getCrudStatus() == OggettoBulk.UNDEFINED ||
                (fattura_passiva_dettColl != null &&
                        fattura_passiva_dettColl.isEmpty() &&
                        fattura_passiva_intrastatColl != null &&
                        fattura_passiva_intrastatColl.isEmpty()) ||
                isElettronica() && getPg_fattura_passiva() == null);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isAnnullato() {

        return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public void setAnnullato(java.sql.Timestamp date) {

        setStato_cofi(STATO_ANNULLATO);
        setDt_cancellazione(date);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/4/2002 3:19:51 PM)
     *
     * @return boolean
     */
    public boolean isAutoFatturaNeeded() {
        return autoFatturaNeeded;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/4/2002 3:19:51 PM)
     *
     * @param newAutoFatturaNeeded boolean
     */
    public void setAutoFatturaNeeded(boolean newAutoFatturaNeeded) {
        autoFatturaNeeded = newAutoFatturaNeeded;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isBollaDoganale() {

        return getFl_bolla_doganale() != null &&
                getFl_bolla_doganale().booleanValue();
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isCommerciale() {
        return TipoIVA.COMMERCIALE.value().equals(getTi_istituz_commerc());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isCongelata() {

        return getFl_congelata() != null && getFl_congelata().booleanValue();
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2002 11:25:24 AM)
     *
     * @return boolean
     */
    public boolean isDefaultValuta() {
        return defaultValuta;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2002 11:25:24 AM)
     *
     * @param newDefaultValuta boolean
     */
    public void setDefaultValuta(boolean newDefaultValuta) {
        defaultValuta = newDefaultValuta;
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isDeleting() {

        return isDeleting;
    }

    public boolean isEditable() {
        try {
            return !(isPagata() ||
                    //(isStampataSuRegistroIVA() && //Modificato a seguito richiesta 423
                    //!isDeleting()) ||
                    isAnnullato() ||
                    isCongelata() ||
                    (!((getEsercizio().intValue() == getEsercizioInScrivania().intValue()) && !isRiportata()) &&
                            !isDeleting()));
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isEstera() {

        //Come da richiesta 108 gestione errori CNR elimino il controllo sulla valuta (09/09/2002 RP)

        if ((getFl_extra_ue() != null && getFl_extra_ue().booleanValue()) ||
                (getFl_intra_ue() != null && getFl_intra_ue().booleanValue()))
            return true;

        return false;

        //if (getFl_extra_ue() == null) return false;
        //return getFl_extra_ue().booleanValue();
    }

    public boolean isSanMarinoSenzaIVA() {

        if ((getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue()))
            return true;

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isFatturaDiBeni() {

        return isIstituzionale() &&
                getTi_bene_servizio() != null &&
                Bene_servizioBulk.BENE.equalsIgnoreCase(getTi_bene_servizio());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isFatturaDiServizi() {

        return isCommerciale() &&
                getTi_bene_servizio() != null &&
                Bene_servizioBulk.SERVIZIO.equalsIgnoreCase(getTi_bene_servizio());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isGenerataDaCompenso() {

        return getFl_fattura_compenso() != null &&
                getFl_fattura_compenso().booleanValue();
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isIstituzionale() {
        return TipoIVA.ISTITUZIONALE.value().equals(getTi_istituz_commerc());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isPagata() {

        return STATO_PAGATO.equalsIgnoreCase(getStato_cofi()) ||
                REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco()) ||
                //&& isGenerataDaCompenso()
                (!isGestione_doc_ele() && isGenerataDaCompenso() && getPg_fattura_passiva() != null);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isPagataParzialmente() {

        return STATO_PARZIALE.equalsIgnoreCase(getStato_cofi());
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isPromiscua() {
        return PROMISCUA.equals(getTi_istituz_commerc());
    }

    public boolean isRiportata() {

        return !NON_RIPORTATO.equals(riportata);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/05/2003 15.55.11)
     *
     * @param newRiportata java.lang.String
     */
    public void setRiportata(java.lang.String newRiportata) {
        riportata = newRiportata;
    }

    public boolean isRiportataInScrivania() {

        return !NON_RIPORTATO.equals(riportataInScrivania);
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.24.40)
     *
     * @param newRiportataInScrivania java.lang.String
     */
    public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
        riportataInScrivania = newRiportataInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROAutofattura() {

        return isAbledToModifyTipoFattura() || isAutoFatturaNeeded() || (getFl_split_payment() != null && getFl_split_payment());
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROCambio() {

        return isDefaultValuta();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isRODateCompetenzaCOGE() {

        if ((isElettronica() && getPg_fattura_passiva() != null)
                ||
                (!isElettronica() && getFattura_passiva_dettColl() != null &&
                        !getFattura_passiva_dettColl().isEmpty())
                )
            return true;

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isRODt_registrazione() {

        return isStampataSuRegistroIVA() || getProgr_univoco() != null;
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isROFl_bolla_doganale() {
        return (isAbledToModifyFlagsTipoFattura() && !(isElettronica()) || isPromiscua() || (isElettronica() && getPg_fattura_passiva() != null));
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isROFl_extra_ue() {
        return isAbledToModifyFlagsTipoFattura() || isPromiscua() || isElettronica() || getFl_split_payment() == null || getFl_split_payment();
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isROFl_intra_ue() {
        return isAbledToModifyFlagsTipoFattura() || isPromiscua() || isElettronica() || getFl_split_payment() == null || getFl_split_payment();
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isROFl_san_marino_senza_iva() {
        return isAbledToModifyFlagsTipoFattura() || isPromiscua() || isElettronica();
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public boolean isROFl_spedizioniere() {
        return (isAbledToModifyFlagsTipoFattura() && !(isElettronica()) || isPromiscua() || (isElettronica() && getPg_fattura_passiva() != null));
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROfornitore() {

        return getFornitore() == null ||
                getFornitore().getCrudStatus() == OggettoBulk.NORMAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROFornitoreCrudTool() {

        return isROFornitoreSearchTool();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROfornitoreForSearch() {

        return isROfornitore();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROFornitoreSearchTool() {

        return (fattura_passiva_obbligazioniHash != null &&
                !fattura_passiva_obbligazioniHash.isEmpty()) ||
                (fattura_passiva_intrastatColl != null &&
                        !fattura_passiva_intrastatColl.isEmpty()) ||
                (fattura_passiva_dettColl != null &&
                        !fattura_passiva_dettColl.isEmpty()) &&
                        !(isElettronica() && getPg_fattura_passiva() == null) ||
                isStampataSuRegistroIVA() ||
                getProgr_univoco() != null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROIm_totale_quadratura() {

        return isPagata() ||
                isStampataSuRegistroIVA() ||
                getProgr_univoco() != null ||
                isAnnullato() ||
                isDefaultValuta();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROImportoTotalePerObbligazione() {

        return true;
    }

    public boolean isROModalita_pagamento() {

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/11/2002 5:10:59 PM)
     */
    public boolean isROSospeso() {

        return getLettera_pagamento_estero() == null ||
                getLettera_pagamento_estero().getSospeso() == null ||
                getLettera_pagamento_estero().getSospeso().getCrudStatus() == OggettoBulk.NORMAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/11/2002 5:10:59 PM)
     */
    public boolean isROSospesoSearchTool() {

        if (getLettera_pagamento_estero() != null)
            return !getLettera_pagamento_estero().isAnnoDiCompetenza();
        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROStato_pagamento_fondo_eco() {

        return true;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROTi_bene_servizio() {

        return    //isCommerciale() &&
                ((getFl_extra_ue() != null && getFl_extra_ue().booleanValue()) ||
                        (getFl_intra_ue() != null && getFl_intra_ue().booleanValue()) ||
                        (getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue())) &&
                        getFattura_passiva_dettColl() != null &&
                        !getFattura_passiva_dettColl().isEmpty();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROValuta() {

        //Come da richiesta 108 gestione errori CNR elimino il controllo sulla valuta (09/09/2002 RP)
        return !isEstera();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isSpedizioniere() {

        return getFl_spedizioniere() != null &&
                getFl_spedizioniere().booleanValue();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isStampataSuRegistroIVA() {

        return STATO_IVA_B.equalsIgnoreCase(getStatoIVA()) ||
                STATO_IVA_C.equalsIgnoreCase(getStatoIVA()) ||
                //A seguito dell'errore segnalato 569 (dovuto alla richiesta 423)
                (getAutofattura() != null && getAutofattura().isStampataSuRegistroIVA());//||
        //(getProgr_univoco()!=null);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isVoidable() {

        // Gennaro Borriello - (02/11/2004 16.48.21)
        // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
        //	DA UN ES. PRECEDENTE a quello di scrivania.
        return ((isRiportataInScrivania() && !isRiportata())) ||
                (STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
                        ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
                (STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
                        PARZIALMENTE_ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
                (!NON_REGISTRATO_IN_COGE.equalsIgnoreCase(getStato_coge()) &&
                        !NON_PROCESSARE_IN_COGE.equalsIgnoreCase(getStato_coge())) ||
                (!NON_CONTABILIZZATO_IN_COAN.equalsIgnoreCase(getStato_coan()) &&
                        !NON_PROCESSARE_IN_COAN.equalsIgnoreCase(getStato_coan()));
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
     */
    public boolean quadraturaInDeroga() {
        return isCommerciale() && (
                (getFl_split_payment() != null && getFl_split_payment()) ||
                        ((getFl_intra_ue() != null && getFl_intra_ue().booleanValue() && Bene_servizioBulk.BENE.equalsIgnoreCase(getTi_bene_servizio())) ||
                                (getFl_intra_ue() != null && getFl_intra_ue().booleanValue() && isFatturaDiServizi() && getFl_autofattura().booleanValue())) ||
                        (getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue() && isFatturaDiServizi() && getFl_autofattura().booleanValue()) ||
                        // quadratura in deroga per commerciali
                        //(getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue()) ||
                        (getFl_extra_ue() != null && getFl_extra_ue().booleanValue() && isFatturaDiServizi() && getFl_autofattura().booleanValue()) ||
                        (getFl_extra_ue() != null && getFl_extra_ue().booleanValue() && Bene_servizioBulk.BENE.equalsIgnoreCase(getTi_bene_servizio()) && getFl_autofattura().booleanValue()
                                && getFl_merce_intra_ue() != null && getFl_merce_intra_ue().booleanValue())
        );
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
     */
    public boolean quadraturaInDeroga1210() {
        return ((isIstituzionale() && (
                (getFl_intra_ue() != null && getFl_intra_ue().booleanValue()) ||
                        (getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue())) &&
                (getTipo_sezionale() != null && getTipo_sezionale().getTi_bene_servizio().equalsIgnoreCase(getTi_bene_servizio())) &&
                isFatturaDiBeni()) || (getTipo_sezionale() != null && getTipo_sezionale().getFl_servizi_non_residenti().booleanValue()) ||
                (isIstituzionale() && getFl_extra_ue() != null && getFl_extra_ue().booleanValue() && Bene_servizioBulk.BENE.equalsIgnoreCase(getTi_bene_servizio())
                        && getFl_merce_intra_ue() != null && getFl_merce_intra_ue().booleanValue()));
    }

    public void removeFromAssociazioniInventarioHash(
            Ass_inv_bene_fatturaBulk ass,
            Fattura_passiva_rigaBulk rigaFattura) {

        if (associazioniInventarioHash == null) return;
        Vector righeAssociate = (Vector) associazioniInventarioHash.get(ass);
        if (righeAssociate != null) {
            if (rigaFattura != null && righeAssociate.contains(rigaFattura))
                righeAssociate.remove(rigaFattura);
            if (righeAssociate.isEmpty())
                associazioniInventarioHash.remove(ass);
        }
    }

    public void removeFromCarichiInventarioHash(
            Buono_carico_scaricoBulk buonoCS,
            Fattura_passiva_rigaBulk rigaFattura) {

        if (carichiInventarioHash == null) return;
        Vector righeAssociate = (Vector) carichiInventarioHash.get(buonoCS);
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
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

        if (docCont != null && deferredSaldi != null &&
                deferredSaldi.containsKey(docCont))
            deferredSaldi.remove(docCont);
    }

    public int removeFromDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) {

        if (BulkCollections.containsByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio))
            getDettagliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio));
        return dettagliCancellati.size() - 1;
    }

    public int removeFromDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio) {

        if (BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) dettaglio))
            getDocumentiContabiliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) dettaglio));
        return documentiContabiliCancellati.size() - 1;
    }

    public void removeFromFattura_passiva_ass_totaliMap(
            Accertamento_scadenzarioBulk scadenza) {

        fattura_passiva_ass_totaliMap.remove(scadenza);
    }

    public void removeFromFattura_passiva_ass_totaliMap(
            Obbligazione_scadenzarioBulk scadenza) {

        fattura_passiva_ass_totaliMap.remove(scadenza);
    }

    public boolean removeFromFattura_passiva_consuntivoColl(Consuntivo_rigaVBulk riga) {

        return fattura_passiva_consuntivoColl.remove(riga);
    }

    public Fattura_passiva_rigaBulk removeFromFattura_passiva_dettColl(int indiceDiLinea) {

        Fattura_passiva_rigaBulk element = (Fattura_passiva_rigaBulk) fattura_passiva_dettColl.get(indiceDiLinea);
        addToDettagliCancellati(element);
        if (element != null && element.getObbligazione_scadenziario() != null)
            removeFromFattura_passiva_obbligazioniHash(element);

        Optional.ofNullable(element)
                .ifPresent(fattura_passiva_rigaBulk -> removeFromFatturaRigaOrdiniHash(fattura_passiva_rigaBulk));
        return (Fattura_passiva_rigaBulk) fattura_passiva_dettColl.remove(indiceDiLinea);
    }

    public Fattura_passiva_intraBulk removeFromFattura_passiva_intrastatColl(int index) {

        return (Fattura_passiva_intraBulk) fattura_passiva_intrastatColl.remove(index);
    }

    public void removeFromFattura_passiva_obbligazioniHash(
            Fattura_passiva_rigaBulk rigaFattura) {

        Vector righeAssociate = (Vector) fattura_passiva_obbligazioniHash.get(rigaFattura.getObbligazione_scadenziario());
        if (righeAssociate != null) {
            righeAssociate.remove(rigaFattura);
            if (righeAssociate.isEmpty()) {
                fattura_passiva_obbligazioniHash.remove(rigaFattura.getObbligazione_scadenziario());
                addToDocumentiContabiliCancellati(rigaFattura.getObbligazione_scadenziario());
            }
        } else
            addToDocumentiContabiliCancellati(rigaFattura.getObbligazione_scadenziario());
    }

    public void removeFromFatturaRigaOrdiniHash(
            Fattura_passiva_rigaBulk rigaFattura) {
        Optional.ofNullable(fatturaRigaOrdiniHash)
                .ifPresent(fatturaRigaOrdiniTable -> fatturaRigaOrdiniTable.remove(rigaFattura));
        Optional.ofNullable(rigaFattura.getFatturaOrdineColl())
                .ifPresent(fatturaOrdineBulks -> fatturaOrdineBulks.clear());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/15/2002 10:50:29 AM)
     */
    public void resetDefferredSaldi() {

        deferredSaldi = null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/22/2002 11:12:29 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk
     */
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
     * @return void
     */
    public void setChangeOperationOn(DivisaBulk newValuta) {
        if (valuta != null && valuta.getFl_calcola_con_diviso() != null)
            setChangeOperation((valuta.getFl_calcola_con_diviso().booleanValue()) ? DIVISIONE : MOLTIPLICA);
        else
            setChangeOperation(MOLTIPLICA);
    }

    /*
 * Setter dell'attributo fl_intra_ue
 */
    public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
        super.setFl_intra_ue(fl_intra_ue);
        //if (fl_intra_ue != null)
        //setAutoFatturaNeeded(fl_intra_ue.booleanValue());
    }

    /*
 * Setter dell'attributo fl_san_marino_senza_iva
 */
    public void setFl_san_marino_senza_iva(java.lang.Boolean fl_san_marino_senza_iva) {
        super.setFl_san_marino_senza_iva(fl_san_marino_senza_iva);
        if (fl_san_marino_senza_iva != null)
            setAutoFatturaNeeded(fl_san_marino_senza_iva.booleanValue());
    }

    /**
     * Restituisce <code>true</code> se il sezionale ? di tipo Istituzionale
     *
     * @return boolean
     */

    public void setIsDeleting(boolean deleting) {

        isDeleting = deleting;
    }

    public void validaDateCompetenza()
            throws ValidationException {

        if (getDt_da_competenza_coge() == null)
            throw new ValidationException("Inserire la data di \"competenza da\" per la testata documento.");
        if (getDt_a_competenza_coge() == null)
            throw new ValidationException("Inserire la data di \"competenza a\" per la testata documento.");
        if (getStato_cofi().compareTo(STATO_INIZIALE) == 0 && getDt_scadenza() == null)
            throw new ValidationException("La data di scadenza non può essere nulla!");

        Calendar competenzaDa = getDateCalendar(getDt_da_competenza_coge());
        Calendar competenzaA = getDateCalendar(getDt_a_competenza_coge());

        if (competenzaA.before(competenzaDa))
            throw new ValidationException("Inserire correttamente le date di competenza in testata documento");

        int annoCompetenzaDa = competenzaDa.get(Calendar.YEAR);
        int annoCompetenzaA = competenzaA.get(Calendar.YEAR);
        try {
            if (annoCompetenzaDa != getEsercizio().intValue())
                throw new ValidationException("La data di inizio competenza deve appartenere all'esercizio di scrivania!");
            //Modificato a seguito della richiesta n? 738 del 24/03/2004
            //Originale:
            //if (!ISTITUZIONALE.equals(getTi_istituz_commerc())) {
            //if (annoCompetenzaA > getEsercizio().intValue())
            //throw new ValidationException("La data di fine competenza deve appartenere all'esercizio di scrivania!");
            //} else if (annoCompetenzaA > getEsercizio().intValue()+1)
            //**********************************************************
            if (annoCompetenzaA > getEsercizio().intValue() + 1)
                throw new ValidationException("La data di fine competenza deve appartenere all'esercizio di scrivania o al successivo!");

        } catch (ValidationException e) {
            //Modificato a seguito della richiesta n? 737 del 21/01/2004
            //Originale:
            //if (ISTITUZIONALE.equals(getTi_istituz_commerc())) {
            //int annoPrecedente = getEsercizio().intValue()-1;
            //if ((annoCompetenzaA < annoPrecedente) ||
            //(annoCompetenzaDa < annoPrecedente))
            //throw e;
            //else if (annoCompetenzaDa == annoPrecedente) {
            //if (annoCompetenzaA > annoPrecedente)
            //throw new ValidationException("La data di \"competenza a\" deve appartenere all'esercizio dell'anno " + annoPrecedente + ".");
            //} else
            //throw e;
            //} else
            //throw e;
            //*******************************************************
            int annoPrecedente = getEsercizio().intValue() - 1;
            if ((annoCompetenzaA < annoPrecedente) ||
                    (annoCompetenzaDa < annoPrecedente))
                throw new ValidationException("Le date di \"competenza COGE\" devono appartenere all'esercizio dell'anno " + annoPrecedente + ".");
            else if (annoCompetenzaDa == annoPrecedente) {
                if (annoCompetenzaA > annoPrecedente)
                    throw new ValidationException("La data di \"competenza a\" deve appartenere all'esercizio dell'anno " + annoPrecedente + ".");
                if (this.getStato_cofi() != null && this.getStato_cofi().equals(STATO_INIZIALE))
                    if (getDt_registrazione().after(getDt_termine_creazione_docamm())) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        throw new ValidationException("Non è possibile inserire documenti con competenza nell'anno precedente con data di registrazione successiva al " + sdf.format(getDt_termine_creazione_docamm()) + "!");
                    }
            } else
                throw e;
            if (!eseguito && (annoCompetenzaA == getEsercizio() - 1)) {
                eseguito = new Boolean(true);
                throw new ValidationException("Attenzione: per le date competenza indicate non è possibile inventariare i beni!");
            }

        }
    }

    public void validate() throws ValidationException {

        if (getStato_cofi() == null)
            setStato_cofi(STATO_INIZIALE);
        if (getTi_associato_manrev() == null)
            setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

        validateDate();
        validaDateCompetenza();
        // campi obbligatori dal 01/07/2014
        // controllo eliminato per nuova gestione del protocollo unico
        //  if (getDt_registrazione().after(dataInizioObbligoRegistroUnico) && getDt_fattura_fornitore().before(dataInizioFatturaElettronica)){
        //	if(getData_protocollo()== null)
        //		throw new ValidationException("Inserire la data di protocollo di entrata.");
        //	if(getNumero_protocollo()== null)
        //		throw new ValidationException("Inserire il numero di protocollo di entrata!");
        //}
        if (getDt_registrazione().after(dataInizioObbligoRegistroUnico)) {
            if (getStato_liquidazione() == null)
                throw new ValidationException("Inserire lo stato della liquidazione!");
            if (getStato_liquidazione() != null && getStato_liquidazione().compareTo(this.LIQ) != 0 && getCausale() == null)
                throw new ValidationException("Inserire la causale.");
        }

        if (getLettera_pagamento_estero() != null)
            getLettera_pagamento_estero().validate();

//        if (!(isEstera() ||isSanMarinoConIVA() || isSanMarinoSenzaIVA()) &&
//                (!Optional.ofNullable(getCig()).isPresent() && !Optional.ofNullable(getMotivo_assenza_cig()).isPresent())) {
//            throw new ValidationException("Inserire il CIG o il motivo di assenza dello stesso!");
//        }
//        if ((Optional.ofNullable(getCig()).isPresent() && Optional.ofNullable(getMotivo_assenza_cig()).isPresent())) {
//            throw new ValidationException("Inserire solo uno tra il CIG e il motivo di assenza dello stesso!");
//        }
    }

    public void validateDate() throws ValidationException {

        if (getDt_registrazione() == null)
            throw new ValidationException("Inserire la data di registrazione del documento!");

        java.util.Calendar limInf = null;
        java.util.Calendar limSup = null;
        java.util.Calendar today = getDateCalendar(null);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        java.util.Calendar dataRegistrazione = getDateCalendar(getDt_registrazione());

        try {
            // Quando sar? a regime la parte della stampa registri IVA limInf dovr? essere
            // impostato a data successiva alla data ultima stampa IVA (se esiste, altrimenti come adesso)
            int compare = getEsercizio().compareTo(new Integer(dataRegistrazione.get(Calendar.YEAR)));
            if (compare == 0) {
                limSup = today;
                limInf = getDateCalendar(new java.sql.Timestamp(sdf.parse("01/01/" + getEsercizio().intValue()).getTime()));
            } else if (compare > 0) {
                limSup = getDateCalendar(new java.sql.Timestamp(sdf.parse("31/12/" + getEsercizio().intValue()).getTime()));
                limInf = getDateCalendar(new java.sql.Timestamp(sdf.parse("01/01/" + getEsercizio().intValue()).getTime()));
            } else
                throw new ValidationException("La data di registrazione deve appartenere all'esercizio " + getEsercizio().intValue() + "!");

            if (!((dataRegistrazione.after(limInf) || (dataRegistrazione.equals(limInf))) &&
                    (dataRegistrazione.before(limSup) || (dataRegistrazione.equals(limSup)))))
                throw new ValidationException("La data di registrazione deve essere compresa tra il " + sdf.format(limInf.getTime()) + " e il " + sdf.format(limSup.getTime()) + "!");
        } catch (java.text.ParseException e) {
            throw new ValidationException("Data registrazione NON valida!");
        }

        if (getFl_split_payment() != null && getFl_split_payment() && !isGestioneSplitPayment())
            throw new ValidationException("Non è possibile registrare una fattura di tipo Split Payment che abbia data di emissione inferiore al " + sdf.format(this.getDataInizioSplitPayment()) + "!");

        if (getDt_fattura_fornitore() != null) {

            java.util.Calendar dataEmissioneFattura = getDateCalendar(getDt_fattura_fornitore());

            if (dataRegistrazione.before(dataEmissioneFattura) &&
                    !dataRegistrazione.equals(dataEmissioneFattura))
                throw new ValidationException("La data di registrazione  non può essere precedente alla data di emissione del documento del fornitore!");
            if (getDt_scadenza() != null) {
                java.util.Calendar dataScadenzaFattura = getDateCalendar(getDt_scadenza());

                if (dataEmissioneFattura.after(dataScadenzaFattura) &&
                        !dataEmissioneFattura.equals(dataScadenzaFattura))
                    throw new ValidationException("La data di scadenza non può essere precedente alla data di emissione del documento del fornitore!");
            }
            if (getData_protocollo() != null && getData_protocollo().before(getDt_fattura_fornitore()))
                throw new ValidationException("La data di protocollo non può essere precedente alla data di emissione del documento del fornitore!");
            if (getData_protocollo() != null && getData_protocollo().after(getDt_registrazione()))
                throw new ValidationException(
                        "La data protocollo di entrata non può essere superiore alla data registrazione");
        }
    }

    /**
     * @return
     */
    public Boolean getHa_beniColl() {
        if (ha_beniColl != null)
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

    public Boolean getEseguito() {
        return eseguito;
    }

    public void setEseguito(Boolean eseguito) {
        this.eseguito = eseguito;
    }

    public boolean isIvaRecuperabile() {
        return isIvaRecuperabile;
    }

    public void setIvaRecuperabile(boolean isIvaRecuperabile) {
        this.isIvaRecuperabile = isIvaRecuperabile;
    }

    public boolean isROFl_liquidazione_differita() {

        return isStampataSuRegistroIVA() ||
                getProgr_univoco() != null ||
                !isConsentitaEsigibilitaDifferita();
    }

    public boolean isConsentitaEsigibilitaDifferita() {
        boolean consentita;
        return (getFl_intra_ue() == null || !getFl_intra_ue().booleanValue()) &&
                (getFl_extra_ue() == null || !getFl_extra_ue().booleanValue()) &&
                (getFl_san_marino_con_iva() == null || !getFl_san_marino_con_iva().booleanValue()) &&
                (getFl_san_marino_senza_iva() == null || !getFl_san_marino_senza_iva().booleanValue()) &&
                (getFl_bolla_doganale() == null || !getFl_bolla_doganale().booleanValue()) &&
                (getTi_istituz_commerc() == null || isCommerciale()) &&
                (getStato_pagamento_fondo_eco() == null || NO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco()));
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

    public boolean hasIntrastatInviati() {

        if (getFattura_passiva_intrastatColl() != null && !getFattura_passiva_intrastatColl().isEmpty()) {
            for (java.util.Iterator i = getFattura_passiva_intrastatColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_intraBulk fpr = (Fattura_passiva_intraBulk) i.next();
                if (fpr.getFl_inviato() != null && fpr.getFl_inviato().booleanValue()) {
                    return true;
                }
            }
        }
        return false;
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

    public java.sql.Timestamp getDataInizioObbligoRegistroUnico() {
        return dataInizioObbligoRegistroUnico;
    }

    public void setDataInizioObbligoRegistroUnico(
            java.sql.Timestamp dataInizioObbligoRegistroUnico) {
        this.dataInizioObbligoRegistroUnico = dataInizioObbligoRegistroUnico;
    }

    public DocumentoEleTestataBulk getDocumentoEleTestata() {
        return documentoEleTestata;
    }

    public void setDocumentoEleTestata(
            DocumentoEleTestataBulk documentoEleTestata) {
        this.documentoEleTestata = documentoEleTestata;
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Restituisce il valore di:
     * [idPaese]
     **/
    public java.lang.String getIdPaese() {
        DocumentoEleTestataBulk documentoEleTestata = this
                .getDocumentoEleTestata();
        if (documentoEleTestata == null)
            return null;
        return getDocumentoEleTestata().getIdPaese();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Setta il valore di: [idPaese]
     **/
    public void setIdPaese(java.lang.String idPaese) {
        this.getDocumentoEleTestata().setIdPaese(idPaese);
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Restituisce il valore di:
     * [idCodice]
     **/
    public java.lang.String getIdCodice() {
        DocumentoEleTestataBulk documentoEleTestata = this
                .getDocumentoEleTestata();
        if (documentoEleTestata == null)
            return null;
        return getDocumentoEleTestata().getIdCodice();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Setta il valore di: [idCodice]
     **/
    public void setIdCodice(java.lang.String idCodice) {
        this.getDocumentoEleTestata().setIdCodice(idCodice);
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Restituisce il valore di:
     * [identificativoSdi]
     **/
    public java.lang.Long getIdentificativoSdi() {
        DocumentoEleTestataBulk documentoEleTestata = this
                .getDocumentoEleTestata();
        if (documentoEleTestata == null)
            return super.getIdentificativoSdi();
        return getDocumentoEleTestata().getIdentificativoSdi();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Setta il valore di:
     * [identificativoSdi]
     **/
    public void setIdentificativoSdi(java.lang.Long identificativoSdi) {
        if (this.getDocumentoEleTestata() != null)
            this.getDocumentoEleTestata().setIdentificativoSdi(identificativoSdi);
        else
            super.setIdentificativoSdi(identificativoSdi);
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Restituisce il valore di:
     * [progressivo]
     **/
    public java.lang.Long getProgressivo() {
        DocumentoEleTestataBulk documentoEleTestata = this
                .getDocumentoEleTestata();
        if (documentoEleTestata == null)
            return super.getProgressivo();
        return getDocumentoEleTestata().getProgressivo();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009] Setta il valore di:
     * [progressivo]
     **/
    public void setProgressivo(java.lang.Long progressivo) {
        if (this.getDocumentoEleTestata() != null)
            this.getDocumentoEleTestata().setProgressivo(progressivo);
        else
            super.setProgressivo(progressivo);
    }

    public Dictionary getStato_liquidazioneKeys() {
        return STATO_LIQUIDAZIONE;
    }

    public Dictionary getCausaleKeys() {
        return CAUSALE;
    }

    public BulkList<DocumentoEleAllegatiBulk> getDocEleAllegatiColl() {
        return docEleAllegatiColl;
    }

    public void setDocEleAllegatiColl(
            BulkList<DocumentoEleAllegatiBulk> docEleAllegatiColl) {
        this.docEleAllegatiColl = docEleAllegatiColl;
    }

    public int addToDocEleAllegatiColl(DocumentoEleAllegatiBulk doc) {
        docEleAllegatiColl.add(doc);
        doc.setDocumentoEleTestata(this.getDocumentoEleTestata());
        return docEleAllegatiColl.size() - 1;
    }

    public CompensoBulk getCompenso() {
        return compenso;
    }

    public void setCompenso(CompensoBulk newCompenso) {
        compenso = newCompenso;
    }

    public java.lang.String getCds_compenso() {
        it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
        if (compenso == null)
            return null;
        return compenso.getCd_cds();
    }

    public void setCds_compenso(java.lang.String cd_cds_compenso) {
        this.getCompenso().setCd_cds(cd_cds_compenso);
    }

    public java.lang.String getUo_compenso() {
        it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
        if (compenso == null)
            return null;
        return compenso.getCd_unita_organizzativa();
    }

    public void setUo_compenso(java.lang.String cd_uo_compenso) {
        this.getCompenso().setCd_unita_organizzativa(cd_uo_compenso);
    }

    public java.lang.Integer getEsercizio_compenso() {
        it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
        if (compenso == null)
            return null;
        return compenso.getEsercizio();
    }

    public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
        this.getCompenso().setEsercizio(esercizio_compenso);
    }

    public java.lang.Long getPg_compenso() {
        it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
        if (compenso == null)
            return null;
        return compenso.getPg_compenso();
    }

    public void setPg_compenso(java.lang.Long pg_compenso) {
        this.getCompenso().setPg_compenso(pg_compenso);
    }

    public java.sql.Timestamp getDataInizioFatturaElettronica() {
        return dataInizioFatturaElettronica;
    }

    public void setDataInizioFatturaElettronica(
            java.sql.Timestamp dataInizioFatturaElettronica) {
        this.dataInizioFatturaElettronica = dataInizioFatturaElettronica;
    }

    public boolean isGestione_doc_ele() {
        if (this.getDt_registrazione() != null && this.getDataInizioFatturaElettronica() != null) {
            if ((this.getDt_registrazione().compareTo(this.getDataInizioFatturaElettronica()) < 0))
                return false;
            else
                return true;
        }
        return true;  //non dovrebbe mai verificarsi
    }

    public boolean isElettronica() {

        if (getDocumentoEleTestata() != null || getIdentificativoSdi() != null)
            return true;
        return false;
    }

    public boolean isROFl_fattura_compenso() {
        if (isStampataSuRegistroIVA() ||
                isAnnullato() ||
                !isGestione_doc_ele() ||
                isElettronica() ||
                getCompenso() != null)
            return true;

        return false;
    }

    public boolean isROStato_liquidazione() {
        return (isGenerataDaCompenso() && getCompenso() != null);
    }

    public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
        return getArchivioAllegati().remove(index);
    }

    public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
        archivioAllegati.add(allegato);
        return archivioAllegati.size() - 1;
    }

    public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
        return archivioAllegati;
    }

    public void setArchivioAllegati(
            BulkList<AllegatoGenericoBulk> archivioAllegati) {
        this.archivioAllegati = archivioAllegati;
    }

    public boolean isNotAbledToModifyTipoIstCom() {
        return (hasDettagliPagati() ||
                existARowInventoried() ||
                isEstera() || isSanMarinoSenzaIVA() || isSanMarinoSenzaIVA()
                || (isGenerataDaCompenso() && getPg_fattura_passiva() != null)
                || isStampataSuRegistroIVA());
    }

    public boolean isROStatoTrasmissioneLettera() {
        if (lettera_pagamento_estero == null)
            return true;
        if (!lettera_pagamento_estero.getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO))
            return true;
        return false;
    }

    public boolean isSanMarinoConIVA() {

        if ((getFl_san_marino_con_iva() != null && getFl_san_marino_con_iva().booleanValue()))
            return true;

        return false;
    }

    public java.sql.Timestamp getDt_termine_creazione_docamm() {
        return dt_termine_creazione_docamm;
    }

    public void setDt_termine_creazione_docamm(
            java.sql.Timestamp dt_termine_creazione_docamm) {
        this.dt_termine_creazione_docamm = dt_termine_creazione_docamm;
    }

    public boolean isROFl_split_payment() {
        return isElettronica() ||
                (getFl_intra_ue() != null && getFl_intra_ue()) ||
                (getFl_extra_ue() != null && getFl_extra_ue()) ||
                (getFl_autofattura() != null && getFl_autofattura()) ||
                isAbledToModifyFlagsTipoFattura();
    }

    public java.sql.Timestamp getDataInizioSplitPayment() {
        return dataInizioSplitPayment;
    }

    public void setDataInizioSplitPayment(java.sql.Timestamp dataInizioSplitPayment) {
        this.dataInizioSplitPayment = dataInizioSplitPayment;
    }

    public boolean isGestioneSplitPayment() {
        return this.getDt_fattura_fornitore() != null && this.getDataInizioSplitPayment() != null &&
                !DateUtils.truncate(this.getDt_fattura_fornitore()).before(DateUtils.truncate(this.getDataInizioSplitPayment()));
    }

    public boolean isROFlDaOrdini() {
        return Optional.ofNullable(fattura_passiva_dettColl)
                .filter(bulkList -> !bulkList.isEmpty())
                .map(bulkList -> bulkList.stream())
                .map(stream -> stream.anyMatch(fattura_attiva_rigaBulk -> !((Fattura_passiva_rigaBulk)fattura_attiva_rigaBulk).isStatoIniziale()))
                .orElse(false);
    }

    public BulkList<DocumentoEleAcquistoBulk> getDocEleAcquistoColl() {
        return docEleAcquistoColl;
    }

    public void setDocEleAcquistoColl(BulkList<DocumentoEleAcquistoBulk> docEleAcquistoColl) {
        this.docEleAcquistoColl = docEleAcquistoColl;
    }

    public List<String> getStorePath() {
        return Optional.ofNullable(getDocumentoEleTestata())
                .map(DocumentoEleTestataBulk::getDocumentoEleTrasmissione)
                .map(DocumentoEleTrasmissioneBase::getCmisNodeRef)
                .map(s -> {
                    return Optional.ofNullable(SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(s))
                            .map(StorageObject::getPath)
                            .map(path -> Arrays.asList(path))
                            .orElse(Collections.emptyList());
                })
                .orElse(Collections.emptyList());
    }

    public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
        return scrittura_partita_doppia;
    }

    public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
        this.scrittura_partita_doppia = scrittura_partita_doppia;
    }

    public TipoDocumentoEnum getTipoDocumentoEnum() {
        if ("C".equals(this.getTi_fattura()))
            return TipoDocumentoEnum.fromValue(TipoDocumentoEnum.TIPO_NOTA_CREDITO_PASSIVA);
        if ("D".equals(this.getTi_fattura()))
            return TipoDocumentoEnum.fromValue(TipoDocumentoEnum.TIPO_NOTA_DEBITO_PASSIVA);
        return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc_amm());
    }

    public boolean registraIvaCoge() {
        if (this.isIstituzionale()) {
            if (Bene_servizioBulk.BENE.equalsIgnoreCase(this.getTi_bene_servizio())) {
                if (this.isSanMarinoSenzaIVA() || this.getFl_intra_ue().booleanValue() || this.getFl_merce_intra_ue().booleanValue())
                    return true;
            }
            if (Bene_servizioBulk.SERVIZIO.equalsIgnoreCase(this.getTi_bene_servizio())) {
                if (this.getTipo_sezionale().getFl_servizi_non_residenti().booleanValue())
                    return true;
            }
            if (this.getFl_split_payment().booleanValue())
                return true;
        }
        return false;
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
        return this.getDt_registrazione();
    }
}
