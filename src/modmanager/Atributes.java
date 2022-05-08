package modmanager;

public class Atributes {

    private String modname;
    private String author;
    // 0 -> Avaliable (no installed)
    // 1 -> Installed
    // 2 -> Unavaliable
    private int status;

    public Atributes(String name, String author, int status) {
        super();
    	this.modname = name;
        this.author = author;
        this.status = status;
    }

    public String getName() {
        return modname;
    }

    public void setName(String name) {
        this.modname = name;
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
        return modname; 
    }   
}
