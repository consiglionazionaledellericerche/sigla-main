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

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tipo_spesaBulk extends Missione_tipo_spesaBase 
{
	private DivisaBulk divisa = new DivisaBulk();
	private java.util.Collection rifInquadramenti;			
	private Rif_inquadramentoBulk rifInquadramento;
	private NazioneBulk nazione = new NazioneBulk();

	/**** Gestione flag pasto, alloggio, trasporto, rimborso Km ***/
	public final static String NESSUN_TIPO = "X";	
	public final static String PASTO = "P";
	public final static String ALLOGGIO = "A";
	public final static String TRASPORTO = "T";
	public final static String RIMBORSO_KM = "R";
	public final static java.util.Dictionary flag_tipo_speseKeys;
	private String flag_tipo_spese;	
	static
	{
		flag_tipo_speseKeys = new it.cnr.jada.util.OrderedHashtable();
		flag_tipo_speseKeys.put(NESSUN_TIPO, "Nessuno");		
		flag_tipo_speseKeys.put(PASTO, "Pasto");
		flag_tipo_speseKeys.put(ALLOGGIO, "Alloggio");
		flag_tipo_speseKeys.put(TRASPORTO, "Trasporto");
		flag_tipo_speseKeys.put(RIMBORSO_KM, "Rimborso Km");		
	}		
public Missione_tipo_spesaBulk() {
	super();
}
public Missione_tipo_spesaBulk(java.lang.String cd_ti_spesa,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica) {
	super(cd_ti_spesa,dt_inizio_validita,pg_nazione,pg_rif_inquadramento,ti_area_geografica);
	setRifInquadramento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk(pg_rif_inquadramento));
	setNazione(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk(pg_nazione));
}
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

	// Solo per la ricerca libera e non per quella guidata
	clauses.addClause("AND", "fl_pasto", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_pasto());
	clauses.addClause("AND", "fl_alloggio", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_alloggio());
	clauses.addClause("AND", "fl_trasporto", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_trasporto());
	clauses.addClause("AND", "fl_giustificativo_richiesto", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_giustificativo_richiesto());
	clauses.addClause("AND", "fl_rimborso_km", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, getFl_rimborso_km());

	return clauses;	
}

public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.29.30)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {
	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (15/11/2001 18.06.45)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (22/10/2002 10.44.19)
 * @return java.lang.String
 */
public java.lang.String getFlag_tipo_spese() 
{
	if(getFl_pasto()==null && getFl_alloggio()==null && getFl_trasporto()== null &&
	   getFl_rimborso_km()==null)
		return NESSUN_TIPO;

	if(!getFl_pasto().booleanValue() && !getFl_alloggio().booleanValue() && 
	   !getFl_trasporto().booleanValue() && !getFl_rimborso_km().booleanValue())
		return NESSUN_TIPO;
	
	if(getFl_pasto().booleanValue())
		return PASTO;
		
	if(getFl_alloggio().booleanValue())
		return ALLOGGIO;
		
	if(getFl_trasporto().booleanValue())
		return TRASPORTO;
		
	if(getFl_rimborso_km().booleanValue())
		return RIMBORSO_KM;		

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (22/10/2002 10.44.40)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getFlag_tipo_speseKeys() {
	return flag_tipo_speseKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.41.11)
 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public NazioneBulk getNazione() {
	return nazione;
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
 * Creation date: (12/02/2002 12.35.28)
 * @return java.util.Collection
 */
public java.util.Collection getRifInquadramenti() {
	return rifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.05.56)
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
	setFl_giustificativo_richiesto(Boolean.FALSE);	
	setFlag_tipo_spese(this.NESSUN_TIPO);
	resetImporti();

	return this;
}
public boolean isRODivisa() {

	return getDivisa() == null || getDivisa().getCrudStatus() == NORMAL;
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
 * Creation date: (18/01/2002 14.25.56)
 */
private void resetImporti() {
	
	setLimite_max_spesa(new java.math.BigDecimal(0));
	setPercentuale_maggiorazione(new java.math.BigDecimal(0));
}
public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
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
 * Creation date: (15/11/2001 18.06.45)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (22/10/2002 10.44.19)
 * @param newFlag_tipo_spese java.lang.String
 */
public void setFlag_tipo_spese(java.lang.String newFlag_tipo_spese) 
{
	if(newFlag_tipo_spese.equals(NESSUN_TIPO)) 
    {
        setFl_pasto(new Boolean(false));
        setFl_alloggio(new Boolean(false));
        setFl_trasporto(new Boolean(false));
        setFl_rimborso_km(new Boolean(false));
    }
    if (newFlag_tipo_spese.equals(PASTO)) 
    {
        setFl_pasto(new Boolean(true));
        setFl_alloggio(new Boolean(false));
        setFl_trasporto(new Boolean(false));
        setFl_rimborso_km(new Boolean(false));        
    }
    if (newFlag_tipo_spese.equals(ALLOGGIO)) 
    {
        setFl_pasto(new Boolean(false));	    
        setFl_alloggio(new Boolean(true));	    
        setFl_trasporto(new Boolean(false));
        setFl_rimborso_km(new Boolean(false));        
    }
    if (newFlag_tipo_spese.equals(TRASPORTO)) 
    {
        setFl_pasto(new Boolean(false));
        setFl_alloggio(new Boolean(false));	    
        setFl_trasporto(new Boolean(true));	    
        setFl_rimborso_km(new Boolean(false));        
    }
    if (newFlag_tipo_spese.equals(RIMBORSO_KM)) 
    {
        setFl_pasto(new Boolean(false));
        setFl_trasporto(new Boolean(false));	            
        setFl_alloggio(new Boolean(false));	    
        setFl_rimborso_km(new Boolean(true));        	    
    }    
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.41.11)
 * @param newNazione it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione(NazioneBulk newNazione) {
	nazione = newNazione;
}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.getRifInquadramento().setPg_rif_inquadramento(pg_rif_inquadramento);
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.35.28)
 * @param newRifInquadramenti java.util.Collection
 */
public void setRifInquadramenti(java.util.Collection newRifInquadramenti) {
	rifInquadramenti = newRifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.05.56)
 * @param newRifInquadramento it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public void setRifInquadramento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk newRifInquadramento) {
	rifInquadramento = newRifInquadramento;
}
public void validate() throws ValidationException {

	// controllo su campo CODICE
	if ( getCd_ti_spesa() == null)
		throw new ValidationException( "Il campo CODICE non può essere vuoto!" );

	// controllo su campo DESCRIZIONE
	if (getDs_ti_spesa() == null)
		throw new ValidationException( "Il campo DESCRIZIONE non può essere vuoto!" );

	// controllo su campo INQUADRAMENTO
	if (getPg_rif_inquadramento() == null ) 
		throw new ValidationException( "Selezionare l'INQUADRAMENTO!" );

	// controllo su campo TI_AREA_GEOGRAFICA
	if (getTi_area_geografica() == null ) 
		throw new ValidationException( "Selezionare l'AREA GEOGRAFICA!" );

	// controllo su campo NAZIONE
	if (getPg_nazione() == null ) 
		throw new ValidationException( "Il campo NAZIONE non può essere vuoto!" );

	// controllo su campo DIVISA
	if (getCd_divisa() == null)
		throw new ValidationException( "Il campo VALUTA non può essere vuoto!" );

	// controllo su campo DATA INIZIO VALIDITA
	if (getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto!" );

	// controllo su campo LIMITE di SPESA
	if ( getLimite_max_spesa() == null )
		throw new ValidationException( "Il campo LIMITE di SPESA non può essere vuoto!" );
	if (getLimite_max_spesa().compareTo( new java.math.BigDecimal(0))<=0)
		throw new ValidationException( "Il campo LIMITE di SPESA deve essere maggiore di 0!" );

	// Non occorre mettere il controllo sulla PERCENTUALE di MAGGIORAZIONE
	// perche' puo' valere anche + di 100%
	if (getPercentuale_maggiorazione()==null)
		throw new ValidationException( "Il campo PERCENTUALE di MAGGIORAZIONE non può essere vuoto!" );
	if (getPercentuale_maggiorazione().compareTo(new java.math.BigDecimal(0))<0)
		throw new ValidationException( "Il campo PERCENTUALE di MAGGIORAZIONE deve essere maggiore o uguale a 0!" );

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");
}
}
