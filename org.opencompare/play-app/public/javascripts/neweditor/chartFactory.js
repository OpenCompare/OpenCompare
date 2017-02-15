//Object to generate chart
function ChartFactory(editor, div){
  var self = this;
  this.editor = editor;
  this.div = div;

  this.chart = null; //Chart object for ChartJS
  this.chartType = 'radar';
  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y

  this.chartTypeLabel = $('<label>').html('Chart : ').appendTo(this.div);
  this.chartTypeSelect = $('<select>').appendTo(this.div).change(function(){
    self.chartType = self.chartTypeSelect.val();
    self.drawChart();
  });

  this.chartTypeSelect.append('<option value="radar">Radar</option>');
  this.chartTypeSelect.append('<option value="bubble">Bubble</option>');
  this.chartTypeSelect.append('<option value="pie">Pie</option>');
  this.chartTypeSelect.append('<option value="productchart">ProductChart</option>');
  this.chartTypeSelect.append('<option value="bar">Bar</option>');
  this.chartTypeSelect.append('<option value="line">Line</option>')

  this.chartXLabel = $('<label>').html(' x : ').appendTo(this.div);
  this.chartXselect = $('<select>').appendTo(this.div).change(function(){
    self.chartDataX = self.editor.getFeatureByID(self.chartXselect.val());
    self.drawChart();
  });
  this.chartYLabel = $('<label>').html(' y : ').appendTo(this.div);
  this.chartYselect = $('<select>').appendTo(this.div).change(function(){
    self.chartDataY = self.editor.getFeatureByID(self.chartYselect.val());
    self.drawChart();
  });
  this.chartCanvas = null;
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
  if(this.chartType=='bubble'){
    this.drawBubble();
  }else if(this.chartType=='pie'){
    this.drawPie();
  }else if(this.chartType == 'radar') {
    this.drawRadar();
  }else if(this.chartType == 'productchart') {
    this.drawProductChart();
  }else if(this.chartType == 'bar') {
	this.drawBar();
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
			delay:200
        }
      }
    };

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

//Draw chart using this.chartDataX and this.chartDataY
ChartFactory.prototype.drawBubble = function(){
  if(this.chartDataX != null && this.chartDataY != null){
    if(this.chartCanvas != null){
      this.chartCanvas.remove();
    }
    this.chartCanvas = $('<canvas>').appendTo(this.div);
    this.chartData = {
      type: 'bubble',
      data: {
          datasets: []
      },
      options:{
        animation: false,
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
        }
      }
    };
    for(var p in this.editor.products){
      var product = this.editor.products[p];
      this.chartData.data.datasets.push(product.newDataset(this.editor.features[0], this.chartDataX, this.chartDataY));
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
            data: []
          }
        ]
      },
      options: {
        animation: false
      }
    };
    for(var p in this.editor.products){
      var product = this.editor.products[p];
      if(product.visible) {
        this.chartData.data.labels.push(product.getCell(this.editor.features[0]).content);
        this.chartData.data.datasets[0].data.push(parseFloat(product.getCell(this.chartDataX).content));
      }
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
      animation: false
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
      }
      /*options:{

      }*/
    };
    for(var p in this.editor.products){
      var product = this.editor.products[p];
	  var label = product.getCell(this.editor.features[0]).content;
      this.chartData.data.labels.push(label);
      this.chartData.data.datasets[0].data.push(parseFloat(product.getCell(this.chartDataX).content));
	  this.chartData.data.datasets[0].backgroundColor.push(label.toColour());
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
    setTimeout(function(){
      that.drawChart();
    },1000);
  }
}
