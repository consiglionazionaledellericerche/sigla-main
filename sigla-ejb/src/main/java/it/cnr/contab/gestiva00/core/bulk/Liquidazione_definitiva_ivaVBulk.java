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

package it.cnr.contab.gestiva00.core.bulk;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.stream.Stream;

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Liquidazione_definitiva_ivaVBulk extends Liquidazione_ivaVBulk {
  
	private java.util.Collection prospetti_stampati;
	private BulkList ripartizione_finanziaria;
	private BulkList variazioni_associate;
	private BulkList mandato_righe_associate;
	private boolean Liquidazione_commerciale = false;
	private java.util.Collection liquidazioniProvvisorie; 
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Liquidazione_definitiva_ivaVBulk() {
	super();
}

/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:35:36 PM)
 * @return BulkList
 */
public void completeFrom(Liquidazione_ivaBulk liqDef) {

	setTipoSezionaleFlag(liqDef.getTipo_liquidazione());
	setCd_cds(liqDef.getCd_cds());
	setCd_unita_organizzativa(liqDef.getCd_unita_organizzativa());
	setEsercizio(liqDef.getEsercizio());
	setData_da(liqDef.getDt_inizio());
	setData_a(liqDef.getDt_fine());

	java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
	cal.setTime(new java.util.Date(getData_da().getTime()));
	Integer meseNum = new Integer(cal.get(java.util.Calendar.MONTH)+1);
	if (meseNum.intValue() > 10) meseNum = new Integer(-1);
	setMese((String)getInt_mesi().get(meseNum));
}

/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 11.02.47)
 * @author: Alfonso Ardire
 * @return java.util.Collection
 */
public java.util.Collection getProspetti_stampati() {
	return prospetti_stampati;
}

public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("idReport");
	param.setValoreParam("0");
	//param.setValore_param(getId_report().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("aPagIniziale");
	param.setValoreParam(getPageNumber().toString());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_cds");
	param.setValoreParam(getCd_cds());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_unita_organizzativa");
	param.setValoreParam(getCd_unita_organizzativa());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(getEsercizio().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("ti_liquidazione");
	param.setValoreParam(getTipo_documento_stampato());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("dataInizio");
	param.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(getData_da()));
	param.setParamType("java.util.Date");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("dataFine");
	param.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(getData_a()));
	param.setParamType("java.util.Date");
	printSpoolerParam.add(param);
	
	return printSpoolerParam;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:18:38 PM)
 */
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
//		params.add(getId_report().toString());
		params.add("0"); // Nella liquidazione definitiva il pg di liq. è 0
		params.add(getPageNumber().toString());
		params.add(getCd_cds());
		params.add(getCd_unita_organizzativa());
		params.add(getEsercizio().toString());
		params.add(getTipo_documento_stampato());
		params.add(getData_da());
		params.add(getData_a());
	}
	return params;

}

public java.lang.String getTipo_documento_stampato() {
	return getTipoSezionaleFlag();
}

/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
	
	bulk.setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	bulk.setTipo_stampa(TIPO_STAMPA_LIQUIDAZIONE);
	bulk.resetLiquidazioneIva();
	
	bulk.setTipo_report(DEFINITIVO);
	bulk.setTipoSezionaleFlag(SEZIONALI_COMMERCIALI);
	bulk.setLiquidazione_commerciale(true);
	bulk.setTipoImpegnoFlag(IMPEGNI_COMPETENZA);
	bulk.setPageNumber(new Integer(1));
	
	return bulk;
}

public boolean isPageNumberRequired() {
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public boolean isRistampabile() {
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 11.02.47)
 * @author: Alfonso Ardire
 * @param newProspetti_stampati java.util.Collection
 */
public void setProspetti_stampati(java.util.Collection newProspetti_stampati) {
	prospetti_stampati = newProspetti_stampati;
}
public void validate() throws ValidationException {

	super.validate();

	if (getTipoImpegnoFlag() == null && getTipoSezionaleFlag().equals(SEZIONALI_COMMERCIALI))
		throw new ValidationException("Selezionare il tipo impegno");
}

public boolean isLiquidazione_commerciale() {
	return Liquidazione_commerciale;
}

public void setLiquidazione_commerciale(boolean liquidazione_commerciale) {
	Liquidazione_commerciale = liquidazione_commerciale;
}

public BulkList getRipartizione_finanziaria() {
	return ripartizione_finanziaria;
}

public void setRipartizione_finanziaria(BulkList ripartizione_finanziaria) {
	this.ripartizione_finanziaria = ripartizione_finanziaria;
}

public BulkList getVariazioni_associate() {
	return variazioni_associate;
}

public void setVariazioni_associate(BulkList variazioni_associate) {
	this.variazioni_associate = variazioni_associate;
}

public BulkList getMandato_righe_associate() {
	return mandato_righe_associate;
}

public void setMandato_righe_associate(BulkList mandato_righe_associate) {
	this.mandato_righe_associate = mandato_righe_associate;
}
public int addToRipartizione_finanziaria(Liquidazione_iva_ripart_finBulk dett) {
	dett.setCd_cds( this.getCd_cds() );
	dett.setEsercizio( this.getEsercizio() );
	dett.setCd_unita_organizzativa( this.getCd_unita_organizzativa() );
	dett.setTipo_liquidazione( this.getTipoSezionaleFlag() );
	dett.setDt_inizio( this.getData_da());
	dett.setDt_fine(this.getData_a());
	//Nel caso di liquidazione dicembre dovendo imputare la variazione sui residui 
	//viene eliminato l'anno corrente
	if (Liquidazione_ivaVBulk.DICEMBRE.equals(this.getMese()))
		dett.getAnniList().remove(dett.getEsercizio());
	ripartizione_finanziaria.add(dett);
	return ripartizione_finanziaria.size()-1;
}

public Liquidazione_iva_ripart_finBulk removeFromRipartizione_finanziaria(int index) {
	Liquidazione_iva_ripart_finBulk dett = (Liquidazione_iva_ripart_finBulk)ripartizione_finanziaria.remove(index);
	return dett;
}

public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
	return new it.cnr.jada.bulk.BulkCollection[] {ripartizione_finanziaria};
}

public boolean isRegistroStampato(String mese) { 
	return Optional.ofNullable(this.getProspetti_stampati())
			.map(e->
				e.stream().filter(x->{
					Calendar cal = new GregorianCalendar();
					cal.setTime(((Liquidazione_ivaBulk)x).getDt_inizio());
					return (cal.get(Calendar.MONTH)==Calendar.DECEMBER && mese.equals(Stampa_registri_ivaVBulk.DICEMBRE)) ||
						   (cal.get(Calendar.MONTH)!=Calendar.DECEMBER && cal.get(Calendar.MONTH)==(Integer)this.getMesi_int().get(mese)-1);
				})
				.findFirst()
				.isPresent()
			)
			.orElse(Boolean.FALSE);
}
public java.util.Collection getLiquidazioniProvvisorie() {
	return liquidazioniProvvisorie;
}
public void setLiquidazioniProvvisorie(java.util.Collection liquidazioniProvvisorie) {
	this.liquidazioniProvvisorie = liquidazioniProvvisorie;
}
public Liquidazione_ivaBulk getLastLiquidazioneProvvisoria() {
	if (this.getLiquidazioniProvvisorie()!=null && !this.getLiquidazioniProvvisorie().isEmpty())
		return (Liquidazione_ivaBulk)Collections.max(this.getLiquidazioniProvvisorie(), Comparator.comparingLong(i -> ((Liquidazione_ivaBulk)i).getReport_id()));
	return null;
}
public BigDecimal getDebitoLastLiquidazioneProvvisoria() {
	Liquidazione_ivaBulk last = getLastLiquidazioneProvvisoria();
	if (last!=null && last.getIva_da_versare()!=null && last.getIva_da_versare().compareTo(BigDecimal.ZERO)<0)
		return last.getIva_da_versare().abs();
	return BigDecimal.ZERO;
}
public Timestamp getDataAggiornamentoLastLiquidazioneProvvisoria() {
	Liquidazione_ivaBulk last = getLastLiquidazioneProvvisoria();
	if (last!=null)
		return last.getDacr();
	return null;
}
public BigDecimal getTotaleRipartizioneFinanziaria() {
	Stream<Liquidazione_iva_ripart_finBulk> lis = getRipartizione_finanziaria().stream().map(Liquidazione_iva_ripart_finBulk.class::cast);
	return lis.map(Liquidazione_iva_ripart_finBulk::getIm_variazione).reduce(BigDecimal.ZERO, BigDecimal::add);
}
public String getNextMeseForLiquidazioneDefinitiva() { 
	if (this.getProspetti_stampati()==null || this.getProspetti_stampati().isEmpty())
		return (String)this.getInt_mesi().get(
				this.getMesi_int().get(Stampa_registri_ivaVBulk.DICEMBRE));
	Stream<Liquidazione_ivaBulk> lis = 
			this.getProspetti_stampati().stream().map(Liquidazione_ivaBulk.class::cast);
	return lis.max((e1,e2)->e1.getDt_inizio().compareTo(e2.getDt_inizio()))
			.map(x->{
					Calendar cal = new GregorianCalendar();
					cal.setTime(x.getDt_inizio());
					if (cal.get(Calendar.MONTH)==Calendar.DECEMBER)
						return (String)this.getInt_mesi().get(
								this.getMesi_int().get(Stampa_registri_ivaVBulk.GENNAIO));
					if (cal.get(Calendar.MONTH)==Calendar.NOVEMBER)
						return null;
					return (String)this.getInt_mesi().get(cal.get(Calendar.MONTH)+2);	
				})
			.orElse(null);
}
}