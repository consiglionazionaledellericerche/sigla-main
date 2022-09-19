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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.utenze00.bp.CNRUserContext;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

public final class AbstractResource {

    public static final String USER_CONTEXT = "UserContext";

    public static CNRUserContext getUserContext(SecurityContext securityContext, HttpServletRequest request) throws BadRequestException {
        return Optional.ofNullable(securityContext.getUserPrincipal())
                .filter(CNRUserContext.class::isInstance)
                .map(CNRUserContext.class::cast)
                .orElseGet(() -> {
                    return Optional.ofNullable(request.getSession().getAttribute(USER_CONTEXT))
                            .filter(CNRUserContext.class::isInstance)
                            .map(CNRUserContext.class::cast)
                            .orElseThrow(() -> new BadRequestException());
                });
    }
}
