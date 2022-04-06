package org.project.BusinessObjects;


import com.google.gson.Gson;
import org.project.DTO.Staff;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }

    public void start()
    {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket
            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort() );

            System.out.println("Client message: The Client is running and has connected to the server");

            System.out.println("Please enter a command:  (\"Time\" to get time, or \"Echo message\" to get echo, or \"Triple number\" to get triple) \n>");
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers

            socketWriter.println(command);

            Scanner socketReader = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply

            boolean contd = true;
            while(contd) {
                if (command.startsWith("Time"))   //we expect the server to return a time
                {
                    String timeString = socketReader.nextLine();
                    System.out.println("Client message: Response from server Time: " + timeString);
                }                           // the user has entered the Echo command or an invalid command
                else  if(command.startsWith("DisplayAll")){
                    Gson gsonParser = new Gson();
                    String input = socketReader.nextLine();
                    Staff[] staffArr = gsonParser.fromJson(input, Staff[].class);
                    System.out.println("Client message: Response from server -> Staff array : ");
                    for(Staff s : staffArr)
                        System.out.println(s);

                }
                else if(command.startsWith("DisplayById")){
                    Gson gsonParser = new Gson();
                    String input = socketReader.nextLine();
                    Staff outputClass = gsonParser.fromJson(input, Staff.class);
                    String output = outputClass.toString();
                    System.out.println("Client message: Response from server: ");
                    System.out.println(output);
                }
                else
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: \"" + input + "\"");
                }

                System.out.println("Please enter a command:  (\"Time\" to get time, or \"Echo message\" to get echo, or \"Triple number\" to get triple) \n>");
                command = in.nextLine();
                socketWriter.println(command); //println very important - otherwise takes in all characters before \n


            }
            socketWriter.close();
            socketReader.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client message: IOException: "+e);
        }
    }
}


