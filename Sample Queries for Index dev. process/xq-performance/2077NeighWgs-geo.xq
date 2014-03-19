import module namespace geo-index = "http://expath.org/ns/SimpleGeoIndex";
import module namespace geo = "http://expath.org/ns/Geo";
declare namespace gml="http://www.opengis.net/gml";

let $a:= <gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates decimal="." cs="," ts=" ">
                  5.47,51.1 5.9,51.1 5.9,53.30 5.47,53.30 5.47,51.1
                  </gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
            </gml:Polygon>

return (
geo-index:visitor("neighbourhoods-espg28992", $a)[geo:intersects( $a, .)],
geo:time())
