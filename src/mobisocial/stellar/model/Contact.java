package mobisocial.stellar.model;

public class Contact {

	private String name;
	private String address;
	
	public Contact(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return name + " - " + address;
	}

}
