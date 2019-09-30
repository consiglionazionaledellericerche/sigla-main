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

package it.cnr.contab.utenze00.bulk;

import java.io.Serializable;

public class ExternalUser implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int PRIORITY = 2;
	public static final String[] NEW = 
	{"firstName","familyName","email","login","profile","telefono","struttura","enabled","mailstop"};

    private String firstName;
    private String familyName;
    private String email;
    private String login;
    private int profile;
    //private String password;
    private String telefono;
    private String struttura;
    //private String strutturaDescrizione;
    private String enabled;
    private String mailStop;
    //private String blurred;
    

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		if(firstName!=null && firstName.trim().length()>0)
			this.firstName = firstName;
		else
			this.firstName = ".";
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		if(familyName!=null && familyName.trim().length()>0)
			this.familyName = familyName;
		else
			this.familyName = ".";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public int getProfile() {
		return profile;
	}
	public void setProfile(int profile) {
		this.profile = profile;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getStruttura() {
		return struttura;
	}
	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getMailStop() {
		return mailStop;
	}
	public void setMailStop(String mailStop) {
		this.mailStop = mailStop;
	}
	
}
