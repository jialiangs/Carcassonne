package edu.cmu.cs.cs214.hw4.core;

// CHECKSTYLE:OFF
public class JSONConfigReader {
  public class JSONItem {
    public char item;
    public int quantity;
  }
  public class JSONGroceryStore{
    public String name;
    public JSONItem[] inventory;
  }
}
