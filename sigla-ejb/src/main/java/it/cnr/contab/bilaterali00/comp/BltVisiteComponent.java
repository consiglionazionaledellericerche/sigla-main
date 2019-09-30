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

package it.cnr.contab.bilaterali00.comp;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaHome;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiHome;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzati_dettBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiHome;
import it.cnr.contab.bilaterali00.bulk.Blt_programma_visiteBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_programma_visiteHome;
import it.cnr.contab.bilaterali00.bulk.Blt_regole_diariaBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_visiteHome;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class BltVisiteComponent extends CRUDComponent {
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
			
			if (oggettobulk instanceof Blt_visiteBulk) {
				Blt_visiteBulk visita = (Blt_visiteBulk)oggettobulk; 

				visita.setAnnoVisita(((CNRUserContext)usercontext).getEsercizio());
				visita.getBltAutorizzatiDett().getBltAutorizzati().setBltProgetti(new Blt_progettiBulk());
				visita.setFlStampatoDocCandidatura(Boolean.FALSE);
				visita.setFlStampatoAutorizPartenza(Boolean.FALSE);
				visita.setFlStampatoProvvImpegno(Boolean.FALSE);
				visita.setFlStampatoProvvPagamAnt(Boolean.FALSE);
				visita.setFlStampatoProvvPagamento(Boolean.FALSE);
				visita.setFlStampatoAnnProvvImpegno(Boolean.FALSE);
				visita.setFlStampatoNotaAddebito(Boolean.FALSE);
				visita.setFlVisitaAnnullata(Boolean.FALSE);
				visita.setFlStampatoModelloContratto(Boolean.FALSE);
				visita.setFlPagamentoFineVisita(Boolean.FALSE);
				visita.setFlPagamentoConBonifico(Boolean.FALSE);
				visita.setFlPagamentoEnte(Boolean.FALSE);
				visita.setFlAutorizzDirettore(Boolean.FALSE);
				visita.setFlBrevePeriodo(Boolean.FALSE);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		if (oggettobulk instanceof Blt_visiteBulk) {
			Blt_visiteBulk visita = (Blt_visiteBulk)oggettobulk; 
			
			if (visita.getObbligazioneScadenzario()!=null && visita.getObbligazioneScadenzario().getObbligazione()!=null && 
					!visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
				try {
					visita.setModalitaPagamento(findModalitaSelezionate(usercontext, visita.getCdModalitaPag()));
					visita.setModalitaPagamentoAnticipo(findModalitaSelezionate(usercontext, visita.getCdModalitaPagAnt()));
					visita.setBanca(findBancaSelezionata(usercontext, visita.getTerzoPagamento().getCd_terzo(), visita.getPgBanca()));
					visita.setBancaAnticipo(findBancaSelezionata(usercontext, visita.getTerzoPagamento().getCd_terzo(), visita.getPgBancaAnt()));
			        initializeKeysAndOptionsInto(usercontext, (OggettoBulk) visita);
			    } catch (it.cnr.jada.persistency.PersistencyException e) {
			        throw handleException(visita, e);
			    } catch (it.cnr.jada.persistency.IntrospectionException e) {
			        throw handleException(visita, e);
			    }
			}
		}
		return oggettobulk;
	}
	
	public it.cnr.jada.persistency.sql.SQLBuilder selectBltAutorizzatiDett_bltAutorizzati_bltProgetti_bltAccordoByClause(UserContext userContext, Blt_visiteBulk visita, Blt_accordiBulk accordo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, accordo.getClass()).createSQLBuilder();

		SQLBuilder sqlExists = getHome(userContext, Blt_autorizzati_dettBulk.class).createSQLBuilder();
		sqlExists.resetColumns();
		sqlExists.addColumn("CD_ACCORDO");

		if (visita.getAnnoVisita()==null)
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.ISNULL, null);
		else
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.EQUALS,visita.getAnnoVisita());

		sql.addSQLINClause(FindClause.AND, "BLT_ACCORDI.CD_ACCORDO", sqlExists);

		sql.addClause( clauses );
		return sql;
	}
	
	public it.cnr.jada.persistency.sql.SQLBuilder selectBltAutorizzatiDett_bltAutorizzati_bltProgettiByClause(UserContext userContext, Blt_visiteBulk visita, Blt_progettiBulk progetto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, progetto.getClass()).createSQLBuilder();

		if (visita.getCdAccordo()==null)
			sql.addClause(FindClause.AND,"cd_accordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cd_accordo",SQLBuilder.EQUALS,visita.getCdAccordo());

		SQLBuilder sqlExists = getHome(userContext, Blt_autorizzati_dettBulk.class).createSQLBuilder();
		sqlExists.resetColumns();
		sqlExists.addColumn("CD_PROGETTO");
		sqlExists.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_ACCORDO","BLT_PROGETTI.CD_ACCORDO");

		if (visita.getAnnoVisita()==null)
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.ISNULL, null);
		else
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.EQUALS,visita.getAnnoVisita());

		sql.addSQLINClause(FindClause.AND, "BLT_PROGETTI.CD_PROGETTO", sqlExists);

		sql.addClause( clauses );
		return sql;
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectBltAutorizzatiDett_bltAutorizzati_terzoByClause(UserContext userContext, Blt_visiteBulk visita, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();

		SQLBuilder sqlExists = getHome(userContext, Blt_autorizzati_dettBulk.class).createSQLBuilder();
		sqlExists.resetColumns();
		sqlExists.addColumn("CD_TERZO");

		if (visita.getCdAccordo()==null)
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.CD_ACCORDO",SQLBuilder.ISNULL, null);
		else
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.CD_ACCORDO",SQLBuilder.EQUALS, visita.getCdAccordo());

		if (visita.getCdProgetto()==null)
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.CD_PROGETTO",SQLBuilder.ISNULL, null);
		else
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.CD_PROGETTO",SQLBuilder.EQUALS, visita.getCdProgetto());

		if (visita.getAnnoVisita()==null)
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.ISNULL, null);
		else
			sqlExists.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.EQUALS, visita.getAnnoVisita());

		sql.addSQLINClause(FindClause.AND, "TERZO.CD_TERZO", sqlExists);
		sql.addClause( clauses );
		return sql;
	}
	
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
	}
	
	private void validaVisita(UserContext usercontext,Blt_visiteBulk visita,Blt_visiteBulk visitaOld) throws ComponentException {
			if (visita.isVisitaStraniero() || visita.isVisitaUniversitario()){
				if (visita.getLuogoVisita()==null)
					throw handleException( new ApplicationException( "Il campo Luogo Visita è obbligatorio!") );
			}
	//		if (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero()){
	//		if (visita.getFlAccettazioneConvenzione()==null)
		//			throw handleException( new ApplicationException( "Il campo Convenzione Fiscale è obbligatorio!") );
			//	if (visita.getNumProtAccettConvenz()==null||visita.getDtProtAccettConvenz()==null)
			//		throw handleException( new ApplicationException( "Indicare il protocollo della lettera di "+(!visita.isConvenzioneAccettata()?"non ":" ")+"accettazione della convenzione!") );
	//		}
				
			if (!visita.getFlPagamentoConBonifico()) {
				if (visita.getFlPagamentoFineVisita())
					throw handleException( new ApplicationException( "Non è possibile effettuare un pagamento a fine visita con modalità di pagamento diverse dal bonifico. Selezionare il flag \"Pagamento tramite Bonifico\"!") );
				else if (visita.getImRimbSpeseAnt()!=null && visita.getImRimbSpeseAnt().compareTo(BigDecimal.ZERO)==1 && visita.getImRimbSpese().compareTo(BigDecimal.ZERO)==1)
					throw handleException( new ApplicationException( "Non è possibile effettuare il pagamento del saldo della visita con modalità di pagamento diverse dal bonifico. Selezionare il flag \"Pagamento tramite Bonifico\"!") );
			}
				
			Date lastDataProt=null;
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

			Date currentDay = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

			//LELLO DA RIPRISTINARE
			java.util.GregorianCalendar gc_currentDay = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			gc_currentDay.set(gc_currentDay.get(java.util.Calendar.YEAR), java.util.Calendar.DECEMBER, 30);
		//	currentDay = gc_currentDay.getTime();
			
			java.util.GregorianCalendar gc_mindata_protocollo = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			gc_mindata_protocollo.setTime(currentDay);
			gc_mindata_protocollo.add(java.util.Calendar.DAY_OF_YEAR,-31);
	
			//Controllo Data Protocollo Candidatura
			lastDataProt=visita.getDtProtCandidatura();
			java.util.GregorianCalendar gcCandidatura = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			gcCandidatura.setTime(visita.getDtProtCandidatura());

			if (visitaOld==null || visitaOld.getDtProtCandidatura()==null) {
				//LELLO DA RIPRISTINARE
//				if (gcCandidatura.before(gc_mindata_protocollo))
//					throw handleException( new ApplicationException( "La data di protocollo candidatura ("+sdf.format(visita.getDtProtCandidatura())+") deve essere uguale o successiva al ("+sdf.format(gc_mindata_protocollo.getTime())+")!") );
			} else if (visita.getDtProtCandidatura().before(visitaOld.getDtProtCandidatura())) {
				throw handleException( new ApplicationException( "La data di protocollo candidatura ("+sdf.format(visita.getDtProtCandidatura())+") deve essere uguale o successiva all'ultima inserita ("+sdf.format(visitaOld.getDtProtCandidatura())+")!") );
			}

			if (gcCandidatura.get(GregorianCalendar.YEAR)<visita.getAnnoVisita()-1||gcCandidatura.get(GregorianCalendar.YEAR)>visita.getAnnoVisita())
				throw handleException( new ApplicationException( "La data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+") deve appartenere all'anno "+(visita.getAnnoVisita()-1)+" o all'anno "+(visita.getAnnoVisita())+"!") );
			if (visita.getDtProtCandidatura().after(currentDay))
				throw handleException( new ApplicationException( "La data di protocollo candidatura ("+sdf.format(visita.getDtProtCandidatura())+") non deve essere successiva alla data odierna!") );
			
			//Controllo Data Inizio Visita
			java.util.GregorianCalendar gcIniVisita = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			gcIniVisita.setTime(visita.getDtIniVisita());

			if (visitaOld==null || visitaOld.getDtIniVisita()==null) {
				if (!visita.getDtIniVisita().after(currentDay))
					throw handleException( new ApplicationException( "La data di inizio visita ("+sdf.format(visita.getDtIniVisita())+") deve essere successiva alla data odierna!") );
			} else if (visita.getDtIniVisita().before(visitaOld.getDtIniVisita())) {
				throw handleException( new ApplicationException( "La data di inizio visita ("+sdf.format(visita.getDtIniVisita())+") deve essere uguale o successiva all'ultima inserita ("+sdf.format(visitaOld.getDtIniVisita())+")!") );
			}

			if (gcIniVisita.get(GregorianCalendar.YEAR)!=visita.getAnnoVisita())
				throw handleException( new ApplicationException( "La data di inizio visita ("+sdf.format(visita.getDtIniVisita())+") deve appartenere all'anno "+visita.getAnnoVisita()+"!") );
			if (!visita.getDtIniVisita().after(visita.getDtProtCandidatura()))
				throw handleException( new ApplicationException( "La data di inizio visita ("+sdf.format(visita.getDtIniVisita())+") deve essere successiva\nalla data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+")!") );
		
			//Controllo Data Fine Visita
			if (visita.getDtIniVisita().after(visita.getDtFinVisita()))
				throw handleException( new ApplicationException( "La data di fine visita ("+sdf.format(visita.getDtFinVisita())+") deve essere uguale o successiva a quella di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			
			if (visita.getDtIniVisitaEffettiva()!=null && visita.getDtFinVisitaEffettiva()!=null) {
				java.util.GregorianCalendar gcIniVisitaEffettiva = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
				gcIniVisitaEffettiva.setTime(visita.getDtIniVisitaEffettiva());

				if (visita.getDtIniVisitaEffettiva().before(visita.getDtIniVisita()) || visita.getDtIniVisitaEffettiva().after(visita.getDtFinVisita()))
					throw handleException( new ApplicationException( "La data di inizio visita effettiva ("+sdf.format(visita.getDtIniVisitaEffettiva())+") deve essere compresa tra la data inizio visita "+sdf.format(visita.getDtIniVisita())+" e la data fine visita "+sdf.format(visita.getDtFinVisita())+"!") );
				if (visita.getDtFinVisitaEffettiva().before(visita.getDtIniVisita()) || visita.getDtFinVisitaEffettiva().after(visita.getDtFinVisita()))
					throw handleException( new ApplicationException( "La data di fine visita effettiva ("+sdf.format(visita.getDtFinVisitaEffettiva())+") deve essere compresa tra la data inizio visita "+sdf.format(visita.getDtIniVisita())+" e la data fine visita "+sdf.format(visita.getDtFinVisita())+"!") );
				if (visita.getDtIniVisitaEffettiva().after(visita.getDtFinVisitaEffettiva()))
					throw handleException( new ApplicationException( "La data di fine visita effettiva ("+sdf.format(visita.getDtFinVisitaEffettiva())+") deve essere uguale o successiva a quella di inizio visita effettiva ("+sdf.format(visita.getDtIniVisitaEffettiva())+")!") );
			}
				
			if (visita.getDtProtAccettConvenz()!=null){
				if (visita.getDtProtAccettConvenz().after(lastDataProt))
					lastDataProt=visita.getDtProtAccettConvenz();
				if (visita.getDtProtCandidatura()==null)
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della convenzione ("+sdf.format(visita.getDtProtAccettConvenz())+") non può essere valorizzata in assenza\ndella data di protocollo della candidatura!") );
				if (visita.getDtProtCandidatura().after(visita.getDtProtAccettConvenz()))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della convenzione ("+sdf.format(visita.getDtProtAccettConvenz())+") deve essere successiva o uguale\nalla data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+")!") );
				if (visita.getDtProtAccettConvenz().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della convenzione ("+sdf.format(visita.getDtProtAccettConvenz())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtAccettConvenz()))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della convenzione ("+sdf.format(visita.getDtProtAccettConvenz())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtDispFin()!=null) {
				if (visita.getDtProtDispFin().after(lastDataProt))
					lastDataProt=visita.getDtProtDispFin();
				if (visita.getDtProtDispFin().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo della lettera delle disposizioni finanziarie ("+sdf.format(visita.getDtProtDispFin())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtProtCandidatura().after(visita.getDtProtDispFin()))
					throw handleException( new ApplicationException( "La data di protocollo della lettera delle disposizioni finanziarie ("+sdf.format(visita.getDtProtDispFin())+") deve essere successiva o uguale\nalla data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+")!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtDispFin()))
					throw handleException( new ApplicationException( "La data di protocollo della lettera delle disposizioni finanziarie ("+sdf.format(visita.getDtProtDispFin())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtTrasmissCandidatura()!=null){
				if (visita.getDtProtTrasmissCandidatura().after(lastDataProt))
					lastDataProt=visita.getDtProtTrasmissCandidatura();
				if (visita.getDtProtTrasmissCandidatura().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo della lettera di trasmissione della candidatura ("+sdf.format(visita.getDtProtTrasmissCandidatura())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtProtCandidatura().after(visita.getDtProtTrasmissCandidatura()))
					throw handleException( new ApplicationException( "La data di protocollo della lettera di trasmissione della candidatura ("+sdf.format(visita.getDtProtTrasmissCandidatura())+") deve essere successiva o uguale\nalla data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+")!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtTrasmissCandidatura()))
					throw handleException( new ApplicationException( "La data di protocollo della lettera di trasmissione della candidatura ("+sdf.format(visita.getDtProtTrasmissCandidatura())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

//			if (visita.getDtProtAccettDispFin()!=null) {
//				if (visita.getDtProtAccettDispFin().after(lastDataProt))
//					lastDataProt=visita.getDtProtAccettDispFin();
//				if (visita.getDtProtDispFin()==null)
//					throw handleException( new ApplicationException( "La data di protocollo di accettazione delle disposizioni finanziarie ("+sdf.format(visita.getDtProtAccettDispFin())+") non può essere valorizzata in assenza\ndella data di protocollo della lettera delle disposizioni finanziarie!") );
//				if (visita.getDtProtDispFin().after(visita.getDtProtAccettDispFin()))
//					throw handleException( new ApplicationException( "La data di protocollo di accettazione delle disposizioni finanziarie ("+sdf.format(visita.getDtProtAccettDispFin())+") deve essere successiva o uguale\nalla data di protocollo della lettera delle disposizioni finanziarie ("+sdf.format(visita.getDtProtDispFin())+")!") );
//				if (visita.getDtProtAccettDispFin().after(currentDay))
//					throw handleException( new ApplicationException( "La data di protocollo di accettazione delle disposizioni finanziarie ("+sdf.format(visita.getDtProtAccettDispFin())+") non deve essere successiva alla data odierna!") );
//				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtAccettDispFin()))
//					throw handleException( new ApplicationException( "La data di protocollo di accettazione delle disposizioni finanziarie ("+sdf.format(visita.getDtProtAccettDispFin())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
//			}

			if (visita.getDtProtAccettEnteStr()!=null) {
				if (visita.getDtProtAccettEnteStr().after(lastDataProt))
					lastDataProt=visita.getDtProtAccettEnteStr();
				if (visita.getDtProtTrasmissCandidatura()==null)
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+") non può essere valorizzata in assenza\ndella data di protocollo della lettera di trasmissione della candidatura!") );
				if (visita.getDtProtTrasmissCandidatura().after(visita.getDtProtAccettEnteStr()))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+") deve essere successiva o uguale\nalla data di protocollo della lettera di trasmissione della candidatura ("+sdf.format(visita.getDtProtTrasmissCandidatura())+")!") );
				if (visita.getDtProtAccettEnteStr().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtAccettEnteStr()))
					throw handleException( new ApplicationException( "La data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtAttribIncarico()!=null) {
				if (visita.getDtProtAttribIncarico().after(lastDataProt))
					lastDataProt=visita.getDtProtAttribIncarico();
				if (visita.getDtProtAttribIncarico().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtProtCandidatura().after(visita.getDtProtAttribIncarico()))
					throw handleException( new ApplicationException( "La data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+") deve essere successiva o uguale\nalla data di protocollo della candidatura ("+sdf.format(visita.getDtProtCandidatura())+")!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtAttribIncarico()))
					throw handleException( new ApplicationException( "La data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDataProtContratto()!=null) {
				if (visita.getDataProtContratto().after(lastDataProt))
					lastDataProt=visita.getDataProtContratto();
				if (visita.getDtProtAccettEnteStr()==null)
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") non può essere valorizzata in assenza\ndella data di protocollo di accettazione della candidatura!") );
				if (visita.getDtProtAttribIncarico()==null)
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") non può essere valorizzata in assenza\ndella data di protocollo di attribuzione incarico!") );
				if (visita.getDtProtAccettEnteStr().after(visita.getDataProtContratto()))
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") deve essere successiva o uguale\nalla data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+")!") );
				if (visita.getDtProtAttribIncarico().after(visita.getDataProtContratto()))
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") deve essere successiva o uguale\nalla data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+")!") );
				if (visita.getDataProtContratto().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDataProtContratto()))
					throw handleException( new ApplicationException( "La data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtNotaAddebitoAnt()!=null) {
				if (visita.getDtProtNotaAddebitoAnt().after(lastDataProt))
					lastDataProt=visita.getDtProtNotaAddebitoAnt();
				if (visita.getDtProtAccettEnteStr()==null)
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") non può essere valorizzata in assenza\ndella data di protocollo di accettazione della candidatura!") );
				if (visita.getDtProtAttribIncarico()==null)
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") non può essere valorizzata in assenza\ndella data di protocollo di attribuzione incarico!") );
				if (visita.getDtProtAccettEnteStr().after(visita.getDtProtNotaAddebitoAnt()))
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") deve essere successiva o uguale\nalla data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+")!") );
				if (visita.getDtProtAttribIncarico().after(visita.getDtProtNotaAddebitoAnt()))
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") deve essere successiva o uguale\nalla data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+")!") );
				if (visita.getDtProtNotaAddebitoAnt().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtNotaAddebitoAnt()))
					throw handleException( new ApplicationException( "La data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtNotaAddebito()!=null) {
				if (visita.getDtProtNotaAddebito().after(lastDataProt))
					lastDataProt=visita.getDtProtNotaAddebito();
				if (visita.isNotaAddebitoSaldoConAnticipoRequired()) { 
					if (visita.getDtProtAccettEnteStr()==null)
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") non può essere valorizzata in assenza\ndella data di protocollo di accettazione della candidatura!") );
					if (visita.getDtProtAttribIncarico()==null)
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") non può essere valorizzata in assenza\ndella data di protocollo di attribuzione incarico!") );
					if (visita.getDtProtAccettEnteStr().after(visita.getDtProtNotaAddebito()))
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") deve essere successiva o uguale\nalla data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+")!") );
					if (visita.getDtProtAttribIncarico().after(visita.getDtProtNotaAddebito()))
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") deve essere successiva o uguale\nalla data di protocollo di attribuzione incarico ("+sdf.format(visita.getDtProtAttribIncarico())+")!") );
					if (visita.getDtProtNotaAddebito().after(currentDay))
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") non deve essere successiva alla data odierna!") );
					if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtNotaAddebito()))
						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
				} else {
//					if (visita.getDtProtAttestatoSogg()==null)
//						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") non può essere valorizzata in assenza\ndella data di protocollo dell'attestato di soggiorno!") );
//					if (visita.getDtProtAttestatoSogg().after(visita.getDtProtNotaAddebito()))
//						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") deve essere successiva o uguale\nalla data di protocollo dell'attestato di soggiorno ("+sdf.format(visita.getDtProtAttestatoSogg())+")!") );
//					if (visita.getDtProtNotaAddebito().after(currentDay))
//						throw handleException( new ApplicationException( "La data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+") non deve essere successiva alla data odierna!") );
				}
			}

			if (visita.getDtProtAutorizPartenza()!=null) {
				if (visita.getDtProtAutorizPartenza().after(lastDataProt))
					lastDataProt=visita.getDtProtAutorizPartenza();
				if (visita.isVisitaDipendente() || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())) {
					if (visita.getDtProtAccettEnteStr()==null)
						throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non può essere valorizzata in assenza\ndella data di protocollo di accettazione della candidatura!") );
					if (visita.getDtProtAccettEnteStr().after(visita.getDtProtAutorizPartenza()))
						throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere successiva o uguale\nalla data di protocollo di accettazione della candidatura ("+sdf.format(visita.getDtProtAccettEnteStr())+")!") );
//					if (visita.isVisitaDipendente()) {
//						if (visita.getDtProtAccettDispFin()==null)
//							throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non può essere valorizzata in assenza\ndella data di protocollo di accettazione delle disposizioni finanziarie!") );
//						if (visita.getDtProtAccettDispFin().after(visita.getDtProtAutorizPartenza()))
//							throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere successiva o uguale\nalla data di protocollo di accettazione delle disposizioni finanziarie ("+sdf.format(visita.getDtProtAccettDispFin())+")!") );
//					}
				} else {
					if (visita.getDataProtContratto()==null)
						throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non può essere valorizzata in assenza\ndella data di protocollo del contratto!") );
					if (visita.getDataProtContratto().after(visita.getDtProtAutorizPartenza()))
						throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere successiva o uguale\nalla data di protocollo del contratto ("+sdf.format(visita.getDataProtContratto())+")!") );
					if (visita.isVisitaStraniero()) {
						if (visita.isNotaAddebitoAnticipoRequired()) { 
							if (visita.getDtProtNotaAddebitoAnt()==null)
								throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non può essere valorizzata in assenza\ndella data di protocollo della nota di addebito di anticipo!") );
							if (visita.getDtProtNotaAddebitoAnt().after(visita.getDtProtAutorizPartenza()))
								throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere successiva o uguale\nalla data di protocollo della nota di addebito di anticipo ("+sdf.format(visita.getDtProtNotaAddebitoAnt())+")!") );
						}
								if (visita.isNotaAddebitoSaldoConAnticipoRequired()) { 
								if (visita.getDtProtNotaAddebito()==null)
								throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non può essere valorizzata in assenza\ndella data di protocollo della nota di addebito!") );
							if (visita.getDtProtNotaAddebito().after(visita.getDtProtAutorizPartenza()))
								throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere successiva o uguale\nalla data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+")!") );
							}
					}
				}
				if (visita.getDtProtAutorizPartenza().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtAutorizPartenza()))
					throw handleException( new ApplicationException( "La data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtProvvImpegno()!=null) {
				if (visita.getDtProtProvvImpegno().after(lastDataProt))
					lastDataProt=visita.getDtProtProvvImpegno();
				if (visita.getDtProtAutorizPartenza()==null)
					throw handleException( new ApplicationException( "La data di protocollo del provvedimento di impegno ("+sdf.format(visita.getDtProtProvvImpegno())+") non può essere valorizzata in assenza\ndella data di protocollo dell'autorizzazione alla partenza!") );
				if (visita.getDtProtAutorizPartenza().after(visita.getDtProtProvvImpegno()))
					throw handleException( new ApplicationException( "La data di protocollo del provvedimento di impegno ("+sdf.format(visita.getDtProtProvvImpegno())+") deve essere successiva o uguale\nalla data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+")!") );
				if (visita.getDtProtProvvImpegno().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo del provvedimento di impegno ("+sdf.format(visita.getDtProtProvvImpegno())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtProvvImpegno()))
					throw handleException( new ApplicationException( "La data di protocollo del provvedimento di impegno ("+sdf.format(visita.getDtProtProvvImpegno())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtRimbSpese()!=null) {
				if (visita.getDtProtRimbSpese().after(lastDataProt))
					lastDataProt=visita.getDtProtRimbSpese();
				if (visita.getDtProtProvvImpegno()==null)
					throw handleException( new ApplicationException( "La data di protocollo della richiesta di rimborso spese ("+sdf.format(visita.getDtProtRimbSpese())+") non può essere valorizzata in assenza\ndella data di protocollo del provvedimento di impegno!") );
				if (visita.getDtProtProvvImpegno().after(visita.getDtProtRimbSpese()))
					throw handleException( new ApplicationException( "La data di protocollo della richiesta di rimborso spese ("+sdf.format(visita.getDtProtRimbSpese())+") deve essere successiva o uguale\nalla data di protocollo del provvedimento di impegno ("+sdf.format(visita.getDtProtProvvImpegno())+")!") );
				if (visita.getDtProtRimbSpese().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo della richiesta di rimborso spese ("+sdf.format(visita.getDtProtRimbSpese())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtFinVisita()!=null && !visita.getDtProtRimbSpese().after(visita.getDtFinVisita()))
					throw handleException( new ApplicationException( "La data di protocollo della richiesta di rimborso spese ("+sdf.format(visita.getDtProtRimbSpese())+") deve essere successiva\nalla data di fine visita ("+sdf.format(visita.getDtFinVisita())+")!") );
			}
			
			if (visita.getDtPagamSaldo()!=null) {
				if (visita.getDtPagamSaldo().after(lastDataProt))
					lastDataProt=visita.getDtPagamSaldo();
	//			if (visita.getDtPagamSaldo().before(currentDay))
	//				throw handleException( new ApplicationException( "La data di pagamento del compenso o del rimborso spese ("+sdf.format(visita.getDtPagamSaldo())+") deve essere successiva alla data odierna!"));
			}
			
			if (visita.getDtPagamAnt()!=null) {
				if (visita.getDtPagamAnt().after(lastDataProt))
					lastDataProt=visita.getDtPagamAnt();
	//			if (visita.getDtPagamAnt().before(currentDay))
	//				throw handleException( new ApplicationException( "La data di pagamento dell'anticipo del compenso ("+sdf.format(visita.getDtPagamAnt())+") deve essere successiva alla data odierna!"));
			}
			
			if (visita.getDtProtProvvPagamAnt()!=null) {
				if (visita.getDtProtProvvPagamAnt().after(lastDataProt))
					lastDataProt=visita.getDtProtProvvPagamAnt();
				if (visita.getDtProtAutorizPartenza()==null)
					throw handleException( new ApplicationException( "La data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+") non può essere valorizzata in assenza\ndella data di protocollo dell'autorizzazione alla partenza!") );
				if (visita.getDtProtAutorizPartenza().after(visita.getDtProtProvvPagamAnt()))
					throw handleException( new ApplicationException( "La data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+") deve essere successiva o uguale\nalla data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+")!") );
				if (visita.getDtProtProvvPagamAnt().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && !visita.getDtIniVisita().after(visita.getDtProtProvvPagamAnt()))
					throw handleException( new ApplicationException( "La data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+") deve essere precedente\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}

			if (visita.getDtProtProvvPagam()!=null) {
				if (visita.getDtProtProvvPagam().after(lastDataProt))
					lastDataProt=visita.getDtProtProvvPagam();
				if (visita.isVisitaDipendente() || visita.isVisitaUniversitario()) {
					if (visita.getDtProtRimbSpese()==null)
						throw handleException( new ApplicationException( "La data di protocollo del provvedimento di pagamento rimborso spese ("+sdf.format(visita.getDtProtProvvPagam())+") non può essere valorizzata in assenza\ndella data di protocollo della richiesta di rimborso spese!") );
					if (visita.getDtProtRimbSpese().after(visita.getDtProtProvvPagam()))
						throw handleException( new ApplicationException( "La data di protocollo del provvedimento di pagamento rimborso spese ("+sdf.format(visita.getDtProtProvvPagam())+") deve essere successiva o uguale\nalla data di protocollo della richiesta di rimborso spese ("+sdf.format(visita.getDtProtRimbSpese())+")!") );
				} else {
					if (!visita.isNotaAddebitoSaldoConAnticipoRequired()) {
						if (visita.isVisitaPagataAdEnteStraniero()){
							if (visita.getDtProtAutorizPartenza()==null)
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") non può essere valorizzata in assenza\ndella data di protocollo dell'autorizzazione alla partenza!") );
							if (visita.getDtProtAutorizPartenza().after(visita.getDtProtProvvPagam()))
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") deve essere successiva o uguale\nalla data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+")!") );
						} else {
							if (visita.getDtProtNotaAddebito()==null)
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") non può essere valorizzata in assenza\ndella data di protocollo della nota di addebito!") );
							if (visita.getDtProtNotaAddebito().after(visita.getDtProtProvvPagam()))
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") deve essere successiva o uguale\nalla data di protocollo della nota di addebito ("+sdf.format(visita.getDtProtNotaAddebito())+")!") );
						}
					} else {
						if (visita.isAnticipoPrevisto()) {
							if (visita.getDtProtProvvPagamAnt()==null)
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") non può essere valorizzata in assenza\ndella data di protocollo dell'anticipo di pagamento!") );
							if (visita.getDtProtProvvPagamAnt().after(visita.getDtProtProvvPagam()))
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") deve essere successiva o uguale\nalla data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+")!") );
						} else {
							if (visita.getDtProtAutorizPartenza()==null)
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") non può essere valorizzata in assenza\ndella data di protocollo dell'autorizzazione alla partenza!") );
							if (visita.getDtProtAutorizPartenza().after(visita.getDtProtProvvPagam()))
								throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") deve essere successiva o uguale\nalla data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+")!") );
						}
					}
				}
				if (visita.getDtProtProvvPagam().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+") non deve essere successiva alla data odierna!") );
			}

			if (visita.getDtProtAttestatoSogg()!=null) {
				if (visita.getDtProtAttestatoSogg().after(lastDataProt))
					lastDataProt=visita.getDtProtAttestatoSogg();
				if (!visita.isNotaAddebitoSaldoConAnticipoRequired()) {
					if (visita.isNotaAddebitoAnticipoRequired()) {
						if (visita.getDtProtProvvPagamAnt()==null)
							throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella data di protocollo dell'anticipo di pagamento!") );
						if (visita.getDtProtProvvPagamAnt().after(visita.getDtProtAttestatoSogg()))
							throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") deve essere successiva o uguale\nalla data di protocollo dell'anticipo di pagamento ("+sdf.format(visita.getDtProtProvvPagamAnt())+")!") );
					} else {
						if (visita.isAccordoPagataAdEnteStraniero()){
							if (visita.getDtProtProvvPagam()==null)
								throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella data di protocollo del saldo di pagamento!") );
							if (visita.getDtProtProvvPagam().after(visita.getDtProtAttestatoSogg()))
								throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") deve essere successiva o uguale\nalla data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+")!") );
						} else {
							if (visita.getDtProtAutorizPartenza()==null)
								throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella data di protocollo dell'autorizzazione alla partenza!") );
							if (visita.getDtProtAutorizPartenza().after(visita.getDtProtAttestatoSogg()))
								throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") deve essere successiva o uguale\nalla data di protocollo dell'autorizzazione alla partenza ("+sdf.format(visita.getDtProtAutorizPartenza())+")!") );
						}
					}
				} else {
					if (visita.getModalitaPagamento()==null)
						throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella modalità di pagamento del saldo!") );
					if (visita.getDtProtProvvPagam()==null)
						throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella data di protocollo del saldo di pagamento!") );
					if (visita.getDtProtProvvPagam().after(visita.getDtProtAttestatoSogg()))
						throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") deve essere successiva o uguale\nalla data di protocollo del saldo di pagamento ("+sdf.format(visita.getDtProtProvvPagam())+")!") );
				}
				if (visita.getDtFinVisitaEffettiva()==null)
					throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non può essere valorizzata in assenza\ndella data di fine visita effettiva!") );
				if (visita.getDtFinVisitaEffettiva().after(visita.getDtProtAttestatoSogg()))
					throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") deve essere successiva o uguale\nalla data di fine visita effettiva ("+sdf.format(visita.getDtFinVisitaEffettiva())+")!") );
				if (visita.getDtProtAttestatoSogg().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo dell'attestato di soggiorno  ("+sdf.format(visita.getDtProtAttestatoSogg())+") non deve essere successiva alla data odierna!") );
			}

			if (visita.isVisitaAnnullata()) {
				if (visita.getNumProtRinunciaVisita()==null || visita.getDtProtRinunciaVisita()==null)
					throw handleException( new ApplicationException( "Essendo la visita annullata è obbligatorio indicare il numero e data di protocollo di rinuncia visita!") );
				if (lastDataProt.after(visita.getDtProtRinunciaVisita()))
					throw handleException( new ApplicationException( "La data di protocollo di rinuncia visita ("+sdf.format(visita.getDtProtRinunciaVisita())+") deve essere successiva o uguale\nall'ultima data di protocollo inserita ("+sdf.format(lastDataProt)+")!") );
				if (visita.getDtProtRinunciaVisita().after(currentDay))
					throw handleException( new ApplicationException( "La data di protocollo di rinuncia visita ("+sdf.format(visita.getDtProtRinunciaVisita())+") non deve essere successiva alla data odierna!") );
				if (visita.getDtIniVisita()!=null && visita.getDtIniVisita().before(visita.getDtProtRinunciaVisita()))
					throw handleException( new ApplicationException( "La data di protocollo di rinuncia visita ("+sdf.format(visita.getDtProtRinunciaVisita())+") deve essere precedente o uguale\nalla data di inizio visita ("+sdf.format(visita.getDtIniVisita())+")!") );
			}
			
			if (!visita.isVisitaDipendente() && visita.getObbligazioneScadenzario()!=null && visita.getObbligazioneScadenzario().getPg_obbligazione()!=null &&
				visita.isAnticipoPrevisto() && !visita.getFlStampatoProvvPagamAnt() && 
				visita.getImRimbSpeseAnt()!=null && visita.getImRimbSpeseAnt().compareTo(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())==1)
				throw handleException( new ApplicationException( "L'importo dell'anticipo non può essere superiore all'importo massimo previsto di "+new it.cnr.contab.util.EuroFormat().format(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())+"!") );
			Blt_visiteHome visiteHome = (Blt_visiteHome) getHome( usercontext, Blt_visiteBulk.class );
			try {
				visiteHome.validaCambioDate(usercontext, visita);
			} catch (PersistencyException e) {
				throw handleException(e);
			}
	}

	private void aggiornaProgrammiVisite(UserContext usercontext, Blt_visiteBulk visita) throws ComponentException {
		try {
			Blt_progettiHome prgHome = (Blt_progettiHome) getHome( usercontext, Blt_progettiBulk.class );
	
			BulkList<Blt_programma_visiteBulk> programmiVisiteList = getListaProgrammaVisite(usercontext, visita);
			
			BulkList<Blt_visiteBulk> visiteList = new BulkList<Blt_visiteBulk>();

			if (visita.isVisitaItaliano())
				visiteList.addAll(new BulkList(prgHome.findBltVisiteItaList(usercontext, visita)));
			else
				visiteList.addAll(new BulkList(prgHome.findBltVisiteStrList(usercontext, visita)));

			Hashtable<Blt_programma_visiteBulk, BulkList<Blt_visiteBulk>> tableAssociazione = new Hashtable<Blt_programma_visiteBulk, BulkList<Blt_visiteBulk>>();

			for (Iterator iterator = programmiVisiteList.iterator(); iterator.hasNext();) {
				Blt_programma_visiteBulk prgVisita = (Blt_programma_visiteBulk) iterator.next();
				
				BulkList<Blt_visiteBulk> visitePiazzate =  new BulkList<Blt_visiteBulk>();
				BulkList<Blt_visiteBulk> visiteScartate =  new BulkList<Blt_visiteBulk>();

				for (Iterator iterator2 = visiteList.iterator(); iterator2.hasNext();) {
					Blt_visiteBulk currVisita = (Blt_visiteBulk) iterator2.next();
					
					if (!currVisita.isVisitaAnnullata()) {
						Long numGgVisita = DateUtils.daysBetweenDates(currVisita.getDtIniVisita(), currVisita.getDtFinVisita())+1;
						
						if (prgVisita.getNumVisiteAutorizzate() > visitePiazzate.size() && 
							prgVisita.getNumMaxGgVisita().compareTo(numGgVisita.intValue())!=-1)
							visitePiazzate.add(currVisita);
						else
							visiteScartate.add(currVisita);
					}
				}
				
				visiteList = visiteScartate;
				tableAssociazione.put(prgVisita, visitePiazzate);
			}
			
			if (visiteList.size()>0)
				throw handleException( new ApplicationException( "Operazione non possibile! Non esiste disponibilità sufficiente a coprire tutte le visite autorizzate!") );

			for (Iterator iterator = tableAssociazione.keySet().iterator(); iterator.hasNext();) {
				Blt_programma_visiteBulk programmaVisiteKey = (Blt_programma_visiteBulk) iterator.next();
				
				BulkList<Blt_visiteBulk> visiteListValue = tableAssociazione.get(programmaVisiteKey);
				programmaVisiteKey.setNumVisiteUtilizzate(visiteListValue.size());
				programmaVisiteKey.setToBeUpdated();
				updateBulk(usercontext, programmaVisiteKey);

				for (Iterator iterator2 = visiteListValue.iterator(); iterator2.hasNext();) {
					Blt_visiteBulk currVisita = (Blt_visiteBulk) iterator2.next();
					currVisita.setBltProgrammaVisite(programmaVisiteKey);
					currVisita.setToBeUpdated();
					updateBulk(usercontext, currVisita);
				}
			}
		} catch (IntrospectionException e) {
			handleException(e);
		} catch (PersistencyException e) {
			handleException(e);
		}
	}
	
	private BulkList<Blt_programma_visiteBulk> getListaProgrammaVisite(UserContext usercontext, Blt_visiteBulk visita) throws ComponentException, IntrospectionException, PersistencyException {
		Blt_progettiHome prgHome = (Blt_progettiHome) getHome( usercontext, Blt_progettiBulk.class );
		Blt_programma_visiteHome prgVisiteHome = (Blt_programma_visiteHome) getHome( usercontext, Blt_programma_visiteBulk.class );

		SQLBuilder sqlPrgVisite = null; 
		if (visita.isVisitaItaliano())
			sqlPrgVisite = prgHome.getSQLBuilderBltProgrammaVisiteItaList(prgVisiteHome, visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti());
		else
			sqlPrgVisite = prgHome.getSQLBuilderBltProgrammaVisiteStrList(prgVisiteHome, visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti());
		
		sqlPrgVisite.addClause(FindClause.AND,"annoVisita",SQLBuilder.EQUALS, visita.getAnnoVisita());
		sqlPrgVisite.addOrderBy("NUM_MAX_GG_VISITA");
		BulkList l =  new BulkList<Blt_programma_visiteBulk>(prgVisiteHome.fetchAll(sqlPrgVisite));
		getHomeCache(usercontext).fetchAll(usercontext);
		return l;
	}

	@Override
	protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		oggettobulk = super.eseguiCreaConBulk(usercontext, oggettobulk);

		if (oggettobulk instanceof Blt_visiteBulk) {
			try
			{
				Blt_visiteBulk visita = (Blt_visiteBulk)oggettobulk;
				validaVisita(usercontext, visita, null);

				if (visita.isVisitaItaliano() || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())){
					visita.setPrc_oneri_contributivi(BigDecimal.ZERO);
					visita.setPrc_oneri_fiscali(BigDecimal.ZERO);
					visita.setFlPagamentoConBonifico(Boolean.TRUE);
					if (visita.isVisitaItaliano()) 
						visita.setFlPagamentoFineVisita(Boolean.TRUE);
				} else {
					visita.setPrc_oneri_contributivi(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getPrc_oneri_contributivi());
					if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getFl_conv_fiscale())
						visita.setPrc_oneri_fiscali(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getPrc_oneri_fiscali());
					else
						visita.setPrc_oneri_fiscali(BigDecimal.ZERO);
				}
				
				aggiornaImportoRimborsi(usercontext, visita, null);
				validaVisita(usercontext, visita, null);
				
				visita.setToBeUpdated();
				updateBulk(usercontext, visita);

				aggiornaProgrammiVisite(usercontext, visita);
			} catch (PersistencyException e) {
				handleException(e);
			}
		}
		return oggettobulk;
	}

	@Override
	protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		Blt_visiteBulk visitaOld = null;
		if (oggettobulk instanceof Blt_visiteBulk) {
			Blt_visiteBulk visita = (Blt_visiteBulk)oggettobulk;
			aggiornaObbligazione(usercontext, visita, null);

			visitaOld = (Blt_visiteBulk)getTempHome(usercontext, visita.getClass()).findByPrimaryKey(visita);
			getTempHomeCache(usercontext).fetchAll(usercontext);
			aggiornaImportoRimborsi(usercontext, visita, visitaOld);
		}

		oggettobulk = super.eseguiModificaConBulk(usercontext, oggettobulk);

		if (oggettobulk instanceof Blt_visiteBulk) {
			Blt_visiteBulk visita = (Blt_visiteBulk)oggettobulk;
			validaVisita(usercontext, visita, visitaOld);
//			aggiornaProgrammiVisite(usercontext, visita);
		}

		return oggettobulk;
	}
	
	private void aggiornaObbligazione(UserContext userContext, Blt_visiteBulk visita, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws ComponentException {
		if (visita.getObbligazioneScadenzario() != null) {
			aggiornaSaldi(userContext, visita, visita.getObbligazioneScadenzario().getObbligazione(),status);
			if (visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo())
				aggiornaObbligazioneTemporanea(userContext, visita.getObbligazioneScadenzario().getObbligazione());
		}
	}
	
	private void aggiornaObbligazioneTemporanea(UserContext userContext, ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {
		try 
		{
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
			Long pg = null;
			pg = numHome.getNextPg(	userContext,
									obbligazioneTemporanea.getEsercizio(), 
									obbligazioneTemporanea.getCd_cds(), 
									obbligazioneTemporanea.getCd_tipo_documento_cont(), 
									obbligazioneTemporanea.getUser());
			ObbligazioneHome home = (ObbligazioneHome)getHome(userContext, obbligazioneTemporanea);
			home.confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg);
		} 
		catch (it.cnr.jada.persistency.PersistencyException e) {throw handleException(obbligazioneTemporanea, e);} 
		catch (it.cnr.jada.persistency.IntrospectionException e) {throw handleException(obbligazioneTemporanea, e);}	
	}

	private void aggiornaSaldi(it.cnr.jada.UserContext userContext, Blt_visiteBulk visita, IDocumentoContabileBulk docCont, OptionRequestParameter status) throws ComponentException
	{
		try 
		{
			if (docCont != null && visita != null && visita.getDefferredSaldi() != null) 
			{
				IDocumentoContabileBulk key = visita.getDefferredSaldoFor(docCont);
				if(key != null) 
				{
					java.util.Map values = (java.util.Map)visita.getDefferredSaldi().get(key);

					if(values != null)
					{				
						//QUI chiamare component del documento contabile interessato
						String jndiName = null;
						Class clazz = null;
						it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession session = null;
						if (docCont instanceof ObbligazioneBulk)
						{
							jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
							clazz = it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class;
							session = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(jndiName,clazz);
						} 
						if(session != null)
						{
							session.aggiornaSaldiInDifferita(userContext, key, values, status);
							//NON Differibile: si rischia di riprocessare i saldi impropriamente
							visita.getDefferredSaldi().remove(key);
						}	
					}		
				}
			}
		} 
		catch (javax.ejb.EJBException e) 
		{
			throw handleException(visita, e);
		} 
		catch (java.rmi.RemoteException e) 
		{
			throw handleException(visita, e);
		}
	}

	public SQLBuilder selectBancaAnticipoByClause(UserContext userContext, Blt_visiteBulk visita, BancaBulk bancaAnticipo, CompoundFindClause clauses) throws ComponentException {
		BancaHome bancaHome = (BancaHome)getHome(userContext, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
		return bancaHome.selectBancaFor(visita.getModalitaPagamentoAnticipo(), visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo().getCd_terzo());
	}

	public java.util.Collection findListaBancheAnticipo(UserContext userContext, Blt_visiteBulk visita) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

		try {
			if (visita.getTerzoPagamento() ==null ) 
				return null;

			return getHome(userContext, BancaBulk.class).fetchAll(selectBancaAnticipoByClause(userContext, visita, null, null));
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}

	public SQLBuilder selectBancaByClause(UserContext userContext, Blt_visiteBulk visita, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {
		BancaHome bancaHome = (BancaHome)getHome(userContext, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
		return bancaHome.selectBancaFor(visita.getModalitaPagamento(), visita.getTerzoPagamento().getCd_terzo());
	}

	public java.util.Collection findListaBanche(UserContext userContext, Blt_visiteBulk visita) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

		try {
			if (visita.getTerzoPagamento() ==null ) 
				return null;

			return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, visita, null, null));
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	
	public TerzoBulk findCessionarioAnticipo(UserContext userContext, Blt_visiteBulk visita) throws ComponentException {
		try	{
			if (visita == null || visita.getModalitaPagamentoAnticipo() == null)
				return null;
			Modalita_pagamentoHome mph = (Modalita_pagamentoHome)getHome(userContext, Modalita_pagamentoBulk.class);
	        TerzoBulk terzo = visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo();
		    if (terzo==null) return null;
			Modalita_pagamentoBulk mp = (Modalita_pagamentoBulk)mph.findByPrimaryKey(new Modalita_pagamentoBulk(visita.getModalitaPagamentoAnticipo().getCd_modalita_pag(), terzo.getCd_terzo()));
			if (mp == null || mp.getCd_terzo_delegato() == null) return null;
			TerzoHome th = (TerzoHome)getHome(userContext, TerzoBulk.class);
			return (TerzoBulk)th.findByPrimaryKey(new TerzoBulk(mp.getCd_terzo_delegato()));
		} catch( Exception e ) {
			throw handleException(e);
		}		
	}

	public TerzoBulk findCessionario(UserContext userContext, Blt_visiteBulk visita) throws ComponentException {
		try	{
			if (visita == null || visita.getModalitaPagamento() == null)
				return null;
			Modalita_pagamentoHome mph = (Modalita_pagamentoHome)getHome(userContext, Modalita_pagamentoBulk.class);
	        TerzoBulk terzo = visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo();
		    if (terzo==null) return null;
			Modalita_pagamentoBulk mp = (Modalita_pagamentoBulk)mph.findByPrimaryKey(new Modalita_pagamentoBulk(visita.getModalitaPagamento().getCd_modalita_pag(), terzo.getCd_terzo()));
			if (mp == null || mp.getCd_terzo_delegato() == null) return null;
			TerzoHome th = (TerzoHome)getHome(userContext, TerzoBulk.class);
			return (TerzoBulk)th.findByPrimaryKey(new TerzoBulk(mp.getCd_terzo_delegato()));
		} catch( Exception e ) {
			throw handleException(e);
		}		
	}
	private Rif_modalita_pagamentoBulk findModalitaSelezionate(UserContext aUC, String cdModalitaPag) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	    try {
		    String modalita;

		    it.cnr.jada.bulk.BulkHome home = getHome(aUC, Rif_modalita_pagamentoBulk.class);
			it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
			
	    	sql.addClause(FindClause.AND, "cd_modalita_pag", SQLBuilder.EQUALS, cdModalitaPag);
			
	    	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
			if (!broker.next())
		         return null;
		 
			Rif_modalita_pagamentoBulk modalitaBulk=(Rif_modalita_pagamentoBulk) broker.fetch(Rif_modalita_pagamentoBulk.class);
			broker.close();
			return modalitaBulk;
		 
		} catch (it.cnr.jada.persistency.PersistencyException e) {
		     throw handleException(e);
		} 
	}

	public java.util.Collection findModalitaPagamentoAnticipoOptions(UserContext aUC, Blt_visiteBulk visita) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		return findModalitaPagamentoOptions(aUC, visita, true);
	}

	public java.util.Collection findModalitaPagamentoSaldoOptions(UserContext aUC, Blt_visiteBulk visita) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		return findModalitaPagamentoOptions(aUC, visita, false);
	}

	public java.util.Collection findModalitaPagamentoOptions(UserContext aUC, Blt_visiteBulk visita, boolean isAnticipo) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		try {
			getHomeCache(aUC).fetchAll(aUC);
		    TerzoHome home = (TerzoHome) getHome(aUC, TerzoBulk.class);
		    if (visita.getBltAutorizzatiDett()==null || visita.getBltAutorizzatiDett().getBltAutorizzati()==null) return null;
		    TerzoBulk terzo = visita.getTerzoPagamento();
			if (terzo == null || terzo.getCd_terzo() == null) return null;
			Collection modalita = home.findRif_modalita_pagamento(terzo, null);
			if (modalita == null || modalita.isEmpty())
		    	return null;        
			List modalitaFiltrata = new BulkList();
			for (Iterator iterator = modalita.iterator(); iterator.hasNext();) {
				Rif_modalita_pagamentoBulk oldModalita = (Rif_modalita_pagamentoBulk) iterator.next();
				if ((oldModalita.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCARIO) && !oldModalita.getFl_per_cessione() && visita.isVisitaItaliano()) || 
					(oldModalita.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.IBAN) && visita.isVisitaStraniero() && (isAnticipo || visita.getFlPagamentoConBonifico())) || 
					 oldModalita.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.ALTRO) && (isAnticipo || !visita.getFlPagamentoConBonifico()))
					 modalitaFiltrata.add(oldModalita);
			}
			return modalitaFiltrata;
	    } catch (it.cnr.jada.persistency.PersistencyException e) {
	        throw handleException(e);
	    } catch (it.cnr.jada.persistency.IntrospectionException e) {
	        throw handleException(e);
	    }
	}
	private BancaBulk findBancaSelezionata(UserContext aUC, Integer cdTerzo, Long pgBanca) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		try {
		    it.cnr.jada.bulk.BulkHome home= getHome(aUC, BancaBulk.class);
		    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();

	        if (pgBanca == null)
	            return null;
	        sql.addClause(FindClause.AND, "pg_banca", SQLBuilder.EQUALS, pgBanca);
	        sql.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, cdTerzo);

	        it.cnr.jada.persistency.Broker broker= home.createBroker(sql);
		    if (!broker.next())
		        return null;

		    BancaBulk bancaBulk= (BancaBulk) broker.fetch(BancaBulk.class);
		    broker.close();
		    return bancaBulk;

	    } catch (it.cnr.jada.persistency.PersistencyException e) {
	        throw handleException(e);
	    }
	}
	public BigDecimal findRimborsoNettoPrevisto(UserContext aUC, Blt_visiteBulk visita) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		if (visita.isVisitaItaliano()) {
			if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getSpese_viaggio_ita()==null)
				return new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN);
			else
  				return visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getSpese_viaggio_ita();
		} else {
			//CALCOLO PER STRANIERI
			Blt_accordiBulk accordo = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo();
		    Blt_accordiHome accordoHome = (Blt_accordiHome)getHome(aUC, Blt_accordiBulk.class);
			Integer numGgMese = 30;
		    
			Integer numGgVisita = visita.getNumGiorniVisita().intValue();
			Integer numGgVisitaEffettiva = numGgVisita;
			if (visita.getDtIniVisitaEffettiva()!=null && visita.getDtFinVisitaEffettiva()!=null)
				numGgVisitaEffettiva = Long.valueOf(DateUtils.daysBetweenDates(visita.getDtIniVisitaEffettiva(), visita.getDtFinVisitaEffettiva())+1).intValue();
	
			if (numGgVisita.compareTo(numGgVisitaEffettiva)==1)
				numGgVisita = numGgVisitaEffettiva;
			
			BigDecimal rimborso = BigDecimal.ZERO, diaria = BigDecimal.ZERO, mensile = BigDecimal.ZERO;
			if (visita.isVisitaDipendente()) {
				diaria = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_diaria_ita();
				mensile = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_mensile_ita();
			} else {
				diaria = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_diaria_str();
				mensile = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_mensile_str();
			}
			
		    List regole = accordoHome.findBlt_regole_diariaList(aUC, accordo);
		    for (Iterator<Blt_regole_diariaBulk> iterator = regole.iterator(); iterator.hasNext();) {
		    	Blt_regole_diariaBulk regolaDiaria = (Blt_regole_diariaBulk) iterator.next();
		    	if (numGgVisita>=regolaDiaria.getGiornoMin() && numGgVisita<=regolaDiaria.getGiornoMax()){
		    		if (regolaDiaria.getGiorniDef()!=null)
		    			numGgVisita = regolaDiaria.getGiorniDef();
	
		    		Integer numMesi = numGgVisita/numGgMese;
		    		Integer numGiorni = numGgVisita-(numGgMese*numMesi);
	
		    		if (regolaDiaria.getFlMensile()){
		    			rimborso = rimborso.add(mensile.multiply(new BigDecimal(numMesi)));
			    		if (regolaDiaria.getFlDiaria())
			    			rimborso = rimborso.add(diaria.multiply(new BigDecimal(numGiorni)));
			    		else
			    			rimborso = rimborso.add(mensile.multiply(new BigDecimal(numGiorni)).divide(new BigDecimal(numGgMese), 2, BigDecimal.ROUND_HALF_EVEN));
		    		} else if (regolaDiaria.getFlDiaria()) {
		    			rimborso = rimborso.add(diaria.multiply(new BigDecimal(numGgVisita)));
		    		}
		    		break;
		    	}
			}
		    return rimborso.setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN);
		}
	}

	private void aggiornaImportoRimborsi(UserContext aUC, Blt_visiteBulk visita, Blt_visiteBulk visitaOld) throws ComponentException, PersistencyException {
		try
		{
			if (visita.getFlStampatoProvvPagamento()) return;

			if (visita.isVisitaItaliano()) {
				if (visita.getImRimbPrevisto()==null) {
					visita.setImRimbPrevisto(findRimborsoNettoPrevisto(aUC, visita));
					visita.setImRimbSpeseAnt(BigDecimal.ZERO);
					visita.setImRimbSpese(BigDecimal.ZERO);
					visita.setToBeUpdated();
				}
			} else {
				Date dataOldIni = null, dataOldFin = null;
				if (visitaOld != null) {
					dataOldIni = visitaOld.getDtIniVisitaEffettiva()==null?visitaOld.getDtIniVisita():visitaOld.getDtIniVisitaEffettiva();
					dataOldFin = visitaOld.getDtFinVisitaEffettiva()==null?visitaOld.getDtFinVisita():visitaOld.getDtFinVisitaEffettiva();
				}
				Date dataNewIni = visita.getDtIniVisitaEffettiva()==null?visita.getDtIniVisita():visita.getDtIniVisitaEffettiva();
				Date dataNewFin = visita.getDtFinVisitaEffettiva()==null?visita.getDtFinVisita():visita.getDtFinVisitaEffettiva();
				
				if (visitaOld==null || DateUtils.daysBetweenDates(dataOldIni, dataOldFin)!=DateUtils.daysBetweenDates(dataNewIni, dataNewFin) ||
					visita.isAnticipoPrevisto()!=visitaOld.isAnticipoPrevisto()) {
					visita.setImRimbPrevisto(findRimborsoNettoPrevisto(aUC, visita));
					visita.setImRimbSpese(BigDecimal.ZERO);

					if (!visita.getFlStampatoProvvPagamAnt()) {
						visita.setImRimbSpeseAnt(BigDecimal.ZERO);
	
						if (visita.isAnticipoPrevisto()) {
							if (visita.getImRimbPrevisto().compareTo(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())==1) {
								if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getPrc_anticipo()==null ||
									visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getPrc_anticipo().compareTo(BigDecimal.ZERO)!=1) {
									visita.setImRimbSpeseAnt(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo());
								} else {
									BigDecimal imp = visita.getImRimbPrevisto().multiply(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getPrc_anticipo().divide(BigDecimal.TEN.multiply(BigDecimal.TEN)));
									imp = imp.setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN);
									if (imp.compareTo(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())==1)
										visita.setImRimbSpeseAnt(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo());
									else
										visita.setImRimbSpeseAnt(imp);
								}
							} else {
								visita.setImRimbSpeseAnt(visita.getImRimbPrevisto());
							}
						}
					}
					visita.setImRimbSpese(visita.getImRimbPrevisto().subtract(visita.getImRimbSpeseAnt()));
					visita.setToBeUpdated();
				}
			}
		} catch (IntrospectionException e) {
			handleException(e);
		}
	}
}
