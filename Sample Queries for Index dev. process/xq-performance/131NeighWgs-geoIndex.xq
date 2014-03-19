import module namespace geo-index = "http://expath.org/ns/ProGeoIndex";
declare namespace gml="http://www.opengis.net/gml";

let $a:= <gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates decimal="." cs="," ts=" ">
                  6,52.6 6.1,52.6 6.1,53 6,53 6,52.6
                  </gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
            </gml:Polygon>

return geo-index:intersects("neighbourhoods-espg28992", $a) 
