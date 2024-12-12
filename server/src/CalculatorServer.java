import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class CalculatorServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/calculate", new CalculatorHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server is started on http://localhost:8000/calculate");


                }
    static class CalculatorHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            if("POST".equals(exchange.getRequestMethod())){
                String request = new String(exchange.getRequestBody().readAllBytes());
                double result = evaluateExpression(request);

                String response = String.valueOf(result);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();


            }else{
                exchange.sendResponseHeaders(405,-1);

            }
        }

        private double evaluateExpression(String expression) {
            try {
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
}
