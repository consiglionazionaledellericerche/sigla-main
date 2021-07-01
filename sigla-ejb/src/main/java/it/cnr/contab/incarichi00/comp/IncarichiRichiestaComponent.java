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

package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_collaborazioneBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_collaborazioneHome;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elencoBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elencoHome;
import it.cnr.contab.incarichi00.bulk.V_incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_richiestaHome;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

public class IncarichiRichiestaComponent extends CRUDComponent {
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_richiestaBulk) {
				((Incarichi_richiestaBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(usercontext));
				((Incarichi_richiestaBulk)oggettobulk).setCds( (CdsBulk)getHome(usercontext, CdsBulk.class).findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(usercontext))) );
				((Incarichi_richiestaBulk)oggettobulk).setUnita_organizzativa( (Unita_organizzativaBulk)getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext))));
				((Incarichi_richiestaBulk)oggettobulk).setStato(Incarichi_richiestaBulk.STATO_PROVVISORIO);
				
				BulkList indirizziMailList = new it.cnr.jada.bulk.BulkList(((UtenteHome)getHome(usercontext, UtenteBulk.class )).findUtente_indirizzi_email(new UtenteBulk(CNRUserContext.getUser(usercontext))));

				if (!indirizziMailList.isEmpty())
					((Incarichi_richiestaBulk)oggettobulk).setEmail_risposte(((Utente_indirizzi_mailBulk)indirizziMailList.get(0)).getIndirizzo_mail());
				
				String indirizzo = Utility.createUnita_organizzativaComponentSession().getIndirizzoUnitaOrganizzativa(usercontext, new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)), true);
				if (indirizzo != null) {
					((Incarichi_richiestaBulk)oggettobulk).setIndirizzo_unita_organizzativa(indirizzo);
					((Incarichi_richiestaBulk)oggettobulk).setSede_lavoro(indirizzo);
				}
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_richiestaBulk) {
				((Incarichi_richiestaBulk)oggettobulk).setIncarichi_proceduraColl(new BulkList(((Incarichi_proceduraHome)getHome(usercontext, Incarichi_proceduraBulk.class)).findIncarichiProcedura((Incarichi_richiestaBulk)oggettobulk)));
				String indirizzo = Utility.createUnita_organizzativaComponentSession().getIndirizzoUnitaOrganizzativa(usercontext, ((Incarichi_richiestaBulk)oggettobulk).getUnita_organizzativa(), true);
				if (indirizzo != null)
					((Incarichi_richiestaBulk)oggettobulk).setIndirizzo_unita_organizzativa(indirizzo);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;

		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		//sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		if (!isUoEnte)
			sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		if (isUoSac)
			sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		sql.addOrderBy("pg_richiesta");
		return sql;
	}
	
	protected java.util.GregorianCalendar getGregorianCalendar() {

		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		
		return gc;
	}

	public OggettoBulk pubblicaSulSito(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			Parametri_cnrBulk parCNR = Utility.createParametriCnrComponentSession().getParametriCnr(usercontext, ((Incarichi_richiestaBulk)oggettobulk).getEsercizio());
			
			if (oggettobulk instanceof Incarichi_richiestaBulk) {
				java.util.GregorianCalendar gc_data_pubblicazione = getGregorianCalendar();
				gc_data_pubblicazione.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
				((Incarichi_richiestaBulk)oggettobulk).setData_pubblicazione(new Timestamp(gc_data_pubblicazione.getTime().getTime()));
	
				java.util.GregorianCalendar gc_data_fine_pubblicazione = getGregorianCalendar();
				gc_data_fine_pubblicazione.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
				gc_data_fine_pubblicazione.add(java.util.Calendar.DAY_OF_YEAR,parCNR.getRicerca_prof_int_giorni_pubbl());
				((Incarichi_richiestaBulk)oggettobulk).setData_fine_pubblicazione(new Timestamp(gc_data_fine_pubblicazione.getTime().getTime()));
	
				java.util.GregorianCalendar gc_data_scadenza = getGregorianCalendar();
				gc_data_scadenza.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
				gc_data_scadenza.add(java.util.Calendar.DAY_OF_YEAR,parCNR.getRicerca_prof_int_giorni_pubbl()+parCNR.getRicerca_prof_int_giorni_scad());
				((Incarichi_richiestaBulk)oggettobulk).setData_scadenza(new Timestamp(gc_data_scadenza.getTime().getTime()));
				
				((Incarichi_richiestaBulk)oggettobulk).setStato(Incarichi_richiestaBulk.STATO_DEFINITIVO);
			}
			updateBulk(usercontext, oggettobulk);
			return oggettobulk;
		}
		catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectUnita_organizzativaByClause(UserContext userContext, Incarichi_richiestaBulk incarico, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
		sqlStruttura.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sqlStruttura.addClause( "AND", "cd_cds", SQLBuilder.EQUALS, incarico.getCd_cds());
		sqlStruttura.addClause( "AND", "cd_tipo_livello", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_UO);
		sqlStruttura.addSQLJoin( "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

		SQLBuilder sql = getHome(userContext, uo.getClass()).createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStruttura);
		sql.addClause( clauses );
		return sql;
	}

	public RemoteIterator findListaIncarichiRichiesta(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws ComponentException {
		V_incarichi_richiestaHome home = (V_incarichi_richiestaHome)getHome(userContext, V_incarichi_richiestaBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		
		if(dominio.equalsIgnoreCase("data")) {
			if (Constants.RICHIESTE_IN_CORSO.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"trunc(sysdate) >= trunc(DATA_PUBBLICAZIONE)");
				sql.addSQLClause(FindClause.AND,"trunc(sysdate) <= trunc(DATA_FINE_PUBBLICAZIONE)");
			} else if (Constants.RICHIESTE_SCADUTE.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"(trunc(sysdate) < trunc(DATA_PUBBLICAZIONE) OR trunc(sysdate) > trunc(DATA_FINE_PUBBLICAZIONE))");
			}
		}
		if(anno!=null)
			sql.addSQLClause(FindClause.AND,"to_char(DATA_PUBBLICAZIONE, 'YYYY')",SQLBuilder.EQUALS,anno);

		if(cdCds!=null)
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,cdCds);

		if(strRicerca!=null) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"instr(ESERCIZIO||'/'||PG_RICHIESTA,'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(DATA_PUBBLICAZIONE,'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(DATA_FINE_PUBBLICAZIONE,'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(CD_CDS),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(DS_CDS),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(EMAIL_RISPOSTE),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(ATTIVITA),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(DURATA),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(SEDE_LAVORO),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(NR_RISORSE_DA_TROVARE),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(NOTE),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(COMPETENZE),'"+strRicerca.toUpperCase()+"')>0");
			sql.closeParenthesis();
		}

		if(order!=null) {
			if (order.equals("numric"))
				sql.addOrderBy("ESERCIZIO,PG_RICHIESTA");
			else if (order.equals("esercizio"))
				sql.addOrderBy("ESERCIZIO");
			else if (order.equals("progressivo"))
				sql.addOrderBy("PG_RICHIESTA");
			else if (order.equals("cds"))
				sql.addOrderBy("CD_CDS");
			else if (order.equals("istituto"))
				sql.addOrderBy("DS_CDS");
			else if (order.equals("sede"))
				sql.addOrderBy("SEDE_LAVORO");
			else if (order.equals("email"))
				sql.addOrderBy("EMAIL_RISPOSTE");
			else if (order.equals("oggetto"))
				sql.addOrderBy("ATTIVITA");
			else if (order.equals("durata"))
				sql.addOrderBy("DURATA");
			else if (order.equals("sedelavoro"))
				sql.addOrderBy("SEDE_LAVORO");
			else if (order.equals("numrisorse"))
				sql.addOrderBy("NR_RISORSE_DA_TROVARE");
			else if (order.equals("note"))
				sql.addOrderBy("NOTE");
			else if (order.equals("competenze"))
				sql.addOrderBy("COMPETENZE");
			else if (order.equals("iniziopubblicazione"))
				sql.addOrderBy("DATA_PUBBLICAZIONE");
			else if (order.equals("finepubblicazione"))
				sql.addOrderBy("DATA_FINE_PUBBLICAZIONE");
		}

		return iterator(userContext, sql, V_incarichi_richiestaBulk.class, getFetchPolicyName("find"));
	}
	
	public List completaListaIncarichiRichiesta(UserContext userContext, List list) throws ComponentException {
		try {	
			Hashtable sediUo = new Hashtable<String, String>();

			// inseriamo l'indirizzo della Sede del CDS
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
    			V_incarichi_richiestaBulk incarico = (V_incarichi_richiestaBulk)iterator.next();

    			String indirizzo = (String) sediUo.get(incarico.getCd_unita_organizzativa());
    			if (indirizzo==null) {
    				indirizzo="";
	    			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(incarico.getCd_unita_organizzativa()));
	    			TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(userContext, uo);
	
	    			if (terzo != null) {
	    				if (terzo.getVia_sede() != null)
	    					indirizzo = indirizzo + terzo.getVia_sede();
	    				if (terzo.getNumero_civico_sede() != null)
	    					indirizzo = indirizzo +  ", " + terzo.getNumero_civico_sede();
	
	    				ComuneBulk comune = (ComuneBulk)getHome(userContext, ComuneBulk.class).findByPrimaryKey(terzo.getComune_sede());
	    				indirizzo = indirizzo + ", "+comune.getDs_comune();
	    			}
        			sediUo.put(incarico.getCd_unita_organizzativa(), indirizzo);
    			}
    			incarico.setSede(indirizzo);
    		}
			return list;

		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (RemoteException ex) {
			throw handleException(ex);
		} catch (EJBException ex) {
			throw handleException(ex);
		}
	}

	public RemoteIterator findListaIncarichiCollaborazione(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws ComponentException {
		V_incarichi_collaborazioneHome home = (V_incarichi_collaborazioneHome)getHome(userContext, V_incarichi_collaborazioneBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		if(dominio.equalsIgnoreCase("data"))
			if (Constants.RICHIESTE_IN_CORSO.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"trunc(sysdate) >= trunc(DT_PUBBLICAZIONE)");
				sql.addSQLClause(FindClause.AND,"trunc(sysdate) <= trunc(DT_FINE_PUBBLICAZIONE)");
			} else if (Constants.RICHIESTE_SCADUTE.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"(trunc(sysdate) < trunc(DT_PUBBLICAZIONE) OR trunc(sysdate) > trunc(DT_FINE_PUBBLICAZIONE))");
			}

		if(anno!=null)
			sql.addSQLClause("AND","to_char(DT_PUBBLICAZIONE, 'YYYY')",SQLBuilder.EQUALS,anno);

		if(cdCds!=null)
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,cdCds);

		if(strRicerca!=null) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"instr(ESERCIZIO||'/'||PG_PROCEDURA,'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(CD_CDS),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(DS_CDS),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(OGGETTO),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(NR_CONTRATTI,'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(DT_PUBBLICAZIONE,'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(DT_FINE_PUBBLICAZIONE,'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.closeParenthesis();
		}

		if(order!=null) {
			if (order.equals("numric"))
				sql.addOrderBy("ESERCIZIO,PG_PROCEDURA");
			else if (order.equals("esercizio"))
				sql.addOrderBy("ESERCIZIO");
			else if (order.equals("progressivo"))
				sql.addOrderBy("PG_PROCEDURA");
			else if (order.equals("cds"))
				sql.addOrderBy("CD_CDS");
			else if (order.equals("istituto"))
				sql.addOrderBy("DS_CDS");
			else if (order.equals("oggetto"))
				sql.addOrderBy("OGGETTO");
			else if (order.equals("numcontratti"))
				sql.addOrderBy("NR_CONTRATTI");
			else if (order.equals("datascadenza"))
				sql.addOrderBy("DT_PUBBLICAZIONE");
			else if (order.equals("datapubblicazione"))
				sql.addOrderBy("DT_FINE_PUBBLICAZIONE");
		}

		return iterator(userContext, sql, V_incarichi_collaborazioneBulk.class, getFetchPolicyName("find"));
	}

	public List completaListaIncarichiCollaborazione(UserContext userContext, List list)throws ComponentException{
		try {	
			Hashtable sediUo = new Hashtable<String, String>();

			// inseriamo l'indirizzo della Sede del CDS
			// e il link al PDF del bando di concorso
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
    			V_incarichi_collaborazioneBulk incarico = (V_incarichi_collaborazioneBulk)iterator.next();

    			String indirizzo = (String) sediUo.get(incarico.getCd_unita_organizzativa());
    			if (indirizzo==null) {
    				indirizzo="";
        			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(incarico.getCd_unita_organizzativa()));
        			TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(userContext, uo);

        			if (terzo != null) {
        				if (terzo.getVia_sede() != null)
        					indirizzo = indirizzo + terzo.getVia_sede();
        				if (terzo.getNumero_civico_sede() != null)
        					indirizzo = indirizzo +  ", " + terzo.getNumero_civico_sede();

        				ComuneBulk comune = (ComuneBulk)getHome(userContext, ComuneBulk.class).findByPrimaryKey(terzo.getComune_sede());
        				indirizzo = indirizzo + ", "+comune.getDs_comune();
        			}
        			sediUo.put(incarico.getCd_unita_organizzativa(), indirizzo);
    			}
    			incarico.setSede(indirizzo);
    			
				Incarichi_proceduraBulk incaricoProcedura = (Incarichi_proceduraBulk)getHome(userContext, Incarichi_proceduraBulk.class).findByPrimaryKey(new Incarichi_proceduraBulk(incarico.getEsercizio(), incarico.getPg_procedura()));
				incaricoProcedura.setArchivioAllegati( new BulkList( ((Incarichi_proceduraHome) getHome( userContext, Incarichi_proceduraBulk.class )).findArchivioAllegati( incaricoProcedura ) ));
				incarico.setIncaricoProcedura(incaricoProcedura);
			}

			return list;
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (RemoteException ex) {
			throw handleException(ex);
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}

	public RemoteIterator findListaIncarichiElenco(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca,String tipoInc) throws ComponentException {
		V_incarichi_elencoHome home = (V_incarichi_elencoHome)getHome(userContext, V_incarichi_elencoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("TIPO_ATTIVITA");
		sql.addSQLJoin("V_INCARICHI_ELENCO.CD_TIPO_ATTIVITA", "TIPO_ATTIVITA.CD_TIPO_ATTIVITA");
		if (tipoInc==null)
			sql.addSQLClause(FindClause.AND,"TIPO_ATTIVITA.TIPO_ASSOCIAZIONE",SQLBuilder.EQUALS,Tipo_attivitaBulk.ASS_INCARICHI);
		else if (tipoInc.equals("1"))
			sql.addSQLClause(FindClause.AND,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_CONSULENZA);
		else if (tipoInc.equals("2")) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_STUDIO);
			sql.addSQLClause(FindClause.OR,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_RICERCA);
			sql.addSQLClause(FindClause.OR,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_ALTRO);
			sql.closeParenthesis();
		} else if (tipoInc.equals("3"))
			sql.addSQLClause(FindClause.AND,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_ASSEGNO_RICERCA);
		else if (tipoInc.equals("4"))
			sql.addSQLClause(FindClause.AND,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_BORSA_STUDIO);
		else if (tipoInc.equals("5"))
			sql.addSQLClause(FindClause.AND,"TIPO_ATTIVITA.TIPOLOGIA",SQLBuilder.EQUALS,Tipo_attivitaBulk.TIPO_TIROCINIO);

		sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause(FindClause.AND,"dt_proroga", SQLBuilder.ISNULL, null);
		sql.addSQLClause(FindClause.AND,"dt_fine_validita", SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause(FindClause.AND,"to_char(DT_FINE_VALIDITA,'yyyymmdd') >= to_char(ADD_MONTHS(sysdate,-3*12),'yyyymmdd')");
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause(FindClause.AND,"dt_proroga", SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause(FindClause.AND,"to_char(DT_PROROGA,'yyyymmdd') >= to_char(ADD_MONTHS(sysdate,-3*12),'yyyymmdd')");
		sql.closeParenthesis();
		sql.closeParenthesis();

		sql = addFiltriListaIncarichiElenco(sql, query, dominio, anno, cdCds, order, strRicerca);
		return iterator(userContext, sql, V_incarichi_elencoBulk.class, getFetchPolicyName("find"));
	}
	
	public RemoteIterator findListaIncarichiElencoArt18(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws ComponentException {
		V_incarichi_elencoHome home = (V_incarichi_elencoHome)getHome(userContext, V_incarichi_elencoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"to_number(to_char(DT_STIPULA,'yyyy')) >= to_number('2013')");

		sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause(FindClause.AND,"dt_proroga", SQLBuilder.ISNULL, null);
		sql.addSQLClause(FindClause.AND,"dt_fine_validita", SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause(FindClause.AND,"to_char(DT_FINE_VALIDITA,'yyyymmdd') >= to_char(ADD_MONTHS(sysdate,-3*12),'yyyymmdd')");
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause(FindClause.AND,"dt_proroga", SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause(FindClause.AND,"to_char(DT_PROROGA,'yyyymmdd') >= to_char(ADD_MONTHS(sysdate,-3*12),'yyyymmdd')");
		sql.closeParenthesis();
		sql.closeParenthesis();

		sql = addFiltriListaIncarichiElenco(sql, query, dominio, anno, cdCds, order, strRicerca);
		return iterator(userContext, sql, V_incarichi_elencoBulk.class, getFetchPolicyName("find"));
	}

	public SQLBuilder addFiltriListaIncarichiElenco(SQLBuilder sql,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws ComponentException {
		if(dominio.equalsIgnoreCase("data"))
			if (Constants.RICHIESTE_IN_CORSO.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"to_number(to_char(sysdate,'yyyy')) = to_number(to_char(DT_STIPULA,'yyyy'))");
			} else if (Constants.RICHIESTE_SCADUTE.equalsIgnoreCase(query)) {
				sql.addSQLClause(FindClause.AND,"to_number(to_char(sysdate,'yyyy')) > to_number(to_char(DT_STIPULA,'yyyy'))");
			}
		if(anno!=null)
			sql.addSQLClause(FindClause.AND,"to_char(DT_STIPULA, 'YYYY')",SQLBuilder.EQUALS,anno);

		if(cdCds!=null)
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,cdCds);

		if(strRicerca!=null) {
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"instr(ESERCIZIO||'/'||PG_REPERTORIO,'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(DS_UNITA_ORGANIZZATIVA),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(BENEF_DENOMINAZIONE_SEDE),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(UPPER(OGGETTO),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(IMPORTO_LORDO,0)+nvl(IMPORTO_VARIAZIONE,0), '999999999999999D99'),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(IMPORTO_LORDO,0)+nvl(IMPORTO_VARIAZIONE,0), '999G999G999G999G999D99'),'"+strRicerca.toUpperCase()+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(DT_INIZIO_VALIDITA,'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(DT_FINE_VALIDITA_VARIAZIONE, DT_FINE_VALIDITA),'dd/mm/yyyy'),'"+strRicerca+"')>0");
			sql.closeParenthesis();
		}

		if(order!=null) {
			if (order.equals("numric"))
				sql.addOrderBy("ESERCIZIO,PG_REPERTORIO");
			else if (order.equals("esercizio"))
				sql.addOrderBy("ESERCIZIO");
			else if (order.equals("progressivo"))
				sql.addOrderBy("PG_REPERTORIO");
			else if (order.equals("cdr"))
				sql.addOrderBy("DS_UNITA_ORGANIZZATIVA");
			else if (order.equals("nominativo"))
				sql.addOrderBy("BENEF_DENOMINAZIONE_SEDE");
			else if (order.equals("oggetto"))
				sql.addOrderBy("OGGETTO");
			else if (order.equals("importo"))
				sql.addOrderBy("nvl(IMPORTO_LORDO,0)+nvl(IMPORTO_VARIAZIONE,0)");
			else if (order.equals("datainizio"))
				sql.addOrderBy("DT_INIZIO_VALIDITA");
			else if (order.equals("datafine"))
				sql.addOrderBy("nvl(DT_FINE_VALIDITA_VARIAZIONE, DT_FINE_VALIDITA)");
		} 
			else sql.addOrderBy("DT_INIZIO_VALIDITA DESC");

		return sql;
	}

	public List completaListaIncarichiElenco(UserContext userContext, List list) throws ComponentException{
		try {	
			Hashtable sediUo = new Hashtable<String, String>();

			// inseriamo l'indirizzo della Sede del CDS
			// e il link al PDF del bando di concorso
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				V_incarichi_elencoBulk incarico = (V_incarichi_elencoBulk)iterator.next();

    			String indirizzo = (String) sediUo.get(incarico.getCd_unita_organizzativa());
    			if (indirizzo==null) {
    				indirizzo="";
	    			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(incarico.getCd_unita_organizzativa()));
	    			TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(userContext, uo);
	
	    			if (terzo != null) {
	    				if (terzo.getVia_sede() != null)
	    					indirizzo = indirizzo + terzo.getVia_sede();
	    				if (terzo.getNumero_civico_sede() != null)
	    					indirizzo = indirizzo +  ", " + terzo.getNumero_civico_sede();
	
	    				ComuneBulk comune = (ComuneBulk)getHome(userContext, ComuneBulk.class).findByPrimaryKey(terzo.getComune_sede());
	    				indirizzo = indirizzo + ", "+comune.getDs_comune();
	    			}
	    			sediUo.put(incarico.getCd_unita_organizzativa(), indirizzo);
    			}

    			incarico.setSede(indirizzo);

    			Incarichi_repertorio_varHome incVarHome = (Incarichi_repertorio_varHome)getHome(userContext, Incarichi_repertorio_varBulk.class);
				SQLBuilder sqlIncVar = incVarHome.createSQLBuilder();
				sqlIncVar.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incarico.getEsercizio());
				sqlIncVar.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
				sqlIncVar.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS,Incarichi_archivioBulk.STATO_DEFINITIVO);
				sqlIncVar.openParenthesis(FindClause.AND);
				sqlIncVar.addClause(FindClause.OR,"tipo_variazione",SQLBuilder.EQUALS,Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO_TRANS);
				sqlIncVar.addClause(FindClause.OR,"tipo_variazione",SQLBuilder.EQUALS,Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO);
				sqlIncVar.closeParenthesis();
				sqlIncVar.addOrderBy("DT_VARIAZIONE DESC");

				List listaVarRep = incVarHome.fetchAll(sqlIncVar);

				if (listaVarRep != null && !listaVarRep.isEmpty()) {
					incarico.setIncaricoVariazione((Incarichi_repertorio_varBulk)listaVarRep.get(0));
					List<Incarichi_archivioBulk> listaDownload = new BulkList();
					listaDownload.add((Incarichi_repertorio_varBulk)listaVarRep.get(0));
					incarico.setListDownloadUrl(listaDownload);
				} else {
					Incarichi_repertorio_archivioHome home2 = (Incarichi_repertorio_archivioHome)getHome(userContext, Incarichi_repertorio_archivioBulk.class);
					SQLBuilder sql2 = home2.createSQLBuilder();
					sql2.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incarico.getEsercizio());
					sql2.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
					sql2.addClause(FindClause.AND,"tipo_archivio",SQLBuilder.EQUALS,Incarichi_repertorio_archivioBulk.TIPO_CONTRATTO);
					sql2.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS,Incarichi_archivioBulk.STATO_VALIDO);
					List listaDownload = home2.fetchAll(sql2);

					incarico.setListDownloadUrl(listaDownload);
				} 

				Incarichi_repertorioHome incHome = (Incarichi_repertorioHome)getHome(userContext, Incarichi_repertorioBulk.class);
				incarico.setIncarichi_repertorio_rapp_detColl( new BulkList( incHome.findIncarichi_repertorio_rapp_detList(userContext, new Incarichi_repertorioBulk(incarico.getEsercizio(), incarico.getPg_repertorio()))));

				Incarichi_repertorioBulk incaricoRepertorio = (Incarichi_repertorioBulk)getHome(userContext, Incarichi_repertorioBulk.class).findByPrimaryKey(new Incarichi_repertorioBulk(incarico.getEsercizio(), incarico.getPg_repertorio()));
				Incarichi_proceduraBulk incaricoProcedura = (Incarichi_proceduraBulk)getHome(userContext, Incarichi_proceduraBulk.class).findByPrimaryKey(new Incarichi_proceduraBulk(incarico.getEsercizio_procedura(), incarico.getPg_procedura()));

				incaricoRepertorio.setIncarichi_procedura(incaricoProcedura);
				incarico.setIncaricoRepertorio(incaricoRepertorio);
				
				incaricoProcedura.setArchivioAllegati( new BulkList( ((Incarichi_proceduraHome) getHome( userContext, Incarichi_proceduraBulk.class )).findArchivioAllegati( incaricoProcedura ) ));
				incaricoRepertorio.setArchivioAllegati( new BulkList( ((Incarichi_repertorioHome) getHome( userContext, Incarichi_repertorioBulk.class )).findArchivioAllegati( incaricoRepertorio ) ));
			}
			return list;

		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (RemoteException ex) {
			throw handleException(ex);
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_richiestaBulk) {
				Incarichi_richiestaBulk richiesta = (Incarichi_richiestaBulk)oggettobulk; 
				richiesta.setEsercizio(CNRUserContext.getEsercizio(usercontext));
			}
			return oggettobulk;
		}catch( Exception e ){
			throw handleException( e );
		}		
	}
	public V_incarichi_elencoBulk completaIncaricoElenco(UserContext userContext, V_incarichi_elencoBulk bulk) throws ComponentException{
		try {
			Incarichi_repertorioBulk incaricoRepertorio = (Incarichi_repertorioBulk)getHome(userContext, Incarichi_repertorioBulk.class).findByPrimaryKey(new Incarichi_repertorioBulk(bulk.getEsercizio(), bulk.getPg_repertorio()));
			Incarichi_proceduraBulk incaricoProcedura = (Incarichi_proceduraBulk)getHome(userContext, Incarichi_proceduraBulk.class).findByPrimaryKey(new Incarichi_proceduraBulk(bulk.getEsercizio_procedura(), bulk.getPg_procedura()));

			incaricoRepertorio.setIncarichi_procedura(incaricoProcedura);
			bulk.setIncaricoRepertorio(incaricoRepertorio);
			
			incaricoProcedura.setArchivioAllegati( new BulkList( ((Incarichi_proceduraHome) getHome( userContext, Incarichi_proceduraBulk.class )).findArchivioAllegati( incaricoProcedura ) ));
			incaricoRepertorio.setArchivioAllegati( new BulkList( ((Incarichi_repertorioHome) getHome( userContext, Incarichi_repertorioBulk.class )).findArchivioAllegati( incaricoRepertorio ) ));
			
			return bulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}
