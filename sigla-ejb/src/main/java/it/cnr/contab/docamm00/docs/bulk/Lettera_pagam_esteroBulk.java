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

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJBException;


import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.intcass.bulk.DistintaCassiere1210Bulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDBP;

@SuppressWarnings("unchecked")
@StorageType(name="D:doccont:document")
public class Lettera_pagam_esteroBulk extends Lettera_pagam_esteroBase implements AllegatoParentBulk, StatoTrasmissione{

	private it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = null;
	private java.util.Vector sospesiCancellati = null;
	private boolean annoDiCompetenza = true;
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	private String documento;
	private DistintaCassiere1210Bulk distintaCassiere;
	
	@SuppressWarnings("rawtypes")
	public final static java.util.Dictionary stato_trasmissioneKeys;
	static 
	{
		stato_trasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO,	"Non inserito in distinta");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO,	"Predisposto alla Firma");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA,	"Prima Firma");		
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_INSERITO,		"Inserito in distinta");		
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_TRASMESSO,		"Trasmesso");
		stato_trasmissioneKeys.put(StatoTrasmissione.ALL, "Tutti");
	}
	public final static String BONIFICO_MEZZO_SWIFT = "S", BONIFICO_MEZZO_TELEGRAMMA = "T", BONIFICO_MEZZO_ASSEGNO = "A";
	@SuppressWarnings("rawtypes")
	public final static java.util.Dictionary ti_bonifico_mezzoKeys;
	static 
	{
		ti_bonifico_mezzoKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_bonifico_mezzoKeys.put(BONIFICO_MEZZO_SWIFT, "S.W.I.F.T.");
		ti_bonifico_mezzoKeys.put(BONIFICO_MEZZO_TELEGRAMMA, "telegramma - telex");
		ti_bonifico_mezzoKeys.put(BONIFICO_MEZZO_ASSEGNO, "assegno da inoltrare al beneficiario");				
	}

	public final static String AMMONTARE_DEBITO_NOSTRO_CONTO = "N", AMMONTARE_DEBITO_CONTO_PROVVISORIO = "P", AMMONTARE_DEBITO_CONTO_SPEC_DEBITORE = "D";
	@SuppressWarnings("rawtypes")
	public final static java.util.Dictionary ti_ammontare_debitoKeys;
	static 
	{
		ti_ammontare_debitoKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_ammontare_debitoKeys.put(AMMONTARE_DEBITO_NOSTRO_CONTO, "nostro conto in");
		ti_ammontare_debitoKeys.put(AMMONTARE_DEBITO_CONTO_PROVVISORIO, "contro provvisorio a nostro nome");
		ti_ammontare_debitoKeys.put(AMMONTARE_DEBITO_CONTO_SPEC_DEBITORE, "conto spec debitore in");				
	}

	public final static String SHA = "Scelta1", OUR = "Scelta2", BEN = "Scelta3";
	@SuppressWarnings("rawtypes")
	public final static java.util.Dictionary ti_commissione_speseKeys;
	static 
	{
		ti_commissione_speseKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_commissione_speseKeys.put(SHA, "SHA -> L'Ordinante e il Beneficiario supportano ciascuno le spese della propria Banca");
		ti_commissione_speseKeys.put(OUR, "OUR*** -> Tutte le spese – anche quelle della Banca del Beneficiario – sono a carico dell'Ordinante");
		ti_commissione_speseKeys.put(BEN, "BEN*** -> Tutte le spese – anche quelle della Banca dell'Ordinante – sono a carico del Beneficiario");
	}
	public enum Divisa {
		AED,AUD,BGN,CAD,CHF,CNY,CZK,DKK,EUR,GBP,HKD,HUF,ILS,INR,JPY,KWD,
		MAD,MXN,NOK,NZD,PLN,QAR,RON,RUB,SAR,SEK,SGD,THB,TND,TRY,USD,ZAR
	}
	public final static java.util.Dictionary ti_divisaKeys = new it.cnr.jada.util.OrderedHashtable();
	static {
		Arrays.asList(Divisa.values()).stream().forEach(divisa -> {
			ti_divisaKeys.put(divisa.name(), divisa.name());
		});
	}

	public Lettera_pagam_esteroBulk() {
		super();
		setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
	}
	public Lettera_pagam_esteroBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_lettera) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_lettera);
		setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
	}
	
	public static java.util.Dictionary getStatoTrasmissionekeys() {
		return stato_trasmissioneKeys;
	}
	
	public static java.util.Dictionary getTiBonificoMezzokeys() {
		return ti_bonifico_mezzoKeys;
	}
	
	public static java.util.Dictionary getTiAmmontareDebitokeys() {
		return ti_ammontare_debitoKeys;
	}
	public static java.util.Dictionary getTiCommissioneSpesekeys() {
		return ti_commissione_speseKeys;
	}
	public void addToSospesiCancellati(SospesoBulk sospeso) {
		if (getSospesiCancellati() == null)
			setSospesiCancellati(new java.util.Vector());
		if (!BulkCollections.containsByPrimaryKey(getSospesiCancellati(), sospeso))
			getSospesiCancellati().addElement(sospeso);
	}

	public void completeFrom(ActionContext context) 
			throws javax.ejb.EJBException, java.text.ParseException, ComponentException, RemoteException {

		java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
		int annoSolare = Fattura_passivaBulk.getDateCalendar(date).get(java.util.Calendar.YEAR);
		int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
		setAnnoDiCompetenza(esercizioInScrivania == getEsercizio().intValue());
		if (annoSolare != esercizioInScrivania)
			date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
		setDt_registrazione(date);

		setIm_commissioni(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		setIm_pagamento(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		setAmmontare_debito(AMMONTARE_DEBITO_NOSTRO_CONTO);				
		try {
			setConto_debito(Utility.createConfigurazioneCnrComponentSession().getVal03(context.getUserContext(), 0, "*", "CONTO_CORRENTE_SPECIALE", "ENTE"));
		} catch (ComponentException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
		SospesoBulk sospeso = new SospesoBulk();
		sospeso.setEsercizio(getEsercizio());
		//if (!Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),this.getEsercizio()).getFl_tesoreria_unica().booleanValue())
			sospeso.setCd_cds(getCd_cds_sospeso());
		sospeso.setTi_entrata_spesa(sospeso.TIPO_SPESA);
		sospeso.setTi_sospeso_riscontro(sospeso.TI_SOSPESO);
		setSospeso(sospeso);
		setUser(context.getUserInfo().getUserid());
	}
	public it.cnr.jada.bulk.OggettoBulk[] getBulksForPersistentcy() {
		return new it.cnr.jada.bulk.OggettoBulk[] { getSospeso() };
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/12/2002 5:18:43 PM)
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public java.util.Vector getSospesiCancellati() {
		return sospesiCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/7/2002 3:17:11 PM)
	 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
	 */
	public it.cnr.contab.doccont00.core.bulk.SospesoBulk getSospeso() {
		return sospeso;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/7/2002 3:17:11 PM)
	 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
	 */
	public java.util.Dictionary getTipo_sospesoKeys() {

		java.util.Dictionary tipi = new java.util.Hashtable();
		tipi.put("E", "Entrata");
		tipi.put("S", "Spesa");
		return tipi;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (03/07/2003 15.36.58)
	 * @return boolean
	 */
	public boolean isAnnoDiCompetenza() {
		return annoDiCompetenza;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/11/2002 5:10:59 PM)
	 */
	public boolean isROSospeso() {

		return false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/11/2002 5:10:59 PM)
	 */
	public boolean isROSospesoSearchTool() {

		return !isAnnoDiCompetenza();	
	}
	public int removeFromSospesiCancellati(SospesoBulk sospeso) {

		if (getSospesiCancellati() == null)
			return -1;
		if (BulkCollections.containsByPrimaryKey(getSospesiCancellati(), sospeso))
			getSospesiCancellati().remove(BulkCollections.indexOfByPrimaryKey(getSospesiCancellati(), sospeso));
		return getSospesiCancellati().size()-1;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (03/07/2003 15.36.58)
	 * @param newAnnoDiCompetenza boolean
	 */
	public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
		annoDiCompetenza = newAnnoDiCompetenza;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/12/2002 5:18:43 PM)
	 * @param newSospesiCancellati it.cnr.jada.bulk.BulkList
	 */
	public void setSospesiCancellati(java.util.Vector newSospesiCancellati) {
		sospesiCancellati = newSospesiCancellati;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/7/2002 3:17:11 PM)
	 * @param newSospeso it.cnr.contab.doccont00.core.bulk.SospesoBulk
	 */
	public void setSospeso(it.cnr.contab.doccont00.core.bulk.SospesoBulk newSospeso) {
		sospeso = newSospeso;
	}
	public void validate() throws ValidationException {
		if (getIm_commissioni() == null)
			throw new ValidationException("Specificare un importo per le commissioni della lettera di pagamento estero!");
		if (getIm_pagamento() != null && getIm_pagamento().compareTo(new java.math.BigDecimal(0)) == 0) {
			throw new ValidationException("Valorizzare l'importo di pagamento!");
		}
		if (getIm_pagamento() != null && getIm_pagamento().compareTo(new java.math.BigDecimal(0)) != 0) {
			if (getIm_pagamento().compareTo(getIm_commissioni()) < 0)
				throw new ValidationException("L'importo delle commissioni della lettera di pagamento estero non puo' superare l'importo di pagamento!");
		}
		if (getDivisa() == null)
			throw new ValidationException("Valorizzare la Divisa!");
		for (int i = 0;i <getDivisa().length();i++)
			if (!Character.isLetter(getDivisa().charAt(i)) && getDivisa().charAt(i)!=' ' )
				throw new ValidationException( "La divisa può essere composta solo da lettere e non può contenere caratteri speciali." );
		if (getBeneficiario() == null)
			throw new ValidationException("Valorizzare il Beneficiario!");
	}

	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
		return super.initializeForInsert(crudbp, actioncontext);
	}
	public String getCMISFolderName() {
		String suffix = "Documento 1210 n.";
		suffix = suffix.concat(String.valueOf(getPg_documento_cont()));
		return suffix;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getStorePath() {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				getCd_unita_organizzativa(),
				"Documenti 1210",
				Optional.ofNullable(getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}


	public Long getPg_documento_cont() {
		return getPg_lettera();
	}
	@StorageProperty(name="doccont:tipo")
	public String getCd_tipo_documento_cont() {
		return "1210";
	}

	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				archivioAllegati };
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
	public DistintaCassiere1210Bulk getDistintaCassiere() {
		return distintaCassiere;
	}
	public void setDistintaCassiere(DistintaCassiere1210Bulk distintaCassiere) {
		this.distintaCassiere = distintaCassiere;
	}
	
	@Override
	public Integer getEsercizio_distinta() {
		if (distintaCassiere == null)
			return null;
		return distintaCassiere.getEsercizio();
	}
	
	@Override
	public void setEsercizio_distinta(Integer esercizio_distinta) {
		distintaCassiere.setEsercizio(esercizio_distinta);
	}

	@Override
	public String getCd_sospeso() {
		if (getSospeso() == null)
			return null;
		return getSospeso().getCd_sospeso();
	}
	
	@Override
	public void setCd_sospeso(String cd_sospeso) {
		if (getSospeso() != null)
			getSospeso().setCd_sospeso(cd_sospeso);
		else
			super.setCd_sospeso(cd_sospeso);
	}
	@Override
	public Long getPg_distinta() {
		if (distintaCassiere == null)
			return null;
		return distintaCassiere.getPgDistinta();
	}
	
	@Override
	public void setPg_distinta(Long pg_distinta) {
		distintaCassiere.setPgDistinta(pg_distinta);
	}
	public String getDisplayStatoTrasmissione() {
		if (getStato_trasmissione() == null)
			return null;
		return (String) stato_trasmissioneKeys.get(getStato_trasmissione());
	}
	public String getCMISName() {
		return getCMISFolderName() + ".pdf";
	}

	/**
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList<Mandato_rigaIBulk> getMandato_rigaColl() {
		return new BulkList<>(Collections.emptyList());
	}

	@Override
	public String toString() {
		return Optional.ofNullable(getPg_lettera())
					.map(lettera -> "Lettera di Pagamento estero n. " + lettera)
				.orElseGet(() -> super.toString());
	}
}