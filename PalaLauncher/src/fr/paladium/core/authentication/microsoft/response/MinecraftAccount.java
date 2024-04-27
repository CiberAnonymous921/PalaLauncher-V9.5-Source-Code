package fr.paladium.core.authentication.microsoft.response;

import java.util.Objects;

public class MinecraftAccount {
	
	private String id;
	private String name;
	private String accessToken;
	private String refreshToken;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public String getRefreshToken() {
		return this.refreshToken;
	}
	
	@Override
	public String toString() {
		return "MinecraftAccount(id=" + getId() + ", name=" + getName() + ", accessToken=" + getAccessToken() + ", refreshToken=" + getRefreshToken() + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		MinecraftAccount that = (MinecraftAccount) o;
		return this.id.equals(that.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
}
