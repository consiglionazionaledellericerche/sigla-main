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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.UtilService;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util00.bulk.HelpBulk;
import it.cnr.contab.web.rest.local.util.HelpLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.ejb.CRUDComponentSession;
import org.springframework.web.util.UriUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class HelpResource implements HelpLocal {
    @EJB
    CRUDComponentSession crudComponentSession;

    @Override
    public Response get(HttpServletRequest request, String jspName, String bpName) throws Exception {
        String helpBaseURL = Optional.ofNullable(System.getProperty("help.base.url"))
                                    .orElseGet(() -> {
                                        return SpringUtil.getBean(UtilService.class).getHelpBaseURL();
                                    });
        UserContext userContext = new WSUserContext("HELP", null,
                new Integer(Calendar.getInstance().get(Calendar.YEAR)),
                null, null, null);

        final Optional<HelpBulk> findByBPName = Optional.ofNullable(crudComponentSession.find(
                userContext, HelpBulk.class,
                "find", new HelpBulk().bpName(bpName)))
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .orElse(null)
                .stream().findAny();
        if (findByBPName.isPresent()) {
            helpBaseURL += findByBPName.get().getHelpUrl();
        } else {
            final Optional<HelpBulk> findByJSPName = Optional.ofNullable(crudComponentSession.find(
                    userContext, HelpBulk.class,
                    "find", new HelpBulk().page(jspName)))
                    .filter(List.class::isInstance)
                    .map(List.class::cast)
                    .orElse(null)
                    .stream().findAny();
            if (findByJSPName.isPresent()) {
                helpBaseURL += UriUtils.encode(findByJSPName.get().getHelpUrl(), Charset.defaultCharset());
            }
        }
        return Response.seeOther(new URI(helpBaseURL)).build();
    }
}
