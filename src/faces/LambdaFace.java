package faces;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class LambdaFace {

	String mashape_key = "";

	PApplet p5;

	public static final String api = "https://lambda-face-recognition.p.mashape.com/detect";

	public LambdaFace(PApplet p5_, String key) {
		p5 = p5_;
		mashape_key = key;
	}

	public Face[] detectFacesURL(String url) {
		HttpResponse<JsonNode> request = Unirest.post(api)
				  .header("X-Mashape-Authorization", mashape_key)
				  .field("urls", url)
				  .asJson();
		JsonNode json = request.getBody();
		String content = json.toString();
		return facesFromJSON(content);
	}
	
	public Face[] detectFaces(File f) {
		HttpResponse<JsonNode> request = Unirest.post(api)
				  .header("X-Mashape-Authorization", mashape_key)
				  .field("files", f)
				  .field("urls", "")
				  .asJson();

		JsonNode json = request.getBody();
		String content = json.toString();
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



	public Face[] facesFromJSON(String content) {
		try {
			JSONObject data = new JSONObject(content);


			JSONArray photos = data.getJSONArray("photos");
			
			// Only supporting one photo at a time right now
			JSONObject photo = photos.getJSONObject(0);

			int width = photo.getInt("width");
			int height = photo.getInt("height");
			System.out.println("Width: " + width + "  height: " + height);

			JSONArray tags = photo.getJSONArray("tags");
			
			Face[] faces = new Face[tags.length()];
			for (int i = 0; i < faces.length; i++) {
				faces[i] = new Face(width,height);
				faces[i].fromJSON(tags.getJSONObject(i));
			}
			return faces;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}







}
