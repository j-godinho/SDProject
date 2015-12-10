package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Answer implements Serializable {
	private String question;
	private ArrayList<String> answers;

	Answer() {
	}

	Answer(String question, ArrayList<String> answers) {
		setQuestion(question);
		setAnswers(answers);

	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
}
