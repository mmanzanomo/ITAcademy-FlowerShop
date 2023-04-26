package org.flowershop.controller;

import org.flowershop.domain.products.Decoration;
import org.flowershop.domain.products.Flower;
import org.flowershop.domain.products.Product;
import org.flowershop.domain.products.Tree;
import org.flowershop.domain.tickets.Ticket;
import org.flowershop.domain.tickets.TicketDetail;
import org.flowershop.repository.ProductRepositoryTXT;
import org.flowershop.repository.TicketRepositoryTXT;
import org.flowershop.service.ProductService;
import org.flowershop.service.TicketService;
import org.flowershop.utils.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static org.flowershop.utils.Scan.Scan.askForInt;
import static org.flowershop.utils.Scan.Scan.askForString;


public class ProductController {


    public void productDataRequest(ProductService productService) {

        List<Product> products = loadProductos();
        products.forEach(productService::addProduct);

        Boolean exit = false;

        try {
            do {
                try {
                    int option = Menu.showTicketOptions();
                    System.out.println(option);
                    switch (option) {
                        case 1:
                            //TODO Adding new product
                            //addProductInTicketDetail(products, ticketDetails);
                            break;
                        case 2:

                            //TODO Update Product by ID
                            //modifyQuantityInTicketDetail(ticketDetails);
                            break;
                        case 3:
                            //TODO Deleting product
                            //removeProductInTicketDetail(ticketDetails);
                            break;
                        case 4:
                            //TODO Print all products
                            productService.getProducts();
                            break;
                        case 5:
                            //TODO Exit from Product Menu
                            exit = true;
                            break;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Incorrect option");
                }
            } while (!exit);
        } catch (Exception e) {
            System.out.println("Bye!!!");
        }


    }

    private void showProductsInTicket(List<TicketDetail> ticketDetails) {
        showObjectList(ticketDetails);
        DecimalFormat df = new DecimalFormat("#.##");
        Double total = ticketDetails.stream().mapToDouble(TicketDetail::getAmount).sum();
        System.out.println("Total ticket: " + df.format(total));
    }

    private void saveTicket(TicketService ticketService, List<TicketDetail> ticketDetails) throws IOException {
        Double total = ticketDetails.stream().mapToDouble(TicketDetail::getAmount).sum();
        Ticket ticket = new Ticket(ticketService.getLastTicketId(), new Date(), 1L, total, true);
        ticketDetails.stream().forEach(ticket::addTicketDetail);

        ticketService.addTicket(ticket);
        System.out.println(ticket);

        ticketDetails.clear();
    }

    private List<TicketDetail> modifyQuantityInTicketDetail(List<TicketDetail> ticketDetails) {

        System.out.println("Update Product in TicketDetail ...");
        showObjectList(ticketDetails);

        String reference = askForString("Input product reference to update");

        Optional<TicketDetail> findTicketDetail = ticketDetails.stream().filter(p -> p.getRef().equalsIgnoreCase(reference)).findFirst();

        if (findTicketDetail.isPresent()) {
            Integer quantity = askForInt("Input new quantity");

            findTicketDetail.get().setQuantity(quantity);
            findTicketDetail.get().setAmount(quantity * findTicketDetail.get().getPrice());
            System.out.println(findTicketDetail);
        } else System.out.println("This product isn't in the ticket");
        ;
        return ticketDetails;

    }

    private List<TicketDetail> removeProductInTicketDetail(List<TicketDetail> ticketDetails) {
        System.out.println("Remove Product in TicketDetail ...");
        showObjectList(ticketDetails);

        if (ticketDetails.size() > 0) {
            String reference = askForString("Input product reference to remove");

            //TODO Verify if product exits in ticket detail
            Optional<TicketDetail> findTicketDetail = ticketDetails.stream().filter(p -> p.getRef().equalsIgnoreCase(reference)).findFirst();

            if (findTicketDetail.isPresent()) {

                Iterator<TicketDetail> it = ticketDetails.iterator();
                while (it.hasNext()) {
                    String ref = it.next().getRef();
                    if (ref.equalsIgnoreCase(reference)) {
                        it.remove();
                        System.out.println("Product " + reference + " remove in ticket");
                    }
                }
            } else System.out.println("There isn't reference in ticket");
        } else System.out.println("There aren't products in ticket");
        return ticketDetails;

    }

    private List<TicketDetail> addProductInTicketDetail(List<Product> products, List<TicketDetail> ticketDetails) {
        System.out.println("Add Product in Order ...");
        showObjectList(products);
        String reference = askForString("Input product reference");


        //TODO Verify if product exits
        Optional<Product> findProduct = products.stream().filter(p -> p.getRef().equalsIgnoreCase(reference)).findFirst();

        if (findProduct.isPresent()) {
            //TODO Verify if product exits in ticket detail
            Optional<TicketDetail> findTicketDetail = ticketDetails.stream().filter(p -> p.getRef().equalsIgnoreCase(reference)).findFirst();

            if (findTicketDetail.isPresent()) {
                System.out.println("Product already exists in the ticket");
            } else {
                Integer quantity = askForInt("Input quantity");
                TicketDetail newTicketDetail = new TicketDetail(findProduct.get().getId(), findProduct.get().getRef(),
                        quantity, findProduct.get().getPrice(), quantity * findProduct.get().getPrice());
                ticketDetails.add(newTicketDetail);

                System.out.println(newTicketDetail);
            }
        }
        return ticketDetails;

    }

    private void showObjectList(List<?> list) {
        list.stream().forEach(System.out::println);

    }

    private List<Product> loadProductos() {
        Product product1 = new Tree("T001", "Abeto", 23.60, 1.60f);
        Product product2 = new Tree("T002", "Magnolia", 16.80, 0.30f);
        Product product3 = new Tree("T003", "Pino", 23.60, 1.60f);
        Product product4 = new Tree("T004", "Limonero", 16.80, 0.30f);
        Product product5 = new Tree("T005", "Almendro", 23.60, 1.60f);
        Product product6 = new Tree("T006", "Manzano", 16.80, 0.30f);

        Product product7 = new Flower("F001", "Rosa", 23.60, "blanco");
        Product product8 = new Flower("F002", "Rosa", 23.60, "rojo");
        Product product9 = new Flower("F003", "Rosa", 23.60, "rosa");
        Product product10 = new Flower("F004", "Margarita", 23.60, "blanca/amarilla");
        Product product11 = new Flower("F005", "Lirio", 23.60, "blanca");
        Product product12 = new Flower("F006", "Lirio", 23.60, "amarillo");
        Product product13 = new Flower("F007", "Amapola", 23.60, "roja");
        Product product14 = new Flower("F008", "Amapola", 23.60, "amarilla");

        Product product15 = new Decoration("D001", "Enredadera de pared", 20.95, "Madera");
        Product product16 = new Decoration("D002", "Centro gris", 2.95, "Madera");
        Product product17 = new Decoration("D003", "Adorno flor", 03.95, "Madera");
        Product product18 = new Decoration("D004", "Palo", 2.0, "Plastico");
        Product product19 = new Decoration("D005", "Vaso flor", 4.0, "Madera");
        Product product20 = new Decoration("D006", "Macetero", 7.0, "Plastico");

        List<Product> products = Arrays.asList(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10,
                product11, product12, product13, product14, product15, product16, product17, product18, product19, product20);

        return products;
    }


}
