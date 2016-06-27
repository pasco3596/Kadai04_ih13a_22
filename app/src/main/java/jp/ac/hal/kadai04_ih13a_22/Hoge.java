package jp.ac.hal.kadai04_ih13a_22;

import java.io.Serializable;

public class Hoge implements Serializable{
	private int id;
	private String name;
	private byte[] memo;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public byte[] getMemo() {
		return memo;
	}
	public void setMemo(byte[] memo) {
		this.memo = memo;
	}

}
