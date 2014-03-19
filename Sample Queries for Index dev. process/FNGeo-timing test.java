  /**
   * Constructor.
   * @param ii input info
   * @param f function definition
   * @param e arguments
   */
  public FNGeo(final InputInfo ii, final Function f, final Expr... e) {
    super(ii, f, e);
  }

  @Override
  public Item item(final QueryContext ctx, final InputInfo ii) throws QueryException {
    switch(sig) {
      case _GEO_DIMENSION: return dimension(ctx);
      case _GEO_CENTROID:  return centroid(ctx);
      default:             return super.item(ctx, ii);
    }
  }

  /**
   * Returns the dimension of an item.
   * @param ctx query context
   * @return archive
   * @throws QueryException query exception
   */
  private Item dimension(final QueryContext ctx) throws QueryException {
    // evaluate first expression and retrieve resulting item
    Item item = checkItem(expr[0], ctx);
    ANode node = checkNode(item);

    // retrieve element name
    QNm qname = node.qname();

    // check QName
    if(qname.eq(Q_GML_POINT) || qname.eq(Q_GML_LINESTRING) || qname.eq(Q_GML_POLYGON)) {
      // loop through all children and choose first element node
      for(ANode n : node.children()) {
        if(n.type == NodeType.ELM) {
          qname = n.qname();
          node = n;
          break;
        }
      }
    }

    // check QName
    if(qname.eq(Q_GML_POS)) {
      byte[] dim = node.attribute(Q_SRS_DIMENSION);
      if(dim != null) return Int.get(Token.toLong(dim));
    }

    /* check namespace uri
     * would be interesting if we want to choose different code, depending on the
     * markup language (gml, kml)
    if(Token.eq(qname.uri(), GMLURI)) {
      System.out.println("Correct namespace found!");
    }*/
    throw Err.GEO_ERROR.thrw(info, node);
  }

  /**
   * Creates a centroid.
   * @param ctx query context
   * @return archive
   * @throws QueryException query exception
   */
  private Item centroid(final QueryContext ctx) throws QueryException {
    // evaluate first expression and retrieve resulting item
    Item item = checkItem(expr[0], ctx);
    ANode node = (ANode) checkType(item, NodeType.ELM);

    FElem elem = new FElem(Q_GML_POINT, GML_NS);
    elem.add(Q_SRS_DIMENSION, "2");
    FElem pos = new FElem(Q_GML_POS);
    elem.add(pos);
    pos.add("hi");
    return elem;

    /*
    <gml:Point [srsDimension='2|3']?>
      <gml:pos [srsDimension='2|3']?>double_x double_y </gml:pos>
    </gml:Point>
    */
  }

  /**
   * Returns the dimension of an item.
   * @param ctx query context
   * @return archive
   * @throws QueryException query exception
   */
  private Item anotherFunc(final QueryContext ctx) throws QueryException {
    // evaluate first expression and retrieve resulting item
    Item item = checkItem(expr[0], ctx);

    if(ENVELOPE_TEST.eq(item)) {
      System.out.println("GML Envelope");
    }
    return null;
  }
}


/*
  private ValueBuilder boundingPolygons() {
    ValueBuilder vb = new ValueBuilder();
    vb.add(Str.get("dd"));
    vb.add(Dbl.get(1234));
    vb.add(new FElem(new QNm("abc")));
    return vb;
  }
  
*/

/* Examples:

let $gml := <gml:pos dimension="2" xmlns:gml="http://www.opengis.net/gml">45.67 88.56</gml:pos>
return geo:dimension($gml)


declare namespace gml = "http://www.opengis.net/gml";
for $a in doc(exa2.xml)/PhotoCollection/items/Item/Position/gml:Point
return geo:envelope($a) 
 *
 */
Features/gml:featureMember/complexPoly/_SHAPE_/gml:MultiPolygon/gml:polygonMember

srsName="EPSG:4326">
     <gml:coordinates>12.970463244,44.292817075000002</gml:coordinates> 

declare namespace gml = "http://www.opengis.net/gml";
for $a in doc("gml1")//gml:Point
for $b in doc("gml1")//gml:Point
return geo:equals($a,$b) 

let $gml := <gml:MultiPoint xmlns:gml="http://www.opengis.net/gml" srsName="EPSG:4326">
	          <gml:pointMember>
	            <gml:Point>
	              <gml:coordinates>2.079641,45.001795</gml:coordinates>
	            </gml:Point>
	          </gml:pointMember>
	          <gml:pointMember>
	            <gml:Point>
	              <gml:coordinates>2.718330,45.541131</gml:coordinates>
	            </gml:Point>
          </gml:pointMember>
	          <gml:pointMember>
	            <gml:Point>
	              <gml:coordinates>3.016384,45.143725</gml:coordinates>
	            </gml:Point>
	          </gml:pointMember>
	          <gml:pointMember>
	            <gml:Point>
	              <gml:coordinates>0.930003,45.001795</gml:coordinates>
	            </gml:Point>
	          </gml:pointMember>
	        </gml:MultiPoint>
          return geo:x($gml)


declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:MultiPolygon>
               <gml:Polygon>
              <gml:outerBoundaryIs> <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 1,20 1,1</gml:coordinates>
                         </gml:LinearRing> </gml:outerBoundaryIs></gml:Polygon>
               <gml:Polygon>
              <gml:outerBoundaryIs> <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2</gml:coordinates>
                         </gml:LinearRing> </gml:outerBoundaryIs></gml:Polygon>
               <gml:Polygon>
              <gml:outerBoundaryIs> <gml:LinearRing><gml:coordinates>10,10 20,10 20,20 10,20 10,10</gml:coordinates>
                         </gml:LinearRing> </gml:outerBoundaryIs></gml:Polygon>                     
                         </gml:MultiPolygon>

        
return geo:centroid($a)


declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:MultiPolygon>
               <gml:Polygon>
              <gml:outerBoundaryIs> <gml:LinearRing><gml:coordinates>1,1 2,1 2,2 1,2 1,1</gml:coordinates>
                         </gml:LinearRing> </gml:outerBoundaryIs></gml:Polygon>
              
               <gml:Polygon>
              <gml:outerBoundaryIs> <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 1,20 1,1</gml:coordinates>
                         </gml:LinearRing> </gml:outerBoundaryIs></gml:Polygon>                     
                         </gml:MultiPolygon>

        
return geo:exterior_ring($a)



declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:MultiLineString>
                <gml:LineString><gml:coordinates>1,1 0,0 2,1</gml:coordinates></gml:LineString>
                <gml:LineString><gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>
              </gml:MultiLineString>

return geo:point_on_surface($a)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:LineString><gml:coordinates>1,1 55,99 2,1 2.1,1 2.2,1 2.3,1 2.4,1 2.5,1 2.6,1 2.7,1 2.8,1 2.9,1</gml:coordinates>
                         </gml:LineString>

        
return geo:point_on_surface($a)

declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2
                </gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>10,10 20,10 20,20 10,20 10,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon>
              return geo:exterior_ring($a)

equals:

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:MultiLineString>
                <gml:LineString><gml:coordinates>1,1 2,1</gml:coordinates></gml:LineString>
              </gml:MultiLineString>
let $b:=<gml:LineString><gml:coordinates>1,1 2,1</gml:coordinates></gml:LineString>
return geo:equals($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:MultiPolygon>
  <gml:Polygon><gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 1,20 1,1</gml:coordinates></gml:LinearRing>
                </gml:outerBoundaryIs>
   </gml:Polygon>
              </gml:MultiPolygon>
let $b:= <gml:Polygon><gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 1,20 1,1</gml:coordinates></gml:LinearRing>
                </gml:outerBoundaryIs>
   </gml:Polygon>
return geo:equals($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>
let $b:= <gml:Point><gml:coordinates>1.00,1.00</gml:coordinates></gml:Point>            
return geo:equals($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>
                
let $b:= <gml:LineString><gml:coordinates>1,1 1,1 55,99 2,1</gml:coordinates></gml:LineString>
            
return geo:equals($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>
                
let $b:= <gml:Polygon><gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>
            </gml:outerBoundaryIs>
          </gml:Polygon>

return geo:equals($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2
                </gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>10,10 19,10 19,19 10,19 10,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon>
              let $a:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2
                </gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>10,10 19,10 19,19 10,19 10,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon>
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
              return geo:equals($a,$b)
disjoint:
declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>2,2 3,2 3,3 2,3 2,2
                </gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing><gml:coordinates>10,10 19,10 19,19 10,19 10,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon>
let $b:=<gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates></gml:LinearRing>
              </gml:outerBoundaryIs>
             
            </gml:Polygon>
            return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:MultiLineString>
              <gml:LineString><gml:coordinates>1,1 0,0 2,1</gml:coordinates></gml:LineString>
              <gml:LineString><gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString> 
              </gml:MultiLineString>
let $b:=<gml:LineString><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:LineString>
            return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>
	  	  
let $b:= <gml:LineString><gml:coordinates>0,0 2,2</gml:coordinates></gml:LineString>
            
return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>
	  	  
let $b:= <gml:LineString><gml:coordinates>1,2 55,0</gml:coordinates></gml:LineString>
            
return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:LinearRing><gml:coordinates>1,1 20,1 50,30 1,1</gml:coordinates></gml:LinearRing>
	  	  
let $b:= <gml:LinearRing><gml:coordinates>1,1 20,1 20,0 1,1</gml:coordinates></gml:LinearRing>
            
return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:MultiPoint>
                <gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>
        	  	  <gml:Point><gml:coordinates>10,10</gml:coordinates></gml:Point>
                <gml:Point><gml:coordinates>2,2</gml:coordinates></gml:Point>
                </gml:MultiPoint>
let $b:= <gml:LinearRing><gml:coordinates>0,0 20,0 20,20 0,20 0,0</gml:coordinates></gml:LinearRing>
            
return geo:disjoint($a,$b)

declare namespace gml="http://www.opengis.net/gml";
let $a:= <gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>
let $b:=  <gml:MultiPoint><gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point> 
    <gml:Point><gml:coordinates>1,2</gml:coordinates></gml:Point> 
</gml:MultiPoint>
return geo:within($a,$b)

import module namespace geo = "http://expath.org/ns/geo";
declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:LinearRing><gml:coordinates>1,1 55,99 2,1
            </gml:coordinates></gml:LinearRing>
            let $b:=  <gml:LineString><gml:coordinates>1,1 55,99 2,1
            </gml:coordinates></gml:LineString>
return geo:equals($a,
           $b)

import module namespace geo = "http://expath.org/ns/geo";
declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:LinearRing><gml:coordinates>1,1 55,99 2,1
            </gml:coordinates></gml:LinearRing>
            let $b:=  <gml:LineString><gml:coordinates>1,1 55,99 2,1
            </gml:coordinates></gml:LineString>
return geo:intersects(<gml:MultiLineString><gml:LineString><gml:coordinates>
            1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>
            <gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>
            </gml:MultiLineString>, <gml:LineString><gml:coordinates>0,0 2,1 3,3
            </gml:coordinates></gml:LineString>)

let $a:= //gml:Point[1]
return geo:queryAll( $a)

let $a:= (//gml:Point[1])[1]
return geo:nnei("layer", $a)



let $a:= //gml:Point
let $b:= <gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>
return geo:contains("layer", $a, $b)



let $a:= //gml:LineString
let $b:= <gml:LineString><gml:coordinates>1,1 2800000,1 1,280000 200000,200000</gml:coordinates></gml:LineString>
return count(geo:contains("layer", $a, $b))



let $a:= //gml:Polygon
let $b:= <gml:Polygon>
          <gml:outerBoundaryIs>
	    <gml:LinearRing>
	      <gml:coordinates>
	        278200,187600 278400,187600 278400,188000 278200,188000 278200,187600
	      </gml:coordinates>
	    </gml:LinearRing>
	  </gml:outerBoundaryIs>
        </gml:Polygon>
return count(geo:contains("topo", $a, $b))



let $a:= <gml:Point srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
<coord><X>5.0</X><Y>40.0</Y></coord>
</gml:Point>

import module namespace geo-index = "http://expath.org/ns/GeoIndexModule";
declare namespace gml="http://www.opengis.net/gml";
let $a:=<gml:MultiLineString><gml:lineStringMember>
              <gml:LineString><coord><X>1.0</X><Y>1.0</Y></coord>
              <coord><X>1.0</X><Y>1.0</Y></coord>
              <coord><X>0.0</X><Y>0.0</Y></coord></gml:LineString></gml:lineStringMember>
              <gml:lineStringMember><gml:LineString><coord><X>2.0</X><Y>1.0</Y></coord>
              <coord><X>3.0</X><Y>3.0</Y></coord>
              <coord><X>4.0</X><Y>4.0</Y></coord></gml:LineString></gml:lineStringMember> 
              </gml:MultiLineString>
let $b:=<gml:LineString><coord><X>2.0</X><Y>1.0</Y></coord>
              <coord><X>1.0</X><Y>1.0</Y></coord>
              <coord><X>0.0</X><Y>0.0</Y></coord></gml:LineString>
let $c:=<gml:MultiPolygon><gml:polygonMember><gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><coord><X>11</X><Y>11</Y></coord>
                <coord><X>18</X><Y>11</Y></coord>
                <coord><X>18</X><Y>18</Y></coord>
                <coord><X>11</X><Y>18</Y></coord>
                <coord><X>11</X><Y>11</Y></coord></gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing>
                <coord><X>2</X><Y>2</Y></coord>
                <coord><X>3</X><Y>2</Y></coord>
                <coord><X>3</X><Y>3</Y></coord>
                <coord><X>2</X><Y>3</Y></coord>
                <coord><X>2</X><Y>2</Y></coord></gml:LinearRing>
              </gml:innerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing>

                <gml:coordinates>10,10 19,10 19,19 10,19 10,10</gml:coordinates></gml:LinearRing>
              </gml:innerBoundaryIs>
              </gml:Polygon></gml:polygonMember><gml:polygonMember><gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing><coord><X>11</X><Y>11</Y></coord>
                <coord><X>18</X><Y>11</Y></coord>
                <coord><X>18</X><Y>18</Y></coord>
                <coord><X>11</X><Y>18</Y></coord>
                <coord><X>11</X><Y>11</Y></coord>
               </gml:LinearRing>
              </gml:outerBoundaryIs>
             
            </gml:Polygon>
            </gml:polygonMember>
            </gml:MultiPolygon>



            let $d:=<gml:MultiPoint>
                <gml:pointMember><gml:Point><coord><X>11</X><Y>11</Y></coord></gml:Point></gml:pointMember>
        	  	  <gml:pointMember><gml:Point><gml:coordinates>10,10</gml:coordinates></gml:Point></gml:pointMember>
                <gml:pointMember><gml:Point><gml:coordinates>2,2</gml:coordinates></gml:Point></gml:pointMember>
                </gml:MultiPoint>
            return geo-index:test("layertest",$c)

insert(10, empty())
--> empty(function($l,$y,$r)...,function(){..})
--> function(){tree:branch(em,x,em)}
--> function() -> $branch(em, x, em)
--> function(br, em) { br(em,x,em)}
--> contains(fup(br,em), 10) 
--> contains( br(l,y,r) , false())



Results: 1777: index -> 2138.63 ms, geo ->18605.79 ms
import module namespace geo-index = "http://expath.org/ns/GeoIndex";
declare namespace gml="http://www.opengis.net/gml";

let $a:= <gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates decimal="." cs="," ts=" ">88289.4063,401843.5 88784.2031,401579.8125 89172,401373.8125 89164.7031,401354.6875 89033.4063,401008.1875 89207.0938,401049.1875 89331.7969,401070.6875 89454.4063,401077.6875 89547.0938,401064.9063 89613.7969,401069.6875 89673.2031,401115.8125 89776.5,401081.4063 89976.0938,401023.0938 89946,400985.4063 140000,400954.3125 140066.2969,400920.9063 140321.5,400841.1875 140583.2031,400751.5 140750,400706.6875 140834.2969,400918.8125 141050.2969,400858 141093.7031,400965.6875 141242.0938,400948.5 141253.7656,400946.1875 141230.75,400891.2188 141189.6563,400785.0938 141146.8594,400671.75 141092.6094,400529.75 141068.2031,400469.5938 141066.0156,400452.7188 141026.9688,400359.5 141002.4063,400296.8125 140970.6406,400206.7813 140909.5625,400053.3438 140931.4531,400051.5 140859.6719,459878.5 140691.8906,459459.5313 140689.1719,459452.1875 140646.3281,459336.1563 140545.8906,459072.25 140465.4375,458864.0938 140402.3906,458685.4375 140327.1563,458496.5625 140255.5,458523.625 140222.0156,458435.9688 140190,458472.6875 140129.7969,458521.5938 140056.4063,458567.6875 140000,458606.6875 89576.4063,458780.9063 89094.7031,458953.9063 88744.2031,459052.9063 88630.8082,459088.7893 88628.4,459091.7 88590.3,459125.3 88567.2,459140 88566.6,459140.3 88556.3,459146.7 88543.3,459156.5 88539.8,459157.4 88530.7,459159.9 88325.3,459215.6 88322.3,459213.2 88273.2,459189.5 88268.5,459193.3 88254.3,459205 88249.6,459208.9 88248.4,459206.1 88233.9,459171.8 88233.6,459170.9 88228.9,459160.6 88227.7,459157.7 88220.7,459137 88220.4,459136.4 88202.1,459143.6 88198.9,459163.5 88198.6,459165.4 88195.4,459184.8 88195.3,459185.5 88194.8,459190.5 88194.8,459206.3 88196.3,459217.3 88199.7,459226.1 88198.1,459226.6 88196.7,459227.1 88182.9,459231.9 88175,459234.6 88175.5,459235.8 88175.6,459236.2 88176.2,459237.9 88177.4,459241 88178.3,459243.3 88179.3,459245.9 88172,459247.9 88171.9,459247.9 88171,459248.1 88147.1,459223.8 88080.9,459180.2 88084.6,459190.6 88097.1,459225.9 88058.2,459242.7 88054.8,459244.2 88054.4,459244.4 88050.4,459246.1 88033.6,459253.3 87991.1,459271.6 87929.9,459297.8 87928.7,459298.3 87887.2,459316.2 87864.8,459325.8 87864,459326.1 87844.9,459334.3 87844.8,459334.4 87824.5,459343.1 87802.6,459352.3 87780.7,459362 87740,459379.7 87697.3,459396.8 87657.3,459413.7 87655.4,459414.5 87653.5,459415.3 87613.4,459432.5 87613,459432.7 87591.9,459441.8 87568.9,459451.6 87549.4,459460 87509.5,459477.1 87508.2,459477.7 87507.3,459478.1 87499.4,459481.5 87465.4,459496.2 87443.8,459505.5 87422.9,459514.8 87422.5,459515 87400.2,459524.2 87358.1,459542.4 87317.9,459559.7 87274.7,459578.3 87274.3,459578.4 87247.4,459590 87211.8,459605.3 87211.3,459605.5 87178.6,459619 87150.1,459630.4 87148.4,459631 87140.1208,459637.0466 87522.9063,400550.9063 87612.7031,400750.3125 87690,400911.0938 87734.5938,401008 87815.4063,401120.5938 87925,401249.5 88016.7031,401381.9063 88147.0938,401580.9063 88266.7969,401798.5938 88289.4063,401843.5</gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
            </gml:Polygon>
let $b:= //gml:Polygon
return geo-index:intersects("neighbourhoods", $b, $a) 

with custom reader: Total Time:  2326.07 ms
DOM: 319.91 ms
Serialize: 210.52 ms
Read: 722.87 ms
Test: 731.88 ms
Count: 1854


with JTS reader: Total Time: 2770.8 ms
DOM: 303.24 ms
Serialize: 204.87 ms
Read: 1272.93 ms
Test: 657.51 ms
Count: 1854

* new: 
With custom reader :Total:1915.91 ms
DOM: 318.4 ms
Serialize: 0.96 ms
Read: 487.28 ms
Test: 748.91 ms
Count: 1854
 
With JTS reader: Total: 2845.47 ms
DOM: 314.21 ms
Serialize: 214.35 ms
Read: 1268.81 ms
Test: 745.81 ms
Count: 1854

Without index, with JTS reader: Total: 17883.9 ms

NEW QUERY IN WGS84 system:
import module namespace geo-index = "http://expath.org/ns/GeoIndex";
declare namespace gml="http://www.opengis.net/gml";

let $a:= <gml:Polygon>
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates decimal="." cs="," ts=" ">4.4245885811185195,51.60302156591154
4.431780595088128,51.60070999143471
4.437416698458258,51.59890396227287
4.437314984070633,51.598731199866464
4.435485365572557,51.595601242588884
4.4379843048937975,51.59599015893792
4.439779995704469,51.596198016074375
4.441548204913222,51.59627526664867
4.442888283748815,51.596171197111865
4.443850061005972,51.59622194729405
4.444698804352266,51.59664346311667
4.446196039755448,51.59634621191295
4.4490874746479,51.595845220458834
4.448660170700746,51.59550297556987
5.171119582619066,51.59879287414707
5.172077973944839,51.59849433603677
5.175765032863048,51.59778443356025
5.179546208510454,51.59698496775784
5.181955574125879,51.59658638359193
5.183163705146179,51.598495422378846
5.186283970720575,51.59795415620842
5.186906223256678,51.5989232939391
5.189048856705497,51.59877244329413
5.1892174245832665,51.59875194097093
5.188887370672373,51.59825723368138
5.1882984019371,51.597302209473696
5.187685160463152,51.59628224652689
5.18690777917695,51.59500439063842
5.18655791217592,51.59446300895282
5.186527012185414,51.59431125619265
5.185967171001036,51.59347229157073
5.185615173973006,51.59290814789604
5.1851603227426954,51.59209801589207
5.184285001891152,51.59071715363215
5.184601000394885,51.59070112771851
5.181112293782265,52.128500232523685
5.178679398952337,52.124729833988525
5.178640003450291,52.12466375067937
5.178019200598762,52.12361964301053
5.17656363966855,52.12124478909368
5.175397669943259,52.119371576427994
5.174484694775189,52.1177639844313
5.173394272959505,52.116064206378645
5.172346822794846,52.11630556357391
5.171861708439386,52.11551673299946
5.1713926460681146,52.11584593878466
5.170511468143933,52.116283939000816
5.169437838453029,52.11669629404411
5.168612500828476,52.117045335436806
4.432366032061528,52.11494439771913
4.425299967770344,52.11644211513402
4.420163448082227,52.117290036183604
4.418500871584122,52.11759895164931
4.418465143763397,52.11762482380924
4.417902319405802,52.1179222441946
4.417562182743714,52.11805159209773
4.417553363967795,52.118054216392906
4.417401730814421,52.11811050162264
4.417210012346275,52.11819702116133
4.417158735150733,52.11820468963233
4.417025383115622,52.11822606562349
4.414015546200126,52.118701963693766
4.413972214636355,52.11868003087399
4.413259973137417,52.118461093516444
4.413190605822837,52.118494680456315
4.412980984371415,52.11859812533758
4.412911597023628,52.11863261090499
4.412894625487973,52.11860729981598
4.412689645817759,52.11829726292282
4.412685442225969,52.11828913755555
4.412618840717888,52.118195994269
4.412601889129666,52.11816978434332
4.4125037475097795,52.11798288870423
4.412499485124024,52.11797745971069
4.412230886124515,52.11803996237077
4.412180259533374,52.11821843567651
4.412175506523156,52.118235476507735
4.412124977618167,52.1184094558158
4.412123380183327,52.11841573528755
4.4121150985971305,52.11846061454688
4.412111997516914,52.11860262390221
4.412131739373036,52.118701672405486
4.4121796542232135,52.11878117700094
4.412156195143455,52.11878547766223
4.412135656178235,52.118789802482894
4.411933225691985,52.11883127707426
4.411817350794601,52.118854589760474
4.411824415505112,52.11886543570925
4.4118257970347114,52.11886904296821
4.411834223647348,52.11888439496861
4.411851135787669,52.118912402587696
4.411863824814847,52.11893318360205
4.411877915022349,52.11895667308007
4.411770937604066,52.1189737666773
4.411769477538667,52.118973754590755
4.41175629768105,52.1189754433976
4.411412114934274,52.11875414741391
4.410454128682701,52.118354265483084
4.410506105766267,52.118448187751845
4.410681673223498,52.118766974331656
4.410110406485003,52.118913263272546
4.410060469319888,52.11892633351446
4.410054589729434,52.11892808266927
4.409995852809447,52.11894287782244
4.4097491455174636,52.11900555642045
4.409125014855407,52.11916488642793
4.408226288865615,52.11939294964227
4.4082086694008,52.119397298037555
4.407599207520323,52.11955314606586
4.407270255371103,52.11963671069639
4.407258515511134,52.11963930993375
4.406978021240795,52.11971069134598
4.406976541421894,52.119711577992135
4.4066784264846195,52.119787306895844
4.406356850341654,52.11986733454592
4.406035174300483,52.119951855274124
4.405437417660241,52.12010599175124
4.404810573462153,52.12025448898101
4.404223187644191,52.12040151415049
4.404195287266209,52.120408473056166
4.404167386879571,52.12041543195516
4.403578477332084,52.120565138091045
4.403572597273879,52.120566886920905
4.40326271074547,52.12064610480624
4.402924942159274,52.120731381685786
4.402638554038983,52.12080450152259
4.402052574365539,52.12095332560928
4.402033473731452,52.12095855966116
4.402020253263941,52.12096204495827
4.401904228887495,52.120991639401105
4.4014048684094575,52.12111960956626
4.40108763386174,52.12120055821332
4.400780619127517,52.12128159156289
4.400774738866222,52.12128334025248
4.400447300910094,52.12136330281729
4.39982896486058,52.121521733497296
4.399238546273097,52.12167230440835
4.39860406039628,52.1218341891317
4.398598199878526,52.12183503892219
4.398203108993792,52.12193600266973
4.3976802453442305,52.122069153644475
4.397672904697108,52.12207088991724
4.397192741438569,52.12218821607732
4.396774321460575,52.12228718115606
4.396749378968291,52.1222923652575
4.396627282956779,52.1223456953059
4.413774212823916,51.591311413598845
4.4150316005670165,51.59311456927062
4.416116129209587,51.59456903137018
4.416741044156681,51.59544542175043
4.417885681894269,51.59646715357022
4.419442626475328,51.59763893896271
4.420740766656314,51.59884002973849
4.422584629800662,51.60064429617418
4.424270805023155,51.60261523639313
4.4245885811185195,51.60302156591154</gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
            </gml:Polygon>
let $b:= //gml:Polygon
return geo-index:intersects("neighbourhoods-espg28992", $a) -> geo (1778): 15324 ms, index: 3175 ms, ProIndex: 2222 ms
simple index (IdentityhashMap):2389 ms
read single input: 366.9 ms
read geometries in DB: 1294.49 ms
test JTS function: 700.42 ms

simple index:3186 ms
read single input: 1273.49 ms
read geometries in DB: 1223.18 ms
test JTS function: 654.3 ms

ProIndex: 2328 ms

result: 194
return geo-index:overlaps("neighbourhoods-espg28992", $a) -> geo: 15907 ms, index: 3296 ms, ProIndex: 2332 ms

result: 1584
return geo-index:contains("neighbourhoods-espg28992", $a) -> geo: 15435 ms, index: 3085 ms, ProIndex: 2296 ms
simple index (IdentityhashMap):2466 ms
read single input: 372.39 ms
read geometries in DB: 1426.14 ms
test JTS function: 601.42 ms

simple index:3278 ms
read single input: 1385.2 ms
read geometries in DB: 1218.44 ms
test JTS function: 605.63 ms

ProIndex:2261 ms
*********************************************************************************************************************


results:219
import module namespace geo-index = "http://expath.org/ns/GeoIndex";
declare namespace gml="http://www.opengis.net/gml";

let $a:= //gml:Polygon
let $b:= <gml:Polygon>
          <gml:outerBoundaryIs>
	    <gml:LinearRing>
	      <gml:coordinates>
	       291109.750,92089.800 291116.950,92087.100 291122.500,92085.150 291132.650,92080.600 291132.250,92079.700 291133.500,92079.150 291130.700,92076.700 291131.250,92075.850 291127.300,92072.650 291123.750,92069.700 291120.050,92066.710 291117.100,92064.100 291113.150,92060.900 291109.200,92057.600 291100.200,92050.200 291097.200,92047.650 291095.350,92049.600 291094.450,92050.800 291093.150,92052.550 291092.300,92053.650 291091.600,92054.600 291091.050,92055.200 291090.400,92055.850 291089.750,92056.450 291088.500,92057.200 291087.100,92057.850 291085.700,92058.250 291084.450,92058.450 291083.750,92058.450 291083.590,92058.440 291081.950,92058.300 291079.250,92057.850 291076.200,92057.300 291074.450,92056.950 291069.700,92056.050 291065.400,92055.250 291060.800,92054.350 291056.300,92053.550 291051.850,92052.700 291047.300,92051.750 291042.850,92050.850 291038.100,92049.900 291033.930,92048.940 291029.250,92048.200 291024.730,92047.280 291018.200,92045.950 291013.350,92044.850 291011.550,92044.500 291005.200,92042.850 291000.000,92041.600 290991.100,92039.800 290983.710,92038.120 290978.150,92036.850 290975.520,92036.280 290970.040,92035.090 290964.550,92033.900 290959.240,92032.730 290951.150,92030.950 290937.450,92027.900 290930.200,92026.350 290927.450,92049.550 290926.790,92055.200 290925.850,92063.210 290925.230,92068.460 290924.900,92071.310 290924.250,92076.810 290923.600,92082.350 290923.270,92085.060 290922.550,92090.960 290921.920,92096.150 290921.590,92098.950 290920.900,92104.560 290920.250,92109.950 290919.960,92112.440 290919.290,92118.090 290918.590,92124.110 290918.350,92126.150 290917.660,92132.160 290916.990,92138.060 290916.750,92140.150 290916.060,92146.010 290915.430,92151.450 290915.100,92154.250 290914.760,92156.660 290914.700,92156.660 290913.730,92164.890 290913.050,92170.650 290912.800,92172.840 290912.150,92178.700 290911.450,92184.350 290911.200,92186.400 290910.450,92192.600 290909.800,92198.300 290909.500,92200.650 290908.900,92206.550 290908.500,92212.200 290908.420,92213.330 290908.350,92214.450 290908.250,92215.900 290908.150,92217.800 290908.200,92220.900 290908.250,92228.150 290908.300,92236.950 290908.550,92245.150 290911.050,92245.380 290911.050,92246.330 290910.490,92259.090 290908.650,92259.100 290908.880,92263.550 290909.110,92268.100 290909.360,92273.050 290909.600,92277.650 290909.600,92279.800 290909.850,92285.500 290910.090,92289.200 290910.450,92296.950 290910.650,92300.050 290915.450,92300.050 290917.700,92300.200 290920.650,92296.550 290923.100,92293.400 290925.640,92294.820 290922.250,92300.400 290921.700,92301.350 290930.100,92303.700 290934.160,92304.810 290934.650,92305.050 290935.700,92305.550 290936.400,92305.900 290937.500,92306.300 290938.450,92306.650 290940.300,92307.300 290942.250,92307.900 290943.450,92308.250 290944.450,92308.550 290947.400,92309.300 290948.390,92309.480 290952.850,92310.280 290953.500,92310.400 290956.650,92310.750 290956.850,92310.750 290957.650,92310.790 290959.750,92310.900 290962.700,92310.850 290963.800,92310.750 290967.000,92310.500 290971.000,92310.100 290971.800,92310.000 290972.000,92309.950 290972.850,92309.750 290974.750,92309.350 290976.650,92308.900 290980.600,92307.960 290985.250,92306.850 290989.430,92305.600 290989.600,92305.550 290996.950,92303.700 290998.290,92303.200 291000.000,92302.570 291002.260,92301.910 291007.100,92300.510 291011.440,92299.240 291015.830,92297.970 291020.180,92296.700 291024.360,92295.490 291028.780,92294.200 291033.190,92292.920 291037.560,92291.650 291042.210,92290.300 291047.060,92288.890 291051.500,92287.600 291051.250,92285.800 291050.100,92278.250 291049.450,92273.800 291049.100,92271.750 291048.600,92268.650 291048.650,92267.650 291047.800,92262.700 291046.990,92257.730 291046.180,92252.740 291045.450,92248.250 291042.900,92248.500 291029.300,92250.250 291023.260,92251.000 291018.560,92251.580 291013.960,92252.160 291009.310,92252.740 291005.060,92253.270 291000.500,92253.820 291000.000,92253.880 290996.230,92254.330 290991.550,92254.890 290986.730,92255.470 290987.000,92257.350 290987.450,92261.020 290986.180,92261.180 290980.630,92261.900 290980.680,92262.250 290975.550,92262.920 290970.430,92263.580 290970.380,92263.230 290964.990,92263.930 290959.600,92264.620 290960.660,92272.850 290961.580,92279.920 290958.700,92280.350 290955.110,92280.890 290953.700,92281.100 290953.480,92279.550 290952.880,92275.290 290952.170,92270.260 290951.460,92265.230 290950.750,92260.200 290950.170,92256.130 290949.600,92252.080 290949.510,92251.410 290953.850,92251.310 290953.850,92251.640 290964.490,92251.950 290964.550,92251.510 290969.040,92251.570 290969.520,92255.660 290971.230,92255.650 290970.770,92251.530 290975.770,92251.440 290982.340,92250.730 290984.450,92250.450 290985.180,92250.580 290985.780,92250.910 290984.550,92251.690 290983.850,92252.090 290983.410,92252.290 290983.150,92252.390 290982.670,92252.550 290982.280,92252.680 290981.550,92252.960 290980.660,92253.370 290979.940,92253.750 290979.230,92254.140 290978.510,92254.510 290976.270,92255.570 290976.030,92255.660 290975.850,92255.730 290975.650,92255.780 290975.460,92255.830 290975.270,92255.870 290975.070,92255.900 290974.760,92255.930 290974.560,92255.940 290974.360,92255.950 290974.160,92255.940 290971.690,92256.210 290970.400,92256.240 290969.330,92256.250 290966.760,92256.360 290963.990,92256.470 290961.980,92256.690 290959.440,92256.790 290959.140,92256.690 290958.980,92256.630 290958.820,92256.560 290958.660,92256.490 290958.510,92256.410 290958.360,92256.320 290958.220,92256.230 290958.080,92256.130 290957.940,92256.020 290957.810,92255.910 290957.690,92255.800 290957.570,92255.680 290957.450,92255.550 290957.340,92255.420 290957.240,92255.280 290957.130,92255.110 290957.040,92254.970 290956.960,92254.830 290956.890,92254.690 290956.830,92254.540 290956.770,92254.390 290956.710,92254.240 290956.670,92254.080 290956.630,92253.930 290956.590,92253.770 290956.570,92253.610 290956.540,92253.450 290956.530,92253.290 290956.430,92252.180 290951.630,92252.250 290954.480,92274.290 290959.340,92273.660 290958.350,92266.190 290958.340,92266.010 290958.330,92265.830 290958.330,92265.650 290958.340,92265.470 290958.360,92265.290 290958.380,92265.110 290958.410,92264.930 290958.450,92264.750 290958.500,92264.550 290958.540,92264.410 290958.590,92264.280 290958.630,92264.150 290958.690,92264.010 290958.740,92263.880 290958.800,92263.760 290958.870,92263.630 290958.940,92263.510 290959.020,92263.390 290959.150,92263.210 290959.270,92263.070 290959.400,92262.930 290959.540,92262.800 290959.690,92262.680 290959.840,92262.570 290960.000,92262.460 290960.280,92262.290 290960.590,92262.130 290960.900,92262.000 290961.230,92261.890 290961.610,92261.780 290963.910,92261.450 290966.470,92261.220 290969.090,92261.150 290971.540,92261.130 290972.370,92261.120 290973.610,92261.060 290974.190,92261.000 290975.330,92260.840 290975.730,92260.760 290976.340,92260.610 290977.710,92260.110 290980.300,92259.110 290980.470,92259.040 290980.630,92258.980 290980.800,92258.910 290980.970,92258.840 290981.140,92258.780 290981.300,92258.700 290981.460,92258.620 290981.650,92258.500 290982.290,92257.970 290982.680,92257.670 290983.340,92257.170 290984.790,92255.700 290985.170,92255.330 290985.550,92254.970 290985.960,92254.610 290987.390,92253.590 290989.460,92252.380 290991.180,92251.400 290991.270,92251.350 290991.360,92251.300 290991.440,92251.250 290991.530,92251.190 290991.620,92251.140 290991.710,92251.090 290991.790,92251.040 290991.880,92250.990 290991.970,92250.940 290992.060,92250.900 290992.160,92250.870 290992.340,92250.820 290992.390,92250.810 290992.440,92250.800 290992.490,92250.790 290992.540,92250.780 290992.590,92250.780 290992.640,92250.770 290992.690,92250.760 290992.740,92250.760 290992.790,92250.760 290992.840,92250.760 290992.890,92250.760 290992.940,92250.760 290992.990,92250.770 290993.070,92250.790 290993.590,92251.100 290993.720,92251.190 290993.850,92251.290 290993.980,92251.390 290994.100,92251.490 290994.230,92251.590 290994.360,92251.680 290994.570,92251.810 290994.720,92251.880 290994.860,92251.940 290995.010,92252.000 290995.170,92252.050 290995.320,92252.100 290995.490,92252.140 290996.680,92252.320 291000.000,92251.890 291042.050,92246.450 291042.200,92246.450 291042.350,92246.400 291042.600,92246.400 291042.750,92246.350 291043.300,92246.350 291043.450,92246.300 291045.000,92246.300 291045.150,92246.350 291045.550,92246.350 291045.700,92246.400 291045.950,92246.450 291046.250,92246.600 291046.350,92246.700 291046.450,92246.750 291046.550,92246.850 291046.650,92246.900 291046.700,92247.000 291046.800,92247.050 291046.900,92247.250 291047.000,92247.300 291047.100,92247.500 291047.200,92247.600 291047.250,92247.700 291047.300,92247.850 291053.250,92286.900 291053.200,92287.650 291053.150,92288.000 291053.100,92288.250 291053.050,92288.450 291052.950,92288.750 291052.850,92288.950 291052.550,92289.250 291052.500,92289.350 291052.400,92289.400 291052.350,92289.500 291052.250,92289.550 291052.000,92289.800 291051.500,92290.050 291051.400,92290.150 291051.100,92290.300 291050.900,92290.350 291050.800,92290.400 291050.650,92290.450 291050.450,92290.500 291050.200,92290.550 291049.900,92290.600 291049.700,92290.600 291000.000,92305.150 290985.900,92309.250 290983.550,92309.700 290980.300,92310.450 290978.900,92310.750 290977.450,92311.050 290975.750,92311.350 290973.800,92311.600 290972.400,92311.800 290970.900,92312.000 290968.650,92312.200 290967.650,92312.350 290966.320,92312.490 290964.350,92312.700 290961.050,92312.800 290959.650,92312.800 290958.640,92312.740 290956.400,92312.600 290953.100,92312.250 290949.350,92311.800 290945.650,92311.250 290943.400,92310.600 290939.700,92309.700 290934.250,92308.350 290928.250,92306.800 290919.150,92304.500 290914.500,92303.550 290910.300,92303.000 290910.000,92303.000 290909.600,92302.850 290909.300,92302.600 290909.050,92302.350 290908.850,92302.000 290908.800,92301.900 290908.750,92301.500 290908.800,92301.050 290908.600,92300.300 290908.350,92294.550 290907.950,92286.300 290906.710,92267.410 290906.650,92266.550 290906.290,92260.820 290906.100,92257.900 290905.850,92252.600 290905.710,92249.400 290905.500,92244.550 290905.440,92241.450 290905.350,92237.350 290905.250,92227.550 290905.330,92221.870 290905.350,92220.550 290905.550,92215.070 290905.600,92213.550 290905.850,92208.600 290906.150,92203.600 290906.700,92197.750 290907.400,92191.950 290908.500,92183.450 290910.450,92167.850 290912.330,92151.900 290912.850,92147.450 290913.090,92145.350 290926.390,92040.140 290927.160,92034.090 290928.120,92026.520 290928.680,92023.960 290929.250,92023.310 290929.920,92023.050 290930.980,92023.000 290933.290,92023.370 290934.430,92024.930 290971.310,92032.790 291000.000,92039.150 291002.900,92039.800 291011.450,92042.150 291011.990,92042.290 291017.550,92043.750 291018.630,92044.040 291019.250,92044.200 291023.250,92045.100 291024.300,92045.350 291028.300,92046.200 291032.300,92046.900 291049.150,92049.900 291060.050,92052.000 291069.450,92053.650 291077.750,92055.350 291081.600,92056.000 291081.900,92056.050 291082.400,92056.150 291082.700,92056.200 291084.350,92056.200 291084.950,92056.100 291085.300,92056.050 291085.650,92055.950 291085.950,92055.850 291086.300,92055.700 291086.600,92055.600 291086.950,92055.450 291087.850,92055.000 291088.100,92054.850 291088.400,92054.700 291088.700,92054.500 291088.950,92054.300 291089.250,92054.100 291089.500,92053.900 291089.800,92053.650 291090.050,92053.450 291090.800,92052.700 291092.900,92050.100 291095.050,92047.550 291096.250,92046.250 291097.100,92045.800 291097.850,92045.850 291099.450,92047.150 291100.000,92047.600 291113.000,92058.300 291118.500,92062.800 291121.050,92064.900 291128.450,92071.050 291130.250,92072.600 291135.100,92076.650 291135.450,92076.900 291135.850,92077.250 291136.150,92077.650 291136.450,92078.250 291136.550,92078.700 291136.600,92079.150 291136.550,92079.600 291136.450,92080.000 291136.350,92080.300 291136.050,92080.650 291135.750,92080.950 291135.400,92081.150 291130.350,92083.250 291119.800,92087.500 291116.850,92088.700 291107.350,92092.500 291105.500,92093.200 291105.100,92093.300 291104.850,92093.200 291104.550,92093.000 291104.400,92092.700 291104.100,92091.700 291103.550,92089.650 291102.150,92084.050 291101.590,92082.080 291103.800,92082.400 291106.000,92091.200 291109.750,92089.800
	      </gml:coordinates>
	    </gml:LinearRing>
	  </gml:outerBoundaryIs>
        </gml:Polygon>
return geo-index:touches("58116-SX9192-2c1", $b) ->by index: 219 results: 4080.96 ms, by geo:75 Polygon in 49756.15 ms + 144 LineString in 156036.4 ms
with custom reader:
DOM: 397.52 ms
Serialize: 163.43 ms
Read: 355.58 ms
Test: 1106.6 ms
Count: 3041 

with JTS reader:
DOM: 392.81 ms
Serialize: 167.0 ms
Read: 1151.01 ms
Test: 976.73 ms
Count: 3041

* new:
With custom reader: Total: 1075.74 ms
DOM: 209.48 ms
Serialize: 1.03 ms
Read: 100.17 ms
Test: 557.26 ms
Count: 3041

With JTS reader: Total: 3304,2 ms
DOM: 391.28 ms
Serialize: 158.19 ms
Read: 1235.14 ms
Test: 1050.35 ms
Count: 3041

Without index, with JTS reader: 52300.81 ms
return geo-index:intersects("58116-SX9192-2c1", $a, $b) ->by index: 223 results: 2833.85 ms, by geo:76 Polygon in 56056.19 ms + 144 LineString in 156743.82 ms + 3 Point in  13634.03 ms
with coustom reader:
DOM: 225.25 ms
Serialize: 124.58 ms
Read: 159.04 ms
Test: 501.11 ms

with JTS reader:
DOM: 217.1 ms
Serialize: 138.59 ms
Read: 946.62 ms
Test: 442.44 ms

* new
With JTS reader: Total :3528.85 ms
DOM: 430.43 ms
Serialize: 172.07 ms
Read: 1312.84 ms
Test: 1150.27 ms
Count: 3041

With custom reader: Total: 2192.61 ms
DOM: 435.41 ms
Serialize: 1.3 ms
Read: 110.82 ms
Test: 1175.04 ms
Count: 3041

Without index, with JTS reader: 52491.69 ms


return geo-index:contains("58116-SX9192-2c1", $a, $b) ->by index: 4 results: 2319.62 ms, by geo:3 Point in 13009.03 ms + 1 Polygon in  50399.29 ms
return geo-index:covers("58116-SX9192-2c1", $a, $b) ->by index: 75 results: 2125.36 ms, by geo:3 Point in 13009.03 ms + 1 Polygon in  50399.29 ms

43 results: index -> 3458.35 ms, geo -> 21172.04 ms
125 results:index -> 1239.79 ms, geo -> 18073.18 ms

219 results with custom reader: 
DOM: 502.06 ms
Serialize: 199.56 ms
Read: 479.15 ms
Test: 1474.9 ms
Count: 3041

with JTS reader:
DOM: 480.69 ms
Serialize: 203.21 ms
Read: 1405.36 ms
Test: 1367.34 ms


set createfilter *.gml
create db 58116-SX9192-2c1-wgs84 "/home/rostam/Desktop/important/BaseX/Sample gml/topo/Initial/58116-SX9192-2c1-wgs84/58116-SX9192-2c1-wgs84.zip"


dosjoint: 51.63 ms
inja poly: 
 time1:0.03 ms
Coord time:2.06 ms
Ring time:3.67 ms
 create outer:5.86 ms
 time1:0.07 ms
Coord time:0.08 ms
Ring time:0.0 ms
 create inner:0.15 ms
 time1:0.11 ms
Coord time:0.06 ms
Ring time:0.0 ms
 create inner:0.28 ms
 time1:0.11 ms
single read in geo function: 15.46 ms
inja poly: 
 time1:0.0 ms
Coord time:0.06 ms
Ring time:0.0 ms
 create outer:0.12 ms
 time1:0.0 ms
single read in geo function: 0.24 ms


IdentityHashMap: 131, last: 110 ms
read single input: 9.29 ms
read geometries in DB: 95.19 ms
test JTS function: 3.51 ms
read single input: 9.07 ms
read geometries in DB: 83.0 ms
read single input: 9.07 ms
read geometries in DB: 83.0 ms
test JTS function: 3.29 ms
