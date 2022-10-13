import java.util.Scanner;
import java.util.Vector;
import java.util.Map;

public class Functionalities {

    public static void CreateEntity(int totalEntitiesAdded, Vector<Manufacturer> manufacturers,
            Vector<Product> products,
            Vector<Customer> customers,
            Vector<DeliveryAgent> deliveryAgents,
            Vector<Shop> shops) {
        Scanner sc = new Scanner(System.in);
        char input = 'Z';
        System.out.print("\nChoose an Entity: \n" +
                "M: Manufacturer\n" +
                "C: Customer\n" +
                "P: Product\n" +
                "S: Shops and Warehouses\n" +
                "D: Delivery Agent\n" +
                "Enter the code: ");
        input = sc.next().charAt(0);
        switch (input) {
            case 'M': {
                Manufacturer m;
                m = Manufacturer.CreateManufacturer(totalEntitiesAdded);
                manufacturers.add(m);
                totalEntitiesAdded++;
                break;
            }
            case 'C': {
                Customer m;
                m = Customer.createCustomer(totalEntitiesAdded);
                customers.add(m);
                totalEntitiesAdded++;
                break;
            }
            case 'P': {
                Product m;
                m = Product.createProduct(totalEntitiesAdded);
                Scanner s = new Scanner(System.in);
                int in;
                Manufacturer.PrintListofManufacturers(manufacturers);
                System.out.print("Enter the manufacturer id: ");
                in = s.nextInt();
                // p.manu.id = input;
                System.out.println("Product " + m.name + " has been created with id: " + m.id + "\n");
                products.add(m);
                int flag = 0;
                for (Manufacturer i : manufacturers) {
                    if (i.id == in) {
                        m.manu = i;
                        i.productsManufactured.add(m); // the product is added to the manufactures' products list
                        i.nproducts++;
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    System.out.print("Incorrect manufacturer id entered\n");
                    break;
                }

                totalEntitiesAdded++;
                break;
            }

            case 'S': {
                Shop m;
                m = Shop.createShop(totalEntitiesAdded);
                shops.add(m);
                totalEntitiesAdded++;
                break;
            }

            case 'D': {
                DeliveryAgent m;
                m = DeliveryAgent.cDeliveryAgent(totalEntitiesAdded);
                deliveryAgents.add(m);
                totalEntitiesAdded++;
            }

            default: {
                System.out.print("Wrong code entered\n");
            }
        }
        // switch case is completed
        // sc.close();
    }

    public static void PrintEntity(int totalEntitiesAdded, Vector<Manufacturer> manufacturers,
            Vector<Product> products,
            Vector<Customer> customers,
            Vector<DeliveryAgent> deliveryAgents,
            Vector<Shop> shops) {
        Scanner sc = new Scanner(System.in);
        char input = 'Z';
        System.out.print("\nChoose an Entity: \n" +
                "M: Manufacturer\n" +
                "C: Customer\n" +
                "P: Product\n" +
                "S: Shops and Warehouses\n" +
                "D: Delivery Agent\n" +
                "Enter the code: ");
        input = sc.next().charAt(0);
        switch (input) {
            case 'M': {
                Manufacturer.PrintListofManufacturers(manufacturers);
                break;
            }
            case 'C': {
                Customer.printListofCustomers(customers);
                break;
            }
            case 'P': {
                Product.PrintListofProducts(products);
                break;
            }

            case 'S': {
                Shop.printListofShops(shops);
                break;
            }

            case 'D': {
                DeliveryAgent.printDeliveryAgents(deliveryAgents);
                break;
            }

            default: {
                System.out.print("Wrong code entered\n");
            }
        }
        // switch case is completed
        // sc.close();
    }

    public static void DeleteEntity(int totalEntitiesAdded, Vector<Manufacturer> manufacturers,
            Vector<Product> products,
            Vector<Customer> customers,
            Vector<DeliveryAgent> deliveryAgents,
            Vector<Shop> shops) {
        Scanner sc = new Scanner(System.in);
        char input = 'Z';
        System.out.print("\nChoose an Entity: \n" +
                "M: Manufacturer\n" +
                "C: Customer\n" +
                "P: Product\n" +
                "S: Shops and Warehouses\n" +
                "D: Delivery Agent\n" +
                "Enter the code: ");
        input = sc.next().charAt(0);
        switch (input) {
            case 'M': {
                Manufacturer.DeleteManufacturer(manufacturers);
                break;
            }
            case 'C': {
                Customer.deleteCustomer(customers);
                break;
            }
            case 'P': {
                Product p = Product.deleteProduct(products);
                for (Manufacturer m : manufacturers) {
                    if (p.manu == m) {
                        m.productsManufactured.remove(p); // the product is removed from the Manufacturers list
                        break;
                    }
                }
                // TO ADD:
                // The product is removed from the shops as well
                break;
            }

            case 'S': {
                break;
            }

            case 'D': {
                DeliveryAgent.DeleteDeliveryAgent(deliveryAgents);
                break;
            }

            default: {
                System.out.print("Wrong code entered\n");
            }
        }
        // switch case is completed
        // sc.close();
    }

    public static void ChangeManufacturer(Vector<Product> products, Vector<Manufacturer> manufacturers) {
        System.out.print("Enter the product id: ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        for (Product p : products) {
            if (p.id == input) {
                for (Manufacturer m : manufacturers) {
                    if (m == p.manu) {
                        m.productsManufactured.remove(p);
                        break;
                    }
                }
                Manufacturer.PrintListofManufacturers(manufacturers);
                System.out.print("Enter the new id of manufacturer: ");
                int newid = sc.nextInt();
                for (Manufacturer m : manufacturers) {
                    if (m.id == newid) {
                        m.productsManufactured.add(p);
                        p.manu = m;
                        System.out.println(
                                "Manufacturer of product " + p.name + " successfully changed to " + m.name + "\n");
                        break;
                    }
                }
                break;
            }
        }
        // sc.close();
    }

    public static void ListPurchases(Vector<Customer> customers) {
        Customer.printListofCustomers(customers);
        System.out.print("Enter the customer id to show purchase history: ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        for (Customer c : customers) {
            if (input == c.id) {
                Product.PrintListofProducts(c.purchased);
                break;
            }
        }
        // sc.close();
    }

    public static void ListProductsbyManu(Vector<Manufacturer> manufacturers) {
        Manufacturer.PrintListofManufacturers(manufacturers);
        System.out.print("Enter manufacturer id to show list of products manufactured by it: ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        for (Manufacturer m : manufacturers) {
            if (m.id == input) {
                Product.PrintListofProducts(m.productsManufactured);
                break;
            }
        }
        // sc.close();
    }

    public static void ListInventory(Vector<Shop> shops, Vector<Product> products) {
        Shop.printListofShops(shops);
        System.out.print("Enter shop id to print its inventory: ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        for (Shop s : shops) {
            if (s.id == input) {
                System.out.print("Inventory of shop " + s.name + ":\n");
                for (Map.Entry<Product, Integer> inv : s.inventory.entrySet()) {
                    System.out.println("Product name: " + inv.getKey().name + "\tID: " + inv.getKey().id
                            + "\tQuantity: " + inv.getValue() + "\n");
                }
                break;
            }
        }
        // sc.close();
    }

    public static void AddCopiesToShops(Vector<Product> products, Vector<Shop> shops) {
        Shop.printListofShops(shops);
        System.out.print("Enter id of shop: ");
        Scanner sc = new Scanner(System.in);
        int sid = sc.nextInt();
        Product.PrintListofProducts(products);
        System.out.print("Enter id of product: ");
        int pid = sc.nextInt();
        System.out.println("Enter quantity of product: ");
        int q = sc.nextInt();
        for (Shop s : shops) {
            if (s.id == sid) {
                int f = 0;
                for (Map.Entry<Product, Integer> inv : s.inventory.entrySet()) {
                    if (pid == inv.getKey().id) {
                        // if the product already exists in inventory, its quantity is added
                        s.inventory.put(inv.getKey(), inv.getValue() + q);
                        f = 1;
                        break;
                    }
                }
                if (f == 0) {
                    for (Product p : products) {
                        // implies product doesnt exist in the shop so it is added
                        if (p.id == pid) {
                            s.inventory.put(p, q);
                        }
                    }
                }
            }
        }
        System.out.print("Products added to the shop successfully\n");
        // sc.close();
        return;
    }

    static void AddanOrder(Vector<Customer> customers, Vector<Shop> shops, Vector<Product> products,
            Vector<DeliveryAgent> deliveryAgents) {

        Customer.printListofCustomers(customers);
        System.out.print("Enter customer id: ");
        Scanner sc = new Scanner(System.in);
        int cid = sc.nextInt();
        int czipcode = 0;
        for (Customer c : customers) {
            if (c.id == cid) {
                czipcode = c.zipcode;
            }
        }
        Product.PrintListofProducts(products);
        System.out.print("Enter the product id: ");
        int pid = sc.nextInt();
        if (ProcessOrder(products, customers, deliveryAgents, shops, pid, czipcode) == true) {
            for (Customer c : customers) {
                if (c.id == cid) {
                    c.norders++;
                    for (Product p : products) {
                        if (p.id == pid) {
                            c.purchased.add(p);
                        }
                    }
                }
            }
        }

        else {
            System.out.print("The order cannot be processed\n");
        }

        // sc.close();
    }

    static boolean ProcessOrder(Vector<Product> products,
            Vector<Customer> customers,
            Vector<DeliveryAgent> deliveryAgents,
            Vector<Shop> shops,
            int pid,
            int czipcode) {
        int shopavailable = 0; // this indicates the availability of product in nearby shops
        for (Shop s : shops) {
            if (s.zipcode == czipcode) { // shops in the same zipcode
                for (Map.Entry<Product, Integer> inv : s.inventory.entrySet()) {
                    if (pid == inv.getKey().id) {
                        int temp = inv.getValue();
                        // nearby shop has the product available
                        shopavailable = 1;
                        // if no delivery agent can be found hence order cant be processed
                        if (temp == 1) { // the product will be finished so it is removed
                            s.inventory.remove(inv.getKey());
                        } else { // the products quantity decreases by 1
                            s.inventory.put(inv.getKey(), temp - 1);
                        }
                        break;
                    }
                }
            }
        }

        if (shopavailable == 0) {
            return false;
        }

        // shop has product
        int deliveryAgentavailable = 0;
        DeliveryAgent valet = null; // to
        int minorders = 1000000; // to get minimum number of orders
        for (DeliveryAgent da : deliveryAgents) {
            if (da.zipcode == czipcode) {
                deliveryAgentavailable = 1;
                if (minorders > da.ndelivered) {
                    minorders = da.ndelivered;
                    valet = da;
                }
            }
        }

        if (deliveryAgentavailable == 0) {
            return false;
        }

        for (DeliveryAgent da : deliveryAgents) {
            if (valet.id == da.id) {
                da.ndelivered++; // order is delivered
            }
        }

        System.out.println("The given order has been successfully delivered by " + valet.name + "\n");
        return true;

    }
}
