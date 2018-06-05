package it.cnr.contab.util;

import java.util.List;

public class PdfSignApparence {
    private List<String> nodes;
    private String username, password, otp;
    private Apparence apparence;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Apparence getApparence() {
        return apparence;
    }

    public void setApparence(Apparence apparence) {
        this.apparence = apparence;
    }
}
