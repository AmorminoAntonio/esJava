package D3_S2_StreamsLambda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GestioneStream {
    private static List<Product> productList = new ArrayList<Product>();
    private static List<Customer> customerList = new ArrayList<Customer>();
    private static List<Order> orderList = new ArrayList<Order>();

    public static void main(String[] args) {
        // Inizializzo i dati
        createProductList();
        createCustomerList();
        createOrderList();

        System.out.println("Products");
        productList.forEach(ele -> System.out.println(ele));
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
        getTierProducts().forEach(System.out::println);
    }

    public static List<Product> getBooks() {
        List<Product> listaBooks = productList.stream()
                .filter(product -> product.getCategory().equals("Books") && product.getPrice() > 100)
                .collect(Collectors.toList());
        return listaBooks;
    }

    public static List<Order> getBabyOrders() {
        List<Order> listaBaby = orderList.stream()
                .filter(order -> order.getProducts()
                        .stream().anyMatch(product -> product.getCategory().equals("Baby")))
                .toList();
        return listaBaby;
    }

    public static List<Product> getBoys() {
        List<Product> listaBoys = productList.stream()
                .filter(p -> p.getCategory().equals("Boys"))
                .map(product -> {
                    product.setPrice(product.getPrice() * 0.90);
                    return product;
                }).toList();
        return listaBoys;
    }

    public static List<Product> getTierProducts() {
        List<Order> filterOrder = orderList.stream()
                .filter(order -> order.getCustomer().getTier() == 2
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