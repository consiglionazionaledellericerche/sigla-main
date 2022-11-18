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

package it.cnr.contab.web.rest.resource.config00;

import it.cnr.contab.security.auth.SIGLALDAPPrincipal;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.web.rest.config.RESTSecurityInterceptor;
import it.cnr.contab.web.rest.exception.InvalidPasswordException;
import it.cnr.contab.web.rest.exception.UnauthorizedException;
import it.cnr.contab.web.rest.exception.UnprocessableEntityException;
import it.cnr.contab.web.rest.local.config00.AccountLocal;
import it.cnr.contab.web.rest.model.AccountDTO;
import it.cnr.contab.web.rest.model.PasswordDTO;
import it.cnr.contab.web.rest.resource.util.AbstractResource;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class AccountResource implements AccountLocal {
    public static final String FIND_UTENTE_BY_UID = "findUtenteByUID";
    private final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    private CRUDComponentSession crudComponentSession;

    public AccountDTO getAccountDTO(HttpServletRequest request) throws Exception {
        CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
        final Optional<SIGLALDAPPrincipal> siglaldapPrincipal = Optional.ofNullable(securityContext.getUserPrincipal())
                .filter(SIGLALDAPPrincipal.class::isInstance)
                .map(SIGLALDAPPrincipal.class::cast);
        final Optional<KeycloakPrincipal> keycloakPrincipal = Optional.ofNullable(securityContext.getUserPrincipal())
                .filter(KeycloakPrincipal.class::isInstance)
                .map(KeycloakPrincipal.class::cast);

        AccountDTO accountDTO = null;
        if (siglaldapPrincipal.isPresent()) {
            LOGGER.info("Try to find user: {}", securityContext.getUserPrincipal().getName());
            final List<UtenteBulk> findUtenteByUID = crudComponentSession.find(
                    userContext,
                    UtenteBulk.class,
                    FIND_UTENTE_BY_UID,
                    userContext,
                    securityContext.getUserPrincipal().getName()
            );
            final Optional<UtenteBulk> utenteBulk1 = findUtenteByUID.stream().findFirst();
            if (utenteBulk1.isPresent()) {
                accountDTO = new AccountDTO(utenteBulk1.get());
                accountDTO.setLogin(siglaldapPrincipal.get().getName());
                accountDTO.setUsers(findUtenteByUID.stream().map(utenteBulk -> new AccountDTO(utenteBulk)).collect(Collectors.toList()));
                accountDTO.setEmail((String) siglaldapPrincipal.get().getAttribute("mail"));
                accountDTO.setFirstName((String) siglaldapPrincipal.get().getAttribute("cnrnome"));
                accountDTO.setLastName((String) siglaldapPrincipal.get().getAttribute("cnrcognome"));
                accountDTO.setLdap(Boolean.TRUE);
                accountDTO.setUtenteMultiplo(findUtenteByUID.size() > 1);
            } else {
                LOGGER.warn("User {} not found!", securityContext.getUserPrincipal().getName());
                request.logout();
                throw new UnprocessableEntityException("");
            }
        } else if (keycloakPrincipal.isPresent()) {
            final IDToken idToken = Optional.ofNullable(keycloakPrincipal.get().getKeycloakSecurityContext().getIdToken())
                    .orElse(keycloakPrincipal.get().getKeycloakSecurityContext().getToken());
            final List<UtenteBulk> findUtenteByUID = crudComponentSession.find(
                    userContext,
                    UtenteBulk.class,
                    FIND_UTENTE_BY_UID,
                    userContext,
                    Optional.ofNullable(idToken.getOtherClaims())
                            .flatMap(stringObjectMap -> Optional.ofNullable(stringObjectMap.get(RESTSecurityInterceptor.USERNAME_CNR)))
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .orElse(idToken.getPreferredUsername())
            );
            final Optional<UtenteBulk> utenteBulk1 = findUtenteByUID.stream().findFirst();
            if (!utenteBulk1.isPresent()) {
                throw new UnprocessableEntityException(idToken);
            }
            accountDTO = new AccountDTO(utenteBulk1.get());
            accountDTO.setLogin(idToken.getPreferredUsername());
            accountDTO.setUsers(findUtenteByUID.stream().map(utenteBulk -> new AccountDTO(utenteBulk)).collect(Collectors.toList()));
            accountDTO.setEmail(idToken.getEmail());
            accountDTO.setFirstName(idToken.getGivenName());
            accountDTO.setLastName(idToken.getFamilyName());
            accountDTO.setLdap(Boolean.TRUE);
            accountDTO.setUtenteMultiplo(findUtenteByUID.size() > 1);
        } else {
            final UtenteBulk utenteBulk = (UtenteBulk) crudComponentSession.findByPrimaryKey(
                    userContext,
                    new UtenteBulk(securityContext
                            .getUserPrincipal()
                            .getName()
                            .toUpperCase()
                    )
            );
            accountDTO = new AccountDTO(utenteBulk);
            accountDTO.setLogin(securityContext.getUserPrincipal().getName());
            accountDTO.setUsers(Arrays.asList(utenteBulk).stream().map(utente -> new AccountDTO(utente)).collect(Collectors.toList()));
            accountDTO.setLdap(Boolean.FALSE);
            accountDTO.setUtenteMultiplo(Boolean.FALSE);
        }
        accountDTO.setEsercizio(userContext.getEsercizio());
        accountDTO.setCds(userContext.getCd_cds());
        accountDTO.setUo(userContext.getCd_unita_organizzativa());
        accountDTO.setCdr(userContext.getCd_cdr());
        return accountDTO;
    }

    @Override
    public Response get(HttpServletRequest request) throws Exception {
        try {
            if (Optional.ofNullable(securityContext.getUserPrincipal()).isPresent())
                return Response.status(Response.Status.OK).entity(getAccountDTO(request)).build();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (UnprocessableEntityException _ex) {
            return Response.status(422).entity(_ex.getEntity()).build();
        }
    }

    @Override
    public Response getUsername(HttpServletRequest request, String username) throws Exception {
        if (Optional.ofNullable(securityContext.getUserPrincipal()).isPresent()) {
            final AccountDTO accountDTO = getAccountDTO(request);
            accountDTO.changeUsernameAndAuthority(username);
            return Response.status(Response.Status.OK).entity(accountDTO).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Override
    public Response changePassword(HttpServletRequest request, PasswordDTO passwordDTO) throws Exception {
        CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
        final String newPassword = Optional.ofNullable(passwordDTO.getNewPassword())
                .filter(s -> s.length() >= 4)
                .filter(s -> s.length() < 50)
                .orElseThrow(() -> new InvalidPasswordException(passwordDTO.getNewPassword()));
        final UtenteBulk utente = (UtenteBulk) crudComponentSession.findByPrimaryKey(
                userContext,
                new UtenteBulk(securityContext
                        .getUserPrincipal()
                        .getName()
                        .toUpperCase()
                )
        );
        byte[] buser = utente.getCd_utente().getBytes();
        byte[] bpassword = newPassword.toUpperCase().getBytes();
        byte h = 0;
        for (int i = 0;i < bpassword.length;i++) {
            h = (byte)(bpassword[i] ^ h);
            for (int j = 0;j < buser.length;j++)
                bpassword[i] ^= buser[j] ^ h;
        }
        utente.setPassword( Base64Utils.encodeToString(bpassword));
        utente.setDt_ultima_var_password(EJBCommonServices.getServerTimestamp());
        utente.setToBeUpdated();
        crudComponentSession.modificaConBulk(userContext, utente);
        return Response.ok().build();
    }
}
