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

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public abstract class Stampa_registri_ivaVBulk extends it.cnr.jada.bulk.OggettoBulk {

	public final static String IMPEGNI_COMPETENZA = "C";
    public final static String IMPEGNI_RESIDUO = "R";


    private String tipoImpegnoFlag;
    
    private java.util.Dictionary tipiImpegnoKeys;

    public java.util.Dictionary getTipiImpegnoKeys() {
    	return TIPI_IMPEGNO_KEYS;
    }

    public final static String SEZIONALI_COMMERCIALI = "C";
    public final static String SEZIONALI_SAN_MARINO_SENZA_IVA = "S";
    public final static String SEZIONALI_BENI_INTRA_UE = "I";
    public final static String SEZIONALI_SERVIZI_NON_RESIDENTI = "X";
    public final static String SEZIONALI_SPLIT_PAYMENT = "P";    
    public final static java.util.Dictionary TIPI_SEZIONALE_KEYS;
    public static java.util.Dictionary MESI;
    public static java.util.Dictionary MESI_INT;
    public static java.util.Dictionary INT_MESI;
    public final static java.util.Dictionary TIPI_IMPEGNO_KEYS;

    private java.util.Collection tipi_sezionali;

    private java.lang.String cd_cds;
    private java.lang.String cd_unita_organizzativa;
    private java.lang.Integer esercizio;

    private String mese= null;

    private String tipo_report= null;
    private String tipo_stampa= null;

    public static final String PROVVISORIO="P";
    public static final String DEFINITIVO="D";

    public static final String RISTAMPA="Y";
    public static final String NON_RISTAMPA="N";

	public static final String TIPO_STAMPA_REGISTRI="REGISTRI";
	public static final String TIPO_STAMPA_RIEPILOGATIVI="RIEPILOGATIVI";
	public static final String TIPO_STAMPA_RIEPILOGATIVI_CENTRO="RIEPILOGATIVI_CENTRO";
	public static final String TIPO_STAMPA_LIQUIDAZIONE="LIQUIDAZIONE";
	public static final String TIPO_STAMPA_LIQUIDAZIONE_MASSA = "LIQUIDAZIONE_MASSA";
	public static final String TIPO_STAMPA_LIQUIDAZIONE_MASSA_PRV = "LIQUIDAZIONE_MASSA_PRV";
	public static final String TIPO_STAMPA_RIEPILOGATIVI_IVA_DIFFERITA="DIFFERITA";

	public static final String GENNAIO= "Gennaio";
    public static final String FEBBRAIO= "Febbraio";
    public static final String MARZO= "Marzo";
    public static final String APRILE= "Aprile";
    public static final String MAGGIO= "Maggio";
    public static final String GIUGNO= "Giugno";
    public static final String LUGLIO= "Luglio";
    public static final String AGOSTO= "Agosto";
    public static final String SETTEMBRE= "Settembre";
    public static final String OTTOBRE= "Ottobre";
    public static final String NOVEMBRE= "Novembre";
    public static final String DICEMBRE= "Dicembre";

    static {

	    initializeHashes();
	    
        TIPI_SEZIONALE_KEYS = new it.cnr.jada.util.OrderedHashtable();
        TIPI_SEZIONALE_KEYS.put(SEZIONALI_COMMERCIALI, "Tutti i sezionali commerciali");
        TIPI_SEZIONALE_KEYS.put(SEZIONALI_SAN_MARINO_SENZA_IVA, "Sezionali per San. Marino senza Iva");
        TIPI_SEZIONALE_KEYS.put(SEZIONALI_BENI_INTRA_UE, "Sezionali Beni non residenti");
        TIPI_SEZIONALE_KEYS.put(SEZIONALI_SERVIZI_NON_RESIDENTI, "Sezionali Servizi non residenti");
        TIPI_SEZIONALE_KEYS.put(SEZIONALI_SPLIT_PAYMENT, "Sezionali Split Payment");
	}
    static {
    	initializeHashes();
        TIPI_IMPEGNO_KEYS = new it.cnr.jada.util.OrderedHashtable();
        TIPI_IMPEGNO_KEYS.put(IMPEGNI_COMPETENZA, "Competenza");
        TIPI_IMPEGNO_KEYS.put(IMPEGNI_RESIDUO, "Residuo");
	}
	private BulkList registri_stampati = new BulkList();

    // DATA_DA TIMESTAMP
    private java.sql.Timestamp data_da;
    // DATA_A TIMESTAMP
    private java.sql.Timestamp data_a;
    // TIPO_SEZIONALE TIPO_SEZIONALE_BULK
    private Tipo_sezionaleBulk tipo_sezionale;

    private boolean ristampa = false;

    private String tipoSezionaleFlag = null;
public int addToRegistri_stampati(Report_statoBulk nuovoRep ) {	

	getRegistri_stampati().add(nuovoRep);
	return getRegistri_stampati().size()-1;
}

/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:35:36 PM)
 * @return BulkList
 */
public void completeFrom(Report_statoBulk reportStato) {

	setTipo_sezionale(reportStato.getTipo_sezionale());
	setData_da(reportStato.getDt_inizio());
	setData_a(reportStato.getDt_fine());
	setCd_cds(reportStato.getCd_cds());
	setCd_unita_organizzativa(reportStato.getCd_unita_organizzativa());

	java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
	cal.setTime(new java.util.Date(getData_da().getTime()));
	Integer meseNum = new Integer(cal.get(java.util.Calendar.MONTH)+1);
	setMese((String)getInt_mesi().get(meseNum));
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getData_a() {
	return data_a;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getData_da() {
	return data_da;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public java.util.Dictionary getInt_mesi() {

	return INT_MESI;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 16.43.24)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getMese() {
	return mese;
}
/**
 * Insert the method's description here.
 * Creation date: (21/06/2002 9.56.57)
 * @author: Alfonso Ardire
 * @return java.util.Dictionary
 */
public java.util.Dictionary getMesi() {

	return MESI;
}
public java.util.Dictionary getMesi_int() {

	return MESI_INT;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:21:46 AM)
 * @return java.lang.Integer
 */
public abstract java.lang.Integer getPageNumber();
/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:35:36 PM)
 * @return BulkList
 */
public BulkList getRegistri_stampati() {
	return registri_stampati;
}
/**
 * Insert the method's description here.
 * Creation date: (21/06/2002 11.28.22)
 * @author: Alfonso Ardire
 * @return java.util.Collection
 */
public java.util.Collection getTipi_sezionali() {
	return tipi_sezionali;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 17.34.28)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public abstract String getTipo_documento_stampato();
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 17.34.28)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getTipo_report() {
	return tipo_report;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 17.34.28)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public abstract String getTipo_report_stampato();
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 16.30.56)
 * @author: Alfonso Ardire
 * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk getTipo_sezionale() {
	return tipo_sezionale;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 14.44.23)
 * @author: Alfonso Ardire
 * @return java.lang.String
 */
public java.lang.String getTipo_stampa() {
	return tipo_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 2:49:04 PM)
 * @return java.lang.String
 */
public java.lang.String getTipoSezionaleFlag() {
	return tipoSezionaleFlag;
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 2:49:04 PM)
 * @return java.lang.String
 */
public java.util.Dictionary getTipoSezionaleFlags() {
	return TIPI_SEZIONALE_KEYS;
}
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.BulkBP bp,it.cnr.jada.action.ActionContext context) {

	if (((it.cnr.contab.gestiva00.bp.StampaRegistriIvaBP)bp).isBulkPrintable())
		((IPrintable)this).setId_report(null);
		
	return this;
}
protected static void initializeHashes() {

	MESI= new OrderedHashtable();
	MESI.put(GENNAIO, GENNAIO);
	MESI.put(FEBBRAIO, FEBBRAIO);
	MESI.put(MARZO, MARZO);
	MESI.put(APRILE, APRILE);
	MESI.put(MAGGIO, MAGGIO);
	MESI.put(GIUGNO, GIUGNO);
	MESI.put(LUGLIO, LUGLIO);
	MESI.put(AGOSTO, AGOSTO);
	MESI.put(SETTEMBRE, SETTEMBRE);
	MESI.put(OTTOBRE, OTTOBRE);
	MESI.put(NOVEMBRE, NOVEMBRE);
	MESI.put(DICEMBRE, DICEMBRE);

	MESI_INT= new OrderedHashtable();
	MESI_INT.put(GENNAIO, new Integer(1));
	MESI_INT.put(FEBBRAIO, new Integer(2));
	MESI_INT.put(MARZO, new Integer(3));
	MESI_INT.put(APRILE, new Integer(4));
	MESI_INT.put(MAGGIO, new Integer(5));
	MESI_INT.put(GIUGNO, new Integer(6));
	MESI_INT.put(LUGLIO, new Integer(7));
	MESI_INT.put(AGOSTO, new Integer(8));
	MESI_INT.put(SETTEMBRE, new Integer(9));
	MESI_INT.put(OTTOBRE, new Integer(10));
	MESI_INT.put(NOVEMBRE, new Integer(11));
	MESI_INT.put(DICEMBRE, new Integer(12));

	INT_MESI= new OrderedHashtable();
	INT_MESI.put(new Integer(1), GENNAIO);
	INT_MESI.put(new Integer(2), FEBBRAIO);
	INT_MESI.put(new Integer(3), MARZO);
	INT_MESI.put(new Integer(4), APRILE);
	INT_MESI.put(new Integer(5), MAGGIO);
	INT_MESI.put(new Integer(6), GIUGNO);
	INT_MESI.put(new Integer(7), LUGLIO);
	INT_MESI.put(new Integer(8), AGOSTO);
	INT_MESI.put(new Integer(9), SETTEMBRE);
	INT_MESI.put(new Integer(10), OTTOBRE);
	INT_MESI.put(new Integer(11), NOVEMBRE);
	INT_MESI.put(new Integer(12), DICEMBRE);
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_da java.sql.Timestamp
 */
public boolean isLiquidazione() {
	return getTipo_stampa() != null && 
			(getTipo_stampa().equals(TIPO_STAMPA_LIQUIDAZIONE) || 
			 getTipo_stampa().equals(TIPO_STAMPA_LIQUIDAZIONE_MASSA) ||
			 getTipo_stampa().equals(TIPO_STAMPA_LIQUIDAZIONE_MASSA_PRV));
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:21:46 AM)
 * @return java.lang.Integer
 */
public abstract boolean isPageNumberRequired();
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_da java.sql.Timestamp
 */
public boolean isRiepilogativo() {
	return getTipo_stampa() != null && getTipo_stampa().equals(TIPO_STAMPA_RIEPILOGATIVI);
}
/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:24:30 PM)
 * @return boolean
 */
public boolean isRistampa() {
	return ristampa;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:21:46 AM)
 * @return java.lang.Integer
 */
public boolean isROPageNumber() {

	return !isPageNumberRequired();
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_da java.sql.Timestamp
 */
public boolean isStarRegistro() {
	return 
		isLiquidazione() || 
		//getTipo_stampa().equals(TIPO_STAMPA_RIEPILOGATIVI_IVA_DIFFERITA) ||
		(isRiepilogativo() && DEFINITIVO.equalsIgnoreCase(getTipo_report()));
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_da java.sql.Timestamp
 */
public boolean isStarSezionali() {
	return 
	(isLiquidazione() || 
	(isRiepilogativo() && 
		((getTipo_sezionale()!=null && getTipo_sezionale().getCd_tipo_sezionale()==null) ||
		(DEFINITIVO.equalsIgnoreCase(getTipo_report())))) ||
	(getTipo_stampa()!=null && getTipo_stampa().equals(TIPO_STAMPA_RIEPILOGATIVI_IVA_DIFFERITA)));	
}
public Report_statoBulk removeFromRegistri_stampati( int indiceDiLinea ) {

	return (Report_statoBulk)getRegistri_stampati().remove(indiceDiLinea);
}

/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @param newCd_unita_organizzativa java.lang.String
 */
public void setCd_unita_organizzativa(java.lang.String newCd_unita_organizzativa) {
	cd_unita_organizzativa = newCd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_a java.sql.Timestamp
 */
public void setData_a(java.sql.Timestamp newData_a) {
	data_a = newData_a;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 12.50.30)
 * @author: Alfonso Ardire
 * @param newData_da java.sql.Timestamp
 */
public void setData_da(java.sql.Timestamp newData_da) {
	data_da = newData_da;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 11.25.30)
 * @author: Alfonso Ardire
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 16.43.24)
 * @author: Alfonso Ardire
 * @param newMese java.lang.String
 */
public void setMese(java.lang.String newMese) {
	mese = newMese;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:21:46 AM)
 * @param newPageNumber java.lang.Integer
 */
public abstract void setPageNumber(java.lang.Integer newPageNumber);
/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:35:36 PM)
 * @param newRegistri_definitivi_stampati BulkList
 */
public void setRegistri_stampati(BulkList newRegistri_stampati) {
	registri_stampati = newRegistri_stampati;
}
/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:24:30 PM)
 * @param newRistampa boolean
 */
public void setRistampa(boolean newRistampa) {
	ristampa = newRistampa;
}
/**
 * Insert the method's description here.
 * Creation date: (21/06/2002 11.28.22)
 * @author: Alfonso Ardire
 * @param newTipi_sezionali java.util.Collection
 */
public void setTipi_sezionali(java.util.Collection newTipi_sezionali) {
	tipi_sezionali = newTipi_sezionali;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2002 17.34.28)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public void setTipo_report(java.lang.String newTipo_report) {
	tipo_report = newTipo_report;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 16.30.56)
 * @author: Alfonso Ardire
 * @param newTipo_sezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public void setTipo_sezionale(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk newTipo_sezionale) {
	tipo_sezionale = newTipo_sezionale;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 14.44.23)
 * @author: Alfonso Ardire
 * @param newTipo_stampa java.lang.String
 */
public void setTipo_stampa(java.lang.String newTipo_stampa) {
	tipo_stampa = newTipo_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 2:49:04 PM)
 * @param newTipiSezionaleFlag java.lang.String
 */
public void setTipoSezionaleFlag(java.lang.String newTipiSezionaleFlag) {
	tipoSezionaleFlag = newTipiSezionaleFlag;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 14.44.23)
 * @author: Alfonso Ardire
 * @param newTipo_stampa java.lang.String
 */
public java.lang.String getTipoImpegnoFlag() {
	return tipoImpegnoFlag;
}
public void setTipoImpegnoFlag(java.lang.String newTipiImpegnoFlag) {
	tipoImpegnoFlag = newTipiImpegnoFlag;
}
public void validate() throws ValidationException {

	super.validate();

	if (getTipo_sezionale() == null && !isStarSezionali())
		throw new ValidationException("Selezionare un tipo sezionale");
	if (getMese() == null && !isRistampa())
		throw new ValidationException("Selezionare un mese");
	if (getPageNumber() == null)
		throw new ValidationException("Specificare il numero di pagina da cui iniziare la stampa!");
	if (getPageNumber().intValue() < 1)
		throw new ValidationException("Il numero di pagina da cui iniziare la stampa deve essere maggiore o uguale a 1!");
}
}
