//Object to generate chart
function ChartFactory(editor, div){

  this.maxLengthLabel = 40;
  this.maxLegendDisplay = 20;

  var self = this;
  this.editor = editor;
  this.div = div;

  this.taboption = [];
  this.taboptionNonNumerique = [];
  this.taboption2 = [];

  this.tabSelect = [];

  this.chart = null; //Chart object for ChartJS
  this.chartType = 'productchart'; // this is the type of current vizualisation
  this.cartTypeA = 'radar'; // this is the type of last vizualisation
  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y
  this.chartDataRadius = null; //feature for radius
  this.chartDataColor = null; //feature for color

  // the image of the Visualization in Bar, and the fonction to call it
  this.chartImgBar = $('<img id="clickBar" src="http://img4.hostingpics.net/pics/227950stat11.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "bar";
    self.drawChart();
  });

  // the image of the Visualization in Pie, and the fonction to call it
  this.chartImgPie = $('<img id="clickPie" src="http://img4.hostingpics.net/pics/212411stat21.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "pie";
    self.drawChart();
  });

  // the image of the Visualization in Line, and the fonction to call it
  this.chartImgLine = $('<img id="clickLine" src="http://img4.hostingpics.net/pics/199610stat31.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "line";
    self.drawChart();
  });

  // the image of the Visualization in Radar, and the fonction to call it
  this.chartImgRadar = $('<img id="clickRadar" src="http://img4.hostingpics.net/pics/915174stat41.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "radar";
    self.drawChart();
  });

  // the image of the Visualization in ProductChart, and the fonction to call it
  this.chartImgPC = $('<img id="clickPC" src="http://img4.hostingpics.net/pics/557345stat51.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "productchart";
    self.drawChart();
  });

  // the image of the Visualization in Other, and the fonction to call it
  this.chartImgPC = $('<img id="clickPC" src="http://img4.hostingpics.net/pics/415060stat61.png" width="40px" />').appendTo(this.div).click(function(){
    self.chartType = "autre";
    self.drawChart();
  });

  this.div.append("</br></br>");

  this.listSelect = $('<div>',{id:'listSelect'}).appendTo(this.div);

  this.chartCanvas = null;

  this.timeout = null;

  this.nbBar = 1; // the number of choice for the vizualisation in Bar
  this.nbLine = 1; // the number of choice for the vizualisation in Line
  this.compAutre =false; // the boolean to know if the constraint is activated for the vizualisation Other

}

//Called when pcm is loaded to init chart
ChartFactory.prototype.init = function(){
  for(var f in this.editor.features){
    var feature = this.editor.features[f];
    if(feature.filter.type == 'integer' || feature.filter.type == 'float'){
      if(this.chartDataX == null){
        this.chartDataX = feature;
      }else if(this.chartDataY == null){
        this.chartDataY = feature;
      }
      this.taboption.push(feature);
    }
	else {
		this.taboptionNonNumerique.push(feature);
	}
	this.taboption2.push(feature);
  }
  this.drawChart();
}

ChartFactory.prototype.drawChart = function(){

  var self=this;

  if(this.chartType=='pie'){

	// if the last type of vizualisation is yet pie, we do anything
    if(this.chartTypeA!="pie") {

		// we remove all select of the listSelect
		this.listSelect.html("");

		// we create the select for the vizualisation in line
		this.chartXLabel = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartXselect = $('<select id="x" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#x option:selected').val();
			self.chartDataX = self.taboption2[a];
			self.drawChart();
		});

		// we put all choice in the select we have created
		for(var i in this.taboption2) {
			this.chartXselect.append('<option value="'+i+'">'+this.taboption2[i].name+'</option>');
		}

		// we take the first option to begin
		this.chartDataX = this.taboption2[0];
	}

	// the last type become pie
	this.chartTypeA = "pie";
	// we draw pie
    this.drawPie();
  }
  else if(this.chartType == 'radar') {

	// we remove all select of the listSelect
	this.listSelect.html("");
	// the last type become radar
	this.chartTypeA = "radar";
	// we draw radar
    this.drawRadar();
  }
  else if(this.chartType == 'productchart') {

	// if the last type of vizualisation is yet pc, we do anything
	if(this.chartTypeA!="productchart") {

		// we remove all select of the listSelect
		this.listSelect.html("");

		// we create the select X for the vizualisation in pc
		this.chartXLabel = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartXselect = $('<select id="x" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#x option:selected').val();
			self.chartDataX = self.taboption[a];
			self.drawChart();
		});

		// we create the select Y for the vizualisation in pc
		this.chartYLabel = $('<label>').html('&nbsp;y&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartYselect = $('<select id="y" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#y option:selected').val();
			self.chartDataY = self.taboption[a];
			self.drawChart();
		});

		// we create the select Radius for the vizualisation in pc
		this.chartRadiusLabel = $('<label>').html('&nbsp;radius&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartRadiusselect = $('<select id="r" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#r option:selected').val();
			self.chartDataRadius = self.taboption[a];
			self.drawChart();
		});

		// we create the select Y for the vizualisation in pc
		this.chartColorLabel = $('<label>').html('&nbsp;color&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartColorselect = $('<select id="c" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#c option:selected').val();
			self.chartDataColor = self.taboption[a];
			self.drawChart();
		});

		// we put all choice in the select we have created
		for(var i in this.taboption) {
			this.chartXselect.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
			this.chartYselect.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
			this.chartRadiusselect.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
			this.chartColorselect.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
		}

		// we take the first option to begin
		this.chartDataX = this.taboption[0];
		this.chartDataY = this.taboption[0];
		this.chartDataRadius = this.taboption[0];
		this.chartDataColor = this.taboption[0];
	}

	// the last type become productChart and we draw productChart
	this.chartTypeA = "productchart";
    this.drawProductChart();
  }
  else if(this.chartType == 'bar') {

	// if the last type of vizualisation is yet bar, we do anything
    if(this.chartTypeA!="bar"){

		// we recover the number of choice for the Bar
		var nb = this.nbBar;
		// we create a array with all select
		this.tabSelect = [];

		// we remove all select of the listSelect
		this.listSelect.html("");

		// we create nb select for the vizualisation in Bar
		for (var j=1 ; j<=nb ; j++){
			var cl = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.listSelect);
			var cs = $('<select id="choix'+j+'" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
				self.drawChart();
			});

			for(var i in this.taboption) {
				cs.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
			}

			this.tabSelect.push(cs);
		}

		// we create a picture plus for add a new choice for the vizualisation
		var d = $('<img src="http://img4.hostingpics.net/pics/755501plus.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(self.nbBar < self.taboption.length){
				self.nbBar = self.nbBar + 1;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

		// we create a picture minus for sub a new choice for the vizualisation
		var b = $('<img src="http://img4.hostingpics.net/pics/836557moins.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(self.nbBar > 1){
				self.nbBar = self.nbBar - 1;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

	}

	// the last type become bar and we draw the vizualisation Bar
	this.chartTypeA = "bar";
    this.drawBar();
  }
  else if(this.chartType == 'line'){

	// if the last type of vizualisation is yet Line, we do anything
    if(this.chartTypeA!="line"){

		// we recover the number of choice for the Line
		var nb = this.nbLine;
		// we create a array with all select
		this.tabSelect = [];

		// we remove all select of the listSelect
		this.listSelect.html("");

		// we create nb select for the vizualisation in Line
		for (var j=1 ; j<=nb ; j++){
			var cl = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.listSelect);
			var cs = $('<select id="choix'+j+'" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
				self.drawChart();
			});

			for(var i in this.taboption) {
				cs.append('<option value="'+i+'">'+this.taboption[i].name+'</option>');
			}

			this.tabSelect.push(cs);
		}

		// we create a picture plus for add a new choice for the vizualisation
		var d = $('<img src="http://img4.hostingpics.net/pics/755501plus.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(self.nbLine < self.taboption.length){
				self.nbLine = self.nbLine + 1;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

		// we create a picture minus for sub a new choice for the vizualisation
		var b = $('<img src="http://img4.hostingpics.net/pics/836557moins.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(self.nbLine > 1){
				self.nbLine = self.nbLine - 1;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

	}

	// the last type become Line and we draw the vizualisation Line
	this.chartTypeA = "line";
	this.drawLine();
  }
  else if(this.chartType=='autre'){

	// if the last type of vizualisation is yet Autre, we do anything
    if(this.chartTypeA!="autre") {

		// we remove all select of the listSelect
		this.listSelect.html("");

		// we create the select X for the vizualisation Autre
		this.chartXLabel = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.listSelect);
		this.chartXselect = $('<select id="x" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
			var a = $('#x option:selected').val();
			self.chartDataX = self.taboptionNonNumerique[a];
			self.drawChart();
		});

		// we put all non-digital choice in the select we have created
		for(var i in this.taboptionNonNumerique) {
			this.chartXselect.append('<option value="'+i+'">'+this.taboptionNonNumerique[i].name+'</option>');
		}

		// if the constraint is activated, we create the select Y for choose the constraint in the non-digital option
		if(this.compAutre){
			this.chartYLabel = $('<label>').html('&nbsp;y&nbsp:&nbsp').appendTo(this.listSelect);
			this.chartYselect = $('<select id="y" class=\"styled-select blue semi-square\">').appendTo(this.listSelect).change(function(){
				var a = $('#y option:selected').val();
				self.chartDataY = self.taboptionNonNumerique[a];
				self.drawChart();
			});

			// we put all non-digital choice in the select we have created
			for(var i in this.taboptionNonNumerique) {
				this.chartYselect.append('<option value="'+i+'">'+this.taboptionNonNumerique[i].name+'</option>');
			}
		}

		// we create a picture plus for add the constraint to the vizualisation
		var d = $('<img src="http://img4.hostingpics.net/pics/755501plus.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(!self.compAutre){
				self.compAutre = true;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

		// we create a picture plus for remove the constraint to the vizualisation
		var b = $('<img src="http://img4.hostingpics.net/pics/836557moins.png" width="20px" />').appendTo(this.listSelect).click(function(){
			if(self.compAutre){
				self.compAutre = false;
				self.chartTypeA = null;
				self.drawChart();
			}
		});

		// we take the first non-digital option to begin
		this.chartDataX = this.taboptionNonNumerique[0];
	}

	// the last type become Autre and we draw the vizualisation Autre
	this.chartTypeA = "autre";
    this.drawAutre();
  }
  else{
    console.error('Unsupported chart type : '+this.chartType);
  }
}

function newProductChartDataset(product,feature,x,y,r,c,imageUrl){
	console.log(x)
	var dataset = product.newDataset(feature,x,y,r,c);
	dataset.data[0].image=imageUrl;
	// console.log(dataset);
	return dataset;
}

//Draw chart using this.chartDataX and this.chartDataY
ChartFactory.prototype.drawProductChart = function(){
  if(this.chartDataX != null && this.chartDataY != null){
    if(this.chartCanvas != null){
      this.chartCanvas.remove();
    }
    this.chartCanvas = $('<canvas>').appendTo(this.div);
    this.chartData = {
      type: 'imageBubbleChart',
      data: {
          datasets: []
      },
      options:{
        animation: false,
        legend: {
          display: true
        },
        scales: {
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString: this.chartDataX.name
            }
          }],
          yAxes: [{
            scaleLabel: {
              display: true,
              labelString: this.chartDataY.name
            }
          }]
        },
		tooltips: {
			displayColors:false,
			displayImages:true
        }
      }
    };
    if(this.editor.products.length > 10){
      this.chartData.options.legend.display = false;
    }
	var imageCol;
	for(var j in this.editor.features)
		if(this.editor.features[j].filter.types.image > 0)
			imageCol = parseInt(j)


    for(var p in this.editor.products){
      var product = this.editor.products[p];
	    var imageUrl="";

		imageUrl = product.getCell(imageCol).content;
      this.chartData.data.datasets.push(
		newProductChartDataset(
			product,
			this.editor.features[0],
			this.chartDataX,
			this.chartDataY,
			this.chartDataRadius,
			this.chartDataColor,
			imageUrl
		)
	  );
    }
    this.chart = new Chart(this.chartCanvas[0], this.chartData);
  }else{
    console.error('X or Y features not defined');
  }
}

ChartFactory.prototype.drawPie = function(){
  if(this.chartDataX != null){
    if(this.chartCanvas != null){
      this.chartCanvas.remove();
    }
    this.chartCanvas = $('<canvas>').appendTo(this.div);
    this.chartData = {
      type: 'pie',
      data: {
        labels: [],
        datasets: [
          {
            backgroundColor: [],
            data: [],
            label: []
          }
        ]
      },
      options: {
        animation: false,
        legend: {
          display: true
        }
      }
    };

	if(this.taboption.includes(this.chartDataX)){
		this.pieNumeric();
	}
	else{
		this.pieNonNumeric();
	}

	if(this.chartData.data.datasets[0].data.length > this.maxLegendDisplay){
		this.chartData.options.legend.display = false;
	}

    this.chart = new Chart(this.chartCanvas[0], this.chartData);
  }else{
    console.error('Value undefined');
  }
}

ChartFactory.prototype.pieNumeric = function (){

	var feat = this.chartDataX;

    // create two arrays
	var arr = [0];
	var arr2 = [0];

	// for each product
    for(var p in this.editor.products){

	  var product = this.editor.products[p];
	  // we see if the product is visible
	  if(product.visible) {

		// we recover the value of the product and parse in int
		var label = product.getCell(this.editor.features[0]).content;
		var value = parseFloat(product.getCell(feat).content);

		// push only if the value is numerical value
		if (!isNaN(value)){
			this.chartData.data.datasets[0].data.push(value);

			// we create a map, in the first array is the values
			// and the labels is in the second array with the same index
			arr.push(parseFloat(product.getCell(feat).content));

			// we cut the label if its length is too big
			if (label.length > this.maxLengthLabel){
				label = label.substring(this.maxLengthLabel);
			}
			arr2.push(label);
		}

	  }
    }

	// we sort directly the array of number
	this.chartData.data.datasets[0].data=this.chartData.data.datasets[0].data.sort((a,b)=>a-b);

	var i = 0;
	while (i < this.chartData.data.datasets[0].data.length) {

		// we recover the value of the first case in the array data
		var nb = this.chartData.data.datasets[0].data[i];
		// we recover the index in the first array with the value
		var p = arr.indexOf(nb);
		// thanks the index, we recover the label associate to the value in the second array
		arr[p] = null;
		// we push the label in the array of labels
		var label = arr2[p];
		this.chartData.data.labels.push(label);
		// we add a color thanks the label
		this.chartData.data.datasets[0].backgroundColor.push(label.toColour());

		i++;
	}

}

ChartFactory.prototype.pieNonNumeric = function (){

	// boolean who serve to
	var bool = true;

	// first array of label
	var tabLabel = [];
	// second array of number which represent values of label
	var tabNombre = [];

	// we recover 2 features
	var feat = this.chartDataX;

	if(bool){
		for(var p in this.editor.products){

			var product = this.editor.products[p];
			// we see if the product is visible
			if(product.visible) {

				// we see the values in the first array and search the index in the array label to obtain the label
				var prod = product.getCell(feat).content;
				var index = tabLabel.indexOf(prod);

				// the work if the constraint is not activated
				if(index == -1){
					// if the label is not yet in the array
					tabLabel.push(prod);
					tabNombre.push(1);
				}
				else{
					// if the label is in the table
					tabNombre[index] = tabNombre[index] + 1;
				}
			}
		}


		// we fill the canvas if the constraint is not activated
		for(var lab in tabLabel){

			// we recover the label of the first element and push into the canvas
			var label = tabLabel[lab];
			this.chartData.data.labels.push(label);

			// with the index lab, we recover the values and push into the canvas with the color of the label
			var nb = tabNombre[lab];
			this.chartData.data.datasets[0].data.push(nb);
			this.chartData.data.datasets[0].backgroundColor.push(label.toColour());
		}
		// we add the name of label in the canvas
		// we cut the label if its length is too big
		var label = feat.name;
		if (label.length > this.maxLengthLabel){
			label = label.substring(this.maxLengthLabel);
		}
		this.chartData.data.datasets[0].label = label;
	}
	else{
		console.error("too much constraint");
	}
}


ChartFactory.prototype.drawRadar = function(){
  if(this.chartCanvas != null){
    this.chartCanvas.remove();
  }
  this.chartCanvas = $('<canvas>').appendTo(this.div);

  this.chartData = {
    type: 'radar',
    data: {
      labels: [],
      datasets: []
    },
    options: {
      animation: false,
      legend: {
        display: true
      }
    }
  };
  var labels = [];
  for(var f in this.editor.features) {
    var feature = this.editor.features[f]
    if(feature.isNumber){
      labels.push(feature.name);
    }
  }
  for(var p in this.editor.products){
    var product = this.editor.products[p];
    var data = [];
    if(product.visible) {
      for (var f in this.editor.features){
        var feature = this.editor.features[f];
        if(feature.isNumber){
          var cellValue = parseFloat(product.getCell(this.editor.features[f]).content);
          data.push(cellValue / feature.filter.max);
        }
      }
			var label = product.getCell(this.editor.features[0]).content;
			this.chartData.data.datasets.push({label: label, borderColor:label.toColour(), data: data});
    }
    this.chartData.data.labels = labels;
  }

	if(this.chartData.data.datasets.length > this.maxLegendDisplay){
		this.chartData.options.legend.display = false;
	}

  this.chart = new Chart(this.chartCanvas[0], this.chartData);
}




ChartFactory.prototype.drawBar = function(){

  if(this.chartDataX != null){
    if(this.chartCanvas != null){
      this.chartCanvas.remove();
    }
    this.chartCanvas = $('<canvas>').appendTo(this.div);
    this.chartData = {
      type: 'bar',
      data: {
        labels: [],
        datasets: [
          {
			backgroundColor: [],
            data: [],
          }
        ]
      },
      options:{
		scales: {
			yAxes: [{
				scaleLabel: {
					display: true,
					labelString: this.chartDataX.name
				}
			}]
		}
	  }
    };

	var nb_choix = this.nbBar;

	var tabSup = [];

	// création de la map
	tabSup.push(new Array());
	tabSup.push(new Array());

	for (var j=2 ; j<= nb_choix ; j++){

		tabSup.push(new Array());
		this.chartData.data.datasets.push({backgroundColor:new Array(),data:new Array()});
	}

	var fea = $('#choix1 option:selected').val();

	var c1 = this.taboption[fea];

	// for each product
    for(var p in this.editor.products){

	  var product = this.editor.products[p];
	  // we see if the product is visible
	  if(product.visible) {

		// we recover the value of the product and parse in int
		var label = product.getCell(this.editor.features[0]).content;
		var value = parseFloat(product.getCell(c1).content);

		// push only if the value is numerical value
		if (!isNaN(value)){
			this.chartData.data.datasets[0].data.push(value);

			// we create a map, in the first array is the values
			// and the labels is in the second array with the same index
			tabSup[0].push(parseFloat(product.getCell(c1).content));
			tabSup[1].push(label);

			for (var j=2 ; j <= nb_choix ; j++){

				var fea2 = $('#choix'+j+' option:selected').val();
				var c2 = this.taboption[fea2];

				// we add the name in the legend
				this.chartData.data.datasets[j-1].label = c2.name;

				tabSup[j].push(parseFloat(product.getCell(c2).content));
			}

		}

	  }
    }

	// we sort directly the array of number
	this.chartData.data.datasets[0].data=this.chartData.data.datasets[0].data.sort((a,b)=>a-b);

	// we add the name of the first feature in the legend
	this.chartData.data.datasets[0].label = this.taboption[fea].name;

	var i = 0;
	while (i < this.chartData.data.datasets[0].data.length) {

		// we recover the value of the first case in the array data
		var nb = this.chartData.data.datasets[0].data[i];
		// we recover the index in the first array with the value
		var p = tabSup[0].indexOf(nb);
		// thanks the index, we recover the label associate to the value in the second array
		tabSup[0][p] = null;
		// we push the label in the array of labels
		var label = tabSup[1][p];
		this.chartData.data.labels.push(label);
		// we add a color thanks the label
		this.chartData.data.datasets[0].backgroundColor.push(label.toColour());

		for (var j=2 ; j<= nb_choix ; j++){

			this.chartData.data.datasets[j-1].backgroundColor.push(label.toColour());
			this.chartData.data.datasets[j-1].data.push(tabSup[j][p]);
		}

		i++;
	}

    this.chart = new Chart(this.chartCanvas[0], this.chartData);

  }else{
    console.log('Value undefined');
  }
}

ChartFactory.prototype.drawLine = function() {
	if(this.chartDataX != null){
		if(this.chartCanvas != null){
			this.chartCanvas.remove();
		}
		this.chartCanvas = $('<canvas>').appendTo(this.div);
		this.chartData = {
			type: 'line',
			data: {
				labels: [],
				datasets: [
				{
					label:String,
					data: [],
					borderColor:String
				}
				]
			},
			options:{
				scales: {
					yAxes: [{
						scaleLabel: {
						display: true,
						labelString: this.chartDataX.name
						}
					}]
				}
			}
		};

		// we recover the number of choice we have
		var nb = this.nbLine;

		// for each choice
		for(var j=1 ; j<=nb ; j++){

			// we recover the index feature selected in the choice i, and find the feature in the taboption
			var fea = $('#choix'+j+' option:selected').val();
			var c1 = this.taboption[fea];
			// a array
			var tmp = [];

			// for each product
			for(var p in this.editor.products){
				var product = this.editor.products[p];
				var label = product.getCell(this.editor.features[0]).content;
				// in the first turn of the for, we push labels in the canvas
				if (j==1){
					this.chartData.data.labels.push(label);
				}
				// we push the value j in the array tmp
				tmp.push(parseFloat(product.getCell(c1).content));
			}

			// we push the array tmp in the canvas, with a backgroundcolor
			if(j==1){
				this.chartData.data.datasets[0] = {borderColor:c1.name.toColour(),data:tmp,label:c1.name};
			}
			else {
				this.chartData.data.datasets.push({borderColor:c1.name.toColour(),data:tmp,label:c1.name});
			}

			// we remove all data of the array to start again with the next choice
			tmp = [];

		}

		this.chart = new Chart(this.chartCanvas[0], this.chartData);
	}else{
		console.log('Value undefined');
	}
}

// -----------------------------------------------------------------------------------------------------------------
// en cours de test
// -----------------------------------------------------------------------------------------------------------------

ChartFactory.prototype.drawAutre = function(){

  if(this.chartDataX != null){
    if(this.chartCanvas != null){
      this.chartCanvas.remove();
    }
    this.chartCanvas = $('<canvas>').appendTo(this.div);
    this.chartData = {
      type: 'bar',
      data: {
        labels: [],
        datasets: [
          {
			label:String,
			backgroundColor: [],
            data: [],
          }
        ]
      },
      options:{
		scales: {
			yAxes: [{
				scaleLabel: {
					display: true,
					labelString: this.chartDataX.name
				}
			}]
		}
	  }
    };

	// boolean who serve to
	var bool = true;

	// first array of label
	var tabLabel = [];
	// second array of number which represent values of label
	var tabNombre = [];
	// third array who represent label constraint
	var tabConstraint = [];

	// we recover 2 features
	var feat = this.chartDataX;
	var feat2 = this.chartDataY;

	// if constraint are activated
	if (this.compAutre){
		for(var p in this.editor.products){

			// we create the array of constraint with label
			var product = this.editor.products[p];
			var prod = product.getCell(feat2).content;
			var index = tabConstraint.indexOf(prod);
			if(index == -1){
				tabConstraint.push(prod);
			}
		}
	}

	// if the array of constraint is more than 10, we do anything
	if(tabConstraint.length > 10){
		bool = false;
	}


	if(bool){
		for(var p in this.editor.products){

			var product = this.editor.products[p];
			// we see if the product is visible
			if(product.visible) {

				// we see the values in the first array and search the index in the array label to obtain the label
				var prod = product.getCell(feat).content;
				var index = tabLabel.indexOf(prod);

				if(!this.compAutre){
					// the work if the constraint is not activated
					if(index == -1){
						// if the label is not yet in the array
						tabLabel.push(prod);
						tabNombre.push(1);
					}
					else{
						// if the label is in the table
						tabNombre[index] = tabNombre[index] + 1;
					}
				}
				else {
					// the work if the constraint is activated
					var prod2 = product.getCell(feat2).content;
					var j = tabConstraint.indexOf(prod2);
					if(index == -1){
						tabLabel.push(prod);
						tabNombre.push(new Array());
						var index = tabLabel.indexOf(prod);
						for(var i=1 ; i<=tabConstraint.length ; i++){
							tabNombre[index].push(0);
						}
						tabNombre[index][j] = tabNombre[index][j] + 1;
					}
					else {
						var index = tabLabel.indexOf(prod);
						tabNombre[index][j] = tabNombre[index][j] + 1;
					}
				}
			}
		}


		if(!this.compAutre){
			// we fill the canvas if the constraint is not activated
			for(var lab in tabLabel){

				// we recover the label of the first element and push into the canvas
				var label = tabLabel[lab];
				this.chartData.data.labels.push(label);

				// with the index lab, we recover the values and push into the canvas with the color of the label
				var nb = tabNombre[lab];
				this.chartData.data.datasets[0].data.push(nb);
				this.chartData.data.datasets[0].backgroundColor.push(label.toColour());
			}
			// we add the name of label in the canvas
			this.chartData.data.datasets[0].label = feat.name;
		}else {
			// we fill the canvas if the constraint is activated
			// for each values in the constraint, we create a new object in the array
			for(var i=2 ; i<=tabConstraint.length ; i++){
				this.chartData.data.datasets.push({backgroundColor:new Array(),data:new Array(),label:tabConstraint[i-1]});
			}
			// we recover the value of the constraint and put in the canvas
			this.chartData.data.datasets[0].label = tabConstraint[0];
			for(var lab in tabLabel){

				// we recover the label of the first element and push into the canvas
				var label = tabLabel[lab];
				this.chartData.data.labels.push(label);

				// for each value in tabConstraint, we recover the value affiliate and push in the canvas
				for(var i=1 ; i<= tabConstraint.length ; i++){

					var nb = tabNombre[lab][i-1];

					this.chartData.data.datasets[i-1].data.push(nb);
					this.chartData.data.datasets[i-1].backgroundColor.push(tabConstraint[i-1].toColour());
				}
			}
		}
	}
	else{
		console.error("too much constraint");
	}

    this.chart = new Chart(this.chartCanvas[0], this.chartData);

  }else{
    console.log('Value undefined');
  }
}

// -----------------------------------------------------------------------------------------------------------------
// fin de la partie test
// -----------------------------------------------------------------------------------------------------------------

/**
 * Return a color based on a string
 * @return {string} A color in hexadecimal.
 */
String.prototype.toColour = function () {
   var hash = 0;
   for (var i = 0; i < this.length; i++) {
     hash = this.charCodeAt(i) + ((hash << 5) - hash);
   }
   var colour = '#';
   for (var i = 0; i < 3; i++) {
     var value = (hash >> (i * 8)) & 0xFF;
     colour += ('00' + value.toString(16)).substr(-2);
   }
   return colour;
 }

 /**
  * Return a color based on a number using String.toColour()
  * @return {string} A color in hexadecimal.
  */
 Number.prototype.toColour = function () {
   return ('' + this).toColour()
 }

//Update chart when configurator change
ChartFactory.prototype.update = function(){
  if(this.chart != null){
    var that = this;
    if(this.timeout != null) {
      clearTimeout(this.timeout);
    }
    this.timeout = setTimeout(function(){
      that.drawChart();
    },500);
  }
}
