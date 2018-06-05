package it.cnr.contab.util;

/**
 * Created by mspasiano on 7/5/17.
 */
public class SignP7M {
    private final String nodeRefSource;
    private final String username;
    private final String password;
    private final String otp;
    private final String nomeFile;

    public SignP7M(String nodeRefSource, String username, String password, String otp, String nomeFile) {
        this.nodeRefSource = nodeRefSource;
        this.username = username;
        this.password = password;
        this.otp = otp;
        this.nomeFile = nomeFile;
    }

    public String getNodeRefSource() {
        return nodeRefSource;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getOtp() {
        return otp;
    }

    public String getNomeFile() {
        return nomeFile;
    }

}
