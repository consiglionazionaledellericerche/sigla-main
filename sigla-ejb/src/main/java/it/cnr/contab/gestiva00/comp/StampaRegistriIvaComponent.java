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

package it.cnr.contab.gestiva00.comp;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome;
import it.cnr.contab.gestiva00.core.bulk.Codici_iva_fatture_acquistiVBulk;
import it.cnr.contab.gestiva00.core.bulk.Codici_iva_fatture_emesseVBulk;
import it.cnr.contab.gestiva00.core.bulk.Gestione_registri_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.IPrintable;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_annualeVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_ripart_finBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_massa_provvisoria_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Quadri_va_veVBulk;
import it.cnr.contab.gestiva00.core.bulk.Quadri_va_vfVBulk;
import it.cnr.contab.gestiva00.core.bulk.Report_statoBulk;
import it.cnr.contab.gestiva00.core.bulk.Report_statoHome;
import it.cnr.contab.gestiva00.core.bulk.Riepilogativi_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Vp_liquid_iva_annualeBulk;
import it.cnr.contab.gestiva00.core.bulk.Vp_liquid_iva_annualeHome;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.MTUStuff;
import it.cnr.jada.bulk.MTUWrapper;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.RicercaComponent;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.ApplicationWarningPersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;

/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
public class StampaRegistriIvaComponent extends RicercaComponent implements IStampaRegistriIvaMgr {
public StampaRegistriIvaComponent() {
	super();
}

private Liquidazione_ivaBulk aggiornaLiquidazione(it.cnr.jada.UserContext userContext, Liquidazione_ivaVBulk stampaBulk, java.math.BigDecimal pg_Stampa) throws it.cnr.jada.comp.ComponentException {

    Liquidazione_ivaBulk liquidazione= stampaBulk.getLiquidazione_iva();

    
    liquidazione.setReport_id(new Long(pg_Stampa.longValue()));
    liquidazione.setDt_inizio(stampaBulk.getData_da());
    liquidazione.setDt_fine(stampaBulk.getData_a());
	liquidazione.setTipo_liquidazione(stampaBulk.getTipoSezionaleFlag());
	
    //if (stampaBulk.getVariazioni_imposta_deb()!=null && stampaBulk.getVariazioni_imposta_deb().compareTo(new java.math.BigDecimal(0))!=0)
	    //liquidazione.setVar_imp_per_prec(stampaBulk.getVariazioni_imposta_deb());
	//if (stampaBulk.getVariazioni_imposta_cre()!=null && stampaBulk.getVariazioni_imposta_cre().compareTo(new java.math.BigDecimal(0))!=0)
		//liquidazione.setVar_imp_per_prec(stampaBulk.getVariazioni_imposta_cre().multiply(new java.math.BigDecimal(-1)));

    return liquidazione;

}

/**
 * riepilogoLiquidazioneIVA method comment.
 */
private void callRiepilogoLiquidazioneIVA(
	it.cnr.jada.UserContext userContext, 
	Liquidazione_iva_annualeVBulk bulk)
	throws SQLException, PersistencyException, ComponentException {

	LoggableStatement cs= null;
	try {
		cs = new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB253.riepilogoLiquidazioneIva(?,?) }",false,this.getClass());
		cs.setLong(1, bulk.getId_report().longValue()); // Id stampa
		cs.setInt(2, bulk.getEsercizio().intValue()); // Esercizio

		cs.executeQuery();
		
	} catch (java.sql.SQLException e) {
		throw SQLExceptionHandler.getInstance().handleSQLException(e);
	} finally {
		if (cs != null) cs.close();
	}
}

//^^@@
/** 
  *  gestisce il richiamo delle procedure sul DB per la registrazione, la stampa,
  *  la liquidazione, la conferma dei registri e l'annullamento di un registro
  *  registro provvisorio o definitivo
  *    PreCondition:
  *      richiesta di creare un registro nuovo provvisorio o definitivo
  *    PostCondition:
  *      crea il registro
  *  stampa di un registro
  *    PreCondition:
  *      richiesta di stampa di un registro provvisorio o definitivo
  *    PostCondition:
  *      crea il prospetto di stampa
  *  liquidazione
  *	   PreCondition:  		
  *      liquidazione provvisoria o definitiva
  *    PostCondition:
  *      crea la liquidazione
  *  Si è verificato un errore.
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio e non permette l'operazione
 */
//^^@@
public MTUWrapper callStampeIva(it.cnr.jada.UserContext userContext, Stampa_registri_ivaVBulk stampaBulk) throws it.cnr.jada.comp.ComponentException {

    //ricavo il progressivo unico pg_stampa
    java.math.BigDecimal pg_Stampa = null;
	if (stampaBulk instanceof IPrintable) {
		if (stampaBulk instanceof Liquidazione_massa_provvisoria_ivaVBulk)
		    pg_Stampa = ((IPrintable)stampaBulk).getId_report();
		else {
		    pg_Stampa = getSequence(userContext);
			((IPrintable)stampaBulk).setId_report(pg_Stampa);
		}
	}
    
    MTUStuff message= new MTUStuff("");

    //per le liquidazioni aggiungo una riga con i dati i dati inseriti dall'utente
    if (stampaBulk.isLiquidazione()) {
        Liquidazione_ivaBulk liquidazione= aggiornaLiquidazione(userContext, (Liquidazione_ivaVBulk) stampaBulk, pg_Stampa);
        try {
            insertBulk(userContext, liquidazione);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw new it.cnr.jada.comp.ApplicationException("Errore generale");
        }
    }

 
    String msgSP= "";
    if (stampaBulk.getTipo_stampa().equals("GESTIONE")) {
        controllaSezionali(userContext, stampaBulk);
        LoggableStatement cs= null;
        try {
            try {
                if (stampaBulk.getTipo_report().equals("CONFERMA"))
                    cs= new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
                    		+ "CNRCTB250.conferma_registro(?,?,?,?,?,?,?) }",false,this.getClass());
                else
                    cs= new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    		+ "CNRCTB250.annulla_registro(?,?,?,?,?,?,?) }",false,this.getClass());
                cs.setString(1, ((Gestione_registri_ivaVBulk)stampaBulk).getUnita_organizzativa().getUnita_padre().getCd_unita_organizzativa()); // CD CDS
                cs.setString(2, ((Gestione_registri_ivaVBulk)stampaBulk).getUnita_organizzativa().getCd_unita_organizzativa()); // Cd_unita_organizzativa
                cs.setInt(3, stampaBulk.getEsercizio().intValue()); // ESERCIZIO
                cs.setString(4, stampaBulk.getTipo_sezionale().getCd_tipo_sezionale()); // Cd_tipo_sezionale
                cs.setDate(5, new Date(stampaBulk.getData_da().getTime())); // Data inizio
                cs.setDate(6, new Date(stampaBulk.getData_a().getTime())); // Data fine
                cs.setString(7, stampaBulk.getUser()); // Utente
                cs.executeQuery();

            } catch (java.sql.SQLException e) {
                throw SQLExceptionHandler.getInstance().handleSQLException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (Throwable ex) {
            if (ex instanceof ApplicationWarningPersistencyException)
                message.setMessage(ex.getMessage());
            else
                throw handleException(ex);

        }
    } else {
        //richiamo la stored procedure per la generazione dei vari report
    	LoggableStatement cs= null;
        try {
            try {
                cs= new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
                		+ "CNRCTB250.StampeIVA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }",false,this.getClass());

                cs.setString(1, stampaBulk.getCd_cds()); // CD CDS
                cs.setString(2, stampaBulk.getCd_unita_organizzativa()); // Cd_unita_organizzativa
                cs.setInt(3, stampaBulk.getEsercizio().intValue()); // ESERCIZIO
                if (stampaBulk.isStarSezionali())
                    cs.setString(4, "*"); // Cd_tipo_sezionale
                else
                    cs.setString(4, stampaBulk.getTipo_sezionale().getCd_tipo_sezionale()); // Cd_tipo_sezionale

                cs.setDate(5, new Date(stampaBulk.getData_da().getTime())); // Data inizio
                cs.setDate(6, new Date(stampaBulk.getData_a().getTime())); // Data fine
                
                if (stampaBulk.getTipo_stampa().equals(stampaBulk.TIPO_STAMPA_LIQUIDAZIONE) && stampaBulk.getTipo_report().equals(stampaBulk.DEFINITIVO))
                    cs.setString(7, stampaBulk.getTipo_stampa() + "_DEF"); // TipoStampa = 'LIQUIDAZIONE_DEF'
                else
                    cs.setString(7, stampaBulk.getTipo_stampa()); // TipoStampa = 'REGISTRI'/ 'RIEPILOGATIVI' / 'LIQUIDAZIONE' / 'LIQUIDAZIONE DI MASSA'
                    
                if (stampaBulk.isStarRegistro()) {
                    if (stampaBulk.isRiepilogativo())
                    	cs.setString(8, ((Riepilogativi_ivaVBulk)stampaBulk).getSezionaliFlag());
                    else
                    	cs.setString(8, "*"); // TipoRegistro
                } else
                	 if (stampaBulk.getTipo_stampa() != null && stampaBulk.getTipo_stampa().equals(stampaBulk.TIPO_STAMPA_RIEPILOGATIVI_IVA_DIFFERITA))
                     	cs.setString(8, ((Riepilogativi_ivaVBulk)stampaBulk).getSezionaliFlag());
                	 else
                		cs.setString(8, stampaBulk.getTipo_sezionale().getTi_acquisti_vendite()); // TipoRegistro
	                
                cs.setString(9, stampaBulk.getTipo_report()); // TipoReport = 'P' [provvisorio] / 'D' [definitivo]
                cs.setString(10, ((stampaBulk.isRistampa())?
	                								stampaBulk.RISTAMPA :
	                								stampaBulk.NON_RISTAMPA)); // Ristampa = 'N' [no ristampa] / 'Y' [ristampa]
                cs.setBigDecimal(11, pg_Stampa); // repId [dalla sequence]
                cs.setInt(12, 1); // logId [?] (al momento non è gestito...)
                

                cs.registerOutParameter(13, java.sql.Types.VARCHAR);

                cs.setString(14, stampaBulk.getUser());
                cs.setString(15, (stampaBulk.getTipoSezionaleFlag() == null) ?
	                							"" :
	                							stampaBulk.getTipoSezionaleFlag());
                cs.setString(16, stampaBulk.getTipoImpegnoFlag());
                cs.executeQuery();

                msgSP= cs.getString(13);
            } catch (java.sql.SQLException e) {
                throw SQLExceptionHandler.getInstance().handleSQLException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (Throwable ex) {
            if (ex instanceof ApplicationWarningPersistencyException)
                message.setMessage(ex.getMessage());
            else
                throw handleException(ex);

        }

        //per la liquidazione vado a rileggere i dati updatati dalla stored procedure 
        if (stampaBulk.isLiquidazione()) {
            try {
                Liquidazione_ivaBulk liquidazione= ((Liquidazione_ivaVBulk) stampaBulk).getLiquidazione_iva();
                it.cnr.jada.bulk.BulkHome home= getHome(userContext, Liquidazione_ivaBulk.class);
                it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
                sql.addClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
                sql.addClause("AND", "esercizio", sql.EQUALS, liquidazione.getEsercizio());
                sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
                sql.addClause("AND", "dt_inizio", sql.EQUALS, liquidazione.getDt_inizio());
                sql.addClause("AND", "dt_fine", sql.EQUALS, liquidazione.getDt_fine());
                if(stampaBulk instanceof Liquidazione_definitiva_ivaVBulk)
                 sql.addClause("AND", "report_id", sql.EQUALS, new Long(0L));
                else
                 sql.addClause("AND", "report_id", sql.EQUALS, liquidazione.getReport_id());
                sql.addClause("AND", "tipo_liquidazione", sql.EQUALS, stampaBulk.getTipoSezionaleFlag());
                it.cnr.jada.persistency.Broker broker= home.createBroker(sql);
                if (!broker.next())
                	throw new ApplicationException("Impossibile recuperare i dati della liquidazione dopo l'esecuzione della procedura!");
                ((Liquidazione_ivaVBulk) stampaBulk).setLiquidazione_iva((Liquidazione_ivaBulk) broker.fetch(Liquidazione_ivaBulk.class));
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(e);
            }
        }
    }
    if (msgSP != null && msgSP != "")
        message.setMessage(msgSP);
    return new MTUWrapper(stampaBulk, message);
}

/**
 * riepilogoLiquidazioneIVA method comment.
 */
private void callTabCodIvaAcquisti(
	it.cnr.jada.UserContext userContext, 
	Liquidazione_iva_annualeVBulk bulk)
	throws SQLException, PersistencyException, ComponentException {

	LoggableStatement cs= null;
	try {
		cs = new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB253.tabCodIvaAcquisti(?, ?, ?) }",false,this.getClass());
		cs.setLong(1, bulk.getId_report().longValue()); // Id stampa
		cs.setInt(2, bulk.getEsercizio().intValue()); // Esercizio
		cs.setString(3, (bulk instanceof Quadri_va_vfVBulk)?"Y":"N"); // FlEsclusione

		cs.executeQuery();
		
	} catch (java.sql.SQLException e) {
		throw SQLExceptionHandler.getInstance().handleSQLException(e);
	} finally {
		if (cs != null) cs.close();
	}
}

/**
 * riepilogoLiquidazioneIVA method comment.
 */
private void callTabCodIvaVendite(
	it.cnr.jada.UserContext userContext, 
	Liquidazione_iva_annualeVBulk bulk)
	throws SQLException, PersistencyException, ComponentException {

	LoggableStatement cs= null;
	try {
		cs = new LoggableStatement(getConnection(userContext),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB253.tabCodIvaVendite(?, ?, ?) }",false,this.getClass());
		cs.setLong(1, bulk.getId_report().longValue()); // Id stampa
		cs.setInt(2, bulk.getEsercizio().intValue()); // Esercizio
		cs.setString(3, (bulk instanceof Quadri_va_veVBulk)?"Y":"N"); // FlEsclusione

		cs.executeQuery();
		
	} catch (java.sql.SQLException e) {
		throw SQLExceptionHandler.getInstance().handleSQLException(e);
	} finally {
		if (cs != null) cs.close();
	}
}

//^^@@
/** 
  *  controlla lo stato del report e della liquidazione
  *	   controllare lo stato del registro in B
  *    PreCondition:
  *      richiesta di confermare o annullare un registro
  *    PostCondition:
  *      autorizza a procedere con l'operazione
  *    controllare che per il periodo non ci siano liquidazioni definitive
  *    PreCondition:
  *      richiesta di annullare un registro
  *    PostCondition:
  *      autorizza a procedere con l'operazione
  *    PreCondition:
  *      non trova nessun record.
  *    PostCondition:
  *      Viene inviato un messaggio e non permette l'operazione
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio e non permette l'operazione
 */
//^^@@
private void controllaSezionali(it.cnr.jada.UserContext userContext, Stampa_registri_ivaVBulk stampaBulk) throws it.cnr.jada.comp.ComponentException {

    Gestione_registri_ivaVBulk bulk= (Gestione_registri_ivaVBulk) stampaBulk;

    //controlla il report stato
    int rc= -1;
               
    try {
        it.cnr.jada.persistency.sql.SQLBuilder sql= getHome(userContext, Report_statoBulk.class).createSQLBuilder();
        sql.addSQLClause("AND", "STATO", sql.EQUALS, "B");
        sql.addSQLClause("AND", "DT_INIZIO", sql.EQUALS, new Date(stampaBulk.getData_da().getTime()));
        sql.addSQLClause("AND", "DT_FINE", sql.EQUALS, new Date(stampaBulk.getData_a().getTime()));
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, bulk.getEsercizio());
        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, bulk.getUnita_organizzativa().getUnita_padre().getCd_unita_organizzativa());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, bulk.getUnita_organizzativa().getCd_unita_organizzativa());
        sql.addSQLClause("AND", "TIPO_REPORT", sql.EQUALS, "REGISTRO_IVA");
        sql.addSQLClause("AND", "CD_TIPO_SEZIONALE", sql.EQUALS, bulk.getTipo_sezionale().getCd_tipo_sezionale());

        rc= sql.executeCountQuery(getHomeCache(userContext).getConnection());

        if (rc == 0)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione, non ci sono registri per i valori impostati.");
    } catch (SQLException ex) {
        throw handleException(ex);
    }

    if (bulk.getTipo_report().equals("ANNULLA")) {
        //controlla la liquidazione	
        rc= -1;

        try {
            it.cnr.jada.persistency.sql.SQLBuilder sql= getHome(userContext, Liquidazione_ivaBulk.class).createSQLBuilder();
            sql.addSQLClause("AND", "STATO", sql.EQUALS, "D");
            sql.addSQLClause("AND", "REPORT_ID", sql.EQUALS, "0");
            sql.addSQLClause("AND", "DT_INIZIO", sql.EQUALS, new Date(stampaBulk.getData_da().getTime()));
            sql.addSQLClause("AND", "DT_FINE", sql.EQUALS, new Date(stampaBulk.getData_a().getTime()));
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, bulk.getEsercizio());
            sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, bulk.getUnita_organizzativa().getUnita_padre().getCd_unita_organizzativa());
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, bulk.getUnita_organizzativa().getCd_unita_organizzativa());

            rc= sql.executeCountQuery(getHomeCache(userContext).getConnection());

            if (rc != 0)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione, non è possibile annullare poichè esiste una liquidazione.");
        } catch (SQLException ex) {
            throw handleException(ex);
        }
    }
    //tutto ok
    return;

}

public BulkList findRegistriStampati(
    UserContext userContext,
    Stampa_registri_ivaVBulk stampa)
    throws ComponentException {

	Report_statoBulk reportStato = new Report_statoBulk();
	reportStato.completeFrom(stampa);
	
	Report_statoHome home = (Report_statoHome)getHome(userContext, reportStato);

	try {
		BulkList registriStampati = new BulkList(home.findAndOrderByDt_inizio(reportStato));
		getHomeCache(userContext).fetchAll(userContext);
		return registriStampati;
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}

//^^@@
/** 
  *  Identificativo univoco progressivo per la gestione dell' IVA
  *    PreCondition:
  *      Viene richiesta un progressivo
  *    PostCondition:
  *      ritorna un valore 
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */
//^^@@
private java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

	//ricavo il progressivo unico pg_stampa
    java.math.BigDecimal pg_Stampa= new java.math.BigDecimal(0);
    try {
    	LoggableStatement ps= new LoggableStatement(getConnection(userContext),
    			"select IBMSEQ00_STAMPA.nextval from dual",true,this.getClass());
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

/**
 * riepilogoLiquidazioneIVA method comment.
 */
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk 
	riepilogoLiquidazioneIVA(it.cnr.jada.UserContext userContext, 
	it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk bulk)
	throws it.cnr.jada.comp.ComponentException {

	if (!(bulk instanceof Liquidazione_annualeVBulk)) return bulk;
		
    //ricavo il progressivo unico pg_stampa
    java.math.BigDecimal pg_Stampa= getSequence(userContext);
	if (bulk instanceof IPrintable)
		((IPrintable)bulk).setId_report(pg_Stampa);

	try {
		callRiepilogoLiquidazioneIVA(userContext, bulk);

		Vp_liquid_iva_annualeHome home = (Vp_liquid_iva_annualeHome)getHome(userContext, Vp_liquid_iva_annualeBulk.class);
		
		BulkList lista = null;
		
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_A_CLAUSE);
		if (lista != null && !lista.isEmpty())
			bulk.setImporto(((Vp_liquid_iva_annualeBulk)lista.get(0)).getIm_iva());
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_B_CLAUSE);
		bulk.setElenco(lista);
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_C_CLAUSE);
		if (lista != null && !lista.isEmpty())
			((Liquidazione_annualeVBulk)bulk).setImportoTotale(((Vp_liquid_iva_annualeBulk)lista.get(0)).getIm_iva());
	} catch (Throwable t) {
		throw handleException(t);
	}

	bulk.setRistampabile(true);
	
	return bulk;
}

//^^@@
/** 
  *  Prospetto delle liquidazioni definitive
  *  richiesta la lista delle liquidazioni
  *    PreCondition:
  *      Viene richiesta la lista delle liquidazioni già stampate 
  *    PostCondition:
  *      visualizza la lista delle liquidazioni stampate
  *  si verifica un errore
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */
//^^@@
public java.util.Collection selectProspetti_stampatiByClause(
    UserContext aUC,
    OggettoBulk bulk,
    Liquidazione_ivaBulk liq,
    CompoundFindClause clauses)
    throws ComponentException, it.cnr.jada.persistency.PersistencyException {

    Liquidazione_ivaHome home= (Liquidazione_ivaHome) getHome(aUC, Liquidazione_ivaBulk.class);

    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();

    Stampa_registri_ivaVBulk stampaBulk=(Stampa_registri_ivaVBulk) bulk;
    
    sql.addClause(clauses);

    sql.setDistinctClause(true);

	sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, stampaBulk.getCd_cds());
	sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, stampaBulk.getCd_unita_organizzativa());
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, stampaBulk.getEsercizio());		
	sql.addSQLClause("AND", "STATO", sql.EQUALS, stampaBulk.DEFINITIVO);		
	sql.addSQLClause("AND", "TIPO_LIQUIDAZIONE", sql.EQUALS, stampaBulk.getTipoSezionaleFlag());		

	return (java.util.Collection) home.fetchAll(sql);
}

//^^@@
/** 
  *  Combo dei sezionali
  *    PreCondition:
  *      Viene richiesta la lista dei sezionali relativi alla UO di appartenenza
  *    PostCondition:
  *      visualizza la lista dei sezionali
  *  Si è verificato un errore
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */
//^^@@
public java.util.Collection selectTipi_sezionaliByClause(
    UserContext aUC,
    OggettoBulk bulk,
    Tipo_sezionaleBulk tipo_sez,
    CompoundFindClause clauses)
    throws ComponentException, it.cnr.jada.persistency.PersistencyException {

    Tipo_sezionaleHome home= (Tipo_sezionaleHome) getHome(aUC, Tipo_sezionaleBulk.class);

    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();

    Stampa_registri_ivaVBulk stampaBulk=(Stampa_registri_ivaVBulk) bulk;
    
    sql.addClause(clauses);

    sql.setDistinctClause(true);

    sql.addTableToHeader("SEZIONALE");
	sql.addSQLJoin("TIPO_SEZIONALE.CD_TIPO_SEZIONALE","SEZIONALE.CD_TIPO_SEZIONALE");
	sql.addSQLClause("AND", "SEZIONALE.CD_CDS", sql.EQUALS, stampaBulk.getCd_cds());
	sql.addSQLClause("AND", "SEZIONALE.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, stampaBulk.getCd_unita_organizzativa());
	sql.addSQLClause("AND", "SEZIONALE.ESERCIZIO", sql.EQUALS, stampaBulk.getEsercizio());		

	if (stampaBulk.getTipoSezionaleFlag() != null) {
		if (stampaBulk.SEZIONALI_COMMERCIALI.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag()))
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, TipoIVA.COMMERCIALE.value());
		else if (stampaBulk.SEZIONALI_SAN_MARINO_SENZA_IVA.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag())) {
				sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_SAN_MARINO_SENZA_IVA", sql.EQUALS, "Y");
				sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, TipoIVA.ISTITUZIONALE.value());
				sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_BENE_SERVIZIO",sql.NOT_EQUALS,Bene_servizioBulk.SERVIZIO);
		/* RP INTRASTAT EVENTUALMENTE UTILE SE RICHIESTO UN'UNICA LIQUIDAZIONE
		} else if (stampaBulk.SEZIONALI_BENI_INTRA_UE_SERVIZI_INTRA_EXTRA_UE.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag())) {
			sql.openParenthesis("AND");
			sql.openParenthesis("AND");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_INTRA_UE", sql.EQUALS, "Y");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, Tipo_sezionaleBulk.ISTITUZIONALE);
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_BENE_SERVIZIO",sql.EQUALS,Bene_servizioBulk.BENE);
			sql.closeParenthesis();
			sql.openParenthesis("OR");
			sql.openParenthesis("OR");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_INTRA_UE", sql.EQUALS, "Y");
			sql.addSQLClause("OR", "TIPO_SEZIONALE.FL_EXTRA_UE", sql.EQUALS, "Y");
			sql.closeParenthesis();
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, Tipo_sezionaleBulk.ISTITUZIONALE);
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_BENE_SERVIZIO",sql.EQUALS,Bene_servizioBulk.SERVIZIO);
			sql.closeParenthesis();
			sql.closeParenthesis();
		}*/ 
		} else if (stampaBulk.SEZIONALI_BENI_INTRA_UE.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag())) {
			sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_INTRA_UE", sql.EQUALS, "Y");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, TipoIVA.ISTITUZIONALE.value());
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_BENE_SERVIZIO",sql.EQUALS,Bene_servizioBulk.BENE);
		} else if (stampaBulk.SEZIONALI_SERVIZI_NON_RESIDENTI.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag())) {
			sql.openParenthesis("AND");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_INTRA_UE", sql.EQUALS, "Y");
			sql.addSQLClause("OR", "TIPO_SEZIONALE.FL_EXTRA_UE", sql.EQUALS, "Y");
			sql.addSQLClause("OR", "TIPO_SEZIONALE.FL_SAN_MARINO_SENZA_IVA", sql.EQUALS, "Y");
			sql.closeParenthesis();
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, TipoIVA.ISTITUZIONALE.value());
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_BENE_SERVIZIO",sql.EQUALS,Bene_servizioBulk.SERVIZIO);
		} else if (stampaBulk.SEZIONALI_SPLIT_PAYMENT.equalsIgnoreCase(stampaBulk.getTipoSezionaleFlag())) {
			sql.addSQLClause("AND", "TIPO_SEZIONALE.FL_SPLIT_PAYMENT", sql.EQUALS, "Y");
			sql.addSQLClause("AND", "TIPO_SEZIONALE.TI_ISTITUZ_COMMERC", sql.EQUALS, TipoIVA.ISTITUZIONALE.value());
		}
	}

	sql.addOrderBy("CD_TIPO_SEZIONALE");
	
	return (java.util.Collection) home.fetchAll(sql);
}

//^^@@
/** 
  *  searchtool delle unita organizzative (creazione e annullamento di registri in stato B)
  *    PreCondition:
  *      Viene richiesta la lista delle UO
  *    PostCondition:
  *      ritorna la lista delle UO
  *   Si è verificato un errore
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */
//^^@@
public it.cnr.jada.persistency.sql.SQLBuilder selectUnita_organizzativaByClause(
    UserContext aUC,
    Stampa_registri_ivaVBulk stampaBulk,
    Unita_organizzativaBulk uo,
    CompoundFindClause clauses)
    throws ComponentException {

    Unita_organizzativaHome home= (Unita_organizzativaHome) getHome(aUC, Unita_organizzativaBulk.class);
    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();

    if (((Gestione_registri_ivaVBulk)stampaBulk).getUnita_organizzativa()!=null)
	    sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, ((Gestione_registri_ivaVBulk)stampaBulk).getUnita_organizzativa().getCd_unita_organizzativa());
	return sql;
}

/**
 * tabCodIvaAcquisti method comment.
 */
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk 
	tabCodIvaAcquisti(
		it.cnr.jada.UserContext userContext,
		it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk bulk)
		throws it.cnr.jada.comp.ComponentException {

	if (!(bulk instanceof Codici_iva_fatture_acquistiVBulk ||
		  bulk instanceof Quadri_va_vfVBulk)) return bulk;
		
    //ricavo il progressivo unico pg_stampa
    java.math.BigDecimal pg_Stampa= getSequence(userContext);
	if (bulk instanceof IPrintable)
		((IPrintable)bulk).setId_report(pg_Stampa);

	try {
		callTabCodIvaAcquisti(userContext, bulk);

		Vp_liquid_iva_annualeHome home = (Vp_liquid_iva_annualeHome)getHome(userContext, Vp_liquid_iva_annualeBulk.class);
		
		BulkList lista = null;
		
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_A_CLAUSE);
		bulk.setElenco(lista);
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_B_CLAUSE);
		Vp_liquid_iva_annualeBulk liquid = (Vp_liquid_iva_annualeBulk)lista.get(0);
		if (lista != null && !lista.isEmpty())
			bulk.setImporto(liquid.getImponibile());

		if (bulk instanceof Quadri_va_vfVBulk) {
			Quadri_va_vfVBulk quadro = (Quadri_va_vfVBulk)bulk;
			quadro.setIm_tot_iva(liquid.getIm_iva());
			quadro.setIm_totale(liquid.getIm_totale());
			
			lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_C_CLAUSE);
			quadro.setElencoSecondario(lista);
		}

	} catch (Throwable t) {
		throw handleException(t);
	}

	bulk.setRistampabile(true);

	return bulk;
}

/**
 * tabCodIvaVendite method comment.
 */
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk 
	tabCodIvaVendite(
		it.cnr.jada.UserContext userContext, 
		it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk bulk)
		throws it.cnr.jada.comp.ComponentException {

	if (!(bulk instanceof Codici_iva_fatture_emesseVBulk ||
		  bulk instanceof Quadri_va_veVBulk)) return bulk;
		
    //ricavo il progressivo unico pg_stampa
    java.math.BigDecimal pg_Stampa= getSequence(userContext);
	if (bulk instanceof IPrintable)
		((IPrintable)bulk).setId_report(pg_Stampa);

	try {
		callTabCodIvaVendite(userContext, bulk);

		Vp_liquid_iva_annualeHome home = (Vp_liquid_iva_annualeHome)getHome(userContext, Vp_liquid_iva_annualeBulk.class);
		
		BulkList lista = null;
		
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_A_CLAUSE);
		bulk.setElenco(lista);
		lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_B_CLAUSE);
		Vp_liquid_iva_annualeBulk liquid = (Vp_liquid_iva_annualeBulk)lista.get(0);
		if (lista != null && !lista.isEmpty())
			bulk.setImporto(liquid.getImponibile());

		if (bulk instanceof Quadri_va_veVBulk) {
			Quadri_va_veVBulk quadro = (Quadri_va_veVBulk)bulk;
			quadro.setIm_tot_iva(liquid.getIm_iva());
			quadro.setIm_totale(liquid.getIm_totale());
			
			lista = home.findDettagliPerTipo(bulk, Liquidazione_iva_annualeVBulk.TIPO_C_CLAUSE);
			quadro.setElencoSecondario(lista);
		}

	} catch (Throwable t) {
		throw handleException(t);
	}

	bulk.setRistampabile(true);
	
	return bulk;
}
public SQLBuilder selectLinea_di_attivitaByClause (UserContext userContext,Liquidazione_iva_ripart_finBulk ripartizione, WorkpackageBulk linea_di_attivita, CompoundFindClause clause) throws ComponentException, PersistencyException{	
	SQLBuilder sql = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
	sql.addClause( clause );
	sql.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
	sql.addSQLClause(FindClause.AND, "TI_GESTIONE", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
	sql.addSQLClause(FindClause.AND, "CD_CENTRO_RESPONSABILITA", SQLBuilder.LIKE, ripartizione.getCd_unita_organizzativa()+"%");
	sql.addTableToHeader("NATURA");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
	sql.addSQLClause(FindClause.AND,"NATURA.TIPO",SQLBuilder.EQUALS,NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);			
	 // Obbligatorio cofog sulle GAE
	try{
		if(((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).isCofogObbligatorio(userContext))
			sql.addSQLClause("AND","CD_COFOG",SQLBuilder.ISNOTNULL,null);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	return sql;
}
}