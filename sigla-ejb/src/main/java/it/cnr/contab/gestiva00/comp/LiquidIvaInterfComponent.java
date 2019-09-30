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
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_ripart_finBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_ripart_finHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_massa_ivaVBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LiquidIvaInterfComponent extends CRUDComponent {
	public boolean contaRiga(UserContext userContext, Liquid_iva_interfBulk liquid_iva) throws ComponentException{
	  try
	  {
			Liquid_iva_interfHome testataHome = (Liquid_iva_interfHome)getHome(userContext, Liquid_iva_interfBulk.class);
			SQLBuilder sql = testataHome.createSQLBuilder();
		    sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND","CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));		    
			sql.addSQLClause("AND","DT_INIZIO", sql.EQUALS, liquid_iva.getDt_inizio());	
			try {
				return sql.executeExistsQuery(testataHome.getConnection());
			} catch (java.sql.SQLException e) {
				throw handleSQLException(e);
			}
	  }
	  catch ( Exception e )
	  {
		throw handleException( e );
	  }
	}
	public void inserisciRighe(UserContext userContext, Liquid_iva_interfBulk liquid_iva) throws ComponentException{
		it.cnr.contab.config00.sto.bulk.CdsHome cdsHome = (it.cnr.contab.config00.sto.bulk.CdsHome)getHome(userContext, CdsBulk.class);
		
		java.util.List listaUoCds;
		try {
			listaUoCds = cdsHome.findUoCds(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			for (java.util.ListIterator i = listaUoCds.listIterator();i.hasNext();) {
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)i.next();
				for (Enumeration j = liquid_iva.getTIPI_LIQ().keys();j.hasMoreElements();) {					
					Liquid_iva_interfBulk liquid = new Liquid_iva_interfBulk(new Integer(0),it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext),it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),uo.getCd_unita_organizzativa(),liquid_iva.getDt_fine(),liquid_iva.getDt_inizio(),(String)j.nextElement());
					liquid.setFl_gia_eleborata(new Boolean(false));	
					liquid.setIva_credito(new BigDecimal("0"));
					liquid.setIva_debito(new BigDecimal("0"));	
					liquid.setNote(new String("Per liquidazione iva "+liquid_iva.getMese()));
					liquid.setToBeCreated();
					super.creaConBulk(userContext,liquid);
				}					
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} 
	}	
	public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
	   SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	   sql.addOrderBy("DT_INIZIO, DT_FINE, CD_UNITA_ORGANIZZATIVA, TI_LIQUIDAZIONE");
	   //sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,CNRUserContext.getEsercizio(userContext));
	   return sql;
	}

	public Liquidazione_definitiva_ivaVBulk inizializzaMese(UserContext aUC,Liquidazione_definitiva_ivaVBulk bulk) throws ComponentException{
		try	{
			if (bulk!=null) { 
				bulk.setRipartizione_finanziaria( new BulkList());
				bulk.setLiquidazioniProvvisorie( new BulkList());
				bulk.setVariazioni_associate( new BulkList());
				bulk.setMandato_righe_associate( new BulkList());
	
				if (bulk.getMese()!=null) {
					Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(aUC, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC))));
					boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;

					if (isUoEnte) {
						Liquidazione_ivaHome home= (Liquidazione_ivaHome) getHome(aUC, Liquidazione_ivaBulk.class);
						if (bulk.getProspetti_stampati()!=null) {
							for (Iterator iterator = bulk.getProspetti_stampati().iterator(); iterator.hasNext();) {
								Liquidazione_ivaBulk liq = (Liquidazione_ivaBulk) iterator.next();
								Calendar cal = new GregorianCalendar();
								cal.setTime(liq.getDt_inizio());
								if (bulk.getMesi_int().get(bulk.getMese()).equals(cal.get(Calendar.MONTH)+1)) {
									bulk.setMandato_righe_associate( new BulkList( home.findMandatoRigheAssociateList(liq) ));
									break;
								}								
							}
						}
					} else {
						Liquidazione_ivaHome home= (Liquidazione_ivaHome) getHome(aUC, Liquidazione_ivaBulk.class);
						bulk.setRipartizione_finanziaria( new BulkList( home.findRipartizioneFinanziariaList( bulk ) ));
						if (!bulk.isRegistroStampato(bulk.getMese())) {
							bulk.setLiquidazioniProvvisorie( new BulkList( home.findLiquidazioniProvvisorieList( bulk ) ));
						} else {
							bulk.setVariazioni_associate( new BulkList( home.findVariazioniAssociateList( bulk ) ));
						}
						getHomeCache(aUC).fetchAll(aUC);
					}
				}
			}
			return bulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	public Liquidazione_massa_ivaVBulk inizializzaMese(UserContext aUC,Liquidazione_massa_ivaVBulk bulk) throws ComponentException{
		try	{
			if (bulk!=null) { 
				bulk.setLiquidazioniProvvisorie( new BulkList());
				bulk.setLiquidazioniDefinitive( new BulkList());
	
				if (bulk.getMese()!=null) {
					Liquidazione_ivaHome home= (Liquidazione_ivaHome) getHome(aUC, Liquidazione_ivaBulk.class);
					bulk.setLiquidazioniProvvisorie( new BulkList( home.findLiquidazioniMassiveProvvisorieList( bulk ) ));
					bulk.setLiquidazioniDefinitive( new BulkList( home.findLiquidazioniMassiveDefinitiveList( bulk ) ));
					getHomeCache(aUC).fetchAll(aUC);
				}
			}
			return bulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	public void saveRipartizioneFinanziaria(UserContext aUC,Liquidazione_definitiva_ivaVBulk bulk) throws ComponentException{
		try	{
			if (bulk!=null && bulk.getRipartizione_finanziaria()!=null) {
				Liquidazione_ivaHome home= (Liquidazione_ivaHome) getHome(aUC, Liquidazione_ivaBulk.class);
				Liquidazione_iva_ripart_finHome homeRipart= (Liquidazione_iva_ripart_finHome) getHome(aUC, Liquidazione_iva_ripart_finBulk.class);

				//cancello prima tutta la vecchia ripartizione
				List listDB = home.findRipartizioneFinanziariaList( bulk );
				for (Object object : listDB) {
					((OggettoBulk)object).setToBeDeleted();
					deleteBulk(aUC, (OggettoBulk)object);
				}

				//poi reinserisco
				long pg_dettaglio = 1;
				for (Object object : bulk.getRipartizione_finanziaria()) {
					if(((Liquidazione_iva_ripart_finBulk)object).getEsercizio_variazione()==null)
						throw new ApplicationException("Indicare l'esercizio della variazione");
					else if(((Liquidazione_iva_ripart_finBulk)object).getIm_variazione()==null)
						throw new ApplicationException("Indicare l'importo della variazione");
					else{
						((Liquidazione_iva_ripart_finBulk)object).setPg_dettaglio(pg_dettaglio++);
						((OggettoBulk)object).setUser(CNRUserContext.getUser(aUC));
						((OggettoBulk)object).setToBeCreated();
						homeRipart.insert((Persistent)object, aUC);
					}
				}
			}
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}