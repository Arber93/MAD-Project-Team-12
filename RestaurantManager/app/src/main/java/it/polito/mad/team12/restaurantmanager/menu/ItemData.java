package it.polito.mad.team12.restaurantmanager.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemData implements Comparable<ItemData>{
    public static final String FIRST_COURSES = "first courses";
    public static final String SECOND_COURSES = "second courses";
    public static final String SIDE_DISHES = "side dishes";
    public static final String DRINKS = "drinks";
    public static final String DESSERTS = "desserts";
    public static final String OTHERS = "others";

    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Set<String> links;

    /*
     *  If characteristics like the following increase in number than an
     * integer with bitwise operations could be used to save space.
     */
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;

    /*
     *   This field is used to indicate whether this item can be prepared
     * by the restaurant at this time.
     */
    private boolean available;

    /*
     *  If true that means that an image of this item can be found in the Firebase DB.
     */
    private boolean hasImage;

    public ItemData() {
        this.name = null;
        this.description = null;
        this.available = true;
        this.vegetarian = false;
        this.vegan = false;
        this.glutenFree = false;
        this.hasImage = false;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {

        if(price != null) {
            return price.setScale(2, RoundingMode.HALF_EVEN);
        }

        return null;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean getHasImage() {
        return this.hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLinks(Map<String, Boolean> links) {
        this.links = links.keySet();
    }

    public Map<String, Boolean> getLinks() {
        Map<String, Boolean> result;

        if(links == null) {
            return null;
        }

        result = new HashMap<>();

        for(String s: links) {
            result.put(s, true);
        }

        return result;
    }

    @Override
    public int compareTo(ItemData another) {
        return this.name.compareTo(another.name);
    }

    @Override
    public boolean equals (Object another) {
        ItemData other;

        try {
            other = (ItemData) another;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }

        return this.name.equals(other.getName());
    }

    public boolean addLink(String name) {
        return links.add(name);
    }

    public boolean removeLink(String name) {
        return links.remove(name);
    }

    public void passLinks(Collection<String> links) {
        if(this.links == null) {
            this.links = new HashSet<>();
        } else {
            this.links.clear();
        }

        if(links != null) {
            this.links.addAll(links);
        }
    }

    public int hashCode(){
        return this.getName().hashCode();
    }
}
