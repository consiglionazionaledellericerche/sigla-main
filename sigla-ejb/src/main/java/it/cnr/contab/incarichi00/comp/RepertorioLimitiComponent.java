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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioHome;
import it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk;
import it.cnr.contab.incarichi00.bulk.Repertorio_limitiHome;
import it.cnr.contab.incarichi00.bulk.V_incarichi_cdsBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_cdsHome;
import it.cnr.contab.incarichi00.bulk.V_incarichi_da_assegnareBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_uoBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_uoHome;
import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaHome;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_limiteBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_limiteHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public class RepertorioLimitiComponent extends CRUDComponent implements ICRUDMgr,Cloneable,Serializable{
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Tipo_limiteBulk) {
				Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext))));
				boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
				
				Tipo_limiteHome tipoLimiteHome = (Tipo_limiteHome) getHome( usercontext, Tipo_limiteBulk.class);
				((Tipo_limiteBulk)oggettobulk).setRepertorio_limitiColl( new BulkList( tipoLimiteHome.findRepertorioLimitiList( (Tipo_limiteBulk)oggettobulk ) ));

				Repertorio_limitiHome repertorioLimitiHome = (Repertorio_limitiHome) getHome( usercontext, Repertorio_limitiBulk.class);
				V_incarichi_cdsHome inccdsHome = (V_incarichi_cdsHome) getHome( usercontext, V_incarichi_cdsBulk.class);
				V_incarichi_uoHome incuoHome = (V_incarichi_uoHome) getHome( usercontext, V_incarichi_uoBulk.class);
				Incarichi_proceduraHome proceduraHome = (Incarichi_proceduraHome) getHome( usercontext, Incarichi_proceduraBulk.class);
				Incarichi_repertorioHome incaricoHome = (Incarichi_repertorioHome) getHome( usercontext, Incarichi_repertorioBulk.class);
				
				for (Iterator i=((Tipo_limiteBulk)oggettobulk).getRepertorio_limitiColl().iterator();i.hasNext();){
					Repertorio_limitiBulk replim = (Repertorio_limitiBulk)i.next();
					BulkList incScadutiAllBulkList = new BulkList();
					BulkList incValidiAllBulkList  = new BulkList();
					BulkList incTerzoAllBulkList  = new BulkList();
					BigDecimal totalePrcCds = new BigDecimal(0);
	
					if (isUoEnte)
						replim.setIncarichi_x_cdsColl( new BulkList( repertorioLimitiHome.findIncarichi_cdsList(usercontext, replim)));
					else
						replim.setIncarichi_x_cdsColl( new BulkList( repertorioLimitiHome.findIncarichi_cdsList(usercontext, replim, uoScrivania.getUnita_padre())));
							
					for (Iterator x=replim.getIncarichi_x_cdsColl().iterator();x.hasNext();){
						V_incarichi_cdsBulk inccds = (V_incarichi_cdsBulk)x.next();
						BigDecimal totalePrcUo = new BigDecimal(0);
	
						/*valorizzo la percentuale se trattasi di uo ente*/
						if (isUoEnte) {
							if (Utility.nvl(replim.getImporto_limite()).compareTo(Utility.ZERO)==1) {
								inccds.setPrc_utilizzato(Utility.nvl(inccds.getIm_incarichi()).divide(Utility.nvl(replim.getImporto_limite()), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
								totalePrcCds = totalePrcCds.add( inccds.getPrc_utilizzato() );
							}
							else
								inccds.setPrc_utilizzato(new BigDecimal(0));
						}
						else 
							inccds.setPrc_utilizzato(replim.getImporto_limite().compareTo(Utility.ZERO)==0?new BigDecimal(0):new BigDecimal(100));

						if (isUoEnte)
							inccds.setIncarichi_x_uoColl( new BulkList( inccdsHome.findIncarichi_uoList(usercontext, inccds)));
						else
							inccds.setIncarichi_x_uoColl( new BulkList( inccdsHome.findIncarichi_uoList(usercontext, inccds, uoScrivania)));
	
						for (Iterator y=inccds.getIncarichi_x_uoColl().iterator();y.hasNext();){
							V_incarichi_uoBulk incuo = (V_incarichi_uoBulk)y.next();
							/*valorizzo la percentuale*/
							if (Utility.nvl(inccds.getIm_incarichi()).compareTo(Utility.ZERO)==1)
								if (x.hasNext())
								{
									incuo.setPrc_utilizzato(Utility.nvl(incuo.getIm_incarichi()).divide(Utility.nvl(inccds.getIm_incarichi()), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
									totalePrcUo = totalePrcUo.add( incuo.getPrc_utilizzato() );
								}
								else
									incuo.setPrc_utilizzato(new BigDecimal(100).subtract(totalePrcUo));
							else
								incuo.setPrc_utilizzato(new BigDecimal(0));
								
							BulkList listTerzo = new BulkList( incuoHome.findIncarichi_terzoList(usercontext, incuo) );
							incuo.setIncarichi_x_terzoColl( listTerzo );

							incTerzoAllBulkList.addAll( listTerzo );
								
							BulkList incScadutiBulkList = new BulkList();
							BulkList incValidiBulkList  = new BulkList();
							
							for (Iterator d=incuoHome.findIncarichi_validiList(usercontext, incuo).iterator();d.hasNext();){
								V_incarichi_da_assegnareBulk incAssegnare = (V_incarichi_da_assegnareBulk)d.next();
								incAssegnare.setIncarichi_procedura((Incarichi_proceduraBulk)proceduraHome.findByPrimaryKey(new Incarichi_proceduraBulk(incAssegnare.getEsercizio(), incAssegnare.getPg_procedura())));
								if (incAssegnare.getEsercizio_repertorio()!=null && incAssegnare.getPg_repertorio()!=null)
									incAssegnare.setIncarichi_repertorio((Incarichi_repertorioBulk)incaricoHome.findByPrimaryKey(new Incarichi_repertorioBulk(incAssegnare.getEsercizio_repertorio(), incAssegnare.getPg_repertorio())));
								if (incAssegnare.getIncarichi_procedura().isProceduraScaduta()) {
									incScadutiBulkList.add(incAssegnare);
									incScadutiAllBulkList.add(incAssegnare);
								}
								else
								{
									incValidiBulkList.add(incAssegnare);
									incValidiAllBulkList.add(incAssegnare);
								}
							}
							incuo.setIncarichi_scadutiColl( incScadutiBulkList );
							incuo.setIncarichi_validiColl(incValidiBulkList);
						}
						
						replim.setIncarichi_validiColl( incValidiAllBulkList );
						replim.setIncarichi_scadutiColl( incScadutiAllBulkList );
						replim.setIncarichi_x_terzoColl( incTerzoAllBulkList );						
					}
				}
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Tipo_limiteBulk) {
				Tipo_limiteHome tipoLimiteHome = (Tipo_limiteHome) getHome( usercontext, Tipo_limiteBulk.class);
		
				for (Iterator i = tipoLimiteHome.findRepertorioLimitiList( (Tipo_limiteBulk)oggettobulk ).iterator();i.hasNext();){
					Repertorio_limitiBulk bulk = (Repertorio_limitiBulk)i.next();
					if (bulk.getImporto_utilizzato().compareTo(Utility.ZERO)!=0)
						throw new ComponentException("Eliminazione non possibile! Risultano già essere state assegnate, per il limite di spesa da eliminare, somme per l'esercizio " + bulk.getEsercizio());
				}
				
				((Tipo_limiteBulk)oggettobulk).setFl_cancellato(Boolean.TRUE);
				oggettobulk.setToBeUpdated();
				updateBulk(usercontext, oggettobulk);
			}
			else
			{
				super.eliminaConBulk(usercontext, oggettobulk);
			}
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	private Repertorio_limitiBulk find(UserContext usercontext, int esercizio, String tipoLimite) throws ComponentException
	{
		try
		{
			return (Repertorio_limitiBulk) getHome( usercontext, Repertorio_limitiBulk.class ).findByPrimaryKey( new Repertorio_limitiBulk( esercizio, tipoLimite));
		}
		catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
		{
			return null;
		}	
		catch ( Exception e )
		{
			throw handleException( e );
		}
	}

	private Repertorio_limitiBulk findAndLock(UserContext usercontext, int esercizio, String tipoLimite) throws ComponentException
	{
		try
		{
			return (Repertorio_limitiBulk) getHome( usercontext, Repertorio_limitiBulk.class ).findAndLock( new Repertorio_limitiBulk( esercizio, tipoLimite));
		}
		catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
		{
			return null;
		}	
		catch ( Exception e )
		{
			throw handleException( e );
		}
		
	}

	public Repertorio_limitiBulk getRepertorioLimiti(UserContext userContext, int esercizio, String tipoIncarico, String tipoAttivita, String tipoNatura) throws ComponentException {
		try {
			Ass_incarico_attivitaHome assHome = (Ass_incarico_attivitaHome)getHome(userContext, Ass_incarico_attivitaBulk.class);
			Ass_incarico_attivitaBulk assBulk = (Ass_incarico_attivitaBulk)assHome.findByPrimaryKey(new Ass_incarico_attivitaBulk(esercizio, tipoIncarico, tipoAttivita, tipoNatura));
			
			if (assBulk!=null && assBulk.getCd_tipo_limite()!=null)
				return find(userContext, assBulk.getEsercizio(), assBulk.getCd_tipo_limite());
		}
		catch( Exception e )
		{
			throw handleException( e );
		}
		return null;
	}

	public Repertorio_limitiBulk aggiornaRepertorioLimiti(UserContext userContext, int esercizio, String cd_tipo_incarico, String cd_tipo_attivita, String cd_tipo_natura, BigDecimal importo) throws ComponentException
	{
		try
		{
			Repertorio_limitiBulk reperBulk = getRepertorioLimiti(userContext, esercizio, cd_tipo_incarico, cd_tipo_attivita, cd_tipo_natura); 

			Tipo_incaricoBulk tipoIncaricoBulk = (Tipo_incaricoBulk)getHome(userContext,Tipo_incaricoBulk.class).findByPrimaryKey(new Tipo_incaricoBulk(cd_tipo_incarico));
			Tipo_attivitaBulk tipoAttivitaBulk = (Tipo_attivitaBulk)getHome(userContext,Tipo_attivitaBulk.class).findByPrimaryKey(new Tipo_attivitaBulk(cd_tipo_attivita));

			if (reperBulk == null)
				throw new ApplicationException("Repertorio limiti non trovato nell'esercizio " +esercizio +" per:\n\n"+
						                       "- Tipo Incarico: "+tipoIncaricoBulk.getDs_tipo_incarico()+"\n"+
						                       "- Tipo Attivita: "+tipoAttivitaBulk.getDs_tipo_attivita()+"\n"+
						                       "- Natura: "+NaturaBulk.tipo_naturaKeys.get(cd_tipo_natura));
			
			reperBulk = findAndLock( userContext, reperBulk.getEsercizio(), reperBulk.getCd_tipo_limite());

			importo = importo.setScale(2, BigDecimal.ROUND_HALF_UP);

			if (reperBulk.getFl_raggiunto_limite() && importo.compareTo(Utility.ZERO)==1)
				  throw new ApplicationException(
							"Impossibile effettuare l'operazione !\n\n"+
					        "Nell'esercizio "+esercizio+ " per la combinazione: \n\n"+
		                    "- Tipo Incarico: "+tipoIncaricoBulk.getDs_tipo_incarico()+"\n"+
		                    "- Tipo Attivita: "+tipoAttivitaBulk.getDs_tipo_attivita()+"\n"+
		                    "- Natura: "+NaturaBulk.tipo_naturaKeys.get(cd_tipo_natura)+"\n\n"+
					        "è stato raggiunto il limite.");
			else {
				if (reperBulk.getImporto_residuo().compareTo(Utility.ZERO)==0 && importo.compareTo(Utility.ZERO)==1)
					  throw new ApplicationException(
						"Impossibile effettuare l'operazione !\n\n"+
				        "Nell'esercizio "+esercizio+ " per la combinazione: \n\n"+
	                    "- Tipo Incarico: "+tipoIncaricoBulk.getDs_tipo_incarico()+"\n"+
	                    "- Tipo Attivita: "+tipoAttivitaBulk.getDs_tipo_attivita()+"\n"+
	                    "- Natura: "+NaturaBulk.tipo_naturaKeys.get(cd_tipo_natura)+"\n\n"+
				        "la disponibilità ad attivare incarichi risulta essere esaurita.");
				else if (reperBulk.getImporto_residuo().subtract(importo).compareTo(Utility.ZERO)<0 && importo.compareTo(Utility.ZERO)==1)
					  throw new ApplicationException(
						"Impossibile effettuare l'operazione !\n\n"+
				        "Nell'esercizio "+esercizio+ " per la combinazione: \n\n"+
	                    "- Tipo Incarico: "+tipoIncaricoBulk.getDs_tipo_incarico()+"\n"+
	                    "- Tipo Attivita: "+tipoAttivitaBulk.getDs_tipo_attivita()+"\n"+
	                    "- Natura: "+NaturaBulk.tipo_naturaKeys.get(cd_tipo_natura)+"\n\n"+
				        "la disponibilità ad attivare incarichi (" + new it.cnr.contab.util.EuroFormat().format(reperBulk.getImporto_residuo()) +
				        ") risulta essere inferiore a quella necessaria (" + new it.cnr.contab.util.EuroFormat().format(importo) + ").");
			}

			reperBulk.setImporto_utilizzato(reperBulk.getImporto_utilizzato().add(importo));
			
			if (reperBulk.getImporto_residuo().compareTo(Utility.ZERO)!=1)
				reperBulk.setFl_raggiunto_limite(true);	

			reperBulk.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			reperBulk.setToBeUpdated();

			updateBulk( userContext, reperBulk );
			return reperBulk;
		}
		catch 	(Exception e )
		{
			throw handleException(  e );
		}	
	}
}
