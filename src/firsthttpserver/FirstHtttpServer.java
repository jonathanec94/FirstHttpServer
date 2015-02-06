package firsthttpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author Lars Mortensen
 */
public class FirstHtttpServer {

    static int port = 8081;
    static String ip = "127.0.0.1";
    static String contentFolder = "public/";

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/welcome", new RequestHandler());
        // server.createContext("/files", new SimpleFileHandler());
        server.createContext("/headers", new RequestHandlerHeaders());
        server.createContext("/pages", new RequestHandlerPages());
        server.createContext("/parameters", new RequestHandlerParameters());

        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started, listening on port: " + port);
    }

    static class RequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response;
            //String response = "<h1>Welcome to my very first almost home made Web Server :-)</h1>";
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<h2>Welcome to my very first home made Web Server :-)</h2>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();

            Scanner scan = new Scanner(he.getRequestBody());
            while (scan.hasNext()) {
                response += "<br/>From Post form: " + scan.nextLine();
            }
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response); //What happens if we use a println instead of print --> Explain
            }
        }
    }
    /*
     static class SimpleFileHandler implements HttpHandler {
     @Override
     public void handle(HttpExchange he) throws IOException {
     File file = new File(contentFolder + "index.html");
     byte[] bytesToSend = new byte[(int) file.length()];
     try {
     BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
     bis.read(bytesToSend, 0, bytesToSend.length);
     } catch (IOException ie) {
     System.out.println(ie);
     }
     he.sendResponseHeaders(200, bytesToSend.length);
     try (OutputStream os = he.getResponseBody()) {
     os.write(bytesToSend, 0, bytesToSend.length);
     }
     }
     }
      
     */

    static class RequestHandlerHeaders implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response;

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>Opgave 2</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<table border='1'><tr><td>Header</td> <td>Value</td></tr>");

            for (String key : he.getRequestHeaders().keySet()) {

                sb.append("<tr>");
                sb.append("<td>");
                sb.append(key);
                sb.append("</td>");

                sb.append("<td>");
                sb.append(he.getRequestHeaders().get(key));
                sb.append("</td>");

                sb.append("</tr>");

            }
            sb.append("</table>");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();

            Scanner scan = new Scanner(he.getRequestBody());
            while (scan.hasNext()) {
                response += "<br /> post paramter " + scan.nextLine();
            }

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response); //What happens if we use a println instead of print --> Explain
            }
        }
    }

    static class RequestHandlerPages implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String contentFolder = "public/";

            File file = new File(contentFolder + "index.html");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }

            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }
    }

    static class RequestHandlerParameters implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response;
            String metode = he.getRequestMethod();
            StringBuilder sb = new StringBuilder();

            String str = "";

            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>Opgave 2</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            if (metode.equalsIgnoreCase("post")) {
                Scanner scan = new Scanner(he.getRequestBody());

                while (scan.hasNext()) {
                    str = scan.nextLine();
                }
            }
            else
            {
                str += he.getRequestURI();
                System.out.println(str);
            }
            sb.append("<h4> Metode " + metode + " <br /></h4>");
            String arr[] = str.split("&");
           if(metode.equalsIgnoreCase("GET"))
           {
                arr[0] = arr[0].substring(12);
           }
          
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i].toString() + " <br />");
            }

            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response); //What happens if we use a println instead of print --> Explain
            }
        }
    }

}
