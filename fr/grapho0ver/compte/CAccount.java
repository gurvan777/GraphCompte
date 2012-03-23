package fr.grapho0ver.compte;

import com.iCo6.system.Account;

public class CAccount {
	private Account acc;
	private String name;
	public CAccount(String name) {
		this.name = name;
		acc = new Account(name);
	}
	public Account getAcc() {
		return acc;
	}
	public void setAcc(Account acc) {
		this.acc = acc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}		
