

public class Signal {
	

	private boolean status;
	private int type;
	private int clients;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	//Constructors
	public Signal(boolean s){
		this.status = s;
	}
	
	//Methods
	public void setStatus(boolean s){
		this.status = s;
	}
	
	public boolean getStatus(){
		return this.status;
	}
	
	
}
