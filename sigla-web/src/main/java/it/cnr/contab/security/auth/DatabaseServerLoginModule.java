/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.Util;

import javax.security.auth.login.LoginException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatabaseServerLoginModule extends org.jboss.security.auth.spi.DatabaseServerLoginModule {
    @Override
    protected String createPasswordHash(String username, String password, String digestOption) throws LoginException {
        byte[] buser = username.toUpperCase().getBytes();
        if (username.startsWith("PDGP")) {
            return password;
        }
        byte[] bpassword = password.toUpperCase().getBytes();
        byte h = 0;
        for (int i = 0; i < bpassword.length; i++) {
            h = (byte) (bpassword[i] ^ h);
            for (int j = 0; j < buser.length; j++)
                bpassword[i] ^= buser[j] ^ h;
        }
        return Util.encodeBase64(bpassword);
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        if (!Optional.ofNullable(expectedPassword).isPresent())
            return true;
        return super.validatePassword(inputPassword, expectedPassword);
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        List<Group> groups = new ArrayList<>();
        try {
            groups = Arrays.asList(super.getRoleSets());
        } catch (Exception _ex) {
            groups.add(new SimpleGroup("Roles"));
        }
        groups.get(0).addMember(new SimplePrincipal("default-roles-cnr"));
        return groups
                .toArray(new Group[groups.size()]);
    }
}
