package it.cnr.contab.config00.contratto.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

	

public class Stampa_elenco_contrattiBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	//	Esercizio di scrivania
	private Integer esercizio;
	private String stato;
	
	private java.sql.Date dataInizioValidita_da;
	private java.sql.Date dataInizioValidita_a;
	private java.sql.Date dataFineValidita_da;
	private java.sql.Date dataFineValidita_a;
	private java.sql.Date dataStipula_da;
	private java.sql.Date dataStipula_a;
		
		
	private Unita_organizzativaBulk uoForPrint;
	private boolean uoForPrintEnabled;
	
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	
	private Tipo_contrattoBulk tipo_contrattoForPrint;
	private boolean tipo_contrattoForPrintEnabled;
	
	private TerzoBulk terzo_firmatarioForPrint;
	private boolean terzo_firmatarioForPrintEnabled;
	
	private TerzoBulk figura_giuridicaForPrint;
	//private boolean figura_giuridicaForPrintEnabled;
		
	final public static String STATO_D = "D";
	final public static String STATO_P = "P";
	final public static String STATO_A = "C";
	final public static String STATO_NOPROV = "NOPROV";
	final public static String STATO_TUTTI = "*";

	static {
			ti_statoKeys.put(STATO_D,"Definitivo");
			ti_statoKeys.put(STATO_P,"Provvisorio");
			ti_statoKeys.put(STATO_A,"Cessato");
			ti_statoKeys.put(STATO_NOPROV,"Non Provvisorio");
			ti_statoKeys.put(STATO_TUTTI,"Tutti");
			}	
		/**
		 * Stampa_elenco_movimentiBulk constructor comment.
		 */
		public Stampa_elenco_contrattiBulk() {
			super();
		}
		
		public final java.util.Dictionary getti_statoKeys() {
			return ti_statoKeys;
		}
		
		public String getCdUoForPrint() {
			if (getUoForPrint()==null)
			  return "*";
			if(getUoForPrint().getCd_unita_organizzativa()==null)
			  return "*";	
			return getUoForPrint().getCd_unita_organizzativa();
		}

		public String getCd_Tipo_contrattoForPrint() {
			if (getTipo_contrattoForPrint()==null)
				 return "*";
			if(getTipo_contrattoForPrint().getCd_tipo_contratto()==null)
				 return "*";	
			return getTipo_contrattoForPrint().getCd_tipo_contratto();
		}
		
		public String getFig_figura_giuridicaForPrint() {
			if (getFigura_giuridicaForPrint()==null)
				return "*";
			if(getFigura_giuridicaForPrint().getCd_terzo()==null)
				return "*";
			return getFigura_giuridicaForPrint().getCd_terzo().toString();
		}
		
		public String getCd_Terzo_firmatarioForPrint() {
			if (getTerzo_firmatarioForPrint()==null)
				return "*";
			if(getTerzo_firmatarioForPrint().getCd_terzo()==null)
				return "*";
			return getTerzo_firmatarioForPrint().getCd_terzo().toString();
		}	
		
		
		public java.lang.Integer getEsercizio() {
			return esercizio;
		}
				
		public Unita_organizzativaBulk getUoForPrint() {
			return uoForPrint;
		}
		
		public void setUoForPrint(Unita_organizzativaBulk unitaOrganizzativa) {
			this.uoForPrint = unitaOrganizzativa;
		}
		
		public Tipo_contrattoBulk getTipo_contrattoForPrint() {
			return tipo_contrattoForPrint;
		}
		
		public void setTipo_contrattoForPrint(Tipo_contrattoBulk newtipo_Contratto) {
			this.tipo_contrattoForPrint = newtipo_Contratto;
		}
		
		public TerzoBulk getTerzo_firmatarioForPrint() {
			return terzo_firmatarioForPrint;
		}
		
		public void setTerzo_firmatarioForPrint(TerzoBulk terzo_Firmatario) {
			this.terzo_firmatarioForPrint = terzo_Firmatario;
		}	
		
		public TerzoBulk getFigura_giuridicaForPrint() {
			return figura_giuridicaForPrint;
		}
		
		public void setFigura_giuridicaForPrint(TerzoBulk figura_Giuridica) {
			this.figura_giuridicaForPrint = figura_Giuridica;
		}	
				
		public void setEsercizio(java.lang.Integer newEsercizio) {
			esercizio = newEsercizio;
		}
		
		public String getstato() {
			 return stato;
		}
	
		public void setstato(String string) {
			 stato = string;
		}
		
		public boolean isROUoForPrint() {
			 return isUoForPrintEnabled();
		}
		
		public boolean isUoForPrintEnabled() {
			  return uoForPrintEnabled;
		}

		public void setUoForPrintEnabled(boolean b) {
			uoForPrintEnabled = b;
		}
		
		
			public boolean isROTipo_contrattoForPrint() {
				 return isTipo_contrattoForPrintEnabled();
			}
		
			public boolean isTipo_contrattoForPrintEnabled() {
				  return tipo_contrattoForPrintEnabled;
			}

			public void setTipo_contrattoForPrintEnabled(boolean b) {
				tipo_contrattoForPrintEnabled = b;
			}
		
				public boolean isROTerzo_firmatarioForPrint() {
					 return isTerzo_firmatarioForPrintEnabled();
				}
		
				public boolean isTerzo_firmatarioForPrintEnabled() {
					  return terzo_firmatarioForPrintEnabled;
				}

				public void setTerzo_firmatarioForPrintEnabled(boolean b) {
					terzo_firmatarioForPrintEnabled = b;
				}
				
		public java.sql.Date getDataFineValidita_da() {
			return dataFineValidita_da;
		}
		
		public java.sql.Date getDataFineValidita_a() {
			return dataFineValidita_a;
		}
	
		public java.sql.Date getDataInizioValidita_da() {
			return dataInizioValidita_da;
		}	
		
		public java.sql.Date getDataInizioValidita_a() {
			return dataInizioValidita_a;
		}	
		
		public java.sql.Date getDataStipula_da() {
			return dataStipula_da;
		}	
		
		public java.sql.Date getDataStipula_a() {
			return dataStipula_a;
		}
			
		public void setDataFineValidita_da(java.sql.Date newDataFineValidita_da) {
			dataFineValidita_da = newDataFineValidita_da;
		}
		
		public void setDataFineValidita_a(java.sql.Date newDataFineValidita_a) {
			dataFineValidita_a = newDataFineValidita_a;
		}
	
		public void setDataInizioValidita_da(java.sql.Date newDataInizioValidita_da) {
			dataInizioValidita_da = newDataInizioValidita_da;
		}
		
		public void setDataInizioValidita_a(java.sql.Date newDataInizioValidita_a) {
			dataInizioValidita_a = newDataInizioValidita_a;
		}
		
		public void setDataStipula_da(java.sql.Date newDataStipula_da) {
			dataStipula_da = newDataStipula_da;
		}
		
		public void setDataStipula_a(java.sql.Date newDataStipula_a) {
			dataStipula_a = newDataStipula_a;
		}
		
	
}