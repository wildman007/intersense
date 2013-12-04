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

float rozsah = 2600;

IsoSurface surface;

Client client;
String input;


PeasyCam cam;
ArrayList body,vec;

void setup(){

  size(1280,720,OPENGL);

  client = new Client(this, "192.168.23.45", 12345);

  body = new ArrayList();
  vec = new ArrayList();

  surface = new IsoSurface(this,new PVector(-200,-200,-200), new PVector(200,200,200), 32);


  for(int i = 0 ; i < 30;i++){
    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));

  }

  for(int i = 0 ; i < body.size();i++){
    Bod tmp = (Bod)body.get(i);
    surface.addPoint(tmp.pos); 
  }

  cam = new PeasyCam(this, 200);
  cam.setMinimumDistance(20);
  cam.setMaximumDistance(500);
}

void draw(){

  background(0);

  rotateY(frameCount/1000.0);

  fill(255,60);




  // Receive data from server
  try{
    body = getData(client);
  
    surface = new IsoSurface(this,new PVector(-200,-200,-200), new PVector(200,200,200), 32);
    surface.plot(mouseX/100000.0);

    for(int i = 0 ; i < body.size();i++){
      Bod tmp = (Bod)body.get(i);
      tmp.draw();
    }


  }catch(Exception e){;}

}



//get data from client and parse them
//returns array of Points or null, if no data were received
ArrayList getData(Client c) {
  ArrayList pointArray;
  if (c.available() > 0) {
    String input = c.readString();
    input = input.substring(0, input.indexOf("#")); // Only up to the newline
    String[] points = split(input, '\n'); // Split values into an array
    pointArray = new ArrayList();

    for(int i=0;i<points.length;i++){
      //   println("P: "+points[i]);
      int[] data = int(split(points[i], ';')); // Split values into an array
      if(data.length==3){
        
        pointArray.add(new Bod(new PVector(
                map(data[0],-rozsah,rozsah,-100,100),
                map(data[1],-rozsah,rozsah,-100,100),
                map(data[2],-rozsah,rozsah,-100,100)
              )));
        println(data);
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
    pushMatrix();
    translate(pos.x,pos.y,pos.z);
    box(1);
    popMatrix();
  }
}
