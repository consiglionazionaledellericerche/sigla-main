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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.Ass_mandato_reversaleBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ListaSospesiCNRPerCdsSelezionatoreBP extends SelezionatoreListaBP {
    private boolean isAbilitatoReversaleIncasso;

    public ListaSospesiCNRPerCdsSelezionatoreBP() {
        super("Tr");
    }

    public ListaSospesiCNRPerCdsSelezionatoreBP(String function) {
        super(function + "Tr");
    }

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            isAbilitatoReversaleIncasso = UtenteBulk.isAbilitatoReversaleIncasso(actioncontext.getUserContext());
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }

    public boolean isSostituisciButtonHidden() {
        return !(isAbilitatoReversaleIncasso &&
                Optional.ofNullable(getModel())
                        .filter(SospesoBulk.class::isInstance)
                        .map(SospesoBulk.class::cast)
                        .flatMap(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()))
                        .isPresent() &&
                Optional.ofNullable(getModel())
                        .filter(SospesoBulk.class::isInstance)
                        .map(SospesoBulk.class::cast)
                        .flatMap(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()))
                        .flatMap(mandatoIBulk -> Optional.ofNullable(mandatoIBulk.getStatoVarSos()))
                        .map(s -> !s.equalsIgnoreCase(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value()))
                        .orElse(Boolean.TRUE)
        );
    }

    @Override
    public List<Button> createToolbarList() {
        final List<Button> toolbarList = super.createToolbarList();
        toolbarList.add(new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.sostituisci"));
        return toolbarList;
    }

    public void confermaMandato(UserContext userContext, MandatoBulk mandatoBulk, MandatoBulk mandatoNew) throws BusinessProcessException {
        final MandatoComponentSession mandatoComponentSession = Optional.ofNullable(createComponentSession("CNRDOCCONT00_EJB_MandatoComponentSession"))
                .filter(MandatoComponentSession.class::isInstance)
                .map(MandatoComponentSession.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Errore nella creazione dell'EJB CNRDOCCONT00_EJB_MandatoComponentSession"));
        try {
            mandatoBulk = Optional.ofNullable(mandatoComponentSession.inizializzaBulkPerModifica(userContext, mandatoBulk))
                    .filter(MandatoBulk.class::isInstance)
                    .map(MandatoBulk.class::cast)
                    .orElseThrow(() -> new BusinessProcessException("Errore nella modifica del Mandato originario!"));

            mandatoBulk.setStatoVarSos(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value());
            mandatoBulk.setFl_riemissione(Boolean.TRUE);
            mandatoBulk.setPg_mandato_riemissione(mandatoNew.getPg_mandato());

            mandatoComponentSession.annullaMandato(userContext, mandatoBulk, true);
            /**
             * Associo al nuovo mandato le reversali collegate se presenti
             */
            final BulkList<Ass_mandato_reversaleBulk> reversaliColl = mandatoBulk.getReversaliColl();
            reversaliColl
                    .stream()
                    .filter(Ass_mandato_reversaleBulk.class::isInstance)
                    .map(Ass_mandato_reversaleBulk.class::cast)
                    .forEach(ass -> {
                try {
                    mandatoComponentSession.creaAss_mandato_reversale(userContext, mandatoNew, new ReversaleBulk(ass.getCd_cds_reversale(), ass.getEsercizio_reversale(), ass.getPg_reversale()));
                } catch (ComponentException | RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            final MandatoBulk mandatoBulk1 = Optional.ofNullable(mandatoComponentSession.inizializzaBulkPerModifica(userContext, mandatoNew))
                    .filter(MandatoBulk.class::isInstance)
                    .map(MandatoBulk.class::cast)
                    .orElseThrow(() -> new BusinessProcessException("Errore nella modifica del nuovo Mandato!"));
            mandatoBulk1.setIm_ritenute(mandatoBulk.getIm_ritenute());
            mandatoBulk1.setToBeUpdated();
            ((CRUDComponentSession)createComponentSession("JADAEJB_CRUDComponentSession")).modificaConBulk(userContext, mandatoBulk1);
            commitUserTransaction();
        } catch (ComponentException | RemoteException e) {
            mandatoBulk.setStatoVarSos(null);
            rollbackUserTransaction();
            throw handleException(e);
        }
    }
}