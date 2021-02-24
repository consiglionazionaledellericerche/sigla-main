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

package it.cnr.contab.web.rest.config;

import io.swagger.annotations.*;

@SwaggerDefinition(
        info = @Info(
                title = "SIGLA REST API",
                description = "A collections of SIGLA Rest API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Marco Spasiano",
                        email = "marco.spasiano@cnr.it"
                ),
                license = @License(name = "GNU AFFERO GENERAL PUBLIC LICENSE", url = "https://www.gnu.org/licenses/agpl-3.0.html")
        ),
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        basePath = "it.cnr.contab.web.rest.local",
        securityDefinition = @SecurityDefinition(
                basicAuthDefinitions = @BasicAuthDefinition(key = "BASIC", description = "You can login with CNR official account"),
                apiKeyAuthDefinitions = {
                        @ApiKeyAuthDefinition(
                                key = SIGLASecurityContext.X_SIGLA_ESERCIZIO,
                                name = SIGLASecurityContext.X_SIGLA_ESERCIZIO,
                                description = "Esercizio in scrivania",
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER
                        ),
                        @ApiKeyAuthDefinition(
                                key = SIGLASecurityContext.X_SIGLA_CD_CDS,
                                name = SIGLASecurityContext.X_SIGLA_CD_CDS,
                                description = "CdS in scrivania",
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER),
                        @ApiKeyAuthDefinition(
                                key = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA,
                                name = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA,
                                description = "Unità Organizzativa in scrivania",
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER),
                        @ApiKeyAuthDefinition(
                                key = SIGLASecurityContext.X_SIGLA_CD_CDR,
                                name = SIGLASecurityContext.X_SIGLA_CD_CDR,
                                description = "CdR in scrivania",
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER)
                }
        )
)
@Api
public interface ApiConfig {
}
