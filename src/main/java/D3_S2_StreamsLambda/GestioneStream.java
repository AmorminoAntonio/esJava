package D3_S2_StreamsLambda;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestioneStream {
  private static List<Product> productList = new ArrayList<>();
  private static List<Customer> customerList = new ArrayList<>();
  private static List<Order> orderList = new ArrayList<>();
  private static Map<Customer, Double> totaleVendite = new HashMap<>();

  public static void main(String[] args) throws IOException {

    // esempi del giorno 4 ----> gestione dependecies & uso common IO.

    //        Faker faker = new Faker();
    //        Logger log = LoggerFactory.getLogger(GestioneStream.class);
    //        System.out.println(writeFile(writeFile("ciao come state paparazzi?")));

    // Inizializzo i dati
    createProductList();
    createCustomerList();
    createOrderList();

    /*stampaMapOrders();
    stampaTotaleVenditePerCliente();*/
    stampaProdottiPiuCostosi();

    /*    System.out.println("Products");
    productList.forEach(System.out::println);
    System.out.println("Customer");
    customerList.forEach(System.out::println);
    System.out.println("Order");
    orderList.forEach(System.out::println);

    System.out.println("--- Esercizio 1 ---");
    getBooks().forEach(System.out::println);

    System.out.println("--- Esercizio 2 ---");
    getBabyOrders().forEach(System.out::println);

    System.out.println("--- Esercizio 3 ---");
    getBoys().forEach(System.out::println);

    System.out.println("--- Esercizio 4 ---");
    getTierProducts().forEach(System.out::println);*/
  }

  public static List<Product> getBooks() {
    return productList.stream()
        .filter(product -> product.getCategory().equals("Books") && product.getPrice() > 100)
        .collect(Collectors.toList());
  }

  public static List<Order> getBabyOrders() {
    return orderList.stream()
        .filter(order -> order.getProducts().stream().anyMatch(product -> product.getCategory().equals("Baby")))
        .toList();
  }

  public static List<Product> getBoys() {
    return productList.stream()
        .filter(p -> p.getCategory().equals("Boys"))
        .peek(product -> product.setPrice((int) (product.getPrice() * 0.90)))
        .toList();
  }

  public static List<Product> getTierProducts() {
    List<Order> filterOrder =
        orderList.stream()
            .filter(
                order ->
                    order.getCustomer().getTier() == 2
                        && order.getOrderDate().isBefore(LocalDate.parse("2025-01-20"))
                        && order.getOrderDate().isAfter(LocalDate.parse("2025-01-01")))
            .toList();

    List<Product> products = new ArrayList<>();
    for (Order order : filterOrder) {
      products.addAll(order.getProducts());
    }
    return products;
  }

  public static void createProductList() {
    // Books - Baby - Boys
    Product p1 = new Product(1, "Iphone", "Smartphone", 1000);
    Product p2 = new Product(2, "ABC", "Books", 127);
    Product p3 = new Product(3, "Pannolini", "Baby", 5);
    Product p4 = new Product(4, "Il Signore Degli Anelli", "Books", 31);
    Product p5 = new Product(5, "Spiderman", "Boys", 100);
    Product p6 = new Product(6, "Ciuccio", "Baby", 2);
    productList.addAll(Arrays.asList(p1, p2, p3, p4, p5, p6));
  }

  public static void createCustomerList() {
    Customer c1 = new Customer(1, "Aldo Baglio", 1);
    Customer c2 = new Customer(2, "Giovanni Storti", 2);
    Customer c3 = new Customer(3, "Giacomo Poretti", 3);
    Customer c4 = new Customer(4, "Marina Massironi", 2);
    customerList.addAll(Arrays.asList(c1, c2, c3, c4));
  }

  public static void createOrderList() {
    Order o1 = new Order(1, customerList.get(0));
    Order o2 = new Order(2, customerList.get(1));
    Order o3 = new Order(3, customerList.get(2));
    Order o4 = new Order(4, customerList.get(3));
    Order o5 = new Order(3, customerList.get(2));

    Product p1 = productList.get(0);
    Product p2 = productList.get(1);
    Product p3 = productList.get(2);
    Product p4 = productList.get(3);
    Product p5 = productList.get(4);
    Product p6 = productList.get(5);

    o1.addProduct(p1);
    o1.addProduct(p3);
    o1.addProduct(p5);
    o2.addProduct(p1);
    o2.addProduct(p4);
    o3.addProduct(p2);
    o3.addProduct(p4);
    o3.addProduct(p3);
    o3.addProduct(p6);
    o4.addProduct(p2);
    o4.addProduct(p6);
    o5.addProduct(p1);
    o5.addProduct(p2);
    o5.addProduct(p4);
    orderList.addAll(Arrays.asList(o1, o2, o3, o4, o5));
  }

  // mi pesco gli ordini legati al cliente (creo la mappa)
  public static Map<Customer, List<Order>> mapOrdiniClienti() {
    return orderList.stream().collect(Collectors.groupingBy(Order::getCustomer));
  }

  // mi stampo la mappa creata qui sopra
  public static void stampaMapOrders() {
    mapOrdiniClienti()
        .forEach(
            (customer, orders) -> {
              System.out.println("Customer: " + customer.getName());
              orders.forEach(System.out::println);
            });
  }

  // esercizio 2
  public static Map<Customer, Double> calcolaTotaleVenditePerCliente() {
    for (Order order : orderList) {
      double totaleOrdine = order.getProducts().stream().mapToDouble(Product::getPrice).sum();
      totaleVendite.merge(order.getCustomer(), totaleOrdine, Double::sum);
    }
    return totaleVendite;
  }

  public static void stampaTotaleVenditePerCliente() {
    totaleVendite = calcolaTotaleVenditePerCliente();
    totaleVendite.forEach(
        (cliente, totale) -> {
          System.out.println("Cliente: " + cliente.getName() + " - Totale Vendite: " + totale);
        });
  }

  // esercizio 3

  public static List<Product> prodottiPiuCostosi() {
    int costosi = productList.stream().mapToInt(Product::getPrice).max().orElseThrow(NoSuchElementException::new);
    return productList.stream().filter(product -> product.getPrice() == costosi).toList();
  }

  public static void stampaProdottiPiuCostosi() {
    List<Product> costosi = prodottiPiuCostosi();
    System.out.println("Prodotti più costosi:");
    costosi.forEach(product -> System.out.println(product.getName() + " - Prezzo: " + product.getPrice()));
  }

  /* public static String writeFile(String str) throws IOException {
      File file = new File("txt/log.txt");
      FileUtils.writeStringToFile(file, str, "UTF-8");
      return str;
  }

  public static void readFile() {

  }*/

}
