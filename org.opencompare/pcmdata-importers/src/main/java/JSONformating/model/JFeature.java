package JSONformating.model;

public class JFeature {
	private String id;
	private String name;
	private JSONFormatType type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JSONFormatType getType() {
		return type;
	}
	public void setType(JSONFormatType type) {
		this.type = type;
	}
	
	public String toString(){
		return id + " : " + name + ", " + type;
	}
	
	/**
	 * Compares the name and type of the 2 features
	 * @param f the feature to compare
	 * @return true if name and type are the same, omits id
	 */
	public boolean sameFeature(JFeature f){
		return this.name.equals(f.name) && this.type.equals(f.type);
	}
}
