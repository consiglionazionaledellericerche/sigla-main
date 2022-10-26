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

package it.cnr.contab.security.auth;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Objects;

public class SIGLALDAPPrincipal implements Principal, Serializable {
    private final String name;
    private final Hashtable<String, Serializable> attributes;

    public SIGLALDAPPrincipal(String name) {
        this.name = name;
        this.attributes = new Hashtable<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public void addAttribute(String key, Serializable value) {
        this.attributes.put(key, value);
    }

    public Serializable getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SIGLALDAPPrincipal that = (SIGLALDAPPrincipal) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
