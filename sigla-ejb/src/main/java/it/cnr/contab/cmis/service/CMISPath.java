package it.cnr.contab.cmis.service;

public class CMISPath {
	private String path;

	public CMISPath() {
		super();
	}

	public static CMISPath construct(String path){
		return new CMISPath(path);
	}
	
	public CMISPath(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public CMISPath appendToPath(String append){
		return CMISPath.construct(getPath()+ "/" + append);
	}
}
