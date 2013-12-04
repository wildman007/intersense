/** 
  Intersense 2013 @ IIM 

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

import peasy.*;
import ComputationalGeometry.*;
import processing.net.*;


PShader mat;

float rozsah = 2500;

float SMOOTHING = 20.0;

IsoWrap surface;
int num = 100;

Client client;
String input;

PGraphics pass1, pass2;

PVector center;
PeasyCam cam;
ArrayList body;

void setup(){

  size(1280,720,P3D);

  client = new Client(this, "192.168.23.45", 12345);

  body = new ArrayList();


  mat = loadShader("frag.glsl", "vert.glsl");

  smooth();

  surface = new IsoWrap(this);

  center = new PVector(0,0,0);

  for(int i = 0 ; i < num;i++){

    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));
  
  }

  for(int i = 0 ; i < body.size();i++){
  
    Bod tmp = (Bod)body.get(i);
    surface.addPt(tmp.pos); 
  
  }

  cam = new PeasyCam(this,200);
  cam.setMinimumDistance(0.001);
  cam.setMaximumDistance(5000);
}

void draw(){

  background(0);

  fill(255,60);

  try{
    ArrayList tmp2 = new ArrayList() ;
  
    tmp2 = getData(client);

    if(tmp2!=null)

      for(int i = 0; i < tmp2.size();i++){
        Bod a = (Bod)tmp2.get(i);
        Bod b = (Bod)body.get(i);

      
        //if(dist(a.pos.x,a.pos.x,a.pos.z,b.pos.x,b.pos.y,b.pos.z) < 200.0){

        b.pos.x += (a.pos.x-b.pos.x)/SMOOTHING;
        b.pos.y += (a.pos.y-b.pos.y)/SMOOTHING;
        b.pos.z += (a.pos.z-b.pos.z)/SMOOTHING;
        
        //}
      }

    for(int i = 0 ; i < body.size();i++){
      Bod tmp = (Bod)body.get(i);
      tmp.draw();
    }

  }catch(Exception e)
  {
    ;
  }

  try{
    surface = new IsoWrap(this);

    for(int i = 0 ; i < body.size();i++){
      Bod tmp = (Bod)body.get(i);
      surface.addPt(tmp.pos); 
    }

    shader(mat);
    lights();
    fill(0,190);
    stroke(255,50);

    surface.plot();

  }catch(Exception e){
    ;
  }
}

// get data from client and parse them
//returns array of Points or null, if no data were received

ArrayList getData(Client c) {
  ArrayList pointArray;
  if (c.available() > 0) {
    String input = c.readString();
    input = input.substring(0, input.indexOf("#")); // Only up to the newline
    String[] points = split(input, '\n'); // Split values into an array
    pointArray = new ArrayList();

    for(int i=0;i<points.length;i++){
      int[] data = int(split(points[i], ';'));
      if(data.length==3){

        pointArray.add(new Bod(new PVector(
                map(data[0],-rozsah,rozsah,-100,100),
                map(data[1],-rozsah,rozsah,100,-100),
                map(data[2],-rozsah,rozsah,-100,100)
                )));
      }
    }
  }
  else {
    pointArray=null;
  }
  return pointArray;
}


class Bod{
  PVector pos;
  color c;

  Bod(PVector _pos){
    pos = _pos;
    c = color(255);
  }

  Bod(int [] data){
    pos = new PVector(data[0],data[1],data[2]);
  }

  void draw(){

    for(int i = 0 ; i < body.size();i++ ){
      Bod tmp = (Bod)body.get(i);
      float d = dist(tmp.pos.x,tmp.pos.y,tmp.pos.z,pos.x,pos.y,pos.z);
      stroke(255,map(d,0,200,25,0));
      line(tmp.pos.x,tmp.pos.y,tmp.pos.z,pos.x,pos.y,pos.z);
    }

    fill(c);
    noStroke();
  }
}
