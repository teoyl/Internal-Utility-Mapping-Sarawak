package com.sains.common.util;

import java.io.Serializable;

public class Options implements Serializable{
	public Options(){} //added default constructor
	public Options(String keyData, String valueData){
		this.keyData = keyData;
		this.valueData = valueData;
	}
	private String keyData;
	private String valueData;
	
	public Integer getKeyData_int() {
		return Integer.parseInt(keyData);
	}
	public String getKeyData() {
		return keyData;
	}
	public void setKeyData(String keyData) {
		this.keyData = keyData;
	}
	public String getValueData() {
		return valueData;
	}
	public void setValueData(String valueData) {
		this.valueData = valueData;
	}
        
        @Override
        public boolean equals(Object obj) {
        if (!(obj instanceof Options)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        return ((Options)obj).getKeyData().equals(this.getKeyData());
    }
}