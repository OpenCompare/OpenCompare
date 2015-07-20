package org.opencompare.api.java;

public interface Cell extends PCMElement {

	String getContent();
	void setContent(String content);

	String getRawContent();
	void setRawContent(String content);
	
	Value getInterpretation();
	void setInterpretation(Value interpretation);

    Feature getFeature();
    void setFeature(Feature feature);

	Product getProduct();
	
}
