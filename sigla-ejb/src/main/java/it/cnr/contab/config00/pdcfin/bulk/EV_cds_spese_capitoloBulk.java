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

package it.cnr.contab.config00.pdcfin.bulk;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkMap;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.OrderedHashtable;
/**
 * Elemento_voceBulk che rappresenta i capitoli di spesa del CDS
 */
public class EV_cds_spese_capitoloBulk extends Elemento_voceBulk {

	BulkMap associazioni = new BulkMap();
	OrderedHashtable tipiCds;
	OrderedHashtable funzioni;
public EV_cds_spese_capitoloBulk() {
	super();
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
	setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	setTi_elemento_voce(Elemento_voceHome.TIPO_CAPITOLO);
	setCd_parte(Elemento_voceHome.PARTE_1);
	elemento_padre   = new Elemento_voceBulk();
}
public EV_cds_spese_capitoloBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
/**
 * Gestisce la valorizzazione degli attributi dell'oggetto bulk EV_cds_spese_capitoloBulk e la creazione
 * delle eventuali associazioni fra questo elemento voce e le funzioni e le tipologie dei CDS,
 * a partire dall'interfaccia utente
 * 
 */

public boolean fillFromHttpRequest(javax.servlet.http.HttpServletRequest request,String prefix,int status, FieldValidationMap aFVM) throws FillException {
	boolean dirty = false;
	try
	{
		dirty = super.fillFromHttpRequest( request, prefix,status, aFVM);
		String key, tipo, funzione;
		Ass_ev_funz_tipocdsBulk	ass;
		for (Iterator e = getAllAssVector().iterator();e.hasNext();)
		{
			key = (String) e.next();
			if ( request.getParameter( key ) == null )
			{
				if( ( ass = (Ass_ev_funz_tipocdsBulk) associazioni.get( key)) != null )
				{
					ass.setToBeDeleted();
					associazioni.remove( key );
					dirty = true;
				}	
			}		
			else
			{
				if( ( ass = (Ass_ev_funz_tipocdsBulk) associazioni.get( key)) == null )
				{
					funzione = key.substring(0,2);
					tipo = key.substring(3);
					// ass = new Ass_ev_funz_tipocdsBulk( esercizio, tipo, cd_elemento_voce, funzione);
					ass = new Ass_ev_funz_tipocdsBulk( getCd_elemento_voce(), funzione, tipo, getEsercizio() );
					ass.setUser( getUser() );
					ass.setToBeCreated();
					associazioni.put( key, ass );
					dirty = true;
				}	
			}	

		}

	}
	catch (	Exception e )
	{
		throw new FillException(e);
	}
	return dirty;
}
/**
 * Restituisce il Vettore contenente tutte le possibili combinazioni fra Funzioni e Tipi CDS
 */


public Vector getAllAssVector( ) throws PersistencyException, it.cnr.jada.comp.ApplicationException, IntrospectionException
{
	Vector allAss = new Vector();
	String funzione;
		
	for ( Enumeration funzioni = getFunzioni().keys(); funzioni.hasMoreElements(); )
	{
		funzione = (String) funzioni.nextElement();
		

		for ( Enumeration tipi = getTipiCds().keys(); tipi.hasMoreElements(); )		
		{
			allAss.add( funzione + "-" + (String) tipi.nextElement() );
		}	
	}		
			
//	System.out.println( allAss );		
	return allAss;
}
/**
 * @return it.cnr.jada.bulk.BulkMap
 */
public it.cnr.jada.bulk.BulkMap getAssociazioni() {
	return associazioni;
}
/**
 * Restituisce la collezione di Ass_ev_funz_tipoCdsbulk associata al capitolo di spese del cds per 
 * renderla persistente contestualmente alla gestione della persistenza del capitolo di spesa del cds
 */

public BulkCollection[] getBulkLists() {
	return new it.cnr.jada.bulk.BulkCollection[] { 
			associazioni };
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable getFunzioni() {
	return funzioni;
}
/**
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable getTipiCds() {
	return tipiCds;
}
/**
 * Inizializza l'oggetto Bulk
 * @param bp business process corrente
 * @param context contesto dell'Action che e' stata generata
 * @return OggettoBulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert(bp, context);
	setFl_limite_ass_obblig(new Boolean(true));
	setFl_voce_personale( new Boolean(false));
	setFl_partita_giro( new Boolean(false));
	return this;
}
/**
 * @param newAssociazioni it.cnr.jada.bulk.BulkMap
 */
public void setAssociazioni(it.cnr.jada.bulk.BulkMap newAssociazioni) {
	associazioni = newAssociazioni;
}
/**
 * @param newFunzioni it.cnr.jada.util.OrderedHashtable
 */
public void setFunzioni(it.cnr.jada.util.OrderedHashtable newFunzioni) {
	funzioni = newFunzioni;
}
/**
 * @param newTipiCds it.cnr.jada.util.OrderedHashtable
 */
public void setTipiCds(it.cnr.jada.util.OrderedHashtable newTipiCds) {
	tipiCds = newTipiCds;
}

private void writeCheckBox(javax.servlet.jsp.JspWriter out, String key, boolean value ) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
{

	try 
	{
		out.print("<input type=\"checkbox\" name=\"");
		out.print( key );
//		out.print('"');
		out.print("\" value=\"true\"");
		if (value )
			out.print(" checked");
		out.print(">");
	} catch(Exception e) {
		out.print("&nbsp;");
	}

	
}
/**
 * Genera l'HTML per disegnare nell'interfaccia utente uan tabella che presenta nelle righe le funzioni e nelle
 * colonne  i tipi CDS; inoltre, se il modello passato come parametro e' valorizzato, inizializza le celle
 * di questa tabella con i dati presenti nel modello.
 */

public void writeTable(javax.servlet.jsp.JspWriter out, Map model ) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
{
	out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
	int columnsNr = getTipiCds().size();
	writeTableHeader( out, columnsNr );
	java.util.Iterator i = getAllAssVector().iterator();
	
	while (  i.hasNext() )
	{
		writeTableRow( out, i , model, columnsNr );

	}	
	out.println("</table>");
}
private void writeTableHeader(javax.servlet.jsp.JspWriter out, int columnNr) throws java.io.IOException, PersistencyException, IntrospectionException, it.cnr.jada.comp.ApplicationException 
{
	//title
	out.println("<tr><td colspan=");
	out.println( (new Integer(columnNr + 1)).toString() );
	out.println("><h3><CENTER>Piano dei conti Economico Finanziario</CENTER></h3></td>");
	//column header
	out.print("<tr>");
	out.print("<td></td>");
	java.util.Enumeration e = getTipiCds().keys();
	while (  e.hasMoreElements() )
	{
		out.print("<td><CENTER><h3>");
		out.print( (String) e.nextElement() );
		out.print("</h3></CENTER></td>");		
	}	
	out.println("</tr>");

}
private void writeTableRow(javax.servlet.jsp.JspWriter out, java.util.Iterator i, Map model, int columnsNr ) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
{
	String key, funzione, tipo;
	
	for ( int j = 0 ; j < columnsNr ; j ++ )
	{
		key = (String) i.next();
		funzione = key.substring(0,2);
		tipo = key.substring(3);
		
		if ( j == 0 )
		{
			out.print("<tr>");	
			out.print("<td><h4>");
			out.print( (String) getFunzioni().get( funzione) );
			out.print("</h4></td>");
		}	
			
	    out.print("<td><CENTER>");
		if ( model != null &&  model.get( key ) != null )
			    writeCheckBox( out, key, true );
		else	    
			    writeCheckBox( out, key, false );

	    out.print("</CENTER></td>");
		if ( j == columnsNr - 1 )
			out.println("</tr>");	
	}	
}
}
