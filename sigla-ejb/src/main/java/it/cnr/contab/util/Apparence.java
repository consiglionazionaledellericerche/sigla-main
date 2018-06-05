package it.cnr.contab.util;

public class Apparence {
    private String image, location, reason, testo;
    private Integer leftx, lefty, page, rightx, righty;

    public Apparence(String image, String location, String reason,
                     String testo, Integer leftx, Integer lefty, Integer page,
                     Integer rightx, Integer righty) {
        super();
        this.image = image;
        this.location = location;
        this.reason = reason;
        this.testo = testo;
        this.leftx = leftx;
        this.lefty = lefty;
        this.page = page;
        this.rightx = rightx;
        this.righty = righty;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getTesto() {
        return testo;
    }
    public void setTesto(String testo) {
        this.testo = testo;
    }
    public Integer getLeftx() {
        return leftx;
    }
    public void setLeftx(Integer leftx) {
        this.leftx = leftx;
    }
    public Integer getLefty() {
        return lefty;
    }
    public void setLefty(Integer lefty) {
        this.lefty = lefty;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getRightx() {
        return rightx;
    }
    public void setRightx(Integer rightx) {
        this.rightx = rightx;
    }
    public Integer getRighty() {
        return righty;
    }
    public void setRighty(Integer righty) {
        this.righty = righty;
    }
}
