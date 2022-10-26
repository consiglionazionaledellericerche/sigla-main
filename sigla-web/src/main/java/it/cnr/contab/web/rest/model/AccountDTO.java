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

package it.cnr.contab.web.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cnr.contab.utenze00.bulk.UtenteBulk;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class AccountDTO {
    private static final long serialVersionUID = 1L;
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SUPERUSER = "ROLE_SUPERUSER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final int MONTH_EXPIRED = 6;

    @JsonIgnore
    private UtenteBulk currentUser;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean ldap;

    private Collection<String> authorities;
    private String login;
    private Integer esercizio;
    private String cds;
    private String uo;
    private String cdr;

    private Map<String, List<String>> roles;
    private List<AccountDTO> users;
    private Boolean utenteMultiplo;

    public AccountDTO(UtenteBulk currentUser) {
        super();
        this.roles = new HashMap<String, List<String>>();
        this.roles.put("U", Arrays.asList(ROLE_USER));
        this.roles.put("A", Arrays.asList(ROLE_USER, ROLE_SUPERUSER));
        this.roles.put("S", Arrays.asList(ROLE_USER, ROLE_ADMIN));

        this.currentUser = currentUser;
        this.username = currentUser.getCd_utente();
        this.utenteMultiplo = Boolean.FALSE;
        this.authorities = Optional.ofNullable(currentUser)
                .map(UtenteBulk::getTi_utente)
                .map(s -> roles.get(s))
                .orElse(Arrays.asList(ROLE_USER));
    }


    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    public Collection<String> getAuthorities() {
        return authorities;
    }

    @JsonProperty("authorities")
    public Collection<String> getAuthoritiesHipster() {
        return getAuthorities()
                .stream()
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public String getPassword() {
        return currentUser.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public AccountDTO changeUsernameAndAuthority(String username) {
        this.username = username;
        this.setAuthorities(
                this.users.stream()
                        .filter(accountDTO -> accountDTO.getUsername().equals(username))
                        .findAny()
                        .map(accountDTO -> accountDTO.getAuthorities())
                        .get()
        );
        this.utenteMultiplo = Boolean.TRUE;
        return this;
    }

    public boolean isAccountNonExpired() {
        return Optional.ofNullable(this.currentUser)
                .filter(utente -> !utente.getFl_autenticazione_ldap())
                .flatMap(utente -> Optional.ofNullable(utente.getDt_ultima_var_password()))
                .filter(java.sql.Date.class::isInstance)
                .map(java.sql.Date.class::cast)
                .map(java.sql.Date::toLocalDate)
                .map(localDate -> localDate.plusMonths(MONTH_EXPIRED))
                .map(localDate -> localDate.isAfter(LocalDate.now(ZoneId.systemDefault())))
                .orElse(Boolean.TRUE);
    }

    public boolean isAccountNonLocked() {
        final Optional<UtenteBulk> user = Optional.ofNullable(currentUser);
        return user
                .flatMap(utente -> Optional.ofNullable(utente.getDt_ultima_var_password()))
                .isPresent() || user.filter(UtenteBulk::getFl_autenticazione_ldap).isPresent();
    }

    public boolean isCredentialsNonExpired() {
        return !Optional.ofNullable(currentUser)
                .flatMap(utente -> Optional.ofNullable(utente.getDt_ultimo_accesso()))
                .filter(java.sql.Date.class::isInstance)
                .map(java.sql.Date.class::cast)
                .map(java.sql.Date::toLocalDate)
                .map(localDate -> localDate.plusMonths(MONTH_EXPIRED))
                .map(localDate -> localDate.isBefore(LocalDate.now(ZoneId.systemDefault())))
                .orElse(Boolean.FALSE);
    }

    public boolean isEnabled() {
        return !Optional.ofNullable(currentUser)
                .flatMap(utente -> Optional.ofNullable(utente.getDt_fine_validita()))
                .filter(java.sql.Date.class::isInstance)
                .map(java.sql.Date.class::cast)
                .map(Date::toLocalDate)
                .map(localDate -> localDate.isBefore(LocalDate.now(ZoneId.systemDefault())))
                .orElse(Boolean.FALSE);
    }

    public Long getId() {
        return 0L;
    }

    public String getLogin() {
        return Optional.ofNullable(login)
                .orElseGet(() -> currentUser.getCd_utente());
    }

    public String getFirstName() {
        return Optional.ofNullable(firstName)
                .orElseGet(() -> currentUser.getNome());
    }

    public String getLastName() {
        return Optional.ofNullable(lastName)
                .orElseGet(() -> currentUser.getCognome());
    }

    public String getEmail() {
        return Optional.ofNullable(email)
                .orElse("");
    }

    public String getLangKey() {
        return Locale.ITALIAN.getLanguage();
    }

    public Integer getEsercizio() {
        return Optional.ofNullable(esercizio)
                .orElse(null);
    }
    public String getCds() {
        return Optional.ofNullable(cds)
                .orElse(null);
    }
    public String getUo() {
        return Optional.ofNullable(uo)
                .orElse(null);
    }
    public String getCdr() {
        return Optional.ofNullable(cdr)
                .orElse(null);
    }

    public boolean isLdap() {
        return Optional.ofNullable(ldap)
                .orElse(Boolean.FALSE);
    }

    public AccountDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public AccountDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AccountDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AccountDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public AccountDTO setLdap(Boolean ldap) {
        this.ldap = ldap;
        return this;
    }

    public AccountDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public AccountDTO setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
        return this;
    }

    public AccountDTO setCds(String cds) {
        this.cds = cds;
        return this;
    }

    public AccountDTO setUo(String uo) {
        this.uo = uo;
        return this;
    }

    public AccountDTO setCdr(String cdr) {
        this.cdr = cdr;
        return this;
    }

    public List<AccountDTO> getUsers() {
        return users;
    }

    public void setUsers(List<AccountDTO> users) {
        this.users = users;
    }

    public AccountDTO users(List<AccountDTO> users) {
        this.users = users;
        return this;
    }

    public String getDsUtente() {
        return Optional.ofNullable(currentUser)
                .map(UtenteBulk::getDs_utente)
                .orElse(null);
    }

    public UtenteBulk getCurrentUser() {
        return currentUser;
    }

    public Boolean getUtenteMultiplo() {
        return utenteMultiplo;
    }

    public void setUtenteMultiplo(Boolean utenteMultiplo) {
        this.utenteMultiplo = utenteMultiplo;
    }
}
