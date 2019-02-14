/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;

import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.sql.CHARToBooleanConverter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Iterator;

public class Incarichi_comunicati_fpBulk extends Incarichi_comunicati_fpBase {

	final public static String TIPO_RECORD_INVIATO_NEW = "NI";
	final public static String TIPO_RECORD_INVIATO_UPD = "UI";
	final public static String TIPO_RECORD_INVIATO_DEL = "DI";
	final public static String TIPO_RECORD_RICEVUTO_NEW = "NR";
	final public static String TIPO_RECORD_RICEVUTO_UPD = "UR";
	final public static String TIPO_RECORD_RICEVUTO_DEL = "DR";
	final public static String TIPO_RECORD_AGGIORNATO = "AGG";

	final public static Long PG_RECORD_PRINCIPALE = Long.valueOf("1");

	private Incarichi_repertorioBulk incarichi_repertorio;
	
	private BulkList incarichi_comunicati_fp_detColl = new BulkList();

	public Incarichi_comunicati_fpBulk() {
		super();
	}
	
	public Incarichi_comunicati_fpBulk(java.lang.Integer esercizio_repertorio, java.lang.Long pg_repertorio, java.lang.String tipo_record, java.lang.Long pg_record) {
		super(esercizio_repertorio, pg_repertorio, tipo_record, pg_record);
		setIncarichi_repertorio(new Incarichi_repertorioBulk(esercizio_repertorio, pg_repertorio));
	}

	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}

	public void setIncarichi_repertorio(Incarichi_repertorioBulk incarichiRepertorio) {
		incarichi_repertorio = incarichiRepertorio;
	}
	
	@Override
	public void setEsercizio_repertorio(Integer esercizio_repertorio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setEsercizio(esercizio_repertorio);
	}
	
	@Override
	public Integer getEsercizio_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio();
	}
	
	@Override
	public void setPg_repertorio(Long pg_repertorio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
	}
	
	@Override
	public Long getPg_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_repertorio();
	}
	
	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, Comunicazione.Consulenti.NuovoIncarico nuovoIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			int esercizio_repertorio = new Integer(nuovoIncarico.getDescrizioneIncarico().substring(1,5)); 
			Long pg_repertorio = new Long(nuovoIncarico.getDescrizioneIncarico().substring(6,nuovoIncarico.getDescrizioneIncarico().indexOf(")"))); 
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk(esercizio_repertorio,pg_repertorio, Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW, new Long(1));
			incaricoFP.setAnno_riferimento(nuovoIncarico.getAnnoRiferimento());
			incaricoFP.setSemestre_riferimento(nuovoIncarico.getSemestreRiferimento());
			incaricoFP.setCodice_ente(nuovoIncarico.getCodiceEnte());
			incaricoFP.setCognome(nuovoIncarico.getIncaricatoPersona().getCognome());
			incaricoFP.setNome(nuovoIncarico.getIncaricatoPersona().getNome());
			incaricoFP.setData_nascita(new Timestamp(formatter.parse(nuovoIncarico.getIncaricatoPersona().getDataNascita()).getTime()));
			incaricoFP.setTi_sesso(nuovoIncarico.getIncaricatoPersona().getSesso());
			incaricoFP.setFl_estero(nuovoIncarico.getIncaricatoPersona().isEstero());
			incaricoFP.setCodice_fiscale_partita_iva(nuovoIncarico.getIncaricatoPersona().getCodiceFiscalePartitaIva());
			incaricoFP.setDescrizione_incarico(nuovoIncarico.getDescrizioneIncarico());
			incaricoFP.setDt_inizio(new Timestamp(nuovoIncarico.getDataInizio().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setDt_fine(new Timestamp(nuovoIncarico.getDataFine().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setImporto_previsto(nuovoIncarico.getImportoPrevisto());
			incaricoFP.setFl_riferimento_regolamento(null);
			incaricoFP.setFl_saldo(null);
			incaricoFP.setAttivita_economica(nuovoIncarico.getAttivitaEconomica());
			incaricoFP.setTipo_rapporto(nuovoIncarico.getTipoRapporto());
			incaricoFP.setModalita_acquisizione(nuovoIncarico.getModalitaAcquisizione());
			incaricoFP.setTipologia_consulente(nuovoIncarico.getIncaricatoPersona().getTipologiaConsulente());
			
			for (Iterator<Comunicazione.Consulenti.NuovoIncarico.NuovoPagamento> iterator = nuovoIncarico.getNuovoPagamento().iterator(); iterator.hasNext();) {
				Comunicazione.Consulenti.NuovoIncarico.NuovoPagamento nuovoPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, nuovoPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}

			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}
	
	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			int esercizio_repertorio = new Integer(nuovoIncarico.getDescrizioneIncarico().substring(1,5)); 
			Long pg_repertorio = new Long(nuovoIncarico.getDescrizioneIncarico().substring(6,nuovoIncarico.getDescrizioneIncarico().indexOf(")"))); 
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk(esercizio_repertorio,pg_repertorio, Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW, new Long(1));
			incaricoFP.setId_incarico(nuovoIncarico.getIdIncarico());
			incaricoFP.setAnno_riferimento(nuovoIncarico.getAnnoRiferimento());
			incaricoFP.setSemestre_riferimento(nuovoIncarico.getSemestreRiferimento());
			incaricoFP.setCodice_ente(nuovoIncarico.getCodiceEnte());
			incaricoFP.setCognome(nuovoIncarico.getIncaricatoPersona().getCognome());
			incaricoFP.setNome(nuovoIncarico.getIncaricatoPersona().getNome());
			if (nuovoIncarico.getIncaricatoPersona().getDataNascita()!=null)
				incaricoFP.setData_nascita(new Timestamp(formatter.parse(nuovoIncarico.getIncaricatoPersona().getDataNascita()).getTime()));
			if (nuovoIncarico.getIncaricatoPersona().getSesso()!=null)
				incaricoFP.setTi_sesso(nuovoIncarico.getIncaricatoPersona().getSesso());
			incaricoFP.setFl_estero(nuovoIncarico.getIncaricatoPersona().isEstero());
			incaricoFP.setCodice_fiscale_partita_iva(nuovoIncarico.getIncaricatoPersona().getCodiceFiscalePartitaIva());
			incaricoFP.setDescrizione_incarico(nuovoIncarico.getDescrizioneIncarico());
			incaricoFP.setDt_inizio(new Timestamp(nuovoIncarico.getDataInizio().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setDt_fine(new Timestamp(nuovoIncarico.getDataFine().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setImporto_previsto(nuovoIncarico.getImportoPrevisto());
			incaricoFP.setFl_riferimento_regolamento(null);
			incaricoFP.setFl_saldo(null);
			incaricoFP.setAttivita_economica(nuovoIncarico.getAttivitaEconomica());
			incaricoFP.setTipo_rapporto(nuovoIncarico.getTipoRapporto());
			incaricoFP.setModalita_acquisizione(nuovoIncarico.getModalitaAcquisizione());
			incaricoFP.setTipologia_consulente(null);
			
			incaricoFP.setEsito_incarico(nuovoIncarico.getEsito()!=null?nuovoIncarico.getEsito().toString():null);
			incaricoFP.setErr_anno_riferimento(nuovoIncarico.getErrAnnoRiferimento());
			incaricoFP.setErr_semestre_riferimento(nuovoIncarico.getErrSemestreRiferimento());
			incaricoFP.setErr_codice_ente(nuovoIncarico.getErrCodiceEnte());
			incaricoFP.setErr_descrizione_incarico(nuovoIncarico.getErrDescrizioneIncarico());
			incaricoFP.setErr_attivita_economica(nuovoIncarico.getErrAttivitaEconomica());
			incaricoFP.setErr_dt_inizio(nuovoIncarico.getErrDataInizio());
			incaricoFP.setErr_dt_fine(nuovoIncarico.getErrDataFine());
			incaricoFP.setErr_importo_previsto(nuovoIncarico.getErrImportoPrevisto());
			incaricoFP.setErr_saldo(nuovoIncarico.getErrSaldo());
			incaricoFP.setErr_modalita_acquisizione(nuovoIncarico.getErrModalitaAcquisizione());
			incaricoFP.setErr_tipo_rapporto(nuovoIncarico.getErrTipoRapporto());
			incaricoFP.setErr_variazioni_incarico(nuovoIncarico.getErrVariazioniIncarico());
			incaricoFP.setEsito_incarico_persona(nuovoIncarico.getIncaricatoPersona().getEsito()!=null?nuovoIncarico.getIncaricatoPersona().getEsito().toString():null);
			incaricoFP.setErr_cognome(nuovoIncarico.getIncaricatoPersona().getErrCognome());
			incaricoFP.setErr_nome(nuovoIncarico.getIncaricatoPersona().getErrNome());
			incaricoFP.setErr_data_nascita(nuovoIncarico.getIncaricatoPersona().getErrDataNascita());
			incaricoFP.setErr_sesso(nuovoIncarico.getIncaricatoPersona().getErrSesso());
			incaricoFP.setErr_estero(nuovoIncarico.getIncaricatoPersona().getErrEstero());
			incaricoFP.setErr_codice_fiscale_partita_iva(nuovoIncarico.getIncaricatoPersona().getErrCodiceFiscalePartitaIva());
			
			for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico.NuovoPagamento> iterator = nuovoIncarico.getNuovoPagamento().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico.NuovoPagamento nuovoPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, nuovoPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}

			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, Comunicazione.Consulenti.ModificaIncarico modificaIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk();
			incaricoFP.setTipo_record(TIPO_RECORD_INVIATO_UPD);
			incaricoFP.setId_incarico(Long.toString(modificaIncarico.getId()));
			incaricoFP.setAttivita_economica(modificaIncarico.getAttivitaEconomica());
			incaricoFP.setDescrizione_incarico(modificaIncarico.getDescrizioneIncarico());
			incaricoFP.setModalita_acquisizione(modificaIncarico.getModalitaAcquisizione());
			incaricoFP.setTipo_rapporto(modificaIncarico.getTipoRapporto());
			incaricoFP.setVariazioni_incarico(modificaIncarico.getVariazioniIncarico());
			incaricoFP.setDt_inizio(new Timestamp(modificaIncarico.getDataInizio().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setDt_fine(new Timestamp(modificaIncarico.getDataFine().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setImporto_previsto(modificaIncarico.getImportoPrevisto());
			incaricoFP.setFl_saldo(modificaIncarico.isSaldo());

			for (Iterator<Comunicazione.Consulenti.ModificaIncarico.NuovoPagamento> iterator = modificaIncarico.getNuovoPagamento().iterator(); iterator.hasNext();) {
				Comunicazione.Consulenti.ModificaIncarico.NuovoPagamento nuovoPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, nuovoPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}
			for (Iterator<Comunicazione.Consulenti.ModificaIncarico.ModificaPagamento> iterator = modificaIncarico.getModificaPagamento().iterator(); iterator.hasNext();) {
				Comunicazione.Consulenti.ModificaIncarico.ModificaPagamento modificaPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, modificaPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}
			for (Iterator<Comunicazione.Consulenti.ModificaIncarico.CancellaPagamento> iterator = modificaIncarico.getCancellaPagamento().iterator(); iterator.hasNext();) {
				Comunicazione.Consulenti.ModificaIncarico.CancellaPagamento cancellaPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, cancellaPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}

			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, EsitoComunicazione.Consulenti.ModificaIncarico modificaIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk();
			incaricoFP.setTipo_record(TIPO_RECORD_RICEVUTO_UPD);
			incaricoFP.setId_incarico(Long.toString(modificaIncarico.getId()));
			incaricoFP.setAttivita_economica(modificaIncarico.getAttivitaEconomica());
			incaricoFP.setDescrizione_incarico(modificaIncarico.getDescrizioneIncarico());
			incaricoFP.setModalita_acquisizione(modificaIncarico.getModalitaAcquisizione());
			incaricoFP.setTipo_rapporto(modificaIncarico.getTipoRapporto());
			incaricoFP.setVariazioni_incarico(modificaIncarico.getVariazioniIncarico());
			incaricoFP.setDt_inizio(new Timestamp(modificaIncarico.getDataInizio().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setDt_fine(new Timestamp(modificaIncarico.getDataFine().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setImporto_previsto(modificaIncarico.getImportoPrevisto());
			incaricoFP.setFl_saldo(modificaIncarico.isSaldo());

			incaricoFP.setEsito_incarico(modificaIncarico.getEsito()!=null?modificaIncarico.getEsito().toString():null);
			incaricoFP.setErr_attivita_economica(modificaIncarico.getErrAttivitaEconomica());
			incaricoFP.setErr_dt_inizio(modificaIncarico.getErrDataInizio());
			incaricoFP.setErr_dt_fine(modificaIncarico.getErrDataFine());
			incaricoFP.setErr_descrizione_incarico(modificaIncarico.getErrDescrizioneIncarico());
			incaricoFP.setErr_id(modificaIncarico.getErrId());
			incaricoFP.setErr_importo_previsto(modificaIncarico.getErrImportoPrevisto());
			incaricoFP.setErr_modalita_acquisizione(modificaIncarico.getErrModalitaAcquisizione());
			incaricoFP.setErr_saldo(modificaIncarico.getErrSaldo());
			incaricoFP.setErr_tipo_rapporto(modificaIncarico.getErrTipoRapporto());
			incaricoFP.setErr_variazioni_incarico(modificaIncarico.getErrVariazioniIncarico());
			
			for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico.NuovoPagamento> iterator = modificaIncarico.getNuovoPagamento().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico.NuovoPagamento modificaPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, modificaPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}
			for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico.ModificaPagamento> iterator = modificaIncarico.getModificaPagamento().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico.ModificaPagamento modificaPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, modificaPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}
			for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico.CancellaPagamento> iterator = modificaIncarico.getCancellaPagamento().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico.CancellaPagamento cancellaPagamento = iterator.next();
				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, cancellaPagamento);
				incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
			}

			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, Comunicazione.Consulenti.CancellaIncarico cancellaIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk();
			incaricoFP.setTipo_record(TIPO_RECORD_INVIATO_DEL);
			incaricoFP.setId_incarico(Long.toString(cancellaIncarico.getId()));
			
			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, EsitoComunicazione.Consulenti.CancellaIncarico cancellaIncarico) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk();
			incaricoFP.setTipo_record(TIPO_RECORD_RICEVUTO_DEL);
			incaricoFP.setId_incarico(Long.toString(cancellaIncarico.getId()));
			incaricoFP.setEsito_incarico(cancellaIncarico.getEsito()!=null?cancellaIncarico.getEsito().toString():null);
			incaricoFP.setErr_id(cancellaIncarico.getErrId());
			
			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType nuovaComunicazione, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType nuovoConsulente) throws it.cnr.jada.comp.ApplicationException {
		return copyFrom(userContext, nuovaComunicazione, nuovoConsulente,null);
	}
	
	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType nuovaComunicazione, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType nuovoConsulente, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType nuovoConsulenteEsito) throws it.cnr.jada.comp.ApplicationException {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			int esercizio_repertorio = new Integer(nuovoConsulente.getIdMittente().substring(0,4)); 
			Long pg_repertorio = new Long(nuovoConsulente.getIdMittente().substring(5)); 
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk(esercizio_repertorio,pg_repertorio, nuovoConsulenteEsito==null?Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW:Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW, new Long(1));
			incaricoFP.setAnno_riferimento(nuovaComunicazione.getInserimentoIncarichi().getAnnoRiferimento().intValue());
			incaricoFP.setSemestre_riferimento(nuovoConsulente.getIncarico().getSemestreRiferimento().intValue());
			incaricoFP.setCodice_ente(String.valueOf(nuovaComunicazione.getInserimentoIncarichi().getCodiceEnte()));
			incaricoFP.setCognome(nuovoConsulente.getIncaricato().getPersonaFisica().getCognome());
			incaricoFP.setNome(nuovoConsulente.getIncaricato().getPersonaFisica().getNome());
			incaricoFP.setData_nascita(new Timestamp(formatter.parse(nuovoConsulente.getIncaricato().getPersonaFisica().getDataNascita().toString()).getTime()));
			incaricoFP.setTi_sesso(nuovoConsulente.getIncaricato().getPersonaFisica().getSesso().toString());
			incaricoFP.setFl_estero((Boolean)new CHARToBooleanConverter().sqlToJava(nuovoConsulente.getIncaricato().getPersonaFisica().getEstero().name()));
			incaricoFP.setCodice_fiscale_partita_iva(nuovoConsulente.getIncaricato().getPersonaFisica().getCodiceFiscale());
			incaricoFP.setDescrizione_incarico(nuovoConsulente.getIncarico().getDescrizioneIncarico());
			incaricoFP.setDt_inizio(new Timestamp(nuovoConsulente.getIncarico().getDataInizio().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setDt_fine(new Timestamp(nuovoConsulente.getIncarico().getDataFine().toGregorianCalendar().getTime().getTime()));
			incaricoFP.setImporto_previsto(nuovoConsulente.getIncarico().getImporto());
			incaricoFP.setFl_riferimento_regolamento((Boolean)new CHARToBooleanConverter().sqlToJava(nuovoConsulente.getIncarico().getRiferimentoRegolamento().name()));
			incaricoFP.setFl_saldo(nuovoConsulente.getIncarico().getIncaricoSaldato().intValue()==2?Boolean.FALSE:Boolean.TRUE);
			incaricoFP.setAttivita_economica(nuovoConsulente.getIncarico().getAttivitaEconomica());
			incaricoFP.setTipo_rapporto(nuovoConsulente.getIncarico().getTipoRapporto());
			incaricoFP.setModalita_acquisizione(nuovoConsulente.getIncarico().getModalitaAcquisizione());
			incaricoFP.setTipologia_consulente(null);
			if (nuovoConsulenteEsito!=null)
				incaricoFP.setId_incarico(String.valueOf(nuovoConsulenteEsito.getId()));
			
			if (nuovoConsulente.getPagamenti()!=null){
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType.Pagamenti.NuovoPagamento> iterator = nuovoConsulente.getPagamenti().getNuovoPagamento().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType.Pagamenti.NuovoPagamento nuovoPagamento = iterator.next();
					Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, nuovoPagamento);
					incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
				}
			}
			
			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType modificaComunicazione, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente) throws it.cnr.jada.comp.ApplicationException {
		return copyFrom(userContext, modificaComunicazione, modificaConsulente,null);
	}
	
	public static Incarichi_comunicati_fpBulk copyFrom(UserContext userContext, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType modificaComunicazione, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType modificaConsulenteEsito) throws it.cnr.jada.comp.ApplicationException {
		try {
			int esercizio_repertorio = new Integer(modificaConsulente.getIdMittente().substring(0,4)); 
			Long pg_repertorio = new Long(modificaConsulente.getIdMittente().substring(5)); 
			Incarichi_comunicati_fpBulk incaricoFP = new Incarichi_comunicati_fpBulk(esercizio_repertorio, pg_repertorio, Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_UPD, null);
			incaricoFP.setAttivita_economica(modificaConsulente.getIncarico().getAttivitaEconomica());
			incaricoFP.setDescrizione_incarico(modificaConsulente.getIncarico().getDescrizioneIncarico());
			incaricoFP.setModalita_acquisizione(modificaConsulente.getIncarico().getModalitaAcquisizione());
			incaricoFP.setTipo_rapporto(modificaConsulente.getIncarico().getTipoRapporto());
			incaricoFP.setDt_fine(modificaConsulente.getIncarico().getDataFine()!=null?new Timestamp(modificaConsulente.getIncarico().getDataFine().toGregorianCalendar().getTime().getTime()):null);
			incaricoFP.setImporto_previsto(modificaConsulente.getIncarico().getImporto());
			incaricoFP.setFl_saldo(modificaConsulente.getIncarico().getIncaricoSaldato()!=null?(modificaConsulente.getIncarico().getIncaricoSaldato().intValue()==2?Boolean.FALSE:Boolean.TRUE):null);

			if (modificaConsulenteEsito!=null)
				incaricoFP.setId_incarico(String.valueOf(modificaConsulenteEsito.getId()));
			
			if (modificaConsulente.getPagamenti()!=null){
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.NuovoPagamento> iterator = modificaConsulente.getPagamenti().getNuovoPagamento().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.NuovoPagamento nuovoPagamento = iterator.next();
					Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = Incarichi_comunicati_fp_detBulk.copyFrom(userContext, incaricoFP, nuovoPagamento);
					incaricoFP.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);
				}
			}
			
			incaricoFP.setToBeCreated();
			return incaricoFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public void updateFrom(UserContext userContext, Incarichi_comunicati_fpBulk incaricoFP) throws it.cnr.jada.comp.ApplicationException {
		try {
			if (incaricoFP.getId_incarico()!=null) this.setId_incarico((incaricoFP.getId_incarico()));
			if (incaricoFP.getAnno_riferimento()!=null) this.setAnno_riferimento(incaricoFP.getAnno_riferimento());
			if (incaricoFP.getSemestre_riferimento()!=null) this.setSemestre_riferimento(incaricoFP.getSemestre_riferimento());
			if (incaricoFP.getCodice_ente()!=null) this.setCodice_ente(incaricoFP.getCodice_ente());
			if (incaricoFP.getCognome()!=null) this.setCognome(incaricoFP.getCognome());
			if (incaricoFP.getNome()!=null) this.setNome(incaricoFP.getNome());
			if (incaricoFP.getData_nascita()!=null) this.setData_nascita(incaricoFP.getData_nascita());
			if (incaricoFP.getTi_sesso()!=null) this.setTi_sesso(incaricoFP.getTi_sesso());
			if (incaricoFP.getFl_estero()!=null) this.setFl_estero(incaricoFP.getFl_estero());
			if (incaricoFP.getCodice_fiscale_partita_iva()!=null) this.setCodice_fiscale_partita_iva(incaricoFP.getCodice_fiscale_partita_iva());
			if (incaricoFP.getDescrizione_incarico()!=null) this.setDescrizione_incarico(incaricoFP.getDescrizione_incarico());
			if (incaricoFP.getDt_inizio()!=null) this.setDt_inizio(incaricoFP.getDt_inizio());
			if (incaricoFP.getDt_fine()!=null) this.setDt_fine(incaricoFP.getDt_fine());
			if (incaricoFP.getImporto_previsto()!=null) this.setImporto_previsto(incaricoFP.getImporto_previsto());
			if (incaricoFP.getFl_riferimento_regolamento()!=null) this.setFl_riferimento_regolamento(incaricoFP.getFl_riferimento_regolamento());
			if (incaricoFP.getFl_saldo()!=null) this.setFl_saldo(incaricoFP.getFl_saldo());
			if (incaricoFP.getAttivita_economica()!=null) this.setAttivita_economica(incaricoFP.getAttivita_economica());
			if (incaricoFP.getTipo_rapporto()!=null) this.setTipo_rapporto(incaricoFP.getTipo_rapporto());
			if (incaricoFP.getModalita_acquisizione()!=null) this.setModalita_acquisizione(incaricoFP.getModalita_acquisizione());
			if (incaricoFP.getTipologia_consulente()!=null) this.setTipologia_consulente(incaricoFP.getTipologia_consulente());
			if (incaricoFP.getVariazioni_incarico()!=null) this.setVariazioni_incarico(incaricoFP.getVariazioni_incarico());
			if (incaricoFP.getId_incarico()!=null) this.setId_incarico(incaricoFP.getId_incarico());
			
			this.setEsito_incarico(incaricoFP.getEsito_incarico());
			this.setEsito_incarico_persona(incaricoFP.getEsito_incarico_persona());
			this.setErr_id(incaricoFP.getErr_id());
			this.setErr_anno_riferimento(incaricoFP.getErr_anno_riferimento());
			this.setErr_semestre_riferimento(incaricoFP.getErr_semestre_riferimento());
			this.setErr_codice_ente(incaricoFP.getErr_codice_ente());
			this.setErr_descrizione_incarico(incaricoFP.getErr_descrizione_incarico());
			this.setErr_attivita_economica(incaricoFP.getErr_attivita_economica());
			this.setErr_dt_inizio(incaricoFP.getErr_dt_inizio());
			this.setErr_dt_fine(incaricoFP.getErr_dt_fine());
			this.setErr_importo_previsto(incaricoFP.getErr_importo_previsto());
			this.setErr_saldo(incaricoFP.getErr_saldo());
			this.setErr_modalita_acquisizione(incaricoFP.getErr_modalita_acquisizione());
			this.setErr_tipo_rapporto(incaricoFP.getErr_tipo_rapporto());
			this.setErr_variazioni_incarico(incaricoFP.getErr_variazioni_incarico());
			this.setErr_cognome(incaricoFP.getErr_cognome());
			this.setErr_nome(incaricoFP.getErr_nome());
			this.setErr_data_nascita(incaricoFP.getErr_data_nascita());
			this.setErr_sesso(incaricoFP.getErr_sesso());
			this.setErr_estero(incaricoFP.getErr_estero());
			this.setErr_codice_fiscale_partita_iva(incaricoFP.getErr_codice_fiscale_partita_iva());
			this.setVariazioni_incarico(incaricoFP.getVariazioni_incarico());
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public boolean similar(Incarichi_comunicati_fpBulk bulk) {
		return this.getEsercizio_repertorio().equals(bulk.getEsercizio_repertorio()) &&
		       this.getPg_repertorio().equals(bulk.getPg_repertorio()) &&
		       this.getTipo_record().equals(bulk.getTipo_record()) &&
		       Utility.equalsNull(this.getId_incarico(), bulk.getId_incarico()) &&	       
		       Utility.equalsNull(this.getAnno_riferimento(),bulk.getAnno_riferimento()) &&
		       Utility.equalsNull(this.getSemestre_riferimento(), bulk.getSemestre_riferimento()) &&
		       Utility.equalsNull(this.getCodice_ente(), bulk.getCodice_ente()) &&
		       Utility.equalsNull(this.getCognome(), bulk.getCognome()) &&
		       Utility.equalsNull(this.getNome(), bulk.getNome()) &&
		       Utility.equalsNull(this.getData_nascita(), bulk.getData_nascita()) &&
		       Utility.equalsNull(this.getTi_sesso(), bulk.getTi_sesso()) &&
		       Utility.equalsNull(this.getFl_estero(), bulk.getFl_estero()) &&
		       Utility.equalsNull(this.getCodice_fiscale_partita_iva(), bulk.getCodice_fiscale_partita_iva()) &&
		       //Utility.equalsNull(this.getDescrizione_incarico(), bulk.getDescrizione_incarico()) &&
		       Utility.equalsNull(this.getDt_inizio(), bulk.getDt_inizio()) &&
		       Utility.equalsNull(this.getDt_fine(), bulk.getDt_fine()) &&
		       Utility.equalsNull(this.getImporto_previsto().setScale(2), bulk.getImporto_previsto().setScale(2)) &&
		       Utility.equalsNull(this.getFl_riferimento_regolamento(), bulk.getFl_riferimento_regolamento()) &&
		       Utility.equalsNull(this.getFl_saldo(), bulk.getFl_saldo()) &&
		       Utility.equalsNull(this.getAttivita_economica(), bulk.getAttivita_economica()) &&
		       Utility.equalsNull(this.getTipo_rapporto(), bulk.getTipo_rapporto()) &&
		       Utility.equalsNull(this.getModalita_acquisizione(), bulk.getModalita_acquisizione()) &&
		       Utility.equalsNull(this.getTipologia_consulente(), bulk.getTipologia_consulente()) &&
		       Utility.equalsNull(this.getVariazioni_incarico(), bulk.getVariazioni_incarico()) &&
		       Utility.equalsNull(this.getEsito_incarico(), bulk.getEsito_incarico()) &&
		       Utility.equalsNull(this.getEsito_incarico_persona(), bulk.getEsito_incarico_persona())  &&
		       Utility.equalsNull(this.getErr_id(), bulk.getErr_id()) &&
		       Utility.equalsNull(this.getErr_anno_riferimento(), bulk.getErr_anno_riferimento()) &&
		       Utility.equalsNull(this.getErr_semestre_riferimento(), bulk.getErr_semestre_riferimento()) &&
		       Utility.equalsNull(this.getErr_codice_ente(), bulk.getErr_codice_ente()) &&
		       Utility.equalsNull(this.getErr_descrizione_incarico(), bulk.getErr_descrizione_incarico()) &&
		       Utility.equalsNull(this.getErr_attivita_economica(), bulk.getErr_attivita_economica()) &&
		       Utility.equalsNull(this.getErr_dt_inizio(), bulk.getErr_dt_inizio()) &&
		       Utility.equalsNull(this.getErr_dt_fine(), bulk.getErr_dt_fine()) &&
		       Utility.equalsNull(this.getErr_importo_previsto(), bulk.getErr_importo_previsto()) &&
		       Utility.equalsNull(this.getErr_saldo(), bulk.getErr_saldo()) &&
		       Utility.equalsNull(this.getErr_modalita_acquisizione(), bulk.getErr_modalita_acquisizione()) &&
		       Utility.equalsNull(this.getErr_tipo_rapporto(), bulk.getErr_tipo_rapporto()) &&
		       Utility.equalsNull(this.getErr_variazioni_incarico(), bulk.getErr_variazioni_incarico()) &&
		       Utility.equalsNull(this.getErr_cognome(), bulk.getErr_cognome()) &&
		       Utility.equalsNull(this.getErr_nome(), bulk.getErr_nome()) &&
		       Utility.equalsNull(this.getErr_data_nascita(), bulk.getErr_data_nascita()) &&
		       Utility.equalsNull(this.getErr_sesso(), bulk.getErr_sesso()) &&
		       Utility.equalsNull(this.getErr_estero(), bulk.getErr_estero()) &&
		       Utility.equalsNull(this.getErr_codice_fiscale_partita_iva(), bulk.getErr_codice_fiscale_partita_iva());
	}

	public BulkList getIncarichi_comunicati_fp_detColl() {
		return incarichi_comunicati_fp_detColl;
	}
	
	public void setIncarichi_comunicati_fp_detColl(
			BulkList incarichiComunicatiFpDetColl) {
		incarichi_comunicati_fp_detColl = incarichiComunicatiFpDetColl;
	}

	public int addToIncarichi_comunicati_fp_detColl( Incarichi_comunicati_fp_detBulk det )	{
		incarichi_comunicati_fp_detColl.add(det);
		det.setIncarichi_comunicati_fp(this);
		return incarichi_comunicati_fp_detColl.size()-1;
	}
	
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 incarichi_comunicati_fp_detColl };
	}	
}
