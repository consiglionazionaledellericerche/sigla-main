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

package it.cnr.contab.util;

import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.jada.action.BusinessProcessException;
import org.junit.Test;
import org.springframework.util.Assert;

public class CompensoCigModificabile {

    @Test
    public void compensoOne() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(!crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }

    @Test
    public void compensoTwo() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);
        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(!crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }

    @Test
    public void compensoThree() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_NON_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);
        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(!crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }

    @Test
    public void compensoFor() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);

        Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = new Obbligazione_scadenzarioBulk();
        ObbligazioneBulk obbligazioneBulk = new ObbligazioneBulk();
        obbligazioneBulk.setPg_obbligazione(Long.valueOf(1));
        obbligazioneBulk.getContratto().setPg_contratto(Long.valueOf(1));

        obbligazione_scadenzarioBulk.setObbligazione(obbligazioneBulk);
        compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);

        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }


    @Test
    public void compensoFive() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);

        Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = new Obbligazione_scadenzarioBulk();
        ObbligazioneBulk obbligazioneBulk = new ObbligazioneBulk();
        obbligazioneBulk.setPg_obbligazione(Long.valueOf(1));
        obbligazioneBulk.getContratto().setPg_contratto(Long.valueOf(1));

        obbligazione_scadenzarioBulk.setObbligazione(obbligazioneBulk);
        compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);

        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }


    @Test
    public void compensoSix() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);

        Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = new Obbligazione_scadenzarioBulk();
        ObbligazioneBulk obbligazioneBulk = new ObbligazioneBulk();
        obbligazioneBulk.setPg_obbligazione(Long.valueOf(1));

        obbligazione_scadenzarioBulk.setObbligazione(obbligazioneBulk);
        compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);

        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }

    @Test
    public void compensoSeven() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);

        Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = new Obbligazione_scadenzarioBulk();
        ObbligazioneBulk obbligazioneBulk = new ObbligazioneBulk();
        obbligazioneBulk.setPg_obbligazione(Long.valueOf(1));
        obbligazioneBulk.getContratto().setPg_contratto(Long.valueOf(1));

        final CigBulk cigBulk = new CigBulk();
        cigBulk.setCdCig("");
        obbligazioneBulk.getContratto().setCig(cigBulk);
        obbligazione_scadenzarioBulk.setObbligazione(obbligazioneBulk);
        compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);

        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(!crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }
    @Test
    public void compensoEight() {
        CRUDCompensoBP crudCompensoBP = new CRUDCompensoBP();
        final CompensoBulk compensoBulk = new CompensoBulk();
        final Tipo_trattamentoBulk tipo_trattamentoBulk = new Tipo_trattamentoBulk();
        tipo_trattamentoBulk.setCd_trattamento("TEST");
        tipo_trattamentoBulk.setTipoDebitoSiope(Tipo_trattamentoBulk.TIPO_DEBITO_COMMERCIALE);
        compensoBulk.setTipoTrattamento(tipo_trattamentoBulk);

        Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = new Obbligazione_scadenzarioBulk();
        ObbligazioneBulk obbligazioneBulk = new ObbligazioneBulk();
        obbligazioneBulk.setPg_obbligazione(Long.valueOf(1));
        obbligazioneBulk.setContratto(null);
        obbligazione_scadenzarioBulk.setObbligazione(obbligazioneBulk);
        compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);

        try {
            crudCompensoBP.setModel(null, compensoBulk);
            Assert.isTrue(crudCompensoBP.isCigModificabile(), "CIG non modificabile");
        } catch (BusinessProcessException e) {
        }
    }
}
