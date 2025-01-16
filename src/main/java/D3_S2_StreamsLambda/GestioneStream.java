package D3_S2_StreamsLambda;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;

public class GestioneStream {
  private static List<Product> productList = new ArrayList<Product>();
  private static List<Customer> customerList = new ArrayList<Customer>();
  private static List<Order> orderList = new ArrayList<Order>();

  public static void main(String[] args) {
    // Inizializzo i dati
    createProductList();
    createCustomerList();
    createOrderList();
    /*
        System.out.println("Products");
        productList.forEach(ele -> System.out.println(ele));
        System.out.println("Customer");
        customerList.forEach(System.out::println);
        System.out.println("Order");
        orderList.forEach(System.out::println);
    */
    // Esercizio 1
    System.out.println("--- Esercizio 1 ---");
    getOrderByClient()
        .forEach(
            (customer, orders) -> {
              System.out.println("Cliente: " + customer.getName() + " Ordini: " + orders.size());
              System.out.println(orders);
            });
    // Esercizio 2
    System.out.println("--- Esercizio 2 ---");
    getOrderByClientTotal()
        .forEach(
            (customer, total) -> {
              System.out.println("Cliente: " + customer.getName() + " Totale: " + total);
            });
    // Esercizio 3
    System.out.println("--- Esercizio 3 ---");
    getMostExpensiveProducts().forEach(System.out::println);
    // Esercizio 4
    System.out.println("--- Esercizio 4 ---");
    System.out.println(getOrdersAverage());
    // Esercizio 5
    System.out.println("--- Esercizio 5 ---");
    System.out.println(getCategoriesAndTotals());
    // Esercizio 6
    System.out.println("--- Esercizio 6 ---");
    try {
      saveToDisk();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    // Esercizio 7
    System.out.println("--- Esercizio 7 ---");
    try {
      loadFromDisk().forEach(product -> System.out.println(product));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  // Metodo 1
  public static Map<Customer, List<Order>> getOrderByClient() {
    return orderList.stream().collect(Collectors.groupingBy(Order::getCustomer));
  }

  // Metodo 2
  public static Map<Customer, Double> getOrderByClientTotal() {
    return orderList.stream()
        .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(Order::getTotal)));
  }

  // Metodo 3
  public static List<Product> getMostExpensiveProducts() {
    return productList.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).limit(3).toList();
  }

  // Metodo 4
  public static double getOrdersAverage() {
    return orderList.stream().mapToDouble(Order::getTotal).average().orElse(0.0);
  }

  // Metodo 5
  public static Map<String, Double> getCategoriesAndTotals() {
    return productList.stream()
        .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble((Product::getPrice))));
  }

  // Metodo 6
  public static void saveToDisk() throws IOException {
    String toWrite = "";
    for (Product product : productList) {
      toWrite += product.getName() + "@" + product.getCategory() + "@" + product.getPrice() + "#";
    }
    File file = new File("txt/products.txt");
    FileUtils.writeStringToFile(file, toWrite, "UTF-8");
  }

  // Metodo 7
  public static List<Product> loadFromDisk() throws IOException {
    File file = new File("txt/products.txt");
    String fileString = FileUtils.readFileToString(file, "UTF-8");
    List<String> splitElementiString = Arrays.asList(fileString.split("#"));
    return splitElementiString.stream()
        .map(
            stringa -> {
              String[] productInfos = stringa.split("@");
              return new Product(0, productInfos[0], productInfos[1], Double.parseDouble(productInfos[2]));
            })
        .toList();
  }

  public static void createProductList() {
    // Books - Baby - Boys
    Product p1 = new Product(1, "Iphone", "Smartphone", 1000);
    Product p2 = new Product(2, "ABC", "Books", 127.15);
    Product p3 = new Product(3, "Pannolini", "Baby", 5.8);
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
}
