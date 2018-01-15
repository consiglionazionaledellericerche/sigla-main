/*
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.jada.bulk.ValidationException;

public class Ass_contratto_ditteBulk extends Ass_contratto_ditteBase {
	
	
	public final static java.util.Dictionary ruoloKeys = new it.cnr.jada.util.OrderedHashtable();


	final public static String LISTA_INVITATE = "I";
	final public static String LISTA_PARTECIPANTI = "P";
	
	final public static String MANDANTE = "MANDANTE";
	final public static String MANDATARIA = "MANDATARIA";
	final public static String ASSOCIATA = "ASSOCIATA";
	final public static String CAPOGRUPPO = "CAPOGRUPPO";
	final public static String CONSORZIATA = "CONSORZIATA";

	static {
		ruoloKeys.put(MANDANTE,"MANDANTE");
		ruoloKeys.put(MANDATARIA,"MANDATARIA");
		ruoloKeys.put(ASSOCIATA,"ASSOCIATA");
		ruoloKeys.put(CAPOGRUPPO,"CAPOGRUPPO");
		ruoloKeys.put(CONSORZIATA,"CONSORZIATA");
	}

	private ContrattoBulk contratto;

	public Ass_contratto_ditteBulk() {
		super();
	}
	public Ass_contratto_ditteBulk(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.Integer pg_dettaglio) {
		super(esercizio, stato_contratto, pg_contratto, pg_dettaglio);
		setContratto(new ContrattoBulk(esercizio,stato_contratto,pg_contratto));
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
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#setEsercizio(java.lang.Integer)
	 */	
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getContratto().setEsercizio(esercizio);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#getEsercizio()
	 */
	public java.lang.Integer getEsercizio () {
		return getContratto().getEsercizio();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#setPg_contratto(java.lang.Long)
	 */
	public void setPg_contratto(java.lang.Long pg_contratto)  {
		this.getContratto().setPg_contratto(pg_contratto);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoKey#getPg_contratto()
	 */
	public java.lang.Long getPg_contratto () {
		return getContratto().getPg_contratto();
	}
	@Override
	public void setStato_contratto(String string) {
		 this.getContratto().setStato(string);
	}
	
	@Override
	public String getStato_contratto() {
		return  getContratto().getStato();
	}

	public static java.util.Dictionary getRuolokeys() {
		return ruoloKeys;
	}
   	@Override
   	public void validate() throws ValidationException {
   				if  (getDenominazione()==null)
   						throw new it.cnr.jada.bulk.ValidationException("Indicare la Denominazione.");
   				if  (!(getCodice_fiscale()!=null  || getId_fiscale()!=null))
   					  throw new it.cnr.jada.bulk.ValidationException("Indicare il Codice Fiscale o Partita Iva/Id Fiscale Estero.");
   				if ((getRuolo()!=null && getDenominazione_rti() ==null) || (getRuolo()==null && getDenominazione_rti() !=null))
   					throw new it.cnr.jada.bulk.ValidationException("Devone essere valorizzati sia il Ruolo che la Denominazione RTI, oppure entrambi non valorizzati.");
   				if(getCodice_fiscale() != null
   		   				&& ((getCodice_fiscale().length()!=11 && getCodice_fiscale().length() != 16)
   		   				|| (getCodice_fiscale().length() == 16 && !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(getCodice_fiscale()))))
   		   					throw new it.cnr.jada.bulk.ValidationException("Codice fiscale inserito errato.");
   				if(getCodice_fiscale() != null && (getCodice_fiscale().length()==11) )
   					for (int i = 0;i < getCodice_fiscale().length();i++)
   						if (!Character.isDigit(getCodice_fiscale().charAt(i)))
   							throw new ValidationException( "Il codice fiscale può essere composto solo da cifre in questo caso." );
   		   						
   				super.validate();
   	}
   	
}