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

package it.cnr.contab.docamm00.consultazioni.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;


import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.DateUtils;

/**
 * @author mdurso
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MonitoCococoComponent extends CRUDComponent {

	public java.util.List findCompensi(UserContext userContext, Monito_cococoBulk lancio_monito) throws PersistencyException, IntrospectionException, ComponentException
	{
		CompensoHome compensoHome = (CompensoHome)getHome(userContext, CompensoBulk.class);
		SQLBuilder sql = compensoHome.createSQLBuilder();
		
		sql.addSQLClause("AND","COMPENSO.CD_TIPO_RAPPORTO", sql.EQUALS, "COLL");
		sql.addSQLClause("AND","COMPENSO.STATO_COFI", sql.NOT_EQUALS, "A");
		sql.addSQLClause("AND","COMPENSO.STATO_COFI", sql.NOT_EQUALS, "I");
		sql.openParenthesis( "AND");
		sql.addSQLClause("AND","(COMPENSO.DT_DA_COMPETENZA_COGE", sql.GREATER_EQUALS, lancio_monito.getDt_da_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_A_COMPETENZA_COGE", sql.LESS_EQUALS, lancio_monito.getDt_a_competenza_coge());
		sql.closeParenthesis();
				
		sql.addSQLClause("OR","(COMPENSO.DT_DA_COMPETENZA_COGE", sql.LESS_EQUALS, lancio_monito.getDt_da_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_A_COMPETENZA_COGE", sql.GREATER_EQUALS, lancio_monito.getDt_a_competenza_coge());
		sql.closeParenthesis();
		
		sql.addSQLClause("OR","(COMPENSO.DT_DA_COMPETENZA_COGE", sql.LESS_EQUALS, lancio_monito.getDt_da_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_A_COMPETENZA_COGE", sql.GREATER_EQUALS, lancio_monito.getDt_da_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_A_COMPETENZA_COGE", sql.LESS_EQUALS, lancio_monito.getDt_a_competenza_coge());
		sql.closeParenthesis();
		
		sql.addSQLClause("OR","(COMPENSO.DT_DA_COMPETENZA_COGE", sql.GREATER_EQUALS, lancio_monito.getDt_da_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_DA_COMPETENZA_COGE", sql.LESS_EQUALS, lancio_monito.getDt_a_competenza_coge());
		sql.addSQLClause("AND","COMPENSO.DT_A_COMPETENZA_COGE", sql.GREATER_EQUALS, lancio_monito.getDt_a_competenza_coge());
		sql.closeParenthesis();
		sql.closeParenthesis();
		
		if (lancio_monito.getAttivita().equals(new String("1")))
			{sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM OBBLIGAZIONE_SCAD_VOCE O, LINEA_ATTIVITA L" +
		                       " WHERE O.CD_CDS = COMPENSO.CD_CDS_OBBLIGAZIONE" +
							   " AND O.ESERCIZIO = COMPENSO.ESERCIZIO_OBBLIGAZIONE" +
							   " AND O.ESERCIZIO_ORIGINALE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE" +
		                       " AND O.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE" +
							   " AND O.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO" +
							   " AND O.CD_LINEA_ATTIVITA = L.CD_LINEA_ATTIVITA" +
		                       " AND O.CD_CENTRO_RESPONSABILITA = L.CD_CENTRO_RESPONSABILITA" +
		                       " AND L.CD_NATURA ", sql.EQUALS, lancio_monito.getAttivita());
			}
		else
			{
			sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM OBBLIGAZIONE_SCAD_VOCE O, LINEA_ATTIVITA L" +
				" WHERE O.CD_CDS = COMPENSO.CD_CDS_OBBLIGAZIONE" +
				" AND O.ESERCIZIO = COMPENSO.ESERCIZIO_OBBLIGAZIONE" +
				" AND O.ESERCIZIO_ORIGINALE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE" +
				" AND O.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE" +
				" AND O.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO" +
				" AND O.CD_LINEA_ATTIVITA = L.CD_LINEA_ATTIVITA" +
				" AND O.CD_CENTRO_RESPONSABILITA = L.CD_CENTRO_RESPONSABILITA" +
				" AND L.CD_NATURA ", sql.NOT_EQUALS, "1");
			sql.closeParenthesis();	
			sql.addSQLClause("AND","NOT EXISTS ( SELECT 1 FROM OBBLIGAZIONE_SCAD_VOCE O, LINEA_ATTIVITA L" +
								   " WHERE O.CD_CDS = COMPENSO.CD_CDS_OBBLIGAZIONE" +
								   " AND O.ESERCIZIO = COMPENSO.ESERCIZIO_OBBLIGAZIONE" +
								   " AND O.ESERCIZIO_ORIGINALE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE" +
								   " AND O.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE" +
								   " AND O.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO" +
								   " AND O.CD_LINEA_ATTIVITA = L.CD_LINEA_ATTIVITA" +
								   " AND O.CD_CENTRO_RESPONSABILITA = L.CD_CENTRO_RESPONSABILITA" +
								   " AND L.CD_NATURA ", sql.EQUALS, "1");		                       
			}	                       
		sql.closeParenthesis();			
		sql.addOrderBy("CD_TERZO");
		return compensoHome.fetchAll(sql);
	}
	
	public void inserisciRighe(UserContext userContext, Monito_cococoBulk lancio_monito) throws ComponentException{
				
		java.util.List listaCompensi;
		java.util.List listaRighe;
		Integer terzo=new Integer(0);
		String denominazione = new String("");
		int seq = 0;
		long conta_giorni_terzo=0; 
		long divisore=0;
	 	BigDecimal tot_compensi_terzo=new BigDecimal(0);
		BigDecimal tot_lordo_perc_terzo=new BigDecimal(0);
		BigDecimal tot_netto_perc_terzo=new BigDecimal(0);
		BigDecimal tot_carico_perc_terzo=new BigDecimal(0); 
		BigDecimal tot_carico_ente_terzo=new BigDecimal(0);
		BigDecimal tot_compensi_periodo_terzo=new BigDecimal(0);
		BigDecimal tot_compensi_periodo_compenso=new BigDecimal(0);
		
		long conta_giorni_compenso=0;
		int moltiplicatore_importi=0;
			 
		//ricavo il progressivo unico id_report
		java.math.BigDecimal prog= getSequence(userContext);

		//cancello tutte le righe che hanno id_report diverso dal nuovo
		try {
			listaRighe = findRighe(userContext,prog);
		} catch (PersistencyException e1) {
			throw new ComponentException(e1);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} 
		for (java.util.ListIterator i = listaRighe.listIterator();i.hasNext();) {
					  Monito_cococoBulk monito = (Monito_cococoBulk)i.next();
					  monito.setToBeDeleted();
					  super.eliminaConBulk (userContext,monito);	
		}			  		 
		try {

			
			listaCompensi = findCompensi(userContext,lancio_monito);
			for (java.util.ListIterator i = listaCompensi.listIterator();i.hasNext();) {
			  CompensoBulk compenso = (CompensoBulk)i.next();
			  
			  if (!terzo.equals(new Integer(0)) && !terzo.equals(compenso.getCd_terzo()))
			   {
				//inserisco solo se il terzo e' valorizzato
				Monito_cococoBulk monito = new Monito_cococoBulk();
				seq = seq + 1;
				monito.setId_report(new Integer(prog.intValue()));
				monito.setChiave(new String("MONITO_COCOCO"));
				monito.setSequenza(new Integer(seq));
				monito.setDescrizione(new String("Monitoraggio CoCoCo"));
				monito.setDt_da_competenza_coge(lancio_monito.getDt_da_competenza_coge());
				monito.setDt_a_competenza_coge(lancio_monito.getDt_a_competenza_coge());
				monito.setTipo(new String("D"));
				monito.setCd_terzo(terzo.toString());
				monito.setDenominazione(denominazione);
				monito.setIm_totale_compenso(tot_compensi_terzo);
				monito.setIm_lordo_percipiente(tot_lordo_perc_terzo);
				monito.setIm_netto_percipiente(tot_netto_perc_terzo);
				monito.setIm_cr_percipiente(tot_carico_perc_terzo);       
				monito.setIm_cr_ente(tot_carico_ente_terzo); 
				monito.setIm_totale_compenso_periodo(tot_compensi_periodo_terzo);
				monito.setNumero_giorni(new Long(conta_giorni_terzo));
				monito.setAttivita(lancio_monito.getAttivita());
				monito.setToBeCreated();
				super.creaConBulk(userContext,monito);

				//annullo le variabili
				conta_giorni_terzo=0; 
				tot_compensi_terzo=new BigDecimal(0);
				tot_lordo_perc_terzo=new BigDecimal(0);
				tot_netto_perc_terzo=new BigDecimal(0);
				tot_carico_perc_terzo=new BigDecimal(0);       
				tot_carico_ente_terzo=new BigDecimal(0); 
				tot_compensi_periodo_terzo=new BigDecimal(0);
			   }
			   if (!terzo.equals(compenso.getCd_terzo()))
			   {
				terzo = compenso.getCd_terzo();
				if (compenso.getRagione_sociale() != null)
				    denominazione = compenso.getRagione_sociale();
				else
				  	denominazione = compenso.getCognome()+" "+ compenso.getNome();     	
			   }
				conta_giorni_compenso=0;
				tot_compensi_periodo_compenso=new BigDecimal(0);
				
				if (!compenso.getDt_da_competenza_coge().before(lancio_monito.getDt_da_competenza_coge()) && 
				    !compenso.getDt_a_competenza_coge().after(lancio_monito.getDt_a_competenza_coge()))
						//conta_giorni_compenso = conta_giorni_compenso + (compenso.getDt_a_competenza_coge() - compenso.getDt_da_competenza_coge() + 1);
						conta_giorni_compenso = conta_giorni_compenso + (DateUtils.daysBetweenDates(compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge())+1);
				else 
				if (!compenso.getDt_da_competenza_coge().after(lancio_monito.getDt_da_competenza_coge()) && 
				    !compenso.getDt_a_competenza_coge().before(lancio_monito.getDt_a_competenza_coge())) 
						//conta_giorni_compenso = conta_giorni_compenso + (lancio_monito.getDt_a_competenza_coge() - lancio_monito.getDt_da_competenza_coge() + 1);
						conta_giorni_compenso = conta_giorni_compenso + (DateUtils.daysBetweenDates(lancio_monito.getDt_da_competenza_coge(), lancio_monito.getDt_a_competenza_coge())+1);
				else
				if (!compenso.getDt_da_competenza_coge().after(lancio_monito.getDt_da_competenza_coge()) && 
					!compenso.getDt_a_competenza_coge().before(lancio_monito.getDt_da_competenza_coge()) &&
					!compenso.getDt_a_competenza_coge().after(lancio_monito.getDt_a_competenza_coge()))
						//conta_giorni_compenso = conta_giorni_compenso + (compenso.getDt_a_competenza_coge() - lancio_monito.getDt_da_competenza_coge() + 1);
						conta_giorni_compenso = conta_giorni_compenso + (DateUtils.daysBetweenDates(lancio_monito.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge())+1);
				else     
						//conta_giorni_compenso = conta_giorni_compenso + (lancio_monito.getDt_a_competenza_coge() - compenso.getDt_da_competenza_coge() + 1);
						conta_giorni_compenso = conta_giorni_compenso + (DateUtils.daysBetweenDates(compenso.getDt_da_competenza_coge(), lancio_monito.getDt_a_competenza_coge())+1);
				
						conta_giorni_terzo = conta_giorni_terzo + conta_giorni_compenso;
						tot_compensi_terzo = tot_compensi_terzo.add(compenso.getIm_totale_compenso());     
						tot_lordo_perc_terzo = tot_lordo_perc_terzo.add(compenso.getIm_lordo_percipiente());     
						tot_netto_perc_terzo = tot_netto_perc_terzo.add(compenso.getIm_netto_percipiente());     
						tot_carico_perc_terzo = tot_carico_perc_terzo.add(compenso.getIm_cr_percipiente());     
						tot_carico_ente_terzo = tot_carico_ente_terzo.add(compenso.getIm_cr_ente());

						//tot_compensi per periodo del compenso in esame
						moltiplicatore_importi = calcolaMoltiplicatore(userContext,compenso,lancio_monito);

						//per calcolare l'importo del compenso relativo al solo periodo di estrazione
						//occorre dividere l'importo per il periodo di competenza del compenso e moltiplicarlo
						//per l'effettivo numero di giorni del compenso nel periodo di riferimento
						// CALCOLO IL DIVISORE considerando che potrebbe essere a cavallo fra due anni
						divisore = DateUtils.daysBetweenDates(compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge()) + 1;
						//tot_compensi_periodo_compenso= (compenso.getIm_totale_compenso().divide(new BigDecimal((getGregorianCalendar(compenso.getDt_a_competenza_coge()).get(java.util.GregorianCalendar.DAY_OF_YEAR) - getGregorianCalendar(compenso.getDt_da_competenza_coge()).get(java.util.GregorianCalendar.DAY_OF_YEAR)) + 1),5,java.math.BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(moltiplicatore_importi));
						tot_compensi_periodo_compenso= (compenso.getIm_totale_compenso().divide(new BigDecimal(divisore),5,java.math.BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(moltiplicatore_importi));
						tot_compensi_periodo_terzo = tot_compensi_periodo_terzo.add(tot_compensi_periodo_compenso.setScale(2,java.math.BigDecimal.ROUND_HALF_UP));														
						/* eventualmente per la riga totale   
						tot_compensi := tot_compensi + rec_compensi.im_totale_compenso;     
						tot_lordo_perc := tot_lordo_perc + rec_compensi.im_lordo_percipiente;     
						tot_netto_perc := tot_netto_perc + rec_compensi.im_netto_percipiente;     
						tot_carico_perc := tot_carico_perc + rec_compensi.im_cr_percipiente;     
						tot_carico_ente := tot_carico_ente + rec_compensi.im_cr_ente;
						*/						
			}
				//per l'ultimo inserisco
				  Monito_cococoBulk monito = new Monito_cococoBulk();
				  seq = seq + 1;
				  monito.setId_report(new Integer(prog.intValue()));
				  monito.setChiave(new String("MONITO_COCOCO"));
				  monito.setSequenza(new Integer(seq));
				  monito.setDescrizione(new String("Monitoraggio CoCoCo"));
				  monito.setDt_da_competenza_coge(lancio_monito.getDt_da_competenza_coge());
				  monito.setDt_a_competenza_coge(lancio_monito.getDt_a_competenza_coge());
				  monito.setTipo(new String("D"));
				  monito.setCd_terzo(terzo.toString());
				  monito.setDenominazione(denominazione);
				  monito.setIm_totale_compenso(tot_compensi_terzo);
				  monito.setIm_lordo_percipiente(tot_lordo_perc_terzo);
				  monito.setIm_netto_percipiente(tot_netto_perc_terzo);
				  monito.setIm_cr_percipiente(tot_carico_perc_terzo);       
				  monito.setIm_cr_ente(tot_carico_ente_terzo); 
				  monito.setIm_totale_compenso_periodo(tot_compensi_periodo_terzo);
				  monito.setNumero_giorni(new Long(conta_giorni_terzo));
				  monito.setAttivita(lancio_monito.getAttivita());
				  monito.setToBeCreated();
				  super.creaConBulk(userContext,monito);
					
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}catch (RuntimeException e) {
			throw new RuntimeException();
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} 
	}
	public int calcolaMoltiplicatore (UserContext userContext, CompensoBulk compenso, Monito_cococoBulk lancio_monito )
	{
		int moltiplicatore;
		Date intervallo_min;
		Date intervallo_max;
		int min;
		int max;

		if (!lancio_monito.getDt_da_competenza_coge().before(compenso.getDt_da_competenza_coge())) 
			 intervallo_min = lancio_monito.getDt_da_competenza_coge();
		else
			 intervallo_min = compenso.getDt_da_competenza_coge();
		if (!lancio_monito.getDt_a_competenza_coge().after(compenso.getDt_a_competenza_coge())) 
			 intervallo_max = lancio_monito.getDt_a_competenza_coge();
		else
		 	 intervallo_max = compenso.getDt_a_competenza_coge();
	 
		min = getGregorianCalendar(intervallo_min).get(java.util.GregorianCalendar.DAY_OF_YEAR);
		max = getGregorianCalendar(intervallo_max).get(java.util.GregorianCalendar.DAY_OF_YEAR);
			 
		moltiplicatore = max - min + 1;
		return moltiplicatore;
	}
	public GregorianCalendar getGregorianCalendar(Date dt) 
	{
		if(dt == null)
			return null;
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(dt);
		return(gc);
	}	
	private java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

		//ricavo il progressivo unico pg_stampa
		java.math.BigDecimal pg_Stampa= new java.math.BigDecimal(0);
		try {
			LoggableStatement ps= new LoggableStatement(getConnection(userContext),"select IBMSEQ00_STAMPA.nextval from dual",
					true,this.getClass());
			try {
				java.sql.ResultSet rs= ps.executeQuery();
				try {
					if (rs.next())
						pg_Stampa= rs.getBigDecimal(1);
				} finally {
					try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			} finally {
				try{ps.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
		return pg_Stampa;

	}
	
	public java.util.List findRighe(UserContext userContext, java.math.BigDecimal prog) throws PersistencyException, IntrospectionException, ComponentException
	{
		Monito_cococoHome monitoHome = (Monito_cococoHome)getHome(userContext, Monito_cococoBulk.class);
		SQLBuilder sql = monitoHome.createSQLBuilder();
		
		sql.addSQLClause("AND","ID_REPORT", sql.NOT_EQUALS, new Integer(prog.intValue()));
		
		return monitoHome.fetchAll(sql);
	}

	public void eliminaConBulk (UserContext aUC,Monito_cococoBulk bulk) throws ComponentException 
	{
		try 
		{
			super.eliminaConBulk(aUC, bulk);
		}
		catch (Throwable e) 
		{
			throw handleException(e);
		} 	
	}
	protected void validaEliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
	{
		
	}
}