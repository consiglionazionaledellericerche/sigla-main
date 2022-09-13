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

package it.cnr.contab.domain;

import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.List;

/**
 * Created by francesco on 14/03/17.
 */
public class TreeNode implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String description;
    private String process;
    private String cdaccesso;
    private String dsaccesso;
    private List<Pair<String, String>> breadcrumb;
    public TreeNode(String id, String description, String process, String cdaccesso, String dsaccesso, List<Pair<String, String>> breadcrumb) {
        this.id = id;
        this.description = description;
        this.process = process;
        this.cdaccesso = cdaccesso;
        this.dsaccesso = dsaccesso;
        this.breadcrumb = breadcrumb;
    }

    public List<Pair<String, String>> getBreadcrumb() {
        return breadcrumb;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getProcess() {
        return process;
    }

    public String getCdaccesso() {
        return cdaccesso;
    }

    public String getDsaccesso() {
        return dsaccesso;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
            "id='" + id + '\'' +
            ", description='" + description + '\'' +
            ", process='" + process + '\'' +
            ", cdaccesso='" + cdaccesso + '\'' +
            ", dsaccesso='" + dsaccesso + '\'' +
            ", breadcrumb=" + breadcrumb +
            '}';
    }

}
