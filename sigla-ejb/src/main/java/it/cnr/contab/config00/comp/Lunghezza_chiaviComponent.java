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

package it.cnr.contab.config00.comp;

import java.io.Serializable;

import it.cnr.contab.config00.sto.bulk.Lunghezza_chiaviBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Classe che fornisce servizi di formattazione della chiave di alcune entita' quali Cds, Unita organizzativa, Cdr,
 * Capoconto, Conto, Linea attivita', etc.
 */


public class Lunghezza_chiaviComponent extends GenericComponent implements ILunghezza_chiaviMgr, java.io.Serializable, Cloneable {



	static final String  VOCE_EP_TABELLA = "VOCE_EP";
	static final String  VOCE_EP_CD_COLONNA = "CD_VOCE_EP";
	static final Integer LIVELLO_CAPOCONTO = new Integer(2);
	static final Integer LIVELLO_CONTO = new Integer(3);	

	static final String CDS_TABELLA =  "UNITA_ORGANIZZATIVA" ;
	static final String CDS_CD_COLONNA =  "CD_UNITA_ORGANIZZATIVA" ;	
	static final String UO_TABELLA =  "UNITA_ORGANIZZATIVA" ;
	static final String UO_CD_COLONNA =  "CD_PROPRIO_UNITA" ;
	static final String CDR_TABELLA =  "CDR" ;
	static final String CDR_CD_COLONNA =  "CD_CENTRO_RESPONSABILITA" ;
	static final String LATT_TABELLA = "LINEA_ATTIVITA";
	static final String LATT_CD_COLONNA = "CD_LINEA_ATTIVITA";

//@@<< CONSTRUCTORCST
	public  Lunghezza_chiaviComponent()
	{
//>>

//<< CONSTRUCTORCSTL
		/*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

	}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      Estrae da una chiave completa di una organizzazione i primi n caratteri che corrispondono al codice CDS
 */

public String extractCdsKey( UserContext userContext,String key ) throws it.cnr.jada.comp.ComponentException
{
	java.math.BigDecimal len = getLunghezza( userContext, CDS_TABELLA, Constants.LIVELLO_CDS, CDS_CD_COLONNA );
	return key.substring(0, len.intValue() - 1);
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      estrae da una chiave completa di una organizzazione gli n caratteri che corrispondono al codice UO
 */

public String extractUoKey( UserContext userContext,String key ) throws it.cnr.jada.comp.ComponentException
{
	java.math.BigDecimal lenCds = getLunghezza( userContext,  CDS_TABELLA, Constants.LIVELLO_CDS, CDS_CD_COLONNA );
	java.math.BigDecimal lenUo = getLunghezza( userContext,  UO_TABELLA, Constants.LIVELLO_UO, UO_CD_COLONNA );
	return key.substring(lenCds.intValue() + 1, lenCds.intValue() + 1 + lenUo.intValue() );
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Capoconto
 */

public String formatCapocontoKey( UserContext userContext,String key, Integer esercizio ) throws it.cnr.jada.comp.ComponentException
{
	if ( key == null ||  key.length() == 0 || esercizio == null)
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, esercizio, VOCE_EP_TABELLA, LIVELLO_CAPOCONTO, VOCE_EP_CD_COLONNA );
	if ( key.length() > len.intValue() )
		throw handleException( new ApplicationException( "Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita" ));	
	return leftPadding(key, len.intValue());
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Cdr
 */

public String formatCdrKey( UserContext userContext,String key, Integer livello ) throws it.cnr.jada.comp.ComponentException
{
	if ( key == null ||  key.length() == 0 )
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, CDR_TABELLA, livello, CDR_CD_COLONNA );
	if ( key.length() > len.intValue() )
		throw handleException( new ApplicationException( "Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita" ));	
	return leftPadding(key, len.intValue());
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Cds
 */

public String formatCdsKey( UserContext userContext,String key) throws  it.cnr.jada.comp.ComponentException
{
	if ( key == null || key.length() == 0 )
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, CDS_TABELLA, Constants.LIVELLO_CDS, CDS_CD_COLONNA );
	if ( key.length() > len.intValue() )
		throw new ApplicationException( "Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita" );
	return leftPadding(key, len.intValue());
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un CONTO
 */

public String formatContoKey( UserContext userContext,String key, Integer esercizio ) throws  it.cnr.jada.comp.ComponentException
{
	if ( key == null ||  key.length() == 0 || esercizio == null)
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, esercizio, VOCE_EP_TABELLA, LIVELLO_CONTO, VOCE_EP_CD_COLONNA );
	if ( key.length() > len.intValue() )
		throw handleException( new ApplicationException( "Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita" ));	
	return leftPadding(key, len.intValue());
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta una generica chiave
 */

public String formatKey( UserContext userContext,String key, Integer esercizio, String tabella, Integer livello, String attributo ) throws it.cnr.jada.comp.ComponentException
{
	if ( key == null || key.length() == 0 || esercizio == null)
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, esercizio, tabella, livello, attributo );
	return leftPadding( key, len.intValue() );
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di una LINEA ATTIVITA'
 */
public String formatLinea_attivitaKey(UserContext userContext,String key) throws it.cnr.jada.comp.ComponentException
{
	if (key == null || key.length() == 0 )
		return key;
	java.math.BigDecimal len = getLunghezza( userContext,LATT_TABELLA,new Integer(1),LATT_CD_COLONNA);
	if (key.length() > len.intValue())
		throw handleException(new ApplicationException("Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita"));	
	return leftPadding(key,len.intValue());
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di una Unita Organizzativa
 */
public String formatUoKey( UserContext userContext,String key ) throws  it.cnr.jada.comp.ComponentException
{
	if ( key == null ||  key.length() == 0 )
		return key;
	java.math.BigDecimal len = getLunghezza( userContext, UO_TABELLA, Constants.LIVELLO_UO, UO_CD_COLONNA );
	if ( key.length() > len.intValue() )
		throw handleException( new ApplicationException( "Il codice non può essere più lungo di " + len.toString() + " caratteri oppure numerazione automatica esaurita" ));	
	return leftPadding(key, len.intValue());
}
/**
 * @return com.ibm.math.BigDecimal
 * @param userContext 
 * @param esercizio com.ibm.math.BigDecimal
 * @param tabella java.lang.String
 * @param livello com.ibm.math.BigDecimal
 * @param attributo java.lang.String
 */
private java.math.BigDecimal getLunghezza( UserContext userContext,Integer esercizio, String tabella, Integer livello, String attributo) throws it.cnr.jada.comp.ComponentException
{
	try 
	{
	    
	    Lunghezza_chiaviBulk bulk = new Lunghezza_chiaviBulk();
	    bulk.setEsercizio( esercizio ) ;
	    bulk.setTabella( tabella ) ;	
	    bulk.setLivello( livello ) ;
	    bulk.setAttributo( attributo ) ;

		Lunghezza_chiaviBulk result = (Lunghezza_chiaviBulk) getHomeCache(userContext).getHome( bulk.getClass()).findByPrimaryKey(bulk);
	
	    return result.getLunghezza();
	    	
			
	} catch (Throwable e) {
		throw handleException( e );
	} 
}
/**
 * @return com.ibm.math.BigDecimal
 * @param userContext 
 * @param tabella java.lang.String
 * @param livello com.ibm.math.BigDecimal
 * @param attributo java.lang.String
 */
private java.math.BigDecimal getLunghezza( UserContext userContext, String tabella, Integer livello, String attributo) throws it.cnr.jada.comp.ComponentException
{
 return getLunghezza(userContext,new Integer(0), tabella, livello, attributo);
}
/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      aggiunge caratteri '0' a sinistra di un astringa fino a raggiungere la lunghezza specificata
 */
public String leftPadding( String key, int keyLen )
{
	int valueLen = key.length();

	String newKey = new String();

	for ( int i = 0; i < (keyLen - valueLen); i++ )
		newKey = newKey.concat("0");
	newKey = newKey.concat( key );
	return newKey;
}
}
