package JSONformating.model;

public class JCell{
	private String id;
	private String productID;
	private String featureID;
	private newJSONFormatType type;
	private boolean isPartial;
	private String unit;
	private JValue value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getFeatureID() {
		return featureID;
	}
	public void setFeatureID(String featureID) {
		this.featureID = featureID;
	}
	public newJSONFormatType getType() {
		return type;
	}
	public void setType(newJSONFormatType type) {
		this.type = type;
	}
	public boolean isPartial() {
		return isPartial;
	}
	public void setPartial(boolean isPartial) {
		this.isPartial = isPartial;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public JValue getValue() {
		return value;
	}
	public void setValue(JValue value) {
		this.value = value;
	}
}