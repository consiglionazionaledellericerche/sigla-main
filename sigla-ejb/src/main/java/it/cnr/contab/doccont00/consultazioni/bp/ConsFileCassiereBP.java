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

package it.cnr.contab.doccont00.consultazioni.bp;

import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_giornaliera_cassaBulk;
import it.cnr.contab.doccont00.consultazioni.ejb.ConsFileCassiereComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

import java.util.Iterator;

public class ConsFileCassiereBP extends ConsultazioniBP {

    public static final String BASE = "BASE";
    public static final String BOTT1 = "BOTT1";
    public static final String BOTT2 = "BOTT2";
    public static final String BOTT3 = "BOTT3";
    public static final String BOTT4 = "BOTT4";
    public static final String BOTT5 = "BOTT5";
    public static final String BOTT6 = "BOTT6";
    public static final String BOTT7 = "BOTT7";
    public static final String BOTT8 = "BOTT8";
    public static final String BOTT9 = "BOTT9";
    public static final String BOTT10 = "BOTT10";
    public static final String FILENAME = "FILENAME";

    private String pathConsultazione;

    public ConsFileCassiereComponentSession createFileCassiereComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (ConsFileCassiereComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsFileCassiereComponentSession", ConsFileCassiereComponentSession.class);
    }

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        CompoundFindClause clauses = new CompoundFindClause();
        clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
        setBaseclause(clauses);
        if (getPathConsultazione() == null) {
            setPathConsultazione(FILENAME);
            super.init(config, context);
            initVariabili(context, null, getPathConsultazione());
        }

    }

    public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
        try {
            this.setPathConsultazione(livello_destinazione);
            setSearchResultColumnSet(getPathConsultazione());
            setFreeSearchSet(getPathConsultazione());
            setTitle();
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public java.util.Vector addButtonsToToolbar(java.util.Vector listButton) {
        if (getPathConsultazione().compareTo(ConsFileCassiereBP.FILENAME) == 0) {
            Button button1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dett");
            button1.setSeparator(true);
            listButton.addElement(button1);
        } else if (getPathConsultazione().compareTo(ConsFileCassiereBP.BASE) == 0) {
            Button button1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott1");
            button1.setSeparator(true);
            listButton.addElement(button1);

            Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott2");
            listButton.addElement(button2);

            Button button3 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott3");
            listButton.addElement(button3);

            Button button4 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott4");
            button4.setSeparator(true);
            listButton.addElement(button4);

            Button button5 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott5");
            listButton.addElement(button5);

            Button button6 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott6");
            button6.setSeparator(true);
            listButton.addElement(button6);

            Button button7 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott7");
            listButton.addElement(button7);

            Button button8 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott8");
            button8.setSeparator(true);
            listButton.addElement(button8);

            Button button9 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott9");
            listButton.addElement(button9);

            Button button10 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bott10");
            listButton.addElement(button10);
        }
        return listButton;
    }

    public String getPathConsultazione() {
        return pathConsultazione;
    }

    public void setPathConsultazione(String string) {
        pathConsultazione = string;
    }

    public boolean isPresenteBOTT1() {
        return getPathConsultazione().indexOf(BOTT1) >= 0;
    }

    public boolean isPresenteBOTT2() {
        return getPathConsultazione().indexOf(BOTT2) >= 0;
    }

    public boolean isPresenteBOTT3() {
        return getPathConsultazione().indexOf(BOTT3) >= 0;
    }

    public boolean isPresenteBOTT4() {
        return getPathConsultazione().indexOf(BOTT4) >= 0;
    }

    public boolean isPresenteBOTT5() {
        return getPathConsultazione().indexOf(BOTT5) >= 0;
    }

    public boolean isPresenteBOTT6() {
        return getPathConsultazione().indexOf(BOTT6) >= 0;
    }

    public boolean isPresenteBOTT7() {
        return getPathConsultazione().indexOf(BOTT7) >= 0;
    }

    public boolean isPresenteBOTT8() {
        return getPathConsultazione().indexOf(BOTT8) >= 0;
    }

    public boolean isPresenteBOTT9() {
        return getPathConsultazione().indexOf(BOTT9) >= 0;
    }

    public boolean isPresenteBOTT10() {
        return getPathConsultazione().indexOf(BOTT10) >= 0;
    }

    /**
     * Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
     * sulla base del path della consultazione
     */
    public void setTitle() {
        String title = null;
        title = "Consultazione File Cassiere";

        if (isPresenteBOTT1()) title = title.concat("- Dettaglio Accrediti Sospesi Registrati");
        if (isPresenteBOTT2()) title = title.concat("- Dettaglio Accrediti Sospesi Stornati");
        if (isPresenteBOTT3()) title = title.concat("- Dettaglio Accrediti Sospesi Regolati");
        if (isPresenteBOTT4()) title = title.concat("- Dettaglio Reversali Regolate");
        if (isPresenteBOTT5()) title = title.concat("- Dettaglio Reversali Stornate");
        if (isPresenteBOTT6()) title = title.concat("- Dettaglio Addediti Sospesi Registrati");
        if (isPresenteBOTT7()) title = title.concat("- Dettaglio Addediti Sospesi Stornati");
        if (isPresenteBOTT8()) title = title.concat("- Dettaglio Addediti Sospesi Regolati");
        if (isPresenteBOTT9()) title = title.concat("- Dettaglio Mandati Regolati");
        if (isPresenteBOTT10()) title = title.concat("- Dettaglio Mandati Stornati");

        getBulkInfo().setShortDescription(title);
        getBulkInfo().setLongDescription("Consultazione File Cassiere");

    }

    /**
     * Ritorna la CompoundFindClause ottenuta in base alla selezione effettuata
     *
     * @param context il campo da aggiornare
     * @param livello_destinazione il nuovo valore da sostituire al vecchio
     */

    public CompoundFindClause getSelezione(ActionContext context, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
        try {
            CompoundFindClause clauses = null;
            for (Iterator i = getSelectedElements(context).iterator(); i.hasNext(); ) {
                V_cons_giornaliera_cassaBulk bulk = (V_cons_giornaliera_cassaBulk) i.next();
                CompoundFindClause parzclause = new CompoundFindClause();
                if (livello_destinazione.compareTo(BASE) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                }
                if (livello_destinazione.compareTo(BOTT1) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_e", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_sos_e_aperti", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "32");
                }
                if (livello_destinazione.compareTo(BOTT2) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_e", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_sos_e_storni", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "32");
                }
                if (livello_destinazione.compareTo(BOTT3) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_e", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_rev_sospesi", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                if (livello_destinazione.compareTo(BOTT4) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "pg_reversale", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_reversali", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                if (livello_destinazione.compareTo(BOTT5) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "pg_reversale", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_rev_storni", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                if (livello_destinazione.compareTo(BOTT6) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_s", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_sos_s_aperti", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "32");
                }
                if (livello_destinazione.compareTo(BOTT7) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_s", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_sos_s_storni", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "32");
                }
                if (livello_destinazione.compareTo(BOTT8) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "cd_sospeso_s", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_man_sospesi", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                if (livello_destinazione.compareTo(BOTT9) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "pg_mandato", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_mandati", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                if (livello_destinazione.compareTo(BOTT10) == 0) {
                    parzclause.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
                    parzclause.addClause("AND", "nome_file", SQLBuilder.EQUALS, bulk.getNome_file());
                    parzclause.addClause("AND", "data_movimento", SQLBuilder.EQUALS, bulk.getData_movimento());
                    parzclause.addClause("AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
                    parzclause.addClause("AND", "pg_mandato", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "im_man_storni", SQLBuilder.ISNOTNULL, null);
                    parzclause.addClause("AND", "tr", SQLBuilder.EQUALS, "30");
                }
                // in realtà non serve (solo se nel caso si aggiunga la multiselezione)
                clauses = CompoundFindClause.or(clauses, parzclause);
            }
            return clauses;
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        try {
            setFindclause(compoundfindclause);
            return createFileCassiereComponentSession().findConsultazione(context.getUserContext(), getPathConsultazione(), getBaseclause(), compoundfindclause);
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            setIterator(context, createFileCassiereComponentSession().findConsultazione(context.getUserContext(), getPathConsultazione(), getBaseclause(), null));
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }
}
