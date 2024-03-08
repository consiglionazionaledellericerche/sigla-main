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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;

public class StampaIndiceTempestivitaPagamentiBulk extends OggettoBulk {
    private Integer esercizio;
    private String trimestre;
    private Boolean dettagli;

    public static final java.util.Dictionary<String, String> trimesteValues = new it.cnr.jada.util.OrderedHashtable();
    static {
        trimesteValues.put("1","Primo");
        trimesteValues.put("2","Secondo");
        trimesteValues.put("3","Terzo");
        trimesteValues.put("4","Quarto");
        trimesteValues.put("*","Intero Anno");
    }

    public StampaIndiceTempestivitaPagamentiBulk() {

    }

    @Override
    public OggettoBulk initializeForPrint(BulkBP bulkBP, ActionContext actioncontext) {
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
        setTrimestre("1");
        return super.initializeForPrint(bulkBP, actioncontext);
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public Boolean getDettagli() {
        return dettagli;
    }

    public void setDettagli(Boolean dettagli) {
        this.dettagli = dettagli;
    }

    public Integer getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
    }

}
