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

package it.cnr.contab.generator.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 22-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 [16-Aug-2006] adattamento a plugin
 */
public class Filter {

    private Collection<String> collection;

    public Filter() {
        collection = new ArrayList<String>();
        collection.add("DACR");
        collection.add("DUVA");
        collection.add("UTCR");
        collection.add("UTUV");
        collection.add("PG_VER_REC");
    }

    public boolean isFilter(String string) {
        return collection.contains(string);
    }

}
