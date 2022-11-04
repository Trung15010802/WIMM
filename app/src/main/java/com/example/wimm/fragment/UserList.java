package com.example.wimm.fragment;

public class UserList {
    private double eat;
    private double shopping;
    private double move;
    private double entertainment;
    private double health;
    private double other;

    public UserList() {
    }

    public UserList(double x, int type) {
        switch (type) {
            case 0:
                this.eat = x;
                break;
            case 1:
                this.shopping = x;
                break;
            case 2:
                this.move = x;
                break;
            case 3:
                this.health = x;
                break;
            case 4:
                this.entertainment = x;
                break;
            case 5:
                this.other = x;
                break;
        }
    }

    public double getEat() {
        return eat;
    }

    public void setEat(double eat) {
        this.eat = eat;
    }

    public double getShopping() {
        return shopping;
    }

    public void setShopping(double shopping) {
        this.shopping = shopping;
    }

    public double getMove() {
        return move;
    }

    public void setMove(double move) {
        this.move = move;
    }

    public double getEntertainment() {
        return entertainment;
    }

    public void setEntertainment(double entertainment) {
        this.entertainment = entertainment;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "eat=" + eat +
                ", shopping=" + shopping +
                ", move=" + move +
                ", entertainment=" + entertainment +
                ", health=" + health +
                ", other=" + other +
                '}';
    }
}
