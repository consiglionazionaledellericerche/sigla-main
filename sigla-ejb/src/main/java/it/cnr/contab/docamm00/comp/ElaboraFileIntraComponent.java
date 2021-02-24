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

package it.cnr.contab.docamm00.comp;


import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistHome;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Bulk;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Home;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatHome;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroBulk;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroHome;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewBulk;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewHome;
import it.cnr.contab.docamm00.intrastat.bulk.FatturaAttivaIntraSBulk;
import it.cnr.contab.docamm00.intrastat.bulk.FatturaAttivaIntraSHome;
import it.cnr.contab.docamm00.intrastat.bulk.FatturaPassivaIntraSBulk;
import it.cnr.contab.docamm00.intrastat.bulk.FatturaPassivaIntraSHome;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraHome;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraHome;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaHome;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ElaboraFileIntraComponent extends it.cnr.jada.comp.CRUDComponent {

	public List EstraiLista(UserContext uc, OggettoBulk bulk) throws ComponentException {
		SQLBuilder sql=null;
		if(bulk instanceof VIntrastatBulk){
			VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
			sql = home.createSQLBuilder();
	//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
	//		sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
			sql.openParenthesis("AND");
			sql.openParenthesis("AND");
			sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
			sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
			sql.closeParenthesis();
			sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
			sql.closeParenthesis();
			
			sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
			try {
				return home.fetchAll(sql);
			} catch (PersistencyException e) {
				handleException(e);
			}
		}else if(bulk instanceof VSpesometroNewBulk){
			VSpesometroNewHome home = (VSpesometroNewHome)getHome(uc,VSpesometroNewBulk.class);
			VSpesometroNewBulk dett=((VSpesometroNewBulk) bulk);
			try {
				
				sql = home.createSQLBuilder();
				sql.addSQLBetweenClause("AND","V_SPESOMETRO_NEW.DATA",dett.getDa_data(),dett.getA_data());
				sql.addSQLClause("AND","TIPO",sql.EQUALS,dett.getTipo());
				//sql.addSQLClause("AND","PROG_FATTURA",sql.GREATER_EQUALS,"500"); 
				sql.addOrderBy("TIPO,esercizio,data,prog_fattura,percentuale,natura");
				return home.fetchAll(sql);
			} catch (PersistencyException e) {
				handleException(e);
			}
		}
		return null;
	}
	
	public List SezioneUnoAcquisti(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.EQUALS,((VIntrastatBulk)bulk).getMese());
		sql.addClause("AND", "tipo",sql.EQUALS,"P");
		sql.addClause("AND", "tiFattura",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA);
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_BENI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneDueAcquisti(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
//		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		sql.openParenthesis("AND");
		sql.addClause("AND", "tipo",sql.EQUALS,"PS");
		sql.addClause("OR", "tipo",sql.EQUALS,"P");
		sql.closeParenthesis();
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_BENI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneTreAcquisti(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		//sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		//sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
		
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		sql.addClause("AND", "tipo",sql.EQUALS,"P");
		sql.addClause("AND", "tiFattura",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA);
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_SERVIZI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneQuattroAcquisti(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
//		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		sql.addClause("AND", "tipo",sql.EQUALS,"PS");
		/*sql.openParenthesis("AND");
		sql.addClause("AND", "tipo",sql.EQUALS,"PS");
		sql.addClause("OR", "tiFattura",sql.NOT_EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA);
		sql.closeParenthesis();*/
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_SERVIZI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneUnoVendite(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.EQUALS,((VIntrastatBulk)bulk).getMese());
		sql.addClause("AND", "tipo",sql.EQUALS,"A");
		sql.addClause("AND", "tiFattura",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_FATTURA_ATTIVA);
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.FATTURA_DI_BENI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneDueVendite(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
//		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		sql.openParenthesis("AND");
		sql.addClause("AND", "tipo",sql.EQUALS,"A");
		sql.addClause("OR", "tipo",sql.EQUALS,"AS");
		sql.closeParenthesis();
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.FATTURA_DI_BENI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneTreVendite(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
//		sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS_EQUALS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		
		sql.addClause("AND", "tipo",sql.EQUALS,"A");
		sql.addClause("AND", "tiFattura",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_FATTURA_ATTIVA);
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_SERVIZI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List SezioneQuattroVendite(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntrastatHome home = (VIntrastatHome)getHome(uc,VIntrastatBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
//		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
//		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.LESS,((VIntrastatBulk)bulk).getMese());
		sql.closeParenthesis();
		sql.addClause("OR", "esercizio", sql.LESS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.closeParenthesis();
		
		/*sql.openParenthesis("AND");
		sql.addClause("AND", "tipo",sql.EQUALS,"AS");
		sql.addClause("OR", "tiFattura",sql.NOT_EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA);
		sql.closeParenthesis();*/
		sql.addClause("AND", "tipo",sql.EQUALS,"AS");
		sql.addClause("AND", "tiBeneServizio",sql.EQUALS,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.FATTURA_DI_SERVIZI);
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}
	public List EstraiListaIntra12(UserContext uc, OggettoBulk bulk) throws ComponentException {

		VIntra12Home home = (VIntra12Home)getHome(uc,VIntra12Bulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addClause("AND", "mese",sql.EQUALS,((VIntra12Bulk)bulk).getMese());
		sql.addOrderBy("cd_bene_servizio");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
		}
	return null;
	}

	
    public java.util.Date recuperoMaxDtPagamentoLiq(UserContext uc, OggettoBulk bulk) throws ComponentException 
    {
    	try {
    	Liquidazione_ivaHome home = (Liquidazione_ivaHome)getHome(uc,Liquidazione_ivaBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("v_unita_organizzativa_valida");
		sql.addSQLClause("AND","V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA",SQLBuilder.EQUALS,it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
		sql.addSQLClause("AND","V_UNITA_ORGANIZZATIVA_VALIDA.FL_CDS",SQLBuilder.EQUALS,"N");
		sql.addSQLClause("AND","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc));
		sql.addSQLJoin("V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA","LIQUIDAZIONE_IVA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, Stampa_registri_ivaVBulk.DEFINITIVO);
    	sql.addSQLClause("AND","DT_INIZIO",SQLBuilder.EQUALS,new java.sql.Timestamp(new GregorianCalendar(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(uc).intValue(),((VIntra12Bulk) bulk).getMese().intValue()-1,01).getTime().getTime()));
    	sql.openParenthesis("AND");
    	sql.addSQLClause("AND","TIPO_LIQUIDAZIONE",SQLBuilder.EQUALS,Stampa_registri_ivaVBulk.SEZIONALI_SERVIZI_NON_RESIDENTI);
    	sql.addSQLClause("OR","TIPO_LIQUIDAZIONE",SQLBuilder.EQUALS,Stampa_registri_ivaVBulk.SEZIONALI_BENI_INTRA_UE);
    	sql.closeParenthesis();		 
    	java.util.Date maxData=null;
    	for (Iterator i=home.fetchAll(sql).iterator();i.hasNext();){
    		Liquidazione_ivaBulk liq=(Liquidazione_ivaBulk)i.next();
    		MandatoIHome home_man = (MandatoIHome)getHome(uc,MandatoIBulk.class);
    		SQLBuilder sql_man = home_man.createSQLBuilder();
    		sql_man.addTableToHeader("MANDATO_RIGA");
    		sql_man.addSQLClause("AND","MANDATO_RIGA.ESERCIZIO_DOC_AMM",SQLBuilder.EQUALS,liq.getEsercizio_doc_amm());
    		sql_man.addSQLClause("AND","MANDATO_RIGA.CD_CDS_DOC_AMM",SQLBuilder.EQUALS,liq.getCd_cds_doc_amm());
    		sql_man.addSQLClause("AND","MANDATO_RIGA.CD_UO_DOC_AMM",SQLBuilder.EQUALS,liq.getCd_uo_doc_amm());
    		sql_man.addSQLClause("AND","MANDATO_RIGA.CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS,liq.getCd_tipo_documento());
    		sql_man.addSQLClause("AND","MANDATO_RIGA.PG_DOC_AMM",SQLBuilder.EQUALS,liq.getPg_doc_amm());
    		sql_man.addSQLClause("AND","MANDATO.STATO",SQLBuilder.EQUALS,MandatoBulk.STATO_MANDATO_PAGATO);
    		sql_man.addSQLJoin("MANDATO.ESERCIZIO",SQLBuilder.EQUALS,"MANDATO_RIGA.ESERCIZIO");
    		sql_man.addSQLJoin("MANDATO.CD_CDS",SQLBuilder.EQUALS,"MANDATO_RIGA.CD_CDS");
    		sql_man.addSQLJoin("MANDATO.PG_MANDATO",SQLBuilder.EQUALS,"MANDATO_RIGA.PG_MANDATO");
    		for (Iterator i_man=home_man.fetchAll(sql_man).iterator();i_man.hasNext();){
    			MandatoIBulk man=(MandatoIBulk)i_man.next();
    			if(maxData==null || maxData.after(man.getDt_pagamento())){
    				maxData=man.getDt_pagamento();
    			}
    		}
    	}
		return maxData;
    } catch (Exception e) {
		handleException(e);
	}
    return null;
    }
	public void confermaElaborazione(UserContext context, VIntrastatBulk bulk) throws ComponentException, PersistencyException {
		
			Fattura_passiva_intraHome home=(Fattura_passiva_intraHome)getHome(context, Fattura_passiva_intraBulk.class);
			Fattura_attiva_intraHome home_att=(Fattura_attiva_intraHome)getHome(context, Fattura_attiva_intraBulk.class);
			FatturaPassivaIntraSHome home_s=(FatturaPassivaIntraSHome)getHome(context, FatturaPassivaIntraSBulk.class);
			FatturaAttivaIntraSHome home_att_s=(FatturaAttivaIntraSHome)getHome(context, FatturaAttivaIntraSBulk.class);
			String prot=null;
			Integer conta=0;
			
			if(bulk.getNrProtocolloAcq()==null &&bulk.getNrProtocolloVen()==null)
				throw new ApplicationException("Non sono stati indicati ne il numero Protocollo Acq/Serv. ricevuti, ne il numero Protocollo Cessioni/Serv. resi.");
			if(bulk.getNrProtocolloAcq()!=null){
				prot=bulk.getNrProtocolloAcq();
			//else
				//throw new ApplicationException("Non è stato indicato il numero Protocollo Acq/Serv. ricevuti");
			// per gestire caso in cui un solo flusso viene accettato
			for (Iterator i=(SezioneUnoAcquisti(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			Fattura_passiva_intraBulk fat=(Fattura_passiva_intraBulk)home.findByPrimaryKey(new Fattura_passiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
    			fat.setNr_protocollo(prot);
    			fat.setNr_progressivo(conta);
    			fat.setFl_inviato(Boolean.TRUE);
    			fat.setToBeUpdated();
    			updateBulk(context, fat);
			}
			conta=0;
			for (Iterator i=(SezioneDueAcquisti(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			if(det.getPgStorico()==0){
	    			Fattura_passiva_intraBulk fat=(Fattura_passiva_intraBulk)home.findByPrimaryKey(new Fattura_passiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
	    			if (fat!=null){
		    			fat.setNr_protocollo(prot);
		    			fat.setNr_progressivo(conta);
		    			fat.setFl_inviato(Boolean.TRUE);
		    			fat.setToBeUpdated();
		    			updateBulk(context, fat);
	    			}
    			}else
    			{
    				FatturaPassivaIntraSBulk fats=(FatturaPassivaIntraSBulk)home_s.findByPrimaryKey(new FatturaPassivaIntraSBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra(),det.getPgStorico()));
    				fats.setNrProtocollo(prot);
	    			fats.setNrProgressivo(conta);
	    			fats.setFlInviato(Boolean.TRUE);
	    			fats.setToBeUpdated();
	    			updateBulk(context, fats);
    			}
			}
			conta=0;
			for (Iterator i=(SezioneTreAcquisti(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			Fattura_passiva_intraBulk fat=(Fattura_passiva_intraBulk)home.findByPrimaryKey(new Fattura_passiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
    			fat.setNr_protocollo(prot);
    			fat.setNr_progressivo(conta);
    			fat.setFl_inviato(Boolean.TRUE);
    			fat.setToBeUpdated();
    			updateBulk(context, fat);
			}
			conta=0;
			for (Iterator i=(SezioneQuattroAcquisti(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			if(det.getPgStorico()==0){
	    			Fattura_passiva_intraBulk fat=(Fattura_passiva_intraBulk)home.findByPrimaryKey(new Fattura_passiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
	    			if (fat!=null){
		    			fat.setNr_protocollo(prot);
		    			fat.setNr_progressivo(conta);
		    			fat.setFl_inviato(Boolean.TRUE);
		    			fat.setToBeUpdated();
		    			updateBulk(context, fat);
	    			}
    			}else
    			{
    				FatturaPassivaIntraSBulk fats=(FatturaPassivaIntraSBulk)home_s.findByPrimaryKey(new FatturaPassivaIntraSBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra(),det.getPgStorico()));
    				fats.setNrProtocollo(prot);
	    			fats.setNrProgressivo(conta);
	    			fats.setFlInviato(Boolean.TRUE);
	    			fats.setToBeUpdated();
	    			updateBulk(context, fats);
    			}
			}
			} //fine (bulk.getNrProtocolloAcq()!=null	
			conta=0;
			// per gestire caso in cui un solo flusso viene accettato
			if(bulk.getNrProtocolloVen()!=null){
				prot=bulk.getNrProtocolloVen();
			//else
				//throw new ApplicationException("Non è stato indicato il numero Protocollo Cessioni/Serv. resi");
			for (Iterator i=(SezioneUnoVendite(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			Fattura_attiva_intraBulk fat=(Fattura_attiva_intraBulk)home_att.findByPrimaryKey(new Fattura_attiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
    			fat.setNr_protocollo(prot);
    			fat.setNr_progressivo(conta);
    			fat.setFl_inviato(Boolean.TRUE);
    			fat.setToBeUpdated();
    			updateBulk(context, fat);
			}
			conta=0;
			for (Iterator i=(SezioneDueVendite(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			if(det.getPgStorico()==0){
    				Fattura_attiva_intraBulk fat=(Fattura_attiva_intraBulk)home_att.findByPrimaryKey(new Fattura_attiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
    				if (fat!=null){
		    			fat.setNr_protocollo(prot);
		    			fat.setNr_progressivo(conta);
		    			fat.setFl_inviato(Boolean.TRUE);
		    			fat.setToBeUpdated();
		    			updateBulk(context, fat);
	    			}
    			}else
    			{
    				FatturaAttivaIntraSBulk fats=(FatturaAttivaIntraSBulk)home_att_s.findByPrimaryKey(new FatturaAttivaIntraSBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra(),det.getPgStorico()));
    				fats.setNrProtocollo(prot);
	    			fats.setNrProgressivo(conta);
	    			fats.setFlInviato(Boolean.TRUE);
	    			fats.setToBeUpdated();
	    			updateBulk(context, fats);
    			}
			}
			conta=0;
			for (Iterator i=(SezioneTreVendite(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			Fattura_attiva_intraBulk fat=(Fattura_attiva_intraBulk)home_att.findByPrimaryKey(new Fattura_attiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
    			fat.setToBeUpdated();
    			fat.setNr_protocollo(prot);
    			fat.setNr_progressivo(conta);
    			fat.setFl_inviato(Boolean.TRUE);
    			fat.setToBeUpdated();
    			updateBulk(context, fat);
			}		
			conta=0;
			for (Iterator i=(SezioneQuattroVendite(context, bulk)).iterator();i.hasNext();){
				conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			if(det.getPgStorico()==0){
	    			Fattura_attiva_intraBulk fat=(Fattura_attiva_intraBulk)home_att.findByPrimaryKey(new Fattura_attiva_intraBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra()));
	    			if (fat!=null){
		    			fat.setNr_protocollo(prot);
		    			fat.setNr_progressivo(conta);
		    			fat.setFl_inviato(Boolean.TRUE);
		    			fat.setToBeUpdated();
		    			updateBulk(context, fat);
	    			}
    			}else
    			{
    				FatturaAttivaIntraSBulk fats=(FatturaAttivaIntraSBulk)home_att_s.findByPrimaryKey(new FatturaAttivaIntraSBulk(det.getCd_cds(),det.getCd_unita_organizzativa(),det.getEsercizio(),det.getPg_fattura(),det.getPg_riga_intra(),det.getPgStorico()));
    				fats.setNrProtocollo(prot);
	    			fats.setNrProgressivo(conta);
	    			fats.setFlInviato(Boolean.TRUE);
	    			fats.setToBeUpdated();
	    			updateBulk(context, fats);
    			}
			}
			}//fine (bulk.getNrProtocollVen()!=null
			it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRASTAT);
				config.setIm01(new BigDecimal(bulk.getNrProtocollo()));
				config.setToBeUpdated();
				updateBulk(context, config);
				// se ribaltata la configurazione aggiorno il valore anche per esercizio +1
				config=Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context)+1, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRASTAT);
				if(config!=null){
					config.setIm01(new BigDecimal(bulk.getNrProtocollo()));
					config.setToBeUpdated();
					updateBulk(context, config);
				}
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			
		
	}

	public List EstraiBlacklist(UserContext context, OggettoBulk bulk,OggettoBulk bulkterzo)  throws ComponentException {
		if(bulk instanceof VFatcomBlacklistBulk){
		// Estrazione antecedente al 2014
				VFatcomBlacklistHome home = (VFatcomBlacklistHome)getHome(context,VFatcomBlacklistBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
				sql.addClause("AND", "mese",sql.EQUALS,((VFatcomBlacklistBulk)bulk).getMese());
				if(bulkterzo !=null && (bulkterzo instanceof TerzoBulk)){
					TerzoBulk terzo=(TerzoBulk)bulkterzo;
					if(terzo.getCd_terzo()!=null)
						sql.addClause("AND", "cd_terzo", sql.EQUALS,terzo.getCd_terzo());
				}
				sql.addOrderBy("esercizio,mese,cd_terzo,tipo,bene_servizio");
				try {
					return home.fetchAll(sql);
				} catch (PersistencyException e) {
					handleException(e);
				}
		}else if (bulk instanceof VSpesometroBulk){
			//nuova versione Comunicazione Polivalente anche per BlackList
			VSpesometroHome home = (VSpesometroHome)getHome(context,VSpesometroBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
			if(((VSpesometroBulk)bulk).isFlBlacklist()){
				if(((VSpesometroBulk)bulk).getMese()!=null)
					sql.addClause("AND", "mese",sql.EQUALS,((VSpesometroBulk)bulk).getMese());
				sql.addClause("AND", "tipoFiscalita",sql.EQUALS,"FS");
			}else{
				sql.addClause("AND", "tipoFiscalita",sql.NOT_EQUALS,"FS");
				sql.addClause("AND", "mese",sql.ISNULL,null);
			}
			sql.addOrderBy("esercizio,quadro,tipo,ti_bene_servizio,prog");
			try {
				return home.fetchAll(sql);
			} catch (PersistencyException e) {
				handleException(e);
			}
		}
					
	return null;
	}

	public JAXBElement<DatiFatturaType> creaDatiFatturaType(UserContext context,
			VSpesometroNewBulk det) throws ComponentException {
	try {

				ObjectFactory factory = new ObjectFactory();
				TerzoBulk terzoCnr = ((TerzoHome)getHome( context, TerzoBulk.class)).findTerzoEnte();
				DatiFatturaType fatturaType = factory.createDatiFatturaType();
				fatturaType.setVersione( VersioneType.DAT_20);
				
				CessionarioCommittenteDTEType cessionario=factory.createCessionarioCommittenteDTEType();
				DatiFatturaBodyDTEType fatturaBody=factory.createDatiFatturaBodyDTEType();
				DatiGeneraliDTEType  datiGenerali =factory.createDatiGeneraliDTEType();
				DatiGeneraliDTEType datiGeneraliOld =factory.createDatiGeneraliDTEType();
				DatiRiepilogoType datiRiepilogo = factory.createDatiRiepilogoType();
				TipoDocumentoType tipoDoc;
				DatiIVAType datiIva=factory.createDatiIVAType();
				DTEType emesse = factory.createDTEType();
				
				DTRType ricevute= factory.createDTRType();
				CedentePrestatoreDTRType fornitore = factory.createCedentePrestatoreDTRType();
				DatiFatturaBodyDTRType fatturaPBody=factory.createDatiFatturaBodyDTRType();
				DatiGeneraliDTRType datiGeneraliP =factory.createDatiGeneraliDTRType();
				DatiGeneraliDTRType datiGeneraliPOld =factory.createDatiGeneraliDTRType();
				DatiRiepilogoType datiRiepilogoP = factory.createDatiRiepilogoType();
				DatiIVAType datiIvaP=factory.createDatiIVAType();
				List fatture_test= EstraiLista(context,det);
				if (fatture_test!=null && fatture_test.size()!=0){
					
					for (Iterator i=fatture_test.iterator();i.hasNext();){
						VSpesometroNewBulk test=(VSpesometroNewBulk)i.next();
						//TerzoBulk terzoUo = ((TerzoBulk)findByPrimaryKey(context, new TerzoBulk(test.getTerzo_uo().intValue())));
						if (test.getTipo().compareTo("ATTIVA")==0){
								if (datiGeneraliOld.getNumero() == null || datiGeneraliOld.getNumero().compareTo(test.getProg_fattura().toString())!=0 
										|| !datiGeneraliOld.getData().equals(convertDateToXmlGregorian(test.getData()))){
									cessionario= new CessionarioCommittenteDTEType();
									fatturaBody=new DatiFatturaBodyDTEType();
									datiGenerali= new DatiGeneraliDTEType();
									datiRiepilogo=new DatiRiepilogoType();
								} else{
									datiRiepilogo=new DatiRiepilogoType();
								}
								datiIva = new DatiIVAType();
							    CedentePrestatoreDTEType cedente = factory.createCedentePrestatoreDTEType();
								IdentificativiFiscaliITType idFiscale=factory.createIdentificativiFiscaliITType();
								IdFiscaleITType p_iva= factory.createIdFiscaleITType();
								p_iva.setIdCodice(terzoCnr.getAnagrafico().getPartita_iva());
								p_iva.setIdPaese(NazioneITType.IT);
								idFiscale.setIdFiscaleIVA(p_iva);
								idFiscale.setCodiceFiscale(terzoCnr.getAnagrafico().getCodice_fiscale());
							    AltriDatiIdentificativiITType altrDatiId=factory.createAltriDatiIdentificativiITType();
								altrDatiId.setDenominazione(substring80(terzoCnr.getAnagrafico().getRagione_sociale()));
								cedente.setAltriDatiIdentificativi(altrDatiId);
								cedente.setIdentificativiFiscali(idFiscale);
								emesse.setCedentePrestatoreDTE(cedente);
							
								IdentificativiFiscaliNoIVAType idFiscale_cli=factory.createIdentificativiFiscaliNoIVAType();
								IdFiscaleType p_iva_cli= factory.createIdFiscaleType();
								if(test.getCod_naz()!=null)
									p_iva_cli.setIdPaese(new String(test.getCod_naz()));
							
								if(test.getPartita_iva()!=null){
									p_iva_cli.setIdCodice(test.getPartita_iva());
									if(test.getCod_naz()!=null)
										p_iva_cli.setIdPaese(new String(test.getCod_naz()));
									idFiscale_cli.setIdFiscaleIVA(p_iva_cli);
								} 
								else 	if(test.getCod_naz()!=null && 	test.getCod_naz().compareTo(NazioneITType.IT.value())!=0){
											idFiscale_cli.setCodiceFiscale(null);
											if(test.getPartita_iva()==null){
												p_iva_cli.setIdCodice(substring28(test.getCognome()==null ? test.getRagione_sociale():test.getCognome()));
												idFiscale_cli.setIdFiscaleIVA(p_iva_cli); 
											}
									}
									else 
											idFiscale_cli.setCodiceFiscale(test.getCodice_fiscale());
							
								cessionario.setIdentificativiFiscali(idFiscale_cli);

							    AltriDatiIdentificativiType altrDatiId_cli=factory.createAltriDatiIdentificativiType();
								if(test.getRagione_sociale()!=null)
									altrDatiId_cli.setDenominazione(substring80(test.getRagione_sociale()));
								else{
									altrDatiId_cli.setCognome(substring60(test.getCognome()));
									altrDatiId_cli.setNome(substring60(test.getNome()));
								}
								IndirizzoType ind_cli = factory.createIndirizzoType();
								ind_cli.setNazione(test.getStato_residenza());
								ind_cli.setComune(substring60(test.getComune_sede()));
								ind_cli.setIndirizzo(substring60(test.getIndirizzo_sede()));
								ind_cli.setNumeroCivico(test.getNumero_civico_sede());
								ind_cli.setProvincia(test.getProvincia());
								altrDatiId_cli.setSede(ind_cli);
								datiGenerali.setData(convertDateToXmlGregorian(test.getData()));
								datiGenerali.setNumero(test.getProg_fattura().toString());
								
								tipoDoc=TipoDocumentoType.valueOf(test.getTi_fattura());
								datiGenerali.setTipoDocumento(tipoDoc);
								datiGeneraliOld=datiGenerali;
								datiRiepilogo.setImponibileImporto(test.getImponibile_fa().setScale(2));
								if(test.getNatura()!=null){
								   datiRiepilogo.setNatura(NaturaType.fromValue(test.getNatura()));
								   datiRiepilogo.setDetraibile(null); //??
								}else
								   datiRiepilogo.setDetraibile(test.getPerc_detra().setScale(2));
								datiIva.setAliquota(test.getPercentuale().setScale(2));
								datiIva.setImposta(test.getIva_fa().setScale(2));
								datiRiepilogo.setDatiIVA(datiIva);
								datiRiepilogo.setEsigibilitaIVA(EsigibilitaIVAType.valueOf(test.getSplit()));
								datiRiepilogo.setDeducibile(null);//??
								
								fatturaBody.getDatiRiepilogo().add(datiRiepilogo);
								fatturaBody.setDatiGenerali(datiGenerali);
								if (!cessionario.getDatiFatturaBodyDTE().contains(fatturaBody)){
								    cessionario.getDatiFatturaBodyDTE().add(fatturaBody);
								    cessionario.setAltriDatiIdentificativi(altrDatiId_cli);
								    emesse.getCessionarioCommittenteDTE().add(cessionario);			
								}
						}//attiva
						else{
							if (datiGeneraliPOld.getNumero() == null || datiGeneraliPOld.getNumero().compareTo(test.getNr_fattura_fornitore())!=0 
									|| !datiGeneraliPOld.getData().equals(convertDateToXmlGregorian(test.getDt_fattura_fornitore()))){
								fornitore= new CedentePrestatoreDTRType();
								fatturaPBody=new DatiFatturaBodyDTRType();
								datiGeneraliP= new DatiGeneraliDTRType(); 
								datiRiepilogoP=new DatiRiepilogoType();
							} else{
								datiRiepilogoP=new DatiRiepilogoType();
							}
							datiIvaP = new DatiIVAType();
							
							IdentificativiFiscaliType idFiscale_forn=factory.createIdentificativiFiscaliType();
							IdFiscaleType p_iva_forn= factory.createIdFiscaleType();
							if(test.getCod_naz()!=null)
								p_iva_forn.setIdPaese(new String(test.getCod_naz()));
						
							if(test.getPartita_iva()!=null){
								p_iva_forn.setIdCodice(test.getPartita_iva());
								if(test.getCod_naz()!=null)
									p_iva_forn.setIdPaese(new String(test.getCod_naz()));
								idFiscale_forn.setIdFiscaleIVA(p_iva_forn);
							} 
							else 	if(test.getCod_naz()!=null && 	test.getCod_naz().compareTo(NazioneITType.IT.value())!=0){
								idFiscale_forn.setCodiceFiscale(null);
										if(test.getPartita_iva()==null){
											p_iva_forn.setIdCodice(substring28(test.getCognome()==null ? test.getRagione_sociale():test.getCognome()));
											idFiscale_forn.setIdFiscaleIVA(p_iva_forn); 
										}
								}
								else 
									idFiscale_forn.setCodiceFiscale(test.getCodice_fiscale());
						
							fornitore.setIdentificativiFiscali(idFiscale_forn);

							AltriDatiIdentificativiType altrDatiIdforn=factory.createAltriDatiIdentificativiType();
							if(test.getRagione_sociale()!=null)
								altrDatiIdforn.setDenominazione(substring80(test.getRagione_sociale()));
							else
							{
								altrDatiIdforn.setCognome(substring60(test.getCognome()));
								altrDatiIdforn.setNome(substring60(test.getNome()));
							}
							IndirizzoType ind_forn = factory.createIndirizzoType();
							ind_forn.setNazione(test.getStato_residenza());
							ind_forn.setComune(substring60(test.getComune_sede()));
							ind_forn.setIndirizzo(substring60(test.getIndirizzo_sede()));
							ind_forn.setNumeroCivico(test.getNumero_civico_sede());
							ind_forn.setProvincia(test.getProvincia());
							altrDatiIdforn.setSede(ind_forn);
							datiGeneraliP.setData(convertDateToXmlGregorian(test.getDt_fattura_fornitore()));
							datiGeneraliP.setNumero(test.getNr_fattura_fornitore());
							datiGeneraliP.setDataRegistrazione(convertDateToXmlGregorian(test.getData()));
							tipoDoc=TipoDocumentoType.valueOf(test.getTi_fattura());
							datiGeneraliP.setTipoDocumento(tipoDoc);
							datiGeneraliPOld=datiGeneraliP;
							datiRiepilogoP.setImponibileImporto(test.getImponibile_fa().setScale(2));
							if(test.getNatura()!=null){
							   datiRiepilogoP.setNatura(NaturaType.fromValue(test.getNatura()));
							   datiRiepilogoP.setDetraibile(null); //??
							}else
							   datiRiepilogoP.setDetraibile(test.getPerc_detra().setScale(2));
							datiIvaP.setAliquota(test.getPercentuale().setScale(2));
							datiIvaP.setImposta(test.getIva_fa().setScale(2));
							datiRiepilogoP.setDatiIVA(datiIvaP);
							datiRiepilogoP.setEsigibilitaIVA(EsigibilitaIVAType.valueOf(test.getSplit()));
							datiRiepilogoP.setDeducibile(null);//??
							
							fatturaPBody.getDatiRiepilogo().add(datiRiepilogoP);
							fatturaPBody.setDatiGenerali(datiGeneraliP);
							if (!fornitore.getDatiFatturaBodyDTR().contains(fatturaPBody)){
								fornitore.getDatiFatturaBodyDTR().add(fatturaPBody);
								fornitore.setAltriDatiIdentificativi(altrDatiIdforn);
							    ricevute.getCedentePrestatoreDTR().add(fornitore);			
							}		
							
						    CessionarioCommittenteDTRType committente = factory.createCessionarioCommittenteDTRType();
							IdentificativiFiscaliITType idFiscale=factory.createIdentificativiFiscaliITType();
							IdFiscaleITType p_iva= factory.createIdFiscaleITType();
							p_iva.setIdCodice(terzoCnr.getAnagrafico().getPartita_iva());
							p_iva.setIdPaese(NazioneITType.IT);
							idFiscale.setIdFiscaleIVA(p_iva);
							idFiscale.setCodiceFiscale(terzoCnr.getAnagrafico().getCodice_fiscale());
							AltriDatiIdentificativiITType altrDatiId=factory.createAltriDatiIdentificativiITType();
							altrDatiId.setDenominazione(substring80(terzoCnr.getAnagrafico().getRagione_sociale()));
							committente.setAltriDatiIdentificativi(altrDatiId);
							committente.setIdentificativiFiscali(idFiscale);
							ricevute.setCessionarioCommittenteDTR(committente);
						
						}//passiva
						
					} //for
					
				}//end if fatture_test.size()!=0
				if (det.getTipo().compareTo("ATTIVA")==0)
						fatturaType.setDTE(emesse);
					else
						fatturaType.setDTR(ricevute);
				return factory.createDatiFattura(fatturaType);
			} catch(Exception e) {
				throw handleException(e);
			}
		
	}
	public XMLGregorianCalendar convertDateToXmlGregorian(Date d) throws DatatypeConfigurationException{
		
		if (d==null)
			return null;		
				
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(d));
						
	}	
	private String substring80(String rit) {
		if (rit==null) 
				return null;
			else 	
				return rit.length() > 80 ? rit.substring(0,80) : rit;
	}

	private String substring60(String rit) {
		if (rit==null) 
			return null;
		else 	
			return rit.length() > 60 ? rit.substring(0,60) : rit;
	}
	private String substring28(String rit) {
		if (rit==null) 
			return null;
		else 	
			return rit.length() > 28 ? rit.substring(0,28) : rit;
	}
}
	
