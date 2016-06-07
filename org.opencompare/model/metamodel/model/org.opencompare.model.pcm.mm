
class org.opencompare.model.PCM  {
    name : String[0,1]
    @contained
    products : org.opencompare.model.Product[0,*] oppositeOf pcm
    @contained
    features : org.opencompare.model.AbstractFeature[0,*]
    productsKey : org.opencompare.model.Feature[0,1]
}

class org.opencompare.model.AbstractFeature  {
    name : String[0,1]
    parentGroup : org.opencompare.model.FeatureGroup[0,1] oppositeOf subFeatures
}

class org.opencompare.model.Product  {
    @contained
    cells : org.opencompare.model.Cell[0,*] oppositeOf product
    pcm : org.opencompare.model.PCM[0,1] oppositeOf products
}

class org.opencompare.model.Feature : org.opencompare.model.AbstractFeature {
    cells : org.opencompare.model.Cell[0,*] oppositeOf feature
}

class org.opencompare.model.FeatureGroup : org.opencompare.model.AbstractFeature {
    @contained
    subFeatures : org.opencompare.model.AbstractFeature[0,*] oppositeOf parentGroup
}

class org.opencompare.model.Cell  {
    content : String[0,1]
    rawContent : String[0,1]
    feature : org.opencompare.model.Feature oppositeOf cells
    @contained
    interpretation : org.opencompare.model.Value[0,1]
    product : org.opencompare.model.Product[0,1] oppositeOf cells
}

class org.opencompare.model.Value  {
}

class org.opencompare.model.IntegerValue : org.opencompare.model.Value {
    value : Int[0,1]
}

class org.opencompare.model.StringValue : org.opencompare.model.Value {
    value : String[0,1]
}

class org.opencompare.model.RealValue : org.opencompare.model.Value {
    value : Double[0,1]
}

class org.opencompare.model.BooleanValue : org.opencompare.model.Value {
    value : Bool[0,1]
}

class org.opencompare.model.Multiple : org.opencompare.model.Value {
    @contained
    subvalues : org.opencompare.model.Value[2,*]
}

class org.opencompare.model.NotAvailable : org.opencompare.model.Value {
}

class org.opencompare.model.Conditional : org.opencompare.model.Value {
    @contained
    value : org.opencompare.model.Value
    @contained
    condition : org.opencompare.model.Value
}

class org.opencompare.model.Partial : org.opencompare.model.Value {
    @contained
    value : org.opencompare.model.Value[0,1]
}

class org.opencompare.model.DateValue : org.opencompare.model.Value {
    value : String[0,1]
}

class org.opencompare.model.Version : org.opencompare.model.Value {
}

class org.opencompare.model.Dimension : org.opencompare.model.Value {
}

class org.opencompare.model.NotApplicable : org.opencompare.model.Value {
}

class org.opencompare.model.Unit : org.opencompare.model.Value {
    unit : String[0,1]
    @contained
    value : org.opencompare.model.Value
}
