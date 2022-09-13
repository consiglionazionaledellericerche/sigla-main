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

package it.cnr.contab.utenze00.bulk;

import org.apache.commons.lang3.tuple.Pair;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TreeBulk extends Albero_mainKey{
    private String dsNodo;
    private String cdAccesso;
    private String dsAccesso;
    private java.lang.Long pgOrdinamento;
    private String businessProcess;
    private TreeBulk parentTreeBulk;

    public String getDsNodo() {
        return dsNodo;
    }

    public void setDsNodo(String dsNodo) {
        this.dsNodo = dsNodo;
    }

    public String getCdAccesso() {
        return cdAccesso;
    }

    public void setCdAccesso(String cdAccesso) {
        this.cdAccesso = cdAccesso;
    }

    public String getDsAccesso() {
        return dsAccesso;
    }

    public void setDsAccesso(String dsAccesso) {
        this.dsAccesso = dsAccesso;
    }

    public Long getPgOrdinamento() {
        return pgOrdinamento;
    }

    public void setPgOrdinamento(Long pgOrdinamento) {
        this.pgOrdinamento = pgOrdinamento;
    }

    public String getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    public TreeBulk getParentTreeBulk() {
        return parentTreeBulk;
    }

    public void setParentTreeBulk(TreeBulk parentTreeBulk) {
        this.parentTreeBulk = parentTreeBulk;
    }

    public List<Pair<String, String>> getBreadcrumb() {
        List<Pair<String, String>> breadcrumb = new ArrayList<>();
        TreeBulk nodo = this;

        do {
            Pair<String, String> p = Pair.of(nodo.getCd_nodo(), Optional.ofNullable(nodo.getDsNodo()).orElse(""));
            breadcrumb.add(p);

            nodo.getDsNodo();

            nodo = nodo.getParentTreeBulk();
        } while (nodo != null && nodo.getParentTreeBulk() != null);

        Collections.reverse(breadcrumb);

        return breadcrumb;


    }

}
