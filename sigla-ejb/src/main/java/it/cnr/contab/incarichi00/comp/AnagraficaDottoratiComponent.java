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

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraHome;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.NumerazioneTempDocAmmComponentSession;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiHome;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiRateBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.52)
 *
 * @author: Roberto Fantino
 */
public class AnagraficaDottoratiComponent extends it.cnr.jada.comp.CRUDComponent {
    /**
     * CompensoComponent constructor comment.
     */
    public AnagraficaDottoratiComponent() {
        super();
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectTerzoByClause(
            UserContext userContext, AnagraficaDottoratiBulk anagraficaDottorati,
            TerzoBulk terzo, CompoundFindClause clauses
    ) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("anagrafico", "ANAGRAFICO");
        sql.addClause(FindClause.AND, "ti_terzo", SQLBuilder.NOT_EQUALS, "C");
      /**  sql.addSQLClause(FindClause.AND, "cognome", SQLBuilder.CONTAINS, anagraficaDottorati.getCognome());
        sql.addSQLClause(FindClause.AND, "nome", SQLBuilder.CONTAINS, anagraficaDottorati.getNome());
        sql.addSQLClause(FindClause.AND, "ragione_sociale", SQLBuilder.CONTAINS, anagraficaDottorati.getRagioneSociale());
        sql.addSQLClause(FindClause.AND, "codice_fiscale", SQLBuilder.CONTAINS, anagraficaDottorati.getCodiceFiscale());
        sql.addSQLClause(FindClause.AND, "partita_iva", SQLBuilder.CONTAINS, anagraficaDottorati.getPartitaIva());*/
        sql.addClause(clauses);
        return sql;
    }

    public AnagraficaDottoratiBulk completaTerzo(UserContext userContext, AnagraficaDottoratiBulk anagraficaDottoratiBulk, TerzoBulk terzoBulk) throws ComponentException {
        TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
        try {
            anagraficaDottoratiBulk.setModalita(terzoHome.findRif_modalita_pagamento(terzoBulk));
            anagraficaDottoratiBulk.setTermini(terzoHome.findRif_termini_pagamento(terzoBulk));
        } catch (PersistencyException | IntrospectionException e) {
            throw handleException(e);
        }
        return anagraficaDottoratiBulk;
    }

    /**
     * Percipiente selezionato
     * PreCondition:
     * Viene richiesta la lista delle Modalità di pagamento
     * associate al percipiente
     * PostCondition:
     * Viene restituita la lista dei Modalità di pagamento
     * associate al percipiente
     * <p>
     * Percipiente NON selezionato
     * PreCondition:
     * Non è stato selezionato il percipiente
     * PostCondition:
     * Non vengono caricate le modalità di pagamento
     **/

    public java.util.Collection findModalita(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {
        try {
            AnagraficaDottoratiBulk anagraficaDottoratiBulk = (AnagraficaDottoratiBulk) bulk;
            if (anagraficaDottoratiBulk.getTerzo() == null ||
                    anagraficaDottoratiBulk.getTerzo().getCd_terzo() == null)
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
            return terzoHome.findRif_modalita_pagamento(anagraficaDottoratiBulk.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }
    }
    /**
     * Percipiente selezionato
     *    PreCondition:
     *		 Viene richiesta la lista dei Termini di pagamento
     * 	 associati al terzo
     *	   PostCondition:
     *		 Viene restituita la lista dei Termini di pagamento
     * 	 associati al terzo
     *
     * Percipiente NON selezionato
     *    PreCondition:
     *		 Non è stato selezionato il terzo
     *	   PostCondition:
     *		 Non vengono caricati i termini di pagamento
     **/
    public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException{
        try {
            AnagraficaDottoratiBulk anagraficaDottoratiBulk = (AnagraficaDottoratiBulk)bulk;
            if(anagraficaDottoratiBulk.getTerzo() == null ||
                    anagraficaDottoratiBulk.getTerzo().getCd_terzo() == null)
                return null;
            TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
            return terzoHome.findRif_termini_pagamento(anagraficaDottoratiBulk.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex){
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex){
            throw handleException(bulk, ex);
        }
    }

    public AnagraficaDottoratiBulk inizializzaBulkPerInserimento(UserContext param0, OggettoBulk param1) throws ComponentException {
        String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(param0);
        String uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(param0);
        AnagraficaDottoratiBulk anagraficaDottorati = (AnagraficaDottoratiBulk) param1;
        anagraficaDottorati.setCdCds(cds);
        anagraficaDottorati.setCdUnitaOrganizzativa(uo);
        anagraficaDottorati.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(param0));
        return anagraficaDottorati;
    }

    /**
     *  Normale.
     *    PreCondition:
     *      Nessun errore segnalato.
     *    PostCondition:
     *      Viene restituita la lista delle banche del percipiente.
     */
//^^@@

    public java.util.List findListaBanche(
            UserContext userContext,
            AnagraficaDottoratiBulk anagraficaDottorati)
            throws ComponentException {

        try {
            if(anagraficaDottorati.getPercipiente() == null ||
                    anagraficaDottorati.getPercipiente().getCd_terzo() == null)
                return null;

            return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, anagraficaDottorati, null, null));
        }catch(it.cnr.jada.persistency.PersistencyException ex){
            throw handleException(ex);
        }
    }

    public SQLBuilder selectBancaByClause(UserContext userContext, AnagraficaDottoratiBulk anagraficaDottorati, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {

        BancaHome bancaHome = (BancaHome)getHome(userContext, BancaBulk.class);

        SQLBuilder sql = bancaHome.selectBancaFor(
                anagraficaDottorati.getModalita_pagamento(),
                anagraficaDottorati.getCdTerzo());
        sql.addClause(clauses);

        return sql;
    }

    /**
     *   Validazione minicarriera.
     *	PreCondition:
     *		Viene richiesta la generazione delle rate di una minicarriera e la stessa non ha superato il metodo 'validate'.
     *	PostCondition:
     *		Non  viene consentita la generazione delle rate della minicarriera.
     *   Tutti i controlli superati.
     *	PreCondition:
     *		Viene richiesta la generazione delle rate di una minicarriera e la stessa ha superato il metodo 'validate'.
     *	PostCondition:
     *		Viene consentita la generazione delle rate della minicarriera.
     */
//^^@@

    public AnagraficaDottoratiBulk generaRate(
            UserContext userContext,
            AnagraficaDottoratiBulk carriera,
            boolean eseguiCopia)
            throws it.cnr.jada.comp.ComponentException {

        try{
            carriera.validate();
            Long pgTmp = null;
            //eseguiCopia &&
            if (carriera.getIdAnagraficaDottoratiPerClone() == null){
                pgTmp = assegnaProgressivoTemporaneo(userContext, carriera);
                carriera.setIdAnagraficaDottoratiPerClone(pgTmp);
                //copiaMinicarriera(userContext, carriera);
            }

            if (carriera.getId() == null){
                pgTmp = assegnaProgressivoTemporaneo(userContext, carriera);
                carriera.setId(pgTmp);
                insertBulk(userContext, carriera);
            }else{
                updateBulk(userContext, carriera);
            }

            callGeneraRate(userContext, carriera);
            return reloadAnagraficaDottorati(userContext, carriera);

        } catch(it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(carriera,ex);
        } catch(it.cnr.jada.bulk.ValidationException ex) {
            throw handleException(carriera,ex);
        }
    }

    private Long assegnaProgressivoTemporaneo(
            UserContext userContext,
            AnagraficaDottoratiBulk carriera)
            throws ComponentException {

        try {
            // Assegno un nuovo progressivo temporaneo alla minicarriera
            NumerazioneTempDocAmmComponentSession session = (NumerazioneTempDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_NumerazioneTempDocAmmComponentSession", NumerazioneTempDocAmmComponentSession.class);
            Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(carriera);
            numerazione.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            return session.getNextTempPG(userContext, numerazione);
        }catch(javax.ejb.EJBException ex){
            throw handleException(carriera, ex);
        }catch(RemoteException ex){
            throw handleException(carriera, ex);
        }

    }

    private void callGeneraRate(UserContext userContext, AnagraficaDottoratiBulk carriera) throws ComponentException{

        LoggableStatement cs = null;
        try	{
            cs = new LoggableStatement(getConnection(userContext),
                    "{call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB605.creaRateMinicarriera(?,?,?,?,?,?,?,?) }",
                    false,this.getClass());
            cs.setString( 1, carriera.getCd_cds()                 );
            cs.setString( 2, carriera.getCdUnitaOrganizzativa() );
            cs.setInt( 3, carriera.getEsercizio().intValue() );
            cs.setLong( 4, carriera.getId() );

            if (carriera.getIdAnagraficaDottoratiPerClone() != null) {
                cs.setString( 5, carriera.getCd_cds() );
                cs.setString( 6, carriera.getCdUnitaOrganizzativa() );
                cs.setInt( 7, carriera.getEsercizio().intValue() );
                cs.setLong( 8, carriera.getIdAnagraficaDottoratiPerClone().longValue() );
            } else {
                cs.setNull( 5, java.sql.Types.VARCHAR );
                cs.setNull( 6, java.sql.Types.VARCHAR );
                cs.setNull( 7, java.sql.Types.NUMERIC );
                cs.setNull( 8, java.sql.Types.NUMERIC );
            }

            cs.executeQuery();

        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            try {
                if (cs != null) cs.close();
            } catch (java.sql.SQLException e) {
                throw handleException(e);
            }
        }
    }

    private AnagraficaDottoratiBulk reloadAnagraficaDottorati(UserContext userContext, AnagraficaDottoratiBulk carriera) throws ComponentException{

        try {
            Long pgTmp = carriera.getIdAnagraficaDottoratiPerClone();
        //    it.cnr.jada.bulk.PrimaryKeyHashMap saldi = carriera.getDefferredSaldi();

            AnagraficaDottoratiHome home = (AnagraficaDottoratiHome)getHome(userContext, carriera);
            AnagraficaDottoratiBulk carrieraCaricata = (AnagraficaDottoratiBulk)home.findByPrimaryKey(carriera);

            try {
                BulkList rate = new BulkList(findRate(userContext, carrieraCaricata));
                for (java.util.Iterator i = carriera.getAnagraficaDottoratiRate().iterator(); i.hasNext();) {
                    AnagraficaDottoratiRateBulk vecchiaRata = (AnagraficaDottoratiRateBulk)i.next();
                }
                carrieraCaricata.setAnagraficaDottoratiRate(rate);
            } catch (it.cnr.jada.persistency.IntrospectionException e) {
                throw handleException(carriera, e);
            }

            getHomeCache(userContext).fetchAll(userContext);

            carrieraCaricata.setIdAnagraficaDottoratiPerClone(pgTmp);
       //     completaMinicarriera(userContext, carrieraCaricata); //da vedere se serve
       //     carrieraCaricata.setDefferredSaldi(saldi);

            return carrieraCaricata;

        }catch (it.cnr.jada.persistency.PersistencyException ex){
            throw handleException(ex);
        }
    }

    /**
     *	Normale.
     *		PreCondition:
     * 		Richiesta di caricamento rate di una minicarriera
     *   	PostCondition:
     *  		Restituisce la lista delle rate
     */
//^^@@

    public java.util.List findRate(UserContext aUC,AnagraficaDottoratiBulk carriera)
            throws
            ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        if (carriera == null) return null;

        it.cnr.jada.bulk.BulkHome home = getHome(aUC, AnagraficaDottoratiRateBulk.class);

        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "pg_minicarriera", sql.EQUALS, carriera.getId());
        sql.addClause("AND", "cd_cds", sql.EQUALS, carriera.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, carriera.getEsercizio());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, carriera.getCdUnitaOrganizzativa());
        sql.addOrderBy("PG_RATA");
        return home.fetchAll(sql);
    }

 /**   private void completaAnagraficaDottorati(
            UserContext userContext,
            AnagraficaDottoratiBulk carriera)
            throws ComponentException{

        try {

            carriera.setPercipiente(loadPercipiente(userContext,carriera));
            completaPercipiente(userContext, carriera);
            carriera.setTipiTrattamento(findTipiTrattamento(userContext, carriera));
            loadTipoTrattamento(userContext, carriera);

            //if (isGestitePrestazioni(userContext))
            //{
            carriera.impostaVisualizzaPrestazione();
            //carriera.setTipiPrestazioneCompenso(getHome(userContext,Tipo_prestazione_compensoBulk.class).findAll());
            carriera.setTipiPrestazioneCompenso(findTipiPrestazioneCompenso(userContext,
                    carriera));
            //}
            //else
            //carriera.setVisualizzaPrestazione(false);

            if (isGestitiIncarichi(userContext))
                carriera.impostaVisualizzaIncarico();
            else
                carriera.setVisualizzaPrestazione(false);

            getHomeCache(userContext).fetchAll(userContext);
            carriera.setAliquotaCalcolata(
                    carriera.getFl_tassazione_separata() != null &&
                            carriera.getFl_tassazione_separata().booleanValue() &&
                            new java.math.BigDecimal(0).compareTo(carriera.getAliquota_irpef_media()) != 0);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }*/

}
