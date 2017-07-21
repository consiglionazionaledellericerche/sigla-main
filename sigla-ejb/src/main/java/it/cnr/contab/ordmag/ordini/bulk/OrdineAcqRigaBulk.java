/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Dictionary;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.CRUDBP;
public class OrdineAcqRigaBulk extends OrdineAcqRigaBase {
	private java.lang.String dspTipoConsegna;

	private java.sql.Timestamp dspDtPrevConsegna;

	private java.math.BigDecimal dspQuantita;

	private LuogoConsegnaMagBulk dspLuogoConsegna;

	private MagazzinoBulk dspMagazzino;

	private UnitaOperativaOrdBulk dspUopDest;
	
	public final static String STATO_INSERITA= "INS";
    public final static String STATO_ANNULLATA= "ANN";
	/**
	 * [ORDINE_ACQ Testata Ordine d'Acquisto]
	 **/
	private OrdineAcqBulk ordineAcq =  new OrdineAcqBulk();
	/**
	 * [BENE_SERVIZIO Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	/**
	 * [VOCE_IVA La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entità si riferisce alla normativa vigente sull'iva.]
	 **/
	private Voce_ivaBulk voceIva =  new Voce_ivaBulk();
	/**
	 * [UNITA_MISURA Rappresenta l'anagrafica delle unità di misura.]
	 **/
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_RIGA
	 **/
	public OrdineAcqRigaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_RIGA
	 **/
	public OrdineAcqRigaBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga);
		setOrdineAcq( new OrdineAcqBulk(cdCds,cdUnitaOperativa,esercizio,cdNumeratore,numero) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Testata Ordine d'Acquisto]
	 **/
	public OrdineAcqBulk getOrdineAcq() {
		return ordineAcq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Testata Ordine d'Acquisto]
	 **/
	public void setOrdineAcq(OrdineAcqBulk ordineAcq)  {
		this.ordineAcq=ordineAcq;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entità si riferisce alla normativa vigente sull'iva.]
	 **/
	public Voce_ivaBulk getVoceIva() {
		return voceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entità si riferisce alla normativa vigente sull'iva.]
	 **/
	public void setVoceIva(Voce_ivaBulk voceIva)  {
		this.voceIva=voceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		OrdineAcqBulk ordineAcq = this.getOrdineAcq();
		if (ordineAcq == null)
			return null;
		return getOrdineAcq().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getOrdineAcq().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		OrdineAcqBulk ordineAcq = this.getOrdineAcq();
		if (ordineAcq == null)
			return null;
		return getOrdineAcq().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcq().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		OrdineAcqBulk ordineAcq = this.getOrdineAcq();
		if (ordineAcq == null)
			return null;
		return getOrdineAcq().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getOrdineAcq().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		OrdineAcqBulk ordineAcq = this.getOrdineAcq();
		if (ordineAcq == null)
			return null;
		return getOrdineAcq().getCdNumeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getOrdineAcq().setCdNumeratore(cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public java.lang.Integer getNumero() {
		OrdineAcqBulk ordineAcq = this.getOrdineAcq();
		if (ordineAcq == null)
			return null;
		return getOrdineAcq().getNumero();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public void setNumero(java.lang.Integer numero)  {
		this.getOrdineAcq().setNumero(numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public java.lang.String getCdVoceIva() {
		Voce_ivaBulk voceIva = this.getVoceIva();
		if (voceIva == null)
			return null;
		return getVoceIva().getCd_voce_iva();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(java.lang.String cdVoceIva)  {
		this.getVoceIva().setCd_voce_iva(cdVoceIva);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura == null)
			return null;
		return getUnitaMisura().getCdUnitaMisura();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.getUnitaMisura().setCdUnitaMisura(cdUnitaMisura);
	}
	public java.lang.String getDspTipoConsegna() {
		return dspTipoConsegna;
	}
	public void setDspTipoConsegna(java.lang.String dspTipoConsegna) {
		this.dspTipoConsegna = dspTipoConsegna;
	}
	public java.sql.Timestamp getDspDtPrevConsegna() {
		return dspDtPrevConsegna;
	}
	public void setDspDtPrevConsegna(java.sql.Timestamp dspDtPrevConsegna) {
		this.dspDtPrevConsegna = dspDtPrevConsegna;
	}
	public java.math.BigDecimal getDspQuantita() {
		return dspQuantita;
	}
	public void setDspQuantita(java.math.BigDecimal dspQuantita) {
		this.dspQuantita = dspQuantita;
	}
	public LuogoConsegnaMagBulk getDspLuogoConsegna() {
		return dspLuogoConsegna;
	}
	public void setDspLuogoConsegna(LuogoConsegnaMagBulk dspLuogoConsegna) {
		this.dspLuogoConsegna = dspLuogoConsegna;
	}
	public MagazzinoBulk getDspMagazzino() {
		return dspMagazzino;
	}
	public void setDspMagazzino(MagazzinoBulk dspMagazzino) {
		this.dspMagazzino = dspMagazzino;
	}
	public UnitaOperativaOrdBulk getDspUopDest() {
		return dspUopDest;
	}
	public void setDspUopDest(UnitaOperativaOrdBulk dspUopDest) {
		this.dspUopDest = dspUopDest;
	}
	public Boolean isROCoefConv(){
		if (getUnitaMisura() != null && getUnitaMisura().getCdUnitaMisura() != null && 
				getBeneServizio() != null && getBeneServizio().getUnitaMisura() != null && getBeneServizio().getCdUnitaMisura() != null && 
				!getUnitaMisura().getCdUnitaMisura().equals(getBeneServizio().getCdUnitaMisura())){
			return false;
		}
		return true;
	}
	public Dictionary getTipoConsegnaKeys() {
		return OrdineAcqConsegnaBulk.TIPO_CONSEGNA;
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) 
	{
		setStato(STATO_INSERITA);
		setImImponibile(BigDecimal.ZERO);
		setImImponibileDivisa(BigDecimal.ZERO);
		setImIva(BigDecimal.ZERO);
		setImIvaDivisa(BigDecimal.ZERO);
		setImTotaleRiga(BigDecimal.ZERO);
		BigDecimal value = null;
		try {
			value = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), 0, "*", Configurazione_cnrBulk.PK_PARAMETRI_ORDINI, Configurazione_cnrBulk.SK_GG_DT_PREV_CONSEGNA).getIm01();
		} catch (RemoteException e) {
		} catch (Exception e) {
		}
		if (value!= null){
			java.sql.Timestamp oggi = null;
			try {
				oggi = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(oggi);
			cal.add(Calendar.DAY_OF_WEEK, value.intValue());
			setDspDtPrevConsegna(DateUtils.truncate(new Timestamp(cal.getTime().getTime()))); 
		}
		return this;
	}
}