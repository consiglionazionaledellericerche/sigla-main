package it.cnr.contab.docamm00.comp;


import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistHome;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Bulk;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Home;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatHome;
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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

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
		
		sql.addOrderBy("TIPO desc,cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,ti_bene_servizio,cd_nomenclatura_combinata,cd_natura_transazione,cd_cpa");
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			handleException(e);
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
	public void confermaElaborazione(UserContext context, VIntrastatBulk bulk) throws ComponentException {
		try {
			Fattura_passiva_intraHome home=(Fattura_passiva_intraHome)getHome(context, Fattura_passiva_intraBulk.class);
			Fattura_attiva_intraHome home_att=(Fattura_attiva_intraHome)getHome(context, Fattura_attiva_intraBulk.class);
			FatturaPassivaIntraSHome home_s=(FatturaPassivaIntraSHome)getHome(context, FatturaPassivaIntraSBulk.class);
			FatturaAttivaIntraSHome home_att_s=(FatturaAttivaIntraSHome)getHome(context, FatturaAttivaIntraSBulk.class);
			Integer prot=0;
			if(bulk.getNrProtocolloAcq()!=null)
				prot=bulk.getNrProtocolloAcq();
			else
				throw new ApplicationException("Non è stato indicato il numero Protocollo Acq/Serv. ricevuti");
			Integer conta=0;
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
			conta=0;
			if(bulk.getNrProtocolloVen()!=null)
				prot=bulk.getNrProtocolloVen();
			else
				throw new ApplicationException("Non è stato indicato il numero Protocollo Cessioni/Serv. resi");
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
			it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRASTAT);
				config.setIm01(new BigDecimal(bulk.getNrProtocollo()));
				config.setToBeUpdated();
				updateBulk(context, config);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			
		} catch (Exception e) {
			handleException(e);
		}
	}

	public List EstraiBlacklist(UserContext context, OggettoBulk bulk,OggettoBulk bulkterzo)  throws ComponentException {
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
	return null;
	}
}