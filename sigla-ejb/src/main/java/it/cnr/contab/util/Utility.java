/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Set;

import javax.ejb.EJBException;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.ejb.Parametri_cdsComponentSession;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.gestiva00.ejb.LiquidIvaInterfComponentSession;
import it.cnr.contab.incarichi00.ejb.IncarichiProceduraComponentSession;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.incarichi00.ejb.RepertorioLimitiComponentSession;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.prevent01.ejb.PdgContrSpeseComponentSession;
import it.cnr.contab.progettiric00.ejb.geco.ProgettoGecoComponentSession;
import it.cnr.contab.varstanz00.comp.VariazioniStanziamentoResiduoComponent;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class Utility {
	private transient static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);
	public static String TIPO_GESTIONE_SPESA = "S";
	public static String TIPO_GESTIONE_ENTRATA = "E";

	/**
	 * Restituisce true se i due oggetti sono uguali o sono entrambi null
	 * false altrimenti
	 */
	public static boolean equalsNull(Object object1, Object object2){
		if (object1 == null && object2 == null)
			return true;
		else if ((object1 == null && object2 != null)||(object1 != null && object2 == null))
			return false;
		else if (object1 != null && object2 != null)
			return object1.equals(object2);
		return false;
	}
	/**
	 * Restituisce true se i due oggetti sono uguali o sono entrambi null
	 * false altrimenti
	 */
	public static boolean equalsBulkNull(OggettoBulk object1, OggettoBulk object2){
		if (object1 == null && object2 == null)
			return true;
		else if ((object1 == null && object2 != null)||(object1 != null && object2 == null))
			return false;
		else if (object1 != null && object2 != null)
			return object1.equalsByPrimaryKey(object2);
		return false;
	}

	public static BigDecimal nvl(BigDecimal imp){
		if (imp != null)
			return imp;
		return ZERO;  
	}
	/**
	 * Restituisce una Stringa ottenuta sostituendo
	 * nella stringa sorgente alla stringa pattern la stringa replace,
	 * se la stringa pattern non è presente restituisce la stringa sorgente
	 */
	public static String replace(String source, String pattern, String replace)
	{
		if (source!=null){
			final int len = pattern.length();
			StringBuffer sb = new StringBuffer();
			int found = -1;
			int start = 0;

			while( (found = source.indexOf(pattern, start) ) != -1) {
				sb.append(source.substring(start, found));
				sb.append(replace);
				start = found + len;
			}

			sb.append(source.substring(start));
			return sb.toString();
		}
		else 
			return null;
	}


	public static String lpad(double d, int size, char pad) {
		return lpad(Double.toString(d), size, pad);
	}

	public static String lpad(long l, int size, char pad) {
		return lpad(Long.toString(l), size, pad);
	}

	public static String lpad(String s, int size, char pad) {
		StringBuilder builder = new StringBuilder();
		while (builder.length() + s.length() < size) {
			builder.append(pad);
		}
		builder.append(s);
		return builder.toString();
	}	

	public static String NumberToText(int n) {
		// metodo wrapper
		if (n == 0) {
			return "zero";
		} else {
			return NumberToTextRicorsiva(n);
		}
	}

	public static void main(String[] args) {
		System.out.println(NumberToText(new BigDecimal("16754")));
	}
	
	public static String NumberToText(BigDecimal importo) {
		int parteIntera = importo.intValue();
		String parteDecimale = importo.remainder(BigDecimal.ONE).abs().toPlainString();
		if (parteDecimale.length() > 1)
			parteDecimale = parteDecimale.substring(2);
		if (parteIntera == 0) {
			return "zero/" + parteDecimale;
		} else {
			return NumberToTextRicorsiva(parteIntera) + "/" + parteDecimale;
		}
	}

	public static synchronized void loadPersistentInfos() throws ServletException{
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		provider.addIncludeFilter(new AssignableTypeFilter(OggettoBulk.class));
		AdminSession adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");
		Set<BeanDefinition> components = provider.findCandidateComponents("it/cnr");
		for (BeanDefinition component : components){
			try {
			    Class<?> clazz = Class.forName(component.getBeanClassName());
				logger.info("Load PersistentInfo for class: {}",clazz.getName());
				adminSession.loadPersistentInfos(clazz);
				if(clazz.getName().endsWith("Bulk")) {
					logger.info("Load BulkInfo for class: {}",clazz.getName());
					adminSession.loadBulkInfos(clazz);					
				}
			} catch (Exception e) {
				logger.error("Cannot load persistentInfo for class : {}", component.getBeanClassName(), e);
			}

		}		
	}
	
	private static String NumberToTextRicorsiva(int n) {
		if (n < 0) {
			return "meno " + NumberToTextRicorsiva(-n);
		} else if (n == 0){
			return "";
		} else if (n <= 19){
			return new String[] { "uno", "due", "tre", "quattro", "cinque", 
					"sei", "sette", "otto", "nove", "dieci", 
					"undici", "dodici", "tredici", 
					"quattordici", "quindici", "sedici", 
					"diciassette", "diciotto", "diciannove" }[n-1];
		} else if (n <= 99) {
			String[] vettore = 
				{ "venti", "trenta", "quaranta", "cinquanta", "sessanta", 
					"settanta", "ottanta", "novanta" };
			String letter = vettore[n / 10 - 2];
			int t = n % 10; // t è la prima cifra di n
			// se è 1 o 8 va tolta la vocale finale di letter
			if (t == 1 || t == 8 ) {
				letter = letter.substring(0, letter.length() - 1);
			}
			return letter + NumberToTextRicorsiva(n % 10);
		} else if (n <= 199){
			return "cento" + NumberToTextRicorsiva(n % 100);
		} else if (n <= 999){
			int m = n % 100;
			m /= 10; // divisione intera per 10 della variabile
			String letter = "cent";
			if (m != 8){
				letter = letter + "o";
			}
			return NumberToTextRicorsiva(n / 100) + letter + 
					NumberToTextRicorsiva(n % 100);
		}
		else if (n <= 1999){
			return "mille" + NumberToTextRicorsiva(n % 1000);
		}  else if (n <= 999999){
			return NumberToTextRicorsiva(n / 1000) + "mila" + 
					NumberToTextRicorsiva(n % 1000);
		}
		else if (n <= 1999999){
			return "unmilione" + NumberToTextRicorsiva(n % 1000000);
		} else if (n <= 999999999){
			return NumberToTextRicorsiva(n / 1000000) + "milioni" + 
					NumberToTextRicorsiva(n % 1000000);
		} else if (n <= 1999999999){
			return "unmiliardo" + NumberToTextRicorsiva(n % 1000000000);
		} else {
			return NumberToTextRicorsiva(n / 1000000000) + "miliardi" + 
					NumberToTextRicorsiva(n % 1000000000);
		}
	}	
	public static Parametri_cnrComponentSession createParametriCnrComponentSession()throws EJBException, RemoteException {
		return (Parametri_cnrComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class);		
	}
	public static Classificazione_vociComponentSession createClassificazioneVociComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (Classificazione_vociComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	}
	/**
	 * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
	 *
	 * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
	 */
	public static Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws EJBException, RemoteException{
		return (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",Configurazione_cnrComponentSession.class);
	}
	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public static SaldoComponentSession createSaldoComponentSession() throws EJBException{
		return (SaldoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_SaldoComponentSession",SaldoComponentSession.class);
	}
	/**
	 * Crea la CdrComponentSession da usare per effettuare operazioni
	 */
	public static it.cnr.contab.config00.ejb.CDRComponentSession createCdrComponentSession() throws javax.ejb.EJBException{
		return (it.cnr.contab.config00.ejb.CDRComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", it.cnr.contab.config00.ejb.CDRComponentSession.class);
	}
	/**
	 * Crea la CdrComponentSession da usare per effettuare operazioni
	 */
	public static PdgAggregatoModuloComponentSession createPdgAggregatoModuloComponentSession() throws javax.ejb.EJBException {
		return (PdgAggregatoModuloComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_PdgAggregatoModuloComponentSession", PdgAggregatoModuloComponentSession.class);
	}	

	/**
	 * Crea la PdgContrSpeseComponentSession da usare per effettuare operazioni
	 */
	public static PdgContrSpeseComponentSession createPdgContrSpeseComponentSession() throws javax.ejb.EJBException{
		return (PdgContrSpeseComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_PdgContrSpeseComponentSession", PdgContrSpeseComponentSession.class);
	}	
	/**
	 * Crea la Local CdrComponentSession da usare per effettuare operazioni
	 */
	public static PdGVariazioniComponentSession createPdGVariazioniComponentSession() throws javax.ejb.EJBException{
		return (PdGVariazioniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGVariazioniComponentSession", PdGVariazioniComponentSession.class);
	}	
	/**
	 * Crea la Local MandatoComponentSession da usare per effettuare operazioni
	 */
	public static MandatoComponentSession createMandatoComponentSession() throws javax.ejb.EJBException{
		return (MandatoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_MandatoComponentSession", MandatoComponentSession.class);
	}	
	/**
	 * Crea la Local ReversaleComponentSession da usare per effettuare operazioni
	 */
	public static ReversaleComponentSession createReversaleComponentSession() throws javax.ejb.EJBException{
		return (ReversaleComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ReversaleComponentSession", ReversaleComponentSession.class);
	}	
	/**
	 * Crea la Local Unita_organizzativaComponentSession da usare per effettuare operazioni
	 */
	public static Unita_organizzativaComponentSession createUnita_organizzativaComponentSession() throws javax.ejb.EJBException{
		return (Unita_organizzativaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Unita_organizzativaComponentSession", Unita_organizzativaComponentSession.class);
	}	
	/**
	 * Crea la Local TerzoComponentSession da usare per effettuare operazioni
	 */
	public static TerzoComponentSession createTerzoComponentSession() throws javax.ejb.EJBException{
		return (TerzoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_TerzoComponentSession", TerzoComponentSession.class);
	}	
	/**
	 * Crea la RepertorioLimitiComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public static RepertorioLimitiComponentSession createRepertorioLimitiComponentSession() throws EJBException{
		return (RepertorioLimitiComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINCARICHI00_EJB_RepertorioLimitiComponentSession",RepertorioLimitiComponentSession.class);
	}
	/**
	 * Crea la IncarichiRepertorioComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public static IncarichiRepertorioComponentSession createIncarichiRepertorioComponentSession() throws EJBException{
		return (IncarichiRepertorioComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINCARICHI00_EJB_IncarichiRepertorioComponentSession",IncarichiRepertorioComponentSession.class);
	}	
	/**
	 * Crea la IncarichiProceduraComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public static IncarichiProceduraComponentSession createIncarichiProceduraComponentSession() throws EJBException{
		return (IncarichiProceduraComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINCARICHI00_EJB_IncarichiProceduraComponentSession",IncarichiProceduraComponentSession.class);
	}	
	/**
	 * Crea la Remote ProgettoGecoComponentSession da usare per effettuare operazioni
	 */
	public static ProgettoGecoComponentSession createProgettoGecoComponentSession() throws javax.ejb.EJBException{
		return (ProgettoGecoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_GECO_ProgettoGecoComponentSession");
	}	
	public static Parametri_cdsComponentSession createParametriCdsComponentSession()throws EJBException, RemoteException {
		return (Parametri_cdsComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cdsComponentSession", Parametri_cdsComponentSession.class);		
	}
	public static it.cnr.contab.anagraf00.ejb.AbiCabComponentSession createAbiCabComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (it.cnr.contab.anagraf00.ejb.AbiCabComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AbiCabComponentSession", it.cnr.contab.anagraf00.ejb.AbiCabComponentSession.class);
	}
	public static Parametri_enteComponentSession createParametriEnteComponentSession()throws EJBException, RemoteException {
		return (Parametri_enteComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession", Parametri_cnrComponentSession.class);		
	}
	public static FatturaAttivaSingolaComponentSession createFatturaAttivaSingolaComponentSession()throws EJBException, RemoteException {
		return (FatturaAttivaSingolaComponentSession)EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession", FatturaAttivaSingolaComponentSession.class);		
	}
	public static FatturaPassivaComponentSession createFatturaPassivaComponentSession()throws EJBException, RemoteException {
		return (FatturaPassivaComponentSession)EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession", FatturaPassivaComponentSession.class);		
	}
	/**
	 * Crea la Local ReversaleComponentSession da usare per effettuare operazioni
	 */
	public static DistintaCassiereComponentSession createDistintaCassiereComponentSession() throws javax.ejb.EJBException{
		return (DistintaCassiereComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_DistintaCassiereComponentSession", DistintaCassiereComponentSession.class);
	}	
	public static LiquidIvaInterfComponentSession createLiquidIvaInterfComponentSession() throws javax.ejb.EJBException{
		return (LiquidIvaInterfComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRGESTIVA00_EJB_LiquidIvaInterfComponentSession", LiquidIvaInterfComponentSession.class);
	}	
	public static VariazioniStanziamentoResiduoComponentSession createVariazioniStanziamentoResiduoComponentSession() throws javax.ejb.EJBException{
		return (VariazioniStanziamentoResiduoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRVARSTANZ00_EJB_VariazioniStanziamentoResiduoComponentSession", VariazioniStanziamentoResiduoComponentSession.class);
	}	
}
