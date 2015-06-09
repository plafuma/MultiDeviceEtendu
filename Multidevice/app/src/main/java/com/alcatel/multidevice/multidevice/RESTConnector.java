package com.alcatel.multidevice.multidevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;



public class RESTConnector {
	private static final String TAG = RESTConnector.class.getSimpleName();

	private static String SERVER_URL = "https://ot2.sqanet.fr";
	private static final String AUTH_PATH = "/api/rest/authenticate?version=1.0";
	

	public RESTConnector() {
		SERVER_URL = MainActivity.ser;
	}


	public String authenticate() {
		URL url;
		String ret = "";
		HttpURLConnection connection = null;

		try {
			url = new URL(SERVER_URL + AUTH_PATH);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);

			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return ret;

	}

    public String lastCallRef(){
        URL url;
        String ret = "";
        HttpURLConnection connection = null;

        try {
            url = new URL(SERVER_URL + "/api/rest/1.0/telephony/calls");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.v(TAG, "Response code is: " + responseCode);

            if (responseCode == 200) {

                // Read the response JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }

                Log.v(TAG, builder.toString());

                ret = builder.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return ret;

    }

    public String transfer(String callRef, String transferTo, boolean anonymous, boolean bypass) {
        URL url;
        String ret = "";
        HttpPost connection = null;
        String postMessage = "{ \"transferTo\": \""+transferTo+"\", \"anonymous\": "+anonymous+", \"bypass\": "+bypass+" }";
        HttpClient client = new DefaultHttpClient();
        try {
            url = new URL(SERVER_URL + "/api/rest/1.0/telephony/calls/"+callRef+"/blindtransfer");

//            connection = url.openConnection();
//            connection.setEntity(new ByteArrayEntity(postMessage.toString().getBytes("UTF8")));
//            connection.setRequestMethod("POST");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.connect();
                connection = new HttpPost(url.toString());


            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response;
            JSONObject json = new JSONObject();
            try{
                json.put("transferTo",transferTo);
                json.put("anonymous", anonymous);
                json.put("bypass", bypass);
            }
            catch(Exception e)
            {

            }

                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                connection.setEntity(se);
                response = client.execute(connection);

                int responseCode = Integer.parseInt(response.getHeaders("code").toString());

            Log.v(TAG, "Response code is: " + responseCode);

            if (responseCode == 200) {

                // Read the response JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }

                Log.v(TAG, builder.toString());

                ret = builder.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            connection.abort();
        }
        return ret;

    }
	
	public String openSession() {
		URL url;
		String ret = "";
		HttpURLConnection connection = null;
		

		try {
			url = new URL(SERVER_URL + "/api/rest/1.0/sessions");
			
				ret="ok";
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);	
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("POST");
				
				// on crée un objet json
				JSONObject appName = new JSONObject();
				try {
					appName.put("applicationName", "TESTS_API"); //on ajoute l'attribut
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
				wr.write(appName.toString()); // on ajoute l'objet json à la requête
				wr.flush();
			
			

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);
			Log.v(TAG, url+" " + connection.getResponseMessage()+ " " + CookieHandler.getDefault().toString());

			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return ret;
	}
	
	/**
	 * Récupère les profils de routage de l'utilisateur connecté
	 * @return json response
	 */
	public String testUser() {
		URL url;
		String ret = "";
		HttpURLConnection connection = null;
		

		try {
			url = new URL(SERVER_URL + "/api/rest/1.0/routing/profiles");
			
				ret="ok";
				connection = (HttpURLConnection) url.openConnection();
				
			
			

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);
			Log.v(TAG, url+" " + connection.getResponseMessage()+ " " +CookieHandler.getDefault().toString());
			
			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return ret;
	}
	
	/**
	 * Modifie les informations d'un profil de routage
	 * @return json response
	 */
	public String modifProfil() {
		
		/*URL url;
		String ret = "";
		HttpURLConnection connection = null;
		

		try {
			url = new URL(SERVER_URL + "/api/rest/1.0/routing/profiles/0");
			
				ret="ok";
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);	
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("POST");
				
				// on crée un objet json
				JSONObject profileRequest = new JSONObject();
				JSONObject presentationRoutes = new JSONObject();
				JSONArray destinations = new JSONArray();
				JSONObject type = new JSONObject();
				JSONArray array1 = new JSONArray();
				try {
						
					
					//type.put("type", "OTHER");
					//destinations.put(type);
					//array1.put(destinations);
					//profileRequest.put("presentationRoutes", presentationRoutes);
					profileRequest.put("profileName", "TOTO");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			
				OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
				wr.write(profileRequest.toString()); // on ajoute l'objet json à la requête
				wr.flush();
			
			

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);
			Log.v(TAG, profileRequest.toString());
			Log.v(TAG, url+" " + connection.getResponseMessage()+ " " +CookieHandler.getDefault().toString());
			
			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return ret;*/
		
		String idProfil = "0"; // profil par défaut
		String res = "";
		try {

	        URL url = new URL(SERVER_URL + "/api/rest/1.0/routing/profiles/"+idProfil);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);	
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestMethod("POST");
	        String bool = "true";
	       // String input = "{\"profileName\": \"TOTO\",\"presentationRoutes\": [ {\"destinations\": [ {\"type\": OTHER,\"deviceId\": \"toast\",\"number\": \"toast\",\"acceptable\": "  + bool + ",\"selected\": "  + bool + ",\"information\": {\"phoneNumber\": \"toast\",\"loginName\": \"toast\",\"lastName\": \"toast\",\"firstName\": \"toast\",\"email\": \"toast\"}} ]} ],\"forwardRoutes\": [ {\"overflowType\": BUSY,\"destinations\": [ {\"type\": OTHER,\"deviceId\": \"toast\",\"number\": \"toast\",\"acceptable\": "  + bool + ",\"selected\": "  + bool + ",\"information\": {\"phoneNumber\": \"toast\",\"loginName\": \"toast\",\"lastName\": \"toast\",\"firstName\": \"toast\",\"email\": \"toast\"}} ]} ],\"currentDeviceId\": \"3191\"}";
	        String input2 = "{ \"profileName\": \"TOTO\", \"currentDeviceId\": \"3191\" }";
	        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
			wr.write(input2.toString()); // on ajoute l'objet json à la requête
			wr.flush();
			
			
			int responseCode = conn.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);
			Log.v(TAG, input2);

	        /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                + conn.getResponseCode());
	        }*/

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));

	        String output;
	        
	        System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	            res+=output;
	        }

	        conn.disconnect();

	      } catch (MalformedURLException e) {

	        e.printStackTrace();

	      } catch (IOException e) {

	        e.printStackTrace();

	     }
		
		
		return res;
		
	}
	
	
	public String closeSession() {
		URL url;
		String ret = "";
		HttpURLConnection connection = null;
		

		try {
			url = new URL(SERVER_URL + "/api/rest/1.0/sessions");
			
				ret="ok";
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);	
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("DELETE");
				
				
			
			

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);
			Log.v(TAG, url+" " + connection.getResponseMessage()+ " " + CookieHandler.getDefault().toString());

			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return ret;
	}
	
	
}
