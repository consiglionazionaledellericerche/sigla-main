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

package it.cnr.contab.web.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public class MassimaleSpesaBulk {
    private static final long serialVersionUID = 1L;
    private Date data;
    private Long nazione;
    private Long inquadramento;
    private String cdTipoSpesa;
    private String cdTipoPasto;
    private String divisa;
    private BigDecimal km;
    private BigDecimal importoSpesa;

    public MassimaleSpesaBulk() {
        super();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getNazione() {
        return nazione;
    }

    public void setNazione(Long nazione) {
        this.nazione = nazione;
    }

    public Long getInquadramento() {
        return inquadramento;
    }

    public void setInquadramento(Long inquadramento) {
        this.inquadramento = inquadramento;
    }

    public String getCdTipoSpesa() {
        return cdTipoSpesa;
    }

    public void setCdTipoSpesa(String cdTipoSpesa) {
        this.cdTipoSpesa = cdTipoSpesa;
    }

    public String getCdTipoPasto() {
        return cdTipoPasto;
    }

    public void setCdTipoPasto(String cdTipoPasto) {
        this.cdTipoPasto = cdTipoPasto;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public BigDecimal getKm() {
        return km;
    }

    public void setKm(BigDecimal km) {
        this.km = km;
    }

    public BigDecimal getImportoSpesa() {
        return importoSpesa;
    }

    public void setImportoSpesa(BigDecimal importoSpesa) {
        this.importoSpesa = importoSpesa;
    }
}
