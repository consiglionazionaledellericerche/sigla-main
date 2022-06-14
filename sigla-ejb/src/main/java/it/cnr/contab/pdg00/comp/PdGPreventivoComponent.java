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

package it.cnr.contab.pdg00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepBulk;
import it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Classificazione_speseBulk;
import it.cnr.contab.config00.pdcfin.bulk.Classificazione_speseHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg00.cdip.bulk.Stampa_rendiconto_finanziarioVBulk;
import it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk;
import it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Vector;

public class PdGPreventivoComponent extends PdGComponent implements it.cnr.jada.comp.ICRUDMgr, IPdGPreventivoTestataMgr, IPdGPreventivoDettagliMgr, IPrintMgr, Cloneable,Serializable {

	private static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);



    public  PdGPreventivoComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto l'annullamento dello scarico dei costi del dipendente per un pdg
  *    PostCondition:
  *      Viene invocata la stored procedure ORACLEannullaCDPSuPdg
 */
//^^@@
	public Pdg_preventivoBulk annullaCDPSuPdg(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB060.annullaCDPSuPdg(?,?,?)}",false,this.getClass());
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setString( 2, pdg.getCd_centro_responsabilita());
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,pdg);
				cs.executeQuery();
				return caricaPdg(userContext,pdg.getCentro_responsabilita());
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			throw handleSQLException(e);
		}
	}

/** 
  *  Utente AC
  *    PreCondition:
  *      l'utente appartiene al CDR Amministrazione Centrale 
  *    PostCondition:
  *      Viene restituito un istanza di Pdg_preventivoBulk per il CDR 00 dell UO CDS SAC e l'elenco di tutti i CDR che possiedono PDG
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_preventivoBulk che per il CDR specificato più l'elenco dei CDR con livello di responsabilià inferiore al CDR specificato (listaCdrPdGPerUtente)
  *  Utente associato a CDR senza PDG
  *    PreCondition:
  *      Il cdr dell'utente non possiede un PDG
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "Il CdR non ha un piano di gestione associato!"
 */

public Pdg_preventivoBulk caricaPdg (UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {

		if(cdr == null)
		 cdr = cdrFromUserContext(userContext);
		int livelloResponsabilita = getLivelloResponsabilitaCDR(userContext, cdr);

		if(livelloResponsabilita == LV_AC)
			cdr = ((it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext,CdrBulk.class)).findCdrSAC(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

		Pdg_preventivoBulk preventivo = new Pdg_preventivoBulk();

//			preventivo.setCentro_responsabilita(new CdrBulk());

		preventivo.setCentro_responsabilita(cdr);

//			preventivo.getCentro_responsabilita().setCd_centro_responsabilita(cdr.getCd_centro_responsabilita());
		preventivo.setEsercizio(
			((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio()
		);

		try {
			preventivo = (Pdg_preventivoBulk)getHome(userContext,preventivo).findByPrimaryKey(preventivo);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new it.cnr.jada.comp.ComponentException(e);
		}
		
		if (preventivo == null)
			throw new it.cnr.jada.comp.ApplicationException("Piano di gestione non ancora aperto.");

		preventivo.setStatiKeys(loadStatiKeys(userContext, preventivo));

//			preventivo.setElencoCdR(listaCdrPdGPerUtente(userContext));

		getHomeCache(userContext).fetchAll(userContext);
		
		return preventivo;
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}

/** 
  *  Controllo di chiusura del pdg aggregato
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkAggregatoChiuso ritorna 'Y'
  *    PostCondition:
  *      Lascia uscire true
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkAggregatoChiuso ritorna 'N'
  *    PostCondition:
  *      Ritorna un'eccezione
 */
private void controllaAggregatoChiuso(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.checkAggregatoChiuso(?,?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.execute();
		    if (cs.getString(1).equals("N"))
				throw new it.cnr.jada.comp.ApplicationException("Il piano di gestione aggregato non è chiuso. Contattare il CDR di primo livello");
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

private void controllaAggregatoPdgDetPositivo(UserContext userContext, Pdg_preventivoBulk pdg,Pdg_preventivo_detBulk pdg_dett) throws it.cnr.jada.comp.ComponentException {
		if (pdg_dett instanceof Pdg_preventivo_etr_detBulk)
			controllaAggregatoPdgEtrDetPositivo(userContext,pdg,(Pdg_preventivo_etr_detBulk)pdg_dett);
		else if (pdg_dett instanceof Pdg_preventivo_spe_detBulk)
			controllaAggregatoPdgSpeDetPositivo(userContext,pdg,(Pdg_preventivo_spe_detBulk)pdg_dett);
}

private void controllaAggregatoPdgEtrDetPositivo(UserContext userContext, Pdg_preventivoBulk pdg,Pdg_preventivo_etr_detBulk pdg_det) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB075.checkAggrPdgEtrDetPositivo(?, ?, ?, ?, ?, ?, ?, ? )}",false,this.getClass());
		try {
			cs.setInt( 1, pdg_det.getEsercizio().intValue() );
			cs.setString( 2, pdg_det.getCd_centro_responsabilita());
			cs.setString( 3, pdg_det.getCd_linea_attivita());
			cs.setString( 4, pdg_det.getTi_appartenenza());
			cs.setString( 5, pdg_det.getTi_gestione());
			cs.setString( 6, pdg_det.getCd_elemento_voce());
			cs.registerOutParameter( 7, java.sql.Types.VARCHAR );
			cs.registerOutParameter( 8, java.sql.Types.DECIMAL);
			cs.execute();
			String columnName = cs.getString(7);
		    if (columnName != null) {
			    String columnDescription = columnName;
			    java.math.BigDecimal importo = cs.getBigDecimal(8);
			    ColumnMapping mapping = getHome(userContext,pdg_det).getColumnMap().getMappingForColumn(columnName);
			    if (mapping != null) {
				    FieldProperty property = BulkInfo.getBulkInfo(pdg_det.getClass()).getFieldProperty(mapping.getPropertyName());
				    if (property != null)
				    	columnDescription = property.getLabel();
			    }
				throw new it.cnr.jada.comp.ApplicationException("Si sta tentando di inserire un dettaglio con importi negativi sulla colonna "+columnDescription+", ma il totale per GAE e voce del piano è negativo ("+new it.cnr.contab.util.EuroFormat().format(importo)+").");
		    }
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

private void controllaAggregatoPdgSpeDetPositivo(UserContext userContext, Pdg_preventivoBulk pdg,Pdg_preventivo_spe_detBulk pdg_det) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs =new LoggableStatement( getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB075.checkAggrPdgSpeDetPositivo(?, ?, ?, ?, ?, ?, ?, ?, ? )}",false,this.getClass());
		try {
			cs.setInt( 1, pdg_det.getEsercizio().intValue() );
			cs.setString( 2, pdg_det.getCd_centro_responsabilita());
			cs.setString( 3, pdg_det.getCd_linea_attivita());
			cs.setString( 4, pdg_det.getTi_appartenenza());
			cs.setString( 5, pdg_det.getTi_gestione());
			cs.setString( 6, pdg_det.getCd_elemento_voce());
			cs.setLong( 7, pdg_det.getPg_spesa().longValue());
			cs.registerOutParameter( 8, java.sql.Types.VARCHAR );
			cs.registerOutParameter( 9, java.sql.Types.DECIMAL);
			cs.execute();
			String columnName = cs.getString(8);
		    if (columnName != null) {
			    String columnDescription = columnName;
			    java.math.BigDecimal importo = cs.getBigDecimal(9);
			    ColumnMapping mapping = getHome(userContext,pdg_det).getColumnMap().getMappingForColumn(columnName);
			    if (mapping != null) {
				    FieldProperty property = BulkInfo.getBulkInfo(pdg_det.getClass()).getFieldProperty(mapping.getPropertyName());
				    if (property != null)
				    	columnDescription = property.getLabel();
			    }
				throw new it.cnr.jada.comp.ApplicationException("Si sta tentando di inserire un dettaglio con importi negativi sulla colonna "+columnDescription+", ma il totale per GAE e voce del piano è negativo ("+new it.cnr.contab.util.EuroFormat().format(importo)+").");
		    }
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

private void controllaBilancioPreventivoCdsApprovato(UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(cdr.getUnita_padre());
		Bilancio_preventivoBulk	bilancio = (Bilancio_preventivoBulk)getHome(userContext,Bilancio_preventivoBulk.class).findByPrimaryKey(new Bilancio_preventivoBulk(
			uo.getCd_unita_padre(),
			it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
			Elemento_voceHome.APPARTENENZA_CDS));
		if (bilancio == null)
			throw new it.cnr.jada.comp.ApplicationException("Bilancio preventivo inesistente per il cds "+uo.getCd_unita_padre());
		if (!bilancio.STATO_C.equalsIgnoreCase(bilancio.getStato()))
			throw new it.cnr.jada.comp.ApplicationException("Il bilancio preventivo del cds "+uo.getCd_unita_padre()+" deve essere approvato per riaprire il pdg per variazioni");
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  Verifica che il bilancio preventivo CNR sia approvato
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB054.isBilancioCNRApprovato ritorna 'N'
  *    PostCondition:
  *      Viene segnalato con un'eccezione che il bilancio preventivo CNR non è approvato
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB054.isBilancioCNRApprovato ritorna 'Y'
  *    PostCondition:
  *      Esce senza alcuna eccezione
 */
private void controllaBilPrevCnrApprovato(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB054.isBilancioCNRApprovato(?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.execute();
		    if (cs.getString(1).equals("N"))
				throw new it.cnr.jada.comp.ApplicationException("Il bilancio preventivo CNR non risulta ancora approvato");
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  Verifica che tutti i dettagli scaricati verso altra uo o cdr siano stati confermati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkDettScarConfermati ritorna 'N'
  *    PostCondition:
  *      Solleva un'eccezione
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkDettScarConfermati ritorna 'Y'
  *    PostCondition:
  *      Esce senza segnalazioni
 */
private void controllaDettScarConfermati(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.checkDettScarConfermati(?,?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.execute();
		    if (cs.getString(1).equals("N"))
				throw new it.cnr.jada.comp.ApplicationException("Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata");
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}
/**
 *  Pdg aggregato contiene discrepanze
 *    PreCondition:
 *      Le viste V_DPDG_AGGREGATO_ETR_DET_D e V_DPDG_AGGREGATO_SPE_DET_D contengono almeno un record
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Sono presenti importi complessivi non compatibili con i dati impostati dall'ente."
 *  Tuttti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Esce senza alcuna eccezione
 */
private void controllaDiscrepanzeAggregato(UserContext userContext,Pdg_preventivoBulk pdg,int livelloResponsabilita) throws ComponentException {
	// Controllo della presenza di discrepanze nell'aggregato; va fatto solo per cdr di 1° livello e per le aree
	if (livelloResponsabilita == 1 ||
		isCdrArea(userContext,pdg.getCentro_responsabilita())) {

		controllaDiscrepanzeAggregato(userContext,pdg,Pdg_aggregato_etr_detBulk.class,"V_DPDG_AGGREGATO_ETR_DET_D");
		controllaDiscrepanzeAggregato(userContext,pdg,Pdg_aggregato_spe_detBulk.class,"V_DPDG_AGGREGATO_SPE_DET_D");
	}
}
private void controllaDiscrepanzeAggregato(UserContext userContext,Pdg_preventivoBulk pdg,Class dettagliClass,String vista) throws ComponentException {
	try {
		PersistentHome home = getHome(userContext,dettagliClass,vista);
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, pdg.getEsercizio());
		sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", sql.EQUALS, pdg.getCd_centro_responsabilita()); 

		if (sql.executeExistsQuery(getConnection(userContext)))
			throw new it.cnr.jada.comp.ApplicationException("Sono presenti importi complessivi non compatibili con i dati impostati dall'ente.");
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public boolean controllaDiscrepanzeAggregatoForCons(UserContext userContext,OggettoBulk obj, String vista) throws ComponentException {
	try {
		Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)obj;
		PersistentHome home = null;
		if (vista.equals("ETR"))
		  home = getHome(userContext,it.cnr.contab.pdg00.consultazioni.bulk.V_dpdg_aggregato_etr_det_dBulk.class);
		else if (vista.equals("SPE"))
		  home = getHome(userContext,it.cnr.contab.pdg00.consultazioni.bulk.V_dpdg_aggregato_spe_det_dBulk.class);
		else
		  return false;  
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, pdg.getEsercizio());
		sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", sql.EQUALS, pdg.getCd_centro_responsabilita()); 
		return sql.executeExistsQuery(getConnection(userContext));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/**
 *  Pdg aggregato contiene discrepanze
 *    PreCondition:
 *      Le viste V_DPDG_AGGREGATO_ETR_DET_D e V_DPDG_AGGREGATO_SPE_DET_D contengono almeno un record
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Sono presenti importi complessivi non compatibili con i dati impostati dall'ente."
 *  Tuttti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Esce senza alcuna eccezione
 */
private void controllaDiscrepanzeAggregatoPerVariazioni(UserContext userContext,Pdg_preventivoBulk pdg,int livelloResponsabilita) throws ComponentException {
	// Controllo della presenza di discrepanze nell'aggregato; va fatto solo per cdr di 1° livello e per le aree
	if (livelloResponsabilita == 1 ||
		isCdrArea(userContext,pdg.getCentro_responsabilita())) {

		controllaDiscrepanzeAggregato(userContext,pdg,Pdg_aggregato_etr_detBulk.class,"V_DPDG_AGGREGATO_ETR_DET_D");
		controllaDiscrepanzeAggregato(userContext,pdg,Pdg_aggregato_spe_detBulk.class,"V_DPDG_AGGREGATO_SPE_DET_D");
	}
}
/** 
  *  Quadratura su insieme linee di attività entrata/spesa
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkDiscrepanzeInsiemeLa ritorna 'Y'
  *    PostCondition:
  *      Lascia uscire true
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkDiscrepanzeInsiemeLa ritorna 'N'
  *    PostCondition:
  *      Esce ritornando false
 */
private boolean controllaDiscrepanzeInsiemeLa(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {
		LoggableStatement cs =new LoggableStatement( getConnection( userContext ),"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.check_discrepanze_insieme_la(?,?)}",false,this.getClass());
		try {			
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.execute();
		    return !cs.getString(1).equals("N");
		} finally {
			cs.close();
		}
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/** 
  *  Quadratura fallita
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkQuadRicFig genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB050.checkQuadRicFig va a buon fine
  *    PostCondition:
  *      Esce senza alcuna eccezione
 */
private void controllaQuadraturaRicaviFigurativi(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB050.checkQuadRicFig(?,?)}",false,this.getClass());
		try {			
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.execute();
		    if (cs.getString(1).equals("N")) 
				throw new it.cnr.jada.comp.ApplicationException("Mancata quadratura dei ricavi figurativi!");
		} finally {
			cs.close();
		}
	} catch (Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  Verifica che lo scarico dei costi del dipendente sia completo
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB060.checkScaricoCDPCompleto genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione
  *  Tutti i controlli superati
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB060.checkScaricoCDPCompleto va a buon fine
  *    PostCondition:
  *      Esce senza alcuna eccezione
 */
private void controllaScaricoCDPCompleto(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB060.checkScaricoCDPCompleto(?,?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.execute();
		    if (cs.getString(1).equals("N"))
				throw new it.cnr.jada.comp.ApplicationException("Costi del personale non ancora scaricati completamente!");
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/** 
  *  Verifica che il pdg aggregato sia nello stato specificato
  *    PreCondition:
  *      L'invocazione della stored procedure CNRCTB060.checkStatoAggregato ritorna 'N'
  *    PostCondition:
  *      Genera una ApplicationException con il messaggio "Per poter eseguire l'operazione richiesta il piano di gestione aggregato dovrebbe essere in stato X ma attualmente si trova in un altro stato."
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Esce senza alcuna eccezione
 */
private void controllaStatoAggregato(UserContext userContext, Pdg_preventivoBulk pdg,String stato) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.checkStatoAggregato(?,?,?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, pdg.getEsercizio() );
			cs.setString( 3, pdg.getCd_centro_responsabilita());
			cs.setString( 4, stato);
			cs.execute();
		    if (cs.getString(1).equals("N"))
				throw new it.cnr.jada.comp.ApplicationException("Per poter eseguire l'operazione richiesta il piano di gestione aggregato dovrebbe essere in stato "+stato+" ma attualmente si trova in un altro stato.");
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/**
 *  Esistono pdg dipendenti in stato diverso da quelli specificati
 *    PreCondition:
 *      Esiste almeno un pdg figlio del pdg specificato che si trova in uno stato diverso da quelli specificificati
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione verificata
 *    PostCondition:
 *      Esce senza alcuna eccezione
 */
private boolean controllaStatoPdgFigli(UserContext userContext,Pdg_preventivoBulk pdg,String[] stati) throws ComponentException {
	try {
		it.cnr.jada.bulk.BulkHome home = getHome(userContext, Pdg_preventivoBulk.class);
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("V_PDG_CDR_FIGLI");
		sql.addSQLJoin("PDG_PREVENTIVO.ESERCIZIO","V_PDG_CDR_FIGLI.ESERCIZIO");
		sql.addSQLJoin("PDG_PREVENTIVO.CD_CENTRO_RESPONSABILITA","V_PDG_CDR_FIGLI.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND", "V_PDG_CDR_FIGLI.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "V_PDG_CDR_FIGLI.CD_CDR_ROOT", sql.EQUALS, pdg.getCd_centro_responsabilita());
		sql.setForUpdateOf("stato");

		LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
		try {
			java.sql.ResultSet rs = stm.executeQuery();
			try {
				while (rs.next()) {
					String stato_figlio = rs.getString("STATO");
					boolean trovato = false;
					for (int i = 0;!trovato && i < stati.length;i++)
						trovato = stati[i].equals(stato_figlio);

					if (!trovato)
							throw new it.cnr.jada.comp.ApplicationException("Il pdg del cdr "+rs.getString("CD_CENTRO_RESPONSABILITA")+" è in stato "+stato_figlio+" che non è compatibile con l'operazione richiesta.");
				}
			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
		return true;
	} catch(Throwable e) {
		throw handleException(e);
	}
}

/**
 *  Esistono pdg padri in stato diverso da quelli specificati
 *    PreCondition:
 *      Esiste almeno un pdg padre del pdg specificato che si trova in uno stato diverso da quelli specificati.
 *		Se "ricorsivo" è true la ricerca viene fatta su tutti i padri fino all'AC, altrimenti solo sul padre diretto
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione verificata
 *    PostCondition:
 *      Esce senza alcuna eccezione
 */
private boolean controllaStatoPdgPadri(UserContext userContext,Pdg_preventivoBulk pdg,int livelloResponsabilita,boolean ricorsivo,String[] stati) throws ComponentException {
	try {

		// Sel il cdr del pdg è di livello 1, o è l'ente o è in un area di ricerca
		// non ci sono pdg padri, quindi ritorno true

		if (livelloResponsabilita == LV_CDRI ||
			livelloResponsabilita == LV_AC ||
			isCdrArea(userContext,pdg.getCentro_responsabilita()))
			return true;

		String cd_cdr = pdg.getCd_centro_responsabilita();

		do {

			BulkHome home = getHome(userContext, Pdg_preventivoBulk.class, "V_PDG_PDG_PADRE");
			SQLBuilder sql = home.createSQLBuilder();

			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ));
			sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cd_cdr);
			sql.setForUpdateOf("stato");

			LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
			try {
				java.sql.ResultSet rs = stm.executeQuery();
				try {
					if (!rs.next())
						return true;

					String stato_padre = rs.getString("STATO");
					cd_cdr = rs.getString("CD_CENTRO_RESPONSABILITA");
					boolean trovato = false;
					for (int i = 0;!trovato && i < stati.length;i++)
						trovato = stati[i].equals(stato_padre);

					if (!trovato)
							throw new it.cnr.jada.comp.ApplicationException("Il pdg del cdr "+cd_cdr+" è in stato "+stato_padre+" che non è compatibile con l'operazione richiesta.");
					
				} finally {
					try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				try{stm.close();}catch( java.sql.SQLException e ){};
			}
		} while(ricorsivo);

		return true;
		
	} catch(Throwable e) {
		throw handleException(e);
	}
}

/**
 *  Imposta gli importi assegnati sul dettaglio di entrata del servente in base
 *  agli importi valorizzati sul dettaglio di spesa del servito.
 *
 *  @param userContext
 *  @param servito		Chi assegna l'entrata figurativa e sottomette la richiesta di accettazione
 *  @param servente		Chi accetta o meno l'entrata figurativa assegnatagli
 */

	private void copiaImportAltroCDR(UserContext userContext,Pdg_preventivo_spe_detBulk servito,Pdg_preventivo_etr_detBulk servente) throws it.cnr.jada.comp.ComponentException {

		servente.setIm_rb_rse( servito.getIm_rp_css_verso_altro_cdr() );

		servente.setIm_rd_a2_ricavi( servito.getIm_rab_a2_costi_altro_cdr() );

		servente.setIm_rf_a3_ricavi( servito.getIm_rai_a3_costi_altro_cdr() );

	}

/**
 *  Imposta gli importi assegnati sul dettaglio di spesa del servente in base
 *  agli importi valorizzati sul dettaglio di spesa del servito.
 *
 *  @param userContext
 *  @param servito		Chi assegna lo scarico di spesa e sottomette la richiesta di accettazione
 *  @param servente		Chi accetta o meno lo scarico di spesa assegnatagli
 */

	private void copiaImportiAltraUO(UserContext userContext,Pdg_preventivo_spe_detBulk servito,Pdg_preventivo_spe_detBulk servente) throws it.cnr.jada.comp.ComponentException {
		if( servito.getIm_rj_ccs_spese_odc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_ru_spese_costi_altrui( servito.getIm_rj_ccs_spese_odc_altra_uo() );
		}

		if( servito.getIm_rl_ccs_spese_ogc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_ru_spese_costi_altrui( servito.getIm_rl_ccs_spese_ogc_altra_uo() );
		}

		if( servito.getIm_rr_ssc_costi_odc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_ru_spese_costi_altrui( servito.getIm_rr_ssc_costi_odc_altra_uo() );
		}

		if( servito.getIm_rt_ssc_costi_ogc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_ru_spese_costi_altrui( servito.getIm_rt_ssc_costi_ogc_altra_uo() );
		}

		if( servito.getIm_rad_a2_spese_odc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_rag_a2_spese_costi_altrui( servito.getIm_rad_a2_spese_odc_altra_uo() );
		}

		if( servito.getIm_raf_a2_spese_ogc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_rag_a2_spese_costi_altrui( servito.getIm_raf_a2_spese_ogc_altra_uo() );
		}

		if( servito.getIm_ram_a3_spese_odc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_rap_a3_spese_costi_altrui( servito.getIm_ram_a3_spese_odc_altra_uo() );
		}

		if( servito.getIm_rao_a3_spese_ogc_altra_uo().compareTo(new java.math.BigDecimal(0)) != 0 ) {
			servente.setIm_rap_a3_spese_costi_altrui( servito.getIm_rao_a3_spese_ogc_altra_uo() );
		}
	}

/** 
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      ll dettaglio viene marcato con CATEGORIA 'SIN', FL_SOLA_LETTURA = false e salvato
 *  spesa relativa altra UO
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato qualche colonna relativa a 'spese verso altra UO' (J,L,R,T,AD,AF,AM,AD); il PDG del CDR scelto è aperto (stato uguale a A, B, D o E)
 *    PostCondition:
 *      Viene creata nel PDG del cdr scelto (servente) un nuovo dettaglio di spesa valorizzazzato come segue:
 *      dettaglioServente.esercizio = dettaglioServito.esercizio
 *      dettaglioServente.cdr = cdrServente
 *      dettaglioServente.appartenenza = dettaglioServito.appartenenza
 *      dettaglioServente.gestione = dettaglioServito.gestione
 *      dettaglioServente.voce = dettaglioServito.voce
 *      dettaglioServente.clgs = dettaglioServito
 *      dettaglioServente.CATEGORIA = 'CAR'
 *      dettaglioServente.STATO = 'X'
 *      dettaglioServente.lineaDiAttivita =  Linea_attivitaComponent.creaLineaAttivitaSAUO(cdrServente,dettaglioServito.lineaDiAttivita)
 *      dettaglioServente.U = dettaglioServito.J / L / R / T (quello valorizzato)
 *      dettaglioServente.AG = dettaglioServito.AD / AF (quello valorizzato)
 *      dettaglioServente.AP = dettaglioServito.AM / AD (quello valorizzato)
 *      il dettaglio servito viene marcato con CATEGORIA 'SCR', quello servente con categotria 'CAR', e viene messo in STATO 'X'
 *  PDG servente già chiuso
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato qualche colonna riferita ad un'altro cdr (CLGS != NULL) e il PDG del CDR scelto è già "chiuso" (checkChiusuraPdg genera una Exception)
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Il PDG del <cdr> servente è stato già chiuso
 *  costi senza spese
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato la colonna P, AB o AI ; il PDG del CDR scelto è modificabile (stato A, B, D o E), la voce è "Prestazioni da struttura dell'ente" (isVoceCSSAC() ritorna true)
 *    PostCondition:
 *      Viene creata per il cdr servente un nuovo dettaglio di entrata valorizzazzato come segue:
 *      entrataServente.esercizio = dettaglioServito.esercizio
 *      entrataServente.cdr = cdrServente
 *      entrataServente.appartenenza = dettaglioServito.appartenenza
 *      entrataServente.gestione = dettaglioServito.gestione
 *      entrataServente.voce = getVoceRicaviFigurativi()
 *      entrataServente.clgs = dettaglioServito
 *      entrataServente.CATEGORIA = 'CAR'
 *      entrataServente.STATO = 'X'
 *      entrataServente.lineaDiAttivita = Linea_attivitaComponent.creaLineaAttivitaCSSAC(cdrServente,dettaglioServito.lineaDiAttivita)
 *      entrataServente.B = dettaglioServito.P
 *      entrataServente.D = dettaglioServito.AB 
 *      entrataServente.F = dettaglioServito.AI
 *      
 *      il dettaglio servito viene marcato con CATEGORIA 'SCR' e viene messo in STATO 'X'
 *      
 *  costi senza spese, voce non valida
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato la colonna P, AB o AI, la voce NON è "Prestazioni da struttura dell'ente" (isVoceCSSAC() ritorna false)
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "La voce del piano dei conti deve essere 'Prestazioni da struttura dell'ente'"
 *  spesa relativa altra UO, campi non validi
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato qualche colonna relativa a 'spese verso altra UO' (J,L,R,T,AD,AF,AM,AD)
 *      sono compilati più di uno alla volta di
 *      - J, L, R, T
 *      - AD, AF
 *      - AM, AD
 *      oppure sono compilati altri campi non relativi ad altra UO eccetto H/AA/AH
 *    PostCondition:
 *      Viene generata una ApplicationException.
 *  costi senza spese, campi non validi
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e l'utente ha compilato la colonna P, AB o AI e qualche altra colonna non relativa a "costi senza spese verso altro CDR"
 *    PostCondition:
 *      Viene generata una ApplicationException.
 *  spesa relativa altra UO per il personale, linea di attivita non valida
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e 
 *      l'utente ha compilato qualche colonna relativa a 'spese verso altra UO' (J,L,R,T,AD,AF,AM,AD) e
 *      isVoceSAUOP(dettaglio.voce) e
 *      il PDG del CDR scelto è aperto (stato uguale a A, B, D o E)
 *      la linea di attivita ha natura o funzione diverse da uno
 *    PostCondition:
 *      Viene generata una eccezione con messaggio: "Le spese per personale devono avere una linea di attività con natura e funzione 1"
 *  spesa relativa altra UO per il personale
 *    PreCondition:
 *      Viene richiesto il salvataggio di una riga di spese del PDG e 
 *      l'utente ha compilato qualche colonna relativa a 'spese verso altra UO' (J,L,R,T,AD,AF,AM,AD) e
 *      isVoceSAUOP(dettaglio.voce) = true e
 *      il PDG del CDR scelto è aperto (stato uguale a A, B, D o E)
 *      la linea di attivita ha natura o funzione uguale a uno
 *    PostCondition:
 *      Viene creata nel PDG del cdr scelto (servente) un nuovo dettaglio di spesa valorizzazzato come segue:
 *      dettaglioServente.esercizio = dettaglioServito.esercizio
 *      dettaglioServente.cdr = cdrServente
 *      dettaglioServente.appartenenza = dettaglioServito.appartenenza
 *      dettaglioServente.gestione = dettaglioServito.gestione
 *      dettaglioServente.voce = dettaglioServito.voce
 *      dettaglioServente.clgs = dettaglioServito
 *      dettaglioServente.CATEGORIA = 'CAR'
 *      dettaglioServente.STATO = 'X'
 *      dettaglioServente.lineaDiAttivita =  Linea_attivitaComponent.creaLineaAttivitaSAUOP(cdrServente)
 *      dettaglioServente.U = dettaglioServito.J / L / R / T (quello valorizzato)
 *      dettaglioServente.AG = dettaglioServito.AD / AF (quello valorizzato)
 *      dettaglioServente.AP = dettaglioServito.AM / AD (quello valorizzato)
 *      il dettaglio servito viene marcato con CATEGORIA 'SCR', quello servente con categotria 'CAR', e viene messo in STATO 'X'
 *  PDG già chiuso
 *    PreCondition:
 *      checkChiusuraPdg genera una eccezione
 *    PostCondition:
 *      Viene lasciata uscire l'eccezione
 *  linea di attività di natura 5, cdr non collegato ad area
 *    PreCondition:
 *      Linea di attività con natura 5 e
 *      CDR afferente ad UO non collegata ad area
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Non è possibile creare dettagli con natura 5 perchè il CDR non è collegato ad area attraverso la sua unità organizzativa"
 *  linea di attività di natura 5, cdr area
 *    PreCondition:
 *      Linea di attività con natura 5 e
 *      CDR afferente UO di tipo area
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Non è possibile creare dettagli con natura 5 perchè il CDR appartiene ad un'area"
 *  linea di attività di natura 5, scarico verso altra UO/CDR
 *    PreCondition:
 *      Linea di attività con natura 5
 *      e l'utente ha compilato qualche colonna relativa a 'spese verso altra UO' (J,L,R,T,AD,AF,AM,AD); il PDG del CDR scelto è aperto (stato uguale a A, B, D o E) o verso altro CDR (P, AB o AI)
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Non è possibile scaricare costi su altra UO o CDR con natura 5"
 *  Dettaglio di spesa/entrata con importo negativo
 *    PreCondition:
 *      Viene richiesto l'inserimento di un dettaglio di spesa/entrata con importo negativo
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "non e' possibile inserire importi negativi"
 *  linea di attività di natura 5, dettaglio di entrata
 *    PreCondition:
 *      Linea di attività con natura 5 e
 *      il dettaglio specificato è un dettaglio di entrata
 *    PostCondition:
 *      Genera un ApplicationException con il messaggio "Non è possibile creare voci di entrata con natura 5"
 *  dtl costo del personale - CDR non è CDR PERSONALE, CDR_SERVIZIO_ENTE - tempo indeterminato
 *    PreCondition:
 *      Il CDR è diverso da CDR PERSONALE e da CDR_SERVIZIO_ENTE.
 *      Nell'interfaccia stipendi è presente almeno un dettaglio stipendiale contenente la voce di spesa Y diversa da voce TFR (identificata in configurazione).
 *      Il dipendente specificato nel dettaglio stipendiale, sulla voce Y, è un dipendente a tempo indeterminato.
 *    PostCondition:
 *      Creazione/Modifica di dettaglio del PDG di un qualsiasi CDR diverso da  CDR DEL PERSONALE e da CDR_SERVIZIO_ENTE, su voce Y diversa da TFR  nel caso su Y esista in interfaccia stipendi un dipendente a tempo indeterminato
 *      Le colonne L e V non saranno imputabile direttamente
 *  dtl costo del personale - CDR è CDR PERSONALE  - tempo indeterminato
 *    PreCondition:
 *      Il CDR è uguale a CDR PERSONALE.
 *      Nell'interfaccia stipendi è presente almeno un dettaglio stipendiale contenente la voce di spesa Y diversa da voce TFR (identificata in configurazione).
 *      Il dipendente specificato nel dettaglio stipendiale, sulla voce Y, è un dipendente a tempo indeterminato.
 *    PostCondition:
 *      Creazione/Modifica di dettaglio del PDG del CDR DEL PERSONALE, su voce Y diversa da TFR nel caso su Y esista in interfaccia stipendi un dipendente a tempo indeterminato.
 *      La colonna K non sarà imputabile direttamente.
 *  dtl costo del personale - CDR non è CDR PERSONALE, CDR_SERVIZIO_ENTE - tempo determinato
 *    PreCondition:
 *      Il CDR è diverso da CDR PERSONALE e da CDR_SERVIZIO_ENTE.
 *      Nell'interfaccia stipendi è presente almeno un dettaglio stipendiale contenente la voce di spesa Y diversa da voce TFR (identificata in configurazione).
 *      Il dipendente specificato nel dettaglio stipendiale, sulla voce Y, è un dipendente a tempo determinato.
 *    PostCondition:
 *      Creazione/Modifica di dettaglio del PDG di un qualsiasi CDR diverso da  CDR DEL PERSONALE e da CDR_SERVIZIO_ENTE, su voce Y diversa da TFR  nel caso su Y esista in interfaccia stipendi un dipendente a tempo determinato
 *      Le colonne O e V non saranno imputabile direttamente
 *  dtl costo del personale - CDR è CDR PERSONALE  - tempo determinato
 *    PreCondition:
 *      Il CDR è uguale a CDR PERSONALE.
 *      Nell'interfaccia stipendi è presente almeno un dettaglio stipendiale contenente la voce di spesa Y diversa da voce TFR (identificata in configurazione).
 *      Il dipendente specificato nel dettaglio stipendiale, sulla voce Y, è un dipendente a tempo determinato.
 *    PostCondition:
 *      Creazione/Modifica di dettaglio del PDG del CDR DEL PERSONALE, su voce Y diversa da TFR nel caso su Y esista in interfaccia stipendi un dipendente a tempo determinato.
 *      La colonna O non sarà imputabile direttamente.
 *  Check imputabilità importi su voci del personale_1
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio NON è il conto TFR
 *      Il PDG NON è del personale o quello speciale di servizio ENTE
 *      La voce specificata nel dettaglio del PDG è presente in COSTO DEL DIPENDENTE su almeno un dipendente a tempo INDETERMINATO
 *      Le colonne L o V sono valorizzate
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulle colonne L o V
 *  Check imputabilità importi su voci del personale_2
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio NON è il conto TFR
 *      Il PDG NON è del personale o quello speciale di servizio ENTE
 *      La voce specificata nel dettaglio del PDG è presente in COSTO DEL DIPENDENTE su almeno un dipendente a tempo DETERMINATO
 *      Le colonne O o V sono valorizzate
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulle colonne O o V
 *  Check imputabilità importi su voci del personale_3
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio NON è il conto TFR
 *      Il PDG è quello del personale
 *      La voce specificata nel dettaglio del PDG è presente in COSTO DEL DIPENDENTE su almeno un dipendente a tempo INDETERMINATO
 *      La colonna K è valorizzata
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulla colonna K
 *      
 *  Check imputabilità importi su voci del personale_4
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio NON è il conto TFR
 *      Il PDG è quello del personale
 *      La voce specificata nel dettaglio del PDG è presente in COSTO DEL DIPENDENTE su almeno un dipendente a tempo DETERMINATO
 *      La colonna O è valorizzate
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulla colonna O
 *  Check imputabilità importi su voci del personale_5
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio è il conto TFR
 *      Il PDG NON è del personale o quello speciale di servizio ENTE
 *      Le colonne O o V sono valorizzate
 *      
 *      
 *      
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulle colonne O o V
 *  Check imputabilità importi su voci del personale_6
 *    PreCondition:
 *      Il dettaglio è di spesa
 *      Il conto su cui sto registrando il dettaglio è il conto TFR
 *      Il PDG è del personale
 *      La colonna O è valorizzata
 *       
 *    PostCondition:
 *      Viene generata una eccezione che segnala la non imputabilità sulla colonna O
 *      
 */

/**
 * Prima di eseguire il metodo {@link it.cnr.jada.comp.CRUDComponent#creaConBulk } vengono
 * effettuati dei controlli sui dati inseriti.
 *
 * Nome: Creare un nuovo elemento PdG;
 * Pre:  Effetuare il salvataggio del PdG con i dati corretti;
 * Post: Prima di effettuare il salvataggio avvia il metodo per i controlli.
 *
 * @param userContext UserContext in uso.
 * @param bulk {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk } da salvare.
 *
 * @return {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk } salvato
 */

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	Pdg_preventivo_detBulk pdgDett = (Pdg_preventivo_detBulk)bulk;

	try {
		Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)getHome(userContext, Pdg_preventivoBulk.class).findAndLock(
			new Pdg_preventivoBulk(
				pdgDett.getEsercizio(),
				pdgDett.getCd_centro_responsabilita()));

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare dettagli del pdg ad esercizio chiuso.");

		init(userContext,pdg,pdgDett);

		super.creaConBulk(userContext,bulk);

		validaVariazione(userContext, bulk);

		/*
		 * Il controllo che la somma degli importi aggregati sia positiva non deve avvenire
		 * per i dettagli provenienti da variazione al PDG non ancora in stato approvato
		 */
		if (!pdgDett.isDaVariazione()) 
			controllaAggregatoPdgDetPositivo(userContext,pdg,pdgDett);
		else
		{ 
		    Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)getHome(userContext, Pdg_variazioneBulk.class).findByPrimaryKey(
			new Pdg_variazioneBulk(
				pdgDett.getPdg_variazione().getEsercizio(),
		        pdgDett.getPdg_variazione().getPg_variazione_pdg()));
		  if (pdgVar.isApprovata())
		  	controllaAggregatoPdgDetPositivo(userContext,pdg,pdgDett);
		}
		    
		return bulk;
	} catch(OutdatedResourceException e) {
		throw handleException(e);
	} catch(PersistencyException e) {
		throw handleException(e);
	} catch(BusyResourceException e) {
		throw handleException(e);
	}
}
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la ripartizione delle entrate bil. fin. cnr sulle UO non oggetto diretto di contrattazione con l'ente
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB055.creaRipartEntrate
 */
private void creaRipartEntrate(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB055.creaRipartEntrate(?,?,?)}",false,this.getClass());
		try {
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setString( 2, pdg.getCd_centro_responsabilita());
			cs.setString( 3, userContext.getUser());			
			cs.execute();
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/**
 *  Genera o modifica un dettaglio di scarico di spesa e il suo collegato
 *
 *  @param userContext
 *  @param pdgSpe		dettaglio del servito
 */

private Pdg_preventivo_spe_detBulk creaSCRAltraUO(UserContext userContext,Pdg_preventivo_spe_detBulk pdgSpe) throws it.cnr.jada.comp.ComponentException {

	boolean newDett;

	if(!pdgSpe.isROaltra_uo()) {
		newDett = true;
	} else if(pdgSpe.isToBeUpdated()){
		newDett = false;
	} else return pdgSpe;

	Pdg_preventivoBulk pdg = null;
	if( isVoceSAUOP(userContext, pdgSpe.getElemento_voce()) ) {
		try {
	// Modifica del 12/02/2002 - I costi finali pluriennali non rientrano nel controllo
			if(	//      ( pdgSpe.getIm_raa_a2_costi_finali() != null && pdgSpe.getIm_raa_a2_costi_finali().intValue() != 0 ) ||
				(pdgSpe.getIm_rac_a2_spese_odc().compareTo(ZERO) != 0)
				||	(pdgSpe.getIm_rae_a2_spese_ogc().compareTo(ZERO) != 0 )
				// ||	(pdgSpe.getIm_rah_a3_costi_finali().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_ral_a3_spese_odc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_ran_a3_spese_ogc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_ri_ccs_spese_odc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rm_css_ammortamenti().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rn_css_rimanenze().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_ro_css_altri_costi().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rk_ccs_spese_ogc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rq_ssc_costi_odc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rs_ssc_costi_ogc().compareTo(ZERO) != 0 )
				||	(pdgSpe.getIm_rv_pagamenti().compareTo(ZERO) != 0 ) )
			{
				throw new it.cnr.jada.comp.ApplicationException("Sono stati valorizzati degli importi non compatibili con lo scarico dei costi del personale.");
			}
			Pdg_preventivoHome pdgHome = (Pdg_preventivoHome)getHome(userContext,Pdg_preventivoBulk.class, "V_PDG_PDG_SAUOP");
			SQLBuilder sql = pdgHome.createSQLBuilder();
			sql.addClause("AND", "esercizio", sql.EQUALS, pdgSpe.getEsercizio());
			it.cnr.jada.persistency.Broker broker = pdgHome.createBroker(sql);

			if(broker.next())
				pdg = (Pdg_preventivoBulk)broker.fetch(Pdg_preventivoBulk.class);
			else
				throw new it.cnr.jada.comp.ApplicationException("Il PdG per lo scarico dei costi del personale non disponibile.");

			getHome(userContext,CdrBulk.class).findByPrimaryKey(pdg.getCentro_responsabilita());

			if(pdg.getCentro_responsabilita().equalsByPrimaryKey( pdgSpe.getCentro_responsabilita() ))
				throw new it.cnr.jada.comp.ApplicationException("Il CdR del Personale su di una voce stipendiale non può fare lo scarico dei costi su altra UO.");

			pdgSpe.setAltro_cdr(pdg.getCentro_responsabilita());
		} catch(Throwable e) {
			throw new it.cnr.jada.comp.ComponentException(e);
		}
	} else {
		if(pdgSpe.getAltro_cdr() == null) {
			throw new it.cnr.jada.comp.ApplicationException("CdR altra UO non indicato");
		} else {
			try {
				pdg = (Pdg_preventivoBulk)getHome(userContext,Pdg_preventivoBulk.class).findByPrimaryKey( new Pdg_preventivoBulk( pdgSpe.getEsercizio(), pdgSpe.getAltro_cdr().getCd_centro_responsabilita() ) );
			} catch(Throwable e) {
				throw new it.cnr.jada.comp.ComponentException(e);
			}
		}
	}
	if(pdg == null)
		throw new it.cnr.jada.comp.ApplicationException("CdR altra UO non valido");

	// Controllo chiusura PDG servente se non è una variazione
	if (pdgSpe.getPdg_variazione() == null){
		if (pdgSpe.OR_PROPOSTA_VARIAZIONE.equals(pdgSpe.getOrigine()))
			checkChiusuraPdgPerVariazioni(userContext,pdg);
		else {
			checkChiusuraPdg(userContext,pdg, pdgSpe);
	
			// 23/09/2003 CNRADM
			// Aggiunto controllo sullo stato del pdg aggregato del cdr servente:
			// se in stato B bisogna bloccare scarichi verso altra UO per garantire
			// allineamento del bilancio finanziaro (altrimenti la colonna dei costi 
			// altrui nel pdg aggregato non viene ricalcolata)
			String statoPdgAggregato = getStatoAggregato(userContext,pdg.getEsercizio(),pdg.getCd_centro_responsabilita());
			if (Pdg_aggregatoBulk.STATO_B.equals(statoPdgAggregato))
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile scaricare costi verso altra UO perchè il pdg aggregato del cdr servente è già in stato B");
		}
	}

	try {

		Pdg_preventivo_spe_detBulk dettaglioServente;
		if(newDett) {

			if( pdgSpe.getAltro_cdr().getCd_unita_organizzativa().equals( pdgSpe.getCentro_responsabilita().getCd_unita_organizzativa() ) )
				throw new it.cnr.jada.comp.ApplicationException("Selezionare un CdR con UO diversa dalla propria!");

			dettaglioServente = new Pdg_preventivo_spe_detBulk();
			dettaglioServente.setCategoria_dettaglio(pdgSpe.CAT_CARICO);
			dettaglioServente.setCentro_responsabilita(pdgSpe.getAltro_cdr());
			dettaglioServente.setCentro_responsabilita_clgs(pdgSpe.getCentro_responsabilita());
			dettaglioServente.setDt_registrazione(getHome(userContext, pdgSpe).getServerTimestamp());
			dettaglioServente.setDescrizione(pdgSpe.getDescrizione());
			dettaglioServente.setElemento_voce(pdgSpe.getElemento_voce());
			dettaglioServente.setElemento_voce_clgs(pdgSpe.getElemento_voce());
			dettaglioServente.setEsercizio(pdgSpe.getEsercizio());
			dettaglioServente.setPdg_variazione(pdgSpe.getPdg_variazione());
			// Creo l'associazione variazione_cdr_uo
			if (pdgSpe.getPdg_variazione() != null){
				Ass_pdg_variazione_cdrHome home = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class); 
				Ass_pdg_variazione_cdrBulk ass_pdg_variazione = new Ass_pdg_variazione_cdrBulk(pdgSpe.getPdg_variazione().getEsercizio(),pdgSpe.getPdg_variazione().getPg_variazione_pdg(),pdgSpe.getAltro_cdr().getCd_centro_responsabilita());
				Ass_pdg_variazione_cdrBulk ass_pdg_variazione_esistente = (Ass_pdg_variazione_cdrBulk)home.findByPrimaryKey(ass_pdg_variazione);
				if (ass_pdg_variazione_esistente == null){
					ass_pdg_variazione.setToBeCreated();
					super.creaConBulk(userContext,ass_pdg_variazione);					
				}
			}
			// I dettagli di carico sono MODIFICABILI (per la sola parte pagamenti) R137R
			// Date: 10/04/2002
			dettaglioServente.setFl_sola_lettura(new Boolean(false));
			dettaglioServente.setLinea_attivita_clgs(pdgSpe.getLinea_attivita());
			dettaglioServente.setOrigine(pdgSpe.getOrigine());
			dettaglioServente.setPg_spesa_clgs(pdgSpe.getPg_spesa());
			dettaglioServente.setUser(userContext.getUser());
			

			if( isVoceSAUOP(userContext, pdgSpe.getElemento_voce()) ) {
				dettaglioServente.setLinea_attivita(
					((it.cnr.contab.config00.ejb.Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
							"CNRCONFIG00_EJB_Linea_attivitaComponentSession", 
							it.cnr.contab.config00.ejb.Linea_attivitaComponentSession.class)
					).creaLineaAttivitaSAUOP(
						userContext,
						pdg.getCentro_responsabilita()
					)
				);
			} else {
				dettaglioServente.setLinea_attivita(
					((it.cnr.contab.config00.ejb.Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
							"CNRCONFIG00_EJB_Linea_attivitaComponentSession", 
							it.cnr.contab.config00.ejb.Linea_attivitaComponentSession.class)
					).creaLineaAttivitaSAUO(
						userContext,
						pdgSpe.getAltro_cdr(),
						pdgSpe.getLinea_attivita()
					)
				);
			}
		} else {

			try {
				dettaglioServente = (Pdg_preventivo_spe_detBulk)getHome(userContext,Pdg_preventivo_spe_detBulk.class).findByPrimaryKey( new Pdg_preventivo_spe_detBulk(
																																					pdgSpe.getCd_centro_responsabilita_clgs(),
																																					pdgSpe.getCd_elemento_voce_clgs(),
																																					pdgSpe.getCd_linea_attivita_clgs(),
																																					pdgSpe.getEsercizio(),
																																					pdgSpe.getPg_spesa_clgs(),
																																					pdgSpe.getTi_appartenenza_clgs(),
																																					pdgSpe.getTi_gestione_clgs()) );
			} catch(Throwable e) {
				throw new it.cnr.jada.comp.ComponentException(e);
			}
		}

		dettaglioServente.setStato(pdgSpe.ST_NESSUNA_AZIONE);

		copiaImportiAltraUO(userContext,pdgSpe,dettaglioServente);

		pdgSpe.setStato(Pdg_preventivo_detBulk.ST_NESSUNA_AZIONE);

		if(newDett) {
			if(pdgSpe.isToBeCreated())
				insertBulk(userContext, pdgSpe);
			else
				updateBulk(userContext, pdgSpe);

			dettaglioServente.completaImportiNulli();
			dettaglioServente.setPg_spesa_clgs(pdgSpe.getPg_spesa());
			insertBulk(userContext, dettaglioServente);

			pdgSpe.setCentro_responsabilita_clgs(dettaglioServente.getCentro_responsabilita());
			pdgSpe.setElemento_voce_clgs(dettaglioServente.getElemento_voce());
			pdgSpe.setLinea_attivita_clgs(dettaglioServente.getLinea_attivita());
			pdgSpe.setPg_spesa_clgs(dettaglioServente.getPg_spesa());
			pdgSpe.setCategoria_dettaglio(dettaglioServente.CAT_SCARICO);
		} else {
			updateBulk(userContext, dettaglioServente);
		}

		updateBulk(userContext, pdgSpe);

	} catch(Throwable e) {
		throw handleException(e);
	}

	return pdgSpe;
}

/**
 *  Genera o modifica un dettaglio di spesa per entrata figurativa e il suo collegato
 *
 *  @param userContext
 *  @param pdgSpe		dettaglio del servito
 */

private Pdg_preventivo_spe_detBulk creaSCRVersoCdR(UserContext userContext,Pdg_preventivo_spe_detBulk pdgSpe) throws it.cnr.jada.comp.ComponentException {

	boolean newDett;

	if(!pdgSpe.isROaltra_uo()) {
		newDett = true;
	} else if(pdgSpe.isToBeUpdated()){
		newDett = false;
	} else return pdgSpe;

	if(newDett) {
		if(!isVoceCSSAC(userContext, pdgSpe.getElemento_voce()))
			throw new it.cnr.jada.comp.ApplicationException("La voce del piano dei conti deve essere \"Prestazioni da struttura dell'ente\"");
	}
	
	if (pdgSpe.getIm_raa_a2_costi_finali().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rac_a2_spese_odc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rae_a2_spese_ogc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rah_a3_costi_finali().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_ral_a3_spese_odc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_ran_a3_spese_ogc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rh_ccs_costi().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_ri_ccs_spese_odc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rm_css_ammortamenti().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rn_css_rimanenze().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_ro_css_altri_costi().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rk_ccs_spese_ogc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rq_ssc_costi_odc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rs_ssc_costi_ogc().compareTo(ZERO) != 0 ||
		pdgSpe.getIm_rv_pagamenti().compareTo(ZERO) != 0)
	{
		throw new it.cnr.jada.comp.ApplicationException("Sono stati valorizzati degli importi non compatibili con il campo 'Verso altro CDR'");
	}

	Pdg_preventivoBulk pdg;
	if(pdgSpe.getAltro_cdr() == null) {
		throw new it.cnr.jada.comp.ApplicationException("CdR altra UO non indicato");
	} else {
		try {
			pdg = (Pdg_preventivoBulk)getHome(userContext,Pdg_preventivoBulk.class).findByPrimaryKey(new Pdg_preventivoBulk(pdgSpe.getEsercizio(),pdgSpe.getAltro_cdr().getCd_centro_responsabilita()));
		} catch(Throwable e) {
			throw new it.cnr.jada.comp.ComponentException(e);
		}
	}
	if(pdg == null)
		throw new it.cnr.jada.comp.ApplicationException("CdR altra UO non valido");

	// Controllo chiusura PDG servente
	if (pdgSpe.OR_PROPOSTA_VARIAZIONE.equals(pdgSpe.getOrigine()))
		checkChiusuraPdgPerVariazioni(userContext,pdg);
	else
		checkChiusuraPdg(userContext,pdg,pdgSpe);

	try {

		Pdg_preventivo_etr_detBulk dettaglioServente;
		if(newDett) {
			dettaglioServente = new Pdg_preventivo_etr_detBulk();
			dettaglioServente.setCategoria_dettaglio(pdgSpe.CAT_CARICO);
			dettaglioServente.setCentro_responsabilita(pdgSpe.getAltro_cdr());
			dettaglioServente.setCentro_responsabilita_clgs(pdgSpe.getCentro_responsabilita());
			dettaglioServente.setDt_registrazione(getHome(userContext, pdgSpe).getServerTimestamp());
			dettaglioServente.setDescrizione(pdgSpe.getDescrizione());
			dettaglioServente.setElemento_voce(getVoceRicaviFigurativi(userContext));
			dettaglioServente.setElemento_voce_clgs(pdgSpe.getElemento_voce());
			dettaglioServente.setEsercizio(pdgSpe.getEsercizio());
			dettaglioServente.setFl_sola_lettura(new Boolean(true));
			dettaglioServente.setLinea_attivita_clgs(pdgSpe.getLinea_attivita());
			dettaglioServente.setOrigine(pdgSpe.getOrigine());
			dettaglioServente.setPg_spesa_clgs(pdgSpe.getPg_spesa());
			dettaglioServente.setUser(userContext.getUser());

			dettaglioServente.setLinea_attivita(
				((it.cnr.contab.config00.ejb.Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						"CNRCONFIG00_EJB_Linea_attivitaComponentSession", 
						it.cnr.contab.config00.ejb.Linea_attivitaComponentSession.class)
				).creaLineaAttivitaCSSAC(
					userContext,
					pdgSpe.getAltro_cdr(),
					pdgSpe.getLinea_attivita()
				)
			);
		} else {

			try {
				dettaglioServente = (Pdg_preventivo_etr_detBulk)getHome(userContext,Pdg_preventivo_etr_detBulk.class).findByPrimaryKey( new Pdg_preventivo_etr_detBulk(
																																					pdgSpe.getCd_centro_responsabilita_clge(),
																																					pdgSpe.getCd_elemento_voce_clge(),
																																					pdgSpe.getCd_linea_attivita_clge(),
																																					pdgSpe.getEsercizio(),
																																					pdgSpe.getPg_entrata_clge(),
																																					pdgSpe.getTi_appartenenza_clge(),
																																					pdgSpe.getTi_gestione_clge() ) );
			} catch(Throwable e) {
				throw new it.cnr.jada.comp.ComponentException(e);
			}
		}

		dettaglioServente.setStato(pdgSpe.ST_NESSUNA_AZIONE);

		copiaImportAltroCDR(userContext,pdgSpe,dettaglioServente);

		pdgSpe.setStato(Pdg_preventivo_detBulk.ST_NESSUNA_AZIONE);

		if(newDett) {
			if(pdgSpe.isToBeCreated())
				insertBulk(userContext, pdgSpe);
			else
				updateBulk(userContext, pdgSpe);

			dettaglioServente.completaImportiNulli();
			dettaglioServente.setPg_spesa_clgs(pdgSpe.getPg_spesa());
			insertBulk(userContext, dettaglioServente);

			pdgSpe.setCentro_responsabilita_clge(dettaglioServente.getCentro_responsabilita());
			pdgSpe.setElemento_voce_clge(dettaglioServente.getElemento_voce());
			pdgSpe.setLinea_attivita_clge(dettaglioServente.getLinea_attivita());
			pdgSpe.setPg_entrata_clge(dettaglioServente.getPg_entrata());
			pdgSpe.setCategoria_dettaglio(dettaglioServente.CAT_SCARICO);
		} else {
			updateBulk(userContext, dettaglioServente);
		}

		updateBulk(userContext, pdgSpe);

	} catch(Throwable e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}
	return pdgSpe;
}

//^^@@
/** 
  *  eliminazione dettagli a partire dalla linea di attivià
  *    PreCondition:
  *      Viene richiesta l'eliminazione dei dettagli di spesa ed entrata che corrispondono ad esercizio, CdR e LA indicati.
  *    PostCondition:
  *      Vengono lanciate due delete una per i dettagli di spesa ed una per quelli di entrata per i record che soddisfano le condizioni.
 */
//^^@@
	public Pdg_preventivoBulk delDetByLA(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {

		checkChiusuraPdg(userContext, pdg, null);

		CdrBulk cdr = cdrFromUserContext(userContext);
     	checkLivelloResponsabilita(userContext, cdr, pdg);

		if(pdg.getLinea_attivita_eliminazione() == null || pdg.getLinea_attivita_eliminazione().getCd_linea_attivita() == null)
			throw new it.cnr.jada.comp.ApplicationException("GAE indicata per l'eliminazione non valida.");

		try {
			String delSpe = "DELETE FROM " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "PDG_PREVENTIVO_SPE_DET WHERE " +
							"ESERCIZIO = " + pdg.getEsercizio() + " AND " +
							"CD_CENTRO_RESPONSABILITA = '" + pdg.getCd_centro_responsabilita() + "' AND " +
							"CD_LINEA_ATTIVITA = '" + pdg.getLinea_attivita_eliminazione().getCd_linea_attivita() + "' AND "+
							"CATEGORIA_DETTAGLIO = '" + Pdg_preventivo_detBulk.CAT_SINGOLO + "'";

			String delEtr = "DELETE FROM " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "PDG_PREVENTIVO_ETR_DET WHERE " +
							"ESERCIZIO = " + pdg.getEsercizio() + " AND " +
							"CD_CENTRO_RESPONSABILITA = '" + pdg.getCd_centro_responsabilita() + "' AND " +
							"CD_LINEA_ATTIVITA = '" + pdg.getLinea_attivita_eliminazione().getCd_linea_attivita() + "' AND "+
							"CATEGORIA_DETTAGLIO = '" + Pdg_preventivo_detBulk.CAT_SINGOLO + "'";

			if (Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI.equals(pdg.getStato()) ||
				Pdg_preventivoBulk.ST_H_PRECHIUSURA_PER_VARIAZIONI.equals(pdg.getStato())) {
				delSpe = delSpe + " AND ORIGINE = '"+Pdg_preventivo_detBulk.OR_PROPOSTA_VARIAZIONE+"'";
				delEtr = delEtr + " AND ORIGINE = '"+Pdg_preventivo_detBulk.OR_PROPOSTA_VARIAZIONE+"'";
			}

			java.sql.Statement cs = getConnection( userContext ).createStatement();
			try {
				lockBulk(userContext,pdg);
				if(pdg.getLinea_attivita_eliminazione().getTi_gestione().equals(it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE))
				 cs.execute(delSpe);
				if(pdg.getLinea_attivita_eliminazione().getTi_gestione().equals(it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk.TI_GESTIONE_ENTRATE))
				 cs.execute(delEtr);
				return caricaPdg(userContext,pdg.getCentro_responsabilita());
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

//^^@@
/** 
  *  PDG non modificabile
  *    PreCondition:
  *      checkLivelloResponsabilita(pdg,pdg_det) genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione generata
  *  Dettaglio scaricato o caricato
  *    PreCondition:
  *      CATEGORIA = 'SCR' o 'CAR'
  *    PostCondition:
  *      Genera una eccezione con messaggio: "Il dettaglio non può essere eliminato"
  *  PDG già chiuso
  *    PreCondition:
  *      checkChiusuraPdg(pdg_det) genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione generata
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Elimina il dettaglio del pdg specificato
 */
//^^@@

	public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException {
		Pdg_preventivo_detBulk dett = (Pdg_preventivo_detBulk)bulk;
		CdrBulk cdr             = cdrFromUserContext(userContext);

        Pdg_preventivoBulk pdg = checkChiusuraPdg(userContext,dett);

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare dettagli del pdg ad esercizio chiuso.");

        checkLivelloResponsabilita(userContext, cdr, pdg);

		if(dett.getFl_sola_lettura().booleanValue())
			throw new it.cnr.jada.comp.ApplicationException("Il dettaglio non può essere eliminato");

		if(Pdg_preventivo_detBulk.CAT_SCARICO.equals( dett.getCategoria_dettaglio())&&
		   dett.getPdg_variazione() == null)
				throw new it.cnr.jada.comp.ApplicationException("Il dettaglio è stato scaricato e non può essere eliminato; contattare il CDR servente");
/*
		if(Pdg_preventivo_detBulk.CAT_SCARICO.equals( dett.getCategoria_dettaglio()) &&
			Pdg_preventivo_detBulk.ST_CONFERMA.equals( dett.getStato() ))
				throw new it.cnr.jada.comp.ApplicationException("Il dettaglio è stato confermato dal centro servente e non può essere eliminato; contattare il CDR servente");
*/

		if(Pdg_preventivo_detBulk.CAT_CARICO.equals( dett.getCategoria_dettaglio() )) {
		  //	if(Pdg_preventivo_detBulk.ST_NESSUNA_AZIONE.equals( dett.getStato() ))
//		 throw new it.cnr.jada.comp.ApplicationException("Il dettaglio non è eliminabile fino alla chiusura della contrattazione. Contattare il CDR servente");
		 throw new it.cnr.jada.comp.ApplicationException("Il dettaglio è di carico e non può essere eliminato");
/*
			if(Pdg_preventivo_detBulk.ST_CONFERMA.equals( dett.getStato() ))
				throw new it.cnr.jada.comp.ApplicationException("Il dettaglio non è eliminabile perchè confermato.");
				
			Pdg_preventivo_spe_detBulk servente = new Pdg_preventivo_spe_detBulk();

			servente.setEsercizio(dett.getEsercizio());
			servente.setCentro_responsabilita(dett.getCentro_responsabilita_clgs());
			servente.setElemento_voce(dett.getElemento_voce_clgs());
			servente.setLinea_attivita(dett.getLinea_attivita_clgs());
			servente.setPg_spesa(dett.getPg_spesa_clgs());

			// Controllo che il pdg servente sia chiuso
			checkChiusuraPdg(userContext,servente);
			try {
				getHome(userContext,servente).delete(servente);
			} catch(PersistencyException e) {
				throw handleException((OggettoBulk)dett,e);
			}
*/

		}

		super.eliminaConBulk(userContext,bulk);
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il CDR PERSONALE da configurazione CNR
  *    PostCondition:
  *      Restituisce il cdr con il codice specificato in CONFIGURAZIONE_CNR alla voce 
  *      "CDR_SPECIALE" , "CDR_PERSONALE"
 */
//^^@@
	private CdrBulk getCDR_PERSONALE (UserContext userContext) throws ComponentException {
		try {
            String aCdCdr =  Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext)))
					.orElseThrow(()->new it.cnr.jada.comp.ApplicationException("Centro di responsabilità del Personale non definito in Configurazione CNR per l'esercizio "+CNRUserContext.getEsercizio(userContext)+"."));
			it.cnr.contab.config00.sto.bulk.CdrHome cdrHome = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext,CdrBulk.class, "V_CDR_VALIDO");
			SQLBuilder sql = cdrHome.createSQLBuilder();
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
 			sql.addClause(FindClause.AND, "cd_centro_responsabilita", SQLBuilder.EQUALS, aCdCdr);
					it.cnr.jada.persistency.Broker broker = cdrHome.createBroker(sql);

			CdrBulk aCdrBulk;
			if(broker.next())
			 aCdrBulk = (CdrBulk)broker.fetch(CdrBulk.class);
			else
			 throw new it.cnr.jada.comp.ApplicationException("Il CDR del personale specificato in CONFIGURAZIONE CNR non è stato trovato!");

			return aCdrBulk;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il CDR SPECIALE SERVIZIO ENTE da configurazione CNR
  *    PostCondition:
  *      Restituisce il cdr con il codice specificato in CONFIGURAZIONE_CNR alla voce 
  *      "CDR_SPECIALE" , "CDR_SERVIZIO_ENTE"
 */
//^^@@
	private CdrBulk getCDR_SERVIZIO_ENTE (UserContext userContext) throws ComponentException {
		try {
			String aCdCdr = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).getCdrServizioEnte(CNRUserContext.getEsercizio(userContext)))
					.orElseThrow(()->new it.cnr.jada.comp.ApplicationException("Cdr di Servizio dell'Ente non trovato in Configurazione CNR per l'esercizio "+CNRUserContext.getEsercizio(userContext)+"."));
			it.cnr.contab.config00.sto.bulk.CdrHome cdrHome = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext,CdrBulk.class, "V_CDR_VALIDO");
			SQLBuilder sql = cdrHome.createSQLBuilder();
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
 			sql.addClause(FindClause.AND, "cd_centro_responsabilita", SQLBuilder.EQUALS, aCdCdr);
					it.cnr.jada.persistency.Broker broker = cdrHome.createBroker(sql);

			CdrBulk aCdrBulk;
			if(broker.next())
			 aCdrBulk = (CdrBulk)broker.fetch(CdrBulk.class);
			else
			 throw new it.cnr.jada.comp.ApplicationException("Il CDR di servizio per l'ENTE specificato in CONFIGURAZIONE CNR non è stato trovato!");

			return aCdrBulk;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la voce di tipo ONERI_CNR da configurazione CNR
  *    PostCondition:
  *      Restituisce l'elemento voce con il codice specificato in CONFIGURAZIONE_CNR alla voce 
  *      "ELEMENTO_VOCE_SPECIALE" , "ONERI_CNR"
 */
//^^@@
	private Elemento_voceBulk getVoceONERI_CNR (UserContext userContext) throws ComponentException {
		try {
			Configurazione_cnrComponentSession configurazione = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",Configurazione_cnrComponentSession.class);
			String aCdOneriCnr=configurazione.getVal01(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),null,"ELEMENTO_VOCE_SPECIALE","ONERI_CNR");
			if(aCdOneriCnr==null)
			 throw new it.cnr.jada.comp.ApplicationException("Voce Oneri Cnr non definita in CONFIGURAZIONE CNR per l'esercizio corrente");
			return (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(
				aCdOneriCnr,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
				Elemento_voceHome.APPARTENENZA_CDS,
				Elemento_voceHome.GESTIONE_SPESE
				));
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta  la voce da assegnare al dettaglio di entrata creato sul PDG del CDR servente nel caso di un costo senza spese
  *    PostCondition:
  *      Restituisce l'elemento voce con il codice specificato in CONFIGURAZIONE_CNR alla voce 
  *      "ELEMENTO_VOCE_SPECIALE" , "RICAVO_FIGURATIVO_ALTRO_CDR"
 */
//^^@@
	private Elemento_voceBulk getVoceRicaviFigurativi (UserContext userContext) throws ComponentException {
		try {
			Configurazione_cnrComponentSession configurazione = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",Configurazione_cnrComponentSession.class);
			String aVoceRicFig = configurazione.getVal01(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),null,"ELEMENTO_VOCE_SPECIALE","RICAVO_FIGURATIVO_ALTRO_CDR");
            if(aVoceRicFig==null)
             throw new it.cnr.jada.comp.ApplicationException("Voce del piano \"Ricavo Figurativo\" non presente in configurazione CNR per l'esercizio corrente");
			return (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(
				aVoceRicFig,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
				Elemento_voceHome.APPARTENENZA_CNR,
				Elemento_voceHome.GESTIONE_ENTRATE
				));
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la voce di tipo TFR da configurazione CNR
  *    PostCondition:
  *      Restituisce l'elemento voce con il codice specificato in CONFIGURAZIONE_CNR alla voce 
  *      "ELEMENTO_VOCE_SPECIALE" , "TFR"
 */
//^^@@
	private Elemento_voceBulk getVoceTFR (UserContext userContext) throws ComponentException {
		try {
			Configurazione_cnrComponentSession configurazione = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",Configurazione_cnrComponentSession.class);
			String aCdTfr=configurazione.getVal01(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),null,"ELEMENTO_VOCE_SPECIALE","TFR");
			if(aCdTfr==null)
			 throw new it.cnr.jada.comp.ApplicationException("Voce TFR non definita in CONFIGURAZIONE CNR per l'esercizio corrente");
			return (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(
				aCdTfr,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
				Elemento_voceHome.APPARTENENZA_CDS,
				Elemento_voceHome.GESTIONE_SPESE
				));
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

/**
 * Metodo per i controlli da effettuare su un oggetto di tipo {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk } .
 *
 * Nome: Controlli per inserimento e modifica;
 * Pre:  Implementare tutti i controlli che garantiscano l'integrita del record rappresentato dall oggetto;
 * Post: Verifica che la linea di attività e l'elemento voce non siano nulli.
 *
 * @param pdg Oggetto da analizzare.
 *
 * @return L'oggetto controllato.
 *
 * @throw it.cnr.jada.comp.ComponentException
 */
private void init(UserContext userContext,Pdg_preventivoBulk pdg,Pdg_preventivo_detBulk pdgDett) throws it.cnr.jada.comp.ComponentException {
	try {
		if(pdgDett.getLinea_attivita() == null || pdgDett.getLinea_attivita().getCd_linea_attivita() == null)
			throw new it.cnr.jada.comp.ApplicationException("Il GAE non può essere nullo.");

		if(pdgDett.getElemento_voce() == null || pdgDett.getElemento_voce().getCd_elemento_voce() == null)
			throw new it.cnr.jada.comp.ApplicationException("Voce del piano non può essere nulla.");

		// Carico tutti gli oggetti complessi legati al pdg
		getHomeCache(userContext).fetchAll(userContext);

		if (pdgDett.getDt_registrazione() == null)
			pdgDett.setDt_registrazione(getHome(userContext, (OggettoBulk)pdgDett).getServerTimestamp());

		if (pdgDett.getOrigine() == null) 
			pdgDett.setOrigine(pdg.isStatoVariazionePdG() || pdgDett.isDaVariazione()? pdgDett.OR_PROPOSTA_VARIAZIONE : pdgDett.OR_UTENTE);
		/**
		 * Controllo lo stato del PDG solo se il dettaglio non proviene da una variazione 
		 */	
        if (!pdgDett.isDaVariazione())
		  checkChiusuraPdg(userContext, pdg, pdgDett);

		if (pdg.isStatoVariazionePdG()&& !pdgDett.isOriginePropostaVariazione())
			throw new it.cnr.jada.comp.ApplicationException("Il pdg è aperto per variazioni. E' possibile modificare o inserire solamente proposte di variazione");
			
		checkLivelloResponsabilita(userContext, cdrFromUserContext(userContext), pdg);

		pdgDett.completaImportiNulli();

		if ("5".equals( pdgDett.getLinea_attivita().getNatura().getCd_natura())) {
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			if (uoHome.findAssociazioneUoArea(userContext, pdgDett.getCentro_responsabilita().getUnita_padre()).isEmpty())
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare dettagli con natura 5 perchè il CDR non è collegato ad area attraverso la sua unità organizzativa");
			if (isCdrArea(userContext,pdgDett.getCentro_responsabilita()))
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare dettagli con natura 5 perchè il CDR appartiene ad un'area");
		}
		
		if(pdgDett instanceof it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)
			initSpe(userContext,pdg,(Pdg_preventivo_spe_detBulk)pdgDett);
		else if(pdgDett instanceof it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk)
			initEtr(userContext,pdg,(Pdg_preventivo_etr_detBulk)pdgDett);
	} catch(Throwable e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}
}

/**
 * Metodo per i controlli da effettuare su un oggetto di tipo {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk } .
 *
 * Nome: Controlli per inserimento e modifica;
 * Pre:  Implementare tutti i controlli che garantiscano l'integrita del record rappresentato dall oggetto;
 * Post: Verifica che la linea di attività e l'elemento voce non siano nulli.
 *
 * @param pdg Oggetto da analizzare.
 *
 * @return L'oggetto controllato.
 *
 * @throw it.cnr.jada.comp.ComponentException
 */
private OggettoBulk initEtr(UserContext userContext,Pdg_preventivoBulk pdg,Pdg_preventivo_etr_detBulk pdg_det) throws it.cnr.jada.comp.ComponentException {

	
	if ("5".equals( pdg_det.getLinea_attivita().getNatura().getCd_natura()))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare dettagli di entrata con natura 5");

	boolean importiNegativi = 
		(pdg_det.getIm_ra_rce().signum() < 0) ||
		(pdg_det.getIm_rb_rse().signum() < 0) ||
		(pdg_det.getIm_rc_esr().signum() < 0) ||
		(pdg_det.getIm_rd_a2_ricavi().signum() < 0) ||
		(pdg_det.getIm_re_a2_entrate().signum() < 0) ||
		(pdg_det.getIm_rf_a3_ricavi().signum() < 0) ||
		(pdg_det.getIm_rg_a3_entrate().signum() < 0);
	
	if (pdg.ST_G_APERTURA_PER_VARIAZIONI.equals(pdg.getStato()) || pdg_det.getPdg_variazione() != null) {
		boolean importiPositivi = 
			(pdg_det.getIm_ra_rce().signum() > 0) ||
			(pdg_det.getIm_rb_rse().signum() > 0) ||
			(pdg_det.getIm_rc_esr().signum() > 0) ||
			(pdg_det.getIm_rd_a2_ricavi().signum() > 0) ||
			(pdg_det.getIm_re_a2_entrate().signum() > 0) ||
			(pdg_det.getIm_rf_a3_ricavi().signum() > 0) ||
			(pdg_det.getIm_rg_a3_entrate().signum() > 0);
		if (importiPositivi && importiNegativi)
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire sia importi positivi che negativi.");
	} else if (importiNegativi) {
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire importi negativi");
	}
	return pdg_det;
}

	/**
 * Metodo per i controlli da effettuare su un oggetto di tipo {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk } .
 *
 * Nome: Controlli per inserimento e modifica;
 * Pre:  Implementare tutti i controlli che garantiscano l'integrita del record rappresentato dall oggetto;
 * Post: Verifica che la linea di attività e l'elemento voce non siano nulli.
 *
 * @param pdg_det Oggetto da analizzare.
 *
 * @return L'oggetto controllato.
 *
 * @throw it.cnr.jada.comp.ComponentException
 */

private OggettoBulk initSpe(UserContext userContext,Pdg_preventivoBulk pdg,Pdg_preventivo_spe_detBulk pdg_det) throws it.cnr.jada.comp.ComponentException {

	// Controllo che non siano imputati importi negativi

	boolean importiNegativi = 
		(pdg_det.getIm_raa_a2_costi_finali().signum() < 0) ||
		(pdg_det.getIm_rab_a2_costi_altro_cdr().signum() < 0) ||
		(pdg_det.getIm_rac_a2_spese_odc().signum() < 0) ||
		(pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rae_a2_spese_ogc().signum() < 0) ||
		(pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rag_a2_spese_costi_altrui().signum() < 0) ||
		(pdg_det.getIm_rah_a3_costi_finali().signum() < 0) ||
		(pdg_det.getIm_rai_a3_costi_altro_cdr().signum() < 0) ||
		(pdg_det.getIm_ral_a3_spese_odc().signum() < 0) ||
		(pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_ran_a3_spese_ogc().signum() < 0) ||
		(pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rap_a3_spese_costi_altrui().signum() < 0) ||
		(pdg_det.getIm_rh_ccs_costi().signum() < 0) ||
		(pdg_det.getIm_ri_ccs_spese_odc().signum() < 0) ||
		(pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rk_ccs_spese_ogc().signum() < 0) ||
		(pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rm_css_ammortamenti().signum() < 0) ||
		(pdg_det.getIm_rn_css_rimanenze().signum() < 0) ||
		(pdg_det.getIm_ro_css_altri_costi().signum() < 0) ||
		(pdg_det.getIm_rp_css_verso_altro_cdr().signum() < 0) ||
		(pdg_det.getIm_rq_ssc_costi_odc().signum() < 0) ||
		(pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_rs_ssc_costi_ogc().signum() < 0) ||
		(pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() < 0) ||
		(pdg_det.getIm_ru_spese_costi_altrui().signum() < 0) ||
		(pdg_det.getIm_rv_pagamenti().signum() < 0);

	if (pdg.ST_G_APERTURA_PER_VARIAZIONI.equals(pdg.getStato()) || pdg_det.getPdg_variazione() != null) {
		boolean importiPositivi = 
			(pdg_det.getIm_raa_a2_costi_finali().signum() > 0) ||
			(pdg_det.getIm_rab_a2_costi_altro_cdr().signum() > 0) ||
			(pdg_det.getIm_rac_a2_spese_odc().signum() > 0) ||
			(pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rae_a2_spese_ogc().signum() > 0) ||
			(pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rag_a2_spese_costi_altrui().signum() > 0) ||
			(pdg_det.getIm_rah_a3_costi_finali().signum() > 0) ||
			(pdg_det.getIm_rai_a3_costi_altro_cdr().signum() > 0) ||
			(pdg_det.getIm_ral_a3_spese_odc().signum() > 0) ||
			(pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_ran_a3_spese_ogc().signum() > 0) ||
			(pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rap_a3_spese_costi_altrui().signum() > 0) ||
			(pdg_det.getIm_rh_ccs_costi().signum() > 0) ||
			(pdg_det.getIm_ri_ccs_spese_odc().signum() > 0) ||
			(pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rk_ccs_spese_ogc().signum() > 0) ||
			(pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rm_css_ammortamenti().signum() > 0) ||
			(pdg_det.getIm_rn_css_rimanenze().signum() > 0) ||
			(pdg_det.getIm_ro_css_altri_costi().signum() > 0) ||
			(pdg_det.getIm_rp_css_verso_altro_cdr().signum() > 0) ||
			(pdg_det.getIm_rq_ssc_costi_odc().signum() > 0) ||
			(pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_rs_ssc_costi_ogc().signum() > 0) ||
			(pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() > 0) ||
			(pdg_det.getIm_ru_spese_costi_altrui().signum() > 0) ||
			(pdg_det.getIm_rv_pagamenti().signum() > 0);

		if (importiPositivi && importiNegativi)
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire sia importi positivi che negativi.");
	} else if (importiNegativi) {
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire importi negativi");
	}
        
        // Richiesta CNR 137R - Solo la colonna pagamenti è modificabile per dettagli di PDG di spesa di CARICO del servente
        if(pdg_det.getCategoria_dettaglio().equals(pdg_det.CAT_CARICO) && pdg_det.isNotNew())  {
          Pdg_preventivo_spe_detBulk aOrigSpe;
          try {
         aOrigSpe=(Pdg_preventivo_spe_detBulk)getHome(userContext,pdg_det).findByPrimaryKey(pdg_det);
          } catch (PersistencyException e) {
		throw new it.cnr.jada.comp.ApplicationException("Errore di lettura del dettaglio di carico originale");	       
      }
          if(

               (pdg_det.getIm_rag_a2_spese_costi_altrui().compareTo(pdg_det.getIm_rag_a2_spese_costi_altrui()) != 0)
            || (pdg_det.getIm_rap_a3_spese_costi_altrui().compareTo(pdg_det.getIm_rap_a3_spese_costi_altrui()) != 0)
            || (pdg_det.getIm_ru_spese_costi_altrui().compareTo(pdg_det.getIm_ru_spese_costi_altrui()) != 0)

            || (pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0)
            || (pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0)
            || (pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0)
            || (pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0)
            || (pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0)
            || (pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0)
            || (pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0)
            || (pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0)

            || (pdg_det.getIm_raa_a2_costi_finali().signum() != 0)
		|| (pdg_det.getIm_rab_a2_costi_altro_cdr().signum() != 0)
		|| (pdg_det.getIm_rac_a2_spese_odc().signum() != 0)
            || (pdg_det.getIm_rae_a2_spese_ogc().signum() != 0)
            || (pdg_det.getIm_rah_a3_costi_finali().signum() != 0)
            || (pdg_det.getIm_rai_a3_costi_altro_cdr().signum() != 0)
            || (pdg_det.getIm_ral_a3_spese_odc().signum() != 0)
            || (pdg_det.getIm_ran_a3_spese_ogc().signum() != 0)
            || (pdg_det.getIm_rh_ccs_costi().signum() != 0)
            || (pdg_det.getIm_ri_ccs_spese_odc().signum() != 0)
            || (pdg_det.getIm_rk_ccs_spese_ogc().signum() != 0)
            || (pdg_det.getIm_rm_css_ammortamenti().signum() != 0)
            || (pdg_det.getIm_rn_css_rimanenze().signum() != 0)
            || (pdg_det.getIm_ro_css_altri_costi().signum() != 0)
            || (pdg_det.getIm_rp_css_verso_altro_cdr().signum() != 0)
            || (pdg_det.getIm_rq_ssc_costi_odc().signum() != 0)
            || (pdg_det.getIm_rs_ssc_costi_ogc().signum() != 0)
          ) {
		throw new it.cnr.jada.comp.ApplicationException("Per i dettagli di carico del servente solo la colonna pagamenti (V) può essere modificata");
    }}

        
//Controllo campi valorizzati
	boolean control = false;
	if( pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0 ) {
		control = true;
	}
	if( pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0 ) {
		if(control)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare più campi verso altra UO nello stesso anno");
		else
			control = true;
	}
	if( pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0 ) {
		if(control)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare più campi verso altra UO nello stesso anno");
		else
			control = true;
	}
	if( pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0
		&&
		control)
	{
		throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare più campi verso altra UO nello stesso anno");
	}

	if( pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0 
		&& pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0 ) {
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare più campi verso altra UO nello stesso anno");
	}

	if( pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0 
		&& pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0 ) {
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare più campi verso altra UO nello stesso anno");
	}

	if( (pdg_det.getIm_rp_css_verso_altro_cdr().signum() != 0
		|| pdg_det.getIm_rab_a2_costi_altro_cdr().signum() != 0
		|| pdg_det.getIm_rai_a3_costi_altro_cdr().signum() != 0)
		&&
		(pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0) )
	{
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare campi verso altra UO e verso altro CdR contemporaneamente");
	}

	// Aggiunto controllo di non imputabilità di colonne di scarico (altra UO e CDR) e colonne non di scarico
	// eccetto COSTI (H) per la quadratura

	if(   (
		      pdg_det.getIm_rp_css_verso_altro_cdr().signum() != 0
		   || pdg_det.getIm_rab_a2_costi_altro_cdr().signum() != 0
		   || pdg_det.getIm_rai_a3_costi_altro_cdr().signum() != 0
		   || pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0
		   || pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0
	       || pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0
		   || pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0
		   || pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0
		   || pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0
		   || pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0
		   || pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0
	      )
	   && ( // manca H per questioni di quadratura e costi altrui perchè NON imputabili direttamente
                  pdg_det.getIm_ri_ccs_spese_odc().signum() != 0		   
               || pdg_det.getIm_rk_ccs_spese_ogc().signum() != 0		   
               || pdg_det.getIm_rm_css_ammortamenti().signum() != 0		   
               || pdg_det.getIm_rn_css_rimanenze().signum() != 0		   
               || pdg_det.getIm_ro_css_altri_costi().signum() != 0		   
               || pdg_det.getIm_rq_ssc_costi_odc().signum() != 0		   
               || pdg_det.getIm_rs_ssc_costi_ogc().signum() != 0		   
               || pdg_det.getIm_rv_pagamenti().signum() != 0		   
// Fix del 2002/02/12 - Come per la colonna H anche le corrispondenti pluriennali sono valorizzabili
//               || pdg_det.getIm_raa_a2_costi_finali().signum() != 0		   
               || pdg_det.getIm_rac_a2_spese_odc().signum() != 0		   
               || pdg_det.getIm_rae_a2_spese_ogc().signum() != 0		   
//               || pdg_det.getIm_rah_a3_costi_finali().signum() != 0		   
               || pdg_det.getIm_ral_a3_spese_odc().signum() != 0		   
               || pdg_det.getIm_ran_a3_spese_ogc().signum() != 0		   
	      )
	  )
	{
			throw new it.cnr.jada.comp.ApplicationException("Impossibile valorizzare campi verso altra UO o CDR e contemporaneamente campi propri");
	}		
		
	if(	pdg_det.getAltro_cdr() != null && pdg_det.getAltro_cdr().getCd_centro_responsabilita() != null
		&& pdg_det.getIm_rp_css_verso_altro_cdr().signum() == 0
		&& pdg_det.getIm_rab_a2_costi_altro_cdr().signum() == 0
		&& pdg_det.getIm_rai_a3_costi_altro_cdr().signum() == 0
		&& pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() == 0
		&& pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() == 0
		&& pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() == 0
		&& pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() == 0
		&& pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() == 0
		&& pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() == 0
		&& pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() == 0
		&& pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() == 0 
        // Richiesta CNR 137R - Solo la colonna pagamenti è modificabile per dettagli di PDG di spesa di CARICO del servente
            && !(pdg_det.getCategoria_dettaglio().equals(pdg_det.CAT_CARICO) && pdg_det.isNotNew())			
    )
	{
			throw new it.cnr.jada.comp.ApplicationException("Non sono stati impostati importi validi da riferire all'\"Altro CdR\" indicato");
	}
	
        
	if(pdg_det.getIm_rh_ccs_costi().compareTo(
					pdg_det.getIm_ri_ccs_spese_odc().add(
						pdg_det.getIm_rj_ccs_spese_odc_altra_uo().add(
							pdg_det.getIm_rk_ccs_spese_ogc().add(
								pdg_det.getIm_rl_ccs_spese_ogc_altra_uo()
							)
						)
					)
			) != 0
	)
	{
		throw new it.cnr.jada.comp.ApplicationException("Il campo \"Costi\" non contiene la somma dei campi relativi ai Costi Con Spese");
	}


	// Controlli sulle colonne nel caso i conti derivino da interfaccia stipendi

	// Leggo CDR e VOCI DEL PIANO COINVOLTE
	try {
	 CdrBulk aCdrPersonaleBulk = getCDR_PERSONALE(userContext);
	 CdrBulk aCdrServizioEnteBulk = getCDR_SERVIZIO_ENTE(userContext);
         Elemento_voceBulk aTFRBulk = getVoceTFR(userContext);
         Elemento_voceBulk aONERICNRBulk = getVoceONERI_CNR(userContext);

         Pdg_preventivo_spe_detHome aSpeHome = (Pdg_preventivo_spe_detHome)getHome(userContext, pdg_det.getClass());
         
         Vector aV = aSpeHome.getVociStipendiali(userContext, pdg_det);

         if(!pdg_det.getElemento_voce().getCd_elemento_voce().equals(aTFRBulk.getCd_elemento_voce())) { 
   		  if(
   		 !(// Su tutti i CDR tranne quello del personale e quello speciale
	   		  pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrPersonaleBulk) 
   		   || pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrServizioEnteBulk)
   		  )   
	    ) {
	     for (int i=0;i<aV.size();i++) {
              if(aV.get(i).equals("IND")) {
               if(
                 pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0
              || pdg_det.getIm_rv_pagamenti().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per la voce selezionata non è possibile imputare importi sulle colonne L e V");                 
          }
              if(aV.get(i).equals("DET")) {
               if(
                 pdg_det.getIm_ro_css_altri_costi().signum() != 0
              || pdg_det.getIm_rv_pagamenti().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per la voce selezionata non è possibile imputare importi sulle colonne O e V");                 
          }		     
		 }   
		}   
   		  if( // sul CDR del personale
   		 pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrPersonaleBulk) 
	    ) {
	     for (int i=0;i<aV.size();i++) {
              if(aV.get(i).equals("IND")) {
               if(
                 pdg_det.getIm_rk_ccs_spese_ogc().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per il CDR del Personale sulla voce selezionata non è possibile imputare importi sulla colonna K");                 
          }
              if(aV.get(i).equals("DET")) {
               if(
                 pdg_det.getIm_ro_css_altri_costi().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per il CDR del Personale sulla voce selezionata non è possibile imputare importi sulla colonna O");                 
          }		     
		 }   
		}   
	 } else { // ... nel caso di voce TFR
          // Incontro telefonico del 29/01/2002 - Per il TFR il controllo è sulla colonna O degli altri costi + V (solo per cdr != Personale e Speciale)
   		  if(
   		 !(// Su tutti i CDR tranne quello del personale e quello speciale
	   		  pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrPersonaleBulk) 
   		   || pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrServizioEnteBulk)
   		  )   
	    ) {
               if(
                 pdg_det.getIm_ro_css_altri_costi().signum() != 0
              || pdg_det.getIm_rv_pagamenti().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per la voce TFR non è possibile imputare importi sulle colonne O e V");                 
		}   
   		  if( // sul CDR del personale
   		 pdg_det.getCentro_responsabilita().equalsByPrimaryKey(aCdrPersonaleBulk) 
	    ) {
               if(
                 pdg_det.getIm_ro_css_altri_costi().signum() != 0
             ) throw new it.cnr.jada.comp.ApplicationException("Per il CDR del Personale sulla voce TFR non è possibile imputare importi sulla colonna O");                 
		}   
	 }

   		} catch(Throwable e) {
				throw new it.cnr.jada.comp.ComponentException(e);
	}
     
	if( isVoceSAUOP(userContext, pdg_det.getElemento_voce()) ) {
		if( !( pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0
			|| pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0
			|| pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0
			|| pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0
			|| pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0
			|| pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0
			|| pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0
			|| pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0) )
		{
			try {
				Pdg_preventivoHome pdgHome = (Pdg_preventivoHome)getHome(userContext,Pdg_preventivoBulk.class, "V_PDG_PDG_SAUOP");
				SQLBuilder sql = pdgHome.createSQLBuilder();
				sql.addClause("AND", "esercizio", sql.EQUALS, pdg_det.getEsercizio());
				it.cnr.jada.persistency.Broker broker = pdgHome.createBroker(sql);

				Pdg_preventivoBulk pdg_saup;
				if(broker.next())
					pdg_saup = (Pdg_preventivoBulk)broker.fetch(Pdg_preventivoBulk.class);
				else
					throw new it.cnr.jada.comp.ApplicationException("Il PdG per lo scarico dei costi del personale non disponibile.");

				getHome(userContext,CdrBulk.class).findByPrimaryKey(pdg_saup.getCentro_responsabilita());

				if(!pdg_saup.getCentro_responsabilita().equalsByPrimaryKey( pdg_det.getCentro_responsabilita() ))
					throw new it.cnr.jada.comp.ApplicationException("Per effettuare uno scarico dei costi del personale è necessario valorizzare i campi altra UO.");
			} catch(Throwable e) {
				throw new it.cnr.jada.comp.ComponentException(e);
			}
		}
    
   }
  
//FINE Controllo campi valorizzati

//Gestione campi "verso altra UO"
	if( pdg_det.getIm_rj_ccs_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rl_ccs_spese_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_rr_ssc_costi_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rt_ssc_costi_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_rad_a2_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_raf_a2_spese_ogc_altra_uo().signum() != 0
		|| pdg_det.getIm_ram_a3_spese_odc_altra_uo().signum() != 0
		|| pdg_det.getIm_rao_a3_spese_ogc_altra_uo().signum() != 0
	) {
		// Richiesta scarico su AREA, minuta C6-40-0091 - Tolto il blocco
		// if ("5".equals( pdg_det.getLinea_attivita().getNatura().getCd_natura()))
		//	throw new it.cnr.jada.comp.ApplicationException("Non è possibile scaricare costi su altra UO con natura 5");
		creaSCRAltraUO(userContext, pdg_det);
	}

//Gestione campi "verso altro CdR"
	if( pdg_det.getIm_rp_css_verso_altro_cdr().signum() != 0
		|| pdg_det.getIm_rab_a2_costi_altro_cdr().signum() != 0
		|| pdg_det.getIm_rai_a3_costi_altro_cdr().signum() != 0
	) {
		if ("5".equals( pdg_det.getLinea_attivita().getNatura().getCd_natura()))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile scaricare costi su altro CDR con natura 5");
		creaSCRVersoCdR(userContext, pdg_det);
	} else if (pdg_det.getElemento_voce() != null && isVoceCSSAC(userContext,pdg_det.getElemento_voce())) {
	    //  Richiesta CNR 111R La voce prestazioni da strutture dell'ente (CSSAC) deve essere abilitata in scrittura anche nelle colonne proprie
		//	throw new it.cnr.jada.comp.ApplicationException("La voce 'Prestazioni da struttura dell'ente' può essere usata solo per scaricare costi senza spese verso altro CDR");
	}
	return pdg_det;
}

/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la preparazione dei dati relativi all'Aggregato.
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB050.inizializzaAggregatoPDG
 */
private void inizializzaAggregatoPDG(UserContext userContext,Pdg_preventivoBulk pdg,int livelloResponsabilita) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
	if (livelloResponsabilita == 1 || isCdrArea(userContext,pdg.getCentro_responsabilita()))
		try {
			lockBulk(userContext,pdg);
			LoggableStatement cs = new LoggableStatement(getConnection(userContext), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
					+ "CNRCTB050.inizializzaAggregatoPDG(?,?,?)}",false,this.getClass());
			try {
				cs.setObject( 1, pdg.getEsercizio() );
				cs.setString( 2, pdg.getCd_centro_responsabilita());
				cs.setObject( 3, userContext.getUser());
				cs.execute();
			} finally {
			    cs.close();
			}
		} catch (Throwable e) {
			throw handleException(pdg,e);
		}
}
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la preparazione dei dati relativi all'Aggregato.
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB050.inizializzaAggregatoPDG
 */
private void inizializzaAggregatoPDGPerVariazioni(UserContext userContext,Pdg_preventivoBulk pdg,int livelloResponsabilita) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
	if (livelloResponsabilita == 1 || isCdrArea(userContext,pdg.getCentro_responsabilita()))
		try {
			lockBulk(userContext,pdg);
			LoggableStatement cs =new LoggableStatement(getConnection(userContext), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB050.inizializzaAggregatoPDG(?,?,?)}",false,this.getClass());
			try {
				cs.setObject( 1, pdg.getEsercizio() );
				cs.setString( 2, pdg.getCd_centro_responsabilita());
				cs.setObject( 3, userContext.getUser());
				cs.execute();
			} finally {
			    cs.close();
			}
		} catch (Throwable e) {
			throw handleException(pdg,e);
		}
}
/** 
  *  normale
  *    PreCondition:
  *      Impostare il CdR di appartenenza
  *    PostCondition:
  *      Vengono pre impostati l'esercizio e il CdR da userContext, viene preimpostato ORIGINE a 'DIR'
 */

public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	Pdg_preventivo_detBulk pdg_det = (Pdg_preventivo_detBulk)bulk;
	
	pdg_det.setFl_sola_lettura(new Boolean(false));
	pdg_det.setCategoria_dettaglio(Pdg_preventivo_detBulk.CAT_SINGOLO);
	pdg_det.setStato(Pdg_preventivo_detBulk.ST_CONFERMA);

	return super.inizializzaBulkPerInserimento(userContext,bulk);
}
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		bulk = super.inizializzaBulkPerModifica(userContext,bulk);

		Pdg_preventivo_detBulk pdg_det = (Pdg_preventivo_detBulk)bulk;
		
		Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)getHome(userContext,Pdg_preventivoBulk.class).findByPrimaryKey(new Pdg_preventivoBulk(pdg_det.getEsercizio(),pdg_det.getCd_centro_responsabilita()));

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg))
			bulk = asRO(bulk,"Dettaglio non modificabile ad esercizio chiuso.");
		else if ((!isPdgModificabile(userContext,pdg) ||
			(pdg.isStatoVariazionePdG() && !pdg_det.isOriginePropostaVariazione()) ||
			(!pdg.isStatoVariazionePdG() && !pdg_det.isOrigineDefinitivo())) && (!pdg_det.isDaVariazione()))
			// Il dettaglio è di sola lettura se:
			// - Il pdg non è modificabile (per lo stato o per il livello dell'utente
			// - il pdg è in uno stato di variazione e il dettaglio è un dettaglio definitivo o da stipendi
			// - il pdg non è in uno stato di variazione e il dettaglio non è un dettaglio definitivo

			bulk = asRO(bulk,null);

		return bulk;
	} catch(Throwable e) {
		throw handleException(e);
	}
}

/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_spese_ldaBulk stampa) 
	throws ComponentException {

	// Imposta l'Esercizio come quello di scrivania
	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));

	CdrBulk cdrUtente = cdrFromUserContext(userContext);
	Unita_organizzativaBulk uoPadre = null;
	
	try{
		uoPadre = (Unita_organizzativaBulk)getHome(userContext, cdrUtente.getUnita_padre()).findByPrimaryKey(cdrUtente.getUnita_padre());
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new it.cnr.jada.comp.ComponentException(pe);
	}

	stampa.setCdrUtente(cdrUtente);
	
	stampa.setLivello_Responsabilita(getLivelloResponsabilitaCDR(userContext, cdrUtente));


	
	stampa.setCd_cds(uoPadre.getCd_unita_padre());
	
	//stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
	
	stampa.setCdrForPrint(new CdrBulk());
	stampa.setIsCdrForPrintEnabled(true);
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_vpg_bilancio_riclassVBulk stampa) 
	throws ComponentException {

	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	//stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	
	stampa.setTi_ist_com(Stampa_vpg_bilancio_riclassVBulk.TIPO_IST_COM);

	if (stampa instanceof Stampa_vpg_stato_patrim_riclassVBulk){
		((Stampa_vpg_stato_patrim_riclassVBulk)stampa).setTi_att_pass(Stampa_vpg_stato_patrim_riclassVBulk.TIPO_ATTIVITA);
	}
	
	try{
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		
		CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
			stampa.setCdsForPrint(cds);
			stampa.setCdsForPrintEnabled(false);
		} else {
			stampa.setCdsForPrint(new CdsBulk());
			stampa.setCdsForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
	
	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setUoForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUoForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_libro_giornaleBulk stampa) 
	throws ComponentException {
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setTipologia(Stampa_libro_giornaleBulk.TIPO_TUTTO);
	try{
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		
		CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
			stampa.setCdsForPrint(cds);
			stampa.setCdsForPrintEnabled(false);
		} else {
			stampa.setCdsForPrint(new CdsBulk());
			stampa.setCdsForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
	
	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setUoForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUoForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_pdg_etr_speVBulk stampa) 
	throws ComponentException {
	stampa.inizializzaRagruppamenti();
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setTi_etr_spe(Stampa_pdg_etr_speVBulk.TIPO_SPESA);
	try{
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		
		CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
			stampa.setCdsForPrint(cds);
			stampa.setCdsForPrintEnabled(false);
		} else {
			stampa.setCdsForPrint(new CdsBulk());
			stampa.setCdsForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
	
	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setUoForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUoForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}

private void inizializzaBulkPerStampa(UserContext userContext, Stampa_situazione_analitica_x_GAEBulk stampa) throws ComponentException {
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setTi_etr_spe(stampa.TIPO_ENTRATA);
try{
	String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
	
	CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
	CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	
	if (!cds.getCd_unita_organizzativa().equals(cd_cds_scrivania)){
		stampa.setCdsForPrint(cds);
		stampa.setCdsForPrintEnabled(true);
	} else {
		stampa.setCdsForPrintEnabled(false);
	}
		
} catch (it.cnr.jada.persistency.PersistencyException pe){
	throw new ComponentException(pe);
}	

}
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_rendiconto_finanziarioVBulk stampa) throws ComponentException {
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setTi_etr_spe(Stampa_rendiconto_finanziarioVBulk.TIPO_ENTRATA);
	stampa.setTipo_stampa(Stampa_rendiconto_finanziarioVBulk.GESTIONALE);
	stampa.inizializzaRagruppamenti();
	try{
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		
		CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		if (!cds.getCd_unita_organizzativa().equals(cd_cds_scrivania)){
			stampa.setCds(cds);
		}
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
}

/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_spese_ldaBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_spese_ldaBulk)bulk);
	else if (bulk instanceof Stampa_vpg_bilancio_riclassVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_vpg_bilancio_riclassVBulk)bulk);
	else if (bulk instanceof Stampa_pdg_etr_speVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_pdg_etr_speVBulk)bulk);
	else if (bulk instanceof Stampa_libro_giornaleBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_libro_giornaleBulk)bulk);
	else if (bulk instanceof Stampa_situazione_analitica_x_GAEBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_situazione_analitica_x_GAEBulk)bulk);
	else if (bulk instanceof Stampa_rendiconto_finanziarioVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_rendiconto_finanziarioVBulk)bulk);
	else if (bulk instanceof Stampa_situazione_sintetica_x_progettoBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_situazione_sintetica_x_progettoBulk)bulk);
	return bulk;
}
	
/**
 *  PDG occupato
 *    PreCondition:
 *      Il tentativo di mettere un lock sul pdg fallisce
 *    PostCondition:
 *      Lascia uscire l'eccezione
 *  PDG in stato A,B, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è A o B e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) != 0
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato Ci, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è Ci e getLivelloResponsabilitaCDR(cdrFromUserContext()) != i
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato D,E,G,H utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è D,E,G o H e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) < 0
 *    PostCondition:
 *      Restituisce true
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Restituisce false
 */
public boolean isDettagliPdGModificabili(UserContext userContext, Pdg_preventivoBulk pdg) throws ComponentException {

	try {
		BulkHome home = getHome(userContext,pdg);
		// rileggo e metto un lock sul pdg
		pdg = (Pdg_preventivoBulk)home.findByPrimaryKey(pdg);
		home.lock(pdg);

		return isPdgModificabile(userContext,pdg);
	} catch(Throwable e) {
		throw handleException(e);
	}
}

/**
 *  A -> B, livello < 3
 *    PreCondition:
 *      statoAttuale = A, nuovoStato = B, livello < 3
 *    PostCondition:
 *      Restituisce true
 *  A -> C, livello = 3
 *    PreCondition:
 *      statoAttuale = A, nuovoStato = C, livello = 3
 *    PostCondition:
 *      Restituisce true
 *  B -> C
 *    PreCondition:
 *      statoAttuale = B, nuovoStato = C
 *    PostCondition:
 *      Restituisce true
 *  C -> F
 *    PreCondition:
 *      statoAttuale = C, nuovoStato = F
 *    PostCondition:
 *      Restituisce true
 *  C -> Ci
 *    PreCondition:
 *      statoAttuale = C, nuovoStato = Ci
 *    PostCondition:
 *      Restituisce true
 *  C -> D, livello < 3
 *    PreCondition:
 *      statoAttuale = C, nuovoStato = D, livello < 3
 *    PostCondition:
 *      Restituisce true
 *  C -> E, livello = 3
 *    PreCondition:
 *      statoAttuale = C, nuovoStato = E, livello = 3
 *    PostCondition:
 *      Restituisce true
 *  Ci -> C
 *    PreCondition:
 *      statoAttuale = Ci, nuovoStato = C
 *    PostCondition:
 *      Restituisce true
 *  A -> B, livello < 3
 *    PreCondition:
 *      statoAttuale = A, nuovoStato = B, livello < 3
 *    PostCondition:
 *      Restituisce true
 *  D -> C
 *    PreCondition:
 *      statoAttuale = D, nuovoStato = C
 *    PostCondition:
 *      Restituisce true
 *  D -> E
 *    PreCondition:
 *      statoAttuale = D, nuovoStato = E
 *    PostCondition:
 *      Restituisce true
 *  E -> D, livello < 3
 *    PreCondition:
 *      statoAttuale = E, nuovoStato = D, livello < 3
 *    PostCondition:
 *      Restituisce true
 *  E -> C, livello = 3
 *    PreCondition:
 *      statoAttuale = E, nuovoStato = C, livello = 3
 *    PostCondition:
 *      Restituisce true
 *  F -> G
 *    PreCondition:
 *      statoAttuale = F, nuovoStato = G
 *    PostCondition:
 *      Restituisce true
 *  G -> H, livello < 3
 *    PreCondition:
 *      statoAttuale = G, nuovoStato = H, livello < 3
 *    PostCondition:
 *      Restituisce true
 *  G -> M, livello = 3
 *    PreCondition:
 *      statoAttuale = G, nuovoStato = M, livello = 3
 *    PostCondition:
 *      Restituisce true
 *  H -> M
 *    PreCondition:
 *      statoAttuale = H, nuovoStato = M
 *    PostCondition:
 *      Restituisce true
 *  M -> G
 *    PreCondition:
 *      statoAttuale = M, nuovoStato = G
 *    PostCondition:
 *      Restituisce true
 *  M -> F
 *    PreCondition:
 *      statoAttuale = M, nuovoStato = F
 *    PostCondition:
 *      Restituisce true
 *  Stato non compatibile
 *    PreCondition:
 *      Nessun'altra precondizione verificata
 *    PostCondition:
 *      Restituisce false
 */
private boolean isStatoCompatibile (String statoAttuale, int livello, String nuovoStato) {

	if( Pdg_preventivoBulk.ST_A_CREAZIONE.equals( statoAttuale ) ) {
		if( livello < 3 ) {
			return Pdg_preventivoBulk.ST_B_MODIFICA.equals( nuovoStato );
		} else if( livello == 3) {
			return Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( nuovoStato );
		}
	}

	if( Pdg_preventivoBulk.ST_B_MODIFICA.equals( statoAttuale )) {
		return Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( nuovoStato );
	}

	if( Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( statoAttuale ) ) {
		return (Pdg_preventivoBulk.ST_F_CHIUSO_DFNT.equals( nuovoStato ) ||
				Pdg_preventivoBulk.ST_CX_MODIFICA.equals( nuovoStato ) ||
				(	livello == 3 &&
					Pdg_preventivoBulk.ST_E_CHIUSO.equals( nuovoStato )) ||
				(	livello != 3 &&
					Pdg_preventivoBulk.ST_D_CHIUSURA_I.equals( nuovoStato ))
				);
	}

	if(	Pdg_preventivoBulk.ST_C0_MODIFICA_AC.equals( statoAttuale )
		||
		Pdg_preventivoBulk.ST_C1_MODIFC_CDRI.equals( statoAttuale )
		||
		Pdg_preventivoBulk.ST_C2_MODIFIC_RUO.equals( statoAttuale ))
	{
		return Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( nuovoStato );
	}

	if( Pdg_preventivoBulk.ST_D_CHIUSURA_I.equals( statoAttuale )) {
		return (Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( nuovoStato ) ||
				Pdg_preventivoBulk.ST_E_CHIUSO.equals( nuovoStato ));
	}

	if( Pdg_preventivoBulk.ST_E_CHIUSO.equals( statoAttuale ) ) {
		if( livello == 3) {
			return Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals( nuovoStato );
		} else if( livello < 3) {
			return Pdg_preventivoBulk.ST_D_CHIUSURA_I.equals( nuovoStato );
		}
	}

	if( Pdg_preventivoBulk.ST_F_CHIUSO_DFNT.equals( statoAttuale )) {
		return Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI.equals( nuovoStato );
	}

	if( Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI.equals( statoAttuale )) {
		if( livello == 3) {
			return Pdg_preventivoBulk.ST_M_MODIFICATO_PER_VARIAZIONI.equals( nuovoStato );
		} else if( livello < 3) {
			return Pdg_preventivoBulk.ST_H_PRECHIUSURA_PER_VARIAZIONI.equals( nuovoStato );
		}
	}

	if( Pdg_preventivoBulk.ST_H_PRECHIUSURA_PER_VARIAZIONI.equals( statoAttuale )) {
		return Pdg_preventivoBulk.ST_M_MODIFICATO_PER_VARIAZIONI.equals( nuovoStato );
	}

	if( Pdg_preventivoBulk.ST_M_MODIFICATO_PER_VARIAZIONI.equals( statoAttuale )) {
		return 
			Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI.equals( nuovoStato ) ||
			Pdg_preventivoBulk.ST_F_CHIUSO_DFNT.equals( nuovoStato );
	}

	return false;
}

//^^@@
/** 
  *  Voce valida
  *    PreCondition:
  *      La voce specificata ha codice uguale a quello presente nella tabella CONFIGURAZIONE_CNR alla voce "ELEMENTO_VOCE_SPECIALE" , "PRESTAZIONI_DA_STRUTTURE_DELL_ENTE"
  *    PostCondition:
  *      Ritorna false
  *  Voce non valida
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Ritorna false
 */
//^^@@
	private boolean isVoceCSSAC (UserContext userContext,Elemento_voceBulk voce) throws ComponentException {
		try {
			Configurazione_cnrComponentSession configurazione = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",Configurazione_cnrComponentSession.class);
			return voce.getCd_elemento_voce().equals(configurazione.getVal01(userContext, voce.getEsercizio(),null,"ELEMENTO_VOCE_SPECIALE","PRESTAZIONI_DA_STRUTTURE_DELL_ENTE"));
		} catch(javax.ejb.EJBException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

//^^@@
/** 
  *  voce SAUOP
  *    PreCondition:
  *      La voce specificata è SAUOP ( fl_voce_personale = Y )
  *      
  *    PostCondition:
  *      Ritorna true
  *  Voce non SAUOP
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Ritorna false
 */
//^^@@
	private boolean isVoceSAUOP (UserContext userContext,Elemento_voceBulk voce) {
		return voce.getFl_voce_personale() != null && voce.getFl_voce_personale().booleanValue();
	}

//^^@@
/** 
  *  CDR NRUO
  *    PreCondition:
  *      L'utente in userContext appartiene a un CDR NRUO (getLivelloResponsabilità ritorna 3)
  *    PostCondition:
  *      Restituisce un SQL builder per selezionare il solo CDR a cui appartiene l'utente
  *  CDR RUO
  *    PreCondition:
  *      L'utente in userContext appartiene a un CDR RUO (getLivelloResponsabilità ritorna 2)
  *    PostCondition:
  *      Restituisce un SQL builder per selezionare il CDR di apparteneza più tutti i CDR della sua UO;
  *  CDR I
  *    PreCondition:
  *      L'utente in userContext appartiene a un CDR I (getLivelloResponsabilità ritorna 1)
  *    PostCondition:
  *      Restituisce un SQL builder per selezionare il CDR di appartenenza più tutti i CDR della sua UO; se il CDR appartiene alla UOCDS restituisce anche tutti i CDR delle UO che non hanno CDR di I livello.
  *  AC
  *    PreCondition:
  *      L'utente in userContext appartiene è appartiene al CDR AC
  *    PostCondition:
  *      Restituisce un SQL builder per selezionare tutti i CDR escluso sè stesso
 */
//^^@@

	private SQLBuilder listaCdrPdGPerUtente (UserContext userContext) throws it.cnr.jada.comp.ComponentException {

			CdrBulk cdr = cdrFromUserContext(userContext);
			int livelloResponsabilita = getLivelloResponsabilitaCDR(userContext, cdr);

			//lista dei CDR visibili all'utente
			Vector lista = new Vector();

/*			
			if (livelloResponsabilita == LV_NRUO) {
				lista.add(cdr);
				return lista;
			}
*/

			it.cnr.contab.config00.sto.bulk.CdrHome home;
			it.cnr.jada.persistency.sql.SQLBuilder sql;

			if (livelloResponsabilita == LV_AC) {
				home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class,"V_CDR_VALIDO","none");
				sql = home.createSQLBuilder();
			} else {
//				lista.add(cdr);
				home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class, "V_PDG_CDR_FIGLI_PADRE","none");
				sql = home.createSQLBuilder();
				sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cdr.getCd_centro_responsabilita());
			}
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

			return sql;
//			lista.addAll(home.fetchAll(sql));
//			return lista;
	}
	
	
	
	private SQLBuilder listaCdrPdGPerUtenteIncludeEnte (UserContext userContext) throws it.cnr.jada.comp.ComponentException {

		CdrBulk cdr = cdrFromUserContext(userContext);
		Optional.ofNullable(cdr).orElseThrow(()->new ApplicationException("Errore: CDR di Scrivania non individuato!"));
		int livelloResponsabilita = getLivelloResponsabilitaCDR(userContext, cdr);

		//lista dei CDR visibili all'utente
		Vector lista = new Vector();


		it.cnr.contab.config00.sto.bulk.CdrHome home;
		it.cnr.jada.persistency.sql.SQLBuilder sql;

		if (livelloResponsabilita == LV_AC) {
			home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class,"V_CDR_VALIDO","none");
			sql = home.createSQLBuilderEsteso();
		} else {

			home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class, "V_PDG_CDR_FIGLI_PADRE","none");
			sql = home.createSQLBuilderEsteso();
			sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cdr.getCd_centro_responsabilita());
		}
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

		return sql;
}
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      
  *    PostCondition:
  *      Restituisce una lista dei PDG dei CDR il cui valore del livello di responsabilità è maggiore stretto di quello del CDR specificato
 */
//^^@@

	private java.util.List listaPdGDipendenti (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
		CdrBulk cdr = pdg.getCentro_responsabilita();
		int liv = getLivelloResponsabilitaCDR(userContext, cdr);
		if(liv == LV_AC) return null; //AC non ha un PdG per tanto va bloccato

		it.cnr.jada.bulk.BulkHome home;
		if(liv == LV_RUO || liv == LV_CDRI) home = getHome(userContext, Pdg_preventivoBulk.class, "V_PDG_PDG_FIGLI");
		else return java.util.Collections.EMPTY_LIST;
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cdr.getCd_centro_responsabilita());

		try {
			return new Vector(getHome(userContext,Pdg_preventivoBulk.class).fetchAll(sql));
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}

/**
 *  Carica la lista degli stati accessibili in base a quello attuale del PdG
 */

	private java.util.Dictionary loadStatiKeys (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
		java.util.Dictionary list = new it.cnr.jada.util.OrderedHashtable();

		if(pdg != null && pdg.getStato() != null && !pdg.getStato().equals("") ) {
			java.util.Enumeration ek = pdg.STATI.keys();
			java.util.Enumeration el = pdg.STATI.elements();
			int livelloCdR = getLivelloResponsabilitaCDR( userContext, pdg.getCentro_responsabilita() );
			while( ek.hasMoreElements() ) {
				String sk = (String)ek.nextElement();
				String sl = (String)el.nextElement();

				if(isStatoCompatibile( pdg.getStato(), livelloCdR, sk )
					|| pdg.getStato().equals( sk ))
				{
						list.put(sk, sl);
				}
			}
		}

		return list;
	}

/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Il dettaglio viene salvato
  *  Dettaglio scaricato, già chiuso
  *    PreCondition:
  *      PDG modificabile dall'utente, CATEGORIA = 'SCR', stato != 'X'
  *    PostCondition:
  *      Genera una eccezione con messaggio: "Il dettaglio è già stato contrattato e non è più modificabile. Contattare il CDR servito"
  *  PDG non modificabile
  *    PreCondition:
  *      checkLivelloResponsabilita(pdg,pdg_det) genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione generata
  *  Dettaglio scaricato
  *    PreCondition:
  *      PDG modificabile dall'utente, CATEGORIA = 'SCR', stato = 'X'
  *    PostCondition:
  *      Salva il dettaglio e modificato lo stato del dettaglio collegato
  *  Dettaglio caricato entrata
  *    PreCondition:
  *      PDG modificabile dall'utente, CATEGORIA = 'CAR'
  *    PostCondition:
  *      Genera una ApplicationException con il messaggio "Il dettaglio non è modificabile. Contattare il CDR servente"
  *  PDG già chiuso
  *    PreCondition:
  *      checkChiusuraPdg(pdg_det) genera una eccezione
  *    PostCondition:
  *      Lascia uscire l'eccezione generata
  *  Dettaglio caricato spesa
  *    PreCondition:
  *      PDG modificabile dall'utente, CATEGORIA = 'CAR', e viene richiesto di modificare una colonna diversa da colonna pagamenti (V)
  *    PostCondition:
  *      Genera una ApplicationException con il messaggio: "Per i dettagli di carico del serevnte solo la colonna pagamenti (V) puo' essere modificata"
 */

public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {
    Pdg_preventivo_detBulk pdgDett = (Pdg_preventivo_detBulk)bulk;

    if (Pdg_preventivo_detBulk.CAT_SCARICO.equals(pdgDett.getCategoria_dettaglio()) &&
		!Pdg_preventivo_detBulk.ST_NESSUNA_AZIONE.equals( pdgDett.getStato()))
		throw new it.cnr.jada.comp.ApplicationException("Il dettaglio è già stato contrattato e non è più modificabile. Contattare il CDR servito");

	// Richiesta CNR 137R Solo i dettagli di spesa, sotto certe condizioni, sono modificabili
	if(pdgDett instanceof it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk && Pdg_preventivo_detBulk.CAT_CARICO.equals( pdgDett.getCategoria_dettaglio()))
		throw new it.cnr.jada.comp.ApplicationException("Il dettaglio non è modificabile. Contattare il CDR servente");

	try {
		Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)getHome(userContext, Pdg_preventivoBulk.class).findAndLock(
			new Pdg_preventivoBulk(
				pdgDett.getEsercizio(),
				pdgDett.getCd_centro_responsabilita()));

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare dettagli del pdg ad esercizio chiuso.");

		init(userContext,pdg,pdgDett);

		super.modificaConBulk(userContext, (OggettoBulk)pdgDett);

		validaVariazione(userContext, bulk);
		validaVariazione(userContext, bulk);

		/*
		 * Il controllo che la somma degli importi aggregati sia positiva non deve avvenire
		 * per i dettagli provenienti da variazione al PDG non ancora in stato approvato
		 */
		if (!pdgDett.isDaVariazione()) 
			controllaAggregatoPdgDetPositivo(userContext,pdg,pdgDett);
		else
		{ 
			Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)getHome(userContext, Pdg_variazioneBulk.class).findByPrimaryKey(
			new Pdg_variazioneBulk(
				pdgDett.getPdg_variazione().getEsercizio(),
				pdgDett.getPdg_variazione().getPg_variazione_pdg()));
		  if (pdgVar.isApprovata())
			controllaAggregatoPdgDetPositivo(userContext,pdg,pdgDett);
		}

		return bulk;
	} catch(OutdatedResourceException e) {
		throw handleException(e);
	} catch(PersistencyException e) {
		throw handleException(e);
	} catch(BusyResourceException e) {
		throw handleException(e);
	}
}
/**
 *  Richiesto cambiamento di stato, Pdg occupato
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg che risulta occupato (lock)
 *    PostCondition:
 *      Viene generata una BusyResourceException
 *  Richiesto cambiamento di stato, nuovo stato non compatibile con lo stato attuale
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg, ma lo stato specificato non è compatibile con lo stato
 *		attuale del pdg (secondo isStatoCompatibile())
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Stato X non compatibile con l'attuale stato Y del Pdg"
 *  Richiesto cambiamento di stato, livello utente insufficiente
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg, ma il livello dell'utente non è sufficiente per effettuare l'operazione (confrontaLivelloResponsabilita() < 0)
 *    PostCondition:
 *      Viene generata una ApplicationException con il messaggio "Utente non abilitato ad operare sul PDG"
 *  Richiesto cambiamento di stato, modifica di stato non riuscita
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg e il passaggio di stato provoca qualche errore applicativo (secondo modificaStatoPdG_X_Y() per 
 *    PostCondition:
 *      Viene lasciato uscire l'errore generato
 *  Richiesto cambiamento in stato C, esistono discrepanza sugli insiemi di linee attività
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg, nessuna delle altre precondizioni è verificata ma esistono discrepanze sugli importi entrate/spese legati a insiemi di linee di attività (secondo controllaDiscrepanzeInsiemeLa())
 *    PostCondition:
 *      Viene effettuata la modifica di stato e viene restituito il messaggio di avvertimento "Esistono discrepanze tra gli importi entrate - spese legati ad insieme di l.a. "
 *  Richiesto cambiamento di stato da C a F
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, nessuna delle altre pecondizioni è verificata
 *    PostCondition:
 *      Viene creata la variazione di bilancio automatica per la spalmatura delle entrate CNR (creaRipartEntrate())
 *  Normale
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, nessuna delle altre pecondizioni è verificata
 *    PostCondition:
 *      Viene creata la variazione di bilancio automatica per la spalmatura delle entrate CNR (creaRipartEntrate())
 */

public OggettoBulk modificaStatoPdG (UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ComponentException {
	try {
		Pdg_preventivoBulk oldPdg = (Pdg_preventivoBulk)getHome(userContext,pdg).findAndLock(pdg);

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare lo stato del pdg ad esercizio chiuso.");

		String nuovoStato = pdg.getStato();
		String vecchioStato = oldPdg.getStato();
		int livelloResponsabilitaPdg = getLivelloResponsabilitaCDR(userContext, oldPdg.getCentro_responsabilita());
		// 09/06/2005
		// Aggiunto controllo sul cambiamento di stato da quello finale, obbligo l'utilizzo delle variazioni al PDG
		if (vecchioStato.equals(Pdg_preventivoBulk.ST_F_CHIUSO_DFNT))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare lo stato del pdg poichè è in chiusura definitiva.");

		// Controllo il nuovo stato del pdg è compatibile con lo stato attuale
		if( !isStatoCompatibile(vecchioStato, livelloResponsabilitaPdg, nuovoStato) )
			throw new it.cnr.jada.comp.ApplicationException("Stato \"" + nuovoStato + "\" non compatibile con l'attuale stato \"" + vecchioStato + "\" del PdG");

		CdrBulk cdrUtente = cdrFromUserContext(userContext);
		int livelloResponsabilitaUtente = getLivelloResponsabilitaCDR(userContext,cdrUtente);

		// Controllo se il livello di responsabilità dell'utente è sufficiente per cambiare lo stato
		// del pdg
		if(confrontaLivelloResponsabilita(userContext, cdrUtente,pdg.getCentro_responsabilita(),livelloResponsabilitaUtente,livelloResponsabilitaPdg) < 0)
			utenteNonAbilitato();

		if(pdg.ST_CX_MODIFICA.equals(nuovoStato)) {
			if (livelloResponsabilitaUtente == LV_NRUO)
				utenteNonAbilitato();
			nuovoStato = Pdg_preventivoBulk.ST_C_CHIUSURA_II + livelloResponsabilitaUtente;
		}

		// Invoco il metodo modificaStato_x_y()
		it.cnr.jada.util.Introspector.invoke(
			this,
			"modificaStatoPdG_"+vecchioStato+"_"+nuovoStato,
			new Object[] {
				userContext,
				cdrUtente,
				pdg,
				oldPdg,
				new Integer(livelloResponsabilitaUtente),
				new Integer(livelloResponsabilitaPdg)
			});

		oldPdg.setStato(nuovoStato);
		oldPdg.setAnnotazioni(pdg.getAnnotazioni());
		oldPdg.setUser(userContext.getUser());
		updateBulk(userContext,oldPdg);

		oldPdg.setStatiKeys(loadStatiKeys(userContext, oldPdg));

		// Passando da C a F devo creare la variazione di bilancio automatica per spalmatura entrate CNR
		// Va fatta qui (non in modificaStatoPdG_C_F perchè lo stato del Pdg deve essere già F
		if (Pdg_preventivoBulk.ST_C_CHIUSURA_II.equals(vecchioStato) &&
			Pdg_preventivoBulk.ST_F_CHIUSO_DFNT.equals(nuovoStato)) {
			creaRipartEntrate(userContext, oldPdg);
		}
		
		if (pdg.ST_C_CHIUSURA_II.equals(nuovoStato) &&
			controllaDiscrepanzeInsiemeLa(userContext,pdg))
			return asMTU(oldPdg,"Esistono discrepanze tra gli importi entrate - spese legati ad insieme di l.a. ");

		return oldPdg;
        
	} catch(java.lang.reflect.InvocationTargetException e) {
		throw handleException(e.getTargetException());
	} catch(Throwable e) {
		throw handleException(e);
	}
}

/*
 *  Richiesto cambiamento di stato da A a B
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad B, pdg dipendenti non chiusi (stato <> C o F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_A_B(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da A a C, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad C, pdg dipendenti non chiusi (stato <> C o F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da A a C, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad C, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da A a C, costi del dipendente non scaricati completamente
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad C, il cdr del pdg è di 1^ livello e i costi del dipendente dell'UO non sono stati scaricati completamente (secondo controllaScaricoCDPCompleto())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Costi del personale non ancora scaricati completamente!"
 *  Richiesto cambiamento di stato da A a C, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad C, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da A a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da A ad C, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_A_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II,pdg.ST_F_CHIUSO_DFNT });

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Controlla che tutti i dipendenti siano stati scaricati completamente sul PIANO DI GESTIONE del CDR di I livello o di AREA per chiudere definitivamente
	controllaScaricoCDPCompleto(userContext,pdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDG(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da B a C, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da B a C, pdg dipendenti non chiusi (stato <> C o F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da B a C, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da B a C, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da B a C, costi del dipendente non scaricati completamente
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da B a C, il cdr del pdg è di 1^ livello e i costi del dipendente dell'UO non sono stati scaricati completamente (secondo controllaScaricoCDPCompleto())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Costi del personale non ancora scaricati completamente!"
 *  Richiesto cambiamento di stato da B a C, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da B a C, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da B a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da B a C, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_B_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II,pdg.ST_F_CHIUSO_DFNT });

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Controlla che tutti i dipendenti siano stati scaricati completamente sul PIANO DI GESTIONE del CDR di I livello o di AREA per chiudere definitivamente
	controllaScaricoCDPCompleto(userContext,pdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDG(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a C0, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad C0, pdg dipendenti non chiusi (stato <> C)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C0, stato padre non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C0, pdg padre in stato <> C0
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C0, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C0, l'utente NON appartiene al CDR dell'Amministrazione Centrale
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_C0(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che l'utente abbia lo stesso livello del Ci richiesto
	if(livelloResponsabilitaUtente != LV_AC)
		utenteNonAbilitato();

	// Controllo che il pdg padre sia in stato C0
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_C0_MODIFICA_AC });

	// Controllo che i pdg figli siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II });
	
	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a C1
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad C1, pdg dipendenti non chiusi (stato <> C)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C1
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C1, pdg padre in stato <> C1
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C1, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C1, l'utente NON appartiene ad un CDR di 1^ livello
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_C1(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che l'utente abbia lo stesso livello del Ci richiesto
	if(livelloResponsabilitaUtente != LV_CDRI)
		utenteNonAbilitato();

	// Controllo che il pdg padre sia in stato C0
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_C1_MODIFC_CDRI });

	// Controllo che i pdg figli siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a C2
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad C2, pdg dipendenti non chiusi (stato <> C)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C2
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C2, pdg padre in stato <> C2
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a C2, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a C2, l'utente NON appartiene ad un CDR di 2^ livello (RUO)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_C2(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che l'utente abbia lo stesso livello del Ci richiesto
	if(livelloResponsabilitaUtente != LV_RUO)
		utenteNonAbilitato();

	// Controllo che il pdg padre sia in stato C0
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_C2_MODIFIC_RUO });

	// Controllo che i pdg figli siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a D
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a D, pdg padre in stato <> E
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_D(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controlla che il padre sia in stato E
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_E_CHIUSO });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a E
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C a E, pdg padre in stato <> E
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_E(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controlla che il padre sia in stato E
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_E_CHIUSO });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C a F, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, pdg dipendenti non chiusi (stato <> F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da C a F, Bilancio Preventivo CNR non approvato
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, il Bilancio Preventivo CNR non è stato approvato (secondo controllaBilPrevCnrApprovato())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il bilancio preventivo CNR non risulta ancora approvato"
 *  Richiesto cambiamento di stato da C a F, Pdg aggregato aperto
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, il pdg del cdr di 1^ livello è ancora aperto (secondo controllaAggregatoChiuso)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il piano di gestione aggregato non è chiuso. Contattare il CDR di primo livello"
 *  Richiesto cambiamento di stato da C a F, esistono discrepanze nel Pdg Aggregato
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C ad F, il cdr del pdg possiede un pdg aggregato ed esistono discrepanze tra gli importi iniziali e quelli modificati nel pdg aggregato (secondo controllaDiscrepanzeAggregato())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Sono presenti importi complessivi non compatibili con i dati impostati dall'ente."
 */
public Pdg_preventivoBulk modificaStatoPdG_C_F(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato F
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_F_CHIUSO_DFNT });

	// Verificare che il bilancio finanziario CNR sia approvato (Richiesta n. 56)
	controllaBilPrevCnrApprovato(userContext, pdg);
	
	// Verificare che l'aggregato sia CHIUSO in stato B
	controllaAggregatoChiuso(userContext,pdg);			

	// Controllo della presenza di discrepanze nell'aggregato
	controllaDiscrepanzeAggregato(userContext,pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C0 a C, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C0 a C, l'utente NON appartiene al CDR dell'Amministrazione Centrale
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 *  Richiesto cambiamento di stato da C0 a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C0 a C, nessun'altra precondizione è verificata
 *    PostCondition:
 *		Viene invocato modificaStatoPdG_Ci_C
 */
public Pdg_preventivoBulk modificaStatoPdG_C0_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	if(livelloResponsabilitaUtente > LV_AC)
		utenteNonAbilitato();

	// Effettuo i controlli comuni a tutti i livelli Ci
	modificaStatoPdG_Ci_C(userContext,cdrUtente,pdg,oldPdg,livelloResponsabilitaUtente,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da C1 a C, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C1 a C, l'utente appartiene ad un CDR di livello RUO o NRUO
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 *  Richiesto cambiamento di stato da C1 a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C1 a C, nessun'altra precondizione è verificata
 *    PostCondition:
 *		Viene invocato modificaStatoPdG_Ci_C
 */
public Pdg_preventivoBulk modificaStatoPdG_C1_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	if(livelloResponsabilitaUtente > LV_CDRI)
		utenteNonAbilitato();

	// Effettuo i controlli comuni a tutti i livelli Ci
	modificaStatoPdG_Ci_C(userContext,cdrUtente,pdg,oldPdg,livelloResponsabilitaUtente,livelloResponsabilitaPdg);

	return pdg;
}
/*
 *  Richiesto cambiamento di stato da C2 a C, livello responsabilità non compatbile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C2 a C, l'utente appartiene ad un CDR di livello NRUO
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Utente non abilitato."
 *  Richiesto cambiamento di stato da C2 a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C2 a C, nessun'altra precondizione è verificata
 *    PostCondition:
 *		Viene invocato modificaStatoPdG_Ci_C
 */
public Pdg_preventivoBulk modificaStatoPdG_C2_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	if(livelloResponsabilitaUtente > LV_RUO)
		utenteNonAbilitato();

	// Effettuo i controlli comuni a tutti i livelli Ci
	modificaStatoPdG_Ci_C(userContext,cdrUtente,pdg,oldPdg,livelloResponsabilitaUtente,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da Ci a C, stato figli non compatibili
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da C0,C1,C2 ad C, pdg dipendenti non chiusi (stato <> C o F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da Ci a C, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da Ci a C, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da Ci a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da Ci a C, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_Ci_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II,pdg.ST_F_CHIUSO_DFNT });

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDG(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da D a C, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da D a C, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da D a C, costi del dipendente non scaricati completamente
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da D a C, il cdr del pdg è di 1^ livello e i costi del dipendente dell'UO non sono stati scaricati completamente (secondo controllaScaricoCDPCompleto())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Costi del personale non ancora scaricati completamente!"
 *  Richiesto cambiamento di stato da D a C, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da D a C, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da D a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da D a C, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_D_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Controlla che tutti i dipendenti siano stati scaricati completamente sul PIANO DI GESTIONE del CDR di I livello o di AREA per chiudere definitivamente
	controllaScaricoCDPCompleto(userContext,pdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDG(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da D a E, stato padre non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da D a E, pdg padre in stato <> E
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_D_E(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controlla che il padre sia in stato E
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_E_CHIUSO });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da E a C, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E a C, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da E a C, costi del dipendente non scaricati completamente
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E a C, il cdr del pdg è di 1^ livello e i costi del dipendente dell'UO non sono stati scaricati completamente (secondo controllaScaricoCDPCompleto())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Costi del personale non ancora scaricati completamente!"
 *  Richiesto cambiamento di stato da E a C, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E a C, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da E a C
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E a C, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_E_C(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Controlla che tutti i dipendenti siano stati scaricati completamente sul PIANO DI GESTIONE del CDR di I livello o di AREA per chiudere definitivamente
	controllaScaricoCDPCompleto(userContext,pdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDG(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da E a D, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E ad D, pdg dipendenti non chiusi (stato <> C o F)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da E a D, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da E a D, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 */
public Pdg_preventivoBulk modificaStatoPdG_E_D(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato C
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II, pdg.ST_F_CHIUSO_DFNT });

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da F a G, stato padre non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da F a G, pdg padre in stato <> F o G
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da F a G, Pdg aggregato aperto
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da F a G, il pdg del cdr di 1^ livello è ancora aperto (secondo controllaAggregatoChiuso)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il piano di gestione aggregato non è chiuso. Contattare il CDR di primo livello"
 *  Richiesto cambiamento di stato da F a G, bilancio preventivo del cds non ancora approvato
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da F a G, il bilancio preventivo del cds a cui appartiene il cdr non è ancora approvato (secondo controllaPreventivoCdsApprovato)
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il bilancio preventivo del cds non è approvato."
 */
public Pdg_preventivoBulk modificaStatoPdG_F_G(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg padri siano in stato F o G
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_F_CHIUSO_DFNT, pdg.ST_G_APERTURA_PER_VARIAZIONI });
	
	// Controllo che il pdg aggregato del cdr di 1° livello sia chiuso
	controllaAggregatoChiuso(userContext,pdg);

	// Controllo che il preventivo finanziario del cds sia approvato
	controllaBilancioPreventivoCdsApprovato(userContext,pdg.getCentro_responsabilita());
		
	return pdg;
}

/*
 *  Richiesto cambiamento di stato da G a H, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da G a H, pdg dipendenti in stato <> F o M
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_G_H(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i pdg dipendenti siano in stato F o M
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_F_CHIUSO_DFNT, pdg.ST_M_MODIFICATO_PER_VARIAZIONI });

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da G a M, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da G a M, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da G a M, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da G a M, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da G a M
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da G a M, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_G_M(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da H a M, dettagli scaricati non confermati
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da H a M, esiste qualche dettaglio scaricato su altra UO non ancora confermato o annullato (secondo controllaDettScarConfermati())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
 *  Richiesto cambiamento di stato da H a M, i ricavi figurativi non quadrano
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da H a M, il controllo dei ricavi figurativi non va a buon fine (secondo controllaQuadraturaRicaviFigurativi()))
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Mancata quadratura dei ricavi figurativi!"
 *  Richiesto cambiamento di stato da H a M
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da H a M, il cdr del pdg possiede un pdg aggregato e nessun'altra precondizione verificata
 *    PostCondition:
 *		Viene aggiornato il pdg aggregato corrispondente con i totali aggregati del pdg e dei suoi figli (inizializzaAggregatoPDG())
 */
public Pdg_preventivoBulk modificaStatoPdG_H_M(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che i dettagli scaricati su altre UO siano stati confermati
	controllaDettScarConfermati(userContext, oldPdg);

	// Effettuo i controlli di quadratura dei ricavi figurativi	
	controllaQuadraturaRicaviFigurativi(userContext,pdg);

	// Se passo in stato M su un pdg di un cdr di 1^ livello
	// e l'aggregato è ancora in stato B, forzo il passaggio dell'aggregato
	// in stato M
	if (livelloResponsabilitaPdg == 1 ||
		isCdrArea(userContext,pdg.getCentro_responsabilita()))
		try {
			Pdg_aggregatoBulk pdg_aggregato = (Pdg_aggregatoBulk)getHome(userContext,Pdg_aggregatoBulk.class).findAndLock(new Pdg_aggregatoBulk(pdg.getCd_centro_responsabilita(),pdg.getEsercizio()));
			if (pdg_aggregato.STATO_B.equals(pdg_aggregato.getStato())) {
				pdg_aggregato.setStato(pdg_aggregato.STATO_M);
				pdg_aggregato.setUser(userContext.getUser());
				getHome(userContext,pdg_aggregato).update(pdg_aggregato, userContext);
			}
		} catch(OutdatedResourceException e) {
			throw handleException(e);
		} catch(BusyResourceException e) {
			throw handleException(e);
		} catch(PersistencyException e) {
			throw handleException(e);
		}

	// Aggiorno il pdg aggregato (solo per cdr 1° livello o cdr figli di uo area
	inizializzaAggregatoPDGPerVariazioni(userContext, pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da M a F, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M a F, pdg dipendenti in stato <> F
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da M a F, stato aggregato non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M a F, il pdg aggregato del cdr di 1^ livello è in stato diverso da E
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Stato del pdg aggregato non compatibile"
 *  Richiesto cambiamento di stato da M a F, esistono discrepanze nel Pdg Aggregato
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M ad F, il cdr del pdg possiede un pdg aggregato ed esistono discrepanze tra gli importi iniziali e quelli modificati nel pdg aggregato (secondo controllaDiscrepanzeAggregatoPerVariazioni())
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Sono presenti importi complessivi non compatibili con i dati impostati dall'ente."
 *  Richiesto cambiamento di stato da M a F
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M ad F e nessun'altra precondizione è verificata
 *    PostCondition:
 *		Viene invocato trasformDettagliVariazioneInDefinitivi() per convertire i dettagli del pdg da dettagli con origine 'MOD' in dettagli con provenienza 'DIR'
 */
public Pdg_preventivoBulk modificaStatoPdG_M_F(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che il pdg aggregato del cdr di 1° livello sia in stato "E"
	controllaStatoAggregato(userContext,pdg,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk.STATO_E);

	// Controllo che i pdg dipendenti siano in stato F
	controllaStatoPdgFigli(userContext,pdg,new String[] { pdg.ST_F_CHIUSO_DFNT });

	// Controllo della presenza di discrepanze nell'aggregato
	controllaDiscrepanzeAggregatoPerVariazioni(userContext,pdg,livelloResponsabilitaPdg);

	return pdg;
}

/*
 *  Richiesto cambiamento di stato da M a G, stato figli non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M a G, pdg padre in stato <> G
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Richiesto cambiamento di stato da M a G, stato padre non compatibile
 *    PreCondition:
 *      L'utente ha richiesto il cambiamento di stato di un pdg da M a G, pdg padre in stato <> E
 *    PostCondition:
 *		Viene generata una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 */
public Pdg_preventivoBulk modificaStatoPdG_M_G(
	UserContext userContext,
	CdrBulk cdrUtente,
	Pdg_preventivoBulk pdg,
	Pdg_preventivoBulk oldPdg,
	int livelloResponsabilitaUtente,
	int livelloResponsabilitaPdg) throws it.cnr.jada.comp.ComponentException {

	// Controllo che il pdg aggregato del cdr di 1° livello sia in stato "E"
	controllaStatoAggregato(userContext,pdg,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk.STATO_E);

	// Controllo che il pdg padre sia già in stato "G"
	controllaStatoPdgPadri(userContext,pdg,livelloResponsabilitaPdg,false,new String[] { pdg.ST_G_APERTURA_PER_VARIAZIONI });

	return pdg;
}

//^^@@
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Viene richiesto il ribaltamento dei costi del Piano di Gestione del CdR specificato all'area di ricerca a cui afferisce. Il bilancio del CNR è già stato approvato
  *    PostCondition:
  *      La procedura Oracle CNRCTB053.ribaltaSuAreaPDG viene eseguita per l'anno di esercizio ed il CdR specificati.
 */
//^^@@
	public Pdg_preventivoBulk ribaltaCostiPdGArea(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
					+ "CNRCTB053.ribaltaSuAreaPDG(?,?,?)}",false,this.getClass());
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setString( 2, pdg.getCd_centro_responsabilita());
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,pdg);
				cs.executeQuery();
				return caricaPdg(userContext,pdg.getCentro_responsabilita());
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto l'esecuzione dello scarico dei costi del dipendente per un pdg
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB060.scaricaCDPSuPdg
 */
//^^@@
	public Pdg_preventivoBulk scaricaCDPSuPdg(UserContext userContext, Pdg_preventivoBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB060.scaricaCDPSuPdg(?,?,?)}",false,this.getClass());
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setString( 2, pdg.getCd_centro_responsabilita());
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,pdg);
				cs.executeQuery();
				return caricaPdg(userContext,pdg.getCentro_responsabilita());
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
			    cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
}

/**
 * Filtra i soli dettagli non annullati
 */
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	Pdg_preventivo_detBulk pdg_det = (Pdg_preventivo_detBulk)bulk;

    SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);

	sql.addClause("AND", "stato", sql.NOT_EQUALS, "N");
	
	return sql;
}
//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili per un dettaglio di spesa verso altra UO o di costi senza spese con un elenco di clausole specificate
  *    PostCondition:
  *      Viene restituito una query sui centri di spesa con le clausole specificate e una clausola sull'esercizio uguale a quello del dettaglio di spesa specificato
 */
//^^@@

 	public SQLBuilder selectAltro_cdrByClause (UserContext userContext,
											Pdg_preventivo_spe_detBulk dettaglio_pdg,
											CdrBulk cdr,
											CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
		if (clause == null) clause = ((OggettoBulk)cdr).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, cdr, "V_CDR_VALIDO").createSQLBuilder();
		sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", sql.NOT_EQUALS, dettaglio_pdg.getCd_centro_responsabilita());
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

		if (clause != null) sql.addClause(clause);

		return sql;
	}
	/** 
	  *  Normale
	  *    PreCondition:
	  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
	  *    PostCondition:
	  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
	 */
//	  ^^@@
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, 
	Stampa_vpg_bilancio_riclassVBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;
	}
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, 
	Stampa_pdg_etr_speVBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;
	}		
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, 
	Stampa_libro_giornaleBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;
	}	
	
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, Stampa_situazione_analitica_x_GAEBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
			{	
				SQLBuilder sql =  ((CdsHome) getHome(userContext, cds.getClass(),"V_CDS_VALIDO")).createSQLBuilderIncludeEnte();
				sql.addClause( clause );
				
				String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);			
				it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
				it.cnr.contab.config00.sto.bulk.CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

				if (cds_scrivania.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
				}
				else{
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
					sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getCd_cds());
					sql.addClause("AND","FL_CDS",sql.EQUALS, new Boolean(true) );
				}
				return sql;
			}	
	
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, Stampa_situazione_sintetica_x_progettoBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql =  ((CdsHome) getHome(userContext, cds.getClass(),"V_CDS_VALIDO")).createSQLBuilderIncludeEnte();
		sql.addClause( clause );
		
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);			
		it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		it.cnr.contab.config00.sto.bulk.CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (cds_scrivania.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		}
		else{
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
			sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getCd_cds());
			sql.addClause("AND","FL_CDS",sql.EQUALS, new Boolean(true) );
		}
		return sql;
	}	

	public SQLBuilder selectElementoVoceForPrintByClause(UserContext userContext, Stampa_pdg_etr_speVBulk stampa, Elemento_voceBulk elementoVoce, CompoundFindClause clauses) throws ComponentException {

		Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, elementoVoce);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND","fl_partita_giro", sql.EQUALS, Boolean.FALSE);
		sql.addClause("AND","ti_elemento_voce", sql.EQUALS, home.TIPO_CAPITOLO);
		sql.addClause("AND","ti_gestione", sql.EQUALS, stampa.getTi_etr_spe());
        if(stampa.getTi_etr_spe().equals(Stampa_pdg_etr_speVBulk.TIPO_ENTRATA))
		  sql.addClause("AND","ti_appartenenza", sql.EQUALS, home.APPARTENENZA_CNR);
		else
		  sql.addClause("AND","ti_appartenenza", sql.EQUALS, home.APPARTENENZA_CDS);  				     
		sql.addClause(clauses);
		return sql;
	}
	public SQLBuilder selectProgettoForPrintByClause (UserContext userContext, 
	Stampa_pdg_etr_speVBulk stampa, ProgettoBulk progetto, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "PG_PROGETTO_PADRE", sql.ISNULL,null);
		// Se uo 999.000 in scrivania: visualizza tutti i progetti
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			sql.addSQLExistsClause("AND",progettohome.abilitazioniProgetti(userContext));
		}
		return sql;
	}

	public SQLBuilder selectDipartimentoForPrintByClause (UserContext userContext,
	Stampa_pdg_etr_speVBulk stampa, DipartimentoBulk dipartimento, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, dipartimento.getClass()).createSQLBuilder();
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext));
	 	sql.addClause("AND", "dt_istituzione", sql.LESS, lastDayOfYear);
		sql.openParenthesis("AND");
		sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS, lastDayOfYear);
		sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
		sql.closeParenthesis();
		sql.addClause( clause );
		return sql;
	}		
	public SQLBuilder selectDipartimentoByClause (UserContext userContext, Stampa_rendiconto_finanziarioVBulk stampa, DipartimentoBulk dipartimento, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, dipartimento.getClass()).createSQLBuilder();
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext));
	 	sql.addClause("AND", "dt_istituzione", sql.LESS, lastDayOfYear);
		sql.openParenthesis("AND");
		sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS, lastDayOfYear);
		sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
		sql.closeParenthesis();
		sql.addClause( clause );
		return sql;
	}		
	public SQLBuilder selectCdsByClause (UserContext userContext,Stampa_rendiconto_finanziarioVBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;
	}	
		
	/** 
	  *  Normale
	  *    PreCondition:
	  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
	  *    PostCondition:
	  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
	 */
//	  ^^@@
	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
	Stampa_vpg_bilancio_riclassVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		if (clause == null) clause = uo.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdsForPrint().getCd_unita_organizzativa());

		if (clause != null) sql.addClause(clause);
		return sql; 
	}
	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
	Stampa_pdg_etr_speVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		if (clause == null) clause = uo.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdsForPrint().getCd_unita_organizzativa());

		if (clause != null) sql.addClause(clause);
		return sql; 
	}
	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
	Stampa_libro_giornaleBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
		{	
			if (clause == null) clause = uo.buildFindClauses(null);

			SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
			sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdsForPrint().getCd_unita_organizzativa());

			if (clause != null) sql.addClause(clause);
			return sql; 
		}	
	
	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
			Stampa_situazione_analitica_x_GAEBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
				{	
					String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
					it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
					it.cnr.contab.config00.sto.bulk.CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

//				if (!cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){	
					if (clause == null) clause = uo.buildFindClauses(null);

					SQLBuilder sql = ((Unita_organizzativaHome) getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
					sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
					sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdCdsForPrint());

					if (clause != null) sql.addClause(clause);
					return sql;
			/*	}
				else {
					if(stampa.getCdCdsForPrint().equals(cd_cds_scrivania)){
					if (clause == null) clause = uo.buildFindClauses(null);

					SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
					sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
				
					if (clause != null) sql.addClause(clause);
					return sql;
					}
					else {
						if (clause == null) clause = uo.buildFindClauses(null);

						SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
						sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
						sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdCdsForPrint());

						if (clause != null) sql.addClause(clause);
						return sql;
					}
					}*/
				
				}		

	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext,  Stampa_situazione_sintetica_x_progettoBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException {	
		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		it.cnr.contab.config00.sto.bulk.CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (clause == null) 
			clause = uo.buildFindClauses(null);

		SQLBuilder sql = ((Unita_organizzativaHome) getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdCdsForPrint()!=null?stampa.getCdCdsForPrint():"*");

		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}		

	public SQLBuilder selectGaeForPrintByClause(UserContext userContext, Stampa_situazione_sintetica_x_progettoBulk stampa, it.cnr.contab.config00.latt.bulk.WorkpackageBulk gae, CompoundFindClause clause) throws ComponentException, PersistencyException {
		if (clause == null) 
			clause = ((OggettoBulk)gae).buildFindClauses(null);
		
		SQLBuilder sql = getHome(userContext,gae, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		if (clause != null) 
			sql.addClause(clause);
		
		sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));

		if (stampa.getPgProgettoForPrint()!=null) 
			sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, stampa.getPgProgettoForPrint());

		if (stampa.getCdResponsabileGaeForPrint()!=null) 
			sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.CD_RESPONSABILE_TERZO", SQLBuilder.EQUALS, stampa.getCdResponsabileGaeForPrint());

		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
		sql.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
		sql.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");

		sql.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
		sql.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, stampa.getCdUoForPrint());

		return sql;
	}

	public SQLBuilder selectResponsabileGaeForPrintByClause(UserContext userContext, Stampa_situazione_sintetica_x_progettoBulk stampa, TerzoBulk responsabile, CompoundFindClause clause) throws ComponentException, PersistencyException {
		if (clause == null) 
			clause = ((OggettoBulk)responsabile).buildFindClauses(null);
		
		SQLBuilder sql = getHome(userContext, responsabile).createSQLBuilder();
		if (clause != null) 
			sql.addClause(clause);
		
		SQLBuilder sqlExist = getHome(userContext,WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_RESPONSABILE_TERZO", "TERZO.CD_TERZO");
		sqlExist.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));

		if (stampa.getPgProgettoForPrint()!=null) 
			sqlExist.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, stampa.getPgProgettoForPrint());

		if (stampa.getCdGaeForPrint()!=null) 
			sqlExist.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", SQLBuilder.EQUALS, stampa.getCdGaeForPrint());
		
		sqlExist.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
		sqlExist.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
		sqlExist.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");

		sqlExist.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
		sqlExist.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, stampa.getCdUoForPrint());

		sql.addSQLExistsClause("AND",sqlExist);

		return sql;
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@
public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_analitica_entrate_ldaBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
{
 	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
 	if(clause != null)
 	 aSQL.addClause(clause);
 	return aSQL; 
}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@
public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_analitica_spese_ldaBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
{
 	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
 	if(clause != null)
 	 aSQL.addClause(clause);
 	return aSQL; 
}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausolOrdineAcqComponentOrdineAcqComponenta sull'esercizio uguale a quello del pdg specificato
 */
//^^@@
public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_sintetica_entrate_ldaBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
{
 	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
 	if(clause != null)
 	 aSQL.addClause(clause);
 	return aSQL; 
}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@
public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_sintetica_spese_ldaBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
{
 	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
 	if(clause != null)
 	 aSQL.addClause(clause);
 	return aSQL; 
}

public SQLBuilder selectCdrForPrintByClause (UserContext userContext, Stampa_situazione_analitica_x_GAEBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
	{
	 	SQLBuilder aSQL = (listaCdrPdGPerUtenteIncludeEnte (userContext));
	 	if(clause != null)
	 	 aSQL.addClause(clause);
	 	 aSQL.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,stampa.getCdUoForPrint());
	 	return aSQL; 
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@
public SQLBuilder selectCdsByClause (UserContext userContext, 
	StampaRendFinCNRVBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
{	
	it.cnr.contab.config00.sto.bulk.CdsHome home = (it.cnr.contab.config00.sto.bulk.CdsHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.CdsBulk.class);
	
 	SQLBuilder sql = home.createSQLBuilder();
 	sql.addClause(clause);
 	
 	return sql; 
}
//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@

 	public SQLBuilder selectCentro_responsabilitaByClause (UserContext userContext,
											Pdg_preventivoBulk pdg,
											CdrBulk cdr,
											CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
	 	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
	 	if(clause != null)
	 	 aSQL.addClause(clause);
	 	return aSQL; 
	}

/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco degli elementi voce compatibili per il dettaglio di entrata specificato con un elenco di clausole specificate
  *    PostCondition:
  *      Viene restituita una query sulla vista V_ELEMENTO_VOCE_PDG_ETR che contiene le clausole specificate più la clausola CD_NATURA =  dettaglioSpesa.cd_natura
  *      Se il CDR non appartiene alla SAC, viene imposta l'ulteriore condizione che la voce del piano non sia riservata SAC
  */

public SQLBuilder selectElemento_voceByClause(UserContext userContext,
											Pdg_preventivo_etr_detBulk dettaglioEntrata,
											Elemento_voceBulk elementoVoce,
											CompoundFindClause clause)
throws ComponentException, PersistencyException
{
	if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

	SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_ETR").createSQLBuilder();
	if(clause != null) sql.addClause(clause);
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

    sql.openParenthesis("AND");
	sql.addSQLClause("OR", "FL_PARTITA_GIRO", sql.ISNULL, null);	
	sql.addSQLClause("OR", "FL_PARTITA_GIRO", sql.EQUALS, "N");	
    sql.closeParenthesis();

	if (dettaglioEntrata.getLinea_attivita() != null)
		sql.addSQLClause("AND","CD_NATURA",sql.EQUALS,dettaglioEntrata.getLinea_attivita().getCd_natura());
	if (!dettaglioEntrata.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC))
		sql.addSQLClause("AND","FL_VOCE_SAC",sql.EQUALS,"N");	      
	if (dettaglioEntrata.getClassificazione_entrate() != null) {
		if (dettaglioEntrata.getClassificazione_entrate().getEsercizio() != null) 
			sql.addSQLClause("AND","ESERCIZIO_CLA_E",sql.EQUALS,dettaglioEntrata.getClassificazione_entrate().getEsercizio());
		if (dettaglioEntrata.getClassificazione_entrate().getCodice_cla_e() != null)
			sql.addSQLClause("AND","COD_CLA_E",sql.EQUALS,dettaglioEntrata.getClassificazione_entrate().getCodice_cla_e());
	}
	
	if (clause != null) sql.addClause(clause);

	return sql;
}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco degli elementi voce compatibili per il dettaglio di spesa specificato con un elenco di clausole specificate
  *    PostCondition:
  *      Viene restituita una query sulla vista V_ELEMENTO_VOCE_PDG_SPE che contiene le clausole specificate più la clausola CD_FUNZIONE =  dettaglioSpesa.cd_funzione, CD_TIPO_UNITA  =  dettaglioSpesa.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita()
 */
//^^@@

	public SQLBuilder selectElemento_voceByClause(UserContext userContext,
												Pdg_preventivo_spe_detBulk dettaglioSpesa,
												Elemento_voceBulk elementoVoce,
												CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_SPE").createSQLBuilder();
		if(clause != null) sql.addClause(clause);
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

		sql.openParenthesis("AND");
		sql.addSQLClause("OR", "FL_PARTITA_GIRO", sql.ISNULL, null);	
		sql.addSQLClause("OR", "FL_PARTITA_GIRO", sql.EQUALS, "N");	
		sql.closeParenthesis();

		if (dettaglioSpesa.getLinea_attivita() != null)
			sql.addSQLClause("AND","CD_FUNZIONE",sql.EQUALS,dettaglioSpesa.getLinea_attivita().getCd_funzione());
		if ((dettaglioSpesa.getCategoria_economica_finanziaria() != null) && (dettaglioSpesa.getCategoria_economica_finanziaria().getCd_capoconto_fin() != null))
			sql.addSQLClause("AND","CD_CAPOCONTO_FIN",sql.EQUALS,dettaglioSpesa.getCategoria_economica_finanziaria().getCd_capoconto_fin());
		if (dettaglioSpesa.getCentro_responsabilita() != null)
			sql.addSQLClause("AND","CD_TIPO_UNITA",sql.EQUALS,dettaglioSpesa.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita());
		if (dettaglioSpesa.getClassificazione_spese() != null) {
			if (dettaglioSpesa.getClassificazione_spese().getEsercizio() != null) 
				sql.addSQLClause("AND","ESERCIZIO_CLA_S",sql.EQUALS,dettaglioSpesa.getClassificazione_spese().getEsercizio());
  			if (dettaglioSpesa.getClassificazione_spese().getCodice_cla_s() != null)
	  			sql.addSQLClause("AND","COD_CLA_S",sql.EQUALS,dettaglioSpesa.getClassificazione_spese().getCodice_cla_s());
		}

		if (clause != null) sql.addClause(clause);

		return sql;
	}

	public SQLBuilder selectClassificazione_speseByClause(UserContext userContext,
												Pdg_preventivo_spe_detBulk dettaglioSpesa,
												Classificazione_speseBulk classificazione_spese,
												CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		Classificazione_speseHome home = (Classificazione_speseHome)getHome(userContext, Classificazione_speseBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		if(clause != null) 
			sql.addClause(clause);

		return sql;
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività utilizzabili dal metodo delDetByLA
  *    PostCondition:
  *       Viene restituita una query sulla vista V_LINEA_ATTIVITA_VALIDA per il cdr del pdg specificato.
 */
//^^@@
	public SQLBuilder selectLinea_attivita_eliminazioneByClause(UserContext userContext,
													Pdg_preventivoBulk pdg,
													it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita,
													CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		if (clause == null) clause = ((OggettoBulk)linea_attivita).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext,linea_attivita, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		if (clause != null) sql.addClause(clause);

		sql.addClause(
				"AND",
				"cd_centro_responsabilita",
				it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
				pdg.getCd_centro_responsabilita()
		);
		sql.addSQLClause(
				"AND",
				"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",
				it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)
		);

		return sql;
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività compatibili per un dettaglio di spesa con un elenco di clausole specificate
  *    PostCondition:
  *      Viene restituito una query sulle linee di attività con le clausole specificate e 
  *      esercizio = dettaglio_pdg.esercizio
  *      cdr = dettaglio_pdg.cdr
 */
//^^@@

	public SQLBuilder selectLinea_attivitaByClause(UserContext userContext,
													Pdg_preventivo_detBulk dettaglio_pdg,
													it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita,
													CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		if (clause == null) clause = ((OggettoBulk)linea_attivita).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext,linea_attivita, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		if (clause != null) sql.addClause(clause);

		sql.addClause(
				"AND",
				"cd_centro_responsabilita",
				it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
				dettaglio_pdg.getCentro_responsabilita().getCd_centro_responsabilita()
		);

		// Date: 20/02/2002 Filtro sulla sezione delle linee di attività in piano di gestione 
		if(dettaglio_pdg instanceof Pdg_preventivo_spe_detBulk) {
		 sql.addClause("AND","ti_gestione",it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk.TI_GESTIONE_SPESE);
		} else if(dettaglio_pdg instanceof Pdg_preventivo_etr_detBulk) {
		 sql.addClause("AND","ti_gestione",it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk.TI_GESTIONE_ENTRATE);
		}
		else {
		 throw new it.cnr.jada.comp.ApplicationException("Impossibile determinare la sezione entrata/spesa del GAE");
		}	
		sql.addSQLClause(
				"AND",
				"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",
				it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)
		);

		return sql;
	}

public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_spese_ldaBulk)
		validateBulkForPrint(aUC, (Stampa_spese_ldaBulk)bulk);
	else if (bulk instanceof Stampa_libro_giornaleBulk)
		validateBulkForPrint(aUC, (Stampa_libro_giornaleBulk)bulk);
	else if (bulk instanceof Stampa_situazione_analitica_x_GAEBulk)
		stampaConBulk(aUC, (Stampa_situazione_analitica_x_GAEBulk)bulk);
	return bulk;
}

public OggettoBulk stampaConBulk(UserContext userContext, Stampa_situazione_analitica_x_GAEBulk stampa) throws ComponentException {
	if ( stampa.getCdsForPrint()==null || stampa.getCdsForPrint().getCd_proprio_unita()==null)
			throw new ApplicationException( "E' necessario selezionare il CDS");
	return stampa;
}	

private void utenteNonAbilitato() throws it.cnr.jada.comp.ApplicationException {
	throw new it.cnr.jada.comp.ApplicationException("Utente non abilitato ad operare sul PDG");
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint( it.cnr.jada.UserContext userContext, Stampa_spese_ldaBulk stampa)
	throws ComponentException {

	try{
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		if (stampa.getCd_cds()==null)
			throw new ValidationException("Il campo CDS e' obbligatorio");

		//if (!stampa.isCdsUOInScrivania() && stampa.getCdUoForPrint() == null)
			//throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");
////			if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(uo.getCd_tipo_unita())){

	} catch(ValidationException ex) {
		throw new it.cnr.jada.comp.ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext,Stampa_libro_giornaleBulk stampa)throws ComponentException {
	if (stampa.getCdCdsForPrint().equals("*"))
		stampa.setIntestazione("Provvisoria");
	else{
		try {		
			Chiusura_coepHome home = (Chiusura_coepHome)getHome(userContext,Chiusura_coepBulk.class);
			Chiusura_coepBulk chiusura_coep = (Chiusura_coepBulk)home.findByPrimaryKey(new Chiusura_coepBulk(stampa.getCdCdsForPrint(),stampa.getEsercizio()));
			if (chiusura_coep != null && chiusura_coep.getStato().equals(Chiusura_coepBulk.STATO_CHIUSO_DEFINITIVAMENTE))
			  stampa.setIntestazione("Definitiva");
			else
			  stampa.setIntestazione("Provvisoria");  		

		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} 
	}
}
public boolean isUoPrincipale(CNRUserContext userContext) throws ComponentException {
	try {
		Unita_organizzativaHome uom = (Unita_organizzativaHome)getHome(userContext,Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uom.findByPrimaryKey(new Unita_organizzativaBulk(userContext.getCd_unita_organizzativa()));
		return uo.isUoCds();
	} catch (it.cnr.jada.persistency.PersistencyException pe){
				throw new ComponentException(pe);
	}
}
private void validaVariazione(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException
{
	Pdg_preventivo_detBulk pdg_det = (Pdg_preventivo_detBulk)oggettobulk;
	boolean diffZeroSpesa  = false;
	boolean diffZeroEntrata = false;
	if (pdg_det.getPdg_variazione() != null){
		try {		
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome)getHome(usercontext, Ass_pdg_variazione_cdrBulk.class);
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)testataHome.findByPrimaryKey(new Ass_pdg_variazione_cdrBulk(pdg_det.getEsercizio(), pdg_det.getPdg_variazione().getPg_variazione_pdg(), 
																																		 pdg_det.getCentro_responsabilita().getCd_centro_responsabilita()),true);
			Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)getHome(usercontext,Pdg_variazioneBulk.class).findByPrimaryKey(new Pdg_variazioneBulk(pdg_det.getEsercizio(), pdg_det.getPdg_variazione().getPg_variazione_pdg()),true);

			if (pdg_det instanceof Pdg_preventivo_etr_detBulk){
				if (ass_pdg.getIm_entrata() != null){
					BigDecimal sommaEntrata = ZERO;
					for (java.util.Iterator entrate = testataHome.findDettagliEntrata(ass_pdg).iterator();entrate.hasNext();){
						Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk)entrate.next();
						sommaEntrata = (sommaEntrata.add(
							(etr_det.getIm_ra_rce()).add(
							(etr_det.getIm_rc_esr()))
						));
					}
					if(ass_pdg.getIm_entrata().compareTo(sommaEntrata) < 0 && !pdg.isPropostaProvvisoria())
						throw new ApplicationException("La Somma dei dettagli di entrata ("+new it.cnr.contab.util.EuroFormat().format(sommaEntrata)+")"+
													   "\n" + " supera la quota di entrata assegnata di "+
													   new it.cnr.contab.util.EuroFormat().format(sommaEntrata.subtract(ass_pdg.getIm_entrata())));
					else if (ass_pdg.getIm_entrata().compareTo(sommaEntrata) == 0)
					  diffZeroEntrata = true;
				}					  
			}else if (pdg_det instanceof Pdg_preventivo_spe_detBulk){
				if (ass_pdg.getIm_spesa() != null){
					BigDecimal sommaSpesa = ZERO;
					for (java.util.Iterator spese = testataHome.findDettagliSpesa(ass_pdg).iterator();spese.hasNext();){
						Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk)spese.next();
						sommaSpesa = (sommaSpesa.add(
							(spesa_det.getIm_ri_ccs_spese_odc()).add(
							(spesa_det.getIm_rj_ccs_spese_odc_altra_uo()).add(
							(spesa_det.getIm_rk_ccs_spese_ogc()).add(
							(spesa_det.getIm_rl_ccs_spese_ogc_altra_uo()).add(
							(spesa_det.getIm_rq_ssc_costi_odc()).add(
							(spesa_det.getIm_rr_ssc_costi_odc_altra_uo()).add(
							(spesa_det.getIm_rs_ssc_costi_ogc()).add(
							(spesa_det.getIm_rt_ssc_costi_ogc_altra_uo()))))))))
						));                     
					}
					if(ass_pdg.getIm_spesa().compareTo(sommaSpesa) < 0 && !pdg.isPropostaProvvisoria())
						throw new ApplicationException("La Somma dei dettagli di spesa ("+new it.cnr.contab.util.EuroFormat().format(sommaSpesa)+")"+
													   "\n" + " supera la quota di spesa assegnata di "+
													   new it.cnr.contab.util.EuroFormat().format(sommaSpesa.subtract(ass_pdg.getIm_spesa())));
					else if (ass_pdg.getIm_spesa().compareTo(sommaSpesa) == 0)
					  diffZeroSpesa = true;													   
				}			
			}
			if(diffZeroSpesa || diffZeroEntrata){
				try {	
					MessaggioHome messHome = (MessaggioHome)getHome(usercontext,MessaggioBulk.class);
					UtenteHome utenteHome = (UtenteHome)getHome(usercontext,UtenteBulk.class);
					for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(pdg.getCd_centro_responsabilita()).iterator();i.hasNext();){
						UtenteBulk utente = (UtenteBulk)i.next();
						MessaggioBulk messaggio = generaMessaggio(usercontext,utente,pdg,ass_pdg,diffZeroSpesa ? "Spesa":"Entrata");
						super.creaConBulk(usercontext, messaggio);
					}
				} catch (PersistencyException e) {
				   throw new ComponentException(e);
				} catch (IntrospectionException e) {
					throw new ComponentException(e);
				}					
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}
}
private MessaggioBulk generaMessaggio(UserContext userContext, UtenteBulk utente, Pdg_variazioneBulk pdg, Ass_pdg_variazione_cdrBulk ass_pdg, String etr_spe) throws ComponentException, PersistencyException{
	MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
	MessaggioBulk messaggio = new MessaggioBulk();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	messaggio.setPg_messaggio(new Long(messHome.fetchNextSequenceValue(userContext,"CNRSEQ00_PG_MESSAGGIO").longValue()));
	messaggio.setCd_utente(utente.getCd_utente());
	messaggio.setPriorita(new Integer(1));
	messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - È stata raggiunta la quota di "+ etr_spe +" assegnata alla Variazione");
	messaggio.setCorpo("Numero variazione:"+pdg.getPg_variazione_pdg());
	messaggio.setCorpo(messaggio.getCorpo() + "\n" + "Il CdR :"+ass_pdg.getCentro_responsabilita().getCd_ds_cdr()+" ha coperto la quota assegnata.");
	messaggio.setSoggetto(messaggio.getDs_messaggio());
	messaggio.setToBeCreated(); 
	return messaggio;	 	
}

private boolean isCdsEnte(UserContext userContext) throws ComponentException {
	try {
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
			return true;
		else
			return false;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
	
public SQLBuilder selectCentro_responsabilitaByClause (UserContext userContext,
			Stampa_situazione_sintetica_x_progettoBulk stampa,
			CdrBulk cdr,
			CompoundFindClause clause) throws ComponentException, PersistencyException {
	SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext));
	if(clause != null)
		aSQL.addClause(clause);
	return aSQL; 
}	

public SQLBuilder selectProgettoForPrintByClause (UserContext userContext, Stampa_situazione_sintetica_x_progettoBulk stampa, ProgettoBulk progetto, CompoundFindClause clause) throws ComponentException, PersistencyException
{	
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	sql.addClause( clause );
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	
	if (stampa.getGaeForPrint()!=null && stampa.getGaeForPrint().getPg_progetto()!=null) 
		sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", SQLBuilder.EQUALS, stampa.getGaeForPrint().getPg_progetto());
	
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

	SQLBuilder sqlAbilitazioni;
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		sqlAbilitazioni = progettohome.abilitazioniCommesse(userContext);
		if (stampa.getCdUoForPrint()!=null)
			sqlAbilitazioni.addSQLClause("AND", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, stampa.getCdUoForPrint());
	} else {
		ProgettoHome progettoAbilhome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_ABIL_PROGETTI");    	
		sqlAbilitazioni = progettoAbilhome.createSQLBuilder();
		sqlAbilitazioni.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_COMMESSA","V_PROGETTO_PADRE.ESERCIZIO");
		sqlAbilitazioni.addSQLJoin("V_ABIL_PROGETTI.PG_COMMESSA","V_PROGETTO_PADRE.PG_PROGETTO");
		sqlAbilitazioni.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_COMMESSA","V_PROGETTO_PADRE.TIPO_FASE");
		sqlAbilitazioni.addSQLClause("AND","V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,stampa.getCdUoForPrint());

	}
	sql.addSQLExistsClause("AND",sqlAbilitazioni);
	
	return sql;
}	

private void inizializzaBulkPerStampa(UserContext userContext, Stampa_situazione_sintetica_x_progettoBulk stampa) throws ComponentException {
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	try{
		String cdCds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String cdUo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cdCds));
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHome( userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdUo));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

		stampa.setCdsForPrintEnabled(true);
		stampa.setUoForPrintEnabled(true);
		stampa.setProgettoForPrintEnabled(true);
		stampa.setGaeForPrintEnabled(true);
		stampa.setTi_ordine_stampa(Stampa_situazione_sintetica_x_progettoBulk.TI_ORDINE_GAE_VOCE_ANNO);

		if (!Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equals(uo.getCd_tipo_unita())){
			stampa.setCdsForPrint(cds);
			stampa.setCdsForPrintEnabled(false);
			if (!uo.getFl_uo_cds().equals(Boolean.TRUE)) {
				stampa.setUoForPrint(uo);
				stampa.setUoForPrintEnabled(false);
			}
		}
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
}
}