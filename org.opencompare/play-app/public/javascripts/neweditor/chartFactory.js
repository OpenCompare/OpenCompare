//Object to generate chart
function ChartFactory(editor, div){
  var self = this;
  this.editor = editor;
  this.div = div;

  this.chart = null; //Chart object for ChartJS
  this.chartType = 'radar';
  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y

  this.chartTypeLabel = $('<label>').html('&nbsp;Chart&nbsp;:&nbsp;').appendTo(this.div);
  this.chartTypeSelect = $('<select>').appendTo(this.div).change(function(){
    self.chartType = self.chartTypeSelect.val();
    self.drawChart();
  });

  this.chartTypeSelect.append('<option value="radar">Radar</option>');
  this.chartTypeSelect.append('<option value="pie">Pie</option>');
  this.chartTypeSelect.append('<option value="productchart">ProductChart</option>');
  this.chartTypeSelect.append('<option value="bar">Bar</option>');
  this.chartTypeSelect.append('<option value="line">Line</option>')

  this.chartXLabel = $('<label>').html('&nbsp;x&nbsp:&nbsp').appendTo(this.div);
  this.chartXselect = $('<select>').appendTo(this.div).change(function(){
    self.chartDataX = self.editor.getFeatureByID(self.chartXselect.val());
    self.drawChart();
  });
  this.chartYLabel = $('<label>').html('&nbsp;y&nbsp:&nbsp').appendTo(this.div);
  this.chartYselect = $('<select>').appendTo(this.div).change(function(){
    self.chartDataY = self.editor.getFeatureByID(self.chartYselect.val());
    self.drawChart();
  });
  this.chartCanvas = null;

  this.timeout = null;

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
      this.chartXselect.append('<option value="'+feature.generated_KMF_ID+'">'+feature.name+'</option>');
      this.chartYselect.append('<option value="'+feature.generated_KMF_ID+'">'+feature.name+'</option>');
    }
  }
  this.drawChart();
}

ChartFactory.prototype.drawChart = function(){
  if(this.chartType=='pie'){
    this.chartXLabel.html('&nbsp;Value&nbsp:&nbsp');
    this.chartXLabel.show();
    this.chartXselect.show();
    this.chartYselect.hide();
    this.chartYLabel.hide();
    this.drawPie();
  }else if(this.chartType == 'radar') {
    this.chartYselect.hide();
    this.chartYLabel.hide();
    this.chartXselect.hide();
    this.chartXLabel.hide();
    this.drawRadar();
  }else if(this.chartType == 'productchart') {
    this.chartYselect.show();
    this.chartYLabel.show();
    this.chartYLabel.html('&nbsp;y&nbsp:&nbsp');
    this.chartXselect.show();
    this.chartXLabel.show();
    this.chartXLabel.html('&nbsp;x&nbsp:&nbsp');
    this.drawProductChart();
  }else if(this.chartType == 'bar') {
    this.chartXLabel.html('&nbsp;y&nbsp:&nbsp');
    this.chartXLabel.show();
    this.chartXselect.show();
    this.chartYselect.hide();
    this.chartYLabel.hide();
    this.drawBar();
  }else if(this.chartType == 'line'){
    this.chartXLabel.html('&nbsp;y&nbsp:&nbsp');
    this.chartXLabel.show();
    this.chartXselect.show();
    this.chartYselect.hide();
    this.chartYLabel.hide();
	  this.drawLine();
  }else{
    console.error('Unsupported chart type : '+this.chartType);
  }
}

function newProductChartDataset(product,feature,x,y,imageUrl){
	var dataset = product.newDataset(feature,x,y);
	dataset.data[0].cropCircle=true;
	dataset.data[0].strokeCircle=true;
	dataset.data[0].image=imageUrl;
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
      type: 'bubbleImage',
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
			imageUrl
		)
	  );
    }
	// console.log(this.chartData);
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

  var feat = this.chartDataX;

    // create two arrays
	var arr = [0];
	var arr2 = [0];

  var num = 0;

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

    this.chart = new Chart(this.chartCanvas[0], this.chartData);
  }else{
    console.error('Value undefined');
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
    }
    this.chartData.data.labels = labels;
    var label = product.getCell(this.editor.features[0]).content;
    this.chartData.data.datasets.push({label: label, borderColor:label.toColour(), data: data});
  }
  if(this.chartData.data.datasets.length > 10){
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

	// create 4 arrays
	var arr = [0];
	var arr2 = [0];
	var arr3 = [0];
	var arr4 = [];

	// for each product
    for(var p in this.editor.products){

	  var product = this.editor.products[p];
	  // we see if the product is visible
	  if(product.visible) {

		// we recover the value of the product and parse in int
		var label = product.getCell(this.editor.features[0]).content;
		var value = parseFloat(product.getCell(this.chartDataX).content);

		// push only if the value is numerical value
		if (!isNaN(value)){
			this.chartData.data.datasets[0].data.push(value);

			// we create a map, in the first array is the values
			// and the labels is in the second array with the same index
			arr.push(parseFloat(product.getCell(this.chartDataX).content));
			arr2.push(label);
			if (this.charDataX != this.chartDataY){
				arr3.push(parseFloat(product.getCell(this.chartDataY).content));
			}

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

		// if chartDataY is different of chartDataX
		if (this.chartDataX != this.chartDataY){
			arr4.push(arr3[p]);
		}

		i++;
	}

	// we add the second array to the datasets with same  array of color
	if (this.chartDataX != this.chartDataY){
		this.chartData.data.datasets.push({backgroundColor:this.chartData.data.datasets[0].backgroundColor,data:arr4});
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
		for(var p in this.editor.products){
			var product = this.editor.products[p];
			var label = product.getCell(this.editor.features[0]).content;
			this.chartData.data.labels.push(label);
			this.chartData.data.datasets[0].data.push(parseFloat(product.getCell(this.chartDataX).content));
		}
		this.chart = new Chart(this.chartCanvas[0], this.chartData);
	}else{
		console.log('Value undefined');
	}
}

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
