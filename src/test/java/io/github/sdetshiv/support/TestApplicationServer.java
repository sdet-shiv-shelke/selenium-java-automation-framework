package io.github.sdetshiv.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;

final class TestApplicationServer {
    private HttpServer server;

    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", this::route);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    String baseUrl() {
        if (server == null) {
            throw new IllegalStateException("Test server has not been started");
        }
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void route(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (method.equals("GET") && path.equals("/")) {
            exchange.getResponseHeaders().add("Location", "/login");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
            return;
        }

        Map<String, String> pages = Map.of(
                "/login", loginPage(),
                "/inventory", inventoryPage(),
                "/cart", cartPage(),
                "/checkout", checkoutPage(),
                "/complete", completePage()
        );

        if (method.equals("GET") && pages.containsKey(path)) {
            respond(exchange, 200, "text/html; charset=utf-8", pages.get(path));
            return;
        }

        if (method.equals("GET") && path.equals("/api/health")) {
            respond(exchange, 200, "application/json", "{\"status\":\"ok\"}");
            return;
        }

        if (method.equals("GET") && path.equals("/api/products")) {
            respond(exchange, 200, "application/json", productsJson());
            return;
        }

        if (method.equals("POST") && path.equals("/api/orders")) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (!body.matches(".*\\\"productIds\\\"\\s*:\\s*\\[\\s*\\d+.*")) {
                respond(exchange, 400, "application/json",
                        "{\"error\":\"productIds must contain at least one item\"}");
                return;
            }
            respond(exchange, 201, "application/json",
                    "{\"orderId\":\"ORD-2001\",\"status\":\"confirmed\",\"productIds\":[1,2]}");
            return;
        }

        respond(exchange, 404, "application/json", "{\"error\":\"Not found\"}");
    }

    private void respond(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.getResponseHeaders().add("Cache-Control", "no-store");
        exchange.getResponseHeaders().add("X-Content-Type-Options", "nosniff");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private String layout(String title, String content, String script) {
        return """
                <!doctype html>
                <html lang="en">
                  <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>%s</title>
                    <style>
                      :root { font-family: system-ui, sans-serif; color: #172033; background: #f5f7fb; }
                      body { margin: 0; }
                      header { display:flex; justify-content:space-between; padding:1rem 2rem; background:#172033; color:white; }
                      header a { color:white; }
                      main { max-width:900px; margin:2rem auto; padding:0 1rem; }
                      form,.card,.summary { background:white; border:1px solid #dce2ed; border-radius:10px; padding:1rem; margin:1rem 0; }
                      label { display:block; margin:.75rem 0 .25rem; font-weight:600; }
                      input { width:min(100%%,420px); padding:.7rem; border:1px solid #8a96aa; border-radius:6px; }
                      button,.button { display:inline-block; margin-top:.75rem; padding:.7rem 1rem; border:0; border-radius:6px; background:#2457d6; color:white; text-decoration:none; cursor:pointer; }
                      .products { display:grid; grid-template-columns:repeat(auto-fit,minmax(230px,1fr)); gap:1rem; }
                      .error { color:#b42318; font-weight:600; }
                      .success { color:#147d3f; }
                    </style>
                  </head>
                  <body>%s<script>%s</script></body>
                </html>
                """.formatted(title, content, script);
    }

    private String loginPage() {
        return layout("Portfolio Shop | Login", """
                <main>
                  <h1>Portfolio Shop Login</h1>
                  <form id="login-form">
                    <label for="username">Email</label>
                    <input id="username" name="username" type="email" required>
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" required>
                    <button data-testid="login-button" type="submit">Sign in</button>
                    <p id="login-error" class="error" role="alert" hidden></p>
                  </form>
                </main>
                """, """
                document.querySelector('#login-form').addEventListener('submit', event => {
                  event.preventDefault();
                  const email = document.querySelector('#username').value;
                  const password = document.querySelector('#password').value;
                  if (email === 'test.user@example.com' && password === 'Password123!') {
                    localStorage.setItem('portfolio-user', email);
                    location.assign('/inventory');
                  } else {
                    const error = document.querySelector('#login-error');
                    error.textContent = 'Invalid email or password';
                    error.hidden = false;
                  }
                });
                """);
    }

    private String inventoryPage() {
        return layout("Portfolio Shop | Products", """
                <header><strong>Portfolio Shop</strong><a data-testid="cart-link" href="/cart">Cart (<span data-testid="cart-count">0</span>)</a></header>
                <main><h1>Products</h1><section class="products">
                  <article class="card" data-testid="product-card"><h2>Quality Engineer Backpack</h2><p>$49.99</p><button data-testid="add-to-cart" data-product-id="1">Add to cart</button></article>
                  <article class="card" data-testid="product-card"><h2>Automation Testing Toolkit</h2><p>$29.99</p><button data-testid="add-to-cart" data-product-id="2">Add to cart</button></article>
                  <article class="card" data-testid="product-card"><h2>API Testing Handbook</h2><p>$19.99</p><button data-testid="add-to-cart" data-product-id="3">Add to cart</button></article>
                </section></main>
                """, """
                const cart = JSON.parse(localStorage.getItem('portfolio-cart') || '[]');
                const update = () => document.querySelector('[data-testid="cart-count"]').textContent = String(cart.length);
                update();
                document.querySelectorAll('[data-testid="add-to-cart"]').forEach(button => button.addEventListener('click', () => {
                  const id = Number(button.dataset.productId);
                  if (!cart.includes(id)) cart.push(id);
                  localStorage.setItem('portfolio-cart', JSON.stringify(cart));
                  update();
                }));
                """);
    }

    private String cartPage() {
        return layout("Portfolio Shop | Cart", """
                <header><strong>Portfolio Shop</strong><a href="/inventory">Continue shopping</a></header>
                <main><h1>Your cart</h1><section data-testid="cart-items"></section><a class="button" data-testid="checkout-link" href="/checkout">Checkout</a></main>
                """, """
                const products = [{id:1,name:'Quality Engineer Backpack',price:49.99},{id:2,name:'Automation Testing Toolkit',price:29.99},{id:3,name:'API Testing Handbook',price:19.99}];
                const cart = JSON.parse(localStorage.getItem('portfolio-cart') || '[]');
                const selected = products.filter(product => cart.includes(product.id));
                document.querySelector('[data-testid="cart-items"]').innerHTML = selected.length
                  ? selected.map(product => '<article class="card" data-testid="cart-item"><h2>' + product.name + '</h2><p>$' + product.price.toFixed(2) + '</p></article>').join('')
                  : '<p>Your cart is empty.</p>';
                """);
    }

    private String checkoutPage() {
        return layout("Portfolio Shop | Checkout", """
                <header><strong>Portfolio Shop</strong><a href="/cart">Back to cart</a></header>
                <main><h1>Checkout</h1>
                  <form id="checkout-form">
                    <label for="first-name">First name</label><input id="first-name" required>
                    <label for="last-name">Last name</label><input id="last-name" required>
                    <label for="postal-code">Postal code</label><input id="postal-code" required>
                    <button data-testid="review-order" type="submit">Review order</button>
                  </form>
                  <section class="summary" data-testid="order-summary" hidden><h2>Order summary</h2><button data-testid="place-order">Place order</button></section>
                </main>
                """, """
                document.querySelector('#checkout-form').addEventListener('submit', event => {
                  event.preventDefault();
                  document.querySelector('[data-testid="order-summary"]').hidden = false;
                });
                document.querySelector('[data-testid="place-order"]').addEventListener('click', async () => {
                  const productIds = JSON.parse(localStorage.getItem('portfolio-cart') || '[]');
                  const response = await fetch('/api/orders', {method:'POST',headers:{'content-type':'application/json'},body:JSON.stringify({productIds})});
                  if (response.ok) location.assign('/complete');
                });
                """);
    }

    private String completePage() {
        return layout("Portfolio Shop | Complete", """
                <main><h1 class="success">Order confirmed</h1><p data-testid="confirmation-message">Thank you. Your test order has been created successfully.</p><a class="button" href="/inventory">Back to products</a></main>
                """, "localStorage.removeItem('portfolio-cart');");
    }

    private String productsJson() {
        return """
                {"products":[
                  {"id":1,"name":"Quality Engineer Backpack","price":49.99},
                  {"id":2,"name":"Automation Testing Toolkit","price":29.99},
                  {"id":3,"name":"API Testing Handbook","price":19.99}
                ]}
                """;
    }
}
