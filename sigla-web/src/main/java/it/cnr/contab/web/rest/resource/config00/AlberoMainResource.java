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

import it.cnr.contab.service.AccessoService;
import it.cnr.contab.service.AlberoMainService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.config00.AlberoMainLocal;
import it.cnr.contab.web.rest.resource.util.AbstractResource;
import it.cnr.jada.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

@Stateless
public class AlberoMainResource implements AlberoMainLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(AlberoMainResource.class);
    @Context
    SecurityContext securityContext;

    @Override
    public Response evictCacheTree(HttpServletRequest request) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            SpringUtil.getBean(AccessoService.class).evictCacheAccessi(userContext.getUser(), userContext.getEsercizio(), userContext.getCd_unita_organizzativa());
            return Response.status(Response.Status.OK).entity(
                    SpringUtil.getBean(AlberoMainService.class).evictCacheTree(
                            userContext.getUser(), userContext.getEsercizio(), userContext.getCd_unita_organizzativa()
                    )
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response tree(HttpServletRequest request, Integer esercizio, String uo) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            return Response.status(Response.Status.OK).entity(
                    SpringUtil.getBean(AlberoMainService.class).tree(
                            userContext,
                            userContext.getUser(),
                            Optional.ofNullable(esercizio).orElse(userContext.getEsercizio()),
                            Optional.ofNullable(uo).orElse(userContext.getCd_unita_organizzativa())
                    )
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
