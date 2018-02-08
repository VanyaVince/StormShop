package org.o7planning.javafx.model;

public class Good {

    public String name;
    public int count;
    public int price;

    public Good() {
    }

    public Good(String name, int count, int price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String nameString (){

        return this.name;
    }
    public String priceString (){

        return Integer.toString(this.price);
    }

    public String countString (){

        return Integer.toString(this.count);
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Name of product: " + this.name + ", count: " + this.count + ", price: " + this.price;

    }
}

