package modmanager;

public class SorrMod {

	private String name;
	private String author;
	// 0 -> Avaliable (no installed)
	// 1 -> Installed
	// 2 -> Unavaliable
	private int status;

	public SorrMod(String name, String author, int status) {
		super();
		this.name = name;
		this.author = author;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String code) {
		this.author = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int code) {
		this.status = code;
	}

	@Override
	public String toString() {
		return name;
	}
}
