/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.coepcoan00.consultazioni.bp;

import it.cnr.contab.coepcoan00.core.bulk.PartitarioBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class ConsultazionePartitarioBP<T extends IDocumentoAmministrativoBulk> extends SelezionatoreListaBP implements SearchProvider, TableCustomizer {

    protected List<T> documentoAmministrativo;
    protected CompoundFindClause baseClause;
    protected String columnSet;

    public ConsultazionePartitarioBP(List<T> documentoAmministrativo) {
        this.documentoAmministrativo = documentoAmministrativo;
    }

    public ConsultazionePartitarioBP(List<T> documentoAmministrativo, String columnSet) {
        this.documentoAmministrativo = documentoAmministrativo;
        this.columnSet = columnSet;
    }

    public ConsultazionePartitarioBP(String s, List<T> documentoAmministrativo) {
        super(s);
        this.documentoAmministrativo = documentoAmministrativo;
    }

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        setBulkInfo(BulkInfo.getBulkInfo(PartitarioBulk.class));
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary(Optional.ofNullable(columnSet).orElse("partitario")));
        setModel(actioncontext, new PartitarioBulk());
        setMultiSelection(false);
        disableSelection();
    }

    private CompoundFindClause addBaseClause(ActionContext actioncontext, CompoundFindClause compoundFindClause) {
        CompoundFindClause baseClause = new CompoundFindClause();
        for (T documentoAmministrativoBulk : documentoAmministrativo) {
            if (documentoAmministrativoBulk != null) {
                CompoundFindClause child = new CompoundFindClause();
                child.addClause(FindClause.AND, "cd_tipo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_tipo_doc_amm());
                child.addClause(FindClause.AND, "esercizio_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getEsercizio());
                child.addClause(FindClause.AND, "cd_cds_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_cds());
                child.addClause(FindClause.AND, "cd_uo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_uo());
                child.addClause(FindClause.AND, "pg_numero_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getPg_doc_amm());
                baseClause = CompoundFindClause.or(baseClause, child);
            }
        }
        return CompoundFindClause.and(baseClause, compoundFindClause);
    }

    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException, BusinessProcessException {
        return (it.cnr.jada.ejb.CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
    }

    public RemoteIterator search(
            ActionContext actioncontext,
            CompoundFindClause compoundfindclause,
            OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return createComponentSession().cerca(actioncontext.getUserContext(),
                    addBaseClause(actioncontext, Optional.ofNullable(compoundfindclause)
                            .orElseGet(() -> new CompoundFindClause())),
                    (OggettoBulk) getBulkInfo().getBulkClass().newInstance(), "selectByClauseForPartitario");
        } catch (ComponentException | RemoteException | IllegalAccessException | InstantiationException e) {
            throw handleException(e);
        }
    }

    public void openIterator(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setIterator(actioncontext, search(
                    actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    @Override
    public void writeHTMLTable(PageContext pagecontext, String s, String s1) throws IOException, ServletException {
        super.writeHTMLTable(pagecontext, s, s1);
    }

    @Override
    public boolean isOrderableBy(String s) {
        return Boolean.FALSE;
    }
    @Override
    public String getRowCSSClass(Object obj, boolean even) {
        return Optional.ofNullable(obj)
                .filter(PartitarioBulk.class::isInstance)
                .map(PartitarioBulk.class::cast)
                .map(partitarioBulk -> {
                    switch (partitarioBulk.getCd_riga()) {
                        case "T" : {
                            return "shadow font-weight-bold font-italic " +
                                    (partitarioBulk.getDifferenza().equals(BigDecimal.ZERO) ? "text-primary" : "text-danger");
                        }
                        default:
                            return null;
                    }
                }).orElse(null);

    };
    @Override
    public String getRowStyle(Object obj) {
        return null;
    }

    @Override
    public boolean isRowEnabled(Object obj) {
        return false;
    }

    @Override
    public boolean isRowReadonly(Object obj) {
        return false;
    }

    @Override
    public String getTableClass() {
        return null;
    }

    @Override
    public void writeTfoot(JspWriter jspwriter) throws IOException {
    }
}
