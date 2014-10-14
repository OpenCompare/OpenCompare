package org.diverse.pcm.api.java;

public interface Cell extends PCMElement {

	String getContent();
	void setContent(String content);
	
	Value getInterpretation();
	void setInterpretation(Value interpretation);

    Feature getFeature();
    void setFeature(Feature feature);
	
}
