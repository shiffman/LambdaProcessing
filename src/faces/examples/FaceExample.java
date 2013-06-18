package faces.examples;

import java.io.File;

import faces.Face;
import faces.FaceDotCom;
import processing.core.PApplet;
import processing.core.PImage;

public class FaceExample extends PApplet {

	
	String path = "data/test.jpg";
	PImage img;
	Face[] faces;
	
	public void setup() {
		size(800,600);
		smooth();
		FaceDotCom face = new FaceDotCom(this,"","");
		img = loadImage(path);
		faces = face.detectFacesPath(path);
	}
	
	public void draw() {
		background(0);
		
		float w = mouseX;// width;
		float h = mouseY;//img.height*(w/img.width);
		
		image(img,0,0,w,h);
		for (int i = 0; i < faces.length; i++) {
			faces[i].scale(w,h);
			stroke(255,0,0);
			strokeWeight(1);
			noFill();
			rectMode(CENTER);
			rect(faces[i].center.x,faces[i].center.y,faces[i].w,faces[i].h);
			rect(faces[i].eye_right.x,faces[i].eye_right.y,4,4);
			rect(faces[i].eye_left.x,faces[i].eye_left.y,4,4);
			rect(faces[i].mouth_left.x,faces[i].mouth_left.y,4,4);
			rect(faces[i].mouth_center.x,faces[i].mouth_center.y,4,4);
			rect(faces[i].mouth_right.x,faces[i].mouth_right.y,4,4);
			rect(faces[i].nose.x,faces[i].nose.y,4,4);
		}
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] {FaceExample.class.getName()});
	}
	
}
