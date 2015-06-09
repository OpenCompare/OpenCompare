
class pcm.PCM  {
    name : String[0,1]
    @contained
    products : pcm.Product[0,*]
    @contained
    features : pcm.AbstractFeature[0,*]
}

class pcm.AbstractFeature  {
    name : String[0,1]
}

class pcm.Product  {
    name : String[0,1]
    @contained
    cells : pcm.Cell[0,*]
}

class pcm.Feature : pcm.AbstractFeature {
    cells : pcm.Cell[0,*] oppositeOf feature
}

class pcm.FeatureGroup : pcm.AbstractFeature {
    @contained
    subFeatures : pcm.AbstractFeature[0,*]
}

class pcm.Cell  {
    content : String[0,1]
    rawContent : String[0,1]
    feature : pcm.Feature oppositeOf cells
    @contained
    interpretation : pcm.Value[0,1]
}

class pcm.Value  {
}

class pcm.IntegerValue : pcm.Value {
    value : Int[0,1]
}

class pcm.StringValue : pcm.Value {
    value : String[0,1]
}

class pcm.RealValue : pcm.Value {
    value : Double[0,1]
}

class pcm.BooleanValue : pcm.Value {
    value : Bool[0,1]
}

class pcm.Multiple : pcm.Value {
    @contained
    subvalues : pcm.Value[2,*]
}

class pcm.NotAvailable : pcm.Value {
}

class pcm.Conditional : pcm.Value {
    @contained
    value : pcm.Value
    @contained
    condition : pcm.Value
}

class pcm.Partial : pcm.Value {
    @contained
    value : pcm.Value[0,1]
}

class pcm.DateValue : pcm.Value {
    value : String[0,1]
}

class pcm.Version : pcm.Value {
}

class pcm.Dimension : pcm.Value {
}

class pcm.NotApplicable : pcm.Value {
}

class pcm.Unit : pcm.Value {
    unit : String[0,1]
    @contained
    value : pcm.Value
}
