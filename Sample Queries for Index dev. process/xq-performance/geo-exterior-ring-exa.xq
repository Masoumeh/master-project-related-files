import module namespace geo = "http://expath.org/ns/geo";
declare namespace gml="http://www.opengis.net/gml";
let $b:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2
                </gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>19,10 19,19 10,19 10,10 19,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon>
              return geo:exterior-ring($b)