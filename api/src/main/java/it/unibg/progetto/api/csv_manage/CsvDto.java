package it.unibg.progetto.api.csv_manage;

import jakarta.persistence.Lob;

public class CsvDto {

	private String fileName;

	private String ownerId;

	private byte[] data;

	public CsvDto(String fileName, String ownerId, byte[] data) {
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
