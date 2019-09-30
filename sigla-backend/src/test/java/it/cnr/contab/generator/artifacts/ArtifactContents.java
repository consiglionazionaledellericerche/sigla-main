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

package it.cnr.contab.generator.artifacts;

import java.util.List;

/**
 * Provvede i metodi di generazione e di accesso al contenuto generato
 *
 * @author Marco Spasiano
 * @version 1.0
 * [21-Aug-2006] creazione
 * [22-Aug-2006] aggiunto contratto generate(List columns)
 */
public interface ArtifactContents {

    public String getContents();

    public void generate(List columns) throws Exception;
}
