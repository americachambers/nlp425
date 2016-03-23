public interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	public DialogAct classify(Utterance u); 
	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogAct>);
}