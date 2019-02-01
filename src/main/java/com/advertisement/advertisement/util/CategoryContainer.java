package com.advertisement.advertisement.util;

public class CategoryContainer {
    private double score;
    private String categoryName;


    public CategoryContainer() {
    }

    public CategoryContainer(double score, String categoryName) {
        this.score = score;
        this.categoryName = categoryName;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryContainer{" +
                "score=" + score +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
