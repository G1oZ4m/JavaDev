import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class CalculatorServer {
    public static void main(String[] args) throws IOException {

        // Simple HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/", new GetHandler());
        server.createContext("/calculate", new PostHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server is started on http://localhost:8001/calculate");


                }

    static class GetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String htmlResponse = "<html lang ='en'" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Document</title>" +
                    "</head>" +
                    "<body>" +
                    "<input id=\"display\" readonly>" +
                    "<div id='keys'>" +
                    "<button onclick='appendToDisplay('+')' class='operator-btn'>+</button>" +
                    "<button onclick='appendToDisplay('7')'>7</button>" +
                    "<button onclick='appendToDisplay('8')'>8</button>" +
                    "<button onclick='appendToDisplay('9')'>9</button>" +
                    "<button onclick='appendToDisplay('-')' class='operator-btn'>-</button>" +
                    "<button onclick='appendToDisplay('4')'>4</button>" +
                    "<button onclick='appendToDisplay('5')'>5</button>" +
                    "<button onclick='appendToDisplay('6')'>6</button>" +
                    "<button onclick='appendToDisplay('*')' class='operator-btn'>*</button>" +
                    "<button onclick='appendToDisplay('1')'>1</button>" +
                    "<button onclick='appendToDisplay('2')'>2</button>" +
                    "<button onclick='appendToDisplay('3')'>3</button>" +
                    "<button onclick='appendToDisplay('/')' class='operator-btn'>/</button>" +
                    "<button onclick='appendToDisplay('0')'>0</button>" +
                    "<button onclick='appendToDisplay('.')'>.</button>" +
                    "<button onclick='calculate()'>=</button>" +
                    "<button onclick='clearDisplay()' class='operator-btn'>C</button>" +
                    "</div>" +
                    "<script src='/script.js'></script>" +
                    "</body>" +
                    "</html>";

            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(htmlResponse.getBytes());
            os.close();
        }
    }
    static class PostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Обработка POST запроса
            InputStream inputStream = exchange.getRequestBody();
            String expression = new String(inputStream.readAllBytes());
            double result = evaluateExpression(expression);

            String response = String.valueOf(result);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }
        private static double evaluateExpression(String expression) {
        System.out.println("Received expression: " + expression);

        try {
            // Используем ScriptEngine для выполнения выражений на JavaScript
            Object result = new ScriptEngineManager()
                    .getEngineByName("JavaScript")
                    .eval(expression);
            return result instanceof Number ? ((Number) result).doubleValue() : 0;

        } catch (Exception e) {
            System.err.println("Error in calculation: " + e.getMessage());
            return 0;
        }

    }
}
