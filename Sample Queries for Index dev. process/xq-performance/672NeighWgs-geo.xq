import module namespace geo-index = "http://expath.org/ns/SimpleGeoIndex";
import module namespace geo = "http://expath.org/ns/Geo";
declare namespace gml="http://www.opengis.net/gml";

let $a:= <gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates decimal="." cs="," ts=" ">
                  5,52.5 6,52.5 6,53 5,53 5,52.5
                  </gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
            </gml:Polygon>

return (
geo-index:visitor("neighbourhoods-espg28992", $a)[geo:intersects( $a, .)],
geo:time())
