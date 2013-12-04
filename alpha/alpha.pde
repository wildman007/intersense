

ArrayList body;

void setup(){

  size(1280,720,OPENGL);

  body = new ArrayList();

  for(int i = 0 ; i < 30;i++){
    body.add(new Bod(new PVector(random(-100,100),random(-100,100),random(-100,100))));
  }
}

void draw(){

  background(0);




}



class Bod{
  PVector pos;

  Bod(PVector _pos){
  pos = _pos;
  }


}
