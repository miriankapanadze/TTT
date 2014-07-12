package edu.freeuni.tictactoe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		start();
	}
	
	private void start(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection con = null;
				try {
					System.out.println("try");
					URL url = new URL("http://192.168.77.74:8080/tictactoe");
					System.out.println("url");
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("POST");
//					int status = urlConnection.getResponseCode();

//					if (status == 200) {
//						urlConnection.connect();
						System.out.println("urlCon");
						con.setDoOutput(true);
						System.out.println("dpt");
						
						OutputStreamWriter wr = new OutputStreamWriter(
								con.getOutputStream());
						System.out.println("wr");
						wr.write("aaaaaaaaa");
						System.out.println("write");
						wr.close();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						 
                        String returnString="";
                        double doubledValue =0;

                        while ((returnString = in.readLine()) != null) 
                        {
                            doubledValue= Integer.parseInt(returnString);
                        }
                        in.close();
//					} else {
//						System.out.println("status != 200");
//					}
						
					
				} catch (Exception ex) {
					System.out.println("errror");
					Log.e("eerroro", "");
				} finally {
					if (con != null)
						con.disconnect();
				}
			}

		}).start();
	}

	@Override
	protected void onResume() {
		start();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}