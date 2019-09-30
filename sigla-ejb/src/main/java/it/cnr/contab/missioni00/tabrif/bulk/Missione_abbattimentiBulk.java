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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabter.bulk.*;
import java.util.Dictionary;

import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.OrderedHashtable;

public class Missione_abbattimentiBulk extends Missione_abbattimentiBase {

	private NazioneBulk nazione = new NazioneBulk();

	// la proprietà OPERATORE_DURATA_ORE_KEYS non è più utlizzata
	private final static java.util.Dictionary OPERATORE_DURATA_ORE_KEYS;
    public final static java.lang.String MAGGIORE	= ">";
	public final static java.lang.String MINORE		= "<";

	// la proprietà ORA_DURATA_ORE_KEYS non è più utlizzata
    private final static java.util.Dictionary ORA_DURATA_ORE_KEYS;
    public final static java.lang.String QUATTRO	= "4";
	public final static java.lang.String OTTO		= "8";
	public final static java.lang.String SEPARATORE_DURATA_ORE = " ";

    private final static java.util.Dictionary DURATA_ORE_KEYS;
    public final static java.lang.String MINORE_QUATTRO	= "< 4";
	public final static java.lang.String MAGGIORE_QUATTRO = "> 4";
	public final static java.lang.String MAGGIORE_OTTO = "> 8";

	static {
		OPERATORE_DURATA_ORE_KEYS = new it.cnr.jada.util.OrderedHashtable();
		OPERATORE_DURATA_ORE_KEYS.put(MAGGIORE, ">");
		OPERATORE_DURATA_ORE_KEYS.put(MINORE,   "<");

		ORA_DURATA_ORE_KEYS = new it.cnr.jada.util.OrderedHashtable();
		ORA_DURATA_ORE_KEYS.put(QUATTRO, "4");
		ORA_DURATA_ORE_KEYS.put(OTTO,    "8");

		DURATA_ORE_KEYS = new it.cnr.jada.util.OrderedHashtable();
		DURATA_ORE_KEYS.put(MINORE_QUATTRO,   "< 4");
		DURATA_ORE_KEYS.put(MAGGIORE_QUATTRO, "> 4");
		DURATA_ORE_KEYS.put(MAGGIORE_OTTO,    "> 8");
	}

	private java.lang.String ore;
	private java.lang.String operatore;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rifInquadramento;
	private java.util.Collection rifInquadramenti;


	/**** Gestione flag navigazione, flag vitto gratuito, flag alloggio gratuito e flag vitto alloggio gratuito ***/
	public final static String NESSUNO = "X";	
	public final static String NAVIGAZIONE = "N";
	public final static String VITTO_GRATUITO = "V";
	public final static String ALLOGGIO_GRATUITO = "A";
	public final static String VITTO_ALLOGGIO_GRATUITO = "E";				
	public final static Dictionary flag_spese_gratuiteKeys;
	private String flag_spese_gratuite;	
	static
	{
		flag_spese_gratuiteKeys = new OrderedHashtable();
		flag_spese_gratuiteKeys.put(NESSUNO, "Nessuno");		
		flag_spese_gratuiteKeys.put(NAVIGAZIONE, "Navigazione");
		flag_spese_gratuiteKeys.put(VITTO_GRATUITO, "Vitto gratuito");
		flag_spese_gratuiteKeys.put(ALLOGGIO_GRATUITO, "Alloggio gratuito");
		flag_spese_gratuiteKeys.put(VITTO_ALLOGGIO_GRATUITO, "Vitto e Alloggio gratuito");				
	}
	/**** Gestione flag pasto, alloggio, trasporto ***/
	public final static String NESSUN_TIPO = "X";	
	public final static String PASTO = "P";
	public final static String ALLOGGIO = "A";
	public final static String TRASPORTO = "T";
	public final static Dictionary flag_tipo_speseKeys;
	private String flag_tipo_spese;	
	static
	{
		flag_tipo_speseKeys = new OrderedHashtable();
		flag_tipo_speseKeys.put(NESSUN_TIPO, "Nessuno");		
		flag_tipo_speseKeys.put(PASTO, "Pasto");
		flag_tipo_speseKeys.put(ALLOGGIO, "Alloggio");
		flag_tipo_speseKeys.put(TRASPORTO, "Trasporto");
	}	
	
public Missione_abbattimentiBulk() {
	super();
}
public Missione_abbattimentiBulk(java.sql.Timestamp dt_inizio_validita,java.lang.String durata_ore,java.lang.Boolean fl_alloggio,java.lang.Boolean fl_alloggio_gratuito,java.lang.Boolean fl_navigazione,java.lang.Boolean fl_pasto,java.lang.Boolean fl_trasporto,java.lang.Boolean fl_vitto_alloggio_gratuito,java.lang.Boolean fl_vitto_gratuito,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica) {
	super(dt_inizio_validita,durata_ore,fl_alloggio,fl_alloggio_gratuito,fl_navigazione,fl_pasto,fl_trasporto,fl_vitto_alloggio_gratuito,fl_vitto_gratuito,pg_nazione,pg_rif_inquadramento,ti_area_geografica);
	setRifInquadramento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk(pg_rif_inquadramento));
	setNazione(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk(pg_nazione));
	setDurata_ore(durata_ore);
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 15.20.50)
 */
//
//	Gestisco le condizioni di ricerca del Tipo Spesa in base alle 
//	Check Box (flag) selezionate o meno
//

public CompoundFindClause buildFindClauses(Boolean freeSearch) 
{
	CompoundFindClause clauses = getBulkInfo().buildFindClausesFrom(this,freeSearch);

	if(clauses == null) 
		clauses = new it.cnr.jada.persistency.sql.CompoundFindClause();

	if(Boolean.FALSE.equals(freeSearch)) 
		return clauses;	

	clauses.addClause("AND", "fl_pasto", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_pasto());
	clauses.addClause("AND", "fl_alloggio", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_alloggio());
	clauses.addClause("AND", "fl_trasporto", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_trasporto());
	clauses.addClause("AND", "fl_navigazione", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_navigazione());
	clauses.addClause("AND", "fl_vitto_gratuito", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_vitto_gratuito());
	clauses.addClause("AND", "fl_alloggio_gratuito", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_alloggio_gratuito());
	clauses.addClause("AND", "fl_vitto_alloggio_gratuito", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_vitto_alloggio_gratuito());
	
	return clauses;	
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.29.30)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.16.03)
 * @param newOre java.lang.String
 */
public String getDurata_ore() {

	if (getOperatore()!=null && getOre()!=null)
		return getOperatore() + SEPARATORE_DURATA_ORE + getOre();

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 17.03.58)
 * @return int
 */
public java.util.Dictionary getDurata_oreKeys() {
	return DURATA_ORE_KEYS;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.33)
 * @return java.lang.String
 */
public java.lang.String getFlag_spese_gratuite() 
{
	if((getFl_navigazione()==null) && (getFl_vitto_gratuito()==null) && 
	   (getFl_alloggio_gratuito()== null) && (getFl_vitto_alloggio_gratuito()== null))
		return NESSUNO;

	if((!getFl_navigazione().booleanValue()) && (!getFl_vitto_gratuito().booleanValue()) && 
	   (!getFl_alloggio_gratuito().booleanValue()) && (!getFl_vitto_alloggio_gratuito().booleanValue()))
		return NESSUNO;
	
	if(getFl_navigazione().booleanValue())
		return NAVIGAZIONE;
		
	if(getFl_vitto_gratuito().booleanValue())
		return VITTO_GRATUITO;
		
	if(getFl_alloggio_gratuito().booleanValue())
		return ALLOGGIO_GRATUITO;

	if(getFl_vitto_alloggio_gratuito().booleanValue())
		return VITTO_ALLOGGIO_GRATUITO;		
	
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.19)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getFlag_spese_gratuiteKeys() {
	return flag_spese_gratuiteKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.33)
 * @return java.lang.String
 */
public java.lang.String getFlag_tipo_spese() 
{
	if(getFl_pasto()==null && getFl_alloggio()==null && getFl_trasporto()== null)
		return NESSUN_TIPO;

	if(!getFl_pasto().booleanValue() && !getFl_alloggio().booleanValue() && !getFl_alloggio().booleanValue())
		return NESSUN_TIPO;
	
	if(getFl_pasto().booleanValue())
		return PASTO;
		
	if(getFl_alloggio().booleanValue())
		return ALLOGGIO;
		
	if(getFl_trasporto().booleanValue())
		return TRASPORTO;

	return null;

}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.19)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getFlag_tipo_speseKeys() {
	return flag_tipo_speseKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 16.59.31)
 * @return int
 */
public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazione() {
	return nazione;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.23.27)
 * @return java.lang.String
 */
public java.lang.String getOperatore() {
	return operatore;
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 17.03.58)
 * @return int
 */
public java.util.Dictionary getOperatoreDurataOreKeys() {
	return OPERATORE_DURATA_ORE_KEYS;
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 17.03.58)
 * @return int
 */
public java.util.Dictionary getOraDurataOreKeys() {
	return ORA_DURATA_ORE_KEYS;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.16.03)
 * @return java.lang.String
 */
public java.lang.String getOre() {
	return ore;
}
public java.lang.Long getPg_nazione() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = this.getNazione();
	if (nazione == null)
		return null;
	return nazione.getPg_nazione();
}
public java.lang.Long getPg_rif_inquadramento() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rifInquadramento = this.getRifInquadramento();
	if (rifInquadramento == null)
		return null;
	return rifInquadramento.getPg_rif_inquadramento();
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/2002 13.17.29)
 * @return java.util.Collection
 */
public java.util.Collection getRifInquadramenti() {
	return rifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/2002 13.17.00)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk getRifInquadramento() {
	return rifInquadramento;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.53.47)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_area_geograficaKeys() {
	return TipoAreaGeografica.TIPI_AREA_GEOGRAFICA;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	setPercentuale_abbattimento(new java.math.BigDecimal(0));
	setFlag_spese_gratuite(this.NESSUNO);
	setFlag_tipo_spese(this.NESSUN_TIPO);

	return this;
}
public boolean isROFindNazione(){

	if (getTi_area_geografica()==null)
		return false;

	return 
		(getTi_area_geografica().compareTo(TipoAreaGeografica.ITALIA)==0) ||
		(getTi_area_geografica().compareTo(TipoAreaGeografica.INDIFFERENTE)==0);
}
public boolean isRONazione(){

	return getNazione()==null || getNazione().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.30.01)
 * @param newTime java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDataFineValidita) {

	this.setDt_fine_validita(newDataFineValidita);
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.16.03)
 * @param newOre java.lang.String
 */
public void setDurata_ore(java.lang.String durata_ore) {

	super.setDurata_ore(durata_ore);
	if (durata_ore!=null){
		setOperatore(durata_ore.substring(0,1));
		setOre(durata_ore.substring(2));
	}else{
		setOperatore(null);
		setOre(null);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.33)
 * @param newFlag_spese_gratuite java.lang.String
 */
public void setFlag_spese_gratuite(java.lang.String newFlag_spese_gratuite) 
{
	if(newFlag_spese_gratuite.equals(NESSUNO)) 
    {
        setFl_navigazione(new Boolean(false));
        setFl_vitto_gratuito(new Boolean(false));
        setFl_alloggio_gratuito(new Boolean(false));
        setFl_vitto_alloggio_gratuito(new Boolean(false));
    }
    if (newFlag_spese_gratuite.equals(NAVIGAZIONE)) 
    {
        setFl_navigazione(new Boolean(true));
        setFl_vitto_gratuito(new Boolean(false));
        setFl_alloggio_gratuito(new Boolean(false));
        setFl_vitto_alloggio_gratuito(new Boolean(false));
    }
    if (newFlag_spese_gratuite.equals(VITTO_GRATUITO)) 
    {
        setFl_navigazione(new Boolean(false));
        setFl_vitto_gratuito(new Boolean(true));
        setFl_alloggio_gratuito(new Boolean(false));
        setFl_vitto_alloggio_gratuito(new Boolean(false));
    }
    if (newFlag_spese_gratuite.equals(ALLOGGIO_GRATUITO)) 
    {
        setFl_navigazione(new Boolean(false));
        setFl_vitto_gratuito(new Boolean(false));
        setFl_alloggio_gratuito(new Boolean(true));
        setFl_vitto_alloggio_gratuito(new Boolean(false));
    }
    if (newFlag_spese_gratuite.equals(VITTO_ALLOGGIO_GRATUITO)) 
    {
        setFl_navigazione(new Boolean(false));
        setFl_vitto_gratuito(new Boolean(false));
        setFl_alloggio_gratuito(new Boolean(false));
        setFl_vitto_alloggio_gratuito(new Boolean(true));
    }
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 15.40.33)
 * @param newFlag_tipo_spese java.lang.String
 */
public void setFlag_tipo_spese(java.lang.String newFlag_tipo_spese) 
{
	if(newFlag_tipo_spese.equals(NESSUN_TIPO)) 
    {
        setFl_pasto(new Boolean(false));
        setFl_alloggio(new Boolean(false));
        setFl_trasporto(new Boolean(false));
    }
    if (newFlag_tipo_spese.equals(PASTO)) 
    {
        setFl_pasto(new Boolean(true));
        setFl_alloggio(new Boolean(false));
        setFl_trasporto(new Boolean(false));
    }
    if (newFlag_tipo_spese.equals(ALLOGGIO)) 
    {
        setFl_alloggio(new Boolean(true));	    
        setFl_pasto(new Boolean(false));
        setFl_trasporto(new Boolean(false));
    }
    if (newFlag_tipo_spese.equals(TRASPORTO)) 
    {
        setFl_trasporto(new Boolean(true));	    
        setFl_pasto(new Boolean(false));
        setFl_alloggio(new Boolean(false));
    }
}
/**
 * Insert the method's description here.
 * Creation date: (26/11/2001 16.59.31)
 * @param 
 */
public void setNazione(NazioneBulk newNazione) {
	nazione = newNazione;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.23.27)
 * @param newOperatore java.lang.String
 */
public void setOperatore(java.lang.String newOperatore) {
	operatore = newOperatore;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 11.16.03)
 * @param newOre java.lang.String
 */
public void setOre(java.lang.String newOre) {
	ore = newOre;
}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.getRifInquadramento().setPg_rif_inquadramento(pg_rif_inquadramento);
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/2002 13.17.29)
 * @param newRifInquadramenti java.util.Collection
 */
public void setRifInquadramenti(java.util.Collection newRifInquadramenti) {
	rifInquadramenti = newRifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/2002 13.17.00)
 * @param newRifInquadramento it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public void setRifInquadramento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk newRifInquadramento) {
	rifInquadramento = newRifInquadramento;
}
public void validate() throws ValidationException {

	// controllo su campo TI_AREA_GEOGRAFICA
	if (getTi_area_geografica() == null ) 
		throw new ValidationException("Selezionare l'AREA GEOGRAFICA!");

	// controllo su campo NAZIONE
	if (getPg_nazione() == null ) 
		throw new ValidationException("Il campo NAZIONE non può essere vuoto!");

	// controllo su campo INQUADRAMENTO
	if (getPg_rif_inquadramento() == null ) 
		throw new ValidationException("Selezionare l'INQUADRAMENTO!");
		
	// controllo su campo DURATA ORE
	// --> operatore
	if(getDurata_ore() == null)
		throw new ValidationException("Selezionare la DURATA ORE!");
	// --> operatore
	if(getOperatore() == null)
		throw new ValidationException("Selezionare l'OPERATORE per il campo DURATA ORE!");
	// --> ore
	if (getOre() == null )
		throw new ValidationException("Selezionare l'ORA per il campo DURATA ORE!");

	// controllo su campo DATA INIZIO VALIDITA
	if (getDt_inizio_validita() == null )
		throw new ValidationException("Il campo DATA INIZIO VALIDITA non può essere vuoto!");

	// controllo PERCENTUALE
	if (getPercentuale_abbattimento() == null )
		throw new ValidationException("Il campo PERCENTUALE ABBATTIMENTO non può essere vuoto!");
	if (getPercentuale_abbattimento().compareTo(new java.math.BigDecimal(100))>0)
		throw new ValidationException("Il campo PERCENTUALE ABBATTIMENTO non puo' essere superiore a 100!");
	if (getPercentuale_abbattimento().compareTo(new java.math.BigDecimal(0))<0)
		throw new ValidationException("Il campo PERCENTUALE ABBATTIMENTO non puo' essere negativa!");		

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");
}
}
