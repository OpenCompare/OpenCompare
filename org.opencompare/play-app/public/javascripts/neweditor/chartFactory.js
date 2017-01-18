//Object to generate chart
function ChartFactory(editor, div){
  var self = this;
  this.editor = editor;
  this.div = div;

  this.chart = null; //Chart object for ChartJS
  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y
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

//Draw chart using this.chartDataX and this.chartDataY
ChartFactory.prototype.drawChart = function(){
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
    console.log('X or Y features not defined');
  }
}

//Update chart when configurator change
ChartFactory.prototype.update = function(){
  if(this.chart != null){
    this.chart.update();
  }
}
