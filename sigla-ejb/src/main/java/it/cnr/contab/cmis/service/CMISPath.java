package it.cnr.contab.cmis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
	
	public List<String> getNames(){
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(path, "/");
		while(st.hasMoreTokens()){
			list.add(st.nextToken());
		}
		return list;	
	}
}
