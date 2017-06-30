/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.util.Dictionary;
import java.util.Iterator;

import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.ModalitaPagamentoType;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.TipoDocumentoType;
public class DocumentoEleTestataBulk extends DocumentoEleTestataBase implements AllegatoParentBulk{
	public static final String STATO_DOCUMENTO_TUTTI = "TUTTI";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	
	public static final String STATO_CONSEGNA_ESITO_CONSEGNATO_SDI = "CON";
	public static final String STATO_CONSEGNA_ESITO_SCARTATO_SDI = "SCA";
	public static final java.util.Dictionary<String, String> statoNotificaEsitoKeys = new OrderedHashtable();	
	public static final java.util.Dictionary<String, String> tiDecorrenzaTerminiKeys = new OrderedHashtable();	
	public static final java.util.Dictionary<String, String> tiStatoDocumentoKeys = new OrderedHashtable();	
	public static final java.util.Dictionary<String, String> tiStatoDocumentoSelectKeys = new OrderedHashtable();	
	public static final java.util.Dictionary<String, String> tiTipoDocumentoKeys = new OrderedHashtable();
	public static final java.util.Dictionary<String, String> tiModalitaPagamentoKeys = new OrderedHashtable();
	private static final String RICEVUTA_DECORRENZA = "RICEVUTA DECORRENZA TERMINI";
	private String ricevutaDecorrenza;
	
	static {
		statoNotificaEsitoKeys.put(STATO_CONSEGNA_ESITO_CONSEGNATO_SDI,"NOTIFICA CONSEGNATA A SDI");
		statoNotificaEsitoKeys.put(STATO_CONSEGNA_ESITO_SCARTATO_SDI,"NOTIFICA RIFIUTATA DA SDI");

		tiDecorrenzaTerminiKeys.put("S","Si");
		tiDecorrenzaTerminiKeys.put("N","No");

		tiStatoDocumentoKeys.put(StatoDocumentoEleEnum.AGGIORNATO.name(),StatoDocumentoEleEnum.AGGIORNATO.name());
		tiStatoDocumentoKeys.put(StatoDocumentoEleEnum.COMPLETO.name(),StatoDocumentoEleEnum.COMPLETO.name());
		tiStatoDocumentoKeys.put(StatoDocumentoEleEnum.REGISTRATO.name(),StatoDocumentoEleEnum.REGISTRATO.name());
		tiStatoDocumentoKeys.put(StatoDocumentoEleEnum.RIFIUTATO.name(),StatoDocumentoEleEnum.RIFIUTATO.name());
		tiStatoDocumentoKeys.put(STATO_DOCUMENTO_TUTTI,STATO_DOCUMENTO_TUTTI);

		tiStatoDocumentoSelectKeys.put(StatoDocumentoEleEnum.AGGIORNATO.name(),StatoDocumentoEleEnum.AGGIORNATO.name());
		tiStatoDocumentoSelectKeys.put(StatoDocumentoEleEnum.COMPLETO.name(),StatoDocumentoEleEnum.COMPLETO.name());
		tiStatoDocumentoSelectKeys.put(StatoDocumentoEleEnum.REGISTRATO.name(),StatoDocumentoEleEnum.REGISTRATO.name());
		tiStatoDocumentoSelectKeys.put(StatoDocumentoEleEnum.RIFIUTATO.name(),StatoDocumentoEleEnum.RIFIUTATO.name());
		
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_01.value(),"Fattura");
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_02.value(),"Acconto / anticipo su fattura");
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_03.value(),"Acconto / anticipo su parcella");
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_04.value(),"Nota di credito");
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_05.value(),"Nota di debito");
		tiTipoDocumentoKeys.put(TipoDocumentoType.TD_06.value(),"Parcella");
		
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_01.value(),"contanti");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_02.value(),"assegno");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_03.value(),"assegno circolare");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_04.value(),"contanti presso Tesoreria");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_05.value(),"bonifico");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_06.value(),"vaglia cambiario");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_07.value(),"bollettino bancario");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_08.value(),"carta di pagamento");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_09.value(),"RID");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_10.value(),"RID utenze");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_11.value(),"RID veloce");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_12.value(),"RIBA");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_13.value(),"MAV");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_14.value(),"quietanza erario");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_15.value(),"giroconto su conti di contabilità  speciale");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_16.value(),"domiciliazione bancaria");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_17.value(),"domiciliazione postale");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_18.value(),"bollettino di c/c postale");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_19.value(),"SEPA Direct Debit");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_20.value(),"SEPA Direct Debit CORE");
		tiModalitaPagamentoKeys.put(ModalitaPagamentoType.MP_21.value(),"SEPA Direct Debit B2B");		
	}
	/**
	 * [DOCUMENTO_ELE_TRASMISSIONE Documento elettronico di trasmissione]
	 **/
	private DocumentoEleTrasmissioneBulk documentoEleTrasmissione =  new DocumentoEleTrasmissioneBulk();
	private Unita_organizzativaBulk unitaCompetenza =  new Unita_organizzativaBulk();
	
	/**
	 * [MODALITA_PAGAMENTO Descrive le modalitÃ  di pagamento previste per un dato terzo.]
	 **/
	private Modalita_pagamentoBulk modalitaPagamento =  new Modalita_pagamentoBulk();
	
	private BulkList<DocumentoEleLineaBulk> docEleLineaColl = new BulkList<DocumentoEleLineaBulk>();
	private BulkList<DocumentoEleIvaBulk> docEleIVAColl = new BulkList<DocumentoEleIvaBulk>();
	private BulkList<DocumentoEleAllegatiBulk> docEleAllegatiColl = new BulkList<DocumentoEleAllegatiBulk>();
	private BulkList<DocumentoEleTributiBulk> docEleTributiColl = new BulkList<DocumentoEleTributiBulk>();
	private BulkList<DocumentoEleScontoMaggBulk> docEleScontoMaggColl = new BulkList<DocumentoEleScontoMaggBulk>();
	private BulkList<DocumentoEleAcquistoBulk> docEleAcquistoColl = new BulkList<DocumentoEleAcquistoBulk>();
	private BulkList<DocumentoEleDdtBulk> docEleDdtColl = new BulkList<DocumentoEleDdtBulk>();
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TESTATA
	 **/
	public DocumentoEleTestataBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TESTATA
	 **/
	public DocumentoEleTestataBulk(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo) {
		super(idPaese, idCodice, identificativoSdi, progressivo);
		setDocumentoEleTrasmissione( new DocumentoEleTrasmissioneBulk(idPaese,idCodice,identificativoSdi) );
	}
	
	public Dictionary getStatoNotificaEsitoKeys() {
		return statoNotificaEsitoKeys;
	}

	public Dictionary getDecorrenzaTerminiKeys() {
		return tiDecorrenzaTerminiKeys;
	}

	public void setDocEleLineaColl(BulkList<DocumentoEleLineaBulk> docEleLineaColl) {
		this.docEleLineaColl = docEleLineaColl;
	}
	public void setDocEleIVAColl(BulkList<DocumentoEleIvaBulk> docEleIVAColl) {
		this.docEleIVAColl = docEleIVAColl;
	}
	public void setDocEleAllegatiColl(
			BulkList<DocumentoEleAllegatiBulk> docEleAllegatiColl) {
		this.docEleAllegatiColl = docEleAllegatiColl;
	}
	public void setDocEleTributiColl(
			BulkList<DocumentoEleTributiBulk> docEleTributiColl) {
		this.docEleTributiColl = docEleTributiColl;
	}
	public void setDocEleScontoMaggColl(
			BulkList<DocumentoEleScontoMaggBulk> docEleScontoMaggColl) {
		this.docEleScontoMaggColl = docEleScontoMaggColl;
	}
	public void setDocEleAcquistoColl(
			BulkList<DocumentoEleAcquistoBulk> docEleAcquistoColl) {
		this.docEleAcquistoColl = docEleAcquistoColl;
	}
	public void setDocEleDdtColl(BulkList<DocumentoEleDdtBulk> docEleDdtColl) {
		this.docEleDdtColl = docEleDdtColl;
	}
	@Override
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		return new it.cnr.jada.bulk.BulkCollection[] { 
				docEleLineaColl,docEleIVAColl,docEleAllegatiColl,
				docEleTributiColl,docEleScontoMaggColl,docEleAcquistoColl,docEleDdtColl,archivioAllegati
		};
	}
	
	public int addToDocEleLineaColl( DocumentoEleLineaBulk doc ) {
		docEleLineaColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleLineaColl.size()-1;
	}

	public int addToDocEleIVAColl( DocumentoEleIvaBulk doc ) {
		docEleIVAColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleIVAColl.size()-1;
	}

	public int addToDocEleAllegatiColl( DocumentoEleAllegatiBulk doc ) {
		docEleAllegatiColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleAllegatiColl.size()-1;
	}

	public int addToDocEleTributiColl( DocumentoEleTributiBulk doc ) {
		docEleTributiColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleTributiColl.size()-1;
	}
	
	public int addToDocEleScontoMaggColl( DocumentoEleScontoMaggBulk doc ) {
		docEleScontoMaggColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleScontoMaggColl.size()-1;
	}

	public int addToDocEleAcquistoColl( DocumentoEleAcquistoBulk doc ) {
		docEleAcquistoColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleAcquistoColl.size()-1;
	}
	public int addToDocEleDdtColl( DocumentoEleDdtBulk doc ) {
		docEleDdtColl.add(doc);
		doc.setDocumentoEleTestata(this);		
		return docEleDdtColl.size()-1;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Documento elettronico di trasmissione]
	 **/
	public DocumentoEleTrasmissioneBulk getDocumentoEleTrasmissione() {
		return documentoEleTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Documento elettronico di trasmissione]
	 **/
	public void setDocumentoEleTrasmissione(DocumentoEleTrasmissioneBulk documentoEleTrasmissione)  {
		this.documentoEleTrasmissione=documentoEleTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrive le modalitÃ  di pagamento previste per un dato terzo.]
	 **/
	public Modalita_pagamentoBulk getModalitaPagamento() {
		return modalitaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrive le modalitÃ  di pagamento previste per un dato terzo.]
	 **/
	public void setModalitaPagamento(Modalita_pagamentoBulk modalitaPagamento)  {
		this.modalitaPagamento=modalitaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idPaese]
	 **/
	public java.lang.String getIdPaese() {
		DocumentoEleTrasmissioneBulk documentoEleTrasmissione = this.getDocumentoEleTrasmissione();
		if (documentoEleTrasmissione == null)
			return null;
		return getDocumentoEleTrasmissione().getIdPaese();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idPaese]
	 **/
	public void setIdPaese(java.lang.String idPaese)  {
		this.getDocumentoEleTrasmissione().setIdPaese(idPaese);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idCodice]
	 **/
	public java.lang.String getIdCodice() {
		DocumentoEleTrasmissioneBulk documentoEleTrasmissione = this.getDocumentoEleTrasmissione();
		if (documentoEleTrasmissione == null)
			return null;
		return getDocumentoEleTrasmissione().getIdCodice();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idCodice]
	 **/
	public void setIdCodice(java.lang.String idCodice)  {
		this.getDocumentoEleTrasmissione().setIdCodice(idCodice);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoSdi]
	 **/
	public java.lang.Long getIdentificativoSdi() {
		DocumentoEleTrasmissioneBulk documentoEleTrasmissione = this.getDocumentoEleTrasmissione();
		if (documentoEleTrasmissione == null)
			return null;
		return getDocumentoEleTrasmissione().getIdentificativoSdi();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoSdi]
	 **/
	public void setIdentificativoSdi(java.lang.Long identificativoSdi)  {
		this.getDocumentoEleTrasmissione().setIdentificativoSdi(identificativoSdi);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pagamentoCdTerzo]
	 **/
	public java.lang.Integer getPagamentoCdTerzo() {
		Modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
		if (modalitaPagamento == null)
			return null;
		return getModalitaPagamento().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pagamentoCdTerzo]
	 **/
	public void setPagamentoCdTerzo(java.lang.Integer pagamentoCdTerzo)  {
		this.getModalitaPagamento().setCd_terzo(pagamentoCdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pagamentoCdModalitaPag]
	 **/
	public java.lang.String getPagamentoCdModalitaPag() {
		Modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
		if (modalitaPagamento == null)
			return null;
		return getModalitaPagamento().getCd_modalita_pag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pagamentoCdModalitaPag]
	 **/
	public void setPagamentoCdModalitaPag(java.lang.String pagamentoCdModalitaPag)  {
		this.getModalitaPagamento().setCd_modalita_pag(pagamentoCdModalitaPag);
	}
	public BulkList<DocumentoEleLineaBulk> getDocEleLineaColl() {
		return docEleLineaColl;
	}
	public BulkList<DocumentoEleIvaBulk> getDocEleIVAColl() {
		return docEleIVAColl;
	}
	public BulkList<DocumentoEleAllegatiBulk> getDocEleAllegatiColl() {
		return docEleAllegatiColl;
	}
	public BulkList<DocumentoEleTributiBulk> getDocEleTributiColl() {
		return docEleTributiColl;
	}
	public BulkList<DocumentoEleScontoMaggBulk> getDocEleScontoMaggColl() {
		return docEleScontoMaggColl;
	}
	public BulkList<DocumentoEleAcquistoBulk> getDocEleAcquistoColl() {
		return docEleAcquistoColl;
	}
	public BulkList<DocumentoEleDdtBulk> getDocEleDdtColl() {
		return docEleDdtColl;
	}
	
	public StatoDocumentoEleEnum getStatoDocumentoEle() {
		try {
			if (getStatoDocumento() != null)
				return StatoDocumentoEleEnum.valueOf(getStatoDocumento());
			return StatoDocumentoEleEnum.INIZIALE;
		} catch (IllegalArgumentException _ex) {
			return null;
		}
	}
	public boolean isValorizzatoTerzo() {
		if (getDocumentoEleTrasmissione() != null && 
				getDocumentoEleTrasmissione().getPrestatore() != null &&
				getDocumentoEleTrasmissione().getPrestatore().getCrudStatus() == OggettoBulk.NORMAL)
			return false;
		return true;
	}
	
	public boolean isCompilabile() {
		return getStatoDocumentoEle().equals(StatoDocumentoEleEnum.COMPLETO);
	}

	public boolean isRegistrata() {
		return getStatoDocumentoEle().equals(StatoDocumentoEleEnum.REGISTRATO);
	}
	
	public boolean isRifiutata() {
		return getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO);
	}	
	public boolean isRifiutabile() {
		return getStatoDocumentoEle().equals(StatoDocumentoEleEnum.AGGIORNATO) || 
				getStatoDocumentoEle().equals(StatoDocumentoEleEnum.COMPLETO);
	}
	public boolean isEditabile() {
		return (getStatoDocumentoEle().equals(StatoDocumentoEleEnum.AGGIORNATO) || 
				getStatoDocumentoEle().equals(StatoDocumentoEleEnum.COMPLETO)) && 
				!isIrregistrabile();
	}	

	public Fattura_passiva_rigaBulk getInstanceRiga() {
		if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO))
			return new Nota_di_credito_rigaBulk();
		else if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO))
			return new Nota_di_debito_rigaBulk();
		return new Fattura_passiva_rigaIBulk();
	}

	public Fattura_passivaBulk getInstanceFattura() {
		if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO))
			return new Nota_di_creditoBulk();
		else if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO))
			return new Nota_di_debitoBulk();
		return new Fattura_passiva_IBulk();
	}
	
	public String getBusinessProcessFattura() {
		if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO))
			return "CRUDNotaDiCreditoBP";
		else if (getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO))
			return "CRUDNotaDiDebitoBP";
		return "CRUDFatturaPassivaBP";
	}
	
	public String getTipoDocumentoSIGLA() {
		if (getTipoDocumento().equalsIgnoreCase(TipoDocumentoType.TD_04.value()))
			return Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO;
		else if (getTipoDocumento().equalsIgnoreCase(TipoDocumentoType.TD_05.value()))
			return Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO;
		return Fattura_passivaBulk.TIPO_FATTURA_PASSIVA;
	}
	
	public boolean isUoNonEnte() {
		if (getDocumentoEleTrasmissione() != null && getDocumentoEleTrasmissione().getUnitaOrganizzativa() != null &&
				!getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return false;
		return true;
	}

	public String getNomeFileFirmato() {
		return getDocumentoEleTrasmissione().getNomeFile();
	}	
	
	public String getNomeFile(String prefix) {
		if (getIdPaese() == null)
			return "";
		String nomeFile = getDocumentoEleTrasmissione().getNomeFile();
		return nomeFile.substring(0, nomeFile.indexOf(".")).
				concat("_").concat(prefix).concat("_").
				concat(Utility.lpad(getPg_ver_rec(), 3, '0')).concat(".xml");
	}	

	public java.math.BigDecimal calcolaImLordoPercipiente(DocumentoEleTestataBulk eleTestata) {

		java.math.BigDecimal imponiIva = new java.math.BigDecimal(0);
		java.math.BigDecimal impoCassa = new java.math.BigDecimal(0);
		
		if(eleTestata.getDocEleIVAColl()!=null)
		{	
			for (Iterator i = eleTestata.getDocEleIVAColl().iterator(); i.hasNext();)
			{	
				DocumentoEleIvaBulk iva = ((DocumentoEleIvaBulk)i.next());
				imponiIva = imponiIva.add(Utility.nvl(iva.getImponibileImporto()));
			}
		}
		
		if(eleTestata.getDocEleTributiColl()!=null)
		{	
			for (Iterator i = eleTestata.getDocEleTributiColl().iterator(); i.hasNext();)
			{	
				DocumentoEleTributiBulk tributi = ((DocumentoEleTributiBulk)i.next());
				if (tributi.getTipoRiga()!=null && tributi.getTipoRiga().equals(DocumentoEleTributiBulk.TIPO_RIGA_C))
				   impoCassa = impoCassa.add(Utility.nvl(tributi.getImporto()));
			}
		}
		
		return imponiIva.subtract(impoCassa); 
	}

/*	old calcolo
	public java.math.BigDecimal calcolaImLordoPercipiente(DocumentoEleTestataBulk eleTestata) {

		java.math.BigDecimal imponiCassa = new java.math.BigDecimal(0);
		java.math.BigDecimal imponiCassaINPS = new java.math.BigDecimal(0);
		
		if(eleTestata.getDocEleTributiColl()!=null)
		{	
		
			for (Iterator i = eleTestata.getDocEleTributiColl().iterator(); i.hasNext();)
			{	
				DocumentoEleTributiBulk tributi = ((DocumentoEleTributiBulk)i.next());
				if (tributi.getTipoRiga()!=null && tributi.getTipoRiga().equals(DocumentoEleTributiBulk.TIPO_RIGA_C))
				{	
					imponiCassa = Utility.nvl(tributi.getImponibileCassa());
					   if (	tributi.getTipoTributo()!=null && tributi.getTipoTributo().equalsIgnoreCase(TipoCassaType.TC_22.value()))
						
						   imponiCassaINPS = Utility.nvl(tributi.getImponibileCassa());
				}
				
			}
			if (imponiCassaINPS.compareTo(new java.math.BigDecimal(0)) != 0)
				return imponiCassaINPS;
			if (imponiCassa.compareTo(new java.math.BigDecimal(0)) != 0)
				return imponiCassa;
		}
		
		java.math.BigDecimal impoIva = new java.math.BigDecimal(0);
		
		if(eleTestata.getDocEleIVAColl()!=null)
		{	
		
			for (Iterator i = eleTestata.getDocEleIVAColl().iterator(); i.hasNext();)
			{	
				DocumentoEleIvaBulk iva = ((DocumentoEleIvaBulk)i.next());
				impoIva = Utility.nvl(iva.getImponibileImporto());
			}
				
			if (impoIva.compareTo(new java.math.BigDecimal(0)) != 0)
				return impoIva; 

		}
		
		return new java.math.BigDecimal(0);
	}
*/	
/*  non pi� utilizzate
	public java.math.BigDecimal calcolaImQuotaEsenteNonImpo(DocumentoEleTestataBulk eleTestata) {

		java.math.BigDecimal quotaEsenteNonImpo = new java.math.BigDecimal(0);
		
		if(eleTestata.getDocEleIVAColl()!=null)
		{	
		
			for (Iterator i = eleTestata.getDocEleIVAColl().iterator(); i.hasNext();)
			{	
				DocumentoEleIvaBulk iva = ((DocumentoEleIvaBulk)i.next());
				if(iva.getNatura()!= null && 
				   (iva.getNatura().compareToIgnoreCase("N1")==0
				    ||
				    iva.getNatura().compareToIgnoreCase("N3")==0
				    )
				  )
					quotaEsenteNonImpo = quotaEsenteNonImpo.add(Utility.nvl(iva.getImponibileImporto()));
			}
		}
		return quotaEsenteNonImpo;
	}
	public java.math.BigDecimal calcolaImQuotaEsente(DocumentoEleTestataBulk eleTestata) {

		java.math.BigDecimal quotaEsente = new java.math.BigDecimal(0);
		
		if(eleTestata.getDocEleIVAColl()!=null)
		{	
		
			for (Iterator i = eleTestata.getDocEleIVAColl().iterator(); i.hasNext();)
			{	
				DocumentoEleIvaBulk iva = ((DocumentoEleIvaBulk)i.next());
				if(iva.getNatura()!= null && 
				   iva.getNatura().compareToIgnoreCase("N1")!=0 &&
				   iva.getNatura().compareToIgnoreCase("N3")!=0)
					quotaEsente = quotaEsente.add(Utility.nvl(iva.getImponibileImporto()));
			}
		}
		return quotaEsente;
	}
*/
	public Boolean isRicevutaDecorrenzaTermini(){
		if (getFlDecorrenzaTermini() != null && getFlDecorrenzaTermini().equals("S")){
			return true;
		}
		return false;
	}
	public String getRicevutaDecorrenza() {
		return RICEVUTA_DECORRENZA;
	}
	public void setRicevutaDecorrenza(String ricevutaDecorrenza) {
//		this.ricevutaDecorrenza = ricevutaDecorrenza;
	}
	public String getStatoNotificaEsitoVisual() {
		return statoNotificaEsitoKeys.get(getStatoNotificaEsito());
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
	public Boolean isIrregistrabile(){
		if (getFlIrregistrabile() != null && getFlIrregistrabile().equals("S")){
			return true;
		}
		return false;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Unit� organizzativa]
	 **/
	public java.lang.String getCdUnitaCompetenza() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaCompetenza();
		if (unitaOrganizzativa == null)
			return null;
		return unitaOrganizzativa.getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Unit� organizzativa]
	 **/
	public void setCdUnitaCompetenza(java.lang.String cdUnitaOrganizzativa)  {
		this.getUnitaCompetenza().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}
	
	public Unita_organizzativaBulk getUnitaCompetenza() {
		return unitaCompetenza;
	}
	public void setUnitaCompetenza(Unita_organizzativaBulk unitaCompetenza) {
		this.unitaCompetenza = unitaCompetenza;
	}	
}