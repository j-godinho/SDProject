package common;

public class Message {
	private int id;
	private String sender;
	private String receiver;
	private int projectID;
	private String text;

	Message(int id, String sender, String receiver, int projectID, String text) {
		setId(id);
		setSender(sender);
		setReceiver(receiver);
		setProjectID(projectID);
		setText(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
