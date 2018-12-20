package it.cnr.contab.generator;

import it.cnr.contab.generator.artifacts.ArtifactGenerator;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.util.DatabaseUtil;

public class Generator {
    public static void main(String[] args) {
        GeneratorBean bean = new GeneratorBean();
        bean.setPackageName("it.cnr.contab.siope.plus.bulk");
        bean.setSourceFolder("/home/mspasiano/");
        bean.setTargetXMLFolder("/home/mspasiano/");
        bean.setPrefix("SIOPEPlusRisultato");

        bean.setTable("SIOPE_PLUS_RISULTATO");
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
