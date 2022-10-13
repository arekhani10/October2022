import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Scanner Obj = new Scanner(System.in);
        int op = -1;
        Vector<Product> products = new Vector<Product>();
        Vector<Customer> customers = new Vector<Customer>();
        Vector<Manufacturer> manufacturers = new Vector<Manufacturer>();
        Vector<DeliveryAgent> deliveryAgents = new Vector<DeliveryAgent>();
        Vector<Shop> shops = new Vector<Shop>();
        int totalEntitiesAdded = 0;

        while (true) {
            System.out.print("\nSELECT ONE OF THE OPTIONS BELOW:\n\n" +
                    "1. Create Entity\n" +
                    "2. Delete Entity\n" +
                    "3. Print Entity\n" +
                    "4. Change manufacturer of a product\n" +
                    "5. Add copies of a product to a shop\n" +
                    "6. Add an order of a product from a customer\n" +
                    "7. List all the purchases made by a customer\n" +
                    "8. List inventory of a shop\n" +
                    "9. List products made by a manufacturer\n" +
                    "0. Exit\n\n" +
                    "Enter your choice: ");
            if (!Obj.hasNext()) {
                System.out.print("Error");
                return;
            }

            op = Obj.nextInt();
            switch (op) {
                case 1:
                    Functionalities.CreateEntity(totalEntitiesAdded, manufacturers, products, customers, deliveryAgents,
                            shops);
                    totalEntitiesAdded++;
                    break;
                case 2:
                    Functionalities.DeleteEntity(totalEntitiesAdded, manufacturers, products, customers, deliveryAgents,
                            shops);
                    break;
                case 3:
                    Functionalities.PrintEntity(totalEntitiesAdded, manufacturers, products, customers, deliveryAgents,
                            shops);
                    break;
                case 4:
                    Functionalities.ChangeManufacturer(products, manufacturers);
                    break;
                case 5:
                    Functionalities.AddCopiesToShops(products, shops);
                    break;
                case 6:
                    Functionalities.AddanOrder(customers, shops, products, deliveryAgents);
                    break;
                case 7:
                    Functionalities.ListPurchases(customers);
                    break;
                case 8:
                    Functionalities.ListInventory(shops, products);
                    break;
                case 9:
                    Functionalities.ListProductsbyManu(manufacturers);
                    break;
                case 0:
                    System.out.print("\nThe program has ended\n");
                    return;
                default:
                    System.out.print("Please enter correct code\n");
                    break;
            }
        }

    }
}
