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


import it.cnr.jada.UserContext;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class TestUserContext implements UserContext {
    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public String getUser() {
        return "test";
    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public void setTransactional(boolean flag) {

    }

    @Override
    public void writeTo(PrintWriter printwriter) {

    }

    @Override
    public Dictionary getHiddenColumns() {
        return null;
    }

    @Override
    public Hashtable<String, Serializable> getAttributes() {
        return null;
    }
}
