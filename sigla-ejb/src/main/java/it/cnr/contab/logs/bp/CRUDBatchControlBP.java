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

package it.cnr.contab.logs.bp;

import it.cnr.contab.coepcoan00.ejb.AsyncScritturaPartitaDoppiaFromDocumentoComponentSession;
import it.cnr.contab.coepcoan00.ejb.ScritturaPartitaDoppiaFromDocumentoComponentSession;
import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;
import java.rmi.RemoteException;

public class CRUDBatchControlBP extends SimpleCRUDBP
{

    public CRUDBatchControlBP()
    {
        parametri = new SimpleDetailCRUDController("parametri", it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, "parametri", this);
        parametri.setReadonly(false);
    }

    public CRUDBatchControlBP(String s)
    {
        super(s);
        parametri = new SimpleDetailCRUDController("parametri", it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, "parametri", this);
        parametri.setReadonly(false);
    }

    protected Button[] createToolbar()
    {
        Button abutton[] = new Button[1];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        return abutton;
    }

    public final SimpleDetailCRUDController getParametri()
    {
        return parametri;
    }

    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        return oggettobulk;
    }

    private final SimpleDetailCRUDController parametri;

    @Override
    public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        try {
            Batch_controlBulk batch_controlbulk = (Batch_controlBulk) this.getModel();
            if (batch_controlbulk.getProcedura().isProceduraJava() && "REGISTRACOGECOANJAVA".equals(batch_controlbulk.getProcedura().getCd_procedura())) {
                AsyncScritturaPartitaDoppiaFromDocumentoComponentSession component = Utility.createAsyncScritturaPartitaDoppiaFromDocumentoComponentSession();
                BigDecimal esercizio = batch_controlbulk.getParametri().stream()
                        .filter(el->el.getNome_parametro().equals("AES"))
                        .findAny()
                        .map(Batch_procedura_parametroBulk::getValore_number)
                        .orElseThrow(()->new ValidationException("Valorizzare il parametro Esercizio!"));

                String cdcds = batch_controlbulk.getParametri().stream()
                        .filter(el->el.getNome_parametro().equals("ACDCDS"))
                        .findAny()
                        .map(Batch_procedura_parametroBulk::getValore_varchar)
                        .orElseThrow(()->new ValidationException("Valorizzare il parametro Centro di Spesa!"));

                component.asyncLoadScritturePatrimoniali(actioncontext.getUserContext(), esercizio.intValue(), cdcds);
            }
            super.save(actioncontext);
        } catch (ComponentException | PersistencyException | RemoteException e){
            throw handleException(e);
        }
    }
}