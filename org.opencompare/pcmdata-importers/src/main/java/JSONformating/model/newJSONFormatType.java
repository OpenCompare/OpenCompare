package JSONformating.model;

public enum newJSONFormatType {
	BOOLEAN("boolean"),
	REAL("real"),
	INTEGER("integer"),
	STRING("string"),
	DATE("date"),
	VERSION("version"),
	UNDEFINED("undefined"), // when the value is undefined
	MULTIPLE("multiple"),
	URL("url"),
	IMAGE("image");


	private String name = "";


	newJSONFormatType(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}
}
