package fr.grapho0ver.compte;

import org.bukkit.Location;

public class SignCompte {
	private CAccount acc;
	private Location location;
	public SignCompte(CAccount acc, Location location) {
		this.acc = acc;
		this.location = location;
		GraphCompte.signComptes.add(this);
	}
	public CAccount getAcc() {
		return acc;
	}
	public void setAcc(CAccount acc) {
		this.acc = acc;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
}
