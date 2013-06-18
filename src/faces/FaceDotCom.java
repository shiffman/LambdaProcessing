package faces;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import httprocessing.PostRequest;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class FaceDotCom {

	String api_key = "";
	String api_secret = "";

	PApplet p5;

	public static final String api = "http://api.face.com/faces/detect.json";

	public FaceDotCom(PApplet p5_, String apiKey_, String apiSec_) {
		p5 = p5_;
		api_key = apiKey_;
		api_secret = apiSec_;
	}

	public Face[] detectFacesURL(String url) {

		PostRequest post = new PostRequest(api);
		post.addData("api_key", api_key);
		post.addData("api_secret", api_secret);
		post.addData("urls",url);
		post.send();
		String content = post.getContent();
		//System.out.println("Reponse Content: " + content);
		//System.out.println("Reponse Content-Length Header: " + post.getHeader("Content-Length"));
		return facesFromJSON(content);
	}

	public Face[] detectFacesPath(String path) {
		File f = new File(path);
		long size = f.length();
		System.out.println("File size: " + size/1000000.0f);

		int maxsize = 1000000;

		if (size > maxsize) {
			String tempPath = "temp/temp.jpg";
			PImage img = p5.loadImage(path);

			float ratio = size/(float)maxsize;
			int w = (int) (img.width/ratio);
			int h = (int) (img.height/ratio);
			img.resize(w,h);
			System.out.println("Too big, resizing to: " + w + "," + h);
			img.save(tempPath);
			return detectFacesPath(tempPath);
		} else {
			return detectFaces(f);
		}
	}

	public Face[] detectFaces(File f) {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", api_key);
		post.addData("api_secret", api_secret);

		post.addFile("image",f);
		post.send();
		String content = post.getContent();
		System.out.println("Reponse Content: " + content);
		System.out.println("Reponse Content-Length Header: " + post.getHeader("Content-Length"));
		return facesFromJSON(content);
	}


	public Face[] facesFromJSON(String content) {
		try {
			JSONObject data = new JSONObject(content);


			JSONArray photos = data.getJSONArray("photos");
			//System.out.println("Photos: " + photos.toString());

			// Assuming just one URL at a time right now
			JSONArray tags = photos.getJSONObject(0).getJSONArray("tags");
			//System.out.println("Tags: " + tags.toString());

			Face[] faces = new Face[tags.length()];
			for (int i = 0; i < faces.length; i++) {
				faces[i] = new Face();
				faces[i].fromJSON(tags.getJSONObject(i));
			}
			return faces;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}







}
