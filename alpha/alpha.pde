
import peasy.*;

PeasyCam cam;

ArrayList body;

void setup(){

  size(1280,720,OPENGL);

  body = new ArrayList();

  for(int i = 0 ; i < 30;i++){
    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));
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


}



class Bod{
  PVector pos;
  color c;

  Bod(PVector _pos){
    pos = _pos;
    c = color(255);
  }

  void draw(){
    fill(c);
    noStroke();
    pushMatrix();
    translate(pos.x,pos.y,pos.z);
    box(1);
    popMatrix();

  }


}
