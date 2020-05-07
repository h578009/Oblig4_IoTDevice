package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;

import okhttp3.*;


public class RestClient {
	
	
	public RestClient() {
		// TODO Auto-generated constructor stub
		
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		
		try(Socket s = new Socket(Configuration.host, Configuration.port)){
			Gson gson = new Gson();
			AccessMessage m = new AccessMessage(message);
			String jsonbody = gson.toJson(m, AccessMessage.class);

			String httppostrequest = 
					"POST " + logpath + " HTTP/1.1\r\n" + 
			        "Host: " + Configuration.host + "\r\n" +
					"Content-type: application/json\r\n" + 
			        "Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + 
			        "\r\n" + 
					jsonbody + 
					"\r\n";
			
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httppostrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					
				} else {
					jsonresponse.append(nextline);
				}

				if (nextline.isEmpty()) {
					header = false;
				}

			}

			scan.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {
		
		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
	try(Socket s = new Socket(Configuration.host, Configuration.port)){
		String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
			+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";
		OutputStream output = s.getOutputStream();

		PrintWriter pw = new PrintWriter(output, false);

		pw.print(httpgetrequest);
		pw.flush();

		// read the HTTP response
		InputStream in = s.getInputStream();

		Scanner scan = new Scanner(in);
		StringBuilder jsonresponse = new StringBuilder();
		boolean header = true;

		while (scan.hasNext()) {

			String nextline = scan.nextLine();

			if (header) {
			} else {
				jsonresponse.append(nextline);
			}

			// simplified approach to identifying start of body: the empty line
			if (nextline.isEmpty()) {
				header = false;
			}

		}

		
		Gson gson = new Gson();
		
		code =  gson.fromJson(jsonresponse.toString(), AccessCode.class);

		scan.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return code;
	}
}
