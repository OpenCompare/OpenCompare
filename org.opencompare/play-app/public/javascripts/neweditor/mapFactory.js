//Object to generate chart

  function MapFactory(editor, div){
  var self = this;
  this.editor = editor;
  this.div = div;

  this.chartDataX = null; //feature for x
  this.chartDataY = null; //feature for y
  this.map = null;
  this.position = null
  this.markerLayer = null
  this.marker = null


  this.configDiv = $('<div class="mapConfig">').appendTo(this.div)

  this.chartXLabel = $('<label>').html(' x : ').appendTo(this.configDiv);
  this.chartXselect = $('<select>').appendTo(this.configDiv).change(function () {
    self.chartDataX = self.editor.pcm.features[self.chartXselect.val()]
    self.mapTypeLabel = $('<div id="map">').addClass('map').appendTo(this.mapTypeLabel);
    if(self.map !== null)
      //self.map.remove();
      self.markerLayer.remove()
    //self.map = L.map('map');
    self.drawMap();
  });

  this.chartYLabel = $('<label>').html(' x : ').appendTo(this.configDiv);
  this.chartYselect = $('<select>').appendTo(this.configDiv).change(function(){
    self.chartDataY = self.editor.pcm.features[self.chartYselect.val()]
    self.mapTypeLabel = $('<div id="map">').addClass('map').appendTo(this.mapTypeLabel);
    if(self.map !== null)
      //self.map.remove();
      self.markerLayer.remove()
    //self.map = L.map('map');
    self.drawMap();
  });
  //this.chartCanvas = null;


  this.mapTypeLabel = $('<div id="map">').addClass('map').html("map : ").appendTo(this.div);
  if(this.map !== null)
    this.map.remove();
  this.map = L.map('map');
};

//Called when pcm is loaded to init chart
MapFactory.prototype.init = function(){
  for(var f in this.editor.pcm.features){
    var feature = this.editor.pcm.features[f];
      if(this.chartDataX == null){
        this.chartDataX = feature;
        this.chartDataY = feature;
      }

      this.chartXselect.append('<option value="'+f+'">'+feature.name+'</option>');
      this.chartYselect.append('<option value="'+f+'">'+feature.name+'</option>');
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
  this.markerLayer = L.layerGroup().addTo(this.map)


  var cartodbAttribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, &copy; <a href="http://cartodb.com/attributions">CartoDB</a>';

   this.position = L.tileLayer('http://{s}.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png', {
    attribution: cartodbAttribution
  }).addTo(this.map);
  //this.position.addTo(this.map);
  this.map.setView({ lat: 47.040182144806664, lng: 9.667968750000002 }, 4);
  this.addAllMarker()
}

//add marker from city
MapFactory.prototype.locationCity = function (city, product, data, map, layer){
  var latLon =[]
  for(var i in data){
    if(data[i].city === city){
      latLon.push(data[i].lat)
      latLon.push(data[i].lng)
      //console.log(latLon)
      this.addMarker(latLon, product)

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

MapFactory.prototype.addMarker = function (latLon, product){
 this.marker = L.marker(latLon).addTo(this.markerLayer);
 string1 = product.getCell(this.chartDataX).value
if (string1 == "undefined")
  string1 = "N/A"
string2 = product.getCell(this.chartDataY).value
if (string2 == "undefined")
   string2 = "N/A"
 this.marker.bindTooltip("<dt>"+this.chartDataX.name+" :"+string1
  +"<dt>"+this.chartDataY.name+" :"+string2);
 this.markerLayer.addLayer(this.marker)

}

MapFactory.prototype.addAllMarker = function (){
  var f = this.isCity();
  var citiesData = cities
  for(var p in this.editor.pcm.products){
    var product = this.editor.pcm.products[p];
    this.locationCity(product.getCell(this.editor.pcm.features[f]).value,
      product, citiesData);
  }
}
MapFactory.prototype.isCity = function(){
  for(var f in this.editor.pcm.features) {
    var feature = this.editor.pcm.features[f]
    //console.log(feature.name)
    if(feature.name === "City" ||feature.name === "Ville" ){
    return f
    }
  }
}
