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

package it.cnr.contab.generator;

import it.cnr.contab.generator.artifacts.ArtifactGenerator;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.util.DatabaseUtil;

import java.io.File;
import java.nio.file.Files;

public class Generator {
    public static void main(String[] args) {
        GeneratorBean bean = new GeneratorBean();
        bean.setPackageName("it.cnr.contab.util.bulk");
        bean.setSourceFolder(System.getProperty("user.home").concat(File.separator));
        bean.setTargetXMLFolder(System.getProperty("user.home").concat(File.separator));
        bean.setPrefix("PhdtipoDottorati");

        bean.setTable("PHDTIPO_DOTTORATI");
        bean.setDriver("oracle.jdbc.driver.OracleDriver");
        bean.setUrl("jdbc:oracle:thin:@dbtest.cedrc.cnr.it:1521:SIGLAF");
        bean.setSchema("PCIR009");
        bean.setUser("PCIR009");
        bean.setPassword("dbform");
        ArtifactGenerator artifactGenerator = new ArtifactGenerator(bean);
        try {
            DatabaseUtil.getInstance().openConnection(bean);
            artifactGenerator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
