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

IsoSurface surface;


PeasyCam cam;
ArrayList body,vec;

void setup(){

  size(1280,720,OPENGL);

  body = new ArrayList();
  vec = new ArrayList();

  surface = new IsoSurface(this,new PVector(-100,-100,-100), new PVector(100,100,100), 16);


  for(int i = 0 ; i < 30;i++){
    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));

  }

  for(int i = 0 ; i < body.size();i++){
    Bod tmp = (Bod)body.get(i);
    surface.addPoint(tmp.pos); 
  }

  cam = new PeasyCam(this, 200);
  cam.setMinimumDistance(50);
  cam.setMaximumDistance(500);
}

void draw(){

  background(0);

  rotateY(frameCount/1000.0);

  for(int i = 0 ; i < body.size();i++){
    Bod tmp = (Bod)body.get(i);
    tmp.draw();
  }

  fill(255,60);
  surface.plot(mouseX/100000.0);


}

class Bod{
  PVector pos;
  color c;

  Bod(PVector _pos){
    pos = _pos;
    c = color(255);
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
