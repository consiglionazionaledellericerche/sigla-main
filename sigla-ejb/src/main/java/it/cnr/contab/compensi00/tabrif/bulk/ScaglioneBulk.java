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

package it.cnr.contab.compensi00.tabrif.bulk;

import java.util.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

public class ScaglioneBulk extends ScaglioneBase 
{

	public final static Dictionary TI_ANAGRAFICO;
	public final static Dictionary TI_ENTE_PERCIPIENTE;
	public final static String INDIFFERENTE = "*";
	public final static String DIPENDENTE = "D";
	public final static String ALTRO = "A";
	public final static String ENTE = "E";
	public final static String PERCIPIENTE = "P";
	
	static
	{
		TI_ANAGRAFICO = new OrderedHashtable();
		TI_ANAGRAFICO.put(INDIFFERENTE, "Entrambi");
		TI_ANAGRAFICO.put(DIPENDENTE, "Dipendente");
		TI_ANAGRAFICO.put(ALTRO, "Altro");		
	}
	static
	{
		TI_ENTE_PERCIPIENTE = new OrderedHashtable();
		TI_ENTE_PERCIPIENTE.put(INDIFFERENTE, "Entrambi");
		TI_ENTE_PERCIPIENTE.put(ENTE, "Ente");
		TI_ENTE_PERCIPIENTE.put(PERCIPIENTE, "Percipiente");		
	}	

	private ProvinciaBulk provincia = new ProvinciaBulk();
	private RegioneBulk regione = new RegioneBulk();
	private ComuneBulk comune = new ComuneBulk();
	private Tipo_contributo_ritenutaBulk contributo_ritenuta = new Tipo_contributo_ritenutaBulk();
	private java.util.List scaglioni;
	public final static java.math.BigDecimal IMPORTO_MASSIMO = new java.math.BigDecimal(999999999);
public ScaglioneBulk() {
	super();
}
public ScaglioneBulk(java.lang.String cd_contributo_ritenuta,java.lang.String cd_provincia,java.lang.String cd_regione,java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Long pg_comune,java.lang.String ti_anagrafico,java.lang.String ti_ente_percipiente) {
	super(cd_contributo_ritenuta,cd_provincia,cd_regione,dt_inizio_validita,im_inferiore,pg_comune,ti_anagrafico,ti_ente_percipiente);
	setProvincia(new it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk(cd_provincia));
	setRegione(new it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk(cd_regione));
	setComune(new it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk(pg_comune));
	setContributo_ritenuta(new Tipo_contributo_ritenutaBulk(cd_contributo_ritenuta, dt_inizio_validita));
}
public java.lang.String getCd_contributo_ritenuta() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk contributo_ritenuta = this.getContributo_ritenuta();
	if (contributo_ritenuta == null)
		return null;
	return contributo_ritenuta.getCd_contributo_ritenuta();
}
public java.lang.String getCd_provincia() {
	it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk provincia = this.getProvincia();
	if (provincia == null)
		return null;
	return provincia.getCd_provincia();
}
public java.lang.String getCd_regione() {
	it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk regione = this.getRegione();
	if (regione == null)
		return null;
	return regione.getCd_regione();
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 9.25.01)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune() {
	return comune;
}
/**
 * Insert the method's description here.
 * Creation date: (14/01/2002 14.34.52)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk
 */
public Tipo_contributo_ritenutaBulk getContributo_ritenuta() {
	return contributo_ritenuta;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.00.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
public java.lang.Long getPg_comune() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = this.getComune();
	if (comune == null)
		return null;
	return comune.getPg_comune();
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 9.25.01)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk getProvincia() {
	return provincia;
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 13.50.05)
 * @return it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk getRegione() {
	return regione;
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.27.43)
 * @return java.util.List
 */
public java.util.List getScaglioni() {
	if (scaglioni==null)
		scaglioni = new LinkedList();
	return scaglioni;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 17.06.35)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_anagraficoKeys() {
	return TI_ANAGRAFICO;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2002 9.16.43)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_entePercipienteKeys() {
	return TI_ENTE_PERCIPIENTE;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);
	setTi_anagrafico(INDIFFERENTE);
	setTi_ente_percipiente(INDIFFERENTE);
	resetImporti();
	
	return this;
	
}
public boolean isROComune() 
{
	return comune == null || comune.getCrudStatus() == NORMAL;
}
public boolean isROContributo_ritenuta() 
{
	return contributo_ritenuta == null || contributo_ritenuta.getCrudStatus() == NORMAL;
}
public boolean isROProvincia() 
{
	return provincia == null || provincia.getCrudStatus() == NORMAL;
}
public boolean isRORegione() 
{
	return regione == null || regione.getCrudStatus() == NORMAL;
}
public BulkList ordinaScaglioni( BulkList scaglioni ) 
{
	// riordino in modo crescente la lista scaglioni per importo inferiore
	
	Collections.sort(scaglioni,new Comparator() {	

		public int compare(Object o1, Object o2) 
		{
			ScaglioneBulk os1 = (ScaglioneBulk) o1;
			ScaglioneBulk os2 = (ScaglioneBulk) o2;

			return os1.getIm_inferiore().compareTo( os2.getIm_inferiore());
		}
		public boolean equals(Object o)  
		{
			return (getIm_inferiore() == ((ScaglioneBulk)o).getIm_inferiore());
		}
	});

	return scaglioni;
}
public void resetImporti(){
	
	setIm_inferiore(new java.math.BigDecimal(0));
	setIm_superiore(null);
//	setAliquota(new java.math.BigDecimal(0));
	setBase_calcolo(new Integer(100));
}
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.getContributo_ritenuta().setCd_contributo_ritenuta(cd_contributo_ritenuta);
}
public void setCd_provincia(java.lang.String cd_provincia) {
	this.getProvincia().setCd_provincia(cd_provincia);
}
public void setCd_regione(java.lang.String cd_regione) {
	this.getRegione().setCd_regione(cd_regione);
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 9.25.01)
 * @param newComune it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public void setComune(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune) {
	comune = newComune;
}
/**
 * Insert the method's description here.
 * Creation date: (14/01/2002 14.34.52)
 * @param newContributo_ritenuta it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk
 */
public void setContributo_ritenuta(Tipo_contributo_ritenutaBulk newContributo_ritenuta) {
	contributo_ritenuta = newContributo_ritenuta;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.01.09)
 * @param newData java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newData) {

	this.setDt_fine_validita(newData);
}
public void setPg_comune(java.lang.Long pg_comune) {
	this.getComune().setPg_comune(pg_comune);
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 9.25.01)
 * @param newProvincia it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk
 */
public void setProvincia(it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk newProvincia) {
	provincia = newProvincia;
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 13.50.05)
 * @param newARegione it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
 */
public void setRegione(it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk newRegione) {
	regione = newRegione;
}
/**
 * Insert the method's description here.
 * Creation date: (07/06/2002 13.27.43)
 * @param newScaglioni java.util.List
 */
public void setScaglioni(java.util.List newScaglioni) {
	scaglioni = newScaglioni;
}
public void validaDate() throws ValidationException{

	if (getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA deve essere valorizzato" );
	if (getDt_fine_validita()==null)
		setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);

	if (getDt_inizio_validita().compareTo(getDt_fine_validita()) > 0)
		throw new ValidationException("La Data Inizio Validita non può essere superiore alla Data Fine Validita");
}
public void validate() throws ValidationException 
{
	// controllo su campo CONTRIBUTO RITENUTA
	if ((getCd_contributo_ritenuta() == null ) || (getCd_contributo_ritenuta().trim().length() == 0))
		throw new ValidationException( "Il campo CONTRIBUTO RITENUTA deve essere valorizzato!" );

	// controllo su campo REGIONE
	if ((getCd_regione() == null ) || (getCd_regione().trim().length() == 0))
		throw new ValidationException( "Il campo REGIONE deve essere valorizzato" );

	// controllo su campo PROVINIA
	if ((getCd_provincia() == null ) || (getCd_provincia().trim().length() == 0))
		throw new ValidationException( "Il campo PROVINCIA deve essere valorizzato" );

	// controllo su campo COMUNE
	if (getPg_comune() == null ) 
		throw new ValidationException( "Il campo COMUNE deve essere valorizzato" );

	// controllo su campo TIPO ANAGRAFICO
	if (getTi_anagrafico() == null ) 
		throw new ValidationException( "Il campo TIPO ANAGRAFICO deve essere valorizzato!" );		

	// controllo sui campi DATA
	validaDate();
		
	// controllo su campo TIPO ENTE PERCIPIENTE
	if (getTi_ente_percipiente() == null ) 
		throw new ValidationException( "Il campo TIPO ENTE PERCIPIENTE deve essere valorizzato" );										
			
	// controllo su campo IMPORTO INFERIORE
	if ((getIm_inferiore() == null ) || (getIm_inferiore().compareTo( new java.math.BigDecimal(0)) < 0))
		throw new ValidationException( "Il campo IMPORTO INFERIORE deve essere un valore positivo !" );
		
	// controllo su campo IMPORTO SUPERIORE
	if (getIm_superiore() == null)
		setIm_superiore(IMPORTO_MASSIMO);
	if ( (getIm_superiore() == null) || (getIm_superiore().compareTo( new java.math.BigDecimal(0)) < 0))
		throw new ValidationException( "Il campo IMPORTO SUPERIORE deve essere maggiore di 0 !" );

	// controllo IMPORTO SUPERIORE > IMPORTO INFERIORE	
	if (getIm_inferiore().compareTo(getIm_superiore()) >= 0)
		throw new ValidationException( "L'importo superiore deve essere maggiore dell'importo inferiore!" );				
		
	// controllo sull' ALIQUOTA
	if(getAliquota() == null)
		throw new ValidationException( "Il campo ALIQUOTA deve essere valorizzato" );

	if ((getAliquota().compareTo( new java.math.BigDecimal(0)) < 0) ||
	   (getAliquota().compareTo( new java.math.BigDecimal(100)) > 0))
		throw new ValidationException( "L' ALIQUOTA deve essere compresa tra 0 e 100" );

	// controllo sulla BASE di CALCOLO
	if((getBase_calcolo() == null) ||
	   (getBase_calcolo().compareTo( new Integer(1)) < 0) ||
	   (getBase_calcolo().compareTo( new Integer(100)) > 0))
		throw new ValidationException( "La percentuale BASE di CALCOLO deve essere compresa tra 1 e 100" );			
}		
}
