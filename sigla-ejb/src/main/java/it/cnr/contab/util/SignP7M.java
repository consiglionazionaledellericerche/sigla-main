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

/**
 * Created by mspasiano on 7/5/17.
 */
public class SignP7M {
    private final String nodeRefSource;
    private final String username;
    private final String password;
    private final String otp;
    private final String nomeFile;

    public SignP7M(String nodeRefSource, String username, String password, String otp, String nomeFile) {
        this.nodeRefSource = nodeRefSource;
        this.username = username;
        this.password = password;
        this.otp = otp;
        this.nomeFile = nomeFile;
    }

    public String getNodeRefSource() {
        return nodeRefSource;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getOtp() {
        return otp;
    }

    public String getNomeFile() {
        return nomeFile;
    }

}
