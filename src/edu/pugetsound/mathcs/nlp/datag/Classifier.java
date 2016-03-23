public interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	public DATag classify(Utterance u); 
	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DATag>);
}