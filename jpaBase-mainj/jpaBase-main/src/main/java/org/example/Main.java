package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            //CREAR FACTURA Y SETEAR LOS ATRIBUTOS
            Factura factura1 = new Factura();
            factura1.setNumero(12);
            factura1.setFecha("03/09/24");

            //CREAR DOMICILIO Y SE PASAN COMO ATRIBUTOS NOMBRECALLE Y NUMERO
            Domicilio dom = new Domicilio("Godoy Cruz" , 9895);

            //CREAR CLIENTE Y SE SETEAN ATRIBUTOS
            Cliente cliente = new Cliente("Juliana" , "Perez" , 45144862);
            cliente.setDomicilio(dom);
            dom.setCliente(cliente);

            //SETEAMOS A FACTURA EL CLIENTE
            factura1.setCliente(cliente);

            //CREAR CATEGORIAS
            Categoria liquidos = new Categoria("Liquidos");
            Categoria gaseosa = new Categoria("Gaseosa");
            Categoria comida = new Categoria("Comida");

            //CREAR ARTICULOS
            Articulo articulo1 = new Articulo(100, "Sprite" , 2000);
            Articulo articulo2 = new Articulo(50 , "galletas" , 500);

            //SE ASIGNAN LOS ARTICULOS A LAS CATEGORIAS Y LAS CATEGORIAS A LOS ARTICULOS
            articulo1.getCategorias().add(liquidos);
            articulo1.getCategorias().add(gaseosa);
            liquidos.getArticulos().add(articulo1);
            gaseosa.getArticulos().add(articulo1);

            articulo2.getCategorias().add(comida);
            comida.getArticulos().add(articulo2);

            //detalle 1
            DetalleFactura detalle1 = new DetalleFactura();

            detalle1.setArticulo(articulo1);
            detalle1.setCantidad(2);
            detalle1.setSubtotal(4000);

            //BIRIDICCIONALIDADES detalle 1
            articulo1.getDetalle().add(detalle1);
            factura1.getDetalles().add(detalle1);
            detalle1.setFactura(factura1);

            //nuevo detalle
            DetalleFactura detalle2 = new DetalleFactura();

            detalle2.setArticulo(articulo2);
            detalle2.setCantidad(1);
            detalle2.setSubtotal(500);

            //BIRIDICCIONALIDADES detalle 2
            articulo2.getDetalle().add(detalle2);
            factura1.getDetalles().add(detalle2);
            detalle2.setFactura(factura1);

            //total de la factura
            factura1.setTotal(4500);

            entityManager.persist(factura1);

            entityManager.flush();

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }



        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();
    }
}
