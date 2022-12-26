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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attiva_rigaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.UtilService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.Persister;
import it.cnr.jada.util.OrderedHashtable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class ObbligazioneBulk extends ObbligazioneBase implements Cloneable, IDocumentoContabileBulk, AllegatoParentBulk {
	private static final long serialVersionUID = 1L;

	private it.cnr.jada.util.OrderedHashtable anniResidui = new it.cnr.jada.util.OrderedHashtable();
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
	private it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk tipo_obbligazione = new it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk();
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk creditore = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
	private OrderedHashtable esercizio_competenza_Keys;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds = new CdsBulk();
	private Fondo_ricercaBulk fondo_ricerca;
	private ContrattoBulk contratto = new ContrattoBulk();	
	private Incarichi_repertorioBulk incarico_repertorio = new Incarichi_repertorioBulk();
	
	private BulkList obbligazione_scadenzarioColl = new BulkList();
	private Collection capitoliDiSpesaCdsColl = Collections.EMPTY_LIST;
	private Collection capitoliDiSpesaCdsSelezionatiColl = Collections.EMPTY_LIST;
	private Collection cdrColl = Collections.EMPTY_LIST;
	private Collection cdrSelezionatiColl = Collections.EMPTY_LIST;	
	private Collection lineeAttivitaColl = Collections.EMPTY_LIST;	
	private Collection lineeAttivitaSelezionateColl = Collections.EMPTY_LIST;
	private BulkList nuoveLineeAttivitaColl = new BulkList();
	private boolean enableVoceNext = false;
	private List<Elemento_voceBulk> listaVociSelezionabili = null;

	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();

	public final static String STATO_OBB_PROVVISORIO 	= "P";
	public final static String STATO_OBB_DEFINITIVO 	= "D";
	public final static String STATO_OBB_STORNATO 		= "S";

	public final static Dictionary stato_obbligazioneKeys;

	static
	{
		stato_obbligazioneKeys = new OrderedHashtable();
		stato_obbligazioneKeys.put("P", "Provvisorio");
		stato_obbligazioneKeys.put("D", "Definitivo");
		stato_obbligazioneKeys.put("S", "Stornato");
	}

	public final static String TIPO_COMPETENZA		  = "C";
	public final static String TIPO_RESIDUO_PROPRIO	  = "R";
	public final static String TIPO_RESIDUO_IMPROPRIO = "I";
	public final static Dictionary competenzaResiduoKeys;

	static 
	{
		competenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
		competenzaResiduoKeys.put(TIPO_COMPETENZA, "Competenza");
		competenzaResiduoKeys.put(TIPO_RESIDUO_PROPRIO,	"Residuo Proprio");
		competenzaResiduoKeys.put(TIPO_RESIDUO_IMPROPRIO, "Residuo Improprio");
	};	

	public final static int INT_STATO_UNDEFINED				= 0;	
	public final static int INT_STATO_TESTATA_CONFERMATA	= 1;
	public final static int INT_STATO_CAPITOLI_CONFERMATI	= 2;
	public final static int INT_STATO_CDR_CONFERMATI	    = 3;	
	public final static int INT_STATO_LATT_CONFERMATE		= 4;

	private int internalStatus = INT_STATO_UNDEFINED;

	private boolean checkDisponibilitaCassaEseguito = false;
	private boolean checkDisponibilitaContrattoEseguito = false;
	private boolean checkDisponibilitaCdrGAEEseguito = false;
	private boolean checkDisponibilitaIncaricoRepertorioEseguito = false;

	//valori salvati alla rilettura da db
	private java.math.BigDecimal im_iniziale_obbligazione;
	private String cd_iniziale_elemento_voce;
	private Integer cd_terzo_iniziale;

	private	Collection cdrAggregatoColl; 
	private Collection capitoliAggregatoColl;

	private java.math.BigDecimal im_mandati;
	private java.math.BigDecimal im_parz_scadenze;
	private java.math.BigDecimal im_residuo_obbligazione;

	//Variabili utilizzate per indicare la Uo e la GAE di destinazione su cui devono essere girati
	// gli stanziamenti e creato l'impegno
	//Attiva solo se parametro isVariazioneAutomaticaSpesa è attivo
	private WorkpackageBulk gaeDestinazioneFinale;

	private boolean fromDocAmm = false;

	private BulkList<Obbligazione_pluriennaleBulk> obbligazioniPluriennali = new BulkList<Obbligazione_pluriennaleBulk>();

public ObbligazioneBulk() {
	super();
}
public ObbligazioneBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione) {
	super(cd_cds,esercizio,esercizio_originale,pg_obbligazione);
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
}
/**
 * Aggiunge una nuova linea di attività (Linea_attivitaBulk) alla lista di linee di attività
 * definite per l'obbligazione inizializzandone alcuni campi
 * @param latt Linea di attività da aggiungere alla lista
 * @return int
 */

public int addToNuoveLineeAttivitaColl( Linea_attivitaBulk latt ) 
{
	latt.getLinea_att().setCentro_responsabilita( new CdrBulk());
//	latt.getLinea_att().setEsercizio( getEsercizio_competenza() );
	latt.setObbligazione( this );
	nuoveLineeAttivitaColl.add(latt);
//	latt.setObbligazione(this);
	return nuoveLineeAttivitaColl.size()-1;
}
/**
 * Aggiunge una nuova scadenza (Obbligazione_scadenzarioBulk) alla lista di scadenze definite per l'obbligazione
 * inizializzandone alcuni campi
 * @param os scadenza da aggiungere alla lista
 * @return int
 */

public int addToObbligazione_scadenzarioColl( Obbligazione_scadenzarioBulk os ) 
{
	obbligazione_scadenzarioColl.add(os);
	os.setObbligazione(this);
	os.setIm_associato_doc_amm( new java.math.BigDecimal(0));
	os.setIm_associato_doc_contabile( new java.math.BigDecimal(0));
	os.setFlAssociataOrdine(false);
	os.setIm_scadenza( new java.math.BigDecimal(0));
	if ( os.getPg_obbligazione_scadenzario() == null )
		os.setPg_obbligazione_scadenzario( getNextPgScadenza());
//	os.setToBeCreated();
	return obbligazione_scadenzarioColl.size()-1;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param notaDiCredito	
 * @param dettagli	
 */
public void completeFrom(
	it.cnr.jada.action.ActionContext context,
	it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk notaDiCredito,
	List dettagli) {

	//setEsercizio( notaDiCredito.getEsercizio() );
	//setEsercizio_competenza(notaDiCredito.getEsercizio());
	
	//unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);

	//setUnita_organizzativa(unita_organizzativa);
	
	//setCds(unita_organizzativa.getUnita_padre());

	//setCd_uo_origine(notaDiCredito.getCd_uo_origine());
	//setCd_cds_origine(notaDiCredito.getCd_cds_origine());
	
//	setStato_contabile("A" );
	setStato_obbligazione(ObbligazioneOrdBulk.STATO_OBB_DEFINITIVO);
	setCreditore(notaDiCredito.getCliente());
	setDs_obbligazione("Impegno per " + 
						(notaDiCredito.getDs_fattura_attiva() == null ?
							"nota di credito" :
							notaDiCredito.getDs_fattura_attiva()));

	setDt_registrazione(notaDiCredito.getDt_registrazione());
	setDt_scadenza_contratto(notaDiCredito.getDt_scadenza());

	setFl_calcolo_automatico(Boolean.TRUE);
	
	java.math.BigDecimal importo = new java.math.BigDecimal(0);
	if (dettagli != null) {
		for (Iterator i = dettagli.iterator(); i.hasNext();) {
			Nota_di_credito_attiva_rigaBulk dettaglio = (Nota_di_credito_attiva_rigaBulk) i.next();
			java.math.BigDecimal imTotaleDettaglio = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
			importo = importo.add(imTotaleDettaglio);
		}
	}

	importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	
	Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk(this);
	scadenza.setUser(getUser());
	scadenza.setToBeCreated();
	addToObbligazione_scadenzarioColl(scadenza);
	java.sql.Timestamp ts = notaDiCredito.getDt_registrazione();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
	scadenza.setDs_scadenza("Scadenza per nota di credito del " + sdf.format(ts));
	java.sql.Timestamp dtScadenza = notaDiCredito.getDt_scadenza();
	if (dtScadenza == null)
		dtScadenza = ts;
	scadenza.setDt_scadenza(dtScadenza);
	scadenza.setIm_associato_doc_amm(importo);
	scadenza.setIm_scadenza(importo);
		
	setIm_obbligazione(importo);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param cd_funzione	
 * @param cd_cdr	
 * @return 
 */
public it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk getArticolo( String cd_funzione, String cd_cdr ) 
{
	it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk voce;
	for ( Iterator i = getCapitoliDiSpesaCdsSelezionatiColl().iterator(); i.hasNext(); )
	{
		voce = (it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk) i.next();
		if ( voce.getCd_funzione().equals( cd_funzione ) && (!(voce instanceof Voce_fBulk) || ((Voce_fBulk)voce).getCd_centro_responsabilita().equals( cd_cdr ) ))
			return voce;
	}
	return null;	
	
}
/**
 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
 * bulk da rendere persistenti insieme al ricevente.
 * L'implementazione standard restituisce <code>null</code>.
 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
 */ 
public BulkCollection[] getBulkLists() {
	 return new it.cnr.jada.bulk.BulkCollection[] { 
			obbligazione_scadenzarioColl,obbligazioniPluriennali  };
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCapitoliAggregatoColl() {
	return capitoliAggregatoColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCapitoliDiSpesaCdsColl() {
	return capitoliDiSpesaCdsColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCapitoliDiSpesaCdsSelezionatiColl() {
	return capitoliDiSpesaCdsSelezionatiColl;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param cd_funzione	
 * @return 
 */
public it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk getCapitolo( String cd_funzione ) 
{
	it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk voce;
	for ( Iterator i = getCapitoliDiSpesaCdsSelezionatiColl().iterator(); i.hasNext(); )
	{
		voce = (it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk) i.next();
		if ( voce.getCd_funzione().equals( cd_funzione ) )
			return voce;
	}
	return null;	
	
}
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_iniziale_elemento_voce'
 *
 * @return Il valore della proprietà 'cd_iniziale_elemento_voce'
 */
public java.lang.String getCd_iniziale_elemento_voce() {
	return cd_iniziale_elemento_voce;
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk creditore = this.getCreditore();
	if (creditore == null)
		return null;
	return creditore.getCd_terzo();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_terzo_iniziale'
 *
 * @return Il valore della proprietà 'cd_terzo_iniziale'
 */
public java.lang.Integer getCd_terzo_iniziale() {
	return cd_terzo_iniziale;
}
public java.lang.String getCd_tipo_obbligazione() {
	it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk tipo_obbligazione = this.getTipo_obbligazione();
	if (tipo_obbligazione == null)
		return null;
	return tipo_obbligazione.getCd_tipo_obbligazione();
}
public java.lang.String getCd_unita_organizzativa() {
	Unita_organizzativaBulk uo = this.getUnita_organizzativa();
	if (uo == null)
		return null;
	return uo.getCd_unita_organizzativa();
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCdrAggregatoColl() {
	return cdrAggregatoColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCdrColl() {
	return cdrColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCdrDiScrivaniaSelezionatiColl( String cd_unita_organizzativa ) {
	List cdrColl = new LinkedList();
	if (isSpesePerCostiAltrui() )
	{
		CdrBulk cdr ;
		for ( Iterator i = getCdrSelezionatiColl().iterator(); i.hasNext(); )
		{
			cdr = (CdrBulk) i.next();
			if ( cdr.getCd_unita_organizzativa().equals( cd_unita_organizzativa ))
				cdrColl.add( cdr );
		}	
	}
	else
		cdrColl = (List) getCdrSelezionatiColl();

	return cdrColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getCdrSelezionatiColl() {
	return cdrSelezionatiColl;
}
/**
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getCreditore() {
	return creditore;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ds_creditore'
 *
 * @return Il valore della proprietà 'ds_creditore'
 */
public java.lang.String getDs_creditore() {
	/*
	if ( creditore != null && creditore.getAnagrafico() != null )
		return creditore.getAnagrafico().getCognome() + " " + creditore.getAnagrafico().getNome();
	return "";
	*/
	if ( creditore != null )
		return creditore.getDenominazione_sede();
	return "";	
}
/**
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable getEsercizio_competenza_Keys() {
	return esercizio_competenza_Keys;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.Fondo_ricercaBulk
 */
public Fondo_ricercaBulk getFondo_ricerca() {
	return fondo_ricerca;
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_iniziale_obbligazione'
 *
 * @return Il valore della proprietà 'im_iniziale_obbligazione'
 */
public java.math.BigDecimal getIm_iniziale_obbligazione() {
	return im_iniziale_obbligazione;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_mandati'
 *
 * @return Il valore della proprietà 'im_mandati'
 */
public java.math.BigDecimal getIm_mandati() {
	return im_mandati;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_parz_scadenze'
 *
 * @return Il valore della proprietà 'im_parz_scadenze'
 */
public java.math.BigDecimal getIm_parz_scadenze() 
{
	Obbligazione_scadenzarioBulk scadenza ;
	im_parz_scadenze = new java.math.BigDecimal( 0 );
	for ( Iterator i = getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		scadenza = (Obbligazione_scadenzarioBulk)i.next();
		if ( scadenza.getIm_scadenza() != null )
			im_parz_scadenze = im_parz_scadenze.add( scadenza.getIm_scadenza());
	}	
	return im_parz_scadenze;
}
/**
 * Calcola l'importo residuo dell'obbligazione, che è dato dalla differenza tra l'importo totale
 * dell'obbligazione e la somma degli importi delle scadenze già inserite.
 * @return im_residuo_obbligazione L'importo residuo dell'obbligazione
 */
public java.math.BigDecimal getIm_residuo_obbligazione() 
{
	return Optional.ofNullable(getIm_obbligazione()).orElse(BigDecimal.ZERO).subtract(
			Optional.ofNullable(getIm_parz_scadenze()).orElse(BigDecimal.ZERO));
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'internalStatus'
 *
 * @return Il valore della proprietà 'internalStatus'
 */
public int getInternalStatus() {
	return internalStatus;
}
/**
 * @return java.util.Collection
 */
public Collection getLineeAttivitaColl() {
	return lineeAttivitaColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getLineeAttivitaSelezionateColl() {
	return lineeAttivitaSelezionateColl;
}
public String getManagerName() {

	return "CRUDObbligazioneBP";
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nextPgScadenza'
 *
 * @return Il valore della proprietà 'nextPgScadenza'
 */
public Long getNextPgScadenza() 
{
	long max = 0;
	Obbligazione_scadenzarioBulk scadenza;
	for ( Iterator i = getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		scadenza = (Obbligazione_scadenzarioBulk)i.next();
		if ( scadenza.getPg_obbligazione_scadenzario() != null && scadenza.getPg_obbligazione_scadenzario().longValue() > max )
			max = scadenza.getPg_obbligazione_scadenzario().longValue();
	}
	return new Long( max + 1);
}
/**
 * @return java.util.Collection
 */
public BulkList getNuoveLineeAttivitaColl() {
	return nuoveLineeAttivitaColl;
}
/**
 * @return it.cnr.jada.bulk.BulkCollection
 */
public it.cnr.jada.bulk.BulkList<Obbligazione_scadenzarioBulk> getObbligazione_scadenzarioColl() {
	return obbligazione_scadenzarioColl;
}
public java.lang.Long getPg_doc_contabile() {
	return getPg_obbligazione();
}
public Map getSaldiInfo() 
{
	HashMap values = new HashMap();
	values.put("pg_ver_rec", getPg_ver_rec());
	return values;
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public Dictionary getStato_obbligazioneKeys() {
	return stato_obbligazioneKeys;
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
public String getTi_entrata_spesa() {
	return TI_SPESA;
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
public java.lang.String getTi_competenza_residuo() 
{
	if ( Numerazione_doc_contBulk.TIPO_OBB_RES.equals( getCd_tipo_documento_cont() ) ||
	     Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA.equals( getCd_tipo_documento_cont() ))
		return MandatoBulk.TIPO_RESIDUO;
	return MandatoBulk.TIPO_COMPETENZA;		
}
/**
 * @return it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk
 */
public it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk getTipo_obbligazione() {
	return tipo_obbligazione;
}
/**
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'vociMap'
 *
 * @return Il valore della proprietà 'vociMap'
 */
public PrimaryKeyHashMap getVociMap(boolean flNuovoPdg ) 
{
	PrimaryKeyHashMap map = new PrimaryKeyHashMap();
	Obbligazione_scad_voceBulk osv;
	BigDecimal im_voce;
	IVoceBilancioBulk voce;

	for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
		for ( Iterator j = ((Obbligazione_scadenzarioBulk) i.next()).getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) j.next();
			if (flNuovoPdg)
				voce = new Elemento_voceBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
			else
				voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
			
			im_voce = (BigDecimal) map.get( voce );
			if ( im_voce == null )
				map.put( voce, osv.getIm_voce() );
			else
				map.put( voce, im_voce.add( osv.getIm_voce()) );
		}
	return map;		
}

public PrimaryKeyHashMap getElementoVociMap( ) 
{
	return getVociMap(true);
}

public PrimaryKeyHashMap getVoceFMap( ) 
{
	return getVociMap(false);
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'Obbligazione_scad_voceMap'
 *
 * @return Il valore della proprietà 'Obbligazione_scad_voceMap'
 */
public PrimaryKeyHashMap getObbligazione_scad_voceMap(  )
{
		PrimaryKeyHashMap map = new PrimaryKeyHashMap();
		Obbligazione_scad_voceBulk osv;
		BigDecimal im_voce;
		for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
				for ( Iterator j = ((Obbligazione_scadenzarioBulk) i.next()).getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
				{
						osv = (Obbligazione_scad_voceBulk) j.next();
						map.put( osv, osv.getIm_voce() );
				}
		return map;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @return 
 */
public boolean hasDettagli()
{
	return  obbligazione_scadenzarioColl != null &&
		 	obbligazione_scadenzarioColl.size() > 0  &&
		 	((Obbligazione_scadenzarioBulk) obbligazione_scadenzarioColl.get(0)).getObbligazione_scad_voceColl() != null &&
		 	((Obbligazione_scadenzarioBulk) obbligazione_scadenzarioColl.get(0)).getObbligazione_scad_voceColl().size() > 0 ;

}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
	setEsercizio_originale( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
	setEsercizio_competenza( getEsercizio() ) ;
	setCd_uo_origine( ((CNRUserContext) context.getUserContext()).getCd_unita_organizzativa());
	setCd_cds_origine( ((CNRUserContext) context.getUserContext()).getCd_cds());		
	unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds( unita_organizzativa.getCd_unita_padre());

	setFl_calcolo_automatico(Boolean.TRUE);
	if (this instanceof ObbligazioneResBulk)  
		setStato_obbligazione( STATO_OBB_DEFINITIVO );
	else
		setStato_obbligazione( STATO_OBB_PROVVISORIO );

	setFl_spese_costi_altrui(Boolean.FALSE);
	setFl_gara_in_corso(Boolean.FALSE);
	setFl_determina_allegata(Boolean.FALSE);
	setRiportato("N");

	// I seguenti campi sono definiti temporaneamente, ma DA CANCELLARE
	setIm_costi_anticipati( new java.math.BigDecimal(0) );

	getCreditore().setAnagrafico( new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
	return super.initializeForInsert(bp, context);
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio( ((CNRUserContext)context.getUserContext()).getEsercizio() );
	setEsercizio_competenza( ((CNRUserContext)context.getUserContext()).getEsercizio() );
//	unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_uo_origine( ((CNRUserContext)context.getUserContext()).getCd_unita_organizzativa());
	getCreditore().setAnagrafico( new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());	
	return super.initializeForSearch(bp,context);
}
/**
 * Verifica se il bottone di Aggiungi delle Latt e' abilitato
 */
public boolean isAddNuoveLattEnabled()
{
//	return getInternalStatus() != INT_STATO_TESTATA_CONFERMATA && getInternalStatus() >= INT_STATO_CAPITOLI_CONFERMATI ;
	return getCapitoliDiSpesaCdsSelezionatiColl() != null && getCapitoliDiSpesaCdsSelezionatiColl().size() > 0;
}	
/**
 * Verifica se l'obbligazione è associata a documenti amministrativi.
 * @return 				TRUE 	L'obbligazione è associata a documenti amministrativi
 *						FALSE 	L'obbligazione non è associata a documenti amministrativi
 */
public boolean isAssociataADocAmm() 
{
	for ( Iterator<Obbligazione_scadenzarioBulk> i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); ) {
		Obbligazione_scadenzarioBulk bulk = i.next();
		if ( bulk.getPg_doc_passivo() != null ||  bulk.getIm_associato_doc_amm().compareTo( new BigDecimal(0)) != 0 )	
			return true;
	}
	return false;
}
/**
 * Verifica se è stato eseguito il controllo sulla disponibilità di cassa.
 * @return 				TRUE 	Il controllo sulla disponibilità di cassa è stato eseguito
 *						FALSE 	Il controllo sulla disponibilità di cassa non è stato eseguito
 */
public boolean isCheckDisponibilitaCassaEseguito() {
	return checkDisponibilitaCassaEseguito;
}
/**
 * Verifica se il bottone di Conferma dei Capitoli è abilitato.
 * @return 				TRUE 	Il bottone di Conferma dei Capitoli è abilitato
 *						FALSE 	Il bottone di Conferma dei Capitoli non è abilitato
 */
public boolean isConfermaCapitoliEnabled()
{
	return (getInternalStatus() >= INT_STATO_TESTATA_CONFERMATA) && getCapitoliDiSpesaCdsColl().size() > 0;
}	
/**
 * Verifica se il bottone di Conferma dei Centri di Responsabilità è abilitato.
 * @return 				TRUE 	Il bottone di Conferma dei Centri di Responsabilità è abilitato
 *						FALSE 	Il bottone di Conferma dei Centri di Responsabilità non è abilitato
 */
public boolean isConfermaCentriDiResponsabilitaEnabled()
{
	return (getInternalStatus() >= INT_STATO_CAPITOLI_CONFERMATI) && getCdrColl().size() > 0 ;
}	
/**
 * Verifica se il bottone di Conferma delle Linee di Attività è abilitato.
 * @return 				TRUE 	Il bottone di Conferma delle Linee di Attività è abilitato
 *						FALSE 	Il bottone di Conferma delle Linee di Attività non è abilitato
 */
public boolean isConfermaLineeAttivitaEnabled()
{
	return (getInternalStatus() >= INT_STATO_CDR_CONFERMATI ) || 
	       (getNuoveLineeAttivitaColl() != null && getNuoveLineeAttivitaColl().size() > 0 );
}	
public boolean isControparteRiportatata()
{
	return false;
}
public boolean isDocRiportato()
{
	
	if ( "Y".equals( getRiportato()))
		return true;
	return false;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'fromDocAmm'
 *
 * @return Il valore della proprietà 'fromDocAmm'
 */
public boolean isFromDocAmm() {
	return fromDocAmm;
}
public boolean isInitialized()
{
	return false;
}
public boolean isCompetenza() {
	return getEsercizio() != null && getEsercizio_originale() != null &&
	       getEsercizio().compareTo(getEsercizio_originale()) == 0;
}
public boolean isResiduo() {
	return getCd_tipo_documento_cont() != null && 
	      (Numerazione_doc_contBulk.TIPO_IMP_RES.equals(getCd_tipo_documento_cont())||
	       Numerazione_doc_contBulk.TIPO_OBB_RES.equals(getCd_tipo_documento_cont()));
}
public boolean isImpegnoResiduo() {
	return getCd_tipo_documento_cont() != null && 
		   Numerazione_doc_contBulk.TIPO_IMP_RES.equals(getCd_tipo_documento_cont());
}
public boolean isObbligazioneResiduo() {
	return getCd_tipo_documento_cont() != null && 
		   Numerazione_doc_contBulk.TIPO_OBB_RES.equals(getCd_tipo_documento_cont());
}
public boolean isObbligazioneResiduoImproprio() {
	return getCd_tipo_documento_cont() != null && 
	       Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA.equals(getCd_tipo_documento_cont());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOCreditore'
 *
 * @return Il valore della proprietà 'rOCreditore'
 */
public boolean isROCreditore() {
	return creditore == null || creditore.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOContratto'
 *
 * @return Il valore della proprietà 'rOContratto'
 */
public boolean isROContratto() {
	return contratto == null || contratto.getCrudStatus() == NORMAL;
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOContratto'
 *
 * @return Il valore della proprietà 'ROIncaricoRepertorio'
 */
public boolean isROIncaricoRepertorio() {
	return incarico_repertorio == null || incarico_repertorio.getCrudStatus() == NORMAL;
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOElemento_voce'
 *
 * @return Il valore della proprietà 'rOElemento_voce'
 */
public boolean isROElemento_voce() {
	return elemento_voce == null || elemento_voce.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOTipo_obbligazione'
 *
 * @return Il valore della proprietà 'rOTipo_obbligazione'
 */
public boolean isROTipo_obbligazione() {
	return tipo_obbligazione == null || tipo_obbligazione.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'spesePerCostiAltrui'
 *
 * @return Il valore della proprietà 'spesePerCostiAltrui'
 */
public boolean isSpesePerCostiAltrui()
{
	return ( getFl_spese_costi_altrui() != null && getFl_spese_costi_altrui().booleanValue() );
}
public boolean isTemporaneo() {

	if (getPg_obbligazione() == null)
		return false;
	return getPg_obbligazione().longValue() < 0;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void refreshCapitoliDiSpesaCdsSelezionatiColl() 
{
	Hashtable capitoli = new Hashtable(); // hashtable per evitare i doppi
	
	//l'imputazione finanziaria e' sempre di testata: seleziono i dettagli di una qualsiasi scadenza per
	// individuare l'elenco dei capitoli di spesa selezionati per l'intera obbligazione
	for ( Iterator s = ((Obbligazione_scadenzarioBulk) obbligazione_scadenzarioColl.iterator().next()).getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) s.next();
		for ( Iterator c = capitoliDiSpesaCdsColl.iterator(); c.hasNext(); )
		{
			it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk voce = ( it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk) c.next();
			if ( osv.getCd_voce().equals( voce.getCd_voce() ))
				capitoli.put ( osv.getCd_voce(), voce );
		}
	}

	capitoliDiSpesaCdsSelezionatiColl = new Vector();
	for ( Enumeration e = capitoli.keys(); e.hasMoreElements(); )
		capitoliDiSpesaCdsSelezionatiColl.add( capitoli.get( e.nextElement() ));
		
}
/**
 * Seleziona tra tutti i capitoli di spesa collegati quelli presenti in vociList 
 *
 * @param vociList List contenente oggetti di tipo V_assestatoBulk 
 * @return void  riempie l'oggetto capitoliDiSpesaCdsSelezionatiColl
 */
public void refreshCapitoliDiSpesaCdsSelezionatiColl(java.util.List vociList)
{
	Hashtable capitoli = new Hashtable(); // hashtable per evitare i doppi
	
	// individuo l'elenco dei capitoli di spesa selezionati per l'intera obbligazione
	for ( Iterator s = vociList.iterator(); s.hasNext(); )
	{
		OggettoBulk voceSel = (OggettoBulk)s.next();
		for ( Iterator c = capitoliDiSpesaCdsColl.iterator(); c.hasNext(); )
		{
			it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk voce = ( it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk) c.next();
			if (voceSel instanceof V_assestatoBulk)  
				if ( ((V_assestatoBulk)voceSel).getCd_voce().equals( voce.getCd_voce() ))
					capitoli.put ( ((V_assestatoBulk)voceSel).getCd_voce(), voce );
		}
	}

	capitoliDiSpesaCdsSelezionatiColl = new Vector();
	for ( Enumeration e = capitoli.keys(); e.hasMoreElements(); )
		capitoliDiSpesaCdsSelezionatiColl.add( capitoli.get( e.nextElement() ));
		
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void refreshCdrSelezionatiColl() 
{
	Hashtable cdrTable = new Hashtable(); // hashtable per evitare i doppi
	
	//l'imputazione finanziaria e' sempre di testata: seleziono i dettagli di una qualsiasi scadenza per
	// individuare l'elenco dei cdr selezionati per l'intera obbligazione
	for ( Iterator s = ((Obbligazione_scadenzarioBulk) obbligazione_scadenzarioColl.iterator().next()).getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) s.next();
		for ( Iterator c = cdrColl.iterator(); c.hasNext(); )
		{
			CdrBulk cdr = ( CdrBulk) c.next();
			if ( osv.getLinea_attivita().getCentro_responsabilita().getCd_centro_responsabilita().equals( cdr.getCd_centro_responsabilita() ))
				cdrTable.put ( osv.getLinea_attivita().getCentro_responsabilita().getCd_centro_responsabilita(), cdr );
		}
	}

	cdrSelezionatiColl = new Vector();
	for ( Enumeration e = cdrTable.keys(); e.hasMoreElements(); )
		cdrSelezionatiColl.add( cdrTable.get( e.nextElement() ));
}
/**
 * Seleziona tra tutti i capitoli di spesa collegati quelli presenti in vociList 
 *
 * @param vociList List contenente oggetti di tipo V_assestatoBulk 
 * @return void  riempie l'oggetto cdrSelezionatiColl
 */
public void refreshCdrSelezionatiColl(java.util.List vociList)
{
	Hashtable cdrTable = new Hashtable(); // hashtable per evitare i doppi
	
	// individuo l'elenco dei capitoli di spesa selezionati per l'intera obbligazione
	for ( Iterator s = vociList.iterator(); s.hasNext(); )
	{
		OggettoBulk voceSel = (OggettoBulk) s.next();
		for ( Iterator c = cdrColl.iterator(); c.hasNext(); )
		{
			CdrBulk cdr = ( CdrBulk) c.next();
			if (voceSel instanceof V_assestatoBulk)  
				if ( ((V_assestatoBulk)voceSel).getCd_centro_responsabilita().equals( cdr.getCd_centro_responsabilita() ))
					cdrTable.put ( ((V_assestatoBulk)voceSel).getCd_centro_responsabilita(), cdr );
		}
	}

	cdrSelezionatiColl = new Vector();
	for ( Enumeration e = cdrTable.keys(); e.hasMoreElements(); )
		cdrSelezionatiColl.add( cdrTable.get( e.nextElement() ));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void refreshDettagliScadenzarioPerCdrECapitoli() 
{
//	Hashtable cdrTotaleTable = new Hashtable();
	Hashtable cdrScadenzaTable = new Hashtable();	
//	Hashtable capitoliTotaleTable = new Hashtable();
	Hashtable capitoliScadenzaTable = new Hashtable();
	String key;
	Obbligazione_scadenzarioBulk os;
	Obbligazione_scad_voceBulk osv;
	Obbligazione_scad_voce_aggregatoBulk osva;
	
	
	for ( Iterator s = obbligazione_scadenzarioColl.iterator(); s.hasNext(); )
	{
		os = (Obbligazione_scadenzarioBulk) s.next();
		for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) d.next();
			/* // totale per Cdr
			osva = (Obbligazione_scad_voce_aggregatoBulk) cdrTotaleTable.get( osv.getCd_centro_responsabilita());			
			if ( osva == null )
				cdrTotaleTable.put( osv.getCd_centro_responsabilita(), 
					new Obbligazione_scad_voce_aggregatoBulk( osv.getCd_centro_responsabilita(), null, osv.getIm_voce()) );
			else
			{
				osva.setImporto( osva.getImporto().add( osv.getIm_voce()));
				cdrTotaleTable.put( osv.getCd_centro_responsabilita(), osva );
			} */	
			// totale per Cdr e per scadenza				
			key = osv.getCd_centro_responsabilita() + os.getDt_scadenza().toString();
			osva = (Obbligazione_scad_voce_aggregatoBulk) cdrScadenzaTable.get( key );			
			if ( osva == null )
				cdrScadenzaTable.put( key, 
					new Obbligazione_scad_voce_aggregatoBulk( osv.getCd_centro_responsabilita(), os.getDt_scadenza(), osv.getIm_voce()) );				
			else
			{
				osva.setImporto( osva.getImporto().add( osv.getIm_voce()));
				cdrScadenzaTable.put( key, osva );
			}			
			/* // totale per Capitoli
			osva = (Obbligazione_scad_voce_aggregatoBulk) capitoliTotaleTable.get( osv.getCd_voce() );		
			if ( osva == null )
				capitoliTotaleTable.put( osv.getCd_voce(),
					new Obbligazione_scad_voce_aggregatoBulk( osv.getCd_voce(), null, osv.getIm_voce()) );					
			else
			{
				osva.setImporto( osva.getImporto().add( osv.getIm_voce()));
				capitoliTotaleTable.put( osv.getCd_voce(), osva );
			}
			*/
		// totale per Capitoli e per scadenza				
			key = osv.getCd_voce() + os.getDt_scadenza().toString();
			osva = (Obbligazione_scad_voce_aggregatoBulk) capitoliScadenzaTable.get( key );			
			if ( osva == null )
				capitoliScadenzaTable.put( key,
					new Obbligazione_scad_voce_aggregatoBulk( osv.getCd_voce(), os.getDt_scadenza(), osv.getIm_voce()) );									
			else
			{
				osva.setImporto( osva.getImporto().add( osv.getIm_voce()));
				capitoliScadenzaTable.put( key, osva );
			}			
			
		}
	}

	cdrAggregatoColl = new Vector();
	capitoliAggregatoColl = new Vector();

//	for ( Enumeration e = cdrTotaleTable.keys(); e.hasMoreElements(); )
//		cdrAggregatoColl.add( cdrTotaleTable.get( (String) e.nextElement()));
	for ( Enumeration e = cdrScadenzaTable.keys(); e.hasMoreElements(); )
		cdrAggregatoColl.add( cdrScadenzaTable.get( (String) e.nextElement()));
//	for ( Enumeration e = capitoliTotaleTable.keys(); e.hasMoreElements(); )
//		capitoliAggregatoColl.add( capitoliTotaleTable.get( (String) e.nextElement()));
	for ( Enumeration e = capitoliScadenzaTable.keys(); e.hasMoreElements(); )
		capitoliAggregatoColl.add( capitoliScadenzaTable.get( (String) e.nextElement()));

	cdrAggregatoColl = new Obbligazione_scad_voce_aggregatoBulk().ordina( (Vector) cdrAggregatoColl );
	capitoliAggregatoColl = new Obbligazione_scad_voce_aggregatoBulk().ordina( (Vector) capitoliAggregatoColl );	

}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void refreshLineeAttivitaSelezionateColl() 
{
	Hashtable laTable = new Hashtable(); // hashtable per evitare i doppi
	
	//l'imputazione finanziaria e' sempre di testata: seleziono i dettagli di una qualsiasi scadenza per
	// individuare l'elenco delle linee di attivita selezionate per l'intera obbligazione
	for ( Iterator s = ((Obbligazione_scadenzarioBulk) obbligazione_scadenzarioColl.iterator().next()).getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) s.next();
		for ( Iterator l = lineeAttivitaColl.iterator(); l.hasNext(); )
		{
			V_pdg_obbligazione_speBulk latt = (V_pdg_obbligazione_speBulk) l.next();
			if ( osv.getLinea_attivita().getCentro_responsabilita().getCd_centro_responsabilita().equals( latt.getCd_centro_responsabilita() ) &&
				 osv.getLinea_attivita().getCd_linea_attivita().equals( latt.getCd_linea_attivita() ) )
				laTable.put ( osv.getLinea_attivita().getCentro_responsabilita().getCd_centro_responsabilita() + osv.getLinea_attivita().getCd_linea_attivita(), latt );
		}
	}

	lineeAttivitaSelezionateColl = new Vector();
	for ( Enumeration e = laTable.keys(); e.hasMoreElements(); )
		lineeAttivitaSelezionateColl.add( laTable.get( e.nextElement() ));

}
/**
 * Seleziona tra tutti i capitoli di spesa collegati quelli presenti in vociList 
 *
 * @param vociList List contenente oggetti di tipo V_assestatoBulk 
 * @return void  riempie l'oggetto lineeAttivitaSelezionateColl
 */
public void refreshLineeAttivitaSelezionateColl(java.util.List vociList)
{
	Hashtable laTable = new Hashtable(); // hashtable per evitare i doppi
	
	// individuo l'elenco dei capitoli di spesa selezionati per l'intera obbligazione
	for ( Iterator s = vociList.iterator(); s.hasNext(); )
	{
		OggettoBulk voceSel = (OggettoBulk) s.next();
		for ( Iterator l = lineeAttivitaColl.iterator(); l.hasNext(); )
		{
			V_pdg_obbligazione_speBulk latt = (V_pdg_obbligazione_speBulk) l.next();
			if (voceSel instanceof V_assestatoBulk)  
				if ( ((V_assestatoBulk)voceSel).getCd_centro_responsabilita().equals( latt.getCd_centro_responsabilita() ) &&
					 ((V_assestatoBulk)voceSel).getCd_linea_attivita().equals( latt.getCd_linea_attivita() ) )
					laTable.put ( ((V_assestatoBulk)voceSel).getCd_centro_responsabilita() + ((V_assestatoBulk)voceSel).getCd_linea_attivita(), latt );
		}
	}

	lineeAttivitaSelezionateColl = new Vector();
	for ( Enumeration e = laTable.keys(); e.hasMoreElements(); )
		lineeAttivitaSelezionateColl.add( laTable.get( e.nextElement() ));

}
/**
 * Rimuove una linea di attività (Linea_attivitaBulk) dalla lista di linee di attività
 * definite per l'obbligazione.
 * @param index L'indice per scorrere la lista delle linee di attività 
 * @return Linea_attivitaBulk La linea di attività da rimuovere dalla lista
 */
public Linea_attivitaBulk removeFromNuoveLineeAttivitaColl(int index) 
{
	return (Linea_attivitaBulk)nuoveLineeAttivitaColl.remove( index );
}
/**
 * Rimuove una scadenza (Obbligazione_scadenzarioBulk) dalla lista di scadenze definite per l'obbligazione.
 * @param index L'indice per scorrere la lista delle scadenze
 * @return Obbligazione_scadenzarioBulk La scadenza da rimuovere dalla lista
 */
public Obbligazione_scadenzarioBulk removeFromObbligazione_scadenzarioColl(int index) 
{
	return (Obbligazione_scadenzarioBulk)obbligazione_scadenzarioColl.remove(index);
}
/**
 * @param newCapitoliAggregatoColl java.util.Collection
 */
public void setCapitoliAggregatoColl(java.util.Collection newCapitoliAggregatoColl) {
	capitoliAggregatoColl = newCapitoliAggregatoColl;
}
/**
 * @param newCapitoliDiSpesaCdsColl java.util.Collection
 */
public void setCapitoliDiSpesaCdsColl(java.util.Collection newCapitoliDiSpesaCdsColl) {
	capitoliDiSpesaCdsColl = newCapitoliDiSpesaCdsColl;
}
/**
 * @param newCapitoliDiSpesaCdsSelezionatiColl java.util.Collection
 */
public void setCapitoliDiSpesaCdsSelezionatiColl(java.util.Collection newCapitoliDiSpesaCdsSelezionatiColl) {
	capitoliDiSpesaCdsSelezionatiColl = newCapitoliDiSpesaCdsSelezionatiColl;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_iniziale_elemento_voce'
 *
 * @param newCd_iniziale_elemento_voce	Il valore da assegnare a 'cd_iniziale_elemento_voce'
 */
public void setCd_iniziale_elemento_voce(java.lang.String newCd_iniziale_elemento_voce) {
	cd_iniziale_elemento_voce = newCd_iniziale_elemento_voce;
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getCreditore().setCd_terzo(cd_terzo);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_terzo_iniziale'
 *
 * @param newCd_terzo_iniziale	Il valore da assegnare a 'cd_terzo_iniziale'
 */
public void setCd_terzo_iniziale(java.lang.Integer newCd_terzo_iniziale) {
	cd_terzo_iniziale = newCd_terzo_iniziale;
}
public void setCd_tipo_obbligazione(java.lang.String cd_tipo_obbligazione) {
	this.getTipo_obbligazione().setCd_tipo_obbligazione(cd_tipo_obbligazione);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * @param newCdrAggregatoColl java.util.Collection
 */
public void setCdrAggregatoColl(java.util.Collection newCdrAggregatoColl) {
	cdrAggregatoColl = newCdrAggregatoColl;
}
/**
 * @param newCdrColl java.util.Collection
 */
public void setCdrColl(java.util.Collection newCdrColl) {
	cdrColl = newCdrColl;
}
/**
 * @param newCdrSelezionatiColl java.util.Collection
 */
public void setCdrSelezionatiColl(java.util.Collection newCdrSelezionatiColl) {
	cdrSelezionatiColl = newCdrSelezionatiColl;
}
/**
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'checkDisponibilitaCassaEseguito'
 *
 * @param newCheckDisponibilitaCassaEseguito	Il valore da assegnare a 'checkDisponibilitaCassaEseguito'
 */
public void setCheckDisponibilitaCassaEseguito(boolean newCheckDisponibilitaCassaEseguito) {
	checkDisponibilitaCassaEseguito = newCheckDisponibilitaCassaEseguito;
}
/**
 * @param newCreditore it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setCreditore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newCreditore) {
	creditore = newCreditore;
}
/**
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
/**
 * @param newEsercizio_competenza_Keys it.cnr.jada.util.OrderedHashtable
 */
public void setEsercizio_competenza_Keys(it.cnr.jada.util.OrderedHashtable newEsercizio_competenza_Keys) {
	esercizio_competenza_Keys = newEsercizio_competenza_Keys;
}
/**
 * @param newFondo_ricerca it.cnr.contab.doccont00.core.bulk.Fondo_ricercaBulk
 */
public void setFondo_ricerca(Fondo_ricercaBulk newFondo_ricerca) {
	fondo_ricerca = newFondo_ricerca;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'fromDocAmm'
 *
 * @param newFromDocAmm	Il valore da assegnare a 'fromDocAmm'
 */
public void setFromDocAmm(boolean newFromDocAmm) {
	fromDocAmm = newFromDocAmm;
}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_iniziale_obbligazione'
 *
 * @param newIm_iniziale_obbligazione	Il valore da assegnare a 'im_iniziale_obbligazione'
 */
public void setIm_iniziale_obbligazione(java.math.BigDecimal newIm_iniziale_obbligazione) {
	im_iniziale_obbligazione = newIm_iniziale_obbligazione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_mandati'
 *
 * @param newIm_mandati	Il valore da assegnare a 'im_mandati'
 */
public void setIm_mandati(java.math.BigDecimal newIm_mandati) {
	im_mandati = newIm_mandati;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'internalStatus'
 *
 * @param newInternalStatus	Il valore da assegnare a 'internalStatus'
 */
public void setInternalStatus(int newInternalStatus) {
	internalStatus = newInternalStatus;
}
/**
 * @param newLineeAttivitaColl java.util.Collection
 */
public void setLineeAttivitaColl(Collection newLineeAttivitaColl) {
	lineeAttivitaColl = newLineeAttivitaColl;
}
/**
 * @param newLineeAttivitaSelezionateColl java.util.Collection
 */
public void setLineeAttivitaSelezionateColl(java.util.Collection newLineeAttivitaSelezionateColl) {
	lineeAttivitaSelezionateColl = newLineeAttivitaSelezionateColl;
}
/**
 * @param newNuoveLineeAttivitaColl java.util.Collection
 */
public void setNuoveLineeAttivitaColl(BulkList newNuoveLineeAttivitaColl) {
	nuoveLineeAttivitaColl = newNuoveLineeAttivitaColl;
}
/**
 * @param newObbligazione_scadenzarioColl it.cnr.jada.bulk.BulkCollection
 */
public void setObbligazione_scadenzarioColl(it.cnr.jada.bulk.BulkList newObbligazione_scadenzarioColl) {
	obbligazione_scadenzarioColl = newObbligazione_scadenzarioColl;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
/**
 * @param newTipo_obbligazione it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk
 */
public void setTipo_obbligazione(it.cnr.contab.doccont00.tabrif.bulk.Tipo_obbligazioneBulk newTipo_obbligazione) {
	tipo_obbligazione = newTipo_obbligazione;
}
public void setToBeDeleted() 
{
	super.setToBeDeleted();
	/*
	for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
		((Obbligazione_scadenzarioBulk) i.next()).setToBeDeleted();
	for ( Iterator i = obbligazione_scadenzarioColl.deleteIterator(); i.hasNext(); )
		((Obbligazione_scadenzarioBulk) i.next()).setToBeDeleted();
	*/	
		
}
/**
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void storna() 
{
	setStato_obbligazione( STATO_OBB_STORNATO );
	setIm_obbligazione( new java.math.BigDecimal( 0 ));
	setToBeUpdated();
	for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
		((Obbligazione_scadenzarioBulk) i.next()).storna();
	for ( Iterator i = obbligazione_scadenzarioColl.deleteIterator(); i.hasNext(); )
		((Obbligazione_scadenzarioBulk) i.next()).storna();		
	

}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obTable	
 */
public void  updateScadenzeFromDocAmm( it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable obTable )
{
	Obbligazione_scadenzarioBulk os;

	for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
	{
		os = (Obbligazione_scadenzarioBulk) i.next();
		if ( obTable != null && obTable.get( os ) != null)
			os.setFromDocAmm( true );
		else
			os.setFromDocAmm( false );		
	}	
}
/**
 * Metodo con cui si verifica la validità di tutte le nuove linee di attività
 */
public void validaNuoveLatt( ) throws ValidationException 
{
	for ( Iterator i = nuoveLineeAttivitaColl.iterator(); i.hasNext(); )
		((Linea_attivitaBulk) i.next()).validate();
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
		
	// controllo su campo ESERCIZIO di COMPETENZA
		if ( getEsercizio_competenza() == null )
			throw new ValidationException( "Il campo ESERCIZIO deve essere selezionato." );
	
	// controllo su campo DATA REGISTRAZIONE
	if ( getDt_registrazione() == null )
		throw new ValidationException( "Il campo DATA è obbligatorio." );

	//java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	//gc.setTime(getDt_registrazione());
	//if (gc.getTime().compareTo(this.getDt_registrazione()) < 0 )
	java.sql.Timestamp dataRegistrazione = getDt_registrazione();
//	java.sql.Timestamp dataSistema = new java.sql.Timestamp(System.currentTimeMillis());
	java.sql.Timestamp dataSistema;
	try
	{
		dataSistema = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	}
	catch ( Exception e )
	{
		throw new ValidationException( "Impossibile recuperare la data di sistema!");
	}		

	if (dataRegistrazione.after(dataSistema))
		throw new ValidationException( "Non è possibile inserire una data di registrazione posteriore a quella di sistema." );
			
	// controllo su campo DESCRIZIONE
	if ( getDs_obbligazione() == null || getDs_obbligazione().equals("") )
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );

	// controllo su campo TIPO
//	if ( tipo_obbligazione == null || tipo_obbligazione.getCd_tipo_obbligazione() == null )
//		throw new ValidationException( "Il campo TIPOLOGIA è obbligatorio." );

	// controllo su campo CREDITORE
	if ( creditore.getCd_terzo() == null || creditore.getCd_terzo().equals("") )
		throw new ValidationException( "Il campo CREDITORE è obbligatorio." );
	
	// controllo su campo IMPORTO
	if ( ( getIm_obbligazione() == null ) ||
		/* 15/03/2003 simona - rimosso il vincolo che la testata dell'obbligazione deve avere importo diverso da 0 ; 
		infatti nel caso di obbligazione con 1 sola scadenza legata a doc amm che viene messa a 0, anche la testata diventa 0
		*/
		 (getIm_obbligazione().compareTo(new java.math.BigDecimal(0)) < 0))
		throw new ValidationException( "L' IMPORTO deve essere maggiore di 0." );
		

	// controllo su campo VOCE DEL PIANO
	if ( elemento_voce.getCd_elemento_voce() == null || elemento_voce.getCd_elemento_voce().equals("") )
		throw new ValidationException( "Il campo VOCE DEL PIANO è obbligatorio." );

	//controllo scadenze
	if ( getFl_pgiro() != null && !getFl_pgiro().booleanValue() )
	{
		for (Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
			((Obbligazione_scadenzarioBulk) i.next()).validate();
				//controllo sulla validità delle nuove linee di attività
		validaNuoveLatt();
	}	


}
/**
 * Metodo con cui si verifica la validità di una nuova linea di attivita
 */
public void validateNuovaLineaAttivita( it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) throws ValidationException 
{
	// verifico che la linea di attività sia di tipo SPESE
	if ( ! it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_SPESE.equals(latt.getTi_gestione()))
	 	 throw new ValidationException( "Il nuovo GAE deve avere tipo gestione = SPESE");						 	 
	
	// verifico che la linea di attività non sia già stata inserita
	for ( Iterator i = getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
	{
		it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk la = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) i.next();
		if ( !la.equals( nuovaLatt ) && latt.getCentro_responsabilita().getCd_centro_responsabilita().equals( la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita()) &&
		 	 latt.getCd_linea_attivita().equals( la.getLinea_att().getCd_linea_attivita() ))
			 	 throw new ValidationException( "Il nuovo GAE e' già presente");
	}

	// verifico che la funzione della nuova linea di attività sia fra quelle dei capitoli selezionati
	boolean found = false;
	for ( Iterator i = getCapitoliDiSpesaCdsSelezionatiColl().iterator(); i.hasNext(); )
	{
		if ( ((it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk) i.next()).getCd_funzione().equals( latt.getFunzione().getCd_funzione()))
		{
			found = true;
			break;
		}
	}
	if ( !found)
	 	 throw new ValidationException( "La funzione del nuovo GAE non appartiene alla lista di funzioni selezionate");			

	// verifico che il cdr della linea di attività appartenga all'uo dell'obbligazione
//	if ( !getCd_unita_organizzativa().equals(latt.getCentro_responsabilita().getUnita_padre().getCd_unita_organizzativa()))
	if ( !latt.getCd_centro_responsabilita().startsWith(getCd_unita_organizzativa()))
	 	 throw new ValidationException( "Il Cdr del nuovo GAE non appartiene all'unita organizzativa dell'impegno");					
	//per CDS SAC verifico che la coppia CDR/funzione sia presente negli articoli selezionati dall'utente
	/* simona 8.5.2002 : non più necessario dopo la modifica della gestione del cds sac per obbligazione
	if ( getCds().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
	{
		boolean ok = false;
		for (Iterator i = getCapitoliDiSpesaCdsSelezionatiColl().iterator(); i.hasNext(); )
		{
			Voce_fBulk voce = (Voce_fBulk)i.next();
			if ( voce.getCd_centro_responsabilita().equals( latt.getCd_centro_responsabilita()) &&
				 voce.getCd_funzione().equals(latt.getCd_funzione()))
			{
				ok = true; 
				 break;
			}	 
		}
		if ( !ok)
	 	 throw new ValidationException( "Il nuovo GAE non ha cdr e funzione uguale a uno di quelli degli articoli di spesa selezionati");
	 	 
	}
	*/
	// verifico che la linea di attività non sia presente nel piano di gestione //
	/* simona 7.5.2002: spostato il controllo sulla Component perchè e' necessario accedere al db 
	V_pdg_obbligazione_speBulk vpos;
	for ( Iterator i = lineeAttivitaColl.iterator(); i.hasNext(); )
	{
		vpos = (V_pdg_obbligazione_speBulk) i.next();
		if ( vpos.getCd_centro_responsabilita().equals(latt.getCd_centro_responsabilita()) &&
			 vpos.getCd_linea_attivita().equals( latt.getCd_linea_attivita()))
			throw new ValidationException( "Il nuovo GAE e' presente nel piano di gestione");							
	}
	*/
}
/**
 * Metodo con cui si verifica la validità di una nuovo terzo
 */
public void validateTerzo( it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo ) throws ValidationException 
{
//	java.sql.Timestamp dataSistema = new java.sql.Timestamp(System.currentTimeMillis());
	java.sql.Timestamp dataSistema;
	try
	{
		dataSistema = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	}
	catch ( Exception e )
	{
		throw new ValidationException( "Impossibile recuperare la data di sistema!");
	}		


	if ( terzo.getDt_fine_rapporto() != null && terzo.getDt_fine_rapporto().before(dataSistema))
	 	 throw new ValidationException( "Il terzo non è più abilitato." );
	if ( !terzo.getTi_terzo().equals( terzo.CREDITORE) && !terzo.getTi_terzo().equals( terzo.ENTRAMBI) )
	 	 throw new ValidationException( "Il terzo deve essere di tipo CREDITORE o ENTRAMBI" );
	if ( isAssociataADocAmm() && !terzo.getCd_terzo().equals( getCd_terzo_iniziale()) && terzo.getAnagrafico() != null && !terzo.getAnagrafico().getTi_entita().equals( it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.DIVERSI ))
	 	 throw new ValidationException( "Impegno già associato a documenti amministrativi: Il terzo deve essere di tipo DIVERSI" );		
}
	/**
	 * @return
	 */
	public ContrattoBulk getContratto() {
		return contratto;
	}

	/**
	 * @param bulk
	 */
	public void setContratto(ContrattoBulk bulk) {
		contratto = bulk;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#getEsercizio_contratto()
	 */
 	public java.lang.Integer getEsercizio_contratto() {
		if (getContratto()==null)
		  return null; 		
		return getContratto().getEsercizio();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#getPg_contratto()
	 */
	public java.lang.Long getPg_contratto() {
		if (getContratto()==null)
		  return null;
		return getContratto().getPg_contratto();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#setEsercizio_contratto(java.lang.Integer)
	 */
	public void setEsercizio_contratto(java.lang.Integer esercizio) {
		getContratto().setEsercizio(esercizio);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#setPg_contratto(java.lang.Long)
	 */
	public void setPg_contratto(java.lang.Long pg_contratto) {
		getContratto().setPg_contratto(pg_contratto);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#getStato_contratto()
	 */
	public java.lang.String getStato_contratto() {
		if (getContratto()==null)
		  return null;		
		return getContratto().getStato();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneBase#setStato_contratto(java.lang.String)
	 */
	public void setStato_contratto(java.lang.String stato) {
		getContratto().setStato(stato);
	}
	/**
	 * @return
	 */
	public boolean isCheckDisponibilitaContrattoEseguito() {
		return checkDisponibilitaContrattoEseguito;
	}

	/**
	 * @param b
	 */
	public void setCheckDisponibilitaContrattoEseguito(boolean b) {
		checkDisponibilitaContrattoEseguito = b;
	}

	/**
	 * @return
	 */
	public boolean isCheckDisponibilitaIncaricoRepertorioEseguito() {
		return checkDisponibilitaIncaricoRepertorioEseguito;
	}

	/**
	 * @param b
	 */
	public void setCheckDisponibilitaIncaricoRepertorioEseguito(boolean b) {
		checkDisponibilitaIncaricoRepertorioEseguito = b;
	}

	/**
	 * @return
	 */
	public boolean isCheckDisponibilitaCdrGAEEseguito() {
		return checkDisponibilitaCdrGAEEseguito;
	}

	/**
	 * @param b
	 */
	public void setCheckDisponibilitaCdrGAEEseguito(boolean b) {
		checkDisponibilitaCdrGAEEseguito = b;
	}
	/**
	 * @return
	 */
	public it.cnr.jada.util.OrderedHashtable getAnniResidui() {
		return anniResidui;
	}

	/**
	 * @param hashtable
	 */
	public void setAnniResidui(it.cnr.jada.util.OrderedHashtable hashtable) {
		anniResidui = hashtable;
	}
	public void caricaAnniResidui(ActionContext actioncontext) {
		IntStream.iterate(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue(), i -> i - 1)
				.limit(SpringUtil.getBean(UtilService.class).getAnniResidui())
				.forEach(value -> anniResidui.put(new Integer(value), new Integer(value)));
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.bulk.OggettoBulk#clone()
	 */
	public Object clona(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		ObbligazioneBulk nuova = null;
		try {
			nuova = (ObbligazioneBulk)getClass().newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}

		// inizializzzaione di data_registrazione ??
		nuova.initializeForInsert(bp, context);
		
//		nuova.setEsercizio( getEsercizio() );
		nuova.setEsercizio_originale( getEsercizio_originale() );
		nuova.setEsercizio_competenza( getEsercizio_competenza() );
		nuova.setDs_obbligazione( getDs_obbligazione());
		nuova.setNote_obbligazione( getNote_obbligazione() );
		nuova.setIm_obbligazione( getIm_obbligazione() );
		nuova.setCd_riferimento_contratto( getCd_riferimento_contratto());
		nuova.setDt_scadenza_contratto( getDt_scadenza_contratto());
		nuova.setFl_calcolo_automatico( getFl_calcolo_automatico());
		nuova.setIm_costi_anticipati( new java.math.BigDecimal(0));
//		nuova.setCd_unita_organizzativa( getCd_unita_organizzativa());
	
		nuova.setUnita_organizzativa( getUnita_organizzativa());
		nuova.setCds( getCds());
		nuova.setTipo_obbligazione( getTipo_obbligazione());
		nuova.setElemento_voce( getElemento_voce() );
		nuova.setCreditore( getCreditore());
		nuova.setFondo_ricerca( getFondo_ricerca() );
		nuova.setCapitoliDiSpesaCdsColl( getCapitoliDiSpesaCdsColl());
		nuova.setCapitoliDiSpesaCdsSelezionatiColl(getCapitoliDiSpesaCdsSelezionatiColl());
		nuova.setCdrColl( getCdrColl());
		nuova.setCdrSelezionatiColl( getCdrSelezionatiColl());
		nuova.setLineeAttivitaColl( getLineeAttivitaColl());
		nuova.setLineeAttivitaSelezionateColl( getLineeAttivitaSelezionateColl());
		nuova.setListaVociSelezionabili(getListaVociSelezionabili());
		nuova.setNuoveLineeAttivitaColl(getNuoveLineeAttivitaColl());
		nuova.setCd_tipo_documento_cont( getCd_tipo_documento_cont());
		nuova.setFl_pgiro(getFl_pgiro());
		nuova.setCd_uo_origine( getCd_uo_origine());
		nuova.setCd_cds_origine( getCd_cds_origine());
//		nuova.setRiportato( "N");
		nuova.setFl_spese_costi_altrui( getFl_spese_costi_altrui());
		nuova.setFl_determina_allegata(Boolean.FALSE);

		nuova.setFl_gara_in_corso(getFl_gara_in_corso());
		nuova.setDs_gara_in_corso(getDs_gara_in_corso());
		// da verificare se vanno clonati
		//nuovo.setObbligazioniPluriennali(clonaObbligazioniPluriennali(nuovo,context));

		return nuova;
	}

	private BulkList<Obbligazione_pluriennaleBulk> clonaObbligazioniPluriennali(ObbligazioneBulk obbligazione,it.cnr.jada.action.ActionContext context){
		if ( this.getObbligazioniPluriennali()==null || this.getObbligazioniPluriennali().isEmpty())
			return this.getObbligazioniPluriennali();
		BulkList<Obbligazione_pluriennaleBulk> pluriennali= new BulkList<Obbligazione_pluriennaleBulk>();
		for ( Obbligazione_pluriennaleBulk p:this.getObbligazioniPluriennali()){
			Obbligazione_pluriennaleBulk n = p.clone(obbligazione,context);
			pluriennali.add( n);
		}
		return pluriennali;
	}


	@Override
	public void insertingUsing(Persister persister, UserContext userContext) {
		if (getFl_netto_sospeso() == null)
			setFl_netto_sospeso(Boolean.FALSE);
		super.insertingUsing(persister, userContext);
	}
	/**
	 * Metodo per ottenere le combinazioni CDR/Voce/Gae presenti nell'obbligazione e l'importo attribuito.
	 *
	 * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da analizzare
	 *
	 * @return PrimaryKeyHashtable con key = oggetti <code>Obbligazione_scad_voceBulk</code> da cui prelevare
	 * le combinazioni CDR/Voce/Gae e value = importo assegnato all'obbligazione
	 *
	 */
	public PrimaryKeyHashtable getRipartizioneCdrVoceLinea() {
		BigDecimal totaleScad = new BigDecimal(0);
		Obbligazione_scad_voceBulk osv;
		Obbligazione_scadenzarioBulk os;
		Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();
		PrimaryKeyHashtable prcImputazioneFinanziariaTable = new PrimaryKeyHashtable();	
		for ( Iterator s = this.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
		{
			os = (Obbligazione_scadenzarioBulk) s.next();
			for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
			{
				osv = (Obbligazione_scad_voceBulk) d.next();
				// totale per Cdr e per scadenza				
				key = new Obbligazione_scad_voceBulk(osv.getCd_cds(),
													 osv.getCd_centro_responsabilita(),
													 osv.getCd_linea_attivita(),
													 osv.getCd_voce(),
													 osv.getEsercizio(),
													 osv.getEsercizio_originale(),
													 osv.getPg_obbligazione(),
													 new Long(1),
													 osv.getTi_appartenenza(),
													 osv.getTi_gestione());

				totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );			
				if ( totaleScad == null || totaleScad.compareTo(new BigDecimal(0)) == 0)
					prcImputazioneFinanziariaTable.put( key, Utility.nvl(osv.getIm_voce()));
				else
				{
					totaleScad = totaleScad.add( Utility.nvl(osv.getIm_voce()) );
					prcImputazioneFinanziariaTable.put( key, totaleScad );
				}			
			}
		}
		return prcImputazioneFinanziariaTable;
	}
	/**
	 * Metodo per accorpare le scadenze dell'obbligazione
	 *
	 * @param BulkList <code>BulkList</code> la lista delle scadenze da accorpare
	 *
	 * @return la scadenza <code>Obbligazione_scadenzarioBulk</code> nuova creata in sostituzione
	 * delle altre accorpate
	 */
	public Obbligazione_scadenzarioBulk raggruppaScadenze(BulkList scadenzarioList) throws ValidationException {
		BigDecimal totaleScad = new BigDecimal(0);
		Obbligazione_scad_voceBulk osvNew=null, osvOld;
		Obbligazione_scadenzarioBulk osList, osNew=null, osOld;
		Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();
		
		for ( Iterator osListIterator = scadenzarioList.iterator(); osListIterator.hasNext(); ) {
			osList = (Obbligazione_scadenzarioBulk) osListIterator.next();

			if (!this.getObbligazione_scadenzarioColl().contains(osList))
				throw new ValidationException( "Errore! E' stata indicata una scadenza non presente nell'obbligazione." );
			
			osOld = (Obbligazione_scadenzarioBulk) this.getObbligazione_scadenzarioColl().get(this.getObbligazione_scadenzarioColl().indexOf(osList));
			
			if (osOld.getIm_associato_doc_amm().compareTo(Utility.ZERO)>0 ||
				osOld.getIm_associato_doc_contabile().compareTo(Utility.ZERO)>0)
				throw new ValidationException( "Errore! E' stata indicata una scadenza associata a documenti amministrativi." );
		}				

		for ( Iterator osListIterator = scadenzarioList.iterator(); osListIterator.hasNext(); ) {
			osList = (Obbligazione_scadenzarioBulk) osListIterator.next();
			osOld = (Obbligazione_scadenzarioBulk) this.getObbligazione_scadenzarioColl().get(this.getObbligazione_scadenzarioColl().indexOf(osList));

			if (osNew == null) 
				osNew = osOld;
			else
			{
				osNew.setIm_scadenza(osNew.getIm_scadenza().add(osOld.getIm_scadenza()));
				osNew.setToBeUpdated();
				for ( Iterator osvNewIterator = osNew.getObbligazione_scad_voceColl().iterator(); osvNewIterator.hasNext(); )
				{
					osvNew = (Obbligazione_scad_voceBulk) osvNewIterator.next();
					Boolean trovato = new Boolean(Boolean.FALSE);
					for ( Iterator osvOldIterator = osOld.getObbligazione_scad_voceColl().iterator(); osvOldIterator.hasNext()&&!trovato; )
					{
						osvOld = (Obbligazione_scad_voceBulk) osvOldIterator.next();
						if (osvNew.getEsercizio().equals(osvOld.getEsercizio()) &&
							osvNew.getCd_cds().equals(osvOld.getCd_cds()) &&
							osvNew.getCd_centro_responsabilita().equals(osvOld.getCd_centro_responsabilita()) &&
							osvNew.getCd_linea_attivita().equals(osvOld.getCd_linea_attivita()) &&
							osvNew.getTi_appartenenza().equals(osvOld.getTi_appartenenza()) &&
							osvNew.getTi_gestione().equals(osvOld.getTi_gestione()) &&
							osvNew.getCd_voce().equals(osvOld.getCd_voce()))
						{
							osvNew.setIm_voce(osvNew.getIm_voce().add(osvOld.getIm_voce()));
							osvNew.setToBeUpdated();
							trovato = Boolean.TRUE;
						}	
					}
				}
				osOld.setToBeDeleted();
				this.getObbligazione_scadenzarioColl().remove(osOld);
			}
		}
		return osNew;
	}
	/**
	 * Verifica se l'obbligazione è associata a documenti contabili.
	 * @return 				TRUE 	L'obbligazione è associata a documenti contabili
	 *						FALSE 	L'obbligazione non è associata a documenti contabili
	 */
	public boolean isAssociataADocCont() 
	{
		for ( Iterator i = obbligazione_scadenzarioColl.iterator(); i.hasNext(); )
			if ( ((Obbligazione_scadenzarioBulk) i.next()).getPg_mandato() != null )	
				return true;
		return false;	
	}
	public Incarichi_repertorioBulk getIncarico_repertorio() {
		return incarico_repertorio;
	}
	public void setIncarico_repertorio(Incarichi_repertorioBulk incarico_repertorio) {
		this.incarico_repertorio = incarico_repertorio;
	}
	public Integer getEsercizio_rep() {
		if (getIncarico_repertorio()==null)
			  return null; 		
		return getIncarico_repertorio().getEsercizio();
	}
	public void setEsercizio_rep(Integer esercizio_rep) {
		getIncarico_repertorio().setEsercizio(esercizio_rep);
	}
	public java.lang.Long getPg_repertorio() {
		if (getIncarico_repertorio()==null)
		  return null;
		return getIncarico_repertorio().getPg_repertorio();
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio) {
		getIncarico_repertorio().setPg_repertorio(pg_repertorio);
	}
	public boolean isROFlGaraInCorso(){
		return !isProvvisoria() ||
			  (getCrudStatus()!=OggettoBulk.UNDEFINED && getFl_gara_in_corso()!=null &&
		      !getFl_gara_in_corso().booleanValue() && 
			   ((getIncarico_repertorio()!=null && getIncarico_repertorio().getEsercizio()!=null &&
				 getIncarico_repertorio().getPg_repertorio()!=null) ||
				(getContratto()!=null && getContratto().getEsercizio()!=null &&
				 getContratto().getPg_contratto()!=null)));
	}
	public boolean isRODsGaraInCorso(){
		return isROFlGaraInCorso();
	}

	public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce_next() {
		return elemento_voce_next;
	}
	
	public void setElemento_voce_next(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next) {
		this.elemento_voce_next = elemento_voce_next;
	}
	
	@Override
	public Integer getEsercizio_ev_next() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next = this.getElemento_voce_next();
		if (elemento_voce_next == null)
			return null;
		return elemento_voce_next.getEsercizio();
	}

	@Override
	public void setEsercizio_ev_next(Integer esercizio_ev_next) {
		this.getElemento_voce_next().setEsercizio(esercizio_ev_next);
	}

	@Override
	public String getTi_appartenenza_ev_next() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next = this.getElemento_voce_next();
		if (elemento_voce_next == null)
			return null;
		return elemento_voce_next.getTi_appartenenza();
	}
	
	@Override
	public void setTi_appartenenza_ev_next(String ti_appartenenza_ev_next) {
		this.getElemento_voce_next().setTi_appartenenza(ti_appartenenza_ev_next);
	}
	
	@Override
	public String getTi_gestione_ev_next() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next = this.getElemento_voce_next();
		if (elemento_voce_next == null)
			return null;
		return elemento_voce_next.getTi_gestione();
	}
	
	@Override
	public void setTi_gestione_ev_next(String ti_gestione_ev_next) {
		this.getElemento_voce_next().setTi_gestione(ti_gestione_ev_next);
	}
	
	@Override
	public String getCd_elemento_voce_next() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_next = this.getElemento_voce_next();
		if (elemento_voce_next == null)
			return null;
		return elemento_voce_next.getCd_elemento_voce();
	}
	
	@Override
	public void setCd_elemento_voce_next(String cd_elemento_voce_next) {
		this.getElemento_voce_next().setCd_elemento_voce(cd_elemento_voce_next);
	}

	public boolean isROElemento_voce_next() {
		return elemento_voce_next == null || elemento_voce_next.getCrudStatus() == NORMAL;
	}
	
	public boolean isEnableVoceNext() {
		return enableVoceNext;
	}
	public void setEnableVoceNext(boolean enableVoceNext) {
		this.enableVoceNext = enableVoceNext;
	}
	public List<Elemento_voceBulk> getListaVociSelezionabili() {
		return listaVociSelezionabili;
	}
	public void setListaVociSelezionabili(List<Elemento_voceBulk> listaVociSelezionabili) {
		this.listaVociSelezionabili = listaVociSelezionabili;
	}
	@Override
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		((AllegatoObbligazioneBulk)allegato).setEsercizioDiAppartenenza(this.getEsercizio());
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;		
	}
	@Override
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	@Override
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	@Override
	public void setArchivioAllegati(BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public BigDecimal getImportoNonPagato() {
		return this.getObbligazione_scadenzarioColl().stream().map(e->e.getImportoNonPagato()).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO);
	}

	public WorkpackageBulk getGaeDestinazioneFinale() {
		return gaeDestinazioneFinale;
	}

	public void setGaeDestinazioneFinale(WorkpackageBulk gaeDestinazioneFinale) {
		this.gaeDestinazioneFinale = gaeDestinazioneFinale;
	}
	public boolean isROFlagPluriennale(){

		return Boolean.FALSE;

	}

	public BulkList<Obbligazione_pluriennaleBulk> getObbligazioniPluriennali() {
		return obbligazioniPluriennali;
	}

	public void setObbligazioniPluriennali(BulkList<Obbligazione_pluriennaleBulk> obbligazioniPluriennali) {
		this.obbligazioniPluriennali = obbligazioniPluriennali;
	}

	public int addToObbligazioniPluriennali(Obbligazione_pluriennaleBulk dett){
		dett.setObbligazione(this);
		getObbligazioniPluriennali().add(dett);
		return getObbligazioniPluriennali().size()-1;
	}

	public Obbligazione_pluriennaleBulk removeFromObbligazioniPluriennali(int index) {
		Obbligazione_pluriennaleBulk dett = (Obbligazione_pluriennaleBulk)getObbligazioniPluriennali().remove(index);
		return dett;
	}

	public boolean isProvvisoria() {
		return STATO_OBB_PROVVISORIO.equals(this.getStato_obbligazione());
	}

	public boolean isDefinitiva() {
		return STATO_OBB_DEFINITIVO.equals(this.getStato_obbligazione());
	}

	public boolean isStornata() {
		return STATO_OBB_STORNATO.equals(this.getStato_obbligazione());
	}

	public AllegatoObbligazioneBulk getAllegatoDetermina(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			AllegatoObbligazioneBulk allegato = (AllegatoObbligazioneBulk)i.next();
			if (allegato.isTipoDetermina())
				return allegato;
		}
		return null;
	}
}
