package com.ibm.wfm.beans;

public class UtNode {
	
	private String utlevel10;
	private String utlevel10description;
	private String utlevel15;
	private String utlevel15description;
	private String utlevel17;
	private String utlevel17description;
	private String utlevel30;
	private String utlevel30description;
	private String gbspracticecode;
	private String gbspracticedescription;
	
	public UtNode() {}
	
	public UtNode(String utlevel10, String utlevel10description, String utlevel15, String utlevel15description,
			String utlevel17, String utlevel17description, String utlevel30, String utlevel30description,
			String gbspracticecode, String gbspracticedescription) {
		super();
		this.utlevel10 = utlevel10;
		this.utlevel10description = utlevel10description;
		this.utlevel15 = utlevel15;
		this.utlevel15description = utlevel15description;
		this.utlevel17 = utlevel17;
		this.utlevel17description = utlevel17description;
		this.utlevel30 = utlevel30;
		this.utlevel30description = utlevel30description;
		this.gbspracticecode = gbspracticecode;
		this.gbspracticedescription = gbspracticedescription;
	}
	public String toCsv() {
		return toDelimitedText(",");
	}
	
	public String toDelimitedText(String delimiter) {
		StringBuffer sb = new StringBuffer();
		sb.append(utlevel10).append(delimiter)
		.append(utlevel10description).append(delimiter)
		.append(utlevel15).append(delimiter)
		.append(utlevel15description).append(delimiter)
		.append(utlevel17).append(delimiter)
		.append(utlevel17description).append(delimiter)
		.append(utlevel30).append(delimiter)
		.append(utlevel30description).append(delimiter)
		.append(gbspracticecode).append(delimiter)
		.append(gbspracticedescription)
		;
		return sb.toString();
	}

	public String getUtlevel10() {
		return utlevel10;
	}

	public void setUtlevel10(String utlevel10) {
		this.utlevel10 = utlevel10;
	}

	public String getUtlevel10description() {
		return utlevel10description;
	}

	public void setUtlevel10description(String utlevel10description) {
		this.utlevel10description = utlevel10description;
	}

	public String getUtlevel15() {
		return utlevel15;
	}

	public void setUtlevel15(String utlevel15) {
		this.utlevel15 = utlevel15;
	}

	public String getUtlevel15description() {
		return utlevel15description;
	}

	public void setUtlevel15description(String utlevel15description) {
		this.utlevel15description = utlevel15description;
	}

	public String getUtlevel17() {
		return utlevel17;
	}

	public void setUtlevel17(String utlevel17) {
		this.utlevel17 = utlevel17;
	}

	public String getUtlevel17description() {
		return utlevel17description;
	}

	public void setUtlevel17description(String utlevel17description) {
		this.utlevel17description = utlevel17description;
	}

	public String getUtlevel30() {
		return utlevel30;
	}

	public void setUtlevel30(String utlevel30) {
		this.utlevel30 = utlevel30;
	}

	public String getUtlevel30description() {
		return utlevel30description;
	}

	public void setUtlevel30description(String utlevel30description) {
		this.utlevel30description = utlevel30description;
	}

	public String getGbspracticecode() {
		return gbspracticecode;
	}

	public void setGbspracticecode(String gbspracticecode) {
		this.gbspracticecode = gbspracticecode;
	}

	public String getGbspracticedescription() {
		return gbspracticedescription;
	}

	public void setGbspracticedescription(String gbspracticedescription) {
		this.gbspracticedescription = gbspracticedescription;
	}


}
