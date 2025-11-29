package it.unibg.progetto.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "File_CSV")
public class Csv {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String fileName;

	private String ownerId;

	@Lob // huge
	private byte[] data;

	public Csv() {
	}

	public Csv(String fileName, String ownerId, byte[] data) {
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}

	public Csv(Integer id, String fileName, String ownerId, byte[] data) {
		this.id = id;
		this.fileName = fileName;
		this.ownerId = ownerId;
		this.data = data;
	}

	// getter and setter

	public Integer getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public byte[] getData() {
		return data;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
