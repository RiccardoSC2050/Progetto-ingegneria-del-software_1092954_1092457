package csv_manage;

import jakarta.persistence.Lob;

public class CsvDto {

	private Integer id;

	private String fileName;

	private String ownerId;

	private byte[] data;

	public CsvDto(Integer id, String fileName, String ownerId, byte[] data) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
