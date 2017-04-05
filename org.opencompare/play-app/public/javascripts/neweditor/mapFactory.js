//Object to generate chart

  function MapFactory(editor, div){
  var self = this;
  this.editor = editor;
  this.div = div;

  this.chartType = 'radar';
  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y
  this.map = null;


  this.chartXLabel = $('<label>').html(' x : ').appendTo(this.div);
  this.chartXselect = $('<select>').appendTo(this.div).change(function(){
    self.chartDataX = self.editor.getFeatureByID(self.chartXselect.val());
    console.log(self.chartDataX)
    this.mapTypeLabel = $('<div id="map">').addClass('map').appendTo(this.mapTypeLabel);
    if(self.map !== null)
      self.map.remove();
    self.map = L.map('map');
    self.init();
  });

  this.chartCanvas = null;


  this.mapTypeLabel = $('<div id="map">').addClass('map').html("map : ").appendTo(this.div);
  if(this.map !== null)
    this.map.remove();
  this.map = L.map('map');
};

//Called when pcm is loaded to init chart
MapFactory.prototype.init = function(){
  for(var f in this.editor.features){
    var feature = this.editor.features[f];
      if(this.chartDataX == null){
        this.chartDataX = feature;
        console.log(this.chartDataX)
      }
      this.chartXselect.append('<option value="'+feature.generated_KMF_ID+'">'+feature.name+'</option>');
      //this.chartYselect.append('<option value="'+feature.generated_KMF_ID+'">'+feature.name+'</option>');
  }
  this.drawMap()
}
MapFactory.prototype.drawMap = function(){
//  var map = L.map('map');
  this.map.createPane('labels');

  // This pane is above markers but below popups
  this.map.getPane('labels').style.zIndex = 650;

  // Layers in this pane are non-interactive and do not obscure mouse/touch events
  this.map.getPane('labels').style.pointerEvents = 'none';


  var cartodbAttribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, &copy; <a href="http://cartodb.com/attributions">CartoDB</a>';

  var position = L.tileLayer('http://{s}.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png', {
    attribution: cartodbAttribution
  }).addTo(this.map);
  position.addTo(this.map);
  this.map.setView({ lat: 47.040182144806664, lng: 9.667968750000002 }, 4);

  addAllMarker(this.map)
}

//add marker from city
locationCity = function (city, product, data, map){
  var latLon =[]
  for(var i in data){
    if(data[i].city === city){
      latLon.push(data[i].lat)
      latLon.push(data[i].lng)
      //console.log(latLon)
      addMarker(latLon, product, map)

    }
    var latLon =[]


  }
  /*$.get( "http://nominatim.openstreetmap.org/search/"+city+"?format=json", function (data) {
    latLon.push(data[0]['lat'])
    latLon.push(data[0]['lon'])
    var marker = L.marker(latLon);
    marker.addTo(map)
  })*/
}

function addMarker(latLon, product, map){
  var marker = L.marker(latLon);

  marker.addTo(map)
  //console.log(product.getCell(this.chartDataX).content)
  marker.bindTooltip(product.getCell(this.chartDataX).content);

}

function addAllMarker(map){
  var f = this.isCity();
  var citiesData = cities
  for(var p in this.editor.products){
    var product = this.editor.products[p];
    var city  = this.editor.features[f];
    city = product.getCell(city).content
    locationCity(city, product, citiesData, map);
  }
}
isCity = function(){
  for(var f in this.editor.features) {
    var feature = this.editor.features[f]
    //console.log(feature.name)
    if(feature.name === "City" ||feature.name === "Ville" ){
    return f
    }
  }
}
