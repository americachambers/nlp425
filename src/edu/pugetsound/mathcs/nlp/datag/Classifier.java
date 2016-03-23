public interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	public DialogueAct classify(Utterance u); 
	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct>);
}