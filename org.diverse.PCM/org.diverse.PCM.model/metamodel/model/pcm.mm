
class pcm.PCM  {
    name : String
    @contained
    products : pcm.Product[0,*]
    @contained
    features : pcm.AbstractFeature[0,*]
}

class pcm.AbstractFeature  {
}

class pcm.Product  {
    name : String
    @contained
    values : pcm.Cell[0,*]
}

class pcm.Feature : pcm.AbstractFeature {
    name : String
}

class pcm.FeatureGroup : pcm.AbstractFeature {
    @contained
    subfeatures : pcm.AbstractFeature
}

class pcm.Cell  {
    content : String
    feature : pcm.Feature
    @contained
    interpretation : pcm.Value
}

class pcm.Value  {
}

class pcm.IntegerValue : pcm.Value {
    value : Int
}

class pcm.StringValue : pcm.Value {
    value : String
}

class pcm.RealValue : pcm.Value {
    value : Double
}

class pcm.BooleanValue : pcm.Value {
    value : Bool
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

class pcm.Partial  {
    @contained
    value : pcm.Value
}

class pcm.DateValue : pcm.Value {
    value : String
}

class pcm.Version : pcm.Value {
}

class pcm.Dimension : pcm.Value {
}

class pcm.NotApplicable : pcm.Value {
}
