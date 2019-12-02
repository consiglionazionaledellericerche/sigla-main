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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_evBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaHome;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_cr_baseBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;



public class AssTipoCORIEvComponent extends it.cnr.jada.comp.CRUDComponent{

	public AssTipoCORIEvComponent() {
		super();
	}
	public SQLBuilder selectElemento_voceByClause(UserContext userContext, Ass_tipo_cori_evBulk ass, Elemento_voceBulk elem, CompoundFindClause clauses) 
	throws ComponentException
{		
	SQLBuilder sql = getHome(userContext, Elemento_voceBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","FL_PARTITA_GIRO",sql.EQUALS,"Y");
	//sql.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS,ass.getTi_appartenenza());
	sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,ass.getTi_gestione());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return sql;		
}
	public SQLBuilder selectVoce_epByClause(UserContext userContext, Ass_tipo_cori_voce_epBulk ass, ContoBulk elem, CompoundFindClause clauses)
	throws ComponentException
	{
		SQLBuilder sql = getHome(userContext, ContoBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause("AND","CD_VOCE_EP", sql.EQUALS,ass.getCd_voce_ep());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return sql;
	}
	public SQLBuilder selectVoce_ep_contrByClause(UserContext userContext, Ass_tipo_cori_voce_epBulk ass, ContoBulk elem, CompoundFindClause clauses)
	throws ComponentException
	{
		SQLBuilder sql = getHome(userContext, ContoBulk.class).createSQLBuilder();

		sql.addClause( clauses );
		sql.addSQLClause("AND","CD_VOCE_EP", sql.EQUALS,ass.getCd_voce_ep_contr());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return sql;
	}
	
	private Tipo_contributo_ritenutaBulk loadTipoContributoRitenuta(UserContext userContext, Tipo_contributo_ritenutaBulk ass) throws ComponentException{
		try 
		{
			Tipo_contributo_ritenutaHome home = (Tipo_contributo_ritenutaHome)getHome(userContext, Tipo_contributo_ritenutaBulk.class);
			Tipo_contributo_ritenutaBulk cori = home.findTipoCORIValido(ass.getCd_contributo_ritenuta(), home.getServerDate());		
			// se il tipo contributo ritenuta selezionato non è più valido
			// carico il tipo co/ri senza clausola di validita
			if (cori==null)
				cori = home.findTipoCORIValido(ass.getCd_contributo_ritenuta(), null);

			if (cori==null)
				throw new it.cnr.jada.comp.ApplicationException("Il Tipo Contributo/Ritenuta \"" + ass.getCd_contributo_ritenuta() + "\" associato NON esiste");
			return cori;
		}
		catch(it.cnr.jada.persistency.PersistencyException ex)
		{
			throw handleException(ex);
		}
	}
	
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		if (oggettobulk instanceof Ass_tipo_cori_evBulk ){
		 Ass_tipo_cori_evBulk ass = (Ass_tipo_cori_evBulk )super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		 ass.setContributo_ritenuta(loadTipoContributoRitenuta(usercontext, ass.getContributo_ritenuta())); 
		 Elemento_voceHome home = (Elemento_voceHome)getHome(usercontext, Elemento_voceBulk.class);
		 Elemento_voceBulk bulk;
		try {
			bulk = (Elemento_voceBulk)home.findByPrimaryKey(new Elemento_voceBulk(ass.getCd_elemento_voce(),ass.getEsercizio(),ass.getTi_appartenenza(),ass.getTi_gestione()));
			ass.setElemento_voce(bulk);
		} catch (PersistencyException e) {
			handleException(e);
		}	
		return ass;
		}
		else if (oggettobulk instanceof Ass_tipo_cori_voce_epBulk){
			 Ass_tipo_cori_voce_epBulk ass = (Ass_tipo_cori_voce_epBulk )super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			 ass.setContributo_ritenuta(loadTipoContributoRitenuta(usercontext, ass.getContributo_ritenuta())); 
			 ContoHome home = (ContoHome)getHome(usercontext,ContoBulk.class);
			 ContoBulk bulk;
			try {
				bulk = (ContoBulk)home.findByPrimaryKey(new ContoBulk(ass.getCd_voce_ep(),ass.getEsercizio()));
				ass.setVoce_ep(bulk);
				bulk = (ContoBulk)home.findByPrimaryKey(new ContoBulk(ass.getCd_voce_ep_contr(),ass.getEsercizio()));
				ass.setVoce_ep_contr(bulk);
			} catch (PersistencyException e) {
				handleException(e);
			}	
			return ass;
			}
		else if (oggettobulk instanceof Tipo_cr_baseBulk){
			Tipo_cr_baseBulk ass = (Tipo_cr_baseBulk )super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			 ass.setContributo_ritenuta(loadTipoContributoRitenuta(usercontext, ass.getContributo_ritenuta())); 
			 ContoHome home = (ContoHome)getHome(usercontext,ContoBulk.class);
			 ContoBulk bulk;
			
			return ass;
			}
		return super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		}
	public SQLBuilder selectGruppoByClause(UserContext userContext, Tipo_cr_baseBulk ass, Gruppo_crBulk elem, CompoundFindClause clauses) 
	throws ComponentException
{		
	SQLBuilder sql = getHome(userContext, Gruppo_crBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return sql;		
}
			
}