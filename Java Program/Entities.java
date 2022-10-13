import java.util.Scanner;
import java.util.Vector;
import java.util.HashMap;

public class Entities {
    int id;
    String name;

    public Entities(Scanner Obj, int totalEntitiesAdded) {
        System.out.print("Enter the name: ");
        this.name = Obj.nextLine();
        this.id = totalEntitiesAdded;
    }

    void printEnt() {
        System.out.println("\nName: " + name + "\tId: " + id);
    }
}

class Manufacturer extends Entities {
    Vector<Product> productsManufactured = new Vector<Product>(); // initally the list is empty
    int nproducts; // number of products the manufacturer can manufacture

    public Manufacturer(Scanner Obj, int totalEntitiesAdded) {
        super(Obj, totalEntitiesAdded);
        this.nproducts = 0;
    }

    public static Manufacturer CreateManufacturer(int totalEntitiesAdded) {
        Scanner Obj = new Scanner(System.in);
        Manufacturer m = new Manufacturer(Obj, totalEntitiesAdded);
        System.out.println("Manufacturer " + m.name + " has been created with id: " + m.id + "\n");
        return m;
    }

    public static void DeleteManufacturer(Vector<Manufacturer> manufacturers) {
        PrintListofManufacturers(manufacturers);
        Scanner obj = new Scanner(System.in);
        System.out.print("Enter id of the manufacturer to be deleted: ");
        int input = obj.nextInt();
        for (Manufacturer m : manufacturers) {
            if (input == m.id) {
                manufacturers.remove(m);
                System.out.println("Manufacturer " + m.name + "with id: " + m.id + " has been deleted\n");
                break;
            }
        }
        // obj.close();
        return;
    }

    public static void PrintListofManufacturers(Vector<Manufacturer> manufacturers) {
        System.out.print("\nList of Manufacturers:\n");
        for (Manufacturer m : manufacturers) {
            m.printEnt();
        }
    }
}

class Product extends Entities {
    Manufacturer manu;

    public Product(Scanner Obj, int totalEntitiesAdded) {
        super(Obj, totalEntitiesAdded);
    }

    public static Product createProduct(int totalEntitiesAdded) {
        Scanner sc = new Scanner(System.in);
        Product p = new Product(sc, totalEntitiesAdded);
        return p;
    }

    public static Product deleteProduct(Vector<Product> products) {
        PrintListofProducts(products);
        Scanner obj = new Scanner(System.in);
        System.out.print("Enter id of the product to be deleted: ");
        int input = obj.nextInt();
        for (Product p : products) {
            if (input == p.id) {
                products.remove(p);
                System.out.println("Product " + p.name + " with id: " + p.id + " has been removed\n");
                // obj.close();
                return p;
            }
        }
        // obj.close();
        return null;
    }

    public static void PrintListofProducts(Vector<Product> products) {
        System.out.print("\nList of Products:\n");
        for (Product p : products) {
            p.printEnt();
        }
    }
}

class Customer extends Entities {
    int zipcode;
    Vector<Product> purchased = new Vector<Product>(); // list of product purchased
    int norders; // number of products ordered

    // this initialises the customer with basic data like name, id and zipcode
    public Customer(Scanner Obj, int totalEntitiesAdded) {
        super(Obj, totalEntitiesAdded);
        this.norders = 0;
        System.out.print("Enter the zipcode of the customer: ");
        this.zipcode = Obj.nextInt();
    }

    static Customer createCustomer(int totalEntitiesAdded) {
        Scanner sc = new Scanner(System.in);
        Customer c = new Customer(sc, totalEntitiesAdded);
        // the created customer has no orders placed yet
        System.out.println("Customer " + c.name + " has been created with id: " + c.id + "\n");
        return c;
    }

    static void deleteCustomer(Vector<Customer> customers) {
        printListofCustomers(customers);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the id of the customer to be deleted: ");
        int input = sc.nextInt();
        for (Customer c : customers) {
            if (c.id == input) {
                customers.remove(c);
                System.out.println("Customer " + c.name + " with id: " + c.id + " has been deleted\n");
                break;
            }
        }
        // sc.close();
        return;
    }

    static void printListofCustomers(Vector<Customer> customers) {
        System.out.print("\nList of Customers: \n");
        for (Customer c : customers) {
            System.out.println("Customer: " + c.name + "\tId: " + c.id + "\tZipcode: " + c.zipcode + "\n");
        }
        return;
    }
}

class Shop extends Entities {
    int zipcode;
    HashMap<Product, Integer> inventory = new HashMap<Product, Integer>();

    public Shop(Scanner Obj, int totalEntitiesAdded) {
        super(Obj, totalEntitiesAdded);
        System.out.print("Enter the zipcode of the shop: ");
        this.zipcode = Obj.nextInt();
    }

    // initially the shop has no inventory
    static Shop createShop(int totalEntitiesAdded) {
        Scanner sc = new Scanner(System.in);
        Shop s = new Shop(sc, totalEntitiesAdded);
        System.out.println("Shop " + s.name + " has been created with id: " + s.id + "\n");
        // the created shop has currently no inventory
        return s;
    }

    static void printListofShops(Vector<Shop> shops) {
        System.out.print("\nList of Shops\n");
        for (Shop s : shops) {
            s.printEnt();
        }
        return;
    }

}

class DeliveryAgent extends Entities {
    int zipcode;
    int ndelivered; // number of products delivered

    public DeliveryAgent(Scanner Obj, int totalEntitiesAdded) {
        super(Obj, totalEntitiesAdded);
        System.out.print("Enter the zipcode of the delivery agent: ");
        this.zipcode = Obj.nextInt();
        this.ndelivered = 0;
    }

    static DeliveryAgent cDeliveryAgent(int totalEntitiesAdded) {
        Scanner Obj = new Scanner(System.in);
        DeliveryAgent da = new DeliveryAgent(Obj, totalEntitiesAdded);
        System.out.println("Delivery Agent " + da.name + " has been created with id: " + da.id + "\n");
        return da;
    }

    static void printDeliveryAgents(Vector<DeliveryAgent> deliveryAgents) {
        System.out.print("\nList of Delivery Agents: \n");
        for (DeliveryAgent d : deliveryAgents) {
            d.printEnt();
        }
    }

    static void DeleteDeliveryAgent(Vector<DeliveryAgent> deliveryAgents) {
        printDeliveryAgents(deliveryAgents);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the id of the delivery agent to be deleted: ");
        int input = sc.nextInt();
        for (DeliveryAgent d : deliveryAgents) {
            if (d.id == input) {
                deliveryAgents.remove(d);
                System.out.println("Delivery Agent " + d.name + " with id: " + d.id + " has been deleted\n");
                break;
            }
        }
        // sc.close();
        return;
    }
}
