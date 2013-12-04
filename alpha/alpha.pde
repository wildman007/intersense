

import peasy.*;

PeasyCam cam;


ArrayList body;

void setup(){

  size(1280,720,OPENGL);

  body = new ArrayList();

  for(int i = 0 ; i < 30;i++){
    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));
  }
  cam = new PeasyCam(this, 100);
  cam.setMinimumDistance(50);
  cam.setMaximumDistance(500);
}

void draw(){

  background(0);


  for(int i = 0 ; i < body.size();i++){
    Bod tmp = (Bod)body.get(i);
  }


}



class Bod{
  PVector pos;
  color c;

  Bod(PVector _pos){
    pos = _pos;
    c = color(255);
  }

  void draw(){
    fill(color);
    noStroke();
    pushMatrix();
    translate(pos.x,pos.y,pos.z);
    rect(10);
    popMatrix();

  }


}
