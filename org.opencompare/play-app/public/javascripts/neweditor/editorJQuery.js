//********************************************************************************************************************************************************************
//Editor

/**
 * The jQuery Editor class
 * @param {string} divID - The id attribute of the tag that will xontain the editor.
 * @param {string} pcmID - The id of the PCM toloadin the API.
 */
function Editor (divID, pcmID) {
  var that = this
  var self = this
  this.api = '/api/get/'
  this.div = $('#' + divID).addClass('editor')
  this.pcmID = pcmID
  this.loadPCM()

  this.pcm = false
  this.metadata = false

  this.views = {}
  this._view = null

  //Create header
  this.headerShow = true
  this.header = $("<div>").addClass("editor-header").appendTo(this.div)
  this.name = $("<div>").addClass("pcm-name").html("No pcm loaded").appendTo(this.header)
  this.licenseDiv = $("<div>").addClass("pcm-param").html("<b>License : </b>").appendTo(this.header)
  this.license = $("<span>").appendTo(this.licenseDiv)
  this.sourceDiv = $("<div>").addClass("pcm-param").html("<b>Source : </b>").appendTo(this.header)
  this.source = $("<span>").appendTo(this.sourceDiv)

  //Create action bar
  this.actionBar = $("<div>").addClass("editor-action-bar").appendTo(this.div)
  this.showConfiguratorButton = $("<div>").addClass("button").click(function () {
    self.showConfigurator()
  }).appendTo(this.actionBar)
  this.configuratorArrow = $("<div>").addClass("configurator-arrow").appendTo(this.showConfiguratorButton)
  this.showConfiguratorButton.append(" ")
  this.showConfiguratorButtonMessage = $("<span>").html("Hide configurator").appendTo(this.showConfiguratorButton)

  this.showPCMButton = $('<div>').addClass('button').html('Show pcm').click(function () {
    self.showView('pcmDiv')
  }).appendTo(this.actionBar)

  this.showChartButton = $('<div>').addClass('button').html('Show chart').click(function () {
    self.showView('chartDiv')
  }).appendTo(this.actionBar)

  this.exportButton = $('<a>').addClass('button').html('Download').attr('href', this.api + this.pcmID).attr('download', this.pcmID + '.json').appendTo(this.actionBar)

  //Action bar right pane
  this.actionBarRightPane = $("<div>").addClass("editor-action-bar-right-pane").appendTo(this.actionBar)
  this.showHeaderButton = $("<div>").addClass("button").click(function () {
    self.showHeader()
  }).html('<i class="material-icons">keyboard_arrow_up</i>').appendTo(this.actionBarRightPane)

  //Create content
  this.content = $("<div>").addClass("editor-content").appendTo(this.div)

  //Create configurator
  this.configuratorShow = true
  this.configurator = $("<div>").addClass("configurator").appendTo(this.content)

  //Create pcm wrap
  this.pcmWrap = $("<div>").addClass("pcm-wrap").appendTo(this.content)

  //Create pcmDiv
  this.views.pcmDiv = $("<div>").addClass("pcm-table").appendTo(this.pcmWrap)

  //Create chart
  this.views.chartDiv = $("<div>").appendTo(this.pcmWrap)
  this.chartFactory = new ChartFactory(this, this.views.chartDiv)

  this.showView('pcmDiv')
}

Object.defineProperty(Editor.prototype, 'checkInterpretation', {
  get: function(){
    var n = 0
    var max = 0
    for(var p in this.products){
      for(var c in this.products[p].cells.array){
        max++
        if(this.products[p].cells.array[c].interpretation != null){
          n++
        }
      }
    }
    return 'interpretation : ' + n + '/' + max + ' ' + ((n == max) ? 'OK' : 'incomplete')
  }
})

//Show view
Editor.prototype.showView = function (view) {
  if (typeof view === 'undefined') {
    view = this.views.pcmDiv
  } else if (typeof view === 'string') {
    view = this.views[view]
  }

  if (this._view !== view) {
    if (this._view != null) {
      this._view.hide()
    }

    this._view = view
    this._view.show()
  }
}

//Some accessors
Editor.prototype.getFeatureByName = function (name) {
  var feature = false
  for (var f in this.features) {
    if (this.features[f].name == name) {
      feature = this.features[f]
      break
    }
  }
  return feature
}

//get feature by generated_KMF_ID
Editor.prototype.getFeatureByID = function (id) {
  var feature = false;
  for (var f in this.features) {
    if (this.features[f].generated_KMF_ID === id) {
      feature = this.features[f]
      break
    }
  }
  return feature
}

//Load the pcm
Editor.prototype.loadPCM = function (pcmID) {
  pcmID = typeof pcmID === 'undefined'
    ? false
    : pcmID
  var that = this
  if (pcmID) this.pcmID = pcmID

  //API url : https://opencompare.org/api/get/
  // or relative, local path "/get/"
  // I propose to change pcmID as pcmLocation; as such the user can specify an opencompare ID or a local file
  // https://opencompare.org/api/get/
  // "/assets/pcm/"
  // works also with a local opencompare server ()"/api/get/")
  $.get(this.api + this.pcmID, function (data) {

    that.metadata = data.metadata; //Get metadata
    that.pcm = mypcmApi.loadPCMModelFromString(JSON.stringify(data.pcm)) //Load PCM
    mypcmApi.decodePCM(that.pcm); //Decode the PCM with KMF, require pcmApi


    //Extract products
    that.products = [];
    for (var p in that.pcm.products.array) {
      var product = that.pcm.products.array[p];
      product.visible = true
      product.dataset = null //dataset for chart

      //Iterate on each cells and add property
      product.cellsByFeature = {}
      for(var c in product.cells.array){
        var cell = product.cells.array[c]
        cell._type = null
        /**
         * return the type of the content.
         * @return {string} undefined, integer, float, image, url, string
         */
        Object.defineProperty(cell, 'type', {
          get: function () {
            if (this._type == null) {
              if (this.content.length === 0 || (this.interpretation != null && this.interpretation.metaClassName() === 'org.opencompare.model.NotAvailable')) {
                this._type = 'undefined'
              } else if (/^(\d+|\d{1,3}(\,\d{3})*|\d{1,3}(\ \d{3})*)$/.test(this.content)) {
                this._type = 'integer'
                this.content = parseInt(this.content.replace(/[^\d]+/g, ''), 10)
              } else if (/^\d+\.\d+$/.test(this.content)) {
                this._type = 'float'
                this.content = parseFloat(this.content)
              } else if (/^.+\.(jpg|jpeg|JPG|JPEG|gif|png|bmp|ico|svg)$/.test(this.content)) {
                this._type = 'image'
              } else if (/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w?=\.-]*)*\/?$/.test(this.content)) {
                this._type = 'url'
              } else {
                this._type = 'string'
              }
            }
            return this._type
          }
        })
        /**
         * return if the type of the content id numeric (integer/float).
         * @return {boolean}
         */
        Object.defineProperty(cell, 'isNumber', {
          get: function () {
            return this.type === 'integer' || this.type === 'float'
          }
        })

        product.cellsByFeature[cell.feature.generated_KMF_ID] = cell
        var htmlContent = cell.content
        if (cell.type === 'image') {
          htmlContent = "<a target='_blank' href='" + cell.content + "'><img class='cell-img' src='" + cell.content + "'></a>"
        } else if (cell.type === 'url') {
          htmlContent = "<a target='_blank' href='" + cell.content + "'>" + cell.content + "</a>"
        }
        cell.div = $('<div>').addClass('pcm-cell').html(htmlContent).click({cell: cell}, function (event) {
          console.log(event.data.cell.content)
          console.log(event.data.cell.interpretation.metaClassName())
          console.log(event.data.cell.interpretation)
        })
        cell.div.cell = cell
        cell.match = true
      }

      /**
       * Return the cell for the specified feature
       * @param {undefined|number|Feature} feature - If undefined return cell for feature at index 0 in editor.features, or at the specified index is feature is a number.
       * @return {Cell} The cell corresponding to the feature.
       */
      product.getCell = function (feature) {
        if (typeof feature === 'undefined') {
          feature = that.features[0]
        } else if (typeof feature === 'number' || typeof feature === 'string') {
          feature = that.features[feature]
        }
        var cell = this.cellsByFeature[feature.generated_KMF_ID];
        if(typeof cell == 'undefined'){
          cell = false;
        }
        return cell;
      }

      //Add a function that return if all cell.match==true
      product.match = function(){
        var match = true;
        for(var c in this.cells.array){
          if(this.cells.array[c].match==false){
            match = false;
            break;
          }
        }
        return match;
      }

      //Add a function that hide/show cells (used to hide products that doesn't match configurator)
      product.setVisible = function(visible){
        this.visible = visible;
        for(var c in this.cells.array){
          if(visible){
            this.cells.array[c].div.removeClass("hidden");
          }else{
            this.cells.array[c].div.addClass("hidden");
          }
        }
        if(this.dataset != null){
          this.dataset.hidden = !this.visible;
        }
      }

      product.newDataset = function(n, x, y){
        var self = this;
        this.dataset = {
           label: self.getCell(n).content,
           hidden: !self.visible,
           data: [{
             x: self.getCell(x).content,
             y: self.getCell(y).content,
             r: 20
           }]
        };
        return this.dataset
      }

      that.products.push(product)
    }

    //Extract features
    that.features = []
    that.addFeaturesFromArray(that.pcm.features.array)

    that.pcmLoaded()
  })
}

//Add all feature in the array to this.features
Editor.prototype.addFeaturesFromArray = function(array){
  for(var i in array){
    var feature = array[i];
    if (feature.subFeatures) {
      this.addFeaturesFromArray(feature.subFeatures.array);
    } else {
      feature.filter = new Filter(feature, this); //filter is used to filter products on this feature

      /**
       * Just a shorthand to feature.filter.type
       */
      Object.defineProperty(feature, 'type', {
        get: function () {
          return this.filter.type
        }
      })

      /**
       * Return if feature.filter.type is a number (integer/float)
       */
      Object.defineProperty(feature, 'isNumber', {
        get: function () {
          return this.type === 'integer' || this.type === 'float'
        }
      })

      if (this.pcm.productsKey != null && this.pcm.productsKey.generated_KMF_ID == feature.generated_KMF_ID) {
        this.features.splice(0, 0, feature);
      } else {
        this.features.push(feature);
      }
    }
  }
}

//Called when the pcm is loaded to update the UI
Editor.prototype.pcmLoaded = function(){
  //console.log(this.pcm);

  //Name
  var name = this.pcm.name
  if (typeof name === 'undefined' || name.length === 0) {
    name = 'No name'
  }
  this.name.html(name)

  //License
  var license = this.metadata.license
  if (typeof license === 'undefined' || license.length === 0) {
    license = 'unknown'
  }
  this.license.html(license)

  //Source
  var source = this.metadata.source;
  if(source.length==0){
    source = "unknown";
  }else{
    source = "<a href='"+source+"' target='_blank'>"+source+"</a>";
  }
  this.source.html(source);

  //Init configurator
  this.initConfigurator();

  //Sort products on first feature (display inside by calling Editor.initPCM())
  this.features[0].filter.setSorting(ASCENDING_SORTING);

  //init the chart
  this.initChart();
}

//Called in pcmLoaded to update the pcm
Editor.prototype.initPCM = function(){
  //init table
  this.views.pcmDiv.find(".pcm-column-header").detach();
  this.views.pcmDiv.find(".pcm-cell").detach();
  this.views.pcmDiv.empty();
  for(var f in this.features){
    var col = $("<div>").addClass("pcm-column").addClass(this.features[f].filter.type).appendTo(this.views.pcmDiv);
    col.append(this.features[f].filter.columnHeader);
    for(var p in this.products){
      col.append(this.products[p].getCell(this.features[f]).div);
    }
  }
}

//init chart
Editor.prototype.initChart = function(){
  this.chartFactory.init();
}

//Called in pcmLoaded to update the configurator
Editor.prototype.initConfigurator = function(){
  this.configurator.empty();
  for(var f in this.features){
    this.configurator.append(this.features[f].filter.div);
  }
}

//Hide or show the configurator
Editor.prototype.showConfigurator = function(){
  this.configuratorShow = !this.configuratorShow;

  if(this.configuratorShow){
    this.configurator.removeClass("hidden");
    this.pcmWrap.removeClass("full-width");
    this.configuratorArrow.removeClass("right");
    this.showConfiguratorButtonMessage.html("Hide configurator");
  }else{
    this.configurator.addClass("hidden");
    this.pcmWrap.addClass("full-width");
    this.configuratorArrow.addClass("right");
    this.showConfiguratorButtonMessage.html("Show configurator");
  }
}

//Hide or show the header
Editor.prototype.showHeader = function(){
  this.headerShow = !this.headerShow;

  if(this.headerShow){
    this.header.removeClass("hidden");
    this.content.removeClass("full-height");
    this.showHeaderButton.html('<i class="material-icons">keyboard_arrow_up</i>');
  }else{
    this.header.addClass("hidden");
    this.content.addClass("full-height");
    this.showHeaderButton.html('<i class="material-icons">keyboard_arrow_down</i>');
  }
}

//Called when a filter changed
Editor.prototype.filterChanged = function(filter){
  //console.log("Filter changed for feature : "+filter.feature.name);
  for(var p in this.products){
    var product = this.products[p]; // get the product
    // chech if the product match all filters (product.match() is not evaluated if filter.match(product.getCell(filter.feature))==false, it's better for perf)
    product.setVisible(filter.match(product.getCell(filter.feature)) && product.match());
  }

  //Update chart
  this.chartFactory.update();
}

//Sort products on the feature using quicksort
Editor.prototype.sortProducts = function(feature=false){
  if(!feature){
    feature = this.features[0];
  }

  //Sort products using quicksort
  //console.time("quicksortProducts");
  this.quicksortProducts(feature);
  //console.timeEnd("quicksortProducts");

  //Update pcm
  //console.time("initPCM");
  editor.initPCM();
  //console.timeEnd("initPCM");
}

//sort products on feature f
Editor.prototype.quicksortProducts = function(f){
  var stack = [];
  stack.push(0);
  stack.push(this.products.length-1);
  while(stack.length>0){
    var h = stack.pop();
    var l = stack.pop();
    var p = this.partitionProducts(l, h, f);

    if(p-1>l){
      stack.push(l);
      stack.push(p-1);
    }

    if(p+1<h){
      stack.push(p+1);
      stack.push(h);
    }
  }
}

Editor.prototype.partitionProducts = function(l, h, f){
  var pivot = this.products[h];
  var i = l;
  for(var j=l;j<h;j++){
    if(f.filter.compare(this.products[j], pivot)<=0){
      var temp = this.products[i];
      this.products[i] = this.products[j];
      this.products[j] = temp;
      i++;
    }
  }
  var temp = this.products[i];
  this.products[i] = this.products[h];
  this.products[h] = temp;
  return i;
}

//********************************************************************************************************************************************************************
//Filter
var NO_SORTING = 1;
var ASCENDING_SORTING = 2;
var DESCENDING_SORTING = 3;

/**
 * Filter object used to filter products on a feature.
 * @param {Feature} feature - The feature for the filter.
 * @param {Editor} editor - The editor.
 */
function Filter(feature, editor){
  var that = this;
  this.feature = feature;
  this.editor = editor;
  this.values = []; //Contains all different values for this feature
  this.hasCheckbox = false; //Set at true is checkbox are used
  this.checkboxs = {}; //For each value associate a checkbox that say if the value match the filter
  this.min = false; //Minimum value in all values
  this.max = false; //Maximum value in all values
  this.lower = false; //Minimum value which match filter
  this.upper = false; //Maximum value which match filter
  this.step = 1; //Step for the slider when feature is a numeric value
  this.type = 'undefined'; //Type of the values : integer, float, string
  this.search = ''; //Will contain a regexp entered by the user in a search form
  this.sorting = NO_SORTING;

  //Determine type of feature
  this.types = {
    undefined: 0,
    integer: 0,
    float: 0,
    image: 0,
    string: 0
  }

  for(var p in this.editor.products){
    var cell = this.editor.products[p].getCell(feature);

    if(cell.content){
      if ($.inArray(cell.content, this.values) === -1) {
        this.values.push(cell.content);
      }

      this.types[cell.type]++
    }
  }

  if (this.types.integer > 0 && this.types.float === 0 && this.types.string === 0) { //Integer
    this.type = "integer";

    for(var v in this.values){
      var value = parseInt(this.values[v], 10);
      if(!this.min && !this.max){
        this.min = value;
        this.max = value;
      }else if(value<this.min){
        this.min = value;
      }else if(value>this.max){
        this.max = value;
      }
    }
    this.lower = this.min;
    this.upper = this.max;
    this.step = 1;
  } else if(this.types.float > 0 && this.types.string === 0) { //Float
    this.type = "float";

    for(var v in this.values){
      var value = parseFloat(this.values[v]);
      if(!this.min && !this.max){
        this.min = value;
        this.max = value;
      }else if(value<this.min){
        this.min = value;
      }else if(value>this.max){
        this.max = value;
      }
    }
    this.lower = this.min;
    this.upper = this.max;
    this.step = 0.1;
  } else { //String
    this.type = "string";

    this.values.sort();

    if(this.values.length <= 20){ //Create checkboxs only if there are les than 20 differents values
      this.hasCheckbox = true;
      for(var v in this.values){
        var value = this.values[v];
        this.checkboxs[value] = new Checkbox(value, function(){
          that.editor.filterChanged(that);
        });
      }
    }
  }

  //Create div for column header
  this.columnHeader = $("<div>").addClass("pcm-column-header").click(function(event){
    that.swapSorting();
    event.stopImmediatePropagation();
  }).html(this.feature.name);

  //Create div for configurator
  this.show = false;
  this.div = $("<div>").addClass("feature");

  this.button = $("<div>").addClass("feature-button").click(function(){
    that.toggleShow();
  }).appendTo(this.div);
  this.arrow = $("<div>").addClass("feature-arrow").appendTo(this.button);
  this.button.append(" " + this.feature.name);

  this.contentWrap = $("<div>").addClass("feature-content-wrap").css("height", 0).appendTo(this.div);

  this.content = $("<div>").addClass("feature-content").appendTo(this.contentWrap);

  if(this.values.length==1 || (this.type=="integer" || this.type=="float") && this.min==this.max){ //If there is only one value
    this.content.append(this.values[0]);
  }else if(this.type=="integer" || this.type=="float"){ //If type is a number
    //Create the slider
    this.slider = new Slider(this.min, this.max, this.lower, this.upper, this.step, function(slider){
      that.lower = slider.lower;
      that.upper = slider.upper;
      that.editor.filterChanged(that);
    });

    //Add the slider
    this.content.append(this.slider.div);
  }else{ //Else, type is a string with multiple values
    //Create and add the search input
    this.searchInput = $("<input>").addClass("search-input").attr("placeholder", "Search").keyup(function(){
      if(that.searchInput.val()!=that.search){
        that.search = that.searchInput.val();
        that.editor.filterChanged(that);
      }
    }).appendTo(this.content);

    if(this.hasCheckbox){
      this.buttonSelectUnselectAll = $("<div>").addClass("button").click(function(){
        that.selectUnselectAll();
      }).html("Select/Unselect all").appendTo(this.content);

      //Add all checkbox
      for(var c in this.checkboxs){
        this.content.append(this.checkboxs[c].div);
      }
    }
  }
}

//Check if all value are matched
Filter.prototype.matchAll = function(){
  var res = true;
  if(this.type=="integer" || this.type=="float"){
    res = (this.lower==this.min && this.upper==this.max);
  }else if(this.search.length>0){
    res = false;
  }else if(this.hasCheckbox){
    for(var c in this.checkboxs){
      if(this.checkboxs[c].notChecked()){
        res = false;
        break;
      }
    }
  }
  return res;
}

//Check if the cell match this filter
Filter.prototype.match = function(cell){
  var match = this.matchAll();

  if(!match){
    if (this.type === 'integer' || this.type === 'float') {
      match = cell.content >= this.lower && cell.content <= this.upper
    } else if(this.type=="string"){
      if(this.search.length>0){ //If there is a search regexp we use it and not the checkboxs
        var regexp = new RegExp(this.search, 'i'); //Create a regexp with this.search that isn't case-sensitive
        match = ('' + cell.content).match(regexp) != null;
      }else{ //Else we use checkboxs
        if(typeof this.checkboxs[cell.content] != "undefined"){
          match = this.checkboxs[cell.content].isChecked();
        }
      }
    }
  }

  cell.match = match; //Set the cell.match attribute, it's used to check if all cell match them respective filter
  return cell.match;
}

//Select/Unselect all checkboxs
Filter.prototype.selectUnselectAll = function(){
  this.search = "";

  var select = true;

  for(var c in this.checkboxs){
    if(this.checkboxs[c].notChecked()){
      select = false;
      break;
    }
  }

  for(var c in this.checkboxs){
    this.checkboxs[c].setChecked(!select, false);
  }

  this.editor.filterChanged(this);
}

/**
 * Scroll to the column's feature in pcm view
 */
Filter.prototype.scrollTo = function () {
  var left = this.editor.views.pcmDiv.scrollLeft() + this.columnHeader.parent().position().left
  this.editor.views.pcmDiv.animate({scrollLeft: left}, 200)
}

//Hide/Show the filter form (checkboxs, input, slider, ...)
Filter.prototype.toggleShow = function(){
  this.show = !this.show;
  if (this.show) {
    this.contentWrap.css("height", this.content.outerHeight()+"px")
    this.arrow.addClass("bottom")
    this.scrollTo()
  } else {
    this.contentWrap.css("height", 0)
    this.arrow.removeClass("bottom")
  }
}

//Change sorting
Filter.prototype.swapSorting = function(){
  //console.log("Swap sorting for feature : "+this.feature.name);
  if(this.sorting==ASCENDING_SORTING){
    this.setSorting(DESCENDING_SORTING);
  }else{
    this.setSorting(ASCENDING_SORTING);
  }
}

Filter.prototype.setSorting = function(sorting, autoSort=true, resetOther=true){
  //Reset all other filter
  if(resetOther){
    for(var f in this.editor.features){
      this.editor.features[f].filter.setSorting(NO_SORTING, false, false);
    }
  }

  //remove old class
  if(this.sorting==ASCENDING_SORTING){
    this.columnHeader.removeClass("ascending");
  }else if(this.sorting==DESCENDING_SORTING){
    this.columnHeader.removeClass("descending");
  }

  //set new value
  this.sorting = sorting;

  //add new class
  if(this.sorting==ASCENDING_SORTING){
    this.columnHeader.addClass("ascending");
  }else if(this.sorting==DESCENDING_SORTING){
    this.columnHeader.addClass("descending");
  }

  //sort
  if(autoSort){
    this.editor.sortProducts(this.feature);
  }
}

//Compare
Filter.prototype.compare = function(p1, p2){
  var res = 0;
  if(this.sorting === NO_SORTING){
    console.log("Try to compare 2 product using a filter without sorting direction");
  }else{
    var val1 = p1.getCell(this.feature).content
    var val2 = p2.getCell(this.feature).content
    if (val1 > val2) {
      res = 1;
    } else if (val1 < val2) {
      res = -1;
    }
  }

  if(this.sorting==DESCENDING_SORTING){
    res = res * -1;
  }

  return res;
}


//********************************************************************************************************************************************************************
//Checkbox
function Checkbox(name, onChange=false, checked=true){
  var that = this;
  this.onChange = onChange;
  this.div = $("<div>").addClass("checkbox");
  this.checkbox = $("<input type='checkbox'>").prop('checked', checked).change(function(){
    that.triggerOnChange()
  }).appendTo(this.div);
  this.name = name;
  this.label = $("<label>").addClass("checkbox-label").html(this.name).click(function(){
    that.setChecked();
  }).appendTo(this.div);
}

Checkbox.prototype.setChecked = function(checked, trigger=true){
  if(typeof checked == "undefined"){
    checked = !this.isChecked();
  }
  this.checkbox.prop('checked', checked);

  if(trigger){
    this.triggerOnChange();
  }
}

Checkbox.prototype.isChecked = function(){
  return this.checkbox.is(":checked");
}

Checkbox.prototype.notChecked = function(){
  return !this.isChecked();
}

Checkbox.prototype.triggerOnChange = function(){
  if(this.onChange){
    this.onChange(this);
  }
}

//********************************************************************************************************************************************************************
//Slider
function Slider(min, max, lower, upper, step, onChange=false){
  var that = this;
  this.min = min;
  this.max = max;
  if(this.max<this.min){
    var temp = this.min;
    this.min = this.max;
    this.max = temp;
  }
  this.lower = lower;
  if(this.lower<this.min){
    this.lower = this.min
  }
  if(this.lower>this.max){
    this.lower = this.max
  }
  this.upper = upper;
  if(this.upper>this.max){
    this.upper = this.max
  }
  if(this.upper<this.lower){
    this.upper = this.lower
  }
  this.step = step;
  this.onChange = onChange;
  this.lowerHandled = false;
  this.upperHandled = false;

  $(document).mouseup(function(){
    that.lowerHandled = false;
    that.lowerDiv.removeClass("active");
    that.upperHandled = false;
    that.upperDiv.removeClass("active");
  }).mousemove(function(event){
    that.mousemove(event);
  });
  this.div = $("<div>").addClass("slider");
  this.lowerInput = $("<input>").val(this.lower).keyup(function(){
    that.setLower(parseFloat(that.lowerInput.val()), false);
  }).appendTo(this.div);
  this.range = $("<div>").addClass("slider-range").appendTo(this.div);
  this.lowerDiv = $("<div>").addClass("slider-thumb").css("left", (this.getLowerRatio()*100)+"%").mousedown(function(){
    that.lowerHandled = true;
    that.lowerDiv.addClass("active");
  }).appendTo(this.range);
  this.upperDiv = $("<div>").addClass("slider-thumb").css("left", (this.getUpperRatio()*100)+"%").mousedown(function(){
    that.upperHandled = true;
    that.upperDiv.addClass("active");
  }).appendTo(this.range);
  this.upperInput = $("<input>").val(this.upper).keyup(function(){
    that.setUpper(parseFloat(that.upperInput.val()), false);
  }).appendTo(this.div);
}

Slider.prototype.getLowerRatio = function(){
  return (this.lower-this.min)/(this.max-this.min);
}

//lower is the value to set lower, correct is if we can correct the value if out of bound (if the false value is rejected)
Slider.prototype.setLower = function(lower, correct=true){
  if(!isNaN(lower)){
    lower -= lower%this.step;
    if(lower<this.min){
      if(correct){
        lower = this.min;
      }else{
        return false;
      }
    }
    if(lower>this.max){
      if(correct){
        lower = this.max;
      }else{
        return false;
      }
    }
    if(lower>this.upper){
      if(correct){
        this.setUpper(lower, correct);
      }else{
        return false;
      }
    }
    this.lower = lower;
    this.lowerInput.val(this.lower);
    this.lowerDiv.css("left", (this.getLowerRatio()*100)+"%");
    this.triggerOnChange();
    return true;
  }
  return false;
}

//upper is the value to set upper, correct is if we can correct the value if out of bound (if the false value is rejected)
Slider.prototype.setUpper = function(upper, correct=true){
  if(!isNaN(upper)){
    upper -= upper%this.step;
    if(upper<this.min){
      if(correct){
        upper = this.min;
      }else{
        return false;
      }
    }
    if(upper<this.lower){
      if(correct){
        this.setLower(upper, correct);
      }else{
        return false;
      }
    }
    if(upper>this.max){
      if(correct){
        upper = this.max;
      }else{
        return false;
      }
    }
    this.upper = upper;
    this.upperInput.val(this.upper);
    this.upperDiv.css("left", (this.getUpperRatio()*100)+"%");
    this.triggerOnChange();
    return true;
  }
  return false;
}

Slider.prototype.getUpperRatio = function(){
  return (this.upper-this.min)/(this.max-this.min);
}

Slider.prototype.mousemove = function(event){
  if(this.lowerHandled){
    this.setLower(((event.pageX-this.range.offset().left)/this.range.width())*(this.max-this.min)+this.min);
  }else if(this.upperHandled){
    this.setUpper(((event.pageX-this.range.offset().left)/this.range.width())*(this.max-this.min)+this.min);
  }
}

Slider.prototype.triggerOnChange = function(){
  if(this.onChange){
    this.onChange(this);
  }
}
