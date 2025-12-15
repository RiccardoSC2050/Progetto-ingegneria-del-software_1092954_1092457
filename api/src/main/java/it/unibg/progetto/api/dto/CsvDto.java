package it.unibg.progetto.api.dto;



public class CsvDto {

	private int id;
	private String fileName;

	private String ownerId;

	private byte[] data;

	public CsvDto() {

	}

	public CsvDto(int id, String fileName, String ownerId, byte[] data) {
		this.id = id;
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}

	public CsvDto(String fileName, String ownerId, byte[] data) {
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
