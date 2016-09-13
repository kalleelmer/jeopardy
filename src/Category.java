public class Category {
	private Question[] questions;
	private String title;

	public Category(String title, Question[] questions) {
		this.title = title;
		this.questions = questions;
	}

	public String getTitle() {
		return title;
	}
	
	public Question getQuestion(int index){
		return questions[index];
	}
}
